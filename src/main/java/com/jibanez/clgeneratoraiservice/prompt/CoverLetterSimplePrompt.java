package com.jibanez.clgeneratoraiservice.prompt;


import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@StructuredPrompt({
        "Create a professional cover letter with the following details:",
        "job position: {{jobPosition}}",
        "company name: {{companyName}}",
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoverLetterSimplePrompt {

    private String jobPosition;
    private String companyName;
}
