package org.insurance.claims.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimResult {
    private ExtractedFields extractedFields;
    private List<String> missingFields;
    private String recommendedRoute;
    private String reasoning;

    public ClaimResult() {
        this.missingFields = new ArrayList<>();
    }

    public ClaimResult(ExtractedFields extractedFields, List<String> missingFields,
                       String recommendedRoute, String reasoning) {
        this.extractedFields = extractedFields;
        this.missingFields = missingFields;
        this.recommendedRoute = recommendedRoute;
        this.reasoning = reasoning;
    }

    // Getters and Setters
    public ExtractedFields getExtractedFields() {
        return extractedFields;
    }

    public void setExtractedFields(ExtractedFields extractedFields) {
        this.extractedFields = extractedFields;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(List<String> missingFields) {
        this.missingFields = missingFields;
    }

    public String getRecommendedRoute() {
        return recommendedRoute;
    }

    public void setRecommendedRoute(String recommendedRoute) {
        this.recommendedRoute = recommendedRoute;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public void addMissingField(String field) {
        this.missingFields.add(field);
    }
}
