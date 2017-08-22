package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_AlertsSearchResults {
	public static By moreDropdown = By.cssSelector("div[class*='overflowMore']");
	public static By overflowFrom = By.cssSelector("ul[class='overflowFrom']");
	public static By blueDotted = By.cssSelector("button[class*='icon la-NewUpdates']");
	public static By eltFilterList = By.cssSelector("label[for*='cb']");
	public static By alertsForm = By.cssSelector("div[class='wrapper']");
	public static By alertsList = By.tagName("li");
	public static By docTitle = By.cssSelector("h2[class*='doc-title']");
	public static By btnEditAlert = By.cssSelector("button[class='icon la-Edit action edit'][type='button']");
	public static By btnSaveAlert = By.cssSelector("input[type='submit'][value='Save']");
	public static By alertsProfilePage = By.cssSelector("main[class='comp alert_profile_list']");
	public static By alertsBannerVerif = By.cssSelector("header[class='banner results-data']");
	

}
