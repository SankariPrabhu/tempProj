package com.cognizant.framework;


/**
 * Class to encapsulate the Excel report generation functions of the framework
 * @author Cognizant
 */
public class ExcelReport implements ReportType
{
	private ExcelDataAccess testLogAccess, resultSummaryAccess;
	
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private ExcelCellFormatting cellFormatting = new ExcelCellFormatting();
	
	private int currentSectionRowNum = 0;
	private int currentSubSectionRowNum = 0;
	
	
	/**
	 * Constructor to initialize the Excel report path and name
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public ExcelReport(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		
		testLogAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										reportSettings.getReportName());
		
		resultSummaryAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										"Summary");
	}
	
	
	/* TEST LOG FUNCTIONS */
	
	@Override
	public void initializeTestLog()
	{
		testLogAccess.createWorkbook();
		testLogAccess.addSheet("Cover_Page");
		testLogAccess.addSheet("Test_Log");
		
		initializeTestLogColorPalette();
		
		testLogAccess.setRowSumsBelow(false);
	}
	
	private void initializeTestLogColorPalette()
	{
		testLogAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		testLogAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		testLogAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
		testLogAccess.setCustomPaletteColor((short) 0x10, "#FF8000");	//Orange (Warning)
		testLogAccess.setCustomPaletteColor((short) 0x11, "#000000");	//Black (Done)
		testLogAccess.setCustomPaletteColor((short) 0x12, "#00FF80");	//Blue (Screenshot)
	}
	
	@Override
	public void addTestLogHeading(String heading)
	{
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = testLogAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, heading, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addTestLogSubHeading(String subHeading1, String subHeading2,
										String subHeading3, String subHeading4)
	{
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		testLogAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		testLogAccess.setValue(rowNum, 2, "", cellFormatting);
		testLogAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		testLogAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}
	
	@Override
	public void addTestLogTableHeadings()
	{
		testLogAccess.setDatasheetName("Test_Log");
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.addColumn("Step_No", cellFormatting);
		testLogAccess.addColumn("Step_Name", cellFormatting);
		testLogAccess.addColumn("Description", cellFormatting);
		testLogAccess.addColumn("Status", cellFormatting);
		testLogAccess.addColumn("Step_Time", cellFormatting);
	}
	
	@Override
	public void addTestLogSection(String section)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		currentSectionRowNum = rowNum + 1;
		currentSubSectionRowNum = 0;
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0xA);
		cellFormatting.setForeColorIndex((short) 0xB);
		
		testLogAccess.setValue(rowNum, 0, section, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addTestLogSubSection(String subSection)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);	
		}
		
		currentSubSectionRowNum = rowNum + 1;
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, " " + subSection, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void updateTestLog(String stepNumber, String stepName,
								String stepDescription, Status stepStatus,
								String screenShotName)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		
		boolean stepContainsScreenshot = processStatusColumn(stepStatus);
		
		cellFormatting.centred = true;
		cellFormatting.bold = true;
		int columnNum = testLogAccess.getColumnNum("Status", 0);
		testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
		
		cellFormatting.setForeColorIndex((short) 0xD);
		cellFormatting.bold = false;
		testLogAccess.setValue(rowNum, "Step_No", stepNumber, cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", Util.getCurrentFormattedTime(reportSettings.getDateFormatString()), cellFormatting);
		
		cellFormatting.centred = false;
		testLogAccess.setValue(rowNum, "Step_Name", stepName, cellFormatting);
		
		if (stepContainsScreenshot) {
			if (reportSettings.linkScreenshotsToTestLog) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
				
				testLogAccess.setValue(rowNum, "Description", stepDescription, cellFormatting);
			} else {
				testLogAccess.setValue(rowNum, "Description",
										stepDescription + " (Refer screenshot @ " + screenShotName + ")",
										cellFormatting);
			}
		} else {
			testLogAccess.setValue(rowNum, "Description", stepDescription, cellFormatting);
		}
	}
	
	private boolean processStatusColumn(Status stepStatus)
	{
		boolean stepContainsScreenshot = false;
		
		switch (stepStatus) {
		case PASS:
			cellFormatting.setForeColorIndex((short) 0xE);
			stepContainsScreenshot = reportSettings.takeScreenshotPassedStep;
			break;
			
		case FAIL:
			cellFormatting.setForeColorIndex((short) 0xF);
			stepContainsScreenshot = reportSettings.takeScreenshotFailedStep;
			break;
			
		case WARNING:
			cellFormatting.setForeColorIndex((short) 0x10);
			stepContainsScreenshot = reportSettings.takeScreenshotFailedStep;
			break;
			
		case DONE:
			cellFormatting.setForeColorIndex((short) 0x11);
			stepContainsScreenshot = false;
			break;
			
		case SCREENSHOT:
			cellFormatting.setForeColorIndex((short) 0x11);
			stepContainsScreenshot = true;
			break;
			
		case DEBUG:
			cellFormatting.setForeColorIndex((short) 0x12);
			stepContainsScreenshot = false;
			break;
		}
		
		return stepContainsScreenshot;
	}
	
	@Override
	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) the previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, "Execution Duration: " + executionTime, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
		
		rowNum = testLogAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		testLogAccess.setValue(rowNum, "Step_No", "Steps passed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Name", ": " + nStepsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		testLogAccess.setValue(rowNum, "Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		testLogAccess.setValue(rowNum, "Status", "Steps failed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", ": " + nStepsFailed, cellFormatting);
		
		wrapUpTestLog();
	}
	
	private void wrapUpTestLog()
	{
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
		
		testLogAccess.setDatasheetName("Cover_Page");
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
	}
	
	
	/* RESULT SUMMARY FUNCTIONS */
	
	@Override
	public void initializeResultSummary()
	{
		resultSummaryAccess.createWorkbook();
		resultSummaryAccess.addSheet("Cover_Page");
		resultSummaryAccess.addSheet("Result_Summary");
		
		initializeResultSummaryColorPalette();
	}
	
	private void initializeResultSummaryColorPalette()
	{
		resultSummaryAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		resultSummaryAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
	}
	
	@Override
	public void addResultSummaryHeading(String heading)
	{
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = resultSummaryAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, heading, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addResultSummarySubHeading(String subHeading1, String subHeading2,
											String subHeading3, String subHeading4)
	{
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		resultSummaryAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 2, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}
	
	@Override
	public void addResultSummaryTableHeadings()
	{
		resultSummaryAccess.setDatasheetName("Result_Summary");
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.addColumn("Test_Scenario", cellFormatting);
		resultSummaryAccess.addColumn("Test_Case", cellFormatting);
		resultSummaryAccess.addColumn("Test_Description", cellFormatting);
		resultSummaryAccess.addColumn("Execution_Time", cellFormatting);
		resultSummaryAccess.addColumn("Test_Status", cellFormatting);
	}
	
	@Override
	public void updateResultSummary(String scenarioName, String testcaseName,
										String testcaseDescription, String executionTime,
										String testStatus)
	{
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		cellFormatting.setForeColorIndex((short) 0xD);
		
		cellFormatting.centred = false;
		cellFormatting.bold = false;
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", scenarioName, cellFormatting);
		
		int columnNum = resultSummaryAccess.getColumnNum("Test_Case", 0);
		resultSummaryAccess.setValue(rowNum, columnNum, testcaseName, cellFormatting);
		if (reportSettings.linkTestLogsToSummary) {
			resultSummaryAccess.setHyperlink(rowNum, columnNum, scenarioName + "_" + testcaseName + ".xls");
		}
		
		resultSummaryAccess.setValue(rowNum, "Test_Description", testcaseDescription, cellFormatting);
		
		cellFormatting.centred = true;
		resultSummaryAccess.setValue(rowNum, "Execution_Time", executionTime, cellFormatting);
		
		cellFormatting.bold = true;
		if (testStatus.equalsIgnoreCase("Passed")) {
			cellFormatting.setForeColorIndex((short) 0xE);
		}
		if (testStatus.equalsIgnoreCase("Failed")) {
			cellFormatting.setForeColorIndex((short) 0xF);
		}
		resultSummaryAccess.setValue(rowNum, "Test_Status", testStatus, cellFormatting);
	}
	
	@Override
	public void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed)
	{	
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, "Total Duration: " +
													totalExecutionTime, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
		
		rowNum = resultSummaryAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", "Tests passed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Case", ": " + nTestsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		resultSummaryAccess.setValue(rowNum, "Test_Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		resultSummaryAccess.setValue(rowNum, "Execution_Time", "Tests failed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Status", ": " + nTestsFailed, cellFormatting);
		
		wrapUpResultSummary();
	}
	
	private void wrapUpResultSummary()
	{
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
		
		resultSummaryAccess.setDatasheetName("Cover_Page");
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
	}
}