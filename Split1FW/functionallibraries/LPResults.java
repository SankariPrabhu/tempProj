package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPSHome;
import UIMAP.UIMAP_LPSReport;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.Status;

import supportlibraries.*;

public class LPResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	// private Home home = new Home(scriptHelper);
	public LPResults(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpsresults"))
				commonLibrary.sleep(50000);

		} while (!url.contains("lpsresults") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpsresults")) {
			throw new IllegalStateException("LPA Results page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout.
	// # Function Name : logout     
	// # Author : Anbu
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Dinesh
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Verdict Settlement
	// Analyser.
	// # Function Name : navigateTovsa     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements navigateTovsa() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Verdict & settlement analyzer");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Verdict & settlement analyzer");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("verdict & settlement analyzer")) {
			report.updateTestLog("Verify Verdict & Settlement Analyzer page is displayed", "Verdict & Settlement Analyzer page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Verdict & Settlement Analyzer page is displayed", "Verdict & Settlement Analyzer page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance®  Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", "Lexis Advance®  Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", "Lexis Advance®  Verdict & Settlement Analyzer logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		}
		if (browsername.contains("internet")) {
			commonLibrary.clickJS(logo, "Lexis Advance® Verdict & Settlement Analyzer");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		} else {
			commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Verdict & Settlement Analyzer");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function click on a report from search results.
	// # Function Name : clickReport     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LPSReport clickReport(String strDocTitle) {
		WebElement resultClass = null;
		WebElement doc = null;
		int counter = 0;

		doc = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10);
		do {
			doc = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10);

			if (doc == null)
				commonLibrary.sleep(5000);
			counter++;

		} while ((doc == null) && counter < 20);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassReport, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().trim().equals(strDocTitle.toLowerCase().trim())) {
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
						if (lnkDocument != null && lnkDocument.getText().trim().toLowerCase().contains(strDocTitle.trim().toLowerCase())) {
							// Clicking the report in search result header
							if (browsername.contains("internet")) {
								commonLibrary.clickLinkWithWebElementWithWaitJS(lnkDocument, lnkDocument.getText());
							} else {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
							}
							break;
						}
					}

				}
				/*
				 * WebElement
				 * h1=commonLibrary.isExist(UIMAP_SearchResult.headerh1,20);
				 * if(h1.getText().contains("Snapshot")) {
				 * report.updateTestLog("Verify 'Snapshot' page for " +
				 * strDocTitle, "'Snapshot' page for " +
				 * strDocTitle+" is displayed", Status.PASS); } if (!blnFlag)
				 * 
				 * report.updateTestLog("Verify 'Snapshot' page for " +
				 * strDocTitle, "'Snapshot' page for " +
				 * strDocTitle+" is not displayed", Status.FAIL);
				 * 
				 * }
				 */
			}
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : verifyFilterHeaderPresent
	// # Function Name : verifyFilterHeaderPresent
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LPResults verifyPostFilterHeaderPresent(String strHeader) {
		Boolean blnFlag = false;
		String[] postFilterlist = strHeader.split(";");
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);
		for (int j = 0; j < postFilterlist.length; j++) {
			for (int i = 0; i < filterHeader.size(); i++) {

				if (filterHeader.get(i).getText().toUpperCase().contains(postFilterlist[j].toUpperCase())) {
					blnFlag = true;
					break;
				}

			}
			if (blnFlag) {
				report.updateTestLog("Verify Post Filter '" + postFilterlist[j] + "' is present", "Post Filter '" + postFilterlist[j] + "' is displayed ", Status.PASS);
			} else {
				report.updateTestLog("Verify Post Filter '" + postFilterlist[j] + "' is present", "Post Filter '" + postFilterlist[j] + "' is not displayed ", Status.FAIL);
			}
		}
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPResults selectPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), strHeader);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
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
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						// WebElement span = commonLibrary.isExistNegative(item,
						// UIMAP_SearchResult.tagSpan, 10);
						if (item.getText().toLowerCase().contains(strFilter.toLowerCase())) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item, strFilter);
							else
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

			if (!Flag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
				if (!Flag) {
					report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter is not Selected.", Status.FAIL);
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
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

		return new LPResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filters present under the
	// narrow by section
	// # Function Name : verifynarrowbyfilters     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPResults verifynarrowbyfilters(String filter) {

		WebElement asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
		WebElement usedfilter = commonLibrary.isExist(asideclass, UIMAP_Document.usedfilter, 20);
		WebElement lilist = commonLibrary.isExist(usedfilter, UIMAP_Document.listItems, 20);
		WebElement narrowbyfilter = commonLibrary.isExist(lilist, UIMAP_Document.narrowbyfilter, 20);

		if (narrowbyfilter.getText().contains(filter)) {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is not displayed", Status.FAIL);
		}
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Clear Previously set filters in
	// Search Results page
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPResults clearFilters() {
		WebElement btnClear = commonLibrary.isExist(UIMAP_SearchResult.btnClear, 10);
		if (btnClear != null)
			commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : To verify message after performing search
	// # Function Name : verifyNoResultsFoundForSearch     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public LPResults verifyNoResultsFoundForSearch(String searchTerm) {

		WebElement noResultsHeader = commonLibrary.isExist(UIMAP_LPSHome.noResultsHeader, 20);

		if (noResultsHeader.getText().contains("No results")) {
			report.updateTestLog("Verify 'No Search results' message displayes", "Message '" + noResultsHeader.getText() + "' is displayed for Search Term '" + searchTerm, Status.PASS);
		} else {
			report.updateTestLog("Verify 'No Search results' message displayes", "Message '" + noResultsHeader.getText() + "' is not displayed for Search Term '" + searchTerm, Status.FAIL);
		}

		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : To verify Results header
	// # Function Name : verifyResultsHeader     
	// # Author : Anbarasan
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LPResults verifyResultsHeader(String header) {
		WebElement pageHader = commonLibrary.isExist(UIMAP_LPSReport.pageHeader, 10);
		if (pageHader == null) {
			int count = 0;
			do {
				count = count + 1;
				pageHader = commonLibrary.isExist(UIMAP_LPSReport.pageHeader, 10);
				if (pageHader == null)
					commonLibrary.sleep(5000);
			} while (pageHader == null && count <= 5);
		}
		// Verifying result page header
		if (pageHader != null && pageHader.getText().trim().toLowerCase().equals(header.trim().toLowerCase())) {
			report.updateTestLog("Verify result list page for " + header + " displays", "Result list page for " + header + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify result list page for " + header + " displays", "Result list page for " + header + " is not displayed", Status.FAIL);
		}
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LitigationProfile clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on specific page number.
	// # Function Name : ClickPageNumber
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPResults clickPageNumber(String number) {
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
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click next page.
	// # Function Name : ClickNextPage
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPResults clickNextPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
		if (btnNextPage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "NextPage");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click previous page.
	// # Function Name : ClickPageNumber
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPResults clickPreviousPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnPrePage);
		if (btnNextPage != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "Previous Page");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "Previous Page");
		} else {
			report.updateTestLog("Click on previous page", "Previous page is not clicked", Status.FAIL);
		}
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserForward
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPSReport clickBrowserForward() {
		commonLibrary.clickBrowserForward();
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDown
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPResults verifypreFilterDropDownDisplayed() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.filterArrow, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify error message for special
	// chracter seearch
	// # Function Name : verifyerrorMessage
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPResults verifyerrorMessage(String character) {
		WebElement specialcharacter = commonLibrary.isExist(UIMAP_LPSHome.specialcharContainer, 10);
		String message = specialcharacter.getText().replaceAll("\n", "");

		String display = "We are unable to process the search term you entered: " + character
				+ "Lexis Advance® does not search for single special characters or symbols, such as # $ % § = @ ^ : - \" \" /.&Please click the Back button on your browser and re-enter your search. For additional information on constructing a search query, please see How do I search using Lexis Advance®? or view the Searching with Lexis Advance® tutorial.If you continue to experience problems, please contact Customer Support at 1-800-543-6862. We apologize for the inconvenience.";
		if (message.equalsIgnoreCase(display)) {
			report.updateTestLog("Verify: error message is dispalyed", "error message is dispalyed", Status.PASS);
		} else {

			report.updateTestLog("Verify: error message is dispalyed", "error message is not dispalyed", Status.FAIL);
		}

		return new LPResults(scriptHelper);
	}
}
