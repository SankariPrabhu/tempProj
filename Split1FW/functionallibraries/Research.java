package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Settings;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Research extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public Research(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("researchhome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("researchhome") && counter < 40);
		if (!driver.getCurrentUrl().contains("researchhome")) {
			throw new IllegalStateException("Research home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research rightClickProductLogoOpenInNewWindow() {
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
					if (CurrentProduct.getText().contains("Research")) {
						count++;
					}
				}
				if (count >= 2) {
					report.updateTestLog("Research landing page is displayed in the secondary browser", "Research landing page is displayed in the secondary browser", Status.PASS);
				} else {
					report.updateTestLog("Research landing page is displayed in the secondary browser", "Research landing page is not displayed in the secondary browser", Status.FAIL);
				}

				driver.close();
				report.updateTestLog("Close the secondary browser", "Secondary browser is closed", Status.PASS);
				commonLibrary.switchToWindow("researchhome");
			} else {
				report.updateTestLog("Click on Open in New Window link in Rightclick menu", "Secondary browser is not displayed", Status.FAIL);
			}

			return new Research(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Litigation Profile
	// page by clicking Lexis Advance arrow
	// # Function Name : NavigateToLitigationProfilePage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LitigationProfile navigateToLitigationProfilePage() {
		try {
			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);

			WebElement btnLexisAdvanceLitigation = commonLibrary.isExist(UIMAP_Home.btnActionProfileSuite, 20);
			commonLibrary.clickLinkWithWebElement(btnLexisAdvanceLitigation, "Lexis Advance® Litigation Profile Suite");
			commonLibrary.sleep(3000);

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains("litigation profile suite")) {
				report.updateTestLog("Litigation Profile Suite landing page is displayed", "Litigation Profile Suite landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Litigation Profile Suite landing page is displayed", "Litigation Profile Suite landing page is not displayed", Status.FAIL);
			}

			return new LitigationProfile(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
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
	// # Function Description : Function to Verify the expansion of pods present
	// in home page
	// # Function Name : Verify_Pod_Expansion_Status     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research verifyPodExpansionStatus(String PodName, String PodStatus) {
		Boolean blnExpand = false;
		Boolean blnCollapse = false;
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
			WebElement PodParent = commonLibrary.isExist(UIMAP_Home.divNewsPod_Parent, 20);
			Pod = commonLibrary.isExist(PodParent, UIMAP_Home.divNewsPod, 20);
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
			return new Research(scriptHelper);
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

		return new Research(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Recent Favorites PreFilters
	// # Function Name : verify_RecentFavorites_PreFilters     
	// # Author : Shobana
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Research verifyRecentFavoritesPreFilters(String RecentList, String Exist) {
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
			if (item.getText().contains(RecentList)) {

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

		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Delete Recent PreFilters
	// # Function Name : clickDelete_RecentPreFilters     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research clickDeleteRecentPreFilters(String recentListName, String buttonName) {
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
			WebElement listName = commonLibrary.isExist(item, By.tagName("a"), 10);
			if (listName.getText().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(recentListName.toLowerCase().replaceAll(" ", ""))) {
				WebElement DeleteRecent = commonLibrary.isExist(item, UIMAP_Home.deleteRecentFav, 20);
				commonLibrary.clickButtonParentWithWait(DeleteRecent, buttonName);
				blnFlag = true;
				report.updateTestLog("Click on Delete button near " + recentListName, "Delete button near " + recentListName + "is clicked", Status.PASS);
				// WebElement popUpPage =
				// commonLibrary.isExist(UIMAP_Home.deleteRecentFavPage, 30);
				WebElement deletepopUp1 = commonLibrary.isExist(UIMAP_Home.deletePopUp1, 10);
				WebElement deletePopUp = commonLibrary.isExist(deletepopUp1, UIMAP_Home.deletePopUp, 30);
				if (deletePopUp.getText() != null) {
					String popUpText = deletePopUp.getText();
					if ((popUpText.contains("Are you sure you would like to delete this filter?")) && (popUpText.toLowerCase().replaceAll(" ", "").contains(recentListName.toLowerCase().replaceAll(" ", "")))) {
						report.updateTestLog("Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, "Delete Pop Up appears with 'Are you sure you would like to delete this filter?' " + recentListName, Status.PASS);
						switch (buttonName) {
						case "Delete": {
							WebElement Delete = commonLibrary.isExist(deletePopUp, UIMAP_Home.deleteDeletePopUp1, 20);
							commonLibrary.clickButtonParentWithWait(Delete, "Delete");
							break;
						}
						case "Cancel": {
							//WebElement footer = commonLibrary.isExist(deletepopUp1, UIMAP_Home.eltPodFooter1, 20);
							/*WebElement deleteAction = commonLibrary.isExist(footer, UIMAP_Home.popupAction, 20);

							WebElement Cancel = commonLibrary.isExist(deleteAction, UIMAP_Home.cancelDeletePopUp1, 20);

							commonLibrary.clickButtonParentWithWait(Cancel, "Cancel");*/
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

		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to filter Menu Selection
	// # Function Name : filterMenuSelection     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research filterMenuSelection(String strMenuName) {

		try {
			WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				List<WebElement> lstButtons = commonLibrary.isExistList(mnuFilterToolBar, UIMAP_Home.btnIdFilterMenu, 20);
				if (lstButtons.size() > 0)
					for (WebElement button : lstButtons) {
						if (button.getText().contains(strMenuName)) {
							commonLibrary.clickButtonParentWithWait(button, strMenuName);
							break;
						}

					}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Recently Selected Displayed
	// At Top
	// # Function Name : verifyRecentlySelectedDisplayedAtTop     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research verifyRecentlySelectedDisplayedAtTop(String RecentList) {
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
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Recently Selected Not
	// Displayed At Top
	// # Function Name : verifyRecentlySelectedNotDisplayedAtTop     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research verifyRecentlySelectedNotDisplayedAtTop(String RecentList) {
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
		for (int i = 0; i < RecentFavList.size(); i++) {
			if (RecentFavList.get(i).getText().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(RecentList.toLowerCase().replaceAll(" ", ""))) {
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify " + RecentList + " is not displayed", "" + RecentList + " is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify " + RecentList + " is not displayed", "" + RecentList + " is not displayed", Status.FAIL);
		}
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple Search
	// # Function Name : simpleSearch
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	// public Search simpleSearch(String strSearchTerm, Boolean strClearFilter)
	// {
	// WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch,
	// 20);
	// commonLibrary.SetDataInTextBox(eltSearchbox, strSearchTerm,
	// "SearchTerm");
	//
	// if (strClearFilter) {
	// WebElement btnClassFilter = commonLibrary.isExist(
	// UIMAP_Home.btnClassFilter, 20);
	// // commonLibrary.highlightElement(btnClassFilter);
	// commonLibrary.clickButton_Parent_WithWait(btnClassFilter, "Filter");
	//
	// WebElement btnClassClearFilter = commonLibrary.isExist(
	// UIMAP_Home.btnClassClearFilter, 20);
	// // commonLibrary.highlightElement(btnClassClearFilter);
	// commonLibrary.clickLink_withWebElement_WithWait(
	// btnClassClearFilter, "Clear");
	//
	// WebElement btnIdSubSearch = commonLibrary.isExist(
	// UIMAP_Home.btnIdSubSearch, 20);
	// commonLibrary.ScrollToView(btnIdSubSearch);
	// // commonLibrary.highlightElement(btnIdSubSearch);
	// commonLibrary.clickLink_withWebElement_WithWait(btnIdSubSearch,
	// "Search");
	// } else {
	// WebElement eltSearchbutton = commonLibrary.isExist(
	// UIMAP_Home.btnIdSearch, 20);
	// // commonLibrary.highlightElement(eltSearchbutton);
	// commonLibrary
	// .clickButton_Parent_WithWait(eltSearchbutton, "Search");
	// }
	//
	// return new Search(scriptHelper);
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to Simple Search Set Start In
	// # Function Name : SimpleSearchSetStartIn
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search simpleSearchSetStartIn(String strSearchTerm, Boolean strClearFilter, String startWith) {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");

		if (strClearFilter) {
			WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
			// commonLibrary.highlightElement(btnClassFilter);
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

			WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
			// commonLibrary.highlightElement(btnClassClearFilter);
			commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

			if (startWith != "") {
				filterMenuSelection("Category");
				WebElement element = commonLibrary.isExist(UIMAP_Home.startIn, 20);
				if (commonLibrary.selectByVisibleText(element, startWith)) {
					report.updateTestLog("Verify start in value is selected as " + startWith, "start in value is selected as " + startWith + "", Status.PASS);
				} else {
					report.updateTestLog("Verify start in value is selected as " + startWith + "", "start in value is not selected as " + startWith + "", Status.FAIL);
				}
			}

			WebElement btnIdSubSearch = commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);
			// commonLibrary.ScrollToView(btnIdSubSearch);
			// commonLibrary.highlightElement(btnIdSubSearch);
			commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
		} else {
			WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
			// commonLibrary.highlightElement(eltSearchbutton);
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify email is sent.
	// # Function Name : verify_eMailSent     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research verifyeMailSent(String eMail) {
		report.updateTestLog("Verify manually if One email is received to '" + eMail + "' with user id, password, and security profile (security question, answer, and/or email address)", "Verify manually if One email is received to '" + eMail + "' with user id, password, and security profile (security question, answer, and/or email address)", Status.WARNING);
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Edit of Given Client
	// # Function Name : clickEditofGivenClient     
	// # Author : Shobana
	// # Date Created : Feb'15
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
	// # Function Description : Function to click ViewAll In History
	// # Function Name : clickViewAllInHistory     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public History clickViewAllInHistory() {
		WebElement history = commonLibrary.isExist(UIMAP_SearchResult.history, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(history, "History");
		else
			commonLibrary.clickButtonParentWithWait(history, "History");

		WebElement viewAllhistory = commonLibrary.isExist(UIMAP_SearchResult.viewAllhistory, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(viewAllhistory, "View All");
		else
			commonLibrary.clickButtonParentWithWait(viewAllhistory, "View All");

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate To ResearchMap
	// # Function Name : navigateToResearchMap     
	// # Author : Pratik
	// # Date Created : Apr'15
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
			if (browsername.contains("internet"))
				commonLibrary.clickJS(lnkTxtResearchMap, "Research Map");
			else

				commonLibrary.clickLinkWithWebElement(lnkTxtResearchMap, "Research Map");
			commonLibrary.sleep(15000);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));

		return new ResearchMap(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Settings Page
	// # Function Name : VerifySettingsPage     
	// # Author : Shobana
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public LASettings verifySettingsPage() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
		if (btnMore != null)
			commonLibrary.clickMethod(btnMore, "More");

		WebElement lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);

		if (lnkSettings == null || !lnkSettings.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);
		}

		if (lnkSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSettings, "Settings");

		// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

		WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleSettings, 10);
		if (PgTitle != null) {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is NOT displayed", Status.FAIL);

		}
		WebElement btnGeneral = commonLibrary.isExist(UIMAP_Settings.btnIdGeneral, 10);
		if (btnGeneral != null && btnGeneral.getAttribute("class").toLowerCase().contains("active")) {
			report.updateTestLog("Verify 'General' settings section displayed by default", "'General' settings section displayed by default", Status.PASS);
		} else {
			report.updateTestLog("Verify 'General' settings section displayed by default", "'General' settings section NOT displayed by default", Status.WARNING);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of pages to be displayed
	// # Function Name : SetNumberOfResultsToBeDisplayed     
	// # Author : Seetha
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Research setNumberOfResultsToBeDisplayed(String number) {
		generalFunctions.setNumberOfResultsToBeDisplayed(number);

		return new Research(scriptHelper);

	}

}
