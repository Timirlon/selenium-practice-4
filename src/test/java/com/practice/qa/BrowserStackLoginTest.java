package com.practice.qa;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowserStackLoginTest {

    private static final String USERNAME = "bsuser_ut2xjm";
    private static final String ACCESS_KEY = "MV8BaUyEnCpxqfcGsCAc";
    private static final String BROWSERSTACK_URL =
            "https://" + USERNAME + ":" + ACCESS_KEY + "@hub.browserstack.com/wd/hub";

    private WebDriver createDriver(String browserName) throws Exception {

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("browserVersion", "latest");

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", "OS X");
        bstackOptions.put("osVersion", "Ventura");
        bstackOptions.put("projectName", "SQAT Assignment 6");
        bstackOptions.put("buildName", "Cross Browser Test");
        bstackOptions.put("sessionName", "Login Test on " + browserName);
        bstackOptions.put("seleniumVersion", "4.21.0");

        capabilities.setCapability("bstack:options", bstackOptions);

        return new RemoteWebDriver(new URL(BROWSERSTACK_URL), capabilities);
    }

    @Test
    void loginTestOnChrome() throws Exception {
        runTest("Chrome");
    }

    @Test
    void loginTestOnSafari() throws Exception {
        runTest("Safari");
    }

    private void runTest(String browser) throws Exception {

        WebDriver driver = createDriver(browser);

        try {
            driver.get("https://the-internet.herokuapp.com/login");
            Thread.sleep(2000);

            driver.findElement(By.id("username")).sendKeys("tomsmith");
            driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            Thread.sleep(3000);

            assertTrue(driver.getPageSource().contains("Secure Area"));
            System.out.println("✅ Test passed on " + browser);

        } finally {
            driver.quit();
        }
    }
}

