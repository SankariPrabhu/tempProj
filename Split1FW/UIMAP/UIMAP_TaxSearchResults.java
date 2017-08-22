package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_TaxSearchResults {
	public static By resultsHeader = By.cssSelector("h2[class='pagewrapper']");
	public static By button = By.tagName("button");
	public static By deliveryTray = By.cssSelector("ul[class='menu overflowTo']");
	public static By resultListPods = By.cssSelector("div[class='wrapper']");
	public static By sortby =By.cssSelector("div[class='current trigger collapsed icon la-TriangleDownAfter'");
}
