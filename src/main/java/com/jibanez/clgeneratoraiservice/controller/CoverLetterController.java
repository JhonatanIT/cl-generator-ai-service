package com.jibanez.clgeneratoraiservice.controller;

import com.jibanez.clgeneratoraiservice.service.CoverLetterAiService;
import com.jibanez.clgeneratoraiservice.service.PromptService;
import com.jibanez.clgeneratoraiservice.util.GeneratePDFBoxExample;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cover-letter")
@AllArgsConstructor
@Log4j2
public class CoverLetterController {

    private CoverLetterAiService coverLetterAiService;
    private PromptService promptService;

    @GetMapping
    public ResponseEntity<String> generateSimpleCoverLetter(
            @RequestParam String companyName,
            @RequestParam String jobPosition) {

        log.info("Generate cover letter for: {} - {}", companyName, jobPosition);
        String coverLetterTextGenerated = coverLetterAiService.generate(promptService.generateSimplePrompt(companyName, jobPosition));
        log.info("Message generated: {}", coverLetterTextGenerated);

        GeneratePDFBoxExample.generatePDF(coverLetterTextGenerated, "Cover Letter");

        return new ResponseEntity<>(coverLetterTextGenerated, HttpStatus.OK);

        //Using only Prompt without AiService
//        AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
//        log.info("Message generated: {}", aiMessage.text());
//        return new ResponseEntity<>(aiMessage.text(), HttpStatus.OK);
    }

    @GetMapping("/web-scraped")
    public ResponseEntity<String> generateFrom(@RequestParam String jobLink) {

        log.info("Generate cover letter from job link: {}", jobLink);
        String coverLetterTextGenerated = coverLetterAiService.generate(promptService.generateAdvancedPrompt(jobLink));
        log.info("Message generated: {}", coverLetterTextGenerated);

        GeneratePDFBoxExample.generatePDF(coverLetterTextGenerated, "Cover Letter");

        return new ResponseEntity<>(coverLetterTextGenerated, HttpStatus.OK);
    }
}
