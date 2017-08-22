package functionallibraries;

//import java.util.List;
//
//import org.openqa.selenium.By;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.Status;
//
//import UIMAP.UIMAP_Document;
//import UIMAP.UIMAP_Home;
//import UIMAP.UIMAP_Interactivecitationworkstation;
//import UIMAP.UIMAP_SignIn;
//
//import com.cognizant.framework.ExcelDataAccess;
//import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class BuildProfile extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	// private GeneralFunctions generalFunctions= new
	// GeneralFunctions(scriptHelper);
	public BuildProfile(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lnaccess"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lnaccess") && counter < 40);

		if (!driver.getCurrentUrl().contains("lnaccess")) {
			throw new IllegalStateException(
					"Build Profile page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to handle Build Profile pages
	// # Function Name : InvokeApplication     
	// # Author : Revathi
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public BuildProfile BuildSigninProfile(String strUsername,
			String strPassword, String strSecurityQuestion,
			String strSecureAns, String strConfirmEmail) {
		// MV Build Sign In Profile page displays

		WebElement pgClassSignInProfile = commonLibrary.isExist(
				UIMAP_SignIn.pgClassSignInProfile, 40);
		if (pgClassSignInProfile != null
				&& pgClassSignInProfile.getText().contains(
						"Build Sign-in Profile")) {
			report.updateTestLog("Verify Build Sign In Profile page displayed",
					"Build Sign In Profile page displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Build Sign In Profile page displayed",
					"Build Sign In Profile page not displayed", Status.FAIL);

		}

		// Enter valid details in Build Sign In Profile page

		WebElement txtidUserId = commonLibrary.isExist(
				UIMAP_SignIn.txtidUserId, 40);
		if (txtidUserId != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidUserId,
					strUsername);

		WebElement txtidFirstPassword = commonLibrary.isExist(
				UIMAP_SignIn.txtidFirstPassword, 40);
		if (txtidFirstPassword != null)
			commonLibrary.setDataInTextBoxWithLog(
					UIMAP_SignIn.txtidFirstPassword, strPassword);

		WebElement txtidVerifyPassword = commonLibrary.isExist(
				UIMAP_SignIn.txtidVerifyPassword, 40);
		if (txtidVerifyPassword != null)
			commonLibrary.setDataInTextBoxWithLog(
					UIMAP_SignIn.txtidVerifyPassword, strPassword);

		WebElement cboidQuestion = commonLibrary.isExist(
				UIMAP_SignIn.cboidQuestion, 40);
		if (cboidQuestion != null)
			commonLibrary.selectByVisibleText(cboidQuestion,
					strSecurityQuestion);

		WebElement txtidAnswer = commonLibrary.isExist(
				UIMAP_SignIn.txtidAnswer, 40);
		if (txtidAnswer != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidAnswer,
					strSecureAns);

		WebElement txtidConfirmEmail = commonLibrary.isExist(
				UIMAP_SignIn.txtidConfirmEmail, 40);
		if (txtidConfirmEmail != null)
			commonLibrary.setDataInTextBoxWithLog(
					UIMAP_SignIn.txtidConfirmEmail, strConfirmEmail);

		// CLICK ON <<<NEXT>>>

		WebElement btnidSubmit = commonLibrary.isExist(
				UIMAP_SignIn.btnidSubmit, 40);
		if (btnidSubmit != null)
			commonLibrary.clickButtonParentWithWait(btnidSubmit, "Next");

		// "CLICK DROP DOWN ARROW FOR <<<SELECT POSITION>>> DROP DOWN EXPANDS
		// CLICK OPTION <<<ATTORNEY>>>

		WebElement cboPosition = commonLibrary.isExist(
				UIMAP_SignIn.cboPosition, 40);
		if (cboPosition != null) {
			Select select = new Select(cboPosition);
			select.selectByIndex(1);
		}
		// commonLibrary.SelectByVisibleText(cboPosition, strPosition);

		// CLICK ON <<<COMPLETE REGISTRATION>>>

		WebElement lnkTextFinish = commonLibrary.isExist(
				UIMAP_SignIn.lnkTextFinish, 40);
		if (lnkTextFinish != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(lnkTextFinish, "Finish");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkTextFinish,
						"Finish");

		WebElement lnkTextFinish1 = commonLibrary.isExistNegative(
				UIMAP_SignIn.lnkTextFinish, 10);
		if (lnkTextFinish1 != null)
			if (browsername.contains("internet"))
				commonLibrary.clickJS(lnkTextFinish, "Finish");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkTextFinish,
						"Finish");

		// MV "Welcome" pop up displays
		int i = 0;
		WebElement btnValGetStarted = null;
		do {

			btnValGetStarted = commonLibrary.isExist(
					UIMAP_SignIn.btnValGetStarted, 40);
			i++;
		} while (btnValGetStarted == null && i < 3);

		WebElement popUp = commonLibrary.isExist(
				By.cssSelector("form[class='dialog-content']"), 100);
		// WebElement pgNameWelcome =
		// commonLibrary.isExist(popUp,UIMAP_SignIn.pgNameWelcome, 100);
		if (popUp != null
				&& popUp.getText().contains("Welcome to Lexis Advance")) {
			report.updateTestLog(
					"Verify Welcome to Lexis Advance page displayed",
					"Welcome to Lexis Advance page displayed", Status.PASS);
		} else {
			report.updateTestLog(
					"Verify Welcome to Lexis Advance page displayed",
					"Welcome to Lexis Advance page not displayed", Status.FAIL);

		}
		// CLICK ON <<<GET STARTED>>>

		btnValGetStarted = commonLibrary.isExist(UIMAP_SignIn.btnValGetStarted,
				40);
		if (btnValGetStarted != null)
			commonLibrary.clickButtonParentWithWait(btnValGetStarted,
					"Get Started");

		return new BuildProfile(scriptHelper);

	}
}
