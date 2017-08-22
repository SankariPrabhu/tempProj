package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Settings {

	public static By PgTitleSettings = By.cssSelector("h2[id='pageHeaderTitle']");
	public static By PgTitleResearch = By.cssSelector("h2[id='selectedMenuTitle']");
	public static By btnIdGeneral = By.cssSelector("button[id='settings_menu_general']");
	public static By btnIdResearch = By.cssSelector("button[id='settings_menu_research']");
	public static By rdoCaseOverview = By.cssSelector("input[id='settings_research_resultscases_CaseOverview']");
	public static By btnIdsubmitSettChange = By.cssSelector("input[id='saveChangesButton']");
	public static By btnIdCancelSettChange = By.cssSelector("input[id='discardChangesButton']");
	public static By rdoCaseShowTerms = By.cssSelector("input[id='settings_research_resultscases_HitsAndCaseOverview']");
	public static By rdoCaseShowExtract = By.cssSelector("input[id='settings_research_resultscases_BestParagraphAndCaseOverview']");
	public static By rdoCaseShowOverview = By.cssSelector("input[id='settings_research_resultscases_CaseOverview']");	
	public static By recentlyViewedIcon = By.cssSelector("input[id='settings_research_recentlyviewedicon']");
	public static By termsRadio = By.cssSelector("input[id='settings_research_resultsstatutes_Hits']");
	public static By extractRadio = By.cssSelector("input[id='settings_research_resultsstatutes_BestParagraph']");
	public static By standardRadio = By.cssSelector("input[id='settings_research_results_implicitand']");
	public static By expandedRadio = By.cssSelector("input[id='settings_research_results_implicitor']");
	public static By applyFilterSubCat = By.cssSelector("input[id='settings_research_narrowResults_filters']");
	public static By leftpanelinks = By.cssSelector("ul[class='content-switcher']");
	public static By lilist = By.tagName("li");
	public static By button = By.tagName("button");
	public static By researchbutton = By.cssSelector("button[id='settings_menu_research']");
	public static By researchsettings = By.cssSelector("div[id='settings_research']");
	public static By ullist = By.tagName("ul");
	public static By formcontainer = By.cssSelector("form[class='inputContainer']");
	public static By savechangesbutton = By.cssSelector("input[id='saveChangesButton']");
	public static By HistRMDateRange = By.cssSelector("select[id='settings_general_researchmap_daterange']");
	public static By StartPagelist = By.cssSelector("select[id='settings_general_startpage']");
	public static By txtBoxfonttype = By.cssSelector("select[id='settings_general_documentdisplay_fonttype']");
	public static By txtBoxfontSize = By.cssSelector("select[id='settings_general_documentdisplay_fontsize']");
	public static By btnSaveChanges = By.cssSelector("input[id='saveChangesButton']");
	public static By StartPage = By.cssSelector("select[id='settings_general_startpage']");
	public static By smplText = By.cssSelector("p[id='text_preview']");
	public static By inputContainer = By.cssSelector("div[id='mainSettings']");
	public static By displayPracticeArea = By.cssSelector("input[id='settings_research_search_legalentities']");
	public static By displaylegalphrase = By.cssSelector("input[id='settings_research_search_withsynonyms']");
	public static By contentSwitcher = By.cssSelector("ul[class='content-switcher']");
	public static By lexisIcw = By.cssSelector("button[id='settings_menu_citationexercises_student']");

	public static By professorContainer = By.cssSelector("div[class='addedprof']");
	public static By professor = By.tagName("li");
	public static By xButton = By.cssSelector("button[class*='deleteprof']");
	public static By professorType = By.cssSelector("span[class='proftype']");
	
	public static By shareUserName = By.cssSelector("input[id='professor_name']");
	public static By wordWheelContent = By.cssSelector("div[id='settings_citationexercises_student']");
	public static By addProf = By.cssSelector("input[id='add_professor']");
	public static By verifyFacultyParent = By.cssSelector("section[class='settings']");
	public static By verifyFaculty = By.tagName("p");
	
	public static By saveCancelCont = By.cssSelector("div[id='saveCancelContainer']");
	public static By saveButton = By.cssSelector("div[id='saveCancelContainer'] input[id='saveChangesButton']");
	
	public static By removeAsso = By.cssSelector("input[value='Remove Association']");
	public static By removeStu = By.cssSelector("input[value='Remove Students']");
	public static By numberOfResultsPerPage=By.cssSelector("select[id='settings_general_resultsdisplay_numberresults']");
	
	public static By lexisLPA = By.cssSelector("button[id='settings_menu_practiceadvisor']");
	public static By practiceAreaPg = By.cssSelector("select[id='settings_practiceadvisor_page']");
	public static By practiceAreaStartIn = By.cssSelector("select[id='settings_practiceadvisor_startin']");
	public static By practiceAreaSearchResults = By.cssSelector("select[id='settings_practiceadvisor_searchresults']");
	public static By addPreferredJurLoc=By.linkText("Add preferred jurisdictions and locations");
	public static By edit=By.cssSelector("a[class='generalJurisListFirstLink']");
	public static By dialog=By.cssSelector("aside[class*='dialog msgbox']");
	public static By checkboxLabel=By.cssSelector("label[for*='filters_preferred']");
	public static By checkbox=By.tagName("input");
	public static By checkboxParagraph=By.tagName("p");
	public static By jurisdictionOk=By.cssSelector("input[value='Submit']");
	public static By jurisdictionCancel=By.cssSelector("input[value='Cancel']");
	public static By selectedItem=By.cssSelector("span[id='selectedJurisItems']");
	public static By selectedItem1=By.cssSelector("span[id='selectedCourtItems']");
	public static By span=By.tagName("span");
	public static By courtEdit=By.cssSelector("a[class='generalCourtListFirstLink']");
	public static By cancel=By.cssSelector("button[data-action='cancel']");
	public static By selectedCourtItem=By.cssSelector("span[id='selectedCourtItems']");
	public static By addPreferredCourts=By.linkText("Add preferred courts");
	public static By editCourts=By.cssSelector("a[class='generalCourtListFirstLink']");
	public static By dilogboxHeader=By.tagName("header");
	public static By section=By.cssSelector("section[class='courtCheckboxes']");
	public static By headerInSection=By.tagName("h3");
	
	public static By lexisLPS = By.cssSelector("button[id='settings_menu_profilesuite']");
	public static By profileTypeDropdown = By.cssSelector("select[id='settings_profilesuite_searchdefault']");
	public static By settingsDialog=By.cssSelector("aside[class*='dialog']");
	
	public static By lexisTax = By.cssSelector("button[id='settings_menu_tax']");
	public static By lexisTaxTabArea = By.cssSelector("div[id='settings_tax']");
	public static By lexisTaxDrpdwn = By.cssSelector("select[id='settings_tax_practiceareatax']");
	
	public static By resultviewMax = By.cssSelector("li[class*='settings_general_resultsdetailmaximum']");
	public static By resultviewMin = By.cssSelector("li[class*='settings_general_resultsdetailminimum']");
	public static By generalSettingsCont = By.cssSelector("div[id='settings_general']");
	public static By notificationOptionItems = By.cssSelector("li[class='inputReverse']");	

	public static By viewMax = By.cssSelector("button[data-action='viewfull']");
	public static By viewMin = By.cssSelector("li[data-action='viewmin']"); 
	public static By practiceAreaResults = By.cssSelector("select[id='settings_research_searchresults']");
	public static By selectedItemStartin = By.cssSelector("option[value='snapshot']");

}


