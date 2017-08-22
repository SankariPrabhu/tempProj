package functionallibraries;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.cognizant.framework.Status;

import UIMAP.UIMAP_Topics;
import supportlibraries.*;

public class Topics extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);

	public Topics(ScriptHelper scriptHelper) {
		super(scriptHelper);

		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("topics"))
				commonLibrary.sleep(5000);

		} while (!url.contains("topics") && counter < 40);

		if (!driver.getCurrentUrl().contains("topics")) {
			throw new IllegalStateException("Topics page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify topic header
	// # Function Name : verifyTopicsPageHeader     
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyTopicsPageHeader(String topicName) {
		WebElement topicsHeader = commonLibrary.isExist(UIMAP_Topics.lblHeaderTitle, 10);
		// commonLibrary.highlightElement(topicsHeader);
		String headerText = topicsHeader.getText();

		// WebElement headerTitle = commonLibrary.isExist(topicsHeader,
		// UIMAP_Topics.lblHeaderTitle,10);
		// String headerSearchTerm = headerTitle.getText();

		String expHeader = "Legal Topic Search: " + topicName;
		if (headerText.trim().toUpperCase().contains(expHeader.trim().toUpperCase())) {
			report.updateTestLog("Verify if 'Legal Topic Search: " + topicName + "' is displayed in topic header", "'Legal Topic Search: " + topicName + "' is displayed in topic header", Status.PASS);
		} else {
			report.updateTestLog("Verify if 'Legal Topic Search: " + topicName + "' is displayed in topic header", "'Legal Topic Search: " + topicName + "' is not displayed in topic header", Status.FAIL);
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if practice area is displayed
	// followed by number of results enclosed in braces
	// # Function Name : verifyPracticeAreaDisplayedWithResults     
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyPracticeAreaDisplayedWithResults(String topicName) {
		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase()) && topicResultName.contains("(") && topicResultName.contains(")")) {
				int subTopicsCount = Integer.parseInt(topicResultName.substring(topicResultName.indexOf("(") + 1, topicResultName.indexOf(")")));
				if (subTopicsCount > 0) {
					report.updateTestLog("Verify if practice area '" + topicName + "' is displayed followed by number of results enclosed in braces", "Practice area '" + topicName + "' is dispalyed as " + topicResultName, Status.PASS);
				} else {
					report.updateTestLog("Verify if practice area '" + topicName + "' is displayed followed by number of results enclosed in braces", "Practice area '" + topicName + "' is not dispalyed with zero results", Status.FAIL);
				}

				break;
			} else {
				report.updateTestLog("Verify if practice area '" + topicName + "' is displayed followed by number of results enclosed in braces", "Practice area '" + topicName + "' is not dispalyed as expected", Status.FAIL);
			}
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select and Expand a Practice area
	// Topic
	// # Function Name : selectAndExpandPracticeArea     
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics selectAndExpandPracticeArea(String topicName) {
		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				commonLibrary.clickButtonLogSmallWait(topicResult, "Topic '" + topicName + "'");
				WebElement topicBreadcrumb = commonLibrary.isExist(section, UIMAP_Topics.lblTopicBreadcrumb, 10);
				int count = 0;
				do {
					count++;
					topicBreadcrumb = commonLibrary.isExist(section, UIMAP_Topics.lblTopicBreadcrumb, 10);
					if (topicBreadcrumb == null)
						commonLibrary.sleep(5000);
				} while (topicBreadcrumb == null && count <= 10);
				break;
			}
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if a particular practice area
	// is expanded
	// # Function Name : verifyIfPracticeAreaExpanded     
	// # Author : Divakar
	// # Date Created : 6 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyIfPracticeAreaExpanded(String topicName) {
		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSectionExpanded = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSectionExpanded, 10);

		for (WebElement section : topicsSectionExpanded) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				// commonLibrary.highlightElement(topicResult);
				report.updateTestLog("Verify if practice area '" + topicName + "' is expanded", "Practice area '" + topicName + "' is expanded", Status.PASS);
			} else {
				report.updateTestLog("Verify if practice area '" + topicName + "' is expanded", "Practice area '" + topicName + "' is not expanded", Status.FAIL);
			}
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if a particular practice area
	// results are displayed with breadcrumb trail
	// # Function Name : verifyIfPracticeAreaResultsWithBreadcrumb     
	// # Author : Divakar
	// # Date Created : 6 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyIfPracticeAreaResultsWithBreadcrumb(String topicName) {
		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				List<WebElement> topicBreadcrumb = commonLibrary.isExistList(section, UIMAP_Topics.lblTopicBreadcrumb, 10);
				if (topicBreadcrumb.size() > 0) {
					report.updateTestLog("Verify if practice area '" + topicName + "' results are displayed with breadcrumb trail", "Practice area '" + topicName + "' results are displayed with breadcrumb trail", Status.PASS);
				} else {
					report.updateTestLog("Verify if practice area '" + topicName + "' results are displayed with breadcrumb trail", "Practice area '" + topicName + "' results are not displayed with breadcrumb trail", Status.FAIL);
				}
				break;
			}
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if a particular practice area
	// results are displayed with 'Actions' button
	// # Function Name : verifyIfPracticeAreaResultsActionsButton     
	// # Author : Divakar
	// # Date Created : 6 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyIfPracticeAreaResultsActionsButton(String topicName) {
		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				List<WebElement> topicActions = commonLibrary.isExistList(section, UIMAP_Topics.btnTopicAction, 10);
				if (topicActions.size() > 0) {
					report.updateTestLog("Verify if practice area '" + topicName + "' results are displayed with 'Actions' button", "Practice area '" + topicName + "' results are displayed with 'Actions' button", Status.PASS);
				} else {
					report.updateTestLog("Verify if practice area '" + topicName + "' results are displayed with 'Actions' button", "Practice area '" + topicName + "' results are not displayed with 'Actions' button", Status.FAIL);
				}
				break;
			}
		}

		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click first Actions button by
	// default
	// # Function Name : clickActionsFromPracticeArea     
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics clickActionsFromPracticeArea(String topicName) {

		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				List<WebElement> topicActions = commonLibrary.isExistList(section, UIMAP_Topics.btnTopicAction, 10);
				commonLibrary.clickButtonLogSmallWait(topicActions.get(0), "Actions");

				break;
			}
		}
		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Action menu and Action menu
	// items
	// # Function Name : VerifyActionListAndActionItems     
	// # Author : Divakar
	// # Date Created : 5 Mar'15
	// #*****************************************************************************************************************************
	public Topics verifyActionListAndActionItems(String topicName, String itemsToVerify) {
		boolean topicFlag = false;

		WebElement searchResult = commonLibrary.isExist(UIMAP_Topics.listSearchResult, 10);

		List<WebElement> topicsSection = commonLibrary.isExistList(searchResult, UIMAP_Topics.listSearchSection, 10);

		for (WebElement section : topicsSection) {
			WebElement topicResult = commonLibrary.isExist(section, UIMAP_Topics.btnSearchResult, 10);

			String topicResultName = topicResult.getText();
			if (topicResultName.toUpperCase().contains(topicName.toUpperCase())) {
				topicFlag = true; // Topic name matched
				boolean isMenuListDisplayed = false;

				// Verify if Actions Menu List is displayed
				List<WebElement> actionsMenuList = commonLibrary.isExistList(section, UIMAP_Topics.listActionsMenu, 10);
				WebElement actionsMenu = actionsMenuList.get(0);
				if (actionsMenu.isDisplayed()) {
					isMenuListDisplayed = true;
					// commonLibrary.highlightElement(actionsMenu);
					report.updateTestLog("Verify if 'Actions' Menu is displayed", "'Actions' Menu is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify if 'Actions' Menu is displayed", "'Actions' Menu is not displayed", Status.FAIL);
				}

				// Verify if individual action items are displayed
				String[] actionItemstoVerify = itemsToVerify.split(";");

				if (isMenuListDisplayed && (actionItemstoVerify.length > 0)) // Check
																				// if
																				// Actions
																				// Menu
																				// displayed
																				// to
																				// proceed
				{
					List<WebElement> actionItems = commonLibrary.isExistList(actionsMenu, UIMAP_Topics.listActionItems, 10);

					for (String actionItemtoVerify : actionItemstoVerify) {
						boolean itemFlag = false;
						for (WebElement actionItem : actionItems) {
							if (actionItemtoVerify.toUpperCase().equals(actionItem.getText().toUpperCase())) {
								itemFlag = true;
								break;
							}
						}
						if (itemFlag)
							report.updateTestLog("Verify if " + actionItemtoVerify + " option from 'Actions' menu is available", actionItemtoVerify + " option from 'Actions' menu is available", Status.PASS);
						else
							report.updateTestLog("Verify if " + actionItemtoVerify + " option from 'Actions' menu is available", actionItemtoVerify + " option from 'Actions' menu is not available", Status.FAIL);
					}
				}
			}
			if (topicFlag) // break once topic name is reached
				break;
		}
		return new Topics(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout of the application
	// # Function Name : logout     
	// # Author : Shobana
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}
}