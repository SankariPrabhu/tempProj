package functionallibraries;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Interactivecitationworkstation;
import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ICWProfHome extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public ICWProfHome(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("icwprofessorhome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("icwprofessorhome") && counter < 40);

		if (!driver.getCurrentUrl().contains("icwprofessorhome")) {
			throw new IllegalStateException("ICW Professor Home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Student details
	// # Function Name : selectStudentDetails     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus selectStudentDetails(String student, String citation) {
		WebElement stud = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.selectStudent, 10);
		commonLibrary.selectFromListOption(stud, student);

		WebElement cit = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.selectCitation, 10);
		commonLibrary.selectFromListOption(cit, citation);

		WebElement getReport = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.getStatusReportStud, 10);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(getReport, "Get Status Report for Student");
		else
			commonLibrary.clickLinkWithWebElementWithWait(getReport, "Get Status Report for Student");

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application
	// # Function Name : logout     
	// # Author : uma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
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
	// # Function Description : Function to select tab in Status Report Pod
	// # Function Name : selectTab     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWProfHome selectTab(String term) {
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 10);
		commonLibrary.selectFromListButton(tabs, term);
		return new ICWProfHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether Get Status Report
	// Button is disabled.
	// # Function Name : verifyGetStatusButtonDisabled     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWProfHome verifyGetStatusButtonDisabled() {
		WebElement podContent = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.podContent, 10);
		WebElement getReport = commonLibrary.isExist(podContent, UIMAP_Interactivecitationworkstation.getStatusReportStud, 10);
		if (getReport.isEnabled())
			report.updateTestLog("Verify 'Get status Report for Student' button is disabled.", "'Get status Report for Student' button is enabled.", Status.FAIL);
		else
			report.updateTestLog("Verify 'Get status Report for Student' button is disabled.", "'Get status Report for Student' button is disabled.", Status.PASS);

		return new ICWProfHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ICW home page is displayed
	// # Function Name : verifyIcwHomePage   
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWProfHome verifyIcwHomePage() {
		WebElement logo = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divlogo, 20);

		WebElement home = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.home);
		if (logo != null && home != null) {
			if (logo.getText().contains("Interactive Citation Workstation") && home.getText().contains("Home")) {
				report.updateTestLog("Verify ICW home page is displayed", "ICW home page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
		}

		return new ICWProfHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab and get status in Status
	// Report Pod
	// # Function Name : selectTabAndGetStatus     
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation selectTabAndGetStatus(String term, String citationManual, String exercise) {
		String isr = "Individual Student Report";
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 10);
		commonLibrary.selectFromListButton(tabs, term);

		if (term.toLowerCase().equals(isr.toLowerCase())) {
			WebElement selectFromStudentDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromStudentDd, 10);
			if (selectFromStudentDd != null)
				commonLibrary.selectByVisibleText(selectFromStudentDd, citationManual);

			commonLibrary.sleep(5000);

			WebElement selectFromStuCitationDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromStuCitationDd, 10);
			if (selectFromStuCitationDd != null)
				commonLibrary.selectByVisibleText(selectFromStuCitationDd, exercise);

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.getStatusReportStu, 10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport, getStatusReport.getText());
			}

			commonLibrary.sleep(5000);

		} else {
			WebElement selectFromCItationManualDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromCItationManualDd, 10);
			if (selectFromCItationManualDd != null)
				commonLibrary.selectByVisibleText(selectFromCItationManualDd, citationManual);

			commonLibrary.sleep(5000);

			WebElement selectFromExerciseDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromExerciseDd, 10);
			if (selectFromExerciseDd != null)
				commonLibrary.selectByVisibleText(selectFromExerciseDd, exercise);

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.getStatusReport, 10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport, getStatusReport.getText());
			}

			commonLibrary.sleep(5000);

		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab and get status in Status
	// Report Pod
	// # Function Name : selectTabAndGetStatus     
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus selectTabAndGetStatus1(String term, String citationManual, String exercise) {
		String isr = "Individual Student Report";
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 10);
		commonLibrary.selectFromListButton(tabs, term);

		if (term.toLowerCase().equals(isr.toLowerCase())) {
			WebElement selectFromStudentDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromStudentDd, 10);
			if (selectFromStudentDd != null)
				commonLibrary.selectByVisibleText(selectFromStudentDd, citationManual);

			commonLibrary.sleep(5000);

			WebElement selectFromStuCitationDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromStuCitationDd, 10);
			if (selectFromStuCitationDd != null)
				commonLibrary.selectByVisibleText(selectFromStuCitationDd, exercise);

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.getStatusReportStu, 10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport, getStatusReport.getText());
			}

			commonLibrary.sleep(5000);

		} else {
			WebElement selectFromCItationManualDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromCItationManualDd, 10);
			if (selectFromCItationManualDd != null)
				commonLibrary.selectByVisibleText(selectFromCItationManualDd, citationManual);

			commonLibrary.sleep(5000);

			WebElement selectFromExerciseDd = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectFromExerciseDd, 10);
			if (selectFromExerciseDd != null)
				commonLibrary.selectByVisibleText(selectFromExerciseDd, exercise);

			commonLibrary.sleep(5000);

			WebElement getStatusReport = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.getStatusReport, 10);
			if (getStatusReport != null) {
				commonLibrary.clickButtonParentWithWait(getStatusReport, getStatusReport.getText());
			}

			commonLibrary.sleep(5000);

		}
		return new ICWStudentStatus(scriptHelper);
	}
}