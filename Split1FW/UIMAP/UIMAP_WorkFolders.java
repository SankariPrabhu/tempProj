package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_WorkFolders {

	// Others
	public static By myFolders = By.cssSelector("li[id='MyFoldersId']");
	public static By folderContentsEntire = By
			.cssSelector("form[class*='results-list search']");
	public static By folderContents = By.cssSelector("div[class='wrapper ']");
	public static By folderContents_RegExp = By
			.cssSelector("div[class*='wrapper']");
	public static By eMailDialog = By
			.cssSelector("aside[class*='dialog delivery']");
	public static By footer = By.tagName("footer");
	public static By documentSection = By
			.cssSelector("section[id='document'] , header[class='banner results-data']");
	public static By folderSection = By
			.cssSelector("span[id='selectedfolder']");
	public static By selectedFolder = By
			.cssSelector("span[class='selectednode']");
	public static By selectedFolderLink = By.tagName("a");
	public static By bnrmsg1 = By.cssSelector("div[class='comp toastmessage']");
	// Buttons

	public static By folderContentsEntire1 = By
			.cssSelector("form[class*='results-list search rwdesign']");

	public static By myFoldersExpand = By
			.cssSelector("button[data-rel='MyFoldersId-children']");
	public static By eMail = By.cssSelector("button[data-action='email']");
	public static By basicOptions = By
			.cssSelector("button[class='tabButton BasicOptions']");
	public static By sendEmail = By.cssSelector("input[data-action='email']");
	public static By closeEmailDelivery = By
			.cssSelector("input[value='Close'][type='button']");

	// Links
	public static By pdfLink = By.cssSelector("a[data-action='pdflink']");

	// CheckBox
	public static By docSelect = By.cssSelector("input[class='tools-toggle']");

	// TextBox
	public static By eMailAddr = By.cssSelector("input[id='EmailAddress']");
	public static By document = By.cssSelector("a[data-action='documentlink']");
	public static By intermediate = By
			.cssSelector("a[data-action='intermediatelink']");
	public static By move = By.cssSelector("button[data-action='move']");
	public static By copy = By.cssSelector("button[data-action='copy']");
	public static By folderDialog = By
			.cssSelector("aside[class='ladialog workfolder_dialog_v2 large']");
	public static By folderDialog1 = By
			.cssSelector("aside[class='workfolderdialog tabbed']");
	public static By tabHeaderContent = By
			.cssSelector("ul[class='wftabheaders']");
	public static By tabContent = By.cssSelector("ul[class='tabs']");
	public static By activeTabContent = By.cssSelector("li[class='shown']");
	public static By listItem = By.tagName("li");
	public static By tabHeader = By.tagName("h2");
	public static By button = By.tagName("button");
	public static By selectedDocsContent = By
			.cssSelector("ul[id='SelectedDocsUL']");
	public static By selectedDocsName = By.cssSelector("input[type='text']");
	public static By createNewFolder = By
			.cssSelector("input[id*='createfolder']");
	public static By foldersContent = By.cssSelector("ul[class='wftree']");
	public static By createFolderText = By
			.cssSelector("input[id='newfolderinput']");
	public static By createFolderButton = By
			.cssSelector("input[id='addnewfolder']");
	public static By moveButton = By.cssSelector("input[id='MoveDocOkButton']");
	public static By copyButton = By.cssSelector("input[id='CopyDocOkButton']");

	// ShareFolder
	public static By shareTextBox = By
			.cssSelector("input[class*='sharedname']");
	public static By wordWheelContent = By
			.cssSelector("ul[class*='SharingDropDownDiv']");
	public static By mainshare = By
			.cssSelector("ul[class*='SharingDropDownmainDiv']");
	// public static By addToShare =
	// By.cssSelector("input[class='blueButton primary']");
	public static By addToShare = By.cssSelector("input[value*='Add']");
	public static By save = By
			.cssSelector("input[type='submit'][value='Save']");

	// RenameFolder
	public static By renameTextBox = By
			.cssSelector("input[id='RenameTitleTextBox']");
	public static By rename = By
			.cssSelector("input[type='submit'][value='Rename']");

	// Search within folder
	public static By searchTextBox = By.cssSelector("input[type='search']");
	public static By searchButton = By
			.cssSelector("button[class='icon la-Search src-submit btn secondary']");
	public static By createbutton = By
			.cssSelector("button[class='btn tertiary createfolderbtn']");
	public static By newFolderText = By
			.cssSelector("input[class*='newfolderinput']");
	public static By createFdlr = By
			.cssSelector("button[class='btn tertiary addnewfolder']");
	public static By createFolder = By.cssSelector("input[value='Create']");

	public static By resultHeader = By.cssSelector("h2[class='pagewrapper']");
	public static By result = By.tagName("span");

	// ClearSearch
	public static By clearSearch = By.linkText("Clear Search");

	// DeleteFolder
	public static By btnDelete = By.cssSelector("input[value='Delete']");
	public static By dialogDelete = By.id("Delete Folder");

	public static By titleWf = By
			.cssSelector("a[data-action='intermediatelink']");
	public static By shared = By.cssSelector("a[data-id='sharedwithme']");
	public static By sharedByMe = By
			.cssSelector("a[data-title='Shared by Me']");
	public static By nextPage = By.cssSelector("a[data-action='nextpage']");
	public static By stopSharing = By.cssSelector("input[id='stopSharing']");
	public static By sharingInfo = By.cssSelector("a[id='sharinginfo']");
	public static By manageShare = By
			.cssSelector("button[data-action='manageshare']");

	public static By foldersTreeCont = By
			.cssSelector("div[class*='wftreewrapper']");
	public static By div = By.tagName("div");
	public static By bnrmsg = By.cssSelector("div[class*='toastmessage']");
	public static By links = By.cssSelector("a[data-action='linksearch']");
	public static By Cancel = By
			.cssSelector("input[type='button'][value='Cancel']");
	public static By currProd = By
			.cssSelector("div[class*='nav_currentproduct']");
	public static By shareUserList = By
			.cssSelector("ul[class*='SelecteduserListTableAddtoFolder']");
	public static By contactList = By
			.cssSelector("div[class='lx-addcontactdiv']");
	public static By divDocCondent = By.cssSelector("div[class='row-content']");
	// public static By sharingInfo =
	// By.cssSelector("a[data-action='fulldocsharinginfo’']");
	public static By shareWithOthers = By
			.cssSelector("button[data-value*='Share With Others']");
	public static By folderClass = By
			.cssSelector("form[class*='results-list search rwdesign']");
	public static By oltag = By.tagName("ol");
	public static By listtag = By.tagName("li");
	public static By TitleClassFol = By.cssSelector("h2[class*='doc-title']");
	public static By atag = By.tagName("a");

	public static By copyFolder = By
			.cssSelector("aside[class='workfolderdialog folderActionWrapper']");
	public static By copyFolderTree = By
			.cssSelector("form[class='dialog-content']");
	public static By copyFolderDiv = By.cssSelector("div[id='wftreeWrapper']");
	public static By folderUlList = By
			.cssSelector("ul[id='MyFoldersId-children'][class='supplemental shown']");
	public static By ultag = By.tagName("ul");
	public static By folderExpand = By
			.cssSelector("button[data-rel='5453df91-128d-4bc9-b2ce-c37be0076794-children'][type='button']");
	public static By clkSubFolder = By
			.cssSelector("a[data-value='COPY_SUB_1.1']");
	public static By clkOkButton = By
			.cssSelector("input[id='CopyOkButton'][value='Copy']");
	public static By parentFol = By.cssSelector("a[data-title='COPY_TOP_1']");
	public static By subFol = By.cssSelector("a[data-title='COPY_SUB_1.1']");
	public static By button1 = By.tagName("button");
	public static By ulTag1 = By.cssSelector("ul[id='treenodes']");
	public static By ulTag2 = By.cssSelector("ul[class='supplemental shown']");
	public static By asideTag = By
			.cssSelector("aside[class='search-controls rwdesign']");
	public static By folderDiv = By.cssSelector("div[class='wftreewrapper']");
	public static By folderUl = By.cssSelector("ul[class='wftree']");
	public static By ulTagMyFolder = By
			.cssSelector("ul[id='MyFoldersId-children']");

	public static By addToSharePP = By.cssSelector("input[id='addtocontact']");

	public static By dismiss = By
			.cssSelector("button[data-action='dismisstoast']");

	public static By recycleBin = By.cssSelector("a[data-id='recyclebin']");

	public static By divDelete = By.cssSelector("div[class='toolbar']");
	public static By spanDelete = By
			.cssSelector("span[class='icon la-Delete']");

	public static By asideDelete = By
			.cssSelector("aside[class='ladialog workfolder_dialog_v2 small']");
	public static By formAside = By.cssSelector("form[class='dialog-content']");
	public static By fieldsetTag = By.tagName("fieldset");

	public static By footerTag = By.tagName("footer");
	public static By ulDelete = By.cssSelector("ul[class='actions']");
	public static By deletebutton = By
			.cssSelector("input[data-action='confirm']");
	public static By cancelbutton = By
			.cssSelector("input[data-action='cancel']");

	public static By navTag = By.tagName("nav");
	public static By closeButton = By
			.cssSelector("button[data-action='cancel']");

	public static By actionbutton = By
			.cssSelector("button[class='icon action trigger la-TriangleDownAfter addNotes collapsed']");
	public static By asideEmpty = By
			.cssSelector("aside[class='supplemental overflow folderActions']");
	public static By emptyRecyclebin = By
			.cssSelector("button[class='icon action la-Delete']");
	public static By selectAll = By
			.cssSelector("input[data-action='selectall']");

	public static By sortDiv = By.cssSelector("div[id='recyclebin']");
	public static By ddDiv = By.cssSelector("div[class='dropdownContainer']");
	public static By sortSpan = By.cssSelector("span[class='sortby']");
}
