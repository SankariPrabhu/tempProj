package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_ResearchMap {

	// Buttons

	public static By addFolder = By.cssSelector("button[data-action='folder']");
	public static By chooseFolder = By.cssSelector("button[id='showaddtofolder']");
	public static By createNewFolder = By.cssSelector("input[id='createfolder']");
	public static By createFolder = By.cssSelector("input[id='addnewfolder']");
	public static By saveFolder = By.cssSelector("input[value='Save'][id='Rm_apply']");
	public static By foldersMore = By.cssSelector("a[data-action='workfolders']");
	public static By selectFolder = By.cssSelector("a[class='wftreelink ']");
	public static By sharedFolderIcon = By.cssSelector("button[class*='trigger icon la-TriangleRight']");
	public static By sharedFoldersSection = By.cssSelector("li[id='sharedwithme']");;
	public static By myFoldersSection = By.cssSelector("li[id='MyFoldersId']");;

	// TextBox
	public static By folderName = By.cssSelector("input[id='newfolderinput']");
	public static By folderDocTitle = By.cssSelector("input[id='searchtitle']");

	// Radio buttons

	public static By saveFolderPDF = By.cssSelector("input[id='ReportOnly']");
	public static By saveFolderLiveMap = By.cssSelector("input[id='LiveMap']");

	// Others

	public static By popup = By.cssSelector("div[class='researchThreadPopup open']");
	public static By lnkLinks = By.tagName("a");
	public static By buttons = By.tagName("button");
	public static By compareTray = By.cssSelector("div[id='comparetraycontainer']");
	public static By allTrials = By.cssSelector("li[id='sidebar_researchThreadsFilter']");
	public static By dateFilter = By.cssSelector("li[id='sidebar_dateFilter']");
	public static By dateFiltersideBar = By.cssSelector("div[class='sidebarFilterPopup open']");
	public static By dateFiltered = By.cssSelector("span[class='linkrefname']");
	public static By activeCompareButton = By.cssSelector("button[class='counter deactivetray']");
	public static By comparePopup = By.cssSelector("div[class='comparetraypopup']");
	public static By lstTagName = By.tagName("li");
	public static By CSRbutton = By.cssSelector("input[value='Compare Search Results']");
	public static By closeComparePopup = By.cssSelector("a[class='comparetraypopupclose icon la-CloseRemove']");
	public static By closeRMPopup = By.cssSelector("a[class='=closePopup icon la-CloseRemove']");
	//public static By recentFavorites = By.cssSelector("button[id='recent-favorites']");

	public static By button = By.cssSelector("button[type='button']");

	// Checkbox
	public static By viewAllTrials = By.cssSelector("input[id='view41_all']");
	public static By checkBox = By.cssSelector("input[type='checkbox']");

	// Tables
	public static By calendar = By.cssSelector("table[class='calendar']");
	public static By activeDates = By.cssSelector("td[class*='active']");

	public static By attachmentContent = By.cssSelector("div[class*='attachments']");
	public static By attachment = By.cssSelector("div[class='attachments']");
	public static By activeAttachmentContent = By.cssSelector("div[class='attachments active']");

	public static By fsdButton = By.cssSelector("input[value='Find Similar Documents']");
	public static By doclnk = By.cssSelector("li[class='activityAttachment']");
	public static By doclnk1 = By.cssSelector("a[class='text']");

	// public static By saveworkFolder=
	// By.cssSelector("input[value='Save'][id='workfolders_apply_save']");
	//public static By saveworkFolder = By.cssSelector("input[value='Save'][id*='workfolders_apply']");
	public static By saveworkFolder = By.cssSelector("input[value='Save'][data-id='DocumentSave']");
	public static By saveworkFolderFromDoc = By.cssSelector("input[value='Save'][data-id='SaveSearch']");
	public static By clientBtn = By.cssSelector("button[class*='client-button']");
	public static By clientdiv = By.cssSelector("ul[class='clientidmenu menu']");
	public static By editClient = By.cssSelector("button[class='icon la-Edit']");
	public static By clientText = By.cssSelector("input[id='setClientIdText']");
	public static By clientDescription = By.cssSelector("textarea[id='setClientIdDesc']");
	public static By maxChar = By.cssSelector("span[class='counter']");
	public static By fieldText = By.cssSelector("div[class='newclientid']");
	public static By fieldDesc = By.cssSelector("fieldset[class='researchdesc edit_client']");
	public static By resultDiv = By.cssSelector("div[id='researchMapGraph']");
	public static By activityDiv = By.cssSelector("div[class='activityItem']");
	public static By bubble = By.cssSelector("a[class='bubble']");

	public static By header5 = By.tagName("h5");
	public static By listHistory = By.cssSelector("button[data-action='historylist']");

	public static By productLogo = By.cssSelector("a[class='researchhome']");
	public static By researchmap = By.cssSelector("a[data-action='researchmap']");

	public static By viewByContent = By.cssSelector("li[class='sidebarPopupFilter']");
	public static By resetFilters = By.cssSelector("a[id='sidebar_resetFiltersLink']");
	public static By viewByPopUp = By.cssSelector("div[class*='sidebarFilterPopup']");
	public static By labels = By.tagName("label");
	public static By radioButton = By.cssSelector("input[type='radio']");
	public static By clientContent = By.cssSelector("li[id='sidebar_clientFilter']");
	public static By viewAll = By.cssSelector("input[id='view36_all']");
	public static By input = By.tagName("input");
	public static By reseachFilter = By.cssSelector("li[id='sidebar_researchThreadsFilter']");
	public static By viewAll1 = By.cssSelector("input[id='view41_all']");
	public static By noTrials = By.cssSelector("div[class*='noTrialsView']");
	public static By viewMapAnchor = By.cssSelector("a[data-action='researchmap']");
	public static By TitleClassDoc = By.cssSelector("h2[class='doc-title']");
	public static By CSRButton = By.cssSelector("button[class*='counter']");
	public static By CSRButtonUnderPopup = By.cssSelector("input[value*='Compare Search Results']");

	public static By sidebar = By.cssSelector("ul[id='sidebar']");
	public static By clientPopUp = By.cssSelector("div[class='sidebarFilterPopup open']");
	public static By toolBar = By.cssSelector("div[role='toolbar']");
	public static By jumpIcon = By.cssSelector("a[class='jumpPoint']");
	public static By header4 = By.tagName("h4");
	public static By popUpHeader = By.cssSelector("div[class='header']");
	
	public static By addtofolder=By.cssSelector("aside[class='doccontextmenu selectedtextmenu']");
	public static By addtofolder1=By.cssSelector("button[data-id='addtofolder']");
	public static By histPageDocTitle = By.cssSelector("h2[class='doc-title caution']");
	public static By popup1 = By.cssSelector("div[class='frame']");
	public static By popupUL=By.cssSelector("ul");
	public static By buttonTag=By.tagName("button");
	
	
	

}
