package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_LPSHome {

	public static By btnClassFilter = By.cssSelector("button[class*='PreFilter']");
	public static By mnuFilterToolBar = By.cssSelector("ul[class*='filterTypeList']");
	public static By btnIdFilterMenu = By.tagName("li");
	public static By divClassCTFilters = By.cssSelector("div[class='filters']");
	public static By entityDropdown = By.cssSelector("button[class*='profileSelection']");
	public static By entityDropdown1 = By.cssSelector("button[class*='selectcategory']");
	public static By dropdownList = By.cssSelector("ul[class='profileLists']");
	public static By dropdownList1 = By.cssSelector("ul[class='supplemental']");
	public static By listItem = By.tagName("li");
	public static By button = By.tagName("button");
	public static By filterButton = By.cssSelector("button[class*='icon showPreFilter']");
	public static By filterButton1 = By.cssSelector("button[class*='filters dropdown']");
	public static By preFilterContainer = By.cssSelector("div[id='lpsPrefilterContainer']");
	public static By filterList = By.cssSelector("ul[class='filterTypeList']");
	public static By uList = By.tagName("ul");
	public static By filterGroup = By.cssSelector("div[class='filters']");
	public static By input = By.tagName("input");
	public static By closeFilterPopup = By.cssSelector("button[class='closeBtn icon la-CloseRemove']");
	public static By wordWheel = By.cssSelector("div[id='lpswordwheel']");
	public static By link = By.tagName("a");
	public static By searchButton = By.cssSelector("button[id='mainSearch']");
	public static By defaultProfileType = By.cssSelector("button[class='selectcategory dropdown icon la-TriangleDownAfter collapsed']");
	public static By noResultsHeader = By.cssSelector("div[class='noResultsHeader']");
	public static By filterArrow = By.cssSelector("button[class='filters dropdown icon la-TriangleDownAfter']");
	public static By filterArrowSelected = By.cssSelector("button[class='filters dropdown icon la-TriangleDownAfter selected']");
	public static By locations = By.cssSelector("li[class='normal']");
	public static By filterLocations = By.cssSelector("div[class='filters']");
	public static By ulTaglist = By.tagName("ul");
	public static By liTagList = By.tagName("li");
	public static By filterType = By.cssSelector("div[class='filterType']");
	public static By ulFilterType = By.cssSelector("ul[class='filterTypeList']");
	public static By jurisCheckbox = By.cssSelector("input[type='checkbox']");
	public static By filterLable = By.cssSelector("label[class='labelText']");
	public static By ulTaglist1 = By.cssSelector("ul[class='locations'][data-id='state']");
	public static By liTagList1 = By.cssSelector("label[class='labelText']");
	public static By specialcharContainer= By.cssSelector("div[class='specialCharacterContainer']");
	public static By searchResultContainer = By.cssSelector("div[class*='results-list']");
	
}
