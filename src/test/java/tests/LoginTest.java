package tests;

import pages.LoginPage;
import pages.SecureAreaPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{
	@Test
	public void loginSuccess() throws InterruptedException {
		
		// arranque
		driver.get("https://the-internet.herokuapp.com/login");
	
		LoginPage loginPage = new LoginPage(driver);
		
		SecureAreaPage secure = new LoginPage(driver)
				.enterUsername("tomsmith")
				.enterPassword("SuperSecretPassword!")
				.clickLoginExitoso();
		
		
		Thread.sleep(2000); // Espera para ver el resultado (no recomendado en producción)
		Assert.assertTrue(loginPage.getMessage().contains("You logged into a secure area!"), "El título no es correcto");
	}
}
