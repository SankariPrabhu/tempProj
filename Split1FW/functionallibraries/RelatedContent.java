package functionallibraries;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPSReport;
import UIMAP.UIMAP_RelatedContent;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class RelatedContent extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public RelatedContent(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("relatedcontent"))
				commonLibrary.sleep(5000);

		} while (!url.contains("relatedcontent") && counter < 40);

		if (!driver.getCurrentUrl().contains("relatedcontent")) {
			throw new IllegalStateException("Related content page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public RelatedContent verifySearchResultHeader(String strPageHeader) {
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
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

		strPageHeader = Header.getText();

		if (HeaderSearchResult != null && HeaderSearchResult.getText().toLowerCase().contains(strPageHeader) || HeaderSearchResult1 != null && HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader) || HeaderSearchResult3 != null && HeaderSearchResult3.getText().contains(strPageHeader) || HeaderSearchResult4 != null && HeaderSearchResult4.getText().toLowerCase().contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else if (HeaderSearchResult != null && HeaderSearchResult.getText().contains(strPageHeader) || HeaderSearchResult1 != null && HeaderSearchResult1.getText().contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a text present in page within
	// a list
	// # Function Name : VerifyText_DocView_withinList     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyTextWithinList(By by, String Text) {
		Boolean blnFlag = false;
		List<WebElement> objList = commonLibrary.isExistList(by, 10);
		for (WebElement item : objList) {
			if (item.getText().contains(Text)) {
				blnFlag = true;
				break;
			}
		}

		if (blnFlag) {
			report.updateTestLog("Pending Legislation results page displays", "Pending Legislation results page displays", Status.PASS);
		} else {
			report.updateTestLog("Pending Legislation results page displays", "Pending Legislation results page is not displayed", Status.FAIL);
		}
		return new RelatedContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a text present in page within
	// a list
	// # Function Name : VerifyText_DocView_withinList     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyListPresent_Parent(By by, By Lby) {
		WebElement parent = driver.findElement(by);
		List<WebElement> ResultLinks = parent.findElements(Lby);
		if (ResultLinks.size() > 0) {
			report.updateTestLog("Results page displays with the links", "Results page displays with the links", Status.PASS);
		} else {
			report.updateTestLog("Results page displays with the links", "Results page is not displayed with the links", Status.FAIL);
		}

		return new RelatedContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Back
	// # Function Name : ClickBrowserBack     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document clickBrowserBack() {

		commonLibrary.clickBrowserBack();
		try {
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify MultiList View
	// # Function Name : verifyMultiListView     
	// # Author : uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyMultiListView(String TabType, String TabPage) {

		WebElement resultList = commonLibrary.isExist(UIMAP_RelatedContent.resultList, 10);

		if (resultList != null) {
			report.updateTestLog("Verify " + TabType + " page for " + TabPage + " is displayed", TabType + " page for " + TabPage + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify " + TabType + " page for " + TabPage + " is displayed", TabType + " page for " + TabPage + " is not displayed", Status.FAIL);
		}
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title
	// # Function Name : Verify_DocumentTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyDocumentTitle(String strDocTitle) {

		WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);
		if (eltDocumentHeading.getText().contains(strDocTitle))
			report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.FAIL);
		return new RelatedContent(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application
	// # Function Name : logout     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Related Content Page Link
	// # Function Name : ClickRelatedContentPageLink     
	// # Author : Ram Prasath
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickRelatedContentPageLink(String linkName) {

		WebElement linkDocPage = commonLibrary.isExist(By.linkText(linkName), 10);

		if (browsername.contains("internet")) {
			commonLibrary.clickLinkWithWebElementWithWaitJS(linkDocPage, linkDocPage.getText());
		} else {
			commonLibrary.clickLinkWithWebElementWithWait(linkDocPage, linkDocPage.getText());
		}
		commonLibrary.sleep(10000);
		WebElement docHeader = commonLibrary.isExist(UIMAP_Document.docHeader, 20);
		int counter = 0;
		do {
			counter = counter + 1;
			docHeader = commonLibrary.isExist(UIMAP_Document.docHeader, 20);
			if (docHeader == null)
				commonLibrary.sleep(5000);
		} while (docHeader == null && counter <= 40);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify recently viewed icon
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyRecentlyViewed(String strDocTitle, Boolean isDisplayed) {
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		Boolean blnFlag = false;
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
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
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
		// if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
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
		// commonLibrary.clickLinkWithWebElementWithWait(btnNextPage,
		// "NextPage");
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
		//
		// if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
		// else if
		// (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3,
		// 10);
		// else if
		// (commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3,
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
	// # Function Description : Function to verify Topic Summary
	// # Function Name : verifyTopicSummary     
	// # Author : seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyTopicSummary() {
		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 10);
		if (hdrResult1.getText().contains("Topic Summary")) {
			report.updateTestLog("Verify Topic summary page is displayed", "Topic summary page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Topic summary page is displayed", "Topic summary page is not displayed", Status.FAIL);
		}

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify LexisAdvance Research Logo
	// # Function Name : verifyLexisAdvanceResearchLogo     
	// # Author : seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyLexisAdvanceResearchLogo() {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().contains("Research")) {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo displays in Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo is not displayed in Global menu", Status.FAIL);
		}
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Briefs
	// # Function Name : verifyBriefs     
	// # Author : seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyBriefs() {
		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 10);
		if (hdrResult1.getText().contains("Briefs")) {
			report.updateTestLog("Verify <Multilink List View> page displays for Briefs", "<Multilink List View> page displays for Briefs", Status.PASS);
		} else {
			report.updateTestLog("Verify <Multilink List View> page displays for Briefs", "<Multilink List View> page is not displayed for Briefs", Status.FAIL);
		}

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify summary headers and rightpane
	// # Function Name : verifysummaryheadersandrightpane     
	// # Author : uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent verifysummaryheadersandrightpane() {
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select checkbox for given results.
	// # Function Name : checkDocTitle     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public RelatedContent checkDocTitle(String docTitle) {
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;
		do {
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().equals(docTitle)) {
							WebElement chkBox = commonLibrary.isExist(document, By.tagName("input"), 20);
							if (chkBox != null) {
								// commonLibrary.ScrollToView(chkBox);
								// commonLibrary.highlightElement(chkBox);
								commonLibrary.setCheckBox(chkBox, true);
								report.updateTestLog("Select checkbox for document " + docTitle, "Checkbox for document " + docTitle + " is selected.", Status.PASS);
								blnFlag = true;
								btnNextPage = null;
								break;
							}
						}

					}

				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
			}
			count++;
		} while (btnNextPage != null && count <= 15);

		if (!blnFlag)

			report.updateTestLog("Select checkbox for document " + docTitle, "Document " + docTitle + " is not present.", Status.FAIL);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add results to a folder.
	// # Function Name : addToFolder     
	// # Author : Uma
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public RelatedContent addToFolder(String FolderName) {
		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			//
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");
			commonLibrary.sleep(20000);

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);

			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWait(create, "create");
					commonLibrary.sleep(20000);
				} else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.clickButton(saveFolder);
			commonLibrary.sleep(20000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify results added to folder.
	// # Function Name : verifyAddedToFolder     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public RelatedContent verifyAddedToFolder(String docTitle) {
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;
		do {
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().equals(docTitle)) {
							WebElement folderIcon = commonLibrary.isExist(document, UIMAP_RelatedContent.folderIcon, 20);
							if (folderIcon != null) {
								report.updateTestLog("Verify folder icon is present for document " + docTitle, "Folder icon is present for document " + docTitle, Status.PASS);
								blnFlag = true;
								btnNextPage = null;
								break;
							}

						}

					}

				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
			}
			count++;
		} while (btnNextPage != null && count <= 15);

		if (!blnFlag)

			report.updateTestLog("Verify folder icon is present for document " + docTitle, "Folder icon is present not for document " + docTitle, Status.FAIL);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on a result.
	// # Function Name : clickDocLink1     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Document clickDocLink1(String strDocTitle) {
		// boolean blnFlag = false;
		//
		// WebElement resultClass = null;
		//
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		//
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
		// if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		//
		// break;
		// }
		// }
		//
		// }
		//
		// }
		// }
		//
		// if (!blnFlag)
		//
		// report.updateTestLog("Click on the report " + strDocTitle,
		// "Not Clicked  on the report " + strDocTitle, Status.FAIL);

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Topic Summary Content
	// # Function Name : clickTopicSummaryContent     
	// # Author : Shobana
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickTopicSummaryContent(String content) {
		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult, 10);
		if (hdrResult1.getText().contains("Topic Summary")) {
			List<WebElement> resultList = commonLibrary.isExistList(UIMAP_RelatedContent.resultListLnk, 10);
			for (WebElement item : resultList)

			{
				if (item.getText().contains(content)) {
					commonLibrary.clickJS(item, content);

					break;
				}
			}

		}
		int count = 0;
		WebElement docTitle = commonLibrary.isExistNegative(By.cssSelector("h1[id='SS_DocumentTitle']"), 10);
		do {
			count++;
			docTitle = commonLibrary.isExistNegative(By.cssSelector("h1[id='SS_DocumentTitle']"), 10);
			commonLibrary.sleep(5000);
		} while (docTitle == null && count < 40);
		pageCheck.positiveCheck(driver, "document", "Document");
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the footer links in the LPS
	// page and to signout of the application from the footer page
	// # Function Name : footerlinksverification     
	// # Author : Ram Prasath
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SignIn footerlinksverification() {
		WebElement footerlinks = commonLibrary.isExist(UIMAP_LPSReport.footerlinks, 12);
		List<WebElement> lilist = commonLibrary.isExistList(footerlinks, UIMAP_LPSReport.listItem, 20);
		WebElement footerlist = commonLibrary.isExist(lilist.get(0), UIMAP_LPSReport.footerlist, 12);
		WebElement footerlogo = commonLibrary.isExist(footerlist, UIMAP_LPSReport.footerlogo, 12);
		if (footerlogo != null) {
			report.updateTestLog("Verify the Presence of Footer Logo", "LexisNexis Logo is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Footer Logo", "LexisNexis Logo is not Displayed", Status.FAIL);
		}
		WebElement footerlist1 = commonLibrary.isExist(lilist.get(1), UIMAP_LPSReport.footerlist, 12);
		if (footerlist1.getText().contains("About LexisNexis®")) {
			report.updateTestLog("Verify the Presence of About LexisNexis Link", footerlist1.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of About LexisNexis Link", footerlist1.getText() + " is not Displayed", Status.FAIL);
		}
		WebElement footerlist2 = commonLibrary.isExist(lilist.get(2), UIMAP_LPSReport.footerlist, 12);
		if (footerlist2.getText().contains("Privacy Policy")) {
			report.updateTestLog("Verify the Presence of Privacy Policy Link", footerlist2.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Privacy Policy Link", footerlist2.getText() + " is not Displayed", Status.FAIL);
		}
		WebElement footerlist3 = commonLibrary.isExist(lilist.get(3), UIMAP_LPSReport.footerlist, 12);
		if (footerlist3.getText().contains("Terms & Conditions")) {
			report.updateTestLog("Verify the Presence of Terms & Conditions Link", footerlist3.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Terms & Conditions Link", footerlist3.getText() + " is not Displayed", Status.FAIL);
		}
		WebElement footerlist4 = commonLibrary.isExist(lilist.get(4), UIMAP_LPSReport.footersignout, 12);
		if (footerlist4.getText().contains("Sign Out")) {
			report.updateTestLog("Verify the Presence of Sign Out Link", footerlist4.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Sign Out Link", footerlist4.getText() + " is not Displayed", Status.FAIL);
		}
		WebElement footerlist5 = commonLibrary.isExist(lilist.get(5), UIMAP_LPSReport.footerlist, 12);
		if (footerlist5.getText().contains("Copyright © 2016 LexisNexis. All rights reserved.")) {
			report.updateTestLog("Verify the Presence of Copyright © 2016 LexisNexis. All rights reserved. Link", footerlist5.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Copyright © 2016 LexisNexis. All rights reserved. Link", footerlist5.getText() + " is not Displayed", Status.FAIL);
		}

		if (browsername.contains("internet")) {
			commonLibrary.clickLinkWithWebElementWithWaitJS(footerlist4, "Sign Out");

		} else {
			commonLibrary.clickLinkWithWebElementWithWait(footerlist4, "Sign Out");

		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent selectDocumentByTitle(String DocName) {
		generalFunctions.selectDocumentByTitle(DocName);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a folder after clicking
	// folder icon.
	// # Function Name : clickFolderIcon
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public WorkFolders clickFolderIcon(String docTitle, String folderName) {
		generalFunctions.clickFolderIcon(docTitle, folderName);
		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent selectDocumentByTitleVerifyCount(String DocName, int count) {
		generalFunctions.selectDocumentByTitleVerifyCount(DocName, count);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Back
	// # Function Name : ClickBrowserBack     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search clickBrowserBack1() {

		commonLibrary.clickBrowserBack();
		try {
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To Folders
	// # Function Name : navigateToFolders
	// # Author : Seetha
	// # Date Created : 26-Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
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
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #************************************************************************************************************
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
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount     
	// # Author : Jubin
	// # Date Created : Dec'15
	// #************************************************************************************************************
	public String getResultcount1() {
		boolean flag = false;
		String s1 = "";
		int i = 0;
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.eltResult, 20);
		if (resultList != null) {
			List<WebElement> searchContent = commonLibrary.isExistList(resultList, UIMAP_SearchResult.lstTagListItems, 10);
			i = searchContent.size();
			System.out.println(i);
			s1 = Integer.toString(i);
			if (i > 0) {
				flag = true;
			} else {
				flag = false;
			}

			if (flag == true) {
				report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
			}

			else {
				report.updateTestLog("Getting result count", "Result header is not present", Status.FAIL);
			}
		}

		return s1;
	}
}
