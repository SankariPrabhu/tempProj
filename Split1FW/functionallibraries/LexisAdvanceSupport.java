package functionallibraries;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.*;

public class LexisAdvanceSupport extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public LexisAdvanceSupport(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("support"))
				commonLibrary.sleep(5000);

		} while (!url.contains("support") && counter < 40);

		if (!driver.getCurrentUrl().contains("support")) {
			throw new IllegalStateException("Support page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a new window is open.
	// # Function Name : VerifyNewWindows     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceSupport verifyNewWindow() {
		// WebElement eltTitleNewWindow =
		// commonLibrary.isExistNegative(UIMAP_Home.eltTitleNewWindow, 10);
		if (driver.getTitle().contains("Support")) {

			report.updateTestLog("Verify Secondary browser window/New tab displays.", driver.getTitle() + " window is displayed.", Status.PASS);
			return new LexisAdvanceSupport(scriptHelper);
		} else
			throw new FrameworkException("Verify Secondary browser window/New tab displays.", "No new window is displayed.");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the new window.
	// # Function Name : CloseAndBackToHomePage     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home closeAndBackToHomePage() {
		driver.close();
		if (commonLibrary.switchToWindow("firsttime")) {
			report.updateTestLog("Verify secondary browser is closed", "Secondary browser is closed", Status.PASS);
		} else {
			report.updateTestLog("Verify secondary browser is closed", "Secondary browser is not closed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

}
