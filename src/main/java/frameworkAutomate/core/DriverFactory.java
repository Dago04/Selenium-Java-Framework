package frameworkAutomate.core;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import frameworkAutomate.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Crea y cierra una ÚNICA instancia WebDriver para el hilo principal. No
 * contempla ejecución paralela.
 */
public class DriverFactory {

	private static WebDriver driver; // Instancia única del WebDriver

	/**
	 * Devuelve la instancia del WebDriver. Si no existe, crea una nueva instancia.
	 * 
	 * @return WebDriver
	 */
	public static WebDriver getDriver() {
		if (driver == null)
			driver = createDriver();
		return driver;
	}

	/** Cierra la instancia actual y la pone a null. */
	public static void quitDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	/* ----------- método privado ----------- */
	private static WebDriver createDriver() {
		String browser = ConfigReader.get("browser");
		int implicit = ConfigReader.getInt("implici.timeout",5);

		WebDriver drv;

		switch (browser) {
		case "firefox" -> {
			// Configura el WebDriver de Firefox con WebDriverManager
			WebDriverManager.firefoxdriver().setup();
			drv = new FirefoxDriver();
		}
		case "edge" -> {
			// Configura el WebDriver de Edge con WebDriverManager
			WebDriverManager.edgedriver().setup();
			drv = new EdgeDriver();
		}
		default -> {
			// chrome por defecto
			WebDriverManager.chromedriver().setup();
			drv = new ChromeDriver();
		}

		}
		drv.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
		drv.manage().window().maximize();
		return drv;
	}

}
