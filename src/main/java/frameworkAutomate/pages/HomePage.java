package frameworkAutomate.pages;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import frameworkAutomate.core.BasePage;

public class HomePage extends BasePage {

	protected WebDriver driver;

	// Locators
	private final By elementsCardText = By.cssSelector("div.category-cards .card-body h5");

	public HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	
	/**
	 * Get the element by its name from the Elements card.
	 * 
	 * @return webElement if found, otherwise throws NoSuchElementException
	 * @param cardName the name of the card to find
	 * @throws NoSuchElementException if the card with the specified name is not
	 *                                found
	 */
	public WebElement getCardByName(String cardName) {
		List<WebElement> cards = getListOfElements(elementsCardText);
		for (WebElement card : cards) {
			if (card.getText().equalsIgnoreCase(cardName)) {
				return card;
			}
		}
		throw new NoSuchElementException(
				String.format("The element with name '%s' was not found on the page.", cardName));
	}

	/**
	 * Click on a card by its name.
	 * 
	 * @param cardName the name of the card to click
	 * @throws NoSuchElementException if the card with the specified name is not
	 *                                found
	 */
	public void clickOnCardByName(String cardName) {
		WebElement card = getCardByName(cardName);
		if (card != null) {
			card.click();
		} else {
			throw new NoSuchElementException(
					String.format("The element with name '%s' was not found on the page.", cardName));
		}
	}

}
