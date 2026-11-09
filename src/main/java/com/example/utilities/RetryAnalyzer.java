package com.example.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

    private int retryCount = 0;
    private static final int maxRetryCount = 2;

    @Override
    public boolean retry(ITestResult arg0) {
        if(retryCount<maxRetryCount){
            retryCount++;
            return true;
        }
        return false;
    }
    
}
