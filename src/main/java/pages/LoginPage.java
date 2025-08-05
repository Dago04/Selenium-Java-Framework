package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

	@FindBy(id = "username")
	private WebElement txtUser;
	@FindBy(id = "password")
	private WebElement txtPassword;
	@FindBy(css = "button[type='submit']")
	private WebElement btnLogin;
	@FindBy(id = "flash")
	private WebElement lblFlash;

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public LoginPage enterUsername(String username) {
		txtUser.sendKeys(username);
		return this;
	}

	public LoginPage enterPassword(String password) {
		txtPassword.sendKeys(password);
		return this;
	}

	public SecureAreaPage clickLoginExitoso() {
		btnLogin.click();
		return new SecureAreaPage(driver);
	}

	public String getMessage() {
		return lblFlash.getText();
		
	}
}
