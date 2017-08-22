package functionallibraries;

import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Law extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public Law(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("law"))
				commonLibrary.sleep(5000);

		} while (!url.contains("law") && counter < 40);

		if (!driver.getCurrentUrl().contains("law")) {
			throw new IllegalStateException("Law page is expected, but not displayed!");
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
		if (commonLibrary.switchToWindow("firsttime")) {
			report.updateTestLog("Close secondary Browser", "Secondary browser is closed", Status.PASS);
			report.updateTestLog("Verify secondary browser is closed", "Secondary browser is closed", Status.PASS);
		} else {
			report.updateTestLog("Verify secondary browser is closed", "Secondary browser is not closed", Status.FAIL);
		}
		return new Home(scriptHelper);
	}

}