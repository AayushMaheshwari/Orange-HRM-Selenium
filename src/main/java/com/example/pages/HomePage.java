package com.example.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.example.actiondriver.ActionDriver;
import com.example.base.BaseClass;

public class HomePage {
    private ActionDriver actionDriver;

    // Define locators using By Class
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIDButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    private By pimTab = By.xpath("//span[text()='PIM']");
    private By employeeSearchBox = By
            .xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By emplFirstNameAndMiddleName = By.xpath("//div[@class='oxd-table-card']//div//div[3]");
    private By emplLastName = By.xpath("//div[@class='oxd-table-card']//div//div[4]");

    // Initialize the ActionDriver object
    public HomePage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Method to verfy if Admin Tab is visible
    public boolean isAdminTabVisisble() throws IOException {
        return actionDriver.isDisplayed(adminTab);
    }

    // Method to verify OrangeHRM logo is visible
    public boolean verifyOrangeHRMLogo() throws IOException {
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    // Method to perform logout operation
    public void logout() throws IOException {
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }

    // Method to navigate to PIM Tab
    public void clickOnPIMTab() throws IOException {
        actionDriver.click(pimTab);
    }

    // Employee search
    public void employeeSearch(String value) throws IOException {
        actionDriver.enterText(employeeSearchBox, value);
        actionDriver.click(searchButton);
        actionDriver.scrollToElement(emplFirstNameAndMiddleName);
    }

    // Verify employee first and middle name
    public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
        return actionDriver.compareText(emplFirstNameAndMiddleName, emplFirstAndMiddleNameFromDB);
    }

    // Verify employee last name
    public boolean verifyEmployeeLastName(String emplLastNameFromDB) {
        return actionDriver.compareText(emplLastName, emplLastNameFromDB);
    }
}
