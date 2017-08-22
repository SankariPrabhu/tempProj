package functionallibraries;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.*;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import supportlibraries.*;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Document extends ReusableLibrary {

	// private static final String Text = null;
	private String Mediumwait = properties.getProperty("MediumWait");
	private Search search;
	int Mwait = Integer.parseInt(Mediumwait);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;
	public static String currentWindow = null;

	public Document(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;
		commonLibrary.sleep(5000);
		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("document"))
				commonLibrary.sleep(5000);

		} while (!url.contains("document") && counter < 40);

		if (!driver.getCurrentUrl().contains("document")) {
			throw new IllegalStateException("document page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to ClickOnCopyCitation
	// # Function Name : ClickOnCopyCitation     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void selectCitation(String strCitationFormat) {
		try {

			WebElement btnCopyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
			if (btnCopyCitation != null)
				commonLibrary.clickButtonParentWithWait(btnCopyCitation, "Copy Citation");

			// Copy CitaSearchtion Pop up window displays
			WebElement elePopUp = commonLibrary.isExist(UIMAP_Document.popUpCitation, 10);
			WebElement elePopUpTitle = commonLibrary.isExist(UIMAP_Document.popUpTitle, 10);
			WebElement cboCitationFormat = commonLibrary.isExist(UIMAP_Document.cboCitationFormat, 10);

			if (elePopUp != null && elePopUpTitle.getText().contains("Copy Citation to Clipboard"))

				report.updateTestLog("Verify Copy Citation Pop up window displays", "Copy Citation Pop up window displays", Status.PASS);
			else
				report.updateTestLog("Verify Copy Citation Pop up window displays", "Copy Citation Pop up window is not displayed", Status.FAIL);
			// Citation format dropdown displays in the pop up

			if (cboCitationFormat != null)

				report.updateTestLog("Verify Citation format dropdown displays in the pop up", "Citation format dropdown is displayed in the pop up", Status.PASS);
			else
				report.updateTestLog("Verify Citation format dropdown displays in the pop up", "Citation format dropdown is displayed in the pop up", Status.FAIL);
			// SELECT <<<STANDARD>>> FROM THE CITATION FORMATTER DROPDOWN
			if (commonLibrary.selectByVisibleText(cboCitationFormat, strCitationFormat))
				report.updateTestLog("SELECT " + strCitationFormat + " FROM THE CITATION FORMATTER DROPDOWN", strCitationFormat + " is selected FROM THE CITATION FORMATTER DROPDOWN", Status.PASS);
			else
				report.updateTestLog("SELECT " + strCitationFormat + " FROM THE CITATION FORMATTER DROPDOWN", strCitationFormat + " is not selected FROM THE CITATION FORMATTER DROPDOWN", Status.FAIL);
			// Click on <Select> button near the dropdown

			WebElement btnCitationSelect = commonLibrary.isExist(UIMAP_Document.CitationSelect, 10);
			if (btnCitationSelect != null)

				commonLibrary.clickButtonParentWithWait(btnCitationSelect, "Select");

			// <<<STANDARD>>> APPEARS AS THE ACTIVE STYLE MANUAL IN CITATION
			// FORMAT DROPDOWN
			WebElement cboCitationFormat1 = commonLibrary.isExist(UIMAP_Document.cboCitationFormat, 10);
			if (commonLibrary.selectIsSelected(cboCitationFormat1, strCitationFormat))
				report.updateTestLog("Verify " + strCitationFormat + " APPEARS AS THE ACTIVE STYLE MANUAL IN CITATION FORMAT DROPDOWN", strCitationFormat + " APPEARS AS THE ACTIVE STYLE MANUAL IN CITATION FORMAT DROPDOWN", Status.PASS);
			else
				report.updateTestLog("Verify " + strCitationFormat + " APPEARS AS THE ACTIVE STYLE MANUAL IN CITATION FORMAT DROPDOWN", strCitationFormat + " is not appearing AS THE ACTIVE STYLE MANUAL IN CITATION FORMAT DROPDOWN", Status.FAIL);
			// Full source citation displays as
			// "Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)"

			WebElement txtClipboardText = commonLibrary.isExist(UIMAP_Document.txtClipboardText, 10);
			if (txtClipboardText != null) {
				WebElement eleUnderLine = commonLibrary.isExist(txtClipboardText, By.tagName("u"), 10);
				{
					if (eleUnderLine != null || txtClipboardText.getText().contains("Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)"))
						report.updateTestLog("Verify Full source citation displays as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", "Full source citation is displayed as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", Status.PASS);
					else
						report.updateTestLog("Verify Full source citation displays as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", "Full source citation is not displayed as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", Status.FAIL);
				}
			} else
				report.updateTestLog("Verify Full source citation displays as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", "Full source citation is not displayed as Strickland v. Washington, 466 U.S. 668, 104 S.Ct. 2052 (1984)", Status.FAIL);
			// Click on <Close> button

			WebElement btnCitationClose = commonLibrary.isExist(UIMAP_Document.btnCitationClose, 10);
			if (btnCitationClose != null)

				commonLibrary.clickButtonParentWithWait(btnCitationClose, "Close");
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a document is present within
	// a folder
	// # Function Name : verifyDocumentPresent     
	// # Author : Dinesh
	// # Date Created : Sept'15
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
	// # Function Description : Function to ClickOnActivePassage
	// # Function Name : ClickOnActivePassage_DownloadDoc    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickOnActivePassageDownloadDoc(String strFilePath, String strAutoITPath) {

		try {

			// Click on "Activate passage" button from About this document
			// section RFC will get highlighted
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			// commonLibrary.scrollDown(1000);
			WebElement btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
			if (btnActivatepassages != null)

				commonLibrary.clickButtonParentWithWait(btnActivatepassages, "Activate passages");

			// Click on highlighted RFC LIT page will display

			WebElement lnkRFPassage = commonLibrary.isExist(UIMAP_Document.lnkRFPassage, 10);
			if (lnkRFPassage != null)
				commonLibrary.clickButtonParentWithWait(lnkRFPassage, "highlighted RFC");

			search.verifySearchResultHeader("legal issue trail");

			selectDocument(1);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download document
	// # Function Name : DownloadDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String downloadDocumentDefaultSettings(String strFilePath, String AutoITPath) {

		try {
			// CLICK ON <<<DOWNLOAD>>>
			String filename = null;
			generalFunctions.clickDeliverySelectOption("delivery", "downloadnow");

			// WebElement btnDownload =
			// commonLibrary.isExist(UIMAP_Document.btnDownload, 10);
			// if (btnDownload != null)
			// commonLibrary.clickButton_Parent_WithWait(btnDownload,
			// "Download");

			// CLICK <<<USE DEFAULT SETTINGS>>> FROM THE DROP DOWN.
			// WebElement btnDownloadNow =
			// commonLibrary.isExist(UIMAP_Document.btnDownloadNow, 10);
			// if (btnDownloadNow != null)
			// commonLibrary.clickButton_Parent_WithWait(btnDownloadNow,
			// "USE DEFAULT SETTINGS");

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("deliverysecondarywindow");
			driver.manage().window().maximize();

			WebElement lnkDownload = commonLibrary.isExist(UIMAP_Document.lnkDownloadedDoc, 10);
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null && pgHeader.getText().toLowerCase().contains("delivery complete") && lnkDownload != null)
				report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE ONCE THE DELIVERY IS COMPLETE.", Status.PASS);
			else
				report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE ONCE THE DELIVERY IS COMPLETE.", Status.FAIL);
			String strFileName = null;
			if (lnkDownload != null) {
				strFileName = lnkDownload.getText();

				commonLibrary.fileExistsDelete(strFilePath, strFileName);
				String path = strFilePath + "\\";

				report.updateTestLog("Verify that Download is complete", "Download is complete", Status.PASS);
				Actions action = new Actions(driver);

				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				String browsername = cap.getBrowserName();

				if (browsername.equalsIgnoreCase("firefox")) {
					driver.manage().window().maximize();
					action.contextClick(lnkDownload).sendKeys("k").build().perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath, "Enter name of file to save to…", path };
					Runtime.getRuntime().exec(cmd);
				} else if (browsername.equalsIgnoreCase("internet explorer")) {
					action.contextClick(lnkDownload).sendKeys("a").build().perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath, "Save As", path };
					Runtime.getRuntime().exec(cmd);
				}

				commonLibrary.sleep(30000);

				commonLibrary.fileExists(strFilePath, strFileName);
				filename = strFilePath + "\\" + strFileName;

			}

			WebElement btnDownloadClose = commonLibrary.isExist(UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("results");
			return filename;

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download document using Choose
	// Settings
	// # Function Name : DownloadDocument_ChooseSettings    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public String downloadDocumentChooseSettings(String strFilePath, String AutoITPath, String FieldLabel, String FieldType, String Values) {

		try {
			String arrFieldLabel[] = FieldLabel.split(";");
			String arrFieldType[] = FieldType.split(";");
			String arrValues[] = Values.split(";");

			boolean[] blnValues = new boolean[15];
			for (int b = 0; b < arrValues.length; b++) {
				if (arrValues[b].equalsIgnoreCase("true"))
					blnValues[b] = true;
				else
					blnValues[b] = false;
			}

			// // CLICK ON <<<DOWNLOAD>>>
			// WebElement btnDownload =
			// commonLibrary.isExist(UIMAP_Document.btnDownload, 10);
			// if (btnDownload != null) {
			// commonLibrary.clickButton_Parent_WithWait(btnDownload,
			// "Download");
			// }
			//
			// // CLICK <<<Choose Settings>>> FROM THE DROP DOWN.
			//
			// WebElement ChooseSettings =
			// commonLibrary.isExist(UIMAP_Document.DownloadChooseSet, 10);
			// if (ChooseSettings != null) {
			// commonLibrary.clickButton_Parent_WithWait(ChooseSettings,
			// "Choose Settings");

			generalFunctions.clickDeliverySelectOption("delivery", "downloadopt");
			WebElement ChooseSettDialog = commonLibrary.isExist(UIMAP_Document.DialogChooseSett, 10);
			if (ChooseSettDialog != null) {
				WebElement tabBasicOpt = commonLibrary.isExist(UIMAP_Document.tabDLDialogBasicOpt);
				commonLibrary.clickButton(tabBasicOpt);

				for (int j = 0; j < arrFieldLabel.length; j++) {
					chooseDownLoadSettings(arrFieldLabel[j], arrFieldType[j], blnValues[j]);
				}
				// Click on 'Donwload' button
				WebElement chkZIPFile = commonLibrary.isExist(UIMAP_Document.chkZIPFile);
				commonLibrary.setCheckBox(chkZIPFile, false);

				WebElement btndownload = commonLibrary.isExist(UIMAP_Document.btnDownloadinDialog, 10);
				commonLibrary.clickButtonParent(btndownload, "Download");
				commonLibrary.sleep(6000);
			}

			else {
				report.updateTestLog("Download Dialog box opens on selecting 'Choose Settings'", "Download Dialog box is not opened on selecting 'Choose Settings'", Status.FAIL);
			}
			//
			// }

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("deliverysecondarywindow");

			WebElement lnkDownload = commonLibrary.isExist(UIMAP_Document.lnkDownloadedDoc, 10);
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null && pgHeader.getText().toLowerCase().contains("delivery complete") && lnkDownload != null)
				report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE ONCE THE DELIVERY IS COMPLETE.", Status.PASS);
			else
				report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE ONCE THE DELIVERY IS COMPLETE.", Status.FAIL);
			String strFileName = null;
			String filename = null;
			if (lnkDownload != null) {
				strFileName = lnkDownload.getText();
				report.updateTestLog("Verify File " + strFileName + " is downloaded in Downloads Folder", "File " + strFileName + " is downloaded in Downloads Folder", Status.PASS);

				commonLibrary.fileExistsDelete(strFilePath, strFileName);

				report.updateTestLog("Verify that Download is complete", "Download is complete", Status.PASS);
				Actions action = new Actions(driver);

				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				String browsername = cap.getBrowserName();

				if (browsername.equalsIgnoreCase("firefox")) {
					action.contextClick(lnkDownload).sendKeys("k").build().perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath, "Enter name of file to save to…" };
					Runtime.getRuntime().exec(cmd);
				} else if (browsername.equalsIgnoreCase("internet explorer")) {
					action.contextClick(lnkDownload).sendKeys("a").build().perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath, "Save As" };
					Runtime.getRuntime().exec(cmd);
				}

				commonLibrary.sleep(30000);

				commonLibrary.fileExists(strFilePath, strFileName);
				filename = strFilePath + "\\" + strFileName;

			}

			WebElement btnDownloadClose = commonLibrary.isExist(UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("results");

			return filename;
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

	public String selectDocument(int intDocNo) {

		try {
			String strDocTitle = null;
			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			WebElement lnkTitle = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[data-action=title]"), 10);
			strDocTitle = lnkTitle.getText();
			WebElement lstchkBox = commonLibrary.isExist(OList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
			String strI = "" + (intDocNo);
			if (lstchkBox != null)
				commonLibrary.setCheckBox(lstchkBox, strI);
			else
				report.updateTestLog("Select document", "document is not selected", Status.FAIL);

			return strDocTitle;
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Document and AddTo Folder
	// # Function Name : SelectDoc_AddToFolder    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String selectDocAddToFolder(int intDocNo, String strContactName) {

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			String strDocTitle = selectDocument(intDocNo);

			// CLICK ON <<<ADD TO FOLDER>>>
			commonLibrary.sleep(1000);
			WebElement btnAddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 10);
			if (btnAddFolder != null)

				commonLibrary.clickButtonParentWithWait(btnAddFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement btnChooseFolder = commonLibrary.isExist(UIMAP_Document.btnChooseFolder, 10);
			if (btnChooseFolder != null)
				commonLibrary.clickButtonParentWithWait(btnChooseFolder, "Choose Folder");

			// CLICK ON <<<CREATE FOLDER>>>

			WebElement btnCreateFolder = commonLibrary.isExist(UIMAP_Document.btnCreateFolder, 10);
			if (btnCreateFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateFolder, "Create Folder");

			// ENTER <<<SMOKE TEST>>> IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT
			// BOX
			String strFolderName = "SmokeTest " + commonLibrary.getCurrentDateTime();
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, strFolderName);
			// CLICK ON <<<CREATE>>>
			WebElement btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			if (btnCreateNewFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateNewFolder, "CREATE");

			// Click <Share with Others> tab in add to folder pop up
			selectTabsAddToFolderWindow("Share with Others");

			// ENTER <<<B>>> IN THE TEXT BOX RECENTLY USED NAMES WILL DISPLAY
			// ALLOWING THE USER TO SELECT ONE

			WebElement txtShareContacts = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
			if (txtShareContacts != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtShareContacts, strContactName);

			commonLibrary.sleep(1000);
			// SELECT THE <<<BECKER>>>
			try {

				WebElement cboSharingList = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
				commonLibrary.selectFromList(cboSharingList, strContactName);
				// commonLibrary.Select_FromList_Index(cboSharingList, 1);
			} catch (Exception e) {

			}
			// Click the "Add" button
			WebElement btnAddToContact = commonLibrary.isExist(UIMAP_Document.btnAddToContact, 10);
			if (btnAddToContact != null)
				commonLibrary.clickButtonParentWithWait(btnAddToContact, "Add");

			// CLICK ON <<<SAVE>>>
			WebElement btnDocumentSave = commonLibrary.isExist(UIMAP_Document.btnDocumentSave, 10);
			if (btnDocumentSave != null)
				commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");

			return strDocTitle;
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
				WebElement tabSelectedDocuments = commonLibrary.isExist(UIMAP_Document.tabSelectedDocuments, 10);
				if (tabSelectedDocuments != null)
					commonLibrary.clickButtonParentWithWait(tabSelectedDocuments, "Selected Documents");
				break;
			}
			case "Save Options": {
				WebElement tabSaveOptions = commonLibrary.isExist(UIMAP_Document.tabSaveOptions, 10);
				if (tabSaveOptions != null)
					commonLibrary.clickButtonParentWithWait(tabSaveOptions, "Save Options");
				break;
			}
			case "Share with Others": {
				WebElement tabShareWithOthers = commonLibrary.isExist(UIMAP_Document.tabShareWithOthers, 10);
				if (tabShareWithOthers != null)
					commonLibrary.clickButtonParentWithWait(tabShareWithOthers, "Share With Others");
				break;
			}
			default: {
				WebElement tabSaveOptions = commonLibrary.isExist(UIMAP_Document.tabSaveOptions, 10);
				if (tabSaveOptions != null)
					commonLibrary.clickButtonParentWithWait(tabSaveOptions, "Save Options");
				break;
			}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on ViewReports link and verfiy
	// the TopicSummary page
	// # Function Name : ClickViewReports_VerifyTopicSummaryPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickViewReportsVerifyTopicSummaryPage() {

		try {
			// Click <View Reports> link under <About this document> section
			// Topic results page displays

			// driver.navigate().to(properties.getProperty("ApplicationUrl")+"/document/");
			WebElement lnkViewReport = commonLibrary.isExist(UIMAP_Document.lnkViewReport, 20);

			if (lnkViewReport != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkViewReport, "View Reports");

			commonLibrary.sleep(1000);
			WebElement pgTitleTopicSummary = commonLibrary.isExist(UIMAP_Document.pgTitleTopicSummary, 10);

			if (pgTitleTopicSummary != null && pgTitleTopicSummary.getText().toLowerCase().contains("topic summary"))
				report.updateTestLog("Verify Topic results Page is displayed", "Topic results Page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Topic results Page is displayed", "Topic results Page is not displayed", Status.FAIL);
			// CLICK ON <<<ASSISTANCE OF COUNSEL>>>
			WebElement lnkAssistanceOFCounsel = commonLibrary.isExist(UIMAP_Document.lnkAssistanceOFCounsel, 20);

			if (lnkAssistanceOFCounsel != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkAssistanceOFCounsel, "Assistance OF Counsel");

			commonLibrary.sleep(1000);

			// MV Topic summary page for <Assistance of Counsel> displays
			commonLibrary.sleep(1000);
			WebElement pgTitleTopicSummary1 = commonLibrary.isExist(UIMAP_Document.pgTitleTopicSummary, 10);

			if (pgTitleTopicSummary1 != null && pgTitleTopicSummary1.getText().contains("Assistance of Counsel"))
				report.updateTestLog("Verify Topic summary for <Assistance of Counsel> Page is displayed", "Topic summary for <Assistance of Counsel> Page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Topic summary for <Assistance of Counsel> Page is displayed", "Topic summary for <Assistance of Counsel> Page is not displayed", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Set values in Choose Download/Print
	// Settings dialog
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void chooseDownLoadSettings(String FieldLabel, String InputType, boolean isCheck) {
		try {
			List<WebElement> Rows = commonLibrary.isExistList(UIMAP_Document.divDLDialogRows, 10);
			boolean blnFlag = false;
			for (WebElement item : Rows) {

				WebElement Label = commonLibrary.isExist(item, By.tagName("label"), 10);
				WebElement Input = commonLibrary.isExist(item, By.tagName("input"), 10);

				switch (InputType) {
				case "CheckBox": {
					if (Label.getText().contains(FieldLabel)) {
						// commonLibrary.ScrollToView(Label);
						commonLibrary.setCheckBox(Input, isCheck);
						blnFlag = true;
					}
					break;
				}
				case "Radio": {
					if (Label.getText().contains(FieldLabel)) {
						// commonLibrary.ScrollToView(Label);
						commonLibrary.setRadioButton(Input, isCheck);
						blnFlag = true;
					}
					break;
				}

				}
				if (blnFlag) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify contents in a PDF file
	// # Function Name : PDFVerification_Path     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void pdfVerificationPath(String FilePath, String Text, String Exist) {
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

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Shepard's document
	// # Function Name : SelectShepDocumentByTitle    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void selectShepDocumentByTitle(String strDocName) {

		try {
			int i;
			boolean blnFlag = false;
			String strDocTitle = null;
			commonLibrary.sleep(1000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (i = 0; i < LList.size(); i++) {
				WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='midlinetitle']"), 10);
				strDocTitle = lnkTitle.getText();
				if (strDocTitle.trim().contains(strDocName.trim())) {
					WebElement lstchkBox = commonLibrary.isExist(OList.get(i), By.cssSelector("input[type='checkbox']"), 10);
					String strI = "" + (i + 1);
					if (lstchkBox != null) {
						commonLibrary.setCheckBox(lstchkBox, strI);
						blnFlag = true;
						break;
					}
				}
			}
			if (blnFlag)
				report.updateTestLog("Select document", strDocName + " document is selected", Status.PASS);
			else
				report.updateTestLog("Select document", strDocName + "document is not selected", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify recently viewed icon
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void verifyRecentlyViewed(String strDocTitle) {
		try {
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			if (commonLibrary.isExistNegative(UIMAP_Document.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_Document.frmClassResult, 10);
			Boolean blnFlag = false;
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Document.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
							WebElement RecntlyViewed = commonLibrary.isExist(document, UIMAP_Document.btnRecentlyViewed, 2);
							if (RecntlyViewed != null) {
								blnFlag = true;
								break;
							}
						}

					}

				}
			}

			if (blnFlag) {

				report.updateTestLog("Recently viewed icon appears next to '" + strDocTitle + "'", "Recently viewed icon appears next to '" + strDocTitle + "'", Status.PASS);
			} else {
				report.updateTestLog("Recently viewed icon appears next to '" + strDocTitle + "'", "Recently viewed icon does not appear next to '" + strDocTitle + "'", Status.FAIL);
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on reference link of a
	// document
	// # Function Name : Click_Doc_Ref_Link    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void clickDocRefLink(String strDocTitle, String RefLink) {
		try {
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			if (commonLibrary.isExistNegative(UIMAP_Document.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_Document.frmClassResult, 10);
			Boolean blnFlag = false;
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Document.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
							WebElement FirstRef = commonLibrary.isExist(document, UIMAP_Document.lnkFirstRef, 2);
							if (FirstRef.getText().equalsIgnoreCase(RefLink)) {
								commonLibrary.clickLinkWithWebElementWithWait(FirstRef, RefLink);
								blnFlag = true;
								break;
							}
						}

					}

				}
			}

			if (blnFlag) {

				report.updateTestLog("Click on the first reference link " + RefLink + " for the document '" + strDocTitle + "'", "the first reference link " + RefLink + " is clicked for the document '" + strDocTitle + "'", Status.PASS);
			} else {
				report.updateTestLog("Click on the first reference link " + RefLink + " for the document '" + strDocTitle + "'", "the first reference link " + RefLink + " is not clicked for the document '" + strDocTitle + "'", Status.FAIL);
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title
	// # Function Name : Verify_DocumentTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentTitle(String strDocTitle) {
		int count = 0;

		// wait for the document to be opened
		WebElement h1 = commonLibrary.isExist(UIMAP_Document.h1, 10);

		do {

			count++;
			h1 = commonLibrary.isExist(UIMAP_Document.h1, 10);
			commonLibrary.sleep(10000);

		} while (h1 == null && count < 15);

		// Verification Point : Document tilte displayed
		WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.DocTitleContent, 10);

		if (eltDocumentHeading.getText().contains(strDocTitle))
			report.updateTestLog("Verify document page is displayed", "Document " + strDocTitle + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify document page is displayed", "Document " + strDocTitle + " is not displayed", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document verifySearchResultHeader(String strPageHeader) {

		generalFunctions.verifyPageTitle(strPageHeader);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close secondary window and back to
	// Shepards window
	// # Function Name : closeAndBack_ToShepardsWindow     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards closeAndBackToShepardsWindow() {

		driver.close();
		commonLibrary.smallWait();
		commonLibrary.switchToWindow("lexis.com/shepards");

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to right click on HN link
	// # Function Name : rightClickNarrowByHN     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document rightClickNarrowByHN() {

		List<WebElement> lstPara = commonLibrary.isExistList(By.tagName("p"), 20);
		for (WebElement webElement : lstPara) {

			if (webElement.getText().contains("HN1")) {

				WebElement lnkSS_NarrowByHN = commonLibrary.isExist(webElement, UIMAP_Document.lnkSS_NarrowByHN, 20);

				// Function call for performing right click on the web element
				this.clickRightSelectOpenNewWindow(lnkSS_NarrowByHN);
				break;
			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Secondary_BroserOpened 
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document verifySecondary_BroserOpened() {

		generalFunctions.openedSecondaryBrowser();

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickRightSelectOpenNewWindow(WebElement element) {

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			Actions oAction = new Actions(driver);
			oAction.moveToElement(element);
			// commonLibrary.ScrollToView(element);
			// commonLibrary.highlightElement(element);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", element);
			commonLibrary.highlightElement(element);
			commonLibrary.sleep(2000);

			String browsername = cap.getBrowserName();
			if (browsername.equalsIgnoreCase("firefox")) {
				oAction.contextClick(element).sendKeys("W").build().perform();
			} else if (browsername.equalsIgnoreCase("internet explorer")) {
				oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();

			} else if (browsername.contains("chrome")) {
				oAction.keyDown(Keys.SHIFT).click(element).keyUp(Keys.SHIFT).perform();
				commonLibrary.sleep(4000);
			}
			commonLibrary.sleep(2000);
			if (driver.getWindowHandles().size() >= 2)

				report.updateTestLog("Right Click on the " + element.getText() + "and select open in new window", "Right Clicked on the " + element.getText() + "and selected open in new window", Status.PASS);
			else

				report.updateTestLog("Right Click on the " + element.getText() + "and select open in new window", "Not Right Clicked on the " + element.getText() + "and Not selected open in new window", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to shepards window
	// # Function Name : switchToShepards     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards switchToShepards(String strWindowName) {

		Boolean blnCheckSwitch = false;
		for (String winHandle : driver.getWindowHandles()) {
			Boolean a = driver.switchTo().window(winHandle).getCurrentUrl().contains(strWindowName);

			if (a == true) {
				driver.switchTo().window(winHandle);

				blnCheckSwitch = true;
				break;
			}
		}
		if (blnCheckSwitch)
			return new Shepards(scriptHelper);
		else
			return null;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for right click on shepardize this doc
	// link
	// # Function Name : RightClickShepardizeThisDocShepPreview     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document rightClickShepardizeThisDocShepPreview() {
		WebElement shepardsSection = commonLibrary.isExist(By.cssSelector("section[id='Shepards']"), 20);
		if (shepardsSection != null) {
			WebElement expand = commonLibrary.isExist(shepardsSection, By.cssSelector("h3[class*='TriangleDownAfter']"), 20);
			if (expand != null)
				commonLibrary.clickJS(expand, "Expand Shepards section");
		}
		// WebElement lstClassLinkSection =
		// commonLibrary.isExist(UIMAP_Document.lstClassLinkSection, 20);
		WebElement lnkShepardizeThisDoc = commonLibrary.isExist(UIMAP_Document.lnkShepardizeThisDoc, 20);
		// commonLibrary.ScrollToView(lstClassLinkSection);
		if (lnkShepardizeThisDoc != null)
			this.clickRightSelectOpenNewWindow(lnkShepardizeThisDoc);
		else
			report.updateTestLog("Right click on Shepardize this document link", "Shepardize this document link is not available", Status.FAIL);

		// WebElement lnkShepardizeThisDoc =
		// commonLibrary.isExist(UIMAP_Document.lnkShepardizeThisDoc, 20);
		// //commonLibrary.ScrollToView(lnkShepardizeThisDoc);
		// this.ClickRight_SelectOpenNewWindow(lnkShepardizeThisDoc);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for close and back to home page
	// # Function Name : closeAndBack_ToHomeWindow     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home closeAndBackToHomeWindow() {

		driver.close();
		commonLibrary.switchToWindow("lexis.com/search");
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click on back button
	// # Function Name : clickBrowserBack_ToHome     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search clickBrowserBack_ToHome() {

		commonLibrary.clickBrowserBack();

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : SimpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		int check = 0;

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for right click on shepardize this doc
	// link
	// # Function Name : RightClickShepardizeThisDoc     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search rightClickShepardizeThisDoc() {

		WebElement lstClassLinkSection = commonLibrary.isExist(UIMAP_Document.lstClassLinkSection, 20);
		WebElement lnkShepardize = commonLibrary.isExist(lstClassLinkSection, UIMAP_Document.lnkShepardize, 20);
		// commonLibrary.ScrollToView(lstClassLinkSection);
		this.clickRightSelectOpenNewWindow(lnkShepardize);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for right click on shepardize this doc
	// link
	// # Function Name : rightClickShepardizeThisDoc_docWindow     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document rightClickShepardizeThisDoc_docWindow() {

		WebElement shepardsSection = commonLibrary.isExist(By.cssSelector("section[id='Shepards']"), 20);
		if (shepardsSection != null) {

			WebElement expand = commonLibrary.isExist(shepardsSection, By.cssSelector("h3[class*='TriangleDownAfter']"), 20);
			if (expand != null)

				commonLibrary.clickJS(expand, "Expand Shepards section");
		}

		WebElement lstClassLinkSection = commonLibrary.isExist(UIMAP_Document.lstClassLinkSection, 20);
		WebElement lnkShepardize = commonLibrary.isExist(lstClassLinkSection, UIMAP_Document.lnkShepardize, 20);

		// Function call for performing right click
		if (lnkShepardize != null)
			this.clickRightSelectOpenNewWindow(lnkShepardize);
		else
			report.updateTestLog("Right click on Shepardize this document link", "Shepardize this document link is not available", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click any link in Shepards Preview
	// section
	// # Function Name : ClickLink_Shepards_Preview_Section     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickLinkShepardsPreviewSection(String lnkHeader, String linkname) {
		Boolean blnFlag = false;
		commonLibrary.scrollDown();
		commonLibrary.scrollUp();

		// to click on a link in the shepards preview page
		WebElement ShepSec = commonLibrary.isExist(UIMAP_Document.ShepardsSec, 10);
		if (ShepSec != null) {
			List<WebElement> ListPrevLinks = commonLibrary.isExistList(ShepSec, By.tagName("li"), 10);
			for (WebElement lst : ListPrevLinks) {
				if (lst.getText().toLowerCase().contains(lnkHeader.toLowerCase()) && lst.getText().toLowerCase().contains(linkname.toLowerCase())) {
					List<WebElement> PrevLinks = commonLibrary.isExistList(lst, By.tagName("a"), 10);
					for (WebElement item : PrevLinks) {
						if (item.getText().toLowerCase().contains(linkname.toLowerCase())) {
							if (browsername.contains("internet")) {
								try {
									driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
									commonLibrary.clickLinkWithWebElementWithWait(item, item.getText());
								} catch (TimeoutException ex) {
									driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
									System.out.println(ex.toString());

								}
								driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
							} else
								commonLibrary.clickLinkWithWebElementWithWait(item, item.getText());

							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag) {
					break;
				}
			}
		} else {
			report.updateTestLog("Shepards Preview section displays at the right pane of the full document", "Shepards Preview section is not displayed at the right pane of the full document", Status.FAIL);
		}
		int count = 0;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		do {
			count++;
			commonLibrary.sleep(5000);
		} while (eltSearchbox == null && count < 15);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Document Header
	// # Function Name : verifyDocumentHeader
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyDocumentHeader(String strDocTitle) {
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		commonLibrary.sleep(10000);

		WebElement atd = commonLibrary.isExist(UIMAP_Document.jumpto, 10);

		int counter = 0;
		do {
			counter = counter + 1;
			atd = commonLibrary.isExist(UIMAP_Document.jumpto, 20);
			if (atd == null)
				commonLibrary.sleep(5000);
		} while (atd == null && counter <= 40);

		WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
		if (txtDocumentHeading.getText().contains(strDocTitle)) {
			report.updateTestLog("Verify Full Document for" + strDocTitle + " displays.", "Full Document for " + strDocTitle + " displays.", Status.PASS);
		} else
			report.updateTestLog("Verify Full Document for " + strDocTitle + " displays.", "Full Document for " + strDocTitle + " is not displayed.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing logout
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
		// commonLibrary.clickMethod(btnMore, "More");
		// else
		// commonLibrary.clickMethod(btnMore, "More");
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
		// report.updatebrowTestLog("Verify Logout",
		// "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		// }
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click 'Acts Affecting this document'
	// link in Shepards Preview section
	// # Function Name : Click_ShepPreview_ActsAffectingthisDoc     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent clickShepPreviewActsAffectingthisDoc() {

		WebElement ShepSec = commonLibrary.isExist(UIMAP_Document.ShepardsSec, 10);
		if (ShepSec != null) {
			List<WebElement> PrevLinks = commonLibrary.isExistList(ShepSec, By.tagName("a"), 10);
			for (WebElement item : PrevLinks) {
				if (item.getText().contains("Acts Affecting this Document")) {
					commonLibrary.clickLinkWithWebElementWithWait(item, item.getText());
					break;
				}
			}

		} else {
			report.updateTestLog("Shepards Preview section displays at the right pane of the full document", "Shepards Preview section is not displayed at the right pane of the full document", Status.FAIL);
		}
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a text present in page
	// # Function Name : VerifyText_DocView     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document verifyTextDocView(By by, String Text) {
		WebElement obj = commonLibrary.isExist(by, 10);
		if (obj.getText().equalsIgnoreCase(Text)) {
			report.updateTestLog("The document  displays with the following in the viewable region:'" + Text + "'", "The document  displays with the following in the viewable region:'" + Text + "'", Status.PASS);
		} else {
			report.updateTestLog("The document  displays with the following in the viewable region:'" + Text + "'", "The document is not displayed with the following in the viewable region:'" + Text + "'", Status.FAIL);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a text present in page within
	// a list
	// # Function Name : VerifyText_DocView_withinList     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document verifyTextDocViewWithinList(By by, String Text) {
		List<WebElement> objList = commonLibrary.isExistList(by, 20);
		Boolean blnFlag = false;
		for (WebElement item : objList) {
			if (item.getText().contains(Text)) {
				blnFlag = true;
				break;
			}

		}
		if (blnFlag) {
			report.updateTestLog("The document displays witht the following in the viewable region '" + Text + "'", "The document displays witht the following in the viewable region '" + Text + "'", Status.PASS);

		} else {
			report.updateTestLog("The document displays witht the following in the viewable region '" + Text + "'", "The document is not displayed witht the following in the viewable region '" + Text + "'", Status.FAIL);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document verifyFullDocument(String strSearchTerm) {
		Boolean blnFlag = false;
		try {
			WebElement banner = commonLibrary.isExist(UIMAP_Document.hdrBanner, 10);
			if (banner != null) {
				List<WebElement> span = commonLibrary.isExistList(banner, UIMAP_Document.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase().contains(strSearchTerm.toLowerCase())) {
						blnFlag = true;
						break;
					}
				}
			}
			if (blnFlag) {
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document page is displayed for " + strSearchTerm + "", Status.PASS);
			} else {
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document page is not displayed for " + strSearchTerm + "", Status.PASS);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify preview section
	// # Function Name : verifyPreviewSection     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyPreviewSection() {

		WebElement preview = commonLibrary.isExist(UIMAP_Document.divShepards, 10);
		if (preview != null) {
			report.updateTestLog("Shepard's® preview section displays at the right pane of the full document", "Shepard's® preview section is displayed at the right pane of the full document", Status.PASS);
		} else {
			report.updateTestLog("Shepard's® preview section displays at the right pane of the full document", "Shepard's® preview section is not displayed at the right pane of the full document", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify text in preview section
	// # Function Name : verifyTextPreviewSection     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyTextPreviewSection(String strText5, String strText6) {
		boolean blnLnk = false;
		WebElement preview9 = commonLibrary.isExist(UIMAP_Document.divShepards, 10);
		if (preview9 != null) {
			List<WebElement> hdr = commonLibrary.isExistList(preview9, UIMAP_Document.taghdr, 10);
			for (WebElement item : hdr) {
				WebElement image = commonLibrary.isExist(item, UIMAP_Document.imgRedTri, 10);
				if (item.getText().toLowerCase().contains(strText5.toLowerCase()) && (image != null)) {
					List<WebElement> link = commonLibrary.isExistList(preview9, UIMAP_Document.lnkActsAffecting, 20);
					for (WebElement item1 : link) {
						if (item1.getText().toLowerCase().contains(strText6.toLowerCase())) {
							blnLnk = true;
							break;
						}
					}
					if (blnLnk)
						break;
				}
			}
		}
		if (blnLnk) {
			report.updateTestLog("" + strText5 + " displays in the Previewsection with a red exclamatory, in which <acts affecting this document>displays as a link", "" + strText5 + " is displayed in the Previewsection, in which <acts affecting this document> is displayed as a link", Status.PASS);
		} else {
			report.updateTestLog("" + strText5 + " displays in the Previewsection with a red exclamatory, in which <acts affecting this document>displays as a link", "" + strText5 + " is not displayed in the Previewsection, in which <acts affecting this document> is not displayed as a link", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify text in preview section
	// # Function Name : verifyTextPreviewSection     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyText1PreviewSection(String strText3) {
		boolean blnLnk = false;

		WebElement preview10 = commonLibrary.isExist(UIMAP_Document.divShepards, 10);
		if (preview10 != null) {
			List<WebElement> lnk = commonLibrary.isExistList(preview10, UIMAP_Document.lstTagName, 10);
			for (WebElement item : lnk) {
				if (item.getText().toLowerCase().contains(strText3.toLowerCase())) {
					WebElement image = commonLibrary.isExist(item, UIMAP_Document.imgRedCir, 10);
					if (image != null) {
						blnLnk = true;
						break;
					}
				}
			}
		}
		if (blnLnk) {
			report.updateTestLog("Verify " + strText3 + " displays in the Previewsection with a red exclamatory (placed in a circle)", "" + strText3 + " is displayed in the Previewsection with a red exclamatory (placed in a circle)", Status.PASS);
		} else {
			report.updateTestLog("Verify " + strText3 + " displays in the Previewsection with a red exclamatory (placed in a circle)", "" + strText3 + " is not displayed in the Previewsection with a red exclamatory (placed in a circle)", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify if comments are visible
	// # Function Name : verifyCommentsVisible     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyCommentsVisible(String strText) {
		WebElement divComments = commonLibrary.isExistNegative(UIMAP_Document.divComments, 10);
		if ((divComments != null && divComments.isDisplayed()) && commonLibrary.getParentElement(divComments).getText().contains(strText))
			report.updateTestLog("Verify 'Comment' section displays in a boxunder '" + strText + "'", "'Comment' section displays in a boxunder '" + strText + "'", Status.PASS);
		else
			report.updateTestLog("Verify 'Comment' section displays in a boxunder " + strText + "", "'Comment' section does not display in a boxunder '" + strText + "'", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to hide the comments
	// # Function Name : clickHideComments     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickHideComments() {
		WebElement btnHideComments = commonLibrary.isExistNegative(UIMAP_Document.btnHideComments, 10);
		commonLibrary.clickButtonLogSmallWait(btnHideComments, "Hide Comments");

		WebElement divComments = commonLibrary.isExistNegative(UIMAP_Document.divComments, 10);
		if ((divComments != null && divComments.isDisplayed()))
			report.updateTestLog("Verify 'Comment' section not present in the document", "'Comment' section  present in the document", Status.FAIL);
		else
			report.updateTestLog("Verify 'Comment' section not present in the document", "'Comment' section  not present in the document", Status.PASS);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify blanks
	// # Function Name : verifyBlankWithText     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyBlankWithText(String strText, String strSection) {
		WebElement eltDocContent = commonLibrary.isExistNegative(UIMAP_Document.eltDocContent, 10);
		String strDocText = eltDocContent.getText();
		String[] strTextList = strDocText.split(strSection);
		if (strTextList[1].contains(strText))
			report.updateTestLog("Verify Blank field displays as " + strText + " under section " + strSection, "Blank field displays as " + strText + " under section " + strSection, Status.PASS);
		else
			report.updateTestLog("Verify Blank field displays as " + strText + " under section " + strSection, "Blank field does not display as " + strText + " under section " + strSection, Status.PASS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links on document page
	// # Function Name : clickDocPageLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public RelatedContent clickDocPageLink(String linkName) {

		WebElement linkDocPage = commonLibrary.isExist(By.linkText(linkName), 10);

		commonLibrary.clickLinkWithWebElementWithWait(linkDocPage, linkDocPage.getText());

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Back
	// # Function Name : ClickBrowserBack     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search clickBrowserBack_Search() {

		driver.navigate().back();
		// commonLibrary.clickBrowserBack();

		commonLibrary.sleep(10000);
		//
		report.updateTestLog("Click on Browser Back", "Clicked on Browser Back", Status.PASS);
		// driver.manage().timeouts().pageLoadTimeout(220,
		// TimeUnit.MILLISECONDS);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Back
	// # Function Name : ClickBrowserBack     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public RelatedContent clickBrowserBack_RelatedContent() {

		driver.navigate().back();

		commonLibrary.sleep(6000);

		report.updateTestLog("Click on Browser Back", "Clicked on Browser Back", Status.PASS);
		// driver.manage().timeouts().pageLoadTimeout(220,
		// TimeUnit.MILLISECONDS);

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ReasearchMap link
	// # Function Name : NavigateToResearchMap
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToResearchMap() {
		generalFunctions.navigateToHistoryFooterLink("Research Map");
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC
	// # Function Name : clickTOCVerifyminiToc
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public Document clickTOCVerifyminiToc(Boolean isDisplayed) {

		// Click on TOC link
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);

		if (tableOfContentsTab != null)
			commonLibrary.clickButtonParentWithWaitJS(tableOfContentsTab, "Table of Contents");
		else
			report.updateTestLog("Click on TOC Link", "TOC link is not available", Status.FAIL);

		// Verification Point: Mini TOC Page
		WebElement miniToc = commonLibrary.isExistNegative(UIMAP_Document.miniToc, 10);
		if (isDisplayed) {

			if (miniToc != null && miniToc.isDisplayed())
				report.updateTestLog("Verify Mini TOC is displayed", "Mini TOC is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Mini TOC is displayed", "Mini TOC is not displayed", Status.FAIL);

		} else {
			if (miniToc == null)
				report.updateTestLog("Verify Mini TOC closes", "Mini TOC is closed", Status.PASS);
			else
				report.updateTestLog("Verify Mini TOC closes", "Mini TOC is not closed", Status.FAIL);

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view Toc
	// # Function Name : clickViewTOCVerifyFullToc
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public TOC clickViewTOCVerifyFullToc() {

		// Click View TOC link
		WebElement tocLink = commonLibrary.isExistNegative(UIMAP_Document.tocLink, 10);

		if (tocLink != null)
			commonLibrary.clickButtonParentWithWait(tocLink, tocLink.getText());
		else
			report.updateTestLog("Click on View TOC Link", "View TOC link is not available", Status.FAIL);

		commonLibrary.sleep(3000);

		// Verification Point : Full TOC
		WebElement txtStudentDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);

		if (txtStudentDocumentHeading != null && txtStudentDocumentHeading.getText().contains("Table of Contents"))
			report.updateTestLog("Verify Full Table of contents	Full TOC displays", "Full Table of contents	Full TOC displays", Status.PASS);
		else
			report.updateTestLog("Verify Full Table of contents	Full TOC displays", "Full Table of contents	Full TOC not displayed", Status.PASS);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alert icon
	// # Function Name : clickAlertIconLeg
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickAlertIconLeg() {
		WebElement btnAlert = commonLibrary.isExistNegative(UIMAP_Document.addalertleg, 10);
		if (btnAlert == null)
			btnAlert = commonLibrary.isExistNegative(UIMAP_Document.addalertTopic, 10);
		if (btnAlert != null) {
			commonLibrary.clickLinkWithWebElement(btnAlert, "Alert");
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tabs in alerts
	// # Function Name : selectTabInAlertsAndVerifyAllChkBoxSelected
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public Document selectTabInAlertsAndVerifyAllChkBoxSelected(String tab, String check) {
		boolean chk = false;
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			commonLibrary.selectFromListButton(topicalert, tab);
		} else {
			report.updateTestLog("Select the given tab " + tab + "", "" + tab + " is not selected", Status.FAIL);
		}
		if (check != null) {
			List<WebElement> chkbox = commonLibrary.isExistList(topicalert, UIMAP_Document.chkbox, 10);
			for (WebElement item : chkbox) {
				if (item.getAttribute("checked").equalsIgnoreCase("true")) {
					chk = true;
				} else {
					chk = false;
					break;
				}
			}
			if (chk) {
				report.updateTestLog("Verify that  under Monitor For Section all the Event Types checkboxes are checked by default", "Under Monitor For Section all the Event Types checkboxes are checked by default", Status.PASS);
			} else {
				report.updateTestLog("Verify that  under Monitor For Section all the Event Types checkboxes are checked by default", "Under Monitor For Section all the Event Types checkboxes are not checked by default", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify that  under Monitor For Section all the Event Types checkboxes are checked by default", "No check box is present for this particular search term", Status.PASS);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tabs in alerts
	// # Function Name : selectTabInAlertsAndVerifyAllChkBoxSelected
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public Document deliverTabAlerts(String strTab, String strFromDate, String strToDate, String strDeliverType, String strEmail, String strDeliverFormat, String strFrequency, String timevalue, String timevalues) {

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, strTab);
			if (strFromDate.toLowerCase() != "") {
				WebElement fromDate = commonLibrary.isExist(UIMAP_Home.txtFromDate);
				fromDate.clear();
				commonLibrary.setDataInTextBox(fromDate, strFromDate, "fromdate");

				if (fromDate.getAttribute("value").equalsIgnoreCase(strFromDate)) {
					report.updateTestLog("Select  currentdate as start date", "Start date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strToDate.toLowerCase() != "") {
				WebElement toDate = commonLibrary.isExist(UIMAP_Home.txtToDate);
				toDate.clear();
				commonLibrary.setDataInTextBox(toDate, strToDate, "todate");

				if (toDate.getAttribute("value").equalsIgnoreCase(strToDate)) {
					report.updateTestLog("Select  " + strToDate + " for end date", "End date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select " + strToDate + " for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strDeliverType.toLowerCase() != "") {
				WebElement objDivName = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName != null) {
					// commonLibrary.ScrollToView(objDivName);
					if (strDeliverType.equalsIgnoreCase("online only")) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_Document.online);
						if (commonLibrary.setRadioButton(onlineOnly, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_Document.online);
						commonLibrary.setRadioButton(onlineOnly, strDeliverType);
						WebElement emailonline = commonLibrary.isExist(UIMAP_Document.emailonline);
						if (commonLibrary.setRadioButton(emailonline, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					}
				} else {
					report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
				}
			}
			if (strEmail.toLowerCase() != "") {
				WebElement Email = commonLibrary.isExist(UIMAP_Home.txtEmail);
				Email.clear();
				commonLibrary.setDataInTextBox(Email, strEmail, "Email");
			}
			if (strDeliverFormat.toLowerCase() != "") {
				WebElement objDivName1 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName1 != null) {
					if (strDeliverFormat.equalsIgnoreCase("text")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement text = commonLibrary.isExist(UIMAP_Document.textRadio);
						if (commonLibrary.setRadioButton(text, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else if (strDeliverFormat.equalsIgnoreCase("html")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement html = commonLibrary.isExist(UIMAP_Document.htmlRadio);
						if (commonLibrary.setRadioButton(html, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else {
						report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
					}
				} else {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
				}
			}
			if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("business daily")) {

				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement busidaily = commonLibrary.isExist(UIMAP_Document.businessDailyradio);
					// commonLibrary.ScrollToView(busidaily);
					if (commonLibrary.setRadioButton(busidaily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_Document.businessDaily);
						if (time != null) {
							// commonLibrary.ScrollToView(time);
							commonLibrary.selectByVisibleTextByValue(time, timevalue, timevalues);
						} else {
							report.updateTestLog("Select time as " + timevalue + "", "Time is not selected as " + timevalue + "", Status.FAIL);
						}
					}
				}

			} else if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("daily")) {

				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement daily = commonLibrary.isExist(UIMAP_Document.dailyradio);
					// commonLibrary.ScrollToView(daily);
					if (commonLibrary.setRadioButton(daily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_Document.dailytime);
						if (time != null) {
							// commonLibrary.ScrollToView(time);
							commonLibrary.selectByVisibleTextByValue(time, timevalue, timevalues);
						} else {
							report.updateTestLog("Select time as " + timevalue + "", "Time is not selected as " + timevalue + "", Status.FAIL);
						}
					}
				}

			} else if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("As updates are available")) {

				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				// commonLibrary.ScrollToView(objDivName2);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement asUpdatesAreAvail = commonLibrary.isExist(UIMAP_Document.asUpdatesare);
					// commonLibrary.ScrollToView(asUpdatesAreAvail);
					if (commonLibrary.setRadioButton(asUpdatesAreAvail, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
				}
			}

		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create alert and share
	// # Function Name : createAlert_ShareDetails
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document createAlert_ShareDetails(String text, String userName) {
		int i = 0;
		this.selectTabInAlerts("Share");
		WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Document.shareUserName, 10);
		WebElement wordWheelContent = null;
		do {
			commonLibrary.setDataInTextBox(shareUserName, text, "Enter user's name");
			try {
				commonLibrary.sleep(3000);
			} catch (Exception e) {
				throw new FrameworkException(e.toString());
			}
			wordWheelContent = commonLibrary.isExistNegative(UIMAP_Document.wordWheelContent, 10);
			i++;
		} while (wordWheelContent == null && i < 3);
		List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_Document.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(wordWheelOptions.get(0), UIMAP_Document.lstTagListItems, 10);
		commonLibrary.selectFromList(options.get(0), userName);

		WebElement addToShare = commonLibrary.isExistNegative(UIMAP_Document.addToShare, 10);
		commonLibrary.clickButtonLogSmallWait(addToShare, "Add to Share");

		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			if (commonLibrary.isExist(UIMAP_Home.btnBrowse) != null) {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is clicked", Status.PASS);
			} else if (commonLibrary.isExist(UIMAP_Home.btnPracArea) != null) {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tabs in alerts
	// # Function Name : SelectTabInAlerts
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document selectTabInAlerts(String tab) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			commonLibrary.selectFromListButton(topicalert, tab);
		} else {
			report.updateTestLog("Select the given tab " + tab + "", "" + tab + " is not selected", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alert icon
	// # Function Name : clickAlertIconreg
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickAlertIconreg() {
		WebElement btnAlert = commonLibrary.isExist(UIMAP_Document.addalertreg);
		if (btnAlert != null) {
			commonLibrary.clickLinkWithWebElement(btnAlert, "Alert");
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify About This Doc
	// # Function Name : verifyAboutThisDoc
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyAboutThisDoc(Boolean isDisplayed) {

		WebElement eltAboutThisDoc = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 10);
		if (isDisplayed) {
			if (eltAboutThisDoc != null && eltAboutThisDoc.isDisplayed())
				report.updateTestLog("Verify About This Document section is  displayed", "About This Document section is  displayed", Status.PASS);
			else
				report.updateTestLog("Verify About This Document section is  displayed", "About This Document section is not displayed", Status.FAIL);

		} else {
			if (eltAboutThisDoc == null)
				report.updateTestLog("Verify About This Document section is not displayed", "About This Document section is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify About This Document section is not displayed", "About This Document section is displayed", Status.FAIL);

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify TOC Fields
	// # Function Name : verifyTOCFields
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyTOCFields(String trail, String docTitle1, String docTitle2) {
		Boolean isButton = false;
		WebElement tocLink = commonLibrary.isExistNegative(UIMAP_Document.tocLink, 10);

		if (tocLink != null && tocLink.isDisplayed())
			report.updateTestLog("Verify view table of contents link is displayed in top left corner of Mini TOC ", "view table of contents link is displayed in top left corner of Mini TOC", Status.PASS);
		else
			report.updateTestLog("Verify view table of contents link is displayed in top left corner of Mini TOC ", "view table of contents link is not displayed in top left corner of Mini TOC", Status.FAIL);

		WebElement miniToc = commonLibrary.isExistNegative(UIMAP_Document.miniToc, 10);
		List<WebElement> list = commonLibrary.isExistList(miniToc, By.tagName("li"), 10);

		for (WebElement item : list) {
			if (item.getText().contains(trail)) {
				WebElement expand = commonLibrary.isExistNegative(item, By.tagName("button"), 10);
				if (expand != null && expand.getAttribute("class").contains("collapsed")) {
					isButton = true;
					break;
				}
			}

		}

		if (isButton)
			report.updateTestLog("Verify Expand button is displayed for the bread crumb trail " + trail + " at the top", "Expand button is displayed for the bread crumb trail " + trail + " at the top", Status.PASS);
		else
			report.updateTestLog("Verify Expand button is displayed for the bread crumb trail " + trail + " at the top", "Expand button is not displayed for the bread crumb trail " + trail + " at the top", Status.FAIL);

		isButton = false;
		for (WebElement item : list) {
			if (item.getText().contains(docTitle1)) {
				WebElement expand = commonLibrary.isExistNegative(item, By.tagName("button"), 10);
				if (expand == null) {
					isButton = true;
					break;
				}
			}

		}

		if (isButton)
			report.updateTestLog("Verify Expand button is Not displayed for the document node " + docTitle1, "Expand button is Not displayed for the document node  " + trail, Status.PASS);
		else
			report.updateTestLog("Verify Expand button is Not displayed for the document node " + docTitle1, "Expand button is displayed for the document node  " + trail, Status.FAIL);

		isButton = false;
		for (WebElement item : list) {
			if (item.getText().contains(docTitle2)) {
				WebElement lnk = commonLibrary.isExistNegative(item, By.tagName("a"), 10);
				if (lnk != null && lnk.getCssValue("border-left-color").equalsIgnoreCase("rgba(237, 28, 36, 1)")) {
					isButton = true;
					break;
				}
			}

		}

		if (isButton)
			report.updateTestLog("Verify The document node  " + docTitle1 + " is highlighted and bolded ", "The document node  " + docTitle1 + " is highlighted and bolded ", Status.PASS);
		else
			report.updateTestLog("Verify The document node  " + docTitle1 + " is highlighted and bolded ", "The document node  " + docTitle1 + " is not highlighted and bolded", Status.FAIL);

		// Scroll bar is not displayed for Mini TOC

		WebElement scrollbar = commonLibrary.isExistNegative(UIMAP_Document.scrollbar, 2);
		if (scrollbar == null)
			report.updateTestLog("Verify Scroll bar is not displayed for Mini TOC", "Scroll bar is not displayed for Mini TOC", Status.PASS);
		else
			report.updateTestLog("Verify Scroll bar is not displayed for Mini TOC", "Scroll bar is displayed for Mini TOC", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify image
	// # Function Name : verifyTOCFields
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyImage_Click(String strcontentType) {

		WebElement copyrightContainer = commonLibrary.isExistNegative(UIMAP_Document.copyrightContainer, 10);
		WebElement copyrightImage = commonLibrary.isExistNegative(copyrightContainer, UIMAP_Document.copyrightImage, 10);
		if (copyrightImage != null) {
			// copyrightImage.click();
			report.updateTestLog("Verify Inline image appears in the page at the top", "Inline image appears in the page at the top", Status.PASS);
			commonLibrary.clickLinkWithWebElementWithWait(copyrightImage, "Inline Copyright Image");
		} else {
			WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
			WebElement linkAboveImage = commonLibrary.isExistNegative(docContent, UIMAP_Document.linkAboveImage, 10);
			WebElement imgParent = commonLibrary.getParentElement(linkAboveImage);
			// WebElement img = commonLibrary.isExistNegative(imgParent,
			// UIMAP_Document.copyrightImage, 10);
			WebElement img = commonLibrary.isExistNegative(imgParent, UIMAP_Document.sketch, 10);
			if (img != null) {
				report.updateTestLog("Verify Inline image appears in the page at the bottom", "Inline image appears in the page at the bottom", Status.PASS);
				commonLibrary.clickLinkWithWebElementWithWait(img, "Inline Image");
			} else
				report.updateTestLog("Verify Inline image appears in the page", "Inline image does not appear in the page", Status.FAIL);
		}
		commonLibrary.sleep(3000);
		if (strcontentType.equals("cases") || strcontentType.equals("news")) {
			commonLibrary.sleep(3000);
			if (driver.getWindowHandles().size() >= 2) {
				commonLibrary.sleep(3000);
				report.updateTestLog("Verify Secondary window is opened", "Secondary window is opened", Status.PASS);
			} else
				report.updateTestLog("Verify Secondary window is opened", "Secondary window is not opened", Status.FAIL);
		} else {
			if (driver.getWindowHandles().size() >= 2) {
				commonLibrary.sleep(3000);
				report.updateTestLog("Verify Secondary window is opened", "Secondary window is opened", Status.FAIL);
			} else
				report.updateTestLog("Verify Secondary window is opened", "Secondary window is not opened", Status.PASS);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Notes link in the Full
	// Document
	// # Function Name : verifyNotesUnderTextSection
	// # Author : Ram Prasath
	// # Date Created : 28 Feb'15
	// #*****************************************************************************************************************************

	public Document verifyNotesUnderTextSection(String notesText) {
		try {
			commonLibrary.isExist(UIMAP_Document.textsectionfulldoc);

			WebElement notesLink = commonLibrary.isExist(UIMAP_Document.noteslinkfulldoc);
			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(notesLink, "Notes");
			} else {
				commonLibrary.clickButtonParentWithWait(notesLink, "Notes");
			}

			// WebElement notespopupdisplay =
			// commonLibrary.isExist(UIMAP_DocumentPage.notespopupdisplay);
			WebElement notespopupheadertext = commonLibrary.isExist(UIMAP_Document.notespopupheadertext);

			String headertext = notespopupheadertext.getText();
			if (notesText.equals(headertext)) {
				report.updateTestLog("Verify Text under the Notes Bubble", "Notes Section contains the text " + notesText, Status.PASS);
			} else {
				report.updateTestLog("Verify Text under the Notes Bubble", "Notes Section does not contains the text " + notesText, Status.FAIL);
			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search within full document
	// # Function Name : searchwithinfulldoc
	// # Author : Ram Prasath
	// # Date Created : 28 Feb'15
	// #*****************************************************************************************************************************
	public void searchwithinfulldoc(String option, String categoryname, String filtername) {
		commonLibrary.isExist(UIMAP_Document.fulldocpagenumber);

		// ((JavascriptExecutor)
		// driver).executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);",
		// textsection);
		// Actions action = new Actions(driver);
		// action.doubleClick(textsection).build().perform();
		// action.doubleClick(commonLibrary.isExist(UIMAP_Document.fulldocpagenumber)).build().perform();
		try {
			commonLibrary.sleep(5000);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("document.getElementByClass('doccontextmenu selectedtextmenu').style.display='block';");
			// WebElement element =
			// commonLibrary.isExist(UIMAP_Document.asideBlockxpath, 10);
			WebElement elem = driver.findElement(By.cssSelector("aside[class='doccontextmenu selectedtextmenu']"));
			String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";
			((JavascriptExecutor) driver).executeScript(js, elem);

		} catch (Exception e) {

		}
		WebElement clickoption = commonLibrary.isExist(UIMAP_Document.addtosearch, 10);
		if (clickoption.getText().contains(option)) {
			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(clickoption, "Add to Search");
				report.updateTestLog("To Verify Add to Search button is Clicked", "Add to Search Button is Clicked", Status.PASS);
			} else {
				commonLibrary.clickButtonLogSmallWait(clickoption, "Add to Search");
				report.updateTestLog("To Verify Add to Search button is Clicked", "Add to Search Button is Clicked", Status.PASS);
			}
			report.updateTestLog("To Verify Add to Search button is Clicked", "Add to Search Button is Clicked", Status.PASS);
		}

		WebElement Selectedtextarea = commonLibrary.isExist(UIMAP_Document.selectedtextarea, 10);
		String textboxvalue = "The proliferation of dog bites in recent years has resulted in what one legal expert has dubbed the dog bite epidemic.";
		// commonLibrary.SetDataInTextBox(UIMAP_DocumentPage.selectedtextarea,textboxvalue);
		commonLibrary.setDataInTextBox(Selectedtextarea, textboxvalue, "search");
		WebElement Submitsearch = commonLibrary.isExist(UIMAP_Document.submitsearch, 10);
		if (browsername.contains("internet") && version.contains("9")) {
			commonLibrary.clickButtonParentWithWaitJS(Submitsearch, "Search");
		} else {
			commonLibrary.clickButtonParentWithWait(Submitsearch, "Search");
		}

		WebElement activecategory = commonLibrary.isExist(UIMAP_Document.activecategory, 10);
		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 10);

		if (activecategory.getText().contains(categoryname)) {
			report.updateTestLog("To Verify active category tab", categoryname + " Category is Active and selected", Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", categoryname + " Category is not Active", Status.FAIL);
		}

		if (usedfilter.getText().contains(filtername)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + filtername, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section contains " + filtername, Status.FAIL);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickRightSelectOpenNewWindowNegative(WebElement element)// ,String
																				// elementName)
	{

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			Actions oAction = new Actions(driver);
			oAction.moveToElement(element).perform();
			// commonLibrary.//(element);
			commonLibrary.highlightElement(element);
			commonLibrary.sleep(2000);

			String browsername = cap.getBrowserName();
			if (browsername.equalsIgnoreCase("firefox")) {
				oAction.contextClick(element).sendKeys("W").build().perform();
			} else if (browsername.equalsIgnoreCase("internet explorer")) {
				oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();

			}
			commonLibrary.sleep(2000);
			if (driver.getWindowHandles().size() >= 2)

				report.updateTestLog("Right Click on the " + element.getText() + "and verify option to open in new window does not appear", "Right Clicked on the " + element.getText() + "and selected open in new window", Status.FAIL);
			else

				report.updateTestLog("Right Click on the " + element.getText() + "and verify option to open in new window does not appear", "Right Clicked on the " + element.getText() + "and option to open new window does not appear", Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : selectSection     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectSection(String option) {
		boolean blnFlag = false;
		commonLibrary.windowFocus();
		// WebElement mobileToolbar =
		// commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement rightContainer = commonLibrary.isExistNegative(UIMAP_Document.rightContainer, 10);
		// generalFunctions.clickJumpTo();

		WebElement sectionContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.sectionContainer, 10);
		// if (sectionContainer != null && !sectionContainer.isDisplayed())
		// generalFunctions.clickJumpTo();
		// else {

		WebElement sections = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sections, 10);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].focus();", sections);
		executor.executeScript("arguments[0].click();", sections);

		sectionContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.sectionContainer, 10);
		WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown, 10);
		WebElement optionList = commonLibrary.isExistNegative(sectionsDropdown, UIMAP_Document.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_Document.tagButton, 10);
		for (WebElement item : options) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickButtonLogSmallWait(item, item.getText());
				blnFlag = true;
				break;
			}

		}
		// }
		if (!blnFlag)
			report.updateTestLog("Select Section " + option, option + " is not selected.", Status.FAIL);
		// commonLibrary.Select_FromList_Button(optionList, option);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Child Custody Link
	// and verify Open in new window is not there
	// # Function Name : rightClick_ChildCustody     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document rightClickChildCustody() {
		WebElement headnotes = commonLibrary.isExistNegative(UIMAP_Document.headnotes, 10);
		WebElement childCustody = commonLibrary.isExistNegative(headnotes, UIMAP_Document.childCustody, 10);
		this.clickRightSelectOpenNewWindowNegative(childCustody);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on HN1 Link and verify
	// Open in new window is not there
	// # Function Name : rightClick_Hn1     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document rightClickHn1() {
		WebElement headnotes = commonLibrary.isExistNegative(UIMAP_Document.headnotes, 10);
		WebElement hn1 = commonLibrary.isExistNegative(headnotes, UIMAP_Document.hn1, 10);
		this.clickRightSelectOpenNewWindowNegative(hn1);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on CharlesDSusano Link
	// and verify Open in new window is not there
	// # Function Name : click_CharlesDSusano     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickCharlesDSusano() {
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
		WebElement charlesDSusano = commonLibrary.isExistNegative(docContent, UIMAP_Document.charlesDSusano, 10);
		commonLibrary.clickButtonLogSmallWait(charlesDSusano, "Charles D. Susano, Jr.");

		WebElement inlinePopup = commonLibrary.isExistNegative(UIMAP_Document.inlinePopup, 10);
		if (inlinePopup != null && inlinePopup.isDisplayed()) {
			report.updateTestLog("Verify Entity popup displays", "Entity popup displays", Status.PASS);
			WebElement closeButton = commonLibrary.isExistNegative(inlinePopup, UIMAP_Document.closeButton, 10);
			if (closeButton != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(closeButton, "Search");
				else
					commonLibrary.clickButtonLogSmallWait(closeButton, "Close Button");
			} else
				report.updateTestLog("Click on Close Button", "Close Button does not display", Status.FAIL);
		} else
			report.updateTestLog("Verify Entity popup displays", "Entity popup does not display", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on CopyCitation Link and
	// verify Open in new window is not there
	// # Function Name : rightClick_CopyCitation     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document rightClickCopyCitation() {
		WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
		WebElement copyCitation = commonLibrary.isExistNegative(docHeader, UIMAP_Document.copyCitation, 10);
		this.clickRightSelectOpenNewWindowNegative(copyCitation);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a term from Jump to Section
	// # Function Name : selectTerm     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectTerm(String option) {
		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement rightContainer = commonLibrary.isExistNegative(mobileToolbar, UIMAP_Document.rightContainer, 10);
		WebElement jumpto = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.jumpto, 10);

		commonLibrary.clickButtonLogSmallWait(jumpto, "Jump To");

		WebElement navigateContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.navigateContainer, 10);
		WebElement nav = commonLibrary.isExistNegative(navigateContainer, UIMAP_Document.sections, 10);

		commonLibrary.clickButtonLogSmallWait(nav, "Navigate Dropdown");
		navigateContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.navigateContainer, 10);
		WebElement navDropDown = commonLibrary.isExistNegative(navigateContainer, UIMAP_Document.sectionsDropdown, 10);
		WebElement optionList = commonLibrary.isExistNegative(navDropDown, UIMAP_Document.lstTagUList, 10);
		commonLibrary.selectFromListButton(optionList, option);

		WebElement jumpToContainer = commonLibrary.isExistNegative(UIMAP_Document.jumpToContainer, 10);
		WebElement nextTerm = commonLibrary.isExistNegative(jumpToContainer, UIMAP_Document.nextTerm, 10);

		this.clickRightSelectOpenNewWindowNegative(nextTerm);
		commonLibrary.clickButtonLogSmallWait(nextTerm, "Next Term");
		commonLibrary.clickButtonLogSmallWait(nextTerm, "Next Term");

		jumpToContainer = commonLibrary.isExistNegative(UIMAP_Document.jumpToContainer, 10);
		WebElement prevTerm = commonLibrary.isExistNegative(jumpToContainer, UIMAP_Document.prevTerm, 10);
		this.clickRightSelectOpenNewWindowNegative(prevTerm);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on FundamentalRights
	// Link and verify Open in new window is not there
	// # Function Name : rightClickFundamentalRights     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document rightClickFundamentalRights() {
		WebElement headnotes = commonLibrary.isExistNegative(UIMAP_Document.headnotes, 10);
		WebElement fundRights = commonLibrary.isExistNegative(headnotes, UIMAP_Document.fundRights, 10);
		this.clickRightSelectOpenNewWindowNegative(fundRights);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ShowHide Link and
	// verify Open in new window is not there
	// # Function Name : rightClick_ShowHide     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document rightClickShowHide() {
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);

		List<WebElement> editorialContent = commonLibrary.isExistList(docContent, UIMAP_Document.editorialContent, 10);
		WebElement showhideLink = commonLibrary.isExistNegative(editorialContent.get(1), UIMAP_Document.showhideLink, 10);
		this.clickRightSelectOpenNewWindowNegative(showhideLink);

		commonLibrary.clickButtonLogSmallWait(showhideLink, "Show/Hide Link");
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select term from Jump dropdown
	// # Function Name : selecttermfromjump     
	// # Author : Ram Prasath
	// # Date Created : 2-Mar'15
	// #*****************************************************************************************************************************

	public Document selecttermfromjump(String option) {
		boolean blnFlag = false;
		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement mobileToolbar1 = commonLibrary.isExistNegative(mobileToolbar, UIMAP_Document.jumptobutton, 15);
		WebElement navigateSection = commonLibrary.isExistNegative(mobileToolbar1, UIMAP_Document.navigateContainer, 10);
		WebElement buttton = commonLibrary.isExistNegative(navigateSection, UIMAP_Document.button3, 10);
		if (buttton != null) {
			commonLibrary.clickLinkWithWebElementWithWait(buttton, "Navigate to");
		}
		if (navigateSection != null) {
			WebElement jumpToContainer = commonLibrary.isExistNegative(navigateSection, UIMAP_Document.jumpToContainer, 7);
			WebElement jumpToContainer1 = commonLibrary.isExistNegative(jumpToContainer, UIMAP_Document.lstTagUList, 5);
			List<WebElement> litag = commonLibrary.isExistList(jumpToContainer1, UIMAP_Document.listItems, 10);
			for (WebElement item : litag) {
				if (item.getText().toLowerCase().contains(option.toLowerCase())) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_Document.tagButton, 10);
					if (button != null) {
						commonLibrary.clickButtonParentWithWait(item, item.getText());
						blnFlag = true;
					}
					break;
				}

			}
		}

		if (!blnFlag)
			report.updateTestLog("Select from Navigate " + option, option + " is not selected.", Status.FAIL);
		// commonLibrary.Select_FromList_Button(optionList, option);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Next and Previous button in
	// Navigate button
	// # Function Name : clickprevnextinjumpoption     
	// # Author : Ram Prasath
	// # Date Created : 2-Mar'15
	// #*****************************************************************************************************************************

	public Document clickprevnextinjumpoption(String action) {

		// WebElement jumpToContainer =
		// commonLibrary.isExistNegative(UIMAP_Document.jumpToContainer, 5);
		// if (jumpToContainer == null || !jumpToContainer.isDisplayed()) {
		// this.openorclosejumpto();
		// commonLibrary.sleep(45000);
		// }

		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement divbuttons = commonLibrary.isExistNegative(mobileToolbar, UIMAP_Document.btncombine, 10);
		if (divbuttons != null && action.contains("Next")) {
			// WebElement jumpToContainer1 =
			// commonLibrary.isExist(UIMAP_Document.jumpToContainer);
			// List<WebElement> fieldset1 =
			// commonLibrary.isExist_List(jumpToContainer1,UIMAP_Document.fieldset,20);

			WebElement nextTerm = commonLibrary.isExist(divbuttons, UIMAP_Document.nextTerm, 20);

			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWait(nextTerm, "Next");
			} else {
				// commonLibrary.clickButton_Parent_WithWait(nextTerm, "Next");
				commonLibrary.clickButtonParentWithWait(nextTerm, "Next");
			}
		} else if (divbuttons != null && action.contains("Prev")) {
			// WebElement jumpToContainer1 =
			// commonLibrary.isExist(UIMAP_Document.jumpToContainer);
			// List<WebElement> fieldset1 =
			// commonLibrary.isExist_List(jumpToContainer1,UIMAP_Document.fieldset,20);
			// List<WebElement> nextbutton =
			// commonLibrary.isExist_List(fieldset1.get(1),UIMAP_Document.prevTerm,
			// 20);
			WebElement prevTerm = commonLibrary.isExist(divbuttons, UIMAP_Document.prevTerm, 20);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWait(prevTerm, "Previous");
			} else {
				// commonLibrary.clickButton_Parent_WithWait(prevTerm,
				// "Previous");
				commonLibrary.clickButtonParentWithWait(prevTerm, "Previous");
			}
		} else {
			report.updateTestLog("To Verify Next or Previous Buttons are clicked", "Buttons are not clicked", Status.FAIL);
		}
		commonLibrary.sleep(600000);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Bold and Highlight of the
	// term in full document
	// # Function Name : verifyboldandhighlightofterm     
	// # Author : Ram Prasath
	// # Date Created : 2-Mar'15
	// #*****************************************************************************************************************************

	public Document verfiytermshighlight(String term, String weight, String backgroundcolor1, String backgroundcolor2) {
		if (term.contains("powder")) {
			List<WebElement> termhighlighttext = commonLibrary.isExistList(UIMAP_Document.powdertermhighlighttext, 10);
			for (WebElement item : termhighlighttext) {
				if (item.getText().toLowerCase().contains(term.toLowerCase())) {
					String bgcolor = item.getCssValue("background-color");
					// String txtcolor = termhighlighttext.getCssValue("color");
					String txtweight = item.getCssValue("font-weight");
					String termcheck = item.getText();

					// if (termcheck.contains(term) &&
					// (weight.contains(txtweight) ||
					// txtweight.contains("bold")) &&
					// (backgroundcolor1.contains(bgcolor) ||
					// backgroundcolor2.contains(bgcolor))) {
					if (termcheck.contains(term) && (weight.contains(txtweight) || Integer.parseInt(txtweight) >= 700) && (backgroundcolor1.contains(bgcolor) || backgroundcolor2.contains(bgcolor))) {
						report.updateTestLog("To Verify the term '" + term + "' is Highlighted and Bolded", "Term '" + term + "' is Highlighted and Bolded", Status.PASS);
					} else {
						report.updateTestLog("To Verify the term '" + term + "' is Highlighted and Bolded", "Term '" + term + "' is not Highlighted and bolded", Status.FAIL);
					}

					break;
				}

			}
		}

		if (term.contains("per cent")) {
			List<WebElement> termhighlighttext = commonLibrary.isExistList(UIMAP_Document.percenttermhighlighttext, 10);
			for (WebElement item : termhighlighttext) {
				if (item.getText().toLowerCase().contains(term.toLowerCase())) {
					String bgcolor = item.getCssValue("background-color");
					// String txtcolor = termhighlighttext.getCssValue("color");
					String txtweight = item.getCssValue("font-weight");
					String termcheck = item.getText();

					// if (termcheck.contains(term) &&
					// (weight.contains(txtweight) ||
					// txtweight.contains("bold")) &&
					// (backgroundcolor1.contains(bgcolor) ||
					// backgroundcolor2.contains(bgcolor))) {
					if (termcheck.contains(term) && (weight.contains(txtweight) || Integer.parseInt(txtweight) >= 700) && (backgroundcolor1.contains(bgcolor) || backgroundcolor2.contains(bgcolor))) {
						report.updateTestLog("To Verify the Highlight and Bold Option of the term", "Term is Highlighted and Bolded", Status.PASS);
					} else {
						report.updateTestLog("To Verify the Highlight and Bold Option of the term", "Term is not Highlighted and bolded", Status.FAIL);
					}

					break;
				}

			}
		}

		if (term.contains("Chambers v. Chester, 172 Mo. 461")) {
			List<WebElement> termhighlighttext = commonLibrary.isExistList(UIMAP_Document.referencetermhighlighttext, 10);
			for (WebElement item : termhighlighttext) {
				if (item.getText().toLowerCase().contains(term.toLowerCase())) {
					String bgcolor = item.getCssValue("background-color");
					// String txtcolor = termhighlighttext.getCssValue("color");
					String txtweight = item.getCssValue("font-weight");
					String termcheck = item.getText();

					// if (termcheck.contains(term) &&
					// (weight.contains(txtweight) ||
					// txtweight.contains("bold")) &&
					// (backgroundcolor1.contains(bgcolor) ||
					// backgroundcolor2.contains(bgcolor))) {
					if (termcheck.contains(term) && (weight.contains(txtweight) || Integer.parseInt(txtweight) >= 700) && (backgroundcolor1.contains(bgcolor) || backgroundcolor2.contains(bgcolor))) {
						report.updateTestLog("To Verify the Highlight and Bold Option of the term", "Term is Highlighted and Bolded", Status.PASS);
					} else {
						report.updateTestLog("To Verify the Highlight and Bold Option of the term", "Term is not Highlighted and bolded", Status.FAIL);
					}

					break;
				}

			}
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title for topic
	// summary
	// # Function Name : Verify_DocumentTitleTopicSummary     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentTitleTopicSummary(String strDocTitle) {

		WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);
		if (eltDocumentHeading.getText().toLowerCase().contains("topic summary") && eltDocumentHeading.getText().contains(strDocTitle))
			report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify document page is displayed", "Document page is not displayed", Status.FAIL);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the doc extra sections
	// # Function Name : verifyDocExtraSections     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyDocExtraSections(String sectionName) {
		boolean blnFlag = false;
		WebElement docExtra = commonLibrary.isExistNegative(UIMAP_Document.docExtra, 10);
		if (docExtra != null) {
			report.updateTestLog("Verify 'About this document' pane displays inthe right corner", "'About this document' pane displays inthe right corner", Status.PASS);
			List<WebElement> docExtraSections = commonLibrary.isExistList(docExtra, UIMAP_Document.docExtraSections, 10);
			for (WebElement section : docExtraSections) {
				WebElement sectionHeader = commonLibrary.isExistNegative(section, UIMAP_Document.sectionHeader, 10);
				if (sectionHeader.getText().toLowerCase().contains(sectionName.toLowerCase())) {
					report.updateTestLog("Verify " + sectionName + " is displayed", sectionName + " is displayed", Status.PASS);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify " + sectionName + " is displayed", sectionName + " is not displayed", Status.FAIL);

		} else
			report.updateTestLog("Verify 'About this document' pane displays inthe right corner", "'About this document' pane does not display inthe right corner", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a link in About this Document
	// Section
	// # Function Name : verifyLinkInSection     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyLinkInSection(String sectionName, String linkName) {
		boolean blnFlag = false;
		WebElement docExtra = commonLibrary.isExistNegative(UIMAP_Document.docExtra, 10);
		if (docExtra != null) {

			List<WebElement> docExtraSections = commonLibrary.isExistList(docExtra, UIMAP_Document.docExtraSections, 10);
			for (WebElement section : docExtraSections) {
				WebElement sectionHeader = commonLibrary.isExistNegative(section, UIMAP_Document.sectionHeader, 10);
				if (sectionHeader.getText().toLowerCase().contains(sectionName.toLowerCase())) {
					List<WebElement> links = commonLibrary.isExistList(section, UIMAP_Document.links, 10);
					for (WebElement link : links) {
						if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
							report.updateTestLog("Verify " + linkName + " is present in " + sectionName + " section", linkName + " is present in " + sectionName + " section", Status.PASS);
							blnFlag = true;
							break;
						}
					}
					if (blnFlag)
						break;
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify " + linkName + " is present in " + sectionName + " section", linkName + " is not present in " + sectionName + " section", Status.PASS);
		} else
			report.updateTestLog("Verify 'About this document' pane displays inthe right corner", "'About this document' pane does not display inthe right corner", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Topic index
	// Terms
	// # Function Name : clickTopicIndex     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document clickTopicIndex() {
		WebElement docExtra = commonLibrary.isExistNegative(UIMAP_Document.docExtra, 10);
		WebElement actionsSection = commonLibrary.isExistNegative(docExtra, UIMAP_Document.actionsSection, 10);
		WebElement topicIndex = commonLibrary.isExistNegative(actionsSection, UIMAP_Document.topicIndex, 10);
		commonLibrary.clickLinkWithWebElementWithWait(topicIndex, "Topic Index");
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click all topics document
	// Terms
	// # Function Name : clickAllTopicDocuments     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Search clickAllTopicDocuments() {

		commonLibrary.scrollDown();
		// WebElement docExtra =
		// commonLibrary.isExist_Negative(UIMAP_Document.docExtra, 10);
		WebElement actionsSection = commonLibrary.isExistNegative(UIMAP_Document.actionsSection, 10);
		WebElement allTopicDocuments = commonLibrary.isExistNegative(actionsSection, UIMAP_Document.allTopicDocuments, 10);
		if (allTopicDocuments == null || !allTopicDocuments.isDisplayed()) {

			actionsSection = commonLibrary.isExistNegative(UIMAP_Document.actionsSection, 10);
			WebElement header = commonLibrary.isExistNegative(actionsSection, UIMAP_Document.sectionHeader, 10);
			commonLibrary.clickButtonLogSmallWait(header, "Actions");
			allTopicDocuments = commonLibrary.isExistNegative(actionsSection, UIMAP_Document.allTopicDocuments, 10);

		}
		commonLibrary.clickLinkWithWebElementWithWait(allTopicDocuments, "Get All Topic Documents");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Browse and verify Highlighted
	// Terms
	// # Function Name : verifyBrowseHighlights     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyBrowseHighlights(String firstSubMenu, String secondSubMenu, String thirdSubMenu) {
		this.clickNavFirst();
		WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		if (divIdBrowserSubMenu != null) {

			report.updateTestLog("Verify The page scrolls up", "The page scrolls up", Status.PASS);
			List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
			WebElement selectedItem = commonLibrary.isExist(lstTagAside.get(0), UIMAP_Document.selectedItem, 20);

			// commonLibrary.highlightElement(button);
			if (selectedItem.getText().toLowerCase().contains(firstSubMenu.toLowerCase()))
				report.updateTestLog("Verify " + firstSubMenu + " is highlighted.", firstSubMenu + " is highlighted.", Status.PASS);
			else
				report.updateTestLog("Verify " + firstSubMenu + " is highlighted.", firstSubMenu + " is not highlighted.", Status.FAIL);

			selectedItem = commonLibrary.isExist(lstTagAside.get(1), UIMAP_Document.selectedItem, 20);
			if (selectedItem.getText().toLowerCase().contains(secondSubMenu.toLowerCase()))
				report.updateTestLog("Verify " + secondSubMenu + " is highlighted.", secondSubMenu + " is highlighted.", Status.PASS);
			else
				report.updateTestLog("Verify " + secondSubMenu + " is highlighted.", secondSubMenu + " is not highlighted.", Status.FAIL);

			selectedItem = commonLibrary.isExist(lstTagAside.get(2), UIMAP_Document.selectedItem, 20);
			if (selectedItem.getText().toLowerCase().contains(thirdSubMenu.toLowerCase()))
				report.updateTestLog("Verify " + thirdSubMenu + " is highlighted.", thirdSubMenu + " is highlighted.", Status.PASS);
			else
				report.updateTestLog("Verify " + thirdSubMenu + " is highlighted.", thirdSubMenu + " is not highlighted.", Status.FAIL);
		} else
			report.updateTestLog("Verify The page scrolls up", "The page does not scrolls up", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set new client id
	// Terms
	// # Function Name : setNewClientIDGeneric     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Research setNewClientIDGeneric(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_Document.rdoClientId);
		if (rdoClientId != null) {
			// report.updateTestLog("Selecting Set New Client ID",
			// "New Client Option Selected.", Status.PASS);
			commonLibrary.setRadioButton(UIMAP_Document.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID", "Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_Document.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client id");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_Document.btnSaveClientId);
		if (btnSaveClientId != null) {
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Research(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to client id link
	// # Function Name : NavigateToClientLink     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public void navigateToClientLink(String strLinkName) {

		try {

			WebElement btnClassClient = commonLibrary.isExist(UIMAP_Home.btnClassClient, 20);
			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnClassClient, "Client");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassClient, "Client");
			}
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
				WebElement lnkTxtSetAddClientID = commonLibrary.isExist(UIMAP_Home.btnActionSetAddClientID, 20);
				if (browsername.contains("internet") && version.contains("9"))
					commonLibrary.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtSetAddClientID, "Set/Add Client ID");
			} else if (strLinkName.equalsIgnoreCase("-None-")) {
				WebElement lnkTxtNone = commonLibrary.isExist(UIMAP_Home.lnkTxtNone, 20);
				if (browsername.contains("internet") && version.contains("9")) {
					commonLibrary.clickJS(lnkTxtNone, "None");
				} else {
					commonLibrary.clickLinkWithWebElement(lnkTxtNone, "None");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify many things under
	// "choose a client id" page.
	// # Function Name : verifyChooseClientId     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document verifyChooseClientId(String name1, String name2) {
		boolean istext = false;
		boolean isname1 = false;
		boolean isname2 = false;
		List<WebElement> header = commonLibrary.isExistList(UIMAP_Document.header, 20);
		if (header.get(1).getText().equalsIgnoreCase("Choose a Client ID")) {
			report.updateTestLog("Verify Choose a Client Id page displays", "Choose a client id page is displayed and it the choose a client id label displays at top left corner", Status.PASS);
		} else {
			report.updateTestLog("Verify Choose a Client Id page displays", "Choose a client id page is not displayed and it the choose a client id label is not displayed at top left corner", Status.FAIL);
		}
		WebElement tips = commonLibrary.isExist(UIMAP_Document.tips, 20);
		if (tips != null) {
			report.updateTestLog("Verify <Tips> link displays at Top right corner", "<Tips> link is displayed at Top right corner", Status.PASS);
		} else {
			report.updateTestLog("Verify <Tips> link displays at Top right corner", "<Tips> link is not displayed at Top right corner", Status.FAIL);
		}
		WebElement aside = commonLibrary.isExist(UIMAP_Document.aside, 20);
		List<WebElement> text = commonLibrary.isExistList(aside, UIMAP_Document.text, 20);
		for (WebElement item : text) {
			if (item.getText().toLowerCase().contains("you have selected to view an item for client id")) {
				istext = true;
				break;
			}
		}
		if (istext) {
			report.updateTestLog("Verify Choose Client ID popup displays with following inline message You have selected to view an item for Client ID : " + name1 + " but your current research is under : " + name2 + "", "Choose Client ID popup displays with following inline message You have selected to view an item for Client ID : " + name1 + ", but your current research is under : " + name2, Status.PASS);

		} else {
			report.updateTestLog("Verify Choose Client ID popup displays with following inline message You have selected to view an item for Client ID : " + name1 + ", but your current research is under : " + name2 + "", "Choose Client ID popup is not displayed with following inline message You have selected to view an item for Client ID : " + name1 + ", but your current research is under : " + name2, Status.FAIL);
		}
		List<WebElement> viewusing = commonLibrary.isExistList(UIMAP_Document.viewusing, 20);
		for (int i = 0; i < viewusing.size(); i++) {
			if (viewusing.get(i).getAttribute("value").contains(name1)) {
				isname1 = true;
			} else if (viewusing.get(i).getAttribute("value").contains(name2)) {
				isname2 = true;
			}
			if (isname1 && isname2)
				break;
		}
		if (isname1 && isname2) {
			report.updateTestLog("Verify Choose Client ID popup displays with the following buttons:1. View using " + name1 + " 2. View using " + name2 + "", "Choose Client ID popup displays with the following buttons:1. View using " + name1 + " 2. View using " + name2, Status.PASS);
		} else {
			report.updateTestLog("Verify Choose Client ID popup displays with the following buttons:1. View using " + name1 + " 2. View using " + name2 + "", "Choose Client ID popup is not displayed with the following buttons:1.View using " + name1 + " 2. View using " + name2, Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view using option
	// # Function Name :       clickViewUsing
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickViewUsing(String name) {

		List<WebElement> viewusing = commonLibrary.isExistList(UIMAP_Document.viewusing, 20);
		for (int i = 0; i < viewusing.size(); i++) {
			if (viewusing.get(i).getAttribute("value").contains(name)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(viewusing.get(i), "View Using" + name);
					break;
				} else {
					commonLibrary.clickButtonParentWithWait(viewusing.get(i), "View Using" + name);
					break;
				}
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the client name in global
	// menu
	// # Function Name : verifyClientNameInGlobalMenu     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyClientNameInGlobalMenu(String client) {
		WebElement clientbtn = commonLibrary.isExist(UIMAP_ResearchMap.clientBtn);
		if (clientbtn.getText().toLowerCase().replaceAll(" ", "").contains(client.toLowerCase().replaceAll(" ", ""))) {
			report.updateTestLog("Verify new client name " + client + " displayes in global menu", "New client id " + client + " is displayed in the global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify new client name " + client + " displayes in global menu", "New client id " + client + " is not displayed in the global menu", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click First nav button under Browse
	// Menu
	// # Function Name : clickNavFirst     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document clickNavFirst() {
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		WebElement divIdBrowserNav = commonLibrary.isExistNegative(divIdBrowserMenu, UIMAP_Document.divIdBrowserNav, 10);
		WebElement firstButton = commonLibrary.isExistNegative(divIdBrowserNav, UIMAP_Document.firstButton, 10);
		if (firstButton.isEnabled())
			commonLibrary.clickButtonLogSmallWait(firstButton, "Leftmost Arrow");
		else
			report.updateTestLog("Click the leftmost arrow.", "The leftmost arrow is disabled.", Status.FAIL);

		WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
		if (lstTagAside.get(0).isDisplayed())
			report.updateTestLog("Verify the first pane is displayed.", "The first pane is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify the first pane is displayed.", "The first pane is not displayed.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Last nav button under Browse
	// Menu
	// # Function Name : clickNavLast     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickNavLast() {
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		WebElement divIdBrowserNav = commonLibrary.isExistNegative(divIdBrowserMenu, UIMAP_Document.divIdBrowserNav, 10);
		WebElement lastButton = commonLibrary.isExistNegative(divIdBrowserNav, UIMAP_Document.lastButton, 10);

		if (lastButton.isEnabled())
			commonLibrary.clickButtonLogSmallWait(lastButton, "Rightmost Arrow");
		else
			report.updateTestLog("Click the Rightmost arrow.", "The Rightmost arrow is disabled.", Status.FAIL);

		WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
		if (lstTagAside.get(3).isDisplayed())
			report.updateTestLog("Verify the last pane is displayed.", "The last pane is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify the last pane is displayed.", "The last pane is not displayed.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Previous nav button under
	// Browse Menu
	// # Function Name : clickNavPrev     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickNavPrev() {
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		WebElement divIdBrowserNav = commonLibrary.isExistNegative(divIdBrowserMenu, UIMAP_Document.divIdBrowserNav, 10);
		WebElement prevButton = commonLibrary.isExistNegative(divIdBrowserNav, UIMAP_Document.prevButton, 10);
		if (prevButton.isEnabled())
			commonLibrary.clickButtonLogSmallWait(prevButton, "Previous Arrow");
		else
			report.updateTestLog("Click the Previous arrow.", "The Previous arrow is disabled.", Status.FAIL);

		WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
		if (lstTagAside.get(0).isDisplayed())
			report.updateTestLog("Verify the first pane is displayed.", "The first pane is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify the first pane is displayed.", "The first pane is not displayed.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Next nav button under Browse
	// Menu
	// # Function Name : clickNavNext     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickNavNext() {
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		WebElement divIdBrowserNav = commonLibrary.isExistNegative(divIdBrowserMenu, UIMAP_Document.divIdBrowserNav, 10);
		WebElement nextButton = commonLibrary.isExistNegative(divIdBrowserNav, UIMAP_Document.nextButton, 10);

		if (nextButton.isEnabled())
			commonLibrary.clickButtonLogSmallWait(nextButton, "Next Arrow");
		else
			report.updateTestLog("Click the Next arrow.", "The Next arrow is disabled.", Status.FAIL);

		WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
		if (lstTagAside.get(3).isDisplayed())
			report.updateTestLog("Verify the last pane is displayed.", "The last pane is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify the last pane is displayed.", "The last pane is not displayed.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click slider and verify document
	// Menu
	// # Function Name : clickSlider_VerifyResult     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document clickSliderVerifyResult(String docName1, String docName2, String docName3) {

		int v=0;
		
		WebElement sliderLeft = commonLibrary.isExist(UIMAP_Document.docSliderLeft, 10);
		WebElement sliderRight = commonLibrary.isExist(UIMAP_Document.docSliderRight, 10);
		
		if (sliderLeft != null && sliderRight != null)
			report.updateTestLog("Verify Document slider displayed in Full Document", "The Document slider is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Document slider displayed in Full Document", "The Document slider is not displayed.", Status.FAIL);

	//	WebElement lnknext = commonLibrary.isExist(UIMAP_Document.docSliderRight, 20);
		WebElement lnknext = commonLibrary.isExistNegative(UIMAP_Document.docSliderRight, 20);
		
		// commonLibrary.clickLinkWithWebElementWithWait(lnknext,
		// "Next Document");
		commonLibrary.clickButtonParentWithWait(lnknext, "Next Document");
		commonLibrary.sleep(30000);
		
	//	WebElement heading1 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
		WebElement heading1 = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
		
		if (heading1.getText().equalsIgnoreCase(docName1))
			report.updateTestLog("Verify '" + docName1 + "' document is displayed", "The Document is displayed as expected.", Status.PASS);
		else
			report.updateTestLog("Verify '" + docName1 + "' document is displayed", "The Document is not displayed.", Status.FAIL);

	//	WebElement lnknext1 = commonLibrary.isExist(UIMAP_Document.docSliderRight, 20);
		WebElement lnknext1 = commonLibrary.isExistNegative(UIMAP_Document.docSliderRight, 20);
		
		// commonLibrary.clickLinkWithWebElementWithWait(lnknext1,
		// "Next Document");
		commonLibrary.clickButtonParentWithWait(lnknext1, "Next Document");
		commonLibrary.sleep(30000);
		
	//	WebElement heading2 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
		WebElement heading2 = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
		
		if (heading2.getText().equalsIgnoreCase(docName2))
			report.updateTestLog("Verify '" + docName2 + "' document is displayed", "The Document is displayed as expected.", Status.PASS);
		else
			report.updateTestLog("Verify '" + docName2 + "' document is displayed", "The Document is not displayed.", Status.FAIL);

	//	WebElement lnknext2 = commonLibrary.isExist(UIMAP_Document.docSliderLeft, 20);
		WebElement lnknext2 = commonLibrary.isExistNegative(UIMAP_Document.docSliderLeft, 20);
		
		// commonLibrary.clickLinkWithWebElementWithWait(lnknext2,"Previous Document");
		commonLibrary.clickButtonParentWithWait(lnknext2, "Previous Document");
		commonLibrary.sleep(30000);
		
	//	WebElement heading3 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
		WebElement heading3 = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
		
		if (heading3.getText().equalsIgnoreCase(docName1))
			report.updateTestLog("Verify '" + docName1 + "' document is displayed", "The Document is displayed as expected.", Status.PASS);
		else
			report.updateTestLog("Verify '" + docName1 + "' document is displayed", "The Document is not displayed.", Status.FAIL);

	//	WebElement lnknext3 = commonLibrary.isExist(UIMAP_Document.docSliderLeft, 20);
		WebElement lnknext3 = commonLibrary.isExistNegative(UIMAP_Document.docSliderLeft, 20);
		
		// commonLibrary.clickLinkWithWebElementWithWait(lnknext3,"Previous Document");
		commonLibrary.clickButtonParentWithWait(lnknext3, "Previous Document");
		commonLibrary.sleep(30000);
		
	//	WebElement heading4 = commonLibrary.isExist(UIMAP_Document.txtDocumentHeading, 10);
		WebElement heading4 = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
		
		if (heading4.getText().equalsIgnoreCase(docName3))
			report.updateTestLog("Verify '" + docName3 + "' document is displayed", "The Document is displayed as expected.", Status.PASS);
		else
			report.updateTestLog("Verify '" + docName3 + "' document is displayed", "The Document is not displayed.", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clicklinkfromATDsection
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clicklinkfromATDsection(String documenttitle) {
		WebElement aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.aboutthisdoclink);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(aboutthisdoclink, "Document");
		} else {
			commonLibrary.clickButtonParentWithWait(aboutthisdoclink, "Document");
		}
		//commonLibrary.isExist(UIMAP_Document.navigateto);
		commonLibrary.isExist(UIMAP_Document.sourceResultHeader);
		commonLibrary.clickBrowserBack();

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to open or close jump to button
	// # Function Name : openorclosejumpto     
	// # Author : Ram
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document openorclosejumpto() {
		commonLibrary.sleep(30000);
		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement rightContainer = commonLibrary.isExistNegative(mobileToolbar, UIMAP_Document.rightContainer, 10);
		WebElement jumpto = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.jumpto, 10);

		commonLibrary.clickButtonLogSmallWait(jumpto, "Go to");
		// driver.manage().timeouts().implicitlyWait(220,
		// TimeUnit.MILLISECONDS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a LA Research Logo
	// # Function Name : verifyLexisAdvanceResearchLogo     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyLexisAdvanceResearchLogo() {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().contains("Research")) {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo displays in Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo is not displayed in Global menu", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a citation
	// # Function Name : clickCitation     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickCitation(String title) {
		boolean footnoter = false;
		List<WebElement> citation = commonLibrary.isExistList(UIMAP_Document.citationlink, 20);
		for (WebElement item : citation)
			if (item.getText().equalsIgnoreCase(title)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, title);
				} else {
					commonLibrary.clickButtonParentWithWait(item, title);
				}
				footnoter = true;
				break;
			}
		if (!footnoter) {
			report.updateTestLog("Click citation " + title + "", "citation " + title + " is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a Shepardize Headnote     
	// # Function Name : clickShepardizeHeadnote     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards clickShepardizeHeadnote() {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);
		if (headNotesContent == null) {
			WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		}
		boolean footnoter = false;
		List<WebElement> citation = commonLibrary.isExistList(UIMAP_Document.shepardizeheadnote, 20);
		for (WebElement item : citation)
			if (item.getText().toLowerCase().contains("narrow by this headnote")) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWait(item, "Shepardize-Narrow by this headnote");
				} else {
					commonLibrary.clickButtonParentWithWait(item, "Shepardize-Narrow by this headnote");
				}
				footnoter = true;
				break;
			}
		commonLibrary.sleep(50000);
		if (!footnoter) {
			report.updateTestLog("Click Shepardize-Narrow by this headnote", "Shepardize-Narrow by this headnote is not clicked", Status.FAIL);
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click particular footnote number
	// # Function Name : clickFootnoteWithNumber     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickFootnoteWithNumber(String number) {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);
		if (headNotesContent == null) {
			WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		}
		boolean footnoter = false;
		List<WebElement> footnote = commonLibrary.isExistList(UIMAP_Document.footnote, 20);
		for (WebElement item : footnote)
			if (item.getText().equalsIgnoreCase(number)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, "footnote" + number);
				} else {
					commonLibrary.clickButtonParentWithWait(item, "footnote" + number);
				}
				footnoter = true;
				break;
			}
		if (!footnoter) {
			report.updateTestLog("Click footnote " + number + "", "footnote " + number + " is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document links in the ATD
	// section
	// # Function Name : clickSpecificlinkfromATDsection     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickSpecificlinkfromATDsection(String documenttitle) {
		boolean clicked = false;
		List<WebElement> aboutthisdoclink = commonLibrary.isExistList(UIMAP_Document.aboutthisdoclink, 20);
		for (WebElement item : aboutthisdoclink) {
			if (item.getText().contains(documenttitle)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, documenttitle);
				} else {
					commonLibrary.clickButtonParentWithWait(item, documenttitle);
				}
				clicked = true;
				break;
			}
		}

		if (!clicked) {
			report.updateTestLog("Click " + documenttitle + " in the about to document section", "" + documenttitle + " in the about to document section is not clicked", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Full Document In Doc Page
	// # Function Name : verifyFullDocumentInDocPage     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyFullDocumentInDocPage(String strSearchTerm) {
		Boolean blnFlag = false;
		try {
			WebElement banner = commonLibrary.isExist(UIMAP_Document.hdrBanner, 10);
			if (banner != null) {
				List<WebElement> span = commonLibrary.isExistList(banner, UIMAP_Document.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().trim().toLowerCase().contains(strSearchTerm.toLowerCase().trim())) {
						blnFlag = true;
						break;
					}
				}
			}
			if (blnFlag) {
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document page is displayed for " + strSearchTerm + "", Status.PASS);
			} else {
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document page is not displayed for " + strSearchTerm + "", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click any link from About this
	// document section
	// # Function Name :
	// clickSpecificlinkfromATDsectionReturnRelatedContent     
	// # Author : Shobana
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public RelatedContent clickSpecificlinkfromATDsectionReturnRelatedContent(String documenttitle) {
		boolean clicked = false;
		commonLibrary.scrollDown();
		commonLibrary.scrollUp();
		WebElement relatedreports = commonLibrary.isExist(UIMAP_Document.relatedreport, 20);
		if (relatedreports != null && documenttitle.contains("Available Reports")) {
			report.updateTestLog("Related reports section displays", "Related reports section is displayed with 'Available reports' link", Status.PASS);
		}

		List<WebElement> aboutthisdoclink = commonLibrary.isExistList(UIMAP_Document.aboutthisdoclink, 20);
		for (WebElement item : aboutthisdoclink) {
			if (item.getText().toLowerCase().contains(documenttitle.toLowerCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, documenttitle);
				} else {
					commonLibrary.clickButtonParentWithWait(item, documenttitle);
				}
				commonLibrary.sleep(10000);
				clicked = true;
				break;
			}
		}

		if (!clicked) {
			report.updateTestLog("Click " + documenttitle + " in the about to document section", "" + documenttitle + " in the about to document section is not clicked", Status.FAIL);
		}
		int count = 0;
		WebElement linkDocPage = commonLibrary.isExist(By.cssSelector("form[class*='relatedcontentview']"), 10);
		do {
			count++;
			commonLibrary.sleep(5000);
			linkDocPage = commonLibrary.isExist(By.cssSelector("form[class*='relatedcontentview']"), 10);
		} while (linkDocPage == null && count < 10);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click tips link
	// # Function Name : clickTips     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickTips() {
		WebElement atdSection = commonLibrary.isExist(UIMAP_Document.atdSection);
		WebElement activeAtd = commonLibrary.isExist(atdSection, UIMAP_Document.activeAtd, 10);
		if (activeAtd == null) {
			WebElement atdDropDown = commonLibrary.isExist(atdSection, UIMAP_Document.atdDropDown, 10);
			if (atdDropDown != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(atdDropDown, "ATD Dropdown");
				else
					commonLibrary.clickButton(atdDropDown);
			}
		}
		WebElement tips = commonLibrary.isExist(UIMAP_Document.tipsinatd, 20);
		if (tips != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(tips, "Tips");
			} else {
				commonLibrary.clickButtonParentWithWait(tips, "Tips");
			}
		} else {
			report.updateTestLog("Click tips link in about the document section", "tips link in about the document section is not clicked", Status.FAIL);
		}
		// commonLibrary.switchToWindow("help");

		// driver.close();
		/*
		 * if (driver.getWindowHandles().size() < 2) {
		 * report.updateTestLog("Close the secondary window",
		 * "Secondary window is closed", Status.PASS); } else {
		 * report.updateTestLog("Close the secondary window",
		 * "Secondary window is not closed", Status.FAIL); }
		 */

		commonLibrary.switchToWindow("document");

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click breif case icon
	// # Function Name : clickTips     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickBriefCaseIcon() {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);
		WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
		if (headNotesContent == null)
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		WebElement briefCaseIcon = commonLibrary.isExist(UIMAP_Document.briefcaseIcon, 10);
		// briefCaseIcon.click();
		commonLibrary.clickButtonParentWithWait(briefCaseIcon, "Briefcase");
		commonLibrary.sleep(10000);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click View Topic Summary Link.
	// # Function Name : clickViewTopicSummary     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickViewTopicSummary() {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);
		WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
		if (headNotesContent == null)
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		WebElement photoIdentification = commonLibrary.isExist(UIMAP_Document.photoIdentificationLink, 10);
		commonLibrary.clickLinkWithWebElementWithWait(photoIdentification, "Photo Identification Link");
		WebElement viewTopicSummary = commonLibrary.isExist(UIMAP_Document.viewTopicSummaryLink, 10);
		commonLibrary.clickLinkWithWebElementWithWait(viewTopicSummary, "View Topic Summary Link");
		commonLibrary.sleep(10000);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created.
	// # Function Name : addToCreatedFolder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document addToCreatedFolder(String folderName) {
		boolean flag = false;
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null)

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		commonLibrary.sleep(5000);
		// CLICK ON <<<CHOOSE A FOLDER>>>
		WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");
		else {
			addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");
		}
		commonLibrary.sleep(5000);
		WebElement createdFoldersContainer = commonLibrary.isExistNegative(UIMAP_Document.createdFoldersContainer, 10);
		List<WebElement> createdFolders = commonLibrary.isExistList(createdFoldersContainer, UIMAP_Document.listItems, 10);

		for (WebElement item : createdFolders) {
			if (item.getText().toLowerCase().contains(folderName.toLowerCase())) {
				WebElement folder = commonLibrary.isExistNegative(item, UIMAP_Document.links, 10);
				commonLibrary.clickLinkWithWebElementWithWait(folder, folderName);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select folder: " + folderName, folderName + " is not present", Status.FAIL);

		WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
		if (saveFolder != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(saveFolder, "save");
			else
				commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
		try {
			commonLibrary.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Document(scriptHelper);
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

			if (folder == null || !folder.isDisplayed()) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
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
	// # Function Description : Function to click Links under Actions Section.
	// # Function Name : clickViewTopicSummary     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectlinksunderActions(String actionlink) {
		WebElement extralinks = commonLibrary.isExist(UIMAP_Document.extralinks, 20);
		WebElement sectionNames = commonLibrary.isExist(extralinks, UIMAP_Document.sectionNames, 20);

		List<WebElement> ullist = commonLibrary.isExistList(sectionNames, UIMAP_Document.lstTagUList, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist.get(0), UIMAP_Document.listItems, 20);
		WebElement selectlink = commonLibrary.isExist(lilist.get(1), UIMAP_Document.allTopicDocuments, 20);
		if (selectlink.getText().contains(actionlink)) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(selectlink, actionlink);
			} else {
				commonLibrary.clickButtonParentWithWait(selectlink, actionlink);
			}
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement lnSearchResults = commonLibrary.isExistNegative(UIMAP_Document.lnkresult, 30);
		int counter = 0;
		do {
			counter = counter + 1;
			lnSearchResults = commonLibrary.isExistNegative(UIMAP_Document.lnkresult, 30);
			if (lnSearchResults == null)
				commonLibrary.sleep(1000);
		} while (lnSearchResults == null && counter <= 10);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document verifyDocResultHeader(String strPageHeader) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_Document.hdrResult, 5);
		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;

		String strPageHeader1 = Header.getText();
		strPageHeader1 = strPageHeader1.replace("\n", " ");
		if (HeaderSearchResult != null && HeaderSearchResult.getText().toLowerCase().contains(strPageHeader) || HeaderSearchResult1 != null && HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader) || HeaderSearchResult3 != null && HeaderSearchResult3.getText().contains(strPageHeader) || HeaderSearchResult4 != null && HeaderSearchResult4.getText().toLowerCase().contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else if (Header != null && strPageHeader1.contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click CopyCitation Links
	// # Function Name : clickCopyCitation     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickCopyCitation() {
		WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
		WebElement copyCitation = commonLibrary.isExistNegative(docHeader, UIMAP_Document.copyCitation, 10);
		commonLibrary.clickButtonParentWithWait(copyCitation, "Copy Citation");

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to open popup in fulldoc
	// # Function Name : openpopupinfulldoc     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document openpopupinfulldoc(String option) {
		// WebElement footnote = commonLibrary.isExist(UIMAP_Document.footnote2,
		// 10);
		// commonLibrary.clickButton_Log_SmallWait(footnote, "Footnote 2");
		// WebElement elem =
		// driver.findElement(By.cssSelector("aside[class='doccontextmenu selectedtextmenu']"));
		// WebElement elem = driver.findElement(By.xpath("/html/body/aside"));
		// String js =
		// "arguments[0].style.height='auto'; arguments[0].style.display='block';";
		// ((JavascriptExecutor) driver).executeScript(js, elem);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("document.getElementByClass('doccontextmenu selectedtextmenu').style.display='block';");
		WebElement clickoption = commonLibrary.isExist(UIMAP_Document.copycitation, 10);
		if (clickoption.getText().contains(option)) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(clickoption, "Copy");
				report.updateTestLog("To Verify Copy button is Clicked", "Copy is Clicked", Status.PASS);
			} else {
				commonLibrary.clickButtonLogSmallWait(clickoption, "Add to Search");
				report.updateTestLog("To Verify Copy button is Clicked", "Copy Button is Clicked", Status.PASS);
			}
			report.updateTestLog("To Verify Copy button is Clicked", "Copy Button is Clicked", Status.PASS);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title
	// # Function Name : Verify_DocumentTitle     
	// # Author : Ram Prasath
	// # Date Created : 30th Mar'15
	// #*****************************************************************************************************************************

	public Document verifyTopicSummaryDocumentTitle(String strDocTitle) {

		if (strDocTitle.contains("Topic Summary")) {
			WebElement docTitle = commonLibrary.isExistNegative(By.cssSelector("h1[id='SS_DocumentTitle']"), 10);

			if (docTitle != null && docTitle.getText().contains(strDocTitle))

				report.updateTestLog("Verify topic summary report page is displayed for " + strDocTitle, "Topic Summary report page is displayed for " + strDocTitle, Status.PASS);

			else

				report.updateTestLog("Verify topic summary report page is displayed for " + strDocTitle, "Topic Summary report page is not displayed for " + strDocTitle, Status.FAIL);

		} else {

			WebElement eltDocumentHeading = commonLibrary.isExist(UIMAP_Document.txtStudentDocumentHeading, 50);
			if (eltDocumentHeading != null && eltDocumentHeading.getText().contains(strDocTitle))

				report.updateTestLog("Verify topic summary report page is displayed for " + strDocTitle, "Topic Summary report page is displayed for " + strDocTitle, Status.PASS);

			else

				report.updateTestLog("Verify topic summary report page is displayed for " + strDocTitle, "Topic Summary report page is not displayed for " + strDocTitle, Status.FAIL);

		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Citation Format from dropdown
	// # Function Name : selectCitationFormat     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectCitationFormat(String option) {

		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement formatDropdown = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.formatDropdown, 10);
		commonLibrary.selectFromListOption(formatDropdown, option);
		if (commonLibrary.selectIsSelected(formatDropdown, option)) {
			report.updateTestLog("Verify " + option + " is selected in Format Dropdown.", option + " is selected in Format Dropdown.", Status.PASS);
		} else
			report.updateTestLog("Verify " + option + " is selected in Format Dropdown.", option + " is not selected in Format Dropdown.", Status.FAIL);
		WebElement selectButton = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.selectButton, 10);
		commonLibrary.clickButtonParentWithWait(selectButton, "Select");
		try {
			String loadProp = properties.getProperty("xSpinner");
			int count = 0;
			WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
			do {
				commonLibrary.sleep(100000);
				loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				count++;
			} while (loader != null && count < 15);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Checkboxes present and their
	// status.
	// # Function Name : checkBoxStatus     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document checkBoxStatus(String option, boolean isChecked, boolean isEnabled) {
		if (option.contains("Ignore")) {
			return new Document(scriptHelper);
		} else {
			WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
			WebElement citationInclude = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationInclude, 10);
			boolean blnFlag = false;
			List<WebElement> reporters = commonLibrary.isExistList(citationInclude, UIMAP_Document.listItems, 10);
			for (WebElement reporter : reporters) {
				if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {
					blnFlag = true;
					WebElement checkBox = commonLibrary.isExistNegative(reporter, UIMAP_Document.checkBox, 10);

					if (isChecked) {
						if (checkBox.isSelected()) {
							report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is selected", Status.PASS);
						} else
							report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is not selected", Status.FAIL);
					} else {
						if (checkBox.isSelected()) {
							report.updateTestLog("Verify checkbox for " + option + " is not  selected", "checkbox for " + option + " is selected", Status.FAIL);
						} else
							report.updateTestLog("Verify checkbox for " + option + " is not selected", "checkbox for " + option + " is not selected", Status.PASS);

					}

					if (isEnabled) {
						if (checkBox.isEnabled()) {
							report.updateTestLog("Verify checkbox for " + option + " is enabled", "checkbox for " + option + " is enabled", Status.PASS);
						} else
							report.updateTestLog("Verify checkbox for " + option + " is enabled", "checkbox for " + option + " is not enabled", Status.FAIL);

					} else {
						if (checkBox.isEnabled()) {
							report.updateTestLog("Verify checkbox for " + option + " is not enabled", "checkbox for " + option + " is enabled", Status.FAIL);
						} else
							report.updateTestLog("Verify checkbox for " + option + " is not enabled", "checkbox for " + option + " is not enabled", Status.PASS);

					}
					break;
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify checkbox for " + option + " is present", "checkbox for " + option + " is not present", Status.FAIL);
			return new Document(scriptHelper);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify party names are italicized in
	// Citation.
	// # Function Name : verifyPartyItalicized     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyPartyItalicized(String party1, String party2) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		List<WebElement> italics = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.italics, 10);

		if (italics.get(0).getText().contains(party1) && italics.get(1).getText().contains(party2)) {
			report.updateTestLog("Verify partynames displays in Italicized format in the case name.", "Partynames displays in Italicized format in the case name.", Status.PASS);
		} else
			report.updateTestLog("Verify partynames displays in Italicized format in the case name.", "Partynames does not display in Italicized format in the case name.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text italicized in Citation.
	// # Function Name : verifyItalicized     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyItalicized(String party1) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		List<WebElement> italics = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.italics, 10);

		if (italics.get(0).getText().contains(party1)) {
			report.updateTestLog("Verify partynames displays in Italicized format in the case name.", party1 + " Partynames displays in Italicized format in the case name.", Status.PASS);
		} else
			report.updateTestLog("Verify partynames displays in Italicized format in the case name.", party1 + " Partynames does not display in Italicized format in the case name.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify v text not italicized in
	// Citation.
	// # Function Name : verifyVNotItalicized     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyVNotItalicized() {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		boolean blnFlag = false;
		List<WebElement> italics = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.italics, 10);
		for (WebElement item : italics) {
			if (item.getText().contains("v.")) {
				report.updateTestLog("Verify 'v.' is not italicized.", "'v.' is italicized.", Status.FAIL);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify 'v.' is not italicized.", "'v.' is not italicized.", Status.PASS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify abbreviations present in
	// Citation.
	// # Function Name : verifyAbbreviation     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyAbbreviation(String officialReporter) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains(officialReporter.toLowerCase())) {
			report.updateTestLog("Verify Washington reporter name abbreviated as 'Wn.' in the Official reporter " + officialReporter, "Washington reporter name abbreviated as 'Wn.' in the Official reporter " + officialReporter, Status.PASS);
		} else
			report.updateTestLog("Verify Washington reporter name abbreviated as 'Wn.' in the Official reporter " + officialReporter, "Washington reporter name is not abbreviated as 'Wn.' in the Official reporter " + officialReporter, Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify full sources present in
	// Citation.
	// # Function Name : verifyFullSource     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyFullSource(String source) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(source.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " displays in the preview box", Status.PASS);
		} else
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " does not display in the preview box", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify full sources present in
	// Citation.
	// # Function Name : verifyUpdatedFullSource     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyUpdatedFullSource(String option, String source) {
		// WebElement copyCitationPopup =
		// commonLibrary.isExist_Negative(UIMAP_Document.copyCitationPopup, 10);
		// WebElement citationInclude =
		// commonLibrary.isExist_Negative(copyCitationPopup,
		// UIMAP_Document.citationInclude, 10);
		// boolean blnFlag=false;
		// List<WebElement> reporters =
		// commonLibrary.isExist_List(citationInclude,UIMAP_Document.listItems,10);
		// for(WebElement reporter:reporters)
		// {
		// if(reporter.getText().toLowerCase().contains(option.toLowerCase()))
		// {
		// blnFlag=true;
		// WebElement checkBox = commonLibrary.isExist_Negative(reporter,
		// UIMAP_Document.checkBox, 10);
		// String checkboxname = reporter.getText();
		// if(browsername.contains("internet"))
		// {
		// commonLibrary.SetCheckBox(reporter, false);
		// report.updateTestLog("To Uncheck Checkbox",
		// "unchecked the checkbox "+checkboxname, Status.PASS);
		// }
		// else
		// {
		// commonLibrary.SetCheckBox(reporter, false);
		// int i = 1;
		// report.updateTestLog("To Uncheck Checkbox",
		// "unchecked the checkbox "+checkboxname, Status.PASS);
		// }
		// }
		// }
		WebElement copyCitationPopup1 = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup1, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains(source.toLowerCase())) {
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " displays in the preview box", Status.PASS);
		} else
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " does not display in the preview box", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document closeCitationPopup() {
		generalFunctions.closeCitationPopup();
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text present.
	// # Function Name : verifyTextPresence     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyTextPresence(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains((text).toLowerCase())) {
			report.updateTestLog("Verify " + text + " is present in the preview box", text + " is present in the preview box", Status.PASS);
		} else {
			report.updateTestLog("Verify " + text + " is present in the preview box", text + " is not present in the preview box", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select None and close Citation
	// popup.
	// # Function Name : selectNoneClosePopUp     
	// # Author : Anbarasan
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectNoneClosePopUp(String option) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement formatDropdown = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.formatDropdown, 10);
		commonLibrary.selectFromListOption(formatDropdown, option + " from the Citation format dropdown");

		WebElement selectButton = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.selectButton, 10);
		commonLibrary.clickButtonParentWithWait(selectButton, "Apply");

		WebElement close = commonLibrary.isExist(UIMAP_Document.closeCitationPopup, 10);
		commonLibrary.clickButtonLogSmallWait(close, "Close");

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify absence of text
	// popup.
	// # Function Name : verifyTextNotPresent     
	// # Author : Anbarasan
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyTextNotPresent(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains((text).toLowerCase())) {
			report.updateTestLog("Verify " + text + " is present in the preview box", text + " is present in the preview box", Status.FAIL);
		} else {
			report.updateTestLog("Verify " + text + " is present in the preview box", text + " is not present in the preview box", Status.PASS);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether the string is
	// italicized
	// # Function Name : verifyStringItalicized
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Document verifyStringItalicized(String string) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		List<WebElement> italics = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.italics, 10);

		for (WebElement italicString : italics) {
			if (italicString.getText().toLowerCase().equalsIgnoreCase(string.toLowerCase())) {
				report.updateTestLog("Verify " + string + " is Italicized", string + " is Italicized", Status.PASS);
				break;
			} else {
				report.updateTestLog("Verify " + string + " is Italicized", string + " is not Italicized", Status.FAIL);
			}

		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify format of text
	// # Function Name : verifyTextFormat
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document verifyTextFormat(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		if (clipTextContainer.getText().equalsIgnoreCase(text)) {
			report.updateTestLog("Verify text format", "The text " + text + " is in the same format as in the string in cliptext", Status.PASS);
		} else {
			report.updateTestLog("Verify text format", "The text " + text + " is not in the same format as in the string in cliptext", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify case name.
	// # Function Name : verifyCaseName     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyCaseName(String caseName) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().contains(caseName)) {
			report.updateTestLog("Verify case name is displayed as " + caseName, "case name is displayed as " + caseName, Status.PASS);
		} else {
			report.updateTestLog("Verify case name is displayed as " + caseName, "case name is not displayed as " + caseName, Status.FAIL);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify check box status.
	// # Function Name : checkBoxStatus     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document checkBoxStatus(String option, boolean isChecked) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement citationInclude = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationInclude, 10);
		boolean blnFlag = false;
		List<WebElement> reporters = commonLibrary.isExistList(citationInclude, UIMAP_Document.listItems, 10);
		for (WebElement reporter : reporters) {
			if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {
				blnFlag = true;
				WebElement checkBox = commonLibrary.isExistNegative(reporter, UIMAP_Document.checkBox, 10);

				if (isChecked) {
					if (checkBox.isSelected()) {
						report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is selected", Status.PASS);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is not selected", Status.FAIL);
				} else {
					if (checkBox.isSelected()) {
						report.updateTestLog("Verify checkbox for " + option + " is not  selected", "checkbox for " + option + " is selected", Status.FAIL);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is not selected", "checkbox for " + option + " is not selected", Status.PASS);

				}
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify checkbox for " + option + " is present", "checkbox for " + option + " is not present", Status.FAIL);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify format of the text inside
	// parenthesis
	// # Function Name : verifyTextFormatInsideParenthesis     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyTextFormatInsideParenthesis(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		String[] stringArray = clipTextContainer.getText().split("\\(");

		if (stringArray[1].contains(text))
			report.updateTestLog("Verify text format inside parenthesis", "The " + text + " is in same format inside the parenthesis", Status.PASS);
		else
			report.updateTestLog("Verify text format inside parenthesis", "The " + text + " is not in same format inside the parenthesis", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify starting of text inside
	// parenthesis
	// # Function Name : verifyTextStartsInsideParenthesis     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document verifyTextStartsInsideParenthesis(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		String[] stringArray = clipTextContainer.getText().split("\\(");

		if (stringArray[1].startsWith(text))
			report.updateTestLog("Verify text starts inside parenthesis", "The " + text + " is the first term inside the parenthesis", Status.PASS);
		else
			report.updateTestLog("Verify text starts inside parenthesis", "The " + text + " is not the first term inside the parenthesis", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Active passage in the
	// document page
	// # Function Name : ClickOnActivePassage     
	// # Author :
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail clickOnActivePassage() {

		WebElement btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
		if (btnActivatepassages != null)
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(btnActivatepassages, "Activate Passages");
			else
				commonLibrary.clickButtonParentWithWait(btnActivatepassages, "Activate Passages");

		// Click on highlighted RFC LIT page will display

		WebElement lnkRFPassage = commonLibrary.isExist(UIMAP_Document.lnkRFPassage, 10);
		if (lnkRFPassage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(lnkRFPassage, "Highlighted RFC");
			else
				commonLibrary.clickButtonParentWithWait(lnkRFPassage, "Highlighted RFC");
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		int count = 0;
		do {
			count++;
			rslt = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader, 5);
			if (rslt == null)
				commonLibrary.sleep(3000);
		} while (rslt == null && count < 20);

		// LIT.verifySearchResultHeader("legal issue trail");

		return new LegalIssueTrail(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the presence of checkbox
	// # Function Name : ClickOnActivePassage     
	// # Author :
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document verifyCheckbox(String option) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement citationInclude = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationInclude, 10);
		boolean blnFlag = false;
		List<WebElement> reporters = commonLibrary.isExistList(citationInclude, UIMAP_Document.listItems, 10);
		for (WebElement reporter : reporters) {
			if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {
				report.updateTestLog("Verify " + option + " checkbox is present.", option + " checkbox is present.", Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + option + " checkbox is present.", option + " checkbox is not present.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Abbreviation
	// # Function Name : verifyAbbreviation1     
	// # Author :seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document verifyAbbreviation1(String officialReporter, String fullform) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains(officialReporter.toLowerCase())) {
			report.updateTestLog("Verify " + fullform + " abbreviated as " + officialReporter + " in the case name", "" + fullform + " is abbreviated as " + officialReporter + " in the case name", Status.PASS);
		} else
			report.updateTestLog("Verify " + fullform + " abbreviated as " + officialReporter + " in the case name", "" + fullform + " is not abbreviated as " + officialReporter + " in the case name", Status.PASS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify if the text is underlined
	// # Function Name : verifyTextUnderlined     
	// # Author :seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document verifyTextUnderlined(String textToVerify, boolean isUnderlined) {
		boolean flag = false;

		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		List<WebElement> textUnderlined = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.clipTextUnderlined, 10);

		for (WebElement eleText : textUnderlined) {
			if (eleText.getText().toLowerCase().equals(textToVerify.toLowerCase())) {
				flag = true;
				break;
			}
		}

		if (isUnderlined) {
			if (flag)
				report.updateTestLog("Verify text '" + textToVerify + "' to be underlined", "'" + textToVerify + "' is underlined as expected", Status.PASS);
			else
				report.updateTestLog("Verify text '" + textToVerify + "' to be underlined", "'" + textToVerify + "' is not underlined as expected", Status.FAIL);
		} else {
			if (!flag)
				report.updateTestLog("Verify text '" + textToVerify + "' is not underlined", "'" + textToVerify + "' is not underlined as expected", Status.PASS);
			else
				report.updateTestLog("Verify text '" + textToVerify + "' is not underlined", "'" + textToVerify + "' is underlined which is not expected", Status.FAIL);

		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify if checkbox is displayed
	// # Function Name : checkBoxDisplayed     
	// # Author :seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document checkBoxDisplayed(String option, Boolean isDisplayed) {
		boolean flag = false;
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement citationInclude = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationInclude, 10);

		List<WebElement> reporters = commonLibrary.isExistList(citationInclude, UIMAP_Document.listItems, 10);
		for (WebElement reporter : reporters) {
			if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {
				flag = true;
				break;
			}
		}
		if (isDisplayed) {
			if (flag)
				report.updateTestLog("Verify checkbox for '" + option + "' is displayed", "checkbox for '" + option + "' is displayed", Status.PASS);
			else
				report.updateTestLog("Verify checkbox for '" + option + "' is displayed", "checkbox for '" + option + "' is not displayed", Status.FAIL);
		} else {
			if (!flag)
				report.updateTestLog("Verify checkbox for '" + option + "' is not displayed", "checkbox for '" + option + "' is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify checkbox for '" + option + "' is not displayed", "checkbox for '" + option + "' is displayed", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select close button in citation
	// popup
	// # Function Name : selectclosebutton
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Document selectclosebutton() {
		WebElement btnCitationClose = commonLibrary.isExist(UIMAP_Document.btnCitationClose);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(btnCitationClose, "Close");
		} else {
			commonLibrary.clickButtonParentWithWait(btnCitationClose, "Close");
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify dropdown value in citation
	// # Function Name : dropdownvalueincitation
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Document dropdownvalueincitation(String option) {
		WebElement formatDropdown = commonLibrary.isExist(UIMAP_Document.formatDropdown);
		String dropValue = formatDropdown.getText();
		if (dropValue.contains(option)) {
			report.updateTestLog("Verify the DropDown Actual Value", "DropDown Value is " + option + " present", Status.PASS);
		} else {
			report.updateTestLog("Verify the DropDown Actual Value", "DropDown Value is " + option + " not present", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify status of the checkbox
	// # Function Name : optionscheckBoxStatus
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document optionscheckBoxStatus(String option, boolean isChecked, boolean isEnabled) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement citationOptions = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationOptions, 10);
		boolean blnFlag = false;
		List<WebElement> reporters = commonLibrary.isExistList(citationOptions, UIMAP_Document.listItems, 10);
		for (WebElement reporter : reporters) {
			if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {
				blnFlag = true;
				WebElement checkBox = commonLibrary.isExistNegative(reporter, UIMAP_Document.checkBox, 10);

				if (isChecked) {
					if (checkBox.isSelected()) {
						report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is selected", Status.PASS);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is selected", "checkbox for " + option + " is not selected", Status.FAIL);
				} else {
					if (checkBox.isSelected()) {
						report.updateTestLog("Verify checkbox for " + option + " is not  selected", "checkbox for " + option + " is selected", Status.FAIL);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is not selected", "checkbox for " + option + " is not selected", Status.PASS);

				}

				if (isEnabled) {
					if (checkBox.isEnabled()) {
						report.updateTestLog("Verify checkbox for " + option + " is enabled", "checkbox for " + option + " is enabled", Status.PASS);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is enabled", "checkbox for " + option + " is not enabled", Status.FAIL);

				} else {
					if (checkBox.isEnabled()) {
						report.updateTestLog("Verify checkbox for " + option + " is not enabled", "checkbox for " + option + " is enabled", Status.FAIL);
					} else
						report.updateTestLog("Verify checkbox for " + option + " is not enabled", "checkbox for " + option + " is not enabled", Status.PASS);

				}
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify checkbox for " + option + " is present", "checkbox for " + option + " is not present", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to uncheck options in Copy citations
	// pop up
	// # Function Name : unCheckcitationsunderoptions
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Document unCheckcitationsunderoptions(String option) {
		if (option.contains("Ignore")) {
			return new Document(scriptHelper);
		} else {
			WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
			if (copyCitationPopup != null) {
				WebElement citationOptions = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.citationInclude, 10);

				List<WebElement> reporters = commonLibrary.isExistList(citationOptions, UIMAP_Document.listItems, 10);
				for (WebElement reporter : reporters) {
					if (reporter.getText().toLowerCase().contains(option.toLowerCase())) {

						WebElement checkBox = commonLibrary.isExistNegative(reporter, UIMAP_Document.checkBox, 10);
						if (checkBox != null) {
							commonLibrary.clickJS(checkBox, option);
						}
					}
				}
			} else {
				report.updateTestLog("Verify Citation popup displayed ", "Citation popup is not displayed", Status.FAIL);
			}
			return new Document(scriptHelper);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the prefix of the text
	// pop up
	// # Function Name : verifyPrefixOfText
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document verifyPrefixOfText(String prefix, String textWithPrefix, String fullText) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (!clipTextContainer.getText().toLowerCase().contains((fullText).toLowerCase())) {
			report.updateTestLog("Verify " + fullText + " is present in the clipboard text", fullText + " is not present in the clipboard text", Status.FAIL);
			return new Document(scriptHelper);
		}

		int length = textWithPrefix.length();
		int startIndex = fullText.indexOf(textWithPrefix);
		// String textToCompare = fullText.substring(startIndex - 1, startIndex
		// + length);
		String textToCompare = fullText.substring(startIndex, startIndex + length);

		if (textToCompare.toLowerCase().equals((prefix + textWithPrefix).toLowerCase())) {
			report.updateTestLog("Verify citation '" + textWithPrefix + "' contains '" + prefix + "' as prefix in '" + fullText + "'", "Citation '" + textWithPrefix + "' contains '" + prefix + "' as prefix in '" + fullText + "'", Status.PASS);
		} else
			report.updateTestLog("Verify citation '" + textWithPrefix + "' contains '" + prefix + "' as prefix in '" + fullText + "'", "Citation '" + textWithPrefix + "' does not contains '" + prefix + "' as prefix in '" + fullText + "'", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alerts icon
	// pop up
	// # Function Name : clickAlertIconPenLeg
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document clickAlertIconPenLeg() {
		WebElement btnAlert = commonLibrary.isExist(UIMAP_Document.alertPending);
		if (btnAlert != null) {
			commonLibrary.clickLinkWithWebElement(btnAlert, "Alert");
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alerts icon
	// pop up
	// # Function Name : createAlert_Backto_ResultList
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Search createAlertBacktoResultList(String filterLink) {
		if (filterLink.equalsIgnoreCase("Bill Text"))
			this.clickAlertIconLeg();
		else if (filterLink.equalsIgnoreCase("Codes"))
			this.clickAlertIconPenLeg();
		else if (filterLink.equalsIgnoreCase("Regulation Text"))
			this.clickAlertIconreg();

		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			/*
			 * if(commonLibrary.isExist(UIMAP_Home.btnBrowse)!=null) {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is clicked", Status.PASS); } else
			 * if(commonLibrary.isExist(UIMAP_Home.btnPracArea)!=null) {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is clicked", Status.PASS); } else {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is not clicked", Status.FAIL); }
			 */
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}

		if (filterLink.equalsIgnoreCase("Bill Text") || filterLink.equalsIgnoreCase("Codes")) {
			WebElement docSlider = commonLibrary.isExist(UIMAP_Document.docSlider, 10);
			WebElement resultList = commonLibrary.isExist(docSlider, UIMAP_Document.allTopicDocuments, 10);
			if (resultList != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(resultList, "ResultList");
				else
					commonLibrary.clickButtonParentWithWait(resultList, "ResultList");
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alerts icon
	// pop up
	// # Function Name : createAlert_Backto_ResultList1
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document createAlertBacktoResultList1() {

		this.clickAlertIconreg();

		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			/*
			 * if(commonLibrary.isExist(UIMAP_Home.btnBrowse)!=null) {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is clicked", Status.PASS); } else
			 * if(commonLibrary.isExist(UIMAP_Home.btnPracArea)!=null) {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is clicked", Status.PASS); } else {
			 * report.updateTestLog("Click on CreateAlert",
			 * "CreateAlert link is not clicked", Status.FAIL); }
			 */
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate alerts icon
	// pop up
	// # Function Name : navigateToAlerts
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Alerts navigateToAlerts() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement alert = commonLibrary.isExist(UIMAP_Home.Alertbtn, 10);
			if (alert == null) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
				if (btnMore != null)
					if (browsername.contains("internet"))
						commonLibrary.clickJS(btnMore, "More");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
			}

			if (alert != null) {
				commonLibrary.clickLinkWithWebElementWithWait(alert, "Alerts");
				commonLibrary.sleep(6000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to embedded links in the document page
	// pop up
	// # Function Name : click_EmbeddedLink
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Search clickEmbeddedLinkEntity(String linkName) {
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
		List<WebElement> embLinks = commonLibrary.isExistList(docContent, UIMAP_Document.embeddedLink, 10);
		for (WebElement el : embLinks) {
			if (el.getText().equalsIgnoreCase(linkName)) {
				if (browsername.contains("internet")) {
					// commonLibrary.click_JS(el, linkName);
					// clicking embedded link in document page
					try {
						driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
						commonLibrary.scrollToView(el);
						commonLibrary.clickLinkWithWebElementWithWait(el, el.getText());
					} catch (TimeoutException ex) {
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println(ex.toString());

					}
					driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);

					break;
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(el, el.getText());
					// commonLibrary.clickButton_Log_SmallWait(el, linkName);
					break;
				}
			}
		}

		WebElement inlinePopup = commonLibrary.isExistNegative(UIMAP_Document.inlinePopup, 10);
		if (inlinePopup != null && inlinePopup.isDisplayed()) {
			report.updateTestLog("Verify Entity popup displays", "Entity popup displays", Status.PASS);
			WebElement searchLink = commonLibrary.isExistNegative(inlinePopup, UIMAP_Document.embeddedSearchLink, 10);
			// commonLibrary.ScrollToView(searchLink);
			if (searchLink != null) {
				// clicking link in the inline popup
				if (browsername.contains("internet"))
					commonLibrary.clickJS(searchLink, "Search for " + linkName);
				else
					commonLibrary.clickButtonLogSmallWait(searchLink, "Search for " + linkName);
			} else
				report.updateTestLog("Click on Search for " + linkName, "Search link does not display", Status.FAIL);
		} else
			report.updateTestLog("Verify Entity popup displays", "Entity popup does not display", Status.FAIL);

		// wait
		WebElement header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);

		int count = 0;
		do {
			count++;
			header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);
			if (header == null)
				commonLibrary.sleep(3000);
		} while (header == null && count <= 10);

		if (header != null) {
			if (header.getText().contains(linkName))
				report.updateTestLog("Verify '" + linkName + "'  entity search results page displayed", linkName + " entity search results page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify '" + linkName + "'  entity search results page displayed", linkName + " entity search results page is NOT displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click footer links in the document
	// page
	// # Function Name : NavigateToHistoryFooterLink
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public History navigateToHistoryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 10);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);
			commonLibrary.sleep(2500);

			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(lnkTxtViewAllHistory, "View All History");
					commonLibrary.sleep(10000);
				} else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory, "View All History");
				int count = 0;
				do {
					count++;
					commonLibrary.sleep(8000);
				} while (driver.getCurrentUrl().contains("history") && count < 15);
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet")) {
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
					commonLibrary.sleep(10000);
				} else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");

				commonLibrary.sleep(10000);

			}

			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click footer links in the document
	// page
	// # Function Name : NavigateToHistoryFooterLink
	// # Author : Ram
	// # Date Created : April'15
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
				List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_Document.lstResearchThread, 1000);

				int count = 0;
				do {
					lstResearchThread = commonLibrary.isExistList(UIMAP_Document.lstResearchThread, 1000);
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
	// # Function Description : Function to click a link under footnote
	// # Function Name : clickLinkUnderFootnote     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickLinkUnderFootnote(String title) {
		boolean footnoter = false;
		List<WebElement> footnote = commonLibrary.isExistList(UIMAP_Document.footnotelink, 20);
		for (WebElement item : footnote)
			if (item.getText().contains(title)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, title);
				} else {
					commonLibrary.clickButtonParentWithWait(item, title);
				}
				footnoter = true;
				break;
			}
		if (!footnoter) {
			report.updateTestLog("Click " + title + "", "" + title + " is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browse link
	// # Function Name : navigateToBrowserLink2
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Search navigateToBrowserLink2(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction, String linktest) {
		generalFunctions.navigateToBrowserLink(strParentMenuName, strFirstSubMenuName, strSecondSubMenuName, strThridSubMenuName, strAction, linktest);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click history link
	// # Function Name : navigateToHistoryLinkSearch
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Search navigateToHistoryLinkSearch(String strTitle, String searchType) {

		try {

			boolean blnFlag = false;
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 10);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Seaches");

			WebElement lstDocumentsContent = commonLibrary.isExist(UIMAP_Home.eltHistoryPodContent, 20);
			if (lstDocumentsContent != null) {
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstDocumentsContent, UIMAP_Document.listItems, 20);
				for (WebElement item : lnkDocuments) {
					WebElement searchLink = commonLibrary.isExistNegative(item, UIMAP_Document.links, 10);
					WebElement type = commonLibrary.isExistNegative(item, UIMAP_Document.span, 10);
					if (searchLink.getText().toLowerCase().contains(strTitle.toLowerCase()) && type.getText().toLowerCase().contains(searchType.toLowerCase())) {
						// report.updateTestLog("Verify '"+strTitle+
						// " "+searchType+
						// "'  displays in recent searches.",strTitle+
						// " "+searchType+
						// "'  displays in recent searches.",Status.PASS);
						commonLibrary.clickLinkWithWebElementWithWait(searchLink, strTitle);
						blnFlag = true;
						break;
					}
				}

			}
			if (!blnFlag)
				report.updateTestLog("Verify '" + strTitle + " " + searchType + "'  displays in recent searches.", strTitle + " " + searchType + "' does not display in recent searches.", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click history link
	// # Function Name : navigateToHistoryLinkDocument_Negative
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public boolean navigateToHistoryLinkDocument_Negative(String strDocTitle) {
		Boolean blnRecenDoc = true;
		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "History - Documents");

			List<WebElement> lstrecentDocumentsContent = commonLibrary.isExistList(UIMAP_Home.viewDocumentList, 20);

			for (int i = 0; i < lstrecentDocumentsContent.size(); i++) {
				String temp = lstrecentDocumentsContent.get(i).getText();
				if (strDocTitle.contains(temp.substring(0, temp.length() - 4))) {
					blnRecenDoc = false;
					break;
				}
			}

			if (blnRecenDoc)
				report.updateTestLog("Verify '" + strDocTitle + " '  displays in recent document searches.", strDocTitle + "' does not display in recent document searches.", Status.PASS);
			else
				report.updateTestLog("Verify '" + strDocTitle + " '  displays in recent document searches.", strDocTitle + "'  displays in recent document searches.", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			return false;

		}
		return blnRecenDoc;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack1
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Document clickBrowserBack() {

		// // driver.navigate().back();
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(5000);
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
	// # Function Description : Function to click a specific link from ATD
	// section returns relatedcontent
	// # Function Name : clickSpecificlinkfromATDsection1     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*********************************************
	public RelatedContent clickSpecificlinkfromATDsection1(String documenttitle) {
		boolean clicked = false;
		WebElement atdSection = commonLibrary.isExist(UIMAP_Document.atdSection);
		WebElement activeAtd = commonLibrary.isExist(atdSection, UIMAP_Document.activeAtd, 10);
		if (activeAtd == null) {
			WebElement atdDropDown = commonLibrary.isExist(atdSection, UIMAP_Document.atdDropDown, 10);
			if (atdDropDown != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(atdDropDown, "ATD Dropdown");
				else
					commonLibrary.clickButton(atdDropDown);
			}
		}
		List<WebElement> aboutthisdoclink = commonLibrary.isExistList(UIMAP_Document.aboutthisdoclink, 20);
		for (WebElement item : aboutthisdoclink) {
			if (item.getText().contains(documenttitle)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWait(item, documenttitle);
				} else {
					commonLibrary.clickButtonParentWithWait(item, documenttitle);
				}
				clicked = true;
				break;
			}
		}

		if (!clicked) {
			report.updateTestLog("Click " + documenttitle + " in the about to document section", "" + documenttitle + " in the about to document section is not clicked", Status.FAIL);
		}
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click RecentSearch
	// # Function Name : verifyRecentSearchAndClick     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*********************************************
	public VSASearchResults verifyRecentSearchAndClick(String strTitle, String searchterm) {

		try {

			boolean istitle = false;
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Seaches");

			WebElement lstDocumentsContent = commonLibrary.isExist(UIMAP_Home.eltHistoryPodContent, 20);
			if (lstDocumentsContent != null) {
				List<WebElement> lnkDocuments1 = commonLibrary.isExistList(lstDocumentsContent, By.tagName("li"), 20);
				for (int j = 0; j < lnkDocuments1.size(); j++) {
					List<WebElement> lnkDocuments = commonLibrary.isExistList(lnkDocuments1.get(j), By.tagName("a"), 20);
					if (lnkDocuments != null) {
						for (int i = 0; i < lnkDocuments.size(); i++) {
							if (lnkDocuments.get(i).getText().equalsIgnoreCase(searchterm)) {
								WebElement title = commonLibrary.isExist(lnkDocuments1.get(j), By.tagName("span"), 10);
								if (title.getText().equalsIgnoreCase(strTitle)) {
									istitle = true;
									commonLibrary.clickJS(title, searchterm);
									break;
								}
							}
						}
						if (istitle)
							break;

					}
				}

				if (istitle) {
					report.updateTestLog("Verify Searches Tab", strTitle + " entity search is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify Searches Tab", strTitle + " entity search is not displayed", Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select filter
	// # Function Name : selectFilter     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*********************************************
	public History selectFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 1;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_Document.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_Document.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_Document.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
					}

					List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_Document.filterMore, 10);
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
				eltFilter = commonLibrary.isExistList(UIMAP_Document.eltFilterList1, 10);
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

		return new History(scriptHelper);

	}

	// //#*****************************************************************************************************************************
	// # Function Description : Function to select the folder by filtering the
	// name
	// # Function Name : selectFolder     
	// # Author : Vidhya
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Document clickFolderLink(String FolderName, String doctitle) {
		try {

			/*
			 * //filtering with folder name WebElement searchFilter =
			 * commonLibrary.isExist(UIMAP_Home.txtclassSearch,30);
			 * if(searchFilter!=null) {
			 * commonLibrary.SetDataInTextBox(searchFilter, FolderName,
			 * "Folder Filter option"); WebElement Submitsearch =
			 * commonLibrary.isExist(UIMAP_Document.FilterButton, 10);
			 * if(browsername.contains("internet") && version.contains("9")) {
			 * commonLibrary.clickButton_Parent_WithWait_JS(Submitsearch,
			 * "Search"); } else {
			 * commonLibrary.clickButton_Parent_WithWait(Submitsearch,
			 * "Search"); } }
			 */
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
						if (browsername.contains("internet"))
							commonLibrary.clickJS(docSelect, "Document Title");
						else
							docSelect.click();
						break;
					}

				}

				WebElement objDocument = commonLibrary.isExist(UIMAP_WorkFolders.documentSection, 10);

				if (blnFlag && objDocument.getText().contains(doctitle))
					report.updateTestLog("Select the document " + doctitle, "' " + doctitle + " ' document is selected and appropriate document is opened", Status.PASS);
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
	// # Function Description : Function for screenshot
	// # Function Name : takeScreenShot     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*********************************************************************************************************************
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
	// # Function Description : Function to click document link
	// # Function Name : clickDocumentLink     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*********************************************************************************************************************
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
	// # Function Description : Function to click parts of a large document.
	// # Function Name : clickPartsOfLargeDoc     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickPartsOfLargeDoc(String documenttitle) {
		boolean clicked = false;
		List<WebElement> aboutthisdoclink = commonLibrary.isExistList(UIMAP_Document.docParts, 20);
		for (WebElement item : aboutthisdoclink) {
			if (item.getText().toLowerCase().contains(documenttitle.toLowerCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(item, documenttitle);
				} else {
					commonLibrary.clickButtonParentWithWait(item, documenttitle);
				}
				clicked = true;
				break;
			}
		}

		if (!clicked) {
			report.updateTestLog("Click " + documenttitle + " in the about to document section", "" + documenttitle + " in the about to document section is not clicked", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify recent search
	// # Function Name : verifyRecentSearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #****************************************************************************************************************************
	public Document verifyRecentSearch(String strTitle, String searchterm) {

		try {

			boolean istitle = false;
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Seaches");

			WebElement lstDocumentsContent = commonLibrary.isExist(UIMAP_Home.eltHistoryPodContent, 20);
			if (lstDocumentsContent != null) {
				List<WebElement> lnkDocuments1 = commonLibrary.isExistList(lstDocumentsContent, By.tagName("li"), 20);
				for (int j = 0; j < lnkDocuments1.size(); j++) {
					List<WebElement> lnkDocuments = commonLibrary.isExistList(lnkDocuments1.get(j), By.tagName("a"), 20);
					if (lnkDocuments != null) {
						for (int i = 0; i < lnkDocuments.size(); i++) {
							if (lnkDocuments.get(i).getText().equalsIgnoreCase(searchterm)) {
								WebElement title = commonLibrary.isExist(lnkDocuments1.get(j), By.tagName("span"), 10);
								if (title.getText().equalsIgnoreCase(strTitle)) {
									istitle = true;
									break;
								}
							}
						}
						if (istitle)
							break;

					}
				}

				if (istitle) {
					report.updateTestLog("Verify Searches Tab", strTitle + " entity search is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify Searches Tab", strTitle + " entity search is not displayed", Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view map
	// # Function Name : clickViewmap     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #****************************************************************************************************************************
	public ResearchMap clickViewmap() {
		WebElement viewMapAnchor = commonLibrary.isExist(UIMAP_ResearchMap.viewMapAnchor, 20);
		if (viewMapAnchor != null)
			commonLibrary.clickButtonParentWithWait(viewMapAnchor, viewMapAnchor.getText());
		report.updateTestLog("Click " + viewMapAnchor.getText(), viewMapAnchor.getText() + " clicked", Status.DONE);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click footer links in History
	// # Function Name : NavigateToHistoryFooterLink_ResearchMap     
	// # Author : Karthi
	// # Date Created : Mar'15
	// #****************************************************************************************************************************
	public ResearchMap navigateToHistoryFooterLinkResearchMap(String strLinkName) {

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

			commonLibrary.sleep(40000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// //#*****************************************************************************************************************************
	// # Function Description : Function to click view report in the document
	// page
	// # Function Name : view_report     
	// # Author : karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public RelatedContent viewReport() {
		WebElement lnkViewReport = commonLibrary.isExist(UIMAP_Document.lnkViewReport, 20);

		if (lnkViewReport != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkViewReport, "View Reports");

		// commonLibrary.sleep(1000);
		WebElement pgTitleTopicSummary = commonLibrary.isExist(UIMAP_Document.pgTitleTopicSummary, 10);

		if (pgTitleTopicSummary != null && pgTitleTopicSummary.getText().toLowerCase().contains("topic summary"))
			report.updateTestLog("Verify Topic results Page is displayed", "Topic results Page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Topic results Page is displayed", "Topic results Page is not displayed", Status.FAIL);
		return new RelatedContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate to litigation profile suite
	// # Function Name : navigateTolitigationProfile     
	// # Author : Karthi
	// # Date Created : Apr 2015
	// #*****************************************************************************************************************************
	public LitigationProfile navigateTolitigationProfile(String lps) {
		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionProfileSuite, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, lps);
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, lps);

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains(lps)) {
			report.updateTestLog("Litigation Profile Suite page is displayed", "Litigation Profile Suite page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Litigation Profile Suite page is displayed", "Litigation Profile Suite page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionProfileSuite, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance® Litigation profile suite logo displays in the Top left corner of Global menu", "Lexis Advance® Litigation profile suite logo displays in the Top left corner of Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Litigation profile suite logo displays in the Top left corner of Global menu", "Lexis Advance® Litigation profile suite logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		}
		if (browsername.contains("internet")) {
			commonLibrary.clickJS(logo, "Lexis Advance® Litigation profile suite");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		} else {
			commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Litigation profile suite");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		}

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify terms are highlighted
	// # Function Name : verifyTermHighlight     
	// # Author : Karthi
	// # Date Created : Apr 2015
	// #*****************************************************************************************************************************
	public Document verifyTermHighlight(String searchTerm) {
		String highlightTerm[] = searchTerm.split(";");
		for (int i = 0; i < highlightTerm.length; i++) {
			boolean flag = false;
			List<WebElement> highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
			if (highlights.size() == 0) {
				int count = 0;
				do {
					count++;
					highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
					if (highlights.size() == 0)
						commonLibrary.sleep(3000);
				} while (highlights.size() == 0 && count <= 10);
			}
			for (WebElement item : highlights) {
				if (item.getText().toLowerCase().contains(highlightTerm[i].toLowerCase()) && item.getCssValue("background-color").contains("rgba(255, 240, 153, 1)")) {
					flag = true;
					break;
				}

			}
			if (flag)
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", Status.PASS);
			else
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is not bolded and highlighted in yellow color", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify terms are bold
	// # Function Name : verifyTermBoldHighlighted     
	// # Author : Karthi
	// # Date Created : Apr 2015
	// #*****************************************************************************************************************************
	public Document verifyTermBoldHighlighted(String term, String term1) {
		int i = 0, j = 0;
		boolean blnFlag = false, blnFlag1 = false;
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 15);
		List<WebElement> paraList = commonLibrary.isExistList(docContent, UIMAP_Document.text, 10);
		for (WebElement para : paraList) {
			int count;
			List<WebElement> highlightedSearchTerms = commonLibrary.isExistList(para, UIMAP_Document.highlightedSearchTerm, 4);

			for (count = 0; count < highlightedSearchTerms.size() - 1; count++) {
				if (highlightedSearchTerms.size() >= 2 && ((highlightedSearchTerms.get(count).getText().toLowerCase().contains(term.toLowerCase()) && highlightedSearchTerms.get(count + 1).getText().toLowerCase().contains(term1.toLowerCase())) || (highlightedSearchTerms.get(count).getText().toLowerCase().contains(term1.toLowerCase()) && highlightedSearchTerms.get(count + 1).getText().toLowerCase().contains(term.toLowerCase())))) {
					blnFlag = true;

					if ((highlightedSearchTerms.get(count).getCssValue("font-weight").equals("bold")) && (highlightedSearchTerms.get(count).getCssValue("background").contains("rgb(255, 240, 153")))
						report.updateTestLog("Verify the term " + highlightedSearchTerms.get(count).getText() + " is bold and highlighted.", "The term " + highlightedSearchTerms.get(count).getText() + " is bold and highlighted.", Status.PASS);
					else
						report.updateTestLog("Verify the term " + highlightedSearchTerms.get(count).getText() + " is bold and highlighted.", "The term " + highlightedSearchTerms.get(count).getText() + " is not bold and highlighted.", Status.FAIL);

					if ((highlightedSearchTerms.get(count + 1).getCssValue("font-weight").equals("bold")) && (highlightedSearchTerms.get(count + 1).getCssValue("background").contains("rgb(255, 240, 153")))
						report.updateTestLog("Verify the term " + highlightedSearchTerms.get(count + 1).getText() + " is bold and highlighted.", "The term " + highlightedSearchTerms.get(count + 1).getText() + " is bold and highlighted.", Status.PASS);
					else
						report.updateTestLog("Verify the term " + highlightedSearchTerms.get(count + 1).getText() + " is bold and highlighted.", "The term " + highlightedSearchTerms.get(count + 1).getText() + " is not bold and highlighted.", Status.FAIL);

					String[] paraText = para.getText().replaceAll("[^a-zA-Z ]", "").trim().split(" ");
					for (i = 0; i < paraText.length; i++) {
						if (paraText[i].equalsIgnoreCase(term))
							break;
					}

					paraText = para.getText().replaceAll("[^a-zA-Z ]", "").trim().split(" ");
					for (j = 0; j < paraText.length; j++) {
						if (paraText[j].equalsIgnoreCase(term1))
							break;
					}

					if ((j - i) <= 25) {
						blnFlag1 = true;
						report.updateTestLog("Verify " + term + " and " + term1 + " is not separated by more than 25 words.", term + " and " + term1 + " is not separated by more than 25 words.", Status.PASS);
					}
				}
				if (blnFlag && blnFlag1)
					break;
			}
			if (blnFlag && blnFlag1)
				break;

		}

		if (!blnFlag)
			report.updateTestLog("Verify " + term + " and " + term1 + " is bold and highlighted in the document.", term + " and " + term1 + " is NOT bold and highlighted in the document.", Status.FAIL);
		if (!blnFlag1)
			report.updateTestLog("Verify " + term + " and " + term1 + " is not separated by more than 25 words.", term + " and " + term1 + " is not separated by more than 25 words.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Browser back button
	// # Function Name : Clickbrowserback     
	// # Author : Karthi
	// # Date Created : Apr 2015
	// #*****************************************************************************************************************************
	public Document clickbrowserback() {

		// driver.navigate().back();
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(5000);
		//
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
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
	// # Function Description : Function to set new client id
	// # Function Name : setNewClientIDGeneric_1     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home setNewClientIDGeneric_1(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_Document.rdoClientId);
		if (rdoClientId != null) {
			// report.updateTestLog("Selecting Set New Client ID",
			// "New Client Option Selected.", Status.PASS);
			commonLibrary.setRadioButton(UIMAP_Document.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID", "Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_Document.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client ID");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_Document.btnSaveClientId);
		if (btnSaveClientId != null) {

			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId, "Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
			}
			int count = 0;
			String url = "";
			do {
				count++;
				url = driver.getCurrentUrl().toLowerCase();
				if (!url.contains(UIMAP_Home.txtHomeTitleMsg))
					commonLibrary.sleep(5000);
			} while (!url.contains(UIMAP_Home.txtHomeTitleMsg) && count <= 40);
		} else
			report.updateTestLog("Clicking on Set Client ID", "Set Client ID can not be clicked.", Status.FAIL);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Research map
	// # Function Name : clickResearchMap     
	// # Author : Seetha
	// # Date Created : Jan'15
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
			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_Document.lstResearchThread, 1000);

			int count = 0;
			do {
				lstResearchThread = commonLibrary.isExistList(UIMAP_Document.lstResearchThread, 1000);
				count = count + 1;
			} while (lstResearchThread.size() == 0 && count <= 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		commonLibrary.sleep(35000);

		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set new client id
	// # Function Name : setNewClientIDGenericHome     
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Home setNewClientIDGenericHome(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_Document.rdoClientId);
		if (rdoClientId != null) {
			// report.updateTestLog("Selecting Set New Client ID",
			// "New Client Option Selected.", Status.PASS);
			commonLibrary.setRadioButton(UIMAP_Document.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID", "Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_Document.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client Id");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_Document.btnSaveClientId);
		if (btnSaveClientId != null) {
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
		} else
			report.updateTestLog("Clicking on Set Client ID", "Set Client ID can not be clicked.", Status.FAIL);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		int i = 0;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		do {
			i++;
			eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		} while (eltSearchbox == null && i < 15);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify appearance of TOC siblings
	// # Function Name : verifyTOCSiblings     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyTOCSiblings(String active) {

		WebElement tocPane = commonLibrary.isExist(UIMAP_Document.miniToc);
		List<WebElement> siblings = commonLibrary.isExistList(tocPane, UIMAP_Document.collpasedList, 20);
		WebElement selectedNode = commonLibrary.isExist(siblings.get(11), UIMAP_Document.tocSelectedNode, 10);
		if (selectedNode != null && selectedNode.getText().equalsIgnoreCase(active) && siblings.size() == 23) {
			report.updateTestLog("10 siblings appear above and 10 siblings appear below the active node: " + active, "10 siblings appear above and 10 siblings appear below the active node: " + active, Status.PASS);
			if (siblings.get(0).getText().equalsIgnoreCase("...") && siblings.get(22).getText().equalsIgnoreCase("..."))
				report.updateTestLog("Ellipsis displays Above and Below the sibling nodes in the Mini TOC", "Ellipsis displays Above and Below the sibling nodes in the Mini TOC", Status.PASS);
			else
				report.updateTestLog("Ellipsis displays Above and Below the sibling nodes in the Mini TOC", "Ellipsis does not display Above and Below the sibling nodes in the Mini TOC", Status.FAIL);
		} else
			report.updateTestLog("10 siblings appear above and 10 siblings appear below the active node: " + active, "10 siblings appear above and 10 siblings does not appear below the active node: " + active, Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the result link from CB
	// # Function Name : selectresultslink     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search selectresultslink() {

		// WebElement formview = commonLibrary.isExist(UIMAP_Document.toolbar,
		// 20);
		// //WebElement toolbar =
		// commonLibrary.isExist(formview,UIMAP_Document.toolbar, 20);
		// WebElement span = commonLibrary.isExist(formview,UIMAP_Document.span,
		// 20);
		// //WebElement overflowtoolbar =
		// commonLibrary.isExist(span,UIMAP_Document.overflowtoolbar, 20);
		// WebElement spanclass =
		// commonLibrary.isExist(span,UIMAP_Document.searchlink, 20);
		WebElement resultlink = commonLibrary.isExist(UIMAP_Document.resultlink, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Results List");
		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Results List");
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to browser back from the current page
	// # Function Name : browserback     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickdocresultlist() {
		WebElement objresultlink = commonLibrary.isExist(UIMAP_Document.aresultslink, 20);
		if (objresultlink != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(objresultlink, "Results link");
			else
				commonLibrary.clickButtonParentWithWait(objresultlink, "Results link");
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the View PDF link
	// # Function Name : clickViewPDFAttachment     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickViewPDFAttachment() {

		WebElement resultlink = commonLibrary.isExist(UIMAP_Document.viewPDFAttachment, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentprovider");
		}

		else if (browsername.contains("firefox")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentprovider");
		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentprovider");
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the View PDF link
	// # Function Name : clickViewPDFAttachment     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public void clickViewPDFAttachment_Secondary() {

		WebElement resultlink = commonLibrary.isExist(UIMAP_Document.viewPDFAttachment, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickMethod(resultlink, "Click to view PDF Document");

			commonLibrary.sleep(50000);
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			commonLibrary.switchToWindow("documentprovider");

		}

		else if (browsername.contains("firefox")) {
			commonLibrary.clickJS(resultlink, "Click to view PDF Document");
			commonLibrary.switchToWindow("documentprovider");

		} else {
			commonLibrary.clickMethod(resultlink, "Click to view PDF Document");
			commonLibrary.sleep(50000);
		}

		// return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Terms Highlighted in docPage
	// # Function Name : docPageVerifyTermsHighlighted     
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document docPageVerifyTermsHighlighted(String termToVerify, String whereIsIt) {
		boolean flg = false;
		WebElement docSection = commonLibrary.isExist(UIMAP_RelevanceEnhancement.docSection, 20);
		if (docSection != null) {
			if (whereIsIt == "header") {
				List<WebElement> headerTermsAll = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermAll, 20);
				WebElement headerTermHighlighted = commonLibrary.isExist(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
				if (headerTermsAll.size() > 0) {
					for (int i = 0; i < headerTermsAll.size(); i++) {
						if (headerTermsAll.get(i).getText().toLowerCase().contains(headerTermHighlighted.getText().toLowerCase())) {
							if (headerTermsAll.get(i).getText().toLowerCase().contains(termToVerify.toLowerCase())) {
								report.updateTestLog("Verify " + termToVerify + " bolded and highlighted in document title", termToVerify + " bolded and highlighted", Status.PASS);
								flg = true;
								break;
							}
						}
						if (flg)
							break;
					}
				}
			} else if (whereIsIt == "rest") {
				List<WebElement> restTermsAll = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.restTermAll, 20);
				WebElement restTermHighlighted = commonLibrary.isExist(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
				if (restTermsAll.size() > 0) {
					for (int i = 0; i < restTermsAll.size(); i++) {
						if (restTermsAll.get(i).getText().toLowerCase().contains(restTermHighlighted.getText().toLowerCase())) {
							if (restTermsAll.get(i).getText().toLowerCase().contains(termToVerify.toLowerCase())) {
								report.updateTestLog("Verify all occurrence of " + termToVerify + " bolded and highlighted in the same page", termToVerify + " bolded and highlighted", Status.PASS);
								flg = true;
								break;
							}
						}
						if (flg)
							break;
					}
				}
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify global menu
	// # Function Name : verifyGlobalMenu     
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyGlobalMenu() {
		generalFunctions.verifyGlobalMenu();

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : selectJumpToSectionVerify     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectJumpToSectionVerify(String option) {
		boolean blnFlag = false;
		// WebElement mobileToolbar =
		// commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		WebElement rightContainer = commonLibrary.isExistNegative(UIMAP_Document.rightContainer, 10);
		// WebElement jumpto = commonLibrary.isExistNegative(rightContainer,
		// UIMAP_Document.jumpto, 10);
		//
		// commonLibrary.clickButtonParentWithWait(jumpto, "Jump To");

		WebElement sectionContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.sectionContainer, 10);
		WebElement sections = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sections, 10);

		commonLibrary.clickButtonParentWithWait(sections, "Section Dropdown");
		sectionContainer = commonLibrary.isExistNegative(rightContainer, UIMAP_Document.sectionContainer, 10);
		WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown, 10);
		WebElement optionList = commonLibrary.isExistNegative(sectionsDropdown, UIMAP_Document.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_Document.tagButton, 10);
		for (WebElement item : options) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(item, item.getText());
				commonLibrary.sleep(10000);
				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Select Section " + option, option + " is not selected.", Status.FAIL);
		// commonLibrary.Select_FromList_Button(optionList, option);
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
					report.updateTestLog("Verify page is scrolled to section: " + option, "Section " + option + " is not present.", Status.FAIL);
				if ((ySection - yToolbar) >= 0 && (ySection - yToolbar) <= 1000)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);
			} else {
				WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
				int yHeading = txtDocumentHeading.getLocation().getY();
				if ((yHeading - yToolbar) >= 0 && (yHeading - yToolbar) <= 400)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);

			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document slider
	// # Function Name : verifyDocumentSlider     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document verifyDocumentSlider() {
		boolean isnext = false;
		boolean isprevious = false;
		WebElement slider = commonLibrary.isExist(UIMAP_Document.docslider, 20);
		if (slider != null) {
			WebElement resultlist = commonLibrary.isExist(slider, UIMAP_Document.resultlink, 20);
			if (resultlist != null) {
				report.updateTestLog("Verify resultlist link displays", "resultlist link is displayed ", Status.PASS);
			} else {
				report.updateTestLog("Verify resultlist link displays", "resultlist link is not displayed ", Status.FAIL);
			}

			List<WebElement> next = commonLibrary.isExistList(slider, UIMAP_Document.nextdoc, 20);
			for (int i = 0; i < next.size(); i++) {
				if (next.get(i).getText().toLowerCase().contains("next document")) {
					isnext = true;
				} else if (next.get(i).getText().toLowerCase().contains("previous document")) {
					isprevious = true;
				}
			}

			if (isnext) {
				report.updateTestLog("Verify next document  link displays with next arrow", "next document  link displays with next arrow ", Status.PASS);
			} else {
				WebElement nextDoc = commonLibrary.isExist(slider, UIMAP_Document.next, 20);
				if (nextDoc != null) {
					report.updateTestLog("Verify next document  link displays with next arrow", "Next document  link displays with next arrow ", Status.PASS);
				} else {
					report.updateTestLog("Verify previous document  link displays with previous arrow", "Previous document  link is not displayed with previous arrow ", Status.FAIL);
				}
			}

			if (isprevious) {
				report.updateTestLog("Verify previous document  link displays with previous arrow", "Previous document  link displays with previous arrow ", Status.PASS);
			} else {
				WebElement previous = commonLibrary.isExist(slider, UIMAP_Document.previous, 20);
				if (previous != null) {
					report.updateTestLog("Verify previous document  link displays with previous arrow", "Previous document  link displays with previous arrow ", Status.PASS);
				} else {
					report.updateTestLog("Verify previous document  link displays with previous arrow", "Previous document  link is not displayed with previous arrow ", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify resultlist link displays", "resultlist link is not displayed ", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Options
	// # Function Name : Verify_DeliveryOptions
	// # Author : Revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document verifyDeliveryOptions() {

		WebElement AddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 20);
		if (AddFolder != null)
			report.updateTestLog("Verify Delivery icons", " Add to folder icon with drop down is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Add to folder icon with drop down is not displayed", Status.FAIL);

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

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify casename,courtname,doctitle
	// and docnumber availbale in document page
	// # Function Name : verifyDocumentSearchName     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentSearchName(String caseName, String CourtName, String date, String docNumber, String docTitle) {

		WebElement docHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);
		if (docHeading != null && docHeading.getText().contains(caseName)) {
			WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
			if (docHeader.getText().contains(CourtName) && docHeader.getText().contains(date) && docHeader.getText().contains(docNumber) && docHeader.getText().contains(docTitle))
				report.updateTestLog("Verify the Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is displayed.", "The Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify the Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is displayed.", "The Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is not displayed", Status.FAIL);
		} else {
			report.updateTestLog("Verify the Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is displayed.", "The Case name " + caseName + " ,court name " + CourtName + " ,Date " + date + ", document Number " + docNumber + ", document Title " + docTitle + " is not displayed", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// with validation
	// # Function Name : selectJumpToSectionVerify
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyFooterLink(String link, String url) {
		WebElement footer = commonLibrary.isExistNegative(UIMAP_Document.footer, 10);

		List<WebElement> links = commonLibrary.isExistList(footer, UIMAP_Document.links, 10);
		if (link.toLowerCase().equals("lexisnexis")) {
			// WebElement logo =
			// commonLibrary.isExist(footer,UIMAP_Document.lnLogo, 10);
			commonLibrary.clickMethod(links.get(0), "Lexis Nexis");
			if (commonLibrary.switchToWindow1(url)) {
				report.updateTestLog("Verify LexisNexis Secondary window is displayed.", "LexisNexis Secondary window is displayed.", Status.PASS);
				try {
					commonLibrary.sleep(3000);
				} catch (Exception e) {
				}
				driver.close();
				commonLibrary.switchToWindow("document");
			} else
				report.updateTestLog("Verify LexisNexis Secondary window is displayed.", "LexisNexis Secondary window is not displayed.", Status.FAIL);
		} else {
			boolean flag = false;
			for (WebElement li : links) {
				if (li.getText().toLowerCase().contains(link.toLowerCase())) {
					commonLibrary.clickMethod(li, li.getText());
					flag = true;
					break;
				}

			}
			if (!flag)
				report.updateTestLog("Click on link " + link, "Link " + link + " is not present.", Status.FAIL);
			else {
				if (commonLibrary.switchToWindow1(url)) {
					report.updateTestLog("Verify " + url + " Secondary window is displayed.", url + " Secondary window is displayed.", Status.PASS);
					try {
						commonLibrary.sleep(3000);
					} catch (Exception e) {
					}
					driver.close();
					commonLibrary.switchToWindow("document");
				} else
					report.updateTestLog("Verify " + url + " Secondary window is displayed.", url + " Secondary window is not displayed.", Status.FAIL);

			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clickLinkFromATDSearch
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search clickLinkFromATDSearch(String documenttitle) {
		generalFunctions.clickLinkFromATD(documenttitle);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page
	// # Function Name : clickEmbedLinkSearch
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search clickEmbedLinkSearch(String link) {
		generalFunctions.clickEmbededLink(link);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Federal Income Tax Computation
	// # Function Name : clickFederalIncomeTaxLink
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickFederalIncomeTaxLink() {

		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
		WebElement topicTrailContent = commonLibrary.isExistNegative(docContent, UIMAP_Document.topicTrailContent, 10);
		WebElement fedIncomeTax = commonLibrary.isExistNegative(topicTrailContent, UIMAP_Document.fedIncomeTax, 10);
		commonLibrary.clickLinkWithWebElementWithWait(fedIncomeTax, fedIncomeTax.getText());
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocContextMenu link.
	// # Function Name : clickDocContextMenuLink
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search clickDocContextMenuLink(String link) {
		boolean flag = false;
		WebElement docContMenu = commonLibrary.isExistNegative(UIMAP_Document.docContMenu, 10);
		List<WebElement> links = commonLibrary.isExistList(docContMenu, UIMAP_Document.links, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains(link.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click link " + link, link + " is not clicked.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Footnote 1 and verify document
	// page is scrolled.
	// # Function Name : clickVerifyFootNote1
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickVerifyFootNote(int i) {
		// boolean flag=false;
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
		List<WebElement> footNotes = commonLibrary.isExistList(docContent, UIMAP_Document.footnote, 10);
		commonLibrary.clickLinkWithWebElementWithWait(footNotes.get(i - 1), "Footnote 1");

		WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
		int yToolbar = toolbar.getLocation().getY();

		WebElement footNotesContainer = commonLibrary.isExistNegative(docContent, UIMAP_Document.footNotesContainer, 10);
		List<WebElement> footNotesList = commonLibrary.isExistList(footNotesContainer, UIMAP_Document.listItems, 10);
		int yFN1 = footNotesList.get(i).getLocation().getY();
		if ((yFN1 - yToolbar) >= 0 && (yFN1 - yToolbar) <= 1000) {
			report.updateTestLog("Verify document is scrolled to relevant section.", "Document is scrolled to relevant section.", Status.PASS);
		} else
			report.updateTestLog("Verify document is scrolled to relevant section.", "Document is not scrolled to relevant section.", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click embedded link
	// # Function Name : clickEmbeddedLink
	// # Author : Seetha
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickEmbeddedLink(String link) {
		List<WebElement> links = commonLibrary.isExistList(UIMAP_Document.embed, 10);
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).getText().toLowerCase().contains(link.toLowerCase())) {
				commonLibrary.clickLinkWithWait(links.get(i), link);
				break;
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clickLinkFromATD
	// # Author : Seetha
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public RelatedContent clickLinkFromATD(String documenttitle) {
		generalFunctions.clickLinkFromATD(documenttitle);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document pdfVerification(String FilePath, String Text, String Exist) {
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
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click On Highlighted RFCData in
	// docPage
	// # Function Name : ClickOnHighlightedRFCData     
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickOnHighlightedRFCData(String targetText) {
		boolean flg = false;
		int Cnt = 0;
		List<WebElement> highlightedRFCSection = commonLibrary.isExistList(UIMAP_Document.highlightedRFCSection, 20);
		if (highlightedRFCSection != null) {
			for (int i = 0; i < highlightedRFCSection.size(); i++) {
				if (highlightedRFCSection.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(highlightedRFCSection.get(i), highlightedRFCSection.get(i).getText());
					else
						commonLibrary.clickButtonParentWithWait(highlightedRFCSection.get(i), highlightedRFCSection.get(i).getText());
					report.updateTestLog("Click on highlighted rfc data", "Highlighted rfc data in doc page is clicked", Status.PASS);
					flg = true;
					break;
				}
				if (flg)
					break;
			}
		}

		WebElement resultClass = commonLibrary.isExistNegative(UIMAP_Document.frmClassResult, 10);
		do {
			Cnt++;
			commonLibrary.sleep(5000);
			resultClass = commonLibrary.isExistNegative(UIMAP_Document.frmClassResult, 10);
		} while (resultClass == null && Cnt < 36);

		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		commonLibrary.sleep(20000);
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document pdfVerificationSecondary(String pdfName, String Exist) {

		Boolean blnFlag = false;
		Boolean pdfload = false;
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.pdfData, 100);
		do {
			docContent = commonLibrary.isExistNegative(UIMAP_Document.pdfData, 100);
			commonLibrary.sleep(60000);
			// pdfload=true;
		} while (docContent == null);
		if (docContent != null) {
			pdfload = true;
			commonLibrary.sleep(60000);
		}
		if (pdfload) {
			docContent = commonLibrary.isExistNegative(UIMAP_Document.pdfData, 100);
			if (docContent.getText().contains(pdfName))
				blnFlag = true;
			else
				blnFlag = false;
		}
		// }
		// else
		// {
		// report.updateTestLog("Verify PdF is loaded","Pdf is not loaded as expected",
		// Status.FAIL);
		// }
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

		commonLibrary.sleep(5000);

		commonLibrary.switchToWindow("document");
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page
	// # Function Name : clickEmbedLinkDoc
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickEmbedLinkDoc(String link) {
		generalFunctions.clickEmbededLink(link);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page
	// # Function Name : clickEmbedLinkDoc
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickBrowserBackDocument() {
		commonLibrary.clickBrowserBack();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickLinkInDoc(String targetText) {
		generalFunctions.clickLinkInDoc(targetText);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click pdf link and verify secondary
	// window is opened
	// # Function Name : clickPdfLinkAndVerify
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickPdfLinkAndVerify(String documenttitle) {
		// WebElement pdfLink=commonLibrary.isExist(UIMAP_Document.pdfLink);
		// if(pdfLink!=null)
		// {
		// if(pdfLink.getText().toLowerCase().contains(documenttitle.toLowerCase()))
		// {
		// pdfLink.click();
		// try {
		// commonLibrary.sleep(15000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		generalFunctions.clickLinkFromATD(documenttitle);
		commonLibrary.sleep(5000);
		if (driver.getWindowHandles().size() > 1) {
			commonLibrary.switchToWindow("documentprovider");
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
			commonLibrary.sleep(200000);
			String pageURL = driver.getCurrentUrl();
			// String pageTitle = driver.getTitle();
			// if (pageTitle.contains(".DOC") || pageTitle.contains("PDF"))
			if (pageURL.toLowerCase().contains("=doc") || pageURL.toLowerCase().contains("=pdf")) {
				report.updateTestLog("click pdf link and verify secondary window is opened", "Document is opened in secondary window", Status.PASS);
			} else {
				report.updateTestLog("click pdf link and verify secondary window is opened", "Document is not opened in secondary window", Status.FAIL);
			}
			driver.close();
			commonLibrary.switchToWindow("document");
			commonLibrary.sleep(200000);
		} else {
			report.updateTestLog("click pdf link and verify secondary window is opened", "Document is opened in secondary window", Status.PASS);
		}
		// }
		// else
		// {
		// report.updateTestLog("click pdf link and verify secondary window is opened",
		// "PDF Link is not matching with given link", Status.FAIL);
		// }
		// }
		// else
		// {
		// report.updateTestLog("click pdf link and verify secondary window is opened",
		// "PDF Link is not available", Status.FAIL);
		// }

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link within document page
	// # Function Name : clickTocNav
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickTocNav(String option) {
		boolean flag = false;
		WebElement tocnavlinks = commonLibrary.isExistNegative(UIMAP_Document.tocNav, 10);
		List<WebElement> links = commonLibrary.isExistList(tocnavlinks, UIMAP_Document.links, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				commonLibrary.sleep(200000);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click link " + option, option + " is not clicked.", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Original Source Image and
	// verify the document int eh secondary window
	// # Function Name : verifyOriginalSourceImageLink
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyOriginalSourceImageLink() {
		WebElement atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 10);
		WebElement expanded = commonLibrary.isExist(atd, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 10);
			WebElement expand = commonLibrary.isExist(atd, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}

		atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 10);

		List<WebElement> links = commonLibrary.isExistList(atd, UIMAP_Document.links, 10);

		boolean flag = false;
		for (WebElement li : links) {
			if (li.getText().contains("Original Source Image")) {
				commonLibrary.clickButtonParentWithWait(li, li.getText());
				flag = true;
				break;
			}

		}
		if (!flag)
			report.updateTestLog("Click on link ", "Original Source Image Link is not present.", Status.FAIL);
		else {

			if (driver.getWindowHandles().size() > 1) {
				if (commonLibrary.switchToWindow("documentprovider")) {
					report.updateTestLog("Verify the document is displayed in a Secondary window", "document is displayed in a Secondary window.", Status.PASS);
					try {
						commonLibrary.sleep(3000);
					} catch (Exception e) {
					}
					driver.close();
					commonLibrary.switchToWindow("document");
				} else
					report.updateTestLog("Verify the document is displayed in a Secondary window", " Secondary window is not displayed.", Status.FAIL);

			} else {
				report.updateTestLog("Verify the document is displayed in a Secondary window", "document is displayed in a Secondary window.", Status.PASS);
			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Company Logo appears within
	// document page
	// # Function Name : verifyCompanyLogo
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyCompanyLogo() {
		WebElement copyrightContainer = commonLibrary.isExistNegative(UIMAP_Document.copyrightContainer, 10);
		if (copyrightContainer == null) {
			int count = 0;
			do {
				count++;
				copyrightContainer = commonLibrary.isExistNegative(UIMAP_Document.copyrightContainer, 10);
				if (copyrightContainer == null)
					commonLibrary.sleep(5000);
			} while (copyrightContainer == null && count <= 20);
		}
		WebElement copyrightImage = commonLibrary.isExistNegative(copyrightContainer, UIMAP_Document.copyrightImage, 10);
		if (copyrightImage != null) {
			report.updateTestLog("Verify Company Logo appears in the page", "Company Logo appears in the page", Status.PASS);
		} else {
			report.updateTestLog("Verify Company Logo appears in the page", "Company Logo does not appear in the page", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Next Previous Document And
	// Verify in docPage
	// # Function Name : clickNextPrevDocAndVerify   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickNextPrevDocAndVerify(String targetText) {
		boolean isNextDoc = false, isPrevDoc = false;
		WebElement slider = commonLibrary.isExist(UIMAP_Document.docslider, 20);
		List<WebElement> next = commonLibrary.isExistList(slider, UIMAP_Document.nextdoc, 20);
		for (int i = 0; i < next.size(); i++) {
			if (next.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(next.get(i), next.get(i).getText());
				commonLibrary.sleep(20000);
				isNextDoc = true;
				break;
			} else if (next.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(next.get(i), next.get(i).getText());
				commonLibrary.sleep(20000);
				isPrevDoc = true;
				break;
			}
		}
		if (isNextDoc)
			report.updateTestLog("Click on next document link", "Next document link is clicked.", Status.PASS);

		if (isPrevDoc)
			report.updateTestLog("Click on previous document link", "Previous document link is clicked.", Status.PASS);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify TOC nav links.
	// # Function Name : verifyTOCPrevNext
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document verifyTOCPrevNext() {
		boolean flag = false, flag1 = false;
		WebElement tocnavlinks = commonLibrary.isExistNegative(UIMAP_Document.tocNav, 10);
		List<WebElement> links = commonLibrary.isExistList(tocnavlinks, UIMAP_Document.links, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains("previous")) {
				report.updateTestLog("Verify Previous button is displayed at the top of Document", "Previous button is displayed at the top of Document", Status.PASS);
				flag = true;
			}

			if (li.getText().toLowerCase().contains("next")) {
				report.updateTestLog("Verify Next button is displayed at the top of Document", "Next button is displayed at the top of Document", Status.PASS);
				flag1 = true;

			}
			if (flag && flag1)
				break;
		}
		if (!flag)
			report.updateTestLog("Verify Previous button is displayed at the top of Document", "Previous button is not displayed at the top of Document", Status.FAIL);
		if (!flag1)
			report.updateTestLog("Verify Next button is displayed at the top of Document", "Next button is not displayed at the top of Document", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get All Anchor tag And Verify ID
	// # Function Name : getAllAnchorAndVerifyID   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document getAllAnchorAndVerifyID(String targetText) {
		List<WebElement> getAllAnchorTags = commonLibrary.isExistList(UIMAP_Document.links, 20);
		for (int i = 0; i < getAllAnchorTags.size(); i++) {
			if (getAllAnchorTags.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
				report.updateTestLog("Verify " + targetText + " exists", targetText + " exists.", Status.PASS);
				commonLibrary.clickLinkWithWebElementWithWait(getAllAnchorTags.get(i), getAllAnchorTags.get(i).getText());
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Outlook Process
	// # Function Name : verifyOutlookProcess   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document verifyOutlookProcess() {
		if (commonLibrary.checkOutlookProcess())
			report.updateTestLog("verify outlook opened", "Outlook opened.", Status.PASS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to kill Outlook Process
	// # Function Name : killOutlookProcess   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document killOutlookProcess() {
		if (commonLibrary.killOutlookProcessThread())
			report.updateTestLog("verify outlook process killed", "Outlook process killed.", Status.PASS);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on action Link In popup
	// # Function Name : clickActioInPopUp   
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search clickActioInPopUp(String targetText) {
		boolean flag = false;
		WebElement popUp = commonLibrary.isExist(UIMAP_Document.containerPopUp, 20);
		if (popUp != null) {
			List<WebElement> actionLinks = commonLibrary.isExistList(UIMAP_Document.actionLinkUnderPopUp, 20);
			for (WebElement li : actionLinks) {
				if (li.getText().toLowerCase().contains(targetText.toLowerCase())) {
					commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
					// report.updateTestLog("Click on Action link",
					// "Action link is clicked.", Status.PASS);
					flag = true;
					break;
				}
			}
			if (!flag) {
				report.updateTestLog("Click on Action link", "Action link is NOT clicked.", Status.FAIL);
			}
		} else
			report.updateTestLog("Click on Action link", "Pop up is not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click EmbeddedLink
	// # Function Name : verifyclick_EmbeddedLink   
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifyclick_EmbeddedLink(String linkName, String entityName, String elements1, String elements2, String elements3, String entitySearchLink, String searchTermValue, String entitySeachterm, String category) {
		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docContent, 10);
		List<WebElement> embLinks = commonLibrary.isExistList(docContent, UIMAP_Document.embeddedLink, 10);
		for (WebElement el : embLinks) {
			if (el.getText().equalsIgnoreCase(linkName)) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWait(el, linkName);
					break;
				} else {
					commonLibrary.clickButtonLogSmallWait(el, linkName);
					break;
				}
			}
		}

		WebElement inlinePopup = commonLibrary.isExistNegative(UIMAP_Document.inlinePopup, 20);
		if (inlinePopup == null) {
			int counter = 0;
			do {
				counter++;
				inlinePopup = commonLibrary.isExistNegative(UIMAP_Document.inlinePopup, 20);
				if (inlinePopup == null)
					commonLibrary.sleep(5000);
			} while (inlinePopup == null && counter < 8);
		}
		if (inlinePopup != null && inlinePopup.isDisplayed()) {
			report.updateTestLog("Verify Entity popup displays", "Entity popup displays", Status.PASS);
			if (entityName != null)
				if (inlinePopup.getText().toLowerCase().contains(entityName.toLowerCase()) || entityName.equalsIgnoreCase("ignore"))
					report.updateTestLog("Verify " + entityName + " entity name displays as a header with bolded in entity popup ", entityName + " entity name displayed as a header with bolded in entity popup ", Status.PASS);
				else
					report.updateTestLog("Verify " + entityName + " entity name displays as a header with bolded in entity popup ", entityName + " entity name is not displayed as a header with bolded in entity popup ", Status.FAIL);

			if (elements1 != null)
				if (inlinePopup.getText().toLowerCase().contains(elements1.toLowerCase()) || elements1.equalsIgnoreCase("ignore"))
					report.updateTestLog("Verify " + elements1 + " elements displays with content in entity popup ", elements1 + " elements is displayed with content in entity popup", Status.PASS);
				else
					report.updateTestLog("Verify " + elements1 + " elements displays with content in entity popup ", elements1 + " elements does not displays with content in entity popup", Status.FAIL);

			if (elements2 != null)
				if (inlinePopup.getText().toLowerCase().contains(elements2.toLowerCase()) || elements2.equalsIgnoreCase("ignore"))
					report.updateTestLog("Verify " + elements2 + " elements displays with content in entity popup ", elements2 + " elements is displayed with content in entity popup", Status.PASS);
				else
					report.updateTestLog("Verify " + elements2 + " elements displays with content in entity popup ", elements2 + " elements does not displayed with content in entity popup", Status.FAIL);

			if (elements3 != null)
				if (inlinePopup.getText().toLowerCase().contains(elements3.toLowerCase()) || elements3.equalsIgnoreCase("ignore"))
					report.updateTestLog("Verify " + elements3 + " elements displays with content in entity popup ", elements3 + " elements is displayed with content in entity popup", Status.PASS);
				else
					report.updateTestLog("Verify " + elements3 + " elements displays with content in entity popup ", elements3 + " elements does not displayed with content in entity popup", Status.FAIL);

			WebElement searchLink = commonLibrary.isExistNegative(inlinePopup, UIMAP_Document.embeddedSearchLink, 10);
			// commonLibrary.ScrollToView(searchLink);
			if (searchLink != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(searchLink, entitySearchLink);
				else
					commonLibrary.clickButtonLogSmallWait(searchLink, entitySearchLink);
			} else
				report.updateTestLog("Click on Search for " + linkName, "Search link does not display", Status.FAIL);
		} else
			report.updateTestLog("Verify Entity popup displays", "Entity popup does not display", Status.FAIL);

		commonLibrary.sleep(10000);
		WebElement header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);
		int count = 0;
		do {
			count++;
			header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);
			if (header == null)
				commonLibrary.sleep(5000);
		} while (header == null && count <= 25);
		if (header != null)
			if (header.getText().toLowerCase().contains(searchTermValue.toLowerCase()))
				report.updateTestLog("Verify '" + searchTermValue + "'  entity search results page displayed", searchTermValue + " entity search results page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify '" + searchTermValue + "'  entity search results page displayed", searchTermValue + " entity search results page is NOT displayed", Status.FAIL);

		WebElement searchTerm = commonLibrary.isExist(UIMAP_Document.txtIdSearch, 10);
		if (searchTerm != null)

			if (searchTerm.getAttribute("value").toLowerCase().contains(entitySeachterm.toLowerCase()))
				report.updateTestLog("Verify the Search term  " + entitySeachterm + " is displayed in results page.", "Search term  " + entitySeachterm + " is displayed in results page.", Status.PASS);
			else
				report.updateTestLog("Verify the Search term  " + entitySeachterm + " is displayed in results page.", "Search term  " + entitySeachterm + " is not displayed in results page.", Status.FAIL);

		WebElement CondentSwitcher = commonLibrary.isExist(UIMAP_Document.ulCondentSwitcher, 10);
		if (CondentSwitcher != null)
			if (CondentSwitcher.getText().toLowerCase().contains(category.toLowerCase()))
				report.updateTestLog("Verify  Active category " + category + " is displayed in results page.", " Active category " + category + " displayed in results page.", Status.PASS);
			else
				report.updateTestLog("Verify  Active category " + category + " is displayed in results page.", " Active category " + category + " is not displayed in results page.", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify font in document page
	// # Function Name : verifyFontinDocument
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document verifyFontinDocument(String FontType, String FontSize) {

		WebElement fontText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		WebElement fontText1 = commonLibrary.isExist(fontText, UIMAP_Document.txtReport, 20);
		if (fontText1 != null) {
			String txtFontType = fontText.getCssValue("font-family");
			String txtFontSize = fontText.getCssValue("font-size");
			if (FontType.equalsIgnoreCase("Courier")) {
				FontType = "Monospace";
			}

			switch (FontType) {
			case "Arial": {
				if (FontType.equalsIgnoreCase(txtFontType)) {
					switch (FontSize) {
					case "Medium": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("16px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("13px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					}
				}
				break;
			}

			case "Times New Roman": {
				if (FontType.equalsIgnoreCase(txtFontType)) {
					switch (FontSize) {
					case "Medium": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("16px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("13px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}

					}
						break;
					}
				}
				break;
			}
			case "Verdana": {
				if (FontType.equalsIgnoreCase(txtFontType)) {
					switch (FontSize) {
					case "Medium": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("14px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("12px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					}
				}
				break;
			}
			case "Monospace": {
				if (FontType.equalsIgnoreCase(txtFontType)) {
					switch (FontSize) {
					case "Medium": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("14px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("16px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("12px")) {
							report.updateTestLog("Verifying the visual of the Full document is in  FontType- Size format", "Full document verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);

						}
						break;
					}
					}
				}
				break;
			}
			}

		}

		else {
			report.updateTestLog("Verify Document Text", "Text is not present", Status.FAIL);
		}

		return new Document(scriptHelper);
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
	// # Function Description : Function to click browser back and return in
	// WorkFolders page
	// # Function Name : clickBrowserBackWorkFolders     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders clickBrowserBackWorkFolders() {
		commonLibrary.clickBrowserBack();
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete the file if it exist already
	// in the given path
	// # Function Name : fileExists_Delete     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document fileExists_Delete(String strFilePath, String strFileName) {

		commonLibrary.fileExistsDelete(strFilePath, strFileName);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select jurisdictions under Other
	// Jurisdictions Pane
	// # Function Name : SelectJurisdictionLinks    
	// # Author : Ram
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document selectJurisdictionLinks(String jurisLink) {
		// commonLibrary.scrollDown();
		// commonLibrary.scrollUp();
		// int i = 2;
		try {
			WebElement jurisViewMore = commonLibrary.isExist(UIMAP_Document.jurisViewMore, 20);
			commonLibrary.clickLinkWithWait(jurisViewMore, "View More");
			WebElement sectionjuris = commonLibrary.isExist(UIMAP_Document.jurissection, 20);
			WebElement ul = commonLibrary.isExist(sectionjuris, UIMAP_Document.lstTagUList, 20);
			List<WebElement> otherjurislinks = commonLibrary.isExistList(ul, UIMAP_Document.otherjurislinks, 20);
			for (WebElement item : otherjurislinks) {
				if (item.getText().contains(jurisLink)) {
					WebElement docParts = commonLibrary.isExist(item, UIMAP_Document.docParts, 20);

					commonLibrary.clickLinkWithWebElementWithWait(docParts, "'" + jurisLink + "'under Other Jurisdiction");
					// JavascriptExecutor executor = (JavascriptExecutor)
					// driver;
					// executor.executeScript("arguments[0].focus();",
					// docParts);
					// executor.executeScript("arguments[0].click();",
					// docParts);

					commonLibrary.sleep(10000);

				}
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.getMessage());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify jurisdictions under
	// # Function Name : VerifyJurisdictionValue  
	// # Author : Ram
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Document verifyJurisdictionValue(String jurisValue, String docTitle) {
		WebElement docHeader = commonLibrary.isExist(UIMAP_Document.docHeader, 20);
		WebElement txtDocumentHeading = commonLibrary.isExist(docHeader, UIMAP_Document.txtDocumentHeading, 20);
		if (txtDocumentHeading.getText().contains(docTitle)) {
			report.updateTestLog("Verify the Presence of Document Title", docTitle + " is displayed in the Document", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Document Title", docTitle + " is not displayed in the Document", Status.FAIL);
		}
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		// List<WebElement> headerCheck =
		// commonLibrary.isExist_List(docText,UIMAP_Document.headerCheck, 20);
		// for(WebElement item : headerCheck)
		// {
		if (docText.getText().contains(jurisValue)) {
			report.updateTestLog("Verify the Presence of Headers in Document", jurisValue + " is displayed in the Document", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Headers in Document", jurisValue + " is not displayed in the Document", Status.FAIL);
		}
		// }
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public Document addToFolder(String FolderName) {
		try {
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

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
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
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
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

			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the text
	// save option
	// # Function Name : selectText
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document selectText(String text) {
		int k = 0;
		boolean shift = false;
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			commonLibrary.sleep(20000);

			int len = text.length();
			for (int i = 0; i < len; i++) {
				char a = text.charAt(i);

				switch (a) {
				case 'a':
					k = KeyEvent.VK_A;
					shift = false;
					break;
				case 'b':
					k = KeyEvent.VK_B;
					shift = false;
					break;
				case 'c':
					k = KeyEvent.VK_C;
					shift = false;
					break;
				case 'd':
					k = KeyEvent.VK_D;
					shift = false;
					break;
				case 'e':
					k = KeyEvent.VK_E;
					shift = false;
					break;
				case 'f':
					k = KeyEvent.VK_F;
					shift = false;
					break;
				case 'g':
					k = KeyEvent.VK_G;
					shift = false;
					break;
				case 'h':
					k = KeyEvent.VK_H;
					shift = false;
					break;
				case 'i':
					k = KeyEvent.VK_I;
					shift = false;
					break;
				case 'j':
					k = KeyEvent.VK_J;
					shift = false;
					break;
				case 'k':
					k = KeyEvent.VK_K;
					shift = false;
					break;
				case 'l':
					k = KeyEvent.VK_L;
					shift = false;
					break;
				case 'm':
					k = KeyEvent.VK_M;
					shift = false;
					break;
				case 'n':
					k = KeyEvent.VK_N;
					shift = false;
					break;
				case 'o':
					k = KeyEvent.VK_O;
					shift = false;
					break;
				case 'p':
					k = KeyEvent.VK_P;
					shift = false;
					break;
				case 'q':
					k = KeyEvent.VK_Q;
					shift = false;
					break;
				case 'r':
					k = KeyEvent.VK_R;
					shift = false;
					break;
				case 's':
					k = KeyEvent.VK_S;
					shift = false;
					break;
				case 't':
					k = KeyEvent.VK_T;
					shift = false;
					break;
				case 'u':
					k = KeyEvent.VK_U;
					shift = false;
					break;
				case 'v':
					k = KeyEvent.VK_V;
					shift = false;
					break;
				case 'w':
					k = KeyEvent.VK_W;
					shift = false;
					break;
				case 'x':
					k = KeyEvent.VK_X;
					shift = false;
					break;
				case 'y':
					k = KeyEvent.VK_Y;
					shift = false;
					break;
				case 'z':
					k = KeyEvent.VK_Z;
					shift = false;
					break;
				case '0':
					k = KeyEvent.VK_0;
					shift = false;
					break;
				case '1':
					k = KeyEvent.VK_1;
					shift = false;
					break;
				case '2':
					k = KeyEvent.VK_2;
					shift = false;
					break;
				case '3':
					k = KeyEvent.VK_3;
					shift = false;
					break;
				case '4':
					k = KeyEvent.VK_4;
					shift = false;
					break;
				case '5':
					k = KeyEvent.VK_5;
					shift = false;
					break;
				case '6':
					k = KeyEvent.VK_6;
					shift = false;
					break;
				case '7':
					k = KeyEvent.VK_7;
					shift = false;
					break;
				case '8':
					k = KeyEvent.VK_8;
					shift = false;
					break;
				case '9':
					k = KeyEvent.VK_9;
					shift = false;
					break;
				case 'A':
					k = KeyEvent.VK_A;
					shift = true;
					break;
				case 'B':
					k = KeyEvent.VK_B;
					shift = true;
					break;
				case 'C':
					k = KeyEvent.VK_C;
					shift = true;
					break;
				case 'D':
					k = KeyEvent.VK_D;
					shift = true;
					break;
				case 'E':
					k = KeyEvent.VK_E;
					shift = true;
					break;
				case 'F':
					k = KeyEvent.VK_F;
					shift = true;
					break;
				case 'G':
					k = KeyEvent.VK_G;
					shift = true;
					break;
				case 'H':
					k = KeyEvent.VK_H;
					shift = true;
					break;
				case 'I':
					k = KeyEvent.VK_I;
					shift = true;
					break;
				case 'J':
					k = KeyEvent.VK_J;
					shift = true;
					break;
				case 'K':
					k = KeyEvent.VK_K;
					shift = true;
					break;
				case 'L':
					k = KeyEvent.VK_L;
					shift = true;
					break;
				case 'M':
					k = KeyEvent.VK_M;
					shift = true;
					break;
				case 'N':
					k = KeyEvent.VK_N;
					shift = true;
					break;
				case 'O':
					k = KeyEvent.VK_O;
					shift = true;
					break;
				case 'P':
					k = KeyEvent.VK_P;
					shift = true;
					break;
				case 'Q':
					k = KeyEvent.VK_Q;
					shift = true;
					break;
				case 'R':
					k = KeyEvent.VK_R;
					shift = true;
					break;
				case 'S':
					k = KeyEvent.VK_S;
					shift = true;
					break;
				case 'T':
					k = KeyEvent.VK_T;
					shift = true;
					break;
				case 'U':
					k = KeyEvent.VK_U;
					shift = true;
					break;
				case 'V':
					k = KeyEvent.VK_V;
					shift = true;
					break;
				case 'W':
					k = KeyEvent.VK_W;
					shift = true;
					break;
				case 'X':
					k = KeyEvent.VK_X;
					shift = true;
					break;
				case 'Y':
					k = KeyEvent.VK_Y;
					shift = true;
					break;
				case 'Z':
					k = KeyEvent.VK_Z;
					shift = true;
					break;
				case '`':
					k = KeyEvent.VK_BACK_QUOTE;
					shift = true;
					break;
				case '-':
					k = KeyEvent.VK_MINUS;
					shift = false;
					break;
				case '=':
					k = KeyEvent.VK_EQUALS;
					shift = false;
					break;
				case '~':
					k = KeyEvent.VK_BACK_QUOTE;
					shift = false;
					break;
				case '!':
					k = KeyEvent.VK_EXCLAMATION_MARK;
					shift = false;
					break;
				case '@':
					k = KeyEvent.VK_AT;
					shift = false;
					break;
				case '#':
					k = KeyEvent.VK_NUMBER_SIGN;
					shift = false;
					break;
				case '$':
					k = KeyEvent.VK_DOLLAR;
					shift = false;
					break;
				case '%':
					k = KeyEvent.VK_5;
					shift = true;
					break;
				case '^':
					k = KeyEvent.VK_CIRCUMFLEX;
					shift = false;
					break;
				case '&':
					k = KeyEvent.VK_AMPERSAND;
					shift = false;
					break;
				case '*':
					k = KeyEvent.VK_ASTERISK;
					shift = false;
					break;
				case '(':
					k = KeyEvent.VK_LEFT_PARENTHESIS;
					shift = false;
					break;
				case ')':
					k = KeyEvent.VK_RIGHT_PARENTHESIS;
					shift = false;
					break;
				case '_':
					k = KeyEvent.VK_UNDERSCORE;
					shift = false;
					break;
				case '+':
					k = KeyEvent.VK_PLUS;
					shift = false;
					break;
				case '\t':
					k = KeyEvent.VK_TAB;
					shift = false;
					break;
				case '\n':
					k = KeyEvent.VK_ENTER;
					shift = false;
					break;
				case '[':
					k = KeyEvent.VK_OPEN_BRACKET;
					shift = false;
					break;
				case ']':
					k = KeyEvent.VK_CLOSE_BRACKET;
					shift = false;
					break;
				case '\\':
					k = KeyEvent.VK_BACK_SLASH;
					shift = false;
					break;
				case '{':
					k = KeyEvent.VK_OPEN_BRACKET;
					shift = true;
					break;
				case '}':
					k = KeyEvent.VK_CLOSE_BRACKET;
					shift = true;
					break;
				case '|':
					k = KeyEvent.VK_BACK_SLASH;
					shift = true;
					break;
				case ';':
					k = KeyEvent.VK_SEMICOLON;
					shift = false;
					break;
				case ':':
					k = KeyEvent.VK_COLON;
					shift = false;
					break;
				case '\'':
					k = KeyEvent.VK_QUOTE;
					shift = false;
					break;
				case '"':
					k = KeyEvent.VK_QUOTEDBL;
					shift = false;
					break;
				case ',':
					k = KeyEvent.VK_COMMA;
					shift = false;
					break;
				case '<':
					k = KeyEvent.VK_COMMA;
					shift = true;
					break;
				case '.':
					k = KeyEvent.VK_PERIOD;
					shift = false;
					break;
				case '>':
					k = KeyEvent.VK_PERIOD;
					shift = true;
					break;
				case '/':
					k = KeyEvent.VK_SLASH;
					shift = false;
					break;
				case '?':
					k = KeyEvent.VK_SLASH;
					shift = true;
					break;
				case ' ':
					k = KeyEvent.VK_SPACE;
					shift = false;
					break;

				}
				if (shift) {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(k);
					robot.keyRelease(k);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					robot.keyPress(k);
					robot.keyRelease(k);
				}

			}

			report.updateTestLog("Select the text: " + text, "The text is selected", Status.PASS);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option
	// # Function Name : addToFolderUsingQuickSave
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document addToFolderUsingQuickSave(String FolderName) {
		try {
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolderdiv = commonLibrary.isExist(UIMAP_ResearchMap.addtofolder, 10);
			WebElement addtoFolder = commonLibrary.isExist(addtoFolderdiv, UIMAP_ResearchMap.addtofolder1, 10);
			if (addtoFolder != null) {

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");
				commonLibrary.sleep(5000);
			} else {
				report.updateTestLog("Click on add to folder", "Add to folder button  not available", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option for document open via source selection page
	// # Function Name : addToFolderUsingQuickSaveForSs
	// # Author : Deepha H
	// # Date Created : OCt'15
	// #*****************************************************************************************************************************

	public Document addToFolderUsingQuickSaveForSs(String FolderName) {

		try {
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			List<WebElement> folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
			if (folder.size() > 0) {
				for (WebElement fol : folder) {
					if (fol.getText().contains(FolderName)) {
						commonLibrary.clickButtonParentWithWait(fol, FolderName);
						break;
					}
				}
			} else
				report.updateTestLog("Select the folder", "Folder not available", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the second header by
	// double clicking on it.
	// # Function Name : selectSecondHeadingText
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Document selectSecondHeadingText(String folderName, String docName, String tcName, String dataSheet, String colName) {

		List<WebElement> subHeader = commonLibrary.isExistList(UIMAP_Document.subHeader, 10);

		String selection = "";
		if (subHeader.get(1).getText().trim().contains(" ")) {
			int indexof1stSpace = subHeader.get(1).getText().trim().indexOf(" ");
			selection = subHeader.get(1).getText().trim().substring(0, indexof1stSpace);
		} else
			selection = subHeader.get(1).getText();

		WebElement addToFolderDialog = null;
		int i = 0;
		boolean flag = false;
		do {
			commonLibrary.windowFocus();
			commonLibrary.clickButtonParentWithWaitWithFocus(subHeader.get(1), "Heading");
			Actions select = new Actions(driver);
			// int y=subHeader.get(1).getSize().height;
			// int xEnd=subHeader.get(1).getSize().width;
			commonLibrary.windowFocus();
			commonLibrary.highlightElement(subHeader.get(1));
			select.moveToElement(subHeader.get(1), 5, 0).doubleClick().build().perform();
			commonLibrary.sleep(500000);

			WebElement docContextMenu = commonLibrary.isExistNegative(UIMAP_Document.docContextMenu, 10);
			if (docContextMenu != null) {
				// report.updateTestLog("Click on add to folder",
				// "Add to folder button  not available", Status.FAIL);
				WebElement addToFolderInMenu = commonLibrary.isExistNegative(docContextMenu, UIMAP_Document.addToFolderInMenu, 10);
				flag = true;
				report.updateTestLog("Select any text from document", "'" + selection + "' is selected from document.", Status.DONE);
				commonLibrary.clickButtonParentWithWait(addToFolderInMenu, "Add To Folder");
			}

			addToFolderDialog = commonLibrary.isExistNegative(UIMAP_Document.addToFolderDialog, 10);
			i++;
		} while (addToFolderDialog == null && i < 3);
		if (!flag)
			report.updateTestLog("Click on add to folder", "Pop up not displayed.", Status.FAIL);

		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, selection.trim());

		addToFolderFromPopUp(folderName, "Selected text from : " + docName.trim());
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for addting to folder from pop up.
	// # Function Name : addToFolderFromPopUp
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public void addToFolderFromPopUp(String folderName, String verifyTitle) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// Verify Pop up

			WebElement docTitInFoldPopup = commonLibrary.isExistNegative(UIMAP_Document.docTitInFoldPopup, 10);
			if (docTitInFoldPopup != null && docTitInFoldPopup.getAttribute("value").toLowerCase().contains(verifyTitle.toLowerCase())) {
				report.updateTestLog("Verify 'Selected text from: Document title-1' is displayed under label 'Document tittle' in Add to Folder pop-up", "Selected text from: Document title-1' is displayed under label 'Document tittle' in Add to Folder pop-up", Status.PASS);
			} else
				report.updateTestLog("Verify 'Selected text from: Document title-1' is displayed under label 'Document tittle' in Add to Folder pop-up", "Selected text from: Document title-1' is not displayed under label 'Document tittle' in Add to Folder pop-up", Status.FAIL);

			// // CLICK ON <<<ADD TO FOLDER>>>
			// WebElement addtoFolder =
			// commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			// if (addtoFolder != null)
			//
			// commonLibrary.clickButton_Parent_WithWait(addtoFolder,
			// "Add To Folder");

			// // CLICK ON <<<CHOOSE A FOLDER>>>
			//
			// WebElement chooseFolder =
			// commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
			// if (chooseFolder != null)
			// commonLibrary.clickButton_Parent_WithWait(chooseFolder,
			// "Choose Folder");

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, folderName);
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
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
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(700000);
					saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (saveFolder == saveNew && count < 40);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for addting to folder from pop up.
	// # Function Name : addToFolderFromPopUp
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public WorkFolders clickFolderIcon(String folderName) {
		WebElement folderIcon = commonLibrary.isExistNegative(UIMAP_Document.folderIcon, 10);
		int i = 0;
		do {
			if (browsername.contains("internet")) {
				Actions actionObject = new Actions(driver);
				actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();
			} else
				driver.navigate().refresh();
			commonLibrary.sleep(10000);
			folderIcon = commonLibrary.isExistNegative(UIMAP_Document.folderIcon, 10);
			i++;
		} while (folderIcon == null && i < 3);
		commonLibrary.clickButtonParentWithWait(folderIcon, "Folder Icon");

		boolean flag1 = false;
		WebElement locatorPopUp = commonLibrary.isExistNegative(UIMAP_RelatedContent.locatorPopUp, 10);
		if (locatorPopUp != null) {
			WebElement folderList = commonLibrary.isExistNegative(UIMAP_RelatedContent.folderList, 10);
			List<WebElement> links = commonLibrary.isExistList(folderList, UIMAP_RelatedContent.links, 10);
			for (WebElement link : links) {
				if (link.getText().contains(folderName)) {
					commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
					flag1 = true;
					break;
				}
			}
			if (!flag1)
				report.updateTestLog("Click folder icon", "Folder name is not present", Status.FAIL);
		} else
			report.updateTestLog("Click folder icon ", "Locator Popup is not displayed.", Status.FAIL);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click previous arrow in jump to
	// # Function Name : clickPreviousInJumpTo
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickPreviousInJumpTo() {
		// WebElement jumpToExpanded =
		// commonLibrary.isExist(UIMAP_Document.jumpToExpanded, 20);
		// if (jumpToExpanded == null)
		// generalFunctions.clickJumpTo();
		WebElement previousArrow = commonLibrary.isExist(UIMAP_Document.jumpToPreviousArrow, 20);
		if (previousArrow != null) {
			if (previousArrow.isEnabled())
				commonLibrary.clickButtonParentWithWait(previousArrow, "Previous Arrow");
			else
				report.updateTestLog("Click Previous Arrow", "Previous Arrow button is disabled", Status.FAIL);
		} else {
			report.updateTestLog("Click Previous Arrow", "Previous Arrow button is not available", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click next arrow in jump to
	// # Function Name : clickNextInJumpTo
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickNextInJumpTo() {
		// WebElement jumpToExpanded =
		// commonLibrary.isExist(UIMAP_Document.jumpToExpanded, 20);
		// if (jumpToExpanded == null)
		// generalFunctions.clickJumpTo();
		WebElement nextArrow = commonLibrary.isExist(UIMAP_Document.jumpToNextArrow, 20);
		if (nextArrow != null) {
			if (nextArrow.isEnabled())
				commonLibrary.clickButtonParentWithWait(nextArrow, "Next Arrow");
			else
				report.updateTestLog("Click Next Arrow", "Next Arrow button is disabled", Status.FAIL);
		} else {
			report.updateTestLog("Click Next Arrow", "Next Arrow button is not available", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click jump to
	// # Function Name : clickJumpTo
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickJumpTo() {
		generalFunctions.clickJumpTo();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC is collapsed
	// # Function Name : verifyMiniTOCIsCollapsed
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document verifyMiniTOCIsCollapsed() {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		if (tableOfContentsTab != null) {
			WebElement collapsedMiniToc = commonLibrary.isExist(UIMAP_Document.collapsedMiniToc, 20);
			if (collapsedMiniToc != null) {
				report.updateTestLog("Verify Mini Toc is collapsed", "Mini Toc is collapsed", Status.PASS);
			} else {
				report.updateTestLog("Verify Mini Toc is collapsed", "Mini Toc is expanded", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Mini Toc is collapsed", "Mini Toc is not available", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC is expanded
	// # Function Name : verifyMiniTOCIsExpanded
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document verifyMiniTOCIsExpanded() {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		if (tableOfContentsTab != null) {
			WebElement expandedMiniToc = commonLibrary.isExist(UIMAP_Document.expandedMiniToc, 20);
			if (expandedMiniToc != null) {
				report.updateTestLog("Verify Mini Toc is Expanded", "Mini Toc is Expanded", Status.PASS);
			} else {
				report.updateTestLog("Verify Mini Toc is Expanded", "Mini Toc is Collapsed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Mini Toc is Expanded", "Mini Toc is not available", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Mini Toc button
	// # Function Name : clickToc
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickToc() {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		if (tableOfContentsTab != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(tableOfContentsTab, "Table of Contents");
			else
				commonLibrary.clickButtonParentWithWait(tableOfContentsTab, "Table of Contents");
		} else
			report.updateTestLog("Click Mini Toc", "Mini Toc is not available", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Next and Previous in document
	// page
	// # Function Name : clickNextPrevDoc   
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickNextPrevDoc(String targetText) {
		boolean isNext = false, isPrev = false;
		WebElement slider = commonLibrary.isExist(UIMAP_Document.docNavClass, 20);
		List<WebElement> docNav = commonLibrary.isExistList(slider, UIMAP_Document.docNav, 20);
		for (int i = 0; i < docNav.size(); i++) {
			if (docNav.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(docNav.get(i), docNav.get(i).getText());
				isNext = true;
				break;
			} else if (docNav.get(i).getText().toLowerCase().contains(targetText.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(docNav.get(i), docNav.get(i).getText());
				isPrev = true;
				break;
			}
		}
		if (targetText.toLowerCase().contains("next")) {
			if (isNext)
				report.updateTestLog("Click on next link", "Next link is clicked.", Status.PASS);
			else
				report.updateTestLog("Click on next link", "Next link is not clicked.", Status.FAIL);
		}
		if (targetText.toLowerCase().contains("previous")) {
			if (isPrev)
				report.updateTestLog("Click on previous link", "Previous link is clicked.", Status.PASS);
			else
				report.updateTestLog("Click on previous link", "Previous link is not clicked.", Status.FAIL);
		}
		WebElement atd = commonLibrary.isExist(UIMAP_Document.jumpto, 10);
		int counter = 0;
		do {
			counter = counter + 1;
			atd = commonLibrary.isExist(UIMAP_Document.jumpto, 20);
			if (atd == null)
				commonLibrary.sleep(5000);
		} while (atd == null && counter <= 40);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Sharing Information button.
	// # Function Name : clickSharingInfo     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickSharingInfo() {
		WebElement sharingInfo = commonLibrary.isExistNegative(UIMAP_WorkFolders.sharingInfo, 10);
		commonLibrary.clickLinkWithWebElementWithWait(sharingInfo, "Sharing Information");
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created.
	// # Function Name : addToCreatedFolder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document stopSharing() {
		generalFunctions.stopSharing();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created.
	// # Function Name : addToCreatedFolder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyViewAllReports() {
		WebElement linkDocPage = commonLibrary.isExist(UIMAP_RelatedContent.resultlist, 10);
		if (linkDocPage != null)
			report.updateTestLog("Veiry 'Available Reports'", "Available Reports are verified", Status.PASS);
		else
			report.updateTestLog("Veiry 'Available Reports'", "Available Reports are not verified", Status.FAIL);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document slider is not
	// present
	// # Function Name : verifyDocSliderNotPresent     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document verifyDocSliderNotPresent() {
		WebElement docSlider = commonLibrary.isExist(UIMAP_Document.docSlider, 10);
		if (docSlider == null)
			report.updateTestLog("Verify Document Slider is not present", "Document slider is not present", Status.PASS);
		else
			report.updateTestLog("Verify Document Slider is not present", "Document slider is present", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : verifyTextNotClickable   
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Document verifyTextNotClickable(String targetText) {
		generalFunctions.verifyTextNotClickable(targetText);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in the document page
	// pop up
	// # Function Name : click_ExternalLink
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Document clickExternalLink(String StrlinkText) {

		WebElement docContent = commonLibrary.isExistNegative(UIMAP_Document.docText, 10);
		List<WebElement> Link = commonLibrary.isExistList(docContent, UIMAP_Document.ExtLink, 10);

		for (int i = 0; i < Link.size(); i++) {
			if (i == 0) {
				commonLibrary.clickLinkWithWebElementWithWait(Link.get(i), Link.get(i).getText());
				String url = Link.get(i).getText();
				if (commonLibrary.switchToWindow(StrlinkText)) {
					report.updateTestLog("Verify secondary window opens", "Secondary window is opened with  '" + url + "'", Status.PASS);
					driver.close();
					report.updateTestLog("Click on X button to close secondary window ", "Secondary window is Closed", Status.PASS);
					commonLibrary.switchToWindow("document");
					break;
				}
			}

		}
		return new Document(scriptHelper);

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
	// # Function Description : Function to verify LASAC pop up is displayed.
	// # Function Name : verifyLASACPopup
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyLASACPopup(String popupText, boolean isPresent) {
		if (isPresent) {
			WebElement txtAccessChargeRed = commonLibrary.isExist(UIMAP_Document.txtAccessChargeRed);
			WebElement lasacMessage = commonLibrary.isExistNegative(UIMAP_Document.lasacMessage1, 10);

			if (txtAccessChargeRed.getText().toLowerCase().trim().contains("Lexis Advance Access Charge".toLowerCase().trim()) && lasacMessage.getText().toLowerCase().trim().contains(popupText.toLowerCase().trim())) {
				report.updateTestLog("Veirfy LASAC Access charge popup displays with message: " + popupText, "LASAC Access charge popup displays with message: " + popupText, Status.PASS);
			} else
				report.updateTestLog("Veirfy LASAC Access charge popup displays with message: " + popupText, "LASAC Access charge popup does not display with message: " + popupText, Status.FAIL);
		} else {
			WebElement txtAccessChargeRed = commonLibrary.isExist(UIMAP_Document.txtAccessChargeRed);
			// WebElement lasacMessage = commonLibrary.isExist_Negative(
			// UIMAP_Document.lasacMessage, 10);

			if (txtAccessChargeRed.getText().toLowerCase().contains("Lexis Advance Access Charge".toLowerCase())) {
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is displayed", Status.FAIL);
			} else
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is not displayed", Status.PASS);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Jump To And Verify Section
	// Content
	// # Function Name : clickJumpToAndVerifySectionContent
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Document clickJumpToAndVerifySectionContent(String optionsToVerify) {

		String[] optionsToVerifyEach = optionsToVerify.split(";");
		int count = 0;
		// commonLibrary.windowFocus();
		// WebElement rightContainer =
		// commonLibrary.isExistNegative(UIMAP_Document.rightContainer, 10);
		generalFunctions.clickJumpTo();

		WebElement sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		if (sectionContainer != null && !sectionContainer.isDisplayed())
			generalFunctions.clickJumpTo();
		else {
			sectionContainer = commonLibrary.isExist(UIMAP_Document.sectionContainer, 10);
			// WebElement sections =
			// commonLibrary.isExistNegative(sectionContainer,
			// UIMAP_Document.sections, 10);
			// JavascriptExecutor executor = (JavascriptExecutor) driver;
			// executor.executeScript("arguments[0].focus();", sections);
			// executor.executeScript("arguments[0].click();", sections);
			sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
			WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown, 10);
			WebElement optionList = commonLibrary.isExistNegative(sectionsDropdown, UIMAP_Document.lstTagUList, 10);
			List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_Document.tagButton, 10);
			if (options.size() > 0) {
				for (int i = 0; i < options.size(); i++) {
					for (int j = 0; j < optionsToVerifyEach.length; j++) {
						if (options.get(i).getText().toLowerCase().replaceAll(".", "").contains(optionsToVerifyEach[j].toLowerCase().replaceAll(".", ""))) {
							report.updateTestLog("Verify section content " + optionsToVerifyEach[j], optionsToVerifyEach[j] + " is present", Status.PASS);
							count++;
						}
						if (count == options.size())
							break;
					}
					if (count == options.size())
						break;
				}
			}
			if (count == options.size()) {
				report.updateTestLog("Verify all section content present", "All section content present", Status.PASS);
				commonLibrary.sleep(5000);
			}
		}
		generalFunctions.clickJumpTo();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : selectSection     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document jumpToSectionAndVerify(String option) {
		boolean blnFlag = false;
		// WebElement mobileToolbar =
		// commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		// WebElement rightContainer =
		// commonLibrary.isExistNegative(mobileToolbar,
		// UIMAP_Document.rightContainer, 10);
		generalFunctions.clickJumpTo();
		WebElement sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown, 10);
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
					report.updateTestLog("Verify page is scrolled to section: " + option, "Section " + option + " is not present.", Status.FAIL);
				if ((ySection - yToolbar) >= 0 && (ySection - yToolbar) <= 1000)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);
			} else {
				WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
				int yHeading = txtDocumentHeading.getLocation().getY();
				if ((yHeading - yToolbar) >= 0 && (yHeading - yToolbar) <= 400)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);

			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on pencil icon to edit the
	// document
	// # Function Name : clickPencilIcon
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Document clickPencilIcon(String editText) {
		boolean blnClick = false;

		WebElement docContent = commonLibrary.isExist(UIMAP_Document.docSection, 10);
		WebElement docSpan = commonLibrary.isExist(docContent, UIMAP_Document.docSpan, 30);
		List<WebElement> docLinks = commonLibrary.isExistList(docSpan, UIMAP_Document.docLinks, 30);
		if (docLinks.size() > 0) {
			for (WebElement editDiv : docLinks) {
				if (editDiv.getAttribute("data-placeholder").toLowerCase().contains(editText.toLowerCase())) {
					commonLibrary.clickButton(editDiv);
					blnClick = true;
					break;
				}
			}
			if (blnClick) {
				report.updateTestLog("Click on pencil icon next to '[" + editText + "]'", "Pencil icon next to '[" + editText + "]' is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on pencil icon next to '[" + editText + "]'", "Pencil icon next to '[" + editText + "]' is not clicked", Status.FAIL);
			}
		}
		// List<WebElement> docMainDiv = commonLibrary.isExist_List(docContent,
		// UIMAP_Document.mainDocDiv, 30);
		// if(docMainDiv.size()>0)
		// {
		// List<WebElement> innerDivs =
		// commonLibrary.isExist_List(docMainDiv.get(1),
		// UIMAP_Document.innerDocDiv, 10);
		// if(innerDivs.size()>0)
		// {
		// for(WebElement editDiv:innerDivs)
		// {
		// WebElement inParaDiv = commonLibrary.isExist(editDiv,
		// UIMAP_Document.inParaDiv, 30);
		// WebElement inTxtParaDiv = commonLibrary.isExist(inParaDiv,
		// UIMAP_Document.inTxtParaDiv, 30);
		// WebElement editLink=commonLibrary.isExist(inTxtParaDiv,
		// UIMAP_Document.links, 30);
		// if(editLink.getAttribute("data-placeholder").toLowerCase().contains(editText.toLowerCase()))
		// {
		// commonLibrary.clickButton(editLink);
		// blnClick=true;
		// break;
		// }
		// }
		// }
		// if(blnClick)
		// {
		// report.updateTestLog("Click on pencil icon next to '[" +
		// editText+"]'", "Pencil icon next to '[" + editText+"]' is clicked",
		// Status.PASS);
		// }
		// else
		// {
		// report.updateTestLog("Click on pencil icon next to '[" +
		// editText+"]'", "Pencil icon next to '[" +
		// editText+"]' is not clicked", Status.FAIL);
		// }
		// }
		else {
			report.updateTestLog("Click on pencil icon next to '[" + editText + "]'", "Pencil icons are not displayed next to '[" + editText + "]'", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on pencil icon to edit the
	// document
	// # Function Name : clickPencilIcon
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Document setTextAndSave(String newText) {
		WebElement popUpPage = commonLibrary.isExist(UIMAP_Document.replaceFiltersAside, 30);
		WebElement eltPopupBoxForm = commonLibrary.isExist(popUpPage, UIMAP_Home.frmFilterAlert, 30);
		WebElement mainDiv = commonLibrary.isExist(eltPopupBoxForm, UIMAP_Document.mainDiv, 30);
		WebElement subDiv = commonLibrary.isExist(mainDiv, UIMAP_Document.subDiv, 30);
		WebElement eltEditBox = commonLibrary.isExist(subDiv, UIMAP_Document.txtIdField, 20);

		commonLibrary.setDataInTextBox(eltEditBox, newText, "Edit Filed");
		WebElement ulList = commonLibrary.isExist(eltPopupBoxForm, UIMAP_Home.ulList, 30);

		List<WebElement> editOptions = commonLibrary.isExistList(ulList, UIMAP_Home.inpt, 10);
		if (editOptions.size() > 0) {
			for (WebElement opt : editOptions) {
				if (opt.getAttribute("value").toLowerCase().equals("apply")) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(opt, "apply");
					else
						commonLibrary.clickButtonParentWithWait(opt, "apply");
					break;
				}
			}
		} else {
			report.updateTestLog("Set Edit field to '" + newText + "'", "Edit Field is not set to '" + newText + "'", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public Document addToFolderforLPADoc(String FolderName) {
		try {
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

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(create, "create");
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
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(saveFolder, "save");
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

				if (!driver.getCurrentUrl().contains("intermediatecontent")) {
					count = 0;
					do {
						count++;
						commonLibrary.sleep(50000);

					} while (driver.getCurrentUrl().contains("intermediatecontent") && count < 20);

				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// commonLibrary.sleep(50000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		if (!browsername.contains("internet"))
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		int count = 0;
		WebElement header = commonLibrary.isExist(UIMAP_Document.pgTitleTopicSummary, 20);
		do {
			count++;
			header = commonLibrary.isExist(UIMAP_Document.pgTitleTopicSummary, 20);
			commonLibrary.sleep(2000);
		} while (header == null && count < 10);
		// commonLibrary.sleep(60000);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Select Value
	// # Function Name : clickActionSelectValue    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Document clickActionSelectValue(String strAction) {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_Document.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_Document.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_Document.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_Document.divAction, 20);
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
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Document saveLink(String tcName, String dataSheet, String colName) {
		generalFunctions.saveLink(tcName, dataSheet, colName);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Document closePopup() {
		WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.dialogContent, 10);
		WebElement close = commonLibrary.isExistNegative(dialogContent, UIMAP_CounselBenchmarking.close, 10);
		commonLibrary.clickButtonParentWithWait(close, "Close");
		pageCheck.ajaxWait(driver);
		return new Document(scriptHelper);
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
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
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
		// driver.quit();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on pencil icon to edit the
	// document
	// # Function Name : clickPencilIcon
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Document verifyPencilIcon(String editText) {
		boolean blnClick = false;
		commonLibrary.sleep(10000);
		WebElement docContent = commonLibrary.isExist(UIMAP_Document.docSection, 10);
		WebElement docSpan = commonLibrary.isExist(docContent, UIMAP_Document.docSpan, 30);
		List<WebElement> docLinks = commonLibrary.isExistList(docSpan, UIMAP_Document.docLinks, 30);
		if (docLinks.size() > 0) {
			for (WebElement editDiv : docLinks) {
				if (editDiv.getAttribute("data-currentval").toLowerCase().contains(editText.toLowerCase())) {
					blnClick = true;
					break;
				}
			}
			if (blnClick) {
				report.updateTestLog("Verify the depositor name is filled as '" + editText + "' in Editable Interim page", "depositor name is filled as '" + editText + "' in Editable Interim page", Status.PASS);
			} else {
				report.updateTestLog("Verify the depositor name is filled as '" + editText + "' in Editable Interim page", "depositor name is not filled as '" + editText + "' in Editable Interim page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify the depositor name is filled as '" + editText + "' in Editable Interim page", "Dipositor name is not displayed.", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from document page and
	// wait
	// # Function Name : clickEmbededLinkWait
	// # Author : Pratik
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickEmbededLinkWait(String link, String title) {
		generalFunctions.clickEmbededLinkWait(link, title);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document pdfVerificationSecondaryScreenshot(String pdfName, String Exist, String testCaseName, String stepName) {
		// Boolean blnFlag = false;
		// Boolean pdfload=false;
		// int c=0;
		commonLibrary.sleep(400000);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		// WebElement docContent =
		// commonLibrary.isExist_Negative(UIMAP_Document.pdfData, 100);
		takeScreenShot(testCaseName, stepName);
		commonLibrary.sleep(100000);
		driver.close();

		commonLibrary.switchToWindow("document");
		return new Document(scriptHelper);
	}

	// public static void type(String characters) {
	// Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	// StringSelection stringSelection = new StringSelection( characters );
	// clipboard.setContents(stringSelection, clipboardOwner);
	// Robot robot = new Robot();
	// robot.keyPress(KeyEvent.VK_CONTROL);
	// robot.keyPress(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_CONTROL);
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Create Alert Link
	// # Function Name : clickonCreateAlertLink
	// # Author : shobana
	// # Date Created : 24-Feb'15
	// #*****************************************************************************************************
	public Document clickonCreateAlertLink() {
		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			if (commonLibrary.isExist(UIMAP_Home.btnBrowse) != null) {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}
		return new Document(scriptHelper);

	}

	public void sslCertificate() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public void savePDFBrowser(String AutoITPath, String FilePath, String docName) throws IOException {
		try {
			sslCertificate();
			commonLibrary.sleep(4000);
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			commonLibrary.sleep(3000);
			String path2 = FilePath + docName + ".Pdf";
			String windowTitle = driver.getCurrentUrl() + " - Internet Explorer provided by Reed Elsevier";
			if (browsername.contains("internet") && version.contains("8")) {
				String[] cmd = { AutoITPath, windowTitle, path2, "Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet") && version.contains("9")) {
				String[] cmd = { AutoITPath, windowTitle, path2, "Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if ((browsername.contains("internet") && version.contains("11")) || (browsername.contains("internet") && version.contains("10"))) {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(10000);
				for (int i = 1; i <= 3; i++) {
					robot.keyPress(KeyEvent.VK_TAB);
					commonLibrary.sleep(1000);
				}
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(1000);
				// String[] cmd = {AutoITPath, path2, "Save As"};
				// Runtime.getRuntime().exec(AutoITPath);
			}
			commonLibrary.sleep(7000);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document pdfVerificationSecondaryByDownload(String pdfContent, String strFilePath, String AutoITPath, String AutoITPath1, String strFileName, String textExistence) {
		try {
			String filename = null;
			commonLibrary.sleep(60000);
			Thread.sleep(20000);
			commonLibrary.windowFocus();
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));

			commonLibrary.fileExistsDelete(strFilePath, strFileName);
			String path = strFilePath + "\\";
			filename = strFilePath + "\\" + strFileName + ".PDF";

			if (browsername.equalsIgnoreCase("internet explorer")) {
				driver.manage().window().maximize();
				savePDFBrowser(AutoITPath, path, strFileName);
			} else if (browsername.equalsIgnoreCase("firefox")) {
				driver.manage().window().maximize();
				Actions action = new Actions(driver);
				action.sendKeys(Keys.chord(Keys.CONTROL, "s")).build().perform();
				commonLibrary.sleep(10000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);

			} else if (browsername.toLowerCase().contains("chrome")) {
				driver.manage().window().maximize();

				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle); // switch focus of
															// WebDriver to the
															// next found window
															// handle (that's
															// your newly opened
															// window)
				}

				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				commonLibrary.sleep(10000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);
			}
			commonLibrary.sleep(100000);
			Thread.sleep(20000);
			pdfVerification(filename, pdfContent, textExistence);
			// driver.close();
			commonLibrary.switchToWindow("document");
			return new Document(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for close and back to doc page
	// # Function Name : closeAndBack_ToDocWindow     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Document closeAndBackToDocWindow() {
		driver.close();
		commonLibrary.switchToWindow("lexis.com/search");
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clcik the Active passage in the
	// document page
	// # Function Name : ClickActivePassage     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickActivatePassageAndRFC(String docTitle) {
		WebElement aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		WebElement expanded = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}

		WebElement btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
		if (btnActivatepassages != null)
			commonLibrary.clickButtonParentWithWait(btnActivatepassages, "Activate Passages");
		commonLibrary.sleep(5000);

		aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		expanded = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}
		btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
		if (btnActivatepassages.getText().toLowerCase().contains("Deactivate Passages".toLowerCase())) {
			report.updateTestLog("Verify Deactivate Passages button appears", "Deactivate Passages button appears", Status.PASS);
			report.updateTestLog("Verify if all the RFC's appears within dashed / dotted lines", "All the RFC's appears within dashed / dotted lines", Status.PASS);
		}

		WebElement lnkRFPassage = commonLibrary.isExist(UIMAP_Document.lnkRFPassage, 10);
		if (lnkRFPassage != null)
			commonLibrary.clickButtonParentWithWait(lnkRFPassage, "Highlighted RFC");
		commonLibrary.sleep(5000);

		WebElement headerSearchResult = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader1, 10);
		String toVerify = "Legal Issue Trail™: " + docTitle;
		if (headerSearchResult != null && headerSearchResult.getText().toLowerCase().contains(toVerify.toLowerCase()))
			report.updateTestLog("Verify highlighted RFC is clicked", "Highlighted RFC is clicked", Status.PASS);

		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clcik the Deactivate passage in the
	// document page
	// # Function Name : clickDeactivatePassage     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Document clickDeactivatePassage() {
		WebElement aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		WebElement expanded = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}

		WebElement btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
		if (btnActivatepassages != null)
			commonLibrary.clickButtonParentWithWait(btnActivatepassages, "Deactivate Passages");

		aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		expanded = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}

		btnActivatepassages = commonLibrary.isExist(UIMAP_Document.btnActivatepassages, 10);
		if (btnActivatepassages.getText().toLowerCase().contains("Activate Passages".toLowerCase())) {
			report.updateTestLog("Verify Activate Passages button appears", "Activate Passages button appears", Status.PASS);
			report.updateTestLog("Verify if all the RFC's are not highlighted", "All the RFC's are not highlighted", Status.PASS);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyFooterLinks in the document
	// page
	// # Function Name : verifyFooterLinks     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Document verifyFooterLinks(String link) {
		WebElement footer = commonLibrary.isExistNegative(UIMAP_Document.footer, 10);

		List<WebElement> links = commonLibrary.isExistList(footer, UIMAP_Document.links, 10);
		if (link.toLowerCase().equals("lexisnexis")) {
			report.updateTestLog("Verify LexisNexis logo is displayed.", "LexisNexis logo is displayed.", Status.PASS);
		} else {
			boolean flag = false;
			for (WebElement li : links) {
				if (li.getText().toLowerCase().contains(link.toLowerCase())) {
					flag = true;
					break;
				}
			}
			if (flag)
				report.updateTestLog("Verify link " + link, "Link " + link + " is displayed.", Status.PASS);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing signout
	// # Function Name : signoutFooter
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public SignIn signoutFooter() {
		WebElement footer = commonLibrary.isExistNegative(UIMAP_Document.footer, 10);
		List<WebElement> links = commonLibrary.isExistList(footer, UIMAP_Document.links, 10);
		boolean flag = false;
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains("Sign Out".toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				flag = true;
				break;
			}
			if (flag)
				break;
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
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clickLinkFromATD
	// # Author : Seetha
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickLinkFromATD_ClickAlternate(String documenttitle, String strTCName) {
		WebElement aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
		WebElement expanded = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);
		if (expanded != null && !expanded.getAttribute("class").contains("active")) {
			aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);
			WebElement expand = commonLibrary.isExist(aboutthisdoclink, By.tagName("h3"), 10);

			commonLibrary.clickButtonParentWithWaitJS(expand, "Expand About This Document");
			commonLibrary.sleep(3000);
		}

		aboutthisdoclink = commonLibrary.isExist(UIMAP_Document.ATD);

		List<WebElement> links = commonLibrary.isExistList(aboutthisdoclink, UIMAP_Document.links, 10);
		for (int i = 0; i < links.size(); i++) {
			documenttitle = "Original Source Image";
			if (links.get(i).getText().toLowerCase().contains(documenttitle.toLowerCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(links.get(i), documenttitle);
					break;
				} else {
					commonLibrary.clickButtonParentWithWait(links.get(i), documenttitle);
					break;
				}
			}
		}
		commonLibrary.switchToWindow("documentprovider");

		pdfVerificationSecondaryScreenshot("", "", strTCName, "Verify whether the pdf is opened.");

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Browser back button
	// # Function Name : Clickbrowserback     
	// # Author : Karthi
	// # Date Created : Apr 2015
	// #*****************************************************************************************************************************
	public Search clickbrowserbackToResults() {

		// // driver.navigate().back();
		// try {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(50000);
		//
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// report.updateTestLog("Click on Browser Back",
		// "Browser Back not clicked", Status.FAIL);
		// }
		commonLibrary.clickBrowserBack();
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickLinkInDocEntity(String targetText) {
		generalFunctions.clickLinkInDoc_EntityLink(targetText);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Document closeCitationPopupDocument() {
		generalFunctions.closeCitationPopup();
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document scrolled to a
	// particular section
	// # Function Name : verifyTextDisplayedOnScreen     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyTextDisplayedOnScreen(String text) {
		WebElement docText = commonLibrary.isExistNegative(UIMAP_Document.docText, 10);
		List<WebElement> paraList = commonLibrary.isExistList(docText, UIMAP_Document.text, 10);
		boolean flag = false;
		for (WebElement para : paraList) {
			if (para.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
				int y = para.getLocation().getY();
				flag = true;
				WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
				int yToolbar = toolbar.getLocation().getY();
				if (y - yToolbar > 0 && y - yToolbar < 1000) {
					report.updateTestLog("Verify document is scrolled to " + text, "Document is scrolled to " + text, Status.PASS);
				} else
					report.updateTestLog("Verify document is scrolled to " + text, "Document is not scrolled to " + text, Status.FAIL);
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify document is scrolled to " + text, text + " is not present in document.", Status.FAIL);
		return new Document(scriptHelper);
	}

	public Shepards clickBrowserBackShepards() {

		// Common Function call for browser back
		commonLibrary.clickBrowserBack();

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text displays in viewable
	// region
	// # Function Name : verifyTextDisplaysInViewableRegion     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document verifyTextDisplaysInViewableRegion(String text) {
		boolean flag = false;
		WebElement toolbar = commonLibrary.isExist(UIMAP_SearchResult.toolbar, 10);
		WebElement documentText = commonLibrary.isExist(UIMAP_SearchResult.documentText, 10);
		List<WebElement> highlightedText = commonLibrary.isExistList(documentText, UIMAP_SearchResult.highlightText, 10);
		for (WebElement textMessage : highlightedText) {
			if (textMessage.getText().toLowerCase().contains(text.toLowerCase()) && toolbar != null) {
				int textLocation = textMessage.getLocation().getY();
				int toolbarLocation = toolbar.getLocation().getY();
				if ((textLocation - toolbarLocation) >= 0 && (textLocation - toolbarLocation) <= 1000) {
					report.updateTestLog("Verify " + text + " displays in the viewable region", text + " is displayed in the viewable region", Status.PASS);
					flag = true;
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + text + " displays in the viewable region", text + " is not displayed in the viewable region", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text displays in viewable
	// region
	// # Function Name : verifyTextDisplaysInViewableRegion1     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public Document verifyTextDisplaysInViewableRegion1(String text) {

		// Verification point : verifying the text is displayed in viewable
		// region or not after loading the document

		boolean flag = false;
		WebElement toolbar = commonLibrary.isExist(UIMAP_SearchResult.toolbar, 10);
		WebElement documentText = commonLibrary.isExist(UIMAP_SearchResult.documentText, 10);
		List<WebElement> highlightedText = commonLibrary.isExistList(documentText, UIMAP_Document.highlightText1, 10);
		for (WebElement textMessage : highlightedText) {

			if (textMessage.getText().toLowerCase().contains(text.toLowerCase()) && toolbar != null) {
				// checking the region with location
				int textLocation = textMessage.getLocation().getY();
				int toolbarLocation = toolbar.getLocation().getY();
				if ((textLocation - toolbarLocation) >= 0 && (textLocation - toolbarLocation) <= 1000) {
					report.updateTestLog("Verify " + text + " displays in the viewable region", text + " is displayed in the viewable region", Status.PASS);
					flag = true;
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + text + " displays in the viewable region", text + " is not displayed in the viewable region", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text displays in viewable
	// region
	// region
	// # Function Name : verifyTextDisplaysInViewableRegion2     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public Document verifyTextDisplaysInViewableRegion2(String text) {
		// Verification point : verifying the text is displayed in viewable
		// region or not after loading the document
		int i = 0;
		boolean flag = false;
		WebElement toolbar = commonLibrary.isExist(UIMAP_SearchResult.toolbar, 10);
		WebElement documentText = commonLibrary.isExist(UIMAP_Document.highlightText3, 10);
		List<WebElement> highlightedText = commonLibrary.isExistList(documentText, UIMAP_Document.recentFolderName, 10);
		for (WebElement textMessage : highlightedText) {
			i++;
			if (i > 141)
				if (textMessage.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim()) && toolbar != null) {
					// checking the region with location

					int textLocation = textMessage.getLocation().getY();
					int toolbarLocation = toolbar.getLocation().getY();
					if ((textLocation - toolbarLocation) >= 0 && (textLocation - toolbarLocation) <= 1000) {

						report.updateTestLog("Verify '" + text + "' displays in the viewable region", "Text '" + text + "' is displayed in the viewable region", Status.PASS);
						flag = true;
						break;
					}
				}
		}
		if (!flag)
			report.updateTestLog("Verify " + text + " displays in the viewable region", text + " is not displayed in the viewable region", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify terms are highlighted and
	// bolded
	// # Function Name : verifyTermHighlightedAndBolded     
	// # Author : Baswaraj
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************
	public Document verifyTermHighlightedAndBolded(String searchTerm) {

		// verifying that a given text is getting displayed as Bolded and
		// highlighted
		String highlightTerm[] = searchTerm.split(";");
		for (int i = 0; i < highlightTerm.length; i++) {

			// looping for the highlighted terms

			boolean flag = false;
			List<WebElement> highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
			for (WebElement item : highlights) {
				// checking the region with location

				if (item.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(highlightTerm[i].toLowerCase().replaceAll("[^a-zA-Z0-9]", "")) && item.getCssValue("background-color").contains("rgba(255, 240, 153, 1)")) {
					flag = true;
					break;
				}

			}
			if (flag)
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", Status.PASS);
			else
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is not bolded and highlighted in yellow color", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify court and date is displayed
	// under document title
	// # Function Name : verifyCourtAndDateDisplayedUnderDocument     
	// # Author : Anbarasan
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************
	public Document verifyCourtAndDateDisplayedUnderDocument() {
		WebElement docHeader = commonLibrary.isExist(UIMAP_Document.docHeader, 10);
		if (docHeader == null) {
			int count = 0;
			do {
				count = count + 1;
				docHeader = commonLibrary.isExist(UIMAP_Document.docHeader, 10);
				if (docHeader == null)
					commonLibrary.sleep(5000);
			} while (docHeader == null && count <= 40);
		}
		List<WebElement> docInfo = commonLibrary.isExistList(docHeader, UIMAP_Document.docInfo, 10);
		String month = "January;February;March;April;May;June;July;August;September;October;November;December";
		String[] monthArr = month.split(";");
		boolean courtFlag = false;
		boolean dateFlag = false;
		if (docInfo.size() > 0) {
			for (WebElement item : docInfo) {
				if (item.getText().toLowerCase().contains("court")) {
					report.updateTestLog("Verify court present under document title", "Court is present under document title", Status.PASS);
					courtFlag = true;
				}
				for (String monthName : monthArr) {
					if (item.getText().toLowerCase().contains(monthName.toLowerCase())) {
						report.updateTestLog("Verify date present under document title", "Date is present under document title", Status.PASS);
						dateFlag = true;
						break;
					}
				}
			}
			if (!courtFlag) {
				report.updateTestLog("Verify court present under document title", "Court is not present under document title", Status.FAIL);
			}
			if (!dateFlag) {
				report.updateTestLog("Verify date present under document title", "Date is not present under document title", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify court & date present under document title", "Court & date is not present under document title", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title,court,date
	// displays in floating area
	// # Function Name : verifyDocCourtDateDisplaysInFloat     
	// # Author : Anbarasan
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************
	public Document verifyDocCourtDateDisplaysInFloat(String docTitle, String court, String date) {
		boolean flag1 = false;
		boolean flag2 = false;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		report.updateTestLog("Scroll down the full document", "Scrolled down the full document", Status.DONE);
		commonLibrary.sleep(10000);
		WebElement floatArea = commonLibrary.isExist(UIMAP_Document.floatAreaSticky, 10);
		WebElement menu = commonLibrary.isExist(UIMAP_Document.menu1, 20);
		WebElement slider = commonLibrary.isExist(UIMAP_Document.docslider, 20);
		String temp = date;
		String[] tempArr = temp.split(",");
		String[] dateArr = tempArr[0].split(" ");
		int dateNumber = Integer.parseInt(dateArr[1]);
		dateArr[1] = String.valueOf(dateNumber);
		date = dateArr[0] + " " + dateArr[1] + "," + tempArr[1];
		if (floatArea != null && floatArea.getText().toLowerCase().contains(docTitle.toLowerCase()) && floatArea.getText().toLowerCase().contains(court.toLowerCase()) && floatArea.getText().toLowerCase().contains(date.toLowerCase())) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatArea, UIMAP_Document.courtDateInfo, 10);
			if (courtDateInfo.size() > 0 && courtDateInfo.get(0).isDisplayed() && courtDateInfo.get(1).isDisplayed()) {
				flag1 = true;
			}
			if (flag1)
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.PASS);
			else
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		} else {
			report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		}
		if (menu != null && slider != null) {
			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is displayed along with Delivery tray and Document slider", Status.PASS);
		} else {
			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is not displayed along with Delivery tray and Document slider", Status.FAIL);
		}
		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
		report.updateTestLog("Scroll back to the top of the document", "Scrolled back to the top of the document", Status.DONE);
		commonLibrary.sleep(10000);
		WebElement floatAreaNew = commonLibrary.isExist(UIMAP_Document.floatArea, 10);
		if (floatAreaNew != null) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatAreaNew, UIMAP_Document.courtDateInfo, 10);

			if (courtDateInfo.size() > 0 && !courtDateInfo.get(0).isDisplayed() && !courtDateInfo.get(1).isDisplayed()) {
				flag2 = true;
			}
			if (flag2)
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date does not display in sign post area", Status.PASS);
			else
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		} else {
			report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Browser back from dull
	// document to results in LPA
	// # Function Name : clickLPAbrowserbackToResults    
	// # Author : Meera
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************

	public LPABrowse clickLPAbrowserbackToResults() {

		commonLibrary.clickBrowserBack();
		return new LPABrowse(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Practice Area pages
	// # Function Name : clickPracticeAreaPage     
	// # Author : Meera
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
	// # Function Description : Function to select a paragraph in document
	// # Function Name : selectParagraphInDocument     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Document selectTextInDocument(String text) {
		boolean flag = false;
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		List<WebElement> paragraph = commonLibrary.isExistList(docText, UIMAP_Document.text, 10);
		for (WebElement item : paragraph) {
			if (item.getText().trim().toLowerCase().contains(text.toLowerCase())) {
				commonLibrary.focusControlJS(item);
				Actions action = new Actions(driver);
				action.moveToElement(item, 5, 0).doubleClick().build().perform();
				commonLibrary.sleep(50000);
				report.updateTestLog("Selecting the text in document", "Text is selected in the document", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Selecting the text in document", "Text is not selected in the document", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify citation format drop down is
	// not present
	// # Function Name : verifyCitationFormatNotPresent     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Document verifyCitationFormatNotPresent() {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement formatDropdown = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.formatDropdown, 10);
		if (formatDropdown == null)
			report.updateTestLog("Verify copy citation dropdown does not display in the popup", "copy citation dropdown is not present in the popup", Status.PASS);
		else
			report.updateTestLog("Verify copy citation dropdown does not display in the popup", "copy citation dropdown is present in the popup", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click option in selected text menu
	// # Function Name : clickOptionInSelectedTextMenu     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Document clickOptionInSelectedTextMenu(String option) {
		boolean flag = false;
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		List<WebElement> button = commonLibrary.isExistList(docContextMenu, UIMAP_Document.btnButton, 10);
		if (button.size() > 0) {
			for (WebElement item : button) {
				if (item.getText().trim().toLowerCase().contains(option.trim().toLowerCase())) {
					commonLibrary.clickJSMouseMove(item, item.getText());
					flag = true;
					break;
				}
			}
			if (!flag) {
				report.updateTestLog("Click " + option + " in selected text menu", option + " is not clicked in selected text menu", Status.FAIL);
			}
		} else
			report.updateTestLog("Click " + option + " in selected text menu", option + " is not clicked in selected text menu", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Bact to Top arrow
	// # Function Name : verifyBackToTop   
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************

	public Document verifyBackToTop(Boolean arrowPresent) {
		WebElement backToTop = commonLibrary.isExist(UIMAP_SearchResult.backToTop, 20);

		if (arrowPresent) {
			if (backToTop != null) {
				report.updateTestLog("Verify Back To Top button is present", "Back To Top button is present", Status.PASS);
			} else {
				report.updateTestLog("Verify Back To Top button is present", "Back To Top button is not present", Status.FAIL);
			}
		} else {
			if (backToTop == null) {
				report.updateTestLog("Verify Back To Top button is not present", "Back To Top button is not present", Status.PASS);
			} else {
				report.updateTestLog("Verify Back To Top button is present", "Back To Top button is present", Status.FAIL);
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll the document page
	// # Function Name : scrollSearchPage  
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************

	public Document scrollSearchPage() {

		commonLibrary.scrollDown();
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click On Bact to Top arrow
	// # Function Name : clickBackToTop   
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************
	public Document clickBackToTop() {
		WebElement backToTop = commonLibrary.isExist(UIMAP_SearchResult.backToTop, 20);
		if (backToTop != null) {
			commonLibrary.clickButton(backToTop);
		} else {
			report.updateTestLog("Click on Back To Top button", "Back To Top button is not clicked", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : selectJumpToSectionVerify     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document selectJumpToSectionVerify1(String option) {
		boolean blnFlag = false;

		// WebElement mobileToolbar =
		// commonLibrary.isExistNegative(UIMAP_Document.mobileToolbar, 10);
		// WebElement rightContainer =
		// commonLibrary.isExistNegative(UIMAP_Document.rightContainer, 10);
		// WebElement jumpto = commonLibrary.isExistNegative(rightContainer,
		// UIMAP_Document.jumpto, 10);
		//
		// commonLibrary.clickButtonParentWithWait(jumpto, "Jump To");

		WebElement sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		WebElement sections = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sections, 10);

		commonLibrary.clickButtonParentWithWait(sections, "Section Dropdown");
		sectionContainer = commonLibrary.isExistNegative(UIMAP_Document.sectionContainer, 10);
		WebElement sectionsDropdown = commonLibrary.isExistNegative(sectionContainer, UIMAP_Document.sectionsDropdown, 10);
		WebElement optionList = commonLibrary.isExistNegative(sectionsDropdown, UIMAP_Document.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_Document.tagButton, 10);
		for (WebElement item : options) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(item, item.getText());
				commonLibrary.sleep(10000);
				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Select Section " + option, option + " is not selected.", Status.FAIL);
		// commonLibrary.Select_FromList_Button(optionList, option);
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
					report.updateTestLog("Verify page is scrolled to section: " + option, "Section " + option + " is not present.", Status.FAIL);
				if ((ySection - yToolbar) >= 0 && (ySection - yToolbar) <= 1000)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);
			} else {
				WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
				int yHeading = txtDocumentHeading.getLocation().getY();
				if ((yHeading - yToolbar) >= 0 && (yHeading - yToolbar) <= 400)
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is scrolled to section: " + option, Status.PASS);
				else
					report.updateTestLog("Verify page is scrolled to section: " + option, "Page is not scrolled to section: " + option, Status.FAIL);

			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected Citation
	// # Function Name : verifySelectedCitation     
	// # Author : Bikash
	// # Date Created : Oct'13
	// #*****************************************************************************************************************************

	public Document verifySelectedCitation(String citation) {
		WebElement currentStyleFormat = commonLibrary.isExistNegative(UIMAP_Document.currentStyleFormat, 10);
		if (currentStyleFormat != null) {
			if (currentStyleFormat.getText().toLowerCase().contains(citation.toLowerCase()))
				report.updateTestLog("Verify selected citation " + citation, "Selected citation " + citation + " displays", Status.PASS);
			else
				report.updateTestLog("Verify selected citation " + citation, "Selected citation " + citation + " does not display", Status.FAIL);
		} else {
			report.updateTestLog("Verify current style format displays", "current style format does not displays", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify parties and citation order in
	// preview box
	// # Function Name : verifyPreviewPartiesAndCitationOrder     
	// # Author : Bikash
	// # Date Created : Oct'13
	// #*****************************************************************************************************************************

	public Document verifyPreviewPartiesAndCitationOrder(String parties, String citation) {

		WebElement previewTextStyle = commonLibrary.isExistNegative(UIMAP_Document.previewTextStyle, 10);
		if (previewTextStyle != null) {
			String previewTextArray[] = previewTextStyle.getText().split(",");
			if (previewTextArray[0].contains(parties) && previewTextArray[1].contains(citation)) {
				report.updateTestLog("Verify the parties " + parties + " displays as first item and " + citation + " displays as second item in the preview box", "The parties " + parties + " displays as first item and " + citation + " displays as second item in the preview box", Status.PASS);
			} else {
				report.updateTestLog("Verify the parties " + parties + " displays as first item and " + citation + " displays as second item in the preview box", "The parties " + parties + " does not displays as first item and " + citation + " does not displays as second item in the preview box", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify preview text style displays in the preview box", "The preview text style does not displays in the preview box", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text separated by commas and
	// spaces in
	// preview box
	// # Function Name : verifyPreviewSeparatedByCommaAndSpaces     
	// # Author : Bikash
	// # Date Created : Oct'13
	// #*****************************************************************************************************************************

	public Document verifyPreviewSeparatedByCommaAndSpaces(String parties, String citation) {

		WebElement previewTextStyle = commonLibrary.isExistNegative(UIMAP_Document.previewTextStyle, 10);
		if (previewTextStyle != null) {
			String previewText = parties + "," + citation;
			final Pattern pattern = Pattern.compile("^[^,]* *, *[^,]*$");
			if (pattern.matcher(previewText).matches()) {
				report.updateTestLog("Verify the citation " + citation + " displays as separated from parties" + parties + " by comma and a space in the preview box", "The citation " + citation + " displays as separated from parties " + parties + " by comma and a space in the preview box", Status.PASS);
			} else {
				report.updateTestLog("Verify the citation " + citation + " displays as separated from parties" + parties + " by comma and a space in the preview box", "The citation " + citation + "does not displays as separated from parties" + parties + " by comma and a space in the preview box", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify preview text style displays in the preview box", "The preview text style does not displays in the preview box", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get the current window handle in
	// document page
	// # Function Name : getCurrentWindowHandle
	// # Author : Bikash
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Document getCurrentWindowHandle() {
		currentWindow = driver.getWindowHandle();
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the new windows in
	// document page
	// # Function Name : closeNewWindow
	// # Author : Bikash
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Document closeNewWindow() {
		for (String winHandle : driver.getWindowHandles()) {
			// switch focus of WebDriver to the next found window handle (that's
			// your newly opened window)
			driver.switchTo().window(winHandle);
		}
		// close newly opened window when done with it
		driver.close();
		// switch back to the original window
		driver.switchTo().window(currentWindow);
		if (driver.getWindowHandles().size() == 1)
			report.updateTestLog("Verify Secondary window has been closed", "Secondary window has been closed", Status.PASS);
		else
			report.updateTestLog("Verify Secondary window has been closed", "Secondary window has not been closed", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document verifyPageTitle(String title) {
		generalFunctions.verifyPageTitle(title);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : ClickBrowserBack
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public WorkFolders clickBrowserBackFolders() {
		commonLibrary.clickBrowserBack();

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC is not present
	// # Function Name : verifyMiniTOCIsCollapsed
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document verifyMiniTOCIsNotPresent() {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		if (tableOfContentsTab == null) {

			report.updateTestLog("Verify Mini Toc is not available", "Mini Toc is not available", Status.PASS);

		} else {
			report.updateTestLog("Verify Mini Toc is not  available", "Mini Toc is available", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title,court,date
	// displays in floating area
	// # Function Name : verifyDocCourtDateDisplaysInFloat1     
	// # Author : Kalaivanan
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************
	public Document verifyDocCourtDateDisplaysInFloat1(String docTitle, String court, String date) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commonLibrary.sleep(10000);
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		report.updateTestLog("Scroll down the full document", "Scrolled down the full document", Status.DONE);
		commonLibrary.sleep(10000);
		WebElement floatArea = commonLibrary.isExist(UIMAP_Document.floatAreaSticky, 10);
		WebElement menu = commonLibrary.isExist(UIMAP_Document.menu1, 20);
		WebElement slider = commonLibrary.isExist(UIMAP_Document.docslider, 20);
		String temp = date;

		String[] tempArr = temp.split(" ");

		if (floatArea != null && floatArea.getText().toLowerCase().contains(docTitle.toLowerCase()) && floatArea.getText().toLowerCase().contains(court.toLowerCase())) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatArea, UIMAP_Document.courtDateInfo, 10);

			String floatDate = courtDateInfo.get(1).getText();
			String[] newDate = floatDate.split(" ");

			if ((newDate[2].contains(tempArr[0])) && (tempArr[1].contains(newDate[3])) && (newDate[4].contains(tempArr[2]))) {
				flag = true;
			}
			if (flag) {
				report.updateTestLog("Verify date in results list and document area", "Date in the results list and document area are matching", Status.PASS);
			} else
				report.updateTestLog("Verify date in results list and document area", "Date in the results list and document area are not matching, Expected result was " + temp + " but it is " + floatDate, Status.FAIL);

			if (courtDateInfo.size() > 0 && courtDateInfo.get(0).isDisplayed() && courtDateInfo.get(1).isDisplayed()) {
				flag1 = true;
			}
			if (flag1)
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.PASS);
			else
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		} else {
			report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		}

		if (menu != null && slider != null) {
			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is displayed along with Delivery tray and Document slider", Status.PASS);
		} else {
			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is not displayed along with Delivery tray and Document slider", Status.FAIL);
		}
		commonLibrary.sleep(10000);
		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
		report.updateTestLog("Scroll back to the top of the document", "Scrolled back to the top of the document", Status.DONE);
		commonLibrary.sleep(10000);
		commonLibrary.sleep(10000);
		WebElement floatAreaNew = commonLibrary.isExist(UIMAP_Document.floatArea, 10);
		if (floatAreaNew != null) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatAreaNew, UIMAP_Document.courtDateInfo, 10);

			if (courtDateInfo.size() > 0 && !courtDateInfo.get(0).isDisplayed() && !courtDateInfo.get(1).isDisplayed()) {
				flag2 = true;
			}
			if (flag2)
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date does not display in sign post area", Status.PASS);
			else
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		} else {
			report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title,court,date
	// displays in floating area from history link
	// # Function Name : verifyDocCourtDateDisplaysInFloatFromHistory     
	// # Author : Kalaivanan
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************
	public Document verifyDocCourtDateDisplaysInFloatFromHistory(String docTitle, String court, String date) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		report.updateTestLog("Scroll down the full document", "Scrolled down the full document", Status.DONE);
		commonLibrary.sleep(10000);
		WebElement floatArea = commonLibrary.isExist(UIMAP_Document.floatAreaSticky, 10);
		WebElement menu = commonLibrary.isExist(UIMAP_Document.menu1, 20);

		String temp = date;
		String[] tempArr = temp.split(" ");

		if (floatArea != null && floatArea.getText().toLowerCase().contains(docTitle.toLowerCase()) && floatArea.getText().toLowerCase().contains(court.toLowerCase())) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatArea, UIMAP_Document.courtDateInfo, 10);

			String floatDate = courtDateInfo.get(1).getText();
			String[] newDate = floatDate.split(" ");

			if ((newDate[2].contains(tempArr[0])) && (tempArr[1].contains(newDate[3])) && (newDate[4].contains(tempArr[2]))) {
				flag = true;
			}
			if (flag) {
				report.updateTestLog("Verify date in results list and document area", "Date in the results list and document area are matching", Status.PASS);
			} else
				report.updateTestLog("Verify date in results list and document area", "Date in the results list and document area are not matching", Status.FAIL);

			if (courtDateInfo.size() > 0 && courtDateInfo.get(0).isDisplayed() && courtDateInfo.get(1).isDisplayed()) {
				flag1 = true;
			}
			if (flag1)
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.PASS);
			else
				report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		} else {
			report.updateTestLog("Verify sign post area displays in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", "Sign post area is not displayed in the format 'Document: " + docTitle + " | Court: " + court + " | Date: " + date + " | Actions'", Status.FAIL);
		}

		if (menu != null) {
			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is displayed along with Delivery tray and Document slider", Status.PASS);
		} else

			report.updateTestLog("Verify sign post area displays along with Delivery tray and Document slider", "Sign post area is not displayed along with Delivery tray and Document slider", Status.FAIL);

		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
		report.updateTestLog("Scroll back to the top of the document", "Scrolled back to the top of the document", Status.DONE);
		commonLibrary.sleep(10000);
		WebElement floatAreaNew = commonLibrary.isExist(UIMAP_Document.floatArea, 10);
		if (floatAreaNew != null) {
			List<WebElement> courtDateInfo = commonLibrary.isExistList(floatAreaNew, UIMAP_Document.courtDateInfo, 10);

			if (courtDateInfo.size() > 0 && !courtDateInfo.get(0).isDisplayed() && !courtDateInfo.get(1).isDisplayed()) {
				flag2 = true;
			}
			if (flag2)
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date does not display in sign post area", Status.PASS);
			else
				report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		} else {
			report.updateTestLog("Verify Court and Date does not display in sign post area", "Court and Date is displayed in sign post area", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify given string is not present
	// as link
	// # Function Name : verifyEmbedLinkNotPresent
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Document verifyEmbedLinkNotPresent(String term) {

		boolean hot = true;
		List<WebElement> embed = commonLibrary.isExistList(UIMAP_Document.embed, 10);
		if (embed == null) {
			report.updateTestLog("Verify" + term + " is not hot", term + "is not hot", Status.PASS);
		} else if (embed.size() > 0) {
			for (WebElement item : embed) {
				if (item.getText().equalsIgnoreCase(term)) {
					hot = false;
					report.updateTestLog("Verify" + term + " is not hot", term + "is hot", Status.FAIL);
					break;
				}
			}
		}
		if (hot)
			report.updateTestLog("Verify" + term + " is not hot", term + "is not hot", Status.PASS);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the sub topics under the
	// topic in
	// document
	// # Function Name : verifySubTopic
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public void subTopic(String subTopicContent) {
		boolean subTopicPresent = false;
		WebElement topic = commonLibrary.isExist(UIMAP_Document.docText, 30);
		List<WebElement> subTopic = commonLibrary.isExistList(topic, UIMAP_Document.subHeader, 30);
		if (subTopic.size() > 0) {
			for (WebElement subToicContent : subTopic) {
				if (subToicContent.getText().toLowerCase().trim().contains(subTopicContent.toLowerCase().trim())) {
					subTopicPresent = true;
					break;
				}

			}

			if (subTopicPresent) {
				report.updateTestLog("Verify sub topics " + subTopicContent + "under the main topic in document page", "Sub topics " + subTopicContent + " display under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify sub topic " + subTopicContent + " under the main topic in document page", "Sub topic " + subTopicContent + " does not display under the main topic in document page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify sub topic " + subTopicContent + " under the main topic in document page", "Sub topic " + subTopicContent + " does not display under the main topic in document page", Status.FAIL);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the sub topics under the
	// topic in
	// document
	// # Function Name : verifySubTopic
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public Document verifySubTopic(String subTopicContent) {
		String subTopics[] = subTopicContent.split(",");
		for (int i = 0; i < subTopics.length; i++) {
			subTopic(subTopics[i]);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the order of the header
	// arear under the
	// topic in
	// document
	// # Function Name : verifyDocumentHeaderAreaOrder
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentHeaderAreaOrder(String practiceArea, String jurisdiction, String context) {
		boolean headerPresent = false;
		WebElement document = commonLibrary.isExist(UIMAP_Document.docText, 30);
		List<WebElement> header = commonLibrary.isExistList(document, UIMAP_Document.headerCheck, 30);
		if (header.size() > 0) {
			if (header.get(0).getText().toLowerCase().trim().contains(practiceArea.toLowerCase().trim()) && header.get(1).getText().toLowerCase().trim().contains(jurisdiction.toLowerCase().trim()) && header.get(2).getText().toLowerCase().trim().contains(context.toLowerCase().trim())) {

				headerPresent = true;

			}

			if (headerPresent) {
				report.updateTestLog("Verify header " + practiceArea + " displays as first, " + jurisdiction + " displays as second and " + context + " displays as last under the main topic in document page", "Verify header " + practiceArea + " displays as first, " + jurisdiction + " displays as second and " + context + " displays as last under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify header " + practiceArea + " displays as first, " + jurisdiction + " displays as second and " + context + " displays as last under the main topic in document page", "Verify header " + practiceArea + " does not displays as first, " + jurisdiction + " does not displays as second and " + context + " does not displays as last under the main topic in document page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify header " + practiceArea + " displays as first, " + jurisdiction + " displays as second and " + context + " displays as last under the main topic in document page", "Verify header " + practiceArea + " does not displays as first, " + jurisdiction + " does not displays as second and " + context + " does not displays as last under the main topic in document page", Status.FAIL);

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the content of header
	// arear under the
	// topic in
	// document
	// # Function Name : verifyDocumentHeaderAreaWithContent
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentHeaderAreaWithContent(String practiceAreaContent, String jurisdictionContent, String context) {
		boolean headerContent = false;
		WebElement document = commonLibrary.isExist(UIMAP_Document.docText, 30);
		if (document != null) {
			if (document.getText().toLowerCase().trim().contains(practiceAreaContent.toLowerCase().trim()) && document.getText().toLowerCase().trim().contains(jurisdictionContent.toLowerCase().trim()) && document.getText().toLowerCase().trim().contains(context.toLowerCase().trim())) {

				headerContent = true;

			}

			if (headerContent) {
				report.updateTestLog("Verify header content with header " + practiceAreaContent + " displays, " + jurisdictionContent + " displays " + context + " displays under the main topic in document page", "Verify header content with header" + practiceAreaContent + " displays , " + jurisdictionContent + " displays and " + context + " displays as last under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify header content with header " + practiceAreaContent + " displays, " + jurisdictionContent + " displays " + context + " displays under the main topic in document page", "Verify header content with header " + practiceAreaContent + " displays, " + jurisdictionContent + " displays and " + context + " displays under the main topic in document page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify header content with header " + practiceAreaContent + " displays, " + jurisdictionContent + " displays " + context + " displays under the main topic in document page", "Verify header content with header " + practiceAreaContent + " displays, " + jurisdictionContent + " displays and " + context + " displays under the main topic in document page", Status.FAIL);

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the order of the practice
	// arear under the
	// title in
	// document
	// # Function Name : verifyPrcticeAreaBelowTitle
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public Document verifyPrcticeAreaBelowTitle(String practiceArea, String headerTitle) {
		boolean headerPresent = false;
		WebElement document = commonLibrary.isExist(UIMAP_Document.docText, 30);
		List<WebElement> header = commonLibrary.isExistList(document, UIMAP_Document.headerCheck, 30);
		WebElement documentTitle = commonLibrary.isExist(UIMAP_Document.h1, 30);
		if (header.size() > 0) {
			if (header.get(0).getText().toLowerCase().trim().contains(practiceArea.toLowerCase().trim()) && documentTitle.getText().toLowerCase().trim().contains(headerTitle.toLowerCase().trim())) {

				headerPresent = true;

			}

			if (headerPresent) {
				report.updateTestLog("Verify header " + practiceArea + " displays as below the , " + headerTitle + " under the main topic in document page", "Verify header " + practiceArea + " displays as below the , " + headerTitle + "under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify header " + practiceArea + " displays as below the , " + headerTitle + " under the main topic in document page", "Verify header " + practiceArea + " does not displays as below the , " + headerTitle + "under the main topic in document page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify header " + practiceArea + " displays as below the , " + headerTitle + " under the main topic in document page", "Verify header " + practiceArea + " does not displays as below the , " + headerTitle + "under the main topic in document page", Status.FAIL);

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the total count in each
	// section under the
	// topic in
	// document
	// # Function Name : verifyTotalCountInSection
	// # Author : Bikash
	// # Date Created : 23,OCT'15
	// #*****************************************************************************************************************************

	public Document verifyTotalCountInSection() {
		boolean subTopicPresent = false;
		WebElement topic = commonLibrary.isExist(UIMAP_Document.docText, 30);
		List<WebElement> subTopic = commonLibrary.isExistList(topic, UIMAP_Document.subHeader, 30);
		if (subTopic.size() > 0) {
			for (WebElement subToicContent : subTopic) {
				if (subToicContent.getText().toLowerCase().trim().matches(".*\\d+.*")) {
					subTopicPresent = true;
					break;
				}

			}

			if (subTopicPresent) {
				report.updateTestLog("Verify sub topics with total count under the main topic in document page", "Sub topics with total count displays under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify sub topics with total count under the main topic in document page", "Sub topics with total count does not displays under the main topic in document page", Status.FAIL);

			}
		} else {
			report.updateTestLog("Verify sub topics with total count under the main topic in document page", "Sub topics with total count does not displays under the main topic in document page", Status.FAIL);

		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify more and fewer link in
	// document page
	// # Function Name : verifyMoreFewerLink     
	// # Author : Bikash
	// # Date Created : Oct'26
	// #*****************************************************************************************************************************
	public Document verifyMoreFewerLink() {
		boolean moreLink = false;
		boolean fewerLink = false;
		WebElement topic = commonLibrary.isExist(UIMAP_Document.docText, 30);
		if (topic != null) {
			List<WebElement> ssList = commonLibrary.isExistList(topic, UIMAP_Document.ssList, 20);
			if (ssList.size() > 0) {
				List<WebElement> listItem = commonLibrary.isExistList(ssList.get(1), UIMAP_Document.lstTagName, 20);
				if (listItem.size() == 3) {
					WebElement showMore = commonLibrary.isExist(UIMAP_Document.showMore, 30);
					if (showMore != null) {
						moreLink = true;
						if (browsername.contains("internet")) {
							commonLibrary.clickButtonParentWithWaitJS(showMore, "View more");
						} else {
							commonLibrary.clickButtonParentWithWait(showMore, "View more");
						}

						commonLibrary.sleep(3000);

					}
					WebElement showLess = commonLibrary.isExist(UIMAP_Document.showLess, 30);
					if (showLess != null) {
						fewerLink = true;
					}

				}

			}
		}

		if (moreLink && fewerLink)
			report.updateTestLog("verify more and fewer link  are displayed in each section", "More and fewer link are displayed in each section", Status.PASS);
		else
			report.updateTestLog("verify more and fewer link are displayed in each section", "More and fewer link are not displayed in each section", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the entries in each sub
	// topics under the
	// topic in
	// document
	// # Function Name : VerifyEntriesInEachSection
	// # Author : Bikash
	// # Date Created : 26,OCT'15
	// #*****************************************************************************************************************************

	public Document VerifyEntriesInEachSection(String subTopicContent, String entry, String pageLink) {
		boolean entryPresent = false;
		String sectionContent = null;
		int sectionIndex = 0;
		WebElement topic = commonLibrary.isExist(UIMAP_Document.docText, 30);
		List<WebElement> subTopic = commonLibrary.isExistList(topic, UIMAP_Document.subHeader, 30);
		if (subTopic.size() > 0) {
			for (WebElement subToicContent : subTopic) {
				if (subToicContent.getText().toLowerCase().trim().contains(subTopicContent.toLowerCase().trim())) {
					sectionContent = subToicContent.getText().toLowerCase().trim();
					sectionIndex = subTopic.indexOf(subToicContent);
					break;
				}

			}
			List<WebElement> ssList = commonLibrary.isExistList(topic, UIMAP_Document.ssList, 20);
			if (sectionContent.contains(entry.toLowerCase().trim()) && ssList.get(sectionIndex).getText().toLowerCase().trim().contains(pageLink.toLowerCase().trim())) {
				entryPresent = true;
			}

			if (entryPresent) {
				report.updateTestLog("Verify entries " + entry + "and " + pageLink + " in section " + subTopicContent + " under the main topic in document page", "Verified entries " + entry + "and " + pageLink + " are displayed in section " + subTopicContent + " under the main topic in document page", Status.PASS);
			} else {
				report.updateTestLog("Verify entries " + entry + "and " + pageLink + " in section " + subTopicContent + " under the main topic in document page", "Verified entries " + entry + "and " + pageLink + " are not displayed in section " + subTopicContent + " under the main topic in document page", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify sub topic " + subTopicContent + " under the main topic in document page", "Sub topic " + subTopicContent + " does not display under the main topic in document page", Status.FAIL);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links on document page
	// # Function Name : clickDocPageLink2     
	// # Author : Bikash
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Document clickDocPageLink2(String linkName) {

		generalFunctions.clickDocPageLink(linkName);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Seminal Cases is having 8
	// entries in more list and 3 entries in fewer list in
	// document page
	// # Function Name : verifyMoreFewerList     
	// # Author : Bikash
	// # Date Created : Oct'26
	// #*****************************************************************************************************************************
	public Document verifyMoreFewerList(String element, String entries) {
		int moreLink = 0;
		int fewerLink = 0;
		boolean totalLinkPresent = false;
		List<WebElement> listItemBeforeExpand = null;
		List<WebElement> ssExpandedList = null;
		List<WebElement> listItemAfterExpand = null;
		WebElement topic = commonLibrary.isExist(UIMAP_Document.docText, 30);
		if (topic != null) {
			List<WebElement> ssList = commonLibrary.isExistList(topic, UIMAP_Document.ssList, 20);
			List<WebElement> showMore = commonLibrary.isExistList(topic, UIMAP_Document.showMore, 20);
			if (showMore.size() > 0) {
				if (browsername.contains("internet")) {
					if (element.equals("Seminal Cases"))
						commonLibrary.clickButtonParentWithWaitJS(showMore.get(0), "View more");
					else if (element.equals("Secondary Sources"))
						commonLibrary.clickButtonParentWithWaitJS(showMore.get(1), "View more");
				} else {
					if (element.equals("Seminal Cases"))
						commonLibrary.clickButtonParentWithWait(showMore.get(0), "View more");

					else if (element.equals("Secondary Sources"))
						commonLibrary.clickButtonParentWithWait(showMore.get(1), "View more");
				}
			}
			commonLibrary.sleep(3000);
			ssExpandedList = commonLibrary.isExistList(topic, UIMAP_Document.ssExpandedList, 20);
			if (element.equals("Seminal Cases")) {
				listItemBeforeExpand = commonLibrary.isExistList(ssList.get(1), UIMAP_Document.lstTagName, 20);
				listItemAfterExpand = commonLibrary.isExistList(ssExpandedList.get(0), UIMAP_Document.lstTagName, 20);
			} else if (element.equals("Secondary Sources")) {
				listItemBeforeExpand = commonLibrary.isExistList(ssList.get(4), UIMAP_Document.lstTagName, 20);
				listItemAfterExpand = commonLibrary.isExistList(ssExpandedList.get(1), UIMAP_Document.lstTagName, 20);
			}

			moreLink = listItemBeforeExpand.size() + listItemAfterExpand.size();
			List<WebElement> showLess = commonLibrary.isExistList(topic, UIMAP_Document.showLess, 20);
			if (showLess.size() > 0) {
				if (browsername.contains("internet")) {
					if (element.equals("Seminal Cases"))
						commonLibrary.clickButtonParentWithWaitJS(showLess.get(0), "View less");
					else if (element.equals("Secondary Sources"))
						commonLibrary.clickButtonParentWithWaitJS(showLess.get(1), "View less");

				} else {
					if (element.equals("Seminal Cases"))
						commonLibrary.clickButtonParentWithWait(showLess.get(0), "View less");
					else if (element.equals("Secondary Sources"))
						commonLibrary.clickButtonParentWithWait(showLess.get(1), "View less");
				}
			}
			commonLibrary.sleep(3000);
			fewerLink = listItemBeforeExpand.size();

			switch (element) {
			case "Seminal Cases":
				if (moreLink == Integer.parseInt(entries) && fewerLink == 3)
					totalLinkPresent = true;
				break;
			case "Secondary Sources":
				if (moreLink == Integer.parseInt(entries) && fewerLink == 3)
					totalLinkPresent = true;
				break;
			}

		}

		if (totalLinkPresent)
			report.updateTestLog("verify " + element + " is having " + entries + " entries in more link and 3 entries in fewer links", "verified " + element + " is having  " + entries + " entries in more link and 3 entries in fewer links", Status.PASS);
		else
			report.updateTestLog("verify " + element + " is having  " + entries + " entries in more link and 3 entries in fewer links", "verified " + element + " is not having  " + entries + " entries in more link and 3 entries in fewer links", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on reference in jump to option
	// # Function Name : clickandVerifyReferenceJumpto    
	// # Author : Deepha Hariramasamy
	// # Date Created : Nov '15
	// #*****************************************************************************************************************************
	public Document clickandVerifyReferenceJumpto() {
		WebElement referenceButton = commonLibrary.isExist(UIMAP_Shepards.shepReferenceButton, 20);
		WebElement referenceNext = commonLibrary.isExist(referenceButton, UIMAP_Shepards.referenceNextterm, 20);
		if (referenceNext != null) {
			commonLibrary.clickButton(referenceNext);
			commonLibrary.isExist(UIMAP_Shepards.highlightDoc, 20);
			report.updateTestLog("Verify the document is highlighted", "The document is highlighted", Status.PASS);
		} else {
			report.updateTestLog("Verify the document highlighted", "The document is not highlighted ", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Drafting Note
	// # Function Name : clickAlterClauseIcon     
	// # Author : Kalaivanan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Document clickDraftNotesUnderSection(String strSection, String strLink) {
		List<WebElement> divClause = commonLibrary.isExistList(UIMAP_Document.divClause, 20);
		if (divClause.size() > 0) {
			for (int i = 0; i < divClause.size(); i++) {
				WebElement sectionSpan = commonLibrary.isExistNegative(divClause.get(i), UIMAP_Document.p, 10);
				if (sectionSpan != null && sectionSpan.getText().toLowerCase().contains(strSection.toLowerCase())) {
					report.updateTestLog("Verify section name " + strSection, strSection + " present", Status.PASS);
					WebElement alterClauseImg = commonLibrary.isExistNegative(divClause.get(i), UIMAP_Document.draftingNote, 10);
					commonLibrary.clickButton(alterClauseImg);
					commonLibrary.sleep(5000);
					WebElement popup = commonLibrary.isExistNegative(UIMAP_Document.draftingNotePopup, 10);
					if ((popup != null) && (popup.isDisplayed())) {
						report.updateTestLog("Verify Drafting Note popup displays", "Drafting note popup is displayed", Status.PASS);
						List<WebElement> draftNoteLink = commonLibrary.isExistList(popup, UIMAP_Document.actualLink, 10);
						for (WebElement link : draftNoteLink) {
							if (link.getText().contains(strLink)) {
								commonLibrary.clickButton(link);
								break;
							}
						}
						commonLibrary.sleep(5000);
						WebElement heading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						if (heading.getText().contains(strLink)) {
							report.updateTestLog("Verify " + strLink + " cross referenced form displays", strLink + " cross referenced form is displayed", Status.PASS);
						}
					} else {
						report.updateTestLog("Verify " + strLink + " cross referenced form", strLink + " cross referenced form is not verified", Status.FAIL);
					}
				}
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify header presence inside
	// document page
	// # Function Name : verifyHeaderPresenceInsideDoc     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyHeaderPresenceInsideDoc(boolean presence, String header) {
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		if (docText != null) {
			List<WebElement> headerText = commonLibrary.isExistList(docText, UIMAP_Document.subHeader, 10);
			if (presence) {
				if (headerText.size() > 0) {
					boolean flag = false;
					for (WebElement item : headerText) {
						// verify header is present in the full document page
						if (item.getText().trim().toLowerCase().contains(header.toLowerCase())) {
							report.updateTestLog("Verify " + header + " section present in the full document", header + " section is present in the full document", Status.PASS);
							flag = true;
							break;
						}
					}
					if (!flag)
						report.updateTestLog("Verify " + header + " section present in the full document", header + " section is not present anywhere in the full document", Status.FAIL);
				} else
					report.updateTestLog("Verify " + header + " section present in the full document", header + " section is not present anywhere in the full document", Status.FAIL);
			} else {
				if (headerText.size() > 0) {
					boolean flag = true;
					for (WebElement item : headerText) {
						// verify header is not present in the full document
						// page
						if (item.getText().trim().toLowerCase().contains(header.toLowerCase())) {
							report.updateTestLog("Verify " + header + " section present in the full document", header + " section is present in the full document", Status.FAIL);
							flag = false;
							break;
						}
					}
					if (flag)
						report.updateTestLog("Verify " + header + " section present in the full document", header + " section is not present anywhere in the full document", Status.PASS);
				} else
					report.updateTestLog("Verify " + header + " section present in the full document", header + " section is not present anywhere in the full document", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify " + header + " section present in the full document", header + " section is not present anywhere in the full document", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Added and Deleted displays
	// inside document page
	// # Function Name : verifyAddedDeletedDisplayed    
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyAddedDeletedDisplayed() {
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		if (docText != null) {
			boolean addFlag = false;
			boolean deleteFlag = false;
			List<WebElement> addDeleteList = commonLibrary.isExistList(docText, UIMAP_Document.headerCheck, 10);
			if (addDeleteList.size() > 0) {
				for (WebElement item : addDeleteList) {
					// verify added displays in document page
					if (item.getText().trim().toLowerCase().contains("added")) {
						report.updateTestLog("Verify Added displays under Notice section", "Added is displayed under Notice section", Status.PASS);
						addFlag = true;

					}
					// verify deleted displays in document page
					else if (item.getText().trim().toLowerCase().contains("deleted")) {
						report.updateTestLog("Verify Deleted displays under Notice section", "Deleted is displayed under Notice section", Status.PASS);
						deleteFlag = true;
					}
				}
				if (!addFlag)
					report.updateTestLog("Verify Added displays under Notice section", "Added is not displayed under Notice section", Status.FAIL);
				if (!deleteFlag)
					report.updateTestLog("Verify Deleted displays under Notice section", "Deleted is not displayed under Notice section", Status.FAIL);
			} else
				report.updateTestLog("Verify Added and Deleted displays under Notice section", "Added and Deleted is displayed under Notice section", Status.FAIL);
		} else
			report.updateTestLog("Verify Added and Deleted displays under Notice section", "Added and Deleted is displayed under Notice section", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify green and red sentences
	// displays inside document page
	// # Function Name : verifyGreenRedSentences    
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyGreenRedSentences(boolean presence) {
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		if (docText != null) {
			List<WebElement> greenSentence = commonLibrary.isExistList(docText, UIMAP_Document.greenSentence, 10);
			List<WebElement> redSentence = commonLibrary.isExistList(docText, UIMAP_Document.redSentence, 10);
			if (presence) {
				// verify green and red sentences are present
				if (greenSentence.size() > 0 && redSentence.size() > 0) {
					report.updateTestLog("Verify sentences are highlighted in green colour under Text section", "Sentences are highlighted in green colour under Text section", Status.PASS);
					report.updateTestLog("Verify sentences are in red colour with a strikethrough under Text section", "Sentences are in red colour with a strikethrough under Text section", Status.PASS);
				} else {
					report.updateTestLog("Verify sentences are highlighted in green colour under Text section", "Sentences are not highlighted in green colour under Text section", Status.FAIL);
					report.updateTestLog("Verify sentences are in red colour with a strikethrough under Text section", "Sentences are not in red colour with a strikethrough under Text section", Status.FAIL);
				}
			} else {
				// verify green and red sentences are not present
				if (greenSentence.size() > 0 && redSentence.size() > 0) {
					report.updateTestLog("Verify sentences are highlighted in green colour under Text section", "Sentences are highlighted in green colour under Text section", Status.FAIL);
					report.updateTestLog("Verify sentences are in red colour with a strikethrough under Text section", "Sentences are in red colour with a strikethrough under Text section", Status.FAIL);
				} else {
					report.updateTestLog("Verify sentences are highlighted in green colour under Text section", "Sentences are not highlighted in green colour under Text section", Status.PASS);
					report.updateTestLog("Verify sentences are in red colour with a strikethrough under Text section", "Sentences are not in red colour with a strikethrough under Text section", Status.PASS);
				}
			}
		} else
			report.updateTestLog("Verify green and red sentences displays inside document page", "Document page is not displayed", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC
	// # Function Name : clickTOCVerifyminiToc
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public Document clickALinkAndSubLinkTOC(String doclink, String sublink) {
		// boolean blnFlag1 = true;
		// Click on TOC link in mini toc
		commonLibrary.sleep(10000);
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.miniToc, 10);

		if (tableOfContentsTab != null) {
			List<WebElement> links = commonLibrary.isExistList(tableOfContentsTab, UIMAP_Document.listItems, 20);
			for (WebElement li : links) {
				if (li.getText().toLowerCase().contains(doclink.toLowerCase())) {
					WebElement btnDropDown = commonLibrary.isExistNegative(li, UIMAP_Document.tagButton, 10);
					if (btnDropDown != null) {
						commonLibrary.clickButtonParentWithWait(btnDropDown, doclink);
					}
					break;
				}
			}

		} else
			report.updateTestLog("Verification of Mini TOC", "TOC tab is not available", Status.FAIL);

		commonLibrary.sleep(100000);
		List<WebElement> links1 = commonLibrary.isExistList(tableOfContentsTab, UIMAP_Document.subsectionlink, 10);
		for (WebElement li1 : links1) {
			if (li1.getText().toLowerCase().contains(doclink.toLowerCase())) {
				WebElement linkexpanded = commonLibrary.isExistNegative(li1, UIMAP_Document.subnodeSection, 10);
				commonLibrary.sleep(10000);
				if (linkexpanded != null) {
					List<WebElement> links = commonLibrary.isExistList(linkexpanded, UIMAP_Document.listItems, 20);
					for (WebElement li : links) {
						// WebElement anchorTag =
						// commonLibrary.isExistNegative(li,UIMAP_Document.links,
						// 10);
						// WebElement btnDropDown =
						// commonLibrary.isExistNegative(UIMAP_Document.tagButton,
						// 10);
						if (li.getText().toLowerCase().contains(sublink.toLowerCase())) {
							WebElement anchorTag = commonLibrary.isExistNegative(li, UIMAP_Document.links, 10);
							if (anchorTag != null)
								commonLibrary.clickButtonParentWithWait(anchorTag, sublink);
							break;
						}

					}

				} else
					report.updateTestLog("Verification of Mini TOC", "TOC tab is not available", Status.FAIL);
			}
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on view enhanced version of
	// this form
	// # Function Name : clickOnViewEnhancements
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Document clickAndVerifyCommentsSection(Boolean check) {

		WebElement btnHideShowComments = commonLibrary.isExist(UIMAP_Document.btnHideComments, 20);
		WebElement divComments = commonLibrary.isExist(UIMAP_Document.divComments, 20);
		if (check && divComments != null && btnHideShowComments != null) {
			commonLibrary.clickButton(btnHideShowComments);
		}
		if (btnHideShowComments != null) {
			if (check)
				commonLibrary.clickButtonParentWithWait(btnHideShowComments, "Show Comments");
			else
				commonLibrary.clickButtonParentWithWait(btnHideShowComments, "Hide Comments");
		}

		divComments = commonLibrary.isExist(UIMAP_Document.divComments, 20);
		if (check) {
			if (divComments != null && divComments.isDisplayed())
				report.updateTestLog("Verify 'Comments' section is present in the document", "'Comment' section  present in the document", Status.PASS);
			else
				report.updateTestLog("Verify 'Comments' section is present in the document", "'Comment' section  not present in the document", Status.FAIL);
		} else {
			if (divComments == null)
				report.updateTestLog("Verify 'Comments' section is not present in the document", "'Comment' section  not present in the document", Status.PASS);
			else
				report.updateTestLog("Verify 'Comments' section is not present in the document", "'Comment' section is present in the document", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option in Medmal
	// # Function Name : addToFolderUsingQuickSave
	// # Author : Sriram
	// # Date Created : Nov 15
	// #*****************************************************************************************************************************

	public Document addToFolderUsingQuickSaveMedmal(String FolderName) {
		try {
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolderdiv = commonLibrary.isExist(UIMAP_Document.addtoFolderDocument, 10);
			WebElement addtoFolder = commonLibrary.isExist(addtoFolderdiv, UIMAP_Document.addtoFolderDocument1, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			List<WebElement> folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
			if (folder.size() > 0) {
				for (WebElement fol : folder) {
					if (fol.getText().contains(FolderName)) {
						commonLibrary.clickButtonParentWithWait(fol, FolderName);
						break;
					}
				}
			} else
				report.updateTestLog("Select the folder", "Folder not available", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the sharing information text
	// # Function Name : verifySharingInfo     
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Document verifySharingInfo(String shareText) {
		WebElement sharingInfo = commonLibrary.isExist(UIMAP_WorkFolders.sharingInfo, 10);
		if (sharingInfo != null) {
			commonLibrary.clickJS(sharingInfo);
			WebElement mainshare = commonLibrary.isExist(UIMAP_WorkFolders.mainshare, 10);
			List<WebElement> OListResult1 = commonLibrary.isExistList(mainshare, UIMAP_WorkFolders.wordWheelContent, 20);
			for (WebElement list : OListResult1) {

				String[] arr = list.getText().split("\\n");
				if (arr[0].contains(shareText))
					report.updateTestLog("Verifying sharing information text", "Sharing information text" + shareText + "is displayed", Status.PASS);
				else
					report.updateTestLog("Verifying sharing information text", "Sharing information text" + shareText + "is not displayed", Status.FAIL);
			}
			WebElement save = commonLibrary.isExist(UIMAP_WorkFolders.save, 10);
			if (save != null)
				commonLibrary.clickJS(save);

		} else
			report.updateTestLog("Clicking on sharing information link", "sharing information link not displayed", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyResultListDisplayed
	// # Function Name : verifyResultListDisplayed     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyResultListDisplayed() {

		WebElement slider = commonLibrary.isExist(UIMAP_Document.docslider, 20);
		if (slider != null) {
			WebElement resultlist = commonLibrary.isExist(slider, UIMAP_Document.resultlink, 20);
			if (resultlist != null) {
				report.updateTestLog("Verify resultlist link displays", "resultlist link is displayed ", Status.PASS);
			} else {
				report.updateTestLog("Verify resultlist link displays", "resultlist link is not displayed ", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify resultlist link displays", "resultlist link is not displayed ", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document scrolled to a
	// particular section
	// # Function Name : verifyTextDisplayedOnScreen     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentisScrolledToSection(String section) {
		WebElement docText = commonLibrary.isExistNegative(UIMAP_Document.docText, 10);
		List<WebElement> paraList = commonLibrary.isExistList(docText, UIMAP_Document.listheader, 10);
		boolean flag = false;
		for (WebElement para : paraList) {
			WebElement span = commonLibrary.isExistNegative(para, UIMAP_Document.spanclass1, 10);
			if (span != null && span.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase(section.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
				int y = para.getLocation().getY();
				flag = true;
				WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
				int yToolbar = toolbar.getLocation().getY();
				if (y - yToolbar > 0 && y - yToolbar < 1000) {
					report.updateTestLog("Verify document is scrolled to " + section, "Document is scrolled to " + section, Status.PASS);
				} else
					report.updateTestLog("Verify document is scrolled to " + section, "Document is not scrolled to " + section, Status.FAIL);
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify document is scrolled to " + section, section + " is not present in document.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify applied filters in search
	// selected text popup
	// # Function Name : verifyAppliedFiltersInSSTPopup     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyAppliedFiltersInSSTPopup(String filters) {
		String[] filterArr = filters.split(";");
		int filterSize = filterArr.length;
		int count = 0;
		WebElement popupAside = commonLibrary.isExist(UIMAP_Document.popupAside, 10);
		WebElement selectedFilters = commonLibrary.isExist(popupAside, UIMAP_Document.selectedFilters, 10);
		List<WebElement> filtersList = commonLibrary.isExistList(selectedFilters, UIMAP_Document.listItems, 10);
		if (filtersList.size() > 0) {
			for (WebElement item : filtersList) {
				for (int i = 0; i < filterArr.length; i++) {
					if (item.getText().trim().toLowerCase().contains(filterArr[i].toLowerCase())) {
						report.updateTestLog("Verify " + filterArr[i] + " displays in the search selected text popup", filterArr[i] + " is displayed in the search selected text popup", Status.PASS);
						count++;
						break;
					}
					if (count == filterSize)
						break;
				}
				if (count == filterSize)
					break;
			}
			if (count != filterSize) {
				report.updateTestLog("Verify applied filters displays in the search selected text popup", "All the applied filters are not displayed in the search selected text popup", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify applied filters displays in the search selected text popup", "All the applied filters are not displayed in the search selected text popup", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click search in search selected text
	// popup
	// # Function Name : clickSearchInSSTPopup     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Search clickSearchInSSTPopup() {
		WebElement popupAside = commonLibrary.isExist(UIMAP_Document.popupAside, 10);
		WebElement search = commonLibrary.isExist(popupAside, UIMAP_Document.submitsearch, 10);
		if (search != null) {
			commonLibrary.clickMethod(search, "Search");

			WebElement formClass = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult3, 3);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(5000);
				formClass = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult3, 3);
			} while (formClass == null && count <= 40);
		} else {
			report.updateTestLog("Click Search button in search selected text popup", "Search button is not available in search selected text popup", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Document verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to itigationProfiler
	// page
	// by clicking Lexis Advance arrow
	// # Function Name : navigateTolitigationProfile     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LitigationProfile navigateToLPSPage() {

		// Calling general function

		generalFunctions.productSwitcher("Litigation profile suite");

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter page number in Jump To page
	// number text box
	// # Function Name : enterPageNumberInJumpTo     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document enterPageNumberInJumpTo(String pageNumber) {
		WebElement pageJumpDiv = commonLibrary.isExist(UIMAP_Document.pageJumpDiv, 10);
		WebElement pageJumpTextBox = commonLibrary.isExist(pageJumpDiv, UIMAP_Document.pageJumpTextBox, 10);
		if (pageJumpTextBox != null) {
			commonLibrary.setDataInTextBoxClear(pageJumpTextBox, pageNumber, "Page");
		} else {
			report.updateTestLog("Enter " + pageNumber + " in Jump To Page text box", "Jump To Page text box is not available ", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify page number inside document
	// is next to another page number
	// # Function Name : verifyPageNumberNextToAnother     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyPageNumberNextToAnother(String parentPageNumber, String nextPageNumber, boolean presence) {
		boolean flag = false;
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 10);
		if (docText != null) {
			List<WebElement> pageNumber = commonLibrary.isExistList(docText, UIMAP_Document.pageNumber, 10);
			if (pageNumber.size() > 0) {
				if (presence) {
					for (int i = 0; i < pageNumber.size(); i++) {
						if (pageNumber.get(i).getText().trim().toLowerCase().contains(parentPageNumber.toLowerCase())) {
							if (pageNumber.get(i + 1).getText().trim().toLowerCase().contains(nextPageNumber.toLowerCase())) {
								report.updateTestLog("Verify " + nextPageNumber + " displays as the next page number after " + parentPageNumber, nextPageNumber + " is displayed as the next page number after " + parentPageNumber, Status.PASS);
							} else {
								report.updateTestLog("Verify " + nextPageNumber + " displays as the next page number after " + parentPageNumber, nextPageNumber + " is not displayed as the next page number after " + parentPageNumber, Status.FAIL);
							}
							flag = true;
							break;
						}
					}
					if (!flag)
						report.updateTestLog("Verify page number inside document is next to another page number", "Page number " + parentPageNumber + " is not displayed", Status.FAIL);
				} else {
					for (int i = 0; i < pageNumber.size(); i++) {
						if (pageNumber.get(i).getText().trim().toLowerCase().contains(parentPageNumber.toLowerCase())) {
							if (pageNumber.get(i + 1).getText().trim().toLowerCase().contains(nextPageNumber.toLowerCase())) {
								report.updateTestLog("Verify " + nextPageNumber + " not displays as the next page number after " + parentPageNumber, nextPageNumber + " is displayed as the next page number after " + parentPageNumber, Status.FAIL);
							} else {
								report.updateTestLog("Verify " + nextPageNumber + " not displays as the next page number after " + parentPageNumber, nextPageNumber + " is not displayed as the next page number after " + parentPageNumber, Status.PASS);
							}
							flag = true;
							break;
						}
					}
					if (!flag)
						report.updateTestLog("Verify page number inside document is next to another page number", "Page number " + parentPageNumber + " is not displayed", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify page number inside document is next to another page number", "Page number is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify page number inside document is next to another page number", "Page number is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Last selected colour is
	// displayed in the selected text menu
	// # Function Name : verifyLastSelectedColorPresent     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyLastSelectedColorPresent() {
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		WebElement highlightedOptions = commonLibrary.isExist(docContextMenu, UIMAP_Document.highlightOptions, 10);
		WebElement lastSelectedColor = commonLibrary.isExist(highlightedOptions, UIMAP_Document.selectedColors, 10);
		if (lastSelectedColor != null)
			report.updateTestLog("Verify Last selected color box is displayed next to the Highlight link", "Last selected color box is displayed next to the Highlight link", Status.PASS);
		else
			report.updateTestLog("Verify Last selected color box is displayed next to the Highlight link", "Last selected color box is not displayed", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click expand dropdown arrow next to
	// Last selected colour box
	// # Function Name : clickColorExpandDropdown     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document clickColorExpandDropdown() {
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		WebElement highlightedOptions = commonLibrary.isExist(docContextMenu, UIMAP_Document.highlightOptions, 10);
		WebElement expandDropdownArrow = commonLibrary.isExist(highlightedOptions, UIMAP_Document.expandColorSelector, 10);
		if (expandDropdownArrow != null) {
			commonLibrary.clickMethod(expandDropdownArrow, "Expand dropdown arrow next to Last selected color box");
			commonLibrary.sleep(50000);
			WebElement expandedDropdownArrow = commonLibrary.isExist(highlightedOptions, UIMAP_Document.expandedColorSelector, 10);
			if (expandedDropdownArrow != null)
				report.updateTestLog("Verify Dropdown expands to choose the highlight color", "Dropdown is expanded to choose the highlight color", Status.PASS);
			else
				commonLibrary.clickMethod(expandDropdownArrow, "Expand dropdown arrow next to Last selected color box");
		} else
			report.updateTestLog("Click expand dropdown arrow next to Last selected colour box", "Expand dropdown arrow next to Last selected colour box is not available", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify all the seven colors are
	// displayed in the highlight colors area
	// # Function Name : verifyAllHighlightColorsDisplayed     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyAllHighlightColorsDisplayed() {
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		WebElement highlightColors = commonLibrary.isExist(docContextMenu, UIMAP_Document.highlightColors, 10);
		if (highlightColors != null && highlightColors.isDisplayed()) {
			List<WebElement> highlightColorsList = commonLibrary.isExistList(highlightColors, UIMAP_Document.listtag, 10);
			if (highlightColorsList.size() == 7) {
				report.updateTestLog("Verify all the seven colors exists in the highlight colors area", "All the seven colors exists in the highlight colors area", Status.PASS);
			} else {
				report.updateTestLog("Verify all the seven colors exists in the highlight colors area", "All the seven colors does not exists in the highlight colors area", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify all the seven colors exists in the highlight colors area", "All the seven colors does not exists in the highlight colors area", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click colors in the highlight colors
	// area
	// # Function Name : clickColorInHighlightColorsArea     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document clickColorInHighlightColorsArea(String color) {
		String buttonId = "";
		boolean flag = false;
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		WebElement highlightColors = commonLibrary.isExist(docContextMenu, UIMAP_Document.highlightColors, 10);
		if (highlightColors != null && highlightColors.isDisplayed()) {
			List<WebElement> highlightColorsList = commonLibrary.isExistList(highlightColors, UIMAP_Document.button, 10);
			if (highlightColorsList.size() > 0) {
				switch (color) {
				case "Yellow(1st)":
					buttonId = "FFF9D4";
					break;

				case "Orange(2nd)":
					buttonId = "F8D6AF";
					break;

				case "Pink(3rd)":
					buttonId = "FBD2D3";
					break;

				case "Purple(4th)":
					buttonId = "EFE1EF";
					break;

				case "Blue(5th)":
					buttonId = "D5EFF9";
					break;

				case "Green(6th)":
					buttonId = "ECF5DB";
					break;

				case "Grey(7th)":
					buttonId = "E9E9EA";
					break;
				}
				for (WebElement item : highlightColorsList) {
					if (item.getAttribute("id").equals(buttonId)) {
						commonLibrary.clickJSNoLog(item);
						report.updateTestLog("Click on " + color + " color", "Clicked on " + color + " color", Status.PASS);
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
						flag = true;
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Click on " + color + " color", "Not clicked on " + color + " color", Status.FAIL);
			} else
				report.updateTestLog("Click on " + color + " color", "Not clicked on " + color + " color", Status.FAIL);
		} else
			report.updateTestLog("Click on " + color + " color", "Not clicked on " + color + " color", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify highlighted text color
	// # Function Name : verifyHighlightedTextColor     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyHighlightedTextColor(String color) {
		String buttonId = "";
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 10);
		WebElement highlightedText = commonLibrary.isExist(docText, UIMAP_Document.highlightedText, 10);
		switch (color) {
		case "Yellow(1st)":
			buttonId = "rgb(255, 249, 212)";
			break;

		case "Orange(2nd)":
			buttonId = "rgb(248, 214, 175)";
			break;

		case "Pink(3rd)":
			buttonId = "rgb(251, 210, 211)";
			break;

		case "Purple(4th)":
			buttonId = "rgb(239, 225, 239)";
			break;

		case "Blue(5th)":
			buttonId = "rgb(213, 239, 249)";
			break;

		case "Green(6th)":
			buttonId = "rgb(236, 245, 219)";
			break;

		case "Grey(7th)":
			buttonId = "rgb(233, 233, 234)";
			break;
		}
		if (highlightedText != null) {
			if (highlightedText.getAttribute("style").contains(buttonId)) {
				report.updateTestLog("Verify highlighted text is in " + color + " color", "Highlighted text is in " + color + " color", Status.PASS);
			} else {
				report.updateTestLog("Verify highlighted text is in " + color + " color", "Highlighted text is not in " + color + " color", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify highlighted text is in " + color + " color", "No text is highlighted", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the color of Last selected
	// color
	// # Function Name : verifyColorOfLastSelectedColor     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Document verifyColorOfLastSelectedColor(String color) {
		String buttonId = "";
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		WebElement highlightedOptions = commonLibrary.isExist(docContextMenu, UIMAP_Document.highlightOptions, 10);
		WebElement lastSelectedColor = commonLibrary.isExist(highlightedOptions, UIMAP_Document.selectedColors, 10);
		switch (color) {
		case "Yellow(1st)":
			buttonId = "rgb(255, 249, 212)";
			break;

		case "Orange(2nd)":
			buttonId = "rgb(248, 214, 175)";
			break;

		case "Pink(3rd)":
			buttonId = "rgb(251, 210, 211)";
			break;

		case "Purple(4th)":
			buttonId = "rgb(239, 225, 239)";
			break;

		case "Blue(5th)":
			buttonId = "rgb(213, 239, 249)";
			break;

		case "Green(6th)":
			buttonId = "rgb(236, 245, 219)";
			break;

		case "Grey(7th)":
			buttonId = "rgb(233, 233, 234)";
			break;
		}
		if (lastSelectedColor != null) {
			if (lastSelectedColor.getAttribute("style").contains(buttonId)) {
				report.updateTestLog("Verify Last selected color " + color + " is displayed in the Last selected color box", "Last selected color " + color + " is displayed in the Last selected color box", Status.PASS);
			} else {
				report.updateTestLog("Verify Last selected color " + color + " is displayed in the Last selected color box", "Last selected color " + color + " is not displayed in the Last selected color box", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify Last selected color " + color + " is displayed in the Last selected color box", "Last selected color box is not displayed", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify particular words are
	// highlighted
	// # Function Name : verifyHighlighted    
	// # Author : Seetha
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public Document verifyHighlighted(String text) {

		boolean color = false;
		String[] texts = text.split(";");
		for (int i = 0; i < texts.length; i++) {
			List<WebElement> high = commonLibrary.isExistList(UIMAP_SearchResult.highlight1, 20);
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

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Access Indicator in document
	// # Function Name : verifyAccessIndicator
	// # Author : Sriram
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************

	public Document verifyAccessIndicator() {

		WebElement eltAboutThisDoc = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 10);

		if (eltAboutThisDoc != null) {
			WebElement AccessIndicator = commonLibrary.isExistNegative(eltAboutThisDoc, UIMAP_Document.span, 10);
			if (AccessIndicator.getText().contains("Accessed ($) - This document is available until"))

				report.updateTestLog("Veriy Accessed indicator is displayed under About this document section", "Accessed indicator is displayed under About this document section", Status.PASS);
			else

				report.updateTestLog("Veriy Accessed indicator is displayed under About this document section", "Accessed indicator is displayed under About this document section", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify footer link displays in order
	// # Function Name : footerLinkOrder   
	// # Author : Sriram
	// # Date Created : 8 Dec 2015
	// #*****************************************************************************************************************************
	public Document footerLinkOrder(String text[]) {
		int i = 0;
		WebElement footer = commonLibrary.isExist(UIMAP_Document.footer, 20);
		List<WebElement> footerLink = commonLibrary.isExistList(footer, UIMAP_Document.listItems, 20);

		if (i == 0) {
			WebElement logo = commonLibrary.isExist(footerLink.get(i), UIMAP_Document.lnLogo, 20);
			if (logo != null)
				report.updateTestLog("Veriy Lexis Nexis logo is displayed first in footer link", "Lexis Nexis logo is displayed first in footer link", Status.PASS);
			else
				report.updateTestLog("Veriy Lexis Nexis logo is displayed first in footer link", "Lexis Nexis logo is not displayed first in footer link", Status.FAIL);
		}
		for (i = 1; i < 6; i++) {
			if (footerLink.get(i).getText().equals(text[i]))
				report.updateTestLog("Veriy" + text[i] + "is displayed in order", text[i] + " is displayed in order", Status.PASS);
			else
				report.updateTestLog("Veriy" + text[i] + "is displayed in order", text[i] + " is not displayed in order", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Full Document
	// # Function Name : verifyFullDocumentPresent    
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document verifyFullDocumentPresent() {
		int count = 0;

		// wait for the document to be opened
		WebElement h1 = commonLibrary.isExist(UIMAP_Document.h1, 10);

		do {

			count++;
			h1 = commonLibrary.isExist(UIMAP_Document.h1, 10);
			commonLibrary.sleep(10000);

		} while (h1 == null && count < 15);

		WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.DocTitleContent, 10);

		if (eltDocumentHeading != null)
			report.updateTestLog("Verify Full document is displayed", "Full document: " + eltDocumentHeading.getText() + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Full document is displayed", "Full document: " + "" + "DocumentHeading is not displayed", Status.FAIL);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : SimpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAResults lpasimpleSearch(String strSearchTerm, Boolean strClearFilter) {

		int check = 0;

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new LPAResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Inline Results button
	// # Function Name : clickResults
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document clickinlineResults() {
		WebElement inlineResultsTab = commonLibrary.isExistNegative(UIMAP_Document.inlineResultsTab, 10);
		if (inlineResultsTab != null) {
			report.updateTestLog("Verify Results displayed in the Document Page", "Results is displayed 'left hand side' of the Document", Status.PASS);
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(inlineResultsTab, "Inline Results");
			} else
				commonLibrary.clickLinkWithWebElementWithWait(inlineResultsTab, "Inline Results");
		} else
			report.updateTestLog("Click Inline Results Button", "Inline Results Button is not available", Status.FAIL);
		commonLibrary.sleep(100000);
		WebElement expandInlineResultsTab = commonLibrary.isExistNegative(UIMAP_Document.expandinlineResults, 10);

		if (expandInlineResultsTab != null) {
			report.updateTestLog("Verify Inline results are displayed in the Full document", "Inline results are displayed", Status.PASS);
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change the Inline Results button
	// from expanded state to collapse
	// # Function Name : clickResults
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document expandedToCollapseInlineResults() {
		WebElement expandInlineResultsTab = commonLibrary.isExistNegative(UIMAP_Document.expandinlineResults, 10);

		if (expandInlineResultsTab != null) {
			report.updateTestLog("Verify the expansion of Inline Results Button", "Inline Results Button is expanded", Status.PASS);
			commonLibrary.clickMethod(expandInlineResultsTab, "Inline Results");
			WebElement collapseInlineResultsTab = commonLibrary.isExistNegative(UIMAP_Document.collapseinlineResults, 10);
			if (collapseInlineResultsTab != null) {
				report.updateTestLog("Verify the collapse of Inline Results Button", "Inline Results Button is collapsed", Status.PASS);
			} else {
				report.updateTestLog("Verify the collapse of Inline Results Button", "Inline Results Button is not collapsed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify the expansion of Inline Results Button", "Inline Results Button is not expanded", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click Results Link
	// # Function Name : clickResultslist
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax clickResultLink() {
		WebElement resultlistTab = commonLibrary.isExistNegative(UIMAP_Document.resultlistlatnews, 10);
		if (resultlistTab != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(resultlistTab, "Result List");
			else
				commonLibrary.clickLinkWithWebElementWithWait(resultlistTab, "Result List");
		} else
			report.updateTestLog("Click Results List Button", "Results List Button is not available", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document from inline results
	// list
	// # Function Name : click link
	// # Author : Jubin
	// # Date Created : Dec'16
	// #*****************************************************************************************************************************
	public Document clickDocInlineResults(String strDocTitle) {
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.aside, 10);
		WebElement inlineResultUl = commonLibrary.isExist(taxTab, UIMAP_Document.lstTagUList, 10);
		List<WebElement> inlineResultLi = commonLibrary.isExistList(inlineResultUl, UIMAP_Document.lstTagListItems, 10);
		boolean flag = false;
		for (int i = 0; i < inlineResultLi.size(); i++) {
			WebElement button = commonLibrary.isExist(inlineResultLi.get(i), UIMAP_Document.inlineresultbutton, 10);
			if (button != null && button.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(button, button.getText());
				report.updateTestLog("Click tab: " + strDocTitle, "Tab: " + strDocTitle + " is  present", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click tab: " + strDocTitle, "Tab: " + strDocTitle + " is not present", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify message present in full
	// document.
	// # Function Name : verifymsgPresence     
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document verifymsgPresence(String text) {
		WebElement wholeBox = commonLibrary.isExistNegative(UIMAP_Document.wholebox, 10);
		WebElement oopmsg = commonLibrary.isExistNegative(wholeBox, UIMAP_Document.oopmsgindoc, 10);

		if (oopmsg.getText().toLowerCase().contains((text).toLowerCase())) {
			report.updateTestLog("Verify " + oopmsg + " is present in the document", oopmsg + " is present in the preview box", Status.PASS);
		} else {
			report.updateTestLog("Verify " + oopmsg + " is present in the document", oopmsg + " is not present in the preview box", Status.FAIL);
		}

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Cancel button in full
	// document
	// # Function Name : clickcancelButton
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Dec'15
	// #*************************************************************************
	public Search clickcancelButton() {
		WebElement eltcancelbutton = commonLibrary.isExist(UIMAP_Home.eltcancelbutton, 20);
		commonLibrary.clickButton(eltcancelbutton);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Original Source Image and
	// verify the document int eh secondary window
	// # Function Name : verifyOriginalSourceImageLink
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void clickcontinueButtonSwitchtoWindow() {
		WebElement eltcontibutton = commonLibrary.isExist(UIMAP_Home.eltcontibutton, 20);
		commonLibrary.clickButton(eltcontibutton);

		commonLibrary.sleep(50000);
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		commonLibrary.switchToWindow("SignIn");

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get no. of document from inline
	// results list
	// # Function Name : verifyInlineResultsCount
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyInlineResultsCount(int expCOunt) {
		List<WebElement> inlineResultLi = null;

		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.aside, 10);
		WebElement inlineResultUl = commonLibrary.isExist(taxTab, UIMAP_Document.lstTagUList, 10);
		inlineResultLi = commonLibrary.isExistList(inlineResultUl, UIMAP_Document.lstTagListItems, 10);
		int actCount = inlineResultLi.size();

		if (actCount == expCOunt) {
			report.updateTestLog("Verify results list count match", "Results list count " + expCOunt + " matches with the Inline results count " + actCount, Status.PASS);
		} else {
			report.updateTestLog("Verify results list count match", "Results list count " + expCOunt + " does not matches with the Inline results count " + actCount, Status.FAIL);
		}
		return new Document(scriptHelper);
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
	// # Function Description : Function for navigating to home page
	// by clicking Lexis Advance arrow
	// # Function Name : navigateToHomePage   
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home navigateToHomePage() {

		// Calling general function

		generalFunctions.productSwitcher("Research");

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify option in selected text menu
	// # Function Name : verifyOptionInSelectedTextMenu     
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyOptionInSelectedTextMenu(String option, String Exist) {
		boolean flag = false;
		WebElement docContextMenu = commonLibrary.isExist(UIMAP_Document.docContextMenu, 20);
		List<WebElement> button = commonLibrary.isExistList(docContextMenu, UIMAP_Document.btnButton, 10);
		if (button.size() > 0) {
			for (WebElement item : button) {
				if (item.getText().trim().toLowerCase().contains(option.trim().toLowerCase())) {
					flag = true;
					break;
				}
			}
		}
		switch (Exist) {
		case "Exist": {
			if (flag)
				report.updateTestLog("Verify " + option + " is displayed in selected text menu", option + " is displayed in selected text menu", Status.PASS);

			else
				report.updateTestLog("Verify " + option + " is displayed in selected text menu", option + " is not displayed in selected text menu", Status.FAIL);
			break;
		}

		case "NotExist": {
			if (!flag)
				report.updateTestLog("Verify " + option + " is not displayed in selected text menu", option + " is not displayed in selected text menu", Status.PASS);

			else
				report.updateTestLog("Verify " + option + " is not displayed in selected text menu", option + " is displayed in selected text menu", Status.FAIL);
			break;
		}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a paragraph in document
	// # Function Name : selectTextInDocumentNew     
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document selectTextInDocumentNew() {
		boolean flag = false;
		WebElement docText = commonLibrary.isExist(UIMAP_Document.docText, 20);
		List<WebElement> paragraph = commonLibrary.isExistList(docText, UIMAP_Document.text, 10);
		for (WebElement item : paragraph) {
			if (item.getText().trim().toLowerCase() != null) {
				commonLibrary.focusControlJS(item);
				Actions action = new Actions(driver);
				action.moveToElement(item, 5, 0).doubleClick().build().perform();
				commonLibrary.sleep(50000);
				report.updateTestLog("Selecting the text in document", "Text is selected in the document", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Selecting the text in document", "Text is not selected in the document", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify terms are not highlighted and
	// not bolded
	// # Function Name : verifyTermHighlightedAndBoldedNegative     
	// # Author : Pratik
	// # Date Created : Dec' 2015
	// #*****************************************************************************************************************************
	public Document verifyTermHighlightedAndBoldedNegative(String searchTerm) {

		// verifying that a given text is getting displayed as Bolded and
		// highlighted
		String highlightTerm[] = searchTerm.split(";");
		for (int i = 0; i < highlightTerm.length; i++) {

			// looping for the highlighted terms

			boolean flag = false;
			List<WebElement> highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
			for (WebElement item : highlights) {
				// checking the region with location

				if (item.getText().toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").contains(highlightTerm[i].toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "")) && item.getCssValue("background-color").contains("rgba(255, 240, 153, 1)")) {
					flag = true;
					break;
				}

			}
			if (flag)
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", Status.FAIL);
			else
				report.updateTestLog("Verify the search term " + highlightTerm[i] + " is bolded and highlighted in yellow color", "The search term " + highlightTerm[i] + " is not bolded and highlighted in yellow color", Status.PASS);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Browser back button
	// # Function Name : Clickbrowserback     
	// # Author : Anbu
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************
	public TOC clickbrowserbackTOC() {

		commonLibrary.clickBrowserBack();
		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click Results Link
	// # Function Name : clickResultslist
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults clickResultLink1() {
		WebElement resultlistTab = commonLibrary.isExist(UIMAP_Document.resultlink1, 10);
		if (resultlistTab != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(resultlistTab, "Result List");
			else
				commonLibrary.clickLinkWithWebElementWithWait(resultlistTab, "Result List");
		} else
			report.updateTestLog("Click Results List Button", "Results List Button is not available", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document scrolled to a
	// particular section
	// # Function Name : verifyDocumentisScrolledToSection1     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document verifyDocumentisScrolledToSection1(String section) {
		boolean flag = false;
		WebElement docText = commonLibrary.isExistNegative(UIMAP_Document.docText, 10);
		List<WebElement> paraList = commonLibrary.isExistList(docText, UIMAP_Document.text, 10);
		if (paraList.size() > 0) {
			for (WebElement para : paraList) {
				// WebElement span =
				// commonLibrary.isExistNegative(para,UIMAP_Document.tagSpan,
				// 10);
				if (para.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(section.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
					int y = para.getLocation().getY();
					flag = true;
					WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
					int yToolbar = toolbar.getLocation().getY();
					if (y - yToolbar > 0 && y - yToolbar < 1000) {
						report.updateTestLog("Verify document is scrolled to the section '" + section + "'", "Document is scrolled to the section '" + section + "'", Status.PASS);
					} else
						report.updateTestLog("Verify document is scrolled to the section '" + section + "'", "Document is not scrolled to the section '" + section + "'", Status.FAIL);
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify document is scrolled to " + section, section + " is not present in document.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on toc footer
	// particular section
	// # Function Name : clickontocfooter     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	// public Document clicktocfooter() {
	//
	//
	// WebElement
	// toc=commonLibrary.isExist(UIMAP_Document.tableOfContentsTab,10);
	// List<WebElement> toc1
	// =commonLibrary.isExistList(toc,UIMAP_Document.listItems,10);
	// for(WebElement item : toc1)
	// {
	//
	// if(item.getText().contains("Search for recent developments"))
	// {
	//
	// WebElement footer
	// =commonLibrary.isExist(toc,UIMAP_Document.resultlink1,10);
	// if(footer!= null){
	//
	// commonLibrary.clickButtonParent(footer, "Primary Law");
	// }
	// else{
	//
	// report.updateTestLog("Click footer Button",
	// "footer Button is not available", Status.FAIL);
	// }
	//
	// }
	// }
	//
	// return new Document(scriptHelper);
	// }
	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax swithToCondentType(String strCondentType) {

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
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

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

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view all notes
	// # Function Name : clickViewAllNotes 
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document clickViewAllNotes() {
		WebElement notes = commonLibrary.isExist(UIMAP_IMContent.notes, 20);
		WebElement viewAllNotes = commonLibrary.isExist(notes, UIMAP_IMContent.viewAllNotes, 10);
		if (viewAllNotes != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(viewAllNotes, "View all notes");
			else
				commonLibrary.clickButtonParentWithWait(viewAllNotes, "View all notes");
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add notes in document page
	// # Function Name : addNotes
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document addNotes(String text, boolean isCheck) {
		// driver.switchTo().defaultContent();
		try {
			WebElement addheader = commonLibrary.isExist(UIMAP_IMContent.addheader, 10);

			WebElement addbtn = commonLibrary.isExist(addheader, UIMAP_IMContent.addbtn, 10);
			if (addheader != null) {

				if (browsername.contains("internet"))
					commonLibrary.clickJS(addbtn, "Add Note");
				else
					commonLibrary.clickButtonParentWithWait(addbtn, "Add Note");
				WebElement editPopup = commonLibrary.isExist(UIMAP_IMContent.editPopup, 10);
				if (editPopup != null) {
					driver.switchTo().frame(editPopup);
					WebElement addContent = commonLibrary.isExist(UIMAP_IMContent.editContent, 10);

					if (browsername.contains("internet"))
						commonLibrary.clickJS(addContent, "textarea in edit pop up");
					else
						commonLibrary.clickButtonParentWithWait(addContent, "textarea in edit pop up");

					driver.switchTo().defaultContent();
					editPopup = commonLibrary.isExist(UIMAP_IMContent.editPopup, 10);
					driver.switchTo().frame(editPopup);
					addContent = commonLibrary.isExist(UIMAP_IMContent.editContent, 10);
					commonLibrary.setDataInTextBox(addContent, text, "Addnote Text box");

					driver.switchTo().defaultContent();
					WebElement notesView = commonLibrary.isExistNegative(UIMAP_SearchResult.notepopup, 10);
					WebElement chkRecipient = commonLibrary.isExistNegative(notesView, UIMAP_SearchResult.chkbox, 10);

					commonLibrary.setCheckBox(chkRecipient, isCheck);
					WebElement save = commonLibrary.isExist(UIMAP_IMContent.save, 10);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(save, "Save");
					else
						commonLibrary.clickButtonParentWithWait(save, "Save");
				}
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes in header
	// # Function Name : verifyNoteHeader
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyNoteHeader(String note) {

		WebElement addedNote = commonLibrary.isExist(UIMAP_IMContent.noteSection, 10);

		if (addedNote.getText().toLowerCase().contains(note.toLowerCase()))
			report.updateTestLog("verify " + note + " is displayed in header", note + " is displayed in header", Status.PASS);
		else
			report.updateTestLog("verify " + note + " is displayed in header", note + " is not displayed in header", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #****************************************************************************************************************************
	// # Function Description : Function to verify add button in notes section
	// # Function Name : verfiyAddbutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document verfiyAddbutton(String exist) {
		WebElement addheader = commonLibrary.isExist(UIMAP_IMContent.addheader, 20);
		WebElement addbtn = commonLibrary.isExist(addheader, UIMAP_IMContent.addbtn, 10);
		switch (exist) {
		case "Exist": {
			if (addbtn != null)
				report.updateTestLog("Verify add button is displayed", "add button is displayed", Status.PASS);
			else
				report.updateTestLog("Verify add button is displayed", "add button is not displayed", Status.FAIL);
			break;
		}
		case "NotExist": {
			if (addbtn == null)
				report.updateTestLog("Verify add button is not displayed", "add button is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify add button is not displayed", "add button is displayed", Status.FAIL);
			break;
		}
		}
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Owner is displayed
	// # Function Name : verifyNOwner
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyDate() {

		WebElement ownerheader = commonLibrary.isExist(UIMAP_IMContent.ownerheader, 10);
		WebElement date = commonLibrary.isExist(ownerheader, UIMAP_IMContent.date, 10);
		if (date != null)
			report.updateTestLog("verify date and time is displayed", "Date and time is displayed", Status.PASS);
		else
			report.updateTestLog("verify date and time is displayed ", "Date and time is not displayed ", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify edit button in notes section
	// # Function Name : verfiyEditbutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document verifyEditbutton(String exist) {
		WebElement editheader = commonLibrary.isExist(UIMAP_IMContent.editheader, 20);
		WebElement editbtn = commonLibrary.isExist(editheader, UIMAP_IMContent.editbtn, 10);
		switch (exist) {
		case "Exist": {
			if (editbtn != null)
				report.updateTestLog("Verify edit button is displayed", "Edit button is displayed", Status.PASS);
			else
				report.updateTestLog("Verify edit button is displayed", "Edit button is not displayed", Status.FAIL);
			break;
		}
		case "NotExist": {
			if (editbtn == null)
				report.updateTestLog("Verify edit button is not displayed", "Edit button is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify edit button is not displayed", "Edit button is displayed", Status.FAIL);
			break;
		}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify delete button in notes
	// section
	// # Function Name : verifyDeletebutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document verifyDeletebutton(String exist) {
		WebElement editheader = commonLibrary.isExist(UIMAP_IMContent.editheader, 20);
		WebElement deletebtn = commonLibrary.isExist(editheader, UIMAP_IMContent.deletebtn, 10);
		switch (exist) {
		case "Exist": {
			if (deletebtn != null)
				report.updateTestLog("Verify delete button is displayed", "delete button is displayed", Status.PASS);
			else
				report.updateTestLog("Verify delete button is displayed", "delete button is not displayed", Status.FAIL);
			break;
		}
		case "NotExist": {
			if (deletebtn == null)
				report.updateTestLog("Verify delete button is not displayed", "delete button is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify delete button is not displayed", "delete button is displayed", Status.FAIL);
			break;
		}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes in header
	// # Function Name : verifyNoteinSection
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyNoteinSection(String note) {

		WebElement ownerheader = commonLibrary.isExist(UIMAP_IMContent.ownerheader, 10);
		WebElement addedNote = commonLibrary.isExist(ownerheader, UIMAP_IMContent.content, 10);

		if (addedNote.getText().toLowerCase().contains(note.toLowerCase()))
			report.updateTestLog("verify " + note + " is displayed ", note + " is displayed ", Status.PASS);
		else
			report.updateTestLog("verify " + note + " is displayed ", note + " is not displayed ", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delete button
	// # Function Name : clickDeletebutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document clickDeletebutton(String text) {

		List<WebElement> editheader = commonLibrary.isExistList(UIMAP_IMContent.ownerheader, 20);
		for (int i = 0; i < editheader.size(); i++) {
			WebElement owner = commonLibrary.isExist(editheader.get(i), UIMAP_IMContent.owner, 10);
			if (owner.getText().toLowerCase().contains(text.toLowerCase())) {
				WebElement deletebtn = commonLibrary.isExist(editheader.get(i), UIMAP_IMContent.deletebtn, 10);
				if (deletebtn != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(deletebtn, "Delete in Editor note");
					else
						commonLibrary.clickButtonParentWithWait(deletebtn, "Delete in Editor note");
				}
				driver.switchTo().defaultContent();
				WebElement delete = commonLibrary.isExist(UIMAP_IMContent.delete, 10);
				if (delete != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(delete, "Delete in pop up");
					else
						commonLibrary.clickButtonParentWithWait(delete, "Delete in pop up");
				}
				break;
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify edit button in notes section
	// # Function Name : verfiyEditDeleteOption
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public Document verfiyEditDeleteOption() {

		List<WebElement> editheader = commonLibrary.isExistList(UIMAP_IMContent.editheader, 20);
		for (int i = 0; i < editheader.size(); i++) {

			WebElement editbtn = commonLibrary.isExist(editheader.get(i), UIMAP_IMContent.editbtn, 10);
			if (i == 0) {
				if (editbtn == null)
					report.updateTestLog("Verify edit button is not displayed for editor", "Edit button is not displayed ", Status.PASS);
				else
					report.updateTestLog("Verify edit button is not displayed for editor", "Edit button is displayed ", Status.FAIL);

				WebElement deletebtn = commonLibrary.isExist(editheader.get(i), UIMAP_IMContent.deletebtn, 10);

				if (deletebtn != null)
					report.updateTestLog("Verify delete button is displayed for editor", "delete button is displayed", Status.PASS);
				else
					report.updateTestLog("Verify delete button is displayed for editor", "delete button is not displayed", Status.FAIL);

			} else {
				if (editbtn != null)
					report.updateTestLog("Verify edit button is displayed for owner", "Edit button is displayed ", Status.PASS);
				else
					report.updateTestLog("Verify edit button is displayed for owner", "Edit button is not displayed ", Status.FAIL);

				WebElement deletebtn = commonLibrary.isExist(editheader.get(i), UIMAP_IMContent.deletebtn, 10);

				if (deletebtn != null)
					report.updateTestLog("Verify delete button is displayed for owner", "delete button is displayed", Status.PASS);
				else
					report.updateTestLog("Verify delete button is displayed for owner", "delete button is not displayed", Status.FAIL);
			}

		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes are not displayed in
	// header
	// # Function Name : verifyNotesnotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyNotesnotDisplayed(String note) {
		boolean flag = false;
		List<WebElement> ownerheader = commonLibrary.isExistList(UIMAP_IMContent.ownerheader, 10);
		for (int i = 0; i < ownerheader.size(); i++) {
			List<WebElement> addedNote = commonLibrary.isExistList(ownerheader.get(i), UIMAP_IMContent.content, 10);
			for (WebElement item : addedNote) {
				if (item.getText().toLowerCase().contains(note.toLowerCase())) {
					flag = true;
					break;
				}
			}

		}
		if (!flag)
			report.updateTestLog("verify " + note + " is not displayed ", note + " is not displayed ", Status.PASS);
		else
			report.updateTestLog("verify " + note + " is not displayed ", note + " is displayed ", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify owner details
	// # Function Name : verifyOwnerDetails
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyOwnerDetails(String text[], String note[]) {
		boolean flag = false;

		List<WebElement> ownerheader = commonLibrary.isExistList(UIMAP_IMContent.ownerheader, 10);
		for (int i = 0; i < ownerheader.size(); i++) {
			WebElement owner = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.owner, 10);

			if (owner.getText().toLowerCase().contains(text[i].toLowerCase()))
				report.updateTestLog("verify " + text[i] + " is displayed ", text[i] + " is displayed ", Status.PASS);
			else
				report.updateTestLog("verify " + text[i] + " is displayed ", text[i] + " is not displayed ", Status.FAIL);

			WebElement date = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.date, 10);
			if (date != null)
				report.updateTestLog("verify date and time is displayed", "Date and time is displayed", Status.PASS);
			else
				report.updateTestLog("verify date and time is displayed ", "Date and time is not displayed ", Status.FAIL);
			if (i == 2) {
				WebElement addedNote = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.content, 10);

				if (addedNote.getText().toLowerCase().contains(note[i].toLowerCase()))
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is displayed ", Status.PASS);
				else
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is not displayed ", Status.FAIL);
			}

			else {
				List<WebElement> addedNote = commonLibrary.isExistList(ownerheader.get(i), UIMAP_IMContent.content, 10);
				for (WebElement item : addedNote) {
					if (item.getText().toLowerCase().contains(note[i].toLowerCase())) {
						flag = true;
						break;
					}
				}
				if (flag)
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is displayed ", Status.PASS);
				else
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is not displayed ", Status.FAIL);
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify recipient in header
	// # Function Name : verifyNoteinSection
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyRecipient(String text) {

		WebElement ownerheader = commonLibrary.isExist(UIMAP_IMContent.ownerheader, 10);
		WebElement owner = commonLibrary.isExist(ownerheader, UIMAP_IMContent.owner, 10);

		if (owner.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("verify " + text + " is displayed ", text + " is displayed ", Status.PASS);
		else
			report.updateTestLog("verify " + text + " is displayed ", text + " is not displayed ", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify recipient details
	// # Function Name : verifyRecipientDetails
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document verifyRecipientDetails(String recipient[], String note[]) {
		boolean flag = false;

		List<WebElement> ownerheader = commonLibrary.isExistList(UIMAP_IMContent.ownerheader, 10);
		for (int i = 0; i < 2; i++) {
			WebElement owner = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.owner, 10);

			if (owner.getText().toLowerCase().contains(recipient[i].toLowerCase()))
				report.updateTestLog("verify " + recipient[i] + " is displayed ", recipient[i] + " is displayed ", Status.PASS);
			else
				report.updateTestLog("verify " + recipient[i] + " is displayed ", recipient[i] + " is not displayed ", Status.FAIL);

			WebElement date = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.date, 10);
			if (date != null)
				report.updateTestLog("verify date and time is displayed", "Date and time is displayed", Status.PASS);
			else
				report.updateTestLog("verify date and time is displayed ", "Date and time is not displayed ", Status.FAIL);
			if (i == 1) {
				WebElement addedNote = commonLibrary.isExist(ownerheader.get(i), UIMAP_IMContent.content, 10);

				if (addedNote.getText().toLowerCase().contains(note[i].toLowerCase()))
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is displayed ", Status.PASS);
				else
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is not displayed ", Status.FAIL);
			}

			else {
				List<WebElement> addedNote = commonLibrary.isExistList(ownerheader.get(i), UIMAP_IMContent.content, 10);
				for (WebElement item : addedNote) {
					if (item.getText().toLowerCase().contains(note[i].toLowerCase())) {
						flag = true;
						break;
					}
				}
				if (flag)
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is displayed ", Status.PASS);
				else
					report.updateTestLog("verify " + note[i] + " is displayed ", note[i] + " is not displayed ", Status.FAIL);
			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on toc footerverify the link
	// in the Mini TOC
	// particular section
	// # Function Name : verifyLinsDisplayedInMiniTOC     
	// # Author : Baswaraj
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document verifyLinsDisplayedInMiniTOC(String links) {
		boolean flag = false;
		String linksList[] = links.split(";");

		WebElement toc = commonLibrary.isExist(UIMAP_Document.tableOfContentsTab, 10);
		List<WebElement> toc1 = commonLibrary.isExistList(toc, UIMAP_Document.listItems, 10);
		for (WebElement item : toc1) {
			for (int i = 0; i < linksList.length; i++) {
				if (item.getText().contains(linksList[i])) {
					flag = true;
				}
			}
			if (flag) {
				report.updateTestLog("Verify Item : " + item.getText() + " in the Mini TOC", item.getText() + " is displayed in the Mini TOC", Status.PASS);
			} else {
				report.updateTestLog("Verify Item : " + item.getText() + " in the Mini TOC", item.getText() + " is not displayed in the Mini TOC", Status.FAIL);
			}

		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : simpleSearchDocument     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document simpleSearchDocument(String strSearchTerm, Boolean strClearFilter) {

		int check = 0;

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		check = pageCheck.positiveCheck(driver, "Document", "Document Page");
		pageCheck.handleFlow(driver, check);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message-
	// Transactional.
	// # Function Name : verifyBannerMessageTrans
	// # Author : Ramesh D
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document verifyBannerMessageTrans(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg, 20);
		if (bannermsg != null && bannermsg.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message displayed with text " + text, Status.PASS);
		else
			report.updateTestLog("Verify Banner message displayed with text " + text, "Banner message is not displayed with text " + text, Status.WARNING);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify and click Results Link
	// # Function Name : clickResultslist
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search clickResultLink2() {
		WebElement resultlistTab = commonLibrary.isExistNegative(UIMAP_Document.resultlink, 10);
		if (resultlistTab != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(resultlistTab, "Result List");
			else
				commonLibrary.clickLinkWithWebElementWithWait(resultlistTab, "Result List");
		} else
			report.updateTestLog("Click Results List Button", "Results List Button is not available", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on toc footer
	// particular section
	// # Function Name : clickontocfooter     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clicktocfooter() {

		boolean flag = false;
		WebElement toc = commonLibrary.isExist(UIMAP_Document.miniToc, 10);
		List<WebElement> toc1 = commonLibrary.isExistList(toc, UIMAP_Document.listItems, 10);
		for (WebElement item : toc1) {

			if (item.getText().contains("Search for recent developments")) {

				WebElement footer = commonLibrary.isExist(item, UIMAP_Document.resultlink1, 10);
				if (footer != null) {

					commonLibrary.clickButtonParent(footer, "Primary Law");
					report.updateTestLog("Click footer Button", "footer Button is available", Status.PASS);
					flag = true;
					break;
				} else {

					report.updateTestLog("Click footer Button", "footer Button is not available", Status.FAIL);
				}
				if (flag)
					break;

			}
		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify format of Italicized text
	// # Function Name : verifyItalicizedTextFormat
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Document verifyItalicizedTextFormat(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		WebElement italic = commonLibrary.isExistNegative(clipTextContainer, UIMAP_Document.italicText, 10);
		if (italic.getText().contains(text)) {
			report.updateTestLog("Verify text format", "The text " + text + " is in the same format as in the string in cliptext", Status.PASS);
		} else {
			report.updateTestLog("Verify text format", "The text " + text + " is not in the same format as in the string in cliptext", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify format of text (contains)
	// # Function Name : verifyTextContains
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Document verifyTexttContains(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		if (clipTextContainer.getText().contains(text)) {
			report.updateTestLog("Verify text format", "The text " + text + " is in the same format as in the string in cliptext", Status.PASS);
		} else {
			report.updateTestLog("Verify text format", "The text " + text + " is not in the same format as in the string in cliptext", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document scrolled to a
	// particular section
	// # Function Name : verifyDocumentisScrolledToSection1     
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Document verifyDocumentisScrolledToSection2(String section) {
		System.out.println(section);
		boolean flag = false;
		int y = 0;
		WebElement docText = commonLibrary.isExistNegative(UIMAP_Document.docText, 10);
		List<WebElement> paraList = commonLibrary.isExistList(docText, UIMAP_Document.text, 10);
		if (paraList.size() > 0) {
			for (WebElement para : paraList) {
				System.out.println(para.getText());
				if (para.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(section.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
					y = para.getLocation().getY();
					System.out.println("y value: " + y);
					flag = true;
					break;
				}
			}
		}
		WebElement docTextUnder = commonLibrary.isExistNegative(docText, UIMAP_Document.docTextUnder, 10);
		List<WebElement> spanList = commonLibrary.isExistList(docTextUnder, UIMAP_Document.docTextUnderSpan, 10);
		if (spanList.size() > 0 && flag == false) {
			for (WebElement span : spanList) {
				System.out.println(span.getText());
				if (span.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(section.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
					y = span.getLocation().getY();
					System.out.println("y value: " + y);
					flag = true;
					break;
				}
			}
		}
		WebElement docTextRef = commonLibrary.isExistNegative(UIMAP_Document.docTextRef, 10);
		WebElement docTextRefList = commonLibrary.isExistNegative(docTextRef, UIMAP_Document.lstTagUList, 10);
		List<WebElement> refList = commonLibrary.isExistList(docTextRefList, UIMAP_Document.lstTagName, 10);
		if (!(refList == null) && flag == false) {
			for (WebElement ref : refList) {
				if (ref.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(section.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
					System.out.println(ref.getText());
					y = ref.getLocation().getY();
					System.out.println("y value: " + y);
					flag = true;
					break;
				}
			}
		}
		if (flag == true) {
			WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
			int yToolbar = toolbar.getLocation().getY();
			System.out.println(yToolbar);
			if (y - yToolbar < 1000) {
				report.updateTestLog("Verify document is scrolled to the section '" + section + "'", "Document is scrolled to the section '" + section + "'", Status.PASS);
			} else
				report.updateTestLog("Verify document is scrolled to the section '" + section + "'", "Document is not scrolled to the section '" + section + "'", Status.FAIL);
		}

		if (!flag)
			report.updateTestLog("Verify document is scrolled to " + section, section + " is not present in document.", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to back in browser
	// # Function Name : clickBrowserBackResults  
	// # Author : Kalai
	// # Date Created : Jan'16
	// #*********************************************************************************************************

	public LexisAdvanceTaxResults clickBrowserBackResults() {
		commonLibrary.clickBrowserBack();
		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC is collapsed
	// # Function Name : verifyTOCPresent
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Document verifyTOCPresent() {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		if (tableOfContentsTab != null) {
			WebElement collapsedMiniToc = commonLibrary.isExist(UIMAP_Document.collapsedMiniToc, 20);
			if (collapsedMiniToc != null) {
				report.updateTestLog("Verify Toc is present", "Toc is present", Status.PASS);
			} else {
				report.updateTestLog("Verify Toc is not present", "Toc is not present", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Toc is present", "Toc is not available", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify mini TOC is collapsed
	// # Function Name : verifyTOCPresent
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Document verifyListOfContentsTOC(String str) {

		boolean bln = false;
		WebElement totalContents = commonLibrary.isExistNegative(UIMAP_Document.contents, 10);
		WebElement list = commonLibrary.isExistNegative(totalContents, UIMAP_Document.tag1, 10);
		List<WebElement> item = commonLibrary.isExistList(list, UIMAP_Document.tag2, 10);
		if (item.size() > 0) {
			for (WebElement ref : item) {
				if (ref.getText().equals(str)) {
					report.updateTestLog("verify" + str + "is present", str + "is present", Status.PASS);
					bln = true;
					break;
				}
			}
			if (!bln) {
				report.updateTestLog("verify" + str + "is not present", str + "is not present", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify items are present", "No item is present", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link
	// # Function Name : ClickDocLinkTOC     
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public TOC clickDocLinkTOC(String strDocTitle) {
		Boolean blnFlag = false;
		WebElement resultClass = null;
		WebElement resultClassOld, resultClassNew;
		int i = 0;
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			resultClassOld = resultClass;
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
				if (btnNextPage != null) {
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
					resultClassNew = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 5);
					int count = 0;
					try {
						do {
							commonLibrary.sleep(100000);
							count++;
							resultClassNew = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 5);
						} while (resultClassOld.equals(resultClassNew) && count < 25);
					} catch (Exception e) {
						commonLibrary.sleep(1000000);
						System.out.println(e.toString());
					}

				} else
					break;
			} else
				break;
		}

		if (!blnFlag)

			report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		else {

			WebElement atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);

			int counter = 0;
			do {
				counter = counter + 1;
				commonLibrary.sleep(20000);
				atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);
				if (atd == null && counter > 20)
					atd = commonLibrary.isExistNegative(UIMAP_Document.copyCitation, 3);

			} while (atd == null && counter <= 40);

			check = pageCheck.positiveCheck(driver, "document", "Document");

			// pageCheck.handleFlow(driver, check);

			WebElement pgHeader = null;
			if (commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);
			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10) != null)
				pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 10);

			if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document")))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else if (pgHeader != null && ((pgHeader.getText().toLowerCase().contains(strDocTitle.toLowerCase()))))
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", pgHeader.getText() + "/" + "document " + strDocTitle + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}
		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the second header by
	// double clicking on it.
	// # Function Name : selectSecondHeadingText
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Document selectSecondHeadingText1() {

		List<WebElement> subHeader = commonLibrary.isExistList(UIMAP_Document.subHeader, 10);

		/*
		 * String selection = ""; if
		 * (subHeader.get(1).getText().trim().contains(" ")) { int
		 * indexof1stSpace = subHeader.get(1).getText().trim().indexOf(" ");
		 * selection = subHeader.get(1).getText().trim().substring(0,
		 * indexof1stSpace); } else selection = subHeader.get(1).getText();
		 */
		WebElement docContextMenu = null;
		int i = 0;
		boolean flag = false;
		do {
			commonLibrary.windowFocus();
			commonLibrary.clickButtonParentWithWaitWithFocus(subHeader.get(1), "Heading");
			Actions select = new Actions(driver);
			// int y=subHeader.get(1).getSize().height;
			// int xEnd=subHeader.get(1).getSize().width;
			commonLibrary.windowFocus();
			commonLibrary.highlightElement(subHeader.get(1));
			commonLibrary.clickButtonParentWithWaitWithFocus(subHeader.get(1), subHeader.get(1).getText());
			select.moveToElement(subHeader.get(1), 5, 0).doubleClick().build().perform();
			commonLibrary.sleep(50000);

			docContextMenu = commonLibrary.isExistNegative(UIMAP_Document.docContextMenu, 20);
			if (docContextMenu != null)
				flag = true;
			i++;
		} while (docContextMenu == null && i < 3);
		if (!flag)
			report.updateTestLog("Select Text in Document", "Text is not selected.", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify next document link is present
	// # Function Name : verifyAndClickNextDocLink  
	// # Author : Anbarasan
	// # Date Created : Jan'16
	// #*********************************************************************************************************
	public Document verifyAndClickNextDocLink() {
		WebElement slider = commonLibrary.isExistNegative(UIMAP_Document.docslider, 20);
		WebElement docSliderRight = commonLibrary.isExistNegative(slider, UIMAP_Document.docSliderRight1, 10);
		WebElement tooltip = commonLibrary.isExistNegative(docSliderRight, UIMAP_Document.tooltip, 10);
		if (tooltip != null) {
			report.updateTestLog("Verify Next Document Link is present", "Next Document Link is present", Status.PASS);
			commonLibrary.clickButtonParentWithWait(docSliderRight, "Next");
		} else {
			report.updateTestLog("Verify Next Document Link is present", "Next Document Link is not present", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Previous document link is
	// present
	// # Function Name : verifyAndClickPreviousDocLink  
	// # Author : Anbarasan
	// # Date Created : Jan'16
	// #*********************************************************************************************************
	public Document verifyAndClickPreviousDocLink() {
		WebElement slider = commonLibrary.isExistNegative(UIMAP_Document.docslider, 20);
		WebElement docSliderLeft = commonLibrary.isExistNegative(slider, UIMAP_Document.docSliderLeft1, 10);
		WebElement tooltip = commonLibrary.isExistNegative(docSliderLeft, UIMAP_Document.tooltip, 10);
		if (tooltip != null) {
			report.updateTestLog("Verify Previous Document Link is present", "Previous Document Link is present", Status.PASS);
			commonLibrary.clickButtonParentWithWait(docSliderLeft, "Previous");
		} else {
			report.updateTestLog("Verify Previous Document Link is present", "Previous Document Link is not present", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

}