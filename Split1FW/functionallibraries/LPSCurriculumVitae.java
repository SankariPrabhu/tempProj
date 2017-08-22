package functionallibraries;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_LPSCurriculumVitae;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LPSCurriculumVitae extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public LPSCurriculumVitae(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpscurriculumvitae"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lpscurriculumvitae") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpscurriculumvitae")) {
			throw new IllegalStateException("LPS Curriculum Vitae page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify curriculum vitae page
	// displays
	// # Function Name : verifyCvPageDisplays
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPSCurriculumVitae verifyCvPageDisplays(String header) {
		WebElement pageHeader = commonLibrary.isExist(UIMAP_LPSCurriculumVitae.pageHeader, 10);
		if (pageHeader == null) {
			int count = 0;
			do {
				count = count + 1;
				pageHeader = commonLibrary.isExist(UIMAP_LPSCurriculumVitae.pageHeader, 10);
				if (pageHeader == null)
					commonLibrary.sleep(5000);
			} while (pageHeader == null && count <= 10);
		}
		if (pageHeader != null) {
			WebElement cvContainer = commonLibrary.isExist(UIMAP_LPSCurriculumVitae.resultContainer, 10);
			// Verifying CV page is displayed
			if (cvContainer != null && pageHeader.getText().trim().toLowerCase().contains(header.trim().toLowerCase())) {
				report.updateTestLog("Verify " + pageHeader.getText() + " page displays with list of records", pageHeader.getText() + " page is displayed with list of records", Status.PASS);
			} else {
				report.updateTestLog("Verify " + header + " page displays with list of records", header + " page is not displayed with list of records", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify " + header + " page displays with list of records", header + " page is not displayed with list of records", Status.FAIL);
		}

		return new LPSCurriculumVitae(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		// Calling general function for logging out
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack
	// # Author : Anbarasan
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public LPSReport clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new LPSReport(scriptHelper);
	}

}
