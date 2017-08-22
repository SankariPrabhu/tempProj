package functionallibraries;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_WorkFolders;
import UIMAP.UIMAP_IMContent;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

//import com.opera.core.systems.scope.protos.UmsProtos.Command;

public class WorkFolders extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public WorkFolders(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("workfolders"))
				commonLibrary.sleep(5000);

		} while (!url.contains("workfolders") && counter < 40);

		if (!driver.getCurrentUrl().contains("workfolders")) {
			throw new IllegalStateException("Work Folders page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click folder title
	// # Function Name : clickFolderTitle     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders clickFolderTitle(String folderTitle) {
		int j = 0;

		try {
			WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
			if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButton(myFoldersExpand);
			}

			WebElement myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
			List<WebElement> folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
			for (j = 0; j < folder.size(); j++) {
				if (folder.get(j).getAttribute("data-title").toLowerCase().contains(folderTitle.toLowerCase())) {
					// commonLibrary.ScrollToView(folder.get(j));
					String folderText = folder.get(j).getAttribute("data-itemcount");
					if (folderText.equalsIgnoreCase("0")) {
						for (int i = 0; i <= 10; i++) {
							// driver.navigate().refresh();
							commonLibrary.sleep(5000);
							Actions builder = new Actions(driver);
							builder.sendKeys(Keys.F5).build().perform();

							commonLibrary.sleep(300000);

							myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
							folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
							folderText = folder.get(j).getAttribute("data-itemcount");
							if (!(folderText.equalsIgnoreCase("0")))
								break;
						}

					}
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(folder.get(j), folderTitle);
					else
						// folder.get(j).click();
						commonLibrary.clickButtonParentWithWait(folder.get(j), folderTitle);
					commonLibrary.sleep(10000);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify recently viewed icon
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Dinesh
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyRecentlyViewed(String strDocTitle, Boolean isDisplayed) {

		Boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {

			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {

				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {

						WebElement RecntlyViewed = commonLibrary.isExist(document, UIMAP_SearchResult.btnRecentlyViewed, 2);
						if (RecntlyViewed != null) {
							blnFlag = true;
							break;
						}
					}

				}

			}
		}

		if (isDisplayed) {
			if (blnFlag)
				report.updateTestLog("Recently viewed icon appears next to '" + strDocTitle + "'", "Recently viewed icon appears next to '" + strDocTitle + "'", Status.PASS);
			else
				report.updateTestLog("Recently viewed icon appears next to '" + strDocTitle + "'", "Recently viewed icon does not appear next to '" + strDocTitle + "'", Status.FAIL);

		} else {

			if (!blnFlag)
				report.updateTestLog("Recently viewed icon not appears next to '" + strDocTitle + "'", "Recently viewed icon not appears next to '" + strDocTitle + "'", Status.PASS);
			else
				report.updateTestLog("Recently viewed icon not appears next to '" + strDocTitle + "'", "Recently viewed icon appear next to '" + strDocTitle + "'", Status.FAIL);
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on PDF link folders
	// # Function Name : clickPDFLinkFolders     
	// # Author : shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders clickPDFLinkFolders(String doctitle) {
		WebElement folderResult = commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire, 20);
		WebElement folderContents = commonLibrary.isExist(folderResult, UIMAP_WorkFolders.folderContents, 20);
		List<WebElement> contentList = commonLibrary.isExistList(folderContents, By.tagName("li"), 20);
		for (WebElement item : contentList) {
			WebElement docLink = commonLibrary.isExist(item, UIMAP_WorkFolders.pdfLink, 20);
			if (docLink != null) {
				WebElement docSelect = commonLibrary.isExist(item, UIMAP_WorkFolders.docSelect, 20);
				commonLibrary.setCheckBox(docSelect, doctitle);
			}
		}
		return new WorkFolders(scriptHelper);
	}

	// //#*****************************************************************************************************************************
	// # Function Description : Function to select the folder by filtering the
	// name
	// # Function Name : selectFolder     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickFolderLink(String FolderName, String doctitle, String TC_Name) {
		try {

			WebElement folderResult = commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire, 20);
			WebElement folderContents = commonLibrary.isExist(folderResult, UIMAP_WorkFolders.folderContents_RegExp, 20);
			List<WebElement> contentList = commonLibrary.isExistList(folderContents, By.tagName("li"), 20);
			for (WebElement item : contentList) {
				WebElement folderLink = commonLibrary.isExist(item, By.partialLinkText(FolderName), 20);
				if (folderLink != null) {

					commonLibrary.clickJS(folderLink, FolderName);
					commonLibrary.sleep(50000);
					WebElement docSelect = commonLibrary.isExist(By.partialLinkText(doctitle.substring(0, 20)), 1);

					if (docSelect != null) {

						commonLibrary.clickJS(docSelect, doctitle);

						commonLibrary.sleep(10000);
						break;
					}

				}
			}
			pageCheck.positiveCheck(driver, "document", "Document");
			generalFunctions.navigateToHistoryLink(doctitle);
			// WebElement objDocument =
			// commonLibrary.isExist(UIMAP_WorkFolders.resultHeader, 10);
			//
			// if (blnFlag && objDocument.getText().contains(doctitle)) {
			// report.updateTestLog("Select the document " + doctitle, "' " +
			// doctitle
			// + " ' document is selected and appropriate document is opened",
			// Status.PASS);
			// // takeScreenShot(TC_Name,
			// //
			// "Verify whether the full document view is opened for "+doctitle);
			// } else
			// report.updateTestLog("Select the document " + doctitle, "' " +
			// doctitle
			// +
			// " ' document is selected and appropriate document is NOT opened",
			// Status.FAIL);

			return new Document(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : to click view folder- navigate to workfolder
	// page
	// # Function Name : selectPopupLinks     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders selectPopupLinks(String searchTerm, String category, String lnkName) {
		boolean blnFlag = false;
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
							List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.lnkLinks, 10);
							for (WebElement link : popuplinks) {
								if (link.getText().toLowerCase().contains(lnkName.toLowerCase())) {
									commonLibrary.clickLinkWithWebElementWithWait(link, lnkName);
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
		if (!blnFlag)
			report.updateTestLog("Click on " + lnkName + " under category menu " + category + " for trail " + searchTerm, lnkName + " link not clicked.", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify FolderPage is Displayed
	// # Function Name : VerifyFolderPageDisplayed     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyFolderPageDisplayed(String FolderName) {
		WebElement folderDoc = commonLibrary.isExist(UIMAP_WorkFolders.folderSection, 20);
		if (folderDoc != null && folderDoc.getText().contains(FolderName))
			report.updateTestLog("Verify Folder : " + FolderName + " page is displayed ", "Folder : " + FolderName + " page is displayed ", Status.PASS);
		else
			report.updateTestLog("Verify Folder : " + FolderName + " page is displayed ", "Folder : " + FolderName + " page is NOT displayed ", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To Histry Footer Link
	// # Function Name : NavigateToHistryFooterLink     
	// # Author : Vidhya
	// # Date Created : Apr'15
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

				commonLibrary.sleep(30000);
				List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 1000);

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
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click FolderLink in DocumentView
	// # Function Name : clickFolderLink_DocumentView     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickFolderLinkDocumentView(String FolderName, String doctitle) {
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
					blnFlag = true;

				}

				commonLibrary.isExist(UIMAP_WorkFolders.documentSection, 10);

				if (blnFlag)
					report.updateTestLog("Select the Folder " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
				else
					report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is NOT opened", Status.FAIL);

			}
			return new Document(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take Screenshot.
	// # Function Name : takeScreenShot     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public WorkFolders takeScreenShot(String strTCName, String strStep) {
		generalFunctions.takeScreenShot(strTCName, strStep);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : clickDocumentLink     
	// # Author : Uma
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickDocumentLink(String doctitle, String TC_Name) {
		try {

			// selecting the document
			Boolean blnFlag = false;

			// WebElement folderResult =
			// commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire,
			// 20);
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
				if (browsername.contains("internet"))
					commonLibrary.clickJS(docSelect, "Document Title");
				else
					docSelect.click();
				// break;
			}

			WebElement objDocument = commonLibrary.isExist(UIMAP_WorkFolders.documentSection, 10);

			if (blnFlag && objDocument.getText().contains(doctitle)) {
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
				takeScreenShot(TC_Name, "Verify whether the full document view is opened for " + doctitle);

			} else
				report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is NOT opened", Status.FAIL);

			// }
			return new Document(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Email
	// # Function Name : clickEmail     
	// # Author : shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Delivery clickEmail(String eMailId) {
		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu, UIMAP_SearchResult.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list, By.tagName("button"), 20);
				commonLibrary.clickButtonParentWithWait(btn1.get(1), "Delivery");
				asideclass = commonLibrary.isExist(list, UIMAP_SearchResult.tagNameAside, 10);
				break;

			}

		}
		if (asideclass != null) {
			WebElement eMail = commonLibrary.isExist(UIMAP_WorkFolders.eMail, 20);
			if (eMail != null) {
				commonLibrary.clickButtonParentWithWait(eMail, "Email");
				WebElement eMailDialog = commonLibrary.isExist(UIMAP_WorkFolders.eMailDialog, 20);
				if (eMailDialog != null) {
					report.updateTestLog("Email Delivery Dialog opens", "Email Delivery Dialog opens", Status.PASS);
					WebElement basicOptions = commonLibrary.isExist(eMailDialog, UIMAP_WorkFolders.basicOptions, 20);
					commonLibrary.clickButtonParent(basicOptions, "Basic Options");
					WebElement eMailAddr = commonLibrary.isExist(eMailDialog, UIMAP_WorkFolders.eMailAddr, 20);
					commonLibrary.setDataInTextBox(eMailAddr, eMailId, "Email Address");

					WebElement sendEmail = commonLibrary.isExist(eMailDialog, UIMAP_WorkFolders.sendEmail, 20);
					commonLibrary.clickButtonParentWithWait(sendEmail, "Send Email");

					commonLibrary.switchToWindow("delivery");
				} else
					report.updateTestLog("Email Delivery Dialog opens", "Email Delivery Dialog is not opened", Status.FAIL);
			}
		}
		return new Delivery(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Email Content
	// # Function Name : verifyEmailContent     
	// # Author : shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyEmailContent(String eMail, String Title) {
		report.updateTestLog("Verify the content present in Email sent to " + eMail, "Verify manually the document in eMail contains the title" + Title, Status.WARNING);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout     
	// # Author : Uma
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Client Name
	// # Function Name : verifyClientName     
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyClientName(String client, String srchterm) {
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
							WebElement clientname = commonLibrary.isExist(item, UIMAP_SearchResult.clientName, 10);
							if (clientname.getText().equalsIgnoreCase(client)) {
								clientPresent = true;
								break;
							}
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

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on ViewAll In History
	// # Function Name : clickViewAllInHistory     
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public History clickViewAllInHistory() {
		try {
			WebElement history = commonLibrary.isExist(UIMAP_SearchResult.history, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(history, "history");
			else
				commonLibrary.clickButtonParentWithWait(history, "History");

			commonLibrary.sleep(5000);
			WebElement viewAllhistory = commonLibrary.isExist(UIMAP_SearchResult.viewAllhistory, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(viewAllhistory, "View all history");
			else
				commonLibrary.clickButtonParentWithWait(viewAllhistory, "View All");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for some verification points under a
	// title in work folder page
	// # Function Name : verificationWithFolderCreated     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public WorkFolders verificationWithFolderCreated(String title) {
		try {
			int i;
			boolean isTitle = false;
			boolean isDate = false;
			boolean isDoc = false;
			boolean isClient = false;
			boolean isLastEdited = false;
			String DocTitle = null;
			String source = null;

			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (i = 0; i < LList.size(); i++) {
				WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.titleWf, 10);
				DocTitle = lnkTitle.getText();
				if (DocTitle.trim().contains(title.trim())) {
					isTitle = true;
					WebElement date = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.update, 10);
					if (date != null) {
						source = date.getText().replace("As of ", "");
						Date dates = new SimpleDateFormat("MMM dd,yyyy").parse(source);
						if (dates != null) {
							isDate = true;
						}

					}
					List<WebElement> clientVal = commonLibrary.isExistList(LList.get(i), UIMAP_SearchResult.dd, 10);
					List<WebElement> clientHeading = commonLibrary.isExistList(LList.get(i), UIMAP_SearchResult.dt, 10);
					for (int j = 0; j < clientHeading.size(); j++) {
						if (clientHeading.get(j).getText().equalsIgnoreCase("Type")) {
							if (clientVal.get(j).getText().equalsIgnoreCase("document")) {
								isDoc = true;
							}
						} else if (clientHeading.get(j).getText().equalsIgnoreCase("Client")) {
							if (clientVal.get(j).getText().equalsIgnoreCase("-None-")) {
								isClient = true;
							}
						} else if (clientHeading.get(j).getText().equalsIgnoreCase("Last Modified")) {
							WebElement modifiedDate = commonLibrary.isExist(clientVal.get(j), By.tagName("span"), 10);
							if (!modifiedDate.getText().equalsIgnoreCase("")) {
								isLastEdited = true;
							}
						}
					}
				}
				if (isTitle && isDate && isDoc && isClient && isLastEdited)
					break;

			}
			if (isTitle) {
				report.updateTestLog("Verify title " + title + " displays", "Title " + title + " is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify title " + title + " displays", "Title " + title + " is not displayed", Status.FAIL);
			}
			if (isDate) {
				report.updateTestLog("Verify Document Update as of mm/dd/yyyy is displayed", "Document Update as of mm/dd/yyyy is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Document Update as of mm/dd/yyyy is displayed", "Document Update as of mm/dd/yyyy is not displayed", Status.FAIL);
			}
			if (isDoc) {
				report.updateTestLog("Verify Document is present in Type column", "Document is present in Type column", Status.PASS);
			} else {
				report.updateTestLog("Verify Document is present in Type column", "Document is not present in Type column", Status.FAIL);
			}
			if (isClient) {
				report.updateTestLog("Verify -None- is present in Client column", "-None- is present in Client column", Status.PASS);
			} else {
				report.updateTestLog("Verify -None- is present in Client column", "-None- is not present in Client column", Status.FAIL);
			}
			if (isLastEdited) {
				report.updateTestLog("Verify Last Edited date is displayed", "Last Edited date is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Last Edited date is displayed", "Last Edited date is not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle     
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickDocumentTitle(String title) {
		List<WebElement> lnkTitle = commonLibrary.isExistList(UIMAP_WorkFolders.document, 10);
		for (WebElement item : lnkTitle) {
			if (item.getText().equalsIgnoreCase(title)) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(item, title);
				else
					commonLibrary.clickButtonParentWithWait(item, title);
				break;
			}
		}
		commonLibrary.sleep(50000);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Client Last Modified details
	// # Function Name : verifyClientLastModified     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyClientLastModified(String docTitle) {
		int i;
		boolean isTitle = false;

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.titleWf, 10);
			DocTitle = lnkTitle.getText();
			if (DocTitle.trim().contains(docTitle.trim())) {
				report.updateTestLog("Verify document with title: " + docTitle + " is displayed", "document with title: " + docTitle + " is displayed", Status.PASS);
				isTitle = true;

				String docText = LList.get(i).getText();

				if (docText.contains("Client"))
					report.updateTestLog("Verify 'client' information is displayed for document: " + docTitle, "client information is displayed for document: " + docTitle, Status.PASS);
				else
					report.updateTestLog("Verify 'client' information is displayed for document: " + docTitle, "client information is not displayed for document: " + docTitle, Status.FAIL);

				if (docText.contains("Last Modified"))
					report.updateTestLog("Verify 'Last modified' information is displayed for document: " + docTitle, "'Last modified' information is displayed for document: " + docTitle, Status.PASS);
				else
					report.updateTestLog("Verify 'Last modified' information is displayed for document: " + docTitle, "'Last modified' information is not displayed for document: " + docTitle, Status.FAIL);

				break;
			}

		}
		if (!isTitle)
			report.updateTestLog("Verify document with title: " + docTitle + " is displayed", "document with title: " + docTitle + " is not displayed", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on PDF Title
	// # Function Name : clickPDFTitle     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickPDFTitle(String title) {
		List<WebElement> lnkTitle = commonLibrary.isExistList(UIMAP_WorkFolders.pdfLink, 10);
		for (WebElement item : lnkTitle) {
			if (item.getText().equalsIgnoreCase(title)) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(item, title);
				else
					commonLibrary.clickButtonParentWithWait(item, title);
				break;
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on DocumentTitle in
	// InterMediateContent
	// # Function Name : clickDocumentTitle_IMContent     
	// # Author : Divakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickDocumentTitle_IMContent(String title) {
		WebElement formclass = commonLibrary.isExist(By.cssSelector("form[class='results-list search rwdesign']"));
		WebElement wrapper = commonLibrary.isExist(formclass, UIMAP_WorkFolders.folderContents, 20);
		List<WebElement> lnkTitle = commonLibrary.isExistList(wrapper, UIMAP_WorkFolders.intermediate, 10);
		for (WebElement item : lnkTitle) {
			if (item.getText().contains(title)) {
				if (browsername.contains("internet")) {
					try {
						driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
						commonLibrary.clickButtonParentWithWaitJS(item, title);
						// commonLibrary.click_MouseMove_Action(item,
						// item.getText());
						// lnkDocument.click();// Using click ans click_JS not
						// working.
						// report.updateTestLog("Click on the document " +
						// lnkDocument.getText(),
						// "Not Clicked  on the document " +
						// lnkDocument.getText(), Status.PASS);;
					} catch (TimeoutException ex) {
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println(ex.toString());

					}
					driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
				} else
					commonLibrary.clickButtonParentWithWait(item, title);
				break;
			}
		}
		int count = 0;
		WebElement btnEdit = commonLibrary.isExist(UIMAP_Document.editdoc, 20);
		do {
			count++;
			btnEdit = commonLibrary.isExist(UIMAP_Document.editdoc, 20);
			commonLibrary.sleep(10000);
		} while (btnEdit == null && count < 20);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link in Research
	// Map
	// # Function Name : clickDocLink_RM     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders clickDocLinkRM(String strDocTitle) {

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
		// UIMAP_ResearchMap.TitleClassDoc, 20);
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
		// (pgHeader.getText().toLowerCase().contains("folder")))
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle
		// + " is displayed", Status.PASS);
		// else
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is not displayed",
		// Status.FAIL);
		// }
		generalFunctions.clickDocLink(strDocTitle);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Folder Link in Research Map
	// # Function Name : clickFolderLink_RM     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickFolderLinkRM(String FolderName, String doctitle) {
		try {
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
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_ResearchMap.TitleClassDoc, 20);
							if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(doctitle.toLowerCase())) {
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
				} else {
					report.updateTestLog("Click Folder link " + doctitle, doctitle + " clicked'", Status.DONE);
					break;
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the folder in folder page
	// # Function Name : clickDoc_folderlink     
	// # Author : Karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders clickDocfolderlink(String strDocTitle) {

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
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
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

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);

		commonLibrary.sleep(20000);

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link in folder
	// # Function Name : clickDocLink_folder     
	// # Author : Karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickDocLinkfolder(String strDocTitle) {

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
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDocFolder, 2);
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

		if (blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, " Clicked  on the document " + strDocTitle, Status.PASS);
		else {
			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);

		}

		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link in
	// Intermediate
	// # Function Name : clickDocLink_intermediate     
	// # Author : Karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickDocLinkIntermediate(String strDocTitle) {

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
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDocFolder, 2);
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

			if (pgHeader != null && ((pgHeader.getText().toLowerCase().contains("document") || pgHeader.getText().toLowerCase().contains("selected text") || pgHeader.getText().toLowerCase().contains(strDocTitle.toLowerCase()))))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a document is present within
	// a folder
	// # Function Name : verifyDocumentPresent     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyDocumentPresent(String docTitle) {
		int i;
		boolean isTitle = false;

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.titleWf, 10);
			DocTitle = lnkTitle.getText();
			if (DocTitle.trim().contains(docTitle.trim())) {
				report.updateTestLog("Verify document with title: " + docTitle + " is displayed", "document with title: " + docTitle + " is displayed", Status.PASS);
				isTitle = true;

				break;
			}

		}
		if (!isTitle)
			report.updateTestLog("Verify document with title: " + docTitle + " is displayed", "document with title: " + docTitle + " is not displayed", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a document within a folder
	// # Function Name : selectDocByTitle     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders selectDocByTitle(String docTitle)

	{

		int i;
		boolean isTitle = false;

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.titleWf, 10);
			DocTitle = lnkTitle.getText();
			if (DocTitle.trim().contains(docTitle.trim())) {
				WebElement checkbox = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.chkbox, 10);
				commonLibrary.setCheckBox(checkbox, docTitle);
				isTitle = true;
				break;
			}

		}
		if (!isTitle)
			report.updateTestLog("Select document with title: " + docTitle + " is displayed", "Document with title: " + docTitle + " is not displayed", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move selected document from one
	// folder to another
	// # Function Name : moveToNewFolder     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders moveToNewFolder(String docTitle, String folderName) {
		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar1, 10);
		WebElement move = commonLibrary.isExistNegative(mobileToolbar, UIMAP_WorkFolders.move, 10);
		commonLibrary.clickButtonParentWithWait(move, "Move");
		try {
			commonLibrary.sleep(100000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		WebElement folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
		if (folderDialog != null && folderDialog.isDisplayed()) {
			boolean flag = false;
			report.updateTestLog("Verify Move to Folder dialogbox is displayed", "Move to Folder dialogbox is displayed", Status.PASS);
			WebElement tabHeaderContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.tabHeaderContent, 10);
			List<WebElement> tabHeaders = commonLibrary.isExistList(tabHeaderContent, UIMAP_WorkFolders.listItem, 10);
			for (WebElement item : tabHeaders) {
				WebElement tabHeader = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.tabHeader, 10);
				if (tabHeader.getText().toLowerCase().contains("selected documents")) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.button, 10);
					commonLibrary.clickButtonLogSmallWait(button, button.getText());
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Select Selected Documents Tab.", "Selected Documents Tab is not present.", Status.FAIL);
			else {
				boolean flag1 = false;
				folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
				WebElement activeTabContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.activeTabContent, 10);
				WebElement selectedDocsContent = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.selectedDocsContent, 10);
				List<WebElement> selectedDocs = commonLibrary.isExistList(selectedDocsContent, UIMAP_WorkFolders.listItem, 10);
				for (WebElement li : selectedDocs) {
					WebElement checkBox = commonLibrary.isExistNegative(li, UIMAP_SearchResult.chkbox, 10);
					WebElement docName = commonLibrary.isExistNegative(li, UIMAP_WorkFolders.selectedDocsName, 10);
					if (docName.getAttribute("value").toLowerCase().contains(docTitle.toLowerCase()) && checkBox.isSelected() && li.getText().contains("-None-")) {
						report.updateTestLog("Verify document " + docTitle + " check box is checked and Client: -None- is present.", "document " + docTitle + " check box is checked and Client: -None- is present.", Status.PASS);
						flag1 = true;
						break;
					}
				}
				if (!flag1)
					report.updateTestLog("Verify document " + docTitle + " check box is checked and Client: -None- is present.", "document " + docTitle + " check box is checked and Client: -None- is present.", Status.PASS);
				else {
					boolean flag2 = false;
					tabHeaderContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.tabHeaderContent, 10);
					tabHeaders = commonLibrary.isExistList(tabHeaderContent, UIMAP_WorkFolders.listItem, 10);
					for (WebElement item : tabHeaders) {
						WebElement tabHeader = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.tabHeader, 10);
						if (tabHeader.getText().toLowerCase().contains("move options")) {
							WebElement button = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.button, 10);
							commonLibrary.clickButtonLogSmallWait(button, button.getText());
							flag2 = true;
							break;
						}
					}
					if (!flag2)
						report.updateTestLog("Select Move Options Tab.", "Move Options Tab is not present.", Status.FAIL);
					else {
						folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
						activeTabContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.activeTabContent, 10);
						WebElement createNewFolder = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createNewFolder, 10);
						commonLibrary.clickButtonLogSmallWait(createNewFolder, "Create New Folder");

						WebElement createFolderText = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderText, 10);
						commonLibrary.setDataInTextBox(createFolderText, folderName, "New Folder");

						WebElement createFolderButton = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 10);
						commonLibrary.clickButtonParentWithWait(createFolderButton, "Create");

						try {
							createFolderButton = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							WebElement createNew = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							int count = 0;
							do {
								commonLibrary.sleep(100000);
								count++;
								createNew = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							} while (createFolderButton.equals(createNew) && count < 40);
						} catch (Exception e) {
							commonLibrary.sleep(500000);
							System.out.println(e.toString());
						}

						folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
						WebElement moveButton = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.moveButton, 10);
						commonLibrary.clickButtonParentWithWait(moveButton, moveButton.getText());

						try {
							moveButton = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.moveButton, 5);
							WebElement moveNew = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.moveButton, 5);
							int count = 0;
							do {
								commonLibrary.sleep(100000);
								count++;
								moveNew = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.moveButton, 5);
							} while (moveButton.equals(moveNew) && count < 40);
						} catch (Exception e) {
							commonLibrary.sleep(500000);
							System.out.println(e.toString());
						}

					}
				}
			}
		} else
			report.updateTestLog("Verify Move to Folder dialogbox is displayed", "Move to Folder dialogbox is not displayed", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to copy selected document from one
	// folder to another
	// # Function Name : copyToNewFolder     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders copyToNewFolder(String docTitle, String folderName) {
		int i = 0;
		int j = 0;
		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar1, 10);
		WebElement copy = commonLibrary.isExistNegative(mobileToolbar, UIMAP_WorkFolders.copy, 10);
		if (copy != null) {
			commonLibrary.clickButtonParentWithWait(copy, "Copy");
		}
		try {
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		WebElement folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
		if (folderDialog != null && folderDialog.isDisplayed()) {
			boolean flag = false;
			report.updateTestLog("Verify Copy Documents dialogbox is displayed", "Copy Documents dialogbox is displayed", Status.PASS);
			WebElement tabHeaderContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.tabHeaderContent, 10);
			List<WebElement> tabHeaders = commonLibrary.isExistList(tabHeaderContent, UIMAP_WorkFolders.listItem, 10);
			for (WebElement item : tabHeaders) {
				WebElement tabHeader = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.tabHeader, 10);
				if (tabHeader.getText().toLowerCase().contains("selected documents")) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.button, 10);
					commonLibrary.clickButtonLogSmallWait(button, button.getText());
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Select Selected Documents Tab.", "Selected Documents Tab is not present.", Status.FAIL);
			else {
				boolean flag1 = false;
				folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
				WebElement activeTabContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.activeTabContent, 10);
				WebElement selectedDocsContent = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.selectedDocsContent, 10);
				List<WebElement> selectedDocs = commonLibrary.isExistList(selectedDocsContent, UIMAP_WorkFolders.listItem, 10);
				for (WebElement li : selectedDocs) {
					WebElement checkBox = commonLibrary.isExistNegative(li, UIMAP_SearchResult.chkbox, 10);
					WebElement docName = commonLibrary.isExistNegative(li, UIMAP_WorkFolders.selectedDocsName, 10);
					if (docName.getAttribute("value").toLowerCase().contains(docTitle.toLowerCase()) && checkBox.isSelected() && li.getText().contains("-None-")) {
						report.updateTestLog("Verify document " + docTitle + " check box is checked and Client: -None- is present.", "document " + docTitle + " check box is checked and Client: -None- is present.", Status.PASS);
						flag1 = true;
						break;
					}
				}
				if (!flag1)
					report.updateTestLog("Verify document " + docTitle + " check box is checked and Client: -None- is present.", "document " + docTitle + " check box is checked and Client: -None- is present.", Status.PASS);
				else {
					boolean flag2 = false;
					tabHeaderContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.tabHeaderContent, 10);
					tabHeaders = commonLibrary.isExistList(tabHeaderContent, UIMAP_WorkFolders.listItem, 10);
					for (WebElement item : tabHeaders) {
						WebElement tabHeader = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.tabHeader, 10);
						if (tabHeader.getText().toLowerCase().contains("copy options")) {
							WebElement button = commonLibrary.isExistNegative(item, UIMAP_WorkFolders.button, 10);
							String s = button.getText();
							commonLibrary.clickButtonLogSmallWait(button, button.getText());
							flag2 = true;
							break;
						}
					}
					if (!flag2)
						report.updateTestLog("Select Copy Options Tab.", "Copy Options Tab is not present.", Status.FAIL);
					else {
						folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
						activeTabContent = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.activeTabContent, 10);
						WebElement createNewFolder = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createNewFolder, 10);
						commonLibrary.clickButtonLogSmallWait(createNewFolder, "Create New Folder");

						WebElement createFolderText = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderText, 10);
						commonLibrary.setDataInTextBox(createFolderText, folderName, "New Folder");

						WebElement createFolderButton = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 10);
						commonLibrary.clickButtonParentWithWait(createFolderButton, "Create");

						try {
							createFolderButton = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							WebElement createNew = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							int count = 0;
							do {
								commonLibrary.sleep(50000);
								count++;
								createNew = commonLibrary.isExistNegative(activeTabContent, UIMAP_WorkFolders.createFolderButton, 5);
							} while (createFolderButton.equals(createNew) && count < 40);
						} catch (Exception e) {
							commonLibrary.sleep(500000);
							System.out.println(e.toString());
						}

						folderDialog = commonLibrary.isExistNegative(UIMAP_WorkFolders.folderDialog1, 10);
						WebElement copyButton = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.copyButton, 10);
						commonLibrary.clickButtonParentWithWait(copyButton, copyButton.getText());

						try {
							copyButton = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.copyButton, 10);
							WebElement copyNew = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.copyButton, 10);
							int count = 0;
							do {
								commonLibrary.sleep(50000);
								count++;
								copyNew = commonLibrary.isExistNegative(folderDialog, UIMAP_WorkFolders.copyButton, 10);
							} while (copyButton.equals(copyNew) && count < 40);
						} catch (Exception e) {
							commonLibrary.sleep(500000);
							System.out.println(e.toString());
						}

					}
				}
			}
		} else
			report.updateTestLog("Verify Copy to Folder dialogbox is displayed", "Copy to Folder dialogbox is not displayed", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select an action item from Actions
	// dropdown
	// # Function Name : clickActionSelectValue     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickActionSelectValue(String strAction) {
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

			if (commonLibrary.selectFromListFolder(divAction, strAction)) {
				report.updateTestLog("Click option " + strAction + "", "" + strAction + " is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click search button from workfolders
	// page
	// # Function Name : clickSearchButton     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search clickSearchButton() {
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		// commonLibrary.highlightElement(eltSearchbutton);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify items count in a selected
	// folder
	// # Function Name : verifyFolderItemsCount     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyFolderItemsCount(String folderName, int folderCount) {
		boolean blnFlag = false;
		WebElement foldersList = commonLibrary.isExist(UIMAP_WorkFolders.myFolders);
		WebElement selectedFolder = commonLibrary.isExist(foldersList, UIMAP_WorkFolders.selectedFolder, 10);

		String expectedFolderCount = folderName + " (" + Integer.toString(folderCount) + ")";
		if (selectedFolder != null) {
			WebElement folderSelection = commonLibrary.isExist(selectedFolder, UIMAP_WorkFolders.selectedFolderLink, 10);
			if (folderSelection != null) {
				if (folderSelection.getText().equals(expectedFolderCount))
					blnFlag = true;
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify Item count for folder '" + folderName + "'", "Item count for folder is displayed as expected as '" + expectedFolderCount + "'", Status.PASS);
		else
			report.updateTestLog("Verify Item count for folder '" + folderName + "'", "Item count for folder is not displayed as expected as '" + expectedFolderCount + "'", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and verify item count is
	// displayed for a folder
	// # Function Name : clickFolderTitleVerifyItemCount     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickFolderTitleVerifyItemCount(String folderTitle, String count) {
		int j = 0;
		try {
			WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
			if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButton(myFoldersExpand);
			}
			WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 10);
			WebElement myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
			List<WebElement> folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
			for (j = 0; j < folder.size(); j++) {
				if (folder.get(j).getAttribute("data-title").contains(folderTitle)) {
					// commonLibrary.ScrollToView(folder.get(j));
					String folderText = folder.get(j).getAttribute("data-itemcount");
					if (folderText.equalsIgnoreCase("0")) {
						for (int i = 0; i <= 10; i++) {
							// driver.navigate().refresh();
							commonLibrary.sleep(5000);
							if (browsername.toLowerCase().contains("chrome")) {
								driver.navigate().refresh();
							} else {
								Actions builder = new Actions(driver);
								builder.sendKeys(Keys.F5).perform();
							}
							commonLibrary.sleep(5000);
							commonLibrary.sleep(3000);

							myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
							folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
							folderText = folder.get(j).getAttribute("data-itemcount");
							if (!(folderText.equalsIgnoreCase("0")))
								break;
						}

					}
					String[] titleSplit = folder.get(j).getText().split(folderTitle);
					if (titleSplit[1].trim().contains(count)) {
						report.updateTestLog("Verify " + count + " is displayed beside " + folderTitle, count + " is displayed beside " + folderTitle, Status.PASS);
					} else
						report.updateTestLog("Verify " + count + " is displayed beside " + folderTitle, count + " is not displayed beside " + folderTitle, Status.FAIL);

					if (browsername.contains("internet"))
						commonLibrary.clickJS(folder.get(j), folderTitle);
					else
						// folder.get(j).click();
						commonLibrary.clickButtonParentWithWait(folder.get(j), folderTitle);

					break;
				}
			}
			try {
				WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
				int counter = 0;
				do {
					commonLibrary.sleep(50000);
					counter++;
					currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
				} while (currProdOld.equals(currProdNew) && counter < 40);
			} catch (Exception e1) {
				commonLibrary.sleep(500000);
				System.out.println(e1.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to share the folder to a recipient
	// # Function Name : shareFolderToRecipient     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders shareFolderToRecipient(String text, String userName) {
		int i = 0;
		WebElement shareTextBox = commonLibrary.isExistNegative(UIMAP_WorkFolders.shareTextBox, 10);
		WebElement wordWheelContent = null;
		do {
			commonLibrary.setDataInTextBox(shareTextBox, text, "Enter user's name");
			try {
				commonLibrary.sleep(6000);
			} catch (Exception e) {
				throw new FrameworkException(e.toString());
			}
			wordWheelContent = commonLibrary.isExistNegative(UIMAP_WorkFolders.wordWheelContent, 10);
			i++;
		} while (wordWheelContent == null && i < 3);
		try {
			List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
			// WebElement options =
			// commonLibrary.isExist_Negative(wordWheelOptions.get(0),
			// UIMAP_SearchResult.lstTagListItems, 10);

			commonLibrary.selectFromListFolderNoParent(wordWheelOptions.get(0), userName);
		} catch (Exception e) {
			System.out.println(e.toString());
			wordWheelContent = commonLibrary.isExistNegative(UIMAP_WorkFolders.wordWheelContent, 10);
			List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
			commonLibrary.selectFromListFolderNoParent(wordWheelOptions.get(0), userName);
		}

		WebElement addToShare = commonLibrary.isExistNegative(UIMAP_WorkFolders.addToShare, 10);
		commonLibrary.clickButtonParentWithWait(addToShare, "Add to Share");

		WebElement save = commonLibrary.isExist(UIMAP_WorkFolders.save);
		if (save != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWait(save, "Save");
			} else {
				commonLibrary.clickButtonParentWithWait(save, "Save");
			}

		} else {
			report.updateTestLog("Click on Save", "Save button is not clicked", Status.FAIL);
		}

		save = commonLibrary.isExistNegative(UIMAP_WorkFolders.save, 4);
		WebElement saveNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.save, 4);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(500000);
				saveNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.save, 4);
				count++;
			} while (save.equals(saveNew) && count < 40);
		} catch (Exception e) {
			commonLibrary.sleep(5000000);
			System.out.println(e.toString());

		}
		WebElement cancel = commonLibrary.isExistNegative(UIMAP_WorkFolders.Cancel, 3);
		if (cancel != null) {
			report.updateTestLog("Verify item is successfully shared", "Item is not successfully shared", Status.FAIL);
		} else {
			report.updateTestLog("Verify item is successfully shared", "Item is successfully shared", Status.PASS);
		}
		commonLibrary.sleep(45000);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to rename the folder
	// # Function Name : renameFolder     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders renameFolder(String name) {
		WebElement renameTextBox = commonLibrary.isExist(UIMAP_WorkFolders.renameTextBox);
		WebElement rename = commonLibrary.isExist(UIMAP_WorkFolders.rename);
		if (renameTextBox != null) {
			commonLibrary.setDataInTextBox(renameTextBox, name, "NewFolderName");
			if (rename != null)
				commonLibrary.clickButtonParentWithWait(rename, "Rename");
			else
				report.updateTestLog("Click on Rename button", "Rename button is not available", Status.FAIL);
		} else {
			report.updateTestLog("Entering new folder name", "Rename text box is not available", Status.FAIL);
		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within all folders
	// # Function Name : searchWithinAllFolders     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders searchWithinAllFolders(String searchTerm) {
		boolean flag = false;
		String searchResult;
		int resultCount;
		WebElement searchTextBox = commonLibrary.isExist(UIMAP_WorkFolders.searchTextBox);
		WebElement searchButton = commonLibrary.isExist(UIMAP_WorkFolders.searchButton);

		if (searchTextBox != null) {
			commonLibrary.setDataInTextBox(searchTextBox, searchTerm, searchTerm);
			if (searchButton != null) {
				commonLibrary.clickButtonParentWithWait(searchButton, "SearchButton");
				flag = true;
			} else {
				report.updateTestLog("click on search button", "search button is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Entering text in search textbox", "search textbox is not available", Status.FAIL);
		}

		commonLibrary.sleep(5000);

		if (flag) {
			WebElement resultHeader = commonLibrary.isExist(UIMAP_WorkFolders.resultHeader);
			WebElement result = commonLibrary.isExist(resultHeader, UIMAP_WorkFolders.result, 10);
			if (result != null) {
				searchResult = result.getText().replace(searchTerm, "");
				searchResult = searchResult.replace("(", "");
				searchResult = searchResult.replace(")", "");
				resultCount = Integer.parseInt(searchResult);
				if (resultCount > 0) {
					report.updateTestLog("verify resultcount for searchterm", "The number of results for " + searchTerm + " is greater than zero", Status.PASS);
				} else {
					report.updateTestLog("verify resultcount for searchterm", "The number of results for " + searchTerm + " is zero", Status.FAIL);
				}
			} else {
				report.updateTestLog("verify result header", "Result is not displayed", Status.FAIL);
			}

		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Clear Search
	// # Function Name : verifyClearSearch     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyClearSearch() {
		WebElement clearSearch = commonLibrary.isExist(UIMAP_WorkFolders.clearSearch);
		if (clearSearch != null) {
			report.updateTestLog("Verify clearsearch link displays", "ClearSearch link is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify clearsearch link displays", "ClearSearch link is not displayed", Status.FAIL);
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on ClearSearchLink
	// # Function Name : clickClearSearchLink     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickClearSearchLink() {
		WebElement clearSearch = commonLibrary.isExist(UIMAP_WorkFolders.clearSearch);
		if (clearSearch != null) {
			commonLibrary.clickLinkWithWait(clearSearch, "clearsearch");
			report.updateTestLog("Verify clearsearch link displays", "ClearSearch link is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify clearsearch link displays", "ClearSearch link is not displayed", Status.FAIL);
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether Search TextBox Is
	// Empty
	// # Function Name : verifySearchTextBoxIsEmpty     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders verifySearchTextBoxIsEmpty() {
		WebElement searchTextBox = commonLibrary.isExist(UIMAP_WorkFolders.searchTextBox);
		if (searchTextBox != null) {
			if (searchTextBox.getAttribute("value").equals("")) {
				report.updateTestLog("verify search text box is empty", "The search text box is empty", Status.PASS);
			} else {
				report.updateTestLog("verify search text box is empty", "The search text box is not empty", Status.FAIL);
			}
		} else {
			report.updateTestLog("verify search text box is empty", "search text box is not available", Status.FAIL);
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Download button and
	// default/current settings
	// # Function Name : clickDownloadDefaultSettings    
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickDownloadDefaultSettings(String AutoITPath, String fileName, String filePath, boolean verifyContent, String PDFText, String TextExistence) {
		generalFunctions.clickDownloadDefaultSettings(AutoITPath, fileName, filePath, verifyContent, PDFText, TextExistence, "workfolders");
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within all folders for zero
	// results
	// # Function Name : searchWithinAllFoldersNegative     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders searchWithinAllFoldersNegative(String searchTerm) {
		boolean flag = false;
		String searchResult;
		int resultCount;
		WebElement searchTextBox = commonLibrary.isExist(UIMAP_WorkFolders.searchTextBox);
		WebElement searchButton = commonLibrary.isExist(UIMAP_WorkFolders.searchButton);

		if (searchTextBox != null) {
			commonLibrary.setDataInTextBox(searchTextBox, searchTerm, searchTerm);
			if (searchButton != null) {
				commonLibrary.clickButtonParentWithWait(searchButton, "SearchButton");
				flag = true;
			} else {
				report.updateTestLog("click on search button", "search button is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Entering text in search textbox", "search textbox is not available", Status.FAIL);
		}

		commonLibrary.sleep(5000);

		if (flag) {
			WebElement resultHeader = commonLibrary.isExist(UIMAP_WorkFolders.resultHeader);
			WebElement result = commonLibrary.isExist(resultHeader, UIMAP_WorkFolders.result, 10);
			if (result != null) {
				searchResult = result.getText().replace(searchTerm, "");
				searchResult = searchResult.replace("(", "");
				searchResult = searchResult.replace(")", "");
				resultCount = Integer.parseInt(searchResult);
				if (resultCount > 0) {
					report.updateTestLog("verify resultcount for searchterm", "The number of results for " + searchTerm + " is greater than zero", Status.FAIL);
				} else {
					report.updateTestLog("verify resultcount for searchterm", "The number of results for " + searchTerm + " is zero", Status.PASS);
				}
			} else {
				report.updateTestLog("verify result header", "Result is not displayed", Status.FAIL);
			}

		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete folder from Workfolders page
	// # Function Name : searchWithinAllFoldersNegative     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders deleteFolder(String folderName) {

		this.clickActionSelectValue("Delete folder");
		// above line will click the Delete folder option from the actions
		// dropdown

		// WebElement
		// deleteDialogHeader=commonLibrary.isExist(UIMAP_WorkFolders.dialogDelete);
		// if (deleteDialogHeader != null)
		// {
		WebElement btnDelete = commonLibrary.isExist(UIMAP_WorkFolders.btnDelete, 10);
		if (btnDelete != null) {
			commonLibrary.clickButtonParentWithWait(btnDelete, "Delete folder");
		}
		// }
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to send Email
	// # Function Name : sendEmail     
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders sendEmail(String Email) {
		// WebElement btnEmail =
		// commonLibrary.isExist(UIMAP_SearchResult.btnEmail);
		// commonLibrary.clickButton_Log_SmallWait(btnEmail, "E-mail");
		generalFunctions.clickDeliverySelectOption("delivery", "email");

		WebElement txtEmail = commonLibrary.isExistNegative(UIMAP_SearchResult.txtEmail, 10);
		commonLibrary.setDataInTextBox(txtEmail, Email, "To");

		WebElement btnSendEmail = commonLibrary.isExistNegative(UIMAP_SearchResult.btnSendEmail, 10);
		commonLibrary.clickButtonLogSmallWait(btnSendEmail, "Send Email");

		WebElement eltEmailPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.eltEmailPopup, 10);
		if (eltEmailPopup == null)
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box closes.", Status.PASS);
		else
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box does not close.", Status.FAIL);
		commonLibrary.switchToWindow("deliverysecondarywindow");
		WebElement txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
		if (txtPopUpHeader != null)
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up opens.", Status.PASS);
		else
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up does not open.", Status.FAIL);
		int i = 0;
		while (txtPopUpHeader.getText().contains("Processing")) {
			i++;
			try {
				commonLibrary.sleep(5000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
			if (i > 10)
				break;
		}
		boolean blnFlag = false;
		if (txtPopUpHeader.getText().contains("Complete"))
			blnFlag = true;
		else
			blnFlag = false;

		if (blnFlag)
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has been sent to: " + Email + "", Status.PASS);
		else
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has not been sent to: " + Email + "", Status.FAIL);

		driver.close();
		commonLibrary.switchToWindow("workfolder");
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on ResearchMap in history tab
	// # Function Name : clickResearchMap     
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ResearchMap clickResearchMap() {
		try {
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");

			WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
			if (lnkTxtResearchMap != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtResearchMap, "ResearchMap");
				else
					commonLibrary.clickButtonParentWithWait(lnkTxtResearchMap, "ResearchMap");
			}
			commonLibrary.sleep(35000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Docs within a folder
	// # Function Name : selectDocLinksByTitle     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders selectDocLinksByTitle(String docTitle)

	{

		int i;
		boolean isTitle = false;

		String DocTitle = null;

		WebElement parent = commonLibrary.isExist(UIMAP_WorkFolders.folderContents, 10);

		List<WebElement> OList = commonLibrary.isExistList(parent, By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_WorkFolders.titleWf, 10);
			DocTitle = lnkTitle.getText();
			if (DocTitle.trim().contains(docTitle.trim())) {
				WebElement checkbox = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.chkbox, 10);
				commonLibrary.setCheckBox(checkbox, docTitle);
				isTitle = true;
				break;
			}

		}
		if (!isTitle)
			report.updateTestLog("Select document with title: " + docTitle + " is displayed", "Document with title: " + docTitle + " is not displayed", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle1     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders clickDocumentTitle1(String title) {
		generalFunctions.clickPdfTitle(title);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for some verification points under a
	// title in work folder page
	// # Function Name : verifyTypeClientLastmodified
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyTypeClientLastmodified(String title) {

		try {
			int i;
			boolean isTitle = false;
			boolean isDoc = false;
			boolean isClient = false;
			boolean isLastEdited = false;
			String DocTitle = null;

			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (i = 0; i < LList.size(); i++) {
				WebElement titles = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.pdflink, 10);
				DocTitle = titles.getText();
				if (DocTitle.trim().contains(title.trim())) {
					isTitle = true;

					List<WebElement> clientVal = commonLibrary.isExistList(LList.get(i), UIMAP_SearchResult.dd, 10);
					List<WebElement> clientHeading = commonLibrary.isExistList(LList.get(i), UIMAP_SearchResult.dt, 10);
					for (int j = 0; j < clientHeading.size(); j++) {
						if (clientHeading.get(j).getText().equalsIgnoreCase("Type")) {
							isDoc = true;
						} else if (clientHeading.get(j).getText().equalsIgnoreCase("Client")) {
							isClient = true;
						} else if (clientHeading.get(j).getText().equalsIgnoreCase("Last Modified")) {
							WebElement modifiedDate = commonLibrary.isExist(clientVal.get(j), By.tagName("span"), 10);
							if (!modifiedDate.getText().equalsIgnoreCase("")) {
								isLastEdited = true;
							}
						}
					}
				}
				if (isTitle && isDoc && isClient && isLastEdited)
					break;

			}
			if (isTitle) {
				report.updateTestLog("Verify title " + title + " displays", "Title " + title + " is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify title " + title + " displays", "Title " + title + " is not displayed", Status.FAIL);
			}

			if (isDoc) {
				report.updateTestLog("Verify  Type column", "Type column is present", Status.PASS);
			} else {
				report.updateTestLog("Verify  Type column", "Type column is not present", Status.FAIL);
			}
			if (isClient) {
				report.updateTestLog("Verify Client column", "Client column is present", Status.PASS);
			} else {
				report.updateTestLog("Verify Client column", "Client column is not present", Status.FAIL);
			}
			if (isLastEdited) {
				report.updateTestLog("Verify Last Edited date is displayed", "Last Edited date is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Last Edited date is displayed", "Last Edited date is not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify student details is displayed
	// # Function Name : verifyStudentDetail
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyStudentDetail(String page, String path, int count, String text) {
		try {
			if (driver.getWindowHandles().size() > 1) {
				if (commonLibrary.switchToWindow(page)) {

					report.updateTestLog("Verify status tracker is displayed in expanded view", "Status tracker is displayed in expanded view", Status.PASS);

				} else {
					report.updateTestLog("Verify status tracker is displayed in expanded view", "Status tracker is not displayed in expanded view", Status.FAIL);
				}
				driver.close();
				commonLibrary.sleep(100000);
				commonLibrary.switchToWindow("workfolders");
			} else {
				int currentCount = new File(path).listFiles().length;
				if (currentCount > count) {
					report.updateTestLog("Verify status tracker is displayed in expanded view", "Status tracker is displayed in expanded view", Status.PASS);

				} else {
					report.updateTestLog("Verify status tracker is displayed in expanded view", "Status tracker is not displayed in expanded view", Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify student details is displayed
	// # Function Name : verifyProblemPage
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyProblemPage(String page) {
		try {
			if (commonLibrary.switchToWindow(page)) {
				report.updateTestLog("Verify often missed problem page is displayed", "Often missed problem page is displayed", Status.PASS);

			} else {
				report.updateTestLog("Verify often missed problem page is displayed", "Often missed problem page is not displayed", Status.FAIL);
			}
			driver.close();
			commonLibrary.switchToWindow("workfolders");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify summary of all exercise
	// details is displayed
	// # Function Name : verifySummaryOfExercise
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders verifySummaryOfExercise(String page) {
		try {
			if (commonLibrary.switchToWindow(page)) {
				report.updateTestLog("Verify summary of all exercises is displayed", "Summary of all exercises is displayed", Status.PASS);

			} else {
				report.updateTestLog("Verify summary of all exercises is displayed", "Summary of all exercises is not displayed", Status.FAIL);
			}
			driver.close();
			commonLibrary.switchToWindow("workfolders");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis® Interactive
	// Citation Workstation
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigatetoICW     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #************************************************************************************************
	public Interactivecitationworkstation navigatetoICW() {
		generalFunctions.navigatetoICW();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify completion of exercise
	// # Function Name : verifyCompletion
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyCompletion(String page) {
		try {
			if (commonLibrary.switchToWindow(page)) {
				report.updateTestLog("Verify completion certificate of exercise is displayed", "Completion certificate of exercise is displayed", Status.PASS);

			} else {
				report.updateTestLog("Verify completion certificate of exercise is displayed", "Completion certificate of exercise is not displayed", Status.FAIL);
			}
			driver.close();
			commonLibrary.switchToWindow("workfolders");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click shared by others
	// # Function Name : clickSharedByOthers
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders clickSharedByOthers() {
		try {
			WebElement shared = commonLibrary.isExist(UIMAP_WorkFolders.shared);
			if (shared != null) {
				commonLibrary.clickJS(shared, "Shared By Others");
			} else {
				report.updateTestLog("Click on Shared by others", "Shared by others link is not present", Status.PASS);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the View PDF link
	// # Function Name : clickViewPDFAttachment     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clickViewPDFAttachment_Secondary() {

		WebElement resultlink = commonLibrary.isExist(UIMAP_WorkFolders.pdfLink, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			commonLibrary.switchToWindow("documentId");
		}

		else if (browsername.contains("firefox")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentId");

		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Click to view PDF Document");
		}

		// return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders pdfVerificationSecondary(String pdfName, String Exist) {

		Boolean blnFlag = false;
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.pdfData, 10);
		if (docContent.getText().contains(pdfName))
			blnFlag = true;
		else
			blnFlag = false;

		switch (Exist) {
		case "Exist": {
			if (blnFlag) {
				report.updateTestLog("'" + pdfName + "' is present in th ePDF document", "'" + pdfName + "' is present in the PDF document", Status.PASS);
			} else {
				report.updateTestLog("'" + pdfName + "' is present in th ePDF document", "'" + pdfName + "' is not present in the PDF document", Status.FAIL);
			}
			break;
		}
		case "NotExist": {
			if (!blnFlag) {
				report.updateTestLog("'" + pdfName + "' is present in th ePDF document", "'" + pdfName + "' is not present in the PDF document", Status.PASS);
			} else {
				report.updateTestLog("'" + pdfName + "' is present in th ePDF document", "'" + pdfName + "' is present in the PDF document", Status.FAIL);
			}
			break;
		}
		}

		driver.close();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commonLibrary.switchToWindow("wokfolder");
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the View PDF link
	// # Function Name : clickViewPDFAttachment     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickViewPDFAttachment() {

		WebElement resultlink = commonLibrary.isExist(UIMAP_WorkFolders.pdfLink, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentId");
		}

		else if (browsername.contains("firefox")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentId");
		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Click to view PDF Document");
		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders pdfVerification(String FilePath, String Text, String Exist) {
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
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Shared By Me
	// # Function Name : clickSharedByMe    
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders clickSharedByMe() {
		WebElement sharedByMe = commonLibrary.isExistNegative(UIMAP_WorkFolders.sharedByMe, 10);
		commonLibrary.clickButtonParentWithWait(sharedByMe, "Shared by me");
		commonLibrary.sleep(2000);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify Docs present in Folders page.
	// # Function Name : verifyDocsPresent    
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyDocsPresent(String docNames) {
		int count = 0;
		String[] docList = docNames.split(";");
		Boolean[] flag = new Boolean[docList.length];
		boolean allFlag = true;
		int pos = 0;
		WebElement nextPage = null;
		for (int i = 0; i < docList.length; i++) {
			flag[i] = false;
		}
		do {
			count++;
			WebElement resultClass = null;
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						for (int i = 0; i < docList.length; i++) {
							if (eleDocTitle.getText().toLowerCase().contains(docList[i].toLowerCase())) {
								report.updateTestLog("Verify the document " + docList[i] + " is present in WFP page.", "the document " + docList[i] + " is present in WFP page.", Status.PASS);
								flag[i] = true;
								break;

							}
						}

						allFlag = true;
						for (int i = 0; i < docList.length; i++) {
							if (!flag[i]) {
								pos = i;
								allFlag = false;
							}
						}
						if (allFlag)
							break;
					}

				}
			}
			if (!allFlag) {

				nextPage = commonLibrary.isExistNegative(UIMAP_WorkFolders.nextPage, 10);
				if (nextPage != null) {
					commonLibrary.clickLinkWithWebElementWithWait(nextPage, "Next Page");
					try {
						WebElement resultClassOld = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
						WebElement resultClassNew = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
						int counter = 0;
						do {
							commonLibrary.sleep(50000);
							counter++;
							resultClassNew = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
						} while (resultClassOld.equals(resultClassNew) && counter < 45);
					} catch (Exception e) {
						commonLibrary.sleep(1000000);
						System.out.println(e.toString());
					}
				}
			}
		} while (!allFlag && nextPage != null && count <= 6);

		if (!allFlag)
			report.updateTestLog("Verify the document " + docList[pos] + " is present in WFP page.", "the document " + docList[pos] + " is not present in WFP page.", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to sharing a folder.
	// # Function Name : stopSharing    
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders stopSharing() {
		generalFunctions.stopSharing();
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify Docs present in Folders page.
	// # Function Name : verifyDocsPresent    
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyDocsAbsent(String docNames) {
		int count = 0;
		String[] docList = docNames.split(";");

		boolean allFlag = true;
		// int pos = 0;
		WebElement nextPage = null;
		do {
			WebElement resultClass = null;
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						for (int i = 0; i < docList.length; i++) {
							if (eleDocTitle.getText().toLowerCase().contains(docList[i].toLowerCase())) {
								report.updateTestLog("Verify the document/folder " + docList[i] + " is not present in WFP page.", "The document " + docList[i] + " is present in WFP page.", Status.FAIL);
								allFlag = false;
							}
						}
						if (!allFlag)
							break;
					}
				}
			}
			count++;
			nextPage = commonLibrary.isExistNegative(UIMAP_WorkFolders.nextPage, 10);
			if (nextPage != null) {
				commonLibrary.clickLinkWithWebElementWithWait(nextPage, "Next Page");
				try {
					WebElement resultClassOld = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
					WebElement resultClassNew = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
					int counter = 0;
					do {
						commonLibrary.sleep(50000);
						counter++;
						resultClassNew = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
					} while (resultClassOld.equals(resultClassNew) && counter < 45);
				} catch (Exception e) {
					commonLibrary.sleep(1000000);
					System.out.println(e.toString());
				}
			}
		} while (nextPage != null && count <= 6);

		if (allFlag)
			report.updateTestLog("Verify the documents/folders: " + docNames + " are absent in WFP page.", "The documents/folders: " + docNames + " are absent in WFP page.", Status.PASS);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Manage Sharing Information.
	// # Function Name : clickManageSharingOptions     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickManageSharingOptions() {
		WebElement manageShare = commonLibrary.isExistNegative(UIMAP_WorkFolders.manageShare, 10);
		commonLibrary.clickButtonParentWithWait(manageShare, "Manage Sharing Information");
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickDocTitle1(String title) {
		generalFunctions.clickDocLink(title);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Item count in Folder
	// # Function Name : verifyItemCount     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyItemCount(String folderTitle, String count) {
		boolean flag = false;
		int j = 0;
		try {
			WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
			if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButton(myFoldersExpand);
			}

			WebElement myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
			List<WebElement> folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
			for (j = 0; j < folder.size(); j++) {
				if (folder.get(j).getAttribute("data-title").toLowerCase().contains(folderTitle.toLowerCase())) {
					// commonLibrary.ScrollToView(folder.get(j));
					String folderText = folder.get(j).getAttribute("data-itemcount");
					if (folderText.equalsIgnoreCase("0")) {
						for (int i = 0; i <= 10; i++) {
							// driver.navigate().refresh();
							commonLibrary.sleep(5000);
							Actions builder = new Actions(driver);
							builder.sendKeys(Keys.F5).perform();
							commonLibrary.sleep(5000);
							commonLibrary.sleep(3000);

							myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
							folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
							folderText = folder.get(j).getAttribute("data-itemcount");
							if ((folderText.equalsIgnoreCase(count))) {
								report.updateTestLog("Verify Saved Items Count matches with " + count, "Saved Items count" + folderText + " is matching with " + count, Status.PASS);
								flag = true;
								break;
							}

						}

					}
					if (flag)
						break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify Saved Items Count matches with " + count, "Saved Items count is not matching with " + count, Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickFirstDocLink() {
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
			/*
			 * else { WebElement pgHeader=null;
			 * 
			 * if(commonLibrary.isExist_Negative(UIMAP_SearchResult.TitleClassTOC
			 * , 10)!=null)
			 * pgHeader=commonLibrary.isExist_Negative(UIMAP_SearchResult
			 * .TitleClassTOC, 10); else
			 * if(commonLibrary.isExist_Negative(UIMAP_SearchResult
			 * .pgClassHeaderOption3, 10)!=null)
			 * pgHeader=commonLibrary.isExist_Negative
			 * (UIMAP_SearchResult.pgClassHeaderOption3, 10);
			 * 
			 * 
			 * if(pgHeader!=null &&
			 * (pgHeader.getText().toLowerCase().contains("document") &&
			 * pgHeader
			 * .getText().contains(strDocTitle)||pgHeader.getText().contains
			 * ("Expert Witness"))) report.updateTestLog("Verify document "+
			 * strDocTitle+" is displayed", pgHeader.getText()+"/"+"document "+
			 * strDocTitle+" is displayed", Status.PASS); else
			 * report.updateTestLog("Verify document "+
			 * strDocTitle+" is displayed", "document "+
			 * strDocTitle+" is displayed", Status.FAIL); }
			 */
			return new Document(scriptHelper);
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Count
	// # Function Name : verifyCount     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyCount(String count) {
		commonLibrary.sleep(60000);
		// JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean flag = false;
		WebElement foldersTree = commonLibrary.isExistNegative(UIMAP_WorkFolders.foldersTreeCont, 10);
		List<WebElement> list = commonLibrary.isExistList(foldersTree, UIMAP_WorkFolders.div, 10);
		for (WebElement item : list) {
			if (item.getText().toLowerCase().contains("items saved")) {
				flag = true;
				commonLibrary.scrollToView(item);
				// executor.executeScript("arguments[0].focus();", item);
				String itemCount = item.getText().split(":")[1].trim();
				if (count.equals(itemCount)) {
					report.updateTestLog("Verify Saved Items Count displayed below folder tree is " + count, "Saved Items Count displayed below folder tree is " + count, Status.PASS);
				} else {
					boolean flag2 = false;
					int repeat = 0;
					do {

						if (browsername.toLowerCase().contains("internet")) {
							Actions builder = new Actions(driver);
							builder.sendKeys(Keys.F5).perform();
						} else {
							driver.navigate().refresh();
						}
						try {
							Thread.sleep(10000);
						} catch (Exception e) {
							System.out.println(e.toString());
						}
						foldersTree = commonLibrary.isExistNegative(UIMAP_WorkFolders.foldersTreeCont, 10);
						list = commonLibrary.isExistList(foldersTree, UIMAP_WorkFolders.div, 10);
						for (WebElement item1 : list) {
							if (item1.getText().toLowerCase().contains("items saved")) {
								// executor.executeScript("arguments[0].focus();",
								// item);
								commonLibrary.scrollToView(item1);
								String itemCount1 = item1.getText().split(":")[1].trim();
								if (count.equals(itemCount1)) {
									flag2 = true;
									flag = true;
									report.updateTestLog("Verify Saved Items Count displayed below folder tree is " + count, "Saved Items Count displayed below folder tree is " + count, Status.PASS);
								}

							}
						}
						repeat++;

					} while (!flag2 && repeat < 4);

					if (!flag2)
						report.updateTestLog("Verify Saved Items Count displayed below folder tree is " + count, "Saved Items Count displayed below folder tree is not " + count, Status.FAIL);
				}
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify Saved Items Count displayed below folder tree is " + count, "Saved Items Count is not displayed", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Pratik
	// # Date Created : June'2015
	// #*****************************************************************************************************************************

	public Research clickProductlogo() {
		generalFunctions.clickProductLogo();
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Folder Title
	// # Function Name : clickFolderTitle1     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public WorkFolders clickFolderTitle1(String title) {
		generalFunctions.clickFolderLink1(title);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click search the terms link
	// # Function Name : clickSearchTheTerms     
	// # Author : Seetha
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public WorkFolders clickSearchTheTerms(String title) {
		{
			List<WebElement> list = commonLibrary.isExistList(UIMAP_WorkFolders.links, 10);
			for (WebElement item : list) {
				if (item.getText().toLowerCase().contains(title.toLowerCase())) {
					commonLibrary.clickJS(item, title);
					break;
				}
			}

		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Selected Items Alone
	// Displayed in Folder
	// # Function Name : verifySelectedItemsAloneDisplayed     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public WorkFolders verifySelectedItemsAloneDisplayed(String docTitle, String docTitle1) {
		boolean isTitle = false;
		boolean isTitle1 = false;
		boolean flag = false;

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		if (LList.size() == 2)
			flag = true;
		for (WebElement document : LList) {
			WebElement lnkTitle = commonLibrary.isExist(document, UIMAP_SearchResult.titleWf, 10);
			DocTitle = lnkTitle.getText();
			if (DocTitle.trim().contains(docTitle.trim())) {
				isTitle = true;
			} else if (DocTitle.trim().contains(docTitle1.trim())) {
				isTitle1 = true;
			}
		}
		if (isTitle && isTitle1 && flag)
			report.updateTestLog("Verify Selected Items Alone Displayed in Folder", "Selected Items Alone is Displayed in Folder", Status.PASS);
		else
			report.updateTestLog("Verify Selected Items Alone Displayed in Folder", "Selected Items Alone is not Displayed in Folder", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Lasac checkbox is present.
	// # Function Name : verifyLASACCheckBox
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyLASACCheckBox(String checkBoxText) {

		WebElement checkBoxCont = commonLibrary.isExistNegative(UIMAP_SearchResult.checkBoxCont1, 10);
		WebElement chkbox = commonLibrary.isExistNegative(checkBoxCont, UIMAP_SearchResult.chkbox, 10);
		if (checkBoxCont.getText().toLowerCase().contains(checkBoxText.toLowerCase()) && chkbox != null) {
			report.updateTestLog("Veirfy Checkbox is present with following message: " + checkBoxText, "Checkbox is present with following message: " + checkBoxText, Status.PASS);
		} else
			report.updateTestLog("Veirfy Checkbox is present with following message: " + checkBoxText, "Checkbox is not present with following message: " + checkBoxText, Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set or reset LASAC checkbox and
	// click Agree and Continue
	// # Function Name : setLASACCheckBoxContinue
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search setLASACCheckBoxContinue(boolean status) {
		WebElement checkBoxCont = commonLibrary.isExistNegative(UIMAP_SearchResult.checkBoxCont1, 10);
		WebElement chkbox = commonLibrary.isExistNegative(checkBoxCont, UIMAP_SearchResult.chkbox, 10);

		if (commonLibrary.setCheckBox(chkbox, status)) {
			report.updateTestLog("Set Lasac Checkbox " + status, "Lasac Checkbox is set to " + status, Status.PASS);
		} else
			report.updateTestLog("Set Lasac Checkbox " + status, "Lasac Checkbox is not set to " + status, Status.FAIL);

		WebElement btnAgreeAndContinue = commonLibrary.isExist(UIMAP_SearchResult.btnAgreeAndContinue);
		commonLibrary.clickLink(btnAgreeAndContinue, "Agree and Continue");

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify saved Items Alone Displayed
	// in Folder
	// # Function Name : verifySavedItemsAloneDisplayed_new   
	// # Author : Deepha H
	// # Date Created : Oct 27'15
	// #*****************************************************************************************************************************

	public WorkFolders verifySavedItemsAloneDisplayed_new(String docTitle) {

		boolean flag = false;
		String[] docTitleList = docTitle.split(";");

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);

		// Iterating all items available in folder and verifying the items
		for (WebElement document : LList) {

			WebElement lnkTitle = commonLibrary.isExist(document, UIMAP_SearchResult.titleWf, 10);
			System.out.print(lnkTitle.getText());
			DocTitle = lnkTitle.getText();
			for (int l = 0; l < docTitleList.length; l++) {
				if (DocTitle.trim().equals(docTitleList[l].trim())) {

					report.updateTestLog("Verify Selected Items" + DocTitle + "Alone Displayed in Folder", "Selected Items Alone is Displayed in Folder", Status.PASS);
					flag = true;
					break;

				}
			}

		}

		if (!flag)
			report.updateTestLog("Verify Selected Items Alone Displayed in Folder", "Selected Items Alone is not Displayed in Folder", Status.FAIL);

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify LASAC pop up is displayed.
	// # Function Name : verifyLASACPopup
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyLASACPopup(String popupText, boolean isPresent) {
		if (isPresent) {
			WebElement txtAccessChargeRed = commonLibrary.isExist(UIMAP_SearchResult.txtAccessChargeRed);
			WebElement lasacMessage = commonLibrary.isExistNegative(UIMAP_SearchResult.lasacMessage1, 10);

			if (txtAccessChargeRed.getText().toLowerCase().trim().contains("Lexis Advance Access Charge".toLowerCase().trim()) && lasacMessage.getText().toLowerCase().trim().contains(popupText.toLowerCase().trim())) {
				report.updateTestLog("Veirfy LASAC Access charge popup displays with message: " + popupText, "LASAC Access charge popup displays with message: " + popupText, Status.PASS);
			} else
				report.updateTestLog("Veirfy LASAC Access charge popup displays with message: " + popupText, "LASAC Access charge popup does not display with message: " + popupText, Status.FAIL);
		} else {
			WebElement txtAccessChargeRed = commonLibrary.isExist(UIMAP_SearchResult.txtAccessChargeRed);
			// WebElement lasacMessage = commonLibrary.isExist_Negative(
			// UIMAP_SearchResult.lasacMessage, 10);

			if (txtAccessChargeRed.getText().toLowerCase().contains("Lexis Advance Access Charge".toLowerCase())) {
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is displayed", Status.FAIL);
			} else
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is not displayed", Status.PASS);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Download in Download Popup
	// # Function Name : clickDownloadFromAction    
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders clickDownloadFromAction(String AutoITPath, String fileName, String filePath, boolean verifyContent, String PDFText, String TextExistence) {
		generalFunctions.clickDownloadFromAction(AutoITPath, fileName, filePath, verifyContent, PDFText, TextExistence, "workfolders");
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickDocTitle(String title) {
		generalFunctions.clickDocLink(title);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Selected Items Alone
	// Displayed in Folder
	// # Function Name : verifySelectedItemsAloneDisplayed_new   
	// # Author : Vennila
	// # Date Created : Oct 23'15
	// #*****************************************************************************************************************************

	public WorkFolders verifySelectedItemsAloneDisplayed_new(String docTitle) {

		boolean flag = false;
		String[] docTitleList = docTitle.split(":");

		String DocTitle = null;

		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);

		// Iterating all items available in folder and verifying the items
		for (WebElement document : LList) {

			WebElement lnkTitle = commonLibrary.isExist(document, UIMAP_SearchResult.titleWf, 10);
			DocTitle = lnkTitle.getText();
			for (int l = 0; l <= docTitleList.length; l++) {
				if (DocTitle.trim().equals(docTitleList[l].trim())) {

					report.updateTestLog("Verify Selected Items" + DocTitle + "Alone Displayed in Folder", "Selected Items Alone is Displayed in Folder", Status.PASS);
					flag = true;
					break;

				}
			}

		}

		if (!flag)
			report.updateTestLog("Verify Selected Items Alone Displayed in Folder", "Selected Items Alone is not Displayed in Folder", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select create new folder button
	// document.
	// # Function Name : ClickCreateNewFolder
	// # Author : Venilla
	// # Date Created : Oct '15
	// #*****************************************************************************************************************************

	public WorkFolders clickCreateNewFolder(String FolderName) {

		WebElement createbutton = commonLibrary.isExist(UIMAP_WorkFolders.createbutton, 10);

		if (createbutton != null) {
			commonLibrary.clickJS(createbutton, "createbutton");
		}

		else
			report.updateTestLog("Clicking on create button", "create button not found", Status.FAIL);

		WebElement inputtext = commonLibrary.isExist(UIMAP_WorkFolders.newFolderText, 10);

		if (inputtext != null) {
			commonLibrary.setDataInTextBoxWithLog(UIMAP_WorkFolders.newFolderText, FolderName);
			commonLibrary.sleep(30000);
			WebElement createFolder = commonLibrary.isExist(UIMAP_WorkFolders.createFolder, 10);

			if (createFolder != null) {
				commonLibrary.clickJS(createFolder, "Create");
			}

			else

				report.updateTestLog("Clicking on create button", "create button not found", Status.WARNING);

		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Vennila
	// # Date Created : 21 sep 16
	// #*****************************************************************************************************************************

	public WorkFolders verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink2     
	// # Author :
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Document clickDocLink2(String strDocTitle) {
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
							if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
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

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Title
	// # Function Name : clickDocumentTitle     
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickDocTitleNew(String title) {
		generalFunctions.clickDocLink1(title);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the details section in body
	// full view WF page
	// # Function Name : verifyPropertiesUnderBodySection_new     
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyPropertiesUnderBodySection_new(String Title, String URL) {
		try {
			WebElement sectioncontent = commonLibrary.isExistNegative(UIMAP_IMContent.sectioncontent, 10);

			if (sectioncontent != null) {

				String[] arr = sectioncontent.getText().split("\\n");

				String Newtitle = "Title:" + " " + Title;
				String NewURL = "Web URL:" + " " + URL;

				if (arr[0].equals(Newtitle.trim()) && arr[1].equals(NewURL.trim()))
					report.updateTestLog("Verify properties sections details", " Title and URL are displayed", Status.PASS);
				else
					report.updateTestLog("Verify properties sections details", "Title and  URL are not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new IntermediateContent(scriptHelper);
	}

	// #******************************************************************************************************************
	// # Function Description : Function to Click Folder in Folders page
	// # Function Name : clickOnFolderTittle    
	// # Author : Chaithanya
	// # Date Created : Nov'15
	// #******************************************************************************************************************
	public WorkFolders clickOnFolderTittle(String strFolderTitle) {

		Boolean blnFlag = false;
		WebElement folderClass = null;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_WorkFolders.folderClass, 10) != null)
				folderClass = commonLibrary.isExist(UIMAP_WorkFolders.folderClass, 10);

			if (folderClass != null) {
				WebElement OListResult = commonLibrary.isExist(folderClass, UIMAP_WorkFolders.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(folderClass, UIMAP_WorkFolders.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_WorkFolders.listtag, 20);
						for (WebElement folder : OListItems) {
							WebElement clkFolderTitle = commonLibrary.isExist(folder, UIMAP_WorkFolders.TitleClassFol, 2);
							if (clkFolderTitle != null && clkFolderTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strFolderTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
								WebElement lnkFolder = commonLibrary.isExist(clkFolderTitle, UIMAP_WorkFolders.atag, 20);
								if (lnkFolder != null) {
									// commonLibrary.ScrollToView(lnkDocument);
									commonLibrary.clickLinkWithWebElementWithWait(lnkFolder, lnkFolder.getText());
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

		}

		if (!blnFlag)

			report.updateTestLog("Click on the Folder " + strFolderTitle, "Not Clicked  on the Folder " + strFolderTitle, Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// # Function Description : Function to verify no items message in folders
	// page
	// # Function Name : verifyNoItemsMsg     
	// # Author :Vennila
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyNoItemsMsg(String msg) {

		WebElement resultClass = commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire1, 10);

		if (resultClass != null) {

			WebElement header = commonLibrary.isExist(resultClass, UIMAP_SearchResult.tagHeader1, 20);

			if (header != null) {
				if (header.getText().trim().toLowerCase().equals(msg.trim().toLowerCase()))
					report.updateTestLog("Verify No items message", " No items message is  displayed", Status.PASS);
			} else
				report.updateTestLog("Verify No items message", "No items message is  not displayed", Status.FAIL);
		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Back browser
	// # Function Name : clickBrowserBack  
	// # Author : Venilla
	// # Date Created : 19-Nov'15
	// #*********************************************************************************************************

	public Home clickBrowserBacktoHome() {
		commonLibrary.clickBrowserBack();

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PDF icon
	// # Function Name : verifyPdficon     
	// # Author :Venilla
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyPdficon() {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		// WebElement resultClass1 = null;
		driver.navigate().refresh();

		if (commonLibrary.isExistNegative(UIMAP_WorkFolders.folderContentsEntire1, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_WorkFolders.folderContentsEntire1, 10);

		// if (commonLibrary.isExistNegative(UIMAP_WorkFolders.folderContents,
		// 10) != null)
		// resultClass1 =
		// commonLibrary.isExist(UIMAP_WorkFolders.folderContents, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
			List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
			for (WebElement list : OListResult1) {
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null)

						{
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.image, 20);
							if (lnkDocument != null) {

								if (lnkDocument.getAttribute("title").contains("PDF"))
									// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
									// lnkDocument.getText());
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

			report.updateTestLog("Verifying PDF icon  ", "Pdf icon is  available ", Status.PASS);
		else
			report.updateTestLog("Verifying PDF icon  ", "Pdf icon is not available ", Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on WebURL link
	// # Function Name : closeAndSwitchTorpage     
	// # Author : Vennila
	// # Date Created : 27 Oct'15
	// #*****************************************************************************************************************************

	public WorkFolders closeAndSwitchTorpage() {

		commonLibrary.smallWait();
		// commonLibrary.switchToWindow(linkName);
		// driver.close();
		commonLibrary.switchToWindow("lexis.com/workfolders");
		report.updateTestLog("Switching to main Window", "Secondary window is displayed", Status.PASS);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on PDF Title
	// # Function Name : clickPDFTitle1     
	// # Author : Venilla
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public WorkFolders clickPDFTitle1(String title) {
		List<WebElement> lnkTitle = commonLibrary.isExistList(UIMAP_WorkFolders.pdfLink, 10);
		for (WebElement item : lnkTitle) {
			if (item.getText().equalsIgnoreCase(title)) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(item, title);
				else
					commonLibrary.clickButtonParentWithWait(item, title);
				break;
			}
		}
		return new WorkFolders(scriptHelper);
	}

	// #******************************************************************************************************************
	// # Function Description : Function to Click Folder in Folders pane
	// # Function Name : clickOnFolderTittle    
	// # Author : Vennila
	// # Date Created : Nov'15
	// #******************************************************************************************************************

	public WorkFolders clickFolderFromPane(String folderTitle) {
		int j = 0;

		try {
			WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
			if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButton(myFoldersExpand);
			}

			WebElement myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
			List<WebElement> folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
			for (j = 0; j < folder.size(); j++) {
				if (folder.get(j).getAttribute("data-title").toLowerCase().contains(folderTitle.toLowerCase())) {

					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWait(folder.get(j), folderTitle);
					else
						// folder.get(j).click();
						commonLibrary.clickButtonParentWithWait(folder.get(j), folderTitle);
					commonLibrary.sleep(10000);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #******************************************************************************************************************
	// # Function Description : Function to expand & Select Folder in Copy
	// Folder Popup
	// # Function Name : expandFolderFromCopy    
	// # Author : Vennila
	// # Date Created : Nov'15
	// #******************************************************************************************************************

	public WorkFolders expandFolderFromCopy(String folderTitle) {
		Boolean blnFlag = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < 5; i++) {
			WebElement copyPopup = commonLibrary.isExist(UIMAP_WorkFolders.copyFolder, 10);
			if (copyPopup != null) {

				WebElement copyForm = commonLibrary.isExist(copyPopup, UIMAP_WorkFolders.copyFolderTree, 10);
				if (copyForm != null) {
					WebElement copyDiv = commonLibrary.isExist(copyForm, UIMAP_WorkFolders.copyFolderDiv, 10);
					if (copyDiv != null) {

						WebElement ulResults = commonLibrary.isExist(copyDiv, UIMAP_WorkFolders.ulTag1, 20);
						List<WebElement> ulListResults = commonLibrary.isExistList(ulResults, UIMAP_WorkFolders.ulTag2, 20);
						for (WebElement list : ulListResults) {
							if (list != null) {

								List<WebElement> folder = commonLibrary.isExistList(list, UIMAP_WorkFolders.listtag, 20);
								for (j = 0; j < folder.size(); j++) {

									if (folder.get(j).getText().toLowerCase().contains(folderTitle.toLowerCase())) {
										WebElement button = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.button1, 20);
										WebElement anchortag = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.atag, 20);
										if (button != null && anchortag != null) {
											commonLibrary.clickButton(button);
											report.updateTestLog("Expand the folder" + folderTitle, folderTitle + "Folder expanded", Status.PASS);

											WebElement clkFolderTitle = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.clkSubFolder, 2);
											if (clkFolderTitle != null) {
												commonLibrary.clickLinkWithWebElementWithWait(clkFolderTitle, clkFolderTitle.getText());
												{
													report.updateTestLog("Click on SubFolder" + clkFolderTitle.getText(), clkFolderTitle.getText() + "subfolder is clicked", Status.PASS);
													WebElement clkCopyButton = commonLibrary.isExist(UIMAP_WorkFolders.clkOkButton, 2);
													if (clkCopyButton != null) {
														commonLibrary.clickButton(clkCopyButton);
														report.updateTestLog("Click on Copy button", "Clicked on Copy button", Status.PASS);
													} else
														report.updateTestLog("Click on Copy button", "Copy button not available", Status.FAIL);
													blnFlag = true;
													break;

												}
											}
										}
									}
								}
							}
						}
					}
					if (blnFlag)
						break;
				}
			}
		}

		if (!blnFlag)

			report.updateTestLog("Copy the Folder from Copy Popup", "Folder not copied", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #******************************************************************************************************************
	// # Function Description : Function to expand Folder in Folders pane
	// # Function Name : expandFolderFromPane    
	// # Author : Vennila
	// # Date Created : Nov'15
	// #******************************************************************************************************************

	public WorkFolders expandFolderFromPane(String folderName) {

		Boolean blnFlag = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < 5; i++) {
			WebElement asideClass = commonLibrary.isExist(UIMAP_WorkFolders.asideTag, 10);
			if (asideClass != null) {

				WebElement divTag = commonLibrary.isExist(asideClass, UIMAP_WorkFolders.folderDiv, 10);
				if (divTag != null) {

					WebElement ulFolder = commonLibrary.isExist(divTag, UIMAP_WorkFolders.folderUl, 10);
					if (ulFolder != null) {

						WebElement folderList = commonLibrary.isExist(ulFolder, UIMAP_WorkFolders.myFolders, 10);
						if (folderList != null) {

							WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
							if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
								commonLibrary.clickButton(myFoldersExpand);
							}
							WebElement ulTag = commonLibrary.isExist(folderList, UIMAP_WorkFolders.ulTagMyFolder, 10);
							if (ulTag != null) {

								List<WebElement> folder = commonLibrary.isExistList(ulTag, UIMAP_WorkFolders.listtag, 20);
								for (j = 0; j < folder.size(); j++) {

									if (folder.get(j).getText().toLowerCase().contains(folderName.toLowerCase())) {
										WebElement button = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.button1, 20);
										WebElement anchortag = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.atag, 20);
										if (button != null && anchortag != null && button.getAttribute("class").contains("collapsed")) {
											commonLibrary.clickButton(button);
											report.updateTestLog("Expand the Folder " + folderName, "Folder" + folderName + "is Expanded", Status.PASS);

											blnFlag = true;
											break;
										}
									}
								}

							}
						}
					}
				}
			}
			if (blnFlag)
				break;
		}
		if (!blnFlag)
			report.updateTestLog("Expand the Folder " + folderName, "Folder" + folderName + "not Expanded", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Folder Displayed
	// # Function Name : VerifyFolderDisplayed     
	// # Author : Vennila
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyFolderDisplayed(String folderName) {
		Boolean blnFlag = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < 5; i++) {
			WebElement asideClass = commonLibrary.isExist(UIMAP_WorkFolders.asideTag, 10);
			if (asideClass != null) {

				WebElement divTag = commonLibrary.isExist(asideClass, UIMAP_WorkFolders.folderDiv, 10);
				if (divTag != null) {

					WebElement ulFolder = commonLibrary.isExist(divTag, UIMAP_WorkFolders.folderUl, 10);
					if (ulFolder != null) {

						WebElement folderList = commonLibrary.isExist(ulFolder, UIMAP_WorkFolders.myFolders, 10);
						if (folderList != null) {

							WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
							if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
								commonLibrary.clickButton(myFoldersExpand);
							}
							WebElement ulTag = commonLibrary.isExist(folderList, UIMAP_WorkFolders.ulTagMyFolder, 10);
							if (ulTag != null) {

								List<WebElement> folder = commonLibrary.isExistList(ulTag, UIMAP_WorkFolders.listtag, 20);
								for (j = 0; j < folder.size(); j++) {

									if (folder.get(j).getText().toLowerCase().contains(folderName.toLowerCase())) {
										WebElement aFolderTag = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.atag, 20);
										if (aFolderTag != null) {
											report.updateTestLog("Verify Folder : " + folderName + " is displayed in Left Pane", "Folder : " + folderName + " is displayed in Left Pane", Status.PASS);

										}
										blnFlag = true;
										break;
									}
								}
							}
						}

					}
				}
			}
			if (blnFlag)
				break;
		}
		if (!blnFlag)
			report.updateTestLog("Verify Folder : " + folderName + " is displayed in Left Pane ", "Folder : " + folderName + " is NOT displayed in Left Pane", Status.FAIL);

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Folder has expand symbol
	// # Function Name : verifyFolderHasExpandSymbol     
	// # Author : Vennila
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyFolderHasExpandSymbol(String folderTitle) {
		Boolean blnFlag = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < 5; i++) {
			WebElement asideClass = commonLibrary.isExist(UIMAP_WorkFolders.asideTag, 10);
			if (asideClass != null) {
				System.out.print("a");
				WebElement divTag = commonLibrary.isExist(asideClass, UIMAP_WorkFolders.folderDiv, 10);
				if (divTag != null) {
					System.out.print("b");
					WebElement ulFolder = commonLibrary.isExist(divTag, UIMAP_WorkFolders.folderUl, 10);
					if (ulFolder != null) {
						System.out.print("c");
						WebElement folderList = commonLibrary.isExist(ulFolder, UIMAP_WorkFolders.myFolders, 10);
						if (folderList != null) {
							System.out.print("d");
							WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
							if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
								commonLibrary.clickButton(myFoldersExpand);
							}
							WebElement ulTag = commonLibrary.isExist(folderList, UIMAP_WorkFolders.ulTagMyFolder, 10);
							if (ulTag != null) {
								System.out.print("e");
								List<WebElement> folder = commonLibrary.isExistList(ulTag, UIMAP_WorkFolders.listtag, 20);
								for (j = 0; j < folder.size(); j++) {
									System.out.print("f");
									if (folder.get(j).getText().toLowerCase().contains(folderTitle.toLowerCase())) {
										WebElement button = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.button1, 20);
										WebElement anchortag = commonLibrary.isExist(folder.get(j), UIMAP_WorkFolders.atag, 20);
										if (button != null && anchortag != null) {
											report.updateTestLog("Verify Expand symbol displayed before folder " + folderTitle, "Expand symbol is displayed before Folder" + folderTitle, Status.PASS);
											System.out.print("g");
											blnFlag = true;
											break;
										}
									}
								}

							}
						}
					}
				}
			}
			if (blnFlag)
				break;
		}
		if (!blnFlag)
			report.updateTestLog("Verify Expand symbol displayed before folder " + folderTitle, "Expand symbol is NOT displayed before Folder" + folderTitle, Status.FAIL);

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message from Copy
	// folder.
	// # Function Name : verifyBannerMessageOnCopy
	// # Author : Chaithanya
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyBannerMessageOnCopy(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg, 20);
		if (bannermsg != null && bannermsg.getText().toLowerCase().trim().contains(text.toLowerCase().trim()))
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message displayed with text " + text, Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message is not displayed with text " + text, Status.WARNING);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify accessed indicator
	// # Function Name : verifyAccessedIndicator    
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyAccessedIndicator(String docTitle) {
		Boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
			List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
			for (WebElement list : OListResult1) {
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
					for (WebElement document : OListItems) {

						WebElement btnGet = commonLibrary.isExist(document, UIMAP_SearchResult.Access, 20);
						if (btnGet != null) {
							if (btnGet.getText().contains("Accessed"))
								blnFlag = true;
							break;
						}
						// }

					}
					if (blnFlag)
						break;

				}
			}
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + docTitle, "Not Clicked  on the document " + docTitle, Status.FAIL);
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document in folder
	// # Function Name : clickDocInFolder  
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document clickDocInFolder(String strDocTitle) {

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
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
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

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);

		commonLibrary.sleep(20000);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : verifyPageTitle     
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document verifyPageTitle(String title) {
		commonLibrary.sleep(1000);
		generalFunctions.verifyPageTitle(title);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click create button without entering
	// name
	// # Function Name : ClickCreateFolder
	// # Author : Ramesh
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public WorkFolders clickCreateFolder() {

		commonLibrary.sleep(30000);
		WebElement createbutton = commonLibrary.isExist(UIMAP_WorkFolders.createbutton, 10);

		if (createbutton != null) {
			commonLibrary.clickJS(createbutton, "createbutton");
		}

		else
			report.updateTestLog("Clicking on create button", "create button not found", Status.FAIL);

		WebElement inputtext = commonLibrary.isExist(UIMAP_WorkFolders.newFolderText, 10);

		if (inputtext != null) {

			commonLibrary.sleep(30000);
			WebElement createFolder = commonLibrary.isExist(UIMAP_WorkFolders.createFolder, 10);

			if (createFolder != null) {
				commonLibrary.clickJS(createFolder, "Create");
			}

			else

				report.updateTestLog("Clicking on create button", "create button not found", Status.WARNING);

		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create a folder
	// document.
	// # Function Name : ClickCreateNewFolder2
	// # Author : Ramesh
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public WorkFolders clickCreateNewFolder2(String FolderName) {

		WebElement inputtext = commonLibrary.isExist(UIMAP_WorkFolders.newFolderText, 10);

		if (inputtext != null) {
			commonLibrary.setDataInTextBoxWithLog(UIMAP_WorkFolders.newFolderText, FolderName);
			commonLibrary.sleep(30000);
			WebElement createFolder = commonLibrary.isExist(UIMAP_WorkFolders.createFolder, 10);

			if (createFolder != null) {
				commonLibrary.clickJS(createFolder, "Create");
			}

			else

				report.updateTestLog("Clicking on create button", "create button not found", Status.WARNING);

		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the header details section in
	// full view WF page
	// # Function Name : verifyPropertiesUnderHeaderSection()     
	// # Author : Deepha Hariramasamy
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyPropertiesUnderHeaderSection(String folderName, String clientID) {
		try {
			WebElement header = commonLibrary.isExist(UIMAP_IMContent.header, 10);
			if (header != null) {
				// WebElement typeProperties =
				// commonLibrary.isExist(UIMAP_IMContent.typeProperties, 10);
				if (header.getText().contains(folderName) && header.getText().contains(clientID))
					report.updateTestLog("Verify properties sections details", folderName + ", " + clientID + " are displayed", Status.PASS);
				else
					report.updateTestLog("Verify properties sections details", folderName + ", " + clientID + " are NOT displayed", Status.FAIL);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLink2     
	// # Author :
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public IntermediateContent clickDocLink2web(String strDocTitle) {
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
							if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
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

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the recycle bin
	// # Function Name : clickRecycleBin     
	// # Author : Ramesh
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public WorkFolders clickRecycleBin() {

		WebElement recycleBin = commonLibrary.isExist(UIMAP_WorkFolders.recycleBin, 10);
		if (recycleBin != null) {
			commonLibrary.clickButton(recycleBin);
			commonLibrary.sleep(5000);
			report.updateTestLog("Click on the Recyclebin button", "Clicked on Recyclebin button", Status.PASS);
		} else
			report.updateTestLog("Click on the Recyclebin button", "Not clicked on Recyclebin button", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public WorkFolders selectDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.tagName("span"), 10);
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

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the delete document button
	// # Function Name : clickDeleteDocButton     
	// # Author : Thangavel M
	// # Date Created : Jan-20-2016
	// #*****************************************************************************************************************************

	public WorkFolders clickDeleteDocButton() {

		WebElement divDelete = commonLibrary.isExist(UIMAP_WorkFolders.divDelete, 10);
		WebElement spanDelete = commonLibrary.isExist(divDelete, UIMAP_WorkFolders.spanDelete, 10);

		if (spanDelete != null) {
			commonLibrary.clickButton(spanDelete);
			// commonLibrary.sleep(5000);
			report.updateTestLog("Click on the Delete button", "Clicked on Delete button", Status.PASS);
		} else
			report.updateTestLog("Click on the Delete button", "Not clicked on Delete button", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in Delete popup
	// # Function Name : verifyTextinDeletePopup     
	// # Author : Thangavel M
	// # Date Created : Jan-20-2016
	// #*****************************************************************************************************************************

	public WorkFolders verifyTextinDeletePopupandDelete(String Message) {

		WebElement asideDelete = commonLibrary.isExist(UIMAP_WorkFolders.asideDelete, 10);
		WebElement formAside = commonLibrary.isExist(asideDelete, UIMAP_WorkFolders.formAside, 10);
		WebElement fieldsetTag = commonLibrary.isExist(formAside, UIMAP_WorkFolders.fieldsetTag, 10);
		if (fieldsetTag != null && fieldsetTag.getText().trim().toLowerCase().contains(Message.trim().toLowerCase())) {

			report.updateTestLog("Verify message " + Message + " in the Delete Popup", "Message " + Message + "in the delete popup is available ", Status.PASS);
		} else
			report.updateTestLog("Verify message " + Message + " in the Delete Popup", "Message " + Message + "in the delete popup is not available ", Status.FAIL);

		WebElement footerTag = commonLibrary.isExist(asideDelete, UIMAP_WorkFolders.footerTag, 10);
		WebElement ulDelete = commonLibrary.isExist(footerTag, UIMAP_WorkFolders.ulDelete, 10);
		WebElement deletebutton = commonLibrary.isExist(ulDelete, UIMAP_WorkFolders.deletebutton, 10);
		if (deletebutton != null) {

			report.updateTestLog("Verify delete buttion is available in the Delete Popup", "Delete buttion is available in the Delete Popup", Status.PASS);
		} else
			report.updateTestLog("Verify delete buttion is available in the Delete Popup", "Delete buttion is not available in the Delete Popup", Status.FAIL);

		WebElement cancelbutton = commonLibrary.isExist(ulDelete, UIMAP_WorkFolders.cancelbutton, 10);
		if (cancelbutton != null) {

			report.updateTestLog("Verify cancel buttion is available in the Delete Popup", "Cancel buttion is available in the Delete Popup", Status.PASS);
		} else
			report.updateTestLog("Verify cancel buttion is available in the Delete Popup", "Cancel buttion is not available in the Delete Popup", Status.FAIL);

		if (deletebutton != null) {
			commonLibrary.clickButton(deletebutton);

			report.updateTestLog("Click on the Delete button in the delete popup", "Clicked on Delete button", Status.PASS);
		} else
			report.updateTestLog("Click on the Delete button  in the delete popup", "Not clicked on Delete button", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select empty RecycleBin
	// # Function Name : emptyRecycleBin     
	// # Author : Thangavel M
	// # Date Created : Jan-21-2016
	// #*****************************************************************************************************************************

	public WorkFolders emptyRecycleBin() {

		WebElement actionbutton = commonLibrary.isExist(UIMAP_WorkFolders.actionbutton, 10);
		if (actionbutton != null) {
			commonLibrary.clickButton(actionbutton);
			commonLibrary.sleep(5000);
		}
		WebElement asideEmpty = commonLibrary.isExist(UIMAP_WorkFolders.asideEmpty, 10);
		WebElement emptyRecyclebin = commonLibrary.isExist(asideEmpty, UIMAP_WorkFolders.emptyRecyclebin, 10);
		if (emptyRecyclebin != null) {
			commonLibrary.clickButton(emptyRecyclebin);
			report.updateTestLog("Click on Empty RecycleBin", "Clicked on empty RecycleBin  ", Status.PASS);
		} else
			report.updateTestLog("Click on Empty RecycleBin", " Not Clicked on empty RecycleBin  ", Status.FAIL);

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in Empty RecycleBin
	// popup
	// # Function Name : verifyTextinEmptyREcycleBinPopupandDelete     
	// # Author : Thangavel M
	// # Date Created : Jan-21-2016
	// #*****************************************************************************************************************************

	public WorkFolders verifyTextinEmptyRecycleBinPopupandDelete(String Message) {

		WebElement asideDelete = commonLibrary.isExist(UIMAP_WorkFolders.asideDelete, 10);
		WebElement formAside = commonLibrary.isExist(asideDelete, UIMAP_WorkFolders.formAside, 10);
		WebElement fieldsetTag = commonLibrary.isExist(formAside, UIMAP_WorkFolders.fieldsetTag, 10);
		if (fieldsetTag != null && fieldsetTag.getText().trim().toLowerCase().contains(Message.trim().toLowerCase())) {

			report.updateTestLog("Verify message " + Message + " in the Delete Popup", "Message " + Message + "in the delete popup is available ", Status.PASS);
		} else
			report.updateTestLog("Verify message " + Message + " in the Delete Popup", "Message " + Message + "in the delete popup is not available ", Status.FAIL);

		WebElement footerTag = commonLibrary.isExist(asideDelete, UIMAP_WorkFolders.footerTag, 10);
		WebElement ulDelete = commonLibrary.isExist(footerTag, UIMAP_WorkFolders.ulDelete, 10);
		WebElement deletebutton = commonLibrary.isExist(ulDelete, UIMAP_WorkFolders.deletebutton, 10);
		if (deletebutton != null) {

			report.updateTestLog("Verify delete buttion is available in the Delete Popup", "Delete buttion is available in the Delete Popup", Status.PASS);
		} else
			report.updateTestLog("Verify delete buttion is available in the Delete Popup", "Delete buttion is not available in the Delete Popup", Status.FAIL);

		WebElement cancelbutton = commonLibrary.isExist(ulDelete, UIMAP_WorkFolders.cancelbutton, 10);
		if (cancelbutton != null) {

			report.updateTestLog("Verify cancel buttion is available in the Delete Popup", "Cancel buttion is available in the Delete Popup", Status.PASS);
		} else
			report.updateTestLog("Verify cancel buttion is available in the Delete Popup", "Cancel buttion is not available in the Delete Popup", Status.FAIL);

		if (deletebutton != null) {
			commonLibrary.clickButton(deletebutton);

			report.updateTestLog("Click on the Delete button in the delete popup", "Clicked on Delete button", Status.PASS);
		} else
			report.updateTestLog("Click on the Delete button  in the delete popup", "Not clicked on Delete button", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Select All button
	// # Function Name : clickSelectAll     
	// # Author : Thangavel M
	// # Date Created : Jan-20-2016
	// #*****************************************************************************************************************************

	public WorkFolders clickSelectAllButton() {

		WebElement selectAll = commonLibrary.isExist(UIMAP_WorkFolders.selectAll, 10);

		if (selectAll != null) {
			commonLibrary.clickButton(selectAll);
			// commonLibrary.sleep(5000);
			report.updateTestLog("Click on the Select All button", "Clicked on Select All button", Status.PASS);
		} else
			report.updateTestLog("Click on the Select All button", "Not clicked on Select All button", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message.
	// # Function Name : verifyBannerMessage
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders verifyBannerMessage1(String text) {
		WebElement bannermsg1 = commonLibrary.isExistNegative(UIMAP_WorkFolders.bnrmsg1, 20);
		if (bannermsg1 != null && bannermsg1.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message displayed with text " + text, Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message is not displayed with text " + text, Status.WARNING);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down
	// # Function Name : selectSortBy
	// # Author : Ramesh(414916)
	// # Date Created : jan '16
	// #*****************************************************************************************************************************
	public WorkFolders selectSortBy(String value) {
		generalFunctions.selectSortBy(value);
		return new WorkFolders(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Days left for folders in Recycle bin in ascending			
	// # Function Name : verifySortByOption
	// # Author : Ramesh(414916)
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public WorkFolders verifySortByOption(String value) {
		
		WebElement sortByDiv = commonLibrary.isExist(UIMAP_WorkFolders.sortDiv, 20);
		if(sortByDiv != null){
			WebElement dropdownDiv = commonLibrary.isExist(UIMAP_WorkFolders.ddDiv, 20);
			if(dropdownDiv != null){
				WebElement sortTitle = commonLibrary.isExist(UIMAP_WorkFolders.sortSpan, 20);
				if(sortTitle != null && sortTitle.getText().toLowerCase().trim().contains(value.toLowerCase().trim())){
					report.updateTestLog(" Verify recycle bin displays with" +value, "Recycle bin displays with " +value, Status.PASS);
				}
				else
				{
					report.updateTestLog("Verify recycle bin displays with" +value, "Recycle bin Not displayed with "  +value, Status.FAIL);
				}
			}
		}							
				commonLibrary.sleep(5000);
	return new WorkFolders(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Sort By options displayed
	// # Function Name : verifySortByOptionDisplayed
	// # Author : Ramesh(414916)
	// # Date Created : jan'16
	// #*****************************************************************************************************************************
	public WorkFolders verifySortByOptionDisplayed(String value) {

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
						report.updateTestLog("Verify " + value + " is displayed in Sort By drop down", value + " is displayed in Sort By drop down", Status.PASS);
						commonLibrary.sleep(2000);
						flag = true;
						break;
					}
				}
				
			} if(!flag)
				report.updateTestLog("Verify " + value + " is displayed in Sort By drop down",  value + " is NOT displayed in Sort By drop down", Status.FAIL);				
			return new WorkFolders(scriptHelper);
	}

}
