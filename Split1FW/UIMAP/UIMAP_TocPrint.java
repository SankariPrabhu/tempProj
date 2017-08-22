package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_TocPrint {
	public static By header = By.cssSelector("header[class*='banner']");
	public static By tocList = By.cssSelector("ul[class='toclist']");
	public static By footer = By.cssSelector("div[class='footer']");
	public static By footerNav = By.cssSelector("ul[class='footer-nav']");
	public static By lnLogo = By.cssSelector("img[alt='LexisNexis Logo']");
	public static By copyright = By.cssSelector("li[class='footer-nav-item copyright']");
	public static By tocHeader = By.cssSelector("div[class='tocheaders']");
	public static By expandNode = By.cssSelector("button[class='icon la-TriangleRight trigger']");
	public static By printHed = By.cssSelector("input[class='primary print headerprintbutton']");
	public static By printFot = By.cssSelector("input[class='primary print']");
	
	public static By resultListDoc = By.cssSelector("div[class='resultlist-doc']");
	public static By resultList = By.cssSelector("ol[id='resultlist']");
	

}
