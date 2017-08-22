package functionallibraries;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_AlertsSearchResults;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

import supportlibraries.*;

public class AlertsSearchResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public AlertsSearchResults(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("alert") && !url.contains("results"))
				commonLibrary.sleep(5000);

		} while (!url.contains("alert") && !url.contains("results") && counter < 40);

		if (!driver.getCurrentUrl().contains("alert") && !driver.getCurrentUrl().contains("results")) {
			throw new IllegalStateException("alert search results page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults selectDocumentBlueDotted() {

		Boolean isExist = false;
		try {

			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);

			for (WebElement item : LList) {
				WebElement lnkTitle = commonLibrary.isExistNegative(item, By.tagName("a"), 10);
				WebElement blueDotted = commonLibrary.isExistNegative(item, UIMAP_AlertsSearchResults.blueDotted, 10);
				if (blueDotted != null) {
					isExist = true;

					WebElement lstchkBox = commonLibrary.isExist(item, By.cssSelector("input[type='checkbox']"), 10);

					if (lstchkBox != null)
						commonLibrary.setCheckBox(lstchkBox, lnkTitle.getText());
					this.putData("Alerts", "TC43824_ALERT_More_Dropdown_Options", "DocTitle", lnkTitle.getText());

					break;

				}

			}
			if (!isExist)

				report.updateTestLog("Check checkbox for Alert", "Not Checked checkbox for Alert", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults clickMoreAndSelectMoreOptions(String optionName) {
		try {
			boolean isExist = false;
			generalFunctions.clickAlertMoreOption();

			WebElement overflowFrom = commonLibrary.isExist(UIMAP_AlertsSearchResults.overflowFrom);
			if (overflowFrom == null)
				generalFunctions.clickAlertMoreOption();

			List<WebElement> li = commonLibrary.isExistList(overflowFrom, By.tagName("li"), 20);
			for (WebElement item : li) {
				WebElement button = commonLibrary.isExist(item, By.tagName("button"), 20);

				if (button.getText().equalsIgnoreCase(optionName)) {
					isExist = true;
					commonLibrary.clickButtonParentWithWait(button, optionName);

					commonLibrary.sleep(5000);

					if (optionName.equalsIgnoreCase("Mark all as read")) {
						WebElement markRead = commonLibrary.isExist(By.cssSelector("input[value='Mark as read']"), 10);
						commonLibrary.clickJS(markRead, "Mark as read");

					} else if (optionName.equalsIgnoreCase("Mark all as unread")) {
						WebElement markRead = commonLibrary.isExist(By.cssSelector("input[value='Mark As Unread']"), 10);
						commonLibrary.clickJS(markRead, "Mark As Unread");
					} else if (optionName.equalsIgnoreCase("Delete all")) {
						try {
							WebElement deleteAll = commonLibrary.isExist(By.cssSelector("input[value='Delete All']"), 10);
							commonLibrary.clickJS(deleteAll, "Delete all");
						} catch (TimeoutException ex) {

						}
					}

					pageCheck.documentState(driver);
					commonLibrary.sleep(30000);
					pageCheck.ajaxWait(driver);
					break;

				}
			}

			if (!isExist)
				report.updateTestLog("Click on " + optionName, "Not clicked on " + optionName, Status.FAIL);
		} catch (Exception e) {
			clickMoreAndSelectMoreOptions(optionName);
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults verifyPrintPage() {
		// Verify that the printable page is displayed for the results page in a
		// secondary browser
		if (driver.getWindowHandles().size() == 2)
			report.updateTestLog("Verify that the printable page is displayed for the results page in a secondary browser", "Printable page is displayed for the results page in a secondary browser", Status.PASS);
		else
			report.updateTestLog("Verify that the printable page is displayed for the results page in a secondary browser", "Printable page is not displayed for the results page in a secondary browser", Status.FAIL);
		commonLibrary.switchToWindow("print");
		// Close the secondary browser
		driver.close();
		commonLibrary.smallWait();

		commonLibrary.switchToWindow("alertsearchresults");
		report.updateTestLog("Close the secondary browser", "Closed the secondary browser", Status.PASS);
		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults verifyDocBoldUnbold(int intDocNo, Boolean isBold) {

		try {

			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);

			// WebElement
			// lnkTitle=commonLibrary.isExist(LList.get(intDocNo-1),By.cssSelector("a[data-action=searchdocument]"),
			// 10);
			WebElement blueDotted = commonLibrary.isExistNegative(LList.get(intDocNo - 1), UIMAP_AlertsSearchResults.blueDotted, 10);
			// Verify that the selected document is read and becomes unbold
			if (isBold) {

				if (blueDotted != null)
					report.updateTestLog("Verify that the selected Alert is unread and becomes bold", "selected Alert is unread and becomes bold", Status.PASS);
				else
					report.updateTestLog("Verify that the selected Alert is unread and becomes bold", "selected Alert is not unread and becomes bold", Status.FAIL);
			} else {
				if (blueDotted == null)
					report.updateTestLog("Verify that the selected Alert is read and becomes unbold", "selected Alert is read and becomes unbold", Status.PASS);
				else
					report.updateTestLog("Verify that the selected Alert is read and becomes unbold", "selected Alert is read and becomes unbold", Status.FAIL);

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether the are Results
	// Deleted
	// # Function Name : verifyResultsDeleted     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Alerts verifyResultsDeleted(String strTCName) {

		int iCount = 0;
		List<WebElement> OList = null;

		commonLibrary.sleep(5000);
		try {
			driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
			driver.navigate().refresh();

		} catch (TimeoutException e) {

		}
		driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
		commonLibrary.sleep(5000);

		do {
			iCount++;
			try {
				driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
				driver.navigate().refresh();

			} catch (TimeoutException e) {

			}
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
			commonLibrary.sleep(65000);
			OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		} while (OList.size() > 0 && iCount <= 50);
		// Verify that all the results for that content type is deleted
		if (OList.size() == 0)

			report.updateTestLog("Verify that all the results for that content type is deleted", "all the results for that content type is deleted", Status.PASS);
		else {

			generalFunctions.takeScreenShot(strTCName, "Verify that all the results for that content type is deleted");
		}

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify all the documents become read
	// /unread
	// # Function Name : VerifyAllItemsBold    
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults verifyAllItemsBoldUnbold(Boolean isBold) {
		Boolean isTrue = true;

		try {

			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (WebElement item : LList) {
				// WebElement
				// lnkTitle=commonLibrary.isExist(item,By.tagName("a"), 10);

				WebElement blueDotted = commonLibrary.isExistNegative(item, UIMAP_AlertsSearchResults.blueDotted, 10);

				// Verify that the selected document is read and becomes unbold
				if (isBold) {
					if (blueDotted == null) {
						isTrue = false;
						break;
					}
				} else {
					if (blueDotted != null) {
						isTrue = false;
						break;
					}
				}
			}
			if (isBold) {
				if (isTrue)

					report.updateTestLog("Verify that the selected Alert is unread and becomes bold", "selected Alert is unread and becomes bold", Status.PASS);
				else

					report.updateTestLog("Verify that the selected Alert is unread and becomes bold", "selected Alert is not unread and becomes bold", Status.FAIL);
			} else {
				if (isTrue)
					report.updateTestLog("Verify that the selected Alert is read and becomes unbold", "selected Alert is read and becomes unbold", Status.PASS);
				else
					report.updateTestLog("Verify that the selected Alert is read and becomes unbold", "selected Alert is not read and becomes unbold", Status.FAIL);

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigation From More Button To
	// Alerts
	// # Function Name : navigationFromMoreButtonToAlerts    
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Alerts navigationFromMoreButtonToAlerts() {

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		if (btnMore != null)
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");

		WebElement alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		if (alert == null || !alert.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
			if (btnMore != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		}

		if (alert != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(alert, "Alerts");
			else
				commonLibrary.clickLinkWithWebElementWithWait(alert, "Alerts");

		else

			report.updateTestLog("Click on alerts", "Alerts button is not clicked", Status.FAIL);

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults selectDocument(int intDocNo) {

		// String strDocTitle=null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		// List<WebElement>
		// LList=commonLibrary.isExist_List(OList.get(0),By.tagName("li"), 10);
		// WebElement
		// lnkTitle=commonLibrary.isExist(LList.get(intDocNo-1),By.tagName("a"),
		// 10);
		// strDocTitle=lnkTitle.getText();
		WebElement lstchkBox = commonLibrary.isExist(OList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
		String strI = "" + (intDocNo);
		if (lstchkBox != null)
			commonLibrary.setCheckBox(lstchkBox, strI);
		else
			report.updateTestLog("Select Alert", "Alert is not selected", Status.FAIL);

		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults verifyDocBoldUnbold(Boolean isBold) {
		String docTitle = dataTable.getData("General_Data", "DocTitle");

		try {

			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (WebElement item : LList) {
				WebElement lnkTitle = commonLibrary.isExist(item, By.tagName("a"), 10);
				if (lnkTitle.getText().equalsIgnoreCase(docTitle)) {
					WebElement blueDotted = commonLibrary.isExistNegative(item, UIMAP_AlertsSearchResults.blueDotted, 10);
					// Verify that the selected document is read and becomes
					// unbold
					if (isBold) {

						if (blueDotted != null)
							report.updateTestLog("Verify that the selected document is unread and becomes bold", "selected document is unread and becomes bold", Status.PASS);
						else
							report.updateTestLog("Verify that the selected document is unread and becomes bold", "selected document is not unread and becomes bold", Status.FAIL);
						break;
					} else {
						if (blueDotted == null)
							report.updateTestLog("Verify that the selected document is read and becomes unbold", "selected document is read and becomes unbold", Status.PASS);
						else
							report.updateTestLog("Verify that the selected document is read and becomes unbold", "selected document is read and becomes unbold", Status.FAIL);
						break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Put data in datasheet
	// # Function Name : putData    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void putData(String sheetName, String tcName, String colName, String varName) {
		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, sheetName);

		excel.setDatasheetName("General_Data");
		// int iRowNo=excel.getColumnNum("TC_ID", 0);
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, varName);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults swithToCondentTypeBlueDotted() {
		Boolean blnFlag = false;
		String strCondentType = null;
		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat1, 10);
			if ((btnFewerCat != null) && (btnFewerCat.getText().contains("Show more")))
				if (browsername.contains("internet"))
					commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, "More Categories");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, "More Categories");
			WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
			if (ulCondentSwitcher != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
				if (OListItems.size() > 0) {
					for (WebElement element : OListItems) {
						WebElement btnCondentType = commonLibrary.isExist(element, By.cssSelector("button[class='icon la-NewUpdates']"), 20);
						if (btnCondentType != null) {
							strCondentType = btnCondentType.getText();
							if (browsername.contains("internet"))
								commonLibrary.clickLinkWithWebElementWithWaitJS(btnCondentType, btnCondentType.getText());
							else
								commonLibrary.clickLinkWithWebElementWithWait(btnCondentType, btnCondentType.getText());
							commonLibrary.sleep(5000);
							blnFlag = true;
							break;

						}
					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Click on " + strCondentType, "Not Click on " + strCondentType, Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public History navigateToHistoryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 10);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			if (browsername.contains("internet")) {

				commonLibrary.clickLinkWithWebElementWithWait(btnIdHistoryMenuArrow, "History");

			} else {
				commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			}
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtViewAllHistory, "View All History");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory, "View All History");
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		pageCheck.positiveCheck(driver, "History", "History");
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delivery options
	// # Function Name : clickDeliverySelectOption     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public AlertsSearchResults clickDeliverySelectOption(String option, String deliverOption) {
		generalFunctions.clickDeliverySelectOption_New(option, deliverOption);
		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults selectDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='searchdocument']"), 10);
			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
				if (strDocTitle.trim().contains(DocName.trim())) {
					WebElement lstchkBox = commonLibrary.isExist(LList.get(i), By.cssSelector("input[type='checkbox']"), 10);
					String strI = "" + (i + 1);
					if (lstchkBox != null) {
						commonLibrary.setCheckBox(lstchkBox, strI);
						blnFlag = true;
						break;
					}
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Select document", DocName + " document is selected", Status.PASS);
		else
			report.updateTestLog("Select document", DocName + "document is not selected", Status.FAIL);

		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public AlertsSearchResults addToFolder(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 1000);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 1000);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

			/*
			 * try { String loadProp = properties.getProperty("xSpinner"); int
			 * count = 0; WebElement loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); do {
			 * commonLibrary.sleep(10000); loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); count++; }
			 * while (loader != null && count < 15); } catch (Exception e) {
			 * System.out.println(e.toString()); }
			 */

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 1000);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(30000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 1000);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(10000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
					count++;
				} while (loader != null && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 1000);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(10000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
					count++;
				} while (loader != null && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			commonLibrary.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To Folders
	// # Function Name : navigateToFolders
	// # Author : Seetha
	// # Date Created : 26-Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				commonLibrary.sleep(3000);
			}

			WebElement folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			if (folder == null) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null) {
					// commonLibrary.highlightElement(btnMore);
					if ((browsername.contains("internet")))
						commonLibrary.clickJS(btnMore, "More");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				}
				folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			}

			if (folder != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(folder, "Folders");
				else
					commonLibrary.clickLinkWithWebElementWithWait(folder, "Folders");
				commonLibrary.sleep(3000);
			}
		} catch (Exception e) {

		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Folder Icon
	// # Function Name : verifyFolderIconLink
	// # Author : Vennila
	// # Date Created :3 Nov'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults verifyFolderIconLink(String DocName) {
		// WebElement resultHeader =
		// commonLibrary.isExist(By.cssSelector("header[class='searchalertresults']"),
		// 10);

		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='searchdocument']"), 10);
			WebElement rowcontent = commonLibrary.isExist(LList.get(i), By.cssSelector("div[class*='search-row-content']"), 10);
			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
				if (strDocTitle.trim().contains(DocName.trim())) {
					WebElement wfIcon = commonLibrary.isExist(rowcontent, By.cssSelector("button[data-action='folderdata']"), 10);
					if (wfIcon != null)
						blnFlag = true;
					break;
				}
			}
		}

		if (blnFlag)
			report.updateTestLog("Verify workfolder icon.", "WF icon not displayed", Status.PASS);
		else
			report.updateTestLog("Verify workfolder icon", "WF icon displayed", Status.FAIL);
		return new AlertsSearchResults(scriptHelper);
	}
}
