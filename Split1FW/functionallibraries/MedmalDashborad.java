package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import supportlibraries.*;
import UIMAP.UIMAP_MedmalDashboard;
import UIMAP.UIMAP_MedmalResearch;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_Home;
import com.cognizant.framework.FrameworkException;

import com.cognizant.framework.Status;

public class MedmalDashborad extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	private GeneralFunctions generalfunctions = new GeneralFunctions(scriptHelper);
	int Mwait = Integer.parseInt(Mediumwait);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public MedmalDashborad(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmaldashboard"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmaldashboard") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmaldashboard")) {
			throw new IllegalStateException("Medmal Navigator dashbarod home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardTopic
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public StandardOfCare clickdashboardTopic(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase().contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);

				WebElement MedRefHeader = commonLibrary.isExist(UIMAP_MedmalDashboard.medSOCHeader);
				WebElement MedRefSection = commonLibrary.isExist(UIMAP_MedmalDashboard.MedRefSection);

				int i = 0;
				do {
					MedRefHeader = commonLibrary.isExist(UIMAP_MedmalDashboard.medSOCHeader);
					MedRefSection = commonLibrary.isExist(UIMAP_MedmalDashboard.MedRefSection);
					i++;
					commonLibrary.sleep(10000);
				} while (MedRefHeader == null && i < 50 && MedRefSection == null);

				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is not selected in the Medmal Dashboard page", Status.FAIL);
		}

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on CaseValueAssessmentPod
	// # Function Name : selectPod     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment selectCVA() {
		boolean flag = false;
		WebElement podsContent = commonLibrary.isExistNegative(UIMAP_MedmalDashboard.podsContent, 20);
		List<WebElement> pods = commonLibrary.isExistList(podsContent, UIMAP_MedmalDashboard.listItem, 10);
		for (WebElement li : pods) {
			if (li.getText().toLowerCase().contains("case value assessment")) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, "Case Value Assessment");
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click on Case Value Assessment pod.", "Case Value Assessment pod not present.", Status.FAIL);
		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on View All Expert Witnesses
	// # Function Name : clickViewAllExpertWitnesses     
	// # Author : Shobana J
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedmalExpertWitness clickViewAllExpertWitnesses() {
		WebElement topNavigation = commonLibrary.isExist(UIMAP_MedmalDashboard.navigationBar, 10);
		WebElement selectedTab = commonLibrary.isExist(topNavigation, UIMAP_MedmalDashboard.selectedTab, 10);
		if (!(selectedTab.getText().equalsIgnoreCase("Dashboard"))) {
			WebElement dashBoard = commonLibrary.isExist(UIMAP_MedmalDashboard.dashBoard, 10);
			commonLibrary.clickButtonParentWithWait(dashBoard, "DashBoard");
		}
		int counter = 0;
		WebElement expertWitness = commonLibrary.isExist(UIMAP_MedmalDashboard.expertWitness, 10);
		do {

			if (expertWitness == null)
				commonLibrary.sleep(5000);

			expertWitness = commonLibrary.isExist(UIMAP_MedmalDashboard.expertWitness, 10);
			counter = counter + 1;
		} while (expertWitness == null && counter < 100);

		if (expertWitness == null)
			report.updateTestLog("Click on View All Expert Witnesses link", "View All Expert Witnesses link is not present", Status.FAIL);
		else
			commonLibrary.clickButtonParentWithWait(expertWitness, "View all Expert Witnesses");

		return new MedmalExpertWitness(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select View All Results
	// # Function Name : selectViewAllResults     
	// # Author : Vidhya
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public MedicalResearch selectViewAllResults() {
		try {
			boolean flag = false;
			// WebElement podsContent =
			// commonLibrary.isExist_Negative(UIMAP_MedmalDashboard.podsContent,
			// 20);
			List<WebElement> pods = commonLibrary.isExistList(By.cssSelector("button[id='medicalResearch']"), 10);
			for (WebElement li : pods) {
				if (li.getText().contains("View all Results")) {
					// WebElement button = commonLibrary.isExist_Negative(li,
					// UIMAP_MedmalDashboard.button, 10);
					commonLibrary.clickButtonParentWithWait(li, "View all Results");
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Click on View all Results.", "View all Results not present.", Status.FAIL);
		} catch (Exception e) {

		}
		return new MedicalResearch(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardTopicResearch
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickdashboardTopicResearch(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase().contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);
				WebElement addNewParty = commonLibrary.isExist(UIMAP_MedmalResearch.addNewParty, 10);
				int i = 0;
				do {
					addNewParty = commonLibrary.isExist(UIMAP_MedmalResearch.addNewParty, 10);
					i++;
					commonLibrary.sleep(10000);
				} while (addNewParty == null && i < 50);
				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is not selected in the Medmal Dashboard page", Status.FAIL);
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardMedicalreferences
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search clickdashboardMedicalreferences(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase().contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);
				WebElement MedRefHeader = commonLibrary.isExist(UIMAP_MedmalDashboard.medRefHeader);
				if (MedRefHeader != null) {
					commonLibrary.clickButtonParentWithWait(button, dashboardTopic);
					int i = 0;
					do {

						MedRefHeader = commonLibrary.isExist(UIMAP_MedmalDashboard.medRefHeader);
						i++;
						commonLibrary.sleep(10000);
					} while (MedRefHeader != null & i < 80);

					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is not selected in the Medmal Dashboard page", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardTopic
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment clickDashboardTopicCVA(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase().contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);
				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic + " in the Medmal Dashboard page", "The Topic link " + dashboardTopic + " is not selected in the Medmal Dashboard page", Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Medmal Dashboard Page
	// # Function Name : verifyDashboard  
	// # Author : Sriram
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public MedmalDashborad verifyMedmalDashboard(String strDocTitle) {

		try {

			WebElement eltDocumentHeading = commonLibrary.isExistNegative(UIMAP_MedmalDashboard.dashboardTopic, 10);
			if (eltDocumentHeading.getText().contains(strDocTitle))
				report.updateTestLog("Verify Dashboard page is displayed", "Dashboard page is displayed", Status.PASS);
			else
				report.updateTestLog("Verify dashboard page is displayed", "Dashbaord page is not displayed", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new MedmalDashborad(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Folders Page from Medmal
	// # Function Name : navigateToFolders    
	// # Author : Sriram
	// # Date Created : Nov 2015
	// #*****************************************************************************************************************************

	public WorkFolders navigateToFolders() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);

			if (folder == null || !folder.isDisplayed()) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null)
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
				folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore, 10);
			}

			if (folder != null) {
				commonLibrary.clickLinkWithWebElementWithWait(folder, "Folders");
				commonLibrary.sleep(6000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the topic link in medmal
	// dashboard page
	// # Function Name : clickdashboardTopicCVALegalAnalysis
	// # Author : Bikash
	// # Date Created : Sep'9
	// #*****************************************************************************************************************************

	public LegalAnalysis clickdashboardTopicCVALegalAnalysis(String dashboardTopic) {
		generalfunctions.clickDashboardTopicCVA(dashboardTopic);

		return new LegalAnalysis(scriptHelper);
	}

}
