package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.Status;

import UIMAP.UIMAP_MedmalDashboard;
import UIMAP.UIMAP_MedmalExpertWitness;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class MedmalExpertWitness extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public MedmalExpertWitness(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalexpertwitness"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmalexpertwitness") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmalexpertwitness")) {
			throw new IllegalStateException("Medmal Expert Witness page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying the expert witness
	// displayed for a selected sub topic
	// # Function Name : verifyListDisplayed     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedmalExpertWitness verifyListDisplayed(String topic) {
		WebElement topNavigation = commonLibrary.isExist(UIMAP_MedmalDashboard.navigationBar, 10);
		WebElement selectedTab = commonLibrary.isExist(topNavigation, UIMAP_MedmalDashboard.selectedTab, 10);
		if (selectedTab.getText().equalsIgnoreCase("Expert Witness")) {
			WebElement signPost = commonLibrary.isExist(UIMAP_MedmalExpertWitness.signPost, 10);
			WebElement heading = commonLibrary.isExist(signPost, By.tagName("h1"), 10);
			if (heading.getText().equalsIgnoreCase(topic))
				report.updateTestLog("Expert Witness List is displayed for the selected topic '" + topic + "'", "Expert Witness List is displayed for the selected topic '" + topic + "'", Status.PASS);
			else
				report.updateTestLog("Expert Witness List is displayed for the selected topic '" + topic + "'", "Expert Witness List is not displayed for the selected topic '" + topic + "'", Status.FAIL);
		} else
			report.updateTestLog("Expert Witness List is displayed for the selected topic '" + topic + "'", "Expert Witness List is not displayed", Status.FAIL);
		return new MedmalExpertWitness(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove filter present
	// # Function Name : removeFilter     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public MedmalExpertWitness removeFilter() {
		WebElement removeFilter = commonLibrary.isExist(UIMAP_MedmalExpertWitness.removeFilter, 10);
		if (removeFilter != null) {
			WebElement close = commonLibrary.isExist(removeFilter, By.tagName("button"), 10);
			commonLibrary.clickButtonParentWithWait(close, "Remove Filter");
		}

		return new MedmalExpertWitness(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click first expert link
	// # Function Name : clickFirstExpert     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPSReport clickFirstExpert() {
		WebElement resultsList = commonLibrary.isExist(UIMAP_MedmalExpertWitness.expertResultsList, 10);
		if (resultsList != null) {
			WebElement expertLink = commonLibrary.isExist(UIMAP_MedmalExpertWitness.expertLink, 10);
			String expert = expertLink.getText();
			expertLink = commonLibrary.isExist(UIMAP_MedmalExpertWitness.expertLink, 10);
			commonLibrary.clickButtonParentWithWait(expertLink, expert);

			WebElement contentTitle = commonLibrary.isExist(UIMAP_MedmalExpertWitness.contentTitle, 10);
			WebElement header = commonLibrary.isExist(UIMAP_MedmalExpertWitness.globalHeader, 10);

			if (contentTitle.getText().contains("Snapshot") && header.getText().contains(expert))
				report.updateTestLog("Snapshot page is displayed for '" + expert + "' expert", "Snapshot page is displayed for '" + expert + "' expert", Status.PASS);
			else
				report.updateTestLog("Snapshot page is displayed for '" + expert + "' expert", "Snapshot page is not displayed for '" + expert + "' expert", Status.FAIL);
		}

		return new LPSReport(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform logout
	// # Function Name : logout     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExistNegative(By.linkText(UIMAP_Home.lnkTextSignOut),
		// 15);
		// if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
		// btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// }
		// if (lnkSignOut != null) {
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
		// }
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
		// }

		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

}
