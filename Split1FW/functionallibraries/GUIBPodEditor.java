package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_GUIBPodEditor;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_PodPreview;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBPodEditor extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	private PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	public GUIBPodEditor(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("guibpodeditor"))
				commonLibrary.sleep(5000);

		} while (!url.contains("guibpodeditor") && counter < 40);

		if (!driver.getCurrentUrl().contains("guibpodeditor")) {
			throw new IllegalStateException("GUIB Pod editor page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Information
	// # Function Name : ClickOnInformation     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickOnInformation() {
		WebElement btnInformation = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnInfo, 20);
		if (btnInformation != null) {
			commonLibrary.clickButtonParentWithWait(btnInformation, "Information");
		} else {
			report.updateTestLog("Click on Information", "Information Button is not clicked", Status.FAIL);
		}
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding collapsing tabs in Pod
	// Editor page
	// # Function Name : Expand_Collapse_Icons_PodEditor     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor expandCollapseIconsPodEditor(String IconName, String State) {
		boolean blnFlag = false;
		List<WebElement> divExpCollapse = commonLibrary.isExistList(UIMAP_GUIBPodEditor.divPodEditExpIcon, 10);
		for (WebElement item : divExpCollapse) {
			WebElement header = commonLibrary.isExist(item, By.tagName("h2"), 10);
			if (header.getText().contains(IconName)) {
				String CurrState = item.getAttribute("class");
				WebElement btnExpand = commonLibrary.isExist(item, By.tagName("button"), 10);
				blnFlag = true;
				switch (State) {
				case "Expand": {
					if (CurrState.contains("collapsed")) {
						commonLibrary.clickButtonParentWithWait(btnExpand, IconName);
						report.updateTestLog("Expand " + IconName + " icon", IconName + "icon is expanded", Status.PASS);
					} else if (CurrState.contains("expanded")) {
						report.updateTestLog("Expand " + IconName + " icon", IconName + "icon is expanded", Status.PASS);
					}
					break;
				}
				case "Collapse": {
					if (CurrState.contains("expanded")) {
						commonLibrary.clickButtonParentWithWait(btnExpand, IconName);
						report.updateTestLog("Collapse " + IconName + " icon", IconName + "icon is collapsed", Status.PASS);
					} else if (CurrState.contains("collapsed")) {
						report.updateTestLog("Collapse " + IconName + " icon", IconName + "icon is collapsed", Status.PASS);
					}
					break;
				}
				}
				break;
			}

		}
		if (!blnFlag) {
			report.updateTestLog(State + " " + IconName + "icon", IconName + "icon is not present", Status.FAIL);
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add customer
	// # Function Name : addCustomer     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addCustomer(String strCustomer) {
		WebElement btnAddCustomer = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnAddCustomer, 10);
		commonLibrary.clickButtonLogSmallWait(btnAddCustomer, "Add Customer");
		boolean blnFlag = false;
		WebElement eltCustomerList = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.eltCustomerList, 10);
		List<WebElement> lstCustomerList = commonLibrary.isExistList(eltCustomerList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstCustomerList) {
			if (li.getText().contains(strCustomer)) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Select customer " + strCustomer, strCustomer + "is selected.", Status.PASS);
		else
			report.updateTestLog("Select customer " + strCustomer, "Customer is not selected.", Status.FAIL);

		WebElement btnProceed = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnProceed, 10);
		commonLibrary.clickButtonLogSmallWait(btnProceed, "Proceed");

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add name
	// # Function Name : addName     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addName(String strname) {
		WebElement txtName = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtName, 10);
		commonLibrary.setDataInTextBox(txtName, strname, "Name");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add title
	// # Function Name : addTitle     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addTitle(String strname) {
		WebElement txtTitle = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtTitle, 10);
		commonLibrary.setDataInTextBox(txtTitle, strname, "Title");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add comments
	// # Function Name : addComments     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addComments(String strname) {
		WebElement txtComments = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtComments, 10);
		commonLibrary.setDataInTextBox(txtComments, strname, "Comments");
		WebElement txtTitle = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtTitle, 10);
		Actions action = new Actions(driver);
		action.moveToElement(txtTitle, 10, 10).click().build().perform();
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add client ID
	// # Function Name : addClientId     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addClientId(String ClientID) {
		// WebElement
		// txtHeader=commonLibrary.isExist(UIMAP_GUIBPodEditor.txtHeader,10);
		List<WebElement> lstHeaders = commonLibrary.isExistList(UIMAP_GUIBPodEditor.divClientId, 10);
		for (int i = 0; i < lstHeaders.size(); i++) {
			if (lstHeaders.get(i).getText().contains("Client Id")) {
				WebElement txtClientId = commonLibrary.isExistNegative(lstHeaders.get(i), By.tagName("input"), 10);
				commonLibrary.setDataInTextBox(txtClientId, ClientID, "ClientId");
			}
		}
		// WebElement txtClientId =
		// commonLibrary.isExist_Negative(txtClientIdHeader,
		// UIMAP_GUIBPodEditor.txtClientId, 10);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting radio buttons under
	// CasePul Customizations section
	// # Function Name : ClickRadio_CasePullCustomizations     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickRadioCasePullCustomizations(String RadioName, boolean State) {
		WebElement divCasePullCust = commonLibrary.isExist(UIMAP_GUIBPodEditor.divCasePullCust, 10);
		List<WebElement> li = commonLibrary.isExistList(divCasePullCust, By.tagName("li"), 10);
		for (WebElement item : li) {
			WebElement input = commonLibrary.isExist(item, By.tagName("input"), 10);
			if (item.getText().trim().equalsIgnoreCase(RadioName)) {
				commonLibrary.setRadioButton(input, State);
				break;
			}
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Save button in Pod Editor
	// page
	// # Function Name : Click_Save     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickSave() {
		try {
			WebElement Save = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnSave, 10);
			if (Save != null) {
				// if(!Save.isEnabled())
				// {
				// commonLibrary.clickButton_Log_SmallWait(Save, "Save");
				// commonLibrary.sleep(80000);
				// }
				commonLibrary.clickButtonLogSmallWait(Save, "Save");
				commonLibrary.sleep(45000);
			} else
				report.updateTestLog("Click on Save button", "Save button is not present", Status.FAIL);
			pageCheck.ajaxWait(driver);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Preview button in Pod
	// Editor page
	// # Function Name : Click_Preview     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickPreview() {
		WebElement Preview = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnPreview, 10);
		if (Preview != null)
			commonLibrary.clickButtonLogSmallWait(Preview, "Preview");
		else
			report.updateTestLog("Click on Preview button", "Preview button is not present", Status.FAIL);
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(45000);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Publish button in Pod
	// Editor page
	// # Function Name : Click_Publish   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickPublish() {
		WebElement Publish = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnPublish, 10);
		if (Publish != null)
			commonLibrary.clickButtonLogSmallWait(Publish, "Publish");
		else
			report.updateTestLog("Click on Publish button", "Publish button is not present", Status.FAIL);

		WebElement PublishPopUp = commonLibrary.isExist(UIMAP_GUIBPodEditor.publisherPopup, 10);

		if (PublishPopUp != null) {
			WebElement PublishPopUpBtn = commonLibrary.isExist(PublishPopUp, UIMAP_GUIBPodEditor.btnPublish, 10);
			commonLibrary.clickButton(PublishPopUpBtn);
		} else
			report.updateTestLog("Click on Publish popup button", "Publish popup button is not present", Status.FAIL);
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(45000);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Go to Preview Page button
	// in Pod Editor page
	// # Function Name : Click_GoToPreviewPage   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public PodPreview clickGoToPreviewPage() {
		try {
			WebElement PreviewPage = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnGoToPreviewPage, 10);
			if (PreviewPage != null) {
				commonLibrary.clickButtonLogSmallWait(PreviewPage, "Go To Preview Page");
				commonLibrary.sleep(3000);
				commonLibrary.switchToWindow("Container");
				commonLibrary.sleep(2000);
			} else
				report.updateTestLog("Click on Go To Preview Page button", "Go To Preview Page button is not present", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for enter the values and getting vaules
	// in podpreview page
	// # Function Name : VerifyPreviewPageTitle   
	// # Author : karthi
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public PodPreview getMultipleCitation(String Title, String strvalue) {

		WebElement citationtext = commonLibrary.isExist(UIMAP_PodPreview.citation, 20);
		commonLibrary.setDataInTextBox(citationtext, strvalue, "Enter Multiple Citation");
		commonLibrary.clickButtonWithWait(UIMAP_PodPreview.Get, "Get");
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Verifying Date restriction and Time period
	// # Function Name : VerifyDateTime     
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyDateTime() {
		WebElement btnDateRestriction = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnDateRestriction, 10);
		WebElement btnTimePeriod = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnTimePeriod, 10);
		if (btnDateRestriction != null && btnTimePeriod != null) {

			commonLibrary.setCheckBox(btnDateRestriction, false);
			commonLibrary.setRadioButton(btnTimePeriod, true);
			report.updateTestLog("Unchecking DateRestricion and Checking No time Restriction", "Unchecked DateRestricion and Checked No time Restriction", Status.PASS);
		} else
			report.updateTestLog("Click on DateRestriction & No time restriction button", "DateRestriction & No time restriction button is not clicked", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Click On Edit Jurisdiction
	// # Function Name :       ClickOnEditJurisdiction
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickOnEditJurisdiction() {
		WebElement btnJurisdiction = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnEditJurisdiction, 10);

		if (btnJurisdiction != null) {
			commonLibrary.clickButtonParentWithWait(btnJurisdiction, "EditJurisdiction");

		} else
			report.updateTestLog("Click on EditJurisdictions button", " EditJurisdictions button is not clicked", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Cancel button in Pod
	// Editor page
	// # Function Name : Click_Cancel   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBInstances clickCancel() {
		try {
			WebElement Cancel = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnCancel, 10);
			if (Cancel != null) {
				commonLibrary.clickButtonLogSmallWait(Cancel, "Cancel");
				commonLibrary.sleep(5000);
			} else
				report.updateTestLog("Click on Cancel button", "Cancel button is not present", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		pageCheck.ajaxWait(driver);
		commonLibrary.sleep(50000);
		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Selecting Jurisdictions
	// # Function Name :       SelectingJurisdictions
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor selectingJurisdictions(String strValues) {
		String list[] = strValues.split(";");
		WebElement divUsfederal = commonLibrary.isExist(UIMAP_GUIBPodEditor.divUSfederal, 10);
		if (divUsfederal != null) {
			WebElement chkBox = commonLibrary.isExist(divUsfederal, UIMAP_GUIBPodEditor.chkbox, 10);
			if (chkBox != null) {
				for (int i = 0; i < list.length; i++) {
					if (chkBox.getAttribute("data-jurisdictionname").equalsIgnoreCase(list[i])) {
						commonLibrary.setCheckBox(chkBox, true);
						break;
					} else {
						report.updateTestLog("Click on " + chkBox.getAttribute("data-jurisdictionname") + " Check Box ", "Check Box" + chkBox.getAttribute("data-jurisdictionname") + " is not present", Status.FAIL);

					}

				}
			}
		} else {
			report.updateTestLog("Select Jurisdictions", "No jurisdictions are present", Status.FAIL);
		}
		WebElement divJurisdictions = commonLibrary.isExist(UIMAP_GUIBPodEditor.divJurisdictions, 10);
		if (divJurisdictions != null) {
			List<WebElement> chkBoxList = commonLibrary.isExistList(divJurisdictions, UIMAP_GUIBPodEditor.chkbox, 10);
			if (chkBoxList != null) {
				for (int i = 0; i < chkBoxList.size(); i++) {
					for (int j = 0; j < list.length; j++) {
						if (chkBoxList.get(i).getAttribute("data-jurisdictionname").equalsIgnoreCase(list[j])) {
							commonLibrary.setCheckBox(chkBoxList.get(i), true);
							report.updateTestLog("Click on " + chkBoxList.get(i).getAttribute("data-jurisdictionname") + " Check box", "Check Box" + chkBoxList.get(i).getAttribute("data-jurisdictionname") + " is Clicked", Status.PASS);
						} else {
							// report.updateTestLog("Click on "+chkBoxList.get(i).getAttribute("data-jurisdictionname")+" Check box",
							// "Check Box"+chkBoxList.get(i).getAttribute("data-jurisdictionname")+" is Not Clicked",
							// Status.FAIL);

						}
					}
				}
			}
		}
		WebElement btnOk = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnOk, 10);
		if (btnOk != null) {
			commonLibrary.clickButtonParentWithWait(btnOk, "Ok");
		} else {
			report.updateTestLog("click on Ok button", "Ok button is clicked", Status.FAIL);
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Clear Customer button in
	// Pod Editor page
	// # Function Name : Clear Customer   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickClearCustomer() {
		WebElement ClearCustomer = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnClearCustomer, 10);
		if (ClearCustomer != null)
			commonLibrary.clickButtonLogSmallWait(ClearCustomer, "Clear Customer");
		else
			report.updateTestLog("Click on Clear Customer button", "Clear Customer button is not present", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// # Function Name : Logout   
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut != null)
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
		//
		// // driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);
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
		//
		// }

		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set date filter
	// # Function Name : setDateFilter   
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor setDateFilter(boolean blnCheckStatus, String strRadioLabel) {
		WebElement divDateFilter = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.divDateFilter, 10);
		WebElement chkShowDateFilter = commonLibrary.isExistNegative(divDateFilter, UIMAP_GUIBPodEditor.chkShowDateFilter, 10);
		boolean blnFlag = false;
		if (chkShowDateFilter != null) {
			commonLibrary.setCheckBox(chkShowDateFilter, blnCheckStatus);
			report.updateTestLog("Set Show Date Restriction Option checkbox to" + blnCheckStatus, "Show Date Restriction Option checkbox is set to " + blnCheckStatus, Status.PASS);
		} else
			report.updateTestLog("Set Show Date Restriction Option checkbox to" + blnCheckStatus, "Show Date Restriction Option checkbox is not present", Status.FAIL);
		List<WebElement> lstRadio = commonLibrary.isExistList(divDateFilter, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement item : lstRadio) {
			WebElement input = commonLibrary.isExist(item, By.tagName("input"), 10);
			if (item.getText().trim().equalsIgnoreCase(strRadioLabel)) {
				commonLibrary.setRadioButton(input, strRadioLabel);
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Set Radio button " + strRadioLabel, strRadioLabel + " is set.", Status.PASS);
		else
			report.updateTestLog("Set Radio button " + strRadioLabel, strRadioLabel + " is not present.", Status.FAIL);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add sources to pod by ID.
	// # Function Name : selectSourceById     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor selectSourceById(String strSourceId) {
		WebElement txtSourceId = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtSourceId, 10);
		commonLibrary.setDataInTextBox(txtSourceId, strSourceId, "Enter source identifier");
		WebElement btnSourceId = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnSourceId, 10);
		commonLibrary.clickButtonLogSmallWait(btnSourceId, "Add Source");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add sources to pod by name.
	// # Function Name : selectSourceByName     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor selectSourceByName(String strSourceName) {
		boolean blnFlag = false;
		WebElement txtSourceName = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtSourceName, 10);
		commonLibrary.setDataInTextBox(txtSourceName, strSourceName, "Search for sources");
		WebElement btnSourceName = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnSourceName, 10);
		commonLibrary.clickButtonLogSmallWait(btnSourceName, "Search Sources");
		WebElement lstSourceName = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.lstSourceName, 10);
		List<WebElement> lstSourceNameList = commonLibrary.isExistList(lstSourceName, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstSourceNameList) {
			if (li.getText().contains(strSourceName)) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Add Source" + strSourceName, strSourceName + " is added.", Status.PASS);
		else
			report.updateTestLog("Add Source" + strSourceName, strSourceName + " is not added.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify added source.
	// # Function Name : verifyAddedSource     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyAddedSource(String strSourceName) {
		boolean blnFlag = false;
		WebElement lstSelectedSource = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.lstSelectedSource, 10);
		List<WebElement> lstSelectedSourceList = commonLibrary.isExistList(lstSelectedSource, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstSelectedSourceList) {
			if (li.getText().contains(strSourceName)) {
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Verify " + strSourceName + "is selected", strSourceName + " is selected.", Status.PASS);
		else
			report.updateTestLog("Verify " + strSourceName + "is selected", strSourceName + " is not selected.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to edit customers.
	// # Function Name : editCustomer     
	// # Author : Pratik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor editCustomer(String strCustomer) {
		WebElement btnEditCustomer = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnEditCustomer, 10);
		commonLibrary.clickButtonLogSmallWait(btnEditCustomer, "Edit Customer");
		boolean blnFlag = false;
		WebElement eltCustomerList = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.eltCustomerList, 10);
		List<WebElement> lstCustomerList = commonLibrary.isExistList(eltCustomerList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstCustomerList) {
			if (li.getText().contains(strCustomer)) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				blnFlag = true;
				break;
			}
		}
		if (blnFlag)
			report.updateTestLog("Select customer " + strCustomer, strCustomer + "is selected.", Status.PASS);
		else
			report.updateTestLog("Select customer " + strCustomer, "Customer is not selected.", Status.FAIL);

		WebElement btnProceed = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnProceed, 10);
		commonLibrary.clickButtonLogSmallWait(btnProceed, "Proceed");

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify comments empty
	// # Function Name : verifyCommentsEmpty     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyCommentsEmpty() {
		WebElement txtComments = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtComments, 10);
		if (txtComments.getText().equals("")) {
			report.updateTestLog("Verify Comments textbox is empty", "Comments textbox is empty.", Status.PASS);
		} else
			report.updateTestLog("Verify Comments textbox is empty", "Comments textbox is not empty.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to remove source
	// # Function Name : removeSource     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor removeSource(String strSourceName) {
		boolean blnFlag = false;
		WebElement lstSelectedSource = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.lstSelectedSource, 10);
		List<WebElement> lstSelectedSourceList = commonLibrary.isExistList(lstSelectedSource, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstSelectedSourceList) {
			if (li.getText().contains(strSourceName)) {
				WebElement eltRemove = commonLibrary.isExistNegative(li, UIMAP_GUIBPodEditor.eltdiv, 10);
				commonLibrary.clickLinkWithWebElementWithWait(eltRemove, "Remove Source");
				break;
			}
		}

		lstSelectedSource = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.lstSelectedSource, 10);
		lstSelectedSourceList = commonLibrary.isExistList(lstSelectedSource, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement li : lstSelectedSourceList) {
			if (li.getText().contains(strSourceName)) {
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag)
			report.updateTestLog("Verify " + strSourceName + "is deleted", strSourceName + " is deleted.", Status.PASS);
		else
			report.updateTestLog("Verify " + strSourceName + "is deleted", strSourceName + " is not deleted.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify pod image
	// # Function Name : VerifyPodImage     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyPodImage() {

		WebElement txtheader = commonLibrary.isExist(UIMAP_GUIBPodEditor.headerPod, 10);
		// WebElement
		// imgPod=commonLibrary.isExist(UIMAP_GUIBPodEditor.imgPod,10);
		// WebElement txtTitle =
		// commonLibrary.isExist_Negative(UIMAP_GUIBPodEditor.txtTitle, 10);

		if (txtheader.getText().contains("Search Box")) {
			WebElement imgPod = commonLibrary.isExist(txtheader, UIMAP_GUIBPodEditor.imgPod, 10);
			if (imgPod.getAttribute("src").contains("icon")) {
				report.updateTestLog("Verify Pod Image", "Pod Image is not Displayed", Status.FAIL);
			}
		}
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected jurisdictions
	// # Function Name : VerifySelectedJurisdictions     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifySelectedJurisdictions(String List) {
		int count = 0;
		String[] JurisList = List.split(";");
		WebElement divSelected = commonLibrary.isExist(UIMAP_GUIBPodEditor.divSelctdJrsdns, 10);
		if (divSelected != null) {
			List<WebElement> lstJuris = commonLibrary.isExistList(divSelected, UIMAP_GUIBPodEditor.divJurisTitle, 10);
			for (int i = 0; i < lstJuris.size(); i++) {
				if (lstJuris.get(i).getText().contains(JurisList[i])) {
					count++;
				}
			}
			if (count == JurisList.length) {
				report.updateTestLog("Verify Selected jurisdictions", "Selected Jurisdictions are " + List + " displayed under selected jurisdictions", Status.PASS);
			} else {
				report.updateTestLog("Verify Selected jurisdictions", "Selected Jurisdictions are not  displayed under selected jurisdictions", Status.FAIL);

			}
		}

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify categories displayed
	// # Function Name : VerifyCategoriesDisplayed     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyCategoriesDisplayed(String List) {
		int count = 0;
		String[] JurisList = List.split(";");
		WebElement divSelected = commonLibrary.isExist(UIMAP_GUIBPodEditor.divSelctdJrsdns, 10);
		if (divSelected != null) {
			List<WebElement> lstCategory = commonLibrary.isExistList(divSelected, UIMAP_GUIBPodEditor.txtCategory, 10);
			for (int i = 0; i < lstCategory.size(); i++) {
				if (lstCategory.get(i).getText().equals("No")) {
					count++;
				}
			}
			if (count == JurisList.length) {
				report.updateTestLog("Verify Categories under jurisdictions", "No categories displayed Under each categories", Status.PASS);
			} else {
				report.updateTestLog("Verify Categories under jurisdictions", "Categories displayed Under each categories", Status.FAIL);

			}
		}

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to history footer link
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor navigateToHistoryFooterLink(String strLinkName) {

		try {
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}

			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow, "History");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtViewAllHistory, "View All History");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory, "View All History");
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap, "Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap, "Research Map");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click edit categories
	// # Function Name : ClickEditCategories     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickEditCategories(String Jurisdiction) {
		boolean blnFlag = false;
		WebElement divSelected = commonLibrary.isExist(UIMAP_GUIBPodEditor.divSelctdJrsdns, 10);
		List<WebElement> lstJurisTitle = commonLibrary.isExistList(divSelected, UIMAP_GUIBPodEditor.divJurisTitle, 10);

		for (int i = 0; i < lstJurisTitle.size(); i++) {
			if (lstJurisTitle.get(i).getText().contains(Jurisdiction)) {
				WebElement lnkCategory = commonLibrary.isExist(lstJurisTitle.get(i), UIMAP_GUIBPodEditor.lnkEditCategory, 10);
				if (lnkCategory != null) {
					commonLibrary.clickButtonParentWithWait(lnkCategory, "Edit Category");
					blnFlag = true;

				} else {
					report.updateTestLog("Click on Edit Categories Button", "Click On Edit Categories Button is not clicked", Status.FAIL);
				}
			}

		}
		if (!blnFlag) {

			report.updateTestLog("Click On Edit Categories under " + Jurisdiction + " Jurisdiction", "Click On Edit Categories under " + Jurisdiction + " Jurisdiction is not clicked", Status.FAIL);

		}

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Selecting Categories
	// # Function Name :       SelectingCategories
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #********************************************************************************************************
	public GUIBPodEditor selectingCategories(String Categories) {
		String[] lstCategories = Categories.split(";");
		WebElement divcategories = commonLibrary.isExist(UIMAP_GUIBPodEditor.divCategoreis, 10);
		if (divcategories != null) {
			List<WebElement> chkBox = commonLibrary.isExistList(divcategories, UIMAP_GUIBPodEditor.chkbox, 10);
			if (chkBox != null) {
				for (int i1 = 0; i1 < chkBox.size(); i1++) {
					for (int j = 0; j < lstCategories.length; j++) {
						if (chkBox.get(i1).getAttribute("data-contenttypename").equals(lstCategories[j])) {
							commonLibrary.setCheckBox(chkBox.get(i1), true);
							report.updateTestLog("Click on " + lstCategories[j] + " Check box", "Check Box " + lstCategories[j] + " is Clicked", Status.PASS);

						}
					}
				}
			}
		}
		WebElement btnOk = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnOk1, 10);
		if (btnOk != null) {
			commonLibrary.clickButtonParentWithWait(btnOk, "Ok");
		} else {
			report.updateTestLog("click on Ok button", "Ok button is not clicked", Status.FAIL);
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear customer
	// # Function Name : ClearCustomer     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clearCustomer() {
		WebElement divCustDetails = commonLibrary.isExist(UIMAP_GUIBPodEditor.divCustDetail, 10);
		if (divCustDetails != null) {
			WebElement btnClearCustomer = commonLibrary.isExist(divCustDetails, UIMAP_GUIBPodEditor.btnClearCust, 10);
			if (btnClearCustomer != null) {
				commonLibrary.clickButtonParentWithWait(btnClearCustomer, "Clear Customer");
			} else {
				report.updateTestLog("Click on ClearCustomer Button", "ClearCustomer Button is not clicked", Status.FAIL);
			}
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click AddSource_EditSource
	// # Function Name : ClickAddSource_Editsource     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickAddSourceEditSource(String JurisdictionName, String CategoryEdit) {
		WebElement divSelectedJuris1 = commonLibrary.isExist(UIMAP_GUIBPodEditor.divSelctdJrsdns, 10);
		if (divSelectedJuris1 != null) {
			List<WebElement> divJurisDisplay = commonLibrary.isExistList(divSelectedJuris1, UIMAP_GUIBPodEditor.divJurisTitle, 10);
			int k = divJurisDisplay.size();
			for (int j = 0; j < k; j++) {

				if (divJurisDisplay.get(j).getText().contains(JurisdictionName)) {

					List<WebElement> divSelectedContentList = commonLibrary.isExistList(divJurisDisplay.get(j), UIMAP_GUIBPodEditor.eltlist1, 10);

					if (divSelectedContentList != null) {
						for (WebElement item : divSelectedContentList) {
							if (item.getText().contains(CategoryEdit)) {
								WebElement Editsources = commonLibrary.isExist(item, UIMAP_GUIBPodEditor.btneditSources, 20);
								commonLibrary.clickButtonParentWithWait(Editsources, "Edit Sources");
							}
						}
					}
				}
			}
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify error message
	// # Function Name : VerifyErrorMsg     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyErrorMsg() {
		WebElement btnSave = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnSave, 10);
		WebElement txtTitle = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtTitle, 10);
		WebElement btnClear = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnClrCstmr, 10);
		if (btnSave != null && txtTitle != null && btnClear != null) {
			if (btnSave.getAttribute("disabled").equals("disabled") && txtTitle.getAttribute("class").equals("fielderror")) {
				report.updateTestLog("Verify Error Message", "Save Buttom is displayed and Title Box highlighted as red", Status.PASS);

			} else {
				report.updateTestLog("Verify Error Message", "Save Buttom is not displayed  and Title Box not in highlighted in red", Status.PASS);

			}
		}
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting sources
	// # Function Name : SelectingSources     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor selectingSources(String Source) {
		WebElement lstSources = commonLibrary.isExist(UIMAP_GUIBPodEditor.lstSource, 10);
		List<WebElement> lstSource = commonLibrary.isExistList(lstSources, UIMAP_GUIBPodEditor.source, 10);
		if (lstSource != null && lstSources != null) {

			for (int i = 0; i < lstSource.size(); i++) {
				// WebElement
				// col1=commonLibrary.isExist(lstSource.get(i),UIMAP_GUIBPodEditor.coloumn1,10);
				// WebElement
				// col2=commonLibrary.isExist(lstSource.get(i),UIMAP_GUIBPodEditor.coloumn2,10);
				// WebElement
				// sourceAdd=commonLibrary.isExist(col2,UIMAP_GUIBPodEditor.sourceAdd,10);
				if (lstSource.get(i).getText().contains(Source)) {
					WebElement AddSource = commonLibrary.isExist(lstSource.get(i + 1), UIMAP_GUIBPodEditor.sourceAdd, 10);
					commonLibrary.clickButtonParentWithWait(AddSource, "Add Source");
				}
				// if(col1!=null && col2!=null && sourceAdd!=null)
				// {
				// if(col1.getText().equalsIgnoreCase(Source1))
				// {
				// commonLibrary.clickButton_Parent_WithWait(sourceAdd,
				// "Add Source");
				// }
				// }
			}
		}

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Clear all sources
	// # Function Name : Click_ClearAllSources     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickClearAllSources() {
		WebElement ClearAll = commonLibrary.isExist(UIMAP_GUIBPodEditor.lnkClearall, 10);
		commonLibrary.clickLinkWithWebElementWithWait(ClearAll, "Clear All");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click ok save sources
	// # Function Name : Click_OK_SaveSources     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickOKSaveSources() {
		WebElement OKSaveSources = commonLibrary.isExist(UIMAP_GUIBPodEditor.oKSaveSources, 10);
		commonLibrary.clickButtonParentWithWait(OKSaveSources, "OK");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Save button in Pod Editor
	// page
	// # Function Name : click_Savebtn     
	// # Author : karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************

	public GUIBInstances clickSaveBtn() {
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
		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting options in Get & Print pod.
	// # Function Name : getPrintSettingsBasic     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor getPrintSettingsBasic(String checkList, String unCheckList) {
		checkList = ";" + checkList + ";";
		unCheckList = ";" + unCheckList + ";";

		WebElement podGetPrintCont = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.podGetPrintCont, 10);
		WebElement basicCont = commonLibrary.isExistNegative(podGetPrintCont, UIMAP_GUIBPodEditor.eltdiv, 10);
		List<WebElement> items = commonLibrary.isExistList(basicCont, UIMAP_GUIBPodEditor.lstTagListItems, 10);

		for (WebElement item : items) {
			WebElement input = commonLibrary.isExistNegative(item, UIMAP_GUIBPodEditor.input, 10);
			if (input != null) {
				WebElement label = commonLibrary.isExistNegative(item, UIMAP_GUIBPodEditor.label, 10);
				String option = ";" + label.getText().trim() + ";";
				if (checkList.toLowerCase().contains(option.toLowerCase())) {
					if (input.getAttribute("type").toLowerCase().contains("checkbox")) {
						commonLibrary.setCheckBox(input, true);
						report.updateTestLog("Set the check box " + label.getText(), "The check box " + label.getText() + " has been set", Status.DONE);
					} else {
						commonLibrary.setRadioButton(input, label.getText());
					}
				} else if (unCheckList.toLowerCase().contains(option.toLowerCase())) {
					commonLibrary.setCheckBox(input, false);
					report.updateTestLog("Clear the check box " + label.getText(), "The check box " + label.getText() + " has been clear", Status.DONE);
				}
			} else {
				WebElement label = commonLibrary.isExistNegative(item, UIMAP_GUIBPodEditor.label, 10);
				if (label != null) {
					String option = ";" + label.getText().trim() + ";";
					if (checkList.toLowerCase().contains(option.toLowerCase()))
						report.updateTestLog("Set the check box/radio button " + label.getText(), "The check box/radio button " + label.getText() + " has not been set", Status.FAIL);
					else if (unCheckList.toLowerCase().contains(option.toLowerCase())) {
						report.updateTestLog("Clear the check box " + label.getText(), "The check box " + label.getText() + " has not been cleared", Status.FAIL);
					}
				}
			}
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting options in Get & Print pod.
	// # Function Name : getPrintSettingsFormat     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor getPrintSettingsFormat(String checkShow, String unCheckShow, String checkSetDefault, String unCheckSetDefault) {

		checkShow = ";" + checkShow + ";";
		unCheckShow = ";" + unCheckShow + ";";

		unCheckSetDefault = ";" + unCheckSetDefault + ";";

		WebElement podGetPrintCont = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.podGetPrintCont, 10);
		WebElement formTable = commonLibrary.isExistNegative(podGetPrintCont, UIMAP_GUIBPodEditor.table, 10);
		WebElement customCont = commonLibrary.getParentElement(formTable);
		// WebElement customCont = commonLibrary.isExist_Negative(customDiv,
		// UIMAP_GUIBPodEditor.customCont, 10);
		WebElement editorLabel = commonLibrary.isExistNegative(customCont, UIMAP_GUIBPodEditor.editorLabel, 10);

		WebElement editorText = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.label, 10);
		if (editorText != null) {
			String text = ";" + editorText.getText().trim() + ";";
			if (checkShow.toLowerCase().contains(text.toLowerCase())) {
				WebElement check = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
				if (check != null) {
					commonLibrary.setCheckBox(check, true);
					report.updateTestLog("Set the check box " + editorText.getText(), "The check box " + editorText.getText() + " has been set", Status.DONE);
				} else
					report.updateTestLog("Set the check box " + editorText.getText(), "The check box " + editorText.getText() + " is not present", Status.FAIL);
			} else if (unCheckShow.toLowerCase().contains(text.toLowerCase())) {
				WebElement check1 = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
				if (check1 != null) {
					commonLibrary.setCheckBox(check1, true);
					report.updateTestLog("Clear the check box " + editorText.getText(), "The check box " + editorText.getText() + " has been cleared", Status.DONE);
				} else
					report.updateTestLog("Clear the check box " + editorText.getText(), "The check box " + editorText.getText() + " is not present", Status.FAIL);
			}

			WebElement table = commonLibrary.isExistNegative(customCont, UIMAP_GUIBPodEditor.table, 10);
			List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_GUIBPodEditor.row, 10);
			int rowcount = 0;
			if (rows.size() > 1) {
				for (WebElement row : rows) {

					List<WebElement> inputList = commonLibrary.isExistList(row, UIMAP_GUIBPodEditor.input, 10);
					if (inputList.size() > 0) {
						rowcount++;
						WebElement label = commonLibrary.isExistNegative(row, UIMAP_GUIBPodEditor.label, 10);
						if (label != null) {
							String rowText = ";" + label.getText().trim() + ";";
							List<WebElement> dataList = commonLibrary.isExistList(row, UIMAP_GUIBPodEditor.data, 10);
							if (checkShow.toLowerCase().contains(rowText.toLowerCase())) {
								WebElement input = commonLibrary.isExistNegative(dataList.get(0), UIMAP_GUIBPodEditor.input, 10);
								if (input != null) {
									commonLibrary.setCheckBox(input, true);
									report.updateTestLog("Set the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has been set", Status.DONE);
								} else
									report.updateTestLog("Set the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has not been set", Status.FAIL);
							} else if (unCheckShow.toLowerCase().contains(rowText.toLowerCase())) {
								WebElement input = commonLibrary.isExistNegative(dataList.get(0), UIMAP_GUIBPodEditor.input, 10);
								if (input != null) {
									commonLibrary.setCheckBox(input, false);
									report.updateTestLog("Clear the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has been cleared", Status.DONE);
								} else
									report.updateTestLog("Clear the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has not been cleared", Status.FAIL);

								if (inputList.size() > 1) {
									if (checkSetDefault.toLowerCase().contains(label.getText().toLowerCase())) {
										WebElement input1 = commonLibrary.isExistNegative(dataList.get(1), UIMAP_GUIBPodEditor.input, 10);
										if (input1 != null) {
											commonLibrary.setCheckBox(input1, true);
											report.updateTestLog("Set the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has been set", Status.DONE);
										} else
											report.updateTestLog("Set the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has not been set", Status.FAIL);
									} else if (unCheckSetDefault.toLowerCase().contains(label.getText().toLowerCase())) {
										WebElement input1 = commonLibrary.isExistNegative(dataList.get(1), UIMAP_GUIBPodEditor.input, 10);
										if (input1 != null) {
											commonLibrary.setCheckBox(input1, false);
											report.updateTestLog("Clear the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has been cleared", Status.DONE);
										} else
											report.updateTestLog("Clear the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has not been cleared", Status.FAIL);
									}
								} else if (checkSetDefault.contains(label.getText())) {
									String part = checkSetDefault.split(label.getText())[1];
									String part1 = part.split(";")[0];
									String option = part1.replace("$", "").trim();
									if (!option.equals("")) {
										WebElement select = commonLibrary.isExistNegative(row, UIMAP_GUIBPodEditor.select, 10);
										if (select != null) {
											commonLibrary.selectFromListOptionContains(select, option);
											if (commonLibrary.selectIsSelected(select, option))
												report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The option " + option + " from dropdown " + label.getText() + " is selected", Status.DONE);
											else
												report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The option " + option + " from dropdown " + label.getText() + " is not selected", Status.FAIL);
										} else
											report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The dropdown " + label.getText() + " is not present", Status.FAIL);
									}
								}

							}
						} else
							report.updateTestLog("Select settings for row" + rowcount, "The label for row " + rowcount + " is not present", Status.FAIL);
					}
				}
			} else
				report.updateTestLog("Select format settings.", "The table is not present", Status.FAIL);

		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting options in Get & Print pod.
	// # Function Name : getPrintSettingsContentSpecific     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor getPrintSettingsContentSpecific(String checkShow, String unCheckShow, String checkSetDefault, String unCheckSetDefault, String dropdown) {

		checkShow = ";" + checkShow + ";";
		unCheckShow = ";" + unCheckShow + ";";
		checkSetDefault = ";" + checkSetDefault + ";";
		unCheckSetDefault = ";" + unCheckSetDefault + ";";

		WebElement podGetPrintCont = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.podGetPrintCont, 10);
		List<WebElement> tableList = commonLibrary.isExistList(podGetPrintCont, UIMAP_GUIBPodEditor.table, 10);
		WebElement listItem = commonLibrary.getParentElement(tableList.get(1));
		WebElement list = commonLibrary.getParentElement(listItem);
		WebElement listCont = commonLibrary.getParentElement(list);
		WebElement customCont = commonLibrary.getParentElement(listCont);
		WebElement editorLabel = commonLibrary.isExistNegative(customCont, UIMAP_GUIBPodEditor.editorLabel, 10);

		WebElement editorText = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.label, 10);
		if (editorText != null) {
			String text = ";" + editorText.getText().trim() + ";";
			if (checkShow.toLowerCase().contains(text.toLowerCase())) {
				WebElement check = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
				if (check != null) {
					commonLibrary.setCheckBox(check, true);
					report.updateTestLog("Set the check box " + editorText.getText(), "The check box " + editorText.getText() + " has been set", Status.DONE);
				} else
					report.updateTestLog("Set the check box " + editorText.getText(), "The check box " + editorText.getText() + " is not present", Status.FAIL);
			} else if (unCheckShow.toLowerCase().contains(text.toLowerCase())) {
				WebElement check = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
				if (check != null) {
					commonLibrary.setCheckBox(check, false);
					report.updateTestLog("Clear the check box " + editorText.getText(), "The check box " + editorText.getText() + " has been cleared", Status.DONE);
				} else
					report.updateTestLog("Clear the check box " + editorText.getText(), "The check box " + editorText.getText() + " is not present", Status.FAIL);
			}
			int blockCount = 0;
			List<WebElement> blocks = commonLibrary.isExistList(customCont, UIMAP_GUIBPodEditor.lstTagListItems, 10);
			for (WebElement block : blocks) {
				blockCount++;
				WebElement editorLabel1 = commonLibrary.isExistNegative(block, UIMAP_GUIBPodEditor.editorLabel, 10);

				WebElement editorText1 = commonLibrary.isExistNegative(editorLabel1, UIMAP_GUIBPodEditor.label, 10);

				if (editorText1 != null) {
					String subText = ";" + editorText1.getText().trim() + ";";
					if (checkShow.toLowerCase().contains(subText.toLowerCase())) {
						WebElement check1 = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
						if (check1 != null) {
							commonLibrary.setCheckBox(check1, true);
							report.updateTestLog("Set the check box " + editorText1.getText(), "The check box " + editorText1.getText() + " has been set", Status.DONE);
						} else
							report.updateTestLog("Set the check box " + editorText1.getText(), "The check box " + editorText1.getText() + " is not present", Status.FAIL);
					} else if (unCheckShow.toLowerCase().contains(subText.toLowerCase())) {
						WebElement check1 = commonLibrary.isExistNegative(editorLabel, UIMAP_GUIBPodEditor.input, 10);
						if (check1 != null) {
							commonLibrary.setCheckBox(check1, true);
							report.updateTestLog("Clear the check box " + editorText1.getText(), "The check box " + editorText1.getText() + " has been cleared", Status.DONE);
						} else
							report.updateTestLog("Clear the check box " + editorText1.getText(), "The check box " + editorText1.getText() + " is not present", Status.FAIL);

						WebElement table = commonLibrary.isExistNegative(block, UIMAP_GUIBPodEditor.table, 10);
						List<WebElement> rows = commonLibrary.isExistList(table, UIMAP_GUIBPodEditor.row, 10);
						int rowcount = 0;
						if (rows.size() > 1) {
							for (WebElement row : rows) {

								List<WebElement> inputList = commonLibrary.isExistList(row, UIMAP_GUIBPodEditor.input, 10);
								if (inputList.size() > 0) {
									rowcount++;
									WebElement label = commonLibrary.isExistNegative(row, UIMAP_GUIBPodEditor.label, 10);
									if (label != null) {
										String rowText = ";" + label.getText().trim() + ";";
										List<WebElement> dataList = commonLibrary.isExistList(row, UIMAP_GUIBPodEditor.data, 10);
										if (checkShow.toLowerCase().contains(rowText.toLowerCase())) {
											WebElement input = commonLibrary.isExistNegative(dataList.get(0), UIMAP_GUIBPodEditor.input, 10);
											if (input != null) {
												commonLibrary.setCheckBox(input, true);
												report.updateTestLog("Set the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has been set", Status.DONE);
											} else
												report.updateTestLog("Set the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has not been set", Status.FAIL);

										} else if (unCheckShow.toLowerCase().contains(rowText.toLowerCase())) {
											WebElement input = commonLibrary.isExistNegative(dataList.get(0), UIMAP_GUIBPodEditor.input, 10);
											if (input != null) {
												commonLibrary.setCheckBox(input, false);
												report.updateTestLog("Clear the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has been cleared", Status.DONE);
											} else
												report.updateTestLog("Clear the 'Show in Interface' check box " + label.getText(), "The 'Show in Interface' check box " + label.getText() + " has not been cleared", Status.FAIL);

											if (inputList.size() > 1) {
												if (checkSetDefault.toLowerCase().contains(label.getText().toLowerCase())) {
													WebElement input1 = commonLibrary.isExistNegative(dataList.get(1), UIMAP_GUIBPodEditor.input, 10);
													if (input1 != null) {
														commonLibrary.setCheckBox(input1, true);
														report.updateTestLog("Set the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has been set", Status.DONE);
													} else
														report.updateTestLog("Set the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has not been set", Status.FAIL);
												} else if (unCheckSetDefault.toLowerCase().contains(label.getText().toLowerCase())) {
													WebElement input1 = commonLibrary.isExistNegative(dataList.get(1), UIMAP_GUIBPodEditor.input, 10);
													if (input1 != null) {
														commonLibrary.setCheckBox(input1, false);
														report.updateTestLog("Clear the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has been cleared", Status.DONE);
													} else
														report.updateTestLog("Clear the 'Set as Default' check box " + label.getText(), "The 'Set as Default' check box " + label.getText() + " has not been cleared", Status.FAIL);
												}
											} else if (blockCount == 1 && rowcount == 1) {

												String option = dropdown.trim();
												WebElement select = commonLibrary.isExistNegative(row, UIMAP_GUIBPodEditor.select, 10);
												if (select != null) {
													commonLibrary.selectFromListOptionContains(select, option);
													if (commonLibrary.selectIsSelected(select, option))
														report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The option " + option + " from dropdown " + label.getText() + " is selected", Status.DONE);
													else
														report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The option " + option + " from dropdown " + label.getText() + " is not selected", Status.FAIL);
												} else
													report.updateTestLog("Select the option " + option + " from dropdown " + label.getText(), "The dropdown " + label.getText() + " is not present", Status.FAIL);
											}

										}
									} else
										report.updateTestLog("Select settings for row" + rowcount, "The label for row " + rowcount + " is not present", Status.FAIL);
								}
							}
						} else
							report.updateTestLog("Select format settings.", "The table is not present", Status.FAIL);

					}
				} else
					report.updateTestLog("Select settings for block " + blockCount, "The block is not present", Status.FAIL);

			}

		} else
			report.updateTestLog("Set the Content Specific settings for Get & Print Pod.", "The Content Specific settings for Get & Print Pod.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the format of Customer Name
	// # Function Name : verifyCustomerName     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor verifyCustomerName(String name) {
		WebElement customerDiv = commonLibrary.isExist(UIMAP_GUIBPodEditor.customerDiv, 10);
		WebElement customerHeader = commonLibrary.isExist(customerDiv, UIMAP_GUIBPodEditor.customerHeader, 10);
		if (customerHeader != null) {
			if (customerHeader.getText().trim().equalsIgnoreCase("Customer")) {
				WebElement customerDetails = commonLibrary.isExist(customerDiv, UIMAP_GUIBPodEditor.customerDetail, 10);
				WebElement customerName = commonLibrary.isExist(customerDetails, UIMAP_GUIBPodEditor.customerSpan, 10);
				if (customerName != null) {
					if (customerName.getText().toLowerCase().contains(name.toLowerCase())) {
						report.updateTestLog("Verify Cuatomer name format", "The customer name " + name + " is displayed below Customer", Status.PASS);
					} else {
						report.updateTestLog("Verify Cuatomer name format", "The customer name " + name + " is not displayed below Customer", Status.FAIL);
					}
				} else {
					report.updateTestLog("Verify Cuatomer name format", "The customer name " + name + " is not displayed", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Verify Cuatomer name format", "The word Customer is not displayed", Status.FAIL);
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************

	// # Function Description : Function to select practice area and verify the
	// same
	// # Function Name : selectPracticeAreaAndVerify     
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor selectPracticeAreaAndVerify(String dropdown, String value) {

		String option = dropdown.trim();
		boolean flag = false;
		WebElement select = commonLibrary.isExist(UIMAP_GUIBPodEditor.practiceAreaDrpdwn, 10);
		if (select != null) {
			commonLibrary.selectByVisibleTextByValue(select, value, dropdown);
			if (commonLibrary.selectIsSelected(select, option)) {
				WebElement setPracarea = commonLibrary.isExist(UIMAP_GUIBPodEditor.setPracAreaBtn, 10);
				if (setPracarea != null) {
					commonLibrary.clickButtonParentWithWait(setPracarea, "Select Practice Area");
				}
			}
			WebElement selectedPrac = commonLibrary.isExist(UIMAP_GUIBPodEditor.selectedPracarea, 10);

			List<WebElement> selected = commonLibrary.isExistList(selectedPrac, UIMAP_GUIBPodEditor.seelctedList, 10);
			if (selected != null) {
				for (WebElement item : selected) {
					if (item.getText().toLowerCase().contains(dropdown.toLowerCase())) {
						flag = true;
						break;
					}
				}

			}
			if (flag)
				report.updateTestLog("Verify selected option " + dropdown + " appears under selected practice area/jurisdiction", "Selected option  " + dropdown + " appears under selected practice area/jurisdiction", Status.PASS);

			else
				report.updateTestLog("Verify selected option " + dropdown + " appears under selected practice area/jurisdiction", "Selected option  " + dropdown + " does not appears under selected practice area/jurisdiction", Status.FAIL);

		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for setting options in Practice Area
	// customization
	// # Function Name : checkUncheckcpracAreaCustomization     
	// # Author : seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor checkUncheckcpracAreaCustomization(String checkbox, String radio, String option) {
		boolean flag = false;
		boolean radios = false;
		WebElement podGetPrintCont = commonLibrary.isExist(UIMAP_GUIBPodEditor.podPracArea, 10);
		WebElement basicCont = commonLibrary.isExist(podGetPrintCont, UIMAP_GUIBPodEditor.datefilterdiv, 10);
		if (basicCont != null) {
			WebElement chk = commonLibrary.isExistNegative(basicCont, UIMAP_GUIBPodEditor.showdateChk, 10);
			if (chk != null && checkbox != "") {
				if (option.toLowerCase().equals("check")) {
					commonLibrary.setCheckBox(chk, true);
					flag = true;

				}

				else if (option.toLowerCase().equals("uncheck")) {
					commonLibrary.setCheckBox(chk, false);
					flag = true;
				}
			}
		}
		if (basicCont != null) {
			List<WebElement> rad = commonLibrary.isExistList(basicCont, UIMAP_GUIBPodEditor.radio, 10);
			if (rad != null && radio != "") {
				for (WebElement item : rad) {
					if (item.getAttribute("value").equalsIgnoreCase(radio)) {
						commonLibrary.setRadioButton(item, radio);
						radios = true;
						break;
					}
				}

			}
		}

		if (flag) {
			report.updateTestLog("" + checkbox + " show date restriction option checkbox", "show date restriction option checkbox is checked/unchecked as expected", Status.PASS);
		} else {
			report.updateTestLog("" + checkbox + " show date restriction option checkbox", "show date restriction option checkbox is checked/unchecked as expected", Status.FAIL);
		}

		if (radios) {
			report.updateTestLog("select " + radio + " radio button", "radio button for " + radio + " is selected", Status.PASS);
		} else {
			report.updateTestLog("select " + radio + " radio button", "radio button for " + radio + " is not selected", Status.FAIL);
		}

		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting radio buttons under
	// Treatises Customizations section
	// # Function Name : clickRadio_TreatisesCustomizations     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickRadio_TreatisesCustomizations(String RadioName, boolean State) {
		WebElement divCasePullCust = commonLibrary.isExist(UIMAP_GUIBPodEditor.treatisesCont, 10);
		List<WebElement> li = commonLibrary.isExistList(divCasePullCust, By.tagName("li"), 10);
		for (WebElement item : li) {
			WebElement input = commonLibrary.isExist(item, By.tagName("input"), 10);
			if (input != null && item.getText().trim().equalsIgnoreCase(RadioName)) {
				commonLibrary.setRadioButton(input, State);
				break;
			}
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Save button is disabled.
	// # Function Name : verifySaveDisabled     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifySaveDisabled() {

		WebElement HeaderBtns = commonLibrary.isExist(UIMAP_GUIBPodEditor.headerbtn, 10);
		WebElement Save = commonLibrary.isExist(HeaderBtns, UIMAP_GUIBPodEditor.btnSave, 10);

		if (Save != null) {
			if (!Save.isEnabled()) {
				report.updateTestLog("Verify Save button is disabled.", "Save button is disabled.", Status.PASS);
			} else
				report.updateTestLog("Verify Save button is disabled.", "Save button is not disabled.", Status.FAIL);
		} else
			report.updateTestLog("Verify Save button is disabled.", "Save button is not present", Status.FAIL);

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting radio buttons under
	// Treatises Customizations section
	// # Function Name : clickRadio_TreatisesCustomizations     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickAddPublishers() {
		// WebElement divCasePullCust =
		// commonLibrary.isExist(UIMAP_GUIBPodEditor.treatisesCont, 10);
		WebElement addPublishers = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.addPublishers, 10);
		commonLibrary.clickButtonParentWithWait(addPublishers, "Add Publishers");
		return new GUIBPodEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for adding a publisher in Add Publisher
	// Dialog Box.
	// # Function Name : addPublishers     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor addPublisher(String pubName) {
		boolean flag = true;
		WebElement addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
		WebElement allPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.allPublisherList, 10);
		List<WebElement> publishers = commonLibrary.isExistList(allPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement publish : publishers) {
			if (publish.getText().toLowerCase().contains(pubName.toLowerCase())) {
				flag = true;
				WebElement inputButton = commonLibrary.isExistNegative(publish, UIMAP_GUIBPodEditor.inputButton, 10);
				if (inputButton != null && inputButton.isDisplayed()) {
					commonLibrary.clickButtonParentWithWait(inputButton, "Add for publisher: " + pubName);
					report.updateTestLog("Add publisher: " + pubName, "Add button for publisher " + pubName + " is clicked", Status.DONE);
				} else {
					if (publish.getText().toLowerCase().contains("selected")) {
						flag = true;
						report.updateTestLog("Add publisher: " + pubName, "Publisher " + pubName + " is already added.", Status.DONE);
					} else {
						report.updateTestLog("Add publisher: " + pubName, "Add button for publisher " + pubName + " is not present", Status.FAIL);
						flag = false;
					}

				}
				break;

			}

		}
		if (!flag)
			report.updateTestLog("Add publisher: " + pubName, "Publisher " + pubName + " could not be added.", Status.FAIL);
		else {
			addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
			WebElement allPublisherList1 = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.allPublisherList, 10);
			List<WebElement> publishers1 = commonLibrary.isExistList(allPublisherList1, UIMAP_GUIBPodEditor.lstTagListItems, 10);
			for (WebElement publisher : publishers1) {
				if (publisher.getText().toLowerCase().contains(pubName.toLowerCase())) {
					if (publisher.getText().toLowerCase().contains("selected"))
						report.updateTestLog("Verify 'Selected'  text  displays for the publisher " + pubName + " in Select Publishers section.", "'Selected'  text  displays for the publisher " + pubName + " in Select Publishers section.", Status.PASS);
					else
						report.updateTestLog("Verify 'Selected'  text  displays for the publisher " + pubName + " in Select Publishers section.", "'Selected'  text does not  display for the publisher " + pubName + " in Select Publishers section.", Status.FAIL);
					break;
				}
			}
			boolean flag1 = false;
			addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
			WebElement selPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.selectedPublisherList, 10);
			publishers1 = commonLibrary.isExistList(selPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
			for (WebElement pub : publishers1) {
				if (pub.getText().toLowerCase().contains(pubName.toLowerCase())) {
					flag1 = true;
					WebElement button = commonLibrary.isExistNegative(pub, UIMAP_Home.button, 10);
					if (button != null)
						report.updateTestLog("Verify publisher name " + pubName + " displays under Added Publishers section with X icon.", "Publisher name " + pubName + " displays under Added Publishers section with X icon.", Status.PASS);
					else
						report.updateTestLog("Verify publisher name " + pubName + " displays under Added Publishers section with X icon.", "Publisher name " + pubName + " displays under Added Publishers section without X icon.", Status.FAIL);
					break;
				}
			}
			if (!flag1)
				report.updateTestLog("Verify publisher name " + pubName + " displays under Added Publishers section with X icon.", "Publisher name " + pubName + " does not display under Added Publishers section.", Status.FAIL);

		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for removing added publisher.
	// # Function Name : addPublishers     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor removeAddedPublisher(String pubName) {
		boolean flag1 = false;
		WebElement addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
		WebElement selPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.selectedPublisherList, 10);
		List<WebElement> publishers1 = commonLibrary.isExistList(selPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement pub : publishers1) {
			if (pub.getText().toLowerCase().contains(pubName.toLowerCase())) {
				flag1 = true;
				WebElement button = commonLibrary.isExistNegative(pub, UIMAP_Home.button, 10);
				if (button != null) {
					commonLibrary.clickButtonParentWithWait(button, "Remove Publisher: " + pubName);
				} else
					report.updateTestLog("Remove publisher " + pubName + " from Added Publishers section.", "Publisher name " + pubName + " displays under Added Publishers section without X icon.", Status.FAIL);
				break;
			}
		}
		if (!flag1)
			report.updateTestLog("Remove publisher " + pubName + " from Added Publishers section.", "Publisher name " + pubName + " does not display under Added Publishers section.", Status.FAIL);
		else {
			boolean flag = false;
			addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
			selPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.selectedPublisherList, 10);
			publishers1 = commonLibrary.isExistList(selPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
			for (WebElement pub : publishers1) {
				if (pub.getText().toLowerCase().contains(pubName.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify publisher " + pubName + " is removed from Added Publishers section.", "Publisher " + pubName + " is present in Added Publishers section.", Status.FAIL);
					break;
				}

			}
			if (!flag)
				report.updateTestLog("Verify publisher " + pubName + " is removed from Added Publishers section.", "Publisher " + pubName + " is removed from Added Publishers section.", Status.PASS);
		}
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for saving added publisher.
	// # Function Name : clickSaveButtonInDialog     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor clickSaveButtonInDialog() {
		WebElement addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
		WebElement savePublishers = commonLibrary.isExist(addPubDialog, UIMAP_GUIBPodEditor.savePublishers, 10);
		commonLibrary.clickButtonParentWithWait(savePublishers, "Save Publishers");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying added publishers.
	// # Function Name : verifyAddedPublisher     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyAddedPublisher(String pubName) {
		boolean flag = false;
		WebElement divCasePullCust = commonLibrary.isExist(UIMAP_GUIBPodEditor.treatisesCont, 10);
		WebElement selectedPubCont = commonLibrary.isExistNegative(divCasePullCust, UIMAP_GUIBPodEditor.selectedPubCont, 10);
		List<WebElement> selPubsList = commonLibrary.isExistList(selectedPubCont, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		for (WebElement item : selPubsList) {
			if (item.getText().toLowerCase().contains(pubName.toLowerCase())) {
				flag = true;
				report.updateTestLog("Verify publisher name " + pubName + " is present in Publishers section.", "Publisher name " + pubName + " is present in Publishers section.", Status.PASS);
			}
		}
		if (!flag)
			report.updateTestLog("Verify publisher name " + pubName + " is present in Publishers section.", "Publisher name " + pubName + " is not present in Publishers section.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting radio buttons under
	// Treatises Customizations section
	// # Function Name : clickRadio_TreatisesCustomizations     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyUnderInformation(String header, String value) {
		boolean flag = false;
		WebElement page = commonLibrary.isExist(UIMAP_GUIBPodEditor.pagedetail, 10);
		List<WebElement> col = commonLibrary.isExistList(page, UIMAP_GUIBPodEditor.column, 10);
		if (col != null) {
			List<WebElement> head = commonLibrary.isExistList(col.get(1), UIMAP_GUIBPodEditor.hdr, 10);
			List<WebElement> span = commonLibrary.isExistList(col.get(1), UIMAP_GUIBPodEditor.span, 10);
			for (int i = 0; i < head.size(); i++) {
				if (head.get(i).getText().equalsIgnoreCase(header)) {
					if (span.get(i).getText().equalsIgnoreCase(value)) {
						flag = true;
						break;
					}
				}
			}
		}
		if (flag) {
			report.updateTestLog("Verify " + header + " is " + value + "", "" + header + " is " + value + "", Status.PASS);
		} else {
			report.updateTestLog("Verify " + header + " is " + value + "", "" + header + " is not " + value + "", Status.FAIL);
		}

		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for filtering publishers.
	// # Function Name : enterTextVerifyPublisher     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor enterTextVerifyPublisher(String pubName) {
		WebElement addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
		WebElement narrowPublishers = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.narrowPublishers, 10);
		if (narrowPublishers != null) {
			commonLibrary.setDataInTextBox(narrowPublishers, pubName, "Narrow by Publisher");
			WebElement findPublishers = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.findPublishers, 10);
			if (findPublishers != null)
				commonLibrary.clickButtonParentWithWait(findPublishers, "Find Publishers");
			else
				report.updateTestLog("Narrow publishers by " + pubName, "Find Publishers is not present.", Status.FAIL);

			addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
			WebElement allPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.allPublisherList, 10);
			List<WebElement> publishers = commonLibrary.isExistList(allPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
			boolean flag = false;
			for (WebElement pub : publishers) {
				if (pub.getText().toLowerCase().contains(pubName.toLowerCase()) && pub.isDisplayed()) {
					flag = true;
					report.updateTestLog("Verify " + pubName + " displayes in the Select Publishers Section.", pubName + " displays in the Select Publishers Section.", Status.PASS);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + pubName + " displayes in the Select Publishers Section.", pubName + " does not display in the Select Publishers Section.", Status.FAIL);
		} else
			report.updateTestLog("Narrow publishers by " + pubName, "Narrow by publisher text box is not present.", Status.FAIL);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for filtering publishers.
	// # Function Name : enterTextVerifyPublisher     
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public GUIBPodEditor verifyAllPublishers(String publisherNames) {
		WebElement addPubDialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.addPubDialog, 10);
		WebElement allPublisherList = commonLibrary.isExistNegative(addPubDialog, UIMAP_GUIBPodEditor.allPublisherList, 10);
		List<WebElement> publishers = commonLibrary.isExistList(allPublisherList, UIMAP_GUIBPodEditor.lstTagListItems, 10);
		String[] pubList = publisherNames.split(";");
		for (String pub : pubList) {
			boolean flag = false;
			for (WebElement item : publishers) {
				if (item.getText().toLowerCase().contains(pub.toLowerCase())) {
					flag = true;
					report.updateTestLog("Verify " + pub + " displayes in the Select Publishers Section.", pub + " displays in the Select Publishers Section.", Status.PASS);
					break;
				}
			}
			if (!flag)
				report.updateTestLog("Verify " + pub + " displayes in the Select Publishers Section.", pub + " does not display in the Select Publishers Section.", Status.FAIL);
		}
		return new GUIBPodEditor(scriptHelper);
	}
}
