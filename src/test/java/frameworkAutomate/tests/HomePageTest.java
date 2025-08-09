package frameworkAutomate.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;
import frameworkAutomate.listeners.ExtentTestListener;
import frameworkAutomate.pages.HomePage;
import frameworkAutomate.utils.ConfigReader;

public class HomePageTest extends BaseTest {

	@Test(groups = { "smoke" }, description = "Validate navigation to Home Page")
	public void Test_01_ValidateNavigationToHomePage() {

		ExtentTest log = ExtentTestListener.getTest();
		
		HomePage homePage = new HomePage(driver);
		homePage.goTo(ConfigReader.get("base.url"));
		
		log.info("Starting Home Page navigation test");

		String actualUrl = ConfigReader.get("base.url");
		String expectedUrl = homePage.getCurrentUrl();

		log.info("Comparing actual URL with expected URL");
		Assert.assertTrue(actualUrl.contains(expectedUrl), "The URL is incorrect");
	}

	@Test(groups = { "smoke" }, description = "Validate clicking on card")
	public void Test_02_ValidateClickingOnCard() {

		ExtentTest log = ExtentTestListener.getTest();
		HomePage homePage = new HomePage(driver);
		homePage.goTo(ConfigReader.get("base.url"));
		
		String cardName = ConfigReader.get("cardname");

		log.info(String.format("Starting '%s' card click test", cardName));

		log.info(String.format("Clicking on the '%s' card", cardName));
		homePage.clickOnCardByName(cardName);

		log.info(String.format("Validating that the URL contains the expected path for '%s'", cardName));
		String expectedUrl = ConfigReader.get("base.url") + cardName.toLowerCase();
		Assert.assertTrue(homePage.getCurrentUrl().contains(expectedUrl),
				String.format("The URL is incorrect after clicking on '%s' card", cardName));

		log.pass(String.format("'%s' card click test completed successfully", cardName));
	}
}
