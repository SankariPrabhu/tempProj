package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
//import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Publicrecords;
import UIMAP.UIMAP_RelatedContent;
import UIMAP.UIMAP_SearchResult;

public class Publicrecords extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	public Publicrecords(ScriptHelper scriptHelper) {

		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("publicrecordshome"))
				commonLibrary.sleep(5000);

		} while (!url.contains("publicrecordshome") && counter < 40);

		if (!driver.getCurrentUrl().contains("publicrecordshome")) {
			throw new IllegalStateException("public records  home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Right Click on ProductLogo and open
	// a new window
	// # Function Name : RightClick_ProductLogo_OpenInNewWindow     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Publicrecords termsForUseData(String val1, String val2) {

		driver.switchTo().frame(0);

		WebElement confirm = commonLibrary.isExist(UIMAP_RelatedContent.cofirm, 10);
		if (confirm != null) {
			WebElement form1 = commonLibrary.isExist(By.cssSelector("form[id='form1']"), 10);
			WebElement div = commonLibrary.isExist(form1, By.cssSelector("div[id='selectPermUse']"), 10);
			WebElement mainterm = commonLibrary.isExist(div, By.cssSelector("table[id='permUseSteps']"), 10);
			WebElement dppadropdown = commonLibrary.isExist(mainterm, UIMAP_RelatedContent.dppadropdown, 10);

			commonLibrary.selectByVisibleText(dppadropdown, val1);
			WebElement glbadropdown = commonLibrary.isExist(UIMAP_RelatedContent.glbadropdown, 10);
			commonLibrary.selectByVisibleText(glbadropdown, val2);
			// WebElement dmfdropdown =
			// commonLibrary.isExist(UIMAP_RelatedContent.dmfdropdown, 10);
			// commonLibrary.SelectByVisibleText(dmfdropdown, val3);
			confirm = commonLibrary.isExist(UIMAP_RelatedContent.cofirm, 10);
			commonLibrary.clickButtonParentWithWait(confirm, "confirm");
		} else
			report.updateTestLog("Enter details in Terms of Use page.", "Terms of Use page is not displayed.", Status.WARNING);
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Public Record links
	// # Function Name : clickPR_links     
	// # Author : uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Publicrecords clickPRLinks(String link) {
		driver.switchTo().frame(0);
		boolean blnflg = false;
		// driver.switchTo().frame(0);
		WebElement wrapper = commonLibrary.isExist(By.cssSelector("div[id='PageWrapper']"));
		WebElement div = commonLibrary.isExist(wrapper, By.cssSelector("div[id='MainContent_menuPanel']"), 20);
		// WebElement mainul = commonLibrary.isExist(div,
		// By.cssSelector("ul[class='menuLevel2Container']"), 20);
		List<WebElement> li_list = commonLibrary.isExistList(div, By.cssSelector("li[class='menuLevel2']"), 20);
		for (WebElement list : li_list) {
			List<WebElement> li_button = commonLibrary.isExistList(list, By.tagName("a"), 20);
			for (int i = 0; i < li_button.size(); i++) {
				if (li_button.get(i).getText().equalsIgnoreCase(link)) {
					commonLibrary.clickButtonParentWithWait(li_button.get(i), link);
					blnflg = true;
					break;

				}
			}

			if (blnflg) {
				break;
			}
		}
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search basic information
	// # Function Name : searchbasicinfo     
	// # Author : uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Publicrecords searchbasicinfo(String cmpyname, String state) {
		driver.switchTo().frame(0);
		WebElement cmpynametxt = commonLibrary.isExist(By.cssSelector("input[id='MainContent_CompanyName']"));
		commonLibrary.setDataInTextBox(cmpynametxt, cmpyname, "Search Box");

		WebElement statedropdown = commonLibrary.isExist(By.cssSelector("select[id='MainContent_Address_State_stateList']"));
		commonLibrary.selectByVisibleText(statedropdown, state);

		WebElement searchbtn = commonLibrary.isExist(By.cssSelector("input[id='MainContent_formSubmit_searchButton']"));
		commonLibrary.clickButtonParentWithWait(searchbtn, "Search");
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Medmal Navigator page
	// by clicking Lexis Advance arrow
	// # Function Name : navigateToMedmalNavigator     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public MedmalNavigator navigateToMedmalNavigator() {
		generalFunctions.productSwitcher("Medmal Navigator");

		return new MedmalNavigator(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click DPPA dropdown
	// # Function Name : clickDppaDropDown     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Publicrecords clickDppaDropDown(String dppaOption1) {

		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_Publicrecords.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_Publicrecords.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 25);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_Publicrecords.termsofuse, 20);
		WebElement dppaDropDown = commonLibrary.isExist(termsofuse, UIMAP_Publicrecords.dppaDropDown, 20);
		if (dppaDropDown != null) {
			commonLibrary.clickLink(dppaDropDown, "DPPA");
			commonLibrary.selectFromListOption(dppaDropDown, dppaOption1);
		}

		else {
			report.updateTestLog("Verify DPPA Dropdown ", "DPPA Dropdown is not available", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click GLBA dropdown
	// # Function Name : clickGlbaDropDown     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Publicrecords clickGlbaDropDown(String glbaoption) {
		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_Publicrecords.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_Publicrecords.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_Publicrecords.termsofuse, 20);
		WebElement glbaDropDown = commonLibrary.isExist(termsofuse, UIMAP_Publicrecords.glbaDropDown, 20);

		if (glbaDropDown != null) {

			commonLibrary.clickLink(glbaDropDown, "GLBA");
			commonLibrary.selectFromListOption(glbaDropDown, glbaoption);

		}

		else {
			report.updateTestLog("Verify GLBA Dropdown ", "GLBA Dropdown is not available", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click DMF dropdown
	// # Function Name : clickDmfDropDown     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Publicrecords clickDmfDropDown(String dmfoption) {
		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_Publicrecords.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_Publicrecords.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_Publicrecords.termsofuse, 20);
		WebElement dmfDropDown = commonLibrary.isExist(termsofuse, UIMAP_Publicrecords.dmfDropDown, 20);

		if (dmfDropDown != null) {

			commonLibrary.clickLink(dmfDropDown, "DMF");
			commonLibrary.selectFromListOption(dmfDropDown, dmfoption);

		}

		else {
			report.updateTestLog("Verify DMF Dropdown ", "DMF Dropdown is not available", Status.DONE);
		}
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used Click confirm button
	// # Function Name : clickConfirm     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Publicrecords clickConfirm() {

		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_Publicrecords.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_Publicrecords.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);

		WebElement confirmButton = commonLibrary.isExist(diverms, UIMAP_Publicrecords.confirmButton, 20);
		commonLibrary.clickButtonParent(confirmButton, "Confirm");
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to open Comprehensive Person Report
	// page
	// # Function Name : clickCompPersonReport     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Publicrecords clickCompPersonReport() {
		driver.switchTo().frame(0);
		WebElement menuContainer = commonLibrary.isExistNegative(UIMAP_Publicrecords.liparentframe1, 20);
		int count = 0;
		do {
			count++;
			menuContainer = commonLibrary.isExist(UIMAP_Publicrecords.liparentframe1, 20);
			commonLibrary.sleep(3000);
		} while (menuContainer == null && count < 5);

		WebElement compPersReport = commonLibrary.isExist(menuContainer, UIMAP_Publicrecords.compPersonReport, 20);
		commonLibrary.clickLink(compPersReport, "Comprehensive Person Report");
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used enter first name & last name
	// # Function Name : enterfirstlastname     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public Publicrecords enterfirstlastname(String first, String last) {

		driver.switchTo().frame(0);
		WebElement divFirstName = commonLibrary.isExist(UIMAP_Publicrecords.divFirst, 20);
		WebElement divLastName = commonLibrary.isExist(UIMAP_Publicrecords.divLast, 20);
		int count = 0;
		do {
			count++;
			divFirstName = commonLibrary.isExist(UIMAP_Publicrecords.divFirst, 20);
			divLastName = commonLibrary.isExist(UIMAP_Publicrecords.divLast, 20);
			commonLibrary.sleep(3000);
		} while (divFirstName == null && divLastName == null && count < 5);
		WebElement enterFirstName = commonLibrary.isExist(divFirstName, UIMAP_Publicrecords.firstName, 20);
		WebElement enterLastName = commonLibrary.isExist(divLastName, UIMAP_Publicrecords.lastName, 20);
		if (enterFirstName != null) {
			commonLibrary.setDataInTextBox(enterFirstName, first, "FirstName");

		} else {
			report.updateTestLog("Set " + first + " in FirstName text box", "FirstName textbox is not available", Status.FAIL);
		}
		if (enterFirstName != null) {
			commonLibrary.setDataInTextBox(enterLastName, last, "LastName");
		} else {
			report.updateTestLog("Set " + last + " in LastName text box", "LastName textbox is not available", Status.FAIL);
		}

		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used click search button
	// # Function Name : clickSearch     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Publicrecords clickSearch() {
		driver.switchTo().frame(0);
		WebElement divButton = commonLibrary.isExistNegative(UIMAP_Publicrecords.divButton, 20);
		int count = 0;
		do {
			count++;
			divButton = commonLibrary.isExistNegative(UIMAP_Publicrecords.divButton, 20);
			commonLibrary.sleep(3000);
		} while (divButton == null && count < 5);

		WebElement clickSearchButton = commonLibrary.isExist(UIMAP_Publicrecords.searchButton, 20);
		commonLibrary.clickButtonParent(clickSearchButton, "Search");
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used click search results
	// # Function Name : clickSearchResults     
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************

	public Publicrecords clickSearchResults() {
		driver.switchTo().frame(0);
		WebElement divOld = commonLibrary.isExistNegative(UIMAP_Publicrecords.divResultCon, 20);
		int count = 0;
		do {
			count++;
			divOld = commonLibrary.isExistNegative(UIMAP_Publicrecords.divResultCon, 20);
			commonLibrary.sleep(3000);
		} while (divOld == null && count < 25);

		WebElement clickSearchResults = commonLibrary.isExist(UIMAP_Publicrecords.clickSearchLink, 20);
		commonLibrary.clickLink(clickSearchResults, "BRAIN, CONNER");
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used verify public records report page
	// # Function Name : verifyReportPage     
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************

	public Publicrecords verifyReportPage() {
		driver.switchTo().frame(0);
		WebElement reportPage = commonLibrary.isExistNegative(UIMAP_Publicrecords.divResultTable, 20);
		if (reportPage != null) {
			report.updateTestLog("Verify Public records Person Report page displays", "Public records Person Report page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public records Person Report page displays", "Public records Person Report page is not displayed", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// the Public records results page
	// # Function Name : logout
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used select state in comp person report
	// # Function Name : selectState     
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************
	public Publicrecords selectState(String state) {
		driver.switchTo().frame(0);
		WebElement selectState = commonLibrary.isExistNegative(UIMAP_Publicrecords.selstatecompper, 20);
		int count = 0;
		do {
			count++;
			selectState = commonLibrary.isExist(UIMAP_Publicrecords.selstatecompper, 20);
			commonLibrary.sleep(3000);
		} while (selectState == null && count < 5);

		if (selectState != null) {
			commonLibrary.selectFromListOption(selectState, state);
		} else {
			report.updateTestLog("Verify " + state + " is selected in State dropdown", "State drop down not available", Status.FAIL);
		}

		driver.switchTo().defaultContent();
		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the document title and other
	// terms in history page
	// # Function Name : verifyHistoryDetails     
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************
	public History verifyHistoryDetails(String DocTitle, String type, String client) {
		boolean clientPresent = false;
		// boolean datePresent = false;
		boolean typePresent = false;
		// boolean origPresent = false;
		// boolean docTypePresent = false;
		boolean allData = false;
		boolean docTil = false;
		// boolean blgterms = false;
		// boolean blgcategory = false;
		WebElement btnClientID = commonLibrary.isExist(UIMAP_SearchResult.btnClientID, 20);
		if (btnClientID != null) {
			String[] clientID = btnClientID.getText().split(":");
			client = clientID[1].trim();
		}
		WebElement rslt = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
		if (rslt != null) {
			WebElement div = commonLibrary.isExist(rslt, UIMAP_SearchResult.resultwrapper, 20);
			if (div != null) {
				List<WebElement> contentList = commonLibrary.isExistList(div, UIMAP_SearchResult.listtag, 20);
				for (WebElement item : contentList) {
					List<WebElement> contentLink = commonLibrary.isExistList(item, UIMAP_SearchResult.atag, 20);
					for (WebElement item1 : contentLink) {
						if (item1.getText().toLowerCase().contains(DocTitle.toLowerCase())) {
							docTil = true;
							List<WebElement> clientVal = commonLibrary.isExistList(item, UIMAP_SearchResult.dd, 10);
							List<WebElement> clientHeading = commonLibrary.isExistList(item, UIMAP_SearchResult.dt, 10);
							for (int i = 0; i < clientHeading.size(); i++) {
								if (clientHeading.get(i).getText().equalsIgnoreCase("Client")) {
									if (client.equalsIgnoreCase("Exists")) {
										if (clientVal.get(i).getText() != "") {
											clientPresent = true;
										}
									} else {
										if (clientVal.get(i).getText().equalsIgnoreCase(client)) {
											clientPresent = true;
										}
									}
								} else if (clientHeading.get(i).getText().equalsIgnoreCase("Type")) {
									if (clientVal.get(i).getText().equalsIgnoreCase(type)) {
										typePresent = true;
									}
								}

								if (docTil && clientPresent && typePresent)
									break;
							}

						}
						if (docTil && clientPresent && typePresent) {
							allData = true;
							break;
						}
					}
					if (allData)
						break;
				}
			}

		}
		if (allData)
			report.updateTestLog("Verify the History details for the document " + DocTitle, "All the sections like Title, Client, Type is displayed as expected", Status.PASS);
		else {
			if (!docTil)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Document Title is NOT displayed as expected", Status.FAIL);

			if (!clientPresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Client is NOT displayed as expected", Status.FAIL);

			if (!typePresent)
				report.updateTestLog("Verify the History details for the document " + DocTitle, "Type is NOT displayed as expected", Status.FAIL);

		}
		driver.switchTo().defaultContent();

		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used verify Terms For Use of Data test
	// displayed
	// # Function Name : verifyTermsforusetext     
	// # Author : Jeeva
	// # Date Created : OCT'15
	// #*****************************************************************************************************************************

	public Publicrecords verifyTermsforusetext() {
		List<WebElement> termsForUse = commonLibrary.isExistList(UIMAP_Publicrecords.uitermsforuse, 20);
		if (termsForUse != null) {
			if (termsForUse.get(0).getText().equalsIgnoreCase("Terms For Use of Data")) {
				report.updateTestLog("Verify" + "Terms For Use of Data" + "has been displayed", "Terms For Use of Data has been displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify" + "Terms For Use of Data" + "has been displayed", "Terms For Use of Data not displayed", Status.FAIL);
			}
		}

		return new Publicrecords(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Absence of PR in product switcher
	// # Function Name : absenceofPR 
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Publicrecords absenceofPR() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Publicrecords.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Publicrecords.btnPublicRecords, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Public Records");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Public Records");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Publicrecords.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("public records")) {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Publicrecords.btnPublicRecords, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance® Public Records logo displays in the Top left corner of Global menu", "Lexis Advance® Public Records logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		} else {

			report.updateTestLog("Verify Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", "Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", Status.PASS);
		}

		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is to verify Public records home page
	// displays
	// # Function Name : verifyPublicRecordsHomePage 
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Publicrecords verifyPublicRecordsHomePage() {

		driver.switchTo().frame(0);
		WebElement prHomePage = commonLibrary.isExist(UIMAP_Publicrecords.prHomePage, 20);
		WebElement menuContainer = commonLibrary.isExistNegative(UIMAP_Publicrecords.liparentframe1, 20);
		if (prHomePage != null && menuContainer != null) {
			report.updateTestLog("Verify Public records Home page has displayed", "Public records Home page has been displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public records Home page has displayed", "Public records Home page not displayed", Status.DONE);
			WebElement diverms = commonLibrary.isExistNegative(UIMAP_Publicrecords.divperms1, 20);
			if (diverms != null) {
				report.updateTestLog("Verify Terms of use page has displayed", "Terms of use page has been displayed", Status.DONE);
				this.clickDppaDropDown("I have no permissible use (searches unregulated data only)");
				this.clickGlbaDropDown("I have no permissible use (searches unregulated data only)");
				this.clickDmfDropDown("Legitimate Business Purpose");
				this.clickConfirm();
				if (menuContainer != null) {
					report.updateTestLog("Verify Public records Home page has displayed", "Public records Home page has been displayed", Status.PASS);
				}
			} else {
				report.updateTestLog("Verify Terms of use page has displayed", "Terms of use page not displayed", Status.DONE);
				report.updateTestLog("Verify Public records Home page has displayed", "Public records Home page not displayed", Status.FAIL);
			}

		}

		return new Publicrecords(scriptHelper);

	}

}
