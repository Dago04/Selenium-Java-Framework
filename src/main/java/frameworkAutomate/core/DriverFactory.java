package frameworkAutomate.core;

import frameworkAutomate.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Crea y cierra UNA única instancia de WebDriver.
 * No contempla ejecución paralela.
 */
public class DriverFactory {

    private static WebDriver driver;   // instancia única

    /** Devuelve la instancia si existe; si no, la crea. */
    public static WebDriver getDriver() {
        if (driver == null) driver = createDriver();
        return driver;
    }

    /** Cierra la instancia y la “borra”. */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /* ---------------- PRIVADO ---------------- */
    private static WebDriver createDriver() {

    	String browser = ConfigReader.get("browser");
        int implicitSec = ConfigReader.getInt("implicit.timeout", 5);

       
        boolean headlessCfg = Boolean.parseBoolean(ConfigReader.get("headless", "false"));
        boolean headless    = headlessCfg || "true".equalsIgnoreCase(System.getenv("CI"));

        WebDriver drv;

        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fOpts = new FirefoxOptions();
                if (headless) fOpts.addArguments("-headless");
                drv = new FirefoxDriver(fOpts);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                drv = new EdgeDriver();                
            }
            default -> { // chrome
                WebDriverManager.chromedriver().setup();
                ChromeOptions cOpts = new ChromeOptions();

                if (headless) {
                    cOpts.addArguments("--headless=new",
                                       "--no-sandbox",
                                       "--disable-dev-shm-usage");
                }
              

                drv = new ChromeDriver(cOpts);
            }
        }

        drv.manage().timeouts()
           .implicitlyWait(Duration.ofSeconds(implicitSec));
        drv.manage().window().setSize(new Dimension(1920, 1080));
        return drv;
    }
}
