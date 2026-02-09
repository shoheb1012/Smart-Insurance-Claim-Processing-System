package org.insurance.claims.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractedFields {
    private PolicyInfo policyInfo;
    private IncidentInfo incidentInfo;
    private InvolvedParties involvedParties;
    private AssetDetails assetDetails;
    private OtherFields otherFields;

    public ExtractedFields() {
        this.policyInfo = new PolicyInfo();
        this.incidentInfo = new IncidentInfo();
        this.involvedParties = new InvolvedParties();
        this.assetDetails = new AssetDetails();
        this.otherFields = new OtherFields();
    }

    // Nested classes for organization
    public static class PolicyInfo {
        private String policyNumber;
        private String policyholderName;
        private String effectiveDate;
        private String naicCode;
        private String lineOfBusiness;

        // Getters and Setters
        public String getPolicyNumber() {
            return policyNumber;
        }

        public void setPolicyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
        }

        public String getPolicyholderName() {
            return policyholderName;
        }

        public void setPolicyholderName(String policyholderName) {
            this.policyholderName = policyholderName;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(String effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public String getNaicCode() {
            return naicCode;
        }

        public void setNaicCode(String naicCode) {
            this.naicCode = naicCode;
        }

        public String getLineOfBusiness() {
            return lineOfBusiness;
        }

        public void setLineOfBusiness(String lineOfBusiness) {
            this.lineOfBusiness = lineOfBusiness;
        }
    }

    public static class IncidentInfo {
        private String dateOfLoss;
        private String timeOfLoss;
        private String location;
        private String description;
        private String reportNumber;
        private String policeDepartmentContacted;

        // Getters and Setters
        public String getDateOfLoss() {
            return dateOfLoss;
        }

        public void setDateOfLoss(String dateOfLoss) {
            this.dateOfLoss = dateOfLoss;
        }

        public String getTimeOfLoss() {
            return timeOfLoss;
        }

        public void setTimeOfLoss(String timeOfLoss) {
            this.timeOfLoss = timeOfLoss;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getReportNumber() {
            return reportNumber;
        }

        public void setReportNumber(String reportNumber) {
            this.reportNumber = reportNumber;
        }

        public String getPoliceDepartmentContacted() {
            return policeDepartmentContacted;
        }

        public void setPoliceDepartmentContacted(String policeDepartmentContacted) {
            this.policeDepartmentContacted = policeDepartmentContacted;
        }
    }

    public static class InvolvedParties {
        private String driverName;
        private String driverAddress;
        private String driverPhone;
        private String ownerName;
        private String ownerAddress;
        private String ownerPhone;
        private String witnesses;
        private String injuredParties;

        // Getters and Setters
        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverAddress() {
            return driverAddress;
        }

        public void setDriverAddress(String driverAddress) {
            this.driverAddress = driverAddress;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getOwnerAddress() {
            return ownerAddress;
        }

        public void setOwnerAddress(String ownerAddress) {
            this.ownerAddress = ownerAddress;
        }

        public String getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(String ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public String getWitnesses() {
            return witnesses;
        }

        public void setWitnesses(String witnesses) {
            this.witnesses = witnesses;
        }

        public String getInjuredParties() {
            return injuredParties;
        }

        public void setInjuredParties(String injuredParties) {
            this.injuredParties = injuredParties;
        }
    }

    public static class AssetDetails {
        private String assetType = "Vehicle";
        private String vin;
        private String make;
        private String model;
        private String year;
        private String plateNumber;
        private String state;
        private String damageDescription;
        private Double estimatedDamage;

        // Getters and Setters
        public String getAssetType() {
            return assetType;
        }

        public void setAssetType(String assetType) {
            this.assetType = assetType;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDamageDescription() {
            return damageDescription;
        }

        public void setDamageDescription(String damageDescription) {
            this.damageDescription = damageDescription;
        }

        public Double getEstimatedDamage() {
            return estimatedDamage;
        }

        public void setEstimatedDamage(Double estimatedDamage) {
            this.estimatedDamage = estimatedDamage;
        }
    }

    public static class OtherFields {
        private String claimType = "Automobile";
        private String attachments;
        private String agencyName;
        private String agencyContact;

        // Getters and Setters
        public String getClaimType() {
            return claimType;
        }

        public void setClaimType(String claimType) {
            this.claimType = claimType;
        }

        public String getAttachments() {
            return attachments;
        }

        public void setAttachments(String attachments) {
            this.attachments = attachments;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getAgencyContact() {
            return agencyContact;
        }

        public void setAgencyContact(String agencyContact) {
            this.agencyContact = agencyContact;
        }
    }

    // Main class getters and setters
    public PolicyInfo getPolicyInfo() {
        return policyInfo;
    }

    public void setPolicyInfo(PolicyInfo policyInfo) {
        this.policyInfo = policyInfo;
    }

    public IncidentInfo getIncidentInfo() {
        return incidentInfo;
    }

    public void setIncidentInfo(IncidentInfo incidentInfo) {
        this.incidentInfo = incidentInfo;
    }

    public InvolvedParties getInvolvedParties() {
        return involvedParties;
    }

    public void setInvolvedParties(InvolvedParties involvedParties) {
        this.involvedParties = involvedParties;
    }

    public AssetDetails getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(AssetDetails assetDetails) {
        this.assetDetails = assetDetails;
    }

    public OtherFields getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(OtherFields otherFields) {
        this.otherFields = otherFields;
    }
}