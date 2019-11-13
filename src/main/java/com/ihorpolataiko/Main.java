package com.ihorpolataiko;

import java.util.List;

import static com.ihorpolataiko.FileIOHandler.readPropertyFile;
import static com.ihorpolataiko.FileIOHandler.saveToCsv;

public class Main {

    public static void main(String[] args) throws Exception {
        AppConfig appConfig = readPropertyFile();
        SharesListRetriever sharesListRetriever = new SharesListRetriever(appConfig);

        System.out.println("Processing.....");
        List<UserDescription> userDescriptions = sharesListRetriever.retrieveList();

        System.out.println("==========================");
        System.out.println("Length of list: " + userDescriptions.size());
        userDescriptions.forEach(System.out::println);
        System.out.println("==========================");

        System.out.println("Saving data....");
        saveToCsv(userDescriptions, appConfig.getOutputFileLocation());
        System.out.println("Process is finished successfully!");
    }
}
