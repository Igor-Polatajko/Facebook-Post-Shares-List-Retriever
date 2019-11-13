package com.ihorpolataiko;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {
    private static String faceBookUrl = "https://facebook.com";
    private static ChromeDriver chromeDriver;
    private static String email;
    private static String password;
    private static String link;
    private static boolean seeMagic;

    static {
        readPropertyFile();
        chromeDriver = prepareDriver();
    }


    public static void main(String[] args) throws InterruptedException {
        chromeDriver.get(faceBookUrl);
        login(email, password);
        goToPost(link);
        closePopUpWindows();
        openSharesList();
        scrollPageDown();
        Thread.sleep(2000);
        fetchUsersList().forEach(System.out::println);
        chromeDriver.close();
    }

    private static ChromeDriver prepareDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chrome_driver\\chromedriver.exe");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        if (!seeMagic) {
            options.addArguments("--headless");
        }
        return new ChromeDriver(options);
    }

    private static void login(String email, String password) {
        WebElement passwordField;
        WebElement loginBtn;

        try {
            passwordField = chromeDriver.findElement(By.name("password"));
        } catch (NoSuchElementException e) {
            passwordField = chromeDriver.findElement(By.name("pass"));
        }

        try {
            loginBtn = chromeDriver.findElement(By.name("login"));
        } catch (NoSuchElementException e) {
            loginBtn = chromeDriver.findElement(By.id("loginbutton"));
        }

        chromeDriver.findElement(By.name("email")).sendKeys(email);
        passwordField.sendKeys(password);
        loginBtn.click();
    }

    private static void goToPost(String link) {
        chromeDriver.get(link);
    }

    private static void openSharesList() {
        chromeDriver.findElements(By.cssSelector("a[class='_3rwx _42ft']"))
                .stream()
                .findFirst()
                .get()
                .click();
    }

    private static void scrollPageDown() {
        // ToDo
    }

    private static void closePopUpWindows() {
        try {
            Thread.sleep(2000);
            WebElement element = chromeDriver.findElement(By.cssSelector("a[class='_xlt _418x']"));
            element.click();
            Thread.sleep(1000);
        } catch (NoSuchElementException | InterruptedException e) {
            // Everything is good here
        }
    }

    private static List<UserDescription> fetchUsersList() {
        return chromeDriver.findElements(
                By.xpath(".//div[@id='repost_view_dialog']//h5[@class='_7tae _14f3 _14f5 _5pbw _5vra']" +
                        "/span[@class='fwn fcg']/span[@class='fcg']/span[@class='fwb']/a[@class='profileLink']"))
                .stream()
                .map(webElement -> UserDescription
                        .builder()
                        .title(webElement.getAttribute("title"))
                        .link(removeRedundantParamsFromUrl(webElement.getAttribute("href")))
                        .build())
                .collect(Collectors.toList());
    }

    private static String removeRedundantParamsFromUrl(String url) {
        return url.substring(0, url.indexOf("__tn__=") - 1);
    }

    private static void readPropertyFile() {
        try (InputStream input = new FileInputStream("src\\main\\resources\\app.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            email = prop.getProperty("facebook.login");
            password = prop.getProperty("facebook.password");
            seeMagic = prop.getProperty("app.see_magic").equals("true");
            link = prop.getProperty("target.link");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
