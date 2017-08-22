package functionallibraries;

import java.util.List;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_CaseValueAssessment;
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.Status;

import supportlibraries.*;

public class ClientID extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(
			scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public ClientID(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("clientid"))
				commonLibrary.sleep(5000);

		} while (!url.contains("clientid") && counter < 40);
		if (!driver.getCurrentUrl().contains("clientid")) {
			throw new IllegalStateException(
					"Client id page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the name and description of
	// the client id
	// # Function Name : verificationWithNameAndDescription     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public ClientID verificationWithNameAndDescription(String client,
			String client1, String description) {
		boolean isText = false;
		boolean isDesc = false;
		WebElement text = commonLibrary.isExist(UIMAP_ResearchMap.clientText);
		if (text != null) {
			if (text.getAttribute("value").equalsIgnoreCase(client)) {
				report.updateTestLog("Verify text box displays with text "
						+ client + "", "Text box is displayed with text "
						+ client + "", Status.PASS);
			} else {
				report.updateTestLog("Verify text box displays with text "
						+ client + "", "Text box is not displayed with text "
						+ client + "", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify text box displays with text " + client
					+ "", "Text box is not displayed with text " + client + "",
					Status.FAIL);
		}
		WebElement Desc = commonLibrary
				.isExist(UIMAP_ResearchMap.clientDescription);
		if (Desc != null) {
			if (Desc.getText().equalsIgnoreCase("")) {
				report.updateTestLog(
						"Verify Research Description field displays with empty textbox",
						"Research Description field displays with empty textbox",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verify Research Description field displays with empty textbox",
						"Research Description field is not displayed with empty textbox",
						Status.FAIL);
			}

		} else {
			report.updateTestLog(
					"Verify Research Description field displays with empty textbox",
					"Research Description field is not displayed with empty textbox",
					Status.FAIL);
		}
		WebElement btnSaveClientId = commonLibrary
				.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {
			report.updateTestLog(
					"Verify save and set client id button is displayed in edit client id popup",
					"save and set client id button is displayed in edit client id popup",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Verify save and set client id button is displayed in edit client id popup",
					"save and set client id button is not displayed in edit client id popup",
					Status.FAIL);
		}
		WebElement btncancel = commonLibrary
				.isExist(UIMAP_SearchResult.btncancel);
		if (btncancel != null) {
			report.updateTestLog(
					"Verify cancel button is displayed in edit client id popup",
					"cancel button is displayed in edit client id popup",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Verify cancel button is displayed in edit client id popup",
					"cancel button is not displayed in edit client id popup",
					Status.FAIL);
		}
		WebElement text1 = commonLibrary.isExist(UIMAP_ResearchMap.clientText);
		commonLibrary.setDataInTextBox(text1, client1, "New client id");
		WebElement textNew = commonLibrary.isExist(UIMAP_ResearchMap.fieldText);
		List<WebElement> span = commonLibrary.isExistList(textNew,
				UIMAP_ResearchMap.maxChar, 10);
		for (WebElement item : span) {
			if (item.getText().equalsIgnoreCase("0")) {
				isText = true;
				break;
			}
		}
		WebElement Desc1 = commonLibrary
				.isExist(UIMAP_ResearchMap.clientDescription);
		commonLibrary.setDataInTextBox(Desc1, description, "New Description");
		WebElement descNew = commonLibrary.isExist(UIMAP_ResearchMap.fieldDesc);
		List<WebElement> span1 = commonLibrary.isExistList(descNew,
				UIMAP_ResearchMap.maxChar, 10);
		for (WebElement item : span1) {
			if (item.getText().equalsIgnoreCase("0")) {
				isDesc = true;
				break;
			}
		}
		if (isText) {
			report.updateTestLog(
					" Enter New Client name with maximum 50 character verification into client ID field and verify User can able to enter only upto 50 characters.",
					"User is able to enter only upto 50 characters and character entered above than 50 is not displayed",
					Status.PASS);
		} else {
			report.updateTestLog(
					" Enter New Client name with maximum 50 character verification into client ID field and verify User can able to enter only upto 50 characters.",
					"User is able to enter more than 50 characters",
					Status.FAIL);
		}
		if (isDesc) {
			report.updateTestLog(
					"Verify 1000 characters only can be entered in Research Discription field.",
					"1000 characters only can be entered in Research Discription field.",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Verify 1000 characters only can be entered in Research Discription field.",
					"More than 1000 characters can be entered in Research Discription field.",
					Status.FAIL);
		}

		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save and verify the new client id
	// # Function Name : clickSaveAndVerify     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home clickSaveAndVerify(String client) {
		WebElement btnSaveClientId = commonLibrary
				.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(btnSaveClientId, "Save Client id");
			else
				commonLibrary.clickButtonParentWithWait(btnSaveClientId,
						"Save Client id");
			WebElement clientbtn = commonLibrary
					.isExist(UIMAP_ResearchMap.clientBtn);
			if (clientbtn.getText().toLowerCase().replaceAll(" ", "")
					.contains(client.toLowerCase().replaceAll(" ", ""))) {
				report.updateTestLog(
						"Click save client id and verify new client name displayes in global menu",
						"New client id is displayed in the global menu",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Click save client id and verify new client name displayes in global menu",
						"New client id is not displayed in the global menu",
						Status.FAIL);
			}
		} else {
			report.updateTestLog(
					"Click save client id and verify new client name displayes in global menu",
					"Save client id is not clicked and new client id is not displayed in the global menu",
					Status.FAIL);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Error message displayed
	// in Client popup
	// # Function Name : verifyClientErrorMessage
	// # Author : Ram Prasath
	// # Date Created : 26 Feb'15
	// #*****************************************************************************************************************************

	public Home verifyClientErrorMessage(String errMessage, String realcolor) {
		WebElement errorClientMessageText = commonLibrary
				.isExist(UIMAP_Home.errorClientMessageText);
		String colour = errorClientMessageText.getCssValue("color");
		if (errorClientMessageText.getText().toLowerCase()
				.contains(errMessage.toLowerCase())) {
			report.updateTestLog("Verify " + errMessage + " is displayed",
					errMessage + " is displayed", Status.PASS);
			if (colour.equals(realcolor)) {
				report.updateTestLog("Verify Red Color Box is displayed",
						"Red color Box is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Red Color Box is displayed",
						"Red color Box is not displayed", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify " + errMessage + " is displayed",
					errMessage + " is not displayed", Status.FAIL);
		}
		WebElement btncancel = commonLibrary
				.isExist(UIMAP_SearchResult.btncancel);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(btncancel, "Cancel");
			// commonLibrary.clickButton_Parent_WithWait_JS(btncancel,
			// "Cancel");
		} else {
			commonLibrary.clickButtonLogSmallWait(btncancel, "Cancel");
			// commonLibrary.clickButton_Parent_WithWait(btncancel, "Cancel");
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set client id
	// # Function Name : setClientID     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking setClientID(String clientId) {
		WebElement newClientId = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.newClientId, 10);
		if (newClientId != null) {
			commonLibrary.setRadioButton(newClientId, "New Client ID Radio");
		}

		WebElement clientIdValue = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ClientId, 10);
		commonLibrary.setDataInTextBox(clientIdValue, clientId, "Client ID");

		WebElement setClientId = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.setClientId, 10);
		if (browsername.toLowerCase().contains("internet"))

			commonLibrary.clickButtonParentWithWaitJS(setClientId,
					"Set Client ID");
		else
			commonLibrary.clickButtonParentWithWait(setClientId,
					"Set Client ID");

			


		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify message displayed in Client
	// popup
	// # Function Name : verifyMessageDisplayed
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ClientID verifyMessageDisplayed(String message) {
		WebElement messageContainer = commonLibrary
				.isExist(UIMAP_Home.messageContainer);
		// WebElement
		// messageHeader=commonLibrary.isExist(UIMAP_Home.messageHeader);
		// WebElement messageText=commonLibrary.isExist(UIMAP_Home.message);
		if (messageContainer != null) {
			String temp = messageContainer.getText().replace(" ", "");
			temp = temp.replaceAll("\n", "");
			if (temp.toLowerCase().contains(
					message.replace(" ", "").toLowerCase())) {
				report.updateTestLog(
						"verify message displayed in Client popup",
						"Message is displayed as '" + message
								+ "' in the client popup", Status.PASS);
			} else {
				report.updateTestLog(
						"verify message displayed in Client popup",
						"The given message is not displayed '" + message
								+ "' in the client popup", Status.FAIL);
			}
		} else {
			report.updateTestLog("verify message displayed in Client popup",
					"The given message is not displayed '" + message
							+ "' in the client popup", Status.FAIL);
		}

		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link in the message is
	// clickable
	// # Function Name : verifyLinkClickable
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ClientID verifyLinkClickable(String link) {
		WebElement messageContainer = commonLibrary
				.isExist(UIMAP_Home.messageContainer);
		WebElement linkInMessage = commonLibrary.isExist(messageContainer,
				UIMAP_Home.linkInMessage, 10);
		if (linkInMessage != null) {
			if (linkInMessage.getText().toLowerCase()
					.equals(link.toLowerCase()))
				report.updateTestLog("verify link '" + link
						+ "' in the message is clickable", "The link '" + link
						+ "' in the message is clickable", Status.PASS);
		} else {
			report.updateTestLog("verify link '" + link
					+ "' in the message is clickable", "The link '" + link
					+ "' in the message is not clickable", Status.FAIL);
		}
		return new ClientID(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in ClientId Page
	// # Function Name : setNewClientID
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Home setNewClientID(String strClientID) {
		// Click Client or Matter dropdown in Global Navigation
		// Click Set/Add Client ID or Click Set/Add Matter ID
		// NavigateToClientLink("Set/Add Client ID");

		// Select New Client ID or New Matter ID Radio button
		WebElement rdoClientId = commonLibrary
				.isExist(UIMAP_SearchResult.rdoClientId);
		if (rdoClientId != null) {

			commonLibrary.setRadioButton(UIMAP_SearchResult.rdoClientId);
		} else
			report.updateTestLog("Selecting Set New Client ID",
					"Client Option Radio button not found.", Status.FAIL);

		// ENTER A <<<>>> ID IN TEXT BOX
		WebElement txtNewClientId = commonLibrary
				.isExist(UIMAP_SearchResult.txtNewClientId);
		if (txtNewClientId != null) {
			commonLibrary.setDataInTextBox(txtNewClientId, strClientID,
					"Client id");

		} else
			report.updateTestLog("Setting New Client ID",
					"New Client ID can not be set.", Status.FAIL);
		// Click Set Client ID Button or Set Matter ID button
		WebElement btnSaveClientId = commonLibrary
				.isExist(UIMAP_SearchResult.btnSaveClientId);
		if (btnSaveClientId != null) {

			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnSaveClientId,
						"Set Client Id");
			} else {
				commonLibrary.clickButtonParentWithWait(btnSaveClientId,
						"Set Client Id");
			}

		} else
			report.updateTestLog("Clicking on Set Client ID",
					"Set Client ID can not be clicked.", Status.FAIL);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set Client ID in ClientId Page
	// # Function Name : setClientIDValue
	// # Author : Bikash
	// # Date Created : Sept'9
	// #*****************************************************************************************************************************
	public MedmalNavigator setClientIDValue(String strClientID) {
		generalFunctions.setClientIDValue(strClientID);
		return new MedmalNavigator(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify "Set/Add client ID" page
	// # Function Name : verifySetClientID
	// #Return Page: ClientID
	// # Author : Bikash
	// # Date Created : Sep'9
	// #*****************************************************************************************************************************

	public ClientID verifySetClientID(String header) {
		WebElement setAddId = commonLibrary.isExist(
				UIMAP_CaseValueAssessment.setAddClient, 10);
		if (setAddId != null) {
			if (setAddId.getText().contains(header)) {
				report.updateTestLog(
						"Verify set and add client id is Displayed",
						"Set and add client id Displayed is Displayed",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verify set and add client id not Displayed",
						"Verify set and add client id is not Displayed",
						Status.FAIL);
			}
		}
		return new ClientID(scriptHelper);
	}

}