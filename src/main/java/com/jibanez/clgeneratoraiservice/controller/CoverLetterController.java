package com.jibanez.clgeneratoraiservice.controller;

import com.jibanez.clgeneratoraiservice.prompt.CoverLetterSimplePrompt;
import com.jibanez.clgeneratoraiservice.service.CoverLetterAiService;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
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

    @GetMapping
    public ResponseEntity<String> generateSimpleCoverLetter(@RequestParam String companyName, @RequestParam String jobPosition) {

        //TODO receive a URL of a job and apply web scrapping to obtain details about the job

        log.info("Generate cover letter for: {} - {}", companyName, jobPosition);

        CoverLetterSimplePrompt coverLetterSimplePrompt = new CoverLetterSimplePrompt();
        coverLetterSimplePrompt.setCompanyName(companyName);
        coverLetterSimplePrompt.setJobPosition(jobPosition);

        Prompt prompt = StructuredPromptProcessor.toPrompt(coverLetterSimplePrompt);

        //Using only Prompt without AiService
//        AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
//        log.info("Message generated: {}", aiMessage.text());
//        return new ResponseEntity<>(aiMessage.text(), HttpStatus.OK);

        //TODO use the AiService
        String coverLetterTextGenerated = coverLetterAiService.generate(prompt);
        log.info("Message generated: {}", coverLetterTextGenerated);
        return new ResponseEntity<>(coverLetterTextGenerated, HttpStatus.OK);
    }

}
