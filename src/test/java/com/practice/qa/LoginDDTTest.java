package com.practice.qa;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginDDTTest {

    private static WebDriver driver;

    @Test
    void loginDataDrivenTest() throws Exception {

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        InputStream file =
                getClass().getClassLoader().getResourceAsStream("data/login_data.xlsx");

        Assertions.assertNotNull(file);
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            String email = row.getCell(0).getStringCellValue();
            String password = row.getCell(1).getStringCellValue();
            String expected = row.getCell(2).getStringCellValue();

            driver.get("https://www.phptravels.net/login");
            Thread.sleep(2000);

            if (!email.isEmpty()) {
                driver.findElement(By.name("email")).sendKeys(email);
            }
            if (!password.isEmpty()) {
                driver.findElement(By.name("password")).sendKeys(password);
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            By submitButton = By.id("submitBTN");

            wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));

            WebElement button = driver.findElement(submitButton);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            wait.until(ExpectedConditions.elementToBeClickable(button));

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", button);

            Thread.sleep(3000);

            String actual;
            if (driver.getCurrentUrl().contains("/dashboard")) {
                actual = "SUCCESS";
            } else {
                actual = "FAILURE";
            }

            System.out.println("Row " + i + ": " + actual);

            assertEquals(expected, actual);

            driver.manage().deleteAllCookies();
        }

        workbook.close();
        file.close();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

