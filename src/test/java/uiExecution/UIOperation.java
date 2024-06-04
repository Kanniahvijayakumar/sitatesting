package uiExecution;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import excelreaders.ReadTestCaseName;
import testsuite.ReservationNowTestSuite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIOperation{

    private WebDriver driver;    
    private static final Logger logger = LogManager.getLogger(UIOperation.class);

    // Initialize WebDriver based on the browser type
    public void UIOperationdriver(String browser){
        logger.info("Initializing WebDriver for browser: {}", browser);
        
        String projectDir = System.getProperty("user.dir");

        try {
            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                System.setProperty("webdriver.chrome.driver", projectDir + "/src/test/resources/Drivers/chromedriver.exe");
//                System.setProperty("webdriver.chrome.driver", "C:\\Users\\kanni\\eclipse-workspace\\sitatesting\\src\\test\\resources\\Drivers\\chromedriver.exe");
                this.driver  = new ChromeDriver(chromeOptions);
                logger.info("Chrome driver initialized");
                
            } else if (browser.equalsIgnoreCase("firefox")) {
                System.setProperty("webdriver.gecko.driver", projectDir + "/src/test/resources/Drivers/geckodriver.exe");
                FirefoxOptions options = new FirefoxOptions();
                options.setCapability("marionette", true);
                this.driver = new FirefoxDriver(options);
                logger.info("Firefox driver initialized");
                
            } else {
                throw new IllegalArgumentException("Invalid browser value: " + browser);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage());
            throw e;
        }
    }    

    // Execute test case steps
    public void Execution(String TestCaseName, String SheetName)throws Exception {
        logger.info("Executing test case: {}", TestCaseName);

        ReadTestCaseName readTestCaseName = new ReadTestCaseName();
        Map<String, Map<String, String>> testCaseMap = readTestCaseName.getTestCaseDetails(TestCaseName, SheetName);

        // Iterate over the outer map
        for (Map.Entry<String, Map<String, String>> entry : testCaseMap.entrySet()) {
            Map<String, String> detailsMap = entry.getValue();

            String Keyword = "", Object = "", ObjectType = "", Value = "";

            // Iterate over the inner map
            for (Map.Entry<String, String> innerEntry : detailsMap.entrySet()) {
                String key = innerEntry.getKey();
                String value = innerEntry.getValue();

                logger.debug("Key: {}, Value: {}", key, value);

                if (key.equalsIgnoreCase("Keyword")) {
                    Keyword = value;
                }

                if (key.equalsIgnoreCase("Object")) {
                    Object = value;
                }

                if (key.equalsIgnoreCase("ObjectType")) {
                    ObjectType = value;
                }

                if (key.equalsIgnoreCase("value")) {
                    Value = value;

                    if (value.equalsIgnoreCase("Date_Ahead2")) {
                        // Calculate the date 2 days ahead from the current date
                        LocalDate currentDate = LocalDate.now();
                        LocalDate checkInDate = currentDate.plusDays(2);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjust the format as needed
                        String formattedDate = checkInDate.format(formatter);

                        Value = formattedDate;
                    }
                }
            }

            perform(Object, Keyword, Object, ObjectType, Value);
        }
    }

    // Perform the specified operation on a web element
    private void perform(String p, String operation, String objectName, String objectType, String value) throws Exception {
        logger.info("Performing operation: {}", operation);

        int attempt = 0;
        boolean isSuccessful = false;
        int maxRetries = 5;

        while (attempt < maxRetries && !isSuccessful) {
            try {
                switch (operation.toUpperCase()) {
                    case "CLICK":
                        // Perform click
                        WebElement element = driver.findElement(this.getObject(p, objectName, objectType));

                        try {
                            element.click();
                        } catch (Exception e) {
                            logger.warn("Normal click failed, attempting JavaScript click.");

                            // Scroll into view and click using JavaScript
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

                            // Wait for the element to be clickable
                            WebDriverWait wait = new WebDriverWait(driver, 10);
                            element = wait.until(ExpectedConditions.elementToBeClickable(element));

                            // Click the element with JavaScript to avoid interception
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        }
                        break;

                    case "SETTEXT":
                        // Set text on control
                        driver.findElement(this.getObject(p, objectName, objectType)).sendKeys(value);
                        break;

                    case "GOTOURL":
                        // Get url of application
                        driver.get(value);
                        break;

                    case "GETTEXT":
                        // Get text of an element
                        driver.findElement(this.getObject(p, objectName, objectType)).getText();
                        break;

                    case "SELECT":
                        // Create a Select instance and select the option by value
                        new Select(driver.findElement(this.getObject(p, objectName, objectType))).selectByValue(value);
                        break;
                     
                    case "TEARDOWN":
                        // TEARDOWN the application
                    	Thread.sleep(1000);
                        driver.quit();
                        break;

                    default:
                        throw new Exception("Invalid operation: " + operation);
                }
                isSuccessful = true; // If the operation succeeds, set the flag to true
                logger.info("Operation {} performed successfully", operation);
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxRetries) {
                    logger.error("Operation {} failed after {} attempts", operation, maxRetries);
                    throw e; // Rethrow the exception if the maximum number of retries is reached
                }
                logger.warn("Attempt {} failed. Retrying...", attempt);
                Thread.sleep(1000); // Optional: wait before retrying
            }
        }
    }

    /**
     * Find element BY using object type and value
     * @param p
     * @param objectName
     * @param objectType
     * @return
     * @throws Exception
     */
    private By getObject(String p, String objectName, String objectType) throws Exception {
        logger.info("Locating element. ObjectType: {}, ObjectName: {}", objectType, objectName);

        // Find by xpath
        if (objectType.equalsIgnoreCase("XPATH")) {
            return By.xpath(p);
        }
        // Find by class
        else if (objectType.equalsIgnoreCase("CLASSNAME")) {
            return By.className(p);
        }
        // Find by name
        else if (objectType.equalsIgnoreCase("NAME")) {
            return By.name(p);
        }
        // Find by css
        else if (objectType.equalsIgnoreCase("CSS")) {
            return By.cssSelector(p);
        }
        // Find by link
        else if (objectType.equalsIgnoreCase("LINK")) {
            return By.linkText(p);
        }
        // Find by partial link
        else if (objectType.equalsIgnoreCase("PARTIALLINK")) {
            return By.partialLinkText(p);
        }
        // Find by ID
        else if (objectType.equalsIgnoreCase("ID")) {
            return By.id(p);
        } else {
            logger.error("Wrong object type: {}", objectType);
            throw new Exception("Wrong object type");
        }
    }
}