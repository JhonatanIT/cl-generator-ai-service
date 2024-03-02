package com.jibanez.clgeneratoraiservice.prompt;


import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@StructuredPrompt({
        "Create a professional cover letter with the following details:",
        "job id reference: {{jobIdReference}}",
        "job position: {{jobPosition}}",
        "job description: {{jobDescription}}",
        "job location: {{jobLocation}}",
        "job skills: {{jobSkills}}",
        "company name: {{companyName}}",
        "company address: {{companyAddress}}",
        "company postal code: {{companyPostalCode}}",
        "company city: {{companyCity}}",
        "company state: {{companyState}}",
        "company country: {{companyCountry}}",
        "Hiring Manager's Name: {{hiringManagerName}}",
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoverLetterAdvancedPrompt {

    private String jobIdReference;
    private String jobPosition;
    private String jobDescription;
    private String jobLocation;
    private List<String> jobSkills;
    private String companyName;
    private String companyAddress;
    private String companyPostalCode;
    private String companyCity;
    private String companyState;
    private String companyCountry;
    private String hiringManagerName;
}
