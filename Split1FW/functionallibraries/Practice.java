package functionallibraries;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.cognizant.framework.Settings;
import UIMAP.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.*;

public class Practice extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	Properties localizeProperties = Settings.getInstance();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;

	public Practice(ScriptHelper scriptHelper) {
		super(scriptHelper);

		if (!driver.getCurrentUrl().contains("practice") && !driver.getCurrentUrl().contains("practice")) {
			throw new IllegalStateException("Practice page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Click a Link in Practice Area page
	// # Function Name : navigateTOBrowseLinksAndVerify
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice navigateTOBrowseLinksAndVerify(String strParentMenuName, String strFirstSubMenuName, String strSecondSubMenuName) {
		try {
			commonLibrary.sleep(100);
			Boolean blnFirst = false, blnSecond = false, blnThird = false;
			WebElement btnIdBrowse = commonLibrary.isExist(UIMAP_Home.btnIdBrowse, 20);
			commonLibrary.clickButtonParentWithWait(btnIdBrowse, "Browse");
			commonLibrary.sleep(Mwait);
			commonLibrary.sleep(20);
			WebElement divIdBrowserMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserMenu, 20);
			if (divIdBrowserMenu != null) {
				WebElement lstTagAside = commonLibrary.isExist(divIdBrowserMenu, UIMAP_Home.lstTagAside, 20);
				List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside, UIMAP_Home.lstTagListItems, 20);
				if (lstTagListItems.size() > 0)
					for (WebElement button : lstTagListItems) {
						if (button.getText().contains(strParentMenuName)) {
							WebElement item = commonLibrary.isExist(button, UIMAP_Home.item, 10);

							blnFirst = true;
							if (browsername.contains("internet"))
								// commonLibrary.click_JS(button,strParentMenuName);
								commonLibrary.clickButtonParentWithWait(item, strParentMenuName);
							else
								// commonLibrary.clickButton_Parent_WithWait(button,
								// strParentMenuName);
								commonLibrary.clickButtonParentWithWait(item, strParentMenuName);
							break;

						}

					}
				if (!blnFirst)
					report.updateTestLog("Click on " + strParentMenuName, "Not Clicked on " + strParentMenuName, Status.FAIL);
			}
			if (strFirstSubMenuName != "") {
				commonLibrary.sleep(20);
				WebElement divIdBrowserSubMenu = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(0), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strFirstSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.btnTypeButton, 20);
								blnSecond = true;
								if (browsername.contains("internet"))
									commonLibrary.clickButtonParentWithWait(button, strFirstSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button, strFirstSubMenuName);
								break;
							}

						}
					if (!blnSecond)
						report.updateTestLog("Click on " + strFirstSubMenuName, "Not Clicked on " + strFirstSubMenuName, Status.FAIL);
				}
			}
			if (strSecondSubMenuName != "") {
				commonLibrary.sleep(20);
				WebElement divIdBrowserSubMenu1 = commonLibrary.isExist(UIMAP_Home.divIdBrowserSubMenu, 20);
				if (divIdBrowserSubMenu1 != null) {
					List<WebElement> lstTagAside = commonLibrary.isExistList(divIdBrowserSubMenu1, UIMAP_Home.lstTagAside, 20);
					List<WebElement> lstTagListItems = commonLibrary.isExistList(lstTagAside.get(1), UIMAP_Home.lstTagListItems, 20);
					if (lstTagListItems.size() > 0)
						for (WebElement item : lstTagListItems) {
							if (item.getText().contains(strSecondSubMenuName)) {
								WebElement button = commonLibrary.isExist(item, UIMAP_Home.lnkTopicPod, 20);
								blnThird = true;
								if (browsername.contains("internet"))
									commonLibrary.clickButtonParentWithWait(button, strSecondSubMenuName);
								else
									commonLibrary.clickButtonParentWithWait(button, strSecondSubMenuName);
								break;
							}

						}
					if (!blnThird)
						report.updateTestLog("Click on " + strSecondSubMenuName, "Not Clicked on " + strSecondSubMenuName, Status.FAIL);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Action and verify button is
	// present
	// # Function Name : verifyButtonInAction    
	// # Author : Harish
	// # Date Created : July'15
	// #**************************************************************************************************
	public Practice verifyButtonInAction(String buttonName, boolean presence) {
		commonLibrary.sleep(10);
		Boolean flag = false;
		for (int i = 0; i < 3; i++) {
			WebElement divider = commonLibrary.isExist(UIMAP_SearchResult.divider, 20);
			WebElement hdrresult = commonLibrary.isExist(divider, UIMAP_SearchResult.btnClassArrow, 20);
			commonLibrary.clickJS(hdrresult, "Actions Dropdown Arrow");
			WebElement divAction = commonLibrary.isExistNegative(UIMAP_SearchResult.divAction, 3);
			if (divAction != null)
				break;
		}

		WebElement divAction = commonLibrary.isExist(UIMAP_SearchResult.divAction, 20);
		if (divAction != null) {
			List<WebElement> listItems = commonLibrary.isExistList(divAction, By.tagName("li"), 20);
			for (WebElement item : listItems) {
				List<WebElement> btn = commonLibrary.isExistList(item, By.tagName("button"), 20);
				for (WebElement item2 : btn) {
					if (item2.getText().toLowerCase().contains(buttonName.toLowerCase())) {
						flag = true;
					}
				}
				if (flag)
					break;
			}
		}
		if (presence) {
			if (flag)
				report.updateTestLog("Verify '" + buttonName + "' is present in Actions dropdown", "'" + buttonName + "' is present in Actions dropdown", Status.PASS);
			else
				report.updateTestLog("Verify '" + buttonName + "' is present in Actions dropdown", "'" + buttonName + "' is not present in Actions dropdown", Status.FAIL);
		} else {
			if (!flag)
				report.updateTestLog("Verify '" + buttonName + "' is present in Actions dropdown", "'" + buttonName + "' is not present in Actions dropdown", Status.PASS);
			else
				report.updateTestLog("Verify '" + buttonName + "' is present in Actions dropdown", "'" + buttonName + "' is present in Actions dropdown", Status.FAIL);
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Expand the sources
	// # Function Name : expandTopSources
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice expandTopSources(int index) {
		int count = index - 1;
		commonLibrary.sleep(10);
		WebElement divTopSrc = commonLibrary.isExist(UIMAP_Home.topicSourceDiv, 100);

		if (divTopSrc != null) {
			List<WebElement> topSrcs = commonLibrary.isExistList(divTopSrc, UIMAP_Home.topSourcesLi, 10);
			if (topSrcs != null) {
				WebElement btnMainDiv = commonLibrary.isExist(topSrcs.get(count), UIMAP_Home.btnMainDiv, 10);
				WebElement btnDiv = commonLibrary.isExist(btnMainDiv, UIMAP_Home.btnDiv, 10);
				WebElement expandSrcBtn = commonLibrary.isExist(btnDiv, UIMAP_Home.expTopSourceBtn, 10);
				commonLibrary.clickButton(expandSrcBtn);
				report.updateTestLog("Expand Top source filters", "Top Source filter is expanded", Status.PASS);
			}
		} else {
			report.updateTestLog("Add source filters", "Top Sources are not displayed", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add source filters
	// # Function Name : expandTopSources
	// # Author : Harish
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice addSourceFilter(int mainIndex, int subIndex) {
		int count = mainIndex - 1;
		int len = subIndex - 1;
		commonLibrary.sleep(10);
		WebElement divTopSrc = commonLibrary.isExist(UIMAP_Home.topicSourceDiv, 100);

		if (divTopSrc != null) {
			List<WebElement> topSrcs = commonLibrary.isExistList(divTopSrc, UIMAP_Home.topSourcesLi, 10);
			if (topSrcs != null) {
				WebElement fltrSrcUl = commonLibrary.isExist(topSrcs.get(count), UIMAP_Home.filterSrc, 10);
				List<WebElement> fltrSrcsList = commonLibrary.isExistList(fltrSrcUl, UIMAP_Home.topSourcesLi, 10);

				if (fltrSrcsList != null && fltrSrcsList.size() >= subIndex) {
					WebElement fltrIconDiv = commonLibrary.isExist(fltrSrcsList.get(len), UIMAP_Home.btnMainDiv, 10);
					WebElement addFltrToSearch = commonLibrary.isExist(fltrIconDiv, UIMAP_Home.addFltrToSrch, 10);
					commonLibrary.clickButtonParentWithWait(addFltrToSearch, "Add to Source");
					report.updateTestLog("Click on search icon next to sources", "Source filter is added", Status.PASS);
				} else {
					report.updateTestLog("Click on search icon next to sources", "Source filter is not displayed", Status.FAIL);
				}
			}
		} else {
			report.updateTestLog("Click on search icon next to sources", "Top Sources are not displayed", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Continue To ReplaceFilter
	// # Function Name : clickContinueToReplaceFilter     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************
	public Practice clickContinueToReplaceFilter(String option) {
		WebElement popUpPage = commonLibrary.isExist(UIMAP_Home.replaceFiltersAside, 30);
		WebElement eltPopupBoxForm = commonLibrary.isExist(popUpPage, UIMAP_Home.frmFilterAlert, 30);
		WebElement ulList = commonLibrary.isExist(eltPopupBoxForm, UIMAP_Home.ulList, 30);

		List<WebElement> replaceFilterOptions = commonLibrary.isExistList(ulList, UIMAP_Home.inpt, 10);
		if (replaceFilterOptions.size() > 0) {
			for (WebElement opt : replaceFilterOptions) {
				if (opt.getAttribute("value").toLowerCase().equals(option.toLowerCase())) {
					if (browsername.contains("internet"))
						commonLibrary.clickJS(opt, option);
					else
						commonLibrary.clickButtonParentWithWait(opt, option);
					break;
				}
			}
		} else {
			report.updateTestLog("Verify 'Replace Existing Filters Pop-up displays'", "Replace Existing Filters Pop-up not displayed", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Validate Filter Count
	// # Function Name : clickContinueToReplaceFilter     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************
	public Practice verifyFilterCount(int filterCount) {
		commonLibrary.sleep(10);
		WebElement FilterIdCount = commonLibrary.isExist(UIMAP_Home.FilterIdCount, 20);
		String strCount = FilterIdCount.getText();

		if (Integer.parseInt(strCount) == filterCount) {
			report.updateTestLog("Verify the count  '" + filterCount + "'  displays in Filters dropdown", "'" + filterCount + "' is displayed as filter count in Filters dropdown", Status.PASS);
		} else {
			report.updateTestLog("Verify the count  '" + filterCount + "'  displays in Filters dropdown", "'" + filterCount + "' is not displayed as filter count in Filters dropdown", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearch     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Search simpleSearch(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Search Result page Header
	// # Function Name : verifyPageTitle     
	// # Author : Vennila
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public Practice verifyPageTitle(String strPageHeader) {
		WebElement HeaderSearchResult = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader1, 5);
		WebElement HeaderSearchResult1 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader, 5);
		WebElement HeaderSearchResult3 = commonLibrary.isExistNegative(UIMAP_Document.SearchResultHeader3, 5);
		WebElement HeaderSearchResult4 = commonLibrary.isExistNegative(UIMAP_Document.hdrResult, 5);
		WebElement HeaderSearchResult5 = commonLibrary.isExistNegative(UIMAP_PracticePage.resultHeader, 5);
		WebElement HeaderPage = commonLibrary.isExistNegative(UIMAP_IMContent.SearchResultHeader4, 5);

		WebElement Header = null;

		if (HeaderSearchResult != null)
			Header = HeaderSearchResult;
		else if (HeaderSearchResult1 != null)
			Header = HeaderSearchResult1;
		else if (HeaderSearchResult3 != null)
			Header = HeaderSearchResult3;
		else if (HeaderSearchResult4 != null)
			Header = HeaderSearchResult4;
		else if (HeaderPage != null)
			Header = HeaderPage;
		else if (HeaderSearchResult5 != null)
			Header = HeaderSearchResult5;

		if ((Header != null) && Header.getText().toLowerCase().contains(strPageHeader.toLowerCase()))
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is displayed as Header", Status.PASS);

		else
			report.updateTestLog("Verify " + strPageHeader + " is displayed as Header", strPageHeader + " is not displayed as Header", Status.FAIL);

		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Expand the key Topics
	// # Function Name : expandKeyTopics
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice expandKeyTopics() {
		Boolean blnFlag;
		commonLibrary.sleep(10);
		blnFlag = false;
		WebElement keyForm = commonLibrary.isExist(UIMAP_PracticePage.formKeyTopics, 10);
		if (keyForm != null) {
			WebElement keyHeader = commonLibrary.isExist(keyForm, UIMAP_PracticePage.headerKeyTopics, 10);
			if (keyHeader != null) {
				WebElement expandKtBtn = commonLibrary.isExist(keyHeader, UIMAP_PracticePage.expKeyTopics, 10);
				if (expandKtBtn.getAttribute("class").contains("Down")) {
					commonLibrary.sleep(10);
				} else {
					commonLibrary.clickButton(expandKtBtn);
				}
				blnFlag = true;
			}
			if (blnFlag)
				report.updateTestLog("Expand Key Topics pod", "Key Topics pod is expanded", Status.PASS);

		}
		if (!blnFlag)
			report.updateTestLog("Expand Key Topics pod", "Key Topics pod is expanded", Status.FAIL);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Get document from Key Topics
	// # Function Name : getDocumentFromKeyTopics
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Search getDocumentFromKeyTopics(String topic) {
		Boolean blnFlag;
		blnFlag = false;
		int j = 0;
		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentDiv, 10);
		if (parentDiv != null) {
			WebElement keyForm = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.formKeyTopics, 10);
			if (keyForm != null) {
				WebElement keyTopicsParentDiv = commonLibrary.isExist(keyForm, UIMAP_PracticePage.keyTopicDiv, 10);
				if (keyTopicsParentDiv != null) {
					WebElement keyChildDiv = commonLibrary.isExist(keyTopicsParentDiv, UIMAP_PracticePage.keyTopicChildDiv, 10);
					if (keyChildDiv != null) {
						WebElement topicUl = commonLibrary.isExist(keyChildDiv, UIMAP_PracticePage.keyTopicUl, 10);
						if (topicUl != null) {
							List<WebElement> topicList = commonLibrary.isExistList(topicUl, UIMAP_PracticePage.listtag, 20);
							for (j = 0; j < topicList.size(); j++) {
								if (topicList.get(j).getText().toLowerCase().trim().contains(topic.toLowerCase().trim())) {
									WebElement expandTopic = commonLibrary.isExist(topicList.get(j), UIMAP_PracticePage.expandTopics, 20);
									// WebElement span =
									// commonLibrary.isExist(expandTopic,
									// UIMAP_PracticePage.spanTag, 20);
									if (expandTopic != null) {
										commonLibrary.clickButton(expandTopic);
										report.updateTestLog("Expand Key Topic" + topic, topic + "Key Topic is expanded", Status.PASS);
									} else {
										report.updateTestLog("Expand Key Topic" + topic, topic + "Key Topic is NOT expanded", Status.FAIL);
									}
									WebElement dropDownDiv = commonLibrary.isExist(UIMAP_PracticePage.actionList, 10);
									if (dropDownDiv != null) {
										WebElement dropDown = commonLibrary.isExist(dropDownDiv, UIMAP_PracticePage.actionList2, 10);
										if (dropDown != null) {
											WebElement getDoc = commonLibrary.isExist(dropDown, UIMAP_PracticePage.getDocument, 10);
											if (getDoc != null) {
												commonLibrary.clickButton(getDoc);
												report.updateTestLog("Click on get document for topic: " + topic, "Get Documents for Topic: " + topic + " is clicked.", Status.PASS);
												blnFlag = true;
												break;
											}
										}
									}

								}
								if (blnFlag)
									break;

							}

						}

					}
				}
			}
		}

		if (!blnFlag)
			report.updateTestLog("Click on get document for topic: " + topic, "Topic: " + topic + " is not present.", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Lexis Practice Advisor
	// pod
	// # Function Name : verifyLexisPracticeAdvisorPodDisplayed
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLexisPracticeAdvisorPodDisplayed(String podName) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader, 10);
			if (podHeader != null) {
				WebElement podSpan = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podSpan, 10);
				if (podSpan.getText().toLowerCase().trim().contains(podName.toLowerCase().trim()))
					report.updateTestLog("Verify" + podName + " pod is displayed in Texas page", podName + " pod is displayed in Texas page", Status.PASS);
				else
					report.updateTestLog("Verify" + podName + " pod is displayed in Texas page", podName + " pod is NOT displayed in Texas page", Status.FAIL);
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in LPA pod
	// # Function Name : verifyLogoDisplayedInLPAPod
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInLPAPod() {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv, 10);
				if (podContentDiv != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.imgDiv, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link in LPA pod
	// # Function Name : verifyLinkDisplayedInPod
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLinkDisplayedInPod(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim()))
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is displayed in LPA pod", Status.PASS);
						else
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is NOT displayed in LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on link in LPA pod
	// # Function Name : clickLinkOnPod
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public LPAHome clickLinkOnPod(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " clicked from LPA pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " NOT clicked from LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Add practice to filters
	// # Function Name : selectaddtofilterforpratice
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice selectaddtofilterforpratice(String topic) {
		Boolean blnFlag;
		blnFlag = false;
		int j = 0;
		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentDiv, 10);
		if (parentDiv != null) {
			WebElement keyForm = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.formKeyTopics, 10);
			if (keyForm != null) {
				WebElement keyTopicsParentDiv = commonLibrary.isExist(keyForm, UIMAP_PracticePage.keyTopicDiv, 10);
				if (keyTopicsParentDiv != null) {
					WebElement keyChildDiv = commonLibrary.isExist(keyTopicsParentDiv, UIMAP_PracticePage.keyTopicChildDiv, 10);
					if (keyChildDiv != null) {
						WebElement topicUl = commonLibrary.isExist(keyChildDiv, UIMAP_PracticePage.keyTopicUl, 10);
						if (topicUl != null) {
							List<WebElement> topicList = commonLibrary.isExistList(topicUl, UIMAP_PracticePage.listtag, 20);
							for (j = 0; j < topicList.size(); j++) {

								if (topicList.get(j).getText().toLowerCase().trim().contains(topic.toLowerCase().trim())) {

									WebElement dropDownDiv = commonLibrary.isExist(topicList.get(j), UIMAP_PracticePage.divtag1, 10);
									if (dropDownDiv != null) {

										WebElement getDoc = commonLibrary.isExist(dropDownDiv, UIMAP_PracticePage.addtofilter, 10);
										if (getDoc != null) {

											commonLibrary.clickButton(getDoc);
											report.updateTestLog("Adding topic to Search: " + topic, "Adding topic to Search: " + topic + " is clicked.", Status.PASS);
											blnFlag = true;
											break;

										}
									}

									if (blnFlag)
										break;
								}
								if (blnFlag)
									break;
							}

						}

					}
				}
			}
		}

		if (!blnFlag)
			report.updateTestLog("Adding topic to Search: " + topic, "Adding topic to Search: " + topic + " is not present.", Status.FAIL);

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Ramesh Devaraj
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyBannerMessage(String text) {
		generalFunctions.verifyBannerMessage(text);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function selecting Filter menu items
	// # Function Name : FilterMenuSelection
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Practice verifyFilterSelected(String strSelectedFilterOptions) {
		Boolean blnFalg = false;
		String[] arrSelectedFilterOptions = strSelectedFilterOptions.split(";");
		try {

			WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			WebElement mnuNarrorFilter = commonLibrary.isExist(UIMAP_Home.mnuNarrorFilter, 20);
			if (mnuNarrorFilter != null) {
				List<WebElement> divClassDeleteFilter = commonLibrary.isExistList(mnuNarrorFilter, UIMAP_Home.divClassDeleteFilter, 20);
				for (int i = 0; i < arrSelectedFilterOptions.length; i++) {

					for (WebElement item : divClassDeleteFilter) {
						if (item.getText().contains(arrSelectedFilterOptions[i])) {
							blnFalg = true;

							break;
						}
					}
				}

			}
			if (blnFalg) {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy textbox", strSelectedFilterOptions + " is displayed in NarrowBy textbox", Status.PASS);
			} else {
				report.updateTestLog("Verify " + strSelectedFilterOptions + " is displayed in NarrowBy textbox", strSelectedFilterOptions + " is not displayed in NarrowBy textbox", Status.FAIL);
			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify filter Menus
	// # Function Name : verifyFilterMenu     
	// # Author : Vennila
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyFilterMenu(String strMenuName, Boolean flag) {

		Boolean flag1 = false;
		try {
			String[] arrSelectedFilterOptions = strMenuName.split(";");
			WebElement btnClassFilter = commonLibrary.isExist(UIMAP_Home.btnClassFilter, 20);
			commonLibrary.clickButtonParentWithWait(btnClassFilter, "Filter");
			WebElement mnuFilterToolBar = commonLibrary.isExist(UIMAP_Home.mnuFilterToolBar, 20);
			if (mnuFilterToolBar != null) {
				List<WebElement> lstButtons = commonLibrary.isExistList(mnuFilterToolBar, UIMAP_Home.btnIdFilterMenu, 20);
				if (lstButtons.size() > 0) {
					for (int i = 0; i < arrSelectedFilterOptions.length; i++) {
						for (WebElement button : lstButtons) {
							if (button.getText().contains(arrSelectedFilterOptions[i])) {
								flag1 = true;
								report.updateTestLog("Verify filter Menu", strMenuName + "is displayed", Status.PASS);
								break;
							}

						}
					}
				}

			}
			if (!flag && !flag1)
				report.updateTestLog("Verify filter Menu", strMenuName + "is  not displayed", Status.PASS);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close pre filter popup
	// # Function Name : closePreFilterPopUp     
	// # Author : Vennila
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice closePreFilterPopUp() {
		generalFunctions.closePreFilterPopUp();
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Pod Name
	// # Function Name : verifyPod
	// # Author : Vennila
	// # Date Created : 14 Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyPod(String podName) {
		boolean flag1 = false;
		List<WebElement> podClass = commonLibrary.isExistList(UIMAP_PracticePage.podClass, 10);
		for (WebElement pod : podClass) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_PracticePage.h2class, 10);
			if (header.getText().trim().toLowerCase().contains(podName.trim().toLowerCase())) {

				flag1 = true;
				break;
			}
		}

		if (!flag1)
			report.updateTestLog(podName + "  is displayed", podName + "  is not displayed", Status.FAIL);
		else
			report.updateTestLog(podName + "  is displayed", podName + "  is  displayed", Status.PASS);

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : function used to add child source filter to
	// search in top source pod.
	// # Function Name : clickAddToFiltForSubSource     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAddToFiltForSubSource(String parSource, String chiSource) {

		boolean flag = false;
		List<WebElement> fullpod = commonLibrary.isExistList(UIMAP_PracticePage.fullPOd, 10);
		// List <WebElement> liClass =
		// commonLibrary.isExistList(UIMAP_PracticePage.liClass, 10);
		List<WebElement> podName = commonLibrary.isExistList(fullpod.get(0), UIMAP_PracticePage.podName, 10);
		if (podName != null) {
			List<WebElement> socFull = commonLibrary.isExistList(fullpod.get(0), UIMAP_PracticePage.parSocFull, 10);
			// WebElement expCol = commonLibrary.isExist (fullpod.get(0),
			// UIMAP_PracticePage.expCol, 10);
			for (int i = 0; i < socFull.size(); i++) {
				if (socFull.get(i).getText().contains(parSource)) {
					WebElement expCol = commonLibrary.isExist(socFull.get(i), UIMAP_PracticePage.expCol, 10);
					commonLibrary.clickButtonParent(expCol, "Expand Arrow");
					List<WebElement> liClass = commonLibrary.isExistList(UIMAP_PracticePage.liClass, 10);
					// List<WebElement> divclass = commonLibrary.isExistList(
					// UIMAP_PracticePage.divclass, 10);
					for (int k = 0; k < liClass.size(); k++) {
						if (liClass.get(k).getText().contains(parSource)) {

							WebElement subListSoc = commonLibrary.isExist(liClass.get(k), UIMAP_PracticePage.subListSoc, 10);
							// List <WebElement> liSoc =
							// commonLibrary.isExistList(
							// liClass.get(k), UIMAP_PracticePage.liSoc, 10);
							List<WebElement> subSocFull = commonLibrary.isExistList(subListSoc, UIMAP_PracticePage.parSocFull, 10);
							// WebElement addFilt = commonLibrary.isExist
							// (fullpod.get(0), UIMAP_PracticePage.addFilt, 10);
							if (subListSoc != null && subSocFull != null) {
								for (int j = 0; j <= subSocFull.size(); j++) {
									if (subSocFull.get(j).getText().contains(chiSource)) {
										WebElement addFilt = commonLibrary.isExist(subSocFull.get(j), UIMAP_PracticePage.addFilt, 10);
										commonLibrary.clickButtonParent(addFilt, "Add to filter icon");
										flag = true;

										break;
									} else {
										flag = false;
										// report.updateTestLog("Adding topic to
										// Search: "
										// +
										// chiSource,"Adding topic to Search: "
										// + chiSource +
										// " is not present.",Status.FAIL);
									}
								}

							}

						}

					}

				}

			}

		}

		if (flag == true) {
			report.updateTestLog("Adding topic to Search: " + chiSource, "Adding topic to Search: " + chiSource + " is present.", Status.PASS);

		} else {
			report.updateTestLog("Adding topic to Search: " + chiSource, "Adding topic to Search: " + chiSource + " is not present.", Status.FAIL);
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and select option for
	// a sub source
	// # Function Name : expandSourceandSelectOption
	// # Author : Ramesh Devaraj
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice expandSourceandSelectOption(String linkName, String subLink, String Option) {
		try {

			boolean flag1 = false;
			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					// WebElement srcexpanded = commonLibrary.isExist(togleBar,
					// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);

						for (WebElement item2 : lists1) {

							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							// WebElement divpod3 =
							// commonLibrary.isExist(divpod2,
							// UIMAP_PracticePage.divtag2, 10);

							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);

							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase()))

							{
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.subshowhide, 10);
								commonLibrary.clickButtonParentWithWait(btn, subLink);
								Thread.sleep(5000);

								WebElement divtag = commonLibrary.isExist(item2, UIMAP_PracticePage.action1, 10);
								WebElement divtag1 = commonLibrary.isExist(divtag, UIMAP_PracticePage.actionlist, 10);
								WebElement listsatag = commonLibrary.isExist(divtag1, UIMAP_PracticePage.listtag, 10);
								if (listsatag.getText().trim().toLowerCase().contains(Option.trim().toLowerCase())) {

									WebElement atag = commonLibrary.isExist(listsatag, By.tagName("a"), 10);
									commonLibrary.clickButtonParentWithWait(atag, Option);
									Thread.sleep(5000);
								}

								flag2 = true;
								break;
							}
							if (flag2)
								break;
						}
					}
				}
				if (flag1)
					break;
			}
			if (flag1) {
				report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is selected under source " + subLink + " for the main source" + linkName, Status.PASS);
			} else {
				report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is not selected under source " + subLink + " for the main source" + linkName, Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter text in big Red search box
	// # Function Name : enterText
	// # Author : Vennila
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice enterText(String value) {
		WebElement text = commonLibrary.isExist(UIMAP_PracticePage.txtSrch2, 20);
		if (text != null) {
			commonLibrary.setDataInTextBox(text, value, "SearchTerm");
		} else {
			report.updateTestLog("Enter " + value + " in text box", "textbox is not present", Status.FAIL);
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the search button
	// # Function Name : clickSearchButton
	// # Author : Pratik
	// # Date Created : Feb'20
	// #*************************************************************************
	public Search clickSearchButton() {
		WebElement eltSearchbutton = commonLibrary.isExist(UIMAP_Home.btnIdSearch, 20);
		commonLibrary.clickButtonParentWithWait(eltSearchbutton, "Search");
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select option from Wordwheel.
	// # Function Name : clickTextInWordWheel
	// # Author : Pratik
	// # Date Created : Mar'15
	// #*****************************************************************************************************************************

	public Practice clickTextInWordWheel(String text) {
		generalFunctions.clickTextInWordWheel(text);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and select option for
	// a sub source
	// # Function Name : expandSourceandSelectOptionSearch
	// # Author : Ramesh Devaraj
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public Search expandSourceandSelectOptionSearch(String linkName, String subLink, String Option) {
		try {

			boolean flag1 = false;
			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					// WebElement srcexpanded = commonLibrary.isExist(togleBar,
					// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);

						for (WebElement item2 : lists1) {

							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							// WebElement divpod3 =
							// commonLibrary.isExist(divpod2,
							// UIMAP_PracticePage.divtag2, 10);

							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);

							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase()))

							{
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.subshowhide, 10);
								commonLibrary.clickButtonParentWithWait(btn, subLink);
								Thread.sleep(5000);

								WebElement divtag = commonLibrary.isExist(item2, UIMAP_PracticePage.action1, 10);
								WebElement divtag1 = commonLibrary.isExist(divtag, UIMAP_PracticePage.actionlist, 10);
								WebElement listsatag = commonLibrary.isExist(divtag1, UIMAP_PracticePage.listtag, 10);
								if (listsatag.getText().trim().toLowerCase().contains(Option.trim().toLowerCase())) {

									WebElement atag = commonLibrary.isExist(listsatag, By.tagName("a"), 10);
									commonLibrary.clickButtonParentWithWait(atag, Option);
									Thread.sleep(5000);
								}

								flag2 = true;
								break;
							}
							if (flag2)
								break;
						}
					}
				}
				if (flag1)
					break;
			}
			if (flag1) {
				report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is selected under source " + subLink + " for the main source" + linkName, Status.PASS);
			} else {
				report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is not selected under source " + subLink + " for the main source" + linkName, Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the Lexis Securities Mosaic
	// pod
	// # Function Name : verifyLexisSecuritiesMosaicPodDisplayed
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLexisSecuritiesMosaicPodDisplayed(String podName) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentLSMPodDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMHeader, 10);
			if (podHeader != null) {
				WebElement podSpan = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podSpan, 10);
				if (podSpan.getText().toLowerCase().trim().contains(podName.toLowerCase().trim()))
					report.updateTestLog("Verify" + podName + " pod is displayed in Mergers & Acquisitions Law page", podName + " pod is displayed in Mergers & Acquisitions Law page", Status.PASS);
				else
					report.updateTestLog("Verify" + podName + " pod is displayed in Mergers & Acquisitions Law page", podName + " pod is NOT displayed in Mergers & Acquisitions Law page", Status.FAIL);
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in LSM pod
	// # Function Name : verifyLogoDisplayedInLSMPod
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInLSMPod() {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentLSMPodDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMHeader, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed")) {
					commonLibrary.clickButton(podButton);
				}
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMDiv, 10);
				if (podContentDiv != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.imgDiv, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify Lexis Securities Mosaic logo is displayed in LSM pod", "Lexis Securities Mosaic logo is displayed in LSM pod", Status.PASS);
						else
							report.updateTestLog("Verify Lexis Securities Mosaic logo is displayed in LSM pod", "Lexis Securities Mosaic logo is NOT displayed in LSM pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on link and Verify link in LSM
	// pod
	// # Function Name : clickAndVerifyLinkFromLSMPod
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAndVerifyLinkFromLSMPod(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentLSMPodDiv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMHeader, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMDiv, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LSM pod", Link + " clicked from LSM pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LSM pod", Link + " NOT clicked from LSM pod", Status.FAIL);
					}
				}
			}
		}

		commonLibrary.sleep(1000);

		String verifyPage = driver.getCurrentUrl();
		if (verifyPage != null && verifyPage.contains("login")) {
			report.updateTestLog("Verify Lexis Securities Mosaic Signin page is displayed", "Lexis Securities Mosaic Signin page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Lexis Securities Mosaic Signin page is displayed", "Lexis Securities Mosaic Signin page is NOT displayed", Status.FAIL);
		}
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to perform simple search
	// # Function Name : simpleSearchPractice     
	// # Author : Aravind Russell V
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Practice simpleSearchPractice(String strSearchTerm, Boolean strClearFilter) {
		generalFunctions.simpleSearch(strSearchTerm, strClearFilter);
		check = pageCheck.positiveCheck(driver, "Search", "Search Page");
		pageCheck.handleFlow(driver, check);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to to click on toc from fav
	// # Function Name : clickTocLinkinFavPod     
	// # Author : Aravind Russell V
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	@SuppressWarnings("unused")
	public TOC clickTocLinkinFavPod(String socName, String TocLink) {
		boolean tag = false;
		WebElement favPodFull = commonLibrary.isExist(UIMAP_PracticePage.favPodFull, 10);
		WebElement favcon = commonLibrary.isExist(favPodFull, UIMAP_PracticePage.favcon, 10);
		List<WebElement> favLi = commonLibrary.isExistList(favcon, UIMAP_PracticePage.favLi, 10);
		for (int i = 0; i < favLi.size(); i++) {
			WebElement favP = commonLibrary.isExist(favLi.get(i), UIMAP_PracticePage.favP, 10);
			if (favP.getText().contains(socName)) {
				List<WebElement> favTocLink = commonLibrary.isExistList(favLi.get(i), UIMAP_PracticePage.favTocLink, 10);
				for (int j = 0; j < favTocLink.size(); j++) {
					if (favTocLink.get(j).getText().contains(TocLink)) {
						commonLibrary.clickButton(favTocLink.get(j));
						report.updateTestLog("Click TOC link for" + socName, "TOC link has been clicked for: " + socName, Status.PASS);
						break;
					}
				}
				break;
			} else {
				tag = false;
			}

			if (tag = false) {
				report.updateTestLog("Click TOC link for" + socName, "TOC link has not clicked for: " + socName, Status.FAIL);
			}
		}

		return new TOC(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify the Lexis Practice Advisor
	// pod-BFSpage
	// # Function Name : verifyLexisPracticeAdvisorPodDisplayed
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice verifyLexisPracticeAdvisorPodDisplayedBFS(String podName) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS2, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeaderFS2, 10);
			if (podHeader1 != null) {
				WebElement podSpan1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podSpan1, 10);
				if (podSpan1.getText().toLowerCase().trim().contains(podName.toLowerCase().trim()))
					report.updateTestLog("Verify" + podName + " pod is displayed in BFS page", podName + " pod is displayed in BFS page", Status.PASS);
				else
					report.updateTestLog("Verify" + podName + " pod is displayed in BFS page", podName + " pod is NOT displayed BFS Texas page", Status.FAIL);
			}
		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify logo in LPA pod BFS page
	// # Function Name : verifyLogoDisplayedInLPAPodBFS
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice verifyLogoDisplayedInLPAPodBFS() {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS2, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeaderFS2, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1, 10);
				WebElement span = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan, 10);
				if (podButton1.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDivFS2, 10);
				if (podContentDiv1 != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.imgDiv, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify link in LPA pod BFS page
	// # Function Name : verifyLinkDisplayedInPod
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice verifyLinkDisplayedInPodBFS(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS2, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeaderFS2, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1, 10);
				WebElement span1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan1, 10);
				if (podButton1.getAttribute("class").contains("Down") && span1.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDivFS2, 10);
				if (podContentDiv1 != null) {
					WebElement linkUl1 = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl1 != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl1, UIMAP_PracticePage.lnkVerify1, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim()))
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is displayed in LPA pod", Status.PASS);
						else
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is NOT displayed in LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to click on link in LPA pod
	// # Function Name : clickLinkOnPod-BFS
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public LPAHome clickLinkOnPodBFS(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS2, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeaderFS2, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1, 10);
				WebElement span1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan1, 10);
				if (podButton1.getAttribute("class").contains("Down") && span1.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDivFS2, 10);
				if (podContentDiv1 != null) {
					WebElement linkUl1 = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl1 != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl1, UIMAP_PracticePage.lnkVerify1, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " clicked from LPA pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " NOT clicked from LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clik <add to search> link displays
	// at the bottom of the pod and click the link
	// # Function Name : verifyAddToSearchFiltersAndClk
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyAddToSearchFiltersAndClk() {
		boolean blnFlag = false;

		WebElement newsPod = commonLibrary.isExist(UIMAP_PracticePage.newspod, 20);
		List<WebElement> allULList = commonLibrary.isExistList(newsPod, UIMAP_PracticePage.ul, 20);
		if (allULList.size() > 0) {

			List<WebElement> newsCommPodList = commonLibrary.isExistList(allULList.get(0), UIMAP_PracticePage.actionList1, 20);
			if (newsCommPodList.size() > 0) {
				// WebElement news = commonLibrary.isExist(UIMAP_Home.divNews,
				// 20);
				// if (news != null) {
				// List<WebElement> li = commonLibrary.isExistList(news,
				// UIMAP_SearchResult.lnkHeader, 10);
				List<WebElement> allNews = commonLibrary.isExistList(newsCommPodList.get(0), UIMAP_PracticePage.addTo, 20);
				for (WebElement item : allNews) {
					if (item.getText().equalsIgnoreCase("Add as a search filter")) {
						commonLibrary.clickLink(item, "Add as a search filter");
						blnFlag = true;

					}
				}
			}
		}
		if (blnFlag) {

			report.updateTestLog("verify Add as a search filter is selected from new pod", "Add as a search filter is added to filter", Status.PASS);

		} else {

			report.updateTestLog("verify Add as a search filter is selected from new pod", "Add as a search filter is added to filter", Status.FAIL);

		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify banner message.
	// # Function Name : verifyBannerMessage     
	// # Author : Ramesh Devaraj
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyBannerMessage1(String text) {
		// commonLibrary.sleep(5);
		generalFunctions.verifyBannerMessage1(text);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in Wallstreet pod
	// # Function Name : verifyLogoDisplayedInWallstreet
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInWallstreet() {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDivWS, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeaderWS, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButtonWS, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDivWS, 10);
				if (podContentDiv != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.imgDivWS, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify Wall Street Journal logo is displayed in pod", "Wall Street Journal logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify Wall Street Journal logo is displayed in pod", "Wall Street Journal logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on link and Verify link in
	// Launch Wall Street Journal
	// # Function Name : clickAndVerifyLinkFromWallstreet
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAndVerifyLinkFromWallstreet(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentLSMPodDivWS, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMHeaderWS, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podLSMDivWS, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in Launch Wall Street Journal", Link + " clicked from Wall Street Journal pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in Launch Wall Street Journal pod", Link + " NOT clicked from Wall Street Journal pod", Status.FAIL);
					}
				}
			}
		}

		commonLibrary.sleep(1000);

		String verifyPage = driver.getCurrentUrl();
		if (verifyPage != null && verifyPage.contains("wsj")) {
			report.updateTestLog("Verify Wall Street Journal is displayed", "Wall Street Journalis displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Wall Street Journal is displayed", "Wall Street Journal is NOT displayed", Status.FAIL);
		}
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in Federal Rulemaking
	// Releases Under the Dodd-Frank Act pod
	// # Function Name :
	// verifyLogoDisplayedInFederalRulemakingReleasesUndertheDoddFrankAct
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInFederalRulemakingReleasesUndertheDoddFrankAct() {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeaderFS, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButtonWS, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDivFS, 10);
				if (podContentDiv != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.imgDivWS, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify Lexis Securities Mosaic logo is displayed in pod", "Lexis Securities Mosaic logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify Lexis Securities Mosaic logo is displayed in pod", "Lexis Securities Mosaic logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on link and Verify link in
	// Launch Dodd-Frank Tracker on Lexis Securities Mosaic
	// # Function Name : clickAndVerifyLinkFromLexisSecuritiesMosaic
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAndVerifyLinkFromLexisSecuritiesMosaic(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeaderFS, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDivFS, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in Launch Wall Street Journal", Link + " clicked from Wall Street Journal pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in Launch Wall Street Journal pod", Link + " NOT clicked from Wall Street Journal pod", Status.FAIL);
					}
				}
			}
		}

		commonLibrary.sleep(1000);

		String verifyPage = driver.getCurrentUrl();
		if (verifyPage != null && verifyPage.contains("login")) {
			report.updateTestLog("Verify Launch Dodd-Frank Tracker on Lexis Securities Mosaic is displayed", "Dodd-Frank Tracker on Lexis Securities Mosaic login page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Launch Dodd-Frank Tracker on Lexis Securities Mosaic is displayed", "Dodd-Frank Tracker on Lexis Securities Mosaic login page  is NOT displayed", Status.FAIL);
		}
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to expand source and select option from
	// Next Child
	// # Function Name : expandSourceandSelectOptionforNextChild11
	// # Author : Vennila
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice expandSourceandSelectOptionforNextChild11(String linkName, String subLink, String Link1, String Option) {

		List<WebElement> podClass = commonLibrary.isExistList(UIMAP_PracticePage.podClass, 10);
		for (WebElement pod : podClass) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_PracticePage.h2class, 10);
			if (header.getText().trim().contains("Top Sources")) {
				WebElement ulItems = commonLibrary.isExistNegative(pod, UIMAP_PracticePage.keyTopicUl, 10);
				if (ulItems != null) {
					List<WebElement> OListItems = commonLibrary.isExistList(ulItems, By.tagName("li"), 20);
					if (OListItems.size() > 0) {
						for (WebElement element : OListItems) {
							if (element.getText().contains(linkName)) {
								WebElement expandIcon = commonLibrary.isExistNegative(element, UIMAP_PracticePage.expCol, 10);
								commonLibrary.clickButton(expandIcon);
								WebElement ulpod = commonLibrary.isExistNegative(element, UIMAP_PracticePage.subListSoc, 10);
								List<WebElement> spanPod = commonLibrary.isExistList(ulpod, UIMAP_PracticePage.spanPod, 20);
								if (spanPod.size() > 0) {
									for (WebElement element1 : spanPod) {
										if (element1.getText().contains(subLink)) {
											WebElement expandIcon2 = commonLibrary.isExistNegative(element, UIMAP_PracticePage.expCol, 10);
											commonLibrary.clickButton(expandIcon2);
											break;
										}

										break;
									}
								}

								List<WebElement> subChild = commonLibrary.isExistList(ulpod, UIMAP_PracticePage.subshowhide, 20);
								for (WebElement element2 : subChild) {
									if (element2.getText().contains(Link1)) {
										commonLibrary.clickButton(element2);

										break;
									}
								}
								WebElement actionCointainer = commonLibrary.isExistNegative(ulpod, UIMAP_PracticePage.action1, 10);
								List<WebElement> ulTag = commonLibrary.isExistList(actionCointainer, By.tagName("li"), 10);
								for (WebElement element3 : ulTag) {
									WebElement aTag = commonLibrary.isExistNegative(element3, By.tagName("a"), 10);
									if (aTag.getText().trim().contains(Option))
										commonLibrary.clickLinkWithWebElementWithWait(element3, Option);
									break;
								}

							} else
								report.updateTestLog("Option not displayed", "Option" + linkName + "not displayed", Status.FAIL);
						}
					}

				}
			} else
				report.updateTestLog("Top sources option not displayed", "Top sources option not displayed", Status.FAIL);

		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on link and Verify link in
	// Launch Lexis Practice Advisor® Banking & Finance
	// # Function Name : clickAndVerifyLinkFromLexis Practice Advisor
	// # Author : Ramesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAndVerifyLinkFromLexisPracticeAdvisor(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDivFS2, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeaderFS2, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDivFS2, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in Lexis Practice Advisor", Link + " clicked from Lexis Practice Advisor pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in Lexis Practice Advisor", Link + " NOT clicked from Lexis Practice Advisor pod", Status.FAIL);
					}
				}
			}
		}

		commonLibrary.sleep(1000);

		String verifyPage = driver.getCurrentUrl();
		if (verifyPage != null && verifyPage.contains("login")) {
			report.updateTestLog("Verify Launch Lexis Practice Advisor Banking & Finance is displayed", "Lexis Practice Advisor Banking & Finance home page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Launch Lexis Practice Advisor Banking & Finance is displayed", "Lexis Practice Advisor Banking & Finance home page  is NOT displayed", Status.FAIL);
		}
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to click on link in LPA pod
	// # Function Name : clickLinkOnPod-BFS
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public LPAHome clickLinkOnPodBFS1(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv2 = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv3, 10);
		if (parentDiv2 != null) {
			WebElement podHeader2 = commonLibrary.isExist(parentDiv2, UIMAP_PracticePage.podHeader3, 10);
			if (podHeader2 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader2, UIMAP_PracticePage.podButton1, 10);
				WebElement span1 = commonLibrary.isExist(podHeader2, UIMAP_PracticePage.expSpan1, 10);
				if (podButton1.getAttribute("class").contains("Down") && span1.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv2, UIMAP_PracticePage.podDiv3, 10);
				if (podContentDiv1 != null) {
					WebElement linkUl1 = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl1 != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl1, UIMAP_PracticePage.lnkVerify3, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " clicked from LPA pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " NOT clicked from LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new LPAHome(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify link in LPA pod BFS page
	// # Function Name : verifyLinkDisplayedInPod
	// # Author : D Ramesh
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice verifyLinkDisplayedInPodBFS1(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv3, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader3, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton1, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan1, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv3, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify3, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim()))
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is displayed in LPA pod", Status.PASS);
						else
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is NOT displayed in LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying History Dropdown
	// Presence.
	// # Function Name : verifyHistoryDropdown
	// # Author : Pratik
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Home verifyHistoryDropdown() {
		WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(UIMAP_Home.btnIdHistoryMenuArrow, 20);
		if (btnIdHistoryMenuArrow == null) {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DOES NOT DISPLAYS' IN GLOBAL MENU.", Status.FAIL);
		} else {
			report.updateTestLog("Verify 'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", "'HISTORY' DROPDOWN 'DISPLAYS' IN GLOBAL MENU.", Status.PASS);
		}
		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in LPA pod
	// # Function Name : verifyLogoDisplayedInLPAPod1
	// # Author : Ramesh
	// # Date Created : 29-Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInLPAPod1() {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv3, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader3, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv3, 10);
				if (podContentDiv != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.imgDiv, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify LPA logo is displayed in pod", "LPA logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify tab under News & analysis pod
	// # Function Name : verifyTabInNews     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyTabInNews(String Tab1, String Tab2, String Tab3) {
		boolean flag = false;
		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);
		List<String> resultListValue = null;
		resultListValue = new ArrayList<String>();
		for (int i = 0; i < newsConLiClass.size(); i++) {
			resultListValue.add(Tab1);
			resultListValue.add(Tab2);
			resultListValue.add(Tab3);
			if (newsConLiClass.get(i).getText().equals(resultListValue.get(i))) {
				flag = true;
			}

		}
		if (flag) {
			report.updateTestLog("Verify " + Tab1 + " " + Tab2 + " " + Tab3 + " are displayed under News & analysis pod", "Verify " + Tab1 + " " + Tab2 + " " + Tab3 + " are displayed under News & analysis pod", Status.PASS);
		} else {
			report.updateTestLog("Verify " + Tab1 + " " + Tab2 + " " + Tab3 + " are displayed under News & analysis pod", "Verify " + Tab1 + " " + Tab2 + " " + Tab3 + " are not displayed under News & analysis pod", Status.FAIL);

		}

		flag = false;
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify add to search icon under all
	// 3 tabs News & analysis pod
	// # Function Name : verifyAddTosearchInNewsTab     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyAddTosearchInNewsTab(String Tab1, String Tab2, String Tab3) {

		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);
		List<String> resultListValue = null;
		resultListValue = new ArrayList<String>();
		for (int i = 0; i < newsConLiClass.size(); i++) {
			resultListValue.add(Tab1);
			resultListValue.add(Tab2);
			resultListValue.add(Tab3);
			WebElement newsTabBut = commonLibrary.isExist(newsConLiClass.get(i), UIMAP_PracticePage.newsTabBut, 10);
			if (newsConLiClass.get(i).getText().equals(resultListValue.get(i))) {
				commonLibrary.clickButtonParent(newsTabBut, resultListValue.get(i));
				WebElement newsTabRes = commonLibrary.isExist(UIMAP_PracticePage.newsTabRes, 10);
				WebElement newsAddFilt = commonLibrary.isExist(newsTabRes, UIMAP_PracticePage.newsAddFilt, 10);
				commonLibrary.isExist(newsAddFilt, null, 10);
				report.updateTestLog("Verify 'Add as a search filter' are displayed under " + resultListValue.get(i) + " Tab", "'Add as a search filter' are displayed under " + resultListValue.get(i) + " Tab", Status.PASS);
			} else {
				report.updateTestLog("Verify 'Add as a search filter' are displayed under " + resultListValue.get(i) + " Tab", "'Add as a search filter' are not displayed under " + resultListValue.get(i) + " Tab", Status.PASS);
			}

		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on tabs under News and
	// analysis pod
	// # Function Name : clickTabOnNewsPod     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickTabOnNewsPod(String Tab1) {
		Boolean flag = false;
		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);
		for (int i = 0; i < newsConLiClass.size(); i++) {
			WebElement newsTabBut = commonLibrary.isExist(newsConLiClass.get(i), UIMAP_PracticePage.newsTabBut, 10);
			if (newsConLiClass.get(i).getText().equals(Tab1)) {
				commonLibrary.clickButtonParent(newsTabBut, Tab1);
				flag = true;
				break;
			} else {
				flag = false;
			}

		}
		if (flag) {
			report.updateTestLog("Verify " + Tab1 + " has been clicked under News & analysis pod", "Verify " + Tab1 + " displayed under News & analysis pod", Status.PASS);
		} else {
			report.updateTestLog("Verify " + Tab1 + " has been clicked under News & analysis pod", "Verify " + Tab1 + " not displayed under News & analysis pod", Status.FAIL);

		}

		flag = false;
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify add to search icon under all
	// 3 tabs News & analysis pod
	// # Function Name : verifyAddTosearchInNewsTab     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickAddTosearchInNewsTab(String Tab1) {
		boolean flag = false;
		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);

		for (int i = 0; i < newsConLiClass.size(); i++) {
			WebElement newsTabBut = commonLibrary.isExist(newsConLiClass.get(i), UIMAP_PracticePage.newsTabBut, 10);
			if (newsConLiClass.get(i).getText().equals(Tab1)) {
				commonLibrary.clickButtonParent(newsTabBut, Tab1);
				WebElement newsTabRes = commonLibrary.isExist(UIMAP_PracticePage.newsTabRes, 10);
				WebElement newsAddFilt = commonLibrary.isExist(newsTabRes, UIMAP_PracticePage.newsAddFilt, 10);
				commonLibrary.clickButtonParent(newsAddFilt, "Add to search");
				flag = true;
				break;

			} else {
				flag = false;

			}

		}
		if (flag) {
			report.updateTestLog("Verify 'Add as a search filter' are displayed under " + Tab1 + " Tab", "'Add as a search filter' clicked under " + Tab1 + " Tab", Status.PASS);

		} else {
			report.updateTestLog("Verify 'Add as a search filter' has been clicked under " + Tab1 + " Tab", "'Add as a search filter' are not clicked under " + Tab1 + " Tab", Status.FAIL);
		}
		flag = false;
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click article under news tab
	// # Function Name : clickArticalUnderNewsTab     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickArticalUnderNewsTab(String tab, int artcount) {
		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);

		for (int i = 0; i < newsConLiClass.size(); i++) {
			WebElement newsTabBut = commonLibrary.isExist(newsConLiClass.get(i), UIMAP_PracticePage.newsTabBut, 10);
			if (newsConLiClass.get(i).getText().equals(tab)) {
				commonLibrary.clickButtonParent(newsTabBut, tab);
				WebElement newsTabRes = commonLibrary.isExist(UIMAP_PracticePage.newsTabRes, 10);
				List<WebElement> newsArtRow = commonLibrary.isExistList(newsTabRes, UIMAP_PracticePage.newsArtRow, 10);
				String artSocName = newsArtRow.get(artcount).getText();
				if (newsArtRow != null && artSocName != null) {
					commonLibrary.clickLink(newsArtRow.get(artcount), artSocName);
					report.updateTestLog("Verify first article " + artSocName + " has click under " + tab + " tab", "Verify first article " + artSocName + " has been click under " + tab + " tab", Status.PASS);
					break;
				} else {
					report.updateTestLog("Verify first article " + artSocName + " has click under " + tab + " tab", "Verify first article " + artSocName + " has not present under " + tab + " tab", Status.FAIL);
				}

			}
		}

		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Secondary_BroserOpened 
	// # Function Name : verifySecondary_BroserOpened     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifySecondaryBroserOpened() {

		generalFunctions.openedSecondaryBrowser();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close print page and navigate back
	// to practice page
	// # Function Name : navigateBacktoPractice     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice navigateBacktoPractice() {
		driver.close();
		commonLibrary.switchToWindow("practice");
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : function is used to verify Artical values
	// # Function Name : verifyArticalValuesUnderNewsPod     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyArticalValuesUnderNewsPod(String tab, int totcount) {
		boolean flag = false;

		WebElement newsPodFull = commonLibrary.isExist(UIMAP_PracticePage.newsPodFull, 10);
		WebElement newsPodContent = commonLibrary.isExist(newsPodFull, UIMAP_PracticePage.newsPodContent, 10);
		WebElement newsConUlClass = commonLibrary.isExist(newsPodContent, UIMAP_PracticePage.newsConUlClass, 10);
		List<WebElement> newsConLiClass = commonLibrary.isExistList(newsConUlClass, UIMAP_PracticePage.newsConLiClass, 10);

		for (int i = 0; i < newsConLiClass.size(); i++) {
			WebElement newsTabBut = commonLibrary.isExist(newsConLiClass.get(i), UIMAP_PracticePage.newsTabBut, 10);
			if (newsConLiClass.get(i).getText().equals(tab)) {
				commonLibrary.clickButtonParent(newsTabBut, tab);
				WebElement newsTabRes = commonLibrary.isExist(UIMAP_PracticePage.newsTabRes, 10);
				List<WebElement> ulTab1 = commonLibrary.isExistList(newsTabRes, UIMAP_PracticePage.ulTab1, 10);
				// List <WebElement> newsConLiClass1 =
				// commonLibrary.isExistList(ulTab1.get(i),
				// UIMAP_PracticePage.newsConLiClass, 10);
				// for(int j=0; j<newsConLiClass1.size(); j++)
				// {

				List<WebElement> newsArtRow = commonLibrary.isExistList(ulTab1.get(i), UIMAP_PracticePage.newsArtRow, 10);
				if (newsArtRow != null) {
					if (newsArtRow.size() == totcount && newsArtRow.size() != 0) {
						flag = true;
						break;
						// a = g++;
					} else {
						flag = false;

					}

				}
				// }

			}
		}
		if (flag) {
			report.updateTestLog("Verify " + totcount + " article are displayed under " + tab + " tab", "Verify " + totcount + " article are displayed under " + tab + " tab", Status.PASS);

		} else {
			report.updateTestLog("Verify " + totcount + " article are displayed under " + tab + " tab", "Verify " + totcount + " article are displayed under " + tab + " tab", Status.FAIL);

		}
		flag = false;
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close secondary window page and
	// navigate back
	// to practice page
	// # Function Name : closeSecondaryWindow     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice closeSecondaryWindow() {
		// driver.close();
		commonLibrary.switchToWindow("practice");
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify logo in NVRLAR pod
	// # Function Name :
	// verifyLogoDisplayedInNationalViewOfRecentLegislationAndRegulationsPod
	// # Author : Prashanthini
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLogoDisplayedInNRLPodNVR(String podName) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv1Nv, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeader1Nv, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1Nv, 10);
				WebElement span = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan1Nv, 10);
				if (podButton1.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDiv1Nv, 10);
				if (podContentDiv1 != null) {
					WebElement logoVerification = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.imgDiv1Nv, 10);
					if (logoVerification != null) {
						WebElement verifyLogo = commonLibrary.isExist(logoVerification, UIMAP_PracticePage.imgTag1Nv, 10);
						if (verifyLogo != null && verifyLogo.isDisplayed())
							report.updateTestLog("Verify National View Of Recent Legislation And Regulations Pod logo is displayed in pod", "National View Of Recent Legislation And Regulations Pod logo is displayed in pod", Status.PASS);
						else
							report.updateTestLog("Verify National View Of Recent Legislation And Regulations Pod logo is displayed in pod", "National View Of Recent Legislation And Regulations Pod logo is NOT displayed in pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify link in LPA pod
	// # Function Name : verifyLinkDisplayedInPodNVR
	// # Author : Prashanthini
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice verifyLinkDisplayedInPodNVR(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv = commonLibrary.isExist(UIMAP_PracticePage.parentDiv1Nv, 10);
		if (parentDiv != null) {
			WebElement podHeader = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podHeader1Nv, 10);
			if (podHeader != null) {
				WebElement podButton = commonLibrary.isExist(podHeader, UIMAP_PracticePage.podButton1Nv, 10);
				WebElement span = commonLibrary.isExist(podHeader, UIMAP_PracticePage.expSpan1Nv, 10);
				if (podButton.getAttribute("class").contains("Down") && span.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton);
				WebElement podContentDiv = commonLibrary.isExist(parentDiv, UIMAP_PracticePage.podDiv1Nv, 10);
				if (podContentDiv != null) {
					WebElement linkUl = commonLibrary.isExist(podContentDiv, UIMAP_PracticePage.linkUlTag, 10);
					if (linkUl != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl, UIMAP_PracticePage.lnkVerify, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim()))
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is displayed in LPA pod", Status.PASS);
						else
							report.updateTestLog("Verify " + Link + " is displayed in LPA pod", Link + " is NOT displayed in LPA pod", Status.FAIL);
					}
				}
			}
		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to click on link in LPA pod
	// # Function Name : clickLinkOnPodBFSNPR
	// # Author : Prashanthini
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice clickLinkOnPodBFSNPR(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv1Nv, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeader1Nv, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1Nv, 10);
				WebElement span1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan1Nv, 10);
				if (podButton1.getAttribute("class").contains("Down") && span1.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDiv1Nv, 10);
				if (podContentDiv1 != null) {
					WebElement linkUl1 = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl1 != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl1, UIMAP_PracticePage.lnkVerify1, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " clicked from LPA pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " NOT clicked from LPA pod", Status.FAIL);
					}
				}
			}
		}

		boolean Flag;
		Flag = false;

		try {
			Robot robot = new Robot();
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.delay(2000);

			Flag = true;
			report.updateTestLog("Verify the external popup is displayed", "External popup displayed", Status.PASS);

			if (!Flag) {
				report.updateTestLog("Verify the external popup is displayed", "External popup NOT displayed", Status.FAIL);
			}

			commonLibrary.clickBrowserBack();

		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and select option for
	// a sub source
	// # Function Name : expandSourceandSelectOptionSearch
	// # Author : Jeeva
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Practice expandSourceandSelectTocLink(String linkName, String subLink) {
		try {

			boolean flag1 = false;
			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);

					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);

						for (WebElement item2 : lists1) {

							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);

							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("a"), 10);

							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase()))

							{
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.tocLink, 10);
								commonLibrary.clickButtonParentWithWait(btn, subLink);

								flag2 = true;
								break;
							}
							if (flag2)
								break;
						}
					}
				}
				if (flag1)
					break;
			}

		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify TOC title
	// # Function Name : verifyPracticePageTocTitle    
	// # Author : Jeeva
	// # Date Created : Dec '15
	// #*****************************************************************************************************************************

	public Practice verifyPracticePageTocTitle(String tocTit) {

		WebElement header = commonLibrary.isExist(UIMAP_TOC.tocheaderPractice, 10);
		if (header == null) {
			report.updateTestLog("Verify " + tocTit + "TOC page display", tocTit + "TOC header is not displayed", Status.FAIL);

		}
		WebElement pagewrapper = commonLibrary.isExist(header, UIMAP_TOC.pagewrapper, 10);
		if (pagewrapper != null) {
			if (pagewrapper.getText().toLowerCase().contains(tocTit.toLowerCase())) {
				report.updateTestLog("Verify " + tocTit + " TOC page display", tocTit + " TOC page is displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify " + tocTit + " TOC page display", tocTit + " TOC page is not displayed", Status.FAIL);
			}
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set client id
	// # Function Name : setClientID     
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Search setClientID(String clientId) {
		WebElement newClientId = commonLibrary.isExist(UIMAP_CounselBenchmarking.newClientId, 10);
		if (newClientId != null) {
			commonLibrary.setRadioButton(newClientId, "New Client ID Radio");
		}

		WebElement clientIdValue = commonLibrary.isExist(UIMAP_CounselBenchmarking.ClientId, 10);
		commonLibrary.setDataInTextBox(clientIdValue, clientId, "Client ID");

		WebElement setClientId = commonLibrary.isExist(UIMAP_CounselBenchmarking.setClientId, 10);
		commonLibrary.clickButtonParentWithWait(setClientId, "Set Client ID");

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and select option for
	// a sub source
	// # Function Name : expandSourceandSelectlink
	// # Author : Dinesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice expandSourceandSelectlink(String sourceGrp, String link)

	{
		WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
		WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
		WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
		WebElement ulTag = commonLibrary.isExist(divpod, UIMAP_PracticePage.ulTag, 10);
		List<WebElement> lists = commonLibrary.isExistList(ulTag, UIMAP_PracticePage.Ullist, 10);

		for (WebElement item : lists) {

			// WebElement item1 = commonLibrary.isExist(item,
			// UIMAP_PracticePage.podContent, 10);
			// WebElement itemSpan = commonLibrary.isExist(item,
			// UIMAP_PracticePage.spanPod, 10);
			String[] itemSpan1 = item.getText().split("\n");

			if (itemSpan1[0].toLowerCase().equalsIgnoreCase(sourceGrp.toLowerCase())) {
				WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
				WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);

				if (srcColapse != null) {

					commonLibrary.clickButtonParentWithWait(srcColapse, sourceGrp);

					List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);

					for (WebElement item2 : lists1) {
						String[] itemSpan2 = item2.getText().split("\n");
						if (itemSpan2[0].toLowerCase().equals(link.toLowerCase())) {
							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_LexisAdvanceTax.linkTOC, 10);
							commonLibrary.clickLink(divpod2, link);
							break;
						}
					}
				}

			}
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the back button from source
	// resutls page
	// # Function Name : clickBrowserBacksource
	// # Author : Dinesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice clickBrowserBacksource() {
		commonLibrary.clickBrowserBack();

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a two source and select option
	// for a sub source
	// # Function Name : expand2SourceandSelectOption
	// # Author : Dinesh
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Practice expand2SourceandSelectOption(String linkName, String linkName2, String subLink, String Option) {
		try {

			boolean flag1 = false;
			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					// WebElement srcexpanded = commonLibrary.isExist(togleBar,
					// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}

					List<WebElement> lists3 = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

					for (WebElement item3 : lists3) {

						WebElement item4 = commonLibrary.isExist(item3, UIMAP_PracticePage.podContent, 10);
						WebElement itemSpan5 = commonLibrary.isExist(item4, UIMAP_PracticePage.spanPod, 10);
						String[] itemSpan4 = itemSpan5.getText().split("\n");
						if (itemSpan4[0].toLowerCase().equalsIgnoreCase(linkName2.trim().toLowerCase())) {
							WebElement togleBar3 = commonLibrary.isExist(item3, By.tagName("div"), 10);
							WebElement srcColapse3 = commonLibrary.isExist(togleBar3, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
							// WebElement srcexpanded =
							// commonLibrary.isExist(togleBar,
							// UIMAP_LexisAdvanceTax.sourceExpanded, 10);
							if (srcColapse != null) {

								commonLibrary.clickButtonParentWithWait(srcColapse3, linkName2);
								Thread.sleep(5000);
							}
							if (subLink != "") {

								List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);

								for (WebElement item2 : lists1) {

									WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
									// WebElement divpod3 =
									// commonLibrary.isExist(divpod2,
									// UIMAP_PracticePage.divtag2, 10);

									WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);

									String[] itemSpan3 = itemSpan2.getText().split("\n");
									if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase()))

									{
										WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
										WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.subshowhide, 10);
										commonLibrary.clickButtonParentWithWait(btn, subLink);
										Thread.sleep(5000);

										WebElement divtag = commonLibrary.isExist(item2, UIMAP_PracticePage.action1, 10);
										WebElement divtag1 = commonLibrary.isExist(divtag, UIMAP_PracticePage.actionlist, 10);
										WebElement listsatag = commonLibrary.isExist(divtag1, UIMAP_PracticePage.listtag, 10);
										if (listsatag.getText().trim().toLowerCase().contains(Option.trim().toLowerCase())) {

											WebElement atag = commonLibrary.isExist(listsatag, By.tagName("a"), 10);
											commonLibrary.clickButtonParentWithWait(atag, Option);
											Thread.sleep(5000);
										}

										flag2 = true;
										break;
									}
									if (flag2)
										break;
								}
							}
						}
						if (flag1)
							break;
					}
					if (flag1) {
						report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is selected under source " + subLink + " for the main source" + linkName, Status.PASS);
					} else {
						report.updateTestLog("Select " + Option + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is not selected under source " + subLink + " for the main source" + linkName, Status.FAIL);
					}

				}
			}
		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify the News and Analysis Pod
	// Displayed
	// // # Function Name : verifyNewsandAnalysisPodDisplayed
	// # Author : Vennila
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice verifyNewsandAnalysisPodDisplayed(String podName) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivn, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeadern, 10);
			if (podHeader1 != null) {
				WebElement podSpan1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podSpann, 10);
				if (podSpan1.getText().toLowerCase().trim().contains(podName.toLowerCase().trim()))
					report.updateTestLog("Verify" + podName + " pod is displayed in NA page", podName + " pod is displayed in NA page", Status.PASS);
				else
					report.updateTestLog("Verify" + podName + " pod is displayed in NA page", podName + " pod is NOT displayed NA page", Status.FAIL);
			}
		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to click on All legal news
	// # Function Name : click All legal news
	// # Author : Vennila
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Practice clickAlllegalnews(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDivn, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeader22, 10);
			if (podHeader1 != null) {
				WebElement verifyLink = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.legalbutton, 10);
				if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
					commonLibrary.clickJS(verifyLink);
					report.updateTestLog("Click on" + Link + "tab", Link + " clicked from News and Analysis pod", Status.PASS);
				} else {
					report.updateTestLog("Click on" + Link + " in tab", Link + " NOT clicked from News and Analysis pod", Status.FAIL);
				}
			}
		}

		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to verify View all legal news link
	// displayed
	// # Function Name : verifyLinkDisplayedInPodNA
	// # Author : Vennila
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public Search ClickLinkDisplayedInPodNA1(String Link) {

		WebElement span2 = commonLibrary.isExist(UIMAP_PracticePage.Viewallegal, 10);
		if (span2 != null && span2.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
			report.updateTestLog("Verify " + Link + " is displayed in NA pod", Link + " is displayed in NA pod", Status.PASS);
			commonLibrary.clickButton(span2);
		} else
			report.updateTestLog("Verify " + Link + " is displayed in NA pod", Link + " is NOT displayed in NA pod", Status.FAIL);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify * is not displayed in a pod
	// # Function Name : verifyspecialcharactersNotDisplayed     
	// # Author : Vennila
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public Practice verifyspecialcharactersNotDisplayed(String character) {

		boolean flag1 = false;
		List<WebElement> alllegal = commonLibrary.isExistList(UIMAP_PracticePage.Viewallegal1, 10);
		for (WebElement pod : alllegal) {
			WebElement header = commonLibrary.isExistNegative(pod, UIMAP_PracticePage.newsConLiClass, 10);
			if (header.getText().trim().contains(character.trim())) {

				/*
				 * List<WebElement> links = commonLibrary.isExistList(pod,
				 * UIMAP_LexisAdvanceTax.links, 10); for (WebElement link :
				 * links) { if
				 * (link.getText().toLowerCase().contains(linkName.toLowerCase
				 * ()))
				 */
				report.updateTestLog("Verify * is not displayed in pod: ", character + " is displayed in pod:", Status.FAIL);
				flag1 = true;
				break;
			}
			if (flag1)
				break;
		}
		// "Verify * is not displayed in pod: " , podName +
		// " is displayed in pod:" , Status.FAIL
		if (!flag1)
			report.updateTestLog("Verify * is not displayed in pod: ", character + " is not displayed in pod: ", Status.PASS);

		return new Practice(scriptHelper);
	}

	public Search expandSourceandSelectOptionGetDocuments(String linkName, String subLink, String Sublink2, String Option) {
		try {

			boolean flag2 = false;
			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);
						for (WebElement item2 : lists1) {
							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);
							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase())) {
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.expCol, 10);

								if (btn != null) {
									commonLibrary.clickButtonParentWithWait(btn, subLink);
									Thread.sleep(5000);
								}

								List<WebElement> lists2 = commonLibrary.isExistList(item, UIMAP_PracticePage.ulsource, 10);
								for (WebElement item3 : lists2) {

									WebElement itemSpan4 = commonLibrary.isExist(item3, By.tagName("span"), 10);
									String[] itemSpan5 = itemSpan4.getText().split("\n");
									if (itemSpan5[0].toLowerCase().equalsIgnoreCase(Sublink2.trim().toLowerCase())) {
										WebElement togleBar3 = commonLibrary.isExist(item3, By.tagName("div"), 10);
										WebElement btn1 = commonLibrary.isExist(togleBar3, UIMAP_PracticePage.subshowhide, 10);
										if (btn1 != null) {
											commonLibrary.clickButtonParentWithWait(btn1, Sublink2);
											Thread.sleep(5000);
										}

										WebElement divtag = commonLibrary.isExist(item3, UIMAP_PracticePage.action1, 10);
										WebElement divtag2 = commonLibrary.isExist(divtag, UIMAP_PracticePage.actionlist, 10);
										WebElement listsatag = commonLibrary.isExist(divtag2, UIMAP_PracticePage.listtag, 10);
										if (listsatag.getText().trim().toLowerCase().contains(Option.trim().toLowerCase())) {

											WebElement atag = commonLibrary.isExist(listsatag, By.tagName("a"), 10);
											if (atag != null) {
												commonLibrary.clickButtonParentWithWait(atag, Option);
												Thread.sleep(5000);
											}
										}

										flag2 = true;
										break;
									}
									if (flag2)
										break;
								}
							}
							if (flag2)
								break;
						}
						if (flag2)
							break;

					}
				}
				if (flag2)
					break;
			}
			if (flag2) {
				report.updateTestLog("Select " + Option + " under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is selected under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Status.PASS);
			} else {
				report.updateTestLog("Select " + Option + " under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is not  selected under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and verify the child
	// source has * in the source name
	// a sub source
	// # Function Name : expandSourceandSelectsourcewithstar
	// # Author : Ramesh Devaraj
	// # Date Created : Jan 16
	// #*****************************************************************************************************************************

	public Practice expandSourceandSelectsourcewithstar(String linkName, String subLink, String Sublink2) {
		try {

			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);
						for (WebElement item2 : lists1) {
							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);
							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase())) {
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.expCol, 10);

								if (btn != null) {
									commonLibrary.clickButtonParentWithWait(btn, subLink);
									Thread.sleep(5000);
								}

								List<WebElement> lists2 = commonLibrary.isExistList(item, UIMAP_PracticePage.ulsource, 10);
								for (WebElement item3 : lists2) {

									WebElement itemSpan4 = commonLibrary.isExist(item3, By.tagName("span"), 10);
									String[] itemSpan5 = itemSpan4.getText().split("\n");
									if (itemSpan5[0].toLowerCase().equalsIgnoreCase(Sublink2.trim().toLowerCase())) {
										if (itemSpan5[0].toLowerCase().contains("*")) {

											flag2 = true;
											break;
										}
									}
									if (flag2)
										break;
								}
							}
							if (flag2)
								break;
						}
						if (flag2)
							break;

					}
				}
				if (flag2)
					break;
			}
			if (flag2) {
				report.updateTestLog("Verify" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "has * ", "The Doc title" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "has * ", Status.PASS);
			} else {
				report.updateTestLog("Verify" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "has * ", "The Doc title" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "has no * ", Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to verify info in Information Bubble
	// a sub source
	// # Function Name : verifyinfoinInformationBubble
	// # Author : Ramesh Devaraj
	// # Date Created : Jan 16
	// #*****************************************************************************************************************************

	public Practice verifyinfoinInformationBubble(String Header, String Title, String Details) {
		try {

			WebElement asideclass = commonLibrary.isExist(UIMAP_PracticePage.asideclass, 10);
			WebElement asideheader = commonLibrary.isExist(asideclass, UIMAP_PracticePage.asideheader, 10);

			WebElement asideform = commonLibrary.isExist(asideclass, UIMAP_PracticePage.asideform, 10);
			WebElement asidediv = commonLibrary.isExist(asideform, UIMAP_PracticePage.asidediv, 10);

			WebElement headertext = commonLibrary.isExist(asideheader, By.tagName("h1"), 10);
			String[] headertextlist = headertext.getText().split("\n");
			if (headertextlist[0].trim().toLowerCase().contains(Header.trim().toLowerCase())) {
				report.updateTestLog("Verify header details in the Information Bubble", Header + " header is present", Status.PASS);
			} else
				report.updateTestLog("Verify header details in the Information Bubble", Header + " header is not present", Status.FAIL);

			WebElement formheadr = commonLibrary.isExist(asideform, By.tagName("h1"), 10);
			if (formheadr.getText().trim().toLowerCase().contains(Title.toLowerCase())) {
				report.updateTestLog("Verify Title details in the Information Bubble", Title + " Title is present", Status.PASS);
			} else
				report.updateTestLog("Verify Title details in the Information Bubble", Title + " Title is not present", Status.FAIL);

			String[] detaillist = Details.split(";");

			List<WebElement> totalheaders = commonLibrary.isExistList(asidediv, By.tagName("span"), 20);
			for (int i = 0; i <= 4; i++) {
				for (WebElement headinglist : totalheaders) {
					String headings[] = headinglist.getText().split("\n");
					if (headings[0].trim().toLowerCase().contains(detaillist[i].toLowerCase())) {
						report.updateTestLog("Verify Details in the Information Bubble", detaillist[i] + " detail is present", Status.PASS);

					}
				}
			}

			WebElement asidefooter = commonLibrary.isExist(asideclass, UIMAP_PracticePage.asidefooter, 10);
			WebElement asideul = commonLibrary.isExist(asidefooter, UIMAP_PracticePage.asideul, 10);
			WebElement aslideli = commonLibrary.isExist(asideul, UIMAP_PracticePage.newsConLiClass, 10);
			WebElement aslideclose = commonLibrary.isExist(aslideli, UIMAP_PracticePage.aslideclose, 10);
			if (aslideclose != null) {
				commonLibrary.clickButton(aslideclose);
			}

		} catch (Exception e) {

		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source for 3 levels and add
	// as a filter
	// a sub source
	// # Function Name : expandsourcefor3levelsandaddfilter
	// # Author : Ramesh Devaraj
	// # Date Created : Jan 16
	// #*****************************************************************************************************************************

	public Practice expandsourcefor3levelsandaddfilter(String linkName, String subLink, String Sublink2) {
		try {

			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);
						Thread.sleep(5000);
					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);
						for (WebElement item2 : lists1) {
							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);
							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase())) {
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.expCol, 10);

								if (btn != null) {
									commonLibrary.clickButtonParentWithWait(btn, subLink);
									Thread.sleep(5000);
								}

								List<WebElement> lists2 = commonLibrary.isExistList(item, UIMAP_PracticePage.ulsource, 10);
								for (WebElement item3 : lists2) {

									WebElement itemSpan4 = commonLibrary.isExist(item3, By.tagName("span"), 10);
									String[] itemSpan5 = itemSpan4.getText().split("\n");
									if (itemSpan5[0].toLowerCase().equalsIgnoreCase(Sublink2.trim().toLowerCase())) {
										WebElement div = commonLibrary.isExist(item3, UIMAP_PracticePage.divtag1, 10);

										WebElement addbutton = commonLibrary.isExist(div, UIMAP_PracticePage.addtofilter, 10);
										if (addbutton != null) {
											commonLibrary.clickButtonParentWithWait(addbutton, "Add to Search");
											Thread.sleep(5000);
											flag2 = true;
											break;
										}

									}
									if (flag2)
										break;
								}
							}
							if (flag2)
								break;
						}
						if (flag2)
							break;

					}
				}
				if (flag2)
					break;
			}
			if (flag2) {
				report.updateTestLog("Verify" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "is added as a filter ", "The Doc title" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + " is added as a filter", Status.PASS);
			} else {
				report.updateTestLog("Verify" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "is added as a filter ", "The Doc title" + Sublink2 + " under the source " + subLink + " for the main source" + linkName + "is not added as a filter", Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description :Function to expand a source and select View
	// Source for
	// a sub source
	// # Function Name : expandSourceandSelectOptionViewSource
	// # Author : Ramesh Devaraj
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Practice expandSourceandSelectOptionViewSource(String linkName, String subLink, String Sublink2, String Option) {
		try {

			boolean flag2 = false;

			WebElement taxPods = commonLibrary.isExist(UIMAP_PracticePage.podContainer, 10);
			WebElement formparent = commonLibrary.isExist(taxPods, UIMAP_PracticePage.formparent, 10);
			WebElement divpod = commonLibrary.isExist(formparent, UIMAP_PracticePage.divpod, 10);
			List<WebElement> lists = commonLibrary.isExistList(divpod, UIMAP_PracticePage.Ullist, 10);

			for (WebElement item : lists) {

				WebElement item1 = commonLibrary.isExist(item, UIMAP_PracticePage.podContent, 10);
				WebElement itemSpan = commonLibrary.isExist(item1, UIMAP_PracticePage.spanPod, 10);
				String[] itemSpan1 = itemSpan.getText().split("\n");
				if (itemSpan1[0].toLowerCase().equalsIgnoreCase(linkName.trim().toLowerCase())) {
					WebElement togleBar = commonLibrary.isExist(item, By.tagName("div"), 10);
					WebElement srcColapse = commonLibrary.isExist(togleBar, UIMAP_LexisAdvanceTax.sourceCollapsed, 10);
					if (srcColapse != null) {

						commonLibrary.clickButtonParentWithWait(srcColapse, linkName);

					}
					if (subLink != "") {

						List<WebElement> lists1 = commonLibrary.isExistList(item, UIMAP_PracticePage.listtag, 10);
						for (WebElement item2 : lists1) {
							WebElement divpod2 = commonLibrary.isExist(item2, UIMAP_PracticePage.divtag1, 10);
							WebElement itemSpan2 = commonLibrary.isExist(divpod2, By.tagName("span"), 10);
							String[] itemSpan3 = itemSpan2.getText().split("\n");
							if (itemSpan3[0].toLowerCase().equalsIgnoreCase(subLink.trim().toLowerCase())) {
								WebElement togleBar2 = commonLibrary.isExist(item2, By.tagName("div"), 10);
								WebElement btn = commonLibrary.isExist(togleBar2, UIMAP_PracticePage.expCol, 10);

								if (btn != null) {
									commonLibrary.clickButtonParentWithWait(btn, subLink);

								}

								List<WebElement> lists2 = commonLibrary.isExistList(item, UIMAP_PracticePage.ulsource, 10);
								for (WebElement item3 : lists2) {

									WebElement itemSpan4 = commonLibrary.isExist(item3, By.tagName("span"), 10);
									String[] itemSpan5 = itemSpan4.getText().split("\n");
									if (itemSpan5[0].toLowerCase().equalsIgnoreCase(Sublink2.trim().toLowerCase())) {
										WebElement togleBar3 = commonLibrary.isExist(item3, By.tagName("div"), 10);
										WebElement btn1 = commonLibrary.isExist(togleBar3, UIMAP_PracticePage.subshowhide, 10);
										if (btn1 != null) {
											commonLibrary.clickButtonParentWithWait(btn1, Sublink2);

										}

										WebElement divtag = commonLibrary.isExist(item3, UIMAP_PracticePage.action1, 10);
										WebElement divtag2 = commonLibrary.isExist(divtag, UIMAP_PracticePage.actionlist, 10);
										List<WebElement> listsatag = commonLibrary.isExistList(divtag2, UIMAP_PracticePage.listtag, 10);

										for (WebElement item4 : listsatag) {

											WebElement itemSpan6 = commonLibrary.isExist(item4, By.tagName("a"), 10);
											String[] itemSpan7 = itemSpan6.getText().split("\n");

											if (itemSpan7[0].trim().toLowerCase().contains(Option.trim().toLowerCase())) {

												WebElement atag = commonLibrary.isExist(item4, By.tagName("a"), 10);
												if (atag != null) {
													commonLibrary.clickButtonParentWithWait(atag, Option);

													flag2 = true;
													break;
												}
											}
											if (flag2)
												break;
										}
									}
									if (flag2)
										break;
								}
							}
							if (flag2)
								break;
						}
						if (flag2)
							break;

					}
				}
				if (flag2)
					break;
			}
			if (flag2) {
				report.updateTestLog("Select " + Option + " under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is selected under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Status.PASS);
			} else {
				report.updateTestLog("Select " + Option + " under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Option + " is not  selected under the child source " + Sublink2 + " under source " + subLink + " for the main source" + linkName + "linkName", Status.FAIL);
			}

		}

		catch (Exception e) {

		}
		return new Practice(scriptHelper);
	}

	// #************************************************************************************************************************

	// # Function Description : Function to click on link in LPA pod
	// # Function Name : clickLinkOnPodBFSNPR
	// # Author : Prashanthini
	// # Date Created : Dec'15
	//

	// #**************************************************************************************************************************

	public LPAHome clickLinkOnPodBFSNPR1(String Link) {

		commonLibrary.sleep(10);

		WebElement parentDiv1 = commonLibrary.isExist(UIMAP_PracticePage.podParentDiv1Nv, 10);
		if (parentDiv1 != null) {
			WebElement podHeader1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podHeader1Nv, 10);
			if (podHeader1 != null) {
				WebElement podButton1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.podButton1Nv, 10);
				WebElement span1 = commonLibrary.isExist(podHeader1, UIMAP_PracticePage.expSpan1Nv, 10);
				if (podButton1.getAttribute("class").contains("Down") && span1.getText().toLowerCase().trim().contains("collapsed"))
					commonLibrary.clickButton(podButton1);
				WebElement podContentDiv1 = commonLibrary.isExist(parentDiv1, UIMAP_PracticePage.podDiv1Nv, 10);
				if (podContentDiv1 != null) {
					WebElement linkUl1 = commonLibrary.isExist(podContentDiv1, UIMAP_PracticePage.linkUlTag1, 10);
					if (linkUl1 != null) {
						WebElement verifyLink = commonLibrary.isExist(linkUl1, UIMAP_PracticePage.lnkVerify1, 10);
						if (verifyLink != null && verifyLink.getText().toLowerCase().trim().contains(Link.toLowerCase().trim())) {
							commonLibrary.clickJS(verifyLink);
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " clicked from LPA pod", Status.PASS);
						} else
							report.updateTestLog("Click on" + Link + " in LPA pod", Link + " NOT clicked from LPA pod", Status.FAIL);
					}
				}
			}
		}

		return new LPAHome(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter text in Search text box in the
	// advanced search page and click enter.
	// # Function Name : setSearchTerminASandpressEnter
	// # Author : Thangavel M
	// # Date Created : Jan -13 2016
	// #*****************************************************************************************************************************

	public Search setSearchTerminASandpressEnter(String searchTerm) throws AWTException {
		WebElement eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);

		commonLibrary.setDataInTextBox(eltSearchbox, searchTerm, "SearchTerm");

		commonLibrary.sleep(3000);// mandatory

		Robot robot = new Robot();
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(500);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(500);

		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify advanced search page is
	// displayed
	// # Function Name : verifyadvancedsearchpage
	// # Author : Thangavel M
	// # Date Created : Jan -18 2016
	// #*****************************************************************************************************************************

	public Practice verifyadvancedsearchpage() {

		WebElement divadvance = commonLibrary.isExist(UIMAP_SearchResult.divadvance, 20);
		WebElement h2advance = commonLibrary.isExist(divadvance, UIMAP_SearchResult.h2advance, 20);

		if (h2advance.getText().trim().toLowerCase().contains("advanced search"))
			report.updateTestLog("Verify whether the advanced search page is displayed", "The advanced search page is displayed", Status.PASS);
		else
			report.updateTestLog("Verify whether the advanced search page is displayed", "The advanced search page is not displayed", Status.FAIL);
		commonLibrary.sleep(3000);// mandatory

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify segment Image
	// displayed
	// # Function Name : verifysegmentImage
	// # Author : Thangavel M
	// # Date Created : Jan -19 2016
	// #*****************************************************************************************************************************

	public Practice verifysegmentImage() {

		WebElement segmentimagediv = commonLibrary.isExist(UIMAP_SearchResult.segmentimagediv, 20);
		WebElement segmentchilddiv = commonLibrary.isExist(segmentimagediv, UIMAP_SearchResult.segmentchilddiv, 20);
		WebElement segmentimage = commonLibrary.isExist(segmentchilddiv, UIMAP_SearchResult.segmentimage, 20);
		if (segmentimage != null)
			report.updateTestLog("Verify whether the segment image is displayed", "The segment image is displayed", Status.PASS);
		else
			report.updateTestLog("Verify whether the segment image is displayed", "The segment image is not displayed", Status.FAIL);
		commonLibrary.sleep(3000);// mandatory

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select content type and its sub
	// content type
	// # Function Name : selectcontenttypeandsubcontent
	// # Author : Thangavel M
	// # Date Created : Jan -19 2016
	// #*****************************************************************************************************************************

	public Practice selectcontenttypeandsubcontent(String Contenttype, String Subcontenttype) {

		boolean flag1 = false;
		boolean flag2 = false;
		WebElement divcontenttype = commonLibrary.isExist(UIMAP_SearchResult.divcontenttype, 20);
		WebElement divcategory = commonLibrary.isExist(divcontenttype, UIMAP_SearchResult.divcategory, 20);
		WebElement uisttagcontent = commonLibrary.isExist(divcategory, UIMAP_SearchResult.uisttagcontent, 20);

		List<WebElement> list = commonLibrary.isExistList(uisttagcontent, UIMAP_SearchResult.listtagcontent, 10);
		for (WebElement item : list) {

			WebElement itemtext = commonLibrary.isExist(item, By.tagName("button"), 10);

			if (itemtext.getText().toLowerCase().equalsIgnoreCase(Contenttype.trim().toLowerCase())) {

				WebElement btn = commonLibrary.isExist(item, UIMAP_SearchResult.contenttypebutton, 10);
				if (btn != null) {
					commonLibrary.clickJS(btn);

					flag1 = true;
					report.updateTestLog("Select the content type " + Contenttype, "The content type is selected", Status.PASS);

				}
			}
			if (flag1)
				break;
		}

		List<WebElement> list2 = commonLibrary.isExistList(divcategory, UIMAP_SearchResult.listtagcontent, 10);
		for (WebElement item1 : list2) {

			WebElement itemtext = commonLibrary.isExist(item1, By.tagName("button"), 10);

			if (itemtext.getText().toLowerCase().equalsIgnoreCase(Subcontenttype.trim().toLowerCase())) {

				WebElement btn = commonLibrary.isExist(item1, By.tagName("button"), 10);
				if (btn != null) {
					commonLibrary.clickJS(btn);

					flag2 = true;

					report.updateTestLog("Select the content type " + Subcontenttype, "The content type is selected", Status.PASS);

				}
			}
			if (flag2)
				break;
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select content type in advanced
	// search page
	// type
	// displayed
	// # Function Name : selectcontenttypeAS
	// # Author : Thangavel M
	// # Date Created : Jan -19 2016
	// #*****************************************************************************************************************************

	public Practice selectcontenttypeAS(String Contenttype) {

		boolean flag1 = false;

		WebElement divcontenttype = commonLibrary.isExist(UIMAP_SearchResult.divcontenttype, 20);
		WebElement divcategory = commonLibrary.isExist(divcontenttype, UIMAP_SearchResult.divcategory, 20);
		WebElement uisttagcontent = commonLibrary.isExist(divcategory, UIMAP_SearchResult.uisttagcontent, 20);

		List<WebElement> list = commonLibrary.isExistList(uisttagcontent, UIMAP_SearchResult.listtagcontent, 10);
		for (WebElement item : list) {

			WebElement itemtext = commonLibrary.isExist(item, By.tagName("button"), 10);

			if (itemtext.getText().toLowerCase().equalsIgnoreCase(Contenttype.trim().toLowerCase())) {

				WebElement btn = commonLibrary.isExist(item, By.tagName("button"), 10);
				if (btn != null) {
					commonLibrary.clickJS(btn);

					flag1 = true;
					report.updateTestLog("Select the content type " + Contenttype, "The content type is selected", Status.PASS);

				}
			}
			if (flag1)
				break;
		}

		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify applied prefilter in generic
	// search page
	// # Function Name : verifyprefiltersearch     
	// # Author : Sankar
	// # Date Created : Jan'16
	// #*****************************************************************************************************
	public Practice verifyprefiltersearch(String strprefiltersearch) {
		commonLibrary.sleep(10);
		WebElement parentdiv1 = commonLibrary.isExist(UIMAP_Home.parentdiv1, 20);
		WebElement div1 = commonLibrary.isExist(parentdiv1, UIMAP_Home.div1, 20);
		WebElement prefiltersearch = commonLibrary.isExist(div1, UIMAP_Home.prefilter, 20);

		if (prefiltersearch != null && prefiltersearch.getText().contains(strprefiltersearch))
			report.updateTestLog("Verify " + strprefiltersearch + " is displayed in BRSB in generic search page", strprefiltersearch + "is displayed in BRSB in generic search page", Status.PASS);
		else
			report.updateTestLog("Verify " + strprefiltersearch + "is displayed in BRSB in generic search page", strprefiltersearch + " is not displayed in BRSB in generic search page", Status.FAIL);
		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click home link .
	// # Function Name : clickhomelink
	// # Author : Thangavel M
	// # Date Created : Jan'16
	// #*****************************************************************************************************************************

	public Home clickhomelink() {

		WebElement homelink = commonLibrary.isExist(UIMAP_Home.homelink, 10);

		if (homelink != null) {
			commonLibrary.clickJS(homelink);
			report.updateTestLog("Click on home ", " Clicked on home ", Status.PASS);
		} else
			report.updateTestLog("Click on home ", "Not Clicked on home ", Status.FAIL);

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Frequently Asked
	// Questions in the Advanced Search page
	// # Function Name : verifyFAQ
	// # Author : Ramesh
	// # Date Created : Jan'16
	// #*************************************************************************
	public Practice verifyFAQ(String FAQlink) {
		WebElement divfaq = commonLibrary.isExist(UIMAP_Home.divfaq, 20);
		WebElement FAQ = commonLibrary.isExist(divfaq, UIMAP_Home.spanFaq, 20);
		if (FAQ != null && FAQ.getText().trim().toLowerCase().contains(FAQlink.trim().toLowerCase())) {
			report.updateTestLog("Verify 'Frequently Asked Questions' text is displayed", "'Frequently Asked Questions' text is displayed", Status.PASS);
		} else
			report.updateTestLog("Verify 'Frequently Asked Questions' text is displayed", "'Frequently Asked Questions' text is not displayed", Status.FAIL);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify About Searching link in the
	// Advanced Search page
	// # Function Name :verifyabt
	// # Author : Ramesh
	// # Date Created : Jan'16
	// #*************************************************************************
	public Practice verifyAbt(String aboutsearchlink, String linkabt) {

		WebElement abtsearch = commonLibrary.isExist(UIMAP_Home.btnclassicon, 20);
		if (abtsearch != null && abtsearch.getText().contains(aboutsearchlink)) {
			report.updateTestLog("Verify about searching is displayed", "about searching is displayed", Status.PASS);
		} else
			report.updateTestLog("Verify about searching is displayed", "about searching is not displayed", Status.FAIL);
		if (abtsearch != null) {
			if (abtsearch.getAttribute("class").contains("collapsed")) {

				abtsearch.click();
			}
		} else {
			report.updateTestLog("Verify " + abtsearch + " about searching is dsiplayed", "" + abtsearch + "about searching  is not displayed", Status.FAIL);
		}
		WebElement linktext = commonLibrary.isExist(UIMAP_Home.btndatahelpkey, 20);
		if (linktext != null && linktext.getText().contains(linkabt)) {

			report.updateTestLog("Verify 'What are the terms and connectors equivalents to WestlawNext™?' is displayed", "'What are the terms and connectors equivalents to WestlawNext™?' is displayed", Status.PASS);
		} else
			report.updateTestLog("Verify 'What are the terms and connectors equivalents to WestlawNext™?' is displayed", "'What are the terms and connectors equivalents to WestlawNext™?' is not displayed", Status.FAIL);
		return new Practice(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to select date range from date
	// dropdown
	// # Function Name :selectDateRangeFromAsf
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*************************************************************************
	public Practice selectDateRangeFromAsf(String dateRange) {
		WebElement selectDate = commonLibrary.isExist(UIMAP_PracticePage.selectDate, 20);
		if (selectDate != null) {
			commonLibrary.selectFromListOption(selectDate, dateRange);
			report.updateTestLog("Verify " + dateRange + " Has been selected in date drop down", dateRange + " Has selected in date dropdown", Status.PASS);
		} else {
			report.updateTestLog("Verify " + dateRange + " Has been selected in date drop down", dateRange + " not selected in date dropdown", Status.FAIL);
		}

		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used verify from and to text box
	// # Function Name :verifyFromAndToTextBoxDisplays
	// # Author : Aravind Russell V
	// # Date Created : Jan'16
	// #*************************************************************************
	public Practice verifyFromAndToTextBoxDisplays() {
		WebElement dateFrom = commonLibrary.isExist(UIMAP_PracticePage.dateFrom, 20);
		WebElement dateTo = commonLibrary.isExist(UIMAP_PracticePage.dateTo, 20);
		if (dateFrom != null && dateTo != null) {
			commonLibrary.isExist(UIMAP_PracticePage.dateFrom, 20);
			commonLibrary.isExist(UIMAP_PracticePage.dateTo, 20);
			report.updateTestLog("Verify FROM and TO date text box has been displayed", "FROM and TO date text box has been displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify FROM and TO date text box has been displayed", "FROM and TO date text box not displayed", Status.PASS);
		}

		return new Practice(scriptHelper);

	}

	// #*****************************************************************************************************************************
			// # Function Description : Function used verify  from  text box is displayed and set a date in from textbox
			// # Function Name :verifyFromTextBoxDisplaysandSetDate
			// # Author : Deepha H
			// # Date Created : Jan'16
			// #*************************************************************************
			public Practice verifyFromTextBoxDisplaysandSetDate(String date){
				WebElement dateFrom = commonLibrary.isExist(UIMAP_PracticePage.dateFrom, 20);
				
				if(dateFrom != null )
				{
					commonLibrary.setDataInTextBoxClear(dateFrom, date, "Date");
							
					report.updateTestLog(
							"Verify FROM text box has been displayed and set a date ",
							"FROM text box has been displayed and date is set",
							Status.PASS);
				}
				else
				{
					report.updateTestLog(
							"Verify FROM text box has been displayed and set a date ",
							"FROM text box has been displayed and date is not set",
							Status.FAIL);
				}
				
				
				return new Practice(scriptHelper);
				
			}
	

}
