package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_TOC {
	
	public static By subSearchPanel = By.cssSelector("div[class='toc-searchbox']");
	public static By subSearchTextBox = By.cssSelector("textarea[class='embeddedSearchTerm']");
	public static By subSearchButton = By.cssSelector("button[class*='icon la-Search']");
	public static By printerFriendlyView = By.cssSelector("button[data-action='printablepage']");
	public static By toolbar = By.cssSelector("div[class='toolbar']");
	public static By subSearchDropdown = By.cssSelector("select[id='sel']");
	public static By tocList = By.cssSelector("ul[class='toclist']");
	public static By tocResult = By.tagName("li");
	public static By headerName = By.className("tocheaders");
	public static By expandCollapse = By.cssSelector("button[class*='TriangleRight']");
	public static By addToSearch = By.cssSelector("button[class*='AddToSearch']");
	public static By closeAddSearch = By.cssSelector("button[class*='CloseRemove']");
	public static By expandTitle = By.cssSelector("button[class='icon la-TriangleRight trigger']");
	public static By tocHeader = By.cssSelector("div[class='tocheaders']");
	public static By docFullPath = By.cssSelector("li[data-docfullpath*='tableofcontents']");
	public static By popUpConfirm = By.cssSelector("div[class*='confirm']");
	public static By popUpWarning = By.cssSelector("div[class*='toastmessage pagewrapper warning']");
	public static By divSection = By.cssSelector("div[class*='wrapper']");
	public static By termHighlighted = By.cssSelector("span[class*='tocSearchHit']");
	public static By btnstarFavoff = By.cssSelector("button[class*='la-FavoriteEmpty']");
	public static By btnstarFavon = By.cssSelector("button[class*='la-FavoriteFull']");
	public static By btnstarverify = By.cssSelector("button[class*='toc-favorites']");
	public static By btnstarverifyLa = By.cssSelector("button[class*='la-Favorite']");
	public static By btnfilterclose = By.cssSelector("button[class*='la-CloseRemove']");
	public static By btnclose = By.cssSelector("button[data-popupid='deleteRecentFavoriteRow']");
	public static By btnmainclose = By.cssSelector("button[class*='la-CloseRemove close']");
	public static By header = By.cssSelector("header[class='banner results-data']");
	public static By divpop = By.cssSelector("div[id='deleteRecentFavoriteRow']");
	public static By btnheader = By.cssSelector("div[class='popup_floater popup_filter_clear']");
	public static By btnpop = By.cssSelector("div[class='pop_bottombar']");
	public static By btncancel = By.cssSelector("button[class='cancel_popup']");
	public static By menubar = By.cssSelector("menu[label='Controls for the filter panel']");
	public static By cancelbtn = By.cssSelector("button[class='icon la-CloseRemove close']");
	public static By btndelete = By.cssSelector("button[class='save_popup']");
	public static By resultListClass=By.cssSelector("div[class='resultlist-doc']");
	public static By docLink=By.tagName("a");
	public static By resultListLi=By.tagName("li");
	public static By resultListOl=By.tagName("ol");
	public static By resultListDiv=By.cssSelector("div[class='wrapper']");
	public static By paragraph=By.tagName("article");
	public static By paragraphLink=By.tagName("a");
	
	public static By searchPanel = By.cssSelector("div[class='toc-searchbox']");
	public static By searchTextBox = By.cssSelector("textarea[class='embeddedSearchTerm']");
	public static By searchButton = By.cssSelector("button[class='icon la-Search mainSearch']");
	public static By selectFromDd = By.cssSelector("select[id*='sel']");
	
	public static By resultslist = By.cssSelector("ol[id='resultlist']");
	public static By eachTeaser = By.cssSelector("a[data-action='teaserlink']");
	public static By pagewrapper = By.cssSelector("h2[class='pagewrapper']");
	public static By spamquery = By.cssSelector("spam[class='query']");
	
	public static By tocheaderPractice = By.cssSelector("div[class='comp container_signpost']");
	
	public static By resultListDoc = By.cssSelector("div[class='resultlist-doc']");
	public static By resultList = By.cssSelector("ol[id='resultlist']");

}
