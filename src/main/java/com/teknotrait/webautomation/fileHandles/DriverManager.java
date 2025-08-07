package com.teknotrait.webautomation.fileHandles;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;



import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void setDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        String version = System.getProperty("browserVersion", "");
        String executionMode = System.getProperty("executionMode", "local").toLowerCase(); // local or grid
        String gridURL = System.getProperty("gridURL", "http://localhost:4444/wd/hub");
        String headless = System.getProperty("headless", "false").toLowerCase();

        try {
            switch (browser) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless.equals("true")) {
                        firefoxOptions.addArguments("--headless");
                        firefoxOptions.addArguments("--width=1920");
                        firefoxOptions.addArguments("--height=1080");
                    }
                    if (executionMode.equals("grid")) {
                        driver.set(new RemoteWebDriver(new URL(gridURL), firefoxOptions));
                    } else {
                        if (!version.isEmpty()) {
                            WebDriverManager.firefoxdriver().browserVersion(version).setup();
                        } else {
                            WebDriverManager.firefoxdriver().setup();
                        }
                        driver.set(new FirefoxDriver(firefoxOptions));
                    }
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--inprivate");
                    if (headless.equals("true")) {
                        edgeOptions.addArguments("--headless=new");
                        edgeOptions.addArguments("--disable-gpu");
                        edgeOptions.addArguments("--window-size=1920,1080");
                    }
                    if (executionMode.equals("grid")) {
                        driver.set(new RemoteWebDriver(new URL(gridURL), edgeOptions));
                    } else {
                        if (!version.isEmpty()) {
                            WebDriverManager.edgedriver().browserVersion(version).setup();
                        } else {
                            WebDriverManager.edgedriver().setup();
                        }
                        driver.set(new EdgeDriver(edgeOptions));
                    }
                    break;

                case "chrome":
                default:
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--disable-popup-blocking");
                    options.addArguments("--disable-notifications");
                    options.addArguments("--start-maximized");

                    if (headless.equals("true")) {
                        options.addArguments("--headless=new");
                        options.addArguments("--disable-gpu");
                        options.addArguments("--window-size=1920,1080");
                    }

                    options.setCapability("platformName", "Windows");
                    options.setCapability("browserVersion", "latest");

                    String crxPath = executionMode.equals("grid")
                            ? "/opt/Extensions/Adblock.crx" // For Grid nodes
                            : "./Extensions/Adblock.crx";   // For Local machine

                    File extensionFile = new File(crxPath);
                    if (extensionFile.exists()) {
                        options.addExtensions(extensionFile);
                    } else {
                        System.out.println("CRX file not found: " + extensionFile.getAbsolutePath());
                    }

                    if (executionMode.equals("grid")) {
                        driver.set(new RemoteWebDriver(new URL(gridURL), options));
                    } else {
                        WebDriverManager.chromedriver().setup();
                        driver.set(new ChromeDriver(options));
                    }
                    break;
            }

            driver.get().manage().window().maximize();
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + e.getMessage());
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}

