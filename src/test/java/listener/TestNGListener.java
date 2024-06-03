package listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utility.LogUtil;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import excelreaders.ReadTestCaseName;

public class TestNGListener implements ITestListener {
	
	private static final Logger logger = LogManager.getLogger(ITestListener.class);

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/reports";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    public void onTestStart(ITestResult result) {
        String logMessage = "Test started: " + result.getName();
        logMessage += "\nStart time: " + getCurrentTime();
        LogUtil.log(getLogFileName(result), logMessage);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String logMessage = "Test passed: " + result.getName();
        logMessage += "\nEnd time: " + getCurrentTime();
        logMessage += "\nParameters: " + Arrays.toString(result.getParameters());
        LogUtil.log(getLogFileName(result), logMessage);
        updateTestResult(result, "PASS");
        extractAndLogJiraId(result);
        
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String logMessage = "Test failed: " + result.getName();
        logMessage += "\nEnd time: " + getCurrentTime();
        logMessage += "\nParameters: " + Arrays.toString(result.getParameters());
        logMessage += "\nReason: " + result.getThrowable();
        logMessage += "\nStack Trace: " + Arrays.toString(result.getThrowable().getStackTrace());
        LogUtil.log(getLogFileName(result), logMessage);
        updateTestResult(result, "FAIL");
        extractAndLogJiraId(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String logMessage = "Test skipped: " + result.getName();
        logMessage += "\nEnd time: " + getCurrentTime();
        logMessage += "\nParameters: " + Arrays.toString(result.getParameters());
        LogUtil.log(getLogFileName(result), logMessage);
        updateTestResult(result, "SKIP");
        extractAndLogJiraId(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String logMessage = "Test failed but within success percentage: " + result.getName();
        logMessage += "\nEnd time: " + getCurrentTime();
        logMessage += "\nParameters: " + Arrays.toString(result.getParameters());
        LogUtil.log(getLogFileName(result), logMessage);
    }

    @Override
    public void onStart(ITestContext context) {
        String logMessage = "Test suite started: " + context.getName();
        logMessage += "\nStart time: " + getCurrentTime();
        LogUtil.log(getLogFileName(context), logMessage);
    }

    @Override
    public void onFinish(ITestContext context) {
        String logMessage = "Test suite finished: " + context.getName();
        logMessage += "\nEnd time: " + getCurrentTime();
        LogUtil.log(getLogFileName(context), logMessage);
    }

    private String getLogFileName(ITestResult result) {
        return "logs/" + result.getTestClass().getRealClass().getSimpleName() + "_" + result.getName() + ".log";
    }

    private String getLogFileName(ITestContext context) {
        return "logs/" + context.getName() + ".log";
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    private void updateTestResult(ITestResult result, String status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO test_results (test_name, test_class, status) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, result.getMethod().getMethodName());
            statement.setString(2, result.getTestClass().getName());
            statement.setString(3, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void extractAndLogJiraId(ITestResult result) {
        ReadTestCaseName readTestCaseName = new ReadTestCaseName();
        try {
            String jiraId = readTestCaseName.getJiraId(result.getName(), "KeywordFramework");
            
            logger.info(jiraId +" "+ "----- >" + " " + "Need to Implement for the Jira or Test Rail API" );
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}