package com.example.test;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.example.utilities.ApiUtility;
import com.example.utilities.ExtentManager;
import io.restassured.response.Response;

public class ApiTest {
    @Test 
    public void verifyGetUserAPI() throws IOException{

        SoftAssert softAssert = new SoftAssert();

        //Step 1 : Define API Endpoint
        String endPoint = "https://jsonplaceholder.typicode.com/users/1";
        ExtentManager.logStep("API Endpoint: " + endPoint);

        //Step 2: Send GET request
        ExtentManager.logStep("Sending GET request to the API");
        Response response = ApiUtility.sendGetRequest(endPoint);

        //Step 3: Validate the status code
        ExtentManager.logStep("Validating API status code");
        boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
        softAssert.assertTrue(isStatusCodeValid, "Status code is not valid for GET response");
        if(isStatusCodeValid){
            ExtentManager.logValidationAPI("Sattus Code Validation Passed!");
        }
        else{
            ExtentManager.logFailureAPI("Sattus Code Validation Failed!");    
        }

        //Step 4: Validate Username
        ExtentManager.logStep("Validating response body for Username");
        String username = ApiUtility.getJsonValue(response, "username");
        boolean isUsernameValid = "Bret".equals(username);
        softAssert.assertTrue(isUsernameValid, "Username is not matched for GET response");
        if(isUsernameValid){
            ExtentManager.logValidationAPI("UserName Validation Passed!");
        }
        else{
            ExtentManager.logFailureAPI("UserName Validation Failed!");    
        }

        //Step 4: Validate Email
        ExtentManager.logStep("Validating response body for email");
        String email = ApiUtility.getJsonValue(response, "email");
        boolean isEmailValid = "Sincere@april.biz".equals(email);
        softAssert.assertTrue(isEmailValid, "Email is not matched for GET response");
        if(isEmailValid){
            ExtentManager.logValidationAPI("Email Validation Passed!");
        }
        else{
            ExtentManager.logFailureAPI("Email Validation Failed!");    
        }

        softAssert.assertAll();
    }
}
