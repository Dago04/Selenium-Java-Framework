package frameworkAutomate.tests;

import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import frameworkAutomate.core.DriverFactory;

public class BaseTest {

	protected WebDriver driver;

	/**
	 * Base class for all tests. It initializes the WebDriver and opens the base URL before each test.
	 * It also ensures that the WebDriver is closed after each test.
	 */
	@BeforeMethod
	public void setup() {
		driver = DriverFactory.getDriver();
		//driver.get(ConfigReader.get("base.url"));
	}

	@AfterMethod
	public void teardown() {
		DriverFactory.quitDriver();
	}
}
