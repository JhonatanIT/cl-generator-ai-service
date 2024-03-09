package com.jibanez.clgeneratoraiservice.util;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Log4j2
public class JSoupWebScraperExample {
    public static void main(String[] args) {

        log.info("EXAMPLE WITH URL: ");
        getFullTextFromURL("https://example.com/");

        log.info("EXAMPLE FROM HTML FILE: ");
        getFullTextFromHtmlFile();
    }

    public static void getFullTextFromURL(String url){
        try {
            //jsoup by default don't use javascript, just with web static content
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .timeout(50000)
//                    .cookie("cookiename", "val234")
//                    .referrer("http://google.com")
//                    .header("headersecurity", "xyz123")
                    .get();

            Elements content = document.select("div");
            content.forEach(x -> log.info(x.text()));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void getFullTextFromHtmlFile() {

        try {
            //Read file from classpath
            InputStream inputStream = JSoupWebScraperExample.class.getClassLoader().getResourceAsStream("LinkedIn/example.html");
            String htmlFileText = readFromInputStream(inputStream);

            //Parse XML
            Document document = Jsoup.parse(htmlFileText);

            //Extract all the text
            log.info(document.text());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
