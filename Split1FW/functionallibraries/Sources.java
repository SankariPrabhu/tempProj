package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

import supportlibraries.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Sources;
import UIMAP.UIMAP_TOC;
import UIMAP.UIMAP_VSASearchResults;
import UIMAP.UIMAP_WorkFolders;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

public class Sources extends ReusableLibrary {
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public Sources(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("sourceselection"))
				commonLibrary.sleep(5000);

		} while (!url.contains("sourceselection") && counter < 40);

		if (!driver.getCurrentUrl().contains("sourceselection")) {
			throw new IllegalStateException("Sources page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within All Sources page.
	// # Function Name : searchWithinSources     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources searchWithinSources(String searchTerm) {
		pageCheck.positiveCheck(driver, "Sources", "Sources");
		generalFunctions.documentState(driver);
		WebElement subSearchPanel = commonLibrary.isExistNegative(UIMAP_Sources.subSearchPanel, 10);
		WebElement subSearchTextBox = commonLibrary.isExistNegative(subSearchPanel, UIMAP_Sources.subSearchTextBox, 10);
		commonLibrary.setDataInTextBox(subSearchTextBox, searchTerm, "Search Within Sources");
		WebElement subSearchButton = commonLibrary.isExistNegative(subSearchPanel, UIMAP_Sources.subSearchButton, 10);
		commonLibrary.clickButtonParentWithWait(subSearchButton, "Search Within Sources");
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(10000);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a source within All Sources
	// page.
	// # Function Name : clickSourceName     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources clickSourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
				commonLibrary.clickButtonLogSmallWait(button, sourceName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify option present or not in
	// Source dropdown.
	// # Function Name : verifySourceDropdownOption     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources verifySourceDropdownOption(String optionName, boolean status) {
		if (status) {
			boolean blnFlag = false;
			WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
			WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
			List<WebElement> sourceDropDownList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceDropDown, 10);
			for (WebElement sourceDropDown : sourceDropDownList) {
				if (sourceDropDown.isDisplayed()) {
					List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_Sources.lstTagListItems, 10);
					for (WebElement option : sourceDropDownOptions) {
						if (option.getText().toLowerCase().contains(optionName.toLowerCase())) {
							report.updateTestLog("Verify " + optionName + " option is present.", optionName + " option is present.", Status.PASS);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag)
					break;
			}
			if (!blnFlag)
				report.updateTestLog("Verify " + optionName + " option is present.", optionName + " option is not present.", Status.FAIL);
		} else {
			boolean blnFlag = false;
			WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
			WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
			List<WebElement> sourceDropDownList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceDropDown, 10);
			for (WebElement sourceDropDown : sourceDropDownList) {
				if (sourceDropDown.isDisplayed()) {
					List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_Sources.lstTagListItems, 10);
					for (WebElement option : sourceDropDownOptions) {
						if (option.getText().toLowerCase().contains(optionName.toLowerCase())) {
							report.updateTestLog("Verify " + optionName + " option is not present.", optionName + " option is present.", Status.FAIL);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag)
					break;
			}
			if (!blnFlag)
				report.updateTestLog("Verify " + optionName + " option is not present.", optionName + " option is not present.", Status.PASS);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Get Documents in Source
	// dropdown.
	// # Function Name : clickGetDocuments     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Search clickGetDocuments() {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceDropDownList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceDropDown, 10);
		for (WebElement sourceDropDown : sourceDropDownList) {
			if (sourceDropDown.isDisplayed()) {
				List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_Sources.lstTagListItems, 10);
				for (WebElement option : sourceDropDownOptions) {
					WebElement link = commonLibrary.isExistNegative(option, UIMAP_Sources.lnkLinks, 10);

					if (link != null && link.getText().toLowerCase().contains("get documents")) {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].focus();", link);
						executor.executeScript("arguments[0].click();", link);

						commonLibrary.sleep(10000);

						report.updateTestLog("Click on button Get Documents", "Clicked on button Get Documents", Status.PASS);
						int counter = 0;
						do {
							commonLibrary.sleep(10000);
							counter = counter + 1;
						} while (!driver.getCurrentUrl().contains("search") && counter <= 15);

						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on Get Documents.", "Get Documents option is not present.", Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select All.
	// # Function Name : clickAll     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources clickAll() {
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(UIMAP_Sources.sourcePageRightContent, 10);
		WebElement buttonAll = commonLibrary.isExistNegative(sourcePageRightContent, UIMAP_Sources.buttonAll, 10);
		commonLibrary.clickButtonParentWithWait(buttonAll, "All");
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the source to filter
	// # Function Name : addSourceToFilter     
	// # Author : Ram
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources addSourceToFilter(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResults = commonLibrary.isExistNegative(UIMAP_Sources.sourceresults, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourceResults, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
				commonLibrary.clickButtonLogSmallWait(button, sourceName);
				blnFlag = true;
				WebElement dropDown = commonLibrary.isExist(source, UIMAP_Sources.actionList, 10);
				WebElement addFilter = commonLibrary.isExist(dropDown, UIMAP_Sources.addfilter, 10);
				commonLibrary.clickButtonParentWithWait(addFilter, "Add source as a search filter");
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		// WebElement dropDown = commonLibrary.isExist(UIMAP_Sources.actionList,
		// 10);
		// WebElement addFilter =
		// commonLibrary.isExist(dropDown,UIMAP_Sources.addfilter, 10);
		// commonLibrary.clickButton_Parent_WithWait(addFilter,
		// "Add source as a search filter");

		// List<WebElement> sourceDropDownList =
		// commonLibrary.isExist_List(sourcePageRightContent,UIMAP_Sources.sourceDropDown,
		// 10);
		// for(WebElement sourceDropDown:sourceDropDownList)
		// {
		// if(sourceDropDown.isDisplayed())
		// {
		// List<WebElement> sourceDropDownOptions =
		// commonLibrary.isExist_List(sourceDropDown,
		// UIMAP_Sources.lstTagListItems, 10);
		// for(WebElement option : sourceDropDownOptions)
		// {
		// WebElement link = commonLibrary.isExist_Negative(option,
		// UIMAP_Sources.lnkLinks, 10);
		//
		// if(link!=null &&
		// link.getText().contains("Add source as a search filter"))
		// {
		//
		// commonLibrary.clickLink_withWebElement_WithWait(link,
		// "Add source as a search filter");
		// blnFlag=true;
		// break;
		// }
		// }
		// if(blnFlag)
		// break;
		// }
		// }
		// if(!blnFlag)
		// report.updateTestLog("Click on Add source as a search filter",
		// "Add source as a search filter is not present.", Status.FAIL);
		commonLibrary.sleep(300000);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take screenshot.
	// # Function Name : TakeScreenShot     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources takeScreenShot(String strTCName, String strStep) {
		try {
			final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			String TestPath = frameworkParameters.getRelativePath() + Util.getFileSeparator();

			String strScreenshot = strTCName + commonLibrary.getCurrentDateTime();
			String strDestination = TestPath + "Screenshot\\" + strScreenshot + ".jpg";

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile.toPath(), new File(strDestination).toPath());
			report.updateTestLog(strStep, "Perform Manual Verification for this step. screenshot is saved in " + strDestination + "", Status.WARNING);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search in global bar
	// # Function Name : SimpleSearch     
	// # Author : Uma
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear the pre filters
	// # Function Name : clearPreFilters     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources clearPreFilters() {
		WebElement filterIdCount = commonLibrary.isExistNegative(UIMAP_Home.FilterIdCount, 2);
		if (filterIdCount != null) {
			WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
			// commonLibrary.highlightElement(btnClassFilter);
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");

			WebElement btnClassClearFilter = commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
			// commonLibrary.highlightElement(btnClassClearFilter);
			commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter, "Clear");

		}

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filter selected
	// # Function Name : VerifyFilterSelected     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources verifyFilterSelected(String strSelectedFilterOptions) {

		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {

			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 20);
			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {

					for (WebElement item : divClassDeleteFilter) {
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", Status.PASS);
							break;
						} else {
							report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy textbox", Status.FAIL);
						}
					}
				}

				WebElement FilterIdCount = commonLibrary.isExist(UIMAP_Home.FilterIdCount, 20);
				String strCount = FilterIdCount.getText();

				if (Integer.parseInt(strCount) == divClassDeleteFilter.size())
					report.updateTestLog("Verify " + divClassDeleteFilter.size() + " is displayed in Big Red Searchbox", divClassDeleteFilter.size() + " is displayed in Big Red Searchbox", Status.PASS);
				else
					report.updateTestLog("Verify " + divClassDeleteFilter.size() + " is displayed in Big Red Searchbox", divClassDeleteFilter.size() + " is not displayed in Big Red Searchbox", Status.FAIL);

			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within the result
	// # Function Name : searchWithinSearch     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources searchWithinSearch(String sectionHeaderName, String searchTerm) {

		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(sectionHeaderName.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), sectionHeaderName);
				report.updateTestLog("Expanding Header: " + sectionHeaderName, sectionHeaderName + " Header Expanded.", Status.DONE);
			}
		}
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchBox, 20);
		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);

		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Browse Menu Options.
	// # Function Name : selectMenuFromBrowse     
	// # Author : Seetha
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources selectMenuFromBrowse(String strOption) {
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
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search for source in Browse Menu.
	// # Function Name : searchSource     
	// # Author : Pratik
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources searchSource(String source, String option) {
		WebElement childMenu = commonLibrary.isExistNegative(UIMAP_Home.browseChildMenu, 10);
		// WebElement browseMenuSearch =
		// commonLibrary.isExist_Negative(childMenu,
		// UIMAP_Sources.browseMenuSearch, 10);
		WebElement sourceSearch = commonLibrary.isExistNegative(childMenu, UIMAP_Home.sourceSearch, 10);

		if (sourceSearch != null) {
			commonLibrary.setDataInTextBox(sourceSearch, source, "Find a source");
			sourceSearch.sendKeys(Keys.BACK_SPACE);
			sourceSearch.sendKeys(Keys.BACK_SPACE);
			sourceSearch.sendKeys(source.substring(source.length() - 2));

			try {

				commonLibrary.sleep(3000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			childMenu = commonLibrary.isExistNegative(UIMAP_Home.browseChildMenu, 10);
			WebElement wordWheel = commonLibrary.isExist(childMenu, UIMAP_Home.sourceSearchWordWheel, 10);
			if (option != "") {
				if (wordWheel != null) {
					List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheel, By.tagName("li"), 20);

					boolean flag = false;
					for (WebElement item : wordWheelOptions) {
						if (item.getText().equalsIgnoreCase(option)) {
							commonLibrary.clickButtonParentWithWait(item, item.getText());
							flag = true;
							break;
						} else if (item.getText().toLowerCase().contains(option.toLowerCase())) {
							commonLibrary.clickButtonParentWithWait(item, item.getText());
							flag = true;
							break;
						}

					}
					if (!flag) {
						commonLibrary.clickButtonParentWithWait(wordWheelOptions.get(0), wordWheelOptions.get(0).getText());
					}
					if (sourceSearch.getAttribute("value").contains(option))
						report.updateTestLog("Verify the text " + option + " is filled in the Search within sources box", "The text " + option + " is filled in the Search within sources box", Status.PASS);
					else
						report.updateTestLog("Verify the text " + option + " is filled in the Search within sources box", "The text " + option + " is filled in the Search within sources box", Status.FAIL);
				}
				// WebElement search = commonLibrary.isExist(browseMenuSearch,
				// UIMAP_HomePage.sourceSearchBtn,10);
				// if(search!=null)
				// commonLibrary.clickButton_Parent_WithWait(search, "Search");
			}

			WebElement resultPageHeaderold = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
			sourceSearch.sendKeys(Keys.ENTER);
			report.updateTestLog("click on Search Within Sources button", "Search Within Sources button is clicked", Status.DONE);

			WebElement resultPageHeadernew = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
			int count = 0;
			do {
				commonLibrary.sleep(20000);
				count++;
				resultPageHeadernew = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
			} while ((resultPageHeadernew == null && resultPageHeadernew == resultPageHeaderold) && count < 15);

		}
		pageCheck.positiveCheck(driver, "Sources", "Sources");
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the recent favorite
	// prefilters
	// # Function Name : verify_RecentFavorites_PreFilters     
	// # Author : Ram
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources verifyRecentFavoritesPreFilters(String RecentList, String Exist) {
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

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the items to favorite
	// # Function Name : addToFavorites     
	// # Author : Ram
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources addToFavorites(String RecentList) {
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
			if (item.getText().contains(RecentList)) {
				WebElement favIcon = commonLibrary.isExist(item, UIMAP_Home.addFavorite, 20);
				if (!(favIcon.getAttribute("class").contains("FavoriteFull")))
					commonLibrary.clickButtonParentWithWait(favIcon, "Favorites for " + RecentList);
				break;
			}
		}

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the favorite items
	// # Function Name : verifyFavorite     
	// # Author : Ram
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public Sources verifyFavorite(String RecentList, String Exist) {
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
				WebElement favIcon = commonLibrary.isExist(item, UIMAP_Home.addFavorite, 20);
				if (favIcon.getAttribute("class").contains("FavoriteFull"))
					blnFlag = true;
				break;
			}
		}

		switch (Exist) {
		case "Exist": {
			if (blnFlag) {
				report.updateTestLog("Verify " + RecentList + " is displayed as a favorite item with star filled in", RecentList + " is displayed as a favorite item with star filled in", Status.PASS);
			} else

			{
				report.updateTestLog("Verify " + RecentList + " is displayed as a favorite item with star filled in", RecentList + " is not displayed as a favorite item with star filled in", Status.FAIL);
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

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the source name
	// # Function Name : verifySourceName     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources verifySourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceresults = commonLibrary.isExist(UIMAP_Sources.sourceresults, 10);
		WebElement sourcePageRightContent = commonLibrary.isExist(sourceresults, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				report.updateTestLog("Verify " + sourceName + " displays", sourceName + " displays", Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify " + sourceName + " displays", "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Result Header
	// # Function Name : verifyResultHeader     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources verifyResultHeader(String searchTerm) {
		WebElement resultPageHeader = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
		if (resultPageHeader.getText().toLowerCase().contains("results within sources: " + searchTerm.toLowerCase()))
			report.updateTestLog("Verify Results Within Sources:" + searchTerm + " displays at the top of the page", "Results Within Sources:" + searchTerm + " displays at the top of the page", Status.PASS);
		else
			report.updateTestLog("Verify Results Within Sources:" + searchTerm + " displays at the top of the page", "Results Within Sources:" + searchTerm + " does not display at the top of the page", Status.FAIL);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the ResultHeader Zero
	// # Function Name : verifyResultHeader_Zero     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources verifyResultHeaderZero(String searchTerm) {
		WebElement resultPageHeader = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
		if ((resultPageHeader.getText().toLowerCase().contains("results within sources: " + searchTerm.toLowerCase())) && resultPageHeader.getText().contains("(0)"))
			report.updateTestLog("Verify Results Within Sources:" + searchTerm + " displays at the top of the page", "Results Within Sources:" + searchTerm + " displays at the top of the page", Status.PASS);
		else
			report.updateTestLog("Verify Results Within Sources:" + searchTerm + " displays at the top of the page", "Results Within Sources:" + searchTerm + " does not display at the top of the page", Status.FAIL);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the clear search button
	// # Function Name : clickClearSearch     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources clickClearSearch() {
		WebElement resultPageHeader = commonLibrary.isExistNegative(UIMAP_Sources.resultPageHeader, 10);
		WebElement clearSearch = commonLibrary.isExistNegative(resultPageHeader, UIMAP_Sources.clearSearch, 10);
		commonLibrary.clickButtonParentWithWait(clearSearch, clearSearch.getText());
		return new Sources(scriptHelper);

	}

	// public Sources search_BrowseDropdown(String searchTerm, String option)
	// {
	// Boolean blnFirst=false;
	// WebElement btnIdBrowse =
	// commonLibrary.isExist(UIMAP_HomePage.btnIdBrowse, 20);
	// commonLibrary.clickButton_Parent_WithWait(btnIdBrowse, "Browse");
	// try
	// {
	// commonLibrary.sleep(4000);
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception",e.toString());
	// }
	// WebElement divIdBrowserMenu =
	// commonLibrary.isExist(UIMAP_HomePage.divIdBrowserMenu, 20);
	// if (divIdBrowserMenu!=null)
	// {
	// WebElement lstTagAside =
	// commonLibrary.isExist(divIdBrowserMenu,UIMAP_HomePage.lstTagAside, 20);
	// List<WebElement> lstTagListItems =
	// commonLibrary.isExist_List(lstTagAside,UIMAP_HomePage.lstTagListItems,
	// 20);
	// if(lstTagListItems.size()>0)
	// for (WebElement button : lstTagListItems) {
	// if(button.getText().contains("Sources"))
	// {
	// blnFirst=true;
	// commonLibrary.clickButton_Parent_WithWait(button, "Sources");
	// break;
	// }
	//
	// }
	// if(!blnFirst)
	// report.updateTestLog("Click on Sources ", "Sources not present",
	// Status.FAIL);
	// }
	//
	//
	//
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sources sub child
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
			if (!itemname.equals(""))
				report.updateTestLog("Display of Categories is Verified", itemname + " is Present", Status.PASS);
		}

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the sub-child Sources
	// # Function Name : selectsubchildSources
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources selectsubchildSources(String item) {
		Boolean blnfirst = false;
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems2) {

			if (button.getText().trim().toLowerCase().contains(item.toLowerCase().trim())) {
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
	// # Function Description : Function to verify sources jurisdiction post
	// filters
	// # Function Name : verifySourcesJurisdictionpostfilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifySourcesJurisdictionpostfilters(String filtervalue) {
		Boolean blnflag = false;
		String option = dataTable.getData("General_Data", "JurisdictionPostFilter1");
		String[] optionList = option.split(";");
		String option2 = dataTable.getData("General_Data", "PostFilter2");
		String[] optionList2 = option2.split(";");
		WebElement appliedfilter = commonLibrary.isExist(UIMAP_Sources.filtersapplied, 20);
		WebElement pageheader = commonLibrary.isExist(UIMAP_Sources.pageheader, 20);
		String pagetitle = pageheader.getText();
		String filter1 = appliedfilter.getText();
		if (pagetitle.contains("Sources")) {
			report.updateTestLog("Page Title is Verified", pagetitle + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Page Title is Verified", pagetitle + " is not displayed", Status.FAIL);
		}
		if (filter1.contains(filtervalue)) {
			report.updateTestLog("Filter is Verified", filtervalue + " is Present under Narrow By Section", Status.PASS);
		} else {
			report.updateTestLog("Filter is Verified", filtervalue + " is not displayed under Narrow By Section", Status.FAIL);
		}

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			String buttontext1 = buttontext.replaceAll("[^a-zA-Z]+", "");
			for (int i = 0; i < optionList.length; i++) {

				if (optionList[i].replaceAll("[^a-zA-Z]+", "").equals(buttontext1)) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		WebElement closefilters = commonLibrary.isExist(UIMAP_Sources.closefilter, 20);
		commonLibrary.clickButtonParentWithWait(closefilters, "Close");
		WebElement unusedfilters1 = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(unusedfilters1, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist1 = commonLibrary.isExistList(ullist1.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist1) {
			String buttontext1 = button.getText();
			for (int i = 0; i < optionList2.length; i++) {

				if (optionList2[i].contains(button.getText())) {

					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext1 + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of sources category
	// post filters in SS
	// # Function Name : verifySourcesCategorypostfilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifySourcesCategorypostfilters(String filtervalue) {
		Boolean blnflag = false;
		String option = dataTable.getData("General_Data", "CategoryPostFilter1");
		option.split(";");
		String option2 = dataTable.getData("General_Data", "PostFilter2");
		String[] optionList2 = option2.split(";");
		WebElement appliedfilter = commonLibrary.isExist(UIMAP_Sources.filtersapplied, 20);
		WebElement pageheader = commonLibrary.isExist(UIMAP_Sources.pageheader, 20);
		String pagetitle = pageheader.getText();
		String filter1 = appliedfilter.getText();
		if (pagetitle.contains("Sources")) {
			report.updateTestLog("Page Title is Verified", pagetitle + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Page Title is Verified", pagetitle + " is not displayed", Status.FAIL);
		}
		if (filter1.contains(filtervalue)) {
			report.updateTestLog("Filter is Verified", filtervalue + " is Present under Narrow By Section", Status.PASS);
		} else {
			report.updateTestLog("Filter is Verified", filtervalue + " is not displayed under Narrow By Section", Status.FAIL);
		}

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			String buttontext1 = buttontext.replaceAll("[^a-zA-Z]+", "");
			for (int i = 0; i < optionList2.length; i++) {

				if (buttontext1.contains(optionList2[i].replaceAll("[^a-zA-Z]+", ""))) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
					break;
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		WebElement closefilters = commonLibrary.isExist(UIMAP_Sources.closefilter, 20);
		commonLibrary.clickButtonParentWithWait(closefilters, "Close");
		WebElement unusedfilters1 = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(unusedfilters1, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist1 = commonLibrary.isExistList(ullist1.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist1) {
			String buttontext = button.getText();
			for (int i = 0; i < optionList2.length; i++) {
				if (optionList2[i].contains(button.getText())) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of sources Publisher
	// post filters in SS
	// # Function Name : verifySourcesPublisherpostfilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifySourcesPublisherpostfilters(String filtervalue) {
		Boolean blnflag = false;
		String option = dataTable.getData("General_Data", "PublisherPostFilter1");
		String[] optionList = option.split(";");
		String option2 = dataTable.getData("General_Data", "PostFilter2");
		String[] optionList2 = option2.split(";");
		WebElement appliedfilter = commonLibrary.isExist(UIMAP_Sources.filtersapplied, 20);
		WebElement pageheader = commonLibrary.isExist(UIMAP_Sources.pageheader, 20);
		String pagetitle = pageheader.getText();
		String filter1 = appliedfilter.getText();
		if (pagetitle.contains("Sources")) {
			report.updateTestLog("Page Title is Verified", pagetitle + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Page Title is Verified", pagetitle + " is not displayed", Status.FAIL);
		}
		if (filter1.contains(filtervalue)) {
			report.updateTestLog("Filter is Verified", filtervalue + " is Present under Narrow By Section", Status.PASS);
		} else {
			report.updateTestLog("Filter is Verified", filtervalue + " is not displayed under Narrow By Section", Status.FAIL);
		}

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			String buttontext1 = buttontext.replaceAll("[^a-zA-Z]+", "");
			for (int i = 0; i < optionList.length; i++) {

				if (buttontext1.contains(optionList[i].replaceAll("[^a-zA-Z]+", ""))) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter is not displayed in the application", Status.FAIL);
			}

		}
		WebElement closefilters = commonLibrary.isExist(UIMAP_Sources.closefilter, 20);
		commonLibrary.clickButtonParentWithWait(closefilters, "Close");
		WebElement unusedfilters1 = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(unusedfilters1, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist1 = commonLibrary.isExistList(ullist1.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist1) {
			String buttontext = button.getText();
			for (int i = 0; i < optionList2.length; i++) {

				if (optionList2[i].contains(button.getText())) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of sources Practice
	// Area post filters in SS
	// # Function Name : verifySourcesPracticeAreapostfilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifySourcesPracticeAreapostfilters(String filtervalue) {
		Boolean blnflag = false;
		String option = dataTable.getData("General_Data", "PracticePostfilter1");
		String[] optionList = option.split(";");
		String option2 = dataTable.getData("General_Data", "PostFilter2");
		String[] optionList2 = option2.split(";");
		WebElement appliedfilter = commonLibrary.isExist(UIMAP_Sources.filtersapplied, 20);
		WebElement pageheader = commonLibrary.isExist(UIMAP_Sources.pageheader, 20);
		String pagetitle = pageheader.getText();
		String filter1 = appliedfilter.getText();
		if (pagetitle.contains("Sources")) {
			report.updateTestLog("Page Title is Verified", pagetitle + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Page Title is Verified", pagetitle + " is not displayed", Status.FAIL);
		}
		if (filter1.contains(filtervalue)) {
			report.updateTestLog("Filter is Verified", filtervalue + " is Present under Narrow By Section", Status.PASS);
		} else {
			report.updateTestLog("Filter is Verified", filtervalue + " is not displayed under Narrow By Section", Status.FAIL);
		}

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			String buttontext1 = buttontext.replaceAll("[^a-zA-Z]+", "");
			for (int i = 0; i < optionList.length; i++) {

				if (buttontext1.contains(optionList[i].replaceAll("[^a-zA-Z]+", ""))) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter is not displayed in the application", Status.FAIL);
			}

		}
		WebElement closefilters = commonLibrary.isExist(UIMAP_Sources.closefilter, 20);
		commonLibrary.clickButtonParentWithWait(closefilters, "Close");
		WebElement unusedfilters1 = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(unusedfilters1, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist1 = commonLibrary.isExistList(ullist1.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist1) {
			String buttontext1 = button.getText();
			for (int i = 0; i < optionList2.length; i++) {
				if (optionList2[i].contains(button.getText())) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext1 + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of sources
	// subscription post filters in SS
	// # Function Name : verifySourcesSubscriptionpostfilters
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifySourcesSubscriptionpostfilters(String option, String filtervalue) {
		Boolean blnflag = false;

		String[] optionList = option.split(";");
		String option2 = dataTable.getData("General_Data", "PostFilter2");
		String[] optionList2 = option2.split(";");
		WebElement appliedfilter = commonLibrary.isExist(UIMAP_Sources.filtersapplied, 20);
		WebElement pageheader = commonLibrary.isExist(UIMAP_Sources.pageheader, 20);
		String pagetitle = pageheader.getText();
		String filter1 = appliedfilter.getText();
		if (pagetitle.contains("Sources")) {
			report.updateTestLog("Page Title is Verified", pagetitle + " is Displayed", Status.PASS);
		} else {
			report.updateTestLog("Page Title is Verified", pagetitle + " is not displayed", Status.FAIL);
		}
		if (filter1.contains(filtervalue)) {
			report.updateTestLog("Filter is Verified", filtervalue + " is Present under Narrow By Section", Status.PASS);
		} else {
			report.updateTestLog("Filter is Verified", filtervalue + " is not displayed under Narrow By Section", Status.FAIL);
		}

		WebElement unusedfilters = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist = commonLibrary.isExistList(unusedfilters, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist = commonLibrary.isExistList(ullist.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist) {
			String buttontext = button.getText();
			String buttontext1 = buttontext.replaceAll("[^a-zA-Z]+", "");
			for (int i = 0; i < optionList.length; i++) {

				if (buttontext1.contains(optionList[i].replaceAll("[^a-zA-Z]+", ""))) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		WebElement closefilters = commonLibrary.isExist(UIMAP_Sources.closefilter, 20);
		commonLibrary.clickButtonParentWithWait(closefilters, "Close");
		try {
			String loadProp = properties.getProperty("xSpinner");
			int count = 0;
			WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
			do {
				commonLibrary.sleep(100000);
				loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				count++;
			} while (loader != null && count < 20);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		WebElement unusedfilters1 = commonLibrary.isExist(UIMAP_Sources.unusedfilters, 20);
		List<WebElement> ullist1 = commonLibrary.isExistList(unusedfilters1, UIMAP_Sources.ulclass, 20);
		List<WebElement> postfilterlist1 = commonLibrary.isExistList(ullist1.get(0), UIMAP_Sources.buttonnew, 20);
		for (WebElement button : postfilterlist1) {
			String buttontext1 = button.getText();
			for (int i = 0; i < optionList2.length; i++) {
				if (button.getText().contains(optionList2[i])) {
					blnflag = true;
					report.updateTestLog("PostFilter presence is verified", optionList2[i] + " Post filter is present", Status.PASS);
				}
			}
			if (!blnflag) {
				report.updateTestLog("PostFilter presence is verified", buttontext1 + " Post filter should not be displayed in the application", Status.FAIL);
			}

		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify display of information bubble
	// in SS
	// # Function Name : verifyinfodisplay
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources verifyinfodisplay(String Searchterm) {
		String headers = dataTable.getData("General_Data", "HeadersinDoc");
		String[] headersList = headers.split(";");
		WebElement searchwithinsources = commonLibrary.isExist(UIMAP_Sources.searchwithinbutton, 20);
		if (searchwithinsources != null) {
			commonLibrary.setDataInTextBox(UIMAP_Sources.searchwithinsources, Searchterm);
			commonLibrary.clickButtonParentWithWait(searchwithinsources, "Search");

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(100000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
					count++;
				} while (loader != null && count < 20);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// pageCheck.ajaxElementCheck(driver,
			// properties.getProperty("xSpinner"));
			WebElement infobutton = commonLibrary.isExistNegative(UIMAP_Sources.infobutton, 5);
			int counter = 0;
			do {
				infobutton = commonLibrary.isExistNegative(UIMAP_Sources.infobutton, 5);
				counter++;
				commonLibrary.sleep(5000);
			} while (infobutton == null && counter <= 20);

			if (infobutton != null) {
				commonLibrary.clickButtonParentWithWait(infobutton, "Information");
				try {
					String loadProp = properties.getProperty("xSpinner");
					int count = 0;
					WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
					do {
						commonLibrary.sleep(100000);
						loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
						count++;
					} while (loader != null && count < 20);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				// pageCheck.ajaxElementCheck(driver,
				// properties.getProperty("xSpinner"));

				// int count = 0;
				// do {
				// count++;
				// infowrapper =
				// commonLibrary.isExistNegative(UIMAP_Sources.infowrapper, 3);
				// if (infowrapper == null)
				// commonLibrary.sleep(5000);
				// } while ((infowrapper == null ||
				// !(infowrapper.isDisplayed())) && count <= 40);
				WebElement infowrapper = commonLibrary.isExistNegative(UIMAP_Sources.infowrapper, 10);
				if (infowrapper == null) {
					commonLibrary.clickButtonParentWithWait(infobutton, "Information");
					try {
						String loadProp = properties.getProperty("xSpinner");
						int count = 0;
						WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
						do {
							commonLibrary.sleep(100000);
							loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
							count++;
						} while (loader != null && count < 20);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				WebElement spanclass = commonLibrary.isExist(infowrapper, UIMAP_Sources.spanclass, 20);
				if (spanclass != null) {
					List<WebElement> totalheaders = commonLibrary.isExistList(spanclass, UIMAP_Sources.headers, 20);
					for (WebElement headinglist : totalheaders) {
						String headings = headinglist.getText().replace(":", "");
						for (int i = 0; i < headersList.length; i++) {
							if (headersList[i].contains(headings)) {
								report.updateTestLog("Headers in Info Bubble is verified", headersList[i] + " header is present", Status.PASS);
							}
						}
					}
					commonLibrary.clickButtonParentWithWait(infobutton, "Information");
					pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
					commonLibrary.sleep(50000);
					WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Sources.Extrainfoenabled, 2);
					if (btnClassFilter == null) {
						report.updateTestLog("Verify Expanded or Collapsed Option", "Information bubble is Collapsed", Status.PASS);
					} else {
						report.updateTestLog("Verify Expanded or Collapsed Option", "Information bubble is Expanded", Status.FAIL);
					}
				}
			}
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify presence of source page
	// layout in SS
	// # Function Name : verifysourcepagelayout
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Search verifysourcepagelayout(String link) {

		String options = "Include/exclude legal phrase equivalents;Expanded Results | Fewer Results | Terms and Connectors;Re-run the search as Natural Language/Terms & Connectors";
		String[] optionItem = options.split(";");
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		boolean blnFlag1 = false;
		int count = 1;
		do {
			WebElement sourceresults = commonLibrary.isExist(UIMAP_Sources.sourceresults, 20);
			WebElement ollist = commonLibrary.isExist(sourceresults, UIMAP_Sources.ollist, 20);
			List<WebElement> lilist = commonLibrary.isExistList(ollist, UIMAP_Sources.lilist, 20);
			for (WebElement button : lilist) {
				WebElement buttonName = commonLibrary.isExist(button, By.tagName("button"), 20);
				if (buttonName.getText().equalsIgnoreCase(link)) {
					blnFlag = true;
					// WebElement sourcelink =
					// commonLibrary.isExist(UIMAP_Sources.sourcelink, 20);
					commonLibrary.clickButtonLogSmallWait(buttonName, link);
					break;
				}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_Sources.btnNextPage);
				if (btnNextPage != null)
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnNextPage, "NextPage");
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
					}
			}
			count++;
		} while (btnNextPage != null && count <= 15);
		if (!blnFlag)
			report.updateTestLog("Click on the document " + link, "Not Clicked  on the document " + link, Status.FAIL);

		// To Verify the options present under the Selected Source
		blnFlag = false;
		blnFlag1 = false;
		String itemname = null;
		WebElement selectedsource = commonLibrary.isExist(UIMAP_Sources.selectedsource, 20);
		WebElement actionlist = commonLibrary.isExist(selectedsource, UIMAP_Sources.actionList, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(actionlist, UIMAP_Sources.lilist, 20);
		for (int i = 0; i < lstTagListItems2.size(); i++) {
			WebElement link1 = commonLibrary.isExist(lstTagListItems2.get(i), UIMAP_Sources.lnkLinks, 20);
			if (link1.getText().contains("Get documents")) {
				commonLibrary.clickButtonLogSmallWait(link1, "Get documents");
				blnFlag = true;
				break;
			}

		}
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		WebElement selectedsource1 = commonLibrary.isExist(UIMAP_Sources.selectedsource, 20);
		if (blnFlag) {
			WebElement pagewrapper = commonLibrary.isExist(UIMAP_Sources.h2class, 20);
			int count1 = 0;
			do {
				count1++;
				commonLibrary.sleep(2000);
			} while (pagewrapper == null && count1 < 15 && selectedsource1 == null);

			WebElement actionButton = commonLibrary.isExist(pagewrapper, UIMAP_Sources.actionButon, 20);
			if (actionButton != null) {
				commonLibrary.clickButtonLogSmallWait(actionButton, "Action");
			}
			// WebElement h2tag=commonLibrary.isExist(UIMAP_Sources.h2class,20);
			// WebElement ul=commonLibrary.isExist(h2tag,By.tagName("ul"),20);
			// WebElement
			// actionlist1=commonLibrary.isExist(ul,UIMAP_Sources.list2,20);
			WebElement divAction = commonLibrary.isExist(UIMAP_SearchResult.divAction, 20);
			// WebElement
			// actionresult=commonLibrary.isExist(divAction,UIMAP_Sources.lilist,20);
			List<WebElement> lstTagListItems3 = commonLibrary.isExistList(divAction, UIMAP_Sources.lilist, 20);
			for (int j = 0; j < optionItem.length; j++) {
				for (WebElement button : lstTagListItems3) {
					itemname = button.getText();
					if (itemname.contains(optionItem[j])) {
						blnFlag1 = true;
						break;
					}

				}
				if (blnFlag1)
					report.updateTestLog("Verify " + optionItem[j] + " is not present in the dropdown", optionItem[j] + " is Present", Status.FAIL);
				else
					report.updateTestLog("Verify " + optionItem[j] + " is not present in the dropdown", optionItem[j] + " is not Present", Status.PASS);

			}
		}

		// for (WebElement button : lstTagListItems2) {
		// itemname = button.getText();
		// if(itemname.contains(optionItem[i]))
		// {
		// blnFlag=true;
		// break;
		// }
		//
		// }
		// if(blnFlag)
		// report.updateTestLog("Verify "+optionItem[i]+" is not present in the dropdown",
		// optionItem[i] + " is Present", Status.FAIL);
		// else
		// report.updateTestLog("Verify "+optionItem[i]+" is not present in the dropdown",
		// optionItem[i] + " is not Present", Status.PASS);
		// }

		// this.selectlinkundersource(sublink1);
		// this.selectlinkundersource(sublink2);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify display of restricted content
	// # Function Name : verifyrestrictedcontentdisplay
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Sources verifyrestrictedcontentdisplay(String restrictedlink) {
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;
		do {
			WebElement sourceresults = commonLibrary.isExist(UIMAP_Sources.sourceresults, 20);
			WebElement ollist = commonLibrary.isExist(sourceresults, UIMAP_Sources.ollist, 20);

			WebElement sourcelink = commonLibrary.isExist(ollist, UIMAP_Sources.restrictedsources, 20);
			List<WebElement> lilist = commonLibrary.isExistList(ollist, UIMAP_Sources.lilist, 20);
			for (WebElement button : lilist) {
				if (sourcelink != null)
					if (button.getText().contains(restrictedlink)) {
						blnFlag = true;

						report.updateTestLog("Verify the restricted document " + restrictedlink, restrictedlink + " Restricted Document is Present ", Status.PASS);
					}
			}

			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_Sources.btnNextPage);
				if (btnNextPage != null)
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnNextPage, "NextPage");
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
					}
			}
			count++;
		} while (btnNextPage != null && count <= 15);
		if (!blnFlag)
			report.updateTestLog("Verify the restricted document " + restrictedlink, restrictedlink + " Restricted Document is not Present ", Status.FAIL);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select link under source
	// # Function Name : selectlinkundersource
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources selectlinkundersource(String sublink) {
		WebElement selectedsource = commonLibrary.isExist(UIMAP_Sources.selectedsource, 20);
		WebElement actionlist = commonLibrary.isExist(selectedsource, UIMAP_Sources.actionList, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(actionlist, UIMAP_Sources.lilist, 20);
		for (WebElement button : lstTagListItems2) {
			String itemname = button.getText();
			if (itemname.contains(sublink)) {
				commonLibrary.clickButtonLogSmallWait(button, itemname);
			}
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : NavigateToAll     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Sources selectchildoptioninSources(String option) {
		Boolean blnSecond = false;
		/*
		 * WebElement btnIdBrowse =
		 * commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
		 * commonLibrary.clickButton_Parent_WithWait(btnIdBrowse, "Browse"); try
		 * { commonLibrary.sleep(Mwait); } catch (Exception e) {
		 * System.out.println(e.toString()); throw new
		 * FrameworkException("Exception",e.toString()); } WebElement
		 * divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu,
		 * 20); if (divIdBrowserMenu!=null) { List <WebElement> lstTagAside =
		 * commonLibrary.isExist_List(divIdBrowserMenu,UIMAP_Home.lstTagAside,
		 * 20); List<WebElement> lstTagListItems =
		 * commonLibrary.isExist_List(lstTagAside
		 * .get(0),UIMAP_Home.lstTagListItems, 20); if(lstTagListItems.size()>0)
		 * for (WebElement button : lstTagListItems) {
		 * if(button.getText().contains("Sources")) { blnFirst=true;
		 * commonLibrary.clickButton_Parent_WithWait(button, "Sources"); break;
		 * }
		 * 
		 * } if(!blnFirst) report.updateTestLog("Click on Sources ",
		 * "Sources not present", Status.FAIL); }
		 */
		selectMenuFromBrowse("Sources");
		// To Select Category, Jurisdiction, Practice Area, Publisher,
		// Subscription
		WebElement divIdBrowserMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserMenu1, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems1 = commonLibrary.isExistList(lstTagAside1.get(1), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems1) {
			button.getText();
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

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select category child option in
	// Sources
	// # Function Name : selectcategorychildoptioninSources     
	// # Author : Ram
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources selectcategorychildoptioninSources(String option) {
		Boolean blnSecond = false;
		/*
		 * WebElement btnIdBrowse =
		 * commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
		 * commonLibrary.clickButton_Parent_WithWait(btnIdBrowse, "Browse"); try
		 * { commonLibrary.sleep(Mwait); } catch (Exception e) {
		 * System.out.println(e.toString()); throw new
		 * FrameworkException("Exception",e.toString()); } WebElement
		 * divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu,
		 * 20); if (divIdBrowserMenu!=null) { List <WebElement> lstTagAside =
		 * commonLibrary.isExist_List(divIdBrowserMenu,UIMAP_Home.lstTagAside,
		 * 20); List<WebElement> lstTagListItems =
		 * commonLibrary.isExist_List(lstTagAside
		 * .get(0),UIMAP_Home.lstTagListItems, 20); if(lstTagListItems.size()>0)
		 * for (WebElement button : lstTagListItems) {
		 * if(button.getText().contains("Sources")) { blnFirst=true;
		 * commonLibrary.clickButton_Parent_WithWait(button, "Sources"); break;
		 * }
		 * 
		 * } if(!blnFirst) report.updateTestLog("Click on Sources ",
		 * "Sources not present", Status.FAIL); }
		 */
		selectMenuFromBrowse("Sources");
		// To Select Category, Jurisdiction, Practice Area, Publisher,
		// Subscription
		WebElement divIdBrowserMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserMenu1, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems1 = commonLibrary.isExistList(lstTagAside1.get(1), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems1) {
			button.getText();
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

		WebElement divIdBrowserMenu2 = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside2 = commonLibrary.isExistList(divIdBrowserMenu2, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems2 = commonLibrary.isExistList(lstTagAside2.get(2), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems2) {
			button.getText();
			if (button.getText().contains("All Statutes and Legislation")) {
				WebElement sourceBtn = commonLibrary.isExist(button, By.tagName("button"), 20);
				blnSecond = true;
				if (browsername.contains("internet"))
					commonLibrary.clickJS(sourceBtn, "All Statutes and Legislation");
				else
					commonLibrary.clickButtonParentWithWait(sourceBtn, "All Statutes and Legislation");
				break;
			}
		}
		if (!blnSecond)
			report.updateTestLog("Click on Categories", "All Statutes and Legislation Option not present", Status.FAIL);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select category child option in
	// Sources
	// # Function Name : selectcategorychildoptioninSources     
	// # Author : Ram
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************
	public Sources selectchildoptionsinSources(String option) {
		Boolean blnFirst = false, blnSecond = false;
		WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
		commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
		try {
			commonLibrary.sleep(1000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		if (divIdBrowserMenu != null) {
			List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
			List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(0), UIMAP_Home.lstTagListItems, 20);
			if (lstTagListItems.size() > 0)
				for (WebElement button : lstTagListItems) {
					if (button.getText().contains("Sources")) {
						blnFirst = true;
						commonLibrary.clickButtonParentWithWait(button, "Sources");
						break;
					}

				}
			if (!blnFirst)
				report.updateTestLog("Click on Sources ", "Sources not present", Status.FAIL);
		}
		// To Select Category, Jurisdiction, Practice Area, Publisher,
		// Subscription
		WebElement divIdBrowserMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
		List<WebElement> lstTagAside1 = commonLibrary.isExistList(divIdBrowserMenu1, UIMAP_Home.lstTagAside, 20);
		List<WebElement> lstTagListItems1 = commonLibrary.isExistList(lstTagAside1.get(1), UIMAP_Home.lstTagListItems, 20);

		for (WebElement button : lstTagListItems1) {
			button.getText();
			if (button.getText().contains(option)) {
				blnSecond = true;
				if (browsername.contains("internet"))
					commonLibrary.clickJS(button, option);
				else
					commonLibrary.clickButtonParentWithWait(button, option);
				break;
			}
		}
		if (!blnSecond)
			report.updateTestLog("Click on Categories", option + " Option not present", Status.FAIL);

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to type the characters using robot
	// # Function Name : enterValueRobot     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void enterValueRobot(String word1) {
		try {
			String word = word1.toUpperCase();
			char ch1 = word.charAt(0);
			for (int i = 0; i < word.length(); i++) {
				ch1 = word.charAt(i);
				Robot robot = new Robot();
				switch (ch1) {
				case 'A': {
					robot.keyPress(KeyEvent.VK_A);
					break;
				}
				case 'B': {
					robot.keyPress(KeyEvent.VK_B);
					break;
				}
				case 'C': {
					robot.keyPress(KeyEvent.VK_C);
					break;
				}
				case 'D': {
					robot.keyPress(KeyEvent.VK_D);
					break;
				}
				case 'E': {
					robot.keyPress(KeyEvent.VK_E);
					break;
				}
				case 'F': {
					robot.keyPress(KeyEvent.VK_F);
					break;
				}
				case 'G': {
					robot.keyPress(KeyEvent.VK_G);
					break;
				}
				case 'H': {
					robot.keyPress(KeyEvent.VK_H);
					break;
				}
				case 'I': {
					robot.keyPress(KeyEvent.VK_I);
					break;
				}
				case 'J': {
					robot.keyPress(KeyEvent.VK_J);
					break;
				}
				case 'K': {
					robot.keyPress(KeyEvent.VK_K);
					break;
				}
				case 'L': {
					robot.keyPress(KeyEvent.VK_L);
					break;
				}
				case 'M': {
					robot.keyPress(KeyEvent.VK_M);
					break;
				}
				case 'N': {
					robot.keyPress(KeyEvent.VK_N);
					break;
				}
				case 'O': {
					robot.keyPress(KeyEvent.VK_O);
					break;
				}
				case 'P': {
					robot.keyPress(KeyEvent.VK_P);
					break;
				}
				case 'Q': {
					robot.keyPress(KeyEvent.VK_Q);
					break;
				}
				case 'R': {
					robot.keyPress(KeyEvent.VK_R);
					break;
				}
				case 'S': {
					robot.keyPress(KeyEvent.VK_S);
					break;
				}
				case 'T': {
					robot.keyPress(KeyEvent.VK_T);
					break;
				}
				case 'U': {
					robot.keyPress(KeyEvent.VK_U);
					break;
				}
				case 'V': {
					robot.keyPress(KeyEvent.VK_V);
					break;
				}
				case 'W': {
					robot.keyPress(KeyEvent.VK_W);
					break;
				}
				case 'X': {
					robot.keyPress(KeyEvent.VK_X);
					break;
				}
				case 'Y': {
					robot.keyPress(KeyEvent.VK_Y);
					break;
				}
				case 'Z': {
					robot.keyPress(KeyEvent.VK_Z);
					break;
				}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to perform search in global
	// navigation
	// // bar
	// // # Function Name : simpleSearch     
	// // # Author : Uma
	// // # Date Created : Jan'15
	// //
	// #*****************************************************************************************************************************
	// public Search simpleSearch(String strSearchTerm, Boolean strClearFilter)
	// {
	// WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch,
	// 20);
	// eltSearchbox.clear();
	// commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm,
	// "SearchTerm");
	//
	// if (strClearFilter) {
	//
	// WebElement filterIdCount =
	// commonLibrary.isExistNegative(UIMAP_Home.FilterIdCount, 2);
	// if (filterIdCount != null) {
	// WebElement btnClassFilter =
	// commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
	// // commonLibrary.highlightElement(btnClassFilter);
	// commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
	//
	// WebElement btnClassClearFilter =
	// commonLibrary.isExist(UIMAP_Home.btnClassClearFilter, 20);
	// // commonLibrary.highlightElement(btnClassClearFilter);
	// commonLibrary.clickLinkWithWebElementWithWait(btnClassClearFilter,
	// "Clear");
	//
	// WebElement btnIdSubSearch =
	// commonLibrary.isExist(UIMAP_Home.btnIdSubSearch, 20);
	// if (browsername.contains("internet"))
	// commonLibrary.clickJS(btnIdSubSearch, "Search");
	// else
	// commonLibrary.clickLinkWithWebElementWithWait(btnIdSubSearch, "Search");
	// } else {
	// WebElement eltSearchbutton =
	// commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
	// // commonLibrary.highlightElement(eltSearchbutton);
	// if (browsername.contains("internet"))
	// commonLibrary.clickJS(eltSearchbutton, "Search");
	// else
	// commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
	// }
	// } else {
	// WebElement eltSearchbutton =
	// commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
	// // commonLibrary.highlightElement(eltSearchbutton);
	// if (browsername.contains("internet"))
	// commonLibrary.clickJS(eltSearchbutton, "Search");
	// else
	// commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
	// }
	//
	// if (driver.getCurrentUrl().contains("usresearchhome")) {
	// commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm,
	// "SearchTerm");
	// WebElement eltSearchbutton =
	// commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
	// // commonLibrary.highlightElement(eltSearchbutton);
	//
	// if (browsername.contains("internet"))
	// commonLibrary.clickJS(eltSearchbutton, "Search");
	// else
	// commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
	// }
	//
	// return new Search(scriptHelper);
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to click AddSourceFilter button
	// # Function Name : clickAddSourceFilter     
	// # Author : Arvind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Sources clickAddSourceFilter() {
		WebElement eltAddSourceFilterButton = commonLibrary.isExistNegative(UIMAP_Sources.eltAddSourceFilterButton, 10);
		if (eltAddSourceFilterButton != null) {
			commonLibrary.clickButtonParentWithWait(eltAddSourceFilterButton, eltAddSourceFilterButton.getText());
			report.updateTestLog("Click on Add source as a search filter.", "Add source as a search filter option is clicked.", Status.PASS);
		} else {
			report.updateTestLog("Click on Add source as a search filter.", "Add source as a search filter option is not present.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking the source name from TOC
	// page
	// # Function Name : clickSourceName_TOC
	// # Author : karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public TOC clickSourceName_TOC(String sourceName) {
		boolean blnFlag = false;
		WebElement sourceresults = commonLibrary.isExist(UIMAP_Sources.sourceresults, 20);
		if (sourceresults != null) {
			WebElement ollist = commonLibrary.isExist(sourceresults, UIMAP_Sources.ollist, 20);
			if (ollist != null) {
				List<WebElement> divActionbar = commonLibrary.isExistList(ollist, UIMAP_Sources.lilist, 20);
				for (WebElement div : divActionbar) {
					if (div != null) {
						if (div.getAttribute("class").contains("indent")) {
							WebElement link = commonLibrary.isExist(div, By.tagName("a"), 20);
							if (link != null && link.getText().toLowerCase().contains(sourceName.toLowerCase())) {
								blnFlag = true;
								commonLibrary.clickButtonLogSmallWait(link, link.getText());
								break;
							}
						}
						WebElement button = commonLibrary.isExist(div, By.tagName("button"), 20);
						if (button != null) {
							WebElement h3tag = commonLibrary.isExist(button, By.tagName("h3"), 10);

							if (h3tag != null && h3tag.getText().contains(sourceName)) {
								blnFlag = true;
								commonLibrary.clickButtonLogSmallWait(button, h3tag.getText());
								break;
							}
						}
					}
				}
				WebElement btnStar = commonLibrary.isExist(UIMAP_TOC.btnstarverify, 20);
				int count = 0;
				do {
					count++;
				} while (btnStar == null && count < 20);
				pageCheck.documentState(driver);
				if (!blnFlag) {
					report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
				}
			}
		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the All Sources option
	// under Browse Menu
	// # Function Name : navigateToAllSources
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Sources navigateToAllSources() {
		Boolean blnFirst = false;
		WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
		commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
		try {
			commonLibrary.sleep(3000);
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
	// # Function Description : Function to verify display of information bubble
	// in SS
	// # Function Name : verifyinfodisplay
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources selectSourceName(String Searchterm) {
		String headers = dataTable.getData("General_Data", "HeadersinDoc");
		String[] headersList = headers.split(";");
		WebElement searchwithinsources = commonLibrary.isExist(UIMAP_Sources.searchwithinbutton, 20);
		commonLibrary.setDataInTextBox(UIMAP_Sources.searchwithinsources, Searchterm);
		commonLibrary.clickButtonParentWithWait(searchwithinsources, "Search");
		WebElement infobutton = commonLibrary.isExist(UIMAP_Sources.infobutton, 20);
		commonLibrary.clickButtonParentWithWait(infobutton, "Information");
		WebElement infowrapper = commonLibrary.isExist(UIMAP_Sources.infowrapper, 20);
		WebElement spanclass = commonLibrary.isExist(infowrapper, UIMAP_Sources.spanclass, 20);
		List<WebElement> totalheaders = commonLibrary.isExistList(spanclass, UIMAP_Sources.headers, 20);
		for (WebElement headinglist : totalheaders) {
			String headings = headinglist.getText();
			for (int i = 0; i < headersList.length; i++) {
				if (headersList[i].contains(headings)) {
					report.updateTestLog("Headers in Info Bubble is verified", headersList[i] + " header is present", Status.PASS);
				}
			}
		}
		commonLibrary.clickButtonParentWithWait(infobutton, "Information");
		commonLibrary.sleep(3000);
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Sources.Extrainfoenabled, 2);
		if (btnClassFilter == null) {
			report.updateTestLog("Verify Expanded or Collapsed Option", "Information bubble is Collapsed", Status.PASS);
		} else {
			report.updateTestLog("Verify Expanded or Collapsed Option", "Information bubble is Expanded", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify display of information bubble
	// in SS
	// # Function Name : verifyinfodisplay
	// # Author : Ram
	// # Date Created : March'15
	// #*****************************************************************************************************************************
	public Sources clearSearch() {
		WebElement clear = commonLibrary.isExist(By.cssSelector("button[class='clearSearch']"), 20);
		if (clear != null)
			commonLibrary.clickButtonParentWithWait(clear, "Clear search");
		else {
			report.updateTestLog("Click on Clear search", "Clear search button is not clicked", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Create a Publication Alert in
	// Source
	// dropdown.
	// # Function Name : clickCreatePubAlert     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Sources clickCreatePubAlert() {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceDropDownList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceDropDown, 10);
		for (WebElement sourceDropDown : sourceDropDownList) {
			if (sourceDropDown.isDisplayed()) {
				List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_Sources.lstTagListItems, 10);
				for (WebElement option : sourceDropDownOptions) {
					WebElement link = commonLibrary.isExistNegative(option, UIMAP_Sources.lnkLinks, 10);

					if (link != null && link.getText().toLowerCase().contains("create a publication alert")) {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].focus();", link);
						executor.executeScript("arguments[0].click();", link);

						commonLibrary.sleep(5000);

						report.updateTestLog("Click on button Get Documents", "Clicked on button Get Documents", Status.PASS);
						// commonLibrary.clickLink_withWebElement_WithWait(link,
						// "Get Documents");
						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on Get Documents.", "Get Documents option is not present.", Status.FAIL);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the source to filter without
	// waiting.
	// # Function Name : addSourceToFilterNoWait     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Sources addSourceToFilterNoWait(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResults = commonLibrary.isExistNegative(UIMAP_Sources.sourceresults, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourceResults, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
				commonLibrary.clickButtonLogSmallWait(button, sourceName);
				blnFlag = true;
				WebElement dropDown = commonLibrary.isExist(source, UIMAP_Sources.actionList, 10);
				WebElement addFilter = commonLibrary.isExist(dropDown, UIMAP_Sources.addfilter, 10);
				commonLibrary.clickButtonParentWithWait(addFilter, "Add source as a search filter");
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		// WebElement dropDown = commonLibrary.isExist(UIMAP_Sources.actionList,
		// 10);
		// WebElement addFilter =
		// commonLibrary.isExist(dropDown,UIMAP_Sources.addfilter, 10);
		// commonLibrary.clickButton_Parent_WithWait(addFilter,
		// "Add source as a search filter");

		// List<WebElement> sourceDropDownList =
		// commonLibrary.isExist_List(sourcePageRightContent,UIMAP_Sources.sourceDropDown,
		// 10);
		// for(WebElement sourceDropDown:sourceDropDownList)
		// {
		// if(sourceDropDown.isDisplayed())
		// {
		// List<WebElement> sourceDropDownOptions =
		// commonLibrary.isExist_List(sourceDropDown,
		// UIMAP_Sources.lstTagListItems, 10);
		// for(WebElement option : sourceDropDownOptions)
		// {
		// WebElement link = commonLibrary.isExist_Negative(option,
		// UIMAP_Sources.lnkLinks, 10);
		//
		// if(link!=null &&
		// link.getText().contains("Add source as a search filter"))
		// {
		//
		// commonLibrary.clickLink_withWebElement_WithWait(link,
		// "Add source as a search filter");
		// blnFlag=true;
		// break;
		// }
		// }
		// if(blnFlag)
		// break;
		// }
		// }
		// if(!blnFlag)
		// report.updateTestLog("Click on Add source as a search filter",
		// "Add source as a search filter is not present.", Status.FAIL);

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Sources verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify post filter presence in
	// source selection page
	// # Function Name : verifyBannerMessage     
	// # Author : Vennila
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Sources verifyPostFilterPresence(String header, boolean value) {

		boolean flag = false;
		WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
		List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);
		for (WebElement li : filterHeader) {
			if (li.getText().toLowerCase().contains(header.toLowerCase())) {
				value = true;
				flag = true;
				break;
			}
		}
		if (!flag)
			value = false;
		if (value = true) {
			report.updateTestLog("Verify " + header + " filters are present", header + " filters are present.", Status.PASS);
		}
		if (value = false) {
			report.updateTestLog("Verify " + header + " filters are absent", header + " filters are absent.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : SelectPostFilter_Multiple     
	// # Author : Vennila
	// # Date Created : 28 Sep 15
	// #*****************************************************************************************************************************

	public Sources selectPostFilterMultipleNew(String strHeader, String strSubHeader, String strFilter) {
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

		} else if (strHeader != "" && strHeader.toLowerCase().contains("category")) {
			filters = commonLibrary.isExist(UIMAP_SearchResult.filtersCategory, 10);

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
					if (!(ullist21.size() > 0)) {
						ullist21 = commonLibrary.isExistList(formclass, UIMAP_VSASearchResults.ullist, 20);
					}
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

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Alphanumeric links
	// displayed in source page.
	// # Function Name : clickAlphalink
	// # Author : Vennila
	// # Date Created : 29 Sep 15
	// #*****************************************************************************************************************************

	public Sources clickAlphalink(String linkname) {

		boolean blnFlag = false;

		WebElement alphaLink = commonLibrary.isExist(UIMAP_Sources.alphaLink, 50);
		List<WebElement> alphaBtnList = commonLibrary.isExistList(alphaLink, UIMAP_Sources.button, 50);
		for (int i = 0; i < alphaBtnList.size(); i++) {
			if (!alphaBtnList.get(i).getAttribute("class").contains("inactive")) {
				if (alphaBtnList.get(i).getText().toUpperCase().contains(linkname.toUpperCase())) {

					commonLibrary.clickButtonLogSmallWait(alphaBtnList.get(i), linkname);
					blnFlag = true;
					break;
				}

			}

		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + linkname, "Source: " + linkname + " is not present.", Status.FAIL);
		}
		// commonLibrary.sleep(300000);
		return new Sources(scriptHelper);
	}

	public Sources verifynarrowbyfilterleftpane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section contains " + strFilter, Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Sources selectPostFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;
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
								commonLibrary.clickButtonLogSmallWait(button, "More");
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

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectssPostFilter
	// # Author : Vennila
	// # Date Created : 7 Oct '15
	// #*****************************************************************************************************************************

	public Sources selectssPostFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;
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
								commonLibrary.clickButtonLogSmallWait(button, "More");
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

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get documents for source
	// # Function Name : getDocuments     
	// # Author : Vennila
	// # Date Created : 30 Sep 15
	// #*****************************************************************************************************************************
	public Search clickgetDocuments_new(String sourceName) {

		boolean blnFlag = false;

		WebElement sourceResults = commonLibrary.isExistNegative(UIMAP_Sources.sourceresults, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourceResults, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
				commonLibrary.clickButtonLogSmallWait(button, sourceName);
				blnFlag = true;
				WebElement dropDown = commonLibrary.isExist(source, UIMAP_Sources.actionList, 10);
				WebElement getDoc = commonLibrary.isExist(dropDown, UIMAP_Sources.getDocuments, 10);
				commonLibrary.clickButtonParentWithWait(getDoc, "Get documents");
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on get document for source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}

		commonLibrary.sleep(300000);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : verifynarrowbyssfilterleftpane
	// # Author : Vennila
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Sources verifynarrowbyssfilterleftpane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section contains " + strFilter, Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify action dropdown
	// # Function Name : verifyActionDropdowncollapsed
	// # Author : Sriram
	// # Date Created : Ded'15
	// #*****************************************************************************************************************************
	public Sources verifyActionDropdowncollapsed(String text) {
		boolean blnFlag = false;
		List<WebElement> actionBars = commonLibrary.isExistList(UIMAP_Sources.actionBars, 20);
		for (WebElement list : actionBars) {
			if (list.getText().contains(text)) {
				blnFlag = true;
				break;
			}
		}

		if (blnFlag)
			report.updateTestLog("To Verify action bar collapsed for " + text + "source", "Action bar is collapsed for " + text + "source ", Status.PASS);
		else
			report.updateTestLog("To Verify action bar collapsed for " + text + "source", "Action bar is not collapsed for " + text + "source", Status.FAIL);

		return new Sources(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filter selected in
	// prefilter pop up
	// # Function Name : VerifyFilterSelectedNew    
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Sources verifyFilterSelectedNew(String strSelectedFilterOptions) {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {
			boolean blnFlag = false;
			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 20);
			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {

					for (WebElement item : divClassDeleteFilter) {
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							blnFlag = true;
							break;
						}
					}
					if (blnFlag)
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + "  is displayed in NarrowBy textbox", Status.PASS);

					else
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy textbox", arrSelectedFilterOptions[i] + "  is not displayed in NarrowBy textbox", Status.FAIL);

				}
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify More option in prefilter pop
	// up
	// # Function Name : verifyMoreOption   
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Sources verifyMoreOption() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}
		WebElement moreOption = commonLibrary.isExist(UIMAP_Home.moredropdown, 20);
		WebElement clickmore = commonLibrary.isExist(moreOption, UIMAP_Home.clickmore, 20);
		if ((moreOption != null) && (moreOption.getText().contains("more"))) {
			if (clickmore != null)
				report.updateTestLog("Verify More drop down is displayed at the end of the applied filter", "More drop down is displayed at the end of the applied filter", Status.PASS);
			else
				report.updateTestLog("Verify More drop down is displayed at the end of the applied filter", "More drop down is not displayed at the end of the applied filter", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the banner message with
	// dismiss button
	// # Function Name : verifyBannerMessageNew  
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Sources verifyBannerMessageNew(String text) {
		WebElement bannermsg = commonLibrary.isExist(UIMAP_WorkFolders.bnrmsg, 20);
		if (bannermsg != null && bannermsg.getText().toLowerCase().contains(text.toLowerCase())) {
			WebElement Dismiss = commonLibrary.isExist(bannermsg, UIMAP_WorkFolders.dismiss, 20);
			if (Dismiss != null && Dismiss.getText().toLowerCase().contains("dismiss"))
				report.updateTestLog("Verify Banner message" + text + "is displayed with Dismiss button", "Banner message" + text + "is displayed with Dismiss button", Status.PASS);
			else
				report.updateTestLog("Verify Banner message" + text + "is displayed with Dismiss button", "Banner message" + text + "is not displayed with Dismiss button", Status.FAIL);
		} else
			report.updateTestLog("Verify Banner message" + text + "is displayed with Dismiss button", "Banner message" + text + "is not displayed with Dismiss button", Status.WARNING);

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify More option is not displayed
	// in prefilter pop up
	// # Function Name : verifyMoreOptionNotDisplayed   
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Sources verifyMoreOptionNotDisplayed() {

		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
		}

		WebElement moreOption = commonLibrary.isExist(UIMAP_Home.moredropdown, 20);

		if (moreOption == null)
			report.updateTestLog("Verify More drop down is not displayed at the end of the applied filter", "More drop down is not displayed at the end of the applied filter", Status.PASS);
		else
			report.updateTestLog("Verify More drop down is not displayed at the end of the applied filter", "More drop down is displayed at the end of the applied filter", Status.FAIL);

		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear applied pre filters.
	// # Function Name : closePreFilterPopup
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Sources closePreFilterPopup() {

		WebElement menu = commonLibrary.isExist(UIMAP_Home.menu, 20);
		WebElement btnClosePreFilterPopup = commonLibrary.isExist(menu, UIMAP_Home.btnClosePreFilterPopup, 10);

		if (btnClosePreFilterPopup != null) {
			commonLibrary.clickButtonLogSmallWait(btnClosePreFilterPopup, "Close PreFilter PopUp");
			WebElement preFiltersDiv = commonLibrary.isExistNegative(UIMAP_SearchResult.preFiltersDiv, 10);
			if (preFiltersDiv == null)
				report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is closed.", Status.PASS);
			else
				report.updateTestLog("Verify pre-filters pop-up is closed.", "Pre-filters pop-up is not closed.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click remove icon in more dropdown
	// # Function Name : clickRemoveIconInMoredropdown
	// # Author : Sriram
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Sources clickRemoveIconInMoredropdown(String text) {

		WebElement moredropdown = commonLibrary.isExist(UIMAP_Home.moredropdown, 20);
		WebElement clickmore = commonLibrary.isExist(moredropdown, UIMAP_Home.btnIdFilterMenu, 20);
		if (clickmore != null) {
			commonLibrary.clickButtonParentWithWait(clickmore, "more");

			WebElement removeFilter = commonLibrary.isExist(UIMAP_Home.removeFilter, 20);
			WebElement remove = commonLibrary.isExist(removeFilter, UIMAP_Home.btnIdFilterMenu, 20);

			if (remove != null && remove.getAttribute("aria-label").contains(text))
				commonLibrary.clickButtonParentWithWait(remove, "remove source");
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Get documents for a source
	// from Source tab
	// # Function Name : clickGetDocumentsSourcePage
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickGetDocumentsSourcePage() {

		boolean blnFlag = false;

		WebElement sourceDropDown = commonLibrary.isExist(UIMAP_LexisAdvanceTax.sourcePop, 10);

		// if (sourceDropDown.isDisplayed()) {
		List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_LexisAdvanceTax.sourceItems, 10);
		for (WebElement option : sourceDropDownOptions) {
			WebElement link = commonLibrary.isExistNegative(option, UIMAP_LexisAdvanceTax.getdocssource1, 10);

			if (link != null && link.getText().toLowerCase().contains("Get All Source Documents")) {
				commonLibrary.clickLinkWithWait(link, link.getText());
				commonLibrary.sleep(10000);

				report.updateTestLog("Click on button Get Documents", "Clicked on button Get Documents", Status.PASS);
				int counter = 0;
				do {
					commonLibrary.sleep(10000);
					counter = counter + 1;
				} while (!driver.getCurrentUrl().contains("taxsearchresults") && counter <= 15);

				blnFlag = true;
				break;
			}
		}

		// }

		if (!blnFlag)
			report.updateTestLog("Click on Get Documents.", "Get Documents option is not present.", Status.FAIL);

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Get Documents in Source from
	// LAT
	// dropdown.
	// # Function Name : clickGetDocuments1     
	// # Author : Meera
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults clickGetDocuments1() {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceDropDownList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceDropDown, 10);
		for (WebElement sourceDropDown : sourceDropDownList) {
			if (sourceDropDown.isDisplayed()) {
				List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_Sources.lstTagListItems, 10);
				for (WebElement option : sourceDropDownOptions) {
					WebElement link = commonLibrary.isExistNegative(option, UIMAP_Sources.lnkLinks1, 10);

					if (link != null && link.getText().toLowerCase().contains("get all source documents")) {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].focus();", link);
						executor.executeScript("arguments[0].click();", link);

						commonLibrary.sleep(10000);

						report.updateTestLog("Click on button Get Documents", "Clicked on button Get Documents", Status.PASS);
						int counter = 0;
						do {
							commonLibrary.sleep(10000);
							counter = counter + 1;
						} while (!driver.getCurrentUrl().contains("search") && counter <= 15);

						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on Get Documents.", "Get Documents option is not present.", Status.FAIL);
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Sources verifyNarrowbyFilterLeftPane1(String practice) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Home.filter1, 20);
		if (usedfilter.getText().contains(practice)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + practice, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + practice, Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Click a Link in Practice Area page
	// # Function Name : navigateTOBrowseLinksAndVerify
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Sources navigateTOBrowseLinksAndVerify(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName) {
		try {
			commonLibrary.sleep(100);
			Boolean blnFirst = false, blnSecond = false, blnThird = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			// commonLibrary.sleep(wait);
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
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Sources verifyNarrowbyFilterLeftPane2(String practice) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Home.filter2, 20);
		if (usedfilter.getText().contains(practice)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + practice, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + practice, Status.FAIL);
		}
		return new Sources(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to click Add All These as Search Filters button
	// # Function Name : clickaddAllSearchFilters     
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Sources clickaddAllSearchFilters() {
		WebElement addAllSearchFilters = commonLibrary.isExist(UIMAP_Sources.addAllSearchFilters, 10);
		if (addAllSearchFilters != null) {
			commonLibrary.clickButtonParentWithWait(addAllSearchFilters, addAllSearchFilters.getText());
			report.updateTestLog("Click on Add All These as Search Filters.", "Add source as a search filter option is clicked.", Status.PASS);
		} else {
			report.updateTestLog("Click on Add All These as Search Filters.", "Add source as a search filter option is not clicked.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify more dropdown under pre filter dropdown
	// # Function Name : verifyMoreDropDownInPreFilter     
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Sources verifyMoreDropDownInPreFilter() {
		WebElement menuFilt = commonLibrary.isExist(UIMAP_Sources.menuFilt, 10);
		WebElement filtMoreDP = commonLibrary.isExist(menuFilt, UIMAP_Sources.filtMoreDP, 10);
		if (filtMoreDP != null) {
			report.updateTestLog("Verify More dropdown has been displayed under Pre-Filter POP", "More dropdown has been displayed under Pre-Filter POP", Status.PASS);
		} else {
			report.updateTestLog("Verify More dropdown has been displayed under Pre-Filter POP", "More dropdown not displayed under Pre-Filter POP", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to click Per Filter DropDown
	// # Function Name : clickPerFilterDropDown     
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Sources clickPerFilterDropDown() {
		WebElement preFiltDP = commonLibrary.isExist(UIMAP_Sources.preFiltDP, 10);
		WebElement filtSpan = commonLibrary.isExist(preFiltDP, UIMAP_Sources.filtSpan, 10);
		WebElement filtBut = commonLibrary.isExist(filtSpan, UIMAP_Sources.filtBut, 10);
		if (filtBut != null) {
			commonLibrary.clickButtonParentWithWait(filtBut, "Pre filter dropdown");
			report.updateTestLog("Click on Pre filter dropdown", "Pre filter drop down has been clicked.", Status.PASS);
		} else {
			report.updateTestLog("Click on Pre filter dropdown", "Pre filter drop down has not clicked.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify narrow by section in pre filter
	// # Function Name : verifyNarrowBySectionInPreFilt     
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Sources verifyNarrowBySectionInPreFilt(String Narrowtext) {
		boolean flag = false;
		WebElement menuFilt = commonLibrary.isExist(UIMAP_Sources.menuFilt, 10);
		List <WebElement> perFiltNarrode = commonLibrary.isExistList(menuFilt, UIMAP_Sources.perFiltNarrode, 10);
		String[] narrowbylist = Narrowtext.split(";");
		if(menuFilt != null)
		{
			for(int a=0; a<perFiltNarrode.size(); a++)
			{
				if(perFiltNarrode.get(a).getText().equals(narrowbylist[a]))
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
			}
		}
		if(flag)
		{
			report.updateTestLog("Verify all the applied source filters are displayed ", "All the applied source filters are displayed ", Status.PASS);
		}
		else
		{
			report.updateTestLog("Verify all the applied source filters are displayed ", "All the applied source filters are not displayed ", Status.FAIL);
		}
		
		flag = false;
		return new Sources(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to click more dropdown under pre filter dropdown
	// # Function Name : clickMoreDropDownInPreFilter     
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public Sources clickMoreDropDownInPreFilter() {
		WebElement menuFilt = commonLibrary.isExist(UIMAP_Sources.menuFilt, 10);
		WebElement filtMoreDP = commonLibrary.isExist(menuFilt, UIMAP_Sources.filtMoreDP, 10);
		if (filtMoreDP != null) {
			commonLibrary.clickButtonParentWithWait(filtMoreDP, "more");
			WebElement moreSectionNarro = commonLibrary.isExist(menuFilt, UIMAP_Sources.moreSectionNarro, 10);
			if (moreSectionNarro != null)
			{
				report.updateTestLog("Verify More dropdown has been expanded under Pre-Filter POP", "More dropdown has been expanded under Pre-Filter POP", Status.PASS);
			}
			else
			{
				report.updateTestLog("Verify More dropdown has been expanded under Pre-Filter POP", "More dropdown not expanded under Pre-Filter POP", Status.FAIL);
			}
				
		} else {
			report.updateTestLog("Verify More dropdown has been expanded under Pre-Filter POP", "More dropdown not expanded under Pre-Filter POP", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}
}
