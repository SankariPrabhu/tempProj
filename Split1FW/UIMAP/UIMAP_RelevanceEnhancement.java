package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_RelevanceEnhancement {
	public static By docSection = By.cssSelector("section[id='document']");
	public static By headerTermAll = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By headerTermHighlighted = By.cssSelector("span[class*='SS_SH']");
	public static By restTermAll = By.tagName("span");
	public static By teaserTextParent = By.cssSelector("div[class='SS_Copyright']");
}
