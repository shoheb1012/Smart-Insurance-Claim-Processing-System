package org.insurance.claims.ExtractedFields;


import org.insurance.claims.model.ExtractedFields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldExtractor {


    public ExtractedFields extractFields(String text) {
        ExtractedFields fields = new ExtractedFields();

        // Extract Policy Information
        extractPolicyInfo(text, fields);

        // Extract Incident Information
        extractIncidentInfo(text, fields);

        // Extract Involved Parties
        extractInvolvedParties(text, fields);

        // Extract Asset Details
        extractAssetDetails(text, fields);

        // Extract Other Fields
        extractOtherFields(text, fields);

        return fields;
    }

    private void extractPolicyInfo(String text, ExtractedFields fields) {
        // Policy Number - looking for pattern after "POLICY NUMBER"
        String policyNumber = extractWithPattern(text,
                "POLICY NUMBER[:\\s]*([A-Z0-9-]+)", 1);
        fields.getPolicyInfo().setPolicyNumber(policyNumber);

        // Policyholder Name - after "NAME OF INSURED"
        String policyholderName = extractWithPattern(text,
                "NAME OF INSURED[^\\n]*\\n([A-Za-z\\s,\\.]+?)(?:\\n|DATE)", 1);
        fields.getPolicyInfo().setPolicyholderName(cleanValue(policyholderName));

        // NAIC Code
        String naicCode = extractWithPattern(text,
                "CARRIER NAIC CODE[:\\s]*([0-9]+)", 1);
        fields.getPolicyInfo().setNaicCode(naicCode);

        // Line of Business
        String lineOfBusiness = extractWithPattern(text,
                "LINE OF BUSINESS[:\\s]*([A-Za-z\\s]+?)(?:\\n|$)", 1);
        fields.getPolicyInfo().setLineOfBusiness(cleanValue(lineOfBusiness));
    }

    private void extractIncidentInfo(String text, ExtractedFields fields) {
        // Date of Loss
        String dateOfLoss = extractWithPattern(text,
                "DATE OF LOSS[^\\n]*?([0-9]{1,2}[/-][0-9]{1,2}[/-][0-9]{2,4})", 1);
        fields.getIncidentInfo().setDateOfLoss(dateOfLoss);

        // Time of Loss
        String timeOfLoss = extractWithPattern(text,
                "TIME[:\\s]*([0-9]{1,2}:[0-9]{2}\\s*(?:AM|PM)?)", 1);
        fields.getIncidentInfo().setTimeOfLoss(timeOfLoss);

        // Location - Street, City, State, ZIP
        String street = extractWithPattern(text,
                "STREET[:\\s]*([^\\n]+?)(?:CITY|\\n)", 1);
        String cityStateZip = extractWithPattern(text,
                "CITY, STATE, ZIP[:\\s]*([^\\n]+?)(?:COUNTRY|\\n)", 1);

        String location = (street + ", " + cityStateZip).trim();
        if (location.length() > 2) {
            fields.getIncidentInfo().setLocation(cleanValue(location));
        }

        // Description of Accident
        String description = extractWithPattern(text,
                "DESCRIPTION OF ACCIDENT[^\\n]*\\n([^\\n]+(?:\\n[^A-Z][^\\n]+)*)", 1);
        fields.getIncidentInfo().setDescription(cleanValue(description));

        // Report Number
        String reportNumber = extractWithPattern(text,
                "REPORT NUMBER[:\\s]*([A-Z0-9-]+)", 1);
        fields.getIncidentInfo().setReportNumber(reportNumber);

        // Police Department Contacted
        String policeContacted = extractWithPattern(text,
                "POLICE OR FIRE DEPARTMENT CONTACTED[:\\s]*([^\\n]+)", 1);
        fields.getIncidentInfo().setPoliceDepartmentContacted(cleanValue(policeContacted));
    }

    private void extractInvolvedParties(String text, ExtractedFields fields) {
        // Driver's Name
        String driverName = extractWithPattern(text,
                "DRIVER'S NAME AND ADDRESS[^\\n]*\\n([A-Za-z\\s,\\.]+?)(?:\\n|PHONE|$)", 1);
        fields.getInvolvedParties().setDriverName(cleanValue(driverName));

        // Owner's Name
        String ownerName = extractWithPattern(text,
                "OWNER'S NAME AND ADDRESS[^\\n]*\\n([A-Za-z\\s,\\.]+?)(?:\\n|PHONE|$)", 1);
        fields.getInvolvedParties().setOwnerName(cleanValue(ownerName));

        // Extract phone numbers if available
        String phonePattern = "\\(?([0-9]{3})\\)?[-\\s]?([0-9]{3})[-\\s]?([0-9]{4})";
        String driverPhone = extractWithPattern(text,
                "DRIVER'S NAME[^\\n]*(?:\\n[^\\n]*){1,3}PHONE[^\\n]*" + phonePattern, 0);
        fields.getInvolvedParties().setDriverPhone(cleanValue(driverPhone));
    }

    private void extractAssetDetails(String text, ExtractedFields fields) {
        // VIN
        String vin = extractWithPattern(text,
                "V\\.I\\.N\\.?[:\\s]*([A-HJ-NPR-Z0-9]{17})", 1);
        fields.getAssetDetails().setVin(vin);

        // Year
        String year = extractWithPattern(text,
                "YEAR[:\\s]*([12][0-9]{3})", 1);
        fields.getAssetDetails().setYear(year);

        // Make
        String make = extractWithPattern(text,
                "MAKE[:\\s]*([A-Za-z]+)", 1);
        fields.getAssetDetails().setMake(cleanValue(make));

        // Model
        String model = extractWithPattern(text,
                "MODEL[:\\s]*([A-Za-z0-9\\s]+?)(?:BODY|TYPE|\\n)", 1);
        fields.getAssetDetails().setModel(cleanValue(model));

        // Plate Number
        String plateNumber = extractWithPattern(text,
                "PLATE NUMBER[:\\s]*([A-Z0-9]+)", 1);
        fields.getAssetDetails().setPlateNumber(plateNumber);

        // State
        String state = extractWithPattern(text,
                "PLATE NUMBER[^\\n]*STATE[:\\s]*([A-Z]{2})", 1);
        fields.getAssetDetails().setState(state);

        // Damage Description
        String damageDesc = extractWithPattern(text,
                "DESCRIBE DAMAGE[^\\n]*\\n([^\\n]+(?:\\n[^A-Z][^\\n]+)*)", 1);
        fields.getAssetDetails().setDamageDescription(cleanValue(damageDesc));

        // Estimated Damage Amount
        String estimateStr = extractWithPattern(text,
                "ESTIMATE AMOUNT[:\\s]*\\$?([0-9,]+(?:\\.[0-9]{2})?)", 1);
        if (!estimateStr.isEmpty()) {
            try {
                // Remove commas and parse
                String cleanAmount = estimateStr.replace(",", "");
                Double estimatedDamage = Double.parseDouble(cleanAmount);
                fields.getAssetDetails().setEstimatedDamage(estimatedDamage);
            } catch (NumberFormatException e) {
                // If parsing fails, leave as null
            }
        }
    }

    private void extractOtherFields(String text, ExtractedFields fields) {
        // Agency Name
        String agencyName = extractWithPattern(text,
                "AGENCY[\\s\\n]+NAME[:\\s]*([^\\n]+)", 1);
        fields.getOtherFields().setAgencyName(cleanValue(agencyName));

        // Agency Contact
        String agencyContact = extractWithPattern(text,
                "CONTACT[^\\n]*\\n([A-Za-z\\s,\\.]+?)(?:\\n|PHONE|$)", 1);
        fields.getOtherFields().setAgencyContact(cleanValue(agencyContact));

        // Check for injury-related content
        if (text.toLowerCase().contains("injured") ||
                text.toLowerCase().contains("injury") ||
                text.toLowerCase().contains("extent of injury")) {
            fields.getOtherFields().setClaimType("Automobile - Injury");
        } else {
            fields.getOtherFields().setClaimType("Automobile - Property Damage");
        }
    }


    private String extractWithPattern(String text, String pattern, int groupIndex) {
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher m = p.matcher(text);

            if (m.find()) {
                String value = m.group(groupIndex);
                return value != null ? value.trim() : "";
            }
        } catch (Exception e) {
            // Pattern matching failed, return empty
        }
        return "";
    }


    private String cleanValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Remove multiple spaces
        value = value.replaceAll("\\s+", " ").trim();

        // Remove trailing colons and commas
        value = value.replaceAll("[,:]+$", "").trim();

        // Return null for empty or placeholder values
        if (value.isEmpty() ||
                value.equals("-") ||
                value.equals("_") ||
                value.matches("^[\\s._-]+$")) {
            return null;
        }

        return value;
    }
}
