package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LPAHome;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.*;

public class LPABrowse extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	int Mwait = Integer.parseInt(Mediumwait);

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	// private Home home = new Home(scriptHelper);
	public LPABrowse(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("lpabrowse"))
				commonLibrary.sleep(5000);

		} while (!url.contains("lpabrowse") && counter < 40);

		if (!driver.getCurrentUrl().contains("lpabrowse")) {
			throw new IllegalStateException("LPA Topics page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select tab in alerts popup
	// # Function Name : SelectTabInAlerts     
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPABrowse selectTabInAlerts(String tab) {
		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			commonLibrary.selectFromListButton(topicalert, tab);
		} else {
			report.updateTestLog("Select the given tab " + tab + "", "" + tab + " is not selected", Status.FAIL);
		}
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Create Alert Icon from LPA
	// Topics Page
	// # Function Name : ClickAlertIcon     
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPABrowse clickAlertIcon() {
		WebElement btnAlert = commonLibrary.isExist(UIMAP_SearchResult.btnIdAddAlert);
		if (btnAlert != null) {
			commonLibrary.clickButton(btnAlert);
		} else {
			report.updateTestLog("Click on Alert icon", "Alert icon is not displayed", Status.FAIL);
		}
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select options in Deliver tab of
	// Alerts Popup
	// # Function Name : DeliverTabAlerts     
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPABrowse deliverTabAlerts(String strTab, String strFromDate, String strToDate, String strDeliverType, String strEmail, String strDeliverFormat, String strFrequency, String timevalue, String timevalues) {

		WebElement topicalert = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
		if (topicalert != null) {
			Actions hover = new Actions(driver);
			hover.moveToElement(topicalert).build().perform();

			commonLibrary.selectFromListButton(topicalert, strTab);
			if (strFromDate.toLowerCase() != "") {
				WebElement fromDate = commonLibrary.isExist(UIMAP_Home.txtFromDate);
				fromDate.clear();
				commonLibrary.setDataInTextBox(fromDate, strFromDate, "fromdate");

				if (fromDate.getAttribute("value").equalsIgnoreCase(strFromDate)) {
					report.updateTestLog("Select  currentdate as start date", "Start date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select a currentdate+5 for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strToDate.toLowerCase() != "") {
				WebElement toDate = commonLibrary.isExist(UIMAP_Home.txtToDate);
				toDate.clear();
				commonLibrary.setDataInTextBox(toDate, strToDate, "todate");

				if (toDate.getAttribute("value").equalsIgnoreCase(strToDate)) {
					report.updateTestLog("Select  " + strToDate + " for end date", "End date is selected", Status.PASS);
				} else {
					report.updateTestLog("Select " + strToDate + " for end date", "End date is not selected", Status.FAIL);
				}
			}
			if (strDeliverType.toLowerCase() != "") {
				WebElement objDivName = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName != null) {
					// commonLibrary.ScrollToView(objDivName);
					if (strDeliverType.equalsIgnoreCase("online only")) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						if (commonLibrary.setRadioButton(onlineOnly, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					} else if ((strDeliverType.toLowerCase().equalsIgnoreCase("email + online"))) {
						WebElement onlineOnly = commonLibrary.isExist(UIMAP_SearchResult.online);
						commonLibrary.setRadioButton(onlineOnly, strDeliverType);
						WebElement emailonline = commonLibrary.isExist(UIMAP_SearchResult.emailonline);
						if (commonLibrary.setRadioButton(emailonline, strDeliverType)) {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is selected as " + strDeliverType + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
						}
					}
				} else {
					report.updateTestLog("Delivery type is selected as " + strDeliverType + "", "Delivery type is not selected as " + strDeliverType + "", Status.FAIL);
				}
			}
			if (strEmail.toLowerCase() != "") {
				WebElement Email = commonLibrary.isExist(UIMAP_Home.txtEmail);
				commonLibrary.setDataInTextBox(Email, strEmail, "Email");
			}
			if (strDeliverFormat.toLowerCase() != "") {
				WebElement objDivName1 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName1 != null) {
					if (strDeliverFormat.equalsIgnoreCase("text")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement text = commonLibrary.isExist(UIMAP_SearchResult.textRadio);
						if (commonLibrary.setRadioButton(text, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else if (strDeliverFormat.equalsIgnoreCase("html")) {
						// commonLibrary.ScrollToView(objDivName1);
						WebElement html = commonLibrary.isExist(UIMAP_SearchResult.htmlRadio);
						if (commonLibrary.setRadioButton(html, strDeliverFormat)) {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is selected as " + strDeliverFormat + "", Status.PASS);
						} else {
							report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
						}
					} else {
						report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
					}
				} else {
					report.updateTestLog("Delivery format is selected as " + strDeliverFormat + "", "Delivery format is not selected as " + strDeliverFormat + "", Status.FAIL);
				}
			}
			if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("business daily")) {
				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement busidaily = commonLibrary.isExist(UIMAP_SearchResult.businessDailyradio);
					// commonLibrary.ScrollToView(busidaily);
					if (commonLibrary.setRadioButton(busidaily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.businessDaily);
						if (time != null) {
							// commonLibrary.ScrollToView(time);
							commonLibrary.selectByVisibleTextByValue(time, timevalue, timevalues);
						} else {
							report.updateTestLog("Select time as " + timevalue + "", "Time is not selected as " + timevalue + "", Status.FAIL);
						}
					}
				}

			} else if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("daily")) {
				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement daily = commonLibrary.isExist(UIMAP_SearchResult.dailyradio);
					// commonLibrary.ScrollToView(daily);
					if (commonLibrary.setRadioButton(daily, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
					if (timevalue != "") {
						WebElement time = commonLibrary.isExist(UIMAP_SearchResult.dailytime);
						if (time != null) {
							// commonLibrary.ScrollToView(time);
							commonLibrary.selectByVisibleTextByValue(time, timevalue, timevalues);
						} else {
							report.updateTestLog("Select time as " + timevalue + "", "Time is not selected as " + timevalue + "", Status.FAIL);
						}
					}
				}

			} else if (strFrequency.toLowerCase() != "" && strFrequency.equalsIgnoreCase("As updates are available")) {

				WebElement objDivName2 = commonLibrary.isExist(UIMAP_Home.frmTopicAlert);
				// commonLibrary.ScrollToView(objDivName2);
				if (objDivName2 != null) {
					// commonLibrary.ScrollToView(objDivName2);
					WebElement asUpdatesAreAvail = commonLibrary.isExist(UIMAP_SearchResult.asUpdatesare);
					// commonLibrary.ScrollToView(asUpdatesAreAvail);
					if (commonLibrary.setRadioButton(asUpdatesAreAvail, strFrequency)) {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is selected as " + strFrequency + "", Status.PASS);
					} else {
						report.updateTestLog("Frequency is selected as " + strFrequency + "", "Frequency is not selected as " + strFrequency + "", Status.FAIL);
					}
				}
			}

		}

		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Update details in Share tab of
	// Alerts popup
	// # Function Name : CreateAlert_ShareDetails     
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPABrowse createAlert_ShareDetails(String text, String userName) {

		this.selectTabInAlerts("Share");
		WebElement shareUserName = commonLibrary.isExistNegative(UIMAP_SearchResult.shareUserName, 10);
		commonLibrary.setDataInTextBox(shareUserName, text, "Enter user's name");
		commonLibrary.smallWait();
		WebElement wordWheelContent = commonLibrary.isExistNegative(UIMAP_SearchResult.wordWheelContent, 10);
		List<WebElement> wordWheelOptions = commonLibrary.isExistList(wordWheelContent, UIMAP_SearchResult.lstTagUList, 10);
		List<WebElement> options = commonLibrary.isExistList(wordWheelOptions.get(0), UIMAP_SearchResult.lstTagListItems, 10);
		commonLibrary.selectFromList(options.get(0), userName);

		WebElement addToShare = commonLibrary.isExistNegative(UIMAP_SearchResult.addToShare, 10);
		commonLibrary.clickButtonLogSmallWait(addToShare, "Add to Share");

		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			if (browsername.contains("internet")) {
				commonLibrary.clickButtonParentWithWaitJS(createAlert, "CreateAlert");
			} else {
				commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			}
			// commonLibrary.clickButton_Parent_WithWait(createAlert,
			// "CreateAlert");
			// if(commonLibrary.isExist(UIMAP_Home.btnPracArea)!=null)
			// {
			// report.updateTestLog("Click on CreateAlert",
			// "CreateAlert link is clicked", Status.PASS);
			// }
			// else
			// {
			// report.updateTestLog("Click on CreateAlert",
			// "CreateAlert link is not clicked", Status.FAIL);
			// }
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}

		return new LPABrowse(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on Create alert link in Alert
	// popup
	// # Function Name : clickonCreateAlertLink     
	// # Author : Ram Prasath
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public LPABrowse clickonCreateAlertLink() {
		WebElement createAlert = commonLibrary.isExist(UIMAP_Home.btnCreateAlert);
		if (createAlert != null) {
			commonLibrary.clickButtonParentWithWait(createAlert, "CreateAlert");
			if (commonLibrary.isExist(UIMAP_Home.btnBrowse) != null) {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is clicked", Status.PASS);
			} else {
				report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click on CreateAlert", "CreateAlert link is not clicked", Status.FAIL);
		}
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout of the application.
	// # Function Name : logout     
	// # Author : Uma
	// # Date Created : 25 Feb'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		// try {
		//
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// // commonLibrary.highlightElement(btnMore);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(btnMore, "More");
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// }
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		//
		// if (browsername.contains("internet")) {
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
		// }
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
		//
		// } catch (Exception e) {
		//
		// }

		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LPABrowse swithToCondentType(String strCondentType) {
		Boolean flg = false;
		for (int i = 0; i < 3; i++) {
			if (generalFunctions.selectCondentType(strCondentType) != 1) {
				if (generalFunctions.selectCondentType(strCondentType) == 1) {
					flg = true;
					break;
				}
			} else {
				flg = true;
				break;
			}
		}
		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink
	// # Function Name : clickDocLink2
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public Document clickDocLink2(String strDocTitle) {

		// Boolean blnFlag = false;
		// WebElement resultClass = null;
		// int i = 0;
		// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		// for (i = 0; i < 3; i++) {
		// if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult,
		// 10) != null)
		// resultClass =
		// commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);
		//
		// if (resultClass != null) {
		// WebElement OListResult = commonLibrary.isExist(resultClass,
		// By.tagName("ol"), 20);
		// if (OListResult != null) {
		// List<WebElement> OListItems = commonLibrary.isExistList(OListResult,
		// By.tagName("li"), 20);
		// for (WebElement document : OListItems) {
		// WebElement eleDocTitle = commonLibrary.isExist(document,
		// UIMAP_SearchResult.TitleClassDoc, 2);
		// if (eleDocTitle != null &&
		// eleDocTitle.getText().trim().toLowerCase().equals(strDocTitle.trim().toLowerCase()))
		// {
		// WebElement lnkDocument = commonLibrary.isExist(eleDocTitle,
		// By.tagName("a"), 20);
		// if (lnkDocument != null) {
		// // commonLibrary.ScrollToView(lnkDocument);
		// // commonLibrary.highlightElement(lnkDocument);
		// if (browsername.contains("internet")) {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// } else {
		// commonLibrary.clickLinkWithWebElementWithWait(lnkDocument,
		// lnkDocument.getText());
		// blnFlag = true;
		// break;
		// }
		//
		// }
		// }
		//
		// }
		//
		// }
		// }
		// if (!blnFlag) {
		// WebElement btnNextPage =
		// commonLibrary.isExistNegative(UIMAP_SearchResult.btnNextPage, 10);
		// if (browsername.contains("internet"))
		// commonLibrary.clickJS(btnNextPage, "Next Page");
		// else
		// commonLibrary.clickButtonParentWithWait(btnNextPage, "Next Page");
		// } else
		// break;
		// }
		//
		// if (blnFlag) {
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is displayed",
		// Status.PASS);
		// } else {
		// report.updateTestLog("Verify document " + strDocTitle +
		// " is displayed", "document " + strDocTitle + " is not displayed",
		// Status.FAIL);
		// }
		// pageCheck.PositiveCheck(driver, "document", "Document");

		generalFunctions.clickDocLink(strDocTitle);

		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on First Document Link
	// # Function Name : ClickFirstDocLink     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Document clickFirstDocLink() {
		Boolean blnFlag = false;

		try {
			String strDocTitle = null;
			WebElement resultClass = null;

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
			if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

			else if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult1, 10) != null)
				resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult1, 10);

			if (resultClass != null) {
				WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
				if (OListResult != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
					for (WebElement document : OListItems) {
						WebElement eleDocTitle = commonLibrary.isExist(document, UIMAP_SearchResult.TitleClassDoc, 2);
						if (eleDocTitle != null) {
							strDocTitle = eleDocTitle.getText();
							report.updateTestLog("Document Title ", "Document Title is " + strDocTitle, Status.DONE);

							WebElement lnkDocument = commonLibrary.isExist(eleDocTitle, By.tagName("a"), 20);
							if (lnkDocument != null) {
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						} else {
							WebElement lnkDocument = commonLibrary.isExist(document, By.tagName("a"), 20);
							if (lnkDocument != null) {
								strDocTitle = lnkDocument.getText();
								commonLibrary.clickLinkWithWebElementWithWait(lnkDocument, lnkDocument.getText());
								blnFlag = true;
								break;
							}
						}
					}

				}
			}

			if (!blnFlag)

				report.updateTestLog("Click on the document " + strDocTitle, "Not Clicked  on the document " + strDocTitle, Status.FAIL);
			/*
			 * else { WebElement pgHeader=null;
			 * 
			 * if(commonLibrary.isExist_Negative(UIMAP_SearchResult.TitleClassTOC
			 * , 10)!=null)
			 * pgHeader=commonLibrary.isExist_Negative(UIMAP_SearchResult
			 * .TitleClassTOC, 10); else
			 * if(commonLibrary.isExist_Negative(UIMAP_SearchResult
			 * .pgClassHeaderOption3, 10)!=null)
			 * pgHeader=commonLibrary.isExist_Negative
			 * (UIMAP_SearchResult.pgClassHeaderOption3, 10);
			 * 
			 * 
			 * if(pgHeader!=null &&
			 * (pgHeader.getText().toLowerCase().contains("document") &&
			 * pgHeader
			 * .getText().contains(strDocTitle)||pgHeader.getText().contains
			 * ("Expert Witness"))) report.updateTestLog("Verify document "+
			 * strDocTitle+" is displayed", pgHeader.getText()+"/"+"document "+
			 * strDocTitle+" is displayed", Status.PASS); else
			 * report.updateTestLog("Verify document "+
			 * strDocTitle+" is displayed", "document "+
			 * strDocTitle+" is displayed", Status.FAIL); }
			 */
			return new Document(scriptHelper);
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Browser back from dull
	// document to results in LPA
	// # Function Name : clickLPAbrowserbackToResults    
	// # Author : Meera
	// # Date Created : Sept' 2015
	// #*****************************************************************************************************************************

	public LPABrowse clickLPAbrowserbackToResults() {

		commonLibrary.clickBrowserBack();
		return new LPABrowse(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Date is not present beside
	// Document Link
	// # Function Name : verifyDateAbsentBesideDocument
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPABrowse verifyDateAbsentBesideDocument() {

		Boolean blnFlag = false;
		WebElement resultClass = null;

		if (commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 10) != null)
			resultClass = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 10);

		if (resultClass != null) {
			WebElement OListResult = commonLibrary.isExist(resultClass, By.tagName("ol"), 20);
			if (OListResult != null) {
				List<WebElement> OListItems = commonLibrary.isExistList(OListResult, By.tagName("li"), 20);
				for (WebElement document : OListItems) {
					List<WebElement> dataTerms = commonLibrary.isExistList(document, UIMAP_SearchResult.dataTerms, 5);
					for (WebElement item : dataTerms) {
						if (item.getText().toLowerCase().contains("date")) {
							blnFlag = true;
							report.updateTestLog("Verify Date column not displays at right of documents in the results page", "Date column displays at right of documents in the results page", Status.FAIL);
							break;
						}
					}
					if (blnFlag)
						break;
				}
				if (!blnFlag)
					report.updateTestLog("Verify Date column not displays at right of documents in the results page", "Date column not displays at right of documents in the results page", Status.PASS);
			} else
				report.updateTestLog("Verify Date column not displays at right of documents in the results page", "Result List is not displayed.", Status.FAIL);
		} else
			report.updateTestLog("Verify Date column not displays at right of documents in the results page", "Result Container is not displayed.", Status.FAIL);
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to expand Sort By dropdown
	// # Function Name : clickSortBy
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPABrowse clickSortBy() {
		WebElement sortByList = null, sortContainer = null, sortBy = null;
		boolean flag = false;
		int i = 0;

		do {

			sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
			sortBy = commonLibrary.isExist(sortContainer, UIMAP_SearchResult.sortByBtn, 10);

			if (sortBy != null && sortBy.getAttribute("class").contains("collapsed")) {
				commonLibrary.clickButtonParentWithWait(sortBy, "Sort By");
				flag = true;
			}

			sortByList = commonLibrary.isExist(UIMAP_SearchResult.sortByList, 10);
			i++;
		} while (sortByList == null && i < 3);
		if (!flag)
			report.updateTestLog("Click Sort By dropdown button.", "Sort By dropdown not present.", Status.FAIL);
		return new LPABrowse(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify sort by options absent.
	// # Function Name : verifySortByOptionAbsent     
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LPABrowse verifySortByOptionAbsent(String text) {
		boolean flag = false;
		WebElement sortContainer = commonLibrary.isExist(UIMAP_SearchResult.dropdownContainer, 10);
		WebElement sortByList = commonLibrary.isExistNegative(sortContainer, UIMAP_SearchResult.sortByList, 10);
		List<WebElement> options = commonLibrary.isExistList(sortByList, UIMAP_SearchResult.listItems, 5);
		for (WebElement item : options) {
			if (item.getText().toLowerCase().contains(text.toLowerCase())) {
				flag = true;
				report.updateTestLog("Verify " + text + " is not present in Sort By Dropdown.", text + " is present in Sort By Dropdown.", Status.FAIL);
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify " + text + " is not present in Sort By Dropdown.", text + " is not present in Sort By Dropdown.", Status.PASS);
		return new LPABrowse(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking Practice Area pages
	// # Function Name : clickPracticeAreaPage     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LPAHome clickPracticeAreaPage(String pageName) {

		WebElement practiceAreaNav = commonLibrary.isExist(UIMAP_LPAHome.practiceAreaNav, 10);
		commonLibrary.clickButtonParent(practiceAreaNav, "Practice Areas");

		WebElement childMenu = commonLibrary.isExist(UIMAP_LPAHome.childMenu, 10);
		WebElement content = commonLibrary.isExist(childMenu, UIMAP_LPAHome.homePageCont, 10);
		List<WebElement> pages = commonLibrary.isExistList(content, By.tagName("li"), 20);
		for (WebElement item : pages) {
			if (item.getText().equalsIgnoreCase(pageName)) {
				WebElement pageLink = commonLibrary.isExist(item, By.tagName("a"), 10);

				commonLibrary.clickMethod(pageLink, pageName);
				commonLibrary.sleep(50000);
				break;
			}
		}
		WebElement pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
		int counter = 0;
		do {
			counter = counter + 1;
			pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
			if (pageHeader == null)
				commonLibrary.sleep(5000);
		} while (pageHeader == null && counter <= 40);

		pageHeader = commonLibrary.isExist(UIMAP_LPAHome.pageHeader, 10);
		if (pageHeader.getText().contains(pageName))
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		else
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click DocLink
	// # Function Name : clickDocLink2
	// # Author : Divakar
	// # Date Created : March'15
	// #*****************************************************************************************************************************

	public LPAHome clickDocLinkAlone(String strDocTitle) {

		generalFunctions.clickDocLinkAlone(strDocTitle);

		return new LPAHome(scriptHelper);

	}

}
