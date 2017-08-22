package functionallibraries;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Shepards;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

public class Shepards extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private ExcelDataAccess excel;

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;

	public Shepards(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("shepards"))
				commonLibrary.sleep(5000);

		} while (!url.contains("shepards") && counter < 40);

		if (!driver.getCurrentUrl().contains("shepards")) {
			throw new IllegalStateException("Shepards page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Result page for SearchTerms
	// other than shepards
	// # Function Name : VerifyResultPage for normal searchterms
	// # Author : Baswaraj V
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void VerifyResultPageNormalSearch(String SearchTerm) {
		try {
			WebElement hdrResult1 = commonLibrary.isExist(UIMAP_Shepards.hdrResult1, 10);
			if (hdrResult1.getText().contains(SearchTerm)) {
				report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on reference in jump to option
	// # Function Name : clickandVerifyPopupPhrase   
	// # Author : Deepha Hariramasamy
	// # Date Created : Nov '15
	// #*****************************************************************************************************************************
	public Shepards clickandVerifyPopupPhrase(String link, boolean flag) {
		WebElement motion = commonLibrary.isExist(UIMAP_Shepards.motion, 20);

		WebElement motionLink = commonLibrary.isExist(motion, UIMAP_Shepards.motionLink, 20);
		WebElement closeButton = commonLibrary.isExist(UIMAP_Shepards.motion, 20);
		WebElement popUp = commonLibrary.isExist(UIMAP_Shepards.clickLinkPopUp, 20);
		if (motionLink != null) {
			commonLibrary.clickLink(motionLink, link);
			commonLibrary.isExist(UIMAP_Shepards.shepPopUp, 20);
			report.updateTestLog("Verify the shep pop up phrase is displayed", "The shep pop up phrase is displayed", Status.PASS);

			if (flag != false) {
				commonLibrary.clickLink(popUp, "All Shepard's® editorial phrases");
				report.updateTestLog("Verify the secondary browser displayed", "secondary browser is displayed ", Status.PASS);
				driver.close();
				commonLibrary.smallWait();
				commonLibrary.switchToWindow("search");
				report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
			} else {
				commonLibrary.clickButton(closeButton);
			}
		} else {
			report.updateTestLog("Verify the shep pop up phrase is displayed", "The shep pop up phrase is not displayed", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Result page
	// # Function Name : VerifyResultPage     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyResultPage() {
		// verifying the results by checking with header
		WebElement hdrResult = commonLibrary.isExist(UIMAP_Shepards.hdrResult, 10);
		if (hdrResult.getText().contains("Shepard")) {
			report.updateTestLog("Verify results page is displayed", "Result page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify results page is displayed", "Result page is not displayed", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selection Category and then click
	// on List/Map/Grid
	// # Function Name : clickShepardsCategoryListMapGrid     
	// # Author : Uma
	// # Date Created :Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickShepardsCategoryListMapGrid(String category, String option) {

		try {

			WebElement btnIdGridMapView = null;
			WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
			// Select different category
			if (category != "") {

				if (category.contains("Citing Decisions")) {

					WebElement lnkCitingDecisions = commonLibrary.isExist(UIMAP_Shepards.lnkCitingDecisions, 10);
					commonLibrary.clickLinkWithWebElementWithWait(lnkCitingDecisions, category);
					btnIdGridMapView = commonLibrary.isExistNegative(UIMAP_Shepards.btnIdGridView, 10);
					int count = 0;
					do {
						count++;
						btnIdGridMapView = commonLibrary.isExistNegative(UIMAP_Shepards.btnIdGridView, 10);
						commonLibrary.sleep(10000);
					} while (btnIdGridMapView == null && count < 15);

				} else if (category.contains("Other Citing Sources")) {

					WebElement lnkOtherCitingResources = commonLibrary.isExist(UIMAP_Shepards.lnkOtherCitingResources, 10);
					commonLibrary.clickLinkWithWebElementWithWait(lnkOtherCitingResources, category);

				} else if (category.contains("Table of Authorities")) {

					WebElement lnkTableOfAuthorities = commonLibrary.isExist(UIMAP_Shepards.lnkTableOfAuthorities, 10);
					commonLibrary.clickLinkWithWebElementWithWait(lnkTableOfAuthorities, category);

				} else if (category.contains("Appellate History")) {

					WebElement lnkAppelatehistory = commonLibrary.isExist(UIMAP_Shepards.lnkAppelatehistory, 10);
					commonLibrary.clickLinkWithWebElementWithWait(lnkAppelatehistory, category);

				}

				WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
				int count = 0;
				try {
					do {
						commonLibrary.sleep(500000);
						count++;
						currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 3);
					} while (currProdOld.equals(currProdNew) && count < 15);

					pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
					commonLibrary.sleep(100000);
				} catch (Exception e) {
					System.out.println(e.toString());
				}

			}

			// Select list/grid/Map option
			if (option != "") {

				if (option.contains("List")) {

					WebElement btnIdListView = commonLibrary.isExist(UIMAP_Shepards.btnIdListView, 10);
					commonLibrary.clickLinkWithWebElementWithWait(btnIdListView, option);
					WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
					int count = 0;
					try {
						do {
							commonLibrary.sleep(500000);
							count++;
							currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
						} while (currProdOld.equals(currProdNew) && count < 15);

						pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
						commonLibrary.sleep(100000);
					} catch (Exception e) {
						System.out.println(e.toString());
					}

				} else if (option.contains("Map") || option.contains("Grid")) {

					btnIdGridMapView = commonLibrary.isExistNegative(UIMAP_Shepards.btnIdGridMapView, 10);
					if (btnIdGridMapView != null)
						commonLibrary.clickLinkWithWebElementWithWait(btnIdGridMapView, option);
					WebElement divWow = commonLibrary.isExist(UIMAP_Shepards.divWowheader, 10);
					int counter = 0;
					do {
						counter++;
						divWow = commonLibrary.isExist(UIMAP_Shepards.divWowheader, 10);
						commonLibrary.sleep(10000);
					} while (divWow == null && counter < 15);

					if (option.contains("Grid") && btnIdGridMapView == null) {
						btnIdGridMapView = commonLibrary.isExistNegative(UIMAP_Shepards.btnIdGridView, 10);

						if (btnIdGridMapView != null)
							commonLibrary.clickLinkWithWebElementWithWait(btnIdGridMapView, option);
						WebElement eltGridContent = commonLibrary.isExist(UIMAP_Shepards.divAnalysisByCourt, 10);
						int count1 = 0;
						do {
							count1++;
							eltGridContent = commonLibrary.isExist(UIMAP_Shepards.divAnalysisByCourt, 10);
							commonLibrary.sleep(10000);
						} while (eltGridContent == null && count1 < 35);
					}

				}

				// wait if the page loading spinner image is displayed
				pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
				commonLibrary.sleep(20000);

			}

			return new Shepards(scriptHelper);

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click legend or display option
	// # Function Name : VerifyErrorText     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickLegendOrDisplayOptionInResultPage(String strlink) {
		boolean blnFlag = false;
		WebElement divWow = commonLibrary.isExist(UIMAP_Shepards.divWowheader, 10);
		if (divWow != null) {
			List<WebElement> lnkDisplayopt = commonLibrary.isExistList(divWow, By.tagName("span"), 10);
			for (WebElement item : lnkDisplayopt) {
				if (item.getText().equalsIgnoreCase(strlink)) {
					// commonLibrary.highlightElement(item);
					// item.click();
					commonLibrary.clickJS(item);
					blnFlag = true;
					try {
						commonLibrary.sleep(3000);
					} catch (Exception e) {
						System.out.println(e.toString());
						throw new FrameworkException("Exception", e.toString());
					}
					break;
				}
			}
		}
		if (blnFlag) {
			report.updateTestLog("Click the button " + strlink + "", "" + strlink + " is clicked", Status.PASS);
		} else {
			report.updateTestLog("Click the button " + strlink + "", "" + strlink + " is not clicked", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whether a particular node is
	// connected with red line
	// # Function Name : VerifyNodeConnectedWithRedLine
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyNodeConnectedWithRedLine() {
		boolean blnFlag3 = false;
		WebElement divresult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 20);
		List<WebElement> path = commonLibrary.isExistList(divresult, By.tagName("path"), 10);
		for (WebElement item : path) {
			if (item.getAttribute("stroke").contains("#ed1c24")) {
				blnFlag3 = true;
				break;
			}

		}
		if (blnFlag3) {
			report.updateTestLog("Check the Star node and Node9 are connectedwith a  Red solid line", "Star node and Node9 are connectedwith a  Red solid line", Status.PASS);
		} else {
			report.updateTestLog("Check the Star node and Node9 are connectedwith a  Red solid line", "Star node and Node9 are not connectedwith a  Red solid line", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Post Filter in sheaprds
	// Results Page
	// # Function Name : SelectPostFilter_Shepards     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards selectPostFilterShepards(String header, String filter) {

		WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
		// Common Function call for selecting post filter
		generalFunctions.selectPostFilter(header, filter);
		WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 10);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(500000);
				count++;
				currProdNew = commonLibrary.isExistNegative(UIMAP_Home.CurrentProduct, 3);
			} while (currProdOld.equals(currProdNew) && count < 15);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click legend or display option
	// # Function Name : VerifyResultPage     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickNodeWithNumber(String strNumber) {
		boolean blnFlag = false;
		try {
			WebElement divResult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 10);
			if (divResult != null) {
				List<WebElement> lnkNode = commonLibrary.isExistList(divResult, By.tagName("tspan"), 10);
				for (WebElement item : lnkNode) {
					if (item.getText().equalsIgnoreCase(strNumber)) {
						// commonLibrary.Click_JS(item);
						item.click();
						commonLibrary.sleep(3000);

						WebElement imgClose = commonLibrary.isExist(UIMAP_Shepards.imgClose, 10);
						if (imgClose != null) {
							blnFlag = true;
							break;
						}
					}
				}
			}
			if (blnFlag) {
				report.updateTestLog("Click the node " + strNumber + "", "Node " + strNumber + " is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click the node " + strNumber + "", "Node " + strNumber + " is not clicked", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on node and select
	// showinList/Open Doc link
	// # Function Name : ClickNodeWithNumber_SelectLink     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickNodeWithNumber(String number, String nodeTitle) {
		Boolean blnFlag = false;

		WebElement divResult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 10);

		if (divResult != null) {

			List<WebElement> lnkNode = commonLibrary.isExistList(divResult, By.tagName("tspan"), 10);

			// Loop for navigating through all Node
			for (WebElement item : lnkNode) {

				if (item.getText().equalsIgnoreCase(number)) {

					try {

						// Focusing window
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("window.focus()");

						WebElement Parent = commonLibrary.getParentElement(item);
						driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MILLISECONDS);

						// Click on Node
						if (browsername.contains("chrome"))
							item.click();

						else if (browsername.contains("internet") && version.contains("8"))
							item.click();

						else if (browsername.contains("internet"))
							item.click();

						else
							Parent.click();

						commonLibrary.sleep(2000);
						report.updateTestLog("click on " + nodeTitle + " link", nodeTitle + " link is clicked", Status.DONE);

					} catch (TimeoutException e) {

						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.MILLISECONDS);
						System.out.println(e.toString());

					}

					// Verification Point :Pop up image is displayed
					WebElement imgClose = commonLibrary.isExist(UIMAP_Shepards.imgClose, 10);

					if (imgClose != null) {

						blnFlag = true;
						break;

					}
				}
			}
		}

		if (blnFlag) {

			report.updateTestLog("Click the node " + number + "", "Node " + number + " is clicked", Status.PASS);
			return new Shepards(scriptHelper);

		} else {
			throw new FrameworkException("Click the node " + number + "", "Node " + number + " is not clicked");

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on node and select
	// showinList/Open Doc link
	// # Function Name : ClickNodeWithNumber_SelectLink     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickOpenDocument() {

		// Click on open document link
		WebElement divIdBubble = commonLibrary.isExist(UIMAP_Shepards.divIdBubble, 10);
		WebElement lnkIdOpenDocument = commonLibrary.isExist(divIdBubble, UIMAP_Shepards.lnkIdOpenDocument, 10);
		if (lnkIdOpenDocument != null) {
			commonLibrary.clickLinkWithWebElement(lnkIdOpenDocument, lnkIdOpenDocument.getText());
			WebElement atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);

			int counter = 0;
			do {
				counter = counter + 1;
				commonLibrary.sleep(20000);
				atd = commonLibrary.isExistNegative(UIMAP_Document.eltAboutThisDoc, 3);
				if (atd == null && counter > 20)
					atd = commonLibrary.isExistNegative(UIMAP_Document.copyCitation, 3);

			} while (atd == null && counter <= 40);
		} else {
			report.updateTestLog("Click open document", "Open document link is not available", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify that dates are arranged in
	// sorting order
	// # Function Name : VerifySort_CitingDecisions_ByDate    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifySortCitingDecisionsByDate() {
		List<WebElement> eltCourtNameLinksContainer = commonLibrary.isExistList(UIMAP_Shepards.eltCourtNameLinksContainer, 10);
		List<WebElement> DateList = commonLibrary.isExistList(eltCourtNameLinksContainer.get(1), UIMAP_Shepards.lnkCourtName, 10);
		Boolean blnFlag = false;

		int j = 1, i = 0;
		List<String> strDateList = new ArrayList<String>();
		if (DateList.get(0).getText().contains("No Date"))
			j = 1;
		else
			j = 0;

		for (i = 0; j < DateList.size(); i++, j++) {
			String str1 = DateList.get(j).getText().toString();
			strDateList.add(str1.trim());
		}
		Collections.sort(strDateList);
		if (DateList.get(0).getText().contains("No Date"))
			j = 1;
		else
			j = 0;
		blnFlag = true;
		for (i = 0; i < strDateList.size() && j < DateList.size(); i++, j++) {
			if (!DateList.get(j).getText().contains(strDateList.get(i))) {
				blnFlag = false;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify 'No Date' column displays Years are displayed in order from lowest to highest, starting with 'No Date' if applicable.", "Dates are arranged in ascending order.", Status.PASS);
		else
			report.updateTestLog("Verify 'No Date' column displays Years are displayed in order from lowest to highest, starting with 'No Date' if applicable.", "Dates are not arranged in ascending order.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search verifyFullDocument(String strSearchTerm) {
		Boolean blnFlag = false;
		try {
			WebElement banner = commonLibrary.isExist(UIMAP_Shepards.hdrBanner, 10);
			if (banner != null) {
				List<WebElement> span = commonLibrary.isExistList(banner, UIMAP_Shepards.tagSpan, 10);
				for (WebElement item : span) {
					if (item.getText().toLowerCase().contains(strSearchTerm.toLowerCase())) {
						blnFlag = true;
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
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click any link in Shepards Preview
	// section
	// # Function Name : ClickLink_Shepards_Preview_Section     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void clickLinkShepardsPreviewSection(String lnkHeader, String linkname) {
		try {
			Boolean blnFlag = false;
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
						break;
					}
				}
			} else {
				report.updateTestLog("Shepards Preview section displays at the right pane of the full document", "Shepards Preview section is not displayed at the right pane of the full document", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on node and select
	// showinList/Open Doc link
	// # Function Name : ClickNodeWithNumber_SelectLink     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickNodeWithNumberSelectLinkShepards(String strNumber, String strNodeTitle) {

		Boolean blnFlag = false;

		WebElement divResult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 10);
		if (divResult != null) {
			List<WebElement> lnkNode = commonLibrary.isExistList(divResult, By.tagName("tspan"), 10);
			for (WebElement item : lnkNode) {
				if (item.getText().equalsIgnoreCase(strNumber)) {

					try {

						JavascriptExecutor executor = (JavascriptExecutor) driver;

						executor.executeScript("window.focus()");

						WebElement Parent = commonLibrary.getParentElement(item);

						commonLibrary.clickLinkWithWebElementWithWait(Parent, strNumber);

					} catch (Exception e) {

					}

					WebElement imgClose = commonLibrary.isExist(UIMAP_Shepards.imgClose, 10);
					if (imgClose != null) {
						blnFlag = true;
						break;
					}
				}
			}
		}
		if (blnFlag) {
			report.updateTestLog("Click the node " + strNumber + "", "Node " + strNumber + " is clicked", Status.PASS);
			return new Shepards(scriptHelper);

		} else {
			throw new FrameworkException("Click the node " + strNumber + "", "Node " + strNumber + " is not clicked");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// Function call to logout
		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Map displayed in NormalView
	// # Function Name : VerifyMap_NormalView     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyMapNormalView() {

		// Verification point : Map displayed in normal view
		WebElement HeaderResults = commonLibrary.isExist(UIMAP_Shepards.HeaderResults, 10);

		if (HeaderResults != null)
			report.updateTestLog("Verify Map displays in normal view", "Map displays in normal view", Status.PASS);

		else

			report.updateTestLog("Verify Map displays in normal view", "Map is not displayed in normal view", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Map displayed in NormalView
	// # Function Name : ClickDisplayFullScreen     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickDisplayFullScreen() {

		// click on full screen button
		WebElement btnIdFullscreen = commonLibrary.isExist(UIMAP_Shepards.btnIdFullscreen, 10);
		commonLibrary.clickButtonParentWithWait(btnIdFullscreen, "Display full screen");

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify FullView_map
	// # Function Name : VerifyFullView_map     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyFullViewMap() {

		// Verification Point : Full Map View
		WebElement divIdDisplayOptions = commonLibrary.isExist(UIMAP_Shepards.divIdDisplayOptions, 10);

		if (divIdDisplayOptions != null)

			report.updateTestLog("Verify Appellate map displays in Full view", "Appellate map displays in Full view", Status.PASS);

		else

			report.updateTestLog("Verify Appellate map displays in Full view", "Appellate map not displayed in Full view", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify FullView_map
	// # Function Name : VerifyFullView_map     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickDisplayOptionCheckDocNoClickOk(Boolean isCheck) {

		// Call <T Click Button> with the following parameters: Button = Display
		// Options
		WebElement divIdDisplayOptions = commonLibrary.isExist(UIMAP_Shepards.divIdDisplayOptions, 10);
		WebElement divIdDisplayOptions1 = commonLibrary.isExist(divIdDisplayOptions, By.tagName("h3"), 10);

		if (browsername.contains("internet"))
			commonLibrary.clickJS(divIdDisplayOptions1, "Display Options");
		else
			commonLibrary.clickButtonParentWithWait(divIdDisplayOptions1, "Display Options");

		// <Uncheck> the checkbox <Documentnumber>
		WebElement chkIdDocumentNumber = commonLibrary.isExist(UIMAP_Shepards.chkIdDocumentNumber, 10);

		if (commonLibrary.setCheckBox(chkIdDocumentNumber, isCheck))
			if (isCheck)
				report.updateTestLog("Check the checkbox <Documentnumber>", "checkbox <Documentnumber> is checked", Status.PASS);

			else
				report.updateTestLog("Uncheck the checkbox <Documentnumber>", "checkbox <Documentnumber> is Unchecked", Status.PASS);
		else
			report.updateTestLog("Check/Uncheck the checkbox <Documentnumber>", "checkbox <Documentnumber> is checked/unchecked", Status.PASS);

		// Call <T Click Button> with the following parameters: Button = Ok
		WebElement btnValOK = commonLibrary.isExist(UIMAP_Shepards.btnValOK, 10);
		commonLibrary.clickButtonParentWithWait(btnValOK, "OK");

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify FullView_map
	// # Function Name : verifyAppellateViewDisplayed     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyAppellateViewDisplayed() {

		// Verification Point : Appellate list appears
		WebElement divIdShepListView = commonLibrary.isExist(UIMAP_Shepards.divIdShepListView, 10);

		if (divIdShepListView != null)
			report.updateTestLog("Verify Appellate list appears", "Appellate list appeared", Status.PASS);

		else
			report.updateTestLog("Verify Appellate list appears", "Appellate list not appeared", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : sendKeys_ESCAPE_Robot
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards sendKeysESCAPERobot() {

		try {

			// Pressing Escape button in keyboard
			Robot r;
			r = new Robot();

			r.keyPress(KeyEvent.VK_ESCAPE);
			r.keyRelease(KeyEvent.VK_ESCAPE);
			report.updateTestLog("Press Escape button in keyboard", "Pressed Escape button in keyboard", Status.PASS);

		} catch (AWTException e) {

			e.printStackTrace();
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : sendKeys_ESCAPE_Robot
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards exitFullScreenMap() {

		// Click on exit full screen button
		WebElement divIdDisplayOptions3 = commonLibrary.isExist(UIMAP_Shepards.divIdDisplayOptions, 10);

		if (divIdDisplayOptions3 != null) {

			WebElement btnIdFullscreen3 = commonLibrary.isExist(UIMAP_Shepards.btnIdFullscreen, 10);
			commonLibrary.clickButtonParentWithWait(btnIdFullscreen3, "Exit full screen");

		} else

			report.updateTestLog("Click on Exit full screen button inappellate map.", "Not Clicked on Exit full screen button inappellate map.", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : ClickShowInList
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickShowInList() {

		// Click on ShowInList
		WebElement divIdBubble = commonLibrary.isExist(UIMAP_Shepards.divIdBubble, 10);
		WebElement lnkIdShowInList = commonLibrary.isExist(divIdBubble, UIMAP_Shepards.lnkIdShowInList, 10);

		commonLibrary.clickLinkWithWebElementWithWait(lnkIdShowInList, lnkIdShowInList.getText());

		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download document using Choose
	// Settings
	// # Function Name : DownloadDocument_ChooseSettings    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards downloadDocumentChooseSettings(String FieldLabel, String FieldType, String Values, String fileName, String filePath, String PDFText, String TextExistence, String AutoITPath) {
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
			commonLibrary.fileExistsDelete(filePath, fileName);
			commonLibrary.sleep(6000);
			// WebElement btnDownload =
			// commonLibrary.isExist(UIMAP_Document.btnDownload, 10);

			// if (btnDownload != null) {
			// commonLibrary.clickButton_Parent_WithWait(btnDownload,
			// "Download");
			// }
			// // CLICK <<<Choose Settings>>> FROM THE DROP DOWN.
			//
			// WebElement ChooseSettings =
			// commonLibrary.isExist(UIMAP_Document.DownloadChooseSet, 10);
			// if (ChooseSettings != null) {
			// JavascriptExecutor executor = (JavascriptExecutor) driver;
			// executor.executeScript("arguments[0].focus();", ChooseSettings);
			// executor.executeScript("arguments[0].click();", ChooseSettings);
			// report.updateTestLog("Click on button Choose Settings",
			// "Clicked on button Choose Settings", Status.PASS);
			// commonLibrary.sleep(9000);

			generalFunctions.clickDeliverySelectOption_New("delivery", "Download(Choose new Settings)");

			WebElement ChooseSettDialog = commonLibrary.isExist(UIMAP_Document.DialogChooseSettings, 10);
			if (ChooseSettDialog != null) {
				WebElement tabBasicOpt = commonLibrary.isExist(UIMAP_Document.tabDLDialogBasicOpt);
				commonLibrary.clickButton(tabBasicOpt);

				for (int j = 0; j < arrFieldLabel.length; j++) {
					chooseDownLoadSettings(arrFieldLabel[j], arrFieldType[j], blnValues[j]);
				}

				chooseDownLoadSettings("Portable Document Format (.PDF)", "Radio", true);

				WebElement fileNameTxt = commonLibrary.isExist(UIMAP_Document.fileName, 10);
				commonLibrary.setDataInTextBox(fileNameTxt, fileName, "File Name");
				WebElement dilog = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 20);
				WebElement btndownload = commonLibrary.isExist(dilog, UIMAP_Shepards.btnDownload, 20);
				if (browsername.contains("internet")) {
					// try
					// {
					// driver.manage().timeouts().pageLoadTimeout(1,
					// TimeUnit.SECONDS);
					// // btndownload.click();
					// commonLibrary.click_MouseMove_Action(btndownload,
					// btndownload.getText());
					// }
					// catch(TimeoutException e)
					// {
					// driver.manage().timeouts().pageLoadTimeout(220,
					// TimeUnit.SECONDS);
					// System.out.println("timeout exception occured");
					// }
					// driver.manage().timeouts().pageLoadTimeout(220,
					// TimeUnit.SECONDS);
					commonLibrary.clickLinkWithWebElementWithWait(btndownload, btndownload.getText());
				} else {
					commonLibrary.clickLinkWithWebElementWithWait(btndownload, btndownload.getText());
				}
				if (!browsername.contains("internet"))
					commonLibrary.sleep(1000);

				commonLibrary.switchToWindow("deliverysecondarywindow");
				if (!browsername.contains("internet"))
					commonLibrary.sleep(25000);

			}

			else {
				report.updateTestLog("Download Dialog box opens on selecting 'Choose Settings'", "Download Dialog box is not opened on selecting 'Choose Settings'", Status.FAIL);
			}

			// }
			commonLibrary.windowFocus();
			if (browsername.contains("firefox")) {
				commonLibrary.sleep(4000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_S);
				// CTRL+Z is now pressed (receiving application should see a
				// "key down" event.)
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_ALT);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);

			} else if (browsername.contains("internet") && version.contains("8")) {
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath, "Save As", filePath, fileName };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet")) {

				commonLibrary.sleep(3000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_F6);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_TAB);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			}

			commonLibrary.sleep(30000);

			String downloadedFilename = filePath + "\\" + fileName;
			String header = null;

			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			try {
				do {
					pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
					if (pgHeader != null) {
						header = pgHeader.getText().toLowerCase();
						if (header.contains("delivery complete"))
							commonLibrary.sleep(2000);
					}
				} while (header.contains("processing"));
			} catch (StaleElementReferenceException e) {
				System.out.println(e);
			}

			try {
				commonLibrary.sleep(2000);
				WebElement lnkDownload = commonLibrary.isExist(UIMAP_Document.lnkDownloadedDoc, 10);
				pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);

				if (pgHeader != null && pgHeader.getText().toLowerCase().contains("delivery complete") && lnkDownload != null) {

					report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE ONCE THE DELIVERY IS COMPLETE.", Status.PASS);
				} else
					report.updateTestLog("Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.", "PROCESSING MSG POP UP is opened AND UPDATED TO A " + pgHeader.getText() + " MESSAGE .Document is not downloaded successfully", Status.FAIL);
			} catch (StaleElementReferenceException e) {
				WebElement lnkDownload = commonLibrary.isExist(UIMAP_Document.lnkDownloadedDoc, 10);
				if (lnkDownload != null)
					commonLibrary.clickJS(lnkDownload, "Download");
			}
			WebElement btnDownloadClose = commonLibrary.isExist(UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("shepards");

			// Unzip the document present
			String zipFile = filePath + "\\" + fileName + ".ZIP";
			int i1 = 0;
			do {
				zipFile = filePath + "\\" + fileName + ".ZIP";
				commonLibrary.sleep(50000);
			} while (zipFile == null && i1 < 15);
			generalFunctions.fileUnZip(filePath, zipFile);

			// Verify content in the downloaded document
			String arrPDFText[] = PDFText.split("#");
			String arrTextExistence[] = TextExistence.split(";");
			String FileDownloaded = downloadedFilename + ".PDF";
			for (int i = 0; i < arrPDFText.length; i++) {
				generalFunctions.pdfVerificationPath(FileDownloaded, arrPDFText[i], arrTextExistence[i]);
			}

			return new Shepards(scriptHelper);
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Clear Previously set filters in
	// Search Results page
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clearFilters() {
		WebElement btnClear = commonLibrary.isExist(UIMAP_Shepards.btnClear, 10);
		commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifySearchResultHeader(String strPageHeader) {

		pageCheck.positiveCheck(driver, "Shepards", "Shepards");
		generalFunctions.verifyPageTitle(strPageHeader);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying doc displayed in appelate
	// history page
	// # Function Name : verifyDocDisplayedInAppelateHistoryPage     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyDocDisplayedInAppelateHistoryPage(String strDocTitle) {

		WebElement lstSelected1 = commonLibrary.isExist(UIMAP_Shepards.lstSelected, 20);
		WebElement lnkAppelatehistory = commonLibrary.isExist(lstSelected1, UIMAP_Shepards.lnkAppelatehistory, 10);

		if (lnkAppelatehistory != null)

			report.updateTestLog("Verify the Shepard's report for " + strDocTitle + " displays on the <Appellate History> category page", "The Shepard's report for " + strDocTitle + " displays on the <Appellate History > category page", Status.PASS);
		else

			report.updateTestLog("Verify the Shepard's report for " + strDocTitle + " displays on the <Appellate History> category page", "The Shepard's report for " + strDocTitle + " is not displayed on the <Appellate History> category page", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for close and back to doc window
	// # Function Name : closeAndBack_ToDocumentWindow     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document closeAndBackToDocumentWindow() {

		driver.close();
		commonLibrary.smallWait();
		commonLibrary.switchToWindow("lexis.com/search");

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifing doc displayed in
	// CitatingDecisions page
	// # Function Name : verifyDocDisplayedInCitatingDecisionsPage     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyDocDisplayedInCitatingDecisionsPage(String strDocTitle) {

		WebElement lstSelected = commonLibrary.isExist(UIMAP_Shepards.lstSelected, 20);
		WebElement lnkCitingDecisions = commonLibrary.isExist(lstSelected, UIMAP_Shepards.lnkCitingDecisions, 10);

		if (lnkCitingDecisions != null)

			report.updateTestLog("Verify the Shepard's report for " + strDocTitle + " displays on the <CitingDecisions> category page", "The Shepard's report for " + strDocTitle + " displays on the <CitingDecisions> category page", Status.PASS);
		else

			report.updateTestLog("Verify the Shepard's report for " + strDocTitle + " displays on the <CitingDecisions> category page", "The Shepard's report for " + strDocTitle + " is not displayed on the <CitingDecisions> category page", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : ClickRightClick_StopImage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickRightDocumentTitle(String strDocumentTitle) {

		Boolean blnFlag = false;

		WebElement resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);
		if (resultClass != null) {

			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {

				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 20);
					if (eleDocTitle.getText().contains(strDocumentTitle)) {

						WebElement lnkDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.lnkDocTitle, 20);
						this.clickRightSelectOpenNewWindow(lnkDocTitle);

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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify secondary browser opened
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifySecondaryBroserOpened() {

		generalFunctions.openedSecondaryBrowser();

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to document
	// # Function Name : switchToDocument     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document switchToDocument(String strWindowName) {

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
			return new Document(scriptHelper);
		else
			return null;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Filters used.
	// # Function Name : verifyFiltersUsed
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Shepards verifyFiltersUsed(String strFilterName) {

		WebElement divClassFiltersUsed = commonLibrary.isExist(UIMAP_Shepards.divClassFiltersUsed, 10);

		if (divClassFiltersUsed != null && divClassFiltersUsed.getText().equalsIgnoreCase(strFilterName))

			report.updateTestLog("Verify The " + strFilterName + " filter appears under the You've Selected filter category", "The  " + strFilterName + " filter appears under the You've Selected filter category", Status.PASS);
		else

			report.updateTestLog("Verify The  " + strFilterName + " filter appears under the You've Selected filter category", "The  " + strFilterName + " filter is not appearing under the You've Selected filter category", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on subsequent app history
	// # Function Name : clickSubSeqAppHistory     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards clickSubSeqAppHistory() {
		WebElement lnkSubSeqAppHistory = commonLibrary.isExist(UIMAP_Shepards.lnkSubSeqAppHistory, 20);
		commonLibrary.clickLinkWithWebElementWithWait(lnkSubSeqAppHistory, lnkSubSeqAppHistory.getText());
		return new Shepards(scriptHelper);
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

			if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

			if (resultClass != null) {

				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {

					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {

						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
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
	// # Function Description : Function to close and come back to search
	// window
	// # Function Name : closeAndBack_ToSearchWindow     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Search closeAndBackToSearchWindow() {

		driver.close();
		commonLibrary.smallWait();
		commonLibrary.switchToWindow("lexis.com/search");

		return new Search(scriptHelper);
	}

	// /#*****************************************************************************************************************************
	// # Function Description : Function to Verify reset button is disabled
	// # Function Name : VerifyResetButtonDisabled
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyResetButtonDisabled() {
		WebElement btnReset = commonLibrary.isExist(UIMAP_Shepards.btnResetHighlight, 10);
		if (btnReset != null) {
			if (btnReset.getAttribute("disabled").equalsIgnoreCase("true")) {
				report.updateTestLog("Check whether Reset Highlight button is disabled", "Reset Highlight button is disabled", Status.PASS);
			} else {
				report.updateTestLog("Check whether Reset Highlight button is disabled", "Reset Highlight button is not disabled", Status.FAIL);
			}
		} else {
			report.updateTestLog("Check whether Reset Highlight button is disabled", "Reset Highlight button is not disabled", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check document number checkbox
	// # Function Name : checkDocNoAndClickOK
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards checkDocNoAndClickOK() {
		WebElement chkDocNo = commonLibrary.isExist(UIMAP_Shepards.chkDocNo, 10);
		if (chkDocNo != null) {
			commonLibrary.setCheckBox(chkDocNo, "Document number");
			commonLibrary.highlightElement(chkDocNo);
			// if(!(chkDocNo.isSelected()))
			// chkDocNo.click();
			WebElement btnOK = commonLibrary.isExist(UIMAP_Shepards.btnOK, 10);
			if (btnOK != null) {
				commonLibrary.clickButtonParentWithWait(btnOK, "OK");
			} else {
				report.updateTestLog("Click OK button", "OK button is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Check the checkbox Document Number", "Checkbox Document number is not available", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// /#*****************************************************************************************************************************
	// # Function Description : Function to Verify reset button is enabled
	// # Function Name : VerifyResetButtonEnabled
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyResetButtonEnabled() {
		WebElement btnReset1 = commonLibrary.isExist(UIMAP_Shepards.btnResetEnabled, 10);
		if (btnReset1 != null) {
			report.updateTestLog("Check whether Reset Highlight button is enabled", "Reset Highlight button is enabled", Status.PASS);
		} else {
			report.updateTestLog("Check whether Reset Highlight button is enabled", "Reset Highlight button is not enabled", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Manual Verification
	// # Function Name : ManualVerification
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards manualVerification() {
		report.updateTestLog("Check whether lines connecting other nodes are faded", "Manual Verification", Status.WARNING);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Manual Verification
	// # Function Name : ManualVerification
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards manualVerification1() {
		report.updateTestLog("Check whether lines connecting other nodes are reset", "Manual Verification", Status.WARNING);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Manual Verification
	// # Function Name : ManualVerification
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards manualVerification2() {
		report.updateTestLog("Check whether node2, node5 is gray. Node 9 is red yellow and affirmed as part. Node 1 and 2 connected with gray solid line and node 1 and 4 connected with gray dotted line ", "Manual Verification", Status.WARNING);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Manual Verification
	// # Function Name : ManualVerification
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards manualVerification3() {
		report.updateTestLog("Verify whether Node 13,15,23 and 53 is blue", "Manual Verification", Status.WARNING);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the x image to close the popup
	// # Function Name : ClickCloseImage
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards clickCloseImage() {
		WebElement imgClose = commonLibrary.isExist(UIMAP_Shepards.imgClose, 10);
		if (imgClose != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWait(imgClose, "Close information bubble");
			else
				imgClose.click();
			report.updateTestLog("Click the X button in information bubble", "X button in information bubble is clicked", Status.PASS);

		} else {
			report.updateTestLog("Click the X button in information bubble", "X button in information bubble is not available", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click reset highlight button
	// # Function Name : ClickResetButton
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards clickResetButton() {
		WebElement btnReset2 = commonLibrary.isExist(UIMAP_Shepards.btnResetEnabled, 10);
		if (btnReset2 != null) {
			commonLibrary.clickButtonParentWithWait(btnReset2, "Reset Highlight");
		} else {
			report.updateTestLog("Click Reset Highlight button", "Reset Highlight button is not available", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whether a particular node is
	// connected with gray line
	// # Function Name : VerifyNodeConnectedWithRedLine
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyNodeConnectedWithGrayLine() {

		boolean blnFlag3 = false;
		WebElement divresult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 20);
		List<WebElement> path = commonLibrary.isExistList(divresult, By.tagName("path"), 10);
		for (WebElement item : path) {
			if (item.getAttribute("stroke").contains("#ed1c24")) {
				blnFlag3 = true;
				break;
			}

		}
		if (blnFlag3) {
			report.updateTestLog("Check the Star node and Node9 are connectedwith a  gray solid line", "Star node and Node9 are connectedwith a  gray solid line", Status.PASS);
		} else {
			report.updateTestLog("Check the Star node and Node9 are connectedwith a  gray solid line", "Star node and Node9 are not connectedwith a  gray solid line", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check image x is present
	// # Function Name : VerifyCloseButton
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyCloseButton(String strSearchTerm) {
		WebElement imgClose1 = commonLibrary.isExist(UIMAP_Shepards.imgClose, 10);
		if (imgClose1 != null) {
			report.updateTestLog("Check whether the information bubble for " + strSearchTerm + " is opened", "The information bubble for " + strSearchTerm + " is opened", Status.PASS);
		} else {
			report.updateTestLog("Check whether the information bubble for " + strSearchTerm + " is opened", "The information bubble for " + strSearchTerm + " is not opened", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check image x is not present
	// # Function Name : VerifyCloseButtonNotPresent
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards verifyCloseButtonNotPresent(String strSearchTerm) {
		WebElement imgClose2 = commonLibrary.isExistNegative(UIMAP_Shepards.imgClose, 10);
		if (imgClose2 == null) {
			report.updateTestLog("Check whether the information bubble for " + strSearchTerm + " is closed", "The information bubble for " + strSearchTerm + " is closed", Status.PASS);
		} else {
			report.updateTestLog("Check whether the information bubble for " + strSearchTerm + " is closed", "The information bubble for " + strSearchTerm + " is not closed", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Shepards Report Tab And Title
	// # Function Name : VerifyShepardsReportTabAndTitle
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepardsReportTabAndTitle(String strTab, String strDocTitle) {
		WebElement eltResultListWrapper = null, eltResultListHeader = null, eltShepListHeader1 = null, lnkTopLineTitle = null;
		eltResultListWrapper = commonLibrary.isExistNegative(UIMAP_Shepards.eltResultListWrapper, 10);
		eltResultListHeader = commonLibrary.isExistNegative(eltResultListWrapper, UIMAP_Shepards.eltResultListHeader, 10);
		eltShepListHeader1 = commonLibrary.isExistNegative(eltResultListHeader, UIMAP_Shepards.eltShepListHeader1, 10);

		lnkTopLineTitle = commonLibrary.isExistNegative(UIMAP_Shepards.lnkTopLineTitle, 10);
		if (lnkTopLineTitle == null) {
			lnkTopLineTitle = commonLibrary.isExist(UIMAP_Shepards.hdrResult1, 10);
		}
		if (eltShepListHeader1.getText().contains(strTab) && lnkTopLineTitle.getText().contains(strDocTitle))
			report.updateTestLog("Verify Shepard's report for " + strDocTitle + " displays on the " + strTab + " category page.", "Shepard's report for " + strDocTitle + " displays on the " + strTab + " category page.", Status.PASS);
		else
			report.updateTestLog("Verify Shepard's report for " + strDocTitle + " displays on the " + strTab + " category page.", "Shepard's report for " + strDocTitle + " is not displayed on the " + strTab + " category page.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click back button on browser from
	// Shepards Reoprt page to go to Document page.
	// # Function Name : ClickBackShepReportToDoc
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document clickBackShepReportToDoc() {
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
	// # Function Description : Function for verifying the court headers
	// # Function Name : VerifyCourtHeader     
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyCourtHeader() {

		WebElement eltGridContent = commonLibrary.isExist(UIMAP_Shepards.divAnalysisByCourt, 10);
		if (eltGridContent != null) {
			WebElement eltGridHeader = commonLibrary.isExist(eltGridContent, UIMAP_Shepards.tagHeader, 10);
			if (eltGridHeader != null) {
				report.updateTestLog("Verify Court Headers are displayed under  Analysis By Court", "Court Headers are displayed under  Analysis By Court", Status.PASS);
			} else {
				report.updateTestLog("Verify Court Headers are displayed under  Analysis By Court", "Court Headers are not displayed under  Analysis By Court", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Court Headers are displayed under  Analysis By Court", "Court Headers are not displayed under  Analysis By Court", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : SimpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking the right click or
	// shepardize this document preview
	// # Function Name : RightClickShepardizeThisDocShepPreview
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document rightClickShepardizeThisDocShepPreview() {

		WebElement lnkShepardizeThisDoc = commonLibrary.isExist(UIMAP_Shepards.lnkShepardizeThisDoc, 20);
		// commonLibrary.ScrollToView(lnkShepardizeThisDoc);
		this.clickRightSelectOpenNewWindow(lnkShepardizeThisDoc);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red stop
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

			String browsername = cap.getBrowserName();
			if (browsername.equalsIgnoreCase("firefox")) {
				oAction.contextClick(element).sendKeys("W").build().perform();
			} else if (browsername.equalsIgnoreCase("internet explorer")) {
				oAction.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();

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
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String strDocTitle) {
		// Boolean blnFlag = false;
		//
		// WebElement resultClass = null;
		//
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		//
		// if (commonLibrary.isExist_Negative(UIMAP_Shepards.frmClassResult, 10)
		// != null)
		// resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult,
		// 10);
		//
		// if (resultClass != null) {
		// WebElement OListResult = commonLibrary.isExist(resultClass,
		// By.tagName("ol"), 20);
		// if (OListResult != null) {
		// List<WebElement> OListItems = commonLibrary.isExist_List(OListResult,
		// By.tagName("li"), 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_Shepards.TitleClassDoc, 2);
		// if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// if (browsername.contains("internet")) {
		// // try
		// // {
		// // driver.manage().timeouts().pageLoadTimeout(1,
		// // TimeUnit.SECONDS);
		// // commonLibrary.click_MouseMove_Action(lnkDocument,
		// // lnkDocument.getText());
		// // } catch (TimeoutException ex) {
		// // driver.manage().timeouts().pageLoadTimeout(220,
		// // TimeUnit.SECONDS);
		// // System.out.println(ex.toString());
		// //
		// // }
		// // driver.manage().timeouts().pageLoadTimeout(220,
		// // TimeUnit.SECONDS);
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// }
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// else {
		// commonLibrary.clickLink_withWebElement_WithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// }
		// }
		// }
		//
		// }
		//
		// }
		// }
		//
		// int count = 0;
		// WebElement h1 = commonLibrary.isExist(UIMAP_Shepards.h1, 10);
		// do {
		// count++;
		// h1 = commonLibrary.isExist(UIMAP_Shepards.h1, 10);
		// commonLibrary.sleep(3000);
		// } while (h1 == null && count < 15);
		// if (!blnFlag)
		//
		// report.updateTestLog("Click on the document " + strDocTitle,
		// "Not Clicked  on the document " + strDocTitle, Status.FAIL);
		// else {
		// WebElement pgHeader = null;
		//
		// if (commonLibrary.isExist_Negative(UIMAP_Shepards.TitleClassTOC, 10)
		// != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_Shepards.TitleClassTOC, 10);
		// else if
		// (commonLibrary.isExist_Negative(UIMAP_Shepards.pgClassHeaderOption3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_Shepards.pgClassHeaderOption3,
		// 10);
		// else if
		// (commonLibrary.isExist_Negative(UIMAP_Shepards.SearchResultHeader3,
		// 10) != null)
		// pgHeader =
		// commonLibrary.isExist_Negative(UIMAP_Shepards.SearchResultHeader3,
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
	// # Function Description : Function to Verify recently viewed icon
	// # Function Name : Verify_RecentlyViewed    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyRecentlyViewed(String strDocTitle) {
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);
		int count = 0;
		do {
			count++;
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);
			commonLibrary.sleep(2000);

		} while (resultClass == null && count < 15);
		;
		Boolean blnFlag = false;
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
						WebElement RecntlyViewed = commonLibrary.isExist(document, UIMAP_Shepards.btnRecentlyViewed, 2);
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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on reference link of a
	// document
	// # Function Name : Click_Doc_Ref_Link    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document clickDocRefLink(String strDocTitle, String RefLink) {
		WebElement resultClass = null;

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);
		Boolean blnFlag = false;
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
						WebElement FirstRef = commonLibrary.isExist(document, UIMAP_Shepards.lnkFirstRef, 2);
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

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on subsection report
	// # Function Name : clickSubSectionReportLink     
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*
	public Shepards clickSubSectionReportLink() {

		WebElement lnkSubsecReports = commonLibrary.isExist(UIMAP_Shepards.lnkSubsecReports, 20);
		// clicking on the subsection report
		if (lnkSubsecReports != null) {
			commonLibrary.clickLinkWithWebElementWithWait(lnkSubsecReports, "Subsection Reports");
		} else {
			report.updateTestLog("click on Subsection Reports link", "Subsection Reports link is NOT clicked", Status.FAIL);
		}

		commonLibrary.sleep(1000);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickDocLinkShepards(String strDocTitle) {
		generalFunctions.clickDocLink(strDocTitle);
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on email
	// # Function Name : clickEmail_EnterToAddress_Send     
	// # Author : Baswaraj
	// # Date Created :Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickEmailEnterToAddressSend(String strEmailAddress, String fileName) {

		// to click on the email option from delivery options

		generalFunctions.clickDeliverySelectOption_New("delivery", "Email");

		// Entering the data into the Email related fields

		WebElement fileNameField = commonLibrary.isExist(UIMAP_Shepards.fileNameField, 10);
		if (fileNameField != null)
			commonLibrary.setDataInTextBox(fileNameField, fileName, "FileName");
		else
			report.updateTestLog("Filename field existance", "filename field is not present", Status.FAIL);
		WebElement txtboxEmailAddress = commonLibrary.isExist(UIMAP_Shepards.txtToAddress, 20);
		if (txtboxEmailAddress != null) {

			commonLibrary.setDataInTextBox(txtboxEmailAddress, strEmailAddress, "To");
			// to click on the Email to send
			WebElement dilog = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 20);
			WebElement sectionFooter1 = commonLibrary.isExist(dilog, By.tagName("footer"), 20);
			WebElement btnSubmitEmail = commonLibrary.isExist(sectionFooter1, UIMAP_Shepards.btnSubmitEmail, 20);
			if (btnSubmitEmail != null) {
				commonLibrary.clickLinkWithWebElementWithWait(btnSubmitEmail, "Email");

			} else
				report.updateTestLog("click on Email link", "Email link is NOT clicked", Status.FAIL);
		} else {
			report.updateTestLog("Entering Email Address into To TextBox", "To TextBox is Not Present", Status.FAIL);

		}
		// }
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to swith to window delivery
	// # Function Name : switchToDelivery     
	// # Author : Basawraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Delivery switchToDelivery(String strWindowName) {

		if (commonLibrary.switchToWindow(strWindowName))
			return new Delivery(scriptHelper);
		else
			return null;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a text present in page within
	// a list
	// # Function Name : VerifyText_DocView_withinList     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyTextWithInList(By by, String Text, String TextType) {
		List<WebElement> objList = commonLibrary.isExistList(by, 10);
		Boolean blnFlag = false;
		for (WebElement item : objList) {
			if (item.getText().contains(Text)) {
				blnFlag = true;
				break;
			}

		}
		if (blnFlag) {
			report.updateTestLog("'" + Text + "' " + TextType + " is displayed", "'" + Text + "' " + TextType + " is displayed", Status.PASS);

		} else {
			report.updateTestLog("'" + Text + "' " + TextType + " is displayed", "'" + Text + "' " + TextType + " is displayed", Status.PASS);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton with SmallWait
	// # Function Name : clickButton_Log_SmallWait(WebElement obj, String
	// strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards clickButtonLogParentSmallWait(By by, By by1, String strBtnName) {

		WebElement parent = commonLibrary.isExistNegative(by, 10);
		WebElement obj = commonLibrary.isExistNegative(parent, by1, 10);
		commonLibrary.clickButtonLogSmallWait(obj, strBtnName);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify node is grayed and labeled as
	// review rehearing
	// # Function Name : VerifyNode2IsGrayedAndLabeled
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyNode2IsGrayedAndLabeled(String strTitle, String strTitle1) {
		clickNodeToActivateThePage("2", "");
		boolean blnGray = false;
		boolean blnGray1 = false;
		// WebElement
		// divResult=commonLibrary.isExist(UIMAP_ShepardsPage.divResultPage,
		// 10);
		WebElement divResult = commonLibrary.isExist(UIMAP_Shepards.divWow, 10);
		if (divResult != null) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) driver;

				executor.executeScript("window.focus()");
				List<WebElement> lnkNode = commonLibrary.isExistList(divResult, UIMAP_Shepards.link, 10);
				for (WebElement item : lnkNode) {
					if (item.getAttribute("title").toLowerCase().contains(strTitle.toLowerCase())) {
						List<WebElement> rect = commonLibrary.isExistList(item, UIMAP_Shepards.rect, 10);
						for (WebElement item1 : rect) {
							if (item1.getAttribute("stroke").contains("#e0e0e0")) {
								blnGray = true;
							}
						}
					} else if (item.getAttribute("title").toLowerCase().contains(strTitle1.toLowerCase())) {
						List<WebElement> rect = commonLibrary.isExistList(item, UIMAP_Shepards.rect, 10);
						for (WebElement item1 : rect) {
							if (item1.getAttribute("stroke").contains("#e0e0e0")) {
								blnGray1 = true;
							}
						}
					}
					if (blnGray && blnGray1)
						break;
				}
			} catch (Exception e) {

			}

		}
		if (blnGray && blnGray1) {
			report.updateTestLog("Verify whether node with label " + strTitle + " and " + strTitle1 + " is gray", "Node with label " + strTitle + " " + strTitle1 + " is gray", Status.PASS);
		} else {
			report.updateTestLog("Verify whether node with label " + strTitle + " " + strTitle1 + " is gray", "Node with label " + strTitle + " " + strTitle1 + " is not gray", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : simpleSearchShepards     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards simpleSearchShepards(String strSearchTerm, Boolean strClearFilter) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");

		if (strClearFilter) {
			WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 2000);
			// commonLibrary.highlightElement(btnClassFilter);
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

			WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
			// commonLibrary.highlightElement(btnClassClearFilter);
			commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

			WebElement btnIdSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);
			// commonLibrary.ScrollToView(btnIdSubSearch);
			// commonLibrary.highlightElement(btnIdSubSearch);
			commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
		} else {
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		}

		return new Shepards(scriptHelper);
	}

	// # Function Description : Function for verifying node colour and label
	// # Function Name : VerifyNodeColourAndLabel
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyNodeColourAndLabel(String strTitle2) {
		// ClickNodeToActivateThePage("33","");
		boolean blnBlue = false;
		WebElement divResult1 = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 10);
		if (divResult1 != null) {
			List<WebElement> lnkNode = commonLibrary.isExistList(divResult1, UIMAP_Shepards.link, 10);
			for (WebElement item : lnkNode) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("window.focus()");
				if (item.getAttribute("title").toLowerCase().contains(strTitle2.toLowerCase())) {
					List<WebElement> rect = commonLibrary.isExistList(item, UIMAP_Shepards.rect, 10);
					for (WebElement item1 : rect) {
						if (item1.getAttribute("stroke").contains("#003399")) {
							blnBlue = true;
						} else {
							blnBlue = false;
							break;
						}
					}
				}
			}
		}
		if (blnBlue) {
			report.updateTestLog("Verify whether node with label " + strTitle2 + " is blue", "Node with label " + strTitle2 + " is blue", Status.PASS);
		} else {
			report.updateTestLog("Verify whether node with label " + strTitle2 + " is blue", "Node with label " + strTitle2 + " is not blue", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying list view Greyed out
	// # Function Name : VerifyListViewGredOut
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyListViewGreyedOut() {
		WebElement btnActiveList = commonLibrary.isExistNegative(UIMAP_Shepards.btnActiveList, 10);
		if (btnActiveList.getText().toLowerCase().contains("list")) {
			report.updateTestLog("Verify the list view is displayed and list button is greyed out.", "List view is displayed and list button is greyed out.", Status.PASS);
		} else
			report.updateTestLog("Verify the list view is displayed and list button is greyed out.", "List view is not displayed", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying grid view Greyed out
	// # Function Name : VerifygridViewGredOut
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifygridViewGreyedOut() {
		WebElement divWrapperPanel1 = commonLibrary.isExist(UIMAP_Shepards.divWrapperPanel, 10);
		WebElement btnActiveGrid = commonLibrary.isExistNegative(divWrapperPanel1, UIMAP_Shepards.btnActiveGrid, 10);
		if (btnActiveGrid != null && btnActiveGrid.getText().toLowerCase().contains("grid")) {
			report.updateTestLog("Verify the Grid view displays and the Grid button is grayed out/inactive", "Grid view  is displayed and the Grid button is grayed out/inactive", Status.PASS);
		} else
			report.updateTestLog("Verify the Grid view displays and the Grid button is grayed out/inactive", "Grid view  is not displayed and the Grid button is not grayed out/inactive", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot of current page
	// # Function Name : TakeScreenShot     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			String strScreenshot = strTCName + commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot + ".jpg";

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep, "Perform Manual Verification for this step. screenshot is saved in " + strDestination + "", Status.WARNING);
			commonLibrary.sleep(3000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the column order in Y axis
	// # Function Name : TreatmentPhrase_YAxis_ColumnOrderVerify     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards treatmentPhraseYAxisColumnOrderVerify(String strCourtYaxis) {
		String[] strCourtYaxisList = strCourtYaxis.split(";");
		Boolean blnFlag = true;
		List<WebElement> eltYaxisContainerList = commonLibrary.isExistList(UIMAP_Shepards.eltYaxisContainer, 10);
		List<WebElement> lnkYaxisList = commonLibrary.isExistList(eltYaxisContainerList.get(0), UIMAP_Shepards.lnkYaxis, 10);

		commonLibrary.sleep(2000);

		for (int i = 0; i < strCourtYaxisList.length; i++) {
			if (!lnkYaxisList.get(i).getText().contains(strCourtYaxisList[i])) {
				blnFlag = false;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Treatment phrases  displays in the following order along the Y axis:Warning,Questioned, Caution, Positive, Neutral,Cited byin the 'Citing Decisions: Analysisby Court'  grid.", strCourtYaxis + "  is displayed in correct order.", Status.PASS);

		else
			report.updateTestLog("Treatment phrases  displays in the following order along the Y axis:Warning,Questioned, Caution, Positive, Neutral,Cited byin the 'Citing Decisions: Analysisby Court'  grid.", strCourtYaxis + "  is not displayed in correct order.", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the column order in Y axis
	// # Function Name : TreatmentPhrase_YAxis_ColumnOrderVerify     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards treatmentPhraseYAxisDateOrderVerify(String strDateYaxis) {
		String[] strDateYaxisList = strDateYaxis.split(";");
		Boolean blnFlag = true;
		List<WebElement> eltYaxisContainerList = commonLibrary.isExistList(UIMAP_Shepards.eltYaxisContainer, 10);
		List<WebElement> lnkYaxisList = commonLibrary.isExistList(eltYaxisContainerList.get(1), UIMAP_Shepards.lnkYaxis, 10);
		for (int i = 0; i < strDateYaxisList.length; i++) {
			if (!lnkYaxisList.get(i).getText().contains(strDateYaxisList[i])) {

				blnFlag = false;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Treatment phrases  displays inthefollowing order along the Y axis:Warning,Questioned, Caution, Positive, Neutral,Cited byin the 'Citing Decisions: Analysisby Date'  grid.", strDateYaxis + "  is displayed in correct order.", Status.PASS);

		else
			report.updateTestLog("Treatment phrases  displays inthefollowing order along the Y axis:Warning,Questioned, Caution, Positive, Neutral,Cited byin the 'Citing Decisions: Analysisby Date'  grid.", strDateYaxis + "  is not displayed in correct order.", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the court order in Y axis
	// # Function Name : VerifyCourtOrder     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyCourtOrder(String[] strCourtXaxisList) {
		List<WebElement> eltCourtNameLinksContainer = commonLibrary.isExistList(UIMAP_Shepards.eltCourtNameLinksContainer, 10);
		List<WebElement> CourtNamesList = commonLibrary.isExistList(eltCourtNameLinksContainer.get(0), UIMAP_Shepards.lnkCourtName, 10);
		Boolean blnFlag = true;
		for (int i = 0; i < CourtNamesList.size(); i++) {
			if (!CourtNamesList.get(i).getText().contains(strCourtXaxisList[i])) {
				blnFlag = false;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify 'Courts' displays in order from highest to lowest", "Courts are displayed in order from highest to lowest.", Status.PASS);
		else
			report.updateTestLog("Verify 'Courts' displays in order from highest to lowest", "Courts are not displayed in order from highest to lowest.", Status.PASS);

		commonLibrary.sleep(5000);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the court order in Y axis
	// # Function Name : VerifyCourtOrder     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document verifyAppellateDocClickSHowMapOpenDocVerifyDoc(String strDocTitle) {
		// MV
		WebElement divWrapperPanel = commonLibrary.isExistNegative(UIMAP_Shepards.divWrapperPanel, 10);
		WebElement btnActiveList = commonLibrary.isExistNegative(divWrapperPanel, UIMAP_Shepards.btnActiveList, 10);
		if (btnActiveList.getText().toLowerCase().contains("list")) {
			report.updateTestLog("Verify the appellate list is displayed' ", "Appellate list is displayed", Status.PASS);
		} else
			report.updateTestLog("Verify the appellate list is displayed' ", "Appellate list is not displayed", Status.FAIL);
		// MV
		WebElement eltSelectedItem = commonLibrary.isExistNegative(UIMAP_Shepards.eltSelectedItem, 10);
		WebElement TitleClassDoc = commonLibrary.isExistNegative(eltSelectedItem, UIMAP_Shepards.TitleClassDoc, 10);
		if (eltSelectedItem != null && TitleClassDoc.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
			report.updateTestLog("Verify the results list  displays at 'Sheppard v. Maxwell' ", "Result list is displayed at required location", Status.PASS);
		} else
			report.updateTestLog("Verify the results list  displays at 'Sheppard v. Maxwell' ", "Result list is not displayed at required location", Status.FAIL);

		WebElement lnkShowInMap = commonLibrary.isExistNegative(eltSelectedItem, UIMAP_Shepards.lnkShowInMap, 10);
		commonLibrary.clickLinkWithWebElementWithWait(lnkShowInMap, "Show in map");

		// MV
		WebElement btnActiveMap = commonLibrary.isExistNegative(UIMAP_Shepards.btnActiveMap, 10);
		if (btnActiveMap != null && btnActiveMap.getText().toLowerCase().contains("map")) {
			report.updateTestLog("Verify the <Appellate Map> view displays and the <Map>button is grayed out/inactive", "<Appellate Map> view  is displayed and the <Map>button is grayed out/inactive", Status.PASS);
		} else
			report.updateTestLog("Verify the <Appellate Map> view displays and the <Map>button is grayed out/inactive", "<Appellate Map> view  is not displayed and the <Map>button is not grayed out/inactive", Status.FAIL);
		// MV
		WebElement lnkOpenDocument = commonLibrary.isExistNegative(UIMAP_Shepards.lnkOpenDocument, 10);
		if (lnkOpenDocument != null) {
			// commonLibrary.highlightElement(lnkOpenDocument);

			try {
				commonLibrary.clickMethod(lnkOpenDocument, "Open Document");
			} catch (Exception e) {
			}
			report.updateTestLog("Verify 'Information bubble' is open", "'Information bubble' is open", Status.PASS);
		} else
			report.updateTestLog("Verify 'Information bubble' is open", "'Information bubble' is not open", Status.FAIL);

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Shepard's document
	// # Function Name : SelectShepDocumentByTitle    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards selectShepDocumentByTitle(String strDocName) {

		WebElement getItNow = commonLibrary.isExist(UIMAP_SearchResult.getItNowSearch, 10);
		if (getItNow != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickButtonParentWithWait(getItNow, "GetItNow");
			else
				commonLibrary.clickButtonParentWithWait(getItNow, "GetItNow");

			WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(5000);
				ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
			} while (ulCondentSwitcher == null && count < 36);
		}

		int i;
		boolean blnFlag = false;
		String strDocTitle = null;
		// commonLibrary.sleep(1000);
		List<WebElement> OList = commonLibrary.isExistList(By.tagName("ol"), 10);
		List<WebElement> LList = commonLibrary.isExistList(OList.get(0), By.tagName("li"), 10);
		for (i = 0; i < LList.size(); i++) {
			WebElement lnkTitle = commonLibrary.isExist(LList.get(i), By.cssSelector("a[data-action='midlinetitle']"), 10);
			strDocTitle = lnkTitle.getText();
			if (strDocTitle.trim().contains(strDocName.trim())) {
				WebElement lstchkBox = commonLibrary.isExist(LList.get(i), By.cssSelector("input[type='checkbox']"), 10);
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

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Email button and verify email
	// popup
	// # Function Name : SelectEmailButton    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards selectEmailButton() {
		// WebElement btnEmail =
		// commonLibrary.isExist(UIMAP_Shepards.btnEmail);
		// commonLibrary.clickButton_Log_SmallWait(btnEmail, "E-mail");

		try {
			generalFunctions.clickDeliverySelectOption_New("delivery", "Email");
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		WebElement eltEmailPopup = null;

		eltEmailPopup = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 20);
		if (eltEmailPopup != null)
			report.updateTestLog("Verify E-mail pop-up is displayed.", "Email pop-up is displayed", Status.PASS);
		else
			report.updateTestLog("Verify E-mail pop-up is displayed.", "Email pop-up is not displayed", Status.FAIL);
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the contents of the e-mail
	// popup
	// # Function Name : VerifyEmailPopupContents    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyEmailPopupContents() {
		int i;
		boolean blnFlag = false;
		WebElement eltEmailPopup = commonLibrary.isExistNegative(UIMAP_Shepards.eltEmailPopup, 10);

		List<WebElement> eltEmailDialogRows = commonLibrary.isExistList(eltEmailPopup, UIMAP_Document.divDLDialogRows, 10);
		for (i = 0; i < eltEmailDialogRows.size(); i++) {
			if (eltEmailDialogRows.get(i).getText().contains("Legislative History")) {
				if (!eltEmailDialogRows.get(i).getText().contains("(")) {
					report.updateTestLog("Verify Total midline count 'Does not Exist' next to 'Legislative History' under Categories section.", "Total midline count 'Does not Exist' next to 'Legislative History'", Status.PASS);
				} else
					report.updateTestLog("Verify Total midline count 'Does not Exist' next to 'Legislative History' under Categories section.", "Total midline count 'Exists' next to 'Legislative History'", Status.FAIL);
				break;
			}
		}

		for (i = 0; i < eltEmailDialogRows.size(); i++) {
			if (eltEmailDialogRows.get(i).getText().contains("Citing Decisions")) {
				if (eltEmailDialogRows.get(i).getText().contains("(")) {
					report.updateTestLog("Verify Total midline count 'Exist' next to 'CitingDecisions' under Categories section.", "Total midline count 'Exist' next to 'CitingDecisions' under Categories section", Status.PASS);
				} else
					report.updateTestLog("Verify Total midline count 'Exist' next to 'CitingDecisions' under Categories section.", "Total midline count 'Does not Exist' next to 'CitingDecisions' under Categories section", Status.FAIL);
				break;
			}
		}
		int intCitDec = i;

		for (i = 0; i < eltEmailDialogRows.size(); i++) {
			if (eltEmailDialogRows.get(i).getText().contains("Other Citing Sources")) {
				if (eltEmailDialogRows.get(i).getText().contains("(")) {
					report.updateTestLog("Verify Total midline count 'Exist' next to 'Other CitingSources' under Categories section.", "Total midline count 'Exist' next to 'Other CitingSources' under Categories section", Status.PASS);
				} else
					report.updateTestLog("Verify Total midline count 'Exist' next to 'Other CitingSources' under Categories section.", "Total midline count 'Does not Exist' next to 'Other CitingSources' under Categories section", Status.FAIL);
				break;
			}
		}
		int intOthCitSrcs = i;

		this.chooseDownLoadSettings("Citing Decisions", "CheckBox", true);
		blnFlag = false;
		List<WebElement> eltCitDcsnSbCntnt = commonLibrary.isExistList(eltEmailDialogRows.get(intCitDec), UIMAP_Document.divDLDialogRows, 10);
		for (i = 0; i < eltCitDcsnSbCntnt.size(); i++) {
			if (eltCitDcsnSbCntnt.get(i).getText().toLowerCase().contains("\"Narrowed by\" view".toLowerCase())) {
				blnFlag = true;
				report.updateTestLog("Verify 'Narrowed by View' displays below 'CitingDecisions' checkbox.", "'Narrowed by View' displays below 'CitingDecisions' checkbox", Status.PASS);
				if (eltCitDcsnSbCntnt.get(i).getText().contains("(")) {
					report.updateTestLog("Verify Midline count 'Exist' next to '\"Narrowed by\" view' under 'Citing Decisions' check box.", "Midline count 'Exist' next to '\"Narrowed by\" view' under 'Citing Decisions' check box.", Status.PASS);
				} else
					report.updateTestLog("Verify Midline count 'Exist' next to '\"Narrowed by\" view' under 'Citing Decisions' check box.", "Midline count 'Does not Exist' next to '\"Narrowed by\" view' under 'Citing Decisions' check box.", Status.FAIL);
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify 'Narrowed by View' displays below 'CitingDecisions' checkbox.", "'Narrowed by View' does not displays below 'CitingDecisions' checkbox", Status.FAIL);

		this.chooseDownLoadSettings("Other Citing Sources", "CheckBox", true);

		blnFlag = false;
		List<WebElement> eltOthCitSrcsSbCntnt = commonLibrary.isExistList(eltEmailDialogRows.get(intOthCitSrcs), UIMAP_Document.divDLDialogRows, 10);
		for (i = 0; i < eltOthCitSrcsSbCntnt.size(); i++) {
			if (eltOthCitSrcsSbCntnt.get(i).getText().toLowerCase().contains("\"Narrowed by\" view".toLowerCase())) {
				blnFlag = true;
				report.updateTestLog("Verify 'Narrowed by View' displays below 'Other Citing Sources' checkbox.", "'Narrowed by View' displays below 'Other Citing Sources' checkbox", Status.PASS);
				if (eltOthCitSrcsSbCntnt.get(i).getText().contains("(")) {
					report.updateTestLog("Verify Midline count 'Exist' next to '\"Narrowed by\" view' under 'Other Citing Sources' check box.", "Midline count 'Exist' next to '\"Narrowed by\" view' under 'Other Citing Sources' check box.", Status.PASS);
				} else
					report.updateTestLog("Verify Midline count 'Exist' next to '\"Narrowed by\" view' under 'Other Citing Sources' check box.", "Midline count 'Does not Exist' next to '\"Narrowed by\" view' under 'Other Citing Sources' check box.", Status.FAIL);
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify 'Narrowed by View' displays below 'Other Citing Sources' checkbox.", "'Narrowed by View' does not displays below 'Other Citing Sources' checkbox", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set text in To field of E-mail popup
	// # Function Name : SetEmailTo    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards setEmailTo(String strEmail) {
		WebElement txtEmail = commonLibrary.isExistNegative(UIMAP_Shepards.txtEmail, 10);
		commonLibrary.setDataInTextBox(txtEmail, strEmail, "To");
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Click on email button handle secondary window
	// and report manual verification steps if any.
	// # Function Name : ClickEmailHandlePopup    
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards clickEmailHandlePopup(int from, int to, String strEmail, String strTCName, String strStep) {
		boolean blnFlag = false;
		WebElement eltEmailPopup = commonLibrary.isExistNegative(UIMAP_Shepards.eltEmailPopup, 10);

		WebElement dilog = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 20);
		// WebElement sectionFooter1 =
		// commonLibrary.isExist(dilog,By.tagName("footer"), 20);
		WebElement btnSubmitEmail = commonLibrary.isExist(dilog, UIMAP_Shepards.btnSubmitEmail, 20);
		if (btnSubmitEmail != null) {
			commonLibrary.clickLinkWithWebElementWithWait(btnSubmitEmail, "Email");

		} else
			report.updateTestLog("click on Email link", "Email link is NOT clicked", Status.FAIL);
		this.takeScreenShot(strTCName, strStep);
		report.updateTestLog("Verify Banner message appears.", "Manually verify banner message.", Status.WARNING);
		eltEmailPopup = commonLibrary.isExistNegative(UIMAP_Shepards.eltEmailPopup, 10);
		if (eltEmailPopup == null)
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box closes.", Status.PASS);
		else
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box does not close.", Status.FAIL);
		commonLibrary.switchToWindow("deliverysecondarywindow");
		WebElement txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_Shepards.txtPopUpHeader, 10);
		if (txtPopUpHeader != null)
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up opens.", Status.PASS);
		else
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up does not open.", Status.FAIL);

		while (txtPopUpHeader.getText().contains("Processing")) {

			try {
				commonLibrary.sleep(5000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_Shepards.txtPopUpHeader, 10);

		}
		blnFlag = false;
		if (txtPopUpHeader.getText().contains("Complete"))
			blnFlag = true;
		else
			blnFlag = false;

		if (!(from == 0 && to == 0)) {
			if (blnFlag)
				report.updateTestLog("Step" + from + "-" + to, "Email has been sent to: " + strEmail + " .Please verify these steps manually.", Status.WARNING);
			else
				report.updateTestLog("Step" + from + "-" + to, "Email sending failed.", Status.FAIL);
		}
		driver.close();
		commonLibrary.switchToWindow("shepards");
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Set values in Choose Download/Print
	// Settings dialog
	// # Function Name : ChooseDownLoadSettings     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards chooseDownLoadSettings(String FieldLabel, String InputType, boolean isCheck) {
		List<WebElement> Rows = commonLibrary.isExistList(UIMAP_Document.divDLDialogRows, 10);
		boolean blnFlag = false;
		for (WebElement item : Rows) {

			WebElement Label = commonLibrary.isExist(item, By.tagName("label"), 10);
			WebElement Input = commonLibrary.isExist(item, By.tagName("input"), 10);

			switch (InputType) {
			case "CheckBox": {
				if (Label.getText().toLowerCase().contains(FieldLabel.toLowerCase())) {
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
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Notification message
	// displays.
	// # Function Name : VerifyNotificationMsg     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyNotificationMsg() {
		WebElement eltShepSearchNot = null;
		eltShepSearchNot = commonLibrary.isExistNegative(UIMAP_Shepards.eltShepSearchNot, 10);
		if (eltShepSearchNot != null)
			report.updateTestLog("Verify Notification message displays.", "Notification message is displayed with text: " + eltShepSearchNot.getText(), Status.PASS);
		else
			report.updateTestLog("Verify Notification message displays.", "Notification message is not displayed.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify shepards report
	// # Function Name : verifyShepards_Report     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepardsReport(String strTab, String strDocTitle1) {
		WebElement eltResultListWrapper = null, eltResultListHeader = null, eltShepListHeader1 = null, lnkTopLineTitle = null;
		eltResultListWrapper = commonLibrary.isExistNegative(UIMAP_Shepards.eltResultListWrapper, 10);
		eltResultListHeader = commonLibrary.isExistNegative(eltResultListWrapper, UIMAP_Shepards.eltResultListHeader, 10);
		eltShepListHeader1 = commonLibrary.isExistNegative(eltResultListHeader, UIMAP_Shepards.eltShepListHeader1, 10);

		lnkTopLineTitle = commonLibrary.isExistNegative(UIMAP_Shepards.lnkTopLineTitle, 10);
		if (eltShepListHeader1 != null && eltShepListHeader1.getText().contains(strTab) && lnkTopLineTitle != null && lnkTopLineTitle.getText().contains(strDocTitle1))
			report.updateTestLog("Verify Shepard's report for '" + strDocTitle1 + "' displays on the '" + strTab + "' category page.", "Shepard's report for 'McNeil v. EconomicsLaboratory, Inc.' displays on the '" + strTab + "' category page.", Status.PASS);
		else
			report.updateTestLog("Verify Shepard's report for '" + strDocTitle1 + "' displays on the '" + strTab + "' category page.", "Shepard's report for 'McNeil v. EconomicsLaboratory, Inc.' is not displayed on the '" + strTab + "' category page.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filters
	// # Function Name : verifyPostFilters     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyPostFilters(String strFilter) {
		WebElement eltFiltersUsed = null;
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		if (eltFiltersUsed != null && eltFiltersUsed.getText().contains(strFilter))
			report.updateTestLog("Verify 'HN16'  filter appears underthe  'You've Selected> filter category'.", "'HN16'  filter appears under the  'You've Selected> filter category'.", Status.PASS);
		else
			report.updateTestLog("Verify 'HN16'  filter appears underthe  'You've Selected> filter category'.", "'HN16'  filter does not appear under the  'You've Selected> filter category'.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter
	// # Function Name : verifyPostFilterImplemented     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyPostFilterImplemented(String strFilter) {
		int i, j = 0;
		boolean blnFlag = false;
		while (j < 2) {
			List<WebElement> eltResultCellGroup = commonLibrary.isExistList(UIMAP_Shepards.eltResultCellGroup, 10);
			for (i = 0; i < eltResultCellGroup.size(); i++) {
				blnFlag = false;
				List<WebElement> lnkHeadNotesList = commonLibrary.isExistList(eltResultCellGroup.get(i), UIMAP_Shepards.lnkHeadNotes, 10);
				for (WebElement lnkHeadNotes : lnkHeadNotesList) {
					if (lnkHeadNotes.getText().contains(strFilter))
						blnFlag = true;
				}
				if (!blnFlag) {
					break;
				}
			}
			if (!blnFlag)
				break;
			WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_Shepards.btnNextPage, 10);
			commonLibrary.clickButtonLogSmallWait(btnNextPage, "Next Page");
			try {

				commonLibrary.sleep(7000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			j++;
		}
		if (blnFlag)
			report.updateTestLog("Verify Results List contains only documents from the following filter:'Headnotes' Filter Type 'HN16' Filter.", "List contains only documents from the following filter:'Headnotes' Filter Type 'HN16' Filter..", Status.PASS);
		else
			report.updateTestLog("Verify Results List contains only documents from the following filter:'Headnotes' Filter Type 'HN16' Filter.", "List does not contain only documents from the following filter:'Headnotes' Filter Type 'HN16' Filter..", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove filter
	// # Function Name : removeFilter_HN16     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to remove HN16 filter.
	// # Function Name : removeFilter_HN16     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards removeFilterHN16() {
		WebElement eltFiltersUsed = null;
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		WebElement btnFilterRemove = commonLibrary.isExistNegative(eltFiltersUsed, UIMAP_Shepards.btnFilterRemove, 10);
		if (btnFilterRemove != null)
			commonLibrary.clickButtonLogSmallWait(btnFilterRemove, "X for HN16");
		else
			report.updateTestLog("Click X for HN16.", "The remove filter button for HN16 is not present.", Status.FAIL);
		commonLibrary.sleep(50000);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify wheter filters are removed or
	// not
	// # Function Name : verifyFiltersRemoved     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify all filter removed.
	// # Function Name : verifyFiltersRemoved     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyFiltersRemoved() {
		WebElement eltFiltersUsed = null;
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		if (eltFiltersUsed == null)
			report.updateTestLog("Verify 'You've Selected' filter type DOES NOT appear in the filter panepage.", "'You've Selected' filter type DOES NOT appear in the filter panepage.", Status.PASS);
		else
			report.updateTestLog("Verify 'You've Selected' filter type DOES NOT appear in the filter panepage.", "'You've Selected' filter type appears in the filter panepage.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter absent
	// # Function Name : verifyFilterAbsent_HN16     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyFilterAbsentHN16() {
		boolean blnFlag = false;
		int i, j = 0;
		while (j < 2) {
			List<WebElement> eltResultCellGroup = commonLibrary.isExistList(UIMAP_Shepards.eltResultCellGroup, 10);
			for (i = 0; i < eltResultCellGroup.size(); i++) {
				blnFlag = true;
				List<WebElement> lnkHeadNotesList = commonLibrary.isExistList(eltResultCellGroup.get(i), UIMAP_Shepards.lnkHeadNotes, 10);
				for (WebElement lnkHeadNotes : lnkHeadNotesList) {
					if (lnkHeadNotes != null && lnkHeadNotes.getText().contains("HN16"))
						blnFlag = false;
					break;
				}
				if (blnFlag)
					break;

			}
			if (blnFlag)
				break;
			WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_Shepards.btnNextPage, 10);
			commonLibrary.clickButtonLogSmallWait(btnNextPage, "Next Page");
			try {

				commonLibrary.sleep(3000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			j++;
		}
		if (blnFlag)
			report.updateTestLog("Verify Results list contains documents from 'Headnotes' filter type from atleast oneother filter in this filter type other than 'HN16'", "Results list contains documents from 'Headnotes' filter type from atleast oneother filter in this filter type other than 'HN16'.", Status.PASS);
		else
			report.updateTestLog("Verify Results list contains documents from 'Headnotes' filter type from atleast oneother filter in this filter type other than 'HN16'", "Results list does not contain documents from 'Headnotes' filter type from atleast oneother filter in this filter type other than 'HN16'.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on alert icon
	// # Function Name : clickAlertIcon     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click alert icon
	// # Function Name : clickAlertIcon
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards clickAlertIcon() {
		WebElement btnAlert = commonLibrary.isExist(UIMAP_Shepards.addAlertShepards);
		if (btnAlert != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnAlert, "Alert");
			else
				commonLibrary.clickJS(btnAlert, "Alert");
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab in alerts
	// # Function Name : selectTabInAlerts     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards selectTabInAlerts(String tab) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			commonLibrary.selectFromList(topicalert, tab);
		} else {
			report.updateTestLog("Select the given tab " + tab + "", "" + tab + " is not selected", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected options in alert box
	// # Function Name : verifySelectedOptionsInAlertBox     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifySelectedOptionsInAlertBox(String strDeliverType, String strDeliverFormat, String strFrequency) {
		// type
		WebElement objDivName = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		Actions hover = new Actions(driver);
		hover.moveToElement(objDivName).build().perform();
		// commonLibrary.ScrollToView(objDivName);
		if (strDeliverType.equalsIgnoreCase("online only")) {
			WebElement onlineOnly = commonLibrary.isExist(UIMAP_Shepards.online);
			if (onlineOnly.isSelected()) {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
			} else {
				report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
			}
		} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
			// WebElement onlineOnly =
			// commonLibrary.isExist(UIMAP_ShepardsPage.online);
			// commonLibrary.SetRadioButton(onlineOnly, strDeliverType);
			WebElement emailonline = commonLibrary.isExist(UIMAP_Shepards.emailonline);
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
				WebElement text = commonLibrary.isExist(UIMAP_Shepards.textRadio);
				if (text.isSelected()) {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
				} else {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
				}
			} else if (strDeliverFormat.equalsIgnoreCase("html")) {
				// commonLibrary.ScrollToView(objDivName1);
				WebElement html = commonLibrary.isExist(UIMAP_Shepards.htmlRadio);
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
				WebElement busidaily = commonLibrary.isExist(UIMAP_Shepards.businessDailyradio);
				// commonLibrary.ScrollToView(busidaily);
				if (busidaily.isSelected()) {
					report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
				} else {
					report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
				}
				break;
			}
			case "daily": {
				WebElement daily = commonLibrary.isExist(UIMAP_Shepards.dailyRadio);
				// commonLibrary.ScrollToView(daily);
				if (daily.isSelected()) {
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
		WebElement alertDialog = commonLibrary.isExist(UIMAP_Home.topicAlertTabbed, 20);
		WebElement cancel = commonLibrary.isExist(alertDialog, UIMAP_Home.cancelAlert, 20);
		if (cancel != null) {
			// cancel.click();
			commonLibrary.clickButtonParentWithWait(cancel, "Cancel");
			if (commonLibrary.isExist(UIMAP_Home.btnBrowse) != null) {
				report.updateTestLog("Click on cancel button", "cancel button  is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on cancel button", "cancel button is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on cancel button", "cancel button is not clicked", Status.FAIL);
		}

		//
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate from more button to alerts
	// # Function Name : navigationFromMoreButtonToAlerts     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to alerts
	// # Function Name : navigationFromMoreButtonToAlerts
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Alerts navigationFromMoreButtonToAlerts() {
		WebElement btnTriangleDown = commonLibrary.isExist(By.cssSelector("button[class='icon la-TriangleDownAfter']"), 20);
		if (btnTriangleDown != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnTriangleDown, "More");
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
			if (btnMore != null)
				commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");

		} else {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
			if (btnMore != null)
				commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		// if(btnMore!=null)
		// {
		// if(browsername.contains("internet"))
		// commonLibrary.click_JS(btnMore, "More");
		// else
		// commonLibrary.clickLink_withWebElement_WithWait(btnMore, "More");
		// }
		WebElement alert = commonLibrary.isExist(UIMAP_Home.Alertbtn);
		if (alert == null || !alert.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
			if (btnMore != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
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
	// # Function Description : Function to set title and create alert
	// # Function Name : giveTitleAndCreateAlert     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to give a title and creating alert
	// # Function Name : giveTitleAndCreateAlert
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards giveTitleAndCreateAlert(String alerttitle, String excelData) {
		String title = "";

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			// Actions hover = new Actions(driver);
			// hover.moveToElement(topicalert).build().perform();

			// commonLibrary.Select_FromList(topicalert, "Overview");

			WebElement alertTitle = commonLibrary.isExist(UIMAP_Shepards.alertTitle, 10);
			if (alertTitle != null) {

				// alertTitle.clear();
				// commonLibrary.SetDataInTextBox(alertTitle, alerttitle,
				// "Alert title");
				title = alertTitle.getAttribute("value");

				// alertTitle.clear();
				// alertTitle.click();
				// commonLibrary.SetDataInTextBox(alertTitle, alerttitle,
				// "Alert title");

			}
			if (excelData != "") {
				String sheetNam = excelData.split(";")[0];
				int row = Integer.parseInt((excelData.split(";")[1]));
				String column = excelData.split(";")[2];
				final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
				String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
				excel = new ExcelDataAccess(datatablePath, sheetNam);
				excel.setDatasheetName("General_Data");
				excel.setValue(row, column, title);
			}

			WebElement create = commonLibrary.isExist(UIMAP_Shepards.btnCreateAlert);
			if (create != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "Create Alert");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create Alert");
			} else {
				report.updateTestLog("Click create alert button", "create alert button is not clicked", Status.FAIL);
			}
		}
		return new Shepards(scriptHelper);

		/*
		 * WebElement title = commonLibrary.isExist(UIMAP_Shepards.title);
		 * if(title!=null) { title.clear();
		 * commonLibrary.SetDataInTextBox(title, alerttitle,"Alert title");
		 * //title.sendKeys(alerttitle); //title.sendKeys(Keys.DOWN); } else {
		 * report.updateTestLog("Enter title "+alerttitle+" in title",
		 * "Title "+alerttitle+" is not entered in title", Status.FAIL); }
		 * WebElement create =
		 * commonLibrary.isExist(UIMAP_Shepards.createAlert); if(create!=null) {
		 * if(browsername.contains("internet")) commonLibrary.click_JS(create,
		 * "Create Alert"); else
		 * commonLibrary.clickButton_Parent_WithWait(create, "Create Alert"); }
		 * else { report.updateTestLog("Click create alert button",
		 * "create alert button is not clicked", Status.FAIL); }
		 * 
		 * return new Shepards(scriptHelper);
		 */
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on node to activate the page
	// # Function Name : ClickNodeToActivateThePage     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards clickNodeToActivateThePage(String strNumber, String strNodeTitle) {

		WebElement divResult = commonLibrary.isExist(UIMAP_Shepards.divResultPage, 10);
		if (divResult != null) {
			List<WebElement> lnkNode = commonLibrary.isExistList(divResult, By.tagName("tspan"), 10);
			for (WebElement item : lnkNode) {
				if (item.getText().equalsIgnoreCase(strNumber)) {

					try {

						JavascriptExecutor executor = (JavascriptExecutor) driver;

						executor.executeScript("window.focus()");

						WebElement Parent = commonLibrary.getParentElement(item);
						if (browsername.contains("internet") && version.contains("9") || browsername.contains("chrome"))
							commonLibrary.clickJS(Parent, strNumber);
						else
							// commonLibrary.highlightElement(Parent);
							commonLibrary.clickLinkWithWebElementWithWait(Parent, strNumber);

					} catch (Exception e) {

					}

					// WebElement
					// imgClose=commonLibrary.isExist(UIMAP_ShepardsPage.imgClose,
					// 10);
					// if(imgClose!=null)
					// {
					// blnFlag = true;
					// break;
					// }
				}
			}
		}
		// if(blnFlag)
		// {
		// report.updateTestLog("Click the node "+strNumber+"",
		// "Node "+strNumber+" is clicked", Status.PASS);
		// return new Shepards(scriptHelper);
		//
		//
		// }
		// else
		// {
		// throw new FrameworkException("Click the node "+strNumber+"",
		// "Node "+strNumber+" is not clicked");
		//
		// }
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify shepards results page
	// # Function Name : verifyShepardsResultPage     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepardsResultPage(String SearchTerm) {

		WebElement hdrResult1 = commonLibrary.isExist(UIMAP_Shepards.hdrResult1, 10);
		if (hdrResult1.getText().contains(SearchTerm)) {
			report.updateTestLog("Verify Shepard's report for " + SearchTerm + " displays", "Shepard's report for " + SearchTerm + " displays", Status.PASS);
		} else {
			report.updateTestLog("Verify Shepard's report for " + SearchTerm + " displays", "Shepard's report for " + SearchTerm + " is not displayed", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Lexis Advance resarch logo
	// # Function Name : verifyLexisAdvanceResearchLogo     
	// # Author : seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards verifyLexisAdvanceResearchLogo() {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().contains("Research")) {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo displays in Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify LexisAdvance® Research logo displays in Global menu", "<LexisAdvance® Research> logo is not displayed in Global menu", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to go to previous page by clicking the
	// back button
	// # Function Name : clickBrowserBack     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
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
	// # Function Description : Function to clcik a link under foot note
	// # Function Name : clickLinkUnderFootnote     
	// # Author : Seetha
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document clickLinkUnderFootnote(String title) {
		WebElement headNotesContent = commonLibrary.isExist(UIMAP_Document.headNotesContent, 10);
		if (headNotesContent == null) {
			WebElement expandButton = commonLibrary.isExist(UIMAP_Document.expandButton, 10);
			commonLibrary.clickButtonLogSmallWait(expandButton, "Expand/Collapse");
		}
		boolean footnoter = false;
		List<WebElement> footnote = commonLibrary.isExistList(UIMAP_Document.footnotelink, 20);
		for (WebElement item : footnote)
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
			report.updateTestLog("Click " + title + "", "" + title + " is not clicked", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a specific link from ATD
	// Section
	// # Function Name : clickSpecificlinkfromATDsection     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public RelatedContent clickSpecificlinkfromATDsection(String documenttitle) {
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
		return new RelatedContent(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the star Node. (Does not work
	// in IE)
	// # Function Name : clickStarNode     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickStarNode() {
		WebElement divWowScene = commonLibrary.isExistNegative(UIMAP_Shepards.divWowScene, 10);
		WebElement btnStarNode = commonLibrary.isExistNegative(divWowScene, UIMAP_Shepards.btnStarNode, 10);

		try {
			driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MILLISECONDS);
			btnStarNode.click();// Using click ans click_JS not working.

		} catch (TimeoutException ex) {
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.MILLISECONDS);
			System.out.println(ex.toString());

		}
		report.updateTestLog("Click on button Star Node", "Clicked on button Star Node", Status.DONE);
		try {
			commonLibrary.sleep(6000);
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the alert title
	// # Function Name : enterAlertTitle     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Shepards enterAlertTitle(String title) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			// Actions hover = new Actions(driver);
			// hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromList(topicalert, "Overview");

			WebElement alertTitle = commonLibrary.isExist(UIMAP_Shepards.alertTitle, 10);
			if (alertTitle != null) {
				commonLibrary.setDataInTextBoxJavaScript(alertTitle.getAttribute("id"), title);
			}
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on create alert
	// # Function Name : clickCreateAlert     
	// # Author : Diwakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Shepards clickCreateAlert() {
		WebElement create = commonLibrary.isExist(UIMAP_Shepards.btnCreateAlert);
		if (create != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(create, "Create Alert");
			else
				commonLibrary.clickButtonParentWithWait(create, "Create Alert");
		} else {
			report.updateTestLog("Click create alert button", "create alert button is not clicked", Status.FAIL);
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to return alert title
	// # Function Name : returnAlertTitle     
	// # Author : Diwakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public String returnAlertTitle() {
		String title = "";

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			WebElement alertTitle = commonLibrary.isExist(topicalert, UIMAP_Shepards.alertTitle, 10);
			if (alertTitle != null) {
				title = alertTitle.getAttribute("value");
				report.updateTestLog("Check Alert title value", "Alert value available is '" + title + "'", Status.DONE);
			}
		}

		return title;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within the results page
	// # Function Name : searchWithinResults     
	// # Author : Karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to Search
	// # Function Name : searchWithinResults     
	// # Author : Karthi
	// # Date Created :
	// #*****************************************************************************************************************************

	public Shepards searchWithinResults(String searchTerm) {

		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_Shepards.searchWithinResults, 20);
		WebElement eltSearchbox = commonLibrary.isExist(searchwithinResults, UIMAP_Shepards.searchWithinSearchBox, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Shepards.searchWithinSearchButton, 20);

		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to function to click doc link
	// # Function Name : clickDocLink_navigation     
	// # Author : Karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkNavigation(String strDocTitle) {
		Boolean blnFlag = false;

		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().equals(strDocTitle)) {
						WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
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

		if (blnFlag) {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click back button on browser from
	// Shepards Reoprt page to go to LIT page.
	// # Function Name : clickBackShepReportToLIT
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LegalIssueTrail clickBackShepReportToLIT() {
		commonLibrary.clickBrowserBack();
		try {
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform search
	// Shepards Reoprt page to go to LIT page.
	// # Function Name : simpleSearch_Documents
	// # Author : Uma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Document simpleSearchDocuments(String strSearchTerm, Boolean strClearFilter) {

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Anbu
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Shepards swithToCondentType(String strCondentType) {
		Boolean flg = false;
		for (int i = 0; i < 3; i++) {
			if (generalFunctions.selectCondentType(strCondentType) != 1) {
				if (generalFunctions.selectCondentType(strCondentType) == 1) {
					flg = true;
					break;
				}
			} else {
				flg = true;
				break;
			}
		}
		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder order
	// # Function Name : addToFolder
	// # Author : Anbarasan
	// # Date Created : July'2015
	// #*****************************************************************************************************************************

	public Shepards addToFolder(String FolderName) {
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
				// if (browsername.contains("internet"))
				// commonLibrary.click_JS(create, "create");
				// else
				commonLibrary.clickButtonParentWithWait(create, "Create");

			create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			WebElement createNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.createFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(7000000);
					createNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.createFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (create.equals(createNew) && count < 80);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				// if (browsername.contains("internet"))
				// commonLibrary.click_JS(saveFolder, "save");
				// else
				commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(5000);

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

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To Folders
	// # Function Name : navigateToFolders
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {
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
						commonLibrary.clickMethod(btnMore, "More");
					else
						commonLibrary.clickMethod(btnMore, "More");
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
	// # Function Description : Function to click Action and Select Value
	// # Function Name : clickActionSelectValue    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public Shepards clickActionSelectValue(String strAction) {
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_Shepards.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_Shepards.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_Shepards.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_Shepards.divAction, 20);
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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Shepards saveLink(String tcName, String dataSheet, String colName) {
		generalFunctions.saveLink(tcName, dataSheet, colName);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Shepards closePopup() {
		WebElement dialogContent = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.generalDialod, 10);
		WebElement close = commonLibrary.isExistNegative(dialogContent, UIMAP_CounselBenchmarking.close, 10);
		commonLibrary.clickButtonParentWithWait(close, "Close");
		pageCheck.ajaxWait(driver);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link present in recent changes
	// section
	// # Function Name : clickLinkInRecentChanges     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Shepards clickLinkInRecentChanges(String link) {
		boolean flag = false;
		WebElement recentChanges = null;
		int count = 0;
		do {
			commonLibrary.sleep(10000);
			recentChanges = commonLibrary.isExist(UIMAP_Shepards.recentChanges, 10);
			count++;
		} while (recentChanges == null && count < 25);
		if (recentChanges != null) {
			List<WebElement> recentChangesDiv = commonLibrary.isExistList(recentChanges, By.tagName("div"), 10);
			for (WebElement item : recentChangesDiv) {
				if (item.getText().trim().toLowerCase().contains(link.toLowerCase())) {
					WebElement itemLink = commonLibrary.isExist(item, By.tagName("a"), 10);
					if (itemLink != null) {
						flag = true;
						if (browsername.contains("internet")) {
							commonLibrary.clickJS(itemLink, itemLink.getText());
							pageCheck.ajaxWait(driver);
							break;
						} else {
							commonLibrary.clickLinkWithWait(itemLink, itemLink.getText());
							pageCheck.ajaxWait(driver);
							break;
						}
					} else {
						report.updateTestLog("Click " + link, link + " is not clickable", Status.FAIL);
					}
				}
			}
		}
		if (!flag) {
			report.updateTestLog("Click " + link, link + " is not present", Status.FAIL);
		}
		return new Shepards(scriptHelper);
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
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// commonLibrary.clickMethod(btnMore, "More");
		// }
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
		// btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if ((browsername.contains("internet")))
		// commonLibrary.clickMethod(btnMore, "More");
		// else
		// commonLibrary.clickMethod(btnMore, "More");
		// }
		// lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// }
		// if (lnkSignOut != null)
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
		//
		// // driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);
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
		//
		// }
		generalFunctions.logout();
		driver.manage().deleteAllCookies();

		report.updateTestLog("Clear the browser cache", "Browser cache is cleared", Status.DONE);
		// driver.quit();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify results header
	// # Function Name : verifyResultHeader     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyResultHeader(String header) {

		WebElement resultsHeaderDiv = null;
		int count = 0;
		do {

			commonLibrary.sleep(20000);
			resultsHeaderDiv = commonLibrary.isExist(UIMAP_Shepards.resultsHeaderDiv, 10);
			count++;
		} while (resultsHeaderDiv == null && count < 25);
		WebElement resultsHeader = commonLibrary.isExist(resultsHeaderDiv, UIMAP_Shepards.resultsHeader, 10);
		if (resultsHeader != null) {

			if (resultsHeader.getText().toLowerCase().contains(header.toLowerCase()))
				report.updateTestLog("Verify " + header + " is displayed", header + " is displayed", Status.PASS);
			else
				report.updateTestLog("Verify " + header + " is displayed", header + " is not displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify " + header + " is displayed", header + " is not displayed", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify results header
	// # Function Name : verifyEmail     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Shepards verifyEmail(String userName, String password, String hostName) {
		// generalFunctions.verifyEmail(userName, password, hostName);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click delivery ooptions
	// # Function Name : clickDeliverySelectOption     
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Shepards clickDeliverySelectOption(String option, String DeliverOption) {
		Boolean blnflag = false;
		WebElement asideclass = null;
		WebElement menu = commonLibrary.isExist(UIMAP_Shepards.menu1, 20);
		List<WebElement> submenu = commonLibrary.isExistList(menu, UIMAP_Shepards.lstTagName, 10);
		for (WebElement list : submenu) {
			if (list.getAttribute("class").contains("splitbutton")) {
				List<WebElement> btn1 = commonLibrary.isExistList(list, By.tagName("button"), 20);
				if (option.contains("print")) {
					commonLibrary.clickButtonParentWithWait(btn1.get(0), option);
					asideclass = commonLibrary.isExist(list, UIMAP_Shepards.tagNameAside, 10);
					break;
				} else if (option.contains("delivery")) {
					commonLibrary.clickButtonParentWithWait(btn1.get(1), option);
					blnflag = true;
					asideclass = commonLibrary.isExist(list, UIMAP_Shepards.tagNameAside, 10);
					break;
				}

			}

		}
		if (blnflag) {
			if (asideclass != null) {
				List<WebElement> listItems = commonLibrary.isExistList(asideclass, By.tagName("button"), 20);
				for (WebElement List : listItems) {
					// WebElement
					// button=commonLibrary.isExist(List,UIMAP_Shepards.button1,10);

					if (List.getAttribute("data-action").contains(DeliverOption)) {
						commonLibrary.clickButtonParentWithWait(List, DeliverOption);
						break;
					}

				}
			} else {
				report.updateTestLog("Click on " + DeliverOption + " Option ", "' " + DeliverOption + " ' option is not clicked", Status.FAIL);
			}

		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyShepardsReportForDoc
	// # Function Name : verifyShepardsReportForDoc     
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepardsReportForDoc(String docTitle) {
		WebElement hdr = commonLibrary.isExist(UIMAP_Shepards.hdrDoc, 20);
		if (hdr != null) {
			WebElement anchor = commonLibrary.isExist(hdr, UIMAP_Document.links, 20);
			if (anchor != null && anchor.getText().toLowerCase().contains(docTitle.toLowerCase()))
				report.updateTestLog("Verify shepards report for the " + docTitle + " displays on Appellate History", "Shepards report for the " + docTitle + " displays on Appellate History", Status.PASS);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify contents of Notification
	// message
	// displays.
	// # Function Name : verifyNotificationMsgContents     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyNotificationMsgContents(String text) {
		WebElement eltShepSearchNot = null;
		eltShepSearchNot = commonLibrary.isExistNegative(UIMAP_Shepards.eltShepSearchNot, 10);
		if (eltShepSearchNot != null && eltShepSearchNot.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify Notification message containing: " + text + " displays.", "Notification message containing: " + text + " displays.", Status.PASS);
		else
			report.updateTestLog("Verify Notification message containing: " + text + " displays.", "Notification message containing: " + text + " does not display.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify contents of Suggestion
	// message
	// displays.
	// # Function Name : verifySuggestionMsgContents     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifySuggestionMsgContents(String text) {
		WebElement suggestionContent = commonLibrary.isExistNegative(UIMAP_Shepards.suggestionContent, 10);
		if (suggestionContent != null && suggestionContent.getText().contains("Suggestions:")) {
			report.updateTestLog("Verify Suggestions displays.", "Suggestions is displayed.", Status.PASS);
			String[] textList = text.split(";");
			for (String line : textList) {
				if (suggestionContent.getText().replaceAll("[^a-zA-Z0-9]", "").toLowerCase().contains(line.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
					report.updateTestLog("Verify " + line + " displays in Suggestions.", line + " displays in Suggestions.", Status.PASS);
				} else
					report.updateTestLog("Verify " + line + " displays in Suggestions.", line + " does not display in Suggestions.", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify Suggestions displays.", "Suggestions is not displayed.", Status.FAIL);
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Shepardize this document.
	// # Function Name : clickShepThisDoc     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickShepThisDoc() {
		WebElement suggestionContent = commonLibrary.isExistNegative(UIMAP_Shepards.suggestionContent, 10);
		WebElement shepThisDoc = commonLibrary.isExistNegative(suggestionContent, UIMAP_Shepards.shepThisDoc, 10);
		if (shepThisDoc != null)
			commonLibrary.clickButtonParentWithWait(shepThisDoc, "Shepardize this document");
		else
			report.updateTestLog("Click Shepardize This Document.", "Shepardize This Document link is not displayed.", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBackShepards     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Shepards clickBrowserBackShepards() {

		commonLibrary.clickBrowserBack();
		try {
			commonLibrary.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click shep doc link
	// # Function Name : clickShepDocLink     
	// # Author : Seetha
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Document clickShepDocLink() {

		WebElement shepDoclink = commonLibrary.isExistNegative(UIMAP_Shepards.shepDocLink, 10);

		if (shepDoclink != null)
			commonLibrary.clickButtonParentWithWait(shepDoclink, "Full text document link");
		else
			report.updateTestLog("Click the link to the full text document in the Full Citation sentence above the Shepard's report", "Link for selecting full text document is not present", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify hover text in document list
	// # Function Name : verifyHoverTextInDocList     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Shepards verifyHoverTextInDocList(String docTitle, String article, String hoverText) {
		Boolean flag = false;
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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(docTitle.toLowerCase())) {
							if (!article.contains("")) {
								WebElement articleTag = commonLibrary.isExist(document, UIMAP_Shepards.articleTag, 10);
								if (articleTag != null && articleTag.getText().toLowerCase().contains(article.toLowerCase())) {
									WebElement image = commonLibrary.isExist(eleDocTitle, UIMAP_Shepards.imgTag, 10);
									if (image != null && image.getAttribute("title").toLowerCase().contains(hoverText.toLowerCase())) {
										report.updateTestLog("Verify " + hoverText + " displays while hovering over the image near " + docTitle, hoverText + " is displayed while hovering over the image near " + docTitle, Status.PASS);
										flag = true;
										break;
									}
								}
							} else {
								WebElement image = commonLibrary.isExist(eleDocTitle, UIMAP_Shepards.imgTag, 10);
								if (image != null && image.getAttribute("title").toLowerCase().contains(hoverText.toLowerCase())) {
									report.updateTestLog("Verify " + hoverText + " displays while hovering over the image near " + docTitle, hoverText + " is displayed while hovering over the image near " + docTitle, Status.PASS);
									flag = true;
									break;
								}
							}

						}

					}

				}
			}
			if (!flag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				else
					break;
			} else
				break;
		}

		if (!flag)
			report.updateTestLog("Verify " + hoverText + " displays while hovering over the image near " + docTitle, hoverText + " is not displayed while hovering over the image near " + docTitle, Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Image in Document list
	// # Function Name : clickImageInDocList     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Shepards clickImageInDocList(String docTitle, String article) {
		Boolean flag = false;
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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(docTitle.toLowerCase())) {
							if (!article.contains("")) {
								WebElement articleTag = commonLibrary.isExist(document, UIMAP_Shepards.articleTag, 10);
								if (articleTag != null && articleTag.getText().toLowerCase().contains(article.toLowerCase())) {
									WebElement spanTag = commonLibrary.isExist(eleDocTitle, UIMAP_Shepards.tagSpan, 10);
									WebElement link = commonLibrary.isExist(spanTag, UIMAP_Shepards.link, 10);
									if (link != null) {
										commonLibrary.clickJSNoLog(link);
										// commonLibrary.clickLinkWithWebElementWithWait(link,
										// "Image");
										report.updateTestLog("Click the Image next to " + docTitle, "Clicked on the Image next to " + docTitle, Status.PASS);
										flag = true;
										break;
									}
								}
							} else {
								WebElement spanTag = commonLibrary.isExist(eleDocTitle, UIMAP_Shepards.tagSpan, 10);
								WebElement link = commonLibrary.isExist(spanTag, UIMAP_Shepards.link, 10);
								if (link != null) {
									commonLibrary.clickJSNoLog(link);
									// commonLibrary.clickLinkWithWebElementWithWait(link,
									// "Image");
									report.updateTestLog("Click the Image next to " + docTitle, "Clicked on the Image next to " + docTitle, Status.PASS);
									flag = true;
									break;
								}
							}

						}

					}

				}
			}
			if (!flag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (btnNextPage != null)
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
				else
					break;
			} else
				break;
		}

		if (!flag)
			report.updateTestLog("Click on the link near " + docTitle, "Not Clicked  on the link near " + docTitle, Status.FAIL);
		else {
			WebElement newResultClass = null;
			int counter = 0;
			do {
				commonLibrary.sleep(10000);
				newResultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
				counter = counter + 1;
			} while (newResultClass == null && counter <= 40);

			check = pageCheck.positiveCheck(driver, "document", "Document");

			pageCheck.handleFlow(driver, check);

		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify applied filters in narrow by
	// filter
	// # Function Name : verifyFiltersNarrowby     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyFiltersNarrowby(String filter) {

		WebElement eltFiltersUsed = null;
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		if (eltFiltersUsed != null) {
			if (eltFiltersUsed != null && eltFiltersUsed.getText().toLowerCase().contains(filter.toLowerCase()))
				report.updateTestLog("Verify " + filter + "  filter appears under the  narrow by section.", filter + "appears under the  narrow by section", Status.PASS);
			else
				report.updateTestLog("Verify " + filter + "  filter appears under the  narrow by section.", filter + "does not appears under the  narrow by section", Status.FAIL);
		} else
			report.updateTestLog("Verify " + filter + "  filter appears under the  narrow by section.", filter + "does not appears under the  narrow by section", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify full document page
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyFullDocumentReturnShepards(String searchTerm) {

		generalFunctions.verifyFullDocument(searchTerm);

		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Document in shepards page
	// without ATD wait
	// # Function Name : clickDocLinkWithoutAtdWait     
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Shepards clickDocLinkWithoutAtdWait(String docTitle) {
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
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(docTitle.toLowerCase())) {
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

			report.updateTestLog("Click on the document " + docTitle, "Not Clicked  on the document " + docTitle, Status.FAIL);
		else {
			WebElement newResultClass = null;
			int counter = 0;
			do {
				commonLibrary.sleep(10000);
				newResultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
				counter = counter + 1;
			} while (newResultClass == null && counter <= 40);

			check = pageCheck.positiveCheck(driver, "document", "Document");

			pageCheck.handleFlow(driver, check);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to find page is scrolled to subsequent
	// document
	// # Function Name : verifyScrolledToSubsequentDocument     
	// # Author : Seetha
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Shepards verifyScrolledToSubsequentDocument() {

		int ySection = 0;
		WebElement toolbar = commonLibrary.isExistNegative(UIMAP_Document.toolbar, 10);
		int yToolbar = toolbar.getLocation().getY();

		List<WebElement> subsequent = commonLibrary.isExistList(UIMAP_Shepards.subsequentDoc, 10);
		for (WebElement item : subsequent) {
			if (item.getText().equalsIgnoreCase("subsequent")) {
				ySection = item.getLocation().getY();
				break;
			}

		}
		if ((ySection - yToolbar) >= 0 && (ySection - yToolbar) <= 1000)
			report.updateTestLog("Verify results list displays at the following location:<Subsequent>", "Results list displays at the following location:<Subsequent>", Status.PASS);

		else
			report.updateTestLog("Verify results list displays at the following location:<Subsequent>", "Results list is not displayed at the following location:<Subsequent>", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyStarNodeOnlyAppearsInMap
	// # Function Name : verifyStarNodeOnlyAppearsInMap     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public Shepards verifyStarNodeOnlyAppearsInMap() {
		Boolean blnFlag = false;
		// verification point : to verify the star node in the map

		WebElement divWowScene = commonLibrary.isExistNegative(UIMAP_Shepards.divWowScene, 10);
		WebElement svg = commonLibrary.isExistNegative(divWowScene, UIMAP_Shepards.svgsection1, 10);
		List<WebElement> circle = commonLibrary.isExistList(svg, UIMAP_Shepards.btnStarNode, 10);

		// checking for the existence of star node
		if (circle != null && circle.size() < 2) {
			blnFlag = true;
		}

		if (blnFlag)

			report.updateTestLog("Verify Star Node only appears ", "Only star node  appeared in the Map", Status.PASS);
		else

			report.updateTestLog("Verify Star Node only appears ", "Only star node  appeared in the Map", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying star Icon for Shepardized
	// document
	// history page
	// # Function Name : verifyShepardizeStarIcon     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public Shepards verifyShepardizeStarIcon(String number) {
		Boolean blnFlag = false;

		// verifying the citation shepardize for the document listed at 5th
		// position

		List<WebElement> lstSelected1 = commonLibrary.isExistList(UIMAP_Shepards.eltSelectedItem, 20);
		for (WebElement list : lstSelected1) {
			// looping for the list of items

			WebElement tagSheprdized = commonLibrary.isExist(list, UIMAP_Shepards.shepardized, 10);
			WebElement docnumber = commonLibrary.isExist(list, UIMAP_Shepards.docnumber, 10);
			WebElement starIcon = commonLibrary.isExist(list, UIMAP_Shepards.starIcon, 10);

			// checking the existence of document number and citation

			if (starIcon != null && tagSheprdized != null && docnumber != null && docnumber.getText().contains(number)) {
				blnFlag = true;
				break;
			}
		}

		if (blnFlag)

			report.updateTestLog("Verify the Citation for Document number " + number + "", "The Citation You Shepardized(TM) displayed above citation number " + number + " in the appellate history chain.A Star icon appeared next to the Citation You Shepardized.", Status.PASS);
		else

			report.updateTestLog("Verify the Citation for Document number " + number + "", "The Citation You Shepardized(TM) is not displayed above citation number " + number + " in the appellate history chain.A Star iconnot appeared next to the Citation You Shepardized.", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifyStarNodeAppearsOnlyOnce
	// history page
	// # Function Name : verifyStarNodeAppearsOnlyOnce     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public Shepards verifyStarNodeAppearsOnlyOnce() {

		Boolean blnFlag = false;
		// verify whether star node is present or not

		WebElement divWowScene = commonLibrary.isExistNegative(UIMAP_Shepards.divWowScene, 10);
		WebElement svg = commonLibrary.isExistNegative(divWowScene, UIMAP_Shepards.svgsection, 10);
		List<WebElement> circle = commonLibrary.isExistList(svg, UIMAP_Shepards.btnStarNode, 10);

		// checking for the existence of star node
		if (circle != null && circle.size() < 2) {
			blnFlag = true;
		}
		if (blnFlag)

			report.updateTestLog("Verify Star Node appears only once", "Star Node appeared only once in the Map", Status.PASS);
		else

			report.updateTestLog("Verify Star Node appears only once", "Star Node is not appeared only once in the Map", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : simpleSearchShep     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Shepards simpleSearchShep(String strSearchTerm, Boolean strClearFilter) {

		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verifyPopupText
	// on it using coordinate
	// # Function Name : verifyPopupText
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public Shepards verifyPopupText() {
		Boolean blnFlag = false;
		Boolean blnFlag1 = false;

		// Verify Bubble text
		WebElement divIdBubble = commonLibrary.isExist(UIMAP_Shepards.divIdBubble1, 10);
		WebElement txtSheprdize = commonLibrary.isExist(divIdBubble, By.tagName("tspan"), 10);

		// verifying the text by sections

		if (txtSheprdize.getText().contains("Shepardized ® case")) {
			blnFlag = true;
		}
		WebElement divIdBubble1 = commonLibrary.isExist(UIMAP_Shepards.divIdBubble, 10);

		if (divIdBubble1.getText().contains("Reversed by") && divIdBubble1.getText().contains("Affirmed by") && divIdBubble1.getText().contains("Companion case at") && divIdBubble1.getText().contains("Brown v. Bd. of Educ.")) {
			blnFlag1 = true;
		}
		if (blnFlag1 && blnFlag) {
			report.updateTestLog("Verify Text in the Bubble", "The following are displayed in the Bubble text 1.Shepardized ® case, 2.Reversed by, 3.Affirmed by, 4.Companion case at, 5.Brown v. Bd. of Educ., 349 U.S. 294(1955) U.S.", Status.PASS);
		} else {
			report.updateTestLog("Verify Text in the Bubble", "The following are not  displayed in the Bubble text 1.Shepardized ® case, 2.Reversed by, 3.Affirmed by, 4.Companion case at, 5.Brown v. Bd. of Educ., 349 U.S. 294(1955) U.S.", Status.FAIL);
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for
	// verifyDocumentDisplayedAtRequiredLocation
	// history page
	// # Function Name : verifyDocumentDisplayedAtRequiredLocation     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public Shepards verifyDocumentDisplayedAtRequiredLocation(String docTitle) {
		Boolean blnFlag = false;
		// Verification point : verifying the document displayes in the applet
		// history page

		List<WebElement> lstSelected1 = commonLibrary.isExistList(UIMAP_Shepards.eltSelectedItem, 20);
		for (WebElement list : lstSelected1) {
			WebElement tagSheprdized = commonLibrary.isExist(list, UIMAP_Shepards.shepardized, 10);
			WebElement docnumber = commonLibrary.isExist(list, UIMAP_Shepards.docnumber, 10);
			WebElement starIcon = commonLibrary.isExist(list, UIMAP_Shepards.starIcon, 10);
			if (starIcon != null && tagSheprdized != null && docnumber != null && list.getText().contains(docTitle)) {
				blnFlag = true;
				break;
			}
		}

		if (blnFlag)

			report.updateTestLog("Verify the results list", "The result list displayed at following location '" + docTitle, Status.PASS);
		else

			report.updateTestLog("Verify the results list", "The result list is not displayed at following location '" + docTitle, Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the star Node.
	// # Function Name : clickStarNode1     
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards clickStarNode1() {

		// to click on the star node appeared in the map view
		WebElement divWowScene = commonLibrary.isExistNegative(UIMAP_Shepards.divWowScene, 10);
		WebElement svg = commonLibrary.isExistNegative(divWowScene, UIMAP_Shepards.svgsection, 10);
		WebElement circle = commonLibrary.isExistNegative(svg, UIMAP_Shepards.btnStarNode, 10);

		// checking for the existence of star node
		try {
			driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MILLISECONDS);
			circle.click();// Using click ans click_JS not working.

		} catch (TimeoutException ex) {
			driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.MILLISECONDS);
			System.out.println(ex.toString());

		}
		report.updateTestLog("Click on button Star Node", "Clicked on button Star Node", Status.DONE);
		try {
			commonLibrary.sleep(6000);
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify KWIC window and click the
	// same
	// # Function Name : verifyKWICWindowAndClick     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Document verifyKWICWindowAndClick(String docName, String verifyText) {
		// verifying KWIC window for the document
		WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		commonLibrary.isExist(resultClass, UIMAP_Shepards.teaserLink, 5);
		WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
		if (OListResult != null) {
			List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);

			for (WebElement document : OListItems) {

				// looping for the document consisting of the KWIC window and
				// the Text needs to be verify
				WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
				if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim().equals(docName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim())) {

					WebElement docContent = commonLibrary.isExist(document, UIMAP_Shepards.teaserLink, 20);
					if (docContent != null) {

						report.updateTestLog("Verify KWIC window displayes  for  document: '" + docName + "'", "KWIC window is displayed for docuement: '" + docName + "'", Status.PASS);
						WebElement span = commonLibrary.isExist(document, UIMAP_Shepards.teaser1, 10);
						WebElement spanhit = commonLibrary.isExist(span, UIMAP_Shepards.teaser2, 10);
						if (span != null && spanhit != null) {

							if (spanhit.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(verifyText.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
								report.updateTestLog("Verify Text in KWIC window for document : '" + docName + "'", "Text '" + verifyText + "' is displayed and Bolded in KWIC Window for document : '" + docName + "'", Status.PASS);
							} else {
								report.updateTestLog("Verify Text in KWIC window for document : '" + docName + "'", "Text '" + verifyText + "' is not displayed and Bolded in KWIC Window for document : '" + docName + "'", Status.FAIL);
							}
						}

						// clicking on the KWIC Window
						commonLibrary.clickLinkWithWebElementWithWait(docContent, "KWIC Window");
						WebElement txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						// verifying whether the document page is displayed or
						// not
						int count = 0;
						do {
							count++;
							commonLibrary.sleep(50000);
							txtDocumentHeading = commonLibrary.isExistNegative(UIMAP_Document.txtDocumentHeading, 10);
						} while (txtDocumentHeading == null && count < 20);

						break;
					} else
						report.updateTestLog("Verify KWIC window displayes  for  document " + docName, "KWIC window is not displayed for docuement" + docName, Status.FAIL);
				}

			}
		}

		return new Document(scriptHelper);
	}

	// # Function Description : Function to clickSignalsNextToDoc
	// # Function Name : clickSignalsNextToDoc     
	// # Author : Dinesh
	// # Date Created : Oct'15
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
	// # Function Description : Function to verify sort by drop appearance
	// # Function Name : verifySortByAppearance
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifySortByAppearance() {

		try {

			WebElement sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
			if (sortContainer != null)

				report.updateTestLog("Verifying  the appearance of sort by", " sort by is displayed", Status.FAIL);
			else
				report.updateTestLog("Verifying  the appearance of sort by", " sort by not displayed", Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify shep signal appearance
	// # Function Name : verifyShepSignalAppearance
	// # Author : Vennila
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepSignalAppearance(String docTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().contains(docTitle.toLowerCase())) {
						WebElement signal = commonLibrary.isExist(UIMAP_Shepards.shepSignal, 10);
						if (signal != null)

							blnFlag = true;
						break;

					}
					if (!blnFlag)
						break;
				}
			}

		}

		if (blnFlag)

			report.updateTestLog("Verifying  the appearance of signal", " signal is displayed", Status.PASS);
		else
			report.updateTestLog("Verifying  the appearance of signal", " signal not displayed", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document count displayed in
	// ordinal numbers
	// # Function Name : verifyDocCount
	// # Author : Vennila
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyDocCount() {

		WebElement resultClass = null;
		String text;
		int count = 0;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 200);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 200);

				int z = 1;
				for (WebElement document : OListItems) {
					WebElement docnumber = commonLibrary.isExist(document, UIMAP_Shepards.docNo, 20);

					text = docnumber.getText().replace(".", "");

					int chk = Integer.parseInt(text);

					if (z == chk) {
						count++;
						z++;
					}

				}
				if (OListItems.size() == count)

					report.updateTestLog("Verifying  the document titles contains ordinal numbers", "Till " + count + " numbers are displayed", Status.PASS);
				else
					report.updateTestLog("Verifying  the document titles contains ordinal numbers", "Till " + count + " numbers are not displayed", Status.FAIL);
			}

		}

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the document title without
	// comma
	// # Function Name : verifyDocumentTitlewithoutcomma    
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyDocumentTitlewithoutcomma() {

		WebElement resultClass = null;
		Boolean flag = true;
		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					if (eleDocTitle != null) {
						// if
						// (eleDocTitle.getText().toLowerCase().contains("document"))
						// {
						String s = eleDocTitle.getText();
						flag = s.startsWith(",");

						// }
					}
				}
			}

			if (!flag)
				report.updateTestLog("Verify none of the document title begins with ,", "none of the document titles begins with ,", Status.PASS);
			else
				report.updateTestLog("Verify none of the document title begins with ,", "document titles begins with ,", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : verifyFilterHeaderPresent
	// # Function Name : verifyFilterHeaderPresence
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Shepards verifyFilterHeaderPresence(String strHeader, String filterPresence) {

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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Filter Header Present
	// # Function Name : verifyFilterHeaderPresent
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Shepards verifyFilternotHeaderPresent(String strHeader) {
		Boolean bnlFlag = true;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

		for (int i = 0; i < filterHeader.size(); i++) {

			if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

				bnlFlag = true;
				break;
			}

		}
		if (!bnlFlag)
			report.updateTestLog("Verify Filter '" + strHeader + "' not present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.PASS);
		else
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is  displayed in the left Pane", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify chicklet indicator color and
	// text
	// # Function Name : verifyChickletindicator
	// # Author : Vennila
	// # Date Created : 5 Nov'15
	// #*****************************************************************************************************************************
	public Shepards verifyChickletindicator(String strDocTitle, String color, String chickletText) {
		WebElement resultClass = null;
		Boolean flag = true;
		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					WebElement treatPhrase = commonLibrary.isExist(document, UIMAP_Shepards.treatPhrase, 2);
					WebElement chickletColor = commonLibrary.isExist(treatPhrase, UIMAP_Shepards.chickletImg, 2);

					if (eleDocTitle != null && treatPhrase != null) {
						if (eleDocTitle.getText().contains(strDocTitle) && treatPhrase.getText().contains(chickletText) && chickletColor.getAttribute("title").contains(color)) {
							flag = true;
							break;
						}
					}
				}
			}

			if (flag)
				report.updateTestLog("Verify chicklet text and chicklet color in document title ',", "Document Title" + strDocTitle + "'Contains" + chickletText + "which is in " + color + "color", Status.PASS);
			else
				report.updateTestLog("Verify chicklet text and chicklet color in document title ',", "Document Title" + strDocTitle + "not Contains" + chickletText + "which is not in " + color + "color", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify content type is displayed or
	// not
	// Box.
	// # Function Name : verifyContentTypeDisplayed_new
	// # Author : Vennila
	// # Date Created : 1 Oct'15
	// #*****************************************************************************************************************************

	public Shepards verifyContentTypeDisplayed_new(String contentTypes, Boolean value) {
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
		// if (OListItems.size() == namesList.length) {
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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the active content type of
	// the
	// results.
	// # Function Name : verifyActiveContentType     
	// # Author : Vennila
	// # Date Created : 5 Oct 15
	// #*****************************************************************************************************************************

	public Shepards verifyActiveContentType(String strContentType) {
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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove HN1 filter.
	// # Function Name : removeFilter_HN1     
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards removeFilterHN1() {
		WebElement eltFiltersUsed = null;
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		eltFiltersUsed = commonLibrary.isExistNegative(UIMAP_Shepards.eltFiltersUsed, 10);
		WebElement btnFilterRemove = commonLibrary.isExistNegative(eltFiltersUsed, UIMAP_Shepards.btnFilterRemove, 10);
		if (btnFilterRemove != null)
			commonLibrary.clickButtonLogSmallWait(btnFilterRemove, "X for HN1");
		else
			report.updateTestLog("Click X for HN1.", "The remove filter button for HN1 is not present.", Status.FAIL);
		commonLibrary.sleep(50000);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Search within results
	// # Function Name : searchWithinResults     
	// # Author : Sriram
	// # Date Created : Nov 2015
	// #*****************************************************************************************************************************

	public Shepards searchWithin(String searchTerm) {

		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_Shepards.searchWithinResults, 20);
		WebElement eltSearchbox = commonLibrary.isExist(searchwithinResults, UIMAP_Shepards.searchWithinSearch, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Shepards.searchWithinSearchButton, 20);

		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying List view Disabled
	// # Function Name : VerifyListViewDisabled
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards verifyListViewDisabled() {
		WebElement btnReset = commonLibrary.isExist(UIMAP_Shepards.btnIdListView, 40);
		if (btnReset != null) {

			if (btnReset.getAttribute("disabled") != null)

				report.updateTestLog("Verify list view is grey out", "list view is grey out", Status.PASS);

			else {
				report.updateTestLog("Verify list view is grey out", "list view is not grey out", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify list view is grey out", "list view is not grey out", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Grid view Disabled
	// # Function Name : VerifyGridViewDisabled
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards verifyGridViewDisabled() {
		WebElement gridParent = commonLibrary.isExist(UIMAP_Shepards.gridParent, 30);
		WebElement btnReset = commonLibrary.isExist(gridParent, UIMAP_Shepards.btnIdGridView, 30);
		if (btnReset != null) {

			if (btnReset.getAttribute("disabled") != null)

				report.updateTestLog("Verify Grid view is grey out", "Grid view is grey out", Status.PASS);

			else {
				report.updateTestLog("Verify Grid view is grey out", "Grid view is not grey out", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Grid view is grey out", "Grid view is not grey out", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Information Phrase
	// displays.
	// # Function Name : VerifyInformation Phrase  
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards verifyInformationPhrase(String text) {
		WebElement eltinfoPhrase = null;
		eltinfoPhrase = commonLibrary.isExistNegative(UIMAP_Shepards.informationPhrase, 10);
		if (eltinfoPhrase != null && eltinfoPhrase.getText().toLowerCase().contains(text.toLowerCase()))
			report.updateTestLog("Verify Information phrase displays in Shepard's Index page", "Information phrase displayed in Shepard's Index page with text: " + eltinfoPhrase.getText(), Status.PASS);
		else
			report.updateTestLog("Verify Information phrase displays in Shepard's Index page", "Information phrase is not displayed in Shepard's Index page ", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify document count displayed in
	// ordinal numbers
	// # Function Name : verifyDocCount
	// # Author : Vennila
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyDocCount1() {

		WebElement resultClass = null;
		String text;
		int count = 0;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult3, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult3, 10);
		WebElement rsltWrapper = commonLibrary.isExist(resultClass, UIMAP_SearchResult.resultwrapper, 200);

		if (rsltWrapper != null) {
			WebElement OListResult = commonLibrary.isExist(rsltWrapper, By.tagName("ol"), 200);

			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 200);

				int z = 1;
				for (WebElement document : OListItems) {
					WebElement docnumber = commonLibrary.isExist(document, UIMAP_Shepards.docNo, 20);
					text = docnumber.getText().replace(".", "");

					int chk = Integer.parseInt(text);

					if (z == chk) {
						count++;
						z++;
					}

				}
				if (OListItems.size() == count)

					report.updateTestLog("Verifying  the document titles contains ordinal numbers", "Till " + count + " numbers are displayed", Status.PASS);
				else
					report.updateTestLog("Verifying  the document titles contains ordinal numbers", "Till " + count + " numbers are not displayed", Status.FAIL);
			}

		}

		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify chicklet indicator color and
	// text
	// # Function Name : verifyChickletindicatorPresence
	// # Author : Vennila
	// # Date Created : 5 Nov'15
	// #*****************************************************************************************************************************
	public Shepards verifyChickletindicatorPresence(String strDocTitle, String color, String chickletText) {
		WebElement resultClass = null;
		Boolean flag = true;
		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					WebElement treatPhrase = commonLibrary.isExist(document, UIMAP_Shepards.treatPhrase, 2);
					WebElement chickletColor = commonLibrary.isExist(treatPhrase, UIMAP_Shepards.chickletImg, 2);

					if (eleDocTitle != null && treatPhrase != null && chickletColor == null) {
						if (eleDocTitle.getText().contains(strDocTitle) && treatPhrase.getText().contains(chickletText)) {
							flag = true;
							break;
						}
					}
				}
			}

			if (flag)
				report.updateTestLog("Verify chicklet text and chicklet color in document title ',", "Document Title" + strDocTitle + "'Contains" + chickletText + "which is in " + color + "color", Status.PASS);
			else
				report.updateTestLog("Verify chicklet text and chicklet color in document title ',", "Document Title" + strDocTitle + "not Contains" + chickletText + "which is not in " + color + "color", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify chicklet indicator color and
	// text
	// # Function Name : verifyChickletindicator
	// # Author : Vennila
	// # Date Created : 5 Nov'15
	// #*****************************************************************************************************************************
	public Shepards verifychickletHoverText(String strDocTitle, String hoverText) {
		WebElement resultClass = null;
		Boolean flag = true;
		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					WebElement treatPhrase = commonLibrary.isExist(document, UIMAP_Shepards.treatPhrase, 2);
					WebElement chickletColor = commonLibrary.isExist(treatPhrase, UIMAP_Shepards.chickletImg, 2);

					if (eleDocTitle != null && treatPhrase != null) {
						if (eleDocTitle.getText().contains(strDocTitle) && chickletColor.getAttribute("title").contains(hoverText)) {
							flag = true;
							break;
						}
					}
				}
			}

			if (flag)
				report.updateTestLog("Verify hover text ',", "Document Title" + strDocTitle + "'Contains" + hoverText, Status.PASS);
			else
				report.updateTestLog("Verify hover text ',", "Document Title" + strDocTitle + "'does not Contains" + hoverText, Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify shepard signal with text
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyShepardSignal(String strText, String strsignal) {

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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document link without page
	// header verification statement
	// # Function Name : clickDocLinkWitoutHeaderVerification     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Shepards clickDocLinkWitoutHeaderVerification(String strDocTitle) {
		generalFunctions.clickDocLinkWitoutHeaderVerification(strDocTitle);
		return new Shepards(scriptHelper);

	}

	public Shepards clickNextPage() {
		WebElement btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
		if (btnNextPage != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnNextPage, "NextPage");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
		return new Shepards(scriptHelper);
	}

	public Shepards selectPostFilterTimeline(String startYear, String endYear) {

		if (!(startYear.equals(" ") && endYear.equals(" "))) {

			int i = 0;
			boolean flag = false;
			commonLibrary.sleep(1000);
			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_Shepards.filterContainer, 10);
			if (filterContainer != null) {
				List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_Shepards.filterHeader, 10);
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

								WebElement startYearTextbox = commonLibrary.isExist(UIMAP_Shepards.minTest, 20);
								WebElement endYearTextbox = commonLibrary.isExist(UIMAP_Shepards.maxTest, 20);

								if (startYearTextbox != null && endYearTextbox != null) {

									commonLibrary.setDataInTextBox(startYearTextbox, startYear, "StartYear");
									commonLibrary.setDataInTextBox(endYearTextbox, endYear, "EndYear");
									WebElement timelinkOKbutton = commonLibrary.isExist(UIMAP_Shepards.btnTimeline, 20);
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
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify shep signal appearance
	// # Function Name : verifyShepSignalAppearance
	// # Author : Vennila
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepNoSignalAppearance(String docTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().contains(docTitle.toLowerCase())) {
						WebElement signal = commonLibrary.isExist(UIMAP_Shepards.shepSignal, 10);
						if (signal != null)

							blnFlag = true;
						break;

					}
					if (!blnFlag)
						break;
				}
			}

		}

		if (blnFlag)

			report.updateTestLog("Verifying  the appearance of signal", " signal is displayed", Status.FAIL);
		else
			report.updateTestLog("Verifying  the appearance of signal", " signal not displayed", Status.PASS);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Add to Folder icon is not
	// displayed
	// # Function Name : verifyFolderIconNotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Shepards verifyFolderIconNotDisplayed() {

		WebElement deliveryTray = commonLibrary.isExistNegative(UIMAP_SearchResult.selectAllItemsUl, 10);
		WebElement addTofolder = commonLibrary.isExistNegative(deliveryTray, UIMAP_SearchResult.addTofolder, 10);
		if (addTofolder == null)
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is not displayed on the delivery tray ", Status.PASS);
		else
			report.updateTestLog("Verify Add to Folder Icon is not displayed on the delivery tray ", "Add to Folder Icon is displayed on the delivery tray ", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to reserach map page
	// # Function Name : navigateToResearchMap
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToResearchMap() {
		generalFunctions.navigateToHistoryFooterLink("Research Map");
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on pop up phrase
	// # Function Name : clickandVerifyPopupPhrase1
	// # Author : Deepha Hariramasamy
	// # Date Created : Nov '15
	// #*****************************************************************************************************************************
	public Shepards clickandVerifyPopupPhrase1(String link, boolean flag) {

		WebElement distin = commonLibrary.isExist(UIMAP_Shepards.distin, 20);
		WebElement closeButton = commonLibrary.isExist(UIMAP_Shepards.closeButton, 20);

		if (distin != null) {
			commonLibrary.clickLink(distin, link);
			commonLibrary.isExist(UIMAP_Shepards.shepPopUp, 20);
			report.updateTestLog("Verify the shep pop up phrase is displayed", "The shep pop up phrase is displayed", Status.PASS);

			if (flag != false) {
				WebElement popUp = commonLibrary.isExist(UIMAP_Shepards.clickLinkPopUp, 20);
				commonLibrary.clickLink(popUp, "All Shepard's® editorial phrases");

				commonLibrary.smallWait();
				commonLibrary.switchToWindow("shepeditorialmappings");
				report.updateTestLog("Verify the secondary browser displayed", "secondary browser is displayed ", Status.PASS);
				driver.close();
				report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
				commonLibrary.switchToWindow("search");

			} else {
				commonLibrary.clickButton(closeButton);
			}
		} else {
			report.updateTestLog("Verify the shep pop up phrase is displayed", "The shep pop up phrase is not displayed", Status.FAIL);
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify chicklet indicator color and
	// text
	// # Function Name : verifyChickletindicator
	// # Author : Vennila
	// # Date Created : 5 Nov'15
	// #*****************************************************************************************************************************
	public Shepards verifychickletHoverText1(String strDocTitle, String hoverText) {
		WebElement resultClass = null;
		Boolean flag = true;
		if (commonLibrary.isExistNegative(UIMAP_Shepards.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_Shepards.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {

					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_Shepards.TitleClassDoc, 2);
					WebElement treatPhrase = commonLibrary.isExist(document, UIMAP_Shepards.treatPhrase, 2);
					WebElement chickletColor = commonLibrary.isExist(treatPhrase, UIMAP_Shepards.chickletImg, 2);

					if (eleDocTitle != null && treatPhrase != null) {
						if (eleDocTitle.getText().contains(strDocTitle) && chickletColor.getAttribute("title").contains(hoverText)) {
							flag = true;
							break;
						}
					}
				}
			}

			if (flag)
				report.updateTestLog("Verify hover text ',", "Document Title" + strDocTitle + "'Contains" + hoverText, Status.PASS);
			else
				report.updateTestLog("Verify hover text ',", "Document Title" + strDocTitle + "'does not Contains" + hoverText, Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify shep signal appearance
	// # Function Name : verifyShepSignalAppearance1
	// # Author : Vennila M
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public Shepards verifyShepSignalNotAppearance(String docTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.cssSelector("li[data-id*='sr']"), 20);
				for (WebElement document : OListItems) {
					WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
					if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().contains(docTitle.toLowerCase())) {
						WebElement signal = commonLibrary.isExist(UIMAP_Shepards.shepSignal, 10);
						if (signal == null)

							blnFlag = true;
						break;

					}
					if (!blnFlag)
						break;
				}
			}

		}

		if (blnFlag = true)

			report.updateTestLog("Verifying  the appearance of signal", " signal is not displayed", Status.PASS);
		else
			report.updateTestLog("Verifying  the appearance of signal", " signal is displayed", Status.FAIL);
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Filter Header Present
	// # Function Name : verifyFilternotHeaderPresent
	// # Author : Vennila M
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Shepards verifyFilternotHeaderPresent1(String strHeader) {
		Boolean bnlFlag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterheader2, 10);

		for (int i = 0; i < filterHeader.size(); i++) {

			if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

				bnlFlag = true;
				break;
			}

		}
		if (!bnlFlag)
			report.updateTestLog("Verify Filter '" + strHeader + "' not present", "Filter '" + strHeader + "' is not displayed in the left Pane", Status.PASS);
		else
			report.updateTestLog("Verify Filter '" + strHeader + "' is present", "Filter '" + strHeader + "' is  displayed in the left Pane", Status.FAIL);

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify shepard signal with text
	// # Function Name : VerifyFullDocument     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Shepards verifyShepardSignal1(String strText, String strsignal) {

		try {

			boolean blnSignal = false;
			WebElement preview = commonLibrary.isExist(UIMAP_SearchResult.divshep, 10);
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
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Anbu
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Shepards swithToCondentType1(String strCondentType) {
		generalFunctions.selectCondentType(strCondentType);
		return new Shepards(scriptHelper);
	}

	// # Function Description : Function to verify content type is displayed or
	// not for search terms which doesnt have content type
	//
	// # Function Name : verifyContentTypeDisplayed_withoutcontent
	// # Author : Vennila
	// # Date Created : 1 Oct'15
	// #*****************************************************************************************************************************

	public Shepards verifyContentTypeDisplayed_withoutcontent() {

		WebElement ulCondentSwitcher = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 10);
		if (ulCondentSwitcher == null) {
			report.updateTestLog("Verify content type Table of Authorities is displayed.", "Content type Table of Authorities is not displayed.", Status.PASS);
		} else {
			report.updateTestLog("Verify content type Table of Authorities is displayed.", "Content type Table of Authorities is displayed.", Status.FAIL);
		}
		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to uncheck Selected documents
	// # Function Name : uncheckSelectedDocs     
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #**************************************************************************************************************************

	public Shepards uncheckSelectedDocs(String docName, Boolean status) {
		// tabheaders

		WebElement tabheaders = commonLibrary.isExist(UIMAP_Home.tabheaders, 20);
		commonLibrary.selectFromListButton(tabheaders, "Selected Documents");
		boolean flag = false;
		WebElement selecteddocs = commonLibrary.isExist(UIMAP_Home.selecteddocs, 20);
		List<WebElement> documentsList = commonLibrary.isExistList(selecteddocs, UIMAP_SearchResult.listItems, 5);

		for (WebElement item : documentsList) {
			WebElement Label = commonLibrary.isExist(item, By.tagName("label"), 10);
			WebElement Input = commonLibrary.isExist(item, By.tagName("input"), 10);
			if (Label.getText().toLowerCase().contains(docName.toLowerCase())) {
				commonLibrary.setCheckBox(Input, status);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Check/UnCheck Selected Documents.", "Document is not present.", Status.FAIL);
		tabheaders = commonLibrary.isExist(UIMAP_Home.tabheaders, 20);
		commonLibrary.selectFromListButton(tabheaders, "Basic Options");
		return new Shepards(scriptHelper);

	}

}
