package com.ihorpolataiko;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SharesListRetriever {
    private final static String faceBookUrl = "https://facebook.com";
    private final ChromeDriver chromeDriver;
    private final AppConfig appConfig;

    public SharesListRetriever(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.chromeDriver = prepareDriver();
    }

    public List<UserDescription> retrieveList() throws InterruptedException {
        login(appConfig.getEmail(), appConfig.getPassword());
        goToPost(appConfig.getLink());
        openSharesList();
        scrollPageDown();
        Thread.sleep(appConfig.getPause());
        List<UserDescription> fetchedData = fetchUsersList().stream()
                .distinct()
                .collect(Collectors.toList());
        chromeDriver.close();
        return fetchedData;
    }

    private ChromeDriver prepareDriver() {
        System.setProperty("webdriver.chrome.driver", appConfig.getDriverLocation());
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-notifications");
        if (!appConfig.isSeeMagic()) {
            options.addArguments("--headless");
        }
        return new ChromeDriver(options);
    }

    private void login(String email, String password) {
        chromeDriver.get(faceBookUrl);

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

    private void goToPost(String link) {
        chromeDriver.get(link);
    }

    private void openSharesList() throws InterruptedException {
        while (true) {
            try {
                Thread.sleep(appConfig.getPause());
                WebElement element = chromeDriver.findElements(By.cssSelector("a[class='_3rwx _42ft']"))
                        .stream()
                        .findFirst()
                        .get();

                Thread.sleep(appConfig.getPause() / 2);
                chromeDriver.executeScript("arguments[0].scrollIntoView(false);", element);
                Thread.sleep(appConfig.getPause() / 2);
                element.click();
                Thread.sleep(appConfig.getPause());
                return;
            } catch (ElementNotVisibleException e) {
                closePopUpWindows();
                closeNotificationWindows();
                Thread.sleep(appConfig.getPause());
            }
        }
    }

    private void scrollPageDown() throws InterruptedException {
        Thread.sleep(appConfig.getPause());
        int currentScrollHeight = 0;
        int scrollStep = 150;
        String scrollBefore;
        String scrollAfter;
        do {
            currentScrollHeight += scrollStep;
            scrollBefore = chromeDriver.executeScript("return window.pageYOffset;").toString();
            chromeDriver.executeScript("window.scrollTo(0, " + currentScrollHeight + ")");
            Thread.sleep(appConfig.getPause() / 10);
            scrollAfter = chromeDriver.executeScript("return window.pageYOffset;").toString();
        } while (!scrollBefore.equals(scrollAfter));
    }

    private void closePopUpWindows() {
        while (true) {
            try {
                Thread.sleep(appConfig.getPause());
                chromeDriver.findElement(By.cssSelector("a[class='_xlt _418x']")).click();
                Thread.sleep(appConfig.getPause() / 2);
            } catch (Exception e) {
                break;
            }
        }
    }

    private void closeNotificationWindows() {
        while (true) {
            try {
                Thread.sleep(appConfig.getPause());
                chromeDriver.findElement(By.cssSelector("div[class='_n8 _3qx uiLayer _3qw']")).click();
                Thread.sleep(appConfig.getPause() / 2);
            } catch (Exception e) {
                break;
            }
        }
    }

    private List<UserDescription> fetchUsersList() {
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

    private String removeRedundantParamsFromUrl(String url) {
        return url.substring(0, url.indexOf("__tn__=") - 1);
    }

}
