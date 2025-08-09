package frameworkAutomate.tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import frameworkAutomate.listeners.ExtentTestListener;
import frameworkAutomate.pages.RegisterPage;
import frameworkAutomate.utils.ConfigReader;

public class RegisterPageTest extends BaseTest {
	
	
	@Test(groups = { "smoke" }, description = "Validate navigation to Register Page")
	public void Test_01_ValidateNavigationToRegisterPage() {
		ExtentTest log = ExtentTestListener.getTest();
		
		log.info("Starting Home Page navigation test");

		RegisterPage registerPage = new RegisterPage(driver);
		
		registerPage.goTo(ConfigReader.get("base.url") + "register");
		
		log.info("Validating that the Register Page url is correct");
		assert registerPage.getCurrentUrl().equals(ConfigReader.get("base.url") + "register") : "Register Page URL is incorrect";
	}
}
