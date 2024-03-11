package com.jibanez.clgeneratoraiservice.service;

import com.jibanez.clgeneratoraiservice.prompt.CoverLetterAdvancedPrompt;
import com.jibanez.clgeneratoraiservice.prompt.CoverLetterSimplePrompt;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Log4j2
public class PromptService {

    private WebScrappingService webScrappingService;

    public String generateSimplePrompt(String companyName, String jobPosition) {

        CoverLetterSimplePrompt coverLetterSimplePrompt = new CoverLetterSimplePrompt(companyName, jobPosition, LocalDate.now().toString());
        Prompt prompt = StructuredPromptProcessor.toPrompt(coverLetterSimplePrompt);
        return prompt.text();
    }

    public String generateAdvancedPrompt(String jobLink) {

        CoverLetterAdvancedPrompt coverLetterAdvancedPrompt = webScrappingService.getCoverLetterAdvancedPromptFromURL(jobLink);
        Prompt prompt = StructuredPromptProcessor.toPrompt(coverLetterAdvancedPrompt);
        return prompt.text();
    }
}
