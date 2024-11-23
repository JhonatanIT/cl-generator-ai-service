package com.jibanez.clgeneratoraiservice.util;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HelloWorldAIExample {

    public static void main(String[] args) {

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String answer = model.generate("Say Hello World hahahaha");

        log.info(answer);
    }
}
