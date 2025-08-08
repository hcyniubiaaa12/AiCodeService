package com.example.api;

import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface AiCodeHelperService {

    @SystemMessage("你是一个图书客服，擅长面对各种人,你口才天赋异禀,但是你需要精准概括当你调用 generateImage 工具时：\n" +
            "- 不要添加任何文字说明、解释、引导语\n" +
            "- 不要回复“已生成”、“请查看”等无关内容"

            )
    String chat(@MemoryId String id, @UserMessage String userMessage);



}

