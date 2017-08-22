package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.Status;
import supportlibraries.*;

import supportlibraries.ScriptHelper;

public class ArchivedSearch extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public ArchivedSearch(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("alert"))
				commonLibrary.sleep(5000);

		} while (!url.contains("searcharchivedcodes") && counter < 40);
		if (!driver.getCurrentUrl().contains("searcharchivedcodes")) {
			throw new IllegalStateException(
					"searcharchivedcodes page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform archivesearch
	// # Function Name : archiveSearchWithConditions     
	// # Author : Seetha
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public Search archiveSearchWithConditions(String searchTerm,
			String category, String daterange, String year, String juri) {

		// setting the search term in search box
		WebElement archiveSearchbox = commonLibrary.isExist(
				UIMAP_Home.archiveSearchbox, 10);
		commonLibrary.setDataInTextBox(archiveSearchbox, searchTerm,
				"SearchTerm");
		commonLibrary.sleep(30000);

		// Choosing category
		if (!category.toLowerCase().equalsIgnoreCase("ignore")) {
			boolean chk = false;
			String[] catVal = category.split(";");
			WebElement categorydiv = commonLibrary.isExist(UIMAP_Home.catDiv,
					10);
			List<WebElement> values = commonLibrary.isExistList(categorydiv,
					UIMAP_SearchResult.lstTagName, 10);
			for (int i = 0; i < catVal.length; i++) {
				for (int j = 0; j < values.size(); j++) {
					List<WebElement> labels = commonLibrary.isExistList(
							values.get(j), By.tagName("label"), 10);
					for (WebElement labelitems : labels) {
						if (labelitems.getText().equalsIgnoreCase(catVal[i])) {
							WebElement chkbox = commonLibrary.isExist(
									values.get(j), UIMAP_Home.chkTypeCheckbox,
									10);
							commonLibrary.setCheckBox(chkbox, category);
							chk = true;
							break;
						}
					}
					if (chk)
						break;
				}
				if (!chk)
					report.updateTestLog("select " + catVal[i] + "", ""
							+ catVal[i] + " is not selected", Status.FAIL);
			}
		}

		// Choosing date option and selecting years
		if (!daterange.toLowerCase().equalsIgnoreCase("ignore")) {
			WebElement archiveyear = commonLibrary.isExist(
					UIMAP_Home.archivedate, 10);
			if (commonLibrary.selectByVisibleText(archiveyear, daterange))
				report.updateTestLog("Select date range " + daterange + "",
						"date range " + daterange + " is selected", Status.PASS);
			else
				report.updateTestLog("Select date range " + daterange + "",
						"date range " + daterange + " is not selected",
						Status.FAIL);

			if (year.contains(";")) {
				String[] years = year.split(";");
				WebElement fromYr = commonLibrary
						.isExist(UIMAP_Home.fromYr, 10);
				WebElement ToYr = commonLibrary.isExist(UIMAP_Home.toYr, 10);
				commonLibrary.setDataInTextBox(fromYr, years[0], "from year");
				commonLibrary.setDataInTextBox(ToYr, years[1], "To year");

			} else {
				WebElement fromYr = commonLibrary
						.isExist(UIMAP_Home.fromYr, 10);
				commonLibrary.setDataInTextBox(fromYr, year, "from year");
			}
		}

		// Selecting jurisdiction
		if (!juri.toLowerCase().equalsIgnoreCase("ignore")) {
			boolean chk1 = false;
			WebElement jurisection = commonLibrary.isExist(
					UIMAP_Home.juriSection, 10);
			WebElement expand = commonLibrary.isExist(jurisection,
					UIMAP_Home.expandIcon, 10);
			if (expand != null) {
				commonLibrary.clickButtonParent(expand,
						"Expand icon of jurisdiction");
			}
			List<WebElement> values1 = commonLibrary.isExistList(jurisection,
					UIMAP_SearchResult.lstTagName, 10);

			String[] juris = juri.split(";");
			for (int m = 0; m < juris.length; m++) {
				for (int j = 0; j < values1.size(); j++) {
					List<WebElement> labels = commonLibrary.isExistList(
							values1.get(j), By.tagName("label"), 10);
					for (WebElement labelitems : labels) {
						if (labelitems.getText().equalsIgnoreCase(juris[m])) {
							WebElement chkbox = commonLibrary.isExist(
									values1.get(j), UIMAP_Home.chkTypeCheckbox,
									10);
							commonLibrary.setCheckBox(chkbox, juris[m]);
							chk1 = true;
							break;
						}
					}
					if (chk1)
						break;
				}
				if (!chk1)
					report.updateTestLog("select " + juris[m] + "", ""
							+ juris[m] + " is not selected", Status.FAIL);
			}
		}

		// Click on search button
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.idsearch,
				20);
		commonLibrary.clickSearchButton(eltSearchbutton, "Search");

		return new Search(scriptHelper);

	}

}
