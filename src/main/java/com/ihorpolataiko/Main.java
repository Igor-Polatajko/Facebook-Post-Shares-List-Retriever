package com.ihorpolataiko;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Provide path to application properties file");
            System.exit(1);
        }

        FileIOHandler fileIOHandler = new FileIOHandler(args[0]);
        AppConfig appConfig = fileIOHandler.readPropertyFile();
        SharesListRetriever sharesListRetriever = new SharesListRetriever(appConfig);

        System.out.println("Processing.....");
        List<UserDescription> userDescriptions = sharesListRetriever.retrieveList();

        System.out.println("==========================");
        System.out.println("Length of list: " + userDescriptions.size());
        userDescriptions.forEach(System.out::println);
        System.out.println("==========================");

        System.out.println("Saving data....");
        fileIOHandler.saveToCsv(userDescriptions, appConfig.getOutputFileLocation());
        System.out.println("Process is finished successfully!");
    }
}
