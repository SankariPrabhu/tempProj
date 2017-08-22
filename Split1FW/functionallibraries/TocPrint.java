package functionallibraries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import UIMAP.UIMAP_TocPrint;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class TocPrint extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public TocPrint(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("tocprint"))
				commonLibrary.sleep(5000);

		} while (!url.contains("tocprint") && counter < 40);

		if (!driver.getCurrentUrl().contains("tocprint")) {
			throw new IllegalStateException("TOC Print page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the header title
	// # Function Name : verifyHeader     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TocPrint verifyHeader(String title) {
		WebElement header = commonLibrary.isExist(UIMAP_TocPrint.header, 10);
		if ((header.getText().contains("Table of Contents")) && (header.getText().contains(title)))
			report.updateTestLog(title + " displays at top left corner of TOC printable page", title + " displays at top left corner of TOC printable page", Status.PASS);
		else
			report.updateTestLog(title + " displays at top left corner of TOC printable page", title + " is not displayed at top left corner of TOC printable page", Status.FAIL);
		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to expand any title present within TOC
	// # Function Name : expandTOCtitle     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TocPrint verifyTitleExpansion(String title) {
		boolean flag = false;
		WebElement tocList = commonLibrary.isExist(UIMAP_TocPrint.tocList, 20);
		List<WebElement> titles = commonLibrary.isExistList(tocList, By.tagName("li"), 30);
		for (WebElement item : titles) {
			WebElement titleList = commonLibrary.isExist(item, By.tagName("span"), 10);
			if (titleList.getText().trim().equalsIgnoreCase(title) && item.getAttribute("class").contains("expanded")) {
				report.updateTestLog("Verify " + title + " is expanded", title + " is in expanded status", Status.PASS);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + title + " is expanded", title + " is not in expanded status", Status.FAIL);

		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the footer text
	// # Function Name : verifyFooter     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TocPrint verifyFooter(String title) {
		WebElement footer = commonLibrary.isExist(UIMAP_TocPrint.footer, 10);
		if (footer.getText().contains("Terms:") && footer.getText().contains("Date and Time:") && footer.getText().contains(title)) {
			report.updateTestLog(title + " displays next to terms in the bottom of the printable page", title + " displays next to terms in the bottom of the printable page", Status.PASS);
			report.updateTestLog("Localized date and time displays next to terms in the bottom of the printable page", "Localized date and time displays next to terms in the bottom of the printable page", Status.PASS);
		} else {
			report.updateTestLog(title + " displays next to terms in the bottom of the printable page", title + " is not displayed next to terms in the bottom of the printable page", Status.FAIL);
			report.updateTestLog("Localized date and time displays next to terms in the bottom of the printable page", "Localized date and time is not displayed next to terms in the bottom of the printable page", Status.FAIL);
		}

		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the footer text
	// # Function Name : verifyFooter     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TocPrint verifyFooterNav() {
		try {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			String yearS = Integer.toString(year);

			WebElement footerNav = commonLibrary.isExist(UIMAP_TocPrint.footerNav, 10);
			WebElement lnLogo = commonLibrary.isExist(footerNav, UIMAP_TocPrint.lnLogo, 10);
			if ((lnLogo != null) && (footerNav.getText().contains("About LexisNexis")) && (footerNav.getText().contains("Privacy Policy")) && (footerNav.getText().contains("Terms & Conditions")))
				report.updateTestLog("Footer is displayed as follows:LexisNexis logo | About LexisNexis | Privacy Policy | Terms & Conditions", "Footer is displayed as follows:LexisNexis logo | About LexisNexis | Privacy Policy | Terms & Conditions", Status.PASS);
			else
				report.updateTestLog("Footer is displayed as follows:LexisNexis logo | About LexisNexis | Privacy Policy | Terms & Conditions", "Footer is not displayed as follows:LexisNexis logo | About LexisNexis | Privacy Policy | Terms & Conditions", Status.FAIL);

			WebElement copyright = commonLibrary.isExist(footerNav, UIMAP_TocPrint.copyright, 10); // Copyright
																									// ©
																									// 2015
																									// LexisNexis.
																									// All
																									// rights
																									// reserved.
			String copyRightYear = copyright.getText().substring(12, 16);
			if (yearS.equalsIgnoreCase(copyRightYear))
				report.updateTestLog(copyright.getText() + " is displayed and the current year " + year + " is displayed as the copyright year.", copyright.getText() + " is displayed and the current year " + year + " is displayed as the copyright year.", Status.PASS);
			else
				report.updateTestLog(copyright.getText() + " is displayed and the current year " + year + " is displayed as the copyright year.", copyright.getText() + " is displayed and the current year " + year + " is not displayed as the copyright year.", Status.FAIL);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify page hyperlinks in TOC print
	// page
	// # Function Name : verifyHyperLinks     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TocPrint verifyHyperLinks() {
		WebElement tocList = commonLibrary.isExist(UIMAP_TocPrint.tocList, 10);
		WebElement titles = commonLibrary.isExist(tocList, By.tagName("li"), 30);
		String before = titles.getAttribute("class");
		WebElement expand = commonLibrary.isExist(titles, UIMAP_TocPrint.expandNode, 10);
		commonLibrary.clickButtonParentWithWait(expand, "Expand title");
		String after = titles.getAttribute("class");
		if (before.equalsIgnoreCase(after))
			report.updateTestLog("Each node in the breadcrumb trail is not hyperlinked in Printable page.", "Each node in the breadcrumb trail is not hyperlinked in Printable page.", Status.PASS);
		else
			report.updateTestLog("Each node in the breadcrumb trail is not hyperlinked in Printable page.", "Node in the breadcrumb trail is hyperlinked in Printable page.", Status.FAIL);
		return new TocPrint(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close print page and navigate back
	// to TOC page
	// # Function Name : navigateBacktoTOC     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public TOC navigateBacktoTOC() {
		driver.close();
		commonLibrary.switchToWindow("toc");
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is used to verify print button
	// # Function Name : verifyHederprintbut     
	// # Author : Jeeva
	// # Date Created : NOV'15
	// #*****************************************************************************************************************************

	public TocPrint verifyPrintBut() {

		WebElement printHed = commonLibrary.isExist(UIMAP_TocPrint.printHed, 10);
		WebElement printFot = commonLibrary.isExist(UIMAP_TocPrint.printFot, 10);

		if (printHed != null && printFot != null) {
			report.updateTestLog("Verify Print button are displayed Top and bottom of the page.", "Print button has been displayed Top and bottom of the page..", Status.PASS);
		} else {
			report.updateTestLog("Verify Print button are displayed Top and bottom of the page.", "Print button not displayed Top and bottom of the page..", Status.FAIL);
		}

		return new TocPrint(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function is used to verify product logo
	// # Function Name : verifyProductLogo     
	// # Author : Jeeva
	// # Date Created : NOV'15
	// #*****************************************************************************************************************************

	public TocPrint verifyProductLogo() {

		WebElement printHed = commonLibrary.isExist(UIMAP_TocPrint.printHed, 10);
		WebElement printFot = commonLibrary.isExist(UIMAP_TocPrint.printFot, 10);

		if (printHed != null && printFot != null) {
			report.updateTestLog("Verify Print button are displayed Top and bottom of the page.", "Print button has been displayed Top and bottom of the page..", Status.PASS);
		} else {
			report.updateTestLog("Verify Print button are displayed Top and bottom of the page.", "Print button not displayed Top and bottom of the page..", Status.FAIL);
		}

		return new TocPrint(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to get the results list values in
	// printer friendly view
	// # Function Name : getResultListPrint
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public List<String> getResultListPrint() {
		List<String> resultListValue = null;
		WebElement result = commonLibrary.isExist(UIMAP_TocPrint.resultList, 20);
		List<WebElement> resultListDoc = commonLibrary.isExistList(result, UIMAP_TocPrint.resultListDoc, 20);

		if (result != null) {
			resultListValue = new ArrayList<String>();
			for (int i = 0; i <= 101; i++) {
				String textValue = resultListDoc.get(i).getText().trim().toLowerCase();
				if (textValue != null) {
					resultListValue.add(textValue);

				}
			}
		}

		return resultListValue;
		// return new TOC(scriptHelper);

	}

}
