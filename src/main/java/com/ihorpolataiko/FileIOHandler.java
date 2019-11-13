package com.ihorpolataiko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class FileIOHandler {
    public static AppConfig readPropertyFile() throws IOException {
        InputStream input = new FileInputStream("src/main/resources/app.properties");
        Properties prop = new Properties();
        prop.load(input);

        return AppConfig.builder()
                .email(prop.getProperty("facebook.login"))
                .password(prop.getProperty("facebook.password"))
                .seeMagic(prop.getProperty("app.see_magic").equals("true"))
                .driverLocation(prop.getProperty("app.driver.location"))
                .link(prop.getProperty("target.link"))
                .outputFileLocation(prop.getProperty("output.file.location"))
                .build();
    }

    public static void saveToCsv(List<UserDescription> userDescriptions, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                new File(filePath + "/peopleWhoHasSharedThePost.txt")))) {
            for (UserDescription userDescription : userDescriptions) {
                writer.write("\"" + userDescription.getTitle() + "\",\"" + userDescription.getLink() + "\"\n\r");
            }
        } catch (IOException e) {
            System.out.println("Some errors while saving data to file...");
        }
    }
}
