package com.jibanez.clgeneratoraiservice.service;

import dev.langchain4j.service.SystemMessage;

public interface CoverLetterAiService {

    @SystemMessage({
            "You are a professional creator of cover letters for jobs",
            "You MUST use the best keywords that match the profile candidate with the jobDescription",
//            "Your job is to generate the most impressive cover letter adapted to the company and the position that someone is going to apply",
//            "Your objective is to catch the attention of the interviewer and get an interview",
            "Just generate the content of the cover letter, don't include personal comments or attachment comments at the end",
//            "Explain in the cover letter why the job candidate and the company are align in the skills, opportunities and improvements of each other"
//            "Don't include placeholder values that contains square brackets: []"
    })
    String generate(String prompt);
}
