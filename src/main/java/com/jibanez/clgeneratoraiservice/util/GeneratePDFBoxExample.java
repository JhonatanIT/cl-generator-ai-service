package com.jibanez.clgeneratoraiservice.util;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class GeneratePDFBoxExample {

    public static void main(String[] args) {

        generatePDF("""
                Armando Paredes
                Melbourne, VIC 3000

                Thank you for considering my application. I am eager to bring my passion for software development and team collaboration to [Company Name], and I am excited about the possibility of contributing to your innovative projects.

                Warm regards,

                Armando Paredes""", "Cover Letter");
    }

    public static void generatePDF(String fullText, String fileName) {

        if (fullText == null || fullText.isEmpty()) {
            log.info("Text not found");
            return;
        }

        if (!fileName.endsWith(".pdf")) {
            fileName = fileName.concat(".pdf");
        }

        log.info("Generating PDF file: {}", fileName);

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDFont pdfFont = PDType1Font.HELVETICA;
                float fontSize = 10;
                float leading = 1.5f * fontSize;

                PDRectangle mediabox = page.getMediaBox();
                float margin = 30;
                float width = mediabox.getWidth() - 2 * margin;
                float startX = mediabox.getLowerLeftX() + margin;
                float startY = mediabox.getUpperRightY() - margin;

                contentStream.beginText();
                contentStream.setFont(pdfFont, fontSize);
                contentStream.newLineAtOffset(startX, startY);

                for (String textLine : fullText.lines().toList()) {
                    for (String pdfTextLine : getPDFTextLines(textLine, pdfFont, fontSize, width)) {
                        contentStream.showText(pdfTextLine);
                        contentStream.newLineAtOffset(0, -leading);
                    }
                }
                contentStream.endText();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }

            document.save(fileName);
            log.info("PDF created successfully!");

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private static List<String> getPDFTextLines(String text, PDFont pdfFont, float fontSize, float width) throws IOException {

        List<String> lines = new ArrayList<>();

        //Split by line
//        for (String text : fullText.lines().toList()) {
        int lastSpace = -1;

        //Separate by paragraph
        if (text.isEmpty()) {
            lines.add("");
        }

        //Get the text line that adjust the font size and margin
        while (!text.isEmpty()) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            String subString = text.substring(0, spaceIndex);
            float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
            log.debug("{} - {} of {}", subString, size, width);
            if (size > width) {
                if (lastSpace < 0)
                    lastSpace = spaceIndex;
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                log.debug("{} is line", subString);
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                log.debug("{} is line", text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
//        }

        return lines;
    }
}
