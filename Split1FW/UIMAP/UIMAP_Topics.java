package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Topics {
	
	public static String headerTitle = "Legal Topic Search:";
	public static By lblHeaderTitle = By.cssSelector("h2[class='pagewrapper']");
	public static By lblHeaderSearchTerm = By.cssSelector("span[class='searchTerm']");
	public static By lblTopicTitle = By.tagName("h2");
	public static By lblTopicBreadcrumb = By.cssSelector("ul[class='breadcrumb']");
	public static By listSearchResult = By.cssSelector("div[class='pagewrapper']");
	public static By listSearchSection = By.tagName("section");
	public static By listSearchSectionExpanded = By.cssSelector("section[class='shown']");
	public static By listTopicsEntry = By.tagName("li");
	public static By listActionsMenu = By.cssSelector("nav[class='actionsList']");
	public static By listActionItems = By.tagName("li");
	public static By btnSearchResult = By.cssSelector("button[class*='la-TriangleDown']");
	public static By btnTopicAction = By.cssSelector("button[class*='la-TriangleDown active']");
	// public static By linkActionItem = By.cssSelector("")
	public static By titleHeader = By.cssSelector("header[class='banner']");

}
