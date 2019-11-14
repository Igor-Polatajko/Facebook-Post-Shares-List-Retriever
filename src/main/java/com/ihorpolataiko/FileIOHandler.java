package com.ihorpolataiko;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class FileIOHandler {
    private final String propertiesLocation;

    public FileIOHandler(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }

    public AppConfig readPropertyFile() throws IOException {
        InputStream input;
        input = new FileInputStream(propertiesLocation);
        Properties prop = new Properties();
        prop.load(input);

        return AppConfig.builder()
                .email(prop.getProperty("facebook.login"))
                .password(prop.getProperty("facebook.password"))
                .seeMagic(prop.getProperty("app.see_magic").equals("true"))
                .driverLocation(prop.getProperty("app.driver.location"))
                .pause(Long.parseLong(prop.getProperty("app.pause")))
                .link(prop.getProperty("target.link"))
                .outputFileLocation(prop.getProperty("output.file.location"))
                .build();
    }

    public void saveToCsv(List<UserDescription> userDescriptions, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                filePath + "/peopleWhoHasSharedThePost.txt"))) {
            for (UserDescription userDescription : userDescriptions) {
                writer.write("\"" + userDescription.getTitle() + "\",\"" + userDescription.getLink() + "\"");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Some errors while saving data to file...");
        }
    }
}
