package functionallibraries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_Interactivecitationworkstation;
import UIMAP.UIMAP_ResearchMap;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ICWStudentStatus extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	// private GeneralFunctions generalFunctions = new
	// GeneralFunctions(scriptHelper);

	public ICWStudentStatus(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("icw"))
				commonLibrary.sleep(5000);

		} while (!url.contains("icw") && counter < 40);

		if (!driver.getCurrentUrl().contains("icw")) {
			// if (!driver.getCurrentUrl().contains("icwstudentstatus")) {
			throw new IllegalStateException("ICW Student Status page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify column names
	// # Function Name : Verify_columnheader     
	// # Author : Revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus verifyColumnheader(String strColumnName) {
		String[] arrColumnName = strColumnName.split(";");

		// boolean blnFlag = false;
		WebElement bluebooktable = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 20);
		List<WebElement> lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.bluebookcolheader, 20);

		for (int i = 0; i < lstcolheaders.size(); i++) {

			if (lstcolheaders.get(i).getText().contains(arrColumnName[i])) {
				report.updateTestLog("Verify " + arrColumnName[i] + " displayed as column headers", arrColumnName[i] + " is displayed as column headers ", Status.PASS);
			} else {
				report.updateTestLog("Verify " + arrColumnName[i] + " displayed as column headers", arrColumnName[i] + " is not displayed as column headers", Status.FAIL);
			}
		}
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the exercise columns
	// # Function Name : verify_exercisename     
	// # Author : Revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus verifyExerciseName(String columnName, String exerciseName, String log) {
		String[] arrExerciseName = exerciseName.split(";");
		List<WebElement> lstcolheaders = null;
		String strVerifyText = null;
		WebElement bluebooktable = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 20);
		switch (columnName) {
		case "Exercise column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.exercisenamecol, 20);
		}
		case "Total problems column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.totalcountnumbercol, 20);
		}
		case "Remaining/Skipped column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.incompletecountnumbercol, 20);
		}
		case "Status column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.exercisestatuscol, 20);
		}
		case "Grades column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.gradenumbercol, 20);
		}
		case "Correct column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.correctcountnumbercol, 20);
		}
		case "Incorrect column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.incorrectcountnumbercol, 20);
		}
		case "Last Accessed column column": {
			lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.lastupdatedcol, 20);
		}

		}
		boolean blnFlag = true;
		for (int i = 1; i < lstcolheaders.size() - 1; i++) {
			if (arrExerciseName.length == 1) {
				strVerifyText = exerciseName;
			} else {
				strVerifyText = arrExerciseName[i];
			}

			if (!(lstcolheaders.get(i).getText().contains(strVerifyText))) {
				blnFlag = false;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Verify the " + columnName, log + " is displayed " + columnName, Status.PASS);
		} else {
			report.updateTestLog("Verify the " + columnName, log + " is not displayed " + columnName, Status.FAIL);
		}

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise
	// # Function Name : resetExercise     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus resetExercise(String exerciseName) {
		WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		boolean flag = false;
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			if (row.getText().toLowerCase().contains(exerciseName.toLowerCase()) && row.getText().toLowerCase().contains("Completed".toLowerCase())) {
				WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
				commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
				WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.resetExercise, 10);
				commonLibrary.clickLinkWithWebElementWithWait(reset, "Reset exercise");

				WebElement confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer, 10);
				commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");
				flag = true;
				break;
			}
		}

		try {
			WebElement currProdNew = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			int count = 0;
			do {
				commonLibrary.sleep(50000);
				currProdNew = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
				count++;
			} while (currProdNew.equals(currentProduct) && count < 20);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (flag) {
			WebElement table1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			List<WebElement> rows1 = commonLibrary.isExistList(table1, UIMAP_Interactivecitationworkstation.rows, 10);
			for (WebElement row1 : rows1) {
				if (row1.getText().contains(exerciseName) && row1.getText().contains("Not Started")) {
					report.updateTestLog("Verify if the exercise is reset ", "The exercise is reset", Status.PASS);
					break;
				}

			}

		} else
			report.updateTestLog("Reset the exercise: " + exerciseName, exerciseName + " is not reset", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application.
	// # Function Name : logout     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut != null)
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
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
	// # Function Description : Function to verify Delivery Options
	// # Function Name : Verify_DeliveryOptions
	// # Author : Revathi
	// # Date Created : May'15 <ICWStudentStatus.java>
	// #*****************************************************************************************************************************
	public ICWStudentStatus verifyDeliveryOptions() {

		WebElement AddFolder = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.btnAddFolder, 20);
		if (AddFolder != null)
			report.updateTestLog("Verify Delivery icons", " Add to folder icon with drop down is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Add to folder icon with drop down is not displayed", Status.FAIL);

		WebElement DeliveryIcon = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.lnkDeliveryDownload, 20);
		if (DeliveryIcon != null)
			report.updateTestLog("Verify Delivery icons", "delivery icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "delivery icon is not displayed", Status.FAIL);
		WebElement EmailIcon = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.btnEmail, 20);
		if (EmailIcon != null)
			report.updateTestLog("Verify Delivery icons", "Email icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Email icon is ot displayed", Status.FAIL);
		WebElement PrintIcon = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.lnkPrint, 20);
		if (PrintIcon != null)
			report.updateTestLog("Verify Delivery icons", "Print icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons", "Print icon is not displayed", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Sort Options
	// # Function Name : Verify_SortOptions
	// # Author : Revathi
	// # Date Created : May'15 <ICWStudentStatus.java>
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifySortOptions() {
		// WebElement sortdiv =
		// commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divSort,
		// 10);
		WebElement sortlist = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divSortList, 10);
		if (sortlist != null)
			report.updateTestLog("Verify 'Sort by:' drop down list", "The 'Sort by:' drop down list is displayed in the top of the page.", Status.PASS);
		else
			report.updateTestLog("Verify 'Sort by:' drop down list", "The 'Sort by:' drop down list is not displayed in the top of the page.", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click icw logo
	// # Function Name : Click_ICWLogo
	// # Author : Revathi
	// # Date Created : May'15 <ICWStudentStatus.java>
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickICWLogo() {
		WebElement prdctSwttcher = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.prdctSwtcher, 20);
		WebElement divLogo = commonLibrary.isExist(prdctSwttcher, UIMAP_Interactivecitationworkstation.divlogo, 20);
		WebElement imglogo = commonLibrary.isExist(divLogo, UIMAP_Interactivecitationworkstation.imglogo, 20);
		commonLibrary.clickLinkWithWebElementWithWait(imglogo, "Home link  to access the home page");
		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus clickLinkinPods(String podName, String strlinkName) {
		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					List<WebElement> lstlinks = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					for (WebElement link1 : lstlinks) {
						if (link1.getText().contains(strlinkName)) {
							commonLibrary.clickButtonParentWithWait(link1, strlinkName);
							blnFlag = true;
							break;
						}
					}
				}
				if (blnFlag) {
					break;
				}
			}
		if (blnFlag) {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.FAIL);
		}

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create folder and save
	// # Function Name : createFolderAndSave     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus createFolderAndSave(String folderName, boolean banner, String strTCName) {
		generalFunctions.createFolderAndSave(folderName, banner, strTCName);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to folders
	// # Function Name : navigateToFolder     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public WorkFolders navigateToFolders() {
		generalFunctions.navigateToFolders();
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to request reset Exercise
	// # Function Name : requestResetExercise     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus requestResetExercise(String exercise) {
		boolean flag = false;
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			WebElement exer = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.exercisename, 10);
			WebElement link = commonLibrary.isExistNegative(exer, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();
				if (exeName.equalsIgnoreCase(exercise)) {
					WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
					if (action != null)
						commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
					commonLibrary.sleep(100000);
					WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.requestResetExercise, 10);
					commonLibrary.clickLinkWithWebElementWithWait(reset, "Request Reset");
					flag = true;
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click Request Reset for a completed exercise.", "Request Reset for a completed exercise is not clicked.", Status.FAIL);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Request Reset Dialog Box
	// # Function Name : verifyRequestReset
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyRequestReset(String para1, String para2) {
		WebElement requestResetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (requestResetDialog.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog contains " + para1, Status.PASS);
		} else
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog does not contains " + para1, Status.FAIL);

		List<WebElement> links = commonLibrary.isExistList(requestResetDialog, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
		if (requestResetDialog.getText().toLowerCase().contains(para2.toLowerCase()) && links.size() >= 1) {
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para2 + " along with professor details.", "Complete Exercise dialog contains " + para2 + " along with professor details.", Status.PASS);
		} else
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para2 + " along with professor details.", "Complete Exercise dialog does not contains " + para2 + " along with professor details.", Status.FAIL);

		List<WebElement> buttons = commonLibrary.isExistList(requestResetDialog, UIMAP_Interactivecitationworkstation.input, 10);
		boolean isSubmit = false, isCancel = false;
		for (WebElement item : buttons) {
			if (item.getAttribute("value").equalsIgnoreCase("Submit Request"))
				isSubmit = true;
			if (item.getAttribute("value").equalsIgnoreCase("Cancel"))
				isCancel = true;

			if (isSubmit && isCancel)
				break;
		}

		if (isSubmit && isCancel) {
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are present.", Status.PASS);
		} else
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are not present.", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Submit Request in Dialog Box
	// # Function Name : verifyRequestReset
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus clickSubmitRequest() {
		WebElement submitRequest = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.submitRequest, 10);
		commonLibrary.clickLinkWithWebElementWithWait(submitRequest, submitRequest.getAttribute("value"));
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot for manual
	// verification.
	// # Function Name : takeScreenShot
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus takeScreenShot(String strTCName, String strStep) {
		generalFunctions.takeScreenShot(strTCName, strStep);
		return new ICWStudentStatus(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and save the name of
	// Exercise that has been Reset
	// # Function Name : resetExerciseSaveName     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus resetExerciseSaveName(String tcName, String dataSheet, String colName, String para1, String para2) {
		boolean flag = false;
		String exerciseName = "";
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			if (row.getText().contains("Completed")) {
				WebElement exercisename = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.exercisename, 10);
				WebElement link = commonLibrary.isExistNegative(exercisename, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
				exerciseName = link.getText();
				exerciseName = exerciseName.split(":")[1].trim();
				final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
				String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
				ExcelDataAccess excel = new ExcelDataAccess(datatablePath, dataSheet);
				excel.setDatasheetName("General_Data");
				int iRowNo = excel.getRowNum(tcName, 0);
				excel.setValue(iRowNo, colName, exerciseName.trim());

				WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
				commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
				WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.resetExercise, 10);
				commonLibrary.clickLinkWithWebElementWithWait(reset, "Reset exercise");

				verifyResetDialogBox(para1, para2);
				WebElement confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer, 10);
				commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");
				flag = true;
				break;
			}
		}

		if (flag) {
			WebElement table1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			List<WebElement> rows1 = commonLibrary.isExistList(table1, UIMAP_Interactivecitationworkstation.rows, 10);
			for (WebElement row1 : rows1) {
				if (row1.getText().contains(exerciseName) && row1.getText().contains("Not Started")) {
					report.updateTestLog("Verify if the exercise is reset ", "The exercise is reset", Status.PASS);
					WebElement exercisename = commonLibrary.isExistNegative(row1, UIMAP_Interactivecitationworkstation.exercisename, 10);
					Date currDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
					if (exercisename.getText().contains(sdf.format(currDate)) && exercisename.getText().toLowerCase().contains("Exercise Reset on")) {
						report.updateTestLog("Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", "'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", Status.PASS);
					} else
						report.updateTestLog("Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", "'Exercise Reset on MMM DD,YYYY' does not display below the Exercise name in the tabular column ", Status.PASS);
					break;
				}

			}

		} else
			report.updateTestLog("Reset the exercise: " + exerciseName, exerciseName + " is not reset", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and save the name of
	// Exercise that has been Reset
	// # Function Name : resetExerciseSaveName     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyResetDialogBox(String para1, String para2) {
		WebElement resetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (resetDialog.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog contains " + para1, Status.PASS);
		} else
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog does not contains " + para1, Status.FAIL);

		if (resetDialog.getText().toLowerCase().contains(para2.toLowerCase())) {
			report.updateTestLog("Verify if Reset dialog contains " + para2 + " along with professor details.", "Reset dialog contains " + para2 + " along with professor details.", Status.PASS);
		} else
			report.updateTestLog("Verify if Reset dialog contains " + para2 + " along with professor details.", "Reset Exercise dialog does not contains " + para2 + " along with professor details.", Status.FAIL);

		List<WebElement> buttons = commonLibrary.isExistList(resetDialog, UIMAP_Interactivecitationworkstation.input, 10);
		boolean isSubmit = false, isCancel = false;
		for (WebElement item : buttons) {
			if (item.getAttribute("value").equalsIgnoreCase("Reset this Exercise"))
				isSubmit = true;
			if (item.getAttribute("value").equalsIgnoreCase("Cancel"))
				isCancel = true;

			if (isSubmit && isCancel)
				break;
		}

		if (isSubmit && isCancel) {
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are present.", Status.PASS);
		} else
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are not present.", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Student Report page is
	// displayed.
	// # Function Name : verifyStudentStatus     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyStudentStatus(String title) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.header, 10);
		if (header.getText().toLowerCase().contains(title.toLowerCase())) {
			report.updateTestLog("Verify Student report is displayed.", "Student report for exercise/student: " + title + " is displayed.", Status.PASS);
		} else
			report.updateTestLog("Verify Student report is displayed.", "Student report for exercise/student: " + title + " is not displayed.", Status.PASS);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether a particular exercise
	// has been Reset.
	// # Function Name : verifyExerciseReset     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyExerciseReset(String exerciseName) {
		boolean flag = false;
		WebElement table1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		if (table1 == null)
			table1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 20);
		List<WebElement> rows1 = commonLibrary.isExistList(table1, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row1 : rows1) {
			WebElement exer = commonLibrary.isExistNegative(row1, UIMAP_Interactivecitationworkstation.exercisename, 10);
			if (exer == null)
				exer = commonLibrary.isExistNegative(row1, UIMAP_Interactivecitationworkstation.studentName, 10);
			WebElement link = commonLibrary.isExistNegative(exer, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();
				if (exeName.toLowerCase().contains(exerciseName.toLowerCase()) && row1.getText().toLowerCase().contains("Not Started".toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify if the exercise " + exerciseName + " is reset ", "The exercise " + exerciseName + " is reset", Status.PASS);

					report.updateTestLog("Verify if the exercise " + exerciseName + " is displayed in Exercise Name Coloumn", "The exercise " + exerciseName + " is displayed in Exercise Name Coloumn", Status.PASS);

					Date currDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
					if (exer.getText().contains(sdf.format(currDate)) && exer.getText().toLowerCase().contains("Exercise Reset on".toLowerCase())) {
						report.updateTestLog("Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", "'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", Status.PASS);
					} else
						report.updateTestLog("Verify if 'Exercise Reset on MMM DD,YYYY' displays below the Exercise name in the tabular column ", "'Exercise Reset on MMM DD,YYYY' does not display below the Exercise name in the tabular column ", Status.PASS);
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Verify if the exercise " + exerciseName + " is reset ", "The exercise " + exerciseName + " is not reset", Status.FAIL);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise
	// # Function Name : resetExercise     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus resetExerciseNoVerification(String exerciseName) {
		boolean flag = false;
		WebElement tableOld = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			WebElement exer = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.exercisename, 10);
			WebElement link = commonLibrary.isExistNegative(exer, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();
				if (exeName.equalsIgnoreCase(exerciseName)) {
					WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
					if (action != null) {
						commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
						WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.resetExercise, 10);
						commonLibrary.clickLinkWithWebElementWithWait(reset, "Reset exercise");
					} else {
						WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.resetExercise, 10);
						commonLibrary.clickLinkWithWebElementWithWait(reset, "Reset exercise");
					}
					WebElement confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer, 10);
					commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");
					flag = true;
					break;
				}
			}
		}
		try {
			tableOld = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			WebElement tableNew = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			int count = 0;
			do {
				commonLibrary.sleep(20000);
				tableNew = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
				count++;
			} while (tableNew.equals(tableOld) && count < 15);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		if (!flag)
			report.updateTestLog("Reset the exercise " + exerciseName, "The exercise " + exerciseName + " can not be reset", Status.FAIL);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and verify
	// # Function Name : resetExerciseVerify     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus resetExerciseVerify(String exerciseName, String para1, String para2) {
		generalFunctions.resetExerciseVerify(exerciseName, para1, para2);
		return new ICWStudentStatus(scriptHelper);
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
	// # Function Description : Function to verify the dialog box after
	// resetting an execise for all students
	// # Function Name : verifyResetAllDialogBox     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyResetAllDialogBox(String para1, String para2) {
		WebElement resetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (resetDialog.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog contains " + para1, Status.PASS);
		} else
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog does not contains " + para1, Status.FAIL);

		List<WebElement> buttons = commonLibrary.isExistList(resetDialog, UIMAP_Interactivecitationworkstation.input, 10);
		boolean isSubmit = false, isCancel = false;
		for (WebElement item : buttons) {
			if (item.getAttribute("value").equalsIgnoreCase("Reset this Exercise"))
				isSubmit = true;
			if (item.getAttribute("value").equalsIgnoreCase("Cancel"))
				isCancel = true;

			if (isSubmit && isCancel)
				break;
		}

		if (isSubmit && isCancel) {
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are present.", Status.PASS);
		} else
			report.updateTestLog("Verify Submit Request and Cancel buttons are present.", "Submit Request and Cancel buttons are not present.", Status.FAIL);

		return new ICWStudentStatus(scriptHelper);

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

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message and click on
	// Dismiss button.
	// # Function Name : verifyBannerMessageClickDismiss
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyBannerMessageClickDismiss(String text) {
		generalFunctions.verifyBannerMessageClickDismiss(text);
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create folder and save
	// # Function Name : createFolderAndSave_new 
	// # Author : Vennila
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus createFolderAndSave_new(String folderName) {

		try {
			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

			// CLICK ON <<<CREATE NEW FOLDER>>>
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.addFolder, 10);
			if (addtoFolder != null)
				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

			commonLibrary.sleep(8000000);
			WebElement chooseFolder = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.chooseFolder, 10);
			if (chooseFolder == null) {
				addtoFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.addFolder, 10);
				if (addtoFolder != null) {
					commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");
					commonLibrary.sleep(1000000);
				}
				chooseFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.chooseFolder, 10);

			}

			if (chooseFolder != null) {
				report.updateTestLog("Verify save instructor dashboard report to dropdown is displayed", "Save instructor dashboard report to dropdown is displayed", Status.PASS);
				commonLibrary.clickButtonParentWithWait(chooseFolder, "Choose Folder");
				commonLibrary.sleep(300000);
			}

			commonLibrary.sleep(800000);

			WebElement createNewFolder = commonLibrary.isExist(UIMAP_ResearchMap.createNewFolder, 10);
			if (createNewFolder != null)
				commonLibrary.clickButtonParentWithWait(createNewFolder, "Create Folder");

			// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

			// WebElement
			// folderNam=commonLibrary.isExist(UIMAP_ResearchMap.folderName,
			// 10);
			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null) {
				commonLibrary.setDataInTextBoxWithLog(UIMAP_Document.txtFolderName, folderName);
				commonLibrary.sleep(3000);
			}

			// CLICK ON <<<CREATE>>>
			WebElement create = commonLibrary.isExist(UIMAP_ResearchMap.createFolder, 10);
			if (create != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(create, "create");
				else
					commonLibrary.clickButtonParentWithWait(create, "Create");

			create = commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
			WebElement createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(7000000);
					createNew = commonLibrary.isExistNegative(UIMAP_CounselBenchmarking.createFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (create.equals(createNew) && count < 80);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// CLICK ON <<<SAVE>>>
			WebElement saveFolder = commonLibrary.isExist(UIMAP_ResearchMap.saveworkFolder, 10);
			if (saveFolder != null)
				if (browsername.contains("internet"))
					commonLibrary.clickJS(saveFolder, "Save");

				else
					commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
			commonLibrary.sleep(40000);
			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolder, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(700000);
					saveNew = commonLibrary.isExistNegative(UIMAP_ResearchMap.saveworkFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (saveFolder == saveNew && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(1000000);
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new ICWStudentStatus(scriptHelper);
	}

}
