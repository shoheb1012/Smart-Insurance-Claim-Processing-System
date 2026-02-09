package org.insurance.claims.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFExtractor {


    public String extractText(String pdfFilePath) throws IOException {
        File pdfFile = new File(pdfFilePath);

        if (!pdfFile.exists()) {
            throw new IOException("PDF file not found: " + pdfFilePath);
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Clean up the text
            text = cleanText(text);

            return text;
        }
    }


    private String cleanText(String text) {
        // Remove multiple spaces
        text = text.replaceAll(" +", " ");

        // Remove multiple newlines but keep structure
        text = text.replaceAll("\n\n+", "\n\n");

        return text.trim();
    }

    /**
     * Extracts a specific section of text between two markers
     * @param text Full text content
     * @param startMarker Start marker
     * @param endMarker End marker
     * @return Extracted section or empty string if not found
     */
    public String extractSection(String text, String startMarker, String endMarker) {
        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) {
            return "";
        }

        int endIndex = text.indexOf(endMarker, startIndex);
        if (endIndex == -1) {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex).trim();
    }
}