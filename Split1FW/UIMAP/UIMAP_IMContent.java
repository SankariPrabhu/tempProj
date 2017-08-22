package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_IMContent {
	public static By reRunSearch = By.cssSelector("a[id='rrnsrch']");
	public static By viewmap = By.cssSelector("a[data-action='researchmap']");
	public static By webLink = By.cssSelector("a[id='weburlredirectlink']");
	public static By docframe = By.cssSelector("div[class='frame']");
	public static By SearchResultHeader4 = By.cssSelector("header[class*='banner document-data']");

	public static By openDashboard = By.cssSelector("a[data-action='linkrmb']");
	public static By sectioncontent = By.cssSelector("section[class='doc-content SS_contentdocument']");
	public static By sectionHeader = By.cssSelector("span[class='title']");
	public static By editIcon = By.cssSelector("button[id='editdocument']");
	public static By editArea = By.cssSelector("section[id='intermediatecontent_banner']");
	public static By editPopUp = By.cssSelector("aside[class='dialog citationpopup']");
	public static By editTitle = By.cssSelector("input[id='editdocumenttextbox']");
	public static By newEditClient = By.cssSelector("input[id='newclient']");
	public static By editClientExisting = By.cssSelector("select[id='clientidselect']");
	public static By existingClientDropdown = By.cssSelector("select[id='clientidselect']");
	public static By editClientText = By.cssSelector("input[class='newclientidtext']");
	public static By editSave = By.cssSelector("input[id='saveEdit']");
	public static By editCancel = By.cssSelector("input[id='closeEdit']");
	public static By openForm = By.cssSelector("a[data-action='linkdoc']");
	
	public static By typeProperties = By.cssSelector("span[class='title']");
	public static By propertiesValue = By.cssSelector("section[class='pod-align']");
	public static By header = By.cssSelector("div[class='pagewrapper']");
	public static By srchwithCurrent = By.cssSelector("a[id='rrnsrch']");	
	//search current
	
	public static By srchwithCurrentnew = By.cssSelector("a[id='rrnsrchfilter']");
	
	// medmal Dashboard
	public static By DashboardMedmal = By.cssSelector("a[data-action='linkmedmal']");
	
	public static By webURL=By.cssSelector("a[id='weburlredirectlink']");
	public static By abtSection = By.cssSelector("section[id='About This Document']");
	
	//Edit
	public static By editheader = By.cssSelector("div[class='wf-notification-primaryaction']");
	public static By editbtn = By.cssSelector("span[class='icon la-Edit wfnotes']");
	public static By editPopup = By.cssSelector("iframe[id='notescontent_ifr']");
	public static By editContent = By.cssSelector("body[class='mceContentBody ']");
	public static By content = By.tagName("p");
	public static By contentNew = By.cssSelector("br[data-mce-bogus']");
	public static By save = By.cssSelector("input[type='submit']");
	public static By notes = By.cssSelector("div[class='topline_gotoNotesList']");
	public static By verifyNote = By.tagName("span");
	public static By viewAllNotes = By.cssSelector("a[id='viewallnotes']");
	public static By cancel = By.cssSelector("input[value='Cancel']");
	public static By noteSection = By.cssSelector("span[id='notesareapassage']");
	public static By deletebtn = By.cssSelector("span[class='icon la-CloseRemove wfnotes']");
	public static By footer = By.tagName("footer");
	public static By delete = By.cssSelector("input[value='Delete']");
	
	//addNote
	public static By addheader = By.cssSelector("div[class='wfcontainer-addNote']");
	public static By addbtn = By.cssSelector("span[class='icon la-Notes wfnotes']");
	public static By ownerheader = By.cssSelector("div[class='notesdiv']");
	public static By owner = By.cssSelector("span[class='icon la-Notes noteowner']");
	public static By date = By.cssSelector("span[class='notecreated']");
}
