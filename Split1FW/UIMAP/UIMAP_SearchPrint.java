package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_SearchPrint {
	public static By SearchResultHeader = By.cssSelector("header[class='resultsHeader']");
	public static By litSymbol = By.cssSelector("div[class*='LegalIssueTrail']");
	public static By lexisAdvaceLogo = By.cssSelector("div[class='navproductswitcher']");
	public static By printHeader = By.id("print_header_btn");
	public static By printHeader1 = By.cssSelector("input[class*='headerprintbutton']");
	public static By prinFooter1 = By.cssSelector("input[class='primary print']");
	public static By prinFooter = By.id("print_footer");
	public static By passageLITHeader = By.cssSelector("div[class*='print pagewrapper']");
	public static By resultsListSearch = By.cssSelector("form[class='results-list search']");
	public static By dateTime = By.cssSelector("div[class='printfooter']");
	public static By dateTimeResults = By.cssSelector("dl[class='pagewrapper']");
	public static By titleHeader = By.tagName("h2");
	public static By orderList = By.tagName("ol");
	public static By asideView = By.tagName("aside");
	public static By relxLogo = By.cssSelector("img[src='/Images/REFooterLogo.png']");
	public static By lexisnexisLogo = By.className("lexisnexis-logo");
	public static By footerElement = By.cssSelector("li[class*='footer-nav-item']");
}
