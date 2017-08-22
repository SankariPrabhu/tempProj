package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_GUIBInstances {

	// Others
	public static By liPods = By.cssSelector("li[data-pditab='newpod']");
	public static By liAllPods = By.cssSelector("li[data-pditab='allpods']");
	public static By doctitle = By.cssSelector("h2[class='doc-title caution']");
	public static By liPage = By.cssSelector("li[data-pditab='newpage']");
	public static By ulContents = By.cssSelector("ul[class='content-switcher']");
	public static By divContent = By.cssSelector("div[class='row-content']");
	public static By eltCollapsedFilterHeader = By.cssSelector("button[class*='TriangleRight collapsed']");
	public static By labelFilter = By.cssSelector("label[for*='uipodslist']");
	public static By ulFiltersUsed = By.cssSelector("ul[class='filters-used']");
	// buttons
	public static By btnClone = By.cssSelector("button[data-action='clone']");
	public static By btnAddtoPage = By.cssSelector("button[data-action='addtopage']");
	public static By btnRemoveFilter = By.cssSelector("button[class='icon la-CloseRemove']");

	// links
	public static By linkNextPage = By.cssSelector("a[data-action='nextpage']");
	public static By lnkdoctitle = By.cssSelector("a[data-action='title']");
	public static By lnkLexisAdvanceResearch = By.cssSelector("a[data-action='practice']");
	public static By lnkLexisAdvanceResearch1 = By.cssSelector("a[data-action='researchhome']");
	
	public static By btnSuspend = By.cssSelector("button[data-action='suspend']");
	public static By confirmSuspend = By.cssSelector("input[value='Suspend']");
	public static By btnDelete = By.cssSelector("button[data-action='delete']");
	public static By confirmDelete = By.cssSelector("input[value='Delete']");
	public static By liAllPage = By.cssSelector("li[data-pditab='allpages']");
	public static By copyCode = By.cssSelector("button[data-action='copycode']");
	public static By copyCodePopup=By.cssSelector("aside[class='dialog copycode_dialog']");
	public static By copyCodeText=By.cssSelector("textarea[name='copycode']");
	public static By body=By.cssSelector("body");

}
