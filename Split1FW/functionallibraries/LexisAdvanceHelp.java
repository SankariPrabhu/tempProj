package functionallibraries;

import org.openqa.selenium.WebElement;

import UIMAP.UIMAP_Home;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.*;

public class LexisAdvanceHelp extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public LexisAdvanceHelp(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("newlexis"))
				commonLibrary.sleep(5000);

		} while (!url.contains("newlexis") && counter < 40);

		if (!driver.getCurrentUrl().contains("newlexis")) {
			throw new IllegalStateException("Help page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify a new window is open.
	// # Function Name : VerifyNewWindows     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceHelp verifyNewWindow() {
		WebElement txtHeaderNewWindow = commonLibrary.isExistNegative(UIMAP_Home.txtHeaderNewWindow1, 10);
		if (txtHeaderNewWindow != null) {
			report.updateTestLog("Verify Secondary browser window/New tab displays.", txtHeaderNewWindow.getText() + " window is displayed.", Status.PASS);
			return new LexisAdvanceHelp(scriptHelper);
		} else {
			WebElement eltPageContentNewWindow = commonLibrary.isExistNegative(UIMAP_Home.eltPageContentNewWindow, 10);
			txtHeaderNewWindow = commonLibrary.isExistNegative(eltPageContentNewWindow, UIMAP_Home.txtHeaderNewWindow, 10);
			if (txtHeaderNewWindow != null) {
				report.updateTestLog("Verify Secondary browser window/New tab displays.", txtHeaderNewWindow.getText() + " window is displayed.", Status.PASS);
				return new LexisAdvanceHelp(scriptHelper);
			} else
				throw new FrameworkException("Verify Secondary browser window/New tab displays.", "No new window is displayed.");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the new window.
	// # Function Name : CloseAndBackToHomePage     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home closeAndBackToHomePage() {
		driver.close();
		report.updateTestLog("Verify Secondary browser is closed.", "Secondary browser is closed.", Status.DONE);
		commonLibrary.switchToWindow("firsttime");
		return new Home(scriptHelper);
	}
}
