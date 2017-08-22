package functionallibraries;

import java.util.List;
import java.util.Properties;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.cognizant.framework.Settings;
import UIMAP.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

import supportlibraries.*;

public class Home extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	Properties localizeProperties = Settings.getInstance();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;

	public Home(ScriptHelper scriptHelper) {

		super(scriptHelper);

		if (!driver.getCurrentUrl().contains("firsttime") && !driver.getCurrentUrl().contains("home")) {
			throw new IllegalStateException("Home page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Landing page panels
	// # Function Name : Verify_LandingPage_Panels
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyLandingPagePanels() {
		String strPanelNames = dataTable.getData("General_Data", "HomePage_Panels");
		String[] arrPanelNames = strPanelNames.split(";");
		for (int i = 0; i < arrPanelNames.length; i++) {
			switch (arrPanelNames[i]) {
			case "History": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassHistory + "']"), 20) != null)
					report.updateTestLog("Verify History Panel is displayed", "History Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify History Panel is displayed", "History Panel is not displayed", Status.FAIL);
				break;

			}
			case "Favourites": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassFavorites + "']"), 20) != null)
					report.updateTestLog("Verify Favourites Panel is displayed", "Favourites Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Favourites Panel is displayed", "Favourites Panel is not displayed", Status.FAIL);
				break;
			}
			case "Folders": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassFolders + "']"), 20) != null)
					report.updateTestLog("Verify Folders Panel is displayed", "Folders Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Folders Panel is displayed", "Folders Panel is not displayed", Status.FAIL);
				break;
			}
			case "Alerts": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassAlert + "']"), 20) != null)
					report.updateTestLog("Verify Alerts Panel is displayed", "Alerts Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Alerts Panel is displayed", "Alerts Panel is not displayed", Status.FAIL);
				break;
			}
			case "News": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassNewspod + "']"), 20) != null)
					report.updateTestLog("Verify News Panel is displayed", "News Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify News Panel is displayed", "News Panel is not displayed", Status.FAIL);
				break;
			}
			case "Notifications": {
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassnotification + "']"), 20) != null)
					report.updateTestLog("Verify Notifications Panel is displayed", "Notifications Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Notifications Panel is displayed", "Notifications Panel is not displayed", Status.FAIL);
				break;
			}
			case "Latest Updates": {

				commonLibrary.scrollDown(1000);
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassLatestupdates + "']"), 20) != null)
					report.updateTestLog("Verify Latest Updates Panel is displayed", "Latest Updates Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Latest Updates Panel is displayed", "Latest Updates Panel is not displayed", Status.FAIL);
				break;
			}
			case "Support": {

				commonLibrary.scrollDown(1000);
				if (commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassSupport + "']"), 20) != null)
					report.updateTestLog("Verify Support Panel is displayed", "Support Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Support Panel is displayed", "Support Panel is not displayed", Status.FAIL);
				break;
			}
			}
		}

		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		// Function calling for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing MedMal Search
	// # Function Name : MedMalSearch
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean medMalSearch(String strSearchTerm) {

		try {

			WebElement txtIdMedMalSearch = commonLibrary.isExist(UIMAP_Home.txtIdMedMalSearch, 20);
			commonLibrary.setDataInTextBox(txtIdMedMalSearch, strSearchTerm, "SearchTerm");
			commonLibrary.sleep(50);
			WebElement btnIdMedMalSearch = commonLibrary.isExist(UIMAP_Home.btnIdMedMalSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdMedMalSearch, "Search");
			commonLibrary.sleep(Mwait);
			WebElement btnIdExpandMadSearch = commonLibrary.isExist(UIMAP_SearchResult.btnIdExpandMadSearch, 20);
			if (btnIdExpandMadSearch != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : ProfileSuiteSearch
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean profileSuiteSearch(String strSearchTerm) {

		try {

			try {
				WebElement txtIdprofileSuiteTextbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox, 20);
				commonLibrary.setDataInTextBox(txtIdprofileSuiteTextbox, strSearchTerm, "SearchTerm");
				commonLibrary.sleep(50);
				commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdprofileSuiteSearch, "Search");
			} catch (Exception e) {
				try {
					if (!driver.getCurrentUrl().contains("lpsresults")) {
						WebElement txtIdprofileSuiteTextbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox, 20);
						commonLibrary.setDataInTextBox(txtIdprofileSuiteTextbox, strSearchTerm, "SearchTerm");
						commonLibrary.sleep(50);

						commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdprofileSuiteSearch, "Search");
					} else {
						WebElement txtIdprofileSuiteTextbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox, 20);
						if (txtIdprofileSuiteTextbox == null) {
							driver.navigate().refresh();

						}
					}
				} catch (Exception e1) {
					if (!driver.getCurrentUrl().contains("lpsresults")) {
						WebElement btnIdprofileSuiteFilter = commonLibrary.isExist(UIMAP_Home.btnIdprofileSuiteFilter, 20);
						commonLibrary.clickButtonParentWithWait(btnIdprofileSuiteFilter, "Filter");

						WebElement btnIdprofileSuiteSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdprofileSuiteSubSearch, 20);
						commonLibrary.highlightElement(btnIdprofileSuiteSubSearch);
						commonLibrary.clickButtonParentWithWait(btnIdprofileSuiteSubSearch, "Search");
					} else {

						driver.navigate().refresh();

					}
				}
			}

			commonLibrary.sleep(Mwait);
			WebElement pgClassHeaderOption3 = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 5);
			if (pgClassHeaderOption3 != null && pgClassHeaderOption3.getText().contains("Results for"))
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.toString());
			WebElement pgClassHeaderOption3 = commonLibrary.isExistNegative(UIMAP_SearchResult.pgClassHeaderOption3, 5);
			if (pgClassHeaderOption3 != null && pgClassHeaderOption3.getText().contains("Results for"))
				return true;
			else
				return false;

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : VerdictSearch
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verdictSearch(String strSearchTerm) {

		try {

			try {
				WebElement txtIdVerdictTextbox = commonLibrary.isExist(UIMAP_Home.txtIdVerdictTextbox, 20);
				commonLibrary.setDataInTextBox(txtIdVerdictTextbox, strSearchTerm, "SearchTerm");
				commonLibrary.sleep(50);
				// txtIdVerdictTextbox.sendKeys(Keys.ENTER);
				// WebElement btnIdVerdictSearch =
				// commonLibrary.isExist(UIMAP_Home.btnIdVerdictSearch, 20);
				// commonLibrary.highlightElement(btnIdVerdictSearch);
				commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdVerdictSearch, "Search");

			} catch (Exception e) {
				try {
					if (!driver.getCurrentUrl().contains("vsasearchresults")) {
						WebElement txtIdVerdictTextbox = commonLibrary.isExist(UIMAP_Home.txtIdVerdictTextbox, 20);
						commonLibrary.setDataInTextBox(txtIdVerdictTextbox, strSearchTerm, "SearchTerm");
						commonLibrary.sleep(50);
						// WebElement btnIdVerdictSearch =
						// commonLibrary.isExist(UIMAP_Home.btnIdVerdictSearch,
						// 20);
						// commonLibrary.highlightElement(btnIdVerdictSearch);
						commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdVerdictSearch, "Search");

					} else {
						WebElement txtIdVerdictTextbox = commonLibrary.isExist(UIMAP_Home.txtIdVerdictTextbox, 20);
						if (txtIdVerdictTextbox == null) {
							String strUrl = driver.getCurrentUrl();
							driver.navigate().to(strUrl);
							driver.navigate().refresh();
							Actions actionObject = new Actions(driver);
							actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();

						}
					}
				} catch (Exception e1) {
					if (!driver.getCurrentUrl().contains("vsasearchresults")) {
						WebElement btnIdVerdictFilter = commonLibrary.isExist(UIMAP_Home.btnIdVerdictFilter, 20);
						commonLibrary.clickButtonParentWithWait(btnIdVerdictFilter, "Filter");
						// eltSearchbox.sendKeys(Keys.ESCAPE);
						// WebElement btnClassFilter1 =
						// commonLibrary.isExist(UIMAP_HomePage.btnClassFilter,
						// 20);
						// commonLibrary.clickButton_Parent_WithWait(btnClassFilter1,
						// "Filter");

						// WebElement btnIdVerdictSubSearch =
						// commonLibrary.isExist(UIMAP_Home.btnIdVerdictSubSearch,
						// 20);
						// commonLibrary.highlightElement(btnIdVerdictSubSearch);
						commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdVerdictSubSearch, "Search");

					} else {
						String strUrl = driver.getCurrentUrl();
						driver.navigate().to(strUrl);
						driver.navigate().refresh();
						Actions actionObject = new Actions(driver);
						actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();

					}
				}
			}

			// eltSearchbox.sendKeys(Keys.SPACE);
			// eltSearchbox.sendKeys(Keys.ENTER);
			// WebElement eltSearchbutton =
			// commonLibrary.isExist(UIMAP_HomePage.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			// commonLibrary.Click_JS(UIMAP_HomePage.btnIdSearch);//(eltSearchbutton,
			// "Search");
			// commonLibrary.Click_Action_MoveByOffset_ByCoordinate(UIMAP_HomePage.btnIdSearch);
			commonLibrary.sleep(Mwait);
			WebElement pgClassHeaderOption3 = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader1, 20);
			if (pgClassHeaderOption3 != null && pgClassHeaderOption3.getText().contains("Results for"))
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.toString());
			WebElement pgClassHeaderOption3 = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader1, 20);
			if (pgClassHeaderOption3 != null && pgClassHeaderOption3.getText().contains("Results for"))
				return true;
			else
				return false;

			// throw new FrameworkException("Exception",e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : NavigateToHistoryLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void navigateToHistoryLink(String strDocTitle) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickButtonParentWithWait(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);

			WebElement btnIdRecentDocuments = commonLibrary.isExist(UIMAP_Home.btnIdRecentDocuments, 20);
			commonLibrary.clickButtonParentWithWait(btnIdRecentDocuments, "Recent Documents");

			WebElement lstrecentDocumentsContent = commonLibrary.isExist(UIMAP_Home.lstrecentDocumentsContent, 20);
			if (lstrecentDocumentsContent != null) {
				List<WebElement> lnkDocuments = commonLibrary.isExistList(lstrecentDocumentsContent, By.linkText(strDocTitle), 20);
				if (lnkDocuments.size() > 0)
					lnkDocuments.get(0).click();

				commonLibrary.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
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
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home applySearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		generalFunctions.applySearchFilter(strToolbarMenuName, strCondentTypes, state);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyFilterSelected(String strSelectedFilterOptions) {
		Boolean blnFalg = false;
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {

			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 20);
			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {

					for (WebElement item : divClassDeleteFilter) {
						System.out.println(arrSelectedFilterOptions[i]);
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							blnFalg = true;

							break;
						}
					}
				}

			}
			if (blnFalg) {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy textbox", strSelectedFilterOptions + " is displayed in NarrowBy textbox", Status.PASS);
			} else {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy textbox", strSelectedFilterOptions + " is not displayed in NarrowBy textbox", Status.FAIL);
			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Home(scriptHelper);
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
	// # Function Description : Function for navigating to reserach map page
	// # Function Name : navigateToResearchMap
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public ResearchMap navigateToResearchMap() {
		generalFunctions.navigateToHistoryFooterLink("Research Map");
		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : NavigateToBrowserLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	// public void NavigateToBrowserLink(String strParentMenuName,String
	// strFirstSubMenuName,String strSecondSubMenuName,String
	// strThridSubMenuName,String strAction)
	// {
	//
	//
	// try
	// {
	//
	// Boolean
	// blnFirst=false,blnSecond=false,blnThird=false,blnFour=false,blnFive=false;
	// WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse,
	// 20);
	// commonLibrary.clickButton_Parent_WithWait(btnIdBrowse, "Browse");
	// commonLibrary.sleep(Mwait);
	//
	// WebElement divIdBrowserMenu =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
	// if (divIdBrowserMenu!=null)
	// {
	// WebElement lstTagAside =
	// commonLibrary.isExist(divIdBrowserMenu,UIMAP_Home.lstTagAside, 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(lstTagAside,UIMAP_Home.lstTagListItems, 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains(strParentMenuName))
	// {
	// blnFirst=true;
	// if(browsername.contains("internet"))
	// commonLibrary.click_JS(button,strParentMenuName);
	// else
	// commonLibrary.clickButton_Parent_WithWait(button, strParentMenuName);
	// break;
	// }
	//
	// }
	// if(!blnFirst)
	// report.updateTestLog("Click on "+strParentMenuName,
	// "Not Clicked on "+strParentMenuName, Status.FAIL);
	// }
	// if(strFirstSubMenuName!="")
	// {
	// WebElement divIdBrowserSubMenu =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
	// if (divIdBrowserSubMenu!=null)
	// {
	// List<WebElement> lstTagAside =
	// commonLibrary.isExist_List(divIdBrowserSubMenu,UIMAP_Home.lstTagAside,
	// 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(lstTagAside.get(0),UIMAP_Home.lstTagListItems,
	// 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains(strFirstSubMenuName))
	// {
	// blnSecond=true;
	// if(browsername.contains("internet") )
	// commonLibrary.click_JS(button,strFirstSubMenuName);
	// else
	// commonLibrary.clickButton_Parent_WithWait(button, strFirstSubMenuName);
	// break;
	// }
	//
	// }
	// if(!blnSecond)
	// report.updateTestLog("Click on "+strFirstSubMenuName,
	// "Not Clicked on "+strFirstSubMenuName, Status.FAIL);
	// }
	// }
	// if(strSecondSubMenuName!="")
	// {
	//
	// WebElement divIdBrowserSubMenu1 =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
	// if (divIdBrowserSubMenu1!=null)
	// {
	// List<WebElement> lstTagAside =
	// commonLibrary.isExist_List(divIdBrowserSubMenu1,UIMAP_Home.lstTagAside,
	// 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(lstTagAside.get(1),UIMAP_Home.lstTagListItems,
	// 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains(strSecondSubMenuName))
	// {
	// blnThird=true;
	// if(browsername.contains("internet"))
	// commonLibrary.click_JS(button,strSecondSubMenuName);
	// else
	// commonLibrary.clickButton_Parent_WithWait(button, strSecondSubMenuName);
	// break;
	// }
	//
	// }
	// if(!blnThird)
	// report.updateTestLog("Click on "+strSecondSubMenuName,
	// "Not Clicked on "+strSecondSubMenuName, Status.FAIL);
	// }
	//
	// }
	// if(strThridSubMenuName!="" && !strThridSubMenuName.contains("Actions"))
	// {
	// WebElement divIdBrowserSubMenu1 =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
	// if (divIdBrowserSubMenu1!=null)
	// {
	// List<WebElement> lstTagAside =
	// commonLibrary.isExist_List(divIdBrowserSubMenu1,UIMAP_Home.lstTagAside,
	// 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(lstTagAside.get(2),UIMAP_Home.lstTagListItems,
	// 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains(strThridSubMenuName))
	// {
	// blnFour=true;
	// if(browsername.contains("internet"))
	// commonLibrary.click_JS(button,strThridSubMenuName);
	// else
	// commonLibrary.clickButton_Parent_WithWait(button, strThridSubMenuName);
	// break;
	// }
	//
	// }
	// if(!blnFour)
	// report.updateTestLog("Click on "+strThridSubMenuName,
	// "Not Clicked on "+strThridSubMenuName, Status.FAIL);
	// }
	//
	//
	// }
	// else if(strThridSubMenuName.contains("Actions"))
	// {
	// WebElement divIdBrowserSubMenu1 =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
	// List<WebElement> lstTagAside =
	// commonLibrary.isExist_List(divIdBrowserSubMenu1,UIMAP_Home.lstTagAside,
	// 20);
	// WebElement btnActions =
	// commonLibrary.isExist(lstTagAside.get(2),UIMAP_Home.btnTypeButton, 20);
	// // commonLibrary.highlightElement(btnActions);
	// commonLibrary.clickButton_Parent_WithWait(btnActions,
	// "ACTIONS FOR AGENCY ADJUDICATION");
	//
	//
	// WebElement divIdBrowserSubMenu2 =
	// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
	// List<WebElement> lstTagAside1 =
	// commonLibrary.isExist_List(divIdBrowserSubMenu2,UIMAP_Home.lstTagAside,
	// 20);
	// WebElement divClassActionsList =
	// commonLibrary.isExist(lstTagAside1.get(2),UIMAP_Home.divClassActionsList,
	// 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(divClassActionsList,UIMAP_Home.lstTagListItems,
	// 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains(strAction))
	// {
	// blnFive=true;
	// if(browsername.contains("internet"))
	// commonLibrary.click_JS(button,strAction);
	// else
	// commonLibrary.clickButton_Parent_WithWait(button, strAction);
	// break;
	// }
	//
	// }
	//
	//
	// if(!blnFive)
	// report.updateTestLog("Click on "+strAction, "Not Clicked on "+strAction,
	// Status.FAIL);
	// }
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	// //#*****************************************************************************************************************************
	// //# Function Description : Function for Navigating through tabs in Status
	// Report pod
	// //# Function Name : SelectStatusReportTab
	// //# Author : Pratik
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	// public void SelectStatusReportTab(String strTabName)
	// {
	// try
	// {
	// switch(strTabName)
	// {
	// case "Individual Student Report":
	// WebElement btnIndividualStudentReportTab =
	// commonLibrary.isExist(UIMAP_Home.btnIndividualStudentReportTab);
	// if(btnIndividualStudentReportTab!=null)
	// {
	// commonLibrary.clickButton(btnIndividualStudentReportTab);
	// report.updateTestLog("Selecting Individual Student Report Tab.","Individual Student Report Tab Selected.",Status.PASS);
	// }
	// else
	// report.updateTestLog("Selecting Individual Student Report Tab","Individual Student Report Tab can not be Selected.",Status.FAIL);
	// break;
	// case "Excercise Report":
	// WebElement btnExcerciseReportTab =
	// commonLibrary.isExist(UIMAP_Home.btnExcerciseReportTab);
	// if(btnExcerciseReportTab!=null)
	// {
	// commonLibrary.clickButton(btnExcerciseReportTab);
	// report.updateTestLog("Selecting Excercise Report Tab.","Excercise Report Tab Selected.",Status.PASS);
	// }
	// else
	// report.updateTestLog("Excercise Student Report Tab","Excercise Report Tab can not be Selected.",Status.FAIL);
	// break;
	// }
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	//
	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the links under Client
	// Menu
	// # Function Name : NavigateToClientLink
	// # Author : Anbu
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void navigateToClientLink(String strLinkName) {

		try {

			WebElement btnClassClient = commonLibrary.isExist(UIMAP_Home.btnClassClient, 20);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnClassClient, "Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassClient, "Set Client ID");
			}
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
				WebElement lnkTxtSetAddClientID = commonLibrary.isExist(UIMAP_Home.btnActionSetAddClientID, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtSetAddClientID, "Set/Add Client ID");
			} else if (strLinkName.equalsIgnoreCase("-None-")) {
				WebElement lnkTxtNone = commonLibrary.isExist(UIMAP_Home.lnkTxtNone, 20);
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(lnkTxtNone, "Set Client ID");
				} else {
					commonLibrary.clickLinkWithWebElement(lnkTxtNone, "Set Client ID");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// //#*****************************************************************************************************************************
	// //# Function Description : Function for selecting the links under Client
	// Menu
	// //# Function Name : NavigateToClientLink
	// //# Author : Uma
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	//
	// public void SetNewClientID(String strClientID)
	// {
	//
	//
	// try
	// {
	// //Click Client or Matter dropdown in Global Navigation
	// //Click Set/Add Client ID or Click Set/Add Matter ID
	// //NavigateToClientLink("Set/Add Client ID");
	//
	// //Select New Client ID or New Matter ID Radio button
	// WebElement rdoClientId =
	// commonLibrary.isExist_Negative(UIMAP_SearchResult.rdoClientId,10);
	// if(rdoClientId!=null)
	// {
	// report.updateTestLog("Selecting Set New Client ID",
	// "New Client Option Selected.", Status.PASS);
	// commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
	// }
	//
	//
	// //ENTER A <<<>>> ID IN TEXT BOX
	// WebElement txtNewClientId =
	// commonLibrary.isExist_Negative(UIMAP_SearchResult.txtNewClientId,10);
	// if(txtNewClientId!=null)
	// {
	// commonLibrary.SetDataInTextBox(txtNewClientId, strClientID,
	// "Enter NEw Client ID");
	// report.updateTestLog("Setting New Client ID",
	// "New Client ID is set to Abcd.", Status.PASS);
	// }
	//
	// //Click Set Client ID Button or Set Matter ID button
	// WebElement btnSaveClientId =
	// commonLibrary.isExist_Negative(UIMAP_SearchResult.btnSaveClientId,10);
	// if(btnSaveClientId!=null)
	// {
	// report.updateTestLog("Clicking on Save Client ID",
	// "Save Client ID is clicked.", Status.PASS);
	// commonLibrary.clickButton(btnSaveClientId);
	// }
	//
	//
	// //MV <Quick smoke> should be set as global client ID and Landing page
	// displays
	//
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	// //#*****************************************************************************************************************************
	// //# Function Description : Function for adding new client id from home
	// page
	// //# Function Name : SetNewClientID_Home
	// //# Author : Uma
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	//
	// public void SetNewClientID_Home(String strClientID)
	// {
	//
	//
	// try
	// {
	// //Click Client or Matter dropdown in Global Navigation
	// //Click Set/Add Client ID or Click Set/Add Matter ID
	// NavigateToClientLink("Set/Add Client ID");
	//
	// //Select New Client ID or New Matter ID Radio button
	// WebElement rdoClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.rdoClientId);
	// if(rdoClientId!=null)
	// {
	// report.updateTestLog("Selecting Set New Client ID",
	// "New Client Option Selected.", Status.PASS);
	// commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
	// }
	// else
	// report.updateTestLog("Selecting Set New Client ID",
	// "Client Option Radio button not found.", Status.FAIL);
	//
	// //ENTER A <<<>>> ID IN TEXT BOX
	// WebElement txtNewClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.txtNewClientId);
	// if(txtNewClientId!=null)
	// {
	// commonLibrary.SetDataInTextBox(txtNewClientId, strClientID, "Client id");
	// //report.updateTestLog("Setting New Client ID",
	// "New Client ID is set to Abcd.", Status.PASS);
	// }
	// else
	// report.updateTestLog("Setting New Client ID",
	// "New Client ID can not be set.", Status.FAIL);
	// //Click Set Client ID Button or Set Matter ID button
	// WebElement btnSaveClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.btnSaveClientId);
	// if(btnSaveClientId!=null)
	// {
	// report.updateTestLog("Clicking on Save Client ID",
	// "Save Client ID is clicked.", Status.PASS);
	// commonLibrary.clickButton_Parent_WithWait(btnSaveClientId,
	// "Save Client");
	// }
	// else
	// report.updateTestLog("Clicking on Save Client ID",
	// "Save Client ID can not be clicked.", Status.FAIL);
	//
	// //MV <Quick smoke> should be set as global client ID and Landing page
	// displays
	// verify_LandingPage_Panels();
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	//
	// //#*****************************************************************************************************************************
	// //# Function Description : Function for selecting the links under Lexis
	// Advance Menu
	// //# Function Name : NavigateToLexisAdvanceLink
	// //# Author : Uma
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	//
	// public void NavigateToLexisAdvanceLink(String strLinkName,String
	// strPageHeader)
	// {
	//
	//
	// try
	// {
	//
	// WebElement btnIdLexisAdvance =
	// commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
	// commonLibrary.clickButton_Parent_WithWait(btnIdLexisAdvance,
	// "Lexis Advance Arrow");
	// commonLibrary.sleep(Mwait);
	//
	// if(strLinkName.equalsIgnoreCase("Counsel Benchmarking"))
	// {
	// WebElement btnActionCunselBenchmarking =
	// commonLibrary.isExist(UIMAP_Home.btnActionCunselBenchmarking, 20);
	//
	// if((browsername.contains("internet")))
	//
	// commonLibrary.click_JS(btnActionCunselBenchmarking,
	// btnActionCunselBenchmarking.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionCunselBenchmarking,
	// "Counsel Benchmarking");
	//
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().contains(strPageHeader))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	//
	// }
	// else if(strLinkName.equalsIgnoreCase("Litigation Profile Suite"))
	// {
	// WebElement btnActionProfileSuite =
	// commonLibrary.isExist(UIMAP_Home.btnActionProfileSuite, 20);
	// if((browsername.contains("internet")))
	//
	// commonLibrary.click_JS(btnActionProfileSuite,
	// btnActionProfileSuite.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionProfileSuite,
	// "Litigation Profile Suite");
	//
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption1,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().contains(strPageHeader))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	// }
	// else if(strLinkName.equalsIgnoreCase("MedMal Navigator"))
	// {
	// WebElement btnActionMedMal =
	// commonLibrary.isExist(UIMAP_Home.btnActionMedMal, 20);
	// if((browsername.contains("internet") ))
	//
	// commonLibrary.click_JS(btnActionMedMal, btnActionMedMal.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionMedMal,
	// "MedMal Navigator");
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption2,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().contains(strPageHeader))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	// }
	// else if(strLinkName.equalsIgnoreCase("Verdict & Settlement Analyzer"))
	// {
	// WebElement btnActionVSA = commonLibrary.isExist(UIMAP_Home.btnActionVSA,
	// 20);
	// if((browsername.contains("internet") ))
	//
	// commonLibrary.click_JS(btnActionVSA, btnActionVSA.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionVSA,
	// "Verdict & Settlement Analyzer");
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption1,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().contains(strPageHeader))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	// }
	// else if(strLinkName.equalsIgnoreCase("Lexis Practice Advisor"))
	// {
	// WebElement btnActionLPA = commonLibrary.isExist(UIMAP_Home.btnActionLPA,
	// 20);
	// if((browsername.contains("internet")))
	//
	// commonLibrary.click_JS(btnActionLPA, btnActionLPA.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionLPA,
	// "Lexis Practice Advisor");
	//
	// }
	// else if(strLinkName.equalsIgnoreCase("Public Records"))
	// {
	//
	// WebElement btnActionPublicRecords =
	// commonLibrary.isExist(UIMAP_Home.btnActionPublicRecords, 20);
	//
	// // SeleniumTest String browsername=cap.getBrowserName();
	//
	// if((browsername.contains("internet") ))
	//
	// commonLibrary.click_JS(btnActionPublicRecords,
	// btnActionPublicRecords.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnActionPublicRecords,
	// "Public Records");
	//
	// commonLibrary.sleep(3000);
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption4,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	// }
	// else if(strLinkName.equalsIgnoreCase("Lexis Advance® Tax"))
	// {
	// WebElement btnLexisAdvanceTax =
	// commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceTax, 20);
	// if((browsername.contains("internet")))
	//
	// commonLibrary.click_JS(btnLexisAdvanceTax, btnLexisAdvanceTax.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnLexisAdvanceTax,
	// "Lexis Advance® Tax");
	// commonLibrary.sleep(3000);
	//
	// }
	// else if(strLinkName.equalsIgnoreCase("Research"))
	// {
	// WebElement btnLexisAdvanceResearch =
	// commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
	// if((browsername.contains("internet")))
	//
	// commonLibrary.click_JS(btnLexisAdvanceResearch,
	// btnLexisAdvanceResearch.getText());
	// else
	// commonLibrary.clickLink_withWebElement(btnLexisAdvanceResearch,
	// "Lexis Advance® Research");
	//
	// commonLibrary.sleep(3000);
	// WebElement
	// HeaderSearchResult1=commonLibrary.isExist(UIMAP_SearchResult.pgClassHeaderOption4,
	// 10);
	//
	// if(HeaderSearchResult1!=null &&
	// HeaderSearchResult1.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
	//
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	//
	// }
	//
	//
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	// //#*****************************************************************************************************************************
	// //# Function Description : Function for selecting the links under
	// Practice Area
	// //# Function Name : SelectPracticeAreaLink
	// //# Author : Pratik
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	//
	// public void SelectPracticeAreaLink(String strLinkText, String
	// strPageHeader)
	// {
	// try{
	//
	//
	// WebElement btnSelectPracticeArea =
	// commonLibrary.isExist(UIMAP_Home.btnSelectPracticeArea);
	// commonLibrary.clickButton(btnSelectPracticeArea);
	//
	// if(strLinkText.equals("Banking & Finance"))
	// {
	// WebElement lnkBankingAndFinance =
	// commonLibrary.isExist(UIMAP_Home.lnkBankingAndFinance);
	// commonLibrary.clickButton(lnkBankingAndFinance);
	// commonLibrary.sleep(3000);
	// WebElement txtPageHeading =
	// commonLibrary.isExist(UIMAP_Home.txtPracticeAreaHeading);
	// if(txtPageHeading.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is displayed", Status.PASS);
	// else
	// report.updateTestLog("Verify "+strPageHeader+" is displayed",
	// strPageHeader+" is not displayed", Status.FAIL);
	// }
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	//
	// //#*****************************************************************************************************************************
	// //# Function Description : Function selecting link from Topic Pod
	// //# Function Name : TopicPodSelectLink
	// //# Author : Pratik
	// //# Date Created : Jan'15
	// //#*****************************************************************************************************************************
	//
	// public void TopicPodSelectLink(String strLinkName)
	// {
	//
	//
	// try
	// {
	// WebElement eltTopicPod =
	// commonLibrary.isExist_Negative(UIMAP_Home.eltTopicPod, 20);
	// if (eltTopicPod!=null)
	// {
	// List<WebElement> lstLinks =
	// commonLibrary.isExist_List(eltTopicPod,UIMAP_Home.lnkTopicPod, 20);
	// if(lstLinks.size()>0)
	// for (WebElement button : lstLinks) {
	// if(button.getText().contains(strLinkName))
	// {
	// commonLibrary.clickButton(button);
	// break;
	// }
	//
	// }
	//
	// }
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }
	//
	//
	// //#*****************************************************************************************************************************
	// //# Function Description : Function to take ScreenShot of current page
	// //# Function Name : TakeScreenShot
	// //# Author : Pratik
	// //# Date Created : Feb'15
	// //#*****************************************************************************************************************************
	//
	// public void TakeScreenShot(String strDestination)
	// {
	// try
	// {
	// File scrFile =
	// ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	// Files.copy(scrFile.toPath(), new File(strDestination).toPath());
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the expansion of pods present
	// in home page
	// # Function Name : Verify_Pod_Expansion_Status
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyPodExpansionStatus(String PodName, String PodStatus) {
		Boolean blnExpand = false;
		Boolean blnCollapse = false;
		WebElement Pod = null;
		commonLibrary.sleep(10000);
		switch (PodName) {
		case "History": {
			Pod = commonLibrary.isExist(UIMAP_Home.divHistoryPod, 20);
			break;
		}
		case "Favorites": {
			Pod = commonLibrary.isExist(UIMAP_Home.divFavoritesPod, 20);
			break;
		}
		case "Folders": {
			Pod = commonLibrary.isExist(UIMAP_Home.divFoldersPod, 20);
			break;
		}
		case "Alerts": {
			Pod = commonLibrary.isExist(UIMAP_Home.divAlertsPod, 20);
			break;
		}
		case "News": {
//			WebElement PodParent = commonLibrary.isExist(UIMAP_Home.divNewsPod_Parent, 20);
			Pod = commonLibrary.isExist(UIMAP_Home.divNewsPod, 20);
			break;
		}
		case "Notifications": {
			Pod = commonLibrary.isExist(UIMAP_Home.divNotificationPod, 20);
			break;
		}

		}
		if (Pod != null) {
			if (Pod.getAttribute("class").contains("expanded")) {
				blnExpand = true;
			} else if (Pod.getAttribute("class").contains("collapsed")) {
				blnCollapse = true;
			}
		} else {
			report.updateTestLog("Verify " + PodName + " pod is dsiplayed", "" + PodName + "pod is not displayed", Status.FAIL);
			return new Home(scriptHelper);
		}

		switch (PodStatus) {
		case "Expanded": {
			if (blnExpand) {
				report.updateTestLog("Verify " + PodName + " is expanded", "" + PodName + " is in expanded status", Status.PASS);
			} else {
				report.updateTestLog("Verify " + PodName + " is expanded", "" + PodName + " is not in expanded status", Status.FAIL);
			}
			break;
		}
		case "Collapsed": {
			if (blnCollapse) {
				report.updateTestLog("Verify " + PodName + " is collapsed", "" + PodName + " is in collapsed status", Status.PASS);
			} else {
				report.updateTestLog("Verify " + PodName + " is collapsed", "" + PodName + " is not in collapsed status", Status.FAIL);
			}
			break;
		}
		}

		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Expand/Collapse pods present in home
	// page
	// # Function Name : Pod_Expansion_Collapse
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home podExpansionCollapse(String PodName, String PodStatus) {
		WebElement Pod = null;
		switch (PodName) {
		case "History": {
			Pod = commonLibrary.isExist(UIMAP_Home.divHistoryPod, 20);
			break;
		}
		case "Favorites": {
			Pod = commonLibrary.isExist(UIMAP_Home.divFavoritesPod, 20);
			break;
		}
		case "Folders": {
			Pod = commonLibrary.isExist(UIMAP_Home.divFoldersPod, 20);
			break;
		}
		case "Alerts": {
			Pod = commonLibrary.isExist(UIMAP_Home.divAlertsPod, 20);
			break;
		}
		case "News": {
			Pod = commonLibrary.isExist(UIMAP_Home.divNewsPod, 20);
			break;
		}
		case "Notifications": {
			Pod = commonLibrary.isExist(UIMAP_Home.divNotificationPod, 20);
			break;
		}

		}

		if (Pod != null) {
			WebElement ExpandIcon = commonLibrary.isExistNegative(Pod, UIMAP_Home.btnPodExpansion, 10);
			if (ExpandIcon == null)
				ExpandIcon = commonLibrary.isExist(Pod, By.cssSelector("button[class='icon']"), 10);

			switch (PodStatus) {
			case "Expand": {
				if (Pod.getAttribute("class").contains("collapsed")) {
					commonLibrary.clickButtonLogSmallWait(ExpandIcon, "Expand");
					// report.updateTestLog("Click on Expand icon of " + PodName
					// + " pod", PodName + " pod is expanded", Status.PASS);
				} else if (Pod.getAttribute("class").contains("expanded")) {
					report.updateTestLog("Click on Expand icon of " + PodName + " pod", PodName + " pod is expanded", Status.PASS);
				}

				break;
			}
			case "Collapse": {
				if (Pod.getAttribute("class").contains("expanded")) {
					// commonLibrary.clickButton(ExpandIcon);
					commonLibrary.clickButtonLogSmallWait(ExpandIcon, "Collapse");
					// report.updateTestLog("Click on Collapse icon of " +
					// PodName + " pod", PodName + " pod is collapsed",
					// Status.PASS);
				} else if (Pod.getAttribute("class").contains("collapsed")) {
					report.updateTestLog("Click on Collapse icon of " + PodName + " pod", PodName + " pod is collapsed", Status.PASS);
				}
				break;
			}
			}
		} else {
			report.updateTestLog("Verify " + PodName + " pod is dsiplayed", "" + PodName + "pod is not displayed", Status.FAIL);
			return new Home(scriptHelper);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the home page is displayed
	// # Function Name : verifyHomePage
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyHomePage() {

		// Click on get started button if welcome page is displayed
		WebElement dlgWelcome = commonLibrary.isExistNegative(UIMAP_Home.dlgWelcome, 2);
		if (dlgWelcome != null) {

			WebElement btnGetStarted = commonLibrary.isExist(dlgWelcome, UIMAP_Home.btnGetStarted, 20);
			if (btnGetStarted != null) {

				commonLibrary.clickLinkWithWebElementWithWait(btnGetStarted, "Get Started");

				WebElement eltSearchbox = commonLibrary.isExistNegative(UIMAP_Home.txtIdSearch, 5);
				int counter = 0;
				do {
					counter = counter + 1;
					eltSearchbox = commonLibrary.isExistNegative(UIMAP_Home.txtIdSearch, 2);
					if (eltSearchbox == null)
						commonLibrary.sleep(5000);
				} while (eltSearchbox == null && counter <= 10);
			}
		}

		// Code to close Latest Updates dialog box
		WebElement closeDialog = commonLibrary.isExistNegative(UIMAP_Home.closeDialog, 5);
		if (closeDialog != null) {
			commonLibrary.clickButtonParentWithWaitJS(closeDialog, "Close Popup");
		}

		// Verification point : Home page is displayed
		if (driver.getCurrentUrl().toLowerCase().contains(UIMAP_Home.txtHomeTitleMsg)) {
			report.updateTestLog("Verify Landing Page", "Landing Page is displayed", Status.PASS);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing search
	// # Function Name : simpleSearchShepards
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards simpleSearchShepards(String strSearchTerm, Boolean strClearFilter) {

		// Function call for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error Handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Shepard", "Shepards Page");
		pageCheck.handleFlow(driver, check);

		return new Shepards(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a link in Latest Updates Pod
	// # Function Name : ClickLatestUpdatesPodLink
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public LexisAdvanceSupport clickLatestUpdatesPodLink(String strLink) {

		WebElement learnMoreAboutLatestRelease = commonLibrary.isExistNegative(UIMAP_Home.latestReleaseLearnMore, 10);

		// boolean blnFlag=false;
		if (learnMoreAboutLatestRelease != null) {
			int oldCount = driver.getWindowHandles().size();
			commonLibrary.clickButtonParentWithWait(learnMoreAboutLatestRelease, "Learn more about our latest updates");
			int count = 0;
			int newCount = driver.getWindowHandles().size();
			do {
				commonLibrary.sleep(100000);
				count++;
				newCount = driver.getWindowHandles().size();
			} while (oldCount == newCount && count < 20);
			commonLibrary.switchToWindow("support");
			return new LexisAdvanceSupport(scriptHelper);

		} else {
			throw new FrameworkException("Click on " + strLink + " link in Latest Updates pod.", strLink + " link is not present");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click a link in Support Pod
	// # Function Name : ClickSupportPodLink
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public LexisAdvanceHelp clickSupportPodLink(String strLink) {
		WebElement eltSupportPod = commonLibrary.isExistNegative(UIMAP_Home.eltSupportPod, 10);
		List<WebElement> lnkSupportPod = commonLibrary.isExistList(eltSupportPod, UIMAP_Home.lnkLinks, 10);
		int i;
		for (i = 0; i < lnkSupportPod.size(); i++) {
			if (lnkSupportPod.get(i).getText().contains(strLink)) {
				commonLibrary.clickMethod(lnkSupportPod.get(i), lnkSupportPod.get(i).getText());
				try {
					commonLibrary.sleep(8000);
				} catch (Exception e) {
					System.out.println(e.toString());
					throw new FrameworkException("Exception", e.toString());
				}
				commonLibrary.switchToWindow("newlexis");
				return new LexisAdvanceHelp(scriptHelper);

			}
		}
		throw new FrameworkException("Click on " + strLink + " link in Latest Updates pod.", strLink + " link is not present");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the searh Box
	// # Function Name : verifySearchBox
	// # Author : Baswaraj
	// # Date Created : dec'14
	// #****************************************************************************
	public void verifySearchBox() {
		WebElement searchBox = commonLibrary.isExist(UIMAP_SearchResult.txtIdSearch, 20);
		if (searchBox != null) {
			report.updateTestLog("Verifying Search Box", "Search Box Displays center at the Landing Page", Status.PASS);
		} else {
			report.updateTestLog("Verifying Search Box", "Search Box is not displayed", Status.PASS);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Research page by
	// clicking Lexis Advance arrow
	// # Function Name : NavigateToReserachPage
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research navigateToResearchPage() {
		generalFunctions.productSwitcher("Research");
		return new Research(scriptHelper);

	}

	// public void navigateToResearchPage()
	// {
	// try
	// {
	// int m=0;
	// WebElement btnIdLexisAdvance =
	// commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
	// commonLibrary.clickButton_Parent_WithWait(btnIdLexisAdvance,
	// "Lexis Advance Arrow");
	// commonLibrary.sleep(Mwait);
	//
	// WebElement btnLexisAdvanceResearch =
	// commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
	// commonLibrary.clickLink_withWebElement(btnLexisAdvanceResearch,
	// "Lexis Advance® Research");
	// commonLibrary.sleep(3000);
	//
	// WebElement CurrentProduct =
	// commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
	// if(CurrentProduct.getText().toLowerCase().contains("research"))
	// {
	// report.updateTestLog("Research landing page is displayed",
	// "Research landing page is displayed", Status.PASS);
	// }
	// else
	// {
	// report.updateTestLog("Research landing page is displayed",
	// "Research landing page is not displayed", Status.FAIL);
	// }
	//
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception", e.toString());
	// }
	// }
	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the news pod is displayed
	// with news label at the left and powered by logo at the right
	// # Function Name : VerifyNewsPodWithNewsLabelAndPoweredByLogo
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyNewsPodWithNewsLabelAndPoweredByLogo(String strLabel, String strLogo) {
		boolean blnLabel = false;
		boolean blnLogo = false;
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			report.updateTestLog("Verify News pod displays in landing page in Left sidebelow <Folders> pod", "News pod is displayed in landing page in Left sidebelow <Folders> pod", Status.PASS);

		} else {

			report.updateTestLog("Verify News pod displays in landing page in Left sidebelow <Folders> pod", "News pod is not displayed in landing page in Left sidebelow <Folders> pod", Status.FAIL);

		}
		WebElement news1 = commonLibrary.isExist(UIMAP_Home.divNews, 30);
		if (news1 != null) {
			List<WebElement> span = commonLibrary.isExistList(news1, UIMAP_SearchResult.tagSpan, 10);

			int i;
			int m = 0;
			int n = 0;
			for (i = 0; i < span.size(); i++) {
				if (span.get(i).getText().equalsIgnoreCase(strLabel)) {
					m = i;
					blnLabel = true;
				} else if (span.get(i).getText().equalsIgnoreCase(strLogo)) {
					n = i;
					blnLogo = true;
				}
				if (blnLabel && blnLogo)
					break;
			}

			if (m < n) {
				report.updateTestLog("Verify News label displays at the top left of Pod", "News label is displayed at the top left of Pod", Status.PASS);
				report.updateTestLog("Verify Powered by: LAW360 logo displays at the top rightof pod", "Powered by: LAW360 logo is displayed at the top rightof pod", Status.PASS);
			} else {
				report.updateTestLog("Verify News label displays at the top left of Pod", "News label is not displayed at the top left of Pod", Status.FAIL);
				report.updateTestLog("Verify Powered by: LAW360 logo displays at the top rightof pod", "Powered by: LAW360 logo is not displayed at the top rightof pod", Status.FAIL);
			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click powered by: in news pod
	// # Function Name : ClickPoweredBy
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home clickPoweredBy() {
		boolean blnFlag = false;
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			List<WebElement> lnk = commonLibrary.isExistList(news, UIMAP_SearchResult.lnkHeader, 10);
			for (int i = 0; i < lnk.size(); i++) {
				if (lnk.get(i).getText().equalsIgnoreCase("Powered by:")) {
					List<WebElement> img = commonLibrary.isExistList(lnk.get(i), By.tagName("img"), 10);
					for (WebElement item : img) {
						if (item.getAttribute("src").contains("Law360")) {
							commonLibrary.clickJS(item, "Law360");
							blnFlag = true;
							break;
						}
					}
					if (blnFlag)
						break;
				}
			}
		}
		// if (blnFlag) {
		// report.updateTestLog("Click on <Powered by: LAW360> logo",
		// "<Powered by: LAW360> logo is clicked", Status.PASS);
		// } else {
		// report.updateTestLog("Click on <Powered by: LAW360> logo",
		// "<Powered by: LAW360> logo is not clicked", Status.FAIL);
		// }
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify recent 5 news present in news
	// pod
	// # Function Name : ClickPoweredBy
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyRecentFiveNews() {
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			List<WebElement> li = commonLibrary.isExistList(news, UIMAP_SearchResult.lnkArticle, 10);
			if (li.size() == 5) {
				report.updateTestLog("Verify recent 5 News articles displays in theNews pod", "Recent 5 News articles are displayed in theNews pod", Status.PASS);
			} else {
				report.updateTestLog("Verify recent 5 News articles displays in theNews pod", "Recent 5 News articles are not displayed in theNews pod", Status.FAIL);
			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date format in news pod
	// # Function Name : ClickPoweredBy
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyDateFormat()// Feb 10, 2015 05:12:07 p.m. EST
	{
		boolean blnFlag = false;
		String strSource = null;
		try {
			WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
			WebElement newsDiv = commonLibrary.isExistNegative(news, UIMAP_Home.eltPodFooter2, 10);
			if (news != null) {
				// List<WebElement> li = commonLibrary.isExist_List(news,
				// UIMAP_SearchResult.lstTagName,10);
				// for(WebElement item:li)
				// {
				List<WebElement> date = commonLibrary.isExistList(newsDiv, UIMAP_Home.spantag, 10);
				{
					for (WebElement item1 : date) {
						strSource = item1.getText().replace(".", "");
						Date dates = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").parse(strSource);
						if (dates != null) {
							blnFlag = true;
						} else {
							blnFlag = false;
							break;
						}
					}
				}
				// }
			}
			if (blnFlag) {
				report.updateTestLog("Verify time zone is displayed below the article in the expected format", "Time zone is displayed below the article in the expected format", Status.PASS);
			} else {
				report.updateTestLog("Verify time zone is displayed below the article in the expected format", "Time zone is not displayed below the article in the expected format", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to law window
	// # Function Name : SwitchToLaw
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Law switchToLaw() {

		if (commonLibrary.switchToWindow("law")) {
			report.updateTestLog("Verify LAW360 page opens in Secondarybrowser", "LAW360 page is opened in Secondarybrowser", Status.PASS);
		} else {
			report.updateTestLog("Verify LAW360 page opens in Secondarybrowser", "LAW360 page is not opened in Secondarybrowser", Status.FAIL);
		}

		return new Law(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying left side panels
	// # Function Name : VerifyLeftSidePanels
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyLeftSidePanels() {
		String strPanelNames = dataTable.getData("General_Data", "LeftSide_Panels");
		String[] arrPanelNames = strPanelNames.split(";");
		WebElement eltLeftSidePods = commonLibrary.isExistNegative(UIMAP_Home.eltLeftSidePods1, 10);
		for (int i = 0; i < arrPanelNames.length; i++) {
			switch (arrPanelNames[i]) {
			case "History": {
				if (commonLibrary.isExistNegative(eltLeftSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassHistory + "']"), 10) != null)
					report.updateTestLog("Verify History Panel is displayed in the left side", "History Panel is displayed in the left side", Status.PASS);
				else
					report.updateTestLog("Verify History Panel is displayed in the left side", "History Panel is not displayed in the left side", Status.FAIL);
				break;

			}

			case "Folders": {
				if (commonLibrary.isExistNegative(eltLeftSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassFolders + "']"), 10) != null)
					report.updateTestLog("Verify Folders Panel is displayed in the left side", "Folders Panel is displayed in the left side", Status.PASS);
				else
					report.updateTestLog("Verify Folders Panel is displayed in the left side", "Folders Panel is not displayed in the left side", Status.FAIL);
				break;
			}

			case "News": {
				if (commonLibrary.isExistNegative(eltLeftSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassNewspod + "']"), 10) != null)
					report.updateTestLog("Verify News Panel is displayed in the left side", "News Panel is displayed in the left side", Status.PASS);
				else
					report.updateTestLog("Verify News Panel is displayed in the left side", "News Panel is not displayed in the left side", Status.FAIL);
				break;
			}

			case "Latest Updates": {

				commonLibrary.scrollDown(1000);
				if (commonLibrary.isExistNegative(eltLeftSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassLatestupdates + "']"), 10) != null)
					report.updateTestLog("Verify Latest Updates Panel is displayed in the left side", "Latest Updates Panel is displayed in the left side", Status.PASS);
				else
					report.updateTestLog("Verify Latest Updates Panel is displayed in the left side", "Latest Updates Panel is not displayed in the left side", Status.FAIL);
				break;
			}

			}
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying right side panels
	// # Function Name : VerifyRightSidePanels
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyRightSidePanels() {
		String strPanelNames = dataTable.getData("General_Data", "RightSide_Panels");
		WebElement eltRightSidePods = commonLibrary.isExistNegative(UIMAP_Home.eltRightSidePods1, 10);
		String[] arrPanelNames = strPanelNames.split(";");
		for (int i = 0; i < arrPanelNames.length; i++) {
			switch (arrPanelNames[i]) {
			case "Favourites": {
				if (commonLibrary.isExistNegative(eltRightSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassFavorites + "']"), 10) != null)
					report.updateTestLog("Verify Favourites Panel is displayed in the right side", "Favourites Panel is displayed in the right side", Status.PASS);
				else
					report.updateTestLog("Verify Favourites Panel is displayed in the right side", "Favourites Panel is not displayed in the right side", Status.FAIL);
				break;
			}
			case "Alerts": {
				if (commonLibrary.isExistNegative(eltRightSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassAlert + "']"), 10) != null)
					report.updateTestLog("Verify Alerts Panel is displayed in the right side", "Alerts Panel is displayed in the right side", Status.PASS);
				else
					report.updateTestLog("Verify Alerts Panel is displayed in the right side", "Alerts Panel is not displayed in the right side", Status.FAIL);
				break;
			}
			case "Notifications": {
				if (commonLibrary.isExistNegative(eltRightSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassnotification + "']"), 10) != null)
					report.updateTestLog("Verify Notifications Panel is displayed", "Notifications Panel is displayed", Status.PASS);
				else
					report.updateTestLog("Verify Notifications Panel is displayed", "Notifications Panel is not displayed", Status.FAIL);
				break;
			}
			case "Support": {

				commonLibrary.scrollDown(1000);
				if (commonLibrary.isExistNegative(eltRightSidePods, By.cssSelector("div[class*='" + UIMAP_Home.podClassSupport + "']"), 10) != null)
					report.updateTestLog("Verify Support Panel is displayed in the right side", "Support Panel is displayed in the right side", Status.PASS);
				else
					report.updateTestLog("Verify Support Panel is displayed in the right side", "Support Panel is not displayed in the right side", Status.FAIL);
				break;
			}
			}
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying history pod footer links
	// # Function Name : VerifyHistoryFooterLink
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyHistoryFooterLink(String strLink) {
		WebElement eltHistoryPod = commonLibrary.isExist(By.cssSelector("div[class*='" + UIMAP_Home.podClassHistory + "']"), 10);
		WebElement eltHistoryFooter = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.eltPodFooter, 10);
		List<WebElement> lnkFooterLinks = commonLibrary.isExistList(eltHistoryFooter, UIMAP_Home.lnkLinks, 10);

		if (strLink.contains("View all history")) {
			if (lnkFooterLinks.get(0) != null && lnkFooterLinks.get(0).getText().contains(strLink))

			{

				report.updateTestLog("Verify View all History appears on the left side' ", "View all History appears on the left side", Status.PASS);
				report.updateTestLog("Verify View all History is at the bottom of the pod ", "View all History is at the bottom of the pod ", Status.PASS);
				report.updateTestLog("Verify View all History is a link ", "View all History is a link", Status.PASS);
			} else {
				report.updateTestLog("Verify View all History appears on the left side' ", "View all History does not appear on the left side", Status.FAIL);
				report.updateTestLog("Verify View all History is at the bottom of the pod ", "View all History is not at the bottom of the pod ", Status.FAIL);
				report.updateTestLog("Verify View all History is a link ", "View all History is not a link", Status.FAIL);

			}

		} else {
			if (lnkFooterLinks.get(1) != null && lnkFooterLinks.get(1).getText().contains(strLink)) {
				report.updateTestLog("Verify Research Map appears on the right side' ", "Research Map appears on the right side", Status.PASS);
				report.updateTestLog("Verify Research Map is at the bottom of the pod ", "Research Map is at the bottom of the pod ", Status.PASS);
				report.updateTestLog("Verify Research Map is a link ", "Research Map is a link", Status.PASS);
			} else {
				report.updateTestLog("Verify Research Map appears on the right side' ", "Research Map does not appear on the right side", Status.FAIL);
				report.updateTestLog("Verify Research Map is at the bottom of the pod ", "Research Map is not at the bottom of the pod ", Status.FAIL);
				report.updateTestLog("Verify Research Map is a link ", "Research Map is not a link", Status.FAIL);

			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of history pod
	// # Function Name : VerifyHistoryPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyHistoryPodContent() {

		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnExpandCollapse4, 10);
		if (eltHistoryPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse, "Expand/Collapse");
			eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
			btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnExpandCollapse, 10);

		}
		if (btnExpandCollapse != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is located attop left hand corner ", "Ability to collopse/exand pod is located attop left hand corner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is located attop left hand corner ", "Ability to collopse/exand pod is not present", Status.FAIL);

		WebElement eltHistoryPodContent = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPodContent, 10);
		List<WebElement> lnkHistoryPod = commonLibrary.isExistList(eltHistoryPodContent, UIMAP_Home.lnkLinks, 10);
		if (lnkHistoryPod.size() > 0 && lnkHistoryPod.size() <= 5) {
			report.updateTestLog("Verify there are 1 to 5 history items listed' ", "there are " + lnkHistoryPod.size() + " history items listed", Status.PASS);
			report.updateTestLog("Verify Each item is a link' ", "Each item is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify there are 1 to 5 history items listed' ", "there are " + lnkHistoryPod.size() + " history items listed", Status.FAIL);
			report.updateTestLog("Verify Each item is a link' ", "Each item is not a link", Status.FAIL);
		}
		WebElement eltHistoryIcon = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.eltPodIcon, 10);
		if (eltHistoryIcon != null)
			report.updateTestLog("Verify Search history icon is present to the left of the Search History title", "Search history icon is present to the left ofthe Search History title", Status.PASS);
		else
			report.updateTestLog("Verify Search history icon is present to the left of the Search History title", "Search history icon is not present to the left ofthe Search History title", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of favorites pod
	// # Function Name : VerifyFavoritesPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyFavoritesPodContent() {

		WebElement eltFavoritesPod = commonLibrary.isExistNegative(UIMAP_Home.eltFavoritesPod, 10);
		WebElement btnExpandCollapse1 = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.btnExpandCollapse1, 10);
		if (eltFavoritesPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse1, "Expand/Collapse");
			eltFavoritesPod = commonLibrary.isExistNegative(UIMAP_Home.eltFavoritesPod, 10);
			btnExpandCollapse1 = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.btnExpandCollapse1, 10);

		}
		if (btnExpandCollapse1 != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is located at top left handcorner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is not located at top left handcorner", Status.FAIL);

		WebElement eltFavoritesPodContent = commonLibrary.isExistNegative(btnExpandCollapse1, UIMAP_Home.eltFavoritesPodContent, 10);
		List<WebElement> lnkFavorritesLink = commonLibrary.isExistList(eltFavoritesPodContent, UIMAP_Home.lnkLinks, 10);
		int i, count = 0;
		for (i = 0; i < lnkFavorritesLink.size(); i++) {
			if (lnkFavorritesLink.get(i).isDisplayed())
				count++;
		}
		if (count > 0 && count <= 5) {
			report.updateTestLog("Verify there are 1 to 5 favorites listed' ", "there are " + count + " favorites listed", Status.PASS);
			report.updateTestLog("Verify Each item is a link' ", "Each item is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify there are 1 to 5 favorites listed' ", "there are " + count + " favorites listed", Status.FAIL);
			report.updateTestLog("Verify Each item is a link' ", "Each item is not a link", Status.FAIL);
		}

		WebElement eltFavFooter = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.eltPodFooter, 10);
		WebElement lnkFavFooter = commonLibrary.isExistNegative(eltFavFooter, UIMAP_Home.lnkLinks, 10);
		if (lnkFavFooter != null && lnkFavFooter.getText().contains("More"))
			report.updateTestLog("Verify More link displays at the bottom of pod ", "More link displays at the bottom of pod", Status.PASS);
		else
			report.updateTestLog("Verify More link displays at the bottom of pod ", "More link does not display at the bottom of pod", Status.FAIL);

		WebElement eltPodIcon = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.eltPodIcon, 10);
		if (eltPodIcon != null)
			report.updateTestLog("Verify Favorites icon is present to the left of the Favorites  title", "Favorites icon is present to the left of the Favorites title", Status.PASS);
		else
			report.updateTestLog("Verify Favorites icon is present to the left of the Favorites  title", "Favorites icon is not present to the left of the Favorites title", Status.FAIL);

		WebElement eltPodHeader = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.eltPodHeader, 10);
		WebElement lnkHeaderLink = commonLibrary.isExistNegative(eltPodHeader, UIMAP_Home.lnkLinks, 10);
		if (lnkHeaderLink.getText().contains("Tips"))
			report.updateTestLog("Verify 'Tips' link displays at the top right corner in 'Favorites' pod", "'Tips' link displays at the top right corner in 'Favorites' pod", Status.PASS);
		else
			report.updateTestLog("Verify 'Tips' link displays at the top right corner in 'Favorites' pod", "'Tips' link displays at the top right corner in 'Favorites' pod", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of folders pod
	// # Function Name : verifyFoldersPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyFoldersPodContent() {
		WebElement eltFoldersPod = commonLibrary.isExistNegative(UIMAP_Home.eltFoldersPod, 10);
		WebElement btnExpandCollapse1 = commonLibrary.isExistNegative(eltFoldersPod, UIMAP_Home.btnExpandCollapse2, 10);
		if (eltFoldersPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse1, "Expand/Collapse");
			eltFoldersPod = commonLibrary.isExistNegative(UIMAP_Home.eltFoldersPod, 10);
			btnExpandCollapse1 = commonLibrary.isExistNegative(eltFoldersPod, UIMAP_Home.btnExpandCollapse2, 10);

		}
		if (btnExpandCollapse1 != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is located at top left handcorner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is not located at top left handcorner", Status.FAIL);

		WebElement eltFolderFooter = commonLibrary.isExistNegative(btnExpandCollapse1, UIMAP_Home.eltPodFooter, 10);
		WebElement lnkFolderFooter = commonLibrary.isExistNegative(eltFolderFooter, UIMAP_Home.lnkLinks, 10);
		if (lnkFolderFooter != null && lnkFolderFooter.getText().contains("View all folders")) {
			report.updateTestLog("Verify View all Folders appears at the bottom of the pod", "View all Folders appears at the bottom of the pod", Status.PASS);
			report.updateTestLog("Verify View all Folders is a link", "View all Folders is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify View all Folders appears at the bottom of the pod", "View all Folders does not appear at the bottom of the pod", Status.FAIL);
			report.updateTestLog("Verify View all Folders is a link", "View all Folders is not a link", Status.FAIL);
		}

		WebElement eltFoldersPodContent = commonLibrary.isExistNegative(btnExpandCollapse1, UIMAP_Home.eltFavoritesPodContent, 10);
		List<WebElement> lnkFoldersPodContent = commonLibrary.isExistList(eltFoldersPodContent, UIMAP_Home.lnkLinks, 10);

		int i, count = 0;
		for (i = 0; i < lnkFoldersPodContent.size(); i++) {
			if (lnkFoldersPodContent.get(i).isDisplayed()) {
				// commonLibrary.highlightElement(lnkFoldersPodContent.get(i));
				count++;
			}
		}
		if (count > 0 && count <= 5) {
			report.updateTestLog("Verify there are 1 to 5 folders listed' ", "there are " + lnkFoldersPodContent.size() + " folders listed", Status.PASS);
			report.updateTestLog("Verify Each item is a link' ", "Each item is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify there are 1 to 5 folders listed' ", "there are " + lnkFoldersPodContent.size() + " folders listed", Status.FAIL);
			report.updateTestLog("Verify Each item is a link' ", "Each item is not a link", Status.FAIL);
		}

		WebElement eltPodIcon = commonLibrary.isExistNegative(eltFoldersPod, UIMAP_Home.eltPodIcon, 10);
		if (eltPodIcon != null)
			report.updateTestLog("Verify Favorites icon is present to the left of the Folders  title", "Favorites icon is present to the left of the Folders title", Status.PASS);
		else
			report.updateTestLog("Verify Favorites icon is present to the left of the Folders  title", "Favorites icon is not present to the left of the Folders title", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on feed back from more
	// # Function Name : clickOnMoreFeedBack
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback clickOnMoreFeedBack() {
		// to click on More

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 100);
		if (lnkFeedBack == null || !lnkFeedBack.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				if ((browsername.contains("internet")))
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 100);
		}
		// click on feed back
		if (lnkFeedBack != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickLinkWithWebElementWithWait(lnkFeedBack, "Feedback");
			} else
				commonLibrary.clickLinkWithWebElementWithWait(lnkFeedBack, "Feedback");
		}
		commonLibrary.switchToWindow("feedback");
		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to folders option under
	// more
	// # Function Name : Navigation_From_MoreButton
	// # Author : Vidhya
	// # Date Created : 10th Apr
	// #*****************************************************************************************************************************

	public WorkFolders navigationFromMoreButton(String Link) {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");

			switch (Link) {
			case "Folders": {
				// WebElement Folder =
				// commonLibrary.isExist(UIMAP_HomePage.btnFolders);
				WebElement Folder = commonLibrary.isExist(By.cssSelector("a[data-action='workfolders']"));

				if (Folder != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(Folder, "Folders");
					else
						commonLibrary.clickLinkWithWebElementWithWait(Folder, "Folders");
					commonLibrary.sleep(8000);
				}
				break;
			}

			}

			return new WorkFolders(scriptHelper);
		} catch (Exception e) {

		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify <View all news> link displays
	// at the bottom of the pod
	// # Function Name : VerifyViewAllNewsLink
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyViewAllNewsLink() {
		boolean blnFlag = true;
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			List<WebElement> li = commonLibrary.isExistList(news, UIMAP_SearchResult.lnkHeader, 10);
			for (WebElement item : li) {
				if (item.getText().equalsIgnoreCase("View all news")) {
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag) {

			report.updateTestLog("verify View all news link displays at the bottom of the pod", "View all news link is displayed at the bottom of the pod", Status.PASS);

		} else {

			report.updateTestLog("verify View all news link displays at the bottom of the pod", "View all news link is not displayed at the bottom of the pod", Status.FAIL);

		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify <View all news> link displays
	// at the bottom of the pod
	// # Function Name : ClickArticleAndVerifyInSecondaryBrowser
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home clickArticleAndVerifyInSecondaryBrowser() {
		String strLink = "";

		boolean blnFlag = true;
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			List<WebElement> li = commonLibrary.isExistList(news, UIMAP_SearchResult.lnkArticle, 10);
			for (WebElement item : li) {
				strLink = item.getText();
				commonLibrary.clickJS(item, strLink);
				if (commonLibrary.switchToWindow("law")) {
					List<WebElement> title = commonLibrary.isExistList(UIMAP_Home.hdrTitle, 10);
					for (WebElement item1 : title) {
						if (item1.getText().equalsIgnoreCase(strLink)) {
							blnFlag = true;
							// report.updateTestLog("Click on article "+strLink+"",
							// ""+strLink+" is clicked", Status.PASS);
							driver.close();
							commonLibrary.switchToWindow("firsttime");
							report.updateTestLog("Close secondary Browser", "Secondary browser is closed", Status.PASS);
							report.updateTestLog("Verify secondary browser is closed", "secondary browser is closed", Status.PASS);
						} else {
							blnFlag = false;
							report.updateTestLog("Click on article " + strLink + "", "" + strLink + " is not clicked", Status.FAIL);
							break;
						}
					}
					if (!blnFlag)
						break;
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on each article", "Each article is not clicked", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Global UI Builder
	// home page by clicking Lexis Advance arrow
	// # Function Name : NavigateToGUIBPage
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBHome navigateToGUIBPage() {
		generalFunctions.productSwitcher("Global UI Builder");
		return new GUIBHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of News pod
	// # Function Name : verifyNewsPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyNewsPodContent() {
		WebElement eltNewsPod = commonLibrary.isExistNegative(UIMAP_Home.eltNewsPod, 10);
		WebElement eltNewsHeader = commonLibrary.isExistNegative(eltNewsPod, UIMAP_Home.eltPodHeader, 10);
		WebElement eltNewsExpandCollapse = commonLibrary.isExistNegative(eltNewsPod, UIMAP_Home.btnExpandCollapse4, 10);
		if (eltNewsExpandCollapse != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is located at top left handcorner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is not located at top left handcorner", Status.FAIL);

		WebElement eltPodFooter = commonLibrary.isExistNegative(eltNewsPod, UIMAP_Home.eltPodFooter2, 10);

		WebElement lnkPodFooter = commonLibrary.isExistNegative(eltNewsPod, UIMAP_Home.viewAllNews, 10);
		if (lnkPodFooter.getText().contains("View all news")) {
			report.updateTestLog("Verify View all News appears at the bottom of the pod and it is a link", "View all News appears at the bottom of the pod and it is a link", Status.PASS);
		} else
			report.updateTestLog("Verify View all News appears at the bottom of the pod and it is a link", "View all News does not appear at the bottom of the pod and it is a link", Status.FAIL);
		WebElement lnkFooterExternal = commonLibrary.isExistNegative(eltNewsPod, UIMAP_Home.eltNewsExternalIcon, 10);
		if (lnkFooterExternal != null)
			report.updateTestLog("Verify 'External citemap' icon displays next to 'View All News' link in 'News' pod", "'External citemap' icon displays next to 'View All News' link in 'News' pod", Status.PASS);
		else
			report.updateTestLog("Verify 'External citemap' icon displays next to 'View All News' link in 'News' pod", "'External citemap' icon does not display next to 'View All News' link in 'News' pod", Status.FAIL);

		WebElement lstNewsPodContent = commonLibrary.isExistNegative(eltPodFooter, UIMAP_Home.lstTagUList, 10);
		List<WebElement> eltNews = commonLibrary.isExistList(lstNewsPodContent, UIMAP_Home.lstTagListItems, 10);
		int i, count = 0;
		boolean blnFlag = true;
		for (i = 0; i < eltNews.size(); i++) {
			WebElement lnkNews = commonLibrary.isExistNegative(eltNews.get(i), UIMAP_Home.newsLink, 10);
			WebElement lnkExternal = commonLibrary.isExistNegative(eltNews.get(i), UIMAP_Home.eltNewsExternalIcon, 10);
			if (lnkNews != null && lnkExternal != null)
				count++;
			else {
				blnFlag = false;
				break;
			}
			if (count >= 5) {
				// blnFlag=false;
				break;
			}
		}
		if (blnFlag) {
			if (count > 0 && count <= 5) {
				report.updateTestLog("Verify there are 1 to 5 news items listed and they are links", "There are 1 to 5 news items listed and they are links", Status.PASS);
				report.updateTestLog("Verify 'External citemap' icon displays next to each 'News' link", "'External citemap' icon displays next to each 'News' link", Status.PASS);
			} else {
				report.updateTestLog("Verify there are 1 to 5 news items listed and they are links", "1 to 5 news items not  listed", Status.FAIL);
				report.updateTestLog("Verify 'External citemap' icon displays next to each 'News' link", "'External citemap' icon does not display next to each 'News' link", Status.PASS);
			}
		} else {
			report.updateTestLog("Verify there are 1 to 5 news items listed and they are links", "1 to 5 news items not  listed", Status.PASS);
			report.updateTestLog("Verify 'External citemap' icon displays next to each 'News' link", "'External citemap' icon does not display next to" + count + "  'News' link", Status.FAIL);
		}
		this.verifyDateFormat();

		WebElement eltNewsIcon = commonLibrary.isExistNegative(eltNewsHeader, UIMAP_Home.eltPodIcon, 10);
		if (eltNewsIcon != null) {
			report.updateTestLog("Verify News icon is present to the left of the News title", "News icon is present to the left of the News title", Status.PASS);
		} else
			report.updateTestLog("Verify News icon is present to the left of the News title", "News icon is not present to the left of the News title", Status.FAIL);

		WebElement lnkLaw360 = commonLibrary.isExistNegative(eltNewsHeader, UIMAP_Home.lnkLaw360, 10);
		WebElement imgLaw360 = commonLibrary.isExistNegative(eltNewsHeader, UIMAP_Home.imgLaw360, 10);
		if (lnkLaw360 != null && lnkLaw360.getText().contains("Powered by:") && imgLaw360 != null)
			report.updateTestLog("Verify Powered by 360 appears at the top right of the pod andit is a link", "Powered by 360 appears at the top right of the pod andit is a link", Status.PASS);
		else
			report.updateTestLog("Verify Powered by 360 appears at the top right of the pod andit is a link", "Powered by 360 does not appear at the top right of the pod", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of Alerts pod
	// # Function Name : verifyAlertsPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyAlertsPodContent() {
		WebElement eltAlertsPod = commonLibrary.isExistNegative(UIMAP_Home.eltAlertsPod, 10);
		WebElement btnExpandCollapse = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.btnExpandCollapse4, 10);
		if (eltAlertsPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse, "Expand/Collapse");
			eltAlertsPod = commonLibrary.isExistNegative(UIMAP_Home.eltAlertsPod, 10);
			btnExpandCollapse = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.btnExpandCollapse, 10);

		}

		if (btnExpandCollapse != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is locatedat top left hand corner", "Ability to collopse/exand pod is locatedat top left hand corner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is locatedat top left hand corner", "Ability to collopse/exand pod is locatedat top left hand corner", Status.FAIL);

		WebElement eltAlertsFooter = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.eltPodFooter1, 10);
		WebElement lnkAlertsFooter = commonLibrary.isExistNegative(eltAlertsFooter, UIMAP_Home.lnkLinks, 10);

		if (lnkAlertsFooter.getText().contains("View all alerts")) {
			report.updateTestLog("Verify View all Alerts appears at the bottom ofthe pod", "View all Alerts appears at the bottom ofthe pod", Status.PASS);
			report.updateTestLog("Verify View all Alerts is a link", "View all Alerts is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify View all Alerts appears at the bottom ofthe pod", "View all Alerts does not appear at the bottom ofthe pod", Status.FAIL);
			report.updateTestLog("Verify View all Alerts is a link", "View all Alerts is not a link", Status.FAIL);
		}

		WebElement eltAlertsPodContent = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.eltAlertsPodContent, 10);
		List<WebElement> lnkAlertsPodContent = commonLibrary.isExistList(eltAlertsPodContent, UIMAP_Home.lnkLinks, 10);
		if (lnkAlertsPodContent.size() > 0 && lnkAlertsPodContent.size() <= 5) {
			report.updateTestLog("Verify there are 1 to 5 alerts listed' ", "there are " + lnkAlertsPodContent.size() + " alerts items listed", Status.PASS);
			report.updateTestLog("Verify Each item is a link' ", "Each item is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify there are 1 to 5 alerts listed' ", "there are " + lnkAlertsPodContent.size() + " alerts items listed", Status.FAIL);
			report.updateTestLog("Verify Each item is a link' ", "Each item is not a link", Status.FAIL);
		}

		WebElement eltAlertsHeader = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.eltPodHeader, 10);
		WebElement eltAlertIcon = commonLibrary.isExistNegative(eltAlertsHeader, UIMAP_Home.eltPodIcon, 10);
		if (eltAlertIcon != null)
			report.updateTestLog("Verify Alert icon is present to the left ofthe Alert title", "Alert icon is present to the left ofthe Alert title", Status.PASS);
		else
			report.updateTestLog("Verify Alert icon is present to the left ofthe Alert title", "Alert icon is not present to the left ofthe Alert title", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
		// # Function Description : Function for verifying content of Latest Updates
		// pod
		// # Function Name : verifyLatestUpdatesPodContent
		// # Author : Pratik
		// # Date Created : Feb'15
		// #*****************************************************************************************************************************

		public Home verifyLatestUpdatesPodContent() {
			WebElement eltLatestUpdatesPod = commonLibrary.isExistNegative(UIMAP_Home.eltLatestUpdatesPod, 10);
			WebElement latestUpdateText = commonLibrary.isExistNegative(eltLatestUpdatesPod, UIMAP_Home.eltFavoritesList, 10);
			String strLatestUpdateContent = "Learn more about our newest changes to Lexis Advance®, including more content, search enhancements, and expanded delivery options.";
			if (latestUpdateText.getText().contains(strLatestUpdateContent))
				report.updateTestLog("Verify Text: 'Learn more about our newest changes to Lexis Advance®, including" + " more content, search enhancements, and expanded delivery options' displays inside pod", "Text: 'Learn more about our newest changes to Lexis Advance®, including" + " more content, search enhancements, and expanded delivery options' displays inside pod", Status.PASS);
			else
				report.updateTestLog("Verify Text: 'Learn more about our newest changes to Lexis Advance®, including" + " more content, search enhancements, and expanded delivery options' displays inside pod", "Text: 'Learn more about our newest changes to Lexis Advance®, including" + " more content, search enhancements, and expanded delivery options' does not display inside pod", Status.FAIL);
			WebElement eltLatestUpdatesFooter = commonLibrary.isExistNegative(latestUpdateText, UIMAP_Home.latestRelease, 10);
			WebElement lnkLatestUpdatesPod = commonLibrary.isExistNegative(latestUpdateText, UIMAP_Home.lnkLinks, 10);
			String strLink = "Learn more about our latest release";
			if (lnkLatestUpdatesPod.getText().contains(strLink))
				report.updateTestLog("Verify 'Learn more about our latest release.' is link and it displays below above text", "'Learn more about our latest release.' is link and it displays below above text", Status.PASS);
			else
				report.updateTestLog("Verify 'Learn more about our latest release.' is link and it displays below above text", "'Learn more about our latest release.' does not display", Status.FAIL);

			return new Home(scriptHelper);
		}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying content of Support pod
	// # Function Name : verifySupportPodContent
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifySupportPodContent() {
		boolean blnFlag1 = false, blnFlag2 = false, blnFlag3 = false;
		WebElement eltSupportPod = commonLibrary.isExistNegative(UIMAP_Home.eltSupportPod, 10);
		// WebElement eltSupportPodContent =
		// commonLibrary.isExist(eltSupportPod,UIMAP_Home.btnExpandCollapse,
		// 10);
		List<WebElement> eltSupportPodContentList = commonLibrary.isExistList(eltSupportPod, UIMAP_Home.header3, 10);
		// List<WebElement> eltSupportListItems =
		// commonLibrary.isExist_List(eltSupportPodContentList,
		// UIMAP_Home.lstTagListItems,10);
		for (WebElement li : eltSupportPodContentList) {
			if (li.getText().contains("Access Lexis Advance® Help"))
				blnFlag1 = true;
			else if (li.getText().contains("Tutorials"))
				blnFlag2 = true;
			else if (li.getText().contains("Contact Customer Support"))
				blnFlag3 = true;
			if (blnFlag1 && blnFlag2 && blnFlag3)
				break;
		}
		if (blnFlag1)
			report.updateTestLog("Support panel displays with following sections:" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Access Lexis Advance® Help Section Exists", Status.PASS);
		else
			report.updateTestLog("Support panel displays with following sections:" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Access Lexis Advance® Help Section does not Exists", Status.FAIL);
		if (blnFlag2)
			report.updateTestLog("Support panel displays with following sections:\n" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Tutorials Section Exists", Status.PASS);
		else
			report.updateTestLog("Support panel displays with following sections:" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Tutorials Section does not Exist", Status.FAIL);
		WebElement eltSupportPodContactInfo = commonLibrary.isExistNegative(eltSupportPod, UIMAP_Home.eltSupportPodContactInfo, 10);
		if (blnFlag3)
			report.updateTestLog("Support panel displays with following sections:" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Contact Customer Support Section Exists", Status.PASS);
		else
			report.updateTestLog("Support panel displays with following sections:" + "1. Access Lexis Advance® Help" + "2. Tutorials" + "3. Contact Customer Support", "Contact Customer Support Section does not Exists", Status.FAIL);
		if (blnFlag3 && eltSupportPodContactInfo.getText().contains("Phone: 1-800-543-6862"))
			report.updateTestLog("Contact Customer Support section displayswith the following labels:" + "Contact Customer Support" + "Phone: 1-800-543-6862", "Required Labels are Present.", Status.PASS);
		else
			report.updateTestLog("Contact Customer Support section displayswith the following labels:" + "Contact Customer Support" + "Phone: 1-800-543-6862", "Required Labels are not Present.", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on filter button
	// # Function Name : clickFilterButton
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home clickFilterButton() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select all filters for US Federal or
	// all states and territories
	// # Function Name : applyFilterUSFederalOrAllStates
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home applyFilterUSFederalOrAllStates(String strFilter) {
		String[] arrstrFilter = strFilter.split(";");
		WebElement button = null;
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}
		filterMenuSelection("Jurisdiction");
		for (int m = 0; m < arrstrFilter.length; m++) {
			switch (arrstrFilter[m]) {
			case "U.S. Federal": {
				button = commonLibrary.isExistNegative(UIMAP_Home.lnkSelectAllUSFederal, 10);
				break;
			}
			case "All States": {
				button = commonLibrary.isExistNegative(UIMAP_Home.lnkSelectAllStates, 10);
				break;
			}
			}
			commonLibrary.clickLinkWithWebElementWithWait(button, arrstrFilter[m]);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close filter pop up.
	// # Function Name : closePreFilterPopUp
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home closePreFilterPopUp() {
		generalFunctions.closePreFilterPopUp();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of pages to be displayed
	// # Function Name : SetNumberOfResultsToBeDisplayed     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Search setNumberOfResultsToBeDisplayed(String number) {
		generalFunctions.setNumberOfResultsToBeDisplayed(number);

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of pages to be displayed
	// # Function Name : SetNumberOfResultsToBeDisplayed     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Home setNumberOfResultsToBeDisplayedHome(String number) {
		generalFunctions.setNumberOfResultsToBeDisplayed(number);

		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check include federal content
	// checkbox
	// # Function Name : selectRelatedFederalContent     
	// # Author : Pratik
	// # Date Created : Jan'15
	// #**********************************************

	public Home selectRelatedFederalContent() {
		WebElement chkRelatedFederal = commonLibrary.isExistNegative(UIMAP_Home.chkRelatedFederal, 10);
		if (chkRelatedFederal != null) {
			commonLibrary.setCheckBox(chkRelatedFederal, true);
			report.updateTestLog("Select Include Related federal.", "Include Related federal is selected.", Status.DONE);
		} else
			report.updateTestLog("Select Include Related federal.", "Include Related federal is not selected.", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the links under Lexis
	// Advance Menu
	// # Function Name : NavigateToLexisAdvanceResearchLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search navigateToLexisAdvanceResearchLink() {

		String defaultProductSwitcher = localizeProperties.getProperty("defaultProductSwitcher");

		WebElement btnPS = commonLibrary.isExist(By.cssSelector("a[data-action='" + defaultProductSwitcher + "']"), 20);
		if (btnPS != null)
			commonLibrary.clickLinkWithWebElement(btnPS, btnPS.getText());
		else
			report.updateTestLog("Select Lexis Advance Research Link", " Lexis Advance Research Link is not available", Status.FAIL);

		return new Search(scriptHelper);

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
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : NavigateToAll
	// # Author : Pratik
	// # Date Created : Feb'15
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
		commonLibrary.sleep(5000);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in the word wheel.
	// # Function Name : verifyTextInWordWheel
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyTextInWordWheel(String text, String term) {

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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter text in Search text box.
	// # Function Name : setSearchTerm
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home setSearchTerm(String searchTerm) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);

		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		commonLibrary.sleep(3000);// mandatory

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the search button
	// # Function Name : clickSearchButton
	// # Author : Pratik
	// # Date Created : Feb'20
	// #*************************************************************************
	public Search clickSearchButton() {
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the search button
	// # Function Name : clickSearchButton
	// # Author : Pratik
	// # Date Created : Feb'20
	// #*************************************************************************
	public LPAResults clickSearchButton1() {
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Alerts page
	// # Function Name : navigateToAlerts
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Alerts navigationFromMoreButtonToAlerts() {
		generalFunctions.navigateToAlerts();
		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search With Pre filter
	// # Function Name : searchWithPrefilter
	// # Author : Seeetha
	// # Date Created : 23-Feb'15
	// #*****************************************************************************************************************************

	public Search searchWithPrefilter(String catfltr, String patfltr, String jurifltr, String state, String srchterm, String startin) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, srchterm, "SearchTerm");
		String[] catfltrs = catfltr.split(";");
		String[] jurifltrs = jurifltr.split(";");
		String[] patfltrs = patfltr.split(";");
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}
		if (catfltr != "") {
			boolean blnFlag = false;
			filterMenuSelection("Category");
			blnFlag = false;
			for (int i = 0; i < catfltrs.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_Home.divClassCTFilters, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().toLowerCase().contains(catfltrs[i])) {
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, true);
								blnFlag = true;
								break;
							}

						}
				}

			}
			if (blnFlag) {
				report.updateTestLog("select check boxes near " + catfltr, "select check boxes near " + catfltr, Status.PASS);
			} else {
				report.updateTestLog("select check boxes near " + catfltr, "Not select check boxes near " + catfltr, Status.FAIL);
			}
		}
		if (jurifltr != "") {
			boolean blnFlag = false;
			filterMenuSelection("Jurisdiction");
			// By Court
			for (int i = 0; i < jurifltrs.length; i++) {
				WebElement divClassFederalFilters = commonLibrary.isExist(UIMAP_Home.divClassFederalFilters, 20);
				if (divClassFederalFilters != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassFederalFilters, UIMAP_Home.lstTagUList, 20);

					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().toLowerCase().contains(jurifltrs[i])) {
								blnFlag = true;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, true);
								break;
							}

						}
				}

			}
			if (!blnFlag) {
				// By circuit
				for (int i = 0; i < jurifltrs.length; i++) {
					WebElement divClassFederalFilters = commonLibrary.isExist(UIMAP_Home.divClassFederalFilters, 20);
					if (divClassFederalFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassFederalFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(jurifltrs[i])) {
									blnFlag = true;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									break;
								}

							}
					}

				}
			}
			if (!blnFlag) {
				// State Filters 1st Column
				for (int i = 0; i < jurifltrs.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(jurifltrs[i])) {
									blnFlag = true;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									break;
								}

							}
					}

				}
			}
			if (!blnFlag) {
				// State Filters 2nd Column
				for (int i = 0; i < jurifltrs.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(jurifltrs[i])) {
									blnFlag = true;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									break;
								}

							}
					}

				}
			}
			if (!blnFlag) {
				// State Filters 3rd Column
				for (int i = 0; i < jurifltrs.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(2), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(jurifltrs[i].toLowerCase())) {
									blnFlag = true;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									break;
								}

							}
					}

				}
			}
			if (!blnFlag) {
				commonLibrary.scrollDown();
				// State Filters Footer Links
				for (int i = 0; i < jurifltrs.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(3), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(jurifltrs[i])) {
									blnFlag = true;
									commonLibrary.scrollDown();
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									commonLibrary.scrollUp();
									break;
								}

							}
					}

				}
			}
			if (blnFlag) {
				report.updateTestLog("select check boxes near " + jurifltr, "select check boxes near " + jurifltr, Status.PASS);
			} else {
				report.updateTestLog("select check boxes near " + jurifltr, "Not select check boxes near " + jurifltr, Status.FAIL);
			}

		}
		if (patfltr != "") {
			boolean blnFlag = false;
			filterMenuSelection("Practice Area & Topic");

			// Column 1
			for (int i = 0; i < patfltrs.length; i++) {
				WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
				if (divClassPracticeArea != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().toLowerCase().contains(patfltrs[i].toLowerCase())) {
								blnFlag = true;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, true);
								break;
							}
						}

				}

			}

			// column 2
			if (!blnFlag) {
				for (int i = 0; i < patfltrs.length; i++) {
					WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
					if (divClassPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().toLowerCase().contains(patfltrs[i].toLowerCase())) {
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, true);
									blnFlag = true;
									break;
								}

							}

					}
				}
			}
			if (blnFlag) {
				report.updateTestLog("select check boxes near " + patfltr, "select check boxes near " + patfltr, Status.PASS);
			} else {
				report.updateTestLog("select check boxes near " + patfltr, "Not select check boxes near " + patfltr, Status.FAIL);
			}
		}
		/*
		 * if(startin!="") { FilterMenuSelection("Category"); WebElement element
		 * =commonLibrary.isExist(UIMAP_Home.startIn, 20);
		 * if(commonLibrary.SelectByVisibleText(element,startin)) {
		 * report.updateTestLog("Verify start in value is selected as "+startin,
		 * "start in value is selected as "+startin+"", Status.PASS); } else {
		 * report
		 * .updateTestLog("Verify start in value is selected as "+startin+"",
		 * "start in value is not selected as "+startin+"", Status.FAIL); } }
		 */
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		// commonLibrary.highlightElement(eltSearchbutton);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the links under Practice
	// Area
	// # Function Name : SelectPracticeAreaLink
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPAHome selectLPAPracticeAreaLink(String strLinkText, String strPageHeader) {
		try {

			WebElement btnSelectPracticeArea = commonLibrary.isExist(UIMAP_Home.btnSelectPracticeArea);
			commonLibrary.clickButton(btnSelectPracticeArea);

			if (strLinkText.equals("Banking & Finance")) {
				WebElement lnkBankingAndFinance = commonLibrary.isExist(UIMAP_Home.lnkBankingAndFinance);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(lnkBankingAndFinance, lnkBankingAndFinance.getText());
				} else {
					commonLibrary.clickButtonParentWithWait(lnkBankingAndFinance, lnkBankingAndFinance.getText());
				}

				commonLibrary.sleep(3000);
				WebElement txtPageHeading = commonLibrary.isExist(UIMAP_Home.txtPracticeAreaHeading);
				if (txtPageHeading.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
					report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
				else
					report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for set the new client Id
	// Area
	// # Function Name : setNewClientIDGeneric
	// # Author : seetha
	// # Date Created : 26-Feb'15
	// #*****************************************************************************************************************************

	public Research setNewClientIDGeneric(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_SearchResult.rdoClientId);
		if (rdoClientId != null) {
			report.updateTestLog("Selecting Set New Client ID", "New Client Option Selected.", Status.PASS);
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
	// # Function Description : Function for selecting the source from the text
	// box
	// Area
	// # Function Name : searchSource
	// # Author : Ram
	// # Date Created : May'15
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
				sourceSearch.clear();
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
	// # Function Description : Function to verify the Display of Client ID in
	// Global Navigation Menu
	// # Function Name : verifyClientIDGmenu
	// # Author : Ram Prasath
	// # Date Created : 26 Feb'15
	// #*****************************************************************************************************************************

	public Home verifyClientIDGmenu(String clientinfo) {
		try {
			WebElement clientname = null;
			int count = 0;
			do {
				commonLibrary.sleep(10000);
				clientname = commonLibrary.isExist(UIMAP_Home.clientName);
				count++;
			} while (clientname == null && count < 25);
			String clientid = clientname.getText();
			if (clientid.equals(clientinfo)) {
				report.updateTestLog("Verify Client Name in Global Menu", clientinfo + " Client Name is displayed in Global Menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Client Name in Global Menu", clientinfo + " Client Name is not displayed in Global Menu", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in Global Navigation
	// Menu from Home Page
	// # Function Name : SetClientIDGeneric_Home
	// # Author : Ram Prasath
	// # Date Created : 27 Feb'15
	// #*****************************************************************************************************************************

	public Home setNewClientIDGeneric_Home(String strClientID) {
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

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId, "Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
			}
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
			WebElement clientname = null;
			int count = 0;
			do {
				commonLibrary.sleep(10000);
				clientname = commonLibrary.isExist(UIMAP_Home.clientName);
				count++;
			} while (clientname == null && count < 25);

		} else
			report.updateTestLog("Clicking on Set Client ID", "Set Client ID can not be clicked.", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a menu from browse options
	// # Function Name : SelectMenuFromBrowse     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home selectMenuFromBrowse(String strOption) {
		boolean blnFlag = false;
		try {
			WebElement browse = commonLibrary.isExist(UIMAP_Home.btnBrowse);
			if (browse != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(browse, "browse");
				else
					commonLibrary.clickButtonParentWithWait(browse, "browse");
				commonLibrary.sleep(2000);
			}
			WebElement browseWindow = commonLibrary.isExist(UIMAP_Home.Winbrowse);
			List<WebElement> Options = commonLibrary.isExistList(browseWindow, UIMAP_Home.btnBrowseMenu, 10);
			for (int i = 0; i < Options.size(); i++) {
				if (Options.get(i).getText().trim().equalsIgnoreCase(strOption)) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(Options.get(i), "browse");
					else {
						Options.get(i).click();
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a menu from browse options
	// # Function Name : SelectMenuFromBrowse     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home verifyTwoPanesAndTheContentsInFirstPane(String values) {

		String[] arr = values.split(";");
		boolean count = false;
		boolean allsource = false;
		boolean valcount = false;
		int counter = 0;
		try {
			WebElement topicList = commonLibrary.isExist(UIMAP_Home.WintopicList);
			List<WebElement> pane = commonLibrary.isExistList(topicList, UIMAP_Home.aside, 10);
			if (pane.size() == 1) {
				count = true;
			}
			List<WebElement> ul = commonLibrary.isExistList(pane.get(0), UIMAP_Home.ul, 10);
			List<WebElement> content = commonLibrary.isExistList(ul.get(0), UIMAP_Home.lstTagListItems, 10);
			for (int j = 0; j < content.size(); j++) {
				List<WebElement> contenta = commonLibrary.isExistList(content.get(j), UIMAP_Home.lnkLinks, 10);
				for (WebElement item1 : contenta) {
					if (item1.getText().equalsIgnoreCase("all sources")) {
						allsource = true;
						break;
					}
				}
				if (allsource)
					break;
			}
			List<WebElement> contents = commonLibrary.isExistList(ul.get(0), UIMAP_Home.lstTagListItems, 10);
			for (WebElement item : contents) {
				List<WebElement> contentbtn = commonLibrary.isExistList(item, UIMAP_Home.btnTagListItems, 10);
				for (int i = 0; i < contentbtn.size(); i++) {
					for (int k = 0; k < arr.length; k++) {
						if (contentbtn.get(i).getText().contains(arr[k])) {
							counter = counter + 1;
							break;
						}
					}
					if (counter == arr.length - 1) {
						valcount = true;
						break;
					}
				}
				if (valcount)
					break;
			}
			if (count && valcount && allsource) {
				report.updateTestLog("Verify two panes are displayed after clicking 'Sources' link and all the options are displayed in vertical position in first pane", "Two panes are displayed after clicking 'Sources' link and all the options are displayed in vertical position in first pane", Status.PASS);
			} else {
				report.updateTestLog("Verify two panes are displayed after clicking 'Sources' link and all the options are displayed in vertical position in first pane", "Two panes are not displayed after clicking 'Sources' link and all the options are not displayed in vertical position in first pane", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify The Contents In SelectedPane
	// # Function Name : VerifyTheContentsInSelectedPane     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home verifyTheContentsInSelectedPane(String topic, String values) {

		WebElement topicList = commonLibrary.isExist(UIMAP_Home.WintopicList);
		commonLibrary.selectFromList(topicList, topic);

		String[] arrmissing = new String[20];

		boolean valcount = false;
		boolean count = false;

		try {
			WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
			List<WebElement> pane = commonLibrary.isExistList(topicList1, UIMAP_Home.aside, 10);
			if (pane.size() == 2) {
				count = true;
			}
			List<WebElement> ul = commonLibrary.isExistList(pane.get(1), UIMAP_Home.ul, 10);
			List<WebElement> content = commonLibrary.isExistList(ul.get(0), UIMAP_Home.lstTagListItems, 10);
			int m = 0;
			for (WebElement item : content) {
				List<WebElement> contentbtn = commonLibrary.isExistList(item, UIMAP_Home.lnkLinks, 10);
				// for(int k=0;k<arr.length;k++)
				// {
				for (int i = 0; i < contentbtn.size(); i++) {
					// if(Arrays.asList(arr.toString().toLowerCase()).contains(contentbtn.get(i).getText().toLowerCase()))
					if (values.toLowerCase().contains(contentbtn.get(i).getText().toLowerCase())) {
						valcount = true;
					} else {
						valcount = false;
						arrmissing[m] = contentbtn.get(i).getText();
						break;
					}
				}

			}
			if (count && valcount) {
				report.updateTestLog("Verify list of " + topic + " displays in the second pane at vertical postion  with scroll bar option ", "List of " + topic + " displays in the second pane at vertical postion  with scroll bar option ", Status.PASS);
			} else {
				report.updateTestLog("Verify list of " + topic + " displays in the second pane at vertical postion  with scroll bar option ", "List of " + topic + " is not displayed in the second pane at vertical postion  with scroll bar option.Missing values are " + arrmissing + " ", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the empty third pane
	// # Function Name : VerifyEmptyThirdPane     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home verifyEmptyThirdPane() {
		boolean count = false;
		WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
		List<WebElement> pane = commonLibrary.isExistList(topicList1, UIMAP_Home.aside, 10);
		if (pane.size() == 2) {
			count = true;
		}
		if (count) {
			report.updateTestLog("Verify third pane displays as blank", "Third pane is displayed as blank", Status.PASS);
		} else {
			report.updateTestLog("Verify third pane displays as blank", "Third pane is not displayed as blank", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select All Sources
	// # Function Name : SelectAllSource     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Sources selectAllSource() {
		WebElement topicList = commonLibrary.isExist(UIMAP_Home.WintopicList);
		commonLibrary.selectFromList(topicList, "All Sources");
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in Global Navigation
	// Menu from Home Page
	// # Function Name : SetClientIDGeneric_Home
	// # Author : Ram Prasath
	// # Date Created : 27 Feb'15
	// #*****************************************************************************************************************************

	// public Home newClientIDGeneric_Home(String strClientID)
	// {
	// //Click Client or Matter dropdown in Global Navigation
	// //Click Set/Add Client ID or Click Set/Add Matter ID
	// NavigateToClientLink("Set/Add Client ID");
	//
	// //Select New Client ID or New Matter ID Radio button
	// WebElement rdoClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.rdoClientId);
	// if(rdoClientId!=null)
	// {
	// report.updateTestLog("Selecting Set New Client ID",
	// "New Client Option Selected.", Status.PASS);
	// commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
	// }
	// else
	// report.updateTestLog("Selecting Set New Client ID",
	// "Client Option Radio button not found.", Status.FAIL);
	//
	// //ENTER A <<<>>> ID IN TEXT BOX
	// WebElement txtNewClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.txtNewClientId);
	// if(txtNewClientId!=null)
	// {
	// commonLibrary.SetDataInTextBox(txtNewClientId, strClientID, "Client id");
	// //report.updateTestLog("Setting New Client ID",
	// "New Client ID is set to Abcd.", Status.PASS);
	// }
	// else
	// report.updateTestLog("Setting New Client ID",
	// "New Client ID can not be set.", Status.FAIL);
	// //Click Set Client ID Button or Set Matter ID button
	// WebElement btnSaveClientId =
	// commonLibrary.isExist(UIMAP_SearchResult.btnSaveClientId);
	// if(btnSaveClientId!=null)
	// {
	// report.updateTestLog("Clicking on Save Client ID",
	// "Save Client ID is clicked.", Status.PASS);
	// if(browsername.contains("internet"))
	// {
	// commonLibrary.clickButton_Parent_WithWait_JS(btnSaveClientId,
	// "Save Client");
	// }
	// else
	// {
	// commonLibrary.clickButton_Parent_WithWait(btnSaveClientId,
	// "Save Client");
	// }
	//
	// }
	// else
	// report.updateTestLog("Clicking on Save Client ID",
	// "Save Client ID can not be clicked.", Status.FAIL);
	//
	// return new Home(scriptHelper);
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : NavigateToBrowserLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document navigateToBrowserLink1(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction) {

		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							blnFirst = true;
							WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
							if (browsername.contains("internet")) {
								// commonLibrary.highlightElement(button1);
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
				WebElement btnActions = commonLibrary.isExist(lstTagAside.get(2), UIMAP_Home.btnTypeButton, 20);
				// commonLibrary.highlightElement(btnActions);
				commonLibrary.clickButtonParentWithWait(btnActions, "ACTIONS FOR AGENCY ADJUDICATION");

				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary.isExist(lstTagAside1.get(2), UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strAction)) {
							blnFive = true;
							// WebElement button1 =
							// commonLibrary.isExist_Negative(button,
							// UIMAP_HomePage.btnTagListItems, 10);
							if (browsername.contains("internet"))
								commonLibrary.clickJS(button, strAction);
							else
								commonLibrary.clickButtonParentWithWait(button, strAction);
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
	// # Function Description : Functionused to click the browse link
	// documents
	// # Function Name : navigateToBrowserLink2     
	// # Author : Ram
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search navigateToBrowserLink2(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction, String linktest) {

		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			int count = 0;
			do {
				count++;
				divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
				if (divIdBrowserMenu == null)
					commonLibrary.sleep(3000);
			} while (divIdBrowserMenu == null && count <= 5);

			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							blnFirst = true;

							WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
							// commonLibrary.ScrollToView(button1);
							if (browsername.contains("internet")) {
								// commonLibrary.highlightElement(button1);
								commonLibrary.clickJS(button1, strParentMenuName);
							} else
								commonLibrary.clickButtonParentWithWait(button1, strParentMenuName);
							break;
						}

					}
				if (!blnFirst)
					report.updateTestLog("Click on " + strParentMenuName, "Not Clicked on " + strParentMenuName, Status.FAIL);
			}
			if (strFirstSubMenuName != "") {
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				int count1 = 0;
				do {
					count1++;
					divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
					if (divIdBrowserSubMenu == null)
						commonLibrary.sleep(3000);
				} while (divIdBrowserSubMenu == null && count1 <= 3);

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
			if (strSecondSubMenuName != "") {

				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				int count1 = 0;
				do {
					count1++;
					divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
					if (divIdBrowserSubMenu1 == null)
						commonLibrary.sleep(3000);
				} while (divIdBrowserSubMenu1 == null && count1 <= 3);

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

			}
			if (strThridSubMenuName != "" && !strThridSubMenuName.contains("Actions")) {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				int count1 = 0;
				do {
					count1++;
					divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
					if (divIdBrowserSubMenu1 == null)
						commonLibrary.sleep(3000);
				} while (divIdBrowserSubMenu1 == null && count1 <= 3);

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
					if (!blnFive) {
						// report.updateTestLog("Click on "+strAction,
						// "Not Clicked on "+strAction, Status.FAIL);
						WebElement divIdBrowserSubMenu4 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> lstTagAside4 = commonLibrary.isExistList(divIdBrowserSubMenu4, UIMAP_Home.lstTagAside, 20);
						// List<WebElement> lstTagListItems4 =
						// commonLibrary.isExist_List(lstTagAside4.get(3),UIMAP_Home.lstTagListItems,
						// 20);
						List<WebElement> header = commonLibrary.isExistList(lstTagAside4.get(3), UIMAP_Home.header, 20);
						WebElement actionBar = commonLibrary.isExist(header.get(1), UIMAP_Home.actionbar, 20);
						WebElement btnActions = commonLibrary.isExist(actionBar, UIMAP_Home.btnTypeButton, 20);
						// commonLibrary.highlightElement(btnActions);
						if (browsername.contains("internet")) {
							commonLibrary.clickButtonLogSmallWait(btnActions, btnActions.getText());
						} else {
							// commonLibrary.clickButton_Parent_WithWait(btnActions,btnActions.getText());
							commonLibrary.clickButtonLogSmallWait(btnActions, btnActions.getText());
						}

						WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
						WebElement divClassActionsList = commonLibrary.isExist(lstTagAside1.get(3), UIMAP_Home.divClassActionsList, 20);
						List<WebElement> lstTagListItems1 = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)
							for (WebElement button1 : lstTagListItems1) {
								if (button1.getText().contains(linktest)) {
									blnFive = true;
									WebElement actionlink = commonLibrary.isExistNegative(button1, UIMAP_Home.actionlink, 10);
									if (browsername.contains("internet"))
										commonLibrary.clickButtonParentWithWait(actionlink, linktest);
									else
										// commonLibrary.clickButton_Parent_WithWait(button1,
										// linktest);
										commonLibrary.clickButtonParentWithWait(actionlink, linktest);
									break;
								}

							}
						// WebElement linkselect =
						// commonLibrary.isExist(UIMAP_HomePage.getdocumentlink,
						// 20);
						// commonLibrary.clickButton_Parent_WithWait(linkselect,linktest);
					}
				}

			} else if (strThridSubMenuName.contains("Actions") || strAction.contains("Actions")) {
				// Steps to Click on Actions button
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				int count1 = 0;
				do {
					count1++;
					divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
					if (divIdBrowserSubMenu1 == null)
						commonLibrary.sleep(3000);
				} while (divIdBrowserSubMenu1 == null && count1 <= 3);

				List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
				List<WebElement> header = commonLibrary.isExistList(lstTagAside.get(2), UIMAP_Home.header, 20);
				WebElement actionBar = commonLibrary.isExist(header.get(1), UIMAP_Home.actionbar, 20);
				WebElement btnActions = commonLibrary.isExist(actionBar, UIMAP_Home.btnTypeButton, 20);
				// commonLibrary.highlightElement(btnActions);
				commonLibrary.clickButtonLogSmallWait(btnActions, btnActions.getText());

				// Steps to click on links from action list dropdown
				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary.isExist(lstTagAside1.get(2), UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(linktest)) {
							blnFive = true;
							WebElement actionlink = commonLibrary.isExistNegative(button, UIMAP_Home.actionlink, 10);
							if (browsername.contains("internet"))
								// commonLibrary.Click_JS(button);
								// commonLibrary.clickLink_withWebElement_WithWait_JS(button,linktest);
								// commonLibrary.clickButton_Parent_WithWait_JS(button,linktest);
								commonLibrary.clickJS(actionlink, linktest);
							else
								commonLibrary.clickButtonLogSmallWait(actionlink, linktest);
							break;
						}

					}

				if (!blnFive)
					report.updateTestLog("Click on " + strAction, "Not Clicked on " + strAction, Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select topic, subtopic and click get
	// documents
	// # Function Name : selectTopicAndGetDocuments     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search selectTopicAndGetDocuments(String strTopic, String strSubtopic, String downArrow) {

		try {
			WebElement topicList = commonLibrary.isExist(UIMAP_Home.WintopicList);
			if (strTopic.equalsIgnoreCase("healthcare law")) {
				WebElement topic = commonLibrary.isExist(topicList, UIMAP_Home.healthcare, 10);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWait(topic, "health care");
				} else {
					commonLibrary.clickButtonParentWithWait(topic, strTopic);
				}
				{
					report.updateTestLog("Click on " + strTopic + "", "" + strTopic + "is clicked", Status.PASS);
					commonLibrary.sleep(3000);
					WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
					List<WebElement> subtopic = commonLibrary.isExistList(topicList1, By.tagName("button"), 20);
					for (WebElement item : subtopic) {
						if (item.getText().toLowerCase().equals(strSubtopic.toLowerCase())) {
							if (browsername.contains("internet")) {
								commonLibrary.clickButtonParentWithWait(item, strSubtopic);
								commonLibrary.sleep(4000);
							} else {
								commonLibrary.clickLinkWithWebElement(item, strSubtopic);
							}
							WebElement topicList2 = commonLibrary.isExist(UIMAP_Home.WintopicList);
							List<WebElement> down = commonLibrary.isExistList(topicList2, UIMAP_Home.downBtn, 20);
							for (WebElement item2 : down) {
								if (item2.isDisplayed()) {
									if (browsername.contains("internet")) {
										commonLibrary.clickButtonParentWithWait(item2, downArrow);
										break;
									} else {
										commonLibrary.clickButtonParentWithWait(item2, downArrow);
										break;
									}
								}
							}
							WebElement topicList3 = commonLibrary.isExist(UIMAP_Home.WintopicList);
							List<WebElement> getdoc = commonLibrary.isExistList(topicList3, UIMAP_Home.getDoc, 20);
							if (getdoc != null) {
								for (WebElement item4 : getdoc) {
									if (item4 != null && item4.isDisplayed()) {
										if (browsername.contains("internet")) {
											commonLibrary.clickButtonParentWithWait(item4, "Get All Documents for topic");
											break;
										} else {
											commonLibrary.clickButtonParentWithWait(item4, "Get All Documents for topic");
											break;
										}
									}
								}
								break;
							} else {
								report.updateTestLog("Click on Get all documents for topic", "Get all documents for topic is not clicked", Status.PASS);
							}
						}
					}
				}
			} else if (commonLibrary.selectFromList(topicList, strTopic)) {
				report.updateTestLog("Click on " + strTopic + "", "" + strTopic + "is clicked", Status.PASS);
				commonLibrary.sleep(3000);
				WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
				List<WebElement> subtopic = commonLibrary.isExistList(topicList1, By.tagName("button"), 20);
				for (WebElement item : subtopic) {
					if (item.getText().toLowerCase().equals(strSubtopic.toLowerCase())) {
						if (browsername.contains("internet")) {
							commonLibrary.clickJS(item, strSubtopic);
						} else {
							commonLibrary.clickLinkWithWebElement(item, strSubtopic);
						}
						WebElement topicList2 = commonLibrary.isExist(UIMAP_Home.WintopicList);
						List<WebElement> down = commonLibrary.isExistList(topicList2, UIMAP_Home.downBtn, 20);
						for (WebElement item2 : down) {
							if (item2.isDisplayed()) {
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(item2, downArrow);
									break;
								} else {
									commonLibrary.clickButtonParentWithWait(item2, downArrow);
									break;
								}
							}
						}
						WebElement topicList3 = commonLibrary.isExist(UIMAP_Home.WintopicList);
						List<WebElement> getdoc = commonLibrary.isExistList(topicList3, UIMAP_Home.getDoc, 20);
						if (getdoc != null) {
							for (WebElement item4 : getdoc) {
								if (item4 != null && item4.isDisplayed()) {
									if (browsername.contains("internet")) {
										commonLibrary.clickJS(item4, "Get All Documents for topic");
										break;
									} else {
										commonLibrary.clickButtonParentWithWait(item4, "Get All Documents for topic");
										break;
									}
								}
							}
							break;
						} else {
							report.updateTestLog("Click on Get all documents for topic", "Get all documents for topic is not clicked", Status.PASS);
						}
					}
				}
			} else {
				report.updateTestLog("Click on " + strTopic + "", "" + strTopic + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		WebElement contentType = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 50);
		int counter = 0;
		do {
			counter = counter + 1;
			contentType = commonLibrary.isExist(UIMAP_SearchResult.ulCondentSwitcher, 50);
			if (contentType == null)
				commonLibrary.sleep(5000);
		} while (contentType == null && counter <= 36);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform topic search in topics menu
	// # Function Name : searchTopic
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics searchTopic(String searchTerm) {
		WebElement topicsMenu = commonLibrary.isExist(UIMAP_Home.topicsMenu, 10);

		WebElement topicsSearch = commonLibrary.isExist(topicsMenu, UIMAP_Home.topicsSearch, 10);
		commonLibrary.setDataInTextBox(topicsSearch, searchTerm, "Topic Search");

		WebElement searchButton = commonLibrary.isExist(topicsMenu, UIMAP_Home.topicsSearchBtn, 10);
		commonLibrary.clickButtonLogSmallWait(searchButton, "Topic Search");

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Topics from Browse topics in
	// Research
	// # Function Name : selectParentandChildTopicsTBE
	// # Author : Ram Prasath
	// # Date Created : 4 Mar'15
	// #*****************************************************************************************************************************

	public Search selectParentandChildTopicsTBE(String parenttopic, String childtopic, String childsubtopic, String actionforchildtopic, String topicoption) {
		try {
			if (parenttopic != null) {

				WebElement parenttopicsTBE = commonLibrary.isExist(UIMAP_Home.ullist, 20);

				if (parenttopicsTBE != null) {
					// commonLibrary.Select_FromList(lsttagtbe,parenttopic);
					List<WebElement> lstTagUList = commonLibrary.isExistList(parenttopicsTBE, UIMAP_Home.aside, 20);

					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.btnTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().toLowerCase().contains(parenttopic)) {

								commonLibrary.clickButtonParentWithWait(item, parenttopic);
								break;
							}

						}
				}

			}

		} catch (Exception e) {

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
	// # Function Description : Function for Click a Link in topic practice area
	// # Function Name : NavigateToBrowserLink
	// # Author : Veeshma
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home navigateTOBrowseLinks_Verify(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName) {

		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

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
				if (blnFirst) {
					WebElement topicSearch = commonLibrary.isExist(UIMAP_Home.topicsSearch, 20);
					WebElement topicMenu = commonLibrary.isExist(UIMAP_Home.topicsMenu, 20);
					WebElement topicSearchBtn = commonLibrary.isExist(topicMenu, UIMAP_Home.topicsSearchBtn, 20);
					WebElement subMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
					List<WebElement> pane = subMenu.findElements(By.tagName("aside"));
					List<WebElement> paneContent = pane.get(0).findElements(By.tagName("li"));
					if (topicSearch != null && topicSearchBtn != null && pane.size() == 1 && paneContent.size() > 0)
						report.updateTestLog("Verify Search for a Topic text box, Search button and Practise Areas in Second Pane is displayed ", "Textbox, Button and Pane contents are displayed", Status.DONE);
					else
						report.updateTestLog("Verify Search for a Topic text box, Search button and Practise Areas Second Pane is displayed ", "Textbox, Button and Pane are NOT displayed", Status.FAIL);
				}
			}
			if (strFirstSubMenuName != "") {
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
					if (blnSecond) {
						WebElement searchType = commonLibrary.isExist(UIMAP_Home.rdoIdSrchType, 20);
						WebElement allTopic = commonLibrary.isExist(UIMAP_Home.rdoIdAllTopic, 20);
						WebElement subMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> pane1 = subMenu1.findElements(By.tagName("aside"));
						List<WebElement> pane1Content = pane1.get(1).findElements(By.tagName("li"));
						if (searchType != null && allTopic != null) {
							if ((commonLibrary.getParentElement(searchType).getText().contains(strFirstSubMenuName)) && pane1.size() == 2 && pane1Content.size() > 0)
								report.updateTestLog("Verify AllTopics,Within " + strFirstSubMenuName + " and Topics in Third Pane is displayed ", "Radio buttons and Pane Contents are displayed", Status.DONE);
							else
								report.updateTestLog("Verify AllTopics,Within " + strFirstSubMenuName + " and Third Pane is displayed ", "Radio buttons and Pane are NOT displayed", Status.FAIL);
						} else
							report.updateTestLog("Verify AllTopics,Within " + strFirstSubMenuName + " is displayed ", "Radio buttons are NOT displayed", Status.FAIL);
					}
				}
			}
			if (strSecondSubMenuName != "") {

				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(1), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strSecondSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.btnTypeButton, 20);
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
					if (blnThird) {
						WebElement subMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
						List<WebElement> pane2 = subMenu2.findElements(By.tagName("aside"));
						List<WebElement> pane2Content = pane2.get(1).findElements(By.tagName("li"));
						if (pane2.size() == 3 && pane2Content.size() > 0)
							report.updateTestLog("Verify Child Topics in Fourth Pane is displayed ", "Pane Contents are displayed", Status.DONE);
						else
							report.updateTestLog("Verify Child Topics in Fourth Pane is displayed ", "Pane are NOT displayed", Status.FAIL);
					}
				}

			}
			if (strThridSubMenuName != "") {
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(2), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strThridSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.btnTypeButton, 20);
								blnFour = true;
								if (browsername.contains("internet"))
									commonLibrary.clickButtonParentWithWait(button, strThridSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button, strThridSubMenuName);
								break;
							}

						}
					if (!blnFour)
						report.updateTestLog("Click on " + strThridSubMenuName, "Not Clicked on " + strThridSubMenuName, Status.FAIL);
				}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click First nav button under Browse
	// Menu
	// # Function Name : clickNavFirst     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickNavFirst() {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Last nav button under Browse
	// Menu
	// # Function Name : clickNavLast     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickNavLast() {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Previous nav button under
	// Browse Menu
	// # Function Name : clickNavPrev     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickNavPrev() {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Next nav button under Browse
	// Menu
	// # Function Name : clickNavNext     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickNavNext() {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Actions menu
	// # Function Name : verifyActions     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home verifyActions(String[] actions) {

		WebElement subMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
		List<WebElement> pane = subMenu.findElements(By.tagName("aside"));
		List<WebElement> paneContent = pane.get(3).findElements(By.tagName("li"));
		for (int i = 0; i < actions.length; i++) {
			boolean itemFlag = false;
			for (int j = 0; j < paneContent.size(); j++) {
				if (paneContent.get(j).getText().contains(actions[i])) {
					itemFlag = true;
					break;
				}
			}

			if (itemFlag)
				report.updateTestLog("Verify if " + actions[i] + " option from 'Actions' menu is available", actions[i] + " option from 'Actions' menu is available", Status.PASS);
			else
				report.updateTestLog("Verify if " + actions[i] + " option from 'Actions' menu is available", actions[i] + " option from 'Actions' menu is available", Status.PASS);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Verdicts and Settlements
	// Analyzer page
	// # Function Name : navigateToVSAPage
	// # Author : Shobana
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements navigateToVSAPage() {
		generalFunctions.productSwitcher("Verdict & Settlement Analyzer");
		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the current experience
	// # Function Name : verifyCurrentexperience
	// # Author : Deepha H
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Document verifyCurrentexperience() {
		WebElement currentproduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 10);
		String Experience = dataTable.getData("General_Data", "CurrentExperience");
		WebElement LexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 10);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(LexisAdvance, "Lexis Advance Arrow");
		} else {
			commonLibrary.clickButtonParentWithWait(LexisAdvance, "Lexis Advance Arrow");
		}

		if (currentproduct.getText().contains(Experience)) {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is present", Status.PASS);
		} else {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is not present", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the list of experiences
	// present under the Experience Bar
	// # Function Name : verifyexperienceoptions
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Home verifyexperienceoptions() {
		WebElement currentproduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 10);
		String Experience = dataTable.getData("General_Data", "CurrentExperience");
		WebElement LexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 10);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(LexisAdvance, "Lexis Advance Arrow");
		} else {
			commonLibrary.clickButtonParentWithWait(LexisAdvance, "Lexis Advance Arrow");
		}

		if (currentproduct.getText().contains(Experience)) {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is present", Status.PASS);
		} else {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is not present", Status.FAIL);
		}
		String experienceproduct1 = dataTable.getData("General_Data", "ExperienceProduct1");
		String experienceproduct2 = dataTable.getData("General_Data", "ExperienceProduct2");
		String experienceproduct3 = dataTable.getData("General_Data", "ExperienceProduct3");
		WebElement product1 = commonLibrary.isExist(UIMAP_Home.prodswitchermenu, 10);
		List<WebElement> product2 = commonLibrary.isExistList(product1, UIMAP_Home.lstTagUList, 10);
		List<WebElement> product3 = commonLibrary.isExistList(product2.get(0), UIMAP_Home.lstTagListItems, 10);
		if (product3.size() > 0) {
			int i = 0;

			for (WebElement button : product3) {

				if (button.getText().contains(experienceproduct1) && (i != 1)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct1 + " Experience is present", Status.PASS);
					i = 1;
				} else if (button.getText().contains(experienceproduct2)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct2 + " Experience is present", Status.PASS);
				} else if (button.getText().contains(experienceproduct3)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct3 + " Experience is present", Status.PASS);
				} else if (i != 1) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct1 + " Experience is not present", Status.FAIL);
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct2 + " Experience is not present", Status.FAIL);
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct3 + " Experience is not present", Status.FAIL);
				}

			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Experienceoptions
	// # Function Name : selectExperienceoptions
	// # Author : Arvind
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Home selectExperienceoptions() {
		WebElement currentproduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 10);
		String Experience = dataTable.getData("General_Data", "CurrentExperience");
		WebElement LexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 10);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWaitJS(LexisAdvance, "Lexis Advance Arrow");
		} else {
			commonLibrary.clickButtonParentWithWait(LexisAdvance, "Lexis Advance Arrow");
		}

		if (currentproduct.getText().contains(Experience)) {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is present", Status.PASS);
		} else {
			report.updateTestLog("Current Experience is verified", Experience + " Experience is not present", Status.FAIL);
		}
		String experienceproduct1 = dataTable.getData("General_Data", "ExperienceProduct1");
		String experienceproduct2 = dataTable.getData("General_Data", "ExperienceProduct2");
		String experienceproduct3 = dataTable.getData("General_Data", "ExperienceProduct3");
		WebElement product1 = commonLibrary.isExist(UIMAP_Home.prodswitchermenu, 10);
		List<WebElement> product2 = commonLibrary.isExistList(product1, UIMAP_Home.lstTagUList, 10);
		List<WebElement> product3 = commonLibrary.isExistList(product2.get(0), UIMAP_Home.lstTagListItems, 10);
		if (product3.size() > 0)

			for (WebElement button : product3) {

				if (button.getText().contains(experienceproduct1)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct1 + " Experience is present", Status.PASS);
				} else if (button.getText().contains(experienceproduct2)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct2 + " Experience is present", Status.PASS);
				} else if (button.getText().contains(experienceproduct3)) {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct3 + " Experience is present", Status.PASS);
				} else {
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct1 + " Experience is not present", Status.FAIL);
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct2 + " Experience is not present", Status.FAIL);
					report.updateTestLog("Items Present in Product Switcher is verified", experienceproduct3 + " Experience is not present", Status.FAIL);
				}
			}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product Switcher
	// # Function Name : productSwitcher
	// # Author : Arvind
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public NonLAProductWindow productSwitcher(String strLink) {
		WebElement productSwitcherMenu = null;
		int i = 0;
		boolean blnFlag = false;
		do {
			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			else
				commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

			productSwitcherMenu = commonLibrary.isExistNegative(UIMAP_Home.productSwitcherMenu, 10);
			i++;
		} while (productSwitcherMenu == null && i < 3);

		if (productSwitcherMenu != null) {
			List<WebElement> links = commonLibrary.isExistList(productSwitcherMenu, UIMAP_Home.lnkLinks, 10);
			for (WebElement lnk : links) {
				if (lnk.getText().toLowerCase().contains(strLink.toLowerCase())) {
					if (browsername.toLowerCase().contains("internet")) {
						try {
							driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.clickLinkWithWebElementWithWait(lnk, strLink);
							blnFlag = true;
							break;
						} catch (TimeoutException ex) {
							driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
						}
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(lnk, strLink);
						blnFlag = true;
						break;
					}

				}
			}
			if (!blnFlag)
				report.updateTestLog("Click on " + strLink, strLink + " is not clicked.", Status.FAIL);
		} else
			report.updateTestLog("Click on Product Switcher", "Product Switcher Menu is not displayed.", Status.FAIL);

		commonLibrary.sleep(5000);
		return new NonLAProductWindow(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to a particular page and
	// logging out
	// # Function Name : navigateToParticularPageAndLogout     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn navigateToParticularPageAndLogout(String page) {

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
			this.logout();
			break;
		}
		case "counsel benchmarking": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionCunselBenchmarking, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Counsel Benchmarking");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Counsel Benchmarking");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains("counsel benchmarking")) {
				report.updateTestLog("Counsel Benchmarking page is displayed", "Counsel Benchmarking page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Counsel Benchmarking page is displayed", "Counsel Benchmarking page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionCunselBenchmarking, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance® counsel benchmarking logo displays in the Top left corner of Global menu", "Lexis Advance® Counsel benchmarking logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® counsel benchmarking logo displays in the Top left corner of Global menu", "Lexis Advance® Counsel benchmarking logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Counsel Benchmarking");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Counsel Benchmarking");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			this.logout();
			break;
		}
		case "litigation profile suite": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionProfileSuite, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Litigation profile suite");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Litigation profile suite");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains("litigation profile suite")) {
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
			this.logout();
			break;
		}
		case "medmal navigator": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionMedMal, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Medmal navigator");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Medmal navigator");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("MedMal Navigator")) {
				report.updateTestLog("Verify MedMal Navigator page is displayed", "MedMal Navigator page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify MedMal Navigator page is displayed", "MedMal Navigator page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionMedMal, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance® Medmal navigator logo displays in the Top left corner of Global menu", "Lexis Advance® Litigation Medmal navigator logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Medmal navigator logo displays in the Top left corner of Global menu", "Lexis Advance® Litigation Medmal navigator logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Medmal navigator");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Medmal navigator");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			this.logout();
			break;
		}
		case "vsa": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Verdict & settlement analyzer");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Verdict & settlement analyzer");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains("verdict & settlement analyzer")) {
				report.updateTestLog("Verify Verdict & Settlement Analyzer page is displayed", "Verdict & Settlement Analyzer page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Verdict & Settlement Analyzer page is displayed", "Verdict & Settlement Analyzer page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance®  Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", "Lexis Advance®  Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Verdict & Settlement Analyzer logo displays in the Top left corner of Global menu", "Lexis Advance®  Verdict & Settlement Analyzer logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Verdict & Settlement Analyzer");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Verdict & Settlement Analyzer");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			this.logout();
			break;
		}

		case "public records": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Public Records");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Public Records");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains("public records")) {
				report.updateTestLog("Verify Public Records page is displayed", "Public Records page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Public Records page is displayed", "Public Records page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", "Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Public Records logo displays in the Top left corner of Global menu", "Lexis Advance® Public Records logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Public Records");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Public Records");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			this.logout();
			break;
		}
		case "guib": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnGuib, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "GUIB");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "GUIB");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("Global UI Builder")) {
				report.updateTestLog("Verify Global UI Builder page is displayed", "Global UI Builder page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Global UI Builder page is displayed", "Global UI Builder page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnGuib, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis Advance®  Global UI Builder logo displays in the Top left corner of Global menu", "Lexis Advance®  Global UI Builder logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Global UI Builder logo displays in the Top left corner of Global menu", "Lexis Advance® Global UI Builder logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis Advance® Global UI Builder");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Global UI Builder");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			WebElement btnIdLexisAdvance1 = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnIdLexisAdvance1, "Lexis Advance Arrow");
			else
				commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance1, "Lexis Advance Arrow");

			WebElement btnLexisAdvanceResearch = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnLexisAdvanceResearch, "Lexis Advance® Research");
			else
				commonLibrary.clickLinkWithWebElement(btnLexisAdvanceResearch, "Lexis Advance® Research");

			this.logout();
			break;
		}
		case "lpa": {

			WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionLPA, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnActionCunselBenchmarking, "Lexis practice advisor");
			else
				commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Lexis practice advisor");

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 200);
			if (CurrentProduct.getText().contains("Practice Advisor")) {
				report.updateTestLog("Verify Lexis practice advisor page is displayed", "Lexis practice advisor page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis practice advisor page is displayed", "Lexis practice advisor page is not displayed", Status.FAIL);
			}
			WebElement logo = commonLibrary.isExist(UIMAP_Home.btnActionLPA, 20);
			if (logo != null) {
				report.updateTestLog("Verify Lexis practice advisor logo displays in the Top left corner of Global menu", "Lexis practice advisor logo displays in the Top left corner of Global menu", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis practice advisor logo displays in the Top left corner of Global menu", "Lexis practice advisor logo is not displayed in the Top left corner of Global menu", Status.FAIL);
			}
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(logo, "Lexis practice advisor");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			} else {
				commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Global UI Builder");
				report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
			}
			this.logout();
			break;
		}
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : NavigateToAll     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Home selectchildoptionsinSources(String option) {
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
				break;
			}
		}
		if (!blnSecond)
			report.updateTestLog("Click on Categories", option + " Option not present", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the presence of subchild
	// source filters
	// # Function Name : verifysubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Home verifysubchildSources() {

		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);
		for (WebElement button : lstTagListItems2) {
			String itemname = button.getText();
			if (!itemname.equals(""))
				report.updateTestLog("Display of Categories is Verified", itemname + " is Present", Status.PASS);
		}

		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the sources subchild
	// # Function Name : verifysourcessubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Sources verifysourcessubchildSources() {

		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);
		for (WebElement button : lstTagListItems2) {
			String itemname = button.getText();
			report.updateTestLog("Display of Categories is Verified", itemname + " is Present", Status.PASS);
		}

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the sub child sources in SS
	// # Function Name : selectsubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Sources selectSubChildSources(String item) {
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
				break;
			}
		}
		if (!blnfirst)
			report.updateTestLog("Click on Items", item + " Items not present", Status.FAIL);
		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify RFF filters in home page
	// # Function Name : verifyRFFFilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Home verifyRFFFilters(String menuname, String rfflink) {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		commonLibrary.clickButtonLogSmallWait(btnClassFilter, "Filter");
		this.filterMenuSelection(menuname);
		WebElement favouritefilter = commonLibrary.isExist(UIMAP_Home.recentFavoritesFilter, 20);
		WebElement ullist = commonLibrary.isExist(favouritefilter, UIMAP_Home.lstTagUList, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist, UIMAP_Home.lstTagListItems, 20);
		WebElement button = commonLibrary.isExist(lilist.get(0), UIMAP_Home.recentFilterValue, 20);
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in Global Navigation
	// Menu from Home Page
	// # Function Name : SetClientIDGeneric_Home
	// # Author : Ram Prasath
	// # Date Created : 27 Feb'15
	// #*****************************************************************************************************************************

	public ClientID newClientIDGenericClient(String strClientID) {
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

			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId, "Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
			}

		} else
			report.updateTestLog("Clicking on Set Client ID", "Set Client ID can not be clicked.", Status.FAIL);

		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on LPA product logo
	// # Function Name : clickProductLogoLPA
	// # Author : Shobana
	// # Date Created : May'2015
	// #*****************************************************************************************************************************

	public LPAHome clickProductLogoLPA() {
		navigateToLPA();
		WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		WebElement practiceAreaNav = commonLibrary.isExist(UIMAP_LPAHome.practiceAreaNav, 10);
		if (currentProduct != null && practiceAreaNav != null)
			commonLibrary.clickLinkWithWebElement(currentProduct, "LPA logo");
		else
			report.updateTestLog("Click on LPA product logo", "LPA is not the current product", Status.FAIL);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for select DocTab In HistoryPod
	// # Function Name : selectDocTabInHistoryPod     
	// # Author : Divakar
	// # Date Created : APR '15
	// #*****************************************************************************************************************************

	public Home selectDocTabInHistoryPod() {
		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnExpandCollapse, 10);
		if (eltHistoryPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse, "Expand/Collapse");
			eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
			btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnExpandCollapse, 10);

		}
		WebElement subHeaderHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.subHeaderHistPod, 10);
		int count = 0;
		do {
			count++;
			subHeaderHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.subHeaderHistPod, 10);
		} while (subHeaderHistPod == null && count < 15);
		WebElement docbtnHistPod = commonLibrary.isExistNegative(subHeaderHistPod, UIMAP_Home.docbtnHistPod, 10);
		commonLibrary.clickLinkWithWebElementWithWait(docbtnHistPod, docbtnHistPod.getText());

		eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement docListHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.docListHistPod, 10);
		try {
			count = 0;
			do {
				commonLibrary.sleep(300000);
				count++;
				docListHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.docListHistPod, 3);

			} while (docListHistPod == null && count < 20);
		} catch (Exception e) {
			commonLibrary.sleep(1000000);
			System.out.println(e.toString());
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Recent Document
	// # Function Name : verifyRecentDocument     
	// # Author : Divakar
	// # Date Created : APR'15
	// #*****************************************************************************************************************************

	public Home verifyRecentDocument(String docTitle) {
		boolean blnFlag = false;
		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement docListHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.docListHistPod, 10);
		List<WebElement> docLinks = commonLibrary.isExistList(docListHistPod, UIMAP_Home.lnkLinks, 10);
		int count = 0;
		do {
			commonLibrary.sleep(200000);
			count++;
			docLinks = commonLibrary.isExistList(docListHistPod, UIMAP_Home.lnkLinks, 10);
		} while (docLinks.size() == 0 && count < 20);

		for (WebElement link : docLinks) {
			if (link.getAttribute("title").toLowerCase().contains(docTitle.toLowerCase())) {
				report.updateTestLog("Verify " + docTitle + " is present in Documents Tab of History Pod", docTitle + " is present in Documents Tab of History Pod", Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + docTitle + " is present in Documents Tab of History Pod", docTitle + " is not present in Documents Tab of History Pod", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Part 2 Absent
	// # Function Name : verifyPart2Absent     
	// # Author : Divakar
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyPart2Absent(String docTitle) {
		boolean blnFlag = false;
		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement docListHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.docListHistPod, 10);
		List<WebElement> docLinks = commonLibrary.isExistList(docListHistPod, UIMAP_Home.lnkLinks, 10);
		for (WebElement link : docLinks) {
			if (link.getAttribute("title").toLowerCase().contains(docTitle.toLowerCase())) {
				report.updateTestLog("Verify New activity for part 2 of 23 is not displayed", "New activity for part 2 of 23 is displayed", Status.FAIL);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify New activity for part 2 of 23 is not displayed", "New activity for part 2 of 23 is not displayed", Status.PASS);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click recent Document
	// # Function Name : clickRecentDocument     
	// # Author : Divakar
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document clickRecentDocument(String docTitle) {
		boolean blnFlag = false;
		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
		WebElement docListHistPod = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.docListHistPod, 10);
		List<WebElement> docLinks = commonLibrary.isExistList(docListHistPod, UIMAP_Home.lnkLinks, 10);
		for (WebElement link : docLinks) {
			if (link.getAttribute("title").toLowerCase().contains(docTitle.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(link, link.getText());
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click " + docTitle + " from Documents Tab of History Pod", docTitle + " is not present in Documents Tab of History Pod", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for adding Fav
	// # Function Name : verifySignInPage1     
	// # Author : Divakar
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifySignInPage1() {

		// driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

		if (driver.getCurrentUrl().toLowerCase().contains("usresearchhome")) {
			report.updateTestLog("Verify Landing Page", "Landing Page is displayed", Status.PASS);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for adding Fav
	// # Function Name : addToFav     
	// # Author : Aravind
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home addToFav() {

		WebElement eltFavButton = commonLibrary.isExistNegative(UIMAP_Home.eltFavButton, 10);
		if (eltFavButton != null)
			commonLibrary.clickButtonParentWithWait(eltFavButton, eltFavButton.getText());
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for adding Fav
	// # Function Name : addToFav     
	// # Author : Vidhya
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home addToFav(String verifyMsg) {
		WebElement eltFavButton = commonLibrary.isExistNegative(UIMAP_Home.eltFavButton, 10);
		if (eltFavButton != null)
			commonLibrary.clickJSWithoutWait(eltFavButton, "Add to Favourites icon");
		WebElement banner = commonLibrary.isExist(UIMAP_SearchResult.eltShepSearchBan, 5);
		if (banner != null && banner.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(verifyMsg.toLowerCase().replaceAll("[^a-zA-Z0-9]", "")))
			report.updateTestLog("Verify " + verifyMsg + " message is appears", " " + verifyMsg + " message is appears", Status.PASS);
		else
			report.updateTestLog("Verify " + verifyMsg + " message is appears", " " + verifyMsg + " message is appears", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click Research Home
	// # Function Name : clickResearchHome     
	// # Author : Aravind
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home clickResearchHome() {
		WebElement eltResearchHomeAnchor = commonLibrary.isExistNegative(UIMAP_Home.eltResearchHomeAnchor, 10);
		if (eltResearchHomeAnchor != null)
			commonLibrary.clickLinkWithWebElementWithWait(eltResearchHomeAnchor, eltResearchHomeAnchor.getText());
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Pod Contents Under Fav
	// # Function Name : verifyPodContentsUnderFav     
	// # Author : Aravind
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyPodContentsUnderFav() {
		WebElement eltFavoritesPod = commonLibrary.isExistNegative(UIMAP_Home.eltFavoritesPod, 10);
		if (eltFavoritesPod != null) {
			WebElement btnExpandCollapse1 = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.btnExpandCollapse1, 10);
			System.out.println("inside favoritepod");
			if (eltFavoritesPod.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButtonLogSmallWait(btnExpandCollapse1, "Expand/Collapse");
				eltFavoritesPod = commonLibrary.isExistNegative(UIMAP_Home.eltFavoritesPod, 10);
				btnExpandCollapse1 = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.btnExpandCollapse1, 10);

			}
			if (btnExpandCollapse1 != null) {
				System.out.println("inside expandcollapse");
				report.updateTestLog("Verify Ability to collopse/exand pod is located at top left handcorner", "Ability to collopse/exand pod is located at top left handcorner", Status.PASS);
				WebElement eltFavoriteItems = commonLibrary.isExist(btnExpandCollapse1, UIMAP_Home.eltFavoritesList, 10);

				if (eltFavoriteItems != null) {
					System.out.println("inside global content");
					WebElement eltFavoritesPodContent = commonLibrary.isExistNegative(eltFavoriteItems, UIMAP_Home.eltFavoritesPodContent, 10);
					if (eltFavoritesPodContent != null) {

						System.out.println("inside eltfavouriteitems");
						List<WebElement> lnkFavorritesLink = commonLibrary.isExistList(eltFavoritesPodContent, UIMAP_Home.eltFavoritelistitem, 10);
						int i, count = 0;
						for (i = 0; i < lnkFavorritesLink.size(); i++) {
							if (lnkFavorritesLink.get(i).isDisplayed())
								count++;
						}
						if (count > 0 && count <= 5) {
							report.updateTestLog("Verify there are 1 to 5 favorites listed' ", "there are " + count + " favorites listed", Status.PASS);
							report.updateTestLog("Verify Each item is a link' ", "Each item is a link", Status.PASS);
						}
					}
				}
			}
		}
		WebElement eltPodIcon = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.eltPodIcon, 10);
		if (eltPodIcon != null)
			report.updateTestLog("Verify Favorites icon is present to the left of the Favorites  title", "Favorites icon is present to the left of the Favorites title", Status.PASS);

		WebElement eltPodHeader = commonLibrary.isExistNegative(eltFavoritesPod, UIMAP_Home.eltPodHeader, 10);
		if (eltPodHeader != null) {
			WebElement lnkHeaderLink = commonLibrary.isExistNegative(eltPodHeader, UIMAP_Home.lnkLinks, 10);
			if (lnkHeaderLink != null) {
				if (lnkHeaderLink.getText().contains("Tips"))
					report.updateTestLog("Verify 'Tips' link displays at the top right corner in 'Favorites' pod", "'Tips' link displays at the top right corner in 'Favorites' pod", Status.PASS);
			}
		}
		return new Home(scriptHelper);
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
	// # Function Description : Function for navigating to Medmal Navigator page
	// by clicking Lexis Advance arrow
	// # Function Name : navigateToMedmalNavigator     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public MedmalNavigator navigateToMedmalNavigator() {
		generalFunctions.productSwitcher("Medmal Navigator");

		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Practice Advisorr
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigateToLPA     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPAHome navigateToLPA() {
		generalFunctions.productSwitcher("Practice Advisor");
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Public Record
	// page by clicking Lexis Advance arrow
	// # Function Name : NaviatetoPR     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #************************************************************************************************
	public Publicrecords naviatetoPR() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Public Records");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Public Records");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("public records")) {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", "Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Public Records logo displays in the Top left corner of Global menu", "Lexis Advance® Public Records logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		}
		if (browsername.contains("internet")) {
			commonLibrary.clickJS(logo, "Lexis Advance® Public Records");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		} else {
			commonLibrary.clickButtonParentWithWait(logo, "Lexis Advance® Public Records");
			report.updateTestLog("Verify Logo is clickable and the feature landing page displays", "Logo is clickable and the feature landing page is displayed", Status.PASS);
		}
		return new Publicrecords(scriptHelper);
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

		WebElement lexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWait(lexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(lexisAdvance, "Lexis Advance Arrow");
		commonLibrary.sleep(200000);
		WebElement actionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionICW, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWait(actionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		else
			commonLibrary.clickLinkWithWebElement(actionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		String url = driver.getCurrentUrl();
		int count = 0;
		do {
			commonLibrary.sleep(800000);
			url = driver.getCurrentUrl();
			if (url.contains("/icw"))
				break;
			count++;
		} while (count < 20);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance® Tax
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigatetoLAT     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #************************************************************************************************
	public LexisAdvanceTax navigatetoLAT() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionTaxhome, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Lexis Advance® Tax");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Lexis Advance® Tax");

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set a new client id
	// # Function Name : setNewClientIDGeneric_1     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home setNewClientIDGeneric_1(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		navigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary.isExist(UIMAP_SearchResult.rdoClientId);
		if (rdoClientId != null) {
			report.updateTestLog("Selecting Set New Client ID", "New Client Option Selected.", Status.PASS);
			commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID", "Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary.isExist(UIMAP_SearchResult.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID, "Client ID");
			// report.updateTestLog("Setting New Client ID",
			// "New Client ID is set to Abcd.", Status.PASS);
		} else
			report.updateTestLog("Setting New Client ID", "New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId, "Set Client ID");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId, "Set Client ID");
			}
		} else
			report.updateTestLog("Clicking on Save Client ID", "Save Client ID can not be clicked.", Status.FAIL);
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		int count = 0;
		do {
			eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
			count++;
			commonLibrary.sleep(2000);
		} while (eltSearchbox != null && count < 15);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to edit the client id
	// # Function Name : clickEditofGivenClient     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public ClientID clickEditofGivenClient(String ClientID) {
		boolean page = false;
		WebElement clientbtn = commonLibrary.isExist(UIMAP_ResearchMap.clientBtn);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(clientbtn, "Client " + ClientID + "");
		else {
			commonLibrary.clickButtonParentWithWait(clientbtn, "Client " + ClientID + "");
		}

		WebElement clientDiv = commonLibrary.isExist(UIMAP_ResearchMap.clientdiv);
		List<WebElement> clientToClick = commonLibrary.isExistList(clientDiv, UIMAP_ResearchMap.editClient, 10);
		for (WebElement item : clientToClick) {
			if (item.getAttribute("data-clientid").equalsIgnoreCase(ClientID)) {
				// report.updateTestLog("Verify edit link displays next to "+ClientID+" under client id drop down",
				// "Client drop down is expanded and edit link displays next to "+ClientID+" under client id drop down",
				// Status.PASS);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(item, "Edit Client");
				else {
					commonLibrary.clickButtonParentWithWait(item, "Edit Client");
				}
				WebElement text = commonLibrary.isExist(UIMAP_ResearchMap.clientText);
				if (text != null) {
					page = true;
					break;
				}

			}
		}
		if (page) {
			report.updateTestLog("Verify edit client id page is displayed", "Edit client id page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify edit client id page is displayed", "Edit client id page is not displayed", Status.FAIL);
		}

		return new ClientID(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the view all link in history
	// page
	// # Function Name : clickViewAllInHistory     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public History clickViewAllInHistory() {
		WebElement history = commonLibrary.isExist(UIMAP_SearchResult.history, 20);
		if (history != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(history, "History");
			else
				commonLibrary.clickButtonParentWithWait(history, "History");

			WebElement viewAllhistory = commonLibrary.isExist(UIMAP_SearchResult.viewAllhistory, 20);
			if (viewAllhistory != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(viewAllhistory, "View All");
				else
					commonLibrary.clickButtonParentWithWait(viewAllhistory, "View All");
				String url = null;
				int counter = 0;

				do {
					counter = counter + 1;
					url = driver.getCurrentUrl();
					if (!url.contains("history"))
						commonLibrary.sleep(5000);

				} while (!url.contains("history") && counter < 40);
			}
		}

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Counsel Bench Marking
	// page by clicking Lexis Advance arrow
	// # Function Name : navigateToCounselBenchMarking     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking navigateToCounselBenchMarking() {
		generalFunctions.productSwitcher("Counsel Benchmarking");
		return new CounselBenchmarking(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click <View all news> link displays
	// at the bottom of the pod
	// # Function Name : ClickViewAllNewsLink
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home clickViewAllNewsLinkAndCloseBrowser() {
		boolean blnFlag = true;
		WebElement news = commonLibrary.isExist(UIMAP_Home.divNews, 20);
		if (news != null) {
			List<WebElement> li = commonLibrary.isExistList(news, UIMAP_SearchResult.lnkHeader, 10);
			for (WebElement item : li) {
				if (item.getText().equalsIgnoreCase("View all news")) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(item, "View all news");
					else
						commonLibrary.clickButton(item);
					if (commonLibrary.switchToWindow("law")) {
						blnFlag = true;
						driver.close();
						commonLibrary.switchToWindow("firsttime");
						break;
					}
				}
			}
		}
		if (blnFlag) {

			// report.updateTestLog("Click <View all news> link displays at the bottom of the pod",
			// "<View all news> link is clicked at the bottom of the pod",
			// Status.PASS);
			report.updateTestLog("Verify LAW 360 page opens in a Secondarybrowser", "<LAW 360> page is opened in a Secondarybrowser", Status.PASS);
			report.updateTestLog("Close secondary Browser", "Secondary browser is closed", Status.PASS);

			report.updateTestLog("Verify secondary browser is closed", "secondary browser is closed", Status.PASS);
		} else {

			report.updateTestLog("Click <View all news> link displays at the bottom of the pod", "<View all news> link is not clicked at the bottom of the pod", Status.FAIL);
			report.updateTestLog("Verify LAW 360 page opens in a Secondarybrowser", "<LAW 360> page is not opened in a Secondarybrowser", Status.FAIL);

			report.updateTestLog("Verify secondary browser is closed", "secondary browser is not closed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close Citation Popup.
	// # Function Name : clearPreFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clearPreFilter() {
		generalFunctions.clearPreFilter();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to kill Outlook Process
	// # Function Name : killOutlookProcess   
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Home killOutlookProcess() {
		if (commonLibrary.killOutlookProcessThread())
			report.updateTestLog("verify outlook process killed", "Outlook process killed.", Status.PASS);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Change fot size and Type in settings page
	// # Function Name : general_Settings_Document_Display
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Home generalSettingsDocumentDisplay(String FontType, String FontSize) {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				commonLibrary.clickMethod(btnMore, "More");

			}
			WebElement btnSettings = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkSettigns), 100);
			if (btnSettings != null) {
				commonLibrary.clickLinkWithWebElementWithWait(btnSettings, "Settings");
			}
			commonLibrary.sleep(10000);
			// WebElement
			// inputContainer=commonLibrary.isExist(UIMAP_Settings.inputContainer,20);
			WebElement selectfontType = commonLibrary.isExist(UIMAP_Settings.txtBoxfonttype, 20);
			WebElement selectfontSize = commonLibrary.isExist(UIMAP_Settings.txtBoxfontSize, 20);
			WebElement btnSave = commonLibrary.isExist(UIMAP_Settings.btnSaveChanges, 20);
			if (selectfontSize != null && selectfontType != null && btnSave != null) {
				commonLibrary.selectFromListOption(selectfontType, FontType);
				commonLibrary.selectFromListOption(selectfontSize, FontSize);
				// Click in Save the Changes
				report.updateTestLog("Select  FontType ", "Font Type " + FontType + " is Selected", Status.PASS);
				report.updateTestLog("Select Font Size ", "Font Size " + FontSize + " is Selected", Status.PASS);

			}
			// for verifying sample text
			if (FontType.equalsIgnoreCase("Courier")) {
				FontType = "Monospace";
			}
			WebElement sampleText = commonLibrary.isExist(UIMAP_Settings.smplText, 20);
			if (sampleText != null) {

				String strTextPreview = sampleText.getAttribute("style");

				if (strTextPreview.contains(FontSize.toLowerCase()) && strTextPreview.contains(FontType)) {
					report.updateTestLog("Verification if Sample Text is reflected in Settings Page ", "Text is displayed with font type :" + FontType + " and font size :" + FontSize, Status.PASS);

				} else {
					report.updateTestLog("Verification if Sample Text is reflected in Settings Page ", "Text is not displayed with font type :" + FontType + " and font size :" + FontSize, Status.FAIL);
				}
				commonLibrary.clickButtonWithoutWait(btnSave, "Save changes to Settings & Close");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			// throw new FrameworkException("Exception", e.toString());
		}
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the landing page
	// # Function Name : Verify_LandingPage     
	// # Author : Revathi
	// # Date Created : May'15 <home java>
	// #*****************************************************************************************************************************
	public Home verifyLandingPage(String StartPage, String StartPagePrint) {

		Boolean blnFlag = false;
		List<WebElement> headerlist = commonLibrary.isExistList(UIMAP_Home.header, 20);
		for (WebElement header : headerlist) {
			if (header.getText().contains(StartPage))
				blnFlag = true;

		}
		if (blnFlag) {
			report.updateTestLog("Verify Landing Page", StartPagePrint + " is displayed", Status.PASS);
		} else {
			if (driver.getTitle().contains(StartPage)) {
				report.updateTestLog("Verify Landing Page", StartPagePrint + " is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Landing Page", StartPagePrint + " is NOT displayed", Status.FAIL);
			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Check Icw Link
	// # Function Name : checkICWLink     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Home checkICWLink() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExistNegative(UIMAP_Home.btnActionICW, 20);
		if (btnActionCunselBenchmarking != null) {
			report.updateTestLog("Verifying ICW is not Displayed", "ICW is Displayed", Status.FAIL);
		} else {
			report.updateTestLog("Verifying ICW is not Displayed", "ICW is not displayed", Status.PASS);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the links under Client
	// Menu
	// # Function Name : navigateToSetClientId     
	// # Author : Anbu
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ClientID navigateToSetClientId(String button) {
		navigateToClientLink(button);
		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to icwprofessorhome page
	// by clicking Lexis Advance arrow
	// # Function Name : NavigatetoICWProf     
	// # Author : Veeshma
	// # Date Created : May'15
	// #************************************************************************************************
	public ICWProfHome navigatetoICWProf() {

		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionICWFaculty, 20);
		if (browsername.contains("internet"))

			commonLibrary.clickButtonParentWithWait(btnActionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		else
			commonLibrary.clickButtonParentWithWait(btnActionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		// commonLibrary.sleep(50000);

		String url = driver.getCurrentUrl();
		int count = 0;
		do {
			commonLibrary.sleep(500000);
			url = driver.getCurrentUrl();
			if (url.contains("/icw"))
				break;
			count++;
		} while (count < 30);

		return new ICWProfHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to setting url to tyhe browser
	// # Function Name : putUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation setUrl(String Url) {
		// driver.manage().deleteAllCookies();
		driver.get(Url);
		report.updateTestLog("Set Url into Browser", "Url " + Url + " is set in to the browser", Status.PASS);

		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis® Interactive
	// Citation Workstation
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigatetoICW_VerifyOldICW     
	// # Author : Baswaraj
	// # Date Created : Jun'15
	// #************************************************************************************************
	public Interactivecitationworkstation navigatetoICWVerifyOldICW() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnActionICW, 20);
		if (btnActionCunselBenchmarking == null)
			btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.oldICW, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Lexis® Interactive Citation Workstation");
		commonLibrary.switchToWindow1("icw");
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWExcercise click_linkinPods1(String podName, String strlinkName) {
		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					List<WebElement> lstlinks = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					for (WebElement link1 : lstlinks) {
						if (link1.getText().toLowerCase().contains(strlinkName.toLowerCase())) {
							commonLibrary.clickJS(link1, strlinkName);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag) {
					break;
				}
			}
		if (blnFlag) {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.FAIL);
		}

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of attempts for each
	// question in ICW
	// # Function Name : SetNumberOfAttempts     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Research setNumberOfAttempts(String number) {
		generalFunctions.setNumberOfAttempts(number);
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to folders

	// # Function Name : navigateTofolders     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public WorkFolders navigateTofolders() {
		generalFunctions.navigateToFolders();
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to Lexis Advance Home page
	// # Function Name : switchToLexisAdvanceHome     
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Home switchToLexisAdvanceHome(String product) {
		boolean flag = false;
		WebElement lexisAdvanceHeader = commonLibrary.isExist(UIMAP_Home.lexisAdvanceHeader, 20);
		if (lexisAdvanceHeader != null) {
			flag = true;
		} else {
			productSwitcher(product);
			flag = true;
		}
		if (!flag)
			report.updateTestLog("Switch to Lexis Advance Home page", "Lexis Advance Home page is not displayed", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the LPAhome page is displayed
	// # Function Name : VerifySignInPage     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public LPAHome verifyLPAHomePage() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		if (driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtLPAHomeTitleMsg)) {
			report.updateTestLog("Verify LPA Landing Page", "LPA Landing Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify LPA Landing Page", "LPA Landing Page is not displayed", Status.FAIL);
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Practise Advisor page
	// by
	// clicking Lexis Advance arrow
	// # Function Name : navigateToPracticeAdvisorPage
	// # Author : Harish.E
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LPAHome navigateToPracticeAdvisorPage() {
		generalFunctions.productSwitcher("Practice Advisor");
		return new LPAHome(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the ClientID is not displayed
	// in
	// Global Navigation Menu
	// # Function Name : verifyClientIDNotDisplayed
	// # Author : Harish.E
	// # Date Created :Jul'15
	// #*****************************************************************************************************************************

	public Home verifyClientIDNotDisplayed() {
		try {
			WebElement clientname = commonLibrary.isExist(UIMAP_Home.clientName);
			String clientid = clientname.getText();
			if (clientid.contains("Set Client ID") || clientid.contains("Client: -None-")) {
				report.updateTestLog("Verify ClientID in Global Menu", "ClientID  is not displayed in Global Menu", Status.PASS);
			} else {
				report.updateTestLog("Verify ClientID in Global Menu", "ClientID is displayed in Global Menu", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome simpleSearchLPA(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to bring added documents to 725.
	// # Function Name : preReqTo725     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Research preReqTo1950() {
		int count = 0;
		WorkFolders folders = navigateTofolders();
		boolean flag = false;
		WebElement foldersTree = commonLibrary.isExistNegative(UIMAP_WorkFolders.foldersTreeCont, 10);
		List<WebElement> list = commonLibrary.isExistList(foldersTree, UIMAP_WorkFolders.div, 10);
		for (WebElement item : list) {
			if (item.getText().toLowerCase().contains("items saved")) {
				flag = true;
				String itemCount = item.getText().split(":")[1].trim();
				count = Integer.parseInt(itemCount);
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Add 1950 Documents", "Item Count is not displayed.", Status.FAIL);

		Search search = folders.clickProductlogo().setNumberOfResultsToBeDisplayed("50").simpleSearch("the", true).swithToCondentType("cases");
		boolean flag1 = false;
		String random = commonLibrary.getCurrentDateTime();
		String folderName = "Folder1950" + random;
		if ((1950 - count) > 50) {
			search = search.selectAllItemsAndVerifyCount("50").addToFolder(folderName).clickNextPage();
			flag1 = true;
		}

		int pages = (1950 - count) / 50;
		for (int i = 1; i <= (pages - 1); i++) {
			search = search.selectAllItemsAndVerifyCount("50").addToFolderUsingQuickSave(folderName).clickNextPage();
		}
		boolean flag2 = false;
		int left = (1950 - count) % 50;
		for (int i = 1; i <= left; i++) {
			flag2 = true;
			search = search.selectDocumentByNo(i);
		}
		if (flag2) {
			if (flag1)
				search = search.addToFolderUsingQuickSave(folderName);
			else
				search = search.addToFolder(folderName);
		}

		return search.clickProductlogo();

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to bring added documents to 450.
	// # Function Name : preReqTo450     
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public Research preReqTo450() {
		int count = 0;
		WorkFolders folders = navigateTofolders();
		boolean flag = false;
		WebElement foldersTree = commonLibrary.isExistNegative(UIMAP_WorkFolders.foldersTreeCont, 10);
		List<WebElement> list = commonLibrary.isExistList(foldersTree, UIMAP_WorkFolders.div, 10);
		for (WebElement item : list) {
			if (item.getText().toLowerCase().contains("items saved")) {
				flag = true;
				String itemCount = item.getText().split(":")[1].trim();
				count = Integer.parseInt(itemCount);
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Add 450 Documents", "Item Count is not displayed.", Status.FAIL);

		Search search = folders.clickProductlogo().setNumberOfResultsToBeDisplayed("50").simpleSearch("the", true).swithToCondentType("cases");
		boolean flag1 = false;
		String random = commonLibrary.getCurrentDateTime();
		String folderName = "Folder450" + random;
		if ((450 - count) > 50) {
			search = search.selectAllItemsAndVerifyCount("50").addToFolder(folderName).clickNextPage();
			flag1 = true;
		}

		int pages = (450 - count) / 50;
		for (int i = 1; i <= (pages - 1); i++) {
			search = search.selectAllItemsAndVerifyCount("50").addToFolderUsingQuickSave(folderName).clickNextPage();
		}
		boolean flag2 = false;
		int left = (450 - count) % 50;
		for (int i = 1; i <= left; i++) {
			flag2 = true;
			search = search.selectDocumentByNo(i);
		}
		if (flag2) {
			if (flag1)
				search = search.addToFolderUsingQuickSave(folderName);
			else
				search = search.addToFolder(folderName);
		}

		return search.clickProductlogo();

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearchLPAResults     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAResults simpleSearchLPAResults(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : applySearchFilterForEWA
	// # Author : Aravind
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home applySearchFilterForEWA(String strToolbarMenuName, String strCondentTypes, boolean state) {
		generalFunctions.applySearchFilterEWA(strToolbarMenuName, strCondentTypes, state);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and verify button is
	// present
	// # Function Name : verifyButtonInAction    
	// # Author : Anbarasan
	// # Date Created : July'15
	// #**************************************************************************************************
	public Home verifyButtonInAction(String buttonName, boolean presence) {
		commonLibrary.sleep(10);
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
		if (presence) {
			if (flag)
				report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is present in Actions dropdown", Status.PASS);
			else
				report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is not present in Actions dropdown", Status.FAIL);
		} else {
			if (!flag)
				report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is not present in Actions dropdown", Status.PASS);
			else
				report.updateTestLog("Verify " + buttonName + " is present in Actions dropdown", buttonName + " is present in Actions dropdown", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Click a Link in Practice Area page
	// # Function Name : navigateTOBrowseLinksAndVerify
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice navigateTOBrowseLinksAndVerify(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName) {
		try {
			commonLibrary.sleep(200);
			Boolean blnFirst = false, blnSecond = false, blnThird = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(100);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(100);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);
			commonLibrary.sleep(50);
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
							if (item.getText().trim().toLowerCase().contains(strFirstSubMenuName.trim().toLowerCase())) {
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
					// if (blnSecond) {
					// WebElement searchType =
					// commonLibrary.isExist(UIMAP_Home.rdoIdSrchType, 20);
					// WebElement allTopic =
					// commonLibrary.isExist(UIMAP_Home.rdoIdAllTopic, 20);
					// WebElement subMenu1 =
					// commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu,
					// 20);
					// List<WebElement> pane1 =
					// subMenu1.findElements(By.tagName("aside"));
					// List<WebElement> pane1Content =
					// pane1.get(1).findElements(By.tagName("li"));
					// if (searchType != null && allTopic != null) {
					// if
					// ((commonLibrary.getParentElement(searchType).getText().contains(strFirstSubMenuName))
					// && pane1.size() == 2 && pane1Content.size() > 0)
					// report.updateTestLog("Verify AllPAPs,Within " +
					// strFirstSubMenuName +
					// " and PAPs in Third Pane is displayed ",
					// "All PAPs are displayed",
					// Status.DONE);
					// else
					// report.updateTestLog("Verify AllPAPs,Within " +
					// strFirstSubMenuName + " and Third Pane is displayed ",
					// "All PAPs are NOT displayed", Status.FAIL);
					// } else
					// report.updateTestLog("Verify AllPAPs,Within " +
					// strFirstSubMenuName + " is displayed ",
					// "All PAPs are NOT displayed", Status.FAIL);
					// }
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
	// # Function Description : Function for navigating to Lexis Advance product
	// pages
	// # Function Name : productTaxSwitcher
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax productTaxSwitcher(String pageName) {
		try {
			commonLibrary.sleep(Mwait);
			WebElement btnIdLexisAdvance1 = commonLibrary.isExist(UIMAP_LexisAdvanceTax.practiceDiv);
			WebElement btnIdLexisAdvance = commonLibrary.isExist(btnIdLexisAdvance1, UIMAP_LexisAdvanceTax.practiceArea, 20);

			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Practice Area");
			commonLibrary.sleep(Mwait);

			List<WebElement> product = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.btntaxpracticearea, 20);

			for (WebElement option : product) {
				if (option.getText().contains(pageName))

				{
					commonLibrary.clickLinkWithWebElement(option, pageName);
					commonLibrary.sleep(10000);
					break;
				}
			}

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
			if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
				report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	public Home verifyLandingPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
		if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switch Tab
	// # Function Name : switchTab
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax switchTab(String tabName) {
		boolean flag = false;
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl, UIMAP_LexisAdvanceTax.li, 10);
		for (int i = 0; i < taxTabLiList.size(); i++) {
			WebElement button = commonLibrary.isExist(taxTabLiList.get(i), UIMAP_LexisAdvanceTax.button, 10);
			if (button.getText().toLowerCase().contains(tabName.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(button, button.getText());
				flag = true;
				break;
			}
			if (flag)
				break;
		}
		if (!flag)
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : navigateToBrowserLinkClickHeader
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Home navigateToBrowserLinkClickHeader(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction) {
		int oldCount = 0, newCount = 0;
		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);
			int counter = 0;
			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				oldCount = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 10).size();
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
					newCount = lstTagAside.size();
					counter = 0;
					try {
						do {
							commonLibrary.sleep(90000);
							counter++;
							lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
							newCount = lstTagAside.size();
						} while (oldCount == newCount && counter < 20);
					} catch (Exception e) {
						System.out.println(e.toString());
						commonLibrary.sleep(100000);
					}

					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(0), UIMAP_Home.lstTagListItems, 20);
					oldCount = lstTagAside.size();
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

					newCount = lstTagAside.size();
					counter = 0;
					try {
						do {
							commonLibrary.sleep(90000);
							counter++;
							lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
							newCount = lstTagAside.size();
						} while (oldCount == newCount && counter < 20);
					} catch (Exception e) {
						System.out.println(e.toString());
						commonLibrary.sleep(100000);
					}

					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(1), UIMAP_Home.lstTagListItems, 20);
					oldCount = lstTagAside.size();
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

					newCount = lstTagAside.size();
					counter = 0;
					try {
						do {
							commonLibrary.sleep(90000);
							counter++;
							lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
							newCount = lstTagAside.size();
						} while (oldCount == newCount && counter < 20);
					} catch (Exception e) {
						System.out.println(e.toString());
						commonLibrary.sleep(100000);
					}

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
				WebElement btnActions = commonLibrary.isExist(lstTagAside.get(2), UIMAP_Home.actionbar, 20);
				WebElement btnActions1 = commonLibrary.isExist(btnActions, UIMAP_Home.actionButton1, 20);
				if (btnActions1 != null) {
					commonLibrary.clickButtonParentWithWait(btnActions1, btnActions1.getText());
				}
				// List<WebElement> lstTagAside1 =
				// commonLibrary.isExistList(lstTagAside.get(2),
				// UIMAP_Home.topSourcesLi, 20);
				// for(WebElement link : lstTagAside1)
				// {
				// WebElement btnActions = commonLibrary.isExist(link,
				// UIMAP_Home.actionButton1, 20);
				// if(btnActions!=null)
				// {
				// if(btnActions.getText().toUpperCase().contains("ACTIONS FOR AGENCY ADJUDICATION"))
				// {
				//
				// commonLibrary.clickButtonParentWithWait(btnActions,
				// link.getText());
				// break;
				// }
				// }
				// }

				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary.isExist(lstTagAside2.get(2), UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement li : lstTagListItems) {
						WebElement anchor = commonLibrary.isExist(li, UIMAP_Home.button, 20);
						WebElement button = commonLibrary.isExist(li, UIMAP_Home.lnkTopicPod, 20);
						if (anchor != null && anchor.getText().contains(strAction)) {
							blnFive = true;
							if (browsername.contains("internet"))
								commonLibrary.clickJS(anchor, strAction);
							else
								commonLibrary.clickButtonParentWithWait(anchor, strAction);
							break;
						} else if (button != null && button.getText().contains(strAction)) {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : navigateToBrowserLinkClickHeader
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Home createTAClickCancel() {
		WebElement topicalert = commonLibrary.isExistNegative(UIMAP_Home.frmTopicAlert1, 20);
		topicalert = commonLibrary.isExistNegative(topicalert, By.tagName("footer"), 20);
		if (topicalert == null)
			topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert, 10);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			WebElement cancelAlert = commonLibrary.isExist(topicalert, By.cssSelector("input[data-action='cancel']"), 10);
			if (cancelAlert != null)
				commonLibrary.clickButtonParentWithWait(cancelAlert, "Cancel Alert");
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Navigating through Browser toolbars
	// # Function Name : navigateToBrowserLinkClickHeader
	// # Author : Aravind
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Search navigateToBrowserLinkClickHeader1(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction) {
		try {
			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
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
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting advanced search
	// # Function Name : FilterMenuSelection
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home applyFilter(String strToolbarMenuName) {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifyItemsInFindDocuments(String value, String dropdwn) {
		WebElement dropdown = commonLibrary.isExist(UIMAP_Home.srchFindDoc, 20);
		if (dropdown != null) {
			// commonLibrary.clickButton_Parent_WithWait(dropdown,
			// "Find documents that");
			commonLibrary.selectByVisibleTextByValue(dropdown, value, dropdwn);
		} else {
			report.updateTestLog("Verify " + value + " is selected", "dropdown is not present", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home enterText(String value) {
		WebElement text = commonLibrary.isExist(UIMAP_Home.txtSrch2, 20);
		if (text != null) {
			commonLibrary.setDataInTextBox(text, value, "SearchTerm");
		} else {
			report.updateTestLog("Enter " + value + " in text box", "textbox is not present", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home enterText1(String value) {
		WebElement text = commonLibrary.isExist(UIMAP_Home.txtSrch, 20);
		if (text != null) {
			commonLibrary.setDataInTextBox(text, value, "SearchTerm");
		} else {
			report.updateTestLog("Enter " + value + " in text box", "textbox is not present", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home enterText2(String value) {
		WebElement text = commonLibrary.isExist(UIMAP_Home.txtSrch1, 20);
		if (text != null) {
			commonLibrary.setDataInTextBox(text, value, "SearchTerm");
		} else {
			report.updateTestLog("Enter " + value + " in text box", "Second textbox is not present", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home clickaddtosearch() {
		WebElement text = commonLibrary.isExist(UIMAP_Home.addSearch, 20);
		if (text != null) {
			commonLibrary.clickButtonParentWithWait(text, "Add to Search");
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home selectNoOfWords(String value, String dropdwn) {
		WebElement dropdown = commonLibrary.isExist(UIMAP_Home.drpNoOfWords, 20);
		if (dropdown != null) {
			// commonLibrary.clickButton_Parent_WithWait(dropdown,
			// "Find documents that");
			commonLibrary.selectByVisibleTextByValue(dropdown, value, dropdwn);
		} else {
			report.updateTestLog("Verify " + value + " is selected", "no of words dropdown is not present", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home selectConnector(String value, String dropdwn) {
		WebElement dropdown = commonLibrary.isExist(UIMAP_Home.connector, 20);
		if (dropdown != null) {
			// commonLibrary.clickButton_Parent_WithWait(dropdown,
			// "Find documents that");
			commonLibrary.selectByVisibleTextByValue(dropdown, value, dropdwn);
		} else {
			report.updateTestLog("Verify " + value + " is selected", "connector dropdown is not present", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home clickaddtosearch1() {
		WebElement text = commonLibrary.isExist(UIMAP_Home.addSearch1, 20);
		if (text != null) {
			commonLibrary.clickButtonParentWithWait(text, "Add to Search");
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home verifySearchTerm(String value) {
		//
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		if (eltSearchbox != null) {
			if (eltSearchbox.getAttribute("value").trim().equalsIgnoreCase(value.trim())) {
				report.updateTestLog("Verify " + value + " is present in search field of red search box", "" + value + " is  present in search field of red search box", Status.PASS);
			} else {
				report.updateTestLog("Verify " + value + " is present in search field of red search box", "" + value + " is not present in search field of red search box", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify " + value + " is present in search field of red search box", "" + value + " is not present in search field of red search box", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting find that document dropdown
	// # Function Name : verifyItemsInFindDocuments
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home enterText3(String value) {
		WebElement text = commonLibrary.isExist(UIMAP_Home.txtSrch2, 20);
		if (text != null) {
			commonLibrary.setDataInTextBox(text, value, "SearchTerm");
		} else {
			report.updateTestLog("Enter " + value + " in text box", "textbox is not present", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in the word wheel.
	// # Function Name : verifyTextInWordWheel
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home verifyTextInWordWheel_1(String text) {

		generalFunctions.verifyTextInWordWheel_1(text);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Search clickTextInWordWheel(String text) {
		generalFunctions.clickTextInWordWheel(text);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickTextInWordWheel1(String text) {
		generalFunctions.clickTextInWordWheel(text);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot of current page
	// # Function Name : TakeScreenShot     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home takeScreenShot1(String strTCName, String strStep) {
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
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select topic, subtopic and click add
	// topic as search filter
	// documents
	// # Function Name : selectTopicAndAddAsSearchFilter     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home selectTopicAndAddAsSearchFilter(String strTopic, String strSubtopic, String downArrow, String parentMenuName) {

		try {
			Boolean blnFirst = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(parentMenuName)) {
							blnFirst = true;

							WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
							// commonLibrary.ScrollToView(button1);
							if (browsername.contains("internet")) {
								// commonLibrary.highlightElement(button1);
								commonLibrary.clickJS(button1, parentMenuName);
							} else
								commonLibrary.clickButtonParentWithWait(button1, parentMenuName);
							break;
						}

					}
				if (!blnFirst)
					report.updateTestLog("Click on " + parentMenuName, "Not Clicked on " + parentMenuName, Status.FAIL);
			}
			WebElement topicList = commonLibrary.isExist(UIMAP_Home.WintopicList);
			if (strTopic.equalsIgnoreCase("healthcare law")) {
				WebElement topic = commonLibrary.isExist(topicList, UIMAP_Home.healthcare, 10);
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(topic, "health care");
				} else {
					commonLibrary.clickButtonParentWithWait(topic, strTopic);
				}
				{
					report.updateTestLog("Click on " + strTopic + "", "" + strTopic + "is clicked", Status.PASS);
					commonLibrary.sleep(3000);
					WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
					List<WebElement> subtopic = commonLibrary.isExistList(topicList1, By.tagName("button"), 20);
					for (WebElement item : subtopic) {
						if (item.getText().toLowerCase().equals(strSubtopic.toLowerCase())) {
							if (browsername.contains("internet")) {
								commonLibrary.clickJS(item, strSubtopic);
							} else {
								commonLibrary.clickLinkWithWebElement(item, strSubtopic);
							}
							WebElement topicList2 = commonLibrary.isExist(UIMAP_Home.WintopicList);
							List<WebElement> down = commonLibrary.isExistList(topicList2, UIMAP_Home.downBtn, 20);
							for (WebElement item2 : down) {
								if (item2.isDisplayed()) {
									if (browsername.contains("internet")) {
										commonLibrary.clickJS(item2, downArrow);
										break;
									} else {
										commonLibrary.clickButtonParentWithWait(item2, downArrow);
										break;
									}
								}
							}
							WebElement topicList3 = commonLibrary.isExist(UIMAP_Home.WintopicList);
							List<WebElement> getdoc = commonLibrary.isExistList(topicList3, UIMAP_Home.addSearchFilter, 20);
							if (getdoc != null) {
								for (WebElement item4 : getdoc) {
									if (item4 != null && item4.isDisplayed()) {
										if (browsername.contains("internet")) {
											commonLibrary.clickJS(item4, "Add topic as a search filter");
											break;
										} else {
											commonLibrary.clickButtonParentWithWait(item4, "Add topic as a search filter");
											break;
										}
									}
								}
								break;
							} else {
								report.updateTestLog("Click on Add topic as a search filter", "Add topic as a search filterGet all documents for topic is not clicked", Status.PASS);
							}
						}
					}
				}
			} else if (commonLibrary.selectFromList(topicList, strTopic)) {
				report.updateTestLog("Click on " + strTopic + "", "" + strTopic + "is clicked", Status.PASS);
				commonLibrary.sleep(3000);
				WebElement topicList1 = commonLibrary.isExist(UIMAP_Home.WintopicList);
				List<WebElement> subtopic = commonLibrary.isExistList(topicList1, By.tagName("button"), 20);
				for (WebElement item : subtopic) {
					if (item.getText().toLowerCase().equals(strSubtopic.toLowerCase())) {
						if (browsername.contains("internet")) {
							commonLibrary.clickJS(item, strSubtopic);
						} else {
							commonLibrary.clickLinkWithWebElement(item, strSubtopic);
						}
						WebElement topicList2 = commonLibrary.isExist(UIMAP_Home.WintopicList);
						List<WebElement> down = commonLibrary.isExistList(topicList2, UIMAP_Home.downBtn, 20);
						for (WebElement item2 : down) {
							if (item2.isDisplayed()) {
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(item2, downArrow);
									break;
								} else {
									commonLibrary.clickButtonParentWithWait(item2, downArrow);
									break;
								}
							}
						}
						WebElement topicList3 = commonLibrary.isExist(UIMAP_Home.WintopicList);
						List<WebElement> getdoc = commonLibrary.isExistList(topicList3, UIMAP_Home.addSearchFilter, 20);
						if (getdoc != null) {
							for (WebElement item4 : getdoc) {
								if (item4 != null && item4.isDisplayed()) {
									if (browsername.contains("internet")) {
										commonLibrary.clickJS(item4, "Add topic as a search filter");
										break;
									} else {
										commonLibrary.clickButtonParentWithWait(item4, "Add topic as a search filter");
										break;
									}
								}
							}
							break;
						} else {
							report.updateTestLog("Click on Add topic as a search filter", "Add topic as a search filter is not clicked", Status.PASS);
						}
					}
				}
			} else {
				report.updateTestLog("Click on " + strTopic + "", "" + strTopic + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in the word wheel is
	// valid
	// # Function Name : verifyTextInWordWheelIsValid
	// # Author : Seetha
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Home verifyTextInWordWheelIsValid(String text) {

		boolean blnFlag = true;
		boolean blnFlag2 = false;
		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_Home.wordWheel, 10);
		List<WebElement> optionList = commonLibrary.isExistList(wordWheel, UIMAP_Home.lnkLinks, 10);
		for (WebElement option : optionList) {
			blnFlag2 = true;
			if (option.getText().contains(text)) {
				report.updateTestLog("Verify " + text + "displays anywhere in wordwheel is valid", "+text+ is invalid", Status.FAIL);
				blnFlag = false;
				break;
			}

		}
		if (blnFlag && blnFlag2)
			report.updateTestLog("Verify suggestions displays anywhere in wordwheel is valid", "all suggestions are valid", Status.PASS);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Vennila
	// # Date Created : 21 sep 16
	// #*****************************************************************************************************************************

	public Home verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickTextInWordWheelHome(String text) {
		generalFunctions.clickTextInWordWheel(text);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Research Page.
	// # Function Name : verifyResearchPage
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Home verifyResearchPage(String pageName) {
		generalFunctions.verifyCurrentPage(pageName);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserForward
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LitigationProfile clickBrowserForward() {
		commonLibrary.clickBrowserForward();
		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verifying startin dropdown is not
	// displayed
	// # Function Name : verifyStartinNotPresent
	// # Author : seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Home verifyStartinNotPresent() {

		WebElement startin = commonLibrary.isExist(UIMAP_Home.startIn, 20);
		if (startin != null)
			report.updateTestLog("Verify startin dropdown is not displayed", "Startin dropdown is displayed", Status.FAIL);
		else
			report.updateTestLog("Verify startin dropdown is not displayed", "Startin dropdown is not displayed", Status.PASS);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click archived code search
	// # Function Name : clickArchivedCodeSearch
	// # Author : seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public ArchivedSearch clickArchiVedCodeSearch() {
		WebElement archive = commonLibrary.isExist(UIMAP_Home.archivelink, 20);
		commonLibrary.clickButtonParentWithWait(archive, "Archived Codes Search");
		return new ArchivedSearch(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check
	// checkbox for Include Non Jurisdiction
	// # Function Name :includeNonJurisdiction    
	// # Author : Meera
	// # Date Created : Oct'15
	// #**********************************************

	public Home includeNonJurisdiction() {
		WebElement chkIncludeNonJurisdiction = commonLibrary.isExistNegative(UIMAP_Home.chkIncludeNonJurisdiction, 10);
		if (chkIncludeNonJurisdiction != null) {
			commonLibrary.setCheckBox(chkIncludeNonJurisdiction, true);
			report.updateTestLog("Select Include Non Jurisdiction.", "Include Non Jurisdiction is selected.", Status.DONE);
		} else
			report.updateTestLog("SelectInclude Non Jurisdiction.", "Include Non Jurisdiction is not selected.", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Wordwheel is displayed.
	// # Function Name : verifyWordWheel
	// # Author : Vennila
	// # Date Created : Oct '15
	// #*****************************************************************************************************************************

	public Home verifyWordWheel() {
		generalFunctions.verifyWordWheel();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select all link under All states
	// all states and territories
	// # Function Name : selectAllStatesTerrirtories
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Home selectAllStatesTerritories() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}
		filterMenuSelection("Jurisdiction");

		WebElement button = commonLibrary.isExistNegative(UIMAP_Home.lnkSelectAllStates, 10);
		if (button != null) {
			commonLibrary.clickLink(button, "All");
			report.updateTestLog("Select All link.", "All link for States and Territories is selected.", Status.DONE);
		} else
			report.updateTestLog("Select All link..", "All link for States and Territories is not selected.", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To Folders
	// # Function Name : navigateToFolders
	// # Author : Seetha
	// # Date Created : 26-Feb'15
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {

		// Function calling for simple search
		generalFunctions.navigateToFolders();

		return new WorkFolders(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to verify Show More Link
	// # Function Name : verifyShowMoreLinkLPABussPod     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPAHome verifyShowMoreLinkLPABussPod() {

		WebElement businessCommPod = commonLibrary.isExist(UIMAP_LPAHome.businesscommPod, 20);
		WebElement showMoreLink = commonLibrary.isExist(businessCommPod, UIMAP_LPAHome.showmorelessLink, 20);

		if (showMoreLink != null) {
			report.updateTestLog("Verify Show More Link is displayed", "Show More Link is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Show More Link is displayed", "Show More Link is not displayed", Status.FAIL);
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to apply filters
	// # Function Name : applySearchFilterWithoutClosingFilterpopup     
	// # Author : seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Home applySearchFilterWithoutClosingFilterpopup(String strToolbarMenuName, String strCondentTypes, String strlistname, String startstatus, String closestate, boolean state, boolean rffstate, boolean deleteflg) {

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

								} else {
									report.updateTestLog("Verify the close button exists", "Close button is not displayed as expected.", Status.FAIL);
								}

								// if (blnFlag2) {
								// WebElement menu =
								// commonLibrary.isExist(UIMAP_TOC.menubar, 20);
								// WebElement closebtn =
								// commonLibrary.isExist(menu,
								// UIMAP_TOC.cancelbtn, 20);
								// commonLibrary.clickButtonParentWithWait(closebtn,
								// "close filter");
								break;
								// }
							}

						}

					}
				}

			}

		}
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the star status
	// # Function Name : clickandverifystarFavouritestatus     
	// # Author : karthi
	// # Date Created :11 May '15
	// #*****************************************************************************************************************************
	public Home clickandverifystarFavouritestatus(String star_state) {
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
		} else {
			WebElement btnStar1 = commonLibrary.isExist(UIMAP_TOC.btnstarverifyLa, 20);
			if (btnStar1 != null) {
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
					if (btnStar1.getAttribute("class").contains("la-FavoriteFull")) {
						report.updateTestLog("Verify the Favourite star status", "Favourite star is selected as expected", Status.PASS);
					} else if (btnStar1.getAttribute("class").contains("la-FavoriteEmpty")) {
						report.updateTestLog("Verify the Favourite star status", "Favourite star is not selected as expected", Status.PASS);
					}
				}
			}
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and verify the filter details
	// in TOC page
	// # Function Name : applySearchFilter     
	// # Author : karthi
	// # Date Created :12 May '15
	// #*****************************************************************************************************************************
	public Home applySearchFilter(String strToolbarMenuName, String strCondentTypes, String strlistname, String startstatus, String closestate, boolean state, boolean rffstate, boolean deleteflg) {

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
	// # Function Description : Function to select tab and get status in Status
	// Report Pod
	// # Function Name : selectTabAndGetStatus     
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation selectTabAndGetStatus(String term, String citationManual, String exercise) {
		generalFunctions.selectTabAndGetStatus(term, citationManual, exercise);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Alerts pod
	// # Function Name : Click on Alerts pod
	// # Author : Vennila
	// # Date Created : 3 Nov'15
	// #*****************************************************************************************************************************

	public Alerts clickAlertpod() {

		WebElement eltAlertsPod = commonLibrary.isExistNegative(UIMAP_Home.eltAlertsPod, 10);
		WebElement btnExpandCollapse = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.btnExpandCollapse, 10);
		if (eltAlertsPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse, "Expand/Collapse");
			eltAlertsPod = commonLibrary.isExistNegative(UIMAP_Home.eltAlertsPod, 10);
			btnExpandCollapse = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.btnExpandCollapse, 10);

		}

		if (btnExpandCollapse != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is locatedat top left hand corner", "Ability to collopse/exand pod is locatedat top left hand corner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is locatedat top left hand corner", "Ability to collopse/exand pod is locatedat top left hand corner", Status.WARNING);

		WebElement eltAlertsFooter = commonLibrary.isExistNegative(eltAlertsPod, UIMAP_Home.eltPodFooter1, 10);
		WebElement lnkAlertsFooter = commonLibrary.isExistNegative(eltAlertsFooter, UIMAP_Home.lnkLinks, 10);

		if (lnkAlertsFooter.getText().contains("View all alerts")) {

			commonLibrary.clickJS(lnkAlertsFooter, "View all alerts");
			report.updateTestLog("Verify View all Alerts appears at the bottom ofthe pod", "View all Alerts appears at the bottom ofthe pod", Status.PASS);
			report.updateTestLog("Verify View all Alerts is a link", "View all Alerts is a link", Status.PASS);
		} else {
			report.updateTestLog("Verify View all Alerts appears at the bottom ofthe pod", "View all Alerts does not appear at the bottom ofthe pod", Status.FAIL);

		}

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the icw logo
	// # Function Name : Verify_ICWLogo   
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Home verifyICWLogo() {
		generalFunctions.verifyICWLogo();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to saving url to note pad
	// # Function Name : getCurrentUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Home getCurrentUrl(String excelData) {

		generalFunctions.getCurrentUrl(excelData);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Home clickTextInWordWheel2(String text) {
		generalFunctions.clickTextInWordWheel1(text);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying LAT search content in
	// history pod
	// # Function Name : verifyHistoryPodLATSearchContent
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home verifyHistoryPodLATSearchContent(String SearchTerm) {

		boolean flag = false;
		WebElement eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);

		WebElement btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnPracticeArea, 10);
		if (eltHistoryPod.getAttribute("class").contains("collapsed")) {
			commonLibrary.clickButtonLogSmallWait(btnExpandCollapse, "Expand/Collapse");
			eltHistoryPod = commonLibrary.isExistNegative(UIMAP_Home.eltHistoryPod, 10);
			btnExpandCollapse = commonLibrary.isExistNegative(eltHistoryPod, UIMAP_Home.btnPracticeArea, 10);

		}
		if (btnExpandCollapse != null)
			report.updateTestLog("Verify Ability to collopse/exand pod is located attop left hand corner ", "Ability to collopse/exand pod is located attop left hand corner", Status.PASS);
		else
			report.updateTestLog("Verify Ability to collopse/exand pod is located attop left hand corner ", "Ability to collopse/exand pod is not present", Status.FAIL);

		WebElement eltHistoryPodContent = commonLibrary.isExistNegative(UIMAP_Home.eltNewsPodContent, 10);
		List<WebElement> lnkHistoryPod = commonLibrary.isExistList(eltHistoryPodContent, UIMAP_Home.lnkLinks, 10);
		if (lnkHistoryPod.size() > 0) {

			for (int i = 0; i < lnkHistoryPod.size(); i++) {
				if (lnkHistoryPod.get(i).getText().contains(SearchTerm)) {
					flag = true;
					break;

				}
				if (flag) {
					report.updateTestLog("Verify search item " + SearchTerm + " is present as a link", SearchTerm + " item is present as a link", Status.FAIL);
				} else {
					report.updateTestLog("Verify search item " + SearchTerm + " is not present as a link", SearchTerm + " item is not present as a link", Status.PASS);
				}
			}
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod is present
	// # Function Name : verifyPodPresent
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home verifyPodPresent(String PodName) {

		boolean flag = false;
		WebElement PodSection = commonLibrary.isExistNegative(UIMAP_Home.podsSection1, 10);
		// List<WebElement> PodTitle =
		// commonLibrary.isExistList(UIMAP_Home.podspanheaders, 10);
		List<WebElement> PodList = commonLibrary.isExistList(PodSection, UIMAP_Home.pods, 10);

		for (WebElement pod : PodList) {
			WebElement PodTitle = commonLibrary.isExistNegative(pod, UIMAP_Home.podspanheaders, 10);
			if (PodTitle.getText().contains(PodName)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			report.updateTestLog("Verify " + PodName + " is displayed", PodName + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify " + PodName + " is displayed", PodName + " is not displayed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Recent & Favourites pod
	// and its contents
	// # Function Name : verifyRecentFavSourcePodContent
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home verifyRecentFavSourcePodContent(String PodName) {

		WebElement PodSection = commonLibrary.isExistNegative(UIMAP_Home.podsSection1, 10);
		List<WebElement> PodList = commonLibrary.isExistList(PodSection, UIMAP_Home.pods, 10);
		if (PodList.size() > 0) {
			for (WebElement pod : PodList) {
				WebElement PodTitle = commonLibrary.isExistNegative(pod, UIMAP_Home.podspanheaders, 10);
				if (PodTitle.getText().contains(PodName)) {

					WebElement PodContent = commonLibrary.isExistNegative(UIMAP_Home.podContent, 10);
					List<WebElement> PodContentList = commonLibrary.isExistList(PodContent, UIMAP_Home.liclass, 10);

					if (PodContentList.size() > 0 && PodContentList.size() <= 5) {
						WebElement PodShowMore = commonLibrary.isExistNegative(UIMAP_Home.showMoreButton, 10);
						if (PodShowMore.isDisplayed()) {
							report.updateTestLog("Verify More button is displayed", "More button is displayed under " + PodName, Status.PASS);
						} else {
							report.updateTestLog("Verify More button is displayed", "More button is not displayed under " + PodName, Status.FAIL);
						}

					} else {
						report.updateTestLog("Verify " + PodName + " has 5 contents", PodName + " did not have 5 contents, in actual it has " + PodContentList.size(), Status.FAIL);
					}

				}
			}
		} else {
			report.updateTestLog("Verify " + PodName + " is displayed", PodName + " is not displayed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying State Specific source pod
	// content
	// # Function Name : verifyStateSpecificSourcePodContent
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home verifyStateSpecificSourcePodContent(String PodName, String rowItems) {
		boolean flag = false;
		WebElement PodSection = commonLibrary.isExistNegative(UIMAP_Home.podsSection1, 10);
		List<WebElement> PodList = commonLibrary.isExistList(PodSection, UIMAP_Home.pods, 10);

		String[] rowValues = rowItems.split(";");

		if (PodList.size() > 0) {
			for (WebElement pod : PodList) {
				WebElement PodTitle = commonLibrary.isExistNegative(pod, UIMAP_Home.podspanheaders, 10);

				if (PodTitle.getText().contains(PodName)) {
					WebElement PodContent = commonLibrary.isExistNegative(UIMAP_Home.stateSpecificContent, 10);
					List<WebElement> PodContentList = commonLibrary.isExistList(PodContent, UIMAP_Home.liclass, 10);

					if (PodContentList.size() > 0 && (PodContentList.size() == rowValues.length)) {
						for (int i = 0; i < rowValues.length; i++) {
							for (int j = 0; j < PodContentList.size(); j++) {
								if (rowValues[i].contains(PodContentList.get(j).getText())) {
									flag = true;
									break;
								} else {
									flag = false;
								}
							}
							if (flag) {
								report.updateTestLog("Verify " + rowValues[i] + " is present in the " + PodTitle.getText() + " list", rowValues[i] + " is present in the " + PodTitle.getText() + "list", Status.PASS);
							} else {
								report.updateTestLog("Verify " + rowValues[i] + " is present in the " + PodTitle.getText() + " list", rowValues[i] + "is not present in the " + PodTitle.getText() + "list", Status.FAIL);
							}
						}
					}

					else {
						report.updateTestLog("Verify Contents size", "Size are not matching, expected was " + rowValues + "but it is " + PodContentList.size(), Status.FAIL);
					}
				}
			}
		} else {
			report.updateTestLog("Verify " + PodName + " is displayed", PodName + " is not displayed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Multi State source pod
	// content
	// # Function Name : verifyMultiStateSourcePodContent
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home verifyMultiStateSourcePodContent(String PodName, String rowItems) {
		boolean flag = false;
		WebElement PodSection = commonLibrary.isExistNegative(UIMAP_Home.podsSection1, 10);
		List<WebElement> PodList = commonLibrary.isExistList(PodSection, UIMAP_Home.pods, 10);

		String[] rowValues = rowItems.split(";");

		if (PodList.size() > 0) {
			for (WebElement pod : PodList) {
				WebElement PodTitle = commonLibrary.isExistNegative(pod, UIMAP_Home.podspanheaders, 10);

				if (PodTitle.getText().contains(PodName)) {
					WebElement PodContent = commonLibrary.isExistNegative(UIMAP_Home.multiStateContent, 10);
					List<WebElement> PodContentList = commonLibrary.isExistList(PodContent, UIMAP_Home.liclass, 10);

					if (PodContentList.size() > 0) {
						for (int i = 0; i < rowValues.length; i++) {
							for (int j = 0; j < PodContentList.size(); j++) {
								if (rowValues[i].contains(PodContentList.get(j).getText())) {
									flag = true;
									break;
								} else {
									flag = false;
								}
							}
							if (flag) {
								report.updateTestLog("Verify " + rowValues[i] + " present in the " + PodTitle.getText() + " list", rowValues[i] + " is present in the " + PodTitle.getText() + "list", Status.PASS);
							} else {
								report.updateTestLog("Verify contents are matching in the " + PodTitle.getText() + " list", rowValues[i] + "is not present in the " + PodTitle.getText() + "list", Status.FAIL);
							}
						}
					}

					else {
						report.updateTestLog("Verify Contents size", "Size are not matching, expected was " + rowValues + "but it is " + PodContentList.size(), Status.FAIL);
					}
				}
			}
		} else {
			report.updateTestLog("Verify " + PodName + " is displayed", PodName + " is not displayed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Table of Contents link from
	// search word wheel
	// # Function Name : clickTOCfromWordWheel
	// # Author : Nancy
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public TOC clickTOCfromWordWheel(String text) {

		Boolean flag = false;
		WebElement wordWheel1 = commonLibrary.isExist(UIMAP_Home.wordWheel, 10);

		List<WebElement> wordList = commonLibrary.isExistList(wordWheel1, UIMAP_Home.lstTagListItems, 10);
		for (int i = 0; i <= wordList.size(); i++) {
			if (wordList.get(i).getText().contains(text)) {
				WebElement wordLink = commonLibrary.isExist(wordList.get(i), UIMAP_Home.wordLink, 10);
				commonLibrary.clickLink(wordLink, "TOC");
				flag = true;
				break;
			}

			else {
				report.updateTestLog(text + " should not be displayed in wordwheel", text + " is not displayed in wordwheel", Status.DONE);
			}

		}

		if (flag)
			report.updateTestLog("TOC page for " + text + "should be displayed", "TOC page for " + text + " is displayed", Status.DONE);

		else
			report.updateTestLog("TOC page for" + text + "should be not be displayed", "TOC page for " + text + " is not displayed", Status.FAIL);

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Public Record
	// page by clicking Lexis Advance arrow
	// # Function Name : absenceofPR     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #************************************************************************************************
	public Home absenceofPR() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Public Records");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Public Records");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("public records")) {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Home.btnPublicRecords, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", "Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Public Records logo displays in the Top left corner of Global menu", "Lexis Advance® Public Records logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		}
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : verify the absence of Alert WF Notif MyLexis in
	// More option
	// # Function Name : verifythepresenceofalertWFinmore
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Dec' 15
	// #*****************************************************************************************************************************

	public Home verifytheabsenceofalertinmore() {

		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore);
		if (btnMore != null) {
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
		}

		commonLibrary.sleep(30000);
		WebElement wf = commonLibrary.isExist(UIMAP_Home.folderBtn);

		if (wf == null) {

			report.updateTestLog("Verify Workfolder not displays in the More dropdown", "Workfolder not displays in the More dropdown", Status.PASS);
		} else {
			report.updateTestLog("verify Workfolder displays in the More dropdown", "Workfolder not displays in the More dropdown", Status.FAIL);
		}

		{
			commonLibrary.sleep(30000);
			WebElement alert = commonLibrary.isExist(UIMAP_Home.alertBtn);

			if (alert == null) {

				report.updateTestLog("Verify alert not displays in the More dropdown", "Alert not displays in the More dropdown", Status.PASS);

			} else {
				report.updateTestLog("verify alert displays in the More dropdown", "Alert not displays in the More dropdown", Status.FAIL);

			}

			{
				commonLibrary.sleep(30000);
				WebElement notif = commonLibrary.isExist(UIMAP_Home.notifBtn);

				if (notif == null) {

					report.updateTestLog("Verify notif not displays in the More dropdown", "notif  not displays in the More dropdown", Status.PASS);

				} else {
					report.updateTestLog("verify notif displays in the More dropdown", "notif  not displays in the More dropdown", Status.FAIL);

				}

				{
					commonLibrary.sleep(30000);
					WebElement myLexis = commonLibrary.isExist(UIMAP_Home.myLexisBtn);

					if (myLexis == null) {

						report.updateTestLog("Verify myLexis not displays in the More dropdown", "myLexis not displays in the More dropdown", Status.PASS);

					} else {
						report.updateTestLog("verify myLexis displays in the More dropdown", "myLexis not displays in the More dropdown", Status.FAIL);

					}

					return new Home(scriptHelper);

				}
			}

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod is present in Landing
	// Page
	// # Function Name : verifyPodPresence
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home verifyPodPresence(String podName, boolean podPresence, boolean rmLinkPresence) {
		boolean flag = false;
		if (podPresence) {
			List<WebElement> homePagePods = commonLibrary.isExistList(UIMAP_Home.homePagePods, 10);
			if (homePagePods.size() == 0)
				homePagePods = commonLibrary.isExistList(UIMAP_Home.homePagePods1, 10);
			for (WebElement pod : homePagePods) {
				WebElement podHeader = commonLibrary.isExistNegative(pod, UIMAP_Home.podHeader, 5);
				if (podHeader != null && podHeader.getText().toLowerCase().contains(podName.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + podName + " is displayed in landing page", podName + " is displayed in landing page", Status.PASS);
					if (podName.toLowerCase().contains("history")) {
						if (rmLinkPresence) {
							WebElement rmLink = commonLibrary.isExistNegative(pod, UIMAP_Home.rmLink, 10);
							if (rmLink != null)
								report.updateTestLog("Verify 'RESEARCH MAP' LINK DISPLAYS IN 'HISTORY' POD", "'RESEARCH MAP' LINK DISPLAYS IN 'HISTORY' POD", Status.PASS);
							else
								report.updateTestLog("Verify 'RESEARCH MAP' LINK DISPLAYS IN 'HISTORY' POD", "'RESEARCH MAP' LINK DOES NOT DISPLAY IN 'HISTORY' POD", Status.FAIL);
						} else {
							WebElement rmLink = commonLibrary.isExistNegative(pod, UIMAP_Home.rmLink, 10);
							if (rmLink != null)
								report.updateTestLog("Verify 'RESEARCH MAP' LINK DOES NOT DISPLAY IN 'HISTORY' POD", "'RESEARCH MAP' LINK DISPLAYS IN 'HISTORY' POD", Status.FAIL);
							else
								report.updateTestLog("Verify 'RESEARCH MAP' LINK DOES NOT DISPLAY IN 'HISTORY' POD", "'RESEARCH MAP' LINK DOES NOT DISPLAY IN 'HISTORY' POD", Status.PASS);
						}
					}
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + podName + " is displayed in landing page", podName + " does not display in landing page", Status.FAIL);
			if (podName.toLowerCase().contains("latest updates"))
				verifyBottomLeftPod();
			if (podName.toLowerCase().contains("support"))
				verifyBottomRightPod();
		} else {
			List<WebElement> homePagePods = commonLibrary.isExistList(UIMAP_Home.homePagePods, 10);
			if (homePagePods.size() == 0)
				homePagePods = commonLibrary.isExistList(UIMAP_Home.homePagePods1, 10);
			for (WebElement pod : homePagePods) {
				WebElement podHeader = commonLibrary.isExistNegative(pod, UIMAP_Home.podHeader, 5);
				if (podHeader != null && podHeader.getText().toLowerCase().contains(podName.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + podName + " is not displayed in landing page", podName + " is displayed in landing page", Status.FAIL);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + podName + " is not displayed in landing page", podName + " does not display in landing page", Status.PASS);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Latest Updates pod is the
	// bottom left pod in landing page.
	// # Function Name : verifyBottomLeftPod
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Home verifyBottomLeftPod() {
		WebElement leftPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltLeftSidePods, 10);
		if (leftPodsCont == null)
			leftPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltLeftSidePods1, 10);
		List<WebElement> leftPods = commonLibrary.isExistList(leftPodsCont, UIMAP_Home.homePagePods, 10);
		if (leftPods.size() == 0)
			leftPods = commonLibrary.isExistList(leftPodsCont, UIMAP_Home.homePagePods1, 10);
		WebElement bottomPodHeader = commonLibrary.isExistNegative(leftPods.get(leftPods.size() - 1), UIMAP_Home.podHeader, 5);
		if (bottomPodHeader.getText().toLowerCase().contains("latest updates"))
			report.updateTestLog("Verify Latest Updates pod is displayed as the left-bottom pod in landing page", "Latest Updates pod is displayed as the left-bottom pod in landing page", Status.PASS);
		else
			report.updateTestLog("Verify Latest Updates pod is displayed as the left-bottom pod in landing page", "Latest Updates pod is not displayed as the left-bottom pod in landing page", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Support pod is the bottom
	// left pod in landing page.
	// # Function Name : verifyBottomRightPod
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Home verifyBottomRightPod() {
		WebElement rightPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltRightSidePods, 10);
		if (rightPodsCont == null)
			rightPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltRightSidePods1, 10);
		List<WebElement> rightPods = commonLibrary.isExistList(rightPodsCont, UIMAP_Home.homePagePods, 10);
		if (rightPods.size() == 0)
			rightPods = commonLibrary.isExistList(rightPodsCont, UIMAP_Home.homePagePods1, 10);
		WebElement bottomPodHeader = commonLibrary.isExistNegative(rightPods.get(rightPods.size() - 1), UIMAP_Home.podHeader, 5);
		if (bottomPodHeader.getText().toLowerCase().contains("support"))
			report.updateTestLog("Verify Support pod is displayed as the right-bottom pod in landing page", "Support pod is displayed as the right-bottom pod in landing page", Status.PASS);
		else
			report.updateTestLog("Verify Support pod is displayed as the right-bottom pod in landing page", "Support pod is not displayed as the right-bottom pod in landing page", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying link presence in more
	// dropdown menu.
	// # Function Name : verifyMoreDropdownOptions
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Home verifyMoreDropdownOptions(String option, boolean presence) {
		if (presence) {
			boolean flag = false;
			WebElement moreDropdownContainer = commonLibrary.isExistNegative(UIMAP_Home.moreDropdownContainer, 10);
			List<WebElement> moreLinks = commonLibrary.isExistList(moreDropdownContainer, UIMAP_Home.lstTagListItems, 5);
			for (WebElement link : moreLinks) {
				if (link.getText().toLowerCase().contains(option.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + option + " is displayed in More Dropdown Menu.", option + " is displayed in More Dropdown Menu.", Status.PASS);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + option + " is displayed in More Dropdown Menu.", option + " is not displayed in More Dropdown Menu.", Status.FAIL);
		} else {
			boolean flag = false;
			WebElement moreDropdownContainer = commonLibrary.isExistNegative(UIMAP_Home.moreDropdownContainer, 10);
			List<WebElement> moreLinks = commonLibrary.isExistList(moreDropdownContainer, UIMAP_Home.lstTagListItems, 5);
			for (WebElement link : moreLinks) {
				if (link.getText().toLowerCase().contains(option.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + option + " is not displayed in More Dropdown Menu.", option + " is displayed in More Dropdown Menu.", Status.FAIL);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + option + " is not displayed in More Dropdown Menu.", option + " is not displayed in More Dropdown Menu.", Status.PASS);
		}
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying the re arranged position
	// of the landing page pods.
	// # Function Name : verifyChangedPodPosition
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Home verifyChangedPodPosition() {
		WebElement leftPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltLeftSidePods, 10);
		if (leftPodsCont == null)
			leftPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltLeftSidePods1, 10);
		List<WebElement> leftPods = commonLibrary.isExistList(leftPodsCont, UIMAP_Home.homePagePods, 10);
		if (leftPods.size() == 0)
			leftPods = commonLibrary.isExistList(leftPodsCont, UIMAP_Home.homePagePods1, 10);
		String header = commonLibrary.isExist(leftPods.get(0), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("history"))
			report.updateTestLog("Verify History pod dispays below the search box in Left side.", "History pod dispays below the search box in Left side", Status.PASS);
		else
			report.updateTestLog("Verify History pod dispays below the search box in Left side.", "History pod does not dispay below the search box in Left side", Status.FAIL);

		header = commonLibrary.isExist(leftPods.get(1), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("folders"))
			report.updateTestLog("Verify Folders pod displays below the History pod.", "Folders pod displays below the History pod", Status.PASS);
		else
			report.updateTestLog("Verify Folders pod displays below the History pod.", "Folders pod does not display below the History pod", Status.FAIL);

		header = commonLibrary.isExist(leftPods.get(2), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("news"))
			report.updateTestLog("Verify News pod displays below the Folders pod.", "News pod displays below the Folders pod", Status.PASS);
		else
			report.updateTestLog("Verify News pod displays below the Folders pod.", "News pod does not display below the Folders pod", Status.FAIL);

		header = commonLibrary.isExist(leftPods.get(3), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("latest updates"))
			report.updateTestLog("Verify Latest Updates pod displays below the News pod.", "Latest Updates pod displays below the News pod", Status.PASS);
		else
			report.updateTestLog("Verify Latest Updates pod displays below the News pod.", "Latest Updates pod does not display below the News pod", Status.FAIL);

		WebElement rightPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltRightSidePods, 10);
		if (rightPodsCont == null)
			rightPodsCont = commonLibrary.isExistNegative(UIMAP_Home.eltRightSidePods1, 10);
		List<WebElement> rightPods = commonLibrary.isExistList(rightPodsCont, UIMAP_Home.homePagePods, 10);
		if (rightPods.size() == 0)
			rightPods = commonLibrary.isExistList(rightPodsCont, UIMAP_Home.homePagePods1, 10);

		header = commonLibrary.isExist(rightPods.get(0), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("favorites"))
			report.updateTestLog("Verify Favorites pod displays below the search on the right side.", "Favorites pod displays below the search on the right side", Status.PASS);
		else
			report.updateTestLog("Verify Favorites pod displays below the search on the right side.", "Favorites pod does not display below the search on the right side", Status.FAIL);

		header = commonLibrary.isExist(rightPods.get(1), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("archives"))
			report.updateTestLog("Verify Archives pod displays below the Favorites pod.", "Archives pod displays below the Favorites pod", Status.PASS);
		else
			report.updateTestLog("Verify Archives pod displays below the Favorites pod.", "Archives pod does not display below the Favorites pod", Status.FAIL);

		header = commonLibrary.isExist(rightPods.get(2), UIMAP_Home.podHeader, 10).getText();
		if (header.toLowerCase().contains("support"))
			report.updateTestLog("Verify Support pod displays below the Archives pod.", "Support pod displays below the Archives pod.", Status.PASS);
		else
			report.updateTestLog("Verify Support pod displays below the Archives pod.", "Support pod does not display below the Archives pod.", Status.FAIL);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click More dropdown.
	// # Function Name : clickMore
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home clickMore() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {

			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");

		}
		commonLibrary.sleep(500000);
		WebElement moreDropdownContainer = commonLibrary.isExistNegative(UIMAP_Home.moreDropdownContainer, 10);
		if (moreDropdownContainer == null || !moreDropdownContainer.isDisplayed()) {
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
			commonLibrary.sleep(500000);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying History Dropdown
	// Presence.
	// # Function Name : verifyHistoryDropdown
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home verifyHistoryDropdown() {
		WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
		if (btnIdHistoryMenuArrow == null) {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DOES NOT DISPLAYS' IN GLOBAL MENU.", Status.FAIL);
		} else {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", Status.PASS);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify date format in news pod
	// # Function Name : verifyDateFormat1
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Practice verifyDateFormat1()// Feb 10, 2015 05:12:07 p.m. EST
	{
		boolean blnFlag = false;
		String strSource = null;
		try {
			WebElement newsPod = commonLibrary.isExist(UIMAP_PracticePage.newspod, 20);

			List<WebElement> allULList = commonLibrary.isExistList(newsPod, UIMAP_PracticePage.ul, 20);
			if (allULList.size() > 0) {

				List<WebElement> newsCommPodList = commonLibrary.isExistList(allULList.get(0), UIMAP_PracticePage.actionList1, 20);
				List<WebElement> date = commonLibrary.isExistList(newsCommPodList.get(1), UIMAP_Home.spanDate1, 10);
				{
					for (WebElement item1 : date) {
						strSource = item1.getText();
						Date dates = new SimpleDateFormat("yyyy-MM-dd").parse(strSource);
						if (dates != null) {
							blnFlag = true;
						} else {
							blnFlag = false;
							break;
						}
					}
				}
				// }
			}
			if (blnFlag) {
				report.updateTestLog("Verify Date is displayed below the article in the expected format", "Date is displayed below the article in the expected format", Status.PASS);
			} else {
				report.updateTestLog("Verify Date is displayed below the article in the expected format", "Date is not displayed below the article in the expected format", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to Verify number of links in
	// News Pod
	// # Function Name : verifyLinkCountNewsPod()     
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLinkCountNewsPod() {

		WebElement newsPod = commonLibrary.isExist(UIMAP_PracticePage.newspod, 20);

		List<WebElement> allULList = commonLibrary.isExistList(newsPod, UIMAP_PracticePage.ul, 20);
		if (allULList.size() > 0) {

			List<WebElement> businessCommPodList = commonLibrary.isExistList(allULList.get(0), UIMAP_PracticePage.actionList1, 20);

			if (businessCommPodList.size() == 7) {
				report.updateTestLog("Verify Top five news title specific to Military Justice  from Legal News content displays  ", "Five news titles are displayed in news pod", Status.PASS);
			} else {
				report.updateTestLog("Verify Top five news title specific to Military Justice  from Legal News content displays  ", "Five news titles are not displayed in news pod", Status.FAIL);
			}
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod is not displayed
	// # Function Name : verifyFoldersPodNotDisplayed
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home verifyFoldersPodNotDisplayed() {

		WebElement PodSection = commonLibrary.isExistNegative(UIMAP_Home.pod, 10);
		WebElement foldersPod = commonLibrary.isExistNegative(PodSection, UIMAP_Home.foldersPod, 10);
		if (foldersPod == null)
			report.updateTestLog("Verify Folders pod is not displayed in landing page", "Folders pod is not displayed in landing page", Status.PASS);
		else
			report.updateTestLog("Verify Folders pod is not displayed in landing page", "Folders pod is displayed in landing page", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clik <View All Legal News> link
	// displays
	// at the bottom of the pod and click the link
	// # Function Name : verifyViewAllLegalNewsAndClk()
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search verifyViewAllLegalNewsAndClk() {
		boolean blnFlag = false;

		WebElement newsPod = commonLibrary.isExist(UIMAP_PracticePage.newspod, 20);
		List<WebElement> allULList = commonLibrary.isExistList(newsPod, UIMAP_PracticePage.ul, 20);
		if (allULList.size() > 0) {

			List<WebElement> newsCommPodList = commonLibrary.isExistList(allULList.get(0), UIMAP_PracticePage.actionList1, 20);
			if (newsCommPodList.size() > 0) {
				// WebElement news = commonLibrary.isExist(UIMAP_Home.divNews,
				// 20);
				// if (news != null) {
				// List<WebElement> li = commonLibrary.isExistList(news,
				// UIMAP_SearchResult.lnkHeader, 10);
				List<WebElement> allNews = commonLibrary.isExistList(newsCommPodList.get(6), UIMAP_PracticePage.viewAll, 20);
				for (WebElement item : allNews) {
					if (item.getText().equalsIgnoreCase("View all legal news")) {
						commonLibrary.clickLink(item, "View all legal news");
						blnFlag = true;

					}
				}
			}
		}
		if (blnFlag) {

			report.updateTestLog("Verify View all news link displays at the bottom of the pod and Clicked", "View all news link is displayed at the bottom of the pod and Selected", Status.PASS);

		} else {

			report.updateTestLog("Verify View all news link displays at the bottom of the pod and Clicked", "View all news link is displayed at the bottom of the pod and Selected", Status.FAIL);

		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on Document's red Stop
	// image
	// # Function Name : navigateToBrowserLink1AndRightClick    
	// # Author : Bharath VMP
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickRightAndVerifyNewWindow(WebElement element) {

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

				report.updateTestLog("Open in new tab option is present", browsername, Status.FAIL);
			else

				report.updateTestLog("Open in new tab option is not present", browsername, Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	public Home navigateToBrowserLink1AndRightClick(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName, String strThridSubMenuName, String strAction) {

		try {

			Boolean blnFirst = false, blnSecond = false, blnThird = false, blnFour = false, blnFive = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			clickRightAndVerifyNewWindow(btnIdBrowse);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);

			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							blnFirst = true;
							WebElement button1 = commonLibrary.isExistNegative(button, UIMAP_Home.btnTagListItems, 10);
							if (browsername.contains("internet")) {
								// commonLibrary.highlightElement(button1);
								commonLibrary.clickJS(button1, strParentMenuName);

								clickRightAndVerifyNewWindow(button1);

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
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(button1, strFirstSubMenuName);
								} else
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
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(button1, strSecondSubMenuName);

								} else
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
				WebElement btnActions = commonLibrary.isExist(lstTagAside.get(2), UIMAP_Home.btnTypeButton, 20);
				// commonLibrary.highlightElement(btnActions);
				commonLibrary.clickButtonParentWithWait(btnActions, "ACTIONS FOR AGENCY ADJUDICATION");

				WebElement divIdBrowserSubMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserSubMenu2, UIMAP_Home.lstTagAside, 20);
				WebElement divClassActionsList = commonLibrary.isExist(lstTagAside1.get(2), UIMAP_Home.divClassActionsList, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(divClassActionsList, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strAction)) {
							blnFive = true;
							// WebElement button1 =
							// commonLibrary.isExist_Negative(button,
							// UIMAP_HomePage.btnTagListItems, 10);
							if (browsername.contains("internet"))
								commonLibrary.clickJS(button, strAction);
							else
								commonLibrary.clickButtonParentWithWait(button, strAction);
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
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switching between Tabs
	// # Function Name : switchTabSources
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax switchTabSources1(String tabName) {
		boolean flag = false;
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		if (taxTabUl != null) {
			List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl, UIMAP_LexisAdvanceTax.li, 10);
			if (taxTabLiList.size() > 0) {
				for (int i = 0; i < taxTabLiList.size(); i++) {
					WebElement button = commonLibrary.isExist(taxTabLiList.get(i), UIMAP_LexisAdvanceTax.button, 10);
					if (button != null) {
						if (button.getText().toLowerCase().contains(tabName.toLowerCase())) {
							commonLibrary.clickButtonParentWithWait(button, button.getText());

							WebElement resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
							int count = 0;
							do {
								count++;
								resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
								if (resultsList == null)
									commonLibrary.sleep(5000);
							} while (resultsList == null && count <= 40);

							if (resultsList != null) {
								report.updateTestLog("Verify sources library page displays", "Sources library page is displayed", Status.PASS);
							}
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting advanced search
	// # Function Name : FilterMenuSelection
	// # Author : seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Practice applyFilterAS(String strToolbarMenuName) {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click advanced search option before
	// Big red search box .
	// # Function Name : clickadvancedsearch
	// # Author : Thangavel M
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Practice clickadvancedsearch() {

		WebElement divclasshead = commonLibrary.isExist(UIMAP_Home.divclasshead, 10);
		WebElement divclass = commonLibrary.isExist(divclasshead, UIMAP_Home.divclass, 10);
		WebElement ulclass = commonLibrary.isExist(divclasshead, UIMAP_Home.ulclass, 10);
		WebElement advancedsearch = commonLibrary.isExist(divclasshead, UIMAP_Home.advancedsearch, 10);
		if (advancedsearch != null) {
			commonLibrary.clickJS(advancedsearch);
			report.updateTestLog("Click on advanced search ", " Clicked on advanced search ", Status.PASS);
		} else
			report.updateTestLog("Click on advanced search ", "Not Clicked on advanced search ", Status.FAIL);

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify applied prefilter in generic
	// search page
	// # Function Name : verifyprefiltersearch1     
	// # Author : Sankar
	// # Date Created : Jan'16
	// #*****************************************************************************************************
	public Home verifyprefiltersearch1(String strprefiltersearch1) {
		commonLibrary.sleep(10);
		WebElement div = commonLibrary.isExist(UIMAP_Home.div, 20);
		WebElement span = commonLibrary.isExist(div, UIMAP_Home.span, 20);
		WebElement prefiltersearch1 = commonLibrary.isExist(span, UIMAP_Home.prefilter1, 20);

		if (prefiltersearch1 != null && prefiltersearch1.getText().contains(strprefiltersearch1))
			report.updateTestLog("Verify " + strprefiltersearch1 + " is displayed in BRSB in generic search page", strprefiltersearch1 + "is displayed in BRSB in generic search page", Status.PASS);
		else
			report.updateTestLog("Verify " + strprefiltersearch1 + "is displayed in BRSB in generic search page", strprefiltersearch1 + " is not displayed in BRSB in generic search page", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : verify the Advanced Search link in landing page
	// # Function Name : verifyAdvanceSearchFormlink
	// # Author : Ramesh
	// # Date Created : Jan' 16
	// #*****************************************************************************************************************************

	public Practice verifyAdvanceSearchFormlink(String Text) {

		WebElement eltSearchbox1 = commonLibrary.isExist(UIMAP_Home.btndatasearch, 20);

		if (eltSearchbox1 != null && eltSearchbox1.getText().contains(Text))
			// Advanced Search
			report.updateTestLog("Verify 'Advanced search' link displays at the top right corner in Landing page", "'Advanced search' link displays at the top right corner in Landing page", Status.PASS);
		else
			report.updateTestLog("Verify 'Advanced Search' link displays at the top right corner in Landing page pod", "'Advanced search' link displays at the top right corner in Landing page pod", Status.FAIL);
		commonLibrary.clickJS(eltSearchbox1);
		commonLibrary.sleep(1000);
		return new Practice(scriptHelper);

	}
	// #*****************************************************************************************************************************
		// # Function Description : Function to navigate to Settings page
		// # Function Name : navigateToSettings
		// # Author : toya
		// # Date Created : Feb'16
		// #*****************************************************************************************************************************


	//#*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : applySearchFilters
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Home applySearchFilters(String strToolbarMenuName, String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");
		int filters = arrCondentType.length;
		/*if (strCondentTypes.toLowerCase().contains("u.s. federal"))
			filters--;
		if (strCondentTypes.toLowerCase().contains("states & territories"))
			filters--;*/
		int count = 0;
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

		case "Category": {
			// blnFlag=false;
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_Home.divClassCTFilters, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								count++;
								// blnFlag=true;
								break;
							}

						}
				}

			}

			break;
		}
		case "Jurisdiction": {
			// blnFlag=false;
			// By Court
			for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
				WebElement divClassFederalFilters = commonLibrary.isExist(UIMAP_Home.divClassFederalFilters, 20);
				if (divClassFederalFilters != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassFederalFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}
			if (count < filters) {
				// By circuit
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassFederalFilters = commonLibrary.isExist(UIMAP_Home.divClassFederalFilters, 20);
					if (divClassFederalFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassFederalFilters, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 1st Column
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					/*if (divClassStateFilters != null) {
						WebElement h2Tag = commonLibrary.isExistNegative(divClassStateFilters, UIMAP_Home.eltPodHeader, 20);
						WebElement spanTag = commonLibrary.isExistNegative(h2Tag, UIMAP_Home.spanTag, 20);
						if (spanTag != null)
								if (spanTag.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(spanTag, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							
					}
*/
				}
			}
			if (count < filters) {
				// State Filters 1st Column
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 2nd Column
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				// State Filters 3rd Column
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					if (divClassStateFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(2), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			
			
			if (count < filters) {
				// State Filters 1st Column
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
					/*WebElement divClassStateAllPreFilters = commonLibrary.isExist(divClassStateFilters, UIMAP_Home.divStateFilters, 20);
					if (divClassStateAllPreFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateAllPreFilters, UIMAP_Home.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									commonLibrary.scrollDown();
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									commonLibrary.scrollUp();
									break;
								}

							}
					}
*/
				}
			}
			break;
		}
		case "Practice Areas & Topics": {
			// blnFlag=false;
			// Column 1
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
				if (divClassPracticeArea != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}

			// column 2
			if (count < filters) {
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
					if (divClassPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}

			break;
		}
		}
		if (count == filters)
			report.updateTestLog("select check boxes near " + strCondentTypes, "check boxes near " + strCondentTypes + " are selected", Status.PASS);
		else
			report.updateTestLog("select check boxes near " + strCondentTypes, " check boxes near " + strCondentTypes + " are not selected", Status.FAIL);
		return new Home(scriptHelper);
	}
		//#*****************************************************************************************************************************
		// # Function Description : Function to verify the search everything button along with rekated federal and non jurisdictional content
		// # Function Name : applySearchFilters
		// # Author : Jubin
		// # Date Created : Jan'16
		// #*****************************************************************************************************************************

		public Home verifySearchEverything(String strToolbarMenuName, String strCondentTypes, boolean state) {
			String[] arrCondentType = strCondentTypes.split(";");
			int count = 0;
			boolean blnFlag1 = false, blnFlag2 = false;
			int filters = arrCondentType.length;
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

			case "Category": {
				// blnFlag=false;
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_Home.divClassCTFilters, 20);
					if (divClassCTFilters != null) {
						WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)
							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									count++;
									break;
								}

							}
					}

				}

				break;
			}
			case "Jurisdiction": {
				if (count < filters) {
					for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
						WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
						if (divClassStateFilters != null) {
							WebElement h2Tag = commonLibrary.isExistNegative(divClassStateFilters, UIMAP_Home.eltPodHeader, 20);
							/*WebElement spanTag = commonLibrary.isExistNegative(h2Tag, UIMAP_Home.spanTag, 20);
							if (spanTag != null)
									if (spanTag.getText().contains(arrCondentType[i])) {
										blnFlag1=true;
										count++;
										break;
									}	*/	
						}

					}
				}
					if (count < filters) {
					for (int i = 0; i < arrCondentType.length; i++) {
						WebElement divClassStateFilters = commonLibrary.isExist(UIMAP_Home.divClassStateFilters, 20);
						/*WebElement divClassStateAllPreFilters = commonLibrary.isExist(divClassStateFilters, UIMAP_Home.divStateFilters, 20);
						if (divClassStateAllPreFilters != null) {
							List<WebElement> lstTagUList = commonLibrary.isExistList(divClassStateAllPreFilters, UIMAP_Home.lstTagUList, 20);
							List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
							if (lstTagListItems.size() > 0)
								for (WebElement item : lstTagListItems) {
									if (item.getText().contains(arrCondentType[i])) {
										blnFlag2=true;
										commonLibrary.scrollDown();
										count++;
										break;
									}

								}
						}*/

					}
				}
				break;
			}
			case "Practice Areas & Topics": {
				// blnFlag=false;
				// Column 1
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
					if (divClassPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_Home.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}

				// column 2
				if (count < filters) {
					for (int i = 0; i < arrCondentType.length; i++) {
						WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_Home.divClassPracticeArea, 20);
						if (divClassPracticeArea != null) {
							List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_Home.lstTagUList, 20);
							List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_Home.lstTagListItems, 20);
							if (lstTagListItems.size() > 0)

								for (WebElement item : lstTagListItems) {
									if (item.getText().contains(arrCondentType[i])) {
										// blnFlag=true;
										count++;
										WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
										commonLibrary.setCheckBox(Checkbox, state);
										break;
									}

								}
						}

					}
				}

				break;
			}
			}
			if (blnFlag1 && blnFlag2 && count == filters)
				report.updateTestLog("select check boxes near " + strCondentTypes, "check boxes near " + strCondentTypes + " are selected", Status.PASS);
			else
				report.updateTestLog("select check boxes near " + strCondentTypes, " check boxes near " + strCondentTypes + " are not selected", Status.FAIL);
			return new Home(scriptHelper);
		}
		
		// #*****************************************************************************************************************************
		// # Function Description : Function for doing search
		// # Function Name : simpleSearchSetClientId     
		// # Author : Anbu
		// # Date Created : Sep'15
		// #*****************************************************************************************************************************

		public Search simpleSearchSetClientId(String strSearchTerm, Boolean strClearFilter) {

			try {

				WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
				// eltSearchbox.sendKeys(strSearchTerm);
				commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "searchterm");
				commonLibrary.sleep(50000);

				if (strClearFilter) {
					WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
					commonLibrary.highlightElement(btnClassFilter);
					commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

					WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
					if (btnClassClearFilter == null) {
						btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
						commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
					}
					commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

					WebElement btnIdSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);

					if (browsername.contains("internet"))
						commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
				} else {
					WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
					commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
				}

				WebElement pageHeader = commonLibrary.isExist(UIMAP_Home.pageHeader, 10);
				if (pageHeader != null && pageHeader.getText().trim().toLowerCase().contains("set/add client id")) {
					WebElement RecentMatterradio = commonLibrary.isExist(UIMAP_Home.btnRecentMatter, 20);
					if (RecentMatterradio != null) 
					{
						commonLibrary.clickButtonParentWithWait(RecentMatterradio, "Recent Matter Radio button");
						commonLibrary.sleep(3000);
						WebElement SelectMatterradio = commonLibrary.isExist(UIMAP_Home.selRecentMatter, 20);

						if (SelectMatterradio != null) 
						{
							Select select = new Select(SelectMatterradio);
							if (select.getOptions().size() > 1) 
							{

								for (int i = 0; i < select.getOptions().size();) 
								{
									if ((select.getOptions().get(i).getText().equalsIgnoreCase("-None-"))) 
									{
										select.selectByIndex(i + 1);
										break;
									} else 
									{
										select.selectByIndex(i);
										break;
									}

								}
							}
						}
					}
					else 
					{

						String strClientId = "123.ABC";
						WebElement newMatterradio = commonLibrary.isExist(UIMAP_Home.btnNewMatter, 20);
						commonLibrary.clickButton(newMatterradio);

						// ENTER A <<<CLIENT OR MATTER>>> ID IN TEXT BOX
						WebElement id = commonLibrary.isExist(UIMAP_Home.txtClientid, 20);
						if (commonLibrary.setDataInTextBox(id, strClientId, "New Client Id")) {
							report.updateTestLog("Enter a client or matter id", "Client or matter id is entered as " + strClientId + "", Status.PASS);
						} else {
							report.updateTestLog("Enter a client or matter id", "Client or matter id is not entered as " + strClientId + "", Status.FAIL);
						}
					}
					commonLibrary.sleep(3000);
					WebElement setMatter = commonLibrary.isExist(UIMAP_Home.btnSetMatter, 20);
					commonLibrary.highlightElement(setMatter);
					commonLibrary.clickButtonParentWithWait(setMatter, "Set Matter ID");
					commonLibrary.sleep(10000);
					if (commonLibrary.isExist(UIMAP_Home.invalidClientid) != null) {
						report.updateTestLog("The Matter ID entered does not match the format set by your firm.", "Error Message: The Matter ID entered does not match the format set by your firm.", Status.WARNING);
						WebElement setCancel = commonLibrary.isExist(UIMAP_Home.submitCancel, 20);
						commonLibrary.highlightElement(setCancel);
						commonLibrary.clickButtonParentWithWait(setCancel, "Submit cancel button");
						commonLibrary.sleep(5000);
					}
				}

//				commonLibrary.documentState(driver);
//				check = pageCheck.positiveCheck(driver, "Search", "Search Page");
//				pageCheck.handleFlow(driver, check);
//				WebElement resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
//				int count = 0;
//				do {
//					count++;
//					commonLibrary.sleep(10000);
//					resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
//				} while (resultClass == null && count < 40);

			} catch (Exception e) {
				System.out.println(e.toString());

			}
			return new Search(scriptHelper);
		}
	
	}
		
	


