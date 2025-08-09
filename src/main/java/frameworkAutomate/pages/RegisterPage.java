package frameworkAutomate.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import frameworkAutomate.core.BasePage;

public class RegisterPage extends BasePage {

	protected WebDriver driver;

	// Locators
	private final By firstNameLabel = By.id("firstName-label");
	private final By lastNameLabel = By.id("lastName-label");
	private final By userNameLabel = By.id("userName-label");
	private final By passwordLabel = By.id("password-label");
	private final By firstNameInput = By.id("firstName");
	private final By lastNameInput = By.id("lastName");
	private final By userNameInput = By.id("userName");
	private final By passwordInput = By.id("password");
	private final By captchaCheckbox = By.id("recaptcha-anchor");
	private final By registerButton = By.id("register");

	public RegisterPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}
}
