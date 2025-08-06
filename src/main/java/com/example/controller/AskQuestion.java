package com.example.controller;

import com.example.api.AiCodeHelperService;
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

    @GetMapping("/askQuestion")
    public String askQuestion(String memoryId, String question) {


        return aiCodeHelperService.chat(memoryId, question);


    }


}
