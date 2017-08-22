package functionallibraries;

import org.openqa.selenium.By;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import UIMAP.UIMAP_AlertsSearchResults;
import UIMAP.UIMAP_CaseValueAssessment;
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_IMContent;
import UIMAP.UIMAP_Interactivecitationworkstation;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_MedmalDashboard;
import UIMAP.UIMAP_RelatedContent;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Settings;
import UIMAP.UIMAP_Shepards;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_WorkFolders;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

//import javax.mail.Flags;
//import javax.mail.Folder;
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Store;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GeneralFunctions extends ReusableLibrary {
	public static int check = 0;
	public static int val = 0;
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	/**
	 * Constructor to initialize the functional library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link driverScript}
	 */
	public GeneralFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of pages to be displayed
	// # Function Name : SetNumberOfResultsToBeDisplayed     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void setNumberOfResultsToBeDisplayed(String number) {
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings,
				100);
		if (setting == null || !setting.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {

				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		}

		if (setting != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(setting, "Setting");
			else
				commonLibrary.clickLinkWithWebElementWithWait(setting,
						"Settings");
		}
		WebElement contentSwitcher = commonLibrary.isExistNegative(
				UIMAP_Home.contentSwitcher, 5);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(5000);
				contentSwitcher = commonLibrary.isExistNegative(
						UIMAP_Home.contentSwitcher, 5);
				count++;
			} while (contentSwitcher == null && count < 30);
		} catch (Exception e) {
			System.out.println(e.toString());
			commonLibrary.sleep(5000);
		}
		contentSwitcher = commonLibrary.isExistNegative(
				UIMAP_Home.contentSwitcher, 5);
		WebElement general = commonLibrary.isExistNegative(contentSwitcher,
				UIMAP_Home.generallink, 10);
		if (general != null)
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(general, "General");
			else
				commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement resultDropdown = commonLibrary.isExist(
				UIMAP_Home.resultsList, 100);
		if (resultDropdown != null) {
			commonLibrary.selectByVisibleText(resultDropdown, number);
			// commonLibrary.Select_FromList_OptionContains(resultDropdown,
			// number);
		}
		WebElement submit = commonLibrary.isExist(
				UIMAP_Settings.btnIdsubmitSettChange, 100);
		if (submit != null && submit.isEnabled()) {
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(submit, "submit");
			else
				commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(
					UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(cancel, "submit");
				else
					commonLibrary.clickButtonParentWithWait(cancel, "submit");

			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void logout() {

		// Click on More button
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {

			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");

		}
		commonLibrary.sleep(5000);
		// Click on signOut button
		WebElement lnkSignOut = commonLibrary.isExist(
				By.linkText(UIMAP_Home.lnkTextSignOut), 100);

		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {

			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);

			if (btnMore != null) {

				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");

			}

			lnkSignOut = commonLibrary.isExist(
					By.linkText(UIMAP_Home.lnkTextSignOut), 100);

		}

		// click on sign out if its not already clicked
		if (lnkSignOut != null)
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			else
				commonLibrary.clickMethod(lnkSignOut, "Sign Out");
		commonLibrary.sleep(5000);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to History or ResearchMap
	// page
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Shobana
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void navigateToHistoryFooterLink(String strLinkName) {

		try {
			commonLibrary.sleep(Mwait);
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(
					UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(
						UIMAP_Home.btnTitleMore, 10);

				if (browsername.contains("internet")) {
					commonLibrary.clickMethod(btnMore, "More");

				} else {
					commonLibrary.clickMethod(btnMore, "More");
				}
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow,
					"History");

			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(
						UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (lnkTxtViewAllHistory == null) {
					commonLibrary.clickLinkWithWebElement(
							btnIdHistoryMenuArrow, "History");
					lnkTxtViewAllHistory = commonLibrary.isExist(
							UIMAP_Home.lnkTxtViewAllHistory, 20);
				}
				if (browsername.contains("internet")) {
					// commonLibrary.click_JS(lnkTxtViewAllHistory,
					// "View All History");
					try {
						driver.manage().timeouts()
								.pageLoadTimeout(1, TimeUnit.SECONDS);
						commonLibrary.clickLinkWithWebElementWithWait(
								lnkTxtViewAllHistory, "View All History");
						commonLibrary.sleep(30000);
					} catch (TimeoutException ex) {
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println(ex.toString());

					}
					driver.manage().timeouts()
							.pageLoadTimeout(220, TimeUnit.SECONDS);
				} else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory,
							"View All History");
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(
						UIMAP_Home.lnkTxtResearchMap, 30);
				commonLibrary.sleep(Mwait);
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);
				if (lnkTxtResearchMap == null) {
					commonLibrary.clickJS(btnIdHistoryMenuArrow, "History");
					lnkTxtResearchMap = commonLibrary.isExist(
							UIMAP_Home.lnkTxtResearchMap, 30);

					if (browsername.contains("internet")) {
						// commonLibrary.click_JS(lnkTxtViewAllHistory,
						// "View All History");
						try {
							driver.manage().timeouts()
									.pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.sleep(Mwait);
							commonLibrary.sleep(Mwait);
							commonLibrary.clickLinkWithWebElementWithWait(
									lnkTxtResearchMap, "ResearchMap");
							commonLibrary.sleep(30000);
						} catch (TimeoutException ex) {
							driver.manage().timeouts()
									.pageLoadTimeout(220, TimeUnit.SECONDS);
							System.out.println(ex.toString());

						}
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
					}

				}

				else
					commonLibrary.clickLinkWithWebElementWithWait(
							lnkTxtResearchMap, "Research Map");

			}
			WebElement pagwrapper = commonLibrary.isExist(
					UIMAP_Home.txtPracticeAreaHeading, 20);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(20000);
			} while (pagwrapper == null && count < 15);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		Boolean xpathChk = false;

		for (int chk = 0; chk < 10; chk++) {

			// wait if the page loading image is displayed
			if (commonLibrary.isExistNegative(
					By.xpath("//*[@id='loadbox']/div/img"), 10) == null) {

				xpathChk = true;
				int i = 0;

				do {
					// Function call for search
					this.search(strSearchTerm, strClearFilter, i);
					commonLibrary.sleep(60000);

					// wait till the page loading state is complete
					documentState(driver);

				} while (i < 3 && !driver.getCurrentUrl().contains("search"));

				if (xpathChk)

					break;
			} else {

				try {
					commonLibrary.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				chk++;
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance product
	// pages
	// # Function Name : productSwitcher
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void productSwitcher(String pageName) {
		try {

			WebElement btnIdLexisAdvance = commonLibrary.isExist(
					UIMAP_Home.btnIdLexisAdvance, 20);
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
			else
				commonLibrary.clickMouseMoveAction(btnIdLexisAdvance,
						"Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);
			WebElement product = null;
			switch (pageName) {
			case "Research": {
				product = commonLibrary.isExist(
						UIMAP_Home.btnLexisAdvanceResearch, 20);
				break;
			}
			case "Global UI Builder": {
				product = commonLibrary.isExist(UIMAP_Home.btnActionGUIB, 20);
				break;
			}
			case "Verdict & Settlement Analyzer": {
				product = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
				break;
			}
			case "Litigation profile suite": {
				product = commonLibrary.isExist(
						UIMAP_Home.btnActionProfileSuite, 20);
				break;
			}
			case "Medmal Navigator": {
				product = commonLibrary.isExist(UIMAP_Home.btnActionMedMal, 20);
				break;
			}
			case "Practice Advisor": {
				product = commonLibrary.isExist(UIMAP_Home.btnActionLPA, 20);
				break;
			}
			case "Counsel Benchmarking": {
				product = commonLibrary.isExist(
						UIMAP_Home.btnActionCunselBenchmarking, 20);
				break;
			}
			}

			// Clicking the product in the global menu

			commonLibrary.clickLinkWithWebElement(product, "Lexis Advance® "
					+ pageName);
			commonLibrary.sleep(10000);

			WebElement CurrentProduct = commonLibrary.isExist(
					UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase()
					.contains(pageName.toLowerCase())
					|| (driver.getCurrentUrl().contains(pageName.replace(" ",
							"").toLowerCase()))) {
				report.updateTestLog(pageName + " landing page is displayed",
						pageName + " landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog(pageName + " landing page is displayed",
						pageName + " landing page is not displayed",
						Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void navigateToSettings() {
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
		if (btnMore != null)
			commonLibrary.clickButtonParentWithWait(btnMore, "More");

		WebElement lnkSettings = commonLibrary.isExist(
				UIMAP_Home.lnkTextSettings, 10);

		if (lnkSettings == null || !lnkSettings.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickButtonParentWithWait(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);
		}

		if (lnkSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSettings,
					"Settings");

		// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

		WebElement PgTitle = commonLibrary.isExist(
				UIMAP_Settings.PgTitleSettings, 10);
		if (PgTitle != null) {
			report.updateTestLog("Verify Settings Page is displayed",
					"Settings Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Settings Page is displayed",
					"Settings Page is NOT displayed", Status.FAIL);

		}
		int count = 0;
		WebElement btnGeneral = commonLibrary.isExistNegative(
				UIMAP_Settings.btnIdGeneral, 10);

		do {
			commonLibrary.sleep(10000);
			btnGeneral = commonLibrary.isExistNegative(
					UIMAP_Settings.btnIdGeneral, 5);
			count++;
		} while (btnGeneral != null && count < 15);

		if (btnGeneral != null
				&& btnGeneral.getAttribute("class").toLowerCase()
						.contains("active")) {
			report.updateTestLog(
					"Verify 'General' settings section displayed by default",
					"'General' settings section displayed by default",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Verify 'General' settings section displayed by default",
					"'General' settings section NOT displayed by default",
					Status.WARNING);
			if (btnGeneral != null)
				commonLibrary.clickButtonParentWithWait(btnGeneral,
						"General Tab");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Alerts page
	// # Function Name : navigateToAlerts
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void navigateToAlerts() {

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		if (btnMore != null)
			commonLibrary.clickMethod(btnMore, "More");

		WebElement alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);

		if (alert == null) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		}

		if (alert != null) {
			commonLibrary.clickLinkWithWebElementWithWait(alert, "Alerts");
		} else {
			report.updateTestLog("Click on alerts",
					"Alerts button is not clicked", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the number of results
	// # Function Name : verifyNumberOfResults
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void verifyNumberOfResults(int number, boolean greater) {
		if (greater) {
			WebElement resultClass = commonLibrary.isExist(
					UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				List<WebElement> title = commonLibrary.isExistList(resultClass,
						UIMAP_SearchResult.docLink, 10);
				if (title.size() > number) {
					report.updateTestLog(
							"Verify number of listed in the page is greater than "
									+ number + "",
							"Number of listed in the page  is greater than "
									+ number + "", Status.PASS);
				} else {
					report.updateTestLog(
							"Verify number of listed in the page is greater than "
									+ number + "",
							"Number of listed in the page  is not greater than "
									+ number + "", Status.FAIL);
				}
			} else {
				report.updateTestLog(
						"Verify number of listed in the page is greater than "
								+ number + "",
						"Number of listed in the page is not greater than "
								+ number + "", Status.FAIL);
			}
		} else {
			WebElement resultClass = commonLibrary.isExist(
					UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				List<WebElement> title = commonLibrary.isExistList(resultClass,
						UIMAP_SearchResult.docLink, 10);
				if ((title.size()) == number) {
					report.updateTestLog(
							"Verify number of listed in the page are " + number
									+ "", "Number of listed in the page are "
									+ number + "", Status.PASS);
				} else {
					report.updateTestLog(
							"Verify number of listed in the page are " + number
									+ "",
							"Number of listed in the page is not " + number
									+ "", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify number of listed in the page are "
						+ number + "", "Number of listed in the page is not "
						+ number + "", Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down
	// # Function Name : selectSortBy
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void selectSortBy(String value) {
		try {
			boolean flag = false;
			WebElement sortByList = null, sortContainer = null, sortBy = null;
			int i = 0;

			do {

				sortContainer = commonLibrary.isExist(
						UIMAP_SearchResult.dropdownContainer, 10);
				sortBy = commonLibrary.isExist(sortContainer,
						UIMAP_SearchResult.sortByBtn, 10);

				if (sortBy.getAttribute("class").contains("collapsed"))
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(sortBy,
								"Sort By");
					else
						commonLibrary.clickButtonParentWithWait(sortBy,
								"Sort By");

				sortByList = commonLibrary.isExist(
						UIMAP_SearchResult.sortByList, 10);
				i++;
			} while (sortByList == null && i < 3);

			if (sortByList != null) {
				List<WebElement> sortValues = commonLibrary.isExistList(
						sortByList, By.tagName("li"), 20);
				for (WebElement item : sortValues) {
					if (item.getText().contains(value)) {
						WebElement button = commonLibrary.isExistNegative(item,
								UIMAP_SearchResult.btnButton, 10);
						if (browsername.contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(button,
									value);
						else
							commonLibrary.clickButtonParentWithWait(button,
									value);
						report.updateTestLog("Click on " + value
								+ " in Sort By drop down", value
								+ " is selected from Sort By drop down",
								Status.PASS);
						commonLibrary.sleep(10000);
						flag = true;
						break;
					}
				}
				if (flag) {
					sortContainer = commonLibrary.isExist(
							UIMAP_SearchResult.dropdownContainer, 10);
					sortBy = commonLibrary.isExist(sortContainer,
							UIMAP_SearchResult.sortByBtn, 10);
					if (sortBy.getText().contains(value))
						report.updateTestLog(
								value
										+ " is the active item in the Sort by dropdown",
								value
										+ " is the active item in the Sort by dropdown",
								Status.PASS);
					else
						report.updateTestLog(
								value
										+ " is the active item in the Sort by dropdown",
								sortBy.getText()
										+ " is the active item in the Sort by dropdown",
								Status.FAIL);
				}
			} else
				report.updateTestLog("Click on " + value
						+ " in Sort By drop down",
						"Sort By drop down is not expanded", Status.FAIL);

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(
						By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(10000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp),
							5);
					count++;
				} while (loader != null && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// ascending order
	// # Function Name : verifyDocTitleSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyDocTitleSortedResultAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					int index1 = 0;
					int index2 = 0;
					WebElement docTitle = commonLibrary.isExist(docList.get(i),
							UIMAP_SearchResult.TitleClassDoc, 20);
					WebElement docTitle1 = commonLibrary.isExist(
							docList.get(i + 1),
							UIMAP_SearchResult.TitleClassDoc, 20);
					String title = docTitle.getText();
					String title1 = docTitle1.getText();
					char[] num = new char[title.length()];
					char[] num1 = new char[title1.length()];

					int loc1 = 0;
					int loc2 = 0;

					char ch1 = title.charAt(0);
					char ch2 = title1.charAt(0);
					if (title.contains("No Title"))
						posflag++;
					else if (title.equalsIgnoreCase(title1))
						posflag++;
					else if (Character.isDigit(ch1) && Character.isDigit(ch2)) {

						do {
							num[loc1++] = ch1;
							index1++;

							if (index1 < title.length()) {
								ch1 = title.charAt(index1);
							} else {
								break;
							}
						} while (Character.isDigit(ch1));

						do {
							num1[loc2++] = ch2;
							index2++;

							if (index2 < title1.length()) {
								ch2 = title1.charAt(index2);
							} else {
								break;
							}
						} while (Character.isDigit(ch2));

						String str1 = new String(num);
						String str2 = new String(num1);
						int number = Integer.parseInt(str1.trim());

						int number1 = Integer.parseInt(str2.trim());
						if (number <= number1)
							posflag++;
						else
							negFlag++;
					} else if (Character.isDigit(ch1)
							&& (!Character.isDigit(ch2))) {
						posflag++;
					} else if (Character.isLetter(ch1)
							&& (Character.isLetter(ch2))) {
						title = title.replace("(", "");
						title = title.replace(")", "");
						title1 = title1.replace("(", "");
						title1 = title1.replace(")", "");
						if ((title
								.toLowerCase()
								.replace(" ", "")
								.replaceAll("[^a-zA-Z]", "")
								.compareTo(
										title1.toLowerCase().replace(" ", "")
												.replaceAll("[^a-zA-Z]", "")) <= 0))
							posflag++;
						else
							negFlag++;
					} else if (((!Character.isDigit(ch1)) && (!Character
							.isLetter(ch1)))
							&& ((Character.isLetter(ch2)) || (Character
									.isDigit(ch2))))
						posflag++;
					else if (((!Character.isDigit(ch1)) && (!Character
							.isLetter(ch1)))
							&& ((!Character.isDigit(ch1)) && (!Character
									.isLetter(ch1))))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in ascending order of Document Title",
							"The results are sorted in ascending order of Document Title",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in ascending order of Document Title",
							"The results are not sorted in ascending order of Document Title",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// descending order
	// # Function Name : verifyDocTitleSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyDocTitleSortedResultZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					int index1 = 0;
					int index2 = 0;
					WebElement docTitle = commonLibrary.isExist(docList.get(i),
							UIMAP_SearchResult.TitleClassDoc, 20);
					WebElement docTitle1 = commonLibrary.isExist(
							docList.get(i + 1),
							UIMAP_SearchResult.TitleClassDoc, 20);
					String title = docTitle.getText();
					String title1 = docTitle1.getText();
					char[] num = new char[title.length()];
					char[] num1 = new char[title1.length()];

					int loc1 = 0;
					int loc2 = 0;

					char ch1 = title.charAt(0);
					char ch2 = title1.charAt(0);
					if (title.equalsIgnoreCase(title1))
						posflag++;
					else if (Character.isDigit(ch1) && Character.isDigit(ch2)) {

						do {
							num[loc1++] = ch1;
							index1++;

							if (index1 < title.length()) {
								ch1 = title.charAt(index1);
							} else {
								break;
							}
						} while (Character.isDigit(ch1));

						do {
							num1[loc2++] = ch2;
							index2++;

							if (index2 < title1.length()) {
								ch2 = title1.charAt(index2);
							} else {
								break;
							}
						} while (Character.isDigit(ch2));

						String str1 = new String(num);
						String str2 = new String(num1);
						int number = Integer.parseInt(str1.trim());

						int number1 = Integer.parseInt(str2.trim());
						if (number >= number1)
							posflag++;
						else
							negFlag++;
					} else if (Character.isLetter(ch1)
							&& (!Character.isDigit(ch2))) {
						posflag++;
					} else if (Character.isLetter(ch1)
							&& (Character.isLetter(ch2))) {
						if ((title
								.toLowerCase()
								.replace(" ", "")
								.compareTo(
										title1.toLowerCase().replace(" ", "")) > 0))
							posflag++;
						else
							negFlag++;
					} else if ((Character.isDigit(ch1) || Character
							.isLetter(ch1))
							&& ((!(Character.isLetter(ch2))) && (!(Character
									.isDigit(ch2)))))
						posflag++;
					else if (((!Character.isDigit(ch1)) && (!Character
							.isLetter(ch1)))
							&& ((!Character.isDigit(ch1)) && (!Character
									.isLetter(ch1))))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in descending order of Document Title",
							"The results are sorted in descending order of Document Title",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in descending order of Document Title",
							"The results are not sorted in descending order of Document Title",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// ascending order
	// # Function Name : verifyJurisSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyJurisSortedResultAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("U.S. Federal"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) < 0))
						posflag++;
					else if (dataDefn1.equalsIgnoreCase("Non-jurisdictional"))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in ascending order of Jurisdiction with U.S.Federal at the top",
							"The results are sorted in ascending order of Jurisdiction U.S.Federal at the top",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in ascending order of Jurisdiction with U.S.Federal at the top",
							"The results are not sorted in ascending order of Jurisdiction with U.S.Federal at the top",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// descending order
	// # Function Name : verifyJurisSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyJurisSortedResultZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("Non-jurisdictional"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase("Tribal"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else if (dataDefn1.equalsIgnoreCase("U.S. Federal"))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in descending order of Jurisdiction with U.S.Federal at the bottom",
							"The results are sorted in descending order of Jurisdiction U.S.Federal at the bottom",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in descending order of Jurisdiction with U.S.Federal at the bottom",
							"The results are not sorted in descending order of Jurisdiction with U.S.Federal at the bottom",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting highest to
	// lowest
	// # Function Name : verifySortCourtHighToLow
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifySortCourtHighToLow() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Court")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Court")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					int index1 = 0;
					int index2 = 0;
					char[] num = new char[dataDefn.length()];
					char[] num1 = new char[dataDefn1.length()];

					int loc1 = 0;
					int loc2 = 0;

					char ch1 = dataDefn.charAt(0);
					char ch2 = dataDefn1.charAt(0);

					if (dataDefn.equalsIgnoreCase("Supreme Court"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (Character.isDigit(ch1) && Character.isDigit(ch2)) {

						do {
							num[loc1++] = ch1;
							index1++;

							if (index1 < dataDefn.length()) {
								ch1 = dataDefn.charAt(index1);
							} else {
								break;
							}
						} while (Character.isDigit(ch1));

						do {
							num1[loc2++] = ch2;
							index2++;

							if (index2 < dataDefn1.length()) {
								ch2 = dataDefn1.charAt(index2);
							} else {
								break;
							}
						} while (Character.isDigit(ch2));

						String str1 = new String(num);
						String str2 = new String(num1);
						int number = Integer.parseInt(str1.trim());

						int number1 = Integer.parseInt(str2.trim());
						if (number <= number1)
							posflag++;
						else
							negFlag++;
					} else if (Character.isDigit(ch1)
							&& (!Character.isDigit(ch2)))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) < 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in ascending order of Court with Supreme Court at the top",
							"The results are sorted in ascending order of Court with Supreme Court at the top",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in ascending order of Court with Supreme Court at the top",
							"The results are not sorted in ascending order of Court with Supreme Court at the top",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting lowest to
	// highest
	// # Function Name : verifySortCourtLowTohigh
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifySortCourtLowTohigh() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("Tribal"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in descending order of Court with Tribal Jurisdiction courts at the top",
							"The results are sorted in descending order of Court with Tribal Jurisdiction courts at the top",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in descending order of Court with Tribal Jurisdiction courts at the top",
							"The results are not sorted in descending order of Court with Tribal Jurisdiction courts at the top",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting newest to oldest
	// # Function Name : verifyDateSortNewToOld
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyDateSortNewToOld() {
		try {

			int posflag = 0;
			int negFlag = 0;

			WebElement resultClass = commonLibrary.isExist(
					UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> docList = commonLibrary.isExistList(
							OListResult, By.tagName("li"), 20);
					for (int i = 0; i < docList.size() - 1; i++) {
						WebElement docContent = commonLibrary.isExist(
								docList.get(i), UIMAP_SearchResult.docContent,
								20);
						WebElement aside = commonLibrary.isExist(docContent,
								UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms = commonLibrary.isExistList(
								aside, By.tagName("dt"), 20);
						List<WebElement> dataDefns = commonLibrary.isExistList(
								aside, By.tagName("dd"), 20);
						String dataDefn = null;
						String dataDefn1 = null;
						for (int t = 0; t < dataTerms.size(); t++) {
							if (dataTerms.get(t).getText()
									.equalsIgnoreCase("Date")) {
								dataDefn = dataDefns.get(t).getText();
								break;
							}
						}

						WebElement docContent1 = commonLibrary.isExist(
								docList.get(i + 1),
								UIMAP_SearchResult.docContent, 20);
						WebElement aside1 = commonLibrary.isExist(docContent1,
								UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms1 = commonLibrary
								.isExistList(aside1, By.tagName("dt"), 20);
						List<WebElement> dataDefns1 = commonLibrary
								.isExistList(aside1, By.tagName("dd"), 20);
						for (int t = 0; t < dataTerms1.size(); t++) {
							if (dataTerms1.get(t).getText()
									.equalsIgnoreCase("Date")) {
								dataDefn1 = dataDefns1.get(t).getText();
								break;
							}
						}

						SimpleDateFormat format = new SimpleDateFormat(
								"MMM dd, yyyy");
						Date date = null;
						try {
							date = format.parse(dataDefn);
						} catch (Exception e) {
							// posflag++;
						}
						Date date1 = null;
						try {
							date1 = format.parse(dataDefn1);
						} catch (Exception e) {
							// posflag++;
						}
						if (date == null || date1 == null)
							posflag++;
						else if (dataDefn.equalsIgnoreCase(dataDefn1))
							posflag++;
						else if (date.compareTo(date1) >= 0)
							posflag++;
						else
							negFlag++;
					}
					if (posflag == (docList.size() - 1) && (negFlag == 0)) {
						report.updateTestLog(
								"The results are sorted in date order from mewest to oldest",
								"The results are sorted in date order from mewest to oldest",
								Status.PASS);
					} else
						report.updateTestLog(
								"The results are sorted in date order from mewest to oldest",
								"The results are sorted in date order from mewest to oldest",
								Status.FAIL);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting oldest to newest
	// # Function Name : verifyDateSortOldtoNew
	// # Author : Shobana
	// # Date Created : 26 Feb'15
	// #*****************************************************************************************************************************
	public void verifyDateSortOldtoNew() {
		try {

			int posflag = 0;
			int negFlag = 0;
			commonLibrary.sleep(10000);

			WebElement resultClass = commonLibrary.isExist(
					UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> docList = commonLibrary.isExistList(
							OListResult, By.tagName("li"), 20);
					for (int i = 0; i < docList.size() - 1; i++) {
						WebElement docContent = commonLibrary.isExist(
								docList.get(i), UIMAP_SearchResult.docContent,
								20);
						if (docContent == null)
							docContent = commonLibrary.isExist(docList.get(i),
									UIMAP_SearchResult.docContent1, 20);
						WebElement aside = commonLibrary.isExist(docContent,
								UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms = commonLibrary.isExistList(
								aside, By.tagName("dt"), 20);
						List<WebElement> dataDefns = commonLibrary.isExistList(
								aside, By.tagName("dd"), 20);
						String dataDefn = null;
						String dataDefn1 = null;
						commonLibrary.sleep(10000);
						for (int t = 0; t < dataTerms.size(); t++) {
							if (dataTerms.get(t).getText()
									.equalsIgnoreCase("Date")) {
								dataDefn = dataDefns.get(t).getText();
								break;
							}
						}

						WebElement docContent1 = commonLibrary.isExist(
								docList.get(i + 1),
								UIMAP_SearchResult.docContent, 20);
						if (docContent1 == null)
							docContent1 = commonLibrary.isExist(docList.get(i),
									UIMAP_SearchResult.docContent1, 20);
						WebElement aside1 = commonLibrary.isExist(docContent1,
								UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms1 = commonLibrary
								.isExistList(aside1, By.tagName("dt"), 20);
						List<WebElement> dataDefns1 = commonLibrary
								.isExistList(aside1, By.tagName("dd"), 20);
						commonLibrary.sleep(10000);
						for (int t = 0; t < dataTerms1.size(); t++) {
							if (dataTerms1.get(t).getText()
									.equalsIgnoreCase("Date")) {
								dataDefn1 = dataDefns1.get(t).getText();
								break;
							}
						}

						SimpleDateFormat format = new SimpleDateFormat(
								"MMM dd, yyyy");

						Date date = null;
						try {
							date = format.parse(dataDefn);
						} catch (Exception e) {
							posflag++;
							continue;
						}
						Date date1 = null;
						try {
							date1 = format.parse(dataDefn1);
						} catch (Exception e) {
							posflag++;
							continue;
						}

						if (dataDefn.equalsIgnoreCase(dataDefn1))
							posflag++;
						else if (date.compareTo(date1) <= 0)
							posflag++;
						else
							negFlag++;
					}
					if (posflag == (docList.size() - 1) && (negFlag == 0)) {
						report.updateTestLog(
								"The results are sorted in date order from mewest to oldest",
								"The results are sorted in date order from mewest to oldest",
								Status.PASS);
					} else
						report.updateTestLog(
								"The results are sorted in date order from mewest to oldest",
								"The results are not sorted in date order from mewest to oldest",
								Status.FAIL);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content results in ascending
	// order
	// # Function Name : verifyContentSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyContentSortedResultAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Content")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Content")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					String title = dataDefn;
					String title1 = dataDefn1;

					char ch1 = title.charAt(0);
					char ch2 = title1.charAt(0);

					if (Character.isDigit(ch1) && Character.isDigit(ch2)) {

						posflag++;

					} else if (Character.isDigit(ch1)
							&& (!Character.isDigit(ch2))) {
						posflag++;
					}

					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (((dataDefn.replace(" ", "").toLowerCase()
							.compareTo(dataDefn1.replace(" ", "").toLowerCase())) < 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in ascending order of Content",
							"The results are sorted in ascending order of Content",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in ascending order of Content",
							"The results are not sorted in ascending order of Content",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content results in descending
	// order
	// # Function Name : verifyContentSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyContentSortedResultZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Content")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Content")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in descending order of Content",
							"The results are sorted in descending order of Content",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in descending order of Content",
							"The results are not sorted in descending order of Content",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Agency results in ascending
	// order
	// # Function Name : verifyContentSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyAgencySortAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Agency")) {
							dataDefn = dataDefns.get(t).getText();
							System.out.println(dataDefn);
							break;
						}
					}
					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Agency")) {
							dataDefn1 = dataDefns1.get(t).getText();
							System.out.println(dataDefn1);
							break;
						}
					}
					
					System.out.println(dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")));
					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) < 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in ascending order of Agency",
							"The results are sorted in ascending order of Agency",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in ascending order of Agency",
							"The results are not sorted in ascending order of Agency",
							Status.FAIL);
				commonLibrary.sleep(50000);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Agency results in descending
	// order
	// # Function Name : verifyAgencySortZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifyAgencySortZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(
				UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass,
					By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(
						OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(
							docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(
							aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(
							aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText()
								.equalsIgnoreCase("Agency")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(
							docList.get(i + 1), UIMAP_SearchResult.docContent,
							20);
					WebElement aside1 = commonLibrary.isExist(docContent1,
							UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(
							aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(
							aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText()
								.equalsIgnoreCase("Agency")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(
							dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog(
							"The results are sorted in descending order of Agency",
							"The results are sorted in descending order of Agency",
							Status.PASS);
				} else
					report.updateTestLog(
							"The results are sorted in descending order of Agency",
							"The results are not sorted in descending order of Agency",
							Status.FAIL);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sort Court Highest Date
	// newest
	// # Function Name : verifySortCourtHighDateNew
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public void verifySortCourtHighDateNew() {
		try {

			WebElement resultClass = commonLibrary.isExist(
					UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> docList = commonLibrary.isExistList(
							OListResult, By.tagName("li"), 20);
					String supremeCourt[] = new String[docList.size()];
					String circuitCourt[] = new String[docList.size()];
					String othercourt[] = new String[docList.size()];
					int s = 0;
					int c = 0;
					int o = 0;
					for (int i = 0; i < docList.size() - 1; i++) {
						WebElement docContent = commonLibrary.isExist(
								docList.get(i), UIMAP_SearchResult.docContent,
								20);
						WebElement aside = commonLibrary.isExist(docContent,
								UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms = commonLibrary.isExistList(
								aside, By.tagName("dt"), 20);
						List<WebElement> dataDefns = commonLibrary.isExistList(
								aside, By.tagName("dd"), 20);
						boolean flag = false;
						for (int t = 0; t < dataTerms.size(); t++) {
							if (dataTerms.get(t).getText()
									.equalsIgnoreCase("Court")
									&& dataDefns.get(t).getText()
											.equalsIgnoreCase("Supreme Court")) {
								for (int d = 0; d < dataTerms.size(); d++) {
									if (dataTerms.get(d).getText()
											.equalsIgnoreCase("Date")) {
										supremeCourt[s] = dataDefns.get(d)
												.getText();
										s++;
										flag = true;
										break;
									}
								}
							} else if (dataTerms.get(t).getText()
									.equalsIgnoreCase("Court")
									&& dataDefns
											.get(t)
											.getText()
											.endsWith(
													"Circuit Court of Appeals")) {
								for (int d = 0; d < dataTerms.size(); d++) {
									if (dataTerms.get(d).getText()
											.equalsIgnoreCase("Date")) {
										circuitCourt[c] = dataDefns.get(d)
												.getText();
										c++;
										flag = true;
										break;
									}
								}
							} else if (dataTerms.get(t).getText()
									.equalsIgnoreCase("Court")
									&& dataDefns
											.get(t)
											.getText()
											.equalsIgnoreCase(
													dataDefns.get(t + 1)
															.getText())) {
								for (int d = 0; d < dataTerms.size(); d++) {
									if (dataTerms.get(d).getText()
											.equalsIgnoreCase("Date")) {
										othercourt[o] = dataDefns.get(d)
												.getText();
										o++;
										flag = true;
										break;
									}
								}
							}
							if (flag)
								break;
						}
					}
					boolean flag1 = false;
					boolean flag2 = false;
					boolean flag3 = false;
					int d = 0;
					if (supremeCourt != null) {
						int posflag = 0;
						int negFlag = 0;
						for (d = 0; supremeCourt[d + 1] != null; d++) {
							SimpleDateFormat format = new SimpleDateFormat(
									"MMM dd, yyyy");
							Date date = format.parse(supremeCourt[d]);
							Date date1 = format.parse(supremeCourt[d + 1]);
							if (date.compareTo(date1) >= 0)
								posflag++;
							else
								negFlag++;
						}
						if (posflag == d && (negFlag == 0)) {
							flag1 = true;
						}
					}

					int d1 = 0;
					if (circuitCourt != null) {
						int posflag = 0;
						int negFlag = 0;
						for (d1 = 0; circuitCourt[d1 + 1] != null; d1++) {
							SimpleDateFormat format = new SimpleDateFormat(
									"MMM dd, yyyy");
							Date date = format.parse(circuitCourt[d1]);
							Date date1 = format.parse(circuitCourt[d1 + 1]);
							if (date.compareTo(date1) >= 0)
								posflag++;
							else
								negFlag++;
						}
						if (posflag == d1 && (negFlag == 0)) {
							flag2 = true;
						}
					}

					int d2 = 0;
					if (othercourt != null) {
						int posflag = 0;
						int negFlag = 0;
						for (d2 = 0; othercourt[d2 + 1] != null; d2++) {
							SimpleDateFormat format = new SimpleDateFormat(
									"MMM dd, yyyy");
							Date date = format.parse(othercourt[d2]);
							Date date1 = format.parse(othercourt[d2 + 1]);
							if (date.compareTo(date1) >= 0)
								posflag++;
							else
								negFlag++;
						}
						if (posflag == d2 && (negFlag == 0)) {
							flag3 = true;
						}
					}

					if (flag1 && flag2 && flag3) {
						report.updateTestLog(
								"The results are sorted in Court Highest date newest order",
								"The results are sorted in Court Highest date newest order",
								Status.PASS);
					} else
						report.updateTestLog(
								"The results are sorted in Court Highest date newest order",
								"The results are not sorted in Court Highest date newest order",
								Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Download button and default
	// settings
	// # Function Name : clickDownloadDefaultSettings    
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickDownloadDefaultSettings(String AutoITPath,
			String fileName, String filePath, boolean verifyContent,
			String PDFText, String TextExistence, String parentWindow) {
		try {
			// WebElement btnDownload =
			// commonLibrary.isExist(UIMAP_Document.btnDownload, 10);
			// if (btnDownload != null) {
			// commonLibrary.clickButton_Parent_WithWait(btnDownload,
			// "Download");
			// }
			//
			// WebElement defaultSettings =
			// commonLibrary.isExist(UIMAP_Document.btnDownloadNow, 10);
			// if (defaultSettings != null) {
			// defaultSettings.click();
			// }
			clickDeliverySelectOption("delivery", "downloadnow");

			commonLibrary.switchToWindow("deliverysecondarywindow");
			if (!browsername.contains("internet"))
				commonLibrary.sleep(25000);

			String header = null;
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			try {
				do {
					pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
					if (pgHeader != null) {
						header = pgHeader.getText().toLowerCase();
						if (header.contains("delivery complete"))
							break;
					}
					commonLibrary.sleep(35000);
					pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);

				} while (header.contains("processing"));
			} catch (StaleElementReferenceException e) {
				System.out.println(e);
			}

			if (browsername.contains("firefox")) {
				commonLibrary.sleep(4000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_S);
				// CTRL+Z is now pressed (receiving application should see a
				// "key down" event.)
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_ALT);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			} else if (browsername.contains("internet")
					&& version.contains("8")) {
				commonLibrary.sleep(4000);

				String[] cmd = { AutoITPath, "Save As", filePath, fileName };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet")) {
				commonLibrary.sleep(3000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_F6);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_TAB);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			}

			commonLibrary.sleep(30000);

			String downloadedFilename = filePath + "\\" + fileName;
			WebElement lnkDownload = commonLibrary.isExist(
					UIMAP_Document.lnkDownloadedDoc, 10);
			pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null
					&& pgHeader.getText().toLowerCase()
							.contains("delivery complete")
					&& lnkDownload != null)
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.FAIL);

			WebElement btnDownloadClose = commonLibrary.isExist(
					UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow(parentWindow);

			// Verify content in the downloaded document
			if (verifyContent) {
				// Unzip the document present
				String zipFile = filePath + "\\" + fileName + ".ZIP";
				fileUnZip(filePath, zipFile);
				String arrPDFText[] = PDFText.split("#");
				String arrTextExistence[] = TextExistence.split(";");
				String FileDownloaded = downloadedFilename + ".PDF";
				for (int i = 0; i < arrPDFText.length; i++) {
					pdfVerificationPath(FileDownloaded, arrPDFText[i],
							arrTextExistence[i]);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Unzip contents from a zip file and
	// save it in a destination folder
	// # Function Name : fileUnZip     
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public void fileUnZip(String outputPath, String zipFile) {
		try {

			File destDir = new File(outputPath);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
					zipFile));
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = outputPath + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(filePath));
					byte[] bytesIn = new byte[4096];
					int read = 0;
					while ((read = zipIn.read(bytesIn)) != -1) {
						bos.write(bytesIn, 0, read);
					}
					bos.close();

				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify contents in a PDF file
	// # Function Name : PDFVerification_Path     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void pdfVerificationPath(String FilePath, String Text, String Exist)
			throws IOException {
		try {
			PdfReader reader = new PdfReader(FilePath);
			int Pages = reader.getNumberOfPages();
			boolean blnFlag = false;
			String TestText = "";
			for (int i = 1; i <= Pages; i++) {
				try {
					TestText = PdfTextExtractor.getTextFromPage(reader, i);
				} catch (Exception e) {
					System.out.println(e.toString());
					TestText = "";
				}
				if (TestText == null)
					TestText = "";
				if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
					blnFlag = true;
					break;
				}

			}
			switch (Exist) {
			case "Exist": {
				if (blnFlag) {
					report.updateTestLog("Verify '" + Text
							+ "' is present in the PDF document", "'" + Text
							+ "' is present in the PDF document", Status.PASS);

				} else {
					report.updateTestLog("Verify '" + Text
							+ "' is present in the PDF document", "'" + Text
							+ "' is not present in the PDF document",
							Status.FAIL);
				}
				break;
			}
			case "NotExist": {
				if (!blnFlag) {
					report.updateTestLog("Verify '" + Text
							+ "' is not present in the PDF document", "'"
							+ Text + "' is not present in the PDF document",
							Status.PASS);
				} else {
					report.updateTestLog("Verify '" + Text
							+ "' is not present in the PDF document", "'"
							+ Text + "' is present in the PDF document",
							Status.FAIL);
				}
				break;
			}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink1     
	// # Author :
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickDocLink(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		WebElement resultClassOld, resultClassNew;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);
			resultClassOld = resultClass;
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(
						resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary
								.isExistList(list, UIMAP_SearchResult.listtag,
										20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(
									document, UIMAP_SearchResult.TitleClassDoc,
									2);
							if (eleDocTitle != null
									&& eleDocTitle
											.getText()
											.toLowerCase()
											.replaceAll("[^a-zA-Z0-9]", "")
											.trim()
											.equals(strDocTitle
													.toLowerCase()
													.replaceAll("[^a-zA-Z0-9]",
															"").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(
										eleDocTitle, UIMAP_SearchResult.atag,
										20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary
											.clickLinkWithWebElementWithWait(
													lnkDocument,
													lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(
						UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null) {
					commonLibrary.clickButtonParentWithWait(btnNextPage,
							"Next Page");
					resultClassNew = commonLibrary.isExistNegative(
							UIMAP_SearchResult.frmClassResult, 5);
					int count = 0;
					try {
						do {
							commonLibrary.sleep(1000);
							count++;
							resultClassNew = commonLibrary.isExistNegative(
									UIMAP_SearchResult.frmClassResult, 5);
						} while (resultClassOld.equals(resultClassNew)
								&& count < 25);
					} catch (Exception e) {
						commonLibrary.sleep(1000);
						System.out.println(e.toString());
					}

				} else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle,
					"Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {

			WebElement atd = commonLibrary.isExistNegative(
					UIMAP_Document.eltAboutThisDoc, 3);

			int counter = 0;
			do {
				counter = counter + 1;
				commonLibrary.sleep(20000);
				atd = commonLibrary.isExistNegative(
						UIMAP_Document.eltAboutThisDoc, 3);
				if (atd == null && counter > 20)
					atd = commonLibrary.isExistNegative(
							UIMAP_Document.copyCitation, 3);

			} while (atd == null && counter <= 40);

			check = pageCheck.positiveCheck(driver, "document", "Document");

			// pageCheck.handleFlow(driver, check);

			WebElement pgHeader = null;
			if (commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null
					&& (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/"
						+ "document " + strDocTitle + " is displayed",
						Status.PASS);
			else if (pgHeader != null
					&& ((pgHeader.getText().toLowerCase().contains(strDocTitle
							.toLowerCase()))))
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/"
						+ "document " + strDocTitle + " is displayed",
						Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", "document " + strDocTitle
						+ " is not displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void closeCitationPopup() {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(
				UIMAP_Document.copyCitationPopup, 10);
		WebElement closeCitationPopup = commonLibrary.isExistNegative(
				copyCitationPopup, UIMAP_Document.closeCitationPopup, 10);
		commonLibrary.clickButtonParentWithWait(closeCitationPopup, "Close");

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify global menu
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public void verifyGlobalMenu() {
		WebElement logoProduct = commonLibrary.isExist(
				UIMAP_SearchResult.logoProduct, 20);
		WebElement logoProduct1 = commonLibrary.isExist(
				UIMAP_SearchResult.pgHeaderOption, 20);
		WebElement btnTriangleDown = commonLibrary.isExist(
				UIMAP_SearchResult.btnTriangleDown, 20);
		WebElement btnClientDropDown = commonLibrary.isExist(
				UIMAP_SearchResult.btnClassClient, 20);
		WebElement btnBrowse = commonLibrary.isExist(
				UIMAP_SearchResult.btnIdBrowse, 20);
		WebElement btnProductSwithcer = commonLibrary.isExist(
				UIMAP_SearchResult.btnIdLexisAdvance, 20);
		WebElement searchBox = commonLibrary.isExist(
				UIMAP_SearchResult.txtIdSearch, 20);
		if ((logoProduct != null || logoProduct1 != null)
				&& btnTriangleDown != null && btnClientDropDown != null
				&& btnBrowse != null && btnProductSwithcer != null
				&& searchBox != null) {
			report.updateTestLog(
					"Verifying Global Menu",
					"Following displays in the Global menu :1.Product Logo2.Product Switcher dropdown3.Browse dropdown4.Search Box5.Client dropdown6.Arrow button",
					Status.PASS);
		} else {
			report.updateTestLog("Verifying Global Menu",
					"Global Menu is Not Displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : clearPreFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void clearPreFilter() {

		WebElement btnClassFilter = commonLibrary.isExist(
				UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement btnClassClearFilter = commonLibrary.isExistNegative(
				UIMAP_Home.btnClassClearFilter, 10);
		commonLibrary.clickButtonLogSmallWait(btnClassClearFilter,
				btnClassClearFilter.getText());

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify PDF Formatting
	// # Function Name : PDFVerificationFormatting     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void pdfVerificationFormatting(String FilePath, String Text,
			String Exist) throws IOException {
		PdfReader reader = new PdfReader(FilePath);
		// PrintWriter out = new PrintWriter(new OutputStreamWriter(new
		// FileOutputStream(txt), "UTF-8"));
		// SemTextExtractionStrategy semTextExtractionStrategy = new
		// SemTextExtractionStrategy();

		for (int j = 1; j <= reader.getNumberOfPages(); j++) {
			// String text = PdfTextExtractor.getTextFromPage(reader, j,
			// semTextExtractionStrategy);

		}
		reader.close();
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void verifySearchResultHeader(String strPageHeader) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(
				UIMAP_SearchResult.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(
				UIMAP_SearchResult.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(
				UIMAP_SearchResult.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(
				UIMAP_SearchResult.hdrResult, 5);
		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;

		// strPageHeader=Header.getText();

		if (Header != null
				&& Header.getText().toLowerCase()
						.contains(strPageHeader.toLowerCase()))// ||
																// HeaderSearchResult1!=null
																// &&
																// HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader)
																// ||
																// HeaderSearchResult3!=null
																// &&
																// HeaderSearchResult3.getText().contains(strPageHeader)
																// ||
																// HeaderSearchResult4!=null
																// &&
																// HeaderSearchResult4.getText().toLowerCase().contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed",
					strPageHeader + " is displayed", Status.PASS);
		// else if(HeaderSearchResult!=null &&
		// HeaderSearchResult.getText().contains(strPageHeader) ||
		// HeaderSearchResult1!=null &&
		// HeaderSearchResult1.getText().contains(strPageHeader))

		// report.updateTestLog("Verify "+strPageHeader+" is displayed",
		// strPageHeader+" is displayed", Status.PASS);
		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed",
					strPageHeader + " is not displayed", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clickLinkFromATD
	// # Author : Seetha
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public void clickLinkFromATD(String documenttitle) {
		WebElement aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		WebElement expanded = commonLibrary.isExist(aboutthisdoclink,
				By.tagName("h3"), 10);
		if (expanded != null
				&& !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink,
					By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWait(expand,
					"Expand About This Document");
			commonLibrary.sleep(3000);
		}

		aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);

		List<WebElement> links = commonLibrary.isExistList(aboutthisdoclink,
				UIMAP_Document.links, 10);
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).getText().toLowerCase()
					.contains(documenttitle.toLowerCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWait(links.get(i),
							documenttitle);
					commonLibrary.sleep(20000);
					break;
				} else {
					commonLibrary.clickButtonParentWithWait(links.get(i),
							documenttitle);
					break;
				}
			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page
	// # Function Name : clickEmbedLink
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public void clickEmbededLink(String link) {
		boolean flag = false;
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		WebElement docContent = commonLibrary.isExistNegative(
				UIMAP_Document.docContent, 10);

		List<WebElement> links = commonLibrary.isExistList(docContent,
				UIMAP_Document.embed, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains(link.toLowerCase())) {
				// commonLibrary.click_MouseMove_Action(li, li.getText());
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				// li.click();
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click link " + link, link
					+ " is not clicked.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close filter pop up.
	// # Function Name : closePreFilterPopUp
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void closePreFilterPopUp() {
		WebElement prefilters = commonLibrary.isExist(UIMAP_Home.preFiltersDiv,
				10);

		WebElement btnClosePreFilterPopup = commonLibrary.isExist(prefilters,
				UIMAP_Home.btnClosePreFilterPopup, 10);
		commonLibrary.clickButtonLogSmallWait(btnClosePreFilterPopup,
				"Close PreFilter PopUp");
		WebElement preFiltersDiv = commonLibrary.isExistNegative(
				UIMAP_Home.preFiltersDiv, 10);

		if (preFiltersDiv == null)
			report.updateTestLog("Verify pre-filters pop-up is closed.",
					"Pre-filters pop-up is closed.", Status.PASS);
		else
			report.updateTestLog("Verify pre-filters pop-up is closed.",
					"Pre-filters pop-up is not closed.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void applySearchFilter(String strToolbarMenuName,
			String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");
		int filters = arrCondentType.length;
		if (strCondentTypes.toLowerCase().contains("u.s. federal"))
			filters--;
		if (strCondentTypes.toLowerCase().contains("all states"))
			filters--;
		int count = 0;
		// Boolean blnFlag=false; Modified by Pratik
		WebElement btnClassFilter = commonLibrary.isExist(
				UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter,
						"Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);

		switch (strToolbarMenuName) {

		case "Category": {
			// blnFlag=false;
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(
						UIMAP_Home.divClassCTFilters, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(
							divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagUList,
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								WebElement Checkbox = commonLibrary.isExist(
										item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								count++;
								// blnFlag=true;
								break;
							}

						}
				}

			}

			break;
		}
		case "Jurisdiction": {
			// blnFlag=false;
			// By Court
			for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
				WebElement divClassFederalFilters = commonLibrary.isExist(
						UIMAP_Home.divClassFederalFilters, 20);
				if (divClassFederalFilters != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(
							divClassFederalFilters, UIMAP_Home.lstTagUList, 20);

					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagUList.get(0),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(
										item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}
			if (count < filters) {
				// By circuit
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassFederalFilters = commonLibrary.isExist(
							UIMAP_Home.divClassFederalFilters, 20);
					if (divClassFederalFilters != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassFederalFilters,
										UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(1),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 1st Column
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassStateFilters = commonLibrary.isExist(
							UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassStateFilters,
										UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(0),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 2nd Column
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(
							UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassStateFilters,
										UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(1),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 3rd Column
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(
							UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassStateFilters,
										UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(2),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				commonLibrary.scrollDown();
				// State Filters Footer Links
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassStateFilters = commonLibrary.isExist(
							UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassStateFilters,
										UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(3),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									commonLibrary.scrollDown();
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									commonLibrary.scrollUp();
									break;
								}

							}
					}

				}
			}
			break;
		}
		case "Practice Areas & Topics": {
			// blnFlag=false;
			// Column 1
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassPracticeArea = commonLibrary.isExist(
						UIMAP_Home.divClassPracticeArea, 20);
				if (divClassPracticeArea != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(
							divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagUList.get(0),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(
										item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}

			// column 2
			if (count < filters) {
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassPracticeArea = commonLibrary.isExist(
							UIMAP_Home.divClassPracticeArea, 20);
					if (divClassPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary
								.isExistList(divClassPracticeArea,
										UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary
								.isExistList(lstTagUList.get(1),
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary
											.isExist(item,
													UIMAP_Home.chkTypeCheckbox,
													20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}

			break;
		}
		}
		if (count == filters)
			report.updateTestLog("select check boxes near " + strCondentTypes,
					"check boxes near " + strCondentTypes + " are selected",
					Status.PASS);
		else
			report.updateTestLog("select check boxes near " + strCondentTypes,
					" check boxes near " + strCondentTypes
							+ " are not selected", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void filterMenuSelection(String strMenuName) {

		try {
			WebElement mnuFilterToolBar = commonLibrary.isExist(
					UIMAP_Home.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				List<WebElement> lstButtons = commonLibrary.isExistList(
						mnuFilterToolBar, UIMAP_Home.btnIdFilterMenu, 20);
				if (lstButtons.size() > 0)
					for (WebElement button : lstButtons) {
						if (button.getText().contains(strMenuName)) {
							Capabilities cap = ((RemoteWebDriver) driver)
									.getCapabilities();
							String browsername = cap.getBrowserName();

							if (browsername
									.equalsIgnoreCase("internet explorer"))
								commonLibrary.clickJS(button, strMenuName);
							else
								commonLibrary.clickButtonParentWithWait(button,
										strMenuName);

							break;
						}

					}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate in browser links and
	// perform an action
	// # Function Name : navigateToBrowserLink2
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void navigateToBrowserLink(String strParentMenuName,
			String strFirstSubMenuName, String strSecondSubMenuName,
			String strThridSubMenuName, String strAction, String linktest) {

		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(
					UIMAP_Home.btnIdBrowse, 20);
			if (!(btnIdBrowse.getAttribute("class").contains("selected")))
				commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

			WebElement divIdBrowserMenu = commonLibrary.isExist(
					UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(
						divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(
						lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							blnFirst = true;

							WebElement button1 = commonLibrary.isExistNegative(
									button, UIMAP_Home.btnTagListItems, 10);
							// commonLibrary.ScrollToView(button1);
							if (browsername.contains("internet")) {
								// commonLibrary.highlightElement(button1);
								commonLibrary.clickJS(button1,
										strParentMenuName);
							} else
								commonLibrary.clickButtonParentWithWait(
										button1, strParentMenuName);
							break;
						}

					}
				if (!blnFirst)
					report.updateTestLog("Click on " + strParentMenuName,
							"Not Clicked on " + strParentMenuName, Status.FAIL);
			}
			if (strFirstSubMenuName != "") {
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(
							divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagAside.get(0),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strFirstSubMenuName)) {
								WebElement button1 = commonLibrary
										.isExistNegative(button,
												UIMAP_Home.btnTagListItems, 10);
								blnSecond = true;
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1,
											strFirstSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(
											button1, strFirstSubMenuName);
								break;
							}

						}
					if (!blnSecond)
						report.updateTestLog("Click on " + strFirstSubMenuName,
								"Not Clicked on " + strFirstSubMenuName,
								Status.FAIL);
				}
			}
			if (strSecondSubMenuName != "") {

				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(
							divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagAside.get(1),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strSecondSubMenuName)) {
								WebElement button1 = commonLibrary
										.isExistNegative(button,
												UIMAP_Home.btnTagListItems, 10);
								blnThird = true;
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1,
											strSecondSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(
											button1, strSecondSubMenuName);
								break;
							}

						}
					if (!blnThird)
						report.updateTestLog(
								"Click on " + strSecondSubMenuName,
								"Not Clicked on " + strSecondSubMenuName,
								Status.FAIL);
				}

			}
			if (strThridSubMenuName != ""
					&& !strThridSubMenuName.contains("Actions")) {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(
							divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagAside.get(2),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strThridSubMenuName)) {
								blnFour = true;
								WebElement button1 = commonLibrary
										.isExistNegative(button,
												UIMAP_Home.btnTagListItems, 10);
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1,
											strThridSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(
											button1, strThridSubMenuName);
								break;
							}

						}
					if (!blnFour)
						report.updateTestLog("Click on " + strThridSubMenuName,
								"Not Clicked on " + strThridSubMenuName,
								Status.FAIL);
				}
				divIdBrowserSubMenu1 = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null && strAction != "") {
					List<WebElement> lstTagAside = commonLibrary.isExistList(
							divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagAside.get(3),
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {

							if (button.getText().contains(strAction)) {
								blnFive = true;
								WebElement button1 = commonLibrary
										.isExistNegative(button,
												UIMAP_Home.lnkLinks, 10);
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1, strAction);
								else
									commonLibrary.clickButtonParentWithWait(
											button1, strAction);
								break;
							}

						}
					if (!blnFive) {
						// report.updateTestLog("Click on "+strAction,
						// "Not Clicked on "+strAction, Status.FAIL);
						WebElement divIdBrowserSubMenu4 = commonLibrary
								.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> lstTagAside4 = commonLibrary
								.isExistList(divIdBrowserSubMenu4,
										UIMAP_Home.lstTagAside, 20);
						// List<WebElement> lstTagListItems4 =
						// commonLibrary.isExist_List(lstTagAside4.get(3),UIMAP_Home.lstTagListItems,
						// 20);
						List<WebElement> header = commonLibrary.isExistList(
								lstTagAside4.get(3), UIMAP_Home.header, 20);
						WebElement actionBar = commonLibrary.isExist(
								header.get(1), UIMAP_Home.actionbar, 20);
						WebElement btnActions = commonLibrary.isExist(
								actionBar, UIMAP_Home.btnTypeButton, 20);
						// commonLibrary.highlightElement(btnActions);
						if (browsername.contains("internet")) {
							commonLibrary.clickButtonLogSmallWait(btnActions,
									btnActions.getText());
						} else {
							// commonLibrary.clickButton_Parent_WithWait(btnActions,btnActions.getText());
							commonLibrary.clickButtonLogSmallWait(btnActions,
									btnActions.getText());
						}

						WebElement divIdBrowserSubMenu2 = commonLibrary
								.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> lstTagAside1 = commonLibrary
								.isExistList(divIdBrowserSubMenu2,
										UIMAP_Home.lstTagAside, 20);
						WebElement divClassActionsList = commonLibrary.isExist(
								lstTagAside1.get(3),
								UIMAP_Home.divClassActionsList, 20);
						List<WebElement> lstTagListItems1 = commonLibrary
								.isExistList(divClassActionsList,
										UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)
							for (WebElement button1 : lstTagListItems1) {
								if (button1.getText().contains(linktest)) {
									blnFive = true;
									WebElement actionlink = commonLibrary
											.isExistNegative(button1,
													UIMAP_Home.actionlink, 10);
									if (browsername.contains("internet"))
										commonLibrary
												.clickButtonParentWithWait(
														actionlink, linktest);
									else
										// commonLibrary.clickButton_Parent_WithWait(button1,
										// linktest);
										commonLibrary
												.clickButtonParentWithWait(
														actionlink, linktest);
									break;
								}

							}
						// WebElement linkselect =
						// commonLibrary.isExist(UIMAP_HomePage.getdocumentlink,
						// 20);
						// commonLibrary.clickButton_Parent_WithWait(linkselect,linktest);
					}
				}

			} else if (strThridSubMenuName.contains("Actions")
					|| strAction.contains("Actions")) {
				// Steps to Click on Actions button
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside = commonLibrary.isExistList(
						divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
				List<WebElement> header = commonLibrary.isExistList(
						lstTagAside.get(2), UIMAP_Home.header, 20);
				WebElement actionBar = commonLibrary.isExist(header.get(1),
						UIMAP_Home.actionbar, 20);
				if (actionBar != null) {
					WebElement btnActions = commonLibrary.isExist(actionBar,
							UIMAP_Home.btnTypeButton, 20);
					commonLibrary.clickButtonLogSmallWait(btnActions,
							btnActions.getText());
				}

				// Steps to click on links from action list dropdown
				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(
						UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside1 = commonLibrary.isExistList(
						divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary
						.isExist(lstTagAside1.get(2),
								UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(
						divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(linktest)) {
							blnFive = true;
							WebElement actionlink = commonLibrary.isExist(
									button, UIMAP_Home.actionlink, 10);
							if (actionlink == null)
								actionlink = commonLibrary.isExist(button,
										UIMAP_Home.actionButtonTopics, 10);
							commonLibrary.clickJS(actionlink, linktest);
							break;
						}

					}

				WebElement replace = commonLibrary.isExistNegative(
						UIMAP_Home.deletePopUp, 10);
				if (replace != null) {
					WebElement contOK = commonLibrary.isExist(replace,
							UIMAP_Home.deleteDeletePopUp, 10);
					commonLibrary.clickButtonParentWithWait(contOK, "Continue");
				}

				if (!blnFive)
					report.updateTestLog("Click on " + strAction,
							"Not Clicked on " + strAction, Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void login() {

		String strUserName = dataTable.getData("General_Data", "Username");
		String strPassword = dataTable.getData("General_Data", "Password");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.
		driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		// Enter User Name
		if (eltUserName != null) {

			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
				if (!eltUserName.getAttribute("value").equals(strUserName)) {
					throw new FrameworkException("Verify UserName is entered",
							"UserName is not entered");
				}
			}

			// Enter Password
			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
				if (eltPassword.getAttribute("value").equals("")) {
					throw new FrameworkException("Verify Password is entered",
							"Password is not entered");
				}
			}

			// Click on signIn button
			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}

			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);

			commonLibrary.sleep(10000);
			if (driver.getPageSource().contains("Login Attempt Limit Exceeded"))
				throw new FrameworkException("Error during sign in",
						"Login Attempt Limit Exceeded for user id "
								+ strUserName);

			// CLICK ON <<<GET STARTED>>>

			WebElement btnValGetStarted = commonLibrary.isExistNegative(
					UIMAP_SignIn.btnValGetStarted, 10);
			if (btnValGetStarted != null)
				commonLibrary.clickButtonParentWithWait(btnValGetStarted,
						"Get Started");

			// wait till home page is displayed
			WebElement eltSearchbox = commonLibrary.isExistNegative(
					UIMAP_Home.txtIdSearch, 5);
			int counter = 0;
			do {
				counter = counter + 1;
				eltSearchbox = commonLibrary.isExistNegative(
						UIMAP_Home.txtIdSearch, 3);
				if (eltSearchbox == null)
					commonLibrary.sleep(5000);
			} while (eltSearchbox == null && counter <= 20);

			// Code to close Latest Updates dialog box
			WebElement closeDialog = commonLibrary.isExistNegative(
					UIMAP_Home.closeDialog, 5);
			if (closeDialog != null) {
				commonLibrary.clickButtonParentWithWaitJS(closeDialog,
						"Close Popup");
			}

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
			// if (browsername.contains("chrome"))
			// DocumentState(driver);

			// if (browsername.contains("ie8")) {
			// sleepWait(1000);
			// PageCheck.checkAlert(driver);
			// }
			// final PageCheck pageCheck = new PageCheck(scriptHelper);
			// check = pageCheck.PositiveCheck(driver, "More", "Home Page");

			// pageCheck.handleFlow(driver, check);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : login_Another     
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void login_Another() {

		String strUserName = dataTable.getData("General_Data", "Username1");
		String strPassword = dataTable.getData("General_Data", "Password1");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.
		// WebElement
		// btnMore=commonLibrary.isExist_Negative(UIMAP_Home.btnTitleMore, 5);
		// if(btnMore!=null)
		// {
		// signinsignout=commonLibrary.logout();
		// signinsignout.invokeApplication();
		// }
		// try
		// {
		// commonLibrary.sleep(1000);
		// }
		// catch (Exception e)
		// {
		// System.out.println(e.toString());
		// throw new FrameworkException("Exception", e.toString());
		//
		// }
		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {
			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}

			WebElement eltSearchbox = commonLibrary.isExistNegative(
					UIMAP_Home.txtIdSearch, 7);
			int counter = 0;
			do {
				counter = counter + 1;
				eltSearchbox = commonLibrary.isExistNegative(
						UIMAP_Home.txtIdSearch, 5);
				if (eltSearchbox == null)
					commonLibrary.sleep(5000);
			} while (eltSearchbox == null && counter <= 25);

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);

			// Code to close Latest Updates dialog box
			WebElement closeDialog = commonLibrary.isExistNegative(
					UIMAP_Home.closeDialog, 5);
			if (closeDialog != null) {
				commonLibrary.clickButtonParentWithWaitJS(closeDialog,
						"Close Popup");
			}
		}

	}

	public int selectCondentType(String strCondentType) {
		Boolean blnFlag = false;
		try {
			WebElement eltResultHeader = commonLibrary.isExistNegative(
					UIMAP_SearchResult.eltResultHeader, 10);
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement btnFewerCat = commonLibrary.isExistNegative(
					UIMAP_SearchResult.btnFewerCat, 10);
			if ((btnFewerCat != null)
					&& (btnFewerCat.getText().contains("Show more"))) {
				if (browsername.contains("internet"))
					commonLibrary.clickLinkWithWebElementWithWaitJS(
							btnFewerCat, "More Categories");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat,
							"More Categories");
				commonLibrary.sleep(50000);
			}
			WebElement ulCondentSwitcher = commonLibrary.isExist(
					UIMAP_SearchResult.ulCondentSwitcher, 10);
			if (ulCondentSwitcher == null)
				ulCondentSwitcher = commonLibrary.isExist(
						UIMAP_SearchResult.latContentType, 10);
			if (ulCondentSwitcher != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(
						ulCondentSwitcher, By.tagName("li"), 20);
				if (OListItems.size() > 0) {
					for (WebElement element : OListItems) {
						WebElement btnCondentType = commonLibrary.isExist(
								element, By.tagName("button"), 20);
						if (btnCondentType == null)
							btnCondentType = commonLibrary.isExist(element,
									By.tagName("span"), 20);
						if (btnCondentType.getText().toString().toLowerCase()
								.contains(strCondentType.toLowerCase())) {

							if (browsername.contains("internet"))
								commonLibrary.clickLinkWithWebElementWithWait(
										btnCondentType,
										btnCondentType.getText());
							else
								commonLibrary.clickLinkWithWebElementWithWait(
										btnCondentType,
										btnCondentType.getText());
							try {
								String loadProp = properties
										.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary
										.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(10000);
									loader = commonLibrary.isExistNegative(
											By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 15);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
							commonLibrary.sleep(1000);
							blnFlag = true;
							break;

						}
					}
				}
			}
			if (!blnFlag) {
				report.updateTestLog("Click on " + strCondentType,
						"Not Click on " + strCondentType, Status.FAIL);
				return 0;
			} else {
				commonLibrary.sleep(3000);
				pageCheck.ajaxElementCheck(driver,
						properties.getProperty("xSpinner"));
				ulCondentSwitcher = commonLibrary.isExist(
						UIMAP_SearchResult.latContentType, 10);
				if (ulCondentSwitcher == null) {
					WebElement eltResultHeaderNew = commonLibrary
							.isExistNegative(
									UIMAP_SearchResult.eltResultHeader, 10);

					int counter = 0;
					do {
						counter = counter + 1;
						eltResultHeaderNew = commonLibrary.isExistNegative(
								UIMAP_SearchResult.eltResultHeader, 10);
						if (eltResultHeader == null)
							commonLibrary.sleep(10000);

					} while ((eltResultHeader.equals(eltResultHeaderNew))
							&& (counter < 30));
				}
				return 1;
			}
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	public int search(String searchTerm, Boolean clearFilter, int i) {

		try {

			// Clear the pre filters if clearFilter is true
			if (clearFilter)
				clearFilter(clearFilter);

			// Enter search term
			WebElement eltSearchbox = commonLibrary.isExist(
					UIMAP_Home.txtIdSearch, 20);
			commonLibrary.setDataInTextBox(eltSearchbox, searchTerm,
					"SearchTerm");
			commonLibrary.sleep(30000);

			// Click on search button
			WebElement eltSearchbutton = commonLibrary.isExist(
					UIMAP_Home.btnIdSearch, 20);
			commonLibrary.clickSearchButton(eltSearchbutton, "Search");
			return 1;

		}

		catch (TimeoutException ex1) {

			// Refresh the page if exception occurs
			for (int loop = 0; loop < 3; loop++) {
				driver.navigate().refresh();

				// recursive call for search
				if (search(searchTerm, clearFilter, i) == 1) {
					return 1;
				}

			}

		} catch (StaleElementReferenceException ex) {

			for (int loop = 0; loop < 3; loop++) {

				// Refresh the page if exception occurs
				driver.navigate().refresh();

				// recursive call for search
				if (search(searchTerm, clearFilter, i) == 1) {
					return 1;
				}
			}

		}

		catch (Exception ex2) {
			System.out.println("Exception in while performing search :"
					+ ex2.getMessage());

		}
		return 1;

	}

	public void clearFilter(Boolean isClear) {
		// WebElement filterIdCount =
		// commonLibrary.isExist_Negative(UIMAP_Home.FilterIdCount, 2);
		WebElement btnClassFilter = commonLibrary.isExist(
				UIMAP_Home.btnClassFilter, 20);
		if (btnClassFilter != null) {
			if (!btnClassFilter.getText().contains("Everything")) {
				commonLibrary.clickButtonParentWithWait(btnClassFilter,
						"Filter");
				WebElement btnClassClearFilter = commonLibrary.isExist(
						UIMAP_Home.btnClassClearFilter, 20);
				if (btnClassClearFilter != null) {
					commonLibrary.clickLinkWithWebElementWithWait(
							btnClassClearFilter, "Clear");
				}
			}

		}
	}

	public void documentState(WebDriver driver) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String state = null;
			boolean temp = false;
			do {
				commonLibrary.sleep(6000);
				state = executor.executeScript("return document.readyState;")
						.toString();
				commonLibrary.sleep(5000);
				System.out.println("Waiting for Page to load. Current State: "
						+ state);
				if (state.contains("loaded"))
					temp = true;
				else if (state.contains("complete"))
					temp = true;
				else
					temp = false;
				commonLibrary.sleep(1000);
			} while (!temp);
		} catch (WebDriverException e) {
			System.out
					.println("JSError Recorded: commonLibrary Time 10 Seconds");

			commonLibrary.sleep(1000);

			documentState(driver);
		} catch (Exception e) {
			System.out
					.println("JSError Recorded: commonLibrary Time 10 Seconds");

			commonLibrary.sleep(1000);

			documentState(driver);
		}
		return;
	}

	public static void sleepWait(long timeToWait) {

		sleep(timeToWait);
		return;

	}

	public void clickJumpTo() {
		int iteration = 0;
		try {
			// WebElement mobileToolbar =
			// commonLibrary.isExist(UIMAP_Document.mobileToolbar, 10);
			WebElement rightContainer = commonLibrary.isExistNegative(
					UIMAP_Document.rightContainer, 10);
			WebElement jumpto = commonLibrary.isExistNegative(rightContainer,
					UIMAP_Document.jumpto, 10);
			commonLibrary.windowFocus();
			commonLibrary.highlightElement(jumpto);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", jumpto);
			executor.executeScript("arguments[0].click();", jumpto);
			report.updateTestLog("Click on button Jumpto",
					"Clicked on button Jumpto", Status.PASS);

			WebElement sectionContainer = commonLibrary.isExistNegative(
					rightContainer, UIMAP_Document.sectionContainer, 10);
			if (sectionContainer != null && !sectionContainer.isDisplayed()) {
				iteration = iteration + 1;
				clickJumpTo();
				if (iteration == 5)
					return;

				System.out.println(iteration);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public int selectPostFilter(String header, String filter) {

		try {

			boolean Flag = false;

			if (!(header.equals(" ") && filter.equals(" "))) {

				List<WebElement> eltFilter = null;
				int i = 0, j = 0;

				WebElement filterContainer = commonLibrary.isExistNegative(
						UIMAP_SearchResult.filterContainer, 10);
				List<WebElement> filterHeader = commonLibrary.isExistList(
						filterContainer, UIMAP_SearchResult.filterHeader, 10);
				List<WebElement> suplementalFilters = commonLibrary
						.isExistList(filterContainer,
								UIMAP_SearchResult.supplementalFilters, 10);

				for (i = 0; i < filterHeader.size(); i++) {

					if (filterHeader.get(i).getText().contains("Timeline"))
						j = 1;

					// expand the filter header if its collapsed
					if (filterHeader.get(i).getText().toUpperCase()
							.contains(header.toUpperCase())) {

						if (filterHeader.get(i).getAttribute("class")
								.contains("collapsed")) {

							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(
										filterHeader.get(i), header);
							else
								commonLibrary.clickLinkWithWebElementWithWait(
										filterHeader.get(i), header);

							report.updateTestLog("Expanding Filter Header: "
									+ header, header
									+ " filter Header Expanded.", Status.DONE);
						}

						// click on More button
						List<WebElement> moreOptions = commonLibrary
								.isExistList(suplementalFilters.get(i - j),
										UIMAP_SearchResult.filterMore, 10);
						for (WebElement button : moreOptions) {

							if (button.getText().toLowerCase().contains("more")) {

								if (browsername.toLowerCase().contains(
										"internet"))
									commonLibrary.clickButtonLogSmallWait(
											button, "More");
								else
									commonLibrary.clickButtonLogSmallWait(
											button, "More");
							}
						}

						// Select the post filter
						List<WebElement> filters = commonLibrary.isExistList(
								suplementalFilters.get(i - j),
								UIMAP_SearchResult.eltFilterList, 20);
						for (WebElement item : filters) {

							WebElement span = commonLibrary.isExistNegative(
									item, UIMAP_SearchResult.tagSpan, 10);
							if (span.getText().equals(filter)) {

								if (browsername.toLowerCase().contains(
										"internet"))
									commonLibrary.clickButtonParentWithWaitJS(
											item, filter);
								else
									commonLibrary.clickButtonParentWithWait(
											item, filter);

								report.updateTestLog("Selecting Filter: "
										+ filter, filter + " Filter Selected.",
										Status.DONE);
								Flag = true;
								break;
							}
						}
					}

					if (Flag)
						break;
				}

				// Verification Point: Post filter selection
				if (!Flag) {

					eltFilter = commonLibrary.isExistList(
							UIMAP_SearchResult.eltFilterList1, 10);

					for (i = 0; i < eltFilter.size(); i++) {

						if (eltFilter.get(i).getText().toUpperCase()
								.contains(filter.toUpperCase())) {

							commonLibrary.clickLinkWithWebElementWithWait(
									eltFilter.get(i), eltFilter.get(i)
											.getText());
							report.updateTestLog("Selecting Filter: " + filter,
									"Required Filter Selected.", Status.DONE);
							Flag = true;
							break;
						}
					}
				}

			} else {
				report.updateTestLog("Selecting Filter: " + filter,
						"No Filter Selected.", Status.DONE);
				Flag = true;
			}
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
					10);
			int count = 0;
			do {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				count++;
				commonLibrary.sleep(10000);
			} while (btnMore == null && count < 15);
			if (Flag)
				return 1;
			else
				return 0;

		} catch (StaleElementReferenceException e1) {
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of attempts for each
	// question in ICW
	// # Function Name : SetNumberOfAttempts     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public void setNumberOfAttempts(String number) {
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings,
				100);
		if (setting == null || !setting.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		}

		if (setting != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(setting, "Setting");
			else
				commonLibrary.clickLinkWithWebElementWithWait(setting,
						"Settings");
		}

		WebElement icwProfSettings = commonLibrary.isExistNegative(
				UIMAP_Home.icwProfSettings, 10);
		if (icwProfSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(icwProfSettings,
					"Interactive Citation Workstation Prof Settings");
		WebElement selectAttempts = commonLibrary.isExistNegative(
				UIMAP_Home.selectAttempts, 10);
		commonLibrary.selectFromListOption(selectAttempts, number);

		WebElement submit = commonLibrary.isExistNegative(
				UIMAP_Settings.btnIdsubmitSettChange, 10);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");

		} else {
			WebElement cancel = commonLibrary.isExistNegative(
					UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");

			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot of current page
	// # Function Name : TakeScreenShot     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters
					.getInstance();
			String TestPath = frameworkParameters.getRelativePath()
					+ Util.getFileSeparator();

			String strScreenshot = strTCName
					+ commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot
					+ ".jpg";

			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep,
					"Perform Manual Verification for this step. screenshot is saved in "
							+ strDestination + "", Status.WARNING);
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Pratik
	// # Date Created : June'2015
	// #*****************************************************************************************************************************

	public void clickProductLogo() {
		WebElement currentProduct = commonLibrary.isExist(
				UIMAP_Home.CurrentProduct, 20);

		if (currentProduct != null)
			commonLibrary.clickLinkWithWebElement(currentProduct,
					"Current Product Logo");
		else
			report.updateTestLog("Click on product logo",
					"Product Logo is not clicked", Status.FAIL);

		try {
			WebElement currProdNew = commonLibrary.isExist(
					UIMAP_Home.CurrentProduct, 20);
			int count = 0;
			do {
				commonLibrary.sleep(50000);
				currProdNew = commonLibrary.isExist(UIMAP_Home.CurrentProduct,
						20);
				count++;
			} while (currProdNew.equals(currentProduct) && count < 20);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create folder and save
	// # Function Name : createFolderAndSave     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public void createFolderAndSave(String folderName, boolean banner,
			String strTCName) {
		// CLICK ON <<<ADD TO FOLDER>>>
		try {
			WebElement addtoFolder = commonLibrary.isExist(
					UIMAP_CounselBenchmarking.addFolder, 10);
			if (addtoFolder != null)
				commonLibrary.clickButtonParentWithWait(addtoFolder,
						"Add To Folder");

			commonLibrary.sleep(8000);

			// CLICK ON <<<CHOOSE A FOLDER>>>
			WebElement chooseFolder = commonLibrary.isExistNegative(
					UIMAP_CounselBenchmarking.chooseFolder, 10);

			if (chooseFolder == null) {
				addtoFolder = commonLibrary.isExist(
						UIMAP_CounselBenchmarking.addFolder, 10);
				if (addtoFolder != null) {
					commonLibrary.clickButtonParentWithWait(addtoFolder,
							"Add To Folder");
					commonLibrary.sleep(1000);
				}
				chooseFolder = commonLibrary.isExist(
						UIMAP_CounselBenchmarking.chooseFolder, 10);

			}

			if (chooseFolder != null) {
				report.updateTestLog(
						"Verify save instructor dashboard report to dropdown is displayed",
						"Save instructor dashboard report to dropdown is displayed",
						Status.PASS);
				commonLibrary.clickButtonParentWithWait(chooseFolder,
						"Choose Folder");
				commonLibrary.sleep(3000);
			}
			pageCheck.ajaxWait(driver);
			commonLibrary.sleep(8000);
		} catch (Exception e) {
			try {
				WebElement chooseFolder = commonLibrary.isExistNegative(
						UIMAP_CounselBenchmarking.chooseFolder, 10);
				if (chooseFolder == null) {
					WebElement addtoFolder = commonLibrary.isExistNegative(
							UIMAP_CounselBenchmarking.addFolder, 10);
					if (addtoFolder != null)
						commonLibrary.clickButtonParentWithWait(addtoFolder,
								"Add To Folder");
					chooseFolder = commonLibrary.isExistNegative(
							UIMAP_CounselBenchmarking.chooseFolder, 10);
				}
				commonLibrary.sleep(1000);
				System.out.println("Exception Caught");
				chooseFolder = commonLibrary.isExistNegative(
						UIMAP_CounselBenchmarking.chooseFolder, 10);
				if (chooseFolder != null) {
					report.updateTestLog(
							"Verify save instructor dashboard report to dropdown is displayed",
							"Save instructor dashboard report to dropdown is displayed",
							Status.PASS);
					commonLibrary.clickMethod(chooseFolder, "Choose Folder");
					commonLibrary.sleep(3000);
				}
				pageCheck.ajaxWait(driver);
				commonLibrary.sleep(8000);
			} catch (Exception e1) {
				WebElement chooseFolder = commonLibrary.isExist(
						UIMAP_CounselBenchmarking.chooseFolder, 10);
				if (chooseFolder == null) {
					WebElement addtoFolder = commonLibrary.isExist(
							UIMAP_CounselBenchmarking.addFolder, 10);
					if (addtoFolder != null)
						commonLibrary.clickButtonParentWithWait(addtoFolder,
								"Add To Folder");
					chooseFolder = commonLibrary.isExist(
							UIMAP_CounselBenchmarking.chooseFolder, 10);
				}
				commonLibrary.sleep(1000);
				System.out.println("Exception Caught2");
				chooseFolder = commonLibrary.isExist(
						UIMAP_CounselBenchmarking.chooseFolder, 10);
				if (chooseFolder != null) {
					report.updateTestLog(
							"Verify save instructor dashboard report to dropdown is displayed",
							"Save instructor dashboard report to dropdown is displayed",
							Status.PASS);
					commonLibrary.clickMethod(chooseFolder, "Choose Folder");
					commonLibrary.sleep(3000);
				}
				pageCheck.ajaxWait(driver);
				commonLibrary.sleep(8000);
			}
		}

		WebElement chooseFolder = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.chooseFolder, 10);
		if (chooseFolder != null) {
			report.updateTestLog(
					"Verify save instructor dashboard report to dropdown is displayed",
					"Save instructor dashboard report to dropdown is displayed",
					Status.PASS);
			commonLibrary.clickMethod(chooseFolder, "Choose Folder");
			commonLibrary.sleep(3000);
		}

		// CLICK ON <<<CREATE NEW FOLDER>>>

		WebElement createNewFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createNewFolder, 10);
		if (createNewFolder != null)
			commonLibrary.clickButtonParentWithWait(createNewFolder,
					"Create New Folder");

		// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

		WebElement folderNam = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.folderName, 10);
		if (folderNam != null)
			commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

		// CLICK ON <<<CREATE>>>
		WebElement create = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createFolder, 10);
		if (create != null)
			commonLibrary.clickButtonParentWithWait(create, "Create");
		if (banner) {
			this.takeScreenShot(strTCName,
					"Verify banner message as folder is successfully created with dismiss button");
		}
		create = commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder,
				10);
		WebElement createNew = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.createFolder, 10);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(7000);
				createNew = commonLibrary.isExistNegative(
						UIMAP_CounselBenchmarking.createFolder, 10);
				count++;
				System.out.println("Waiting" + count);
			} while (create.equals(createNew) && count < 80);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		if (banner) {
			this.takeScreenShot(strTCName,
					"Verify banner message as item is being saved to "
							+ folderName + "");
		}
		// saveFolder =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.saveFolder, 10);
		WebElement saveNew = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		count = 0;
		try {
			do {
				commonLibrary.sleep(7000);
				saveNew = commonLibrary.isExistNegative(
						UIMAP_CounselBenchmarking.saveFolder, 10);
				count++;
				System.out.println("Waiting" + count);
			} while (saveFolder == saveNew && count < 40);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Alerts page
	// # Function Name : navigateToFolders
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void navigateToFolders() {

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		if (btnMore != null) {
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		}

		commonLibrary.sleep(3000);
		WebElement alert = commonLibrary.isExist(UIMAP_Home.folderBtn);

		if (alert == null) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			alert = commonLibrary.isExist(UIMAP_Home.folderBtn);
		}

		if (alert != null) {
			commonLibrary.clickLinkWithWebElementWithWait(alert, "Folders");
		} else {
			report.updateTestLog("Click on folders",
					"Folders button is not clicked", Status.FAIL);
		}

		WebElement foldersTreeCont = commonLibrary.isExistNegative(
				UIMAP_WorkFolders.foldersTreeCont, 5);
		int count = 0;
		do {
			commonLibrary.sleep(5000);
			count++;
			foldersTreeCont = commonLibrary.isExistNegative(
					UIMAP_WorkFolders.foldersTreeCont, 5);

		} while (foldersTreeCont == null && count < 25);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle     
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public void clickPdfTitle(String title) {
		List<WebElement> lnkTitle = commonLibrary.isExistList(
				UIMAP_WorkFolders.pdfLink, 10);
		int countold = driver.getWindowHandles().size();
		for (WebElement item : lnkTitle) {
			if (item.getText().toLowerCase().contains(title.toLowerCase())) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(item, title);
				else
					commonLibrary.clickButtonParentWithWait(item, title);
				break;
			}
		}
		int i = 0;
		int count = driver.getWindowHandles().size();
		do {
			commonLibrary.sleep(5000);
			count = driver.getWindowHandles().size();
			i++;
		} while (count == countold && i < 20);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis® Interactive
	// Citation Workstation
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigatetoICW     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #************************************************************************************************
	public void navigatetoICW() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(
				UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance,
					"Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(
				UIMAP_Home.btnActionICW, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking,
					"Lexis® Interactive Citation Workstation");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking,
					"Lexis® Interactive Citation Workstation");
		commonLibrary.sleep(1000);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clicklinkinPods(String podName, String linkName) {

		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(
				UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					List<WebElement> lstlinks = commonLibrary.isExistList(
							button,
							UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					for (WebElement link1 : lstlinks) {

						String exerName = "";
						if (link1.getText().contains(":"))
							exerName = link1.getText().split(":")[1].trim();
						else
							exerName = link1.getText();
						if (linkName.toLowerCase().contains(
								exerName.toLowerCase())) {

							commonLibrary.clickLinkWithWebElementWithWait(
									link1, linkName);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag) {
					break;
				}
			}
		if (blnFlag) {
			report.updateTestLog("Verify link " + linkName + " in pod "
					+ podName, "link " + linkName + " in pod " + podName
					+ " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify link " + linkName + " in pod "
					+ podName, "link " + linkName + " in pod " + podName
					+ " is not displayed", Status.FAIL);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the status of a link in ICW
	// pods
	// # Function Name : verifylinkinPodStatus   
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public boolean verifylinkinPodStatus(String podName, String linkName,
			String status) {
		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(
				UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					List<WebElement> lstlinks = commonLibrary.isExistList(
							button,
							UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					for (WebElement link1 : lstlinks) {
						String exerName = "";
						if (link1.getText().contains(":"))
							exerName = link1.getText().split(":")[1].trim();
						else
							exerName = link1.getText();
						if (exerName.toLowerCase().equals(
								linkName.toLowerCase())) {
							WebElement row = commonLibrary
									.getParentElement(link1);
							row = commonLibrary.getParentElement(row);
							if (row.getText().toLowerCase()
									.contains(status.toLowerCase())) {
								blnFlag = true;
								break;
							}
						}
					}
				}
				if (blnFlag) {
					break;
				}
			}
		if (blnFlag) {

			return true;
		} else {
			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application with third
	// different credentials.
	// # Function Name : loginThird     
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void loginThird() {

		String strUserName = dataTable.getData("General_Data", "Username2");
		String strPassword = dataTable.getData("General_Data", "Password2");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.
		// WebElement
		// btnMore=commonLibrary.isExist_Negative(UIMAP_Home.btnTitleMore, 5);
		// if(btnMore!=null)
		// {
		// signinsignout=commonLibrary.logout();
		// signinsignout.invokeApplication();
		// }
		// try
		// {
		// commonLibrary.sleep(1000);
		// }
		// catch (Exception e)
		// {
		// System.out.println(e.toString());
		// throw new FrameworkException("Exception", e.toString());
		//
		// }
		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {
			/*
			 * try { commonLibrary.sleep(7000); } catch (Exception e) {
			 * System.out.println(e.toString()); throw new
			 * FrameworkException("Exception", e.toString());
			 * 
			 * }
			 */
			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}

			WebElement eltSearchbox = commonLibrary.isExist(
					UIMAP_Home.txtIdSearch, 20);
			int counter = 0;
			do {
				counter = counter + 1;
				eltSearchbox = commonLibrary
						.isExist(UIMAP_Home.txtIdSearch, 20);
				if (eltSearchbox == null)
					commonLibrary.sleep(5000);
			} while (eltSearchbox == null && counter <= 40);
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void loginPro1() {

		String strUserName = dataTable.getData("General_Data", "Username1");
		String strPassword = dataTable.getData("General_Data", "Password1");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.

		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
			documentState(driver);

			if (browsername.contains("internet")) {
				sleepWait(20000);
				// PageCheck.checkAlert(driver);
			}

			WebElement currProd = commonLibrary.isExistNegative(
					UIMAP_Home.currProd, 5);
			int counter = 0;
			do {
				counter = counter + 1;
				currProd = commonLibrary
						.isExistNegative(UIMAP_Home.currProd, 5);
				if (currProd == null)
					commonLibrary.sleep(5000);
			} while (currProd == null && counter <= 30);

			// final PageCheck pageCheck = new PageCheck(scriptHelper);
			// check = pageCheck.PositiveCheck(driver, "landingpage",
			// "Home Page");
			//
			// pageCheck.handleFlow(driver, check);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void loginPro2() {

		String strUserName = dataTable.getData("General_Data", "Username2");
		String strPassword = dataTable.getData("General_Data", "Password2");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.

		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
			documentState(driver);

			if (browsername.contains("internet")) {
				sleepWait(10000);
				PageCheck.checkAlert(driver);
			}

			WebElement eltSearchbox = commonLibrary.isExist(
					UIMAP_Home.txtIdSearch, 20);
			int counter = 0;
			do {
				counter = counter + 1;
				eltSearchbox = commonLibrary
						.isExist(UIMAP_Home.txtIdSearch, 20);
				if (eltSearchbox == null)
					commonLibrary.sleep(5000);
			} while (eltSearchbox == null && counter <= 40);

			// final PageCheck pageCheck = new PageCheck(scriptHelper);
			// check = pageCheck.PositiveCheck(driver, "landingpage",
			// "Home Page");
			//
			// pageCheck.handleFlow(driver, check);

		}

	}

	public void shareContactSelectContact(String strContactName) {

		try {
			WebElement AddedContact = commonLibrary.isExistNegative(
					UIMAP_Document.ulIdSelecteduserListTableAddtoFolder, 10);
			for (int k = 0; k < 3; k++) {

				if (AddedContact == null) {
					// if(browsername.contains("internet"))
					// {

					WebElement txtShareContacts = commonLibrary.isExist(
							UIMAP_Document.txtShareContacts, 10);
					if (txtShareContacts != null)
						commonLibrary
								.setDataInTextBox(
										UIMAP_Document.txtShareContacts,
										strContactName);
					commonLibrary.sleep(2000);

					// SELECT THE <<<BECKER>>>
					boolean blnFlag = false;
					// try
					// {
					//
					commonLibrary.sleep(3000);
					WebElement cboSharingList = commonLibrary.isExist(
							UIMAP_Document.cboSharingList, 10);
					List<WebElement> li = commonLibrary.isExistList(
							cboSharingList, By.tagName("li"), 20);
					for (WebElement item : li) {
						if (item.getText().trim()
								.equalsIgnoreCase(strContactName)) {
							commonLibrary.clickJS(item);
							blnFlag = true;
							break;
						}
					}
					if (!(blnFlag)) {
						WebElement txtShareContacts1 = commonLibrary.isExist(
								UIMAP_Document.txtShareContacts, 10);
						for (int j = 1; j <= 3; j++) {
							Actions action = new Actions(driver);
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE)
									.perform();
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE)
									.perform();
							action.sendKeys(
									txtShareContacts1,
									strContactName.substring(strContactName
											.length() - 2)).perform();
							commonLibrary.sleep(4000);
							WebElement cboSharingList1 = commonLibrary.isExist(
									UIMAP_Document.cboSharingList, 10);
							List<WebElement> li1 = commonLibrary.isExistList(
									cboSharingList1, By.tagName("li"), 20);
							for (WebElement item : li1) {
								if (item.getText().trim()
										.equalsIgnoreCase(strContactName)) {
									commonLibrary.clickJS(item);
									blnFlag = true;
									break;
								}
							}
							if (blnFlag)
								break;
						}
					}

					// Click the "Add" button
					WebElement btnAddToContact = commonLibrary.isExist(
							UIMAP_Document.btnAddToContact, 10);
					if (btnAddToContact != null) {
						if (browsername.contains("internet"))
							commonLibrary.clickJS(btnAddToContact, "Add");
						else
							commonLibrary.clickButtonParentWithWait(
									btnAddToContact, "Add");

					}

				}

				// if(AddedContact==null)
				// report.updateTestLog("Add Contact Name ", "Contact Name"+
				// strVal +" is not added", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void selectTabsAddToFolderWindow(String strTabName) {

		try {
			switch (strTabName) {
			case "Selected Documents": {
				WebElement tabSelectedDocuments = commonLibrary.isExist(
						UIMAP_Document.tabSelectedDocuments, 10);
				if (tabSelectedDocuments != null)
					commonLibrary.clickButtonParentWithWait(
							tabSelectedDocuments, "Selected Documents");
				break;
			}
			case "Save Options": {
				WebElement tabSaveOptions = commonLibrary.isExist(
						UIMAP_Document.tabSaveOptions, 10);
				if (tabSaveOptions != null)
					commonLibrary.clickButtonParentWithWait(tabSaveOptions,
							"Save Options");
				break;
			}
			case "Share With Others": {
				WebElement tabShareWithOthers = commonLibrary.isExist(
						UIMAP_Document.tabShareWithOthers1, 10);
				if (tabShareWithOthers != null) {
					if (browsername.equalsIgnoreCase("internet explorer"))
						commonLibrary.clickJS(tabShareWithOthers);
					else
						commonLibrary.clickButtonParentWithWait(
								tabShareWithOthers, "Share With Others");
				}
				break;
			}
			default: {
				WebElement tabSaveOptions = commonLibrary.isExist(
						UIMAP_Document.tabSaveOptions, 10);
				if (tabSaveOptions != null)
					commonLibrary.clickButtonParentWithWait(tabSaveOptions,
							"Save Options");
				break;
			}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait
	// # Function Name : sleep()     
	// # Author : Uma
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public static void sleep(long count) {
		count = count * 40000;
		for (int i = 0; i <= count; i++) {
			i = i + 1;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on more drop down in Alert
	// Results page
	// # Function Name : clickAlertMoreOption()     
	// # Author : Uma
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public void clickAlertMoreOption() {
		WebElement moreDropdown = commonLibrary
				.isExist(UIMAP_AlertsSearchResults.moreDropdown);
		WebElement moreButton = commonLibrary.isExist(moreDropdown,
				By.tagName("button"), 10);
		commonLibrary.highlightElement(moreButton);
		if (moreButton.getAttribute("class").contains("collapsed"))
			commonLibrary.clickJS(moreButton, "More");

		WebElement overflowFrom = commonLibrary
				.isExist(UIMAP_AlertsSearchResults.overflowFrom);
		if (overflowFrom == null) {
			commonLibrary.sleep(1000);
			moreDropdown = commonLibrary
					.isExist(UIMAP_AlertsSearchResults.moreDropdown);
			WebElement moreButton1 = commonLibrary.isExist(moreDropdown,
					By.tagName("button"), 10);
			commonLibrary.highlightElement(moreButton1);
			commonLibrary.clickJS(moreButton1, "More");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void loginICW() {

		String strUserName = dataTable.getData("General_Data", "Username");
		String strPassword = dataTable.getData("General_Data", "Password");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.

		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);

			WebElement currProd = commonLibrary.isExistNegative(
					UIMAP_Home.currProd, 5);
			int counter = 0;
			do {
				counter = counter + 1;
				currProd = commonLibrary
						.isExistNegative(UIMAP_Home.currProd, 5);
				if (currProd == null)
					commonLibrary.sleep(5000);
			} while (currProd == null && counter <= 30);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Select Value
	// # Function Name : clickActionSelectValue    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public void clickActionSelectValue(String strAction) {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(
					UIMAP_SearchResult.divider, 20);
			if (divider == null)
				divider = commonLibrary
						.isExist(UIMAP_SearchResult.divider1, 20);
			WebElement hdrresult = commonLibrary.isExist(divider,
					UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(
					UIMAP_SearchResult.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(
				UIMAP_SearchResult.divAction, 20);
		if (divAction != null) {

			if (commonLibrary.selectFromListButton(divAction, strAction)) {
				report.updateTestLog("Click option " + strAction + "", ""
						+ strAction + " is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click option " + strAction + "", ""
						+ strAction + " is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click option " + strAction + "", ""
					+ strAction + " is not clicked", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public void addToFolder(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder,
						"Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder,
						"Choose Folder");

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder,
						"Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(
					UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(
						UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(
					UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void selectDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary
				.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0),
				By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i),
					By.cssSelector("a[data-action='title']"), 10);
			if (lnkTitle == null)
				lnkTitle = commonLibrary.isExistNegative(LList.get(i),
						By.tagName("a"), 10);
			strDocTitle = lnkTitle.getText();
			if (strDocTitle.trim().contains(DocName.trim())) {
				WebElement lstchkBox = commonLibrary.isExist(LList.get(i),
						By.cssSelector("input[type='checkbox']"), 10);
				String strI = "" + (i + 1);
				if (lstchkBox != null) {
					commonLibrary.setCheckBox(lstchkBox, strI);
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Select document", DocName
					+ " document is selected", Status.PASS);
		else
			report.updateTestLog("Select document", DocName
					+ "document is not selected", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created.
	// # Function Name : addToCreatedFolder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void addToCreatedFolder(String folderName) {
		boolean flag = false;
		WebElement addtoFolder = commonLibrary.isExist(
				UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null)

			commonLibrary.clickButtonParentWithWait(addtoFolder,
					"Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>
		WebElement chooseFolder = commonLibrary.isExist(
				UIMAP_ResearchMap.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder,
					"Choose Folder");

		WebElement createdFoldersContainer = commonLibrary.isExistNegative(
				UIMAP_Document.createdFoldersContainer, 10);
		List<WebElement> createdFolders = commonLibrary.isExistList(
				createdFoldersContainer, UIMAP_Document.listItems, 10);

		for (WebElement item : createdFolders) {
			if (item.getText().toLowerCase().contains(folderName.toLowerCase())) {
				WebElement folder = commonLibrary.isExistNegative(item,
						UIMAP_Document.links, 10);
				commonLibrary.clickLinkWithWebElementWithWait(folder,
						folderName);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select folder: " + folderName, folderName
					+ " is not present", Status.FAIL);

		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_ResearchMap.saveworkFolder, 10);
		if (saveFolder != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(saveFolder, "save");
			else
				commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
		try {
			commonLibrary.sleep(20000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to sharing a folder.
	// # Function Name : stopSharing    
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void stopSharing() {
		WebElement stopSharing = commonLibrary.isExistNegative(
				UIMAP_WorkFolders.stopSharing, 10);
		commonLibrary.setCheckBox(stopSharing, "Stop Sharing");

		WebElement save = commonLibrary.isExist(UIMAP_WorkFolders.save);
		if (save != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(save, "Save");
			} else {
				commonLibrary.clickButtonParentWithWait(save, "Save");
			}
			save = commonLibrary.isExistNegative(UIMAP_WorkFolders.save, 4);
			WebElement saveNew = commonLibrary.isExistNegative(
					UIMAP_WorkFolders.save, 4);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(5000);
					saveNew = commonLibrary.isExistNegative(
							UIMAP_WorkFolders.save, 4);
					count++;
				} while (save.equals(saveNew) && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(5000);
				System.out.println(e.toString());

			}

		} else {
			report.updateTestLog("Click on Save", "Save button is not clicked",
					Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message.
	// # Function Name : verifyBannerMessage
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void verifyBannerMessage(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg,
				20);
		if (bannermsg != null
				&& bannermsg.getText().toLowerCase()
						.contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text "
					+ text, "Banner message displayed with text " + text,
					Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text "
					+ text,
					"Banner message is not displayed with text " + text,
					Status.FAIL);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message.
	// # Function Name : verifyBannerMessage
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void verifyBannerMessage1(String text) {
		WebElement bannermsg1 = commonLibrary.isExistNegative(
				UIMAP_WorkFolders.bnrmsg1, 20);
		if (bannermsg1 != null
				&& bannermsg1.getText().toLowerCase()
						.contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text "
					+ text, "Banner message displayed with text " + text,
					Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text "
					+ text,
					"Banner message is not displayed with text " + text,
					Status.WARNING);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click folder
	// # Function Name : ClickFolderLink1     
	// # Author :Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public void clickFolderLink1(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(
							OListResult, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(
								document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null
								&& eleDocTitle.getText().toLowerCase()
										.equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(
									eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(
										lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(
						UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage,
							"Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the Folder " + strDocTitle,
					"Not Clicked  on the Folder " + strDocTitle, Status.FAIL);
		else {
			WebElement pgHeader = null;
			pageCheck.positiveCheck(driver, "folder", "Folder");
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC,
					10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null
					&& ((pgHeader.getText().toLowerCase().contains("folder"))))
				report.updateTestLog("Verify Folder " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/" + "Folder "
						+ strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Folder " + strDocTitle
						+ " is displayed", "Folder " + strDocTitle
						+ " is not displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created with small wait.
	// # Function Name : addToCreatedFolder_NoWait     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public void addToCreatedFolder_NoWait(String folderName) {
		try {
			boolean flag = false;
			WebElement addtoFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder,
						"Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>
			WebElement chooseFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder,
						"Choose Folder");

			WebElement createdFoldersContainer = commonLibrary.isExistNegative(
					UIMAP_Document.createdFoldersContainer, 10);
			List<WebElement> createdFolders = commonLibrary.isExistList(
					createdFoldersContainer, UIMAP_Document.listItems, 10);

			for (WebElement item : createdFolders) {
				if (item.getText().toLowerCase()
						.contains(folderName.toLowerCase())) {
					WebElement folder = commonLibrary.isExistNegative(item,
							UIMAP_Document.links, 10);
					commonLibrary.clickLinkWithWebElementWithWait(folder,
							folderName);
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Select folder: " + folderName, folderName
						+ " is not present", Status.FAIL);

			WebElement saveFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonLogSmallWait(saveFolder, "Save");
			pageCheck.ajaxElementCheck(driver,
					properties.getProperty("xSpinner"));

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clickLinkInDoc(String targetText) {

		boolean flg = false;
		// List<WebElement> embeddedLinks =
		// commonLibrary.isExist_List(UIMAP_Document.eltEmbeddedLinks, 20);
		List<WebElement> embeddedLinks = commonLibrary.isExistList(
				UIMAP_Document.eltEmbeddedLinks, 20);
		if (embeddedLinks != null) {
			for (int i = 0; i < embeddedLinks.size(); i++) {
				if (embeddedLinks.get(i).getText().toLowerCase()
						.contains(targetText.toLowerCase())) {
					commonLibrary.clickLinkWithWebElementWithWait(embeddedLinks
							.get(i), embeddedLinks.get(i).getText());
					report.updateTestLog(
							"Click on embedded link " + targetText, "Embedded "
									+ targetText + " link is clicked.",
							Status.PASS);
					flg = true;
					break;
				}
				if (flg)
					break;
			}
		}
		if (!flg)
			report.updateTestLog("Click on embedded link " + targetText,
					"Embedded link " + targetText + " is not clicked.",
					Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : verifyTextNotClickable   
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public void verifyTextNotClickable(String targetText) {
		boolean flg = false;
		WebElement docContent = commonLibrary.isExistNegative(
				UIMAP_Document.docContent, 10);
		if (docContent.getText().toLowerCase()
				.contains(targetText.toLowerCase())) {
			List<WebElement> embeddedLinks = commonLibrary.isExistList(
					UIMAP_Document.eltEmbeddedLinks, 20);
			if (embeddedLinks != null) {
				for (int i = 0; i < embeddedLinks.size(); i++) {
					if (embeddedLinks.get(i).getText().toLowerCase()
							.contains(targetText.toLowerCase())) {
						report.updateTestLog("Verify text " + targetText
								+ " is not clickable", targetText
								+ " is clickable", Status.FAIL);
						flg = true;
						break;
					}
				}
			}
			if (!flg)
				report.updateTestLog("Verify text " + targetText
						+ " is not clickable",
						targetText + " is not clickable", Status.PASS);
		} else
			report.updateTestLog("Verify text " + targetText
					+ " is not clickable", targetText
					+ " is not present in document.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Print Icon and Choose default
	// settings
	// # Function Name : printWithDefaultSettings
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public void printWithDefaultSettings(String parentWindow) {

		clickDeliverySelectOption_New("delivery", "Print (Current Settings)");
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		commonLibrary.switchToWindow("deliverysecondarywindow");

		String header = null;
		WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
		try {
			do {
				pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
				if (pgHeader != null) {
					header = pgHeader.getText().toLowerCase();
					if (header.contains("delivery complete")) {
						try {
							Thread.sleep(8000);
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
				}
			} while (header.contains("processing"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// }

		pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
		if (pgHeader != null
				&& pgHeader.getText().toLowerCase().contains("taking longer"))
			report.updateTestLog(
					"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
					"PROCESSING MSG POP UP is opened AND UPDATED TO A "
							+ pgHeader.getText()
							+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
					Status.FAIL);
		else
			report.updateTestLog(
					"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
					"PROCESSING MSG POP UP is opened AND UPDATED TO A "
							+ pgHeader.getText()
							+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
					Status.PASS);

		WebElement btnDownloadClose = commonLibrary.isExist(
				UIMAP_Document.btnDownloadClose, 10);
		if (btnDownloadClose != null)
			driver.close();

		commonLibrary.smallWait();
		commonLibrary.switchToWindow(parentWindow);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void pdfVerification(String FilePath, String Text, String Exist) {
		try {
			PdfReader reader = new PdfReader(FilePath);
			int Pages = reader.getNumberOfPages();
			boolean blnFlag = false;

			for (int i = 1; i <= Pages; i++) {
				String TestText = PdfTextExtractor.getTextFromPage(reader, i);
				if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
					blnFlag = true;
					break;
				}

			}
			switch (Exist) {
			case "Exist": {
				if (blnFlag) {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is present in the PDF document", Status.PASS);

				} else {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is not present in the PDF document",
							Status.FAIL);
				}
				break;
			}
			case "NotExist": {
				if (!blnFlag) {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is not present in the PDF document",
							Status.PASS);
				} else {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is present in the PDF document", Status.FAIL);
				}
				break;
			}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a folder after clicking
	// folder icon.
	// # Function Name : clickFolderIcon
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public void clickFolderIcon(String docTitle, String folderName) {
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;
		do {
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(
							OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(
								document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null
								&& eleDocTitle.getText().equals(docTitle)) {
							WebElement folderIcon = commonLibrary.isExist(
									document, UIMAP_RelatedContent.folderIcon,
									20);
							if (folderIcon == null)
								folderIcon = commonLibrary.isExist(document,
										UIMAP_RelatedContent.folderIcon1, 20);
							if (folderIcon != null) {
								commonLibrary.clickButtonParentWithWait(
										folderIcon, "Folder Icon");
								blnFlag = true;
								btnNextPage = null;
								break;
							}

						}

					}

				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary
						.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnNextPage,
							"NextPage");

			} else
				break;
			count++;
		} while (btnNextPage != null && count <= 15);

		if (!blnFlag)

			report.updateTestLog("Click folder icon for document " + docTitle,
					"Folder icon is not present for document " + docTitle,
					Status.FAIL);
		else {
			boolean flag1 = false;
			commonLibrary.sleep(25000);
			WebElement locatorPopUp = commonLibrary.isExist(
					UIMAP_RelatedContent.locatorPopUp, 10);
			if (locatorPopUp != null) {
				WebElement folderList = commonLibrary.isExistNegative(
						UIMAP_RelatedContent.folderList, 10);
				List<WebElement> links = commonLibrary.isExistList(folderList,
						UIMAP_RelatedContent.links, 10);
				for (WebElement link : links) {
					if (link.getText().contains(folderName)) {
						commonLibrary.clickLinkWithWebElementWithWait(link,
								link.getText());
						flag1 = true;
						break;
					}
				}
				if (!flag1)
					report.updateTestLog("Click folder icon for document "
							+ docTitle,
							"Folder name is not present for document "
									+ docTitle, Status.FAIL);
			}

			else
				report.updateTestLog("Click folder icon for document "
						+ docTitle, "Locator Popup is not displayed.",
						Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : applySearchFilterEWA
	// # Author : Aravind
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void applySearchFilterEWA(String strToolbarMenuName,
			String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");
		int filters = arrCondentType.length;
		int count = 0;
		WebElement btnClassFilter = commonLibrary.isExist(
				UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter,
						"Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);
		switch (strToolbarMenuName) {
		case "Category": {
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(
						UIMAP_Home.divClassCTFilters, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(
							divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary
							.isExistList(lstTagUList,
									UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								WebElement Checkbox = commonLibrary.isExist(
										item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBoxEWA(Checkbox, state);
								count++;
								break;
							}
						}
				}
			}
			break;
		}
		}
		if (count == filters)
			report.updateTestLog("select check boxes near " + strCondentTypes,
					"check boxes near " + strCondentTypes + " are selected",
					Status.PASS);
		else
			report.updateTestLog("select check boxes near " + strCondentTypes,
					" check boxes near " + strCondentTypes
							+ " are not selected", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void loginThree() {

		String strUserName = dataTable.getData("General_Data", "Username3");
		String strPassword = dataTable.getData("General_Data", "Password3");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.

		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {
			// eltUserName.click(); Previously Used
			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
			}

			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			eltPassword = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
							+ "']"), 20);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
			}

			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
			if (browsername.contains("chrome"))
				documentState(driver);

			if (browsername.contains("ie8")) {
				sleepWait(1000);
				PageCheck.checkAlert(driver);
			}
			final PageCheck pageCheck = new PageCheck(scriptHelper);
			check = pageCheck.positiveCheck(driver, "More", "Home Page");

			pageCheck.handleFlow(driver, check);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write the link from the popup into
	// data sheet.
	// # Function Name : saveLink     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public void saveLink(String tcName, String dataSheet, String colName) {
		WebElement dialogContent = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.dialogContent, 10);
		WebElement linkCont = commonLibrary.isExistNegative(dialogContent,
				UIMAP_CounselBenchmarking.linkCont, 10);
		if (linkCont == null) {
			dialogContent = commonLibrary.isExistNegative(
					UIMAP_CounselBenchmarking.generalDialod, 10);
			linkCont = commonLibrary.isExistNegative(dialogContent,
					UIMAP_CounselBenchmarking.linkCont, 10);
		}
		if (linkCont != null) {
			String link = linkCont.getAttribute("value");
			final FrameworkParameters frameworkParameters = FrameworkParameters
					.getInstance();
			String datatablePath = frameworkParameters.getRelativePath()
					+ Util.getFileSeparator() + "Datatables";
			ExcelDataAccess excel = new ExcelDataAccess(datatablePath,
					dataSheet);
			excel.setDatasheetName("General_Data");
			int iRowNo = excel.getRowNum(tcName, 0);
			excel.setValue(iRowNo, colName, link.trim());
			report.updateTestLog("Write link " + link + " for TestCase "
					+ tcName + " under coloumn " + colName + " in DataSheet "
					+ dataSheet, "Link " + link + " for TestCase " + tcName
					+ " under coloumn " + colName + " in DataSheet "
					+ dataSheet + " is saved.", Status.DONE);

		} else
			report.updateTestLog("Write link for TestCase " + tcName
					+ " under coloumn " + colName + " in DataSheet "
					+ dataSheet, "Link is not present.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void selectDocumentByTitleVerifyCount(String DocName, int count) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary
				.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0),
				By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i),
					By.cssSelector("a[data-action='title']"), 10);
			if (lnkTitle == null)
				lnkTitle = commonLibrary.isExistNegative(LList.get(i),
						By.tagName("a"), 10);
			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
				if (strDocTitle.trim().contains(DocName.trim())) {
					WebElement lstchkBox = commonLibrary.isExist(LList.get(i),
							By.cssSelector("input[type='checkbox']"), 10);
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
			report.updateTestLog("Select document", DocName
					+ " document is selected", Status.PASS);
		else
			report.updateTestLog("Select document", DocName
					+ "document is not selected", Status.FAIL);

		WebElement seletcAllItemsUl = commonLibrary.isExist(
				UIMAP_SearchResult.selectAllItemsUl, 20);
		WebElement selectAllItemsCount = commonLibrary.isExist(
				seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCount, 20);
		i = 0;
		do {

			commonLibrary.sleep(40000);
			i++;
			seletcAllItemsUl = commonLibrary.isExist(
					UIMAP_SearchResult.selectAllItemsUl, 20);
			selectAllItemsCount = commonLibrary.isExist(seletcAllItemsUl,
					UIMAP_SearchResult.selectAllItemsCount, 20);
		} while ((selectAllItemsCount == null || !selectAllItemsCount.getText()
				.equals(count)) && i < 10);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : NavigateToHistoryLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void navigateToHistoryLink(String strDocTitle) {
		Boolean blnFlag = false;

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(
					UIMAP_Home.btnIdHistoryMenuArrow, 20);

			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(
						UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow,
					"History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(
					UIMAP_Home.btnIdRecentDocuments, 20);
			if (btnIdRecentDocuments == null) {
				commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow,
						"History");
			}
			commonLibrary.sleep(Mwait);
			btnIdRecentDocuments = commonLibrary.isExist(
					UIMAP_Home.btnIdRecentDocuments, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments,
					"Recent Documents");

			WebElement lstrecentDocumentsContent = commonLibrary.isExist(
					UIMAP_Home.lstrecentDocumentsContent, 20);
			if (lstrecentDocumentsContent != null) {
				List<WebElement> lnkDocuments = commonLibrary
						.isExistList(lstrecentDocumentsContent,
								UIMAP_Home.lstrecentdocs, 20);
				// if (lnkDocuments.size() > 0)
				// lnkDocuments.get(0).click();
				for (WebElement link : lnkDocuments) {
					if (link.getText().contains(strDocTitle)) {
						blnFlag = true;
						break;

					}
				}
				if (blnFlag)
					report.updateTestLog("Verify Document" + strDocTitle
							+ " will appear in Recemt Dpcoments", "'"
							+ strDocTitle
							+ "' is Displayed in History Menu Documents tab",
							Status.FAIL);
				else
					report.updateTestLog(
							"Verify Document" + strDocTitle
									+ " will appear in Recemt Dpcoments",
							"'"
									+ strDocTitle
									+ "' is not Displayed in History Menu Documents tab(Since folder document view will not appear)",
							Status.PASS);
				commonLibrary.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Download in Download Popup
	// # Function Name : clickDownloadFromAction    
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickDownloadFromAction(String AutoITPath, String fileName,
			String filePath, boolean verifyContent, String PDFText,
			String TextExistence, String parentWindow) {
		try {
			WebElement btnDownload = commonLibrary.isExist(
					UIMAP_Document.btnDownloadinDialog, 10);
			if (btnDownload != null) {
				commonLibrary
						.clickButtonParentWithWait(btnDownload, "Download");
			}

			commonLibrary.switchToWindow("deliverysecondarywindow");
			if (!browsername.contains("internet"))
				commonLibrary.sleep(25000);

			String header = null;
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			do {
				pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
				if (pgHeader != null) {
					header = pgHeader.getText().toLowerCase();
					if (header.contains("delivery complete"))
						break;
				}
				commonLibrary.sleep(20000);
				pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);

			} while (header.contains("processing"));

			if (browsername.contains("firefox")) {
				commonLibrary.sleep(4000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_S);
				// CTRL+Z is now pressed (receiving application should see a
				// "key down" event.)
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_ALT);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			} else if (browsername.contains("internet")
					&& version.contains("8")) {
				commonLibrary.sleep(4000);

				String[] cmd = { AutoITPath, "Save As", filePath, fileName };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet")) {
				commonLibrary.sleep(3000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_F6);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_TAB);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			}

			commonLibrary.sleep(30000);

			String downloadedFilename = filePath + "\\" + fileName;

			WebElement lnkDownload = commonLibrary.isExist(
					UIMAP_Document.lnkDownloadedDoc, 10);
			pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null
					&& pgHeader.getText().toLowerCase()
							.contains("delivery complete")
					&& lnkDownload != null)
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.FAIL);

			WebElement btnDownloadClose = commonLibrary.isExist(
					UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow(parentWindow);

			// Verify content in the downloaded document
			if (verifyContent) {
				// Unzip the document present
				String zipFile = filePath + "\\" + fileName + ".ZIP";
				fileUnZip(filePath, zipFile);
				String arrPDFText[] = PDFText.split("#");
				String arrTextExistence[] = TextExistence.split(";");
				String FileDownloaded = downloadedFilename + ".PDF";
				for (int i = 0; i < arrPDFText.length; i++) {
					pdfVerificationPath(FileDownloaded, arrPDFText[i],
							arrTextExistence[i]);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page and
	// wait
	// # Function Name : clickEmbededLinkWait
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public void clickEmbededLinkWait(String link, String title) {
		boolean flag = false;
		// try {
		// Thread.sleep(5000);
		// } catch (Exception e) {
		// System.out.println(e.toString());
		// }
		WebElement docContent = commonLibrary.isExistNegative(
				UIMAP_Document.docContent, 10);

		List<WebElement> links = commonLibrary.isExistList(docContent,
				UIMAP_Document.embed, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains(link.toLowerCase())) {
				commonLibrary.scrollToView(li);
				commonLibrary.clickMouseMoveAction(li, li.getText());
				// li.click();
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click link " + link, link
					+ " is not clicked.", Status.FAIL);
		else {
			WebElement header = commonLibrary.isExistNegative(
					UIMAP_Document.pgTitleTopicSummary, 10);
			do {
				commonLibrary.sleep(1000);
				header = commonLibrary.isExistNegative(
						UIMAP_Document.pgTitleTopicSummary, 10);
			} while (header == null
					|| !header.getText().toLowerCase()
							.contains(title.toLowerCase()));
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clickLinkInDoc_EntityLink(String targetText) {

		boolean flg = false;
		// List<WebElement> embeddedLinks =
		// commonLibrary.isExist_List(UIMAP_Document.eltEmbeddedLinks, 20);
		List<WebElement> embeddedLinks = commonLibrary.isExistList(
				UIMAP_Document.eltEmbeddedLinks1, 20);
		if (embeddedLinks != null) {
			for (int i = 0; i < embeddedLinks.size(); i++) {
				if (embeddedLinks.get(i).getText().toLowerCase()
						.contains(targetText.toLowerCase())) {
					commonLibrary.clickLinkWithWebElementWithWait(embeddedLinks
							.get(i), embeddedLinks.get(i).getText());
					report.updateTestLog(
							"Click on embedded link " + targetText, "Embedded "
									+ targetText + " link is clicked.",
							Status.PASS);
					flg = true;
					break;
				}
				if (flg)
					break;
			}
		}
		if (!flg)
			report.updateTestLog("Click on embedded link " + targetText,
					"Embedded link " + targetText + " is not clicked.",
					Status.FAIL);

	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to create folder and save
	// // # Function Name : createFolderAndSave     
	// // # Author : Seetha
	// // # Date Created : June'15
	// //
	// #*****************************************************************************************************************************
	//
	// public void verifyEmail(String userName, String password, String
	// hostName)
	// {
	// int i=0;
	// Properties sysProps = System.getProperties();
	//
	// sysProps.setProperty("mail.store.protocol", "imap");
	// // sysProps.setProperty("mail.smtp.host",hostName);
	//
	// sysProps.setProperty("mail.smtp.port","25");
	// sysProps.setProperty("mail.imap.port","25");
	// // sysProps.setProperty("mail.pop.port","25");
	//
	//
	// try {
	//
	// Session session = Session.getInstance(sysProps, null);
	// session.setDebug(true);
	// Store store = session.getStore();
	//
	// store.connect(hostName, userName, password);
	//
	// Folder emailInbox = store.getFolder("INBOX");
	//
	// emailInbox.open(Folder.READ_ONLY);
	//
	// int messageCount = emailInbox.getMessageCount();
	//
	// System.out.println("Total Message Count: " + messageCount);
	//
	// int unreadMsgCount = emailInbox.getUnreadMessageCount();
	//
	// Message emailMessage = emailInbox.getMessage(0);
	//
	// System.out.println("Email Subject: " + emailMessage.getSubject());
	//
	// emailMessage.setFlag(Flags.Flag.SEEN, true);
	//
	// emailInbox.close(true);
	//
	// store.close();
	//
	// } catch (Exception mex) {
	//
	// mex.printStackTrace();
	//
	// }
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delivery ooptions
	// # Function Name : verifyResultHeader     
	// # Author : Baswaraj
	// # Date Created : August'15
	// #*****************************************************************************************************************************
	public void clickDeliverySelectOption(String option, String DeliverOption) {
		Boolean blnflag = false;
		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu,
				UIMAP_SearchResult.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list,
						By.tagName("button"), 20);
				if (option.contains("print")) {
					commonLibrary
							.clickButtonParentWithWait(btn1.get(0), option);
					asideclass = commonLibrary.isExist(list,
							UIMAP_SearchResult.tagNameAside, 10);
					break;
				} else if (option.toLowerCase().contains("delivery")) {
					commonLibrary
							.clickButtonParentWithWait(btn1.get(1), option);
					blnflag = true;
					asideclass = commonLibrary.isExist(list,
							UIMAP_SearchResult.tagNameAside, 10);
					break;
				}
			}
		}
		if (blnflag) {
			if (asideclass != null) {
				List<WebElement> listItems = commonLibrary.isExistList(
						asideclass, By.tagName("button"), 20);
				for (WebElement List : listItems) {
					if (List.getAttribute("data-action")
							.contains(DeliverOption)) {
						commonLibrary.clickButtonParentWithWait(List,
								DeliverOption);
						break;
					}
				}
			} else {
				report.updateTestLog("Click on " + DeliverOption + " Option ",
						"' " + DeliverOption + " ' option is not clicked",
						Status.FAIL);
			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delivery ooptions
	// # Function Name : verifyResultHeader     
	// # Author : Baswaraj
	// # Date Created : August'15
	// #*****************************************************************************************************************************
	public void clickDeliverySelectOption_New(String option,
			String DeliverOption) {
		Boolean blnflag = false;
		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu,
				UIMAP_SearchResult.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list,
						By.tagName("button"), 20);
				if (option.toLowerCase().toLowerCase().contains("delivery")) {
					if (browsername.contains("internet")) {
						try {
							driver.manage().timeouts()
									.pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.clickLinkWithWebElementWithWait(
									btn1.get(1), option);

							// commented by kirthika

							/*
							 * driver.manage().timeouts().pageLoadTimeout(1,
							 * TimeUnit.SECONDS);
							 * commonLibrary.click_MouseMove_Action(btn1.get(1),
							 * option);
							 */
							blnflag = true;
							asideclass = commonLibrary.isExist(list,
									UIMAP_SearchResult.tagNameAside, 10);
							break;
						} catch (TimeoutException ex) {
							driver.manage().timeouts()
									.pageLoadTimeout(220, TimeUnit.SECONDS);
							System.out.println(ex.toString());

						}
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
					} else {
						commonLibrary.clickButtonParentWithWait(btn1.get(1),
								option);
						blnflag = true;
						asideclass = commonLibrary.isExist(list,
								UIMAP_SearchResult.tagNameAside, 10);
						break;
					}

				}
			}
		}
		if (blnflag) {

			if (asideclass != null) {
				String dataaction = null;
				switch (DeliverOption) {
				case "Email": {
					dataaction = "email";
					break;
				}
				case "DownLoad(Current Settings)": {
					dataaction = "downloadnow";
					break;
				}
				case "Print (Current Settings)": {
					dataaction = "printnow";
					break;
				}
				case "Print(Choose new Settings)": {
					dataaction = "printopt";
					break;
				}
				case "Download(Choose new Settings)": {
					dataaction = "downloadopt";
					break;

				}
				case "Printer-friendly view": {
					dataaction = "printablepage";
					break;
				}
				default: {
					report.updateTestLog("Click on " + DeliverOption
							+ " Option ", "' " + DeliverOption
							+ " ' option is not presnt", Status.FAIL);
				}
				}
				List<WebElement> listItems = commonLibrary.isExistList(
						asideclass, By.tagName("button"), 20);
				for (WebElement List : listItems) {
					if (List.getAttribute("data-action").contains(dataaction)) {
						if (browsername.contains("internet")) {
							try {
								driver.manage().timeouts()
										.pageLoadTimeout(1, TimeUnit.SECONDS);
								commonLibrary.clickMouseMoveAction(List,
										DeliverOption);
								break;
							} catch (TimeoutException ex) {
								driver.manage().timeouts()
										.pageLoadTimeout(220, TimeUnit.SECONDS);
								System.out.println(ex.toString());

							}
							driver.manage().timeouts()
									.pageLoadTimeout(220, TimeUnit.SECONDS);
						} else {
							commonLibrary.clickButtonParentWithWait(List,
									DeliverOption);
							break;
						}

					}
				}
			} else {
				report.updateTestLog("Click on " + DeliverOption + " Option ",
						"' " + DeliverOption + " ' option is not clicked",
						Status.FAIL);
			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void verifyPageTitle(String strPageHeader) {
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(
				UIMAP_Document.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(
				UIMAP_Document.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(
				UIMAP_Document.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(
				UIMAP_Document.hdrResult, 5);

		WebElement HeaderSearchResult5 = commonLibrary.isExistNegative(
				UIMAP_IMContent.SearchResultHeader4, 5);

		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;
		else if (HeaderSearchResult5 != null)
			Header = HeaderSearchResult5;

		if (Header != null
				&& Header.getText().toLowerCase().trim()
						.contains(strPageHeader.toLowerCase().trim()))
			report.updateTestLog("Verify " + strPageHeader
					+ " is displayed as Header", strPageHeader
					+ " is displayed as Header", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader
					+ " is displayed as Header", strPageHeader
					+ " is not displayed as Header instead it is displaying"
					+ Header.getText(), Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Secondary_BroserOpened 
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void openedSecondaryBrowser() {
		if (driver.getWindowHandles().size() >= 2)

			report.updateTestLog("Verify Secondary browser window is opened",
					"Secondary browser window is opened", Status.PASS);
		else

			report.updateTestLog("Verify Secondary browser window is opened",
					"Secondary browser window is not opened", Status.FAIL);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickDocTitle(String strDocTitle) {

		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;

		do {

			// Loop for finding the document title and click on it
			WebElement resultClass = null;

			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {

				WebElement OListResult = commonLibrary.isExist(resultClass,
						By.tagName("ol"), 20);
				if (OListResult != null) {

					List<WebElement> OListItems = commonLibrary.isExistList(
							OListResult, By.tagName("li"), 20);

					for (WebElement document : OListItems) {

						WebElement eleDocTitle = commonLibrary.isExist(
								document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null
								&& eleDocTitle.getText().trim()
										.equals(strDocTitle.trim())) {

							WebElement lnkDocument = commonLibrary.isExist(
									eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {

								commonLibrary.clickLinkWithWebElementWithWait(
										lnkDocument, lnkDocument.getText());

								blnFlag = true;

								break;
							}
						}

					}

				}

			}

			// if document is not present in the current page, click on next
			// page button
			if (!blnFlag) {

				btnNextPage = commonLibrary
						.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null)

					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(
								btnNextPage, "NextPage");
					} else {
						commonLibrary.clickMethod(btnNextPage, "NextPage");
					}

				commonLibrary.sleep(5000);
			}
			count++;

		} while ((blnFlag != true) && (count <= 15));

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle,
					"Not Clicked  on the document " + strDocTitle, Status.FAIL);

		else {

			WebElement pgHeader = null;
			pageCheck.positiveCheck(driver, "document", "Document");

			// wait till the document page is loaded
			int counter1 = 0;

			do {

				commonLibrary.sleep(5000);
				counter1 = counter1 + 1;

			} while (!driver.getCurrentUrl().contains("document")
					&& counter1 <= 15);

			WebElement h1 = commonLibrary.isExist(
					UIMAP_Document.txtDocumentHeading, 10);
			int count1 = 0;

			do {

				h1 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading,
						10);
				commonLibrary.sleep(2000);
				count1++;

			} while (h1 == null && count1 < 20);

			WebElement atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);
			WebElement copyCitation = commonLibrary.isExist(
					UIMAP_Document.copyCitation, 10);
			int counter = 0;
			do {
				counter = counter + 1;
				atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);
				copyCitation = commonLibrary.isExist(
						UIMAP_Document.copyCitation, 10);

				if (atd == null && copyCitation == null)
					commonLibrary.sleep(3000);
			} while (atd == null && copyCitation == null && counter <= 20);

			// Verification Point: Document page title displayed
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC,
					100) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.TitleClassTOC, 10);

			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.pgClassHeaderOption3, 10);

			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null
					&& (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/"
						+ "document " + strDocTitle + " is displayed",
						Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", "document " + strDocTitle
						+ " is not displayed", Status.FAIL);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify delivery options
	// # Function Name : verifyDeliveryOption_New     
	// # Author : Kirthika
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public void verifyDeliveryOption_New(String option, String DeliverOption) {
		Boolean blnflag = false;
		Boolean blnflag1 = false;
		WebElement asideclass = null;
		String[] deliveryOptions = DeliverOption.split(";");
		WebElement menu = commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu,
				UIMAP_SearchResult.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list,
						By.tagName("button"), 20);
				if (option.toLowerCase().toLowerCase().contains("delivery")) {
					if (browsername.contains("internet")) {
						try {
							driver.manage().timeouts()
									.pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.clickLinkWithWebElementWithWait(
									btn1.get(1), option);

							blnflag = true;
							asideclass = commonLibrary.isExist(list,
									UIMAP_SearchResult.tagNameAside, 10);
							break;
						} catch (TimeoutException ex) {
							driver.manage().timeouts()
									.pageLoadTimeout(220, TimeUnit.SECONDS);
							System.out.println(ex.toString());

						}
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
					} else {
						commonLibrary.clickButtonParentWithWait(btn1.get(1),
								option);
						blnflag = true;
						asideclass = commonLibrary.isExist(list,
								UIMAP_SearchResult.tagNameAside, 10);
						break;
					}

				}
			}
		}
		if (blnflag) {
			for (int iterator = 0; iterator < deliveryOptions.length; iterator++) {
				if (asideclass != null) {
					String dataaction = null;
					switch (deliveryOptions[iterator]) {
					case "Email": {
						dataaction = "email";
						break;
					}
					case "DownLoad(Current Settings)": {
						dataaction = "downloadnow";
						break;
					}
					case "Print (Current Settings)": {
						dataaction = "printnow";
						break;
					}
					case "Print(Choose new Settings)": {
						dataaction = "printopt";
						break;
					}
					case "Download(Choose new Settings)": {
						dataaction = "downloadopt";
						break;

					}
					case "Printer-friendly view": {
						dataaction = "printablepage";
						break;
					}
					default: {
						report.updateTestLog(
								"Verify " + deliveryOptions[iterator]
										+ " is Displayed in Delivery DropDown",
								deliveryOptions[iterator] + " is not displayed",
								Status.FAIL);
					}
					}
					List<WebElement> listItems = commonLibrary.isExistList(
							asideclass, By.tagName("button"), 20);
					for (WebElement List : listItems) {
						if (List.getAttribute("data-action").contains(
								dataaction)) {
							blnflag1 = true;
						}

					}
					if (blnflag1) {
						report.updateTestLog("Verify "
								+ deliveryOptions[iterator]
								+ " is Displayed in Delivery DropDown ",
								deliveryOptions[iterator] + " is  displayed",
								Status.PASS);
					}

					else {
						report.updateTestLog(
								"Verify " + deliveryOptions[iterator]
										+ " is Displayed in Delivery DropDown",
								deliveryOptions[iterator] + " is not displayed",
								Status.FAIL);
					}
				}
			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : sep'15
	// #*****************************************************************************************************************************

	public void verifyFullDocument(String searchTerm) {

		Boolean flag = false;
		try {
			WebElement banner = commonLibrary.isExist(UIMAP_Shepards.hdrBanner,
					10);
			if (banner != null) {
				List<WebElement> span = commonLibrary.isExistList(banner,
						UIMAP_Shepards.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase()
							.contains(searchTerm.toLowerCase())) {
						flag = true;
					}
				}
			}
			if (flag) {
				report.updateTestLog(
						"Verify full document page is displayed for "
								+ searchTerm + "",
						"Full document page is displayed for " + searchTerm
								+ "", Status.PASS);
			} else {
				report.updateTestLog(
						"Verify full document page is displayed for "
								+ searchTerm + "",
						"Full document page is not displayed for " + searchTerm
								+ "", Status.PASS);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void clickTextInWordWheel(String text) {
		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(
				UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel,
				UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			if (option.getText().toLowerCase().contains(text.toLowerCase())) {
				commonLibrary.clickLinkWithWebElement(option, text);
				report.updateTestLog("Click " + text
						+ " phrase from word wheel.", text
						+ " is clicked from word wheel.", Status.DONE);

				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Click " + text + " phrase from word wheel.",
					text + " is not present in word wheel.", Status.FAIL);
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch,
				20);
		if (eltSearchbox.getAttribute("value").toLowerCase()
				.contains(text.toLowerCase()))
			report.updateTestLog("Verify " + text
					+ " displays in main search box.", text
					+ " displays in main search box.", Status.PASS);
		else
			report.updateTestLog("Verify " + text
					+ " displays in main search box.", text
					+ " does not display in main search box.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify current page.
	// # Function Name : verifyCurrentPage
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public void verifyCurrentPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(
				UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase()
				.contains(pageName.toLowerCase())
				|| (driver.getCurrentUrl().contains(pageName.replace(" ", "")
						.toLowerCase()))) {
			report.updateTestLog(pageName + " landing page is displayed",
					pageName + " landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " landing page is displayed",
					pageName + " landing page is not displayed", Status.FAIL);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save changes to settings or
	// close settings without saving
	// # Function Name : clickSaveOrCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public void clickSaveOrCancel(String action) {
		switch (action) {
		case "Save": {
			WebElement submit = commonLibrary.isExist(
					UIMAP_Settings.savechangesbutton, 100);
			if (submit != null && submit.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(submit, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch,
							20);
					count++;
				} while (searchBox == null && count < 40);
			} else {
				WebElement cancel = commonLibrary.isExist(
						UIMAP_Settings.btnIdCancelSettChange, 100);
				if (cancel != null && cancel.isEnabled()) {
					commonLibrary.clickButtonParentWithWait(cancel, "submit");
					WebElement searchBox = null;
					int count = 0;
					do {
						commonLibrary.sleep(10000);
						searchBox = commonLibrary.isExist(
								UIMAP_Home.txtIdSearch, 20);
						count++;
					} while (searchBox == null && count < 40);
				}
			}
			break;
		}
		case "Cancel": {
			WebElement cancel = commonLibrary.isExist(
					UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch,
							20);
					count++;
				} while (searchBox == null && count < 40);
			}
			break;
		}
		}
		commonLibrary.sleep(20000);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in ClientId Page
	// # Function Name : setClientIDValue
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public void setClientIDValue(String strClientID) {
		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary
				.isExist(UIMAP_SearchResult.rdoClientId);
		if (rdoClientId != null) {

			commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID",
					"Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary
				.isExist(UIMAP_SearchResult.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID,
					"Client id");

		} else
			report.updateTestLog("Setting New Client ID",
					"New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary
				.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId,
						"Set Client Id");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId,
						"Set Client Id");
			}

		} else
			report.updateTestLog("Clicking on Set Client ID",
					"Set Client ID can not be clicked.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardTopicCVALegalAnalysis
	// # Author : Bikash
	// # Date Created : Sep'9
	// #*****************************************************************************************************************************

	public void clickDashboardTopicCVA(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary
				.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(
				topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase()
					.contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li,
						UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);
				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic
					+ " in the Medmal Dashboard page", "The Topic link "
					+ dashboardTopic
					+ " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic
					+ " in the Medmal Dashboard page", "The Topic link "
					+ dashboardTopic
					+ " is not selected in the Medmal Dashboard page",
					Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify medmal dialog with buttons
	// # Function Name : verifyMedmalDialogLegalAnalysis
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public void verifyMedmalDialog() {

		WebElement dialog = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.dialog, 10);
		WebElement saveDashboardBtn = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.saveDashboardBtn, 10);
		WebElement continueWithoutSavingBtn = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.continueWithoutSaving, 10);

		if (dialog != null && saveDashboardBtn != null
				&& continueWithoutSavingBtn != null) {
			report.updateTestLog("Verify the  medmal dialog with buttons",
					"The medmal dialog with buttons are displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the  medmal dialog with buttons",
					"The medmal dialog with buttons are not displayed",
					Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the client id
	// # Function Name : navigateToClientLinkCaseValueAssessment
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public void navigateToClientLink(String strLinkName) {

		WebElement btnClassClient = commonLibrary.isExist(
				UIMAP_Home.btnClassClient, 20);
		if (btnClassClient != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnClassClient,
						"Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassClient,
						"Set Client ID");
			}
		}
		commonLibrary.sleep(Mwait);
		if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
			WebElement lnkTxtSetAddClientID = commonLibrary.isExist(
					UIMAP_Home.btnActionSetAddClientID, 20);
			if (browsername.contains("internet"))
				commonLibrary
						.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
			else
				commonLibrary.clickLinkWithWebElement(lnkTxtSetAddClientID,
						"Set/Add Client ID");
		} else if (strLinkName.equalsIgnoreCase("-None-")) {
			WebElement lnkTxtNone = commonLibrary.isExist(
					UIMAP_Home.lnkTxtNone, 20);
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(lnkTxtNone, "Set Client ID");

			} else {
				commonLibrary.clickLinkWithWebElement(lnkTxtNone,
						"Set Client ID");
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in the word wheel.
	// # Function Name : verifyWordWheel
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public void verifyWordWheel() {

		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(
				UIMAP_Home.wordWheel, 10);
		// List<WebElement> optionList = commonLibrary.isExistList(wordWheel,
		// UIMAP_Home.lnkLinks, 10);
		if (wordWheel != null) {
			report.updateTestLog("Verify wordwheel displayed",
					"Wordwheel is displayed", Status.PASS);
			blnFlag = true;
			// break;
		}

		if (!blnFlag)
			report.updateTestLog("Verify wordwheel displayed",
					"Wordwheel is displayed", Status.FAIL);
		// return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink1     
	// # Author :
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickDocLinkContains(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(
						resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary
								.isExistList(list, UIMAP_SearchResult.listtag,
										20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(
									document, UIMAP_SearchResult.TitleClassDoc,
									2);
							if (eleDocTitle != null
									&& eleDocTitle
											.getText()
											.toLowerCase()
											.replaceAll("[^a-zA-Z0-9]", "")
											.trim()
											.contains(
													strDocTitle
															.toLowerCase()
															.replaceAll(
																	"[^a-zA-Z0-9]",
																	"").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(
										eleDocTitle, UIMAP_SearchResult.atag,
										20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary
											.clickLinkWithWebElementWithWait(
													lnkDocument,
													lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(
						UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage,
							"Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle,
					"Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {

			WebElement atd = commonLibrary.isExist(
					UIMAP_Document.jumptoCollapse, 10);

			int counter = 0;
			do {
				counter = counter + 1;
				atd = commonLibrary.isExist(UIMAP_Document.jumptoCollapse, 20);
				if (atd == null)
					commonLibrary.sleep(5000);
			} while (atd == null && counter <= 40);

			check = pageCheck.positiveCheck(driver, "document", "Document");

			pageCheck.handleFlow(driver, check);

			WebElement pgHeader = null;
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC,
					10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null
					&& (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/"
						+ "document " + strDocTitle + " is displayed",
						Status.PASS);
			else if (pgHeader != null
					&& ((pgHeader.getText().toLowerCase().contains(strDocTitle))))
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", pgHeader.getText() + "/"
						+ "document " + strDocTitle + " is displayed",
						Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle
						+ " is displayed", "document " + strDocTitle
						+ " is not displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in the word wheel.
	// # Function Name : verifyTextInWordWheel
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void verifyTextInWordWheel_1(String text) {

		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(
				UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel,
				UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			if (option.getText().toLowerCase().contains(text.toLowerCase())) {
				report.updateTestLog("Verify " + text
						+ "displays in the word wheel", text
						+ "is displayed in word wheel", Status.PASS);
				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Verify " + text
					+ "displays in the word wheel", text
					+ "is not displayed in word wheel", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write the URL into data sheet.
	// # Function Name : saveURL     
	// # Author : Anbu
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public void saveURL(String tcName, String dataSheet, String colName) {
		if (driver.getWindowHandles().size() > 0) {
			// storing url in string
			String url = driver.getCurrentUrl();
			// saving url in exceel sheet
			final FrameworkParameters frameworkParameters = FrameworkParameters
					.getInstance();
			String datatablePath = frameworkParameters.getRelativePath()
					+ Util.getFileSeparator() + "Datatables";
			ExcelDataAccess excel = new ExcelDataAccess(datatablePath,
					dataSheet);
			excel.setDatasheetName("General_Data");
			int iRowNo = excel.getRowNum(tcName, 0);
			excel.setValue(iRowNo, colName, url.trim());
			report.updateTestLog("Write URL " + url + " for TestCase " + tcName
					+ " under coloumn " + colName + " in DataSheet "
					+ dataSheet, "URL " + url + " for TestCase " + tcName
					+ " under coloumn " + colName + " in DataSheet "
					+ dataSheet + " is saved.", Status.DONE);
		} else {
			report.updateTestLog("Write URL for TestCase " + tcName
					+ " under coloumn " + colName + " in DataSheet "
					+ dataSheet, "URL is not available", Status.FAIL);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application and to
	// delete cookies
	// # Function Name : logoutDeleteAllCookies     
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public SignIn logoutDeleteAllCookies() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkSignOut = commonLibrary.isExist(
				By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore,
							"More");
			}
			lnkSignOut = commonLibrary.isExist(
					By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		}
		if (lnkSignOut != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
					"Sign Out");

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		WebElement btnIdLogin = commonLibrary.isExistNegative(
				UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null
				&& driver.getCurrentUrl().toLowerCase()
						.contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is NOT displayed",
					Status.WARNING);

		}
		driver.manage().deleteAllCookies();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links on document page
	// # Function Name : clickDocPageLink     
	// # Author : Bikash
	// # Date Created : Oct'26
	// #*****************************************************************************************************************************
	public void clickDocPageLink(String linkName) {

		WebElement linkDocPage = commonLibrary.isExist(By.linkText(linkName),
				10);

		commonLibrary.clickLinkWithWebElementWithWait(linkDocPage,
				linkDocPage.getText());

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab and get status in Status
	// Report Pod
	// # Function Name : selectTabAndGetStatus     
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public void selectTabAndGetStatus(String term, String citationManual,
			String exercise) {
		String isr = "Individual Student Report";
		WebElement tabs = commonLibrary.isExistNegative(
				UIMAP_Interactivecitationworkstation.tabs, 10);
		if (!term.toLowerCase().equals(isr.toLowerCase()))
			commonLibrary.selectFromListButton(tabs, term);

		if (term.toLowerCase().equals(isr.toLowerCase())) {
			WebElement selectFromStudentDd = commonLibrary.isExistNegative(
					UIMAP_Interactivecitationworkstation.selectFromStudentDd,
					10);
			if (selectFromStudentDd != null && !citationManual.equals(""))
				commonLibrary.selectByVisibleText(selectFromStudentDd,
						citationManual);

			else {
				try {
					Select select = new Select(selectFromStudentDd);
					select.selectByIndex(0);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			commonLibrary.sleep(5000);

			WebElement selectFromStuCitationDd = commonLibrary
					.isExistNegative(
							UIMAP_Interactivecitationworkstation.selectFromStuCitationDd,
							10);
			if (selectFromStuCitationDd != null && !exercise.equals(""))
				commonLibrary.selectByVisibleText(selectFromStuCitationDd,
						exercise);

			else {
				try {
					Select select = new Select(selectFromStuCitationDd);
					select.selectByIndex(0);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary
					.isExistNegative(
							UIMAP_Interactivecitationworkstation.getStatusReportStu,
							10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport,
						getStatusReport.getAttribute("value"));
			}

			commonLibrary.sleep(5000);

		} else {
			WebElement selectFromCItationManualDd = commonLibrary
					.isExistNegative(
							UIMAP_Interactivecitationworkstation.selectFromCItationManualDd,
							10);
			if (selectFromCItationManualDd != null
					&& !citationManual.equals(""))
				commonLibrary.selectByVisibleText(selectFromCItationManualDd,
						citationManual);

			else {
				try {
					Select select = new Select(selectFromCItationManualDd);
					select.selectByIndex(0);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			commonLibrary.sleep(5000);

			WebElement selectFromExerciseDd = commonLibrary.isExistNegative(
					UIMAP_Interactivecitationworkstation.selectFromExerciseDd,
					10);
			if (selectFromExerciseDd != null && !exercise.equals(""))
				commonLibrary.selectByVisibleText(selectFromExerciseDd,
						exercise);

			else {
				try {
					Select select = new Select(selectFromExerciseDd);
					select.selectByIndex(0);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary.isExistNegative(
					UIMAP_Interactivecitationworkstation.getStatusReport, 10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport,
						getStatusReport.getText());
			}

			commonLibrary.sleep(5000);

		}

	}

	public int lpaselectCondentType(String strCondentType) {
		Boolean blnFlag = false;
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement btnFewerCat = commonLibrary.isExistNegative(
					UIMAP_SearchResult.btnFewerCat, 10);
			if ((btnFewerCat != null)
					&& (btnFewerCat.getText().contains("Show more"))) {
				if (browsername.contains("internet"))
					commonLibrary.clickLinkWithWebElementWithWaitJS(
							btnFewerCat, "More Categories");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat,
							"More Categories");
				commonLibrary.sleep(50000);
			}
			List<WebElement> ulCondentSwitcher = commonLibrary.isExistList(
					UIMAP_SearchResult.ulCondentSwitcher, 10);
			if (ulCondentSwitcher != null) {
				for (WebElement ele : ulCondentSwitcher) {
					List<WebElement> OListItems = commonLibrary.isExistList(
							ele, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement element : OListItems) {
							WebElement btnCondentType = commonLibrary.isExist(
									element, By.tagName("button"), 20);
							if (btnCondentType == null)
								btnCondentType = commonLibrary.isExist(element,
										By.tagName("span"), 20);
							if (btnCondentType.getText().toString()
									.toLowerCase()
									.contains(strCondentType.toLowerCase())) {

								if (browsername.contains("internet"))
									commonLibrary
											.clickLinkWithWebElementWithWait(
													btnCondentType,
													btnCondentType.getText());
								else
									commonLibrary.clickMethod(btnCondentType,
											btnCondentType.getText());
								try {
									String loadProp = properties
											.getProperty("xSpinner");
									int count = 0;
									WebElement loader = commonLibrary
											.isExistNegative(
													By.xpath(loadProp), 5);
									do {
										commonLibrary.sleep(10000);
										loader = commonLibrary.isExistNegative(
												By.xpath(loadProp), 5);
										count++;
									} while (loader != null && count < 15);
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								commonLibrary.sleep(50000);
								blnFlag = true;
								break;
							}
						}
					}
					if (blnFlag)
						break;
				}
			}
			if (!blnFlag) {
				report.updateTestLog("Click on " + strCondentType,
						"Not Click on " + strCondentType, Status.FAIL);
				return 0;
			} else {
				commonLibrary.sleep(30000);
				pageCheck.ajaxElementCheck(driver,
						properties.getProperty("xSpinner"));

				WebElement eltResultHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.eltResultHeader, 10);

				int counter = 0;
				do {
					counter = counter + 1;
					eltResultHeader = commonLibrary.isExistNegative(
							UIMAP_SearchResult.eltResultHeader, 10);
					if (eltResultHeader == null)
						commonLibrary.sleep(3000);

				} while ((eltResultHeader != null) && (counter < 30));

				return 1;
			}
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the icw logo
	// # Function Name : Verify_ICWLogo   
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void verifyICWLogo() {
		WebElement prdctSwttcher = commonLibrary.isExist(
				UIMAP_Interactivecitationworkstation.prdctSwtcher, 20);
		WebElement divLogo = commonLibrary.isExist(prdctSwttcher,
				UIMAP_Interactivecitationworkstation.divlogo, 20);
		WebElement imglogo = commonLibrary.isExist(divLogo,
				UIMAP_Interactivecitationworkstation.imglogo, 20);
		if (imglogo != null) {
			String TxtLogo = imglogo.getText();
			if (TxtLogo.contains("Interactive Citation Workstation")) {
				report.updateTestLog(
						"Verifying ICW Logo",
						"ICW Home Page displayed with the logo LexisNexis Interactive Ciation Workstation",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verifying ICW Logo",
						"ICW Home Page is not displayed with the logo LexisNexis Interactive Ciation Workstation",
						Status.FAIL);
			}
		} else {
			report.updateTestLog("Verifying ICW Logo",
					"ICW Logo is not displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink1     
	// # Author :
	// # Date Created :Nov '15
	// #*****************************************************************************************************************************

	public void clickDocLink1(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(
						resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary
								.isExistList(list, UIMAP_SearchResult.listtag,
										20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(
									document, UIMAP_SearchResult.TitleClassDoc,
									2);
							if (eleDocTitle != null
									&& eleDocTitle
											.getText()
											.toLowerCase()
											.replaceAll("[^a-zA-Z0-9]", "")
											.trim()
											.equals(strDocTitle
													.toLowerCase()
													.replaceAll("[^a-zA-Z0-9]",
															"").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(
										eleDocTitle, UIMAP_SearchResult.atag,
										20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary
											.clickLinkWithWebElementWithWait(
													lnkDocument,
													lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(
						UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage,
							"Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle,
					"Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		//
		// WebElement atd =
		// commonLibrary.isExist(UIMAP_Document.eltAboutThisDoc, 10);
		//
		// int counter = 0;
		// do {
		// counter = counter + 1;
		// commonLibrary.sleep(20000);
		// atd = commonLibrary.isExist(UIMAP_Document.eltAboutThisDoc, 20);
		// if (atd == null && counter>20)
		// atd = commonLibrary.isExist(UIMAP_Document.copyCitation, 20);
		//
		// } while (atd == null && counter <= 40);
		//
		// check = pageCheck.positiveCheck(driver, "document", "Document");
		//
		// pageCheck.handleFlow(driver, check);
		//
		// WebElement pgHeader = null;
		// if (commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10) !=
		// null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
		// else if
		// (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10);
		// else if
		// (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3,
		// 10);
		//
		// if (pgHeader != null &&
		// (pgHeader.getText().toLowerCase().contains("document")))
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle
		// + " is displayed", Status.PASS);
		// else if (pgHeader != null &&
		// ((pgHeader.getText().toLowerCase().contains(strDocTitle.toLowerCase()))))
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle
		// + " is displayed", Status.PASS);
		// else
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is not displayed",
		// Status.FAIL);
		// }

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to saving url to note pad
	// # Function Name : getCurrentUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public void getCurrentUrl(String excelData) {

		String sheetNam = excelData.split(";")[0];
		int row = Integer.parseInt((excelData.split(";")[1]));
		String column = excelData.split(";")[2];
		String datatablePath = excelData.split(";")[3];
		String strUrl = driver.getCurrentUrl();
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, sheetNam);
		excel.setDatasheetName("General_Data");
		excel.setValue(row, column, strUrl);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link without page
	// header verification statement
	// # Function Name : clickDocLinkWitoutHeaderVerification     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public void clickDocLinkWitoutHeaderVerification(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(
						resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary
								.isExistList(list, UIMAP_SearchResult.listtag,
										20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(
									document, UIMAP_SearchResult.TitleClassDoc,
									2);
							if (eleDocTitle != null
									&& eleDocTitle
											.getText()
											.toLowerCase()
											.replaceAll("[^a-zA-Z0-9]", "")
											.trim()
											.equals(strDocTitle
													.toLowerCase()
													.replaceAll("[^a-zA-Z0-9]",
															"").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(
										eleDocTitle, UIMAP_SearchResult.atag,
										20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary
											.clickLinkWithWebElementWithWait(
													lnkDocument,
													lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(
						UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage,
							"Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle,
					"Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {

			WebElement headerOld = commonLibrary.isExistNegative(
					UIMAP_Document.txtStudentDocumentHeading, 3);
			WebElement headerNew = commonLibrary.isExistNegative(
					UIMAP_Document.txtStudentDocumentHeading, 3);
			int counter = 0;
			try {
				do {
					counter = counter + 1;
					commonLibrary.sleep(2000);
					headerNew = commonLibrary.isExistNegative(
							UIMAP_Document.txtStudentDocumentHeading, 3);

				} while (headerOld.equals(headerNew) && counter <= 40);

			} catch (Exception e) {
				System.out.println(e.toString());
				commonLibrary.sleep(1000);
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public void clickTextInWordWheel1(String text) {
		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(
				UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel,
				UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			if (option.getText().toLowerCase().contains(text.toLowerCase())) {
				commonLibrary.clickLinkWithWebElement(option, text);
				report.updateTestLog("Click " + text
						+ " phrase from word wheel.", text
						+ " is clicked from word wheel.", Status.DONE);

				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Click " + text + " phrase from word wheel.",
					text + " is not present in word wheel.", Status.FAIL);
		WebElement eltSearchbox = commonLibrary.isExist(
				UIMAP_Home.btnClassFilter, 20);
		if (eltSearchbox.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify " + text
					+ " displays in main search box.", text
					+ " displays in main search box.", Status.PASS);
		else
			report.updateTestLog("Verify " + text
					+ " displays in main search box.", text
					+ " does not display in main search box.", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc1   
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clickLinkInDoc1(String targetText) {

		boolean flg = false;
		// List<WebElement> embeddedLinks =
		// commonLibrary.isExist_List(UIMAP_Document.eltEmbeddedLinks, 20);
		List<WebElement> embeddedLinks = commonLibrary.isExistList(
				UIMAP_Document.eltEmbeddedLinks1, 20);
		if (embeddedLinks != null) {
			for (int i = 0; i < embeddedLinks.size(); i++) {
				if (embeddedLinks.get(i).getText().toLowerCase()
						.contains(targetText.toLowerCase())) {
					commonLibrary.clickLinkWithWebElementWithWait(embeddedLinks
							.get(i), embeddedLinks.get(i).getText());
					report.updateTestLog(
							"Click on embedded link " + targetText, "Embedded "
									+ targetText + " link is clicked.",
							Status.PASS);
					flg = true;
					break;
				}
				if (flg)
					break;
			}
		}
		if (!flg)
			report.updateTestLog("Click on embedded link " + targetText,
					"Embedded link " + targetText + " is not clicked.",
					Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Home Link
	// # Function Name : clickHome
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public void clickHome() {
		WebElement homeLink = commonLibrary.isExistNegative(
				UIMAP_Interactivecitationworkstation.homeLink, 20);
		if (homeLink == null)
			homeLink = commonLibrary.isExistNegative(
					UIMAP_Interactivecitationworkstation.homeLinkProf, 20);
		WebElement currProdOld = commonLibrary.isExistNegative(
				UIMAP_WorkFolders.currProd, 10);
		commonLibrary.clickButtonParentWithWait(homeLink, "Home");
		try {
			WebElement currProdNew = commonLibrary.isExistNegative(
					UIMAP_WorkFolders.currProd, 5);
			int counter = 0;
			do {
				commonLibrary.sleep(5000);
				counter++;
				currProdNew = commonLibrary.isExistNegative(
						UIMAP_WorkFolders.currProd, 5);
			} while (currProdOld.equals(currProdNew) && counter < 40);
		} catch (Exception e1) {
			commonLibrary.sleep(5000);
			System.out.println(e1.toString());
		}
		commonLibrary.sleep(5000);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action
	// # Function Name : clickActionDropdown    
	// # Author : Pratik
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public void clickActionDropdown() {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(
					UIMAP_SearchResult.divider, 20);
			if (divider == null)
				divider = commonLibrary
						.isExist(UIMAP_SearchResult.divider1, 20);
			WebElement hdrresult = commonLibrary.isExist(divider,
					UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(divider,
					UIMAP_SearchResult.divAction, 3);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(50000);
					count++;
					divAction = commonLibrary.isExistNegative(divider,
							UIMAP_SearchResult.divAction, 3);
				} while ((divAction == null || !divAction.isDisplayed())
						&& count < 10);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			if (divAction != null)
				break;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message.
	// # Function Name : verifyBannerMessage
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void verifyBannerMessageDismiss(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg,
				20);
		if (bannermsg != null
				&& bannermsg.getText().toLowerCase()
						.contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text "
					+ text, "Banner message displayed with text " + text,
					Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text "
					+ text,
					"Banner message is not displayed with text " + text,
					Status.FAIL);

		WebElement dismiss = commonLibrary.isExistNegative(bannermsg,
				UIMAP_WorkFolders.dismiss, 10);
		if (dismiss != null && dismiss.isDisplayed()) {
			report.updateTestLog(
					"Verify Dissmiss button is displayed in Banner Message.",
					"Dissmiss button is displayed in Banner Message.",
					Status.PASS);
		} else
			report.updateTestLog(
					"Verify Dissmiss button is displayed in Banner Message.",
					"Dissmiss button is not displayed in Banner Message.",
					Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and verify
	// # Function Name : resetExerciseVerify     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public void resetExerciseVerify(String exerciseName, String para1,
			String para2) {
		boolean flag = false;
		WebElement tableOld = commonLibrary.isExist(
				UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		WebElement table = commonLibrary.isExist(
				UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table,
				UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			WebElement exer = commonLibrary.isExistNegative(row,
					UIMAP_Interactivecitationworkstation.exercisename, 10);
			if (exer == null)
				exer = commonLibrary.isExistNegative(row,
						UIMAP_Interactivecitationworkstation.studentName, 10);
			WebElement link = commonLibrary.isExistNegative(exer,
					UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();
				if (exeName.toLowerCase().contains(exerciseName.toLowerCase())) {
					WebElement action = commonLibrary.isExist(row,
							UIMAP_Interactivecitationworkstation.actionButton,
							10);
					if (action != null)
						commonLibrary.clickLinkWithWebElementWithWait(action,
								"Action");
					WebElement reset = commonLibrary.isExist(row,
							UIMAP_Interactivecitationworkstation.resetExercise,
							10);
					commonLibrary.clickLinkWithWebElementWithWait(reset,
							"Reset exercise");
					this.verifyResetDialogBox(para1, para2);
					WebElement confirmReset = commonLibrary
							.isExist(
									UIMAP_Interactivecitationworkstation.comfirmResetExer,
									10);
					commonLibrary.clickLinkWithWebElementWithWait(confirmReset,
							"Confirm Reset exercise");
					flag = true;
					break;
				}
			}
		}
		try {
			tableOld = commonLibrary.isExist(
					UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			WebElement tableNew = commonLibrary.isExist(
					UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			int count = 0;
			do {
				commonLibrary.sleep(20000);
				tableNew = commonLibrary.isExist(
						UIMAP_Interactivecitationworkstation.bluebooktable, 10);
				count++;
			} while (tableNew.equals(tableOld) && count < 15);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (flag) {
			this.verifyExerciseReset(exerciseName);
		}

		else
			report.updateTestLog("Reset the exercise: " + exerciseName,
					exerciseName + " is not reset", Status.FAIL);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and save the name of
	// Exercise that has been Reset
	// # Function Name : resetExerciseSaveName     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public void verifyResetDialogBox(String para1, String para2) {
		WebElement resetDialog = commonLibrary.isExistNegative(
				UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (resetDialog.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Request Reset dialog contains "
					+ para1, "Request Reset dialog contains " + para1,
					Status.PASS);
		} else
			report.updateTestLog("Verify if Request Reset dialog contains "
					+ para1, "Request Reset dialog does not contains " + para1,
					Status.FAIL);

		if (resetDialog.getText().toLowerCase().contains(para2.toLowerCase())) {
			report.updateTestLog("Verify if Reset dialog contains " + para2
					+ " along with professor details.",
					"Reset dialog contains " + para2
							+ " along with professor details.", Status.PASS);
		} else
			report.updateTestLog("Verify if Reset dialog contains " + para2
					+ " along with professor details.",
					"Reset Exercise dialog does not contains " + para2
							+ " along with professor details.", Status.FAIL);

		List<WebElement> buttons = commonLibrary.isExistList(resetDialog,
				UIMAP_Interactivecitationworkstation.input, 10);
		boolean isSubmit = false, isCancel = false;
		for (WebElement item : buttons) {
			if (item.getAttribute("value").equalsIgnoreCase(
					"Reset this Exercise"))
				isSubmit = true;
			if (item.getAttribute("value").equalsIgnoreCase("Cancel"))
				isCancel = true;

			if (isSubmit && isCancel)
				break;
		}

		if (isSubmit && isCancel) {
			report.updateTestLog(
					"Verify Submit Request and Cancel buttons are present.",
					"Submit Request and Cancel buttons are present.",
					Status.PASS);
		} else
			report.updateTestLog(
					"Verify Submit Request and Cancel buttons are present.",
					"Submit Request and Cancel buttons are not present.",
					Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether a particular exercise
	// has been Reset.
	// # Function Name : verifyExerciseReset     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public void verifyExerciseReset(String exerciseName) {
		boolean flag = false;
		WebElement table1 = commonLibrary.isExist(
				UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		if (table1 == null)
			table1 = commonLibrary.isExist(
					UIMAP_Interactivecitationworkstation.studentProgressTable,
					20);
		List<WebElement> rows1 = commonLibrary.isExistList(table1,
				UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row1 : rows1) {
			WebElement exer = commonLibrary.isExistNegative(row1,
					UIMAP_Interactivecitationworkstation.exercisename, 10);
			if (exer == null)
				exer = commonLibrary.isExistNegative(row1,
						UIMAP_Interactivecitationworkstation.studentName, 10);
			WebElement link = commonLibrary.isExistNegative(exer,
					UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();
				if (exeName.toLowerCase().contains(exerciseName.toLowerCase())
						&& row1.getText().toLowerCase()
								.contains("Not Started".toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify if the exercise "
							+ exerciseName + " is reset ", "The exercise "
							+ exerciseName + " is reset", Status.PASS);

					report.updateTestLog("Verify if the exercise "
							+ exerciseName
							+ " is displayed in Exercise Name Coloumn",
							"The exercise " + exerciseName
									+ " is displayed in Exercise Name Coloumn",
							Status.PASS);

					Date currDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
					if (exer.getText().contains(sdf.format(currDate))
							&& exer.getText()
									.toLowerCase()
									.contains("Exercise Reset on".toLowerCase())) {
						report.updateTestLog(
								"Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ",
								"'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ",
								Status.PASS);
					} else
						report.updateTestLog(
								"Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ",
								"'Exercise Reset on MMM DD,YYYY' does not display below the Exercise name in the tabular column ",
								Status.PASS);
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify if the exercise " + exerciseName
					+ " is reset ", "The exercise " + exerciseName
					+ " is not reset", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save settings and close.
	// # Function Name : saveSettingsAndClose     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public void saveSettingsAndClose() {
		WebElement saveChangesButton = commonLibrary.isExistNegative(
				UIMAP_Settings.savechangesbutton, 10);
		if (saveChangesButton != null && saveChangesButton.isEnabled())
			commonLibrary.clickButtonParentWithWait(saveChangesButton,
					"Save Changes");
		else {
			WebElement closeButton = commonLibrary.isExistNegative(
					UIMAP_Settings.btnIdCancelSettChange, 10);
			if (closeButton != null)
				commonLibrary.clickButtonParentWithWait(closeButton, "Close");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify VSA logo
	// # Function Name : verifyVSAlogo    
	// # Author : Nancy
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public void verifyVSAlogo() {
		WebElement prdctSwttcher = commonLibrary.isExist(
				UIMAP_Interactivecitationworkstation.prdctSwtcher, 20);
		WebElement divLogo = commonLibrary.isExist(prdctSwttcher,
				UIMAP_Interactivecitationworkstation.divlogo, 20);
		WebElement imgVSALogo = commonLibrary.isExist(divLogo,
				UIMAP_Interactivecitationworkstation.imgVSALogo, 20);
		if (imgVSALogo != null) {
			String TxtLogo = imgVSALogo.getText();
			if (TxtLogo.contains("Verdict & Settlement Analyzer")) {
				report.updateTestLog(
						"Verifying VSA Logo",
						"The logo LexisAdvance Verdict & Settlement Analyzer appears",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verifying VSA Logo",
						"The logo LexisAdvance Verdict & Settlement Analyzer does not appear",
						Status.FAIL);
			}
		} else {
			report.updateTestLog("Verifying VSA Logo",
					"VSA Logo is not displayed", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source if source is already expanded
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandChildSourceAndClickOption2(String podName,
			String subLink, String childName, String option1) {
		boolean flag = false;
		List<WebElement> taxPods = commonLibrary.isExistList(
				UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod,
					UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				List<WebElement> lists = commonLibrary.isExistList(pod,
						UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					if (item.getText().toLowerCase()
							.contains(subLink.toLowerCase())) {
						WebElement togleBar = commonLibrary.isExist(item,
								By.tagName("div"), 10);
						// WebElement srcColapse =
						// commonLibrary.isExist(togleBar,
						// UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
						WebElement srcExpand = commonLibrary.isExist(togleBar,
								UIMAP_LexisAdvanceTax.sourceExpanded, 10);
						if (srcExpand != null) {
							// commonLibrary.clickButtonParentWithWait(srcColapse,
							// subLink);
							commonLibrary.sleep(10000);
							// List<WebElement> child =
							// commonLibrary.isExistList(srcColapse,
							// UIMAP_LexisAdvanceTax.childList, 10);
							List<WebElement> child = commonLibrary.isExistList(
									item, UIMAP_LexisAdvanceTax.childList, 10);
							for (WebElement item1 : child) {
								if (item1.getText().toLowerCase()
										.contains(childName.toLowerCase())) {
									commonLibrary.clickButtonParentWithWait(
											item1, childName);
									WebElement dropmenu = commonLibrary
											.isExist(
													UIMAP_LexisAdvanceTax.dropmenu,
													10);
									/*
									 * if (dropmenu != null) { List<WebElement>
									 * buttons =
									 * commonLibrary.isExistList(dropmenu,
									 * By.tagName("button"), 10); for
									 * (WebElement btn : buttons) {
									 * if(btn.isEnabled() &&
									 * btn.getText().toLowerCase
									 * ().equalsIgnoreCase(option2)) {
									 * commonLibrary
									 * .clickButtonParentWithWait(btn, option2);
									 * WebElement deltePopup =
									 * commonLibrary.isExist
									 * (UIMAP_LexisAdvanceTax.deleteDialogPopUp,
									 * 10); WebElement btnContinue =
									 * commonLibrary.isExist(deltePopup,
									 * UIMAP_LexisAdvanceTax.btncontiune, 10);
									 * if(btnContinue!=null) {
									 * commonLibrary.clickButtonParentWithWait
									 * (btnContinue, btnContinue.getText());
									 * break; } } } if (flag) break; }
									 */
									commonLibrary.sleep(10000);
									if (dropmenu != null) {
										List<WebElement> butons = commonLibrary
												.isExistList(dropmenu,
														By.tagName("button"),
														10);
										for (WebElement bttn : butons) {
											if (bttn.isEnabled()
													&& bttn.getText()
															.toLowerCase()
															.contains(
																	option1.toLowerCase())) {
												commonLibrary
														.clickButtonParentWithWait(
																bttn, option1);
												flag = true;
												break;
											}
										}
									}
									if (flag)
										break;
								}

							}
						}
						if (flag)
							break;
					}
					if (flag)
						break;
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.updateTestLog("Select " + option1 + " under source "
					+ subLink + " inside pod " + podName + "", option1
					+ " is selected under source " + subLink + " inside pod "
					+ podName, Status.PASS);
		} else {
			report.updateTestLog("Select " + option1 + " under source "
					+ subLink + " inside pod " + podName + "", option1
					+ " is not selected under source " + subLink
					+ " inside pod " + podName, Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click Results Link
	// # Function Name : clickResultslist
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public void clickResultLink() {
		WebElement resultlistTab = commonLibrary.isExistNegative(
				UIMAP_Document.resultlistlatnews, 10);
		if (resultlistTab != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(resultlistTab, "Result List");
			else
				commonLibrary.clickLinkWithWebElementWithWait(resultlistTab,
						"Result List");
		} else
			report.updateTestLog("Click Results List Button",
					"Results List Button is not available", Status.FAIL);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application with
	// invalid password
	// # Function Name : Logininvalid    
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public void logininvalid() {

		String strUserName = dataTable.getData("General_Data", "Username");
		String strPassword = dataTable.getData("General_Data", "Password");

		// Modified by Pratik, logout functionality has been added to
		// invokeApplication function.
		driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
		WebElement eltUserName = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
						+ "']"), 20);
		if (eltUserName == null) {

			eltUserName = commonLibrary.isExist(
					By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername
							+ "']"), 20);
		}

		WebElement eltPassword = commonLibrary.isExist(
				By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword
						+ "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='"
				+ UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By
				.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		// Enter User Name
		if (eltUserName != null) {

			commonLibrary.clickJSNoLog(eltUserName);
			commonLibrary
					.setDataInTextBox(eltUserName, strUserName, "UserName");
			if (!eltUserName.getAttribute("value").equals(strUserName)) {
				commonLibrary.setDataInTextBox(eltUserName, strUserName,
						"UserName");
				if (!eltUserName.getAttribute("value").equals(strUserName)) {
					throw new FrameworkException("Verify UserName is entered",
							"UserName is not entered");
				}
			}

			// Enter Password
			// eltPassword.click();Previously Used
			commonLibrary.clickJSNoLog(eltPassword);
			commonLibrary
					.setDataInTextBox(eltPassword, strPassword, "Password");
			if (eltPassword.getAttribute("value").equals("")) {
				commonLibrary.setDataInTextBox(eltPassword, strPassword,
						"Password");
				if (eltPassword.getAttribute("value").equals("")) {
					throw new FrameworkException("Verify Password is entered",
							"Password is not entered");
				}
			}

			// Click on signIn button
			commonLibrary.setCheckBox(eltRememberMe, false);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSignIn, "SignIn");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");
			}
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
			WebElement errormsg = commonLibrary.isExist(UIMAP_SignIn.errormsg,
					10);
			if (errormsg != null) {
				report.updateTestLog("Verify the error message is displayed",
						"Error Message is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify the error message is displayed",
						"Error Message is not displayed", Status.FAIL);
			}

			// wait till home page is displayed
			/*
			 * WebElement eltSearchbox =
			 * commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20); int counter =
			 * 0; do { counter = counter + 1; eltSearchbox =
			 * commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20); if
			 * (eltSearchbox == null) commonLibrary.sleep(5000); } while
			 * (eltSearchbox == null && counter <= 40);
			 */

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
			// if (browsername.contains("chrome"))
			// DocumentState(driver);

			// if (browsername.contains("ie8")) {
			// sleepWait(1000);
			// PageCheck.checkAlert(driver);
			// }
			// final PageCheck pageCheck = new PageCheck(scriptHelper);
			// check = pageCheck.PositiveCheck(driver, "More", "Home Page");

			// pageCheck.handleFlow(driver, check);

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the password
	// # Function Name : setNewPassword
	// # Author : Banu
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public void setNewPassword(String strId, String strAnswer,
			String strPassword) {
		// Select New Password
		WebElement rdopassword = commonLibrary
				.isExist(UIMAP_SignIn.rdopassword);
		if (rdopassword != null) {

			commonLibrary.setRadioButton(UIMAP_SignIn.rdopassword);
		} else
			report.updateTestLog("Selecting Forgot Password",
					"Forgot Password Option Radio button not found.",
					Status.FAIL);

		// ENTER A <<<>>> Password IN TEXT BOX
		WebElement txtId = commonLibrary.isExist(UIMAP_SignIn.txtId);
		if (txtId != null) {
			commonLibrary.setDataInTextBox(txtId, strId, "Id");

		} else
			report.updateTestLog("Enter the ID", "Id can not be set.",
					Status.FAIL);
		// Click Submit button
		WebElement btnSubmit = commonLibrary.isExist(UIMAP_SignIn.btnSubmit);
		if (btnSubmit != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSubmit, "Submit");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSubmit, "Submit");
			}

		} else
			report.updateTestLog("Clicking on Submit",
					"Submit can not be clicked.", Status.FAIL);
		// ENTER A <<<>>> Security Answer IN TEXT BOX
		WebElement txtAnswer = commonLibrary.isExist(UIMAP_SignIn.txtAnswer);
		if (txtAnswer != null) {
			commonLibrary.setDataInTextBox(txtAnswer, strAnswer, "Answer");

		} else
			report.updateTestLog("Enter the Security Answer",
					"Security Answer is not entered.", Status.FAIL);
		// Click Submit button
		WebElement btnSubmit1 = commonLibrary.isExist(UIMAP_SignIn.btnSubmit1);
		if (btnSubmit1 != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSubmit1, "Submit");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSubmit1, "Submit");
			}

		} else
			report.updateTestLog("Clicking on Submit",
					"Submit can not be clicked.", Status.FAIL);
		// ENTER A <<<>>> New Password IN TEXT BOX
		WebElement txtPassword = commonLibrary
				.isExist(UIMAP_SignIn.txtPassword);
		WebElement txtCoPassword = commonLibrary
				.isExist(UIMAP_SignIn.txtCoPassword);
		if (txtPassword != null && txtCoPassword != null) {
			commonLibrary
					.setDataInTextBox(txtPassword, strPassword, "Password");
			commonLibrary.setDataInTextBox(txtCoPassword, strPassword,
					"Password");

		} else
			report.updateTestLog("Enter the Password and confirm Password ",
					"Password and confirm Password is not entered.",
					Status.FAIL);
		// Click Submit button
		WebElement btnSubmit2 = commonLibrary.isExist(UIMAP_SignIn.btnSubmit2);
		if (btnSubmit2 != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSubmit2, "Submit");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSubmit2, "Submit");
			}

		} else
			report.updateTestLog("Clicking on Submit",
					"Submit can not be clicked.", Status.FAIL);
		WebElement successfull = commonLibrary.isExist(UIMAP_SignIn.successful);
		if (successfull != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary
						.clickButtonParentWithWaitJS(successfull, "Submit");
			} else {
				commonLibrary.clickButtonParentWithWait(successfull, "Submit");
			}

		} else
			report.updateTestLog("Signin with a new password is clicked ",
					"Signin with a new password is not clicked .", Status.FAIL);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink1     
	// # Author :
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickDocLinkAlone(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(
					UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(
						UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass,
						UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(
						resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary
								.isExistList(list, UIMAP_SearchResult.listtag,
										20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(
									document, UIMAP_SearchResult.TitleClassDoc,
									2);
							if (eleDocTitle != null
									&& eleDocTitle
											.getText()
											.toLowerCase()
											.replaceAll("[^a-zA-Z0-9]", "")
											.trim()
											.equals(strDocTitle
													.toLowerCase()
													.replaceAll("[^a-zA-Z0-9]",
															"").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(
										eleDocTitle, UIMAP_SearchResult.atag,
										20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary
											.clickLinkWithWebElementWithWait(
													lnkDocument,
													lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
			}
			if (blnFlag)
				break;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message and click on
	// Dismiss button.
	// # Function Name : verifyBannerMessageClickDismiss
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void verifyBannerMessageClickDismiss(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg,
				20);
		if (bannermsg != null
				&& bannermsg.getText().toLowerCase()
						.contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text "
					+ text, "Banner message displayed with text " + text,
					Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text "
					+ text,
					"Banner message is not displayed with text " + text,
					Status.FAIL);

		WebElement dismiss = commonLibrary.isExistNegative(bannermsg,
				UIMAP_WorkFolders.dismiss, 10);
		if (dismiss != null && dismiss.isDisplayed()) {
			commonLibrary.clickButtonLogSmallWait(dismiss, "Dismiss");
		} else
			report.updateTestLog(
					"Verify Dissmiss button is displayed in Banner Message.",
					"Dissmiss button is not displayed in Banner Message.",
					Status.FAIL);

	}
	// #*****************************************************************************************************************************
		// # Function Description : Function to navigate to Settings page
		// # Function Name : navigateToSettings
		// # Author : Toya
		// # Date Created : Feb'16
		// #*****************************************************************************************************************************

		public void navigateToSettings1() {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
			if (btnMore != null) {
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}


			WebElement lnkSettings = commonLibrary.isExist(
					UIMAP_Home.lnkTextSettings, 10);

			if (lnkSettings == null || !lnkSettings.isDisplayed()) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null) {
					// commonLibrary.highlightElement(btnMore);
					if ((browsername.contains("internet")))
						commonLibrary.clickButtonParentWithWait(btnMore, "More");
					else
						commonLibrary.clickLinkWithWebElementWithWait(lnkSettings,
								"Settings");
				}
				lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);
			}

			if (lnkSettings != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkSettings,
						"Settings");

			// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

			WebElement PgTitle = commonLibrary.isExist(
					UIMAP_Settings.PgTitleSettings, 10);
			if (PgTitle != null) {
				report.updateTestLog("Verify Settings Page is displayed",
						"Settings Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Settings Page is displayed",
						"Settings Page is NOT displayed", Status.FAIL);

			}
			int count = 0;
			WebElement btnGeneral = commonLibrary.isExistNegative(
					UIMAP_Settings.btnIdGeneral, 10);

			do {
				commonLibrary.sleep(10000);
				btnGeneral = commonLibrary.isExistNegative(
						UIMAP_Settings.btnIdGeneral, 5);
				count++;
			} while (btnGeneral != null && count < 15);

			if (btnGeneral != null
					&& btnGeneral.getAttribute("class").toLowerCase()
							.contains("active")) {
				report.updateTestLog(
						"Verify 'General' settings section displayed by default",
						"'General' settings section displayed by default",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verify 'General' settings section displayed by default",
						"'General' settings section NOT displayed by default",
						Status.WARNING);
				if (btnGeneral != null)
					commonLibrary.clickButtonParentWithWait(btnGeneral,
							"General Tab");
			}

		}

}
