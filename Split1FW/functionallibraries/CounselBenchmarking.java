package functionallibraries;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_CounselBenchmarking;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_ResearchMap;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;
import UIMAP.UIMAP_VSASearchResults;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import supportlibraries.*;

public class CounselBenchmarking extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	public int resultCount;
	public String filename;
	private PageCheck pageCheck = new PageCheck(scriptHelper);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private GeneralFunctions generalFunctions = new GeneralFunctions(
			scriptHelper);

	public CounselBenchmarking(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("counselbenchmarkingreport"))
				commonLibrary.sleep(5000);

		} while (!url.contains("counselbenchmarkingreport") && counter < 40);

		if (!driver.getCurrentUrl().contains("counselbenchmarkingreport")) {
			throw new IllegalStateException(
					"CounselBenchmarking page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on links in left hand pane in
	// CB
	// # Function Name: SelectlinksinCB    
	// # Author : Ram Prasath
	// # Date Created : 20 - Apr'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking selectlinksinCB(String linkname) {
		WebElement parentlink = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.cbLeftpanelinks, 10);
		List<WebElement> lilist = commonLibrary.isExistList(parentlink,
				UIMAP_CounselBenchmarking.lilist, 10);
		for (WebElement item1 : lilist) {
			if (item1.getText().equalsIgnoreCase(linkname)) {
				WebElement click = commonLibrary.isExist(
						UIMAP_CounselBenchmarking.buttontag, 10);
				if (browsername.contains("internet")) {
					commonLibrary.clickButtonParentWithWaitJS(click, linkname);
				} else
					commonLibrary.clickButtonParentWithWait(click, linkname);
				break;
			}
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary
				.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			if ((browsername.contains("internet")))
				commonLibrary.clickMethod(btnMore, "More");
			else
				commonLibrary.clickMethod(btnMore, "More");
		}

		WebElement lnkSignOut = commonLibrary.isExist(
				By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut != null)
			if ((browsername.contains("internet")))
				commonLibrary.clickJS(lnkSignOut, "Sign Out");
			else
				commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut,
						"Sign Out");

		WebElement btnIdLogin = commonLibrary.isExistNegative(
				UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null
				&& driver.getCurrentUrl().toLowerCase()
						.contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is NOT displayed",
					Status.DONE);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link councebench
	// # Function Name : clicklinks_councelbench
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking clicklinks_councelbench(String Stlink) {

		WebElement clicklnk = commonLibrary
				.isExist(UIMAP_Home.counsellink, 100);
		List<WebElement> lists = commonLibrary.isExistList(clicklnk,
				By.tagName("li"), 10);

		for (WebElement list : lists) {
			WebElement btnCondentType = commonLibrary.isExist(list,
					By.tagName("button"), 20);

			if (btnCondentType.getText().equalsIgnoreCase(Stlink)) {
				commonLibrary.clickLinkWithWebElementWithWait(btnCondentType,
						btnCondentType.getText());
				break;
			}
		}
		WebElement resultContent = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ulheadertitle, 10);
		int count = 0;
		do {
			count++;
			resultContent = commonLibrary.isExist(
					UIMAP_CounselBenchmarking.ulheadertitle, 10);
			if (resultContent == null) {
				commonLibrary.sleep(5000);
			}
		} while (resultContent == null && count < 10);
		commonLibrary.sleep(5000);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to click link print
	// # Function Name : click_print_usedefaultsettings
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking click_print_usedefaultsettings(String Strlink) {

		// WebElement clicklnk =
		// commonLibrary.isExist(UIMAP_Home.couselbencprint, 100);
		// if (clicklnk != null) {
		//
		// commonLibrary.clickButton_Parent(clicklnk, "Print");
		// }
		// WebElement usedefaultsettings =
		// commonLibrary.isExist_Negative(UIMAP_Home.printdropdown, 20);
		//
		// if (usedefaultsettings != null)
		// commonLibrary.clickButton_Parent_WithWait(usedefaultsettings,
		// "Use default settings");

		generalFunctions.clickDeliverySelectOption("print", "");
		commonLibrary.sleep(5000);

		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to lexis get
	// # Function Name : NavigateToLexisGetandPrint
	// # Author : Aravind
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public GetandprintExperience navigateToLexisGetandPrint() {

		WebElement btnIdLexisAdvance = commonLibrary.isExist(
				UIMAP_Home.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance,
					"Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(
				UIMAP_Home.btnActionGetandprint, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking,
					"LexisNexis® Get & Print");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking,
					"LexisNexis® Get & Print");

		commonLibrary.sleep(5000);

		commonLibrary.switchToWindow("GetAndPrintExperience");
		WebElement CurrentProduct = commonLibrary.isExist(
				By.cssSelector("div[class='widget-container']"), 20);
		if (CurrentProduct != null) {
			List<WebElement> CurrentProduct1 = commonLibrary.isExistList(
					CurrentProduct, By.tagName("h1"), 20);
			if (CurrentProduct1.get(0).getText()
					.contains("Lexis Advance® Get & Print")) {
				report.updateTestLog(
						"Verify Lexis Get and print advisor page is displayed",
						"Lexis Get and print advisor page is displayed",
						Status.PASS);
			} else {
				report.updateTestLog(
						"Verify Lexis Get and print advisor page is displayed",
						"Lexis Get and print advisor page is not displayed",
						Status.FAIL);
			}
		}
		return new GetandprintExperience(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to set client id
	// # Function Name : setClientID
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking setClientID(String clientId) {
		WebElement newClientId = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.newClientId, 10);
		if (newClientId != null) {
			commonLibrary.setRadioButton(newClientId, "New Client ID Radio");
		}

		WebElement clientIdValue = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ClientId, 10);
		commonLibrary.setDataInTextBox(clientIdValue, clientId, "Client ID");

		WebElement setClientId = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.setClientId, 10);
		if (browsername.toLowerCase().contains("internet"))
			commonLibrary.clickButtonParentWithWaitJS(setClientId,
					"Set Client ID");
		else
			commonLibrary.clickButtonParentWithWait(setClientId, "Set Client ID");
			commonLibrary.clickButtonParentWithWait(setClientId,
					"Set Client ID");

		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to client link
	// # Function Name : NavigateToClientLink
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public ClientID navigateToClientLink(String strLinkName) {
		WebElement btnClassClient = commonLibrary.isExist(
				UIMAP_Home.btnClassClient, 20);
		commonLibrary.clickButtonParentWithWaitJS(btnClassClient, "Client");

		if (strLinkName.equalsIgnoreCase("Set/Add Client ID")) {
			WebElement lnkTxtSetAddClientID = commonLibrary.isExist(
					UIMAP_Home.btnActionSetAddClientID, 20);
			commonLibrary.clickJS(lnkTxtSetAddClientID, "Set/Add Client ID");
		} else if (strLinkName.equalsIgnoreCase("-None-")) {
			WebElement lnkTxtNone = commonLibrary.isExist(
					UIMAP_Home.lnkTxtNone, 20);
			commonLibrary.clickJS(lnkTxtNone, "None");
		}

		return new ClientID(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function toadd folders
	// # Function Name : AddToFolder
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking addToFolder(String folderName)

	{
		// CLICK ON <<<ADD TO FOLDER>>>
		commonLibrary.sleep(10000);
		WebElement addtoFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.addFolder, 10);
		if (addtoFolder != null)
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(addtoFolder,
						"Add To Folder");
			else
				commonLibrary.clickButtonParentWithWait(addtoFolder, "Add To Folder");

		commonLibrary.sleep(10000);
		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.chooseFolder, 10);
		if (chooseFolder != null)
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(chooseFolder,
						"Choose Folder");
			else
				commonLibrary.clickButtonParentWithWait(chooseFolder,
						"Choose Folder");
		commonLibrary.sleep(10000);
		// CLICK ON <<<CREATE NEW FOLDER>>>

		WebElement createNewFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createNewFolder, 10);
		if (createNewFolder != null)
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(createNewFolder,
						"Create New Folder");
			else
				commonLibrary.clickButtonParentWithWait(createNewFolder,
						"Create New Folder");

		// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

		WebElement folderNam = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.folderName, 10);
		if (folderNam != null)
			commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

		// CLICK ON <<<CREATE>>>
		WebElement create = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createFolder, 10);
		if (create != null)
			if (browsername.toLowerCase().contains("internet"))
				commonLibrary.clickButtonParentWithWaitJS(create, "Create");
			else
				commonLibrary.clickButtonParentWithWait(create, "Create");
		commonLibrary.sleep(100000);

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.sleep(10000);
		if (browsername.toLowerCase().contains("internet"))
			commonLibrary.clickButtonParentWithWaitJS(saveFolder, "Save");
		else
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");
		commonLibrary.sleep(10000);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to folders
	// # Function Name : navigateToFolders
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public WorkFolders navigateToFolders() {
		try {
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore,
					100);
			if (btnMore != null)
				commonLibrary.clickMethod(btnMore, "More");

			WebElement folder = commonLibrary.isExist(
					UIMAP_ResearchMap.foldersMore, 10);
			if (folder == null) {
				btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
				if (btnMore != null) {
					// commonLibrary.highlightElement(btnMore);
					if ((browsername.contains("internet")))
						commonLibrary.clickJS(btnMore, "More");
					else
						commonLibrary.clickLinkWithWebElementWithWait(btnMore,
								"More");
				}
				folder = commonLibrary.isExist(UIMAP_ResearchMap.foldersMore,
						10);
			}

			if (folder != null) {
				commonLibrary
						.clickLinkWithWebElementWithWait(folder, "Folders");
				commonLibrary.sleep(6000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

		return new WorkFolders(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to change the client id
	// # Function Name : changeClientId
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking changeClientId(String clientID) {
		WebElement pageHeading = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.pageHeading, 10);
		if (pageHeading.getText().equalsIgnoreCase("Choose a Client ID")) {
			report.updateTestLog("Change Client Id page is displayed",
					"Change Client Id page is displayed", Status.PASS);
			List<WebElement> viewUsing = commonLibrary.isExistList(
					UIMAP_CounselBenchmarking.viewUsing, 10);
			for (WebElement item : viewUsing) {
				if (item.getAttribute("value").contains(clientID)) {
					if (browsername.toLowerCase().contains("internet"))
						commonLibrary.clickButtonParentWithWaitJS(item, item.getAttribute("value"));
					else
						commonLibrary.clickButtonParentWithWait(item, item.getAttribute("value"));
					break;
				}
			}
		} else
			report.updateTestLog("Change Client Id page is displayed",
					"Change Client Id page is displayed", Status.PASS);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify content header content
	// # Function Name : verifyCategoryHeadContent
	// # Author : Shobana
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifyCategoryHeadContent(String header,
			String content) {
		WebElement heading = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.categoryHead, 10);
		if (heading.getText().trim().equalsIgnoreCase(header)) {
			report.updateTestLog("Counsel BenchMarking page displays with "
					+ header + " category",
					"Counsel BenchMarking page displays with " + header
							+ " category", Status.PASS);
			WebElement resultContent = commonLibrary.isExist(
					UIMAP_CounselBenchmarking.resultContent, 10);
			String actualContent = resultContent.getAttribute("id");
			boolean flag = false;
			switch (content) {
			case "Hourly Rates": {
				if (actualContent.equalsIgnoreCase("hourRate"))
					flag = true;
				break;
			}
			case "Hourly Rates Comparison": {
				if (actualContent.equalsIgnoreCase("matterCompare"))
					flag = true;
				break;
			}
			case "Hourly Rates Trends": {
				if (actualContent.equalsIgnoreCase("hourlyratestrends"))
					flag = true;
				break;
			}
			case "Matter Statistics": {
				if (actualContent.equalsIgnoreCase("statistic"))
					flag = true;
				break;
			}
			case "Alternative Fee Arrangements": {
				if (actualContent.equalsIgnoreCase("feeArrg"))
					flag = true;
				break;
			}
			}
			if (flag)
				report.updateTestLog(
						"Full View of the counsel BenchMarking report "
								+ content + " displays",
						"Full View of the counsel BenchMarking report "
								+ content + " displays", Status.PASS);
			else
				report.updateTestLog(
						"Full View of the counsel BenchMarking report "
								+ content + " displays",
						"Full View of the counsel BenchMarking report "
								+ content + " is not displayed", Status.FAIL);

		} else
			report.updateTestLog("Counsel BenchMarking page displays with "
					+ header + " category",
					"Counsel BenchMarking page is not displayed with " + header
							+ " category", Status.FAIL);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the filters in left pane of
	// counsel benchmarking
	// # Function Name: selectPostFilter    
	// # Author : karthi
	// # Date Created : 07 May '15
	// #*****************************************************************************************************************************

	public CounselBenchmarking selectPostFilter(String strHeader,
			String strFilter) {
		if (!(strHeader.equals(" ") && strFilter.equals(" "))) {
			List<WebElement> eltFilter = null;
			int i = 0, j = 0;
			boolean Flag = false;
			WebElement filterContainer = commonLibrary.isExistNegative(
					UIMAP_CounselBenchmarking.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary
					.isExistList(filterContainer,
							UIMAP_CounselBenchmarking.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(
					filterContainer,
					UIMAP_CounselBenchmarking.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				// System.out.println(suplementalFilters.get(i));
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase()
						.contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class")
							.contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(
									filterHeader.get(i), strHeader);
						else
							commonLibrary.clickLinkWithWebElementWithWait(
									filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: "
								+ strHeader, strHeader
								+ " filter Header Expanded.", Status.DONE);
					}
					System.out.println("i" + i);
					System.out.println("j" + j);
					List<WebElement> moreOptions = commonLibrary.isExistList(
							suplementalFilters.get(i - j),
							UIMAP_CounselBenchmarking.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().contains("More")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button,
										"More in Postfilter " + strHeader);
							else
								commonLibrary.clickButtonLogSmallWait(button,
										"More in Postfilter " + strHeader);
						}
					}
					List<WebElement> filters = commonLibrary.isExistList(
							suplementalFilters.get(i - j),
							UIMAP_CounselBenchmarking.eltFilterList, 20);
					for (WebElement item : filters) {
						if (item.getText().contains(strFilter)) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonParentWithWaitJS(item,
										strFilter + " in Postfilter "
												+ strHeader);
							else
								commonLibrary.clickButtonParentWithWait(item,
										strFilter + " in Postfilter "
												+ strHeader);
							try {
								String loadProp = properties
										.getProperty("xSpinner");
								int count = 0;
								WebElement loader = commonLibrary
										.isExistNegative(By.xpath(loadProp), 5);
								do {
									commonLibrary.sleep(10000);
									loader = commonLibrary.isExistNegative(
											By.xpath(loadProp), 5);
									count++;
								} while (loader != null && count < 15);
							} catch (Exception e) {
								System.out.println(e.toString());
							}
							report.updateTestLog("Selecting Filter: "
									+ strFilter, strFilter
									+ " Filter is Selected in Postfilter "
									+ strHeader, Status.DONE);
							Flag = true;
							break;
						}
					}
				}
				if (Flag)
					break;
			}

			if (!Flag) {
				eltFilter = commonLibrary.isExistList(
						UIMAP_SearchResult.eltFilterList1, 10);
				for (i = 0; i < eltFilter.size(); i++) {
					if (eltFilter.get(i).getText().toUpperCase()
							.contains(strFilter.toUpperCase())) {
						commonLibrary.clickLinkWithWebElementWithWait(
								eltFilter.get(i), eltFilter.get(i).getText());
						report.updateTestLog("Selecting Filter: " + strFilter,
								"Required Filter Selected.", Status.DONE);
						Flag = true;
						break;
					}
				}
			}
		} else
			report.updateTestLog("Selecting Filter: " + strFilter,
					"No Filter Selected.", Status.DONE);

		return new CounselBenchmarking(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear the filters
	// # Function Name: VerifypostfilterText    
	// # Author : karthi
	// # Date Created : 07 May '15
	// #*****************************************************************************************************************************

	public CounselBenchmarking clear_Filters() {
		WebElement btnClear = commonLibrary.isExist(
				UIMAP_SearchResult.btnClear, 10);
		if (btnClear != null)
			commonLibrary.clickButtonParentWithWait(btnClear, "Clear");
		report.updateTestLog("Verify whether filters cleared ",
				"Filters cleared.", Status.DONE);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filtered text in the
	// counsel benchmarking
	// # Function Name: VerifypostfilterText    
	// # Author : karthi
	// # Date Created : 07 May '15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifypostfilterText(String verifytext) {
		WebElement filterheader = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ulheadertitle, 10);
		WebElement filterli = commonLibrary.isExist(filterheader,
				UIMAP_CounselBenchmarking.liclasstitle, 10);
		WebElement filterheaderlist = commonLibrary.isExist(filterli,
				UIMAP_CounselBenchmarking.header2, 10);
		if (filterheaderlist.getText().equalsIgnoreCase(verifytext)) {
			report.updateTestLog("Selecting the Filter: " + verifytext,
					"Expected filter text is displayed.", Status.PASS);
		} else {
			report.updateTestLog("Selecting the Filter: " + verifytext,
					"Expected filter text  is not displayed.", Status.FAIL);
		}

		return new CounselBenchmarking(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the
	// "Insufficient Benchmark Rates" error message in counsel benchmarking
	// # Function Name: verifyerrormessage    
	// # Author : karthi
	// # Date Created : 07 May '15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifyerrormessage(String verifytext1,
			String verifytext2) {

		WebElement filterContainerheader = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.liclass, 10);

		WebElement errormsg1 = commonLibrary.isExist(filterContainerheader,
				UIMAP_CounselBenchmarking.header2, 10);
		WebElement errormsg2 = commonLibrary.isExist(filterContainerheader,
				UIMAP_CounselBenchmarking.ptag, 10);
		if (errormsg1.getText().equalsIgnoreCase(verifytext1)
				&& errormsg2.getText().equalsIgnoreCase(verifytext2)) {
			report.updateTestLog("Verify the inline message " + verifytext1
					+ "\n" + verifytext2 + " displays", "The inline message "
					+ verifytext1 + "\n" + verifytext2 + " is displayed",
					Status.PASS);
		} else {
			report.updateTestLog("Verify the inline message " + verifytext1
					+ "\n" + verifytext2 + " displays",
					"Expected inline Message is not displayed", Status.FAIL);
		}

		return new CounselBenchmarking(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the sub category in the HRC
	// sublist
	// # Function Name: selectHRCsubtabs    
	// # Author : karthi
	// # Date Created : 08 May '15
	// #*****************************************************************************************************************************
	public CounselBenchmarking selectHRCsubtabs(String selecttab) {

		boolean blng = false;
		WebElement ulsublist = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ulsub, 10);

		List<WebElement> sublists = commonLibrary.isExistList(ulsublist,
				UIMAP_CounselBenchmarking.lilist, 20);
		for (WebElement sublist : sublists) {
			WebElement subbtn = commonLibrary.isExistNegative(sublist,
					UIMAP_CounselBenchmarking.buttontag, 20);

			if (subbtn.getText().equals(selecttab)) {
				commonLibrary.clickButtonParentWithWaitJS(subbtn, selecttab);
				blng = true;
				break;
			}

		}
		if (!blng)
			report.updateTestLog("Select subcategory: " + selecttab, selecttab
					+ " subcategory is not selected.", Status.FAIL);
		pageCheck.ajaxWait(driver);
		return new CounselBenchmarking(scriptHelper);

	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify cb filter tabs
	// # Function Name : verifyCBFilterTabs
	// # Author : Aravind
	// # Date Created : May'15
	// #******************************************************************************************************************************
	public CounselBenchmarking verifyCBFilterTabs(String strHeader,
			String contentToVerify, int expectedCount) {
		boolean flg = false;
		String[] contentToVerifyEach = contentToVerify.split(",");
		if (!(strHeader.equals(" "))) {
			int i = 0, j = 1;
			WebElement filterContainer = commonLibrary.isExistNegative(
					UIMAP_SearchResult.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary.isExistList(
					filterContainer, UIMAP_SearchResult.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary
					.isExistList(filterContainer,
							UIMAP_SearchResult.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 2;
				if (filterHeader.get(i).getText().toUpperCase()
						.contains(strHeader.toUpperCase())) {
					report.updateTestLog("Filter Tab: " + strHeader, strHeader
							+ " filter tab present.", Status.DONE);
					if (!(contentToVerify.toLowerCase().equals("ignore"))) {
						if (contentToVerify.length() > 0) {
							if (filterHeader.get(i).getAttribute("class")
									.contains("collapsed")) {
								if (browsername.toLowerCase().contains(
										"internet"))
									commonLibrary.clickButtonParentWithWaitJS(
											filterHeader.get(i), strHeader);
								else
									commonLibrary
											.clickLinkWithWebElementWithWait(
													filterHeader.get(i),
													strHeader);
								report.updateTestLog(
										"Expanding Filter Header: " + strHeader,
										strHeader + " filter Header Expanded.",
										Status.DONE);
							}

							List<WebElement> moreOptions = commonLibrary
									.isExistList(suplementalFilters.get(i - j),
											UIMAP_SearchResult.filterMore, 10);
							for (WebElement button : moreOptions) {
								if (button.getText().toLowerCase()
										.contains("more")) {
									if (browsername.toLowerCase().contains(
											"internet"))
										commonLibrary.clickButtonLogSmallWait(
												button, "More");
									else
										commonLibrary.clickButtonLogSmallWait(
												button, "More");
								}
							}

							List<WebElement> filters = commonLibrary
									.isExistList(suplementalFilters.get(i - j),
											UIMAP_SearchResult.eltFilterList,
											20);
							int count = 0;
							for (WebElement item : filters) {
								for (int chk = 0; chk < contentToVerifyEach.length; chk++) {
									String toChk = item.getText();
									if (toChk.substring(0, toChk.length() - 3)
											.contains(contentToVerifyEach[chk])) {
										count++;
									}
									if (count >= expectedCount) {
										report.updateTestLog(
												"Verifying filter header: "
														+ strHeader
														+ " content",
												strHeader
														+ "  filter Header contains almost all the expected content.",
												Status.DONE);
										flg = true;
										break;
									}
									if (flg)
										break;
								}
								if (flg)
									break;
							}
						}
					}
				}
			}
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify cb fault filter
	// # Function Name : verifyCBDefaultHeader
	// # Author : Aravind
	// # Date Created : May'15
	// #******************************************************************************************************************************
	public CounselBenchmarking verifyCBDefaultHeader(String strHeaderContent) {
		WebElement eltDefaultHeaderParent = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.eltMatterCompareContent, 10);
		if (eltDefaultHeaderParent != null) {
			WebElement eltHeaderContent = commonLibrary.isExistNegative(
					eltDefaultHeaderParent,
					UIMAP_CounselBenchmarking.eltHeader, 10);
			if (eltHeaderContent.getText().toLowerCase()
					.contains(strHeaderContent.toLowerCase()))
				report.updateTestLog("Verify header content",
						"Header contains expected content.", Status.DONE);
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify dash board reports
	// # Function Name : verifyDashboardReports
	// # Author : Aravind
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to client link
	// # Function Name : NavigateToClientLink
	// # Author : Shobana
	// # Date Created : May'15
	// #*************************************************************************a****************************************************
	public CounselBenchmarking verifyDashboardReports(String rowTitle,
			String rowDataToCompare, String whereToVerify) {
		String[] rowTitleEach = rowTitle.split(";");
		int countOfRowTitleEach = rowTitleEach.length;
		int countForTitle = 0;

		String[] rowDataToCompareEach = rowDataToCompare.split(";");
		int countOfRowDataToCompareEach = rowDataToCompareEach.length;
		int countForRowDataToCompare = 0;

		WebElement verifyGraphExists = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.graphExists, 10);
		if (verifyGraphExists != null)
			report.updateTestLog("Verify graph exists", "Graph Exists.",
					Status.PASS);

		WebElement eltTableDiv = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.eltTableDiv, 10);
		if (eltTableDiv != null) {
			WebElement eltTableHeader = commonLibrary.isExistNegative(
					UIMAP_CounselBenchmarking.eltTableHeader, 10);
			if (eltTableHeader != null) {
				if (whereToVerify == "Head") {
					WebElement eltTableData = commonLibrary.isExistNegative(
							UIMAP_CounselBenchmarking.eltTableHeader, 10);
					if (eltTableData != null) {
						WebElement eltTableDataChk = commonLibrary
								.isExistNegative(
										UIMAP_CounselBenchmarking.eltTableData,
										10);
						List<WebElement> eltTableRowTitle = commonLibrary
								.isExistList(
										eltTableDataChk,
										UIMAP_CounselBenchmarking.eltTableRowTitle,
										10);
						for (int i = 0; i < eltTableRowTitle.size(); i++) {
							for (int j = 0; j < rowTitleEach.length; j++) {
								if (eltTableRowTitle
										.get(i)
										.getText()
										.toLowerCase()
										.contains(rowTitleEach[j].toLowerCase())) {
									countForTitle++;
								}
							}
						}

						List<WebElement> eltTableRowDataAll = commonLibrary
								.isExistList(
										eltTableData,
										UIMAP_CounselBenchmarking.eltTableRowDataAll,
										10);
						for (int i = 0; i < eltTableRowDataAll.size(); i++) {
							for (int j = 0; j < rowDataToCompareEach.length; j++) {
								if (eltTableRowDataAll
										.get(i)
										.getText()
										.toLowerCase()
										.contains(
												rowDataToCompareEach[j]
														.toLowerCase())) {
									countForRowDataToCompare++;
								}
							}
						}
					}
				} else {
					WebElement eltTableData = commonLibrary.isExistNegative(
							UIMAP_CounselBenchmarking.eltTableData, 10);
					if (eltTableData != null) {
						List<WebElement> eltTableRowTitle = commonLibrary
								.isExistList(
										eltTableData,
										UIMAP_CounselBenchmarking.eltTableRowTitle,
										10);
						for (int i = 0; i < eltTableRowTitle.size(); i++) {
							for (int j = 0; j < rowTitleEach.length; j++) {
								if (eltTableRowTitle
										.get(i)
										.getText()
										.toLowerCase()
										.contains(rowTitleEach[j].toLowerCase())) {
									countForTitle++;
								}
							}
						}

						List<WebElement> eltTableRowDataAll = commonLibrary
								.isExistList(
										eltTableData,
										UIMAP_CounselBenchmarking.eltTableRowDataAll,
										10);
						for (int i = 0; i < eltTableRowDataAll.size(); i++) {
							for (int j = 0; j < rowDataToCompareEach.length; j++) {
								if (eltTableRowDataAll
										.get(i)
										.getText()
										.toLowerCase()
										.contains(
												rowDataToCompareEach[j]
														.toLowerCase())) {
									countForRowDataToCompare++;
								}
							}
						}
					}
				}
			}
		}
		if (countForTitle == countOfRowTitleEach)
			report.updateTestLog("Verify row title " + rowTitle + " exists",
					rowTitle + " exists as expected.", Status.PASS);

		if (countForRowDataToCompare == countOfRowDataToCompareEach)
			report.updateTestLog("Verify row data " + rowDataToCompare
					+ " exists", rowDataToCompare + " exists as expected.",
					Status.PASS);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to click next arrow button in dashboard
	// # Function Name : clickNextArrowInDashboard
	// # Author : Aravind
	// # Date Created : May'15
	// #*************************************************************************(****************************************************
	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to client link
	// # Function Name : NavigateToClientLink
	// # Author : Shobana
	// # Date Created : May'15
	// #*************************************************************************a****************************************************
	public CounselBenchmarking clickNextArrowInDashboard() {
		WebElement btnNext = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.btnNxt, 10);
		if (btnNext != null)
			commonLibrary.clickButtonParentWithWait(btnNext, btnNext.getText());
		report.updateTestLog(
				"Verify whether next button in the dashboard is clicked",
				"Next button clicked.", Status.DONE);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify whether graph exits or not
	// # Function Name : verifyGraphExists
	// # Author : Aravind
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to client link
	// # Function Name : NavigateToClientLink
	// # Author : Shobana
	// # Date Created : May'15
	// #*************************************************************************a****************************************************
	public CounselBenchmarking verifyGraphExists() {
		WebElement verifyGraphExists = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.graphExists, 10);
		if (verifyGraphExists != null)
			report.updateTestLog("Verify graph exists", "Graph Exists.",
					Status.PASS);
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to verify filter counts
	// # Function Name : verifyfiltercounts
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the filter count in the left
	// pane
	// # Function Name: verifyfiltercounts    
	// # Author : Ram Prasath
	// # Date Created : 10-May-15
	// #*****************************************************************************************************************************

	public CounselBenchmarking verifyfiltercounts(String strHeader) {
		if (!(strHeader.equals(" "))) {

			int i = 0, j = 1;

			WebElement filterContainer = commonLibrary.isExistNegative(
					UIMAP_CounselBenchmarking.filterContainer, 10);
			List<WebElement> filterHeader = commonLibrary
					.isExistList(filterContainer,
							UIMAP_CounselBenchmarking.filterHeader, 10);

			List<WebElement> suplementalFilters = commonLibrary.isExistList(
					filterContainer,
					UIMAP_CounselBenchmarking.supplementalFilters, 10);
			for (i = 0; i < filterHeader.size(); i++) {
				// System.out.println(suplementalFilters.get(i));
				if (filterHeader.get(i).getText().contains("Timeline"))
					j = 1;
				if (filterHeader.get(i).getText().toUpperCase()
						.contains(strHeader.toUpperCase())) {

					if (filterHeader.get(i).getAttribute("class")
							.contains("collapsed")) {
						if (browsername.toLowerCase().contains("internet"))
							commonLibrary.clickButtonParentWithWaitJS(
									filterHeader.get(i), strHeader);
						else
							commonLibrary.clickLinkWithWebElementWithWait(
									filterHeader.get(i), strHeader);
						report.updateTestLog("Expanding Filter Header: "
								+ strHeader, strHeader
								+ " filter Header Expanded.", Status.DONE);
					}
					System.out.println("i" + i);
					System.out.println("j" + j);
					List<WebElement> moreOptions = commonLibrary.isExistList(
							suplementalFilters.get(i - j),
							UIMAP_CounselBenchmarking.filterMore, 10);
					for (WebElement button : moreOptions) {
						if (button.getText().contains("More")) {
							if (browsername.toLowerCase().contains("internet"))
								commonLibrary.clickButtonLogSmallWait(button,
										"More");
							else
								commonLibrary.clickButtonLogSmallWait(button,
										"More");
						}
					}
					List<WebElement> lioptions = commonLibrary.isExistList(
							suplementalFilters.get(i - j),
							UIMAP_CounselBenchmarking.lilabel, 10);
					int filtercount = lioptions.size();
					if (filtercount == 51 || filtercount == 15) {
						report.updateTestLog("Size of " + strHeader
								+ " Filter is verified", filtercount
								+ "are Displayed", Status.PASS);
					} else {
						report.updateTestLog("Size of " + strHeader
								+ " Filter is verified", filtercount
								+ "are Displayed", Status.FAIL);
					}

				}
			}
		}

		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to click on deliver options
	// # Function Name : clickOnDeliveryOptions
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Delivery Options
	// # Function Name : clickOnDeliveryOptions
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking clickOnDeliveryOptions(String optionName) {

		WebElement AddFolder = commonLibrary.isExist(
				UIMAP_Document.btnAddFolder, 20);
		if (AddFolder != null)
			report.updateTestLog("Verify Delivery icons",
					" Add to folder icon with drop down is displayed",
					Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons",
					"Add to folder icon with drop down is not displayed",
					Status.FAIL);

		WebElement DeliveryIcon = commonLibrary.isExist(
				UIMAP_Document.lnkDeliveryDownload, 20);
		if (DeliveryIcon != null)
			report.updateTestLog("Verify Delivery icons",
					"delivery icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons",
					"delivery icon is not displayed", Status.FAIL);
		WebElement EmailIcon = commonLibrary.isExist(UIMAP_Document.btnEmail,
				20);
		if (EmailIcon != null)
			report.updateTestLog("Verify Delivery icons",
					"Email icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons",
					"Email icon is ot displayed", Status.FAIL);
		WebElement PrintIcon = commonLibrary.isExist(UIMAP_Document.lnkPrint,
				20);
		if (PrintIcon != null)
			report.updateTestLog("Verify Delivery icons",
					"Print icon is displayed", Status.PASS);
		else
			report.updateTestLog("Verify Delivery icons",
					"Print icon is not displayed", Status.FAIL);

		switch (optionName) {
		case "Add To Folder": {
			if (browsername.contains("internet")) {
				commonLibrary
						.clickButtonParentWithWaitJS(AddFolder, optionName);
			} else
				commonLibrary.clickButtonParentWithWait(AddFolder, optionName);
			break;
		}
		case "Print": {
			// if (browsername.contains("internet")) {
			// commonLibrary.clickButton_Parent_WithWait_JS(PrintIcon,
			// optionName);
			// } else
			// commonLibrary.clickButton_Parent_WithWait(PrintIcon, optionName);
			generalFunctions.clickDeliverySelectOption("print", "");
			break;
		}
		case "Download": {
			// if (browsername.contains("internet")) {
			// commonLibrary.clickButton_Parent_WithWait_JS(DeliveryIcon,
			// optionName);
			// } else
			// commonLibrary.clickButton_Parent_WithWait(DeliveryIcon,
			// optionName);
			generalFunctions.clickDeliverySelectOption("delivery",
					"downloadnow");
			break;
		}
		case "Email": {
			// if (browsername.contains("internet")) {
			// commonLibrary.clickButton_Parent_WithWait_JS(EmailIcon,
			// optionName);
			// } else
			// commonLibrary.clickButton_Parent_WithWait(EmailIcon, optionName);
			generalFunctions.clickDeliverySelectOption("delivery", "email");
			break;
		}

		}

		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to select print option
	// # Function Name : selectPrintOption
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click on print options
	// # Function Name : clickTocNav
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking selectPrintOption(String option) {
		boolean flag = false;
		WebElement print = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.printOptions, 10);
		List<WebElement> links = commonLibrary.isExistList(print,
				UIMAP_CounselBenchmarking.printButton, 10);
		for (WebElement li : links) {
			if (li.getText().toLowerCase().contains(option.toLowerCase())) {
				commonLibrary.clickLinkWithWebElementWithWait(li, li.getText());
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Click link " + option, option
					+ " is not clicked.", Status.FAIL);

		return new CounselBenchmarking(scriptHelper);

	}

	// #**************************************************************************************************************************
	// # Function Description : Function to print and verify the content
	// # Function Name : printAndVerify
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click print icon and verifying it
	// # Function Name : printAndVerify     
	// # Author : Harish
	// # Date Created : May'15
	// #**********************************************
	public CounselBenchmarking printAndVerify() {
		generalFunctions.printWithDefaultSettings("counselbenchmarking");
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to send email
	// # Function Name : sendEmail
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to send email to a recepient
	// # Function Name : sendEmail     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking sendEmail(String Email) {
		// WebElement btnEmail =
		// commonLibrary.isExist(UIMAP_SearchResult.btnEmail);
		// commonLibrary.clickButton_Log_SmallWait(btnEmail, "E-mail");
		generalFunctions.clickDeliverySelectOption("delivery", "email");

		WebElement txtEmail = commonLibrary.isExistNegative(
				UIMAP_SearchResult.txtEmail, 10);
		commonLibrary.setDataInTextBox(txtEmail, Email, "To");

		WebElement btnSendEmail = commonLibrary.isExistNegative(
				UIMAP_SearchResult.btnSendEmail, 10);
		commonLibrary.clickButtonLogSmallWait(btnSendEmail, "Send Email");

		WebElement eltEmailPopup = commonLibrary.isExistNegative(
				UIMAP_SearchResult.eltEmailPopup, 10);
		if (eltEmailPopup == null)
			report.updateTestLog("Verify Email Delivery Dialog box closes.",
					"Email Delivery Dialog box closes.", Status.PASS);
		else
			report.updateTestLog("Verify Email Delivery Dialog box closes.",
					"Email Delivery Dialog box does not close.", Status.FAIL);
		commonLibrary.switchToWindow("deliverysecondarywindow");
		WebElement txtPopUpHeader = commonLibrary.isExistNegative(
				UIMAP_SearchResult.txtPopUpHeader, 10);
		if (txtPopUpHeader != null)
			report.updateTestLog("Verify Processing msg pop up opens.",
					"Processing msg pop up opens.", Status.PASS);
		else
			report.updateTestLog("Verify Processing msg pop up opens.",
					"Processing msg pop up does not open.", Status.FAIL);
		int i = 0;
		try {
			while (txtPopUpHeader.getText().contains("Processing")) {

				i++;
				try {
					commonLibrary.sleep(50000);
				} catch (Exception e) {
					System.out.println(e.toString());
					throw new FrameworkException("Exception", e.toString());
				}
				txtPopUpHeader = commonLibrary.isExistNegative(
						UIMAP_SearchResult.txtPopUpHeader, 10);
				if (i > 100)
					break;
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		}

		boolean blnFlag = false;
		if (txtPopUpHeader.getText().toLowerCase().contains("complete"))
			blnFlag = true;
		else
			blnFlag = false;

		if (blnFlag)
			report.updateTestLog("Verify email has been sent to " + Email + "",
					"Email has been sent to: " + Email + "", Status.PASS);
		else
			report.updateTestLog("Verify email has been sent to " + Email + "",
					"Email has not been sent to: " + Email + "", Status.FAIL);

		driver.close();
		commonLibrary.switchToWindow("counselbenchmarking");
		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to add folder with type specified
	// # Function Name : AddToFolderWithTypeSpecified
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to add folder with type specified
	// # Function Name : AddToFolderWithTypeSpecified     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking addToFolderWithTypeSpecified(String folderName,
			String documentType)

	{
		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.addFolder, 10);
		if (addtoFolder != null)
			commonLibrary.clickButtonParentWithWait(addtoFolder,
					"Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder,
					"Choose Folder");

		// SELECT TYPE OF DOCUMENT
		WebElement docType = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.documetType, 10);
		List<WebElement> docTypeList = commonLibrary.isExistList(docType,
				UIMAP_CounselBenchmarking.documentOption, 10);
		for (WebElement item1 : docTypeList) {
			if (item1.getText().equalsIgnoreCase(documentType)) {

				commonLibrary.clickLinkWithWebElementWithWait(item1,
						documentType);
			}

		}

		// CLICK ON <<<CREATE NEW FOLDER>>>

		WebElement createNewFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createNewFolder, 10);
		if (createNewFolder != null)
			commonLibrary.clickButtonParentWithWait(createNewFolder,
					"Create New Folder");

		// ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX

		WebElement folderNam = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.folderName, 10);
		if (folderNam != null)
			commonLibrary.setDataInTextBox(folderNam, folderName, "FolderName");

		// CLICK ON <<<CREATE>>>
		WebElement create = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.createFolder, 10);
		if (create != null)
			commonLibrary.clickButtonParentWithWait(create, "Create");

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		return new CounselBenchmarking(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to click on browser back button
	// # Function Name : clickBrowserBack
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click on browser back
	// # Function Name : clickBrowserBack     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public IntermediateContent clickBrowserBack() {

		//
		try {
			if (browsername.contains("chrome")
					|| browsername.contains("firefox")) {
				driver.navigate().back();
				commonLibrary.sleep(7000);
			} else {
				Actions builder = new Actions(driver);
				builder.sendKeys(Keys.BACK_SPACE).perform();
				commonLibrary.sleep(5000);

				report.updateTestLog("Click on Browser Back",
						"Clicked on Browser Back", Status.PASS);
				// driver.manage().timeouts().pageLoadTimeou(220,
				// TimeUnit.MILLISECONDS);
			}
		} catch (Exception e) {

			e.printStackTrace();
			report.updateTestLog("Click on Browser Back",
					"Browser Back not clicked", Status.FAIL);
		}
		return new IntermediateContent(scriptHelper);
	}

	// #**************************************************************************************************************************
	// # Function Description : Function to dowload documents
	// # Function Name : DownloadDocument_DefaultSettings
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to download document
	// # Function Name : DownloadDocument    
	// # Author : uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	// public CounselBenchmarking DownloadDocument_DefaultSettings(
	// String strFilePath, String AutoITPath) {
	//
	// try {
	// // String
	// //
	// strAuoItPAth="C:\\WorkSpace\\LA_R4_6_SmokeTest\\AutoItExe\\SaveFile.au3";
	// // CLICK ON <<<DOWNLOAD>>>
	//
	// // driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
	// WebElement btnDownload = commonLibrary.isExist(
	// UIMAP_Document.btnDownload, 10);
	// if (btnDownload != null)
	//
	// commonLibrary.clickButton_Parent_WithWait(btnDownload,
	// "Download");
	//
	// // CLICK <<<USE DEFAULT SETTINGS>>> FROM THE DROP DOWN.
	// WebElement btnDownloadNow = commonLibrary.isExist(
	// UIMAP_Document.btnDownloadNow, 10);
	// if (btnDownloadNow != null)
	// commonLibrary.clickButton_Parent_WithWait(btnDownloadNow,
	// "USE DEFAULT SETTINGS");
	//
	// commonLibrary.smallWait();
	// commonLibrary.SwitchToWindow("deliverysecondarywindow");
	// driver.manage().window().maximize();
	//
	// WebElement lnkDownload = commonLibrary.isExist(
	// UIMAP_Document.lnkDownloadedDoc, 10);
	// WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
	// if (pgHeader != null
	// && pgHeader.getText().toLowerCase()
	// .contains("delivery complete")
	// && lnkDownload != null)
	// report.updateTestLog(
	// "Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
	// "PROCESSING MSG POP UP is opened AND UPDATED TO A "
	// + pgHeader.getText()
	// + " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
	// Status.PASS);
	// else
	// report.updateTestLog(
	// "Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
	// "PROCESSING MSG POP UP is opened AND UPDATED TO A "
	// + pgHeader.getText()
	// + " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
	// Status.FAIL);
	// String strFileName = null;
	// if (lnkDownload != null) {
	// strFileName = lnkDownload.getText();
	// // report.updateTestLog("Verify File "+
	// // strFileName+" is downloaded in Downloads Folder", "File "+
	// // strFileName+" is downloaded in Downloads Folder",
	// // Status.PASS);
	//
	// commonLibrary.fileExists_Delete(strFilePath, strFileName);
	// String path = strFilePath + "\\";
	//
	// report.updateTestLog("Verify that Download is complete",
	// "Download is complete", Status.PASS);
	// Actions action = new Actions(driver);
	//
	// Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	// String browsername = cap.getBrowserName();
	//
	// if (browsername.equalsIgnoreCase("firefox")) {
	// driver.manage().window().maximize();
	// action.contextClick(lnkDownload).sendKeys("k").build()
	// .perform();
	// commonLibrary.sleep(4000);
	// String[] cmd = { AutoITPath,
	// "Enter name of file to save to…", path };
	// Runtime.getRuntime().exec(cmd);
	// } else if (browsername.equalsIgnoreCase("internet explorer")) {
	// action.contextClick(lnkDownload).sendKeys("a").build()
	// .perform();
	// commonLibrary.sleep(4000);
	// String[] cmd = { AutoITPath, "Save As", path };
	// Runtime.getRuntime().exec(cmd);
	// }
	//
	// commonLibrary.sleep(30000);
	//
	// commonLibrary.fileExists(strFilePath, strFileName);
	// filename = strFilePath + "\\" + strFileName;
	//
	// }
	//
	// // commonLibrary.FileExists(strFilePath,strFileName);
	// WebElement btnDownloadClose = commonLibrary.isExist(
	// UIMAP_Document.btnDownloadClose, 10);
	// if (btnDownloadClose != null)
	// driver.close();
	// // commonLibrary.clickButton_Parent_WithWait(btnDownloadClose,
	// // "Close");
	//
	// commonLibrary.smallWait();
	// commonLibrary.SwitchToWindow("counselbenchmarking");
	// return new CounselBenchmarking(scriptHelper);
	//
	// } catch (Exception e) {
	// System.out.println(e.toString());
	// throw new FrameworkException("Exception", e.toString());
	// }
	//
	// }

	public CounselBenchmarking downloadDocumentDefaultSettings() {

		try {

			final FrameworkParameters frameworkParameters = FrameworkParameters
					.getInstance();
			String TestPath = frameworkParameters.getRelativePath()
					+ Util.getFileSeparator();
			String filePath = dataTable.getData("General_Data", "FilePath");
			String strAutoIT = "Download.exe";
			String AutoITPath = TestPath + "AutoITControls"
					+ Util.getFileSeparator() + strAutoIT;

			// WebElement btnDownload =
			// commonLibrary.isExist(UIMAP_Document.btnDownload, 10);
			// if (btnDownload != null)
			//
			// commonLibrary.clickButton_Parent_WithWait(btnDownload,
			// "Download");
			//
			// // CLICK <<<USE DEFAULT SETTINGS>>> FROM THE DROP DOWN.
			// WebElement btnDownloadNow =
			// commonLibrary.isExist(UIMAP_Document.btnDownloadNow, 10);
			// if (btnDownloadNow != null)
			// commonLibrary.clickWithSmallWait_JS(btnDownloadNow,
			// "USE DEFAULT SETTINGS");
			generalFunctions.clickDeliverySelectOption("delivery",
					"downloadnow");

			commonLibrary.sleep(1000);
			commonLibrary.smallWait();
			commonLibrary.switchToWindow("deliverysecondarywindow");
			driver.manage().window().maximize();
			if (!browsername.contains("internet"))
				commonLibrary.sleep(25000);

			String header = null;
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			try {
				do {
					pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
					if (pgHeader != null) {
						header = pgHeader.getText().toLowerCase();
						if (header.toLowerCase().contains("delivery complete"))
							break;
					}
					commonLibrary.sleep(50000);
					pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);

				} while (header.toLowerCase().contains("processing"));
			} catch (StaleElementReferenceException e) {
				System.out.println(e.toString());
			}

			WebElement lnkDownload = commonLibrary.isExist(
					UIMAP_Document.lnkDownloadedDoc, 10);
			pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null
					&& pgHeader.getText().toLowerCase()
							.contains("delivery complete")
					&& lnkDownload != null)
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.FAIL);
			String strFileName = null;
			if (lnkDownload != null) {
				strFileName = lnkDownload.getText();

				commonLibrary.fileExistsDelete(filePath, strFileName);
				String path = filePath + "\\";

				report.updateTestLog("Verify that Download is complete",
						"Download is complete", Status.PASS);
				Actions action = new Actions(driver);

				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				String browsername = cap.getBrowserName();

				if (browsername.equalsIgnoreCase("firefox")) {
					driver.manage().window().maximize();
					action.contextClick(lnkDownload).sendKeys("k").build()
							.perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath,
							"Enter name of file to save to…", path };
					Runtime.getRuntime().exec(cmd);
				} else if (browsername.equalsIgnoreCase("internet explorer")) {
					action.contextClick(lnkDownload).sendKeys("a").build()
							.perform();
					commonLibrary.sleep(4000);
					String[] cmd = { AutoITPath, "Save As", path };
					Runtime.getRuntime().exec(cmd);
				} else if (browsername.equalsIgnoreCase("chrome")) {
					commonLibrary.clickButtonParentWithWait(lnkDownload,
							strFileName);
					commonLibrary.sleep(50000);
				}

				commonLibrary.sleep(30000);

				commonLibrary.fileExists(filePath, strFileName);
				filename = filePath + "\\" + strFileName;

			}

			WebElement btnDownloadClose = commonLibrary.isExist(
					UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();
			commonLibrary.switchToWindow("counselbenchmarking");
			return new CounselBenchmarking(scriptHelper);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #**************************************************************************************************************************
	// # Function Description : Function to click research map
	// # Function Name : clickResearchMap
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click on research map in HIstory tab
	// # Function Name : clickResearchMap     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ResearchMap clickResearchMap() {
		try {
			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(
					UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(
						UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow,
					"History");

			WebElement lnkTxtResearchMap = commonLibrary.isExist(
					UIMAP_Home.lnkTxtResearchMap, 30);
			if (lnkTxtResearchMap != null) {
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtResearchMap, "ResearchMap");
				else
					commonLibrary.clickButtonParentWithWait(lnkTxtResearchMap,
							"ResearchMap");
			}
			commonLibrary.sleep(60000);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ResearchMap(scriptHelper);

	}

	// #**************************************************************************************************************************
	// # Function Description : Function to navigate to history footer link
	// # Function Name : NavigateToHistoryFooterLink
	// # Author : Ram
	// # Date Created : May'15
	// #******************************************************************************************************************************
	// #*****************************************************************************************************************************
	// # Function Description : Function to click on links in HIstory tab
	// # Function Name : NavigateToHistoryFooterLink     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public History navigateToHistoryFooterLink(String strLinkName) {

		try {

			WebElement btnIdHistoryMenuArrow = commonLibrary.isExist(
					UIMAP_Home.btnIdHistoryMenuArrow, 20);
			if (btnIdHistoryMenuArrow == null) {
				WebElement btnMore = commonLibrary.isExist(
						UIMAP_Home.btnTitleMore, 10);
				commonLibrary.clickMethod(btnMore, "More");
				btnIdHistoryMenuArrow = commonLibrary.isExist(
						UIMAP_Home.btnIdHistoryMenuArrow, 20);

			}
			commonLibrary.clickLinkWithWebElement(btnIdHistoryMenuArrow,
					"History");
			commonLibrary.sleep(Mwait);
			if (strLinkName.equalsIgnoreCase("View All History")) {
				WebElement lnkTxtViewAllHistory = commonLibrary.isExist(
						UIMAP_Home.lnkTxtViewAllHistory, 20);
				if (browsername.contains("internet"))
					commonLibrary.clickJS(lnkTxtViewAllHistory,
							"View All History");
				else
					commonLibrary.clickLinkWithWebElement(lnkTxtViewAllHistory,
							"View All History");
			} else if (strLinkName.equalsIgnoreCase("Research Map")) {
				WebElement lnkTxtResearchMap = commonLibrary.isExist(
						UIMAP_Home.lnkTxtResearchMap, 30);
				if (browsername.contains("internet"))
					commonLibrary.clickJSMouseMove(lnkTxtResearchMap,
							"Research Map");
				else
					commonLibrary.clickMouseMoveAction(lnkTxtResearchMap,
							"Research Map");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new History(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify whether the Tab is Active
	// # Function Name : verifyTabActive     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking verifyTabActive(String tabName) {
		boolean blng = false;
		WebElement ulsublist = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ulsub, 10);

		if (ulsublist != null) {
			WebElement subTab = commonLibrary.isExist(ulsublist,
					UIMAP_CounselBenchmarking.liSubTab, 20);
			if (subTab != null) {
				if (subTab.getText().equalsIgnoreCase(tabName)) {
					report.updateTestLog(
							"validate 'Hourly Rates Comparison category page displays and "
									+ tabName + " sub category is active.",
							"'Hourly Rates Comparison category page is displayed and "
									+ tabName + " sub category is active.",
							Status.PASS);
					blng = true;
				}
			}
		}
		if (!blng)
			report.updateTestLog(
					"validate 'Hourly Rates Comparison category page displays and "
							+ tabName + " sub category is active.",
					"'Hourly Rates Comparison category page is NOT displayed and "
							+ tabName + " sub category is NOT active.",
					Status.FAIL);

		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click link councebench
	// # Function Name : clicklinks_councelbench2
	// # Author : Harish
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking clicklinks_councelbench2(String Stlink) {

		WebElement clicklnk = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.counselContentLink, 100);
		List<WebElement> lists = commonLibrary.isExistList(clicklnk,
				By.tagName("li"), 10);

		for (WebElement list : lists) {
			WebElement btnCondentType = commonLibrary.isExist(list,
					By.tagName("button"), 20);

			if (btnCondentType.getText().equalsIgnoreCase(Stlink)) {
				commonLibrary.clickLinkWithWebElementWithWaitJS(btnCondentType,
						btnCondentType.getText());
				break;
			}
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for selecting the recent documents
	// searched using History
	// # Function Name : navigateToViewAllHistory
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public History navigateToViewAllHistory() {
		generalFunctions.navigateToHistoryFooterLink("View All History");
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Print Icon and Choose default
	// settings
	// # Function Name : printWithDefaultSettings
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking printWithDefaultSettings(String parentWindow) {
		generalFunctions.printWithDefaultSettings(parentWindow);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Print Icon and Choose default
	// settings
	// # Function Name : printWithDefaultSettings
	// # Author : Pratik
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking verifySubCat(String header) {
		WebElement ulsublist = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.ulsub, 10);
		WebElement liSubTab = commonLibrary.isExistNegative(ulsublist,
				UIMAP_CounselBenchmarking.liSubTab, 10);
		if (liSubTab.getText().toLowerCase().contains(header.toLowerCase()))
			report.updateTestLog("Verify subcategory " + header
					+ " is selected.", "subcategory " + header
					+ " is selected.", Status.PASS);
		else
			report.updateTestLog("Verify subcategory " + header
					+ " is selected.", "subcategory " + header
					+ " is not selected.", Status.FAIL);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilterleftpane     
	// # Author : Karthi
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking verifynarrowbyfilterleftpane(String strFilter) {

		WebElement usedfilter = commonLibrary.isExist(
				UIMAP_Document.usedfilter, 20);
		if (usedfilter.getText().contains(strFilter)) {
			report.updateTestLog("To Verify Narrow By Section",
					" Narrow By Section contains " + strFilter, Status.PASS);
		} else {
			report.updateTestLog("To Verify active category tab",
					"Narrow By Section contains " + strFilter, Status.FAIL);
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select an action item from Actions
	// dropdown
	// # Function Name : clickActionSelectValue     
	// # Author : Divakar
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking clickActionSelectValue(String strAction) {
		generalFunctions.clickActionSelectValue(strAction);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking closePopup() {
		WebElement dialogContent = commonLibrary.isExistNegative(
				UIMAP_CounselBenchmarking.dialogContent, 10);
		WebElement close = commonLibrary.isExistNegative(dialogContent,
				UIMAP_CounselBenchmarking.close, 10);
		commonLibrary.clickButtonParentWithWait(close, "Close");
		pageCheck.ajaxWait(driver);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to close the Pop-up
	// # Function Name : closePopup     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking saveLink(String tcName, String dataSheet,
			String colName) {
		generalFunctions.saveLink(tcName, dataSheet, colName);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select Multiple Post Filters
	// # Function Name : SelectPostFilter_Multiple     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking selectPostFilter_Multiple(String strHeader,
			String strSubHeader, String strFilters) {
		WebElement filters1 = null;
		String[] arrFilters = strFilters.split(";");
		int i = 0;
		List<WebElement> eltCollapsedFilterHeader = commonLibrary.isExistList(
				UIMAP_SearchResult.eltCollapsedFilterHeader, 10);
		for (i = 0; i < eltCollapsedFilterHeader.size(); i++) {
			if (eltCollapsedFilterHeader.get(i).getText().toUpperCase()
					.contains(strHeader.toUpperCase())) {
				commonLibrary.clickLink(eltCollapsedFilterHeader.get(i),
						strHeader);
				report.updateTestLog("Expanding Filter Header: " + strHeader,
						"Filter Header Expanded.", Status.DONE);
			}
		}

		if (strHeader != "" && strHeader.toLowerCase().contains("court")
				&& strSubHeader.toLowerCase().contains("federal")) {
			filters1 = commonLibrary.isExist(UIMAP_SearchResult.filtersCourt,
					10);
			commonLibrary.isExist(filters1, UIMAP_SearchResult.filtersFederal,
					10);
		} else if (strHeader != ""
				&& strHeader.toLowerCase().contains("jurisdiction")) {
			filters1 = commonLibrary.isExist(
					UIMAP_SearchResult.filtersJurisdiction, 10);

		} else if (strHeader != ""
				&& strHeader.toLowerCase().contains("keyword")) {
			commonLibrary.isExist(UIMAP_SearchResult.filtersKeyword, 10);

		}

		int count = 0;
		Boolean flg = false;
		List<WebElement> eltExpandedFilterHeader = commonLibrary.isExistList(
				UIMAP_SearchResult.filterHeader, 10);
		for (i = 0; i < eltExpandedFilterHeader.size(); i++) {

			if (eltExpandedFilterHeader.get(i).getText().toUpperCase()
					.contains(strHeader.toUpperCase())) {
				eltExpandedFilterHeader.get(i);
				List<WebElement> SelectMultiple = commonLibrary.isExistList(
						UIMAP_SearchResult.btnSelectMultipleCB, 10);
				if (SelectMultiple != null) {
					for (int index = 0; index < SelectMultiple.size(); index++) {
						if (index == (count - 2)) {
							commonLibrary.clickButtonLogSmallWait(
									SelectMultiple.get(index),
									"Select Multiple in Postfilter "
											+ strHeader);
							flg = true;
							break;
						}
						if (flg)
							break;
					}
				}
			}
			if (flg)
				break;
			else
				count++;
		}
		WebElement SelMultiDialog = commonLibrary.isExist(
				UIMAP_SearchResult.SelMultiPopUp, 10);
		if (SelMultiDialog != null) {
			report.updateTestLog("Select Multiple Pop Up is displayed",
					"Select Multiple Pop Up is displayed", Status.PASS);
			List<WebElement> FilterList = commonLibrary.isExistList(
					SelMultiDialog, By.tagName("label"), 30);
			if (arrFilters[0].contains("triangle")) {
				WebElement filterdialogbox = commonLibrary.isExist(
						UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div = commonLibrary.isExist(filterdialogbox,
						UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist11 = commonLibrary.isExistList(div,
						UIMAP_VSASearchResults.ulcolumn1, 20);
				List<WebElement> ullist2 = commonLibrary.isExistList(
						ullist11.get(1), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> lilist3 = commonLibrary.isExistList(
						ullist2.get(0), UIMAP_VSASearchResults.lilist, 20);
				for (WebElement item3 : lilist3) {
					if (item3.getText().contains(arrFilters[1])) {
						WebElement buttoncourt = commonLibrary.isExist(item3,
								UIMAP_VSASearchResults.button, 20);
						if (browsername.contains("internet")) {
							commonLibrary.clickButtonParentWithWaitJS(
									buttoncourt, arrFilters[1]);
						} else {
							commonLibrary.clickButtonParentWithWait(
									buttoncourt, arrFilters[1]);
						}
					}
				}

				WebElement filterdialogbox1 = commonLibrary.isExist(
						UIMAP_VSASearchResults.filterdialogbox, 20);
				WebElement div1 = commonLibrary.isExist(filterdialogbox1,
						UIMAP_VSASearchResults.div, 20);
				List<WebElement> ullist21 = commonLibrary.isExistList(div1,
						UIMAP_VSASearchResults.ulcolumn1, 20);
				List<WebElement> lilist13 = commonLibrary.isExistList(
						ullist21.get(1), UIMAP_VSASearchResults.ullist, 20);
				List<WebElement> ullist12 = commonLibrary.isExistList(
						lilist13.get(0), UIMAP_VSASearchResults.lilist, 20);
				WebElement ulclass = commonLibrary.isExist(ullist12.get(19),
						UIMAP_VSASearchResults.ulclass, 20);
				List<WebElement> ullist22 = commonLibrary.isExistList(ulclass,
						UIMAP_VSASearchResults.lilist, 20);

				for (WebElement item4 : ullist22) {
					if (item4.getText().contains(arrFilters[2])) {
						WebElement label1 = commonLibrary.isExist(item4,
								UIMAP_VSASearchResults.label, 20);

						WebElement spanname1 = commonLibrary.isExist(label1,
								UIMAP_VSASearchResults.spanname, 20);
						if (spanname1.getText().contains(arrFilters[2])) {
							if (browsername.contains("internet")) {
								commonLibrary.clickButtonParentWithWaitJS(
										spanname1, arrFilters[2]);
							} else {
								commonLibrary.clickButtonParentWithWait(
										spanname1, arrFilters[2]);
							}
						}
					}
				}
			} else {
				for (int j = 0; j < arrFilters.length; j++) {
					for (WebElement item : FilterList) {
						if (item.getText().contains(arrFilters[j])) {
							WebElement ChkBox = commonLibrary.isExist(item,
									By.tagName("input"), 10);
							commonLibrary.setCheckBox(ChkBox, arrFilters[j]);
							break;
						}
					}
				}
			}
			// To Select OK Button in Select Multiple Pop Up
			WebElement btnOK = commonLibrary.isExist(
					UIMAP_SearchResult.OKSelMultiPopUp, 10);
			WebElement btnCancel = commonLibrary.isExist(
					UIMAP_SearchResult.cancelSelMultiPopUp, 10);
			if (btnCancel != null) {
				report.updateTestLog(
						"Verify Cancel button displays in the bottom right corner of the popup",
						"Cancel button is displayed in the bottom right corner of the popup",
						Status.PASS);
			}
			if (btnOK != null) {
				report.updateTestLog(
						"Verify Ok button displays in the bottom right corner of the popup",
						"Ok button is displayed in the bottom right corner of the popup",
						Status.PASS);
				commonLibrary.clickButtonParentWithWait(btnOK, "OK");
			}
			try {
				commonLibrary.sleep(5000);
			} catch (Exception e) {
				System.out.println(e.toString());
				throw new FrameworkException("Exception", e.toString());

			}
		}
		return new CounselBenchmarking(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifynarrowbyfilter    
	// # Author : Aravind
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifyNarrowByFilter(String strFilter) {
		Boolean flg = false;
		WebElement usedfilter = commonLibrary.isExist(
				UIMAP_Document.usedfilter, 20);
		List<WebElement> list = commonLibrary.isExistList(usedfilter,
				UIMAP_VSASearchResults.lilist, 20);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getText().toLowerCase()
					.contains(strFilter.toLowerCase())) {
				report.updateTestLog("Verify narrow by section content",
						"Narrow By Section contains " + strFilter, Status.PASS);
				flg = true;
				break;
			}
		}
		if (!flg)
			report.updateTestLog("Verify narrow by section content",
					"Narrow By Section does not contains " + strFilter,
					Status.FAIL);
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add folder with type specified
	// # Function Name : AddToFolderWithTypeSpecified     
	// # Author : Harish
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking addToCreatedFolderWithTypeSpecified(
			String folderName, String documentType)

	{
		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.addFolder, 10);
		if (addtoFolder != null)
			commonLibrary.clickButtonParentWithWait(addtoFolder,
					"Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder,
					"Choose Folder");

		// SELECT TYPE OF DOCUMENT
		WebElement docType = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.documetType, 10);
		List<WebElement> docTypeList = commonLibrary.isExistList(docType,
				UIMAP_CounselBenchmarking.documentOption, 10);
		for (WebElement item1 : docTypeList) {
			if (item1.getText().equalsIgnoreCase(documentType)) {

				commonLibrary.clickLinkWithWebElementWithWait(item1,
						documentType);
			}

		}

		// CLICK ON <<<CREATE NEW FOLDER>>>

		// WebElement createNewFolder =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.createNewFolder, 10);
		// if (createNewFolder != null)
		// commonLibrary.clickButton_Parent_WithWait(createNewFolder,
		// "Create New Folder");
		//
		// // ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX
		//
		// WebElement folderNam =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.folderName, 10);
		// if (folderNam != null)
		// commonLibrary.SetDataInTextBox(folderNam, folderName, "FolderName");
		//
		// // CLICK ON <<<CREATE>>>
		// WebElement create =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
		// if (create != null)
		// commonLibrary.clickButton_Parent_WithWait(create, "Create");
		boolean flag = false;
		commonLibrary.sleep(5000);
		WebElement createdFoldersContainer = commonLibrary.isExistNegative(
				UIMAP_Document.createdFoldersContainer, 10);
		List<WebElement> createdFolders = commonLibrary.isExistList(
				createdFoldersContainer, UIMAP_Document.listItems, 10);

		for (WebElement item : createdFolders) {
			if (item.getText().toLowerCase().contains(folderName.toLowerCase())) {
				WebElement folder = commonLibrary.isExistNegative(item,
						UIMAP_Document.links, 10);
				commonLibrary.clickLinkWithWebElementWithWait(folder,
						folderName);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select folder: " + folderName, folderName
					+ " is not present", Status.FAIL);

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add folder with type specified
	// # Function Name : AddToFolderWithTypeSpecifiedPDF
	// # Author :Vennila
	// # Date Created : 18,Nov'15
	// #**************************************************************************************************************************
	public CounselBenchmarking addToCreatedFolderWithTypeSpecifiedPDF(
			String folderName, String documentType)

	{
		// CLICK ON <<<ADD TO FOLDER>>>
		WebElement addtoFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.addFolder, 10);
		if (addtoFolder != null)
			commonLibrary.clickButtonParentWithWait(addtoFolder,
					"Add To Folder");

		// CLICK ON <<<CHOOSE A FOLDER>>>

		WebElement chooseFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.chooseFolder, 10);
		if (chooseFolder != null)
			commonLibrary.clickButtonParentWithWait(chooseFolder,
					"Choose Folder");

		// SELECT TYPE OF DOCUMENT

		WebElement Type = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.pdfLink, 10);
		if (Type != null) {

			commonLibrary.clickButton(Type);
		} else
			report.updateTestLog("Selecting PDF Option",
					"Client Option Radio button not found.", Status.FAIL);
		// CLICK ON <<<CREATE NEW FOLDER>>>

		// WebElement createNewFolder =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.createNewFolder, 10);
		// if (createNewFolder != null)
		// commonLibrary.clickButton_Parent_WithWait(createNewFolder,
		// "Create New Folder");
		//
		// // ENTER Folder Name IN SEARCH BOX ABLE TO ENTER TEXT INTO TEXT BOX
		//
		// WebElement folderNam =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.folderName, 10);
		// if (folderNam != null)
		// commonLibrary.SetDataInTextBox(folderNam, folderName, "FolderName");
		//
		// // CLICK ON <<<CREATE>>>
		// WebElement create =
		// commonLibrary.isExist(UIMAP_CounselBenchmarking.createFolder, 10);
		// if (create != null)
		// commonLibrary.clickButton_Parent_WithWait(create, "Create");
		boolean flag = false;
		commonLibrary.sleep(5000);
		WebElement createdFoldersContainer = commonLibrary.isExistNegative(
				UIMAP_Document.createdFoldersContainer, 10);
		List<WebElement> createdFolders = commonLibrary.isExistList(
				createdFoldersContainer, UIMAP_Document.listItems, 10);

		for (WebElement item : createdFolders) {
			if (item.getText().toLowerCase().contains(folderName.toLowerCase())) {
				WebElement folder = commonLibrary.isExistNegative(item,
						UIMAP_Document.links, 10);
				commonLibrary.clickLinkWithWebElementWithWait(folder,
						folderName);
				flag = true;
				break;
			}
		}
		if (!flag)
			report.updateTestLog("Select folder: " + folderName, folderName
					+ " is not present", Status.FAIL);

		// CLICK ON <<<SAVE>>>
		WebElement saveFolder = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.saveFolder, 10);
		if (saveFolder != null)
			commonLibrary.clickButtonParentWithWait(saveFolder, "Save");

		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Enter timeline in counsel
	// benchmarking page
	// # Function Name : selectPostFilterTimeline
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking selectPostFilterTimeline(String startYear,
			String endYear) {

		if (!(startYear.equals(" ") && endYear.equals(" "))) {

			int i = 0;

			WebElement filterContainer = commonLibrary.isExistNegative(
					UIMAP_VSASearchResults.filterContainer, 10);
			if (filterContainer != null) {
				List<WebElement> filterHeader = commonLibrary.isExistList(
						filterContainer, UIMAP_VSASearchResults.filterHeader,
						10);
				if (filterHeader != null) {

					for (i = 0; i < filterHeader.size(); i++) {
						if (filterHeader.get(i).getText().contains("Timeline"))

						{
							if (filterHeader.get(i).getAttribute("class")
									.contains("collapsed")) {

								if (browsername.toLowerCase().contains(
										"internet"))
									commonLibrary.clickButtonParentWithWaitJS(
											filterHeader.get(i), "Timeline");
								else
									commonLibrary
											.clickLinkWithWebElementWithWait(
													filterHeader.get(i),
													"Timeline");
								report.updateTestLog(
										"Expanding Filter Header: "
												+ "Timeline", "Timeline"
												+ " filter Header Expanded.",
										Status.DONE);
								commonLibrary.sleep(2000);
							}
							WebElement startYearTextbox = commonLibrary
									.isExist(
											UIMAP_CounselBenchmarking.startYear,
											20);
							WebElement endYearTextbox = commonLibrary.isExist(
									UIMAP_CounselBenchmarking.endYear, 20);

							if (startYearTextbox != null
									&& endYearTextbox != null) {

								commonLibrary.setDataInTextBox(
										startYearTextbox, startYear,
										"StartYear");
								commonLibrary.setDataInTextBox(endYearTextbox,
										endYear, "EndYear");
								WebElement timelinkOKbutton = commonLibrary
										.isExist(
												UIMAP_VSASearchResults.timeLineOkButton,
												20);
								if (timelinkOKbutton != null) {

									if (browsername.toLowerCase().contains(
											"internet")) {
										commonLibrary
												.clickButtonParentWithWaitJS(
														timelinkOKbutton, "OK");
										break;
									} else {
										commonLibrary
												.clickLinkWithWebElementWithWait(
														timelinkOKbutton, "OK");

										break;
									}
								}

								else {
									report.updateTestLog(
											"Clicking the OK Button",
											"OK Button is not Clicked",
											Status.FAIL);
									System.out.println("Textbox not present");

								}
							}
						}
					}
				}
			}
		}

		pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the narrow filter panel text
	// # Function Name : verifyNarrowByFilterWithClear    
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifyNarrowByFilterWithClear(String strFilter) {
		if (!(strFilter.equals(" "))) {
			Boolean flg = false;
			WebElement usedfilter = commonLibrary.isExist(
					UIMAP_Document.usedfilter, 20);
			List<WebElement> list = commonLibrary.isExistList(usedfilter,
					UIMAP_VSASearchResults.lilist, 20);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getText().toLowerCase()
						.contains(strFilter.toLowerCase())) {
					report.updateTestLog("Verify narrow by section content",
							"Narrow By Section contains " + strFilter
									+ " along with 'X' button", Status.PASS);
					WebElement filterSection = commonLibrary.isExist(
							UIMAP_CounselBenchmarking.filterSection, 10);
					WebElement clearFilterAside = commonLibrary.isExist(
							filterSection,
							UIMAP_CounselBenchmarking.clearFilters, 10);
					WebElement clearFilter = commonLibrary.isExist(
							clearFilterAside,
							UIMAP_CounselBenchmarking.anchorTag, 10);
					if (clearFilter != null) {
						report.updateTestLog(
								"Verify Clear link is present under narrow by section",
								"Clear link is present under narrow by section",
								Status.PASS);
					} else {
						report.updateTestLog(
								"Verify Clear link is present under narrow by section",
								"Clear link is not present under narrow by section",
								Status.FAIL);
					}

					flg = true;
					break;
				}
			}
			if (!flg)
				report.updateTestLog("Verify narrow by section content",
						"Narrow By Section does not contains " + strFilter
								+ " along with 'X' button", Status.FAIL);
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take report value in dashboard of CB
	// page
	// # Function Name : takeReportValue    
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public String takeReportValue() {
		String reportValue = "";
		WebElement resultContent = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.resultContent, 10);
		List<WebElement> resultList = commonLibrary.isExistList(resultContent,
				UIMAP_CounselBenchmarking.liclasstitle, 10);
		if (resultList.size() > 0 && resultList.size() < 2) {
			WebElement tableDiv = commonLibrary.isExist(
					UIMAP_CounselBenchmarking.eltTableDiv, 10);
			WebElement table = commonLibrary.isExist(tableDiv,
					UIMAP_CounselBenchmarking.eltTableData, 10);
			WebElement firstRow = commonLibrary.isExist(table,
					UIMAP_CounselBenchmarking.firstRow, 10);
			if (firstRow != null) {
				List<WebElement> tableData = commonLibrary.isExistList(
						firstRow, UIMAP_CounselBenchmarking.tagNameTd, 10);
				if (tableData.size() > 0) {
					reportValue = tableData.get(1).getText();
				}
			}
		} else if (resultList.size() > 1) {
			for (int i = 0; i < resultList.size(); i++) {
				WebElement tableDiv = commonLibrary.isExist(resultList.get(i),
						UIMAP_CounselBenchmarking.eltTableDiv, 10);
				WebElement table = commonLibrary.isExist(tableDiv,
						UIMAP_CounselBenchmarking.eltTableData, 10);
				WebElement firstRow = commonLibrary.isExist(table,
						UIMAP_CounselBenchmarking.firstRow, 10);
				if (firstRow != null) {
					List<WebElement> tableData = commonLibrary.isExistList(
							firstRow, UIMAP_CounselBenchmarking.tagNameTd, 10);
					if (tableData.size() > 0) {
						reportValue = reportValue + tableData.get(1).getText()
								+ ";";
					}
				} else {
					List<WebElement> area = commonLibrary.isExistList(
							resultList.get(i),
							UIMAP_CounselBenchmarking.areaTag, 10);
					if (area.size() > 0) {
						reportValue = reportValue
								+ area.get(1).getAttribute("title") + ";";
					}
				}
			}
		}
		return reportValue;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Dashboard changes according
	// to the filter applied
	// # Function Name : verifyDashboardChanges    
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking verifyDashboardChanges(String dashBoardName,
			String valueToCheck) {
		WebElement resultContent = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.resultContent, 10);
		List<WebElement> resultList = commonLibrary.isExistList(resultContent,
				UIMAP_CounselBenchmarking.liclasstitle, 10);
		if (resultList.size() > 0 && resultList.size() < 2) {
			for (WebElement item : resultList) {
				WebElement dashBoardHeader = commonLibrary.isExist(item,
						UIMAP_CounselBenchmarking.eltHeader, 10);
				if (dashBoardHeader != null
						&& dashBoardHeader.getText().trim().toLowerCase()
								.contains(dashBoardName.trim().toLowerCase())) {
					WebElement tableDiv = commonLibrary.isExist(
							UIMAP_CounselBenchmarking.eltTableDiv, 10);
					WebElement table = commonLibrary.isExist(tableDiv,
							UIMAP_CounselBenchmarking.eltTableData, 10);
					WebElement firstRow = commonLibrary.isExist(table,
							UIMAP_CounselBenchmarking.firstRow, 10);
					if (firstRow != null) {
						List<WebElement> tableData = commonLibrary.isExistList(
								firstRow, UIMAP_CounselBenchmarking.tagNameTd,
								10);
						if (tableData.size() > 0) {
							if (tableData.get(1).getText()
									.compareTo(valueToCheck) != 0) {
								report.updateTestLog(
										"Verify "
												+ dashBoardName
												+ " dashboard changes according to the filter applied",
										dashBoardName
												+ " dashboard is changed according to the filter applied",
										Status.PASS);
							} else {
								report.updateTestLog(
										"Verify "
												+ dashBoardName
												+ " dashboard changes according to the filter applied",
										dashBoardName
												+ " dashboard is not changed according to the filter applied",
										Status.FAIL);
							}
						}
					}
				}
			}
		} else if (resultList.size() > 1) {
			int count = 0;
			String[] dashBoardNameArr = dashBoardName.split(";");
			String[] valueToCheckArr = valueToCheck.split(";");
			for (int i = 0; i < resultList.size(); i++) {
				WebElement dashBoardHeader = commonLibrary.isExist(
						resultList.get(i), UIMAP_CounselBenchmarking.eltHeader,
						10);
				if (dashBoardHeader != null
						&& dashBoardHeader
								.getText()
								.trim()
								.toLowerCase()
								.contains(
										dashBoardNameArr[i].trim()
												.toLowerCase())) {
					count++;
					WebElement tableDiv = commonLibrary.isExist(
							resultList.get(i),
							UIMAP_CounselBenchmarking.eltTableDiv, 10);
					WebElement table = commonLibrary.isExist(tableDiv,
							UIMAP_CounselBenchmarking.eltTableData, 10);
					WebElement firstRow = commonLibrary.isExist(table,
							UIMAP_CounselBenchmarking.firstRow, 10);
					if (firstRow != null) {
						List<WebElement> tableData = commonLibrary.isExistList(
								firstRow, UIMAP_CounselBenchmarking.tagNameTd,
								10);
						if (tableData.size() > 0) {
							if (tableData.get(1).getText()
									.compareTo(valueToCheckArr[i]) != 0) {
								report.updateTestLog(
										"Verify "
												+ dashBoardNameArr[i]
												+ " dashboard changes according to the filter applied",
										dashBoardNameArr[i]
												+ " dashboard is changed according to the filter applied",
										Status.PASS);
							} else {
								report.updateTestLog(
										"Verify "
												+ dashBoardNameArr[i]
												+ " dashboard changes according to the filter applied",
										dashBoardNameArr[i]
												+ " dashboard is not changed according to the filter applied",
										Status.FAIL);
							}
						}
					} else {
						List<WebElement> area = commonLibrary.isExistList(
								resultList.get(i),
								UIMAP_CounselBenchmarking.areaTag, 10);
						if (area.size() > 0) {
							if (area.get(1).getAttribute("title")
									.compareTo(valueToCheckArr[i]) != 0) {
								report.updateTestLog(
										"Verify "
												+ dashBoardNameArr[i]
												+ " dashboard changes according to the filter applied",
										dashBoardNameArr[i]
												+ " dashboard is changed according to the filter applied",
										Status.PASS);
							} else {
								report.updateTestLog(
										"Verify "
												+ dashBoardNameArr[i]
												+ " dashboard changes according to the filter applied",
										dashBoardNameArr[i]
												+ " dashboard is not changed according to the filter applied",
										Status.FAIL);
							}
						}
					}
				}
				if (count == dashBoardNameArr.length) {
					break;
				}
			}
		}
		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clear narrow by filters
	// # Function Name : clearNarrowByFilters    
	// # Author : Anbarasan
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public CounselBenchmarking clearNarrowByFilters() {
		WebElement filterSection = commonLibrary.isExist(
				UIMAP_CounselBenchmarking.filterSection, 10);
		WebElement clearFilterAside = commonLibrary.isExist(filterSection,
				UIMAP_CounselBenchmarking.clearFilters, 10);
		WebElement clearFilter = commonLibrary.isExist(clearFilterAside,
				UIMAP_CounselBenchmarking.anchorTag, 10);
		if (clearFilter != null) {
			commonLibrary.clickLinkWithWait(clearFilter, "Clear");
		} else {
			report.updateTestLog("Click Clear link under narrow by section",
					"Clear link is not present under narrow by section",
					Status.FAIL);
		}
		return new CounselBenchmarking(scriptHelper);
	}

}
