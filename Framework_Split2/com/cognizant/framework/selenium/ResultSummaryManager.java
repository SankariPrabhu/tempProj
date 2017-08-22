package com.cognizant.framework.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;
import com.cognizant.framework.ReportThemeFactory;
import com.cognizant.framework.Settings;
import com.cognizant.framework.TimeStamp;
import com.cognizant.framework.Util;
import com.cognizant.framework.ReportThemeFactory.Theme;


/**
 * Class that manages the result summary creation during a batch execution
 * @author Cognizant
 */
public class ResultSummaryManager
{
	private static SeleniumReport summaryReport;
	
	private static ReportSettings reportSettings;
	private static String reportPath;
	
	private static Date overallStartTime, overallEndTime;
	// All the above variables have been marked as static
	// so that they will maintain their state across multiple threads
	
	private Properties properties;
	private FrameworkParameters frameworkParameters =
									FrameworkParameters.getInstance();
	
	
	/**
	 * Function to set the absolute path of the framework (to be used as a relative path) 
	 */
	public void setRelativePath()
	{
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if(relativePath.contains("supportlibraries")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}
	
	/**
	 * Function to initialize the test batch execution
	 * @param runConfiguration The run configuration to be executed
	 */
	public void initializeTestBatch(String runConfiguration)
	{
		overallStartTime = Util.getCurrentTime();
		
		properties = Settings.getInstance();
		
		frameworkParameters.setRunConfiguration(runConfiguration);
		
		//System.setProperty("ReportPath", "C:\\ACoE In-house tools\\TempResults");
	}
	
	/**
	 * Function to initialize the summary report
	 * @param nThreads The number of parallel threads configured for the test batch execution
	 */
	public void initializeSummaryReport(int nThreads)
	{
		initializeReportSettings();
		ReportTheme reportTheme =
				ReportThemeFactory.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));
		
		summaryReport = new SeleniumReport(reportSettings, reportTheme);
		
		summaryReport.initialize();
		summaryReport.initializeResultSummary();
		createResultSummaryHeader(nThreads);
	}
	
	private void initializeReportSettings()
	{
		if(System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}
		
		reportSettings = new ReportSettings(reportPath, "");
		
		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReports = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHtmlReports = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		reportSettings.linkTestLogsToSummary = true;
	}
	
	private void createResultSummaryHeader(int nThreads)
	{
		summaryReport.addResultSummaryHeading(reportSettings.getProjectName() +
											" - " +	" Automation Execution Result Summary");
		summaryReport.addResultSummarySubHeading("Date & Time",
								": " + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")),
								"OnError", ": " + properties.getProperty("OnError"));
		summaryReport.addResultSummarySubHeading("Run Configuration",
								": " + frameworkParameters.getRunConfiguration(),
								"No. of threads", ": " + nThreads);
		
		summaryReport.addResultSummaryTableHeadings();
	}
	
	/**
	 * Function to set up the error log file within the test report
	 */
	public void setupErrorLog()
	{
		String errorLogFile = reportPath + Util.getFileSeparator() + "ErrorLog.txt";
		try {
			System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
	}
	
	/**
	 * Function to update the results summary with the status of the executed test case
	 * @param scenarioName The name of the scenario/module under which the test case falls
	 * @param testcaseName The name of the test case
	 * @param testcaseDescription The description of the test case
	 * @param executionTime The time taken to execute the test case
	 * @param testStatus The Pass/Fail status of the test case
	 */
	public void updateResultSummary(String scenarioName,
									String testcaseName, String testcaseDescription,
									String executionTime, String testStatus)
	{
		summaryReport.updateResultSummary(scenarioName, testcaseName, testcaseDescription, executionTime, testStatus);
		
	}
	
	/**
	 * Function to do the required wrap-up activities after completing the test batch execution
	 * @param testExecutedInUnitTestFramework Boolean variable indicating whether the test is executed in JUnit/TestNG
	 */
	public void wrapUp(Boolean testExecutedInUnitTestFramework)
	{
		overallEndTime = Util.getCurrentTime();
		String totalExecutionTime =
				Util.getTimeDifference(overallStartTime, overallEndTime);
		summaryReport.addResultSummaryFooter(totalExecutionTime);
		
		if(testExecutedInUnitTestFramework) {
			File testNgResultSrc = new File(frameworkParameters.getRelativePath() +
											Util.getFileSeparator() +
											properties.getProperty("TestNgReportPath"));		
			File testNgResultDest = new File(reportPath +
											Util.getFileSeparator() +
											"TestNG Results");
			try {
				FileUtils.copyDirectory(testNgResultSrc, testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Function to launch the summary report at the end of the test batch execution
	 */
	public void launchResultSummary()
	{
		if (reportSettings.generateHtmlReports) {
			try {
				Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " +
												reportPath + "\\HTML Results\\Summary.Html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (reportSettings.generateExcelReports) {
			try {
				Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " +
												reportPath + "\\Excel Results\\Summary.xls");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		clearJVM();
	}
	
	 
	 public void clearJVM()
	 {
		Runtime rs = Runtime.getRuntime();
		System.out.println("Free memory in JVM before Garbage Collection = "+rs.freeMemory());
		rs.gc();
		System.out.println("Free memory in JVM after Garbage Collection = "+rs.freeMemory());
		System.exit(0);
	 }
}