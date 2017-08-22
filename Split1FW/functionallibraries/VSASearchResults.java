package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.cognizant.framework.ExcelDataAccess;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_VSASearchResults;
import UIMAP.UIMAP_VerdictsSettlements;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

public class VSASearchResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private ExcelDataAccess excel;

	public VSASearchResults(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("search"))
				commonLibrary.sleep(10000);

		} while (!url.contains("search") && counter < 40);

		if (!driver.getCurrentUrl().contains("search")) {
			throw new IllegalStateException("Verdicts & Settlements Analyzer Results page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Header.
	// # Function Name : verifyResultPageNormalSearch     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyResultPageNormalSearch(String SearchTerm) {
		try {
			WebElement hdrResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult1, 10);
			if (hdrResult1 == null) {
				report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
				commonLibrary.clickBrowserBack();
				this.logout();

			}
			if (hdrResult1.getText().contains(SearchTerm)) {
				report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {

		}

		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Page Number.
	// # Function Name : verifyPageNumberHighlightedAndLastPageNo     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyPageNumberHighlightedAndLastPageNo(String highlight) {
		int m = 0;
		int n = 0;
		boolean istotal = false;
		boolean ishighlight = false;
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		if (pagination != null) {
			List<WebElement> li = commonLibrary.isExistList(pagination, UIMAP_SearchResult.lstTagListItems, 10);
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getAttribute("class").equalsIgnoreCase("continued")) {
					m = i;
					break;
				}
			}
			if (li.get(li.size() - 2).getAttribute("class").equalsIgnoreCase("current") || li.get(li.size() - 2).getAttribute("class").equalsIgnoreCase("overflow")) {
				n = li.size() - 2;
				if (m < n) {
					istotal = true;
				}
			}
		}

		WebElement current = commonLibrary.isExist(pagination, UIMAP_SearchResult.currentPage, 20);
		if (current.getText().equalsIgnoreCase(highlight)) {
			ishighlight = true;
		}
		if (istotal && ishighlight) {
			if (highlight == "2") {
				report.updateTestLog("Verify 1 2 3 4 5  ... 20 displays in the Go to PAGE WIDGET and link 2 is highlighted", "1 2 3 4 5  ... 20 displays in the Go to PAGE WIDGET and link 2 is highlighted", Status.PASS);
			} else if (highlight == "5") {
				report.updateTestLog("Verify 1 ... 3 4 5 6 7 ...20 displays in the Go to Page widget and link 5 is highlighted.", "1 ... 3 4 5 6 7 ...20 displays in the Go to Page widget and link 5 is highlighted.", Status.PASS);
			} else if (highlight == "20") {
				report.updateTestLog("Verify 1 ... 16 17 18 19 20 displays in the Go to Page widget and link 20 is highlighted", "1 ... 16 17 18 19 20 displays displays in the Go to Page widget and link 20 is highlighted", Status.PASS);
			} else if (highlight == "19") {
				report.updateTestLog("Verify 1 ... 16 17 18 19 20 displays in the Go to Page widget and link 19 is highlighted", "1 ... 16 17 18 19 20 displays in the Go to Page widget and link 76 19 highlighted", Status.PASS);
			}
		} else {
			report.updateTestLog("Verify " + highlight + " is highlighted in the Go to Widget page and last page number is displayed", "" + highlight + " is not highlighted in the Go to Widget page and last page number is not displayed", Status.FAIL);
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click page Number.
	// # Function Name : clickPageNumber     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickPageNumber(String number) {
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 20);
		for (WebElement item : pageNumbers) {
			if (item.getAttribute("data-value").equalsIgnoreCase(number)) {
				commonLibrary.clickButtonParentWithWait(item, "Page Number " + number + "");
				break;
			}

		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify on First Document Title
	// # Function Name : verifyFirstDocTitle     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyFirstDocTitle(String strDocTitle) {
		Boolean blnFlag = false;

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
				WebElement eleDocTitle = commonLibrary.isExist(OListItems.get(0), UIMAP_SearchResult.TitleClassDoc, 2);
				if (eleDocTitle != null) {
					if (eleDocTitle.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						report.updateTestLog("Verify Result list displays from " + strDocTitle, "Result list displays from " + strDocTitle, Status.PASS);
						blnFlag = true;

					}
				}

			}
		}

		if (!blnFlag)
			report.updateTestLog("Verify Result list displays from " + strDocTitle, "Result list does not display from " + strDocTitle, Status.FAIL);
		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {

		// Function call for logout
		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : ClickDocLink     
	// # Author : Veeshma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document clickDocLink(String strDocTitle) {
		Boolean blnFlag = false;

		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_VSASearchResults.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_VSASearchResults.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
						if (lnkDocument != null) {
							if (browsername.contains("internet")) {
								commonLibrary.clickLinkWithWebElementWithWaitJS(lnkDocument, lnkDocument.getText());
							} else {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
							}

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

			WebElement atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);

			int counter = 0;
			do {
				counter = counter + 1;
				atd = commonLibrary.isExist(UIMAP_Document.ATD, 20);
				if (atd == null)
					commonLibrary.sleep(5000);
			} while (atd == null && counter <= 40);

			WebElement pgHeader = null;
			commonLibrary.sleep(50000);
			if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : ClickDocLink     
	// # Author : Veeshma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkDocument(String strDocTitle) {
		// WebElement btnNextPage = null;
		// boolean blnFlag = false;
		// int count = 1;
		// do {
		// WebElement resultClass = null;
		//
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		//
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
		// if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLink_withWebElement_WithWait_JS(lnkDocument,
		// lnkDocument.getText());
		// } else {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// }
		// blnFlag = true;
		// btnNextPage = null;
		// break;
		// }
		// }
		//
		// }
		//
		// }
		// }
		//
		// if (!blnFlag) {
		// btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
		// if (btnNextPage != null)
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLink_withWebElement_WithWait_JS(btnNextPage,
		// "NextPage");
		// } else {
		// commonLibrary.clickLink_withWebElement_WithWait(btnNextPage,
		// "NextPage");
		// }
		// }
		// count++;
		// } while (btnNextPage != null && count <= 15);
		//
		// if (!blnFlag)
		//
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		// WebElement pgHeader = null;
		// pageCheck.PositiveCheck(driver, "document", "Document");
		// int counter1 = 0;
		// do {
		// commonLibrary.sleep(5000);
		// counter1 = counter1 + 1;
		// } while (!driver.getCurrentUrl().contains("document") && counter1 <=
		// 15);
		//
		// if (commonLibrary.isExist_Negative(UIMAP_SearchResult.TitleClassTOC,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_SearchResult.TitleClassTOC, 10);
		// else if
		// (commonLibrary.isExist_Negative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10);
		// else if
		// (commonLibrary.isExist_Negative(UIMAP_SearchResult.SearchResultHeader3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_SearchResult.SearchResultHeader3,
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
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the display of access popup
	// in VSA
	// # Function Name : verifyAccessPopup
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public VerdictsSettlements verifyAccessPopup(String title, String message, String listprice, String clientName, String button1, String button2) {
		WebElement vsatitle = commonLibrary.isExist(UIMAP_VSASearchResults.vsatitle);
		if (vsatitle.getText().contains(title)) {
			report.updateTestLog("Verification of VSA Title", "VSA contains the text " + title, Status.PASS);
		} else {
			report.updateTestLog("Verification of VSA Title", "VSA does not contains the text " + title, Status.FAIL);
		}
		WebElement vsagraph = commonLibrary.isExist(UIMAP_VSASearchResults.vsagraph);
		if (vsagraph.isDisplayed()) {
			report.updateTestLog("Verification of VSA Graph", "VSA Graph is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verification of VSA Graph", "VSA Graph is displayed", Status.FAIL);
		}
		WebElement innermessage = commonLibrary.isExist(UIMAP_VSASearchResults.vsainnerbox);
		if (innermessage.getText().contains(message)) {
			report.updateTestLog("Verification of VSA text in Inner Message", "VSA Access item popup contains the text " + message, Status.PASS);
		} else {
			report.updateTestLog("Verification of VSA  text in Inner Message", "VSA Access item popup does not contain the text " + message, Status.FAIL);
		}
		WebElement actuallistprice = commonLibrary.isExist(UIMAP_VSASearchResults.listprice);
		if (actuallistprice.getText().contains(listprice)) {
			report.updateTestLog("Verification of List Price", "VSA Access item popup contains the list price " + listprice, Status.PASS);
		} else {
			report.updateTestLog("Verification of List Price", "VSA Access item popup does not contain the list price " + listprice, Status.FAIL);
		}
		if (actuallistprice.getText().contains("Your Price")) {
			report.updateTestLog("Verification of Your Price", "VSA Access item popup contains the Your price ", Status.PASS);
		} else {
			report.updateTestLog("Verification of Your Price", "VSA Access item popup does not contain the Your price ", Status.FAIL);
		}
		WebElement clientID = commonLibrary.isExist(UIMAP_VSASearchResults.vsaclientid);

		if (clientID.getText().contains(clientName)) {
			report.updateTestLog("Verification of Client ID", "VSA Access item popup contains the Client ID " + clientID, Status.PASS);
		} else {
			report.updateTestLog("Verification of Client ID", "VSA Access item popup does not contain the Client ID " + clientID, Status.FAIL);
		}

		WebElement getitbutton = commonLibrary.isExist(UIMAP_VSASearchResults.vsagetitnow);
		if (getitbutton.getAttribute("value").contains(button1)) {
			report.updateTestLog("Verification of Client ID Buttons", "VSA Access item popup contains the button " + button1, Status.PASS);
		} else {
			report.updateTestLog("Verification of Client ID Buttons", "VSA Access item popup does not contain the button " + button1, Status.FAIL);
		}
		WebElement cancelbutton = commonLibrary.isExist(UIMAP_VSASearchResults.vsacancel);
		if (cancelbutton.getAttribute("value").contains(button2)) {
			report.updateTestLog("Verification of Client ID Buttons", "VSA Access item popup contains the button " + button2, Status.PASS);
		} else {
			report.updateTestLog("Verification of Client ID Buttons", "VSA Access item popup does not contain the button " + button2, Status.FAIL);
		}
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(cancelbutton, "Cancel");
		} else {
			commonLibrary.clickButtonParentWithWait(cancelbutton, "Cancel");
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : ClickDocLink     
	// # Author : Veeshma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickGraph_verify() {

		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpCasesYearResoln, "Number of Cases per Year by Resolution Graph");
		WebElement largeGraph1 = commonLibrary.isExist(UIMAP_VSASearchResults.lrgGrpCasesYearResoln, 10);
		WebElement closeButton1 = commonLibrary.isExist(largeGraph1, UIMAP_VSASearchResults.closeButton, 10);
		if (largeGraph1 != null && largeGraph1.isDisplayed())
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is NOT expanded", Status.FAIL);

		commonLibrary.clickButtonLogSmallWait(closeButton1, "Close");

		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpCasesYear, "Number of Cases per Year Graph");
		WebElement largeGraph2 = commonLibrary.isExist(UIMAP_VSASearchResults.lrgGrpCasesYear, 10);
		WebElement closeButton2 = commonLibrary.isExist(largeGraph2, UIMAP_VSASearchResults.closeButton, 10);
		if (largeGraph2 != null && largeGraph2.isDisplayed())
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is NOT expanded", Status.FAIL);

		commonLibrary.clickButtonLogSmallWait(closeButton2, "Close");

		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpAwardResoln, "Award in US Dollars by Resolution Graph");
		WebElement largeGraph3 = commonLibrary.isExist(UIMAP_VSASearchResults.lrgGrpAwardResoln, 10);
		WebElement closeButton3 = commonLibrary.isExist(largeGraph3, UIMAP_VSASearchResults.closeButton, 10);
		if (largeGraph3 != null && largeGraph3.isDisplayed())
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is NOT expanded", Status.FAIL);

		commonLibrary.clickButtonLogSmallWait(closeButton3, "Close");

		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpPerCasesResoln, "Percentage of Cases by Resolution Graph");
		WebElement largeGraph4 = commonLibrary.isExist(UIMAP_VSASearchResults.ltgGrpPerCasesResoln, 10);
		WebElement closeButton4 = commonLibrary.isExist(largeGraph4, UIMAP_VSASearchResults.closeButton, 10);
		if (largeGraph4 != null && largeGraph4.isDisplayed())
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is NOT expanded", Status.FAIL);

		commonLibrary.clickButtonLogSmallWait(closeButton4, "Close");

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : clickGraphAndVerifytheResultListCount
	// # Author : Kirthika
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickGraphAndVerifytheResultListCount(int expectedValue) {
		String text = "";
		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpPerCasesResoln, "Percentage of Cases by Resolution Graph");

		WebElement largeGraph4 = commonLibrary.isExist(UIMAP_VSASearchResults.ltgGrpPerCasesResoln, 10);
		// WebElement closeButton4 = commonLibrary.isExist(largeGraph4,
		// UIMAP_VSASearchResults.closeButton, 10);

		if (largeGraph4 != null && largeGraph4.isDisplayed())
			report.updateTestLog("Verify if the Graph 'Percentage of Cases by Resolution' is expanded", "The Graph 'Percentage of Cases by Resolution is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph 'Percentage of Cases by Resolution' is expanded", "The Graph 'Percentage of Cases by Resolution' is NOT expanded", Status.FAIL);

		commonLibrary.sleep(10000);
		// //// WebElement mapview =
		// commonLibrary.isExist(UIMAP_VSASearchResults.mapview, 10);
		// ////
		// //// List<WebElement> areas =
		// commonLibrary.isExistList(mapview,UIMAP_VSASearchResults.areas, 10);
		// //// for(WebElement area : areas)
		// //// {
		// //// if(area.getAttribute("tooltip").contains("DEFENDANT VERDICT"))
		// //// {
		// //// text=area.getAttribute("tooltip").toString();
		// //// break;
		// //// }
		// ////
		// //// }
		// //// String texts[] = text.split(",");
		// //// int actualValue=Integer.parseInt(texts[2].trim());
		// //
		// // if(actualValue<expectedValue)
		// {
		// report.updateTestLog("Verify that Current result list count is less than Previous count in the map",
		// "Current result list count '"+actualValue+"' is less than Previous resultList count '"+expectedValue+"'",
		// Status.PASS);
		// }
		// else
		// {
		// report.updateTestLog("Verify that Current result list count is less than Previous count in the map",
		// "Current result list count '"+actualValue+"' is not less than Previous resultList count '"+expectedValue+"'",
		// Status.FAIL);
		// }

		// commonLibrary.clickButtonLogSmallWait(closeButton4, "Close");

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify term navigation in VSA
	// # Function Name : verifyTermNavigation
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Document verifyTermNavigation(String termvalue) {
		// commonLibrary.windowFocus();

		generalFunctions.clickJumpTo();

		WebElement navigateto = commonLibrary.isExist(UIMAP_Document.navigateto);
		if (navigateto != null && !navigateto.isDisplayed())
			generalFunctions.clickJumpTo();
		else {

			if (navigateto.getText().toLowerCase().contains(termvalue.toLowerCase())) {
				report.updateTestLog("To Verify Default Value in Navigate Section", "Navigate Section contains " + termvalue, Status.PASS);
			} else {
				report.updateTestLog("To Verify Default Value in Navigate Section", "Navigate Section does not contain " + termvalue, Status.FAIL);
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Get it now buttom from VSA
	// searchresults page
	// # Function Name : clickVSAGetItNowButton     
	// # Author : Divakar
	// # Date Created : 11 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickVSAGetItNowButton() {

		WebElement getItNowContainer = commonLibrary.isExist(UIMAP_VSASearchResults.getItNowContainer, 10);
		WebElement getItNowbutton = commonLibrary.isExist(getItNowContainer, UIMAP_VSASearchResults.getItNowButton, 10);

		commonLibrary.clickButtonLogSmallWait(getItNowbutton, "Get It Now");

		/*
		 * if (browsername.contains("internet"))
		 * commonLibrary.clickButton_Parent_WithWait_JS
		 * (getItNowbutton,"Get It Now!"); else
		 * commonLibrary.clickButton_Parent_WithWait(getItNowbutton,
		 * "Get It Now!");
		 */
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down
	// # Function Name : selectSortBy
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults selectSortBy(String value) {
		try {
			boolean flag = false;
			WebElement sortByList = null, sortContainer = null, sortBy = null;
			int i = 0;

			do {

				sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
				sortBy = commonLibrary.isExist(sortContainer, UIMAP_SearchResult.sortByBtn, 10);

				if (sortBy.getAttribute("class").contains("collapsed"))
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(sortBy, "Sort By");
					else
						commonLibrary.clickButtonParentWithWait(sortBy, "Sort By");

				sortByList = commonLibrary.isExist(UIMAP_SearchResult.sortByList, 10);
				i++;
			} while (sortByList == null && i < 3);

			if (sortByList != null) {
				List<WebElement> sortValues = commonLibrary.isExistList(sortByList, By.tagName("li"), 20);
				for (WebElement item : sortValues) {
					if (item.getText().contains(value)) {
						WebElement button = commonLibrary.isExistNegative(item, UIMAP_SearchResult.btnButton, 10);
						if (browsername.contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(button, value);
						else {
							commonLibrary.clickButtonParentWithWait(button, value);
							// item.click();
							commonLibrary.sleep(3000);
						}
						report.updateTestLog("Click on " + value + " in Sort By drop down", value + " is selected from Sort By drop down", Status.PASS);
						commonLibrary.sleep(7000);
						flag = true;
						break;
					}
				}
				if (flag) {
					sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
					sortBy = commonLibrary.isExist(sortContainer, UIMAP_SearchResult.sortByBtn, 10);
					if (sortBy.getText().contains(value))
						report.updateTestLog(value + " is the active item in the Sort by dropdown", value + " is the active item in the Sort by dropdown", Status.PASS);
					else
						report.updateTestLog(value + " is the active item in the Sort by dropdown", sortBy.getText() + " is the active item in the Sort by dropdown", Status.FAIL);
				}
			} else
				report.updateTestLog("Click on " + value + " in Sort By drop down", "Sort By drop down is not expanded", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// ascending order
	// # Function Name : verifyDocTitleSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyDocTitleSortedResultAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					int index1 = 0;
					int index2 = 0;
					WebElement docTitle = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
					WebElement docTitle1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.TitleClassDoc, 20);
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
					} else if (Character.isDigit(ch1) && (!Character.isDigit(ch2))) {
						posflag++;
					} else if (Character.isLetter(ch1) && (Character.isLetter(ch2))) {
						title = title.replace("(", "");
						title = title.replace(")", "");
						title = title.toLowerCase();
						title1 = title1.replace("(", "");
						title1 = title1.replace(")", "");
						title1 = title1.toLowerCase();
						if ((title.replace(" ", "").compareTo(title1.replace(" ", "")) < 0))
							posflag++;
						else
							negFlag++;
					} else if (((!Character.isDigit(ch1)) && (!Character.isLetter(ch1))) && ((Character.isLetter(ch2)) || (Character.isDigit(ch2))))
						posflag++;
					else if (((!Character.isDigit(ch1)) && (!Character.isLetter(ch1))) && ((!Character.isDigit(ch1)) && (!Character.isLetter(ch1))))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in ascending order of Document Title", "The results are sorted in ascending order of Document Title", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in ascending order of Document Title", "The results are not sorted in ascending order of Document Title", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// descending order
	// # Function Name : verifyDocTitleSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyDocTitleSortedResultZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					int index1 = 0;
					int index2 = 0;
					WebElement docTitle = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
					WebElement docTitle1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.TitleClassDoc, 20);
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
					} else if (Character.isLetter(ch1) && (!Character.isDigit(ch2))) {
						posflag++;
					} else if (Character.isLetter(ch1) && (Character.isLetter(ch2))) {
						if ((title.toLowerCase().replace(" ", "").compareTo(title1.toLowerCase().replace(" ", "")) > 0))
							posflag++;
						else
							negFlag++;
					} else if ((Character.isDigit(ch1) || Character.isLetter(ch1)) && ((!(Character.isLetter(ch2))) && (!(Character.isDigit(ch2)))))
						posflag++;
					else if (((!Character.isDigit(ch1)) && (!Character.isLetter(ch1))) && ((!Character.isDigit(ch1)) && (!Character.isLetter(ch1))))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in descending order of Document Title", "The results are sorted in descending order of Document Title", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in descending order of Document Title", "The results are not sorted in descending order of Document Title", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// ascending order
	// # Function Name : verifyJurisSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyJurisSortedResultAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("U.S. Federal"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) < 0))
						posflag++;
					else if (dataDefn1.equalsIgnoreCase("Non-jurisdictional"))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in ascending order of Jurisdiction with U.S.Federal at the top", "The results are sorted in ascending order of Jurisdiction U.S.Federal at the top", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in ascending order of Jurisdiction with U.S.Federal at the top", "The results are not sorted in ascending order of Jurisdiction with U.S.Federal at the top", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// descending order
	// # Function Name : verifyJurisSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyJurisSortedResultZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("Non-jurisdictional"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else if (dataDefn1.equalsIgnoreCase("U.S. Federal"))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in descending order of Jurisdiction with U.S.Federal at the bottom", "The results are sorted in descending order of Jurisdiction U.S.Federal at the bottom", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in descending order of Jurisdiction with U.S.Federal at the bottom", "The results are not sorted in descending order of Jurisdiction with U.S.Federal at the bottom", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting lowest to
	// highest
	// # Function Name : verifySortCourtLowTohigh
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortCourtLowTohigh() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in descending order of Jurisdictional Courts", "The results are sorted in descending order of Jurisdictional Courts", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in descending order of Jurisdictional Courts", "The results are not sorted in descending order of Court with Tribal Jurisdiction courts at the top", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sort Court Highest to Lowest
	// in VSA page
	// # Function Name : verifySortCourtHighToLowVSA
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortCourtHighToLowVSA() {
		try {

			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					String usFederal[] = new String[docList.size() + 1];
					int s = 0;
					int posFlag = 0;
					int negFlag = 0;
					for (int i = 0; i < docList.size() - 1; i++) {
						WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
						if (docContent == null)
							docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
						WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
						List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
						String dataDefn = null;
						String dataDefn1 = null;
						for (int t = 0; t < dataTerms.size(); t++) {
							if (dataTerms.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
								dataDefn = dataDefns.get(t).getText();
								break;
							}
						}

						WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
						if (docContent1 == null)
							docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
						WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
						List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
						for (int t = 0; t < dataTerms1.size(); t++) {
							if (dataTerms1.get(t).getText().equalsIgnoreCase("Jurisdiction")) {
								dataDefn1 = dataDefns1.get(t).getText();
								break;
							}
						}

						if ((dataDefn.equalsIgnoreCase(dataDefn1)) && (dataDefn.equalsIgnoreCase("U.S. Federal"))) {
							for (int t = 0; t < dataTerms.size(); t++) {
								if (dataTerms.get(t).getText().equalsIgnoreCase("Court")) {
									usFederal[s] = dataDefns.get(t).getText();
									usFederal[s + 1] = dataDefns1.get(t).getText();
									s++;
									break;
								}
							}
						} else if ((!(dataDefn.equalsIgnoreCase(dataDefn1))) && (dataDefn.equalsIgnoreCase("U.S. Federal")))
							posFlag++;
						else if (dataDefn.equalsIgnoreCase(dataDefn1))
							posFlag++;
						else if ((!(dataDefn.equalsIgnoreCase(dataDefn1))) && ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) < 0)))
							posFlag++;
						else
							negFlag++;

					}
					int d = 0;
					if (usFederal != null) {

						for (d = 0; usFederal[d + 1] != null; d++) {
							if ((usFederal[0].replace(" ", "").compareTo(usFederal[1].replace(" ", "")) < 0))
								posFlag++;
							else
								negFlag++;
						}
					}
					if (posFlag == (docList.size() - 1) && (negFlag == 0)) {
						report.updateTestLog("The results are sorted in ascending order of Court with U.S.Federal Jurisdiction courts at the top", "The results are sorted in ascending order of Court with U.S.Federal Jurisdiction courts at the top", Status.PASS);
					} else
						report.updateTestLog("The results are sorted in ascending order of Court with U.S.Federal Jurisdiction courts at the top", "The results are not sorted in ascending order of Court with U.S.Federal Jurisdiction courts at the top", Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sort Year Newest to
	// Oldest
	// # Function Name : verifySortYearNewtoOld
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortYearNewtoOld() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					int year = 0;
					int year1 = 0;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Year")) {
							dataDefn = dataDefns.get(t).getText();
							year = Integer.parseInt(dataDefn);
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Year")) {
							dataDefn1 = dataDefns1.get(t).getText();
							year1 = Integer.parseInt(dataDefn1);
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (year >= year1)
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Year Newest to Oldeat order", "The results are sorted in Year Newest to Oldest order", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Year Newest to Oldeat order", "The results are not sorted in Year Newest to Oldest order", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Year Oldest to Newest
	// # Function Name : verifySortYearOldtoNew
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortYearOldtoNew() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					int year = 0;
					int year1 = 0;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Year")) {
							dataDefn = dataDefns.get(t).getText();
							year = Integer.parseInt(dataDefn);
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Year")) {
							dataDefn1 = dataDefns1.get(t).getText();
							year1 = Integer.parseInt(dataDefn1);
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (year <= year1)
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Year Newest to Oldeat order", "The results are sorted in Year Newest to Oldest order", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Year Newest to Oldeat order", "The results are not sorted in Year Newest to Oldest order", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Award sort Highest to Lowest
	// # Function Name : verifySortAwardHightoLow
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortAwardHightoLow() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					long award = 0;
					long award1 = 0;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Award")) {
							dataDefn = dataDefns.get(t).getText();
							award = Long.parseLong(dataDefn);
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Award")) {
							dataDefn1 = dataDefns1.get(t).getText();
							award1 = Long.parseLong(dataDefn1);
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (award >= award1)
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Award Highest to Oldest", "The results are sorted in Award Highest to Oldest", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Award Highest to Oldest", "The results are not sorted in Award Highest to Oldest", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Award sort Lowest to Highest
	// # Function Name : verifySortAwardLowtoHigh
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortAwardLowtoHigh() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					long award = 0;
					long award1 = 0;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Award")) {
							dataDefn = dataDefns.get(t).getText();
							award = Long.parseLong(dataDefn);
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Award")) {
							dataDefn1 = dataDefns1.get(t).getText();
							award1 = Long.parseLong(dataDefn1);
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if (award <= award1)
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Award Lowest to Highest", "The results are sorted in Award Lowest to Highest", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Award Lowest to Highest", "The results are not sorted in Award Lowest to Highest", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Resolution A-Z
	// # Function Name : verifySortResolutionAZ
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortResolutionAZ() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Resolution")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Resolution")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) < 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Resolution A-Z order", "The results are sorted in Resolution A-Z order", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Resolution A-Z order", "The results are not sorted in Resolution A-Z order", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Resolution Z-A
	// # Function Name : verifySortResolutionZA
	// # Author : Shobana
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifySortResolutionZA() {
		int posflag = 0;
		int negFlag = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					if (docContent == null)
						docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent1, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Resolution")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					if (docContent1 == null)
						docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent1, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Resolution")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					if (dataDefn.equalsIgnoreCase("unknown"))
						posflag++;
					else if (dataDefn.equalsIgnoreCase(dataDefn1))
						posflag++;
					else if ((dataDefn.replace(" ", "").compareTo(dataDefn1.replace(" ", "")) > 0))
						posflag++;
					else
						negFlag++;
				}
				if (posflag == (docList.size() - 1) && (negFlag == 0)) {
					report.updateTestLog("The results are sorted in Resolution Z-A order", "The results are sorted in Resolution Z-A order", Status.PASS);
				} else
					report.updateTestLog("The results are sorted in Resolution Z-A order", "The results are not sorted in Resolution Z-A order", Status.FAIL);
			}
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Resolution Z-A
	// # Function Name : verifySortResolutionZA
	// # Author : Seetha
	// # Date Created : 15 Apr'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyResultPage(String SearchTerm) {
		try {
			WebElement hdrResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult1, 10);
			if (hdrResult1.getText().equalsIgnoreCase(SearchTerm)) {
				report.updateTestLog("Verify results page is displayed for the search term " + SearchTerm + "", "Result page is displayed for the search term " + SearchTerm + "", Status.PASS);

			} else {
				report.updateTestLog("Verify results page is displayed for the search term " + SearchTerm + "", "Result page is not displayed for the search term " + SearchTerm + "", Status.FAIL);
			}

		} catch (Exception e) {

		}

		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to the footer links
	// # Function Name : NavigateToHistoryFooterLink
	// # Author : Seetha
	// # Date Created : 15 Apr'15
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
			WebElement lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
			if (lnkTxtViewAllHistory == null) {
				commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
				lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
			}

			if (strLinkName.equalsIgnoreCase("View All History")) {
				// lnkTxtViewAllHistory =
				// commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
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
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to counselbenchmarking
	// # Function Name : navigateTocounselbenchmarking
	// # Author : Uma
	// # Date Created : 25 Mar'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking navigateTocounselbenchmarking() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionCunselBenchmarking, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Counsel Benchmarking");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Counsel Benchmarking");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("counsel benchmarking")) {
			report.updateTestLog("Counsel Benchmarking page is displayed", "Counsel Benchmarking page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Counsel Benchmarking page is displayed", "Counsel Benchmarking page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionCunselBenchmarking, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance® counsel benchmarking logo displays in the Top left corner of Global menu", "Lexis Advance® Counsel benchmarking logo displays in the Top left corner of Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® counsel benchmarking logo displays in the Top left corner of Global menu", "Lexis Advance® Counsel benchmarking logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		}
		if (browsername.contains("internet")) {
			commonLibrary.clickJS(logo, "Lexis Advance® Counsel Benchmarking");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		} else {
			commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Counsel Benchmarking");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of post filters
	// # Function Name : verifyVSApostfilters
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyVSApostfilters() {
		String option = dataTable.getData("General_Data", "VSAPostFilter");
		String[] optionList = option.split(";");
		Boolean blnflag = false;

		// WebElement appliedfilter =
		// commonLibrary.isExist(UIMAP_VSASearchResults.filtersapplied, 20);
		// WebElement pageheader =
		// commonLibrary.isExist(UIMAP_VSASearchResults.pageheader, 20);

		// String filter1 = appliedfilter.getText();

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_VSASearchResults.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_VSASearchResults.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			for (int j = 0; j < 7; j++) {
				if (optionList[j].contains(button.getText())) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList[j] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter is not displayed in the application", Status.FAIL);
			}

		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of post filters
	// # Function Name : verifyVSApostfilters
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public VSASearchResults applycourtpostfilter(String filtervalue) {
		WebElement unusedfilters = commonLibrary.isExist(UIMAP_VSASearchResults.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(1), UIMAP_VSASearchResults.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			if (filtervalue.contains(button.getText())) {
				WebElement parentcourt = commonLibrary.isExist(UIMAP_VSASearchResults.parentcourt, 20);
				List<WebElement> lilist1 = commonLibrary.isExistList(parentcourt, UIMAP_VSASearchResults.lilist, 20);
				WebElement uldataid = commonLibrary.isExist(lilist1.get(0), UIMAP_VSASearchResults.uldataid, 20);
				List<WebElement> lilist2 = commonLibrary.isExistList(uldataid, UIMAP_VSASearchResults.lilist2, 20);
				WebElement selectmultiplebutton = commonLibrary.isExist(lilist2.get(0), UIMAP_VSASearchResults.selectmultiplebutton, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(selectmultiplebutton, "Select Multiple");
				} else {
					commonLibrary.clickButtonParentWithWait(selectmultiplebutton, "Select Multiple");
				}
				WebElement filterdialogbox = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div = commonLibrary.isExist(filterdialogbox, UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist1 = commonLibrary.isExistList(div, UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> ullist2 = commonLibrary.isExistList(ullist1.get(0), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> lilist3 = commonLibrary.isExistList(ullist2.get(0), UIMAP_VSASearchResults.lilist, 20);
				commonLibrary.isExist(lilist3.get(0), UIMAP_VSASearchResults.label, 20);

			}
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply cases resolution post filter
	// # Function Name : applyCaseResolutionpostfilter
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public VSASearchResults applyCaseResolutionpostfilter(String filtervalue, String Subfilter1, String Subfilter2, String Subfilter3) {
		WebElement unusedfilters = commonLibrary.isExist(UIMAP_VSASearchResults.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_VSASearchResults.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			if (filtervalue.contains(button.getText())) {
				WebElement expandedcheck = commonLibrary.isExist(UIMAP_VSASearchResults.expandedcheck, 20);
				if (expandedcheck == null) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(button, filtervalue);
					} else {
						commonLibrary.clickButtonParentWithWait(button, filtervalue);
					}
				}
				WebElement parentcaseresolution = commonLibrary.isExist(UIMAP_VSASearchResults.parentcaseresolution, 20);
				List<WebElement> lilist2 = commonLibrary.isExistList(parentcaseresolution, UIMAP_VSASearchResults.lilist2, 20);
				WebElement selectmultiplebutton = commonLibrary.isExist(lilist2.get(0), UIMAP_VSASearchResults.selectmultiplebutton, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(selectmultiplebutton, "Select Multiple");
				} else {
					commonLibrary.clickButtonParentWithWait(selectmultiplebutton, "Select Multiple");
				}
				WebElement filterdialogbox = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div = commonLibrary.isExist(filterdialogbox, UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist1 = commonLibrary.isExistList(div, UIMAP_VSASearchResults.ulnotcourt, 20);
				List<WebElement> lilist3 = commonLibrary.isExistList(ullist1.get(0), UIMAP_VSASearchResults.lilist, 20);
				List<WebElement> ullist2 = commonLibrary.isExistList(lilist3.get(0), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> lilist4 = commonLibrary.isExistList(ullist2.get(0), UIMAP_VSASearchResults.lilist, 20);
				WebElement label = commonLibrary.isExist(lilist4.get(0), UIMAP_VSASearchResults.label, 20);
				WebElement spanname = commonLibrary.isExist(label, UIMAP_VSASearchResults.spanname, 20);
				if (spanname.getText().contains(Subfilter1)) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(spanname, Subfilter1);
					} else {
						commonLibrary.clickButtonParentWithWait(spanname, Subfilter1);
					}
				}
				WebElement filterdialogbox1 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div1 = commonLibrary.isExist(filterdialogbox1, UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist11 = commonLibrary.isExistList(div1, UIMAP_VSASearchResults.ulnotcourt, 20);
				List<WebElement> lilist13 = commonLibrary.isExistList(ullist11.get(0), UIMAP_VSASearchResults.lilist, 20);
				List<WebElement> ullist12 = commonLibrary.isExistList(lilist13.get(0), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> lilist14 = commonLibrary.isExistList(ullist12.get(0), UIMAP_VSASearchResults.lilist, 20);
				WebElement label1 = commonLibrary.isExist(lilist14.get(1), UIMAP_VSASearchResults.label, 20);
				WebElement spanname1 = commonLibrary.isExist(label1, UIMAP_VSASearchResults.spanname, 20);
				if (spanname1.getText().contains(Subfilter2)) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(spanname1, Subfilter2);
					} else {
						commonLibrary.clickButtonParentWithWait(spanname1, Subfilter2);
					}
				}

				WebElement filterdialogbox2 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div2 = commonLibrary.isExist(filterdialogbox2, UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist21 = commonLibrary.isExistList(div2, UIMAP_VSASearchResults.ulnotcourt, 20);
				List<WebElement> lilist23 = commonLibrary.isExistList(ullist21.get(0), UIMAP_VSASearchResults.lilist, 20);
				List<WebElement> ullist22 = commonLibrary.isExistList(lilist23.get(0), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> lilist24 = commonLibrary.isExistList(ullist22.get(0), UIMAP_VSASearchResults.lilist, 20);
				WebElement label2 = commonLibrary.isExist(lilist24.get(2), UIMAP_VSASearchResults.label, 20);
				WebElement spanname2 = commonLibrary.isExist(label2, UIMAP_VSASearchResults.spanname, 20);
				if (spanname2.getText().contains(Subfilter3)) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(spanname2, Subfilter3);
					} else {
						commonLibrary.clickButtonParentWithWait(spanname2, Subfilter3);
					}
				}
				// Select OK Button in Popup
				WebElement filterdialogbox3 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement footer = commonLibrary.isExist(filterdialogbox3, UIMAP_VSASearchResults.footer, 20);
				WebElement ulclassaction = commonLibrary.isExist(footer, UIMAP_VSASearchResults.ulclassaction, 20);
				List<WebElement> lilist34 = commonLibrary.isExistList(ulclassaction, UIMAP_VSASearchResults.lilist, 20);
				WebElement confirmbutton = commonLibrary.isExist(lilist34.get(1), UIMAP_VSASearchResults.submitbutton, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(confirmbutton, "OK");
					break;
				} else {
					commonLibrary.clickButtonParentWithWait(confirmbutton, "OK");
					break;
				}
			}
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply post filter
	// # Function Name : applyinjuriespostfilter
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public VSASearchResults applyinjuriespostfilter(String filtervalue, String subfilter) {
		WebElement unusedfilters = commonLibrary.isExist(UIMAP_VSASearchResults.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_VSASearchResults.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			if (filtervalue.contains(button.getText())) {
				WebElement expandedcheck = commonLibrary.isExist(UIMAP_VSASearchResults.expandedcheck, 20);
				if (expandedcheck == null) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(button, filtervalue);
					} else {
						commonLibrary.clickButtonParentWithWait(button, filtervalue);
					}
				}
				WebElement parentinjury = commonLibrary.isExist(UIMAP_VSASearchResults.parentinjury, 20);
				List<WebElement> lilist2 = commonLibrary.isExistList(parentinjury, UIMAP_VSASearchResults.lilist, 20);
				WebElement label2 = commonLibrary.isExist(lilist2.get(0), UIMAP_VSASearchResults.label, 20);
				WebElement spanname2 = commonLibrary.isExist(label2, UIMAP_VSASearchResults.spanname, 20);
				if (spanname2.getText().contains(subfilter)) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(spanname2, "Select Multiple");
					} else {
						commonLibrary.clickButtonParentWithWait(spanname2, "Select Multiple");
					}
				}

				// Select OK Button in Popup
				WebElement filterdialogbox3 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement footer = commonLibrary.isExist(filterdialogbox3, UIMAP_VSASearchResults.footer, 20);
				WebElement ulclassaction = commonLibrary.isExist(footer, UIMAP_VSASearchResults.ulclassaction, 20);
				List<WebElement> lilist34 = commonLibrary.isExistList(ulclassaction, UIMAP_VSASearchResults.lilist, 20);
				WebElement confirmbutton = commonLibrary.isExist(lilist34.get(0), UIMAP_VSASearchResults.submitbutton, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(confirmbutton, "OK");
				} else {
					commonLibrary.clickButtonParentWithWait(confirmbutton, "OK");
				}
			}
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to to verify the hover text
	// # Function Name : verifyhovertext
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to apply cases resolution post filter
	// # Function Name : applyCaseResolutionpostfilter
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyhovertext(String hovertext) {
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : NavigateToHistoryLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public VSASearchResults navigateToHistoryLink(String strDocTitle) {
		Boolean blnFlag = false;

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentDocuments, 20);
			if (btnIdRecentDocuments == null) {
				commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			}
			btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentDocuments, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Documents");

			WebElement lstrecentDocumentsContent = commonLibrary.isExist(UIMAP_Home.lstrecentDocumentsContent, 20);
			if (lstrecentDocumentsContent != null) {
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstrecentDocumentsContent, UIMAP_Home.lstrecentdocs, 20);
				// if (lnkDocuments.size() > 0)
				// lnkDocuments.get(0).click();
				for (WebElement link : lnkDocuments) {
					WebElement link2 = commonLibrary.isExist(link, UIMAP_Document.links, 10);
					if (link2.getAttribute("title").contains(strDocTitle)) {
						blnFlag = true;
						break;

					}
				}
				if (blnFlag)
					report.updateTestLog("Verify Document" + strDocTitle + " will appear in Recemt Dpcoments", "'" + strDocTitle + "' is Displayed in History Menu Documents tab", Status.PASS);
				else
					report.updateTestLog("Verify Document" + strDocTitle + " will appear in Recemt Dpcoments", "'" + strDocTitle + "' is not Displayed in History Menu Documents tab", Status.FAIL);
				commonLibrary.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Select Value
	// # Function Name : clickActionSelectValue    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public VSASearchResults clickActionSelectValue(String strAction) {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_SearchResult.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_SearchResult.divAction, 20);
		if (divAction != null) {

			if (commonLibrary.selectFromListButton(divAction, strAction)) {

				pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
				commonLibrary.sleep(5000);
				// report.updateTestLog("Click option " + strAction + "", "" +
				// strAction + " is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public VSASearchResults saveLink(String tcName, String dataSheet, String colName) {
		generalFunctions.saveLink(tcName, dataSheet, colName);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public VSASearchResults closePopup() {
		WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.dialogContent, 10);
		WebElement close = commonLibrary.isExistNegative(dialogContent, UIMAP_CounselBenchmarking.close, 10);
		commonLibrary.clickButtonParentWithWait(close, "Close");
		pageCheck.ajaxWait(driver);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application and to
	// delete cookies
	// # Function Name : logoutdeleteAllCookies     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public SignIn logoutdeleteAllCookies() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");
			}
			lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		}
		if (lnkSignOut != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);

		}
		driver.manage().deleteAllCookies();

		report.updateTestLog("Clear the browser cache", "Browser cache is cleared", Status.DONE);
		// driver.quit();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Brand Name in Sign In page
	// # Function Name : verifyBrandName     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyBrandName(String brandName) {
		WebElement logo = commonLibrary.isExist(UIMAP_SignIn.brandName, 10);
		WebElement logoHeader = commonLibrary.isExist(logo, By.tagName("header"), 10);
		if (logoHeader != null) {
			if (logoHeader.getText().toLowerCase().contains(brandName.toLowerCase()))
				report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is displayed in sign in page", Status.PASS);
			else
				report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is not displayed in sign in page", Status.FAIL);
		} else
			report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is not displayed in sign in page", Status.FAIL);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Warning screen is displayed
	// # Function Name : verifyWarningScreen    
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyWarningScreen() {
		WebElement warning = commonLibrary.isExist(UIMAP_VSASearchResults.warning, 10);
		WebElement warningHeader = commonLibrary.isExist(warning, UIMAP_VSASearchResults.warningHeader, 10);
		if (warningHeader != null) {
			if (warningHeader.getText().toLowerCase().contains("before you continue"))
				report.updateTestLog("Verify warning screen is displayed", "Warning screen is displayed", Status.PASS);
			else
				report.updateTestLog("Verify warning screen is displayed", "Warning screen is not displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify warning screen is displayed", "Warning screen is not displayed", Status.FAIL);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click GetItNow under document title
	// # Function Name : clickGetItNow
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickGetItNow() {
		// WebElement getItNow =
		// commonLibrary.isExist(UIMAP_SearchResult.getItNow, 10);
		// if (getItNow == null)
		WebElement getItNow = commonLibrary.isExist(UIMAP_VSASearchResults.vsagetitnow, 10);
		if (getItNow != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(getItNow, "GetItNow");
			else
				commonLibrary.clickButtonParentWithWait(getItNow, "GetItNow");
		} else
			report.updateTestLog("Click ButtonGetItNow", "GetItNow button is not available", Status.FAIL);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify VSA Search Result page is
	// displayed
	// # Function Name : verifyVSASearchResultPage
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public VSASearchResults verifyVSASearchResultPage() {

		if (driver.getCurrentUrl().toLowerCase().contains(UIMAP_VSASearchResults.vsaSearchResult)) {
			report.updateTestLog("Verify VSA Search Result page is displayed", "VSA Search Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify VSA Search Result page is displayed", "VSA Search Result page is not displayed", Status.FAIL);
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify VSA Search Result value is
	// displayed
	// # Function Name : verifyVSASearchResultValue
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VSASearchResults verifyVSASearchResultValue() {

		int convertedValue;

		WebElement SearchResultsHeader = commonLibrary.isExist(UIMAP_VSASearchResults.searchHeader, 10);
		if (SearchResultsHeader != null) {

			WebElement SearchResultsHeaderValue = commonLibrary.isExist(SearchResultsHeader, UIMAP_VSASearchResults.searchHeaderValue, 10);
			if (SearchResultsHeaderValue != null) {

				String Value = SearchResultsHeaderValue.getText();
				Value = Value.substring(Value.indexOf('(') + 1, Value.indexOf(')'));
				if (Value != null) {

					convertedValue = Integer.parseInt(Value);

					if (convertedValue > 0) {
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is displayed as " + convertedValue, Status.PASS);
					} else
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is not displayed", Status.FAIL);

				}
			}
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public VSASearchResults selectPostFilter(String header, String filter) {

		// Common function call for selecting post filter
		generalFunctions.selectPostFilter(header, filter);

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Enter timeline and search
	// Results page.
	// # Function Name : selectPostFilterTimeline
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public VSASearchResults selectPostFilterTimeline(String startYear, String endYear) {

		if (!(startYear.equals(" ") && endYear.equals(" "))) {

			int i = 0;
			boolean flag = false;
			commonLibrary.sleep(1000);
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_VSASearchResults.filterContainer, 10);
			if (filterContainer != null) {
				List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_VSASearchResults.filterHeader, 10);
				if (filterHeader != null) {

					for (i = 0; i < filterHeader.size(); i++) {
						if (filterHeader.get(i).getText().contains("Timeline"))

						{
							if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {

								if (browsername.toLowerCase().contains("internet"))
									commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), "Timeline");
								else
									commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), "Timeline");
								report.updateTestLog("Expanding Filter Header: " + "Timeline", "Timeline" + " filter Header Expanded.", Status.DONE);
								flag = true;

								WebElement startYearTextbox = commonLibrary.isExist(UIMAP_VSASearchResults.startYear, 20);
								WebElement endYearTextbox = commonLibrary.isExist(UIMAP_VSASearchResults.endYear, 20);

								if (startYearTextbox != null && endYearTextbox != null) {

									commonLibrary.setDataInTextBox(startYearTextbox, startYear, "StartYear");
									commonLibrary.setDataInTextBox(endYearTextbox, endYear, "EndYear");
									WebElement timelinkOKbutton = commonLibrary.isExist(UIMAP_VSASearchResults.timeLineOkButton, 20);
									if (timelinkOKbutton != null) {

										if (browsername.toLowerCase().contains("internet")) {
											commonLibrary.clickButtonParentWithWaitJS(timelinkOKbutton, "OK");
											break;
										} else {
											commonLibrary.clickLinkWithWebElementWithWait(timelinkOKbutton, "OK");

											break;
										}
									}

									else {
										report.updateTestLog("Clicking the OK Button", "OK Button is not Clicked", Status.FAIL);
										System.out.println("Textbox not present");

									}

								}
							}
						}
					}
				}
				if (!flag) {
					report.updateTestLog("Selecting Filter TimeLine", "No Filter Selected.", Status.FAIL);

				}
			}

			else {

				report.updateTestLog("Selecting Filter TimeLine", "No Filter Selected.", Status.DONE);
			}
		}
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to awardAmoutFilterVerify Search
	// Results page.
	// # Function Name : awardAmoutFilterVerify
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public VSASearchResults awardAmoutFilterVerify(String amountLow, String amountHigh) {
		int i = 0, j = 1;
		Boolean flg = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

		List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
		for (i = 0; i < filterHeader.size(); i++) {
			if (filterHeader.get(i).getText().contains("Timeline"))
				j = 2;
			if (filterHeader.get(i).getText().toUpperCase().contains("Award Amount".toUpperCase())) {
				if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
					if (browsername.toLowerCase().contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), "Award Amount");
					else
						commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), "Award Amount");
					report.updateTestLog("Expanding Filter Header: " + "Award Amount", "Award Amount" + " filter Header Expanded.", Status.DONE);
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

				WebElement amtAwardDiv = commonLibrary.isExist(UIMAP_SearchResult.amtAwardDiv, 10);
				WebElement okButton = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.okButton, 10);
				WebElement amountLowInput = null;
				WebElement amountHighInput = null;
				if (amountLow.equals("") && amountHigh.equals("")) {
					amountLowInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountLowInput, 10);
					if (amountLowInput != null)
						commonLibrary.setDataInTextBox(amountLowInput, amountLow, "Award Amount Low");
					amountHighInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountHighInput, 10);
					if (amountHighInput != null)
						commonLibrary.setDataInTextBox(amountHighInput, amountHigh, "Award Amount High");

					okButton = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.okButton, 10);
					if (okButton != null && okButton.getAttribute("class").equals("disabled")) {
						report.updateTestLog("Verify OK button", "OK button appears disabled.", Status.PASS);
						flg = true;
					}
				} else if (amountLow.equals("") && amountHigh != null) {
					amountLowInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountLowInput, 10);
					if (amountLowInput != null)
						commonLibrary.setDataInTextBox(amountLowInput, amountLow, "Award Amount Low");
					amountHighInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountHighInput, 10);
					if (amountHighInput != null)
						commonLibrary.setDataInTextBox(amountHighInput, amountHigh, "Award Amount High");

					okButton = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.okButton, 10);
					if (okButton != null && okButton.getAttribute("class").equals("disabled")) {
						report.updateTestLog("Verify OK button", "OK button appears disabled.", Status.PASS);
						flg = true;
					}
				} else if (amountLow != null && amountHigh.equals("")) {
					amountLowInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountLowInput, 10);
					if (amountLowInput != null)
						commonLibrary.setDataInTextBox(amountLowInput, amountLow, "Award Amount Low");
					amountHighInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountHighInput, 10);
					if (amountHighInput != null)
						commonLibrary.setDataInTextBox(amountHighInput, amountHigh, "Award Amount High");

					okButton = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.okButton, 10);
					if (okButton != null && okButton.getAttribute("class").equals("disabled")) {
						report.updateTestLog("Verify OK button", "OK button appears disabled.", Status.PASS);
						flg = true;
					}
				} else if (amountLow != null && amountHigh != null) {
					amountLowInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountLowInput, 10);
					if (amountLowInput != null)
						commonLibrary.setDataInTextBox(amountLowInput, amountLow, "Award Amount Low");

					amountHighInput = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.amountHighInput, 10);
					if (amountHighInput != null)
						commonLibrary.setDataInTextBox(amountHighInput, amountHigh, "Award Amount High");

					okButton = commonLibrary.isExist(amtAwardDiv, UIMAP_SearchResult.okButton, 10);
					if (okButton != null && okButton.getAttribute("class").equals("disabled")) {
						report.updateTestLog("Verify OK button", "OK button appears disabled.", Status.PASS);
						flg = true;
					} else {
						commonLibrary.clickButtonParentWithWait(okButton, "OK");
						flg = true;
					}
				}
				if (flg)
					break;
			}
			if (flg)
				break;
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifyNarrowByFilter     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyNarrowByFilter(String strFilter) {
		Boolean flg = false;
		WebElement usedfilter = commonLibrary.isExist(UIMAP_VSASearchResults.usedfilter, 30);
		List<WebElement> filters = commonLibrary.isExistList(usedfilter, UIMAP_VSASearchResults.lilist, 10);
		for (int i = 0; i < filters.size(); i++) {
			WebElement button = commonLibrary.isExist(filters.get(i), UIMAP_VSASearchResults.btnButton, 20);
			if (button.getText().toLowerCase().contains(strFilter.toLowerCase())) {
				report.updateTestLog("Verify Filter " + strFilter + " is present in narrow by section.", "Filter " + strFilter + " is present in narrow by section.", Status.PASS);
				flg = true;
				break;
			}
			if (flg)
				break;
		}

		if (!flg)
			report.updateTestLog("Verify Filter " + strFilter + " is present in narrow by section.", "Filter " + strFilter + " is not present in narrow by section.", Status.FAIL);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter not used
	// # Function Name : verifyNarrowByFilternotused     
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyNarrowByFilterNotUsed() {
		WebElement usedfilter = commonLibrary.isExist(UIMAP_VSASearchResults.usedfilter, 30);
		if (usedfilter == null) {

			report.updateTestLog("Filters are not displayed in the VSA search result page", "Filters are not displayed in the VSA search result page", Status.PASS);

		} else {

			report.updateTestLog("Filters are not displayed in the VSA search result page", "Filters are getting displayed in the VSA search result page", Status.FAIL);

		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Verdicts and Settlements
	// Analyzer page
	// # Function Name : navigateToVSAPage
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements navigateToVSAPage() {
		generalFunctions.productSwitcher("Verdict & Settlement Analyzer");
		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function Click Filter button
	// # Function Name : FilterButtonClick
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VSASearchResults clickPreFilter() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterButton, 20);
		if (btnClassFilter != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		} else {

			report.updateTestLog("Click On filter Button", "filter Button is not Clicked", Status.FAIL);

		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function Click Filter button
	// # Function Name : clearFilter
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VSASearchResults clearFilter() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VSASearchResults.clearfilter, 20);
		if (btnClassFilter != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Clear Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Clear Filter");
			}
		} else {

			report.updateTestLog("Click On filter Button", "filter Button is not Clicked", Status.FAIL);

		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : ClickDocLink and save it in excel    
	// # Author : Kirthika
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkSaveInExcel(String excelData) {
		Boolean blnFlag = false;

		WebElement resultClass = null;
		String strDocTitle = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_VSASearchResults.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_VSASearchResults.TitleClassDoc, 2);
					if (eleDocTitle != null) {

						strDocTitle = eleDocTitle.getText();

						// To write the docment title to excel

						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
						if (lnkDocument != null) {
							if (browsername.contains("internet")) {
								commonLibrary.clickLinkWithWebElementWithWaitJS(lnkDocument, lnkDocument.getText());
							} else {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
							}

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

			WebElement docTitle = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);

			int counter = 0;
			do {
				counter = counter + 1;
				docTitle = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 5);
				commonLibrary.sleep(5000);
			} while (docTitle == null && counter <= 40);

			WebElement pgHeader = null;
			commonLibrary.sleep(50000);
			if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(UIMAP_VSASearchResults.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_VSASearchResults.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document"))) {
				if (excelData != "") {
					String sheetNam = excelData.split(";")[0];
					int row = Integer.parseInt((excelData.split(";")[1]));
					String column = excelData.split(";")[2];
					String datatablePath = excelData.split(";")[3];

					String docTit = pgHeader.getText();
					excel = new ExcelDataAccess(datatablePath, sheetNam);
					excel.setDatasheetName("General_Data");
					excel.setValue(row, column, docTit);
				}
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			}

			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDown
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public VSASearchResults verifypreFilterDropDownDisplayed() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrow, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify VSA Search Result value is
	// displayed
	// # Function Name : verifyVSASearchResultValue
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public int verifyVSASearchResultValue1() {

		int convertedValue = 0;

		WebElement SearchResultsHeader = commonLibrary.isExist(UIMAP_VSASearchResults.searchHeader, 10);
		if (SearchResultsHeader != null) {

			WebElement SearchResultsHeaderValue = commonLibrary.isExist(SearchResultsHeader, UIMAP_VSASearchResults.searchHeaderValue, 10);
			if (SearchResultsHeaderValue != null) {

				String Value = SearchResultsHeaderValue.getText();
				Value = Value.substring(Value.indexOf('(') + 1, Value.indexOf(')'));
				if (Value != null) {

					convertedValue = Integer.parseInt(Value);

					if (convertedValue > 0) {
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is displayed as " + convertedValue, Status.PASS);
					} else
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is not displayed", Status.FAIL);

				}
			}
		}
		return convertedValue;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify graph
	// # Function Name : verifyGraph  
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public VSASearchResults verifyGraph() {

		WebElement CasesYearResoln = commonLibrary.isExist(UIMAP_VSASearchResults.grpCasesYearResoln, 10);
		WebElement CasesYear = commonLibrary.isExist(UIMAP_VSASearchResults.grpCasesYear, 10);
		WebElement AwardResoln = commonLibrary.isExist(UIMAP_VSASearchResults.grpAwardResoln, 10);
		WebElement PerCasesResoln = commonLibrary.isExist(UIMAP_VSASearchResults.grpPerCasesResoln, 10);

		if (CasesYearResoln != null && CasesYearResoln.isDisplayed())
			report.updateTestLog("Verify Number of Cases per Year by Resolution Graph is displayed", "Number of Cases per Year by Resolution Graph is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Number of Cases per Year by Resolution Graph is displayed", "Number of Cases per Year by Resolution Graph is not displayed", Status.FAIL);

		if (CasesYear != null && CasesYear.isDisplayed())
			report.updateTestLog("Verify Number of Cases per Year Graph is displayed", "Number of Cases per Year Graph is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Number of Cases per Year Graph is displayed", "Number of Cases per Year Graph is not displayed", Status.FAIL);

		if (AwardResoln != null && AwardResoln.isDisplayed())
			report.updateTestLog("Verify Award in US Dollars by Resolution Graph is displayed", "Award in US Dollars by Resolution Graph is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Award in US Dollars by Resolution Graph is displayed", "Award in US Dollars by Resolution Graph is not displayed", Status.FAIL);

		if (PerCasesResoln != null && PerCasesResoln.isDisplayed())
			report.updateTestLog("Verify Percentage of Cases by Resolution Graph is displayed", "Percentage of Cases by Resolution Graph is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Percentage of Cases by Resolution Graph is displayed", "Percentage of Cases by Resolution Graph is not displayed", Status.FAIL);

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify VSA Search Result value is
	// displayed
	// # Function Name : verifyVSASearchResultValueNew
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public VSASearchResults verifyVSASearchResultValueNew() {

		int convertedValue;

		WebElement SearchResultsHeader = commonLibrary.isExist(UIMAP_VSASearchResults.searchHeader, 10);
		if (SearchResultsHeader != null) {

			WebElement SearchResultsHeaderValue = commonLibrary.isExist(SearchResultsHeader, UIMAP_VSASearchResults.searchHeaderValue, 10);
			if (SearchResultsHeaderValue != null) {

				String Value = SearchResultsHeaderValue.getText();
				Value = Value.substring(Value.indexOf('(') + 1, Value.indexOf(')'));
				Value = Value.replace(",", "");
				if (Value != null) {

					convertedValue = Integer.parseInt(Value);

					if (convertedValue > 0) {
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is displayed as " + convertedValue, Status.PASS);
					} else
						report.updateTestLog("The SearchResult value is displayed", "The SearchResult value is not displayed", Status.FAIL);

				}
			}
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get total result count in VSA
	// results page

	// # Function Name : getResultcountVSA  
	// # Author : Sriram
	// # Date Created : Dec 15
	// #*****************************************************************************************************************************
	public VSASearchResults getResultcountVSA(String tcName, String dataSheet, String colName) {
		String s1 = "";

		WebElement SearchResultsHeader = commonLibrary.isExist(UIMAP_VSASearchResults.searchHeader, 10);
		if (SearchResultsHeader != null) {

			WebElement SearchResultsHeaderValue = commonLibrary.isExist(SearchResultsHeader, UIMAP_VSASearchResults.searchHeaderValue, 10);
			if (SearchResultsHeaderValue != null) {

				String Value = SearchResultsHeaderValue.getText();
				Value = Value.substring(Value.indexOf('(') + 1, Value.indexOf(')'));
				Value = Value.replace(",", "");
				if (Value != null) {

					s1 = String.valueOf(Integer.parseInt(Value));

				}
			}

		}
		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, s1);
		report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : verifyresultcountVSA   
	// # Author : Sriram
	// # Date Created :Dec 15
	// #*****************************************************************************************************************************

	public VSASearchResults verifyresultcountVSA(String rescnt1, String rescnt2) {

		int v1, v2 = 0;
		v1 = Integer.parseInt(rescnt1);
		v2 = Integer.parseInt(rescnt2);
		if (v2 < v1) {
			report.updateTestLog("Verify the current total number of results is less than the prior number of results ", "The current total number of results is less than the prior number of results", Status.PASS);
		} else {
			report.updateTestLog("Verify the current total number of results is less than the prior number of results", "The current total number of results is not less than the prior number of results", Status.FAIL);
		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilterVSA
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public VSASearchResults selectPostFilterVSA(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;

			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {

				if (filterHeader.get(i).getText().contains("Search Within Results"))
					j = j + 1;
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = j + 1;
				if (filterHeader.get(i).getText().contains("Award Amount"))
					j = j + 1;

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
						if (item.getText().contains(strFilter)) {

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

		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify cite list Order
	// # Function Name : verifyCiteListOrder
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public VSASearchResults verifyCiteListOrder() {

		WebElement frmClassResult = commonLibrary.isExist(UIMAP_VSASearchResults.frmClassResult, 10);
		List<WebElement> result = commonLibrary.isExistList(frmClassResult, UIMAP_VSASearchResults.resultlist, 10);
		int i = 0, j = 0, k = 0;

		for (i = 0; i < result.size(); i++) {
			if (result.get(i).getAttribute("class").toString().equals("graphs-container")) {
				j = i;
			}

			if (result.get(i).getAttribute("class").toString().equals("wrapper")) {
				k = i;
			}

		}

		if (k > j)
			report.updateTestLog("Verify citeList is displayed below graph", "citeList is displayed below graph", Status.PASS);

		else
			report.updateTestLog("Verify citeList is displayed below graph", "citeList is not displayed below graph", Status.FAIL);

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickgetitnow from VSA results page
	// # Function Name : clickGetItNowbutton
	// # Author : Sriram
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************
	public Document clickGetItNowbutton(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
			List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
			for (WebElement list : OListResult1) {
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleDoc, 2);

						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
							WebElement btnGet = commonLibrary.isExist(document, UIMAP_SearchResult.btnGetIt, 20);
							if (btnGet != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
								// lnkDocument.getText());
								commonLibrary.clickJS(btnGet);
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
			report.updateTestLog("Click on Get It Now Button below doc Title" + strDocTitle, "Clicked  on the Get It Now button below " + strDocTitle, Status.PASS);
		else

			report.updateTestLog("Click on Get It Now Button below doc Title" + strDocTitle, "Not Clicked  on the Get It Now button below " + strDocTitle, Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given Doc link
	// # Function Name : clickGraphPocbr
	// # Author : Kirthika
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VSASearchResults clickGraphPocbr() {

		commonLibrary.clickLinkWithWait(UIMAP_VSASearchResults.grpPerCasesResoln, "Percentage of Cases by Resolution Graph");

		WebElement largeGraph4 = commonLibrary.isExist(UIMAP_VSASearchResults.ltgGrpPerCasesResoln, 10);
		WebElement closeButton4 = commonLibrary.isExist(largeGraph4, UIMAP_VSASearchResults.closeButton, 10);

		if (largeGraph4 != null && largeGraph4.isDisplayed())
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is expanded", Status.PASS);
		else
			report.updateTestLog("Verify if the Graph is expanded", "The Graph is NOT expanded", Status.FAIL);

		commonLibrary.clickButtonLogSmallWait(closeButton4, "Close");

		return new VSASearchResults(scriptHelper);
	}

}