package org.akash.sofvalidator.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class PdfSofParserService {

    public List<String> extractInputFileNames(
            File pdfFile,
            List<String> extensions) throws IOException {

        Set<String> fileNames = new HashSet<>();

        String extRegex = extensions.stream()
                .map(Pattern::quote)
                .reduce((a, b) -> a + "|" + b)
                .orElse("");

        Pattern pattern = Pattern.compile(
                "\\b[\\w\\-]+\\.(" + extRegex + ")\\b"
        );

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                fileNames.add(matcher.group());
            }
        }
        return new ArrayList<>(fileNames);
    }
}
