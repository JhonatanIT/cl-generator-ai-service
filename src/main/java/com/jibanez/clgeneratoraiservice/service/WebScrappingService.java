package com.jibanez.clgeneratoraiservice.service;

import com.jibanez.clgeneratoraiservice.prompt.CoverLetterAdvancedPrompt;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class WebScrappingService {

    private JobDetailsExtractorAiService jobDetailsExtractorAiService;

    public CoverLetterAdvancedPrompt getCoverLetterAdvancedPromptFromURL(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            //TODO try if it is possible to obtain the hiringManagerName
            //TODO evaluate jobDescription to obtain more precise data (try to obtain more data like job benefits, company value)

            List<String> jobSkills = List.of("[Skill 1]", "[Skill 2]");

            String companyAddress = "[Company Address]";
            String companyPostalCode = "[Company Postal Code]";
            String companyCity = "[Company City]";
            String companyState = "[Company State]";
            String companyCountry = "Australia";

            String hiringManagerName = "Hiring Manager";

            String jobPosition = document.select("[data-automation=\"job-detail-title\"]")
                    .stream().findFirst().map(Element::text).orElse("[Job Position]");
            String companyName = document.select("[data-automation=\"advertiser-name\"]")
                    .stream().findFirst().map(Element::text).orElse("[Company Name]");

            //:not(:has(*)) -> CSS selector when don't have children
            String jobLocation = document.select("._1iz8dgs6u span.y735df0._1iz8dgs4y._1iz8dgsr:not(:has(*))")
                    .stream().findFirst().map(Element::text).orElse("[Job Location]");

            if (!"[Job Location]".equals(jobLocation)) {
                companyCity = jobLocation.split(" ")[0];
                companyState = jobLocation.split(" ")[1];

                if (!"[Company Name]".equals(companyName)) {
                    //Call google to obtain location address of the company
                    Document documentLocation = Jsoup.connect("https://www.google.com/search?q=location of ".concat(companyName)).get();

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

            return coverLetterAdvancedPrompt;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
