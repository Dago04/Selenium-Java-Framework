package frameworkAutomate.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import frameworkAutomate.listeners.ExtentTestListener;
import frameworkAutomate.pages.HomePage;
import frameworkAutomate.utils.ConfigReader;

public class HomePageTest extends BaseTest {

	@Test(groups = {"smoke"}, description = "Validate navigation to Home Page")
	public void Test_01_ValidateNavigationToHomePage() {
		
		ExtentTest log = ExtentTestListener.getTest();
		
		log.info("Starting Home Page navigation test");
		
		HomePage homePage = new HomePage(driver);
		
		String actualUrl = ConfigReader.get("base.url");
		String expectedUrl = homePage.getCurrentUrl();
		
		System.out.println("Actual URL: " + actualUrl);
		System.out.println("Expected URL: " + expectedUrl);
		
		log.info("Validating that category cards are visible on the Home Page");
		Assert.assertTrue(homePage.isCategoryCardsVisible(), "Category cards are not visible");
		
		log.info("Comparing actual URL with expected URL");
		Assert.assertTrue(actualUrl.contains(expectedUrl), "The URL is incorrect");
	}
}
