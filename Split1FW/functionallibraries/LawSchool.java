package functionallibraries;

import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LawSchool extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public LawSchool(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lawschool"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lawschool") && counter < 40);

		if (!driver.getCurrentUrl().contains("lawschool")) {
			throw new IllegalStateException("Law School home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify landing page pods
	// # Function Name : verify_LandingPage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LawSchool verifyLandingPage() {
		if (driver.getCurrentUrl().contains("lawschool")) {
			report.updateTestLog("Verify Law School landing page is displayed", "Law School landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Law School landing page is displayed", "Law School landing page is not displayed", Status.FAIL);
		}
		return new LawSchool(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to log out of application
	// # Function Name : Logout     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExist(UIMAP_Home.lawSchoolSignOut, 20);
		// if (lnkSignOut != null)
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

}
