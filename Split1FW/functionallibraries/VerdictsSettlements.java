package functionallibraries;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_VerdictsSettlements;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

public class VerdictsSettlements extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public VerdictsSettlements(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("vsahome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("vsahome") && counter < 40);
		if (!driver.getCurrentUrl().contains("vsahome")) {
			throw new IllegalStateException("Verdicts & Settlements Analyzer home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public VerdictsSettlements rightClickProductLogoOpenInNewWindow() {
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
				action.contextClick(CurrentProduct).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();
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
					if (CurrentProduct.getText().contains("Verdict & Settlement Analyzer")) {
						count++;
					}
				}
				if (count >= 2) {
					report.updateTestLog("Verdicts & Settlements Analyzer landing page is displayed in the secondary browser", "Verdicts & Settlements Analyzer landing page is displayed in the secondary browser", Status.PASS);
				} else {
					report.updateTestLog("Verdicts & Settlements Analyzer landing page is displayed in the secondary browser", "Verdicts & Settlements Analyzer landing page is not displayed in the secondary browser", Status.FAIL);
				}

				driver.close();
				report.updateTestLog("Close the secondary browser", "Secondary browser is closed", Status.PASS);
				commonLibrary.switchToWindow("vsahome");
			} else {
				report.updateTestLog("Click on Open in New Window link in Rightclick menu", "Secondary browser is not displayed", Status.FAIL);
			}

			return new VerdictsSettlements(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing VSA search
	// # Function Name : VerdictSearch     
	// # Author : Ram Prasath
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public VSASearchResults verdictSearch(String strSearchTerm) {
		try {
			WebElement txtIdVerdictTextbox = commonLibrary.isExist(UIMAP_Home.txtIdVerdictTextbox, 20);
			commonLibrary.setDataInTextBox(txtIdVerdictTextbox, strSearchTerm, "SearchTerm");
			commonLibrary.sleep(50);
			commonLibrary.isExist(UIMAP_Home.btnIdVerdictSearch, 20);
			// commonLibrary.highlightElement(btnIdVerdictSearch);
			commonLibrary.clickButtonSubmit(UIMAP_Home.btnIdVerdictSearch, "Search");
		} catch (Exception e) {

		}

		return new VSASearchResults(scriptHelper);
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
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public VSASearchResults simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_VerdictsSettlements.txtIdSearch, 20);
		if (eltSearchbox != null) {
			commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");

			if (strClearFilter) {

				WebElement filterIdCount = commonLibrary.isExistNegative(UIMAP_VerdictsSettlements.filterCount, 20);
				if (filterIdCount != null && !filterIdCount.getText().equals("")) {

					WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterButton, 20);
					commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

					WebElement preFilterContainer = commonLibrary.isExistNegative(UIMAP_VerdictsSettlements.preFilterContainer, 10);
					WebElement btnClassClearFilter = commonLibrary.isExist(preFilterContainer, UIMAP_VerdictsSettlements.clearFilterButton, 20);

					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(btnClassClearFilter, "Clear");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

					WebElement btnIdSubSearch = commonLibrary.isExist(preFilterContainer, UIMAP_VerdictsSettlements.subSearchButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(btnIdSubSearch, "Search");
					else
						commonLibrary.clickButtonParentWithWait(btnIdSubSearch, "Search");
				} else {
					WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_VerdictsSettlements.searchButton, 20);

					if (browsername.contains("internet")) {
						try {
							driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
							commonLibrary.clickMouseMoveAction(eltSearchbutton, "Search");
						} catch (TimeoutException ex) {
							driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
							System.out.println(ex.toString());

						}
						driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
					} else
						commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
				}
			} else {

				WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_VerdictsSettlements.searchButton, 20);
				if (eltSearchbutton != null) {
					if (browsername.contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(eltSearchbutton, "Search");
					else
						commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
				} else {

					report.updateTestLog("Click On Search Button", "Search Button is not Clicked", Status.FAIL);
				}
			}
		}
		int count = 0;
		WebElement h2 = commonLibrary.isExist(UIMAP_VerdictsSettlements.h2class, 20);
		do {
			count++;
			h2 = commonLibrary.isExist(UIMAP_VerdictsSettlements.h2class, 20);
			commonLibrary.sleep(5000);
		} while (h2 == null && count < 20);
		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing VSA search
	// # Function Name : VerdictSearch     
	// # Author : Ram Prasath
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public VSASearchResults verdictSearch_Resultspage(String strSearchTerm) {
		try {
			WebElement txtIdVerdictTextbox = commonLibrary.isExist(UIMAP_Home.txtIdtaxlSearch, 20);
			commonLibrary.setDataInTextBox(txtIdVerdictTextbox, strSearchTerm, "SearchTerm");
			commonLibrary.sleep(50);
			// txtIdVerdictTextbox.sendKeys(Keys.ENTER);
			WebElement btnIdVerdictSearch = commonLibrary.isExist(UIMAP_Home.btnIdprofileSuiteSearch, 20);
			// commonLibrary.highlightElement(btnIdVerdictSearch);
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(btnIdVerdictSearch, "Search");
			} else {
				commonLibrary.clickButtonParentWithWait(btnIdVerdictSearch, "Search");
			}
			// commonLibrary.clickButton_submit(UIMAP_HomePage.btnIdVerdictSearch,"Search");
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new VSASearchResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click PreFilter button
	// # Function Name : clickPreFilter
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements clickPreFilter() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterButton, 20);
		if (btnClassFilter != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		} else {

			report.updateTestLog("Click On filter Button", "filter Button is not Clicked", Status.FAIL);

		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements applySearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		vsaapplySearchFilter(strToolbarMenuName, strCondentTypes, state);

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name :VsaFilterMenuSelection
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public void vsaapplySearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");
		int filters = arrCondentType.length;
		if (strCondentTypes.toLowerCase().contains("u.s. federal"))
			filters--;
		if (strCondentTypes.toLowerCase().contains("all states"))
			filters--;
		int count = 0;
		// Boolean blnFlag=false; Modified by Pratik

		switch (strToolbarMenuName) {

		case "Jurisdictions": {

			WebElement menuItem = commonLibrary.isExist(UIMAP_VerdictsSettlements.jurisdictionsmenu, 20);
			if (!(menuItem.getAttribute("class").contains("selected"))) {
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(menuItem, "Filter");
				} else {
					commonLibrary.clickButtonParentWithWait(menuItem, "Filter");
				}
			}

			for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
				WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
				if (divClassJurisdicationFilters != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_VerdictsSettlements.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}
			if (count < filters) {
				// By circuit
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
					if (divClassJurisdicationFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
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
					WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
					if (divClassJurisdicationFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
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
					WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
					if (divClassJurisdicationFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
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
					WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
					if (divClassJurisdicationFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(2), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									break;
								}

							}
					}

				}
			}
			if (count < filters) {
				commonLibrary.scrollDown();
				// State Filters Footer Links
				for (int i = 0; i < arrCondentType.length; i++) { // blnFlag=false;
					WebElement divClassJurisdicationFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
					if (divClassJurisdicationFilters != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassJurisdicationFilters, UIMAP_VerdictsSettlements.lstTagUList, 20);

						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(3), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									commonLibrary.scrollDown();
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
									commonLibrary.setCheckBox(Checkbox, state);
									commonLibrary.scrollUp();
									break;
								}

							}
					}

				}
			}
			break;
		}
		case "Practice Area & Topic": {

			WebElement menuItem = commonLibrary.isExist(UIMAP_VerdictsSettlements.practicemenu, 20);
			if (!(menuItem.getAttribute("class").contains("selected"))) {
				if (browsername.contains("internet")) {
					commonLibrary.clickJS(menuItem, "Practice Area & topic");
				} else {
					commonLibrary.clickButtonParentWithWait(menuItem, "Filter");
				}
			}

			// blnFlag=false;
			// Column 1
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_VerdictsSettlements.divClassPracticeArea, 20);
				if (divClassPracticeArea != null) {
					List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_VerdictsSettlements.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_VerdictsSettlements.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)

						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								// blnFlag=true;
								count++;
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								break;
							}

						}
				}

			}

			// column 2
			if (count < filters) {
				for (int i = 0; i < arrCondentType.length; i++) {
					WebElement divClassPracticeArea = commonLibrary.isExist(UIMAP_VerdictsSettlements.divClassPracticeArea, 20);
					if (divClassPracticeArea != null) {
						List<WebElement> lstTagUList = commonLibrary.isExistList(divClassPracticeArea, UIMAP_VerdictsSettlements.lstTagUList, 20);
						List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(1), UIMAP_VerdictsSettlements.lstTagListItems, 20);
						if (lstTagListItems.size() > 0)

							for (WebElement item : lstTagListItems) {
								if (item.getText().contains(arrCondentType[i])) {
									// blnFlag=true;
									count++;
									WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.chkTypeCheckbox, 20);
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

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filters Selected
	// # Function Name : verifyFilterSelected
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements verifyFilterSelected(String strSelectedFilterOptions) {
		boolean flag = false;

		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");

		WebElement divClassSelectedPreFilters = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassSelectedPrefilter, 20);
		if (divClassSelectedPreFilters != null) {
			List<WebElement> lstTagUList = commonLibrary.isExistList(divClassSelectedPreFilters, UIMAP_VerdictsSettlements.lstTagOList, 20);

			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList.get(0), UIMAP_VerdictsSettlements.lstTagListItems, 20);
			if (lstTagListItems.size() > 0)
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {
					for (WebElement item : lstTagListItems) {
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", Status.PASS);
							flag = true;

							break;
						}
					}
					if (!flag) {
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy textbox", Status.FAIL);
					}

				}

		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDown
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements verifypreFilterDropDownDisplayed() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrow, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify filter is collapsed after clicking
	// # Function Name : clickpreFiltercollpase
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements clickpreFiltercollpase() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrowSelected, 20);
		if (btnClassFilter != null) {
			commonLibrary.clickJS(btnClassFilter, "Filter");
		}
		WebElement btnClassFilterCollapse = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrow, 20);
		if (btnClassFilter.equals(btnClassFilterCollapse)) {
			report.updateTestLog("Click on button filter", "Filter dropdown is collapsed", Status.PASS);
		} else {
			report.updateTestLog("Click on button filter", "Filter dropdown is not collapsed", Status.FAIL);
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function sto apply Jurisdiction
	// # Function Name :vsaApplyJurisdiction
	// # Author : Kirthika
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements vsaApplyJurisdiction(String filter) {
		boolean blnFlag = false;
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrow, 20);
		if (btnClassFilter != null) {
			commonLibrary.clickJS(btnClassFilter, "Filter");
		}
		WebElement jurisdiction = commonLibrary.isExist(UIMAP_VerdictsSettlements.divclassjurisdicationfilter, 20);
		if (jurisdiction != null) {
			List<WebElement> lstTagUList1 = commonLibrary.isExistList(jurisdiction, UIMAP_VerdictsSettlements.ulTaglist1, 20);
			// List<WebElement> lstTagUList =
			// commonLibrary.isExistList(lstTagUList1.get(0),
			// UIMAP_VerdictsSettlements.ulTaglist, 20);
			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList1.get(1), UIMAP_VerdictsSettlements.liTagList, 20);
			if (lstTagListItems.size() > 0)
				for (WebElement item : lstTagListItems) {
					if (item.getText().contains(filter)) {
						WebElement Checkbox = commonLibrary.isExist(item, UIMAP_VerdictsSettlements.jurisCheckbox, 20);
						commonLibrary.setCheckBox(Checkbox, true);
						blnFlag = true;
						break;
					}

				}
		}
		if (blnFlag)
			report.updateTestLog("Set filter check box", "Filter is checked", Status.PASS);
		else {
			report.updateTestLog("Set filter check box", "Filter is checked", Status.FAIL);
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : to verify preFilter DropDown arrow
	// # Function Name : verifypreFilterDropDownselected
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements verifypreFilterDropDownselected() {
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_VerdictsSettlements.filterArrowSelected, 20);
		if (btnClassFilter != null) {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is present", Status.PASS);
		} else {
			report.updateTestLog("Verify filter dropdown is available", "Filter dropdown is not present", Status.FAIL);
		}

		return new VerdictsSettlements(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public VSASearchResults vsaSearch(String strSearchTerm, Boolean strClearFilter) {

		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_VerdictsSettlements.txtSearch, 20);
		if (eltSearchbox != null) {
			commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_VerdictsSettlements.btnSearch, 20);

			if (browsername.contains("internet")) {
				try {
					driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
					commonLibrary.clickMouseMoveAction(eltSearchbutton, "Search");
				} catch (TimeoutException ex) {
					driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
					System.out.println(ex.toString());

				}
				driver.manage().timeouts().pageLoadTimeout(220, TimeUnit.SECONDS);
			} else
				commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		}
		return new VSASearchResults(scriptHelper);
	}
}
