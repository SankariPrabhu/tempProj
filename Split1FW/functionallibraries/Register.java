package functionallibraries;

import java.util.List;
import java.util.Random;
import org.openqa.selenium.WebElement;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import UIMAP.UIMAP_Register;
import UIMAP.UIMAP_SearchResult;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Register extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public Register(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("register"))
				commonLibrary.sleep(5000);

		} while (!url.contains("register") && counter < 40);

		if (!driver.getCurrentUrl().contains("register")) {
			throw new IllegalStateException("Register page is expected, but not displayed!");
		}
	}
	

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues in LA Registration page
	// # Function Name : enterValues_LA_Registration     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public USRFirstSignOn enterValuesLARegistration(String firstName, String lastName, String eMail, String regCode) {
		try {
			Random rndNum = new Random();
			int rand = rndNum.nextInt();
			WebElement frstName = commonLibrary.isExist(UIMAP_Register.firstName, 10);
			WebElement lstName = commonLibrary.isExist(UIMAP_Register.lastName, 10);
			WebElement eMailId = commonLibrary.isExist(UIMAP_Register.eMail, 10);
			WebElement regnCode = commonLibrary.isExist(UIMAP_Register.registrationCode, 10);
			commonLibrary.setDataInTextBox(frstName, firstName + rand, "First Name");
			commonLibrary.setDataInTextBox(lstName, lastName + rand, "Last Name");
			commonLibrary.setDataInTextBox(eMailId, eMail, "Email");
			commonLibrary.setDataInTextBox(regnCode, regCode, "Registration Code");

			WebElement submitNext = commonLibrary.isExist(UIMAP_Register.submitNext, 10);
			commonLibrary.clickButtonParentWithWait(submitNext, "Submit and Go to the next step");
			commonLibrary.sleep(50000);
			generalFunctions.documentState(driver);
			// pageCheck.PositiveCheck(driver, "Build Personal Profile Page",
			// "Build Personal Profile Page");
			// WebElement student =
			// commonLibrary.isExist(UIMAP_USRfirstSignOn.student, 20);
			// int count=0;
			// do
			// {
			// student = commonLibrary.isExist(UIMAP_USRfirstSignOn.student,
			// 20);
			// count=count+1;
			// }
			// while(student==null && count<=10);
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new USRFirstSignOn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to EnterValues in LA Registration Error
	// # Function Name : enterValues_LA_RegistrationError     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public USRFirstSignOn enterValuesLARegistrationError(String firstName, String lastName, String eMail, String regCode) {
		try {
			WebElement frstName = commonLibrary.isExist(UIMAP_Register.firstName, 10);
			WebElement lstName = commonLibrary.isExist(UIMAP_Register.lastName, 10);
			WebElement eMailId = commonLibrary.isExist(UIMAP_Register.eMail, 10);
			WebElement regnCode = commonLibrary.isExist(UIMAP_Register.registrationCode, 10);
			commonLibrary.setDataInTextBox(frstName, firstName, "First Name");
			commonLibrary.setDataInTextBox(lstName, lastName, "Last Name");
			commonLibrary.setDataInTextBox(eMailId, eMail, "Email");
			commonLibrary.setDataInTextBox(regnCode, regCode, "Registration Code");

			WebElement submitNext = commonLibrary.isExist(UIMAP_Register.submitNext, 10);
			commonLibrary.clickButtonParentWithWait(submitNext, "Submit and Go to the next step");
			commonLibrary.sleep(15000);
			generalFunctions.documentState(driver);
			// pageCheck.PositiveCheck(driver, "Registration", "Register");
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new USRFirstSignOn(scriptHelper);
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
