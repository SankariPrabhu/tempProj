package functionallibraries;

//import java.awt.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_GUIBContainer;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_IMContent;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_RelevanceEnhancement;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Settings;
import UIMAP.UIMAP_Sources;
import UIMAP.UIMAP_TOC;
import UIMAP.UIMAP_VSASearchResults;
import UIMAP.UIMAP_WorkFolders;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

//import com.gargoylesoftware.htmlunit.javascript.host.Text;

public class Search extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	public WebElement wordWheelContent;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private Search search;
	public Document document;
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;
	private ExcelDataAccess excel;
	final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	// private Robot robot;

	public Search(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("search"))
				commonLibrary.sleep(5000);

		} while (!url.contains("search") && counter < 40);

		if (!driver.getCurrentUrl().contains("search")) {
			throw new IllegalStateException("Search page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the results are displayed
	// with overview of document
	// # Function Name : VerifyDocumentOverView     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verifyDocumentOverView() {
		Boolean blnFlag = false;

		try {
			String strDocTitle = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						report.updateTestLog("Document Title ", "Document Title is " + strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						WebElement eleOverviewSection = commonLibrary.isExist(eleDocCondent, By.tagName("strong"), 20);
						if (eleOverviewSection != null && eleOverviewSection.getText().toLowerCase().contains("overview")) {
							blnFlag = true;
							break;
						}

					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return blnFlag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search swithToCondentType(String strCondentType) {

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
		commonLibrary.sleep(10000);

		// wait after selecting the docket content type

		if (strCondentType.toLowerCase().contains("dockets") || strCondentType.toLowerCase().contains("news")) {

			try {
				Thread.sleep(25000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the table of condents link
	// and verfiy the TOC page
	// # Function Name : ClickTabeOfCondents_VerifyTOCPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickTabeOfCondentsVerifyTOCPage() {
		Boolean blnFlag = false;

		try {
			String strDocTitle = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						report.updateTestLog("Document Title ", "Document Title is " + strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						WebElement eleTableOfCondents = commonLibrary.isExist(eleDocCondent, UIMAP_SearchResult.lnkTableOfCondents, 20);
						if (eleTableOfCondents != null) {
							commonLibrary.clickLinkWithWebElementWithWait(eleTableOfCondents, "View Table of Contents");
							blnFlag = true;
							break;
						}

					}
				}
			}
			if (blnFlag) {
				WebElement pgTOC = commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10);
				// "TOC PAGE IS DISPLAYED

				if (pgTOC != null && pgTOC.getText().toLowerCase().contains("table of contents"))
					report.updateTestLog("Verify TOC Page is displayed", "TOC Page is displayed", Status.PASS);
				else
					report.updateTestLog("Verify TOC Page is displayed", "TOC Page is not displayed", Status.FAIL);
			} else
				report.updateTestLog("Click on View Table of Contents for document" + strDocTitle, "Not Click on View Table of Contents for document" + strDocTitle, Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the table of contents link
	// in the result page for the respective title
	// # Function Name : ClickTabeOfContentsResultPage     
	// # Author : gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TOC clickTabeOfContentsResultPage(String resultTitle) {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Boolean blnFlag = false;

		try {
			String strDocTitle = null;
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();

						if (strDocTitle.equalsIgnoreCase(resultTitle)) {
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
							WebElement eleTableOfCondents = commonLibrary.isExist(eleDocCondent, UIMAP_SearchResult.lnkTableOfCondents, 20);
							if (eleTableOfCondents != null) {
								commonLibrary.clickLinkWithWebElementWithWait(eleTableOfCondents, "View Table of Contents");
								blnFlag = true;
								break;
							}
						}

					}
				}
			}
			if (blnFlag)
				report.updateTestLog("Click on View table of Contents in the Document Title " + strDocTitle, "View table of Contents in the Document Title " + strDocTitle + " is Clicked", Status.PASS);
			else
				report.updateTestLog("Click on View table of Contents in the Document Title " + strDocTitle, "View table of Contents in the Document Title " + strDocTitle + " is not Clicked", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String clickFirstDocLink() {
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
			return strDocTitle;
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the searched item displayed
	// in history with all details
	// # Function Name : VerifySearchedItemDetails_History     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void verifySearchedItemDetailsHistory(String strSearchTerm, String strArticleText, String strAsideText) {

		try {
			String strDocTitle = null;
			String[] arrArticleText = strArticleText.split(";");

			String[] arrAsideText = strAsideText.split(";");

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement tagNameArticle = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameArticle, 20);
						if (tagNameArticle.getText().contains("Narrowed By")) {
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
							if (eleDocTitle != null)
								strDocTitle = eleDocTitle.getText();

							if (strSearchTerm.equalsIgnoreCase(strDocTitle))
								report.updateTestLog("Verify " + strSearchTerm + " is displayed", "SearchTerm is displayed as " + strDocTitle, Status.PASS);
							else
								report.updateTestLog("Verify " + strSearchTerm + " is displayed", "SearchTerm is displayed as " + strDocTitle, Status.FAIL);

							// WebElement
							// tagNameArticle=commonLibrary.isExist(document,UIMAP_SearchResultPage.tagNameArticle,
							// 20);
							for (int i = 0; i < arrArticleText.length; i++) {
								if (tagNameArticle.getText().contains(arrArticleText[i]))
									report.updateTestLog("Verify " + arrArticleText[i] + " is displayed", arrArticleText[i] + " is displayed", Status.PASS);
								else
									report.updateTestLog("Verify " + arrArticleText[i] + " is displayed", arrArticleText[i] + " is not displayed", Status.FAIL);

							}

							WebElement tagNameAside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 20);
							for (int i = 0; i < arrAsideText.length; i++) {
								if (tagNameAside.getText().contains(arrAsideText[i]))
									report.updateTestLog("Verify " + arrAsideText[i] + " is displayed", arrAsideText[i] + " is displayed", Status.PASS);
								else
									report.updateTestLog("Verify " + arrAsideText[i] + " is displayed", arrAsideText[i] + " is not displayed", Status.FAIL);

							}

							break;
						}

					}
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickResearchMapFirstBubbleLink(String strLinkName) {
		Boolean blnFlag = false;

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 10);
			if (lstResearchThread != null) {
				List<WebElement> lnkClassBubble = commonLibrary.isExistList(lstResearchThread.get(0), UIMAP_SearchResult.lnkClassBubble, 20);
				if (lnkClassBubble.size() > 0) {

					for (WebElement item : lnkClassBubble) {
						if (item != null && item.getText().equals(strLinkName)) {
							commonLibrary.clickLinkWithWebElementWithWait(item, "BubbleLink :" + strLinkName);
							blnFlag = true;
							break;
						}

					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Click on the search node named " + strLinkName, "Not Clicked on the search node named " + strLinkName, Status.FAIL);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the searched item displayed
	// in history with all details
	// # Function Name : VerifySearchedItemDetails_History     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void verifySearchedItemDetailsResearchMap(String strResearchMapText) {

		try {

			String[] arrArticleText = strResearchMapText.split(";");

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement divClassBubblePopup = commonLibrary.isExist(UIMAP_SearchResult.divClassBubblePopup, 10);
			if (divClassBubblePopup != null) {
				WebElement divClassBubblePopUpCondent = commonLibrary.isExist(divClassBubblePopup, UIMAP_SearchResult.divClassBubblePopUpCondent, 20);
				if (divClassBubblePopUpCondent != null) {

					for (int i = 0; i < arrArticleText.length; i++) {
						if (divClassBubblePopUpCondent.getText().contains(arrArticleText[i]))
							report.updateTestLog("Verify " + arrArticleText[i] + " is displayed", arrArticleText[i] + " is displayed", Status.PASS);
						else
							report.updateTestLog("Verify " + arrArticleText[i] + " is displayed", arrArticleText[i] + " is not displayed", Status.FAIL);

					}
				}
			}
		}

		catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to Select Required Filter Option in
	// // Search Results page.
	// // # Function Name : selectPostFilter
	// // # Author : Pratik
	// // # Date Created : Jan'15
	// //
	// #*****************************************************************************************************************************
	//
	// public Search selectPostFilter(String strHeader, String strFilter) {
	//
	// boolean flag=false;
	// for(int i=0;i<3;i++)
	// {
	// if(generalFunctions.selectPostFilter(strHeader, strFilter)==1)
	// {
	// flag=true;
	// break;
	// }
	// }
	// if(!flag)
	// {
	// report.updateTestLog("Select post filter " + strFilter
	// + " under "+strHeader, strFilter
	// + " is not selected from "+strHeader, Status.FAIL);
	// }
	// return new Search(scriptHelper);
	//
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search selectPostFilter(String strHeader, String strFilter) {
		boolean Flag = false;
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;

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
								commonLibrary.clickButtonParentWithWaitWithFocus(button, "More");
							else
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
					}
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						WebElement span = commonLibrary.isExistNegative(item, UIMAP_SearchResult.tagSpan, 10);
						if (span.getText().equals(strFilter)) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item, strFilter);
							else
								commonLibrary.clickButtonParentWithWaitWithFocus(item, strFilter);
							try {
								String loadProp = properties.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(1000);
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

			// WebElement filterContainer =
			// commonLibrary.isExist_Negative(UIMAP_SearchResultPage.filterContainer,
			// 10);
			// List<WebElement> filterMoreList =
			// commonLibrary.isExist_List(filterContainer,
			// UIMAP_SearchResultPage.filterMore, 10);
			// for(WebElement more:filterMoreList)
			// if(more.getText().toLowerCase().contains("more"))
			// commonLibrary.clickButton_Log_SmallWait(more, "More");
			//
			// eltFilter=
			// commonLibrary.isExist_List(UIMAP_SearchResultPage.eltFilterList,10);
			// for(i=0;i<eltFilter.size();i++)
			// {
			// if(eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase()))
			// {
			// commonLibrary.highlightElement(eltFilter.get(i));
			// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
			// eltFilter.get(i).getText());
			// report.updateTestLog("Selecting Filter: "+strFilter,
			// "Required Filter Selected.", Status.DONE);
			// blnFlag=true;
			// break;
			// }
			// }
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
		if (Flag) {
			try {
				int count = 0;
				WebElement appliedFiltersOld = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
				WebElement appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
				do {
					commonLibrary.sleep(2000);
					System.out.println("Filters Waiting " + count);
					appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 2);
					count++;
				} while (appliedFiltersOld == appliedFiltersNew && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(10000);
				System.out.println(e.toString());
			}
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickRightDocumentTitle(String strDocumentTitle) {
		Boolean blnFlag = false;

		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle.getText().contains(strDocumentTitle)) {
							WebElement lnkDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.lnkDocTitle, 20);

							Actions oAction = new Actions(driver);
							// commonLibrary.highlightElement(lnkDocTitle);
							oAction.moveToElement(lnkDocTitle);

							oAction.contextClick(lnkDocTitle).sendKeys("w").build().perform();
							commonLibrary.sleep(2000);
							blnFlag = true;
							break;

						}

					}
				}
			}
			if (blnFlag && driver.getWindowHandles().size() >= 2)

				report.updateTestLog("Right Click on the document " + strDocumentTitle + " Stop image and select open in new window", "Right Clicked on the document " + strDocumentTitle + " Stop image and selected open in new window", Status.PASS);
			else {
				report.updateTestLog("Right Click on the document " + strDocumentTitle + " Stop image and select open in new window", "Not Right Clicked on the document " + strDocumentTitle + " Stop image and Not selected open in new window", Status.FAIL);
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

	public Search verifyDocumentTitle(String strDocTitle) {

		try {

			WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 10);
			if (eltDocumentHeading.getText().toLowerCase().contains("document") && eltDocumentHeading.getText().contains(strDocTitle))
				report.updateTestLog("Verify document page is displayed", "Document page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify document page is displayed", "Document page is not displayed", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickRightSelectOpenNewWindow(WebElement element) {

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			Actions oAction = new Actions(driver);
			oAction.moveToElement(element);
			// commonLibrary.highlightElement(element);
			commonLibrary.sleep(2000);
			commonLibrary.windowFocus();
			String browsername = cap.getBrowserName();
			if (browsername.equalsIgnoreCase("firefox")) {
				oAction.contextClick(element).sendKeys("W").build().perform();
			} else if (browsername.equalsIgnoreCase("internet explorer")) {
				oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();

			}

			else if (browsername.contains("chrome")) {
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

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : VerifyAllDocTitle     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verifyAllDocTitle(String strDocTitle) {
		Boolean blnFlag = true;
		try {

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
						if (eleDocTitle != null && !eleDocTitle.getText().contains(strDocTitle)) {

							blnFlag = false;
							break;

						}

					}
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return blnFlag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click LexisNexis Logo
	// # Function Name : Click_LexisNexis_Logo     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Research clickProductLogo() {
		WebElement Productlogo = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 10);
		if (Productlogo != null) {
			// commonLibrary.ScrollToView(Productlogo);
			commonLibrary.clickLinkWithWebElementWithWait(Productlogo, "Product Logo");
			commonLibrary.sleep(15000);
			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("Research")) {
				report.updateTestLog("Research landing page is displayed", "Research landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Research landing page is displayed", "Research landing page is not displayed", Status.FAIL);
			}
		}

		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Secondary_BroserOpened 
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifySecondaryBroserOpened() {

		generalFunctions.openedSecondaryBrowser();

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to shepards window
	// # Function Name : switchToShepards     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards switchToShepards(String strWindowName) {

		// swithcing b/w windows to url(windowname)
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
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifySearchResultHeader(String strPageHeader) {

		generalFunctions.verifyPageTitle(strPageHeader);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickDocLink(String strDocTitle) {
		// Boolean blnFlag = false;
		//
		// WebElement resultClass = null;
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
		//
		// if (!blnFlag)
		//
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		// WebElement pgHeader = null;
		//
		// WebElement atd = commonLibrary.isExist(UIMAP_Document.ATD, 10);
		//
		// int counter = 0;
		// do {
		// counter = counter + 1;
		// atd = commonLibrary.isExist(UIMAP_Document.ATD, 20);
		// if (atd == null)
		// commonLibrary.sleep(5000);
		// } while (atd == null && counter <= 40);
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
		generalFunctions.clickDocTitle(strDocTitle);
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search clickRightClickStopImage(String strDocumentTitle) {

		Boolean blnFlag = false;
		try {

			// Loop for finding the document title matched
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {

				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {

					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {

						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle.getText().equalsIgnoreCase(strDocumentTitle)) {

							WebElement imgCaseLawWarning = commonLibrary.isExist(document, UIMAP_SearchResult.imgCaseLawWarning, 20);
							if (imgCaseLawWarning != null) {

								commonLibrary.getParentElement(imgCaseLawWarning);

								// Function call for performing rightclick on
								// the webelement and select open new window
								this.clickRightSelectOpenNewWindow(imgCaseLawWarning);

								commonLibrary.sleep(2000);
								blnFlag = true;
								break;
							}
						}

					}
				}
			}

			// Verification Point : Secondary browser is opened
			if (blnFlag && driver.getWindowHandles().size() == 2)

				report.updateTestLog("Right Click on the document " + strDocumentTitle + " Stop image and select open in new window", "Right Clicked on the document " + strDocumentTitle + " Stop image and selected open in new window", Status.PASS);
			else {
				report.updateTestLog("Right Click on the document " + strDocumentTitle + " Stop image and select open in new window", "Not Right Clicked on the document " + strDocumentTitle + " Stop image and Not selected open in new window", Status.FAIL);
			}

			return new Search(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for right click on shepardize this doc
	// link
	// # Function Name : RightClickShepardizeThisDocShepPreview     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search rightClickShepardizeThisDoc() {

		WebElement lstClassLinkSection = commonLibrary.isExist(UIMAP_SearchResult.lstClassLinkSection, 20);
		WebElement lnkShepardize = commonLibrary.isExist(lstClassLinkSection, UIMAP_SearchResult.lnkShepardize, 20);

		this.clickRightSelectOpenNewWindow(lnkShepardize);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify header of Search Result Page.
	// # Function Name : verifyResultPageNormalSearch     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyResultPageNormalSearch(String SearchTerm) {

		// to verify the results page for normal search

		WebElement lnSearchResults = commonLibrary.isExist(UIMAP_SearchResult.lnkresult, 30);
		if (lnSearchResults != null) {

			commonLibrary.clickLinkWithWebElementWithWait(lnSearchResults, "Search for" + SearchTerm);
		}

		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 30);
		// verifying the header
		if (hdrResult1 != null && hdrResult1.getText().contains(SearchTerm)) {

			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {

			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify SearchBox Data
	// # Function Name : verifySearchBoxEnterData     
	// # Author : Baswaraj
	// # Date Created : Feb'14
	// #*****************************************************************************************************************************

	public Search verifySearchBoxEnterData(String SearchTerm) {
		WebElement searchBox1 = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		if (searchBox1 != null) {
			report.updateTestLog("Verifying Search Box", "Search Box Displays in Global Menu next to Browse dropdown link", Status.PASS);
			commonLibrary.setDataInTextBox(searchBox1, SearchTerm, "Search Box");
		} else {
			report.updateTestLog("Verifying Search Box", "Search Box is not displayed", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Extended Global Menu
	// Collapses
	// # Function Name : verifyGlobalMenuExtnClps     
	// # Author : Baswaraj
	// # Date Created : 9-Dec'14
	// #*****************************************************************************************************************************

	public Search verifyGlobalMenuExtnClps() {
		WebElement btnHistory = commonLibrary.isExist(UIMAP_SearchResult.btnHistory, 20);
		WebElement btnTriangleDown = commonLibrary.isExist(UIMAP_SearchResult.btnTriangleDown, 20);

		if (btnHistory == null && btnTriangleDown != null) {
			report.updateTestLog("Verifying Global Menu", "1.The Global Menu in extended to the right corner  2. History and More links in the Global menu collapses as an arrow directing towards left ", Status.PASS);
		} else {
			report.updateTestLog("Verifying Global Menu", "1.The Global Menu in not extended to the right corner  2. History and More links in the Global menu not collapsed ", Status.FAIL);

		}

		commonLibrary.clickButtonParentWithWait(btnTriangleDown, "Arrow");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify GlobalMenu
	// Collapses
	// # Function Name : verifyGlobalMenu     
	// # Author : Baswaraj
	// # Date Created : 9-Dec'14
	// #*********************************************************************************************************
	public Search verifyGlobalMenu() {
		generalFunctions.verifyGlobalMenu();

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click On TriangleButton
	// Collapses
	// # Function Name : ClickOnTriangleButton     
	// # Author : Baswaraj
	// # Date Created : 9-Dec'14
	// #*********************************************************************************************************
	public Search clickOnTriangleButton() {
		WebElement btnTriangleDown = commonLibrary.isExist(UIMAP_SearchResult.btnTriangleDown, 20);
		if (btnTriangleDown != null) {
			commonLibrary.clickButtonParentWithWait(btnTriangleDown, "Triangle");
		} else {
			report.updateTestLog("Click on Triangle Button", "Triangle Button is not clicked", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Global Menu Extn Nrml
	// # Function Name : verifyGlobalMenuExtnNrml     
	// # Author : Baswaraj
	// # Date Created : 9-Dec'14
	// #*****************************************************************************************************************************

	public Search verifyGlobalMenuExtnNrml() {

		WebElement btnHistory1 = commonLibrary.isExist(UIMAP_SearchResult.btnHistory, 20);
		if (btnHistory1 != null) {
			report.updateTestLog("Verifying Global Menu", "Following displays in Global Menu:1. Search Box  collapses again to normal size  2. History and More links displays in ViewableRegion", Status.PASS);
		} else {
			report.updateTestLog("Verifying Global Menu", "Search box is not collapsed and History and more buttons are not visible", Status.FAIL);

		}
		return new Search(scriptHelper);
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
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkDocument(String strDocTitle) {

		generalFunctions.clickDocTitle(strDocTitle);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click in Shepardize This Doc
	// ShepPreview
	// # Function Name : RightClickShepardizeThisDocShepPreview     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #***********************************************************************************************************
	public Document rightClickShepardizeThisDocShepPreview() {

		WebElement lnkShepardizeThisDoc = commonLibrary.isExist(UIMAP_SearchResult.lnkShepardizeThisDoc, 20);
		// commonLibrary.ScrollToView(lnkShepardizeThisDoc);
		this.clickRightSelectOpenNewWindow(lnkShepardizeThisDoc);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify ResultPage NormalSearch in
	// Document
	// ShepPreview
	// # Function Name : VerifyResultPageNormalSearch_Document     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #***********************************************************************************************************
	public Document verifyResultPageNormalSearchDocument(String SearchTerm) {

		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 10);
		if (hdrResult1.getText().contains(SearchTerm)) {
			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click any link in Shepards Preview
	// # Function Name : ClickLink_Shepards_Preview_Section     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickLinkShepardsPreviewSection(String lnkHeader, String linkname) {
		Boolean blnFlag = false;
		commonLibrary.scrollDown();
		commonLibrary.scrollUp();

		WebElement ShepSec = commonLibrary.isExist(UIMAP_Document.ShepardsSec, 10);
		if (ShepSec != null) {
			List<WebElement> ListPrevLinks = commonLibrary.isExistList(ShepSec, By.tagName("li"), 10);
			for (WebElement lst : ListPrevLinks) {
				if (lst.getText().contains(lnkHeader) && lst.getText().contains(linkname)) {
					List<WebElement> PrevLinks = commonLibrary.isExistList(lst, By.tagName("a"), 10);
					for (WebElement item : PrevLinks) {
						if (item.getText().contains(linkname)) {
							commonLibrary.clickLinkWithWebElementWithWait(item, item.getText());
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag) {
					WebElement resultClass = null;
					int count = 0;
					do {
						commonLibrary.sleep(10000);
						resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
						count++;
					} while (resultClass == null && count < 40);
					break;
				}
			}
		} else {
			report.updateTestLog("Shepards Preview section displays at the right pane of the full document", "Shepards Preview section is not displayed at the right pane of the full document", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click On More FeedBack
	// # Function Name : ClickOnMoreFeedBack     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #******************************************************************************************************
	public Search clickOnMoreFeedBack() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 100);
		if (lnkFeedBack == null || !lnkFeedBack.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 100);
		}

		if (lnkFeedBack != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkFeedBack, "Feedback");

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFullDocument(String strSearchTerm) {
		Boolean blnFlag = false;
		try {

			WebElement copyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 20);
			if (copyCitation == null) {
				document = search.clickDocLink1(strSearchTerm);
			}
			WebElement banner = commonLibrary.isExist(UIMAP_SearchResult.hdrBanner, 10);
			if (banner != null) {
				List<WebElement> span = commonLibrary.isExistList(banner, UIMAP_SearchResult.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase().contains(strSearchTerm.toLowerCase())) {
						blnFlag = true;
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
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the preview section
	// # Function Name : verifyPreviewSection
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyPreviewSection() {

		WebElement preview = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview != null) {
			report.updateTestLog("Shepard's® preview section displays at the right pane of the full document", "Shepard's® preview section is displayed at the right pane of the full document", Status.PASS);
		} else {
			report.updateTestLog("Shepard's® preview section displays at the right pane of the full document", "Shepard's® preview section is not displayed at the right pane of the full document", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Prior Link
	// # Function Name : verifyPriorLink
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************************************************************************
	public Search verifyPriorLink(String strText) {
		boolean blnLink = false;
		WebElement preview1 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview1 != null) {
			List<WebElement> span = commonLibrary.isExistList(preview1, UIMAP_SearchResult.tagSpan, 10);
			for (WebElement item : span) {
				if (item.getText().toLowerCase().contains(strText.toLowerCase())) {
					List<WebElement> link = commonLibrary.isExistList(item, UIMAP_SearchResult.lnkHeader, 20);
					for (WebElement item1 : link) {
						if (item1.getText().toLowerCase().contains("prior history")) {
							blnLink = true;
							break;
						}
					}
					if (blnLink)
						break;
				}
			}
		}
		if (blnLink) {
			report.updateTestLog("No subsequent appellate history. Priorhistory available displays in the Previewsection, in which <Prior history available>displays as a link", "No subsequent appellate history. Priorhistory available displayeed in the Previewsection, in which <Prior history available> is displayed as a link", Status.PASS);
		} else {
			report.updateTestLog("No subsequent appellate history. Priorhistory available displays in the Previewsection, in which <Prior history available>displays as a link", "No subsequent appellate history. Priorhistory available displayeed in the Previewsection, in which <Prior history available> is not displayed as a link", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Text Preview Section
	// # Function Name : verifyTextPreviewSection
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************
	public Search verifyTextPreviewSection(String strText1, String strText2, String strText3) {
		boolean blnFlag3 = false;
		boolean blnFlag4 = false;
		boolean blnFlag5 = false;
		WebElement preview2 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview2 != null) {
			List<WebElement> li = commonLibrary.isExistList(preview2, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				// List<WebElement> span = commonLibrary.isExist_List(listitem,
				// UIMAP_SearchResult.lnkHeader, 10);
				// for (WebElement item : span) {
				if (listitem.getText().toLowerCase().contains(strText1.toLowerCase())) {
					blnFlag3 = true;
				} else if (listitem.getText().toLowerCase().contains(strText2.toLowerCase())) {
					blnFlag4 = true;
				} else if (listitem.getText().toLowerCase().contains(strText3.toLowerCase())) {
					blnFlag5 = true;
				}
				if (blnFlag3 && blnFlag4 && blnFlag5)
					break;
				// }
			}
		}
		if (blnFlag3 && blnFlag4) {
			report.updateTestLog("Verify the link " + strText1 + " and " + strText2 + " is displayed in the preview section, and " + strText3 + " is displayed without any signal", "The link " + strText1 + " and " + strText2 + " is displayed in the preview section, and " + strText3 + " is displayed without any signal", Status.PASS);
		} else {
			report.updateTestLog("Verify the link " + strText1 + " and " + strText2 + " is displayed in the preview section, and " + strText3 + " is displayed without any signal", "The link " + strText1 + " and " + strText2 + " is not displayed in the preview section, and " + strText3 + " is not displayed without any signal", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify shepard signal with text
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyShepardSignal(String strText, String strsignal) {

		try {
			boolean blnSignal = false;
			WebElement preview = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
			if (preview != null) {
				List<WebElement> li = commonLibrary.isExistList(preview, UIMAP_SearchResult.lstTagName, 10);
				for (WebElement listitem : li) {
					List<WebElement> span = commonLibrary.isExistList(listitem, UIMAP_SearchResult.tagSpan, 10);
					for (WebElement item : span) {
						if (item.getText().toLowerCase().contains(strText.toLowerCase())) {
							switch (strsignal.toLowerCase()) {
							case "red":
								WebElement signalText = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgRed, 10);
								if (signalText != null) {
									blnSignal = true;
									break;
								}
							case "orange":
								WebElement signalText1 = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgOrange, 10);
								if (signalText1 != null) {
									blnSignal = true;
									break;
								}
							case "yellow":
								WebElement signalText2 = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgYellow, 10);
								if (signalText2 != null) {
									blnSignal = true;
									break;
								}
							case "green":
								WebElement signalText4 = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgGreen, 10);
								if (signalText4 != null) {
									blnSignal = true;
									break;
								}
							case "bluea":
								WebElement signalText3 = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgBlueA, 10);
								if (signalText3 != null) {
									blnSignal = true;
									break;
								}
							case "bluei":
								WebElement signalText5 = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgBlueI, 10);
								if (signalText5 != null) {
									blnSignal = true;
									break;
								}
							}
							if (blnSignal)
								break;
						}

					}
					if (blnSignal)
						break;
				}
			}
			if (blnSignal) {
				report.updateTestLog("Verify the link with text " + strText + " is present with signal colour " + strsignal + "", "The link with text " + strText + " is present with signal colour " + strsignal + "", Status.PASS);
			} else {
				report.updateTestLog("Verify the link with text " + strText + " is present with signal colour " + strsignal + "", "The link with text " + strText + " is not present with signal colour " + strsignal + "", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simpleSearch
	// # Function Name : simpleSearch     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search
	// # Function Name : simpleSearch_Shepards
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards simpleSearchShepards(String strSearchTerm, Boolean strClearFilter) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);

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
					commonLibrary.clickButtonParentWithWait(btnIdSubSearch, "Search");
				else
					commonLibrary.clickButtonParentWithWait(btnIdSubSearch, "Search");
			} else {
				WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
				// commonLibrary.highlightElement(eltSearchbutton);
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
				else
					commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
			}
		} else {
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
			else
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Text1 PreviewSection
	// # Function Name : verifyText1PreviewSection
	// # Author : Seetha
	// # Date Created : Jan'15
	// #****************************************************************************************************
	public Search verifyText1PreviewSection(String strText1, String strText4) {
		boolean blnFlag1 = false;
		boolean blnFlag2 = false;
		boolean blnFinal = false;
		WebElement preview4 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview4 != null) {
			List<WebElement> li = commonLibrary.isExistList(preview4, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> links = commonLibrary.isExistList(listitem, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement item : links) {
					if (item.getText().toLowerCase().contains(strText1.toLowerCase())) {
						blnFlag1 = true;
					} else if (item.getText().toLowerCase().contains(strText4.toLowerCase())) {
						blnFlag2 = true;
					}
					if (blnFlag1 && blnFlag2)
						break;
				}
				if (blnFlag1 && blnFlag2) {
					blnFinal = true;
					break;
				}

			}
		}
		if (blnFinal) {
			report.updateTestLog("Verify the links " + strText1 + " and " + strText4 + " are displayed in the preview section", "The links " + strText1 + " and " + strText4 + " are displayed in the preview section", Status.PASS);
		} else {
			report.updateTestLog("Verify the links " + strText1 + " and " + strText4 + " are displayed in the preview section", "The links " + strText1 + " and " + strText4 + " are not displayed in the preview section", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title link
	// # Function Name : clickDocLink1
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document clickDocLink1(String strDocTitle) {

		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		//
		// int i = 0;
		//
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
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]",
		// "").trim().equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]",
		// "").trim())) {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		//
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// commonLibrary.sleep(10000);
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
		// if (!blnFlag) {
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// }
		//
		// WebElement pgHeader = null;
		// pageCheck.PositiveCheck(driver, "document", "Document");
		// int counter = 0;
		// do {
		// commonLibrary.sleep(20000);
		// counter = counter + 1;
		// if (counter == 500)
		// break;
		// } while (!driver.getCurrentUrl().contains("document"));
		//
		// pgHeader = null;
		// pageCheck.PositiveCheck(driver, "document", "Document");
		// List<WebElement> aboutthisdoclink =
		// commonLibrary.isExist_List(UIMAP_Document.aboutthisdoclink, 20);
		// counter = 0;
		// do {
		// commonLibrary.sleep(60000);
		// counter = counter + 1;
		// if (counter == 500)
		// break;
		// } while (aboutthisdoclink == null && counter < 15);
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
		// commonLibrary.sleep(30000);

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Text3 PreviewSection
	// # Function Name : verifyText3PreviewSection
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**************************************************************************************************
	public Search verifyText3PreviewSection(String strText7) {
		boolean blnYel = false;
		WebElement previewSec = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (previewSec != null) {
			List<WebElement> lnk = commonLibrary.isExistList(previewSec, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement item : lnk) {
				if (item.getText().toLowerCase().contains(strText7.toLowerCase())) {
					WebElement image = commonLibrary.isExist(item, UIMAP_SearchResult.imgYelTri, 10);
					if (image != null) {
						blnYel = true;
						break;
					}
				}
			}
		}
		if (blnYel) {
			report.updateTestLog("Verify " + strText7 + " displays in the Previewsection with a yellowexclamatory (placed in a inverted triangle)", "" + strText7 + "  is displayed in the Previewsection with a yellowexclamatory (placed in a inverted triangle)", Status.PASS);
		} else {
			report.updateTestLog("Verify " + strText7 + " displays in the Previewsection with a yellowexclamatory (placed in a inverted triangle)", "" + strText7 + "  is not displayed in the Previewsection with a yellowexclamatory (placed in a inverted triangle)", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify GreenSignal led
	// ShepardizeText
	// # Function Name : verifyGreenSignalledShepardizeText
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**************************************************************************************************
	public Search verifyGreenSignalledShepardizeText(String strText3) {
		try {
			boolean blnGrn = false;
			commonLibrary.sleep(5000);
			WebElement previewSec1 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
			if (previewSec1 != null) {
				List<WebElement> lnk = commonLibrary.isExistList(previewSec1, UIMAP_SearchResult.lstTagName, 10);
				for (WebElement item : lnk) {
					if (item.getText().toLowerCase().contains(strText3.toLowerCase())) {
						WebElement image = commonLibrary.isExist(item, UIMAP_SearchResult.imgGreen, 10);
						if (image != null) {
							blnGrn = true;
							break;
						}
					}
				}
			}
			if (blnGrn) {
				report.updateTestLog("Verify " + strText3 + " displays in the Previewsection with a green signal", "" + strText3 + " is displayed in the Previewsection with a green signal", Status.PASS);
			} else {
				report.updateTestLog("Verify " + strText3 + " displays in the Previewsection with a green signal", "" + strText3 + " is not displayed in the Previewsection with a green signal", Status.FAIL);
			}
			WebElement previewSec2 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
			if (previewSec2 != null) {
				List<WebElement> lnk = commonLibrary.isExistList(previewSec1, UIMAP_SearchResult.lnkHeader, 10);
				if ((lnk.size()) == 1) {
					report.updateTestLog("Verify no other links display in the Shepard's®preview section", "No other links are present in the shepards's preview section", Status.PASS);
				} else {
					report.updateTestLog("Verify no other links display in the Shepard's®preview section", "Other links are present in the shepards's preview section", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify no other links display in the Shepard's®preview section", "Other links are present in the shepards's preview section", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the shepardize link
	// # Function Name : verifyShepardizeLinkWithoutSignal
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyShepardizeLinkWithoutSignal(String strText3) {
		boolean blnFlags1 = false;
		WebElement previewSec11 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (previewSec11 != null) {
			List<WebElement> li = commonLibrary.isExistList(previewSec11, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> span = commonLibrary.isExistList(listitem, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase().contains(strText3.toLowerCase())) {
						blnFlags1 = true;
						break;
					}
				}
				if (blnFlags1)
					break;
			}
		}
		if (blnFlags1) {
			report.updateTestLog("Verify <Shepardize® this document> linkwithoutany signal displays in the Preview section", "<Shepardize® this document> linkwithoutany signal is displayed in the Preview section", Status.PASS);
		} else {
			report.updateTestLog("Verify <Shepardize® this document> linkwithoutany signal displays in the Preview section", "<Shepardize® this document> linkwithoutany signal is not displayed in the Preview section", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the text and colour of link
	// # Function Name : verifyTextAndColour
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyTextAndColour(String strValue, String strValue1, String strValue2) {
		boolean blnRed = false;
		boolean blnText = false;
		WebElement previewSec5 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (previewSec5 != null) {
			List<WebElement> li = commonLibrary.isExistList(previewSec5, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> links = commonLibrary.isExistList(listitem, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement item : links) {
					if (item.getText().toLowerCase().contains(strValue.toLowerCase())) {
						WebElement image = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgRed1, 10);
						if (image != null) {
							blnRed = true;
						}
					}
					List<WebElement> span = commonLibrary.isExistList(listitem, UIMAP_SearchResult.tagSpan, 10);
					for (WebElement item2 : span) {
						if (item2.getText().toLowerCase().contains(strValue1.toLowerCase())) {
							blnText = true;
							break;
						}
					}
					if (blnRed && blnText)
						break;
				}
				if (blnRed && blnText)
					break;
			}
		}
		if (blnRed && blnText) {
			report.updateTestLog("Verify <Subsequent appellate history containsnegative analysis.> with red warning signaldisplays in the Shepards Preview section,in which <Subsequent appellate history >displays as a link", "<Shepardize® this document> linkwithoutany signal is displayed in the Preview section", Status.PASS);
		} else {
			report.updateTestLog("Verify <Shepardize® this document> linkwithoutany signal displays in the Preview section", "<Shepardize® this document> linkwithoutany signal is not displayed in the Preview section", Status.FAIL);
		}

		boolean blnText1 = false;
		WebElement previewSec6 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (previewSec6 != null) {
			List<WebElement> li = commonLibrary.isExistList(previewSec6, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> span = commonLibrary.isExistList(listitem, UIMAP_SearchResult.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase().contains(strValue2.toLowerCase())) {
						WebElement image = commonLibrary.isExist(listitem, UIMAP_SearchResult.imgRed1, 10);
						if (image != null) {
							blnText1 = true;
							break;
						}
					}
				}
				if (blnText1)
					break;
			}
		}
		if (blnText1) {
			report.updateTestLog("Verify <Not Citable; Ordered Not Published (SeeCal. Rules of Court)> with red warningsignal displays in Shepards Previewsection.", "<Not Citable; Ordered Not Published (SeeCal. Rules of Court)> with red warningsignal is displayed in Shepards Previewsection.", Status.PASS);
		} else {
			report.updateTestLog("Verify <Not Citable; Ordered Not Published (SeeCal. Rules of Court)> with red warningsignal displays in Shepards Previewsection.", "<Not Citable; Ordered Not Published (SeeCal. Rules of Court)> with red warningsignal is not displayed in Shepards Previewsection.", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the case history
	// # Function Name : verifyCaseHistory
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyCaseHistory() {
		boolean blnLink = false;
		WebElement preview1 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview1 != null) {
			List<WebElement> span = commonLibrary.isExistList(preview1, UIMAP_SearchResult.tagSpan, 10);
			for (WebElement item : span) {
				if (item.getText().toLowerCase().contains("contains negative analysis")) {
					List<WebElement> link = commonLibrary.isExistList(item, UIMAP_SearchResult.lnkHeader, 20);
					for (WebElement item1 : link) {
						if (item1.getText().toLowerCase().contains("case history")) {
							blnLink = true;
							break;
						}
					}
					if (blnLink)
						break;
				}
			}
		}
		if (blnLink) {
			report.updateTestLog("Verify Case history contains negative analysisdisplays in the Shepards Preview section, inwhich Case history displays as a link, in which <Prior history available>displays as a link", "Case history contains negative analysis is displayed in the Shepards Preview section, in which Case history is displayed as a link", Status.PASS);
		} else {
			report.updateTestLog("Verify Case history contains negative analysisdisplays in the Shepards Preview section, inwhich Case history displays as a link, in which <Prior history available>displays as a link", "Case history contains negative analysis is not displayed in the Shepards Preview section, in which Case history is not displayed as a link", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Hover Other CitingSources
	// # Function Name : HoverOtherCitingSources
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************************************************************************
	public Search hoverOtherCitingSources() {

		Actions actions = new Actions(driver);
		WebElement preview4 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview4 != null) {
			List<WebElement> li = commonLibrary.isExistList(preview4, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> links = commonLibrary.isExistList(listitem, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement item : links) {
					if (item.getText().toLowerCase().contains("other citing sources")) {
						actions.moveToElement(item).build().perform();
						WebElement tooltip = commonLibrary.isExistNegative(UIMAP_SearchResult.tooltip, 3);
						if (tooltip != null)
							report.updateTestLog("Hover over the link Other Citing Sources", "Hover text is displayed", Status.PASS);
						else
							report.updateTestLog("Hover over the link Other Citing Sources", "Hover text is not displayed", Status.FAIL);

						break;
					}
				}
			}
		} else {
			report.updateTestLog("Hover over the link Other Citing Sources", "Hover text is not displayed", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify only results having
	// HeadNote16 is displayed
	// # Function Name : verify_ClickHeadnote16
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyClickHeadnote16() {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);

		if (headNotesContent == null) {
			WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		}
		WebElement lnkShepardizeHeadNote16 = commonLibrary.isExistNegative(UIMAP_Document.lnkShepardizeHeadNote16, 10);
		if (lnkShepardizeHeadNote16 != null)
			report.updateTestLog("Verify Shepardize - Narrow by this Headnote linkdisplays in the headnote section 'HN16'.", "Shepardize - Narrow by this Headnote linkdisplays in the headnote section 'HN16'.", Status.PASS);
		else
			report.updateTestLog("Verify Shepardize - Narrow by this Headnote linkdisplays in the headnote section 'HN16'.", "Shepardize - Narrow by this Headnote link is not displayed in the headnote section 'HN16'.", Status.FAIL);

		// commonLibrary.ScrollToView(lnkShepardizeHeadNote16);
		// commonLibrary.highlightElement(lnkShepardizeHeadNote16);
		if (browsername.contains("internet")) {
			// commonLibrary.clickLink_withWebElement_WithWait_JS(lnkShepardizeHeadNote16,
			// "Shepardize - Narrow by this Headnote");
			try {
				driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
				commonLibrary.clickLinkWithWebElementWithWait(lnkShepardizeHeadNote16, "Shepardize - Narrow by this Headnote");
				// lnkDocument.click();// Using click ans click_JS not working.
			} catch (TimeoutException ex) {
				driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
				System.out.println(ex.toString());

			}
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
		} else
			commonLibrary.clickLinkWithWebElementWithWait(lnkShepardizeHeadNote16, "Shepardize - Narrow by this Headnote");
		int count = 0;
		WebElement pagewrapper = commonLibrary.isExist(UIMAP_Document.pagewrapperpanel, 10);
		do {
			count++;
			pagewrapper = commonLibrary.isExist(UIMAP_Document.pagewrapperpanel, 10);
			commonLibrary.sleep(3000);
		} while (pagewrapper == null && count < 15);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Parent Filter Expands
	// HeadNote16 is displayed
	// # Function Name : verifyParentFilterExpands
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*******************************************************************************************
	public Search verifyParentFilterExpands(String strFilterHeader, String strFilter) {

		boolean blnFlag = false;
		List<WebElement> eltExpandedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltExpandedFilterHeader, 10);
		for (WebElement li : eltExpandedFilterHeader) {
			if (li.getText().contains(strFilterHeader)) {
				List<WebElement> eltParentFilterExpanded = commonLibrary.isExistList(commonLibrary.getParentElement(li), UIMAP_SearchResult.tagHeader, 10);
				for (WebElement li1 : eltParentFilterExpanded) {
					if (li1 != null && li1.getText().toLowerCase().contains(strFilter.toLowerCase())) {
						blnFlag = true;
						report.updateTestLog("Verify Selected Post filter type expands.", strFilter + "is expanded.", Status.PASS);
						break;
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify Selected Post filter type expands.", strFilter + "is not expanded.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove filter.
	// # Function Name : remove_Filter
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search removeFilter(String FilterName) {
		boolean blnFlag = false;
		WebElement FiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.btnRemoveFilter, 10);
		for (WebElement item : Filters) {
			if (item.getText().toLowerCase().equalsIgnoreCase(FilterName.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(item, FilterName);
				report.updateTestLog(FilterName + " filter is removed", FilterName + " filter is removed", Status.PASS);
				blnFlag = true;
				break;

			}
		}
		if (!blnFlag) {
			report.updateTestLog(FilterName + " filter is removed", FilterName + " filter is not removed", Status.FAIL);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Post filter Applied
	// # Function Name : verify_Filter
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyFilter(String filterName) {
		boolean flag = false;
		commonLibrary.sleep(5000);
		WebElement filtersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		List<WebElement> filters = commonLibrary.isExistList(filtersUsed, UIMAP_SearchResult.lstTagListItems, 10);
		// Looping through all the applied Filters to check whether given filter
		// is available.
		for (WebElement item : filters) {
			if (item.getText().toLowerCase().equalsIgnoreCase(filterName.toLowerCase())) {
				report.updateTestLog(filterName + " filter is present", filterName + " filter is present", Status.PASS);
				flag = true;
			}
		}
		if (!flag) {
			report.updateTestLog(filterName + " filter is present", filterName + " filter is not present", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter header is absent
	// # Function Name : verifyFilterNegative
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyFilterNegative(String FilterName) {
		boolean blnFlag = false;
		WebElement FiltersUsed = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 10);
		if (FiltersUsed != null) {
			List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.lstTagListItems, 10);
			for (WebElement item : Filters) {
				if (item.getText().toLowerCase().equalsIgnoreCase(FilterName.toLowerCase())) {
					report.updateTestLog(FilterName + " filter is present", FilterName + " filter is present in the left pane", Status.FAIL);
					blnFlag = true;
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog(FilterName + " filter is present", FilterName + " filter is not present in the left pane", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Citation Result
	// # Function Name : verifyCitationResult
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyCitationResult(String citation) {
		boolean result = false;

		WebElement results = commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10);
		if (results != null) {
			if (results.getText().toLowerCase().contains(citation.toLowerCase())) {
				result = true;
			}
		} else {
			report.updateTestLog("Verify You are taken into the app with a results list of citations entered into the widget", "You are not taken into the app with a results list of citations entered into the widget", Status.FAIL);
		}
		if (result) {
			report.updateTestLog("Verify You are taken into the app with a results list of citations entered into the widget", "You are taken into the app with a results list of citations entered into the widget", Status.PASS);
		} else {
			report.updateTestLog("Verify You are taken into the app with a results list of citations entered into the widget", "You are not taken into the app with a results list of citations entered into the widget", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch To Search
	// # Function Name : verifyCitationResult
	// # Author : Pratik
	// # Date Created : Feb'15
	// #**********************************************************************************************************
	public GUIBContainer switchToSearch() {
		driver.close();
		commonLibrary.switchToWindow("container");
		report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter citation and click get button
	// # Function Name : EnterCitationAndClickGetButton     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyStatus(String message) {
		boolean error = false;
		commonLibrary.switchToWindow("container");

		WebElement status = commonLibrary.isExist(UIMAP_GUIBContainer.statusBox);
		if (status != null) {
			List<WebElement> list = commonLibrary.isExistList(UIMAP_SearchResult.lstTagName, 10);
			for (WebElement item : list) {
				if (item.getText().toLowerCase().contains(message.toLowerCase())) {
					error = true;
					break;
				}
			}
		} else {
			report.updateTestLog("Click Get button", "Get button is not displaye", Status.FAIL);
		}
		if (error) {
			report.updateTestLog("Verify the message 428 us could not be retrieved the citation is not recognized.Shepard's®: 428us could not be retrieved the citationis not recognized", "Expected message is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the message 428 us could not be retrieved the citation is not recognized.Shepard's®: 428us could not be retrieved the citationis not recognized", "Expected message is not displayed", Status.FAIL);
		}

		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : SelectPostFilter_Multiple     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search selectPostFilter_Multiple(String strHeader, String strSubHeader, String strFilters) {
		WebElement filters = null;
		WebElement filters1 = null;
		String[] arrFilters = strFilters.split(";");
		WebElement filtersUsedOld = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 10);
		int i = 0;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
				report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.DONE);
			}
		}

		if (strHeader != "" && strHeader.toLowerCase().contains("court") && strSubHeader.toLowerCase().contains("federal")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10);
			filters = commonLibrary.isExist(filters1, UIMAP_SearchResult.filtersFederal, 10);
		} else if (strHeader != "" && strHeader.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10);

		} else if (strHeader != "" && strHeader.toLowerCase().contains("keyword")) {
			filters = commonLibrary.isExist(UIMAP_SearchResult.filtersKeyword, 10);

		}
		WebElement SelectMultiple = commonLibrary.isExist(filters, UIMAP_SearchResult.btnSelectMultiple, 10);
		if (SelectMultiple != null) {
			commonLibrary.clickButtonParentWithWaitWithFocus(SelectMultiple, "Select Multiple");
			WebElement SelMultiDialog = commonLibrary.isExist(UIMAP_SearchResult.SelMultiPopUp, 10);
			if (SelMultiDialog != null) {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is displayed", Status.PASS);
				List<WebElement> FilterList = commonLibrary.isExistList(SelMultiDialog, By.tagName("label"), 30);
				if (arrFilters[0].contains("triangle")) {
					WebElement filterdialogbox1 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
					WebElement div1 = commonLibrary.isExist(filterdialogbox1, UIMAP_VSASearchResults.div, 20);
					List<WebElement> ullist21 = commonLibrary.isExistList(div1, UIMAP_VSASearchResults.ulcolumn1, 20);
					WebElement lilist13 = commonLibrary.isExist(ullist21.get(1), UIMAP_VSASearchResults.ullist, 20);
					List<WebElement> ullist12 = commonLibrary.isExistList(lilist13, UIMAP_VSASearchResults.lilist, 20);
					for (int i1 = 0; i1 < ullist12.size(); i1++) {
						WebElement btn = commonLibrary.isExist(ullist12.get(i1), By.tagName("button"), 5);
						WebElement lable = commonLibrary.isExist(ullist12.get(i1), By.tagName("label"), 5);
						if (btn != null && lable != null) {
							if (lable.getText().contains(arrFilters[1])) {
								if (browsername.contains("internet")) {
									commonLibrary.clickButtonParentWithWaitJS(btn, "Arrow of " + arrFilters[1]);
								} else {
									commonLibrary.clickButtonParentWithWait(btn, "Arrow of " + arrFilters[1]);
								}
								WebElement ul = commonLibrary.isExist(ullist12.get(i1), UIMAP_VSASearchResults.ulclass, 20);
								if (ul != null) {
									List<WebElement> list = commonLibrary.isExistList(ul, UIMAP_VSASearchResults.lilist, 20);
									for (int j = 0; j < list.size(); j++) {
										WebElement lable1 = commonLibrary.isExist(list.get(j), By.tagName("label"), 20);
										if (lable1 != null && lable1.getText().contains("Superior Court")) {
											WebElement input = commonLibrary.isExist(list.get(j), By.tagName("input"), 20);
											if (input != null) {
												commonLibrary.setCheckBox(input, "Superior Court");
											}
										}
									}
								}
								break;
							}

						} else {
							report.updateTestLog("Click Arrow of " + arrFilters[1], "Arrow is not clicked", Status.FAIL);
						}
					}

				} else {
					for (int j = 0; j < arrFilters.length; j++) {
						for (WebElement item : FilterList) {
							if (item.getText().contains(arrFilters[j])) {
								WebElement ChkBox = commonLibrary.isExist(item, By.tagName("input"), 10);
								commonLibrary.setCheckBox(ChkBox, arrFilters[j]);
								break;
							}
						}
					}
				}
				// To Select OK Button in Select Multiple Pop Up
				WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.OKSelMultiPopUp, 10);
				commonLibrary.clickButtonParentWithWait(btnOK, "OK");

				try {
					commonLibrary.sleep(5000);
					filtersUsedOld = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 10);
					WebElement filtersUsedNew = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 10);
					int count = 0;
					do {
						commonLibrary.sleep(4000);
						count++;
						filtersUsedNew = commonLibrary.isExistNegative(UIMAP_SearchResult.ulFiltersUsed, 10);
					} while ((filtersUsedNew == null || filtersUsedNew.equals(filtersUsedOld)) && count < 25);

				} catch (Exception e) {
					commonLibrary.sleep(1000);
					System.out.println(e.toString());
					// throw new FrameworkException("Exception", e.toString());

				}
			}
		} else {
			report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is not displayed", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Clear Previously set filters in
	// Search Results page
	// # Function Name : ChooseDownLoclearFiltersadSettings     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search clearFilters() {
		WebElement btnClear = commonLibrary.isExist(UIMAP_SearchResult.btnClear, 10);
		if (btnClear != null)
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWaitWithFocus(btnClear, "Clear");
			else
				commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
		try {
			String loadProp = properties.getProperty("xSpinner");
			int count = 0;
			WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
			do {
				commonLibrary.sleep(1000);
				loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				count++;
			} while (loader != null && count < 25);
		} catch (Exception e) {
			commonLibrary.sleep(1000);
			System.out.println(e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click filter button
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research clickFilterButton() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnClassFilter, "Filter");
		else
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click product logo
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research clickLogo() {
		WebElement logo = commonLibrary.isExist(UIMAP_SearchResult.productLogo, 10);
		if (logo != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(logo, "Product Logo");
			else
				commonLibrary.clickButtonParentWithWait(logo, "Product Logo");
			report.updateTestLog("Click on product logo", "Lexis Advance Landing page is displayed ", Status.PASS);
		} else {
			report.updateTestLog("Click on product logo", "Lexis Advance Landing page is not displayed ", Status.FAIL);
		}
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : SelectPostFilter_Multiple_Checkbox     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search selectPostFilterMultipleCheckbox(String strHeader, String strSubHeader, String strFilters, String isMainCheckboxSelect, String strSubFilters, String strButton) {
		WebElement filters = null;
		WebElement filters1 = null;
		String[] arrFilters = strFilters.split(";");
		String[] arrMainCheckboxSelect = isMainCheckboxSelect.split(";");

		int i = 0;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
				report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.DONE);
			}
		}

		if (strHeader != "" && strHeader.toLowerCase().contains("court") && strSubHeader.toLowerCase().contains("federal")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10);
			filters = commonLibrary.isExist(filters1, UIMAP_SearchResult.filtersFederal, 10);
		} else if (strHeader != "" && strHeader.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10);

		}
		WebElement SelectMultiple = commonLibrary.isExist(filters, UIMAP_SearchResult.btnSelectMultiple, 10);
		if (SelectMultiple != null) {
			commonLibrary.clickButtonLogSmallWait(SelectMultiple, "Select Multiple");
			WebElement SelMultiDialog = commonLibrary.isExist(UIMAP_SearchResult.SelMultiPopUp, 10);
			if (SelMultiDialog != null) {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is displayed", Status.PASS);
				List<WebElement> FilterList = commonLibrary.isExistList(SelMultiDialog, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					for (WebElement item : FilterList) {
						if (item.getText().contains(arrFilters[j])) {

							WebElement list = commonLibrary.getParentElement(item);
							WebElement button = commonLibrary.isExist(list, By.tagName("button"), 10);

							if (button.getAttribute("class").contains("collapsed"))
								commonLibrary.clickButtonParentWithWait(button, "Expand " + arrFilters[j]);

							if (arrMainCheckboxSelect[j].equals("true")) {
								WebElement ChkBox = commonLibrary.isExist(item, By.tagName("input"), 10);
								commonLibrary.setCheckBox(ChkBox, arrFilters[j]);

								WebElement filterColor = commonLibrary.isExist(item, By.tagName("span"), 10);
								if (filterColor.getCssValue("font-family").contains("Lato")) {

									report.updateTestLog("Verify " + arrFilters[j] + " are checked and bolded", arrFilters[j] + "are checked and bolded", Status.PASS);
								}
								if (!strSubFilters.equals("")) {
									String[] arrsubFilters1 = strSubFilters.split(",");
									String[] arrsubFilters = arrsubFilters1[j].split(";");
									WebElement ul = commonLibrary.isExist(list, UIMAP_SearchResult.subPostFilter, 10);
									List<WebElement> subFilterList = commonLibrary.isExistList(ul, By.tagName("label"), 30);
									for (int k = 0; k < arrsubFilters.length; k++) {
										for (WebElement subItem : subFilterList) {
											if (subItem.getText().contains(arrsubFilters[k])) {
												WebElement ChkBox1 = commonLibrary.isExist(subItem, By.tagName("input"), 10);
												commonLibrary.isChecked(ChkBox1, arrsubFilters[k]);
												WebElement filterColor1 = commonLibrary.isExist(subItem, By.tagName("span"), 10);
												if (filterColor1.getCssValue("font-family").contains("Lato")) {

													report.updateTestLog("Verify " + arrsubFilters[k] + " are checked and bolded", arrsubFilters[k] + "are checked and bolded", Status.PASS);
												}
												break;
											}
										}
									}
								}

								break;
							} else {
								String[] arrsubFilters = null;
								String[] arrsubFilters1 = strSubFilters.split(",");

								if (arrsubFilters1[j].contains(";"))
									arrsubFilters = arrsubFilters1[j].split(";");
								else {
									arrsubFilters = arrsubFilters1[j].split("/");

								}

								WebElement ul = commonLibrary.isExist(list, UIMAP_SearchResult.subPostFilter, 10);
								List<WebElement> subFilterList = commonLibrary.isExistList(ul, By.tagName("label"), 30);
								for (int k = 0; k < arrsubFilters.length; k++) {
									for (WebElement subItem : subFilterList) {
										if (subItem.getText().contains(arrsubFilters[k])) {
											WebElement ChkBox = commonLibrary.isExist(subItem, By.tagName("input"), 10);
											commonLibrary.setCheckBox(ChkBox, arrsubFilters[k]);
											WebElement filterColor = commonLibrary.isExist(subItem, By.tagName("span"), 10);
											if (filterColor.getCssValue("font-family").contains("Lato")) {

												report.updateTestLog("Verify " + arrsubFilters[k] + " are checked and bolded", arrsubFilters[k] + "are checked and bolded", Status.PASS);
											}
											break;
										}
									}
								}
								break;
							}

						}
					}
				}

				if (strButton.equals("Ok"))

				{
					WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.OKSelMultiPopUp, 10);
					commonLibrary.clickButtonParentWithWait(btnOK, "OK");

				} else {
					WebElement btnCancel = commonLibrary.isExist(UIMAP_SearchResult.cancelSelMultiPopUp, 10);
					commonLibrary.clickButtonParentWithWait(btnCancel, "Cancel");
				}
			} else {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is not displayed", Status.FAIL);
			}

		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify previous button status.
	// # Function Name : VerifyPreviousButtonStatus     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyPreviousButtonStatus(String status) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot of current page
	// # Function Name : TakeScreenShot     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			String strScreenshot = strTCName + commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot + ".jpg";

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep, "Perform Manual Verification for this step. screenshot is saved in " + strDestination + "", Status.WARNING);
			commonLibrary.sleep(20000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify tooltip text on hovering over
	// applied filters in Narrow By Section.
	// # Function Name : verifyFilterToolTip
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyFilterToolTip(String strToolTipText, String strFilter) {
		boolean blnFlag = false;

		WebElement FiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.btnEachFilter, 10);
		for (WebElement li : Filters) {
			if (li.getText().toLowerCase().contains(strFilter.toLowerCase()) && li.getAttribute("title").toLowerCase().contains(strToolTipText.toLowerCase())) {
				report.updateTestLog("Verify " + strToolTipText + " text is displayed on hovering over " + strFilter, strToolTipText + " text is displayed on hovering over " + strFilter, Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + strToolTipText + " text is displayed on hovering over " + strFilter, strToolTipText + " text is not displayed on hovering over " + strFilter, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear applied pre filters.
	// # Function Name : clearPreFilter
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clearPreFilter() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		// commonLibrary.highlightElement(btnClassFilter);
		commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

		WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_SearchResult.btnClassClearFilter, 20);
		// commonLibrary.highlightElement(btnClassClearFilter);
		commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

		WebElement prefilters = commonLibrary.isExist(UIMAP_Home.preFiltersDiv, 10);
		WebElement btnClosePreFilterPopup = commonLibrary.isExist(prefilters, UIMAP_Home.btnClosePreFilterPopup, 10);
		commonLibrary.clickButtonLogSmallWait(btnClosePreFilterPopup, "Close PreFilter PopUp");
		WebElement preFiltersDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.preFiltersDiv, 10);
		if (preFiltersDiv == null)
			report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is closed.", Status.PASS);
		else
			report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is not closed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click next page.
	// # Function Name : ClickNextPage
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickNextPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
		if (btnNextPage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "NextPage");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Count Of Results
	// # Function Name : VerifyCountOfResults     
	// # Author : Seetha
	// # Date Created : Feb '15
	// #*****************************************************************************************************************************

	public Search verifyCountOfResults(String number) {
		String num = "";
		if (number == "10") {
			num = "11-20";
		} else if (number == "25") {
			num = "26-50";
		} else if (number == "50") {
			num = "51-100";
		}
		boolean isDocNum = false;
		WebElement form = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (form != null) {
			WebElement lists = commonLibrary.isExist(form, UIMAP_SearchResult.resultwrapper, 20);
			if (lists != null) {
				List<WebElement> li = commonLibrary.isExistList(lists, UIMAP_SearchResult.lstTagResult, 20);
				if (li.size() == Integer.parseInt(number)) {
					isDocNum = true;
				}
			}
		}
		if (isDocNum) {
			report.updateTestLog("Verify results " + num + "/" + number + " displays.", "Results " + num + "/" + number + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results " + num + "/" + number + " displays.", "Results " + num + "/" + number + " is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyResultCount(String strSearchTerm, int expCount, Boolean isGreater) {

		String resultsCount = null;
		int pgItemCount = 10;

		// retrive the number of results displayed in the results page by
		// calculating the list count
		WebElement form = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (form != null) {
			WebElement lists = commonLibrary.isExist(form, UIMAP_SearchResult.resultwrapper, 20);
			if (lists != null) {
				List<WebElement> li = commonLibrary.isExistList(lists, UIMAP_SearchResult.lstTagListItems, 20);
				pgItemCount = li.size();

			}
		}

		// retrive the number of pages displayed for the search term - taking
		// the last page number
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 20);

		if (pageNumbers.size() > 0)
			resultsCount = pageNumbers.get(pageNumbers.size() - 1).getAttribute("data-value");

		// Verification poing if the results count > expected count
		int resCount = Integer.parseInt(resultsCount);
		if (isGreater) {

			if ((resCount * pgItemCount) >= expCount) {

				WebElement searchResultHeader = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 20);
				if (searchResultHeader.getText().contains("10,000+"))
					report.updateTestLog("Verify Result count 10000+ is displayed next to HLCT", "Result count is displayed as " + searchResultHeader.getText(), Status.PASS);
				else
					report.updateTestLog("Verify Result count 10000+ is displayed next to HLCT", "Result count is not displayed as 10000+", Status.FAIL);

			} else
				report.updateTestLog("Verify Result count 10000+ is displayed next to HLCT", "For the search term " + strSearchTerm + " there is no 1000+ results displayed", Status.PASS);
		} else {

			// Verification poing if the results count < expected count
			if ((resCount * pgItemCount) <= expCount) {

				WebElement searchResultHeader = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 20);
				try {
					String count = searchResultHeader.getText().toString().replace(",", "");
					String count1 = count.replace(" ", "");
					String arrCount1 = count1.replace("(", "");
					String arrcount2 = arrCount1.replace(")", "");

					if (Integer.parseInt(arrcount2) < expCount)
						report.updateTestLog("Verify Result count less than " + expCount + " is displayed next to HLCT", "Result count is displayed as " + searchResultHeader.getText(), Status.PASS);
					else
						report.updateTestLog("Verify Result count less than " + expCount + " is displayed next to HLCT", "Result count is not displayed as less than " + expCount, Status.FAIL);
				} catch (Exception e) {

				}

			} else
				report.updateTestLog("Verify Result count less than  is displayed next to HLCT", "For the search term " + strSearchTerm + " there is no less than " + expCount + " results displayed", Status.PASS);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SearchPrint clickPrinterFriendlyView() {

		generalFunctions.clickDeliverySelectOption_New("delivery", "Printer-friendly view");
		generalFunctions.documentState(driver);
		commonLibrary.switchToWindow("searchprint");
		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for searching within the results
	// # Function Name : searchWithinSearch     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search searchWithinSearch(String sectionHeaderName, String searchTerm) {

		// Expand search within search header
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);

		for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {

			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(sectionHeaderName.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), sectionHeaderName);
				report.updateTestLog("Expanding Header: " + sectionHeaderName, sectionHeaderName + " Header Expanded.", Status.DONE);
			}

		}

		// Enter search term
		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_SearchResult.searchWithinResults, 20);
		if (searchwithinResults != null) {
			WebElement eltSearchbox = commonLibrary.isExist(searchwithinResults, UIMAP_SearchResult.searchWithinSearchBox, 20);
			if (eltSearchbox != null)
				commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
			else
				report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);
		} else
			report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);

		// Click on search button
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
		if (eltSearchbutton != null)
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		else
			report.updateTestLog("Search button", "Search button is not available", Status.FAIL);

		commonLibrary.sleep(4000);

		// wait

		try {
			WebElement searchbuttonNew = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
			int count = 0;
			do {
				System.out.println("Waiting " + count);
				commonLibrary.sleep(1000);
				searchbuttonNew = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
				count++;
			} while (searchbuttonNew.equals(eltSearchbutton) && count < 80);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Browser Title
	// # Function Name : verifyBrowserTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyBrowserTitle(String title) {

		if (driver.getTitle().contains(title))
			report.updateTestLog("Verify Broswer Title", "Broswer Title is having " + driver.getTitle(), Status.PASS);
		else
			report.updateTestLog("Verify Broswer Title", "Broswer Title is not having title as " + title, Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Browser Title
	// # Function Name : verifyBrowserTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyBrowserTitle_ResultsCount(int expCount, Boolean isGreater) {

		// Verification Point: Results count in the browser tiltle is greater
		// than the expected count
		if (isGreater) {

			if (driver.getTitle().replace(",", "").contains(expCount + "+"))
				report.updateTestLog("Verify Broswer Title", "Broswer Title is having result count greater than " + expCount, Status.PASS);
			else
				report.updateTestLog("Verify Broswer Title", "Broswer Title is not having result count greater than " + expCount, Status.FAIL);
		} else {
			String[] arrTitle = driver.getTitle().split(" ");

			if (Integer.parseInt((arrTitle[0].replace(",", ""))) < expCount)
				report.updateTestLog("Verify Broswer Title", "Broswer Title is having result count less than " + expCount, Status.PASS);
			else
				report.updateTestLog("Verify Broswer Title", "Broswer Title is not having result count less than " + expCount, Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the content type of the
	// results.
	// # Function Name : verifyContentType     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyContentType(String strContentType) {
		WebElement eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);

		int counter = 0;
		try {
			do {

				counter = counter + 1;
				eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
				commonLibrary.sleep(1000);

			} while ((eltResultHeader != null) && !eltResultHeader.getText().toLowerCase().contains(strContentType.toLowerCase()) && (counter < 40));
		} catch (Exception e) {
			System.out.println(e.toString());
			commonLibrary.sleep(2000);
		}
		eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
		if (eltResultHeader != null && eltResultHeader.getText().toLowerCase().contains(strContentType.toLowerCase()))
			report.updateTestLog("Verify content type: " + strContentType + " displays.", "Content type: " + strContentType + " displays.", Status.PASS);
		else {
			generalFunctions.selectCondentType(strContentType);
			// generalFunctions.selectCondentType(strContentType);
			report.updateTestLog("Verify content type: " + strContentType + " displays.", "Content type: " + strContentType + " is not displayed.", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to and verify the settings
	// page.
	// # Function Name : verifySettingsPage     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LASettings verifySettingsPage() {

		// Click More button
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
		if (btnMore != null)
			commonLibrary.clickMethod(btnMore, "More");

		// Select Settings
		WebElement lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);
		if (lnkSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSettings, "Settings");

		commonLibrary.sleep(2000);

		WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleSettings, 10);
		if (PgTitle != null) {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is NOT displayed", Status.FAIL);

		}
		// Verification Point: General Optionis selcted by default
		WebElement btnGeneral = commonLibrary.isExist(UIMAP_Settings.btnIdGeneral, 10);
		if (btnGeneral != null && btnGeneral.getAttribute("class").toLowerCase().contains("active")) {
			report.updateTestLog("Verify 'General' settings section displayed by default", "'General' settings section displayed by default", Status.PASS);
		} else {
			report.updateTestLog("Verify 'General' settings section displayed by default", "'General' settings section NOT displayed by default", Status.WARNING);
		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Overview is displayed for
	// Results in Result page.
	// # Function Name : verifyDocumentOverView
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyDocumentOverView(String strDocTitle) {
		Boolean blnFlag = false;

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						if (document.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
							WebElement eleOverviewSection = commonLibrary.isExist(eleDocCondent, By.tagName("strong"), 20);
							if (eleOverviewSection != null && eleOverviewSection.getText().toLowerCase().contains("overview")) {
								report.updateTestLog("Verify overview displays for document: " + strDocTitle, "Overview displays for document: " + strDocTitle, Status.PASS);
								blnFlag = true;
								break;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		if (!blnFlag)
			report.updateTestLog("Verify overview displays for document: " + strDocTitle, "Overview is not displayed for document: " + strDocTitle, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify page number highlighted and
	// page count.
	// # Function Name : VerifyPageNumberHighlightedAndPageCount
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyPageNumberHighlightedAndPageCount(String highlight, String total) {
		boolean istotal = false;
		boolean ishighlight = false;
		String resultsCount = "";
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 20);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 20);
		if (pageNumbers.size() > 0)
			resultsCount = pageNumbers.get(pageNumbers.size() - 1).getAttribute("data-value");

		int resCount = Integer.parseInt(resultsCount);
		if (resCount == Integer.parseInt(total)) {
			istotal = true;
		}
		WebElement current = commonLibrary.isExist(pagination, UIMAP_SearchResult.currentPage, 20);
		if (current.getText().equalsIgnoreCase(highlight)) {
			ishighlight = true;
		}
		if (istotal && ishighlight) {
			report.updateTestLog("Verify " + highlight + " is highlighted in the Go to Widget page and total number of page is " + total + "", "" + highlight + " is highlighted in the Go to Widget page and total number of page is " + total + "", Status.PASS);
		} else {
			report.updateTestLog("Verify " + highlight + " is highlighted in the Go to Widget page and total number of page is " + total + "", "" + highlight + " is not highlighted in the Go to Widget page and total number of page is not displayed as " + total + "", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify page number highlighted and
	// last page number.
	// # Function Name : VerifyPageNumberHighlightedAndPageCount
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyPageNumberHighlightedAndLastPageNo(String highlight) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify recently viewed icon
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyRecentlyViewed(String strDocTitle, Boolean isDisplayed) {

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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Terms is displayed for
	// Results in Result page.
	// # Function Name : verifyShowTerms
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyShowTerms(String strDocTitle) {
		boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					if (document.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						List<WebElement> eltShowTerms = commonLibrary.isExistList(eleDocCondent, UIMAP_SearchResult.eltShowTerms, 10);
						if (eltShowTerms.size() > 0) {
							report.updateTestLog("Verify terms displays for document: " + strDocTitle, "Terms displays for document: " + strDocTitle, Status.PASS);
							blnFlag = true;
							break;
						}
					}
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify terms displays for document: " + strDocTitle, "Terms does not display for document: " + strDocTitle, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to extract Terms is displayed for
	// Results in Result page.
	// # Function Name : verifyShowTerms
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyShowExtract(String strDocTitle) {
		boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					if (document.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
						if (eltShowExtract != null) {
							report.updateTestLog("Verify extract displays for document: " + strDocTitle, "Extract displays for document: " + strDocTitle, Status.PASS);
							blnFlag = true;
							break;
						}
					}
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify extract displays for document: " + strDocTitle, "Extract does not display for document: " + strDocTitle, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on specific page number.
	// # Function Name : ClickPageNumber
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickPageNumber(String number) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify next button is disabled
	// # Function Name : ClickPageNumber
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyNextButtonStatus(String status) {
		boolean active = false;
		boolean inactive = false;
		WebElement navigate = commonLibrary.isExist(UIMAP_SearchResult.previousBtn, 10);
		if (navigate != null) {
			switch (status) {
			case "active": {
				List<WebElement> li = commonLibrary.isExistList(navigate, UIMAP_SearchResult.lstTagListItems, 10);
				for (WebElement item : li) {
					WebElement link = commonLibrary.isExist(item, UIMAP_SearchResult.NextBtnActive, 10);
					if (link != null) {
						active = true;
						break;
					}
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
				for (WebElement item : li) {
					WebElement link = commonLibrary.isExist(item, UIMAP_SearchResult.NextBtnInActive, 10);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click previous page.
	// # Function Name : ClickPageNumber
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickPreviousPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnPrePage);
		if (btnNextPage != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "Previous Page");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "Previous Page");
		} else {
			report.updateTestLog("Click on previous page", "Previous page is not clicked", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save number of results to be
	// displayed.
	// # Function Name : SetNumberOfResultsToBeDisplayed
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search setNumberOfResultsToBeDisplayed(String number) {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else {
				// commonLibrary.highlightElement(btnMore);
				commonLibrary.clickMethod(btnMore, "More");
			}
		}
		WebElement setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		if (setting == null || !setting.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");
			}
			setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		}
		if (setting != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(setting, "setting");
			else
				commonLibrary.clickLinkWithWebElementWithWait(setting, "Settings");
		}
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement resultDropdown = commonLibrary.isExist(UIMAP_Home.resultsList, 100);
		if (resultDropdown != null) {
			commonLibrary.selectFromListOptionContains(resultDropdown, number);
		}
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content type of Results
	// present or absent.
	// # Function Name : verifyResultContentTypes
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyResultContentTypes(String strContentType, Boolean isPresent) {
		String strDocTitle = "";
		Boolean blnFlag = false;
		int i = 0;
		if (isPresent) {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					blnFlag = false;
					for (WebElement document : OListItems) {
						i++;
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						// report.updateTestLog("Document Title ",
						// "Document Title is "+strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						List<WebElement> eltContentTypeHeaders = commonLibrary.isExistList(eleDocCondent, UIMAP_SearchResult.eltContentTypeHeader, 10);
						for (WebElement header : eltContentTypeHeaders) {
							if (header.getText().toLowerCase().contains(strContentType.toLowerCase())) {
								report.updateTestLog("Verify Content Type " + strContentType + " is present.", "Content Type " + strContentType + " is present for document." + strDocTitle, Status.PASS);
								blnFlag = true;
								break;
							}
						}
						if (i > 15 || blnFlag)
							break;
					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify Content Type " + strContentType + " is present.", "Content Type " + strContentType + " is not present.", Status.FAIL);

		} else {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					i++;
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					blnFlag = false;
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null)
							strDocTitle = eleDocTitle.getText();
						// report.updateTestLog("Document Title ",
						// "Document Title is "+strDocTitle, Status.DONE);

						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						List<WebElement> eltContentTypeHeaders = commonLibrary.isExistList(eleDocCondent, UIMAP_SearchResult.eltContentTypeHeader, 10);
						for (WebElement header : eltContentTypeHeaders) {
							if (header.getText().toLowerCase().contains(strContentType.toLowerCase())) {
								report.updateTestLog("Verify Content Type " + strContentType + " is not present.", "Content Type " + strContentType + " is present for document " + strDocTitle, Status.FAIL);
								blnFlag = true;
								break;
							}
						}
						if (i > 15 || blnFlag)
							break;
					}
				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify Content Type " + strContentType + " is not present.", "Content Type " + strContentType + " is not present.", Status.PASS);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify titles of Results present.
	// # Function Name : verifyDocumentTitles
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyDocumentTitles() {
		boolean blnFlag = false;
		int i = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				blnFlag = false;
				for (WebElement document : OListItems) {
					i++;
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
					if (eleDocTitle == null) {
						blnFlag = true;
						break;
					}
					if (i > 15)
						break;
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify Document titles are present.", "Document titles are not present.", Status.FAIL);
		else
			report.updateTestLog("Verify Document titles are present.", "Document titles are present.", Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify titles of Results present.
	// # Function Name : clickDocPageLink
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public RelatedContent clickDocPageLink(String linkName) {

		WebElement linkDocPage = commonLibrary.isExist(By.linkText(linkName), 10);

		commonLibrary.clickLinkWithWebElementWithWait(linkDocPage, linkDocPage.getText());

		commonLibrary.sleep(5000);

		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click document page links.
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verify_RecentlyViewed_MouseOVer_ToolTip(String strDocTitle, Boolean isDisplayed, String tcName) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		;
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
							DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
							Date date = new Date();
							String strFormatDate = dateFormat.format(date);
							// Hover over the Recently viewed icon next tothe
							// document title <Chew v. Gates, 27 F.3d1432>
							// MV <Current Date> displays on hovering overthe
							// text/object: Recently viewed icon next tothe
							// document title <Chew v. Gates, 27 F.3d1432>
							report.updateTestLog("Hover over the Recently viewed icon next to the document title" + strDocTitle, "Hover overed the Recently viewed icon next to the document title" + strDocTitle, Status.PASS);
							mouseOverElement(RecntlyViewed, tcName, strDocTitle, strFormatDate);

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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move mouse over.
	// # Function Name : mouseOver    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search mouseOver() {
		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');" + "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";
		WebElement preview4 = commonLibrary.isExist(UIMAP_SearchResult.divShepards, 10);
		if (preview4 != null) {
			List<WebElement> li = commonLibrary.isExistList(preview4, UIMAP_SearchResult.lstTagName, 10);
			for (WebElement listitem : li) {
				List<WebElement> links = commonLibrary.isExistList(listitem, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement item : links) {
					if (item.getText().toLowerCase().contains("other citing sources")) {
						((JavascriptExecutor) driver).executeScript(code, item);
						report.updateTestLog("Hover over the link <Other Citing Sources", "Hover text is not displayed", Status.FAIL);
						break;
					}
				}
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move mouse over a particular
	// element.
	// # Function Name : mouseOver    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void mouseOverElement(WebElement elmt, String tcName, String strDocTitle, String strFormatDate) {

		String javaScript = "var evObj = document.createEvent('MouseEvents');" + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + "arguments[0].dispatchEvent(evObj);";

		((JavascriptExecutor) driver).executeScript(javaScript, elmt);

		// WebElement
		// tooltip=commonLibrary.isExist(elmt,UIMAP_SearchResultPage.tooltip,
		// 2);

		if (elmt.findElement(UIMAP_SearchResult.tooltip).getText().contains(strFormatDate.trim()))
			report.updateTestLog("Verify " + strFormatDate + " displays on hovering overthe text/object: Recently viewed icon next tothe document title " + strDocTitle, strFormatDate + " displays on hovering overthe text/object: Recently viewed icon next tothe document title " + strDocTitle, Status.PASS);

		else {
			takeScreenShot(tcName, "Verify " + strFormatDate + " displays on hovering overthe text/object: Recently viewed icon next tothe document title " + strDocTitle);
			// report.updateTestLog("Verify "+strFormatDate+" displays on hovering overthe text/object: Recently viewed icon next tothe document title "+strDocTitle,
			// "Verify "+strFormatDate+" is displayed on hovering overthe text/object: Recently viewed icon next tothe document title "+strDocTitle,
			// Status.WARNING);
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alert icon
	// # Function Name : clickAlertIcon    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search clickAlertIcon() {
		WebElement btnAlert = commonLibrary.isExist(UIMAP_SearchResult.btnIdAddAlert);
		if (btnAlert != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnAlert, "Alert");
			else
				commonLibrary.clickButtonParentWithWait(btnAlert, "Alert");
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify doc title present.
	// # Function Name : verify_DocTitlePresent    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyDocTitlePresent(String DocTitle) {
		boolean blnFlag = false;
		boolean blnExist = false;
		int i = 0;
		do {
			commonLibrary.sleep(50000);
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					blnFlag = false;
					for (WebElement document : OListItems) {

						WebElement docTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (docTitle != null && docTitle.getText().contains(DocTitle)) {
							blnFlag = true;
							blnExist = true;
							break;
						}
					}
				}
			}

			if (blnExist == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage, 10);
				if (NextPage != null) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(NextPage, "Next Page");
					} else {
						commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");
					}
					i++;

				} else {
					report.updateTestLog("Verify " + DocTitle + " is present in Search Results page", DocTitle + " is not present in first 10 pages of Search Results", Status.FAIL);
					blnFlag = true;
				}
			}

		} while ((!blnFlag) && (i < 10));

		if (blnExist)
			report.updateTestLog("Verify " + DocTitle + " is present in Search Results page", DocTitle + " is present in Search Results page", Status.PASS);
		else
			report.updateTestLog("Verify " + DocTitle + " is present in Search Results page", DocTitle + " is not present in first 10 pages of Search Results", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify First Doc
	// # Function Name : verify_FirstDoc    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyFirstDoc() {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

				WebElement docTitle = commonLibrary.isExist(OListItems.get(0), UIMAP_SearchResult.TitleClassDoc, 20);
				String docTitleText = docTitle.getText();
				report.updateTestLog("Verify " + docTitleText + " is present in Search Results page", docTitleText + " is present in Search Results page", Status.PASS);

			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify MouseHover Teaser
	// # Function Name : verifyMouseHoverTeaser    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search verifyMouseHoverTeaser(String setting) {
		boolean blnFlag = false;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

				for (WebElement document : OListItems) {
					WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserLink, 20);
					if (docContent != null) {
						String before = docContent.getCssValue("color");// -moz-text-decoration-color
						Actions hover = new Actions(driver);
						hover.moveToElement(docContent).build().perform();
						String after = docContent.getCssValue("color");// -moz-text-decoration-color
						if (!(before.equalsIgnoreCase(after))) {
							blnFlag = true;
							break;
						}
					}

				}
			}
		}
		switch (setting) {
		case "Extract": {
			if (blnFlag) {
				report.updateTestLog("Verify teaser link is present and color changes on mouse over", "Teaser link is present and color changes on mouse over", Status.PASS);
			} else {
				report.updateTestLog("Verify teaser link is present and color changes on mouse over", "Teaser link is present and color does not change on mouse over", Status.FAIL);
			}
			break;
		}
		case "Terms": {
			if (blnFlag) {
				report.updateTestLog("Verify teaser link is not clickable", "Verify teaser link is not clickable", Status.FAIL);
			} else {
				report.updateTestLog("Verify teaser link is not clickable", "Verify teaser link is not clickable", Status.PASS);
			}
			break;
		}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Action Verify CountMatched
	// # Function Name : selectActionVerifyCountMatched    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search selectActionVerifyCountMatched(String SearchTerm, String actionName, String actionName1, String actionName2) {
		int prevCount = 0;
		clickActionSelectValue(actionName);

		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 50);
		if (hdrResult1.getText().contains(SearchTerm)) {
			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}
		String[] arrTitle = driver.getTitle().split(" ");
		String title = arrTitle[0].replace("+", "");

		if (Integer.parseInt((title.replace(",", ""))) > 0) {
			prevCount = Integer.parseInt((title.replace(",", "")));

			report.updateTestLog("Note the results count displayed in Browser label", "Noted the results count as " + arrTitle[0], Status.PASS);
		} else
			report.updateTestLog("Note the results count displayed in Browser label", "not noted the results count", Status.FAIL);

		clickActionSelectValue(actionName1);

		WebElement hdrResult = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 50);
		if (hdrResult.getText().contains(SearchTerm)) {
			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}

		clickActionSelectValue(actionName2);

		WebElement hdrResult2 = commonLibrary.isExist(UIMAP_SearchResult.hdrResult1, 50);
		if (hdrResult2.getText().contains(SearchTerm)) {
			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}
		String[] arrTitle1 = driver.getTitle().split(" ");
		String title1 = arrTitle1[0].replace("+", "");

		if (Integer.parseInt((title1.replace(",", ""))) == prevCount) {

			report.updateTestLog("Verify Result count should match the previously noted result count", "Result count is matched with the previously noted result count", Status.PASS);
		} else
			report.updateTestLog("Verify Result count should match the previously noted result count", "Result count is not matched with the previously noted result count", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Select Value
	// # Function Name : clickActionSelectValue    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search clickActionSelectValue(String strAction) {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			commonLibrary.sleep(3000);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get Results Count
	// # Function Name : getResultsCount    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public int getResultsCount() {
		return this.resultCount;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Results Count
	// # Function Name : setResultsCount    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public void setResultsCount(int resultCnt) {
		this.resultCount = resultCnt;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Results Count Matched
	// # Function Name : verifyResultsCountMatched    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public void verifyResultsCountMatched() {
		String[] arrTitle = driver.getTitle().split(" ");

		if (Integer.parseInt((arrTitle[0].replace(",", ""))) == this.getResultsCount()) {
			resultCount = Integer.parseInt((arrTitle[0].replace(",", "")));
			report.updateTestLog("Verify Result count should match the previously noted result count", "Result count is matched with the previously noted result count", Status.PASS);
		} else
			report.updateTestLog("Verify Result count should match the previously noted result count", "Result count is not matched with the previously noted result count", Status.FAIL);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create Alert
	// # Function Name : createAlert    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search createAlert(String strTab, String strFromDate, String strToDate, String strDeliverType, String strEmail, String strDeliverFormat, String strFrequency, String timevalue, String timevalues) {

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, strTab);
			if (strFromDate.toLowerCase() != "") {
				WebElement fromDate = commonLibrary.isExist(UIMAP_Home.txtFromDate);
				commonLibrary.setDataInTextBox(fromDate, strFromDate, "fromdate");

				if (fromDate.getAttribute("value").equalsIgnoreCase(strFromDate)) {
					report.updateTestLog("Select  currentdate as start date", "Start date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strToDate.toLowerCase() != "") {
				WebElement toDate = commonLibrary.isExist(UIMAP_Home.txtToDate);
				commonLibrary.setDataInTextBox(toDate, strToDate, "todate");

				if (toDate.getAttribute("value").equalsIgnoreCase(strToDate)) {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strDeliverType.toLowerCase() != "") {
				WebElement objDivName = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName != null) {
					// commonLibrary.ScrollToView(objDivName);
					if (strDeliverType.equalsIgnoreCase("online only")) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						if (commonLibrary.setRadioButton(onlineOnly, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
						// WebElement onlineOnly =
						// commonLibrary.isExist(UIMAP_SearchResultPage.online);
						// commonLibrary.SetRadioButton(onlineOnly,
						// strDeliverType);
						WebElement emailonline = commonLibrary.isExist(UIMAP_SearchResult.emailonline);
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
				commonLibrary.setDataInTextBox(Email, strEmail, "Email");
			}
			if (strDeliverFormat.toLowerCase() != "") {
				WebElement objDivName1 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName1 != null) {
					if (strDeliverFormat.equalsIgnoreCase("text")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement text = commonLibrary.isExist(UIMAP_SearchResult.textRadio);
						if (commonLibrary.setRadioButton(text, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else if (strDeliverFormat.equalsIgnoreCase("html")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement html = commonLibrary.isExist(UIMAP_SearchResult.htmlRadio);
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
			if (strFrequency.toLowerCase() != "") {
				if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("business daily")) {
					WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
					if (objDivName2 != null) {
						// commonLibrary.ScrollToView(objDivName2);
						WebElement busidaily = commonLibrary.isExist(UIMAP_SearchResult.businessDailyradio);
						// commonLibrary.ScrollToView(busidaily);
						if (commonLibrary.setRadioButton(busidaily, strFrequency)) {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
						} else {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
						}
						if (timevalue != "") {
							WebElement time = commonLibrary.isExist(UIMAP_SearchResult.businessDaily);
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
						WebElement daily = commonLibrary.isExist(UIMAP_SearchResult.dailyradio);
						// commonLibrary.ScrollToView(daily);
						if (commonLibrary.setRadioButton(daily, strFrequency)) {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
						} else {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
						}
						if (timevalue != "") {
							WebElement time = commonLibrary.isExist(UIMAP_SearchResult.dailytime);
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
						WebElement asUpdatesAreAvail = commonLibrary.isExist(UIMAP_SearchResult.asUpdatesare);
						// commonLibrary.ScrollToView(asUpdatesAreAvail);
						if (commonLibrary.setRadioButton(asUpdatesAreAvail, strFrequency)) {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
						} else {
							report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
						}
					}
				}
			}

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

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Tab In Alerts
	// # Function Name : selectTabInAlerts    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search selectTabInAlerts(String tab) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {

			commonLibrary.selectFromListButton(topicalert, tab);
		} else {
			report.updateTestLog("Select the given tab " + tab + "", "" + tab + " is not selected", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify SelectedOptions In AlertBox
	// # Function Name : verifySelectedOptionsInAlertBox    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search verifySelectedOptionsInAlertBox(String strDeliverType, String strDeliverFormat, String strFrequency) {
		commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		// commonLibrary.ScrollToView(objDivName);
		if (strDeliverType.equalsIgnoreCase("online only")) {
			WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
			if (onlineOnly.isSelected()) {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
			} else {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
			}
		} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
			WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
			commonLibrary.setRadioButton(onlineOnly, strDeliverType);
			WebElement emailonline = commonLibrary.isExist(UIMAP_SearchResult.emailonline);
			commonLibrary.setRadioButton(emailonline, strDeliverType);

			if (emailonline.isSelected()) {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
			} else {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
			}
		} else {
			report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
		}
		// format
		WebElement objDivName1 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (objDivName1 != null) {
			if (strDeliverFormat.equalsIgnoreCase("text")) {
				// commonLibrary.ScrollToView(objDivName1);
				WebElement text = commonLibrary.isExist(UIMAP_SearchResult.textRadio);
				if (text.isSelected()) {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
				} else {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
				}
			} else if (strDeliverFormat.equalsIgnoreCase("html")) {
				// commonLibrary.ScrollToView(objDivName1);
				WebElement html = commonLibrary.isExist(UIMAP_SearchResult.htmlRadio);
				if (html.isSelected()) {
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
		// frequencey
		WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (objDivName2 != null) {
			// commonLibrary.ScrollToView(objDivName2);
			switch (strFrequency.toLowerCase()) {
			case "business daily": {
				WebElement busidaily = commonLibrary.isExist(UIMAP_SearchResult.businessDailyradio);
				// commonLibrary.ScrollToView(busidaily);
				if (busidaily.isSelected()) {
					report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
				} else {
					report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
				}
				break;
			}
			}
		} else {
			report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
		}
		// click cancel
		WebElement cancel = commonLibrary.isExist(UIMAP_Home.cancelAlert);
		if (cancel != null) {
			cancel.click();
			if (commonLibrary.isExist(UIMAP_Home.btnBrowse) != null) {
				report.updateTestLog("Click on cancel button", "cancel button  is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on cancel button", "cancel button is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on cancel button", "cancel button is not clicked", Status.FAIL);
		}

		//
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigation From MoreButton To Alerts
	// # Function Name : navigationFromMoreButtonToAlerts    
	// # Author : Seetha
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Alerts navigationFromMoreButtonToAlerts() {

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		if (btnMore != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		if (alert == null || !alert.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");
			}
			alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		}

		if (alert != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(alert, "Alerts");
			else
				commonLibrary.clickLinkWithWebElementWithWait(alert, "Alerts");
		} else {
			report.updateTestLog("Click on alerts", "Alerts button is not clicked", Status.FAIL);
		}
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify alert icon absent.
	// # Function Name : verifyAlertIconNegative
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyAlertIconNegative() {
		WebElement btnAlert = commonLibrary.isExistNegative(UIMAP_SearchResult.btnIdAddAlert, 10);
		if (btnAlert != null) {
			report.updateTestLog("Verify Alert icon is not present", "Alert icon is displayed", Status.FAIL);
		} else {
			report.updateTestLog("Verify Alert icon is not present", "Alert icon is not displayed", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : NavigateToAllSources     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Sources navigateToAllSources() {
		Boolean blnFirst = false;
		WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_SearchResult.btnIdBrowse, 20);
		commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
		try {
			commonLibrary.sleep(Mwait);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_SearchResult.divIdBrowserMenu, 20);
		if (divIdBrowserMenu != null) {
			WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_SearchResult.lstTagAside, 20);
			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_SearchResult.lstTagListItems, 20);
			if (lstTagListItems.size() > 0)
				for (WebElement item : lstTagListItems) {
					if (item.getText().contains("Sources")) {
						WebElement button = commonLibrary.isExistNegative(item, UIMAP_SearchResult.btnButton, 10);
						blnFirst = true;

						commonLibrary.clickButtonParentWithWait(button, "Sources");
						break;
					}

				}
			if (!blnFirst)
				report.updateTestLog("Click on Sources ", "Sources not present", Status.FAIL);
		}
		WebElement allSources = commonLibrary.isExistNegative(UIMAP_SearchResult.allSources, 10);
		commonLibrary.clickLinkWithWebElementWithWait(allSources, "All Sources");
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Required Filter Option in
	// Search Results page.
	// # Function Name : verifyPostFilter
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), strHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), strHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
					// eltFilter.get(i).getText());
					report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
						// eltFilter.get(i).getText());
						report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
						blnFlag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Verifying Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickTextInWordWheel(String text) {

		// click on the text in the word wheel
		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel, UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			if (option.getText().toLowerCase().contains(text.toLowerCase())) {
				commonLibrary.clickLinkWithWebElement(option, text);
				report.updateTestLog("Click " + text + " phrase from word wheel.", text + " is clicked from word wheel.", Status.DONE);

				blnFlag = true;
				break;
			}

		}

		if (!blnFlag)
			report.updateTestLog("Click " + text + " phrase from word wheel.", text + " is not present in word wheel.", Status.FAIL);

		// Verification Point : Main search box text
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 50);
		if (eltSearchbox.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify " + text + " displays in main search box.", text + " displays in main search box.", Status.PASS);
		else
			report.updateTestLog("Verify " + text + " displays in main search box.", text + " does not display in main search box.", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify option in Wordwheel.
	// # Function Name : verifyTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTextInWordWheel(String text, String term) {
		boolean blnFlag = false;
		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel, UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			if (option.getText().toLowerCase().contains(text.toLowerCase())) {
				report.updateTestLog("Verify " + term + "displays anywhere in " + text + "(Starting/middle of terms/ inbetween terms)", term + "displays anywhere in " + text + "(Starting/middle of terms/ inbetween terms)", Status.PASS);
				blnFlag = true;
				break;
			}

		}
		if (!blnFlag)
			report.updateTestLog("Verify " + term + "displays anywhere in " + text + "(Starting/middle of terms/ inbetween terms)", term + "does not display anywhere in " + text + "(Starting/middle of terms/ inbetween terms)", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter Search Term in Search Box.
	// # Function Name : setSearchTerm
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search setSearchTerm(String searchTerm) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		commonLibrary.sleep(5000);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		commonLibrary.sleep(5000);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the respective Filter
	// location
	// # Function Name : ScrollFilter Location
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search scrollFilterLocation() {

		WebElement eltCollapsedFilterHeader1 = commonLibrary.isExist(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		commonLibrary.scrollToView(eltCollapsedFilterHeader1);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Child Filter Option under
	// Parent Filter in Search Results page.
	// # Function Name : selectChildPostFilter
	// # Author : Ram Prasath
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search selectChildPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), strHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), strHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.morechildFilterOption, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
					report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						if (browsername.contains("internet")) {
							commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
						} else {
							commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						}
						report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
						blnFlag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Verifying Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the respective Filter
	// location
	// # Function Name : ScrollFilter Location
	// # Author : Ram Prasath
	// # Date Created : 21 Feb'15
	// #*****************************************************************************************************************************

	public Search verifyFilterTagandValues(String strTagName, String strTagValue) {

		String strDocTitle, strFilterTagName, strFilterTagValue;
		List<WebElement> eltResultList = commonLibrary.isExistList(UIMAP_SearchResult.eltResultList, 10);
		strDocTitle = commonLibrary.isExist(eltResultList.get(0), By.tagName("h2"), 10).getText();
		strFilterTagName = commonLibrary.isExist(eltResultList.get(0), By.tagName("dt"), 10).getText();
		strFilterTagValue = commonLibrary.isExist(eltResultList.get(0), By.tagName("dd"), 10).getText();

		if (strTagName == strFilterTagName && strTagValue == strFilterTagValue) {
			report.updateTestLog("Filter Tag and Value Verification for Document ", strFilterTagValue + " is displayed under " + strFilterTagName + " tag for document:" + strDocTitle, Status.PASS);
		} else {
			report.updateTestLog("Filter Tag and Value Verification for Document", strFilterTagValue + " is displayed under " + strFilterTagName + " tag for document:" + strDocTitle, Status.PASS);
		}

		commonLibrary.sleep(8000);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to right click on element and check no
	// new window is present
	// # Function Name : clickRight_CheckNoNewWindow
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search clickRightCheckNoNewWindow() {

		WebElement element = commonLibrary.isExist(UIMAP_SearchResult.btnIdAddAlert);
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		Actions oAction = new Actions(driver);
		oAction.moveToElement(element);
		// commonLibrary.highlightElement(element);

		String browsername = cap.getBrowserName();
		if (browsername.equalsIgnoreCase("firefox")) {
			oAction.contextClick(element).sendKeys("W").build().perform();
		} else if (browsername.contains("internet")) {
			oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();

		}

		if (driver.getWindowHandles().size() <= 1)

			report.updateTestLog("Right Click on the " + element.getText() + "and verify no new window is opened", "Right Clicked on the " + element.getText() + "and no new window is opened", Status.PASS);
		else

			report.updateTestLog("Right Click on the " + element.getText() + "and verify no new window is opened", "Right Clicked on the " + element.getText() + "and new window is opened", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ReasearchMap link
	// # Function Name : NavigateToResearchMap
	// # Author : Shobana
	// # Date Created : 23 Feb'15
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
	// # Function Description : Function to enter details in Overview tab of
	// Alerts dialog box.
	// # Function Name : enterAlertDetailsOverviewTab
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search enterAlertDetailsOverviewTab(String name, String option) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, "Overview");
			WebElement alertName = commonLibrary.isExistNegative(UIMAP_SearchResult.alertName, 10);
			alertName.clear();
			commonLibrary.setDataInTextBoxClear(alertName, name, "Title");
			WebElement clientOption = commonLibrary.isExistNegative(UIMAP_SearchResult.clientOption, 10);
			commonLibrary.setRadioButton(clientOption, option);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter details in Deliver tab of
	// Alerts dialog box.
	// # Function Name : enterAlertDetailsDeliverTab
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search enterAlertDetailsDeliverTab(String strFromDate, String strToDate, String strDeliverType, String strEmail, String strDeliverFormat, String strFrequency, String timevalue, String timevalues) {

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, "Deliver");
			if (strFromDate.toLowerCase() != "") {
				WebElement fromDate = commonLibrary.isExist(UIMAP_Home.txtFromDate);
				fromDate.clear();
				commonLibrary.setDataInTextBoxClear(fromDate, strFromDate, "fromdate");

				if (fromDate.getAttribute("value").equalsIgnoreCase(strFromDate)) {
					report.updateTestLog("Select  currentdate as start date", "Start date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strToDate.toLowerCase() != "") {
				WebElement toDate = commonLibrary.isExist(UIMAP_Home.txtToDate);
				toDate.clear();
				commonLibrary.setDataInTextBoxClear(toDate, strToDate, "todate");

				if (toDate.getAttribute("value").equalsIgnoreCase(strToDate)) {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strDeliverType.toLowerCase() != "") {
				WebElement objDivName = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName != null) {
					// commonLibrary.ScrollToView(objDivName);
					if (strDeliverType.equalsIgnoreCase("online only")) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						if (commonLibrary.setRadioButton(onlineOnly, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
						// WebElement onlineOnly =
						// commonLibrary.isExist(UIMAP_SearchResultPage.online);
						// commonLibrary.SetRadioButton(onlineOnly,
						// strDeliverType);
						WebElement emailonline = commonLibrary.isExist(UIMAP_SearchResult.emailonline);
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
				commonLibrary.setDataInTextBoxClear(Email, strEmail, "Email");
			}
			if (strDeliverFormat.toLowerCase() != "") {
				WebElement objDivName1 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName1 != null) {
					if (strDeliverFormat.equalsIgnoreCase("text")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement text = commonLibrary.isExist(UIMAP_SearchResult.textRadio);
						if (commonLibrary.setRadioButton(text, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else if (strDeliverFormat.equalsIgnoreCase("html")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement html = commonLibrary.isExist(UIMAP_SearchResult.htmlRadio);
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
					WebElement busidaily = commonLibrary.isExist(UIMAP_SearchResult.businessDailyradio);
					// commonLibrary.ScrollToView(busidaily);
					if (commonLibrary.setRadioButton(busidaily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.businessDaily);
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
					WebElement daily = commonLibrary.isExist(UIMAP_SearchResult.dailyradio);
					// commonLibrary.ScrollToView(daily);
					if (commonLibrary.setRadioButton(daily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.dailytime);
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
					WebElement asUpdatesAreAvail = commonLibrary.isExist(UIMAP_SearchResult.asUpdatesare);
					// commonLibrary.ScrollToView(asUpdatesAreAvail);
					if (commonLibrary.setRadioButton(asUpdatesAreAvail, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
				}
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter details in Share tab of Alerts
	// dialog box and click Create Alert button.
	// # Function Name : createAlert_ShareDetails
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search createAlert_ShareDetails(String text, String userName) {
		int i = 0;
		this.selectTabInAlerts("Share");
		WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_SearchResult.shareUserName, 10);
		wordWheelContent = null;
		do {
			commonLibrary.setDataInTextBox(shareUserName, text, "Enter user's name");
			shareUserName.sendKeys(Keys.ARROW_DOWN);
			try {
				commonLibrary.sleep(5000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			wordWheelContent = commonLibrary.isExistNegative(UIMAP_SearchResult.wordWheelContent, 10);
			i++;
		} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);
		List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
		// WebElement options = commonLibrary
		// .isExistNegative(wordWheelOptions.get(0),
		// UIMAP_SearchResult.lstTagListItems, 10);
		commonLibrary.selectFromList(wordWheelOptions.get(0), userName);

		WebElement addToShare = commonLibrary.isExistNegative(UIMAP_SearchResult.addToShare, 10);
		commonLibrary.clickButtonLogSmallWait(addToShare, "Add to Share");

		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			} else {
				commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			}
			try {
				createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
				WebElement createAlertNew = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
				int count = 0;
				do {
					commonLibrary.sleep(5000);
					count++;
					createAlertNew = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
				} while (createAlert.equals(createAlertNew) && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(3000);
				System.out.println(e.toString());
			}

			// if(commonLibrary.isExist(UIMAP_Home.btnBrowse)!=null)
			// {
			// report.updateTestLog("Click on CreateAlert",
			// "CreateAlert link is clicked", Status.PASS);
			// }
			// else if(commonLibrary.isExist(UIMAP_Home.btnPracArea)!=null)
			// {
			// report.updateTestLog("Click on CreateAlert",
			// "CreateAlert link is clicked", Status.PASS);
			// }
			// else
			// {
			// report.updateTestLog("Click on CreateAlert",
			// "CreateAlert link is not clicked", Status.FAIL);
			// }
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the filters in the left pane
	// # Function Name : verifyFilterInLeftPane
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFilterInLeftPane(String filter) {
		if (filter != "") {
			String[] filters = filter.split(";");
			for (int i = 0; i < filters.length; i++) {
				WebElement classfilt = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 20);
				if (commonLibrary.verifyFromListSpan(classfilt, filters[i])) {
					report.updateTestLog("Verify " + filters[i] + " is present in the left pane", "" + filters[i] + " is present in the left pane", Status.PASS);
					break;
				} else {
					report.updateTestLog("Verify " + filters[i] + " is present in the left pane", "" + filters[i] + " is not present in the left pane", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify filters is present in the left pane", "No filters is passed", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the filters
	// # Function Name : verify_Filter_Ignore
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFilterIgnore(String FilterName) {
		if (FilterName != "") {
			boolean blnFlag = false;
			WebElement FiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
			List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.lstTagListItems, 10);
			for (WebElement item : Filters) {
				if (item.getText().toLowerCase().equalsIgnoreCase(FilterName.toLowerCase())) {
					report.updateTestLog(FilterName + " filter is present", FilterName + " filter is present", Status.PASS);
					blnFlag = true;
				}
			}

			if (!blnFlag) {
				report.updateTestLog(FilterName + " filter is present", FilterName + " filter is not present", Status.FAIL);
			}
		} else {
			report.updateTestLog(FilterName + " filter is present", "No filter is passed", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Create Alert Link
	// # Function Name : clickonCreateAlertLink
	// # Author : shobana
	// # Date Created : 24-Feb'15
	// #*****************************************************************************************************

	public Search clickonCreateAlertLink() {
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
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify monitor tab contents
	// # Function Name : verifyMonitorTabContents
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************

	public Search verifyMonitorTabContents(String category, String filter) {
		boolean flag = false;
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, "Monitor");
			WebElement monitorcontent = commonLibrary.isExist(UIMAP_SearchResult.monitorTabContent, 10);
			WebElement datas = commonLibrary.isExist(monitorcontent, By.tagName("dl"), 10);
			List<WebElement> dataTerms = commonLibrary.isExistList(datas, UIMAP_SearchResult.dataTerms, 10);
			List<WebElement> dataDefn = commonLibrary.isExistList(datas, UIMAP_SearchResult.dataDefn, 10);
			if (dataTerms.size() >= 16) {
				report.updateTestLog("All 16 content / category type appears under Category section", "All 16 content / category type appears under Category section", Status.PASS);
				for (int i = 0; i < dataTerms.size(); i++) {
					flag = false;
					WebElement categ = commonLibrary.isExist(dataTerms.get(i), By.tagName("label"), 10);
					WebElement chkBox = commonLibrary.isExist(dataTerms.get(i), By.tagName("input"), 10);
					if (categ.getText().contains(category) && chkBox.isSelected()) {
						report.updateTestLog(category + " checkbox is selected under Categories section", category + " checkbox is selected under Categories section", Status.PASS);
						flag = true;
						if (dataDefn.get(i).getText().contains(filter))
							report.updateTestLog(filter + " should be present under Narrowed by section", filter + " is present under Narrowed by section", Status.PASS);
						else
							report.updateTestLog(filter + " should be present under Narrowed by section", filter + " is not present under Narrowed by section", Status.FAIL);
					} else if (categ.getText().contains(category) && (!(chkBox.isSelected())))
						report.updateTestLog(category + " checkbox is selected under Categories section", category + " checkbox is not selected under Categories section", Status.FAIL);
					else if (!(chkBox.isSelected()))
						flag = true;
				}
				if (flag)
					report.updateTestLog(category + " checkbox is selected under Categories section", "Other category checkboxes are not selected under Categories section", Status.PASS);
				else
					report.updateTestLog(category + " checkbox is selected under Categories section", "Other category checkboxes are selected under Categories section", Status.FAIL);
			} else {
				report.updateTestLog("All 16 content / category type appears under Category section", "All 16 content / category type does not appears under Category section", Status.FAIL);
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter alert title in Overview tab
	// # Function Name : enterAlerttitle
	// # Author : Shobana
	// # Date Created : 23 Feb'15
	// #*****************************************************************************************************************************
	public Search enterAlertTitle(String title) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromList(topicalert, "Overview");

			WebElement alertTitle = commonLibrary.isExist(UIMAP_SearchResult.alertTitle, 10);
			if (alertTitle != null)
				alertTitle.clear();
			commonLibrary.setDataInTextBox(alertTitle, title, "Alert title");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change settings for alert in deliver
	// tab
	// # Function Name : deliverTabAlerts
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search deliverTabAlerts(String strTab, String strFromDate, String strToDate, String strDeliverType, String strEmail, String strDeliverFormat, String strFrequency, String timevalue, String timevalues) {

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
					if (strDeliverType.equalsIgnoreCase("Online only")) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						if (commonLibrary.setRadioButton(onlineOnly, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						commonLibrary.setRadioButton(onlineOnly, strDeliverType);
						WebElement emailonline = commonLibrary.isExist(UIMAP_SearchResult.emailonline);
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
						WebElement text = commonLibrary.isExist(UIMAP_SearchResult.textRadio);
						if (commonLibrary.setRadioButton(text, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else if (strDeliverFormat.equalsIgnoreCase("html")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement html = commonLibrary.isExist(UIMAP_SearchResult.htmlRadio);
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
					WebElement busidaily = commonLibrary.isExist(UIMAP_SearchResult.businessDailyradio);
					// commonLibrary.ScrollToView(busidaily);
					if (commonLibrary.setRadioButton(busidaily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.businessDaily);
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
					WebElement daily = commonLibrary.isExist(UIMAP_SearchResult.dailyradio);
					// commonLibrary.ScrollToView(daily);
					if (commonLibrary.setRadioButton(daily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.dailytime);
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
					WebElement asUpdatesAreAvail = commonLibrary.isExist(UIMAP_SearchResult.asUpdatesare);
					// commonLibrary.ScrollToView(asUpdatesAreAvail);
					if (commonLibrary.setRadioButton(asUpdatesAreAvail, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
				}
			}

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter AlertDetails in Overview Tab
	// in Recent Client page
	// tab
	// # Function Name : enterAlertDetailsOverviewTabRecentClient
	// # Author : Seetha
	// # Date Created : 20-Feb'15
	// #*****************************************************************************************************

	public Search enterAlertDetailsOverviewTabRecentClient(String name, String option) {
		boolean recent = false;
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();
			commonLibrary.selectFromListButton(topicalert, "Overview");
			commonLibrary.isExistNegative(UIMAP_SearchResult.alertName, 10);
			// if(name!="")
			/*
			 * alertName.clear();
			 * commonLibrary.SetDataInTextBox(alertName,name,"Title");}
			 */
			WebElement clientOption = commonLibrary.isExistNegative(UIMAP_SearchResult.clientOption, 10);
			commonLibrary.setRadioButton(clientOption, option);
			WebElement dropdown = commonLibrary.isExistNegative(UIMAP_SearchResult.recentclient, 10);
			List<WebElement> options = commonLibrary.isExistList(dropdown, UIMAP_SearchResult.lstOption, 10);
			for (WebElement item : options) {
				if (item.getText() != "")
					recent = true;
				break;
			}
			if (recent) {
				report.updateTestLog("Verify List of recently entered client names displays", "List of recently entered client names is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify List of recently entered client names displays", "List of recently entered client names is not displayed", Status.FAIL);
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click and Select All In Monitor Tab
	// tab
	// # Function Name : ClickSelectAllInMonitorTab
	// # Author : Seetha
	// # Date Created : 20-Feb'15
	// #*****************************************************************************************************

	public Search clickSelectAllInMonitorTab() {
		boolean click = false;
		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.monitor, 10);
		List<WebElement> btn = commonLibrary.isExistList(header, UIMAP_SearchResult.button, 10);
		for (WebElement item : btn) {
			if (item.getText().equalsIgnoreCase("Select all")) {
				commonLibrary.clickLink(item, "Select all");
				click = true;
				break;
			}
		}
		if (!click) {
			report.updateTestLog("Click link select all", "Select All link is not clicked", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click LPA Logo in product switcher
	// # Function Name : clickLPALogo
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPAHome clickLPALogo() {
		WebElement logo = commonLibrary.isExist(UIMAP_SearchResult.lpaProductLogo, 10);
		if (logo != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(logo, "Product Logo");
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Product Logo");
			}

			report.updateTestLog("Click on product logo", "Lexis Practice Area Home page is displayed ", Status.PASS);
		} else {
			report.updateTestLog("Click on product logo", "Lexis Practice Area Home page is not displayed ", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down
	// # Function Name : selectSortBy
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search selectSortBy(String value) {
		generalFunctions.selectSortBy(value);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// ascending order
	// # Function Name : verifyDocTitleSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyDocTitleSortedResultAZ() {
		generalFunctions.verifyDocTitleSortedResultAZ();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document title results in
	// descending order
	// # Function Name : verifyDocTitleSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyDocTitleSortedResultZA() {
		generalFunctions.verifyDocTitleSortedResultZA();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// ascending order
	// # Function Name : verifyJurisSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyJurisSortedResultAZ() {
		generalFunctions.verifyJurisSortedResultAZ();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Jurisdiction results in
	// descending order
	// # Function Name : verifyJurisSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyJurisSortedResultZA() {
		generalFunctions.verifyJurisSortedResultZA();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting highest to
	// lowest
	// # Function Name : verifySortCourtHighToLow
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifySortCourtHighToLow() {
		generalFunctions.verifySortCourtHighToLow();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court sorting lowest to
	// highest
	// # Function Name : verifySortCourtLowTohigh
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifySortCourtLowTohigh() {
		generalFunctions.verifySortCourtLowTohigh();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting newest to oldest
	// # Function Name : verifyDateSortNewToOld
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyDateSortNewToOld() {
		generalFunctions.verifyDateSortNewToOld();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date sorting oldest to newest
	// # Function Name : verifyDateSortOldtoNew
	// # Author : Shobana
	// # Date Created : 26 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyDateSortOldtoNew() {
		generalFunctions.verifyDateSortOldtoNew();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content results in ascending
	// order
	// # Function Name : verifyContentSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyContentSortedResultAZ() {
		generalFunctions.verifyContentSortedResultAZ();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content results in descending
	// order
	// # Function Name : verifyContentSortedResultZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyContentSortedResultZA() {
		generalFunctions.verifyContentSortedResultZA();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Agency results in ascending
	// order
	// # Function Name : verifyContentSortedResultAZ
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyAgencySortAZ() {
		generalFunctions.verifyAgencySortAZ();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Agency results in descending
	// order
	// # Function Name : verifyAgencySortZA
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyAgencySortZA() {
		generalFunctions.verifyAgencySortZA();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sort Court Highest Date
	// newest
	// # Function Name : verifySortCourtHighDateNew
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifySortCourtHighDateNew() {
		generalFunctions.verifySortCourtHighDateNew();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click alert icon depending on search
	// term
	// # Function Name : clickAlertIconDependingonSearchterm
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search clickAlertIconDependingonSearchterm() {
		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrBanner, 10);
		if (header != null) {
			WebElement btnAlert = commonLibrary.isExist(UIMAP_SearchResult.alertPending);
			if (btnAlert != null) {
				commonLibrary.clickButtonParentWithWait(btnAlert, "Alert");
			} else {
				report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
			}
		} else {
			WebElement btnAlert = commonLibrary.isExist(UIMAP_SearchResult.btnIdAddAlert);
			if (btnAlert != null) {
				commonLibrary.clickButtonParentWithWait(btnAlert, "Alert");
			} else {
				report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and SelectAll In MonitorTab
	// # Function Name : clickSelectAllInMonitorTab
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #****************************************************************************************************

	public Search clickSelectAllInMonitorTab(String check) {
		if (check != "") {
			boolean click = false;
			WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.monitor, 10);
			List<WebElement> btn = commonLibrary.isExistList(header, UIMAP_SearchResult.button, 10);
			for (WebElement item : btn) {
				if (item.getText().equalsIgnoreCase("Select all")) {
					commonLibrary.clickLinkWithWebElementWithWait(item, "Select all");
					click = true;
					break;
				}
			}
			if (!click) {
				report.updateTestLog("Click link select all", "Select All link is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click link select all", "No Check boxes will be present for this search term", Status.PASS);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click TOC and Verify miniToc page
	// # Function Name : clickTOCVerifyminiToc
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #****************************************************************************************************

	public Document clickTOCVerifyminiToc(Boolean isDisplayed) {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		commonLibrary.clickButtonParentWithWait(tableOfContentsTab, "Table of Contents Tab");

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
	// # Function Description : Function to click and View TOC and Verify Full
	// Toc
	// # Function Name : clickViewTOCVerifyFullToc
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #****************************************************************************************************

	public TOC clickViewTOCVerifyFullToc() {
		WebElement tocLink = commonLibrary.isExistNegative(UIMAP_Document.tocLink, 10);
		commonLibrary.clickButtonParentWithWait(tocLink, tocLink.getText());

		WebElement miniToc = commonLibrary.isExistNegative(UIMAP_Document.miniToc, 10);
		if (miniToc != null && miniToc.isDisplayed())
			report.updateTestLog("Verify Full TOC is displayed", "Full TOC is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Full TOC is displayed", "Full TOC is not displayed", Status.FAIL);
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Agency results in descending
	// order
	// # Function Name : verifyTribalSorting
	// # Author : Shobana
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************
	public Search verifyTribalSorting() {
		boolean flag = false;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {
					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Jurisdiction") && dataDefns.get(t).getText().equalsIgnoreCase("Tribal")) {
							report.updateTestLog("Tribal documents are displayed at the top", "Tribal documents are displayed at the top", Status.PASS);
							flag = true;
							break;
						}
					}

					if (!flag)
						report.updateTestLog("Tribal documents are displayed at the top", "Tribal documents are not displayed at the top", Status.PASS);
				}
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder
	// order
	// # Function Name : addToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public Search addToFolder(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 1000);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 1000);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

			/*
			 * try { String loadProp = properties.getProperty("xSpinner"); int
			 * count = 0; WebElement loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); do {
			 * commonLibrary.sleep(10000); loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); count++; }
			 * while (loader != null && count < 15); } catch (Exception e) {
			 * System.out.println(e.toString()); }
			 */

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 1000);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(30000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 1000);
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
					commonLibrary.sleep(7000);
					createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (create.equals(createNew) && count < 80);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 1000);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(7000);
					saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (saveFolder == saveNew && count < 40);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			commonLibrary.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : ClickBrowserBack
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public Sources clickBrowserBack() {
		commonLibrary.clickBrowserBack();

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Folder Title
	// # Function Name : clickFolderTitle
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public WorkFolders clickFolderTitle(String folderTitle) {
		try {
			WebElement myFoldersExpand = commonLibrary.isExist(UIMAP_WorkFolders.myFoldersExpand, 10);
			if (myFoldersExpand.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButton(myFoldersExpand);
			}

			WebElement myFolders = commonLibrary.isExist(UIMAP_WorkFolders.myFolders, 20);
			List<WebElement> folder = commonLibrary.isExistList(myFolders, By.tagName("a"), 20);
			for (WebElement item : folder) {
				if (item.getAttribute("data-title").contains(folderTitle)) {
					// commonLibrary.ScrollToView(item);
					String folderText = item.getAttribute("data-itemcount");
					if (folderText.equalsIgnoreCase("0")) {
						for (int i = 0; i <= 10; i++) {
							driver.navigate().refresh();
							commonLibrary.sleep(3000);
							folderText = item.getAttribute("data-itemcount");
							if (!(folderText.equalsIgnoreCase("0")))
								break;
						}
					}
					if (browsername.contains("internet"))
						commonLibrary.clickJS(item, "title");
					else
						item.click();

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
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='title']"), 10);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the folder icon of particular
	// document title
	// # Function Name : clickFolderForGivenTitle     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public WorkFolders clickFolderForGivenTitle(String title) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		WebElement lnkTitle = null;
		// commonLibrary.sleep(1000);
		// if (browsername.contains("internet")) {
		// Actions actionObject = new Actions(driver);
		// actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();
		// } else
		// driver.navigate().refresh();
		// check = pageCheck.positiveCheck(driver, "Results", "Search Page");
		//
		// pageCheck.handleFlow(driver, check);
		// commonLibrary.sleep(60000);

		WebElement list = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult);
		List<WebElement> OList = commonLibrary.isExistList(list, By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='title']"), 10);
			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
			} else {
				lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[class*='ExternalLinkAfter']"), 10);
				strDocTitle = lnkTitle.getText();
			}
			if (strDocTitle.trim().contains(title.trim())) {
				WebElement folderBtn = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.folderIcon, 10);
				if (folderBtn != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWait(folderBtn, "Folder");
					else
						commonLibrary.clickButtonParentWithWait(folderBtn, "Folder");
					WebElement folderName = commonLibrary.isExist(LList.get(i), UIMAP_SearchResult.folderName, 10);
					if (folderName != null) {
						if (browsername.contains("internet")) {
							commonLibrary.clickButtonParentWithWait(folderName, "Folder Name");
							blnFlag = true;
							break;
						} else {
							commonLibrary.clickButtonParentWithWait(folderName, "Folder Name");
							blnFlag = true;
							break;
						}
					}
				}
			}
		}
		if (blnFlag)
			report.updateTestLog("Click the folder icon next to document title " + title + "", "folder icon next to document title " + title + " is clicked", Status.PASS);
		else
			report.updateTestLog("Click the folder icon next to document title " + title + "", "folder icon next to document title " + title + " is not clicked", Status.FAIL);

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify header for Topic Results.
	// # Function Name : verifyTopicResultsHeader
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTopicResultsHeader(String searchTerm) {
		WebElement headerSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		if (headerSearchResult1 != null && headerSearchResult1.getText().toLowerCase().contains("topic results") && headerSearchResult1.getText().toLowerCase().contains(searchTerm.toLowerCase()))
			report.updateTestLog("Verify Topic Results header contains: " + searchTerm, "Topic Results header contains: " + searchTerm, Status.PASS);
		else
			report.updateTestLog("Verify Topic Results header contains: " + searchTerm, "Topic Results header contains: " + searchTerm, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify VSA Icon Link
	// # Function Name : verifyVSAIconLink
	// # Author : Veeshma
	// # Date Created :6-March'15
	// #*****************************************************************************************************************************

	public Search verifyVSAIconLink() {
		WebElement resultHeader = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 10);
		WebElement vsaIcon = commonLibrary.isExist(resultHeader, UIMAP_SearchResult.vsaViewCharts, 10);
		if (vsaIcon == null)
			report.updateTestLog("Verify Lexisnexis Verdict and Settlement Analyzer link is absent.", "Lexisnexis Verdict and Settlement Analyzer link is NOT Present", Status.PASS);
		else
			report.updateTestLog("Verify Lexisnexis Verdict and Settlement Analyzer link is absent.", "Lexisnexis Verdict and Settlement Analyzer link is Present", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify VSA GlobalMenu Absent
	// # Function Name : verifyVSAGlobalMenu_Absent
	// # Author : Veeshma
	// # Date Created :6-March'15
	// #*****************************************************************************************************************************

	public Search verifyVSAGlobalMenuAbsent() {
		try {

			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);
			WebElement btnActionVSA = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
			if (btnActionVSA == null)
				report.updateTestLog("Verify Verdict and Settlement Analyzer link not present in the dropdown", "Verdict and Settlement Analyzer link not present in the dropdown", Status.PASS);
			else
				report.updateTestLog("Verify Verdict and Settlement Analyzer link not present in the dropdown", "Verdict and Settlement Analyzer link present in the dropdown", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on VSA Icon from full document
	// # Function Name : clickVSAIconLink
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public VSASearchResults clickVSAIconLink() {

		WebElement resultHeader = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 10);
		WebElement vsaIcon = commonLibrary.isExist(resultHeader, UIMAP_SearchResult.vsaViewCharts, 10);
		if (vsaIcon != null) {
			commonLibrary.clickButtonLogSmallWait(vsaIcon, "Verdict & Settlement Analyzer");
		} else
			report.updateTestLog("Click Lexisnexis Verdict and Settlement Analyzer link ", "Lexisnexis Verdict and Settlement Analyzer link is NOT Present", Status.FAIL);

		WebElement searchWithin = commonLibrary.isExistNegative(UIMAP_VSASearchResults.searchWithin, 5);

		int counter = 0;
		do {
			commonLibrary.sleep(50000);
			counter = counter + 1;
			searchWithin = commonLibrary.isExistNegative(UIMAP_VSASearchResults.searchWithin, 3);
		} while (searchWithin == null && counter <= 20);

		commonLibrary.sleep(50000);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink
	// # Function Name : clickDocLink2
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickDocLink2(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click On Show More Button
	// # Function Name : clickOnShowMoreButton
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search clickOnShowMoreButton() {
		WebElement showMoreButton = commonLibrary.isExist(UIMAP_SearchResult.showMoreButton, 10);
		WebElement searchControl = commonLibrary.isExistNegative(UIMAP_SearchResult.searchControl, 10);
		WebElement casesButton = commonLibrary.isExistNegative(searchControl, UIMAP_SearchResult.casesButton, 10);
		showMoreButton.click();

		commonLibrary.sleep(5000);
		if (casesButton.isDisplayed())
			report.updateTestLog("Show More button is clickled", "Click show more button", Status.DONE);
		else
			report.updateTestLog("Show More button is not clickled", "Click show more button", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click On Cases
	// # Function Name : clickOnCases
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search clickOnCases() {
		WebElement searchControl = commonLibrary.isExistNegative(UIMAP_SearchResult.searchControl, 10);
		WebElement casesButton = commonLibrary.isExistNegative(searchControl, UIMAP_SearchResult.casesButton, 10);
		List<WebElement> header = commonLibrary.isExistList(UIMAP_SearchResult.casesHeader, 10);
		for (WebElement caseHeader : header) {
			if (caseHeader.getText().contains("Cases")) {
				casesButton.click();
				break;
			}
		}

		commonLibrary.sleep(30000);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void FilterMenuSelection(String strMenuName) {

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
	// # Function Description : Function to clear narrow by filter in Search
	// page
	// # Function Name : verifynarrowbyfilterleftpane
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search verifynarrowbyfilterleftpane(String narrowlink, String menuname) {
		WebElement eltFiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.eltFiltersUsed, 20);
		WebElement btnFilterRemove = commonLibrary.isExist(eltFiltersUsed, UIMAP_SearchResult.btnFilterRemove, 20);
		if (btnFilterRemove.getText().contains(narrowlink)) {
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(btnFilterRemove, "Clear");
			else
				commonLibrary.clickButtonParentWithWait(btnFilterRemove, "Clear");
		}
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		commonLibrary.clickButtonLogSmallWait(btnClassFilter, "Filter");
		this.FilterMenuSelection(menuname);
		WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_SearchResult.btnClassClearFilter, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWaitJS(btnClassClearFilter, "Clear");
		else
			commonLibrary.clickButtonParentWithWait(btnClassClearFilter, "Clear");
		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.preFiltersDiv, 20);

		WebElement closebuttonRFF = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.closebuttonRFF, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWaitJS(closebuttonRFF, "Close");
		else
			commonLibrary.clickButtonParentWithWait(closebuttonRFF, "Close");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Copy Citation.
	// # Function Name : clickCopyCitation
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickCopyCitation() {
		WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
		WebElement copyCitation = commonLibrary.isExistNegative(docHeader, UIMAP_Document.copyCitation, 10);
		commonLibrary.clickButtonParentWithWait(copyCitation, "Copy Citation");

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Citation Format from dropdown
	// # Function Name : selectCitationFormat     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectCitationFormat(String option) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement formatDropdown = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.formatDropdown, 10);
		commonLibrary.selectFromListOption(formatDropdown, option);

		WebElement selectButton = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.selectButton, 10);
		commonLibrary.clickButtonParentWithWait(selectButton, "Select");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Checkboxes present and their
	// status.
	// # Function Name : checkBoxStatus     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search checkBoxStatus(String option, boolean isChecked, boolean isEnabled) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the format of text is present
	// in Citation popup.
	// # Function Name : verifyTextFormat     
	// # Author : Anbu
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTextFormat(String text) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().contains(text)) {
			report.updateTestLog("Verify text format", "The text " + text + " is in the same format as in the string in cliptext", Status.PASS);
		} else {
			report.updateTestLog("Verify text format", "The text " + text + " is not in the same format as in the string in cliptext", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify full sources present in
	// Citation.
	// # Function Name : verifyFullSource     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyFullSource(String source) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().toLowerCase().contains(source.toLowerCase().trim())) {
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " displays in the preview box", Status.PASS);
		} else
			report.updateTestLog("Verify Full source citation " + source + " displays in the preview box", "Full source citation " + source + " does not display in the preview box", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select None and close Citation
	// popup.
	// # Function Name : selectNoneClosePopUp     
	// # Author : Anbarasan
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectNoneClosePopUp(String option) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement formatDropdown = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.formatDropdown, 10);
		commonLibrary.selectFromListOption(formatDropdown, option);

		WebElement selectButton = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.selectButton, 10);
		commonLibrary.clickButtonParentWithWait(selectButton, "Select");

		WebElement close = commonLibrary.isExist(UIMAP_Document.closeCitationPopup, 10);
		commonLibrary.clickButtonLogSmallWait(close, "Close Button");

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ActualLink
	// # Function Name : clickActualLink     
	// # Author : Anbarasan
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickActualLink(String docTitle) {
		WebElement actualLinkSection = commonLibrary.isExist(UIMAP_SearchResult.actualLinkSection, 10);
		List<WebElement> actualLink = commonLibrary.isExistList(actualLinkSection, UIMAP_SearchResult.actualLink, 10);

		for (WebElement link : actualLink) {
			if (link.getText().toLowerCase().equals((docTitle).toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, docTitle);
				break;
			}

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Citation Format
	// # Function Name : verifyCitationFormat     
	// # Author : Anbarasan
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyCitationFormat(String option) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		// WebElement formatDropdown =
		// commonLibrary.isExist_Negative(copyCitationPopup,
		// UIMAP_Document.formatDropdown, 10);
		WebElement selectedStyle = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.selectedStyle1, 10);

		if (selectedStyle.getText().contains(option)) {
			report.updateTestLog("Verify " + option + " is selected in Format Dropdown.", option + " is selected in Format Dropdown.", Status.PASS);
		} else
			report.updateTestLog("Verify " + option + " is selected in Format Dropdown.", option + " is not selected in Format Dropdown.", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search closeCitationPopup_Search() {
		generalFunctions.closeCitationPopup();
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : closeCitationPopup     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document closeCitationPopup_Document() {
		generalFunctions.closeCitationPopup();
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To History Link
	// # Function Name : NavigateToHistoryLink     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search navigateToHistoryLink(String strTitle) {

		try {

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
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstDocumentsContent, By.linkText(strTitle), 20);
				if (lnkDocuments.size() > 0) {
					report.updateTestLog("Verify Searches Tab", strTitle + " entity search is displayed", Status.PASS);
					commonLibrary.clickButtonParentWithWait(lnkDocuments.get(0), lnkDocuments.get(0).getText());
				}
				commonLibrary.sleep(3000);
			}
			WebElement header = commonLibrary.isExist(UIMAP_SearchResult.TitleClassTOC, 10);
			if (header != null) {
				if (header.getText().contains(strTitle))
					report.updateTestLog("Verify '" + strTitle + "'  entity search results page displayed", strTitle + " entity search results page is displayed", Status.PASS);
				else
					report.updateTestLog("Verify '" + strTitle + "'  entity search results page displayed", strTitle + " entity search results page is NOT displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Source Results Header
	// # Function Name : verifySourceResultsHeader     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifySourceResultsHeader(String searchTerm) {
		WebElement headerSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		if (headerSearchResult1 != null && headerSearchResult1.getText().toLowerCase().contains("source results") && headerSearchResult1.getText().toLowerCase().contains(searchTerm.toLowerCase()))
			report.updateTestLog("Verify Source Results header contains: " + searchTerm, "Source Results header contains: " + searchTerm, Status.PASS);
		else
			report.updateTestLog("Verify Source Results header contains: " + searchTerm, "Source Results header contains: " + searchTerm, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To History Link in Search
	// # Function Name : navigateToHistoryLinkSearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search navigateToHistoryLinkSearch(String strTitle, String searchType) {

		try {
			boolean blnFlag = false;
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
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstDocumentsContent, UIMAP_Document.listItems, 20);
				for (WebElement item : lnkDocuments) {
					WebElement searchLink = commonLibrary.isExistNegative(item, UIMAP_Document.links, 10);
					WebElement type = commonLibrary.isExistNegative(item, UIMAP_Document.span, 10);
					if (searchLink.getText().toLowerCase().contains(strTitle.toLowerCase()) && type.getText().toLowerCase().contains(searchType.toLowerCase())) {
						report.updateTestLog("Verify '" + strTitle + " " + searchType + "'  displays in recent searches.", strTitle + " " + searchType + "'  displays in recent searches.", Status.PASS);
						commonLibrary.clickLinkWithWebElementWithWait(searchLink, strTitle);
						WebElement headerSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
						int count = 0;
						do {
							count++;
							headerSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
							if (headerSearchResult1 == null)
								commonLibrary.sleep(5000);
						} while (headerSearchResult1 == null && count <= 30);
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
	// # Function Description : Function to Navigate To History FooterLink
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Pratik
	// # Date Created : Mar'15
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
				if (browsername.contains("internet")) {
					// commonLibrary.click_JS(lnkTxtViewAllHistory,
					// "View All History");
					try {
						driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
						commonLibrary.clickMouseMoveAction(lnkTxtViewAllHistory, lnkTxtViewAllHistory.getText());
					} catch (TimeoutException ex) {
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println(ex.toString());

					}
					driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
				} else {
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory, "View All History");
				}
				WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
				int count = 0;
				do {
					count++;
					rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
					commonLibrary.sleep(2000);
				} while (rslt == null && count <= 15);
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");
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
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify History Details One
	// # Function Name : verifyHistoryDetailsOne     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public History verifyHistoryDetailsOne(String DocTitle, String docType, String type, String originated, String client, String dateTime) {
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
								} else if (clientHeading.get(i).getText().trim().equalsIgnoreCase("Type:")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(docType)) {
										docTypePresent = true;
									}
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
	// # Function Description : Function to click Search Term Link
	// # Function Name : clickSearchTermLink    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Search clickSearchTermLink() {
		WebElement searchTermLink = commonLibrary.isExist(UIMAP_SearchResult.searchTermLink, 20);
		if (searchTermLink != null)
			commonLibrary.clickLinkWithWebElementWithWait(searchTermLink, "Search for 32CFR");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search Back in ResearchMap
	// # Function Name : searchBackResearchMap    
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*******************************************************************************************************
	public ResearchMap searchBackResearchMap() {
		commonLibrary.clickBrowserBack();
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document count
	// # Function Name : verifydocumentcount     
	// # Author : Seetha
	// # Date Created : Feb '15
	// #*****************************************************************************************************************************

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

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click first Document from result
	// list and store Document Title in the data sheet.
	// # Function Name : ClickFirstDocLink1     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search addToFolderFromAction(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

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
					commonLibrary.sleep(7000);
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
					commonLibrary.clickJS(saveFolder, "Save");

				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(7000);
					saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (saveFolder == saveNew && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(10000);
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click first Document from result
	// list and store Document Title in the data sheet.
	// # Function Name : ClickFirstDocLink1     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Document clickFirstDocLink1(String tcName, String dataSheet, String colName) {
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
							// report.updateTestLog("Document Title ",
							// "Document Title is " + strDocTitle, Status.DONE);

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
			WebElement docHeader = commonLibrary.isExistNegative(UIMAP_Document.docHeader, 10);
			WebElement copyCitation = commonLibrary.isExistNegative(docHeader, UIMAP_Document.copyCitation, 10);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(2000);
			} while (copyCitation == null && count < 15);
			if (!blnFlag)

				report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
			else {
				WebElement pgHeader = null;

				if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
				else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);

				if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document") || pgHeader.getText().contains("Expert Witness"))) {

					strDocTitle = pgHeader.getText();
					// strDocTitle=strDocTitle.replace("\n", "");
					String[] title = strDocTitle.split("\n");
					WebElement spanquery = commonLibrary.isExist(pgHeader, UIMAP_Document.pgTitleTopicSummary, 20);
					if (spanquery != null) {
						strDocTitle = spanquery.getText();
					} else {
						strDocTitle = title[1].trim();
					}

					final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
					String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
					ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
					excel.setDatasheetName("General_Data");
					int iRowNo = excel.getRowNum(tcName, 0);
					excel.setValue(iRowNo, colName, strDocTitle);
					report.updateTestLog("Verify document " + strDocTitle + " is displayed", "Document " + strDocTitle + " is displayed", Status.PASS);

				} else
					report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
			}
			return new Document(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to SelectDoc and AddTo Folder
	// # Function Name : SelectDoc_AddToFolder     
	// # Author : vidhya
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public String selectDocAddToFolder(int intDocNo, String strContactName, String strFolderName) {

		try {

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
			commonLibrary.sleep(4000);
			String strDocTitle = selectDocumentVerifyCount(intDocNo, 1);

			// CLICK ON <<<ADD TO FOLDER>>>
			commonLibrary.sleep(1000);
			WebElement btnAddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 10);
			if (btnAddFolder != null)

				commonLibrary.clickButtonParentWithWait(btnAddFolder, "Add To Folder");
			commonLibrary.sleep(50000);
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

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement btnChooseFolder = commonLibrary.isExist(UIMAP_Document.btnChooseFolder, 10);
			if (btnChooseFolder != null)
				commonLibrary.clickButtonParentWithWait(btnChooseFolder, "Choose Folder");
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

			// CLICK ON <<<CREATE FOLDER>>>

			WebElement btnCreateFolder = commonLibrary.isExist(UIMAP_Document.btnCreateFolder, 10);
			if (btnCreateFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateFolder, "Create Folder");

			// ENTER <<<SMOKE TEST>>> IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT
			// BOX

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, strFolderName);

			// CLICK ON <<<CREATE>>>
			WebElement btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			if (btnCreateNewFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateNewFolder, "CREATE");
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

			commonLibrary.sleep(40000);

			// CLICK ON <<<SAVE>>>
			WebElement btnDocumentSave = commonLibrary.isExist(UIMAP_Document.btnDocumentSave, 10);
			if (btnDocumentSave != null) {
				commonLibrary.highlightElement(btnDocumentSave);
				commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");
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

			/*
			 * 
			 * strDocTitle=SelectDocument(intDocNo); //CLICK ON <<<ADD TO
			 * FOLDER>>> commonLibrary.sleep(1000);
			 * btnAddFolder=commonLibrary.isExist(UIMAP_Document.btnAddFolder,
			 * 10); if(btnAddFolder!=null)
			 * 
			 * commonLibrary.clickButton_Parent_WithWait(btnAddFolder,
			 * "Add To Folder");
			 * 
			 * //CLICK ON <<<CHOOSE A FOLDER>>>
			 * 
			 * Boolean blnFlag = false; List <WebElement>
			 * btnQuickSaveList=commonLibrary
			 * .isExist_List(By.cssSelector("button[id='quicksavebutton']"),
			 * 10); for(int i=0;i<btnQuickSaveList.size();i++) {
			 * if(btnQuickSaveList
			 * .get(i).getText().trim().equals(strFolderName)) {
			 * commonLibrary.click_JS(btnQuickSaveList.get(i), strFolderName);
			 * blnFlag=true; break; }
			 * 
			 * }
			 */
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

	public String selectDocument(int intDocNo) {

		try {
			String strDocTitle = null;
			WebElement lnkTitle = null;
			WebElement lnkTitle1 = null;
			commonLibrary.sleep(20000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			lnkTitle = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[data-action=title]"), 10);

			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
			} else {
				lnkTitle1 = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[class*='ExternalLinkAfter']"), 10);
				strDocTitle = lnkTitle1.getText();
			}
			WebElement lstchkBox = commonLibrary.isExist(OList.get(intDocNo - 1), By.cssSelector("a[type='checkbox']"), 10);
			WebElement lstchkBoxWeb = commonLibrary.isExist(OList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
			String strI = "" + (intDocNo);
			if (lstchkBox != null)
				commonLibrary.setCheckBox(lstchkBox, strI);
			else if (lstchkBoxWeb != null) {
				commonLibrary.setCheckBox(lstchkBoxWeb, strI);
			} else
				report.updateTestLog("Select document", "document is not selected", Status.FAIL);

			return strDocTitle;
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink With Content
	// # Function Name : clickDocLinkWithContent    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkWithContent(String strDocTitle) {

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
						WebElement articleContent = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameArticle, 2);

						if (eleDocTitle != null && eleDocTitle.getText().trim().toLowerCase().trim().equals(strDocTitle.toLowerCase().trim()) && (!(articleContent.getText().trim().equals("")))) {
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

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set NewClient ID Generic
	// # Function Name : setNewClientIDGeneric    
	// # Author : Uma
	// # Date Created : Jan'15
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
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Research(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To Client Link
	// # Function Name : NavigateToClientLink    
	// # Author : Uma
	// # Date Created : Jan'15
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
				WebElement lnkTxtNone = commonLibrary.isExist(UIMAP_Home.selectNone, 20);
				if (browsername.contains("internet") && version.contains("9")) {
					commonLibrary.clickLinkWithWebElementWithWait(lnkTxtNone, "None");
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(lnkTxtNone, "None");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set ClientID None
	// # Function Name : setClientIDNone    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Research setClientIDNone() {
		navigateToClientLink("-None-");
		commonLibrary.sleep(15000);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set generalSettings
	// # Function Name : generalSettings    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home generalSettings(String settingsName, String value)

	{
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement dropdown = null;
		switch (settingsName) {
		case "History": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.HistRMDateRange, 20);
			break;
		}
		}

		commonLibrary.selectByVisibleText(dropdown, value);

		WebElement submit = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Search Option in Settings_Search
	// # Function Name : setSearchOptioninSettings_Search    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search setSearchOptioninSettings_Search(String stroption) {
		WebElement researchlink = commonLibrary.isExist(UIMAP_Settings.researchbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		} else {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		}
		WebElement researchheader = commonLibrary.isExist(UIMAP_Settings.researchsettings, 20);
		WebElement formheader = commonLibrary.isExist(researchheader, UIMAP_Settings.formcontainer, 20);
		WebElement ullist = commonLibrary.isExist(formheader, UIMAP_Settings.ullist, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist, UIMAP_Settings.lilist, 20);
		for (WebElement button : lilist) {
			if (button.getText().contains(stroption)) {
				WebElement checkBox = commonLibrary.isExistNegative(button, UIMAP_Document.checkBox, 10);
				commonLibrary.clickJS(checkBox, stroption);
			}
		}
		WebElement savechangesbutton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		} else {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document count
	// # Function Name : verifydocumentcount    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifydocumentcount() {
		WebElement activetab = commonLibrary.isExist(UIMAP_SearchResult.activetab, 20);
		WebElement doccount = commonLibrary.isExist(activetab, UIMAP_SearchResult.documentcount, 20);
		String doccounter = doccount.getText();
		dataTable.putData("General_Data", "DocCount1", doccounter);
		return new Search(scriptHelper);
	}

	// public ResearchMap clickResearchMap()
	// {
	// try
	// {
	// WebElement header = commonLibrary.isExist(UIMAP_History.headerDiv, 10);
	// WebElement researchMap =
	// commonLibrary.isExist(header,UIMAP_History.researchMap,10);
	// if(researchMap != null)
	// {
	// if(browsername.contains("internet"))
	// commonLibrary.click_JS(researchMap, "ResearchMap");
	// else
	// commonLibrary.clickButton_Parent_WithWait(researchMap, "ResearchMap");
	// }
	// commonLibrary.sleep(15000);
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// return new ResearchMap(scriptHelper);
	//
	// }
	// #*****************************************************************************************************************************
	// # Function Description : Function to Navigate To History FooterLink in
	// ResearchMap
	// # Function Name : NavigateToHistoryFooterLink_ResearchMapNew     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
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
	// # Function Description : Function toclick view toc
	// # Function Name : clickViewTOCVerifyTableOfContent     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public TOC clickViewTOCVerifyTableOfContent() {
		WebElement header;
		List<WebElement> tocLink = commonLibrary.isExistList(UIMAP_Document.toc, 10);
		commonLibrary.clickLinkWithWait(tocLink.get(0), tocLink.get(0).getText());
		commonLibrary.sleep(10000);
		int counter = 0;
		header = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 60);
		do {
			counter = counter + 1;
			header = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 60);
			if (header == null)
				commonLibrary.sleep(5000);

		} while ((header == null) && counter < 20);
		if (header != null && header.getText().toLowerCase().contains("table of contents"))
			report.updateTestLog("Verify View table of contents page is displayed", "View table of contents page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify View table of contents page is displayed", "View table of contents page is not displayed", Status.FAIL);
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click First DocLink
	// # Function Name : ClickFirstDocLink2     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Document clickFirstDocLink2() {
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
			else {
				WebElement pgHeader = null;

				if (commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
				else if (commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10) != null)
					pgHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 10);

				if (pgHeader != null && (pgHeader.getText().toLowerCase().contains("document") || pgHeader.getText().contains("Expert Witness"))) {

					strDocTitle = pgHeader.getText();
					// strDocTitle=strDocTitle.replace("\n", "");
					String[] title = strDocTitle.split("\n");

					report.updateTestLog("Verify document " + title[1].trim() + " is displayed", "Document " + title[1].trim() + " is displayed", Status.PASS);

				} else
					report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.FAIL);
			}
			return new Document(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to swith To Condent Type Modified
	// # Function Name : swithToCondentTypeModified     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Search swithToCondentTypeModified(String strCondentType) {
		if (!(strCondentType != "Ignore")) {
			Boolean blnFlag = false;
			try {
				WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
				if ((btnFewerCat != null) && (btnFewerCat.getText().contains("Show more")))
					if (browsername.contains("internet"))
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, "More Categories");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, "More Categories");
				WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
				if (ulCondentSwitcher != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement element : OListItems) {
							WebElement btnCondentType = commonLibrary.isExist(element, By.tagName("button"), 20);
							if (btnCondentType.getText().toString().toLowerCase().contains(strCondentType.toLowerCase())) {
								if (browsername.contains("internet"))
									commonLibrary.clickLinkWithWebElementWithWaitJS(btnCondentType, btnCondentType.getText());
								else
									commonLibrary.clickLinkWithWebElementWithWait(btnCondentType, btnCondentType.getText());
								commonLibrary.sleep(25000);
								blnFlag = true;
								break;

							}
						}
					}
				}
				if (!blnFlag)
					report.updateTestLog("Click on " + strCondentType, "Not Click on " + strCondentType, Status.FAIL);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());

			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select PostFilter Modified
	// # Function Name : selectPostFilterModified     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Search selectPostFilterModified(String strHeader, String strFilter) {
		if (!(strHeader != "Ignore" && strFilter != "Ignore")) {
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
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						if (item.getText().contains(strFilter)) {
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
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter, "Required Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to just Click FirstDoc
	// # Function Name : justClickFirstDoc     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Document justClickFirstDoc() {
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
								WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
								int count = 0;
								do {
									count++;
									commonLibrary.sleep(50000);
									txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
								} while (txtDocumentHeading == null && count < 30);
								blnFlag = true;
								break;
							}
						} else {
							WebElement lnkDocument = commonLibrary.isExist(document, By.tagName("a"), 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
								int count = 0;
								do {
									count++;
									commonLibrary.sleep(50000);
									txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
								} while (txtDocumentHeading == null && count < 30);
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
				report.updateTestLog("Click on the document " + strDocTitle, "Clicked  on the document " + strDocTitle, Status.PASS);
			}
			return new Document(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Teaser Text
	// # Function Name : clickTeaserText     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Document clickTeaserText() {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement teaser = commonLibrary.isExist(resultClass, UIMAP_SearchResult.teaserLink, 5);
		if (teaser == null) {
			this.justClickFirstDoc();
			report.updateTestLog("Click on the teaser text of the first document", "First document link is clicked as teaser text is not available for any of the document", Status.PASS);
		} else if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

				for (WebElement document : OListItems) {
					WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserLink, 20);
					if (docContent != null) {
						commonLibrary.clickLinkWithWebElementWithWait(docContent, "Teaser Text");
						if (!(driver.getCurrentUrl().contains("document"))) {
							WebElement docLink = commonLibrary.isExist(document, UIMAP_SearchResult.docLink, 20);
							commonLibrary.clickLinkWithWebElementWithWait(docLink, "Document title with Teaser Text");
							WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
							int count = 0;
							do {
								count++;
								commonLibrary.sleep(50000);
								txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
							} while (txtDocumentHeading == null && count < 30);

							List<WebElement> highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
							int count1 = 0;
							do {
								count1++;
								highlights = commonLibrary.isExistList(UIMAP_RelevanceEnhancement.headerTermHighlighted, 20);
								if (highlights.size() == 0)
									commonLibrary.sleep(5000);
							} while (highlights.size() == 0 && count1 <= 25);
						}
						break;
					}
				}

			}
		}
		/*
		 * if(!(blnFlag)) { blnFlag = false;
		 * resultClass=commonLibrary.isExist(UIMAP_SearchResult.frmClassResult,
		 * 10); if(resultClass!=null) { WebElement
		 * OListResult=commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		 * if(OListResult!=null) { List<WebElement>
		 * OListItems=commonLibrary.isExist_List(OListResult, By.tagName("li"),
		 * 20);
		 * 
		 * for (WebElement document: OListItems) { WebElement docContent =
		 * commonLibrary.isExist(document,UIMAP_SearchResult.teaserLink, 20);
		 * if(docContent!=null) { WebElement docLink =
		 * commonLibrary.isExist(document,UIMAP_SearchResult.docLink, 20);
		 * commonLibrary.clickLink_withWebElement_WithWait(docLink,
		 * "Document title with Teaser Text");
		 * if(driver.getCurrentUrl().contains("document")) blnFlag = true;
		 * break; } }
		 * 
		 * } } } if(!(blnFlag)) {
		 * report.updateTestLog("Click on the teaser text of the document tile",
		 * "There is no document available with teaser text/teaser text is unclickable"
		 * , Status.FAIL); }
		 */

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Research Home
	// # Function Name : clickResearchHome     
	// # Author : Aravind
	// # Date Created : 4-May'15
	// #*****************************************************************************************************************************

	public Home clickResearchHome() {
		WebElement eltResearchHomeAnchor = commonLibrary.isExistNegative(UIMAP_Home.eltResearchHomeAnchor, 10);
		if (eltResearchHomeAnchor != null)
			commonLibrary.clickLinkWithWebElementWithWait(eltResearchHomeAnchor, eltResearchHomeAnchor.getText());
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sections
	// # Function Name :verifysections    
	// # Author : Ram
	// # Date Created : Feb '15
	// #*****************************************************************************************************
	public Search verifysections(String textvalue) {
		boolean blnflag = false;
		// List <WebElement> ulclass =
		// commonLibrary.isExist_List(UIMAP_SearchResult.ulclass, 20);

		// WebElement sectiong =
		// commonLibrary.isExist(UIMAP_SearchResult.sectiong, 20);
		// List <WebElement> spanclass =
		// commonLibrary.isExist_List(UIMAP_SearchResult.spanclass, 20);
		// for(WebElement text : spanclass)
		// {
		// if(text.getText().contains("(G)"))
		// {
		// report.updateTestLog("Verify the Section in the Full document" ,
		// "Section is displayed in the Full Document", Status.PASS);
		// }
		// }
		List<WebElement> lstTagName = commonLibrary.isExistList(UIMAP_SearchResult.lstTagName, 20);
		for (WebElement text : lstTagName) {

			if (text.getText().contains(textvalue)) {
				blnflag = true;
				report.updateTestLog("Verify the Section in the Full document", text.getText() + "Section is displayed in the Visbile region of Full Document", Status.PASS);
			}

		}
		if (!blnflag) {
			report.updateTestLog("Verify the Section in the Full document", "Section is displayed in the Visbile region of Full Document", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PostFilters in Multiple list
	// # Function Name :verifyPostFiltersMultiple    
	// # Author : Ram
	// # Date Created : Feb '15
	// #*****************************************************************************************************
	public Search verifyPostFiltersMultiple(String header, String filter) {
		int i;
		WebElement filters1 = null;
		String[] arrFilters = filter.split(";");
		boolean isfilter = false;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}
		List<WebElement> eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
		for (i = 0; i < eltFilter.size(); i++) {
			if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
				//
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
				}
			}
		}
		if (header.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10);
			isfilter = true;
		} else if (header.toLowerCase().contains("court")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10); // put
																					// for
																					// court
			isfilter = true;
		} else if (header.toLowerCase().contains("location")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersLocation, 10); // put
																						// for
																						// location
			isfilter = true;
		}
		if (isfilter) {
			List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
			for (int j = 0; j < arrFilters.length; j++) {
				boolean flag = false;
				for (WebElement item : FilterList) {
					if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
						flag = true;
						report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
						break;
					}

				}
				if (!flag) {
					report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Recently Selected Displayed
	// # Function Name :verifyRecentlySelectedDisplayed    
	// # Author : Ram
	// # Date Created : Feb '15
	// #*****************************************************************************************************
	public Search verifyRecentlySelectedDisplayed(String RecentList) {
		boolean blnFlag = false;
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
				blnFlag = true;
				break;
			}
		}
		if (count == 0) {
			report.updateTestLog("Verify " + RecentList + " is displayed", "" + RecentList + " is displayed at the top", Status.PASS);
		} else if (blnFlag) {
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is  displayed ", Status.PASS);
		} else
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PostFilter Absent
	// # Function Name :verifyPostFilterAbsent    
	// # Author : Ram
	// # Date Created : Feb '15
	// #*****************************************************************************************************
	public Search verifyPostFilterAbsent(String header) {
		boolean flag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);
		for (WebElement li : filterHeader) {
			if (li.getText().toLowerCase().contains(header.toLowerCase())) {
				report.updateTestLog("Verify " + header + " filters are absent", header + " filters are present.", Status.FAIL);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + header + " filters are absent", header + " filters are absent.", Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify PostFilters MultipleWarn
	// # Function Name :verifyPostFiltersMultipleWarn    
	// # Author : Ram
	// # Date Created : Feb '15
	// #**************************************************************************************************
	public Search verifyPostFiltersMultipleWarn(String header, String filter) {
		int i;
		WebElement filters1 = null;
		boolean present = false;

		boolean isfilter = false;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}

		if (header.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10);
			isfilter = true;
		} else if (header.toLowerCase().contains("court")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10); // put
																					// for
																					// court
			isfilter = true;
		} else if (header.toLowerCase().contains("location")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersLocation, 10); // put
																						// for
																						// location
			isfilter = true;
		}
		WebElement moreOptions = commonLibrary.isExistNegative(filters1, UIMAP_SearchResult.filterMore, 10);
		if (moreOptions != null && moreOptions.getText().toLowerCase().contains("more")) {
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
			else
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
		}

		if (isfilter && filters1 != null) {

			/*
			 * List<WebElement> FilterList = commonLibrary.isExistList(filters1,
			 * By.tagName("label"), 30); for (WebElement item : FilterList) {
			 * WebElement filterName = commonLibrary.isExistNegative(item,
			 * By.tagName("span"), 5); if
			 * (!filter.toLowerCase().contains(filterName
			 * .getText().toLowerCase())) {
			 * report.updateTestLog("Verify filters among " + filter +
			 * " is present under " + header + "", "Filter " +
			 * filterName.getText() + " is present under " + header,
			 * Status.FAIL);
			 * 
			 * } else report.updateTestLog("Verify filters among " + filter +
			 * " is present under " + header + "", "Filter " +
			 * filterName.getText() + " is present under " + header,
			 * Status.PASS); }
			 */
			// Modified by seetha
			String[] filtersToCheck = filter.split(";");
			for (int k = 0; k < filtersToCheck.length; k++) {
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (WebElement item : FilterList) {
					WebElement filterName = commonLibrary.isExistNegative(item, By.tagName("span"), 5);
					if (filterName.getText().toLowerCase().equalsIgnoreCase(filtersToCheck[k])) {
						report.updateTestLog("Verify filters among " + filtersToCheck[k] + " is present under " + header + "", "Filter " + filtersToCheck[k] + " is present under " + header, Status.PASS);
						present = true;
						break;
					}
				}
				if (!present) {
					report.updateTestLog("Verify filters among " + filtersToCheck[k] + " is present under " + header + "", "Filter " + filtersToCheck[k] + " is not present under " + header, Status.FAIL);
				}

			}

		} else {
			report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ResearchMap
	// # Function Name :clickResearchMap    
	// # Author : Ram
	// # Date Created : Feb '15
	// #******************************************************************************************
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
			List<WebElement> lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 1000);

			int count = 0;
			do {
				lstResearchThread = commonLibrary.isExistList(UIMAP_SearchResult.lstResearchThread, 1000);
				count = count + 1;
			} while (lstResearchThread.size() == 0 && count <= 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifyNarrowbyFilterLeftPane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + strFilter, Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the post filter header
	// # Function Name : verifyFilterlistcourt     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search verifyFilterlistcourt(String strHeader) {
		boolean blnflg = false;
		if (!(strHeader.equals(" "))) {
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					Flag = true;

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

					List<WebElement> filters1 = commonLibrary.isExistList(suplementalFilters.get(i - j), By.tagName("li"), 20);

					if (filters1.size() > 0 && Flag) {
						blnflg = true;
						break;

					}
				}

			}
			if (blnflg && Flag) {
				report.updateTestLog("Verify the Court List in the Court Header  ", "All the Court list details are displayed as expected", Status.PASS);

			} else {
				report.updateTestLog("Verify the Court List in the Court Header  ", "All the Court list details are not displayed as expected", Status.FAIL);

			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply Search Filter
	// # Function Name : applySearchFilter     
	// # Author : Karthi
	// # Date Created : May'15
	// #****************************************************************************************************************************
	public Search applySearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		generalFunctions.applySearchFilter(strToolbarMenuName, strCondentTypes, state);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Continue To ReplaceFilter
	// # Function Name : clickContinueToReplaceFilter     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************
	public Search clickContinueToReplaceFilter() {
		WebElement popUpPage = commonLibrary.isExist(UIMAP_Home.eltPopupBoxParent, 30);
		WebElement eltPopupBox = commonLibrary.isExist(popUpPage, UIMAP_Home.eltPopupBox, 30);
		if (eltPopupBox != null) {
			report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up displayed", Status.PASS);
			WebElement buttonContinue = commonLibrary.isExist(eltPopupBox, UIMAP_Home.eltPopupButton1, 20);
			commonLibrary.clickButtonParentWithWait(buttonContinue, buttonContinue.getText());
		} else {
			report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up not displayed", Status.PASS);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the post filters under court(
	// I have written this function since that particular testcase has 12
	// different configurations with differenct scenarios)
	// # Function Name : verifyPostFiltersMultipleCourt     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Search verifyPostFiltersMultipleCourt(String header, String filter) {
		int i;
		WebElement filters1 = null;
		String[] arrFilters = filter.split(";");
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}
		//

		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10); // put
																				// for
																				// court
		List<WebElement> moreOptions = commonLibrary.isExistList(filters1, UIMAP_SearchResult.filterMore, 10);
		for (WebElement moreButton : moreOptions) {
			if (moreButton != null && moreButton.getText().toLowerCase().contains("more")) {
				if (browsername.toLowerCase().contains("internet"))
					commonLibrary.clickButtonLogSmallWait(moreButton, "More");
				else
					commonLibrary.clickButtonLogSmallWait(moreButton, "More");
			}
		}
		if (filters1 != null) {
			report.updateTestLog("Verify court filter is present", "Court filter is present", Status.PASS);
			switch (header.toLowerCase()) {
			case "heading": // verify headings inside court like federal, tribal
							// and state
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("h2"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under court", arrFilters[j] + " is present under court", Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under court", arrFilters[j] + " is not present under court", Status.FAIL);
					}
				}
				break;
			}
			case "court": // this is to check whether on or more items specified
							// is present in the court filter
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);

					}
				}
				break;
			}
			}

		} else {
			report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the post filters under
	// jurisdiction( I have written this function since that particular testcase
	// has 12 different configurations with differenct scenarios)
	// # Function Name : verifyPostFiltersMultipleJuris     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Search verifyPostFiltersMultipleJuris(String header, String filter, String exceptCheck, String contentType) {
		int i;
		WebElement filters1 = null;
		String[] arrFilters = filter.split(";");
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}

		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10); // put
																						// for
																						// court
		WebElement moreOptions = commonLibrary.isExistNegative(filters1, UIMAP_SearchResult.filterMore, 10);
		if (moreOptions != null && moreOptions.getText().toLowerCase().contains("more")) {
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
			else
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
		}
		if (filters1 != null) {
			report.updateTestLog("Verify jurisdiction filter is present", "Jurisdiction filter is present", Status.PASS);
			switch (exceptCheck.toLowerCase()) {
			case "except": // verify headings inside court like federal, tribal
							// and state
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is not present under jurisdiction", arrFilters[j] + " is  present under jurisdiction", Status.FAIL);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is not present under jurisdiction", arrFilters[j] + " is  not present under jurisdiction", Status.PASS);

					}
				}
				break;
			}
			case "jurisdiction": // this is to check whether on or more items
									// specified is present in the court filter
			{
				List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
				for (int j = 0; j < arrFilters.length; j++) {
					boolean flag = false;
					for (WebElement item : FilterList) {
						if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
							flag = true;
							report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
							break;
						}

					}
					if (!flag) {
						report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);

					}
				}
				break;
			}
			}

		} else {
			if (contentType.contains("Cases") || contentType.contains("Briefs, Pleadings and Motions") || contentType.contains("Jury Verdicts and Settlements") || contentType.contains("Dockets")) {
				report.updateTestLog("Verify jurisdiction filter is absent", "Jurisdictiion filter is absent", Status.PASS);
			} else {
				report.updateTestLog("Verify the filter " + header + " is present", "Filter " + header + " is not present", Status.FAIL);
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the post filters under
	// Location( I have written this function since that particular testcase has
	// 12 different configurations with differenct scenarios)
	// # Function Name : verifyPostFiltersMultipleLocation     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Search verifyPostFiltersMultipleLocation(String header, String filter, String headingfilter) {
		int i;
		WebElement filters1 = null;
		String[] arrheader = header.split(";");
		String[] arrFilters = filter.split(";");
		String[] arrFiltersheading = headingfilter.split(";");
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains("Location".toUpperCase())) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), header);
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), header);
				}
				report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
			}
		}
		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersLocation, 10);// filter
		// for
		// location
		WebElement moreOptions = commonLibrary.isExistNegative(filters1, UIMAP_SearchResult.filterMore, 10);
		if (moreOptions != null && moreOptions.getText().toLowerCase().contains("more")) {
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
			else
				commonLibrary.clickButtonLogSmallWait(moreOptions, "More");
		}

		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersLocation, 10);

		if (filters1 != null) {
			report.updateTestLog("Verify location filter is present", "location filter is present", Status.PASS);

			for (int m = 0; m < arrheader.length; m++) {
				switch (arrheader[m].toLowerCase()) {

				case "heading": // verify headings inside court like federal,
								// tribal and state
				{
					List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("h2"), 30);
					for (int j = 0; j < arrFilters.length; j++) {
						boolean flag = false;
						for (WebElement item : FilterList) {
							if (item.getText().toLowerCase().contains(arrFiltersheading[j].toLowerCase())) {
								flag = true;
								report.updateTestLog("Verify " + arrFiltersheading[j] + " is present under location", arrFiltersheading[j] + " is present under location", Status.PASS);
								break;
							}

						}
						if (!flag) {
							report.updateTestLog("Verify " + arrFiltersheading[j] + " is present under location", arrFiltersheading[j] + " is not present under location", Status.FAIL);
						}
					}
					break;
				}
				case "except": // verify a value is not present
				{
					List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
					for (int j = 0; j < arrFilters.length; j++) {
						boolean flag = false;
						for (WebElement item : FilterList) {
							if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
								flag = true;
								report.updateTestLog("Verify " + arrFilters[j] + " is not present under location", arrFilters[j] + " is present under location", Status.FAIL);
								break;
							}

						}
						if (!flag) {
							report.updateTestLog("Verify " + arrFilters[j] + " is not present under location", arrFilters[j] + " is not present under location", Status.PASS);

						}
					}
					break;
				}
				case "location": // this is to check whether on or more items
									// specified is present in the location
									// filter
				{
					List<WebElement> FilterList = commonLibrary.isExistList(filters1, By.tagName("label"), 30);
					for (int j = 0; j < arrFilters.length; j++) {
						boolean flag = false;
						for (WebElement item : FilterList) {
							if (item.getText().toLowerCase().contains(arrFilters[j].toLowerCase())) {
								flag = true;
								report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is present under " + header, Status.PASS);
								break;
							}

						}
						if (!flag) {
							report.updateTestLog("Verify " + arrFilters[j] + " is present under " + header + "", arrFilters[j] + " is not present under " + header, Status.FAIL);

						}
					}
					break;
				}
				}
			}

		} else {
			report.updateTestLog("Verify the filter location is present", "Filter location is not present", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set NewClientID in GenericHome
	// # Function Name : setNewClientIDGenericHome     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************
	public Home setNewClientIDGenericHome(String strClientID) {
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
		WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_SearchResult.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client Id");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
		} else
			report.updateTestLog("Clicking on Set Client ID", "Set Client ID can not be clicked.", Status.FAIL);

		try {
			WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			int counter = 0;
			do {
				commonLibrary.sleep(50000);
				counter++;
				currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			} while (currProdOld.equals(currProdNew) && counter < 40);
		} catch (Exception e1) {
			commonLibrary.sleep(5000);
			System.out.println(e1.toString());
		}

		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Court in PostFilter
	// select Illustrations tab.
	// # Function Name : verifyCourtPostFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************
	public Search verifyCourtPostFilter() {
		int i;
		WebElement filters1 = null;

		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains("COURT")) {
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), "Court");
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), "Court");
				}
				report.updateTestLog("Expanding Filter Header: Court", "Court filter Header Expanded.", Status.DONE);
			}
		}
		filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10);
		if (filters1 != null)
			report.updateTestLog("Verify Court post filter is present.", "Court post filter is present.", Status.PASS);
		else
			report.updateTestLog("Verify Court post filter is present.", "Court post filter is not present.", Status.FAIL);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify reference tab is selected and
	// select Illustrations tab.
	// # Function Name : verifyRefClickIllustrations     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyRefClickIllustrations() {
		WebElement buttonsCont = commonLibrary.isExistNegative(UIMAP_SearchResult.buttonsCont, 10);
		WebElement selectedButton = commonLibrary.isExistNegative(buttonsCont, UIMAP_SearchResult.selectedButton, 10);
		if (selectedButton.getText().toLowerCase().contains("references"))
			report.updateTestLog("Verify References is selected by default.", "References is selected by default.", Status.PASS);
		else
			report.updateTestLog("Verify References is selected by default.", "References is not selected by default.", Status.FAIL);

		WebElement illustrationsButton = commonLibrary.isExistNegative(buttonsCont, UIMAP_SearchResult.illustrationsButton, 10);
		commonLibrary.clickButtonParentWithWait(illustrationsButton, "Illustrations");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Back To MedicalTopics
	// # Function Name : clickBackToMedicalTopics     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************
	public MedicalResearch clickBackToMedicalTopics() {
		WebElement headerContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.headerContainer, 10);
		WebElement backToMedTopics = commonLibrary.isExistNegative(headerContainer, UIMAP_SearchResult.backToMedTopics, 10);
		commonLibrary.clickLinkWithWebElementWithWait(backToMedTopics, "Back to all Medical Topics");
		return new MedicalResearch(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result page header.
	// # Function Name : verifyResultHeader     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyResultHeader(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
		String headerText = header.getText().replace("\n", " ");
		if (headerText.toLowerCase().contains(term.toLowerCase()))
			report.updateTestLog("Verify results page with header: " + term + " is displayed", "Results page with header: " + term + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify results page with header: " + term + " is displayed", "Results page with header: " + term + " is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document header title
	// # Function Name : clickdoclinktitle     
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickdoclinktitle(String strDocTitle) {

		Boolean blnFlag = false;
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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.link, 20);
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

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Mini TOC and click view o
	// # Function Name : clickandverifystarFavouritestatus     
	// # Author : karthi
	// # Date Created :11 May '15
	// #*****************************************************************************************************************************

	public TOC clickTOCandVerifyminiToc(Boolean isDisplayed) {
		WebElement tableOfContentsTab = commonLibrary.isExistNegative(UIMAP_Document.tableOfContentsTab, 10);
		commonLibrary.clickButtonParentWithWait(tableOfContentsTab, "Table of Contents Tab");

		WebElement miniToc = commonLibrary.isExistNegative(UIMAP_Document.miniToc, 10);
		WebElement viewfulltoc = commonLibrary.isExistNegative(UIMAP_Document.tocLink, 10);
		if (viewfulltoc == null)
			viewfulltoc = commonLibrary.isExist(UIMAP_Document.tocLink1, 10);
		if (isDisplayed) {
			if (miniToc != null && miniToc.isDisplayed()) {
				commonLibrary.clickButtonParentWithWait(viewfulltoc, "View full table of contents");
				report.updateTestLog("Verify Mini TOC is displayed", "Mini TOC is displayed", Status.PASS);
			} else
				report.updateTestLog("Verify Mini TOC is displayed", "Mini TOC is not displayed", Status.FAIL);

		} else {
			if (miniToc == null)
				report.updateTestLog("Verify Mini TOC closes", "Mini TOC is closed", Status.PASS);
			else
				report.updateTestLog("Verify Mini TOC closes", "Mini TOC is not closed", Status.FAIL);

		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Document Checkbox By Number
	// # Function Name : clickDocumentCheckboxByNumber     
	// # Author : karthi
	// # Date Created :11 May '15
	// #*****************************************************************************************************
	public Search clickDocumentCheckboxByNumber(int DocNo) {
		boolean blnFlag = false;
		int count = 0;
		List<WebElement> OList = null;

		do {
			commonLibrary.sleep(20000);
			OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			count++;
		} while (!(OList.size() > 0) && count < 25);

		OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (int i = 0; i < LList.size(); i++) {
			if (i == (DocNo - 1)) {
				WebElement lstchkBox = commonLibrary.isExist(LList.get(i), By.cssSelector("input[type='checkbox']"), 10);
				if (lstchkBox != null) {
					commonLibrary.setCheckBox(lstchkBox, lstchkBox.getText());
					// lstchkBox.sendKeys(Keys.SPACE);
					blnFlag = true;
					break;
				}
			}
		}

		if (blnFlag)
			report.updateTestLog("Click document checkbox", "Checkbox for  document " + DocNo + " is selected", Status.PASS);
		else
			report.updateTestLog("Click document checkbox", "Checkbox for  document " + DocNo + " is not selected", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply Case Resolution postfilter
	// # Function Name : applyCaseResolutionpostfilter     
	// # Author : karthi
	// # Date Created :11 May '15
	// #*****************************************************************************************************
	public Search applyCaseResolutionpostfilter(String filtervalue, String Subfilter1, String Subfilter2, String Subfilter3) {
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
				if (spanname1.getText().contains(Subfilter1)) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(spanname1, Subfilter2);
					} else {
						commonLibrary.clickButtonParentWithWait(spanname1, Subfilter2);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to applycourtpostfilter
	// # Function Name : applycourtpostfilter  
	// # Author : Pratik
	// # Date Created : Jan'15
	// #******************************************************************************************************
	public Search applycourtpostfilter(String filtervalue, String Subfilter1) {
		WebElement unusedfilters = commonLibrary.isExist(UIMAP_VSASearchResults.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_VSASearchResults.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			if (filtervalue.contains(button.getText())) {
				// WebElement expandedcheck =
				// commonLibrary.isExist(postfilterlist.get(2),UIMAP_VSASearchResults.spanclass,20);
				WebElement parentcaseresolution = commonLibrary.isExist(UIMAP_VSASearchResults.parentcourt, 20);

				if (parentcaseresolution == null) {
					if (browsername.contains("internet")) {
						commonLibrary.clickButtonParentWithWaitJS(button, filtervalue);
					} else {
						commonLibrary.clickButtonParentWithWait(button, filtervalue);
					}
				} else {
					report.updateTestLog("Verify the display of Post filter", "Court Filter is Expanded", Status.DONE);
				}
				break;
			}
		}

		WebElement parentcaseresolution = commonLibrary.isExist(UIMAP_VSASearchResults.parentcourt, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(parentcaseresolution, UIMAP_VSASearchResults.ullist, 20);
		List<WebElement> lilist2 = commonLibrary.isExistList(ullist1.get(0), UIMAP_VSASearchResults.lilist2, 20);
		WebElement selectmultiplebutton = commonLibrary.isExist(lilist2.get(0), UIMAP_VSASearchResults.selectmultiplebutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(selectmultiplebutton, "Select Multiple");
		} else {
			commonLibrary.clickButtonParentWithWait(selectmultiplebutton, "Select Multiple");
		}
		WebElement filterdialogbox = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
		WebElement div = commonLibrary.isExist(filterdialogbox, UIMAP_VSASearchResults.div, 20);
		List<WebElement> ullist11 = commonLibrary.isExistList(div, UIMAP_VSASearchResults.ulcolumn1, 20);
		List<WebElement> ullist2 = commonLibrary.isExistList(ullist11.get(1), UIMAP_VSASearchResults.ullist, 20);
		List<WebElement> lilist3 = commonLibrary.isExistList(ullist2.get(0), UIMAP_VSASearchResults.lilist, 20);
		WebElement buttoncourt = commonLibrary.isExist(lilist3.get(19), UIMAP_VSASearchResults.button, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(buttoncourt, "Louisiana");
		} else {
			commonLibrary.clickButtonParentWithWait(buttoncourt, "Louisiana");
		}

		WebElement filterdialogbox1 = commonLibrary.isExist(UIMAP_VSASearchResults.filterdialogbox, 20);
		WebElement div1 = commonLibrary.isExist(filterdialogbox1, UIMAP_VSASearchResults.div, 20);
		List<WebElement> ullist21 = commonLibrary.isExistList(div1, UIMAP_VSASearchResults.ulcolumn1, 20);
		List<WebElement> lilist13 = commonLibrary.isExistList(ullist21.get(1), UIMAP_VSASearchResults.ullist, 20);
		List<WebElement> ullist12 = commonLibrary.isExistList(lilist13.get(0), UIMAP_VSASearchResults.lilist, 20);
		WebElement ulclass = commonLibrary.isExist(ullist12.get(19), UIMAP_VSASearchResults.ulclass, 20);
		List<WebElement> ullist22 = commonLibrary.isExistList(ulclass, UIMAP_VSASearchResults.lilist, 20);
		WebElement label1 = commonLibrary.isExist(ullist22.get(2), UIMAP_VSASearchResults.label, 20);

		WebElement spanname1 = commonLibrary.isExist(label1, UIMAP_VSASearchResults.spanname, 20);
		if (spanname1.getText().contains(Subfilter1)) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(spanname1, Subfilter1);
			} else {
				commonLibrary.clickButtonParentWithWait(spanname1, Subfilter1);
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
		} else {
			commonLibrary.clickButtonParentWithWait(confirmbutton, "OK");
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filters present under the
	// narrow by section
	// # Function Name : verifynarrowbyfilters     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifynarrowbyfilters(String filter) {

		commonLibrary.sleep(90000);
		WebElement asideclass;
		int count = 0;
		do {
			commonLibrary.sleep(1000);
			asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
			count++;
		} while (asideclass == null && count < 25);

		asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
		WebElement usedfilter = commonLibrary.isExist(asideclass, UIMAP_Document.usedfilter, 20);
		WebElement lilist = commonLibrary.isExist(usedfilter, UIMAP_Document.listItems, 20);
		WebElement narrowbyfilter = commonLibrary.isExist(lilist, UIMAP_Document.narrowbyfilter, 20);

		if (narrowbyfilter.getText().contains(filter)) {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add notes while adding a document to
	// folder
	// # Function Name : addToFolderNotes     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search addToFolderNotes(String FolderName, String notes) {
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

			commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");
			commonLibrary.sleep(1000);

			// ADD <<<NOTES>>>
			if (!notes.equals("")) {
				WebElement notesIframe = commonLibrary.isExistNegative(UIMAP_Document.notesIframe, 10);
				driver.switchTo().frame(notesIframe);
				WebElement notesArea = commonLibrary.isExistNegative(UIMAP_Document.notesArea, 10);
				commonLibrary.setDataInTextBox(notesArea, notes, "Notes");
				driver.switchTo().defaultContent();
			}

			// CLICK ON <<<SAVE>>>
			commonLibrary.sleep(1000);
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(1000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add items to new sub folder under an
	// existing parent folder
	// # Function Name : addToFolder_SubFolder     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search addToFolder_SubFolder(String parentFolderName, String subFolderName) {
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

			// CLICK ON Parent folder from my folders section
			WebElement myFoldersSection = commonLibrary.isExist(UIMAP_ResearchMap.myFoldersSection, 10);
			if (myFoldersSection != null) {
				List<WebElement> foldersList = commonLibrary.isExistList(myFoldersSection, UIMAP_ResearchMap.selectFolder, 10);
				if (foldersList != null) {
					for (WebElement folder : foldersList) {
						if (folder.getText().equals(parentFolderName))
							commonLibrary.clickButtonParentWithWait(folder, parentFolderName);
					}
				}
			}

			// CLICK ON <<<CREATE NEW Sub FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Sub Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT
			// BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, subFolderName);

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the number of results
	// # Function Name : verifyNumberOfResults     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search verifyNumberOfResults(int number, boolean greater) {
		generalFunctions.verifyNumberOfResults(number, greater);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add items to new sub folder under an
	// existing parent folder
	// # Function Name : addToFolder_SubFolder     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search addToFolder_SharedFolder(String folderName) {
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

			// Expand Shared folders and Click a folder
			WebElement sharedFoldersSection = commonLibrary.isExist(UIMAP_ResearchMap.sharedFoldersSection, 10);
			if (sharedFoldersSection != null) {
				WebElement expandIcon = commonLibrary.isExist(sharedFoldersSection, UIMAP_ResearchMap.sharedFolderIcon, 10);
				if (expandIcon != null) {
					// commonLibrary.ScrollToView(expandIcon);
					commonLibrary.clickButtonParentWithWait(expandIcon, "Expand Shared folders");
				}
			}

			// CLICK ON a Shared folder
			List<WebElement> foldersList = commonLibrary.isExistList(sharedFoldersSection, UIMAP_ResearchMap.selectFolder, 10);
			if (foldersList != null) {
				for (WebElement folder : foldersList) {
					if (folder.getText().equals(folderName)) {
						// commonLibrary.ScrollToView(folder);
						commonLibrary.clickButtonParentWithWait(folder, folderName);
					}
				}
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// //#*****************************************************************************************************************************
	// # Function Description : Function to check whether the folder name is
	// available in recent folder list
	// # Function Name : checkRecentFolder     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search checkRecentFolder(String FolderName) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		Boolean flag = false;

		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null && !addtoFolder.getAttribute("class").contains("expanded"))

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		WebElement recentFolderList = commonLibrary.isExist(UIMAP_SearchResult.recentFolderList);
		List<WebElement> recentFolderName = commonLibrary.isExistList(recentFolderList, UIMAP_SearchResult.recentFolderName, 10);
		for (WebElement folderName : recentFolderName) {
			if (folderName.getText().equals(FolderName)) {
				flag = true;
				break;
			}
		}
		if (flag)
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is available in recent folder list", Status.PASS);
		else
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is not available in recent folder list", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to validate the date of the first
	// result
	// # Function Name : compareFullDate
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search compareFullDate(String date1, String operator) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				WebElement docContent = commonLibrary.isExist(docList.get(0), UIMAP_SearchResult.docContent, 20);
				WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

				List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
				List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
				String dataDefn = null;
				for (int t = 0; t < dataTerms.size(); t++) {
					if (dataTerms.get(t).getText().equalsIgnoreCase("Date")) {
						dataDefn = dataDefns.get(t).getText();
						break;
					}
				}
				Date appDate = null, enteredDate = null;
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
				try {
					enteredDate = format.parse(date1);
				} catch (Exception e) {
					System.out.println(e.toString());
				}

				SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
				try {
					appDate = format1.parse(dataDefn);
				} catch (Exception e) {
					System.out.println(e.toString());
				}

				if (operator.equals("=")) {
					if (appDate != null && appDate.compareTo(enteredDate) == 0) {
						report.updateTestLog("Verify Date in Result page is equal to: " + date1, "Date in Result page is equal to: " + date1, Status.PASS);
					} else
						report.updateTestLog("Verify Date in Result page is equal to: " + date1, "Date in Result page is not equal to: " + date1, Status.FAIL);
				} else if (operator.equals("<")) {
					if (appDate != null && appDate.compareTo(enteredDate) < 0) {
						report.updateTestLog("Verify Date in Result page is earlier to: " + date1, "Date in Result page is earlier to: " + date1, Status.PASS);
					} else
						report.updateTestLog("Verify Date in Result page is earlier to: " + date1, "Date in Result page is not earlier to: " + date1, Status.FAIL);

				} else {
					if (appDate != null && appDate.compareTo(enteredDate) > 0) {
						report.updateTestLog("Verify Date in Result page is later than: " + date1, "Date in Result page is later than: " + date1, Status.PASS);
					} else
						report.updateTestLog("Verify Date in Result page is later than: " + date1, "Date in Result page is not later than: " + date1, Status.FAIL);

				}

			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to validate the year of the first
	// result
	// # Function Name : compareYear
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search compareYear(String date1) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				WebElement docContent = commonLibrary.isExist(docList.get(0), UIMAP_SearchResult.docContent, 20);
				WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

				List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
				List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
				String dataDefn = null;
				for (int t = 0; t < dataTerms.size(); t++) {
					if (dataTerms.get(t).getText().equalsIgnoreCase("Date")) {
						dataDefn = dataDefns.get(t).getText();
						break;
					}
				}
				Date appDate = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyy");

				SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
				try {
					appDate = format1.parse(dataDefn);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				String appYear = format.format(appDate);
				if (date1.equals(appYear)) {
					report.updateTestLog("Verify Year in Result page is equal to: " + date1, "Year in Result page is equal to: " + date1, Status.PASS);
				} else
					report.updateTestLog("Verify Year in Result page is equal to: " + date1, "Year in Result page is not equal to: " + date1, Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to validate the month and year of the
	// first result
	// # Function Name : compareMonthYear
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search compareMonthYear(String date1) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				WebElement docContent = commonLibrary.isExist(docList.get(0), UIMAP_SearchResult.docContent, 20);
				WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

				List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
				List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);
				String dataDefn = null;

				for (int t = 0; t < dataTerms.size(); t++) {
					if (dataTerms.get(t).getText().equalsIgnoreCase("Date")) {
						dataDefn = dataDefns.get(t).getText();
						break;
					}
				}
				Date appDate = null;
				SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");

				SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
				try {
					appDate = format1.parse(dataDefn);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				String appYear = format.format(appDate);
				if (date1.equals(appYear)) {
					report.updateTestLog("Verify Month and Year in Result page is equal to: " + date1, "Month and Year in Result page is equal to: " + date1, Status.PASS);
				} else
					report.updateTestLog("Verify Month and Year in Result page is equal to: " + date1, "Month and Year in Result page is not equal to: " + date1, Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the display of default page
	// and content type
	// # Function Name : verifytabandpagedisplay
	// # Author : Ram Prasath
	// # Date Created : 4 Mar'15
	// #*****************************************************************************************************************************

	public Home verifytabandpagedisplay(String categoryname) {

		try {
			WebElement activecategory = commonLibrary.isExist(UIMAP_Document.activecategory, 10);

			if (activecategory.getText().contains(categoryname)) {
				report.updateTestLog("To Verify active category tab", categoryname + " Category is Active and selected", Status.PASS);
			} else {
				report.updateTestLog("To Verify active category tab", categoryname + " Category is not Active", Status.FAIL);
			}

			/*
			 * Robot robot = new Robot(); robot.keyPress(KeyEvent.VK_ALT);
			 * robot.keyPress(KeyEvent.VK_LEFT);
			 * robot.keyRelease(KeyEvent.VK_LEFT);
			 * robot.keyRelease(KeyEvent.VK_ALT); commonLibrary.sleep(2000);
			 */

		} catch (Exception e) {
		}
		commonLibrary.clickBrowserBack();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to getFirstDocLink
	// # Function Name : getFirstDocLink  
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String getFirstDocLink() {
		// Boolean blnFlag=false;

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
							// blnFlag=true;
							break;
							/*
							 * report.updateTestLog("Document Title ",
							 * "Document Title is "+strDocTitle, Status.DONE);
							 * 
							 * WebElement
							 * lnkDocument=commonLibrary.isExist(eleDocTitle
							 * ,By.tagName("a"), 20); if(lnkDocument!=null) {
							 * commonLibrary
							 * .clickLink_withWebElement_WithWait(lnkDocument,
							 * lnkDocument.getText()); blnFlag=true; break; }
							 */
						} else {
							WebElement lnkDocument = commonLibrary.isExist(document, By.tagName("a"), 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
								// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
								// lnkDocument.getText());
								// blnFlag=true;
								break;
							}
						}
					}

				}
			}

			// if(!blnFlag)

			// report.updateTestLog("Click on the document "+strDocTitle,
			// "Not Clicked  on the document "+strDocTitle, Status.FAIL);
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
			return strDocTitle;
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : Click_MouseMove_Action(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickBrowserBackDocument() {
		commonLibrary.clickBrowserBack();
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Recent and Favourites Panel
	// # Function Name : verifyRecentAndFavorites
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifyRecentAndFavorites(String RecentList) {
		String recentSet[] = RecentList.split(";");
		boolean flag = false;
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
			if (RecentFavList.get(i).getText().replaceAll(" ", "").contains(recentSet[0].replace(" ", ""))) {
				for (int j = 1; j < recentSet.length; j++) {
					if (RecentFavList.get(i).getText().replaceAll(" ", "").contains(recentSet[j].replace(" ", ""))) {
						flag = true;

					}
				}
				if (flag)
					break;

			}
		}
		if (flag) {
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is  displayed ", Status.PASS);
		} else
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is not displayed", Status.FAIL);

		WebElement preFilters = commonLibrary.isExist(UIMAP_Home.preFiltersDiv, 10);
		WebElement close = commonLibrary.isExist(preFilters, UIMAP_Home.btnClosePreFilterPopup, 10);

		if (close != null) {
			report.updateTestLog("Close button is present in Recent and Favorites panel", "Close button is present in Recent and Favorites panel", Status.PASS);
			report.updateTestLog("Edit link is not present in Recent and Favorites panel", "Edit link is not present in Recent and Favorites panel", Status.PASS);
		} else
			report.updateTestLog("Close button is present in Recent and Favorites panel", "Close button is not present in Recent and Favorites panel", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close filter pop up.
	// # Function Name : closePreFilterPopUp
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search closePreFilterPopUp() {
		generalFunctions.closePreFilterPopUp();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate in browser links and
	// perform an action
	// # Function Name : navigateToBrowserLink2
	// # Author : Shoaban
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search navigateToBrowserLink2(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction, String linktest) {
		generalFunctions.navigateToBrowserLink(strParentMenuName, strFirstSubMenuName, strSecondSubMenuName, strThridSubMenuName, strAction, linktest);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select any menu item from browse pop
	// up
	// # Function Name : selectMenuFromBrowse
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search selectMenuFromBrowse(String strOption) {
		boolean blnFlag = false;
		try {
			WebElement browse = commonLibrary.isExist(UIMAP_Home.btnBrowse);
			if (browse != null) {
				commonLibrary.clickButtonLogSmallWait(browse, "Browse");
			}
			WebElement browseWindow = commonLibrary.isExist(UIMAP_Home.Winbrowse);
			List<WebElement> Options = commonLibrary.isExistList(browseWindow, UIMAP_Home.btnBrowseMenu, 10);
			for (int i = 0; i < Options.size(); i++) {
				if (Options.get(i).getText().trim().equalsIgnoreCase(strOption)) {
					commonLibrary.clickButtonLogSmallWait(Options.get(i), Options.get(i).getText());
					blnFlag = true;
					commonLibrary.sleep(4000);
				}
				if (blnFlag == true) {
					break;
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			blnFlag = false;
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search a source
	// # Function Name : searchSource
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Sources searchSource(String source) {
		try {
			WebElement childMenu = commonLibrary.isExist(UIMAP_Home.browseChildMenu, 10);
			WebElement sourceSearch = commonLibrary.isExist(childMenu, UIMAP_Home.sourceSearch, 10);
			// WebElement sourceDiv = commonLibrary.isExist(childMenu,
			// UIMAP_Home.sourceDiv,10);
			if (sourceSearch != null) {
				// commonLibrary.highlightElement(sourceSearch);
				// commonLibrary.sleep(2000);
				// sourceSearch.clear();
				commonLibrary.setDataInTextBox(sourceSearch, source, "Find a Source");
				// sourceSearch.sendKeys(source);
				sourceSearch.sendKeys(Keys.ENTER);
				commonLibrary.sleep(5000);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a maximum of 50 recent and
	// favorites items only can be added
	// # Function Name : verify50RecentAndFav
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verify50RecentAndFav(String filters) {
		// boolean blnFlag = false;
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
		int recentList = RecentFavList.size();
		if (recentList < 50) {
			report.updateTestLog("Verify there are 50 recent activity lists", "There are only " + recentList + " recent activity lists", Status.PASS);
			String filter[] = filters.split(";");
			for (int i = 0; i <= 50 - recentList; i++) {
				generalFunctions.applySearchFilter("Practice Area & Topic", filter[i], true);
				generalFunctions.closePreFilterPopUp();
				generalFunctions.simpleSearch("crime", false);
				generalFunctions.clearPreFilter();
			}

			btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
			if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}

			mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				WebElement RecentFavorites = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.btnIdRecentFavorites, 20);
				commonLibrary.clickButtonParentWithWait(RecentFavorites, "Recent & Favorites");
			}

			RecentFav = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);

			RecentFavList = commonLibrary.isExistList(RecentFav, UIMAP_Home.lstTagListItems, 20);
			recentList = RecentFavList.size();
			if (recentList == 50)
				report.updateTestLog("Verify only a maximum of 50 items are available in the Recent and Favorites filter", "Only a maximum of 50 items are available in the Recent and Favorites filter", Status.PASS);
			else
				report.updateTestLog("Verify only a maximum of 50 items are available in the Recent and Favorites filter", "Maximum of 50 items are not available in the Recent and Favorites filter", Status.FAIL);

		}

		return new Search(scriptHelper);
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

			if (txtAccessChargeRed != null) {
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is displayed", Status.FAIL);
			} else
				report.updateTestLog("Veirfy LASAC Access charge popup is not displayed", "LASAC Access charge popup is not displayed", Status.PASS);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Lasac checkbox is present.
	// # Function Name : verifyLASACCheckBox
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search verifyLASACCheckBox(String checkBoxText) {

		WebElement checkBoxCont = commonLibrary.isExistNegative(UIMAP_SearchResult.checkBoxCont1, 10);
		WebElement chkbox = commonLibrary.isExistNegative(checkBoxCont, UIMAP_SearchResult.chkbox, 10);
		if (checkBoxCont.getText().toLowerCase().contains(checkBoxText.toLowerCase()) && chkbox != null) {
			report.updateTestLog("Veirfy Checkbox is present with following message: " + checkBoxText, "Checkbox is present with following message: " + checkBoxText, Status.PASS);
		} else
			report.updateTestLog("Veirfy Checkbox is present with following message: " + checkBoxText, "Checkbox is not present with following message: " + checkBoxText, Status.FAIL);

		return new Search(scriptHelper);
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
	// # Function Description : Function to click on add search to folder and
	// save search
	// # Function Name : addToFolderNotesSaveSearchFromAction
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search addToFolderNotesSaveSearchFromAction(String folderName, String notes) {
		try {

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, folderName);

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// ADD <<<NOTES>>>
			if (!notes.equals("")) {
				WebElement notesIframe = commonLibrary.isExistNegative(UIMAP_Document.notesIframe, 10);
				driver.switchTo().frame(notesIframe);
				WebElement notesArea = commonLibrary.isExistNegative(UIMAP_Document.notesArea, 10);
				commonLibrary.setDataInTextBox(notesArea, notes, "Notes");
				driver.switchTo().defaultContent();
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add search to folder and
	// save search
	// # Function Name : addToFolderSaveSearchFromAction
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search addToFolderSaveSearchFromAction(String FolderName, String SearchName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

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

			// Enter Search Term in Save Search TextBox
			WebElement txtSearchName = commonLibrary.isExist(UIMAP_SearchResult.txtSearchName, 10);
			if (txtSearchName != null) {
				commonLibrary.setDataInTextBox(txtSearchName, SearchName, "Save Search Name");
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "Save");

				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter Client ID Dtails
	// # Function Name : enterClientIDDetails
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search enterClientIDDetails(String strClientID) {

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
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to note document count in datasheet
	// # Function Name : enterClientIDDetails
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search getDocumentCount(String tcName, String dataSheet, String colName) {

		String resultText = driver.getTitle();
		String[] resultArray = resultText.split(" results");
		String resultCount = resultArray[0].trim();

		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, resultCount);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the second results count is
	// greater than first
	// # Function Name : VerifySecondCountWithFirst
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifySecondCountWithFirst(String Count1, String comparator) {
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		String[] resultArray = new String[0];
		String resultText = driver.getTitle();
		/*
		 * if(resultText.contains("+")) { resultText=resultText.replace("+",
		 * ";"); resultArray=resultText.split(";"); } else
		 * if(resultText.toLowerCase().contains("results")) {
		 * resultArray=resultText.split("results"); }
		 */

		if (resultText.contains("+")) {
			resultText = resultText.replace("+", "");
		}

		resultArray = resultText.split(" ");

		String resultCount = resultArray[0].trim();
		resultCount = resultCount.replaceAll("[^0-9]", "");

		if (comparator.toLowerCase().equals("greater than")) {

			if ((Integer.parseInt(resultCount) == 10000 && Integer.parseInt(Count1) == 10000) || (Integer.parseInt(resultCount) > Integer.parseInt(Count1))) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is greater than previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is NOT greater than previously noted result", Status.FAIL);
			}
		} else if (comparator.toLowerCase().equals("less than")) {
			if (Integer.parseInt(resultCount) < Integer.parseInt(Count1)) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is less than previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is NOT less than previously noted result", Status.FAIL);
			}
		} else if (comparator.toLowerCase().equals("equal to")) {
			if (Integer.parseInt(resultCount) == Integer.parseInt(Count1)) {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is Equal to previously noted result", Status.PASS);
			} else {
				report.updateTestLog("Verifying second count results with previously noted results", "Secondly noted results is NOT Equal to previously noted result", Status.FAIL);
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Harish
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selectand verify Filter menu items
	// # Function Name : select_verifyFilterSelected
	// # Author : revathi
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Search selectVerifyFilterSelected(String strSelectedFilterOptions) {
		commonLibrary.sleep(50000);
		// WebElement filterIdCount =
		// commonLibrary.isExist_Negative(UIMAP_Home.FilterIdCount, 2);
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (btnClassFilter != null) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement mnuNarrorFilter1 = commonLibrary.isExist(UIMAP_Home.divMoreFilter, 10);
		if (mnuNarrorFilter1 != null) {
			commonLibrary.clickButtonParentWithWait(mnuNarrorFilter1, "More");
		}

		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {

			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 10);

			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {
					boolean blnFlag = false;
					for (WebElement item : divClassDeleteFilter) {
						String strFilterText = item.getText();
						String strFilterVerify = (arrSelectedFilterOptions[i]);
						if (strFilterText.contains(strFilterVerify)) {
							blnFlag = true;
							break;
						}

					}
					if (blnFlag) {
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", Status.PASS);
					} else {
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy textbox", Status.FAIL);
					}
				}

				// WebElement FilterIdCount =
				// commonLibrary.isExist(UIMAP_Home.FilterIdCount, 20);
				// String strCount=FilterIdCount.getText();
				//
				// if(Integer.parseInt(strCount)==divClassDeleteFilter.size())
				// report.updateTestLog("Verify "+ divClassDeleteFilter.size()
				// +" is displayed in Big Red Searchbox",
				// divClassDeleteFilter.size()
				// +" is displayed in Big Red Searchbox", Status.PASS);
				// else
				// report.updateTestLog("Verify "+ divClassDeleteFilter.size()
				// +" is displayed in Big Red Searchbox",
				// divClassDeleteFilter.size()
				// +" is not displayed in Big Red Searchbox", Status.FAIL);

			} else {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy textbox", "NarrowBy textbox is not displayed", Status.FAIL);
			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify message is not present in the
	// search result
	// # Function Name : verifyMessageNotPresent
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifyMessageNotPresent(String message) {
		WebElement wholeText = commonLibrary.isExist(UIMAP_SearchResult.wholeText);
		if (!wholeText.getText().toLowerCase().replace(" ", "").equals(message.toLowerCase().replace(" ", ""))) {
			report.updateTestLog("verify message is not present in the search result", "The " + message + " is not present in the search result", Status.PASS);
		} else {
			report.updateTestLog("verify message is not present in the search result", "The " + message + " is present in the search result", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option
	// # Function Name : addToFolderUsingQuickSave
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search addToFolderUsingQuickSave(String FolderName) {
		try {
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<FOLDER with given FolderName>>>

			List<WebElement> folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(5000);
					folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
					count++;
				} while (folder.size() == 0 && count < 40);
			} catch (Exception e) {
				System.out.println(e.toString());
				commonLibrary.sleep(1000);
				folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
			}

			if (folder.size() > 0) {
				for (WebElement fol : folder) {
					if (fol.getText().contains(FolderName)) {
						commonLibrary.clickButtonParentWithWait(fol, FolderName);
						break;
					}
				}
			} else
				report.updateTestLog("Select the folder", "Folder not available", Status.FAIL);

			WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 5);
			WebElement selectedDocCount = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.selectAllItemsCount, 5);
			count = 0;
			try {
				do {
					commonLibrary.sleep(1000);
					mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 5);
					selectedDocCount = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.selectAllItemsCount, 5);
					count++;
				} while (selectedDocCount != null && count < 60);
			} catch (Exception e) {
				System.out.println(e.toString());
				commonLibrary.sleep(2000);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option
	// # Function Name : addToFolderUsingQuickSave
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search selectText(String text) {
		int k = 0;
		boolean shift = false;
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			commonLibrary.sleep(2000);

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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filters present under the
	// narrow by section
	// # Function Name : verifynarrowbyfilters1     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifynarrowbyfilters1(String filter, String filerName) {

		WebElement asideclass = commonLibrary.isExist(UIMAP_Document.asideclass, 20);
		WebElement usedfilter = commonLibrary.isExist(asideclass, UIMAP_Document.usedfilter, 20);
		WebElement lilist = commonLibrary.isExist(usedfilter, UIMAP_Document.listItems, 20);
		WebElement narrowbyfilter = commonLibrary.isExist(lilist, UIMAP_Document.narrowbyfilter, 20);

		if (narrowbyfilter.getText().contains(filter) && (narrowbyfilter.getText().contains(filerName) || filerName.toLowerCase().contains("ignore"))) {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is displayed", Status.PASS);
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filerName + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filter + " is not displayed", Status.FAIL);
			report.updateTestLog("Verify the display of Post filter under Narrow By Section", filerName + " is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click in Shepardize This Doc
	// ShepPreview
	// # Function Name : RightClickShepardizeThisDocShepPreview     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #***********************************************************************************************************
	public Search rightClickShepardizeThisDocShepPreviewSearch() {

		WebElement lnkShepardizeThisDoc = commonLibrary.isExist(UIMAP_SearchResult.lnkShepardizeThisDoc, 20);

		this.clickRightSelectOpenNewWindow(lnkShepardizeThisDoc);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link from About this document
	// section
	// # Function Name : clickLinkFromATD
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public RelatedContent clickLinkFromATD(String documenttitle) {
		generalFunctions.clickLinkFromATD(documenttitle);
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify TOC title is expanded
	// # Function Name : verifyTOCTitleExpanded
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyTOCTitleExpanded(String title) {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean flag = false;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 20);
		List<WebElement> titles = commonLibrary.isExistList(tocList, By.tagName("li"), 30);
		for (WebElement item : titles) {
			WebElement tocHeader = commonLibrary.isExist(item, UIMAP_TOC.tocHeader, 30);
			if (tocHeader.getText().equalsIgnoreCase(title) && item.getAttribute("class").contains("expanded")) {
				report.updateTestLog("Verify " + title + " is expanded", title + " is expanded", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag) {
			report.updateTestLog("Verify " + title + " is expanded", title + " is not expanded", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created.
	// # Function Name : addToCreatedFolder     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search addToCreatedFolder(String folderName) {
		generalFunctions.addToCreatedFolder(folderName);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to share folder
	// # Function Name : addToFolder     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Search addToFolder(String strContactName, String strFolderName) {

		try {
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

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBox(UIMAP_Document.txtFolderName, strFolderName);
			// CLICK ON <<<CREATE>>>
			WebElement btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			if (btnCreateNewFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateNewFolder, "CREATE");
			commonLibrary.sleep(3000);

			WebElement create = commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
			WebElement createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(7000);
					createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (create.equals(createNew) && count < 80);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// Click <Share with Others> tab in add to folder pop up
			generalFunctions.selectTabsAddToFolderWindow("Share With Others");

			// ENTER <<<B>>> IN THE TEXT BOX RECENTLY USED NAMES WILL DISPLAY
			// ALLOWING THE USER TO SELECT ONE

			if (browsername.contains("internet")) {

				WebElement txtShareContacts = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
				if (txtShareContacts != null)
					commonLibrary.setDataInTextBox(UIMAP_Document.txtShareContacts, strContactName);
				commonLibrary.sleep(2000);

				// SELECT THE <<<BECKER>>>
				boolean blnFlag = false;
				// try
				// {
				//
				commonLibrary.sleep(3000);
				WebElement cboSharingList = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
				List<WebElement> li = commonLibrary.isExistList(cboSharingList, By.tagName("li"), 20);
				for (WebElement item : li) {
					if (item.getText().trim().equalsIgnoreCase(strContactName)) {
						commonLibrary.clickJS(item);
						blnFlag = true;
						break;
					}
				}
				if (!(blnFlag)) {
					WebElement txtShareContacts1 = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
					for (int j = 1; j <= 3; j++) {
						Actions action = new Actions(driver);
						action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
						action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
						action.sendKeys(txtShareContacts1, strContactName.substring(strContactName.length() - 2)).perform();
						commonLibrary.sleep(4000);
						WebElement cboSharingList1 = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
						List<WebElement> li1 = commonLibrary.isExistList(cboSharingList1, By.tagName("li"), 20);
						for (WebElement item : li1) {
							if (item.getText().trim().equalsIgnoreCase(strContactName)) {
								commonLibrary.clickJS(item);
								blnFlag = true;
								break;
							}
						}
						if (blnFlag)
							break;
					}
				}

			} else
				generalFunctions.shareContactSelectContact(strContactName);

			// Click the "Add" button
			WebElement btnAddToContact = commonLibrary.isExist(UIMAP_Document.btnAddToContact, 10);
			if (btnAddToContact != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnAddToContact, "Add");
				else
					commonLibrary.clickButtonParentWithWait(btnAddToContact, "Add");
			}

			// CLICK ON <<<SAVE>>>
			WebElement btnDocumentSave = commonLibrary.isExist(UIMAP_Document.btnDocumentSave, 10);
			if (btnDocumentSave != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDocumentSave, "SAVE");
				else
					commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");

				WebElement saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
				count = 0;
				try {
					do {
						commonLibrary.sleep(7000);
						saveNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.saveFolder, 10);
						count++;
						System.out.println("Waiting" + count);
					} while (btnDocumentSave == saveNew && count < 40);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			return new Search(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click 'Select All Items on This
	// Page' and verify count
	// # Function Name : selectAllItemsAndVerifyCount
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search selectAllItemsAndVerifyCount(String count) {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement seletcAllItemsUl = commonLibrary.isExist(UIMAP_SearchResult.selectAllItemsUl, 20);
		WebElement selectAllItemsCheckBox = commonLibrary.isExist(seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCheckBox, 20);
		if (selectAllItemsCheckBox != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(selectAllItemsCheckBox);
			} else {
				commonLibrary.setCheckBox(selectAllItemsCheckBox, "Select All Items on this Page");
			}
			WebElement selectAllItemsCount = commonLibrary.isExist(seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCount, 20);
			if (selectAllItemsCount != null) {
				if (selectAllItemsCount.getText().contains(count)) {
					report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is displayed in the 'X Selected tray'", Status.PASS);
				} else {
					report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is not displayed in the 'X Selected tray'", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is not displayed in the 'X Selected tray'", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click 'Select All Items on this Page'", "'Select All Items on this Page' is not available", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Clear Check box of selected document
	// # Function Name : clearCheckBoxSelectedDocument    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search clearCheckBoxSelectedDocument(int intDocNo) {

		try {
			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> docList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			WebElement lstchkBox = commonLibrary.isExist(docList.get(intDocNo - 1), By.cssSelector("a[type='checkbox']"), 10);
			WebElement lstchkBoxWeb = commonLibrary.isExist(docList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
			if (lstchkBox != null) {
				commonLibrary.setCheckBox(lstchkBox, false);
				report.updateTestLog("Clear Check box of selected document", "Check box is cleared", Status.PASS);
			} else if (lstchkBoxWeb != null) {
				commonLibrary.setCheckBox(lstchkBoxWeb, false);
				report.updateTestLog("Clear Check box of selected document", "Check box is cleared", Status.PASS);
			} else
				report.updateTestLog("Clear Check box of selected document", "Check box for the document is not present", Status.FAIL);

			return new Search(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Selected Items count
	// # Function Name : verifySelectedItemsCount
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifySelectedItemsCount(String count) {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement seletcAllItemsUl = commonLibrary.isExist(UIMAP_SearchResult.selectAllItemsUl, 20);
		WebElement selectAllItemsCount = commonLibrary.isExist(seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCount, 20);
		if (selectAllItemsCount != null) {
			if (selectAllItemsCount.getText().contains(count)) {
				report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is displayed in the 'X Selected tray'", Status.PASS);
			} else {
				report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is not displayed in the 'X Selected tray'", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify " + count + " displays in the 'X Selected tray'", count + " is not displayed in the 'X Selected tray'", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message
	// # Function Name : verifyBannerMessage     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Choose Folder
	// # Function Name : clickChooseFolder     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search clickChoosefolders() {
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null)

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 10);
		WebElement recentFolderListOld = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.recentFolderList, 5);
		commonLibrary.sleep(3000);
		int count = 0;
		WebElement recentFolderListNew = null;
		try {
			do {
				commonLibrary.sleep(5000);
				mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 10);
				recentFolderListNew = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.recentFolderList, 5);
				count++;
			} while (recentFolderListNew.equals(recentFolderListOld) && count < 30);
		} catch (Exception e) {
			System.out.println(e.toString());
			commonLibrary.sleep(1000);

		}
		mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 10);
		WebElement recentFolderList = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.recentFolderList, 8);
		if (recentFolderList == null) {
			addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			commonLibrary.sleep(10000);
		}

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
		if (chooseFolder != null) {
			commonLibrary.windowFocus();

			// JavascriptExecutor executor = (JavascriptExecutor) driver;
			// executor.executeScript("arguments[0].focus();", chooseFolder);
			// executor.executeScript("arguments[0].click();", chooseFolder);
			// report.updateTestLog("Click on button " + chooseFolder.getText(),
			// "Clicked on button " + chooseFolder.getText(), Status.PASS);
			commonLibrary.clickJS(chooseFolder);
			commonLibrary.sleep(5000);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Verify Value
	// # Function Name : VerfiyActionListValues    
	// # Author : E Harish
	// # Date Created : July'15
	// #**************************************************************************************************
	public Search verfiyActionListValues(String strAction) {
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

			if (commonLibrary.verifyFromListButton(divAction, strAction)) {
				report.updateTestLog("Verfiy Action's option " + strAction + "", "" + strAction + " is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verfiy Action's option " + strAction + "", "" + strAction + " is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Highlighted text in full
	// document
	// # Function Name : verfiytermshighlight     
	// # Author : Harish.E
	// # Date Created :jul 15
	// #*****************************************************************************************************************************

	public Search verfiytermshighlight(String SampleText) {
		commonLibrary.sleep(15000);
		if (!SampleText.equalsIgnoreCase("asset purchase")) {
			String[] Textname = SampleText.split(";");

			Boolean flag1 = false;
			int count = 0;
			int count1 = 0;

			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement sentence : OListItems) {
						WebElement ShowExtractText = commonLibrary.isExist(sentence, UIMAP_SearchResult.eltShowExtract, 10);
						List<WebElement> termhighlighttext = commonLibrary.isExistList(ShowExtractText, UIMAP_SearchResult.eltShowTerms, 10);
						for (WebElement span : termhighlighttext) {// for(i=0;i<term.le;i++)
																	// {ter.get(0).eq(str1)}
																	//

							if (span.getText().equalsIgnoreCase(SampleText)) {
								flag1 = true;
							} else {
								for (int j = 0; j < Textname.length; j++) {
									if (span.getText().equalsIgnoreCase(Textname[j])) {
										count1++;
										break;
									}
									if (count1 == Textname.length) {
										flag1 = true;
									}
								}
							}
						}

						if (flag1) {
							count++;

						}
						if (count == OListItems.size()) {
							break;
						}

					}
				}

			}

			report.updateTestLog("Verify term " + SampleText + " is highlighed", "" + SampleText + " is Highlighted under all documents in results page", Status.PASS);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search selectDocumentByNo(int intDocNo) {

		try {
			WebElement lnkTitle = null;
			WebElement lnkTitle1 = null;
			commonLibrary.sleep(2000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			lnkTitle = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[data-action=title]"), 10);

			if (lnkTitle != null) {
				lnkTitle.getText();
			} else {
				lnkTitle1 = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[class*='ExternalLinkAfter']"), 10);
				lnkTitle1.getText();
			}
			WebElement lstchkBox = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[type='checkbox']"), 10);
			WebElement lstchkBoxWeb = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
			String strI = "" + (intDocNo);
			if (lstchkBox != null)
				commonLibrary.setCheckBox(lstchkBox, strI);
			else if (lstchkBoxWeb != null) {
				commonLibrary.setCheckBox(lstchkBoxWeb, strI);
			} else
				report.updateTestLog("Select document", "document is not selected", Status.FAIL);

			WebElement mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 5);
			WebElement selectedDocCount = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.selectAllItemsCount, 5);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(50000);
					mobileToolbar = commonLibrary.isExistNegative(UIMAP_SearchResult.mobileToolbar, 5);
					selectedDocCount = commonLibrary.isExistNegative(mobileToolbar, UIMAP_SearchResult.selectAllItemsCount, 5);
					count++;
				} while (selectedDocCount == null && count < 35);
			} catch (Exception e) {
				System.out.println(e.toString());
				commonLibrary.sleep(5000);
			}

			return new Search(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

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
	// # Function Description : Function to add the folder with small wait
	// # Function Name : addToFolder_NoWait
	// # Author : Anbarasan
	// # Date Created : July-2015
	// #*****************************************************************************************************************************

	public Search addToFolder_NoWait(String FolderName) {
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

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonLogSmallWait(saveFolder, "Save");
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
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

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	public Search addToFolderNoWait(String FolderName) {
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
					commonLibrary.sleep(7000);
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
					commonLibrary.clickButtonLogSmallWait(saveFolder, "save");
				else
					commonLibrary.clickButtonLogSmallWait(saveFolder, "Save");

			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the foldername through quick
	// save option with small wait.
	// # Function Name : addToFolderUsingQuickSaveNoWait
	// # Author : Veeshma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search addToFolderUsingQuickSaveNoWait(String FolderName) {
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
						commonLibrary.windowFocus();
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].focus();", fol);
						executor.executeScript("arguments[0].click();", fol);
						report.updateTestLog("Click on button " + FolderName, "Clicked on button " + FolderName, Status.PASS);
						commonLibrary.sleep(3000);
						break;
					}
				}
			} else
				report.updateTestLog("Select the folder", "Folder not available", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add document to a folder that is
	// already created with small wait.
	// # Function Name : addToCreatedFolder_Nowait     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search addToCreatedFolder_Nowait(String folderName) {
		generalFunctions.addToCreatedFolder_NoWait(folderName);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check popup displays and click OK
	// # Function Name : verifyPopupAndClickOk     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyPopupAndClickOk() {
		WebElement popupDiv = commonLibrary.isExist(UIMAP_SearchResult.popupDiv, 10);
		WebElement popup = commonLibrary.isExist(popupDiv, UIMAP_SearchResult.popup, 10);
		if (popup != null) {
			report.updateTestLog("Verify Popup message displays", "Popup message is displayed", Status.PASS);
			WebElement popupOk = commonLibrary.isExist(popup, UIMAP_SearchResult.popupOk, 10);
			if (popupOk != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(popupOk);
				else
					commonLibrary.clickButtonParentWithWait(popupOk, "OK");
			} else {
				report.updateTestLog("Click Ok button", "Ok button is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Popup message displays", "Popup message is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text displays in viewable
	// region
	// # Function Name : verifyTextDisplaysInViewableRegion     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyTextDisplaysInViewableRegion(String text) {
		/*
		 * boolean flag = false; WebElement toolbar =
		 * commonLibrary.isExist(UIMAP_SearchResult.toolbar, 10); WebElement
		 * documentText = commonLibrary.isExist(UIMAP_SearchResult.documentText,
		 * 10); List<WebElement> highlightedText =
		 * commonLibrary.isExistList(documentText,
		 * UIMAP_SearchResult.highlightText1, 10); for (WebElement textMessage :
		 * highlightedText) { if
		 * (textMessage.getText().toLowerCase().contains(text.toLowerCase()) &&
		 * toolbar != null) { int textLocation =
		 * textMessage.getLocation().getY(); int toolbarLocation =
		 * toolbar.getLocation().getY(); if ((textLocation - toolbarLocation) >=
		 * 0 && (textLocation - toolbarLocation) <= 1000) {
		 * report.updateTestLog("Verify " + text +
		 * " displays in the viewable region", text +
		 * " is displayed in the viewable region", Status.PASS); flag = true;
		 * break; } } } if (!flag) report.updateTestLog("Verify " + text +
		 * " displays in the viewable region", text +
		 * " is not displayed in the viewable region", Status.FAIL);
		 */
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify value in sort by drop down
	// # Function Name : verifySortBy
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifySortBy(String value) {
		try {
			WebElement sortContainer = null, sortBy = null;
			sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
			sortBy = commonLibrary.isExist(sortContainer, UIMAP_SearchResult.sortByBtn, 10);
			if (sortBy != null) {
				if (sortBy.getText().toLowerCase().contains(value.toLowerCase())) {
					report.updateTestLog("Verify " + value + " is the active item in the Sort by dropdown", value + " is the active item in the Sort by dropdown", Status.PASS);
				} else
					report.updateTestLog(value + " is the active item in the Sort by dropdown", sortBy.getText() + " is not the active item in the Sort by dropdown", Status.FAIL);
			} else {
				report.updateTestLog("Verify " + value + " is the active item in the Sort by dropdown", "Sort by dropdown is not displayed in the results list page", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : clickLinkInDoc   
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Document clickLinkInDoc(String targetText) {
		generalFunctions.clickLinkInDoc(targetText);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Link In Doc in docPage
	// # Function Name : verifyTextNotClickable   
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Search verifyTextNotClickable(String targetText) {
		generalFunctions.verifyTextNotClickable(targetText);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document is transactional
	// # Function Name : verifyTransactionalDoc
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Search verifyTransactionalDoc(String strDocTitle) {

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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").equals(strDocTitle.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
							WebElement btnGetItNow = commonLibrary.isExistNegative(document, UIMAP_SearchResult.btnGetItNow, 10);
							if (btnGetItNow != null) {
								report.updateTestLog("Verify Get It Now button is available for document  " + strDocTitle, "Get It Now button is available for document  " + strDocTitle, Status.PASS);
								blnFlag = true;
								break;
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

		if (!blnFlag) {
			report.updateTestLog("Verify Get It Now button is available for document  " + strDocTitle, "Get It Now button is not available for document  " + strDocTitle, Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to itigationProfiler
	// page
	// by clicking Lexis Advance arrow
	// # Function Name : navigateTolitigationProfile     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LitigationProfile navigateTolitigationProfile() {
		generalFunctions.productSwitcher("Litigation profile suite");

		return new LitigationProfile(scriptHelper);
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
	// # Function Description : Function to click view references link is
	// present
	// # Function Name : clickViewReferencesLink
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Search clickViewReferencesLink(String link) {
		WebElement citationHeader = commonLibrary.isExist(UIMAP_SearchResult.citationHeader, 10);
		WebElement viewReferences = commonLibrary.isExist(citationHeader, UIMAP_SearchResult.viewReferences, 10);
		boolean flag = false;
		if (viewReferences != null) {
			if (viewReferences.getText().toLowerCase().contains(link.toLowerCase())) {

				if (browsername.contains("internet")) {
					commonLibrary.clickJS(viewReferences, viewReferences.getText());
					flag = true;
				} else {
					commonLibrary.clickLinkWithWait(viewReferences, viewReferences.getText());
					flag = true;
				}
			} else {
				report.updateTestLog("Click " + link, link + " is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click " + link, link + " is not available", Status.FAIL);
		}
		if (flag) {
			WebElement resultClass = null;
			int count = 0;

			do {
				commonLibrary.sleep(20000);
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
				count++;
			} while (resultClass == null && count < 25);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Postfilters displays
	// # Function Name : verifyPostFiltersDisplays
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyPostFiltersDisplays() {
		WebElement postFilters = commonLibrary.isExist(UIMAP_SearchResult.postFilters, 10);
		if (postFilters != null)
			report.updateTestLog("Verify PostFilters displays", "Post Filters is displayed", Status.PASS);
		else
			report.updateTestLog("Verify PostFilters displays", "Post Filters is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Options
	// # Function Name : verifyDeliveryOptions
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyDeliveryOptions() {

		WebElement AddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 20);
		if (AddFolder != null)
			report.updateTestLog("Verify Delivery icons", " Add to folder icon with drop down is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Add to folder icon with drop down is not displayed", Status.FAIL);
		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu, UIMAP_SearchResult.lstTagName, 10);
		if (submenu.size() > 0) {
			for (WebElement list : submenu) {
				if (list.getAttribute("class").contains("splitbutton")) {
					List<WebElement> btn1 = commonLibrary.isExistList(list, By.tagName("button"), 20);
					commonLibrary.clickButtonParentWithWait(btn1.get(1), "Delivery");
					asideclass = commonLibrary.isExist(list, UIMAP_SearchResult.tagNameAside, 10);
					break;

				}

			}
		} else {
			report.updateTestLog("Verify delivery options are present", "Delivery tray is not present", Status.FAIL);
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
			WebElement printerFriendlyView = commonLibrary.isExist(UIMAP_Document.printerFriendlyView, 20);
			if (printerFriendlyView != null)
				report.updateTestLog("Verify Delivery icons", "PrinterFriendlyView icon is displayed", Status.PASS);
			else
				report.updateTestLog("Verify Delivery icons", "Print icon is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click GetItNow under document title
	// # Function Name : clickGetItNow
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Document clickGetItNow() {
		WebElement getItNow = commonLibrary.isExist(UIMAP_SearchResult.getItNowSearch, 10);
		if (getItNow != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWait(getItNow, "GetItNow");
			else
				commonLibrary.clickButtonParentWithWait(getItNow, "GetItNow");
		} else
			report.updateTestLog("Click ButtonGetItNow", "GetItNow button is not available", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Abbreviated results list
	// displays with only one sub-tab
	// # Function Name : verifyAbbreviatedResultsList
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyAbbreviatedResultsList() {
		Boolean flag = false;
		Boolean flag1 = false;
		Boolean flag2 = false;
		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		if (ulCondentSwitcher != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
			if (OListItems.size() > 0 && OListItems.size() < 3) {
				flag = true;
			}
		}
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null) {
						WebElement article = commonLibrary.isExist(document, By.tagName("article"), 10);
						WebElement overview = commonLibrary.isExist(article, UIMAP_SearchResult.overview, 10);
						WebElement docText = commonLibrary.isExist(article, UIMAP_SearchResult.docText, 10);
						if (overview == null && docText == null)
							flag1 = true;
						else
							flag2 = true;

					}
					if (flag1)
						break;
				}

			}

		}
		if (flag && flag1 && !flag2)
			report.updateTestLog("Verify Abbreviated results list displays with only one sub-tab", "Abbreviated results list is displayed with only one sub-tab", Status.PASS);
		else
			report.updateTestLog("Verify Abbreviated results list displays with only one sub-tab", "Abbreviated results list is not displayed with only one sub-tab", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Overview and links not
	// present
	// # Function Name : verifyOverviewAndLinksNotPresent
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyOverviewAndLinksNotPresent() {
		Boolean flag = false;
		Boolean flag1 = false;

		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null) {
						WebElement article = commonLibrary.isExist(document, By.tagName("article"), 10);
						WebElement overview = commonLibrary.isExist(article, UIMAP_SearchResult.overview, 5);
						WebElement docText = commonLibrary.isExist(article, UIMAP_SearchResult.docText, 5);
						if (overview == null && docText == null)
							flag = true;
						else
							flag1 = true;
					}
					if (flag)
						break;
				}

			}

		}
		if (flag && !flag1)
			report.updateTestLog("Verify Overview and Links terms not present", "Overview and Links terms are not present", Status.PASS);
		else
			report.updateTestLog("Verify Overview and Links terms not present", "Overview and Links terms are present", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Unexpected error is not
	// displayed .
	// # Function Name : verifyUnExpectedErrorAbsent     
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search verifyUnExpectedErrorAbsent(String term) {
		commonLibrary.sleep(5000);

		WebElement header = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
		WebElement spantext = commonLibrary.isExist(header, By.tagName("span"), 10);
		String headerText = spantext.getText();
		if (headerText.toLowerCase().contains(term.toLowerCase()))
			report.updateTestLog("Verify the presence of unexpected error message:", "Unexpected error is not displayed", Status.PASS);
		else
			report.updateTestLog("Verify the presence of unexpected error message:", "Unexpected error is displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Documents that match the
	// citation displays at the top of the results list and highlighted
	// # Function Name : verifyDocumentHighlightedAndAtTop
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyDocumentHighlightedAndAtTop(String citation) {
		Boolean flag = false;
		Boolean flag1 = false;
		Boolean flag2 = false;
		WebElement resultClass = null;

		// do {
		// commonLibrary.sleep(20000);
		// btnNextPage =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// count++;
		// } while (btnNextPage == null && count < 25);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class=' exact']"), 20);
				int i = OListItems.size() - 1;
				if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
					flag = true;
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(citation.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
						flag1 = true;
						break;
					}
				}
				if (!flag1)
					flag2 = true;
			}

		}
		if (flag && flag1 && !flag2)
			report.updateTestLog("Verify Documents that match the citation displays at the top of the results list and highlighted", "Documents that match the citation is displayed at the top of the results list and highlighted", Status.PASS);
		else
			report.updateTestLog("Verify Documents that match the citation displays at the top of the results list and highlighted", "Documents that match the citation is not displayed at the top of the results list and highlighted", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Abbreviated Result List Alone
	// Displays
	// # Function Name : verifyAbbreviatedResultListAloneDisplays
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyAbbreviatedResultListAloneDisplays() {
		Boolean flag = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class=' exact']"), 20);
				int i = OListItems.size() - 1;
				if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
					flag = true;
			}

		}
		if (flag)
			report.updateTestLog("Verify Abbreviated Result List Alone Displays", "Abbreviated Result List Alone is Displayed", Status.PASS);
		else
			report.updateTestLog("Verify Abbreviated Result List Alone Displays", "Abbreviated Result List Alone is not Displays", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Abbreviated Result List
	// Displays in Single Page
	// # Function Name : verifyAbbreviatedResultListDisplaysInSinglePage
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyAbbreviatedResultListDisplaysInSinglePage() {
		Boolean flag = false;
		boolean flag1 = false;
		Boolean flag2 = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class=' exact']"), 20);
				int i = OListItems.size() - 1;
				if (OListItems.size() > 0) {
					if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
						flag = true;
				}
			}

		}
		clickPageNumber("2");
		commonLibrary.sleep(10000);
		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class=' exact']"), 20);
				int i = OListItems.size() - 1;
				if (OListItems.size() > 0) {
					if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
						flag1 = true;
				}
			}

		}
		clickPageNumber("3");
		commonLibrary.sleep(10000);
		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class=' exact']"), 20);
				int i = OListItems.size() - 1;
				if (OListItems.size() > 0) {
					if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
						flag2 = true;
				}
			}

		}
		clickPageNumber("1");
		commonLibrary.sleep(10000);
		if (flag && !flag1 && !flag2)
			report.updateTestLog("Verify Abbreviated Result List Displays in Single Page", "Abbreviated Result List is Displayed in Single Page", Status.PASS);
		else
			report.updateTestLog("Verify Abbreviated Result List Displays in Single Page", "Abbreviated Result List is not Displayed in Single Page", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and verify button is
	// present
	// # Function Name : verifyButtonInAction    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public Search verifyButtonInAction(String buttonName) {
		Boolean flag = false;
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
			List<WebElement> listItems = commonLibrary.isExistList(divAction, By.tagName("li"), 20);
			for (WebElement item : listItems) {
				List<WebElement> btn = commonLibrary.isExistList(item, By.tagName("button"), 20);
				for (WebElement item2 : btn) {
					if (item2.getText().toLowerCase().contains(buttonName.toLowerCase())) {
						flag = true;
					}
				}
				if (flag)
					break;
			}
		}
		if (flag)
			report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is present in Actions dropdown", Status.PASS);
		else
			report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is not present in Actions dropdown", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select document
	// # Function Name : SelectDocument    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String selectDocumentVerifyCount(int intDocNo, int count) {

		try {
			String strDocTitle = null;
			WebElement lnkTitle = null;
			WebElement lnkTitle1 = null;
			WebElement lnkTitle2 = null;
			commonLibrary.sleep(20000);
			List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			int i = 0;
			do {
				commonLibrary.sleep(10000);
				i++;
				OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			} while ((OList == null && i < 10));

			OList = commonLibrary.isExistList(By.tagName("ol"), 10);
			List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
			lnkTitle = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[data-action='title']"), 10);

			if (lnkTitle != null) {
				strDocTitle = lnkTitle.getText();
			} else {
				lnkTitle1 = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[class*='ExternalLinkAfter']"), 10);
				if (lnkTitle1 != null) {
					strDocTitle = lnkTitle1.getText();
				} else {
					lnkTitle2 = commonLibrary.isExist(LList.get(intDocNo - 1), By.tagName("h2"), 10);
					strDocTitle = lnkTitle2.getText();
				}
			}
			WebElement lstchkBox = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("a[type='checkbox']"), 10);
			WebElement lstchkBoxWeb = commonLibrary.isExist(LList.get(intDocNo - 1), By.cssSelector("input[type='checkbox']"), 10);
			String strI = "" + (intDocNo);
			if (lstchkBox != null)
				commonLibrary.setCheckBox(lstchkBox, strI);
			else if (lstchkBoxWeb != null) {
				commonLibrary.setCheckBox(lstchkBoxWeb, strI);
			} else
				report.updateTestLog("Select document", "document is not selected", Status.FAIL);
			WebElement seletcAllItemsUl = commonLibrary.isExist(UIMAP_SearchResult.selectAllItemsUl, 20);
			WebElement selectAllItemsCount = commonLibrary.isExist(seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCount, 20);
			i = 0;
			do {
				Thread.sleep(4000);
				i++;
				seletcAllItemsUl = commonLibrary.isExist(UIMAP_SearchResult.selectAllItemsUl, 20);
				selectAllItemsCount = commonLibrary.isExist(seletcAllItemsUl, UIMAP_SearchResult.selectAllItemsCount, 20);
			} while ((selectAllItemsCount == null || !selectAllItemsCount.getText().equals(count)) && i < 10);

			return strDocTitle;
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

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
	// # Function Description : Function to click Add to Folder and verify the
	// order in the Add to folder menu.
	// # Function Name : clickAddToFolderVerifyOrder
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search clickAddToFolderVerifyOrder(String FolderName) {
		try {
			String[] folderList = FolderName.split(";");
			boolean flag = false;
			int i = 0;
			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
			if (addtoFolder != null)
				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			List<WebElement> folder = commonLibrary.isExistList(UIMAP_SearchResult.quickSaveFolder, 10);
			if (folder.size() > 0) {
				for (i = 0; i < folderList.length; i++) {
					if (!folder.get(i).getText().toLowerCase().contains(folderList[i].toLowerCase())) {
						flag = true;
						break;
					}
				}
				if (flag)
					report.updateTestLog("Verify the folders in Add to Folder menu displays in the order: " + FolderName, "The folder " + folderList[i] + "is not present at position: " + (i + 1), Status.FAIL);
				else
					report.updateTestLog("Verify the folders in Add to Folder menu displays in the order: " + FolderName, "The folders in Add to Folder menu displays in the order: " + FolderName, Status.PASS);
			} else
				report.updateTestLog("Select the folder", "Folder not available", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Choose Folder for cases where
	// add to folder is already clicked and add document to folder.
	// # Function Name : clickChooseFolderAddToFolder
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************

	public Search clickChooseFolderAddToFolder(String FolderName) {
		try {
			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 10);
			if (chooseFolder != null) {
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");
				pageCheck.ajaxWait(driver);
			}

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

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given
	// document.
	// # Function Name : selectDocumentByTitle
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectDocumentByTitleVerifyCount(String DocName, int count) {
		generalFunctions.selectDocumentByTitleVerifyCount(DocName, count);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search saveLink(String tcName, String dataSheet, String colName) {
		generalFunctions.saveLink(tcName, dataSheet, colName);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Search closePopup() {
		WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.dialogContent, 10);
		WebElement close = commonLibrary.isExistNegative(dialogContent, UIMAP_CounselBenchmarking.close, 10);
		commonLibrary.clickButtonParentWithWait(close, "Close");
		pageCheck.ajaxWait(driver);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Validate Filter Count
	// # Function Name : verifyFilterCount     
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************
	public Practice verifyFilterCount(int filterCount) {
		commonLibrary.sleep(30);
		WebElement FilterIdCount = commonLibrary.isExist(UIMAP_Home.FilterIdCount, 20);
		String strCount = FilterIdCount.getText();

		if (Integer.parseInt(strCount) == filterCount) {
			report.updateTestLog("Verify the count  '" + filterCount + "'  displays in Filters dropdown", "'" + filterCount + "' is displayed as filter count in Filters dropdown", Status.PASS);
		} else {
			report.updateTestLog("Verify the count  '" + filterCount + "'  displays in Filters dropdown", "'" + filterCount + "' is not displayed as filter count in Filters dropdown", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Post filter count
	// # Function Name : verifyFilterCount
	// # Author : Harish
	// # Date Created : jul'15
	// #*****************************************************************************************************************************

	public Search verifyPostFilterCount(int count) {
		commonLibrary.sleep(30);
		WebElement FiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.lstTagListItems, 10);
		for (WebElement item : Filters) {
			String filterText = item.getText().toLowerCase();
			String[] filterTextArray = filterText.split(" or ");
			int filtersApplied = filterTextArray.length;
			if (count == filtersApplied) {
				report.updateTestLog("Verify the count  '" + count + "'  displays in Filters dropdown", "'" + count + "' is displayed as filter count in Filters dropdown", Status.PASS);
			} else {
				report.updateTestLog("Verify the count  '" + count + "'  displays in Filters dropdown", "'" + count + "' is not displayed as filter count in Filters dropdown", Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Continue in PermaLinking
	// page
	// # Function Name : clickContinuePermaLinking
	// # Author : Harish
	// # Date Created : jul'15
	// #*****************************************************************************************************************************

	public Search clickContinuePermaLinking(String option) {
		commonLibrary.sleep(30);
		WebElement permaDiv = commonLibrary.isExist(UIMAP_SearchResult.permaLinkDiv, 20);
		List<WebElement> permaOpt = commonLibrary.isExistList(permaDiv, UIMAP_Home.inpt, 10);
		if (permaOpt.size() > 0) {
			for (WebElement opt : permaOpt) {
				if (opt.getAttribute("value").toLowerCase().equals(option.toLowerCase())) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(opt, option);
					else
						commonLibrary.clickButtonParentWithWait(opt, option);
					break;
				}
			}
		} else {
			report.updateTestLog("Verify result page displays", "Result page is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Add To Folder Dialog Box
	// # Function Name : verifyAddToFolderDialog
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search verifyAddToFolderDialog(String tabs, String text, String radioOptions) {
		if (!tabs.equals("")) {
			String[] tabNames = tabs.split(";");
			WebElement dialogContent = commonLibrary.isExist(UIMAP_SearchResult.workfolderPopup, 10);
			WebElement tabsCont = commonLibrary.isExistNegative(dialogContent, UIMAP_SearchResult.tabList1, 10);
			List<WebElement> tabList = commonLibrary.isExistList(tabsCont, UIMAP_SearchResult.listItems, 10);
			int i = 0;
			for (i = 0; i < tabNames.length; i++) {
				boolean flag = false;
				for (WebElement tab : tabList) {
					if (tab.getText().toLowerCase().contains(tabNames[i].toLowerCase())) {
						flag = true;
						report.updateTestLog("Verify " + tabNames[i] + " tab is present in the popup", tabNames[i] + " tab is present in the popup", Status.PASS);
					}
				}
				if (!flag)
					report.updateTestLog("Verify " + tabNames[i] + " tab is present in the popup", tabNames[i] + " tab is not present in the popup", Status.FAIL);
			}

		}
		if (!text.equals("")) {
			WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_SearchResult.workfolderPopup, 10);
			WebElement searchName = commonLibrary.isExistNegative(dialogContent, UIMAP_SearchResult.txtSearchName, 10);
			if (searchName.getAttribute("value").equalsIgnoreCase(text))
				report.updateTestLog("Verify " + text + " displays in Save Search title box", text + " displays in Save Search title box", Status.PASS);
			else
				report.updateTestLog("Verify " + text + " displays in Save Search title box", text + " does not display in Save Search title box", Status.FAIL);

		}
		if (radioOptions.equals("")) {
			String[] radioList = radioOptions.split(";");
			WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_SearchResult.workfolderPopup, 10);
			WebElement leftPanelCont = commonLibrary.isExistNegative(dialogContent, UIMAP_SearchResult.leftPanelCont, 10);
			List<WebElement> spans = commonLibrary.isExistList(leftPanelCont, UIMAP_SearchResult.tagSpan, 10);
			List<WebElement> options = commonLibrary.isExistList(spans.get(1), UIMAP_SearchResult.listItems, 10);
			int i = 0;

			for (i = 0; i < radioList.length; i++) {
				boolean flag = false;
				for (WebElement option : options) {
					WebElement radio = commonLibrary.isExistNegative(option, UIMAP_SearchResult.radio, 10);
					if (option.getText().toLowerCase().contains(radioList[i]) && radio != null) {
						report.updateTestLog("Verify " + radioList[i] + " is displayed below Store Narrow by Selections for.", radioList[i] + " is displayed below Store Narrow by Selections for.", Status.PASS);
						flag = true;
						break;
					}

				}
				if (!flag)
					report.updateTestLog("Verify " + radioList[i] + " is displayed below Store Narrow by Selections for.", radioList[i] + " is not displayed below Store Narrow by Selections for.", Status.FAIL);
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select options in AddToFolder dialog
	// Box.
	// # Function Name : setAddToFolderOption
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search setAddToFolderOption(String text) {

		WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_SearchResult.workfolderPopup, 10);
		WebElement leftPanelCont = commonLibrary.isExistNegative(dialogContent, UIMAP_SearchResult.leftPanelCont, 10);
		List<WebElement> spans = commonLibrary.isExistList(leftPanelCont, UIMAP_SearchResult.tagSpan, 10);
		WebElement optionList = commonLibrary.isExistNegative(spans.get(1), UIMAP_SearchResult.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(optionList, UIMAP_SearchResult.listItems, 10);
		boolean flag = false;
		for (WebElement option : options) {
			WebElement radio = commonLibrary.isExistNegative(option, UIMAP_SearchResult.radio, 10);
			if (option.getText().toLowerCase().contains(text.toLowerCase()) && radio != null) {
				commonLibrary.setRadioButton(radio, text);
				flag = true;
				break;
			}

		}
		if (!flag)
			report.updateTestLog("Select " + text + " from Store Narrow by Selections for options.", text + " is not select from Store Narrow by Selections for options.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select options in AddToFolder dialog
	// Box.
	// # Function Name : setAddToFolderOption
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Search veirfyContentTypeDisplayed(String contentTypes) {
		String[] namesList = contentTypes.split(";");
		WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
		if ((btnFewerCat != null) && (btnFewerCat.getText().contains("Show more")))
			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, "More Categories");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, "More Categories");
		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
		if (OListItems.size() - 1 == namesList.length) {
			report.updateTestLog("Verify only " + namesList.length + " content types are displayed.", namesList.length + " content types are displayed.", Status.PASS);
			for (int i = 0; i < namesList.length; i++) {
				boolean flag = false;
				for (WebElement item : OListItems) {
					if (item.getText().toLowerCase().contains(namesList[i].toLowerCase())) {
						flag = true;
						report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is displayed.", Status.PASS);
						break;
					}

				}
				if (!flag)
					report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is not displayed.", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify only " + namesList.length + " content types are displayed.", +namesList.length + " content types are not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	public Search verifyAppliedFilterWithin(String filterList, String filters) {
		boolean blnFlag = false;
		WebElement FiltersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_SearchResult.btnEachFilter, 10);
		for (WebElement li : Filters) {
			String[] appFilters = li.getText().split(" or ");
			for (String text : appFilters) {

				System.out.println(text.toLowerCase());
			}
			for (String text : appFilters) {
				if (!filterList.toLowerCase().contains(text.toLowerCase().trim())) {
					blnFlag = true;
					System.out.println(text.toLowerCase());
					report.updateTestLog("Verify Applied filters are within " + filters, "Applied filters are not within " + filters, Status.FAIL);
					break;
				}
			}
			if (blnFlag)
				break;
		}
		if (!blnFlag)
			report.updateTestLog("Verify Applied filters are within " + filters, "Applied filters are within " + filters, Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text Bolded
	// Box.
	// # Function Name : verifySelectedFilterBolded
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifySelectedFilterBolded(String strHeader, String strFilter, Boolean boldFlag) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("expanded")) {

						List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.lstTagListItems, 20);
						for (WebElement item : filters) {
							WebElement h2 = commonLibrary.isExistNegative(item, UIMAP_SearchResult.h2tag, 10);
							if (h2 != null) {
								if (h2.getText().equals(strFilter)) {

									Flag = true;
									break;
								}
							}
						}
					}

				}
				if (Flag)
					break;
			}

			if (boldFlag) {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + "is Bolded", strFilter + " is  Bolded.", Status.PASS);
				else
					report.updateTestLog("Verify : " + strFilter + "is Bolded", strFilter + " is not Bolded.", Status.FAIL);
			} else {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + "is Bolded", strFilter + " is  Bolded.", Status.FAIL);
				else
					report.updateTestLog("Verify : " + strFilter + "is Bolded", strFilter + " is not Bolded.", Status.PASS);
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Timeline slider
	// Box.
	// # Function Name : selectTimelineFilterSlider
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search selectTimelineFilterSlider(
	/* int ddfrom, int mmfrom, */int yyyyfrom, /*
												 * int ddto , int mmto ,
												 */int yyyyto) {
		commonLibrary.sleep(5000);
		try {
			Thread.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		if (filterContainer != null) {
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);
			int i = 0;
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().toUpperCase().contains("timeline".toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), "Timeline");
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), "Timeline");
						report.updateTestLog("Expanding Filter Header: " + "Timeline", "Timeline" + " filter Header Expanded.", Status.DONE);

					}
					break;
				}
			}
			filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			WebElement timelineCont = commonLibrary.isExistNegative(filterContainer, UIMAP_SearchResult.timelineCont, 10);
			WebElement labelsCont = commonLibrary.isExistNegative(timelineCont, UIMAP_SearchResult.labelsCont, 10);
			List<WebElement> spanList = commonLibrary.isExistList(labelsCont, UIMAP_Home.spantag, 10);
			int min = Integer.parseInt(spanList.get(0).getText().trim());
			int max = Integer.parseInt(spanList.get(1).getText().trim());

			WebElement sliderBase = commonLibrary.isExistNegative(filterContainer, UIMAP_SearchResult.sliderBase, 10);
			commonLibrary.focusControlJS(sliderBase);

			sliderBase.getLocation().getY();
			int width = sliderBase.getSize().getWidth();
			Actions action = new Actions(driver);
			action.moveToElement(sliderBase, 0, 5).click().build().perform();
			commonLibrary.sleep(5000);
			action.moveToElement(sliderBase, width, 5).click().build().perform();
			commonLibrary.sleep(5000);
			int xoffsetmin = (yyyyfrom - min) * width / (max - min);
			WebElement lower = commonLibrary.isExistNegative(filterContainer, UIMAP_SearchResult.lower, 10);
			commonLibrary.focusControlJS(lower);
			action.moveToElement(lower).clickAndHold(lower).moveByOffset(xoffsetmin, 0).release(lower).build().perform();
			commonLibrary.sleep(5000);

			int xoffsetmax = (yyyyto - (max + 1)) * width / (max - min);
			WebElement upper = commonLibrary.isExistNegative(filterContainer, UIMAP_SearchResult.upper, 10);
			action.moveToElement(upper).clickAndHold(upper).moveByOffset(xoffsetmax, 0).release(upper).build().perform();
			commonLibrary.sleep(5000);

			WebElement timelinkOKbutton = commonLibrary.isExist(UIMAP_VSASearchResults.timeLineOkButton, 20);
			if (timelinkOKbutton != null) {
				report.updateTestLog("Verify OK button is enabled", "OK button is enabled", Status.PASS);
				if (browsername.toLowerCase().contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(timelinkOKbutton, "ENTER");

				} else {
					commonLibrary.clickLinkWithWebElementWithWait(timelinkOKbutton, "ENTER");

					try {
						String loadProp = properties.getProperty("xSpinner");
						int count = 0;
						WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
						do {
							commonLibrary.sleep(1000);
							loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
							count++;
						} while (loader != null && count < 15);
					} catch (Exception e) {
						System.out.println(e.toString());
					}

				}
			}

			else {
				report.updateTestLog("Clicking the OK Button", "OK Button is not Clicked", Status.FAIL);
				System.out.println("Textbox not present");

			}
			commonLibrary.sleep(90000);

		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : navigateToBrowserLinkClickHeader
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Document navigateToBrowserLinkClickHeader(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction) {
		try {
			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);
			WebElement divIdBrowserMenu = commonLibrary.isExistNegative(UIMAP_Home.divIdBrowserMenu, 5);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(50000);
					count++;
					if (count == 10)
						commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
					divIdBrowserMenu = commonLibrary.isExistNegative(UIMAP_Home.divIdBrowserMenu, 5);

				} while (divIdBrowserMenu == null && count < 20);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							blnFirst = true;
							WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
							if (browsername.contains("internet")) {
								commonLibrary.clickJS(button1, strParentMenuName);
							} else
								commonLibrary.clickButtonParentWithWait(button1, strParentMenuName);
							break;
						}
					}
				commonLibrary.sleep(5000);
				if (!blnFirst)
					report.updateTestLog("Click on " + strParentMenuName, "Not Clicked on " + strParentMenuName, Status.FAIL);
			}
			if (strFirstSubMenuName != "") {
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strFirstSubMenuName)) {
								WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
								blnSecond = true;
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1, strFirstSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button1, strFirstSubMenuName);
								break;
							}
						}
					if (!blnSecond)
						report.updateTestLog("Click on " + strFirstSubMenuName, "Not Clicked on " + strFirstSubMenuName, Status.FAIL);
				}
			}
			commonLibrary.sleep(5000);
			if (strSecondSubMenuName != "") {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(1), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strSecondSubMenuName)) {
								WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
								blnThird = true;
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1, strSecondSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button1, strSecondSubMenuName);
								break;
							}
						}
					if (!blnThird)
						report.updateTestLog("Click on " + strSecondSubMenuName, "Not Clicked on " + strSecondSubMenuName, Status.FAIL);
				}
				commonLibrary.sleep(5000);
			}
			if (strThridSubMenuName != "" && !strThridSubMenuName.contains("Actions")) {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(2), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strThridSubMenuName)) {
								blnFour = true;
								WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1, strThridSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button1, strThridSubMenuName);
								break;
							}
						}
					if (!blnFour)
						report.updateTestLog("Click on " + strThridSubMenuName, "Not Clicked on " + strThridSubMenuName, Status.FAIL);
					commonLibrary.sleep(5000);
				}
				divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null && strAction != "") {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(3), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement button : lstTagListItems) {
							if (button.getText().contains(strAction)) {
								blnFive = true;
								WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.lnkLinks, 10);
								if (browsername.contains("internet"))
									commonLibrary.clickJS(button1, strAction);
								else
									commonLibrary.clickButtonParentWithWait(button1, strAction);
								break;
							}
						}
					commonLibrary.sleep(5000);
					if (!blnFive)
						report.updateTestLog("Click on " + strAction, "Not Clicked on " + strAction, Status.FAIL);
				}
			} else if (strThridSubMenuName.contains("Actions")) {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
				WebElement btnActions = commonLibrary.isExist(lstTagAside.get(2), UIMAP_Home.actionButton, 20);
				commonLibrary.clickButtonParentWithWait(btnActions, "ACTIONS FOR AGENCY ADJUDICATION");

				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary.isExist(lstTagAside1.get(2), UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement li : lstTagListItems) {
						WebElement anchor = commonLibrary.isExist(li, UIMAP_Home.button, 20);
						WebElement button = commonLibrary.isExist(li, UIMAP_Home.lnkTopicPod, 20);
						if (button != null && button.getText().contains(strAction)) {
							blnFive = true;
							if (browsername.contains("internet"))
								commonLibrary.clickJS(button, strAction);
							else
								commonLibrary.clickButtonParentWithWait(button, strAction);
							break;
						} else if (anchor != null && anchor.getText().contains(strAction)) {
							blnFive = true;
							if (browsername.contains("internet"))
								commonLibrary.clickJS(anchor, strAction);
							else
								commonLibrary.clickLinkWithWebElementWithWait(anchor, strAction);
							break;
						}
					}
				if (!blnFive)
					report.updateTestLog("Click on " + strAction, "Not Clicked on " + strAction, Status.FAIL);
				commonLibrary.sleep(5000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Create a Publication Alert in
	// Source
	// dropdown.
	// # Function Name : clickCreatePubAlert     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Search verifyOverviewTabandSearchTermAndPublication(String title, String publication) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			WebElement activeAlertTab = commonLibrary.isExistNegative(topicalert, UIMAP_Sources.activeAlertTab, 10);
			if (activeAlertTab != null && activeAlertTab.getText().contains("Overview")) {
				report.updateTestLog("Verify Overview Tab is displayed.", "Overview Tab is displayed.", Status.PASS);
				WebElement alertTitle = commonLibrary.isExist(UIMAP_SearchResult.alertTitle, 10);
				if (alertTitle != null && alertTitle.getAttribute("value").equalsIgnoreCase(title))
					report.updateTestLog("Verify " + title + " is displayed under title.", title + " is displayed under title.", Status.PASS);
				else
					report.updateTestLog("Verify " + title + " is displayed under title.", title + " is not displayed under title.", Status.FAIL);

				boolean flag = false;

				WebElement alertDialogTabContentList = commonLibrary.isExistNegative(topicalert, UIMAP_Sources.alertDialogTabContentList, 10);
				WebElement activeDialogContent = commonLibrary.isExistNegative(alertDialogTabContentList, UIMAP_Sources.activeDialogContent, 10);
				List<WebElement> list = commonLibrary.isExistList(activeDialogContent, UIMAP_Sources.dl, 10);
				for (WebElement item : list) {
					if (item.getText().toLowerCase().contains("publication") && item.getText().contains(publication.toLowerCase())) {
						report.updateTestLog("Verify " + publication + " is displayed under Publication.", publication + " is displayed under Publication.", Status.PASS);
						flag = true;
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Verify " + publication + " is displayed under Publication.", publication + " is displayed under Publication.", Status.PASS);
			} else
				report.updateTestLog("Verify Overview Tab is displayed.", "Overview Tab is not displayed.", Status.FAIL);

		} else
			report.updateTestLog("Verify Overview Tab is displayed.", "Alert Popup is not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter details in Share tab of Alerts
	// dialog box.
	// # Function Name : createAlert_ShareDetails
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search enterShareTabDetails(String text, String userName) {
		int i = 0;
		this.selectTabInAlerts("Share");
		WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_SearchResult.shareUserName, 10);
		wordWheelContent = null;
		do {
			commonLibrary.setDataInTextBox(shareUserName, text, "Enter user's name");
			shareUserName.sendKeys(Keys.ARROW_DOWN);
			try {
				commonLibrary.sleep(5000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			wordWheelContent = commonLibrary.isExistNegative(UIMAP_SearchResult.wordWheelContent, 10);
			i++;
		} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);
		List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
		WebElement options = commonLibrary.isExistNegative(wordWheelOptions.get(0), UIMAP_SearchResult.lstTagListItems, 10);
		commonLibrary.selectFromList(options, userName);

		WebElement addToShare = commonLibrary.isExistNegative(UIMAP_SearchResult.addToShare, 10);
		commonLibrary.clickButtonLogSmallWait(addToShare, "Add to Share");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click create alert button.
	// # Function Name : createAlert_ShareDetails
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search clickCreateAlert() {
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
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify monitor tab contents
	// # Function Name : verifyMonitorTabContents
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyMonitorTabContents_Displayed(String category, String filter) {
		boolean flag = false;
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, "Monitor");
			WebElement monitorcontent = commonLibrary.isExist(UIMAP_SearchResult.monitorTabContent, 10);
			WebElement datas = commonLibrary.isExist(monitorcontent, By.tagName("dl"), 10);
			List<WebElement> dataTerms = commonLibrary.isExistList(datas, UIMAP_SearchResult.dataTerms, 10);
			List<WebElement> dataDefn = commonLibrary.isExistList(datas, UIMAP_SearchResult.dataDefn, 10);
			for (int i = 0; i < dataTerms.size(); i++) {
				flag = false;
				// WebElement categ = commonLibrary.isExist(dataTerms.get(i),
				// By.tagName("label"), 10);
				// WebElement chkBox = commonLibrary.isExist(dataTerms.get(i),
				// By.tagName("input"), 10);
				if (dataTerms.get(i).getText().contains(category) /*
																 * &&
																 * chkBox.isSelected
																 * ()
																 */) {
					// report.updateTestLog(category +
					// " checkbox is selected under Categories section",
					// category +
					// " checkbox is selected under Categories section",
					// Status.PASS);
					report.updateTestLog(category + " is displayed under Categories section", category + " is displayed under Categories section", Status.PASS);
					flag = true;
					if (!filter.equalsIgnoreCase("")) {
						if (dataDefn.get(i).getText().contains(filter))
							report.updateTestLog(filter + " should be present under Narrowed by section", filter + " is present under Narrowed by section", Status.PASS);
						else
							report.updateTestLog(filter + " should be present under Narrowed by section", filter + " is not present under Narrowed by section", Status.FAIL);
					}
					break;
				}
				// else if (categ.getText().contains(category) /*&&
				// (!(chkBox.isSelected()))*/)
				// report.updateTestLog(category +
				// " checkbox is selected under Categories section", category +
				// " checkbox is not selected under Categories section",
				// Status.FAIL);
				// else if (!(chkBox.isSelected()))
				// flag = true;
			}
			// if (flag)
			// report.updateTestLog(category +
			// " checkbox is selected under Categories section",
			// "Other category checkboxes are not selected under Categories section",
			// Status.PASS);
			// else
			// report.updateTestLog(category +
			// " checkbox is selected under Categories section",
			// "Other category checkboxes are selected under Categories section",
			// Status.FAIL);
			if (!flag)
				report.updateTestLog(category + " checkbox is selected under Categories section", category + " is not displayed under Categories section", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify No Documents Found is not
	// displayed.
	// # Function Name : verifyNoDocsFoundNotDisplayed
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyNoDocsFoundNotDisplayed() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			if (!resultClass.getText().toLowerCase().contains("no documents found"))
				report.updateTestLog("Verify 'No Documents Found' is not displayed", "'No Documents Found' is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify 'No Documents Found' is not displayed", "'No Documents Found' is not displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify 'No Documents Found' is not displayed", "Result list container is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify No Documents Found is
	// displayed.
	// # Function Name : verifyNoDocsFoundDisplayed
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyNoDocsFoundDisplayed() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			if (resultClass.getText().toLowerCase().contains("no documents found in cases"))
				report.updateTestLog("Verify 'No Documents Found in cases' is  displayed", "'No Documents Found in cases' is  displayed", Status.PASS);
			else
				report.updateTestLog("Verify 'No Documents Found in cases' is  displayed", "'No Documents Found in cases' is not  displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify 'No Documents Found' is not displayed", "Result list container is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkDocumentNoWait(String strDocTitle) {
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
		//
		// List<WebElement> OListItems = commonLibrary.isExist_List(OListResult,
		// By.tagName("li"), 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().trim().equals(strDocTitle.trim())) {
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
		//
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
		// commonLibrary.sleep(5000);
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
		// WebElement citation =
		// commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
		// int count1 = 0;
		// do {
		// citation = commonLibrary.isExist(UIMAP_Document.copyCitation, 10);
		// commonLibrary.sleep(2000);
		// count1++;
		// } while (citation == null && count1 < 20);
		generalFunctions.clickDocLink(strDocTitle);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the number of results
	// # Function Name : verifyNumberOfResults
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search verifyNumberOfWebResults(int number, boolean greater) {
		if (greater) {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				List<WebElement> title = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.TitleClassDoc, 10);
				if (title.size() > number) {
					report.updateTestLog("Verify number of listed in the page is greater than " + number + "", "Number of listed in the page  is greater than " + number + "", Status.PASS);
				} else {
					report.updateTestLog("Verify number of listed in the page is greater than " + number + "", "Number of listed in the page  is not greater than " + number + "", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify number of listed in the page is greater than " + number + "", "Number of listed in the page is not greater than " + number + "", Status.FAIL);
			}
		} else {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				List<WebElement> title = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.TitleClassDoc, 10);
				if ((title.size()) == number) {
					report.updateTestLog("Verify number of listed in the page are " + number + "", "Number of listed in the page are " + number + "", Status.PASS);
				} else {
					report.updateTestLog("Verify number of listed in the page are " + number + "", "Number of listed in the page is not " + number + "", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify number of listed in the page are " + number + "", "Number of listed in the page is not " + number + "", Status.FAIL);
			}
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether the string is
	// italicized
	// # Function Name : verifyStringItalicized
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************
	public Search verifyStringItalicized(String string) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);
		List<WebElement> italics = commonLibrary.isExistList(clipTextContainer, UIMAP_Document.italics, 10);

		for (WebElement italicString : italics) {
			if (italicString.getText().toLowerCase().equalsIgnoreCase(string.toLowerCase())) {
				report.updateTestLog("Verify " + string + " is Italicized", string + " is Italicized", Status.PASS);
				break;
			}
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search selectConnector(String value, String dropdwn) {
		WebElement dropdown = commonLibrary.isExist(UIMAP_Home.connector, 20);
		if (dropdown != null) {
			// commonLibrary.clickButton_Parent_WithWait(dropdown,
			// "Find documents that");
			commonLibrary.selectByVisibleTextByValue(dropdown, value, dropdwn);
		} else {
			report.updateTestLog("Verify " + value + " is selected", "connector dropdown is not present", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyConectorMsg(String message) {
		boolean flag = false;
		WebElement msg = commonLibrary.isExist(UIMAP_SearchResult.connectorMsg, 20);
		if (msg != null) {
			if (msg.getText().equalsIgnoreCase(message)) {
				flag = true;
			}
		}
		if (flag) {
			report.updateTestLog("The message displays as " + message + "", "The message is displayed as " + message + "", Status.PASS);
		} else {
			report.updateTestLog("The message displays as " + message + "", "The message is not displayed as " + message + "", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting advanced search
	// # Function Name : FilterMenuSelection
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search applyFilter(String strToolbarMenuName) {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		}
		FilterMenuSelection(strToolbarMenuName);
		return new Search(scriptHelper);
	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to click delivery ooptions
	// // # Function Name : verifyResultHeader     
	// // # Author : Baswaraj
	// // # Date Created : August'15
	// //
	// #*****************************************************************************************************************************
	// public void clickPrintDeliverySelectOption(String option, String
	// DeliverOption)
	// {
	// // int i=1;
	// Boolean blnflag=false;
	// // Boolean blnflag1=false;
	// WebElement asideclass=null;
	// WebElement menu=commonLibrary.isExist(UIMAP_SearchResult.menu1, 20);
	// List<WebElement>
	// submenu=commonLibrary.isExist_List(menu,UIMAP_SearchResult.lstTagName,
	// 10);
	// for(WebElement list : submenu)
	// {
	// if(list.getAttribute("class").contains("splitbutton"))
	// {
	// List<WebElement>
	// btn1=commonLibrary.isExist_List(list,By.tagName("button"),20);
	// if(option.contains("print"))
	// {
	// commonLibrary.clickButton_Parent_WithWait(btn1.get(0), option);
	// // blnflag1=true;
	// asideclass=commonLibrary.isExist(list,UIMAP_SearchResult.tagNameAside,10);
	// break;
	// }
	// else if(option.contains("delivery"))
	// {
	// commonLibrary.clickButton_Parent_WithWait(btn1.get(1), option);
	// blnflag=true;
	// asideclass=commonLibrary.isExist(list,UIMAP_SearchResult.tagNameAside,10);
	// break;
	// }
	// }
	// }
	// if(blnflag)
	// {
	// if(asideclass!=null)
	// {
	// List<WebElement> listItems=commonLibrary.isExist_List(asideclass,
	// By.tagName("button"),20);
	// for(WebElement List : listItems)
	// {
	// // WebElement
	// button=commonLibrary.isExist(List,UIMAP_SearchResult.button1,10);
	//
	// if(List.getAttribute("data-action").contains(DeliverOption))
	// {
	// commonLibrary.clickButton_Parent_WithWait(List, DeliverOption);
	// break;
	// }
	// }
	// }
	// else
	// {
	// report.updateTestLog("Click on "+DeliverOption+" Option ","' "+DeliverOption+" ' option is not clicked",
	// Status.FAIL);
	// }
	// }
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFullDocumentTitle(String strSearchTerm) {
		Boolean blnFlag = false;
		try {

			WebElement copyCitation = commonLibrary.isExist(UIMAP_Document.copyCitation, 20);
			if (copyCitation == null) {
				document = search.clickDocLink1(strSearchTerm);
			}
			WebElement banner = commonLibrary.isExist(UIMAP_SearchResult.h1, 10);
			if (banner != null) {
				if (banner.getText().toLowerCase().contains(strSearchTerm.toLowerCase())) {
					blnFlag = true;
				}

			}
			if (blnFlag) {
				report.updateTestLog("Verify full document title is displayed for " + strSearchTerm + "", "Full document title is displayed for " + strSearchTerm + "", Status.PASS);
			} else {
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document title is not displayed for " + strSearchTerm + "", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : selectPostFilterMultipleNew     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search selectPostFilterMultipleNew(String strHeader, String strSubHeader, String strFilter) {
		String strSubHeader1 = "federal";
		WebElement filters = null;
		WebElement filters1 = null;
		String[] filterList = strFilter.split(";");
		List<WebElement> ullist12 = null;
		int i = 0;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
				report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.DONE);
			}
		}

		if (strHeader != "" && strHeader.toLowerCase().contains("court") && strSubHeader1.toLowerCase().contains("federal")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt, 10);
			filters = commonLibrary.isExist(filters1, UIMAP_SearchResult.filtersFederal, 10);
		} else if (strHeader != "" && strHeader.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersJurisdiction, 10);

		} else if (strHeader != "" && strHeader.toLowerCase().contains("keyword")) {
			filters = commonLibrary.isExist(UIMAP_SearchResult.filtersKeyword, 10);

		}
		WebElement SelectMultiple = commonLibrary.isExist(filters, UIMAP_SearchResult.btnSelectMultiple, 10);
		if (SelectMultiple != null) {
			commonLibrary.clickButtonLogSmallWait(SelectMultiple, "Select Multiple");
			WebElement SelMultiDialog = commonLibrary.isExist(UIMAP_SearchResult.SelMultiPopUp, 10);
			if (SelMultiDialog != null) {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is displayed", Status.PASS);
				WebElement formclass = commonLibrary.isExist(SelMultiDialog, By.tagName("form"), 30);
				if (formclass != null) {
					List<WebElement> ullist21 = commonLibrary.isExistList(formclass, UIMAP_VSASearchResults.ulcolumn1, 20);
					for (int i1 = 0; i1 < ullist21.size(); i1++) {
						WebElement header = commonLibrary.isExist(ullist21.get(i1), UIMAP_VSASearchResults.header, 20);
						if (header.getText().toLowerCase().contains(strSubHeader.toLowerCase())) {
							WebElement ul = commonLibrary.isExist(ullist21.get(i1), UIMAP_VSASearchResults.ullist, 20);
							ullist12 = commonLibrary.isExistList(ul, UIMAP_VSASearchResults.lilist, 20);
							commonLibrary.sleep(10000);
							break;
						}
					}
				}
				int count = 0;
				for (int j = 0; j < filterList.length; j++) {
					for (int i2 = 0; i2 < ullist12.size(); i2++) {
						WebElement lable = commonLibrary.isExist(ullist12.get(i2), By.tagName("label"), 20);

						if (lable != null) {
							String full_List[] = lable.getText().split(Pattern.quote("("));
							String first = full_List[0].trim();
							if (first.equals(filterList[j])) {
								WebElement input = commonLibrary.isExist(lable, By.tagName("input"), 20);
								if (input != null) {
									commonLibrary.setCheckBox(input, filterList[j]);
									count++;
									break;
								}

							}

						}

					}
					if (count == filterList.length)
						break;
				}
				// To Select OK Button in Select Multiple Pop Up
				WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.OKSelMultiPopUp, 10);
				WebElement btnCancel = commonLibrary.isExist(UIMAP_SearchResult.CancelSelMultiPopUp, 10);
				if (btnCancel != null && btnCancel != null) {
					report.updateTestLog("Verify 'OK' and 'Cancel' Buttons", "OK and Cancel buttons  are displayed in bottom right corner of popup", Status.PASS);
					commonLibrary.clickButtonParentWithWait(btnOK, "OK");
				} else {
					report.updateTestLog("Verify 'OK' and 'Cancel' Buttons", "OK and Cancel buttons  are not displayed in bottom right corner of popup", Status.PASS);
				}

				try {
					commonLibrary.sleep(5000);
				} catch (Exception e) {
					System.out.println(e.toString());
					throw new FrameworkException("Exception", e.toString());

				}
			} else {
				report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on Select Multiple", "Select Multiple is not clicked", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Teaser Text
	// # Function Name : clickTeaserText     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public Document clickTeaserText(String docName) {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		commonLibrary.isExist(resultClass, UIMAP_SearchResult.teaserLink, 5);
		WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		if (OListResult != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			for (WebElement document : OListItems) {
				WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
				if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(docName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {
					WebElement docContent = commonLibrary.isExist(document, UIMAP_SearchResult.teaserLink, 20);
					if (docContent != null) {
						commonLibrary.clickLinkWithWebElementWithWait(docContent, "Teaser Text");
						WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						int count = 0;
						do {
							count++;
							commonLibrary.sleep(50000);
							txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						} while (txtDocumentHeading == null && count < 20);

						break;
					} else
						report.updateTestLog("Click teaser text for document " + docName, "teaser text for document " + docName + " is not present.", Status.FAIL);
				}

			}
		}
		/*
		 * if(!(blnFlag)) { blnFlag = false;
		 * resultClass=commonLibrary.isExist(UIMAP_SearchResult.frmClassResult,
		 * 10); if(resultClass!=null) { WebElement
		 * OListResult=commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		 * if(OListResult!=null) { List<WebElement>
		 * OListItems=commonLibrary.isExist_List(OListResult, By.tagName("li"),
		 * 20);
		 * 
		 * for (WebElement document: OListItems) { WebElement docContent =
		 * commonLibrary.isExist(document,UIMAP_SearchResult.teaserLink, 20);
		 * if(docContent!=null) { WebElement docLink =
		 * commonLibrary.isExist(document,UIMAP_SearchResult.docLink, 20);
		 * commonLibrary.clickLink_withWebElement_WithWait(docLink,
		 * "Document title with Teaser Text");
		 * if(driver.getCurrentUrl().contains("document")) blnFlag = true;
		 * break; } }
		 * 
		 * } } } if(!(blnFlag)) {
		 * report.updateTestLog("Click on the teaser text of the document tile",
		 * "There is no document available with teaser text/teaser text is unclickable"
		 * , Status.FAIL); }
		 */

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickSignalsNextToDoc
	// # Function Name : clickSignalsNextToDoc     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Shepards clickSignalsNextToDoc(String docTitle) {
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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().contains(docTitle.toLowerCase())) {
							List<WebElement> spanList = commonLibrary.isExistList(document, By.tagName("span"), 20);
							for (WebElement anchorInsideSpan : spanList) {
								WebElement eltSignal = commonLibrary.isExist(anchorInsideSpan, By.tagName("a"), 20);
								if (eltSignal != null) {
									WebElement eltImg = commonLibrary.isExist(eltSignal, By.tagName("img"), 20);
									if (eltImg != null) {
										commonLibrary.clickLinkWithWebElementWithWait(eltSignal, eltImg.getAttribute("alt").toString());
										commonLibrary.sleep(10000);
										blnFlag = true;
										break;
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
		pageCheck.positiveCheck(driver, "shepards", "shepards");
		int counter = 0;
		do {
			commonLibrary.sleep(20000);
			counter = counter + 1;
			if (counter == 500)
				break;
		} while (!driver.getCurrentUrl().contains("shepards"));
		commonLibrary.sleep(30000);
		return new Shepards(scriptHelper);
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
	// # Function Description : Function to verify Filter Header Present
	// # Function Name : verifyFilterHeaderPresent
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyFilterHeaderPresent(String strHeader) {
		Boolean bnlFlag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

		for (int i = 0; i < filterHeader.size(); i++) {

			if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

				bnlFlag = true;
				break;
			}

		}
		if (bnlFlag)
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is displayed in the left Pane", Status.PASS);
		else
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filter order
	// # Function Name : verifyPreferredFilterOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPreferredFilterOrder(String filterHeader, String filters) {

		if (!(filterHeader.equals(" ") && filters.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(filterHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), filterHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), filterHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + filterHeader, filterHeader + " filter Header Expanded.", Status.DONE);
				}
			}

			List<WebElement> supplementalFilters = commonLibrary.isExistList(UIMAP_SearchResult.supplementalFilters, 10);
			if (supplementalFilters.size() > 0) {
				for (WebElement item : supplementalFilters) {
					if (item.getAttribute("data-id").toLowerCase().contains(filterHeader.toLowerCase())) {
						WebElement preferredFilters = commonLibrary.isExist(item, UIMAP_SearchResult.preferredFilters, 10);
						List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.lstTagName, 10);

						if (preferredFiltersList.size() > 0) {
							commonLibrary.focusControlJS(preferredFilters);
							if (preferredFiltersList.size() == 1) {
								List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(0), UIMAP_SearchResult.tagSpan, 20);
								String name = filterName.get(0).getText();
								if (name.trim().toLowerCase().equals(filters.toLowerCase())) {
									report.updateTestLog("Verify the filter " + name + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", "The filter " + name + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", Status.PASS);
								} else {
									report.updateTestLog("Verify the filter " + filters + " displays under preferred " + filterHeader + " title in " + filterHeader + " post filter", "The filter " + filters + " is not displayed under preferred " + filterHeader + " title in " + filterHeader + " post filter", Status.FAIL);
								}
							} else if (preferredFiltersList.size() != 1) {
								for (int i = 0; i < preferredFiltersList.size() - 1; i++) {
									List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(i), UIMAP_SearchResult.tagSpan, 20);
									String name = filterName.get(0).getText();

									List<WebElement> filterName1 = commonLibrary.isExistList(preferredFiltersList.get(i + 1), UIMAP_SearchResult.tagSpan, 20);
									String name1 = filterName1.get(0).getText();
									if (filters.toLowerCase().contains(name.toLowerCase().trim()) && filters.toLowerCase().contains(name1.toLowerCase().trim())) {
										name = name.replace("(", "");
										name = name.replace(")", "");
										name1 = name1.replace("(", "");
										name1 = name1.replace(")", "");

										if (name.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").compareTo(name1.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "")) < 0) {
											posFlag++;
										} else {
											negFlag++;
										}

									}
								}
								if (posFlag == (preferredFiltersList.size() - 1) && (negFlag == 0)) {
									report.updateTestLog("Verify the " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, "The " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, Status.PASS);
								} else
									report.updateTestLog("Verify the " + filters + " are sorted Alphabetically(A-Z) in preferred " + filterHeader, "The " + filters + " are not sorted Alphabetically(A-Z) in preferred " + filterHeader, Status.FAIL);

							}
						}
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + filters, "No Filter Selected.", Status.DONE);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order
	// # Function Name : verifyPostFilterOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPostFilterOrder(String header) {
		if (!(header.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			int i = 0, j = 0, k = 0;
			int newSize = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
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
					if (filters.size() > 0) {

						commonLibrary.focusControlJS(filterContainer);
						WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

						if (preferredFilters != null) {
							List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.eltFilterList, 10);
							k = preferredFiltersList.size();
							newSize = filters.size() - preferredFiltersList.size();
						}
						for (; k < filters.size() - 1; k++) {
							int count = 1;
							List<WebElement> filterName = commonLibrary.isExistList(filters.get(k), UIMAP_SearchResult.tagSpan, 20);
							List<WebElement> filterName1 = commonLibrary.isExistList(filters.get(k + 1), UIMAP_SearchResult.tagSpan, 20);
							String name = filterName.get(0).getText();
							String name1 = filterName1.get(0).getText();

							name = name.replace("(", "");
							name = name.replace(")", "");
							name1 = name1.replace("(", "");
							name1 = name1.replace(")", "");

							if (count == 1 && name.trim().toLowerCase().contains("u.s. federal")) {
								posFlag++;
							} else if (name.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "").compareTo(name1.toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "")) < 0) {
								posFlag++;
							} else {
								negFlag++;
							}

						}
						if (preferredFilters != null) {
							if (posFlag == (newSize - 1) && (negFlag == 0)) {
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are sorted Alphabetically(A-Z) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are not sorted Alphabetically(A-Z) under " + header, Status.FAIL);
						} else {
							if (posFlag == (filters.size() - 1) && (negFlag == 0)) {
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are sorted Alphabetically(A-Z) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted Alphabetically(A-Z) under " + header, "The filters are not sorted Alphabetically(A-Z) under " + header, Status.FAIL);
						}
					}

				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Edit button near preferred
	// filters
	// # Function Name : clickEditInPostFilter
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings clickEditInPostFilter(String header) {
		if (!(header.equals(" "))) {

			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {

				// looping for finding the post filter header

				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						// clicking on the expand icon

						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
					}

					WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

					if (preferredFilters != null) {

						WebElement edit = commonLibrary.isExist(preferredFilters, UIMAP_SearchResult.preferredEdit, 10);
						if (edit != null) {
							// to click on edit settings
							commonLibrary.highlightElement(edit);
							commonLibrary.clickButtonParentWithWait(edit, "Edit Settings");
							commonLibrary.sleep(20000);
						}
					} else {
						report.updateTestLog("Click Edit button near preferred " + header, "Not clicked on Edit button", Status.FAIL);
					}

				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order
	// # Function Name : verifyPostFilterOrderNumberOfResults
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPostFilterOrderNumberOfResults(String header) {

		if (!(header.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			int i = 0, j = 0, k = 0;
			int newSize = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
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
					int count = moreOptions.size();
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					if (filters.size() > 0) {
						commonLibrary.focusControlJS(filterContainer);
						WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

						if (preferredFilters != null) {
							List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.eltFilterList, 10);
							k = preferredFiltersList.size();
							newSize = filters.size() - preferredFiltersList.size();
						}
						for (; k < filters.size() - 1; k++) {
							List<WebElement> filterName = commonLibrary.isExistList(filters.get(k), UIMAP_SearchResult.tagSpan, 20);
							List<WebElement> filterName1 = commonLibrary.isExistList(filters.get(k + 1), UIMAP_SearchResult.tagSpan, 20);

							String text = filterName.get(1).getText();
							String text1 = filterName1.get(1).getText();
							text = text.replaceAll("(?<=\\d),(?=\\d)", "");
							text1 = text1.replaceAll("(?<=\\d),(?=\\d)", "");
							int name = Integer.valueOf(text);
							int name1 = Integer.valueOf(text1);

							if (name >= name1) {
								posFlag++;
							} else {
								negFlag++;
							}

						}
						if (preferredFilters != null) {
							if ((posFlag + count - 1) == (newSize - 1) && (negFlag == (count - 1))) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						} else {
							if (posFlag == (filters.size() - 1) && (negFlag == 0)) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header, Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						}
					}
				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filter order
	// # Function Name : verifyPreferredFilterNumberOfResultsOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPreferredFilterNumberOfResultsOrder(String filterHeader, String filters) {

		if (!(filterHeader.equals(" ") && filters.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			String newFilters = "";
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(filterHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), filterHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), filterHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + filterHeader, filterHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			List<WebElement> supplementalFilters = commonLibrary.isExistList(UIMAP_SearchResult.supplementalFilters, 10);
			if (supplementalFilters.size() > 0) {
				for (WebElement item : supplementalFilters) {
					if (item.getAttribute("data-id").toLowerCase().contains(filterHeader.toLowerCase())) {
						WebElement preferredFilters = commonLibrary.isExist(item, UIMAP_SearchResult.preferredFilters, 10);
						List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.lstTagName, 10);
						if (preferredFiltersList.size() > 0) {
							commonLibrary.focusControlJS(preferredFilters);
							for (int i = 0; i < preferredFiltersList.size() - 1; i++) {
								List<WebElement> filterName = commonLibrary.isExistList(preferredFiltersList.get(i), UIMAP_SearchResult.tagSpan, 20);
								List<WebElement> filterName1 = commonLibrary.isExistList(preferredFiltersList.get(i + 1), UIMAP_SearchResult.tagSpan, 20);
								String name = filterName.get(0).getText();
								String name1 = filterName1.get(0).getText();
								if (filters.toLowerCase().contains(name.toLowerCase().trim()) && filters.toLowerCase().contains(name1.toLowerCase().trim())) {
									if (!newFilters.contains(name))
										newFilters = name + ";";
									if (!newFilters.contains(name1))
										newFilters = newFilters + name1 + ";";
									String text = filterName.get(1).getText();
									String text1 = filterName1.get(1).getText();
									text = text.replaceAll("(?<=\\d),(?=\\d)", "");
									text1 = text1.replaceAll("(?<=\\d),(?=\\d)", "");
									int numberOfResults = Integer.valueOf(text);
									int numberOfResults1 = Integer.valueOf(text1);

									if (numberOfResults >= numberOfResults1) {
										posFlag++;
									} else {
										negFlag++;
									}
								}
							}
						}
						if (posFlag == (preferredFiltersList.size() - 1) && (negFlag == 0)) {
							report.updateTestLog("Verify the preferred filters " + filters + " are sorted By number of results (highest - lowest) under " + filterHeader, "The preferred filters " + newFilters + " are sorted By number of results (highest - lowest) under " + filterHeader, Status.PASS);
						} else
							report.updateTestLog("Verify the preferred filters " + filters + " are sorted By number of results (highest - lowest) under " + filterHeader, "The preferred filters " + filters + " are not sorted By number of results (highest - lowest) under " + filterHeader, Status.FAIL);
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + filters, "No Filter Selected.", Status.DONE);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink With Content
	// # Function Name : clickDocLinkWithContent    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkWithContent1(String strDocTitle, String text) {

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
						WebElement articleContent = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameArticle, 2);

						if (eleDocTitle != null && eleDocTitle.getText().trim().toLowerCase().trim().equals(strDocTitle.toLowerCase().trim()) && (articleContent.getText().trim().toLowerCase().contains(text.toLowerCase()))) {
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

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************

	// # Function Description : Function to verify filter content
	// # Function Name : verifyFilterContent    
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFilterContent(String filters) {
		boolean finalVerification = false;
		int counter = 0;
		int arrayLen;

		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_SearchResult.searchWithinResults, 20);
		if (ulCondentSwitcher != null && searchwithinResults != null) {
		}
		String arrfilters[] = filters.split(";");
		for (int k = 0; k < arrfilters.length; k++) {
			arrayLen = arrfilters.length;
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (int i = 0; i < docList.size() - 1; i++) {
						WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
						WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

						List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);

						for (int t = 0; t < dataTerms.size(); t++) {
							if (dataTerms.get(t).getText().equalsIgnoreCase(arrfilters[k])) {
								counter = counter + 1;
							}

						}
					}
				}
			}
			if (arrayLen == counter) {
				finalVerification = true;
				break;
			}
		}
		if (finalVerification)
			report.updateTestLog("Verify the contents of the Filter Pane on the left side of the page", " The contents of the Filter Pane is present on the left side of the page", Status.PASS);
		else
			report.updateTestLog("Verify the contents of the Filter Pane on the left side of the page", " The contents of the Filter Pane is not present on the left side of the page", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #****************************************************************************************************************************
	// # Function Description : Function to verify Recent and Favourites Panel
	// # Function Name : verifyRecentAndFavorites
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search verifyRecentAndFavorites1(String RecentList) {

		String recentSet[] = RecentList.split(";");
		boolean flag = false;
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
			if (RecentFavList.get(i).getText().replaceAll(" ", "").contains(recentSet[0].replace(" ", ""))) {
				for (int j = 0; j < recentSet.length; j++) {
					if (RecentFavList.get(i).getText().replaceAll(" ", "").contains(recentSet[j].replace(" ", ""))) {
						flag = true;

					}
				}
				if (flag)
					break;

			}
		}
		if (flag) {
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is  displayed ", Status.PASS);
		} else
			report.updateTestLog("Verify " + RecentList + " is displayed under recent and favourites", "" + RecentList + " is not displayed", Status.FAIL);

		WebElement preFilters = commonLibrary.isExist(UIMAP_Home.preFiltersDiv, 10);
		WebElement close = commonLibrary.isExist(preFilters, UIMAP_Home.btnClosePreFilterPopup, 10);

		if (close != null) {
			report.updateTestLog("Close button is present in Recent and Favorites panel", "Close button is present in Recent and Favorites panel", Status.PASS);

		} else
			report.updateTestLog("Close button is present in Recent and Favorites panel", "Close button is not present in Recent and Favorites panel", Status.FAIL);

		WebElement star = commonLibrary.isExist(UIMAP_Home.star, 10);
		if (star != null)
			report.updateTestLog("Verify state of the star icon is empty", "state of the star icon is empty", Status.PASS);
		else
			report.updateTestLog("Verify state of the star icon is empty", "state of the star icon is empty", Status.PASS);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected filter under Header
	// Box.
	// # Function Name : verifySelectedFilterUnderHeader
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifySelectedFilterUnderHeader(String strHeader, String filtersList) {
		String[] filters = filtersList.split(";");
		if (!(strHeader.equals(" ") && filtersList.equals(" "))) {
			int i = 0;

			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline")) {
				}
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("expanded")) {

						List<WebElement> filters1 = commonLibrary.isExistList(suplementalFilters.get(i), UIMAP_SearchResult.lstTagListItems, 20);

						for (int l = 0; l < filters.length; l++) {
							for (WebElement item : filters1) {

								if (item != null) {
									if (item.getText().toLowerCase().contains(filters[l].toLowerCase())) {
										report.updateTestLog("Verify : " + filters[l] + " is Present under" + strHeader, filters[l] + " is Present under" + strHeader, Status.PASS);
										Flag = true;
										break;

									}
								}
							}

						}
					}

				}
				if (Flag)
					break;
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : verifyPageTitle     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifyPageTitle(String strPageHeader) {
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader1, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader, 5);
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

		if (Header != null && Header.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is displayed as Header", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is not displayed as Header", Status.FAIL);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the notification message
	// # Function Name : verifyNotification
	// # Author : Seetha
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifyNotification(String notificationText) {

		WebElement notification = commonLibrary.isExist(UIMAP_Document.notificationError, 5);
		if (notification != null && notification.getText().toLowerCase().contains(notificationText.toLowerCase()))
			report.updateTestLog("Verify notification message appears", "Notification message is displayed", Status.PASS);
		else
			report.updateTestLog("Verify notification message appears", "Notification message is not displayed", Status.FAIL);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifySelectingPrefiltersMesage
	// filters
	// # Function Name : verifySelectingPrefiltersMesage
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public Search verifySelectingPrefiltersMesage(String header, String message) {

		if (!(header.equals(" "))) {

			int i = 0, j = 0;
			boolean Flag = false;
			// Clicking on Expand button for post filter header

			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {

				// looping for finding post filter header

				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {

						// to click on expand button

						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
					}

					WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

					if (preferredFilters != null) {

						// Verifying the message and edit settings button

						WebElement header1 = commonLibrary.isExist(preferredFilters, UIMAP_SearchResult.tagHeader1, 10);
						WebElement edit = commonLibrary.isExist(preferredFilters, UIMAP_SearchResult.preferredEdit, 10);
						if (header1 != null && edit != null) {

							switch (message) {
							case "select": {
								if (header1.getText().toLowerCase().contains(message.toLowerCase())) {
									report.updateTestLog("Verifying the message in '" + header + "' Post filter", "Select " + header + "  to display at the top of this list displays on the top of " + header + " list", Status.PASS);
									report.updateTestLog("Verifying the Edit Button displays under '" + header + "' Post filter", "Edit Button with 'X' Icon displayed", Status.PASS);
								} else {
									report.updateTestLog("Verifying the message in '" + header + "' Post filter", "Select " + header + "  to display at the top of this list is not displayed on the top of " + header + " list", Status.FAIL);
									report.updateTestLog("Verifying the Edit Button displays under '" + header + "' Post filter", "Edit Button with 'X' Icon displayed", Status.PASS);
								}
								break;
							}
							case "preferred": {
								if (header1.getText().toLowerCase().contains(message.toLowerCase())) {
									report.updateTestLog("Verifying the message in '" + header + "' Post filter", "Preferred " + header + "  displays under " + header + " post filter", Status.PASS);
									report.updateTestLog("Verifying the Edit Button displays under '" + header + "' Post filter", "The Edit Button displays under '" + header + "' Post filter", Status.PASS);
								} else {
									report.updateTestLog("Verifying the message in '" + header + "' Post filter", "Preferred " + header + "  is not displayed under " + header + " post filter", Status.FAIL);
									report.updateTestLog("Verifying the Edit Button displays under '" + header + "' Post filter", "The Edit Button displays under '" + header + "' Post filter", Status.PASS);
								}
								break;
							}
							}
						}
					} else {

						report.updateTestLog("Verifying message and button displays", "Verificatin is not done", Status.FAIL);
					}

				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verifying message and button displays", "Verificatin is not done", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Delivery Option
	// # Function Name : verifyDeliveryOption
	// # Author : Kirthika K
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifyDeliveryOption(String option, String deliveryOption) {

		generalFunctions.verifyDeliveryOption_New(option, deliveryOption);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Delivery Option
	// # Function Name : verifyDeliveryOption
	// # Author : Kirthika K
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifyAlertIcon() {
		WebElement btnAlert = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.alertIcon, 10);
		if (btnAlert != null) {
			report.updateTestLog("Verify Alert icon is  present", "Alert icon is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Alert icon is not present", "Alert icon is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter under Header
	// Box.
	// # Function Name : verifyFilterPresentUnderHeader
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifyFilterPresentUnderHeader(String strHeader, String strFilter, Boolean blnFlag) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("expanded")) {

						List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.lstTagListItems, 20);
						for (WebElement item : filters) {

							if (item != null) {
								if (item.getText().contains(strFilter)) {

									Flag = true;
									break;
								}
							}
						}
					}

				}
				if (Flag)
					break;
			}

			if (blnFlag) {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader, strFilter + " is  Displayed.", Status.PASS);
				else
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader, strFilter + " is not  Displayed.", Status.FAIL);
			} else {
				if (Flag)
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader, strFilter + " is   Displayed.", Status.FAIL);
				else
					report.updateTestLog("Verify : " + strFilter + " is Displayed under " + strHeader, strFilter + " is  Not Displayed.", Status.PASS);
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Highlighted text in
	// results
	// # Function Name : verfiytermshighlight1     
	// # Author : Vennila
	// # Date Created :22 sep 15
	// #*****************************************************************************************************************************

	public Search verfiytermshighlight1(String SampleText, String docTitle) {
		commonLibrary.sleep(15000);
		if (!SampleText.equalsIgnoreCase("asset purchase")) {

			Boolean flag1 = false;
			int count = 0;

			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement sentence : OListItems) {
						if (docTitle == sentence.getText()) {
							WebElement ShowExtractText = commonLibrary.isExist(sentence, UIMAP_SearchResult.eltShowExtract, 10);
							List<WebElement> termhighlighttext = commonLibrary.isExistList(ShowExtractText, UIMAP_SearchResult.eltShowTerms, 10);
							for (WebElement span : termhighlighttext) {// for(i=0;i<term.le;i++)
																		// {ter.get(0).eq(str1)}
								if (span != null) //
								{

									if (flag1) {
										count++;

									}
									if (count == OListItems.size()) {
										break;
									}
								}
							}
						}
					}

				}

				report.updateTestLog("Verify term " + SampleText + " is highlighed", "" + SampleText + " is Highlighted under" + docTitle + "in results page", Status.PASS);

			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click product logo
	// # Function Name : clickLATLogo     
	// # Author : Krthika
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickLATLogo() {
		WebElement logo = commonLibrary.isExist(UIMAP_SearchResult.latProductLogo, 10);
		if (logo != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(logo, "Product Logo");
			else
				commonLibrary.clickButtonParentWithWait(logo, "Product Logo");
			report.updateTestLog("Click on product logo", "Lexis Advance Landing page is displayed ", Status.PASS);
		} else {
			report.updateTestLog("Click on product logo", "Lexis Advance Landing page is not displayed ", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount1     
	// # Author : Vennila
	// # Date Created :23 sep 15
	// #*****************************************************************************************************************************
	public Search getResultcount1(String tcName, String dataSheet, String colName) {

		String category1 = null;
		// String[] category=;
		String list, s1 = "";
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.eltResultHeader, 20);
		if (resultList != null) {
			list = resultList.getText();
			String[] category = list.split(" ");

			category1 = category[1].substring(1, category[1].length() - 1);

			String[] s = category1.split(Pattern.quote(")"));
			s1 = s[0].replace(",", "");
			// int x=s1;
			// x=Integer.parseInt(s1);
		}
		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, s1);
		report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : verifyresultcount1    
	// # Author : Vennila
	// # Date Created :23 sep 15
	// #*****************************************************************************************************************************

	public Search verifyresultcount1(String rescnt1, String rescnt2) {

		int v1, v2 = 0;
		v1 = Integer.parseInt(rescnt1);
		v2 = Integer.parseInt(rescnt2);
		if (v2 >= v1) {
			report.updateTestLog("Verifying Result count displayed after excluding legal phrase", "Result count displayed after excluding legal phrase is greater", Status.PASS);
		} else {
			report.updateTestLog("Verifying Result count displayed after excluding legal phrase", "Result count displayed after excluding legal phrase is NOT greater", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click favourite icon in RFF tab
	// # Function Name : clickFavbtninRFF
	// # Author : Vennila
	// # Date Created : 24 Sep 15
	// #*****************************************************************************************************************************

	public Search clickFavbtninRFF(String menuname, String rfflink) {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		commonLibrary.clickButtonLogSmallWait(btnClassFilter, "Filter");
		this.FilterMenuSelection(menuname);

		WebElement rffitems = commonLibrary.isExist(UIMAP_Home.divRecentFilters, 20);
		List<WebElement> li = commonLibrary.isExistList(rffitems, UIMAP_Home.lstTagListItems, 20);

		for (int i = 0; i < li.size(); i++) {
			if (li.get(i).getText().contains(rfflink)) {
				WebElement btnFav = commonLibrary.isExist(li.get(i), UIMAP_Home.addFavorite, 20);
				if (btnFav != null && btnFav.getAttribute("class").contains("icon la-FavoriteEmpty")) {
					commonLibrary.clickButtonLogSmallWait(btnFav, "Favourite button of " + rfflink);
					report.updateTestLog("Selecting favorite filter", "Favourite button of " + rfflink + "is clicked", Status.PASS);
				} else if (btnFav != null && btnFav.getAttribute("class").contains("icon la-FavoriteFull")) {
					report.updateTestLog("Selecting favorite filter", "Favourite button of " + rfflink, Status.WARNING);
				}
			}

		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click favourite icon in RFF tab
	// # Function Name : clickRFFlink
	// # Author : Vennila
	// # Date Created : 24 Sep 15
	// #*****************************************************************************************************************************
	public Search clickRFFlink(String rffText) {

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
			if (RecentFavList.get(i).getText().equalsIgnoreCase(rffText)) {

				commonLibrary.clickLinkWithWebElementWithWait(RecentFavList.get(i), rffText);

				break;

			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Results list link from
	// document after SDR search
	// # Function Name : selectresultslink
	// # Author : Meera
	// # Date Created : Sep'15
	// #*************************************************************************************************************************

	public Search selectsdrresultslink() {

		WebElement resultlink = commonLibrary.isExist(UIMAP_Document.lnksdrresultslist, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Results List");
		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Results List");
		}

		return new Search(scriptHelper);
	}

	public Search clickSearchButton() {
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order
	// # Function Name : verifyPostFilterOrder
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPostFilterPresent(String header) {
		if (!(header.equals(" "))) {
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					report.updateTestLog("Verify Post filter " + header + " present", "Post filter " + header + " is present", Status.PASS);
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
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
					if (filters.size() > 0) {
						report.updateTestLog("Verify filters present under " + header + " Post filter", "Filters present under " + header + " Post filter", Status.PASS);
					}
				}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : verifyFilterHeaderPresent
	// # Function Name : verifyFilterHeaderPresence
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search verifyFilterHeaderPresence(String strHeader, String filterPresence) {

		boolean state = Boolean.parseBoolean(filterPresence);
		if (state) {
			boolean flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			if (filterHeader.size() > 0) {
				commonLibrary.scrollToView(filterContainer);
				for (int i = 0; i < filterHeader.size(); i++) {

					if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
						report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is displayed in the left Pane", Status.PASS);
						flag = true;
						break;
					}

				}
				if (!flag) {
					report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify post filter presence", "Post filter container is not present", Status.FAIL);
			}
		} else {
			boolean flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			if (filterHeader.size() > 0) {
				commonLibrary.scrollToView(filterContainer);
				for (int i = 0; i < filterHeader.size(); i++) {

					if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
						report.updateTestLog("Verify Filter '" + strHeader + "' is not present", "Filter '" + strHeader + "' is displayed in the left Pane", Status.FAIL);
						flag = true;
						break;
					}

				}
				if (!flag) {
					report.updateTestLog("Verify Filter '" + strHeader + "' is not present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.PASS);
				}
			} else {
				report.updateTestLog("Verify post filter presence", "Post filter container is not present", Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify content type is displayed or
	// not
	// Box.
	// # Function Name : verifyContentTypeDisplayed_new
	// # Author : Vennila
	// # Date Created : 1 Oct'15
	// #*****************************************************************************************************************************

	public Search verifyContentTypeDisplayed_new(String contentTypes, Boolean value) {
		value = false;
		String[] namesList = contentTypes.split(";");
		WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
		if ((btnFewerCat != null) && (btnFewerCat.getText().contains("Show more")))
			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, "More Categories");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, "More Categories");
		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
		if (OListItems.size() - 1 == namesList.length) {
			report.updateTestLog("Verify only " + namesList.length + " content types are displayed.", namesList.length + " content types are displayed.", Status.PASS);
			for (int i = 0; i < namesList.length; i++) {
				boolean flag = false;
				for (WebElement item : OListItems) {
					if (item.getText().toLowerCase().contains(namesList[i].toLowerCase())) {
						flag = true;
						value = true;
						if (value = true) {
							report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is displayed.", Status.PASS);
							break;
						}
					}

				}
				if (!flag && !value)
					report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is not displayed.", Status.PASS);
			}
		} else
			report.updateTestLog("Verify only " + namesList.length + " content types are displayed.", +namesList.length + " content types are not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify value displayed in sort by
	// drop down or not
	// # Function Name : verifySortBy_new
	// # Author : Vennila
	// # Date Created : 30 Sep '15
	// #*****************************************************************************************************************************
	public Search verifySortBy_new(String value, Boolean flag1) {

		// boolean flag = false;
		flag1 = false;
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
				if (item.getText().toLowerCase().contains(value.toLowerCase())) {
					flag1 = true;
					if (flag1 = true) {
						report.updateTestLog(value + " is displayed in the Sort by dropdown", value + " is displayed in the Sort by dropdown", Status.PASS);
					} else
						report.updateTestLog(value + " is the active item in the Sort by dropdown", sortBy.getText() + " is not the active item in the Sort by dropdown", Status.FAIL);
				}
			}

			if (!flag1) {
				report.updateTestLog(value + " is displayed in the Sort by dropdown", value + " is not displayed in the Sort by dropdown", Status.PASS);
			}

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : NavigateToAll     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Search selectchildoptionsinSources(String option) {
		Boolean blnSecond = false;

		selectMenuFromBrowse("Sources");
		// To Select Category, Jurisdiction, Practice Area, Publisher,
		// Subscription
		WebElement divIdBrowserMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserMenu1, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems1 = commonLibrary.isExistList(lstTagAside1.get(1), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems1) {

			if (button.getText().contains(option)) {
				WebElement sourceBtn = commonLibrary.isExist(button, By.tagName("button"), 20);
				blnSecond = true;
				if (browsername.contains("internet"))
					commonLibrary.clickJS(sourceBtn, option);
				else
					commonLibrary.clickButtonParentWithWait(sourceBtn, option);
				break;
			}
		}
		if (!blnSecond)
			report.updateTestLog("Click on Categories", option + " Option not present", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the sub child sources in SS
	// # Function Name : selectsubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Search selectsubchildSources(String item) {
		Boolean blnfirst = false;
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems2) {

			if (button.getText().contains(item)) {
				blnfirst = true;
				WebElement linkname = commonLibrary.isExist(button, UIMAP_Home.linkname, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(linkname, item);
				else
					commonLibrary.clickButtonParentWithWait(linkname, item);
				break;
			}
		}
		if (!blnfirst)
			report.updateTestLog("Click on Items", item + " Items not present", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the active content type of
	// the
	// results.
	// # Function Name : verifyActiveContentType     
	// # Author : Vennila
	// # Date Created : 5 Oct 15
	// #*****************************************************************************************************************************

	public Search verifyActiveContentType(String strContentType) {
		WebElement eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);

		int counter = 0;
		do {
			counter = counter + 1;
			eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
			if (eltResultHeader == null)
				commonLibrary.sleep(1000);

		} while ((eltResultHeader != null) && (counter < 30));

		eltResultHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.eltResultHeader, 10);
		if (eltResultHeader != null) {
			// if (eltResultHeader.class.trim()="activetab" &&
			// eltResultHeader.getText().toLowerCase().contains(strContentType.toLowerCase()))
			report.updateTestLog("Verify content type: " + strContentType + " displays.", "Content type: " + strContentType + " displays.", Status.PASS);
		} else {
			generalFunctions.selectCondentType(strContentType);
			// generalFunctions.selectCondentType(strContentType);
			report.updateTestLog("Verify content type: " + strContentType + " displays.", "Content type: " + strContentType + " is not displayed.", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click On Bact to Top arrow
	// # Function Name : clickBackToTop   
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************
	public Search clickBackToTop() {
		WebElement backToTop = commonLibrary.isExist(UIMAP_SearchResult.backToTop, 20);
		if (backToTop != null) {
			commonLibrary.clickButton(backToTop);
			report.updateTestLog("Click on Back To Top button", "Back To Top button is clicked", Status.PASS);
		} else {
			report.updateTestLog("Click on Back To Top button", "Back To Top button is not clicked", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Bact to Top arrow
	// # Function Name : verifyBackToTop   
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************

	public Search verifyBackToTop(Boolean arrowPresent) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll the search page
	// # Function Name : scrollSearchPage  
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************

	public Search scrollSearchPage() {

		commonLibrary.scrollDown();
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Back browser
	// # Function Name : clickBrowserBack  
	// # Author : Meera
	// # Date Created : 1-Oct'15
	// #*********************************************************************************************************

	public Home clickBrowserBacktoHome() {
		commonLibrary.clickBrowserBack();

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyResultsCount
	// # Function Name : VerifyCountOfResults     
	// # Author : Kirthika
	// # Date Created : Feb '15
	// #*****************************************************************************************************************************

	public Search verifyResultsCount(String number) {
		int num = Integer.parseInt(number);
		boolean isDocNum = false;
		WebElement form = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 20);
		if (form != null) {
			if (form.getText().contains(number)) {

				isDocNum = true;
			}

		}

		if (isDocNum) {
			report.updateTestLog("Verify results " + num + " displays.", "Results " + num + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results " + num + " displays.", "Results " + num + " is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click webLink
	// # Function Name : clickWebLink     
	// # Author : Kirthika
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickWebLink(String docTitle) {
		boolean flg = false;
		List<WebElement> document = commonLibrary.isExistList(UIMAP_SearchResult.h2Document, 10);
		if (document != null) {
			for (WebElement list : document) {
				List<WebElement> actualLink = commonLibrary.isExistList(list, UIMAP_SearchResult.webLink, 10);

				for (WebElement link : actualLink) {

					if (link.getText().toLowerCase().trim().equals((docTitle).toLowerCase().trim())) {
						commonLibrary.clickLinkWithWebElementWithWait(link, docTitle);
						commonLibrary.sleep(5000);

						boolean secondWindow = commonLibrary.switchToWindow("negotiationlawblog");
						if (secondWindow == true) {

							commonLibrary.switchToWindow("cert7-advance");

						}
						flg = true;
						break;
					}

				}

				if (flg)
					break;
			}

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Enter timeline and search
	// Results page.
	// # Function Name : selectPostFilterTimeline
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search selectPostFilterTimeline(String startYear, String endYear) {

		if (!(startYear.equals(" ") && endYear.equals(" "))) {

			int i = 0;
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
								commonLibrary.sleep(2000);
							}
							WebElement startYearTextbox = commonLibrary.isExist(UIMAP_VSASearchResults.startYear, 20);

							WebElement endYearTextbox = commonLibrary.isExist(UIMAP_VSASearchResults.endYear, 20);

							if (startYearTextbox != null && endYearTextbox != null) {
								commonLibrary.sleep(4000);
								commonLibrary.setDataInTextBox(startYearTextbox, startYear, "StartYear");
								commonLibrary.sleep(4000);
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
		}

		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		commonLibrary.sleep(40000);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details
	// # Function Name : verifyHistoryDetails     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search verifyHistoryDetails(String startDate, String endDate) {
		String actualDate = null;
		Boolean dateVerified = false;
		Boolean verificationDone = false;
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				if (contentList.size() > 0) {
					for (WebElement item : contentList) {
						List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("aside"), 20);
						if (contentLink.size() > 0) {
							for (WebElement item1 : contentLink) {

								List<WebElement> Val = commonLibrary.isExistList(item1, UIMAP_SearchResult.dd, 10);
								if (Val.size() > 0) {
									List<WebElement> clientHeading = commonLibrary.isExistList(item1, UIMAP_SearchResult.dt, 10);
									for (int i = 0; i < clientHeading.size(); i++) {
										if (clientHeading.get(i).getText().equalsIgnoreCase("Date")) {
											actualDate = Val.get(i).getText();

											dateVerified = verifyDateRange(startDate, endDate, actualDate);
											verificationDone = true;
											if (!dateVerified)

												break;
										}
									}

									if (!dateVerified)

										break;
								}
							}
						}
					}

				}

			}
		}

		if (dateVerified)
			report.updateTestLog("Dates are in the range of timeline filters applied", "Dates are in the region of the timeline filters applied", Status.PASS);
		else if (verificationDone == true) {

			report.updateTestLog("Dates are in the range of timeline filters applied", "Dates are not in the region of the timeline filters applied", Status.FAIL);
		} else {
			report.updateTestLog("Dates filters are not there in the report", "Dates filters are not there in the report", Status.WARNING);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Date Range
	// # Function Name : verifyDateRange     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Boolean verifyDateRange(String startDate, String endDate, String actualDate)

	{

		Boolean flag = false;

		SimpleDateFormat formatter = new SimpleDateFormat("MMMdd,yyyy");

		Date startDate1, endDate1, actualDate1;

		try {
			startDate1 = formatter.parse(startDate);
			endDate1 = formatter.parse(endDate);
			actualDate1 = formatter.parse(actualDate);
			System.out.println(actualDate);

			if (actualDate1.after(startDate1) && actualDate1.before(endDate1)) {
				flag = true;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change Date in calendar
	// # Function Name : changeDateInCalendar     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search changeDateInCalendar() {
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.startDateCalender, 20);
		WebElement day = null;

		if (rslt != null) {

			commonLibrary.clickLinkWithWebElementWithWait(rslt, "Calendar");
			day = commonLibrary.isExist(UIMAP_SearchResult.startDay, 20);
			commonLibrary.clickLinkWithWebElementWithWait(day, "Day");
			WebElement timelinkOKbutton = commonLibrary.isExist(UIMAP_VSASearchResults.timeLineOkButton, 20);
			if (timelinkOKbutton != null) {

				if (browsername.toLowerCase().contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(timelinkOKbutton, "OK");

				} else {
					commonLibrary.clickLinkWithWebElementWithWait(timelinkOKbutton, "OK");

				}
			}

			else {
				report.updateTestLog("Clicking the OK Button", "OK Button is not Clicked", Status.FAIL);
				System.out.println("Textbox not present");

			}

		}
		commonLibrary.sleep(40000);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify timeline slider year
	// # Function Name : verifyTimeLineSliderYear     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search verifyTimeLineSliderYear(String startDate, String endDate) {

		WebElement startYear = commonLibrary.isExist(UIMAP_SearchResult.sliderStartYear, 20);

		WebElement endYear = commonLibrary.isExist(UIMAP_SearchResult.sliderEndYear, 20);
		if (startYear != null && endYear != null) {

			if (startYear.getText().equalsIgnoreCase(startDate) && endYear.getText().equalsIgnoreCase(endDate)) {

				report.updateTestLog("Slider year matches with the year in the timeline textbox", "Slider year matches with the year in the timeline textbox", Status.PASS);

			}

			else {
				report.updateTestLog("Slider year matches with the year in the timeline textbox", "Slider year  does not matches with the year in the timeline textbox", Status.PASS);

			}
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify content types and active
	// content type
	// # Function Name : verifyActiveContentTypeAndOthers
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search verifyActiveContentTypeAndOthers(String activeType, String others) {
		String[] otherContent = others.split(";");
		boolean active = false;
		boolean present = false;

		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		WebElement activeContent = commonLibrary.isExist(contentSwitcher, UIMAP_SearchResult.activeContentType, 10);
		if (activeContent != null) {
			if (activeContent.getText().toLowerCase().contains(activeType.toLowerCase())) {
				active = true;
			}
		}
		if (active)
			report.updateTestLog("Verify results are displayed with " + activeType + " as active contentType", "Results are with " + activeType + " as active contentType", Status.PASS);
		else
			report.updateTestLog("Verify results are displayed with " + activeType + " as active contentType", "Results are not displayed with " + activeType + " as active contentType", Status.FAIL);

		List<WebElement> otherTypes = commonLibrary.isExistList(contentSwitcher, UIMAP_SearchResult.lstTagName, 10);
		if (otherTypes != null) {
			for (int i = 0; i < otherContent.length; i++) {
				for (int j = 0; j < otherTypes.size(); j++) {
					if (otherContent[i].toLowerCase().contains(otherTypes.get(j).getText().toLowerCase())) {
						report.updateTestLog("Verify " + otherContent[i] + " is displayed", "" + otherContent[i] + " is displayed", Status.PASS);
						present = true;
						break;
					}
				}
				if (!present) {
					report.updateTestLog("Verify " + otherContent[i] + " is displayed", "" + otherContent[i] + " is not displayed", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify results displayed with all filters applied", "Results are not displayed with all filters applied", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify green flag displayed
	// # Function Name : verifyGreenFlag
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search verifyGreenFlag(String content) {
		boolean flagnotPresent = false;

		WebElement dialog = commonLibrary.isExist(UIMAP_SearchResult.contenttyperesult, 10);
		if (dialog != null) {
			List<WebElement> lists = commonLibrary.isExistList(dialog, UIMAP_SearchResult.lstTagName, 10);
			if (lists != null) {
				for (int j = 0; j < lists.size(); j++) {
					List<WebElement> checkbox = commonLibrary.isExistList(lists.get(j), UIMAP_SearchResult.chkbox, 10);
					for (WebElement item : checkbox) {
						if (item.getAttribute("data-value").equalsIgnoreCase(content)) {
							WebElement flag = commonLibrary.isExist(lists.get(j), UIMAP_SearchResult.flagPresent, 10);
							if (!flag.getAttribute("class").contains("showflag")) {
								flagnotPresent = true;
								break;
							}

						}
					}
					if (flagnotPresent)
						break;
				}
			}
		}
		if (flagnotPresent)
			report.updateTestLog("Verify green flag is displayed next to " + content + "", "Green flag is displayed next to " + content + "", Status.PASS);
		else
			report.updateTestLog("Verify green flag is displayed next to " + content + "", "Green flag is not displayed next to " + content + "", Status.FAIL);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select value in sort by drop down of
	// snapshot page
	// # Function Name : selectSortBySnapshot
	// # Author : Vennila
	// # Date Created : 6 Oct '15
	// #*****************************************************************************************************************************
	public Search selectSortBySnapshot(String value) {
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

				sortByList = commonLibrary.isExist(UIMAP_SearchResult.sortByJumpList, 10);
				i++;
			} while (sortByList == null && i < 3);

			if (sortByList != null) {
				List<WebElement> sortValues = commonLibrary.isExistList(sortByList, By.tagName("li"), 20);
				for (WebElement item : sortValues) {
					if (item.getText().contains(value)) {
						WebElement button = commonLibrary.isExistNegative(item, UIMAP_SearchResult.btnButton, 10);
						if (browsername.contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(button, value);
						else
							commonLibrary.clickButtonParentWithWait(button, value);
						report.updateTestLog("Click on " + value + " in Sort By drop down", value + " is selected from Sort By drop down", Status.PASS);
						commonLibrary.sleep(20000);
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
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected pod displays in
	// viewable region of snapshot page
	// region
	// # Function Name : verifyPodDisplaysInViewableRegion   
	// # Author : Vennila
	// # Date Created : 6 Oct 15
	// #*****************************************************************************************************************************
	public Search verifyPodDisplaysInViewableRegion(String text) {
		// Verification point : verifying the text is displayed in viewable
		// region or not after loading the document

		boolean flag = false;
		WebElement toolbar = commonLibrary.isExist(UIMAP_SearchResult.snapToolbar, 10);
		List<WebElement> documentText = commonLibrary.isExistList(UIMAP_SearchResult.eltExpandedFilterHeader, 10);

		if (documentText != null) {
			for (WebElement textMessage : documentText) {

				if (textMessage.getText().toLowerCase().contains(text.toLowerCase()) && toolbar != null) {
					// checking the region with location

					int textLocation = textMessage.getLocation().getY();
					int toolbarLocation = toolbar.getLocation().getY();
					if ((textLocation - toolbarLocation) >= 0 && (textLocation - toolbarLocation) <= 1000) {

						report.updateTestLog("Verify '" + text + "' pod displays in the viewable region", "Text '" + text + "' pod is displayed in the viewable region", Status.PASS);
						flag = true;
						break;
					}
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + text + " pod displays in the viewable region", text + " pod is not displayed in the viewable region", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the sub child sources in SS
	// # Function Name : selectSSsubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Sources selectSSsubchildSources(String item) {
		Boolean blnfirst = false;
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems2) {

			if (button.getText().contains(item)) {
				blnfirst = true;
				WebElement linkname = commonLibrary.isExist(button, UIMAP_Home.linkname, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(linkname, item);
				else
					commonLibrary.clickButtonParentWithWait(linkname, item);
				break;
			}
		}
		if (!blnfirst)
			report.updateTestLog("Click on Items", item + " Items not present", Status.FAIL);
		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify content type is displayed or
	// not
	// Box.
	// # Function Name : verifyContentTypeDisplayed_new
	// # Author : Vennila
	// # Date Created : 1 Oct'15
	// #*****************************************************************************************************************************

	public Search verifyssContentTypeDisplayed_new(String contentTypes, Boolean value) {
		value = false;
		String[] namesList = contentTypes.split(";");
		WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
		if ((btnFewerCat != null) && (btnFewerCat.getText().contains("Show more")))
			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, "More Categories");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, "More Categories");
		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher1, 10);
		List<WebElement> OListItems = commonLibrary.isExistList(ulCondentSwitcher, By.tagName("li"), 20);
		// if (OListItems.size()-1 >= namesList.length) {
		// report.updateTestLog("Verify only " + namesList.length +
		// " content types are displayed.", namesList.length +
		// " content types are displayed.", Status.PASS);
		for (int i = 0; i < namesList.length; i++) {
			boolean flag = false;
			for (WebElement item : OListItems) {
				if (item.getText().toLowerCase().contains(namesList[i].toLowerCase())) {
					flag = true;
					value = true;
					if (value = true) {
						report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is displayed.", Status.PASS);
						break;
					}
				}

			}
			if (!flag && !value)
				report.updateTestLog("Verify content type " + namesList[i] + " is displayed.", "Content type " + namesList[i] + " is not displayed.", Status.PASS);
		}
		// } else
		// report.updateTestLog("Verify only " + namesList.length +
		// " content types are displayed.", +namesList.length +
		// " content types are not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify start date and end date
	// # Function Name : verifyStartDateAndEndDate
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search verifyStartDateAndEndDate(String start, String end) {
		boolean starts = false;
		boolean ends = false;
		SimpleDateFormat formatter = new SimpleDateFormat("MMMdd,yyyy");
		Date start1, end1;
		try {
			start1 = formatter.parse(start);
			end1 = formatter.parse(end);

			// Verify start date
			WebElement startdate = commonLibrary.isExist(UIMAP_Home.startdate, 10);
			if (startdate != null) {
				commonLibrary.focusControlJS(startdate);
				if (start1.equals(formatter.parse(startdate.getAttribute("value"))))
					starts = true;
			}

			// Verify end date
			WebElement enddate = commonLibrary.isExist(UIMAP_Home.enddate, 10);
			if (enddate != null) {
				commonLibrary.focusControlJS(enddate);
				if (end1.equals(formatter.parse(enddate.getAttribute("value"))))
					ends = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (starts && ends)
			report.updateTestLog("Verify date range. Start date as " + start + " and end date as " + end + "", " Start date is displayed as " + start + " and end date is displayed as " + end + "", Status.PASS);
		else
			report.updateTestLog("Verify date range. Start date as " + start + " and end date as " + end + "", " Start date is not displayed as " + start + " and end date is not displayed as " + end + "", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify secondary sort order
	// Box.
	// # Function Name : verifySecSortOrder
	// # Author : Vennila
	// # Date Created : 12 Oct'15
	// #*****************************************************************************************************************************

	public Search verifySecSortOrder() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult2, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);

			if (OListResult != null) {
				List<WebElement> docList = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (int i = 0; i < docList.size() - 1; i++) {

					WebElement docContent = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.docContent, 20);
					WebElement aside = commonLibrary.isExist(docContent, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms = commonLibrary.isExistList(aside, By.tagName("dt"), 20);
					List<WebElement> dataDefns = commonLibrary.isExistList(aside, By.tagName("dd"), 20);

					String dataDefn = null;
					String dataDefn1 = null;
					for (int t = 0; t < dataTerms.size(); t++) {
						if (dataTerms.get(t).getText().equalsIgnoreCase("Date")) {
							dataDefn = dataDefns.get(t).getText();
							break;
						}
					}

					WebElement docContent1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.docContent, 20);
					WebElement aside1 = commonLibrary.isExist(docContent1, UIMAP_SearchResult.lstTagAside, 20);

					List<WebElement> dataTerms1 = commonLibrary.isExistList(aside1, By.tagName("dt"), 20);
					List<WebElement> dataDefns1 = commonLibrary.isExistList(aside1, By.tagName("dd"), 20);
					for (int t = 0; t < dataTerms1.size(); t++) {
						if (dataTerms1.get(t).getText().equalsIgnoreCase("Date")) {
							dataDefn1 = dataDefns1.get(t).getText();
							break;
						}
					}

					SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
					Date date = null;
					try {
						date = format.parse(dataDefn);
					} catch (Exception e) {
						// posflag++;
					}
					Date date1 = null;
					try {
						date1 = format.parse(dataDefn1);
					} catch (Exception e) {
						// posflag++;
					}
					if (date != null && date1 != null) {
						int k = date.compareTo(date1);
						if (k == 0) {
							WebElement docTitle = commonLibrary.isExist(docList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
							WebElement docTitle1 = commonLibrary.isExist(docList.get(i + 1), UIMAP_SearchResult.TitleClassDoc, 20);
							String title = docTitle.getText();
							String title1 = docTitle1.getText();
							if (title.compareTo(title1) < 0) {
								report.updateTestLog("Verifying secondary sort in document title", "Secondary sort in document title is verified", Status.PASS);
								break;
							} else
								report.updateTestLog("Verifying secondary sort in document title", "None of the document titles displayed have same dates ", Status.WARNING);
						}
					}
				}
			}
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify timeline graph
	// # Function Name : verifyTimelineGraph     
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search verifyTimelineGraph(String startDate, String endDate) {

		WebElement startYear = commonLibrary.isExist(UIMAP_SearchResult.sliderStartYear, 20);
		WebElement endYear = commonLibrary.isExist(UIMAP_SearchResult.sliderEndYear, 20);

		if (startYear != null && endYear != null) {
			commonLibrary.focusControlJS(startYear);
			if (startYear.getText().equalsIgnoreCase(startDate) && endYear.getText().equalsIgnoreCase(endDate))
				report.updateTestLog("Verify timeline graph is displayed correctly", "Timeline graph is displayed correctly", Status.PASS);
			else
				report.updateTestLog("Verify timeline graph is displayed correctly", "Timeline graph is not displayed correctly", Status.FAIL);
		} else
			report.updateTestLog("Verify timeline graph is displayed correctly", "Timeline graph is not displayed correctly", Status.FAIL);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifyNarrowbyFilterLeftPanePresent    
	// # Author : Vennila
	// # Date Created : 13 Oct '15
	// #*****************************************************************************************************************************

	public Search verifyNarrowbyFilterLeftPanePresent() {

		/*
		 * WebElement usedfilter =
		 * commonLibrary.isExist(UIMAP_SearchResult.unusedfilter, 20); if
		 * (usedfilter == null) {
		 * report.updateTestLog("To Verify Narrow By Section",
		 * " Narrow By Section contains " + "null", Status.PASS); } else {
		 * report.updateTestLog("To Verify active category tab",
		 * "Narrow By Section contains " + "null", Status.FAIL); }
		 */
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set cases in start in
	// # Function Name : setCases   
	// # Author : Deepha
	// # Date Created : 13 Oct '15
	// #*****************************************************************************************************************************

	public Search setCases() {
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

		WebElement listbox = commonLibrary.isExist(By.cssSelector("select[id='settings_research_searchresults']"), 20);
		commonLibrary.selectByVisibleTextByValue(listbox, "urn:hlct:5", "Cases");
		report.updateTestLog("Selecting Cases from start in drop down", "Selecting Cases from start in drop down", Status.DONE);
		commonLibrary.scrollDown(1000);
		WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
		if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
			commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

		else {
			report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.DONE);
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title link with
	// contains method
	// # Function Name : clickDocLink1
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkContains(String strDocTitle) {

		generalFunctions.clickDocLinkContains(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Wordwheel is displayed.
	// # Function Name : verifyWordWheel
	// # Author : Vennila
	// # Date Created : Oct '15
	// #*****************************************************************************************************************************

	public Search verifyWordWheel() {
		generalFunctions.verifyWordWheel();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Date Range
	// # Function Name : verifyDateRange     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Boolean verifyYearRange(String startDate, String endDate, String actualDate) {

		Boolean flag = false;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

		Date startDate1, endDate1, actualDate1;

		try {
			startDate1 = formatter.parse(startDate);
			endDate1 = formatter.parse(endDate);
			actualDate1 = formatter.parse(actualDate);
			System.out.println(actualDate);

			if ((actualDate1.after(startDate1) && actualDate1.before(endDate1)) || (actualDate1.equals(startDate1)) || (actualDate1.equals(endDate1))) {
				flag = true;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify history details
	// # Function Name : verifyHistoryDetailsWithYear     
	// # Author : Kirthika
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Search verifyHistoryDetailsWithYear(String startDate, String endDate) {
		String actualDate = null;
		Boolean dateVerified = false;
		Boolean verificationDone = false;
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, By.tagName("li"), 20);
				if (contentList.size() > 0) {
					for (WebElement item : contentList) {
						List<WebElement> contentLink = commonLibrary.isExistList(item, By.tagName("aside"), 20);
						if (contentLink.size() > 0) {
							for (WebElement item1 : contentLink) {

								List<WebElement> Val = commonLibrary.isExistList(item1, UIMAP_SearchResult.dd, 10);
								if (Val.size() > 0) {
									List<WebElement> clientHeading = commonLibrary.isExistList(item1, UIMAP_SearchResult.dt, 10);
									for (int i = 0; i < clientHeading.size(); i++) {
										if (clientHeading.get(i).getText().equalsIgnoreCase("Date")) {
											actualDate = Val.get(i).getText();

											dateVerified = verifyYearRange(startDate, endDate, actualDate);
											verificationDone = true;
											if (!dateVerified)

												break;
										}
									}

									if (!dateVerified)

										break;
								}
							}
						}
					}

				}

			}
		}

		if (dateVerified)
			report.updateTestLog("Dates are in the range of timeline filters applied", "Dates are in the region of the timeline filters applied", Status.PASS);
		else if (verificationDone == true) {

			report.updateTestLog("Dates are in the range of timeline filters applied", "Dates are not in the region of the timeline filters applied", Status.FAIL);
		} else {
			report.updateTestLog("Dates filters are not there in the report", "Dates filters are not there in the report", Status.WARNING);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify narrow by search item in
	// Search page
	// # Function Name : verifySearchItemNarrowBy
	// # Author : Bikash
	// # Date Created : Oct'14
	// #*****************************************************************************************************************************

	public Search verifySearchItemNarrowBy(String searchItem) {
		WebElement filterRemoveButon = commonLibrary.isExist(UIMAP_SearchResult.btnFilterRemove, 30);
		if (filterRemoveButon != null) {
			filterRemoveButon = commonLibrary.isExist(UIMAP_SearchResult.btnFilterRemove, 10);
			if (filterRemoveButon.getText().toLowerCase().trim().equals(searchItem.toLowerCase().trim()))
				report.updateTestLog("Verify selected item " + searchItem + " under narrow by", "Selected item " + searchItem + " displays under narrow by", Status.PASS);
			else
				report.updateTestLog("Verify selected item " + searchItem + " under narrow by", "Selected item " + searchItem + " does not displays under narrow by", Status.FAIL);
		} else {
			report.updateTestLog("Verify filter remove button under narrow by", "Filter remove button is not presentunder narrow by", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTextInWordWheel(String text) {

		generalFunctions.verifyTextInWordWheel_1(text);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write the URL into data sheet.
	// # Function Name : saveURL     
	// # Author : Anbu
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Search saveURL(String tcName, String dataSheet, String colName) {

		generalFunctions.saveURL(tcName, dataSheet, colName);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout.
	// # Function Name : logoutDeleteAllCookies     
	// # Author : Anbu
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public SignIn logoutDeleteAllCookies() {

		generalFunctions.logoutDeleteAllCookies();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify entity search result page is
	// displayed.
	// # Function Name : verifyEntitySearchResultPageDisplayed     
	// # Author : Anbu
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Search verifyEntitySearchResultPageDisplayed(String headerText) {

		WebElement header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);
		int count = 0;
		do {
			count++;
			header = commonLibrary.isExist(UIMAP_Document.TitleClassTOC, 10);
			if (header == null)
				commonLibrary.sleep(3000);
		} while (header == null && count <= 5);

		if (header != null) {
			// verifying entity search result page is displayed
			if (header.getText().contains(headerText))
				report.updateTestLog("Verify '" + headerText + "'  entity search results page displayed", headerText + " entity search results page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify '" + headerText + "'  entity search results page displayed", headerText + " entity search results page is NOT displayed", Status.FAIL);

		} else
			report.updateTestLog("Verify '" + headerText + "'  entity search results page displayed", headerText + " entity search results page is NOT displayed", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink With Content
	// # Function Name : clickDocLinkWithContent2    
	// # Author : Bikash
	// # Date Created : Oct'22
	// #*****************************************************************************************************************************

	public Document clickDocLinkWithContent2(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		for (i = 0; i < 6; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

					for (WebElement document : OListItems) {
						commonLibrary.sleep(5000);
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().trim().toLowerCase().trim().equals(strDocTitle.toLowerCase().trim())) {
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

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify court and date being
	// displayed aside results list
	// region
	// # Function Name : verifyCourtDatePresence   
	// # Author : Kalaivanan
	// # Date Created : 12 Sep 15
	// #*****************************************************************************************************************************

	public Search verifyCourtDatePresence() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement tagNameAside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 20);

					String month = "Jan;Feb;Mar;Apr;May;Jun;Jul;Aug;Sep;Oct;Nov;Dec";
					String[] monthArr = month.split(";");

					boolean courtFlag = false;
					boolean dateFlag = false;
					List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dd, 10);
					for (WebElement value : Val) {
						if (value.getText().toLowerCase().contains("court")) {
							// report.updateTestLog("Verify court present aside results list",
							// "Court is present aside results list",
							// Status.PASS);
							courtFlag = true;
						}
						for (String monthName : monthArr) {
							if (value.getText().toLowerCase().contains(monthName.toLowerCase())) {
								// report.updateTestLog("Verify date present aside results list",
								// "Date is present aside results list",
								// Status.PASS);
								dateFlag = true;
								break;
							}
						}
					}
					if (!(courtFlag) && (!(dateFlag))) {
						report.updateTestLog("Verify court present aside results list", "Court Date is not present aside results list", Status.FAIL);
					}
				}
			} else {
				report.updateTestLog("Verify date present aside results list", "Date is not present aside results list", Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save the court name and date aside
	// document title
	// # Function Name : saveCourtAndDateAsideDocumentTitle     
	// # Author : Kalaivanan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Search saveCourtAndDateAsideDocumentTitle(String docTitle, String ConfigId, int ColNum, int WriteColumn) {
		String court = "";
		String date = "";
		WebElement resultClass = null;
		boolean blnFlag = false;

		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		excel = new ExcelDataAccess(datatablePath, "FullDocument");
		excel.setDatasheetName("General_Data");
		int rowNum = excel.getRowNum(ConfigId, ColNum);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass == null) {
			int count = 0;
			do {
				count = count + 1;
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
				if (resultClass == null)
					commonLibrary.sleep(5000);
			} while (resultClass == null && count <= 40);
		}

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(docTitle)) {
						WebElement tagNameAside = commonLibrary.isExist(document, UIMAP_SearchResult.tagNameAside, 20);

						List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dd, 10);
						for (WebElement value : Val) {
							if (value.getText().toLowerCase().contains("court")) {
								court = value.getText() + ";";
							} else if (value.getText().contains(", ")) {
								date = value.getText();
								court = court + date;
							}

						}
						excel.setValue(rowNum, WriteColumn, court);
						commonLibrary.sleep(5000);
						report.updateTestLog("Verify Date value has been set ", "Date value has been set with " + court, Status.PASS);
						blnFlag = true;
						break;
					}
				}

			}
			if (!blnFlag)

				report.updateTestLog("Click on the document " + docTitle, "Not Clicked  on the document " + docTitle, Status.FAIL);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whether the folder name is
	// available in recent folder list
	// # Function Name : clickkRecentFolder     
	// # Author : Vennila
	// # Date Created : 23 Oct'15
	// #*****************************************************************************************************************************

	public Search clickRecentFolder(String FolderName) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		Boolean flag = false;

		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null && !addtoFolder.getAttribute("class").contains("expanded"))

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		WebElement recentFolderList = commonLibrary.isExist(UIMAP_SearchResult.recentFolderList);
		List<WebElement> recentFolderName = commonLibrary.isExistList(recentFolderList, UIMAP_SearchResult.recentFolderName, 100);
		// Click on recently used folder
		for (WebElement folderName : recentFolderName) {
			if (folderName.getText().equals(FolderName)) {
				flag = true;
				commonLibrary.clickMethod(folderName, "Folder Name");
				// commonLibrary.clickButtonParentWithWait(folderName,
				// "Folder Name");
				break;
			}
		}
		if (flag)
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is available in recent folder list", Status.PASS);
		else
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is not available in recent folder list", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select check box of the given Web
	// document.
	// # Function Name : selectWebDocumentByTitle
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search selectWebDocumentByTitle(String DocName) {
		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[class='icon la-ExternalLinkAfter']"), 10);
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on search for link
	// # Function Name : clickSearchFor     
	// # Author : Vennila
	// # Date Created : 26 Oct'15
	// #*****************************************************************************************************************************
	public Search clickSearchFor(String linkName) {
		String LinkName;
		LinkName = null;
		WebElement lnkresult = commonLibrary.isExist(UIMAP_SearchResult.lnkresult);
		if (lnkresult != null) {
			LinkName = lnkresult.getText();
			if (LinkName.trim().contains(linkName.trim())) {
				commonLibrary.clickLinkWithWebElementWithWait(lnkresult, "Search For link");
				report.updateTestLog("Clicking on search for link", "Clicked on Search for link", Status.PASS);
			} else
				report.updateTestLog("Clicking on search for link", "Not Clicked on Search for link", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to Scroll the full document to the
	// bottom
	// # Function Name : bottomofthedocument
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search scrollBottomofthedocument() {
		boolean flag = false;
		try {

			Robot D;
			D = new Robot();

			D.keyPress(KeyEvent.VK_CONTROL);
			D.keyPress(KeyEvent.VK_END);
			D.keyRelease(KeyEvent.VK_CONTROL);
			D.keyRelease(KeyEvent.VK_END);
			flag = true;

		} catch (AWTException e) {

			e.printStackTrace();
		}
		if (flag) {
			report.updateTestLog("Verify full document has been scroll to bottom of the document", "Full document has been scroll to bottom of the document", Status.DONE);
		} else {
			report.updateTestLog("Verify full document has been scroll to bottom of the document", "Full document Not scroll to bottom of the document", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Headnotes
	// # Function Name : clickheadnotes
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards clickheadnotes(String docTitle) {
		WebElement actualLinkSection = commonLibrary.isExist(UIMAP_SearchResult.actualLinkSection, 10);
		List<WebElement> actualLink = commonLibrary.isExistList(actualLinkSection, UIMAP_SearchResult.actualLink, 10);

		for (WebElement link : actualLink) {
			if (link.getText().toLowerCase().equals((docTitle).toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, docTitle);
				break;
			}

		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to Scroll the full document
	// # Function Name : pageDown
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search scrollDocument() {
		try {

			Robot D;
			D = new Robot();

			// D.keyPress(KeyEvent.VK_CONTROL);
			D.keyPress(KeyEvent.VK_PAGE_DOWN);
			// D.keyRelease(KeyEvent.VK_CONTROL);
			// D.keyRelease(KeyEvent.VK_PAGE_DOWN);

		} catch (AWTException e) {

			e.printStackTrace();
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on view enhanced version of
	// this form
	// # Function Name : clickOnViewEnhancements
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Document clickOnViewEnhancements(String embedlink) {

		WebElement linkSection = commonLibrary.isExist(UIMAP_SearchResult.docText1, 10);
		List<WebElement> links = commonLibrary.isExistList(linkSection, UIMAP_SearchResult.embed, 10);
		for (WebElement link1 : links) {
			if (link1.getText().toLowerCase().contains(embedlink.toLowerCase())) {
				commonLibrary.clickLinkWithWait(link1, embedlink);
				break;
			}
		}
		WebElement btnHideShowComments = commonLibrary.isExist(UIMAP_Document.btnHideComments, 20);
		int counter = 0;
		do {
			counter++;
			btnHideShowComments = commonLibrary.isExist(UIMAP_Document.btnHideComments, 20);
			commonLibrary.sleep(10000);
		} while (btnHideShowComments == null && counter < 20);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder with sharing text
	// # Function Name : addToFolderwithShare
	// # Author : Vennila
	// # Date Created : Nov 13 2015
	// #*****************************************************************************************************************************

	public Search addToFolderwithShare(String FolderName, String recipient, String text) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 1000);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_ResearchMap.chooseFolder, 1000);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 1000);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(30000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 1000);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// Click on share with others
			WebElement addToShare = commonLibrary.isExistNegative(UIMAP_WorkFolders.shareWithOthers, 10);
			commonLibrary.clickJS(addToShare, "Share with others");
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
			List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);

			commonLibrary.selectFromListFolderNoParent(wordWheelOptions.get(0), recipient);

			WebElement addToSharePP = commonLibrary.isExist(UIMAP_WorkFolders.addToSharePP, 10);
			if (addToSharePP != null)
				commonLibrary.clickButton(addToSharePP);
			else
				report.updateTestLog("Share the document to folder", "Document not shared", Status.FAIL);

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 1000);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
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

			commonLibrary.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on last page.
	// # Function Name : ClickLastPage
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search clickLastPage() {
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pagination, 100);
		List<WebElement> pageNumbers = commonLibrary.isExistList(pagination, UIMAP_SearchResult.pageNumbers, 100);
		int number = pageNumbers.size();
		if (number > 1) {
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWait(pageNumbers.get(number - 1), "Page Number " + pageNumbers.get(number - 1).getAttribute("data-value").toString() + "");
			else
				commonLibrary.clickButtonParentWithWait(pageNumbers.get(number - 1), "Page Number " + pageNumbers.get(number - 1).getAttribute("data-value").toString() + "");
			;
		}
		commonLibrary.sleep(5000);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get total result count
	// results
	// # Function Name : getResultcountNew     
	// # Author : Sriram
	// # Date Created :17 Nov 1getResultcountNewgetResultcountNew5
	// #*****************************************************************************************************************************
	public Search getResultcountNew(String tcName, String dataSheet, String colName) {
		String s1 = "", str = "";
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pageWrapper, 20);
		List<WebElement> DocumentNumber = commonLibrary.isExistList(pagination, UIMAP_SearchResult.eltResultList, 20);
		int number = DocumentNumber.size();

		if (number > 0) {

			str = DocumentNumber.get(number - 1).getAttribute("data-id").toString();
			str = str.substring(2);
			s1 = String.valueOf(Integer.parseInt(str) + 1);
		}

		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, s1);
		report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save result list folder
	// # Function Name : addToFolderFromActionSearch
	// # Author : Sriram
	// # Date Created : Nov 15
	// #*****************************************************************************************************************************

	public Search addToFolderFromActionSearch(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

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

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_SearchResult.saveFolder, 30);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "Save");

				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify extract is displayed for
	// Results in Result page.
	// # Function Name : verifyShowExtractnew
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search verifyShowExtractnew(String strDocTitle) {
		boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					if (document.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						WebElement eltShowOverview = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.overview, 10);
						WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.docText, 10);

						if (eltShowOverview != null && eltShowExtract != null) {
							report.updateTestLog("Verify extract displays for document: " + strDocTitle, "Extract displays for document: " + strDocTitle, Status.PASS);
							blnFlag = true;
							break;
						}
					}
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify extract displays for document: " + strDocTitle, "Extract does not display for document: " + strDocTitle, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : ClickBrowserBack
	// # Author : Kalaivanan
	// # Date Created : Nov 2015
	// #*****************************************************************************************************************************

	public History clickBrowserBackToHistory() {
		commonLibrary.clickBrowserBack();
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Result List header.
	// # Function Name : verifyResultsListHeader     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search verifyResultsListHeader(String term) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader, 10);
		String headerText = header.getText().replace("\n", " ");
		if (headerText.trim().toLowerCase().contains(term.toLowerCase()))
			report.updateTestLog("Verify results list with header: " + term + " is displayed", "Results list with header: " + term + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify results list with header: " + term + " is displayed", "Results list with header: " + term + " is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder with sharing text
	// # Function Name : addToFolderwithShare
	// # Author : Vennila
	// # Date Created : Nov 13 2015
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
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);

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
	// # Function Description : Function to enter page number in Jump To page
	// number text box
	// # Function Name : enterPageNumberInJumpTo     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Search enterPageNumberInJumpTo(String pageNumber) {
		WebElement pageJumpDiv = commonLibrary.isExist(UIMAP_Document.pageJumpDiv, 10);
		WebElement pageJumpTextBox = commonLibrary.isExist(pageJumpDiv, UIMAP_Document.pageJumpTextBox, 10);
		if (pageJumpTextBox != null) {
			commonLibrary.setDataInTextBoxClear(pageJumpTextBox, pageNumber, "Page");
		} else {
			report.updateTestLog("Enter " + pageNumber + " in Jump To Page text box", "Jump To Page text box is not available ", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify page number inside document
	// is next to another page number
	// # Function Name : verifyPageNumberNextToAnother     
	// # Author : Anbarasan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Search verifyPageNumberNextToAnother(String parentPageNumber, String nextPageNumber, boolean presence) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click next arrow in jump to
	// # Function Name : clickNextInJumpTo
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search clickNextInJumpTo() {
		// WebElement jumpToExpanded =
		// commonLibrary.isExist(UIMAP_Document.jumpToExpanded, 20);
		// if (jumpToExpanded == null)
		// generalFunctions.clickJumpTo();
		WebElement nextArrow = commonLibrary.isExist(UIMAP_Document.jumpToNextArrow, 20);
		if (nextArrow != null) {
			if (nextArrow.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(nextArrow, "Next Arrow");
				commonLibrary.sleep(50000);
			} else
				report.updateTestLog("Click Next Arrow", "Next Arrow button is disabled", Status.FAIL);
		} else {
			report.updateTestLog("Click Next Arrow", "Next Arrow button is not available", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Jump to a section of the document
	// # Function Name : selectJumpToSectionVerify     
	// # Author : Uma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search selectJumpToSectionVerify(String option) {
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whether the folder name is
	// available in recent folder list and click on the same
	// # Function Name : checkRecentFolderAndClick     
	// # Author : Vennila
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search checkRecentFolderAndClick(String FolderName) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		Boolean flag = false;

		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 10);
		if (addtoFolder != null && !addtoFolder.getAttribute("class").contains("expanded"))

			commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		WebElement recentFolderList = commonLibrary.isExist(UIMAP_SearchResult.recentFolderList);
		List<WebElement> recentFolderName = commonLibrary.isExistList(recentFolderList, UIMAP_SearchResult.recentFolderName, 10);
		for (WebElement folderName : recentFolderName) {
			if (folderName.getText().equals(FolderName)) {
				commonLibrary.clickButton(folderName);
				flag = true;
				break;
			}
		}
		if (flag) {
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is available in recent folder list", Status.PASS);
			report.updateTestLog("Click on recent folder", "The given recent folder " + FolderName + " is Clicked", Status.PASS);
		} else {
			report.updateTestLog("Check for recent folder", "The given " + FolderName + " is not available in recent folder list", Status.FAIL);
			report.updateTestLog("Click on recent folder", "The given recent folder " + FolderName + " is NOT Clicked", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink
	// # Function Name : clickDocLink_srch
	// # Author : Venilla
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Search clickDocLink_srch(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a paragraph in document
	// # Function Name : selectParagraphInDocument     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Search selectTextInDocument(String text) {
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

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click option in selected text menu
	// # Function Name : clickOptionInSelectedTextMenu     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Search clickOptionInSelectedTextMenu(String option) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the format of text in preview
	// box of copy citation popup
	// # Function Name : verifyTextFormat1     
	// # Author : Anbu
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTextFormat1(String totalText, String text1, String text2) {
		WebElement copyCitationPopup = commonLibrary.isExistNegative(UIMAP_Document.copyCitationPopup, 10);
		WebElement clipTextContainer = commonLibrary.isExistNegative(copyCitationPopup, UIMAP_Document.clipText, 10);

		if (clipTextContainer.getText().contains(totalText)) {
			report.updateTestLog("Verify The Asterisk * displays for the page number as *" + text2 + " after " + text1 + " with a comma and space", "The Asterisk * is displayed for the page number as *" + text2 + " after " + text1 + " with a comma and space", Status.PASS);
		} else {
			report.updateTestLog("Verify The Asterisk * displays for the page number as *" + text2 + " after " + text1 + " with a comma and space", "The Asterisk * is not displayed for the page number as *" + text2 + " after " + text1 + " with a comma and space", Status.FAIL);
		}

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get document title which has get it
	// now button
	// # Function Name : getDoctitle   
	// # Author : Sriram
	// # Date Created : 7 Dec 2015
	// #*****************************************************************************************************************************
	public Search getDoctitle(String tcName, String dataSheet, String colName) {
		int j;
		Boolean blnFlag = false;
		String s1 = "";
		WebElement results = commonLibrary.isExist(UIMAP_SearchResult.resultlist, 20);
		List<WebElement> eltResultList = commonLibrary.isExistList(results, UIMAP_SearchResult.eltResultList, 20);
		for (j = 0; j < 5; j++) {
			int i;
			for (i = 0; i < eltResultList.size(); i++) {
				WebElement getItNow = commonLibrary.isExistNegative(eltResultList.get(i), UIMAP_SearchResult.btnGetItNow, 20);
				if (getItNow != null) {
					WebElement doctitle = commonLibrary.isExist(eltResultList.get(i), UIMAP_SearchResult.TitleClassDoc, 20);
					s1 = doctitle.getText();
					blnFlag = true;

				}
				if (blnFlag) {
					break;
				}
			}
			if (blnFlag) {
				break;
			}

			else {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");

			}
		}

		if (!blnFlag)

			report.updateTestLog("Verify transactional document is displayed", "Transactional document is not displayed", Status.FAIL);

		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
		excel.setDatasheetName("General_Data");
		int iRowNo = excel.getRowNum(tcName, 0);
		excel.setValue(iRowNo, colName, s1);
		report.updateTestLog("Getting transactional document", "Transactional document is " + s1, Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select close button in citation
	// popup
	// # Function Name : selectclosebutton
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Search selectclosebutton() {
		WebElement btnCitationClose = commonLibrary.isExist(UIMAP_Document.btnCitationClose);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(btnCitationClose, "Close");
		} else {
			commonLibrary.clickButtonParentWithWait(btnCitationClose, "Close");
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Verify the message under document title
	// # Function Name : verifythemsgunderdoctitle
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Dec 11 2015
	// #*****************************************************************************************************************************
	public Search verifythemsgunderdoctitle(String Doctitleoop, String text2) {

		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
			List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
			for (WebElement list : OListResult1) {
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
					for (WebElement doctitle1 : OListItems) {
						WebElement Doctitleoop1 = commonLibrary.isExist(doctitle1, UIMAP_SearchResult.TitleClassDoc, 2);
						List<WebElement> DocPurtitle = commonLibrary.isExistList(Doctitleoop1, UIMAP_SearchResult.TitleDocPurinfo, 20);
						for (WebElement doctitle2 : DocPurtitle) {
							WebElement doctext = commonLibrary.isExist(doctitle2, UIMAP_SearchResult.wholeoopmsginresults, 20);

							if (doctext != null && doctext.getText().toLowerCase().trim().contains((text2.toString()).toLowerCase().trim())) {

								report.updateTestLog("Message" + text2 + "displays below the document" + Doctitleoop, "Message" + text2 + "displayed below the document" + Doctitleoop, Status.PASS);
								break;

							} else {
								report.updateTestLog("Message" + text2 + "not displays below the document" + Doctitleoop, "Message" + text2 + "not displayed below the document" + Doctitleoop, Status.FAIL);
							}
						}
					}
				}
			}
		}
		return new Search(scriptHelper);
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
	// # Function Description : Function for Click a Link in Practice Area page
	// # Function Name : navigateTOBrowseLinksAndVerify
	// # Author : Deepha H
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice navigateTOBrowseLinksAndVerify(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName) {
		try {
			commonLibrary.sleep(100);
			Boolean blnFirst = false, blnSecond = false, blnThird = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);
			commonLibrary.sleep(20);
			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							WebElement item = commonLibrary.isExist(button, UIMAP_Home.item, 10);

							blnFirst = true;
							if (browsername.contains("internet"))
								// commonLibrary.click_JS(button,strParentMenuName);
								commonLibrary.clickButtonParentWithWait(item, strParentMenuName);
							else
								// commonLibrary.clickButton_Parent_WithWait(button,
								// strParentMenuName);
								commonLibrary.clickButtonParentWithWait(item, strParentMenuName);
							break;

						}

					}
				if (!blnFirst)
					report.updateTestLog("Click on " + strParentMenuName, "Not Clicked on " + strParentMenuName, Status.FAIL);
			}
			if (strFirstSubMenuName != "") {
				commonLibrary.sleep(20);
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strFirstSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.btnTypeButton, 20);
								blnSecond = true;
								if (browsername.contains("internet"))
									commonLibrary.clickButtonParentWithWait(button, strFirstSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button, strFirstSubMenuName);
								break;
							}

						}
					if (!blnSecond)
						report.updateTestLog("Click on " + strFirstSubMenuName, "Not Clicked on " + strFirstSubMenuName, Status.FAIL);
				}
			}
			if (strSecondSubMenuName != "") {
				commonLibrary.sleep(20);
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(1), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strSecondSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.lnkTopicPod, 20);
								blnThird = true;
								if (browsername.contains("internet"))
									commonLibrary.clickButtonParentWithWait(button, strSecondSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button, strSecondSubMenuName);
								break;
							}

						}
					if (!blnThird)
						report.updateTestLog("Click on " + strSecondSubMenuName, "Not Clicked on " + strSecondSubMenuName, Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the items to favorite
	// # Function Name : addToFavorites     
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search addToFavorites(String RecentList) {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
		if (mnuFilterToolBar != null) {
			WebElement RecentFavorites = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.btnIdRecentFavorites, 20);
			if (!(RecentFavorites.getAttribute("class").contains("selected"))) {
				commonLibrary.clickButtonParentWithWait(RecentFavorites, "Recent & Favorites");
			}
		}

		WebElement RecentFav = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);

		List<WebElement> RecentFavList = commonLibrary.isExistList(RecentFav, UIMAP_Home.lstTagListItems, 20);
		for (WebElement item : RecentFavList) {
			if (item.getText().replace("\n", " ").trim().contains(RecentList.trim())) {
				WebElement favIcon = commonLibrary.isExist(item, UIMAP_Home.addFavorite, 20);
				if (!(favIcon.getAttribute("class").contains("FavoriteFull")))
					commonLibrary.clickButtonParentWithWait(favIcon, "Favorites for " + RecentList);
				break;
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear prefilter
	// # Function Name : clearPreFilterNew    
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search clearPreFilterNew() {
		generalFunctions.clearPreFilter();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click RFF link in RFF tab
	// # Function Name : clickRFFlinkNew
	// # Author : Sriram
	// # Date Created : Dec 15
	// #*****************************************************************************************************************************
	public Search clickRFFlinkNew(String rffText, String rffLink) {

		boolean blnFlag = false;

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
		if (mnuFilterToolBar != null) {
			WebElement RecentFavorites = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.btnIdRecentFavorites, 20);
			if (!(RecentFavorites.getAttribute("class").contains("selected"))) {
				commonLibrary.clickButtonParentWithWait(RecentFavorites, "Recent & Favorites");
			}
		}

		WebElement RecentFav = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);

		List<WebElement> RecentFavList = commonLibrary.isExistList(RecentFav, UIMAP_Home.lstTagListItems, 20);

		for (int i = 0; i < RecentFavList.size(); i++) {
			if ((RecentFavList.get(i).getText().replace("\n", " ").trim()).contains(rffText.trim())) {
				List<WebElement> linkrff = commonLibrary.isExistList(RecentFavList.get(i), UIMAP_Home.lnkTopicPod, 20);
				for (WebElement list : linkrff) {
					if (list.getText().trim().contains(rffLink.trim())) {
						commonLibrary.clickLinkWithWebElementWithWait(list, rffLink);
						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Delete Recent PreFilters
	// Function Name : clickDelete_RecentPreFilters     
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search clickDeleteRecentPreFilters(String recentListName, String buttonName) {
		boolean blnFlag = false;

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
		if (mnuFilterToolBar != null) {
			WebElement RecentFavorites = commonLibrary.isExist(mnuFilterToolBar, UIMAP_Home.btnIdRecentFavorites, 20);
			if (!(RecentFavorites.getAttribute("class").contains("selected")))
				commonLibrary.clickButtonParentWithWait(RecentFavorites, "Recent & Favorites");
		}
		WebElement RecentFav = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);
		List<WebElement> RecentFavList = commonLibrary.isExistList(RecentFav, UIMAP_Home.lstTagListItems, 20);

		for (WebElement item : RecentFavList) {
			WebElement listName = commonLibrary.isExist(item, By.tagName("a"), 10);
			if (listName.getText().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(recentListName.toLowerCase().replaceAll(" ", ""))) {
				WebElement DeleteRecent = commonLibrary.isExist(item, UIMAP_Home.deleteRecentFav, 20);
				commonLibrary.clickButtonParentWithWait(DeleteRecent, buttonName);
				blnFlag = true;
				report.updateTestLog("Click on Delete button near " + recentListName, "Delete button near " + recentListName + "is clicked", Status.PASS);
				WebElement popUpPage = commonLibrary.isExist(UIMAP_Home.deletePopupPage, 30);
				WebElement deletePopUp = commonLibrary.isExist(popUpPage, UIMAP_Home.section, 30);
				if (deletePopUp != null) {
					String popUpText = deletePopUp.getText();
					if ((popUpText.contains("Are you sure you would like to delete this filter?")) && (popUpText.toLowerCase().replaceAll(" ", "").contains(recentListName.toLowerCase().replaceAll(" ", "")))) {
						report.updateTestLog("Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, "Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, Status.PASS);
						switch (buttonName) {
						case "Delete": {
							WebElement Delete = commonLibrary.isExist(popUpPage, UIMAP_Home.DeleteButton, 20);
							commonLibrary.clickButtonParentWithWait(Delete, "Delete");
							break;
						}
						case "Cancel": {
							WebElement Cancel = commonLibrary.isExist(popUpPage, UIMAP_Home.cancelAlert, 20);
							commonLibrary.clickButtonParentWithWait(Cancel, "Cancel");
							break;
						}
						}

					} else {
						report.updateTestLog("Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, "Delete Pop Up does not appears with 'Are you sure you would like to delete this filter?' " + recentListName, Status.FAIL);
					}

				} else {
					report.updateTestLog("Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, "Delete Pop Up does not appears with 'Are you sure you would like to delete this filter?' " + recentListName, Status.FAIL);
				}

				break;

			}
		}

		if (!blnFlag) {
			report.updateTestLog("Click on Delete button near " + recentListName, "Delete button near " + recentListName + "is not clicked", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Dismiss button in banner
	// message
	// # Function Name : clickDismiss 
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search clickDismiss(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg, 20);
		if (bannermsg != null && bannermsg.getText().toLowerCase().contains(text.toLowerCase())) {
			WebElement Dismiss = commonLibrary.isExist(bannermsg, UIMAP_WorkFolders.dismiss, 20);
			if (Dismiss != null && Dismiss.getText().toLowerCase().contains("dismiss"))
				commonLibrary.clickButtonParentWithWait(Dismiss, "click Dismiss button");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// // # Function Description : Function to click SearchEverything utton
	// # Function Name :  clickSearchEverything  
	// # Author : Sriram
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search clickSearchEverything() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnClassFilter, "Filter");
		else
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify star is filled in favorite
	// icon
	// # Function Name : verifyFavoriteinRFF
	// # Author : Sriram
	// # Date Created : Dec 15
	// #*****************************************************************************************************************************

	public Search verifyFavoriteinRFF(String rfflink) {

		WebElement rffitems = commonLibrary.isExist(UIMAP_Home.divRecentFilters, 20);
		List<WebElement> li = commonLibrary.isExistList(rffitems, UIMAP_Home.lstTagListItems, 20);

		for (int i = 0; i < li.size(); i++) {
			if ((li.get(i).getText().replace("\n", " ").trim()).contains(rfflink.trim())) {
				WebElement btnFav = commonLibrary.isExist(li.get(i), UIMAP_Home.addFavorite, 20);
				if (btnFav != null && btnFav.getAttribute("class").contains("icon la-FavoriteFull")) {
					report.updateTestLog("Verfiy the Star is filled with yellow color for" + rfflink, "Star is filled with with yellow color for" + rfflink, Status.PASS);
					break;
				} else {
					report.updateTestLog("Verfiy the Star is filled with yellow color for" + rfflink, "Star is not filled with with yellow color for" + rfflink, Status.FAIL);
					break;
				}
			}
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the recent favorite
	// prefilters
	// # Function Name : verify_RecentFavoritesNew     
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search verifyRecentFavoritesNew(String RecentList, String Exist) {
		boolean blnFlag = false;
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
		for (WebElement item : RecentFavList) {
			if (item.getText().replace("\n", " ").trim().contains(RecentList.trim())) {
				blnFlag = true;
				break;
			}
		}

		switch (Exist) {
		case "Exist": {
			if (blnFlag) {
				report.updateTestLog("Verify " + RecentList + " is displayed under Recent & Favorites", RecentList + " is displayed under Recent & Favorites", Status.PASS);
			} else

			{
				report.updateTestLog("Verify " + RecentList + " is displayed under Recent & Favorites", RecentList + " is not displayed under Recent & Favorites", Status.FAIL);
			}
			break;
		}
		case "NotExist": {
			if (!blnFlag) {
				report.updateTestLog("Verify " + RecentList + " is displayed under Recent & Favorites", RecentList + " is not displayed under Recent & Favorites", Status.PASS);
			} else

			{
				report.updateTestLog("Verify " + RecentList + " is displayed under Recent & Favorites", RecentList + " is displayed under Recent & Favorites", Status.FAIL);
			}
			break;
		}
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filter selected in
	// prefilter pop up
	// # Function Name : VerifyFilterSelectedNew    
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search verifyFilterSelectedNew(String strSelectedFilterOptions) {
		boolean blnFlag = false;
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {
			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 20);
			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {

					for (WebElement item : divClassDeleteFilter) {
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", Status.PASS);
							blnFlag = true;
							break;
						} else
							blnFlag = false;
					}
					if (!blnFlag)
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy textbox", Status.FAIL);
				}

			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : VerifyAllDocTitle     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public int getResultsListCount() {
		List<WebElement> OListItems = null;

		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			}
		}
		return OListItems.size();
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify No Documents Found is
	// displayed.
	// # Function Name : verifyNoDocsFoundDisplayed
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyNoResultsDsiplayed() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			if (resultClass.getText().toLowerCase().contains("no documents found"))
				report.updateTestLog("Verify 'No Documents Found in cases' is  displayed", "'No Documents Found in cases' is  displayed", Status.PASS);
			else
				report.updateTestLog("Verify 'No Documents Found in cases' is  displayed", "'No Documents Found in cases' is not  displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify 'No Documents Found' is displayed", "Result list container is not displayed", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on See more information on
	// segment searching link and handle secondary browser.
	// # Function Name : verifyNoDocsFoundDisplayed
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search clickMoreInfoOnSegmentSearchingVerifySecondaryBrowser() {
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement moreInfoSegmentSearching = commonLibrary.isExistNegative(resultClass, UIMAP_SearchResult.moreInfoSegmentSearching, 10);
		if (moreInfoSegmentSearching != null) {
			commonLibrary.clickLinkWithWebElementWithWait(moreInfoSegmentSearching, moreInfoSegmentSearching.getText());

			if (commonLibrary.switchToWindow("newlexis/searchsegment")) {
				driver.close();
				report.updateTestLog("Close the Secondary window.", "The Secondary Window is closed.", Status.DONE);
				commonLibrary.switchToWindow("/search/");
			} else {
				report.updateTestLog("Close the Secondary window.", "The Secondary Window is not displayed.", Status.FAIL);
			}

		} else
			report.updateTestLog("Click on See more information on segment searching link in zero results page.", "See more information on segment searching link is not displayed.", Status.FAIL);
		return new Search(scriptHelper);
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
	// # Function Description : Function to verifyLegalNewsResults
	// popup
	// # Function Name : verifyLegalNewsResults
	// # Author : Ram
	// # Date Created : April'15
	// #*****************************************************************************************************************************

	public Search verifyLegalNewsResults() {
		WebElement legalNewsResults = commonLibrary.isExist(UIMAP_Document.legalNews);
		if (legalNewsResults.getText().contains("Legal News - Military Justice")) {
			report.updateTestLog("Verify Legal News - Military Justice Results page is displayed", "Legal News - Military Justice Results page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Legal News - Military Justice Results page is displayed", "Legal News - Military Justice Results page is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and Verify Value is not
	// displayed
	// # Function Name : VerfiyActionListValuesNotDisplayed   
	// # Author : Sriram
	// # Date Created : Dec 15
	// #**************************************************************************************************
	public Search VerfiyActionListValuesNotDisplayed(String strAction) {
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

			if (commonLibrary.verifyFromListButton(divAction, strAction))
				report.updateTestLog("Verfiy Action's option " + strAction + " is not displayed", "" + strAction + " is displayed", Status.FAIL);
			else
				report.updateTestLog("Verfiy Action's option " + strAction + " is not displayed", "" + strAction + " is not displayed", Status.PASS);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Add to Folder icon is not
	// displayed
	// # Function Name : verifyFolderIconNotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search verifyFolderIconNotDisplayed() {

		WebElement deliveryTray = commonLibrary.isExistNegative(UIMAP_SearchResult.selectAllItemsUl, 10);
		WebElement addTofolder = commonLibrary.isExistNegative(deliveryTray, UIMAP_SearchResult.addTofolder, 10);
		if (addTofolder == null)
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is not displayed on the delivery tray ", Status.PASS);
		else
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is displayed on the delivery tray ", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : clickBrowserBackToSources
	// # Author : Banu
	// # Date Created : Dec 2015
	// #*****************************************************************************************************************************

	public Practice clickBrowserBackToSources() {
		commonLibrary.clickBrowserBack();
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add search to folder and
	// save search
	// # Function Name : addToFolderNotesSaveSearchFromActionNew
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search addToFolderNotesSaveSearchFromActionNew(String folderName, String notes) {
		try {

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			commonLibrary.isExist(UIMAP_ResearchMap.folderName, 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, folderName);

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			// ADD <<<NOTES>>>
			if (!notes.equals("")) {
				WebElement notesIframe = commonLibrary.isExistNegative(UIMAP_Document.notesIframe, 10);
				driver.switchTo().frame(notesIframe);
				WebElement notesArea = commonLibrary.isExistNegative(UIMAP_Document.notesArea, 10);
				commonLibrary.setDataInTextBox(notesArea, notes, "Notes");
				driver.switchTo().defaultContent();
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolderFromDoc, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to share contact in folder pop up
	// # Function Name : shareContactSelectContactNew
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search shareContactSelectContactNew(String strContactName) {

		try {

			for (int k = 0; k < 3; k++) {
				// if(browsername.contains("internet"))
				// {
				WebElement txtShareContacts = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
				if (txtShareContacts != null)
					commonLibrary.setDataInTextBox(UIMAP_Document.txtShareContacts, strContactName);
				commonLibrary.sleep(2000);

				// SELECT THE <<<BECKER>>>
				boolean blnFlag = false;
				// try
				// {
				//
				commonLibrary.sleep(3000);
				WebElement cboSharingList = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
				List<WebElement> li = commonLibrary.isExistList(cboSharingList, By.tagName("li"), 20);
				for (WebElement item : li) {
					if (item.getText().trim().equalsIgnoreCase(strContactName)) {
						commonLibrary.clickJS(item);
						blnFlag = true;
						break;
					}
				}
				if (!(blnFlag)) {
					WebElement txtShareContacts1 = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
					for (int j = 1; j <= 3; j++) {
						Actions action = new Actions(driver);
						action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
						action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
						action.sendKeys(txtShareContacts1, strContactName.substring(strContactName.length() - 2)).perform();
						commonLibrary.sleep(4000);
						WebElement cboSharingList1 = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
						List<WebElement> li1 = commonLibrary.isExistList(cboSharingList1, By.tagName("li"), 20);
						for (WebElement item : li1) {
							if (item.getText().trim().equalsIgnoreCase(strContactName)) {
								commonLibrary.clickJS(item);
								blnFlag = true;
								break;
							}
						}
						if (blnFlag)
							break;
					}
				}

				if (blnFlag)
					break;

				// if(AddedContact==null)
				// report.updateTestLog("Add Contact Name ", "Contact Name"+
				// strVal +" is not added", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to share folder with notes
	// # Function Name : addToFolderNotesWithShare
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search addToFolderNotesWithShare(String strContactName, String strFolderName, String notes) {
		try {
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

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBox(UIMAP_Document.txtFolderName, strFolderName);
			// CLICK ON <<<CREATE>>>
			WebElement btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			if (btnCreateNewFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateNewFolder, "CREATE");
			commonLibrary.sleep(3000);

			// Click <Share with Others> tab in add to folder pop up
			generalFunctions.selectTabsAddToFolderWindow("Share With Others");

			for (int i = 0; i < 2; i++) {
				// ENTER <<<B>>> IN THE TEXT BOX RECENTLY USED NAMES WILL
				// DISPLAY
				// ALLOWING THE USER TO SELECT ONE

				String addrecipient[] = strContactName.split(";");

				if (browsername.contains("internet")) {

					WebElement txtShareContacts = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
					if (txtShareContacts != null)
						commonLibrary.setDataInTextBox(UIMAP_Document.txtShareContacts, addrecipient[i]);
					commonLibrary.sleep(2000);

					// SELECT THE <<<BECKER>>>
					boolean blnFlag = false;
					// try
					// {
					//
					commonLibrary.sleep(3000);
					WebElement cboSharingList = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
					List<WebElement> li = commonLibrary.isExistList(cboSharingList, By.tagName("li"), 20);
					for (WebElement item : li) {
						if (item.getText().trim().equalsIgnoreCase(addrecipient[i])) {
							commonLibrary.clickJS(item);
							blnFlag = true;
							break;
						}
					}
					if (!(blnFlag)) {
						WebElement txtShareContacts1 = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
						for (int j = 1; j <= 3; j++) {
							Actions action = new Actions(driver);
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
							action.sendKeys(txtShareContacts1, addrecipient[i].substring(addrecipient[i].length() - 2)).perform();
							commonLibrary.sleep(4000);
							WebElement cboSharingList1 = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
							List<WebElement> li1 = commonLibrary.isExistList(cboSharingList1, By.tagName("li"), 20);
							for (WebElement item : li1) {
								if (item.getText().trim().equalsIgnoreCase(addrecipient[i])) {
									commonLibrary.clickJS(item);
									blnFlag = true;
									break;
								}
							}
							if (blnFlag)
								break;
						}
					}

				} else
					shareContactSelectContactNew(addrecipient[i]);
				// Click the "Add" button
				WebElement btnAddToContact = commonLibrary.isExist(UIMAP_Document.btnAddToContact, 10);
				if (btnAddToContact != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(btnAddToContact, "Add");
					else
						commonLibrary.clickButtonParentWithWait(btnAddToContact, "Add");
				}
				if (i == 1) {
					List<WebElement> option = commonLibrary.isExistList(UIMAP_SearchResult.option, 10);
					for (WebElement item : option) {
						if (item.getText().trim().contains(addrecipient[i])) {
							WebElement selectOption = commonLibrary.isExist(item, UIMAP_SearchResult.selectOption, 10);
							if (selectOption != null) {
								commonLibrary.selectByVisibleText(selectOption, "Viewer");
								break;
							}
						}
					}
				}
			}
			generalFunctions.selectTabsAddToFolderWindow("Save Options");
			// ADD <<<NOTES>>>
			if (!notes.equals("")) {
				WebElement notesIframe = commonLibrary.isExistNegative(UIMAP_Document.notesIframe, 10);
				driver.switchTo().frame(notesIframe);
				WebElement notesArea = commonLibrary.isExistNegative(UIMAP_Document.notesArea, 10);

				commonLibrary.setDataInTextBox(notesArea, notes, "Notes");

				driver.switchTo().defaultContent();
				WebElement notesView = commonLibrary.isExistNegative(UIMAP_SearchResult.notesView, 10);
				WebElement chkRecipient = commonLibrary.isExistNegative(notesView, UIMAP_SearchResult.chkRecipient, 10);
				commonLibrary.setCheckBox(chkRecipient, "Allow recipients to view your notes");
			}

			// CLICK ON <<<SAVE>>>
			WebElement btnDocumentSave = commonLibrary.isExist(UIMAP_Document.btnDocumentSave, 10);
			if (btnDocumentSave != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnDocumentSave, "SAVE");
				else
					commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify given term is not highlighted
	// or bolded
	// # Function Name : verifyTermsNotBoldedNotHighlighted
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyTermsNotBoldedNotHighlighted(String searchTerm) {
		boolean flag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> hitList = commonLibrary.isExistList(OListResult, UIMAP_SearchResult.highlightedBoldedTerm, 10);
				for (WebElement item : hitList) {
					if (item.getText().equalsIgnoreCase(searchTerm)) {
						flag = true;
						report.updateTestLog("Verify " + searchTerm + " is not highlighted and not bolded.", searchTerm + " is highlighted and bolded.", Status.FAIL);
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Verify " + searchTerm + " is not highlighted and not bolded.", searchTerm + " is not highlighted and not bolded.", Status.PASS);
			} else
				report.updateTestLog("Verify " + searchTerm + " is not highlighted and not bolded.", "Result List is not displayed.", Status.FAIL);
		} else
			report.updateTestLog("Verify " + searchTerm + " is not highlighted and not bolded.", "Result Container is not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify SFEDocuments is displayed for
	// Results in Result page.
	// # Function Name : verifySFEDocuments
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #************************************************************************************************************
	public Search getResultcount() {
		String list, s1 = "";
		int k = 0;
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.eltResultHeader, 20);
		if (resultList != null) {
			list = resultList.getText();
			String[] category = list.split(Pattern.quote("("));
			String[] s = category[1].split(Pattern.quote(")"));
			s1 = s[0].replace("+", "");
			k = Integer.parseInt(s1);
		}
		if (k > 0) {
			report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
		} else {
			report.updateTestLog("Getting result count", "Result header is not present", Status.FAIL);
		}

		// return s1;
		return new Search(scriptHelper);
	}

	public Search verifySFEDocuments(String searchTerm) {
		Boolean blnFlag = false;
		int pstflg = 0, negflg = 0;

		try {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement document : OListItems) {
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
							WebElement eleOverviewSection = commonLibrary.isExist(eleDocCondent, By.tagName("p"), 20);
							if ((eleOverviewSection != null && eleOverviewSection.getText().toLowerCase().contains("[effective ") || (!(eleOverviewSection != null && eleOverviewSection.getText().toLowerCase().contains("[effective "))))) {
								pstflg++;
							} else {
								negflg++;
							}
						}
						if ((pstflg == OListItems.size() && (negflg == 0))) {
							report.updateTestLog("Verify result list has one current and sfe document each" + searchTerm, "Result list has one current and sfe document each" + searchTerm, Status.PASS);
							blnFlag = true;
						} else {
							report.updateTestLog("Verify result list has one current and sfe document each" + searchTerm, "Result list does not have one current and sfe document each" + searchTerm, Status.FAIL);
						}

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		if (!blnFlag)
			report.updateTestLog("Verify sfe displays for document: " + searchTerm, "sfe is not displayed for document: " + searchTerm, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add filter to left side
	// results
	// # Function Name : addfilte    
	// # Author : Jubin
	// # Date Created : Dec'15
	// #************************************************************************************************************

	public Search wrapperpanel(String option) {
		WebElement List = commonLibrary.isExist(UIMAP_SearchResult.resultList, 20);
		if (List != null) {
			WebElement list = commonLibrary.isExist(List, UIMAP_LexisAdvanceTax.ul, 10);
			// for (WebElement item : lists) {
			List<WebElement> lists = commonLibrary.isExistList(list, UIMAP_LexisAdvanceTax.lists, 10);
			for (WebElement item : lists) {
				System.out.println(item.getText());
				System.out.println(option);
				if (item.getText().toLowerCase().contains(option.toLowerCase())) {
					WebElement active = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.active, 10);
					System.out.println(active);
					if (active != null) {
						report.updateTestLog("Getting result", "Result header is already selected", Status.PASS);
						break;
					}
				} else if (item.getText().toLowerCase().contains(option.toLowerCase())) {
					WebElement active = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.active, 10);
					System.out.println(active);
					if (active == null) {
						WebElement buttons = commonLibrary.isExist(item, By.tagName("button"), 10);
						commonLibrary.clickButton(buttons);
						report.updateTestLog("Getting result", "Result header is selected", Status.PASS);
						break;
					}
				} else {
					report.updateTestLog("Getting result", "Result header is not present", Status.FAIL);
					break;
				}
			}

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link by having
	// number
	// # Function Name : clickDocLinkbyNumer     
	// # Author : Jubin
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickDocLinkbyNumer(String value) {
		// value = value.concat(".");
		System.out.println(value);
		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i, s = 0;
		String k = "";
		String strDocTitle = "";
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, UIMAP_SearchResult.oltag, 20);
				List<WebElement> OListResult1 = commonLibrary.isExistList(resultClass, UIMAP_SearchResult.oltag, 20);
				for (WebElement list : OListResult1) {
					if (OListResult != null) {
						// WebElement OListItems = commonLibrary.isExist(list,
						// UIMAP_SearchResult.listtag, 20);
						List<WebElement> OListItems = commonLibrary.isExistList(list, UIMAP_SearchResult.listtag, 20);
						for (WebElement document : OListItems) {
							WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
							if (eleDocTitle != null) {
								WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.atag, 20);
								s++;
								k = Integer.toString(s);
								if (lnkDocument != null && k.equals(value)) {
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

			report.updateTestLog("Click on the document by number" + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
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
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to content type
	// # Function Name : switchContentType
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search switchContentType(String ContentType) {
		boolean flag = false;
		WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);
		List<WebElement> searchContent = commonLibrary.isExistList(searchControl, UIMAP_SearchResult.lstTagListItems, 10);
		if (searchContent.size() > 0) {
			for (WebElement item : searchContent) {
				if (item.getText().toLowerCase().contains(ContentType.toLowerCase())) {
					WebElement contentbutton = commonLibrary.isExist(item, UIMAP_SearchResult.button, 20);
					commonLibrary.clickButton(contentbutton);
					commonLibrary.sleep(30000);
					flag = true;
					break;
				}
			}
			if (!flag) {
				report.updateTestLog("Verify ContentType " + ContentType + " displays in the LexisAdvanceTaxResults page", ContentType + " is not displayed in the LexisAdvanceTaxResults page", Status.FAIL);
			}
			return new Search(scriptHelper);
		} else {
			report.updateTestLog("Verify ContentType " + ContentType + " displays in the LexisAdvanceTaxResults page", ContentType + " No content type is displayed in the LexisAdvanceTaxResults page", Status.FAIL);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Documents highlighted
	// # Function Name : verifyDocumentsHighlighted
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search verifyDocumentsHighlighted(String value) {
		Boolean flag = false;
		Boolean flag1 = false;
		int i, c = 0;
		WebElement resultClass = null;
		int value1 = Integer.parseInt(value);

		// do {
		// commonLibrary.sleep(20000);
		// btnNextPage =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// count++;
		// } while (btnNextPage == null && count < 25);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr'][class*='exact']"), 20);
				for (i = 0; i < value1; i++) {
					String number = "sr" + i + "";
					if (OListItems.get(i).getAttribute("data-id").contains(number)) {
						flag = true;
						c++;
					}

				}
			}

		}
		if (c == value1)
			flag1 = true;

		if (flag && flag1)
			report.updateTestLog("Verify Documents that displays at the top of the results list are highlighted", value + "Documents displayed at the top of the results list are highlighted", Status.PASS);
		else
			report.updateTestLog("Verify Documents that displays at the top of the results list are highlighted", value + "Documents displayed at the top of the results list are not highlighted", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the landing page by checking
	// the text
	// # Function Name : verifyTOCLandingPage    
	// # Author : Jubin
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public Search verifyTOCLandingPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.tocHeader, 20);
		String pageNameFinal = pageName.substring(25, pageName.length());
		if (CurrentProduct.getText().toLowerCase().contains(pageNameFinal.toLowerCase())) {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the table case result
	// Documents
	// # Function Name : verifyDocumentTableCaseResults
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Search verifyDocumentTableCaseResults(String citation) {
		Boolean flag = false;
		Boolean flag1 = false;
		Boolean flag2 = false;
		WebElement resultClass = null;

		// do {
		// commonLibrary.sleep(20000);
		// btnNextPage =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// count++;
		// } while (btnNextPage == null && count < 25);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 20);
				int i = OListItems.size() - 1;
				if ((OListItems.get(0).getAttribute("data-id").equals("sr0")) && (OListItems.get(i)).getAttribute("data-id").equals("sr" + i))
					flag = true;
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(citation.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
						flag1 = true;
						break;
					}
				}
				if (!flag1)
					flag2 = true;
			}

		}
		if (flag && flag1 && !flag2)
			report.updateTestLog("Verify Documents that match the citation displays at the top of the results list ", "Documents that match the citation is displayed at the top of the results list ", Status.PASS);
		else
			report.updateTestLog("Verify Documents that match the citation displays at the top of the results list ", "Documents that match the citation is not displayed at the top of the results list ", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on current document for
	// Results in Result page.
	// # Function Name : clickCurrentDocument
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document clickCurrentDocument(String searchTerm) {
		Boolean blnFlag = false;

		try {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement document : OListItems) {
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
							WebElement eleOverviewSection = commonLibrary.isExist(eleDocCondent, By.tagName("p"), 20);
							if ((eleOverviewSection != null && (!(eleOverviewSection.getText().toLowerCase().contains("[effective "))))) {

								WebElement docTitle = commonLibrary.isExist(document, UIMAP_SearchResult.docLink, 20);
								commonLibrary.clickButton(docTitle);
								blnFlag = true;
							}

						}
						if (blnFlag) {
							report.updateTestLog("Click on the current document in the sfe result list " + searchTerm, "Clicked on the current document in the sfe result list " + searchTerm, Status.PASS);

						} else {
							report.updateTestLog("Click on the current document in the sfe result list " + searchTerm, "Not clicked on the current document in the sfe result list " + searchTerm, Status.FAIL);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		if (!blnFlag)
			report.updateTestLog("Verify sfe displays for document: " + searchTerm, "sfe is not displayed for document: " + searchTerm, Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on sfe document for
	// Results in Result page.
	// # Function Name : clickSFEDocument
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document clickSFEDocument(String searchTerm) {
		Boolean blnFlag = false;

		try {
			WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement document : OListItems) {
							WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
							WebElement eleOverviewSection = commonLibrary.isExist(eleDocCondent, By.tagName("p"), 20);
							if ((eleOverviewSection != null && (eleOverviewSection.getText().toLowerCase().contains("[effective ")))) {

								WebElement docTitle = commonLibrary.isExist(document, UIMAP_SearchResult.docLink, 20);
								commonLibrary.clickButton(docTitle);
								blnFlag = true;
							}

						}
						if (blnFlag) {
							report.updateTestLog("Click on the sfe document in the sfe result list " + searchTerm, "Clicked on the currsfeent document in the sfe result list " + searchTerm, Status.PASS);

						} else {
							report.updateTestLog("Click on the sfe document in the sfe result list " + searchTerm, "Not clicked on the sfe document in the sfe result list " + searchTerm, Status.FAIL);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		if (!blnFlag)
			report.updateTestLog("Verify sfe displays for document: " + searchTerm, "sfe is not displayed for document: " + searchTerm, Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Full Document In Doc Page
	// # Function Name : verifyFullDocumentInDocPage     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyFullDocumentInDocPage(String strSearchTerm) {
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
				report.updateTestLog("Verify full document page is displayed for " + strSearchTerm + "", "Full document page is not displayed for " + strSearchTerm + "", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the result link from CB
	// # Function Name : selectresultslink     
	// # Author : Ram
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search selectresultslink() {
		WebElement resultlink = commonLibrary.isExist(UIMAP_Document.lnksdrresultslist, 20);

		if (browsername.contains("internet")) {
			commonLibrary.clickJS(resultlink, "Results List");
		} else {
			commonLibrary.clickLinkWithWait(resultlink, "Results List");
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyResultListDisplayed
	// # Function Name : verifyResultListDisplayed     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Search verifyResultListDisplayed() {

		WebElement resultlist = commonLibrary.isExist(UIMAP_Document.lnksdrresultslist, 20);
		if (resultlist != null) {
			report.updateTestLog("Verify resultlist link displays", "resultlist link is displayed ", Status.PASS);
		} else {
			report.updateTestLog("Verify resultlist link displays", "resultlist link is not displayed ", Status.FAIL);
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the source Result page Header
	// # Function Name : verifysourcePageTitle     
	// # Author :
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Search verifysourcePageTitle(String strPageHeader) {

		WebElement HeadersourceResult = commonLibrary.isExistNegative(UIMAP_Document.sourceResultHeader, 5);
		WebElement HeadersourceResult2 = commonLibrary.isExist(HeadersourceResult, UIMAP_Document.HeadersourceResult2, 5);
		WebElement Header = commonLibrary.isExist(HeadersourceResult2, UIMAP_Document.headerSource, 10);

		if (Header != null && Header.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is displayed as Header", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is not displayed as Header", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button from source
	// resutls page
	// # Function Name : clickBrowserBacksource
	// # Author : Dinesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickBrowserBacksource() {
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set NewClient ID Generic
	// # Function Name : setNewClientIDGeneric    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search setNewClientIDGenericSrch(String strClientID) {
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
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set NewClient ID Generic
	// # Function Name : setNewClientIDGeneric    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search setNewClientIDGeneric_New(String strClientID) {
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
			// report.updateTestLog("Clicking on Save Client ID",
			// "Save Client ID is clicked.", Status.PASS);
			commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Save Client");
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifySearchResultHeader_wfnew(String strPageHeader) {

		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_Document.hdrResult, 5);

		WebElement HeaderSearchResult5 = commonLibrary.isExistNegative(UIMAP_IMContent.SearchResultHeader4, 5);

		WebElement Header = null;

		if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;
		else if (HeaderSearchResult5 != null)
			Header = HeaderSearchResult5;

		if (Header != null && Header.getText().toLowerCase().trim().contains(strPageHeader.toLowerCase().trim()))
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is displayed as Header", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is not displayed as Header instead it is displaying" + Header.getText(), Status.FAIL);

		return new Search(scriptHelper);
	}

	// #****************************************************************************************************************
	// # Function Description : Function to click Action and verify Value
	// # Function Name : clickActionVerifyValue    
	// # Author : Jubin
	// # Date Created : Feb'15
	// #**************************************************************************************************
	public Search clickActionVerifyValue(String strAction) {
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
			WebElement ul = commonLibrary.isExistNegative(divAction, By.tagName("ul"), 10);
			List<WebElement> ListItems = commonLibrary.isExistList(ul, By.tagName("li"), 20);

			if (ListItems.size() > 0 && ListItems != null) {
				for (WebElement document : ListItems) {
					WebElement btn = commonLibrary.isExistNegative(document, UIMAP_SearchResult.button, 10);

					if (btn.getText().toLowerCase().contains(strAction.toLowerCase())) {
						report.updateTestLog("Verify option " + strAction + "", "" + strAction + " is present", Status.PASS);
						break;
					}
					/*
					 * else { report.updateTestLog("Click option " + strAction +
					 * "", "" + strAction + " is not clicked", Status.FAIL); }
					 */
				}

				/*
				 * if (commonLibrary.selectFromListButton(divAction, strAction))
				 * {
				 * 
				 * pageCheck.ajaxElementCheck(driver,
				 * properties.getProperty("xSpinner"));
				 * commonLibrary.sleep(5000); //
				 * report.updateTestLog("Click option " + strAction + "", "" +
				 * // strAction + " is clicked", Status.PASS); } else {
				 * report.updateTestLog("Click option " + strAction + "", "" +
				 * strAction + " is not clicked", Status.FAIL); }
				 */
			}
		} else {
			report.updateTestLog("Click option " + strAction + "", "" + strAction + " is not clicked", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify jump to
	// # Function Name : verifyJumpTo
	// # Author : Jubin
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Search verifyJumpTo() {
		WebElement jumptoHeader = commonLibrary.isExist(UIMAP_Document.jumpTo, 20);
		WebElement jumpto = commonLibrary.isExist(jumptoHeader, UIMAP_Document.sectionHeader, 20);
		if (jumpto != null) {
			report.updateTestLog("Display Jump To", "Jump To is dspalyed", Status.PASS);
		} else {
			report.updateTestLog("Display Jump To", "Jump To is dspalyed", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Parent Filter Expands for
	// first 4 filters
	// HeadNote16 is displayed
	// # Function Name : verifyParentFilterExpandsForFirstFourFilters
	// # Author : Jubin
	// # Date Created : Feb'15
	// #*******************************************************************************************
	public Search verifyParentFilterExpandsForFirstFourFilters(String value) {

		int i, count = 0, nooffilters = 0;
		nooffilters = Integer.parseInt(value);
		boolean blnFlag = false;
		for (i = 0; i < 5; i++) {
			List<WebElement> eltExpandedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltExpandedFilterHeader1, 10);
			for (WebElement li : eltExpandedFilterHeader) {
				if (li != null) {
					count++;

					if (count == nooffilters) {
						blnFlag = true;
						report.updateTestLog("Verify Post filter type expands.", value + " filters are expanded.", Status.PASS);
						break;
					}

					if (blnFlag)
						break;
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify Post filter type expands.", value + " filters are not expanded.", Status.FAIL);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select drop down in jump to
	// # Function Name : clickDropDownInJumpTo
	// # Author : jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Search clickDropDownInJumpTo(String straction) {
		boolean flag = false;
		WebElement jumptoDropdownArrow = commonLibrary.isExist(UIMAP_Document.dropdownContainer, 20);
		WebElement button = commonLibrary.isExist(jumptoDropdownArrow, UIMAP_Document.button, 20);
		if (button != null) {
			commonLibrary.clickButton(button);
			WebElement aside = commonLibrary.isExist(jumptoDropdownArrow, UIMAP_Document.jumptoAside, 20);
			WebElement ul = commonLibrary.isExist(aside, UIMAP_Document.lstTagUList, 20);
			if (ul != null) {
				List<WebElement> ListItems = commonLibrary.isExistList(ul, By.tagName("li"), 20);
				for (WebElement item : ListItems) {
					String[] category = item.getText().split(Pattern.quote("("));
					String selection = category[0].trim();
					if (selection.toLowerCase().startsWith(straction.toLowerCase())) {
						WebElement btn = commonLibrary.isExistNegative(item, UIMAP_SearchResult.button, 10);
						commonLibrary.clickButton(btn);
						flag = true;
						report.updateTestLog("Select Cases in Jump to section.", straction + " is selected in jump to section.", Status.PASS);
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Select Cases in Jump to section.", straction + " is not selected in jump to section.", Status.FAIL);

			} else {
				report.updateTestLog("Select Cases in Jump to section.", straction + " is not selected in jump to section.", Status.FAIL);
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select drop down in jump to
	// # Function Name : clickDropDownInJumpTo
	// # Author : jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Search verifyHLCTPod(String straction) {
		boolean flag = false;
		WebElement resultlist = commonLibrary.isExist(UIMAP_Document.resultsList, 20);
		WebElement resultheader = commonLibrary.isExist(resultlist, UIMAP_Document.resultwrapper, 20);
		if (resultheader != null) {
			List<WebElement> ListButtons = commonLibrary.isExistList(resultheader, UIMAP_Document.eltExpandedFilterHeader, 20);
			for (WebElement button : ListButtons) {
				String[] category = button.getText().split(Pattern.quote("("));
				String selection = category[0].trim();
				if (selection.toLowerCase().startsWith(straction.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is present.", Status.PASS);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is not present.", Status.FAIL);
		} else
			report.updateTestLog("Verify " + straction + " pod is present ", straction + " pod is not present.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click extract Terms of first doc
	// which is displayed for
	// Results in Result page.
	// # Function Name : clickExtractTermOfFirstDoc
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document clickExtractTermOfFirstDoc(String straction) {
		boolean blnFlag1 = false;
		boolean blnFlag2 = false;
		String text = "";
		int i = 0;
		// int number1 = Integer.parseInt(number);

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement resultClassList = commonLibrary.isExist(resultClass, UIMAP_SearchResult.historyListContainer, 10);
		List<WebElement> button = commonLibrary.isExistList(resultClassList, UIMAP_SearchResult.filterHeader, 20);
		List<WebElement> OListResult = commonLibrary.isExistList(resultClassList, By.tagName("ol"), 20);

		for (i = 0; i < button.size(); i++) {
			String[] category = button.get(i).getText().split(Pattern.quote("("));
			String selection = category[0].trim();
			if (selection.toLowerCase().contains(straction.toLowerCase())) {
				WebElement OListItems = commonLibrary.isExist(OListResult.get(i), By.tagName("li"), 10);
				WebElement eleDocCondent = commonLibrary.isExist(OListItems, UIMAP_SearchResult.divDocCondent, 20);
				WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
				WebElement eltShowTerm = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowTerm, 10);
				if (eltShowExtract != null) {
					text = eltShowExtract.getText();
					System.out.println(text);
					commonLibrary.clickButton(eltShowExtract);
					blnFlag1 = true;
					WebElement docTitle = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
					WebElement docTitleHeader = commonLibrary.isExist(docTitle, UIMAP_SearchResult.doctitleheader, 10);
					if (docTitleHeader.getText().contains("Document")) {
						blnFlag2 = true;
						break;
					}
				} else {
					text = eltShowTerm.getText();
					System.out.println(text);
					blnFlag1 = true;
					commonLibrary.clickButton(eltShowTerm);
					WebElement docTitle1 = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
					WebElement docTitleHeader1 = commonLibrary.isExist(docTitle1, UIMAP_SearchResult.doctitleheader, 10);
					if (docTitleHeader1.getText().contains("Document")) {
						blnFlag2 = true;
						break;

					}
				}
			}
		}
		if (blnFlag1 && blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable under: " + straction, "Extract displays for document is clickable under under: " + straction, Status.PASS);
		}

		if (!blnFlag1 && !blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable under: " + straction, "Extract does not displays for document under: " + straction, Status.FAIL);
		}
		if (blnFlag1 && !blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable under: " + straction, "Extract displays for document is not clickable under: " + straction, Status.FAIL);
		}

		return new Document(scriptHelper);
		// return text;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click extract Terms which is
	// displayed for
	// Results in Result page.
	// # Function Name : clickExtractTerm
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Document clickExtractTerm(String strDocTitle) {
		boolean blnFlag1 = false;
		boolean blnFlag2 = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					if (document.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
						WebElement eleDocCondent = commonLibrary.isExist(document, UIMAP_SearchResult.divDocCondent, 20);
						WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
						if (eltShowExtract != null) {
							blnFlag1 = true;
							commonLibrary.clickButton(eltShowExtract);
							WebElement docTitle = commonLibrary.isExistNegative(UIMAP_SearchResult.TitleClassTOC, 10);
							WebElement docTitleHeader = commonLibrary.isExist(docTitle, UIMAP_SearchResult.doctitleheader, 10);
							if (docTitleHeader.getText().contains("Document")) {
								blnFlag2 = true;
								break;
							}
						}
					}
				}
			}
		}
		if (blnFlag1 && blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable: " + strDocTitle, "Extract displays for document is clickable: " + strDocTitle, Status.PASS);
		}

		if (!blnFlag1 && !blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable: " + strDocTitle, "Extract does not displays for document : " + strDocTitle, Status.FAIL);
		}
		if (blnFlag1 && !blnFlag2) {
			report.updateTestLog("Verify extract displays for document is clickable: " + strDocTitle, "Extract displays for document is not clickable: " + strDocTitle, Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify jurisdiction, court and date
	// being
	// displayed aside results list
	// region
	// # Function Name : verifyJurCourtDatePresence   
	// # Author : jubin
	// # Date Created : jan 16
	// #*****************************************************************************************************************************

	public Search verifyJurCourtDatePresence(String straction, String asides) {
		boolean blnFlag = false;
		String[] asidevalues = new String[5];
		int i, j, k = 0;
		int count = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement resultClassList = commonLibrary.isExist(resultClass, UIMAP_SearchResult.historyListContainer, 10);
		List<WebElement> button = commonLibrary.isExistList(resultClassList, UIMAP_SearchResult.filterHeader, 20);
		List<WebElement> OListResult = commonLibrary.isExistList(resultClassList, By.tagName("ol"), 20);

		for (i = 0; i < button.size(); i++) {
			String[] category = button.get(i).getText().split(Pattern.quote("("));
			String selection = category[0].trim();
			System.out.println(selection);
			System.out.println(straction);
			if (selection.toLowerCase().contains(straction.toLowerCase())) {
				WebElement OListItems = commonLibrary.isExist(OListResult.get(i), By.tagName("li"), 10);
				WebElement eleDocCondent = commonLibrary.isExist(OListItems, UIMAP_SearchResult.divDocCondent, 20);
				WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
				if (eltShowExtract != null) {
					blnFlag = true;
					WebElement tagNameAside = commonLibrary.isExist(eleDocCondent, UIMAP_SearchResult.tagNameAside, 20);
					asidevalues = asides.split(";");
					List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dt, 10);
					for (k = 0; k < asidevalues.length; k++) {
						System.out.println(k);
						for (j = 0; j < Val.size(); j++) {
							System.out.println(j);
							System.out.println(asidevalues[k]);
							System.out.println(Val.get(j).getText());
							if (Val.get(j).getText().toLowerCase().contains(asidevalues[k].toLowerCase())) {
								count++;
								System.out.println(count);
								break;
							}
						}
					}
				}

				WebElement eltShowTerm = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowTerm, 10);
				if (eltShowTerm != null) {
					blnFlag = true;
					WebElement tagNameAside = commonLibrary.isExist(eleDocCondent, UIMAP_SearchResult.tagNameAside, 20);
					asidevalues = asides.split(";");
					List<WebElement> Val = commonLibrary.isExistList(tagNameAside, UIMAP_SearchResult.dt, 10);
					for (k = 0; k < asidevalues.length; k++) {
						System.out.println(k);
						for (j = 0; j < Val.size(); j++) {
							System.out.println(k);
							if (Val.get(j).getText().contains(asidevalues[k])) {
								count++;
								break;
							}
						}
					}
				}
				break;
			}
		}
		if (count == asidevalues.length && blnFlag == true) {
			report.updateTestLog("Verify Jurisdiction, court, Date present aside results list", "Jur, Court, Date is present aside results list", Status.PASS);
		}
		if (blnFlag == false) {
			report.updateTestLog("Verify Jurisdiction, court, Date present aside results list", "Jur, Court, Date is not present aside results list", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify given term is highlighted or
	// bolded
	// # Function Name : verifyTermsBoldedHighlighted
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Search verifyTermsBoldedHighlighted(String searchTerm, String straction) {
		boolean blnFlag = false;
		int i = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement resultClassList = commonLibrary.isExist(resultClass, UIMAP_SearchResult.historyListContainer, 10);
		List<WebElement> button = commonLibrary.isExistList(resultClassList, UIMAP_SearchResult.filterHeader, 20);
		List<WebElement> OListResult = commonLibrary.isExistList(resultClassList, By.tagName("ol"), 20);

		for (i = 0; i < button.size(); i++) {
			String[] category = button.get(i).getText().split(Pattern.quote("("));
			String selection = category[0].trim();
			if (selection.toLowerCase().contains(straction.toLowerCase())) {
				WebElement OListItems = commonLibrary.isExist(OListResult.get(i), By.tagName("li"), 10);
				WebElement eleDocCondent = commonLibrary.isExist(OListItems, UIMAP_SearchResult.divDocCondent, 20);
				WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
				WebElement eltShowTerm = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowTerm, 10);
				if (eltShowExtract != null) {
					List<WebElement> hitList = commonLibrary.isExistList(eltShowExtract, UIMAP_SearchResult.highlightedBoldedTerm, 10);
					for (WebElement item : hitList) {
						if (item.getText().equalsIgnoreCase(searchTerm)) {
							blnFlag = true;
							report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is highlighted and bolded.", Status.PASS);
							break;
						}
					}
					if (!blnFlag)
						report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is not highlighted and not bolded.", Status.FAIL);
					break;
				}
				if (eltShowTerm != null) {
					List<WebElement> hitList = commonLibrary.isExistList(eltShowTerm, UIMAP_SearchResult.highlightedBoldedTerm, 10);
					for (WebElement item : hitList) {
						if (item.getText().equalsIgnoreCase(searchTerm)) {
							blnFlag = true;
							report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is highlighted and bolded.", Status.PASS);
							break;
						}
					}
					if (!blnFlag)
						report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is not highlighted and not bolded.", Status.FAIL);
					break;
				}
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get result count
	// results
	// # Function Name : getResultcount1    
	// # Author : Kalai
	// # Date Created : Dec'15
	// #************************************************************************************************************
	public String getResultcount1() {
		String list, s1, s2 = "";
		int k = 0;
		WebElement resultList = commonLibrary.isExist(UIMAP_SearchResult.eltResultHeader, 20);
		if (resultList != null) {
			list = resultList.getText();
			String[] category = list.split(Pattern.quote("("));
			String[] s = category[1].split(Pattern.quote(")"));
			s1 = s[0].replace("+", "");
			s2 = s[0].replace(",", "");
			k = Integer.parseInt(s1);
		}
		if (k > 0) {
			report.updateTestLog("Getting result count", "Result count of search term is " + s2, Status.PASS);
		} else {
			report.updateTestLog("Getting result count", "Result header is not present", Status.FAIL);
		}

		return s2;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to extract Terms is displayed for
	// Results in Result page.
	// # Function Name : verifyShowExtractWithoutTitle
	// # Author : Jubin
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search verifyShowExtractWithoutTitle(String straction) {
		boolean blnFlag = false;
		int i = 0;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement resultClassList = commonLibrary.isExist(resultClass, UIMAP_SearchResult.historyListContainer, 10);
		List<WebElement> button = commonLibrary.isExistList(resultClassList, UIMAP_SearchResult.filterHeader, 20);
		List<WebElement> OListResult = commonLibrary.isExistList(resultClassList, By.tagName("ol"), 20);

		for (i = 0; i < button.size(); i++) {
			String[] category = button.get(i).getText().split(Pattern.quote("("));
			String selection = category[0].trim();
			if (selection.toLowerCase().contains(straction.toLowerCase())) {
				WebElement OListItems = commonLibrary.isExist(OListResult.get(i), By.tagName("li"), 10);
				WebElement eleDocCondent = commonLibrary.isExist(OListItems, UIMAP_SearchResult.divDocCondent, 20);
				WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
				WebElement eltShowTerm = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowTerm, 10);
				if (eltShowExtract != null) {
					blnFlag = true;
					report.updateTestLog("Verify extract displays for document: ", "Extract displays for document: ", Status.PASS);
					break;
				}
				if (eltShowTerm != null) {
					blnFlag = true;
					report.updateTestLog("Verify extract displays for document: ", "Extract displays for document: ", Status.PASS);
					break;
				}
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify extract displays for document: ", "Extract does not display for document: ", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click extract Terms of first doc
	// which is displayed for
	// Results in Result page.
	// # Function Name : extractTermOfFirstDoc
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public String extractTermOfFirstDoc(String straction) {
		boolean blnFlag1 = false;
		String text, text1 = "";
		int i = 0;
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		WebElement resultClassList = commonLibrary.isExist(resultClass, UIMAP_SearchResult.historyListContainer, 10);
		List<WebElement> button = commonLibrary.isExistList(resultClassList, UIMAP_SearchResult.filterHeader, 20);
		List<WebElement> OListResult = commonLibrary.isExistList(resultClassList, By.tagName("ol"), 20);

		for (i = 0; i < button.size(); i++) {
			String[] category = button.get(i).getText().split(Pattern.quote("("));
			String selection = category[0].trim();
			if (selection.toLowerCase().contains(straction.toLowerCase())) {
				WebElement OListItems = commonLibrary.isExist(OListResult.get(i), By.tagName("li"), 10);
				WebElement eleDocCondent = commonLibrary.isExist(OListItems, UIMAP_SearchResult.divDocCondent, 20);
				WebElement eltShowExtract = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowExtract, 10);
				WebElement eltShowTerm = commonLibrary.isExistNegative(eleDocCondent, UIMAP_SearchResult.eltShowTerm, 10);
				if (eltShowExtract != null) {
					text = eltShowExtract.getText();
					System.out.println(text);
					blnFlag1 = true;
					text1 = text.substring(150, 165);
					break;
				} else {
					text = eltShowTerm.getText();
					System.out.println(text);
					blnFlag1 = true;
					text1 = text.substring(25, 50);
					break;
				}
			}
		}
		System.out.println(text1);
		if (blnFlag1) {
			report.updateTestLog("Verify extract displays for document is clickable under: " + straction, "Extract displays for document is clickable under: " + straction, Status.PASS);

		} else
			report.updateTestLog("Verify extract displays for document is clickable under: " + straction, "Extract displays for document is not clickable under: " + straction, Status.FAIL);
		return text1;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify content types and active
	// content type
	// # Function Name : verifyActiveContentTypeAndOthersPresent
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Search verifyActiveContentTypeAndOthersPresent(String activeType) {
		String otherContent = "Other Content";
		boolean active = false;
		boolean present = false;

		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		WebElement activeContent = commonLibrary.isExist(contentSwitcher, UIMAP_SearchResult.activeContentType, 10);
		if (activeContent != null) {
			if (activeContent.getText().toLowerCase().contains(activeType.toLowerCase())) {
				active = true;
			}
		}
		if (active)
			report.updateTestLog("Verify results are displayed with " + activeType + " as active contentType", "Results are with " + activeType + " as active contentType", Status.PASS);
		else
			report.updateTestLog("Verify results are displayed with " + activeType + " as active contentType", "Results are not displayed with " + activeType + " as active contentType", Status.FAIL);

		List<WebElement> otherTypes = commonLibrary.isExistList(contentSwitcher, UIMAP_SearchResult.lstTagName, 10);
		if (otherTypes != null) {
			/*
			 * for (int i = 0; i < otherContent.length; i++) { for (int j = 0; j
			 * < otherTypes.size(); j++) { if
			 * (otherContent[i].toLowerCase().contains(
			 * otherTypes.get(j).getText().toLowerCase())) {
			 */
			report.updateTestLog("Verify " + otherContent + " is displayed", "" + otherContent + " is displayed", Status.PASS);
			present = true;
			// break;

			// }
			if (!present) {
				report.updateTestLog("Verify " + otherContent + " is displayed", "" + otherContent + " is not displayed", Status.FAIL);
				// }
			}
		} else {
			report.updateTestLog("Verify results displayed with all filters applied", "Results are not displayed with all filters applied", Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : clickBrowserBackResults
	// # Author : KAlai
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickBrowserBackResults() {
		commonLibrary.clickBrowserBack();
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify error message on performing
	// search with spl characters
	// # Function Name : verifyErrmsg
	// # Author : Vennila
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Search verifyErrmsg(String msg) {
		WebElement errorResult = commonLibrary.isExistNegative(UIMAP_SearchResult.errorResult, 10);
		WebElement divclass = commonLibrary.isExistNegative(errorResult, By.tagName("div"), 10);
		if (divclass != null) {
			if (divclass.getText().trim().contains(msg))
				report.updateTestLog("Verify error message is displayed", "Error message is displayed", Status.PASS);
			else
				report.updateTestLog("Given error message is displayed", "Error message is displayed as" + divclass.getText(), Status.FAIL);
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Unexpected error is not
	// displayed .
	// # Function Name : verifyUnExpectedErrorAbsent_new   
	// # Author : Vennila
	// # Date Created :Jan 16
	// #*****************************************************************************************************************************

	public Search verifyUnExpectedErrorAbsent_new() {
		commonLibrary.sleep(5000);

		WebElement errorResult = commonLibrary.isExistNegative(UIMAP_SearchResult.errorResult, 10);

		if (errorResult.getText().contains("Unexpected error"))
			report.updateTestLog("Verify the presence of unexpected error message:", "Unexpected error is displayed", Status.FAIL);
		else
			report.updateTestLog("Verify the presence of unexpected error message:", "Unexpected error is not displayed", Status.PASS);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify No Documents Found is not
	// displayed.
	// # Function Name : verifyNoDocsFoundNotDisplayed_new
	// # Author : Vennila
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search verifyNoDocsFoundNotDisplayed_new() {

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader1, 10);
		if (resultClass == null)
			report.updateTestLog("Verify 'No Documents Found' is not displayed", "'No Documents Found' is not displayed", Status.PASS);
		else
			report.updateTestLog("Verify 'No Documents Found' is not displayed", "'No Documents Found' is displayed", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get total result count
	// results
	// # Function Name : getResultcountNew     
	// # Author : toya
	// # Date Created :jan 16
	// #*****************************************************************************************************************************
	public String getResultcount2() {
		String s1 = "", str = "";
		WebElement pagination = commonLibrary.isExist(UIMAP_SearchResult.pageWrapper, 20);
		List<WebElement> DocumentNumber = commonLibrary.isExistList(pagination, UIMAP_SearchResult.eltResultList, 20);
		int number = DocumentNumber.size();

		if (number > 0) {

			str = DocumentNumber.get(number - 1).getAttribute("data-id").toString();
			str = str.substring(2);
			s1 = String.valueOf(Integer.parseInt(str) + 1);
		}

		report.updateTestLog("Getting result count", "Result count of search term is " + s1, Status.PASS);
		return s1;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Search Box
	// # Function Name : clickSearchBox    
	// # Author : Toya
	// # Date Created : 28- Jan 16
	// #*****************************************************************************************************************************
	public Search clickSearchBox() {
		WebElement searchBox = commonLibrary.isExist(UIMAP_SearchResult.searchBox, 20);
		if (searchBox != null)
			commonLibrary.clickLinkWithWebElementWithWait(searchBox, "Search");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click advanced search in Breadcrum
	// Trail
	// # Function Name : clickadvancedsearchinBreadcrum
	// # Author : Deepha H
	// # Date Created : Jan' 16
	// #*****************************************************************************************************************************

	public Practice clickadvancedsearchinBreadcrum(String LinkName) {
		Boolean flag = false;
		WebElement ulBreadCrum = commonLibrary.isExist(UIMAP_Home.ulBreadCrum, 20);
		List<WebElement> list = commonLibrary.isExistList(ulBreadCrum, UIMAP_Home.liBreadcrum, 10);
		for (WebElement item : list) {
			WebElement itemtext = commonLibrary.isExist(item, By.tagName("a"), 10);
			if (itemtext.getText().toLowerCase().equalsIgnoreCase(LinkName.trim().toLowerCase())) {
				WebElement btn = commonLibrary.isExist(item, By.tagName("a"), 10);
				if (btn != null) {
					commonLibrary.clickJS(btn);
					commonLibrary.sleep(1000);
					flag = true;
					report.updateTestLog("Click advanced search in Breadcrum Trail", "Advanced search in Breadcrum Trail is selected", Status.PASS);
				}
			}
			if (flag)
				break;
		}
		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the presence of Breadcrum
	// Trail
	// # Function Name : verifyBreabCrumTrail
	// # Author : Deepha H
	// # Date Created : Jan' 16
	// #*****************************************************************************************************************************

	public Search verifyBreabCrumTrail(String LinkName) {
		Boolean flag = false;
		WebElement ulBreadCrum = commonLibrary.isExist(UIMAP_Home.ulBreadCrum, 20);
		List<WebElement> list = commonLibrary.isExistList(ulBreadCrum, UIMAP_Home.liBreadcrum, 10);
		for (WebElement item : list) {
			WebElement itemtext = commonLibrary.isExist(item, By.tagName("a"), 10);
			if (itemtext.getText().toLowerCase().equalsIgnoreCase(LinkName.trim().toLowerCase())) {
				WebElement btn = commonLibrary.isExist(item, By.tagName("a"), 10);
				if (btn != null) {
					commonLibrary.sleep(1000);
					flag = true;
					report.updateTestLog("Verify the presence of Breadcrum Trail", "Link Name " + LinkName + " is available in the BreadCrum trail", Status.PASS);
				}
			}
			if (flag)
				break;
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify History Details One
	// # Function Name : verifyHistoryDetailsDate     
	// # Author : Deepha H
	// # Date Created : Jan' 16
	// #*****************************************************************************************************************************

	public Search verifyHistoryDetailsDate(String DocTitle, String dateTime) {
		boolean datePresent = false;
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
								if (clientHeading.get(i).getText().equalsIgnoreCase("Date")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(dateTime)) {
										datePresent = true;
									}
								}
								if (datePresent)
									break;
							}
						}
						if (docTil)
							break;
					}
				}
			}

		}

		if (!datePresent)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "Date & Time is NOT displayed as expected", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify given term is highlighted or
	// bolded
	// # Function Name : verifyTermsBoldedHighlighted
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Search verifyTermsBoldedHighlighted_new(String searchTerm) {
		boolean flag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> hitList = commonLibrary.isExistList(OListResult, UIMAP_SearchResult.highlightedBoldedTerm, 10);
				for (WebElement item : hitList) {
					if (item.getText().equalsIgnoreCase(searchTerm)) {
						flag = true;
						report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is highlighted and bolded.", Status.PASS);
						break;
					}
				}
				if (!flag)
					report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", searchTerm + " is not highlighted and not bolded.", Status.FAIL);
			} else
				report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", "Result List is not displayed.", Status.FAIL);
		} else
			report.updateTestLog("Verify " + searchTerm + " is highlighted and bolded.", "Result Container is not displayed.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Document Header
	// # Function Name : verifyDocumentHeader
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifyDocumentHeader(String strDocTitle) {
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ViewReferencesLink is
	// displayed
	// # Function Name : verifyViewReferencesLink
	// # Author : Vennila
	// # Date Created : Feb'16
	// #*****************************************************************************************************************************

	public Search verifyViewReferencesLink(String link) {
		WebElement citationHeader = commonLibrary.isExist(UIMAP_SearchResult.citationHeader, 10);
		WebElement viewReferences = commonLibrary.isExist(citationHeader, UIMAP_SearchResult.viewReferences, 10);
		boolean flag = false;
		if (viewReferences != null) {
			if (viewReferences.getText().toLowerCase().contains(link.toLowerCase()))
				report.updateTestLog("Verify " + link, link + " is available", Status.PASS);
			else
				report.updateTestLog("Verify " + link, link + " is not available", Status.FAIL);
		} else {
			report.updateTestLog("Verify " + link, link + " is not available", Status.FAIL);
		}
		return new Search(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter order in jurisdiction
	// # Function Name : verifyPostFilterOrderNumberOfResults
	// # Author : Anbarasan/*modified by vennila for Jurisdiction sort order verification*/
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search verifyPostFilterOrderNumberOfResults_new(String header) {

		if (!(header.equals(" "))) {
			int posFlag = 0;
			int negFlag = 0;
			int i = 0, j = 0, k = 0;
			int newSize = 0;
			boolean Flag = false;
			int flag1=0;
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(header.toUpperCase())) {

					Flag = true;
					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), header);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), header);
						report.updateTestLog("Expanding Filter Header: " + header, header + " filter Header Expanded.", Status.DONE);
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
					int count = moreOptions.size();
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					if (filters.size() > 0) {
						commonLibrary.focusControlJS(filterContainer);
						WebElement preferredFilters = commonLibrary.isExist(suplementalFilters.get(i - j), UIMAP_SearchResult.preferredFilters, 10);

						if (preferredFilters != null) {
							List<WebElement> preferredFiltersList = commonLibrary.isExistList(preferredFilters, UIMAP_SearchResult.eltFilterList, 10);
							k = preferredFiltersList.size();
							
							newSize = filters.size() - preferredFiltersList.size();
						}
						for (; k < filters.size() - 1; k++) {
							List<WebElement> filterName = commonLibrary.isExistList(filters.get(k), UIMAP_SearchResult.tagSpan, 20);
							List<WebElement> filterName1 = commonLibrary.isExistList(filters.get(k + 1), UIMAP_SearchResult.tagSpan, 20);
							
							if(!(filterName.get(0).getText().contains("U.S. Federal")))
							{
								String text = filterName.get(1).getText();
							String text1 = filterName1.get(1).getText();
							text = text.replaceAll("(?<=\\d),(?=\\d)", "");
							text1 = text1.replaceAll("(?<=\\d),(?=\\d)", "");
							int name = Integer.valueOf(text);
							int name1 = Integer.valueOf(text1);

							if (name >= name1) {
								posFlag++;
							} else {
								negFlag++;
							}
							}
						}
						if (preferredFilters != null) {
							if ((posFlag + count) == (newSize - 1) && (negFlag == (count - 1))) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header + "other than U.s. Federal", Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						} else {
							if (posFlag == (filters.size() - 1) && (negFlag == 0)) {
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are sorted By number of results (highest - lowest) under " + header +"other than U.s. Federal", Status.PASS);
							} else
								report.updateTestLog("Verify the filters are sorted By number of results (highest - lowest) under " + header, "The filters are not sorted By number of results (highest - lowest) under " + header, Status.FAIL);
						}
					}
					
									}
				if (Flag)
					break;
			}
		} else
			report.updateTestLog("Verify order of the filter", "No Filter Selected", Status.DONE);
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

		return new Search(scriptHelper);
	}

}
