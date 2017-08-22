package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_MedicalResearch {

	public static By searchSection = By.cssSelector("section[id='searchbox']");
	public static By searchBox = By.cssSelector("input[id='txtSearchBox']");
	public static By searchButton = By.cssSelector("button[id='searchBtn']");
	public static By toolbarCont = By.cssSelector("div[class='toolbar_wrapper']");
	public static By viewMedRef = By.cssSelector("button[data-action='medmalmedicalreferences']");
	public static By medResearchContainer = By.cssSelector("div[class*='medmal-medicalresearch-container']");
	public static By header = By.tagName("header");
}
