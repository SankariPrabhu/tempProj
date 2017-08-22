package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPSHome;
import UIMAP.UIMAP_SearchResult;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LitigationProfile extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	public LitigationProfile(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpshome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lpshome") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpshome")) {
			throw new IllegalStateException("Litigation Profile home page is expected, but not displayed!");
		}
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
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Dinesh
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************

	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LitigationProfile rightClickProductLogoOpenInNewWindow() {

		try {
			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			int PrevSize = driver.getWindowHandles().size();

			Actions action = new Actions(driver);
			action.moveToElement(CurrentProduct);
			// commonLibrary.highlightElement(CurrentProduct);
			commonLibrary.sleep(2000);

			String browsername = cap.getBrowserName();
			if (browsername.equalsIgnoreCase("firefox")) {
				action.contextClick(CurrentProduct).sendKeys("W").build().perform();
				commonLibrary.sleep(4000);
			} else if (browsername.equalsIgnoreCase("internet explorer")) {
				action.contextClick(CurrentProduct).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
				commonLibrary.sleep(4000);
			} else if (browsername.contains("chrome")) {
				action.keyDown(Keys.SHIFT).click(CurrentProduct).keyUp(Keys.SHIFT).perform();
				commonLibrary.sleep(4000);
			}
			report.updateTestLog("Right click on Lexis Advance Product Logo", "Right click popup displays ", Status.PASS);
			report.updateTestLog("Verification of Right Click popup", "Right click popup displayed", Status.PASS);
			report.updateTestLog("Veification of 'Open in new window' link", "'Open in new window' link is displayed", Status.PASS);
			int NewSize = driver.getWindowHandles().size();
			int count = 0;
			if (NewSize > PrevSize) {
				report.updateTestLog("Click on Open in New Window link in Rightclick menu", "Secondary browser is displayed", Status.PASS);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
					if (CurrentProduct.getText().contains("Litigation Profile Suite")) {
						count++;
					}
				}
				if (count >= 2) {
					report.updateTestLog("Litigation Profile Suite landing page is displayed in the secondary browser", "Litigation Profile Suite landing page is displayed in the secondary browser", Status.PASS);
				} else {
					report.updateTestLog("Litigation Profile Suite landing page is displayed in the secondary browser", "Litigation Profile Suite landing page is not displayed in the secondary browser", Status.FAIL);
				}

				driver.close();
				report.updateTestLog("CLose the secondary browser", "Secondary browser is closed", Status.PASS);
				commonLibrary.switchToWindow("lpshome");
			} else {
				report.updateTestLog("Secondary browser is displayed", "Secondary browser is not displayed", Status.FAIL);
			}

			return new LitigationProfile(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click LPS report.
	// # Function Name : clickReport
	// # Author : Dinesh
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LPSReport clickReport(String strDocTitle) {
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

			}
		}
		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Medmal Navigator page
	// by clicking Lexis Advance arrow
	// # Function Name : NavigateToMedmalNavigatorPage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public MedmalNavigator navigateToMedmalNavigatorPage() {
		try {
			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);

			WebElement btnLexisAdvanceMedmal = commonLibrary.isExist(UIMAP_Home.btnActionMedMal, 20);
			commonLibrary.clickLinkWithWebElement(btnLexisAdvanceMedmal, "Lexis Advance® Medmal Navigator");
			commonLibrary.sleep(3000);

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("MedMal Navigator®")) {
				report.updateTestLog("MedMal Navigator® landing page is displayed", "MedMal Navigator® landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("MedMal Navigator® landing page is displayed", "MedMal Navigator® landing page is not displayed", Status.FAIL);
			}

			return new MedmalNavigator(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for searching in Litigation Profile
	// page.
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LPResults simpleSearch(String strSearchTerm) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
		if (eltSearchbox != null) {
			eltSearchbox.clear();
			// Entering search term in search text box
			commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");
		}
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdprofileSuiteSearch, 20);
		// /commonLibrary.highlightElement(eltSearchbutton);
		// clicking search button
		if (browsername.contains("internet"))
			commonLibrary.clickJS(eltSearchbutton, "Search");
		else
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		if (driver.getCurrentUrl().contains("lpsresults")) {
			report.updateTestLog("Navigate to Litigation Profile Suite results page", "Litigation Profile Suite results  page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Navigate to Litigation Profile Suite results page", "Litigation Profile Suite results  page is  not displayed", Status.FAIL);
		}

		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Verdict Settlements
	// page.
	// # Function Name : navigateToverdictsettlementPage     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements navigateToverdictsettlementPage() {
		try {
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

			return new VerdictsSettlements(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for select option from Entity Dropdown
	// # Function Name : selectEntity     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LitigationProfile selectEntity(String option) {
		WebElement entityDropdown = commonLibrary.isExistNegative(UIMAP_LPSHome.entityDropdown1, 10);

		// Clicking the entity dropdown
		commonLibrary.clickButtonLogSmallWait(entityDropdown, "Entity Dropdown");
		boolean flag = false;
		WebElement dropdownList = commonLibrary.isExistNegative(UIMAP_LPSHome.dropdownList1, 10);

		if (dropdownList != null && dropdownList.isDisplayed()) {
			List<WebElement> optionList = commonLibrary.isExistList(dropdownList, UIMAP_LPSHome.listItem, 10);
			for (WebElement item : optionList) {
				if (item.getText().toLowerCase().contains(option.toLowerCase())) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_LPSHome.button, 10);

					// selecting entity in the dropdown
					commonLibrary.clickButtonParentWithWait(button, option);
					flag = true;
					break;

				}
			}
			if (!flag)
				report.updateTestLog("Click " + option + " under Entity dropdown.", option + " not present under Entity dropdown.", Status.FAIL);
		} else
			report.updateTestLog("Click " + option + " under Entity dropdown.", "Entity dropdown is not displayed.", Status.FAIL);

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Select Pre-Filters in LPS home page
	// # Function Name : selectPreFilter     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LitigationProfile selectPreFilter(String header, String filters) {
		int i = 0, j = 0;
		boolean flag = false, flag1 = false;
		String[] optionsList = filters.split(";");

		WebElement filterButton = commonLibrary.isExistNegative(UIMAP_LPSHome.filterButton1, 10);
		if (!filterButton.getAttribute("class").toLowerCase().contains("selected"))
			commonLibrary.clickButtonLogSmallWait(filterButton, "Filter");
		else
			report.updateTestLog("Click filter button.", "Filter popup is already open.", Status.DONE);

		WebElement preFilterContainer = commonLibrary.isExistNegative(UIMAP_LPSHome.preFilterContainer, 10);

		if (preFilterContainer.isDisplayed()) {
			WebElement filterList = commonLibrary.isExistNegative(preFilterContainer, UIMAP_LPSHome.filterList, 10);
			List<WebElement> headers = commonLibrary.isExistList(filterList, UIMAP_LPSHome.listItem, 10);
			List<WebElement> filterGroup = commonLibrary.isExistList(filterList, UIMAP_LPSHome.filterGroup, 10);
			for (i = 0; i < headers.size(); i++) {
				if (headers.get(i).getText().toLowerCase().contains(header.toLowerCase())) {
					commonLibrary.clickButtonLogSmallWait(headers.get(i), header);
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Click filter header" + header, "Filter header" + header + " is not present.", Status.DONE);
			else {
				for (j = 0; j < optionsList.length; j++) {
					flag1 = false;
					WebElement parent = null;
					if (header.toLowerCase().contains("jurisdictions") && !(filters.toLowerCase().contains("states (& territories)"))) {
						if (i > 0) {
							parent = commonLibrary.isExistNegative(filterGroup.get(1), By.tagName("div"), 10);
						} else
							parent = commonLibrary.isExistNegative(filterGroup.get(0), By.tagName("div"), 10);
					} else {
						if (i > 0)
							parent = filterGroup.get(1);
						else
							parent = filterGroup.get(0);
					}
					List<WebElement> lists = commonLibrary.isExistList(parent, UIMAP_LPSHome.uList, 10);
					for (WebElement li : lists) {
						List<WebElement> options = commonLibrary.isExistList(li, UIMAP_LPSHome.listItem, 10);
						for (WebElement item : options) {
							if (item.getText().toLowerCase().contains(optionsList[j].toLowerCase())) {
								WebElement checkBox = commonLibrary.isExistNegative(item, UIMAP_LPSHome.input, 10);
								commonLibrary.setCheckBox(checkBox, optionsList[j]);
								// count++;
								flag1 = true;
								break;
							}
						}
						if (flag1)
							break;
					}
					if (!flag1)
						report.updateTestLog("Select prefilter " + optionsList[j], "Prefilter " + optionsList[j] + " is not selected.", Status.FAIL);
				}
			}

		} else
			report.updateTestLog("Click filter button.", "Filter popup is not displayed.", Status.DONE);
		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Search with option selected from WorldWheel
	// # Function Name : wordwheelSearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LPResults wordWheelSearch(String searchTerm, String wordWheelTerm, String verifyTerm) {
		boolean flag = false;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
		try {
			commonLibrary.sleep(500000);
		} catch (Exception e) {
			System.out.println(e.toString());

		}

		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_LPSHome.wordWheel, 10);
		List<WebElement> options = commonLibrary.isExistList(wordWheel, UIMAP_LPSHome.listItem, 10);
		for (WebElement li : options) {
			if (li.getText().toLowerCase().contains(wordWheelTerm.toLowerCase()) && li.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is present in wordwheel.", Status.PASS);
				WebElement link = commonLibrary.isExistNegative(li, UIMAP_LPSHome.link, 10);
				commonLibrary.clickLinkWithWait(link, li.getText());
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is not present in wordwheel.", Status.FAIL);
		else {
			eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
			if (eltSearchbox.getAttribute("value").toLowerCase().contains(verifyTerm.toLowerCase()))
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is displayed in SearchBox.", Status.PASS);
			else
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is not displayed in SearchBox.", Status.FAIL);

			WebElement searchButton = commonLibrary.isExistNegative(UIMAP_LPSHome.searchButton, 10);
			commonLibrary.clickButtonParentWithWait(searchButton, "Search");
			// WebElement searchResultContainer = commonLibrary.isExistNegative(
			// UIMAP_LPSHome.searchResultContainer, 5);
			// int count =0;
			//
			// do{
			// commonLibrary.sleep(500000);
			// count++;
			// searchResultContainer = commonLibrary.isExistNegative(
			// UIMAP_LPSHome.searchResultContainer, 3);
			// }while(searchResultContainer==null && count<20);

		}
		return new LPResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : To verify whether
	// # Function Name : wordwheelSearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public LitigationProfile verifyDefaultEntity() {

		WebElement defaultProfileType = commonLibrary.isExist(UIMAP_LPSHome.defaultProfileType, 20);

		if (defaultProfileType.getText().contains("Attorney")) {
			report.updateTestLog("Verify 'Attorney' is the default value in 'Profile Type'drop down in Search Box", "'Attorney' is the default value in 'Profile Type'drop down in Search Box", Status.PASS);
		} else {
			report.updateTestLog("Verify 'Attorney' is the default value in 'Profile Type'drop down in Search Box", "'Attorney' is not the default value in 'Profile Type'drop down in Search Box", Status.FAIL);
		}
		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserForward
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPResults clickBrowserForward() {
		commonLibrary.clickBrowserForward();
		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Lps home Page.
	// # Function Name : verifyLpsPage
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LitigationProfile verifyLpsPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
			report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is not displayed", Status.FAIL);
		}

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Home clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDown
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LitigationProfile verifypreFilterDropDownDisplayed() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.filterArrow, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDownselected
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LitigationProfile verifypreFilterDropDownselected() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.filterArrowSelected, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify filter is collapsed after clicking
	// # Function Name : clickpreFiltercollpase
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LitigationProfile clickpreFiltercollpase() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.filterArrowSelected, 20);
		if (btnClassFilter != null) {
			commonLibrary.clickJS(btnClassFilter, "Filter");
		}
		WebElement btnClassFilterCollapse = commonLibrary.isExist(UIMAP_LPSHome.filterArrow, 20);
		if (btnClassFilter.equals(btnClassFilterCollapse)) {
			report.updateTestLog("Click on button filter", "Filter dropdown is collapsed", Status.PASS);
		} else {
			report.updateTestLog("Click on button filter", "Filter dropdown is not collapsed", Status.FAIL);
		}

		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function sto apply Jurisdiction
	// # Function Name :vsaApplyJurisdiction
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LitigationProfile lpsApplyJurisdiction(String filter, String Entity) {
		boolean blnFlag = false;

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.filterArrow, 20);
		if (btnClassFilter != null) {
			commonLibrary.clickJS(btnClassFilter, "Filter");
		}

		if (Entity.equalsIgnoreCase("Expert Witness")) {
			WebElement locationFilter = commonLibrary.isExist(UIMAP_LPSHome.locations, 20);
			if (locationFilter != null) {
				commonLibrary.clickJS(locationFilter);
			} else {
				report.updateTestLog("Select Location filter", "Filter is not selected", Status.FAIL);
			}
		}
		// WebElement filterType =
		// commonLibrary.isExist(UIMAP_LPSHome.filterType, 20);
		// WebElement ulFilterType = commonLibrary.isExist(filterType,
		// UIMAP_LPSHome.ulFilterType, 20);
		// WebElement jurisdiction = commonLibrary.isExist(ulFilterType,
		// UIMAP_LPSHome.filterLocations, 20);

		// List<WebElement> lstTagUList =
		// commonLibrary.isExistList(jurisdiction, UIMAP_LPSHome.ulTaglist1,
		// 20);
		// if (lstTagUList.size() > 0)
		// {
		// for (WebElement item1 : lstTagUList)
		// {
		// List<WebElement> lstTagListItems =
		// commonLibrary.isExistList(jurisdiction, UIMAP_LPSHome.liTagList, 20);

		// if (lstTagListItems.size() > 0)
		// for (int i=0; i<=lstTagListItems.size(); i++) {
		// WebElement filterLabel =
		// commonLibrary.isExist(lstTagListItems.get(i),
		// UIMAP_LPSHome.filterLable, 20);

		// if
		// ((filterLabel.getText().toLowerCase()).contains(filter.toLowerCase()))
		// {
		// WebElement Checkbox = commonLibrary.isExist(lstTagListItems.get(i),
		// UIMAP_LPSHome.jurisCheckbox, 20);
		// commonLibrary.setCheckBox(Checkbox, true);
		// blnFlag = true;
		// break;
		// }

		// }
		// if(blnFlag)
		// break;
		// }
		// }

		WebElement filterType = commonLibrary.isExist(UIMAP_LPSHome.filterType, 20);
		WebElement ulFilterType = commonLibrary.isExist(filterType, UIMAP_LPSHome.ulFilterType, 20);
		WebElement jurisdiction = commonLibrary.isExist(ulFilterType, UIMAP_LPSHome.filterLocations, 20);
		if (jurisdiction != null) {
			List<WebElement> lstTagUList = commonLibrary.isExistList(jurisdiction, UIMAP_LPSHome.ulTaglist, 20);

			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(2), UIMAP_LPSHome.liTagList, 20);
			if (lstTagListItems.size() > 0)

				for (WebElement item : lstTagListItems) {
					if (item.getText().contains(filter)) {
						// blnFlag=true;

						WebElement Checkbox = commonLibrary.isExist(item, UIMAP_LPSHome.jurisCheckbox, 20);
						commonLibrary.setCheckBox(Checkbox, true);
						break;
					}

				}
		}
		if (blnFlag)
			report.updateTestLog("Set filter check box", "Filter is checked", Status.PASS);
		else {
			report.updateTestLog("Set filter check box", "Filter is not checked", Status.FAIL);
		}

		return new LitigationProfile(scriptHelper);
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
	// # Function Description : click with bolded option selected from
	// WorldWheel
	// # Function Name : clickBoldWordWheelSearch1     
	// # Author : Niraj
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LitigationProfile clickBoldWordWheelSearch1(String searchTerm, String wordWheelTerm, String verifyTerm) {
		boolean flag = false;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
		try {
			commonLibrary.sleep(500000);
		} catch (Exception e) {
			System.out.println(e.toString());

		}

		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_LPSHome.wordWheel, 10);
		List<WebElement> options = commonLibrary.isExistList(wordWheel, UIMAP_LPSHome.listItem, 10);
		for (WebElement li : options) {
			if (li.getText().toLowerCase().contains(wordWheelTerm.toLowerCase()) && li.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is present in wordwheel.", Status.PASS);
				WebElement link = commonLibrary.isExistNegative(li, UIMAP_LPSHome.link, 10);
				commonLibrary.clickJSMouseMove(link, searchTerm);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is not present in wordwheel.", Status.FAIL);
		else {
			eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
			if (eltSearchbox.getAttribute("value").toLowerCase().contains(verifyTerm.toLowerCase()))
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is displayed in SearchBox.", Status.PASS);
			else
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is not displayed in SearchBox.", Status.FAIL);

		}
		return new LitigationProfile(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Clear the search box
	// # Function Name : clearsearchbox()     
	// # Author : Niraj
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LitigationProfile clearSearchBox() {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);

		eltSearchbox.clear();
		return new LitigationProfile(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Search with option selected from WorldWheel and
	// return to the same page
	// # Function Name : wordwheelSearch1     
	// # Author : Niraj
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LitigationProfile wordWheelSearch1(String searchTerm, String wordWheelTerm, String verifyTerm) {
		boolean flag = false;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
		try {
			commonLibrary.sleep(500000);
		} catch (Exception e) {
			System.out.println(e.toString());

		}

		WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_LPSHome.wordWheel, 10);
		List<WebElement> options = commonLibrary.isExistList(wordWheel, UIMAP_LPSHome.listItem, 10);
		for (WebElement li : options) {
			if (li.getText().toLowerCase().contains(wordWheelTerm.toLowerCase()) && li.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
				report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is present in wordwheel.", Status.PASS);
				WebElement link = commonLibrary.isExistNegative(li, UIMAP_LPSHome.link, 10);
				commonLibrary.clickJSMouseMove(link, wordWheelTerm);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is not present in wordwheel.", Status.FAIL);
		else {
			eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdprofileSuiteTextbox1, 20);
			if (eltSearchbox.getAttribute("value").toLowerCase().contains(verifyTerm.toLowerCase()))
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is displayed in SearchBox.", Status.PASS);
			else
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is not displayed in SearchBox.", Status.FAIL);

		}
		return new LitigationProfile(scriptHelper);

	}

}
