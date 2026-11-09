package com.example.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.example.base.BaseClass;
import com.example.utilities.ExtentManager;

public class DummyClass extends BaseClass {
    @Test 
    public void dummyTest(){
        //ExtentManager.stratTest("DummyTest1 Test to verify the title");  -- THis has been implemented in TestListener

        String title = getWebDriver().getTitle();
        assert title.equals("OrangeHRM"):"Test Faied - Title is not matched";
        System.out.println("Test Passed - Title is Matched");

        ExtentManager.logSkip("This test case is skipped");
        // throw new SkipException("Skipping the test as part of Testing");
    }
}
