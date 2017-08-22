package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_TaxSearchResults;
import UIMAP.UIMAP_WorkFolders;
import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ResearchMap extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private ExcelDataAccess excel;

	public ResearchMap(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("researchmap"))
				commonLibrary.sleep(5000);

		} while (!url.contains("researchmap") && counter < 40);
		if (!driver.getCurrentUrl().contains("researchmap")) {
			throw new IllegalStateException("Research Map page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add Research Map to folder and add
	// the document title to Excel
	// # Function Name : AddToFolder    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap selectPopupLinks(String searchTerm, String category, String lnkName) {
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
							List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttons, 10);
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
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select PopupLink in Actions
	// # Function Name : selectPopupLink_Actions    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap selectPopupLinkActions(String searchTerm, String lnkName) {
		boolean flag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachment, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				commonLibrary.clickButton(button);
				attachmentContent = commonLibrary.isExist(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement doclink = commonLibrary.isExist(UIMAP_ResearchMap.doclnk, 10);
				if (doclink != null && doclink.isDisplayed()) {
					WebElement content = commonLibrary.isExistNegative(doclink, UIMAP_ResearchMap.lnkLinks, 10);
					commonLibrary.clickButton(content);
					WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
					List<WebElement> popupActionList = commonLibrary.isExistList(popup, UIMAP_SearchResult.lstTagName, 10);
					for (WebElement ActionLink : popupActionList) {
						if (ActionLink.getText().toLowerCase().contains(lnkName.toLowerCase())) {
							WebElement actionbutton = commonLibrary.isExistNegative(ActionLink, UIMAP_ResearchMap.buttonTag, 10);
							commonLibrary.clickButton(actionbutton);
							flag = true;
							break;
						}
					}
					if (!flag) {
						report.updateTestLog("Click on " + lnkName + " under attachments for trail " + searchTerm, lnkName + " link not clicked", Status.FAIL);
						break;
					} else {
						break;
					}

					/*
					 * WebElement ActionLink = commonLibrary.isExist(popup,
					 * By.linkText(lnkName), 2); if (ActionLink != null) {
					 * commonLibrary.clickLink(ActionLink, lnkName); break; }
					 * else break;
					 * 
					 * }
					 */

				}
			}
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select PopupLink
	// # Function Name : selectPopupLink_Wf    
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public WorkFolders selectPopupLinkWf(String searchTerm, String category, String lnkName) {
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
							List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
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
	// # Function Description : Function to Verify Folder Page Displayed
	// # Function Name : VerifyFolderPageDisplayed    
	// # Author : uma
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
	// # Function Description : Function to verify search added to CSR.
	// # Function Name : verifySearchAddedtoCSR     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifySearchAddedtoCSR(String searchTerm) {
		boolean blnFlag = false;
		WebElement compareTray = commonLibrary.isExistNegative(UIMAP_ResearchMap.compareTray, 10);
		List<WebElement> activeButtons = commonLibrary.isExistList(compareTray, UIMAP_ResearchMap.activeCompareButton, 10);
		for (WebElement button : activeButtons) {
			if (button.getText().contains("Compare Search Results")) {
				commonLibrary.clickButtonLogSmallWait(button, "Compare Search Results");
				WebElement comparePopup = commonLibrary.isExistNegative(UIMAP_ResearchMap.comparePopup, 10);
				List<WebElement> searchTermList = commonLibrary.isExistList(comparePopup, UIMAP_ResearchMap.lstTagName, 10);
				for (WebElement term : searchTermList) {
					if (term.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
						report.updateTestLog("Verify " + searchTerm + " is added to CSR tray.", searchTerm + " is added to CSR tray.", Status.PASS);
						WebElement closeComparePopup = commonLibrary.isExistNegative(compareTray, UIMAP_ResearchMap.closeComparePopup, 10);
						commonLibrary.clickButtonLogSmallWait(closeComparePopup, "Close Popup");
						blnFlag = true;
						break;
					}
				}
			}
			if (blnFlag)
				break;
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + searchTerm + " is added to CSR tray.", searchTerm + " is not added to CSR tray.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Compare Search results.
	// # Function Name : clickCompareSearchResults     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Search clickCompareSearchResults() {
		boolean blnFlag = false;
		WebElement compareTray = commonLibrary.isExistNegative(UIMAP_ResearchMap.compareTray, 10);
		List<WebElement> activeButtons = commonLibrary.isExistList(compareTray, UIMAP_ResearchMap.activeCompareButton, 10);
		for (WebElement button : activeButtons) {
			if (button.getText().contains("Compare Search Results")) {
				commonLibrary.clickButtonLogSmallWait(button, "Compare Search Results");
				WebElement comparePopup = commonLibrary.isExistNegative(UIMAP_ResearchMap.comparePopup, 10);
				WebElement CSRbutton = commonLibrary.isExistNegative(comparePopup, UIMAP_ResearchMap.CSRbutton, 10);
				if (CSRbutton.isEnabled()) {
					commonLibrary.clickButtonParentWithWait(CSRbutton, "Compare Search Results");
					blnFlag = true;
					break;
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click CSR button", "Button is not clicked.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Add To Folder
	// # Function Name : AddToFolder     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap addToFolder(String folderName, String folderType, String excelData)

	{

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null)

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

		// CLICK ON <<<CREATE NEW FOLDER>>>

		WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
		if (createNewFolder != null)
			commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

		// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

		WebElement folderNam = commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
		if (folderNam != null)
			// folderNam.sendKeys(folderName);
			// report.updateTestLog("Enter "+folderName+" in Folder Name text box",folderName+" is entered in Folder Name text box"
			// , Status.PASS);
			commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

		// CLICK ON <<<CREATE>>>
		WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
		if (create != null)
			commonLibrary.clickButtonParentWithWait(create, "Create");

		create = commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
		WebElement createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 5);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(7000000);
				createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 3);
				count++;
				System.out.println("Waiting" + count);
			} while (create.equals(createNew) && count < 80);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// click on the radio buttons PDF/Live Map
		if (folderType.equalsIgnoreCase("PDF")) {
			WebElement pdfRadio = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderPDF, 10);
			commonLibrary.setRadioButton(pdfRadio, "PDF");
		} else if (folderType.equalsIgnoreCase("Live Map")) {
			WebElement liveMap = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderLiveMap, 10);
			commonLibrary.setRadioButton(liveMap, "Live Map");
		}

		// To write the docment title to excel
		if (excelData != "") {
			String sheetNam = excelData.split(";")[0];
			int row = Integer.parseInt((excelData.split(";")[1]));
			String column = excelData.split(";")[2];
			String datatablePath = excelData.split(";")[3];

			WebElement doctitle = commonLibrary.isExist(UIMAP_ResearchMap.folderDocTitle, 10);
			String docTit = doctitle.getAttribute("value");
			// String datatablePath = frameworkParameters.getRelativePath() +
			// Util.getFileSeparator() + "Datatables";
			excel = new ExcelDataAccess(datatablePath, sheetNam);
			excel.setDatasheetName("General_Data");
			excel.setValue(row, column, docTit);
		}

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		// saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder,
		// 10);
		WebElement saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolder, 3);
		count = 0;
		try {
			do {
				commonLibrary.sleep(700000);
				saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolder, 3);
				count++;
				System.out.println("Waiting" + count);
			} while (saveFolder == saveNew && count < 40);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Folders Page
	// # Function Name : navigateToFolders    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			if (folder == null) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null) {
					// commonLibrary.highlightElement(btnMore);
					if ((browsername.contains("internet")))
						commonLibrary.clickMethod(btnMore, "More");
					else
						commonLibrary.clickMethod(btnMore, "More");
				}
				folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			}

			if (folder != null) {
				commonLibrary.clickLinkWithWebElementWithWait(folder, "Folders");
				commonLibrary.sleep(6000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select first 20 RM trial checkboxes
	// # Function Name : changeDateRange    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap selectTrails(int num) {
		WebElement viewAll = commonLibrary.isExist(UIMAP_ResearchMap.viewAllTrials, 10);
		if (viewAll != null)
			commonLibrary.setCheckBox(viewAll, false);

		WebElement allTrials = commonLibrary.isExist(UIMAP_ResearchMap.allTrials, 10);
		List<WebElement> trials = commonLibrary.isExistList(allTrials, By.tagName("li"), 20);
		int i = 0;
		for (WebElement item : trials) {
			WebElement trial = commonLibrary.isExist(item, UIMAP_ResearchMap.checkBox, 20);
			commonLibrary.setCheckBox(trial, true);
			i++;
			if (i == num)
				break;

		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select first 20 RM trial checkboxes
	// # Function Name : changeDateRange    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap changeDateRange() {
		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);
		cal.add(Calendar.DATE, 1);

		now = cal.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String DateToday = format.format(now);

		boolean flag = false;
		WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
		WebElement dateRange = commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
		if (dateRange != null) {
			if (browsername.equalsIgnoreCase("internet explorer"))
				commonLibrary.clickJS(dateRange, "Change Date Range");
			else
				commonLibrary.clickLinkWithWebElementWithWait(dateRange, "Change Date Range");
		}

		WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
		List<WebElement> months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
		for (int i = months.size() - 1; i > 0; i--) {
			List<WebElement> date = commonLibrary.isExistList(months.get(i), UIMAP_ResearchMap.activeDates, 10);
			for (WebElement count : date) {
				if ((count.getAttribute("class").contains("small")) || (count.getAttribute("class").contains("medium"))) {
					// count.click();
					commonLibrary.clickLinkWithWebElementWithWait(count, "");
					flag = true;
					break;
				}
			}
			if (flag)
				break;

		}

		if (!(flag)) {
			WebElement today = commonLibrary.isExist(months.get(months.size() - 1), By.cssSelector("td[data-date='" + DateToday + "']"), 10);
			// today.click();
			commonLibrary.clickLinkWithWebElementWithWait(today, "Today");

		}

		List<WebElement> buttons = commonLibrary.isExistList(dateFilter, By.tagName("button"), 10);
		for (WebElement item : buttons) {
			if (item.getText().contains("OK")) {
				commonLibrary.clickButtonParentWithWait(item, "OK");
				break;
			}
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select attachment link from the
	// trail, the document link and popup links.
	// # Function Name : selectAttachmentLinks     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectAttachmentLinks(String searchTerm, String docName, String lnkName) {

		boolean blnFlag = false, blnFlag1 = false;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				if (!attachmentContent.getAttribute("class").contains("active")) {
					commonLibrary.clickButtonParentWithWait(button, "Attachment Button");
					blnFlag1 = true;
					break;
				}
			}
		}

		if (blnFlag1) {
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExist(trail, UIMAP_ResearchMap.attachmentContent, 10);
					WebElement doclink = commonLibrary.isExist(attachmentContent, UIMAP_ResearchMap.doclnk, 10);
					if (doclink != null && doclink.isDisplayed()) {
						WebElement link = commonLibrary.isExistNegative(doclink, UIMAP_ResearchMap.lnkLinks, 10);
						commonLibrary.clickButtonLogSmallWait(link, docName);
						WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
						List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttons, 10);
						for (WebElement link1 : popuplinks) {
							if (link1.getText().toLowerCase().contains(lnkName.toLowerCase())) {
								commonLibrary.clickLinkWithWebElementWithWait(link1, lnkName);
								blnFlag = true;
								break;
							}
						}
					}
					if (blnFlag)
						break;

				}
			}
		} else
			report.updateTestLog("Click on " + lnkName + " for trail " + searchTerm, "Attachment link not present.", Status.FAIL);
		if (!blnFlag)
			report.updateTestLog("Click on " + lnkName + " for trail " + searchTerm, lnkName + " link not clicked.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Attachment Node and click
	// Document.
	// # Function Name : selectAttachmentNode_clickDocument_retRM     
	// # Author : vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectAttachmentNode_clickDocument_retRM(String searchTerm, String docName, int docNo) {
		try {
			boolean blnFlag = false;

			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
					if (!attachmentContent.getAttribute("class").contains("active")) {
						commonLibrary.clickJS(button, "1st Attachment Button");
						if (browsername.contains("internet"))
							commonLibrary.sleep(10000);

						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
			if (docNo >= 2) {
				blnFlag = false;
				lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
				for (WebElement trail : lstResearchThread) {
					WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
					if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
						WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
						if (attachmentContent != null) {
							WebElement attachment = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.attachment, 10);
							WebElement button = commonLibrary.isExistNegative(attachment, UIMAP_ResearchMap.button, 10);
							// if(!attachmentContent.getAttribute("class").contains("active"))
							commonLibrary.clickJS(button, "2nd Attachment Button");// remove
																					// commented
																					// commonLibrary
																					// inside
							blnFlag = true;
							break;

						}
					}
					if (blnFlag)
						break;
				}
			}

			if (docNo == 3) {
				blnFlag = false;
				lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
				for (WebElement trail : lstResearchThread) {
					WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
					if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
						WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
						if (attachmentContent != null) {
							WebElement attachment = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.attachment, 10);
							// attachment =
							// commonLibrary.isExist_Negative(attachment,
							// UIMAP_ResearchMap.attachment, 10);
							WebElement button = commonLibrary.isExistNegative(attachment, UIMAP_ResearchMap.button, 10);
							// if(!attachmentContent.getAttribute("class").contains("active"))
							commonLibrary.clickJS(button, "3rd Attachment Button");// remove
																					// commented
																					// commonLibrary
																					// inside
							blnFlag = true;
							break;

						}
					}
					if (blnFlag)
						break;
				}
			}

			blnFlag = false;

			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					if (attachmentContent != null) {

						List<WebElement> doclink = commonLibrary.isExistList(attachmentContent, By.partialLinkText(docName), 10);
						if (doclink.size() == 1) {
							if (doclink != null && doclink.get(0) != null && doclink.get(0).isDisplayed()) {
								commonLibrary.clickJS(doclink.get(0), docName);
								blnFlag = true;
								break;
							}
						}
						if (doclink != null && doclink.size() >= docNo && doclink.get(docNo - 1) != null && doclink.get(docNo - 1).isDisplayed()) {
							// WebElement link =
							// commonLibrary.isExist_Negative(doclink.get(lnkNo-1),
							// UIMAP_ResearchMap.lnkLinks,10);
							commonLibrary.clickJS(doclink.get(docNo - 1), docName);
							blnFlag = true;
							break;
						}

					}
					if (blnFlag)
						break;

				}

			}
			if (!blnFlag)
				report.updateTestLog("Click on " + docName + " for trail " + searchTerm, docName + " link not clicked.", Status.FAIL);

		} catch (Exception e) {

		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Attachment Node and click
	// Document_retDoc
	// # Function Name : selectAttachmentNode_clickDocument_retDoc     
	// # Author : vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Document selectAttachmentNode_clickDocument_retDoc(String searchTerm, String docName, int docNo) {
		try {
			boolean blnFlag = false;

			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
					if (!attachmentContent.getAttribute("class").contains("active")) {
						commonLibrary.clickJS(button, "Attachment Button");// remove
																			// commented
																			// commonLibrary
																			// inside
						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
			if (docNo > 2) {
				blnFlag = false;
				lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
				for (WebElement trail : lstResearchThread) {
					WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
					if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
						WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
						if (attachmentContent != null) {
							WebElement attachment = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.attachment, 10);
							WebElement button = commonLibrary.isExistNegative(attachment, UIMAP_ResearchMap.button, 10);
							// if(!attachmentContent.getAttribute("class").contains("active"))
							commonLibrary.clickJS(button, "Attachment Button");// remove
																				// commented
																				// commonLibrary
																				// inside
							blnFlag = true;
							break;

						}
					}
					if (blnFlag)
						break;
				}
			}

			if (docNo == 3) {
				blnFlag = false;
				lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
				for (WebElement trail : lstResearchThread) {
					WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
					if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
						WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
						if (attachmentContent != null) {
							WebElement attachment = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.attachment, 10);
							// attachment =
							// commonLibrary.isExist_Negative(attachment,
							// UIMAP_ResearchMap.attachment, 10);
							WebElement button = commonLibrary.isExistNegative(attachment, UIMAP_ResearchMap.button, 10);
							// if(!attachmentContent.getAttribute("class").contains("active"))
							commonLibrary.clickJS(button, "Attachment Button");// remove
																				// commented
																				// commonLibrary
																				// inside
							blnFlag = true;
							break;

						}
					}
					if (blnFlag)
						break;
				}
			}

			blnFlag = false;
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					if (attachmentContent != null) {

						List<WebElement> doclink = commonLibrary.isExistList(attachmentContent, By.partialLinkText(docName), 10);
						if (doclink != null && doclink.size() >= docNo && doclink.get(docNo - 1) != null && doclink.get(docNo - 1).isDisplayed()) {
							// WebElement link =
							// commonLibrary.isExist_Negative(doclink.get(lnkNo-1),
							// UIMAP_ResearchMap.lnkLinks,10);
							commonLibrary.clickJS(doclink.get(docNo - 1), docName);
							blnFlag = true;
							break;
						} else if (doclink != null && doclink.get(0) != null && doclink.get(0).isDisplayed()) {
							commonLibrary.clickJS(doclink.get(0), docName);
							blnFlag = true;
							break;
						}
					}
					if (blnFlag)
						break;

				}

			}
			if (!blnFlag)
				report.updateTestLog("Click on " + docName + " for trail " + searchTerm, docName + " link not clicked.", Status.FAIL);

		} catch (Exception e) {

		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Link From Popup
	// # Function Name : selectLinkFromPopup_LIT     
	// # Author : vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail selectLinkFromPopup_LIT(String searchTerm, String lnkName) {
		try {
			boolean blnFlag = false;

			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					if (attachmentContent != null) {
						WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
						List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
						for (WebElement link1 : popuplinks) {
							if (link1.getText().toLowerCase().contains(lnkName.toLowerCase())) {

								commonLibrary.clickLinkWithWebElementWithWait(link1, lnkName);
								blnFlag = true;
								break;
							}
						}
					}
				}
				if (blnFlag)
					break;

			}

			if (!blnFlag)
				report.updateTestLog("Click on " + lnkName + " for trail " + searchTerm, lnkName + " link not clicked.", Status.FAIL);

		} catch (Exception e) {

		}

		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Link From Popup_Doc
	// # Function Name : selectLinkFromPopup_Doc     
	// # Author : vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Document selectLinkFromPopup_Doc(String searchTerm, String lnkName) {
		try {
			boolean blnFlag = false;

			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					if (attachmentContent != null) {
						WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
						List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
						for (WebElement link1 : popuplinks) {
							if (link1.getText().toLowerCase().contains(lnkName.toLowerCase())) {

								commonLibrary.clickLinkWithWebElementWithWait(link1, lnkName);
								if (browsername.contains("internet"))
									commonLibrary.sleep(10000);
								blnFlag = true;
								break;
							}
						}
					}
				}
				if (blnFlag)
					break;

			}

			if (!blnFlag)
				report.updateTestLog("Click on " + lnkName + " for trail " + searchTerm, lnkName + " link not clicked.", Status.FAIL);

		} catch (Exception e) {

		}
		WebElement citation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
		int count = 0;
		do {
			count++;
			commonLibrary.sleep(10000);
		} while (citation == null && count < 40);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Popup Content
	// # Function Name : verifyPopupContent     
	// # Author : vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyPopupContent(String searchTerm, String lnkName, String verifyText) {
		try {
			boolean blnFlag = false;
			boolean flag = false;
			String popupContent = "e ";
			String[] cont;
			String temp = null;
			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					if (attachmentContent != null) {
						WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
						popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
						popupContent = popup.getText();
						cont = verifyText.split("#");
						flag = true;
						for (String c : cont) {
							if (popupContent.contains(c)) {
								blnFlag = true;
							} else if (c.contains("Shepard")) {
								report.updateTestLog("Verify if the popup displays the necessary details ", "Popup is not displaying the following: " + c, Status.FAIL);
							} else {
								blnFlag = false;
								temp = temp + c + ",";
								break;

							}
						}
						// flag = flag && blnFlag;
						if (flag)
							break;
					}
				}
				if (blnFlag || flag)
					break;

			}

			if (flag && blnFlag)
				report.updateTestLog("Verify if the popup displays the necessary details ", "Popup displayes the following: " + verifyText, Status.PASS);
			else if (temp != null)
				report.updateTestLog("Verify if the popup displays the necessary details ", "Popup NOT displayes the following: " + temp, Status.FAIL);
			else
				report.updateTestLog("Verify if the popup displays the necessary details ", "Popup NOT displayes the following: " + verifyText, Status.FAIL);

			return new ResearchMap(scriptHelper);

		} catch (Exception e) {

		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Find Similar Documents
	// # Function Name : clickFindSimilarDocuments     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search clickFindSimilarDocuments() {
		boolean blnFlag = false;
		WebElement compareTray = commonLibrary.isExistNegative(UIMAP_ResearchMap.compareTray, 10);
		List<WebElement> activeButtons = commonLibrary.isExistList(compareTray, UIMAP_ResearchMap.activeCompareButton, 10);
		for (WebElement button : activeButtons) {
			if (button.getText().contains("Find Similar Documents")) {
				commonLibrary.clickButtonLogSmallWait(button, "Find Similar Documents");

				List<WebElement> comparePopup = commonLibrary.isExistList(compareTray, UIMAP_ResearchMap.comparePopup, 10);
				for (WebElement popup : comparePopup) {
					if (popup.isDisplayed()) {
						WebElement fsdButton = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.fsdButton, 10);

						commonLibrary.clickButtonParentWithWait(fsdButton, "Find Similar Documents");
						blnFlag = true;
						break;

					}
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click FSD button", "Button is not clicked.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Trial
	// # Function Name : verifyTrial     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyTrial(String docTitle, String client) {
		try {
			boolean flag = false;
			WebElement rmDiv = commonLibrary.isExist(UIMAP_ResearchMap.resultDiv, 10);
			List<WebElement> list = commonLibrary.isExistList(rmDiv, UIMAP_ResearchMap.lstTagName, 10);
			for (WebElement item : list) {
				if (item.getText().contains(docTitle) && item.getText().contains(client)) {
					flag = true;
					break;
				}
			}
			if (flag)
				report.updateTestLog("Verify RM trial", "Title:" + docTitle + " and client information is present", Status.PASS);
			else
				report.updateTestLog("Verify RM trial", "Title:" + docTitle + " and client information is NOT present", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select ResearchMap Alert Node
	// # Function Name : selectRMAlertNode     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectRMAlertNode(String docTitle) {
		try {

			WebElement rmDiv = commonLibrary.isExist(UIMAP_ResearchMap.resultDiv, 10);
			List<WebElement> list = commonLibrary.isExistList(rmDiv, UIMAP_ResearchMap.lstTagName, 10);
			for (WebElement item : list) {
				if (item.getText().contains(docTitle)) {
					WebElement activity = commonLibrary.isExist(item, UIMAP_ResearchMap.activityDiv, 10);
					WebElement bubble = commonLibrary.isExist(activity, UIMAP_ResearchMap.bubble, 10);
					if (bubble != null) {
						if (browsername.contains("internet")) {
							commonLibrary.clickJS(bubble, docTitle + " Node");
							break;
						} else {
							commonLibrary.clickButtonParentWithWait(bubble, docTitle + " Node");
							break;
						}
					}
					commonLibrary.sleep(3000);
				}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify verify Popup Details
	// # Function Name : verifyPopupDetails    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyPopupDetails(String docTitle, String docType, String header) {
		try {
			WebElement popup = commonLibrary.isExist(UIMAP_ResearchMap.popup, 10);
			if (popup != null) {
				if (popup.getText().contains(docTitle) && popup.getText().contains(header) && popup.getText().contains(docType))
					report.updateTestLog("Verify RM popup details", header + ", " + docType + ", " + docTitle + " are displayed", Status.PASS);
				else
					report.updateTestLog("Verify RM popup details", header + ", " + docType + ", " + docTitle + " are NOT displayed", Status.FAIL);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application.
	// # Function Name : logout    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select aatachment link, document
	// link and verify pop up.
	// # Function Name : selectAttachmentLink_VerifyPopup     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectAttachmentLink_VerifyPopup(String searchTerm, String docName, String content) {

		boolean blnFlag = false;
		boolean flag = false;
		String popupContent = "e";
		String[] cont;
		String temp = null;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				if (!attachmentContent.getAttribute("class").contains("active"))
					commonLibrary.clickButtonParentWithWait(button, "Attachment Button");
				attachmentContent = commonLibrary.isExist(trail, UIMAP_ResearchMap.attachmentContent, 10);

				WebElement doclink = commonLibrary.isExist(UIMAP_ResearchMap.doclnk, 10);
				if (doclink != null && doclink.isDisplayed()) {
					WebElement link = commonLibrary.isExistNegative(doclink, UIMAP_ResearchMap.lnkLinks, 10);
					commonLibrary.clickButtonLogSmallWait(link, docName);

					WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
					popupContent = popup.getText();
					cont = content.split("#");
					flag = true;
					for (String c : cont) {
						if (popupContent.contains(c)) {
							blnFlag = true;
						} else {
							blnFlag = false;
							temp = temp + c + ",";
							break;

						}

					}
				}
				flag = flag && blnFlag;
				if (flag)
					break;

			}
		}
		if (flag)
			report.updateTestLog("Verify if the popup displays the necessary details ", "Popup displayes the following: " + content, Status.PASS);
		else if (temp != null)
			report.updateTestLog("Verify if the popup displays the necessary details ", "Popup NOT displayes the following: " + temp, Status.FAIL);
		else
			report.updateTestLog("Verify if the popup displays the necessary details ", "Popup NOT displayes the following: " + content, Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Node Order
	// # Function Name : verifyNodeOrder     
	// # Author : Veeshma
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyNodeOrder(String searchTerm, String order) {

		boolean blnFlag = false;
		boolean flag = false;
		String[] ord;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				List<WebElement> bubbleList = commonLibrary.isExistList(trail, UIMAP_ResearchMap.bubble, 10);
				ord = order.split("##");
				if (bubbleList.size() == ord.length) {
					flag = true;
					for (int i = 0; i < ord.length; i++) {
						if (ord[i].equalsIgnoreCase(bubbleList.get(i).getText())) {
							blnFlag = true;
						} else {
							blnFlag = false;
							break;
						}
						flag = flag && blnFlag;
					}
				}

			}
			if (flag)
				break;
		}
		if (blnFlag)
			report.updateTestLog("Verify the Trail '" + searchTerm + "' and its node representations/ connections", "Nodes are displayed in the following order: " + order, Status.PASS);
		else
			report.updateTestLog("Verify the Trail '" + searchTerm + "' and its node representations/ connections", "Nodes are displayed in the following order: " + order, Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify popup
	// # Function Name : selectRMNode_VerifyPopup    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public ResearchMap selectRMNodeVerifyPopup(String searchTerm, String category, String content) {
		boolean blnFlag = false;
		boolean flag = false;
		String popupContent = "";
		String[] cont;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		int count = 0;
		do {
			count++;
			commonLibrary.sleep(10000);
		} while (lstResearchThread != null & count < 15);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().equals(searchTerm.toLowerCase()) || trailHeader.getText().toLowerCase().equals(category.toLowerCase() )) {
				List<WebElement> lnkClassBubble = commonLibrary.isExistList(trail, UIMAP_SearchResult.lnkClassBubble, 20);
				if (lnkClassBubble.size() > 0) {

					for (WebElement item : lnkClassBubble) {
						if (item != null && item.getText().toLowerCase().contains(category.toLowerCase())) {
							commonLibrary.scrollToView(item);
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
	// # Function Description : Function to select ResearchMap Node and Verify
	// Popup
	// # Function Name : selectRMNode_VerifyPopup     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectRMNodeVerifyPopup(String searchTerm, String category, String content, boolean closePopup) {
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

							if (closePopup) {
								WebElement closePop = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.closeRMPopup, 10);
								commonLibrary.clickButton(closePop);
								// commonLibrary.clickButtonLogSmallWait(closePop,
								// "Close Popup");
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
	// # Function Description : Function to click Link in Popup
	// # Function Name : clickLink_Popup     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Search clickLink_Popup(String linkName) {
		boolean blnFlag = false;
		WebElement popup1 = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.buttonTag, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, linkName);
				commonLibrary.sleep(10000);
				blnFlag = true;
				break;
			}
		}
		WebElement header = commonLibrary.isExist(UIMAP_SearchResult.txtStudentReportHeader, 20);
		if (header != null)
			commonLibrary.focusControlJS(header);

		if (header.getText().contains("Table of Contents"))
			report.updateTestLog("Verify Legal Search Results page displays", "Legal Search Results Page is not displayed", Status.FAIL);
		else
			report.updateTestLog("Verify Legal Search Results page displays", "Legal Search Results Page is displayed", Status.PASS);
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a trail is present in
	// Research Map.
	// # Function Name : verifyTrail     
	// # Author : Pratik
	// # Date Created : Apr'15
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
	// # Function Description : Function to verify the nodes present in trail.
	// # Function Name : verifyNodesInTrail     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyNodesInTrail(String searchTerm, String contentType, String filter, String contentType1) {
		boolean contentTypeFlag = false, filterFlag = false, contentTypeFlag1 = false;
		WebElement rmDiv = commonLibrary.isExist(UIMAP_ResearchMap.resultDiv, 10);
		List<WebElement> list = commonLibrary.isExistList(rmDiv, UIMAP_ResearchMap.lstTagName, 10);
		for (WebElement item : list) {
			if (item.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				List<WebElement> bubbleLinks = commonLibrary.isExistList(item, UIMAP_ResearchMap.bubble, 10);
				if (bubbleLinks.size() > 2) {
					if (!contentType.equals("")) {
						for (int i = 1; i < bubbleLinks.size(); i++) {
							if (bubbleLinks.get(i).getText().toLowerCase().contains(contentType.toLowerCase()) && bubbleLinks.get(i - 1).getText().toLowerCase().contains(searchTerm.toLowerCase())) {
								report.updateTestLog("Verify " + contentType + " node is present to the right of " + searchTerm, contentType + " node is present to the right of " + searchTerm, Status.PASS);
								contentTypeFlag = true;
								break;
							}
						}
					}

					if (!filter.equals("")) {
						for (int i = 1; i < bubbleLinks.size(); i++) {
							if (bubbleLinks.get(i).getText().toLowerCase().contains(filter.toLowerCase()) && bubbleLinks.get(i - 1).getText().toLowerCase().contains(contentType.toLowerCase())) {
								report.updateTestLog("Verify " + filter + " node is present to the right of " + contentType, filter + " node is present to the right of " + contentType, Status.PASS);
								filterFlag = true;
								break;
							}
						}
					}
					if (!contentType1.equals("")) {
						if (bubbleLinks.get(bubbleLinks.size() - 1).getText().toLowerCase().contains(contentType1.toLowerCase())) {
							report.updateTestLog("Verify " + contentType1 + " node is present below  " + contentType, contentType1 + " node is present below " + contentType, Status.PASS);
							contentTypeFlag1 = true;
							break;
						}

					}

					if (contentTypeFlag && filterFlag && contentType1.equals(""))
						break;
					else if ((!contentType1.equals("")) && contentTypeFlag && filterFlag && contentTypeFlag1)
						break;
				}
			}
		}
		if (!contentTypeFlag)
			report.updateTestLog("Verify " + contentType1 + " node is present below  " + searchTerm, contentType + " node is not present to the right of " + searchTerm, Status.FAIL);
		if (!filterFlag && !filter.equals(""))
			report.updateTestLog("Verify " + filter + " node is present to the right of " + contentType, filter + " node is not present to the right of " + contentType, Status.FAIL);
		if (!contentTypeFlag1 && !contentType1.equals(""))
			report.updateTestLog("Verify " + contentType1 + " node is present below  " + contentType, contentType1 + " node is not present below " + contentType, Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the links present in the
	// popup.
	// # Function Name : verifyPopUpLink     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyPopUpLink(String lnkName) {
		boolean blnFlag = false;
		WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(lnkName.toLowerCase())) {
				report.updateTestLog("Verify " + lnkName + " is present in the Pop-Up", lnkName + " is present in the Pop-Up", Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify " + lnkName + " is present in the Pop-Up", lnkName + " is present in the Pop-Up", Status.FAIL);
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the text present in the
	// popup.
	// # Function Name : verifyPopUpText     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyPopUpText(String term) {
		WebElement popup = commonLibrary.isExist(UIMAP_ResearchMap.popup, 10);
		if (popup != null) {
			String popupText = popup.getText().toLowerCase();
			if (popupText.contains(term.toLowerCase()))
				report.updateTestLog("Verify RM popup details " + term, term + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify RM popup details " + term, term + " is NOT displayed", Status.FAIL);
		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a bubble link in a trail.
	// # Function Name : selectBubbleLinks     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectBubbleLinks(String searchTerm, String category) {
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
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on bubble link: " + category, category + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select document link in a trail.
	// # Function Name : clickDocumentLink     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap clickDocumentLink(String searchTerm, String docName) {

		boolean blnFlag = false, blnFlag1 = false;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				if (button != null) {
					if (!attachmentContent.getAttribute("class").contains("active"))
						commonLibrary.clickButtonParentWithWait(button, "Attachment Button");
					blnFlag1 = true;
					break;
				}
			}
		}
		if (blnFlag1) {
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					// List<WebElement> items =
					// commonLibrary.isExist_List(trail,
					// UIMAP_ResearchMap.activityDiv, 10);
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					WebElement doclink = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.doclnk, 10);
					// WebElement doclink =
					// commonLibrary.isExist_Negative(trail,UIMAP_ResearchMap.doclnk,
					// 10);
					if (doclink != null && doclink.isDisplayed()) {
						WebElement link = commonLibrary.isExistNegative(doclink, UIMAP_ResearchMap.lnkLinks, 10);
						commonLibrary.clickButtonLogSmallWait(link, docName);
						// try
						// {
						// driver.manage().timeouts().pageLoadTimeout(1,
						// TimeUnit.SECONDS);
						// commonLibrary.click_MouseMove_Action(link, docName);
						// // lnkDocument.click();// Using click ans click_JS
						// not working.
						// // report.updateTestLog("Click on the document " +
						// lnkDocument.getText(),
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
						blnFlag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Click on document link: " + docName, "Attachment link is NOT displayed", Status.FAIL);
		if (!blnFlag)
			report.updateTestLog("Click on document link: " + docName, docName + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a popup link.
	// # Function Name : selectPopUpLink     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectPopUpLink(String lnkName) {
		boolean blnFlag = false;
		WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(lnkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click " + lnkName + " present in the Pop-Up", lnkName + " is not present in the Pop-Up", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on List
	// # Function Name : clickList     
	// # Author : Divakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public History clickList() {
		WebElement listHistory = commonLibrary.isExistNegative(UIMAP_ResearchMap.listHistory, 10);
		commonLibrary.clickButtonParentWithWait(listHistory, "List");
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a link in a trail having
	// minimum 3 bubbles.
	// # Function Name : selectPopUpLink     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap selectBubbleLinks2(String searchTerm, String category) {
		boolean blnFlag = false;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().equals(searchTerm.toLowerCase())) {
				List<WebElement> lnkClassBubble = commonLibrary.isExistList(trail, UIMAP_SearchResult.lnkClassBubble, 20);
				if (lnkClassBubble.size() > 2) {

					for (WebElement item : lnkClassBubble) {
						if (item != null && item.getText().toLowerCase().equals(category.toLowerCase())) {
							commonLibrary.clickLinkWithWebElementWithWait(item, "BubbleLink :" + category);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on bubble link: " + category, category + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a single attachment link is
	// present in a trail.
	// # Function Name : verifySingleDoc     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifySingleDoc(String searchTerm) {
		boolean blnFlag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				//
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement attachmentContent1 = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.attachmentContent, 10);
				if (attachmentContent != null && attachmentContent1 == null) {
					report.updateTestLog("Verify Attachment icon is not connected to the right of part 1 of 23 document node.", "Attachment icon is not connected to the right of part 1 of 23 document node.", Status.PASS);
					blnFlag = true;
					break;
				}

			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify Attachment icon is not connected to the right of part 1 of 23 document node.", "Attachment icon is connected to the right of part 1 of 23 document node.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Product Logo
	// # Function Name : clickProductLogo     
	// # Author : Divakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Home clickProductLogo() {
		WebElement productLogo = commonLibrary.isExistNegative(UIMAP_ResearchMap.productLogo, 10);
		commonLibrary.clickLinkWithWebElementWithWait(productLogo, productLogo.getText());
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify vIew By filter has the given
	// text.
	// # Function Name : verifyViewByFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyViewByFilter(String text) {
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 20);
		WebElement viewByContent = commonLibrary.isExistNegative(sidebar, UIMAP_ResearchMap.viewByContent, 20);
		if (viewByContent.getText().replace("\n", " ").toLowerCase().contains(text.toLowerCase())) {
			report.updateTestLog("Verify " + text + " displays in View By Filter.", text + " displays in View By Filter.", Status.PASS);
		} else
			report.updateTestLog("Verify " + text + " displays in View By Filter.", text + " does not display in View By Filter.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify reset filters is present.
	// # Function Name : verifyResetFilterPresent     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyResetFilterPresent() {
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement resetFilters = commonLibrary.isExistNegative(sidebar, UIMAP_ResearchMap.resetFilters, 10);
		if (resetFilters != null && resetFilters.isEnabled())
			report.updateTestLog("Verify Reset map to default state link displays.", "Reset map to default state link displays.", Status.PASS);
		else
			report.updateTestLog("Verify Reset map to default state link displays.", "Reset map to default state link is not displayed.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify order of trails.
	// # Function Name : verifyTrailOrder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyTrailOrder(String trail1, String client, int pos) {
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		WebElement trailHeader = commonLibrary.isExistNegative(lstResearchThread.get(pos - 1), By.tagName("h4"), 10);
		if (trailHeader.getText().toLowerCase().contains(trail1.toLowerCase()) && lstResearchThread.get(pos - 1).getText().toLowerCase().contains(client.toLowerCase()))
			report.updateTestLog("Verify " + trail1 + "with client-id: " + client + " is present in pos: " + pos, trail1 + "with client-id: " + client + " is present in pos: " + pos, Status.PASS);
		else
			report.updateTestLog("Verify " + trail1 + "with client-id: " + client + " is present in pos: " + pos, trail1 + "with client-id: " + client + " is not present in pos: " + pos, Status.FAIL);

		// if(!trail2.equals(""))
		// {
		// trailHeader =
		// commonLibrary.isExist_Negative(lstResearchThread.get(1),
		// By.tagName("h4"), 10);
		// if(trailHeader.getText().toLowerCase().contains(trail2.toLowerCase()))
		// report.updateTestLog("Verify "+trail2+" is the second trail."
		// ,trail2+" is the second trail.",Status.PASS);
		// else
		// report.updateTestLog("Verify "+trail2+" is the second trail."
		// ,trail2+" is not the second trail.",Status.FAIL);
		// }
		//
		// if(!trail3.equals(""))
		// {
		// trailHeader =
		// commonLibrary.isExist_Negative(lstResearchThread.get(1),
		// By.tagName("h4"), 10);
		// if(trailHeader.getText().toLowerCase().contains(trail3.toLowerCase()))
		// report.updateTestLog("Verify "+trail3+" is the third trail."
		// ,trail3+" is the third trail.",Status.PASS);
		// else
		// report.updateTestLog("Verify "+trail3+" is the third trail."
		// ,trail3+" is not the third trail.",Status.FAIL);
		// }
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select links from Document pop up.
	// # Function Name : selectPopUpLinkDocument     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectPopUpLinkDocument(String lnkName) {
		boolean blnFlag = false;
		WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup1, 10);
		WebElement popupUL = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.popupUL, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popupUL, UIMAP_ResearchMap.lstTagName, 10);

		for (int iterator = 0; iterator < popuplinks.size(); iterator++) {

			List<WebElement> popupanchortag = commonLibrary.isExistList(popuplinks.get(iterator), UIMAP_ResearchMap.buttonTag, 10);
			for (WebElement link : popupanchortag) {
				if (link.getText().toLowerCase().contains(lnkName.toLowerCase())) {
					if (browsername.contains("internet")) {
						try {
							driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
							// lnkDocument.click();// Using click ans click_JS
							// not working.
							// report.updateTestLog("Click on the document " +
							// lnkDocument.getText(),
							// "Not Clicked  on the document " +
							// lnkDocument.getText(), Status.PASS);;
						} catch (TimeoutException ex) {
							driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
							System.out.println(ex.toString());

						}
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						blnFlag = true;
						break;
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
						blnFlag = true;
						break;
					}
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click " + lnkName + " present in the Pop-Up", lnkName + " is not present in the Pop-Up", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change order of trail by applying
	// view by filter.
	// # Function Name : changeOrderOfTrail     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap changeOrderOfTrail(String option) {
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement viewByContent = commonLibrary.isExistNegative(sidebar, UIMAP_ResearchMap.viewByContent, 10);
		WebElement changeOrderOfTrail = commonLibrary.isExistNegative(viewByContent, UIMAP_ResearchMap.lnkLinks, 10);
		commonLibrary.clickLinkWithWebElementWithWait(changeOrderOfTrail, changeOrderOfTrail.getText());
		viewByContent = commonLibrary.isExistNegative(UIMAP_ResearchMap.viewByContent, 10);
		WebElement viewByPopUp = commonLibrary.isExistNegative(viewByContent, UIMAP_ResearchMap.viewByPopUp, 10);
		boolean blnFlag = false;
		if (viewByPopUp != null && viewByPopUp.isDisplayed()) {
			report.updateTestLog("Verify View By pop-up displays.", "View By pop-up displays.", Status.PASS);
			List<WebElement> labels = commonLibrary.isExistList(viewByPopUp, UIMAP_ResearchMap.labels, 10);
			for (WebElement item : labels) {
				if (item.getText().toLowerCase().contains(option.toLowerCase())) {
					WebElement radio = commonLibrary.isExistNegative(item, UIMAP_ResearchMap.radioButton, 10);
					commonLibrary.setRadioButton(radio, option);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag)
				report.updateTestLog("Click on radio button " + option, "radio button " + option + " is not present.", Status.FAIL);

			boolean blnFlag1 = false;
			List<WebElement> buttons = commonLibrary.isExistList(viewByPopUp, UIMAP_ResearchMap.button, 10);
			for (WebElement item : buttons) {
				if (item.getText().toLowerCase().contains("ok")) {
					commonLibrary.clickButtonParentWithWait(item, "OK");
					blnFlag1 = true;
					break;
				}
			}
			if (!blnFlag1)
				report.updateTestLog("Click on OK button ", "OK button is not present.", Status.FAIL);

		} else
			report.updateTestLog("Verify View By pop-up displays.", "View By pop-up does not display.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the selected client filter.
	// # Function Name : verifyClientFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyClientFilter(String text) {
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement clientContent = commonLibrary.isExistNegative(sidebar, UIMAP_ResearchMap.clientContent, 10);
		if (clientContent.getText().toLowerCase().replace("\n", " ").contains(text.toLowerCase())) {
			report.updateTestLog("Verify " + text + " displays in Client Filter.", text + " displays in Client Filter.", Status.PASS);
		} else
			report.updateTestLog("Verify " + text + " displays in Client Filter.", text + " does not display in Client Filter.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change client filter.
	// # Function Name : changeClientFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap changeClientFilter(String option, String option1) {
		try {
			commonLibrary.sleep(3000);
		} catch (Exception e) {
		}
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement clientContent = commonLibrary.isExistNegative(sidebar, UIMAP_ResearchMap.clientContent, 10);
		WebElement changeClient = commonLibrary.isExistNegative(clientContent, UIMAP_ResearchMap.lnkLinks, 10);
		commonLibrary.clickLinkWithWebElementWithWait(changeClient, changeClient.getText());
		clientContent = commonLibrary.isExistNegative(UIMAP_ResearchMap.clientContent, 10);
		WebElement clientPopUp = commonLibrary.isExistNegative(clientContent, UIMAP_ResearchMap.viewByPopUp, 10);
		commonLibrary.highlightElement(clientPopUp);
		boolean blnFlag = false, blnFlag1 = false;
		if (clientPopUp != null && clientPopUp.isDisplayed()) {
			report.updateTestLog("Verify Client pop-up displays.", "Client pop-up displays.", Status.PASS);
			WebElement viewAll = commonLibrary.isExistNegative(clientPopUp, UIMAP_ResearchMap.viewAll, 10);
			commonLibrary.setCheckBox(viewAll, false);

			List<WebElement> checkList = commonLibrary.isExistList(clientPopUp, UIMAP_ResearchMap.lstTagName, 10);

			for (WebElement item : checkList) {
				if (item.getText().toLowerCase().equals(option.toLowerCase())) {
					WebElement check = commonLibrary.isExistNegative(item, UIMAP_ResearchMap.input, 10);
					commonLibrary.setCheckBox(check, true);
					blnFlag = true;

				} else if (item.getText().toLowerCase().equals(option1.toLowerCase())) {
					WebElement check = commonLibrary.isExistNegative(item, UIMAP_ResearchMap.input, 10);
					commonLibrary.setCheckBox(check, true);
					blnFlag1 = true;

				}
				if (blnFlag && blnFlag1)
					break;
			}
			if (!blnFlag)
				report.updateTestLog("Select Checkbox " + option, "CheckBox " + option + " is not present.", Status.FAIL);

			if (!blnFlag1)
				report.updateTestLog("Select Checkbox " + option1, "CheckBox " + option1 + " is not present.", Status.FAIL);

			blnFlag1 = false;
			List<WebElement> buttons = commonLibrary.isExistList(clientPopUp, UIMAP_ResearchMap.button, 10);
			for (WebElement item : buttons) {
				if (item.getText().toLowerCase().contains("ok")) {
					commonLibrary.clickButtonParentWithWait(item, "OK");
					blnFlag1 = true;
					break;
				}
			}
			if (!blnFlag1)
				report.updateTestLog("Click on OK button ", "OK button is not present.", Status.FAIL);

		} else
			report.updateTestLog("Verify Client pop-up displays.", "Client pop-up does not display.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify trail absent.
	// # Function Name : verifyTrailAbsent     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyTrailAbsent(String searchTerm) {
		boolean blnFlag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				report.updateTestLog("Verify trail: " + searchTerm + " is absent.", "Trail " + searchTerm + "is present.", Status.FAIL);
				blnFlag = true;
				break;

			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify trail: " + searchTerm + " is absent.", "Trail " + searchTerm + "is absent.", Status.PASS);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set date filter to current date.
	// # Function Name : selectCurrentDate     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap selectCurrentDate() {

		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);

		now = cal.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
		String DateToday = format.format(now);

		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement dateFilter = commonLibrary.isExist(sidebar, UIMAP_ResearchMap.dateFilter, 10);
		WebElement dateRange = commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
		if (dateRange != null) {
			if (browsername.equalsIgnoreCase("internet explorer"))
				commonLibrary.clickJS(dateRange, "Change Date Range");
			else
				commonLibrary.clickLinkWithWebElementWithWait(dateRange, "Change Date Range");
		}

		WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
		List<WebElement> months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
		WebElement today = commonLibrary.isExist(months.get(months.size() - 1), By.cssSelector("td[data-date='" + DateToday + "']"), 10);
		WebElement link = commonLibrary.isExistNegative(today, UIMAP_ResearchMap.lnkLinks, 10);
		commonLibrary.clickLinkWithWebElementWithWait(link, DateToday);

		List<WebElement> buttons = commonLibrary.isExistList(dateFilter, By.tagName("button"), 10);
		for (WebElement item : buttons) {
			if (item.getText().contains("OK")) {
				commonLibrary.clickButtonParentWithWait(item, "OK");
				break;
			}
		}

		dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
		if (dateFilter.getText().toLowerCase().replace("\n", " ").contains(("Date " + format1.format(now)).toLowerCase()))
			report.updateTestLog("Verify date filter: 'Date " + format1.format(now) + "'", "Date filter: 'Date " + format1.format(now) + "' is present.", Status.PASS);
		else
			report.updateTestLog("Verify date filter: 'Date " + format1.format(now) + "'", "Date filter: 'Date " + format1.format(now) + "' is not present.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to deselect all trails from Show Trails
	// section.
	// # Function Name : deselectAllTrails     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap deselectAllTrails() {
		boolean blnFlag = false;
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement reseachFilter = commonLibrary.isExist(sidebar, UIMAP_ResearchMap.reseachFilter, 20);
		WebElement viewAll1 = commonLibrary.isExistNegative(reseachFilter, UIMAP_ResearchMap.viewAll1, 10);
		commonLibrary.setCheckBox(viewAll1, false);

		reseachFilter = commonLibrary.isExist(UIMAP_ResearchMap.reseachFilter, 20);
		List<WebElement> checkList = commonLibrary.isExistList(reseachFilter, UIMAP_ResearchMap.input, 10);
		for (WebElement item : checkList) {
			if (item.isSelected()) {
				report.updateTestLog("Verify all checkboxes are unchecked.", "Checkbox " + commonLibrary.getParentElement(item).getText() + " is selected.", Status.FAIL);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify all checkboxes are unchecked.", "All checkboxes are unchecked.", Status.PASS);

		WebElement researchThread = commonLibrary.isExistNegative(UIMAP_SearchResult.lstResearchThread, 10);
		if (researchThread == null)
			report.updateTestLog("Verify no trails are displayed in the right pane.", "No trails are displayed in the right pane.", Status.PASS);
		else
			report.updateTestLog("Verify no trails are displayed in the right pane.", "Trails are displayed in the right pane.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select particular trail from Show
	// Trails section.
	// # Function Name : selectTrail     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap selectTrail(String searchTerm) {
		boolean blnFlag = false;
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement reseachFilter = commonLibrary.isExist(sidebar, UIMAP_ResearchMap.reseachFilter, 20);
		List<WebElement> checkList = commonLibrary.isExistList(reseachFilter, UIMAP_ResearchMap.lstTagName, 10);
		for (WebElement item : checkList) {
			if (item.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement check = commonLibrary.isExistNegative(item, UIMAP_ResearchMap.input, 10);
				commonLibrary.setCheckBox(check, true);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Check the check box for " + searchTerm + " under Show Trails.", "Check box " + searchTerm + " is not present.", Status.FAIL);

		WebElement researchThread = commonLibrary.isExistNegative(UIMAP_SearchResult.lstResearchThread, 10);
		WebElement trailHeader = commonLibrary.isExistNegative(researchThread, By.tagName("h4"), 10);

		if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase()))
			report.updateTestLog("Verify trail for " + searchTerm + " displays.", "Trail for " + searchTerm + " displays.", Status.PASS);
		else
			report.updateTestLog("Verify trail for " + searchTerm + " displays.", "Trail for " + searchTerm + " does not displays.", Status.PASS);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset filters and verify default
	// values.
	// # Function Name : resetFilters     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public ResearchMap resetFilters() {
		WebElement sidebar = commonLibrary.isExistNegative(UIMAP_ResearchMap.sidebar, 10);
		WebElement resetFilters = commonLibrary.isExist(sidebar, UIMAP_ResearchMap.resetFilters, 20);
		commonLibrary.clickLinkWithWebElementWithWait(resetFilters, resetFilters.getText());
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));

		verifyViewByFilter("View by: Last modified date");
		verifyClientFilter("Client: All");

		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);

		now = cal.getTime();

		cal.add(Calendar.DATE, -6);

		Date now1 = cal.getTime();

		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
		String dateToday = format.format(now);
		String dateBefore = format.format(now1);

		WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);

		if (dateFilter.getText().toLowerCase().replace("\n", " ").contains(("Date " + dateBefore + " - " + dateToday).toLowerCase()))
			report.updateTestLog("Verify date filter: Date " + dateBefore + " - " + dateToday + " displays.", "Date filter: Date " + dateBefore + " - " + dateToday + " displays.", Status.PASS);
		else
			report.updateTestLog("Verify date filter: Date " + dateBefore + " - " + dateToday + " displays.", "Date filter: Date " + dateBefore + " - " + dateToday + " does not displays.", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify range of date filter
	// # Function Name : verifydateFilter    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifydateFilter(int filter, String endDate) {
		try {

			Calendar cal = Calendar.getInstance();
			Date now = new Date();
			cal.setTime(now);

			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");

			Date end = format.parse(endDate);

			String DateToday = format.format(now);
			String endD = format.format(end);

			WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
			WebElement filteredDate = commonLibrary.isExist(dateFilter, UIMAP_ResearchMap.dateFiltered, 10);

			String filtDate = filteredDate.getText();// Apr 10, 2015 - Apr 24,
														// 2015
			if (endD.equalsIgnoreCase(DateToday) && filter == 1) {
				if (DateToday.equalsIgnoreCase(filteredDate.getText()))
					report.updateTestLog("Today's date is displayed in the left pane section in the date filter", "Today's date is displayed in the left pane section in the date filter", Status.PASS);
				else
					report.updateTestLog("Today's date is displayed in the left pane section in the date filter", "Today's date is not displayed in the left pane section in the date filter", Status.FAIL);
			} else {
				String fromdate = filtDate.substring(0, 12);
				String toDate = filtDate.substring(15);

				Date from = format.parse(fromdate);
				Date to = format.parse(toDate);

				long diff = to.getTime() - from.getTime();

				long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

				if (endD.equalsIgnoreCase(DateToday)) {

					if ((toDate.equalsIgnoreCase(DateToday)) && (days + 1 == filter))
						report.updateTestLog("Verify date filter is set from " + filter + " days till today", "Date filter is set from " + filter + " days till today", Status.PASS);
					else
						report.updateTestLog("Verify date filter is set from " + filter + " days till today", "Date filter is not set from " + filter + " days till today", Status.FAIL);
				} else {
					if ((toDate.equalsIgnoreCase(endDate)) && (days + 1 == filter))
						report.updateTestLog("Verify date filter is set from " + filter + " days till " + endD, "Date filter is set from " + filter + " days till " + endD, Status.PASS);
					else
						report.updateTestLog("Verify date filter is set from " + filter + " days till " + endD, "Date filter is not set from " + filter + " days till " + endD, Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify floating area
	// # Function Name : verifyFloatingArea    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyFloatingArea() {
		commonLibrary.scrollUp();
		WebElement toolBar = commonLibrary.isExist(UIMAP_ResearchMap.toolBar, 10);
		String beforeScroll = toolBar.getAttribute("class");
		commonLibrary.scrollDown();
		toolBar = commonLibrary.isExist(UIMAP_ResearchMap.toolBar, 10);
		String afterScroll = toolBar.getAttribute("class");
		if ((!(beforeScroll.equalsIgnoreCase(afterScroll))) && afterScroll.contains("sticky"))
			report.updateTestLog("Verify floating area is displayed", "Floating area is displayed", Status.PASS);
		else
			report.updateTestLog("Verify floating area is displayed", "Floating area is not displayed", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Jump icon near any trial
	// under Show Trials
	// # Function Name : clickJumpIcon    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickJumpIcon(int num) {
		WebElement allTrials = commonLibrary.isExist(UIMAP_ResearchMap.allTrials, 10);
		List<WebElement> trials = commonLibrary.isExistList(allTrials, By.tagName("li"), 20);
		WebElement trial = commonLibrary.isExist(trials.get(num), UIMAP_ResearchMap.jumpIcon, 20);
		commonLibrary.clickJS(trial, "Jump Icon");
		report.updateTestLog("The page is scrolled to trial number " + num, "The page is scrolled to trial number " + num, Status.PASS);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on change date range link
	// # Function Name : clickChangeDateRange    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickChangeDateRange() {
		WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
		WebElement dateRange = commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
		if (dateRange != null) {
			commonLibrary.clickJS(dateRange, "Change Date Range");

		}
		WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
		if (dateFilterPopUp != null)
			report.updateTestLog("Verify calendar is displayed", "Calendar is displayed", Status.PASS);
		else
			report.updateTestLog("Verify calendar is displayed", "Calendar is not displayed", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on change date range link
	// # Function Name : clickChangeDateRange    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyDateRangeActive(int range, String endDate) {
		try {
			// Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			new Date();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date end = format.parse(endDate);
			cal1.setTime(end);

			WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
			if (dateFilterPopUp != null) {
				WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
				commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
				List<WebElement> months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
				boolean flagRange = false;
				cal1.add(Calendar.DATE, -range + 1);
				Date from = cal1.getTime();
				String fromDate = format.format(from);
				int daysCount = 0;
				for (int i = months.size() - 1; i > 0; i--) {
					List<WebElement> date = commonLibrary.isExistList(months.get(i), UIMAP_ResearchMap.activeDates, 10);
					daysCount = daysCount + date.size();
					for (WebElement item : date) {
						if (item.getAttribute("data-date").equalsIgnoreCase(fromDate)) {
							flagRange = true;
							break;
						}
					}

				}
				if ((daysCount == range) && flagRange)
					report.updateTestLog("Each date with the " + range + " day range selected are bordered with green color.", "Each date with the " + range + " day range selected are bordered with green color.", Status.PASS);
				else
					report.updateTestLog("Each date with the " + range + " day range selected are bordered with green color.", "Each date with the " + range + " day range selected are not bordered with green color.", Status.FAIL);

			} else
				report.updateTestLog("Verify calendar is displayed", "Calendar is not displayed", Status.FAIL);
		} catch (Exception e) {
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the color of a date as per
	// the search count performed on the particular date
	// # Function Name : verifyDateSearchCountColor    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyDateSearchCountColor(String date, String countColor) {
		boolean flag = false;

		WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
		if (dateFilterPopUp != null) {
			WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
			commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
			List<WebElement> months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
			WebElement today = commonLibrary.isExist(months.get(months.size() - 1), By.cssSelector("td[data-date='" + date + "']"), 10);
			if (countColor != null) {
				String color = today.getAttribute("class");
				switch (countColor) {
				case "none": {
					if (color.contains("active"))
						flag = true;
					break;
				}
				case "small": {
					if (color.contains("small"))
						flag = true;
					break;
				}
				case "medium": {
					if (color.contains("medium"))
						flag = true;
					break;
				}
				case "large": {
					if (color.contains("large"))
						flag = true;
					break;
				}
				}
				if (flag)
					report.updateTestLog("Verify the date is highlighted with the color for " + color, "The date is highlighted with the color for " + countColor, Status.PASS);
				else
					report.updateTestLog("Verify the date is highlighted with the color for " + color, "The date is highlighted with the color for " + countColor, Status.FAIL);

			}
		} else
			report.updateTestLog("Verify calendar is displayed", "Calendar is not displayed", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the date range to set filter
	// # Function Name : selectDateRange    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectDateRange(String start, int days) {
		try {
			Calendar cal = Calendar.getInstance();
			Date now = new Date();
			cal.setTime(now);

			now = cal.getTime();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
			String DateToday = format.format(now);
			format1.format(now);

			String dateToSelect[] = new String[days];

			Calendar cal1 = Calendar.getInstance();
			WebElement dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
			if (dateFilterPopUp != null) {
				WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
				commonLibrary.isExist(dateFilter, By.tagName("a"), 10);
				List<WebElement> months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
				WebElement today = commonLibrary.isExist(months.get(months.size() - 1), By.cssSelector("td[data-date='" + DateToday + "']"), 10);
				if (start.equalsIgnoreCase(DateToday) && days == 1) {
					WebElement todayLink = commonLibrary.isExist(today, By.tagName("a"), 10);
					commonLibrary.clickLinkWithWebElementWithWait(todayLink, "Today's date");
				}

				else if (start != null) {
					Date startDate = format.parse(start);
					cal1.setTime(startDate);
					dateToSelect[0] = format.format(startDate);
					for (int i = 1; i < days; i++) {
						cal1.add(Calendar.DATE, -1);
						Date next = cal1.getTime();
						dateToSelect[i] = format.format(next);
					}

					dateFilterPopUp = commonLibrary.isExist(UIMAP_ResearchMap.dateFiltersideBar, 10);
					if (dateFilterPopUp != null) {
						months = commonLibrary.isExistList(dateFilterPopUp, UIMAP_ResearchMap.calendar, 10);
						// Robot robot = new Robot();
						for (int d = months.size() - 1; d > 0; d--) {
							WebElement startdate = commonLibrary.isExist(months.get(d), By.cssSelector("td[data-date='" + dateToSelect[0] + "']"), 5);
							if (startdate != null) {
								WebElement startdatelink = commonLibrary.isExist(startdate, By.tagName("a"), 10);
								commonLibrary.clickJS(startdatelink, start);
								break;
							}
						}
						Actions action = new Actions(driver);

						for (int i = 0; i < months.size(); i++) {
							WebElement endDate = commonLibrary.isExist(months.get(i), By.cssSelector("td[data-date='" + dateToSelect[days - 1] + "']"), 5);
							if (endDate != null) {
								WebElement endDateLink = commonLibrary.isExist(endDate, By.tagName("a"), 10);
								action.keyDown(Keys.SHIFT).click(endDateLink).keyUp(Keys.SHIFT).build().perform();
								break;
							}
						}
					}
				}

				int range = 0;
				if (days <= 30)
					range = days;
				else {
					range = 30;
					report.updateTestLog("Only 30 days wil be selected to a maximum if more than 30 days are selected", "Only 30 days wil be selected to a maximum if more than 30 days are selected", Status.PASS);
				}

				verifyDateRangeActive(range, start);
			} else
				report.updateTestLog("Verify calendar is displayed", "Calendar is displayed", Status.PASS);
		} catch (Exception e) {
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click OK button in Date Filter
	// # Function Name : clickOKDateRange    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickOKDateRange() {
		WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
		List<WebElement> buttons = commonLibrary.isExistList(dateFilter, By.tagName("button"), 10);
		for (WebElement item : buttons) {
			if (item.getText().contains("OK")) {
				commonLibrary.clickButtonParentWithWait(item, "OK");
				commonLibrary.sleep(10000);
				break;
			}
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Cancel button in Date Filter
	// # Function Name : clickCancelDateRange    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickCancelDateRange() {
		WebElement dateFilter = commonLibrary.isExist(UIMAP_ResearchMap.dateFilter, 10);
		List<WebElement> buttons = commonLibrary.isExistList(dateFilter, By.tagName("button"), 10);
		for (WebElement item : buttons) {
			if (item.getText().contains("Cancel")) {
				commonLibrary.clickButtonParentWithWait(item, "Cancel");
				break;
			}
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify No Trials are displayed
	// message
	// # Function Name : verifyNoTrials    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyNoTrials() {
		WebElement notrials = commonLibrary.isExist(UIMAP_ResearchMap.noTrials, 10);
		if (notrials != null && notrials.getText().contains("No Trails to Display"))
			report.updateTestLog("No trials to display message displays in RM canvas", "No trials to display message displays in RM canvas", Status.PASS);
		else
			report.updateTestLog("No trials to display message displays in RM canvas", "No trials to display message is not displayed in RM canvas", Status.WARNING);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple Search
	// # Function Name : simpleSearch    
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function Add To Folder With Title
	// # Function Name : RMAddToFolderWithTitle    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap rmAddToFolderWithTitle(String folderName, String folderType, String excelData, String strdoctitle) {
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null)

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

		// CLICK ON <<<CREATE NEW FOLDER>>>

		WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
		if (createNewFolder != null)
			commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

		// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

		WebElement folderNam = commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
		if (folderNam != null)
			// folderNam.sendKeys(folderName);
			// report.updateTestLog("Enter "+folderName+" in Folder Name text box",folderName+" is entered in Folder Name text box"
			// , Status.PASS);
			commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

		// CLICK ON <<<CREATE>>>
		WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
		if (create != null)
			commonLibrary.clickButtonParentWithWait(create, "Create");

		// To change document title
		WebElement documenttitle = commonLibrary.isExist(UIMAP_ResearchMap.folderDocTitle, 10);
		if (documenttitle != null)
			// folderNam.sendKeys(folderName);
			// report.updateTestLog("Enter "+folderName+" in Folder Name text box",folderName+" is entered in Folder Name text box"
			// , Status.PASS);
			documenttitle.clear();
		commonLibrary.setDataInTextBox(documenttitle, strdoctitle, "FolderName");
		// click on the radio buttons PDF/Live Map
		if (folderType.equalsIgnoreCase("PDF")) {
			WebElement pdfRadio = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderPDF, 10);
			commonLibrary.setRadioButton(pdfRadio, "PDF");
		} else if (folderType.equalsIgnoreCase("Live Map")) {
			WebElement liveMap = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderLiveMap, 10);
			commonLibrary.setRadioButton(liveMap, "Live Map");
		}

		// To write the docment title to excel
		if (excelData != "") {
			String sheetNam = excelData.split(";")[0];
			int row = Integer.parseInt((excelData.split(";")[1]));
			String column = excelData.split(";")[2];

			WebElement doctitle = commonLibrary.isExist(UIMAP_ResearchMap.folderDocTitle, 10);
			String docTit = doctitle.getAttribute("value");
			String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
			excel = new ExcelDataAccess(datatablePath, sheetNam);
			excel.setDatasheetName("General_Data");
			excel.setValue(row, column, docTit);
		}

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify searchTerm in Popup
	// # Function Name : searchTermVerifyPopup    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap searchTermVerifyPopup(String searchTerm, String category, String content) {
		boolean blnFlag = false;
		boolean flag = false;
		String popupContent = "";
		String[] cont;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
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
	// # Function Description : Function to click on Link in Popup CSR
	// # Function Name : clickLink_PopupCSR    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickLink_PopupCSR(String linkName) {
		boolean blnFlag = false;
		WebElement popup1 = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.lnkLinks, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, linkName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on CSR
	// # Function Name : clickCSR    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickCSR() {
		WebElement viewCSRButton = commonLibrary.isExist(UIMAP_ResearchMap.CSRButton, 20);
		if (viewCSRButton != null)
			commonLibrary.clickButtonParentWithWait(viewCSRButton, viewCSRButton.getText());
		report.updateTestLog("Click Compare Search Results", "Compare Search Results button is clicked'", Status.DONE);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click CSR Under Popup
	// # Function Name : clickCSRUnderPopup  
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Search clickCSRUnderPopup() {
		WebElement viewCSRButtonUnderPopup = commonLibrary.isExist(UIMAP_ResearchMap.CSRButtonUnderPopup, 20);
		if (viewCSRButtonUnderPopup != null)
			commonLibrary.clickButtonParentWithWait(viewCSRButtonUnderPopup, viewCSRButtonUnderPopup.getText());
		report.updateTestLog("Click Compare Search Results under popup", "Compare Search Results button under popup is clicked'", Status.DONE);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To History FooterLink in
	// ResearchMapNew
	// # Function Name : NavigateToHistoryFooterLink_ResearchMapNew  
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToHistoryFooterLinkResearchMapNew(String strLinkName) {
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
					commonLibrary.clickLinkWithWebElement(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtResearchMap, "Research Map");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Trail in ResearchMap
	// # Function Name : verifyTrail_RM  
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyTrail_RM(String searchTerm) {
		// String dateAndTimeFormat = "";
		String clientText = "Client";

		boolean flag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.resultListDiv, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			// WebElement trailVerifyDateAndTime =
			// commonLibrary.isExist_Negative(trail, By.tagName("h5"), 10);
			WebElement trailVerifyClient = commonLibrary.isExistNegative(trail, By.tagName("h6"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase()) && trailVerifyClient.getText().toLowerCase().contains(clientText.toLowerCase())) {
				flag = true;
				break;
			}
			if (flag)
				break;
		}
		if (flag)
			report.updateTestLog("Verify trails 'accident' and 'Theft' in ResearchMap", "'accident' and 'Theft' Trails verified in ResearchMap", Status.PASS);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify SearchTerm Not Exist
	// # Function Name : verifySearchTermNotExist  
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifySearchTermNotExist(String searchTerm) {
		boolean flag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				flag = false;
			} else {
				flag = true;
			}
			if (flag)
				break;
		}
		if (flag)
			report.updateTestLog("Verify " + searchTerm + " exists", searchTerm + "not exists", Status.PASS);
		else
			report.updateTestLog("Verify " + searchTerm + " exists", searchTerm + " exists", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Document And Verify PDF
	// # Function Name : clickDocAndVerifyPDF  
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickDocAndVerifyPDF(String docTitle) {

		try {

			String filePath = dataTable.getData("General_Data", "FilePath");

			// WebElement historyListContainer =
			// commonLibrary.isExist_Negative(UIMAP_SearchResult.historyListContainer,
			// 10);
			// List<WebElement> listItems =
			// commonLibrary.isExist_List(historyListContainer,
			// UIMAP_SearchResult.listItems, 10);
			// for(WebElement item:listItems)
			// {
			// WebElement title = commonLibrary.isExist(item,
			// UIMAP_SearchResult.TitleClassDoc, 10);
			// if(title.getText().toLowerCase().contains("print: "+docTitle.toLowerCase()))
			// {
			// WebElement link=commonLibrary.isExist(title,
			// UIMAP_SearchResult.link, 10);
			// commonLibrary.clickButton_Parent_WithWait(link, link.getText());
			// blnFlag1=true;
			// break;
			// }
			// }
			//
			// if(blnFlag1)
			// {
			WebElement historyPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.historyPopup, 10);
			WebElement deliveryLink = commonLibrary.isExist(historyPopup, UIMAP_SearchResult.link, 10);
			String docNames = deliveryLink.getText().trim();
			String docName[] = docNames.split(".PDF");
			docName[0] = docName[0].replace(docName[0].substring(docName[0].length() - 4), "");

			String path1 = filePath;
			commonLibrary.fileExistsDelete(path1, docName[0]);
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
			// this.PDFVerification_Path(DocPath, docTitle,"Exist");

			historyPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.historyPopup, 10);
			WebElement historyPopupClose = commonLibrary.isExist(historyPopup, UIMAP_SearchResult.historyPopupClose, 10);
			commonLibrary.clickButtonParentWithWait(historyPopupClose, "Close");
			commonLibrary.sleep(3000);
		} catch (Exception e) {

		}

		// else
		// {
		// report.updateTestLog(
		// "Click Document Link from History Page",
		// "Document Link not present.",
		// Status.FAIL);
		// }

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Bubble Links
	// # Function Name : selectBubbleLinks3  
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selectBubbleLinks3(String searchTerm, String category) {
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
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on bubble link: " + category, category + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link in Popup
	// # Function Name : clickLink_Popup1  
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickLink_Popup1(String linkName) {
		boolean blnFlag = false;
		WebElement popup1 = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.lnkLinks, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, linkName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to header title and click pop links
	// # Function Name : VerifydocLinkdetails     
	// # Author : karthi
	// # Date Created : Apr 15
	// #*****************************************************************************************************************************

	public ResearchMap verifydocLinkdetails(String searchTerm, String docName1, String docName2, String docName3, String Verifytext, String verifyframetext) {

		String[] doclink = docName1.split(";");
		String[] docdetails = docName2.split(";");
		String[] docframlink = docName3.split(";");
		String[] docframeverify = verifyframetext.split(";");
		boolean blnFlag = false, blnFlag1 = false;

		selectRMNodeVerifyPopup(searchTerm, searchTerm, searchTerm);
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {

			WebElement trailHeader = commonLibrary.isExistNegative(trail, UIMAP_SearchResult.header4, 10);
			System.out.println(trailHeader.getText());
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				if (button != null) {
					if (!attachmentContent.getAttribute("class").contains("active"))
						commonLibrary.clickButtonParentWithWait(button, "Attachment Button");
					blnFlag1 = true;
					break;
				}
			}
		}

		if (blnFlag1) {
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, UIMAP_SearchResult.header4, 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					// List<WebElement> items =
					// commonLibrary.isExist_List(trail,
					// UIMAP_ResearchMap.activityDiv, 10);
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);

					List<WebElement> doclinks = commonLibrary.isExistList(attachmentContent, UIMAP_ResearchMap.doclnk1, 10);

					if (doclinks != null) {
						for (int i = 0; i <= 1; i++) {
							commonLibrary.clickButtonParentWithWait(doclinks.get(i), doclink[i]);
							verifyframedocdetails(docdetails[i], docframlink[i]);
							blnFlag = true;

							commonLibrary.sleep(5000);

							closedocframe();
							// break;
						}
						break;
					}
				}
			}
		}

		// if(blnFlag1)
		// {
		lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, UIMAP_SearchResult.header4, 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				// List<WebElement> items = commonLibrary.isExist_List(trail,
				// UIMAP_ResearchMap.activityDiv, 10);
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				List<WebElement> buttons = commonLibrary.isExistList(attachmentContent, UIMAP_ResearchMap.button, 10);

				if (buttons.get(1).getText().equals("1")) {
					// if(!attachmentContent.getAttribute("class").contains("active"))
					commonLibrary.clickButtonParentWithWait(buttons.get(1), "Attachment Button");
					blnFlag1 = true;
					break;
				}
			}
		}

		// }

		if (blnFlag1) {
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					// List<WebElement> items =
					// commonLibrary.isExist_List(trail,
					// UIMAP_ResearchMap.activityDiv, 10);
					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.activeAttachmentContent, 10);

					List<WebElement> doclinks = commonLibrary.isExistList(attachmentContent, UIMAP_ResearchMap.doclnk1, 10);

					if (doclinks != null) {

						commonLibrary.clickButtonParentWithWait(doclinks.get(3), Verifytext);
						verifyframedocdetails(Verifytext, docframeverify[0]);
						verifyframedocdetails(Verifytext, docframeverify[1]);
						verifyframedocdetails(Verifytext, docframeverify[2]);
						verifyframedocdetails(Verifytext, docframeverify[3]);
						verifyframedocdetails(Verifytext, docframeverify[4]);
						blnFlag = true;

						commonLibrary.sleep(5000);

						closedocframe();

						break;

					}
				}
			}
		}

		if (!blnFlag)
			report.updateTestLog("Click on document link: " + docName1, docName1 + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify frame documets details
	// # Function Name : verifyframedocdetails     
	// # Author : karthi
	// # Date Created : Apr 15
	// #*****************************************************************************************************************************

	public ResearchMap verifyframedocdetails(String strlink, String linkaction) {
		boolean blnFlag = false;
		;
		WebElement div = commonLibrary.isExist(By.cssSelector("div[class*='researchThreadPopup']"), 10);
		if (div != null) {

			WebElement divframe = commonLibrary.isExist(div, By.cssSelector("div[class='frame']"), 20);
			if (divframe != null) {

				List<WebElement> contentlist = commonLibrary.isExistList(divframe, By.tagName("p"), 10);
				for (WebElement content : contentlist) {

					if (content.getText().contains(strlink)) {
						blnFlag = true;
						break;
					}

				}
			}
			List<WebElement> Contenlinks = commonLibrary.isExistList(divframe, By.tagName("a"), 10);
			for (WebElement contentlink : Contenlinks) {

				if (contentlink.getText().contains(linkaction)) {
					blnFlag = true;
					break;
				}

			}

		}

		if (blnFlag) {
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", linkaction + " link  verfied.", Status.PASS);
		} else {
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", linkaction + " link not  verfied.", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close document frame
	// # Function Name : closedocframe     
	// # Author : karthi
	// # Date Created : Apr 15
	// #*****************************************************************************************************************************

	public ResearchMap closedocframe() {

		WebElement doclcose = commonLibrary.isExist(By.cssSelector("a[class='closePopup icon la-CloseRemove']"));
		if (doclcose != null) {
			commonLibrary.clickButtonParentWithWaitJS(doclcose, "closedoc");
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add new folder in the History
	// # Function Name : AddToFolder    
	// # Author : updated:karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap addToFolderHistory(String folderName, String folderType, String excelData, String strdoctitle)

	{
		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null) {

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder == null) {
				int count = 0;
				do {
					count++;
					chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
					if (chooseFolder == null)
						commonLibrary.sleep(5000);
				} while (chooseFolder == null && count < 8);
			}
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

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

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			WebElement folderNam = commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
			if (folderNam != null)
				// folderNam.sendKeys(folderName);
				// report.updateTestLog("Enter "+folderName+" in Folder Name text box",folderName+" is entered in Folder Name text box"
				// , Status.PASS);
				commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				commonLibrary.clickButtonParentWithWait(create, "Create");

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

			// To change document title
			WebElement documenttitle = commonLibrary.isExist(UIMAP_ResearchMap.folderDocTitle, 10);
			if (documenttitle != null)
				// folderNam.sendKeys(folderName);
				// report.updateTestLog("Enter "+folderName+" in Folder Name text box",folderName+" is entered in Folder Name text box"
				// , Status.PASS);
				documenttitle.clear();
			commonLibrary.setDataInTextBox(documenttitle, strdoctitle, "FolderName");
			// click on the radio buttons PDF/Live Map
			if (folderType.equalsIgnoreCase("PDF")) {
				WebElement pdfRadio = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderPDF, 10);
				commonLibrary.setRadioButton(pdfRadio, "PDF");
			} else if (folderType.equalsIgnoreCase("Live Map")) {
				WebElement liveMap = commonLibrary.isExist(UIMAP_ResearchMap.saveFolderLiveMap, 10);
				commonLibrary.setRadioButton(liveMap, "Live Map");
			}

			// To write the docment title to excel
			if (excelData != "") {
				String sheetNam = excelData.split(";")[0];
				int row = Integer.parseInt((excelData.split(";")[1]));
				String column = excelData.split(";")[2];

				WebElement doctitle = commonLibrary.isExist(UIMAP_ResearchMap.folderDocTitle, 10);
				String docTit = doctitle.getAttribute("value");
				String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
				excel = new ExcelDataAccess(datatablePath, sheetNam);
				excel.setDatasheetName("General_Data");
				excel.setValue(row, column, docTit);
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveFolder, 10);
			if (saveFolder != null)
				commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

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

		} else
			report.updateTestLog("Click Add to folder Icon", "Add to folder Icon is not available", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to folders
	// # Function Name : navigateToFolders_Researchmap    
	// # Author : updated:karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders_Researchmap() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");
				commonLibrary.sleep(3000);
			}

			WebElement folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			if (folder == null) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null) {
					// commonLibrary.highlightElement(btnMore);
					if ((browsername.contains("internet")))
						commonLibrary.clickJS(btnMore, "More");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				}
				folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			}

			if (folder != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(folder, "Folders");
				else
					commonLibrary.clickLinkWithWebElementWithWait(folder, "Folders");
				commonLibrary.sleep(3000);
			}
		} catch (Exception e) {

		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the history page document
	// header title
	// # Function Name : verifyHistoryDetailsOne_documentfolder    
	// # Author : karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyHistoryDetailsOne_documentfolder(String DocTitle) {
		boolean blnFlag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
				blnFlag = true;
				break;
			}

		}
		if (blnFlag) {
			report.updateTestLog("Verify The document title" + DocTitle, DocTitle + " is displayed in the researchmap page", Status.PASS);
		} else {
			report.updateTestLog("Verify The document title" + DocTitle, DocTitle + " is not displayed in the researchmap page", Status.FAIL);
		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the researchmap page document
	// header title
	// # Function Name : Verifyingdoctitle_researchmap    
	// # Author : karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyingdoctitleResearchMap(String searchTerm) {
		boolean blnFlag = false;
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().equalsIgnoreCase(searchTerm)) {
				blnFlag = true;
				;
				break;
			}

		}
		if (!blnFlag) {
			report.updateTestLog("Verify The document title" + searchTerm, searchTerm + " is not displayed in the researchmap page", Status.PASS);
		} else {
			report.updateTestLog("Verify The document title" + searchTerm, searchTerm + " is  displayed in the researchmap page", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the node order and
	// attachments present in Research Map
	// # Function Name : verifyNodeAttachment    
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyNodeAttachment(String searchTerm, String nodes, String attachments) {

		boolean flag = false;
		String node[] = nodes.split(";");
		String attachment[] = attachments.split(";");
		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				List<WebElement> activities = commonLibrary.isExistList(trail, UIMAP_ResearchMap.activityDiv, 10);

				if (activities.size() == node.length) {
					flag = true;
					int j = 0;
					for (int i = 0; i < node.length - 1; i++) {
						commonLibrary.sleep(10000);
						WebElement bubbleList = commonLibrary.isExist(activities.get(i), UIMAP_ResearchMap.bubble, 10);
						if (node[i].equalsIgnoreCase(bubbleList.getText())) {
							WebElement icon = commonLibrary.isExist(activities.get(i), By.tagName("i"), 10);
							if (icon.getAttribute("class").contains("contentfilter")) {
								WebElement Picon = commonLibrary.isExist(activities.get(i - 1), By.tagName("i"), 10);
								if (icon.getAttribute("class").equalsIgnoreCase(Picon.getAttribute("class")))
									report.updateTestLog(node[i] + " is linked to " + node[0] + " and is present below " + node[i - 1], node[i] + " is linked to " + node[0] + " and is present below " + node[i - 1], Status.PASS);
								else
									report.updateTestLog(node[i] + " is connected to " + node[0], node[i] + " is connected to " + node[0], Status.PASS);
							} else if (node[i].equalsIgnoreCase(searchTerm))
								report.updateTestLog(searchTerm + " is the starting node", searchTerm + " is the starting node", Status.PASS);
							else
								report.updateTestLog(node[i] + " is connected to " + node[i - 1], node[i] + " is connected to " + node[i - 1], Status.PASS);

						} else {
							report.updateTestLog(node[i] + " is present in correct order", node[i] + " is not present in correct order", Status.FAIL);
						}
						if (attachment[j] != null && attachment[j].equalsIgnoreCase(node[i])) {
							WebElement attachmnt = commonLibrary.isExist(activities.get(i), UIMAP_ResearchMap.attachment, 10);
							if (attachmnt != null)
								report.updateTestLog("Attachment Icon is present for the node number" + i, "Attachment Icon is present for the node number" + i, Status.PASS);
							else
								report.updateTestLog("Attachment Icon is present for the node number" + i, "Attachment Icon is not present for the node number" + i, Status.PASS);
							j++;

						}
					}
				}

			}
			if (flag)
				break;
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the second results count is
	// greater than first
	// # Function Name : VerifySecondCountWithFirst
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifySecondCountWithFirst(String Count1, String comparator) {

		String resultText = driver.getTitle();
		String[] resultArray = resultText.split(" results");
		String resultCount = resultArray[0].trim();

		if (comparator.toLowerCase().equals("greater than")) {

			if (Integer.parseInt(resultCount) > Integer.parseInt(Count1)) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is greater than previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is NOT greater than previously noted result", Status.FAIL);
			}
		} else if (comparator.toLowerCase().equals("less than")) {
			if (Integer.parseInt(resultCount) < Integer.parseInt(Count1)) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is less than previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is less than previously noted result", Status.FAIL);
			}
		} else if (comparator.toLowerCase().equals("equal to")) {
			if (Integer.parseInt(resultCount) == Integer.parseInt(Count1)) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is Equal to previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is Equal to previously noted result", Status.FAIL);
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on links within attachment
	// icon
	// # Function Name : clickOnAttachmentLinks
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ResearchMap clickOnAttachmentLinks(String searchTerm, String docLinkName) {

		boolean blnFlag = false;

		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		// for(WebElement trail:lstResearchThread)
		// {
		// WebElement trailHeader = commonLibrary.isExist_Negative(trail,
		// By.tagName("h4"), 10);
		// if(trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase()))
		// {
		// WebElement attachmentContent
		// =commonLibrary.isExist_Negative(trail,UIMAP_ResearchMap.attachmentContent,
		// 10);
		// WebElement button = commonLibrary.isExist_Negative(attachmentContent,
		// UIMAP_ResearchMap.button, 10);
		// if(button!=null)
		// {
		// if(!attachmentContent.getAttribute("class").contains("active"))
		// commonLibrary.clickButton_Parent_WithWait(button,"Attachment Button");
		// blnFlag1=true;
		// break;
		// }
		// }
		// }
		// if(blnFlag1)
		// {
		lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				//
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				// WebElement doclink =
				// commonLibrary.isExist_Negative(attachmentContent,UIMAP_ResearchMap.doclnk,
				// 10);

				List<WebElement> linkNodes = commonLibrary.isExistList(attachmentContent, UIMAP_ResearchMap.doclnk, 10);

				for (WebElement nodeLnk : linkNodes) {
					// WebElement
					// nodeLink=commonLibrary.isExist_Negative(nodeLnk,
					// By.tagName("h4"), 10);
					if (nodeLnk.getText().toLowerCase().contains(docLinkName.toLowerCase())) {
						commonLibrary.clickButtonParentWithWait(nodeLnk, docLinkName);
						blnFlag = true;
					}
				}
				// if(doclink!=null && doclink.isDisplayed())
				// {
				// WebElement link = commonLibrary.isExist_Negative(doclink,
				// UIMAP_ResearchMap.lnkLinks,10);
				// commonLibrary.clickButton_Log_SmallWait(link, docName);
				// blnFlag=true;
				// break;
				// }
			}
		}
		// }
		// else
		// report.updateTestLog("Click on document link: "+docLinkName,"Attachment link is NOT displayed",Status.FAIL);
		if (!blnFlag)
			report.updateTestLog("Click on document link: " + docLinkName, docLinkName + " is NOT displayed", Status.FAIL);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the header present in the
	// popup.
	// # Function Name : verifyPopUpHeader     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyPopUpHeader(String term) {
		WebElement popup = commonLibrary.isExist(UIMAP_ResearchMap.popup, 10);
		if (popup != null) {
			WebElement popupHeader = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.popUpHeader, 10);
			if (popupHeader != null) {
				if (popupHeader.getText().toLowerCase().contains(term.toLowerCase()))
					report.updateTestLog("Verify RM popup Header details " + term, term + " is displayed", Status.PASS);
				else
					report.updateTestLog("Verify RM popup Header details " + term, term + " is NOT displayed", Status.FAIL);
			}

		}

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click attachment icon
	// # Function Name : clickAttachmentIcon
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ResearchMap clickAttachmentIcon(String searchTerm) {

		boolean blnFlag1 = false;

		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
				WebElement button = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.button, 10);
				if (button != null) {
					if (!attachmentContent.getAttribute("class").contains("active"))
						commonLibrary.clickButtonParentWithWait(button, "Attachment Button for " + searchTerm);
					blnFlag1 = true;
					break;
				}
			}
		}
		if (!blnFlag1)
			report.updateTestLog("Click on Atachment link for " + searchTerm, "Attachment link is NOT displayed", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from attachment popup
	// # Function Name : clickLinkFromAttachmentPopup
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking clickLinkFromAttachmentPopup(String linkName) {
		boolean blnFlag = false;
		WebElement popup1 = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.buttons, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, linkName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new CounselBenchmarking(scriptHelper);
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
			report.updateTestLog("Verify results page with header: " + term + " is displayed", "Results page with header: " + term + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify results page with header: " + term + " is displayed", "Results page with header: " + term + " is not displayed", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link in Popup
	// # Function Name : clickLink_Popup2
	// # Author : Meera
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickLink_Popup2(String linkName) {
		boolean blnFlag = false;
		WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		WebElement popup1 = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.popup1, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.lstTagName, 10);
		System.out.println("size: " + popuplinks.size());
		for (WebElement link : popuplinks) {
			System.out.println(link.getText());
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickWithSmallWaitJS(link, linkName);
				commonLibrary.clickButton(link);
				// commonLibrary.clickLinkWithWebElementWithWait(popup1,
				// linkName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To any link under History
	// # Function Name : navigateToHistoryFooterLink  
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search navigateToHistoryFooterLink(String strLinkName, String SearchType) {
		boolean flag = false;
		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			List<WebElement> HistoryList = commonLibrary.isExistList(UIMAP_Home.viewDocumentList, 10);
			for (WebElement link : HistoryList) {
				if ((link.getText().contains(strLinkName) && (link.getText().contains(SearchType)))) {
					commonLibrary.clickLink(link, strLinkName);
					flag = true;
					break;
				}
			}
			if (!flag) {
				report.updateTestLog("Click " + strLinkName + "link ", strLinkName + " link is not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link in Popup
	// # Function Name : clickLink_Popup2
	// # Author : Meera
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickLink_PopupRerun(String searchTerm, String category, String linkName) {

		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {
			WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
			if (trailHeader.getText().toLowerCase().equals(searchTerm.toLowerCase())) {
				List<WebElement> lnkClassBubble = commonLibrary.isExistList(trail, UIMAP_SearchResult.lnkClassBubble, 20);
				if (lnkClassBubble.size() > 0) {

					for (WebElement item : lnkClassBubble) {
						if (item != null && item.getText().toLowerCase().equals(category.toLowerCase())) {
							commonLibrary.clickLinkWithWebElementWithWait(item, "BubbleLink :" + category);
							/*
							 * WebElement popup =
							 * commonLibrary.isExistNegative(UIMAP_ResearchMap
							 * .popup, 10); popupContent = popup.getText();
							 */

							boolean blnFlag = false;
							WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
							WebElement popup1 = commonLibrary.isExistNegative(popup, UIMAP_ResearchMap.popup1, 10);
							List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.lstTagName, 10);
							System.out.println("size: " + popuplinks.size());
							for (WebElement link : popuplinks) {
								System.out.println(link.getText());
								if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
									commonLibrary.clickButtonParentWithWaitJS(link, linkName);
									commonLibrary.clickButton(link);
									// commonLibrary.clickLinkWithWebElementWithWait(popup1,
									// linkName);
									blnFlag = true;
									break;
								}
							}
							if (!blnFlag)
								report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
						}
					}
				}
			}
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Add to Folder icon is not
	// displayed
	// # Function Name : verifyFolderIconNotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyFolderIconNotDisplayed() {

		WebElement deliveryTray = commonLibrary.isExistNegative(UIMAP_SearchResult.selectAllItemsUl, 10);
		WebElement addTofolder = commonLibrary.isExistNegative(deliveryTray, UIMAP_SearchResult.addTofolder, 10);
		if (addTofolder == null)
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is not displayed on the delivery tray ", Status.PASS);
		else
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is displayed on the delivery tray ", Status.FAIL);

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link in Popup
	// # Function Name : clickLink_Popup     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickLink_Popup3(String linkName) {
		boolean blnFlag = false;
		WebElement popup1 = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
		List<WebElement> popuplinks = commonLibrary.isExistList(popup1, UIMAP_ResearchMap.buttonTag, 10);
		for (WebElement link : popuplinks) {
			if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, linkName);
				commonLibrary.sleep(10000);
				blnFlag = true;
				break;
			}
		}
		WebElement header = commonLibrary.isExist(UIMAP_SearchResult.txtStudentReportHeader, 20);
		if (header != null)
			commonLibrary.focusControlJS(header);

		if (header.getText().contains("Table of Contents"))
			report.updateTestLog("Verify Legal Search Results page displays", "Legal Search Results Page is not displayed", Status.FAIL);
		else
			report.updateTestLog("Verify Legal Search Results page displays", "Legal Search Results Page is displayed", Status.PASS);
		if (!blnFlag)
			report.updateTestLog("Clikc popup link ", linkName + " link is not clicked", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select PopupLink
	// # Function Name : selectPopupLink_Wf    
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public ResearchMap selectPopupLinkFSD(String searchTerm, String category, String docTitle, String lnkName) {
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
							WebElement attachment = commonLibrary.isExistNegative(UIMAP_ResearchMap.attachment, 10);
							commonLibrary.clickButton(attachment);/*
																 * (item,
																 * "BubbleLink :"
																 * + attachment)
																 */
							;
							List<WebElement> attachContent = commonLibrary.isExistList(attachment, UIMAP_ResearchMap.lstTagName, 10);

							if (attachContent.size() > 0) {
								for (WebElement contents : attachContent) {
									if (contents.getText().toLowerCase().contains(docTitle.toLowerCase())) {
										commonLibrary.clickLinkWithWebElement(contents, docTitle);
										break;
									}
								}

							} else {
								report.updateTestLog("Click on " + attachContent + " under attachments menu " + category + " for trail " + searchTerm, attachContent + " link not clicked.", Status.FAIL);
							}

							WebElement popup = commonLibrary.isExistNegative(UIMAP_ResearchMap.popup, 10);
							List<WebElement> popuplinks = commonLibrary.isExistList(popup, UIMAP_ResearchMap.buttonTag, 10);
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
		return new ResearchMap(scriptHelper);
	}

}
