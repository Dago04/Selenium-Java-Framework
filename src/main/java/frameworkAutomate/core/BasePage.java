package frameworkAutomate.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/** Superclass of all Page Objects. */
public abstract class BasePage {

	protected final WebDriver driver;
	protected final Waits waits;
	private final Actions actions;

	protected BasePage(WebDriver driver) {
		this.driver = Objects.requireNonNull(driver, "driver cannot be null");
		this.waits = new Waits(driver);
		this.actions = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	/*
	 * ============================ Navigation / State ============================
	 */

	/**
	 * Performs the 'goTo' operation.
	 *
	 * @param url Parameter of type String.
	 */
	public void goTo(String url) {
		driver.get(url);
		waits.documentReady();
	}

	/**
	 * Returns the current browser URL.
	 * 
	 * @return The result of type String.
	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Returns the current document title.
	 * 
	 * @return The result of type String.
	 */
	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * Refreshes the page and waits for it to be fully loaded.
	 */
	public void refreshAndWait() {
		driver.navigate().refresh();
		waits.documentReady();
	}

	/**
	 * Navigates back and waits for the page to be fully loaded.
	 */
	public void backAndWait() {
		driver.navigate().back();
		waits.documentReady();
	}

	/* ============================ Finders ============================ */

	/**
	 * Find an element waiting for it to be visible in the DOM
	 *
	 * @param locator The locator to find the element.
	 * @return The found WebElement.
	 */
	public WebElement find(By locator) {
		return waits.visible(locator);
	}

	/**
	 * Find an element waiting for it to be present in the DOM (without requiring
	 * visibility).
	 *
	 * @param locator The locator to find the element.
	 * @return The found WebElement.
	 */
	public WebElement findPresent(By locator) {
		return waits.present(locator);
	}

	/**
	 * Find a list of elements waiting for them to be visible in the DOM
	 *
	 * @param locator The locator to find the elements.
	 * @return The list of found WebElements.
	 */

	public List<WebElement> getListOfElements(By locator) {
		return waits.visibleAll(locator);
	}

	/**
	 * Find a list of elements waiting for them to be present in the DOM(without
	 * requiring visibility).
	 * 
	 * @param locator The locator to find the elements.
	 * @return The list of found WebElements.
	 */
	public List<WebElement> getAll(By locator) {
		return waits.presenceOfAll(locator);
	}

	/*
	 * ============================ Safe interactions ============================
	 */

	/**
	 * Click an element waiting for it to be clickable, scrolls into view, and
	 * clicks.
	 */
	public void click(By locator) {
		withRetry(() -> {
			WebElement element = waits.clickable(locator);
			scrollIntoViewCenter(element);
			try {
				element.click();
			} catch (Exception clickEx) {
				jsClick(element);
			}
			return null;
		});
	}

	/** Send text securely: focus, clean with various strategies, and then write. */
	public void write(By locator, CharSequence text) {
		withRetry(() -> {
			WebElement element = waits.visible(locator);
			scrollIntoViewCenter(element);
			focus(element);
			element.clear();
			element.sendKeys(text);
			return null;
		});
	}

	/**
	 * Selects an option in a <select> by its visible text.
	 *
	 * @param locator Parameter of type By.
	 * @param value   Parameter of type String.
	 */
	public void selectByVisibleText(By locator, String value) {
		withRetry(() -> {
			Select select = new Select(waits.visible(locator));
			select.selectByVisibleText(value);
			return null;
		});
	}

	/**
	 * Selects an option in a <select> by its value.
	 *
	 * @param locator Parameter of type By.
	 * @param value   Parameter of type String.
	 */

	public void selectByValue(By locator, String value) {
		withRetry(() -> {
			Select select = new Select(waits.visible(locator));
			select.selectByValue(value);
			return null;
		});
	}

	/**
	 * Selects an option in a <select> by its index.
	 *
	 * @param locator Parameter of type By.
	 * @param index   Parameter of type int.
	 */
	public void selectByIndex(By locator, int index) {
		withRetry(() -> {
			Select select = new Select(waits.visible(locator));
			select.selectByIndex(index);
			return null;
		});
	}

	/** Hover using actions. */
	public void hover(By locator) {
		WebElement el = waits.visible(locator);
		scrollIntoViewCenter(el);
		actions.moveToElement(el).perform();
	}

	/** Upload a file using a <input type="file">. */
	public void uploadFile(By inputLocator, Path filePath) {
		WebElement input = waits.present(inputLocator);
		input.sendKeys(filePath.toAbsolutePath().toString());
	}

	/*
	 * ============================ Read / SoftAssertion
	 * ============================
	 */

	/**
	 * Reads the text of an element waiting for it to be visible.
	 *
	 * @param locator Parameter of type By.
	 * @return The text of the element as a String.
	 */
	public String read(By locator) {
		return waits.visible(locator).getText().trim();
	}

	/*
	 * ============================ Element state checks
	 * ============================
	 */

	/**
	 * Return true if the element is displayed, false otherwise.
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean isDisplayed(By locator) {
		try {
			return waits.visible(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Return true if the element is enabled, false otherwise.
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean isEnabled(By locator) {
		try {
			return waits.visible(locator).isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Return true if the element is selected (for checkboxes/radio buttons),
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean isSelected(By locator) {
		try {
			return waits.visible(locator).isSelected();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/*
	 * ============================ Page waits/URL ============================
	 */

	/**
	 * Return true if the current URL contains the specified fragment, false
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean waitForUrlContains(String fragment) {
		try {
			return waits.urlContains(fragment);
		} catch (NoSuchElementException e) {
			return false; // If the URL is not found, return false
		}
	}

	/**
	 * Return true if the current URL is exactly the specified URL, false
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean waitForUrlToBe(String url) {
		try {
			return waits.urlToBe(url);
		} catch (NoSuchElementException e) {
			return false; // If the URL is not found, return false
		}

	}

	/**
	 * Return true if the page title contains the specified text, false
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean waitForTitleContains(String text) {
		try {
			return waits.titleContains(text);
		} catch (NoSuchElementException e) {
			return false; // If the title is not found, return false
		}
	}

	/**
	 * Return true if the page title is exactly the specified text, false
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public boolean waitForTitle(String text) {
		try {
			return waits.titleIs(text);
		} catch (NoSuchElementException e) {
			return false; // If the title is not found, return false
		}
	}

	/**
	 * Return true if an alert is present, false otherwise.
	 * 
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */
	public Alert waitForAlert() {
		return waits.alertIsPresent();
	}

	/**
	 * ============================ Scrolling / JS helper
	 * ============================
	 */

	public void scrollIntoViewCenter(By locator) {
		scrollIntoViewCenter(waits.present(locator));
	}

	public void scrollIntoViewCenter(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});",
				element);
	}

	public void jsClick(By locator) {
		jsClick(waits.present(locator));
	}

	public void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void focus(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].focus();", element);
	}

	/* ============================ Infra: Retries ============================ */

	/**
	 * Return the result of a supplier action with retries for specific exceptions.
	 * 
	 * @param action A Supplier that returns a result of type T.
	 * @return The result of type T.
	 */

	protected <T> T withRetry(Supplier<T> action) {
		RuntimeException last = null;
		for (int i = 0; i < 3; i++) {
			try {
				return action.get();
			} catch (StaleElementReferenceException | NoSuchElementException e) {
				last = e;
				sleep(120);
			} catch (RuntimeException e) {
				last = e;
				sleep(120);
			}
		}
		if (last != null)
			throw last;
		return null;
	}

	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

}