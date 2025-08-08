package frameworkAutomate.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import frameworkAutomate.core.BasePage;

public class HomePage extends BasePage {
	
	protected WebDriver driver;
	
	// Locators
	private final By categoryCards = By.cssSelector(".category-cards");
	private final By elementsCardText = By.cssSelector("div.category-cards .card-body h5");
	
	
	public HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;	
	}
	
	/**
	 * Check if category cards are visible on the home page
	 * @return true if category cards are visible, false otherwise
	 */
	
	public boolean isCategoryCardsVisible() {
		return waits.visible(categoryCards).isDisplayed();
	}
	
	
}
