package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import supportlibraries.*;
import UIMAP.*;

import com.cognizant.framework.Status;

public class LegalAnalysis extends ReusableLibrary {
	private GeneralFunctions generalfunctions = new GeneralFunctions(scriptHelper);

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public LegalAnalysis(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalstandardofcare"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmallegalanalysis") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmallegalanalysis")) {
			throw new IllegalStateException("Medmal Legal Analysis page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a value from the radio button
	// and submit the answer in Legal Analysis page(E.g. Yes/NO/Answer Unknown)
	// # Function Name : selectRadioAndSubmitAnswserLglAnlys
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalAnalysis selectRadioAndSubmitAnswserLglAnlys(String option) {
		boolean flag = false;

		WebElement currQues = commonLibrary.isExistNegative(UIMAP_LegalAnalysis.questionSection, 10);
		List<WebElement> choices = commonLibrary.isExistList(currQues, UIMAP_LegalAnalysis.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li, UIMAP_LegalAnalysis.answerInput, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option, "radio button for " + option + " is not present", Status.FAIL);
		else {
			currQues = commonLibrary.isExistNegative(UIMAP_LegalAnalysis.questionSection, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues, UIMAP_LegalAnalysis.submitAnswer, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer, "Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.", "Button Submit answer is not present", Status.FAIL);
		}

		return new LegalAnalysis(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to CVA Pod from
	// Legal Analysis page
	// # Function Name : navigateToCva
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment navigateToCva(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary.isExist(UIMAP_LegalAnalysis.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(topNavigator, UIMAP_LegalAnalysis.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase().contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li, UIMAP_LegalAnalysis.button, 10);
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
	// # Function Description : Function to logout
	// the SOC page
	// # Function Name : logout
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalfunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to submit answer for given question
	// # Function Name : selectAnswerForLegalAnalysis     
	// # Author : Bikash
	// # Date Created : Sep'9
	// #*****************************************************************************************************************************

	public LegalAnalysis selectAnswerForLegalAnalysis(String option) {
		boolean flag = false;
		WebElement leftContent = commonLibrary.isExistNegative(UIMAP_CaseValueAssessment.mainQaBox, 10);
		WebElement currQues = commonLibrary.isExistNegative(leftContent, UIMAP_CaseValueAssessment.QaBox, 10);
		WebElement uList = commonLibrary.isExist(currQues, UIMAP_CaseValueAssessment.uList, 10);
		List<WebElement> choices = commonLibrary.isExistList(uList, UIMAP_CaseValueAssessment.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li, UIMAP_CaseValueAssessment.input, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option, "radio button for " + option + " is not present", Status.FAIL);
		else {
			leftContent = commonLibrary.isExistNegative(UIMAP_CaseValueAssessment.mainQaBox, 10);
			currQues = commonLibrary.isExistNegative(leftContent, UIMAP_CaseValueAssessment.QaBox, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues, UIMAP_CaseValueAssessment.submitAnswer2, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer, "Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.", "Button Submit answer is not present", Status.FAIL);
		}
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(500000);
		String xpath = properties.getProperty("xSpinner");
		WebElement spinner = commonLibrary.isExist(By.xpath(xpath), 20);
		int i = 0;
		do {
			commonLibrary.sleep(500000);
			i++;
			spinner = commonLibrary.isExist(By.xpath(xpath), 20);
		} while (spinner != null && i < 50);
		return new LegalAnalysis(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the client id
	// # Function Name : navigateToClientLinkLegalAnalysis
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public LegalAnalysis navigateToClientLinkLegalAnalysis(String strLinkName) {

		generalfunctions.navigateToClientLink(strLinkName);
		return new LegalAnalysis(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify medmal dialog with buttons
	// # Function Name : verifyMedmalDialogLegalAnalysis
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public LegalAnalysis verifyMedmalDialogLegalAnalysis() {
		generalfunctions.verifyMedmalDialog();
		return new LegalAnalysis(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on button Continue without
	// saving
	// # Function Name : clickContinuewithoutSavingClientID
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public ClientID clickContinuewithoutSavingClientID() {
		WebElement continueWithOutSavingBtn = commonLibrary.isExist(UIMAP_CaseValueAssessment.continueWithoutSaving);
		if (continueWithOutSavingBtn != null) {
			commonLibrary.clickButtonParentWithWait(continueWithOutSavingBtn, "Continue without saving");
			commonLibrary.sleep(15000);
		} else
			report.updateTestLog("Verify Continue Without Saving button ", "Continue Without Saving button is not available", Status.FAIL);
		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select a value from the radio button
	// and submit the answer in Legal Analysis page(E.g. Yes/NO/Answer Unknown)
	// # Function Name : selectRadioAndSubmitAnswserLglAnlys
	// # Author : Bikash
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LegalAnalysis selectRadioAndSubmitAnswserLegallAnalys(String option) {
		boolean flag = false;

		WebElement currQues = commonLibrary.isExistNegative(UIMAP_LegalAnalysis.questionSection, 10);
		List<WebElement> choices = commonLibrary.isExistList(currQues, UIMAP_LegalAnalysis.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li, UIMAP_LegalAnalysis.answerInput, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option, "radio button for " + option + " is not present", Status.FAIL);
		else {
			currQues = commonLibrary.isExistNegative(UIMAP_LegalAnalysis.questionSection, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues, UIMAP_LegalAnalysis.submitAnswerLegalAnalysis, 10);
			if (submitAnswer != null) {
				commonLibrary.clickButtonParentWithWait(submitAnswer, "Submit Answer");
				WebElement leftContent = commonLibrary.isExistNegative(UIMAP_CaseValueAssessment.mainQaBox, 10);
				int count = 0;
				do {
					count++;
					leftContent = commonLibrary.isExistNegative(UIMAP_CaseValueAssessment.mainQaBox, 10);
					if (leftContent == null)
						commonLibrary.sleep(5000);
				} while (leftContent == null && count <= 25);
			} else
				report.updateTestLog("Click button Submit answer.", "Button Submit answer is not present", Status.FAIL);
		}

		return new LegalAnalysis(scriptHelper);
	}

}