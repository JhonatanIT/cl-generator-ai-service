package com.jibanez.clgeneratoraiservice.controller;

import com.jibanez.clgeneratoraiservice.util.AiDemoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cl")
@AllArgsConstructor
@Log4j2
public class CoverLetterController {

    private AiDemoService aiDemoService;

    @GetMapping
    public ResponseEntity<String> getMessage(@RequestParam String prompt) {

        log.info("Prompt received: {}", prompt);
        String agentMessage = aiDemoService.chat(prompt);
        log.info("Message generated: {}", agentMessage);

        return new ResponseEntity<>(agentMessage, HttpStatus.OK);
    }

}
