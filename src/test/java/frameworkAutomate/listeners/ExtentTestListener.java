package frameworkAutomate.listeners;

import frameworkAutomate.core.DriverFactory;
import frameworkAutomate.core.ExtentManager;
import org.testng.*;
import com.aventstack.extentreports.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * TestNG listener that generates an ExtentReports report with automatic
 * screenshots in case of failure.
 */

public class ExtentTestListener implements ITestListener {

	private final ExtentReports extent = ExtentManager.getExtent();
	private ExtentTest test; // one reference per method execution

	/* ---------- TestNG hooks ---------- */

	// This method is executed at the start of all tests
	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
	}

	// This method is executed when the test is successful
	@Override
	public void onTestSuccess(ITestResult result) {
		test.pass("Test passed");
	}

	// This method is executed when the test fails
	@Override
	public void onTestFailure(ITestResult result) {
		test.fail(result.getThrowable());

		try {
			String path = takeScreenshot(result.getMethod().getMethodName());
			test.addScreenCaptureFromPath(path);
		} catch (Exception e) {
			test.warning("Could not attach screenshot: " + e.getMessage());
		}
	}

	// This method is executed when a test is skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		test.skip("Test skipped");
	}

	// This method is executed at the end of a test
	@Override
	public void onFinish(ITestContext context) {
		extent.flush(); // writes the final HTML
	}

	/* ---------- utilities ---------- */

	// This method takes a screenshot and saves it in the reports directory
	private String takeScreenshot(String method) throws Exception {
		File src = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);

		Path dest = Path.of("reports", "screenshots", method + "_" + System.currentTimeMillis() + ".png");
		Files.createDirectories(dest.getParent());
		Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
		return dest.toString();
	}
}