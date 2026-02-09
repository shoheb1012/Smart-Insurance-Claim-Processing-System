package org.insurance.claims.ExtractedFields;



import org.insurance.claims.model.ExtractedFields;

import java.util.List;

public class ClaimRouter {

    private static final double FAST_TRACK_THRESHOLD = 25000.0;


    public RoutingDecision determineRoute(ExtractedFields fields, List<String> missingFields) {
        String route;
        StringBuilder reasoning = new StringBuilder();
        int priority = 0;

        // Rule 1: Missing mandatory fields → Manual Review (Highest Priority)
        if (!missingFields.isEmpty()) {
            route = "Manual Review";
            reasoning.append("Missing mandatory fields: ")
                    .append(String.join(", ", missingFields))
                    .append(". ");
            priority = 1;
            return new RoutingDecision(route, reasoning.toString(), priority);
        }

        // Rule 2: Check for fraud indicators → Investigation Queue
        if (containsFraudIndicators(fields)) {
            route = "Investigation Queue";
            reasoning.append("Description contains potential fraud indicators (words like 'fraud', 'inconsistent', or 'staged'). ");
            priority = 2;
            return new RoutingDecision(route, reasoning.toString(), priority);
        }

        // Rule 3: Injury claims → Specialist Queue
        if (isInjuryClaim(fields)) {
            route = "Specialist Queue";
            reasoning.append("Claim involves injuries and requires specialist review. ");
            priority = 3;

            // Also mention damage amount if available
            if (fields.getAssetDetails().getEstimatedDamage() != null) {
                reasoning.append(String.format("Estimated damage: $%.2f. ",
                        fields.getAssetDetails().getEstimatedDamage()));
            }

            return new RoutingDecision(route, reasoning.toString(), priority);
        }

        // Rule 4: Estimated damage < $25,000 → Fast-track
        Double estimatedDamage = fields.getAssetDetails().getEstimatedDamage();
        if (estimatedDamage != null && estimatedDamage < FAST_TRACK_THRESHOLD) {
            route = "Fast-track";
            reasoning.append(String.format("Estimated damage ($%.2f) is below the $%.2f threshold. ",
                            estimatedDamage, FAST_TRACK_THRESHOLD))
                    .append("All mandatory fields are present. ")
                    .append("No fraud indicators or injuries detected. ");
            priority = 4;
            return new RoutingDecision(route, reasoning.toString(), priority);
        }

        // Rule 5: High value claims (>= $25,000) → Standard Review
        if (estimatedDamage != null && estimatedDamage >= FAST_TRACK_THRESHOLD) {
            route = "Standard Review";
            reasoning.append(String.format("Estimated damage ($%.2f) exceeds the fast-track threshold of $%.2f. ",
                            estimatedDamage, FAST_TRACK_THRESHOLD))
                    .append("Requires standard review process. ");
            priority = 5;
            return new RoutingDecision(route, reasoning.toString(), priority);
        }

        // Default: Standard Review (if damage amount is missing but all other fields present)
        route = "Standard Review";
        reasoning.append("Standard processing route - all mandatory fields present except damage estimate. ");
        priority = 6;

        return new RoutingDecision(route, reasoning.toString(), priority);
    }


    private boolean containsFraudIndicators(ExtractedFields fields) {
        String description = fields.getIncidentInfo().getDescription();
        if (description == null || description.isEmpty()) {
            return false;
        }

        String lowerDesc = description.toLowerCase();

        // Check for fraud-related keywords
        return lowerDesc.contains("fraud") ||
                lowerDesc.contains("fraudulent") ||
                lowerDesc.contains("inconsistent") ||
                lowerDesc.contains("staged") ||
                lowerDesc.contains("suspicious") ||
                lowerDesc.contains("fake");
    }


    private boolean isInjuryClaim(ExtractedFields fields) {
        String claimType = fields.getOtherFields().getClaimType();

        if (claimType != null && claimType.toLowerCase().contains("injury")) {
            return true;
        }

        // Also check description for injury mentions
        String description = fields.getIncidentInfo().getDescription();
        if (description != null) {
            String lowerDesc = description.toLowerCase();
            return lowerDesc.contains("injury") ||
                    lowerDesc.contains("injured") ||
                    lowerDesc.contains("hurt") ||
                    lowerDesc.contains("hospital") ||
                    lowerDesc.contains("ambulance") ||
                    lowerDesc.contains("medical");
        }

        return false;
    }

    /**
     * Inner class to hold routing decision details
     */
    public static class RoutingDecision {
        private String route;
        private String reasoning;
        private int priority;

        public RoutingDecision(String route, String reasoning, int priority) {
            this.route = route;
            this.reasoning = reasoning;
            this.priority = priority;
        }

        public String getRoute() {
            return route;
        }

        public String getReasoning() {
            return reasoning;
        }

        public int getPriority() {
            return priority;
        }
    }
}