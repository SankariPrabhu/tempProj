package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_VSASearchResults {
	public static By vsatitle = By.cssSelector("h1[class='title pagewrapper']");
	public static By vsagraph = By.cssSelector("img[src='/images/ImgVSASample.png']");
	public static By vsainnerbox = By.cssSelector("div[id='message']");
	public static By listprice = By.cssSelector("div[class='pricedisplay']");
	public static By vsaclientid = By.cssSelector("p[class='clientid']");
	public static By vsagetitnow = By.cssSelector("input[type='submit'][name='purchase']");
	public static By vsacancel = By.cssSelector("input[type='button'][name='cancel']");
	public static By frmClassResult = By.cssSelector("form[class*='results-list vsareport']");
	public static By TitleClassDoc = By.cssSelector("h2[class='doc-title caution']");
	public static By TitleClassTOC = By.cssSelector("h2[class='pagewrapper']");
	public static By SearchResultHeader3 = By.cssSelector("header[class*='document-data']");
	public static By pgClassHeaderOption3 = By.cssSelector("header[class*='header']");
	public static By grpCasesYearResoln = By.cssSelector("img[class='graph-img'][alt='Number of Cases per Year by Resolution']");
	public static By grpCasesYear = By.cssSelector("img[class='graph-img'][alt='Number of Cases per Year']");
	public static By grpPerCasesResoln = By.cssSelector("img[class='graph-img'][alt='Percentage of Cases by Resolution']");
	public static By grpAwardResoln = By.cssSelector("img[class='graph-img'][alt='Award in US Dollars by Resolution']");
	public static By lrgGrpCasesYearResoln = By.cssSelector("div[class*='large-graph'][id='CasePerYearByResolutionChart']");
	public static By lrgGrpCasesYear = By.cssSelector("div[class*='large-graph'][id='NumberOfCasesPerYearChart']");
	public static By ltgGrpPerCasesResoln = By.cssSelector("div[class*='large-graph'][id='PercentOfCasesByResolutionChart']");
	public static By lrgGrpAwardResoln = By.cssSelector("div[class*='large-graph'][id='AwardByResolutionChart']");
	public static By closeButton = By.cssSelector("button[class='close icon la-CloseRemove'][title='close']");	
	public static By getItNowContainer=By.cssSelector("fieldset[class='access']");
	public static By getItNowButton = By.cssSelector("input[type='submit'][value='Get It Now!']");
	public static By filtersapplied = By.cssSelector("button[class='icon la-CloseRemove removable']");
	public static By unusedfilters = By.cssSelector("div[class*='filters-unused']");
	public static By ulclass = By.cssSelector("ul[class='supplemental']");
	public static By buttonnew = By.cssSelector("button[class*='icon trigger la-TriangleRight']");
	public static By parentcourt = By.cssSelector("ul[data-id='court-rollup']");
	public static By lilist = By.tagName("li");
	public static By uldataid = By.cssSelector("ul[data-title='Federal']");
	public static By lilist2 = By.cssSelector("li[class='sel-multi']");
	public static By selectmultiplebutton = By.cssSelector("button[data-action='selmulti']");
	public static By filterdialogbox = By.cssSelector("form[class='dialog-content']");
	public static By div = By.tagName("div");
	public static By ullist = By.tagName("ul");
	public static By label = By.tagName("label");
	public static By filtercheckbox = By.cssSelector("input[type='checkbox']");
	public static By statedropdown = By.cssSelector("button[data-value='courtstate-rollup']");
	public static By federaldropdown = By.cssSelector("button[data-value='courtfederal-rollup']");
	public static By expandedcheck = By.cssSelector("button[class='icon trigger la-TriangleRight collapsed']");
	public static By parentcaseresolution = By.cssSelector("ul[data-id='outcome']");
	public static By ulnotcourt = By.cssSelector("ul[class='not-court']");
	public static By spanname = By.tagName("span");
	public static By ulclassaction = By.cssSelector("ul[class='actions']");
	public static By submitbutton = By.cssSelector("input[data-action='confirm']");
	public static By footer = By.tagName("footer");
	public static By parentinjury = By.cssSelector("ul[data-id='typeofinjury']");
	public static By ulcolumn1 = By.cssSelector("ul[class*='columns']");
	public static By button = By.cssSelector("button[type='button']");
	public static By spanclass = By.cssSelector("span[class='hidden js-expcol']");
	public static By warning=By.cssSelector("aside[class='vsamessage']");
	public static By warningHeader=By.tagName("p");
	public static String vsaSearchResult = "vsasearchresults";
	public static By searchWithin = By.cssSelector("input[id='search-within-input']");
	public static By btnButton = By.tagName("button");
	public static By header = By.tagName("h3");
	
	public static By menu = By.cssSelector("menu[type='toolbar']");
	public static By searchHeader=By.cssSelector("header[class='resultsHeader']");
	public static By searchHeaderValue=By.tagName("h2");
	public static By filterButton = By.cssSelector("button[data-id='prefilters']");
	public static By clearfilter= By.cssSelector("button[class*='clear-filters']");
	public static By clearfilter1= By.cssSelector("button[data-action='clear']");
	public static By usedfilter = By.cssSelector("ul[class*='filters-used']");
	public static By filterContainer = By.cssSelector("ul[id='refine']");
	public static By filterHeader = By.cssSelector("button[class*='icon trigger la-TriangleRight']");
	public static By startYear=By.cssSelector("input[class='min-val'][type='text']");
	public static By endYear=By.cssSelector("input[class='max-val'][type='text']");
	public static By timeLineOkButton=By.cssSelector("button[class='save btn secondary'][type='button']");
	public static By liclassaction = By.cssSelector("li[data-id*='sr']");
// cite list
	
	public static By resultlist = By.tagName("div");
	// hover
	public static By map = By.cssSelector("li[data-id*='sr']");
	
	
	
}
