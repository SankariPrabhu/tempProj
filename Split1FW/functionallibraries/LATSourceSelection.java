package functionallibraries;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LATSourceSelection;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Sources;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

public class LATSourceSelection extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;

	public LATSourceSelection(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("tax"))
				commonLibrary.sleep(5000);

		} while (!url.contains("tax") && counter < 40);

		if (!driver.getCurrentUrl().contains("taxsourceselection")) {// taxsearchhome
			throw new IllegalStateException("Tax Source Selection page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on alphabetical letter in he
	// alphabar in Source selection
	// # Function Name : clickOnLetterAlphabetSource     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LATSourceSelection clickOnLetterAlphabetSource(String letter) {
		boolean flag = true;
		WebElement resultList = commonLibrary.isExist(UIMAP_LATSourceSelection.resultList, 10);
		WebElement alphabar = commonLibrary.isExist(resultList, UIMAP_LATSourceSelection.alphabar, 10);
		List<WebElement> lists = commonLibrary.isExistList(alphabar, UIMAP_LATSourceSelection.button, 10);
		for (WebElement button : lists) {
			if (button != null && button.getText().toLowerCase().contains(letter.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(button, letter);
				commonLibrary.sleep(10000);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click on letter " + letter, letter + " is not clicked", Status.FAIL);

		return new LATSourceSelection(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add to source for source
	// # Function Name : clickOnAddtoSource     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LATSourceSelection clickOnAddtoSource(String sourcename) {
		try {
			boolean flag1 = false;
			WebElement resultList = commonLibrary.isExist(UIMAP_LATSourceSelection.resultList, 10);
			WebElement section = commonLibrary.isExistNegative(resultList, UIMAP_LATSourceSelection.ol, 10);
			if (section != null) {
				// WebElement header = commonLibrary.isExistNegative(pod,
				// UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				// if
				// (header.getText().toLowerCase().contains(podName.toLowerCase()))
				// {

				List<WebElement> lists = commonLibrary.isExistList(section, UIMAP_LATSourceSelection.li, 10);
				for (WebElement item : lists) {
					WebElement header = commonLibrary.isExist(item, UIMAP_LATSourceSelection.header, 10);
					if (header != null && header.getText().toLowerCase().trim().contains(sourcename.toLowerCase().trim())) {
						WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LATSourceSelection.addtoSearch, 10);
						if (btnAdd != null) {
							commonLibrary.clickButtonParentWithWait(btnAdd, "add to search for source: " + sourcename);
							flag1 = true;
							break;
						}
					}
				}
				// }
			}
			if (flag1)
				report.updateTestLog("Select add to search icon near source " + sourcename + "", "add to search icon near source " + sourcename + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add to search icon near source " + sourcename + "", "add to search icon near source " + sourcename + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		// Function calling for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter in narrowby section
	// # Function Name : verifyNarrowBySection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LATSourceSelection verifyNarrowBySection(String strSelectedFilterOptions) {
		ArrayList<String> arrBtn = new ArrayList<String>();
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {
			WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
			if (filterTray != null) {
				WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
				List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
				for (WebElement item : filters) {
					List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
					for (WebElement btnItem : btn) {
						arrBtn.add(btnItem.getText());

					}
				}
				WebElement moreFilters = commonLibrary.isExist(filterTray, UIMAP_Home.divMoreFilter, 20);
				if (moreFilters != null) {
					commonLibrary.clickButtonParentWithWait(moreFilters, "More");

					WebElement sectionMoreFilter = commonLibrary.isExist(filterTray, UIMAP_Home.btnClassMoreFilters, 20);
					List<WebElement> filters1 = commonLibrary.isExistList(sectionMoreFilter, UIMAP_Home.lstTagListItems, 10);
					for (WebElement item : filters1) {
						List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
						for (WebElement btnItem : btn) {
							arrBtn.add(btnItem.getText());

						}
					}
				}

			}
			for (int i = 0; i < arrSelectedFilterOptions.length; i++) {
				if (arrBtn.contains(arrSelectedFilterOptions[i])) {
					report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", Status.PASS);
				} else {
					report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy section", Status.FAIL);
				}
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Search with option selected from WordWheel
	// # Function Name : searchWithinSourcesUsingWordWheel   
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LATSourceSelection searchWithinSourcesUsingWordWheel(String searchTerm, String wordWheelTerm, String verifyTerm) {
		boolean flag = false;
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_LATSourceSelection.searchWithinTextBox, 20);
		if (eltSearchbox != null) {
			commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "Search Within Sources");
			try {
				commonLibrary.sleep(500000);
			} catch (Exception e) {
				System.out.println(e.toString());

			}

			WebElement wordWheel = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.wordWheel, 10);
			if (wordWheel != null) {
				List<WebElement> options = commonLibrary.isExistList(wordWheel, UIMAP_LATSourceSelection.li, 10);
				if (options.size() > 0) {
					for (WebElement li : options) {
						if (li.getText().toLowerCase().contains(wordWheelTerm.toLowerCase()) && li.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
							report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is present in wordwheel.", Status.PASS);
							WebElement button = commonLibrary.isExistNegative(li, UIMAP_LATSourceSelection.button, 10);
							commonLibrary.clickMethod(button, li.getText());
							flag = true;
							break;
						}
					}
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + wordWheelTerm + " is present in wordwheel.", wordWheelTerm + " is not present in wordwheel.", Status.FAIL);

			eltSearchbox = commonLibrary.isExist(UIMAP_LATSourceSelection.searchWithinTextBox, 20);
			if (eltSearchbox.getAttribute("value").toLowerCase().contains(verifyTerm.toLowerCase()))
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is displayed in SearchBox.", Status.PASS);
			else
				report.updateTestLog("Verify " + verifyTerm + " is displayed in SearchBox.", verifyTerm + " is not displayed in SearchBox.", Status.FAIL);

			WebElement searchButton = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.searchWithinButton, 10);
			if (searchButton != null) {
				commonLibrary.clickButtonParentWithWait(searchButton, "Search");
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
			} else {
				report.updateTestLog("Click search within sources button", "Search within source button is not available", Status.FAIL);
			}

		} else
			report.updateTestLog("Enter " + searchTerm + " in search within sources", "Search within source text box is not available", Status.FAIL);
		return new LATSourceSelection(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the source name
	// # Function Name : verifySourceName     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LATSourceSelection verifySourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceresults = commonLibrary.isExist(UIMAP_Sources.sourceresults, 10);
		WebElement sourcePageRightContent = commonLibrary.isExist(sourceresults, UIMAP_Sources.sourcePageRightContent, 10);
		if (sourcePageRightContent != null) {
			List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceButton, 10);
			if (sourceList.size() > 0) {
				for (WebElement source : sourceList) {
					if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
						report.updateTestLog("Verify " + sourceName + " displays", sourceName + " displays", Status.PASS);
						blnFlag = true;
						break;
					}
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify " + sourceName + " displays", "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a source within All Sources
	// page.
	// # Function Name : clickSourceName     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LATSourceSelection clickSourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		if (sourcePageRightContent != null) {
			List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceButton, 10);
			if (sourceList.size() > 0) {
				for (WebElement source : sourceList) {
					if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
						WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
						commonLibrary.clickButtonLogSmallWait(button, sourceName);
						blnFlag = true;
						break;
					}
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify action menu is displayed for
	// the source
	// # Function Name : verifyActionMenuDisplays     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LATSourceSelection verifyActionMenuDisplays(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		if (sourcePageRightContent != null) {
			List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.expandedSourceButton, 10);
			if (sourceList.size() > 0) {
				for (WebElement source : sourceList) {
					if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
						if (source.getAttribute("class").equals("actionbar shown")) {
							report.updateTestLog("Verify action menu is displayed for the source " + sourceName, "Action menu is displayed for the source " + sourceName, Status.PASS);
							blnFlag = true;
							break;
						} else {
							WebElement actionList = commonLibrary.isExist(source, UIMAP_LATSourceSelection.actionList, 10);
							if (actionList != null) {
								report.updateTestLog("Verify action menu is displayed for the source " + sourceName, "Action menu is displayed for the source " + sourceName, Status.PASS);
								blnFlag = true;
								break;
							}
						}
					}
				}
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify action menu is displayed for the source " + sourceName, "Action menu is not displayed for the source " + sourceName, Status.FAIL);
		}
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select link under source
	// # Function Name : selectlinkundersource
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LATSourceSelection selectLinkUnderSource(String subLink) {
		WebElement selectedSource = commonLibrary.isExist(UIMAP_Sources.selectedsource, 20);
		WebElement actionList = commonLibrary.isExist(selectedSource, UIMAP_Sources.actionList, 20);
		if (actionList != null) {
			List<WebElement> listItems = commonLibrary.isExistList(actionList, UIMAP_Sources.lilist, 20);
			if (listItems.size() > 0) {
				for (WebElement item : listItems) {
					String actionName = item.getText().trim().toLowerCase();
					if (actionName.contains(subLink.toLowerCase())) {
						WebElement actionButton = commonLibrary.isExist(item, UIMAP_LATSourceSelection.button, 10);
						if (actionButton != null) {
							commonLibrary.clickMethod(actionButton, actionName);
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
							if (subLink.toLowerCase().contains("get all source documents")) {
								WebElement resultClass = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 3);
								int count = 0;
								do {
									count++;
									resultClass = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 3);
									if (resultClass == null)
										commonLibrary.sleep(5000);
								} while (resultClass == null && count <= 40);
							}
							break;
						}
					}
				}
			}
		}
		return new LATSourceSelection(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function is used to click clear link on Narrow by tray.
	// # Function Name : clickClearInNarrowBy     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	
	public LATSourceSelection clickClearInNarrowBy()
	{
		WebElement filtTray = commonLibrary.isExist(UIMAP_LexisAdvanceTax.filtTray, 20);
		WebElement clrLin = commonLibrary.isExist(UIMAP_LexisAdvanceTax.clrLin, 20);
		if(clrLin != null)
		{
			commonLibrary.clickLink(clrLin, "Clear");
			if(filtTray == null)
			{
				report.updateTestLog("Verify Narrow by tray has cleared", "Narrow By section has been cleared" , Status.PASS);
			}
			else
			{
				report.updateTestLog("Verify Narrow by tray has cleared", "Narrow By section not cleared" , Status.FAIL);
			}
		}
		else
		{
			report.updateTestLog("Verify clear link has been clicked", "Clear link not clicked" , Status.FAIL);
		}
		
		return new LATSourceSelection(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the particular tab in the
	// navigation bar
	// # Function Name : navigateToTab     
	// # Author : Aravind Russell V
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax navigateToTab(String tab) {

		// WebElement taxtab =
		// commonLibrary.isExist(UIMAP_LexisAdvanceTax.divTax, 10);
		WebElement divTabs = commonLibrary.isExist(UIMAP_LexisAdvanceTax.divTabs, 10);
		List<WebElement> lists = commonLibrary.isExistList(divTabs, UIMAP_LexisAdvanceTax.lists, 10);
		for (WebElement list : lists) {
			if (list.getText().toLowerCase().contains(tab.toLowerCase())) {
				WebElement button = commonLibrary.isExist(list, UIMAP_LexisAdvanceTax.links, 10);
				if (button != null) {
					commonLibrary.clickButtonParentWithWait(button, tab);
					break;
				}
			}
		}

		return new LexisAdvanceTax(scriptHelper);

	}
}