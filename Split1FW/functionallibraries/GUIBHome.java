package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import UIMAP.*;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBHome extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public GUIBHome(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("guibhome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("guibhome") && counter < 40);

		if (!driver.getCurrentUrl().contains("guibhome")) {
			throw new IllegalStateException("GUIB home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking links in GUIB Home page
	// # Function Name : ClickLick_GUIBHomePage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances clickLinkGUIBHomePage(String linkName) {
		try {
			boolean flag = false;
			List<WebElement> divWrappers = commonLibrary.isExistList(UIMAP_GUIBHome.podCont, 20);
			for (WebElement divWrapper : divWrappers) {
				List<WebElement> Links = commonLibrary.isExistList(divWrapper, By.tagName("a"), 20);
				for (WebElement item : Links) {
					if (item.getText().equalsIgnoreCase(linkName)) {
						commonLibrary.clickLinkWithWebElementWithWait(item, linkName);
						commonLibrary.sleep(6000);
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
			if (!flag)
				report.updateTestLog("Click Link: " + linkName, linkName + " is not clicked.", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new GUIBInstances(scriptHelper);
	}

}
