package com.example.controller;

import com.example.api.AiCodeHelperService;
import com.example.configuration.ImageModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController

public class AskQuestion {

    @Autowired
    private AiCodeHelperService aiCodeHelperService;
    @Autowired
    private ImageModelFactory imageModelFactory;

    @GetMapping("/askQuestion")
    public String askQuestion(String memoryId, String question) {
        System.out.println(memoryId);

        return aiCodeHelperService.chat(memoryId, question);

    }
    @GetMapping("/generateImage")
    public String generateImage(String prompt) {
        System.out.println("---create Task1----");
        String taskId = imageModelFactory.createAsyncTask(prompt);
        String s = imageModelFactory.waitAsyncTask(taskId);
        System.out.println(s);

        return s;

    }





}
