package testsuite;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import uiExecution.UIOperation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

 
public class ReservationNowTestSuite {
	
    // Logger instance for logging
    private static final Logger logger = LogManager.getLogger(ReservationNowTestSuite.class);

    // Instance of UIOperation to handle UI operations
    UIOperation Execution = new UIOperation();
	
    /**
     * Sets up the browser configuration before any test runs.
     */	
    @Parameters("browser")
    @BeforeTest
	public void browserSetup(String browser) {
    	
    	logger.info("Starting browser setup.");

    	Execution.UIOperationdriver(browser);
    	

	}

	
@Test
public void testCase1() throws Exception{ 
	
	logger.info("Starting test case execution: Test case 1");
	// Excel - Testcase name and Sheet Name
	Execution.Execution("testCase1","KeywordFramework");
       
}
	
	
}