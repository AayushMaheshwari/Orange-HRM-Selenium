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

public class HomePageTest extends BaseClass{
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod 
    public void setUpPages(){
        loginPage = new LoginPage(getWebDriver());
        homePage = new HomePage(getWebDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username, String password) throws IOException{
        //ExtentManager.stratTest("Home Page Logo Verification Test");  -- THis has been implemented in TestListener

        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(username, password);

        ExtentManager.logStep("Verifying Logo visible or not");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");
        ExtentManager.logStep("Validation Done!");
    }
}
