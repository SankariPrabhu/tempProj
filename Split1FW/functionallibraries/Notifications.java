package functionallibraries;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;

import UIMAP.*;
import supportlibraries.*;

public class Notifications extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	Properties localizeProperties = Settings.getInstance();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;

	public Notifications(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("notification"))
				commonLibrary.sleep(5000);

		} while (!url.contains("notification") && counter < 40);

		if (!driver.getCurrentUrl().contains("notification")) {
			throw new IllegalStateException("Notification page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Notifications
	// # Function Name : verifyNotifications     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public Notifications verifyNotifications(String strDocTitle, String whom) {
		WebElement btnNextPage = null;
		boolean blnFlag = false;
		int count = 1;
		do {
			WebElement divWrapper = commonLibrary.isExist(UIMAP_SearchResult.resultwrapper, 10);
			if (divWrapper != null) {
				WebElement OListResult = commonLibrary.isExist(divWrapper, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 20);
						if (eleDocTitle != null) {
							if (eleDocTitle.getText().toLowerCase().contains(strDocTitle.toLowerCase())) {
								if (!whom.equalsIgnoreCase("student"))
									report.updateTestLog("Verify " + strDocTitle + " exists", strDocTitle + " is part of " + eleDocTitle.getText() + " it is hyperlinked", Status.PASS);
								else
									report.updateTestLog("Verify student " + strDocTitle + " exists", "Student " + strDocTitle + " is part of " + eleDocTitle.getText(), Status.PASS);
								blnFlag = true;
								btnNextPage = null;
								break;
							}
						}
					}
				}
			}
			if (!blnFlag) {
				btnNextPage = commonLibrary.isExist(UIMAP_SearchResult.btnNextPage);
				if (btnNextPage != null) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(btnNextPage, "NextPage");
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(btnNextPage, "NextPage");
					}
				}
			}
			count++;
		} while (btnNextPage != null && count <= 15);
		return new Notifications(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout.
	// # Function Name : logout     
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

}
