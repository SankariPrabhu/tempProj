package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_RelatedContent {
	public static By allVersionRegulation = By.cssSelector("a[data-pdlinktext='All versions of this regulation|']");
	public static By resultList = By.cssSelector("div[id='resultlist']");
	public static By embeddedlinks = By.cssSelector("h2[class='doc-title caution']");

	public static By folderIcon = By.cssSelector("button[class='icon action la-Folder']");
	public static By documenttitle = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By resultListLnk = By.cssSelector("a[class='resultlistclick']");

	public static By dppadropdown = By.cssSelector("select[id*='dppaList']");
	public static By glbadropdown = By.cssSelector("select[id='MainContent_glbaList']");
	public static By dmfdropdown = By.cssSelector("select[id='MainContent_dmfList']");
	public static By cofirm = By.cssSelector("input[id='MainContent_btnConfirm1']");	
	public static By locatorPopUp = By.cssSelector("div[class='locatorwrapper']");
	public static By folderList = By.cssSelector("ul[class='folderlist']");
	public static By links = By.tagName("a");
	public static By resultlist=By.cssSelector("div[class='wrapper']");
	public static By folderIcon1 = By.cssSelector("button[class='icon action la-SavedToFolder']");

}
