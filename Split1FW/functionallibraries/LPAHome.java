package functionallibraries;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPABrowse;
import UIMAP.UIMAP_LPAHome;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.*;

public class LPAHome extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	public static int check = 0;
	public static int val = 0;
	PageCheck pageCheck = new PageCheck(scriptHelper);

	// private Home home = new Home(scriptHelper);
	public LPAHome(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpa"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lpa") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpa")) {
			throw new IllegalStateException("LPA Home page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting LPA Filter menu items
	// # Function Name : LPAFilterMenuSelection     
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAHome applyLPASearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");

		Boolean blnFlag = false;
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}

		}
		if (strToolbarMenuName.equals("Start In"))
			report.updateTestLog("Verify that the 'Start in' option is not displayed in the filters pop up any longer", "'Start in' option is not displayed in the filters pop up any longer", Status.PASS);
		else {
			filterMenuSelection(strToolbarMenuName);

			switch (strToolbarMenuName) {

			case "Category": {
				blnFlag = false;
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_Home.divClassCTFilters, 20);
					if (divClassCTFilters != null) {
						WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									blnFlag = true;
									break;
								}

							}
					}
				}
				break;
			}

			case "Practice Area & Topic": {
				blnFlag = false;
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassLPAPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassLPAPracticeArea, 20);
					if (divClassLPAPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassLPAPracticeArea, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									blnFlag = true;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
				break;
			}
			if (!blnFlag) {
				report.updateTestLog("select check boxes near " + strCondentTypes, "Not select check boxes near " + strCondentTypes, Status.FAIL);
			}

			pageCheck.documentState(driver);

		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the LPAhome page is displayed
	// # Function Name : VerifySignInPage     
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAHome verifyLPAHomePage() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtLPAHomeTitleMsg)) {
			report.updateTestLog("Verify LPA Landing Page", "LPA Landing Page is displayed", Status.PASS);
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the prefilter popup
	// # Function Name : LPA closePreFilterPopUp     
	// # Author : Ram Prasath
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAHome closePreFilterPopUp() {
		WebElement btnClosePreFilterPopup = commonLibrary.isExistNegative(UIMAP_Home.btnClosePreFilterPopup, 10);
		commonLibrary.clickButtonLogSmallWait(btnClosePreFilterPopup, "Close PreFilter PopUp");
		WebElement preFiltersDiv = commonLibrary.isExistNegative(UIMAP_Home.preFiltersDiv, 10);
		if (preFiltersDiv == null)
			report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is closed.", Status.PASS);
		else
			report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is not closed.", Status.FAIL);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : LPA FilterMenuSelection     
	// # Author : Ram Prasath
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void filterMenuSelection(String strMenuName) {

		try {
			WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				List<WebElement> lstButtons = commonLibrary.isExistList(mnuFilterToolBar, UIMAP_Home.btnIdFilterMenu, 20);
				if (lstButtons.size() > 0)
					for (WebElement button : lstButtons) {
						if (button.getText().contains(strMenuName)) {
							if (browsername.contains("internet")) {
								commonLibrary.clickButtonParentWithWaitJS(button, strMenuName);
							} else {
								commonLibrary.clickButtonParentWithWait(button, strMenuName);
							}

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
	// # Function Description : Function for selecting Parent and child topics
	// under practice Area
	// # Function Name : selectParentandChildTopicsPracticeAreaLink     
	// # Author : Ram Prasath
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPABrowse selectParentandChildTopicsPracticeAreaLink(String topicTabNavigation, String parentTopicName, String childTopicName, String strPageHeader, String linktest) {
		try {

			WebElement btnSelectPracticeArea = commonLibrary.isExist(UIMAP_Home.btnSelectPracticeArea);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSelectPracticeArea, btnSelectPracticeArea.getText());
			} else {
				commonLibrary.clickButtonParentWithWait(btnSelectPracticeArea, btnSelectPracticeArea.getText());
			}

			if (topicTabNavigation.equals("Topic Navigation")) {
				WebElement lpaPracticeTabNav = commonLibrary.isExist(UIMAP_Home.lpaPracticeTabNav);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(lpaPracticeTabNav, lpaPracticeTabNav.getText());
				} else {
					commonLibrary.clickButtonParentWithWait(lpaPracticeTabNav, lpaPracticeTabNav.getText());
				}

				WebElement lpaPracticeParentTopics = commonLibrary.isExist(UIMAP_Home.lpaPracticeParentTopics, 20);
				WebElement lpaPracticeChildTopics = commonLibrary.isExist(UIMAP_Home.lpaPracticeChildTopics, 20);

				selectFromListLink(lpaPracticeParentTopics, parentTopicName);
				selectFromListLink(lpaPracticeChildTopics, childTopicName);
				WebElement lpaPracticeSubTopics = commonLibrary.isExist(UIMAP_Home.lpaPracticeSubTopics, 20);
				try {
					selectFromListLink(lpaPracticeSubTopics, linktest);
				} catch (Exception e) {

				}
				WebElement txtPageHeading = commonLibrary.isExist(UIMAP_Home.txtPracticeAreaHeading);
				if (txtPageHeading.getText().toLowerCase().contains(linktest.toLowerCase())) {
					report.updateTestLog("Verify " + linktest + " is displayed", linktest + " is displayed", Status.PASS);

				} else {
					report.updateTestLog("Verify " + linktest + " is displayed", linktest + " is not displayed", Status.FAIL);

				}

			} else if (topicTabNavigation.equals("Home Pages")) {

				if (parentTopicName.equals("Banking & Finance")) {
					WebElement lnkBankingAndFinance = commonLibrary.isExist(UIMAP_Home.lnkBankingAndFinance);
					commonLibrary.clickButton(lnkBankingAndFinance);
					WebElement txtPageHeading = commonLibrary.isExist(UIMAP_Home.txtPracticeAreaHeading);
					if (txtPageHeading.getText().toLowerCase().contains(parentTopicName.toLowerCase())) {
						report.updateTestLog("Verify " + parentTopicName + " is displayed", parentTopicName + " is displayed", Status.PASS);

					} else {
						report.updateTestLog("Verify " + parentTopicName + " is displayed", parentTopicName + " is not displayed", Status.FAIL);

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		pageCheck.documentState(driver);
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting links under LPApractice
	// Area
	// # Function Name : select_FromList_Link   
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void selectFromListLink(WebElement element, String strValue) {

		List<WebElement> listItems = commonLibrary.isExistList(element, By.tagName("li"), 20);
		for (WebElement item : listItems) {
			List<WebElement> btn = commonLibrary.isExistList(item, By.tagName("a"), 20);
			for (WebElement item2 : btn) {
				if (item2.getText().equalsIgnoreCase(strValue)) {
					try {
						if (browsername.contains("internet")) {
							commonLibrary.clickLinkWithWebElementWithWaitJS(item2, strValue);
						} else {
							commonLibrary.clickLinkWithWebElementWithWait(item2, strValue);
						}

					} catch (Exception e) {

					}
					break;
				}
			}

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : SimpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing search in LPA page.
	// # Function Name : LPAsearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LPAResults lpaSearch(String strSearchTerm, Boolean strClearFilter) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");

		if (strClearFilter) {

			WebElement filterIdCount = commonLibrary.isExistNegative(UIMAP_Home.FilterIdCount, 2);
			if (filterIdCount != null) {
				WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
				// commonLibrary.highlightElement(btnClassFilter);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(btnClassFilter, "Filter");
				} else {
					commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
				}

				WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
				// commonLibrary.highlightElement(btnClassClearFilter);
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(btnClassClearFilter, "Clear");
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");
				}

				WebElement btnIdSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);
				// commonLibrary.ScrollToView(btnIdSubSearch);
				// commonLibrary.highlightElement(btnIdSubSearch);
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(btnIdSubSearch, "Search");
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
				}

			} else {
				WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
				// commonLibrary.highlightElement(eltSearchbutton);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(eltSearchbutton, "Search");
				} else {
					commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
				}

			}
		} else {
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(eltSearchbutton, "Search");
			} else {
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
			}

		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Practice Area pages
	// # Function Name : clickPracticeAreaPage     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPAHome clickPracticeAreaPage(String pageName) {
		commonLibrary.sleep(5000);
		WebElement practiceAreaNav = commonLibrary.isExist(UIMAP_LPAHome.practiceAreaNav, 10);
		commonLibrary.clickButtonParent(practiceAreaNav, "Practice Areas");
		commonLibrary.sleep(5000);
		WebElement childMenu = commonLibrary.isExist(UIMAP_LPAHome.childMenu, 10);
		WebElement content = commonLibrary.isExist(childMenu, UIMAP_LPAHome.homePageCont, 10);
		List<WebElement> pages = commonLibrary.isExistList(content, By.tagName("li"), 20);
		for (WebElement item : pages) {
			if (item.getText().equalsIgnoreCase(pageName)) {
				WebElement pageLink = commonLibrary.isExist(item, By.tagName("a"), 10);

				commonLibrary.clickMethod(pageLink, pageName);
				commonLibrary.sleep(10000);
				break;
			}
		}
		WebElement pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
		int counter = 0;
		do {
			counter = counter + 1;
			pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
			if (pageHeader == null)
				commonLibrary.sleep(5000);
		} while (pageHeader == null && counter <= 40);

		pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
		if (pageHeader.getText().contains(pageName))
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		else
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod presence
	// # Function Name : verifyPod     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPAHome verifyPod(String podName, String podContent, boolean exist) {
		WebElement pod = null;
		switch (podName) {
		case "Updates": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.updatesPod, 10);
			break;
		}
		case "Knowledge": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.knowledgeMosaicPod, 10);
			break;
		}
		case "Recent": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.recentPod, 10);
			break;
		}
		case "Law360": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.law360Pod, 10);
			break;
		}
		case "EditorPick": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.editorPod, 10);
			break;
		}
		case "MeetAuthor": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.meetAuthPod, 10);
			break;
		}
		case "Market": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.meetAuthPod, 10);
			break;
		}
		case "CSC": {
			pod = commonLibrary.isExist(UIMAP_LPAHome.cscPod, 10);
			break;
		}
		}
		WebElement name = null;
		if (pod != null) {
			name = commonLibrary.isExist(pod, By.tagName("h2"), 10);
			if (name.getText().contains(podContent))
				report.updateTestLog("Verify the pod " + podContent + " is present", "The pod " + podContent + " is displayed", Status.PASS);
		} else if (exist)
			report.updateTestLog("Verify the pod " + podContent + " is present", "The pod " + podContent + " is not present", Status.FAIL);
		else
			report.updateTestLog("Verify the pod " + podContent + " is present", "The pod " + podContent + " is not present", Status.PASS);

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout of the application.
	// # Function Name : logout     
	// # Author : Uma
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExistNegative(By.linkText(UIMAP_Home.lnkTextSignOut),
		// 15);
		// if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
		// btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// }
		// if (lnkSignOut != null) {
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
		// }
		//
		// WebElement btnIdLogin =
		// commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		// if (btnIdLogin != null &&
		// driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg))
		// {
		// report.updateTestLog("Verify Logout",
		// "Sign In to Lexis Advance screen is displayed", Status.PASS);
		// } else {
		// report.updateTestLog("Verify Logout",
		// "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		// }

		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on LPA product logo
	// # Function Name : clickProductLogoLPA
	// # Author : Anbarasan
	// # Date Created : July'2015
	// #*****************************************************************************************************************************

	public LPAHome clickProductLogoLPA() {
		WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		WebElement practiceAreaNav = commonLibrary.isExist(UIMAP_LPAHome.practiceAreaNav, 10);
		if (currentProduct != null && practiceAreaNav != null)
			commonLibrary.clickLinkWithWebElement(currentProduct, "LPA logo");
		else
			report.updateTestLog("Click on LPA product logo", "LPA is not the current product", Status.FAIL);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : lpaSimpleSearch     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public LPAResults lpaSimpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new LPAResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title
	// # Function Name : clickDocLink
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickDocLink(String strDocTitle) {
		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		//
		// for (i = 0; i < 3; i++) {
		// if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult,
		// 10) != null)
		// resultClass =
		// commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		//
		// if (resultClass != null) {
		// WebElement OListResult = commonLibrary.isExist(resultClass,
		// By.tagName("ol"), 20);
		// if (OListResult != null) {
		// List<WebElement> OListItems = commonLibrary.isExistList(OListResult,
		// By.tagName("li"), 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().toLowerCase().contains(strDocTitle.toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// }
		// }
		// }
		// }
		// }
		// }
		// if (!blnFlag) {
		// WebElement btnNextPage =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnNextPage, "Next Page");
		// else
		// commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
		// } else
		// break;
		// }
		// if (!blnFlag)
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		// WebElement pgHeader = null;
		// pageCheck.PositiveCheck(driver, "document", "Document");
		//
		// if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC,
		// 10) != null)
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
		// else
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is not displayed",
		// Status.FAIL);
		// }
		generalFunctions.clickDocLink(strDocTitle);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : SimpleSearch1     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults simpleSearch1(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new LPAResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select child topic from parent
	// category
	// # Function Name : SelectChildTopicFromParent     
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LPABrowse selectChildTopicFromParent(String strParent, String strChild) {
		boolean flagName = false;
		WebElement SelectParent = commonLibrary.isExist(UIMAP_LPAHome.Parent_topic, 20);
		List<WebElement> SelectParent1 = commonLibrary.isExistList(SelectParent, UIMAP_LPAHome.topicslist, 20);

		for (WebElement element : SelectParent1) {
			WebElement ParentTitle = commonLibrary.isExist(element, By.tagName("h2"), 20);
			if (ParentTitle != null) {
				if (ParentTitle.getText().contains(strParent)) {
					List<WebElement> SelectChild = commonLibrary.isExistList(element, UIMAP_LPAHome.child_topic, 20);
					for (WebElement child : SelectChild) {
						if (child != null) {
							if (child.getText().contains(strChild)) {
								commonLibrary.clickJS(child);
								flagName = true;

								WebElement title = commonLibrary.isExist(UIMAP_LPABrowse.browseTitle, 10);

								int counter = 0;
								do {
									counter = counter + 1;
									title = commonLibrary.isExist(UIMAP_LPABrowse.browseTitle, 20);
									if (title == null)
										commonLibrary.sleep(5000);
								} while (title == null && counter <= 40);
							}
						}
						if (flagName) {
							break;
						}

					}
					if (flagName) {
						break;
					}
				}
			}
		}
		return new LPABrowse(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome swithToCondentType(String strCondentType) {
		Boolean flg = false;
		for (int i = 0; i < 3; i++) {
			if (generalFunctions.selectCondentType(strCondentType) != 1) {
				if (generalFunctions.selectCondentType(strCondentType) == 1) {
					flg = true;
					break;
				}
			} else {
				flg = true;
				break;
			}
		}
		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Alternate Clause Icon
	// # Function Name : clickAlterClauseIcon     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickAlterClauseIconUnderSection(String strSection) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.sectionSpan, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					report.updateTestLog("Verify section name " + strSection, strSection + " present", Status.PASS);
					WebElement alterClauseImg = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.alternateClauseImg, 10);
					if (alterClauseImg != null) {
						commonLibrary.clickButtonParentWithWait(alterClauseImg, "Alternate Clause");
						report.updateTestLog("Verify Alternate Clause popup displays", "Alternate Clause popup displays.", Status.PASS);
						break;
					}
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Button Inside Popup
	// # Function Name : clickButtonInsidePopup     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickBtnPopupPerformActions(String strSection, String clickWht) {
		String addToForm = "Add to form";
		WebElement popupAltClause = commonLibrary.isExistNegative(UIMAP_LPAHome.popupAltClause, 20);
		if (popupAltClause != null) {
			if (clickWht.toLowerCase().equals(addToForm.toLowerCase())) {
				WebElement btnAddToForm = commonLibrary.isExistNegative(popupAltClause, UIMAP_LPAHome.btnAddToForm, 10);
				if (btnAddToForm != null) {
					commonLibrary.clickButtonParentWithWait(btnAddToForm, "Add To Form");
					report.updateTestLog("Verify Added 'Alternate Clause' displays below 'original clause'", "Added 'Alternate Clause' displays below 'original clause'.", Status.PASS);
				}
			} else {
				WebElement btnAddYourOwnClauseText = commonLibrary.isExistNegative(popupAltClause, UIMAP_LPAHome.btnAddYourOwnClauseText, 10);
				if (btnAddYourOwnClauseText != null) {
					commonLibrary.clickButtonParentWithWait(btnAddYourOwnClauseText, "Add your own clause text");
					report.updateTestLog("Verify 'Add your own clause text' box displays", "'Add your own clause text' box displays.", Status.PASS);
				}
				commonLibrary.sleep(2000);
				driver.switchTo().frame(driver.findElement(By.id("userclausecontent_ifr")));
				Robot robot;
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					robot = null;
				} catch (AWTException e) {
					e.printStackTrace();
				}
				commonLibrary.sleep(2000);
				driver.switchTo().defaultContent();
				WebElement addContentText = commonLibrary.isExistNegative(UIMAP_LPAHome.addContentTextAltClause, 20);
				if (addContentText != null) {
					commonLibrary.clickButtonParentWithWait(addContentText, "Add Text");
					report.updateTestLog("Verify 'Added clause' displays below the Alternate clause with tilte as 'User Added Clause'", "'Added clause' displays below the Alternate clause with tilte as 'User Added Clause'.", Status.PASS);
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to double Click And Select Options
	// # Function Name : doubleClickAndSelectOptions     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome doubleClickAndSelectOptions(String strSection) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.sectionSpan, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					Actions action = new Actions(driver);
					action.doubleClick(sectionSpan).perform();
					WebElement copySelectedTextBtn = commonLibrary.isExistNegative(UIMAP_LPAHome.copySelectedTextBtn, 20);
					if (copySelectedTextBtn != null) {
						commonLibrary.clickButtonParentWithWait(copySelectedTextBtn, copySelectedTextBtn.getText());
					}
					Robot robot;
					try {
						robot = new Robot();
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_C);
						robot.keyRelease(KeyEvent.VK_C);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						robot = null;
					} catch (AWTException e) {
						e.printStackTrace();
					}
					WebElement citationCloseButton = commonLibrary.isExistNegative(UIMAP_LPAHome.citationCloseButton, 20);
					if (citationCloseButton != null) {
						commonLibrary.clickButtonParentWithWait(citationCloseButton, "Close");
					}
					break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify X Edit
	// # Function Name : verifyXEdit     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyXEdit(String strSection) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.sectionSpan, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					WebElement xImg = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.xImg, 10);
					WebElement editAnchor = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.editAnchor, 10);
					if (xImg != null && editAnchor != null) {
						report.updateTestLog("Verify 'X' mark displays before the 'User Added Clause' in Alternate Clause section", "'X' mark displays before the 'User Added Clause' in Alternate Clause section.", Status.PASS);
						report.updateTestLog("Verify 'Edit' button displays in the right side of 'User Added Clause' section", "'Edit' button displays in the right side of 'User Added Clause' section.", Status.PASS);
					}
					break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify X Edit
	// # Function Name : verifyXEdit     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickEditBtnAndChangeContent(String strSection, String content) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.sectionSpan, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					WebElement editAnchor = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.editAnchor, 10);
					if (editAnchor != null) {
						commonLibrary.clickLinkWithWebElementWithWait(editAnchor, "Edit");
						report.updateTestLog("Verify Add your own clause box displays", "Add your own clause box displays.", Status.PASS);
						commonLibrary.sleep(10000);
						commonLibrary.sleep(10000);
						driver.switchTo().frame(driver.findElement(By.id("userclausecontent_ifr")));
						WebElement editable = driver.switchTo().activeElement();
						if (editable != null) {
							Robot robot;
							try {
								robot = new Robot();
								robot.keyPress(KeyEvent.VK_CONTROL);
								robot.keyPress(KeyEvent.VK_A);
								robot.keyRelease(KeyEvent.VK_A);
								robot.keyRelease(KeyEvent.VK_CONTROL);
								robot = null;
							} catch (AWTException e) {
								e.printStackTrace();
							}
							editable.sendKeys(content);
							report.updateTestLog("Verify 'Testing_Clause' displays below the Alternate clause with tilte as 'User Added Clause'", "'Testing_Clause' displays below the Alternate clause with tilte as 'User Added Clause'.", Status.PASS);
						}
						driver.switchTo().defaultContent();
						WebElement saveClauseTxt = commonLibrary.isExistNegative(UIMAP_LPAHome.saveClauseTxt, 20);
						if (saveClauseTxt != null)
							commonLibrary.clickButtonParentWithWait(saveClauseTxt, "Save");
					}
					break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder and verify
	// # Function Name : addToFolderAndVerify
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Document addToFolderAndVerify(String strSection, String folderName, String targetTxt) {
		try {
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)
				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, folderName);
				commonLibrary.sleep(3000);
			}

			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");
			}

			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			}
			commonLibrary.sleep(5000);
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		commonLibrary.sleep(100000);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		String openForm = "Open Form";
		List<WebElement> allAnchors = commonLibrary.isExistList(UIMAP_LPAHome.a, 20);
		if (allAnchors.size() > 0) {
			for (int i = 0; i < allAnchors.size(); i++) {
				if (allAnchors.get(i) != null && allAnchors.get(i).getText().toLowerCase().contains(openForm.toLowerCase())) {
					commonLibrary.clickLinkWithWebElementWithWait(allAnchors.get(i), allAnchors.get(i).getText());
					List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
					if (divClause.size() > 0) {
						for (int j = 0; j < divClause.size(); j++) {
							WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(j), UIMAP_LPAHome.sectionSpan, 10);
							if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
								List<WebElement> allParas = commonLibrary.isExistList(divClause.get(j), UIMAP_LPAHome.p, 20);
								if (allParas.size() > 0) {
									for (int k = 0; k < allParas.size(); k++) {
										if (allParas.get(k) != null && allParas.get(k).getText().toLowerCase().contains(targetTxt.toLowerCase())) {
											report.updateTestLog("Verify 'Testing_Clause' displays below the Alternate clause with tilte as 'User Added Clause'", "'Testing_Clause' displays below the Alternate clause with tilte as 'User Added Clause'.", Status.PASS);
											break;
										}
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Target Txt Under Section
	// # Function Name : verifyTargetTxtUnderSection
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyTargetTxtUnderSection(String strSection, String targetTxt) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_LPAHome.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_LPAHome.sectionSpan, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					List<WebElement> allParas = commonLibrary.isExistList(UIMAP_LPAHome.p, 20);
					if (allParas.size() > 0) {
						for (int k = 0; k < allParas.size(); k++) {
							if (!(allParas.get(i) != null && allParas.get(i).getText().toLowerCase().contains(targetTxt.toLowerCase()))) {
								report.updateTestLog("Verify 'Testing_Clause' displays below the Alternate clause with tilte as 'User Added Clause'", "'Testing_Clause' doesnot displays below the Alternate clause with tilte as 'User Added Clause'.", Status.PASS);
								break;
							}
						}
					}
					break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome simpleSearchLPA(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Condent Type
	// # Function Name : verifyCondentType     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyCondentType(String condentTypeToVerify) {
		Boolean flg = false;
		WebElement ulCondentSwitcher = commonLibrary.isExistNegative(UIMAP_SearchResult.ulCondentSwitcher, 10);
		if (ulCondentSwitcher != null) {
			List<WebElement> allList = commonLibrary.isExistList(ulCondentSwitcher, UIMAP_LPAHome.actionList, 20);
			if (allList.size() > 0) {
				for (int i = 0; i < allList.size(); i++) {
					if (allList.get(i).getText() != null && allList.get(i).getText().toLowerCase().contains(condentTypeToVerify.toLowerCase())) {
						report.updateTestLog("Verify resultlist displays with category '" + condentTypeToVerify + "' is selected", "Resultlist displays with category '" + condentTypeToVerify + "' is selected", Status.PASS);
						flg = true;
						break;
					} else {
						WebElement btnCondentType = commonLibrary.isExist(allList.get(i), By.tagName("button"), 20);
						if (btnCondentType == null)
							btnCondentType = commonLibrary.isExist(allList.get(i), By.tagName("span"), 20);
						if (btnCondentType.getText().toLowerCase().contains(condentTypeToVerify.toLowerCase())) {
							report.updateTestLog("Verify resultlist displays with category '" + condentTypeToVerify + "' is selected", "Resultlist displays with category '" + condentTypeToVerify + "' is selected", Status.PASS);
							flg = true;
							break;
						}
					}
					if (flg)
						break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Landing Page LPA
	// # Function Name : verifyLandingPageLPA     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyLandingPageLPA(String strLandingPage) {
		WebElement landingPgHdr = commonLibrary.isExistNegative(UIMAP_LPAHome.HeaderResults, 10);
		if (landingPgHdr != null) {
			WebElement tagNameH = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
			if (tagNameH != null && tagNameH.getText().toLowerCase().contains(strLandingPage.toLowerCase()))
				report.updateTestLog("Verify '" + strLandingPage + "' landing page displays", "'" + strLandingPage + "' landing page displays", Status.PASS);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Topics
	// # Function Name : clickLinkInTopics     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickLinkInTopics(String topic, String strLink, String startIn) {
		boolean flg = false;
		WebElement topicsTab = commonLibrary.isExistNegative(UIMAP_LPAHome.topicsTab, 10);
		if (topicsTab != null) {
			List<WebElement> allULList = commonLibrary.isExistList(topicsTab, UIMAP_LPAHome.ul, 20);
			if (allULList.size() > 0) {
				for (int i = 0; i < allULList.size(); i++) {
					List<WebElement> allLIList = commonLibrary.isExistList(allULList.get(i), UIMAP_LPAHome.actionList, 20);
					for (int j = 0; j < allLIList.size(); j++) {
						WebElement topicHeader = commonLibrary.isExistNegative(allLIList.get(j), UIMAP_LPAHome.topicHeader, 10);
						if (topicHeader != null && topicHeader.getText().toLowerCase().contains(topic.toLowerCase())) {
							List<WebElement> allULListOne = commonLibrary.isExistList(allLIList.get(j), UIMAP_LPAHome.ul, 20);
							if (allULListOne.size() > 0) {
								for (int k = 0; k < allULListOne.size(); k++) {
									List<WebElement> allLIListOne = commonLibrary.isExistList(allULListOne.get(k), UIMAP_LPAHome.actionList, 20);
									for (int l = 0; l < allLIListOne.size(); l++) {
										WebElement link = commonLibrary.isExistNegative(allLIListOne.get(l), UIMAP_LPAHome.a, 10);
										if (link != null && link.getText().toLowerCase().contains(strLink.toLowerCase())) {
											commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
											commonLibrary.sleep(10000);
											commonLibrary.sleep(10000);
											if (!startIn.equals(""))
												verifyCondentType(startIn);
											flg = true;
											break;
										}
										if (flg)
											break;
									}
									if (flg)
										break;
								}
							}
						}
						if (flg)
							break;
					}
					if (flg)
						break;
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Back To Top Arrow
	// # Function Name : verifyBackToTopArrow     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyBackToTopArrow(String isExists) {
		if (isExists.toLowerCase().contains("yes")) {
			WebElement backToTopArrow = commonLibrary.isExistNegative(UIMAP_LPAHome.backToTopArrow, 10);
			if (backToTopArrow != null)
				report.updateTestLog("Verify 'Back To Top' arrow is now displayed at the bottom right corner of the page", "'Back To Top' arrow is now displayed at the bottom right corner of the page", Status.PASS);
		} else {
			report.updateTestLog("Verify 'Back To Top' arrow does not display at the bottom right corner of the page when gloabl menu is in visible region", "'Back To Top' arrow does not display at the bottom right corner of the page when gloabl menu is in visible region", Status.PASS);
		}
		commonLibrary.sleep(5000);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll To Bottom of the page
	// # Function Name : scrollToBottom     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome scrollToBottom() {
		Actions actions = new Actions(driver);
		actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		commonLibrary.sleep(5000);
		commonLibrary.sleep(5000);
		report.updateTestLog("Verify 'Back To Top' arrow display at the bottom right corner of the page when global menu is not visible region", "'Back To Top' arrow display at the bottom right corner of the page when global menu is not visible region", Status.PASS);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Back To Top Arrow
	// # Function Name : clickBackToTopArrow     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickBackToTopArrow() {
		WebElement backToTopArrow = commonLibrary.isExistNegative(UIMAP_LPAHome.backToTopArrow, 10);
		if (backToTopArrow != null) {
			commonLibrary.clickButtonParentWithWait(backToTopArrow, "Back To Top");
			commonLibrary.sleep(5000);
			commonLibrary.sleep(5000);
			report.updateTestLog("Verify page scrolls back to the top of the page", "Page scrolls back to the top of the page", Status.PASS);
		} else {
			Actions actions = new Actions(driver);
			actions.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).perform();
			commonLibrary.sleep(5000);
			commonLibrary.sleep(5000);
			report.updateTestLog("Verify page scrolls back to the top of the page", "Page scrolls back to the top of the page", Status.PASS);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Default
	// # Function Name : verifyPodDefault     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyPodDefault() {
		int countE = 0;
		int countC = 0;
		String contentTypeChk = "Forms;Practice Notes;Briefs, Pleadings & Motions;Cases;Statutes & Legislation;Administrative Codes & Regulations;Secondary Materials;Emerging Issues;Administrative Materials;";
		WebElement resultDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.resultwrapper, 10);
		if (resultDiv != null) {
			List<WebElement> allButtonExpanded = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltExpandedFilterHeader, 20);
			List<WebElement> allButtonCollapsed = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltCollapsedFilterHeaderOne, 20);
			for (int i = 0; i < allButtonExpanded.size(); i++) {
				if (contentTypeChk.toLowerCase().contains(allButtonExpanded.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonExpanded.get(i).getText().split("\\(")[0].length() - 1)))
					countE++;
			}
			for (int i = 0; i < allButtonCollapsed.size(); i++) {
				if (contentTypeChk.toLowerCase().contains(allButtonCollapsed.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonCollapsed.get(i).getText().split("\\(")[0].length() - 1)))
					countC++;
			}
			if (countE == 4 && countC == 5) {
				report.updateTestLog("Verify first four pods displayed in snapshot view are in expanded view by default", "First four pods displayed in snapshot view are in expanded view by default", Status.PASS);
				report.updateTestLog("Verify remaining pods displayed in snapshot view are 'Collapsed' by default", "Remaining pods displayed in snapshot view are 'Collapsed' by default", Status.PASS);
			}
			report.updateTestLog("Verify 'View More Categories' does not display in snapshot page", "'View More Categories' does not display in snapshot page", Status.PASS);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Jump To And Proceed
	// # Function Name : clickJumpToAndProceed     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickJumpToAndProceed(String clickWht) {
		if (clickWht.equals("")) {
			WebElement jumpToToolbar = commonLibrary.isExistNegative(UIMAP_LPAHome.jumpToToolbar, 10);
			if (jumpToToolbar != null) {
				WebElement jumpToContainer = commonLibrary.isExistNegative(jumpToToolbar, UIMAP_LPAHome.jumpToContainer, 10);
				if (jumpToContainer != null) {
					WebElement jumpTo = commonLibrary.isExistNegative(jumpToContainer, UIMAP_LPAHome.jumpTo1, 10);
					if (jumpTo != null) {
						commonLibrary.clickButtonParentWithWait(jumpTo, "Jump To");
						WebElement jumpToDd = commonLibrary.isExistNegative(jumpToContainer, UIMAP_LPAHome.jumpToDd1, 10);
						if (jumpToDd != null) {
							WebElement jumpToUList = commonLibrary.isExistNegative(jumpToDd, UIMAP_LPAHome.ul, 10);
							if (jumpToUList != null) {
								List<WebElement> allList = commonLibrary.isExistList(jumpToUList, UIMAP_LPAHome.actionList, 20);
								if (allList.size() > 0) {
									for (int i = 0; i < allList.size(); i++) {
										WebElement btnTag = commonLibrary.isExistNegative(allList.get(i), UIMAP_LPAHome.btn, 10);
										if (btnTag != null) {
											report.updateTestLog("Verify '" + btnTag.getText().split("\\(")[0] + "' is displayed with (n) – number of results count", "'" + btnTag.getText() + "' is displayed with (n) – number of results count", Status.PASS);
										}
									}
								}
							}
						}
					}
					commonLibrary.clickButtonParentWithWait(jumpTo, "Jump To");
					commonLibrary.sleep(5000);
				}
			}
		} else {
			Boolean flg = false;
			WebElement jumpToToolbar = commonLibrary.isExistNegative(UIMAP_LPAHome.jumpToToolbar, 10);
			if (jumpToToolbar != null) {
				WebElement jumpToContainer = commonLibrary.isExistNegative(jumpToToolbar, UIMAP_LPAHome.jumpToContainer, 10);
				if (jumpToContainer != null) {
					WebElement jumpTo = commonLibrary.isExistNegative(jumpToContainer, UIMAP_LPAHome.jumpTo1, 10);
					if (jumpTo != null) {
						commonLibrary.clickButtonParentWithWait(jumpTo, "Jump To");
						WebElement jumpToDd = commonLibrary.isExistNegative(jumpToContainer, UIMAP_LPAHome.jumpToDd1, 10);
						if (jumpToDd != null) {
							WebElement jumpToUList = commonLibrary.isExistNegative(jumpToContainer, UIMAP_LPAHome.ul, 10);
							if (jumpToUList != null) {
								List<WebElement> allList = commonLibrary.isExistList(jumpToUList, UIMAP_LPAHome.actionList, 20);
								if (allList.size() > 0) {
									for (int i = 0; i < allList.size(); i++) {
										WebElement btnTag = commonLibrary.isExistNegative(allList.get(i), UIMAP_LPAHome.btn, 10);
										if (btnTag != null && btnTag.getText().toLowerCase().contains(clickWht.toLowerCase())) {
											commonLibrary.clickButtonParentWithWait(btnTag, btnTag.getText());
											commonLibrary.sleep(5000);
											flg = true;
										}
									}
								}
							}
						}
					}
				}
			}
			if (flg) {
				report.updateTestLog("Verify spinner displays inside '" + clickWht + "' pod to load the results", "Spinner displays inside '" + clickWht + "' pod to load the results", Status.PASS);
				report.updateTestLog("Verify result list displays " + clickWht + " as viewable region", "result list displays " + clickWht + " as viewable region", Status.PASS);
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Expanded Collapsed
	// # Function Name : verifyPodExpandedCollapsed     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome verifyPodExpandedCollapsed(String contentType) {
		WebElement resultDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.resultwrapper, 10);
		if (resultDiv != null) {
			List<WebElement> allButtonExpanded = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltExpandedFilterHeader, 20);
			List<WebElement> allButtonCollapsed = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltCollapsedFilterHeaderOne, 20);
			for (int i = 0; i < allButtonExpanded.size(); i++) {
				if (contentType.toLowerCase().contains(allButtonExpanded.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonExpanded.get(i).getText().split("\\(")[0].length() - 1)))
					report.updateTestLog("Verify '" + allButtonExpanded.get(i).getText().split("\n")[0] + "' pods displayed in snapshot view is in expanded view", "'" + allButtonExpanded.get(i).getText().split("\n")[0] + "' pods displayed in snapshot view is in expanded view", Status.PASS);
			}
			for (int i = 0; i < allButtonCollapsed.size(); i++) {
				if (contentType.toLowerCase().contains(allButtonCollapsed.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonCollapsed.get(i).getText().split("\\(")[0].length() - 1)))
					report.updateTestLog("Verify '" + allButtonCollapsed.get(i).getText().split("\n")[0] + "' pods displayed in snapshot view is in collapsed view", "'" + allButtonCollapsed.get(i).getText().split("\n")[0] + "' pods displayed in snapshot view is in collapsed view", Status.PASS);
			}
		}
		commonLibrary.sleep(5000);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Expand Or Collapse Icon From
	// Pod
	// # Function Name : clickExpandOrCollapseIconFromPod     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickExpandOrCollapseIconFromPod(String contentType, String doWht) {
		Boolean flg = false;
		WebElement resultDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.resultwrapper, 10);
		if (doWht.equals("Expand")) {
			if (resultDiv != null) {
				List<WebElement> allButtonCollapsed = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltCollapsedFilterHeaderOne, 20);
				for (int i = 0; i < allButtonCollapsed.size(); i++) {
					if (contentType.toLowerCase().contains(allButtonCollapsed.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonCollapsed.get(i).getText().split("\\(")[0].length() - 1))) {
						commonLibrary.clickButtonParentWithWait(allButtonCollapsed.get(i), allButtonCollapsed.get(i).getText().split("\n")[0]);
						flg = true;
					}
					if (flg)
						break;
				}
				if (flg) {
					report.updateTestLog("Verify spinner displays inside '" + contentType + "' pod to load the results", "Spinner displays inside '" + contentType + "' pod to load the results", Status.PASS);
					report.updateTestLog("Verify '" + contentType + "' expanded", "'" + contentType + "' expanded", Status.PASS);
				}
			}
			commonLibrary.sleep(5000);
		} else {
			if (resultDiv != null) {
				List<WebElement> allButtonCollapsed = commonLibrary.isExistList(resultDiv, UIMAP_SearchResult.eltExpandedFilterHeader, 20);
				for (int i = 0; i < allButtonCollapsed.size(); i++) {
					if (contentType.toLowerCase().contains(allButtonCollapsed.get(i).getText().toLowerCase().split("\\(")[0].substring(0, allButtonCollapsed.get(i).getText().split("\\(")[0].length() - 1))) {
						commonLibrary.clickButtonParentWithWait(allButtonCollapsed.get(i), allButtonCollapsed.get(i).getText().split("\n")[0]);
						flg = true;
					}
					if (flg)
						break;
				}
				if (flg)
					report.updateTestLog("Verify '" + contentType + "' collapses", "'" + contentType + "' collapses", Status.PASS);
			}
			commonLibrary.sleep(5000);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Doc Title From Pod
	// # Function Name : clickDocTitleFromPod     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome clickDocTitleFromPod(String contentType, String docTitle) {
		Boolean flg = false;
		WebElement resultDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.resultwrapper, 10);
		if (docTitle.equals("")) {
			if (contentType.toLowerCase().contains("forms"))
				docTitle = "sr0";
			if (contentType.toLowerCase().contains("practice notes"))
				docTitle = "sr3";
			if (contentType.toLowerCase().contains("briefs, pleadings & motions"))
				docTitle = "sr6";
			if (contentType.toLowerCase().contains("cases"))
				docTitle = "sr9";
			if (contentType.toLowerCase().contains("statutes & legislation"))
				docTitle = "sr12";
			if (contentType.toLowerCase().contains("secondary materials"))
				docTitle = "sr13";
			if (contentType.toLowerCase().contains("emerging issues"))
				docTitle = "sr16";
			if (contentType.toLowerCase().contains("administrative materials"))
				docTitle = "sr19";

			List<WebElement> allListItems = commonLibrary.isExistList(resultDiv, UIMAP_LPAHome.actionList, 20);
			for (int j = 0; j < allListItems.size(); j++) {
				if (allListItems.get(j).getAttribute("data-id").equals(docTitle)) {
					List<WebElement> firstDoc = commonLibrary.isExistList(allListItems.get(j), UIMAP_LPAHome.a, 10);
					if (firstDoc.size() > 0) {
						for (int k = 0; k < allListItems.size(); k++) {
							if (k == 1) {
								commonLibrary.clickLinkWithWebElementWithWaitJS(firstDoc.get(k), firstDoc.get(k).getText());
								flg = true;
								commonLibrary.sleep(5000);
								commonLibrary.sleep(5000);
							}
							if (flg)
								break;
						}
					}
				}
				if (flg)
					break;
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearchLPAResults     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAResults simpleSearchLPAResults(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the links under Practice
	// Area
	// # Function Name : SelectPracticeAreaLink
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAHome selectLPAPracticeAreaLink(String strLinkText, String strPageHeader) {

		try {

			WebElement btnSelectPracticeArea = commonLibrary.isExist(UIMAP_Home.btnSelectPracticeArea);
			commonLibrary.clickMouseMoveAction(btnSelectPracticeArea, "Practice Areas");

			if (strLinkText.equals("Banking & Finance")) {
				WebElement lnkBankingAndFinance = commonLibrary.isExist(UIMAP_Home.lnkBankingAndFinance);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(lnkBankingAndFinance, lnkBankingAndFinance.getText());
				} else {
					commonLibrary.clickButtonParentWithWait(lnkBankingAndFinance, lnkBankingAndFinance.getText());
				}
				commonLibrary.sleep(3000);

				WebElement txtPageHeading = commonLibrary.isExist(UIMAP_Home.txtPracticeAreaHeading);
				if (txtPageHeading.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
					report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
				else
					report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Pod Contents Under Recdnt
	// pod
	// # Function Name : verifyPodContentsUnderRecent    
	// # Author : Meera
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public LPAHome verifyPodContentsUnderRecent(String docTitle) {
		WebElement recentPod = commonLibrary.isExistNegative(UIMAP_LPAHome.recentPod, 10);

		if (recentPod != null) {
			report.updateTestLog("Verify Recent pod is present", "Recent pod is present", Status.PASS);

			WebElement recentTitle = commonLibrary.isExistNegative(recentPod, UIMAP_LPAHome.recentTitle, 10);

			if (recentTitle.equals(docTitle)) {

				report.updateTestLog("Verify the document" + docTitle + " displays in the recent pod", "Verify the document" + docTitle + " displays in the recent pod", Status.PASS);
			} else
				report.updateTestLog("Verify the document" + docTitle + " displays in the recent pod", "Verify the document" + docTitle + " is not displayed in the recent pod", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click View all document history link
	// # Function Name : clickViewAllDocumentHistoryLink   
	// # Author : Meera
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public History clickViewAllDocumentHistoryLink() {
		WebElement recentPod = commonLibrary.isExistNegative(UIMAP_LPAHome.recentPod, 10);
		WebElement linkToHistory = commonLibrary.isExist(recentPod, UIMAP_LPAHome.linkToHistory, 10);

		if (linkToHistory != null) {
			commonLibrary.clickLinkWithWebElementWithWaitJS(linkToHistory, "View all document history");

			report.updateTestLog("Click on View all document history ", "History page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Click on View all document history ", "History page is displayed", Status.FAIL);
		}
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Pod Cheader under Law360 pod
	// pod
	// # Function Name : verifyPodContentsUnderLaw360   
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome verifyPodContentsUnderLaw360(String header1, String header2) {
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;

		WebElement lawPod = commonLibrary.isExistNegative(UIMAP_LPAHome.law360Pod, 10);

		if (lawPod != null) {
			report.updateTestLog("Verify Recent pod is present", "Recent pod is present", Status.PASS);

			WebElement podContent = commonLibrary.isExist(lawPod, UIMAP_LPAHome.podContent, 10);
			WebElement podTab = commonLibrary.isExist(podContent, UIMAP_LPAHome.podTab, 10);
			WebElement ulLaw360 = commonLibrary.isExist(podTab, UIMAP_LPAHome.ulLaw360, 10);

			List<WebElement> liElement = commonLibrary.isExistList(ulLaw360, By.tagName("li"), 20);

			if (header1 != "Ignore" && header2 != "Ignore") {
				if (liElement.size() > 0) {
					for (WebElement lielement : liElement) {

						if ((lielement.getText().toLowerCase().contains(header1.toLowerCase())) || (lielement.getText().toLowerCase().contains(header2.toLowerCase()))) {

							flag1 = true;
						}

					}
				}
			} else if (header1 == "Ignore") {
				if (liElement.size() > 0) {
					for (WebElement lielement : liElement) {

						if (lielement.getText().toLowerCase().contains(header2.toLowerCase())) {

							flag2 = true;
						}

					}
				}
			} else if (header2 == "Ignore") {
				if (liElement.size() > 0) {
					for (WebElement lielement : liElement) {

						if (lielement.getText().toLowerCase().contains(header2.toLowerCase())) {

							flag3 = true;
						}

					}
				}
			}
			if (flag1 || flag2 || flag3) {
				report.updateTestLog("Verify the tab displays in the pod", "the tab is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify the tab displays in the pod", "the tab is not displayed", Status.FAIL);
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click content type
	// # Function Name : selectCondentType2   
	// # Author : Bikash
	// # Date Created : Oct'16
	// #*****************************************************************************************************************************

	public LPAResults selectCondentType2(String strCondentType) {
		Boolean blnFlag = false;
		try {
			WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);
			if (searchControl != null) {
				List<WebElement> ulElement = commonLibrary.isExistList(searchControl, By.tagName("ul"), 20);
				if (ulElement.size() > 0) {
					for (WebElement element : ulElement) {
						List<WebElement> liElement = commonLibrary.isExistList(searchControl, By.tagName("li"), 20);
						for (WebElement lielement : liElement) {
							WebElement button = commonLibrary.isExist(lielement, By.tagName("button"), 20);
							if (button == null)
								button = commonLibrary.isExist(element, By.tagName("span"), 20);
							if (button.getText().toString().toLowerCase().contains(strCondentType.toLowerCase())) {
								// Clicking on the selected content type
								if (browsername.contains("internet"))
									commonLibrary.clickLinkWithWebElementWithWait(button, button.getText());
								else
									commonLibrary.clickMethod(button, button.getText());
								commonLibrary.sleep(50000);
								blnFlag = true;
								break;

							}
						}
					}
				}
			}

			if (blnFlag) {
				report.updateTestLog("Click on " + strCondentType, "Clicked on " + strCondentType, Status.PASS);
			} else {
				report.updateTestLog("Click on " + strCondentType, "Not Clicked on " + strCondentType, Status.FAIL);
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify pod lexis market is not
	// present
	// pod
	// # Function Name : verifyLexisMarketTrackerPodNotPresent   
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome verifyLexisMarketTrackerPodNotPresent() {

		WebElement marketpod = commonLibrary.isExistNegative(UIMAP_LPAHome.marketPod, 10);
		if (marketpod != null)
			report.updateTestLog("Verify pod LexisMarketTracker is not present", "Pod LexisMarketTracker is present", Status.FAIL);
		else
			report.updateTestLog("Verify pod LexisMarketTracker is not present", "Pod LexisMarketTracker is not present", Status.PASS);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Law360NewsTab
	// # Function Name : clickLaw360NewsTab     
	// # Author : Kalaivanan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome clickLaw360NewsFeedsWithTab(String PageArea, String tabName, String LinkPosition, String URLContains) {
		boolean flag = false;
		boolean flag1 = false;
		WebElement lawpodfeed = null;
		WebElement lawpodtab = commonLibrary.isExist(UIMAP_LPAHome.law360Pod, 10);
		List<WebElement> tabList = commonLibrary.isExistList(lawpodtab, By.tagName("li"), 20);

		String OldWindow = driver.getWindowHandle();

		for (WebElement tab : tabList) {
			if (tab.getText().contains(tabName)) {
				tab.click();
				flag = true;
				break;
			}
		}
		if ((flag)) {
			report.updateTestLog("Click on " + tabName + " tab ", tabName + " tab is clicked", Status.PASS);
		} else {
			report.updateTestLog("Click on " + tabName + " tab ", tabName + " tab is not clicked", Status.FAIL);
		}

		commonLibrary.sleep(5000);

		if (tabName.equals("Immigration")) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedImmigration, 20);
		} else if (tabName.equals("Employment")) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedEmployment, 20);
		} else if ((PageArea.equals("Securities & Capital Markets")) && (tabName.equals("Capital Markets"))) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedCapitalMarket, 20);
		} else if (tabName.equals("Legal Industry")) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedLegalIndustry, 20);
		} else if (tabName.equals("Corporate")) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedCorporate, 20);
		} else if ((PageArea.equals("Banking & Finance")) && (tabName.equals("Capital Markets"))) {
			lawpodfeed = commonLibrary.isExist(lawpodtab, UIMAP_LPAHome.law360feedCapitalMarket1, 20);
		}

		List<WebElement> feedList = commonLibrary.isExistList(lawpodfeed, By.tagName("a"), 20);

		int Link = Integer.parseInt(LinkPosition);

		if (Link < 5) {
			feedList.get(Link - 1).click();
			flag1 = true;
		} else {
			WebElement showmorebutton = commonLibrary.isExist(lawpodfeed, UIMAP_LPAHome.btn, 10);
			showmorebutton.click();
			List<WebElement> feedList1 = commonLibrary.isExistList(lawpodfeed, By.tagName("a"), 20);
			feedList1.get(Link - 1).click();
			flag1 = true;
		}
		if (flag1) {
			report.updateTestLog("Click on Law360 feed at position " + LinkPosition, "Law360 feed at position " + LinkPosition + " is clicked containing " + feedList.get(Link - 1).getText(), Status.PASS);
		} else {
			report.updateTestLog("Click on Law360 feed at position " + LinkPosition, "Law360 feed at position " + LinkPosition + " is not clicked", Status.FAIL);
		}

		commonLibrary.switchToWindow1(URLContains);
		if (driver.getCurrentUrl().contains(URLContains)) {
			report.updateTestLog("Verify the URL-HTTP://WWW.LAW360.COM/CAPITALMARKETS ", "Verified the URL-HTTP://WWW.LAW360.COM/CAPITALMARKETS", Status.PASS);
		} else {
			report.updateTestLog("Verify the URL-HTTP://WWW.LAW360.COM/CAPITALMARKETS ", "Not verified the URL-HTTP://WWW.LAW360.COM/CAPITALMARKETS ", Status.FAIL);
		}

		driver.close();
		report.updateTestLog("Click on close Button ", "Clicked on close Button", Status.PASS);

		driver.switchTo().window(OldWindow);

		return new LPAHome(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout and Delete Cookies.
	// # Function Name : logoutDeleteAllCookies     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public SignIn logoutDeleteAllCookies() {

		generalFunctions.logoutDeleteAllCookies();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click Show More Link
	// # Function Name : clickShowMoreLink     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome clickShowMoreLink() {

		WebElement businessCommPod = commonLibrary.isExist(UIMAP_LPAHome.businesscommPod, 20);
		WebElement showMoreLink = commonLibrary.isExist(businessCommPod, UIMAP_LPAHome.showmorelessLink, 20);

		if (showMoreLink != null) {

			commonLibrary.clickLink(showMoreLink, "Show More");

		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to Verify number of links in
	// Business & Commercial Pod
	// # Function Name : verifyLinkCountBussPod     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome verifyLinkCountBussPod() {

		WebElement businessCommPod = commonLibrary.isExist(UIMAP_LPAHome.businesscommPod, 20);

		List<WebElement> allULList = commonLibrary.isExistList(businessCommPod, UIMAP_LPAHome.ul, 20);
		if (allULList.size() > 0) {

			List<WebElement> businessCommPodList = commonLibrary.isExistList(allULList.get(1), UIMAP_LPAHome.actionList, 20);

			if (businessCommPodList.size() == 10) {
				report.updateTestLog("Verify number of links in Business & Commercial Pod ", "Only 10 Links are displayed in Business & Commercial Pod", Status.PASS);
			} else {
				report.updateTestLog("Verify number of links in Business & Commercial Pod  ", "Links displayed in Business & Commercial Pod is not equal to 10", Status.FAIL);
			}
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write the URL into data sheet.
	// # Function Name : saveURL     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public LPAHome saveURL(String tcName, String dataSheet, String colName) {

		generalFunctions.saveURL(tcName, dataSheet, colName);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying History Dropdown
	// Presence.
	// # Function Name : verifyHistoryDropdownAbsence
	// # Author : Deepha Hariramasamy
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPAHome verifyHistoryDropdownAbsence() {
		WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
		if (btnIdHistoryMenuArrow == null) {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DOES NOT DISPLAYS' IN GLOBAL MENU.", Status.PASS);
		} else {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : clickBrowserBackToPractice
	// # Author : Deepha H
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************

	public Practice clickBrowserBackToPractice() {
		commonLibrary.clickBrowserBack();
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Topics
	// # Function Name : clickLinkInTopics     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPABrowse clickLinkInTopics1(String topic, String strLink, String startIn) {
		boolean flg = false;
		WebElement topicsTab = commonLibrary.isExistNegative(UIMAP_LPAHome.topicsTab, 10);
		if (topicsTab != null) {
			List<WebElement> allULList = commonLibrary.isExistList(topicsTab, UIMAP_LPAHome.ul, 20);
			if (allULList.size() > 0) {
				for (int i = 0; i < allULList.size(); i++) {
					List<WebElement> allLIList = commonLibrary.isExistList(allULList.get(i), UIMAP_LPAHome.actionList, 20);
					for (int j = 0; j < allLIList.size(); j++) {
						WebElement topicHeader = commonLibrary.isExistNegative(allLIList.get(j), UIMAP_LPAHome.topicHeader, 10);
						if (topicHeader != null && topicHeader.getText().toLowerCase().contains(topic.toLowerCase())) {
							List<WebElement> allULListOne = commonLibrary.isExistList(allLIList.get(j), UIMAP_LPAHome.ul, 20);
							if (allULListOne.size() > 0) {
								for (int k = 0; k < allULListOne.size(); k++) {
									List<WebElement> allLIListOne = commonLibrary.isExistList(allULListOne.get(k), UIMAP_LPAHome.actionList, 20);
									for (int l = 0; l < allLIListOne.size(); l++) {
										WebElement link = commonLibrary.isExistNegative(allLIListOne.get(l), UIMAP_LPAHome.a, 10);
										if (link != null && link.getText().toLowerCase().contains(strLink.toLowerCase())) {
											commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
											commonLibrary.sleep(10000);
											commonLibrary.sleep(10000);
											if (!startIn.equals(""))
												verifyCondentType(startIn);
											flg = true;
											break;
										}
										if (flg)
											break;
									}
									if (flg)
										break;
								}
							}
						}
						if (flg)
							break;
					}
					if (flg)
						break;
				}
			}
		}
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click
	// pop up
	// # Function Name : click_ExternalLink
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome switchTosecondaryBrowser(String StrlinkText) {

		if (commonLibrary.switchToWindow(StrlinkText)) {
			report.updateTestLog("Verify secondary window opens", "Secondary window is displayed", Status.PASS);
			driver.close();
			commonLibrary.switchToWindow("lpabrowse");
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking link from lexis 360 pod
	// pod
	// # Function Name : clickLinkFromLexis360   
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome clickLinkFromLexis360(String title) {

		WebElement lawPod = commonLibrary.isExistNegative(UIMAP_LPAHome.law360Pod, 10);
		if (lawPod != null) {
			List<WebElement> links = commonLibrary.isExistList(lawPod, UIMAP_LPAHome.doclink, 20);
			for (WebElement item : links) {
				if (item.getText().equalsIgnoreCase(title)) {
					WebElement icon = commonLibrary.isExist(item, UIMAP_LPAHome.externalIcon, 10);
					if (icon != null) {
						report.updateTestLog("Verify external link icon is displayed", "External link icon is displayed", Status.PASS);
					} else {
						report.updateTestLog("Verify external link icon is displayed", "External link icon is not displayed", Status.FAIL);
					}
					commonLibrary.clickButtonParentWithWait(item, title);
					break;
				}
			}
		} else {
			report.updateTestLog("Click link " + title + " from law 360 pod", "Law 360 pod is not present", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPABrowse swithToCondentType1(String strCondentType) {
		Boolean flg = false;
		for (int i = 0; i < 3; i++) {
			if (generalFunctions.selectCondentType(strCondentType) != 1) {
				if (generalFunctions.selectCondentType(strCondentType) == 1) {
					flg = true;
					break;
				}
			} else {
				flg = true;
				break;
			}
		}
		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click
	// pop up
	// # Function Name : click_ExternalLink
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome switchTosecondaryBrowserhome(String StrlinkText) {

		if (commonLibrary.switchToWindow(StrlinkText)) {
			report.updateTestLog("Verify secondary window opens", "Secondary window is displayed", Status.PASS);
			driver.close();
			commonLibrary.switchToWindow("lpahome");
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking link from lexis 360 pod
	// pod
	// # Function Name : clickLinkFromLexis360   
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome clickFirstLinkFromLexis360() {

		WebElement lawPod = commonLibrary.isExistNegative(UIMAP_LPAHome.law360Pod, 10);
		if (lawPod != null) {
			List<WebElement> links = commonLibrary.isExistList(lawPod, UIMAP_LPAHome.doclink, 20);
			for (WebElement item : links) {
				if (item != null) {
					WebElement icon = commonLibrary.isExist(item, UIMAP_LPAHome.externalIcon, 10);
					if (icon != null) {
						report.updateTestLog("Verify external link icon is displayed", "External link icon is displayed", Status.PASS);
					} else {
						report.updateTestLog("Verify external link icon is displayed", "External link icon is not displayed", Status.FAIL);
					}
					commonLibrary.clickButtonParentWithWait(item, item.getText());
					break;
				}
			}
		} else {
			report.updateTestLog("Click first from law 360 pod", "Law 360 pod is not present", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying History Dropdown absence.
	// # Function Name : verifyHistoryDropdown
	// # Author : ramesh
	// # Date Created : 24-Dec'15
	// #*****************************************************************************************************************************

	public LPAHome verifyHistoryDropdown() {
		WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
		if (btnIdHistoryMenuArrow == null) {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DOES NOT DISPLAYS' IN GLOBAL MENU.", Status.PASS);
		} else {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function for click More dropdown IN LPAHome .
	// # Function Name : clickMoreDropDown
	// # Author : Ramesh
	// # Date Created : 24-Dec'15
	// #*****************************************************************************************************************************

	public LPAHome clickMore() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {

			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");

		}
		commonLibrary.sleep(500000);
		WebElement moreDropdownContainer = commonLibrary.isExistNegative(UIMAP_Home.moreDropdownContainer, 10);
		if (moreDropdownContainer == null || !moreDropdownContainer.isDisplayed()) {
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
			commonLibrary.sleep(500000);
		}
		return new LPAHome(scriptHelper);

	}

	// # Function Description : Function for MoreDropDownVerification
	// # Function Name : MoreDropDownVerification    
	// # Author : Ramesh
	// # Date Created : 24-Jan'15
	// #*****************************************************************************************************************************

	public LPAHome MoreDropDownVerification() {

		// Click on More button
		WebElement btnMore = commonLibrary.isExist(UIMAP_LPAHome.btnTitleMore, 100);
		if (btnMore != null) {

			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");

		}

		// verify signout is not displayed in more dropdown
		WebElement lnkSignOut = commonLibrary.isExist(UIMAP_LPAHome.lnkTextSignOut, 100);

		if (lnkSignOut == null) {
			report.updateTestLog("Verify signout is not displayed in more dropdown", "'SIGNOUT 'DOES NOT DISPLAYS' IN GLOBAL MENU.", Status.PASS);
		}

		else
			report.updateTestLog("Verify signout is not displayed in more dropdown", "'SIGNOUT DISPLAYS' IN GLOBAL MENU.", Status.FAIL);

		// verify folders is not displayed in more dropdown
		WebElement Folders = commonLibrary.isExist(UIMAP_LPAHome.folder, 100);
		if (Folders == null) {
			report.updateTestLog("Verify Folders is not displayed in more dropdown", "Folders is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify Folders is not displayed in more dropdown", "Folders is displayed.", Status.FAIL);

		// Verify feedback is displayed in more dropdown
		WebElement feedback = commonLibrary.isExist(UIMAP_LPAHome.feedback, 100);
		if (feedback != null) {
			report.updateTestLog("Verify feedback  is displayed in more dropdown", "feedback is displayed", Status.PASS);
		}

		else
			report.updateTestLog("Verify feedback  is displayed in more dropdown", "'feedback is not displayed", Status.FAIL);

		// Verify Help is displayed in more dropdown
		WebElement Help = commonLibrary.isExist(UIMAP_LPAHome.Help, 100);
		if (Help != null) {
			report.updateTestLog("Verify Help  is displayed in more dropdown", "Help is displayed", Status.PASS);
		}

		else
			report.updateTestLog("Verify Help  is displayed in more dropdown", "'Help is not displayed", Status.FAIL);

		// verify alerts is not displayed in more dropdown
		WebElement Alerts = commonLibrary.isExist(UIMAP_LPAHome.Alerts, 100);
		if (Alerts == null) {
			report.updateTestLog("Verify Alerts is not displayed in more dropdown", "Alerts is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify Alerts is not displayed in more dropdown", "Alerts is displayed.", Status.FAIL);

		// verify notifications is not displayed in more dropdown
		WebElement Notifications = commonLibrary.isExist(UIMAP_LPAHome.Notifications, 100);
		if (Notifications == null) {
			report.updateTestLog("Verify Notifications is not displayed in more dropdown", "Notifications is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify Notifications is not displayed in more dropdown", "Notifications is displayed.", Status.FAIL);

		// verify Settings is not displayed in more dropdown
		WebElement Settings = commonLibrary.isExist(UIMAP_LPAHome.Settings, 100);
		if (Settings == null) {
			report.updateTestLog("Verify Settings is not displayed in more dropdown", "Settings is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify Settings is not displayed in more dropdown", "Settings is displayed.", Status.FAIL);

		// verify PriceGuide is not displayed in more dropdown
		WebElement PriceGuide = commonLibrary.isExist(UIMAP_LPAHome.PriceGuide, 100);
		if (PriceGuide == null) {
			report.updateTestLog("Verify PriceGuide is not displayed in more dropdown", "PriceGuide is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify PriceGuide is not displayed in more dropdown", "PriceGuide is displayed.", Status.FAIL);

		// verify LiveSupport is not displayed in more dropdown
		WebElement LiveSupport = commonLibrary.isExist(UIMAP_LPAHome.LiveSupport, 100);
		if (LiveSupport == null) {
			report.updateTestLog("Verify LiveSupport is not displayed in more dropdown", "LiveSupport is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify LiveSupport is not displayed in more dropdown", "LiveSupport is displayed.", Status.FAIL);

		// verify SignInProfile is not displayed in more dropdown
		WebElement SignInProfile = commonLibrary.isExist(UIMAP_LPAHome.SignInProfile, 100);
		if (SignInProfile == null) {
			report.updateTestLog("Verify SignInProfile is not displayed in more dropdown", "SignInProfile is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify SignInProfile is not displayed in more dropdown", "SignInProfile is displayed.", Status.FAIL);

		// verify MyLexis is not displayed in more dropdown
		WebElement MyLexis = commonLibrary.isExist(UIMAP_LPAHome.MyLexis, 100);
		if (MyLexis == null) {
			report.updateTestLog("Verify MyLexis is not displayed in more dropdown", "MyLexis is not displayed.", Status.PASS);
		}

		else
			report.updateTestLog("Verify MyLexis is not displayed in more dropdown", "MyLexis is displayed.", Status.FAIL);

		return new LPAHome(scriptHelper);

	}

}