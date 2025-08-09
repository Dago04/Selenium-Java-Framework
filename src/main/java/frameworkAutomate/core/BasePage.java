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

	// Navigate to the base URL defined in the configuration
	public void goTo(String url) {
		driver.get(url);
		waits.documentReady();
	}

	// Get the url of the current page
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	// Get the title of the current page
	public String getTitle() {
		return driver.getTitle();
	}

	// Refresh the current page and wait for it to load
	public void refreshAndWait() {
		driver.navigate().refresh();
		waits.documentReady();
	}

	// Navigate back to the previous page and wait for it to load
	public void backAndWait() {
		driver.navigate().back();
		waits.documentReady();
	}

	/* ============================ Finders ============================ */

	/** Find an element waiting for it to be visible */
	protected WebElement find(By locator) {
		return waits.visible(locator);
	}

	/**
	 * Find an element waiting for it to be visible in the DOM(no necesary visible)
	 */
	protected WebElement findPresent(By locator) {
		return waits.present(locator);
	}

	/**
	 * Find a list of elements waiting for them to be visible
	 */
	public List<WebElement> getListOfElements(By locator) {
		return waits.visibleAll(locator);
	}

	/** Present list of elements (without requiring visibility). */
	public List<WebElement> getAll(By locator) {
		return waits.presenceOfAll(locator);
	}

	/*
	 * ============================ Safe interactions ============================
	 */

	/**
	 * Resilient click: wait for it to be clickable, center in the viewport, retry,
	 * and fall back to JS if necessary.
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

	public void selectByVisibleText(By locator, String value) {
		withRetry(() -> {
			Select select = new Select(waits.visible(locator));
			select.selectByVisibleText(value);
			return null;
		});
	}

	public void selectByValue(By locator, String value) {
		withRetry(() -> {
			Select select = new Select(waits.visible(locator));
			select.selectByValue(value);
			return null;
		});
	}

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

	/** Read text from an element located */
	public String read(By locator) {
		return waits.visible(locator).getText().trim();
	}

	/*
	 * ============================ Element state checks
	 * ============================
	 */

	/* Return true if the element is displayed, false otherwise */
	public boolean isDisplayed(By locator) {
		try {
			return waits.visible(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/** Return true if the element is enabled, false otherwise */
	public boolean isEnabled(By locator) {
		try {
			return waits.visible(locator).isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/** Return true if the element is selected, false otherwise */
	public boolean isSelected(By locator) {
		try {
			return waits.visible(locator).isSelected();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/*
	 * ============================ Page waits/URL
	 * ============================
	 */

	public void waitForUrlContains(String fragment) {
		waits.urlContains(fragment);
	}

	public void waitForUrlToBe(String url) {
		waits.urlToBe(url);
	}

	public void waitForTitleContains(String text) {
		waits.titleContains(text);
	}

	public void waitForInvisibility(By locator) {
		waits.invisible(locator);
	}

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