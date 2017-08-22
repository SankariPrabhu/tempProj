package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.*;
import com.cognizant.framework.Status;

public class MedicalResearch extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public MedicalResearch(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medical"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medical") && counter < 40);

		if (!driver.getCurrentUrl().contains("medical")) {
			throw new IllegalStateException("medical research page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Medical Research link
	// # Function Name : selectMedicalResearchLink     
	// # Author : Vidhya
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public MedicalResearch selectMedicalResearchLink(String linkName) {
		try {
			WebElement section = commonLibrary.isExist(UIMAP_MedmalNavigator.medicalResearch, 10);
			List<WebElement> topicList = commonLibrary.isExistList(section, By.tagName("a"), 10);
			for (WebElement item : topicList) {
				if (item.getText().contains(linkName)) {
					commonLibrary.clickLinkWithWebElement(item, linkName);
					commonLibrary.sleep(10000);// mandatory
					break;
				}

			}

		} catch (Exception e) {

		}
		return new MedicalResearch(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Selected Medical Topic Page
	// # Function Name : Logout     
	// # Author : Vidhya
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public MedicalResearch verifySelectedMedicalTopicPage(String Title, String subtopic, String docName) {
		try {
			Boolean blnTitle = false;
			Boolean blnsubtopic = false;
			Boolean blndocName = false;
			List<WebElement> topicList = commonLibrary.isExistList(By.tagName("header"), 10);
			for (WebElement item : topicList) {
				if (item.getText().toLowerCase().contains(Title.toLowerCase()))
					blnTitle = true;

				if (item.getText().toLowerCase().contains(subtopic.toLowerCase())) {
					blnsubtopic = true;

				}
				if (item.getText().toLowerCase().contains(docName.toLowerCase())) {

					blndocName = true;
				}

			}

			if (blnTitle)
				report.updateTestLog("Verify Title: " + Title + " is dispalyed", "Title:" + Title + " is dispalyed", Status.PASS);
			else
				report.updateTestLog("Verify Title: " + Title + " is dispalyed", "Title:" + Title + " is NOT dispalyed", Status.FAIL);

			if (blnsubtopic)
				report.updateTestLog("Verify header: " + subtopic + " is dispalyed", "header:" + subtopic + " is dispalyed", Status.PASS);
			else
				report.updateTestLog("Verify header: " + subtopic + " is dispalyed", "header:" + subtopic + " is NOT dispalyed", Status.FAIL);

			if (blndocName)
				report.updateTestLog("Verify header: " + docName + " is dispalyed", "header:" + docName + " is dispalyed", Status.PASS);
			else
				report.updateTestLog("Verify header: " + docName + " is dispalyed", "header:" + docName + " is NOT dispalyed", Status.FAIL);
			return new MedicalResearch(scriptHelper);
		} catch (Exception e) {
			return new MedicalResearch(scriptHelper);
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the topics
	// # Function Name : selectTopic     
	// # Author : Vidhya
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public MedmalDashborad selectTopic(String topic, String subtopic) {
		boolean blnFlag = false;
		WebElement secIdMainTopics = commonLibrary.isExist(UIMAP_SearchResult.secIdMainTopics1, 20);
		if (secIdMainTopics != null) {
			List<WebElement> lstTagName = commonLibrary.isExistList(secIdMainTopics, UIMAP_SearchResult.lstTagName, 20);
			for (WebElement item : lstTagName) {
				if (item.getText().contains(topic)) {
					WebElement btnClassExpand = commonLibrary.isExist(item, UIMAP_SearchResult.btnClassExpand, 20);
					if (btnClassExpand != null)
						commonLibrary.clickButtonParentWithWait(btnClassExpand, "Expand " + topic);
					else
						report.updateTestLog("Click on " + topic + " Expand Arrow", topic + " Expand Arrow is already expanded", Status.PASS);
				}
				List<WebElement> subLinks = commonLibrary.isExistList(item, UIMAP_Home.lnkLinks, 10);
				for (WebElement li : subLinks) {
					if (li.getText().toLowerCase().contains(subtopic.toLowerCase())) {
						commonLibrary.clickLinkWithWebElementWithWait(li, subtopic);
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
	// # Function Description : Function to click the Change topic link
	// # Function Name : clickChangeTopic     
	// # Author : Vidhya
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public MedicalResearch clickChangeTopic() {

		WebElement changeTopic = commonLibrary.isExist(UIMAP_MedmalNavigator.changeTopic, 10);
		if (changeTopic != null)
			commonLibrary.clickLinkWithWebElement(changeTopic, "Change Topic Link");
		WebElement Results = commonLibrary.isExist(UIMAP_MedmalNavigator.medicalResearchResults, 10);
		if (Results != null)
			report.updateTestLog("Verify medical topic list is dispalyed", "medical topic list is dispalyed", Status.PASS);
		else
			report.updateTestLog("Verify medical topic list is dispalyed", "medical topic list is not dispalyed", Status.FAIL);

		return new MedicalResearch(scriptHelper);
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
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(btnMore, "More");
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// }
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
		// btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// }
		//
		// if (lnkSignOut != null)
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// } else
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
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search within Medical Research page.
	// # Function Name : searchWithin     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedicalResearch searchWithin(String searchTerm) {
		WebElement searchSection = commonLibrary.isExistNegative(UIMAP_MedicalResearch.searchSection, 10);
		WebElement searchBox = commonLibrary.isExistNegative(searchSection, UIMAP_MedicalResearch.searchBox, 10);
		commonLibrary.setDataInTextBox(searchBox, searchTerm, "Search Within");

		WebElement searchButton = commonLibrary.isExistNegative(searchSection, UIMAP_MedicalResearch.searchButton, 10);
		commonLibrary.clickButtonParentWithWait(searchButton, "Search Within");
		WebElement filtersUsed = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 5);
		int count = 0;
		try {
			do {
				commonLibrary.sleep(50000);
				count++;
				filtersUsed = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 5);
			} while (filtersUsed == null && count < 20);
		} catch (Exception e) {
			System.out.println(e.toString());
			commonLibrary.sleep(100000);
		}
		return new MedicalResearch(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click View Medical References link.
	// # Function Name : clickViewMedicalReferences     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search clickViewMedicalReferences() {
		WebElement toolbarCont = commonLibrary.isExistNegative(UIMAP_MedicalResearch.toolbarCont, 10);
		WebElement viewMedRef = commonLibrary.isExistNegative(toolbarCont, UIMAP_MedicalResearch.viewMedRef, 10);
		String s = viewMedRef.getText();
		commonLibrary.clickButtonParentWithWait(viewMedRef, viewMedRef.getText());
		commonLibrary.sleep(100000);
		return new Search(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click verify Medical Research page
	// is displayed.
	// # Function Name : verifyPage     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedicalResearch verifyPage() {
		WebElement medResearchContainer = commonLibrary.isExistNegative(UIMAP_MedicalResearch.medResearchContainer, 10);
		WebElement header = commonLibrary.isExistNegative(medResearchContainer, UIMAP_MedicalResearch.header, 10);
		if (header.getText().toLowerCase().contains("medical topics"))
			report.updateTestLog("Verify Medical Topics Page is displayed.", "Medical Topics Page is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Medical Topics Page is displayed.", "Medical Topics Page is not displayed.", Status.FAIL);
		return new MedicalResearch(scriptHelper);
	}

}
