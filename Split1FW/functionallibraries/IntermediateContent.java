package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_IMContent;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_WorkFolders;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

public class IntermediateContent extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private PageCheck pageCheck = new PageCheck(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public IntermediateContent(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("intermediatecontent"))
				commonLibrary.sleep(5000);

		} while (!url.contains("intermediatecontent") && counter < 40);

		if (!driver.getCurrentUrl().contains("intermediatecontent")) {
			throw new IllegalStateException("Intermediate Content page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on ReRun Saved Search
	// # Function Name : clickReRunSavedSearch     
	// # Author : Divakar
	// # Date Created : apr'15
	// #*****************************************************************************************************************************
	public Search clickReRunSavedSearch() {
		WebElement reRunSearch = commonLibrary.isExist(UIMAP_IMContent.reRunSearch, 10);
		if (reRunSearch != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(reRunSearch, "Re-run saved search");
			else
				commonLibrary.clickButtonParentWithWait(reRunSearch, "Re-run saved search");
		} else {
			report.updateTestLog("Click 'Re-run saved search': ", "'Re-run saved search button is not clicked'", Status.FAIL);
		}
		int check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the researchmap
	// # Function Name : viewmap_researchmap     
	// # Author : karthi
	// # Date Created : apr'15
	// #*****************************************************************************************************************************
	public ResearchMap viewmap() {
		WebElement viewmap = commonLibrary.isExist(UIMAP_IMContent.viewmap, 10);
		if (viewmap != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(viewmap, "View-map");
			else
				commonLibrary.clickButtonParentWithWait(viewmap, "View-map");
		} else {
			report.updateTestLog("Click 'View map': ", "'view-map link  is not clicked'", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link and pop
	// links
	// # Function Name : clickDocumentLink     
	// # Author : karthi
	// # Date Created : apr'15
	// #*****************************************************************************************************************************
	public ResearchMap clickDocumentLink(String searchTerm, String docName1, String docName2, String popuplink) {

		boolean blnFlag = false, blnFlag1 = false;

		List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
		for (WebElement trail : lstResearchThread) {

			WebElement trailHeader = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.header4, 10);
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
				WebElement trailHeader = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.header4, 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {

					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);
					WebElement doclink = commonLibrary.isExistNegative(attachmentContent, UIMAP_ResearchMap.doclnk, 10);

					if (doclink != null && doclink.isDisplayed()) {
						WebElement link = commonLibrary.isExistNegative(doclink, UIMAP_ResearchMap.lnkLinks, 10);
						commonLibrary.clickButtonLogSmallWait(link, docName1);
						selecthisdoctofindsimilar(popuplink);
						blnFlag = true;

						commonLibrary.sleep(5000);

						closedocframe();
						break;
					}
				}
			}
		}

		if (blnFlag1) {
			lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			for (WebElement trail : lstResearchThread) {
				WebElement trailHeader = commonLibrary.isExistNegative(trail, By.tagName("h4"), 10);
				if (trailHeader.getText().toLowerCase().contains(searchTerm.toLowerCase())) {

					WebElement attachmentContent = commonLibrary.isExistNegative(trail, UIMAP_ResearchMap.attachmentContent, 10);

					List<WebElement> doclinks = commonLibrary.isExistList(attachmentContent, UIMAP_ResearchMap.doclnk1, 10);

					if (doclinks != null) {
						commonLibrary.clickButtonParentWithWait(doclinks.get(1), docName2);
						selecthisdoctofindsimilar(popuplink);
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
	// # Function Description : Function to close the popup frame
	// # Function Name : closedocframe     
	// # Author : karthi
	// # Date Created : apr'15
	// #*****************************************************************************************************************************
	public ResearchMap closedocframe() {

		WebElement doclcose = commonLibrary.isExist(By.cssSelector("a[class='closePopup icon la-CloseRemove']"));
		if (doclcose != null) {
			commonLibrary.clickButtonParentWithWaitJS(doclcose, "closedoc");
		}

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify frame document details
	// # Function Name : verifyframedocdetails     
	// # Author : uma
	// # Date Created : apr'15
	// #*****************************************************************************************************************************

	public ResearchMap verifyframedocdetails(String strlink, String linkaction) {
		boolean blnFlag = false;
		;
		WebElement div = commonLibrary.isExist(By.cssSelector("div[class*='researchThreadPopup']"), 10);
		if (div != null) {

			WebElement divframe = commonLibrary.isExist(div, UIMAP_IMContent.docframe, 20);
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
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", strlink + " link  clicked.", Status.PASS);
		} else {
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", strlink + " link not  clicked.", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to find similar documents
	// # Function Name : selecthisdoctofindsimilar     
	// # Author : uma
	// # Date Created : apr'15
	// #*****************************************************************************************************************************

	public ResearchMap selecthisdoctofindsimilar(String strlink) {
		boolean blnFlag = false;
		;
		WebElement div = commonLibrary.isExist(By.cssSelector("div[class*='researchThreadPopup']"), 10);
		if (div != null) {

			WebElement divframe = commonLibrary.isExist(div, UIMAP_IMContent.docframe, 20);
			if (divframe != null) {
				List<WebElement> contentlist = commonLibrary.isExistList(divframe, By.tagName("li"), 10);
				for (WebElement list : contentlist) {

					List<WebElement> contentLink = commonLibrary.isExistList(list, By.tagName("a"), 20);
					for (WebElement item : contentLink) {

						if (item.getText().contains(strlink)) {

							commonLibrary.clickLinkWithWebElementWithWait(item, strlink);
							blnFlag = true;
							break;
						}

					}
				}
			}
		}

		if (blnFlag) {
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", strlink + " link  clicked.", Status.PASS);
		} else {
			report.updateTestLog("Click on " + strlink + " on document Popup trail ", strlink + " link not  clicked.", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Viewmap
	// # Function Name : clickViewmap     
	// # Author : Aravind
	// # Date Created : apr'15
	// #*****************************************************************************************************************************

	public ResearchMap clickViewmap() {
		WebElement viewMapAnchor = commonLibrary.isExist(UIMAP_ResearchMap.viewMapAnchor, 20);
		if (viewMapAnchor != null)
			commonLibrary.clickButtonParentWithWait(viewMapAnchor, viewMapAnchor.getText());
		report.updateTestLog("Click View Map", "View Map is clicked'", Status.DONE);
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the researchmap
	// # Function Name : viewmap_researchmap     
	// # Author : karthi
	// # Date Created : apr'15
	// #*****************************************************************************************************************************
	public ResearchMap viewMapResearchMap() {
		WebElement viewmap = commonLibrary.isExist(UIMAP_IMContent.viewmap, 10);
		if (viewmap != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(viewmap, "View-map");
			else
				commonLibrary.clickButtonParentWithWait(viewmap, "View-map");
		} else {
			report.updateTestLog("Click 'View map': ", "'view-map link  is not clicked'", Status.FAIL);
		}
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on open Dashboard
	// # Function Name : openDashboard     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking openDashboard() {
		WebElement openDashBoard = commonLibrary.isExist(UIMAP_IMContent.openDashboard, 20);
		if (openDashBoard != null)
			commonLibrary.clickLinkWithWebElementWithWait(openDashBoard, "Open Dashboard");

		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Content
	// # Function Name : VerifyContent     
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyContent(String FontType, String FontSize) {
		String txtFontType;
		String txtFontSize;
		WebElement txtSection = commonLibrary.isExist(UIMAP_IMContent.sectioncontent, 20);
		if (txtSection != null) {
			txtFontType = txtSection.getCssValue("font-family");
			txtFontSize = txtSection.getCssValue("font-size");
			if (FontType.equalsIgnoreCase("Courier")) {
				FontType = "Monospace";
			}

			switch (FontType) {
			case "Arial": {
				if (FontType.equalsIgnoreCase(txtFontType)) {
					switch (FontSize) {
					case "Medium": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("16px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("13px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
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
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("13px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
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
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("18px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("12px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
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
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Large": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("16px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);
						}
						break;
					}
					case "Small": {
						if (FontSize.equalsIgnoreCase(txtFontSize) || txtFontSize.equals("12px")) {
							report.updateTestLog("Verifying the visual of the  Page  is in  FontType- Size format", "Visual of the Page verification is done with font type :" + FontType + " and font size :" + FontSize, Status.PASS);

						}
						break;
					}

					}

				}
				break;
			}

			}
		} else {
			report.updateTestLog("Verifying the visual of the Full page is in  FontType- Size format", "The visual of the page is not displayed with font type :" + FontType + " and font size :" + FontSize, Status.FAIL);
		}

		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application
	// # Function Name : logout     
	// # Author : Baswaraj
	// # Date Created : May'15
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
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyPageTitle(String title) {
		commonLibrary.sleep(1000);
		generalFunctions.verifyPageTitle(title);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the properties section in
	// full view WF page
	// # Function Name : verifyPropertiesUnderAboutThisDocument()     
	// # Author : Deepha Hariramasamy
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyPropertiesUnderAboutThisDocument(String Type, String searchType) {
		try {

			WebElement propertiesValue = commonLibrary.isExist(UIMAP_IMContent.propertiesValue, 10);
			if (propertiesValue != null) {
				String name = propertiesValue.getText();
				String[] arrsplitted = name.split("Last Updated");
				String[] splittedDate = arrsplitted[1].split(" a.m");
				String a = splittedDate[0].trim();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy   h:mm:ss", Locale.ENGLISH);
				Date date = sdf.parse(a);
				if (date != null) {
					report.updateTestLog("verify date format", "date is in correct format", Status.PASS);
				} else {
					report.updateTestLog("verify date format", "date is in incorrect format", Status.FAIL);
				}

				if (propertiesValue.getText().contains(Type) && propertiesValue.getText().contains(searchType))
					report.updateTestLog("Verify properties sections details", Type + ", " + searchType + " are displayed", Status.PASS);
				else
					report.updateTestLog("Verify properties sections details", Type + ", " + searchType + " are NOT displayed", Status.FAIL);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new IntermediateContent(scriptHelper);

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
	// # Function Description : Function to click and enter in Edit properties
	// pop up
	// # Function Name : clickEditProperties()     
	// # Author : Deepha Hariramasamy
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public IntermediateContent clickEditProperties(String editTitle, String clientID, String clientType) {
		WebElement editArea = commonLibrary.isExist(UIMAP_IMContent.editArea, 10);
		WebElement editIcon = commonLibrary.isExist(editArea, UIMAP_IMContent.editIcon, 10);
		if (editIcon != null) {
			commonLibrary.clickButtonParentWithWait(editIcon, "Edit Document Properties");
			WebElement editPopUp = commonLibrary.isExist(UIMAP_IMContent.editPopUp, 10);
			if (editPopUp != null) {
				report.updateTestLog("Verify Edit Document Properties pop up is displayed", "Edit Document Properties pop up is displayed", Status.PASS);
				WebElement editPopTitle = commonLibrary.isExist(UIMAP_IMContent.editTitle, 10);
				if (editPopTitle != null) {
					commonLibrary.setDataInTextBox(editPopTitle, editTitle, "title text box");
				}
				if (clientType.contains("Recent")) {

					WebElement RecentMatterradio = commonLibrary.isExist(UIMAP_IMContent.editClientExisting, 20);
					if (RecentMatterradio != null) {
						commonLibrary.clickButtonParentWithWait(RecentMatterradio, "Recent client Radio button");
						commonLibrary.sleep(3000);
						WebElement SelectMatterradio = commonLibrary.isExist(UIMAP_IMContent.existingClientDropdown, 20);
						if (SelectMatterradio != null) {
							Select select = new Select(SelectMatterradio);
							if (select.getOptions().size() > 1) {

								for (int i = 0; i < select.getOptions().size();) {
									if ((select.getOptions().get(i).getText().equalsIgnoreCase("-None-"))) {
										select.selectByIndex(i + 1);
										break;
									} else {
										select.selectByIndex(i);
										break;
									}

								}
							}
						}
					}
				} else {
					// String strClientId = commonLibrary.getCurrentDateTime();
					WebElement newMatterradio = commonLibrary.isExist(UIMAP_IMContent.newEditClient, 20);
					commonLibrary.clickButton(newMatterradio);

					// ENTER A <<<CLIENT OR MATTER>>> ID IN TEXT BOX
					WebElement id = commonLibrary.isExist(UIMAP_IMContent.editClientText, 20);
					if (commonLibrary.setDataInTextBox(id, clientID, "New Client Id")) {
						report.updateTestLog("Enter a client or matter id", "Client or matter id is entered as " + clientID + "", Status.PASS);
					} else {
						report.updateTestLog("Enter a client or matter id", "Client or matter id is not entered as " + clientID + "", Status.FAIL);
					}

				}

				WebElement editSave = commonLibrary.isExist(UIMAP_IMContent.editSave, 20);
				commonLibrary.clickButton(editSave);

			} else {
				report.updateTestLog("Verify Edit Document Properties pop up is displayed", "Edit Document Properties pop up is not displayed", Status.FAIL);
			}

		}

		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click BrowserBack
	// # Function Name : clickBrowserBack     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public WorkFolders clickBrowserBack() {

		// //
		// try {
		// if (browsername.contains("chrome") ||
		// browsername.contains("firefox")) {
		// driver.navigate().back();
		// commonLibrary.sleep(7000);
		// } else {
		// Actions builder = new Actions(driver);
		// builder.sendKeys(Keys.BACK_SPACE).perform();
		// commonLibrary.sleep(5000);
		//
		// report.updateTestLog("Click on Browser Back",
		// "Clicked on Browser Back", Status.PASS);
		// // driver.manage().timeouts().pageLoadTimeou(220,
		// // TimeUnit.MILLISECONDS);
		// }
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// report.updateTestLog("Click on Browser Back",
		// "Browser Back not clicked", Status.FAIL);
		// }

		commonLibrary.clickBrowserBack();
		return new WorkFolders(scriptHelper);
	}

	public IntermediateContent verifyText(String text) {
		WebElement sectioncontent = commonLibrary.isExistNegative(UIMAP_IMContent.sectioncontent, 10);
		WebElement sectionHeader = commonLibrary.isExistNegative(sectioncontent, UIMAP_IMContent.sectionHeader, 10);

		if (sectionHeader.getText().contains("Selected text") && sectioncontent.getText().toLowerCase().contains(text.toLowerCase())) {
			report.updateTestLog("Verify Selected text from the document displays under the title 'Selected text:'", "Selected text from the document displays under the title 'Selected text:'", Status.PASS);
		} else
			report.updateTestLog("Verify Selected text from the document displays under the title 'Selected text:'", "Selected text from the document does not display under the title 'Selected text:'", Status.FAIL);
		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click search the terms link
	// # Function Name : clickSearchTheTerms     
	// # Author : Seetha
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search clickSearchTheTerms(String title) {
		List<WebElement> list = commonLibrary.isExistList(UIMAP_WorkFolders.links, 10);
		for (WebElement item : list) {
			if (item.getText().toLowerCase().contains(title.toLowerCase())) {
				commonLibrary.clickJS(item, title);
				break;
			}
		}
		return new Search(scriptHelper);
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
	// # Function Description : Function to click on open Form
	// # Function Name : openForm
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Document openForm() {
		WebElement openDashBoard = commonLibrary.isExist(UIMAP_IMContent.openForm, 20);
		if (openDashBoard != null)
			commonLibrary.clickLinkWithWebElementWithWait(openDashBoard, "Open Dashboard");

		return new Document(scriptHelper);
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
	// # Function Description : Function to click on ReRun Saved Search
	// # Function Name : clicksrchwithCurrentSelected     
	// # Author : Vennila
	// # Date Created : 20 Oct 15
	// #*****************************************************************************************************************************
	public Search clicksrchwithCurrentSelected() {
		WebElement srchwithCurrent = commonLibrary.isExist(UIMAP_IMContent.srchwithCurrent, 10);
		if (srchwithCurrent != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(srchwithCurrent, "Search for these terms using the currently selected filter parameters");
			else
				commonLibrary.clickButtonParentWithWait(srchwithCurrent, "Search for these terms using the currently selected filter parameters");
		} else {
			report.updateTestLog("Click 'Search for these terms using the currently selected filter parameters': ", "'Search for these terms using the currently selected filter parameters'", Status.FAIL);
		}
		int check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on WebURL link
	// # Function Name : clickWebURL     
	// # Author : Vennila
	// # Date Created : 27 Oct'15
	// #*****************************************************************************************************************************
	public IntermediateContent clickWebURL(String linkName) {
		String LinkName = "";

		WebElement webURL = commonLibrary.isExist(UIMAP_IMContent.webURL, 20);
		if (webURL != null) {
			LinkName = webURL.getText();
			if (LinkName.trim().contains(linkName.trim())) {
				commonLibrary.clickLinkWithWebElementWithWait(webURL, "Web URL link is clicked");
				commonLibrary.sleep(10000);
				// commonLibrary.switchToWindow(linkName);
				report.updateTestLog("Clicking on search for link", "Clicked on Search for link", Status.PASS);
			} else
				report.updateTestLog("Clicking on search for link", "Not Clicked on Search for link", Status.FAIL);
		}

		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on WebURL link
	// # Function Name : closeAndSwitchTorpage     
	// # Author : Vennila
	// # Date Created : 27 Oct'15
	// #*****************************************************************************************************************************

	public IntermediateContent closeAndSwitchTorpage(String linkName) {

		commonLibrary.smallWait();
		commonLibrary.switchToWindow(linkName);
		driver.close();
		commonLibrary.switchToWindow("intermediatecontent");
		report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the details section in abt
	// section
	// full view WF page
	// # Function Name : vverifyPropertiesUnderabtSection_new     
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public IntermediateContent verifyPropertiesUnderabtSection_new(String type, boolean flag) {

		flag = false;
		try {
			WebElement header = commonLibrary.isExist(UIMAP_IMContent.abtSection, 10);
			if (header != null) {
				String[] arr = header.getText().split("\\n");
				for (int i = 0; i <= arr.length; i++) {
					if (arr[i].trim().contains(type)) {
						flag = true;
						break;
					}

				}
				if (flag)
					report.updateTestLog("Verify properties sections details", "" + type + " is displayed", Status.PASS);
				if (!flag)
					report.updateTestLog("Verify properties sections details", "" + type + " not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new IntermediateContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on open Medmal Dashboard
	// # Function Name : DashboardMedmal    
	// # Author : Sriram
	// # Date Created : Nov '15
	// #*****************************************************************************************************************************

	public MedmalDashborad OpenDashboardMedmal() {
		WebElement openDashBoard = commonLibrary.isExist(UIMAP_IMContent.DashboardMedmal, 20);
		if (openDashBoard != null)
			commonLibrary.clickLinkWithWebElementWithWait(openDashBoard, "Open Dashboard");

		return new MedmalDashborad(scriptHelper);
	}

	// ***********************************************************************************************************************
	// # Function Description : Function to click on ReRun Saved Search in
	// folder
	// # Function Name : clicksrchwithCurrentSelectedNew    
	// # Author : Sriram
	// # Date Created : Nov 2015
	// #*****************************************************************************************************************************
	public Search clicksrchwithCurrentSelectedNew() {
		WebElement sectioncontent = commonLibrary.isExistNegative(UIMAP_IMContent.sectioncontent, 20);
		WebElement srchwithCurrent = commonLibrary.isExistNegative(sectioncontent, UIMAP_IMContent.srchwithCurrentnew, 20);
		if (srchwithCurrent != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(srchwithCurrent, "Search for these terms using the currently selected filter parameters");
			else
				commonLibrary.clickButtonParentWithWait(srchwithCurrent, "Search for these terms using the currently selected filter parameters");
		} else {
			report.updateTestLog("Click 'Search for these terms using the currently selected filter parameters': ", "'Search for these terms using the currently selected filter parameters'", Status.FAIL);
		}
		int check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to edit notes in Intermediate content
	// page
	// # Function Name : editNotes
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public IntermediateContent editNotes(String text) {
		// driver.switchTo().defaultContent();
		try {
			WebElement editheader = commonLibrary.isExist(UIMAP_IMContent.editheader, 10);

			WebElement editbtn = commonLibrary.isExist(editheader, UIMAP_IMContent.editbtn, 10);
			if (editheader != null) {

				if (browsername.contains("internet"))
					commonLibrary.clickJS(editbtn, "Edit");
				else
					commonLibrary.clickButtonParentWithWait(editbtn, "Edit");
				WebElement editPopup = commonLibrary.isExist(UIMAP_IMContent.editPopup, 10);
				if (editPopup != null) {
					driver.switchTo().frame(editPopup);
					WebElement editContent = commonLibrary.isExist(UIMAP_IMContent.editContent, 10);

					if (browsername.contains("internet"))
						commonLibrary.clickJS(editContent, "textarea in edit pop up");
					else
						commonLibrary.clickButtonParentWithWait(editContent, "textarea in edit pop up");

					Robot r = new Robot();
					r.keyPress(KeyEvent.VK_END);
					r.keyRelease(KeyEvent.VK_END);
					r.keyPress(KeyEvent.VK_ENTER);
					r.keyRelease(KeyEvent.VK_ENTER);
					report.updateTestLog("Press Enter button in keyboard", "Pressed Enter button in keyboard", Status.PASS);
					commonLibrary.sleep(2000);
					r = null;

					driver.switchTo().defaultContent();
					editPopup = commonLibrary.isExist(UIMAP_IMContent.editPopup, 10);
					driver.switchTo().frame(editPopup);
					editContent = commonLibrary.isExist(UIMAP_IMContent.editContent, 10);
					enterText(editContent, text, "Editnote Text box");
					driver.switchTo().defaultContent();
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
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter text in edit notes pop up
	// # Function Name : enterText
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public IntermediateContent enterText(WebElement editContent, String strVal, String FieldName) {

		try {

			WebElement eltTextBoxId = editContent;
			// this.ScrollToView(eltTextBoxId);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", editContent);

			String strNew = null;
			strNew = strVal.replaceAll("\\(", Keys.chord(Keys.SHIFT, "9"));
			strNew = strNew.replaceAll("\\#", Keys.chord(Keys.SHIFT, "3"));
			strNew = strNew.replaceAll("\\-", Keys.SUBTRACT.toString());
			strNew = strNew.replaceAll("\\}", Keys.chord(Keys.SHIFT, "]"));
			if (eltTextBoxId != null) {
				if (browsername.contains("internet")) {

					String textPresent = "";
					if (editContent.getAttribute("value") != null)
						textPresent = editContent.getAttribute("value");
					else if (editContent.getText() != null)
						textPresent = editContent.getText();
					if (!textPresent.contains("Enter terms, sources, a citation, or shep: to Shepardize®")) {
						// int len = textPresent.length();
						if (!textPresent.equals(strNew)) {

							/*
							 * for (i = 0; i <= len; i++) {
							 * 
							 * /*Obj.sendKeys(Keys.END);
							 * Obj.sendKeys(Keys.BACK_SPACE);
							 * 
							 * }
							 */
							editContent.sendKeys(strNew);
						}
					}

				} else {
					String textPresent = "";
					if (editContent.getAttribute("value") != null)
						textPresent = editContent.getAttribute("value");
					else if (editContent.getText() != null)
						textPresent = editContent.getText();
					if (!textPresent.equals(strVal)) {

						commonLibrary.sleep(2000);
						eltTextBoxId.click();
						eltTextBoxId.sendKeys(strNew);
						eltTextBoxId.sendKeys(Keys.DOWN);
					}
				}

				report.updateTestLog("Set value " + strVal + " in the edit note text box", strVal + " is set in the edit note text box", Status.DONE);

				return new IntermediateContent(scriptHelper);
			} else {
				throw new FrameworkException("Set value " + strVal + " in the editnote text box", strVal + " is not set in the edit note text box");

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			return new IntermediateContent(scriptHelper);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes
	// # Function Name : verifyNotes
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public IntermediateContent verifyNotes(String note, String text) {

		WebElement notes = commonLibrary.isExist(UIMAP_IMContent.noteSection, 10);
		List<WebElement> addedNote = commonLibrary.isExistList(notes, UIMAP_IMContent.content, 10);
		for (int k = 0; k < addedNote.size(); k++) {
			if (k == 0) {
				if (addedNote.get(k).getText().toLowerCase().contains(note.toLowerCase()))
					report.updateTestLog("verify " + note + " is displayed first", note + " is displayed first", Status.PASS);
				else
					report.updateTestLog("verify " + note + " is displayed first", note + " is not displayed first ", Status.FAIL);
			}
			if (k == 1) {
				if (addedNote.get(k).getText().toLowerCase().contains(text.toLowerCase()))
					report.updateTestLog("verify " + text + " is displayed below first note", text + " is displayed below first note", Status.PASS);
				else
					report.updateTestLog("verify" + text + " is displayed below first note", text + " is not displayed below first note", Status.FAIL);
			}
		}
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view all notes
	// # Function Name : clickViewAllNotes 
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public IntermediateContent clickViewAllNotes() {
		WebElement notes = commonLibrary.isExist(UIMAP_IMContent.notes, 20);
		WebElement viewAllNotes = commonLibrary.isExist(notes, UIMAP_IMContent.viewAllNotes, 10);
		if (viewAllNotes != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(viewAllNotes, "View all notes");
			else
				commonLibrary.clickButtonParentWithWait(viewAllNotes, "View all notes");
		}
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes in edit note pop up
	// # Function Name : verifyNotesinPopup
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public IntermediateContent verifyNotesinPopup(String note, String text) {
		// driver.switchTo().defaultContent();
		try {
			WebElement editheader = commonLibrary.isExist(UIMAP_IMContent.editheader, 10);
			WebElement editbtn = commonLibrary.isExist(editheader, UIMAP_IMContent.editbtn, 10);
			if (editheader != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(editbtn, "Edit");
				else
					commonLibrary.clickButtonParentWithWait(editbtn, "Edit");
				WebElement editPopup = commonLibrary.isExist(UIMAP_IMContent.editPopup, 10);
				if (editPopup != null) {
					driver.switchTo().frame(editPopup);
					WebElement editContent = commonLibrary.isExist(UIMAP_IMContent.editContent, 10);
					List<WebElement> addedNote = commonLibrary.isExistList(editContent, UIMAP_IMContent.content, 10);
					for (int i = 0; i < addedNote.size(); i++) {
						if (i == 0) {
							if (addedNote.get(i).getText().toLowerCase().contains(note.toLowerCase()))
								report.updateTestLog("verify " + note + " is displayed first in edit notes pop up", note + " is displayed first in edit notes pop up", Status.PASS);
							else
								report.updateTestLog("verify " + note + " is displayed first in edit notes pop up", note + " is not displayed first in edit notes pop up", Status.FAIL);
						}
						if (i == 1) {
							if (addedNote.get(i).getText().toLowerCase().contains(text.toLowerCase()))
								report.updateTestLog("verify " + text + " is displayed below first note in edit notes pop up", text + " is displayed below first note in edit notes pop up", Status.PASS);
							else
								report.updateTestLog("verify " + text + " is displayed below first note in edit notes pop up", text + " is not displayed below first note in edit notes pop up", Status.FAIL);
						}
					}
					driver.switchTo().defaultContent();
					WebElement cancel = commonLibrary.isExist(UIMAP_IMContent.cancel, 10);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(cancel, "Cancel");
					else
						commonLibrary.clickButtonParentWithWait(cancel, "Cancel");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify edit button in notes section
	// # Function Name : verfiyEditbutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public IntermediateContent verfiyEditbutton(String exist) {
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
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify delete button in notes
	// section
	// # Function Name : verfiyDeletebutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public IntermediateContent verfiyDeletebutton(String exist) {
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
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delete button
	// # Function Name : clickDeletebutton
	// # Author : Sriram
	// # Date Created :Dec '15
	// #*****************************************************************************************************************************

	public IntermediateContent clickDeletebutton() {
		WebElement editheader = commonLibrary.isExist(UIMAP_IMContent.editheader, 20);
		WebElement deletebtn = commonLibrary.isExist(editheader, UIMAP_IMContent.deletebtn, 10);
		if (deletebtn != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(deletebtn, "Delete");
			else
				commonLibrary.clickButtonParentWithWait(deletebtn, "Delete");
		}
		driver.switchTo().defaultContent();
		WebElement delete = commonLibrary.isExist(UIMAP_IMContent.delete, 10);
		if (delete != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(delete, "Delete in pop up");
			else
				commonLibrary.clickButtonParentWithWait(delete, "Delete in pop up");
		}
		return new IntermediateContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify notes are not displayed in
	// header
	// # Function Name : verifyNotesnotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public IntermediateContent verifyNotesnotDisplayed() {
		WebElement editArea = commonLibrary.isExist(UIMAP_IMContent.editArea, 10);
		WebElement addedNote = commonLibrary.isExist(editArea, UIMAP_IMContent.noteSection, 10);

		if (addedNote.getText().toLowerCase().contains(""))
			report.updateTestLog("verify added note is not displayed", " Added note is not displayed", Status.PASS);
		else
			report.updateTestLog("verify added note is not displayed", " Added note is displayed ", Status.FAIL);

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

}
