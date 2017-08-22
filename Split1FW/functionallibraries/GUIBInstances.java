package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import UIMAP.UIMAP_GUIBInstances;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_GUIBPodEditor;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBInstances extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private PageCheck pageCheck = new PageCheck(scriptHelper);

	public GUIBInstances(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("guibinstances"))
				commonLibrary.sleep(5000);

		} while (!url.contains("guibinstances") && counter < 40);

		if (!driver.getCurrentUrl().contains("guibinstances")) {
			throw new IllegalStateException("GUIB instances page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking create pod/clone under a
	// pod name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor createPodUnderPodName(String PodName, String btnName) {
		boolean blnPod = false;
		boolean blnFlag = false;
		List<WebElement> li = null;
		do {
			switch (btnName) {
			case "Create Pod": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liPods, 20);
				break;
			}
			case "Clone": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				break;
			}
			}

			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().toLowerCase().contains(PodName.toLowerCase())) {

					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnClone, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, btnName);
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", btnName + " under'" + PodName + "' is clicked", Status.PASS);
					blnFlag = true;
					blnPod = true;

					WebElement Cancel = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnCancel, 10);
					int count = 0;
					do {
						commonLibrary.sleep(20000);
						Cancel = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnCancel, 10);
						count++;
					} while (Cancel == null && count < 20);

					break;
				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");
					pageCheck.ajaxWait(driver);

				} else {
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking create pod under a pod
	// name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor createPageUnderPageName(String strpagename) {
		boolean blnPod = false;
		boolean blnFlag = false;
		do {

			List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liPage, 20);
			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().toLowerCase().contains(strpagename.toLowerCase())) {
					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnClone, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, "Create Page");
					blnFlag = true;
					blnPod = true;
					break;
				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click Create Page under'" + strpagename + "' .", "'" + strpagename + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switching between pods
	// # Function Name : Switch_Content    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances switchContent(String ContentName) {
		boolean blnFlag = false;
		WebElement ulContents = commonLibrary.isExist(UIMAP_GUIBInstances.ulContents, 10);
		List<WebElement> liContent = commonLibrary.isExistList(ulContents, By.tagName("li"), 10);
		for (WebElement item : liContent) {
			if (item.getText().equalsIgnoreCase(ContentName)) {
				WebElement button = commonLibrary.isExist(item, By.tagName("button"), 10);
				commonLibrary.clickButtonLogSmallWait(button, ContentName);
				blnFlag = true;
				break;
			}
		}
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(30000);
		if (!blnFlag)
			report.updateTestLog("Click on the Content " + ContentName, ContentName + " content is not present", Status.FAIL);
		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod status under a pod
	// name
	// # Function Name : Verify_PodStatus     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances verifyPodStatus(String PodName, String PodStatus) {
		boolean blnPod = false;
		boolean blnFlag = false;
		boolean blnExist = false;

		do {
			List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().contains(PodName)) {
					// commonLibrary.ScrollToView(item);
					WebElement divContent = commonLibrary.isExist(item, UIMAP_GUIBInstances.divContent, 10);
					List<WebElement> DataTitle = commonLibrary.isExistList(divContent, By.tagName("dt"), 10);
					List<WebElement> DataDefn = commonLibrary.isExistList(divContent, By.tagName("dd"), 10);
					for (int i = 0; i < DataTitle.size(); i++) {
						if ((DataTitle.get(i).getText().equalsIgnoreCase("Pod Status:")) && (DataDefn.get(i).getText().equalsIgnoreCase(PodStatus))) {
							blnExist = true;
							report.updateTestLog("Pod Status section displays as " + PodStatus + " in pod " + PodName, "Pod Status section displays as " + PodStatus + " in pod " + PodName, Status.PASS);
							break;
						}

					}

					if (blnExist) {

						blnFlag = true;
						blnPod = true;
						break;
					}

				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click Create Pod under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on pod name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor clickPodName(String PodName) {
		try {
			boolean blnPod = false;
			boolean blnFlag = false;
			do {
				List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				for (WebElement item : li) {
					WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.lnkdoctitle, 10);
					if (PodTitle.getText().contains(PodName)) {
						// commonLibrary.highlightElement(PodTitle);

						commonLibrary.clickLinkWithWebElementWithWait(PodTitle, PodName);

						blnFlag = true;
						blnPod = true;
						break;
					}
				}
				if (blnPod == false) {
					WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
					if (NextPage != null) {
						commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");
						pageCheck.ajaxWait(driver);

					} else {
						report.updateTestLog("Click Pod Name'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
						blnFlag = true;
					}
				}
			} while (!blnFlag);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// # Function Description : Function for clicking create pod under a pod
	// name
	// # Function Name : ClickAddtopageUnderGivenPod     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor clickAddtopageUnderGivenPod(String strpagename) {
		boolean blnPod = false;
		boolean blnFlag = false;
		do {

			List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().toLowerCase().contains(strpagename.toLowerCase())) {
					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnAddtoPage, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, "Add to page");
					blnFlag = true;
					blnPod = true;
					break;
				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click Add to page under'" + strpagename + "' .", "'" + strpagename + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Post Filter in GUIB Instances
	// Page
	// # Function Name : SelectPostFilter_GUIB     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBInstances selectPostFilterGUIB(String strHeader, String strFilter) {
		try {
			int i = 0;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_GUIBInstances.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), strHeader);
					report.updateTestLog("Expanding Filter Header: " + strHeader, "Filter Header Expanded.", Status.PASS);
				}
			}

			List<WebElement> eltFilter = commonLibrary.isExistList(UIMAP_GUIBInstances.labelFilter, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					commonLibrary.clickButtonParentWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					commonLibrary.sleep(50000);
					report.updateTestLog("Selecting Filter: " + strFilter, "Required Filter Selected.", Status.PASS);
					break;
				}
			}

			pageCheck.ajaxWait(driver);
			commonLibrary.sleep(80000);
			return new GUIBInstances(scriptHelper);
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod status under a pod
	// name
	// # Function Name : Verify_PodStatus     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances verifyDraftFilter() {

		int notExist = 0;
		pageCheck.ajaxWait(driver);
		List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
		for (WebElement item : li) {
			WebElement divContent = commonLibrary.isExist(item, UIMAP_GUIBInstances.divContent, 10);
			List<WebElement> DataTitle = commonLibrary.isExistList(divContent, By.tagName("dt"), 10);
			List<WebElement> DataDefn = commonLibrary.isExistList(divContent, By.tagName("dd"), 10);
			for (int i = 0; i < DataTitle.size(); i++) {
				if ((DataTitle.get(i).getText().equalsIgnoreCase("Pod Status:")) && (!(DataDefn.get(i).getText().equalsIgnoreCase("Draft")))) {
					notExist++;
					break;
				}

			}

		}
		if (notExist > 0)
			report.updateTestLog("The list of configured pods filtered by draft status", "The list of configured pods not filtered by draft status", Status.FAIL);
		else
			report.updateTestLog("The list of configured pods filtered by draft status", "The list of configured pods filtered by draft status", Status.PASS);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for removing a filter
	// # Function Name : Remove_Filter     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances removeFilter(String FilterName) {
		boolean blnFlag = false;
		WebElement FiltersUsed = commonLibrary.isExist(UIMAP_GUIBInstances.ulFiltersUsed, 20);
		List<WebElement> Filters = commonLibrary.isExistList(FiltersUsed, UIMAP_GUIBInstances.btnRemoveFilter, 10);
		for (WebElement item : Filters) {
			if (item.getText().toLowerCase().equalsIgnoreCase(FilterName.toLowerCase())) {
				commonLibrary.clickButtonParentWithWait(item, FilterName + " Remove");
				report.updateTestLog(FilterName + " filter is removed", FilterName + " filter is removed", Status.PASS);
				blnFlag = true;
			}
		}
		if (!blnFlag) {
			report.updateTestLog(FilterName + " filter is removed", FilterName + " filter is not removed", Status.FAIL);
		}
		pageCheck.ajaxWait(driver);
		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Research page by
	// clicking Lexis Advance arrow
	// # Function Name : NavigateToReserachPage     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Research navigateToResearchPage() {
		try {
			WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);

			// WebElement btnLexisAdvanceResearch =
			// commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
			WebElement btnLexisAdvanceResearch = commonLibrary.isExist(UIMAP_GUIBInstances.lnkLexisAdvanceResearch1, 20);
			commonLibrary.clickLinkWithWebElement(btnLexisAdvanceResearch, "Lexis Advance® Research");
			commonLibrary.sleep(3000);

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Home.CurrentProduct, 20);
			if (CurrentProduct.getText().contains("Research")) {
				report.updateTestLog("Research landing page is displayed", "Research landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Research landing page is displayed", "Research landing page is not displayed", Status.FAIL);
			}

			return new Research(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod status under a pod
	// name
	// # Function Name : Verify_PublishedFilter     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances verifyPublishedFilter() {

		int notExist = 0;

		List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
		for (WebElement item : li) {
			WebElement divContent = commonLibrary.isExist(item, UIMAP_GUIBInstances.divContent, 10);
			List<WebElement> DataTitle = commonLibrary.isExistList(divContent, By.tagName("dt"), 10);
			List<WebElement> DataDefn = commonLibrary.isExistList(divContent, By.tagName("dd"), 10);
			for (int i = 0; i < DataTitle.size(); i++) {
				if ((DataTitle.get(i).getText().equalsIgnoreCase("Pod Status:")) && (!(DataDefn.get(i).getText().equalsIgnoreCase("Published")))) {
					notExist++;
				}

			}
		}
		if (notExist > 0)
			report.updateTestLog("The list of configured pods filtered by published status", "The list of configured pods not filtered by published status", Status.FAIL);
		else
			report.updateTestLog("The list of configured pods filtered by published status", "The list of configured pods filtered by published status", Status.PASS);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : logout     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if (browsername.contains("internet")) {
				commonLibrary.clickMethod(btnMore, "More");
			} else {
				commonLibrary.clickMethod(btnMore, "More");
			}
		}
		WebElement lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickJS(btnMore, "More");
				else
					commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		}

		if (lnkSignOut != null)
			if (browsername.contains("internet")) {
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			} else
				commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);

		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Document Link
	// # Function Name : ClickDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public GUIBInstances clickDocLink2(String strDocTitle) {

		Boolean blnFlag = false;
		WebElement resultClass = null;
		int i = 0;
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		for (i = 0; i < 5; i++) {
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null && eleDocTitle.getText().toLowerCase().equals(strDocTitle.toLowerCase())) {
							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								// commonLibrary.ScrollToView(lnkDocument);
								// commonLibrary.highlightElement(lnkDocument);
								if (browsername.contains("internet")) {
									commonLibrary.clickJS(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								} else {
									commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
									blnFlag = true;
									break;
								}

							}
						}

					}

				}
			}
			if (!blnFlag) {
				WebElement btnNextPage = commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(btnNextPage, "Next Page");
				else
					commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
			} else
				break;
		}

		if (blnFlag) {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify document " + strDocTitle + " is displayed", "document " + strDocTitle + " is not displayed", Status.FAIL);
		}

		return new GUIBInstances(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click save button.
	// # Function Name : click_Save     
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickSave() {
		try {
			WebElement Save = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnSave, 10);
			if (Save != null) {
				commonLibrary.clickButtonLogSmallWait(Save, "Save");
				commonLibrary.sleep(5000);
			} else
				report.updateTestLog("Click on Save button", "Save button is not present", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking create pod/clone under a
	// pod name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor createButtonUnderPodNameEquals(String PodName, String btnName) {
		boolean blnPod = false;
		boolean blnFlag = false;
		List<WebElement> li = null;
		do {
			switch (btnName) {
			case "Create Pod": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liPods, 20);
				break;
			}
			case "Clone": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				break;
			}
			}

			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().equalsIgnoreCase(PodName)) {

					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnClone, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, btnName);
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", btnName + " under'" + PodName + "' is clicked", Status.PASS);
					blnFlag = true;
					blnPod = true;
					break;
				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking create pod/clone under a
	// pod name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor createPodunderPodNameEquals(String PodName, String btnName) {
		boolean blnPod = false;
		boolean blnFlag = false;
		List<WebElement> li = null;
		do {
			switch (btnName) {
			case "Create Pod": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liPods, 20);
				break;
			}
			case "Clone": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				break;
			}
			}

			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().equalsIgnoreCase(PodName)) {

					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnClone, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, btnName);
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", btnName + " under'" + PodName + "' is clicked", Status.PASS);
					blnFlag = true;
					blnPod = true;
					break;
				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod status under a pod
	// name
	// # Function Name : Verify_PodStatus     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances verifyPodUpdatedBy(String PodName, String PodStatus) {
		boolean blnPod = false;
		boolean blnFlag = false;
		boolean blnExist = false;

		do {
			List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().contains(PodName)) {
					// commonLibrary.ScrollToView(item);
					WebElement divContent = commonLibrary.isExist(item, UIMAP_GUIBInstances.divContent, 10);
					List<WebElement> DataTitle = commonLibrary.isExistList(divContent, By.tagName("dt"), 10);
					List<WebElement> DataDefn = commonLibrary.isExistList(divContent, By.tagName("dd"), 10);
					for (int i = 0; i < DataTitle.size(); i++) {
						if (DataTitle.get(i).getText().equalsIgnoreCase("Last Updated By:")) {
							if (DataDefn.get(i).getText().equalsIgnoreCase(PodStatus)) {
								blnExist = true;
								report.updateTestLog("Last Updated By section displays as " + PodStatus + " in pod " + PodName, "Last Updated By section displays as " + PodStatus + " in pod " + PodName, Status.PASS);
								break;
							} else {
								report.updateTestLog("Last Updated By section displays as " + PodStatus + " in pod " + PodName, "Last Updated By section not displayed as " + PodStatus + " in pod " + PodName, Status.FAIL);
								break;
							}
						}

					}

					if (blnExist) {

						blnFlag = true;
						blnPod = true;
						break;
					}

				}
			}
			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click Create Pod under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking create pod/clone under a
	// pod name
	// # Function Name : CreatePod_under_PodName     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBInstances deleteButtonUnderPodNameEquals(String PodName, String btnName) {
		boolean blnPod = false;
		boolean blnFlag = false;
		List<WebElement> li = null;

		do {
			switch (btnName) {
			case "Create Pod": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liPods, 20);
				break;
			}
			case "Clone": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				break;
			}
			case "Suspend": {
				li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
				break;
			}
			}

			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().equalsIgnoreCase(PodName)) {

					// commonLibrary.ScrollToView(item);
					WebElement btnCreatePod = commonLibrary.isExist(item, UIMAP_GUIBInstances.btnSuspend, 10);
					commonLibrary.clickButtonParentWithWait(btnCreatePod, btnName);
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", btnName + " under'" + PodName + "' is clicked", Status.PASS);
					WebElement suspend = commonLibrary.isExist(UIMAP_GUIBInstances.confirmSuspend, 10);
					commonLibrary.clickButtonParentWithWaitJS(suspend, "Confirm suspend");
					break;
				}
			}
			List<WebElement> li2 = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
			for (WebElement item1 : li2) {
				WebElement PodTitle1 = commonLibrary.isExist(item1, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle1.getText().equalsIgnoreCase(PodName)) {
					WebElement btnDelete = commonLibrary.isExist(item1, UIMAP_GUIBInstances.btnDelete, 10);
					commonLibrary.clickButtonParentWithWait(btnDelete, "Delete");
					WebElement del = commonLibrary.isExist(UIMAP_GUIBInstances.confirmDelete, 10);
					commonLibrary.clickButtonParentWithWait(del, "Confirm Delete");
					report.updateTestLog("Verify the practice area is removed", "" + PodName + " is removed successfully", Status.PASS);
					blnFlag = true;
					blnPod = true;
					break;
				}
			}

			if (blnPod == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click " + btnName + " under'" + PodName + "' .", "'" + PodName + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for details under a pod name
	// # Function Name : verifyPodDetails     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public GUIBInstances verifyPodDetails(String podName, String details) {
		String[] detailsList = details.split(";");
		pageCheck.ajaxWait(driver);
		List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPods, 20);
		for (WebElement item : li) {
			WebElement podTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.lnkdoctitle, 10);
			if (podTitle != null && podTitle.getText().toLowerCase().contains(podName.toLowerCase())) {
				report.updateTestLog("Verify Pod Name is present in the Pods Details Section.", "Pod Name is present in the Pods Details Section.", Status.PASS);
				for (String text : detailsList) {
					if (item.getText().toLowerCase().replace("\n", " ").contains(text.toLowerCase())) {
						report.updateTestLog("Verify " + text + " is present in the Pods Details Section.", text + " is present in the Pods Details Section.", Status.PASS);
					} else
						report.updateTestLog("Verify " + text + " is present in the Pods Details Section.", text + " is not present in the Pods Details Section.", Status.FAIL);

				}
				break;
			} else if (podTitle == null)
				report.updateTestLog("Verify Pod Name is present in the Pods Details Section.", "Pod Name is not present in the Pods Details Section.", Status.FAIL);
		}
		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking CopyCode under a page
	// name
	// # Function Name : clickCopyCodeUnderPageName     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public GUIBInstances clickCopyCodeUnderPageName(String strpagename) {
		boolean blnPage = false;
		boolean blnFlag = false;
		do {

			List<WebElement> li = commonLibrary.isExistList(UIMAP_GUIBInstances.liAllPage, 20);
			for (WebElement item : li) {
				WebElement PodTitle = commonLibrary.isExist(item, UIMAP_GUIBInstances.doctitle, 10);
				if (PodTitle.getText().toLowerCase().contains(strpagename.toLowerCase())) {
					// commonLibrary.ScrollToView(item);
					WebElement copyCode = commonLibrary.isExist(item, UIMAP_GUIBInstances.copyCode, 10);
					commonLibrary.clickButtonParentWithWait(copyCode, "Copy code");
					blnFlag = true;
					blnPage = true;
					break;
				}
			}
			if (blnPage == false) {
				WebElement NextPage = commonLibrary.isExist(UIMAP_GUIBInstances.linkNextPage, 10);
				if (NextPage != null) {
					commonLibrary.clickButtonParentWithWait(NextPage, "Next Page");

				} else {
					report.updateTestLog("Click Copycode under'" + strpagename + "' .", "'" + strpagename + "' is not present", Status.FAIL);
					blnFlag = true;
				}
			}
		} while (!blnFlag);

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to copy the code and paste in new tab
	// # Function Name : copyCodeOpenNewTab     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public GUIBContainer copyCodeOpenNewTab() {
		WebElement copyCodePopup = commonLibrary.isExist(UIMAP_GUIBInstances.copyCodePopup, 10);
		WebElement copyCodeText = commonLibrary.isExist(copyCodePopup, UIMAP_GUIBInstances.copyCodeText, 10);
		if (copyCodeText != null) {
			String[] code = copyCodeText.getText().trim().split("\"");
			WebElement body = commonLibrary.isExist(UIMAP_GUIBInstances.body, 10);
			body.sendKeys(Keys.CONTROL + "t");
			commonLibrary.sleep(10000);
			commonLibrary.switchToWindowByTitle("NewTab");
			driver.get(code[5]);
			pageCheck.ajaxWait(driver);
			// commonLibrary.SwitchToWindow("container");
		}

		return new GUIBContainer(scriptHelper);
	}

}
