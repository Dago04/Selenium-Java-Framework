package frameworkAutomate.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/** Superclass of all Page Objects. */
public abstract class BasePage {

	protected WebDriver driver;
	protected Waits waits;
	
	protected BasePage(WebDriver driver) {
		this.driver = driver;
		this.waits = new Waits(driver);
		PageFactory.initElements(driver, this);
	}
	
	/* ---------- interaction wrappers ---------- */
	
	// Click on an element located by the By
	protected void click(By locator) {
		waits.clickable(locator).click();
	}
	
	// Write text in an element located by the By
	protected void write(By locator, String text) {
		WebElement element = waits.visible(locator);
		element.clear();
		element.sendKeys(text);
	}
	
	// Read text from an element located by the By
	protected String read(By locator) {
		return waits.visible(locator).getText();
	}
	
	// Read the title of the current page
	public String getTitle() {
		return driver.getTitle();
	}
}