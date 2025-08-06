package frameworkAutomate.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import frameworkAutomate.listeners.ExtentTestListener;
import frameworkAutomate.pages.LoginPage;
import frameworkAutomate.pages.SecureAreaPage;
import frameworkAutomate.utils.ConfigReader;

public class LoginTest extends BaseTest {
	@Test
	public void loginSuccess() throws InterruptedException {
		
		ExtentTest log = ExtentTestListener.getTest();
		
		log.info("Starting login test");
		
		LoginPage loginPage = new LoginPage(driver);

		log.info("Ingreso credenciales válidas");
		SecureAreaPage secure = new LoginPage(driver).writeUser(ConfigReader.get("username"))
				.writePassword(ConfigReader.get("password")).clickLogin();

		log.info("Verifico que aparezca el mensaje de éxito");
		Assert.assertTrue(loginPage.getMessage().contains("You logged into a secure area!"),
				"The message is incorrect");
		
		log.pass("Test finalizado con éxito ✔️");
	}
}
