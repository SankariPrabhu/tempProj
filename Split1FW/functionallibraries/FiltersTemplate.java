package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import UIMAP.UIMAP_SearchResult;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class FiltersTemplate extends ReusableLibrary {

	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public FiltersTemplate(ScriptHelper scriptHelper) {

		super(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Apply date Restriction in Search
	// Results Page
	// # Function Name : Filter_Apply_Date_Restriction     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void filterApplyDateRestriction(String StartDate, String EndDate) {

		try {
			String strHeader = "Timeline";
			int i = 0;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
					report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.DONE);
				}
			}

			WebElement FromDate = commonLibrary.isExist(UIMAP_SearchResult.txtDateMinValue, 10);
			commonLibrary.setDataInTextBox(FromDate, StartDate, "Start Date");

			WebElement ToDate = commonLibrary.isExist(UIMAP_SearchResult.txtDateMaxValue, 10);
			commonLibrary.setDataInTextBox(ToDate, EndDate, "End Date");

			WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.btnOKDateSelect, 10);
			if (btnOK != null) {
				report.updateTestLog("OK button is enabled", "OK button is enabled", Status.PASS);
				commonLibrary.clickButtonParentWithWait(btnOK, "OK button");
			} else {
				report.updateTestLog("OK button is enabled", "OK button is not enabled", Status.FAIL);
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : SelectPostFilter_Multiple     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void selectPostFilterMultiple(String strHeader, String strFilters) {
		try {
			String[] arrFilters = strFilters.split(";");
			int i = 0;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
					report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.DONE);
				}
			}

			WebElement SelectMultiple = commonLibrary.isExist(UIMAP_SearchResult.btnSelectMultiple, 10);
			if (SelectMultiple != null) {
				commonLibrary.clickButtonLogSmallWait(SelectMultiple, "Select Multiple");
				WebElement SelMultiDialog = commonLibrary.isExist(UIMAP_SearchResult.SelMultiPopUp, 10);
				if (SelMultiDialog != null) {
					report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is displayed", Status.PASS);
					List<WebElement> FilterList = commonLibrary.isExistList(SelMultiDialog, By.tagName("label"), 30);
					for (int j = 0; j < arrFilters.length; j++) {
						for (WebElement item : FilterList) {
							if (item.getText().contains(arrFilters[j])) {
								WebElement ChkBox = commonLibrary.isExist(item, By.tagName("input"), 10);
								commonLibrary.setCheckBox(ChkBox, arrFilters[j]);
								break;
							}
						}
					}

					WebElement btnOK = commonLibrary.isExist(UIMAP_SearchResult.OKSelMultiPopUp, 10);
					commonLibrary.clickButton(btnOK);
				} else {
					report.updateTestLog("Select Multiple Pop Up is displayed", "Select Multiple Pop Up is not displayed", Status.FAIL);
				}

			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

}
