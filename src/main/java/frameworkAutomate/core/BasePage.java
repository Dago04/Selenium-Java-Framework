package frameworkAutomate.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/** Superclase de todos los Page Objects. */
public abstract class BasePage {

	protected WebDriver driver;
	protected Waits waits;
	
	protected BasePage(WebDriver driver) {
		this.driver = driver;
		this.waits = new Waits(driver);
		PageFactory.initElements(driver, this);
	}
	
	/* ---------- wrappers de interacción ---------- */
	
	// Click en un elemento localizado por el By
	protected void click(By locator) {
		waits.clickable(locator).click();
	}
	
	// Escribir texto en un elemento localizado por el By
	protected void write(By locator, String text) {
		WebElement element = waits.visible(locator);
		element.clear();
		element.sendKeys(text);
	}
	
	// Leer texto de un elemento localizado por el By
	protected String read(By locator) {
		return waits.visible(locator).getText();
	}
	
	// Leer el título de la página actualAn
	public String getTitle() {
		return driver.getTitle();
	}
}
