package functionallibraries;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_LATSourceSelection;
import UIMAP.UIMAP_LexisAdvanceTax;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_Sources;
import UIMAP.UIMAP_TOC;
import UIMAP.UIMAP_WorkFolders;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

public class LexisAdvanceTax extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	WebDriverWait wait = new WebDriverWait(driver, 15);
	public static int check = 0;

	public LexisAdvanceTax(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("tax"))
				commonLibrary.sleep(5000);

		} while (!url.contains("tax") && counter < 40);

		if (!driver.getCurrentUrl().contains("tax") && !(driver.getCurrentUrl().contains("firsttime"))) {// taxsearchhome
			throw new IllegalStateException("Tax Search home page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for perform Search in Tax page.
	// # Function Name : TaxSearch
	// # Author : Gokul
	// # Date Created : Jun 23
	// #*****************************************************************************************************************************

	public Search taxSearch(String strSearchTerm) {
		Boolean blnFlag = false;
		try {

			WebElement txtIdTaxSearch = commonLibrary.isExist(UIMAP_Home.txtIdtaxlSearch, 20);
			WebElement btnIdTaxSearch = commonLibrary.isExist(UIMAP_Home.btnIdtaxSearch, 20);
			if (txtIdTaxSearch != null && btnIdTaxSearch != null) {
				commonLibrary.setDataInTextBox(txtIdTaxSearch, strSearchTerm, "SearchTerm");
				commonLibrary.clickButtonParentWithWait(btnIdTaxSearch, "Search");
				commonLibrary.sleep(Mwait);
			}

			WebElement btnIdSearchResult = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
			if (btnIdSearchResult != null)
				blnFlag = true;
		} catch (Exception e) {
			blnFlag = false;

		}
		if (blnFlag) {
			report.updateTestLog("Verify Search Results are Displayed", "Search Results are displayed for the SearchTerm '" + strSearchTerm + "'", Status.PASS);
		} else {

			report.updateTestLog("Verify Search Results are Displayed", "Search Results are not displayed for the SearchTerm '" + strSearchTerm + "'", Status.PASS);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance product
	// pages
	// # Function Name : productTaxSwitcher
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax productTaxSwitcher(String pageName) {
		try {
			commonLibrary.sleep(Mwait);
			WebElement btnIdLexisAdvance = driver.findElement(By.cssSelector("div[id='nav_PracticeArea_btn'] button[class*='icon la-TriangleDown']"));

			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");
			commonLibrary.sleep(Mwait);

			List<WebElement> product = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.btntaxpracticearea, 20);

			for (WebElement option : product) {
				if (option.getText().contains(pageName))

				{
					commonLibrary.clickLinkWithWebElement(option, pageName);
					commonLibrary.sleep(10000);
					break;
				}
			}

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
			if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
				report.updateTestLog(CurrentProduct.getText() + " landing page is displayed", CurrentProduct.getText() + " landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog(CurrentProduct.getText() + " landing page is displayed", CurrentProduct.getText() + " landing page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on a pod link.
	// # Function Name : clickPodLink
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickPodLink(String podName, String linkName) {
		boolean flag1 = false;
		WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 10);
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				List<WebElement> links = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.links, 10);
				for (WebElement link : links) {
					if (link.getText().toLowerCase().equalsIgnoreCase(linkName.toLowerCase())) {
						commonLibrary.clickButtonParentWithWaitJS(link, link.getText());
						flag1 = true;
						break;
					}
				}

			}
			if (flag1)
				break;
		}
		if (!flag1)
			report.updateTestLog("Click link: " + linkName + " under pod: " + podName, "link: " + linkName + " is not present under pod: " + podName, Status.FAIL);
		try {
			WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			int counter = 0;
			do {
				commonLibrary.sleep(100000);
				counter++;
				currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			} while (currProdOld.equals(currProdNew) && counter < 40);
		} catch (Exception e1) {
			commonLibrary.sleep(500000);
			System.out.println(e1.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying page header.
	// # Function Name : verifyHeader
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyHeader(String heading) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
		if (CurrentProduct.getText().toLowerCase().contains(heading.toLowerCase()) || (driver.getCurrentUrl().contains(heading.replace(" ", "").toLowerCase()))) {
			report.updateTestLog(heading + " landing page is displayed", heading + " landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog(heading + " landing page is displayed", heading + " landing page is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source under pods
	// # Function Name : expandSource
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSource(String podName, String linkName, String subLink) {
		try {

			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
					if (!linkName.equals("")) {
						List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : lists) {
							if (item.getText().toLowerCase().equalsIgnoreCase(linkName.toLowerCase())) {
								WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
								WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
								if (srcColapse != null) {
									commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
									try {
										String loadProp = properties.getProperty("xSpinner");
										int count = 0;
										WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										do {
											commonLibrary.sleep(100000);
											loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
											count++;
										} while (loader != null && count < 15);
									} catch (Exception e) {
										System.out.println(e.toString());
									}
									if (subLink != "") {
										List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
										for (WebElement item2 : lists1) {
											if (item2.getText().toLowerCase().contains(subLink.toLowerCase())) {
												WebElement togleBar1 = commonLibrary.isExist(item2, By.tagName("div"), 10);
												WebElement srcColapse1 = commonLibrary.isExist(togleBar1, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
												if (srcColapse1 != null) {
													commonLibrary.clickButtonParentWithWait(srcColapse1, subLink);
													try {
														String loadProp = properties.getProperty("xSpinner");
														int count = 0;
														WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														do {
															commonLibrary.sleep(100000);
															loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
															count++;
														} while (loader != null && count < 20);
													} catch (Exception e) {
														System.out.println(e.toString());
													}
													flag1 = true;
													break;
												}
											}
										}
									} else {
										flag1 = true;
										break;
									}
								} else {
									WebElement srcExp = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceExpanded, 10);
									if (srcExp != null) {
										flag1 = true;
										List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
										for (WebElement item2 : lists1) {
											if (item2.getText().toLowerCase().contains(subLink.toLowerCase())) {
												commonLibrary.clickButtonParentWithWait(item2, subLink);
												try {
													String loadProp = properties.getProperty("xSpinner");
													int count = 0;
													WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
													do {
														commonLibrary.sleep(100000);
														loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														count++;
													} while (loader != null && count < 20);
												} catch (Exception e) {
													System.out.println(e.toString());
												}
												flag1 = true;
												break;
											}
										}
									}
								}
							}
						}
					} else {
						WebElement togleBar1 = commonLibrary.isExist(pod, By.tagName("div"), 10);
						WebElement srcColapse1 = commonLibrary.isExist(togleBar1, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
						if (srcColapse1 != null) {
							commonLibrary.clickButtonParentWithWait(srcColapse1, subLink);
							try {
								String loadProp = properties.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(100000);
									loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 20);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
							flag1 = true;
							break;
						} else {
							flag1 = true;
							break;
						}
					}

				}
				if (flag1)
					break;
			}
			if (!flag1) {
				if (!linkName.equals("")) {
					report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is not present under pod: " + podName, Status.FAIL);
				} else {
					report.updateTestLog("Expand pod: " + podName, podName + " pod is not present", Status.FAIL);
				}
			} else {
				if (!linkName.equals("")) {
					report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is expanded under pod: " + podName, Status.PASS);
				} else {
					report.updateTestLog("Expand pod: " + podName, podName + " is expanded", Status.PASS);
				}
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source
	// # Function Name : expandChildSource
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandChildSourceAndClickOption(String podName, String subLink, String childName, String option) {
		boolean flag = false;

		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				WebElement showmorebutton = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.showmorebutton, 10);
				if (showmorebutton != null) {
					commonLibrary.clickButton(showmorebutton);
				}
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					System.out.println(item.getText());
					WebElement labelName = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.labelName, 10);
					if (labelName != null && labelName.getText().toLowerCase().contains(subLink.toLowerCase())) {
						WebElement sourceCollapsed = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
						if (sourceCollapsed != null) {
							commonLibrary.clickButtonParentWithWait(sourceCollapsed, "Expand near " + subLink);
							try {
								String loadProp = properties.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(100000);
									loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 20);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
						}
						List<WebElement> child = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
						for (WebElement item1 : child) {
							System.out.println("child under statutes: " + item1.getText());
							if (item1.getAttribute("data-label").equalsIgnoreCase(childName)) {
								commonLibrary.clickButtonParentWithWait(item1, childName);
								try {
									String loadProp = properties.getProperty("xSpinner");
									int count = 0;
									WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									do {
										commonLibrary.sleep(100000);
										loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										count++;
									} while (loader != null && count < 20);
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								WebElement dropmenu = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.dropmenu, 10);
								if (dropmenu != null) {
									report.updateTestLog("Verify Action menu displayes in dropdown for " + childName, option + "Action drop down is displayed", Status.PASS);
									List<WebElement> buttons = commonLibrary.isExistList(dropmenu, By.tagName("button"), 10);
									for (WebElement btn : buttons) {
										if (btn.isEnabled() && btn.getText().toLowerCase().contains(option.toLowerCase())) {
											commonLibrary.clickButtonParentWithWait(btn, option);
											WebElement formClassResults = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 5);
											int resultCount = 0;
											do {
												formClassResults = commonLibrary.isExistNegative(UIMAP_SearchResult.frmClassResult, 5);
												resultCount++;
												commonLibrary.sleep(100000);
											} while (formClassResults == null && resultCount <= 40);
											flag = true;
											break;
										}
									}
									if (flag)
										break;
								}

							}
						}
						if (flag)
							break;
					}
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is selected under source " + subLink + " inside pod " + podName, Status.PASS);
		} else {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is not selected under source " + subLink + " inside pod " + podName, Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the landing page
	// # Function Name : verifyLandingPage     
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLandingPage(String landingPageString) {
		if (driver.getTitle().contains(landingPageString))
			report.updateTestLog("Verify Landing Page", landingPageString + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Landing Page", landingPageString + " is NOT displayed", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switch Tab
	// # Function Name : switchTab
	// # Author : Aravind M
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax switchTab(String tabName) {
		int j = 0;
		boolean flag = false;
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl, UIMAP_LexisAdvanceTax.li, 10);
		for (int i = 0; i < taxTabLiList.size(); i++) {
			// WebElement button = commonLibrary.isExist(taxTabLiList.get(i),
			// UIMAP_LexisAdvanceTax.button, 10);

			WebElement button = commonLibrary.isExist(taxTabLiList.get(i), UIMAP_LexisAdvanceTax.button, 10);
			if (button.getText().toLowerCase().contains(tabName.toLowerCase())) {
				commonLibrary.clickMouseMoveAction(button, button.getText());
				flag = true;
				break;
			}
			if (flag)
				break;
		}
		if (!flag)
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance product
	// pages
	// # Function Name : selectProductTaxSwitcher
	// # Author : Seetha
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax selectProductTaxSwitcher(String pageName) {
		try {
			commonLibrary.sleep(Mwait);
			WebElement btnIdLexisAdvance1 = commonLibrary.isExist(UIMAP_LexisAdvanceTax.practiceDiv);
			WebElement btnIdLexisAdvance = commonLibrary.isExist(btnIdLexisAdvance1, UIMAP_LexisAdvanceTax.practiceArea, 20);

			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Practice Area");
			commonLibrary.sleep(Mwait);

			List<WebElement> product = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.btntaxpracticearea, 20);
			for (WebElement option : product) {
				if (option.getText().contains(pageName)) {
					commonLibrary.clickLinkWithWebElement(option, pageName);
					commonLibrary.sleep(10000);
					break;
				}
			}

			WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
			if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
				report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is displayed", Status.PASS);
			} else {
				report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		// WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
		// 100);
		// if (btnMore != null) {
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(btnMore, "More");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
		// }
		//
		// WebElement lnkSignOut =
		// commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		// if (lnkSignOut != null)
		// if ((browsername.contains("internet")))
		// commonLibrary.clickJS(lnkSignOut, "Sign Out");
		// else
		// commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
		// "Sign Out");
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
		// }

		generalFunctions.logout();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : VerifySearchResultHeader     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifySearchResultHeader(String strPageHeader) {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader1, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_SearchResult.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_SearchResult.hdrResult, 5);
		if (HeaderSearchResult != null) {
		} else if (HeaderSearchResult1 != null) {
		} else if (HeaderSearchResult3 != null) {
		} else if (HeaderSearchResult4 != null) {
		}

		// strPageHeader = Header.getText();

		if (HeaderSearchResult != null && HeaderSearchResult.getText().toLowerCase().contains(strPageHeader))/*
																											 * ||
																											 * HeaderSearchResult1
																											 * !=
																											 * null
																											 * &&
																											 * HeaderSearchResult1
																											 * .
																											 * getText
																											 * (
																											 * )
																											 * .
																											 * toLowerCase
																											 * (
																											 * )
																											 * .
																											 * contains
																											 * (
																											 * strPageHeader
																											 * )
																											 * ||
																											 * HeaderSearchResult3
																											 * !=
																											 * null
																											 * &&
																											 * HeaderSearchResult3
																											 * .
																											 * getText
																											 * (
																											 * )
																											 * .
																											 * contains
																											 * (
																											 * strPageHeader
																											 * )
																											 * ||
																											 * HeaderSearchResult4
																											 * !=
																											 * null
																											 * &&
																											 * HeaderSearchResult4
																											 * .
																											 * getText
																											 * (
																											 * )
																											 * .
																											 * toLowerCase
																											 * (
																											 * )
																											 * .
																											 * contains
																											 * (
																											 * strPageHeader
																											 * )
																											 * )
																											 */

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else if (HeaderSearchResult != null && HeaderSearchResult.getText().contains(strPageHeader) || HeaderSearchResult1 != null && HeaderSearchResult1.getText().contains(strPageHeader))

			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is displayed", Status.PASS);
		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed", strPageHeader + " is not displayed", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click browser back
	// # Function Name : clickBrowserBack     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax clickBrowserBack() {
		commonLibrary.clickBrowserBack();
		return new LexisAdvanceTax(scriptHelper);
	}

	public LexisAdvanceTax verifyPracticeArea(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
		if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " page is displayed", pageName + " page is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source under pods
	// # Function Name : expandSourceAndAddToSearch
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSourceAndAddToSearch(String podName, String linkName, String subLink, String addtoSearch) {
		try {
			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						WebElement labelName = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.labelName, 10);
						if (labelName != null) {
							if (labelName.getText().toLowerCase().trim().split("\n")[0].equalsIgnoreCase(linkName)) {
								WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
								WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
								if (srcColapse != null) {
									commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
									try {
										String loadProp = properties.getProperty("xSpinner");
										int count = 0;
										WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										do {
											commonLibrary.sleep(10000);
											loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
											count++;
										} while (loader != null && count < 15);
									} catch (Exception e) {
										System.out.println(e.toString());
									}
									Thread.sleep(5000);
								}
								List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
								for (WebElement item2 : lists1) {
									if (item2.getText().toLowerCase().contains(subLink.toLowerCase())) {
										WebElement togleBar1 = commonLibrary.isExist(item2, By.tagName("div"), 10);
										WebElement srcColapse1 = commonLibrary.isExist(togleBar1, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
										if (srcColapse1 != null) {
											commonLibrary.clickButtonParentWithWait(srcColapse1, subLink);
											try {
												String loadProp = properties.getProperty("xSpinner");
												int count = 0;
												WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
												do {
													commonLibrary.sleep(10000);
													loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
													count++;
												} while (loader != null && count < 15);
											} catch (Exception e) {
												System.out.println(e.toString());
											}
											Thread.sleep(3000);
										}
										WebElement inner = commonLibrary.isExist(item2, UIMAP_LexisAdvanceTax.innerchild, 10);
										WebElement ULtagelement = commonLibrary.isExist(inner, By.tagName("ul"), 10);
										List<WebElement> finallist = commonLibrary.isExistList(ULtagelement, UIMAP_LexisAdvanceTax.lists, 10);
										for (WebElement addtosrch : finallist) {
											if (addtosrch.getText().toLowerCase().trim().contains(addtoSearch.toLowerCase())) {
												WebElement btnAdd = commonLibrary.isExist(addtosrch, UIMAP_LexisAdvanceTax.addtoSearch, 10);
												if (btnAdd != null) {
													commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
													try {
														String loadProp = properties.getProperty("xSpinner");
														int count = 0;
														WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														do {
															commonLibrary.sleep(10000);
															loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
															count++;
														} while (loader != null && count < 15);
													} catch (Exception e) {
														System.out.println(e.toString());
													}
													flag1 = true;
													break;
												}
											}
										}

									}
									if (flag1)
										break;

								}
								if (flag1)
									break;
							}
						}

					}
					if (flag1)
						break;
				}
			}
			if (flag1)
				report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "add all to search icon near source " + addtoSearch + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "add all to search icon near source " + addtoSearch + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter in narrowby section
	// # Function Name : verifyNarrowBySection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyNarrowBySection(String strSelectedFilterOptions) {
		ArrayList<String> arrBtn = new ArrayList<String>();
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {
			WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
			if (filterTray != null) {
				WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
				List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
				for (WebElement item : filters) {
					List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
					for (WebElement btnItem : btn) {
						arrBtn.add(btnItem.getText());
					}
				}
				WebElement moreFilters = commonLibrary.isExist(filterTray, UIMAP_Home.btnMoreFilter, 20);
				if (moreFilters != null) {
					commonLibrary.clickButtonParentWithWait(moreFilters, "More");

					WebElement sectionMoreFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulMoreFilter, 20);
					List<WebElement> filters1 = commonLibrary.isExistList(sectionMoreFilter, UIMAP_Home.lstTagListItems, 10);
					for (WebElement item : filters1) {
						List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
						for (WebElement btnItem : btn) {
							arrBtn.add(btnItem.getText());

						}
					}
				}

			}
			boolean flag = false;
			for (String listValues : arrBtn) {
				for (String lists : arrSelectedFilterOptions) {
					if (listValues.contains(lists.trim())) {
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
			if (flag) {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy section", strSelectedFilterOptions + " is displayed in NarrowBy section " + arrBtn, Status.PASS);
			} else {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy section", strSelectedFilterOptions + " is not displayed in NarrowBy section, actual was " + arrBtn, Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter in narrowby section
	// # Function Name : verifyFilterCount
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyFilterCount(String count) {
		try {
			WebElement txtIdTaxSearch = commonLibrary.isExist(UIMAP_Home.taxdiv, 20);
			WebElement counter = commonLibrary.isExist(txtIdTaxSearch, UIMAP_Home.filterCountNumber, 10);
			if (counter != null) {
				if (counter.getText().equalsIgnoreCase(count)) {
					report.updateTestLog("Verify " + count + " Filter is displayed in searchbox", count + " Filter is displayed in searchbox", Status.PASS);
				} else {
					report.updateTestLog("Verify " + count + " Filter is displayed in searchbox", count + " Filter is not displayed in searchbox", Status.FAIL);
				}
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for perform Search in Tax page.
	// # Function Name : TaxSearch
	// # Author : Gokul
	// # Date Created : Jun 23
	// #*****************************************************************************************************************************

	public LexisAdvanceTax taxSearchTax(String strSearchTerm) {
		Boolean blnFlag = false;
		try {

			WebElement txtIdTaxSearch = commonLibrary.isExist(UIMAP_Home.txtIdtaxlSearch, 20);
			commonLibrary.setDataInTextBox(txtIdTaxSearch, strSearchTerm, "SearchTerm");
			commonLibrary.sleep(50);
			WebElement btnIdTaxSearch = commonLibrary.isExist(UIMAP_Home.btnIdtaxSearch, 20);
			commonLibrary.clickButtonParentWithWait(btnIdTaxSearch, "Search");
			commonLibrary.sleep(Mwait);
			WebElement btnIdSearchResult = commonLibrary.isExist(UIMAP_SearchResult.frmClassResult, 20);
			if (btnIdSearchResult != null)
				blnFlag = true;
		} catch (Exception e) {
			blnFlag = false;

		}
		if (blnFlag) {
			report.updateTestLog(" Enter the search Term " + strSearchTerm + " and click on Search Button.", "User is able to enter the search Term + " + strSearchTerm + " and click on Search Button", Status.PASS);
		} else {
			report.updateTestLog("Enter the search Term " + strSearchTerm + " and click on Search Button", "User is not able to enter the search Term + " + strSearchTerm + " and click on Search Button", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source under pods
	// # Function Name : expandSourceAndAddToSearch
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source under pods and add
	// source filter.
	// # Function Name : expandSourceAndAddToSearch1
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSourceAndAddToSearch1(String podName, String linkName, String addtoSearch) {
		try {
			boolean flag1 = false;

			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			if (taxPods.size() > 0) {
				for (WebElement pod : taxPods) {
					WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
						WebElement showmorebutton = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.showmorebutton, 10);
						if (showmorebutton != null) {
							commonLibrary.clickButton(showmorebutton);
						}
						List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : lists) {
							if (item.getText().toLowerCase().contains(linkName.toLowerCase())) {
								if (addtoSearch != null && !addtoSearch.equals("")) {
									WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
									WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
									if (srcColapse != null) {
										commonLibrary.clickButtonParentWithWaitJS(srcColapse, linkName);
										// Thread.sleep(5000);
										try {
											srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
											WebElement srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
											int count = 0;
											do {
												commonLibrary.sleep(1000000);
												srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
												count++;
											} while (srcColapse.equals(srcColapseNew) && count < 50);
										} catch (Exception e) {
											commonLibrary.sleep(5000000);
											System.out.println(e.toString());
										}

										commonLibrary.sleep(100000);
									}
									List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
									try {
										int count = 0;
										do {
											commonLibrary.sleep(10000000);
											count++;
											lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
										} while (lists1.size() == 0 && count < 60);
									} catch (Exception e) {
										lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
										commonLibrary.sleep(5000000);
										System.out.println(e.toString());
									}

									if (lists1.size() > 0) {
										report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + " displays", Status.PASS);
										for (WebElement addtosrch : lists1) {
											if (addtosrch.getText().toLowerCase().contains(addtoSearch.toLowerCase())) {
												WebElement btnAdd = commonLibrary.isExist(addtosrch, UIMAP_LexisAdvanceTax.addtoSearch, 10);
												if (btnAdd != null) {
													commonLibrary.clickButtonParentWithWaitJS(btnAdd, "add to search");
													try {
														String loadProp = properties.getProperty("xSpinner");
														int count = 0;
														WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														do {
															commonLibrary.sleep(10000);
															loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
															count++;
														} while (loader != null && count < 15);
													} catch (Exception e) {
														System.out.println(e.toString());
													}
													commonLibrary.sleep(100000);
													report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is clicked", Status.PASS);
													flag1 = true;
													break;
												}
											}
										}
									} else
										report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + "does not display", Status.FAIL);
									if (flag1)
										break;

								} else {
									WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
									if (btnAdd != null) {

										commonLibrary.clickButtonParentWithWaitJS(btnAdd, "add to search");
										try {
											String loadProp = properties.getProperty("xSpinner");
											int count = 0;
											WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
											do {
												commonLibrary.sleep(10000);
												loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
												count++;
											} while (loader != null && count < 15);
										} catch (Exception e) {
											System.out.println(e.toString());
										}
										flag1 = true;
										report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is clicked", Status.PASS);
										break;
									}
								}

							}
						}

					}

					if (flag1)
						break;
				}
			}
			if (!flag1) {
				if (addtoSearch != null && !addtoSearch.equals("")) {
					report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is not clicked", Status.FAIL);
				} else
					report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for removing applied source filter.
	// # Function Name : removeFilter
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax removeFilter(String name) {
		boolean flag = false;
		int oldSize = 0;
		WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
		if (filterTray != null) {
			WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
			List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
			oldSize = filters.size();
			for (WebElement item : filters) {
				if (item.getText().toLowerCase().contains(name.toLowerCase())) {
					WebElement button = commonLibrary.isExistNegative(item, UIMAP_Home.button, 10);
					commonLibrary.clickLinkWithWebElementWithWait(button, "Remove Filter: " + name);
					flag = true;
					break;
				}
			}

		}
		if (!flag)
			report.updateTestLog("Remove filter  " + name, "The filter " + name + " is not present.", Status.FAIL);
		else {
			try {
				WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
				List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
				int count = 0;
				do {
					commonLibrary.sleep(250000);
					count++;
					ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
					filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
				} while (filters.size() == oldSize && count < 40);
			} catch (Exception e) {
				commonLibrary.sleep(5000000);
				System.out.println(e.toString());
			}
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying filter is removed.
	// # Function Name : verifyFilterAbsent
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyFilterAbsent(String name) {
		boolean flag = false;
		WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
		if (filterTray != null) {
			WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
			List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
			for (WebElement item : filters) {
				if (item.getText().toLowerCase().contains(name.toLowerCase())) {
					report.updateTestLog("Verify filter  " + name + " is not present.", "The filter " + name + " is present.", Status.FAIL);
					flag = true;
				}
			}

		}
		if (!flag)
			report.updateTestLog("Verify filter  " + name + " is not present.", "The filter " + name + " is not present.", Status.PASS);

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on a pod link.
	// # Function Name : clickPodLink
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public Search clickPodLink1(String podName, String linkName) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				List<WebElement> links = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.links, 10);
				for (WebElement link : links) {
					if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
						commonLibrary.clickButtonParentWithWait(link, link.getText());
						flag1 = true;
						break;
					}
				}

			}
			if (flag1)
				break;
		}
		if (!flag1)
			report.updateTestLog("Click link: " + linkName + " under pod: " + podName, "link: " + linkName + " is not present under pod: " + podName, Status.FAIL);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying the component page
	// displayed.
	// # Function Name : verifySearchComponentPage
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifySearchComponentPage(String pageName) {
		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
		if (CurrentProduct.getText().toLowerCase().contains(pageName.toLowerCase()) || (driver.getCurrentUrl().contains(pageName.replace(" ", "").toLowerCase()))) {
			report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is displayed", Status.PASS);
		} else {
			report.updateTestLog(pageName + " landing page is displayed", pageName + " landing page is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying the component page
	// displayed.
	// # Function Name : verifySearchComponentPage
	// # Author : Pratik
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickProductLogo() {
		generalFunctions.clickProductLogo();
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select source pod from previously
	// expanding source under pods
	// # Function Name : previousExpandedSourceAndAddToSearch
	// # Author : Kirthika
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax previousExpandedSourceAndAddToSearch(String podName, String linkName) {
		try {
			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						if (item.getText().toLowerCase().trim().equalsIgnoreCase(linkName.toLowerCase())) {
							WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
							if (btnAdd != null) {
								commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
								flag1 = true;
								break;
							}
						}
					}
				}
			}
			if (flag1)
				report.updateTestLog("Select add all to search icon near source " + linkName + "", "add all to search icon near source " + linkName + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add all to search icon near source " + linkName + "", "add all to search icon near source " + linkName + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Name
	// # Function Name : verifyPod
	// # Author : Kirthika K
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyPod(String podName) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().trim().toLowerCase().contains(podName.trim().toLowerCase())) {

				flag1 = true;
				break;
			}
		}

		if (!flag1)
			report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is not displayed", Status.FAIL);
		else
			report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is  displayed", Status.PASS);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify addtosearch,favorite and
	// deleteicon
	// source filter.
	// # Function Name : verifyIconContainer
	// # Author : KirthikaK
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyIconContainer(String podName) {
		try {
			boolean flag1 = false;

			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
			if (taxPods.size() > 0) {
				for (WebElement pod : taxPods) {
					WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

						List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : lists) {

							WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
							WebElement btnFavorite = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addToFavorite, 10);
							if (btnFavorite == null)
								btnFavorite = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addToFavorite1, 10);
							WebElement btnDelete = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addToDelete, 10);

							if (btnAdd != null && btnFavorite != null && btnDelete != null) {
								flag1 = true;
								break;
							}
						}
					}

					if (flag1)
						break;
				}
			}

			if (!flag1) {
				report.updateTestLog("Verify Add to search icon,favorite icon,delete icon is displayed near source in " + podName + " pod", "Add to search icon,favorite icon,delete icon is  not displayed near source in " + podName + " pod", Status.FAIL);

			} else
				report.updateTestLog("Verify Add to search icon,favorite icon,delete icon is displayed near source in " + podName + " pod", "Add to search icon,favorite icon,delete icon is displayed near source in " + podName + " pod", Status.PASS);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Name
	// # Function Name : verifyPod
	// # Author : Kirthika K
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyPod1(String podName) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().trim().toLowerCase().contains(podName.trim().toLowerCase())) {

				flag1 = true;
				break;
			}
		}

		if (!flag1)
			report.updateTestLog("Verify " + podName + " pod  is displayed", podName + "  is not displayed", Status.FAIL);
		else
			report.updateTestLog("Verify " + podName + " pod  is displayed", podName + "  pod is  displayed", Status.PASS);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify addtosearch,favorite and
	// deleteicon
	// source filter.
	// # Function Name : verifyIconContainer
	// # Author : KirthikaK
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyIconContainer1(String podName) {
		try {
			boolean flag1 = false;

			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			if (taxPods.size() > 0) {
				for (int i = 0; i < taxPods.size(); i++) {
					WebElement header = commonLibrary.isExistNegative(taxPods.get(i), UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
						WebElement btnAdd = commonLibrary.isExist(taxPods.get(i + 1), UIMAP_LexisAdvanceTax.addtoSearch, 10);

						if (btnAdd != null) {
							flag1 = true;
							break;
						}

					}

					if (flag1)
						break;
				}
			}

			if (!flag1) {
				report.updateTestLog("Verify Add to search icon is displayed near source in " + podName + " pod", "Add to search icon is not displayed near source in " + podName + " pod", Status.FAIL);

			} else
				report.updateTestLog("Verify Add to search icon is displayed near source in " + podName + " pod", "Add to search icon is displayed near source in " + podName + " pod", Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to choose all sources in the pod
	// # Function Name : chooseAllSources
	// # Author : KirthikaK
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax chooseAllSources(String podName) {
		try {
			boolean flag1 = false;

			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			if (taxPods.size() > 0) {
				for (WebElement pod : taxPods) {
					WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
						WebElement btnAdd = commonLibrary.isExist(header, UIMAP_LexisAdvanceTax.addtoSearch, 10);

						if (btnAdd != null) {
							commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
							flag1 = true;
							break;
						}

					}

					if (flag1)
						break;
				}
			}

			if (!flag1) {
				report.updateTestLog("Add all to search icon is displayed near source in " + podName, "Add all to search icon is  not displayed near source in " + podName, Status.FAIL);

			} else
				report.updateTestLog("Add all to search icon is displayed near source " + podName, "Add all to search icon is displayed near source in " + podName, Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Get documents for a source
	// # Function Name : clickGetDocuments
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults clickGetDocuments() {

		boolean blnFlag = false;

		WebElement sourceDropDown = commonLibrary.isExist(UIMAP_LexisAdvanceTax.sourcePop, 10);

		// if (sourceDropDown.isDisplayed()) {
		List<WebElement> sourceDropDownOptions = commonLibrary.isExistList(sourceDropDown, UIMAP_LexisAdvanceTax.sourceItems, 10);
		for (WebElement option : sourceDropDownOptions) {
			WebElement link = commonLibrary.isExistNegative(option, UIMAP_LexisAdvanceTax.getdocssource1, 10);

			if (link != null && link.getText().toLowerCase().contains("get all documents for this source")) {
				commonLibrary.clickLinkWithWait(link, link.getText());
				commonLibrary.sleep(10000);

				report.updateTestLog("Click on button Get Documents", "Clicked on button Get Documents", Status.PASS);
				int counter = 0;
				do {
					commonLibrary.sleep(10000);
					counter = counter + 1;
				} while (!driver.getCurrentUrl().contains("taxsearchresults") && counter <= 15);

				blnFlag = true;
				break;
			}
		}

		// }

		if (!blnFlag)
			report.updateTestLog("Click on Get Documents.", "Get Documents option is not present.", Status.FAIL);

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Source
	// # Function Name : clickSourceName
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Sources clickSourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.sourceButton, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().contains(sourceName.toLowerCase())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.button, 10);
				commonLibrary.clickButtonLogSmallWait(button, sourceName);
				blnFlag = true;
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new Sources(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on a pod link.
	// # Function Name : clickPodLink2
	// # Author : Meera
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickPodLink2(String linkName) {

		boolean blnFlag = false;
		WebElement taxPod = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxPods, 10);
		WebElement pod = commonLibrary.isExist(taxPod, UIMAP_LexisAdvanceTax.federalTaxPods, 20);
		WebElement content = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.taxPods1, 10);
		WebElement content1 = commonLibrary.isExist(content, UIMAP_LexisAdvanceTax.content1, 10);
		WebElement expandIcon = commonLibrary.isExist(content1, UIMAP_LexisAdvanceTax.toggleBar, 10);
		WebElement button = commonLibrary.isExist(expandIcon, UIMAP_LexisAdvanceTax.expandIcon, 10);

		if (button != null) {
			commonLibrary.clickButton(button);
			report.updateTestLog("click on expand icon", "pod is expanded", Status.PASS);
		}

		else {
			report.updateTestLog("click on expand icon", "pod is not expanded", Status.FAIL);
		}

		WebElement child = commonLibrary.isExist(UIMAP_LexisAdvanceTax.innerchild, 20);
		WebElement childList = commonLibrary.isExist(child, UIMAP_LexisAdvanceTax.childList1, 10);
		List<WebElement> liChild = commonLibrary.isExistList(childList, UIMAP_LexisAdvanceTax.liList, 10);

		if (liChild.size() > 0) {
			for (WebElement item : liChild)

			{
				WebElement list = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.childList, 10);

				if (list.getText().toLowerCase().equals(linkName.toLowerCase())) {

					commonLibrary.clickButton(list);
					blnFlag = true;

				}
			}
		}
		if (blnFlag) {

			report.updateTestLog("click on link", "drop down is displayed", Status.PASS);
		}

		else {
			report.updateTestLog("click on link", "drop down is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on a pod link.
	// # Function Name : clickPodLink3
	// # Author : Meera
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickPodLink3(String linkName) {
		boolean blnFlag = false;
		WebElement taxPod = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxPods, 10);
		WebElement pod = commonLibrary.isExist(taxPod, UIMAP_LexisAdvanceTax.federalTaxPods, 20);
		WebElement content = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.taxPods2, 10);
		WebElement content1 = commonLibrary.isExist(content, UIMAP_LexisAdvanceTax.content2, 10);
		WebElement expandIcon = commonLibrary.isExist(content1, UIMAP_LexisAdvanceTax.toggleBar, 10);
		WebElement button = commonLibrary.isExist(expandIcon, UIMAP_LexisAdvanceTax.expandIcon, 10);

		if (button != null) {
			commonLibrary.clickButton(button);
			report.updateTestLog("click on expand icon", "pod is expanded", Status.PASS);
		}

		else {
			report.updateTestLog("click on expand icon", "pod is not expanded", Status.FAIL);
		}

		WebElement child = commonLibrary.isExist(UIMAP_LexisAdvanceTax.innerchild, 20);
		WebElement childList = commonLibrary.isExist(child, UIMAP_LexisAdvanceTax.childList1, 10);
		List<WebElement> liChild = commonLibrary.isExistList(childList, UIMAP_LexisAdvanceTax.liList, 10);

		if (liChild.size() > 0) {
			for (WebElement item : liChild)

			{
				WebElement list = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.childList, 10);

				if (list.getText().toLowerCase().equals(linkName.toLowerCase())) {

					commonLibrary.clickButton(list);
					blnFlag = true;

				}
			}
		}
		if (blnFlag) {

			report.updateTestLog("click on link", "drop down is displayed", Status.PASS);
		}

		else {
			report.updateTestLog("click on link", "drop down is not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	public LexisAdvanceTax expandSource1(String podName, String linkName, String subLink) {
		try {

			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						if (item.getText().toLowerCase().equalsIgnoreCase(linkName.toLowerCase())) {
							WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
							WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
							if (srcColapse != null) {
								commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
								Thread.sleep(5000);
								if (subLink != "") {
									List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
									for (WebElement item2 : lists1) {
										if (item2.getText().toLowerCase().contains(subLink.toLowerCase())) {
											WebElement togleBar1 = commonLibrary.isExist(item2, By.tagName("div"), 10);
											WebElement srcColapse1 = commonLibrary.isExist(togleBar1, UIMAP_LexisAdvanceTax.sourceCollapsed1, 10);
											if (srcColapse1 != null) {
												commonLibrary.clickButtonParentWithWait(srcColapse1, subLink);
												flag1 = true;
												break;
											}
										}
									}
								} else {
									flag1 = true;
									break;
								}

							}
						}
					}

				}
				if (flag1)
					break;
			}
			if (!flag1)
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is not present under pod: " + podName, Status.FAIL);
			else {
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is expanded under pod: " + podName, Status.PASS);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for navigating to Lexis Advance Home
	// page
	// # Function Name : navigateToResearch
	// # Author : Kalaivanan
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Home navigateToResearch() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Home.btnLexisAdvanceResearch, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Lexis Advance® Research");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Lexis Advance® Research");

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults simpleSearch(String strSearchTerm, Boolean strClearFilter) {

		// Function calling for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select source pod from previously
	// expanding source under pods
	// # Function Name : previousExpandedSourceAndAddToSearch
	// # Author : Kirthika
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickOnAddtoSourceInRecentAndFavPod(String linkName) {
		try {
			boolean flag1 = false;
			for (int i = 0; i < 3; i++) {

				WebElement pod = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.recentPod, 10);
				WebElement section = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.federalTaxPods, 10);
				if (section != null) {
					// WebElement header = commonLibrary.isExistNegative(pod,
					// UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					// if
					// (header.getText().toLowerCase().contains(podName.toLowerCase()))
					// {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						if (item.getText().toLowerCase().trim().equalsIgnoreCase(linkName.toLowerCase())) {
							WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
							if (btnAdd != null) {
								commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
								flag1 = true;
								break;
							}
						}
					}
					// }
				}
				if (!flag1) {
					if (section != null) {
						// WebElement header =
						// commonLibrary.isExistNegative(pod,
						// UIMAP_LexisAdvanceTax.taxPodHeader, 10);
						// if
						// (header.getText().toLowerCase().contains(podName.toLowerCase()))
						// {

						List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						// for (WebElement item : lists) {

						WebElement btnDelete = commonLibrary.isExist(lists.get(0), UIMAP_LexisAdvanceTax.addToDelete, 10);
						if (btnDelete != null) {
							commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Source");
							commonLibrary.sleep(5000);
						}
						WebElement btnContinue = commonLibrary.isExist(UIMAP_LexisAdvanceTax.btncontiune, 10);
						if (btnContinue != null) {
							commonLibrary.clickButtonParentWithWait(btnContinue, "Confirm");
							commonLibrary.sleep(5000);
						}
						lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : lists) {
							if (item.getText().toLowerCase().trim().equalsIgnoreCase(linkName.toLowerCase())) {
								WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
								if (btnAdd != null) {
									commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
									flag1 = true;
									break;
								}
							}
						}

					}
				}
				if (i == 2 || flag1)
					break;
			}
			if (flag1)
				report.updateTestLog("Select add all to search icon near source " + linkName + "", "add all to search icon near source " + linkName + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add all to search icon near source " + linkName + "", "add all to search icon near source " + linkName + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the particular tab in the
	// navigation bar
	// # Function Name : navigateToTab     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LATSourceSelection navigateToTab(String tab) {

		// WebElement taxtab =
		// commonLibrary.isExist(UIMAP_LexisAdvanceTax.divTax, 10);
		WebElement divTabs = commonLibrary.isExist(UIMAP_LexisAdvanceTax.divTabs, 10);
		List<WebElement> lists = commonLibrary.isExistList(divTabs, UIMAP_LexisAdvanceTax.lists, 10);
		for (WebElement list : lists) {
			if (list.getText().toLowerCase().contains(tab.toLowerCase())) {
				WebElement button = commonLibrary.isExist(list, UIMAP_LexisAdvanceTax.links, 10);
				if (button != null) {
					commonLibrary.clickButtonParentWithWait(button, tab);
				}
			}
		}

		return new LATSourceSelection(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link displays in a pod
	// # Function Name : verifyLinkDisplaysInPod     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLinkDisplaysInPod(String podName, String linkName) {

		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				List<WebElement> links = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.links, 10);
				for (WebElement link : links) {
					if (link.getText().toLowerCase().contains(linkName.toLowerCase())) {
						report.updateTestLog("Verify " + linkName + " link displays in pod: " + podName, linkName + " link is displayed in pod: " + podName, Status.PASS);
						flag1 = true;
						break;
					}
				}

			}
			if (flag1)
				break;
		}

		if (!flag1)
			report.updateTestLog("Verify " + linkName + " link displays in pod: " + podName, linkName + " link is not displayed in pod: " + podName, Status.FAIL);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify source group displays
	// # Function Name : verifySourceGroupDisplays     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifySourceGroupDisplays(String sourceName) {
		boolean blnFlag = false;
		String[] sourceNameList = sourceName.split(";");
		int sourceNameSize = sourceNameList.length;
		int count = 0;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		if (taxPods.size() > 0) {
			for (WebElement pod : taxPods) {
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				if (lists.size() > 0) {
					for (int i = 0; i < lists.size(); i++) {
						for (int j = 0; j < sourceNameList.length; j++) {
							if (lists.get(i).getText().toLowerCase().contains(sourceNameList[j].toLowerCase())) {

								report.updateTestLog("Verify source " + sourceNameList[j] + " displays", "Source " + sourceNameList[j] + " is displayed", Status.PASS);
								count++;
							}
							if (count == sourceNameList.length) {
								blnFlag = true;
								break;
							}
						}
						if (blnFlag)
							break;
					}
				}
				if (blnFlag)
					break;
			}
			if (count != sourceNameSize)
				report.updateTestLog("Verify source displays", "Not all the sources are displayed", Status.FAIL);
		} else {
			report.updateTestLog("Verify source displays", "Sources are not displayed", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source and add source
	// filter.
	// # Function Name : expandSourceAndAddToSearch2
	// # Author : Anbu
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSourceAndAddToSearch2(String linkName, String addtoSearch) {
		try {
			boolean flag1 = false;

			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			if (taxPods.size() > 0) {
				for (WebElement pod : taxPods) {
					WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(linkName.toLowerCase())) {

						if (addtoSearch != null && !addtoSearch.equals("")) {
							WebElement togleBar = commonLibrary.isExist(pod, By.tagName("div"), 10);
							WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
							if (srcColapse != null) {
								commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
								// Thread.sleep(5000);
								try {
									srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
									WebElement srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
									int count = 0;
									do {
										commonLibrary.sleep(1000000);
										srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
										count++;
									} while (srcColapse.equals(srcColapseNew) && count < 50);
								} catch (Exception e) {
									commonLibrary.sleep(5000000);
									System.out.println(e.toString());
								}

								commonLibrary.sleep(100000);
							}
							List<WebElement> lists1 = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
							try {
								int count = 0;
								do {
									commonLibrary.sleep(10000000);
									count++;
									lists1 = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
								} while (lists1.size() == 0 && count < 60);
							} catch (Exception e) {
								lists1 = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
								commonLibrary.sleep(5000000);
								System.out.println(e.toString());
							}

							if (lists1.size() > 0) {
								report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + " displays", Status.PASS);
								for (WebElement addtosrch : lists1) {
									if (addtosrch.getText().toLowerCase().contains(addtoSearch.toLowerCase())) {
										WebElement btnAdd = commonLibrary.isExist(addtosrch, UIMAP_LexisAdvanceTax.addtoSearch, 10);
										if (btnAdd != null) {
											commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
											commonLibrary.sleep(100000);
											report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is clicked", Status.PASS);
											flag1 = true;
											break;
										}
									}
								}
							} else
								report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + "does not display", Status.FAIL);

							if (flag1)
								break;

						} else {
							WebElement btnAdd = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.addtoSearch, 10);
							if (btnAdd != null) {

								commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
								flag1 = true;
								report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is clicked", Status.PASS);
								break;
							}
						}

					}
				}
			}
			if (!flag1) {
				if (addtoSearch != null && !addtoSearch.equals(""))
					report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is not clicked", Status.FAIL);
				else
					report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on save favourite icon.
	// # Function Name : clickSaveFavourite
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickSaveFavourite() {

		WebElement filterTray = commonLibrary.isExistNegative(UIMAP_Home.filterTray, 10);
		WebElement saveFavFilter = commonLibrary.isExistNegative(filterTray, UIMAP_Home.saveFavourite, 10);
		if (saveFavFilter != null) {
			commonLibrary.clickButton(saveFavFilter);
			report.updateTestLog("Click Save as favourite button ", "Save as favourite button is clicked", Status.PASS);
		} else {
			report.updateTestLog("Click Save as favourite button ", "Save as favourite button is clicked", Status.PASS);
		}

		WebElement dismissBtn = commonLibrary.isExistNegative(UIMAP_Home.dismissBtn, 10);
		if (dismissBtn != null) {
			commonLibrary.clickButton(dismissBtn);
		} else {
			report.updateTestLog("Verify dismiss button present ", "Dismiss button is not present", Status.WARNING);
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Required Filter Option in
	// Search Results page.
	// # Function Name : selectPostFilter
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax selectPostFilter(String strHeader, String strFilter) {

		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;

			WebElement filterContainer = commonLibrary.isExistNegative(UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(filterContainer, UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class").contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(filterHeader.get(i), strHeader);
						else
							commonLibrary.clickLinkWithWebElementWithWait(filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
					}

					List<WebElement> moreOptions = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().toLowerCase().contains("more")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button, "More");
							else
								commonLibrary.clickButtonLogSmallWait(button, "More");
						}
					}
					List<WebElement> filters = commonLibrary.isExistList(suplementalFilters.get(i - j), UIMAP_SearchResult.eltFilterList, 20);
					for (WebElement item : filters) {
						WebElement span = commonLibrary.isExistNegative(item, UIMAP_SearchResult.tagSpan, 10);
						if (span.getText().equals(strFilter)) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item, strFilter);
							else
								commonLibrary.clickButtonParentWithWait(item, strFilter);
							try {
								String loadProp = properties.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(10000);
									loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 15);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
							report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.", Status.DONE);
							Flag = true;
							break;
						}
					}
				}
				if (Flag)
					break;
			}

			// WebElement filterContainer =
			// commonLibrary.isExist_Negative(UIMAP_SearchResultPage.filterContainer,
			// 10);
			// List<WebElement> filterMoreList =
			// commonLibrary.isExist_List(filterContainer,
			// UIMAP_SearchResultPage.filterMore, 10);
			// for(WebElement more:filterMoreList)
			// if(more.getText().toLowerCase().contains("more"))
			// commonLibrary.clickButton_Log_SmallWait(more, "More");
			//
			// eltFilter=
			// commonLibrary.isExist_List(UIMAP_SearchResultPage.eltFilterList,10);
			// for(i=0;i<eltFilter.size();i++)
			// {
			// if(eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase()))
			// {
			// commonLibrary.highlightElement(eltFilter.get(i));
			// commonLibrary.clickLink_withWebElement_WithWait(eltFilter.get(i),
			// eltFilter.get(i).getText());
			// report.updateTestLog("Selecting Filter: "+strFilter,
			// "Required Filter Selected.", Status.DONE);
			// blnFlag=true;
			// break;
			// }
			// }
			if (!Flag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
				if (!Flag) {
					report.updateTestLog("Selecting Filter: " + strFilter, strFilter + " Filter is not Selected.", Status.FAIL);
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter, "No Filter Selected.", Status.DONE);
		try {
			int count = 0;
			WebElement appliedFiltersOld = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
			WebElement appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 3);
			do {
				commonLibrary.sleep(200000);
				System.out.println("Filters Waiting " + count);
				appliedFiltersNew = commonLibrary.isExistNegative(UIMAP_SearchResult.eltFiltersUsed, 2);
				count++;
			} while (appliedFiltersOld == appliedFiltersNew && count < 60);
		} catch (Exception e) {
			commonLibrary.sleep(1000000);
			System.out.println(e.toString());
		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document title header and
	// other details in legal trial page
	// # Function Name : ClickDocLink1     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document clickDocLink(String strDocTitle) {

		generalFunctions.clickDocLink(strDocTitle);
		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delte and verify sources from pod
	// # Function Name : deleteResourcesFromPod
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax deleteResourcesFromPod(String podName, String Source) {
		try {
			boolean flag1 = false;
			boolean presenceFlag = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
			if (taxPods.size() > 0) {
				for (WebElement pod : taxPods) {
					WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
					if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
						WebElement showMore = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.showMore, 10);
						if (showMore != null) {
							commonLibrary.clickMethod(showMore, "More Under Pod:" + podName);
							try {
								String loadProp = properties.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(10000);
									loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 15);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
						}
						List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : lists) {
							if (item.getText().toLowerCase().contains(Source.toLowerCase())) {
								presenceFlag = true;
								WebElement btnDelete = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addToDelete, 10);
								if (btnDelete != null) {
									commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Source");
									WebElement deltePopup = commonLibrary.isExist(UIMAP_LexisAdvanceTax.deleteDialogPopUp, 10);
									WebElement btnContinue = commonLibrary.isExist(deltePopup, UIMAP_LexisAdvanceTax.btncontiune, 10);
									if (btnContinue != null) {
										commonLibrary.clickButtonParentWithWait(btnContinue, "Continue");
										flag1 = true;
									} else {
										report.updateTestLog("Confirm pop up after delete icon is clicked display" + podName, "Confirm pop up after delete icon is clicked is not displayed " + podName, Status.FAIL);
									}

								} else {
									report.updateTestLog("Delete icon display aside the source" + Source + " under " + podName, "Delete icon is not displayed aside the source" + Source + " under " + podName, Status.FAIL);
								}
							}

							if (flag1)
								break;
						}
					}
				}
				if (presenceFlag) {
					if (!flag1) {
						report.updateTestLog("Verify " + Source + " is present under " + podName, Source + " is present under " + podName, Status.FAIL);

					} else
						report.updateTestLog("Verify " + Source + " is present under " + podName, Source + " is not present under " + podName, Status.PASS);
				} else
					report.updateTestLog("Verify " + Source + " is present under " + podName, Source + " is not present under " + podName, Status.PASS);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify clear button in narrowby
	// section
	// # Function Name : verifyClearBtnNarrowBySection
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyClearBtnNarrowBySection() {
		WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
		if (filterTray != null) {
			WebElement clearbtn = commonLibrary.isExist(filterTray, UIMAP_Home.btnClassClearFilter, 20);
			if (clearbtn != null) {
				report.updateTestLog("Verify clear button under narrow by section", "Clear button is present under narrow by section", Status.PASS);
			} else
				report.updateTestLog("Verify clear button under narrow by section", "Clear button is not present under narrow by section", Status.FAIL);
		} else
			report.updateTestLog("Verify Narrow by section in LAT home page", "Narrow by section is not present in LAT home page", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Child Filter Option under
	// Parent Filter in Search Results page.
	// # Function Name : verifyChildPostFilter
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyChildPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			List<WebElement> eltFilterList = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltExpandedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltExpandedFilterHeader, 10);
			for (i = 0; i < eltExpandedFilterHeader.size(); i++) {
				if (eltExpandedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag)
				report.updateTestLog("Verify post filter header: " + strHeader, strHeader + " header is present and Expanded.", Status.PASS);
			else
				report.updateTestLog("Verify post filter header: " + strHeader, strHeader + " post filter header is not present in the left pane", Status.FAIL);

			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.morechildFilterOption, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
				}
			}
			eltFilterList = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilterList.size(); i++) {
				if (eltFilterList.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag)
				report.updateTestLog("Verifying Filter: " + strFilter, strFilter + " source is present under " + strHeader, Status.PASS);
			else {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						blnFlag = true;
						break;
					}
				}
				if (blnFlag)
					report.updateTestLog("Verifying Filter: " + strFilter, strFilter + " required filter is verified", Status.PASS);
				else
					report.updateTestLog("Verifying Filter: " + strFilter, strFilter + " required filter is not verified", Status.FAIL);
			}
		} else
			report.updateTestLog("Verifying Filter: " + strFilter, "No Filter Selected.", Status.FAIL);

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the document title header and
	// other details in legal trial page
	// # Function Name : ClickDocLink1     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax VerifySwitchTab(String tabName) {
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		WebElement taxTabUlLi = commonLibrary.isExist(taxTabUl, UIMAP_LexisAdvanceTax.taxTabUlli, 10);
		if (taxTabUlLi != null) {
			String strTab = taxTabUlLi.getText();
			if (strTab.equalsIgnoreCase(tabName)) {
			} else
				report.updateTestLog("Selecting Filter: " + strTab, strTab + " Filter is not Selected.", Status.FAIL);
		} else {
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);

		/*
		 * List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl,
		 * UIMAP_LexisAdvanceTax.li, 10); for (int i = 0; i <
		 * taxTabLiList.size(); i++) { WebElement button =
		 * commonLibrary.isExist(taxTabLiList.get(i),
		 * UIMAP_LexisAdvanceTax.button, 10); if
		 * (button.getText().toLowerCase().contains(tabName.toLowerCase()) &&
		 * (driver.getCurrentUrl().contains(tabName.replace(" ",
		 * "").toLowerCase()))) { flag = true; break; } if (flag) break; } if
		 * (!flag) report.updateTestLog("Click tab: " + tabName, "Tab: " +
		 * tabName + " is not present", Status.FAIL); return new
		 * LexisAdvanceTax(scriptHelper);
		 */

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Child Filter Option under
	// Parent Filter in Search Results page.
	// # Function Name : selectChildPostFilter
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax selectChildPostFilter(String strHeader, String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0;
			boolean blnFlag = false;
			List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
			for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
				if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(strHeader.toUpperCase())) {
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltCollapsedFilterHeader.get(i), strHeader);
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltCollapsedFilterHeader.get(i), strHeader);
					}
					report.updateTestLog("Expanding Filter Header: " + strHeader, strHeader + " filter Header Expanded.", Status.DONE);
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.morechildFilterOption, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains("MORE")) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
				}
			}
			eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList, 10);
			for (i = 0; i < eltFilter.size(); i++) {
				if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
					// commonLibrary.highlightElement(eltFilter.get(i));
					if (browsername.contains("internet")) {
						commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
					} else {
						commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
					}
					report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
					blnFlag = true;
					break;
				}
			}
			if (!blnFlag) {
				eltFilter = commonLibrary.isExistList(UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase().contains(strFilter.toUpperCase())) {
						// commonLibrary.highlightElement(eltFilter.get(i));
						if (browsername.contains("internet")) {
							commonLibrary.clickLinkWithWebElementWithWaitJS(eltFilter.get(i), eltFilter.get(i).getText());
						} else {
							commonLibrary.clickLinkWithWebElementWithWait(eltFilter.get(i), eltFilter.get(i).getText());
						}
						report.updateTestLog("Verifying Filter: " + strFilter, "Required Filter Verified.", Status.DONE);
						blnFlag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Verifying Filter: " + strFilter, "No Filter Selected.", Status.DONE);

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyNarrowbyFilterLeftPane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + strFilter, Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Post filter Applied
	// # Function Name : verifyFilter
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyFilter(String filterName) {
		boolean flag = false;
		commonLibrary.sleep(5000);
		WebElement filtersUsed = commonLibrary.isExist(UIMAP_SearchResult.ulFiltersUsed, 20);
		if (filtersUsed != null) {

			List<WebElement> filters = commonLibrary.isExistList(filtersUsed, UIMAP_SearchResult.lstTagListItems, 10);
			// Looping through all the applied Filters to check whether given
			// filter
			// is available.
			if (filters.size() > 0) {
				for (WebElement item : filters) {
					if (item.getText().toLowerCase().contains(filterName.toLowerCase())) {
						report.updateTestLog("Verify source: " + filterName + " displays under Narrow By", "source: " + filterName + " is displayed under Narrow By", Status.PASS);
						flag = true;
					}
				}
			}
		}
		if (!flag) {
			report.updateTestLog("Verify source: " + filterName + " displays under Narrow By", "source: " + filterName + " is not displayed under Narrow By", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the doc title link with
	// contains method
	// # Function Name : clickDocLink1
	// # Author : Kalai
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Document clickDocLinkContains(String strDocTitle) {
		generalFunctions.clickDocLinkContains(strDocTitle);
		return new Document(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Continue To ReplaceFilter
	// # Function Name : clickContinueToReplaceFilter     
	// # Author : Kalai
	// # Date Created : May'15
	// #*****************************************************************************************************
	public LexisAdvanceTax clickContinueToReplaceFilter(String Text, String option) {
		WebElement popUpPage = commonLibrary.isExist(UIMAP_Home.replaceFilterPopup, 30);
		if (popUpPage != null) {
			WebElement eltPopupBoxForm = commonLibrary.isExist(popUpPage, UIMAP_Home.frmFilterAlert, 30);
			if (eltPopupBoxForm.getText().contains(Text)) {
				report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up contains the expected text " + Text, Status.PASS);
			} else {
				report.updateTestLog("Verify 'Replace Existing Filters Pop-up content'", "Replace Existing Filters Pop-up does not contain the expected text " + Text, Status.FAIL);
			}
			WebElement ulList = commonLibrary.isExist(eltPopupBoxForm, UIMAP_Home.ulList, 30);

			List<WebElement> replaceFilterOptions = commonLibrary.isExistList(ulList, UIMAP_Home.inpt, 10);
			if (replaceFilterOptions.size() > 0) {
				for (WebElement opt : replaceFilterOptions) {
					if (opt.getAttribute("value").toLowerCase().contains(option.toLowerCase())) {
						if (browsername.contains("internet"))
							commonLibrary.clickJS(opt, option);
						else
							commonLibrary.clickButtonParentWithWait(opt, option);
						break;
					}
				}
			} else {
				report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up does not contain " + option + " button", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up not displayed", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the condent Type
	// # Function Name : SwithToCondentType     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax swithToCondentType(String strCondentType) {

		Boolean flg = false;

		for (int i = 0; i < 3; i++) {

			try {
				// function call for selecting contenttype
				if (generalFunctions.selectCondentType(strCondentType) != 1) {

					// if it's not selected again calling the function
					if (generalFunctions.selectCondentType(strCondentType) == 1) {

						flg = true;
						break;

					}

				} else {

					flg = true;
					break;
				}
			}

			catch (StaleElementReferenceException e) {

			}
		}

		if (!flg)
			report.updateTestLog("Verify switch to condent type is set", "Switch to condent type is not set", Status.FAIL);

		// wait if the page loading spinner image is displayed
		if (!browsername.contains("internet")) {

			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		}
		commonLibrary.sleep(800000);

		// wait after selecting the docket content type

		if (strCondentType.toLowerCase().contains("dockets") || strCondentType.toLowerCase().contains("news")) {

			try {
				Thread.sleep(25000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link displays in a pod
	// # Function Name : verifyLinkDisplaysInPod     
	// # Author : Anbu
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLinksDisplaysInPod(String podName, String linkName) {
		String linknames[] = linkName.split(";");
		boolean flag1 = false;
		int count = 0;

		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				List<WebElement> links = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.links, 10);
				for (WebElement link : links) {
					for (int i = 0; i < linknames.length; i++) {
						if (link.getText().toLowerCase().contains(linknames[i].toLowerCase())) {
							report.updateTestLog("Verify " + linknames[i] + " link displays in pod: " + podName, linknames[i] + " link is displayed in pod: " + podName, Status.PASS);
							count++;
							flag1 = true;
							break;
						}
					}
					if (count == linknames.length)
						break;
				}

			}

		}
		// }
		if (!flag1)
			report.updateTestLog("Verify " + linkName + " link displays in pod: " + podName, linkName + " link is not displayed in pod: " + podName, Status.FAIL);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link displays in a pod
	// # Function Name : verifyLinkDisplaysInPod     
	// # Author : Baswaraj
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLinksDisplayesUnderLink(String sourceGroup, String linkName) {
		String linknames[] = linkName.split(";");
		boolean flag1 = false;
		List<WebElement> linksection = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.innerchild, 10);
		if (linksection.size() > 0) {
			for (WebElement item : linksection) {
				report.updateTestLog("Verify SourceSet Details displays under under sourceGroup : " + sourceGroup, "SourceSet Details displayed in under : " + sourceGroup, Status.PASS);
				for (int i = 0; i < linknames.length; i++) {

					List<WebElement> links = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.links, 10);
					for (WebElement link : links) {
						if (link.getText().toLowerCase().trim().contains(linknames[i].toLowerCase().trim())) {
							report.updateTestLog("Verify " + linknames[i] + " link displays under under sourceGroup : " + sourceGroup, linknames[i] + " link is displayed in under : " + sourceGroup, Status.PASS);
							flag1 = true;
							break;
						}
					}

				}
			}
			if (!flag1 || linksection == null)
				report.updateTestLog("Verify " + linkName + " links displays under sourceGroup : " + sourceGroup, linkName + " links is not displayed  under : " + sourceGroup, Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source
	// # Function Name : expandChildSource
	// # Author : Seetha
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults expandChildSourceAndClickGetAllDocuments(String podName, String subLink, String childName, String option) {
		boolean flag = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				WebElement footer = commonLibrary.isExist(pod, By.tagName("footer"), 10);
				if (footer != null) {
					WebElement button = commonLibrary.isExist(footer, By.tagName("button"), 10);
					if (button != null && button.getAttribute("data-action").contains("getmorelist")) {
						commonLibrary.clickButtonLogSmallWait(button, "More");
					}
				}
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					if (item.getText().toLowerCase().contains(subLink.toLowerCase())) {
						List<WebElement> child = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
						for (WebElement item1 : child) {
							if (item1.getAttribute("data-label").equalsIgnoreCase(childName)) {
								commonLibrary.clickButtonParentWithWait(item1, childName);
								WebElement dropmenu = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.dropmenu, 10);
								if (dropmenu != null) {
									report.updateTestLog("Verify Action menu displayes in dropdown for " + childName, option + "Action drop down is displayed", Status.PASS);
									List<WebElement> Lists = commonLibrary.isExistList(dropmenu, By.tagName("li"), 10);
									for (WebElement li : Lists) {
										if (li.isEnabled() && li.getText().toLowerCase().contains(option.toLowerCase())) {
											WebElement button = commonLibrary.isExist(li, By.tagName("button"), 10);
											if (button != null) {
												commonLibrary.clickButtonParentWithWait(button, option);
												flag = true;
												break;
											}
										}
									}
									if (flag)
										break;
								}

							}
						}
						if (flag)
							break;
					}
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is selected under source " + subLink + " inside pod " + podName, Status.PASS);
		} else {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is not selected under source " + subLink + " inside pod " + podName, Status.FAIL);
		}

		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public TOC peforrmSearchAndVerifyTOCPagedisplayed(String strSearchTerm, Boolean strClearFilter) {

		// Function calling for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		if (driver.getCurrentUrl().contains("toc")) {
			report.updateTestLog("Verify TOC Page is displayed", "TOC Page is displayed after performing search", Status.PASS);
		} else {
			report.updateTestLog("Verify TOC Page is displayed", "TOC Page is not displayed after performing search", Status.FAIL);
		}

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify label displayed above source
	// dropdown in Get Document page
	// # Function Name : verifyLabelInGetDocument
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLabelInGetDocument(String header) {
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement pageHeader = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.header, 10);
		WebElement sources = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.sourcesLabel, 10);
		if (sources != null && pageHeader != null) {
			if (pageHeader.getText().trim().toLowerCase().contains(header.toLowerCase())) {
				report.updateTestLog("Verify " + header + " label is displayed above the source dropdown", header + " label is displayed above the source dropdown", Status.PASS);
			} else {
				report.updateTestLog("Verify " + header + " label is displayed above the source dropdown", header + " label is not displayed above the source dropdown", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify " + header + " label is displayed above the source dropdown", header + " label is not displayed above the source dropdown", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option in source dropdown in
	// Get document page
	// # Function Name : selectOptionInSource
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax selectOptionInSource(String option) {
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement sourceDropdown = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.sourceSelector, 10);
		if (sourceDropdown != null) {
			commonLibrary.clickMethod(sourceDropdown, "Source dropdown");
			commonLibrary.selectFromListOption(sourceDropdown, option);
		} else {
			report.updateTestLog("Click Source dropdown", "Source dropdown is not available", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}


	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the format displayed below
	// the Citation in Get document page
	// # Function Name : verifyFormatBelowCitation
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyFormatBelowCitation(String format) {
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement rightFormat = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.rightFormatLabel, 10);
		WebElement rightFormatText = commonLibrary.isExist(rightFormat, UIMAP_LexisAdvanceTax.spanTag, 10);
		if (rightFormat != null) {
			if (rightFormat.getText().trim().toLowerCase().contains(format.toLowerCase())) {
				report.updateTestLog("Verify the '" + format + "' format is displayed below citation", "'" + format + "' format is displayed below citation", Status.PASS);
			} else {
				report.updateTestLog("Verify the '" + format + "' format is displayed below citation", "'" + format + "' format is not displayed below citation", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify the '" + format + "' format is displayed below citation", "Format is not displayed below citation", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter text in left and right
	// citation in Get document page
	// # Function Name : enterTextInCitationBox
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax enterTextInCitationBox(String leftText, String rightText) {
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement leftFormatBox = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.leftFormatBox, 10);
		WebElement rightFormatBox = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.rightformatBox, 10);
		if (leftFormatBox != null) {
			commonLibrary.setDataInTextBox(leftFormatBox, leftText, "Left citation");
		} else {
			report.updateTestLog("Enter text in left citation box", "Left citation box is not available", Status.FAIL);
		}
		if (rightFormatBox != null) {
			commonLibrary.setDataInTextBox(rightFormatBox, rightText, "Right citation");
		} else {
			report.updateTestLog("Enter text in Right citation box", "Right citation box is not available", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Get document
	// # Function Name : clickGetDocument
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTaxResults clickGetDocument() {
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement getDocument = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.getDocument, 10);
		if (getDocument != null) {
			commonLibrary.clickLinkWithWebElementWithWait(getDocument, "Get Document");
			WebElement searchResult = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.searchResult, 3);
			int count = 0;
			do {
				count++;
				searchResult = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.searchResult, 3);
				if (searchResult == null)
					commonLibrary.sleep(100000);
			} while (searchResult == null && count < 40);
		} else {
			report.updateTestLog("Click Get Document", "Get Document button is not available", Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the add to search button near
	// the source
	// # Function Name : clickAddToSearchButtonInSource     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax clickAddToSearchButtonInSource(String parentSource, String childSource, String subChildSource) {
		boolean flag = false;
		String parentHeading = null;
		String childHeading = null;
		String subChildHeading = null;
		WebElement tocList = commonLibrary.isExist(UIMAP_TOC.tocList, 10);
		if (tocList != null) {
			List<WebElement> parentList = commonLibrary.isExistList(tocList, UIMAP_TOC.docFullPath, 10);
			if (parentList.size() > 0) {
				for (int i = 0; i < parentList.size(); i++) {
					WebElement parentTitle = commonLibrary.isExist(parentList.get(i), UIMAP_TOC.headerName, 10);
					parentHeading = parentTitle.getText();
					if (parentHeading.trim().equalsIgnoreCase(parentSource.trim())) {
						if (!childSource.equals("")) {
							WebElement parentExpandCollapseButton = commonLibrary.isExist(parentList.get(i), UIMAP_TOC.expandCollapse, 10);
							if (parentExpandCollapseButton != null) {
								commonLibrary.clickButtonParentWithWait(parentExpandCollapseButton, "Expand");
								try {
									String loadProp = properties.getProperty("xSpinner");
									int count = 0;
									WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									do {
										commonLibrary.sleep(10000);
										loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										count++;
									} while (loader != null && count < 15);
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								if (parentList.get(i).getAttribute("class").equalsIgnoreCase("collapsed")) {
									commonLibrary.clickButtonParentWithWait(parentExpandCollapseButton, "Expand");
									try {
										String loadProp = properties.getProperty("xSpinner");
										int count = 0;
										WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										do {
											commonLibrary.sleep(10000);
											loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
											count++;
										} while (loader != null && count < 15);
									} catch (Exception e) {
										System.out.println(e.toString());
									}
								}
							}
							List<WebElement> childList = commonLibrary.isExistList(parentList.get(i), UIMAP_TOC.docFullPath, 10);
							if (childList.size() > 0) {
								for (int j = 0; j < childList.size(); j++) {
									WebElement childTitle = commonLibrary.isExist(childList.get(j), UIMAP_TOC.headerName, 10);
									childHeading = childTitle.getText();
									if (childHeading.trim().equalsIgnoreCase(childSource.trim())) {
										if (!subChildSource.equals("")) {
											WebElement childExpandCollapseButton = commonLibrary.isExist(childList.get(j), UIMAP_TOC.expandCollapse, 10);
											if (childExpandCollapseButton != null) {
												commonLibrary.clickButtonParentWithWait(childExpandCollapseButton, "Expand");
												try {
													String loadProp = properties.getProperty("xSpinner");
													int count = 0;
													WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
													do {
														commonLibrary.sleep(10000);
														loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														count++;
													} while (loader != null && count < 15);
												} catch (Exception e) {
													System.out.println(e.toString());
												}
												if (childList.get(j).getAttribute("class").equalsIgnoreCase("collapsed")) {
													commonLibrary.clickButtonParentWithWait(childExpandCollapseButton, "Expand");
													try {
														String loadProp = properties.getProperty("xSpinner");
														int count = 0;
														WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														do {
															commonLibrary.sleep(10000);
															loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
															count++;
														} while (loader != null && count < 15);
													} catch (Exception e) {
														System.out.println(e.toString());
													}
												}
											}
											List<WebElement> subChildList = commonLibrary.isExistList(childList.get(j), UIMAP_TOC.docFullPath, 10);
											if (subChildList.size() > 0) {
												for (int k = 0; k < subChildList.size(); k++) {
													WebElement subChildTitle = commonLibrary.isExist(subChildList.get(k), UIMAP_TOC.headerName, 10);
													subChildHeading = subChildTitle.getText();
													if (subChildHeading.trim().equalsIgnoreCase(subChildSource.trim())) {
														WebElement addToSearch = commonLibrary.isExist(subChildList.get(k), UIMAP_TOC.addToSearch, 10);

														if (addToSearch != null) {
															commonLibrary.clickButtonParentWithWait(addToSearch, "Add to search");
															try {
																String loadProp = properties.getProperty("xSpinner");
																int count = 0;
																WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
																do {
																	commonLibrary.sleep(10000);
																	loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
																	count++;
																} while (loader != null && count < 15);
															} catch (Exception e) {
																System.out.println(e.toString());
															}
															report.updateTestLog("Click the Add to search button for the source" + subChildSource, "The Add to search button for the source " + subChildSource + " is clicked", Status.PASS);
															flag = true;
															break;
														}
													}
												}
											}
										} else {
											WebElement addToSearch = commonLibrary.isExist(childList.get(j), UIMAP_TOC.addToSearch, 10);

											if (addToSearch != null) {
												commonLibrary.clickButtonParentWithWait(addToSearch, "Add to search");
												try {
													String loadProp = properties.getProperty("xSpinner");
													int count = 0;
													WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
													do {
														commonLibrary.sleep(10000);
														loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
														count++;
													} while (loader != null && count < 15);
												} catch (Exception e) {
													System.out.println(e.toString());
												}
												report.updateTestLog("Click the Add to search button for the source" + childSource, "The Add to search button for the source " + childSource + " is clicked", Status.PASS);
												flag = true;
												break;
											}
										}
									}
								}
							}
						} else {
							WebElement addToSearch = commonLibrary.isExist(parentList.get(i), UIMAP_TOC.addToSearch, 10);

							if (addToSearch != null) {
								commonLibrary.clickButtonParentWithWait(addToSearch, "Add to search");
								try {
									String loadProp = properties.getProperty("xSpinner");
									int count = 0;
									WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
									do {
										commonLibrary.sleep(10000);
										loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
										count++;
									} while (loader != null && count < 15);
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								report.updateTestLog("Click the Add to search button for the source" + parentSource, "The Add to search button for the source " + parentSource + " is clicked", Status.PASS);
								flag = true;
								break;
							}
						}
					}
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click the Add to search button for the source", "The Add to search button for the source is not clicked", Status.FAIL);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearchTOC     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public TOC simpleSearchTOC(String strSearchTerm, Boolean strClearFilter) {

		// Function calling for simple search
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);

		// Error handing if the expected page is not displayed
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);

		return new TOC(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter in narrowby section
	// # Function Name : verifyNarrowBySectionInTaxOverview
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyNarrowBySectionInTaxOverview(String strSelectedFilterOptions) {
		ArrayList<String> arrBtn = new ArrayList<String>();
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {
			WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray1, 20);
			if (filterTray != null) {
				WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter1, 20);
				List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
				for (WebElement item : filters) {
					List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
					for (WebElement btnItem : btn) {
						arrBtn.add(btnItem.getText());

					}
				}
				WebElement moreFilters = commonLibrary.isExist(filterTray, UIMAP_Home.btnMoreFilter, 20);
				if (moreFilters != null) {
					commonLibrary.clickButtonParentWithWait(moreFilters, "More");

					WebElement sectionMoreFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulMoreFilter, 20);
					List<WebElement> filters1 = commonLibrary.isExistList(sectionMoreFilter, UIMAP_Home.lstTagListItems, 10);
					for (WebElement item : filters1) {
						List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
						for (WebElement btnItem : btn) {
							arrBtn.add(btnItem.getText());

						}
					}
				}

			}
			for (int i = 0; i < arrSelectedFilterOptions.length; i++) {
				for (int j = 0; j < arrSelectedFilterOptions.length; j++) {
					if (arrBtn.get(j).contains(arrSelectedFilterOptions[i].trim())) {
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", Status.PASS);
					} else {
						report.updateTestLog("Verify " + arrSelectedFilterOptions[i] + " is displayed in NarrowBy section", arrSelectedFilterOptions[i] + " is not displayed in NarrowBy section", Status.FAIL);
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switching between Tabs
	// # Function Name : switchTabSources
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LATSourceSelection switchTabSources(String tabName) {
		boolean flag = false;
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		if (taxTabUl != null) {
			List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl, UIMAP_LexisAdvanceTax.li, 10);
			if (taxTabLiList.size() > 0) {
				for (int i = 0; i < taxTabLiList.size(); i++) {
					WebElement button = commonLibrary.isExist(taxTabLiList.get(i), UIMAP_LexisAdvanceTax.button, 10);
					if (button != null) {
						if (button.getText().toLowerCase().contains(tabName.toLowerCase())) {
							commonLibrary.clickButtonParentWithWait(button, button.getText());

							WebElement resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
							int count = 0;
							do {
								count++;
								resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
								if (resultsList == null)
									commonLibrary.sleep(5000);
							} while (resultsList == null && count <= 40);

							if (resultsList != null) {
								report.updateTestLog("Verify sources library page displays", "Sources library page is displayed", Status.PASS);
							}
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		return new LATSourceSelection(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link displays at first
	// position in pod
	// # Function Name : verifyLinkDisplaysAtFirst     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyLinkDisplaysAtFirst(String podName, String linkName) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				List<WebElement> links = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.links, 10);
				if (links.size() > 0) {
					if (links.get(1).getText().trim().toLowerCase().contains(linkName.toLowerCase())) {
						report.updateTestLog("Verify " + linkName + " link displays at first in the pod: " + podName, linkName + " link is displayed at first in the pod: " + podName, Status.PASS);
						flag1 = true;
					}
				}

			}
			if (flag1)
				break;
		}

		if (!flag1)
			report.updateTestLog("Verify " + linkName + " link displays at first in the pod: " + podName, linkName + " link is not displayed at first in the pod: " + podName, Status.FAIL);

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for searching within the results
	// # Function Name : searchWithinSearch     
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax searchWithinSearch(String sectionHeaderName, String searchTerm) {

		// Expand search within search header
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(UIMAP_SearchResult.eltCollapsedFilterHeader, 10);

		for (int i = 0; i < eltCollapsedFilterHeader.size(); i++) {

			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase().contains(sectionHeaderName.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i), sectionHeaderName);
				report.updateTestLog("Expanding Header: " + sectionHeaderName, sectionHeaderName + " Header Expanded.", Status.DONE);
			}

		}

		// Enter search term
		WebElement searchwithinResults = commonLibrary.isExist(UIMAP_SearchResult.searchWithinResults, 20);
		if (searchwithinResults != null) {
			WebElement eltSearchbox = commonLibrary.isExist(searchwithinResults, UIMAP_SearchResult.searchbox, 20);
			if (eltSearchbox != null)
				commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");
			else
				report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);
		} else
			report.updateTestLog("SearchTerm text box", "SearchTerm text box is not available", Status.FAIL);

		// Click on search button
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_SearchResult.searchWithinSearchButton, 20);
		if (eltSearchbutton != null)
			commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		else
			report.updateTestLog("Search button", "Search button is not available", Status.FAIL);

		commonLibrary.sleep(400000);

		// wait

		try {
			sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
			int count = 0;
			do {
				System.out.println("Waiting " + count);
				commonLibrary.sleep(1000000);
				sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
				count++;
			} while (sourceResultList.equals(eltSearchbutton) && count < 80);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Source
	// # Function Name : clickSourceName
	// # Author : Meera
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickOnAddToSearchBySourceName(String sourceName) {

		boolean blnFlag = false;
		WebElement sourceResultList = commonLibrary.isExistNegative(UIMAP_Sources.sourceResultList, 10);
		WebElement sourcePageRightContent = commonLibrary.isExistNegative(sourceResultList, UIMAP_Sources.sourcePageRightContent, 10);
		List<WebElement> sourceList = commonLibrary.isExistList(sourcePageRightContent, UIMAP_Sources.lstTagListItems, 10);
		for (WebElement source : sourceList) {

			if (source.getText().toLowerCase().trim().contains(sourceName.toLowerCase().trim())) {
				WebElement button = commonLibrary.isExistNegative(source, UIMAP_Sources.buttonAddToSearch, 10);
				if (button != null) {
					commonLibrary.clickButtonLogSmallWait(button, sourceName);
				}
				blnFlag = true;
				commonLibrary.sleep(60000);
				break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Add to Search for the source Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to search in federal code reporter
	// # Function Name : clickFCRLink     
	// # Author : Toya
	// # Date Created : Dec'22
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickFCRLink() {

		WebElement pod = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxReporterPod, 10);
		WebElement btn = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.federalbtn, 10);
		if (btn != null) {
			commonLibrary.clickButtonParentWithWaitJS(btn, btn.getText());

		} else {
			report.updateTestLog("click on FCR link ", "FCR link  is not clicked", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to exand t source
	// # Function Name : expandSourcename     
	// # Author : Toya
	// # Date Created : Dec'22
	// #*****************************************************************************************************************************
	public LexisAdvanceTax expandSourcename(String podName, String linkName, String subLink, int position) {

		try {

			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					if ((commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10) != null)) {
						WebElement Morelink = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10);
						WebElement Morelink1 = commonLibrary.isExist(Morelink, UIMAP_LexisAdvanceTax.button, 10);

						commonLibrary.clickLinkWithWebElementWithWaitJS(Morelink1, Morelink1.getText());

						WebElement lists = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.podlist, 10);
						List<WebElement> list1 = commonLibrary.isExistList(lists, UIMAP_LexisAdvanceTax.lists, 10);
						for (WebElement item : list1) {
							if (item.getText().toLowerCase().contains(linkName.toLowerCase())) {
								WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
								WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
								if (srcColapse != null) {
									commonLibrary.clickButtonParentWithWait(srcColapse, linkName);

									if (subLink != "") {

										List<WebElement> lists2 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
										List<String> item2 = new ArrayList<String>();
										for (WebElement search : lists2) {
											item2.add(search.getText());

											if (item2.get(position).contains(subLink)) {
												flag1 = true;
												break;
											}
										}
									}
								}
							}
							flag1 = true;
							break;

						}

					}

				}
				flag1 = true;
				break;

			}

			if (!flag1)
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is not present under pod: " + podName, Status.FAIL);
			else
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is expanded under pod: " + podName, Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to expand the source and check the
	// position of the link
	// # Function Name : expandSourcename1     
	// # Author : Toya
	// # Date Created : Dec'22
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSourcename1(String podName, String linkName, String subLink, String subheading, int position) {

		try {

			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						if (item.getText().toLowerCase().equalsIgnoreCase(linkName.toLowerCase())) {
							WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
							WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
							if (srcColapse != null) {
								commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
								Thread.sleep(5000);
								if (subLink != "") {
									List<WebElement> heading = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
									for (WebElement item1 : heading) {
										if (item1.getText().toLowerCase().equalsIgnoreCase(subheading.toLowerCase())) {
											WebElement toggleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
											WebElement srcColapse1 = commonLibrary.isExist(toggleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
											if (srcColapse != null) {
												commonLibrary.clickButtonParentWithWait(srcColapse1, subheading);
												Thread.sleep(5000);

												List<WebElement> lists2 = commonLibrary.isExistList(item1, UIMAP_LexisAdvanceTax.childList, 10);
												List<String> item2 = new ArrayList<String>();
												for (WebElement search : lists2) {
													item2.add(search.getText());
												}
												if (item2.get(position).contains(subLink))
													flag1 = true;
												break;
											}
										}

										else {
											flag1 = true;
											break;
										}

									}
								}
							}
						}
					}
				}
			}

			if (!flag1)
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is not present under pod: " + podName, Status.FAIL);
			else {
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is expanded under pod: " + podName, Status.PASS);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option in source dropdown in
	// Get document page
	// # Function Name : selectOptionInSource
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifySourcesInDropDown(String optionList) {
		String options[] = optionList.split(";");
		boolean flag = false;
		WebElement getADocumentSection = commonLibrary.isExist(UIMAP_LexisAdvanceTax.getADocumentSection, 10);
		WebElement sourceDropdown = commonLibrary.isExist(getADocumentSection, UIMAP_LexisAdvanceTax.sourceSelector, 10);
		if (sourceDropdown != null) {
			List<WebElement> optionsList1 = commonLibrary.isExistList(sourceDropdown, UIMAP_LexisAdvanceTax.options, 10);
			for (WebElement option : optionsList1) {
				for (int i = 0; i < options.length; i++) {
					if (option.getText().contains(options[i])) {
						flag = true;
					}
				}
				if (flag) {
					report.updateTestLog("Verify Item : " + option.getText() + " in the DropDown", option.getText() + " is displayed in the DropDown", Status.PASS);
				} else {
					report.updateTestLog("Verify Item : " + option.getText() + " in the DropDown", option.getText() + " is not displayed in the DropDown", Status.FAIL);
				}
			}
			// commonLibrary.clickMethod(sourceDropdown, "Source dropdown");
			// commonLibrary.selectFromListOption(sourceDropdown, option);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source by expanding the source and saving that source as a
	// favorite
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandChildSourceAndClickOption1(String podName, String subLink, String childName, String option2) {
		boolean flag = false;
		int j = 0;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					if (item.getText().toLowerCase().contains(subLink.toLowerCase())) {
						WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
						WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
						// WebElement srcExpand =
						// commonLibrary.isExist(togleBar,
						// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
						if (srcColapse != null) {
							commonLibrary.clickButtonParentWithWait(srcColapse, subLink);
							commonLibrary.sleep(10000);
							List<WebElement> child = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
							for (WebElement item1 : child) {
								if (item1.getText().toLowerCase().contains(childName.toLowerCase())) {
									commonLibrary.clickButtonParentWithWait(item1, childName);
									WebElement dropmenu = commonLibrary.isExist(UIMAP_LexisAdvanceTax.dropmenu, 10);
									if (dropmenu != null) {
										commonLibrary.sleep(5000);
										List<WebElement> buttons = commonLibrary.isExistList(dropmenu, By.tagName("button"), 10);
										commonLibrary.sleep(5000);
										for (WebElement btn : buttons) {
											if (btn.isEnabled() && btn.getText().toLowerCase().equalsIgnoreCase(option2)) {
												commonLibrary.sleep(5000);
												commonLibrary.clickButtonParentWithWait(btn, option2);
												WebElement deltePopup = commonLibrary.isExist(UIMAP_LexisAdvanceTax.deleteDialogPopUp, 10);
												WebElement btnContinue = commonLibrary.isExist(deltePopup, UIMAP_LexisAdvanceTax.btncontiune, 10);
												if (btnContinue != null) {
													commonLibrary.clickButtonParentWithWait(btnContinue, btnContinue.getText());
													flag = true;
													j = 1;
												}
											}
										}
										if (j == 0) {
											WebElement CurrentProduct = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxpracticeareaHeader, 20);
											CurrentProduct.click();
										}
										if (flag)
											break;
									}

									if (flag)
										break;
								}

							}
						}
						if (flag)
							break;
					}
					if (flag)
						break;
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.addTestLogSection(childName + " has been removed from Recent and Favorite Sources");
		} else {
			report.addTestLogSection(childName + " is not present under Recent and Favorite Sources");
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to very the source presence in recent
	// and favorite tab
	// # Function Name : verifySourceInRecentAndFavPod
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifySourceInRecentAndFavPod(String linkName) {
		WebElement pod = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.recentPod, 10);
		WebElement section = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.federalTaxPods, 10);
		boolean flag = false;
		if (section != null) {
			if ((commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morebutton, 10) != null)) {
				WebElement Morelink = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morebutton, 10);
				WebElement Morelink1 = commonLibrary.isExist(Morelink, UIMAP_LexisAdvanceTax.button, 10);
				commonLibrary.clickButton(Morelink1);
			}
			List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
			for (WebElement item : lists) {
				if (item.getText().toLowerCase().contains(linkName.toLowerCase())) {
					flag = true;
					break;
				}
			}
		}
		if (flag) {
			report.updateTestLog("Verifying Source: " + linkName + "is present under Recent and Favorite Sources ", linkName + "is present under Recent and Favorite Sources", Status.PASS);
		} else {
			report.updateTestLog("Verifying Source: " + linkName + "is not present under Recent and Favorite Sources ", linkName + "is not present under Recent and Favorite Sources", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// # Function Description : Function to delete top sources from pod
	// # Function Name : deleteResourceFromPod
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax deleteResourceFromPod(String podName) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
		if (taxPods.size() > 0) {
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					lists.remove(lists.size() - 1);
					for (WebElement item : lists) {
						if (lists.size() > 0) {
							WebElement btnDelete = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addToDelete, 10);
							if (btnDelete != null) {
								commonLibrary.clickButtonParentWithWait(btnDelete, "Delete Source");
								WebElement deltePopup = commonLibrary.isExist(UIMAP_LexisAdvanceTax.deleteDialogPopUp, 10);
								WebElement btnContinue = commonLibrary.isExist(deltePopup, UIMAP_LexisAdvanceTax.btncontiune, 10);
								if (btnContinue != null) {
									commonLibrary.clickButtonParentWithWait(btnContinue, btnContinue.getText());
									flag1 = true;
								} else {
									report.updateTestLog("Confirm pop up after delete icon is clicked display" + podName, "Confirm pop up after delete icon is clicked is not displayed " + podName, Status.FAIL);
								}
							}
						}

						if (flag1)
							break;
					}
				}
			}
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// # Function Description : Function to delete top sources from pod
	// # Function Name : deleteResourceFromPod
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax closeSourceDescpPopup() {
		WebElement Sourcedespdialogbox = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.sourceDespDialgBox, 10);
		WebElement Sourcedespdialogboxheader = commonLibrary.isExistNegative(Sourcedespdialogbox, UIMAP_LexisAdvanceTax.sourceDespDialgBoxheader, 10);
		WebElement Sourcedespdialogboxbtn = commonLibrary.isExistNegative(Sourcedespdialogboxheader, UIMAP_LexisAdvanceTax.sourceDespDialgBoxbuton, 10);
		if (Sourcedespdialogboxbtn != null) {
			commonLibrary.clickButton(Sourcedespdialogboxbtn);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source if source is already expanded
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandChildSourceAndClickOption2(String podName, String subLink, String childName, String option1) {
		generalFunctions.expandChildSourceAndClickOption2(podName, subLink, childName, option1);
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source if source is already expanded
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults expandChildSourceAndClickOption3(String podName, String subLink, String childName, String option1) {
		generalFunctions.expandChildSourceAndClickOption2(podName, subLink, childName, option1);
		return new LexisAdvanceTaxResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source by expanding the source
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public TOC expandSource2(String podName, String linkName, String subLink, String option) {
		try {

			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					for (WebElement item : lists) {
						if (item.getText().toLowerCase().equalsIgnoreCase(linkName.toLowerCase())) {
							WebElement togleBar = commonLibrary.isExist(item, By.cssSelector("div[class='togglebar']"), 10);
							WebElement srcColapse = commonLibrary.isExist(togleBar, By.tagName("button"), 10);
							if (srcColapse != null) {
								commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
								Thread.sleep(5000);
								if (subLink != "") {
									List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
									for (WebElement item2 : lists1) {
										if (item2.getText().toLowerCase().contains(subLink.toLowerCase())) {
											WebElement togleBar1 = commonLibrary.isExist(item2, By.cssSelector("div[class='togglebar']"), 10);
											WebElement srcColapse1 = commonLibrary.isExist(togleBar1, By.tagName("button"), 10);
											if (srcColapse1 != null) {
												Thread.sleep(5000);
												commonLibrary.clickButtonParentWithWait(srcColapse1, subLink);
												Thread.sleep(5000);
												if (option != "") {
													List<WebElement> lists2 = commonLibrary.isExistList(item2, UIMAP_LexisAdvanceTax.lists, 10);
													for (WebElement item3 : lists2) {
														if (item3.getText().toLowerCase().contains(option.toLowerCase())) {
															WebElement btn = commonLibrary.isExist(item3, By.tagName("button"), 10);
															commonLibrary.clickButtonParentWithWait(btn, option);
															flag1 = true;
															break;
														}
													}

												}
											}
										}
									}
								} else {
									flag1 = true;
									break;
								}

							}
						}
					}

				}
				if (flag1)
					break;
			}
			if (!flag1)
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is not present under pod: " + podName, Status.FAIL);
			else {
				report.updateTestLog("Expand" + linkName + " under pod: " + podName, linkName + " is expanded under pod: " + podName, Status.PASS);
			}
		} catch (Exception e) {

		}
		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source by expanding the source
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public TOC clickSourceNameFromPod(String podName, String sourceName) {

		boolean blnFlag = false;

		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					if (item.getText().toLowerCase().contains(sourceName.toLowerCase())) {
						WebElement button = commonLibrary.isExistNegative(item, UIMAP_Sources.button, 10);
						commonLibrary.clickButtonLogSmallWait(button, sourceName);
						blnFlag = true;
						break;
					}
				}
				if (blnFlag = true)
					break;
			}
		}
		if (!blnFlag) {
			report.updateTestLog("Click on Source: " + sourceName, "Source: " + sourceName + " is not present.", Status.FAIL);
		}

		else {
			report.updateTestLog("Click Source dropdown", "Source dropdown is not available", Status.FAIL);
		}

		return new TOC(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify tab is displayed
	// # Function Name : ClickDocLink1     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyTab(String tabName) {
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		WebElement taxTabUlLi = commonLibrary.isExist(taxTabUl, UIMAP_LexisAdvanceTax.taxTabUlli, 10);
		if (taxTabUlLi != null) {
			String strTab = taxTabUlLi.getText();
			if (strTab.trim().equalsIgnoreCase(tabName)) {
				report.updateTestLog("Verify " + tabName + " tab is displayed", tabName + " tab is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify " + tabName + " tab is displayed", tabName + " tab is not displayed", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify tab: " + tabName + " is displayed", "Tab: " + tabName + " is not present", Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify More link is displayed if
	// more than 5 sources are present in a pod
	// # Function Name : verifyMoreLinkDisplayed     
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyMoreLinkDisplayed(String podName) {
		boolean flag = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
		if (taxPods.size() > 0) {
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
					WebElement showMore = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.showMore, 10);
					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
					if (showMore != null && lists.size() >= 5) {
						report.updateTestLog("Verify More link displays if more than 5 sources displayed at the bottom of the " + podName + " pod", "More link is displayed since more than 5 sources displayed at the bottom of the " + podName + " pod", Status.PASS);
						flag = true;
					}

				}
				if (flag)
					break;
			}
		}
		if (!flag)
			report.updateTestLog("Verify More link displays if more than 5 sources displayed at the bottom of the " + podName + " pod", "More link is not displayed under " + podName + " pod", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description: Function to add to search
	// # Function Name : addtosearch     
	// # Author : Toya
	// # Date Created : jan'16
	// #*****************************************************************************************************************************
	public LexisAdvanceTax addTosearch(String podName, String[] linkName) {
		try {
			boolean flag1 = false;
			List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
			for (WebElement pod : taxPods) {
				WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
				if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

					if ((commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10) != null)) {
						WebElement Morelink = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10);
						WebElement Morelink1 = commonLibrary.isExist(Morelink, UIMAP_LexisAdvanceTax.button, 10);

						commonLibrary.clickLinkWithWebElementWithWaitJS(Morelink1, Morelink1.getText());

						WebElement lists = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.ul, 10);
						if (lists != null) {
							List<WebElement> list1 = commonLibrary.isExistList(lists, UIMAP_LexisAdvanceTax.lists, 10);
							int t = 0;
							for (int p = 0; p < list1.size(); p++) {

								if (linkName[t].equalsIgnoreCase((list1.get(p).getText().toLowerCase()))) {
									int a = linkName[t].indexOf(list1.get(p).getText());
									WebElement item = list1.get(p);
									WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
									if (btnAdd != null) {
										commonLibrary.clickButtonParentWithWait(btnAdd, "add to search");
										report.updateTestLog("Add to Search button under :" + linkName[a], "Add to Search button is  present under :" + linkName[a], Status.DONE);
										t++;
										if ((linkName.length) == t) {
											flag1 = true;
											break;
										}

									} else {
										report.updateTestLog("Add to Search button under :" + linkName, "Add to Search button is not present under :" + linkName, Status.FAIL);

									}
								}

							}

							if (!flag1) {
								report.updateTestLog(linkName + " in:" + podName, linkName + "is not present in:" + podName, Status.FAIL);
							}
						}
						if (!flag1) {
							report.updateTestLog("sources under" + podName, "sources not present in:" + podName, Status.FAIL);
						}
					}
					if (!flag1) {
						report.updateTestLog("morelink in" + podName, "morelink not present in:" + podName, Status.FAIL);
					}
				}
				if (flag1)
					break;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switching between Tabs
	// # Function Name : switchTabSources
	// # Author : Anbu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public LexisAdvanceTax switchTabSources1(String tabName) {
		boolean flag = false;
		WebElement taxTab = commonLibrary.isExist(UIMAP_LexisAdvanceTax.taxTabHeader, 10);
		WebElement taxTabUl = commonLibrary.isExist(taxTab, UIMAP_LexisAdvanceTax.ul, 10);
		if (taxTabUl != null) {
			List<WebElement> taxTabLiList = commonLibrary.isExistList(taxTabUl, UIMAP_LexisAdvanceTax.li, 10);
			if (taxTabLiList.size() > 0) {
				for (int i = 0; i < taxTabLiList.size(); i++) {
					WebElement button = commonLibrary.isExist(taxTabLiList.get(i), UIMAP_LexisAdvanceTax.button, 10);
					if (button != null) {
						if (button.getText().toLowerCase().contains(tabName.toLowerCase())) {
							commonLibrary.clickButtonParentWithWait(button, button.getText());

							WebElement resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
							int count = 0;
							do {
								count++;
								resultsList = commonLibrary.isExistNegative(UIMAP_LATSourceSelection.resultList, 3);
								if (resultsList == null)
									commonLibrary.sleep(5000);
							} while (resultsList == null && count <= 40);

							if (resultsList != null) {
								report.updateTestLog("Verify sources library page displays", "Sources library page is displayed", Status.PASS);
							}
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
			}
		}
		if (!flag)
			report.updateTestLog("Click tab: " + tabName, "Tab: " + tabName + " is not present", Status.FAIL);
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify search component in federal
	// page
	// # Function Name : verifyFederalsearch     
	// # Author : Toya
	// # Date Created : Dec'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyFederalsearch(String search) {
		if (driver.getCurrentUrl().contains(search)) {
			report.updateTestLog("Verify search Component page is displayed", "Verify search Component page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify search Component page is not displayed", "Verify search Component page is not displayed", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify FCR page
	// # Function Name : verifyFCRpage     
	// # Author : Toya
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyFCRpage(String[] sources) {
		WebElement header = commonLibrary.isExist(UIMAP_LexisAdvanceTax.fcrheader, 10);
		WebElement FCRheader = commonLibrary.isExist(header, UIMAP_LexisAdvanceTax.tag, 10);
		if (FCRheader != null) {

			if (FCRheader.getText().equalsIgnoreCase("Federal Code Reporter")) {
				WebElement FCRpods = commonLibrary.isExist(UIMAP_LexisAdvanceTax.FCRpods, 10);
				List<WebElement> item = commonLibrary.isExistList(FCRpods, UIMAP_LexisAdvanceTax.sourceCollapsed1, 10);
				for (int i = 0; i < item.size(); i++) {

					if (item.get(i).getText().equalsIgnoreCase(sources[i])) {
						report.updateTestLog("Verify:" + (item.get(i).getText()) + "source is displayed in FCR Page", (item.get(i).getText()) + "source is displayed in FCR Page", Status.PASS);

					} else {
						report.updateTestLog("Verify:" + (item.get(i).getText()) + "source is displayed in FCR Page", (item.get(i).getText()) + "source is not displayed in FCR Page", Status.FAIL);
					}
				}
			} else {
				report.updateTestLog("Verify if FCR page is  displayed", "Verify FCR page is not displayed", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify FCR page", "FCR header is not Displayed", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add to search in FCR page
	// # Function Name : addtosearchFCR   
	// # Author : Toya
	// # Date Created : Jan'16
	// #**********************************************************************************************************
	public LexisAdvanceTax addtosearchFCR(String source1) {
		WebElement FCRpods = commonLibrary.isExist(UIMAP_LexisAdvanceTax.FCRpods, 10);
		List<WebElement> item = commonLibrary.isExistList(FCRpods, UIMAP_LexisAdvanceTax.li, 10);
		if (item != null) {
			for (WebElement pod : item) {
				if (pod.getText().equalsIgnoreCase(source1.toLowerCase())) {
					WebElement button = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.addtoSearch, 10);
					commonLibrary.clickButtonParentWithWait(button, "add to search");
					break;

				}
			}
		} else {
			report.updateTestLog(source1 + "is not present in FCR page", source1 + "is not present in FCR page", Status.FAIL);

		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filters in FCR page
	// # Function Name : verifyNarrowbyFilterFCR   
	// # Author : Toya
	// # Date Created : Jan'16
	// #**********************************************************************************************************

	public LexisAdvanceTax verifyNarrowbyFilterFCR(String strFilter) {
		WebElement searchfilter = commonLibrary.isExist(UIMAP_Document.searchfilter, 20);
		if (searchfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section", " Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab", "Narrow By Section does not contains " + strFilter, Status.FAIL);
		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify replace pop up message
	// # Function Name : verifyReplacepopupmesg   
	// # Author : Toya
	// # Date Created : Jan'16
	// #**********************************************************************************************************

	public LexisAdvanceTax verifyReplacepopupmesg() {

		// WebElement replacemsg = commonLibrary.isExist(
		// UIMAP_LexisAdvanceTax.replacepop, 10);
		WebElement msgbox = commonLibrary.isExist(UIMAP_LexisAdvanceTax.msgbox, 10);
		WebElement msg = commonLibrary.isExist(msgbox, UIMAP_LexisAdvanceTax.msg, 10);
		WebElement footer = commonLibrary.isExist(msgbox, UIMAP_LexisAdvanceTax.morebutton, 10);

		String message = msg.getText().replace("\n", "");
		String display = "If you continue and select \"Accountants' Handbook\" as a filter, you will replace the existing filter \"Accounting: In General\".What would you like to do?Note: You cannot add a parent source as a filter if you've already selected one of its child sources. Additionally, you cannot add multiple sources from your Favorites list as filters.";
		if (message.equalsIgnoreCase(display)) {
			report.updateTestLog("Verify Replace Pop Up Message:", "Replace Pop Up Message is Displayed", Status.PASS);
			List<WebElement> buttons = commonLibrary.isExistList(footer, UIMAP_LexisAdvanceTax.btn, 10);
			for (WebElement item : buttons) {
				if (item.getAttribute("data-action").contains("confirm")) {
					commonLibrary.clickButtonParentWithWait(item, "Continue");
					report.updateTestLog("Click on continue button in Pop up message:", "Clicked on continue button in Pop up message", Status.PASS);
				}

			}
		} else {
			report.updateTestLog("Verify Replace Pop Up Message:", "Replace Pop Up Message is not Displayed", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add to source for source
	// # Function Name : clickOnAddtoSource     
	// # Author : Jubin
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSource(String sourcename) {
		try {
			boolean flag1 = false;
			// boolean flag2 = false;
			WebElement podList = commonLibrary.isExist(UIMAP_LATSourceSelection.pods, 10);
			WebElement section = commonLibrary.isExistNegative(podList, UIMAP_LATSourceSelection.ul, 10);
			if (section != null) {
				List<WebElement> lists = commonLibrary.isExistList(section, UIMAP_LATSourceSelection.li, 10);
				for (WebElement item : lists) {
					WebElement div3 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div3, 10);
					WebElement div1 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div1, 10);
					WebElement header = commonLibrary.isExist(div1, UIMAP_LATSourceSelection.button, 10);
					// WebElement div2 = commonLibrary.isExist(item,
					// UIMAP_LATSourceSelection.div2, 10);
					if (header != null && header.getText().toLowerCase().trim().contains(sourcename.toLowerCase().trim())) {
						// WebElement btnAdd = commonLibrary.isExist(div2,
						// UIMAP_LATSourceSelection.addtoSearch, 10);
						WebElement expand = commonLibrary.isExist(div3, UIMAP_LATSourceSelection.button, 10);
						commonLibrary.sleep(10000);
						if (expand != null) {
							commonLibrary.clickButtonParentWithWait(expand, "add to search for source: " + sourcename);
							flag1 = true;
							break;
						}
					}
				}
			}
			if (flag1)
				report.updateTestLog("Select the source to expand " + sourcename + "", "Select the source to expand" + sourcename + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select the source to expand " + sourcename + "", "Select the source to expand" + sourcename + " is clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add to source for source
	// # Function Name : clickOnAddtoSource     
	// # Author : Jubin
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax addtosource(String sourcename, String childname) {
		try {
			boolean flag1 = false;
			WebElement podList = commonLibrary.isExist(UIMAP_LATSourceSelection.pods, 10);
			WebElement section = commonLibrary.isExistNegative(podList, UIMAP_LATSourceSelection.ul, 10);
			if (section != null) {
				List<WebElement> lists = commonLibrary.isExistList(section, UIMAP_LATSourceSelection.li, 10);
				for (WebElement item : lists) {
					WebElement div1 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div1, 10);
					WebElement header = commonLibrary.isExist(div1, UIMAP_LATSourceSelection.button, 10);
					System.out.println("headers: " + header.getText());
					if (header != null && header.getText().toLowerCase().trim().contains(sourcename.toLowerCase().trim())) {
						// WebElement btnAdd = commonLibrary.isExist(div2,
						// UIMAP_LATSourceSelection.addtoSearch, 10);
						// WebElement div3 = commonLibrary.isExist(item,
						// UIMAP_LATSourceSelection.div3, 10);
						WebElement expand = commonLibrary.isExist(item, UIMAP_LATSourceSelection.button, 10);
						WebElement expand1 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.expanded, 10);

						if (expand != null && expand1 != null) {
							WebElement div5 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div5, 10);
							WebElement section2 = commonLibrary.isExistNegative(div5, UIMAP_LATSourceSelection.ul, 10);
							List<WebElement> list = commonLibrary.isExistList(section2, UIMAP_LATSourceSelection.li, 10);
							for (WebElement items : list) {
								WebElement div11 = commonLibrary.isExist(items, UIMAP_LATSourceSelection.div1, 10);
								if (div11 != null) {
									WebElement header1 = commonLibrary.isExist(div11, UIMAP_LATSourceSelection.button, 10);
									WebElement div2 = commonLibrary.isExist(items, UIMAP_LATSourceSelection.div2, 10);
									WebElement btnAdd = commonLibrary.isExist(div2, UIMAP_LATSourceSelection.addtoSearch, 10);
									if (header1 != null && header1.getText().toLowerCase().trim().contains(childname.toLowerCase().trim())) {
										commonLibrary.clickButtonParentWithWait(btnAdd, "add to search for source: " + sourcename);
										flag1 = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			if (flag1)
				report.updateTestLog("Select add to search icon from source " + sourcename + "", "add to search icon for " + childname + "from source " + sourcename + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add to search icon near source " + sourcename + "", "add to search icon for " + childname + "from source " + sourcename + " has not been clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on add to source for source
	// # Function Name : clic kOnAddtoSource     
	// # Author : Jubin
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTax clickOnAddtoSource(String sourcename) {
		try {
			boolean flag1 = false;
			WebElement podList = commonLibrary.isExist(UIMAP_LATSourceSelection.pods, 10);
			WebElement section = commonLibrary.isExistNegative(podList, UIMAP_LATSourceSelection.ul, 10);
			System.out.println("1");
			if (section != null) {
				List<WebElement> lists = commonLibrary.isExistList(section, UIMAP_LATSourceSelection.li, 10);
				for (WebElement item : lists) {
					WebElement div1 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div1, 10);
					WebElement header = commonLibrary.isExist(div1, UIMAP_LATSourceSelection.button, 10);
					WebElement div2 = commonLibrary.isExist(item, UIMAP_LATSourceSelection.div2, 10);

					if (header != null && header.getText().toLowerCase().trim().contains(sourcename.toLowerCase().trim())) {
						WebElement btnAdd = commonLibrary.isExist(div2, UIMAP_LATSourceSelection.addtoSearch, 10);
						if (btnAdd != null) {
							commonLibrary.clickButtonParentWithWait(btnAdd, "add to search for source: " + sourcename);
							flag1 = true;
							break;
						}
					}
				}
				// }
			}
			if (flag1)
				report.updateTestLog("Select add to search icon near source " + sourcename + "", "add to search icon near source " + sourcename + " is clicked", Status.PASS);
			else {
				report.updateTestLog("Select add to search icon near source " + sourcename + "", "add to search icon near source " + sourcename + " is not clicked", Status.FAIL);
			}
		} catch (Exception e) {

		}
		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on more link
	// # Function Name : clic clickmorelink     
	// # Author : Toya
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************
	public LexisAdvanceTax clickmorelink(String podName) {

		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {

				if ((commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10) != null)) {
					WebElement Morelink = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10);
					WebElement Morelink1 = commonLibrary.isExist(Morelink, UIMAP_LexisAdvanceTax.button, 10);

					commonLibrary.clickLinkWithWebElementWithWaitJS(Morelink1, Morelink1.getText());
					break;
				}
			}
		}
		return new LexisAdvanceTax(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verigy content displayed in ICT
	// particular section
	// # Function Name : verifyContentinICT     
	// # Author : Toya
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults verifyContentinICT(String content) {

		WebElement searchControl = commonLibrary.isExist(UIMAP_SearchResult.searchControl, 20);

		WebElement ict = commonLibrary.isExist(searchControl, UIMAP_SearchResult.searchControl, 20);
		if (ict.getText().contains(content)) {
			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP", "ICTs displayed in category navigation pane that appears in LHP", Status.PASS);
		} else {

			report.updateTestLog("Verify ICTs display in category navigation pane that appears in LHP", "ICTs not displayed in category navigation pane that appears in LHP", Status.FAIL);

		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for perform Search in Tax page.
	// # Function Name : TaxSearchSpecial
	// # Author : Banu
	// # Date Created : Jun 23
	// #*****************************************************************************************************************************

	public Search taxSearchSpecial(String strSearchTerm) {
		Boolean blnFlag = false;
		try {

			WebElement txtIdTaxSearch = commonLibrary.isExist(UIMAP_Home.txtIdtaxlSearch, 20);
			WebElement btnIdTaxSearch = commonLibrary.isExist(UIMAP_Home.btnIdtaxSearch, 20);
			if (txtIdTaxSearch != null && btnIdTaxSearch != null) {
				commonLibrary.setDataInTextBox(txtIdTaxSearch, strSearchTerm, "SearchTerm");
				commonLibrary.clickButtonParentWithWait(btnIdTaxSearch, "Search");
				commonLibrary.sleep(Mwait);
			}

			WebElement btnIdSearchResult = commonLibrary.isExist(UIMAP_SearchResult.errorResult, 20);
			if (btnIdSearchResult != null)
				blnFlag = true;
		} catch (Exception e) {
			blnFlag = false;

		}
		if (blnFlag) {
			report.updateTestLog("Verify Error Message is Displayed", "We are unable to process the search term you entered: ' " + strSearchTerm + " Lexis Advance® does not search for single special characters or symbols, such as  # $ % § = @ ^ : -  /.&'", Status.PASS);
		} else {

			report.updateTestLog("Verify Error Message is Displayed", "Search Results are not displayed for the SearchTerm '" + strSearchTerm + "'", Status.FAIL);

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source by expanding the source and saving that source as a
	// favorite
	// # Function Name : expandChildSource
	// # Author : Jubin
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults expandChildSourceAndClickOption4(String podName, String subLink, String childName, String option) {
		boolean flag = false;

		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					if (item.getText().toLowerCase().contains(subLink.toLowerCase())) {
						WebElement togleBar = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.toggleBar, 10);
						WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
						// WebElement srcExpand =
						// commonLibrary.isExist(togleBar,
						// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
						if (srcColapse != null) {
							commonLibrary.clickButtonParentWithWait(srcColapse, subLink);
							commonLibrary.sleep(5000);
							List<WebElement> child = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
							for (WebElement item1 : child) {
								if (item1.getText().toLowerCase().contains(childName.toLowerCase())) {
									commonLibrary.clickButtonParentWithWait(item1, childName);
									// commonLibrary.sleep(5000);
									WebElement dropmenu = commonLibrary.isExist(UIMAP_LexisAdvanceTax.dropmenu, 10);
									if (dropmenu != null) {
										// commonLibrary.sleep(5000);
										List<WebElement> buttons = commonLibrary.isExistList(dropmenu, By.tagName("button"), 10);
										// commonLibrary.sleep(5000);
										for (WebElement btn : buttons) {
											if (btn.isEnabled() && btn.getText().toLowerCase().equalsIgnoreCase(option)) {
												commonLibrary.sleep(5000);
												commonLibrary.clickButtonParentWithWait(btn, option);
												flag = true;
												break;

											}
											if (flag)
												break;
										}

									}
								}
								if (flag)
									break;
							}
						}
					}

					if (flag)
						break;
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is selected under source " + subLink + " inside pod " + podName, Status.PASS);
		} else {
			report.updateTestLog("Select " + option + " under source " + subLink + " inside pod " + podName + "", option + " is not selected under source " + subLink + " inside pod " + podName, Status.FAIL);
		}
		return new LexisAdvanceTaxResults(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Name
	// # Function Name : verifyPodExists
	// # Author : Bharath VMP
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyPodExits(String podName, boolean value) {
		boolean flag1 = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.federalTaxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().trim().toLowerCase().contains(podName.trim().toLowerCase())) {

				flag1 = true;
				break;
			}
		}

		if (value) {

			if (!flag1)
				report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is not displayed", Status.FAIL);
			else
				report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is  displayed", Status.PASS);
		} else {
			if (!flag1)
				report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is not displayed", Status.PASS);
			else
				report.updateTestLog("Verify " + podName + "  Component Page is displayed", podName + " Page is  displayed", Status.FAIL);
		}

		return new LexisAdvanceTax(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if the pod is expanded and
	// present sources
	// # Function Name : verifyExpandedPod
	// # Author : Toya
	// # Date Created : Jan '16
	// #*****************************************************************************************************************************
	public LexisAdvanceTax verifyExpandedPod(String podName, String Sources) {
		boolean flag = false;
		String[] Sources1 = Sources.split(";");
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				WebElement footer = commonLibrary.isExist(pod, UIMAP_LexisAdvanceTax.morelink, 10);
				WebElement lesslink = commonLibrary.isExist(footer, UIMAP_LexisAdvanceTax.lesslink, 10);
				if (lesslink != null) {
					List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.TPG, 10);
					int i = 0;
					for (WebElement item : lists) {
						if (item.getText().contains(Sources1[i])) {
							i++;
							report.updateTestLog("Source" + item.getText() + "is present:", "under" + podName + "pod", Status.PASS);
						} else {
							report.updateTestLog("Source" + item.getText() + "is  not present:", "under" + podName + "pod", Status.FAIL);
						}

					}
					flag = true;
				}
			}
			if (flag) {
				break;
			}
		}
		return new LexisAdvanceTax(scriptHelper);
	}


	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting child item of a
	// particular source and click Get All Document
	// # Function Name : expandChildSourceandGetAllDocument
	// # Author : Senthil
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTaxResults expandChildSourceAndClickGetAllDocuments1(String podName, String childName, String option) {
		boolean flag = false;
		List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
		for (WebElement pod : taxPods) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
			if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
				WebElement footer = commonLibrary.isExist(pod,By.tagName("footer"),10);
				if(footer!=null)
				{
					WebElement button =commonLibrary.isExist(footer,By.tagName("button"),10);
					if(button!=null && button.getAttribute("data-action").contains("getmorelist"))
					{
						commonLibrary.clickButtonLogSmallWait(button, "More");
					}
				}
				List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
				for (WebElement item : lists) {
					//if (item.getText().toLowerCase().contains(subLink.toLowerCase())) {
						List<WebElement> child = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.childList, 10);
						for (WebElement item1 : child) {
							if (item1.getAttribute("data-label").equalsIgnoreCase(childName)) {
								commonLibrary.clickButtonParentWithWait(item1, childName);
								WebElement dropmenu = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.dropmenu, 10);
								if (dropmenu != null) {
									report.updateTestLog("Verify Action menu displayes in dropdown for "+childName, option + "Action drop down is displayed", Status.PASS);
									List<WebElement> Lists = commonLibrary.isExistList(dropmenu, By.tagName("li"), 10);
									for (WebElement li : Lists) {
										if (li.isEnabled() && li.getText().toLowerCase().contains(option.toLowerCase())) {
											WebElement button = commonLibrary.isExist(li, By.tagName("button"), 10);
											if(button!=null)
											{
												commonLibrary.clickButtonParentWithWait(button, option);
												flag = true;
												break;
											}
										}
									}
									if (flag)
										break;
								}

							}
						}
						if (flag)
							break;
					//}
				}
			}
			if (flag)
				break;
		}
		if (flag) {
			report.updateTestLog("Select " + option + " under source " + childName + " inside pod " + podName + "", option + " is selected under source " + childName  + " inside pod " + podName, Status.PASS);
		} else {
			report.updateTestLog("Select " + option + " under source " + childName  + " inside pod " + podName + "", option + " is not selected under source " + childName  + " inside pod " + podName, Status.FAIL);
		}

		return new LexisAdvanceTaxResults(scriptHelper);
	}

	
	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Home Link
	// # Function Name : clickHome
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
		
	public LexisAdvanceTax clickHomeTax()
	{
		WebElement homeLink = commonLibrary.isExistNegative(UIMAP_LexisAdvanceTax.homeTax, 20);
		
		WebElement currProdOld = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 10);
		commonLibrary.clickButtonParentWithWait(homeLink, "Home");
		try
		{
			WebElement currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			int counter =0;
			do
			{
				commonLibrary.sleep(50000);
				counter++;
				currProdNew = commonLibrary.isExistNegative(UIMAP_WorkFolders.currProd, 5);
			}while(currProdOld.equals(currProdNew) && counter <40);
		}
		catch(Exception e1)
		{
			commonLibrary.sleep(500000);
			System.out.println(e1.toString());
		}
		commonLibrary.sleep(500000);
		return new LexisAdvanceTax(scriptHelper);
	}

	
	// #*****************************************************************************************************************************
	// # Function Description : Function to navigate to Settings page
	// # Function Name : navigateToSettings
	// # Author : Uma
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public LASettings navigateToSettings() {
		generalFunctions.navigateToSettings();
		return new LASettings(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function is used to verify the filter count in Pre-Filt dropdown
	// # Function Name : verifyFilterCountInPreFilt
	// # Author : Aravind Russell V
	// # Date Created : Jan '16
	// #*****************************************************************************************************************************
	
	public LexisAdvanceTax verifyFilterCountInPreFilt(String filtCountas) {
		
		WebElement filtDropDownButton = commonLibrary.isExist(UIMAP_LexisAdvanceTax.filtDropDownButton, 10);
		WebElement filtCount1 = commonLibrary.isExist(filtDropDownButton, UIMAP_LexisAdvanceTax.filtCount, 10);
		
		if(filtCount1 != null && filtCount1.getText().contains(filtCountas))
		{
			report.updateTestLog("Verify filter Count as" + filtCountas + "in Pre filter dropdown", "filter Count displayed as" + filtCountas + "in Pre filter dropdown", Status.PASS);
		}
		else
		{
			report.updateTestLog("Verify filter Count as" + filtCountas + "in Pre filter dropdown", "filter Count not displayed as" + filtCountas + "in Pre filter dropdown", Status.FAIL);
		}
		
		return new LexisAdvanceTax(scriptHelper);
		
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function for expanding source under pods and add
	// source filter.
	// # Function Name : expandSourceAndAddToSearch2
	// # Author : Aravind Russell V
	// # Date Created : Feb'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTax expandSourceAndAddToSearch2(String podName, String linkName, String addtoSearch) {
		if(podName.equals(""))
		{
			report.updateTestLog("Select add all to search step has been ignored", "Add all to search has been ignored", Status.DONE);
		}
		else
		{
			try {
				boolean flag1 = false;

				List<WebElement> taxPods = commonLibrary.isExistList(UIMAP_LexisAdvanceTax.taxPods, 10);
				if (taxPods.size() > 0) {
					for (WebElement pod : taxPods) {
						WebElement header = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.taxPodHeader, 10);
						if (header.getText().toLowerCase().contains(podName.toLowerCase())) {
							WebElement showmorebutton = commonLibrary.isExistNegative(pod, UIMAP_LexisAdvanceTax.showmorebutton, 10);
							if (showmorebutton != null) {
								commonLibrary.clickButton(showmorebutton);
							}
							List<WebElement> lists = commonLibrary.isExistList(pod, UIMAP_LexisAdvanceTax.lists, 10);
							for (WebElement item : lists) {
								if (item.getText().toLowerCase().contains(linkName.toLowerCase())) {
									if (addtoSearch != null && !addtoSearch.equals("")) {
										WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
										WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
										if (srcColapse != null) {
											commonLibrary.clickButtonParentWithWaitJS(srcColapse, linkName);
											// Thread.sleep(5000);
											try {
												srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
												WebElement srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
												int count = 0;
												do {
													//commonLibrary.sleep(1000000);
													srcColapseNew = commonLibrary.isExistNegative(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 5);
													count++;
												} while (srcColapse.equals(srcColapseNew) && count < 50);
											} catch (Exception e) {
												//commonLibrary.sleep(5000000);
												System.out.println(e.toString());
											}

											//commonLibrary.sleep(100000);
										}
										List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
										try {
											int count = 0;
											do {
												//commonLibrary.sleep(10000000);
												count++;
												lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
											} while (lists1.size() == 0 && count < 60);
										} catch (Exception e) {
											lists1 = commonLibrary.isExistList(item, UIMAP_LexisAdvanceTax.lists, 10);
											//commonLibrary.sleep(5000000);
											System.out.println(e.toString());
										}

										if (lists1.size() > 0) {
											report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + " displays", Status.PASS);
											for (WebElement addtosrch : lists1) {
												if (addtosrch.getText().toLowerCase().contains(addtoSearch.toLowerCase())) {
													WebElement btnAdd = commonLibrary.isExist(addtosrch, UIMAP_LexisAdvanceTax.addtoSearch, 10);
													if (btnAdd != null) {
														commonLibrary.clickButtonParentWithWaitJS(btnAdd, "add to search");
														try {
															String loadProp = properties.getProperty("xSpinner");
															int count = 0;
															WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
															do {
																commonLibrary.sleep(10000);
																loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
																count++;
															} while (loader != null && count < 15);
														} catch (Exception e) {
															System.out.println(e.toString());
														}
														//commonLibrary.sleep(100000);
														report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is clicked", Status.PASS);
														flag1 = true;
														break;
													}
												}
											}
										} else
											report.updateTestLog("Verify Source set details for  " + linkName + " displays", "Source set details for  " + linkName + "does not display", Status.FAIL);
										if (flag1)
											break;

									} else {
										WebElement btnAdd = commonLibrary.isExist(item, UIMAP_LexisAdvanceTax.addtoSearch, 10);
										if (btnAdd != null) {

											commonLibrary.clickButtonParentWithWaitJS(btnAdd, "add to search");
											try {
												String loadProp = properties.getProperty("xSpinner");
												int count = 0;
												WebElement loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
												do {
													commonLibrary.sleep(10000);
													loader = commonLibrary.isExistNegative(By.xpath(loadProp), 5);
													count++;
												} while (loader != null && count < 15);
											} catch (Exception e) {
												System.out.println(e.toString());
											}
											flag1 = true;
											report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is clicked", Status.PASS);
											break;
										}
									}

								}
							}

						}

						if (flag1)
							break;
					}
				}
				if (!flag1) {
					if (addtoSearch != null && !addtoSearch.equals("")) {
						report.updateTestLog("Select add all to search icon near source " + addtoSearch + "", "Add all to search icon near source " + addtoSearch + " is not clicked", Status.FAIL);
					} else
						report.updateTestLog("Select add all to search icon near source " + linkName + "", "Add all to search icon near source " + linkName + " is not clicked", Status.FAIL);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		
		
		return new LexisAdvanceTax(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter in narrowby section
	// # Function Name : verifyNarrowBySection
	// # Author : Aravind Russell V
	// # Date Created : Feb'16
	// #*****************************************************************************************************************************

	public LexisAdvanceTax verifyNarrowBySection1(String strSelectedFilterOptions) {
		if(strSelectedFilterOptions.equals(""))
		{
			report.updateTestLog("Narrow by section has been ignored", "Narrow by section has been ignored", Status.DONE);
		}
		else
		{
			ArrayList<String> arrBtn = new ArrayList<String>();
			String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
			try {
				WebElement filterTray = commonLibrary.isExist(UIMAP_Home.filterTray, 20);
				if (filterTray != null) {
					WebElement ulFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulFilter, 20);
					List<WebElement> filters = commonLibrary.isExistList(ulFilter, UIMAP_Home.lstTagListItems, 10);
					for (WebElement item : filters) {
						List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
						for (WebElement btnItem : btn) {
							arrBtn.add(btnItem.getText());
						}
					}
					WebElement moreFilters = commonLibrary.isExist(filterTray, UIMAP_Home.btnMoreFilter, 20);
					if (moreFilters != null) {
						commonLibrary.clickButtonParentWithWait(moreFilters, "More");

						WebElement sectionMoreFilter = commonLibrary.isExist(filterTray, UIMAP_Home.ulMoreFilter, 20);
						List<WebElement> filters1 = commonLibrary.isExistList(sectionMoreFilter, UIMAP_Home.lstTagListItems, 10);
						for (WebElement item : filters1) {
							List<WebElement> btn = commonLibrary.isExistList(item, UIMAP_Home.button, 20);
							for (WebElement btnItem : btn) {
								arrBtn.add(btnItem.getText());

							}
						}
					}

				}
				boolean flag = false;
				for (String listValues : arrBtn) {
					for (String lists : arrSelectedFilterOptions) {
						if (listValues.contains(lists.trim())) {
							flag = true;
							break;
						}
					}
					if (flag)
						break;
				}
				if (flag) {
					report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy section", strSelectedFilterOptions + " is displayed in NarrowBy section " + arrBtn, Status.PASS);
				} else {
					report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy section", strSelectedFilterOptions + " is not displayed in NarrowBy section, actual was " + arrBtn, Status.FAIL);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());
			}
		}
		
		return new LexisAdvanceTax(scriptHelper);
	}


}
