package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_Interactivecitationworkstation;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Settings;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Interactivecitationworkstation extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public Interactivecitationworkstation(ScriptHelper scriptHelper) {
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

			throw new IllegalStateException("Interactive Citation Workstation page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the icw logo
	// # Function Name : Verify_ICWLogo   
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyICWLogo() {
		generalFunctions.verifyICWLogo();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout from the application
	// # Function Name : logout   
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to saving url to note pad
	// # Function Name : getCurrentUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation getCurrentUrl(String excelData) {

		generalFunctions.getCurrentUrl(excelData);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ICW home page is displayed
	// # Function Name : verifyIcwHomePage   
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyIcwHomePage() {
		WebElement logo = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divlogo, 20);
		WebElement logoImage = commonLibrary.isExist(logo, UIMAP_Interactivecitationworkstation.imglogo, 20);
		WebElement home = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.home);
		if (logoImage != null && home != null) {
			if (logoImage.getText().contains("Interactive Citation Workstation") && home.getText().contains("Home")) {
				report.updateTestLog("Verify ICW home page is displayed", "ICW home page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ICW pods
	// # Function Name : verifyICWPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyICWPods(String podName) {
		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					blnFlag = true;
					break;
				}

			}
		if (blnFlag) {
			report.updateTestLog("Verify pod " + podName, podName + " is displayed for the entitled user", Status.PASS);
		} else {
			report.updateTestLog("Verify pod " + podName, podName + " is not displayed for the entitled user", Status.FAIL);
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify links in ICW pods
	// # Function Name : verify_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyLinkinPods(String podName, String strlinkName) {
		boolean blnFlag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0)
			for (WebElement button : lstButtons) {
				if (button.getText().contains(podName)) {
					WebElement podLinks = commonLibrary.isExist(button, UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					if (podLinks.getText().contains(strlinkName)) {
						blnFlag = true;
						break;
					}
				}
			}
		if (blnFlag) {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify link " + strlinkName + " in pod " + podName, "link " + strlinkName + " in pod " + podName + " is not displayed", Status.FAIL);
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWStudentStatus clickLinkinPods(String podName, String strlinkName) {

		WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		generalFunctions.clicklinkinPods(podName, strlinkName);
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
		return new ICWStudentStatus(scriptHelper);
	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to click links in ICW pods w.r.t
	// status
	// // # Function Name : click_linkinPods   
	// // # Author : veeshma
	// // # Date Created : Jun'15
	// //
	// #*****************************************************************************************************************************
	// public ICWExcercise click_linkinPods_Using_Status(String podName, String
	// status) {
	//
	// boolean blnFlag = false;
	// String strlinkName = "";
	// ;
	// List<WebElement> lstButtons =
	// commonLibrary.isExist_List(UIMAP_Interactivecitationworkstation.icwPods,
	// 20);
	// if (lstButtons.size() > 0)
	// for (WebElement button : lstButtons) {
	// if (button.getText().contains(podName)) {
	// List<WebElement> rows =
	// commonLibrary.isExist_List(UIMAP_Interactivecitationworkstation.rows,
	// 20);
	// for (WebElement row : rows) {
	// if (row.getText().contains(status)) {
	// WebElement excercise = commonLibrary.isExist(row,
	// UIMAP_Interactivecitationworkstation.exercisename, 10);
	// WebElement link = commonLibrary.isExist(excercise,
	// UIMAP_Interactivecitationworkstation.lnkLinks, 10);
	// strlinkName = link.getText();
	// commonLibrary.click_JS(link, strlinkName);
	// blnFlag = true;
	// break;
	// }
	// }
	// }
	// if (blnFlag) {
	// break;
	// }
	// }
	// if (blnFlag) {
	// report.updateTestLog("Verify link " + strlinkName + " in pod " + podName,
	// "link " + strlinkName + " in pod " + podName
	// + " is not displayed", Status.PASS);
	// } else {
	// report.updateTestLog("Verify link " + strlinkName + " in pod " + podName,
	// "link " + strlinkName + " in pod " + podName
	// + " is not displayed", Status.FAIL);
	// }
	//
	// return new ICWExcercise(scriptHelper);
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether old icw page is
	// opened in other tab or not
	// # Function Name : verifyOldICW   
	// # Author : Baswaraj
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyOldICW() {
		if (driver.getCurrentUrl().contains("icw")) {
			report.updateTestLog("Verifying Old ICW ", "Old ICW is opened in other tab and new ICW experice is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verifying Old ICW ", "Verification of old ICW is not done", Status.FAIL);
		}
		driver.close();

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickLinkInPods(String podName, String strlinkName) {

		WebElement currentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
		generalFunctions.clicklinkinPods(podName, strlinkName);
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
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : clickPodLink   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickPodLink(String podName, String strlinkName, String headerToverify) {
		// boolean blnFlag = false;
		// List<WebElement> lstButtons =
		// commonLibrary.isExist_List(UIMAP_Interactivecitationworkstation.icwPods,
		// 20);
		// if (lstButtons.size() > 0)
		// for (WebElement button : lstButtons) {
		// if (button.getText().contains(podName)) {
		// List<WebElement> lstlinks =
		// commonLibrary.isExist_List(UIMAP_Interactivecitationworkstation.lnkLinks,
		// 20);
		// for (WebElement link1 : lstlinks) {
		// if
		// (link1.getText().toLowerCase().contains(strlinkName.toLowerCase())) {
		// commonLibrary.click_JS(link1, strlinkName);
		// blnFlag = true;
		// break;
		// }
		// }
		// }
		// if (blnFlag) {
		// break;
		// }
		// }
		// if (blnFlag) {
		// report.updateTestLog("Verify link " + strlinkName + " in pod " +
		// podName, "Link " + strlinkName + " in pod " + podName +
		// " is displayed",
		// Status.PASS);
		// } else {
		// report.updateTestLog("Verify link " + strlinkName + " in pod " +
		// podName, "Link " + strlinkName + " in pod " + podName
		// + " is not displayed", Status.FAIL);
		// }
		generalFunctions.clicklinkinPods(podName, strlinkName);
		try {
			commonLibrary.sleep(15000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		WebElement headerBanner = null;
		int count = 0;
		do {
			commonLibrary.sleep(20000);
			headerBanner = commonLibrary.isExist(UIMAP_Home.header, 20);
			count++;
		} while (headerBanner == null && count < 25);

		if (headerBanner.getText().toLowerCase().contains(headerToverify.toLowerCase()))
			report.updateTestLog("Verify header " + headerToverify + " is displayed", "Header '" + headerToverify + "' is displayed", Status.PASS);

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Graph Exists
	// # Function Name : verifyGraphExists   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyGraphExists() {
		WebElement verifyGraphExists = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.graphExists, 10);
		if (verifyGraphExists != null) {
			report.updateTestLog("Verify graph exists", "Graph Exists.", Status.PASS);
			report.updateTestLog("Verify the Exercise name is not hyperlinked", "Exercise name is not hyperlinked.", Status.PASS);
		} else {
			report.updateTestLog("Verify graph exists", "Graph not exists.", Status.FAIL);
			report.updateTestLog("Verify the Exercise name is not hyperlinked", "Exercise name is hyperlinked.", Status.FAIL);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Tray
	// # Function Name : verifyDeliveryTray   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyDeliveryTray() {
		WebElement verifyDeliveryTray = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.deliveryTray, 10);
		if (verifyDeliveryTray != null)
			report.updateTestLog("Verify Delivery Tray", "Delivery Tray Exists.", Status.PASS);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the completed exercise or not
	// started exercise
	// # Function Name : clickCompletedOrNotStartedExercise
	// # Author : Seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickCompletedOrNotStartedExercise(String podName, String status) {
		boolean flag = false;
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		for (WebElement button : lstButtons) {
			if (button.getText().contains(podName)) {
				List<WebElement> rows = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.rows, 20);
				switch (status) {
				case "completed": {
					for (int i = 0; i < rows.size(); i++) {
						WebElement status1 = commonLibrary.isExist(rows.get(i), UIMAP_Interactivecitationworkstation.linkStatus, 20);
						if (status1.getText().toLowerCase().contains("completed")) {
							WebElement links = commonLibrary.isExist(rows.get(i), UIMAP_Interactivecitationworkstation.lnkLinks, 20);
							commonLibrary.clickLinkWithWebElementWithWait(links, links.getText());
							flag = true;
							break;
						}
					}
					break;
				}
				case "notstarted": {
					for (int i = 0; i < rows.size(); i++) {
						WebElement status1 = commonLibrary.isExist(rows.get(i), UIMAP_Interactivecitationworkstation.linkStatus, 20);
						if (status1.getText().toLowerCase().contains("not started")) {
							WebElement links = commonLibrary.isExist(rows.get(i), UIMAP_Interactivecitationworkstation.lnkLinks, 20);
							commonLibrary.clickLinkWithWebElementWithWait(links, links.getText());
							flag = true;
							break;
						}
					}
					break;
				}
				}
				if (flag)
					break;

			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view completion
	// # Function Name : clickViewCompletion
	// # Author : Seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickViewCompletion() {

		WebElement viewcomple = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.viewComp, 20);
		commonLibrary.clickJS(viewcomple, "View Completion certificate");

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create folder and save
	// # Function Name : createFolderAndSave     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation createFolderAndSave(String folderName, boolean banner, String strTCName) {
		generalFunctions.createFolderAndSave(folderName, banner, strTCName);
		return new Interactivecitationworkstation(scriptHelper);
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
	// # Function Description : Function to select tab and get status in Status
	// Report Pod
	// # Function Name : selectTabAndGetStatus     
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation selectTabAndGetStatus(String term, String citationManual, String exercise) {
		generalFunctions.selectTabAndGetStatus(term, citationManual, exercise);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Tray Icons
	// # Function Name : verifyDeliveryTrayIcons   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyDeliveryTrayIcons() {
		WebElement verifyDeliveryTray = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.deliveryTrayIconParent, 10);
		if (verifyDeliveryTray != null) {
			report.updateTestLog("Verify Delivery Tray", "Delivery Tray Exists.", Status.PASS);

			WebElement AddFolder = commonLibrary.isExist(verifyDeliveryTray, UIMAP_Document.btnAddFolder, 20);
			if (AddFolder != null)
				report.updateTestLog("Verify Delivery icons", " Add to folder icon with drop down is displayed", Status.PASS);

			WebElement DeliveryIcon = commonLibrary.isExist(verifyDeliveryTray, UIMAP_Document.lnkDeliveryDownload, 20);
			if (DeliveryIcon != null)
				report.updateTestLog("Verify Delivery icons", "Download icon is displayed", Status.PASS);

			WebElement EmailIcon = commonLibrary.isExist(verifyDeliveryTray, UIMAP_Document.btnEmail, 20);
			if (EmailIcon != null)
				report.updateTestLog("Verify Delivery icons", "Email icon is displayed", Status.PASS);

			WebElement PrintIcon = commonLibrary.isExist(verifyDeliveryTray, UIMAP_Document.lnkPrint, 20);
			if (PrintIcon != null)
				report.updateTestLog("Verify Delivery icons", "Print icon is displayed", Status.PASS);

		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Content
	// # Function Name : verifyContent   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyContent(String contentsToVerify) {
		String[] contentsToVerifyEach = contentsToVerify.split(";");
		int count = 0;
		WebElement studentProgressTable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.studentProgressTable, 10);
		if (studentProgressTable != null) {
			List<WebElement> thTags = commonLibrary.isExistList(studentProgressTable, UIMAP_Interactivecitationworkstation.bluebookcolheader, 20);
			for (int i = 0; i < contentsToVerifyEach.length; i++) {
				for (int j = 0; j < thTags.size(); j++) {
					if (thTags.get(j).getText().toLowerCase().contains(contentsToVerifyEach[i].toLowerCase()))
						count++;
				}
			}
		}
		if (contentsToVerifyEach.length == count)
			report.updateTestLog("Verify " + contentsToVerify, "Expected " + contentsToVerify + " is displayed", Status.PASS);

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Sortable
	// # Function Name : verifySortable   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifySortable() {
		String headerName = "Sort by";
		WebElement verifySortable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.verifySortable, 10);
		if (verifySortable != null) {
			WebElement verifyHeader = commonLibrary.isExistNegative(verifySortable, UIMAP_Interactivecitationworkstation.headerCheck, 10);
			if (verifyHeader.getText().toLowerCase().contains(headerName.toLowerCase())) {
				WebElement verifySortableDd = commonLibrary.isExistNegative(verifySortable, UIMAP_Interactivecitationworkstation.verifySortableDd, 10);
				if (verifySortableDd != null)
					report.updateTestLog("Verify sortable dropdown appears", "Sortable dropdown appears", Status.PASS);
			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Actions Column
	// # Function Name : verifyActionsColumn   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyActionsColumn() {
		WebElement studentProgressTable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.studentProgressTable, 10);
		WebElement verifyActionsColumn = commonLibrary.isExistNegative(studentProgressTable, UIMAP_Interactivecitationworkstation.verifyActionsColumn, 10);
		if (verifyActionsColumn != null)
			report.updateTestLog("Verify actions column", "Student actions displayed", Status.PASS);
		return new Interactivecitationworkstation(scriptHelper);
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
	// # Function Description : Function to verify Graph Exists
	// # Function Name : verifyGraphExists   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyGraphNotExists() {
		WebElement verifyGraphExists = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.graphExists, 2);
		if (verifyGraphExists != null) {
			report.updateTestLog("Verify graph exists", "Graph Exists.", Status.FAIL);
		} else {
			report.updateTestLog("Verify graph doesnot exists", "Graph is not displayed", Status.PASS);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Sortable
	// # Function Name : verifySortable   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation sortColumns(String sortColumnHeader) {
		WebElement verifySortable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.verifySortable, 10);
		if (verifySortable != null) {
			WebElement clickSortHeaderButton = commonLibrary.isExistNegative(verifySortable, UIMAP_Interactivecitationworkstation.clickSortHeaderButton, 10);
			if (clickSortHeaderButton != null) {
				commonLibrary.clickButtonParentWithWait(clickSortHeaderButton, clickSortHeaderButton.getText());
				WebElement listParent = commonLibrary.isExistNegative(verifySortable, UIMAP_Interactivecitationworkstation.listParent, 10);
				commonLibrary.selectFromList(listParent, sortColumnHeader);
				report.updateTestLog("Verify " + sortColumnHeader + " is clicked", sortColumnHeader + " is clicked", Status.PASS);
				report.updateTestLog("Verify students details are sorted by " + sortColumnHeader, "Students details are sorted", Status.PASS);
			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to assign and verify the grade for a
	// exercise
	// # Function Name : assignGrade     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus assignGrade(String exerName, String grade) {
		boolean flag = false;
		String exerciseName = "";
		WebElement tableOld, tableNew;
		;
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
		if (table == null)
			table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 20);
		tableOld = table;
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			WebElement exercisename = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.exercisename, 10);
			if (exercisename == null)
				exercisename = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.studentName, 10);
			WebElement link = commonLibrary.isExistNegative(exercisename, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				exerciseName = link.getText();
				if (exerciseName.contains(":"))
					exerciseName = exerciseName.split(":")[1].trim();
				// WebElement gradeData = commonLibrary.isExist_Negative(row,
				// UIMAP_Interactivecitationworkstation.gradeData, 10);
				if (exerciseName.toLowerCase().contains(exerName.toLowerCase())) {
					WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
					commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
					WebElement assignGrade = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.assignGrade, 10);
					commonLibrary.clickLinkWithWebElementWithWait(assignGrade, "Edit or Assign Grade");

					WebElement requestResetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
					if (requestResetDialog != null)
						report.updateTestLog("Verify Edit or Assign Grades dialog box displays.", "Edit or Assign Grades dialog box displays.", Status.PASS);
					else
						report.updateTestLog("Verify Edit or Assign Grades dialog box displays.", "Edit or Assign Grades dialog box does not display.", Status.FAIL);
					WebElement enterGrade = commonLibrary.isExistNegative(requestResetDialog, UIMAP_Interactivecitationworkstation.enterGrade, 10);
					commonLibrary.setDataInTextBox(enterGrade, grade, "Enter Grade");

					WebElement saveGrade = commonLibrary.isExistNegative(requestResetDialog, UIMAP_Interactivecitationworkstation.saveGrade, 10);
					commonLibrary.clickButtonParentWithWait(saveGrade, "Save");

					flag = true;
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click Request Reset for a completed exercise.", "Request Reset for a completed exercise is not clicked.", Status.FAIL);
		else {
			boolean flag1 = false;

			table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
			if (table == null)
				table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 20);

			tableNew = table;
			int count = 0;
			try {
				do {
					commonLibrary.sleep(50000);
					count++;
					tableNew = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 10);
					if (tableNew == null)
						tableNew = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 20);
				} while (tableOld.equals(tableNew) && count < 15);
			} catch (Exception e) {
				System.out.println(e.toString());
				commonLibrary.sleep(500000);
			}

			rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
			for (WebElement row : rows) {
				if (row.getText().contains(exerciseName)) {
					flag1 = true;
					WebElement gradeData = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.gradeData, 10);
					if (gradeData.getText().equalsIgnoreCase(grade)) {
						report.updateTestLog("Verify if the " + grade + " displays in the Grade Coloumn for exercise: " + exerciseName, grade + " displays in the Grade Coloumn for exercise: " + exerciseName, Status.PASS);
					} else
						report.updateTestLog("Verify if the " + grade + " displays in the Grade Coloumn for exercise: " + exerciseName, grade + " does not display in the Grade Coloumn for exercise: " + exerciseName, Status.FAIL);
					break;
				}

			}
			if (!flag1)
				report.updateTestLog("Verify if the " + grade + " displays in the Grade Coloumn for exercise: " + exerciseName, exerciseName + " is not present", Status.FAIL);
		}
		return new ICWStudentStatus(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify column is present
	// # Function Name : verifyColumnPresent     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyColumnPresent(String strColumnName) {

		boolean blnFlag = false;
		WebElement bluebooktable = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.bluebooktable, 20);
		if (bluebooktable == null)
			bluebooktable = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 20);
		List<WebElement> lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.bluebookcolheader, 20);

		for (int i = 0; i < lstcolheaders.size(); i++) {

			if (lstcolheaders.get(i).getText().contains(strColumnName)) {
				blnFlag = true;
				report.updateTestLog("Verify " + strColumnName + " displayed as column headers", strColumnName + " is displayed as column headers ", Status.PASS);
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Verify " + strColumnName + " displayed as column headers", strColumnName + " is not displayed as column headers", Status.FAIL);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a product from Not
	// Started to In Progress.
	// # Function Name : preReqNotStartToInProg     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation preReqToInProg(String podName, String linkName) {
		if (generalFunctions.verifylinkinPodStatus(podName, linkName, "In Progress")) {
			return new Interactivecitationworkstation(scriptHelper);
		} else if (generalFunctions.verifylinkinPodStatus(podName, linkName, "Completed")) {
			String student = dataTable.getData("General_Data", "StudentName");
			String citation = dataTable.getData("General_Data", "CitationManual");
			this.logout().invokeApplication().loginAnother().navigatetoICWProf().selectTab("Individual Student Report").selectStudentDetails(student, citation).resetExerciseNoVerification(linkName).logout().invokeApplication().login().navigatetoICW();
		}
		generalFunctions.clicklinkinPods(podName, linkName);
		ICWExcercise icwEx = new ICWExcercise(scriptHelper);
		icwEx.setICWAnswer("1", "Abcd").clickVerifyICWSubmitAnswer("1", true, true).clickProductlogo();
		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click view completion certificate
	// # Function Name : viewcompletion     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation viewcompletion() {
		boolean flag = false;
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			if (row.getText().contains("Completed")) {
				WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
				commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
				WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.viewcomple, 10);
				commonLibrary.clickLinkWithWebElementWithWait(reset, "View completion certificate");
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click view completion certificate for completed exercise.", "view completion certificate for a completed exercise is not clicked.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a product to
	// Completed.
	// # Function Name : preReqToCompleted     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation preReqToCompleted(String podName, String linkName) {
		// boolean flag = false;
		if (generalFunctions.verifylinkinPodStatus(podName, linkName, "Completed")) {
			return new Interactivecitationworkstation(scriptHelper);
		}
		Boolean flag = generalFunctions.verifylinkinPodStatus(podName, linkName, "Not Started");
		ICWExcercise icwExercise = this.clickLinkInPods(podName, linkName);
		if (flag) {
			icwExercise = icwExercise.setICWAnswer("1", "Abcd").clickVerifyICWSubmitAnswer("1", true, true);
		}
		icwExercise.completeExercise().clickProductlogo();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a exercise to
	// Not Started.
	// # Function Name : preReqToNotStarted     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation preReqToNotStarted(String podName, String linkName) {
		if (generalFunctions.verifylinkinPodStatus(podName, linkName, "Not Started")) {
			return new Interactivecitationworkstation(scriptHelper);
		}

		String student = dataTable.getData("General_Data", "StudentName");
		String citation = dataTable.getData("General_Data", "CitationManual");
		this.logout().invokeApplication().loginAnother().navigatetoICWProf().selectTab("Individual Student Report").selectStudentDetails(student, citation).resetExerciseNoVerification(linkName).logout().invokeApplication().login().navigatetoICW();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of attempts for each
	// question in ICW
	// # Function Name : SetNumberOfAttempts     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation setNumberOfAttempts(String number) {
		generalFunctions.setNumberOfAttempts(number);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set number of attempts for each
	// question in ICW
	// # Function Name : SetNumberOfAttempts     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation setNumberOfAttemptsVerify(String number, String number1, String heading, String inline) {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet"))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		if (setting == null || !setting.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			setting = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 100);
		}

		if (setting != null) {
			if (browsername.contains("internet"))
				commonLibrary.clickJS(setting, "Setting");
			else
				commonLibrary.clickLinkWithWebElementWithWait(setting, "Settings");
		}

		WebElement icwProfSettings = commonLibrary.isExistNegative(UIMAP_Home.icwProfSettings, 10);
		if (icwProfSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(icwProfSettings, "Interactive Citation Workstation Prof Settings");

		WebElement inputCont = commonLibrary.isExistNegative(UIMAP_Home.inputCont, 10);
		WebElement subHeader = commonLibrary.isExistNegative(inputCont, UIMAP_Home.messageHeader, 10);
		if (subHeader.getText().toLowerCase().contains(heading.toLowerCase()))
			report.updateTestLog("Verify subheader contains " + heading, "subheader contains " + heading, Status.PASS);
		else
			report.updateTestLog("Verify subheader contains " + heading, "subheader does not contain " + heading, Status.FAIL);

		WebElement header3 = commonLibrary.isExistNegative(inputCont, UIMAP_Home.header3, 10);
		if (header3.getText().toLowerCase().contains(inline.toLowerCase()))
			report.updateTestLog("Verify inline text contains " + inline, "inline text contains " + inline, Status.PASS);
		else
			report.updateTestLog("Verify inline text contains " + inline, "inline text does not contain " + inline, Status.FAIL);

		WebElement selectAttempts = commonLibrary.isExistNegative(UIMAP_Home.selectAttempts, 10);

		if (selectAttempts != null)
			report.updateTestLog("Verify Drop down with values displays to set the Maximum Number of Attempts exists.", "Drop down with values displays to set the Maximum Number of Attempts exists.", Status.PASS);
		else
			report.updateTestLog("Verify Drop down with values displays to set the Maximum Number of Attempts exists.", "Drop down with values displays to set the Maximum Number of Attempts does not exist.", Status.FAIL);

		if (commonLibrary.selectIsSelected(selectAttempts, number1)) {
			report.updateTestLog("Verify " + number1 + " is selected in Format Dropdown.", number1 + " is selected in Format Dropdown.", Status.PASS);
		} else
			report.updateTestLog("Verify " + number1 + " is selected in Format Dropdown.", number1 + " is not selected in Format Dropdown.", Status.FAIL);

		commonLibrary.selectFromListOption(selectAttempts, number);

		WebElement submit = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");

		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");

			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to share folder
	// // question in ICW
	// // # Function Name : addToFolderAndShare     
	// // # Author : Seetha
	// // # Date Created : June'15
	// //
	// #*****************************************************************************************************************************
	// public String addToFolder(String strContactName) {
	//
	// try {
	// // CLICK ON <<<ADD TO FOLDER>>>
	// commonLibrary.sleep(1000);
	// WebElement btnAddFolder = commonLibrary.isExist(
	// UIMAP_Document.btnAddFolder, 10);
	// if (btnAddFolder != null)
	//
	// commonLibrary.clickButton_Parent_WithWait(btnAddFolder,
	// "Add To Folder");
	//
	// // CLICK ON <<<CHOOSE A FOLDER>>>
	//
	// WebElement btnChooseFolder = commonLibrary.isExist(
	// UIMAP_Document.btnChooseFolder, 10);
	// if (btnChooseFolder != null)
	// commonLibrary.clickButton_Parent_WithWait(btnChooseFolder,
	// "Choose Folder");
	//
	// // CLICK ON <<<CREATE FOLDER>>>
	//
	// WebElement btnCreateFolder = commonLibrary.isExist(
	// UIMAP_Document.btnCreateFolder, 10);
	// if (btnCreateFolder != null)
	// commonLibrary.clickButton_Parent_WithWait(btnCreateFolder,
	// "Create Folder");
	//
	// // ENTER <<<SMOKE TEST>>> IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT
	// // BOX
	// String strFolderName = "SmokeTest"
	// + commonLibrary.getCurrentDateTime();
	// WebElement txtFolderName = commonLibrary.isExist(
	// UIMAP_Document.txtFolderName, 10);
	// if (txtFolderName != null)
	// commonLibrary.SetDataInTextBox(
	// UIMAP_Document.txtFolderName, strFolderName);
	// // CLICK ON <<<CREATE>>>
	// WebElement btnCreateNewFolder = commonLibrary.isExist(
	// UIMAP_Document.btnCreateNewFolder, 10);
	// if (btnCreateNewFolder != null)
	// commonLibrary.clickButton_Parent_WithWait(btnCreateNewFolder,
	// "CREATE");
	// commonLibrary.sleep(3000);
	// int i = 0;
	// // Click <Share with Others> tab in add to folder pop up
	// generalFunctions.SelectTabs_AddToFolderWindow("Share With Others");
	//
	// // ENTER <<<B>>> IN THE TEXT BOX RECENTLY USED NAMES WILL DISPLAY
	// // ALLOWING THE USER TO SELECT ONE
	//
	// if (browsername.contains("internet")) {
	//
	// WebElement txtShareContacts = commonLibrary.isExist(
	// UIMAP_Document.txtShareContacts, 10);
	// if (txtShareContacts != null)
	// commonLibrary
	// .SetDataInTextBox(
	// UIMAP_Document.txtShareContacts,
	// strContactName);
	// commonLibrary.sleep(2000);
	//
	// // SELECT THE <<<BECKER>>>
	// boolean blnFlag = false;
	// // try
	// // {
	// //
	// commonLibrary.sleep(3000);
	// WebElement cboSharingList = commonLibrary.isExist(
	// UIMAP_Document.cboSharingList, 10);
	// List<WebElement> li = commonLibrary.isExist_List(
	// cboSharingList, By.tagName("li"), 20);
	// for (WebElement item : li) {
	// if (item.getText().trim().equalsIgnoreCase(strContactName)) {
	// commonLibrary.Click_JS(item);
	// blnFlag = true;
	// break;
	// }
	// }
	// if (!(blnFlag)) {
	// WebElement txtShareContacts1 = commonLibrary.isExist(
	// UIMAP_Document.txtShareContacts, 10);
	// for (int j = 1; j <= 3; j++) {
	// Actions action = new Actions(driver);
	// action.sendKeys(txtShareContacts1, Keys.BACK_SPACE)
	// .perform();
	// action.sendKeys(txtShareContacts1, Keys.BACK_SPACE)
	// .perform();
	// action.sendKeys(
	// txtShareContacts1,
	// strContactName.substring(strContactName
	// .length() - 2)).perform();
	// commonLibrary.sleep(4000);
	// WebElement cboSharingList1 = commonLibrary.isExist(
	// UIMAP_Document.cboSharingList, 10);
	// List<WebElement> li1 = commonLibrary.isExist_List(
	// cboSharingList1, By.tagName("li"), 20);
	// for (WebElement item : li1) {
	// if (item.getText().trim()
	// .equalsIgnoreCase(strContactName)) {
	// commonLibrary.Click_JS(item);
	// blnFlag = true;
	// break;
	// }
	// }
	// if (blnFlag)
	// break;
	// }
	// }
	//
	// } else
	// generalFunctions.ShareContact_SelectContact(strContactName);
	//
	// // Click the "Add" button
	// WebElement btnAddToContact = commonLibrary.isExist(
	// UIMAP_Document.btnAddToContact, 10);
	// if (btnAddToContact != null) {
	// if (browsername.contains("internet"))
	// commonLibrary.Click_JS(btnAddToContact, "Add");
	// else
	// commonLibrary.clickButton_Parent_WithWait(btnAddToContact,
	// "Add");
	// }
	//
	// // CLICK ON <<<SAVE>>>
	// WebElement btnDocumentSave = commonLibrary.isExist(
	// UIMAP_Document.btnDocumentSave, 10);
	// if (btnDocumentSave != null) {
	// if (browsername.contains("internet"))
	// commonLibrary.Click_JS(btnDocumentSave, "SAVE");
	// else
	// commonLibrary.clickButton_Parent_WithWait(btnDocumentSave,
	// "SAVE");
	// }
	//
	// return strFolderName;
	// } catch (Exception e) {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception", e.toString());
	// }
	//
	// }
	//
	//
	// }
	// #*****************************************************************************************************************************
	// # Function Description : Function to share folder
	// question in ICW
	// # Function Name : addToFolderAndShare     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation addToFolder(String strContactName, String strFolderName) {

		try {
			// CLICK ON <<<ADD TO FOLDER>>>
			commonLibrary.sleep(1000);
			WebElement btnAddFolder = commonLibrary.isExist(UIMAP_Document.btnAddFolder, 10);
			if (btnAddFolder != null)

				commonLibrary.clickButtonParentWithWait(btnAddFolder, "Add To Folder");

			WebElement chooseFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.chooseFolder, 10);
			if (chooseFolder == null) {
				btnAddFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.addFolder, 10);
				if (btnAddFolder != null)
					commonLibrary.clickButtonParentWithWait(btnAddFolder, "Add To Folder");
				chooseFolder = commonLibrary.isExist(UIMAP_CounselBenchmarking.chooseFolder, 10);
			}

			// CLICK ON <<<CHOOSE A FOLDER>>>

			WebElement btnChooseFolder = commonLibrary.isExist(UIMAP_Document.btnChooseFolder, 10);
			if (btnChooseFolder != null)
				commonLibrary.clickButtonParentWithWait(btnChooseFolder, "Choose Folder");

			// CLICK ON <<<CREATE FOLDER>>>

			WebElement btnCreateFolder = commonLibrary.isExist(UIMAP_Document.btnCreateFolder, 10);
			if (btnCreateFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateFolder, "Create Folder");

			// ENTER <<<SMOKE TEST>>> IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT
			// BOX

			WebElement txtFolderName = commonLibrary.isExist(UIMAP_Document.txtFolderName, 10);
			if (txtFolderName != null)
				commonLibrary.setDataInTextBox(UIMAP_Document.txtFolderName, strFolderName);
			// CLICK ON <<<CREATE>>>
			WebElement btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			if (btnCreateNewFolder != null)
				commonLibrary.clickButtonParentWithWait(btnCreateNewFolder, "CREATE");
			commonLibrary.sleep(30000);

			btnCreateNewFolder = commonLibrary.isExist(UIMAP_Document.btnCreateNewFolder, 10);
			WebElement createNew = commonLibrary.isExistNegative(UIMAP_Document.btnCreateNewFolder, 10);
			int count = 0;
			try {
				do {
					commonLibrary.sleep(7000000);
					createNew = commonLibrary.isExistNegative(UIMAP_Document.btnCreateNewFolder, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (btnCreateNewFolder.equals(createNew) && count < 80);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// Click <Share with Others> tab in add to folder pop up
			generalFunctions.selectTabsAddToFolderWindow("Share With Others");

			// ENTER <<<B>>> IN THE TEXT BOX RECENTLY USED NAMES WILL DISPLAY
			// ALLOWING THE USER TO SELECT ONE

			if (browsername.contains("internet")) {

				WebElement txtShareContacts = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
				if (txtShareContacts != null)
					commonLibrary.setDataInTextBox(UIMAP_Document.txtShareContacts, strContactName);
				commonLibrary.sleep(50000);

				// SELECT THE <<<BECKER>>>
				boolean blnFlag = false;
				// try
				// {
				//
				try {
					commonLibrary.sleep(30000);
					WebElement cboSharingList = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
					List<WebElement> li = commonLibrary.isExistList(cboSharingList, By.tagName("li"), 20);
					for (WebElement item : li) {
						if (item.getText().trim().equalsIgnoreCase(strContactName)) {
							commonLibrary.clickJS(item);

							break;
						}
					}
				} catch (Exception e) {
					try {
						System.out.println(e.toString());
						WebElement txtShareContacts1 = commonLibrary.isExist(UIMAP_Document.txtShareContacts, 10);
						for (int j = 1; j <= 3; j++) {
							Actions action = new Actions(driver);
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
							action.sendKeys(txtShareContacts1, Keys.BACK_SPACE).perform();
							action.sendKeys(txtShareContacts1, strContactName.substring(strContactName.length() - 2)).perform();
							commonLibrary.sleep(4000);
							WebElement cboSharingList1 = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
							List<WebElement> li1 = commonLibrary.isExistList(cboSharingList1, By.tagName("li"), 20);
							for (WebElement item : li1) {
								if (item.getText().trim().equalsIgnoreCase(strContactName)) {
									commonLibrary.clickJS(item);
									blnFlag = true;
									break;
								}
							}
							if (blnFlag)
								break;
						}
					} catch (Exception e1) {
						System.out.println(e1.toString());
						commonLibrary.sleep(30000);
						WebElement cboSharingList1 = commonLibrary.isExist(UIMAP_Document.cboSharingList, 10);
						List<WebElement> li1 = commonLibrary.isExistList(cboSharingList1, By.tagName("li"), 20);
						for (WebElement item : li1) {
							if (item.getText().trim().equalsIgnoreCase(strContactName)) {
								commonLibrary.clickJS(item);
								blnFlag = true;
								break;
							}
						}
					}
				}

			} else
				generalFunctions.shareContactSelectContact(strContactName);

			// Click the "Add" button
			WebElement btnAddToContact = commonLibrary.isExist(UIMAP_Document.btnAddToContact, 10);
			if (btnAddToContact != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnAddToContact, "Add");
				else
					commonLibrary.clickButtonParentWithWait(btnAddToContact, "Add");
			}

			// CLICK ON <<<SAVE>>>
			WebElement btnDocumentSave = commonLibrary.isExist(UIMAP_Document.btnDocumentSave, 10);
			if (btnDocumentSave != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");
				else
					commonLibrary.clickButtonParentWithWait(btnDocumentSave, "SAVE");
			}

			WebElement saveNew = commonLibrary.isExistNegative(UIMAP_Document.btnDocumentSave, 10);
			count = 0;
			try {
				do {
					commonLibrary.sleep(700000);
					saveNew = commonLibrary.isExistNegative(UIMAP_Document.btnDocumentSave, 10);
					count++;
					System.out.println("Waiting" + count);
				} while (btnDocumentSave == saveNew && count < 40);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			return new Interactivecitationworkstation(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click links in ICW pods
	// # Function Name : click_linkinPods   
	// # Author : revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clicklinkinPods2(String podName, String strlinkName) {
		generalFunctions.clicklinkinPods(podName, strlinkName);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to send email with some verifications
	// # Function Name : sendEmail   
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation sendEmail(String Email) {
		// WebElement btnEmail =
		// commonLibrary.isExist(UIMAP_SearchResult.btnEmail);
		// commonLibrary.clickButton_Log_SmallWait(btnEmail, "E-mail");

		generalFunctions.clickDeliverySelectOption("delivery", "email");

		WebElement format = commonLibrary.isExistNegative(UIMAP_SearchResult.emailFormat, 10);
		if (format != null) {
			report.updateTestLog("Verify whether document type -PDF as default  is present", "Document type is PDF as default", Status.PASS);
		} else {
			report.updateTestLog("Verify whether document type -PDF as default  is present", "Document type is not PDF as default", Status.FAIL);
		}

		WebElement txtEmail = commonLibrary.isExistNegative(UIMAP_SearchResult.txtEmail, 10);
		if (txtEmail != null) {
			report.updateTestLog("Verify email pop up is displayed", "Email pop up is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify email pop up is displayed", "Email pop up is not displayed", Status.FAIL);
		}
		commonLibrary.setDataInTextBox(txtEmail, Email, "To");

		WebElement coverPage = commonLibrary.isExistNegative(UIMAP_SearchResult.chkbox, 2);
		if (coverPage != null) {
			report.updateTestLog("Verify cover page checkbox is not displayed", "Cover page checkbox is displayed", Status.FAIL);
		} else {
			report.updateTestLog("Verify cover page checkbox is not displayed", "Cover page checkbox is not displayed", Status.PASS);
		}

		WebElement btnSendEmail = commonLibrary.isExistNegative(UIMAP_SearchResult.btnSendEmail, 10);
		commonLibrary.clickButtonLogSmallWait(btnSendEmail, "Send Email");

		WebElement eltEmailPopup = commonLibrary.isExistNegative(UIMAP_SearchResult.eltEmailPopup, 10);
		if (eltEmailPopup == null)
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box closes.", Status.PASS);
		else
			report.updateTestLog("Verify Email Delivery Dialog box closes.", "Email Delivery Dialog box does not close.", Status.FAIL);
		commonLibrary.switchToWindow("deliverysecondarywindow");
		WebElement txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
		if (txtPopUpHeader != null)
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up opens.", Status.PASS);
		else
			report.updateTestLog("Verify Processing msg pop up opens.", "Processing msg pop up does not open.", Status.FAIL);
		int i = 0;
		while (txtPopUpHeader.getText().contains("Processing")) {
			i++;
			try {
				commonLibrary.sleep(70000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
			txtPopUpHeader = commonLibrary.isExistNegative(UIMAP_SearchResult.txtPopUpHeader, 10);
			if (i > 30)
				break;
		}
		boolean blnFlag = false;
		if (txtPopUpHeader.getText().contains("Complete"))
			blnFlag = true;
		else
			blnFlag = false;

		if (blnFlag)
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has been sent to: " + Email + "", Status.PASS);
		else
			report.updateTestLog("Verify email has been sent to " + Email + "", "Email has not been sent to: " + Email + "", Status.FAIL);

		driver.close();
		commonLibrary.switchToWindow("icw");
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to history
	// # Function Name : navigateToHistory   
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public History navigateToHistory(String option) {
		generalFunctions.navigateToHistoryFooterLink(option);
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify student report table
	// # Function Name : verifyStudentReport   
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyStudentReport() {
		WebElement studentReport = commonLibrary.isExist(UIMAP_SearchResult.stuReport, 10);
		if (studentReport != null) {
			report.updateTestLog("Verify Status page of<BlueBook Exercise> displays with delivery tab", "Status page of BlueBook Exercise is displayed with delivery tab", Status.PASS);
		} else {
			report.updateTestLog("Verify Status page of<BlueBook Exercise> displays with delivery tab", "Status page of BlueBook Exercise is not displayed with delivery tab", Status.FAIL);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Grade For First Exercise
	// # Function Name : verifyGradeForFirstExercise   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public String verifyGradeForFirstExercise() {
		String getGrade = null;
		WebElement gradeParent = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.gradeParent, 10);
		if (gradeParent != null) {
			WebElement verifyGradeForFirstExercise = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.verifyGradeForFirstExercise, 10);
			if (verifyGradeForFirstExercise != null) {
				WebElement tagNameP = commonLibrary.isExistNegative(verifyGradeForFirstExercise, UIMAP_Interactivecitationworkstation.p, 10);
				getGrade = tagNameP.getText();
				report.updateTestLog("Verify grade for first exercise", "Grade for first exercise is " + getGrade, Status.DONE);
			}
		}
		return getGrade;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty In HomePage
	// # Function Name : verifyfacultyInHomePage   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyFacultyInHomePage() {
		String settingsPod = "Settings";
		List<WebElement> lstPods = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 10);
		if (lstPods.size() > 0) {
			for (WebElement pods : lstPods) {
				if (pods.getText().toLowerCase().contains(settingsPod.toLowerCase())) {
					List<WebElement> tagNameP = commonLibrary.isExistList(pods, UIMAP_Interactivecitationworkstation.p, 10);
					for (int i = 0; i < tagNameP.size(); i++) {
						if (tagNameP.get(i).getText().toLowerCase() != null)
							report.updateTestLog("Verify faculty in homepage", "Faculty exist in homepage " + tagNameP.get(i).getText(), Status.PASS);
					}
				}
			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty In HomePage
	// # Function Name : verifyfacultyInHomePage   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyFacultyInHomePage(String facultyToVerify) {
		String[] facultyToVerifyEach = facultyToVerify.split(";");
		int count = 0;
		int countToVerify = facultyToVerifyEach.length;
		String settingsPod = "Settings";
		List<WebElement> lstPods = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 10);
		if (lstPods.size() > 0) {
			for (WebElement pods : lstPods) {
				if (pods.getText().toLowerCase().contains(settingsPod.toLowerCase())) {
					List<WebElement> tagNameP = commonLibrary.isExistList(pods, UIMAP_Interactivecitationworkstation.p, 10);
					for (int i = 0; i < tagNameP.size(); i++) {
						for (int fEach = 0; fEach < facultyToVerifyEach.length; fEach++) {
							if (tagNameP.get(i).getText().toLowerCase().contains(facultyToVerifyEach[fEach].toLowerCase()))
								count++;
						}
					}
				}
			}
		}
		if (count == countToVerify)
			report.updateTestLog("Verify faculty changed in homepage", "Faculty changed in homepage ", Status.PASS);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty
	// # Function Name : verifyfaculty   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyfaculty(String usernameToVerify) {
		WebElement verifyFacultyParent = commonLibrary.isExist(UIMAP_Settings.verifyFacultyParent, 10);
		if (verifyFacultyParent != null) {
			WebElement verifyFaculty = commonLibrary.isExist(verifyFacultyParent, UIMAP_Settings.verifyFaculty, 10);
			if (verifyFaculty.getText().toLowerCase().contains(usernameToVerify.toLowerCase()))
				report.updateTestLog("verify if faculty has been changed.", "Faculty changed.", Status.PASS);
		}

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select options from action
	// # Function Name : selectLinkUnderActions   
	// # Author : Seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickActionSelectValue(String action) {
		generalFunctions.clickActionSelectValue(action);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty
	// # Function Name : verifyfaculty   
	// # Author : Seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyAndRemoveAssociation(boolean banner, String strTCName) {
		WebElement remove = commonLibrary.isExist(UIMAP_Settings.removeAsso, 10);
		if (remove != null) {
			report.updateTestLog("Verify whether remove association popup is displayed", "Remove association popup is displayed", Status.PASS);
			commonLibrary.clickButtonParentWithWaitJS(remove, "Remove Association");
		} else {
			report.updateTestLog("Verify whether remove association popup is displayed", "Remove association popup is not displayed", Status.FAIL);
		}
		if (banner) {
			generalFunctions.takeScreenShot(strTCName, "Verify banner message as Student Name is no longer associated to you with Dismiss button is displayed ");
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty
	// # Function Name : verifyfaculty   
	// # Author : Seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyAndRemoveAllAssociation(boolean banner, String strTCName) {
		WebElement remove = commonLibrary.isExist(UIMAP_Settings.removeStu, 10);
		if (remove != null) {
			report.updateTestLog("Verify whether remove association popup is displayed", "Remove association popup is displayed", Status.PASS);
			commonLibrary.clickButtonParentWithWaitJS(remove, "Remove All Association");
		} else {
			report.updateTestLog("Verify whether remove all association popup is displayed", "Remove all association popup is not displayed", Status.FAIL);
		}
		if (banner) {
			generalFunctions.takeScreenShot(strTCName, "Verify banner message as N  Students association has been removed with Dismiss button is displayed ");
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a product to
	// Completed.
	// # Function Name : preReqToCompletedForce     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn preReqToCompletedForce(String podName, String linkName) {
		// boolean flag = false;
		if (generalFunctions.verifylinkinPodStatus(podName, linkName, "Completed")) {

			String student = dataTable.getData("General_Data", "StudentName");
			String citation = dataTable.getData("General_Data", "CitationManual");
			this.logout().invokeApplication().loginAnother().navigatetoICWProf().selectTab("Individual Student Report").selectStudentDetails(student, citation).resetExerciseNoVerification(linkName).logout().invokeApplication().login().navigatetoICW();
		}
		boolean flag = generalFunctions.verifylinkinPodStatus(podName, linkName, "Not Started");
		ICWExcercise icwExercise = this.clickLinkInPods(podName, linkName);
		if (flag) {
			icwExercise = icwExercise.setICWAnswer("1", "Answer1").clickVerifyICWSubmitAnswer("1", true, true);
		}
		return icwExercise.completeExercise().logout();

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a product to
	// Completed.
	// # Function Name : preReqToCompletedForce     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation resetForAllStudents(String para1) {
		WebElement headerAction = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.headerAction, 10);
		if (headerAction != null)
			commonLibrary.clickButtonLogSmallWait(headerAction, "Action");

		// WebElement actionPopup =
		// commonLibrary.isExist_Negative(UIMAP_Interactivecitationworkstation.actionPopup,
		// 10);
		WebElement resetForAllStu = commonLibrary.isExistNegative(
		/*
		 * actionPopup,
		 */UIMAP_Interactivecitationworkstation.resetForAllStu, 10);
		commonLibrary.clickButtonParentWithWait(resetForAllStu, "Reset for all students");

		WebElement confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer, 3);
		int count = 0;
		do {
			commonLibrary.sleep(200000);
			confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer, 3);
			count++;
		} while (confirmReset == null && count < 20);

		verifyResetAllDialogBox(para1);

		confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer, 10);
		commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");

		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the dialog box after
	// resetting an execise for all students
	// # Function Name : verifyResetAllDialogBox     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWStudentStatus verifyResetAllDialogBox(String para1) {
		WebElement resetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (resetDialog.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog contains " + para1, Status.PASS);
		} else
			report.updateTestLog("Verify if Request Reset dialog contains " + para1, "Request Reset dialog does not contains " + para1, Status.FAIL);

		List<WebElement> buttons = commonLibrary.isExistList(resetDialog, UIMAP_Interactivecitationworkstation.input, 10);
		boolean isSubmit = false, isCancel = false;
		for (WebElement item : buttons) {
			if (item.getAttribute("value").toLowerCase().contains("reset"))
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
					WebElement exercisename = commonLibrary.isExistNegative(row1, UIMAP_Interactivecitationworkstation.exercisename, 10);
					if (exercisename.getText().toLowerCase().contains(exerciseName.toLowerCase()))
						report.updateTestLog("Verify if the exercise " + exerciseName + " is displayed in Exercise Name Coloumn", "The exercise " + exerciseName + " is displayed in Exercise Name Coloumn", Status.PASS);
					else
						report.updateTestLog("Verify if the exercise " + exerciseName + " is displayed in Exercise Name Coloumn", "The exercise " + exerciseName + " is not displayed in Exercise Name Coloumn", Status.FAIL);
					Date currDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
					if (exercisename.getText().contains(sdf.format(currDate)) && exercisename.getText().toLowerCase().contains("Exercise Reset on".toLowerCase())) {
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
	// # Function Description : Function to verify Sortable
	// # Function Name : verifySortable   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation sortColumnsVerifyDefault(String sortColumnHeader) {
		WebElement verifySortable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.verifySortable, 10);
		if (verifySortable != null)
			report.updateTestLog("Verify students details are sorted by " + sortColumnHeader, "Students details are sorted in " + sortColumnHeader, Status.PASS);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Notifications
	// # Function Name : navigateTofolders     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Notifications navigateToNotifications() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement lnkNotifications = commonLibrary.isExist(UIMAP_Home.lnkNotifications, 10);
			if (lnkNotifications != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkNotifications, "Notifications");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Notifications(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether a particular exercise
	// has been Reset.
	// # Function Name : verifyExerciseResetStuHome     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyExerciseResetStuHome(String podName, String exerciseName) {

		if (generalFunctions.verifylinkinPodStatus(podName, exerciseName, "Not Started")) {
			report.updateTestLog("Verify if the exercise " + exerciseName + " is reset ", "The exercise " + exerciseName + " is reset", Status.PASS);
		} else
			report.updateTestLog("Verify if the exercise " + exerciseName + " is reset ", "The exercise " + exerciseName + " is not reset", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);

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
	// # Function Description : Function to navigate to Help and verify content
	// # Function Name : navigateToHelpAndVerifyContent     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation navigateToHelpAndVerifyContent(String loggedInAs) {
		String stu = "student";
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement lnkNotifications = commonLibrary.isExist(UIMAP_Home.lnkHelp, 10);
			if (lnkNotifications != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkNotifications, "Help");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		commonLibrary.switchToWindow("cert-help");
		WebElement headerText = commonLibrary.isExist(UIMAP_Home.txtHeaderNewWindow1, 10);
		if (loggedInAs.equalsIgnoreCase(stu)) {
			if (headerText.getText().toLowerCase().contains(stu)) {
				report.updateTestLog("Verify whether Interactive Citation Workstation for students is displayed", "Interactive Citation Workstation for students is displayed", Status.PASS);
				report.updateTestLog("Verify whether basic instructions and Faqs regarding ICW Student Features displays in the Help page", "Basic instructions and Faqs regarding ICW Student Features displays in the Help page", Status.PASS);
			}
		} else {
			if (headerText.getText().toLowerCase().contains("lexis")) {
				report.updateTestLog("Verify whether Interactive Citation Workstation for professor is displayed", "Interactive Citation Workstation for professor is displayed", Status.PASS);
				report.updateTestLog("Verify whether ICW Help Page pop up displays with basic information and FAQs about Professor/Faculty features", "ICW Help Page pop up displays with basic information and FAQs about Professor/Faculty features", Status.PASS);
			}
		}
		driver.close();
		commonLibrary.switchToWindow("icw");
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
		if (btnMore != null)
			commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Exercise Status
	// # Function Name : verifyExerciseStatus     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public int verifyExerciseStatus(String podName, String exerName) {
		String statusNotStarted = "Not Started";
		String statusInProgress = "In Progress";
		String statusCompleted = "Completed";
		String bluBExe = "Bluebook Exercise";
		List<WebElement> lstButtons = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.icwPods, 20);
		if (lstButtons.size() > 0) {
			for (WebElement button : lstButtons) {
				if (button.getText().toLowerCase().contains(bluBExe.toLowerCase())) {
					List<WebElement> lstlinks = commonLibrary.isExistList(button, UIMAP_Interactivecitationworkstation.lnkLinks, 20);
					for (WebElement links : lstlinks) {
						String name = "";
						if (links.getText().contains(":"))
							name = links.getText().split(":")[1].trim();
						else
							name = links.getText();
						if (exerName.toLowerCase().contains(name.toLowerCase())) {
							WebElement row = commonLibrary.getParentElement(links);
							row = commonLibrary.getParentElement(row);
							if (row.getText().toLowerCase().contains(statusNotStarted.toLowerCase())) {
								report.updateTestLog("Verify Exercise Status for " + exerName + " in pod " + podName, "Exercise Status for " + exerName + " in pod " + podName + " is " + statusNotStarted, Status.PASS);
								return 1;
							} else if (row.getText().toLowerCase().contains(statusInProgress.toLowerCase())) {
								report.updateTestLog("Verify Exercise Status for " + exerName + " in pod " + podName, "Exercise Status for " + exerName + " in pod " + podName + " is " + statusInProgress, Status.PASS);
								return 2;
							} else if (row.getText().toLowerCase().contains(statusCompleted.toLowerCase())) {
								report.updateTestLog("Verify Exercise Status for " + exerName + " in pod " + podName, "Exercise Status for " + exerName + " in pod " + podName + " is " + statusCompleted, Status.PASS);
								return 3;
							}
						}
					}
				}
			}
		}
		return 0;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter the answer in
	// icwcitationexercise page
	// # Function Name : setICWAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation setICWAnswer(String problemNo, String answer) {
		String id = "problem-" + problemNo + "-textarea_ifr";
		driver.switchTo().frame(driver.findElement(By.id(id)));
		WebElement textBox = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.textBox, 10);
		if (textBox != null)
			commonLibrary.setDataInTextBox(textBox, answer, "Answer text box");
		else
			report.updateTestLog("verify if Answer box is displayed", "Answer box is NOT displayed", Status.FAIL);

		driver.switchTo().defaultContent();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the submit answer button in
	// icwcitationexercise page
	// # Function Name : ICWSubmitAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickVerifyICWSubmitAnswer(String problemNo, boolean click, boolean presence) {
		String submitid = "problem-" + problemNo + "-textarea-submit";
		WebElement submit = commonLibrary.isExist(By.id(submitid), 10);
		if (submit != null) {
			if (click)
				commonLibrary.clickJS(submit, "Submit Answer");
			else {
				if (presence)
					report.updateTestLog("verify if Submit Answer button is displayed", "Submit Answer button is displayed", Status.PASS);
			}

		} else if (!presence)
			report.updateTestLog("verify if Submit Answer button is NOT displayed", "Submit Answer button is NOT displayed", Status.PASS);
		else
			report.updateTestLog("verify if Submit Answer button is displayed", "Submit Answer button is NOT displayed", Status.FAIL);

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to complete Exercise
	// # Function Name : completeExercise
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation completeExercise() {
		WebElement compEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.completeExercise, 10);
		if (compEx != null) {

			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWait(compEx, compEx.getText());
			else
				commonLibrary.clickLinkWithWebElementWithWait(compEx, compEx.getText());

		}

		WebElement conCompEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmCompleteExer, 10);
		if (conCompEx != null) {
			report.updateTestLog("Verify if Complete Exercise pop up displays withInline message, Instructor details,<Complete Exercise> and <Cancel>buttons.", "Complete Exercise pop up displays", Status.PASS);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(conCompEx, conCompEx.getText());
			else
				commonLibrary.clickLinkWithWebElementWithWait(conCompEx, conCompEx.getText());

		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab in Status Report Pod
	// # Function Name : selectTabISR     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation selectTabISR(String term) {
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 10);
		commonLibrary.selectFromListButton(tabs, term);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select Student details
	// # Function Name : selectStudentDetails     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation selectStudentDetails(String student, String citation) {
		WebElement stud = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.selectStudent, 10);
		commonLibrary.selectFromListOption(stud, student);

		WebElement cit = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.selectCitation, 10);
		commonLibrary.selectFromListOption(cit, citation);

		WebElement getReport = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.getStatusReportStud, 10);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(getReport, "Get Status Report for Student");
		else
			commonLibrary.clickLinkWithWebElementWithWait(getReport, "Get Status Report for Student");

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise
	// # Function Name : resetExercise     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation resetExerciseNoVerification(String exerciseName) {
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
				if (exerciseName.toLowerCase().contains(exeName.toLowerCase())) {
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
		if (!flag)
			report.updateTestLog("Reset the exercise " + exerciseName, "The exercise " + exerciseName + " can not be reset", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Request Reset
	// # Function Name : clickRequestReset
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickRequestReset() {
		WebElement requestReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestReset, 10);
		commonLibrary.clickLinkWithWebElementWithWait(requestReset, requestReset.getText());

		commonLibrary.sleep(3000);

		WebElement submitReq = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.submitReq, 10);
		commonLibrary.clickButtonParentWithWait(submitReq, submitReq.getText());

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button
	// # Function Name : ClickBrowserBack
	// # Author : Seetha
	// # Date Created : Mar 16 2015
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new Interactivecitationworkstation(scriptHelper);
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
	// # Function Description : Function to click view completion certificate
	// # Function Name : viewcompletion1     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation viewcompletion1(String exerName) {
		boolean flag = false;
		WebElement table = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.studentProgressTable, 10);
		List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.rows, 10);
		for (WebElement row : rows) {
			WebElement exer = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.exercisename, 10);
			if (exer == null)
				exer = commonLibrary.isExistNegative(row, UIMAP_Interactivecitationworkstation.studentName, 10);
			WebElement link = commonLibrary.isExistNegative(exer, UIMAP_Interactivecitationworkstation.lnkLinks, 10);
			if (link != null) {
				String exeName = "";
				if (link.getText().contains(":"))
					exeName = link.getText().split(":")[1].trim();
				else
					exeName = link.getText().trim();

				if (exeName.toLowerCase().contains(exerName.toLowerCase())) {
					WebElement action = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.actionButton, 10);
					commonLibrary.clickLinkWithWebElementWithWait(action, "Action");
					WebElement reset = commonLibrary.isExist(row, UIMAP_Interactivecitationworkstation.viewcomple, 10);
					commonLibrary.clickLinkWithWebElementWithWait(reset, "View completion certificate");
					flag = true;
					break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click view completion certificate for completed exercise.", "view completion certificate for a completed exercise is not clicked.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to change status of a exercise to
	// Not Started.
	// # Function Name : preReqToNotStarted1     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation preReqToNotStarted1(String podName, String linkName) {
		if (generalFunctions.verifylinkinPodStatus(podName, linkName, "Not Started")) {
			return new Interactivecitationworkstation(scriptHelper);
		}

		String student = dataTable.getData("General_Data", "StudentName1");
		String citation = dataTable.getData("General_Data", "CitationManual1");
		this.logout().invokeApplication().loginAnother().navigatetoICWProf().selectTab("Individual Student Report").selectStudentDetails(student, citation).resetExerciseNoVerification(linkName).logout().invokeApplication().login().navigatetoICW();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Student Report page is
	// displayed.
	// # Function Name : verifyStudentStatus     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyStudentStatus(String title) {
		WebElement header = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.header, 10);
		if (header.getText().toLowerCase().contains(title.toLowerCase())) {
			report.updateTestLog("Verify Student report is displayed.", "Student report for exercise/student: " + title + " is displayed.", Status.PASS);
		} else
			report.updateTestLog("Verify Student report is displayed.", "Student report for exercise/student: " + title + " is not displayed.", Status.PASS);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action
	// # Function Name : clickActionDropdown    
	// # Author : Pratik
	// # Date Created : Nov'15
	// #**************************************************************************************************
	public Interactivecitationworkstation clickActionDropdown() {
		generalFunctions.clickActionDropdown();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether option present in
	// Action Dropdown
	// # Function Name : verifyActionsDropdownOption    
	// # Author : Pratik
	// # Date Created : Nov'15
	// #**************************************************************************************************

	public Interactivecitationworkstation verifyActionsDropdownOption(String option) {
		boolean flag = false;
		WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
		WebElement divAction = commonLibrary.isExistNegative(divider, UIMAP_SearchResult.divAction, 5);
		List<WebElement> buttons = commonLibrary.isExistList(divAction, UIMAP_Interactivecitationworkstation.buttons, 10);
		for (WebElement item : buttons) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				report.updateTestLog("Verify " + option + " is present in Actions Dropdown.", option + " is present in Actions Dropdown.", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + option + " is present in Actions Dropdown.", option + " is not present in Actions Dropdown.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option present in Action
	// Dropdown
	// # Function Name : selectActionsDropdownOption    
	// # Author : Pratik
	// # Date Created : Nov'15
	// #**************************************************************************************************

	public Interactivecitationworkstation selectActionsDropdownOption(String option) {
		boolean flag = false;
		WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
		WebElement divAction = commonLibrary.isExistNegative(divider, UIMAP_SearchResult.divAction, 5);
		List<WebElement> buttons = commonLibrary.isExistList(divAction, UIMAP_Interactivecitationworkstation.buttons, 10);
		for (WebElement item : buttons) {
			if (item.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(item, option);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select " + option + " from Actions Dropdown.", option + " is not present in Actions Dropdown.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to confirm Reset for all exercises
	// # Function Name : confirmResetAllExer     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation confirmResetAllExer() {

		WebElement confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer1, 3);
		int count = 0;
		do {
			commonLibrary.sleep(200000);
			confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer1, 3);
			count++;
		} while (confirmReset == null && count < 20);

		verifyResetDialogBoxDisplayed();

		confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer1, 10);
		commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");

		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the dialog box after
	// resetting an execise for all students
	// # Function Name : verifyResetAllDialogBox     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyResetDialogBoxDisplayed() {
		WebElement resetDialog = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestResetDialog, 10);
		if (resetDialog != null && resetDialog.isDisplayed() && resetDialog.getText().toLowerCase().contains("reset"))
			report.updateTestLog("Verify Reset Exercise Dialog Box is displayed.", "Reset Exercise Dialog Box is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Reset Exercise Dialog Box is displayed.", "Reset Exercise Dialog Box is not displayed.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the banner message
	// # Function Name : verifyBannerMessageDismiss     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyBannerMessageDismiss(String text) {
		generalFunctions.verifyBannerMessageDismiss(text);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether graphical view is
	// displayed.
	// # Function Name : verifyBannerMessageDismiss     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyGraphView() {
		WebElement graphExists = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.graphExists, 10);
		if (graphExists != null)
			report.updateTestLog("Verify graphical representation of studentsprogress of exercises is displayed inexpanded view.", "Graphical representation of studentsprogress of exercises is displayed inexpanded view.", Status.PASS);
		else
			report.updateTestLog("Verify graphical representation of studentsprogress of exercises is displayed inexpanded view.", "Graphical representation of studentsprogress of exercises is not displayed inexpanded view.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify ICW home page is displayed
	// # Function Name : verifyIcwHomePage   
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyProfHomePage() {
		WebElement logo = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divlogo, 20);

		WebElement home = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.home);
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 10);
		if (logo != null && home != null) {
			if (logo.getText().contains("Interactive Citation Workstation") && home.getText().contains("Home") && tabs != null) {
				report.updateTestLog("Verify ICW professor home page is displayed", "ICW professor home page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify ICW professor home page is displayed", "ICW professor home page is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to confirm Reset for all students
	// # Function Name : confirmResetAllStu     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation confirmResetAllStu() {

		WebElement confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer, 3);
		int count = 0;
		do {
			commonLibrary.sleep(200000);
			confirmReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.comfirmResetExer, 3);
			count++;
		} while (confirmReset == null && count < 20);

		verifyResetDialogBoxDisplayed();

		confirmReset = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmResetExer, 10);
		commonLibrary.clickLinkWithWebElementWithWait(confirmReset, "Confirm Reset exercise");

		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether Get Status Report
	// Button is disabled.
	// # Function Name : verifyGetStatusButtonDisabled     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyGetStatusButtonDisabled() {
		WebElement podContent = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.podContent, 10);
		WebElement getReport = commonLibrary.isExist(podContent, UIMAP_Interactivecitationworkstation.getStatusReportStud, 10);
		if (getReport.isEnabled())
			report.updateTestLog("Verify 'Get status Report for Student' button is disabled.", "'Get status Report for Student' button is enabled.", Status.FAIL);
		else
			report.updateTestLog("Verify 'Get status Report for Student' button is disabled.", "'Get status Report for Student' button is disabled.", Status.PASS);

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to reset Exercise and verify
	// # Function Name : resetExerciseVerify     
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation resetExerciseVerify(String exerciseName, String para1, String para2) {
		generalFunctions.resetExerciseVerify(exerciseName, para1, para2);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if Exercise Page is
	// displayed.
	// # Function Name : verifyExercisePage
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyExercisePage() {
		WebElement exerciseContent = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.exerciseContent, 15);
		if (exerciseContent != null)
			report.updateTestLog("Verify Exercise page is displayed.", "Exercise page is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Exercise page is displayed.", "Exercise page is not displayed.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if previously entered answers
	// are displayed in exercise page.
	// # Function Name : verifyAnswersNotDisplayed
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyAnswersNotDisplayed() {
		WebElement incorrAns = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.incorrAns, 5);
		WebElement correctAnswer = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.correctAnswer, 5);
		if (incorrAns == null && correctAnswer == null)
			report.updateTestLog("Verify Previously entered answers do not display.", "Previously entered answers do not display.", Status.PASS);
		else
			report.updateTestLog("Verify Previously entered answers do not display.", "Previously entered answers are displayed.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty name is displayed
	// under Assigned Instructors section
	// # Function Name : verifyfaculty   
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyFaculty(String usernameToVerify) {
		boolean flag = false;
		WebElement verifyFacultyParent = commonLibrary.isExist(UIMAP_Settings.verifyFacultyParent, 10);
		if (verifyFacultyParent != null) {
			List<WebElement> verifyFaculty = commonLibrary.isExistList(verifyFacultyParent, UIMAP_Settings.verifyFaculty, 10);
			for (WebElement item : verifyFaculty) {
				if (item.getText().toLowerCase().contains(usernameToVerify.toLowerCase())) {
					report.updateTestLog("Verify if faculty " + usernameToVerify + " is displayed under Assigned Instructors.", "Faculty " + usernameToVerify + " is displayed under Assigned Instructors.", Status.PASS);
					flag = true;
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify if faculty " + usernameToVerify + " is displayed under Assigned Instructors.", "Faculty " + usernameToVerify + " is not displayed under Assigned Instructors.", Status.FAIL);
		} else
			report.updateTestLog("Verify if faculty " + usernameToVerify + " is displayed under Assigned Instructors.", "Assigned Instructors section is not displayed.", Status.FAIL);
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify column names and their order
	// # Function Name : verifyColumnHeaderOrder     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyColumnHeaderOrder(String strColumnName) {
		String[] arrColumnName = strColumnName.split(";");

		// boolean blnFlag = false;
		WebElement bluebooktable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.bluebooktable, 15);
		if (bluebooktable == null)
			bluebooktable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.table, 10);
		List<WebElement> lstcolheaders = commonLibrary.isExistList(bluebooktable, UIMAP_Interactivecitationworkstation.bluebookcolheader, 20);

		for (int i = 0; i < lstcolheaders.size(); i++) {

			if (lstcolheaders.get(i).getText().contains(arrColumnName[i])) {
				report.updateTestLog("Verify " + arrColumnName[i] + " displayed as column headers", arrColumnName[i] + " is displayed as column headers ", Status.PASS);
			} else {
				report.updateTestLog("Verify " + arrColumnName[i] + " displayed as column headers", arrColumnName[i] + " is not displayed as column headers", Status.FAIL);
			}
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Coloumn details are
	// correctly displayed for the first row
	// # Function Name : verifyColoumnDetails1stRow     
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyColoumnDetails1stRow(String coloumnHeader, String text) {
		// boolean flag=false;
		String elementClass = "";
		switch (coloumnHeader) {
		case "Manual":
			elementClass = "citationmanualtitle";
			break;
		case "Exercise":
			elementClass = "exercisename";
			break;
		case "Problem Number":
			elementClass = "problemnumber number";
			break;
		case "% Incorrect":
			elementClass = "inorrectpercent number";
			break;
		case "% Correct":
			elementClass = "correctpercent number";
			break;
		case "% Skipped":
			elementClass = "incompletepercent number";
			break;
		case "% Remaining":
			elementClass = "notattemptedpercent number";
			break;
		}
		if (elementClass.equals("")) {

			if (coloumnHeader.toLowerCase().contains("manual"))
				report.updateTestLog("Verify The Citation manuals containing the 5 most incorrectly answered problems display in 'Manual' column", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("exercise"))
				report.updateTestLog("Verify The exercises in the respective Citation Manuals containing the 5 most incorrectly answered problemsdisplay in the 'Exercise' column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("problem number"))
				report.updateTestLog("Verify The 5 most incorrectly answered problems display in the 'Problem Number' column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("% incorrect"))
				report.updateTestLog("Verify The % of students answering the particular problem incorrectly displays in the '%Incorrect' column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("% correct"))
				report.updateTestLog("Verify The % of students answering the particular problem correctly displays in the <%Correct> column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("% skipped"))
				report.updateTestLog("Verify The % of students leaving the particular problem unanswered and completing the exercise displays in the '%Skipped's column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
			else if (coloumnHeader.toLowerCase().contains("% remaining"))
				report.updateTestLog("Verify The % of students yet to attempt the particular problem displays in the '% Remaining' column.", "Enter Coloumn Header as any of the following: Manual;Exercise;Problem Number;% Incorrect;% Correct;% Skipped;% Remaining", Status.FAIL);
		} else {
			WebElement bluebooktable = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.table, 10);
			WebElement contents = commonLibrary.isExistNegative(bluebooktable, UIMAP_Interactivecitationworkstation.tableContents(elementClass), 5);
			if (contents.getText().toLowerCase().contains(text.toLowerCase())) {
				if (coloumnHeader.toLowerCase().contains("manual"))
					report.updateTestLog("Verify The Citation manuals containing the 5 most incorrectly answered problems display in 'Manual' column", "The Citation manuals containing the 5 most incorrectly answered problems display in 'Manual' column", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("exercise"))
					report.updateTestLog("Verify The exercises in the respective Citation Manuals containing the 5 most incorrectly answered problemsdisplay in the 'Exercise' column.", "The exercises in the respective Citation Manuals containing the 5 most incorrectly answered problems display in the 'Exercise' column.", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("problem number"))
					report.updateTestLog("Verify The 5 most incorrectly answered problems display in the 'Problem Number' column.", "The 5 most incorrectly answered problems display in the 'Problem Number' column.", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("% incorrect"))
					report.updateTestLog("Verify The % of students answering the particular problem incorrectly displays in the '%Incorrect' column.", "The % of students answering the particular problem incorrectly displays in the '%Incorrect' column.", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("% correct"))
					report.updateTestLog("Verify The % of students answering the particular problem correctly displays in the <%Correct> column.", "The % of students answering the particular problem correctly displays in the '%Correct' column.", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("% skipped"))
					report.updateTestLog("Verify The % of students leaving the particular problem unanswered and completing the exercise displays in the '%Skipped's column.", "The % of students leaving the particular problem unanswered and completing the exercise displays in the '%Skipped' column.", Status.PASS);
				else if (coloumnHeader.toLowerCase().contains("% remaining"))
					report.updateTestLog("Verify The % of students yet to attempt the particular problem displays in the '% Remaining' column.", "The % of students yet to attempt the particular problem displays in the '% Remaining' column.", Status.PASS);
			} else {
				if (coloumnHeader.toLowerCase().contains("manual"))
					report.updateTestLog("Verify The Citation manuals containing the 5 most incorrectly answered problems display in 'Manual' column", "The Citation manuals containing the 5 most incorrectly answered problems do not display in 'Manual' column", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("exercise"))
					report.updateTestLog("Verify The exercises in the respective Citation Manuals containing the 5 most incorrectly answered problemsdisplay in the 'Exercise' column.", "The exercises in the respective Citation Manuals containing the 5 most incorrectly answered problems do not display in the 'Exercise' column.", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("problem number"))
					report.updateTestLog("Verify The 5 most incorrectly answered problems display in the 'Problem Number' column.", "The 5 most incorrectly answered problems do not display in the 'Problem Number' column.", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("% incorrect"))
					report.updateTestLog("Verify The % of students answering the particular problem incorrectly displays in the '%Incorrect' column.", "The % of students answering the particular problem incorrectly does not displays in the '%Incorrect' column.", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("% correct"))
					report.updateTestLog("Verify The % of students answering the particular problem correctly displays in the <%Correct> column.", "The % of students answering the particular problem correctly does not displays in the '%Correct' column.", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("% skipped"))
					report.updateTestLog("Verify The % of students leaving the particular problem unanswered and completing the exercise displays in the '%Skipped's column.", "The % of students leaving the particular problem unanswered and completing the exercise does not displays in the '%Skipped' column.", Status.FAIL);
				else if (coloumnHeader.toLowerCase().contains("% remaining"))
					report.updateTestLog("Verify The % of students yet to attempt the particular problem displays in the '% Remaining' column.", "The % of students yet to attempt the particular problem does not displays in the '% Remaining' column.", Status.FAIL);
			}
		}
		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify The Problems are sorted from
	// highest to lowest as per the %Incorrect column.
	// # Function Name : verifyIncorrectSortedDescending     
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation verifyIncorrectSortedDescending() {
		// WebElement bluebooktable =
		// commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.table,
		// 10);
		List<WebElement> incorrectPercentList = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.tableContents("inorrectpercent number"), 10);
		boolean flag = true;
		for (int i = 1; i < incorrectPercentList.size(); i++) {
			int data1 = Integer.parseInt(incorrectPercentList.get(i - 1).getText().replaceAll("[^0-9]", ""));
			int data2 = Integer.parseInt(incorrectPercentList.get(i).getText().replaceAll("[^0-9]", ""));
			if (data1 < data2) {
				flag = false;
				report.updateTestLog("Verify The Problems are sorted from highest to lowest as per the '%Incorrect' column.", "The Problems are not sorted from highest to lowest as per the '%Incorrect' column.", Status.FAIL);
				break;
			}
		}
		if (flag) {
			report.updateTestLog("Verify The Problems are sorted from highest to lowest as per the '%Incorrect' column.", "The Problems are not sorted from highest to lowest as per the '%Incorrect' column.", Status.PASS);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify The Problems are sorted from
	// highest to lowest as per the %Incorrect column.
	// # Function Name : verifyIncorrectSortedDescending     
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyCutOff(int cutOff) {
		List<WebElement> incorrectPercentList = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.tableContents("inorrectpercent number"), 10);
		boolean flag = false;
		for (WebElement item : incorrectPercentList) {
			int data = Integer.parseInt(item.getText().replaceAll("[^0-9]", ""));
			if (data < cutOff) {
				flag = true;
				report.updateTestLog("Verify The 'Cut Off %' is set as " + cutOff + "% by default.", "The 'Cut Off %' is not set as " + cutOff + "% by default", Status.FAIL);
				report.updateTestLog("Verify The problems display such that '%Incorrect' is greater than 'Cut off %'", "The problems display such that '%Incorrect' is lesser than 'Cut off %'", Status.FAIL);
				report.updateTestLog("Verify The 'Cut Off %' is set as " + cutOff + "% by default.", "The 'Cut Off %' is not set as " + cutOff + "% by default", Status.FAIL);
				break;
			}
		}
		if (!flag) {
			report.updateTestLog("Verify The 'Cut Off %' is set as " + cutOff + "% by default.", "The 'Cut Off %' is set as " + cutOff + "% by default", Status.PASS);
			report.updateTestLog("Verify The problems display such that '%Incorrect' is greater than 'Cut off %'", "The problems display such that '%Incorrect' is greater than 'Cut off %'", Status.PASS);
		}
		return new Interactivecitationworkstation(scriptHelper);
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
	// # Function Description : Function to verify ICW home page is displayed
	// # Function Name : verifyIcwHomePage   
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyStuHomePage() {
		WebElement logo = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.divlogo, 20);

		WebElement home = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.home);
		WebElement tabs = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.tabs, 8);
		if (logo != null && home != null) {
			if (logo.getText().contains("Interactive Citation Workstation") && home.getText().contains("Home") && tabs == null) {
				report.updateTestLog("Verify ICW student home page is displayed", "ICW student home page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify ICW student home page is displayed", "ICW student home page is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify ICW home page is displayed", "ICW home page is not displayed", Status.FAIL);
		}

		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to create folder and save
	// # Function Name : createFolderAndSave_new 
	// # Author : Vennila
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation createFolderAndSave_new(String folderName) {

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

		return new Interactivecitationworkstation(scriptHelper);
	}
}
