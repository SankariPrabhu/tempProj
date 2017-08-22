package functionallibraries;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_AlertsSearchResults;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.ExcelDataAccess;
import supportlibraries.*;

public class Alerts extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public Alerts(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("alert"))
				commonLibrary.sleep(5000);

		} while (!url.contains("alert") && counter < 40);
		if (!driver.getCurrentUrl().contains("alert")) {
			throw new IllegalStateException("alert page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Landing page panels
	// # Function Name : Verify_LandingPage_Panels     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts deleteAlert(String alert) {
		boolean isalert = false;
		WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
		List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
		for (WebElement item : li) {
			List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
			for (WebElement item1 : a) {
				if (item1.getAttribute("data-action").contains("searchalertresults")) {
					if (item1.getText().equalsIgnoreCase(alert)) {
						WebElement chk1 = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 10);
						commonLibrary.setCheckBox(chk1, "Checkbox of " + alert + "");
						isalert = true;
						break;
					}
				}
			}
			if (isalert)
				break;
		}
		if (isalert) {
			WebElement btnDelete = commonLibrary.isExist(UIMAP_Home.btnAlertDelete, 10);
			if (btnDelete != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDelete, "Delete Alert Icon");
				else
					commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Alert Icon");
				report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
			}
			WebElement btnDeleteAlert = commonLibrary.isExist(UIMAP_Home.btnDeleteAlert, 10);
			if (btnDeleteAlert != null) {
				if (browsername.contains("internet")) {
					// commonLibrary.highlightElement(btnDeleteAlert);
					commonLibrary.clickJS(btnDeleteAlert, "Delete Alert");
				} else
					commonLibrary.clickButtonParentWithWait(btnDeleteAlert, "Delete Alert");
			}
		} else {
			report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout of the application
	// # Function Name : logout     
	// # Author : Shobana
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take Screenshot
	// # Function Name : takeScreenShot     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			String strScreenshot = strTCName + commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot + ".jpg";

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep, "Perform Manual Verification for this step. screenshot is saved in " + strDestination + "", Status.WARNING);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to right click on edit of alert in
	// alerts page
	// # Function Name : clickRightOnEdit_CheckNoNewWindow
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Alerts clickRightOnEdit_CheckNoNewWindow(String alert) {

		try {
			boolean edit = false;
			WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
			List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
			for (WebElement item : li) {
				List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
				for (WebElement item1 : a) {
					if (item1.getAttribute("data-action").contains("shepardsalertresults")) {
						if (item1.getText().equalsIgnoreCase(alert)) {
							WebElement element = commonLibrary.isExist(item, UIMAP_SearchResult.editAlert, 10);
							if (element != null) {
								// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
								Actions oAction = new Actions(driver);
								oAction.moveToElement(element);
								// commonLibrary.highlightElement(element);
								commonLibrary.sleep(2000);

								String browsername = cap.getBrowserName();
								if (browsername.equalsIgnoreCase("firefox")) {
									oAction.contextClick(element).sendKeys("W").build().perform();
									edit = true;
									break;
								} else if (browsername.contains("internet")) {
									oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();
									edit = true;
									break;

								} else if (browsername.contains("chrome")) {

									oAction.keyDown(Keys.SHIFT).click(element).keyUp(Keys.SHIFT).perform();
									edit = true;
									break;
								}
							} else {
								report.updateTestLog("Right Click on the update button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the update button in alert " + alert + " and  right click pop up does not appear", Status.FAIL);
							}
						}
					}
				}
				if (edit)
					break;
			}
			commonLibrary.sleep(2000);
			if (edit && driver.getWindowHandles().size() <= 1)

				report.updateTestLog("Right Click on the edit button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the edit button in alert " + alert + " and  right click pop up appears where open in new window link is not available", Status.PASS);
			else

				report.updateTestLog("Right Click on the edit button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the edit button in alert " + alert + " and  right click pop up does not appear", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to right click on update option in
	// alerts page
	// # Function Name : clickRightOnUpdate_CheckNoNewWindow
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Alerts clickRightOnUpdate_CheckNoNewWindow(String alert) {
		try {
			boolean edit = false;
			WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
			List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
			for (WebElement item : li) {
				List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
				for (WebElement item1 : a) {
					if (item1.getAttribute("data-action").contains("shepardsalertresults")) {
						if (item1.getText().equalsIgnoreCase(alert)) {
							WebElement element = commonLibrary.isExist(item, UIMAP_SearchResult.updateAlert, 10);
							if (element != null) {
								// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
								Actions oAction = new Actions(driver);
								oAction.moveToElement(element);
								// commonLibrary.highlightElement(element);
								commonLibrary.sleep(2000);

								String browsername = cap.getBrowserName();
								if (browsername.equalsIgnoreCase("firefox")) {
									oAction.contextClick(element).sendKeys("W").build().perform();
									edit = true;
									break;
								} else if (browsername.contains("internet")) {
									oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();
									edit = true;
									break;
								} else if (browsername.contains("chrome")) {

									oAction.keyDown(Keys.SHIFT).click(element).keyUp(Keys.SHIFT).perform();
									edit = true;
									break;
								}
							} else {
								report.updateTestLog("Right Click on the update button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the update button in alert " + alert + " and  right click pop up does not appear", Status.FAIL);
							}
						}
					}
				}
				if (edit)
					break;
			}
			commonLibrary.sleep(2000);
			if (edit && driver.getWindowHandles().size() <= 1)

				report.updateTestLog("Right Click on the update button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the update button in alert " + alert + " and  right click pop up appears where open in new window link is not available", Status.PASS);
			else

				report.updateTestLog("Right Click on the update button in alert " + alert + " and verify right click pop up appears True and Open in new tab link does not appear in the pop up", "Right Click on the update button in alert " + alert + " and  right click pop up does not appear", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete alert
	// # Function Name : deleteShepAlert
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Alerts deleteShepAlert(String alert) {
		boolean isalert = false;
		WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
		List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
		for (WebElement item : li) {
			List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
			for (WebElement item1 : a) {
				if (item1.getAttribute("data-action").contains("shepardsalertresults")) {
					if (item1.getText().equalsIgnoreCase(alert)) {
						WebElement chk1 = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 10);
						commonLibrary.setCheckBox(chk1, "Checkbox of " + alert + "");
						isalert = true;
						break;
					}
				}
			}
			if (isalert)
				break;
		}
		if (isalert) {
			WebElement btnDelete = commonLibrary.isExist(UIMAP_Home.btnAlertDelete, 10);
			if (btnDelete != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDelete, "Delete Alert Icon");
				else
					commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Alert Icon");
				report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
			}
			WebElement btnDeleteAlert = commonLibrary.isExist(UIMAP_Home.btnDeleteAlert, 10);
			if (btnDeleteAlert != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDeleteAlert, "Delete Alert");
				else
					commonLibrary.clickButtonParentWithWait(btnDeleteAlert, "Delete Alert");
			}
		} else {
			report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults clickDocLinkBlueDotted() {
		Boolean blnFlag = false;
		int count = 0;
		String strDocTitle = null;
		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		WebElement btnNextPage = null;

		do {
			count = count + 1;
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						WebElement blueDotted = commonLibrary.isExistNegative(document, UIMAP_SearchResult.blueDotted, 2);
						if (blueDotted != null) {

							strDocTitle = eleDocTitle.getText();
							report.updateTestLog("Document Title ", "Document Title is " + strDocTitle, Status.DONE);

							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								btnNextPage = null;
								break;
							}
						}

					}

				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.nextPage);
				if (btnNextPage != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
			}

		} while (btnNextPage != null && count <= 20);

		if (!blnFlag) {
			report.updateTestLog("Click on the alert having blue dotted " + strDocTitle, "Not Clicked on the alert having blue dotted ", Status.FAIL);
			throw new FrameworkException("There is no Alert with results update");
		} else {

			// String name = "Test 1";
			List<WebElement> viewusing = commonLibrary.isExistList(UIMAP_Document.viewusing, 20);
			// for (int i = 0; i < viewusing.size(); i++) {
			// if (viewusing.get(i).getAttribute("value").contains(name)) {
			// if (browsername.contains("internet")) {
			if (viewusing.size() > 0)
				commonLibrary.clickJS(viewusing.get(0), "View Using" + viewusing.get(0).getAttribute("value"));
			// break;
			// } else {
			// commonLibrary.clickButton_Parent_WithWait(viewusing.get(i),
			// "View Using" + name);
			// break;
			// }
			// }
			// }
			WebElement pgHeader = null;

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 10);

			if (pgHeader != null && pgHeader.getText().contains(strDocTitle))
				report.updateTestLog("Verify Alert " + strDocTitle + " Result is displayed", "Alert " + strDocTitle + " Result is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Alert " + strDocTitle + " Result is displayed", "Alert " + strDocTitle + " Result is not displayed", Status.FAIL);
		}

		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts clickMoreAndSelectMoreOptions(String optionName) {
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
					if (browsername.contains("internet"))

						commonLibrary.clickJS(button, "optionName");
					else
						commonLibrary.clickButtonParentWithWait(button, optionName);

					commonLibrary.sleep(5000);

					if (optionName.equalsIgnoreCase("Mark results as read")) {
						WebElement markRead = commonLibrary.isExist(By.cssSelector("input[value='OK']"), 10);
						commonLibrary.clickJS(markRead, "OK");

						commonLibrary.sleep(50000);

					} else if (optionName.equalsIgnoreCase("Delete alert results")) {
						try {

							WebElement markRead = commonLibrary.isExist(By.cssSelector("input[value='Delete Results']"), 10);
							commonLibrary.clickJS(markRead, "Delete Results");
						} catch (TimeoutException ex) {

						}
					}
					commonLibrary.sleep(30000);
					pageCheck.documentState(driver);
					pageCheck.ajaxWait(driver);
					break;

				}
			}

			if (!isExist)
				report.updateTestLog("Click on " + optionName, "Not clicked on " + optionName, Status.FAIL);
		} catch (Exception e) {
			clickMoreAndSelectMoreOptions(optionName);
		}
		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts verifyPrintPage() {
		// Verify that the printable page is displayed for the results page in a
		// secondary browser
		if (driver.getWindowHandles().size() == 2)
			report.updateTestLog("Verify that the printable page is displayed for the results page in a secondary browser", "Printable page is displayed for the results page in a secondary browser", Status.PASS);
		else
			report.updateTestLog("Verify that the printable page is displayed for the results page in a secondary browser", "Printable page is not displayed for the results page in a secondary browser", Status.FAIL);
		commonLibrary.switchToWindow("print");
		// Close the secondary browser
		driver.close();
		report.updateTestLog("Close the secondary browser", "Closed the secondary browser", Status.PASS);
		commonLibrary.switchToWindow("alerts");

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts selectDocument(int intDocNo) {

		// String strDocTitle=null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		// List<WebElement>
		// LList=commonLibrary.isExist_List(OList.get(0),By.tagName("li"), 10);
		// WebElement
		// lnkTitle=commonLibrary.isExist(LList.get(intDocNo-1),By.cssSelector("a[data-action=shepardsalertresults]"),
		// 10);
		// strDocTitle=lnkTitle.getText();
		WebElement lstchkBox = commonLibrary.isExist(OList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
		String strI = "" + (intDocNo);
		if (lstchkBox != null)
			commonLibrary.setCheckBox(lstchkBox, strI);
		else
			report.updateTestLog("Select document", "document is not selected", Status.FAIL);

		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify all the documents become read
	// /unread
	// # Function Name : VerifyAllItemsBold    
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Alerts verifyAllItemsBoldUnbold(Boolean isBold) {
		Boolean isTrue = true;

		try {

			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (WebElement item : LList) {
				// WebElement
				// lnkTitle=commonLibrary.isExist(item,By.cssSelector("a[data-action=searchdocument]"),
				// 10);

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
					report.updateTestLog("Verify that the selected Alert is read and becomes unbold", "selected Alert is read and becomes unbold", Status.FAIL);

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify result is deleted
	// # Function Name : verifyResultsDeleted
	// # Author : Shobana
	// # Date Created : 24 Feb'15
	// #*****************************************************************************************************************************
	public AlertsSearchResults verifyResultsDeleted() {
		int iCount = 0;
		List<WebElement> OList = null;

		commonLibrary.sleep(5000);

		driver.navigate().refresh();

		commonLibrary.sleep(5000);

		do {
			iCount++;
			driver.navigate().refresh();
			OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		} while (OList.size() > 0 && iCount <= 10);
		// Verify that all the results for that content type is deleted
		if (OList.size() == 0)

			report.updateTestLog("Verify that all the results for that content type is deleted", "all the results for that content type is deleted", Status.PASS);
		else

			report.updateTestLog("Verify that all the results for that content type is deleted", "all the results for that content type is not deleted", Status.FAIL);
		return new AlertsSearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter verify alert present
	// # Function Name : verifyAlertPresent
	// # Author : Shobana
	// # Date Created : 24 Feb'15
	// #*****************************************************************************************************************************
	public Alerts verifyAlertPresent(String title, String terms, String filters) {
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		WebElement results = commonLibrary.isExist(resultList, UIMAP_SearchResult.resultwrapper, 20);
		List<WebElement> contentList = commonLibrary.isExistList(results, By.tagName("li"), 20);
		boolean flag = false;
		for (int i = 0; i < contentList.size(); i = i + 2) {
			WebElement docLink = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
			if (docLink.getText().equalsIgnoreCase(title)) {
				flag = true;
				report.updateTestLog(title + " is present under Alerts", title + " is present under Alerts", Status.PASS);
				if (terms != "") {
					WebElement content = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.tagNameArticle, 20);
					if (content.getText().contains("Terms\n" + terms))
						report.updateTestLog(terms + " is present under Terms", terms + " is present under Terms", Status.PASS);
					else
						report.updateTestLog(terms + " is present under Terms", terms + " is not present under Terms", Status.FAIL);
				}
				if (filters != "") {
					WebElement content = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.tagNameArticle, 20);
					System.out.println(content.getText());
					if (content.getText().contains("Filters\n" + filters))
						report.updateTestLog(filters + " is present under Filters", filters + " is present under Filters", Status.PASS);
					else
						report.updateTestLog(filters + " is present under Filters", filters + " is not present under Filters" + content.getText(), Status.FAIL);
				}
				break;
			}
		}
		if (!flag)
			report.updateTestLog(title + " is present under Alerts", title + " is not present under Alerts", Status.FAIL);

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete multiple alerts
	// # Function Name : deleteMultilpleAlerts     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Alerts deleteMultilpleAlerts(String titles) {
		try {
			String title[] = titles.split(";");
			boolean isalert = false;
			for (int i = 0; i < title.length; i++) {
				isalert = false;
				WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
				List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
				for (WebElement item : li) {
					List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : a) {
						if (item1.getAttribute("data-action").contains("searchalertresults")) {
							if (item1.getText().equalsIgnoreCase(title[i])) {
								WebElement chk1 = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 10);
								commonLibrary.setCheckBox(chk1, "Checkbox of " + title[i] + "");
								isalert = true;
								break;
							}
						}
					}
					if (isalert)
						break;
				}
			}
			if (isalert) {
				WebElement btnDelete = commonLibrary.isExist(UIMAP_Home.btnAlertDelete, 10);
				if (btnDelete != null) {
					commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Alert Icon");
					report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
				}
				WebElement btnDeleteAlert = commonLibrary.isExist(UIMAP_Home.btnDeleteAlert, 10);
				if (btnDeleteAlert != null) {
					// commonLibrary.highlightElement(btnDeleteAlert);
					commonLibrary.clickButtonParentWithWait(btnDeleteAlert, "Delete Alert");
					commonLibrary.sleep(10000);
				}
			} else {
				report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts setCheckboxBlueDottedDocument() {
		Boolean blnFlag = false;

		String strDocTitle = null;
		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		WebElement btnNextPage = null;

		do {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, By.tagName("a"), 2);
						WebElement blueDotted = commonLibrary.isExistNegative(document, UIMAP_SearchResult.blueDotted, 2);
						if (blueDotted != null) {

							strDocTitle = eleDocTitle.getText();
							report.updateTestLog("Alert Title ", "Alert Title is " + strDocTitle, Status.DONE);

							WebElement lstchkBox = commonLibrary.isExist(document, By.cssSelector("input[type='checkbox']"), 10);

							if (lstchkBox != null) {
								commonLibrary.setCheckBox(lstchkBox, strDocTitle);

								blnFlag = true;
								btnNextPage = null;
								this.putData("Alerts", "TC43824_ALERT_More_Dropdown_Options", "DocTitle", eleDocTitle.getText());
								break;
							}
						}

					}

				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.nextPage);
				if (btnNextPage != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
			}
		} while (btnNextPage != null);

		if (!blnFlag)

			report.updateTestLog("set checkbox for the document having blue dotted ", "Not checked on the alert having blue dotted ", Status.FAIL);

		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts selectPostFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 1;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			if (filterContainer != null) {
				List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

				List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
				for (i = 0; i < filterHeader.size(); i++) {
					if (filterHeader.get(i).getText().contains("Timeline"))
						j = 2;
					if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

						if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
							report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
						}

						List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.filterMore, 10);
						for (WebElement button : moreOptions) {
							if (button.getText().toLowerCase().contains("more"))
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
						List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_AlertsSearchResults.eltFilterList, 20);
						for (WebElement item : filters) {
							if (item.getText().contains(strFilter)) {
								if (browsername.contains("internet")) {
									WebElement span = commonLibrary.isExistNegative(item, By.tagName("span"), 10);

									commonLibrary.clickJS(span, item.getText());
								} else
									commonLibrary.clickButtonParentWithWait(item, strFilter);

								report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.", Status.DONE);
								Flag = true;
								break;
							}
						}
					}
					if (Flag)
						break;
				}
			} else {
				report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " section is not available", Status.FAIL);
			}

			if (!Flag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				if (eltFilter != null) {
					for (i = 0; i < eltFilter.size(); i++) {
						if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
							// commonLibrary.highlightElement(eltFilter.get(i));
							commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
							report.updateTestLog("Selecting Filter: " + strFilter, "Required Filter Selected.", Status.DONE);
							Flag = true;
							break;
						}
					}
				}
			}

		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		return new Alerts(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public AlertsSearchResults clickDocumentBlueDotted() {
		boolean isExist = false;
		String docTitle = dataTable.getData("General_Data", "DocTitle");

		try {

			commonLibrary.sleep(40000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (WebElement item : LList) {
				WebElement lnkTitle = commonLibrary.isExist(item, By.tagName("a"), 10);
				if (lnkTitle != null && lnkTitle.getText().equalsIgnoreCase(docTitle)) {
					commonLibrary.clickLinkWithWebElementWithWait(lnkTitle, lnkTitle.getText());
					isExist = true;
					break;
				}
			}

			if (!isExist)
				report.updateTestLog("Click on Alert " + docTitle, "Not Clicked on Alert " + docTitle, Status.FAIL);
			else {
				List<WebElement> viewusing = commonLibrary.isExistList(UIMAP_Document.viewusing, 20);

				if (viewusing.size() > 0)
					commonLibrary.clickJS(viewusing.get(0), "View Using" + viewusing.get(0).getAttribute("value"));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to put the data in to data sheet
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
	// # Function Description : Function to enter the alert title
	// # Function Name : enterAlertTitle    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts enterAlertTitle(String title) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			// Actions hover = new Actions(driver);
			// hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromList(topicalert, "Overview");

			WebElement alertTitle = commonLibrary.isExist(UIMAP_SearchResult.alertTitle, 10);
			if (alertTitle != null) {
				commonLibrary.setDataInTextBoxJavaScript(alertTitle.getAttribute("id"), title);
				// commonLibrary.SetDataInTextBox_Robot(alertTitle, title);
			}
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to edit the alert created
	// # Function Name : clickEditAlert    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Alerts clickEditAlert(String docTitle) {
		boolean flag = false;

		List<WebElement> alertsForms = commonLibrary.isExistList(UIMAP_AlertsSearchResults.alertsForm, 10);
		for (WebElement alertsForm : alertsForms) {
			List<WebElement> alertsList = commonLibrary.isExistList(alertsForm, UIMAP_AlertsSearchResults.alertsList, 10);
			for (WebElement alert : alertsList) {
				WebElement alertItemElement = commonLibrary.isExist(alert, UIMAP_AlertsSearchResults.docTitle, 10);
				if (alertItemElement == null)
					continue;

				String alertItemTitle = alertItemElement.getText();
				if (alertItemTitle.toLowerCase().equals(docTitle.toLowerCase())) {
					WebElement editAlert = commonLibrary.isExist(alert, UIMAP_AlertsSearchResults.btnEditAlert, 10);
					if (editAlert == null) {
						report.updateTestLog("Click on Edit for Alert '" + docTitle + "'", "Edit for Alert '" + docTitle + "' is not clicked", Status.FAIL);
						return new Alerts(scriptHelper);
					}

					commonLibrary.clickJS(editAlert);
					flag = true;
					break;
				}
			}
			if (flag) {
				report.updateTestLog("Click on Edit for Alert '" + docTitle + "'", "Edit for Alert '" + docTitle + "' is clicked", Status.DONE);
				break;
			} else
				report.updateTestLog("Click on Edit for Alert '" + docTitle + "'", "Edit for Alert '" + docTitle + "' is not clicked", Status.FAIL);
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to save the alert created
	// # Function Name : clickSaveAlert    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Alerts clickSaveAlert() {
		WebElement save = commonLibrary.isExist(UIMAP_AlertsSearchResults.btnSaveAlert);
		if (save != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(save, "Save");
			else
				commonLibrary.clickButtonParentWithWait(save, "Save");
		} else {
			report.updateTestLog("Click Save alert button", "Save alert button is not clicked", Status.FAIL);
		}

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to History or ResearchMap
	// page
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Shobana
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public History navigateToHistoryFooterLink(String strLinkName) {
		generalFunctions.navigateToHistoryFooterLink(strLinkName);
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public AlertsSearchResults alertClickDocLink(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.highlightElement(lnkDocument);
								if (browsername.contains("internet")) {
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								} else {
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								}

							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnNextPage, "Next Page");
				else
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}

		if (blnFlag) {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new AlertsSearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the browser back button
	// # Function Name : clickBrowserBack    
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Alerts clickBrowserBack() {

		try {
			// Actions builder = new Actions(driver);
			// builder.sendKeys(Keys.BACK_SPACE).perform();
			commonLibrary.sleep(20000);
			driver.navigate().back();

			report.updateTestLog("Click on Browser Back", "Clicked on Browser Back", Status.PASS);
			// driver.manage().timeouts().pageLoadTimeou(220,
			// TimeUnit.MILLISECONDS);
		} catch (Exception e) {

			e.printStackTrace();
			// report.updateTestLog("Click on Browser Back",
			// "Browser Back not clicked", Status.FAIL);
		}
		int count = 0;
		WebElement pagewrapper = commonLibrary.isExist(UIMAP_Document.pagewrapperpanel1, 20);
		do {
			count++;
			pagewrapper = commonLibrary.isExist(UIMAP_Document.pagewrapperpanel1, 20);
			commonLibrary.sleep(10000);
		} while (pagewrapper == null && count < 15);
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : clickDocLink_Document    
	// # Author : Aravind
	// # Date Created : May'15
	// #********************************************************************

	public Alerts clickDocLink_Document(String strDocTitle, String docType) {

		boolean blnDocType = false;

		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
						WebElement aside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 10);
						List<WebElement> typeList = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
						for (WebElement type : typeList) {
							if (type.getText().equalsIgnoreCase(docType)) {
								blnDocType = true;
								break;
							}
						}
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
						if (lnkDocument != null && blnDocType) {
							// commonLibrary.ScrollToView(lnkDocument);
							// commonLibrary.highlightElement(lnkDocument);
							if (browsername.contains("internet")) {
								// try
								// {
								// driver.manage().timeouts().pageLoadTimeout(1,
								// TimeUnit.SECONDS);
								// commonLibrary.click_MouseMove_Action(lnkDocument,
								// lnkDocument.getText());
								// // lnkDocument.click();// Using click ans
								// click_JS not working.
								// //
								// report.updateTestLog("Click on the document "
								// + lnkDocument.getText(),
								// "Not Clicked  on the document " +
								// lnkDocument.getText(), Status.PASS);;
								// } catch (TimeoutException ex) {
								// driver.manage().timeouts().pageLoadTimeout(220,
								// TimeUnit.SECONDS);
								// System.out.println(ex.toString());
								//
								// }
								// driver.manage().timeouts().pageLoadTimeout(220,
								// TimeUnit.SECONDS);
								commonLibrary.clickLinkWithWebElementWithWaitJS(lnkDocument, lnkDocument.getText());
							} else {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
							}
							break;
						}
					}

				}

			}
		}
		int counter = 0;
		do {
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			commonLibrary.sleep(5000);
			counter = counter + 1;
		} while (resultClass == null && counter <= 15);

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to view all history link
	// # Function Name : NavigateToViewAllHistoryLink    
	// # Author : Aravind
	// # Date Created : May'15
	// #********************************************************************

	public History navigateToViewAllHistoryLink(String strDocTitle) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Searches");

			WebElement lstrecentDocumentsContent = commonLibrary.isExist(UIMAP_Home.podFooterHistory, 20);
			if (lstrecentDocumentsContent != null) {
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstrecentDocumentsContent, By.linkText(strDocTitle), 20);
				if (lnkDocuments.size() > 0)
					commonLibrary.clickButtonParentWithWait(lnkDocuments.get(0), lnkDocuments.get(0).getText());

				commonLibrary.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc link in shepards page
	// # Function Name : alertClickDocLink_Shepards    
	// # Author : Aravind
	// # Date Created : May'15
	// #********************************************************************
	public AlertsShepardsResults alertClickDocLinkShepards(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.highlightElement(lnkDocument);
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								} else {
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								}

							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnNextPage, "Next Page");
				else
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}

		if (blnFlag) {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new AlertsShepardsResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete topic alert
	// # Function Name : deleteTopicAlert     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Alerts deleteTopicAlert(String alert) {
		boolean isalert = false;
		WebElement result = commonLibrary.isExist(UIMAP_Home.frmResult);
		List<WebElement> li = commonLibrary.isExistList(result, By.tagName("li"), 20);
		for (WebElement item : li) {
			List<WebElement> a = commonLibrary.isExistList(item, By.tagName("a"), 20);
			for (WebElement item1 : a) {
				if (item1.getAttribute("data-action").contains("topicalertresults")) {
					if (item1.getText().equalsIgnoreCase(alert)) {
						WebElement chk1 = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 10);
						commonLibrary.setCheckBox(chk1, "Checkbox of " + alert + "");
						isalert = true;
						break;
					}
				}
			}
			if (isalert)
				break;
		}
		if (isalert) {
			WebElement btnDelete = commonLibrary.isExist(UIMAP_Home.btnAlertDelete, 10);
			if (btnDelete != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDelete, "Delete Alert Icon");
				else
					commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Alert Icon");
				report.updateTestLog("Verify modal dialog is displayed", "Modal dialog is displayed", Status.PASS);
			}
			WebElement btnDeleteAlert = commonLibrary.isExist(UIMAP_Home.btnDeleteAlert, 10);
			if (btnDeleteAlert != null) {
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(btnDeleteAlert, "Delete Alert");
				} else
					commonLibrary.clickButtonParentWithWait(btnDeleteAlert, "Delete Alert");
			}
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter verify alert present
	// # Function Name : verifyAlertPresent
	// # Author : Shobana
	// # Date Created : 24 Feb'15
	// #*****************************************************************************************************************************
	public Alerts verifyAlertPresent1(String title, String publication, String date) {
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		WebElement results = commonLibrary.isExist(resultList, UIMAP_SearchResult.resultwrapper, 20);
		List<WebElement> contentList = commonLibrary.isExistList(results, By.tagName("li"), 20);
		boolean flag = false;
		for (int i = 0; i < contentList.size(); i = i + 2) {
			WebElement docLink = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
			WebElement blueDotted = commonLibrary.isExistNegative(contentList.get(i), UIMAP_SearchResult.blueDotted, 2);
			if (docLink.getText().equalsIgnoreCase(title) && blueDotted != null) {
				flag = true;
				report.updateTestLog("Verify blue dot appears next to " + title + " is present under Alerts", "Blue dot appears next to " + title + " is present under Alerts", Status.PASS);
				if (publication != "") {
					WebElement content = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.tagNameArticle, 20);
					if (content.getText().contains("Publication\n" + publication))
						report.updateTestLog(publication + " is present under Publication", publication + " is present under Publication", Status.PASS);
					else
						report.updateTestLog(publication + " is present under Publication", publication + " is not present under Publication", Status.FAIL);
				}
				if (date != "") {
					WebElement content = commonLibrary.isExist(contentList.get(i), UIMAP_SearchResult.tagNameArticle, 20);
					System.out.println(content.getText());
					if (content.getText().contains("Period\n" + date))
						report.updateTestLog(date + " is present under Period", date + " is present under Period", Status.PASS);
					else
						report.updateTestLog(date + " is present under Period", date + " is not present under Period" + content.getText(), Status.FAIL);
				}
				break;
			}
		}
		if (!flag)
			report.updateTestLog(title + " is present under Alerts", title + " is not present under Alerts", Status.FAIL);

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delivery options
	// # Function Name : clickDeliverySelectOption     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Alerts clickDeliverySelectOption(String option, String deliverOption) {
		generalFunctions.clickDeliverySelectOption_New(option, deliverOption);
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify VSA logo in Alerts page
	// # Function Name : verifyVSAlogo
	// # Author : Nancy
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Alerts verifyVSAlogo() {
		generalFunctions.verifyVSAlogo();
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the presence of Alert profile
	// page
	// # Function Name : verifyAlertsProfilePage
	// # Author : Nancy
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Alerts verifyAlertsProfilePage() {
		WebElement alertsprofilepage = commonLibrary.isExist(UIMAP_AlertsSearchResults.alertsProfilePage, 20);
		WebElement alertspagetitle = commonLibrary.isExist(UIMAP_AlertsSearchResults.alertsBannerVerif, 20);
		if (alertsprofilepage != null) {
			if (alertspagetitle.getText().contains("Alerts")) {
				report.updateTestLog("Verify Alerts profile page is displayed", "Alerts profile page appears", Status.PASS);
			} else {
				report.updateTestLog("Verify the page title appears as Alerts", "The page title does not appear as Alerts", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Alerts profile page is displayed", "Alerts profile page does not appear", Status.FAIL);
		}

		return new Alerts(scriptHelper);
	}

}