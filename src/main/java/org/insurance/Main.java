package org.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.insurance.claims.ExtractedFields.ClaimRouter;
import org.insurance.claims.ExtractedFields.FieldExtractor;
import org.insurance.claims.extractor.PDFExtractor;
import org.insurance.claims.model.ClaimResult;
import org.insurance.claims.model.ExtractedFields;
import org.insurance.claims.validator.ClaimValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Insurance Claims Processing Agent");
        System.out.println("========================================\n");



        String pdfPath ="resources/Automobile_Loss_Notice.pdf";

        try {
            // Process the claim
            ClaimResult result = processClaim(pdfPath);

            // Convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonOutput = mapper.writeValueAsString(result);

            // Print to console
            System.out.println("\n========================================");
            System.out.println("CLAIM PROCESSING RESULT");
            System.out.println("========================================\n");
            System.out.println(jsonOutput);

            // Save to file
            String outputFileName = "claim_result_" + System.currentTimeMillis() + ".json";
            saveToFile(jsonOutput, outputFileName);

            System.out.println("\n========================================");
            System.out.println("Result saved to: " + outputFileName);
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("Error processing claim: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Processes a claim from PDF file
     * @param pdfPath Path to the PDF file
     * @return ClaimResult with all extracted data and routing decision
     * @throws IOException If file cannot be read
     */
    public static ClaimResult processClaim(String pdfPath) throws IOException {
        System.out.println("Step 1: Reading PDF file...");
        PDFExtractor pdfExtractor = new PDFExtractor();
        String pdfText = pdfExtractor.extractText(pdfPath);
        System.out.println(" PDF text extracted successfully");

        System.out.println("\nStep 2: Extracting fields from text...");
        FieldExtractor fieldExtractor = new FieldExtractor();
        ExtractedFields fields = fieldExtractor.extractFields(pdfText);
        System.out.println(" Fields extracted successfully");

        System.out.println("\nStep 3: Validating mandatory fields...");
        ClaimValidator validator = new ClaimValidator();
        List<String> missingFields = validator.validateMandatoryFields(fields);

        if (missingFields.isEmpty()) {
            System.out.println(" All mandatory fields are present");
        } else {
            System.out.println(" Missing fields detected: " + missingFields.size());
            for (String field : missingFields) {
                System.out.println("  - " + field);
            }
        }

        // Check consistency
        List<String> inconsistencies = validator.validateConsistency(fields);
        if (!inconsistencies.isEmpty()) {
            System.out.println(" Inconsistencies detected:");
            for (String issue : inconsistencies) {
                System.out.println("  - " + issue);
            }
        }

        System.out.println("\nStep 4: Determining routing...");
        ClaimRouter router = new ClaimRouter();
        ClaimRouter.RoutingDecision decision = router.determineRoute(fields, missingFields);
        System.out.println(" Route determined: " + decision.getRoute());
        System.out.println("  Reasoning: " + decision.getReasoning());

        // Build result
        ClaimResult result = new ClaimResult();
        result.setExtractedFields(fields);
        result.setMissingFields(missingFields);
        result.setRecommendedRoute(decision.getRoute());
        result.setReasoning(decision.getReasoning());

        return result;
    }

    /**
     * Saves JSON output to a file
     * @param jsonContent JSON string content
     * @param fileName Output file name
     * @throws IOException If file cannot be written
     */
    private static void saveToFile(String jsonContent, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonContent);
        }
    }
}