package frameworkAutomate.core;

import frameworkAutomate.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
	
	 /** Create a temporal WebDriverWait with personalize time with the  polling/ignores. */
    public WebDriverWait withTimeout(Duration timeout) {
        WebDriverWait custom = new WebDriverWait(driver, timeout);
        custom.pollingEvery(defaultPolling)
              .ignoring(NoSuchElementException.class)
              .ignoring(StaleElementReferenceException.class)
              .ignoring(ElementClickInterceptedException.class)
              .ignoring(ElementNotInteractableException.class);
        return custom;
    }
	
	private <T> T until(Function<WebDriver, T> condition) {
        return wait.until(condition);
    }

	/* ---------- Basic waits ---------- */
	public WebElement visible(By locator) {
		return until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement present(By locator) {
		return until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public WebElement clickable(By locator) {
		return until(ExpectedConditions.elementToBeClickable(locator));
	}

	public boolean invisible(By locator) {
		return until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public boolean textPresent(By locator, String text) {
		return until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	public List<WebElement> visibleAll(By locator) {
		return until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public List<WebElement> presenceOfAll(By locator) {
		return until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public List<WebElement> numberOfElementsToBe(By locator, int size) {
		return until(ExpectedConditions.numberOfElementsToBe(locator, size));
	}

	/*
	 * ============================ Page / Navigation ============================
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

	public boolean urlContains(String fragment) {

		return until(ExpectedConditions.urlContains(fragment));
	}

	public boolean urlToBe(String url) {
		return until(ExpectedConditions.urlToBe(url));
	}

	public boolean titleContains(String text) {
		return until(ExpectedConditions.titleContains(text));
	}

	public boolean titleIs(String title) {
		return until(ExpectedConditions.titleIs(title));
	}

	/*
	 * ============================ Attributes/ State ============================
	 */

	public boolean attributeToBe(By locator, String name, String value) {
		return until(ExpectedConditions.attributeToBe(locator, name, value));
	}

	public boolean attributeContains(By locator, String name, String value) {
		return until(ExpectedConditions.attributeContains(locator, name, value));
	}

	public boolean stalenessOf(WebElement element) {
		return until(ExpectedConditions.stalenessOf(element));
	}

	public boolean elementSelectionStateToBe(By locator, boolean selected) {
		return until(ExpectedConditions.elementSelectionStateToBe(locator, selected));
	}
	
	/* ============================ Frames / Alerts ============================ */

    public WebDriver frameToBeAvailableAndSwitchToIt(By locator) {
        return until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public Alert alertIsPresent() {
        return until(ExpectedConditions.alertIsPresent());
    }
}
