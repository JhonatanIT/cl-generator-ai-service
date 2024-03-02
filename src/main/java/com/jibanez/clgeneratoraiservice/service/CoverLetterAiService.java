package com.jibanez.clgeneratoraiservice.service;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.service.SystemMessage;

public interface CoverLetterAiService {

    @SystemMessage({
            "You are a professional creator of cover letters for IT jobs",
            "Your job is to generate the most impressive cover letter adapted to the company and the position that someone is going to apply",
            "Your objective is to catch the attention of the interviewer and get an interview"
    })
    String generate(Prompt prompt);
}
