package com.example.configuration;

import com.example.api.AiCodeHelperService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AiCodeHelperServiceFactory {
    @Resource
    private McpToolProvider mcpToolProvider;
    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;
    @Resource
    private StreamingChatModel qwenStreamingChatModel;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private InterviewQuestionTool interviewQuestionTool;

    @Bean
    public AiCodeHelperService aiCodeHelperService () {

        // ------ RAG ------
        // 1. 加载文档
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
        // 2. 文档切割：将每个文档按句子进行分割，最大 1000 字符，每次重叠最多 200 个句子
        DocumentBySentenceSplitter documentBySentenceSplitter = new DocumentBySentenceSplitter(512, 100);
        // 3. 自定义文档加载器
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentBySentenceSplitter)
                // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
                // 使用指定的向量模型
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        // 加载文档
        ingestor.ingest(documents);
        // 4. 自定义内容查询器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(qwenEmbeddingModel)
                .maxResults(5) // 最多 5 个检索结果
                .minScore(0.75) // 过滤掉分数小于 0.75 的结果
                .build();
        // 构造 AI Service


        AiCodeHelperService aiCodeHelperService = AiServices.builder(AiCodeHelperService.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .maxMessages(20)
                        .chatMemoryStore(new PersistentChatMemoryStore(stringRedisTemplate))
                        .id( id)
                        .build())
                .contentRetriever(contentRetriever) // RAG 检索增强生成
                .toolProvider(mcpToolProvider)// MCP 工具调用
                .tools(interviewQuestionTool)
                .build();


        return aiCodeHelperService;
    }
}
