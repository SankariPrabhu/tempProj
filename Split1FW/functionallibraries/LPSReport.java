package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import UIMAP.UIMAP_LPSCurriculumVitae;
import UIMAP.UIMAP_LPSReport;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_VSASearchResults;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LPSReport extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public LPSReport(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpsreport"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lpsreport") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpsreport")) {
			throw new IllegalStateException("LPS Report page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click expert witness list link
	// # Function Name : clickEWlist     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedmalExpertWitness clickEWlist() {
		WebElement ewList = commonLibrary.isExist(UIMAP_LPSReport.expertWitnesList, 10);
		if (ewList != null)
			commonLibrary.clickLinkWithWebElementWithWait(ewList, "Expert Witness List");
		else
			report.updateTestLog("Click on Expert Witness List link", "Expert Witness List link is not present", Status.FAIL);
		return new MedmalExpertWitness(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the current page displayed.
	// # Function Name : verifyPage     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport verifyPage(String header, String contentType) {
		WebElement headerContent = commonLibrary.isExistNegative(UIMAP_LPSReport.header, 10);
		WebElement contentHeader = commonLibrary.isExistNegative(UIMAP_LPSReport.contentHeader, 10);

		// Verifying the current page is displayed
		if (headerContent.getText().toLowerCase().contains(header.toLowerCase()) && contentHeader.getText().toLowerCase().contains(contentType.toLowerCase()))
			report.updateTestLog("Verify " + contentType + " page is displayed for " + header, contentType + " page is displayed for " + header, Status.PASS);
		else
			report.updateTestLog("Verify " + contentType + " page is displayed for " + header, contentType + " page is not displayed for " + header, Status.FAIL);
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the content type links
	// present under respective headers.
	// # Function Name : verifyContentTypeLinks     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport verifyContentTypeLinks(String header, String contentTypes) {
		String[] contentList = contentTypes.split(";");
		boolean flag = false;
		int i = 0, j = 0;
		WebElement contentTypeList = commonLibrary.isExist(UIMAP_LPSReport.contentTypeList, 12);
		List<WebElement> contents = commonLibrary.isExistList(contentTypeList, UIMAP_LPSReport.contentGroup, 10);
		for (j = 0; j < contentList.length; j++) {
			flag = false;
			for (i = 0; i < contents.size(); i++) {
				WebElement contentHead = commonLibrary.isExistNegative(contents.get(i), UIMAP_LPSReport.span, 10);
				if (contentHead != null && contentHead.getText().toLowerCase().contains(header.toLowerCase())) {
					List<WebElement> options = commonLibrary.isExistList(contents.get(i), UIMAP_LPSReport.listItem, 10);
					for (WebElement item : options) {
						if (item.getText().toLowerCase().contains(contentList[j].toLowerCase())) {
							report.updateTestLog("Verify " + contentList[j] + " content is displayed under " + header, contentList[j] + " content is displayed under " + header, Status.PASS);
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + contentList[j] + " content is displayed under " + header, contentList[j] + " content is not displayed under " + header, Status.FAIL);
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click content type link.
	// # Function Name : clickContentTypeLink     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport clickContentTypeLink(String contentTypes) {
		boolean flag = false;
		WebElement contentTypeList = commonLibrary.isExist(UIMAP_LPSReport.contentTypeList, 12);
		List<WebElement> contents = commonLibrary.isExistList(contentTypeList, UIMAP_LPSReport.contentGroup, 10);
		for (WebElement li : contents) {

			List<WebElement> options = commonLibrary.isExistList(li, UIMAP_LPSReport.listItem, 10);
			for (WebElement item : options) {
				if (item.getText().toLowerCase().contains(contentTypes.toLowerCase())) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_LPSReport.button, 10);
					commonLibrary.clickButtonParentWithWait(button, contentTypes);
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
					commonLibrary.sleep(20000);
					flag = true;
					break;
				}

			}
			if (flag)
				break;
		}
		if (flag)
			report.updateTestLog("Verify  " + contentTypes + " displays alone in the left pane ", contentTypes + " content is displayed alone in the left pane ", Status.PASS);

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on specific page number.
	// # Function Name : ClickPageNumber
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPSReport clickPageNumber(String number) {
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
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click next page.
	// # Function Name : ClickNextPage
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPSReport clickNextPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
		if (btnNextPage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "NextPage");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click previous page.
	// # Function Name : ClickPageNumber
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPSReport clickPreviousPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnPrePage);
		if (btnNextPage != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "Previous Page");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "Previous Page");
		} else {
			report.updateTestLog("Click on previous page", "Previous page is not clicked", Status.FAIL);
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickReport(String strDocTitle) {
		WebElement resultClass = null;
		boolean blnFlag = false;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassReport, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.reportLink, 20);
						if (lnkDocument != null) {
							// commonLibrary.ScrollToView(lnkDocument);
							// commonLibrary.highlightElement(lnkDocument);
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
				if (!blnFlag)

					report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);

			}
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the the graphs displayed
	// # Function Name : verifyGraphs     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPSReport verifyGraphs(String headings) {

		String AvailGraphs = "";
		String[] arrheadings = headings.split(";");
		WebElement graphForm = commonLibrary.isExist(UIMAP_LPSReport.graphList, 20);
		if (graphForm != null) {
			List<WebElement> graphs = commonLibrary.isExistList(graphForm, UIMAP_LPSReport.graphpod, 20);
			if (graphs != null) {
				for (int j = 0; j < graphs.size(); j++) {
					WebElement header = commonLibrary.isExist(graphs.get(j), By.tagName("h2"), 20);
					if (header != null) {
						AvailGraphs = AvailGraphs + ";" + header.getText();
					}
				}

			} else {
				report.updateTestLog("Verify graphs are displayed", "Graphs are not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify graphs are displayed", "Graphs are not displayed", Status.FAIL);
		}

		for (int i = 0; i < arrheadings.length; i++) {
			if (AvailGraphs.toLowerCase().contains(arrheadings[i].toLowerCase())) {
				report.updateTestLog("Verify whether " + arrheadings[i] + " graph is displayed", "" + arrheadings[i] + " graph is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify whether " + arrheadings[i] + " graph is displayed", "" + arrheadings[i] + " graph is not displayed", Status.FAIL);
			}
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a particular graph
	// # Function Name : clickGraph     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************
	public LPSReport clickGraph(String graph) {
		try {
			boolean clicked = false;
			commonLibrary.sleep(6000);
			WebElement graphForm = commonLibrary.isExist(UIMAP_LPSReport.graphList, 20);
			if (graphForm != null) {
				List<WebElement> graphs = commonLibrary.isExistList(graphForm, UIMAP_LPSReport.graphpod, 20);
				if (graphs != null) {
					for (int j = 0; j < graphs.size(); j++) {
						WebElement header = commonLibrary.isExist(graphs.get(j), By.tagName("footer"), 20);
						WebElement link = commonLibrary.isExist(header, By.tagName("a"), 20);
						if (link.getText().toLowerCase().equalsIgnoreCase(graph.toLowerCase())) {
							commonLibrary.clickJS(link, "Graph");
							clicked = true;
							break;
						}
					}

				} else {
					report.updateTestLog("Verify graphs are displayed", "Graphs are not displayed", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify graphs are displayed", "Graphs are not displayed", Status.FAIL);
			}
			if (!clicked) {
				report.updateTestLog("Click on graph " + graph + "", "" + graph + " is not clicked", Status.FAIL);
			} else {
				WebElement max = commonLibrary.isExist(graphForm, UIMAP_LPSReport.graphMax, 20);
				if (max != null) {
					report.updateTestLog("Verify expanded form of the clicked graph is displayed", "Expanded form of the clicked graph is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify expanded form of the clicked graph is displayed", "Expanded form of the clicked graph is not displayed", Status.FAIL);
				}

			}
		} catch (Exception e) {

		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Close graph button
	// # Function Name : clickCloseGraph     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #**********************************************

	public LPSReport clickCloseGraph() {
		WebElement graphForm = commonLibrary.isExist(UIMAP_LPSReport.graphList, 20);
		if (graphForm != null) {
			WebElement maxGraph = commonLibrary.isExist(graphForm, UIMAP_LPSReport.graphMax, 20);
			WebElement max = commonLibrary.isExist(maxGraph, UIMAP_LPSReport.closegrph, 20);
			if (max != null && max.isEnabled() && max.isDisplayed()) {
				commonLibrary.clickButtonLogSmallWait(max, "Clolse Graph");
			}

		} else {
			report.updateTestLog("Verify graphs are displayed", "Graphs are not displayed", Status.FAIL);
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click web link, verify and close
	// secondary browser and return to orginal browser.
	// # Function Name : clickReportVerifySecondBrowserClose     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport clickReportVerifySecondBrowserClose(String strDocTitle, String url, String url1, String verify) {
		WebElement resultClass = null;
		boolean blnFlag = false;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassReport, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, UIMAP_SearchResult.reportLink, 20);
						if (lnkDocument != null) {
							// commonLibrary.ScrollToView(lnkDocument);
							// commonLibrary.highlightElement(lnkDocument);
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
				if (!blnFlag)

					report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
				else {
					if (driver.getWindowHandles().size() > 1) {
						report.updateTestLog("Verify Secondary browser is displayed.", "Secondary browser is displayed.", Status.PASS);
					} else
						report.updateTestLog("Verify Secondary browser is displayed.", "Secondary browser is not displayed.", Status.FAIL);

					if (commonLibrary.switchToWindow1(url)) {
						driver.close();
						report.updateTestLog("Close secondary browser.", "Secondary browser is closed.", Status.DONE);
					}

					commonLibrary.switchToWindow(url1);
				}

			}
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut != null)
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
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
	// # Function Description : Function to select more or Few categories in LPS
	// # Function Name : selectmoreorfewcategories     
	// # Author : Ram Prasath
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport selectmoreorfewcategories(String Categorytype, String contentTypes) {

		try {
			if (Categorytype.equalsIgnoreCase("more")) {
				report.updateTestLog("Verify Show more category displays at the bottom of the left pane", "Show more option is displayed at the bottom of the Left Pane", Status.PASS);
			}

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
			if ((btnFewerCat != null) && (btnFewerCat.getText().contains(Categorytype)))
				if (browsername.contains("internet")) {
					commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, Categorytype + " categories");
					// report.updateTestLog("Verify Categories",
					// Categorytype+" is Selected", Status.PASS);

				} else {
					commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, Categorytype + " categories");
					// report.updateTestLog("Verify Categories",
					// Categorytype+" is Selected", Status.PASS);
				}
			if (Categorytype.equalsIgnoreCase("less")) {
				report.updateTestLog("Verify  Left pane gets collapsed and displayes ", contentTypes + " content is displayed alone in the left pane ", Status.PASS);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Verify the Presence of Header in Categories Tab
	// # Function Name : Header Presence in Categories Tab     
	// # Author : Ram Prasath
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport verifyCategoriesHeader(boolean categoryheader0, boolean categoryheader1, boolean categoryheader2, boolean categoryheader3) {
		String allcategoryheader = dataTable.getData("General_Data", "CategoryHeader");
		String eachcategoryheader[] = allcategoryheader.split(";");
		String mainHeader = dataTable.getData("General_Data", "MainHeader");
		Boolean blnFlag = false;
		if (categoryheader0) {
			WebElement contentTypeList1 = commonLibrary.isExist(UIMAP_LPSReport.contentheader, 12);
			List<WebElement> lilist1 = commonLibrary.isExistList(contentTypeList1, UIMAP_LPSReport.listItem, 20);
			for (WebElement item : lilist1) {
				WebElement span = commonLibrary.isExist(item, UIMAP_LPSReport.span, 12);
				if (span != null && span.getText().contains(mainHeader)) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag) {
				report.updateTestLog("Presence of Headers in Categories tab", mainHeader + " is displayed in categories tab", Status.PASS);
			} else {
				report.updateTestLog("Presence of Headers in Categories tab", mainHeader + " is not displayed in categories tab", Status.FAIL);
			}
		}
		blnFlag = false;

		if (categoryheader1) {
			WebElement contentTypeList1 = commonLibrary.isExist(UIMAP_LPSReport.contentheader, 12);
			List<WebElement> lilist1 = commonLibrary.isExistList(contentTypeList1, UIMAP_LPSReport.listItem, 20);
			for (WebElement item : lilist1) {
				WebElement span = commonLibrary.isExist(item, UIMAP_LPSReport.span, 12);
				if (span != null && span.getText().contains(eachcategoryheader[0])) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag) {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[0] + " is displayed in categories tab", Status.PASS);
			} else {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[0] + " is not displayed in categories tab", Status.FAIL);
			}
		}
		blnFlag = false;

		if (categoryheader2) {
			WebElement contentTypeList1 = commonLibrary.isExist(UIMAP_LPSReport.contentheader, 12);
			List<WebElement> lilist1 = commonLibrary.isExistList(contentTypeList1, UIMAP_LPSReport.listItem, 20);
			for (WebElement item : lilist1) {
				WebElement span = commonLibrary.isExist(item, UIMAP_LPSReport.span, 12);
				if (span != null && span.getText().contains(eachcategoryheader[1])) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag) {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[1] + " is displayed in categories tab", Status.PASS);
			} else {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[1] + " is not displayed in categories tab", Status.FAIL);
			}
		}

		blnFlag = false;
		if (categoryheader3) {
			WebElement contentTypeList1 = commonLibrary.isExist(UIMAP_LPSReport.contentheader, 12);
			List<WebElement> lilist1 = commonLibrary.isExistList(contentTypeList1, UIMAP_LPSReport.listItem, 20);
			for (WebElement item : lilist1) {
				WebElement span = commonLibrary.isExist(item, UIMAP_LPSReport.span, 12);
				if (span != null && span.getText().contains(eachcategoryheader[2])) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag) {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[2] + " is displayed in categories tab", Status.PASS);
			} else {
				report.updateTestLog("Presence of Headers in Categories tab", eachcategoryheader[2] + " is not displayed in categories tab", Status.FAIL);
			}
		}

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Verify the Presence of Header in Categories Tab
	// along with Counts
	// # Function Name : Header Presence in Categories Tab     
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPSReport verifyCategoriesHeader(boolean categoryheader0) {
		String allcategoryheader = dataTable.getData("General_Data", "CategoryHeader");
		allcategoryheader.split(";");
		String mainHeader = dataTable.getData("General_Data", "MainHeader");
		Boolean blnFlag = false;
		if (categoryheader0) {
			WebElement contentTypeList1 = commonLibrary.isExist(UIMAP_LPSReport.contentheader, 12);
			List<WebElement> lilist1 = commonLibrary.isExistList(contentTypeList1, UIMAP_LPSReport.listItem, 20);
			for (WebElement item : lilist1) {
				WebElement span = commonLibrary.isExist(item, UIMAP_LPSReport.span, 12);
				if (span != null && span.getText().contains(mainHeader)) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag) {
				report.updateTestLog("Presence of Headers in Categories tab", mainHeader + " is displayed in categories tab", Status.PASS);
			} else {
				report.updateTestLog("Presence of Headers in Categories tab", mainHeader + " is not displayed in categories tab", Status.FAIL);
			}
		}
		return new LPSReport(scriptHelper);
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
		if (footerlist5.getText().contains("Copyright © 2015 LexisNexis. All rights reserved.")) {
			report.updateTestLog("Verify the Presence of Copyright © 2015 LexisNexis. All rights reserved. Link", footerlist5.getText() + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the Presence of Copyright © 2015 LexisNexis. All rights reserved. Link", footerlist5.getText() + " is not Displayed", Status.FAIL);
		}

		if (browsername.contains("internet")) {
			commonLibrary.clickLinkWithWebElementWithWaitJS(footerlist4, "Sign Out");

		} else {
			commonLibrary.clickLinkWithWebElementWithWait(footerlist4, "Sign Out");

		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to signout of the application from the footer
	// page
	// # Function Name : signoutFromFooter     
	// # Author : Baswaraj
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public SignIn signoutFromFooter() {
		WebElement footerlinks = commonLibrary.isExist(UIMAP_LPSReport.footerlinks, 12);
		List<WebElement> lilist = commonLibrary.isExistList(footerlinks, UIMAP_LPSReport.listItem, 20);
		WebElement footerlist4 = commonLibrary.isExist(lilist.get(4), UIMAP_LPSReport.footersignout, 12);
		if (browsername.contains("internet")) {
			commonLibrary.clickLinkWithWebElementWithWaitJS(footerlist4, "Sign Out");

		} else {
			commonLibrary.clickLinkWithWebElementWithWait(footerlist4, "Sign Out");

		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select more or Few categories in LPS
	// # Function Name : selectMoreOrFewCategoriesInLeftPane     
	// # Author : Ram Prasath
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport selectMoreOrFewCategoriesInLeftPane(String Categorytype, String contentTypes) {
		try {
			if (Categorytype.toLowerCase().contains("more")) {
				report.updateTestLog("Verify Show more category displays at the bottom of the left pane", "Show more option is displayed at the bottom of the Left Pane", Status.PASS);
				WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
				if ((btnFewerCat != null) && (btnFewerCat.getText().contains(Categorytype) || Categorytype.toLowerCase().contains("more"))) {
					if (browsername.contains("internet"))
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, Categorytype + " categories");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, Categorytype + " categories");
				}
			}

			if (Categorytype.toLowerCase().contains("less") || Categorytype.toLowerCase().contains("fewer")) {
				report.updateTestLog("Verify  Left pane gets collapsed and displayes ", contentTypes + " content is displayed alone in the left pane ", Status.PASS);
				WebElement btnFewerCat = commonLibrary.isExistNegative(UIMAP_SearchResult.btnFewerCat, 10);
				if ((btnFewerCat != null) && (Categorytype.toLowerCase().contains("less") || Categorytype.toLowerCase().contains("fewer"))) {
					if (browsername.contains("internet"))
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnFewerCat, Categorytype + " categories");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnFewerCat, Categorytype + " categories");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Get it now button from LPS
	//
	// # Function Name : clickGetItNowButton     
	// # Author : KirthikaK
	// # Date Created : 11 Mar'15
	// #*****************************************************************************************************************************
	public LPSReport clickGetItNowButton() {

		WebElement getItNow;
		int counter = 0;
		getItNow = commonLibrary.isExistNegative(UIMAP_VSASearchResults.getItNowContainer, 10);
		do {
			getItNow = commonLibrary.isExistNegative(UIMAP_VSASearchResults.getItNowContainer, 10);

			if (getItNow == null)
				commonLibrary.sleep(5000);
			counter++;

		} while ((getItNow == null) && counter < 20);

		WebElement getItNowContainer = commonLibrary.isExist(UIMAP_VSASearchResults.getItNowContainer, 10);
		WebElement getItNowbutton = commonLibrary.isExist(getItNowContainer, UIMAP_VSASearchResults.getItNowButton, 10);

		commonLibrary.clickButtonLogSmallWait(getItNowbutton, "Get It Now");

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save the court name and date under
	// document title
	// # Function Name : saveCourtAndDateUnderDocumentTitle     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public String saveCourtAndDateUnderDocumentTitle(String docTitle) {
		String court = "";
		String date = "";
		String total = "";
		WebElement resultClass = null;
		boolean blnFlag = false;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);
		if (resultClass == null) {
			int count = 0;
			do {
				count = count + 1;
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);
				if (resultClass == null)
					commonLibrary.sleep(5000);
			} while (resultClass == null && count <= 40);
		}

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassReport, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(docTitle)) {
						WebElement attributeContainer = commonLibrary.isExist(document, UIMAP_LPSReport.attributeContainer, 10);
						WebElement courtValue = commonLibrary.isExist(attributeContainer, UIMAP_LPSReport.courtValue, 10);
						WebElement dateValue = commonLibrary.isExist(attributeContainer, UIMAP_LPSReport.dateValue, 10);
						if (courtValue != null && dateValue != null) {
							court = courtValue.getText();
							date = dateValue.getText();
							total = court + ";" + date;
							blnFlag = true;
							break;
						}
					}

				}
				if (!blnFlag)

					report.updateTestLog("Click on the document " + docTitle, "Not Clicked  on the document " + docTitle, Status.FAIL);

			}
		}
		return total;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPSReport selectPostFilter(String strHeader, String strFilter) {
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
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						if (item.getText().toLowerCase().contains(strFilter.toLowerCase())) {
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

		return new LPSReport(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view all curriculum vitae
	// # Function Name : clickViewAllCV
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPSCurriculumVitae clickViewAllCV() {
		WebElement viewAllCV = commonLibrary.isExist(UIMAP_LPSReport.viewAllCV, 10);
		if (viewAllCV != null) {
			// Clicking on view all cv link
			commonLibrary.clickLinkWithWebElement(viewAllCV, viewAllCV.getText());
			WebElement pageHeader = commonLibrary.isExist(UIMAP_LPSCurriculumVitae.pageHeader, 10);
			if (pageHeader == null) {
				int count = 0;
				do {
					count = count + 1;
					pageHeader = commonLibrary.isExist(UIMAP_LPSCurriculumVitae.pageHeader, 10);
					if (pageHeader == null)
						commonLibrary.sleep(5000);
				} while (pageHeader == null && count <= 40);
			}
		} else {
			report.updateTestLog("Click view all curriculum vitae link", "Not clicked on view all curriculum vitae link", Status.FAIL);
		}
		return new LPSCurriculumVitae(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserForward
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPSCurriculumVitae clickBrowserForward() {
		commonLibrary.clickBrowserForward();
		return new LPSCurriculumVitae(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPResults clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click content type link.
	// # Function Name : clickContentTypeLink     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport clickContentTypeLinkModified(String contentType) {
		boolean flag = false;
		WebElement contentTypeList = commonLibrary.isExist(UIMAP_LPSReport.contentTypeList1, 20);
		List<WebElement> contents = commonLibrary.isExistList(contentTypeList, UIMAP_LPSReport.contentGroup, 10);
		WebElement category = commonLibrary.isExist(contents.get(1), UIMAP_LPSReport.categoryList, 10);
		List<WebElement> listCategory = commonLibrary.isExistList(category, UIMAP_LPSReport.listItem, 20);

		if (listCategory.size() > 0) {
			for (WebElement item : listCategory) {
				WebElement categoryName = commonLibrary.isExist(item, UIMAP_LPSReport.categoryName, 10);
				if (categoryName.getText().toLowerCase().contains(contentType.toLowerCase())) {
					commonLibrary.clickButton(categoryName);
					commonLibrary.sleep(20000);
					flag = true;
					break;
				}

			}
		}
		if (flag)
			report.updateTestLog("Click on Category", "Category is clicked", Status.PASS);
		else
			report.updateTestLog("Click on Category", "Category is not clicked", Status.FAIL);

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the current page displayed.
	// # Function Name : verifyPage     
	// # Author : Niraj
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LPSReport verifyContentSwitcher(String contentType) {
		WebElement contentHeader = commonLibrary.isExistNegative(UIMAP_LPSReport.contentHeader, 10);

		// Verifying the current page is displayed
		if (contentHeader != null && contentHeader.getText().toLowerCase().contains(contentType.toLowerCase()))
			report.updateTestLog("Verify  the results page is displayed for " + contentType, " Results page is displayed for " + contentType, Status.PASS);
		else
			report.updateTestLog("Verify  the results page is displayed for " + contentType, " Results page is not displayed for " + contentType, Status.FAIL);
		return new LPSReport(scriptHelper);
	}
	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPSReport swithToCondentType(String strCondentType) {

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

		return new LPSReport(scriptHelper);
	}
	// #*****************************************************************************************************************************
	// # Function Description : Function to Click First DocLink
	// # Function Name : ClickFirstDocLink2     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
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
							WebElement lnkDocument = commonLibrary.isExist(document, UIMAP_SearchResult.docLink, 20);
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
							
						} else {
							WebElement lnkDocument = commonLibrary.isExist(document, UIMAP_SearchResult.docLink, 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
								break;
							}
						}
					}

				}
			}
			return strDocTitle;
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}
	// #*****************************************************************************************************************************
	// # Function Description : Function to getFirstDocLink
	// # Function Name : getSecondDocLink  
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String getSecondDocLink() {
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
					if(OListItems.size()>1) {
						WebElement eleDocTitle = commonLibrary.isExist(OListItems.get(1), UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null) {
							strDocTitle = eleDocTitle.getText();
							// blnFlag=true;	
						} else {
							WebElement lnkDocument = commonLibrary.isExist(OListItems.get(1), UIMAP_SearchResult.docLink, 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
							}
						}
					}

				}
			}
			return strDocTitle;
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

}
