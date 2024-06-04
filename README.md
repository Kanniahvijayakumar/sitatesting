<b>High Level Architecture diagram :</b>

The testing framework follows a modular architecture designed for efficient UI testing of web applications. Selenium is utilized for executing UI tests, driven by test data in Excel DDF and KDF formats. TestNG generates comprehensive test reports, while a dedicated component updates results to popular test management tools like TestRail and Jira. A MySQL database stores relevant test data and results. A TestNG Listener component logs errors, generates customized reports, and captures test case details and trends. The framework supports testing across multiple browser types and allows running individual UI test cases in a pipeline. Additionally, it facilitates different testing types, including smoke, sanity, and regression testing, ensuring comprehensive coverage of the application under test.

![Diagram Name](https://github.com/Kanniahvijayakumar/sitatesting/blob/master/ArchitectureDiagram/SitaArchitecture.jpg).

<b>Project - FIleStructure :</b>

![Diagram Name](https://github.com/Kanniahvijayakumar/sitatesting/blob/master/ArchitectureDiagram/SitaFIleStructure.jpg).


<b> excelreaders Folder : </b>

ReadExcel.java -
 
This Java class is designed to read Excel files (both .xls and .xlsx formats) using the Apache POI library.

ReadTestCaseName.java -

This Java class reads test case details from an Excel sheet and retrieves specific information, such as test case steps and associated Jira IDs / Test Rail IDS.

<b>listener Folder :</b>

This TestNGListener class implements the ITestListener interface from TestNG to provide custom handling of test execution events. Key functionalities include logging test start, success, failure, and skip events with detailed messages and timestamps using LogUtil, and updating test results in a MySQL database. Additionally, it logs information about associated Jira / Test Rail IDs for test cases by reading them from an external source. This listener aims to enhance test execution tracking, reporting, and integration with external test management tools.


<b>testsuite Folder :</b>

The ReservationNowTestSuite class is a part of the testsuite package and is designed to execute automated UI tests using Selenium WebDriver and TestNG.

<b> uiExecution Folder : </b>

This class, UIOperation, is designed for automating web-based UI tests. It leverages Selenium WebDriver to interact with web elements, allowing for operations such as clicking buttons, entering text, and navigating URLs. The class includes methods to initialize WebDriver for different browsers (Chrome and Firefox), execute test cases by reading test data from an Excel sheet, and perform specified operations on web elements. Logging is implemented using Log4j to provide detailed information about the execution process, including errors and retries. The perform method handles various UI actions, with built-in retries to enhance robustness. Additionally, the class includes a utility method to locate web elements based on different selectors like XPath, CSS, and ID.

<b> utility Folder : </b>

The LogUtil class provides a utility method log for writing log messages to a file

<b> Drivers Folder : </b>

It contains the exe file of chrome and Firefox webdrivers

<b> Excel Data : </b>

It contains an Excel sheet which includes test data ( DDF ), keywords ( KDF ), locators, and Jira/Testrail IDs.

<b> Logs Folder : </b>

It contains logs of individual test case, Test Suite and application.

<b> Pipelines Folder: </b>

This pipeline automates the process of checking out the code, building it with Maven, and running TestNG tests based on specified parameters ( BROWSER  and Type Of Testing ( Smoke, Regression and sanity ) )


<b> Pom.xml File : </b>

This is a Maven project configuration file (pom.xml). It defines project metadata such as group ID, artifact ID, and version. It also specifies project dependencies including Selenium WebDriver for automated web testing, TestNG for test execution and reporting, Apache POI for handling Excel files, MySQL connector for database connectivity, and log4j for logging. Additionally, it includes configuration for Maven plugins such as the Compiler Plugin and Surefire Plugin for compiling and executing tests respectively. You can uncomment and customize dependencies or plugins as needed for your project.

<b> TestNG.xml : </b>

This XML file is a TestNG test suite configuration. It defines a suite named "SitaTestingTestSuite" that includes a test named "SitaTesting". The suite specifies a listener called "TestNGListener" and a parameter named "browser" with a placeholder value "${browser}". This parameter allows flexibility in specifying the browser at runtime. The test "SitaTesting" includes a class named "ReservationNowTestSuite" to execute the test cases.


<b> Note : </b>

Please run the code using the following command in CMD - mvn test -Dbrowser=chrome -DtestngXmlFile=TestNG.xml
