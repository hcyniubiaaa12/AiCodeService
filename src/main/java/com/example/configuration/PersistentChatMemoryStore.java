package com.example.configuration;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PersistentChatMemoryStore implements ChatMemoryStore {
    private final StringRedisTemplate redisTemplate;



    public PersistentChatMemoryStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;



    }


    @Override
    public List<ChatMessage> getMessages(Object id) {
        String json = redisTemplate.opsForValue().get("chat:messages:" + id.toString());
        if (json == null || json.isEmpty()) {
            return List.of();
        }
      return ChatMessageDeserializer.messagesFromJson(json);


    }

    @Override
    public void updateMessages(Object id, List<ChatMessage> list) {
        String key = "chat:messages:" + id.toString();
        String messages = ChatMessageSerializer.messagesToJson(list);

        redisTemplate.opsForValue().set(key, messages,2, TimeUnit.MINUTES);

    }

    @Override
    public void deleteMessages(Object o) {
        redisTemplate.delete("chat:messages:" + o.toString());

    }
}
