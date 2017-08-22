package functionallibraries;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Home;
import com.cognizant.framework.FrameworkException;

import supportlibraries.*;

public class AlertsShepardsResults extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	public AlertsShepardsResults(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("alertshepardsresults"))
				commonLibrary.sleep(5000);

		} while (!url.contains("alertshepardsresults") && counter < 40);
		if (!driver.getCurrentUrl().contains("alertshepardsresults")) {
			throw new IllegalStateException(
					"alert shepards results page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the name and description of
	// the client id
	// # Function Name : verificationWithNameAndDescription     
	// # Author : Divakar
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public History navigateToHistoryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(
					UIMAP_Home.btnIdHistoryMenuArrow, 10);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(
						UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow,
					"History");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(
						UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtViewAllHistory,
							"View All History");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory,
							"View All History");
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(
						UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap,
							"Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap,
							"Research Map");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new History(scriptHelper);

	}
}
