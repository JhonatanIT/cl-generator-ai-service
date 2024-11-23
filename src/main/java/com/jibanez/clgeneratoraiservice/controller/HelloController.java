package com.jibanez.clgeneratoraiservice.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
@Log4j2
public class HelloController {

    ChatLanguageModel chatLanguageModel;

    @GetMapping("/hi")
    public ResponseEntity<String> hi() {

        String aiMessage = chatLanguageModel.generate("Hi Cover Letter Service!");
        log.info("Message generated: {}", aiMessage);
        return ResponseEntity.ok(aiMessage);
    }

}
