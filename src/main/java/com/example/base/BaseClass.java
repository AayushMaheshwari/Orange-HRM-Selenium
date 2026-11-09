package com.example.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.example.actiondriver.ActionDriver;
import com.example.utilities.ExtentManager;
import com.example.utilities.LoggerManager;

public class BaseClass {
    protected static Properties prop;
    // protected static WebDriver driver;
    // private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    // Getter method for softAssert
    public SoftAssert getSoftAssert() {
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        // load the config file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file loaded");

        // Start the Extent Report
        // ExtentManager.getReporter(); -- THis has been implemented in TestListener
    }

    public synchronized void launchBrowser() {
        // Initiliaze the webdriver based on browser defined in config.properties f ile
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            // Create ChromeOptions
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

            // driver = new ChromeDriver();
            driver.set(new ChromeDriver(options)); // New Changes as per Thread
            ExtentManager.registerDriver(getWebDriver());
            logger.info("ChromeDriver Instance Created");
        } else if (browser.equalsIgnoreCase("firefox")) {
            // Create FirefoxOptions
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless"); // Run Firefox in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
            options.addArguments("--width=1920"); // Set browser width
            options.addArguments("--height=1080"); // Set browser height
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Needed for CI/CD environments
            options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

            // driver = new FirefoxDriver();
            driver.set(new FirefoxDriver(options)); // New Changes as per Thread
            ExtentManager.registerDriver(getWebDriver());
            logger.info("FirefoxDriver Instance Created");
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless"); // Run Edge in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU acceleration
            options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable pop-up notifications
            options.addArguments("--no-sandbox"); // Needed for CI/CD
            options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes

            // driver = new EdgeDriver();
            driver.set(new EdgeDriver(options)); // New Changes as per Thread;
            ExtentManager.registerDriver(getWebDriver());
            logger.info("EdgeDriver Instance Created");
        } else {
            throw new IllegalArgumentException("Invalid browser name" + browser);
        }
    }

    public void configureBrowser() {
        // Implicit Wait
        int ImplicitWait = Integer.parseInt(prop.getProperty("ImplicitWait"));
        getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ImplicitWait));

        // maximize the browser
        // getWebDriver().manage().window().maximize();

        boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless"));
        // Alternative check: Read your setup configs or string inputs directly if
        // preferred
        if (!isHeadless) {
            getWebDriver().manage().window().maximize();
        } else {
            logger.info("Skipping native window maximization for Headless mode to preserve explicit viewport sizes.");
        }

        // Navigate to URL
        try {
            String URL = prop.getProperty("url");
            getWebDriver().get(URL);
        } catch (Exception e) {
            System.out.println("Failed to Navigate to the URL: " + e.getMessage());
        }
    }

    @BeforeMethod
    public synchronized void setup() throws IOException {
        System.out.println("Setting up WebDriver for :" + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
        logger.info("WebDriver launced and Browser maximized");

        logger.trace("Trace Message");
        logger.error("Error Message");
        logger.debug("Debug Message");
        logger.fatal("Fatal Message");
        logger.warn("Warn Message");

        // Initialize actionDriver only once
        /*
         * if(actionDriver == null){
         * actionDriver = new ActionDriver(driver);
         * logger.info("Action Driver Instance Created" +
         * Thread.currentThread().threadId());
         * }
         */

        // Initialize ActionDriver for the current Thread
        actionDriver.set(new ActionDriver(getWebDriver()));
        logger.info("ActionDriver Instance Created for thread: " + Thread.currentThread().threadId());
    }

    @AfterMethod
    public synchronized void tearDown(ITestResult result) {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                if (result.getStatus() == ITestResult.SUCCESS || result.getStatus() == ITestResult.FAILURE) {
                    String outcome = result.getStatus() == ITestResult.SUCCESS ? "Test Passed" : "Test Failed";
                    ExtentManager.attachScreeshot(currentDriver, "Test End: " + result.getName() + " - " + outcome);
                }
                // close all the opened by webdriver sessions
                currentDriver.quit();
            } catch (Exception e) {
                System.out.println("Unable to Quit the Browser: " + e.getMessage());
            }
        }
        logger.info("Webdriver instance closed");
        driver.remove();
        actionDriver.remove();
        // driver = null;
        // actionDriver = null;
        // ExtentManager.endTest(); THis has been implemented in TestListener
    }

    // Getter method for prop
    public static Properties getProp() {
        return prop;
    }

    // Getter Method for WebDriver
    public static WebDriver getWebDriver() {
        if (driver.get() == null) {
            System.out.println("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    // Getter Method for ActionDriver
    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    // static method for pause
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
