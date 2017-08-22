package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.*;

public class LegalIssueTrail extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public LegalIssueTrail(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("litresults"))
				commonLibrary.sleep(5000);

		} while (!url.contains("litresults") && counter < 40);

		if (!driver.getCurrentUrl().contains("litresults")) {
			throw new IllegalStateException("legal issue trail page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the LexisAdvanceResearch Logo
	// # Function Name : verifyLexisAdvanceResearchLogo     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyLexisAdvanceResearchLogo() {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().contains("Research")) {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo displays in Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo is not displayed in Global menu", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for log out
	// # Function Name : logout
	// # Author : Uma
	// # Date Created : Jan'15
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
	// # Function Description : Function for verifying search results header
	// # Function Name : verifySearchResultHeader
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySearchResultHeader(String strPageHeader) {

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

		// strPageHeader=Header.getText();

		if (Header != null && Header.getText().replace("\n", "").toLowerCase().contains(strPageHeader.toLowerCase()))// ||
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

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else if (Header != null && HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);

		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting document from LIT page
	// # Function Name : selectDocumentFromLITPage
	// # Author : Vidhya
	// # Date Created : Apr'15'
	// #*****************************************************************************************************************************
	public Document selectDocumentFromLITPage(String DocName) {

		try {
			// driver.manage().timeouts().pageLoadTimeout(220,
			// TimeUnit.SECONDS);
			List<WebElement> documentList = commonLibrary.isExistList(UIMAP_GUIBInstances.lnkdoctitle, 5);
			boolean blnFlag = false;
			for (int i = 0; i < documentList.size(); i++) {
				if (documentList.get(i).getText().contains(DocName)) {
					commonLibrary.clickJSObjType(documentList.get(i), DocName + " document title from LIT page");

					blnFlag = true;
					break;
				}
			}

			if (!blnFlag)
				report.updateTestLog("Select the document :" + DocName + "from LIT page", DocName + "from LIT page is not selected", Status.FAIL);
			WebElement citation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
			int count = 0;
			do {
				count++;
			} while (citation == null && count < 20);
			return new Document(scriptHelper);
		} catch (Exception e) {

		}
		return null;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to the history footer
	// link
	// # Function Name : NavigateToHistryFooterLink
	// # Author : Vidhya
	// # Date Created : Apr'15'
	// #*****************************************************************************************************************************
	public ResearchMap navigateToHistryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 10);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");

				commonLibrary.sleep(50000);

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		WebElement rmDiv = commonLibrary.isExist(UIMAP_ResearchMap.resultDiv, 10);
		int count = 0;
		do {
			count++;
			commonLibrary.sleep(3000);
		} while (rmDiv == null && count < 30);
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking back button to go to
	// previous page
	// # Function Name : Clickbrowserback
	// # Author : Vidhya
	// # Date Created : Apr'15'
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickbrowserbackLIT() {

		// // driver.navigate().back();
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(50000);
		//
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
		// // driver.manage().timeouts().pageLoadTimeou(220,
		// // TimeUnit.MILLISECONDS);
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// report.updateTestLog("Click on Browser Back",
		// "Browser Back not clicked", Status.FAIL);
		// }

		commonLibrary.clickBrowserBack();
		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the number of results
	// # Function Name : verifyNumberOfResults     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyNumberOfResults(int number, boolean greater) {
		generalFunctions.verifyNumberOfResults(number, greater);

		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the previous button status
	// # Function Name : VerifyPreviousButtonStatus     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyPreviousButtonStatus(String status) {
		boolean active = false;
		boolean inactive = false;
		WebElement navigate = commonLibrary.isExist(UIMAP_SearchResult.previousBtn, 10);
		if (navigate != null) {
			switch (status) {
			case "active": {
				List<WebElement> li = commonLibrary.isExistList(navigate, UIMAP_SearchResult.lstTagListItems, 10);
				for (WebElement item : li) {
					WebElement link = commonLibrary.isExist(item, UIMAP_SearchResult.previousBtnActive, 10);
					if (link != null) {
						active = true;
						break;
					}
				}
				if (active) {
					report.updateTestLog("Verify Previous button is active in the <Go to page widget> at the bottom of the page", "Previous button is active in the <Go to page widget> at the bottom of the page", Status.PASS);
				} else {
					report.updateTestLog("Verify Previous button is active in the <Go to page widget> at the bottom of the page", "Previous button is not active in the <Go to page widget> at the bottom of the page", Status.FAIL);
				}
				break;
			}
			case "inactive": {
				List<WebElement> li = commonLibrary.isExistList(navigate, UIMAP_SearchResult.lstTagListItems, 10);
				for (WebElement item : li) {
					WebElement link = commonLibrary.isExist(item, UIMAP_SearchResult.previousBtnInActive, 10);
					if (link != null) {
						inactive = true;
						break;
					}
				}
				if (inactive) {
					report.updateTestLog("Verify Previous button is inactive in the <Go to page widget> at the bottom of the page", "Previous button is inactive in the <Go to page widget> at the bottom of the page", Status.PASS);
				} else {
					report.updateTestLog("Verify Previous button is inactive in the <Go to page widget> at the bottom of the page", "Previous button is not inactive in the <Go to page widget> at the bottom of the page", Status.FAIL);
				}
				break;
			}
			}
		} else {
			report.updateTestLog("Verify Previous button is " + status + " in the <Go to page widget> at the bottom of the page", "Previous button is not present in the <Go to page widget> at the bottom of the page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the next button status
	// # Function Name : VerifyNextButtonStatus     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyNextButtonStatus(String status) {
		boolean active = false;
		boolean inactive = false;
		commonLibrary.sleep(50000);
		WebElement navigate = commonLibrary.isExist(UIMAP_SearchResult.previousBtn, 10);
		if (navigate != null) {
			switch (status) {
			case "active": {
				List<WebElement> li = commonLibrary.isExistList(navigate, UIMAP_SearchResult.lstTagListItems, 10);
				for (@SuppressWarnings("unused")
				WebElement item : li) {
					WebElement link = commonLibrary.isExist(UIMAP_SearchResult.NextBtnActive, 10);

					int count = 0;
					do {
						link = commonLibrary.isExist(UIMAP_SearchResult.NextBtnActive, 10);
						if (link != null) {
							active = true;
							break;
						}
						count++;
						commonLibrary.sleep(2000);
					} while (link == null && count < 5);

					if (active)
						break;
				}
				if (active) {
					report.updateTestLog("Verify next button is active in the <Go to page widget> at the bottom of the page", "next button is active in the <Go to page widget> at the bottom of the page", Status.PASS);
				} else {
					report.updateTestLog("Verify next button is active in the <Go to page widget> at the bottom of the page", "next button is not active in the <Go to page widget> at the bottom of the page", Status.FAIL);
				}
				break;
			}
			case "inactive": {
				List<WebElement> li = commonLibrary.isExistList(navigate, UIMAP_SearchResult.lstTagListItems, 10);
				for (@SuppressWarnings("unused")
				WebElement item : li) {
					WebElement link = commonLibrary.isExist(UIMAP_SearchResult.NextBtnInActive, 10);
					if (link != null) {
						inactive = true;
						break;
					}
				}
				if (inactive) {
					report.updateTestLog("Verify next button is inactive in the <Go to page widget> at the bottom of the page", "next button is inactive in the <Go to page widget> at the bottom of the page", Status.PASS);
				} else {
					report.updateTestLog("Verify next button is inactive in the <Go to page widget> at the bottom of the page", "next button is not inactive in the <Go to page widget> at the bottom of the page", Status.FAIL);
				}
				break;
			}
			}
		} else {
			report.updateTestLog("Verify Previous button is " + status + " in the <Go to page widget> at the bottom of the page", "next button is not present in the <Go to page widget> at the bottom of the page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the particular page number
	// # Function Name : ClickPageNumber     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail clickPageNumber(String number) {
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 20);
		for (WebElement item : pageNumbers) {
			if (item.getAttribute("data-value").equalsIgnoreCase(number)) {
				report.updateTestLog("Verify number " + number + " is clickable and hyperlinked", "number " + number + " is clickable and hyperlinked", Status.PASS);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(item, "Page Number " + number + "");
				else
					commonLibrary.clickButtonParentWithWait(item, "Page Number " + number + "");
				break;
			}

		}

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
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify particular pageno's results
	// are displayed
	// # Function Name : VerifyPageNumberHighlighted     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyPageNumberHighlighted(String highlight) {

		boolean ishighlight = false;
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);

		WebElement current = commonLibrary.isExist(pagination, UIMAP_SearchResult.currentPage, 20);
		if (current.getText().equalsIgnoreCase(highlight)) {
			ishighlight = true;
		}
		if (ishighlight) {
			report.updateTestLog("Verify LIT results of page number " + highlight + " is displayed", "LIT results of page number " + highlight + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify LIT results of page number " + highlight + " is displayed", "LIT results of page number " + highlight + " is not displayed", Status.FAIL);
		}

		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on previous button
	// # Function Name : ClickPreviousPage     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickPreviousPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnPrePage);
		if (btnNextPage != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "Previous Page");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "Previous arrow");
		} else {
			report.updateTestLog("Click on previous arrow in the page navigation widget", "Previous arrow in the page navigation widget is not clicked", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of pages to be displayed
	// # Function Name : SetNumberOfResultsToBeDisplayed     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail setNumberOfResultsToBeDisplayed(String number) {
		generalFunctions.setNumberOfResultsToBeDisplayed(number);
		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document title header and
	// other details in legal trial page
	// # Function Name : verifydocumentheaderdetails     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail verifydocumentheaderdetails(String DocTitle, String Jurisdiction, String court, String dateTime) {
		boolean jurPresent = false;
		boolean datePresent = false;
		boolean courtPresent = false;
		boolean allData = false;
		boolean docTil = false;
		int count = 0;

		for (int k = 0; k <= 3; k++) {

			WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
			if (rslt != null) {
				WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
				if (div != null) {
					List<WebElement> contentList = commonLibrary.isExistList(div, UIMAP_SearchResult.lstTagName, 20);
					for (WebElement item : contentList) {
						WebElement trailHeader = commonLibrary.isExistNegative(item, UIMAP_SearchResult.tagHeader, 10);
						List<WebElement> contentLink = commonLibrary.isExistList(trailHeader, UIMAP_SearchResult.link, 20);
						for (WebElement item1 : contentLink) {
							count++;
							if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
								docTil = true;
								List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
								List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
								for (int i = 0; i < clientHeading.size(); i++) {
									if (clientHeading.get(i).getText().equalsIgnoreCase("Jurisdiction")) {
										if (Jurisdiction.equalsIgnoreCase("Exists")) {
											if (clientVal.get(i).getText() != "") {
												jurPresent = true;
											}
										} else {
											if (clientVal.get(i).getText().equalsIgnoreCase(Jurisdiction)) {
												jurPresent = true;
											}
										}
									} else if (clientHeading.get(i).getText().equalsIgnoreCase("Date")) {
										if (dateTime.equalsIgnoreCase("Exists")) {
											if (clientVal.get(i).getText() != "") {
												datePresent = true;
											}
										} else {
											if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
												datePresent = true;
											}
										}
									} else if (clientHeading.get(i).getText().equalsIgnoreCase("Court")) {
										if (clientVal.get(i).getText().equalsIgnoreCase(court)) {
											courtPresent = true;
										}
									}
									if (docTil && jurPresent && datePresent && courtPresent)
										break;
								}

							}
							if (docTil && jurPresent && datePresent && courtPresent) {
								allData = true;
								break;
							}
							if (count == 10 || count == 20 || count == 30) {
								break;
							}

						}
						if (allData)
							break;
						if (count == 10 || count == 20 || count == 30) {
							break;
						}
					}
				}
			}
			if (allData) {
				break;
			}

			else if (!allData) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWaitJS(btnNextPage, "Next Page");
				else
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			}

		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, Jurisdiction, court and DateTime is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!jurPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Jurisdiction is NOT displayed as expected", Status.FAIL);
			if (!courtPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "court is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

		}

		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document title header and
	// other details in legal trial page
	// # Function Name : ClickDocLink1     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to browser back from the current page
	// # Function Name : browserback     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail browserback() {
		commonLibrary.clickBrowserBack();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search
	// # Function Name : simpleSearch     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down
	// # Function Name : selectSortBy
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail selectSortBy(String value) {
		generalFunctions.selectSortBy(value);
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// ascending order
	// # Function Name : verifyDocTitleSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyDocTitleSortedResultAZ() {
		generalFunctions.verifyDocTitleSortedResultAZ();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// descending order
	// # Function Name : verifyDocTitleSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyDocTitleSortedResultZA() {
		generalFunctions.verifyDocTitleSortedResultZA();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// ascending order
	// # Function Name : verifyJurisSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyJurisSortedResultAZ() {
		generalFunctions.verifyJurisSortedResultAZ();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// descending order
	// # Function Name : verifyJurisSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyJurisSortedResultZA() {
		generalFunctions.verifyJurisSortedResultZA();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting highest to
	// lowest
	// # Function Name : verifySortCourtHighToLow
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySortCourtHighToLow() {
		generalFunctions.verifySortCourtHighToLow();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting lowest to
	// highest
	// # Function Name : verifySortCourtLowTohigh
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySortCourtLowTohigh() {
		generalFunctions.verifySortCourtLowTohigh();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting newest to oldest
	// # Function Name : verifyDateSortNewToOld
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyDateSortNewToOld() {
		generalFunctions.verifyDateSortNewToOld();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting oldest to newest
	// # Function Name : verifyDateSortOldtoNew
	// # Author : Shobana
	// # Date Created : 26 Feb'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyDateSortOldtoNew() {
		generalFunctions.verifyDateSortOldtoNew();
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Doc Negative Treatment
	// # Function Name : clickDocNT     
	// # Author : Aravind M
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Shepards clickDocNT(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null && eleDocTitle.getText().trim().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement eleClickNTAnchor = commonLibrary.isExist(document, UIMAP_Document.eleClickNTAnchor, 20);
							if (eleClickNTAnchor != null) {
								commonLibrary.clickLinkWithWebElementWithWait(eleClickNTAnchor, eleClickNTAnchor.getText());
								blnFlag = true;
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
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "Document " + strDocTitle + " is displayed.", Status.PASS);
			report.updateTestLog("Click Negative treatment anchor", "Negative treatment anchor in document clicked.", Status.PASS);
		} else
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Document Link
	// # Function Name : clickDocNT     
	// # Author : Aravind M
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickDocLink2(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				List<WebElement> OListResult = commonLibrary.isExistList(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					for (int index = 0; index < OListResult.size(); index++) {
						List<WebElement> OListItems = commonLibrary.isExistList(OListResult.get(index), By.tagName("li"), 20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
							if (eleDocTitle != null && eleDocTitle.getText().trim().toLowerCase().equals(strDocTitle.toLowerCase())) {
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
							if (blnFlag)
								break;
						}
						if (blnFlag)
							break;
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
		WebElement btnCopyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
		int count = 0;
		do {
			count++;
			commonLibrary.sleep(2000);
		} while (btnCopyCitation == null && count < 15);
		pageCheck.positiveCheck(driver, "document", "Document");

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail selectDocumentByTitle(String DocName) {
		generalFunctions.selectDocumentByTitle(DocName);
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add to a folder
	// # Function Name : addToFolder
	// # Author : Pratik
	// # Date Created : Jun 2015
	// #*****************************************************************************************************************************

	public LegalIssueTrail addToFolder(String folderName) {
		generalFunctions.addToFolder(folderName);
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to a folder
	// # Function Name : navigateToFolder
	// # Author : Pratik
	// # Date Created : Jun 2015
	// #*****************************************************************************************************************************
	public WorkFolders navigateToFolders() {
		generalFunctions.navigateToFolders();
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking back button to go to
	// previous page
	// # Function Name : clickbrowserback
	// # Author : Aravind
	// # Date Created : Apr'15'
	// #*****************************************************************************************************************************
	public Document clickbrowserback() {
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(5000);
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
		// } catch (Exception e) {
		// e.printStackTrace();
		// report.updateTestLog("Click on Browser Back",
		// "Browser Back not clicked", Status.FAIL);
		// }
		commonLibrary.clickBrowserBack();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking back button to go to
	// previous page
	// # Function Name : Clickbrowserback
	// # Author : Vidhya
	// # Date Created : Apr'15'
	// #*****************************************************************************************************************************
	public Document clickbrowserbackToDoc() {

		// // driver.navigate().back();
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(50000);
		//
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
		// // driver.manage().timeouts().pageLoadTimeou(220,
		// // TimeUnit.MILLISECONDS);
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// report.updateTestLog("Click on Browser Back",
		// "Browser Back not clicked", Status.FAIL);
		// }
		commonLibrary.clickBrowserBack();

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document title header and
	// other details in legal trial page
	// # Function Name : ClickPrevPageIfExists     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail clickPrevPageIfExists() {
		commonLibrary.sleep(5000);
		WebElement btnPrevPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnPrePage, 10);
		if (btnPrevPage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(btnPrevPage, "Previous Page");
			else
				commonLibrary.clickButtonParentWithWait(btnPrevPage, "Previous Page");

		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the selected passage
	// # Function Name : verifySelectedPassageDisplay     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySelectedPassageDisplay() {
		WebElement spanSelectedPassage = commonLibrary.isExist(UIMAP_LegalIssueTrail.spanSelectedPassage, 20);
		if (spanSelectedPassage != null && spanSelectedPassage.isDisplayed()) {
			report.updateTestLog("Verify Selected Passage Section is displayed", "Selected Passage section is displayed in LIT Page", Status.PASS);
		} else {
			report.updateTestLog("Verify Selected Passage section is displayed", "Selected Passage section is not displayed in LIT Page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the citation results header
	// # Function Name : headerCitations     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyCitationHeaderDisplay() {
		WebElement headerCitations = commonLibrary.isExist(UIMAP_LegalIssueTrail.headerCitations, 20);
		if (headerCitations != null && headerCitations.isDisplayed()) {
			report.updateTestLog("Verify Citation Header is displayed", "Citation Header is displayed in LIT Page", Status.PASS);

			if (headerCitations.getText().contains("Citations")) {
				report.updateTestLog("Verify Citation Header Name is displayed as 'Citations'", "Citation Header Name is displayed as 'Citations'", Status.PASS);
			} else {
				report.updateTestLog("Verify Citation Header Name is displayed as 'Citations'", "Citation Header Name is not displayed as 'Citations'", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Selected Passage section is displayed", "Selected Passage section is not displayed in LIT Page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the cited cases Section
	// Header
	// # Function Name : verifySectionHeaderSection1Display     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySectionHeaderSection1Display(String headerSectiontext) {
		List<WebElement> wrapperResultHeader = commonLibrary.isExistList(UIMAP_LegalIssueTrail.wrapperResultHeader, 20);
		if (wrapperResultHeader != null && wrapperResultHeader.get(0).getText().equalsIgnoreCase(headerSectiontext)) {
			report.updateTestLog("Verify cited cases header display", "Cited Cases section header is displayed as :" + "'" + headerSectiontext + "'", Status.PASS);
		} else {
			report.updateTestLog("Verify cited cases header display", "Cited Cases section header is not displayed as :" + "'" + headerSectiontext + "'", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to CLick the "Show more text"
	// # Function Name : verifyShowMoreText()    
	// # Author : Bharath VMP
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickShowMoreText() {
		WebElement showMoreText = commonLibrary.isExist(UIMAP_LegalIssueTrail.showMoreText, 20);
		if (showMoreText != null && showMoreText.isDisplayed()) {
			showMoreText.click();
		} else {
			report.updateTestLog("Verify  Show more text option is displayed", "Show more text option is not displayed in LIT Page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the cases cited Section
	// Header
	// # Function Name : verifySectionHeaderSection2Display     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifySectionHeaderSection2Display(String headerSectiontext) {
		List<WebElement> wrapperResultHeader = commonLibrary.isExistList(UIMAP_LegalIssueTrail.wrapperResultHeader, 20);
		if (wrapperResultHeader != null && wrapperResultHeader.get(1).getText().equalsIgnoreCase(headerSectiontext)) {
			report.updateTestLog("Verify cases cited header display", "Cases Cited section header is displayed as :" + "'" + headerSectiontext + "'", Status.PASS);
		} else {
			report.updateTestLog("Verify cases cited header display", "Cases Cited section header is not displayed as :" + "'" + headerSectiontext + "'", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the "Show more text"
	// # Function Name : verifyShowMoreText()    
	// # Author : Bharath VMP
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail verifyShowMoreText() {
		WebElement showMoreText = commonLibrary.isExist(UIMAP_LegalIssueTrail.showMoreText, 20);
		if (showMoreText != null && showMoreText.isDisplayed()) {
			report.updateTestLog("Verify Show more text option is displayed", "Show more text option is displayed in LIT Page", Status.PASS);
		} else {
			report.updateTestLog("Verify  Show more text option is displayed", "Show more text option is not displayed in LIT Page", Status.FAIL);
		}
		return new LegalIssueTrail(scriptHelper);
	}

}
