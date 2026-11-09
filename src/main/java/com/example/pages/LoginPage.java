package com.example.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.example.actiondriver.ActionDriver;
import com.example.base.BaseClass;

public class LoginPage {
    private ActionDriver actionDriver;

    // Define locators using By Class
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    //Initialize the ActionDriver object
    public LoginPage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Method to perform login
    public void login(String username, String password) throws IOException {
        actionDriver.enterText(userNameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    // Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() throws IOException {
        return actionDriver.isDisplayed(errorMessage);
    }

    // Method to get the text from Error message
    public String getErrorMessage() {
        return actionDriver.getText(errorMessage);
    }

    // Verify if error is correct or not
    public boolean verifyErrorMessage(String expectedError) {
        return actionDriver.compareText(errorMessage, expectedError);
    }
}
