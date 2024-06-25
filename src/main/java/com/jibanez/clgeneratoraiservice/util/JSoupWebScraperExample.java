package com.jibanez.clgeneratoraiservice.util;

import com.jibanez.clgeneratoraiservice.prompt.CoverLetterAdvancedPrompt;
import com.jibanez.clgeneratoraiservice.service.JobDetailsExtractorAiService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Log4j2
public class JSoupWebScraperExample {

    public static void main(String[] args) {

//        log.info("EXAMPLE WITH URL: ");
//        getFullTextFromURL("https://example.com/");
//
//        log.info("EXAMPLE FROM HTML FILE: ");
//        getFullTextFromHtmlFile();
//
//        log.info("EXAMPLE FROM SEEK JOB: ");
        fromSeek();
    }

    public static void getFullTextFromURL(String url) {
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

    public static void fromSeek() {

        try {
            Document document = Jsoup.connect("https://www.seek.co.nz/job/76274648")
                    .userAgent("Mozilla")
                    .get();

//            Elements content = document.select("[data-automation]");
//            content.forEach(x -> log.info(x.text()));

            List<String> jobSkills = List.of("[Skill 1]", "[Skill 2]", "[Skill 3]");

            String companyAddress = "[Company Address]";
            String companyPostalCode = "[Company Postal Code]";
            String companyCity = "[Company City]";
            String companyState = "[Company State]";
            String companyCountry = "New Zealand";   //"Australia"

            String hiringManagerName = "Hiring Manager";

            String jobPosition = document.select("[data-automation=\"job-detail-title\"]")
                    .stream().findFirst().map(Element::text).orElse("[Job Position]");
            String companyName = document.select("[data-automation=\"advertiser-name\"]")
                    .stream().findFirst().map(Element::text).orElse("[Company Name]");

            //:not(:has(*)) -> CSS selector when don't have children
//            String jobLocation = document.select("._1iz8dgs6u span.y735df0._1iz8dgs4y._1iz8dgsr:not(:has(*))")
            String jobLocation = document.select("[data-automation=\"job-detail-location\"]")
                    .stream().findFirst().map(Element::text).orElse("[Job Location]");

            if (!"[Job Location]".equals(jobLocation)) {

                if (jobLocation.contains(",")) {
                    String[] jobLocationSplit = jobLocation.split(",");
                    companyCity = jobLocationSplit[0].trim();
                    companyState = jobLocationSplit[jobLocationSplit.length - 1].trim();
                } else {
                    companyCity = jobLocation.split(" ")[0];
                    companyState = jobLocation.split(" ")[1];
                }

                if (!"[Company Name]".equals(companyName)) {
                    //Call google to obtain location address of the company
                    Document documentLocation = Jsoup.connect("https://www.google.com/search?q=location of ".concat(companyName)).userAgent("Mozilla").get();

                    companyAddress = documentLocation.select("div .sXLaOe")
                            .stream().findFirst().map(Element::text).orElse("[Job Location]");

                    if (!"[Job Location]".equals(companyAddress)) {
                        companyPostalCode = companyAddress.split(" ")[companyAddress.split(" ").length - 1];
                        companyState = companyAddress.split(" ")[companyAddress.split(" ").length - 2];
                        companyCity = companyAddress.split(" ")[companyAddress.split(" ").length - 3];
                        companyAddress = companyAddress.split(",")[0];
                    }
                }
            }

            String jobDescription = document.select("[data-automation=\"jobAdDetails\"]")
                    .stream().findFirst().map(Element::text).orElse("[Job Description]");

            if (!"[Job Description]".equals(jobDescription)) {

                ChatLanguageModel chatLanguageModel = OpenAiChatModel.withApiKey("demo");
                JobDetailsExtractorAiService jobDetailsExtractorAiService = AiServices.create(JobDetailsExtractorAiService.class, chatLanguageModel);

                //Call jobDetailsExtractorAiService to obtain more data through jobDescription
                jobSkills = jobDetailsExtractorAiService.extractJobSkills(jobDescription);
                hiringManagerName = jobDetailsExtractorAiService.extractHiringManagerName(jobDescription);
            }

            CoverLetterAdvancedPrompt coverLetterAdvancedPrompt = new CoverLetterAdvancedPrompt();
            coverLetterAdvancedPrompt.setJobPosition(jobPosition);
            coverLetterAdvancedPrompt.setJobDescription(jobDescription);
            coverLetterAdvancedPrompt.setJobLocation(jobLocation);
            coverLetterAdvancedPrompt.setJobSkills(jobSkills);
            coverLetterAdvancedPrompt.setCompanyName(companyName);
            coverLetterAdvancedPrompt.setCompanyAddress(companyAddress);
            coverLetterAdvancedPrompt.setCompanyPostalCode(companyPostalCode);
            coverLetterAdvancedPrompt.setCompanyCity(companyCity);
            coverLetterAdvancedPrompt.setCompanyState(companyState);
            coverLetterAdvancedPrompt.setCompanyCountry(companyCountry);
            coverLetterAdvancedPrompt.setHiringManagerName(hiringManagerName);
            coverLetterAdvancedPrompt.setCurrentDate(LocalDate.now().toString());

            System.out.println(coverLetterAdvancedPrompt);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
