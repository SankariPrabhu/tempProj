package functionallibraries;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_TaxSearchResults;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LexisAdvanceTaxResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public LexisAdvanceTaxResults(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("taxsearchresults"))
				commonLibrary.sleep(5000);

		} while (!url.contains("taxsearchresults") && counter < 40);

		if (!driver.getCurrentUrl().contains("taxsearchresults")) {
			throw new IllegalStateException("Tax Search results page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function search in LA Tax
	// # Function Name : LATaxSearch     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults laTaxSearch(String strSearchTerm) {

		WebElement txtIdMedMalSearch = commonLibrary.isExist(UIMAP_Home.txtIdtaxlSearch, 20);
		commonLibrary.setDataInTextBox(txtIdMedMalSearch, strSearchTerm, "SearchTerm");

		WebElement btnIdMedMalSearch = commonLibrary.isExist(UIMAP_Home.btnIdtaxSearch, 20);
		// commonLibrary.highlightElement(btnIdMedMalSearch);
		commonLibrary.clickButtonParentWithWait(btnIdMedMalSearch, "Search");

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack     
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result page header.
	// # Function Name : verifyResultHeader     
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultHeader(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_TaxSearchResults.resultsHeader, 10);
		String headerText = header.getText().replace("\n", " ");
		if (headerText.toLowerCase().contains(term.toLowerCase()))
			report.updateTestLog("Verify results page with header: " + term + " is displayed",
					"Results page with header: " + term + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify results page with header: " + term + " is displayed",
					"Results page with header: " + term + " is not displayed", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout     
	// # Author : Meera
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}

		WebElement lnkSignOut = commonLibrary.isExistNegative(By.linkText(UIMAP_Home.lnkTextSignOut), 15);
		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if (browsername.contains("internet"))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		}
		if (lnkSignOut != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");
		}

		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to History
	// # Function Name : navigateToHistoryFooterLink  
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public History navigateToHistoryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet")) {
					// commonLibrary.click_JS(lnkTxtViewAllHistory,
					// "View All History");
					try {
						driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
						commonLibrary.clickMouseMoveAction(lnkTxtViewAllHistory, lnkTxtViewAllHistory.getText());
					} catch (TimeoutException ex) {
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println(ex.toString());

					}
					driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
				} else {
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory, "View All History");
				}
				WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
				int count = 0;
				do {
					count++;
					rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
					commonLibrary.sleep(2000);
				} while (rslt == null && count <= 15);
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");
				List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread,
						1000);

				int count = 0;
				do {
					lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 1000);
					count = count + 1;
				} while (lstResearchThread.size() == 0 && count <= 1000);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to research map
	// # Function Name : navigateToResearchMap     
	// # Author : Meera
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToResearchMap() {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 20);
			commonLibrary.clickLinkWithWebElement(lnkTxtResearchMap, "Research Map");
			commonLibrary.sleep(10000);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying the component page
	// displayed.
	// # Function Name : verifySearchComponentPage
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickProductLogo() {
		generalFunctions.clickProductLogo();
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Un expected page is displays
	// # Function Name : verifyUnexpectedPageDisplayes     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyUnexpectedPageDisplays(boolean check) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		if (eltSearchbox == null && check)
			report.updateTestLog("Verify 'Unexpected Error Page' not displayed",
					"'Unexpected Error Page' is not displayed", Status.PASS);
		else
			report.updateTestLog("Verify 'Unexpected Error Page' not displayed", "'Unexpected Error Page' is displayed",
					Status.FAIL);

		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : verifySearchResultHeader1    
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifySearchResultHeader1(String strPageHeader) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 5);
		if (HeaderSearchResult != null) {
		} else if (HeaderSearchResult1 != null) {
		} else if (HeaderSearchResult3 != null) {
		} else if (HeaderSearchResult4 != null) {
		}

		// strPageHeader = Header.getText();

		if (HeaderSearchResult != null && HeaderSearchResult.getText().toLowerCase().contains(strPageHeader))
			report.updateTestLog("Verify results for search term " + strPageHeader + " displays in the results list",
					"Results for search term " + strPageHeader + " is displayed in the results list", Status.PASS);
		else if (HeaderSearchResult != null && HeaderSearchResult.getText().contains(strPageHeader)
				|| HeaderSearchResult1 != null && HeaderSearchResult1.getText().contains(strPageHeader))

			report.updateTestLog("Verify results for search term " + strPageHeader + " displays in the results list",
					"Results for search term " + strPageHeader + " is displayed in the results list", Status.PASS);
		else
			report.updateTestLog("Verify results for search term " + strPageHeader + " displays in the results list",
					"Results for search term " + strPageHeader + " is not displayed", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Post filter Applied
	// # Function Name : verifyFilter
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyFilter(String filterName) {
		boolean flag = false;
		commonLibrary.sleep(5000);
		WebElement filtersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		if (filtersUsed != null) {

			List<WebElement> filters = commonLibrary.isExistList(filtersUsed, UIMAP_SearchResult.lstTagListItems, 10);
			// Looping through all the applied Filters to check whether given
			// filter
			// is available.
			if (filters.size() > 0) {
				for (WebElement item : filters) {
					if (item.getText().toLowerCase().contains(filterName.toLowerCase())) {
						report.updateTestLog("Verify source: " + filterName + " displays under Narrow By",
								"Source: " + filterName + " is displayed under Narrow By", Status.PASS);
						flag = true;
					}
				}
			}
		}
		if (!flag) {
			report.updateTestLog("Verify source: " + filterName + " displays under Narrow By",
					"Source: " + filterName + " is not displayed under Narrow By", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance product
	// pages
	// # Function Name : productTaxSwitcher
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax productTaxSwitcher(String pageName) {
		try {
			commonLibrary.sleep(Mwait);
			WebElement btnIdLexisAdvance1 = commonLibrary.isExist(UIMAP_LexisAdvanceTax.practiceDiv);
			WebElement btnIdLexisAdvance = commonLibrary.isExist(btnIdLexisAdvance1, UIMAP_LexisAdvanceTax.practiceArea,
					20);

			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Practice Area");
			commonLibrary.sleep(Mwait);

			List<WebElement> product = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.btntaxpracticearea, 20);

			for (WebElement option : product) {
				if (option.getText().contains(pageName))

				{
					commonLibrary.clickLinkWithWebElement(option, pageName);
					commonLibrary.sleep(10000);
					break;
				}
			}

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
			if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase())
					|| (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
				report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is displayed",
						Status.PASS);
			} else {
				report.updateTestLog(pageName + " landing page is displayed",
						pageName + " landing page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click first Document from result
	// list and store Document Title in the data sheet.
	// # Function Name : ClickFirstDocLink1     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickFirstDocLink1(String tcName, String dataSheet, String colName) {
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
							// report.updateTestLog("Document Title ",
							// "Document Title is " + strDocTitle, Status.DONE);

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
			WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
			WebElement copyCitation = commonLibrary.isExistNegative(docHeader, UIMAP_Document.copyCitation, 10);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(2000);
			} while (copyCitation == null && count < 15);
			if (!blnFlag)

				report.updateTestLog("Click on the document " + strDocTitle,
						"Not Clicked  on the document " + strDocTitle, Status.FAIL);
			else {
				WebElement pgHeader = null;

				if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
				else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);

				if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")
						|| pgHeader.getText().contains("Expert Witness"))) {

					strDocTitle = pgHeader.getText();
					// strDocTitle=strDocTitle.replace("\n", "");
					String[] title = strDocTitle.split("\n");
					WebElement spanquery = commonLibrary.isExist(pgHeader, UIMAP_Document.pgTitleTopicSummary, 20);
					if (spanquery != null) {
						strDocTitle = spanquery.getText();
					} else {
						strDocTitle = title[1].trim();
					}

					final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
					String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator()
							+ "Datatables";
					ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
					excel.setDatasheetName("General_Data");
					int iRowNo = excel.getRowNum(tcName, 0);
					excel.setValue(iRowNo, colName, strDocTitle);
					report.updateTestLog("Verify document " + strDocTitle + " is displayed",
							"Document " + strDocTitle + " is displayed", Status.PASS);

				} else
					report.updateTestLog("Verify document " + strDocTitle + " is displayed",
							"document " + strDocTitle + " is not displayed", Status.FAIL);
			}
			return new Document(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults swithToCondentType(String strCondentType) {

		Boolean flg = false;

		for (int i = 0; i < 3; i++) {

			try {
				// function call for selecting contenttype
				if (generalFunctions.selectCondentType(strCondentType) != 1) {

					// if it's not selected again calling the function
					if (generalFunctions.selectCondentType(strCondentType) == 1) {

						flg = true;
						break;

					}

				} else {

					flg = true;
					break;
				}
			}

			catch (StaleElementReferenceException e) {

			}
		}

		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set",
					Status.FAIL);

		// wait if the page loading spinner image is displayed
		if (!browsername.contains("internet")) {

			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		}
		commonLibrary.sleep(800000);

		// wait after selecting the docket content type

		if (strCondentType.toLowerCase().contains("dockets") || strCondentType.toLowerCase().contains("news")) {

			try {
				Thread.sleep(25000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to content type
	// # Function Name : switchContentType
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults switchContentType(String ContentType) {
		boolean flag = false;
		WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);
		List<WebElement> searchContent = commonLibrary.isExistList(searchControl, UIMAP_SearchResult.lstTagListItems,
				10);
		if (searchContent.size() > 0) {
			for (WebElement item : searchContent) {
				if (item.getText().toLowerCase().contains(ContentType.toLowerCase())) {
					WebElement contentbutton = commonLibrary.isExist(item, UIMAP_SearchResult.button, 20);
					commonLibrary.clickButton(contentbutton);
					commonLibrary.sleep(40000);
					flag = true;
					break;
				}
			}
			if (!flag) {
				report.updateTestLog(
						"Verify ContentType " + ContentType + " displays in the LexisAdvanceTaxResults page",
						ContentType + " is not displayed in the LexisAdvanceTaxResults page", Status.FAIL);
			}
			return new LexisAdvanceTaxResults(scriptHelper);
		} else {
			report.updateTestLog("Verify ContentType " + ContentType + " displays in the LexisAdvanceTaxResults page",
					ContentType + " No content type is displayed in the LexisAdvanceTaxResults page", Status.FAIL);

		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result page header.
	// # Function Name : verifyResultHeader1     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultHeader1(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
		String headerText = header.getText().replace("\n", " ");
		if (headerText.toLowerCase().contains(term.toLowerCase()))
			report.updateTestLog("Verify results page with header: " + term + " is displayed",
					"Results page with header: " + term + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify results page with header: " + term + " is displayed",
					"Results page with header: " + term + " is not displayed", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for searching within the results
	// # Function Name : searchWithinSearch     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults searchWithinSearch(String sectionHeaderName, String searchTerm) {

		// Expand search within search header
		List<WebElement> eltCollapsedFilterHeader = commonLibrary
				.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);

		for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {

			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(sectionHeaderName.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), sectionHeaderName);
				report.updateTestLog("Expanding Header: " + sectionHeaderName, sectionHeaderName + " Header Expanded.",
						Status.DONE);
			}

		}

		// Enter search term
		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_SearchResult.searchWithinResults, 20);
		if (searchwithinResults != null) {
			WebElement eltSearchbox = commonLibrary.isExist(searchwithinResults,
					UIMAP_SearchResult.searchWithinSearchBox, 20);
			if (eltSearchbox != null)
				commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
			else
				report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);
		} else
			report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);

		// Click on search button
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
		if (eltSearchbutton != null)
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		else
			report.updateTestLog("Search button", "Search button is not available", Status.FAIL);

		commonLibrary.sleep(4000);

		// wait

		try {
			WebElement searchbuttonNew = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
			int count = 0;
			do {
				System.out.println("Waiting " + count);
				commonLibrary.sleep(1000000);
				searchbuttonNew = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
				count++;
			} while (searchbuttonNew.equals(eltSearchbutton) && count < 80);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public String getResultcount() {
		String list, s1 = "";
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.eltResultHeader, 20);
		if (resultList != null) {
			list = resultList.getText();
			String[] category = list.split(Pattern.quote("("));
			String[] s = category[1].split(Pattern.quote(")"));
			s1 = s[0].replace("+", "");
			report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);

		} else {
			report.updateTestLog("Getting result count", "Result header is not present", Status.FAIL);
		}

		return s1;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Clear Previously set filters in
	// Search Results page
	// # Function Name : clearFilters     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clearFilters() {
		WebElement btnClear = commonLibrary.isExist(UIMAP_SearchResult.btnClear, 10);
		if (btnClear != null)
			commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
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
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultsCount(String value1, String value2, Boolean isGreater) {

		String spValue = value1.replace(",", "");
		String spValue1 = value2.replace(",", "");
		if ((value1 != null) && (value2 != null)) {
			int valueOne = Integer.valueOf(spValue);
			int valueTwo = Integer.valueOf(spValue1);
			if (isGreater) {
				if (valueOne >= valueTwo) {
					report.updateTestLog("Verify " + value1 + " is greater than " + value2,
							value1 + " is greater than " + value2, Status.PASS);
				} else {
					report.updateTestLog("Verify " + value1 + " is greater than " + value2,
							value1 + " is lesser than " + value2, Status.FAIL);
				}

			} else {
				if (valueOne <= valueTwo) {
					report.updateTestLog("Verify " + value1 + " is lesser than " + value2,
							value1 + " is lesser than " + value2, Status.PASS);
				} else {
					report.updateTestLog("Verify " + value1 + " is lesser than " + value2,
							value1 + " is greater than " + value2, Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify String for comparison is present",
					"One/Both of the values are missing" + value1 + " or " + value2, Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : VerifyAllDocTitle     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public int getResultsListCount() {
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
		return OListItems.size();
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title link
	// # Function Name : clickDocLink1
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickDocLink1(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify group Post filter Applied
	// # Function Name : verifyGroupFilter
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyGroupFilter(String filterVer, String filterName) {
		boolean flag = false;
		commonLibrary.sleep(5000);
		String contentType = dataTable.getData("General_Data", "ContentType");
		String[] filterList = filterVer.split(";");

		WebElement filtersUsed = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 15);
		if (filtersUsed == null) {
			swithToCondentType(contentType);
			filtersUsed = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 30);
		}

		if (filtersUsed != null) {

			List<WebElement> filters = commonLibrary.isExistList(filtersUsed, UIMAP_SearchResult.lstTagListItems, 10);
			// Looping through all the applied Filters to check whether given
			// filter
			// is available.
			if (filters.size() > 0) {
				for (String filterItem : filterList) {
					for (WebElement item : filters) {
						if (item.getText().toLowerCase().contains(filterItem.toLowerCase())) {
							report.updateTestLog("Verify source: " + filterName + " displays under Narrow By",
									"source: " + filterName + " is displayed under Narrow By", Status.PASS);
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
			}
		}
		if (!flag) {
			report.updateTestLog("Verify source: " + filterName + " displays under Narrow By",
					"source: " + filterName + " is not displayed under Narrow By", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyNarrowbyFilterLeftPane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);

		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section",
					" Narrow By Section displayed at Left hand side and contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + strFilter,
					Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result page header.
	// # Function Name : verifyResultHeader2    
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultHeader2(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_TaxSearchResults.resultsHeader, 10);
		if (header != null) {
			String headerText = header.getText().replace("\n", " ");
			if (headerText.toLowerCase().contains(term.toLowerCase()))
				report.updateTestLog("Verify " + term + " displays in the sign post area",
						term + " is  displayed in the sign post area", Status.PASS);
			else
				report.updateTestLog("Verify " + term + " displays in the sign post area",
						term + " is not displayed in the sign post area", Status.FAIL);
		} else
			report.updateTestLog("Verify " + term + " displays in the sign post area",
					term + " is not displayed in the sign post area", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Alert icon and Actions
	// dropdown is displayed next to the sign post.
	// # Function Name : verifyAlertAndActionsDisplayed    
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyAlertAndActionsDisplayed() {
		boolean actionsFlag = false;
		WebElement header = commonLibrary.isExistNegative(UIMAP_TaxSearchResults.resultsHeader, 10);
		WebElement btnAlert = commonLibrary.isExistNegative(header, UIMAP_LexisAdvanceTax.alertIcon, 10);
		if (header != null) {
			List<WebElement> buttonList = commonLibrary.isExistList(header, UIMAP_TaxSearchResults.button, 10);
			if (buttonList.size() > 0) {
				for (WebElement item : buttonList) {
					if (item.getText().trim().toLowerCase().contains("actions")) {
						report.updateTestLog("Verify Actions dropdown displays next to the sign post area",
								"Actions dropdown is displayed next to the sign post area", Status.PASS);
						actionsFlag = true;
						break;
					}
				}
			}
		}
		if (!actionsFlag) {
			report.updateTestLog("Verify Actions dropdown displays next to the sign post area",
					"Actions dropdown is not displayed next to the sign post area", Status.FAIL);
		}
		if (btnAlert != null)
			report.updateTestLog("Verify Alert Icon displays next to the sign post area",
					"Alert Icon is displayed next to the sign post area", Status.PASS);
		else
			report.updateTestLog("Verify Alert Icon displays next to the sign post area",
					"Alert Icon is not displayed next to the sign post area", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ICTs display in category
	// navigation pane.
	// # Function Name : verifyICTDisplays    
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyICTDisplays() {
		WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);
		if (searchControl != null)
			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP",
					"ICTs displayed in category navigation pane that appears in LHP", Status.PASS);
		else
			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP",
					"ICTs not displayed in category navigation pane that appears in LHP", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result page header displays
	// above delivery tray
	// # Function Name : verifyResultHeaderDisplaysAboveDelivery   
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultHeaderDisplaysAboveDelivery(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
		WebElement deliveryTray = commonLibrary.isExist(UIMAP_TaxSearchResults.deliveryTray, 10);
		if (header != null && deliveryTray != null) {
			String headerText = header.getText().replace("\n", " ");
			if (headerText.toLowerCase().contains(term.toLowerCase()))
				report.updateTestLog("Verify " + term + " label displays above the delivery tray",
						term + " label is displayed above the delivery tray", Status.PASS);
			else
				report.updateTestLog("Verify " + term + " label displays above the delivery tray",
						term + " label is not displayed above the delivery tray", Status.FAIL);
		} else {
			report.updateTestLog("Verify " + term + " label displays above the delivery tray",
					term + " label is not displayed above the delivery tray", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Options
	// # Function Name : Verify_DeliveryOptions
	// # Author : Revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults verifyDeliveryOptions() {

		WebElement AddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 20);
		if (AddFolder != null)
			report.updateTestLog("Verify Delivery icons", " Add to folder icon with drop down is displayed",
					Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Add to folder icon with drop down is not displayed",
					Status.FAIL);

		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_Document.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu, UIMAP_Document.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list, By.tagName("button"), 20);
				commonLibrary.clickButtonParentWithWait(btn1.get(1), "Delivery");
				asideclass = commonLibrary.isExist(list, UIMAP_Document.tagNameAside, 10);
				break;

			}

		}
		if (asideclass != null) {
			WebElement DeliveryIcon = commonLibrary.isExist(UIMAP_Document.lnkDeliveryDownloadContent, 20);
			if (DeliveryIcon != null)
				report.updateTestLog("Verify Delivery icons", "delivery icon is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Delivery icons", "delivery icon is not displayed", Status.FAIL);
			WebElement EmailIcon = commonLibrary.isExist(UIMAP_Document.btnEmail, 20);
			if (EmailIcon != null)
				report.updateTestLog("Verify Delivery icons", "Email icon is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Delivery icons", "Email icon is ot displayed", Status.FAIL);
			WebElement PrintIcon = commonLibrary.isExist(UIMAP_Document.lnkPrint, 20);
			if (PrintIcon != null)
				report.updateTestLog("Verify Delivery icons", "Print icon is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Delivery icons", "Print icon is not displayed", Status.FAIL);
		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the pods displays in results
	// page
	// # Function Name : verifyPodsInResultsPage    
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyPodsInResultsPage(String header, String podName) {
		boolean flag = false;
		WebElement resultsList = commonLibrary.isExistNegative(UIMAP_TaxSearchResults.resultListPods, 10);
		if (resultsList != null) {
			List<WebElement> resultsListPods = commonLibrary.isExistList(resultsList, UIMAP_TaxSearchResults.button,
					10);
			if (resultsListPods.size() > 0) {
				for (WebElement item : resultsListPods) {
					if (item.getText().trim().toLowerCase().contains(podName.toLowerCase())) {
						report.updateTestLog("Verify " + podName + " pod displays in " + header + " page",
								podName + " pod is displayed in " + header + " page", Status.PASS);
						flag = true;
						break;
					}
				}
			}
		}
		if (!flag) {
			report.updateTestLog("Verify " + podName + " pod displays in " + header + " page",
					podName + " pod is not displayed in " + header + " page", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document title header and
	// other details in legal trial page
	// # Function Name : ClickDocLink1     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocTitleWhichHasTOCLink(String strDocTitle) {

		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;

		do {

			// Loop for finding the document title and click on it
			WebElement resultClass = null;

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {

				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {

					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

					for (WebElement document : OListItems) {

						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().trim().equals(strDocTitle.trim())) {

							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent,
									20);
							WebElement eleTableOfContents = commonLibrary.isExist(eleDocCondent,
									UIMAP_SearchResult.lnkTableOfCondents, 20);
							if (lnkDocument != null && eleTableOfContents != null) {

								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());

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

				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null)

					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnNextPage, "NextPage");
					} else {
						commonLibrary.clickMethod(btnNextPage, "NextPage");
					}

				commonLibrary.sleep(5000);
			}
			count++;

		} while ((blnFlag != true) && (count <= 15));

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle,
					Status.FAIL);

		else {

			WebElement pgHeader = null;
			pageCheck.positiveCheck(driver, "document", "Document");

			// wait till the document page is loaded
			int counter1 = 0;

			do {

				commonLibrary.sleep(5000);
				counter1 = counter1 + 1;

			} while (!driver.getCurrentUrl().contains("document") && counter1 <= 15);

			WebElement h1 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
			int count1 = 0;

			do {

				h1 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
				commonLibrary.sleep(2000);
				count1++;

			} while (h1 == null && count1 < 20);

			WebElement atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);
			WebElement copyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
			int counter = 0;
			do {
				counter = counter + 1;
				atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);
				copyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);

				if (atd == null && copyCitation == null)
					commonLibrary.sleep(3000);
			} while (atd == null && copyCitation == null && counter <= 20);

			// Verification Point: Document page title displayed
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 100) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);

			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);

			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed",
						pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed",
						"document " + strDocTitle + " is not displayed", Status.FAIL);

		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on toc footer
	// particular section
	// # Function Name : clickontocfooter     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clicktocfooter() {

		WebElement toc = commonLibrary.isExist(UIMAP_Document.tableOfContentsTab, 10);
		List<WebElement> toc1 = commonLibrary.isExistList(toc, UIMAP_Document.listItems, 10);
		for (WebElement item : toc1) {

			if (item.getText().contains("Search for recent developments")) {

				WebElement footer = commonLibrary.isExist(toc, UIMAP_Document.resultlink1, 10);
				if (footer != null) {

					commonLibrary.clickButtonParent(footer, "Primary Law");
				} else {

					report.updateTestLog("Click footer Button", "footer Button is not available", Status.FAIL);
				}

			}
		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verigy content displayed in ICT
	// particular section
	// # Function Name : verifyContentinICT     
	// # Author : Toya
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyContentinICT(String content) {

		WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);

		WebElement ict = commonLibrary.isExist(searchControl, UIMAP_SearchResult.ict, 20);
		if (ict.getText().contains(content)) {
			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP",
					"ICTs displayed in category navigation pane that appears in LHP", Status.PASS);
		} else {

			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP",
					"ICTs not displayed in category navigation pane that appears in LHP", Status.FAIL);

		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify no of categories expanded by
	// default
	// # Function Name : verifyCategoriesExpansion     
	// # Author : Kalai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifySnapShotExtractView(String Category) {

		WebElement snapshotsection = commonLibrary.isExist(UIMAP_SearchResult.sortByJumpList, 10);
		if (snapshotsection != null) {
			commonLibrary.selectByVisibleText(snapshotsection, Category);
			report.updateTestLog("Verify category selection from snapshot section",
					Category + " is selected from snapshot section", Status.PASS);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to sort by relevance
	// default
	// # Function Name : sortByrelevance    
	// # Author : Toya
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults sortByrelevance() {
		// String d1 = null;
		// String d2 = null;
		WebElement dropDown = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
		if (dropDown != null) {
			commonLibrary.selectFromListButton(dropDown, "Date (newest - oldest)");

		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter under Header
	// Box.
	// # Function Name : verifyFilterPresentUnderHeader
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyFilterPresentUnderHeader(String strHeader, String strFilter, Boolean blnFlag) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader,
					10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer,
					UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("expanded")) {

						List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j),
								UIMAP_SearchResult.lstTagListItems, 20);
						for (WebElement item : filters) {

							if (item != null) {
								if (item.getText().contains(strFilter)) {

									Flag = true;
									break;
								}
							}
						}
					}

				}
				if (Flag)
					break;
			}

			if (blnFlag) {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader,
							strFilter + " is  Displayed.", Status.PASS);
				else
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader,
							strFilter + " is not  Displayed.", Status.FAIL);
			} else {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader,
							strFilter + " is   Displayed.", Status.FAIL);
				else
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader,
							strFilter + " is  Not Displayed.", Status.PASS);
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting newest to oldest
	// # Function Name : verifyDateSortNewToOld
	// # Author : Senthil
	// # Date Created : 28 Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyDateSortNewToOld() {
		generalFunctions.verifyDateSortNewToOld();
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select sortby value
	// # Function Name : selectSortByValue
	// # Author : Senthil
	// # Date Created : 28 Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults selectSortByValue(String sortValue) {
		generalFunctions.selectSortBy(sortValue);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : jumpToSectionAndVerify
	// # Author : Toya
	// # Date Created : 29 Jan'16
	// #***************************************************************************************************************************
	public LexisAdvanceTaxResults jumpToSectionAndVerify(String option) {
		boolean blnFlag = false;
		generalFunctions.clickJumpTo();
		WebElement sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown,
				10);
		WebElement optionList = commonLibrary.isExistNegative(sectionsDropdown, UIMAP_Document.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_Document.tagButton, 10);
		for (WebElement item : options) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickButtonLogSmallWait(item, item.getText());
				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Select Section " + option, option + " is not selected.", Status.FAIL);
		else {
			WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
			int yToolbar = toolbar.getLocation().getY();
			int ySection = 0;
			if (!option.toLowerCase().contains("top of document")) {
				boolean flag1 = false;
				List<WebElement> jumpToParts = commonLibrary.isExistList(UIMAP_Document.jumpToParts, 10);
				for (WebElement li : jumpToParts) {
					if (li.getText().toLowerCase().contains(option.toLowerCase())) {
						ySection = li.getLocation().getY();
						flag1 = true;
						break;
					}
				}
				if (!flag1) {
					jumpToParts = commonLibrary.isExistList(UIMAP_Document.jumpToParts1, 10);
					for (WebElement li : jumpToParts) {
						if (li.getText().toLowerCase().contains(option.toLowerCase())) {
							ySection = li.getLocation().getY();
							flag1 = true;
							break;
						}
					}
				}
				if (!flag1)
					report.updateTestLog("Verify page is scrolled to section: " + option,
							"Section " + option + " is not present.", Status.FAIL);
				if ((ySection - yToolbar) >= 0 && (ySection - yToolbar) <= 1000)
					report.updateTestLog("Verify page is scrolled to section: " + option,
							"Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option,
							"Page is not scrolled to section: " + option, Status.FAIL);
			} else {
				WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
				int yHeading = txtDocumentHeading.getLocation().getY();
				if ((yHeading - yToolbar) >= 0 && (yHeading - yToolbar) <= 400)
					report.updateTestLog("Verify page is scrolled to section: " + option,
							"Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option,
							"Page is not scrolled to section: " + option, Status.FAIL);

			}
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a link section of the document
	// # Function Name : clickLinkindocumentSection
	// # Author : Toya
	// # Date Created : 29 Jan'16
	// #***************************************************************************************************************************

	public LexisAdvanceTaxResults clickLinkindocumentSection() {

		WebElement headNote = commonLibrary.isExist(UIMAP_Document.headnotes, 20);
		WebElement link = commonLibrary.isExist(headNote, UIMAP_Document.generalOverview, 20);
		if (link != null) {
			commonLibrary.clickButtonParent(link, "generalOverview");
		} else {
			report.updateTestLog("Verify general Overview link is present in the document",
					"General overview link is not present in the document", Status.FAIL);
		}

		WebElement menu = commonLibrary.isExist(UIMAP_Document.menuContainer, 20);
		WebElement getDocument = commonLibrary.isExist(menu, UIMAP_Document.getDocument, 20);
		if (getDocument != null) {
			commonLibrary.clickButtonParent(getDocument, "Get Documents");
			commonLibrary.sleep(5000);
		} else {
			report.updateTestLog("Verify get Documents link is present in the document",
					"Get Document link is not present in the document", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Teaser Text
	// # Function Name : clickTeaserText     
	// # Author : KAlai
	// # Date Created : Jan'16
	// #**********************************************
	public Document clickTeaserText(String docName) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		commonLibrary.isExist(resultClass, UIMAP_SearchResult.teaserLink, 5);
		WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		if (OListResult != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			for (WebElement document : OListItems) {
				WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
				System.out.println("eleDocTitle: " + eleDocTitle.getText());
				if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim()
						.contains(docName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
					WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserLink, 20);
					WebElement docContent1 = commonLibrary.isExist(document, UIMAP_SearchResult.termsteaserLink, 20);
					if (docContent != null) {
						commonLibrary.clickLinkWithWebElementWithWait(docContent, "Teaser Text");
						WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading,
								10);
						int count = 0;
						do {
							count++;
							commonLibrary.sleep(50000);
							txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						} while (txtDocumentHeading == null && count < 20);

						break;
					} else if (docContent1 != null) {

						commonLibrary.clickLinkWithWebElementWithWait(docContent1, "Teaser Text");
						WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading,
								10);
						int count = 0;
						do {
							count++;
							commonLibrary.sleep(50000);
							txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						} while (txtDocumentHeading == null && count < 20);

						break;
					} else
						report.updateTestLog("Click teaser text for document " + docName,
								"teaser text for document " + docName + " is not present.", Status.FAIL);
				}

			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify MouseHover Teaser
	// # Function Name : verifyMouseHoverTeaser    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public LexisAdvanceTaxResults verifyMouseHoverTeaser(String setting) {
		boolean blnFlag = false;
		WebElement docContent = null;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

				for (WebElement document : OListItems) {
					if (setting.equals("Extract")) {
						docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserLink, 20);
					} else if (setting.equals("Terms")) {
						docContent = commonLibrary.isExist(document, UIMAP_SearchResult.termsteaserLink, 20);
					}
					if (docContent != null) {
						String before = docContent.getCssValue("color");// -moz-text-decoration-color
						Actions hover = new Actions(driver);
						hover.moveToElement(docContent).build().perform();
						String after = docContent.getCssValue("color");// -moz-text-decoration-color
						if (!(before.equalsIgnoreCase(after))) {
							blnFlag = true;
							break;
						}
					}

				}
				if (blnFlag) {
					report.updateTestLog("Verify teaser link is present and color changes on mouse over",
							"Teaser link is present and color changes on mouse over", Status.PASS);
				} else {
					report.updateTestLog("Verify teaser link is present and color changes on mouse over",
							"Teaser link is present and color does not change on mouse over", Status.FAIL);
				}
			}
		}
		/*
		 * switch (setting) { case "Extract": { if (blnFlag) {
		 * report.updateTestLog(
		 * "Verify teaser link is present and color changes on mouse over",
		 * "Teaser link is present and color changes on mouse over",
		 * Status.PASS); } else { report.updateTestLog(
		 * "Verify teaser link is present and color changes on mouse over",
		 * "Teaser link is present and color does not change on mouse over",
		 * Status.FAIL); } break; } case "Terms": { if (blnFlag) {
		 * report.updateTestLog("Verify teaser link is not clickable",
		 * "Verify teaser link is not clickable", Status.FAIL); } else {
		 * report.updateTestLog("Verify teaser link is not clickable",
		 * "Verify teaser link is not clickable", Status.PASS); } break; } }
		 */
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Teaser Text
	// # Function Name : clickTeaserText     
	// # Author : KAlai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Search clickreferenceLink(String docName) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		commonLibrary.isExist(resultClass, UIMAP_SearchResult.reference, 5);
		WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		if (OListResult != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			for (WebElement document : OListItems) {
				WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
				if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim()
						.contains(docName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
					WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.reference, 20);
					if (docContent != null) {
						commonLibrary.clickLinkWithWebElementWithWait(docContent, "Reference");
						report.updateTestLog("Click reference link for document " + docName,
								"Reference link for document " + docName + " is present.", Status.PASS);
					} else
						report.updateTestLog("Click reference link for document " + docName,
								"Reference link for document " + docName + " is not present.", Status.FAIL);
				}

			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify no of categories expanded by
	// default
	// # Function Name : verifyCategoriesDefaultExpansion     
	// # Author : Kalai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyCategoriesDefaultExpansion(int count) {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement categories = commonLibrary.isExist(resultClass, UIMAP_SearchResult.resultwrapper, 20);
			if (categories != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(categories,
						UIMAP_SearchResult.eltExpandedFilterHeader1, 20);

				if (OListItems.size() == count)
					report.updateTestLog("Verify categories expansion by default in the results page",
							"No of categories to be expanded by default matches the count", Status.PASS);
				else
					report.updateTestLog("Verify categories expansion by default in the results page",
							"No of categories to be expanded by default matches the count", Status.FAIL);

			}
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify no of categories expanded by
	// default
	// # Function Name : verifyCategoriesExpansion     
	// # Author : Kalai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults selectSnapshotSection(String Category) {
		commonLibrary.sleep(10000);
		WebElement snapshotsection = commonLibrary.isExist(UIMAP_SearchResult.sortByJumpList, 20);
		if (snapshotsection != null) {
			commonLibrary.selectFromList(snapshotsection, Category);
			report.updateTestLog("Verify category selection from snapshot section",
					Category + " is selected from snapshot section", Status.PASS);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickViewAllCases
	// # Function Name : clickViewAllLinks     
	// # Author : Kalai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickViewAllLinks(String LinkName) {

		boolean flag = false;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement categories = commonLibrary.isExist(resultClass, UIMAP_SearchResult.resultwrapper, 20);
			if (categories != null) {
				List<WebElement> buttons = commonLibrary.isExistList(categories, By.tagName("button"), 20);
				for (WebElement button : buttons) {
					System.out.println("button: " + button.getText());
					if (button.getText().toLowerCase().equals(LinkName.toLowerCase())) {
						// commonLibrary.clickButtonParentWithWait(button,
						// button.getText());
						commonLibrary.clickJS(button, button.getText());
						try {
							Thread.sleep(25000);
						} catch (Exception e) {
							System.out.println(e.toString());
						}

						// commonLibrary.sleep(800000);
						flag = true;
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Click View All Cases link",
							"View All Cases link is not availabele in the results list", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify search results", "Search results are not available", Status.FAIL);

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify given term is highlighted or
	// bolded
	// # Function Name : verifyTermsBoldedHighlighted
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyTermsBoldedHighlighted(String searchTerm) {
		boolean flag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> hitList = commonLibrary.isExistList(OListResult,
						UIMAP_SearchResult.highlightedBoldedTerm, 10);
				for (WebElement item : hitList) {
					if (item.getText().equalsIgnoreCase(searchTerm)) {
						flag = true;
						report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.",
								searchTerm + " is highlighted and bolded.", Status.PASS);
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.",
							searchTerm + " is not highlighted and not bolded.", Status.FAIL);
			} else
				report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.",
						"Result List is not displayed.", Status.FAIL);
		} else
			report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.",
					"Result Container is not displayed.", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify court and date being
	// displayed aside results list
	// region
	// # Function Name : verifyCourtDatePresence   
	// # Author : Kalaivanan
	// # Date Created : 12 Sep 15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyCourtDatePresence() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
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
							// report.updateTestLog("Verify court present aside
							// results list",
							// "Court is present aside results list",
							// Status.PASS);
							courtFlag = true;
						}
						for (String monthName : monthArr) {
							if (value.getText().toLowerCase().contains(monthName.toLowerCase())) {
								// report.updateTestLog("Verify date present
								// aside results list",
								// "Date is present aside results list",
								// Status.PASS);
								dateFlag = true;
								break;
							}
						}
					}
					if (!(courtFlag) && (!(dateFlag))) {
						report.updateTestLog("Verify court present aside results list",
								"Court Date is not present aside results list", Status.FAIL);
					}
				}
			} else {
				report.updateTestLog("Verify date present aside results list", "Date is not present aside results list",
						Status.FAIL);
			}
		}
		return new LexisAdvanceTaxResults(scriptHelper);
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
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack     
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Home clickBrowserBackHome() {
		commonLibrary.clickBrowserBack();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select drop down in jump to
	// # Function Name : clickDropDownInJumpTo
	// # Author : jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults clickDropDownInJumpTo(String straction) {
		boolean flag = false;
		WebElement jumptoDropdownArrow = commonLibrary.isExist(UIMAP_Document.dropdownContainer, 20);
		WebElement button = commonLibrary.isExist(jumptoDropdownArrow, UIMAP_Document.button, 20);
		if (button != null) {
			commonLibrary.clickButton(button);
			WebElement aside = commonLibrary.isExist(jumptoDropdownArrow, UIMAP_Document.jumptoAside, 20);
			WebElement ul = commonLibrary.isExist(aside, UIMAP_Document.lstTagUList, 20);
			if (ul != null) {
				List<WebElement> ListItems = commonLibrary.isExistList(ul, By.tagName("li"), 20);
				for (WebElement item : ListItems) {
					String[] category = item.getText().split(Pattern.quote("("));
					String selection = category[0].trim();
					if (selection.toLowerCase().startsWith(straction.toLowerCase())) {
						WebElement btn = commonLibrary.isExistNegative(item, UIMAP_SearchResult.button, 10);
						commonLibrary.clickButton(btn);
						flag = true;
						report.updateTestLog("Select " + straction + "in Jump to section.",
								straction + " is selected in jump to section.", Status.PASS);
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Select " + straction + "in Jump to section.",
							straction + " is not selected in jump to section.", Status.FAIL);

			} else {
				report.updateTestLog("Select " + straction + " in Jump to section.",
						straction + " is not selected in jump to section.", Status.FAIL);
			}
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select drop down in jump to
	// # Function Name : clickDropDownInJumpTo
	// # Author : jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyHLCTPod(String straction) {
		boolean flag = false;
		WebElement resultlist = commonLibrary.isExist(UIMAP_Document.resultsList, 20);
		WebElement resultheader = commonLibrary.isExist(resultlist, UIMAP_Document.resultwrapper, 20);
		if (resultheader != null) {
			List<WebElement> ListButtons = commonLibrary.isExistList(resultheader,
					UIMAP_Document.eltExpandedFilterHeader, 20);
			for (WebElement button : ListButtons) {
				String[] category = button.getText().split(Pattern.quote("("));
				String selection = category[0].trim();
				if (selection.toLowerCase().startsWith(straction.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is present.",
							Status.PASS);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is not present.",
						Status.FAIL);
		} else
			report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is not present.",
					Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Required Filter Option in
	// LexisAdvanceTaxResults page.
	// # Function Name : verifyPostFilter
	// # Author : Pratik
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary
					.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), strHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), strHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + strHeader,
							strHeader + " filter Header Expanded.", Status.DONE);
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
					// commonLibrary.highlightElement(eltFilter.get(i));
					// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
					// eltFilter.get(i).getText());
					report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
						// eltFilter.get(i).getText());
						report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.",
								Status.DONE);
						blnFlag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Verifying Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		return new LexisAdvanceTaxResults(scriptHelper);

	}
	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// LexisAdvanceTaxResults page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults selectPostFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;

			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader,
					10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer,
					UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), strHeader);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: " + strHeader,
								strHeader + " filter Header Expanded.", Status.DONE);
					}

					List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j),
							UIMAP_SearchResult.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().toLowerCase().contains("more")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button, "More");
							else
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
					}
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j),
							UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						WebElement span = commonLibrary.isExistNegative(item, UIMAP_SearchResult.tagSpan, 10);
						if (span.getText().equals(strFilter)) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item, strFilter);
							else
								commonLibrary.clickButtonParentWithWait(item, strFilter);
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
							report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.",
									Status.DONE);
							Flag = true;
							break;
						}
					}
				}
				if (Flag)
					break;
			}

			// WebElement filterContainer =
			// commonLibrary.isExist_Negative(UIMAP_SearchResultPage.filterContainer,
			// 10);
			// List<WebElement> filterMoreList =
			// commonLibrary.isExist_List(filterContainer,
			// UIMAP_SearchResultPage.filterMore, 10);
			// for(WebElement more:filterMoreList)
			// if(more.getText().toLowerCase().contains("more"))
			// commonLibrary.clickButton_Log_SmallWait(more, "More");
			//
			// eltFilter=
			// commonLibrary.isExist_List(UIMAP_SearchResultPage.eltFilterList,10);
			// for(i=0;i<eltFilter.size();i++)
			// {
			// if(eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase()))
			// {
			// commonLibrary.highlightElement(eltFilter.get(i));
			// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
			// eltFilter.get(i).getText());
			// report.updateTestLog("Selecting Filter: "+strFilter,
			// "Required Filter Selected.", Status.DONE);
			// blnFlag=true;
			// break;
			// }
			// }
			if (!Flag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.",
								Status.DONE);
						Flag = true;
						break;
					}
				}
				if (!Flag) {
					report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter is not Selected.",
							Status.FAIL);
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		try {
			int count = 0;
			WebElement appliedFiltersOld = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
			WebElement appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
			do {
				commonLibrary.sleep(200000);
				System.out.println("Filters Waiting " + count);
				appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 2);
				count++;
			} while (appliedFiltersOld == appliedFiltersNew && count < 60);
		} catch (Exception e) {
			commonLibrary.sleep(1000000);
			System.out.println(e.toString());
		}

		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content type of Results
	// present or absent.
	// # Function Name : verifyResultContentTypes
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyResultContentTypes(String strContentType, Boolean isPresent) {
		String strDocTitle = "";
		Boolean blnFlag = false;
		int i = 0;
		if (isPresent) {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					blnFlag = false;
					for (WebElement document : OListItems) {
						i++;
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						// report.updateTestLog("Document Title ",
						// "Document Title is "+strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent,
								20);
						List<WebElement> eltContentTypeHeaders = commonLibrary.isExistList(eleDocCondent,
								UIMAP_SearchResult.eltContentTypeHeader, 10);
						for (WebElement header : eltContentTypeHeaders) {
							if (header.getText().toLowerCase().contains(strContentType.toLowerCase())) {
								report.updateTestLog("Verify Content Type " + strContentType + " is present.",
										"Content Type " + strContentType + " is present for document." + strDocTitle,
										Status.PASS);
								blnFlag = true;
								break;
							}
						}
						if (i > 15 || blnFlag)
							break;
					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify Content Type " + strContentType + " is present.",
						"Content Type " + strContentType + " is not present.", Status.FAIL);

		} else {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					i++;
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					blnFlag = false;
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						// report.updateTestLog("Document Title ",
						// "Document Title is "+strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent,
								20);
						List<WebElement> eltContentTypeHeaders = commonLibrary.isExistList(eleDocCondent,
								UIMAP_SearchResult.eltContentTypeHeader, 10);
						for (WebElement header : eltContentTypeHeaders) {
							if (header.getText().toLowerCase().contains(strContentType.toLowerCase())) {
								report.updateTestLog("Verify Content Type " + strContentType + " is not present.",
										"Content Type " + strContentType + " is present for document " + strDocTitle,
										Status.FAIL);
								blnFlag = true;
								break;
							}
						}
						if (i > 15 || blnFlag)
							break;
					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify Content Type " + strContentType + " is not present.",
						"Content Type " + strContentType + " is not present.", Status.PASS);

		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PostFilter Absent
	// # Function Name :verifyPostFilterAbsent    
	// # Author : Ram
	// # Date Created : Feb '15
	// #*****************************************************************************************************
	public LexisAdvanceTaxResults verifyPostFilterAbsent(String header) {
		boolean flag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);
		for (WebElement li : filterHeader) {
			if (li.getText().toLowerCase().contains(header.toLowerCase())) {
				report.updateTestLog("Verify " + header + " filters are absent", header + " filters are present.",
						Status.FAIL);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + header + " filters are absent", header + " filters are absent.",
					Status.PASS);
		return new LexisAdvanceTaxResults(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifyNarrowbynotinsnapshort     
	// # Author : Araviond Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyNarrowbynotinsnapshort(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);

		if (usedfilter == null) {
			report.updateTestLog("To Verify narrow by in snap short page", "Narrow By Section does not contains in snapshort", Status.PASS);
		} else {
			report.updateTestLog("To Verify Narrow By Section in snapshor", " Narrow By Section displayed at Left hand side and contains " + strFilter, Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

}
