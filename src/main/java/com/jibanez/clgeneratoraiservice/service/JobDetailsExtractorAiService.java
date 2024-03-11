package com.jibanez.clgeneratoraiservice.service;

import dev.langchain4j.service.UserMessage;

import java.util.List;

public interface JobDetailsExtractorAiService {

    //TODO add more values to extract

    @UserMessage("Extract job skills from the following job description{{it}}")
    List<String> extractJobSkills(String text);

    @UserMessage("Extract hiring manager's name from the following job description{{it}}, if it is not found return: Hiring Manager")
    String extractHiringManagerName(String text);

}
