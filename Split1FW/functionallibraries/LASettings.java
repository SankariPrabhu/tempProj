package functionallibraries;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPAHome;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Settings;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.*;

public class LASettings extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	public WebElement wordWheelContent;
	private PageCheck pageCheck = new PageCheck(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public LASettings(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("settings"))
				commonLibrary.sleep(5000);

		} while (!url.contains("settings") && counter < 40);

		if (!driver.getCurrentUrl().contains("settings")) {
			throw new IllegalStateException("Settings page is expected, but not displayed!");

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Settings page is
	// displayed
	// # Function Name : VerifySettingsPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LASettings verifySettingsPage() {
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
		if (btnMore != null)
			commonLibrary.clickMethod(btnMore, "More");

		WebElement lnkSettings = commonLibrary.isExist(UIMAP_Home.lnkTextSettings, 10);
		if (lnkSettings == null || !lnkSettings.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
		}

		if (lnkSettings != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSettings, "Settings");

		// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

		WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleSettings, 10);
		if (PgTitle != null) {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Settings Page is displayed", "Settings Page is NOT displayed", Status.FAIL);

		}
		WebElement btnGeneral = commonLibrary.isExist(UIMAP_Settings.btnIdGeneral, 10);
		if (btnGeneral != null && btnGeneral.getAttribute("class").toLowerCase().contains("active")) {
			report.updateTestLog("Verify <General> settings section displayed by default", "<General> settings section displayed by default", Status.PASS);
		} else {
			report.updateTestLog("Verify <General> settings section displayed by default", "<General> settings section NOT displayed by default", Status.WARNING);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Change Default profile type in lps
	// # Function Name : lpsChangeDefaultProfileType
	// # Author : Dinesh
	// # Date Created : '15
	// #*****************************************************************************************************************************
	public LPResults lpsChangeDefaultProfileType(String profileType) {

		if (!profileType.equals("")) {
			WebElement profileTypeDropdown = commonLibrary.isExist(UIMAP_Settings.profileTypeDropdown, 10);
			commonLibrary.selectByVisibleText(profileTypeDropdown, profileType);
			report.updateTestLog("Verify '" + profileType + "' is set in drop down under heading 'Search Litigation Profile Suite using this default profile type:'", "'" + profileType + "' is set in drop down under heading 'Search Litigation Profile Suite using this default profile type:'", Status.PASS);
		}

		{
			commonLibrary.scrollDown(1000);

			// Click on save settings
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);

			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				// if the settings option is already selected, just close the
				// settings page
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);

				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}

		}

		return new LPResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do settings in LexisAdvance Research
	// Page
	// # Function Name : VerifySettingsPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search researchPageSettings(String strOption, Boolean isCheck) {
		try {

			// Click Research link
			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// Verification Point: Lexis Advance® Research Page is displayed
			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}

			// select different radio button based on the options
			WebElement rdoCaseOption = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Standard": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.standardRadio, 10);
				break;
			}
			case "Expanded": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.expandedRadio, 10);
				break;
			}
			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon")) {

				commonLibrary.setRadioButton(rdoCaseOption, strOption);
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is selected from Category/Other options", Status.PASS);

			} else if (strOption.equals("Recently Viewed Icon")) {

				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {

					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);

				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);

			} else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}

			commonLibrary.scrollDown(1000);

			// Click on save settings
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);

			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				// if the settings option is already selected, just close the
				// settings page
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);

				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 5);
			int count = 0;
			do {
				commonLibrary.sleep(25000);
				addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 5);
				count++;
			} while (addtoFolder == null && count < 20);

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do settings in LexisAdvance Research
	// Page
	// # Function Name : VerifySettingsPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LASettings researchPageSettings2(String strOption, Boolean isCheck) {
		try {

			// Click Research link
			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// Verification Point: Lexis Advance® Research Page is displayed
			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}

			// select different radio button based on the options
			WebElement rdoCaseOption = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Standard": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.standardRadio, 10);
				break;
			}
			case "Expanded": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.expandedRadio, 10);
				break;
			}
			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon")) {

				commonLibrary.setRadioButton(rdoCaseOption, strOption);
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is selected from Category/Other options", Status.PASS);

			} else if (strOption.equals("Recently Viewed Icon")) {

				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {

					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);

				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);

			} else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}

			commonLibrary.scrollDown(1000);

			// Click on save settings
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);

			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				// if the settings option is already selected, just close the
				// settings page
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);

				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}
			WebElement addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 5);
			int count = 0;
			do {
				commonLibrary.sleep(25000);
				addtoFolder = commonLibrary.isExist(UIMAP_ResearchMap.addFolder, 5);
				count++;
			} while (addtoFolder == null && count < 20);

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do settings in LexisAdvance Research
	// Page
	// # Function Name : VerifySettingsPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Research researchPageSettingsResearchHome(String strOption, Boolean isCheck) {
		System.out.println(strOption);
		try {
			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}
			WebElement rdoCaseOption = null;
			WebElement rdoCaseOption1 = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.termsRadio, 10);
				break;
			}
			case "Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;
			}
			case "Show Extract & Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;

			}
			case "Show Overview & Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowOverview, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;

			}
			case "Show Terms & Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.termsRadio, 10);
				break;

			}

			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon")) {
				commonLibrary.setRadioButton(rdoCaseOption, "Show Extract");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			} else if (strOption.equals("Recently Viewed Icon")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);
			} else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}
			if (rdoCaseOption1 != null && strOption.equals("Show Extract & Extract")) {
				commonLibrary.setRadioButton(rdoCaseOption1, "Extract");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			}
			commonLibrary.scrollDown(1000);
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);
				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply settings
	// # Function Name : researchPageSettingsHome     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Home researchPageSettingsHome(String strOption, Boolean isCheck) {
		try {

			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}
			WebElement rdoCaseOption = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.termsRadio, 10);
				break;
			}
			case "Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;
			}
			case "Apply filters for subcategories": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.applyFilterSubCat, 10);
				break;
			}
			case "Recognize and use legal entities when performing a search": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.displayPracticeArea, 10);
				break;
			}
			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon") && !strOption.equals("Apply filters for subcategories") && !strOption.equals("Recognize and use legal entities when performing a search")) {
				commonLibrary.setRadioButton(rdoCaseOption, strOption);
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is selected from Category/Other options", Status.PASS);
			} else if (strOption.equals("Recently Viewed Icon")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);
			} else if (strOption.equals("Apply filters for subcategories")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Narrow Results", strOption + " is Checked from Narrow Results", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Narrow Results", strOption + " is unchecked from Narrow Results", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Narrow Results", strOption + " is not checked/unchecked from Narrow Results", Status.FAIL);
			} else if (strOption.equals("Recognize and use legal entities when performing a search")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Narrow Results", strOption + " is Checked from Search", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Narrow Results", strOption + " is unchecked from Search", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Narrow Results", strOption + " is not checked/unchecked from Search", Status.FAIL);
			}

			else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}
			commonLibrary.scrollDown(1000);
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null) {
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(20000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}

			else {
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);
				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply Cases as start in settings
	// page
	// # Function Name : setCases   
	// # Author : Deepha H
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public Home setCases() {
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

		WebElement listbox = commonLibrary.isExist(By.cssSelector("select[id='settings_research_searchresults']"), 20);
		commonLibrary.selectByVisibleTextByValue(listbox, "urn:hlct:5", "Cases");
		commonLibrary.scrollDown(1000);
		WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
		if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
			commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

		else {
			report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.DONE);
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
		}
		return new Home(scriptHelper);
	}

	public Home setSearchOptioninSettings(String stroption) {
		WebElement researchlink = commonLibrary.isExist(UIMAP_Settings.researchbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		} else {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		}
		WebElement researchheader = commonLibrary.isExist(UIMAP_Settings.researchsettings, 20);
		WebElement formheader = commonLibrary.isExist(researchheader, UIMAP_Settings.formcontainer, 20);
		WebElement ullist = commonLibrary.isExist(formheader, UIMAP_Settings.ullist, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist, UIMAP_Settings.lilist, 20);
		for (WebElement button : lilist) {
			if (button.getText().contains(stroption)) {
				WebElement checkBox = commonLibrary.isExistNegative(button, UIMAP_Document.checkBox, 10);
				commonLibrary.clickJS(checkBox, stroption);
			}
		}
		WebElement savechangesbutton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		} else {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing settings in the General
	// settings page
	// # Function Name : generalSettings
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Home generalSettings(String settingsName, String value)

	{
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement dropdown = null;
		switch (settingsName) {
		case "History": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.HistRMDateRange, 20);
			break;
		}
		case "StartPage": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.StartPagelist, 20);
			break;
		}
		case "Number of Results Per Page": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.numberOfResultsPerPage, 20);
			break;
		}
		}

		commonLibrary.selectByVisibleText(dropdown, value);
		if (commonLibrary.selectIsSelected(dropdown, value))
			report.updateTestLog("Select " + value + " from " + settingsName + " dropdown in general Settings Page.", value + " from " + settingsName + " dropdown is selected in general Settings Page.", Status.DONE);
		else
			report.updateTestLog("Select " + value + " from " + settingsName + " dropdown in general Settings Page.", value + " from " + settingsName + " dropdown is not selected in general Settings Page.", Status.FAIL);
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}
		pageCheck.ajaxWait(driver);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for vwerifying document count
	// # Function Name : verifydocumentcount
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search verifydocumentcount() {
		WebElement activetab = commonLibrary.isExist(UIMAP_SearchResult.activetab, 20);
		WebElement doccount = commonLibrary.isExist(activetab, UIMAP_SearchResult.documentcount, 20);
		String doccounter = doccount.getText();
		dataTable.putData("General_Data", "DocCount1", doccounter);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to client link
	// # Function Name : NavigateToClientLink
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void NavigateToClientLink(String strLinkName) {

		try {

			WebElement btnClassClient = commonLibrary.isExist(UIMAP_Home.btnClassClient, 20);
			if (browsername.contains("internet") && version.contains("9")) {
				commonLibrary.clickButtonParentWithWaitJS(btnClassClient, "Client");
			} else {
				commonLibrary.clickButtonParentWithWait(btnClassClient, "Client");
			}
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
				WebElement lnkTxtSetAddClientID = commonLibrary.isExist(UIMAP_Home.btnActionSetAddClientID, 20);
				if (browsername.contains("internet") && version.contains("9"))
					commonLibrary.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtSetAddClientID, "Set/Add Client ID");
			} else if (strLinkName.equalsIgnoreCase("-None-")) {
				WebElement lnkTxtNone = commonLibrary.isExist(UIMAP_Home.lnkTxtNone, 20);
				if (browsername.contains("internet") && version.contains("9")) {
					commonLibrary.clickJS(lnkTxtNone, "None");
				} else {
					commonLibrary.clickLinkWithWebElement(lnkTxtNone, "None");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting search option in settings
	// # Function Name : setSearchOptioninSettings_Search
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search setSearchOptioninSettings_Search(String stroption) {
		WebElement researchlink = commonLibrary.isExist(UIMAP_Settings.researchbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		} else {
			commonLibrary.clickButtonLogSmallWait(researchlink, "Lexis Advance Research");
		}
		WebElement researchheader = commonLibrary.isExist(UIMAP_Settings.researchsettings, 20);
		WebElement formheader = commonLibrary.isExist(researchheader, UIMAP_Settings.formcontainer, 20);
		WebElement ullist = commonLibrary.isExist(formheader, UIMAP_Settings.ullist, 20);
		List<WebElement> lilist = commonLibrary.isExistList(ullist, UIMAP_Settings.lilist, 20);
		for (WebElement button : lilist) {
			if (button.getText().contains(stroption)) {
				WebElement checkBox = commonLibrary.isExistNegative(button, UIMAP_Document.checkBox, 10);
				commonLibrary.clickJS(checkBox, stroption);
			}
		}
		WebElement savechangesbutton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 20);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		} else {
			commonLibrary.clickButtonLogSmallWait(savechangesbutton, "Save Changes to Settings & Close");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the StartPage dropdown value
	// in Settings and click on cancel button
	// # Function Name : Verify_LandingPage     
	// # Author : Revathi
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Home verifyStartPageDropdownDiscardChanges(String option) {

		WebElement StartPageDropdown = commonLibrary.isExistNegative(UIMAP_Settings.StartPagelist, 10);

		if (commonLibrary.selectIsSelected(StartPageDropdown, option)) {
			report.updateTestLog("Verify " + option + " is selected in StartPage Dropdown.", option + " is selected in StartPage Dropdown.", Status.PASS);
		} else
			report.updateTestLog("Verify " + option + " is selected in StartPage Dropdown.", option + " is not selected in StartPage Dropdown.", Status.FAIL);
		WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
		if (cancel != null && cancel.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(cancel, "submit");

		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting research page in search
	// page
	// # Function Name : researchPageSettingsSearch
	// # Author : Harish
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search researchPageSettingsSearch(String strOption, Boolean isCheck) {
		try {

			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}
			WebElement rdoCaseOption = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Apply filters for subcategories": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.applyFilterSubCat, 10);
				break;
			}
			case "Recognize and use legal entities when performing a search": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.displayPracticeArea, 10);
				break;
			}
			case "Include legal phrase equivalents with search": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.displaylegalphrase, 10);
				break;
			}
			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon") && !strOption.equals("Apply filters for subcategories") && !strOption.equals("Recognize and use legal entities when performing a search")) {
				commonLibrary.setRadioButton(rdoCaseOption, strOption);
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is selected from Category/Other options", Status.PASS);
			} else if (strOption.equals("Recently Viewed Icon")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);
			} else if (strOption.equals("Apply filters for subcategories")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Narrow Results", strOption + " is Checked from Narrow Results", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Narrow Results", strOption + " is unchecked from Narrow Results", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Narrow Results", strOption + " is not checked/unchecked from Narrow Results", Status.FAIL);
			} else if (strOption.equals("Recognize and use legal entities when performing a search")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Narrow Results", strOption + " is Checked from Search", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Narrow Results", strOption + " is unchecked from Search", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Narrow Results", strOption + " is not checked/unchecked from Search", Status.FAIL);
			}

			else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}
			commonLibrary.scrollDown(1000);
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);
				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Lexis@ Interactive Citation
	// Worksation
	// # Function Name : clickLexisIcw     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LASettings clickLexisIcw() {
		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_Settings.contentSwitcher);
		WebElement lexisIcw = commonLibrary.isExist(contentSwitcher, UIMAP_Settings.lexisIcw, 10);
		if (browsername.contains("internet")) {
			commonLibrary.clickButtonParentWithWait(lexisIcw, "Lexis@ Interactive Citation Workstation");
		} else {
			commonLibrary.clickButton(lexisIcw);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click X icon and verify Instructor
	// is removed
	// # Function Name : verifyInstructors     
	// # Author : Anbu
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation verifyInstructors() {

		WebElement professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		List<WebElement> professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
		WebElement saveChangesButton = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 10);

		String[] primaryProfessor = professors.get(0).getText().split("\n");
		String[] secondaryProfessor = professors.get(1).getText().split("\n");
		WebElement primaryXButton = commonLibrary.isExist(professors.get(0), UIMAP_Settings.xButton, 10);
		commonLibrary.clickButtonParentWithWait(primaryXButton, "Primary X Button");
		professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
		saveChangesButton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 10);

		if (!professors.get(0).getText().contains(primaryProfessor[0])) {
			report.updateTestLog("Verify primary Instructor is removed", "Primary Instructor is removed", Status.PASS);
			if (professors.get(0).getText().contains(secondaryProfessor[0].split(">")[0])) {
				report.updateTestLog("Verify secondary instructor is moved to primary position", "Secondary instructor is moved to primary position", Status.PASS);
				WebElement secondaryXButton = commonLibrary.isExist(professors.get(0), UIMAP_Settings.xButton, 10);
				commonLibrary.clickButtonParentWithWait(secondaryXButton, "Secondary X Button");

				professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
				saveChangesButton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 10);

				if (professors.size() == 0) {
					report.updateTestLog("Verify Secondary Instructor is removed", "Secondary instructor is removed", Status.PASS);
					if (saveChangesButton != null) {
						commonLibrary.clickButtonParentWithWait(saveChangesButton, "Save Changes");
					} else {
						report.updateTestLog("Click Save changes to settings and close", "Save changes button is not available", Status.FAIL);
					}
				} else {
					report.updateTestLog("Verify Secondary Instructor is removed", "Secondary instructor is not removed", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify secondary instructor is moved to primary position", "Secondary instructor is not moved to primary position", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify primary Instructor is removed", "Primary Instructor is not removed", Status.FAIL);
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click X icon and verify instructor
	// is removed
	// # Function Name : removeInstructors
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public LASettings removeInstructors() {
		WebElement professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		List<WebElement> professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);

		if (professors.size() > 0 && professors != null) {
			if (professors.size() == 2) {
				String[] primaryProfessor = professors.get(0).getText().split("\n");
				String[] secondaryProfessor = professors.get(1).getText().split("\n");
				WebElement primaryXButton = commonLibrary.isExist(professors.get(0), UIMAP_Settings.xButton, 10);
				commonLibrary.clickButtonParentWithWait(primaryXButton, "Primary X Button");
				professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
				professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);

				if (!professors.get(0).getText().contains(primaryProfessor[0])) {
					report.updateTestLog("Verify primary Instructor is removed", "Primary Instructor is removed", Status.PASS);
					if (professors.get(0).getText().contains(secondaryProfessor[0])) {
						report.updateTestLog("Verify secondary instructor is moved to primary position", "Secondary instructor is moved to primary position", Status.PASS);
						WebElement secondaryXButton = commonLibrary.isExist(professors.get(0), UIMAP_Settings.xButton, 10);
						commonLibrary.clickButtonParentWithWait(secondaryXButton, "Secondary X Button");
						professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
						if (professors.size() == 0)
							report.updateTestLog("Verify Secondary Instructor is removed", "Secondary instructor is removed", Status.PASS);
					}
				}
			} else {
				WebElement primaryXButton = commonLibrary.isExist(professors.get(0), UIMAP_Settings.xButton, 10);
				commonLibrary.clickButtonParentWithWait(primaryXButton, "Primary X Button");
				professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
				professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);

				if (professors.size() == 0)
					report.updateTestLog("Verify primary Instructor is removed", "Primary Instructor is removed", Status.PASS);

			}
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add Instructors
	// # Function Name : addInstructors
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public Interactivecitationworkstation addInstructors(String facultyToAdd) {
		String[] facultyToAddEach = facultyToAdd.split(";");
		for (int fEach = 0; fEach < facultyToAddEach.length; fEach++) {
			int i = 0;
			WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			if (shareUserName != null) {
				wordWheelContent = null;
				do {
					commonLibrary.setDataInTextBox(shareUserName, facultyToAddEach[fEach], "Enter prof's name");
					shareUserName.sendKeys(Keys.ARROW_DOWN);
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
					wordWheelContent = commonLibrary.isExistNegative(UIMAP_Settings.wordWheelContent, 15);
					commonLibrary.sleep(2000);
					WebElement wordWheelOptions = commonLibrary.isExistNegative(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
					commonLibrary.selectFromList(wordWheelOptions, facultyToAddEach[fEach]);
					i++;
				} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);

				WebElement addProf = commonLibrary.isExist(UIMAP_Settings.addProf, 10);
				if (addProf != null)
					commonLibrary.clickButtonParentWithWait(addProf, "Add");
			}
		}

		WebElement saveChangesButton = commonLibrary.isExistNegative(UIMAP_Settings.savechangesbutton, 10);
		if (saveChangesButton != null)
			commonLibrary.clickButtonParentWithWait(saveChangesButton, "Save Changes");

		WebElement closeButton = commonLibrary.isExistNegative(UIMAP_Settings.btnIdCancelSettChange, 10);
		if (closeButton != null)
			commonLibrary.clickButtonParentWithWait(closeButton, "Close");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove all instructor
	// # Function Name : removeAllInstructors
	// # Author : seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public LASettings removeAllInstructors() {

		WebElement professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		if (professorContainer != null) {
			List<WebElement> close = commonLibrary.isExistList(professorContainer, UIMAP_Settings.xButton, 10);
			if (close.size() > 0) {
				commonLibrary.clickButtonParentWithWait(close.get(0), "X Button");
				WebElement professorContainer1 = commonLibrary.isExist(UIMAP_Settings.professorContainer);
				if (professorContainer1 != null) {
					List<WebElement> close1 = commonLibrary.isExistList(professorContainer1, UIMAP_Settings.xButton, 10);
					if (close1.size() > 0) {
						commonLibrary.clickButtonParentWithWait(close1.get(0), "X Button");
					} else {
						report.updateTestLog("Remove all associated instructors", "No instructor is added", Status.PASS);
					}

				}
			} else {
				report.updateTestLog("Remove all associated instructors", "No instructor is added", Status.PASS);
			}

		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove all instructor
	// # Function Name : removeAllInstructors
	// # Author : seetha
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation addInstructorsOnNeed(String facultyToAdd) {
		String[] facultyToAddEach = facultyToAdd.split(";");
		WebElement professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		List<WebElement> professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
		if (professors.size() == 2) {
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			return new Interactivecitationworkstation(scriptHelper);
		} else if (professors.size() == 1) {
			int i = 0;
			String faculty = "";
			String[] primaryProfessor = professors.get(0).getText().split("\n");
			if (facultyToAddEach[0].equalsIgnoreCase(primaryProfessor[0].split("<")[0].trim()))
				faculty = facultyToAddEach[1];
			else
				faculty = facultyToAddEach[0];
			WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			wordWheelContent = null;
			do {
				commonLibrary.setDataInTextBox(shareUserName, faculty, "Enter prof's name");
				shareUserName.sendKeys(Keys.ARROW_DOWN);
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				wordWheelContent = commonLibrary.isExistNegative(UIMAP_Settings.wordWheelContent, 10);
				WebElement wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
				commonLibrary.selectFromList(wordWheelOptions, faculty);
				i++;
			} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);

			WebElement addProf = commonLibrary.isExist(UIMAP_Settings.addProf, 10);
			if (addProf != null)
				commonLibrary.clickButtonParentWithWait(addProf, "Add");
		} else {
			for (int fEach = 0; fEach < facultyToAddEach.length; fEach++) {
				int i = 0;
				WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
				wordWheelContent = null;
				do {
					commonLibrary.setDataInTextBox(shareUserName, facultyToAddEach[fEach], "Enter prof's name");
					shareUserName.sendKeys(Keys.ARROW_DOWN);
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
					wordWheelContent = commonLibrary.isExistNegative(UIMAP_Settings.wordWheelContent, 10);
					WebElement wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
					commonLibrary.selectFromList(wordWheelOptions, facultyToAddEach[fEach]);
					i++;
				} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);

				WebElement addProf = commonLibrary.isExist(UIMAP_Settings.addProf, 10);
				if (addProf != null)
					commonLibrary.clickButtonParentWithWait(addProf, "Add");
			}
		}

		WebElement saveChangesButton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 10);
		if (saveChangesButton != null)
			commonLibrary.clickButtonParentWithWait(saveChangesButton, "Save Changes");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing settings in the General
	// settings page
	// # Function Name : generalSettings
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Search generalSettings1(String settingsName, String value)

	{
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement dropdown = null;
		switch (settingsName) {
		case "History": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.HistRMDateRange, 20);
			break;
		}
		case "StartPage": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.StartPagelist, 20);
			break;
		}
		case "Number of Results Per Page": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.numberOfResultsPerPage, 20);
			break;
		}
		}

		commonLibrary.selectByVisibleText(dropdown, value);

		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting number of results to be
	// displayed from the dropdown
	// # Function Name : selectNumberOfResults
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LASettings selectNumberOfResults(String number) {
		List<WebElement> listItems = commonLibrary.isExistList(UIMAP_Settings.lilist, 50);
		for (WebElement Item : listItems) {
			WebElement general = commonLibrary.isExist(Item, UIMAP_Home.generallink, 100);
			if (general != null) {
				commonLibrary.clickButtonParentWithWait(general, "General");
				WebElement resultDropdown = commonLibrary.isExist(UIMAP_Home.resultsList, 100);
				if (resultDropdown != null) {
					// commonLibrary.selectByVisibleText(resultDropdown,
					// number);
					commonLibrary.selectByVisibleTextByValue(resultDropdown, number, number);
					break;
				}
			} else {
				report.updateTestLog("Select the value", "The value " + number + " is not selected", Status.FAIL);

			}
		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Lexis Practice Advisor®
	// # Function Name : clickLPA     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings clickLPA() {
		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_Settings.contentSwitcher);
		WebElement lexisLPA = commonLibrary.isExist(contentSwitcher, UIMAP_Settings.lexisLPA, 10);
		if (lexisLPA != null)
			commonLibrary.clickButtonParentWithWait(lexisLPA, "Lexis Practice Advisor®");
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Check Default Settings in lpa
	// # Function Name : lpaCheckDefaultSettings     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings lpaCheckDefaultSettings() {
		String bf = "Banking & Finance";
		String snapshot = "Snapshot";

		WebElement practiceAreaPg = commonLibrary.isExist(UIMAP_Settings.practiceAreaPg, 10);
		Select selectPracticeAreaPg = new Select(practiceAreaPg);
		WebElement practiceAreaPgDefaultOption = selectPracticeAreaPg.getFirstSelectedOption();
		if (practiceAreaPgDefaultOption.getText().toLowerCase().equals(bf.toLowerCase()))
			report.updateTestLog("Verify 'Banking & Finance' is the default value in drop down under heading 'Display this practice area by default when signing in'", "'Banking & Finance' is the default value in drop down under heading 'Display this practice area by default when signing in'", Status.PASS);
		else
			report.updateTestLog("Verify 'Banking & Finance' is the default value in drop down under heading 'Display this practice area by default when signing in'", "'Banking & Finance' is not the default value in drop down under heading 'Display this practice area by default when signing in' - (Using a fresh ID would resolve this issue)", Status.FAIL);

		WebElement practiceAreaStartIn = commonLibrary.isExist(UIMAP_Settings.practiceAreaStartIn, 10);
		Select selectPracticeAreaStartIn = new Select(practiceAreaStartIn);
		WebElement practiceAreaStartInDefaultOption = selectPracticeAreaStartIn.getFirstSelectedOption();
		if (practiceAreaStartInDefaultOption.getText().toLowerCase().equals(snapshot.toLowerCase()))
			report.updateTestLog("Verify 'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", "'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", Status.PASS);
		else
			report.updateTestLog("Verify 'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", "'Snapshot' is not the default value in drop down under heading 'Display browse results in this category first' - (Using a fresh ID would resolve this issue)", Status.FAIL);

		WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaSearchResults, 10);
		Select selectPracticeAreaSearchResults = new Select(practiceAreaSearchResults);
		WebElement practiceAreaSearchResultsDefaultOption = selectPracticeAreaSearchResults.getFirstSelectedOption();
		if (practiceAreaSearchResultsDefaultOption.getText().toLowerCase().equals(snapshot.toLowerCase()))
			report.updateTestLog("Verify 'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", "'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", Status.PASS);
		else
			report.updateTestLog("Verify 'Snapshot' is the default value in drop down under heading 'Display browse results in this category first'", "'Snapshot' is not the default value in drop down under heading 'Display browse results in this category first' - (Using a fresh ID would resolve this issue)", Status.FAIL);

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Change Default Settings in lpa
	// # Function Name : lpaChangeDefaultSettings     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome lpaChangeDefaultSettings(String praticeArea, String startIn, String searchResults) {

		if (!praticeArea.equals("")) {
			WebElement practiceAreaPg = commonLibrary.isExist(UIMAP_Settings.practiceAreaPg, 10);
			commonLibrary.selectByVisibleText(practiceAreaPg, praticeArea);
			report.updateTestLog("Verify '" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", "'" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", Status.PASS);
		}

		if (!startIn.equals("")) {
			WebElement practiceAreaStartIn = commonLibrary.isExist(UIMAP_Settings.practiceAreaStartIn, 10);
			commonLibrary.selectByVisibleText(practiceAreaStartIn, startIn);
			report.updateTestLog("Verify '" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", "'" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", Status.PASS);
		}

		if (!searchResults.equals("")) {
			WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaSearchResults, 10);
			commonLibrary.selectByVisibleText(practiceAreaSearchResults, searchResults);
			report.updateTestLog("Verify '" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", "'" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", Status.PASS);
		}

		WebElement saveChangesButton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 10);
		if (saveChangesButton != null)
			commonLibrary.clickButtonParentWithWait(saveChangesButton, "Save Changes to Settings & Close");
		commonLibrary.sleep(5000);

		WebElement closeButton = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 10);
		if (closeButton != null)
			commonLibrary.clickButtonParentWithWait(closeButton, "Close");

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Check Settings in lpa
	// # Function Name : lpaCheckSettings     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings lpaCheckSettings(String praticeArea, String startIn, String searchResults) {
		if (!praticeArea.equals("")) {
			WebElement practiceAreaPg = commonLibrary.isExist(UIMAP_Settings.practiceAreaPg, 10);
			Select selectPracticeAreaPg = new Select(practiceAreaPg);
			WebElement practiceAreaPgDefaultOption = selectPracticeAreaPg.getFirstSelectedOption();
			if (practiceAreaPgDefaultOption.getText().toLowerCase().equals(praticeArea.toLowerCase()))
				report.updateTestLog("Verify '" + praticeArea + "' is the value in drop down under heading 'Display this practice area by default when signing in'", praticeArea + " is the value in drop down under heading 'Display this practice area by default when signing in'", Status.PASS);
			else
				report.updateTestLog("Verify '" + praticeArea + "' is the value in drop down under heading 'Display this practice area by default when signing in'", praticeArea + " is not the value in drop down under heading 'Display this practice area by default when signing in' - (Using a fresh ID would resolve this issue)", Status.FAIL);
		}

		if (!startIn.equals("")) {
			WebElement practiceAreaStartIn = commonLibrary.isExist(UIMAP_Settings.practiceAreaStartIn, 10);
			Select selectPracticeAreaStartIn = new Select(practiceAreaStartIn);
			WebElement practiceAreaStartInDefaultOption = selectPracticeAreaStartIn.getFirstSelectedOption();
			if (practiceAreaStartInDefaultOption.getText().toLowerCase().equals(startIn.toLowerCase()))
				report.updateTestLog("Verify '" + startIn + "' is the default value in drop down under heading 'Display browse results in this category first'", startIn + " is the value in drop down under heading 'Display browse results in this category first'", Status.PASS);
			else
				report.updateTestLog("Verify '" + startIn + "' is the default value in drop down under heading 'Display browse results in this category first'", startIn + " is not the value in drop down under heading 'Display browse results in this category first' - (Using a fresh ID would resolve this issue)", Status.FAIL);
		}

		if (!searchResults.equals("")) {
			WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaSearchResults, 10);
			Select selectPracticeAreaSearchResults = new Select(practiceAreaSearchResults);
			WebElement practiceAreaSearchResultsDefaultOption = selectPracticeAreaSearchResults.getFirstSelectedOption();
			if (practiceAreaSearchResultsDefaultOption.getText().toLowerCase().equals(searchResults.toLowerCase()))
				report.updateTestLog("Verify '" + searchResults + "' is the default value in drop down under heading 'Display browse results in this category first'", searchResults + " is the value in drop down under heading 'Display browse results in this category first'", Status.PASS);
			else
				report.updateTestLog("Verify '" + searchResults + "' is the default value in drop down under heading 'Display browse results in this category first'", searchResults + " is not the value in drop down under heading 'Display browse results in this category first' - (Using a fresh ID would resolve this issue)", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Change Default Settings and Dont
	// Save in lpa
	// # Function Name : lpaChangeSettingsDontSave     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome lpaChangeSettingsDontSave(String praticeArea, String startIn, String searchResults) {
		if (!praticeArea.equals("")) {
			WebElement practiceAreaPg = commonLibrary.isExist(UIMAP_Settings.practiceAreaPg, 10);
			commonLibrary.selectByVisibleText(practiceAreaPg, praticeArea);
			report.updateTestLog("Verify '" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", "'" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", Status.PASS);
		}

		if (!startIn.equals("")) {
			WebElement practiceAreaStartIn = commonLibrary.isExist(UIMAP_Settings.practiceAreaStartIn, 10);
			commonLibrary.selectByVisibleText(practiceAreaStartIn, startIn);
			report.updateTestLog("Verify '" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", "'" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", Status.PASS);
		}

		if (!searchResults.equals("")) {
			WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaSearchResults, 10);
			commonLibrary.selectByVisibleText(practiceAreaSearchResults, searchResults);
			report.updateTestLog("Verify '" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", "'" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", Status.PASS);
		}

		WebElement closeButton = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 10);
		if (closeButton != null)
			commonLibrary.clickButtonParentWithWait(closeButton, "Close");

		commonLibrary.sleep(5000);
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText().trim();
			System.out.println("Alert data: " + alertText);
			alert.accept();
			commonLibrary.sleep(5000);
			report.updateTestLog("Verify 'Leave this page' is clicked", "'Leave this page' is clicked", Status.PASS);
		} catch (UnhandledAlertException ex) {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText().trim();
			System.out.println("Alert data: " + alertText);
			alert.accept();
			commonLibrary.sleep(5000);
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select general setting
	// # Function Name : lpaChangeSettingsDontSave     
	// # Author : seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings generalSettings() {
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");

		WebElement generalLbl = commonLibrary.isExist(UIMAP_Home.generalLabel, 100);
		if (generalLbl != null) {
			report.updateTestLog("Select general tab", "General tab is selected", Status.PASS);
		} else {
			report.updateTestLog("Select general tab", "General tab is not selected", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select radio button under narrow by
	// filter section
	// # Function Name : lpaChangeSettingsDontSave     
	// # Author :seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings selectRadioUnderNarrowBy(String option) {
		switch (option.toLowerCase()) {
		case "alpha": {
			WebElement radio = commonLibrary.isExist(UIMAP_Home.alphaRadio, 100);
			commonLibrary.setRadioButton(radio, "alphabetical order");
			break;
		}
		case "numberofresult": {
			WebElement radio = commonLibrary.isExist(UIMAP_Home.numberOfResRadio, 100);
			commonLibrary.setRadioButton(radio, "By number of results");
			break;
		}
		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select add preffered jurisdiction
	// link
	// # Function Name : lpaChangeSettingsDontSave     
	// # Author :seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings clickAddPreferredJuriLink(String linkName) {
		WebElement link = commonLibrary.isExist(UIMAP_Home.addPreferedJuriLink, 100);
		if (link.getText().equalsIgnoreCase(linkName)) {
			commonLibrary.clickJS(link, linkName);
		}
		WebElement juris = commonLibrary.isExist(UIMAP_Home.jurispopup, 100);
		if (juris != null) {
			report.updateTestLog("Verify jurisdiction popup is displayed", "Jurisdiction popup is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify jurisdiction popup is displayed", "Jurisdiction popup is not displayed", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check or uncheck jurisdiction values
	// in jurisdiction popup
	// # Function Name : lpaChangeSettingsDontSave     
	// # Author :seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LASettings checkUncheckInJurisdictionPopup(String check, String uncheck) {
		String arrCheck[] = null;
		if (check != "") {
			arrCheck = check.split(";");
			if (arrCheck.length > 0) {
				for (int i = 0; i < arrCheck.length; i++) {
					WebElement juris = commonLibrary.isExist(UIMAP_Home.jurispopup, 10);
					List<WebElement> checkboxes = commonLibrary.isExistList(juris, UIMAP_Home.chkTypeCheckbox, 10);
					for (WebElement item : checkboxes) {
						if (item.getAttribute("name").equalsIgnoreCase(arrCheck[i])) {
							commonLibrary.setCheckBox(item, true);
							report.updateTestLog("Check " + arrCheck[i] + "", arrCheck[i] + "is checked", Status.PASS);
						}
					}
				}
			}
		}
		if (uncheck != "") {
			String arrUncheck[] = uncheck.split(";");
			if (arrUncheck.length > 0) {
				for (int j = 0; j < arrUncheck.length; j++) {
					WebElement juris = commonLibrary.isExist(UIMAP_Home.jurispopup, 10);
					List<WebElement> checkboxes = commonLibrary.isExistList(juris, UIMAP_Home.chkTypeCheckbox, 10);
					for (WebElement item : checkboxes) {
						if (item.getAttribute("name").equalsIgnoreCase(arrUncheck[j])) {
							commonLibrary.setCheckBox(item, false);
							report.updateTestLog("UnCheck " + arrUncheck[j] + "", arrUncheck[j] + "is Unchecked", Status.PASS);
						}
					}
				}
			}
		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Litigation Profile Suite
	// # Function Name : clickLPS     
	// # Author : Dinesh
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings clickLPS() {
		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_Settings.contentSwitcher);
		WebElement lexisLPS = commonLibrary.isExist(contentSwitcher, UIMAP_Settings.lexisLPS, 10);
		if (lexisLPS != null)
			commonLibrary.clickButtonParentWithWait(lexisLPS, "Lexis Advance® Litigation Profile Suite");
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add preferred jurisdiction and
	// location
	// # Function Name : addPreferredJurisdictionAndLocation     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings addPreferredJurisdictionAndLocation(String filters) {
		String[] filtersList = filters.split(";");
		WebElement addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
		if (addPreferredJurLoc == null) {
			addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.edit, 10);
			if (addPreferredJurLoc != null) {
				commonLibrary.highlightElement(addPreferredJurLoc);
				commonLibrary.clickLinkWithWait(addPreferredJurLoc, "Edit");
				commonLibrary.sleep(20000);
				List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
				if (checkboxLabel.size() > 0) {
					for (WebElement item : checkboxLabel) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null && checkbox.isSelected()) {
							commonLibrary.setCheckBox(checkbox, false);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Unchecked", Status.DONE);
						}
					}
				}
				WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
				commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
				commonLibrary.sleep(20000);
			}
		}

		addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
		if (addPreferredJurLoc != null) {
			commonLibrary.highlightElement(addPreferredJurLoc);
			commonLibrary.clickLinkWithWait(addPreferredJurLoc, "Add preferred jurisdictions and locations");
			commonLibrary.sleep(20000);
			List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
			if (checkboxLabel.size() > 0) {
				for (WebElement item : checkboxLabel) {
					// WebElement checkboxParagraph =
					// commonLibrary.isExist(item,
					// UIMAP_Settings.checkboxParagraph, 10);
					if (item != null && filters.toLowerCase().trim().contains(item.getText().toLowerCase().trim())) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null) {
							commonLibrary.setCheckBox(checkbox, true);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Checked", Status.DONE);
						}
					}
				}
			}
			WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
			if (jurisdictionOk != null) {
				commonLibrary.highlightElement(jurisdictionOk);
				commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
				commonLibrary.sleep(20000);
			}
			int count = 0;
			WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem, 10);
			if (selectedItem != null) {
				List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
				if (spanText.size() > 0) {
					addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.edit, 10);
					for (WebElement item1 : spanText) {
						if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
							report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
							count++;
						}
					}
					if (count != filtersList.length) {
						report.updateTestLog("Verify " + filters + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", filters + " is not present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.FAIL);
					}
					if (addPreferredJurLoc != null) {
						report.updateTestLog("Verify Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", "Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
					}
				}
			}

		}
		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : setNarrowbyFiltersAlphabatically
	// # Function Name : setNarrowbyFiltersAlphabatically
	// # Author : Baswaraj
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LASettings setNarrowbyFiltersAlphabatically(String sorting) {

		WebElement sortfilter = commonLibrary.isExist(UIMAP_Home.sortby, 100);
		if (sortfilter != null) {
			commonLibrary.setRadioButton(sortfilter, sorting);

		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save changes to settings or
	// close settings without saving
	// # Function Name : clickSaveOrCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Search clickSaveOrCancel(String action) {
		switch (action) {
		case "Save": {
			WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
			if (submit != null && submit.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(submit, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}
			break;
		}
		case "Cancel": {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}
			break;
		}
		}
		commonLibrary.sleep(20000);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click edit button under header
	// # Function Name : clickEditButtonUnderHeader
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings clickEditButtonUnderHeader(String header) {
		if (!header.equals(" ")) {
			switch (header.toLowerCase()) {
			case "jurisdiction and location":
				WebElement jurisdictionEdit = commonLibrary.isExist(UIMAP_Settings.edit, 10);
				if (jurisdictionEdit != null) {
					commonLibrary.highlightElement(jurisdictionEdit);
					commonLibrary.clickButtonParentWithWait(jurisdictionEdit, "Edit");
					commonLibrary.sleep(20000);
				} else {
					report.updateTestLog("Click edit under jurisdiction and location", "Edit button does not present", Status.FAIL);
				}
				break;
			case "court":
				WebElement courtEdit = commonLibrary.isExist(UIMAP_Settings.courtEdit, 10);
				if (courtEdit != null) {
					commonLibrary.highlightElement(courtEdit);
					commonLibrary.clickButtonParentWithWait(courtEdit, "Edit");
					commonLibrary.sleep(20000);
				} else {
					report.updateTestLog("Click edit under jurisdiction and location", "Edit button does not present", Status.FAIL);
				}
				break;
			}
		} else {
			report.updateTestLog("Click edit under header", "No header selected", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click cancel button
	// # Function Name : clickCancelButtonInPopUp
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings clickCancelButtonInPopUp() {
		WebElement cancel = commonLibrary.isExist(UIMAP_Settings.cancel, 10);
		if (cancel != null) {
			commonLibrary.highlightElement(cancel);
			commonLibrary.clickButtonParentWithWait(cancel, "Cancel");
			commonLibrary.sleep(20000);
		} else {
			report.updateTestLog("Click Cancel button", "Cancel button does not present", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify checkbox enabled/disabled
	// # Function Name : verifyCheckboxEnabledDisabled     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings verifyCheckboxEnabledDisabled(String filters, boolean checkAction, boolean checkUncheck, boolean verifyAction, boolean enableDisable) {
		List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
		if (checkboxLabel.size() > 0) {
			if (checkAction) {
				if (checkUncheck) {
					for (WebElement item : checkboxLabel) {
						WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
						if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
							WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
							if (checkbox != null) {
								commonLibrary.setCheckBox(checkbox, checkboxParagraph.getText());
							}
						}
					}
				}
				if (!checkUncheck) {
					for (WebElement item : checkboxLabel) {
						WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
						if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
							WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
							if (checkbox != null) {
								commonLibrary.setCheckBox(checkbox, false);
								report.updateTestLog("Uncheck " + checkboxParagraph.getText(), checkboxParagraph.getText() + " is unchecked", Status.PASS);
							}
						}
					}
				}
			}
			if (verifyAction) {
				if (enableDisable) {
					for (WebElement item : checkboxLabel) {
						WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
						if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
							WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
							if (checkbox != null) {
								if (checkbox.isEnabled()) {
									report.updateTestLog("Verify " + checkboxParagraph.getText() + " is enabled", checkboxParagraph.getText() + " is enabled", Status.PASS);
								} else {
									report.updateTestLog("Verify " + checkboxParagraph.getText() + " is enabled", checkboxParagraph.getText() + " is not enabled", Status.FAIL);
								}
							}
						}
					}
				}
				if (!enableDisable) {
					for (WebElement item : checkboxLabel) {
						WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
						if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
							WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
							if (checkbox != null) {
								if (checkbox.isEnabled()) {
									report.updateTestLog("Verify " + checkboxParagraph.getText() + " is enabled", checkboxParagraph.getText() + " is not enabled", Status.FAIL);
								} else {
									report.updateTestLog("Verify " + checkboxParagraph.getText() + " is enabled", checkboxParagraph.getText() + " is enabled", Status.PASS);
								}
							}
						}
					}
				}
			}
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click OK button in popu
	// # Function Name : verifyCheckboxEnabledDisabled     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LASettings clickOkButtonInPopUp() {
		WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
		if (jurisdictionOk != null) {
			commonLibrary.highlightElement(jurisdictionOk);
			commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
			commonLibrary.sleep(20000);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click OK button in popu
	// # Function Name : verifyCheckboxEnabledDisabled     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings verifySelectedPreferredFilters(String header, String filters) {
		if (!header.equals(" ")) {
			String[] filtersList = filters.split(";");
			switch (header.toLowerCase()) {
			case "jurisdiction and location":
				int count = 0;
				WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem, 10);
				if (selectedItem != null) {
					List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
					if (spanText.size() > 0) {
						for (WebElement item1 : spanText) {
							if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
								report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
								count++;
							}
						}
						if (count != filtersList.length) {
							report.updateTestLog("Verify " + filters + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", filters + " is not present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.FAIL);
						}
					}
				} else {
					report.updateTestLog("Verify " + filters + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", "No filters selected under the section 'When displaying jurisdiction and location filters, always list these first'", Status.FAIL);
				}
				break;
			case "court":
				int count1 = 0;
				WebElement selectedCourtItem = commonLibrary.isExist(UIMAP_Settings.selectedCourtItem, 10);
				if (selectedCourtItem != null) {
					List<WebElement> spanText = commonLibrary.isExistList(selectedCourtItem, UIMAP_Settings.span, 10);
					if (spanText.size() > 0) {
						for (WebElement item1 : spanText) {
							if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
								report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying court filters, always list these first'", item1.getText() + " is present under the section 'When displaying court filters, always list these first'", Status.PASS);
								count1++;
							}
						}
						if (count1 != filtersList.length) {
							report.updateTestLog("Verify " + filters + " is present under the section 'When displaying court filters, always list these first'", filters + " is not present under the section 'When displaying court filters, always list these first'", Status.FAIL);
						}
					}
				} else {
					report.updateTestLog("Verify " + filters + " is present under the section 'When displaying court filters, always list these first'", "No filters selected under the section 'When displaying court filters, always list these first'", Status.FAIL);
				}
				break;
			}
		} else {
			report.updateTestLog("Verify selected preferred filter under header", "No header selected", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if ((browsername.contains("internet")) ||
		// (browsername.contains("internet") && version.contains("8")))
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
		// if ((browsername.contains("internet")) ||
		// (browsername.contains("internet") && version.contains("8")))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// }
		// if (lnkSignOut != null) {
		// if ((browsername.contains("internet")) ||
		// (browsername.contains("internet") && version.contains("8")))
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
	// # Function Description : Function to addPreferredCourts
	// # Function Name : addPreferredCourts     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings clickAddPreferredFilter(String filters, String header) {
		String[] filtersList = filters.split(";");
		WebElement addPreferredFilter = null;
		switch (header.toLowerCase()) {
		case "jurisdiction": {
			addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
			break;
		}
		case "court": {
			addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
			break;
		}
		}
		if (addPreferredFilter != null) {
			commonLibrary.clickLinkWithWait(addPreferredFilter, addPreferredFilter.getText());
			commonLibrary.sleep(20000);
			WebElement dilagbox = commonLibrary.isExist(UIMAP_Settings.settingsDialog, 10);
			WebElement dilagboxHeader = commonLibrary.isExist(dilagbox, UIMAP_Settings.dilogboxHeader, 10);
			WebElement section = commonLibrary.isExist(dilagbox, UIMAP_Settings.section, 10);
			WebElement header1 = commonLibrary.isExist(section, UIMAP_Settings.headerInSection, 10);
			if (dilagboxHeader != null & header1 != null && header1.getText().contains("three")) {
				report.updateTestLog("Verify popup opens with list of " + header + " in Settings page", "Popup opens with list of " + header + " in Settings page", Status.PASS);
				report.updateTestLog("Verification of Pop Up", "Pop Up is displayed with " + header + " header and with 'X' button", Status.PASS);
				report.updateTestLog("Verification of Pop Up", "'Select upto three preferred '" + header + "' is displayed in the Pop Up", Status.PASS);
			} else {
				report.updateTestLog("Verification of Pop Up", "Pop up verification not done", Status.FAIL);
			}

			List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
			if (checkboxLabel.size() > 0) {

				report.updateTestLog("Verification of Pop Up", "'" + header + "' with check boxes are displayed in the Pop Up", Status.PASS);

				for (WebElement item : checkboxLabel) {
					WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
					if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null) {
							commonLibrary.setCheckBox(checkbox, true);
						}
					}
				}
			}
			WebElement buttonOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
			WebElement buttonCancel = commonLibrary.isExist(UIMAP_Settings.jurisdictionCancel, 10);
			if (buttonOk != null && buttonCancel != null) {
				report.updateTestLog("Verification of Pop Up", "'Ok' and 'Cancel' buttons are displayed in the Pop Up", Status.PASS);
				commonLibrary.clickButtonParentWithWait(buttonOk, "OK");
			}
			commonLibrary.sleep(20000);
			int count = 0;
			WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem1, 10);
			if (selectedItem != null) {
				List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
				if (spanText.size() > 0) {
					switch (header.toLowerCase()) {
					case "jurisdiction": {
						addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.edit, 10);
						break;
					}
					case "court": {
						addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
						break;
					}
					}
					for (WebElement item1 : spanText) {
						if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
							report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying " + header + " filters, always list these first'", item1.getText() + " is present under the section 'When displaying " + header + " filters, always list these first'", Status.PASS);
							count++;
						}
					}
					if (count != filtersList.length) {
						report.updateTestLog("Verify " + filters + " is present under the section 'When displaying " + header + " filters, always list these first'", filters + " is not present under the section 'When displaying " + header + " filters, always list these first'", Status.FAIL);
					}
					if (addPreferredFilter != null) {
						report.updateTestLog("Verify Edit button is present under the section 'When displaying " + header + " filters, always list these first'", "Edit button is present under the section 'When displaying " + header + " filters, always list these first'", Status.PASS);
					}
				}
			}

		}
		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add click on navigateToResearchPage
	// # Function Name : navigateToResearchPage     
	// # Author : Baswaraj
	// # Date Created : sep'15
	// #*****************************************************************************************************************************
	public LASettings navigateToResearchPage() {
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

		// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

		WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
		if (PgTitle != null) {
			report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

		}
		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to addPreferredCourts
	// # Function Name : addPreferredCourts     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings addPreferredCourts(String filters) {
		String[] filtersList = filters.split(";");
		WebElement addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
		if (addPreferredCourts == null) {
			addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
			if (addPreferredCourts != null) {
				commonLibrary.clickLinkWithWait(addPreferredCourts, "Edit");
				commonLibrary.sleep(20000);
				List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
				if (checkboxLabel.size() > 0) {
					for (WebElement item : checkboxLabel) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null && checkbox.isSelected()) {
							commonLibrary.setCheckBox(checkbox, false);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Unchecked", Status.DONE);
						}
					}
				}
				WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
				commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
				commonLibrary.sleep(20000);
			}
		}
		int counter = 0;
		addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
		if (addPreferredCourts != null) {
			commonLibrary.clickLinkWithWait(addPreferredCourts, "Add preferred Courts");
			commonLibrary.sleep(20000);
			List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
			if (checkboxLabel.size() > 0) {
				for (WebElement item : checkboxLabel) {
					WebElement checkboxParagraph = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
					if (checkboxParagraph != null && filters.toLowerCase().trim().contains(checkboxParagraph.getText().toLowerCase().trim())) {
						counter++;
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null) {
							commonLibrary.setCheckBox(checkbox, true);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Checked", Status.DONE);
						}
						if (counter == 4) {
							if (!checkbox.isSelected()) {
								report.updateTestLog("Verify '" + checkboxParagraph.getText() + "' is selected", "'" + checkboxParagraph.getText() + "' is not selected because already 3 selected", Status.PASS);
							}
						}
					}
				}
			}
			WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
			commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
			commonLibrary.sleep(20000);
			int count = 0;
			WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem1, 10);
			if (selectedItem != null) {
				List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
				if (spanText.size() > 0) {
					addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
					for (WebElement item1 : spanText) {
						if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
							report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying court filters, always list these first'", item1.getText() + " is present under the section 'When displaying court filters, always list these first'", Status.PASS);
							count++;
						}
					}
					int k = filtersList.length;
					if (counter == 4) {
						k = filtersList.length - 1;
					}
					if (count != k) {
						report.updateTestLog("Verify " + filters + " is present under the section 'When displaying court filters, always list these first'", filters + " is not present under the section 'When displaying court filters, always list these first'", Status.FAIL);
					}
					if (addPreferredCourts != null) {
						report.updateTestLog("Verify Edit button is present under the section 'When displaying court filters, always list these first'", "Edit button is present under the section 'When displaying court filters, always list these first'", Status.PASS);
					}
				}
			}

		}
		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clearAllPreferredFilter
	// # Function Name : clearAllPreferredFilter     
	// # Author : Baswaraj
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings clearAllPreferredFilter(String header) {
		WebElement addPreferredFilter = null;
		switch (header.toLowerCase()) {
		case "jurisdiction and location": {
			addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
			if (addPreferredFilter == null) {
				addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.edit, 10);
			}
			break;
		}
		case "court": {
			addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
			if (addPreferredFilter == null) {
				addPreferredFilter = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
			}
			break;
		}
		}

		if (addPreferredFilter != null) {
			commonLibrary.clickLinkWithWait(addPreferredFilter, addPreferredFilter.getText());
			commonLibrary.sleep(20000);
			List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
			if (checkboxLabel.size() > 0) {
				for (WebElement item : checkboxLabel) {
					WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
					if (checkbox != null) {
						commonLibrary.setCheckBox(checkbox, false);
					}
				}
			}
			WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
			commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
			commonLibrary.sleep(20000);
		}

		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filter message
	// header and add preferred link
	// # Function Name : verifyPreferredFilterMessage     
	// # Author : Anbarasan
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings verifyPreferredFilterMessage(String text) {
		boolean flag = false;
		WebElement formContainer = commonLibrary.isExist(UIMAP_Settings.formcontainer, 10);
		List<WebElement> headerTag = commonLibrary.isExistList(formContainer, UIMAP_Settings.headerInSection, 10);
		if (headerTag.size() > 0) {
			for (WebElement item : headerTag) {
				if (item.getText().trim().toLowerCase().contains(text.trim().toLowerCase())) {
					report.updateTestLog("Verify " + text + " displays", text + " is displayed", Status.PASS);
					flag = true;
				}
			}
			if (!flag) {
				report.updateTestLog("Verify " + text + " displays", text + " is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify " + text + " displays", text + " is not displayed", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify preferred filters under
	// section When displaying preferred filters
	// # Function Name : verifyFiltersUnderSection     
	// # Author : Anbarasan
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings verifyFiltersUnderSection(String header, String filters) {
		String[] filtersList = filters.split(";");
		if (!header.contains(" ")) {
			switch (header.toLowerCase()) {
			case "jurisdiction and location": {
				int count = 0;
				WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem, 10);
				if (selectedItem != null) {
					List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
					if (spanText.size() > 0) {
						WebElement addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.edit, 10);
						for (WebElement item1 : spanText) {
							if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
								report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", item1.getText() + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
								count++;
							}
						}
						if (count != filtersList.length) {
							report.updateTestLog("Verify " + filters + " is present under the section 'When displaying jurisdiction and location filters, always list these first'", filters + " is not present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.FAIL);
						}
						if (addPreferredJurLoc != null) {
							report.updateTestLog("Verify Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", "Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
						}
					}
				}
				break;
			}
			case "court": {
				int count = 0;
				WebElement selectedItem = commonLibrary.isExist(UIMAP_Settings.selectedItem1, 10);
				if (selectedItem != null) {
					List<WebElement> spanText = commonLibrary.isExistList(selectedItem, UIMAP_Settings.span, 10);
					if (spanText.size() > 0) {
						WebElement addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
						for (WebElement item1 : spanText) {
							if (filters.toLowerCase().trim().contains(item1.getText().toLowerCase().trim())) {
								report.updateTestLog("Verify " + item1.getText() + " is present under the section 'When displaying court filters, always list these first'", item1.getText() + " is present under the section 'When displaying court filters, always list these first'", Status.PASS);
								count++;
							}
						}
						if (count != filtersList.length) {
							report.updateTestLog("Verify " + filters + " is present under the section 'When displaying court filters, always list these first'", filters + " is not present under the section 'When displaying court filters, always list these first'", Status.FAIL);
						}
						if (addPreferredCourts != null) {
							report.updateTestLog("Verify Edit button is present under the section 'When displaying court filters, always list these first'", "Edit button is present under the section 'When displaying court filters, always list these first'", Status.PASS);
						}
					}
				}
				break;
			}
			}
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify add preferred filter link is
	// present
	// # Function Name : verifyAddPreferredFilterLink     
	// # Author : Anbarasan
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public LASettings verifyAddPreferredFilterLink(String header) {
		if (!header.contains(" ")) {
			switch (header.toLowerCase()) {
			case "jurisdiction and location": {
				WebElement addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
				if (addPreferredJurLoc != null) {
					report.updateTestLog("Verify " + addPreferredJurLoc.getText() + " link is present", addPreferredJurLoc.getText() + " link is present", Status.PASS);
				} else {
					report.updateTestLog("Verify Add preferred jurisdictions and locations link is present", "Add preferred jurisdictions and locations link is not present", Status.FAIL);
				}
				break;
			}
			case "court": {
				WebElement addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
				if (addPreferredCourts != null) {
					report.updateTestLog("Verify " + addPreferredCourts.getText() + " link is present", addPreferredCourts.getText() + " link is present", Status.PASS);
				} else {
					report.updateTestLog("Verify Add preferred courts link is present", "Add preferred courts link is not present", Status.FAIL);
				}
				break;
			}
			}
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to selet display results
	// present
	// # Function Name : setCategoryInSettings     
	// # Author : Seetha
	// # Date Created : Sept'15
	// #*****************************************************************************************************************************
	public Search setCategoryInSettings(String value1, String value) {
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");
		WebElement listbox = commonLibrary.isExist(By.cssSelector("select[id='settings_research_searchresults']"), 20);
		commonLibrary.selectByVisibleTextByValue(listbox, value1, value);
		commonLibrary.scrollDown(1000);
		WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
		if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
			commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

		else {
			report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.DONE);
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for performing settings in the General
	// settings page
	// # Function Name : generalSettings1
	// # Author : Kirthika
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Research generalSettings2(String settingsName, String value)

	{
		WebElement general = commonLibrary.isExist(UIMAP_Home.generallink, 100);
		if (general != null)
			commonLibrary.clickButtonParentWithWait(general, "General");
		WebElement dropdown = null;
		switch (settingsName) {
		case "History": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.HistRMDateRange, 20);
			break;
		}
		case "StartPage": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.StartPagelist, 20);
			break;
		}
		case "Number of Results Per Page": {
			dropdown = commonLibrary.isExist(UIMAP_Settings.numberOfResultsPerPage, 20);
			break;
		}
		}

		commonLibrary.selectByVisibleText(dropdown, value);
		if (commonLibrary.selectIsSelected(dropdown, value))
			report.updateTestLog("Select " + value + " from " + settingsName + " dropdown in general Settings Page.", value + " from " + settingsName + " dropdown is selected in general Settings Page.", Status.DONE);
		else
			report.updateTestLog("Select " + value + " from " + settingsName + " dropdown in general Settings Page.", value + " from " + settingsName + " dropdown is not selected in general Settings Page.", Status.FAIL);
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}
		pageCheck.ajaxWait(driver);
		return new Research(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save changes to settings or
	// close settings without saving
	// # Function Name : clickSaveOrCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Home clickSaveOrCancelSettings(String action) {
		generalFunctions.clickSaveOrCancel(action);
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Tax
	// # Function Name : clickSettingsLexisTax     
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LASettings clickSettingsLexisTax() {
		WebElement contentSwitcher = commonLibrary.isExist(UIMAP_Settings.contentSwitcher);
		WebElement lexisTax = commonLibrary.isExist(contentSwitcher, UIMAP_Settings.lexisTax, 10);
		if (lexisTax != null) {
			commonLibrary.clickButtonParentWithWait(lexisTax, "Lexis Advance® Tax");
			report.updateTestLog("Click LAT in general Settings Page", lexisTax.getText() + " link has been clicked in general Settings Page.", Status.PASS);
		} else {
			report.updateTestLog("Click LAT in general Settings Page", "Lexis Advance® Tax link has not been clicked in general Settings Page.", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select practice area in Lexis Tax
	// # Function Name : selectLexisTaxPracticeArea     
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LASettings selectLexisTaxPracticeArea(String PraticeArea) {
		WebElement lexisTaxTabArea = commonLibrary.isExist(UIMAP_Settings.lexisTaxTabArea);
		WebElement lexisTaxdrpdwn = commonLibrary.isExist(lexisTaxTabArea, UIMAP_Settings.lexisTaxDrpdwn, 10);
		if (lexisTaxdrpdwn != null) {
			commonLibrary.selectByVisibleText(lexisTaxdrpdwn, PraticeArea);
			report.updateTestLog("Select " + PraticeArea + " from LAT dropdown in general Settings Page", PraticeArea + " from LAT dropdown is selected in general Settings Page.", Status.PASS);
		} else {
			report.updateTestLog("Select " + PraticeArea + " from LAT dropdown in general Settings Page", PraticeArea + " from LAT dropdown is not selected in general Settings Page.", Status.FAIL);
		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select title view or full view
	// # Function Name : selectResultView     
	// # Author : Seetha
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LPAHome selectResultView(String option) {
		if (option.equalsIgnoreCase("title")) {
			WebElement resView = commonLibrary.isExist(UIMAP_Settings.resultviewMin);
			if (resView != null) {
				commonLibrary.clickButtonParentWithWait(resView, "Title View");
			} else
				report.updateTestLog("select " + option + " view", "" + option + " view is not selected", Status.FAIL);
		} else if (option.equalsIgnoreCase("full")) {
			WebElement resView = commonLibrary.isExist(UIMAP_Settings.resultviewMax);
			if (resView != null) {
				commonLibrary.clickButtonParentWithWait(resView, "Full View");
			} else
				report.updateTestLog("select " + option + " view", "" + option + " view is not selected", Status.FAIL);
		}

		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
		} else {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
			}
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add instructor and verify.
	// # Function Name : addInstructorsVerify
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LASettings addInstructorsVerify(String facultyToAdd) {
		WebElement professorContainer = commonLibrary.isExist(UIMAP_Settings.professorContainer);
		List<WebElement> professors = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
		// Checking if instructors are already added.
		if (professors.size() == 2) {
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			return new LASettings(scriptHelper);
		}
		// Checking if one instructor is already added.
		else if (professors.size() == 1) {
			int i = 0;

			WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			wordWheelContent = null;
			boolean wordWheelFlag = false;
			do {
				// Entering instructor Name.
				commonLibrary.setDataInTextBox(shareUserName, facultyToAdd, "Enter prof's name");
				shareUserName.sendKeys(Keys.ARROW_DOWN);
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				if (shareUserName.getAttribute("value").contains(facultyToAdd)) {
					report.updateTestLog("Verify we are able to enter text into text box", "We are able to enter text into text box", Status.PASS);
				}

				wordWheelContent = commonLibrary.isExistNegative(UIMAP_Settings.wordWheelContent, 10);
				WebElement wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
				if (wordWheelOptions != null) {
					wordWheelFlag = true;
					report.updateTestLog("Verify the wordwheel displays.", "The wordwheel displays.", Status.PASS);
				}
				// Selecting instructor from word wheel.
				wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
				commonLibrary.selectFromList(wordWheelOptions, facultyToAdd);
				i++;
			} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);

			if (!wordWheelFlag)
				report.updateTestLog("Verify the wordwheel displays.", "The wordwheel does not display.", Status.FAIL);

			shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			// Checking if selected option displays.
			if (shareUserName.getAttribute("value").contains(facultyToAdd))
				report.updateTestLog("Verify Selected professor displays", "Selected professor displays", Status.PASS);
			else
				report.updateTestLog("Verify Selected professor displays", "Selected professor does not displays", Status.FAIL);

			WebElement addProf = commonLibrary.isExist(UIMAP_Settings.addProf, 10);
			if (addProf != null)
				commonLibrary.clickButtonParentWithWait(addProf, "Add");

			// Checking if instructor is added.
			List<WebElement> professorsNew = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
			if (professorsNew.size() > 1) {
				if (professorsNew.get(1).getText().toLowerCase().contains(facultyToAdd.toLowerCase()) && professorsNew.get(1).getText().toLowerCase().contains("alternate")) {
					report.updateTestLog("Verify the added Professor has the term 'alternate' suffixed to the name", "The added Professor has the term 'alternate' suffixed to the name", Status.PASS);
				} else
					report.updateTestLog("Verify the added Professor has the term 'alternate' suffixed to the name", "The added Professor does not have the term 'alternate' suffixed to the name", Status.FAIL);

			} else
				report.updateTestLog("Verify the added Professor has the term 'primary' suffixed to the name", "Professor adding failed.", Status.FAIL);

		}
		// Proceeding if no instructor is added.
		else {

			int i = 0;
			WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			wordWheelContent = null;
			boolean wordWheelFlag = false;
			do {
				// Entering instructor Name.
				commonLibrary.setDataInTextBox(shareUserName, facultyToAdd, "Enter prof's name");
				shareUserName.sendKeys(Keys.ARROW_DOWN);
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				wordWheelContent = commonLibrary.isExistNegative(UIMAP_Settings.wordWheelContent, 10);
				WebElement wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
				if (wordWheelOptions != null) {
					wordWheelFlag = true;
					report.updateTestLog("Verify the wordwheel displays.", "The wordwheel displays.", Status.PASS);
				}
				wordWheelOptions = commonLibrary.isExist(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
				commonLibrary.selectFromList(wordWheelOptions, facultyToAdd);
				i++;
			} while ((wordWheelContent == null && !(wordWheelContent.isDisplayed())) && i < 3);

			if (!wordWheelFlag)
				report.updateTestLog("Verify the wordwheel displays.", "The wordwheel does not display.", Status.FAIL);

			shareUserName = commonLibrary.isExistNegative(UIMAP_Settings.shareUserName, 10);
			// Checking if selected option displays.
			if (shareUserName.getAttribute("value").contains(facultyToAdd))
				report.updateTestLog("Verify Selected professor displays", "Selected professor displays", Status.PASS);
			else
				report.updateTestLog("Verify Selected professor displays", "Selected professor does not displays", Status.FAIL);

			WebElement addProf = commonLibrary.isExist(UIMAP_Settings.addProf, 10);
			if (addProf != null)
				commonLibrary.clickButtonParentWithWait(addProf, "Add");

			List<WebElement> professorsNew = commonLibrary.isExistList(professorContainer, UIMAP_Settings.professor, 10);
			// Checking if instructor is added.
			if (professorsNew.size() > 0) {
				if (professorsNew.get(0).getText().toLowerCase().contains(facultyToAdd.toLowerCase()) && professorsNew.get(0).getText().toLowerCase().contains("primary")) {
					report.updateTestLog("Verify the added Professor has the term 'primary' suffixed to the name", "The added Professor has the term 'primary' suffixed to the name", Status.PASS);
				} else
					report.updateTestLog("Verify the added Professor has the term 'primary' suffixed to the name", "The added Professor does not have the term 'primary' suffixed to the name", Status.FAIL);

			} else
				report.updateTestLog("Verify the added Professor has the term 'primary' suffixed to the name", "Professor adding failed.", Status.FAIL);

		}

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to save settings and close.
	// # Function Name : saveSettingsAndClose     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation saveSettingsAndCloseToICW() {
		generalFunctions.saveSettingsAndClose();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Notification option
	// present and status of its checkbox.
	// # Function Name : verifyNotificationSettings     
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LASettings verifyNotificationSettings(String text, boolean state) {
		WebElement generalSettingsCont = commonLibrary.isExistNegative(UIMAP_Settings.generalSettingsCont, 10);
		List<WebElement> notificationOptionItems = commonLibrary.isExistList(generalSettingsCont, UIMAP_Settings.notificationOptionItems, 10);
		boolean flag = false;
		if (generalSettingsCont != null) {
			report.updateTestLog("Verify 'Notification Settings' list displays.", " 'Notification Settings' list displays.", Status.PASS);
			for (WebElement item : notificationOptionItems) {
				if (item.getText().toLowerCase().contains(text.toLowerCase())) {
					report.updateTestLog("Verify " + text + " is displayed under Notification Settings.", text + " is displayed under Notification Settings.", Status.PASS);
					flag = true;
					if (state) {
						WebElement checkbox = commonLibrary.isExistNegative(item, UIMAP_Settings.checkbox, 5);
						if (checkbox != null) {
							if (checkbox.isSelected() == state) {
								report.updateTestLog("Verify the checkbox for " + text + " is selected.", "The checkbox for " + text + " is selected.", Status.PASS);

							} else
								report.updateTestLog("Verify the checkbox for " + text + " is selected.", "The checkbox for " + text + " is not selected.", Status.FAIL);
						} else
							report.updateTestLog("Verify the checkbox for " + text + " is selected.", "The checkbox for " + text + " is not present.", Status.FAIL);
					} else {
						WebElement checkbox = commonLibrary.isExistNegative(item, UIMAP_Settings.checkbox, 5);
						if (checkbox != null) {
							if (checkbox.isSelected() == state) {
								report.updateTestLog("Verify the checkbox for " + text + " is not selected.", "The checkbox for " + text + " is not selected.", Status.PASS);

							} else
								report.updateTestLog("Verify the checkbox for " + text + " is not selected.", "The checkbox for " + text + " is selected.", Status.FAIL);
						} else
							report.updateTestLog("Verify the checkbox for " + text + " is not selected.", "The checkbox for " + text + " is not present.", Status.FAIL);

					}
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + text + " is displayed under Notification Settings.", text + " is not displayed under Notification Settings.", Status.FAIL);
		} else
			report.updateTestLog("Verify 'Notification Settings' list displays.", " 'Notification Settings' list does not display.", Status.FAIL);
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Change Default Settings in lpa
	// # Function Name : lpaChangeDefaultSettings     
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LPAHome lpaChangeDefaultSettingsAndRetainFilter(String praticeArea, String startIn, String searchResults, String check) {

		if (!praticeArea.equals("")) {
			WebElement practiceAreaPg = commonLibrary.isExist(UIMAP_Settings.practiceAreaPg, 10);
			commonLibrary.selectByVisibleText(practiceAreaPg, praticeArea);
			report.updateTestLog("Verify '" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", "'" + praticeArea + "' is set in drop down under heading 'Display this practice area by default when signing in'", Status.PASS);
		}

		if (!startIn.equals("")) {
			WebElement practiceAreaStartIn = commonLibrary.isExist(UIMAP_Settings.practiceAreaStartIn, 10);
			commonLibrary.selectByVisibleText(practiceAreaStartIn, startIn);
			report.updateTestLog("Verify '" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", "'" + startIn + "' is set in drop down under heading 'Display browse results in this category first'", Status.PASS);
		}

		if (!searchResults.equals("")) {
			WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaSearchResults, 10);
			commonLibrary.selectByVisibleText(practiceAreaSearchResults, searchResults);
			report.updateTestLog("Verify '" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", "'" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", Status.PASS);
		}

		if (!check.equalsIgnoreCase("")) {
			WebElement retain = commonLibrary.isExist(UIMAP_LPAHome.retainFilter, 20);

			if (retain != null) {
				if (check.equalsIgnoreCase("check")) {
					if (!retain.isSelected()) {
						commonLibrary.clickButtonParentWithWait(retain, "Retain search filter");
					}
				} else if (check.equals("uncheck")) {
					if (retain.isSelected()) {
						commonLibrary.clickButtonParentWithWait(retain, "Retain search filter");
					}
				} else
					report.updateTestLog("" + check + " the retain search filter checkbox", "Give check or uncheck as input", Status.FAIL);
			} else
				report.updateTestLog("" + check + " the retain search filter checkbox", "Checkbox is not present", Status.FAIL);
		}

		WebElement saveChangesButton = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 10);
		if (saveChangesButton != null)
			commonLibrary.clickButtonParentWithWait(saveChangesButton, "Save Changes to Settings & Close");
		commonLibrary.sleep(5000);

		WebElement closeButton = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 10);
		if (closeButton != null)
			commonLibrary.clickButtonParentWithWait(closeButton, "Close");

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save
	// # Function Name : clickSaveOrCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public Document clickSave() {
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
			WebElement searchBox = null;
			int count = 0;
			do {
				commonLibrary.sleep(10000);
				searchBox = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 5);
				count++;
			} while (searchBox == null && count < 10);
		} else {
			report.updateTestLog("Click on Save/Cance Button", "Save button is not enaabled clicking on browser back", Status.PASS);
			commonLibrary.clickBrowserBack();
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do settings in LexisAdvance Research
	// Page
	// # Function Name : VerifySettingsPage     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LASettings researchPageSettingsWithoutSave(String strOption, Boolean isCheck) {

		// Click Research link
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

		// Verification Point: Lexis Advance® Research Page is displayed
		WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
		if (PgTitle != null) {
			report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

		}

		// select different radio button based on the options
		WebElement rdoCaseOption = null;
		switch (strOption) {
		case "Overview": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
			break;
		}
		case "Show Terms": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
			break;
		}
		case "Show Extract": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
			break;
		}
		case "Recently Viewed Icon": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
			break;
		}
		case "Standard": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.standardRadio, 10);
			break;
		}
		case "Expanded": {
			rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.expandedRadio, 10);
			break;
		}
		}

		if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon")) {

			commonLibrary.setRadioButton(rdoCaseOption, strOption);
			report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is selected from Category/Other options", Status.PASS);

		} else if (strOption.equals("Recently Viewed Icon")) {

			if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {

				if (isCheck)
					report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
				else
					report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);

			} else
				report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);

		} else {
			report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

		}
		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save
	// # Function Name : clickSaveOrCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LPAResults clickSaveLPA() {
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
			WebElement searchBox = null;
			int count = 0;
			do {
				commonLibrary.sleep(10000);
				searchBox = commonLibrary.isExistNegative(UIMAP_Document.txtStudentDocumentHeading, 5);
				count++;
			} while (searchBox == null && count < 10);
		} else {
			report.updateTestLog("Click on Save/Cance Button", "Save button is not enaabled clicking on browser back", Status.PASS);
			commonLibrary.clickBrowserBack();
		}

		return new LPAResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save changes to settings or
	// close settings without saving
	// # Function Name : clickSaveOrCancelReturnLAT
	// # Author : Anbarasan
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickSaveOrCancelReturnLAT(String action) {
		switch (action) {
		case "Save": {
			WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
			if (submit != null && submit.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(submit, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}
			break;
		}
		case "Cancel": {
			WebElement cancel = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 100);
			if (cancel != null && cancel.isEnabled()) {
				commonLibrary.clickButtonParentWithWait(cancel, "submit");
				WebElement searchBox = null;
				int count = 0;
				do {
					commonLibrary.sleep(10000);
					searchBox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
					count++;
				} while (searchBox == null && count < 40);
			}
			break;
		}
		}
		commonLibrary.sleep(20000);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save
	// # Function Name : clickSaveReturnLAT
	// # Author : Anbarasan
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickSaveReturnLAT() {
		WebElement submit = commonLibrary.isExist(UIMAP_Settings.savechangesbutton, 100);
		if (submit != null && submit.isEnabled()) {
			commonLibrary.clickButtonParentWithWait(submit, "submit");
			WebElement searchBox = null;
			int count = 0;
			do {
				commonLibrary.sleep(10000);
				searchBox = commonLibrary.isExistNegative(UIMAP_Home.txtIdSearch, 3);
				count++;
			} while (searchBox == null && count < 40);
		} else {
			report.updateTestLog("Click on Save/Cance Button", "Save button is not enaabled clicking on browser back", Status.PASS);
			commonLibrary.clickBrowserBack();
		}

		return new LexisAdvanceTax(scriptHelper);
	}


	// #*****************************************************************************************************************************
	// # Function Description : Function to click save changes to settings or
	// close settings without saving
	// # Function Name : clickSaveOrCancelSettings1
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPAHome clickSaveOrCancelSettings1(String action) {
		generalFunctions.clickSaveOrCancel(action);
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add preferred jurisdiction and
	// location
	// # Function Name : addPreferredJurisdictionAndLocation     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public LASettings clearPreferredJurisdictionAndLocation(String filters) {

		WebElement addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
		if (addPreferredJurLoc == null) {
			addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.edit, 10);
			if (addPreferredJurLoc != null) {

				commonLibrary.highlightElement(addPreferredJurLoc);
				commonLibrary.clickLinkWithWait(addPreferredJurLoc, "Edit");
				commonLibrary.sleep(20000);
				List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
				if (checkboxLabel.size() > 0) {
					for (WebElement item : checkboxLabel) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null && checkbox.isSelected()) {
							commonLibrary.setCheckBox(checkbox, false);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Unchecked", Status.DONE);
						}
					}
				}
				WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
				commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
				commonLibrary.sleep(20000);
			}
		}

		addPreferredJurLoc = commonLibrary.isExist(UIMAP_Settings.addPreferredJurLoc, 10);
		/*
		 * if (addPreferredJurLoc != null) {
		 * commonLibrary.highlightElement(addPreferredJurLoc);
		 * commonLibrary.clickLinkWithWait(addPreferredJurLoc,
		 * "Add preferred jurisdictions and locations");
		 * commonLibrary.sleep(20000); List<WebElement> checkboxLabel =
		 * commonLibrary.isExistList( UIMAP_Settings.checkboxLabel, 10); if
		 * (checkboxLabel.size() > 0) { for (WebElement item : checkboxLabel) {
		 * // WebElement checkboxParagraph = // commonLibrary.isExist(item, //
		 * UIMAP_Settings.checkboxParagraph, 10); if (item != null && filters
		 * .toLowerCase() .trim() .contains(
		 * item.getText().toLowerCase().trim())) { WebElement checkbox =
		 * commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10); if
		 * (checkbox != null) { commonLibrary.setCheckBox(checkbox, true);
		 * report.updateTestLog( "Set Checkbox '" + item.getText() + "'", "'" +
		 * item.getText() + "' Checkbox is Checked", Status.DONE); } } } }
		 * WebElement jurisdictionOk = commonLibrary.isExist(
		 * UIMAP_Settings.jurisdictionOk, 10); if (jurisdictionOk != null) {
		 * commonLibrary.highlightElement(jurisdictionOk);
		 * commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
		 * commonLibrary.sleep(20000); } int count = 0; WebElement selectedItem
		 * = commonLibrary.isExist( UIMAP_Settings.selectedItem, 10); if
		 * (selectedItem != null) { List<WebElement> spanText =
		 * commonLibrary.isExistList( selectedItem, UIMAP_Settings.span, 10); if
		 * (spanText.size() > 0) { addPreferredJurLoc = commonLibrary.isExist(
		 * UIMAP_Settings.edit, 10); for (WebElement item1 : spanText) { if
		 * (filters.toLowerCase().trim()
		 * .contains(item1.getText().toLowerCase().trim())) {
		 * report.updateTestLog( "Verify " + item1.getText() +
		 * " is present under the section 'When displaying jurisdiction and location filters, always list these first'"
		 * , item1.getText() +
		 * " is present under the section 'When displaying jurisdiction and location filters, always list these first'"
		 * , Status.PASS); count++; } } if (count != filtersList.length) {
		 * report.updateTestLog( "Verify " + filters +
		 * " is present under the section 'When displaying jurisdiction and location filters, always list these first'"
		 * , filters +
		 * " is not present under the section 'When displaying jurisdiction and location filters, always list these first'"
		 * , Status.FAIL); }
		 */
		if (addPreferredJurLoc != null) {
			report.updateTestLog("Verify Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", "Edit button is present under the section 'When displaying jurisdiction and location filters, always list these first'", Status.PASS);
		}

		return new LASettings(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clearPreferredCourts
	// # Function Name : clearPreferredCourts     
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LASettings clearPreferredCourts(String filters) {

		WebElement addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.addPreferredCourts, 10);
		if (addPreferredCourts == null) {
			addPreferredCourts = commonLibrary.isExist(UIMAP_Settings.editCourts, 10);
			if (addPreferredCourts != null) {
				commonLibrary.clickLinkWithWait(addPreferredCourts, "Edit");
				commonLibrary.sleep(20000);
				List<WebElement> checkboxLabel = commonLibrary.isExistList(UIMAP_Settings.checkboxLabel, 10);
				if (checkboxLabel.size() > 0) {
					for (WebElement item : checkboxLabel) {
						WebElement checkbox = commonLibrary.isExist(item, UIMAP_Settings.checkbox, 10);
						if (checkbox != null && checkbox.isSelected()) {
							commonLibrary.setCheckBox(checkbox, false);
							report.updateTestLog("Set Checkbox '" + item.getText() + "'", "'" + item.getText() + "' Checkbox is Unchecked", Status.DONE);
						}
					}
				}
				WebElement jurisdictionOk = commonLibrary.isExist(UIMAP_Settings.jurisdictionOk, 10);
				commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
				commonLibrary.sleep(20000);
			}
		}
		/*
		 * int counter = 0; addPreferredCourts = commonLibrary.isExist(
		 * UIMAP_Settings.addPreferredCourts, 10); if (addPreferredCourts !=
		 * null) { commonLibrary.clickLinkWithWait(addPreferredCourts,
		 * "Add preferred Courts"); commonLibrary.sleep(20000); List<WebElement>
		 * checkboxLabel = commonLibrary.isExistList(
		 * UIMAP_Settings.checkboxLabel, 10); if (checkboxLabel.size() > 0) {
		 * for (WebElement item : checkboxLabel) { WebElement checkboxParagraph
		 * = commonLibrary.isExist(item, UIMAP_Settings.checkboxParagraph, 10);
		 * if (checkboxParagraph != null && filters .toLowerCase() .trim()
		 * .contains( checkboxParagraph.getText() .toLowerCase().trim())) {
		 * counter++; WebElement checkbox = commonLibrary.isExist(item,
		 * UIMAP_Settings.checkbox, 10); if (checkbox != null) {
		 * commonLibrary.setCheckBox(checkbox, true); report.updateTestLog(
		 * "Set Checkbox '" + item.getText() + "'", "'" + item.getText() +
		 * "' Checkbox is Checked", Status.DONE); } if (counter == 4) { if
		 * (!checkbox.isSelected()) { report.updateTestLog( "Verify '" +
		 * checkboxParagraph.getText() + "' is selected", "'" +
		 * checkboxParagraph.getText() +
		 * "' is not selected because already 3 selected", Status.PASS); } } } }
		 * } WebElement jurisdictionOk = commonLibrary.isExist(
		 * UIMAP_Settings.jurisdictionOk, 10);
		 * commonLibrary.clickButtonParentWithWait(jurisdictionOk, "OK");
		 * commonLibrary.sleep(20000); int count = 0; WebElement selectedItem =
		 * commonLibrary.isExist( UIMAP_Settings.selectedItem1, 10); if
		 * (selectedItem != null) { List<WebElement> spanText =
		 * commonLibrary.isExistList( selectedItem, UIMAP_Settings.span, 10); if
		 * (spanText.size() > 0) { addPreferredCourts = commonLibrary.isExist(
		 * UIMAP_Settings.editCourts, 10); for (WebElement item1 : spanText) {
		 * if (filters.toLowerCase().trim()
		 * .contains(item1.getText().toLowerCase().trim())) {
		 * report.updateTestLog( "Verify " + item1.getText() +
		 * " is present under the section 'When displaying court filters, always list these first'"
		 * , item1.getText() +
		 * " is present under the section 'When displaying court filters, always list these first'"
		 * , Status.PASS); count++; } } int k=filtersList.length; if(counter==4)
		 * { k=filtersList.length-1; } if (count != k) { report.updateTestLog(
		 * "Verify " + filters +
		 * " is present under the section 'When displaying court filters, always list these first'"
		 * , filters +
		 * " is not present under the section 'When displaying court filters, always list these first'"
		 * , Status.FAIL); }
		 */
		if (addPreferredCourts != null) {
			report.updateTestLog("Verify Edit button is present under the section 'When displaying court filters, always list these first'", "Edit button is present under the section 'When displaying court filters, always list these first'", Status.PASS);
		}

		return new LASettings(scriptHelper);

	}

	/*
	 * //
	 * #**********************************************************************
	 * ******************************************************* // # Function
	 * Description : Function to select drop down in jump to // # Function Name
	 * : clickDropDownInJumpTo // # Author : jubin // # Date Created : Jan'16 //
	 * #
	 * *************************************************************************
	 * ****************************************************
	 * 
	 * public LASettings selectDropdownUnderDisplatResults(String
	 * searchResults){
	 * 
	 * WebElement lnkResearch = commonLibrary.isExist(
	 * UIMAP_Settings.btnIdResearch, 20); if (lnkResearch != null)
	 * commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");
	 * 
	 * // driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);
	 * 
	 * WebElement PgTitle = commonLibrary.isExist(
	 * UIMAP_Settings.PgTitleResearch, 20); if (PgTitle != null) {
	 * report.updateTestLog( "Verify Lexis Advance® Research Page is displayed",
	 * "Lexis Advance® Research Page is displayed", Status.PASS); } else {
	 * report.updateTestLog( "Verify Lexis Advance® Research Page is displayed",
	 * "Lexis Advance® Research Page is NOT displayed", Status.FAIL);
	 * 
	 * }
	 * 
	 * if (!searchResults.equals("")) { WebElement practiceAreaSearchResults =
	 * commonLibrary.isExist( UIMAP_Settings.practiceAreaResults, 10);
	 * commonLibrary.selectByVisibleText(practiceAreaSearchResults,
	 * searchResults); WebElement selectedItem =
	 * commonLibrary.isExist(practiceAreaSearchResults,
	 * UIMAP_Settings.selectedItemStartin, 10);
	 * 
	 * if(selectedItem.getText().toLowerCase().contains(searchResults.toLowerCase
	 * ())){ report.updateTestLog( "Verify '" + searchResults +
	 * "' is set in drop down under heading 'Display search results in this category first'"
	 * , "'" + searchResults +
	 * "' is set in drop down under heading 'Display search results in this category first'"
	 * , Status.PASS); } else report.updateTestLog( "Verify '" + searchResults +
	 * "' is set in drop down under heading 'Display search results in this category first'"
	 * , "'" + searchResults +
	 * "' is not set in drop down under heading 'Display search results in this category first'"
	 * , Status.FAIL); } return new LASettings(scriptHelper);
	 * 
	 * }
	 */

	// #*****************************************************************************************************************************
	// # Function Description : Function to do settings in LexisAdvance Research
	// Page
	// # Function Name : researchPageSettingsWithStartin     
	// # Author : Jubin
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Search researchPageSettingsWithStartin(String searchResults, String strOption, Boolean isCheck) {
		System.out.println(strOption);
		try {
			WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
			if (lnkResearch != null)
				commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

			// driver.manage().timeouts().implicitlyWait(120,TimeUnit.SECONDS);

			WebElement PgTitle = commonLibrary.isExist(UIMAP_Settings.PgTitleResearch, 20);
			if (PgTitle != null) {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify Lexis Advance® Research Page is displayed", "Lexis Advance® Research Page is NOT displayed", Status.FAIL);

			}

			if (!searchResults.equals("")) {
				WebElement practiceAreaSearchResults = commonLibrary.isExist(UIMAP_Settings.practiceAreaResults, 10);
				commonLibrary.selectByVisibleText(practiceAreaSearchResults, searchResults);
				WebElement selectedItem = commonLibrary.isExist(practiceAreaSearchResults, UIMAP_Settings.selectedItemStartin, 10);

				if (selectedItem.getText().toLowerCase().contains(searchResults.toLowerCase())) {
					report.updateTestLog("Verify '" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", "'" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", Status.PASS);
				} else
					report.updateTestLog("Verify '" + searchResults + "' is set in drop down under heading 'Display search results in this category first'", "'" + searchResults + "' is not set in drop down under heading 'Display search results in this category first'", Status.FAIL);
			}

			WebElement rdoCaseOption = null;
			WebElement rdoCaseOption1 = null;
			switch (strOption) {
			case "Overview": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseOverview, 10);
				break;
			}
			case "Show Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				break;
			}
			case "Show Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				break;
			}
			case "Recently Viewed Icon": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.recentlyViewedIcon, 10);
				break;
			}
			case "Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.termsRadio, 10);
				break;
			}
			case "Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;
			}
			case "Show Extract & Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowExtract, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;

			}
			case "Show Overview & Extract": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowOverview, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.extractRadio, 10);
				break;

			}
			case "Show Terms & Terms": {
				rdoCaseOption = commonLibrary.isExistNegative(UIMAP_Settings.rdoCaseShowTerms, 10);
				rdoCaseOption1 = commonLibrary.isExistNegative(UIMAP_Settings.termsRadio, 10);
				break;

			}

			}

			if (rdoCaseOption != null && !strOption.equals("Recently Viewed Icon")) {
				commonLibrary.setRadioButton(rdoCaseOption, "Show Extract");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			} else if (strOption.equals("Recently Viewed Icon")) {
				if (commonLibrary.setCheckBox(rdoCaseOption, isCheck)) {
					if (isCheck)
						report.updateTestLog("Check " + strOption + " from Other options", strOption + " is Checked from Other options", Status.PASS);
					else
						report.updateTestLog("UnCheck " + strOption + " from Other options", strOption + " is unchecked from Other options", Status.PASS);
				} else
					report.updateTestLog("Check/uncheck " + strOption + " from Other options", strOption + " is not checked/unchecked from Other options", Status.FAIL);
			} else {
				report.updateTestLog("Select " + strOption + " from Category/Other options", strOption + " is not selected from Category/Other options", Status.FAIL);

			}
			if (rdoCaseOption1 != null && strOption.equals("Show Extract & Extract")) {
				commonLibrary.setRadioButton(rdoCaseOption1, "Extract");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			}
			if (rdoCaseOption1 != null && strOption.equals("Show Terms & Terms")) {
				commonLibrary.setRadioButton(rdoCaseOption1, "Terms");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			}
			if (rdoCaseOption1 != null && strOption.equals("Show Overview & Extract")) {
				commonLibrary.setRadioButton(rdoCaseOption1, "Terms");
				// report.updateTestLog("Select " + strOption +
				// " from Category/Other options", strOption +
				// " is selected from Category/Other options",
				// Status.PASS);
			}
			commonLibrary.scrollDown(1000);
			WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
			if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
				commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

			else {
				report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.WARNING);
				WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
				commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to apply hclt as start in settings page
	// # Function Name : setCategory_new
	// # Author : Vennila
	// # Date Created : Jan 16
	// #*****************************************************************************************************************************
	public Home setCategory_new(String Value) {
		WebElement lnkResearch = commonLibrary.isExist(UIMAP_Settings.btnIdResearch, 20);
		if (lnkResearch != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkResearch, "Research");

		WebElement listbox = commonLibrary.isExist(By.cssSelector("select[id='settings_research_searchresults']"), 20);
		// commonLibrary.selectByVisibleTextByValue(listbox, "urn:hlct:5",
		// "Cases");
		commonLibrary.selectFromListOption(listbox, Value);
		commonLibrary.scrollDown(1000);
		WebElement btnSaveSettings = commonLibrary.isExist(UIMAP_Settings.btnIdsubmitSettChange, 20);
		if (btnSaveSettings != null && btnSaveSettings.getAttribute("disabled") == null)
			commonLibrary.clickLinkWithWebElementWithWait(btnSaveSettings, "SaveSettings");

		else {
			report.updateTestLog("Click on Save Settings", "Save Settings button is disabled", Status.DONE);
			WebElement btnCancelSettings = commonLibrary.isExist(UIMAP_Settings.btnIdCancelSettChange, 20);
			commonLibrary.clickLinkWithWebElementWithWait(btnCancelSettings, "CancelSettings");
		}
		return new Home(scriptHelper);
	}
}
