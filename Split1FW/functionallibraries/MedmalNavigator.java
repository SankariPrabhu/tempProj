package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

public class MedmalNavigator extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public MedmalNavigator(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalhome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmalhome") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmalhome")) {
			throw new IllegalStateException("Medmal Navigator home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public MedmalNavigator rightClickProductLogoOpenInNewWindow() {
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
				report.updateTestLog("Click on open in New Window link in Rightclick menu", "Secondary browser is displayed", Status.PASS);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
					if (CurrentProduct.getText().contains("MedMal Navigator®")) {
						count++;
					}
				}
				if (count >= 2) {
					report.updateTestLog("MedMal Navigator® landing page is displayed in the secondary browser", "MedMal Navigator® landing page is displayed in the secondary browser", Status.PASS);
				} else {
					report.updateTestLog("MedMal Navigator® landing page is displayed in the secondary browser", "MedMal Navigator® landing page is not displayed in the secondary browser", Status.FAIL);
				}

				driver.close();
				report.updateTestLog("Close the secondary browser", "Secondary browser is closed", Status.PASS);
				commonLibrary.switchToWindow("medmalhome");
			} else {
				report.updateTestLog("Click on Open in New Window link in Rightclick menu", "Secondary browser is not displayed", Status.FAIL);
			}

			return new MedmalNavigator(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Verdicts &
	// Settlements Analyzer page by clicking Lexis Advance arrow
	// # Function Name : NavigateToVerdictsSettlementsPage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public VerdictsSettlements navigateToVerdictsSettlementsPage() {
		try {
			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);

			WebElement btnLexisAdvanceVSA = commonLibrary.isExist(UIMAP_Home.btnActionVSA, 20);
			commonLibrary.clickLinkWithWebElement(btnLexisAdvanceVSA, "Lexis Advance® Verdicts & Settlements Analyzer");
			commonLibrary.sleep(3000);

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("Verdict & Settlement Analyzer")) {
				report.updateTestLog(" Verdicts & Settlements Analyzer landing page is displayed", " Verdicts & Settlements Analyzer landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog(" Verdicts & Settlements Analyzer Suite landing page is displayed", " Verdicts & Settlements Analyzer Suite landing page is not displayed", Status.FAIL);
			}

			return new VerdictsSettlements(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click the Topic and sub topic link
	// in the Medmal Navigator
	// # Function Name : clicksectionlinks
	// # Parameters: topicName,subTopicName
	// #Return Page: MedmalDashborad
	// # Author : Karthikayan
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public MedmalDashborad clicksectionlinks(String topicName, String subTopicName) {

		Boolean blnFlag = false;
		List<WebElement> medicalTopicslist = null;
		WebElement medicalTopicsOld = null, medicalTopicsNew = null;
		WebElement medicalTopics = commonLibrary.isExist(UIMAP_MedmalNavigator.expandCollapse);
		medicalTopicsOld = medicalTopics;
		medicalTopicslist = commonLibrary.isExistList(medicalTopics, By.tagName("li"), 20);

		for (int i = 0; i < medicalTopicslist.size(); i++)

		{

			if (medicalTopicslist.get(i).getText().contains(topicName)) {
				// List<WebElement> expandTopic =
				// commonLibrary.isExist_List(medicalTopicslist.get(i),
				// By.tagName("button"), 20);
				WebElement expandTopic = commonLibrary.isExist(medicalTopicslist.get(i), By.tagName("button"), 20);
				commonLibrary.clickButtonParentWithWait(expandTopic, topicName);

				break;
			}
		}
		medicalTopicsNew = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.expandCollapse, 3);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(300000);
				count++;
				medicalTopicsNew = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.expandCollapse, 3);
			} while (medicalTopicsOld.equals(medicalTopicsNew) && count < 20);
		} catch (Exception e) {
			commonLibrary.sleep(1000000);
			System.out.println(e.toString());
		}

		medicalTopicslist = commonLibrary.isExistList(medicalTopics, By.tagName("li"), 20);
		for (WebElement topiclist : medicalTopicslist) {
			List<WebElement> subTopicLink = commonLibrary.isExistList(topiclist, By.tagName("a"), 20);
			for (int i = 0; i <= subTopicLink.size(); i++) {
				if (subTopicLink.get(i).getText().contains(subTopicName)) {
					{
						commonLibrary.clickLinkWithWebElement(subTopicLink.get(i), subTopicName);
						blnFlag = true;
						break;
					}

				}
			}
			if (blnFlag)
				break;
		}

		return new MedmalDashborad(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to topic and subtopic from Medmal Home
	// page.
	// # Function Name : selectTopic     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedmalDashborad selectTopic(String topic, String subtopic) {
		boolean blnFlag = false;
		WebElement secIdMainTopics = commonLibrary.isExist(UIMAP_SearchResult.secIdMainTopics1, 20);
		if (secIdMainTopics != null) {
			List<WebElement> lstTagName = commonLibrary.isExistList(secIdMainTopics, UIMAP_SearchResult.lstTagName, 20);
			for (WebElement item : lstTagName) {
				String title = item.getText().split("\n")[1];
				if (title.equalsIgnoreCase(topic)) {
					WebElement btnClassExpand = commonLibrary.isExist(item, UIMAP_SearchResult.btnClassExpand, 20);
					if (btnClassExpand != null)
						commonLibrary.clickButtonParentWithWait(btnClassExpand, "Expand " + topic);
					else
						report.updateTestLog("Click on " + topic + " Expand Arrow", topic + " Expand Arrow is already expanded", Status.PASS);
				}
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
				int count = 0;
				List<WebElement> subLinks = commonLibrary.isExistList(item, UIMAP_Home.lnkLinks, 10);
				do {
					subLinks = commonLibrary.isExistList(item, UIMAP_Home.lnkLinks, 10);
					commonLibrary.sleep(4000);
				} while (subLinks == null && count < 10);

				for (WebElement li : subLinks) {
					if (li.getText().toLowerCase().contains(subtopic.toLowerCase())) {
						commonLibrary.clickButtonParentWithWait(li, subtopic);
						blnFlag = true;
						break;
					}
				}
				if ((blnFlag))
					break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Click on " + subtopic + " under " + topic, subtopic + " under " + topic + " is not selected.", Status.FAIL);
		return new MedmalDashborad(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ViewAllTopic.
	// # Function Name : clickViewAllTopics     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedicalResearch clickViewAllTopics() {
		WebElement secIdMainTopics = commonLibrary.isExist(UIMAP_SearchResult.secIdMainTopics1, 20);
		WebElement viewAllTopics = commonLibrary.isExistNegative(secIdMainTopics, UIMAP_MedmalNavigator.viewAllTopics, 10);
		commonLibrary.clickButtonParentWithWait(viewAllTopics, "View All Medical Topics");
		return new MedicalResearch(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click the Topic and sub topic link
	// in the Medmal Navigator
	// # Function Name : clickTopicandSubTopic
	// # Parameters: topicName,subTopicName
	// #Return Page: MedmalDashborad
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalDashborad clickTopicandSubTopic(String topicName, String subTopicName) {

		Boolean blnFlag = false;
		List<WebElement> medicalTopicslist = null;
		WebElement medicalTopics = commonLibrary.isExist(UIMAP_MedmalNavigator.expandCollapse);
		medicalTopicslist = commonLibrary.isExistList(medicalTopics, By.tagName("li"), 20);

		for (int i = 0; i < medicalTopicslist.size(); i++)

		{

			if (medicalTopicslist.get(i).getText().contains(topicName)) {
				// List<WebElement> expandTopic =
				// commonLibrary.isExist_List(medicalTopicslist.get(i),
				// By.tagName("button"), 20);
				WebElement expandTopic = commonLibrary.isExist(medicalTopicslist.get(i), By.tagName("button"), 20);
				commonLibrary.clickButtonParentWithWait(expandTopic, topicName);

				break;
			}
		}
		medicalTopicslist = commonLibrary.isExistList(medicalTopics, By.tagName("li"), 20);
		for (WebElement topiclist : medicalTopicslist) {
			if (topiclist.getText().contains(topicName)) {
				List<WebElement> subTopicLink = commonLibrary.isExistList(topiclist, By.tagName("a"), 20);
				for (int i = 0; i <= subTopicLink.size(); i++) {
					if (subTopicLink.get(i).getText().contains(subTopicName)) {
						{
							commonLibrary.clickLinkWithWebElement(subTopicLink.get(i), subTopicName);
							blnFlag = true;
							break;
						}

					}
				}
				if (blnFlag)
					break;
			}
		}

		return new MedmalDashborad(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search in medmal
	// navigator page.
	// # Function Name : medmalSearch     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalNavigator medmalSearch(String strSearchTerm) {
		WebElement backToTopicListold = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.backToTopicList, 5);
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);

		commonLibrary.setDataInTextBox(eltSearchbox, strSearchTerm, "SearchTerm");
		commonLibrary.sleep(30000);
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);

		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");

		WebElement backToTopicList = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.backToTopicList, 5);
		int count = 0;
		if (backToTopicListold == null) {
			do {
				commonLibrary.sleep(20000);
				backToTopicList = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.backToTopicList, 5);
			} while (backToTopicList == null && count < 10);
		} else {
			do {
				commonLibrary.sleep(20000);
				backToTopicList = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.backToTopicList, 5);
			} while (backToTopicListold.equals(backToTopicList) && count < 10);
		}
		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : verifySearchTermHiglighted     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalNavigator verifySearchTermHiglighted(String topicHeader, String searchTerm) {
		WebElement topicHeaderCont = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.topicHeaderCont, 10);
		List<WebElement> topicHeaderList = commonLibrary.isExistList(topicHeaderCont, UIMAP_MedmalNavigator.listitem, 10);
		boolean isTopicPresent = false, isPresent = false;
		for (WebElement topicHead : topicHeaderList) {
			WebElement spanHead = commonLibrary.isExistNegative(topicHead, UIMAP_MedmalNavigator.topicName, 10);
			if (spanHead != null && spanHead.getText().toLowerCase().contains(topicHeader.toLowerCase())) {
				isTopicPresent = true;
				WebElement button = commonLibrary.isExistNegative(topicHead, UIMAP_MedmalNavigator.expandTopic, 5);
				if (button != null && button.getAttribute("data-isexpanded").contains("true"))
					report.updateTestLog("Verify 'Cancer Screening & Diagnosis' topic header expands and Sub Topics are displayed", "'Cancer Screening & Diagnosis' topic header expands and Sub Topics are displayed", Status.PASS);
				else
					report.updateTestLog("Verify 'Cancer Screening & Diagnosis' topic header expands and Sub Topics are displayed", "'Cancer Screening & Diagnosis' topic header is not expanded", Status.FAIL);
				WebElement subListCont = commonLibrary.isExistNegative(topicHead, UIMAP_MedmalNavigator.subListCont, 10);
				if (subListCont != null) {
					List<WebElement> subList = commonLibrary.isExistList(subListCont, UIMAP_MedmalNavigator.listitem, 10);
					for (WebElement item : subList) {
						WebElement strong = commonLibrary.isExistNegative(item, UIMAP_MedmalNavigator.strong, 10);
						if (strong != null && strong.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
							isPresent = true;
							report.updateTestLog("Verify the search term 'Cancer' is displayed anywhere in the results list and it is 'Bolded'", "The search term 'Cancer' is displayed anywhere in the results list and it is 'Bolded'", Status.PASS);
						} else
							report.updateTestLog("Verify the search term 'Cancer' is displayed anywhere in the results list and it is 'Bolded'", "The search term 'Cancer' is  not 'Bolded'", Status.FAIL);
						break;
					}
					if (!isPresent)
						report.updateTestLog("Verify the search term 'Cancer' is displayed anywhere in the results list and it is 'Bolded'", "The search term 'Cancer' is  not present", Status.FAIL);
					break;
				} else
					report.updateTestLog("Verify 'Cancer Screening & Diagnosis' topic header expands and Sub Topics are displayed", "'Cancer Screening & Diagnosis' topic header expands and Sub Topics are not displayed", Status.FAIL);
			}

		}
		if (!isTopicPresent)
			report.updateTestLog("Verify the search term 'Cancer' is displayed anywhere in the results list and it is 'Bolded'", "The search term 'Cancer' is  not present", Status.FAIL);
		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : verifyNoResultsDisplayed     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalNavigator verifyNoResultsDisplayed(String text) {
		WebElement noResultsCont = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.noResultsCont, 10);
		if (noResultsCont != null) {
			if (noResultsCont.getText().toLowerCase().contains(text.toLowerCase()))
				report.updateTestLog("Verify 'No results were found for aabbc' is displayed", "'No results were found for aabbc' is displayed", Status.PASS);
			else
				report.updateTestLog("Verify 'No results were found for aabbc' is displayed", "'No results were found for aabbc' is not displayed", Status.FAIL);
		} else
			report.updateTestLog("Verify 'No results were found for aabbc' is displayed", "Container is not displayed", Status.FAIL);
		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate back to topiclist page.
	// # Function Name : clickBackToTopicList     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalNavigator clickBackToTopicList() {
		WebElement backToTopicList = commonLibrary.isExistNegative(UIMAP_MedmalNavigator.backToTopicList, 10);
		commonLibrary.clickButtonParentWithWait(backToTopicList, "Back to topic list.");
		commonLibrary.sleep(10000);
		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Medmal navigator home page
	// # Function Name : verifyMedmalNavigatorHome     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public MedmalNavigator verifyMedmalNavigatorHome() {
		WebElement podHeader = commonLibrary.isExist(UIMAP_MedmalNavigator.podheader, 10);
		WebElement viewAllTopics = commonLibrary.isExist(podHeader,UIMAP_MedmalNavigator.viewAllTopics, 10);
		if (viewAllTopics != null)
			report.updateTestLog("Verify 'Medmal Navigator' landing page is displays", "'Medmal Navigator' landing page is displays.", Status.PASS);
		else
			report.updateTestLog("Verify 'Medmal Navigator' landing page is displays", "'Medmal Navigator' landing page is not displayed.", Status.FAIL);
		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the last client id display at
	// the top of the drop down
	// Medmal Navigator page
	// # Function Name : verifyLastClientId
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public MedmalNavigator verifyLastClientId(String clientId) {
		Boolean blnFlag = false;
		WebElement btnClassClient = commonLibrary.isExist(UIMAP_Home.btnClassClient, 20);
		if (btnClassClient != null) {
			if (btnClassClient.getText().toLowerCase().contains(clientId.toLowerCase())) {
				blnFlag = true;

			} else {
				blnFlag = false;

			}
		}
		if (blnFlag) {
			report.updateTestLog("Verified the last client id " + clientId + " displayed at the top of the drop down", "The client id " + clientId + " is selected at the top of  the dropdown menu", Status.PASS);
		} else {
			report.updateTestLog("Verified the last client id " + clientId + " displayed at the top of the drop dow", "The client id " + clientId + " is not selected at the top of the drop down menu", Status.FAIL);
		}

		return new MedmalNavigator(scriptHelper);
	}

}
