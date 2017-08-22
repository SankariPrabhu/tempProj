package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_GUIBContainer {	
	public static By container = By.cssSelector("div[class='widget-container']");
	public static By logo = By.tagName("h1");
	public static By getButton = By.cssSelector("button[class*='get-button']");
	public static By footer = By.cssSelector("div[class*='footer']");
	public static By lexisNexisLogo = By.cssSelector("img[alt='LexisNexis Logo']");
	public static By citationText = By.cssSelector("textarea[class*='Citations']");
	public static By errorMessage = By.cssSelector("label[class='field-validation-error']");
	public static By statusBox = By.cssSelector("div[class='status-box']");
	public static By jurisForm = By.cssSelector("form[class='jurisForm']");
	public static By cityList = By.cssSelector("div[class*='group']");
	public static By citySubList = By.cssSelector("div[class*='subgroup']");
	public static By expandIcon = By.cssSelector("button[class*='TriangleRight']");
	public static By collapseIcon = By.cssSelector("button[class*='TriangleDown']");
	public static By tocLink = By.cssSelector("a[class*='toc']");
	public static By addToSearch = By.cssSelector("a[class*='addtosearch']");
	public static By addSource = By.cssSelector("a[class*='source addsource']");
	public static By searchAdded = By.cssSelector("div[class='apisearchbox']");
	public static By searchSelectedtxt = By.cssSelector("input[id='SearchKey']");
	public static By searchButton = By.cssSelector("a[class='apisearchbutton']");	
	public static By footerCB = By.cssSelector("div[class*='nav_footer']");
	public static By getButtonInput = By.cssSelector("input[class*='get-button']");
	public static By statusOfRequest=By.cssSelector("div[class='courierstatus']");
	public static By statusOfRequestLi=By.tagName("li");
	public static By container1 = By.cssSelector("div[class='widget-container globalpod']");
	public static By h2Tag=By.tagName("h2");
	
}
