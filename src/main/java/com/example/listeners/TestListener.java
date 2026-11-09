package com.example.listeners;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.example.base.BaseClass;
import com.example.utilities.ExtentManager;
import com.example.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{

    //triggered when suite ends
    @Override
    public void onFinish(ITestContext context) {
        //Flush the Extent Report
        ExtentManager.endTest();
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    //Triggered when a suite Starts
    @Override
    public void onStart(ITestContext context) {
        //Initialize the Extent Reports
        ExtentManager.getReporter();
    }

    //Triggered when a Test Fails
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMsg = result.getThrowable().getMessage();
        try {
            ExtentManager.logStep(failureMsg);
            if(!result.getTestClass().getName().toLowerCase().contains("api")){
                ExtentManager.logFailure(BaseClass.getWebDriver(), "Test Failed!", "Test End: " + testName + " - ❌ Test Failed");
            }
            else{
                ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Triggered when test skips
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped: " + testName);
    }

    //Triggered when the test start
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        //Start logging in Extent Report
        ExtentManager.stratTest(testName);
        ExtentManager.logStep("Test Started: " + testName);
    }

    //Triggered when a test is pass
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        try {
            if(!result.getTestClass().getName().toLowerCase().contains("api")){
                ExtentManager.logStepWithScreenShot(BaseClass.getWebDriver(), "Test Passed!", "Test End: " + testName + " - ✔ Test Passed");
            }
            else{
                ExtentManager.logValidationAPI("Test End: " + testName + " - ✔ Test Passed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
