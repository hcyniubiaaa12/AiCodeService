package com.example;


import com.example.api.AiCodeHelperService;
import com.example.api.Friend;
import com.example.configuration.ImageModelFactory;
import com.example.configuration.PersistentChatMemoryStore;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class Demo7ApplicationTests {
    @Autowired
    private ChatModel chatModel;
    @Resource
    private AiCodeHelperService aiCodeHelperService;
    @Autowired
    private ImageModelFactory imageModelFactory;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Test
    void contextLoads() {

        Friend friend = AiServices.builder(Friend.class)
                .chatModel(chatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .id(id)
                        .maxMessages(20)
                        .chatMemoryStore(new PersistentChatMemoryStore(stringRedisTemplate))
                        .build())
                .build();
        System.out.println("=== 娜美和路飞对话 ===");
        String r1 = friend.chat("娜美", "船长，天气怎么样？");
        System.out.println("路飞: " + r1);

        String r2 = friend.chat("娜美", "我们能到达新世界吗？");
        System.out.println("路飞: " + r2);
        String r3 = friend.chat("娜美", "我需要帮助，请给我一个助手");
        System.out.println("路飞: " + r3);
        String r4 = friend.chat("山治", "绿藻头呢");
        System.out.println("路飞: " + r4);




    }

    @Test
    void test1() {
        String template = """
                你是一个资深的专家,什么都会无所不能。
                请用中文回答用户的问题，控制在200字以内。
                    
                用户问题：{{question}}
                """;

        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Prompt question = promptTemplate.apply(Map.of("question", "草帽海贼团在和治国发生了什么？,索隆打败了谁结局是什么"));
        System.out.println(question);


    }

    @Test
    public void asyncCall() {
        System.out.println("---create task----");
        String taskId = imageModelFactory.createAsyncTask("生成火星演唱会的图");
        System.out.println("---wait task done then return image url----");
        imageModelFactory.waitAsyncTask(taskId);
    }



    @Test
    void test() {
        String s = String.valueOf(aiCodeHelperService.chat("1","你好"));
        System.out.println(s);
    }

}
