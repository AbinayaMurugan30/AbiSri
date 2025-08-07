package com.teknotrait.webautomation.stepDefinitions;

import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.teknotrait.webautomation.fileHandles.DriverManager;
import com.teknotrait.webautomation.fileHandles.EmailSender;
import com.teknotrait.webautomation.fileHandles.ExtentReportManager;
import com.teknotrait.webautomation.fileHandles.PropertyFileHandling;
import com.teknotrait.webautomation.fileHandles.TestResultLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

	public class Hooks {
		private long scenarioStartTime;
		@Before
		public void setUp(Scenario scenario) {

		    // Setup the report FIRST
		    ExtentReportManager.getExtentReports();
		    scenarioStartTime = System.currentTimeMillis();
		    

		    // Format scenario name and timestamp
		    String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
		    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		    // Logging setup
		    String logs = PropertyFileHandling.getAbsolutePath("logsPath");
		    String logPath = logs + File.separator + scenarioName + "_" + timestamp + ".log";

		    ThreadContext.put("scenarioName", scenarioName + "_" + timestamp);
		    ThreadContext.put("logFilename", logPath);

		    ExtentReportManager.createTest(scenario.getName());  // Now safe to call
		    ExtentReportManager.setLogFilePath(logPath);

		    // Start browser driver
		    DriverManager.setDriver();

		    // Get base URL from Maven system property
		    String baseUrl = System.getProperty("baseUrl");

		    if (baseUrl == null || baseUrl.isEmpty()) {
		        throw new IllegalArgumentException("Base URL not provided. Use -DbaseUrl=<url> when running the tests.");
		    }

		    // Navigate to the entered URL
		    DriverManager.getDriver().get(baseUrl);
		}



	@After
	public void tearDown(Scenario scenario) throws Exception {
		
		WebDriver driver = DriverManager.getDriver();
		long duration = 
				System.currentTimeMillis()- scenarioStartTime;

	    String testName = scenario.getName();
	    String status = scenario.isFailed() ? "fail" : "pass";
	    String error = scenario.isFailed() ? scenario.getStatus().toString() : null;
	    String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");

	    TestResultLogger.logTestResult(testName, status, duration, error, scenarioName);

		if (scenario.isFailed()) {
			ExtentReportManager.captureFailureScreenshot(driver, scenario);

		} else {
			ExtentReportManager.getTest().pass("Scenario passed: " + scenario.getName());
		}
		ExtentReportManager.attachLogToReport();
		DriverManager.quitDriver();

	}

	@AfterAll
	public static void afterAll() {
		ExtentReportManager.getExtentReports().flush();

		LogManager.shutdown();
		EmailSender.sendReportByEmail();

	}
}
