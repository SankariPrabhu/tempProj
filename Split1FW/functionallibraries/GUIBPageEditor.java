package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_GUIBPageeditor;
import UIMAP.UIMAP_GUIBPodEditor;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBPageEditor extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();

	public GUIBPageEditor(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("guibpageeditor"))
				commonLibrary.sleep(5000);

		} while (!url.contains("guibpageeditor") && counter < 40);

		if (!driver.getCurrentUrl().toLowerCase().contains("guibpageeditor")) {
			throw new IllegalStateException("GUIB Page Editor page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding collapsing tabs in Pod
	// Editor page
	// # Function Name : Expand_Collapse_Icons_PageEditor     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor expandCollapseIconsPageEditor(String IconName, String State) {
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
						break;
					} else if (CurrState.contains("expanded")) {
						report.updateTestLog("Expand " + IconName + " icon", IconName + "icon is expanded", Status.PASS);
						break;
					}
					break;
				}
				case "Collapse": {
					if (CurrState.contains("expanded")) {
						commonLibrary.clickButtonParentWithWait(btnExpand, IconName);
						report.updateTestLog("Collapse " + IconName + " icon", IconName + "icon is collapsed", Status.PASS);
						break;
					} else if (CurrState.contains("collapsed")) {
						report.updateTestLog("Collapse " + IconName + " icon", IconName + "icon is collapsed", Status.PASS);
						break;
					}
					break;
				}
				}
			}

		}
		if (!blnFlag) {
			report.updateTestLog(State + " " + IconName + "icon", IconName + "icon is not present", Status.FAIL);
		}

		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add customer
	// # Function Name : addCustomer     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor addCustomer(String strCustomer) {
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

		return new GUIBPageEditor(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add title
	// # Function Name : addTitle     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor addTitle(String strname) {
		WebElement txtTitle = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtTitle, 10);
		if (txtTitle.getAttribute("value") != null) {
			strname = txtTitle.getAttribute("value");
			report.updateTestLog("Enter title", "Title is displayed as " + strname + "", Status.PASS);
		} else {
			commonLibrary.setDataInTextBox(txtTitle, strname, "Title");
		}
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add comments
	// # Function Name : addComments     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor addComments(String strname) {
		WebElement txtComments = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtComments, 10);
		commonLibrary.setDataInTextBox(txtComments, strname, "Comments");
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on Add Pod button
	// # Function Name : ClickAddPod     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBInstances clickAddPod() {
		WebElement btnAddPod = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.btnAddPod, 10);
		commonLibrary.clickButtonLogSmallWait(btnAddPod, "Add Pod");
		return new GUIBInstances(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whther clearcustomer button is
	// displayed
	// # Function Name : verifyClearCustomer     
	// # Author : Seetha
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor verifyClearCustomer() {
		WebElement btnClearCust = commonLibrary.isExistNegative(UIMAP_GUIBPageeditor.txtClearCustomer, 10);
		if (btnClearCust != null) {
			report.updateTestLog("Verify Clear Customer is displayed and assign customer is not displayed", "Clear Customer is displayed and assign customer is not displayed", Status.PASS);
		}
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify pods are added
	// # Function Name : VerifyAddedPod     
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor verifyAddedPod(String strPodname) {

		boolean blnFlag = false;
		List<WebElement> liPods = commonLibrary.isExistList(UIMAP_GUIBPageeditor.liPods, 10);
		for (WebElement item : liPods) {
			if (item.getText().toLowerCase().contains(strPodname.toLowerCase())) {
				blnFlag = true;
				break;
			}
		}
		if (blnFlag) {
			report.updateTestLog("Verify " + strPodname + " is added", "" + strPodname + " is added", Status.PASS);
		} else {
			report.updateTestLog("Verify " + strPodname + " is addedr", "" + strPodname + " is not added", Status.FAIL);
		}
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Preview button in Pod
	// Editor page
	// # Function Name : Click_Preview     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor clickPreview() {
		WebElement Preview = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnPreview, 10);
		if (Preview != null)
			commonLibrary.clickButtonLogSmallWait(Preview, "Preview");
		else
			report.updateTestLog("Click on Preview button", "Preview button is not present", Status.FAIL);
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Go to Preview Page button
	// in Pod Editor page
	// # Function Name : Click_GoToPreviewPage   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBContainer clickGoToPreviewPage() {
		WebElement PreviewPage = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnGoToPreviewPage, 10);
		if (PreviewPage != null) {
			commonLibrary.clickButtonLogSmallWait(PreviewPage, "Go To Preview Page");
			commonLibrary.sleep(10000);
			commonLibrary.switchToWindow("container");
		} else
			report.updateTestLog("Click on Go To Preview Page button", "Go To Preview Page button is not present", Status.FAIL);

		return new GUIBContainer(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add name
	// # Function Name : addName     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor addName(String strname) {
		WebElement txtName = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtName, 10);
		if (txtName != null) {
			commonLibrary.setDataInTextBox(txtName, strname, "Name");
		} else {
			report.updateTestLog("Set Name", "Name Text Box is not displayed", Status.FAIL);
		}
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickAddTabbyIndexAddTabName
	// # Function Name : clickAddTabbyIndexAddTabName     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor clickAddTabbyIndexAddTabName(int index, String tabName) {
		WebElement sectionTab = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.sectionTab, 10);
		List<WebElement> listTab = commonLibrary.isExistList(sectionTab, UIMAP_GUIBPodEditor.tabs, 20);
		if (listTab != null) {
			WebElement buttonAddTab = commonLibrary.isExist(listTab.get(index), UIMAP_GUIBPodEditor.tabButton, 20);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(buttonAddTab, "+Add Tab");
			else
				commonLibrary.clickLinkWithWebElementWithWait(buttonAddTab, "+Add Tab");
		} else {
			report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
		}
		WebElement dialog = commonLibrary.isExist(UIMAP_GUIBPodEditor.dilaog, 20);
		WebElement txtBoxTab = commonLibrary.isExist(dialog, UIMAP_GUIBPodEditor.tabTitle, 20);
		if (txtBoxTab != null) {
			commonLibrary.setDataInTextBox(txtBoxTab, tabName, "Tab Title");
			WebElement buttonSave = commonLibrary.isExist(dialog, UIMAP_GUIBPodEditor.btnSave1, 20);
			if (buttonSave != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(buttonSave, "Save");
				else
					commonLibrary.clickButtonParentWithWait(buttonSave, "Save");
			}
		} else {
			report.updateTestLog("Set Tab Name", "Tab Name Text Box is not displayed", Status.FAIL);
		}

		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to guibAddPodHeader
	// # Function Name : guibAddPodHeader     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBInstances guibAddPodHeader(String section) {
		WebElement pod = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.sectionCustomization, 10);
		if (pod != null) {
			switch (section) {
			case "header": {
				WebElement addPodHeader = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.headerSection, 10);
				if (addPodHeader != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodHeader, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			case "left": {
				WebElement addPodLeft = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.leftSection, 10);
				if (addPodLeft != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodLeft, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			case "right": {
				WebElement addPodRight = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.rightSection, 10);
				if (addPodRight != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodRight, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}

			}
		} else {
			report.updateTestLog("Click on Add pod under " + section + " Section", "Add Pod is not Clicked", Status.FAIL);
		}

		// WebElement
		// dialog=commonLibrary.isExist(UIMAP_GUIBPodEditor.dilaog,20);
		// WebElement
		// txtBoxTab=commonLibrary.isExist(dialog,UIMAP_GUIBPodEditor.tabTitle,20);
		// if(txtBoxTab!=null)
		// {
		// commonLibrary.SetDataInTextBox(txtBoxTab, tabName, "Tab Title");
		// WebElement
		// buttonSave=commonLibrary.isExist(dialog,UIMAP_GUIBPodEditor.btnSave1,20);
		// if(buttonSave!=null)
		// {
		// if (browsername.contains("internet"))
		// commonLibrary.click_JS(buttonSave, "Save");
		// else
		// commonLibrary.clickButton_Parent_WithWait(buttonSave, "Save");
		// }
		// }
		// else
		// {
		// report.updateTestLog("Set Tab Name",
		// "Tab Name Text Box is not displayed", Status.FAIL);
		// }

		return new GUIBInstances(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the format of Customer Name
	// # Function Name : verifyCustomerName     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor verifyCustomerName(String name) {
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
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Save button in Page Editor
	// page
	// # Function Name : Click_Save     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor clickSave() {
		try {
			WebElement Save = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnSave, 10);
			if (Save != null) {
				// if(!Save.isEnabled())
				// {
				// commonLibrary.clickButton_Log_SmallWait(Save, "Save");
				// commonLibrary.sleep(80000);
				// }
				commonLibrary.clickButtonLogSmallWait(Save, "Save");
				commonLibrary.sleep(15000);
			} else
				report.updateTestLog("Click on Save button", "Save button is not present", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add tab's under Customizations
	// # Function Name : clickAddTabbyIndex     
	// # Author : Baswaraj
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBInstances guibAddPodSection(String section) {
		WebElement pod = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.sectionCustomization, 10);
		if (pod == null) {
			pod = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.sectionPods, 10);
		}
		if (pod != null) {
			switch (section) {
			case "header": {
				WebElement addPodHeader = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.headerSection, 10);
				if (addPodHeader != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodHeader, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			case "left": {
				WebElement addPodLeft = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.leftSection, 10);
				if (addPodLeft != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodLeft, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			case "right": {
				WebElement addPodRight = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.rightSection, 10);
				if (addPodRight != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodRight, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			case "center": {
				WebElement addPodRight = commonLibrary.isExistNegative(pod, UIMAP_GUIBPodEditor.centerSection, 10);
				if (addPodRight != null) {
					WebElement buttonAddTab = commonLibrary.isExist(addPodRight, UIMAP_GUIBPodEditor.tabButton, 20);
					if (browsername.contains("internet"))
						commonLibrary.clickJS(buttonAddTab, "+Add Pod");
					else
						commonLibrary.clickButtonParentWithWait(buttonAddTab, "+Add Pod");
				} else {
					report.updateTestLog("Click on Add Tab", "Add Tab is not Clicked", Status.FAIL);
				}
				break;
			}
			}
		} else {
			report.updateTestLog("Click on Add pod under " + section + " Section", "Add Pod is not Clicked", Status.FAIL);
		}

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
	// # Function Description : Function to add title
	// # Function Name : addTitle     
	// # Author : Shobana
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public GUIBPageEditor addTitleInTitleBox(String strname) {
		WebElement txtTitle = commonLibrary.isExistNegative(UIMAP_GUIBPodEditor.txtTitle, 10);
		commonLibrary.setDataInTextBox(txtTitle, strname, "Title");
		return new GUIBPageEditor(scriptHelper);
	}

}
