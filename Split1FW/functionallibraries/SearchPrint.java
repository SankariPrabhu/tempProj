package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_SearchPrint;
import UIMAP.UIMAP_SearchResult;

import com.cognizant.framework.Status;

public class SearchPrint extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public SearchPrint(ScriptHelper scriptHelper) {
		super(scriptHelper);
		if (!((driver.getCurrentUrl().contains("print")) || (driver.getCurrentUrl().contains("litresultsprint")))) {
			throw new IllegalStateException("Print page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the result Count
	// # Function Name : verifyResultCount     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyResultCount(int expCount, Boolean isGreater) {

		// Verification Point : ContentType Result count is greater than the
		// expected count
		if (isGreater) {

			WebElement searchResultHeader = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 20);
			if (searchResultHeader.getText().contains("10,000+"))
				report.updateTestLog("Verify Result count 10000+ is displayed next to HLCT", "Result count is displayed as " + searchResultHeader.getText(), Status.PASS);
			else
				report.updateTestLog("Verify Result count 10000+ is displayed next to HLCT", "Result count is not displayed as 10000+", Status.FAIL);

		} else {

			WebElement searchResultHeader1 = commonLibrary.isExist(UIMAP_SearchResult.SearchResultHeader, 20);
			WebElement searchResultHeader = commonLibrary.isExist(searchResultHeader1, By.tagName("h2"), 20);
			String[] arrcount = searchResultHeader.getText().toString().replace(",", "").split("Sort");
			String[] arrCount3 = arrcount[0].split(" ");

			String arrCount1 = null;
			if (arrCount3[1].contains("("))
				arrCount1 = arrCount3[1].trim().replace("(", "");
			else if (arrCount3[2].contains("("))
				arrCount1 = arrCount3[2].trim().replace("(", "");

			String arrcount2 = arrCount1.replace(")", "");

			if (Integer.parseInt((arrcount2.trim())) < expCount)
				report.updateTestLog("Verify Result count less than" + expCount + " is displayed next to HLCT", "Result count is displayed as " + searchResultHeader.getText(), Status.PASS);
			else
				report.updateTestLog("Verify Result count  less than" + expCount + " is displayed next to HLCT", "Result count is not displayed as  less than" + expCount, Status.FAIL);

		}

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for close and switch to search
	// # Function Name : closeAndSwitchToSearch     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search closeAndSwitchToSearch() {

		driver.close();
		commonLibrary.smallWait();
		commonLibrary.switchToWindow("search");
		report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for close and switch to lit
	// # Function Name : closeAndSwitchTolit     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LegalIssueTrail closeAndSwitchTolit() {
		driver.close();
		commonLibrary.smallWait();
		commonLibrary.switchToWindow("Legal Issue Trail");
		report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
		return new LegalIssueTrail(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verify Browser Title
	// # Function Name : verifyBrowserTitle     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyBrowserTitle_ResultsCount(int expCount, Boolean isGreater) {

		// Verification Point: Results count in the browser tiltle is greater
		// than the expected count
		if (isGreater) {

			if (driver.getTitle().replace(",", "").contains(expCount + "+"))
				report.updateTestLog("Verify Broswer Title", "Broswer Title is having result count greater than " + expCount, Status.PASS);
			else
				report.updateTestLog("Verify Broswer Title", "Broswer Title is not having result count greater than " + expCount, Status.FAIL);
		} else {
			String[] arrTitle = driver.getTitle().split(" ");

			if (Integer.parseInt((arrTitle[0].replace(",", ""))) < expCount)
				report.updateTestLog("Verify Broswer Title", "Broswer Title is having result count less than " + expCount, Status.PASS);
			else
				report.updateTestLog("Verify Broswer Title", "Broswer Title is not having result count less than " + expCount, Status.FAIL);
		}

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the footer link name
	// # Function Name : verifyFooterLink     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyFooterLink(String footerName) {
		Boolean blnFlag = false;
		List<WebElement> footer = commonLibrary.isExistList(UIMAP_SearchPrint.footerElement, 20);
		for (WebElement option : footer) {
			if (option.getText().contains(footerName)) {
				blnFlag = true;
				break;
			}

		}
		if (blnFlag)
			report.updateTestLog("Verify the following links " + footerName + " appear at the very bottom of  Print friendly page.", " The following links " + footerName + " appear at the very bottom of  Print friendly page.", Status.PASS);

		else
			report.updateTestLog("Verify the following links " + footerName + " appear at the very bottom of Print friendly page.", " The following links " + footerName + " does not appear at the very bottom of  Print friendly page.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the footer Lexis nexis
	// logo
	// # Function Name : verifyLexisLogo     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyFooterLexisLogo() {

		WebElement lexislogo = commonLibrary.isExist(UIMAP_SearchPrint.lexisnexisLogo, 20);
		if (lexislogo != null)
			report.updateTestLog("Verify the following links Lexis Nexis Logo appear at the very bottom of Print friendly page.", " The following links Lexis Nexis Logo appear at the very bottom of  Print friendly page.", Status.PASS);
		else
			report.updateTestLog("Verify the following links Lexis Nexis Logo appear at the very bottom of Print friendly page.", " The following links Lexis Nexis Logo does not appear at the very bottom of  Print friendly page.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the footer Lexis advance
	// logo
	// # Function Name : verifyLexisAdvanceLogo     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyLexisAdvanceLogo() {

		WebElement lexislogo = commonLibrary.isExist(UIMAP_SearchPrint.lexisAdvaceLogo, 20);
		if (lexislogo != null)
			report.updateTestLog("'Lexis Advance' logo displays at top left of the LIT page.", " 'Lexis Advance' logo is displayed at top left of the LIT page.", Status.PASS);
		else
			report.updateTestLog("'Lexis Advance' logo displays at top left of the LIT page.", " 'Lexis Advance' logo is not displayed at top left of the LIT page.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the footer Relx
	// logo
	// # Function Name : verifyLexisRelxLogo     
	// # Author : Deepha Hariramasamy
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyLexisRelxLogo() {

		WebElement relxLogo = commonLibrary.isExist(UIMAP_SearchPrint.relxLogo, 20);
		if (relxLogo != null)
			report.updateTestLog("'Relx group' logo displays at bottom right of the  page.", " 'Relx group' logo is displayed at bottom right of the  page.", Status.PASS);
		else
			report.updateTestLog("'Relx group' logo displays at bottom right of the  page.", " 'Relx group' logo is not displayed at bottom right of the  page.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the LIT Title in LIT
	// print page
	// # Function Name : verifyLITTitle     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyLITTitle(String linkName) {

		WebElement litResultHeader = commonLibrary.isExist(UIMAP_SearchPrint.passageLITHeader, 20);
		if (litResultHeader.getText().contains(linkName))
			report.updateTestLog("Verify Page title displays as  " + linkName + ".", "  Page title is displayed as  " + linkName + ".", Status.PASS);
		else
			report.updateTestLog("Verify Page title displays as  " + linkName + ".", "  Page title id not displayed as  " + linkName + ".", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the print button in LIT
	// print page
	// # Function Name : verifyPrintButton     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyPrintButton() {
		WebElement printHeaderLIT = commonLibrary.isExist(UIMAP_SearchPrint.printHeader1, 20);
		WebElement printFooterLIT = commonLibrary.isExist(UIMAP_SearchPrint.prinFooter1, 20);
		if (printHeaderLIT != null && printFooterLIT != null)
			report.updateTestLog("Verify Print button displays in 2 places, at the top and bottom right of the LIT page", "Print button displayed in 2 places, at the top and bottom right of the LIT page.", Status.PASS);
		else
			report.updateTestLog("Verify Print button displays in 2 places, at the top and bottom right of the LIT page.", "Print button is not displays in 2 places, at the top and bottom right of the LIT page.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the print button in LIT
	// print page
	// # Function Name : verifyPrintButton     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifySelectedPassageLIT(String selectedPassageName) {
		WebElement selectedPassage = commonLibrary.isExist(UIMAP_SearchPrint.passageLITHeader, 20);
		WebElement litSymbol = commonLibrary.isExist(selectedPassage, UIMAP_SearchPrint.litSymbol, 20);
		if (selectedPassage.getText().contains(selectedPassageName) && litSymbol != null)
			report.updateTestLog("Verify Selected passage displays along with LIT icon", "Selected passage is displayed along with LIT icon.", Status.PASS);
		else
			report.updateTestLog("Verify Selected passage displays along with LIT icon.", "Selected passage is not displayed along with LIT icon.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the Citation and sortBy
	// heading
	// # Function Name : verifyCitationSortBy     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyCitationSortBy(String citationName, String SortByOption) {
		Boolean blnFlag = false;
		WebElement litHeader = commonLibrary.isExist(UIMAP_SearchPrint.passageLITHeader, 20);
		List<WebElement> citSortOption = commonLibrary.isExistList(litHeader, UIMAP_SearchPrint.titleHeader, 20);

		for (WebElement option : citSortOption) {
			if (option.getText().contains(citationName) && option.getText().contains(SortByOption)) {
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify the following displays right below the Selected passage - Citation(N) at the left and Sort by Relevance at the right.", "Citation(N) at the left and Sort by Relevance at the right is displayed right below the Selected passage.", Status.PASS);
		else
			report.updateTestLog("Verify the following displays right below the Selected passage - Citation(N) at the left and Sort by Relevance at the right.", "Citation(N) at the left and Sort by Relevance at the right is not displayed right below the Selected passage.", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Verifying the section name
	// # Function Name : verifyDocumentSectionNames     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyDocumentSectionNames(String sectionName, String sectionJuris, String sectionCourt, String sectionDate) {
		Boolean blnFlag = false;
		Boolean blnFlag1 = false;
		// WebElement resultsListSearch=
		// commonLibrary.isExist(UIMAP_SearchPrint.resultsListSearch, 20);
		WebElement resultsListSearch = commonLibrary.isExist(By.cssSelector("form[class='results-list search']"), 20);
		List<WebElement> titleOption = commonLibrary.isExistList(resultsListSearch, UIMAP_SearchPrint.titleHeader, 20);
		List<WebElement> headerOption = commonLibrary.isExistList(resultsListSearch, UIMAP_SearchPrint.asideView, 20);

		for (WebElement option : titleOption) {
			if (option.getText().contains(sectionName)) {
				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {

			for (WebElement option : headerOption) {
				if (option.getText().contains(sectionJuris) && option.getText().contains(sectionCourt) && option.getText().contains(sectionDate)) {
					blnFlag1 = true;
					break;
				}
			}
		}
		if (blnFlag1)
			report.updateTestLog("Verify the following displays., a) " + sectionName + ", b) " + sectionJuris + " , " + sectionCourt + " and " + sectionDate + " are below one another corresponding to each document in that section.", "The Following is displayed a) " + sectionName + ", b) " + sectionJuris + " , " + sectionCourt + " and " + sectionDate + " are below one another corresponding to each document in that section.", Status.PASS);
		else
			report.updateTestLog("Verify the following displays., a) " + sectionName + ", b) " + sectionJuris + " , " + sectionCourt + " and " + sectionDate + " are below one another corresponding to each document in that section.", " The Following is not displayed a) " + sectionName + ", b) " + sectionJuris + " , " + sectionCourt + " and " + sectionDate + " are below one another corresponding to each document in that section..", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for date and time in results page
	// printer friendly
	// # Function Name : verifyDateTimeResults     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyDateTimeResults() {

		WebElement datavalue = commonLibrary.isExist(UIMAP_SearchPrint.dateTimeResults, 20);
		if (datavalue.getText().contains("Date and Time:"))
			report.updateTestLog("Verify Date and Time displays at left bottom of the Print friendly page", " The Date and Time " + datavalue.getText() + " is displayed at left bottom of the  Print friendly page", Status.PASS);
		else
			report.updateTestLog("Verify Date and Time displays at left bottom of the Print friendly page", " Date and Time is " + datavalue.getText() + " not displayed at left bottom of the  Print friendly page", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for dat and time
	// # Function Name : verifyDateTime     
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public SearchPrint verifyDateTime() {

		WebElement datavalue = commonLibrary.isExist(UIMAP_SearchPrint.dateTime, 20);
		if (datavalue.getText() != null)
			report.updateTestLog("Verify Date and Time displays at left bottom of the LIT Print friendly page", " The Date and Time " + datavalue.getText() + " is displayed at left bottom of the LIT Print friendly page", Status.PASS);
		else
			report.updateTestLog("Verify Date and Time displays at left bottom of the LIT Print friendly page", " Date and Time is " + datavalue.getText() + " not displayed at left bottom of the LIT Print friendly page", Status.FAIL);

		return new SearchPrint(scriptHelper);
	}

}
