package frameworkAutomate.core;

import frameworkAutomate.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Waits is a reusable base component for page synchronization and stable
 * interactions.
 * <p>
 * This class centralizes explicit waits, safe element interactions, scrolling
 * helpers, page/URL synchronization, and small resilience mechanisms to reduce
 * test flakiness. It is designed to be extended by concrete Page Objects.
 */

public class Waits {

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final Duration defaultTimeout;
	private final Duration defaultPolling;

	public Waits(WebDriver driver) {
		this.driver = Objects.requireNonNull(driver, "driver cannot be null");

		int timeoutSec = ConfigReader.getInt("explicit.timeout", 10);
		int pollingMs = ConfigReader.getInt("explicit.polling", 200);

		this.defaultTimeout = Duration.ofSeconds(timeoutSec);
		this.defaultPolling = Duration.ofMillis(pollingMs);

		this.wait = new WebDriverWait(driver, defaultTimeout);

		this.wait.pollingEvery((defaultPolling)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class).ignoring(ElementNotInteractableException.class);
	}

	/**
	 * Create a temporal WebDriverWait with personalize time with the
	 * polling/ignores.
	 */
	public WebDriverWait withTimeout(Duration timeout) {
		WebDriverWait custom = new WebDriverWait(driver, timeout);
		custom.pollingEvery(defaultPolling).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class).ignoring(ElementClickInterceptedException.class)
				.ignoring(ElementNotInteractableException.class);
		return custom;
	}

	/**
	 * Performs the 'until' operation.
	 *
	 * @param Function<WebDriver Parameter.
	 * @param condition          Parameter of type T>.
	 * @return The result of type T.
	 */

	private <T> T until(Function<WebDriver, T> condition) {
		return wait.until(condition);
	}

	/* ---------- Basic waits ---------- */

	/**
	 * Waits for the element to be visible and returns it.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type WebElement.
	 */
	public WebElement visible(By locator) {
		return until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Waits for the element to be present in the DOM and returns it.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type WebElement.
	 */

	public WebElement present(By locator) {
		return until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * Waits for the element to be clickable and returns it.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type WebElement.
	 */
	public WebElement clickable(By locator) {
		return until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * Waits for the element to be invisible.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type boolean.
	 */

	public boolean invisible(By locator) {
		return until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * Waits for text to be present in the located element.
	 *
	 * @param locator Parameter of type By.
	 * @param text    Parameter of type String.
	 * @return The result of type boolean.
	 */

	public boolean textPresent(By locator, String text) {
		return until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	/**
	 * Waits for all elements to be visible and returns them.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type List<WebElement>.
	 */

	public List<WebElement> visibleAll(By locator) {
		return until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * Waits for all elements to be present in the DOM.
	 *
	 * @param locator Parameter of type By.
	 * @return The result of type List<WebElement>.
	 */

	public List<WebElement> presenceOfAll(By locator) {
		return until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	/**
	 * Waits until the number of elements equals the expected size.
	 *
	 * @param locator Parameter of type By.
	 * @param size    Parameter of type int.
	 * @return The result of type List<WebElement>.
	 */

	public List<WebElement> numberOfElementsToBe(By locator, int size) {
		return until(ExpectedConditions.numberOfElementsToBe(locator, size));
	}

	/*
	 * ============================ Page / Navigation ============================
	 */

	/**
	 * Waits until the document is ready.
	 * 
	 * @return The result of type boolean.
	 */

	public boolean documentReady() {
		return until(d -> {
			try {
				return "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState"));
			} catch (JavascriptException e) {
				return false;
			}
		});
	}

	/**
	 * Waits until the URL contains a given substring.
	 *
	 * @param fragment Parameter of type String.
	 * @return The result of type boolean.
	 */
	public boolean urlContains(String fragment) {

		return until(ExpectedConditions.urlContains(fragment));
	}

	/**
	 * Waits until the URL equals the expected value.
	 *
	 * @param url Parameter of type String.
	 * @return The result of type boolean.
	 */

	public boolean urlToBe(String url) {
		return until(ExpectedConditions.urlToBe(url));
	}

	/**
	 * Waits until the title contains text.
	 *
	 * @param text Parameter of type String.
	 * @return The result of type boolean.
	 */

	public boolean titleContains(String text) {
		return until(ExpectedConditions.titleContains(text));
	}

	/**
	 * Waits until the title is exactly the expected string.
	 *
	 * @param title Parameter of type String.
	 * @return The result of type boolean.
	 */

	public boolean titleIs(String title) {
		return until(ExpectedConditions.titleIs(title));
	}

	/*
	 * ============================ Attributes/ State ============================
	 */

	/**
	 * Waits until an element attribute equals a specific value.
	 *
	 * @param locator Parameter of type By.
	 * @param name    Parameter of type String.
	 * @param value   Parameter of type String.
	 * @return The result of type boolean.
	 */

	public boolean attributeToBe(By locator, String name, String value) {
		return until(ExpectedConditions.attributeToBe(locator, name, value));
	}

	/**
	 * Waits until an element attribute contains a value.
	 *
	 * @param locator Parameter of type By.
	 * @param name    Parameter of type String.
	 * @param value   Parameter of type String.
	 * @return The result of type boolean.
	 */
	public boolean attributeContains(By locator, String name, String value) {
		return until(ExpectedConditions.attributeContains(locator, name, value));
	}

	/**
	 * Waits until the given element becomes stale.
	 *
	 * @param element Parameter of type WebElement.
	 * @return The result of type boolean.
	 */

	public boolean stalenessOf(WebElement element) {
		return until(ExpectedConditions.stalenessOf(element));
	}

	/**
	 * Waits until the selection state equals the expected state.
	 *
	 * @param locator  Parameter of type By.
	 * @param selected Parameter of type boolean.
	 * @return The result of type boolean.
	 */

	public boolean elementSelectionStateToBe(By locator, boolean selected) {
		return until(ExpectedConditions.elementSelectionStateToBe(locator, selected));
	}

	/* ============================ Frames / Alerts ============================ */

	/**
	 * Waits until the frame is available and switches to it.
	 * 
	 * @return The result of type WebDriver.
	 */
	public WebDriver frameToBeAvailableAndSwitchToIt(By locator) {
		return until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}

	/**
	 * Waits until an alert is present and returns it.
	 * 
	 * @return The result of type Alert.
	 */
	public Alert alertIsPresent() {
		return until(ExpectedConditions.alertIsPresent());
	}
}
