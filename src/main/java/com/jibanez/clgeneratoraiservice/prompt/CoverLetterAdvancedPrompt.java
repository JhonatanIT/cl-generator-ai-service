package com.jibanez.clgeneratoraiservice.prompt;


import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.*;

import java.util.List;

@StructuredPrompt({
        "Create a professional cover letter with the following details:",
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
        "current date: {{currentDate}}",
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CoverLetterAdvancedPrompt {

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
    private String currentDate;
}
