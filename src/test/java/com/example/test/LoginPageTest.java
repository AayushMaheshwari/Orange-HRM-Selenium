package com.example.test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.example.base.BaseClass;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.utilities.DataProviders;
import com.example.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(getWebDriver());
        homePage = new HomePage(getWebDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username, String password) throws IOException {
        //ExtentManager.stratTest("Valid Login Test"); -- THis has been implemented in TestListener
        System.out.println("Running testMethod1 on thread: " + Thread.currentThread().threadId());

        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(username, password);

        ExtentManager.logStep("Verifying Admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisisble(), "Login Failed");
        ExtentManager.logStep("Validation Done!");

        homePage.logout();
        ExtentManager.logStep("Logged out successfully!");
        staticWait(5);
    }

    @Test(dataProvider = "InvalidLoginData", dataProviderClass = DataProviders.class)
    public void verifyInvalidLoginTest(String username, String password) throws IOException {
        // ExtentManager.stratTest("Invalid Login Test");   -- THis has been implemented in TestListener
        System.out.println("Running testMethod1=2 on thread: " + Thread.currentThread().threadId());

        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login success with invalid credentials");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test failed with Invalid error message");
        ExtentManager.logStep("Validation Done!");
    }
}
