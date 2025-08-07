package com.teknotrait.webautomation.fileHandles;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.Scenario;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<String> logFilePath = new ThreadLocal<>();

    // Initialize ExtentReports only once
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("report/ExtentReport.html");
            reporter.config().setReportName("Automation Test Report");
            reporter.config().setDocumentTitle("Luma Web Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Abinaya");
            extent.setSystemInfo("Environment", "Test Environment");
        }
        return extent;
    }

    // Create a new ExtentTest for each scenario
    public static void createTest(String name) {
        ExtentTest scenarioTest = extent.createTest(name);
        test.set(scenarioTest);
    }

    // Get current ExtentTest
    public static ExtentTest getTest() {
        return test.get();
    }

    // Set scenario-specific log file path
    public static void setLogFilePath(String path) {
        logFilePath.set(path);
    }

// Get scenario-specific log file path
    public static String getLogFilePath() {
        return logFilePath.get();
    }

    // Attach screenshot and mark scenario as failed in report
    public static void captureFailureScreenshot(WebDriver driver, Scenario scenario) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Failure Screenshot");

        getTest().addScreenCaptureFromBase64String(ts.getScreenshotAs(OutputType.BASE64), "Failure Screenshot");
        getTest().fail("Scenario failed: " + scenario.getName());
    }

    // Attach log file link to the report
    public static void attachLogToReport() throws Exception {
    	String logPath = logFilePath.get();
        File logFile = new File(logPath);

        System.out.println("Checking log file: " + logFile.getAbsolutePath());
        System.out.println("Exists: " + logFile.exists());

        if (logFile.exists()) {
            String fileName = logFile.getName(); 
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String logContent = new String(Files.readAllBytes(logFile.toPath()), StandardCharsets.UTF_8);
            
            String encodedLog = Base64.getEncoder().encodeToString(logContent.getBytes(StandardCharsets.UTF_8));

        //   String downloadLink = "<a download='execution.log' href='data:text/plain;base64 ," + encodedLog + "'>Download Execution Log</a>";
            String downloadLink = "<a download='" + fileName + "' href='data:text/plain;base64," + encodedLog + "'>Download Execution Log </a>";
            getTest().info(downloadLink);
            getTest().info("<details><summary>Click to View Execution Log </summary><pre>" + logContent + "</pre></details>");
        } else {
            getTest().info("Log file not found or not generated properly.");
        }
        

    }



    }

