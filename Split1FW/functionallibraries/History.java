package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_AlertsSearchResults;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_History;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_WorkFolders;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class History extends ReusableLibrary {
	public static int check = 0;
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public History(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("history"))
				commonLibrary.sleep(5000);

		} while (!url.contains("history") && counter < 40);

		if (!driver.getCurrentUrl().contains("history")) {
			throw new IllegalStateException("History page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify client name
	// # Function Name : verifyClientName     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyClientName(String client, String srchterm) {
		boolean clientPresent = false;
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(srchterm.toLowerCase())) {
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
										clientPresent = true;
										break;
									}
								}
							}
							if (clientPresent)
								break;

						}
					}
					if (clientPresent)
						break;
				}
			}
		}
		if (clientPresent) {
			report.updateTestLog("Verify client section of search activity and " + client + " displays below the client section", "Client section of search activity is verified and " + client + " displays below the client section", Status.PASS);
		} else {
			report.updateTestLog("Verify client section of search activity and " + client + " displays below the client section", "Client section of search activity is not verified and " + client + " is not displayed below the client section", Status.PASS);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set new client ID
	// # Function Name : setNewClientIDGeneric     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Research setNewClientIDGeneric(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_SearchResult.rdoClientId);
		if (rdoClientId != null) {
			// report.updateTestLog("Selecting Set New Client ID",
			// "New Client Option Selected.", Status.PASS);
			commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID", "Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_SearchResult.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client id");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Research(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to client link
	// # Function Name : NavigateToClientLink     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public void navigateToClientLink(String strLinkName) {

		try {

			WebElement btnClassClient = commonLibrary.isExist(UIMAP_Home.btnClassClient, 20);
			commonLibrary.clickButtonParentWithWait(btnClassClient, "Client");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
				WebElement lnkTxtSetAddClientID = commonLibrary.isExist(UIMAP_Home.btnActionSetAddClientID, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtSetAddClientID, "Set/Add Client ID");
			} else if (strLinkName.equalsIgnoreCase("-None-")) {
				WebElement lnkTxtNone = commonLibrary.isExist(UIMAP_Home.lnkTxtNone, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtNone, "None");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtNone, "None");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout     
	// # Author : Seetha
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
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
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
	// # Function Description : Function to click document link
	// # Function Name : ClickDocLink1     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String strDocTitle) {

		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		// for (i = 0; i < 3; i++) {
		// if (commonLibrary.isExist_Negative(UIMAP_SearchResult.frmClassResult,
		// 10) != null)
		// resultClass =
		// commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		//
		// if (resultClass != null) {
		// WebElement OListResult = commonLibrary.isExist(resultClass,
		// UIMAP_SearchResult.oltag, 20);
		// if (OListResult != null) {
		// List<WebElement> OListItems = commonLibrary.isExist_List(OListResult,
		// UIMAP_SearchResult.listtag, 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// UIMAP_SearchResult.atag, 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
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
		// commonLibrary.clickButton_Parent_WithWait(btnNextPage, "Next Page");
		// } else
		// break;
		// }
		//
		// if (!blnFlag)
		//
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		// WebElement pgHeader = null;
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
		// " is displayed", "document " + strDocTitle + " is displayed",
		// Status.FAIL);
		// }
		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click VSA topic from History page
	// # Function Name : ClickVSATopicLink     
	// # Author : Divakar
	// # Date Created : 11 Mar'15
	// #*****************************************************************************************************************************

	public VSASearchResults clickVSATopicLink(String vsaTopicName) {

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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(vsaTopicName.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.highlightElement(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				try {
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				} catch (StaleElementReferenceException e) {
					System.out.println(e.toString());
					// TODO: handle exception
				}

			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the VSA topic " + vsaTopicName, "Not Clicked  on the VSA topic " + vsaTopicName, Status.FAIL);

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : clickDocLink2     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocLink2(String strDocTitle) {
		//
		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
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
		// eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(lnkDocument, lnkDocument.getText());
		// commonLibrary.sleep(100000);
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
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
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnNextPage, "Next Page");
		// else
		// commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
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
	// # Function Description : Function to verify history page
	// # Function Name : VerifyHistoryPage     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryPage() {
		boolean pagedisplayed = false;
		List<WebElement> header = commonLibrary.isExistList(UIMAP_Home.historyPageHeader, 20);
		for (WebElement item : header) {
			if (item.getText().toLowerCase().contains("history")) {
				pagedisplayed = true;
				break;
			}
		}

		if (pagedisplayed) {
			report.updateTestLog("Verify whether history page is displayed", "history page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify whether history page is displayed", "history page is not displayed", Status.FAIL);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to history footer link
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Divakar
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToHistoryFooterLink(String strLinkName) {

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

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document title and other
	// terms in history page
	// # Function Name : HistVerifyDetails     
	// # Author : Aravind
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public History histVerifyDetails(String DocTitle, String docType, String type, String originated, String client, String dateTime) {
		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (client.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											clientPresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
											clientPresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time")) {
									if (dateTime.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											datePresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
											datePresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(type)) {
										typePresent = true;
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(originated)) {
										origPresent = true;
									}
								} else if (clientHeading.get(i).getText().trim().toLowerCase().contains("type:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(docType)) {
										docTypePresent = true;
									}
								}
								if (docType == "") {
									docTypePresent = true;
								}

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Research Map button from right
	// side of header section (not from History dropdown).
	// # Function Name : clickResearchMap     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickResearchMap() {
		try {
			WebElement header = commonLibrary.isExist(UIMAP_History.headerDiv, 10);
			WebElement researchMap = commonLibrary.isExist(header, UIMAP_History.researchMap, 10);
			if (researchMap != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(researchMap, "ResearchMap");
				else
					commonLibrary.clickButtonParentWithWait(researchMap, "ResearchMap");
			}
			commonLibrary.sleep(15000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document title and other
	// terms in history page for saved to folder activity
	// # Function Name : HistVerifyDetailsSavedFolder     
	// # Author : Deepha H
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public History HistVerifyDetailsSavedFolder(String DocTitle, String savedItems, String savedTo, String type, String originated, String client, String dateTime) {
		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean docTypePresent1 = false;
		boolean allData = false;
		boolean docTil = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (client.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											clientPresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
											clientPresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time")) {
									if (dateTime.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											datePresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
											datePresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(type)) {
										typePresent = true;
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(originated)) {
										origPresent = true;
									}
								} else if (clientHeading.get(i).getText().trim().toLowerCase().contains("items:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(savedItems)) {
										docTypePresent = true;
									}
								} else if (clientHeading.get(i).getText().trim().toLowerCase().contains("To:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(savedTo)) {
										docTypePresent1 = true;
									}
								}
								if (savedItems == "") {
									docTypePresent = true;
								}
								if (savedTo == "") {
									docTypePresent1 = true;
								}

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && docTypePresent1)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && docTypePresent1) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, Saved Items, Saved To, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Saved Items is NOT displayed as expected", Status.FAIL);
			if (!docTypePresent1)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Saved To is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history detail
	// # Function Name : verifyHistoryDetail     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	private boolean verifyHistoryDetail(String DocTitle, String header, String value) {
		boolean flag = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							List<WebElement> Val = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> Heading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < Heading.size(); i++) {
								if (Heading.get(i).getText().trim().equalsIgnoreCase(header)) {
									if (value.equalsIgnoreCase("Exists")) {
										if (Val.get(i).getText() != "") {
											flag = true;
											break;
										}
									} else {
										if (Val.get(i).getText().toLowerCase().contains(value.toLowerCase())) {
											flag = true;
											break;
										}
									}
								}
							}
							if (flag)
								break;
						}

					}
					if (flag)
						break;
				}

			}
		}
		return flag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details
	// # Function Name : verifyHistoryDetails     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetails(String DocTitle, String docType, String type, String originated, String client, String dateTime, String term, String contentType) {
		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean blnTerm = false;
		boolean blnCT = false;

		if (client.equalsIgnoreCase("ignore"))
			clientPresent = true;
		if (dateTime.equalsIgnoreCase("ignore"))
			datePresent = true;
		if (type.equalsIgnoreCase("ignore"))
			typePresent = true;
		if (originated.equalsIgnoreCase("ignore"))
			origPresent = true;
		if (docType.equalsIgnoreCase("ignore"))
			docTypePresent = true;
		if (term.equalsIgnoreCase("ignore"))
			blnTerm = true;
		if (contentType.equalsIgnoreCase("ignore"))
			blnCT = true;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							// List<WebElement> clientVal =
							// commonLibrary.isExist_List(item,UIMAP_SearchResult.dd,10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if ((!clientPresent) && clientHeading.get(i).getText().equalsIgnoreCase("Client"))
									clientPresent = verifyHistoryDetail(DocTitle, "Client", client);
								else if ((!datePresent) && clientHeading.get(i).getText().equalsIgnoreCase("Date & time"))
									datePresent = verifyHistoryDetail(DocTitle, "Date & time", dateTime);
								else if ((!typePresent) && clientHeading.get(i).getText().equalsIgnoreCase("Type"))
									typePresent = verifyHistoryDetail(DocTitle, "Type", type);
								else if ((!origPresent) && clientHeading.get(i).getText().equalsIgnoreCase("Originated In"))
									origPresent = verifyHistoryDetail(DocTitle, "Originated In", originated);
								else if ((!docTypePresent) && clientHeading.get(i).getText().trim().equalsIgnoreCase("Type:"))
									docTypePresent = verifyHistoryDetail(DocTitle, "Type:", docType);
								else if ((!blnTerm) && clientHeading.get(i).getText().trim().equalsIgnoreCase("Terms:"))
									blnTerm = verifyHistoryDetail(DocTitle, "Terms:", term);
								else if ((!blnCT) && clientHeading.get(i).getText().trim().equalsIgnoreCase("Content Type:"))
									blnCT = verifyHistoryDetail(DocTitle, "Content Type:", contentType);

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);

			if (!blnTerm)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Term is NOT displayed as expected", Status.FAIL);

			if (!blnCT)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "ContentType is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select RM node and to verify popup
	// # Function Name : selectRMNode_VerifyPopup     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ResearchMap selectRMNodeVerifyPopup(String searchTerm, String category, String content) {

		boolean blnFlag = false;
		boolean flag = false;
		String popupContent = "";
		String[] cont;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().equals(searchTerm.toLowerCase())) {
				List<WebElement> lnkClassBubble = commonLibrary.isExistList(trail, UIMAP_SearchResult.lnkClassBubble, 20);
				if (lnkClassBubble.size() > 0) {

					for (WebElement item : lnkClassBubble) {
						if (item != null && item.getText().toLowerCase().equals(category.toLowerCase())) {
							commonLibrary.clickLinkWithWebElementWithWait(item, "BubbleLink :" + category);
							WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
							popupContent = popup.getText();
							cont = content.split("#");
							flag = true;
							for (String c : cont) {
								if (popupContent.contains(c)) {
									blnFlag = true;
								} else {
									blnFlag = false;
									break;
								}
								flag = flag && blnFlag;
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
			report.updateTestLog("Verify if the popup displays the necessary details ", "Popup displayes the following: " + content, Status.PASS);
		else
			report.updateTestLog("Verify if the popup displays the necessary details ", "Popup NOT displayes the following: " + content, Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details
	// # Function Name : verifyHistoryDetails1     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetails1(String DocTitle, String docType, String type, String originated, String client, String dateTime, String term, String contentType, String narrowedBy) {

		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean blnTerm = false;
		boolean blnCT = false;
		boolean narrow = false;

		if (client.equalsIgnoreCase("ignore"))
			clientPresent = true;
		if (dateTime.equalsIgnoreCase("ignore"))
			datePresent = true;
		if (type.equalsIgnoreCase("ignore"))
			typePresent = true;
		if (originated.equalsIgnoreCase("ignore"))
			origPresent = true;
		if (docType.equalsIgnoreCase("ignore"))
			docTypePresent = true;
		if (term.equalsIgnoreCase("ignore"))
			blnTerm = true;
		if (contentType.equalsIgnoreCase("ignore"))
			blnCT = true;
		if (narrowedBy.equalsIgnoreCase("ignore"))
			narrow = true;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							// List<WebElement> clientVal =
							// commonLibrary.isExist_List(item,UIMAP_SearchResult.dd,10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client") && clientPresent == false)
									clientPresent = verifyHistoryDetail(DocTitle, "Client", client);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time") && datePresent == false)
									datePresent = verifyHistoryDetail(DocTitle, "Date & time", dateTime);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Type") && typePresent == false)
									typePresent = verifyHistoryDetail(DocTitle, "Type", type);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In") && origPresent == false)
									origPresent = verifyHistoryDetail(DocTitle, "Originated In", originated);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Search Type:") && docTypePresent == false)
									docTypePresent = verifyHistoryDetail(DocTitle, "Search Type:", docType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Terms:") && blnTerm == false)
									blnTerm = verifyHistoryDetail(DocTitle, "Terms:", term);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Content Type:") && blnCT == false)
									blnCT = verifyHistoryDetail(DocTitle, "Content Type:", contentType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Narrowed By:") && narrow == false)
									narrow = verifyHistoryDetail(DocTitle, "Narrowed By:", narrowedBy);

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)

			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client, Originated and Narrowed By is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);

			if (!blnTerm)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Term is NOT displayed as expected", Status.FAIL);

			if (!blnCT)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "ContentType is NOT displayed as expected", Status.FAIL);
			if (!narrow)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Narrowed By is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details LIT
	// # Function Name : verifyHistoryDetails_LIT     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetails_LIT(String DocTitle, String docType, String type, String originated, String client, String dateTime, String term, String contentType, String narrowedBy, String VerifyLITText) {

		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean blnTerm = false;
		boolean blnCT = false;
		boolean narrow = false;
		boolean LITText = false;

		if (client.equalsIgnoreCase("ignore"))
			clientPresent = true;
		if (dateTime.equalsIgnoreCase("ignore"))
			datePresent = true;
		if (type.equalsIgnoreCase("ignore"))
			typePresent = true;
		if (originated.equalsIgnoreCase("ignore"))
			origPresent = true;
		if (docType.equalsIgnoreCase("ignore"))
			docTypePresent = true;
		if (term.equalsIgnoreCase("ignore"))
			blnTerm = true;
		if (contentType.equalsIgnoreCase("ignore"))
			blnCT = true;
		if (narrowedBy.equalsIgnoreCase("ignore"))
			narrow = true;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							WebElement clientVal = commonLibrary.isExist(item, UIMAP_SearchResult.displayItem, 10);
							if (clientVal != null && clientVal.getText().contains(VerifyLITText))
								LITText = true;

							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client"))
									clientPresent = verifyHistoryDetail(DocTitle, "Client", client);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time"))
									datePresent = verifyHistoryDetail(DocTitle, "Date & time", dateTime);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Type"))
									typePresent = verifyHistoryDetail(DocTitle, "Type", type);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In"))
									origPresent = verifyHistoryDetail(DocTitle, "Originated In", originated);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Search Type:"))
									docTypePresent = verifyHistoryDetail(DocTitle, "Search Type:", docType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Terms:"))
									blnTerm = verifyHistoryDetail(DocTitle, "Terms:", term);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Content Type:"))
									blnCT = verifyHistoryDetail(DocTitle, "Content Type:", contentType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Narrowed By:"))
									narrow = verifyHistoryDetail(DocTitle, "Narrowed By:", narrowedBy);

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}

		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);

			if (!blnTerm)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Term is NOT displayed as expected", Status.FAIL);

			if (!blnCT)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "ContentType is NOT displayed as expected", Status.FAIL);
		}
		if (LITText)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "Expected LIT Text: " + VerifyLITText + " present  ", Status.PASS);
		else
			report.updateTestLog("Verify the History details for the document " + DocTitle, "Expected LIT Text: " + VerifyLITText + " NOT displayed ", Status.FAIL);

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select filter
	// # Function Name : selectFilter     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History selectFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 1;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
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

			if (!Flag) {
				List<WebElement> objFolderDoc = commonLibrary.isExistList(By.cssSelector("button[data-type='filterbutton']"), 5);
				for (i = 0; i < objFolderDoc.size(); i++) {
					if (objFolderDoc.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						commonLibrary.clickLinkWithWebElementWithWait(objFolderDoc.get(i), objFolderDoc.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter, "Required Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
			}

			if (!Flag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
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
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 10);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section contains " + strFilter, Status.FAIL);
		}

		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify folder document and to view
	// filter page
	// # Function Name : verifyFolderDoc_ViewFilterPage     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyFolderDocViewFilterPage(String FolderName, String DocTitle, String type, String originated, String client, String dateTime) {
		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean folder_Exist = false;

		WebElement FolderLink = commonLibrary.isExist(By.partialLinkText(FolderName), 2);
		if (FolderLink != null)
			folder_Exist = true;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (client.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											clientPresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
											clientPresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time")) {
									if (dateTime.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											datePresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
											datePresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(type)) {
										typePresent = true;
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(originated)) {
										origPresent = true;
									}
								}

								if (docTil && clientPresent && datePresent && typePresent && origPresent)
									break;
							}

						}
						if (folder_Exist && docTil && clientPresent && datePresent && typePresent && origPresent) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Folder Name, Title, DocType : " + type + ", originated in :" + originated + ", DateTime and Client  is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : clickDocumentLink     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocumentLink(String doctitle, String TC_Name) {
		try {

			// selecting the document
			Boolean blnFlag = false;
			int DocSize = doctitle.length();
			if (DocSize > 30)
				DocSize = 30;

			WebElement docSelect = commonLibrary.isExist(By.partialLinkText(doctitle.substring(0, DocSize)), 1);

			if (docSelect != null) {
				blnFlag = true;
				commonLibrary.clickJS(docSelect, "Document Title");
				commonLibrary.sleep(15000);
				commonLibrary.sleep(Mwait);

			}
			pageCheck.positiveCheck(driver, "document", "Document");
			WebElement copycitaton = commonLibrary.isExist(By.cssSelector("button[id='copycitaton']"), 1000);
			int count = 0;
			do {
				copycitaton = commonLibrary.isExist(By.cssSelector("button[id='copycitaton']"), 1000);
				count = count + 1;
			} while (copycitaton == null && count <= 1000);
			WebElement objDocument = commonLibrary.isExist(UIMAP_WorkFolders.resultHeader, 10);
			if (blnFlag && true && objDocument.getText().contains(doctitle)) {
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
			} else
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is NOT opened", Status.FAIL);

			return new Document(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link LIT
	// # Function Name : clickDocumentLink_LIT     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// vidhya
	public LegalIssueTrail clickDocumentLinkLIT(String doctitle, String TC_Name) {
		try {

			// selecting the document
			Boolean blnFlag = false;

			// WebElement folderResult =
			// commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire,
			// 10);
			// WebElement folderContents =
			// commonLibrary.isExist(folderResult,UIMAP_WorkFolders.folderContents_RegExp,
			// 20);
			// List<WebElement> contentList =
			// commonLibrary.isExist_List(folderContents,By.tagName("li"), 20);
			// for(WebElement item:contentList)
			// {

			// commonLibrary.click_JS(folderLink,FolderName);
			int DocSize = doctitle.length();
			if (DocSize > 30)
				DocSize = 30;

			WebElement docSelect = commonLibrary.isExist(By.partialLinkText(doctitle.substring(0, DocSize)), 1);

			if (docSelect != null) {
				blnFlag = true;
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(docSelect, "Document Title");

				} else
					commonLibrary.clickButtonParentWithWait(docSelect, "Document Title");
				commonLibrary.sleep(5000);
				commonLibrary.sleep(Mwait);
				// break;
			}
			// }
			WebElement objDocument = commonLibrary.isExist(UIMAP_WorkFolders.documentSection, 10);

			if (blnFlag && true && objDocument.getText().contains(doctitle)) {
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
				// takeScreenShot(TC_Name,
				// "Verify whether the full document view is opened for "+doctitle);

			} else
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is NOT opened", Status.FAIL);

			return new LegalIssueTrail(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take screenshot
	// # Function Name : takeScreenShot     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			String strScreenshot = strTCName + commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot + ".jpg";

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep, "Perform Manual Verification for this step. screenshot is saved in " + strDestination + "", Status.WARNING);
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click folder link
	// # Function Name : clickFolderLink     
	// # Author : Vidhya
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// //#*****************************************************************************************************************************
	// # Function Description : Function to select the folder by filtering the
	// name
	// # Function Name : selectFolder     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Document clickFolderLink(String FolderName, String doctitle, String TC_Name) {
		try {

			// selecting the folder
			Boolean blnFlag = false;

			WebElement folderResult = commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire, 20);
			WebElement folderContents = commonLibrary.isExist(folderResult, UIMAP_WorkFolders.folderContents_RegExp, 20);
			List<WebElement> contentList = commonLibrary.isExistList(folderContents, By.tagName("li"), 20);
			for (WebElement item : contentList) {
				WebElement folderLink = commonLibrary.isExist(item, By.partialLinkText(FolderName), 20);
				if (folderLink != null) {

					commonLibrary.clickJS(folderLink, FolderName);

					WebElement docSelect = commonLibrary.isExist(By.partialLinkText(doctitle.substring(0, 20)), 1);

					if (docSelect != null) {
						blnFlag = true;

						commonLibrary.clickJS(docSelect, doctitle);
						commonLibrary.sleep(10000);
						break;
					}

				}

			}
			pageCheck.positiveCheck(driver, "document", "Document");
			WebElement copycitaton = commonLibrary.isExist(By.cssSelector("button[id='copycitaton']"), 1000);
			int count = 0;
			do {
				copycitaton = commonLibrary.isExist(By.cssSelector("button[id='copycitaton']"), 1000);
				count = count + 1;
			} while (copycitaton == null && count <= 1000);
			WebElement objDocument = commonLibrary.isExist(UIMAP_WorkFolders.resultHeader, 10);

			if (blnFlag && objDocument.getText().contains(doctitle)) {
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
				// takeScreenShot(TC_Name,
				// "Verify whether the full document view is opened for "+doctitle);
			} else
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is NOT opened", Status.FAIL);

			return new Document(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within search
	// # Function Name : searchWithinSearch     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to search Within Search
	// # Function Name : searchWithinSearch    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public History searchWithinSearch(String sectionHeaderName, String searchTerm) {

		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(sectionHeaderName.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), sectionHeaderName);
				report.updateTestLog("Expanding Header: " + sectionHeaderName, sectionHeaderName + " Header Expanded.", Status.DONE);
			}
		}
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_History.searchWithinSearchBox, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);

		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		// WebElement resultClass =
		// commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		int count = 0;
		do {
			count++;
			commonLibrary.sleep(10000);
		} while (count < 15);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click clear search
	// # Function Name : clickClearSearch     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Clear Search
	// # Function Name : clickClearSearch    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public History clickClearSearch() {
		WebElement resultPageHeader = commonLibrary.isExistNegative(UIMAP_History.resultPageHeader, 10);
		WebElement clearSearch = commonLibrary.isExistNegative(resultPageHeader, UIMAP_History.clearSearch, 10);
		commonLibrary.clickButtonParentWithWait(clearSearch, clearSearch.getText());
		report.updateTestLog("Verify document activity ", "All activities/dcoments displayed", Status.PASS);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter
	// # Function Name : verifyPostFilter     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyPostFilter(String strHeader, String contentToVerify, int expectedCount) {
		boolean flg = false;
		String[] contentToVerifyEach = contentToVerify.split(",");
		if (!(strHeader.equals(" "))) {
			int i = 0, j = 1;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
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
					int count = 0;
					for (WebElement item : filters) {
						for (int chk = 0; chk < contentToVerifyEach.length; chk++) {
							String toChk = item.getText();
							if (toChk.substring(0, toChk.length() - 3).contains(contentToVerifyEach[chk])) {
								count++;
							}
							if (count >= expectedCount) {
								if (strHeader.contains("Date")) {
									report.updateTestLog("Verifying filter header: " + strHeader + " content", strHeader + "  filter has values in chronological order, For first 7 days from today, Filter values are displayed in alpabhetical form. For Eg  .Today, Yesterday, Wednesday", Status.DONE);
								} else if (strHeader.contains("Type")) {
									report.updateTestLog("Verifying filter header: " + strHeader + " content", "Filters are displayed under TYPE filter in Descending order. Eg. Legal Search,Document View,Print,Download etc", Status.DONE);
								} else if (strHeader.contains("Client")) {
									report.updateTestLog("Verifying filter header: " + strHeader + " content", strHeader + "  filter has values " + contentToVerify, Status.DONE);
								}

								flg = true;
								report.updateTestLog("Verify activity count", "Activity count is displayed with each type filter at its end", Status.DONE);
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
		}

		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click to open the shrink list
	// # Function Name : clickToShrinkListOpened     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ToS hrink List Opened
	// # Function Name : clickToShrinkListOpened    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public History clickToShrinkListOpened(String strHeader) {
		int i = 0, j = 1;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

		List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
		for (i = 0; i < filterHeader.size(); i++) {
			if (filterHeader.get(i).getText().contains("Timeline"))
				j = 2;
			if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
				List<WebElement> lessOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.filterMore, 10);
				for (WebElement button : lessOptions) {
					if (button.getText().toLowerCase().contains("less")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonLogSmallWait(button, "Less");
						else
							commonLibrary.clickButtonLogSmallWait(button, "Less");
					}
				}
			}
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select post filter
	// # Function Name : selectPostFilter     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History selectPostFilter(String strHeader, String strFilter) {

		if (strHeader.equalsIgnoreCase("Client")) {
			if (strFilter.equals(" ") || strFilter.length() == 0)
				strFilter = "-None-";
		}
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 1;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
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
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterListHistory, 20);
					for (WebElement item : filters) {
						if (item.getText().contains(strFilter)) {
							// if
							// (browsername.toLowerCase().contains("internet"))
							// commonLibrary.clickButton_Parent_WithWait_JS(item,
							// strFilter);
							// else
							// commonLibrary.clickButton_Parent_WithWait(item,
							// strFilter);
							report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " with a X button'appears under Narrow by section.", Status.DONE);
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item, strFilter);
							else
								commonLibrary.clickButtonParentWithWait(item, strFilter);
							report.updateTestLog("Filter '" + strFilter + "' is selected", "Only the activities performed with '" + strFilter + "' displays in history page", Status.DONE);
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
						report.updateTestLog("Selecting Filter: " + strFilter, "Required Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		pageCheck.ajaxWait(driver);
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear filters
	// # Function Name : clear_Filters     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear_Filters
	// # Function Name : clear_Filters
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public History clear_Filters() {
		WebElement btnClear = commonLibrary.isExist(UIMAP_SearchResult.btnClear, 10);
		if (btnClear != null)
			commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
		report.updateTestLog("Verify whether filters cleared ", "Filters cleared.", Status.DONE);
		try {
			btnClear = commonLibrary.isExistNegative(UIMAP_SearchResult.btnClear, 10);
			int count = 0;
			do {
				commonLibrary.sleep(500000);
				count++;
				btnClear = commonLibrary.isExistNegative(UIMAP_SearchResult.btnClear, 3);
			} while (btnClear != null && count < 25);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		pageCheck.ajaxWait(driver);
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select multiple post filters
	// # Function Name : selectPostFilter_Multiple     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History selectPostFilterMultiple(String strHeader, String strFilters) {

		if (!(strHeader.equals(" "))) {
			int i = 0;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))

					if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

						if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), strHeader);
							else
								commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
							report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
						}
					}
			}
		}

		WebElement filters = null;
		String[] arrFilters = strFilters.split(";");
		filters = commonLibrary.isExist(UIMAP_History.filtersSelectMultiple, 10);
		WebElement SelectMultiple = commonLibrary.isExist(filters, UIMAP_SearchResult.btnSelectMultiple, 10);
		if (SelectMultiple != null) {
			commonLibrary.highlightElement(SelectMultiple);
			commonLibrary.clickButtonLogSmallWait(SelectMultiple, "Select Multiple");
			WebElement SelMultiDialog = commonLibrary.isExist(UIMAP_SearchResult.SelMultiPopUp, 10);
			if (SelMultiDialog != null) {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is displayed", Status.PASS);
				List<WebElement> FilterList = commonLibrary.isExistList(SelMultiDialog, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					for (WebElement item : FilterList) {
						if (item.getText().contains(arrFilters[j])) {
							WebElement ChkBox = commonLibrary.isExist(item, By.tagName("input"), 10);
							commonLibrary.setCheckBox(ChkBox, arrFilters[j]);
							break;
						}
					}
				}

				WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.OKSelMultiPopUp, 10);
				commonLibrary.clickButtonParentWithWait(btnOK, "OK");
				try {
					commonLibrary.sleep(5000);
				} catch (Exception e) {
					System.out.println(e.toString());
					throw new FrameworkException("Exception", e.toString());

				}
				report.updateTestLog("Verify whether the result displayed " + strFilters, strFilters + " filtered results displayed.", Status.DONE);
			} else {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is not displayed", Status.FAIL);
			}

		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to research map
	// # Function Name : navigateToResearchMap     
	// # Author : Aravind
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
	// # Function Description : Function to click research map button
	// # Function Name : clickResearchMapButton     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ResearchMap clickResearchMapButton() {
		WebElement listHistory = commonLibrary.isExistNegative(UIMAP_ResearchMap.researchmap, 10);
		// commonLibrary.click_JS(listHistory, "ResearchMap");
		if (browsername.contains("internet")) {
			// commonLibrary.clickLink_withWebElement(lnkTxtResearchMap,
			// "Research Map");
			try {
				driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
				commonLibrary.clickLinkWithWebElement(listHistory, "Research Map");
				// commonLibrary.click_MouseMove_Action(listHistory,
				// "Research Map");
			} catch (TimeoutException ex) {
				driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
				System.out.println(ex.toString());

			}
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
		} else
			commonLibrary.clickLinkWithWebElement(listHistory, "Research Map");
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details
	// # Function Name : verifyHistoryDetails2     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the history details
	// # Function Name : verifyHistoryDetails2     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetails2(String DocTitle, String docType, String type, String originated, String client, String dateTime, String term, String contentType, String narrowedBy, String jobnumber, String destination, String email) {

		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean blnTerm = false;
		boolean blnCT = false;
		boolean narrow = false;
		boolean jobno = false;
		boolean destine = false;
		boolean emails = false;

		if (client.equalsIgnoreCase("ignore"))
			clientPresent = true;
		if (dateTime.equalsIgnoreCase("ignore"))
			datePresent = true;
		if (type.equalsIgnoreCase("ignore"))
			typePresent = true;
		if (originated.equalsIgnoreCase("ignore"))
			origPresent = true;
		if (docType.equalsIgnoreCase("ignore"))
			docTypePresent = true;
		if (term.equalsIgnoreCase("ignore"))
			blnTerm = true;
		if (contentType.equalsIgnoreCase("ignore"))
			blnCT = true;
		if (narrowedBy.equalsIgnoreCase("ignore"))
			narrow = true;
		if (jobnumber.equalsIgnoreCase("ignore"))
			jobno = true;
		if (destination.equalsIgnoreCase("ignore"))
			destine = true;
		if (email.equalsIgnoreCase("ignore"))
			emails = true;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							// List<WebElement> clientVal =
							// commonLibrary.isExist_List(item,UIMAP_SearchResult.dd,10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client") && clientPresent == false)
									clientPresent = verifyHistoryDetail(DocTitle, "Client", client);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time") && datePresent == false)
									datePresent = verifyHistoryDetail(DocTitle, "Date & time", dateTime);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Type") && typePresent == false)
									typePresent = verifyHistoryDetail(DocTitle, "Type", type);
								else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In") && origPresent == false)
									origPresent = verifyHistoryDetail(DocTitle, "Originated In", originated);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Search Type:") && docTypePresent == false)
									docTypePresent = verifyHistoryDetail(DocTitle, "Search Type:", docType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Terms:") && blnTerm == false)
									blnTerm = verifyHistoryDetail(DocTitle, "Terms:", term);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Content Type:") && blnCT == false)
									blnCT = verifyHistoryDetail(DocTitle, "Content Type:", contentType);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Narrowed By:") && narrow == false)
									narrow = verifyHistoryDetail(DocTitle, "Narrowed By:", narrowedBy);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Job Number:") && jobno == false)
									jobno = verifyHistoryDetail(DocTitle, "Job Number:", jobnumber);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Destination:") && destine == false)
									destine = verifyHistoryDetail(DocTitle, "Destination:", destination);
								else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Emailed to:") && emails == false)
									emails = verifyHistoryDetail(DocTitle, "Emailed to:", email);

								if (docTil && clientPresent && emails && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow && jobno && destine)
									break;
							}

						}
						if (docTil && clientPresent && emails && datePresent && typePresent && origPresent && docTypePresent && blnTerm && blnCT && narrow && jobno && destine) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}
		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);

			if (!blnTerm)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Term is NOT displayed as expected", Status.FAIL);

			if (!blnCT)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "ContentType is NOT displayed as expected", Status.FAIL);

			if (!jobno)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "jobno is NOT displayed as expected", Status.FAIL);

			if (!destine)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "destine is NOT displayed as expected", Status.FAIL);

			if (!emails)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Email is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PDF in the specific path
	// # Function Name : PDFVerification_Path     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public void pdfVerificationPath(String FilePath, String Text, String Exist) {
		try {
			PdfReader reader = new PdfReader(FilePath);
			int Pages = reader.getNumberOfPages();
			boolean blnFlag = false;

			for (int i = 1; i <= Pages; i++) {
				String TestText = PdfTextExtractor.getTextFromPage(reader, i);
				Text = Text.replace(Text.substring(Text.length() - 2), "");
				if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
					blnFlag = true;
					break;
				}

			}
			switch (Exist) {
			case "Exist": {
				if (blnFlag) {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is present in the PDF document", Status.PASS);

				} else {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is not present in the PDF document", Status.FAIL);
				}
				break;
			}
			case "NotExist": {
				if (!blnFlag) {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is not present in the PDF document", Status.PASS);
				} else {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is present in the PDF document", Status.FAIL);
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
	// # Function Description : Function to click document and veirfy PDF.
	// # Function Name : clickDocAndVerifyPDF     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a document and verify the pdf
	// # Function Name : clickDocAndVerifyPDF     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public History clickDocAndVerifyPDF(String docTitle) {
		try {

			String filePath = dataTable.getData("General_Data", "FilePath");

			boolean blnFlag1 = false;
			WebElement historyListContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.historyListContainer, 10);
			List<WebElement> listItems = commonLibrary.isExistList(historyListContainer, UIMAP_SearchResult.listItems, 10);
			for (WebElement item : listItems) {
				WebElement title = commonLibrary.isExist(item, UIMAP_SearchResult.TitleClassDoc, 10);
				if (title.getText().toLowerCase().contains("print: " + docTitle.toLowerCase())) {
					WebElement link = commonLibrary.isExist(title, UIMAP_SearchResult.link, 10);
					commonLibrary.clickButtonParentWithWait(link, link.getText());
					blnFlag1 = true;
					break;
				}
			}

			if (blnFlag1) {
				WebElement historyPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.historyPopup, 10);
				WebElement deliveryLink = commonLibrary.isExist(historyPopup, UIMAP_SearchResult.link, 10);
				String docNames = deliveryLink.getText().trim();
				String docName[] = docNames.split(".PDF");

				String path1 = filePath;

				commonLibrary.fileExistsDelete(path1, docName[0]);
				String DocPath = path1 + "\\" + docName[0] + ".Pdf";

				commonLibrary.clickJS(deliveryLink, "Retrieve Document");

				if ((browsername.contains("internet") && version.contains("11")) || (browsername.contains("internet") && version.contains("10"))) {
					// blnFlag = true;
					// commonLibrary.SwitchToWindow("delivery");
					// document.savePDFBrowser(AutoITPath, path2, docName[0]);
					// commonLibrary.sleep(8000);

				} else if (browsername.equalsIgnoreCase("internet explorer")) {
					// blnFlag = true;
					// commonLibrary.SwitchToWindow("delivery");
					// document.savePDFBrowser(AutoITPath, path2, docName[0]);
					// commonLibrary.sleep(8000);
					// driver.close();
					// commonLibrary.smallWait();
					// commonLibrary.SwitchToWindow("document");

				}

				else if (browsername.equalsIgnoreCase("firefox"))

				{
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_S);
					// CTRL+Z is now pressed (receiving application should see a
					// "key down" event.)
					robot.keyRelease(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_ALT);
					commonLibrary.sleep(1000);
					robot.keyPress(KeyEvent.VK_ENTER);
					commonLibrary.sleep(1000);

				}

				commonLibrary.fileExists(path1, docName[0]);
				this.pdfVerificationPath(DocPath, docTitle, "Exist");

				historyPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.historyPopup, 10);
				WebElement historyPopupClose = commonLibrary.isExist(historyPopup, UIMAP_SearchResult.historyPopupClose, 10);
				commonLibrary.clickButtonParentWithWait(historyPopupClose, "Close");
				commonLibrary.sleep(3000);
			}

			else {
				report.updateTestLog("Click Document Link from History Page", "Document Link not present.", Status.FAIL);
			}
		} catch (Exception e) {

		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify trail in History page
	// # Function Name : verifyTrail     
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyTrail(String searchTerm, String client) {

		boolean flag = false, dateFlag = false;
		WebElement rmDiv = commonLibrary.isExist(UIMAP_ResearchMap.resultDiv, 10);
		List<WebElement> list = commonLibrary.isExistList(rmDiv, UIMAP_ResearchMap.lstTagName, 10);
		for (WebElement item : list) {
			List<WebElement> date = commonLibrary.isExistList(item, UIMAP_ResearchMap.header5, 10);
			dateFlag = false;
			if (item.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				for (WebElement item1 : date) {
					Date dates = null;
					String strSource = item1.getText().replace(".", "");
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyyHH:mm:ss a");
						dates = sdf.parse(strSource);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
					if (dates != null) {
						dateFlag = true;
					} else {
						dateFlag = false;
						break;
					}
				}
			}

			if (item.getText().toLowerCase().contains(client.toLowerCase()) && dateFlag) {
				flag = true;
				break;
			}
		}
		if (flag)
			report.updateTestLog("Verify trial contents", "Title:" + searchTerm + ", date and client information is present", Status.PASS);
		else
			report.updateTestLog("Verify trial contents", "Title:" + searchTerm + ", date and client information is NOT present", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Originated In content for
	// the trail.
	// # Function Name : verifyHistory_orginated_details     
	// # Author : Karthi
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryOrginatedDetails(String originated) {

		boolean origPresent = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, UIMAP_SearchResult.listtag, 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, UIMAP_SearchResult.atag, 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText() != null) {
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {

								if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In")) {
									if (clientVal.get(i).getText().toLowerCase().contains(originated.toLowerCase())) {
										origPresent = true;
										break;
									}
								}

							}
							if (origPresent)
								break;
						}

					}
					if (origPresent)
						break;

				}

			}
		}

		if (origPresent)
			report.updateTestLog("Verify the History details for the document " + originated, originated + "Originated is displayed as expected", Status.PASS);
		else {
			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + originated, originated + "Originated In is NOT displayed as expected", Status.FAIL);

		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filters count.
	// # Function Name : verifyFilterlistcount     
	// # Author : Karthi
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyFilterlistcount(String strHeader) {

		if (!(strHeader.equals(" "))) {

			int i = 0, j = 1;

			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
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
					ArrayList<String> list = new ArrayList<String>();

					List<WebElement> filters1 = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.listtag, 20);
					int len = 1;
					if (filters1.size() > 5) {

						for (WebElement item : filters1) {

							// if (len != filters1.size() - 1) {
							List<WebElement> count = commonLibrary.isExistList(item, UIMAP_SearchResult.spancount, 20);

							len++;

							if (count.size() > 0) {

								list.add(count.get(0).getText());

								// }
							}

						}

					} else {
						for (WebElement item : filters1) {
							if (filters1.size() >= len) {
								if (!(item.getText().equalsIgnoreCase("Select multiple"))) {
									List<WebElement> count = commonLibrary.isExistList(item, UIMAP_SearchResult.spancount, 20);

									len++;
									if (count != null) {
										list.add(count.get(0).getText());

									}
								}
							}

						}
					}
					int totallist = 0;
					for (String arraylist : list) {
						int val = Integer.parseInt(arraylist);
						totallist = totallist + val;

					}
					String listtext = null;
					String listot = null;

					List<WebElement> listheader = commonLibrary.isExistList(By.cssSelector("div[class='resultListheader']"), 10);
					for (WebElement trail : listheader) {
						WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h2"), 10);

						listtext = trailHeader.getText();
					}

					listot = listtext.substring(6, (listtext.length()) - 1);
					String aString = Integer.toString(totallist);
					if (listot.equals(aString)) {
						report.updateTestLog("Verify the Type List " + aString + " count and Type list header count " + listot, "Both of the list count same and displayed as expected", Status.PASS);
					} else {
						report.updateTestLog("Verify the Type List " + aString + " count and Type list header count " + listot, "Both of the list count not same  displayed as expected", Status.FAIL);
					}

				}
			}
		}
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		int count = 0;
		do {
			count++;
		} while (rslt == null && count < 15);

		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify narrow by section in left
	// pane
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifynarrowbyfilterleftpane(String strFilter) {
		// WebElement eltFiltersUsed =
		// commonLibrary.isExist(UIMAP_SearchResult.eltFiltersUsed,20);
		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 10);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(100000);
				count++;
				usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 10);
			} while (usedfilter == null && count < 20);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (usedfilter.getText().toLowerCase().contains(strFilter.toLowerCase())) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section contains " + strFilter, Status.FAIL);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify trail details.
	// # Function Name : verifyHistoryDetailsVerify     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document title and other
	// terms in history page
	// # Function Name : verifyHistoryDetailsVerify     
	// # Author : karthi
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public History verifyHistoryDetailsVerify(String DocTitle, String docType, String type, String originated, String client, String dateTime, String terms, String category) {
		boolean clientPresent = false;
		boolean datePresent = false;
		boolean typePresent = false;
		boolean origPresent = false;
		boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		boolean blgterms = false;
		boolean blgcategory = false;

		WebElement btnClientID = commonLibrary.isExist(UIMAP_SearchResult.btnClientID, 20);
		if (btnClientID != null) {
			String[] clientID = btnClientID.getText().split(":");
			client = clientID[1].trim();
		}
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, UIMAP_SearchResult.listtag, 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, UIMAP_SearchResult.atag, 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (client.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											clientPresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
											clientPresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Date & time")) {
									if (dateTime.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											datePresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
											datePresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(type)) {
										typePresent = true;
									}
								}
								if (type == "") {
									typePresent = true;
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Originated In")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(originated)) {
										origPresent = true;
									}
								}

								else if (clientHeading.get(i).getText().trim().contains("Content Type:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase("Cases")) {
										docTypePresent = true;
									}
								}

								if (docType == "") {
									docTypePresent = true;
								} else if (clientHeading.get(i).getText().trim().contains("Terms:")) {
									if (clientVal.get(i).getText().contains(terms)) {
										blgterms = true;
									}
								}
								if (terms == "") {
									blgterms = true;
								} else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Category:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(category)) {
										blgcategory = true;
									}
								}
								if (category == "") {
									blgcategory = true;
								}

								if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blgterms && blgcategory)
									break;
							}

						}
						if (docTil && clientPresent && datePresent && typePresent && origPresent && docTypePresent && blgterms && blgcategory) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}

		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, DocType, AlertType, DateTime, Client and Originated is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!datePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

			if (!origPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Originated In is NOT displayed as expected", Status.FAIL);

			if (!docTypePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "DocType is NOT displayed as expected", Status.FAIL);
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search.
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		eltSearchbox.clear();
		commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");

		if (strClearFilter) {

			WebElement filterIdCount = commonLibrary.isExistNegative(UIMAP_Home.FilterIdCount, 2);
			if (filterIdCount != null) {
				WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
				// commonLibrary.highlightElement(btnClassFilter);
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

				WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
				// commonLibrary.highlightElement(btnClassClearFilter);
				commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

				WebElement btnIdSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnIdSubSearch, "Search");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
			} else {
				WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
				// commonLibrary.highlightElement(eltSearchbutton);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(eltSearchbutton, "Search");
				else
					commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
			}
		} else {
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(eltSearchbutton, "Search");
			else
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		}

		if (driver.getCurrentUrl().contains("usresearchhome")) {
			commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);

			if (browsername.contains("internet"))
				commonLibrary.clickJS(eltSearchbutton, "Search");
			else
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history detail
	// # Function Name : verifyHistoryDetail     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetail1(String DocTitle, String header, String value) {
		boolean flag = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							List<WebElement> Val = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> Heading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < Heading.size(); i++) {
								if (Heading.get(i).getText().trim().equalsIgnoreCase(header)) {
									if (value.equalsIgnoreCase("Exists")) {
										if (Val.get(i).getText() != "") {
											flag = true;
											break;
										}
									} else {
										if (Val.get(i).getText().toLowerCase().contains(value.toLowerCase())) {
											flag = true;
											break;
										}
									}
								}
							}
							if (flag)
								break;
						}

					}
					if (flag)
						break;
				}

			}
		}
		if (flag) {
			report.updateTestLog("Verify whether the same document is delivered", "The document is delivered successfully", Status.PASS);
		} else {
			report.updateTestLog("Verify whether the same document is delivered", "The document is not delivered successfully", Status.FAIL);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history detail
	// # Function Name : verifyHistoryDetail     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetail2(String DocTitle, String header, String value) {
		boolean flag = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("a"), 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							List<WebElement> Val = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> Heading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < Heading.size(); i++) {
								if (Heading.get(i).getText().trim().equalsIgnoreCase(header)) {
									if (value.equalsIgnoreCase(Val.get(i).getText())) {
										report.updateTestLog("Verify " + value + " is displayed for " + header + "for the link" + DocTitle + "", "" + value + " is displayed for" + DocTitle + "", Status.PASS);
										flag = true;

										break;

									} else {
										report.updateTestLog("Verify " + value + " is displayed for " + header + "for the link" + DocTitle + "", "" + value + " is not displayed for " + DocTitle + "", Status.FAIL);
										flag = true;

										break;

									}
								}
							}
							if (flag)
								break;
						}

					}
					if (flag)
						break;
				}

			}
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the filters in the left pane
	// # Function Name : verifyFilterInLeftPane
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public History verifyFilterInLeftPane(String filter) {
		if (filter != "") {
			String[] filters = filter.split(";");
			for (int i = 0; i < filters.length; i++) {
				WebElement classfilt = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 20);
				if (commonLibrary.verifyFromListButton(classfilt, filters[i])) {
					report.updateTestLog("Verify " + filters[i] + " is present in the left pane", "" + filters[i] + " is present in the left pane", Status.PASS);
					break;
				} else {
					report.updateTestLog("Verify " + filters[i] + " is present in the left pane", "" + filters[i] + " is not present in the left pane", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify filters is present in the left pane", "No filters is passed", Status.PASS);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the filters in the left side
	// pane
	// # Function Name : verifyFiltersInLeftPane
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public History verifyFiltersInLeftPane(String filter) {
		if (filter != "") {
			boolean blnFlag = false;
			String[] filters = filter.split(";");
			for (int i = 0; i < filters.length; i++) {
				WebElement classfilt = commonLibrary.isExist(UIMAP_SearchResult.filtersContainer, 20);
				if (classfilt != null) {

					List<WebElement> button = commonLibrary.isExistList(classfilt, By.tagName("button"), 20);
					for (WebElement buttonname : button) {

						if (buttonname.getText().toLowerCase().contains(filters[i].toLowerCase())) {
							blnFlag = true;
							break;
						}
						if (blnFlag)
							break;
					}
				}
				if (blnFlag) {
					report.updateTestLog("Verify " + filters[i] + " post filter is present in the left pane", "" + filters[i] + "  post filter is present in the left pane", Status.PASS);
					break;
				} else {
					report.updateTestLog("Verify " + filters[i] + " post filter is present in the left pane", "" + filters[i] + " post filter is not present in the left pane", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify filters is present in the left pane", "No filters is passed", Status.PASS);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify DocumentDisplay
	// # Function Name : verifyDocumentDisplay
	// # Author : Baswaraj
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public History verifyDocumentDisplay() {
		WebElement pgHeader = null;
		if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
			pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
		else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
			pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);
		else if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10) != null)
			pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10);

		if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document"))) {
			report.updateTestLog("VErify Document view Activity", "Document View activity ' Priebe v. Nelson, 39 Cal. 4th 1112' displayed and Other activities that exactly contain terms 'Priebe v. Nelson' also displayed", Status.PASS);
		} else {
			report.updateTestLog("VErify Document view Activity", "Document View activity 'Priebe v. Nelson, 39 Cal. 4th 1112' is not displayed", Status.FAIL);
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document view activity for a
	// document is displayed
	// # Function Name : verifyDocumentView     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public History verifyDocumentView(String docTitle) {

		boolean origPresent = false;

		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, UIMAP_SearchResult.listtag, 20);
				for (WebElement item : contentList) {
					WebElement title = commonLibrary.isExistNegative(item, UIMAP_ResearchMap.histPageDocTitle, 10);
					WebElement link = commonLibrary.isExistNegative(title, UIMAP_ResearchMap.lnkLinks, 10);
					if (link.getText().toLowerCase().contains(docTitle.toLowerCase())) {
						List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
						List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
						for (int i = 0; i < clientHeading.size(); i++) {

							if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
								if (clientVal.get(i).getText().toLowerCase().contains("Document View".toLowerCase())) {
									origPresent = true;
									break;
								}
							}

						}
						if (origPresent)
							break;
					}

					if (origPresent)
						break;

				}

			}
		}

		if (origPresent)
			report.updateTestLog("Verify the Document View Activity for the document " + docTitle, "The Document View Activity for the document " + docTitle + " is displayed", Status.PASS);
		else {
			if (!origPresent)
				report.updateTestLog("Verify the Document View Activity for the document " + docTitle, "The Document View Activity for the document " + docTitle + " is displayed", Status.FAIL);

		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : clickDocLink2     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search clickDocLink3(String strDocTitle) {
		//
		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
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
		// eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(lnkDocument, lnkDocument.getText());
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
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
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnNextPage, "Next Page");
		// else
		// commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
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
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : ClickDocLink5 
	// # Author : Deepha H
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public WorkFolders clickDocLink5(String strDocTitl) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		String strDocTitle = null;
		int i = 0;
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null) {
							strDocTitle = eleDocTitle.getText();
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {
			WebElement pgHeader = null;

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.FAIL);
		}

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link
	// # Function Name : ClickDocLink1     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocLink() {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		String strDocTitle = null;
		int i = 0;
		for (i = 0; i < 3; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null) {
							strDocTitle = eleDocTitle.getText();
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {
			WebElement pgHeader = null;

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link and to verify
	// Choose client Id page is displayed
	// # Function Name : clickDocLinkChooseClientId     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkChooseClientId(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {

			check = pageCheck.positiveCheck(driver, "document", "Document");

			pageCheck.handleFlow(driver, check);

			WebElement pgHeader = null;

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC1, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC1, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("choose a client id")))
				report.updateTestLog("Verify Choose a Client ID page is displayed", "Choose a Client ID page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Choose a Client ID page is displayed", "Choose a Client ID page is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click LPS topic from History page
	// # Function Name : clickLPSTopicLink     
	// # Author : KirthikaK
	// # Date Created : 11 Mar'15
	// #*****************************************************************************************************************************

	public LPSReport clickLPSTopicLink(String title) {

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

						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().trim().equals(title.toLowerCase().trim())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.highlightElement(lnkDocument);
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				try {
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				} catch (StaleElementReferenceException e) {
					System.out.println(e.toString());
					// TODO: handle exception
				}

			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the LPS topic " + title, "Not Clicked  on the LPS topic " + title, Status.FAIL);

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the link from History
	// # Function Name : clickHistLink     
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickHistLink(String strHistTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
							if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strHistTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
								WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
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
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strHistTitle, "Not Clicked  on the document " + strHistTitle, Status.FAIL);
		else {

			WebElement atd = commonLibrary.isExist(UIMAP_Document.jumptoCollapse, 10);

			int counter = 0;
			do {
				counter = counter + 1;
				atd = commonLibrary.isExist(UIMAP_Document.jumptoCollapse, 20);
				if (atd == null)
					commonLibrary.sleep(5000);
			} while (atd == null && counter <= 40);

			WebElement pgHeader = null;
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("results")))
				report.updateTestLog("Verify results  " + strHistTitle + " is displayed", pgHeader.getText() + "/" + "results  " + strHistTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify results " + strHistTitle + " is displayed", "results  " + strHistTitle + " is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click web url link
	// # Function Name : clickHistLink     
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public History clickWebUrl(String url) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.article, 2);
							if (eleDocTitle != null) {
								WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.weburl, 20);
								if (lnkDocument != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								}
							}

						}
						if (blnFlag)
							break;

					}
				}
				if (blnFlag)
					break;
			}
		}
		if (commonLibrary.switchToWindow(url)) {
			report.updateTestLog("Verify secondary window opens", "Secondary window is opened with  '" + url + "'", Status.PASS);
			driver.close();
			report.updateTestLog("Click on X button to close secondary window ", "Secondary window is Closed", Status.PASS);
			commonLibrary.switchToWindow("history");
		}

		return new History(scriptHelper);

	}
	
	// #*****************************************************************************************************************************
			// # Function Description : Function to click the link from History
			// # Function Name : clickHistLink     
			// # Author : Meera
			// # Date Created : Oct'15
			// #*****************************************************************************************************************************
		
			public Search clickHistLink1(String strHistTitle) {
				Boolean blnFlag = false;
				WebElement resultClass = null;
				int i = 0;
				for (i = 0; i < 5; i++) {
					if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
						resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		
					if (resultClass != null) {
						WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
						List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
						for (WebElement list : OListResult1) {
							if (OListResult != null) {
								List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
								for (WebElement document : OListItems) {
									WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
									if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strHistTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
										WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
										if (lnkDocument != null) {
											// commonLibrary.ScrollToView(lnkDocument);
											commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
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
						WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
						if (btnNextPage != null)
							commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
						else
							break;
					} else
						break;
				}
		
				if (!blnFlag)
		
					report.updateTestLog("Click on the document " + strHistTitle, "Not Clicked  on the document " + strHistTitle, Status.FAIL);
				else {
		
					WebElement atd = commonLibrary.isExist(UIMAP_Document.jumptoCollapse, 10);
		
					int counter = 0;
					do {
						counter = counter + 1;
						atd = commonLibrary.isExist(UIMAP_Document.jumptoCollapse, 20);
						if (atd == null)
							commonLibrary.sleep(5000);
					} while (atd == null && counter <= 40);
		
					WebElement pgHeader = null;
					if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
						pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
		
					if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("results")))
						report.updateTestLog("Verify results  " + strHistTitle + " is displayed", pgHeader.getText() + "/" + "results  " + strHistTitle + " is displayed", Status.PASS);
					else
						report.updateTestLog("Verify results " + strHistTitle + " is displayed", "results  " + strHistTitle + " is not displayed", Status.FAIL);
				}
				return new Search(scriptHelper);
			}

}
