package com.example.test;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.example.base.BaseClass;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.utilities.DBConnection;
import com.example.utilities.DataProviders;
import com.example.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(getWebDriver());
        homePage = new HomePage(getWebDriver());
    }

    @Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
    public void verifyEmployeeNameFromDB(String empID, String empName) throws IOException {
        // ExtentManager.stratTest("Home Page Logo Verification Test"); -- THis has been
        // implemented in TestListener

        SoftAssert softAssert = getSoftAssert();

        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        ExtentManager.logStep("Click on PIM Tab");
        homePage.clickOnPIMTab();

        ExtentManager.logStep("Search for Employee");
        homePage.employeeSearch(empName);

        ExtentManager.logStep("Get the employee Name from DB");
        String emp_id = empID;
        Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(emp_id);
        String firstName = employeeDetails.get("firstName");
        String middleName = employeeDetails.get("middleName");
        String lastName = employeeDetails.get("lastName");

        String emplFirstNameAndMiddleName = (firstName + " " + middleName).trim();

        ExtentManager.logStep("Verify the employee first and middle name");
        softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstNameAndMiddleName),
            "First and Middle Name are not matching");

        ExtentManager.logStep("Verify the employee last name");
        Assert.assertTrue(homePage.verifyEmployeeLastName(lastName), "Last Name not matching");

        ExtentManager.logStep("DB Validation Done!");

        softAssert.assertAll();
        
    }
}
