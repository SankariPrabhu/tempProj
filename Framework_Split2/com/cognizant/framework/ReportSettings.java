package com.cognizant.framework;

import java.io.File;
import java.text.SimpleDateFormat;


/**
 * Class to encapsulate the reporting settings in the framework
 * @author Cognizant
 */
public class ReportSettings
{
	private final String reportPath;
	/**
	 * Function to get the absolute path where the report is to be stored
	 * @return The report path
	 */
	public String getReportPath()
	{
		return reportPath;
	}
	
	private final String reportName;
	/**
	 * Function to get the name of the report
	 * @return The report name
	 */
	public String getReportName()
	{
		return reportName;
	}
	
	private String projectName = "";
	/**
	 * Function to get the name of the project being automated
	 * @return The project name
	 */
	public String getProjectName()
	{
		return projectName;
	}
	/**
	 * Function to set the name of the project being automated
	 * @param projectName The project name
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	
	private int logLevel = 4;
	/**
	 * Function to get the logging level of the reports.
	 * Log levels range between 0 to 5,
	 * with 0 being minimal reporting and 5 being highly detailed reporting
	 * @return The log level
	 */
	public int getLogLevel()
	{
		return logLevel;
	}
	/**
	 * Function to set the logging level of the reports.
	 * Log levels range between 0 to 5,
	 * with 0 being minimal reporting and 5 being highly detailed reporting
	 * @param logLevel The log level
	 */
	public void setLogLevel(int logLevel)
	{
		if (logLevel < 0)
			logLevel = 0;
		
		if (logLevel > 5)
			logLevel = 5;
		
		this.logLevel = logLevel;
	}
	
	/**
	 * Boolean variable indicating whether Excel reports should be generated
	 * @see #generateHtmlReports
	 */
	public boolean generateExcelReports = true;
	/**
	 * Boolean variable indicating whether HTML reports should be generated
	 * @see #generateExcelReports
	 */
	public boolean generateHtmlReports = true;
	
	/**
	 * Boolean variable indicating whether a screenshot should be captured for failed/warning steps
	 * @see #takeScreenshotPassedStep
	 */
	public boolean takeScreenshotFailedStep = true;
	/**
	 * Boolean variable indicating whether a screenshot should be captured for passed steps
	 * @see #takeScreenshotFailedStep
	 */
	public boolean takeScreenshotPassedStep = false;
	
	/**
	 * Boolean variable indicating whether any screenshots taken must be linked to the corresponding steps within the test logs
	 * @see #linkTestLogsToSummary
	 */
	public boolean linkScreenshotsToTestLog = true;
	/**
	 * Boolean variable indicating whether the individual test logs must be linked to the result summary
	 */
	public boolean linkTestLogsToSummary = true;
	
	/**
	 * Boolean variable indicating whether all the screenshots must be consolidated into a Word document
	 */
	public boolean consolidateScreenshotsInWordDoc = false;
	
	private String dateFormatString = "dd-MMM-yyyy hh:mm:ss a";
	/**
	 * Function to get a string indicating the format for the date/time to be used within the report
	 * @return The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public String getDateFormatString()
	{
		return dateFormatString;
	}
	/**
	 * Function to set a string indicating the format for the date/time to be used within the report
	 * @param dateFormatString The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public void setDateFormatString(String dateFormatString)
	{
		this.dateFormatString = dateFormatString;
	}
	
	
	/**
	 * Constructor to initialize the report settings
	 * @param reportPath The report path
	 * @param reportName The report name
	 */
	public ReportSettings(String reportPath, String reportName)
	{
		boolean reportPathExists = new File(reportPath).isDirectory();
		if(!reportPathExists) {
			throw new FrameworkException("The given report path does not exist!");
		}
		this.reportPath = reportPath;
		
		this.reportName = reportName;
	}
}