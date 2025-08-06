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
 * Listener TestNG que genera un reporte ExtentReports con capturas automáticas
 * en caso de fallo.
 */

public class ExtentTestListener implements ITestListener {

	private final ExtentReports extent = ExtentManager.getExtent();
	private ExtentTest test; // una referencia por ejecución de método

	/* ---------- TestNG hooks ---------- */

	// Este método se ejecuta al iniciar todos los tests
	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
	}

	// Este método se ejecuta al ser la prueba exitosa
	@Override
	public void onTestSuccess(ITestResult result) {
		test.pass("Test passed");
	}

	// Este método se ejecuta al fallar el test
	@Override
	public void onTestFailure(ITestResult result) {
		test.fail(result.getThrowable());

		try {
			String path = takeScreenshot(result.getMethod().getMethodName());
			test.addScreenCaptureFromPath(path);
		} catch (Exception e) {
			test.warning("No se pudo adjuntar captura: " + e.getMessage());
		}
	}

	// Este método se ejecuta al skippear un test
	@Override
	public void onTestSkipped(ITestResult result) {
		test.skip("Prueba saltada");
	}
	
	// Este método se ejecuta al finalizar un test
	@Override
	public void onFinish(ITestContext context) {
		extent.flush(); // escribe el HTML final
	}

	/* ---------- utilidades ---------- */

	private String takeScreenshot(String method) throws Exception {
		File src = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);

		Path dest = Path.of("reports", "screenshots", method + "_" + System.currentTimeMillis() + ".png");
		Files.createDirectories(dest.getParent());
		Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
		return dest.toString();
	}
}
