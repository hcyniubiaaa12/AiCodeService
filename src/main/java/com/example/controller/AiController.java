package com.example.controller;

import com.example.api.AiCodeHelperService;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {
    @Autowired
    private AiCodeHelperService aiCodeHelperService;
    @GetMapping("/chat")
    public Flux<ServerSentEvent<String>> chat(int memoryId, String message) {
        String template = "你是一名动漫高手,你能回答用户的所有动漫问题,用户的问题是{{q}}";
        PromptTemplate prompt = PromptTemplate.from(template);
        Prompt q = prompt.apply(Map.of("q", message));
        System.out.println(q.text());

        return null;

    }
}
