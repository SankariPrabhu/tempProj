package functionallibraries;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_CaseValueAssessment;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_MedmalDashboard;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_ResearchMap;

import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import com.cognizant.framework.FrameworkException;

public class CaseValueAssessment extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(
			scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private PageCheck pageCheck = new PageCheck(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	Properties localizeProperties = Settings.getInstance();

	public CaseValueAssessment(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalcasevalueassessment"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmalcasevalueassessment") && counter < 40);
		if (!driver.getCurrentUrl().contains("medmalcasevalueassessment")) {
			throw new IllegalStateException(
					"Medmal Case Value Assessment page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Jurisdiction
	// # Function Name : selectJurisdiction     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CaseValueAssessment selectJurisdiction(String option) {
		boolean flag = false;
		WebElement leftContent = commonLibrary.isExistNegative(
				UIMAP_CaseValueAssessment.leftContent, 10);
		WebElement currQues = commonLibrary.isExistNegative(leftContent,
				UIMAP_CaseValueAssessment.currQues, 10);
		WebElement uList = commonLibrary.isExist(currQues,
				UIMAP_CaseValueAssessment.uList, 10);
		List<WebElement> choices = commonLibrary.isExistList(uList,
				UIMAP_CaseValueAssessment.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li,
						UIMAP_CaseValueAssessment.input, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option,
					"radio button for " + option + " is not present",
					Status.FAIL);
		else {
			leftContent = commonLibrary.isExistNegative(
					UIMAP_CaseValueAssessment.leftContent, 10);
			currQues = commonLibrary.isExistNegative(leftContent,
					UIMAP_CaseValueAssessment.currQues, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues,
					UIMAP_CaseValueAssessment.submitAnswer, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer,
						"Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.",
						"Button Submit answer is not present", Status.FAIL);
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
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to submit answer for given question
	// # Function Name : selectAnswer     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment selectAnswer(String option) {
		boolean flag = false;
		WebElement leftContent = commonLibrary.isExistNegative(
				UIMAP_CaseValueAssessment.leftContent, 10);
		WebElement currQues = commonLibrary.isExistNegative(leftContent,
				UIMAP_CaseValueAssessment.currQues, 10);
		WebElement uList = commonLibrary.isExist(currQues,
				UIMAP_CaseValueAssessment.uList, 10);
		List<WebElement> choices = commonLibrary.isExistList(uList,
				UIMAP_CaseValueAssessment.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li,
						UIMAP_CaseValueAssessment.input, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option,
					"radio button for " + option + " is not present",
					Status.FAIL);
		else {
			leftContent = commonLibrary.isExistNegative(
					UIMAP_CaseValueAssessment.leftContent, 10);
			currQues = commonLibrary.isExistNegative(leftContent,
					UIMAP_CaseValueAssessment.currQues, 10);
			WebElement submitAnswer = commonLibrary.isExistNegative(currQues,
					UIMAP_CaseValueAssessment.submitAnswer1, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer,
						"Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.",
						"Button Submit answer is not present", Status.FAIL);
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
		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to previous answer
	// # Function Name : changePrevAnswer     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CaseValueAssessment changePrevAnswer(String prevQues,
			String prevAns, String option) {
		boolean flag = false;
		WebElement leftContent = commonLibrary.isExistNegative(
				UIMAP_CaseValueAssessment.leftContent, 10);
		WebElement upcomingQsec = commonLibrary.isExistNegative(leftContent,
				UIMAP_CaseValueAssessment.upcomingQsec, 10);
		WebElement changePrevAnswer = commonLibrary.isExistNegative(
				upcomingQsec, UIMAP_CaseValueAssessment.changePrevAnswer, 10);
		commonLibrary.clickButtonParentWithWait(changePrevAnswer,
				"Change Previous Answer");

		WebElement popUpPrev = commonLibrary.isExistNegative(leftContent,
				UIMAP_CaseValueAssessment.popUpPrev, 10);
		WebElement changePrevAnsCont = commonLibrary.isExist(popUpPrev,
				UIMAP_CaseValueAssessment.changePrevAnsCont, 10);

		if (changePrevAnsCont.isDisplayed()
				&& changePrevAnsCont.getText().toLowerCase()
						.contains(prevQues.toLowerCase())) {
			report.updateTestLog(
					"Verify previous question is displayed in Change Previous Answer pop up.",
					"Previous question is displayed in Change Previous Answer pop up.",
					Status.PASS);
		} else
			report.updateTestLog(
					"Verify previous question is displayed in Change Previous Answer pop up.",
					"Previous question is not displayed in Change Previous Answer pop up.",
					Status.FAIL);

		if (changePrevAnsCont.isDisplayed()
				&& changePrevAnsCont.getText().toLowerCase()
						.contains(prevAns.toLowerCase())) {
			report.updateTestLog(
					"Verify previous answer is displayed in Change Previous Answer pop up.",
					"Previous answer is displayed in Change Previous Answer pop up.",
					Status.PASS);
		} else
			report.updateTestLog(
					"Verify previous answer is displayed in Change Previous Answer pop up.",
					"Previous answer is not displayed in Change Previous Answer pop up.",
					Status.FAIL);

		WebElement changeAnswer = commonLibrary.isExistNegative(
				changePrevAnsCont, UIMAP_CaseValueAssessment.changeAnswer, 10);
		commonLibrary.clickButtonParentWithWait(changeAnswer, "Change Answer");

		changePrevAnsCont = commonLibrary.isExist(popUpPrev,
				UIMAP_CaseValueAssessment.changePrevAnsCont, 10);
		WebElement ansPrevQues = commonLibrary.isExistNegative(
				changePrevAnsCont, UIMAP_CaseValueAssessment.ansPrevQues, 10);

		WebElement uList = commonLibrary.isExist(ansPrevQues,
				UIMAP_CaseValueAssessment.uList, 10);
		List<WebElement> choices = commonLibrary.isExistList(uList,
				UIMAP_CaseValueAssessment.listItem, 10);
		for (WebElement li : choices) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				WebElement radio = commonLibrary.isExistNegative(li,
						UIMAP_CaseValueAssessment.input, 10);
				commonLibrary.setRadioButton(radio, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select radio button for " + option,
					"radio button for " + option + " is not present",
					Status.FAIL);
		else {

			WebElement submitAnswer = commonLibrary
					.isExistNegative(ansPrevQues,
							UIMAP_CaseValueAssessment.submitPrevAnswer, 10);
			if (submitAnswer != null)
				commonLibrary.clickButtonParentWithWait(submitAnswer,
						"Submit Answer");
			else
				report.updateTestLog("Click button Submit answer.",
						"Button Submit answer is not present", Status.FAIL);
			pageCheck.ajaxElementCheck(driver,
					properties.getProperty("xSpinner"));
			popUpPrev = commonLibrary.isExistNegative(leftContent,
					UIMAP_CaseValueAssessment.popUpPrev, 10);
			changePrevAnsCont = commonLibrary.isExist(popUpPrev,
					UIMAP_CaseValueAssessment.changePrevAnsCont, 10);
			if (!changePrevAnsCont.isDisplayed())
				report.updateTestLog(
						"Verify Change Previous Answer content is no longer displayed.",
						"Change Previous Answer content is no longer displayed.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify Change Previous Answer content is no longer displayed.",
						"Change Previous Answer content is still displayed.",
						Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		}

		WebElement lnkSignOut = commonLibrary.isExist(
				By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut != null)
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
						"Sign Out");

		WebElement btnIdLogin = commonLibrary.isExistNegative(
				UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null
				&& driver.getCurrentUrl().toLowerCase()
						.contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is NOT displayed",
					Status.WARNING);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : navigateToViewAllHistory
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public History navigateToViewAllHistory() {
		generalFunctions.navigateToHistoryFooterLink("View All History");
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : navigateToViewAllHistory
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String docTitle) {
		boolean flag = false;
		WebElement annotationList = commonLibrary.isExistNegative(
				UIMAP_CaseValueAssessment.annotationList, 10);
		List<WebElement> annotations = commonLibrary.isExistList(
				annotationList, UIMAP_CaseValueAssessment.listItem, 10);
		for (WebElement item : annotations) {
			if (item.getText().toLowerCase().contains(docTitle.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(item,
						UIMAP_CaseValueAssessment.button, 10);
				commonLibrary.clickButtonParentWithWait(button, docTitle);
				flag = true;
				break;
			}
		}
		if (!flag) {
			report.updateTestLog("Click on citation: " + docTitle, "citation: "
					+ docTitle + " is NOT displayed", Status.FAIL);
		} else {
			WebElement dialogCont = commonLibrary.isExistNegative(
					UIMAP_CaseValueAssessment.dialogCont, 10);
			if (dialogCont != null) {
				WebElement closeWithoutSave = commonLibrary.isExistNegative(
						dialogCont, UIMAP_CaseValueAssessment.closeWithoutSave,
						10);
				if (closeWithoutSave != null) {
					commonLibrary.clickButtonParentWithWait(closeWithoutSave,
							"Continue without saving");
					commonLibrary.sleep(800000);
				} else
					report.updateTestLog(
							"Click on button: Close Without Saving",
							"Close Without Saving button is NOT displayed",
							Status.FAIL);
			} else
				report.updateTestLog("Click on button: Close Without Saving ",
						"Dialog Content is NOT displayed", Status.FAIL);
		}
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the SOC POD is dispalyed
	// # Function Name : verifyQASection
	// #Return Page: CaseValueAssessment
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyQASection() {
		WebElement caseValueAssessment = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.caseValueAssessment, 10);
		WebElement cvaHeader = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.cvaHeader, 10);
		WebElement cvaQABox = commonLibrary.isExist(caseValueAssessment,
				UIMAP_CaseValueAssessment.cvaQABox, 10);
		WebElement qaBOXHeader = commonLibrary.isExist(cvaQABox,
				UIMAP_CaseValueAssessment.qaBOXHeader, 10);
		if (cvaHeader != null) {
			if (qaBOXHeader.getText().contains("Evaluate Value of My Case")) {
				report.updateTestLog("Verify cvaDQABox Displayed",
						"CVA Q&A section pod is Displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify CVA DQABox Displayed",
						"CVA Q&A section pod is not Displayed", Status.FAIL);
			}
		} else
			report.updateTestLog("Verify CVA Header Displayed",
					"CVA Header is not Displayed", Status.FAIL);
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the presence of Verdict Graph
	// in CVA page
	// # Function Name : verifyVerdictGraph
	// #Return Page: CaseValueAssessment
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyVerdictGraph() {

		WebElement verdictGraph = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.verdictGraph, 10);
		if (verdictGraph != null)
			report.updateTestLog("Verify CVA Verdict Graph Displayed",
					"CVA Verdict Graph is Displayed", Status.PASS);
		else
			report.updateTestLog("Verify CVA Verdict Graph Displayed",
					"CVA Verdict Graph is not Displayed 302194", Status.WARNING);
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the presence of Verdict List
	// in CVA page
	// # Function Name : verifyVerdictList
	// #Return Page: CaseValueAssessment
	// # Author : Logu
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyVerdictList() {

		WebElement verdictList = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.annotationList, 10);
		if (verdictList != null)
			report.updateTestLog("Verify CVA Verdict List Displayed",
					"CVA Verdict List is Displayed", Status.PASS);
		else
			report.updateTestLog("Verify CVA Verdict List Displayed",
					"CVA Verdict List is not Displayed 302194", Status.WARNING);
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Logu
	// # Date Created : Aug'2015
	// #*****************************************************************************************************************************

	public CaseValueAssessment clickProductLogo() {
		generalFunctions.clickProductLogo();
		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Logu
	// # Date Created : Aug'2015
	// #*****************************************************************************************************************************

	public MedmalNavigator clickContinuewithoutSaving() {
		WebElement continueWithOutSavingBtn = commonLibrary
				.isExist(UIMAP_CaseValueAssessment.continueWithoutSaving);
		if (continueWithOutSavingBtn != null) {
			commonLibrary.clickButtonParentWithWait(continueWithOutSavingBtn,
					"Continue without saving");
			commonLibrary.sleep(15000);
		} else
			report.updateTestLog("Verify Continue Without Saving button ",
					"Continue Without Saving button is not available",
					Status.FAIL);

		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify question displayed in CVA pod
	// # Function Name : verifyCvPodQuesAnswer     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public CaseValueAssessment verifyCvPodQuesAnswer(String question) {

		WebElement currQuestion = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.currQues, 10);

		if (currQuestion.isDisplayed()
				&& currQuestion.getText().toLowerCase()
						.contains(question.toLowerCase())) {
			report.updateTestLog("Verify question is displayed",
					"Question is displayed as " + "'" + question + "'",
					Status.PASS);
		} else {
			report.updateTestLog("Verify question is displayed",
					"Question is not displayed as " + "'" + question + "'",
					Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to SOC Pod in medmal
	// dashboard page
	// # Function Name : navigateToSOCPod
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public StandardOfCare navigateToSOCPod(String dashboardTopic) {
		Boolean blnFlag = false;
		WebElement topNavigator = commonLibrary
				.isExist(UIMAP_MedmalDashboard.dashboardName);
		List<WebElement> dashboardSubTopic = commonLibrary.isExistList(
				topNavigator, UIMAP_MedmalDashboard.listItem, 20);

		for (WebElement li : dashboardSubTopic) {

			if (li.getText().toLowerCase()
					.contains(dashboardTopic.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(li,
						UIMAP_MedmalDashboard.button, 10);
				commonLibrary.clickButtonParentWithWait(button, dashboardTopic);

				WebElement MedRefHeader = commonLibrary
						.isExist(UIMAP_MedmalDashboard.medSOCHeader);
				WebElement MedRefSection = commonLibrary
						.isExist(UIMAP_MedmalDashboard.MedRefSection);

				int i = 0;
				do {
					MedRefHeader = commonLibrary
							.isExist(UIMAP_MedmalDashboard.medSOCHeader);
					MedRefSection = commonLibrary
							.isExist(UIMAP_MedmalDashboard.MedRefSection);
					i++;
					commonLibrary.sleep(10000);
				} while (MedRefHeader == null && i < 50
						&& MedRefSection == null);

				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Selected the Topic link " + dashboardTopic
					+ " in the Medmal Dashboard page", "The Topic link "
					+ dashboardTopic
					+ " is selected in the Medmal Dashboard page", Status.PASS);
		} else {
			report.updateTestLog("Selected the Topic link " + dashboardTopic
					+ " in the Medmal Dashboard page", "The Topic link "
					+ dashboardTopic
					+ " is not selected in the Medmal Dashboard page",
					Status.FAIL);
		}

		return new StandardOfCare(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the client id
	// # Function Name : navigateToClientLinkCaseValueAssessment
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public CaseValueAssessment navigateToClientLinkCaseValueAssessment(
			String strLinkName) {

		generalFunctions.navigateToClientLink(strLinkName);
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify medmal dialog with buttons
	// # Function Name : verifyMedmalDialog
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyMedmalDialog() {
		generalFunctions.verifyMedmalDialog();

		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function verify dialog content
	// saving
	// # Function Name : verifyMedmalDialogContent
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyMedmalDialogContent(String button) {
		WebElement dialog = commonLibrary.isExistNegative(
				UIMAP_CaseValueAssessment.dialog, 10);
		WebElement dialog_content = commonLibrary.isExistNegative(dialog,
				UIMAP_CaseValueAssessment.dialogCont, 10);
		boolean blnFlag = false;
		List<WebElement> buttons = commonLibrary.isExistList(dialog_content,
				UIMAP_Document.listItems, 10);
		for (WebElement buttonText : buttons) {
			if (buttonText.getText().toLowerCase()
					.contains(button.toLowerCase())) {
				report.updateTestLog(
						"Verify " + button + " button is present.", button
								+ " button is present.", Status.PASS);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + button + " button is present.",
					button + " button is not present.", Status.FAIL);
		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on button Continue without
	// saving
	// # Function Name : clickContinuewithoutSavingClientID
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************

	public ClientID clickContinuewithoutSavingClientID() {
		WebElement continueWithOutSavingBtn = commonLibrary
				.isExist(UIMAP_CaseValueAssessment.continueWithoutSaving);
		if (continueWithOutSavingBtn != null) {
			commonLibrary.clickButtonParentWithWait(continueWithOutSavingBtn,
					"Continue without saving");
			commonLibrary.sleep(15000);
		} else
			report.updateTestLog("Verify Continue Without Saving button ",
					"Continue Without Saving button is not available",
					Status.FAIL);
		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to verify Selected jurisdiction in
	// CVA POD
	// # Function Name : verifySelectJuris
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifySelectJuris(String jurisdiction) {
		WebElement selectJuris = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.seljuris, 20);
		if (selectJuris != null) {
			if (selectJuris.getText().contains(jurisdiction)) {
				report.updateTestLog("Verify Selected jurisdiction as "
						+ jurisdiction + " ",
						"Select jurisdiction displayed as " + jurisdiction,
						Status.PASS);
			} else {
				report.updateTestLog("Verify Selected jurisdiction as "
						+ jurisdiction + " ",
						"Select jurisdiction not displayed as " + jurisdiction,
						Status.FAIL);
			}
		}
		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is used to verify the CVA graph
	// # Function Name : verifyCvaGraph
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyCvaGraph() {
		WebElement cvaGraph = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.cvaGraph, 20);
		if (cvaGraph != null) {
			report.updateTestLog("Verify CVA graph has been displayed",
					"CVA graph has been displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify CVA graph has been displayed",
					"CVA graph Not displayed", Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is used to select View Data For
	// jurisdiction
	// # Function Name : selectViewjuris
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment selectViewjuris(String viewJuris) {
		WebElement viewjurisdiction = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.viewJuris, 20);

		if (viewjurisdiction != null) {
			commonLibrary.selectFromListOption(viewjurisdiction, viewJuris);
			// report.updateTestLog("Verify" +viewJuris+
			// "has been selected in View Data For jurisdiction", viewJuris+
			// "has been selected in View Data For jurisdiction",Status.PASS);
		} else {
			report.updateTestLog("Verify" + viewJuris
					+ "has been selected in View Data For jurisdiction",
					viewJuris + "not selected in View Data For jurisdiction",
					Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add the folder from medmal page
	// order
	// # Function Name : addToFolder
	// # Author : Sriram
	// # Date Created :Nov 13 2015
	// #*****************************************************************************************************************************

	public CaseValueAssessment addToFolderFromMN(String FolderName) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<ADD TO FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.addFolder, 1000);
			if (addtoFolder != null)

				commonLibrary.clickButtonParentWithWait(addtoFolder,
						"Add To Folder");

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement chooseFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.chooseFolder, 1000);
			if (chooseFolder != null)
				commonLibrary.clickButtonParentWithWait(chooseFolder,
						"Choose Folder");

			/*
			 * try { String loadProp = properties.getProperty("xSpinner"); int
			 * count = 0; WebElement loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); do {
			 * commonLibrary.sleep(10000); loader =
			 * commonLibrary.isExistNegative(By.xpath(loadProp), 5); count++; }
			 * while (loader != null && count < 15); } catch (Exception e) {
			 * System.out.println(e.toString()); }
			 */

			// CLICK ON <<<CREATE NEW FOLDER>>>

			WebElement createNewFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.createNewFolder, 1000);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder,
						"Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(
					UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(
						UIMAP_Document.txtFolderName, FolderName);
				commonLibrary.sleep(30000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(
					UIMAP_ResearchMap.createFolder, 1000);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(
						By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(10000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp),
							5);
					count++;
				} while (loader != null && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(
					UIMAP_ResearchMap.saveworkFolder, 1000);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "save");
				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

			try {
				String loadProp = properties.getProperty("xSpinner");
				int count = 0;
				WebElement loader = commonLibrary.isExistNegative(
						By.xpath(loadProp), 5);
				do {
					commonLibrary.sleep(10000);
					loader = commonLibrary.isExistNegative(By.xpath(loadProp),
							5);
					count++;
				} while (loader != null && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			commonLibrary.sleep(8000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new CaseValueAssessment(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Document link in CVA .
	// # Function Name : clickDocumentInCVA  
	// # Author : Sriram
	// # Date Created : Nov 13 2015
	// #*****************************************************************************************************************************

	public Document clickDocumentInCVA(String strDocTitle1) {
		WebElement DocumentInCVA = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.DoclinkCVA, 20);

		if (DocumentInCVA != null
				&& DocumentInCVA.getText().contains(strDocTitle1))
			commonLibrary.clickButtonParentWithWait(DocumentInCVA,
					"Click on Document in CVA");
		else
			report.updateTestLog("Click on Document" + strDocTitle1,
					strDocTitle1 + "is not available", Status.FAIL);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is used to verify View Data For
	// jurisdiction
	// # Function Name : verifyViewjuris
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public CaseValueAssessment verifyViewjuris(String viewJuris) {
		WebElement viewjurisdiction = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.viewJuris, 20);
		List<WebElement> jurisOption = commonLibrary.isExistList(
				viewjurisdiction, UIMAP_CaseValueAssessment.option, 20);
		WebElement selJuris = commonLibrary.isExist(viewjurisdiction,
				UIMAP_CaseValueAssessment.optionsel, 20);
		commonLibrary.selectIsSelected(viewjurisdiction, viewJuris);
		if (viewjurisdiction != null) {
			for (int i = 0; i < jurisOption.size(); i++) {
				if (jurisOption.get(i).getText().contains(viewJuris)) {
					if (selJuris != null
							&& commonLibrary.selectIsSelected(viewjurisdiction,
									viewJuris)) {

						report.updateTestLog(
								"Verify"
										+ viewJuris
										+ " has been displayed in View Data For jurisdiction",
								viewJuris
										+ " is displayed in View Data For jurisdiction",
								Status.PASS);
						break;

					} else {
						report.updateTestLog(
								"Verify"
										+ viewJuris
										+ " has been displayed in View Data For jurisdiction",
								viewJuris
										+ " not displayed in View Data For jurisdiction",
								Status.FAIL);
					}

				}
			}

		} else {
			report.updateTestLog("Verify" + viewJuris
					+ " has been selected in View Data For jurisdiction",
					viewJuris + " not selected in View Data For jurisdiction",
					Status.FAIL);
		}

		return new CaseValueAssessment(scriptHelper);
	}

}