package frameworkAutomate.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import frameworkAutomate.pages.LoginPage;
import frameworkAutomate.pages.SecureAreaPage;
import frameworkAutomate.utils.ConfigReader;

public class LoginTest extends BaseTest{
	@Test
	public void loginSuccess() throws InterruptedException {
		

		LoginPage loginPage = new LoginPage(driver);
				
		SecureAreaPage secure = new LoginPage(driver)
				.writeUser(ConfigReader.get("username"))
				.writePassword(ConfigReader.get("password"))
				.clickLogin();
		
		Assert.assertTrue(loginPage.getMessage().contains("You logged into a secure area!"), "The message is incorrect");
	}
}
