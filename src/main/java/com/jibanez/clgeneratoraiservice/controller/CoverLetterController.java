package com.jibanez.clgeneratoraiservice.controller;

import com.jibanez.clgeneratoraiservice.service.CoverLetterAiService;
import com.jibanez.clgeneratoraiservice.service.PromptService;
import com.jibanez.clgeneratoraiservice.service.WebScrappingService;
import com.jibanez.clgeneratoraiservice.util.JSoupWebScraperExample;
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

    private CoverLetterAiService coverLetterAiService;
    private WebScrappingService webScrappingService;
    private PromptService promptService;

    @GetMapping
    public ResponseEntity<String> generateSimpleCoverLetter(
            @RequestParam String companyName,
            @RequestParam String jobPosition,
            @RequestParam String jobLink) {

        //TODO receive a URL of a job and apply web scrapping to obtain details about the job
        //TODO use the advanced prompt

        log.info("Generate cover letter for: {} - {}", companyName, jobPosition);
        String coverLetterTextGenerated = coverLetterAiService.generate(promptService.generateSimplePrompt(companyName, jobPosition));
        log.info("Message generated: {}", coverLetterTextGenerated);

//        webScrappingService.getFullTextFromURL(jobLink);
        JSoupWebScraperExample.getFullTextFromHtmlFile();

        return new ResponseEntity<>(coverLetterTextGenerated, HttpStatus.OK);

        //Using only Prompt without AiService
//        AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
//        log.info("Message generated: {}", aiMessage.text());
//        return new ResponseEntity<>(aiMessage.text(), HttpStatus.OK);
    }

}
