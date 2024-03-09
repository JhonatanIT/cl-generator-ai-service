package com.jibanez.clgeneratoraiservice.service;

import com.jibanez.clgeneratoraiservice.prompt.CoverLetterSimplePrompt;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PromptService {

    public String generateSimplePrompt(String companyName, String jobPosition) {

        CoverLetterSimplePrompt coverLetterSimplePrompt =
                new CoverLetterSimplePrompt(companyName, jobPosition, LocalDate.now().toString());
        Prompt prompt = StructuredPromptProcessor.toPrompt(coverLetterSimplePrompt);

        return prompt.text();
    }
}
