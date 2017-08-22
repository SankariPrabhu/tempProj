package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPSHome;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class NonLAProductWindow extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public NonLAProductWindow(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lexis"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lexis") && counter < 40);

		if (!driver.getCurrentUrl().contains("lexis")) {
			throw new IllegalStateException("Non LA product page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the new eindow displayed.
	// # Function Name : verifyPage     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Home verifyPage(String url, String title) {

		if (driver.getWindowHandles().size() == 1) {
			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().toLowerCase().contains(title.toLowerCase())) {
				report.updateTestLog("Verify " + title + " landing/signin page is displayed.", title + " landing/signin page is displayed.", Status.PASS);
				commonLibrary.clickBrowserBack();
			} else {
				report.updateTestLog("Verify " + title + " landing/signin page is displayed.", title + " landing/signin page is not displayed.", Status.FAIL);
			}
		} else {
			if (commonLibrary.switchToWindow1(url)) {
				if (url.equals("signin")) {
					report.updateTestLog("Verify landing/signin page is displayed.", title + " Signin page is displayed.", Status.PASS);
				} else {
					WebElement header = commonLibrary.isExistNegative(By.cssSelector("img[title='" + title + "']"), 10);
					if (header != null) {
						report.updateTestLog("Verify " + title + " landing/signin page is displayed.", title + " landing/signin page is displayed.", Status.PASS);
					} else {
						WebElement header1 = commonLibrary.isExistNegative(UIMAP_Home.titleHeader, 10);
						if (header1 == null)
							header1 = commonLibrary.isExistNegative(UIMAP_Home.getPrintHeader, 10);
						else if (header1 != null && header1.getText().toLowerCase().contains(title.toLowerCase()))
							report.updateTestLog("Verify " + title + " landing/signin page is displayed.", title + " landing/signin page is displayed.", Status.PASS);
						else
							report.updateTestLog("Verify " + title + " landing/signin page is displayed.", title + " landing/signin page is not displayed.", Status.FAIL);
					}
				}
				driver.close();
				try {
					commonLibrary.sleep(3000);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				report.updateTestLog("Close Secondary browser..", "Secondary browser is closed.", Status.DONE);

			} else
				report.updateTestLog("Verify Secondary window is displayed.", "Secondary window is not displayed.", Status.FAIL);

			commonLibrary.switchToWindow("firsttime");
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply Search Filter
	// # Function Name : applySearchFilter     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public Home applySearchFilter(String strToolbarMenuName, String strCondentTypes, boolean state) {
		String[] arrCondentType = strCondentTypes.split(";");

		Boolean blnFlag = false;
		WebElement btnClassFilter = commonLibrary.isExist(UIMAP_LPSHome.btnClassFilter, 20);
		if (!(btnClassFilter.getAttribute("class").contains("selected"))) {
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(btnClassFilter, "Filter");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			}
		}
		filterMenuSelection(strToolbarMenuName);

		switch (strToolbarMenuName) {
		case "Areas of Expertise": {
			blnFlag = false;
			for (int i = 0; i < arrCondentType.length; i++) {
				WebElement divClassCTFilters = commonLibrary.isExist(UIMAP_LPSHome.divClassCTFilters, 20);
				if (divClassCTFilters != null) {
					WebElement lstTagUList = commonLibrary.isExist(divClassCTFilters, UIMAP_Home.lstTagUList, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagUList, UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(arrCondentType[i])) {
								WebElement Checkbox = commonLibrary.isExist(item, UIMAP_Home.chkTypeCheckbox, 20);
								commonLibrary.setCheckBox(Checkbox, state);
								blnFlag = true;
								break;
							}

						}
				}
			}
			break;
		}
		}
		if (!blnFlag)
			report.updateTestLog("select check boxes near " + strCondentTypes, "Not select check boxes near " + strCondentTypes, Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply Search Filter
	// # Function Name : applySearchFilter     
	// # Author : Aravind
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public void filterMenuSelection(String strMenuName) {

		try {
			WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_LPSHome.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				List<WebElement> lstButtons = commonLibrary.isExistList(mnuFilterToolBar, UIMAP_LPSHome.btnIdFilterMenu, 20);
				if (lstButtons.size() > 0)
					for (WebElement button : lstButtons) {
						if (button.getText().contains(strMenuName)) {
							Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
							String browsername = cap.getBrowserName();

							if (browsername.equalsIgnoreCase("internet explorer"))
								commonLibrary.clickJS(button, strMenuName);
							else
								commonLibrary.clickButtonParentWithWait(button, strMenuName);

							break;
						}

					}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

}
