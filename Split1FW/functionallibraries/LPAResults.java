package functionallibraries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPAHome;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_VSASearchResults;
import UIMAP.UIMAP_Settings;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

import supportlibraries.*;

public class LPAResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	int Mwait = Integer.parseInt(Mediumwait);
	public static int check = 0;
	public static int val = 0;
	private ExcelDataAccess excel;

	final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	PageCheck pageCheck = new PageCheck(scriptHelper);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	// private Home home = new Home(scriptHelper);
	public LPAResults(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lparesults"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lparesults") && counter < 40);

		if (!driver.getCurrentUrl().contains("lparesults")) {
			throw new IllegalStateException("LPA Results page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults swithToCondentType(String strCondentType) {
		Boolean flg = false;
		commonLibrary.sleep(5000);
		for (int i = 0; i < 3; i++) {
			// commonLibrary.sleep(5000);
			if (generalFunctions.lpaselectCondentType(strCondentType) != 1) {
				commonLibrary.sleep(5000);
				if (generalFunctions.lpaselectCondentType(strCondentType) == 1) {
					flg = true;
					break;
				}
			} else {
				flg = true;
				break;
			}
			commonLibrary.sleep(5000);
		}
		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults verifySearchResultHeader(String strPageHeader) {
		// WebElement ulCondentSwitcher =
		// commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		// int count =0;
		// do
		// {
		// count++;
		// commonLibrary.sleep(5000);
		// ulCondentSwitcher =
		// commonLibrary.isExist_Negative(UIMAP_SearchResult.ulCondentSwitcher,
		// 10);
		// }
		// while(ulCondentSwitcher==null && count<36);
		commonLibrary.sleep(3000);
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 5);
		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;

		if (Header != null && Header.getText().toLowerCase().contains(strPageHeader.toLowerCase()))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify button in Action
	// # Function Name : verifyButtonInAction    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public LPAResults verifyButtonInAction(String button) {
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickButtonParentWithWait(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_SearchResult.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_SearchResult.divAction, 20);
		if (divAction != null) {
			List<WebElement> actionList = commonLibrary.isExistList(divAction, UIMAP_LPAHome.actionList, 20);
			for (WebElement buttonName : actionList) {
				if (buttonName.getText().toLowerCase().contains(button.toLowerCase())) {
					report.updateTestLog("Verify " + button + " is present in Action", button + " is present in Action Button", Status.PASS);
					flag = true;
					break;
				}
			}
		} else {
			report.updateTestLog("Verify " + button + " is present in Action", "Buttons list is not available after clicking Action Button", Status.FAIL);
		}
		if (!flag) {
			report.updateTestLog("Verify " + button + " is present in Action", button + " is not present in Action Button", Status.FAIL);
		}
		return new LPAResults(scriptHelper);
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
	// # Function Description : Function to verify No documents is present in
	// Result Page and to click ReRun Search
	// # Function Name : verifyNoDocumentsClickReRun     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public LPAResults verifyNoDocumentsClickReRun() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement noDocumentsDiv = commonLibrary.isExist(UIMAP_LPAHome.noDocumnentsDiv, 20);
		WebElement noDocumentsHeader = commonLibrary.isExist(noDocumentsDiv, UIMAP_LPAHome.noDocumentsHeader, 20);
		if (noDocumentsHeader != null) {
			if (noDocumentsHeader.getText().toLowerCase().contains("no documents")) {
				report.updateTestLog("Verify 'No Documents Found is displayed'", "'No Documents Found' is displayed", Status.PASS);
				WebElement reRunSearch = commonLibrary.isExist(noDocumentsDiv, UIMAP_LPAHome.reRunSearch, 20);
				if (reRunSearch != null) {
					commonLibrary.clickButtonParentWithWait(reRunSearch, reRunSearch.getText());
				} else {
					report.updateTestLog("Click on 'ReRun search as natural language", "'ReRun search as natural language' button is not available", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify 'No Documents Found' is displayed", "'No Documents Found' is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify 'No Documents Found' is displayed", "'No Documents Found' is not displayed", Status.FAIL);
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout of the application.
	// # Function Name : logout     
	// # Author : Anbarasan
	// # Date Created : July'15
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
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : navigateToViewAllHistory
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public History navigateToViewAllHistory() {
		generalFunctions.navigateToHistoryFooterLink("View All History");
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : clickDocLink2     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocLink2(String strDocTitle) {

		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// for (i = 0; i < 3; i++) {
		// if (commonLibrary.isExist_Negative(UIMAP_SearchResult.frmClassResult,
		// 10) != null)
		// resultClass =
		// commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		//
		// if (resultClass != null) {
		// WebElement OListResult = commonLibrary.isExist(resultClass,
		// By.tagName("ol"), 20);
		// if (OListResult != null) {
		// List<WebElement> OListItems = commonLibrary.isExist_List(OListResult,
		// By.tagName("li"), 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// commonLibrary.sleep(100000);
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// }
		//
		// }
		// }
		//
		// }
		//
		// }
		// }
		// if (!blnFlag) {
		// WebElement btnNextPage =
		// commonLibrary.isExist_Negative(UIMAP_SearchResult.btnNextPage, 10);
		// if (browsername.contains("internet"))
		// commonLibrary.click_JS(btnNextPage, "Next Page");
		// else
		// commonLibrary.clickButton_Parent_WithWait(btnNextPage, "Next Page");
		// } else
		// break;
		// }
		//
		// if (blnFlag) {
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is displayed",
		// Status.PASS);
		// } else {
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is not displayed",
		// Status.FAIL);
		// }
		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click On Cases
	// # Function Name : clickOnCases
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public LPAResults clickOnCaseLink() {
		boolean blnflag = false;
		WebElement searchControl = commonLibrary.isExistNegative(UIMAP_SearchResult.searchControl, 10);
		commonLibrary.isExistNegative(searchControl, UIMAP_SearchResult.casesButton, 10);
		List<WebElement> header = commonLibrary.isExistList(searchControl, UIMAP_SearchResult.casesHeader, 10);
		for (WebElement caseHeader : header) {
			if (caseHeader.getText().contains("Cases")) {
				caseHeader.click();
				commonLibrary.sleep(30000);
				blnflag = true;
				if (blnflag) {
					report.updateTestLog("Click on Case Button", "Case Button is Clicked", Status.PASS);
				} else {
					report.updateTestLog("Click on Case Button", "Case Button is not clicked", Status.FAIL);
				}
				break;
			}
		}
		commonLibrary.sleep(30000);

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to swith To Condent Type Modified
	// # Function Name : swithToCondentTypeModified     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public LPAResults swithToCondentTypeModified(String strCondentType) {
		if (!(strCondentType != "Ignore")) {
			Boolean blnFlag = false;
			try {
				WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
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
							WebElement btnCondentType = commonLibrary.isExist(element, By.tagName("button"), 20);
							if (btnCondentType.getText().toString().toLowerCase().contains(strCondentType.toLowerCase())) {
								if (browsername.contains("internet"))
									commonLibrary.clickLinkWithWebElementWithWaitJS(btnCondentType, btnCondentType.getText());
								else
									commonLibrary.clickLinkWithWebElementWithWait(btnCondentType, btnCondentType.getText());
								commonLibrary.sleep(25000);
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
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify court and date being
	// displayed aside results list
	// region
	// # Function Name : verifyCourtDatePresence   
	// # Author : Kalaivanan
	// # Date Created : 12 Sep 15
	// #*****************************************************************************************************************************

	public LPAResults verifyCourtDatePresence() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement tagNameAside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 20);

					String month = "Jan;Feb;Mar;Apr;May;Jun;Jul;Aug;Sep;Oct;Nov;Dec";
					String[] monthArr = month.split(";");

					boolean courtFlag = false;
					boolean dateFlag = false;
					List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dd, 10);
					for (WebElement value : Val) {
						if (value.getText().toLowerCase().contains("court")) {
							report.updateTestLog("Verify court present aside results list", "Court is present aside results list", Status.PASS);
							courtFlag = true;
						}
						for (String monthName : monthArr) {
							if (value.getText().toLowerCase().contains(monthName.toLowerCase())) {
								report.updateTestLog("Verify date present aside results list", "Date is present aside results list", Status.PASS);
								dateFlag = true;
								break;
							}
						}
					}
					if (!courtFlag) {
						report.updateTestLog("Verify court present aside results list", "Court is not present aside results list", Status.FAIL);
					}
					if (!dateFlag) {
						report.updateTestLog("Verify date present aside results list", "Date is not present aside results list", Status.FAIL);
					}
				}
			} else {
				report.updateTestLog("Verify date present aside results list", "Date is not present aside results list", Status.FAIL);
			}
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify header of Search Result Page.
	// # Function Name : verifyResultPageNormalSearch     
	// # Author : Kalaivanan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LPAResults verifyResultPageNormalSearch(String SearchTerm) {

		// to verify the results page for normal search

		WebElement lnSearchResults = commonLibrary.isExist(UIMAP_SearchResult.lnkresult, 30);
		if (lnSearchResults != null) {

			commonLibrary.clickLinkWithWebElementWithWait(lnSearchResults, "Search for" + SearchTerm);
		}

		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 30);
		// verifying the header
		if (hdrResult1 != null && hdrResult1.getText().contains(SearchTerm)) {

			report.updateTestLog("Verify results page is displayed", "Result page is displayed for the searchterm " + SearchTerm, Status.PASS);
		} else {

			report.updateTestLog("Verify results page is displayed", "Result page is not displayed for the searchterm " + SearchTerm, Status.FAIL);
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save the court name and date aside
	// document title
	// # Function Name : saveCourtAndDateAsideDocumentTitle     
	// # Author : Kalaivanan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Search saveCourtAndDateAsideDocumentTitle(String docTitle, String ConfigId, int ColNum, int WriteColumn) {
		String court = "";
		String date = "";
		WebElement resultClass = null;
		boolean blnFlag = false;

		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		excel = new ExcelDataAccess(datatablePath, "FullDocument");
		excel.setDatasheetName("General_Data");
		int rowNum = excel.getRowNum(ConfigId, ColNum);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass == null) {
			int count = 0;
			do {
				count = count + 1;
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
				if (resultClass == null)
					commonLibrary.sleep(5000);
			} while (resultClass == null && count <= 40);
		}

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(docTitle)) {
						WebElement tagNameAside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 20);

						List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dd, 10);
						for (WebElement value : Val) {
							if (value.getText().toLowerCase().contains("court")) {
								court = value.getText() + ";";
							} else if (value.getText().contains(", ")) {
								date = value.getText();
								court = court + date;
							}

						}
						excel.setValue(rowNum, WriteColumn, court);
						commonLibrary.sleep(5000);
						report.updateTestLog("Verify Date value has been set ", "Date value has been set with " + court, Status.PASS);
						blnFlag = true;
						break;
					}
				}

			}
			if (!blnFlag)

				report.updateTestLog("Click on the document " + docTitle, "Not Clicked  on the document " + docTitle, Status.FAIL);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Single party case name only displays
	// for single party case search"
	// # Function Name : verifySinglePartySearch     
	// # Author : Bikash
	// # Date Created : Oct'16
	// #**********************************************
	public LPAResults verifySinglePartySearch(String keyword1, String keyword2, String keyword3) {
		Boolean blnFlag = false;

		try {
			WebElement documentWrapper = commonLibrary.isExist(UIMAP_LPAHome.noDocumnentsDiv, 10);
			WebElement olItem = commonLibrary.isExist(documentWrapper, By.tagName("ol"), 20);
			if (olItem != null) {
				List<WebElement> listItems = commonLibrary.isExistList(olItem, By.tagName("li"), 20);
				if (listItems.size() > 0) {
					for (WebElement element : listItems) {
						WebElement documentLink = commonLibrary.isExist(element, By.tagName("div"), 20);
						if (documentLink == null)
							documentLink = commonLibrary.isExist(element, By.tagName("h2"), 20);
						// Verifying single party case name only displays for
						// single party case search
						if (documentLink.getText().toString().toLowerCase().contains(keyword1.toLowerCase()) || documentLink.getText().toString().toLowerCase().contains(keyword2.toLowerCase())) {
							blnFlag = false;
						} else {
							blnFlag = true;
						}
					}
				}
			}

			if (!blnFlag)
				report.updateTestLog("Single party case name only displays for single party case search", " Single party case name only has been displayed for single party case search", Status.PASS);
			else
				report.updateTestLog("Single party case name only displays for single party case search", " Single party case name only has not been displayed for single party case search", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPAResults verifyNarrowbyFilterLeftPane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + strFilter, Status.FAIL);
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Required Filter Option and
	// absence of filters from other modules
	// Search Results page.
	// # Function Name : verifyPostFilter
	// # Author : kirthikak
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAResults verifyPostFilter(String strHeader, String strFilter, String headerlist) {
		ArrayList<String> header = new ArrayList<>();
		ArrayList<String> uiHeader = new ArrayList<>();
		String[] headersList = headerlist.split(";");
		for (int i = 0; i < headersList.length; i++) {
			header.add(headersList[i]);

		}
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);

			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {

				uiHeader.add(eltCollapsedFilterHeader.get(i).getText());
			}

			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), strHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), strHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {

					report.updateTestLog("Child topics are getting displayed for " + strHeader, "Child topics are getting displayed for " + strHeader, Status.DONE);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {

						report.updateTestLog("Child topics are geeting displayed for " + strHeader, "Child topics are getting displayed for " + strHeader, Status.DONE);
						blnFlag = true;
						break;
					}
				}
			}
		} else {
			report.updateTestLog("Child topics are geeting displayed for " + strHeader, "Child topics are  not getting displayed for " + strHeader, Status.FAIL);

		}

		if (Collections.disjoint(uiHeader, header)) {

			report.updateTestLog("No other modules are getting displayed", "No other modules are getting displayed", Status.PASS);

		} else {
			report.updateTestLog("No other modules are getting displayed", " Other modules are getting displayed", Status.FAIL);

		}

		return new LPAResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function Click Filter button
	// # Function Name : clearFilter
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LPAResults clearFilter() {
		WebElement clearMenu = commonLibrary.isExist(UIMAP_VSASearchResults.menu, 20);

		if (clearMenu != null) {
			WebElement btnClassFilter = commonLibrary.isExist(clearMenu, UIMAP_VSASearchResults.clearfilter1, 20);
			if (btnClassFilter != null) {
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(btnClassFilter, "Clear Filter");
				} else {
					commonLibrary.clickButtonParentWithWait(btnClassFilter, "Clear Filter");
				}
			} else {

				report.updateTestLog("Click On filter Button", "filter Button is not Clicked", Status.FAIL);

			}
		}
		commonLibrary.sleep(4000);
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Venilla
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public LPAResults addToFolder(String FolderName) {
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

			create = commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
			WebElement createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(7000000);
					createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (create.equals(createNew) && count < 80);
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

			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(700000);
					saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (saveFolder == saveNew && count < 40);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			commonLibrary.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To Folders
	// # Function Name : navigateToFolders
	// # Author : Venilla
	// # Date Created : 26-Nov'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {

		// Function calling for simple search
		generalFunctions.navigateToFolders();

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take result count
	// # Function Name : searchResultCount     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults searchResultCount(String excelData) {
		commonLibrary.sleep(3000);
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 5);
		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;

		// To write the result count to excel
		if (excelData != "") {
			String sheetNam = excelData.split(";")[0];
			int row = Integer.parseInt((excelData.split(";")[1]));
			String column = excelData.split(";")[2];
			String datatablePath = excelData.split(";")[3];

			WebElement doc = commonLibrary.isExist(Header, By.tagName("h2"), 10);
			String docTit = doc.getText().toLowerCase();
			String[] docTit1 = docTit.split("\n");

			excel = new ExcelDataAccess(datatablePath, sheetNam);
			excel.setDatasheetName("General_Data");
			excel.setValue(row, column, docTit1[0]);
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify button in Action
	// # Function Name : verifyButtonInAction    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public LPAResults clickButtonInAction(String button) {
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickButtonParentWithWait(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_SearchResult.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_SearchResult.divAction, 20);
		if (divAction != null) {
			List<WebElement> actionList = commonLibrary.isExistList(divAction, UIMAP_LPAHome.actionList, 20);
			for (WebElement buttonName : actionList) {
				if (buttonName.getText().toLowerCase().contains(button.toLowerCase())) {
					// commonLibrary.clickButtonParentWithWait(buttonName,
					// button);
					buttonName.click();
					flag = true;
					break;
				}
			}
		} else {
			report.updateTestLog("Click " + button + " in Action", "Buttons list is not available after clicking Action Button", Status.FAIL);
		}
		if (!flag) {
			report.updateTestLog("Click " + button + " in Action", button + " is not clicked in Action Button", Status.FAIL);
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify particular words are
	// highlighted
	// # Function Name : verifyHighlighted    
	// # Author : Seetha
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public LPAResults verifyHighlighted(String text) {

		boolean color = false;
		String[] texts = text.split(";");
		for (int i = 0; i < texts.length; i++) {
			List<WebElement> high = commonLibrary.isExistList(UIMAP_SearchResult.highlight, 20);
			for (WebElement item : high) {
				if (item.getText().toLowerCase().contains(texts[i].toLowerCase())) {
					color = true;
					break;
				}

			}
			if (!color)
				report.updateTestLog("Verify term " + texts[i] + " is highlighted", "term " + texts[i] + " is not highlighted", Status.FAIL);
			else
				report.updateTestLog("Verify term " + texts[i] + " is highlighted", "term " + texts[i] + " is highlighted", Status.PASS);
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click First DocLink
	// # Function Name : ClickFirstDocLink2     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Document clickFirstDocLink2() {
		Boolean blnFlag = false;

		try {
			String strDocTitle = null;
			WebElement resultClass = null;

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
						if (eleDocTitle != null) {
							strDocTitle = eleDocTitle.getText();
							report.updateTestLog("Document Title ", "Document Title is " + strDocTitle, Status.DONE);

							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						} else {
							WebElement lnkDocument = commonLibrary.isExist(document, By.tagName("a"), 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}
					}

				}
			}

			if (!blnFlag)

				report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
			else {
				WebElement pgHeader = null;

				if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
				else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);

				if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document") || pgHeader.getText().contains("Expert Witness"))) {

					strDocTitle = pgHeader.getText();
					// strDocTitle=strDocTitle.replace("\n", "");
					String[] title = strDocTitle.split("\n");

					report.updateTestLog("Verify document " + title[1].trim() + " is displayed", "Document " + title[1].trim() + " is displayed", Status.PASS);

				} else
					report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.FAIL);
			}
			return new Document(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());

			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify result count matches
	// # Function Name : verifySearchCount    
	// # Author : Seetha
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public LPAResults verifySearchCount() {

		String text = dataTable.getData("General_Data", "Pods");
		String text1 = dataTable.getData("General_Data", "PodContent");
		if (text.equalsIgnoreCase(text1))
			report.updateTestLog("Verify results count for both the searches with and, & are same", "Results count for both the searches with and, & are same", Status.PASS);
		else
			report.updateTestLog("Verify results count for both the searches with and, & are same", "Results count for both the searches with and, & are not same", Status.FAIL);

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify result view option
	// # Function Name : verifyResultViewOption    
	// # Author : Seetha
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public LPAResults verifyResultViewOption(String option) {

		boolean view = false;
		switch (option.toLowerCase()) {
		case "full": {
			WebElement resView = commonLibrary.isExist(UIMAP_SearchResult.resViewFull, 20);
			if (resView.getAttribute("class").contains("selected"))
				view = true;
			break;
		}
		case "title": {
			WebElement resView = commonLibrary.isExist(UIMAP_SearchResult.resViewtitle, 20);
			if (resView.getAttribute("class").contains("selected"))
				view = true;
			break;
		}
		}
		if (view)
			report.updateTestLog("Verify " + option + " view is selected", "" + option + " view is selected", Status.PASS);
		else
			report.updateTestLog("Verify " + option + " view is selected", "" + option + " view is not selected", Status.FAIL);

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select title view or full view
	// # Function Name : selectResultView     
	// # Author : Seetha
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LPAResults selectResultViewInResultPage(String option) {
		if (option.equalsIgnoreCase("title")) {
			WebElement resView = commonLibrary.isExist(UIMAP_Settings.viewMin);
			if (resView != null) {
				commonLibrary.clickButtonParentWithWait(resView, "Title View");
			} else
				report.updateTestLog("select " + option + " view", "" + option + " view is not selected", Status.FAIL);
		} else if (option.equalsIgnoreCase("full")) {
			WebElement resView = commonLibrary.isExist(UIMAP_Settings.viewMax);
			if (resView != null) {
				commonLibrary.clickButtonParentWithWait(resView, "Full View");
			} else
				report.updateTestLog("select " + option + " view", "" + option + " view is not selected", Status.FAIL);
		}

		return new LPAResults(scriptHelper);
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
	// # Function Description : Function to verify the filters present under the
	// narrow by section
	// # Function Name : verifynarrowbyfilters     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPAResults verifynarrowbyfilters(String filter) {

		commonLibrary.sleep(90000);
		WebElement asideclass;
		int count = 0;
		do {
			commonLibrary.sleep(100000);
			asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
			count++;
		} while (asideclass == null && count < 25);

		asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
		WebElement usedfilter = commonLibrary.isExist(asideclass, UIMAP_Document.usedfilter, 20);
		WebElement lilist = commonLibrary.isExist(usedfilter, UIMAP_Document.listItems, 20);
		WebElement narrowbyfilter = commonLibrary.isExist(lilist, UIMAP_Document.narrowbyfilter, 20);

		if (narrowbyfilter.getText().contains(filter)) {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is not displayed", Status.FAIL);
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

		WebElement practiceAreaNav = commonLibrary.isExist(UIMAP_LPAHome.practiceAreaNav, 10);
		commonLibrary.clickButtonParent(practiceAreaNav, "Practice Areas");

		WebElement childMenu = commonLibrary.isExist(UIMAP_LPAHome.childMenu, 10);
		WebElement content = commonLibrary.isExist(childMenu, UIMAP_LPAHome.homePageCont, 10);
		List<WebElement> pages = commonLibrary.isExistList(content, By.tagName("li"), 20);
		for (WebElement item : pages) {
			if (item.getText().equalsIgnoreCase(pageName)) {
				WebElement pageLink = commonLibrary.isExist(item, By.tagName("a"), 10);

				commonLibrary.clickMethod(pageLink, pageName);
				commonLibrary.sleep(50000);
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
	// # Function Description : Function to verify Condent Type
	// # Function Name : verifyCondentType     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAResults verifyCondentType(String condentTypeToVerify) {
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
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Filter Header Present
	// # Function Name : verifyFilterHeaderPresent
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LPAResults verifyFilterHeaderPresent(String strHeader) {
		Boolean bnlFlag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

		for (int i = 0; i < filterHeader.size(); i++) {

			if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

				bnlFlag = true;
				break;
			}

		}
		if (bnlFlag)
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is displayed in the left Pane", Status.PASS);
		else
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.FAIL);

		return new LPAResults(scriptHelper);
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
	// # Function Description : Function selecting LPA Filter menu items
	// # Function Name : LPAFilterMenuSelection     
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAResults applyLPASearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
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
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : getResultsListCount     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public int countResultsDispalyed() {
		List<WebElement> OListItems = null;

		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			}
		}
		report.updateTestLog("Verfify the rusults count displayed in the Results Page", "Number of results displayed in the page is " + OListItems.size(), Status.PASS);
		return OListItems.size();
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on specific page number.
	// # Function Name : ClickPageNumber
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LPAResults clickPageNumber(String number) {
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 20);
		for (WebElement item : pageNumbers) {
			if (item.getAttribute("data-value").equalsIgnoreCase(number)) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(item, "Page Number " + number + "");
				else
					commonLibrary.clickButtonParentWithWait(item, "Page Number " + number + "");
				break;
			}
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAResults clickPrinterFriendlyViewVerifyResultsAndCloseWindow() {

		generalFunctions.clickDeliverySelectOption_New("delivery", "Printer-friendly view");
		generalFunctions.documentState(driver);
		commonLibrary.switchToWindow("resultprint");
		commonLibrary.sleep(20000);
		// this.verifyResultsCount();
		List<WebElement> OListItems = null;

		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			}
		}
		report.updateTestLog("Verfify the rusults count displayed in the Results Page", "Number of results displayed in the page is " + OListItems.size(), Status.PASS);
		this.closePritnerFriendlyView();
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAResults closePritnerFriendlyView() {

		driver.close();
		commonLibrary.switchToWindow("lparesults");
		commonLibrary.sleep(20000);
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : getResultsListCount     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults verifyResultsCount(int count) {
		List<WebElement> OListItems = null;

		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			}
		}
		if (count == OListItems.size()) {
			report.updateTestLog("Verfify the rusults count displayed in the Results Page", "Result count is matching with the previous count that is " + count, Status.PASS);
		} else {
			report.updateTestLog("Verfify the rusults count displayed in the Results Page", "Result count is not matching with the previous count that is " + count, Status.PASS);
		}
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the post filters under court(
	// I have written this function since that particular testcase has 12
	// different configurations with differenct scenarios)
	// # Function Name : verifyPostFiltersMultipleCourt     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public LPAResults verifyPostFiltersMultipleCourt(String header, String filter) {
		int i;
		WebElement filters1 = null;
		String[] arrFilters = filter.split(";");
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}
		//

		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10); // put
																				// for
																				// court
		List<WebElement> moreOptions = commonLibrary.isExistList(filters1, UIMAP_SearchResult.filterMore, 10);
		for (WebElement moreButton : moreOptions) {
			if (moreButton != null && moreButton.getText().toLowerCase().contains("more")) {
				if (browsername.toLowerCase().contains("internet"))
					commonLibrary.clickButtonLogSmallWait(moreButton, "More");
				else
					commonLibrary.clickButtonLogSmallWait(moreButton, "More");
			}
		}
		if (filters1 != null) {
			report.updateTestLog("Verify court filter is present", "Court filter is present", Status.PASS);
			switch (header.toLowerCase()) {
			case "heading": // verify headings inside court like federal, tribal
							// and state
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("h2"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under court", arrFilters[j] + " is present under court", Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under court", arrFilters[j] + " is not present under court", Status.FAIL);
					}
				}
				break;
			}
			case "court": // this is to check whether on or more items specified
							// is present in the court filter
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);

					}
				}
				break;
			}
			}

		} else {
			report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the post filters under
	// jurisdiction( I have written this function since that particular testcase
	// has 12 different configurations with differenct scenarios)
	// # Function Name : verifyPostFiltersMultipleJuris     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public LPAResults verifyPostFiltersMultipleJuris(String header, String filter, String exceptCheck, String contentType) {
		int i;
		WebElement filters1 = null;
		String[] arrFilters = filter.split(";");
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}

		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10); // put
																						// for
																						// court
		WebElement moreOptions = commonLibrary.isExistNegative(filters1, UIMAP_SearchResult.filterMore, 10);
		if (moreOptions != null && moreOptions.getText().toLowerCase().contains("more")) {
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
			else
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
		}
		if (filters1 != null) {
			report.updateTestLog("Verify jurisdiction filter is present", "Jurisdiction filter is present", Status.PASS);
			switch (exceptCheck.toLowerCase()) {
			case "except": // verify headings inside court like federal, tribal
							// and state
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is not present under jurisdiction", arrFilters[j] + " is  present under jurisdiction", Status.FAIL);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is not present under jurisdiction", arrFilters[j] + " is  not present under jurisdiction", Status.PASS);

					}
				}
				break;
			}
			case "jurisdiction": // this is to check whether on or more items
									// specified is present in the court filter
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);

					}
				}
				break;
			}
			}

		} else {
			if (contentType.contains("Cases") || contentType.contains("Briefs, Pleadings and Motions") || contentType.contains("Jury Verdicts and Settlements") || contentType.contains("Dockets")) {
				report.updateTestLog("Verify jurisdiction filter is absent", "Jurisdictiion filter is absent", Status.PASS);
			} else {
				report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
			}
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order
	// # Function Name : verifyPostFilterOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LPAResults verifyPostFilterOrder(String header) {
		if (!(header.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			int i = 0, j = 0, k = 0;
			int newSize = 0;

			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
					}

					List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().toLowerCase().contains("more")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button, "More");
							else
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
					}
					int moreCount = moreOptions.size();
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					if (filters.size() > 0) {

						commonLibrary.focusControlJS(filterContainer);
						WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

						if (preferredFilters != null) {
							List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.eltFilterList, 10);
							k = preferredFiltersList.size();
							newSize = filters.size() - preferredFiltersList.size();
						}
						for (; k < filters.size() - 1; k++) {
							int count = 1;
							List<WebElement> filterName = commonLibrary.isExistList(filters.get(k), UIMAP_SearchResult.tagSpan, 20);
							List<WebElement> filterName1 = commonLibrary.isExistList(filters.get(k + 1), UIMAP_SearchResult.tagSpan, 20);
							String name = filterName.get(0).getText();
							String name1 = filterName1.get(0).getText();

							name = name.replace("(", "");
							name = name.replace(")", "");

							name1 = name1.replace("(", "");
							name1 = name1.replace(")", "");

							if (count == 1 && name.trim().toLowerCase().contains("u.s. federal")) {
								posFlag++;
							} else if (name.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").contains("9thcircuit") && (name1.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").contains("10thcircuit"))) {
								posFlag++;
							} else if (name.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").compareTo(name1.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "")) < 0) {
								posFlag++;
							} else {
								negFlag++;
							}

						}
						if (preferredFilters != null) {
							if (posFlag == ((newSize - 1) - (moreCount - 1)) && (negFlag == moreCount - 1)) {
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are sorted Alphabetically(A-Z) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are not sorted Alphabetically(A-Z) under " + header, Status.FAIL);
						} else {
							if (posFlag == (filters.size() - 1) && (negFlag == moreCount - 1)) {
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are sorted Alphabetically(A-Z) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are not sorted Alphabetically(A-Z) under " + header, Status.FAIL);
						}
					}

				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order
	// # Function Name : verifyPostFilterOrderNumberOfResults
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LPAResults verifyPostFilterOrderNumberOfResults(String header) {

		if (!(header.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			int i = 0, j = 0, k = 0;
			int newSize = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
					}

					List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().toLowerCase().contains("more")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button, "More");
							else
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
					}
					int count = moreOptions.size();
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					if (filters.size() > 0) {
						commonLibrary.focusControlJS(filterContainer);
						WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

						if (preferredFilters != null) {
							List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.eltFilterList, 10);
							k = preferredFiltersList.size();
							newSize = filters.size() - preferredFiltersList.size();
						}
						for (; k < filters.size() - 1; k++) {
							List<WebElement> filterName = commonLibrary.isExistList(filters.get(k), UIMAP_SearchResult.tagSpan, 20);
							List<WebElement> filterName1 = commonLibrary.isExistList(filters.get(k + 1), UIMAP_SearchResult.tagSpan, 20);

							String text = filterName.get(1).getText();
							String text1 = filterName1.get(1).getText();
							text = text.replaceAll("(?<=\\d),(?=\\d)", "");
							text1 = text1.replaceAll("(?<=\\d),(?=\\d)", "");
							int name = Integer.valueOf(text);
							int name1 = Integer.valueOf(text1);

							if (name >= name1) {
								posFlag++;
							} else {
								negFlag++;
							}

						}
						if (preferredFilters != null) {
							if ((posFlag + count - 1) == (newSize - 1) && (negFlag == (count - 1))) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						} else {
							if (posFlag == (filters.size() - 1) && (negFlag == 0)) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						}
					}
				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filter order
	// # Function Name : verifyPreferredFilterOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LPAResults verifyPreferredFilterOrder(String filterHeader, String filters) {

		if (!(filterHeader.equals(" ") && filters.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(filterHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), filterHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), filterHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + filterHeader, filterHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			List<WebElement> supplementalFilters = commonLibrary.isExistList(UIMAP_SearchResult.supplementalFilters, 10);
			if (supplementalFilters.size() > 0) {
				for (WebElement item : supplementalFilters) {
					if (item.getAttribute("data-id").toLowerCase().contains(filterHeader.toLowerCase())) {
						WebElement preferredFilters = commonLibrary.isExist(item, UIMAP_SearchResult.preferredFilters, 10);
						List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.lstTagName, 10);

						if (preferredFiltersList.size() > 0) {
							commonLibrary.focusControlJS(preferredFilters);
							if (preferredFiltersList.size() == 1) {
								List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(0), UIMAP_SearchResult.tagSpan, 20);
								String name = filterName.get(0).getText();
								if (name.trim().toLowerCase().equals(filters.toLowerCase())) {
									report.updateTestLog("Verify the filter " + name + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", "The filter " + name + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", Status.PASS);
								} else {
									report.updateTestLog("Verify the filter " + filters + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", "The filter " + filters + " is not displayed under preferred " + filterHeader + " title in " + filterHeader + " post filter", Status.FAIL);
								}
							} else if (preferredFiltersList.size() != 1) {
								for (int i = 0; i < preferredFiltersList.size() - 1; i++) {
									List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(i), UIMAP_SearchResult.tagSpan, 20);
									String name = filterName.get(0).getText();

									List<WebElement> filterName1 = commonLibrary.isExistList(preferredFiltersList.get(i + 1), UIMAP_SearchResult.tagSpan, 20);
									String name1 = filterName1.get(0).getText();
									if (filters.toLowerCase().contains(name.toLowerCase().trim()) && filters.toLowerCase().contains(name1.toLowerCase().trim())) {
										name = name.replace("(", "");
										name = name.replace(")", "");
										name1 = name1.replace("(", "");
										name1 = name1.replace(")", "");

										if (name.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").compareTo(name1.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "")) < 0) {
											posFlag++;
										} else {
											negFlag++;
										}

									}
								}
								if (posFlag == (preferredFiltersList.size() - 1) && (negFlag == 0)) {
									report.updateTestLog("Verify the " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, "The " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, Status.PASS);
								} else
									report.updateTestLog("Verify the " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, "The " + filters + " are not sorted Alphabetically(A-Z) in preferred " + filterHeader, Status.FAIL);

							}
						}
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + filters, "No Filter Selected.", Status.DONE);
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
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filter order
	// # Function Name : verifyPreferredFilterNumberOfResultsOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LPAResults verifyPreferredFilterNumberOfResultsOrder(String filterHeader, String filters) {

		if (!(filterHeader.equals(" ") && filters.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			String newFilters = "";
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(filterHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), filterHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), filterHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + filterHeader, filterHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			List<WebElement> supplementalFilters = commonLibrary.isExistList(UIMAP_SearchResult.supplementalFilters, 10);
			if (supplementalFilters.size() > 0) {
				for (WebElement item : supplementalFilters) {
					if (item.getAttribute("data-id").toLowerCase().contains(filterHeader.toLowerCase())) {
						WebElement preferredFilters = commonLibrary.isExist(item, UIMAP_SearchResult.preferredFilters, 10);
						List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.lstTagName, 10);
						if (preferredFiltersList.size() > 0) {
							commonLibrary.focusControlJS(preferredFilters);
							for (int i = 0; i < preferredFiltersList.size() - 1; i++) {
								List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(i), UIMAP_SearchResult.tagSpan, 20);
								List<WebElement> filterName1 = commonLibrary.isExistList(preferredFiltersList.get(i + 1), UIMAP_SearchResult.tagSpan, 20);
								String name = filterName.get(0).getText();
								String name1 = filterName1.get(0).getText();
								if (filters.toLowerCase().contains(name.toLowerCase().trim()) && filters.toLowerCase().contains(name1.toLowerCase().trim())) {
									if (!newFilters.contains(name))
										newFilters = name + ";";
									if (!newFilters.contains(name1))
										newFilters = newFilters + name1 + ";";
									String text = filterName.get(1).getText();
									String text1 = filterName1.get(1).getText();
									text = text.replaceAll("(?<=\\d),(?=\\d)", "");
									text1 = text1.replaceAll("(?<=\\d),(?=\\d)", "");
									int numberOfResults = Integer.valueOf(text);
									int numberOfResults1 = Integer.valueOf(text1);

									if (numberOfResults >= numberOfResults1) {
										posFlag++;
									} else {
										negFlag++;
									}
								}
							}
						}
						if (posFlag == (preferredFiltersList.size() - 1) && (negFlag == 0)) {
							report.updateTestLog("Verify the preferred filters " + filters + " are sorted By number of results (highest - lowest) under " + filterHeader, "The preferred filters " + newFilters + " are sorted By number of results (highest - lowest) under " + filterHeader, Status.PASS);
						} else
							report.updateTestLog("Verify the preferred filters " + filters + " are sorted By number of results (highest - lowest) under " + filterHeader, "The preferred filters " + filters + " are not sorted By number of results (highest - lowest) under " + filterHeader, Status.FAIL);
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + filters, "No Filter Selected.", Status.DONE);
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
		return new LPAResults(scriptHelper);
	}
}
