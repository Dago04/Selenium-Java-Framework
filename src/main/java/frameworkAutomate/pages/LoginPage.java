package frameworkAutomate.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import frameworkAutomate.core.BasePage;

public class LoginPage extends BasePage {

	protected WebDriver driver;
	/* Locators */
	private final By userTxt = By.id("username");
	private final By passTxt = By.id("password");
	private final By loginBtn = By.cssSelector("button[type='submit']");
	private final By flashLbl = By.id("flash");

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public LoginPage writeUser(String user) {
		write(userTxt, user);
		return this;
	}

	public LoginPage writePassword(String password) {
		write(passTxt, password);
		return this;
	}

	public SecureAreaPage clickLogin() {
		click(loginBtn);
		return new SecureAreaPage(driver);
	}

	public String getMessage() {
		return read(flashLbl);
	}
}
