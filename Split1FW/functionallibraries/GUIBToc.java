package functionallibraries;

import java.util.List;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBToc extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public GUIBToc(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("toc"))
				commonLibrary.sleep(5000);

		} while (!url.contains("toc") && counter < 40);

		if (!driver.getCurrentUrl().toLowerCase().contains("toc")) {
			throw new IllegalStateException("GUIB toc page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify toc page is displayed
	// # Function Name : VerifyTocPage     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyTocPage(String value) {
		boolean tocDisplayed = false;
		commonLibrary.switchToWindow("toc");
		WebElement header = commonLibrary.isExist(UIMAP_SearchResult.txtStudentReportHeader, 10);
		if (header != null) {
			if (header.getText().toLowerCase().contains("table of contents")) {
				List<WebElement> text = commonLibrary.isExistList(header, UIMAP_SearchResult.tagSpan, 10);
				for (WebElement item : text) {
					if (item.getText().equalsIgnoreCase(value)) {
						tocDisplayed = true;
						break;
					}
				}
			}
		}
		if (tocDisplayed) {
			report.updateTestLog("Verify Table of contents for the selected source is displayed in the secondary browser", "Table of contents for the selected source is displayed in the secondary browser", Status.PASS);
			driver.close();
			commonLibrary.switchToWindow("container");
			report.updateTestLog("Close the secondary browser", "Secondary browser is closed", Status.PASS);
		} else {
			report.updateTestLog("Verify Table of contents for the selected source is displayed in the secondary browser", "Table of contents for the selected source is not  displayed in the secondary browser", Status.FAIL);
			report.updateTestLog("Close the secondary browser", "Secondary browser is not closed", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

}
