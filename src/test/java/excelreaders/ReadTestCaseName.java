package excelreaders;

import org.apache.poi.ss.usermodel.Sheet;

import uiExecution.UIOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReadTestCaseName {

    private static final Logger logger = LogManager.getLogger(ReadTestCaseName.class);

    /**
     * Reads test case details from the specified Excel sheet.
     *
     * @param testCaseName the name of the test case to read details for
     * @return a map containing the test case details
     * @throws IOException if there is an issue reading the Excel file
     */
    public Map<String, Map<String, String>> getTestCaseDetails(String testCaseName, String sheetname) throws IOException {

        ReadExcel file = new ReadExcel();
        
        String projectDir = System.getProperty("user.dir");
        // Read the specified Excel sheet
        Sheet guru99Sheet = file.readExcel(projectDir + "\\src\\test\\resources\\ExcelData\\", "TestCase.xlsx", "KeywordFramework");

        Map<String, Map<String, String>> testCaseMap = new LinkedHashMap<>();

        // Find the number of rows in the Excel file
        int rowCount = guru99Sheet.getLastRowNum() - guru99Sheet.getFirstRowNum();

        String testcasename = "";
        String actualTestcasename = "";

        // Create a loop over all the rows of the Excel file to read it
        for (int i = 1; i <= rowCount; i++) {
            Row row = guru99Sheet.getRow(i);
            Map<String, String> detailsMap = new LinkedHashMap<>();

            // Check if the first cell contains a value, indicating a new test case name
            if (row.getCell(0).toString().length() != 0) {
                testcasename = row.getCell(0).toString();
                logger.info("New Testcase -> {} Started", testcasename);
            } else {
                // If the current row belongs to the specified test case
                if (testcasename.equals(testCaseName)) {
                    if (actualTestcasename.isEmpty()) {
                        actualTestcasename = testcasename;
                    }

                    if (row.getCell(0).toString().length() == 0 && testcasename.equalsIgnoreCase(testCaseName)) {
                        // Log the test case details
                        logger.debug("Step details: {} ---- {} ---- {} ---- {}", row.getCell(1), row.getCell(2), row.getCell(3), row.getCell(4));

                        // Populate the details map with test case step details
                        detailsMap.put("Keyword", row.getCell(1).toString());
                        detailsMap.put("Object", row.getCell(2).toString());
                        detailsMap.put("ObjectType", row.getCell(3).toString());
                        detailsMap.put("value", row.getCell(4).toString());

                        String stepKey = "Step" + i;
                        testCaseMap.put(stepKey, detailsMap);
                    }
                }
            }
        }

        logger.info("Test case details extraction complete");
        return testCaseMap;
    }
    
    public String getJiraId(String testCaseName, String sheetName) throws IOException {
        ReadExcel file = new ReadExcel();
        String projectDir = System.getProperty("user.dir");
        Sheet sheet = file.readExcel(projectDir + "\\src\\test\\resources\\ExcelData\\", "TestCase.xlsx", sheetName);

        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        String testCaseId = "";

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);

            // Check if the row belongs to the specified test case
            if (row.getCell(0) != null && row.getCell(0).toString().equals(testCaseName)) {
                // Check if the row contains a Jira ID
                if (row.getCell(5) != null && !row.getCell(5).toString().isEmpty()) {
                    testCaseId = row.getCell(5).toString();
                    break;
                }
            }
        }

        return testCaseId;
    }
}
