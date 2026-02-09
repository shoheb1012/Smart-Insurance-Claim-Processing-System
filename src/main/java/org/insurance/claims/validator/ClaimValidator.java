package org.insurance.claims.validator;



import org.insurance.claims.model.ExtractedFields;

import java.util.ArrayList;
import java.util.List;

public class ClaimValidator {

    /**
     * Validates extracted fields and returns list of missing mandatory fields
     * @param fields Extracted fields from the claim
     * @return List of missing field names
     */
    public List<String> validateMandatoryFields(ExtractedFields fields) {
        List<String> missingFields = new ArrayList<>();

        // Policy Information - Mandatory
        if (isNullOrEmpty(fields.getPolicyInfo().getPolicyNumber())) {
            missingFields.add("Policy Number");
        }

        if (isNullOrEmpty(fields.getPolicyInfo().getPolicyholderName())) {
            missingFields.add("Policyholder Name");
        }

        // Incident Information - Mandatory
        if (isNullOrEmpty(fields.getIncidentInfo().getDateOfLoss())) {
            missingFields.add("Date of Loss");
        }

        if (isNullOrEmpty(fields.getIncidentInfo().getLocation())) {
            missingFields.add("Location of Loss");
        }

        if (isNullOrEmpty(fields.getIncidentInfo().getDescription())) {
            missingFields.add("Description of Accident");
        }

        // Asset Details - Mandatory
        if (fields.getAssetDetails().getEstimatedDamage() == null) {
            missingFields.add("Estimated Damage Amount");
        }

        // Other Mandatory Fields
        if (isNullOrEmpty(fields.getOtherFields().getClaimType())) {
            missingFields.add("Claim Type");
        }

        return missingFields;
    }

    /**
     * Validates data consistency
     * @param fields Extracted fields
     * @return List of inconsistencies found
     */
    public List<String> validateConsistency(ExtractedFields fields) {
        List<String> inconsistencies = new ArrayList<>();

        // Check if estimated damage is reasonable (not negative or extremely high)
        if (fields.getAssetDetails().getEstimatedDamage() != null) {
            double damage = fields.getAssetDetails().getEstimatedDamage();

            if (damage < 0) {
                inconsistencies.add("Estimated damage cannot be negative");
            }

            if (damage > 1000000) { // More than $1M seems unusual for auto
                inconsistencies.add("Estimated damage amount is unusually high");
            }
        }

        // Check if VIN format is valid (17 characters)
        String vin = fields.getAssetDetails().getVin();
        if (vin != null && !vin.isEmpty() && vin.length() != 17) {
            inconsistencies.add("VIN should be 17 characters");
        }

        // Check if year is reasonable
        String year = fields.getAssetDetails().getYear();
        if (year != null && !year.isEmpty()) {
            try {
                int yearInt = Integer.parseInt(year);
                if (yearInt < 1900 || yearInt > 2026) {
                    inconsistencies.add("Vehicle year is outside reasonable range");
                }
            } catch (NumberFormatException e) {
                inconsistencies.add("Invalid vehicle year format");
            }
        }

        return inconsistencies;
    }

    /**
     * Checks if a string is null or empty
     * @param value String to check
     * @return true if null or empty
     */
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}