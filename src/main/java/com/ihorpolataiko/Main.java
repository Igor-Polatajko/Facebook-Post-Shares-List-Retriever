package com.ihorpolataiko;

import java.util.List;

import static com.ihorpolataiko.FileIOHandler.readPropertyFile;

public class Main {
    private static AppConfig appConfig;

    public static void main(String[] args) throws Exception {
        appConfig = readPropertyFile();

        SharesListRetriever sharesListRetriever = new SharesListRetriever(appConfig);
        List<UserDescription> userDescriptions = sharesListRetriever.retrieveList();
        System.out.println("Length of list: " + userDescriptions.size());
        userDescriptions.forEach(System.out::println);
    }
}
