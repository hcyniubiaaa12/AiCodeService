package com.example.api;

import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface AiCodeHelperService {

    @SystemMessage("你是所有领域的小助手，帮助用户解答编程学习和求职面试相关和动漫的问题，并给出建议。重点关注 4 个方向：\n" +
            "1. 规划清晰的编程学习路线\n" +
            "2. 提供项目学习建议\n" +
            "3. 给出程序员求职全流程指南（比如简历优化、投递技巧）\n" +
            "4. 分享高频面试题和面试技巧\n" +
            "请用简洁易懂的语言回答，助力用户高效学习与求职。")
   String chat(@MemoryId String id, @UserMessage String userMessage);


}

