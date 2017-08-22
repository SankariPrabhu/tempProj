package functionallibraries;

import java.util.List;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import UIMAP.UIMAP_Register;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_USRfirstSignOn;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class USRFirstSignOn extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public USRFirstSignOn(ScriptHelper scriptHelper) {
		super(scriptHelper);
		if (!driver.getCurrentUrl().contains("FirstSignOn")) {
			throw new IllegalStateException("USR First Sign On page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify drop down objects present in
	// First Sign On page
	// # Function Name : verify_DropDown_FirstSignOn     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public USRFirstSignOn verifyDropDownFirstSignOn(String Locale, String TimeZone) {

		WebElement locale = commonLibrary.isExist(UIMAP_USRfirstSignOn.locale, 10);
		WebElement timeZone = commonLibrary.isExist(UIMAP_USRfirstSignOn.timeZone, 10);
		WebElement position = commonLibrary.isExist(UIMAP_USRfirstSignOn.position, 10);

		if ((locale != null) && (timeZone != null) && (position != null)) {
			report.updateTestLog("Verify the 'Locale' , 'TimeZone' and 'Position' drop downs are persent", "'Locale' , 'TimeZone' and 'Position' drop downs are persent", Status.PASS);
			Select select = new Select(locale);
			Select select1 = new Select(timeZone);

			if ((select.getFirstSelectedOption().getText().equalsIgnoreCase(Locale)) && (select1.getFirstSelectedOption().getText().equalsIgnoreCase(TimeZone)))
				report.updateTestLog("Verify '" + Locale + "' and '" + TimeZone + "' are selected in Locale and timeZone dropdowns by default", "'" + Locale + "' and '" + TimeZone + "' are selected in Locale and timeZone dropdowns by default", Status.PASS);
			else
				report.updateTestLog("Verify '" + Locale + "' and '" + TimeZone + "' are selected in Locale and timeZone dropdowns by default", "'" + Locale + "' and '" + TimeZone + "' are not selected in Locale and timeZone dropdowns by default", Status.FAIL);
		} else {
			report.updateTestLog("Verify the 'Locale' , 'TimeZone' and 'Position' drop downs are persent", "'Locale' , 'TimeZone' and 'Position' drop downs are not persent", Status.FAIL);
		}

		return new USRFirstSignOn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues in LNI first sign on
	// page
	// # Function Name : enterValues_FirstSignOn_LNI     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignInLNAccess enterValuesFirstSignOnLNI(String Locale, String TimeZone, String Position) {
		try {
			WebElement locale = commonLibrary.isExist(UIMAP_USRfirstSignOn.locale, 10);
			WebElement timeZone = commonLibrary.isExist(UIMAP_USRfirstSignOn.timeZone, 10);
			WebElement position = commonLibrary.isExist(UIMAP_USRfirstSignOn.position, 10);

			if ((locale != null) && (timeZone != null) && (position != null)) {

				if (!(Locale == "")) {

					commonLibrary.selectByVisibleText(locale, Locale);
					report.updateTestLog("Select " + Locale + "in Locale dropdown", Locale + " is selected in Locale dropdown", Status.PASS);
				}
				if (!(TimeZone == "")) {

					commonLibrary.selectByVisibleText(timeZone, TimeZone);
					report.updateTestLog("Select " + TimeZone + "in TimeZone dropdown", TimeZone + " is selected in TimeZone dropdown", Status.PASS);
				}
				if (!(Position == "")) {

					commonLibrary.selectByVisibleText(position, Position);
					report.updateTestLog("Select " + Position + "in Position dropdown", Position + " is selected in Position dropdown", Status.PASS);
				}

				WebElement submitNext = commonLibrary.isExist(UIMAP_USRfirstSignOn.submitNext, 10);
				commonLibrary.highlightElement(submitNext);
				commonLibrary.clickButtonParentWithWait(submitNext, "Submit and Go to the next step");
				commonLibrary.sleep(15000);
				pageCheck.positiveCheck(driver, " Build Sign In Profile Page", " Build Sign In Profile Page");
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new SignInLNAccess(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the header
	// # Function Name : verify_trainStopHeader     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public USRFirstSignOn verifyTrainStopHeader(String activeHeader) {
		WebElement trainStopHeader = commonLibrary.isExist(UIMAP_USRfirstSignOn.trainStopHeader, 10);
		List<WebElement> headerLabels = commonLibrary.isExistList(trainStopHeader, By.tagName("li"), 20);
		for (WebElement item : headerLabels) {
			if (item.getText().contains(activeHeader)) {
				String colour = item.getCssValue("background-color");
				if (colour.equalsIgnoreCase("rgba(255, 255, 255, 1)"))
					report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is active", Status.PASS);
				else
					report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is active", "The header label '" + item.getText() + "' is present and is not active", Status.FAIL);

			} else {
				String colour = item.getCssValue("background-color");
				if (colour.equalsIgnoreCase("rgba(233, 233, 234, 1)"))
					report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is grayed out", Status.PASS);
				else
					report.updateTestLog("Verify the header label '" + item.getText() + "' is present and is grayed out", "The header label '" + item.getText() + "' is present and is not grayed out", Status.FAIL);
			}
		}

		return new USRFirstSignOn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues inLSP student page
	// # Function Name : enterValues_LSP_Student     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignInLNAccess enterValuesLSPStudent(String loc, String timZon, String pgm, String gradTrm, String gradYr) {
		try {
			WebElement student = commonLibrary.isExist(UIMAP_USRfirstSignOn.student, 10);
			if (student != null) {
				commonLibrary.setCheckBoxSimpleClick(student, "Student");
				commonLibrary.clickButtonSubmit(UIMAP_USRfirstSignOn.student, "Student");
				WebElement locale = commonLibrary.isExist(UIMAP_USRfirstSignOn.locale, 10);
				int count = 0;
				do {
					count++;
					commonLibrary.sleep(2000);
				} while (locale == null && count < 15);
				// modifed by Divakar: Simple Click on Radio button required in
				// this case

				// WebElement locale =
				// commonLibrary.isExist(UIMAP_USRfirstSignOn.locale, 10);
				WebElement timeZone = commonLibrary.isExist(UIMAP_USRfirstSignOn.timeZone, 10);
				WebElement program = commonLibrary.isExist(UIMAP_USRfirstSignOn.program, 10);
				WebElement gradTerm = commonLibrary.isExist(UIMAP_USRfirstSignOn.gradTerm, 10);
				WebElement gradYear = commonLibrary.isExist(UIMAP_USRfirstSignOn.gradYear, 10);

				if ((locale != null) && (timeZone != null) && (program != null) && (gradTerm != null)) {
					report.updateTestLog("Verify Locale, TimeZone, Program and Graduation Term fields are present", "Verify Locale, TimeZone, Program and Graduation Term fields are present", Status.PASS);
					Select select = new Select(locale);
					Select select1 = new Select(timeZone);

					if ((select.getFirstSelectedOption().getText().equalsIgnoreCase(loc)) && (select1.getFirstSelectedOption().getText().equalsIgnoreCase(timZon)))
						report.updateTestLog("Verify '" + loc + "' and '" + timZon + "' are selected in Locale and timeZone dropdowns by default", "'" + loc + "' and '" + timZon + "' are selected in Locale and timeZone dropdowns by default", Status.PASS);
					else
						report.updateTestLog("Verify '" + loc + "' and '" + timZon + "' are selected in Locale and timeZone dropdowns by default", "'" + select.getFirstSelectedOption().getText() + "' and '" + select1.getFirstSelectedOption().getText() + "' are selected in Locale and timeZone dropdowns by default", Status.WARNING);

					if (!(pgm == "")) {

						commonLibrary.selectByVisibleText(program, pgm);
						report.updateTestLog("Select " + pgm + "in Program dropdown", pgm + " is selected in Program dropdown", Status.PASS);
					}
					if (!(gradTrm == "")) {

						commonLibrary.selectByVisibleText(gradTerm, gradTrm);
						report.updateTestLog("Select " + gradTrm + "in Graduation Term dropdown", gradTrm + " is selected in Graduation Term dropdown", Status.PASS);
					}

					if (!(gradYr == "")) {

						commonLibrary.setDataInTextBox(gradYear, gradYr, "Graduation Year");
						report.updateTestLog("Select " + gradYr + "in Graduation Year dropdown", gradYr + " is selected in Graduation Year dropdown", Status.PASS);
					}

					WebElement participateLA = commonLibrary.isExist(UIMAP_USRfirstSignOn.participateLA, 10);
					if ((participateLA != null) && (participateLA.isSelected()))
						report.updateTestLog("Participate in LexisNexis® Rewards Program checkbox is present and is checked by default", "Participate in LexisNexis® Rewards Program checkbox is present and is checked by default", Status.PASS);
					else
						report.updateTestLog("Participate in LexisNexis® Rewards Program checkbox is present and is checked by default", "Participate in LexisNexis® Rewards Program checkbox is present and is not checked by default", Status.FAIL);

					WebElement learnRewards = commonLibrary.isExist(UIMAP_USRfirstSignOn.learnRewards, 10);
					WebElement rewardRules = commonLibrary.isExist(UIMAP_USRfirstSignOn.rewardRules, 10);

					if ((learnRewards != null) && (rewardRules != null))
						report.updateTestLog("'Learn about the rewards program' and 'Reward Program Rules' links are preset under 'Participate in LexisNexis® Rewards Program'", "'Learn about the rewards program' and 'Reward Program Rules' links are preset under 'Participate in LexisNexis® Rewards Program'", Status.PASS);
					else
						report.updateTestLog("'Learn about the rewards program' and 'Reward Program Rules' links are preset under 'Participate in LexisNexis® Rewards Program'", "'Learn about the rewards program' and 'Reward Program Rules' links are not preset under 'Participate in LexisNexis® Rewards Program'", Status.FAIL);

					WebElement submitNext = commonLibrary.isExist(UIMAP_USRfirstSignOn.submitNext, 10);
					commonLibrary.clickButtonParentWithWait(submitNext, "Submit and Go to the next step");
					commonLibrary.sleep(30000);

					// pageCheck.PositiveCheck(driver, TextCheck, Flow);

				} else
					report.updateTestLog("Verify Locale, TimeZone, Program and Graduation Term fields are present", "Verify Locale, TimeZone, Program and Graduation Term fields are not present", Status.FAIL);

			} else {
				report.updateTestLog("Click on Student Radio button", "Student Radio button is not present", Status.FAIL);
			}
			WebElement trainStopHeader = commonLibrary.isExist(By.cssSelector("ol[id*='Steps']"), 10);
			int count = 0;
			do {
				count++;
				commonLibrary.sleep(2000);
			} while (trainStopHeader == null && count < 15);
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new SignInLNAccess(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify error text in sign in page
	// # Function Name : VerifyErrorText
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void verifyErrorText(String error) {
		boolean isError = false;
		WebElement errorDiv = commonLibrary.isExist(UIMAP_Register.error, 100);
		if (errorDiv != null) {
			List<WebElement> span = commonLibrary.isExistList(errorDiv, UIMAP_SearchResult.tagSpan, 10);
			for (WebElement item : span) {
				if (item.getText().contains(error)) {
					isError = true;
					break;
				}
			}
		} else {
			isError = false;
		}

		if (isError) {
			report.updateTestLog("Verify the error message " + error + " is displayed", "The error messge " + error + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the error message " + error + " is displayed", "The error messge " + error + " is not displayed", Status.FAIL);
		}

	}

}
