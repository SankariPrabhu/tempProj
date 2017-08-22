package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class GUIBContainer extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	String version = cap.getVersion();
	public Delivery deliv;

	public GUIBContainer(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("container"))
				commonLibrary.sleep(5000);

		} while (!url.contains("container") && counter < 40);

		if (!driver.getCurrentUrl().toLowerCase().contains("container")) {
			throw new IllegalStateException("GUIB container page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify under casepull pod in
	// container page
	// # Function Name : VerifyInsideCasePullContainer     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBContainer verifyInsideCasePullContainer(String strText) {

		boolean blnText = false;
		boolean blnLogo = false;
		boolean Tips = false;
		boolean Clear = false;
		boolean get = false;
		boolean terms = false;
		boolean logos = false;
		boolean copyright = false;
		WebElement divCont = commonLibrary.isExist(UIMAP_GUIBContainer.container);
		if (divCont != null) {
			List<WebElement> span = commonLibrary.isExistList(divCont, UIMAP_SearchResult.tagSpan, 20);
			for (WebElement item : span) {
				if (item.getText().equalsIgnoreCase(strText)) {
					blnText = true;
					break;
				}
			}

			WebElement divCont1 = commonLibrary.isExist(UIMAP_GUIBContainer.container);
			List<WebElement> logo = commonLibrary.isExistList(divCont1, UIMAP_GUIBContainer.logo, 20);
			for (WebElement item1 : logo) {
				if (item1.getText().equalsIgnoreCase("Lexis Advance® CasePull™")) {
					blnLogo = true;
					break;
				}
			}

			List<WebElement> link = commonLibrary.isExistList(divCont1, UIMAP_SearchResult.lnkHeader, 10);
			for (WebElement item1 : link) {
				if (item1.getText().equalsIgnoreCase("Tips")) {
					Tips = true;
				} else if (item1.getText().equalsIgnoreCase("Clear")) {
					Clear = true;
				}
				if (Tips && Clear)
					break;
			}
			WebElement getButton = commonLibrary.isExist(UIMAP_GUIBContainer.getButton);
			if (getButton != null) {
				get = true;
			}
			List<WebElement> footer = commonLibrary.isExistList(UIMAP_GUIBContainer.footerCB, 10);
			for (WebElement item2 : footer) {
				List<WebElement> links = commonLibrary.isExistList(item2, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement allLink : links) {
					if (allLink.getText().equalsIgnoreCase("Terms & Conditions")) {
						terms = true;
					} else if (allLink.getText().equalsIgnoreCase("Copyright © 2015 LexisNexis. All rights reserved.")) {
						copyright = true;
					} else {
						WebElement logoLexisNesis = commonLibrary.isExist(allLink, UIMAP_GUIBContainer.lexisNexisLogo, 10);
						if (logoLexisNesis != null) {
							logos = true;
						}
					}
					if (terms && logos && copyright)
						break;
				}
				if (terms && logos && copyright)
					break;
			}

		}
		if (blnText) {
			report.updateTestLog("Verify Casepull pod for multiple citation is displayed", "Casepull pod for multiple citation is displayed", Status.PASS);
			report.updateTestLog("Verify Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines", "Enter your citations (up to 10). Separate multiple citations using a semicolon, or by entering them onseparate lines is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Casepull pod for multiple citation is displayed", "Casepull pod for multiple citation is not displayed", Status.FAIL);
			report.updateTestLog("Verify Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines", "Enter your citations (up to 10). Separate multiple citations using a semicolon, or by entering them onseparate lines is not displayed", Status.FAIL);
		}
		if (blnLogo) {
			report.updateTestLog("Verify Lexis Advance® CasePull™ by Citation Logo is at top left ofcasepull pod", "Lexis Advance® CasePull™by Citation Logo is at top left ofcasepull pod", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® CasePull™ by Citation Logo is at top left ofcasepull pod", "Lexis Advance® CasePull™by Citation Logo is not present at top left ofcasepull pod", Status.FAIL);
		}
		if (get) {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button appears directly under the box on the left hand side", Status.PASS);
		} else {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button does not appears directly under the box on the left hand side", Status.FAIL);
		}
		if (terms && logos && copyright) {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottom of casepull pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottomofcasepull pod", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottom of casepull pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are not displayed at the bottomofcasepull pod", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter citation and click get button
	// # Function Name : EnterCitationAndClickGetButton     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBContainer enterCitationAndClickGetButton(String citation) {
		commonLibrary.setDataInTextBox(UIMAP_GUIBContainer.citationText, citation);
		WebElement getButton = commonLibrary.isExist(UIMAP_GUIBContainer.getButtonInput);
		if (getButton != null) {
			commonLibrary.clickButtonParentWithWait(getButton, "Get");
			commonLibrary.sleep(5000);
		} else {
			report.updateTestLog("Click Get button", "Get button is not displayed", Status.FAIL);
		}
		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new GUIBContainer(scriptHelper);
	}

	// #******************************************************************************************************************************
	// # Function Description : Function to enter citation and click get button
	// # Function Name : EnterCitationAndClickGetButton     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Search enterCitationAndClickGetButtonWithoutErr(String citation) {
		commonLibrary.setDataInTextBox(UIMAP_GUIBContainer.citationText, citation);
		WebElement getButton = commonLibrary.isExist(UIMAP_GUIBContainer.getButton);
		if (getButton != null) {
			commonLibrary.clickButtonParentWithWait(getButton, "Get");
			commonLibrary.switchToWindow("search");
		} else {
			report.updateTestLog("Click Get button", "Get button is not displaye", Status.FAIL);
		}

		return new Search(scriptHelper);
	}

	// # Function Description : Function to verify error message
	// # Function Name : VerifyErrorMessage     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyErrorMessage(String message) {
		boolean errmessage = false;
		List<WebElement> errMessage = commonLibrary.isExistList(UIMAP_GUIBContainer.errorMessage, 10);
		for (WebElement item : errMessage) {
			if (item.getText().equalsIgnoreCase(message)) {
				errmessage = true;
				break;
			}
		}
		if (errmessage) {
			report.updateTestLog("Verify Error Message dispalyed as " + message + "", "Error Message is dispalyed as " + message + "", Status.PASS);
		} else {
			report.updateTestLog("Verify Error Message dispalyed as " + message + "", "Error Message is not dispalyed as " + message + "", Status.FAIL);
		}

		return new GUIBContainer(scriptHelper);
	}

	// # Function Description : Function to switchToSearch
	// # Function Name : switchToSearch     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBContainer switchToSearch() {
		commonLibrary.switchToWindow("search");
		driver.close();
		commonLibrary.switchToWindow("container");
		report.updateTestLog("Close the secondary window", "Secondary window is closed", Status.PASS);
		return new GUIBContainer(scriptHelper);
	}

	// # Function Description : Function to select expand icon in juris form
	// # Function Name : expandGivenValueUnderJurisdiction     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBContainer expandGivenValueUnderJurisdiction(String city) {
		boolean isexpand = false;
		WebElement form = commonLibrary.isExist(UIMAP_GUIBContainer.jurisForm);
		if (form != null) {
			report.updateTestLog("Verify jurisdiction pod is displayed", "Jurisdiction pod is displayed", Status.PASS);
			List<WebElement> list = commonLibrary.isExistList(UIMAP_GUIBContainer.cityList, 10);
			for (WebElement item : list) {
				List<WebElement> span = commonLibrary.isExistList(item, UIMAP_SearchResult.tagSpan, 10);
				for (WebElement item1 : span) {
					if (item1.getText().equalsIgnoreCase(city)) {
						WebElement expand = commonLibrary.isExist(UIMAP_GUIBContainer.expandIcon);
						if (expand != null) {
							commonLibrary.clickButtonParentWithWait(expand, "Expand");
							isexpand = true;
							break;
						} else if (expand == null) {
							WebElement collapse = commonLibrary.isExist(UIMAP_GUIBContainer.collapseIcon);
							if (collapse != null) {
								isexpand = true;
								break;
							}
						}

					}
				}
				if (isexpand)
					break;
			}
		} else {
			report.updateTestLog("Verify jurisdiction pod is displayed", "Jurisdiction pod is not displayed", Status.FAIL);
		}
		if (!isexpand) {
			report.updateTestLog("Click expand button of " + city + "", "Expand button of " + city + " is not clicked", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// # Function Description : Function to select expand icon for sub value in
	// juris form
	// # Function Name : expandGivenSubValueUnderJurisdiction     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBContainer expandGivenSubValueUnderJurisdiction(String city) {
		boolean isexpand = false;
		WebElement form = commonLibrary.isExist(UIMAP_GUIBContainer.jurisForm);
		if (form != null) {
			List<WebElement> list = commonLibrary.isExistList(UIMAP_GUIBContainer.citySubList, 10);
			for (WebElement item : list) {
				List<WebElement> span = commonLibrary.isExistList(item, UIMAP_SearchResult.tagSpan, 10);
				for (WebElement item1 : span) {
					if (item1.getText().equalsIgnoreCase(city)) {
						WebElement expand = commonLibrary.isExist(UIMAP_GUIBContainer.expandIcon);
						if (expand != null) {
							commonLibrary.clickButtonParentWithWait(expand, "Expand");
							isexpand = true;
							break;
						} else if (expand == null) {
							WebElement collapse = commonLibrary.isExist(UIMAP_GUIBContainer.collapseIcon);
							if (collapse != null) {
								isexpand = true;
								break;
							}
						}

					}
				}
				if (isexpand)
					break;
			}
		}
		if (!isexpand) {
			report.updateTestLog("Click expand button of " + city + "", "Expand button of " + city + " is not clicked", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// # Function Description : Function to click toc link
	// # Function Name : clickTOC     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBToc clickTOC(String city) {
		boolean click = false;
		WebElement form = commonLibrary.isExist(UIMAP_GUIBContainer.jurisForm);
		if (form != null) {
			List<WebElement> list = commonLibrary.isExistList(UIMAP_GUIBContainer.tocLink, 10);
			for (WebElement item : list) {
				if (item.getText().equalsIgnoreCase(city)) {
					commonLibrary.clickLinkWithWebElement(item, city);
					click = true;
					break;
				}
			}
		}
		if (!click) {
			report.updateTestLog("Click button  " + city + "", "Button  " + city + " is not clicked", Status.FAIL);
		}
		return new GUIBToc(scriptHelper);
	}

	// # Function Description : Function to select add source to search
	// # Function Name : clickAddSourceToSearchAndVerify     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBContainer clickAddSourceToSearchAndVerify(String source) {
		boolean click = false;
		boolean added = false;
		WebElement form = commonLibrary.isExist(UIMAP_GUIBContainer.jurisForm);
		if (form != null) {
			List<WebElement> list = commonLibrary.isExistList(form, UIMAP_GUIBContainer.citySubList, 10);
			for (WebElement item : list) {
				List<WebElement> link = commonLibrary.isExistList(item, UIMAP_GUIBContainer.addSource, 10);
				for (WebElement item1 : link) {
					if (item1.getText().equalsIgnoreCase(source)) {
						commonLibrary.clickLinkWithWebElement(item1, source);
						WebElement addToSrch = commonLibrary.isExist(UIMAP_GUIBContainer.addToSearch);
						if (addToSrch != null) {
							commonLibrary.clickLinkWithWebElement(addToSrch, "Add to Search");
							click = true;
							WebElement searchAdded = commonLibrary.isExist(UIMAP_GUIBContainer.searchAdded, 10);
							if (searchAdded != null) {
								List<WebElement> texts = commonLibrary.isExistList(searchAdded, UIMAP_SearchResult.tagSpan, 10);
								for (WebElement item3 : texts) {
									if (item3.getText().toLowerCase().equalsIgnoreCase(source)) {
										added = true;
										break;
									}
								}
								if (click && added)
									break;
							}

						}

					}
				}
				if (click && added)
					break;
			}
		}
		if (!click) {
			report.updateTestLog("Click button  " + source + "", "Button  " + source + " is not clicked", Status.FAIL);
		}
		if (added) {
			report.updateTestLog("Verify Selected source is added to the Search pop up", "Selected source is added to the Search pop up", Status.PASS);
		} else {
			report.updateTestLog("Verify Selected source is added to the Search pop up", "Selected source is not added to the Search pop up", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// # Function Description : Function to perform search in search selected
	// # Function Name : performSearchInSearchSelected     
	// # Author : seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Search performSearchInSearchSelected(String search) {
		WebElement searchBox = commonLibrary.isExist(UIMAP_GUIBContainer.searchSelectedtxt, 10);
		if (searchBox != null) {
			commonLibrary.setDataInTextBox(searchBox, search, "Search Selected");
			commonLibrary.clickButton(UIMAP_GUIBContainer.searchButton, "Search");
			commonLibrary.switchToWindow("search");
		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}

		WebElement lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut != null)
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify under casepull pod in
	// container page
	// # Function Name : VerifyInsideCasePullContainer     
	// # Author : Aravind
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public GUIBPageEditor verifyTextInsideCasePullContainer(String strText) {
		boolean blnText = false;
		boolean blnLogo = false;
		boolean Tips = false;
		boolean Clear = false;
		boolean get = false;
		boolean terms = false;
		boolean logos = false;
		boolean copyright = false;
		WebElement divCont = commonLibrary.isExist(UIMAP_GUIBContainer.container);
		if (divCont != null) {
			List<WebElement> span = commonLibrary.isExistList(divCont, UIMAP_SearchResult.tagSpan, 20);
			for (WebElement item : span) {
				if (item.getText().equalsIgnoreCase(strText)) {
					blnText = true;
					break;
				}
			}

			WebElement divCont1 = commonLibrary.isExist(UIMAP_GUIBContainer.container);
			List<WebElement> logo = commonLibrary.isExistList(divCont1, UIMAP_GUIBContainer.logo, 20);
			for (WebElement item1 : logo) {
				if (item1.getText().equalsIgnoreCase("Lexis Advance® CasePull™")) {
					blnLogo = true;
					break;
				}
			}

			List<WebElement> link = commonLibrary.isExistList(divCont1, UIMAP_SearchResult.lnkHeader, 10);
			for (WebElement item1 : link) {
				if (item1.getText().equalsIgnoreCase("Tips")) {
					Tips = true;
				} else if (item1.getText().equalsIgnoreCase("Clear")) {
					Clear = true;
				}
				if (Tips && Clear)
					break;
			}
			WebElement getButton = commonLibrary.isExist(UIMAP_GUIBContainer.getButton);
			if (getButton != null) {
				get = true;
			}
			List<WebElement> footer = commonLibrary.isExistList(UIMAP_GUIBContainer.footerCB, 10);
			for (WebElement item2 : footer) {
				List<WebElement> links = commonLibrary.isExistList(item2, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement allLink : links) {
					if (allLink.getText().equalsIgnoreCase("Terms & Conditions")) {
						terms = true;
					} else if (allLink.getText().equalsIgnoreCase("Copyright © 2015 LexisNexis. All rights reserved.")) {
						copyright = true;
					} else {
						WebElement logoLexisNesis = commonLibrary.isExist(allLink, UIMAP_GUIBContainer.lexisNexisLogo, 10);
						if (logoLexisNesis != null) {
							logos = true;
						}
					}
					if (terms && logos && copyright)
						break;
				}
				if (terms && logos && copyright)
					break;
			}

		}
		if (blnText) {
			report.updateTestLog("Verify Case pull pod for multiple citation is displayed", "Case pull pod for multiple citation is displayed", Status.PASS);
			report.updateTestLog("Verify Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines", "Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Case pull pod for multiple citation is displayed", "Case pull pod for multiple citation is not displayed", Status.FAIL);
			report.updateTestLog("Verify Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines", "Enter your citations (up to 100). Separate multiple citations using a semicolon, or by entering them on separate lines is not displayed", Status.FAIL);
		}
		if (blnLogo) {
			report.updateTestLog("Verify Lexis Advance® CasePull™ by Citation Logo is at top left of case pull pod", "Lexis Advance® CasePull™by Citation Logo is at top left of case pull pod", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Advance® CasePull™ by Citation Logo is at top left of case pull pod", "Lexis Advance® CasePull™by Citation Logo is not present at top left of case pull pod", Status.FAIL);
		}
		if (get) {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button appears directly under the box on the left hand side", Status.PASS);
		} else {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button does not appears directly under the box on the left hand side", Status.FAIL);
		}
		if (terms && logos && copyright) {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexis. All rights reserved footers links are displayed at the bottom of casepull pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexis. All rights reserved footers links are displayed at the bottom of case pull pod", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexis. All rights reserved footers links are displayed at the bottom of casepull pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexis. All rights reserved footers links are not displayed at the bottom of case pull pod", Status.FAIL);
		}
		return new GUIBPageEditor(scriptHelper);
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
	// # Function Description : Function to verify Page title
	// # Function Name : verifyPageTitle     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyPageTitle(String title) {
		if (driver.getTitle().trim().toLowerCase().contains(title.toLowerCase())) {
			report.updateTestLog("Veriyf page title", "Page title is displayed as " + title, Status.PASS);
		} else {
			report.updateTestLog("Veriyf page title", "Page title is not displayed as " + title, Status.FAIL);
		}

		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify under casepull pod in
	// container page
	// # Function Name : verifyItemsInPreviewPage     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public GUIBContainer verifyItemsInPreviewPage(String message, String logoText) {

		boolean blnText = false;
		boolean blnLogo = false;
		boolean Tips = false;
		boolean Clear = false;
		boolean get = false;
		boolean terms = false;
		boolean logos = false;
		boolean copyright = false;
		WebElement divCont = commonLibrary.isExist(UIMAP_GUIBContainer.container1);
		if (divCont != null) {
			List<WebElement> list = commonLibrary.isExistList(divCont, UIMAP_SearchResult.listtag, 20);
			for (WebElement item : list) {
				if (item.getText().equalsIgnoreCase(message)) {
					blnText = true;
					break;
				}
			}

			WebElement divCont1 = commonLibrary.isExist(UIMAP_GUIBContainer.container1);
			List<WebElement> logo = commonLibrary.isExistList(divCont1, UIMAP_GUIBContainer.h2Tag, 20);
			for (WebElement item1 : logo) {
				if (item1.getText().trim().toLowerCase().contains(logoText.toLowerCase())) {
					blnLogo = true;
					break;
				}
			}

			List<WebElement> link = commonLibrary.isExistList(divCont1, UIMAP_SearchResult.lnkHeader, 10);
			for (WebElement item1 : link) {
				if (item1.getText().equalsIgnoreCase("Tips")) {
					Tips = true;
				} else if (item1.getText().equalsIgnoreCase("Clear")) {
					Clear = true;
				}
				if (Tips && Clear)
					break;
			}
			WebElement getButton = commonLibrary.isExist(UIMAP_GUIBContainer.getButtonInput);
			if (getButton != null) {
				get = true;
			}
			List<WebElement> footer = commonLibrary.isExistList(UIMAP_GUIBContainer.footerCB, 10);
			for (WebElement item2 : footer) {
				List<WebElement> links = commonLibrary.isExistList(item2, UIMAP_SearchResult.lnkHeader, 10);
				for (WebElement allLink : links) {
					if (allLink.getText().equalsIgnoreCase("Terms & Conditions")) {
						terms = true;
					} else if (allLink.getText().equalsIgnoreCase("Copyright © 2015 LexisNexis. All rights reserved.")) {
						copyright = true;
					} else {
						WebElement logoLexisNesis = commonLibrary.isExist(allLink, UIMAP_GUIBContainer.lexisNexisLogo, 10);
						if (logoLexisNesis != null) {
							logos = true;
						}
					}
					if (terms && logos && copyright)
						break;
				}
				if (terms && logos && copyright)
					break;
			}

		}
		if (blnText) {
			report.updateTestLog("Verify " + message + " is displayed above the textbox", message + " is displayed above the textbox", Status.PASS);
		} else {
			report.updateTestLog("Verify " + message + " is displayed above the textbox", message + " is not displayed above the textbox", Status.FAIL);
		}
		if (blnLogo) {
			report.updateTestLog("Verify " + logoText + " is displayed", logoText + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify " + logoText + " is displayed", logoText + " is not displayed", Status.FAIL);
		}
		if (get) {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button appears directly under the box on the left hand side", Status.PASS);
		} else {
			report.updateTestLog("Verify Get button appears directly under the box on the left hand side", "Get button does not appears directly under the box on the left hand side", Status.FAIL);
		}
		if (terms && logos && copyright) {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottom of pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottom of pod", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are displayed at the bottom of pod", "Lexis Nexis, Terms & Conditions, Copyright 2014 LexisNexisAll rights reserved  footers links are not displayed at the bottom of pod", Status.FAIL);
		}
		if (Tips) {
			report.updateTestLog("Verify 'Tips' Link is displayed at top right of casepull pod", "'Tips' Link is displayed at top right of casepull pod", Status.PASS);
		} else {
			report.updateTestLog("Verify 'Tips' Link is displayed at top right of casepull pod", "'Tips' Link is not displayed at top right of casepull pod", Status.FAIL);
		}
		if (Clear) {
			report.updateTestLog("Verify 'Clear' link appears directly under the box on the right hand side", "'Clear' link appeared directly under the box on the right hand side", Status.PASS);
		} else {
			report.updateTestLog("Verify 'Clear' link appears directly under the box on the right hand side", "'Clear' link is not appeared directly under the box on the right hand side", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click tabs and select options in
	// Preview page
	// # Function Name : selectTabsAndSelectOptions     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public GUIBContainer selectTabsAndSelectOptions(String tabName, String list) {
		WebElement tabHeaders = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabHeaders, 10);
		List<WebElement> tabs = commonLibrary.isExistList(tabHeaders, UIMAP_GUIBPodEditor.tabs, 10);
		if (tabs != null) {
			for (WebElement tab : tabs) {
				WebElement tabButton = commonLibrary.isExist(tab, UIMAP_GUIBPodEditor.tabButton, 10);
				if (tabButton != null && tabName.toLowerCase().contains(tabButton.getText().toLowerCase())) {
					commonLibrary.clickJS(tabButton, tabButton.getText());
					WebElement tabUl = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabUl, 10);
					WebElement tabShown = commonLibrary.isExist(tabUl, UIMAP_GUIBPodEditor.tabLi, 10);
					WebElement fieldset = commonLibrary.isExist(tabShown, UIMAP_GUIBPodEditor.tabFieldset, 10);
					List<WebElement> fieldsetList = commonLibrary.isExistList(fieldset, UIMAP_GUIBPodEditor.fieldsetList, 10);
					if (fieldsetList != null) {
						for (WebElement fieldsetLi : fieldsetList) {
							WebElement fieldsetSpan = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetSpan, 10);
							if (fieldsetSpan != null) {
								if (!(fieldsetSpan.getText().equals("")) && (list.toLowerCase().contains(fieldsetSpan.getText().toLowerCase()))) {
									WebElement fieldsetInput = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetInput, 10);
									if (fieldsetInput != null) {
										commonLibrary.setCheckBox(fieldsetInput, true);
									}
								}
							}
						}
					} else {
						report.updateTestLog("click tabs and select options in Preview page", "Check Boxes are not available", Status.FAIL);
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
											if (!option.getText().equals("") && list.toLowerCase().contains(option.getText().trim().toLowerCase())) {
												commonLibrary.selectFromListOption(option, option.getText());
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
			report.updateTestLog("click tabs and select options in Preview page", "Tabs are not displayed in the preview page", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify tabs are present in preview
	// page
	// # Function Name : verifyTabsPresent     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyTabsPresent(String list) {
		WebElement tabHeaders = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabHeaders, 10);
		List<WebElement> tabs = commonLibrary.isExistList(tabHeaders, UIMAP_GUIBPodEditor.tabs, 10);
		if (tabs != null) {
			for (WebElement tab : tabs) {
				WebElement tabButton = commonLibrary.isExist(tab, UIMAP_GUIBPodEditor.tabButton, 10);
				if (tabButton != null && list.toLowerCase().contains(tabButton.getText().toLowerCase())) {
					report.updateTestLog("verify tabs are present in preview page", tabButton.getText() + " is present in preview page", Status.PASS);
				}
			}
		} else {
			report.updateTestLog("verify tabs are present in preview page", "Tabs are not displayed in the preview page", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify selected checkboxes and
	// dropdown
	// # Function Name : verifySelectedCheckboxes     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifySelectedCheckBoxesInTabs(String tabName, String list, String dropdownOption) {
		WebElement tabHeaders = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabHeaders, 10);
		List<WebElement> tabs = commonLibrary.isExistList(tabHeaders, UIMAP_GUIBPodEditor.tabs, 10);
		if (tabs != null) {
			for (WebElement tab : tabs) {
				WebElement tabButton = commonLibrary.isExist(tab, UIMAP_GUIBPodEditor.tabButton, 10);
				if (tabButton != null && tabName.toLowerCase().contains(tabButton.getText().toLowerCase())) {
					commonLibrary.clickJS(tabButton, tabButton.getText());
					WebElement tabUl = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabUl, 10);
					WebElement tabShown = commonLibrary.isExist(tabUl, UIMAP_GUIBPodEditor.tabLi, 10);
					WebElement fieldset = commonLibrary.isExist(tabShown, UIMAP_GUIBPodEditor.tabFieldset, 10);
					List<WebElement> fieldsetList = commonLibrary.isExistList(fieldset, UIMAP_GUIBPodEditor.fieldsetList, 10);
					if (fieldsetList != null) {
						for (WebElement fieldsetLi : fieldsetList) {
							WebElement fieldsetSpan = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetSpan, 10);
							if (fieldsetSpan != null) {
								if (!fieldsetSpan.getText().equals("") && list.toLowerCase().contains(fieldsetSpan.getText().toLowerCase())) {
									report.updateTestLog("Verify " + fieldsetSpan.getText() + " is displyed", fieldsetSpan.getText() + " is displyed", Status.PASS);
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
													report.updateTestLog("Verify " + option.getText() + " is displyed", option.getText() + " is displyed", Status.PASS);
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
			}
		} else {
			report.updateTestLog("Verify configured items display in the preview page", "Configured items does not display in the preview page", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
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
	// # Function Description : Function to verify under casepull pod in
	// container page
	// # Function Name : verifyItemsInPreviewPage     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public GUIBContainer verifyItemsInPreviewPage1(String message, String logoText) {

		boolean blnText = false;
		boolean blnLogo = false;
		WebElement divCont = commonLibrary.isExist(UIMAP_GUIBContainer.container);
		if (divCont != null) {
			List<WebElement> list = commonLibrary.isExistList(divCont, UIMAP_SearchResult.listtag, 20);
			for (WebElement item : list) {
				if (item.getText().equalsIgnoreCase(message)) {
					blnText = true;
					break;
				}
			}

			WebElement divCont1 = commonLibrary.isExist(UIMAP_GUIBContainer.container);
			List<WebElement> logo = commonLibrary.isExistList(divCont1, UIMAP_GUIBContainer.logo, 20);
			for (WebElement item1 : logo) {
				if (item1.getText().equalsIgnoreCase(logoText)) {
					blnLogo = true;
					break;
				}
			}

		}
		if (blnText) {
			report.updateTestLog("Verify " + message + " is displayed above the textbox", message + " is displayed above the textbox", Status.PASS);
		} else {
			report.updateTestLog("Verify " + message + " is displayed above the textbox", message + " is not displayed above the textbox", Status.FAIL);
		}
		if (blnLogo) {
			report.updateTestLog("Verify " + logoText + " is displayed", logoText + " is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify " + logoText + " is displayed", logoText + " is not displayed", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click tabs and select options in
	// Preview page
	// # Function Name : selectTabsAndSelectOptions     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public GUIBContainer unselectTabsAndSelectOptions(String tabName, String list) {
		WebElement tabHeaders = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabHeaders, 10);
		List<WebElement> tabs = commonLibrary.isExistList(tabHeaders, UIMAP_GUIBPodEditor.tabs, 10);
		if (tabs != null) {
			for (WebElement tab : tabs) {
				WebElement tabButton = commonLibrary.isExist(tab, UIMAP_GUIBPodEditor.tabButton, 10);
				if (tabButton != null && tabName.toLowerCase().contains(tabButton.getText().toLowerCase())) {
					commonLibrary.clickJS(tabButton, tabButton.getText());
					WebElement tabUl = commonLibrary.isExist(UIMAP_GUIBPodEditor.tabUl, 10);
					WebElement tabShown = commonLibrary.isExist(tabUl, UIMAP_GUIBPodEditor.tabLi, 10);
					WebElement fieldset = commonLibrary.isExist(tabShown, UIMAP_GUIBPodEditor.tabFieldset, 10);
					List<WebElement> fieldsetList = commonLibrary.isExistList(fieldset, UIMAP_GUIBPodEditor.fieldsetList, 10);
					if (fieldsetList != null) {
						for (WebElement fieldsetLi : fieldsetList) {
							WebElement fieldsetSpan = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetSpan, 10);
							if (fieldsetSpan != null) {
								if (!fieldsetSpan.getText().equals("") && list.toLowerCase().contains(fieldsetSpan.getText().toLowerCase())) {
									WebElement fieldsetInput = commonLibrary.isExist(fieldsetLi, UIMAP_GUIBPodEditor.fieldsetInput, 10);
									if (fieldsetInput != null) {
										commonLibrary.setCheckBox(fieldsetInput, false);
									}
								}
							}
						}
					} else {
						report.updateTestLog("click tabs and select options in Preview page", "Check Boxes are not available", Status.FAIL);
					}
				}
			}
		} else {
			report.updateTestLog("click tabs and select options in Preview page", "Tabs are not displayed in the preview page", Status.FAIL);
		}
		return new GUIBContainer(scriptHelper);
	}

	public void sslCertificate() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public GUIBContainer pdfVerification(String FilePath, String Text, String Exist) {
		try {
			PdfReader reader = new PdfReader(FilePath);
			int Pages = reader.getNumberOfPages();
			boolean blnFlag = false;

			for (int i = 1; i <= Pages; i++) {
				String TestText = PdfTextExtractor.getTextFromPage(reader, i);
				if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
					blnFlag = true;
					break;
				}

			}
			switch (Exist) {
			case "Exist": {
				if (blnFlag) {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is present in the PDF document", Status.PASS);

				} else {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is not present in the PDF document", Status.FAIL);
				}
				break;
			}
			case "NotExist": {
				if (!blnFlag) {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is not present in the PDF document", Status.PASS);
				} else {
					report.updateTestLog("'" + Text + "' is present in th ePDF document", "'" + Text + "' is present in the PDF document", Status.FAIL);
				}
				break;
			}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new GUIBContainer(scriptHelper);
	}

	public void savePDFBrowser(String AutoITPath, String FilePath, String docName) throws IOException {
		try {
			sslCertificate();
			commonLibrary.sleep(4000);
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			commonLibrary.sleep(3000);
			String path2 = FilePath + docName + ".Pdf";
			String windowTitle = driver.getCurrentUrl() + " - Internet Explorer provided by Reed Elsevier";
			if (browsername.contains("internet") && version.contains("8")) {
				String[] cmd = { AutoITPath, windowTitle, path2, "Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet") && version.contains("9")) {
				String[] cmd = { AutoITPath, windowTitle, path2, "Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if ((browsername.contains("internet") && version.contains("11")) || (browsername.contains("internet") && version.contains("10"))) {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(10000);
				for (int i = 1; i <= 3; i++) {
					robot.keyPress(KeyEvent.VK_TAB);
					commonLibrary.sleep(1000);
				}
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(1000);
				// String[] cmd = {AutoITPath, path2, "Save As"};
				// Runtime.getRuntime().exec(AutoITPath);
			}
			commonLibrary.sleep(7000);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public GUIBContainer pdfVerificationSecondaryByDownload(String pdfContent, String strFilePath, String AutoITPath, String AutoITPath1, String strFileName, String textExistence) {
		try {
			commonLibrary.switchToWindow("delivery");
			String filename = null;
			commonLibrary.sleep(600000);
			Thread.sleep(20000);
			commonLibrary.windowFocus();
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));

			commonLibrary.fileExistsDelete(strFilePath, strFileName);
			String path = strFilePath + "\\";
			filename = strFilePath + "\\" + strFileName + ".PDF";

			if (browsername.equalsIgnoreCase("internet explorer")) {
				driver.manage().window().maximize();
				savePDFBrowser(AutoITPath, path, strFileName);
			} else if (browsername.equalsIgnoreCase("firefox")) {
				driver.manage().window().maximize();
				Actions action = new Actions(driver);
				action.sendKeys(Keys.chord(Keys.CONTROL, "s")).build().perform();
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);
				// commonLibrary.sleep(1000000);
				// Thread.sleep(10000);
				// PDFVerification(filename,pdfContent,textExistence);
			} else if (browsername.toLowerCase().contains("chrome")) {
				driver.manage().window().maximize();
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);
			}
			commonLibrary.sleep(1000000);
			Thread.sleep(20000);
			pdfVerification(filename, pdfContent, textExistence);
			driver.close();
			commonLibrary.switchToWindow("container");
			return new GUIBContainer(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify status of request message
	// # Function Name : verifyStatusOfRequest     
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public GUIBContainer verifyStatusOfRequest(String pdfContent, String strFilePath, String AutoITPath, String AutoITPath1, String strFileName, String textExistence) {
		boolean flag = false;
		int i = 0;
		do {
			commonLibrary.sleep(15000);
			WebElement statusOfRequest = commonLibrary.isExist(UIMAP_GUIBContainer.statusOfRequest, 10);
			WebElement statusOfRequestLi = commonLibrary.isExist(statusOfRequest, UIMAP_GUIBContainer.statusOfRequestLi, 10);
			if (statusOfRequestLi.getText().trim().equalsIgnoreCase("Your documents have been opened in a new window.")) {
				report.updateTestLog("Verify 'Your documents have been opened in a new window.' message is displayed", "'Your documents have been opened in a new window.' message is displayed in status of request section", Status.PASS);
				flag = true;
				break;
			}
			i++;
		} while (i < 100);
		if (!flag) {
			report.updateTestLog("Verify 'Your documents have been opened in a new window.' message is displayed", "'Your documents have been opened in a new window.' message is not displayed in status of request section", Status.FAIL);
		} else {

			pdfVerificationSecondaryByDownload(pdfContent, strFilePath, AutoITPath, AutoITPath1, strFileName, textExistence);
			// driver.close();

		}
		return new GUIBContainer(scriptHelper);
	}

}