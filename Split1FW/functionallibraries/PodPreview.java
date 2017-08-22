package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_GUIBPodEditor;
import UIMAP.UIMAP_PodPreview;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class PodPreview extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

	public PodPreview(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("Container"))
				commonLibrary.sleep(5000);

		} while (!url.contains("Container") && counter < 40);

		if (!driver.getCurrentUrl().contains("Container")) {
			throw new IllegalStateException("Pod Preview page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying pod preview page title
	// # Function Name : VerifyPreviewPageTitle   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public PodPreview verifyPreviewPageTitle(String Title) {
		String PageTitle = driver.getTitle();
		if (PageTitle.contains(Title))
			report.updateTestLog(Title + " pod displays in secondary browser", Title + " pod displays in secondary browser", Status.PASS);
		else
			report.updateTestLog(Title + " pod displays in secondary browser", Title + " pod is not displayed in secondary browser", Status.FAIL);
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for closing secondary window
	// # Function Name : ClosePodPreviewWindow   
	// # Author : karthik
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor closeSecondarywindow(String secondarytitle) {
		// driver.close();
		commonLibrary.switchToWindow(secondarytitle);
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for closing Pod Preview secondary
	// window
	// # Function Name : ClosePodPreviewWindow   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPodEditor closePodPreviewWindow() {
		driver.close();
		report.updateTestLog("Close Preview Window", "Preview Window is closed", Status.PASS);
		commonLibrary.switchToWindow("podeditor");
		return new GUIBPodEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying in Pod Preview window
	// # Function Name : Verify_Filters_PodPreview   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public PodPreview verifyFiltersPodPreview(String FilterName) {
		boolean blnFlag = false;
		WebElement toolBar = commonLibrary.isExist(UIMAP_PodPreview.menuToolbar, 20);
		if (toolBar != null) {
			List<WebElement> FiltButtons = commonLibrary.isExistList(toolBar, By.tagName("button"), 20);
			for (WebElement item : FiltButtons) {
				if (item.getText().equalsIgnoreCase(FilterName)) {
					blnFlag = true;
					report.updateTestLog("Verify " + FilterName + " filter is set", FilterName + " filter is set", Status.PASS);
					break;
				}

			}
			if (!blnFlag) {
				report.updateTestLog("Verify " + FilterName + " filter is set", FilterName + " filter is not set", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify the filters displayed in Pod Preview page", "There are no filters present", Status.FAIL);
		}
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Secondary window and get
	// values
	// # Function Name : Verify case pull preview window and get values   
	// # Author : karthi
	// # Date Created : Apr'15
	// #*****************************************************************************************************************************
	public PodPreview verifySecondaryWindowGetValues(String FilterName) {
		commonLibrary.switchToWindow("podeditor");

		boolean blnFlag = false;
		WebElement toolBar = commonLibrary.isExist(UIMAP_PodPreview.menuToolbar, 20);
		if (toolBar != null) {
			List<WebElement> FiltButtons = commonLibrary.isExistList(toolBar, By.tagName("button"), 20);
			for (WebElement item : FiltButtons) {
				if (item.getText().equalsIgnoreCase(FilterName)) {
					blnFlag = true;
					report.updateTestLog("Verify " + FilterName + " filter is set", FilterName + " filter is set", Status.PASS);
					break;
				}

			}
			if (!blnFlag) {
				report.updateTestLog("Verify " + FilterName + " filter is set", FilterName + " filter is not set", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify the filters displayed in Pod Preview page", "There are no filters present", Status.FAIL);
		}
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on move to preview page
	// # Function Name : click_MovetToPreviewPage   
	// # Author : uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Document clickMovetToPreviewPage() {
		try {
			WebElement PreviewPage = commonLibrary.isExist(UIMAP_GUIBPodEditor.btnGoToPreviewPage, 10);
			if (PreviewPage != null) {
				commonLibrary.clickButtonLogSmallWait(PreviewPage, "Go To Preview Page");
				commonLibrary.sleep(4000);
				commonLibrary.switchToWindow("podpreview");
				commonLibrary.sleep(2000);
			} else
				report.updateTestLog("Click on Go To Preview Page button", "Go To Preview Page button is not present", Status.FAIL);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for enter the values and getting vaules
	// in podpreview page
	// # Function Name : VerifyPreviewPageTitle   
	// # Author : karthi
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Document getMultipleCitation(String Title, String strvalue, String strwindow) {
		WebElement citationtext = commonLibrary.isExist(UIMAP_PodPreview.citation, 20);
		commonLibrary.setDataInTextBox(citationtext, strvalue, "Enter Multiple Citation");
		WebElement objGet = commonLibrary.isExist(By.cssSelector("button[class*='get-button'][data-action='getfulldoc']"), 20);
		if (objGet != null) {
			commonLibrary.clickButtonParentWithWait(objGet, "Get");

		}

		commonLibrary.switchToWindow(Title);
		String PageTitle1 = driver.getTitle();

		if (PageTitle1.contains("McNeil v. Econs. Lab., 800 F.2d 111")) {
			report.updateTestLog(Title + " pod displays in secondary browser", Title + " pod displays in secondary browser", Status.PASS);
		} else {
			report.updateTestLog(Title + " pod displays in secondary browser", Title + " pod is not displayed in secondary browser", Status.FAIL);
		}

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected checkboxes and
	// dropdown
	// # Function Name : verifySelectedCheckboxes     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public PodPreview verifySelectedCheckBoxes(String showOnInterface, String setDefault, String list, String dropdownOption) {
		WebElement tabHeaders = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabHeaders, 10);
		List<WebElement> tabs = commonLibrary.isExistList(tabHeaders, UIMAP_GUIBPodEditor.tabs, 10);
		if (tabs != null) {
			for (WebElement tab : tabs) {
				WebElement tabButton = commonLibrary.isExist(tab, UIMAP_GUIBPodEditor.tabButton, 10);
				commonLibrary.clickJS(tabButton, tabButton.getText());
				WebElement tabUl = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabUl, 10);
				WebElement tabShown = commonLibrary.isExist(tabUl, UIMAP_GUIBPodEditor.tabLi, 10);
				WebElement fieldset = commonLibrary.isExist(tabShown, UIMAP_GUIBPodEditor.tabFieldset, 10);
				List<WebElement> fieldsetList = commonLibrary.isExistList(fieldset, UIMAP_GUIBPodEditor.fieldsetList, 10);
				if (fieldsetList != null) {
					for (WebElement fieldsetLi : fieldsetList) {
						WebElement fieldsetSpan = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetSpan, 10);
						if (fieldsetSpan != null) {
							if (!fieldsetSpan.getText().equals("") && (list.toLowerCase().contains(fieldsetSpan.getText().toLowerCase()) || showOnInterface.toLowerCase().contains(fieldsetSpan.getText().toLowerCase()))) {
								report.updateTestLog("Verify configured items display in the preview page", "The selected item " + fieldsetSpan.getText() + " is displyed in preview page", Status.PASS);
								if (setDefault.toLowerCase().contains(fieldsetSpan.getText().toLowerCase())) {
									WebElement fieldsetInput = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetInput, 10);
									if (fieldsetInput != null) {
										if (fieldsetInput.isSelected() || fieldsetInput.isEnabled()) {
											report.updateTestLog("Verify configured items display in the preview page", "The selected " + fieldsetSpan.getText() + " is set default", Status.PASS);
										}
									}
								}
							}
						}
					}
				} else {
					report.updateTestLog("Verify configured items display in the preview page", "Configured items does not display in the preview page", Status.FAIL);
				}
				List<WebElement> fieldsetDiv = commonLibrary.isExistList(fieldset, UIMAP_GUIBPodEditor.fieldsetDiv, 10);
				if (fieldsetDiv != null) {
					for (WebElement fieldsetDivision : fieldsetDiv) {
						WebElement fieldsetLabel = commonLibrary.isExist(fieldsetDivision, UIMAP_GUIBPodEditor.fieldsetLabel, 10);
						if (fieldsetLabel != null) {
							if (list.toLowerCase().contains(fieldsetLabel.getText().toLowerCase())) {
								WebElement fieldsetSelect = commonLibrary.isExist(fieldsetDivision, UIMAP_GUIBPodEditor.fieldsetSelect, 10);
								if (fieldsetSelect != null) {
									List<WebElement> options = commonLibrary.isExistList(fieldsetSelect, UIMAP_GUIBPodEditor.fieldsetOption, 10);
									for (WebElement option : options) {
										if (dropdownOption.toLowerCase().contains(option.getText().trim().toLowerCase())) {
											if (!option.getText().equals("") && option.isSelected() == true) {
												report.updateTestLog("Verify configured items display in the preview page", "The selected " + option.getText().trim() + " is set default", Status.PASS);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			report.updateTestLog("Verify configured items display in the preview page", "Configured items does not display in the preview page", Status.FAIL);
		}
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for closing Pod Preview secondary
	// window
	// # Function Name : ClosePodPreviewWindowReturnToPage   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor closePodPreviewWindowReturnToPage() {
		driver.close();
		report.updateTestLog("Close Preview Window", "Preview Window is closed", Status.PASS);
		commonLibrary.switchToWindow("podeditor");
		return new GUIBPageEditor(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for closing Pod Preview secondary
	// window
	// # Function Name : ClosePodPreviewWindowReturnToPage   
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public PodPreview verifySelectedPracticeArea(String practiceAreas) {
		boolean flag = false;
		WebElement form = commonLibrary.isExist(UIMAP_GUIBPodEditor.form, 10);
		List<WebElement> practice = commonLibrary.isExistList(form, UIMAP_GUIBPodEditor.practiceArea, 10);
		String[] practiceareas = practiceAreas.split(";");
		if (practice != null) {
			for (int i = 0; i < practiceareas.length; i++) {
				for (WebElement item : practice) {
					if (item.getText().equalsIgnoreCase(practiceareas[i])) {
						report.updateTestLog("Verify configured pod " + practiceareas[i] + " displayed in secondary browser", "Configured pod " + practiceareas[i] + " is displayed in secondary browser", Status.PASS);
						flag = true;
						break;
					}

				}
				if (!flag)
					report.updateTestLog("Verify configured pod " + practiceareas[i] + " displayed in secondary browser", "Configured pod " + practiceareas[i] + " is not displayed in secondary browser", Status.FAIL);

			}
		}
		return new PodPreview(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying Content of Treatises Pod
	// displayed in Preview Page.
	// # Function Name : verifyTreatisesContent   
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public PodPreview verifyTreatisesContent(String categorization) {
		WebElement contentArea = commonLibrary.isExistNegative(UIMAP_PodPreview.contentArea, 10);
		List<WebElement> groups = null;
		switch (categorization) {
		case "Alphabetical": {
			groups = commonLibrary.isExistList(contentArea, UIMAP_PodPreview.alphaContent, 10);
			break;
		}
		case "Jurisdiction": {
			groups = commonLibrary.isExistList(contentArea, UIMAP_PodPreview.jurisContent, 10);
			break;
		}
		}
		if (groups != null && groups.size() > 0)
			report.updateTestLog("Verify Configured Treatises pod displays", "Configured Treatises pod displays", Status.PASS);
		else
			report.updateTestLog("Verify Configured Treatises pod displays", "Configured Treatises pod does not display", Status.FAIL);
		return new PodPreview(scriptHelper);
	}

}
