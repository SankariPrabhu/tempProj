package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import UIMAP.*;

import supportlibraries.*;

public class GetandprintExperience extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public GetandprintExperience(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("GetAndPrintExperience"))
				commonLibrary.sleep(5000);

		} while (!url.contains("GetAndPrintExperience") && counter < 40);

		if (!driver.getCurrentUrl().contains("GetAndPrintExperience")) {
			throw new IllegalStateException("Help page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get multiple citation
	// # Function Name : getmultiple_citation     
	// # Author :Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking getMultipleCitation(String Title, String title2, String strvalue) {
		commonLibrary.switchToWindow(Title);
		WebElement citationtext = commonLibrary.isExist(UIMAP_PodPreview.citation, 20);
		commonLibrary.setDataInTextBox(citationtext, strvalue, "Enter Multiple Citation");
		WebElement reportcheckbox = commonLibrary.isExist(UIMAP_PodPreview.reportcheckbox, 20);
		commonLibrary.setRadioButton(reportcheckbox, "Retrieve Shepard's® report");

		WebElement objGet = commonLibrary.isExist(By.cssSelector("input[type*='submit'][value='Get']"), 20);
		if (objGet != null) {
			commonLibrary.clickButtonParentWithWait(objGet, "Get");

		}

		commonLibrary.switchToWindow(title2);

		return new CounselBenchmarking(scriptHelper);
	}
}
