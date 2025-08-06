package frameworkAutomate.tests;

import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import frameworkAutomate.core.DriverFactory;
import frameworkAutomate.utils.ConfigReader;

public class BaseTest {

	protected WebDriver driver;

	// Configura el WebDriver de acuerdo al navegador especificado en el archivo de configuración

	@BeforeMethod
	public void setup() {
		driver = DriverFactory.getDriver();
		driver.get(ConfigReader.get("base.url"));
	}

	@AfterMethod
	public void teardown() {
		DriverFactory.quitDriver();
	}
}
