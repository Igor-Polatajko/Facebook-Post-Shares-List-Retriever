package com.ihorpolataiko;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
                .build();
    }
}
