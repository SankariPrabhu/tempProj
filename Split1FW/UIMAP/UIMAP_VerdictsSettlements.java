package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_VerdictsSettlements {
	public static By txtIdSearch = By.cssSelector("textarea[id='searchTerms']");
	public static By searchContainer = By.cssSelector("input[class='lx-vsa-search-text fe']");
	public static By filterCount = By.cssSelector("span[class='filtercount']");
	public static By filterCount1 = By.cssSelector("span[id='filterText']");
	public static By searchButton = By.cssSelector("button[id='mainSearch']");
	public static By filterButton = By.cssSelector("button[class*='dropdown filters']");
	public static By clearFilterButton = By.cssSelector("button[class*='clear-filters']");
	public static By subSearchButton = By.cssSelector("input[value='Search']");
	public static By preFilterContainer = By.cssSelector("div[id='prefilters']");

	public static By h2class = By.cssSelector("h2[class='pagewrapper']");
	public static By jurisdictionsmenu = By.cssSelector("button[id='jurisdiction']");
	public static By practicemenu = By.cssSelector("button[id='ssat']");

	public static By divclassjurisdicationfilter = By.cssSelector("div[class='jurisdiction-filters hidelink']");
	public static By lstTagUList = By.tagName("ul");
	public static By lstTagListItems = By.tagName("li");
	public static By chkTypeCheckbox = By.cssSelector("input[type='checkbox']");
	
	public static By divClassPracticeArea= By.cssSelector("div[class='ssat-filters']");
	public static By divclassSelectedPrefilter = By.cssSelector("div[class='lx-narrow-by']");
	public static By lstTagOList = By.tagName("ol");
	public static By filterArrow = By.cssSelector("button[class='dropdown filters icon la-TriangleDownAfter']");
	public static By filterArrowSelected = By.cssSelector("button[class='dropdown filters icon la-TriangleDownAfter selected']");
	public static By filtJuris = By.cssSelector("input[data-parentid='poly-jurisdiction']");
	public static By jurisdiction = By.cssSelector("li[class='parent-prefilter']");
	public static By ulTaglist = By.tagName("ul");
	public static By liTagList = By.tagName("li");
	public static By ulTaglist1 = By.tagName("ul");
	public static By jurisCheckbox = By.cssSelector("input[type='checkbox']");
	public static By txtSearch = By.cssSelector("textarea[id='searchTerms']");
	public static By btnSearch = By.cssSelector("button[class='icon la-Search mainSearch']");
	
}
