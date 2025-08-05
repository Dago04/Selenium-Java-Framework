package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class BaseTest {

	protected WebDriver driver;

	@BeforeClass
	public void setupSuite() {
		WebDriverManager.chromedriver().setup(); // Descarga el driver si hiciera falta
	}

	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void teardown() {
		if (driver != null)
			driver.quit();
	}
}
