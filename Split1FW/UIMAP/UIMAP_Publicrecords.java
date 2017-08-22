package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Publicrecords 
{
	
	public static By dppaDropDown = By.cssSelector("select[id='MainContent_dppaList']");
	public static By glbaDropDown = By.cssSelector("select[id='MainContent_glbaList']");
	public static By dmfDropDown = By.cssSelector("select[id='MainContent_dmfList']");
	public static By confirmButton = By.cssSelector("input[id='MainContent_btnConfirm1']");
	public static By compPersonReport = By.cssSelector("a[href='FindAPerson.aspx']");
	public static By firstName = By.cssSelector("input[id='MainContent_Name_FirstName']");
	public static By lastName = By.cssSelector("input[id='MainContent_Name_LastName']");
	public static By searchButton = By.cssSelector("input[id='MainContent_formSubmit_searchButton']");
	public static By termsofuse = By.cssSelector("table[id='permUseSteps']");
	public static By divperms1 = By.cssSelector("div[id='selectPermUse']");
	public static By divperms2 = By.cssSelector("div[id='terms']");
	
	public static By iframe = By.cssSelector("iframe[id='publicRecordsUrl']");
	public static By liparentframe1 = By.cssSelector("li[id='menuLevel1Item0']");
	public static By divFirst = By.cssSelector("div[id='MainContent_Name_divFirstName']");
	public static By divLast = By.cssSelector("div[id='MainContent_Name_divLastName']");
	public static By divButton = By.cssSelector("div[class='button']");
	public static By divOldRow = By.cssSelector("tr[class='oddrow']");
	public static By divSpanName = By.cssSelector("span[id='spanNames1_0']");
	public static By clickSearchLink = By.cssSelector("a[class='_clickSearchLink']");
	public static By divResultCon = By.cssSelector("table[id='resultscontent']");
	public static By divResultTable = By.cssSelector("div[id='resultTableDiv']");
	public static By selstatecompper = By.cssSelector("select[id='MainContent_Address_State_stateList']");
	
	public static By uitermsforuse = By.cssSelector("div[id='terms']>h1");
	public static By uifulldef = By.cssSelector("a[href='javascript:void(0);']");
	
	public static By btnIdLexisAdvance = By.cssSelector("button[id='nav_productswitcher_arrowbutton']");
	public static By btnPublicRecords = By.cssSelector("a[data-action='publicrecords']");
	public static By CurrentProduct = By.cssSelector("a[id*='currentproduct']");
	
	public static By prHomePage = By.cssSelector("div[id='PageWrapper'");

	
}
