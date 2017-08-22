package functionallibraries;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_TOC;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import supportlibraries.*;

public class TOC extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public TOC(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("toc"))
				commonLibrary.sleep(8000);

		} while (!url.contains("toc") && counter < 40);

		if (!driver.getCurrentUrl().contains("toc")) {
			throw new IllegalStateException("toc page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search within the results
	// # Function Name : searchWithinSearch     
	// # Author : Uma
	// # Date Created :Feb'15
	// #*****************************************************************************************************************************
	public TOC searchWithinSearch(String searchTerm) {
		WebElement subSearchPanel = commonLibrary.isExistNegative(UIMAP_TOC.subSearchPanel, 10);
		WebElement subSearchTextBox = commonLibrary.isExistNegative(subSearchPanel, UIMAP_TOC.subSearchTextBox, 10);
		commonLibrary.setDataInTextBox(subSearchTextBox, searchTerm, "Search the table of contents");
		WebElement subSearchButton = commonLibrary.isExistNegative(subSearchPanel, UIMAP_TOC.subSearchButton, 10);
		commonLibrary.clickButtonParentWithWait(subSearchButton, "Search");
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title
	// # Function Name : Verify_DocumentTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public TOC verifyDocumentTitle(String strDocTitle) {

		WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);
		if (eltDocumentHeading != null && eltDocumentHeading.getText().contains(strDocTitle))
			report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.FAIL);

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title
	// # Function Name : Verify_DocumentTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public TOC verifyNoResultsMessage() {

		WebElement noResults = commonLibrary.isExistNegative(UIMAP_Document.noResults, 10);
		if (noResults != null && noResults.getText().contains("No results found for your search"))
			report.updateTestLog("Verify No results found for your search message is displayed", "No results found for your search message is displayed", Status.PASS);
		else
			report.updateTestLog("Verify No results found for your search message is displayed", "No results found for your search message is not displayed", Status.FAIL);

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for log out
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		try {
			generalFunctions.logout();

		} catch (Exception e) {

		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public TOC clickRightSelectOpenNewWindowNegative(WebElement element)// ,String
																		// elementName)
	{

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			Actions oAction = new Actions(driver);
			oAction.moveToElement(element);
			// commonLibrary.highlightElement(element);
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

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for right clicking on Printer Friendly
	// View
	// # Function Name : rightClickPFV_NoOptions     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public TOC rightClickPFVNoOptions() {
		WebElement toolbar = commonLibrary.isExistNegative(UIMAP_TOC.toolbar, 10);
		WebElement printerFriendlyView = commonLibrary.isExistNegative(toolbar, UIMAP_TOC.printerFriendlyView, 10);
		this.clickRightSelectOpenNewWindowNegative(printerFriendlyView);// ,
																		// "Printer Friendly View");
		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search within the results
	// # Function Name : searchWithinSearch     
	// # Author : Uma
	// # Date Created :Feb'15
	// #*****************************************************************************************************************************
	public TOC searchWithinSearch_DropDown(String searchTerm, String option) {

		WebElement subSearchPanel = commonLibrary.isExistNegative(UIMAP_TOC.subSearchPanel, 10);
		if (subSearchPanel != null) {
			WebElement subSearchTextBox = commonLibrary.isExistNegative(subSearchPanel, UIMAP_TOC.subSearchTextBox, 10);
			if (subSearchTextBox != null)
				commonLibrary.setDataInTextBox(subSearchTextBox, searchTerm, "Search the table of contents");

			WebElement subSearchDropdown = commonLibrary.isExistNegative(subSearchPanel, UIMAP_TOC.subSearchDropdown, 10);
			if (subSearchDropdown != null)
				commonLibrary.selectFromListOption(subSearchDropdown, option);
		} else
			report.updateTestLog("Search within search", "Search within search text box is not present", Status.FAIL);

		WebElement subSearchButton = commonLibrary.isExistNegative(subSearchPanel, UIMAP_TOC.subSearchButton, 10);
		if (subSearchButton != null)
			commonLibrary.clickButtonParentWithWait(subSearchButton, "Search");
		else

			report.updateTestLog("Click Search", "Search button is not available", Status.FAIL);
		commonLibrary.sleep(10000);
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to expand traiangle icon
	// # Function Name : expandTriangleicon     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public TOC expandTriangleicon() {
		WebElement icon = commonLibrary.isExist(UIMAP_Document.traiangleicon, 10);
		commonLibrary.clickJS(icon, "triangle icon");

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the document by title
	// # Function Name : selectDocumentByTitle     
	// # Author : Seetha
	// # Date Created : 28 Apr '15
	// #*****************************************************************************************************************************
	public TOC selectDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.cssSelector("ul[class*='toclist']"), 10);
		if (OList != null) {
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			for (i = 0; i < LList.size(); i++) {
				WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='toclink']"), 10);
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

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click print icon and verifying it
	// # Function Name : printAndVerify     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #******************************************************************************************************************************
	public TOC printAndVerify() {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
			String browsername = cap.getBrowserName();
			String version = cap.getVersion();
			String AutoITPath1 = TestPath + "AutoITControls" + Util.getFileSeparator() + "printCancel.exe";

			// WebElement PrintIcon =
			// commonLibrary.isExist(UIMAP_SearchResult.lnkPrint);
			// if(PrintIcon!=null)
			// {
			// if (browsername.equalsIgnoreCase("internet explorer"))
			// commonLibrary.Click_JS(PrintIcon, "Print Icon");
			// else
			// commonLibrary.Click_JS(PrintIcon, "Print Icon");
			// commonLibrary.sleep(3000);
			// }
			//
			// // CLICK <<<USE CURRENT SETTINGS>>> FROM THE DROP DOWN.
			// WebElement Default_Settings =
			// commonLibrary.isExist(UIMAP_SearchResult.lnkUseDefaultSettPrint);
			// commonLibrary.click_JS(Default_Settings, "Use Default Settings");
			generalFunctions.clickDeliverySelectOption("delivery", "printnow");

			commonLibrary.sleep(40000);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				if (driver.getTitle().equalsIgnoreCase("Document Delivery")) {
					commonLibrary.sleep(40000);
					WebElement Delivery_Status = commonLibrary.isExist(UIMAP_Document.header);
					if (Delivery_Status.getText().equalsIgnoreCase("Delivery Complete") || Delivery_Status.getText().equalsIgnoreCase("Processing Delivery")) {
						commonLibrary.sleep(15000);
						if (driver.getTitle().equalsIgnoreCase("Document Delivery")) {
							report.updateTestLog("Complete/Delivery message is displayed once the delivery is complete", "Complete/Delivery message is displayed once the delivery is complete", Status.PASS);
							report.updateTestLog("Verify that secondary browser window is displayed", "Secondary browser window is displayed", Status.PASS);
						}
						if ((browsername.contains("internet") && (version.contains("9") || version.contains("8")))) {

						} else
							Runtime.getRuntime().exec(AutoITPath1);
						commonLibrary.sleep(3000);
						driver.close();
						commonLibrary.smallWait();
						commonLibrary.switchToWindow("toc");
						commonLibrary.sleep(Mwait);
						break;

					}

				}
			}

		} catch (Exception e) {

		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download and verify the documents
	// # Function Name : downloadAndVerify     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public TOC downloadAndVerify(String DocName) {
		try {
			String strAutoIT = "Download.exe";

			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();
			String filePath = dataTable.getData("General_Data", "FilePath");
			// String strAutoIT = "SavePDF.exe";

			Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
			cap.getBrowserName();
			cap.getVersion();
			String AutoITPath = TestPath + "AutoITControls" + Util.getFileSeparator() + strAutoIT;
			generalFunctions.clickDownloadDefaultSettings(AutoITPath, DocName, filePath, false, "", "", "toc");

			// WebElement DownloadIcon =
			// commonLibrary.isExist(UIMAP_SearchResult.lnkDownload);
			// commonLibrary.Click_JS(DownloadIcon, "Download");
			//
			// commonLibrary.sleep(3000);
			//
			// // CLICK <<<USE CURRENT SETTINGS>>> FROM THE DROP DOWN.
			// WebElement Default_Settings =
			// commonLibrary.isExist(UIMAP_SearchResult.lnkUseDefaultSett);
			// commonLibrary.clickButton_Parent_WithWait(Default_Settings,
			// "Use Default Settings");
			// commonLibrary.sleep(30000);
			// if(!browsername.contains("internet"))
			// {
			//
			// Boolean blnFlag = false;
			// for (String winHandle : driver.getWindowHandles()) {
			// driver.switchTo().window(winHandle);
			// commonLibrary.sleep(1000);
			// // driver.manage().window().maximize();
			// if (driver.getTitle().equalsIgnoreCase("Document Delivery")) {
			//
			//
			// report.updateTestLog("Complete/Delivery message is displayed once the download is complete",
			// "Complete/Delivery message is displayed once the download is complete",
			// Status.PASS);
			// report.updateTestLog("Verify that secondary browser window is displayed",
			// "Secondary browser window is displayed", Status.PASS);
			// blnFlag = true;
			// commonLibrary.sleep(50000);
			// WebElement delStatus =
			// commonLibrary.isExist_Negative(UIMAP_Document.deliveryStatus,
			// 10);
			// if (delStatus == null) {
			// commonLibrary.sleep(25000);
			// delStatus =
			// commonLibrary.isExist_Negative(UIMAP_Document.deliveryStatus,
			// 10);
			// }
			// WebElement Delivery_Status = commonLibrary.isExist(delStatus,
			// UIMAP_Document.txtDownloadStatus, 10);
			// System.out.println(Delivery_Status.getText());
			//
			// //
			// if
			// (Delivery_Status.getText().equalsIgnoreCase("Delivery Complete")
			// ||
			// Delivery_Status.getText().equalsIgnoreCase("Processing Delivery"))
			// {
			//
			// // WebElement Document_link =
			// // commonLibrary.isExist(UIMAP_Document.lnkDeliveryDownload);
			// // String Filename = Document_link.getText();
			// String Filename = DocName.replace("$", "_");
			// commonLibrary.sleep(2000);
			// // String Filename = Filename1.replace(" ", "");
			//
			// // String path1 = TestPath + "Downloads";
			// String path1 = filePath;
			// String path2 = path1 + "\\";
			// if(filePath.contains("§"))
			// filePath=filePath.replaceAll("§","_");
			// commonLibrary.FileExists_Delete(filePath, Filename);
			// // String path = TestPath + "Downloads" +
			// // Util.getFileSeparator()+file;
			// if (Filename != null)
			// // if(Document_link!=null)
			// {
			// commonLibrary.windowFocus();
			// report.updateTestLog("Verify that Download is complete",
			// "Download is complete", Status.PASS);
			//
			// if (browsername.contains("firefox")) {
			// // commonLibrary.Click_JS(Document_link,
			// // "Document Link");
			//
			// commonLibrary.sleep(4000);
			// Robot robot = new Robot();
			// robot.keyPress(KeyEvent.VK_ALT);
			// robot.keyPress(KeyEvent.VK_S);
			// // CTRL+Z is now pressed (receiving application
			// // should see a "key down" event.)
			// robot.keyRelease(KeyEvent.VK_S);
			// robot.keyRelease(KeyEvent.VK_ALT);
			// commonLibrary.sleep(1000);
			// robot.keyPress(KeyEvent.VK_ENTER);
			//
			// } else if (browsername.contains("internet") &&
			// version.contains("8")) {
			// // commonLibrary.Click_JS(Document_link,
			// // "Document Link");
			// commonLibrary.sleep(4000);
			// // Runtime.getRuntime().exec(DownloadFile);
			// // commonLibrary.sleep(1000);
			// String[] cmd = { AutoITPath, "Save As", path2, Filename };
			// Runtime.getRuntime().exec(cmd);
			// } else {
			// // commonLibrary.Click_JS(Document_link,
			// // "Document Link");
			// commonLibrary.sleep(3000);
			// Robot robot = new Robot();
			// robot.keyPress(KeyEvent.VK_F6);
			// commonLibrary.sleep(1000);
			// robot.keyPress(KeyEvent.VK_TAB);
			// commonLibrary.sleep(1000);
			// robot.keyPress(KeyEvent.VK_ENTER);
			// }
			// commonLibrary.sleep(20000);
			// // Document_link =
			// // commonLibrary.isExist(UIMAP_Document.lnkDeliveryDownload);
			// // String DocumentName = Document_link.getText();
			//
			// commonLibrary.FileExists(filePath, Filename);
			// String DocPath = filePath + "\\" + Filename + ".PDF";
			// // String DocumentName1 = DocumentName.replace(" ",
			// // "");
			// String DocumentName1 = Filename.replace(" ", "");
			// this.PDFVerification_Path(DocPath, DocumentName1, "Exist");
			// commonLibrary.sleep(4000);
			// //
			// report.updateTestLog("Verify that contents of the delivereddocument is same as that of the content in full document","Verify manually that contents of the delivered document "+Filename+" is same as that of the content in full document from the file in path '"+
			// // path+"'", Status.WARNING);
			// }
			// } else {
			// report.updateTestLog("Verify that Download is complete",
			// "Download is failed", Status.FAIL);
			// }
			//
			// // CLICK ON
			// // "COURT PROCEDURES RULES 2006 PART 3.9 HABEAS CORPUS" LINK
			// // ON THE ACCESS DOCUMENTS DIALOG BOX/BROWSER.
			//
			// // VERIFY THAT CONTENTS OF THE DELIVERED DOCUMENT IS SAME AS
			// // THAT OF THE CONTENT IN FULL DOCUME
			// break;
			// }
			// }
			// if (!blnFlag) {
			// report.updateTestLog("Complete/Delivery message is displayed once the download is complete",
			// "Complete/Delivery message is not displayed once the download is complete",
			// Status.FAIL);
			// report.updateTestLog("Verify that secondary browser window is displayed",
			// "Secondary browser window is not displayed", Status.FAIL);
			// }
			//
			// driver.close();
			// commonLibrary.smallWait();
			// commonLibrary.SwitchToWindow("toc");
			//
			// commonLibrary.sleep(3000);
			// }
			// else
			// {
			// driver.close();
			// report.updateTestLog("Secondary browser",
			// "Secondary browser window is not supported in IE", Status.FAIL);
			// }

		} catch (Exception e) {

		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download and verify the documents
	// # Function Name : downloadAndVerify     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void pdfVerificationPath(String FilePath, String Text, String Exist) {

		if (FilePath.contains("§"))
			FilePath = FilePath.replaceAll("§", "_");
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
	// # Function Description : Function to send email to a recepient
	// # Function Name : sendEmail     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public TOC sendEmail(String Email) {
		// WebElement btnEmail =
		// commonLibrary.isExist(UIMAP_SearchResult.btnEmail);
		// commonLibrary.clickButton_Log_SmallWait(btnEmail, "E-mail");
		generalFunctions.clickDeliverySelectOption("delivery", "email");

		WebElement txtEmail = commonLibrary.isExistNegative(UIMAP_SearchResult.txtEmail, 10);
		commonLibrary.setDataInTextBox(txtEmail, Email, "To");

		WebElement dilog = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 20);
		WebElement btnSubmitEmail = commonLibrary.isExist(dilog, UIMAP_SearchResult.btnSubmitEmail, 20);
		if (btnSubmitEmail != null) {
			commonLibrary.clickLinkWithWebElementWithWait(btnSubmitEmail, "Email");

		} else
			report.updateTestLog("click on Email link", "Email link is NOT clicked", Status.FAIL);

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
				commonLibrary.sleep(70000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
			if (i > 29)
				break;
		}
		boolean blnFlag = false;
		commonLibrary.sleep(70000);
		txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
		if (txtPopUpHeader.getText().contains("Complete"))
			blnFlag = true;
		else
			blnFlag = false;

		if (blnFlag)
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has been sent to: " + Email + "", Status.PASS);
		else
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has not been sent to: " + Email + "", Status.FAIL);

		driver.close();
		commonLibrary.switchToWindow("toc");
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to history footer link
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Seetha
	// # Date Created : Jan'15
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
	// # Function Description : Function to expand any title present within TOC
	// # Function Name : expandTOCtitle     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC expandTOCtitle(String title) {
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 20);
		List<WebElement> titles = commonLibrary.isExistList(tocList, By.tagName("li"), 30);
		for (WebElement item : titles) {
			WebElement tocHeader = commonLibrary.isExist(item, UIMAP_TOC.tocHeader, 30);
			if (tocHeader.getText().equalsIgnoreCase(title) && item.getAttribute("class").contains("expanded")) {
				report.updateTestLog("Expand the title: " + title, title + " is already in expanded status", Status.PASS);
				break;
			} else if (tocHeader.getText().equalsIgnoreCase(title)) {
				WebElement expand = commonLibrary.isExist(item, UIMAP_TOC.expandTitle, 20);
				if(browsername.contains("chrome"))
					commonLibrary.clickButtonParentWithWait(expand, "Expand " + title);
				else
					commonLibrary.clickButtonParentWithWaitJS(expand, "Expand " + title);
				break;
			}
		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and verify the ExpandvCollapse
	// button for the Document Title
	// # Function Name : clickandVerifyExpandCollapseDocumentTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC clickandVerifyExpandCollapseDocumentTitle(String buttonType, String docName) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.tocResult, 10);
		for (int i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), UIMAP_TOC.headerName, 10);
			strDocTitle = lnkTitle.getText();
			if (strDocTitle.trim().equalsIgnoreCase(docName.trim())) {
				WebElement expandCollapseButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.expandCollapse, 10);

				if (expandCollapseButton != null) {
					if (("Expanded".toLowerCase()).contains(buttonType.toLowerCase())) {
						commonLibrary.clickButtonParentWithWait(expandCollapseButton, "Expand");
						if (LList.get(i).getAttribute("class").equalsIgnoreCase("expanded"))
							blnFlag = true;
						break;
					}
				} else {
					if (("Collapsed".toLowerCase()).contains(buttonType.toLowerCase())) {
						commonLibrary.clickButtonParentWithWait(expandCollapseButton, "collapsed");
						if (LList.get(i).getAttribute("class") == "collapsed")
							blnFlag = true;
						break;
					}
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Click the " + buttonType + " button for the document" + docName, docName + " document is  " + buttonType + " ", Status.PASS);
		else
			report.updateTestLog("Click the " + buttonType + " button for the document" + docName, docName + "document is not  " + buttonType + " ", Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify teh highlighted terms
	// button for the Document Title
	// # Function Name : verifyTermsHighlighted     
	// # Author : Arvind
	// # Date Created : 11 May'15
	// #*****************************************************************************************************************************
	public TOC verifyTermsHighlighted(String termToVerify) {
		boolean flg = false;
		WebElement divSection = commonLibrary.isExist(UIMAP_TOC.divSection, 20);
		if (divSection != null) {
			List<WebElement> termsAll = commonLibrary.isExistList(UIMAP_TOC.termHighlighted, 20);
			if (termsAll.size() > 0) {
				for (int i = 0; i < termsAll.size(); i++) {
					if (termsAll.get(i).getText().toLowerCase().contains(termToVerify.toLowerCase()) || termsAll.get(i).getText().toLowerCase().contains(termToVerify.substring(0, 2).toLowerCase())) {
						flg = true;
						break;
					}
					if (flg)
						break;
				}
			}
		}
		if (flg)
			report.updateTestLog("Verify " + termToVerify + " bolded and highlighted in result page", termToVerify + " bolded and highlighted", Status.PASS);
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Printer Friendly View icon
	// # Function Name : clickPrinterFrndlyView     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TocPrint clickPrinterFrndlyView() {

		generalFunctions.clickDeliverySelectOption_New("delivery", "Printer-friendly view");
		/*
		 * WebElement toolBar = commonLibrary.isExist(UIMAP_TOC.toolbar, 10);
		 * WebElement printView = commonLibrary.isExist(toolBar,
		 * UIMAP_TOC.printerFriendlyView, 10); if (printView != null) {
		 * commonLibrary.clickButtonParentWithWait(printView,
		 * "Printer Friendly View"); commonLibrary.switchToWindow("tocprint");
		 * 
		 * } else report.updateTestLog("Click on Printer Friendly View icon",
		 * "Printer Friendly View icon is not present", Status.FAIL);
		 */
		commonLibrary.switchToWindow("tocprint");
		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the add to search button near
	// the Document Title
	// # Function Name : clickAddToSearchButtonDocumentTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC clickAddToSearchButtonDocumentTitle(String buttonType, String docName) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
		for (int i = 0; i < LList.size(); i++) {
			strDocTitle = LList.get(i).getText();
			if (strDocTitle.trim().equalsIgnoreCase(docName.trim())) {
				WebElement addToSearch = commonLibrary.isExist(LList.get(i), UIMAP_TOC.addToSearch, 10);

				if (addToSearch != null) {
					commonLibrary.clickButtonParentWithWait(addToSearch, "Add to search");
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Click the " + buttonType + " button for the document" + docName, "The " + buttonType + " button for the document " + docName + " displays in Narrowed by section in left pane", Status.PASS);
		else
			report.updateTestLog("Click the " + buttonType + " button for the document" + docName, "The " + buttonType + " button for the document " + docName + " does not displays in Narrowed by section in left pane", Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the add to search button near
	// the Document Title
	// # Function Name : verifyAddToSearchButtonDocumentTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC verifyAddToSearchButtonDocumentTitle(String docName) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
		for (int i = 0; i < LList.size(); i++) {
			strDocTitle = LList.get(i).getText();
			if (strDocTitle.trim().equalsIgnoreCase(docName.trim())) {
				WebElement addToSearch = commonLibrary.isExist(LList.get(i), UIMAP_TOC.addToSearch, 10);

				if (addToSearch != null) {
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify the Add to search button exist for the document" + docName, "The Add to search button exist for the document " + docName, Status.PASS);
		else
			report.updateTestLog("Verify the Add to search button exist for the document" + docName, "The Add to search button does not exist for the document " + docName, Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the close button near the
	// Document Title
	// # Function Name : verifyCloseButtonNearDocumentTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC verifyCloseButtonNearDocumentTitle(String docLink) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
		for (int i = 0; i < LList.size(); i++) {
			strDocTitle = LList.get(i).getText();
			if (strDocTitle.trim().equalsIgnoreCase(docLink.trim())) {
				WebElement closeButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.closeAddSearch, 10);

				if (closeButton != null) {
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify the [X] button display next to " + docLink, "The [X] button is displayed next to " + docLink, Status.PASS);
		else
			report.updateTestLog("Verify the [X] button display next to " + docLink, "The [X] button does not displayed next to " + docLink, Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the close button near the
	// Document Title
	// # Function Name : verifyCloseButton_Negative     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC verifyCloseButton_Negative(String docLink) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
		for (int i = 0; i < LList.size(); i++) {
			strDocTitle = LList.get(i).getText();
			if (strDocTitle.trim().equalsIgnoreCase(docLink.trim())) {
				WebElement closeButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.closeAddSearch, 10);

				if (closeButton == null) {
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify the [X] button is not display next to " + docLink, "The [X] button is not displayed next to " + docLink, Status.PASS);
		else
			report.updateTestLog("Verify the [X] button is not display next to " + docLink, "The [X] button displayed next to " + docLink, Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the close button near the
	// Document Title
	// # Function Name : verifyCloseButtonNearDocumentTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC clickCloseButtonNearDocumentTitle(String docName) {
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
		for (int i = 0; i < LList.size(); i++) {
			strDocTitle = LList.get(i).getText();
			if (strDocTitle.trim().equalsIgnoreCase(docName.trim())) {
				WebElement closeButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.closeAddSearch, 10);

				if (closeButton != null) {
					commonLibrary.clickButtonParentWithWait(closeButton, "Close Button");
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Click the [X] button display next to " + docName, "The [X] button is Clicked next to " + docName, Status.PASS);
		else
			report.updateTestLog("Click the [X] button display next to " + docName, "The [X] button does not Clicked next to " + docName, Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the star status
	// # Function Name : clickandverifystarFavouritestatus     
	// # Author : karthi
	// # Date Created :11 May '15
	// #*****************************************************************************************************************************
	public TOC clickandverifystarFavouritestatus(String star_state) {
		WebElement btnStar = commonLibrary.isExist(UIMAP_TOC.btnstarverify, 20);
		if (btnStar != null) {
			if (star_state.equals("Select star")) {
				WebElement Favstaron = commonLibrary.isExist(UIMAP_TOC.btnstarFavoff, 20);
				if (Favstaron != null) {
					commonLibrary.clickButtonParentWithWait(Favstaron, star_state);
					report.updateTestLog("Verify the Favourite star Selected", "Favourite star is selected as expected", Status.DONE);
				}
			} else if (star_state.equals("deselect star")) {
				WebElement header = commonLibrary.isExist(UIMAP_TOC.header, 20);
				WebElement Favstaroff = commonLibrary.isExist(header, UIMAP_TOC.btnstarFavon, 20);
				if (Favstaroff != null) {
					commonLibrary.clickButtonParentWithWait(Favstaroff, star_state);
					report.updateTestLog("Verify the Favourite star UnSelected", "Favourite star is not  selected as expected", Status.DONE);
				}

			} else if (star_state.equals("verify star")) {
				if (btnStar.getAttribute("class").contains("la-FavoriteFull")) {
					report.updateTestLog("Verify the Favourite star status", "Favourite star is selected as expected", Status.PASS);
				} else if (btnStar.getAttribute("class").contains("la-FavoriteEmpty")) {
					report.updateTestLog("Verify the Favourite star status", "Favourite star is not selected as expected", Status.PASS);
				}
			}
		}

		return new TOC(scriptHelper);
	}

	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
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
							Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
							String browsername = cap.getBrowserName();

							if (browsername.equalsIgnoreCase("internet explorer"))
								commonLibrary.clickJS(button, strMenuName);
							else
								commonLibrary.clickButtonParentWithWait(button, strMenuName);

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
	// # Function Description : Function to click and verify the filter details
	// in TOC page
	// # Function Name : applySearchFilter     
	// # Author : karthi
	// # Date Created :12 May '15
	// #*****************************************************************************************************************************
	public TOC applySearchFilter(String strToolbarMenuName, String strCondentTypes, String strlistname, String startstatus, String closestate, boolean state, boolean rffstate, boolean deleteflg) {

		String[] arrCondentType = strCondentTypes.split(";");
		if (strCondentTypes.toLowerCase().contains("u.s. federal")) {
		}
		if (strCondentTypes.toLowerCase().contains("all states")) {
		}
		// Boolean blnFlag=false; Modified by Pratik
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);

		switch (strToolbarMenuName) {

		case "Recent & Favorites": {

			Boolean blnFlag2 = false;
			Boolean blnFlag1 = false;
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_Home.divClassrecentandfav, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0) {

						for (WebElement item : lstTagListItems) {
							commonLibrary.isExist(item, UIMAP_Home.divtag, 20);
							if (item.getText().contains(strlistname)) {
								report.updateTestLog("Verify the source name of the " + strlistname + " displayed", "Source name of the Nebraska Code is displayed as expected.", Status.PASS);
								List<WebElement> spanlists = commonLibrary.isExistList(item, UIMAP_Home.spantag, 20);
								for (int k = 0; k <= spanlists.size(); k++) {
									WebElement btnstar = commonLibrary.isExist(spanlists.get(k), UIMAP_Home.item, 20);

									if (startstatus.equalsIgnoreCase("Not Selected")) {
										if (btnstar.getAttribute("class").contains("la-FavoriteEmpty")) {
											report.updateTestLog("Verify Favourite star is not selected", "Favourite star is not selected as expected.", Status.PASS);
											blnFlag1 = true;
										} else {
											report.updateTestLog("Verify Favourite star is not selected", "Favourite star is  selected as expected.", Status.FAIL);
											blnFlag1 = true;
										}
									} else if (startstatus.equals("Selected")) {

										if (btnstar.getAttribute("class").contains("la-FavoriteFull")) {
											report.updateTestLog("Verify Favourite star is selected", "Favourite star is selected as expected.", Status.PASS);
											blnFlag1 = true;
										} else {
											report.updateTestLog("Verify Favourite star is selected", "Favourite star is not selected as expected.", Status.FAIL);
											blnFlag1 = true;
										}
									}
									if (blnFlag1) {
										break;
									}

								}
								if (blnFlag1) {
									break;
								}

							}
						}

						for (WebElement item : lstTagListItems) {
							commonLibrary.isExist(item, UIMAP_Home.divtag, 20);
							if (item.getText().contains(strlistname)) {
								WebElement btnstar1 = commonLibrary.isExist(item, UIMAP_TOC.btnclose, 20);
								if (btnstar1 != null && rffstate) {

									if (btnstar1.getAttribute("class").contains("la-CloseRemove")) {
										commonLibrary.clickButtonParentWithWait(btnstar1, "X icon for Nebraska Code");
										WebElement div1 = commonLibrary.isExist(UIMAP_TOC.divpop, 20);
										WebElement Btnpopheader = commonLibrary.isExist(div1, UIMAP_TOC.btnheader, 20);
										WebElement Btnpop = commonLibrary.isExist(Btnpopheader, UIMAP_TOC.btnpop, 20);
										WebElement btncancel = commonLibrary.isExist(Btnpop, UIMAP_TOC.btncancel, 20);
										commonLibrary.clickButtonParentWithWait(btncancel, "cancel");
										break;
									}
								} else if (btnstar1 != null && rffstate && deleteflg) {

									if (btnstar1.getAttribute("class").contains("la-CloseRemove")) {
										commonLibrary.clickButtonParentWithWait(btnstar1, "X icon for Nebraska Code");
										WebElement div1 = commonLibrary.isExist(UIMAP_TOC.divpop, 20);
										WebElement Btnpopheader = commonLibrary.isExist(div1, UIMAP_TOC.btnheader, 20);
										WebElement Btnpop = commonLibrary.isExist(Btnpopheader, UIMAP_TOC.btnpop, 20);
										WebElement btndelete = commonLibrary.isExist(Btnpop, UIMAP_TOC.btndelete, 20);
										if (btndelete != null) {
											report.updateTestLog("Verify the Delete button is displayed in  popup window", "Delete button is displayed as expected.", Status.PASS);
										} else {
											report.updateTestLog("Verify the Delete button is displayed in  popup window", "Delete button is not displayed as expected.", Status.FAIL);
										}

										break;
									}
								} else if (btnstar1 != null) {
									report.updateTestLog("Verify the close button exists", "Close button is displayed as expected.", Status.PASS);
									blnFlag2 = true;

								} else {
									report.updateTestLog("Verify the close button exists", "Close button is not displayed as expected.", Status.FAIL);
									blnFlag2 = true;
								}

								if (blnFlag2) {
									WebElement menu = commonLibrary.isExist(UIMAP_TOC.menubar, 20);
									WebElement closebtn = commonLibrary.isExist(menu, UIMAP_TOC.cancelbtn, 20);
									commonLibrary.clickButtonParentWithWait(closebtn, "close filter");
									break;
								}
							}

						}

					}
				}

			}

		}
		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and verify the filter details
	// in TOC page
	// # Function Name : applySearchFilter     
	// # Author : karthi
	// # Date Created :12 May '15
	// #*****************************************************************************************************************************
	public Sources navigateToAllSources() {
		Boolean blnFirst = false;
		WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
		commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
		try {
			commonLibrary.sleep(Mwait);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		if (divIdBrowserMenu != null) {
			WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
			if (lstTagListItems.size() > 0)
				for (WebElement li : lstTagListItems) {
					if (li.getText().contains("Sources")) {
						blnFirst = true;
						WebElement button = commonLibrary.isExistNegative(li, UIMAP_Home.btnTypeButton, 10);
						commonLibrary.clickButtonParentWithWait(button, "Sources");
						break;
					}

				}
			if (!blnFirst)
				report.updateTestLog("Click on Sources ", "Sources not present", Status.FAIL);
		}
		WebElement allSources = commonLibrary.isExistNegative(UIMAP_Home.allSources, 10);
		commonLibrary.clickLinkWithWebElementWithWait(allSources, "All Sources");
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and verify the filter details
	// in TOC page
	// # Function Name : applySearchFilter     
	// # Author : karthi
	// # Date Created :12 May '15
	// #*****************************************************************************************************************************
	public TOC verifyTocPageElements() {
		// Verify TOC list
		WebElement toclist = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		if (toclist != null)
			report.updateTestLog("Verify Toc nodes list", "Toc nodes list is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Toc nodes list", "Toc nodes list is not displayed", Status.FAIL);

		// Verify Expand/collapse button exist at the left side of each node
		// addToSearch
		boolean blnExpandFlag = true;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		List<WebElement> LList = commonLibrary.isExistList(tocList, UIMAP_TOC.tocResult, 10);
		for (int i = 0; i < LList.size(); i++) {
			WebElement expandCollapseButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.expandCollapse, 10);
			WebElement addToSearchButton = commonLibrary.isExist(LList.get(i), UIMAP_TOC.addToSearch, 10);

			if (expandCollapseButton != null) {
				blnExpandFlag = false;
				break;
			}
			if (addToSearchButton != null) {
				break;
			}
		}
		if (blnExpandFlag)
			report.updateTestLog("Verify Expand/collapse button", "Expand/collapse button exist at the left side of each node", Status.PASS);
		else
			report.updateTestLog("Verify Expand/collapse button", "Expand/collapse button dose not exists at the left side of each node", Status.FAIL);

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify RFF Filters
	// # Function Name : verifyRFFFilters     
	// # Author : Ram
	// # Date Created :May '15
	// #*****************************************************************************************************************************
	public TOC verifyRecentlySelectedDisplayedAtTop(String RecentList) {
		int count = 0;
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
		if (mnuFilterToolBar != null) {
			WebElement RecentFavorites = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.btnIdRecentFavorites, 20);
			commonLibrary.clickButtonParentWithWait(RecentFavorites, "Recent & Favorites");
		}

		WebElement RecentFav = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);

		List<WebElement> RecentFavList = commonLibrary.isExistList(RecentFav, UIMAP_Home.lstTagListItems, 20);
		for (int i = 0; i < RecentFavList.size(); i++) {
			if (RecentFavList.get(i).getText().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(RecentList.toLowerCase().replaceAll(" ", ""))) {
				count = i;
				break;
			}
		}
		if (count == 0) {
			report.updateTestLog("Verify " + RecentList + " is displayed at the top", "" + RecentList + " is displayed at the top", Status.PASS);
		} else {
			report.updateTestLog("Verify " + RecentList + " is displayed at the top", "" + RecentList + " is not displayed at the top", Status.FAIL);
		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify RFF Filters
	// # Function Name : verifyRFFFilters     
	// # Author : Ram
	// # Date Created :May '15
	// #*****************************************************************************************************************************
	public TOC verifyRFFFilters(String rfflink) {

		WebElement favouritefilter = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);
		WebElement ullist = commonLibrary.isExist(favouritefilter, UIMAP_Home.lstTagUList, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist, UIMAP_Home.lstTagListItems, 20);
		WebElement button = commonLibrary.isExist(lilist.get(0), UIMAP_Home.lnkLinks, 20);
		if (browsername.contains("internet")) {
			commonLibrary.highlightElement(button);
			commonLibrary.clickJSMouseMove(button, button.getText());
		} else
			commonLibrary.clickButtonParentWithWait(button, rfflink);

		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.preFiltersDiv, 20);
		WebElement headerinFilter = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.headerinFilter, 20);
		WebElement currentFilter = commonLibrary.isExist(headerinFilter, UIMAP_Home.currentFilterSelected, 20);
		WebElement deleteFilter = commonLibrary.isExist(currentFilter, UIMAP_Home.deleteFilter, 20);
		if (deleteFilter.getText().contains(rfflink)) {
			report.updateTestLog("Verify Recent & Favourite Filter", rfflink + " is present", Status.PASS);
		} else {
			report.updateTestLog("Verify Recent & Favourite Filter", rfflink + " is not present", Status.FAIL);
		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document link with highlighted
	// terms
	// # Function Name : clickDocLinkWithTermsHighlighted     
	// # Author : Anbu
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkWithTermsHighlighted(String term) {
		Boolean flag = false;
		String[] terms = term.split(";");
		WebElement resultClass = null;
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
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								List<WebElement> termsAll = commonLibrary.isExistList(document, UIMAP_TOC.termHighlighted, 20);
								if (termsAll.size() > 0) {
									for (int j = 0; j < termsAll.size(); j++) {
										if (termsAll.get(j).getText().toLowerCase().contains(terms[0].toLowerCase())) {
											for (int k = 0; k < termsAll.size(); k++) {
												if (termsAll.get(k).getText().toLowerCase().contains(terms[1].toLowerCase())) {
													commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
													flag = true;
													break;
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

					}

				}
			}
			if (!flag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}
		if (!flag) {
			report.updateTestLog("Click document with terms highlighted", "Documents does not have the highlighted terms", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check paragraph is clickable
	// # Function Name : verifyParagraph     
	// # Author : Anbu
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public TOC verifyParagraph() {
		Boolean flag = false;
		boolean flag1 = false;
		WebElement resultClass = null;
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
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
							if (lnkDocument != null) {
								WebElement paragraph = commonLibrary.isExist(document, UIMAP_TOC.paragraph, 20);
								if (paragraph != null) {
									report.updateTestLog("Verify Best paragraph is displayed", "Best paragraph is displayed", Status.PASS);
									flag = true;
									WebElement paragraphLink = commonLibrary.isExist(paragraph, UIMAP_TOC.paragraphLink, 20);
									if (paragraphLink != null) {
										report.updateTestLog("Verify Best Paragraph is clickable", "Best paragraph is clickable", Status.PASS);
										flag1 = true;
										break;
									}
								}

							}
						}

					}

				}
			}
			if (!flag1) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}
		if (!flag1) {
			report.updateTestLog("Verify Best Paragraph is clickable", "Best paragraph is not clickable", Status.FAIL);
		}
		if (!flag) {
			report.updateTestLog("Verify Best paragraph is displayed", "Best paragraph is not displayed", Status.FAIL);
		}
		return new TOC(scriptHelper);
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
	// # Function Description : Function to perform search within toc
	// # Function Name : searchWithinTOC
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public TOC searchWithinTOC(String searchTerm, String dropdownOption) {

		try {

			WebElement searchPanel = commonLibrary.isExist(UIMAP_TOC.searchPanel, 10);
			if (searchPanel != null) {
				// Enter search term
				WebElement searchTextBox = commonLibrary.isExist(searchPanel, UIMAP_TOC.searchTextBox, 10);
				if (searchTextBox != null) {

					commonLibrary.setDataInTextBox(searchTextBox, searchTerm, "Search within table of contents");
					if (!dropdownOption.equals("")) {
						// Select search option
						WebElement selectFromDd = commonLibrary.isExist(searchPanel, UIMAP_TOC.selectFromDd, 10);
						if (selectFromDd != null)

							commonLibrary.selectByVisibleText(selectFromDd, dropdownOption);
					}
					// Click on search button
					WebElement searchButton = commonLibrary.isExistNegative(searchPanel, UIMAP_TOC.searchButton, 30);
					if (searchButton != null) {

						commonLibrary.clickButtonParentWithWait(searchButton, "Search");
					} else

						report.updateTestLog("Click on Search button", "Search button i snot available", Status.FAIL);

				} else
					report.updateTestLog("Search within table of contents text box", "Search within table of contents text box not available", Status.FAIL);

			} else
				report.updateTestLog("Search within table of contents text box", "Search within table of contents text box not available", Status.FAIL);

		} catch (StaleElementReferenceException e) {

		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to a particular page and
	// logging out
	// # Function Name : navigateToParticularPageAndLogout     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home navigateToParticularPageAndLogout(String page) {

		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		switch (page) {

		case "research": {

			WebElement btnLexisAdvanceResearch = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnLexisAdvanceResearch, "Lexis Advance® Research");
			else
				commonLibrary.clickLinkWithWebElement(btnLexisAdvanceResearch, "Lexis Advance® Research");

			WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (currentProduct.getText().toLowerCase().contains("research")) {
				report.updateTestLog("Research landing page is displayed", "Research landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Research landing page is displayed", "Research landing page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance® Research logo displays in the Top left corner of Global menu", "Lexis Advance® Research logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research logo displays in the Top left corner of Global menu", "Lexis Advance® Research logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Research");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Research");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}

			break;
		}

		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to a particular page and
	// logging out
	// # Function Name : navigateToParticularPageAndLogout     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home navigateToParticularPage(String page) {

		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		switch (page) {

		case "research": {

			WebElement btnLexisAdvanceResearch = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnLexisAdvanceResearch, "Lexis Advance® Research");
			else
				commonLibrary.clickLinkWithWebElement(btnLexisAdvanceResearch, "Lexis Advance® Research");

			WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (currentProduct.getText().toLowerCase().contains("research")) {
				report.updateTestLog("Research landing page is displayed", "Research landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Research landing page is displayed", "Research landing page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance® Research logo displays in the Top left corner of Global menu", "Lexis Advance® Research logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research logo displays in the Top left corner of Global menu", "Lexis Advance® Research logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Research");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Research");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}

			break;
		}

		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if Best paragraph is repeated
	// for TOC search within result documents
	// # Function Name : verifyBestParagraphOneTimePresence    
	// # Author : Nancy
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public TOC verifyBestParagraphOneTimePresence() {

		WebElement tocResultsList = commonLibrary.isExist(UIMAP_TOC.resultslist, 10);
		List<WebElement> TOCdocRange = commonLibrary.isExistList(tocResultsList, UIMAP_TOC.resultListClass, 10);

		boolean flag = false;

		// int n=50;

		for (int i = 0; i <= TOCdocRange.size() - 1; i++) {

			List<WebElement> article = commonLibrary.isExistList(TOCdocRange.get(i), UIMAP_TOC.paragraph, 10);

			int bpcount = article.size();

			if (bpcount == 1)
				flag = true;

			else {
				List<WebElement> teaser = commonLibrary.isExistList(TOCdocRange.get(i), UIMAP_TOC.eachTeaser, 10);
				for (int j = 0; j <= bpcount; i++) {
					for (int k = 1; k <= bpcount; k++) {

						String a = teaser.get(j).getText();
						String b = teaser.get(k).getText();

						if (a != b)
							flag = true;
					}
				}
			}

		}

		if (flag)

			report.updateTestLog("The documents in the TOC results list current page should have unique articles / best paragraph", "All the documents in the TOC results list current page has unique articles / best paragraph", Status.DONE);

		else
			report.updateTestLog("One or more documents in the TOC results list current page should have repeated articles / best paragraph", "One or more documents in the TOC results list current page has repeated articles / best paragraph", Status.FAIL);

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the landing page by checking
	// the text
	// # Function Name : verifyTOCLandingPage    
	// # Author : Jubin
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public TOC verifyTOCLandingPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.tocHeader, 20);
		String pageNameFinal = pageName.substring(25, pageName.length());
		if (CurrentProduct.getText().toLowerCase().contains(pageNameFinal.toLowerCase())) {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);
		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify TOC title
	// # Function Name : verifyTocTitle    
	// # Author : Aravind Russell V
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public TOC verifyTocTitle(String toctit) {

		WebElement header = commonLibrary.isExist(UIMAP_TOC.header, 10);
		WebElement pagewrapper = commonLibrary.isExist(header, UIMAP_TOC.pagewrapper, 10);
		// WebElement spamquery = commonLibrary.isExist(pagewrapper,
		// UIMAP_TOC.spamquery, 10);
		if (pagewrapper != null) {
			if (pagewrapper.getText().toLowerCase().contains(toctit.toLowerCase())) {
				report.updateTestLog("Verify " + toctit + "TOC page has displayed", toctit + "TOC page has been displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify " + toctit + "TOC page has displayed", toctit + "TOC page not displayed", Status.FAIL);
			}
		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : clickBrowserBackToPractice
	// # Author : Aravind Russell V
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************

	public Practice clickBrowserBackToPractice() {
		commonLibrary.clickBrowserBack();
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title link
	// # Function Name : clickDocLink
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document clickDocLink1(String strDocTitle) {
		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : clickBrowserBackToPractice
	// # Author : Aravind Russell V
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickBrowserBackToLAT() {
		commonLibrary.clickBrowserBack();
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Teaser Text
	// # Function Name : clickTeaserText     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document clickTeaserText(String text) {
		boolean flag = false;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		String docTitle = null;
		if (OListResult != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
			if (OListItems.size() > 0) {
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null) {
						docTitle = eleDocTitle.getText();
						WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserText, 20);

						if (docContent != null) {
							if (docContent.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().contains(text.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
								commonLibrary.clickLinkWithWebElementWithWait(docContent, "Teaser Text");
								report.updateTestLog("Click teaser text corresponding to the document " + docTitle, "Clicked on teaser text corresponding to the document " + docTitle, Status.PASS);
								WebElement atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);

								int counter = 0;
								do {
									counter = counter + 1;
									commonLibrary.sleep(20000);
									atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);
									if (atd == null && counter > 20)
										atd = commonLibrary.isExistNegative(UIMAP_Document.copyCitation, 3);

								} while (atd == null && counter <= 40);
								flag = true;
								break;
							}
						}

					}

				}
			}
		}
		if (!flag)
			report.updateTestLog("Click teaser text", "Not clicked on teaser text", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to get the results list values
	// # Function Name : getResultList
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public List<String> getResultList() {
		List<String> resultListValue = null;
		WebElement result = commonLibrary.isExist(UIMAP_TOC.resultList, 20);
		List<WebElement> resultListDoc = commonLibrary.isExistList(result, UIMAP_TOC.resultListDoc, 20);

		if (result != null) {
			resultListValue = new ArrayList<String>();
			for (int i = 0; i <= 101; i++) {
				String textValue = resultListDoc.get(i).getText().trim().toLowerCase();
				if (textValue != null) {
					resultListValue.add(textValue);

				}
			}
		}

		return resultListValue;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to display the report
	// # Function Name : printreport
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public TOC printreport(boolean flag) {
		if (flag) {
			report.updateTestLog("Verify result list displays same in TOC page", "Result list displays same in TOC page", Status.PASS);
		} else {
			report.updateTestLog("Verify result list displays same in TOC page", "Result list displays not same in TOC page", Status.FAIL);
		}
		return new TOC(scriptHelper);

	}

}
