package functionallibraries;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Interactivecitationworkstation;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ICWCertificate extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	// private GeneralFunctions generalFunctions = new
	// GeneralFunctions(scriptHelper);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public ICWCertificate(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("icwcertificate"))
				commonLibrary.sleep(5000);

		} while (!url.contains("icwcertificate") && counter < 40);

		if (!driver.getCurrentUrl().contains("icwcertificate")) {
			throw new IllegalStateException("ICW Certificate is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout of the application
	// # Function Name : logout
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		//
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

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if the certification page
	// displayed
	// # Function Name : verifyCertificatePage
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWCertificate verifyCertificatePage() {

		// driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

		if (driver.getCurrentUrl().toLowerCase().contains("icwcertificate")) {
			report.updateTestLog("Verify if Completion certificate page displays.", "Completion certificate page displays.", Status.PASS);
		}

		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Pratik
	// # Date Created : June'2015
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation clickProductlogo() {
		generalFunctions.clickProductLogo();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the average number of
	// attempts
	// # Function Name : verifyAvgAttemptsCount   
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public ICWCertificate verifyAvgAttemptsCount(String number) {
		WebElement avgAttempts = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.avgAttempts, 10);
		if (avgAttempts.getText().trim().equals(number))
			report.updateTestLog("Verify Average attempts per problems displays as: " + number, "Average attempts per problem displays as: " + number, Status.PASS);
		else
			report.updateTestLog("Verify Average attempts per problems displays as: " + number, "Average attempts per problem does not display as: " + number, Status.FAIL);
		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Marks Report
	// # Function Name : verifyMarksReport   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public ICWCertificate verifyMarksReport() {
		WebElement tableReport = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.tableReport, 10);
		WebElement totalProbCount = commonLibrary.isExist(tableReport, UIMAP_Interactivecitationworkstation.totalProbCount, 10);
		WebElement correctProbCount = commonLibrary.isExist(tableReport, UIMAP_Interactivecitationworkstation.correctProbCount, 10);
		WebElement incorrectProbCount = commonLibrary.isExist(tableReport, UIMAP_Interactivecitationworkstation.incorrectProbCount, 10);

		if (totalProbCount != null)
			report.updateTestLog("Verify if total number of problem in certificate page displays", "Total number of problem in certificate page displays as: " + totalProbCount.getText(), Status.PASS);

		if (correctProbCount != null)
			report.updateTestLog("Verify if correct number of problem in certificate page displays", "Correct number of problem in certificate page displays as: " + correctProbCount.getText(), Status.PASS);

		if (incorrectProbCount != null)
			report.updateTestLog("Verify if incorrect number of problem in certificate page displays", "Incorrect number of problem in certificate page displays as: " + incorrectProbCount.getText(), Status.PASS);

		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Print Icon and Choose default
	// settings
	// # Function Name : printWithDefaultSettings
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public ICWCertificate printWithDefaultSettings(String parentWindow) {
		generalFunctions.printWithDefaultSettings(parentWindow);
		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : navigateToViewAllHistory
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public History navigateToViewAllHistory() {
		generalFunctions.navigateToHistoryFooterLink("View All History");
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Home link
	// # Function Name : clickHome
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation clickHome() {
		generalFunctions.clickHome();
		return new Interactivecitationworkstation(scriptHelper);
	}

}
