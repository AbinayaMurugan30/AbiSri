package com.teknotrait.webautomation.fileHandles;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileHandling {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/env.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    //  Resolves config paths like logsPath=logs/ to an absolute path
    public static String getAbsolutePath(String key) {
        String relativePath = get(key);  // e.g., "logs/"
        return new File(System.getProperty("user.dir"), relativePath).getAbsolutePath();
    }
}


