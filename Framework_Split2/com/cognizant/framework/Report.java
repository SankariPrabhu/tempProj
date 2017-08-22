package com.cognizant.framework;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * Class to encapsulate all the reporting features of the framework
 * @author Cognizant
 */
public class Report
{
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	
	private int stepNumber;
	private int nStepsPassed = 0, nStepsFailed = 0;
	private int nTestsPassed = 0, nTestsFailed = 0;
	
	private List<ReportType> reportTypes = new ArrayList<ReportType>();
	
	public String testStatus = "Passed";
	public String configurationStaus="Passed";//Added by Uma
	/**
	 * Function to get the current status of the test being executed
	 * @return the current status of the test being executed
	 */
	public String getTestStatus()
	{
		return testStatus;
	}
	//Added by Uma
	public String getConfigurationStatus()
	{
		return configurationStaus;
	}
	private String failureDescription;
	/**
	 * Function to get the decription of any failure that may occur during the script execution
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription()
	{
		return failureDescription;
	}
	
	
	/**
	 * Constructor to initialize the Report
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public Report(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
	}
	
	/**
	 * Function to initialize the report
	 */
	public void initialize()
	{
		if(reportSettings.generateExcelReports) {
			new File(reportSettings.getReportPath() +
						Util.getFileSeparator() + "Excel Results").mkdir();
			
			ExcelReport excelReport = new ExcelReport(reportSettings, reportTheme);
			reportTypes.add(excelReport);
		}
		
		if(reportSettings.generateHtmlReports) {
			new File(reportSettings.getReportPath() +
						Util.getFileSeparator() + "HTML Results").mkdir();
			
			HtmlReport htmlReport = new HtmlReport(reportSettings, reportTheme);
			reportTypes.add(htmlReport);
		}
		
		new File(reportSettings.getReportPath() + Util.getFileSeparator() +
															"Screenshots").mkdir();
	}
	
	
	/* TEST LOG FUNCTIONS*/
	
	/**
	 * Function to initialize the test log
	 */
	public void initializeTestLog()
	{
		if(reportSettings.getReportName().equals("")) {
			throw new FrameworkException("The report name cannot be empty!");
		}
		
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeTestLog();
		}
	}
	
	/**
	 * Function to add a heading to the test log
	 * @param heading The heading to be added
	 */
	public void addTestLogHeading(String heading)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogHeading(heading);
		}
	}
	
	/**
	 * Function to add sub-headings to the test log
	 * (4 sub-headings present per test log row)
	 * @param subHeading1 The first sub-heading to be added
	 * @param subHeading2 The second sub-heading to be added
	 * @param subHeading3 The third sub-heading to be added
	 * @param subHeading4 The fourth sub-heading to be added
	 */
	public void addTestLogSubHeading(String subHeading1, String subHeading2,
										String subHeading3, String subHeading4)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubHeading(subHeading1, subHeading2,
														subHeading3, subHeading4);
		}
	}
	
	/**
	 * Function to add the overall table headings to the test log
	 * (should be called first before adding the actual content into the test log;
	 * headings and sub-heading should be added before this)
	 */
	public void addTestLogTableHeadings()
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogTableHeadings();
		}
	}
	
	/**
	 * Function to add a section to the test log
	 * @param section The section to be added
	 */
	public void addTestLogSection(String section)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSection(section);
		}
		
		stepNumber = 1;
	}
	
	/**
	 * Function to add a sub-section to the test log
	 * (should be called only within a previously created section)
	 * @param subSection The sub-section to be added
	 */
	public void addTestLogSubSection(String subSection)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubSection(subSection);
		}
	}
	
	/**
	 * Function to update the test log with the details of a particular test step
	 * @param stepName The test step name
	 * @param stepDescription The description of what the test step does
	 * @param stepStatus The status of the test step
	 */
	public void updateTestLog(String stepName, String stepDescription, Status stepStatus)
	{
		
		if(stepStatus.equals(Status.FAIL)) {
			testStatus = "Failed";
			configurationStaus="Failed";//Added by Uma
			if(failureDescription == null) {
				failureDescription = stepDescription;
			} else {
				failureDescription = failureDescription + "; " + stepDescription;
			}
			
			nStepsFailed++;
		}
		
		if(stepStatus.equals(Status.PASS)) {
			nStepsPassed++;
		}
		
		if(stepStatus.ordinal() <= reportSettings.getLogLevel())
		{	
			String screenshotName = null;
			
			if(stepStatus.equals(Status.FAIL)) {
				if(reportSettings.takeScreenshotFailedStep) {
					screenshotName = reportSettings.getReportName() + "_" +
										Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
					takeScreenshot(reportSettings.getReportPath() +
									Util.getFileSeparator() + "Screenshots" +
									Util.getFileSeparator() + screenshotName);
				}
			}
			
			if(stepStatus.equals(Status.PASS)) {
				if(reportSettings.takeScreenshotPassedStep) {
					screenshotName = reportSettings.getReportName() + "_" +
										Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
					takeScreenshot(reportSettings.getReportPath() +
									Util.getFileSeparator() + "Screenshots" +
									Util.getFileSeparator() + screenshotName);
				}
			}
			
			if(stepStatus.equals(Status.SCREENSHOT))
			{
				screenshotName = reportSettings.getReportName() + "_" +
									Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
				takeScreenshot(reportSettings.getReportPath() +
								Util.getFileSeparator() + "Screenshots" +
								Util.getFileSeparator() + screenshotName);
			}
			
			for(int i=0; i < reportTypes.size();i++) {
				reportTypes.get(i).updateTestLog(Integer.toString(stepNumber), stepName, stepDescription, stepStatus, screenshotName);
			}
			
			stepNumber++;
		}
		
		
	}
	
	/**
	 * Function to take a screenshot
	 * @param screenshotPath The path where the screenshot should be saved
	 */
	protected void takeScreenshot(String screenshotPath)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
		Robot robot;
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating Robot object (for taking screenshot)");
		}
		
		BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		File screenshotFile = new File (screenshotPath);
		
		try {
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to .jpg file");
		}
	}
	
	/**
	 * Function to add a footer to the test log
	 * (The footer format is pre-defined - it contains the execution time and the number of passed/failed steps)
	 * @param executionTime The time taken to execute the test case
	 */
	public void addTestLogFooter(String executionTime)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogFooter(executionTime, nStepsPassed, nStepsFailed);
		}
	}
	
	/**
	 * Function to consolidate all screenshots into a Word document
	 */
	public void consolidateScreenshotsInWordDoc()
	{
		String screenshotsConsolidatedFolderPath = reportSettings.getReportPath() +
														Util.getFileSeparator() +
														"Screenshots (Consolidated)";
		new File(screenshotsConsolidatedFolderPath).mkdir(); 
		
		WordDocumentManager documentManager =
				new WordDocumentManager(screenshotsConsolidatedFolderPath,
											reportSettings.getReportName());
		
		String screenshotsFolderPath = reportSettings.getReportPath() +
											Util.getFileSeparator() +
											"Screenshots";
		File screenshotsFolder = new File(screenshotsFolderPath);
		
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				if (fileName.contains(reportSettings.getReportName())) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		File[] screenshots = screenshotsFolder.listFiles(filenameFilter);
		if (screenshots != null && screenshots.length > 0) {
			documentManager.createDocument();
			
			for(File screenshot: screenshots) {
				documentManager.addPicture(screenshot);
			}
		}
	}
	
	
	/* RESULT SUMMARY FUNCTIONS */
	
	/**
	 * Function to initialize the result summary
	 */
	public void initializeResultSummary()
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeResultSummary();
		}
	}
	
	/**
	 * Function to add a heading to the result summary
	 * @param heading The heading to be added
	 */
	public void addResultSummaryHeading(String heading)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryHeading(heading);
		}
	}
	
	/**
	 * Function to add sub-headings to the result summary
	 * (4 sub-headings present per result summary row)
	 * @param subHeading1 The first sub-heading to be added
	 * @param subHeading2 The second sub-heading to be added
	 * @param subHeading3 The third sub-heading to be added
	 * @param subHeading4 The fourth sub-heading to be added
	 */
	public void addResultSummarySubHeading(String subHeading1, String subHeading2,
											String subHeading3, String subHeading4)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummarySubHeading(subHeading1, subHeading2,
															subHeading3, subHeading4);
		}
	}
	
	/**
	 * Function to add the overall table headings to the result summary
	 */
	public void addResultSummaryTableHeadings()
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryTableHeadings();
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
	public synchronized void updateResultSummary(String scenarioName, String testcaseName,
										String testcaseDescription, String executionTime,
										String testStatus)
	{
		if(testStatus.equalsIgnoreCase("failed"))
		{
			nTestsFailed++;
		}
		else if(testStatus.equalsIgnoreCase("passed"))
		{
			nTestsPassed++;
		}
		
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).updateResultSummary(scenarioName, testcaseName,
																testcaseDescription,
																executionTime, testStatus);
		}
	}
	
	/**
	 * Function to add a footer to the result summary
	 * (The footer format is pre-defined - it contains the total execution time and the number of passed/failed tests)
	 * @param totalExecutionTime The total time taken to execute all the test cases
	 */
	public void addResultSummaryFooter(String totalExecutionTime)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryFooter(totalExecutionTime, nTestsPassed, nTestsFailed);
		}
	}
}