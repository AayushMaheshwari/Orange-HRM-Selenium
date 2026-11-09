package com.example.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
    private static final String FILE_PATH = System.getProperty("user.dir")
            + "/src/test/java/resources/testdata/TestData.xlsx";

    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return getSheetData("ValidLoginData");
    }

    @DataProvider(name = "InvalidLoginData")
    public static Object[][] InvalidLoginData() {
        return getSheetData("inValidLoginData");
    }

    @DataProvider(name = "emplVerification")
    public static Object[][] emplVerificationData() {
        return getSheetData("emplVerification");
    }

    private static Object[][] getSheetData(String sheetName) {
        List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

        Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

        for (int i = 0; i < sheetData.size(); i++) {
            data[i] = sheetData.get(i);
        }

        return data;
    }
}
