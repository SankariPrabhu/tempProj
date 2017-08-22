package functionallibraries;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SignIn;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class SignInLNAccess extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public SignInLNAccess(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lnaccess") && !url.contains("signin"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lnaccess") && !url.contains("signin") && counter < 40);

		if ((!driver.getCurrentUrl().contains("lnaccess")) || (!driver.getCurrentUrl().contains("signin"))) {
			throw new IllegalStateException("Build SignIn Profile page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues in BuildSignInProfile
	// # Function Name : enterValues_BuildSignInProfile     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research enterValuesBuildSignInProfile(String userName, String password, String secQuestion, String secAns, String eMail) {

		WebElement txtidUserId = commonLibrary.isExist(UIMAP_SignIn.txtidUserId, 40);
		if (txtidUserId != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidUserId, userName);

		WebElement txtidFirstPassword = commonLibrary.isExist(UIMAP_SignIn.txtidFirstPassword, 40);
		if (txtidFirstPassword != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidFirstPassword, password);

		WebElement txtidVerifyPassword = commonLibrary.isExist(UIMAP_SignIn.txtidVerifyPassword, 40);
		if (txtidVerifyPassword != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidVerifyPassword, password);

		WebElement cboidQuestion = commonLibrary.isExist(UIMAP_SignIn.cboidQuestion, 40);
		if (cboidQuestion != null)
			commonLibrary.selectByVisibleText(cboidQuestion, secQuestion);

		WebElement txtidAnswer = commonLibrary.isExist(UIMAP_SignIn.txtidAnswer, 40);
		if (txtidAnswer != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidAnswer, secAns);

		WebElement txtidConfirmEmail = commonLibrary.isExist(UIMAP_SignIn.txtidConfirmEmail, 40);
		if (txtidConfirmEmail != null)
			commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidConfirmEmail, eMail);

		WebElement rememberMe = commonLibrary.isExist(UIMAP_SignIn.rememberMe, 40);
		if (rememberMe != null) {
			if (!(rememberMe.isSelected())) {
				report.updateTestLog("Verify Check box for Remeber Me is unchecked by default", "Check box for Remember Me is unchecked by default", Status.PASS);
				commonLibrary.setCheckBox(rememberMe, "Remember Me");
			}

			else

				report.updateTestLog("Verify Check box for Remeber Me is unchecked by default", "Check box for Remember Me is checked by default", Status.FAIL);
		}

		WebElement btnidSubmit = commonLibrary.isExist(UIMAP_SignIn.btnidSubmit, 40);
		if (btnidSubmit != null)
			commonLibrary.clickButtonParentWithWait(btnidSubmit, "Finish");

		WebElement pgNameWelcome = commonLibrary.isExist(UIMAP_SignIn.pgNameWelcome, 40);
		if (pgNameWelcome == null) {
			Random rndNum = new Random();
			int rand = rndNum.nextInt();
			String userName1 = userName + rand;
			txtidUserId = commonLibrary.isExist(UIMAP_SignIn.txtidUserId, 40);
			if (txtidUserId != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidUserId, userName1);
			btnidSubmit = commonLibrary.isExist(UIMAP_SignIn.btnidSubmit, 40);
			if (btnidSubmit != null)
				commonLibrary.clickButtonParentWithWait(btnidSubmit, "Finish");
		}

		WebElement welcome = commonLibrary.isExist(UIMAP_Home.welcomeDialog, 40);
		pgNameWelcome = commonLibrary.isExist(welcome, UIMAP_SignIn.pgNameWelcome, 40);
		if (pgNameWelcome != null && pgNameWelcome.getText().contains("Welcome to Lexis Advance")) {
			report.updateTestLog("Verify Welcome to Lexis Advance page displayed", "Welcome to Lexis Advance page displayed", Status.PASS);
		} else {
			throw new FrameworkException("Verify Welcome to Lexis Advance page displayed", "Welcome to Lexis Advance page not displayed");

		}
		// CLICK ON <<<GET STARTED>>>
		WebElement btnValGetStarted = commonLibrary.isExist(UIMAP_SignIn.btnValGetStarted, 40);
		if (btnValGetStarted != null)
			commonLibrary.clickButtonParentWithWait(btnValGetStarted, "Get Started");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		if (CurrentProduct.getText().contains("Pacific Research")) {
			report.updateTestLog("Pacific Research landing page is displayed", "Pacific Research landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Pacific Research landing page is displayed", "Pacific Research landing page is not displayed", Status.FAIL);
		}

		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify header
	// # Function Name : verify_trainStopHeader     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignInLNAccess verifyTrainStopHeader(String activeHeader) {
		WebElement trainStopHeader = commonLibrary.isExist(By.cssSelector("ol[id*='Steps']"), 10);// UIMAP_USRfirstSignOn.trainStopHeader1
		List<WebElement> headerLabels = commonLibrary.isExistList(trainStopHeader, By.tagName("li"), 20);

		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browsername = cap.getBrowserName();

		for (WebElement item : headerLabels) {
			switch (browsername.toLowerCase()) {
			case "firefox":
				if (item.getText().contains(activeHeader)) {
					String colour = item.getCssValue("background-color");
					if (colour.equalsIgnoreCase("rgba(255, 255, 255, 1)"))
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is active", Status.PASS);
					else
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is not active", Status.FAIL);

				} else {
					String colour = item.getCssValue("background-color");
					if (colour.equalsIgnoreCase("rgba(244, 244, 245, 1)"))
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is grayed out", Status.PASS);
					else
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is not grayed out", Status.FAIL);
				}
				break;

			case "internet explorer":
				if (item.getText().contains(activeHeader)) {
					String colour = item.getCssValue("background-color");
					if (colour.equalsIgnoreCase("rgba(255, 255, 255, 1)"))
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is active", Status.PASS);
					else
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is not active", Status.FAIL);

				} else {
					String colour = item.getCssValue("background-color");
					if (colour.equalsIgnoreCase("rgba(244, 244, 245, 1)"))
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is grayed out", Status.PASS);
					else
						report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is not grayed out", Status.FAIL);
				}
				break;
			}
		}

		return new SignInLNAccess(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function page header
	// # Function Name : verifyText     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignInLNAccess verifyText(String pageText) {
		String pageTexts[] = pageText.split("#");
		WebElement pageContent = commonLibrary.isExist(UIMAP_Home.txtPageContent, 40);

		for (int i = 0; i < pageTexts.length; i++) {
			if (pageContent.getText().contains(pageTexts[i]))
				report.updateTestLog(pageTexts[i] + " is present in Build Sign In Profile page", pageTexts[i] + " is present in Build Sign In Profile page", Status.PASS);
			else
				report.updateTestLog(pageTexts[i] + " is present in Build Sign In Profile page", pageContent.getText() + " is present in Build Sign In Profile page", Status.WARNING);
		}
		return new SignInLNAccess(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues in BuildSignInProfile
	// # Function Name : enterValues_BuildSignInProfile_LSP     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LawSchool enterValuesBuildSignInProfileLSP(String userName, String password, String secQuestion, String secAns, String eMail) {
		try {
			WebElement txtidUserId = commonLibrary.isExist(UIMAP_SignIn.txtidUserId, 40);
			if (txtidUserId != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidUserId, userName);

			WebElement txtidFirstPassword = commonLibrary.isExist(UIMAP_SignIn.txtidFirstPassword, 40);
			if (txtidFirstPassword != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidFirstPassword, password);

			WebElement txtidVerifyPassword = commonLibrary.isExist(UIMAP_SignIn.txtidVerifyPassword, 40);
			if (txtidVerifyPassword != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidVerifyPassword, password);

			WebElement cboidQuestion = commonLibrary.isExist(UIMAP_SignIn.cboidQuestion, 40);
			if (cboidQuestion != null)
				commonLibrary.selectByVisibleText(cboidQuestion, secQuestion);

			WebElement txtidAnswer = commonLibrary.isExist(UIMAP_SignIn.txtidAnswer, 40);
			if (txtidAnswer != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidAnswer, secAns);

			WebElement txtIdEmail = commonLibrary.isExist(UIMAP_SignIn.txtidEmail, 40);
			if (txtIdEmail != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidEmail, eMail);

			WebElement txtidConfirmEmail = commonLibrary.isExist(UIMAP_SignIn.txtidConfirmEmail, 40);
			if (txtidConfirmEmail != null)
				commonLibrary.setDataInTextBoxWithLog(UIMAP_SignIn.txtidConfirmEmail, eMail);

			WebElement rememberMe = commonLibrary.isExist(UIMAP_SignIn.rememberMe, 40);
			if (rememberMe != null) {
				if (!(rememberMe.isSelected())) {
					report.updateTestLog("Verify Check box for Remeber Me is unchecked by default", "Check box for Remember Me is unchecked by default", Status.PASS);
					commonLibrary.setCheckBox(rememberMe, "Remember Me");
				}

				else

					report.updateTestLog("Verify Check box for Remeber Me is unchecked by default", "Check box for Remember Me is checked by default", Status.FAIL);
			}

			WebElement btnidSubmit = commonLibrary.isExist(UIMAP_SignIn.btnidSubmit, 40);
			if (btnidSubmit != null)
				commonLibrary.clickButtonParentWithWait(btnidSubmit, "Finish");

			commonLibrary.sleep(15000);

			int count = 0;
			do {
				commonLibrary.sleep(15000);
				count = count + 1;
			} while (!driver.getCurrentUrl().contains("law") && count <= 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new LawSchool(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset the password
	// # Function Name : enterValues_Reset the passowrd     
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public SignInLNAccess reSetPassword() {

		boolean blnFlag = false;

		WebElement forgotlink = commonLibrary.isExist(UIMAP_SignIn.forgotlink, 20);
		List<WebElement> allULList = commonLibrary.isExistList(forgotlink, UIMAP_SignIn.ul, 20);
		if (allULList.size() > 0) {

			List<WebElement> forgotList = commonLibrary.isExistList(allULList.get(0), UIMAP_SignIn.List1, 20);
			if (forgotList.size() > 0) {
				List<WebElement> flink = commonLibrary.isExistList(forgotList.get(3), UIMAP_SignIn.flink, 20);

				for (WebElement value : flink) {
					if (value.getText().equalsIgnoreCase("Forgot your ID or password?")) {

						commonLibrary.clickLink(value, "Forgot your ID or password?");
						blnFlag = true;
						break;
					}
				}
			}
		}

		if (blnFlag == true) {

			report.updateTestLog("Verify Forgot your ID or password? link is clicked", "Forgot your ID or password? link is clicked", Status.PASS);

		} else {

			report.updateTestLog("Verify Forgot your ID or password? link is clicked", "Verify Forgot your ID or password? link is not clicked", Status.FAIL);

		}
		return new SignInLNAccess(scriptHelper);
	}

}
