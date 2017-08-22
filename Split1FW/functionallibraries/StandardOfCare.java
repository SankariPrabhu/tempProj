package functionallibraries;

import java.util.List;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import supportlibraries.*;
import UIMAP.*;

import com.cognizant.framework.Status;

public class StandardOfCare extends ReusableLibrary {
	private GeneralFunctions generalfunctions = new GeneralFunctions(scriptHelper);

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public StandardOfCare(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalstandardofcare"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmalstandardofcare") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmalstandardofcare")) {
			throw new IllegalStateException("Medmal Standard of Care page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a value from the radio button
	// and submit the answer in SOC page(E.g. Yes/NO/Answer Unknown)
	// # Function Name : selectRadioAndSubmitAnswser
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public StandardOfCare selectRadioAndSubmitAnswser(String option) {
		boolean flag = false;
		WebElement currQues = commonLibrary.isExistNegative(UIMAP_StandardOfCare.questionSection, 10);
		List<WebElement> choices = commonLibrary.isExistList(currQues, UIMAP_StandardOfCare.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li, UIMAP_StandardOfCare.answerInput, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option, "radio button for " + option + " is not present", Status.FAIL);
		else {
			currQues = commonLibrary.isExistNegative(UIMAP_StandardOfCare.questionSection, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues, UIMAP_StandardOfCare.submitAnswer, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer, "Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.", "Button Submit answer is not present", Status.FAIL);
		}

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a value from the check box
	// and submit the answer in SOC page(E.g. Yes/NO/Answer Unknown)
	// # Function Name : selectCheckboxAndSubmitAnswser
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public StandardOfCare selectCheckboxAndSubmitAnswser(String option) {
		boolean flag = false;

		WebElement currQues = commonLibrary.isExistNegative(UIMAP_StandardOfCare.questionSection, 10);
		List<WebElement> choices = commonLibrary.isExistList(currQues, UIMAP_StandardOfCare.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement check = commonLibrary.isExistNegative(li, UIMAP_StandardOfCare.answerInput, 10);
				commonLibrary.setCheckBox(check, true);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select check box  for " + option, "check box  for " + option + " is not present", Status.FAIL);
		else {
			currQues = commonLibrary.isExistNegative(UIMAP_StandardOfCare.questionSection, 10);
			WebElement submitAnswer = commonLibrary.isExist(currQues, UIMAP_StandardOfCare.submitAnswer, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer, "Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.", "Button Submit answer is not present", Status.FAIL);
		}

		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify issue indicator section in
	// the SOC page
	// # Function Name : verifySocContentAnalysis
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public StandardOfCare verifySocContentAnalysis() {

		WebElement socContent = commonLibrary.isExist(UIMAP_StandardOfCare.socContent, 10);
		WebElement analysis = commonLibrary.isExist(socContent, UIMAP_StandardOfCare.analysisContent, 10);
		List<WebElement> issueIndicatorName = commonLibrary.isExistList(analysis, UIMAP_StandardOfCare.issueIndicator, 10);
		if (issueIndicatorName != null) {
			report.updateTestLog("Verify the Question Answer section is displayed along with issue indicators.", "Question Answer is displayed along with issue indicators at the right pane", Status.PASS);
		} else
			report.updateTestLog("Verify the Question Answer section is displayed along with issue indicators.", "Question Answer is not displayed along with issue indicators at the right pane", Status.FAIL);

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify issue indicator section in
	// the SOC page
	// # Function Name : verifySocContentAnalysis
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public StandardOfCare selectShowOnlyIssueAndVerifyIssueIndicator(String issueType) {
		Boolean blnFlag = false;
		WebElement socContent = commonLibrary.isExistNegative(UIMAP_StandardOfCare.socContent, 10);
		WebElement showOnly = commonLibrary.isExist(socContent, UIMAP_StandardOfCare.showOnlyIssue, 10);
		if (showOnly != null) {

			commonLibrary.setCheckBox(showOnly, true);
			List<WebElement> issueIndicator = commonLibrary.isExistList(socContent, UIMAP_StandardOfCare.sectionName, 10);
			if (issueIndicator != null) {
				for (WebElement option : issueIndicator) {

					WebElement nonIssue = commonLibrary.isExistNegative(option, UIMAP_StandardOfCare.nonIssueType, 10);
					if (nonIssue != null) {
						blnFlag = true;
						break;
					}
				}
			}
		}

		if (blnFlag) {
			report.updateTestLog("Verify the Question Answer section is displayed along with issue indicators.", "Question Answer flagged with Red issue indicator is displayed", Status.PASS);
		} else
			report.updateTestLog("Verify the Question Answer section is displayed along with issue indicators.", "Question Answer is not displayed along with issue indicators at the right pane", Status.FAIL);

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify issue indicator section in
	// the SOC page
	// # Function Name : verifySocContentAnalysis
	// # Author : Gokul
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public StandardOfCare clickShowHideDetails(String answerValue, String actiondetails, String verifyedetails) {
		Boolean blnFlag = false;
		WebElement showDetailsLink = null;
		WebElement socContent = commonLibrary.isExist(UIMAP_StandardOfCare.socContent, 10);
		WebElement analysis = commonLibrary.isExist(socContent, UIMAP_StandardOfCare.analysisContent, 10);
		List<WebElement> issueIndicatorName = commonLibrary.isExistList(analysis, UIMAP_StandardOfCare.socShowDetails, 10);
		for (WebElement option : issueIndicatorName) {
			showDetailsLink = commonLibrary.isExist(option, UIMAP_StandardOfCare.buttonInput, 20);
			WebElement socAnswer = commonLibrary.isExist(option, UIMAP_StandardOfCare.socAnswer, 20);
			if (socAnswer.getText().contains(answerValue) && showDetailsLink != null && showDetailsLink.getText().contains(actiondetails)) {
				commonLibrary.clickButtonParentWithWait(showDetailsLink, showDetailsLink.getText());
				showDetailsLink = commonLibrary.isExist(option, UIMAP_StandardOfCare.buttonInput, 20);
				if (showDetailsLink != null && showDetailsLink.getText().contains(verifyedetails)) {
					blnFlag = true;
					break;
				}
			}
		}
		if (blnFlag) {
			report.updateTestLog("Click on " + actiondetails + " link ", "" + actiondetails + " is clicked and  " + verifyedetails + " is displayed", Status.PASS);
		} else
			report.updateTestLog("Click on " + actiondetails + " link.", "" + actiondetails + " is not clicked and  " + verifyedetails + " is not displayed", Status.FAIL);

		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// the SOC page
	// # Function Name : logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalfunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the SOC POD is dispalyed
	// # Function Name : verifySOCPage
	// #Return Page: StandardOfCare
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public StandardOfCare verifySOCPage() {

		WebElement standardOfCare = commonLibrary.isExist(UIMAP_StandardOfCare.standardOfCare, 10);
		WebElement socHeader = commonLibrary.isExist(standardOfCare, UIMAP_StandardOfCare.socHeader, 10);
		if (socHeader.getText().contains("Standard of Care")) {
			report.updateTestLog("verify SOC Page Displayed", "SOC Page is Displayed", Status.PASS);
		} else {
			report.updateTestLog("verify SOC Page Displayed", "SOC Page is not Displayed", Status.FAIL);
		}
		return new StandardOfCare(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify question displayed in SOC Pod
	// # Function Name : verifySocPodQuesAnswer     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public StandardOfCare verifySocPodQuesAnswer(String question) {

		WebElement currQuestion = commonLibrary.isExist(UIMAP_StandardOfCare.questionSection, 10);

		if (currQuestion.isDisplayed() && currQuestion.getText().toLowerCase().contains(question.toLowerCase())) {
			report.updateTestLog("Verify question is displayed", "Question is displayed as " + "'" + question + "'", Status.PASS);
		} else {
			report.updateTestLog("Verify question is displayed", "Question is not displayed as " + "'" + question + "'", Status.FAIL);
		}

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the Legal Analyis Pod in
	// medmal
	// dashboard page
	// # Function Name : navigateToLegalAnalysis
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LegalAnalysis navigateToLegalAnalysis(String dashboardTopic) {
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

		return new LegalAnalysis(scriptHelper);
	}

}