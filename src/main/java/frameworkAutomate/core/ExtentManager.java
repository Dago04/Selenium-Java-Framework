package frameworkAutomate.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	
	// Initialize the ExtentReports instance
	private static final ExtentReports extent =  createInstance();
	
	private static ExtentReports createInstance() {
		
		
		String timeStamp = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		
		String reportPath = "reports/AutomationReport_" + timeStamp + ".html";
		//Especificar la carpeta donde se guardará el reporte
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
		
		spark.config().setDocumentTitle("Automation Test Results");
		spark.config().setReportName("Test execution " + timeStamp);
		
		ExtentReports ext = new ExtentReports();
		
		ext.attachReporter(spark);
		ext.setSystemInfo("Framework", "Selenium TestNG-POM");
		ext.setSystemInfo("Author", "Dagoberto Salas Cordero");
		
		return ext;
	}
	
	public static ExtentReports getExtent() {
		return extent;
	}
	
	
}
