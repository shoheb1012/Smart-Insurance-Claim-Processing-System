# Insurance Claims Processing Agent

An autonomous Java-based agent that processes FNOL (First Notice of Loss) documents, extracts key information, validates data, and routes claims to appropriate queues based on business rules.

## 🎯 Overview

This application automates the initial processing of automobile insurance claims by:
- Extracting structured data from PDF/TXT FNOL documents
- Validating mandatory fields and data consistency
- Applying business rules to route claims appropriately
- Providing detailed reasoning for routing decisions
- Outputting results in JSON format

## 🏗️ Architecture

```
insurance-claims-agent/
├── src/main/java/com/insurance/claims/
│   ├── Main.java                      # Main application orchestrator
│   ├── model/
│   │   ├── ExtractedFields.java       # Data model for extracted claim data
│   │   └── ClaimResult.java           # Output model with routing decision
│   ├── extractor/
│   │   ├── PDFExtractor.java          # PDF text extraction using Apache PDFBox
│   │   └── FieldExtractor.java        # Rule-based field extraction with regex
│   ├── validator/
│   │   └── ClaimValidator.java        # Field validation and consistency checks
│   └── router/
│       └── ClaimRouter.java           # Business rule engine for routing
├── sample-documents/                   # Sample FNOL documents for testing
├── pom.xml                            # Maven dependencies and build config
└── README.md                          # This file
```

## 📋 Features

### 1. Field Extraction
Automatically extracts the following information from FNOL documents:

**Policy Information:**
- Policy Number
- Policyholder Name
- Effective Dates
- NAIC Code
- Line of Business

**Incident Information:**
- Date of Loss
- Time of Loss
- Location (Street, City, State, ZIP)
- Description of Accident
- Police Report Number
- Police Department Contacted

**Involved Parties:**
- Driver Name, Address, Phone
- Owner Name, Address, Phone
- Witnesses
- Injured Parties

**Asset Details:**
- Vehicle Identification Number (VIN)
- Make, Model, Year
- License Plate Number
- State
- Damage Description
- Estimated Damage Amount

**Other Fields:**
- Claim Type
- Agency Information
- Attachments

### 2. Validation
- Checks for missing mandatory fields
- Validates data formats (VIN, dates, amounts)
- Identifies inconsistencies (negative amounts, unrealistic values)

### 3. Intelligent Routing
Routes claims based on business rules with priority:

| Priority | Condition | Route | Reasoning |
|----------|-----------|-------|-----------|
| 1 | Missing mandatory fields | **Manual Review** | Cannot auto-process incomplete claims |
| 2 | Contains fraud keywords | **Investigation Queue** | Words like "fraud", "staged", "inconsistent" |
| 3 | Injury involved | **Specialist Queue** | Requires medical expertise |
| 4 | Damage < $25,000 | **Fast-track** | Low-value, straightforward claims |
| 5 | Damage ≥ $25,000 | **Standard Review** | High-value claims need thorough review |

### 4. JSON Output
Generates structured JSON with:
- All extracted fields organized by category
- List of missing mandatory fields
- Recommended routing queue
- Detailed reasoning for the decision

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- (Optional) Git for cloning the repository

### Installation

1. **Clone or download the repository:**
```bash
git clone https://github.com/yourusername/insurance-claims-agent.git
cd insurance-claims-agent
```

2. **Build the project:**
```bash
mvn clean package
```

This will:
- Download all dependencies
- Compile the code
- Run tests
- Create an executable JAR file in `target/claims-agent-1.0-SNAPSHOT.jar`

## 📖 Usage

### Running the Application

**Basic usage:**
```bash
java -jar target/claims-agent-1.0-SNAPSHOT.jar <path-to-fnol-document>
```

**With sample documents:**
```bash
# Test with complete claim (should route to Fast-track)
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_1.txt

# Test with missing fields (should route to Manual Review)
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_2_missing_fields.txt

# Test with injury claim (should route to Specialist Queue)
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_3_injury.txt
```

### Output

The application produces:
1. **Console output** - Detailed processing steps and results
2. **JSON file** - `claim_result_<timestamp>.json` with complete analysis

**Sample JSON Output:**
```json
{
  "extractedFields": {
    "policyInfo": {
      "policyNumber": "PA-2024-001234",
      "policyholderName": "Robert James Anderson",
      "naicCode": "12345"
    },
    "incidentInfo": {
      "dateOfLoss": "01/10/2024",
      "timeOfLoss": "2:30 PM",
      "location": "456 Main Street, Springfield, IL 62702",
      "description": "Vehicle was traveling southbound..."
    },
    "assetDetails": {
      "vin": "4T1B11HK5LU123456",
      "make": "Toyota",
      "model": "Camry",
      "year": "2020",
      "estimatedDamage": 8500.0
    }
  },
  "missingFields": [],
  "recommendedRoute": "Fast-track",
  "reasoning": "Estimated damage ($8,500.00) is below the $25,000.00 threshold. All mandatory fields are present. No fraud indicators or injuries detected."
}
```

## 🧪 Testing

### Test with Different Scenarios

**Scenario 1: Complete Low-Value Claim**
```bash
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_1.txt
```
Expected: Routes to **Fast-track** (damage < $25,000, all fields present)

**Scenario 2: Incomplete Claim**
```bash
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_2_missing_fields.txt
```
Expected: Routes to **Manual Review** (missing estimate and other fields)

**Scenario 3: Injury Claim**
```bash
java -jar target/claims-agent-1.0-SNAPSHOT.jar sample-documents/sample_fnol_3_injury.txt
```
Expected: Routes to **Specialist Queue** (injuries reported)

## 🔧 Technical Details

### Dependencies
- **Apache PDFBox 2.0.29** - PDF parsing and text extraction
- **Jackson 2.15.2** - JSON serialization/deserialization
- **JUnit 4.13.2** - Unit testing framework

### Approach: Rule-Based Extraction

This implementation uses a **rule-based approach** with regex patterns for field extraction:

**Advantages:**
- ✅ Fast processing
- ✅ No external API dependencies
- ✅ Deterministic results
- ✅ Low cost (no API fees)
- ✅ Works offline

**Limitations:**
- ⚠️ Requires maintenance for new form formats
- ⚠️ Less flexible than AI-based extraction
- ⚠️ May struggle with handwritten or poorly scanned documents

### Routing Logic Priority

The router applies rules in strict priority order:
1. Missing fields check (highest priority)
2. Fraud detection
3. Injury detection
4. Damage amount threshold
5. Default routing

## 🎨 Customization

### Adding New Fields
Edit `ExtractedFields.java` and add extraction logic in `FieldExtractor.java`

### Modifying Routing Rules
Edit `ClaimRouter.java` to change:
- Damage threshold (default: $25,000)
- Fraud keywords
- Routing destinations
- Priority order

### Changing Mandatory Fields
Edit `ClaimValidator.java` to add/remove required fields

## 📁 Sample Documents

The project includes three sample FNOL documents:

1. **sample_fnol_1.txt** - Complete claim, property damage only, $8,500 estimate
2. **sample_fnol_2_missing_fields.txt** - Incomplete claim with fraud indicators
3. **sample_fnol_3_injury.txt** - High-value claim with injuries, $35,000 estimate

## 🐛 Troubleshooting

**Issue: "File not found" error**
- Ensure the path to the FNOL document is correct
- Use absolute paths if relative paths don't work

**Issue: Build fails**
- Verify Java 11+ is installed: `java -version`
- Verify Maven is installed: `mvn -version`
- Try: `mvn clean install -U` to force dependency updates

**Issue: No fields extracted**
- Check if the document format matches expected ACORD structure
- Review console output for extraction warnings
- Verify PDF is not encrypted or image-based

## 📝 Future Enhancements

Potential improvements:
- [ ] AI-powered extraction for better accuracy
- [ ] Support for multiple FNOL form formats
- [ ] OCR for scanned/image PDFs
- [ ] REST API interface
- [ ] Database persistence
- [ ] Dashboard UI
- [ ] Batch processing
- [ ] Email integration

## 👤 Author

Created for Synapx Assessment

## 📄 License

This project is created for assessment purposes.

---

## 🎓 Assessment Requirements Met

✅ Extracts all required fields from FNOL documents  
✅ Identifies missing and inconsistent fields  
✅ Classifies claims and routes to correct workflow  
✅ Provides clear explanations for routing decisions  
✅ Outputs results in JSON format  
✅ Uses Java and Maven  
✅ Rule-based approach (no AI API costs)  
✅ Includes README with approach and instructions  
✅ Includes sample documents for testing  
✅ Complete working implementation