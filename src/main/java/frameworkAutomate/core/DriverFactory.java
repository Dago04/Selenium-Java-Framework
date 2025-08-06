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
 * Creates and closes ONE single instance of WebDriver. Does not support
 * parallel execution.
 */
public class DriverFactory {

	private static WebDriver driver; // single instance

	/** Returns the instance if it exists; if not, creates it. */
	public static WebDriver getDriver() {
		if (driver == null)
			driver = createDriver();
		return driver;
	}

	/** Closes the instance and "deletes" it. */
	public static void quitDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	/* ---------------- PRIVATE ---------------- */
	private static WebDriver createDriver() {

		String browser = ConfigReader.get("browser");
		int implicitSec = ConfigReader.getInt("implicit.timeout", 5);

		boolean headlessCfg = Boolean.parseBoolean(ConfigReader.get("headless", "false"));
		boolean headless = headlessCfg || "true".equalsIgnoreCase(System.getenv("CI"));

		WebDriver drv;

		switch (browser) {
		case "firefox" -> {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions fOpts = new FirefoxOptions();
			if (headless)
				fOpts.addArguments("-headless");
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
				cOpts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
			}

			drv = new ChromeDriver(cOpts);
		}
		}

		drv.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitSec));
		drv.manage().window().setSize(new Dimension(1920, 1080));
		return drv;
	}
}