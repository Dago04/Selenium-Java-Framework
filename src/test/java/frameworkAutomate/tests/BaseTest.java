package frameworkAutomate.tests;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import frameworkAutomate.utils.ConfigReader;

public class BaseTest {

	protected WebDriver driver;

	@BeforeClass
	public void setupSuite() {
		WebDriverManager.chromedriver().setup(); // Descarga el driver si hiciera falta
	}

	@BeforeMethod
	public void setup() {
		String browser = ConfigReader.get("browser");
		
		driver = switch(browser) {
		case "edge" -> new EdgeDriver();
		case "firefox" -> new FirefoxDriver();
		default -> new ChromeDriver();
		};
		driver.manage().timeouts()
        .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.timeout")));
		driver.manage().window().maximize();
		driver.get(ConfigReader.get("base.url"));
	}

	@AfterMethod
	public void teardown() {
		if (driver != null)
			driver.quit();
	}
}
