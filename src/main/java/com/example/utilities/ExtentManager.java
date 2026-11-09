package com.example.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // Initialize the Extent Report
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir")
                    + "/src/test/java/resources/ExtendReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            // Adding system information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
    }

    // Start the Test
    public synchronized static ExtentTest stratTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    // End the Test
    public synchronized static void endTest() {
        getReporter().flush();
    }

    // Get Current Thread's test Name
    public synchronized static ExtentTest getTest() {
        return test.get();
    }

    // Method to get the name of the current test
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        } else {
            return "No Test is cuurently active for this Tread";
        }
    }

    // Log a step
    public static void logStep(String logMessage) {
        String colorMesage = String.format("<span style='color:green;'>%s</span>", logMessage);
        getTest().info(colorMesage);
    }

    // Log a step validation with screenshot
    public static void logStepWithScreenShot(WebDriver driver, String logMessage, String screenShotMessage) throws IOException {
        String colorMesage = String.format("<span style='color:green;'>%s</span>", logMessage);
        getTest().pass(colorMesage);

        // Screenshot method
        attachScreeshot(driver, screenShotMessage);
    }

    // Log a step validation for API
    public static void logValidationAPI(String logMessage) throws IOException {
        String colorMesage = String.format("<span style='color:green;'>%s</span>", logMessage);
        getTest().pass(colorMesage);
    }

    // Log a Failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) throws IOException {
        String colorMesage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMesage);

        // Screenshot method
        attachScreeshot(driver, screenShotMessage);
    }

    // Log a Failure for API
    public static void logFailureAPI(String logMessage) throws IOException {
        String colorMesage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMesage);
    }

    // Log a Skip
    public static void logSkip(String logMessage) {
        String colorMesage = String.format("<span style='color:yellow;'>%s</span>", logMessage);
        getTest().skip(colorMesage);
    }

    // Take screenshot with Date and Time
    public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        // Format Date and Time for file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        // Saving the screenshot to a file
        String destPath = System.getProperty("user.dir")
                + "/src/test/java/resources/screenshots/" + screenshotName + "_" + timeStamp + ".png";

        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert screenshot to Base64 for Embedding in the Report
        String base64Format = convertToBase64(src);
        return base64Format;
    }

    // Convert screenshot to Base64 format
    public static String convertToBase64(File screenShotFile) throws IOException {
        String base64Format = "";
        // Read a file content into a byte array
        byte[] fileContent;
        fileContent = FileUtils.readFileToByteArray(screenShotFile);
        base64Format = Base64.getEncoder().encodeToString(fileContent);
        return base64Format;
    }

    // Attach screenshot to report using Base64
    public synchronized static void attachScreeshot(WebDriver driver, String message) throws IOException {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot: " + message);
            e.printStackTrace();
        }
    }

    // Register WebDriver for current Thread
    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().threadId(), driver);
    }
}
