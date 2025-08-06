package frameworkAutomate.core;

import frameworkAutomate.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;


/** API simple sobre WebDriverWait para evitar código repetido. */
public class Waits {
	
	private final WebDriver driver;
	private final WebDriverWait wait;
	
	public Waits(WebDriver driver) {
		this.driver = driver;
		int timeout = ConfigReader.getInt("explicit.timeout", 10);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
	}
	
	  /* ---------- helpers más comunes ---------- */
	public WebElement visible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public WebElement clickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public boolean textPresent(By locator, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}
}
