package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_SearchResult {
	
	public static By btnIdDeliverTab = By.cssSelector("button[data-switch='2']");
	public static By rdoIdOnlineOnly = By.cssSelector("input[id='deliverytype_Online']");
	public static By frmClassResult = By.cssSelector("form[class*='results-list']");
	public static By errorResult = By.cssSelector("article[class='notification pagewrapper']");
	public static By frmClassResult1 = By.cssSelector("div[class*='results-list']");
	public static By frmClassResult2=  By.cssSelector("form[class*='results-list search min']");
	public static By frmClassResult3=  By.cssSelector("form[class*='results-list search']");
	public static By lnkDocTitle = By.cssSelector("a[data-action='midlinetitle']");
	public static By TitleClassDoc = By.cssSelector("h2[class*='doc-title']");
	public static By divDocCondent = By.cssSelector("div[class='row-content']");
	public static By ulCondentSwitcher = By.cssSelector("ul[class*='content-switcher']");
	public static By ulCondentSwitcher1 = By.cssSelector("ul[class*='content-switcher open expanded']");
	public static By lnkTableOfCondents = By.cssSelector("a[class*='TableOfContents']");
	public static By TitleClassTOC = By.cssSelector("h2[class='pagewrapper']");
	public static By SearchResultHeader3 = By.cssSelector("header[class*='document-data']");
	public static By ulCondentSwitcher2 = By.cssSelector("ul[class*='content-switcher open']");

	public static By pgClassHeaderOption3 = By.cssSelector("header[class*='header']");
	public static By SearchResultHeader = By.cssSelector("header[class='resultsHeader']");
	public static By SearchResultHeader1 = By.cssSelector("header[class*='results-data']");
	public static By pgClassHeaderOption = By.cssSelector("header[class='signpost']");
	public static By pgClassHeaderOption1 = By.cssSelector("div[class*='headerbanner']");
	public static By pgClassHeaderOption2 = By.cssSelector("section[class*='signpost']");
	public static By secIdMainTopics = By.cssSelector("section[id='results']");
	public static By lstTagName = By.tagName("li");
	public static By tagHeader1 = By.tagName("h3");	
	public static By image = By.tagName("img");	
	public static By btnClassExpand = By.cssSelector("button[class*='TriangleRight']");
	public static By pgClassHeaderOption4 = By.cssSelector("div[id='nav_currentproduct_button']");

	public static By btnIdExpandMadSearch = By.cssSelector("button[id='expand-topic icon la-TriangleDown']");
	public static By lnkBladderCancer = By.cssSelector("a[data-title='Failure to Diagnose & Delay in Treatment of Bladder Cancer']");
	public static By lstidCase_assessment = By.cssSelector("li[id='case_assessment']");
	public static By lstidcare_analysis = By.cssSelector("li[id='care_analysis']");
	public static By lstidlegal_analysis = By.cssSelector("li[id='legal_analysis']");
	public static By lstidexpert_witness = By.cssSelector("li[id='expert_witness']");
	public static By lstidresearch_parties = By.cssSelector("li[id='research']");
	public static By lstidresearch_medical = By.cssSelector("li[id='research_medical']");

	public static By btnFewerCat = By.cssSelector("button[data-action='moretypes']");
	public static By btnFewerCat1 = By.cssSelector("button[data-selectedvalues='moretypes']");
	public static By eltDocketList = By.cssSelector("h2[class*='doc-title']");
	public static By lnkTitleSharedByOthers = By.cssSelector("a[data-title='Shared by Others']");
	public static By btnIdAddAlert = By.cssSelector("button[data-id='addalert']");
	
	public static By btnIdCreateAlert = By.cssSelector("input[value='Create Alert']");
	public static By btnClassAlert = By.cssSelector("button[class*='Alerts']");
	public static By divClassError = By.cssSelector("div[class*='Error']");
	public static By btnIdCancel = By.cssSelector("input[value='Cancel']");
	public static By tagNameArticle = By.tagName("article");
	public static By tagNameAside = By.tagName("aside");
	public static By lstResearchThread = By.cssSelector("li[class='researchThread']");
	public static By lnkClassBubble = By.cssSelector("a[class='bubble']");
	public static By divClassBubblePopup = By.cssSelector("div[class*='researchThreadPopup']");
	public static By divClassBubblePopUpCondent = By.cssSelector("div[class='frame']");
	public static By lnkSharedDocs = By.cssSelector("a[data-action='documentlink']");
	public static By divClassMultipleJustice = By.cssSelector("div[class='multiplejusticebanner']");
	public static By lnkClassJudge = By.cssSelector("a[class='SS_EntityLink']");
	public static By lnkidJudgeSearch = By.cssSelector("a[id='entityLinkingSearchLinkClick']");

	public static By txtAppTitleBlack = By.cssSelector("span[title='Lexis Advance®']");
	public static By txtAccessChargeRed = By.cssSelector("header[role='banner']");
	public static By btnAgreeAndContinue = By.cssSelector("input[id='submit_laac']");
	public static By eltCollapsedFilterHeader = By.cssSelector("button[class*='TriangleRight collapsed']");
	public static By filterHeader = By.cssSelector("button[class*='icon trigger la-TriangleRight']");
	
	public static By eltFilterList = By.tagName("label");
	public static By eltFilterListHistory = By.tagName("span");

	public static By eltResultList = By.cssSelector("li[data-id*='sr']");
	public static By btnNextPage = By.cssSelector("a[data-action='nextpage']");
	public static By docLink = By.cssSelector("a[data-action='title']");

	public static By btnGetItNow = By.cssSelector("button[data-action='purchase']");

	public static By eltSnapshotList = By.cssSelector("a[data-action='snapshottitle']");
	public static By eltStatutes = By.cssSelector("button[data-id='urn:ict:1140']");
	
	public static By attachments = By.cssSelector("div[class='attachments']");
	
	
	

	public static By txtNewClientId = By.cssSelector("input[id='setClientIdText']");
	public static By rdoClientId = By.cssSelector("input[id='new_clientid']");
	public static By btnSaveClientId = By.cssSelector("input[id='submit_clientid']");

	public static By txtStudentReportHeader = By.cssSelector("h2[class='pagewrapper']");

	public static By eltSignInButton = By.cssSelector("div[id='lexisviews_embedded_signin']");
	public static By btnButton = By.tagName("button");
	public static By lnkSignOut = By.cssSelector("a[title='Sign Out']");

	public static By eltGoogleDiv = By.cssSelector("div[id='gs_top']");
	public static By eltGoogleInnerDiv = By.cssSelector("div[id='gs_ccl']");
	public static By eltGoogleSignOutDiv = By.cssSelector("div[id='lexisviews_embeddedsearchheader']");

	// TextBox
	public static By txtDateMinValue = By.cssSelector("input[class='min-val']");
	public static By txtDateMaxValue = By.cssSelector("input[class='max-val']");

	// Buttons
	public static By btnOKDateSelect = By.cssSelector("button[class='save']");
	public static By btnSelectMultiple = By.cssSelector("button[data-action='selmulti']");
	public static By OKSelMultiPopUp = By.cssSelector("input[data-action='confirm']");

	public static By btnResetHighlight = By.cssSelector("button[id='resetHighlight']");
	public static By btnResetEnabled = By.cssSelector("button[id='resetHighlight'][class*='enabled']");
	
	public static By btnClear = By.cssSelector("button[data-action='clear']");
	public static By btnRecentlyViewed = By.cssSelector("button[class='icon la-RecentlyViewed']");
	public static By btncancel = By.cssSelector("input[id='discard_clientid']");

	// Links
	public static By lnkFirstRef = By.cssSelector("a[data-action='firstref']");
	public static By vsaViewCharts = By.cssSelector("a[class='icon la-VSA'][data-action='viewcharts']");

	// Div
	public static By SelMultiPopUp = By.cssSelector("aside[class*='dialog selmulti']");
	

	// others
	public static By hdrResult = By.cssSelector("header[class*='banner results']");
	public static By FilterTitle = By.cssSelector("span[class='filtertitle']");
	public static By chkDocNo = By.cssSelector("input[id='documentNumber']");
	public static By btnGetIt = By.cssSelector("button[data-action='purchase']");
	

	
	
	

	

	
	public static By eltFilterList1 = By.cssSelector("span[class='filtertitle']");

	public static By imgCaseLawWarning = By.cssSelector("img[src*='CaselawWarning.png']");

	public static By lnkSS_NarrowByHN = By.cssSelector("span[class='SS_it']");

	public static By link = By.tagName("a");
	

	public static By eltEmailPopup = By.cssSelector("form[class*='dialog-content']");

	public static By txtEmail = By.cssSelector("input[id='EmailAddress']");
	public static By btnSendEmail = By.cssSelector("input[data-action='email']");
	public static By txtPopUpHeader = By.tagName("h1");
	
	public static By lstShepListContainer = By.cssSelector("div[class='indent']");
	
	public static By lnkHeader = By.tagName("a");
	public static By btnEmail = By.cssSelector("button[data-action='email']");
	public static By txtToAddress = By.cssSelector("input[id='EmailAddress']");
	public static By btnSubmitEmail= By.cssSelector("input[class*='primary']");
	public static By btnDownload= By.cssSelector("input[class*='primary']");
	public static By btnClose = By.cssSelector("input[class='close return primary']");
	public static By txtPopupHeader = By.tagName("h1");
	public static By svg = By.tagName("svg");
	public static By btnZoom = By.cssSelector("button[id='zoomIn']");
	public static By btnZoomOut = By.cssSelector("button[id='zoomOut']");
	
	
	public static By lnkShepardize = By.cssSelector("a[data-action='sheppreview']");
	public static By lstClassLinkSection = By.cssSelector("li[class='SS_RelatedLinksSection']");
	
	public static By btnFilter = By.cssSelector("button[class*='filterBtn']");
	public static By btnFakeForgetInfor = By.cssSelector("a[class='fake-button forget-info']");
	public static By btnjurisdiction = By.cssSelector("button[id='jurisdiction']");
	public static By chkBox = By.cssSelector("input[data-value='California']");
	public static By btnCategory = By.cssSelector("button[id='contenttype']");
	public static By chkBox1 = By.cssSelector("input[data-value='Cases']");
	public static By btnPAT = By.cssSelector("button[id='ssat']");
	public static By chkBox2 = By.cssSelector("input[data-value='ppt:1']");

	
	public static By eltShepSearchBan = By.cssSelector("div[class='toastmessage pagewrapper confirm']");
	public static By hdrBanner = By.cssSelector("header[class*='banner document']");
	
	public static By divShepards = By.cssSelector("section[id='Shepards']");
	public static By imgRed = By.cssSelector("img[src*='Negative']");
	public static By imgOrange = By.cssSelector("img[src*='Questioned']");
	public static By imgYellow = By.cssSelector("img[src*='Caution']");
	public static By imgGreen = By.cssSelector("img[src*='Positive']");
	public static By imgBlueI = By.cssSelector("img[src*='Referenced']");
	public static By imgBlueA = By.cssSelector("img[src*='Analyzed']");

	public static By eltFiltersUsed = By.cssSelector("ul[class='filters-used']");
	public static By eltResultListHeader = By.tagName("header");

	
	public static By eltResultCellGroup = By.cssSelector("div[class='cellgroup']");
	public static By lnkHeadNotes = By.cssSelector("a[class='headnotes']");
	public static By btnFilterRemove = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By divResultList = By.cssSelector("div[id*='resultlist']");
	public static By hdrResult1 = By.cssSelector("span[class*='query']");
	public static By imgRedTri = By.cssSelector("img[src*='recentlyenacted']");
	public static By imgYelTri = By.cssSelector("img[src*='pendingleg']");
	public static By taghdr = By.tagName("h4");
	public static By lnkActsAffecting = By.cssSelector("a[data-action='linkrcm']");
	public static By imgRedCir = By.cssSelector("img[src*='StatuteWarning']");

	public static By btnHistory = By.cssSelector("button[class='history-button icon']");
	public static By btnTriangleDown = By.cssSelector("button[class='icon la-TriangleDownAfter']");
	public static By logoProduct = By.cssSelector("div[id='nav_currentproduct_button']");
	public static By btnClassClient = By.cssSelector("button[class*='client-button icon']");
	public static By btnIdBrowse = By.cssSelector("button[id='nav_browse_btn']");
	public static By btnIdLexisAdvance = By.cssSelector("button[id='nav_productswitcher_arrowbutton']");
	//public static By txtIdSearch = By.cssSelector("input[id='searchTerms']");
	public static By txtIdSearch = By.cssSelector("textarea[id='searchTerms']");
	public static By lnkArticle = By.cssSelector("a[href*='articles_search']");
	public static By filtersCategory=By.cssSelector("ul[data-id='contenttype-rollup']");

	public static By lnkTxtFeedBack = By.cssSelector("a[id='Feedback']");

	public static By eltExpandedFilterHeader = By.cssSelector("button[class='icon trigger la-TriangleRight expanded']");
	public static By ulFiltersUsed = By.cssSelector("ul[class='filters-used']");
	public static By btnRemoveFilter = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By lstTagListItems = By.tagName("li");
	public static By filtersCourt = By.cssSelector("ul[class='supplemental filters court']");
	public static By filtersJurisdiction = By.cssSelector("ul[data-id='jurisdiction']");
	public static By filtersFederal = By.cssSelector("ul[data-id='pf0']");
	public static By tagBtn = By.tagName("button");
	public static By productLogo = By.cssSelector("a[class='researchhome'][data-action='researchhome']");
	public static By lpaProductLogo = By.cssSelector("a[class='lpahome'][data-action='lpahome']");
	public static By latProductLogo = By.cssSelector("a[class='taxhome'][data-action='taxhome']");
	public static By filtersKeyword = By.cssSelector("ul[data-id='term']");
	public static By cancelSelMultiPopUp = By.cssSelector("input[data-action='cancel']");
	public static By subPostFilter = By.cssSelector("ul[class='supplemental']");
	public static By previousBtn = By.cssSelector("nav[class='pagination']");
	public static By previousBtnActive = By.cssSelector("a[class*='la-TriangleLeft action']");
	public static By previousBtnInActive = By.cssSelector("span[class*='la-TriangleLeft']");

	public static By btnClassFilter = By.cssSelector("button[class*='filters icon la-TriangleDownAfter']");
	public static By btnClassClearFilter = By.cssSelector("button[class='clear-filters']");
	public static By btnClosePreFilterPopup = By.cssSelector("button[class='icon la-CloseRemove close']");
	public static By preFiltersDiv = By.cssSelector("div[class='searchfilters shown']");
	public static By resultwrapper = By.cssSelector("div[class*='wrapper']");
	public static By cellgrp = By.cssSelector("div[class*='cellgroup']");

	public static By pagination = By.cssSelector("nav[class='pagination']");
	public static By pageNumbers = By.cssSelector("a[class='action']");
	public static By printFriendlyView = By.cssSelector("button[data-action='printablepage']");
	public static By searchWithinResults = By.cssSelector("div[id='search-within']");
	public static By searchWithinSearchBox = By.cssSelector("textarea[class='search expandingTextarea']");
	public static By searchbox = By.cssSelector("input[type='search']");
	public static By searchWithinSearchButton = By.cssSelector("button[class*='Search src-submit']");
	public static By btnEachFilter = By.cssSelector("button[class*='icon la-CloseRemove']");

	public static By eltResultHeader = By.cssSelector("header[class='resultsHeader']");
	public static By currentPage = By.cssSelector("li[class='current']");
	public static By lstTagResult = By.cssSelector("li[data-id*='sr']");
	public static By lstContinue = By.cssSelector("li[class='continued']");
	public static By lstLastPno = By.cssSelector("li[class='overflow']");
	public static By NextBtnActive = By.cssSelector("a[data-action='nextpage']");
	public static By NextBtnInActive = By.cssSelector("span[class*='la-TriangleRight']");
	public static By btnPrePage = By.cssSelector("a[data-action='prevpage']");

	public static By eltShowTerms = By.cssSelector("span[class='hit']");
	public static By eltShowExtract = By.cssSelector("span[class='sentence']");
	public static By tooltip = By.cssSelector("span[class='tooltip']");

	public static By eltContentTypeHeader = By.tagName("dt");
	public static By btnClassArrow = By.cssSelector("button[type='button']");
	public static By divAction = By.cssSelector("aside[class*='supplemental overflow']");
	public static By divider = By.cssSelector("li[class*='divided']");
	public static By divider1 = By.cssSelector("div[class='divided']");
	public static By filterContainer = By.cssSelector("ul[id='refine']");
	public static By filterMore = By.cssSelector("button[data-action='moreless']");
	public static By teaserLink = By.cssSelector("a[data-action='teaser']");
	public static By businessDaily = By.cssSelector("select[name='hourofday'][id*='BusinessDaily']");
	public static By selected = By.cssSelector("optiion[selected='selected']");
	public static By emailonline = By.cssSelector("input[type='radio'][value='Email + Online']");
	public static By online = By.cssSelector("input[type='radio'][value='Online only']");
	public static By businessDailyradio = By.cssSelector("input[type='radio'][value='Business daily']");
	public static By dailyRadio = By.cssSelector("input[type='radio'][value='Daily']");
	public static By asUpdatesare = By.cssSelector("input[type='radio'][value='As updates are available']");

	public static By textRadio = By.cssSelector("input[type='radio'][value='Text']");
	public static By htmlRadio = By.cssSelector("input[type='radio'][value='HTML']");
	public static By supplementalFilters = By.cssSelector("ul[class*='supplemental filters']");

	public static By divIdBrowserMenu = By.cssSelector("div[id='exploreMenuWrapper']");
	public static By allSources = By.cssSelector("a[data-id='allsources']");
	public static By lstTagAside = By.tagName("aside");

	public static By filtersContainer = By.cssSelector("ul[class='supplemental']");
	public static By editAlert = By.cssSelector("button[class*='Edit action edit']");
	public static By updateAlert = By.cssSelector("button[class*='Update action row']");
	public static By blueDotted = By.cssSelector("span[class*='icon la-NewUpdates']");

	public static By alertName = By.cssSelector("input[id='profilename']");
	public static By clientOption = By.cssSelector("input[id='recentclient']");
	public static By shareUserName = By.cssSelector("input[class*='wordwheel']");
	public static By shareTabContent = By.cssSelector("fieldset[class='supplemental']");
	public static By addToShare = By.cssSelector("input[class='secondary addContact']");
	public static By alertsDialogBox = By.cssSelector("aside[class='dialog alerts_dialog findcommon_dialog tabbed']");

	public static By wordWheelContent = By.cssSelector("div[class*='wordwheel-contents']");
	public static By lstTagUList = By.tagName("ul");
	public static By addToSharePP = By.cssSelector("input[id='addtocontact']");
	public static By dataTerms = By.tagName("dt");
	public static By dataDefn = By.tagName("dd");
	public static By alertTitle = By.cssSelector("input[id='profilename']");
	public static By monitorTabContent = By.cssSelector("li[class*='createalert monitor-tab']");
	public static By recentclient = By.cssSelector("select[id*='recentclients'][name='clientid']");
	public static By lstOption = By.tagName("option");
	public static By monitor = By.cssSelector("li[class*='createalert monitor-tab']");
	public static By button = By.tagName("button");
	public static By dailyradio = By.cssSelector("input[type='radio'][value='Daily']");
	public static By dailytime = By.cssSelector("select[name='hourofday'][id*='howoften_Daily']");

	public static By addalert = By.cssSelector("button[data-alerttype='Regulatory']");
	public static By dropdownContainer = By.cssSelector("div[class='dropdownContainer']");
	public static By sortByBtn = By.cssSelector("button[class*='icon la-Triangle']");
	public static By sortByList = By.cssSelector("aside[class='supplemental sortdropdown']");
	public static By sortByJumpList=By.cssSelector("aside[class='supplemental jumptodropdown']");
	public static By nextPage = By.cssSelector("a[data-selectedvalue='nextpage']");
	public static By sortByContainer = By.cssSelector("div[class='dropdownContainer']");
	public static By sortByButton = By.cssSelector("button[class*='current trigger']");
	public static By sortByMenu = By.cssSelector("aside[class='supplemental sortdropdown']");
	public static By docContent = By.cssSelector("div[class='row-content']");
	public static By docContent1 = By.cssSelector("div[class='result-data']");

	public static By addalertreg = By.cssSelector("button[data-alerttype='Regulatory']");
	public static By chkbox = By.cssSelector("input[type='checkbox']");
	public static By alertPending = By.cssSelector("button[data-alerttype='PendingLegislative']");
	public static By addalertleg = By.cssSelector("button[data-alerttype='Legislative']");

	public static By clientName = By.cssSelector("dd[class='client-name']");
	public static By history = By.cssSelector("button[id*='nav_historymenu']");
	public static By viewAllhistory = By.cssSelector("a[data-action*='history']");
	public static By dd = By.tagName("dd");
	public static By dt = By.tagName("dt");

	// In Full Document
	public static By textsectionfulldoc = By.cssSelector("h2[class='SS_Banner'][id='JUMPTO_Text']");
	public static By noteslinkfulldoc = By.cssSelector("a[class='SS_PopupLink'][name='fnlink_mnr_1']");
	public static By notespopupdisplay = By.cssSelector("div[class='SS_Popup showPopup'][id='fntext_mnr_1']");
	public static By notespopupheadertext = By.cssSelector("span[class='SS_PopupHdr']");
	// public static By fulldocpagenumber =
	// By.cssSelector("span[name='PAGE_8326']");
	public static By fulldocpagenumber = By.cssSelector("span[id='PAGE_2847_8326']");
	public static By asideBlock = By.cssSelector("aside[class='doccontextmenu selectedtextmenu']");
	public static By asideBlockxpath = By.xpath("html/body/aside");

	public static By folderIcon = By.cssSelector("button[class*='icon action la-SavedToFolder']");// icon
																									// action
																									// la-Folder
	public static By folderName = By.cssSelector("a[class*='la-Folder']");
	public static By titleWf = By.cssSelector("a[data-action*='link']");
	public static By update = By.cssSelector("span[id='updatesignaldate']");
	public static By divWow = By.cssSelector("div[id='wow']");

	public static By errormessage = By.cssSelector("div[class='errorHeading']");
	public static By textsectiondblclick = By.cssSelector("span[id='PAGE_2848_8326']");

	public static By showMoreButton = By.cssSelector("button[data-action='moretypes']");
	public static By casesButton = By.cssSelector("button[data-id='urn:hlct:5']");
	public static By searchControl = By.cssSelector("aside[class='search-controls']");
	public static By ict = By.cssSelector("ul[class='content-switcher open expanded']");
	public static By casesHeader = By.tagName("li");

	public static By divWrapperPanel = By.cssSelector("div[class='wrapper pagewrapper panel ']");
	
	public static By fileNameField = By.cssSelector("input[id='FileName']");

	public static By actualLinkSection = By.cssSelector("section[class='doc-content SS_contentdocument small']");
	public static By actualLink = By.tagName("a");

	public static By searchTermLink = By.cssSelector("a[data-pdsearchterms='32CFR']");

	public static By searchTypeFilter = By.cssSelector("ul[data-id='type']");

	public static By supplementalFiltersList = By.cssSelector("ul[class*='default']");
	public static By TitleClassDocFolder = By.cssSelector("h2[class*='doc-title']");

	public static By lnkPrint = By.cssSelector("button[data-action='print']");
	public static By lnkUseDefaultSettPrint = By.cssSelector("button[data-action='printnow']");

	public static By documentcount = By.cssSelector("span[class='count']");

	public static By activetab = By.cssSelector("li[class='Active']");

	public static By resultListDiv = By.cssSelector("div[id='researchMapGraph']");
	public static By displayItem = By.cssSelector(".displayItem");

	public static By lnkDownload = By.cssSelector("button[data-action='download']");
	public static By lnkUseDefaultSett = By.cssSelector("button[data-action='downloadnow']");

	public static By historyListContainer = By.cssSelector("div[class='wrapper']");
	public static By listItems = By.tagName("li");
	public static By historyPopup = By.cssSelector("form[class='dialog-content']");
	public static By historyPopupClose = By.cssSelector("input[value='Close']");

	public static By ulclass = By.cssSelector("ul[class='SS_NoStyleList']");
	public static By sectiong = By.cssSelector("a[id='_j_10_g'][class='textHilg']");
	public static By spanclass = By.cssSelector("span[class='SS_bf']");

	public static By filtersLocation = By.cssSelector("ul[data-id*='location']");
	public static By spancount = By.cssSelector("span[class='count']");
	public static By divheader = By.cssSelector("div[class='resultListheader']");
	public static By listtag = By.tagName("li");
	
	public static By atag = By.tagName("a");
	public static By oltag = By.tagName("ol");
	public static By header4 = By.tagName("h4");

	public static By secIdMainTopics1 = By.cssSelector("section[id='mainTopics']");

	public static By buttonsCont = By.cssSelector("aside[class='mrheader_right']");
	public static By selectedButton = By.cssSelector("button[class*='selected']");
	public static By illustrationsButton = By.cssSelector("button[data-action='medillustrations']");
	public static By headerContainer = By.cssSelector("section[id='signpost']");
	public static By backToMedTopics = By.cssSelector("a[id='viewAllTopics']");
	public static By TitleClassReport = By.cssSelector("h2[class*='reportName']");
	public static By reportLink = By.cssSelector("a[id*='sr']");
	public static By aresultslink = By.cssSelector("a[data-action='linklitresults']");

	public static By recentFolderList = By.cssSelector("aside[class='supplemental recentlyusedfolders']");
	public static By recentFolderName = By.tagName("li");

	public static By pgHeaderOption = By.cssSelector("div[class*='currentproduct']");
	public static By checkBoxCont1 = By.cssSelector("div[class='check']");
	public static By lasacMessage1 = By.cssSelector("div[class='laacmessage']");
	public static By txtSearchName = By.cssSelector("input[id='searchtitle']");
	public static By quickSaveFolder = By.cssSelector("button[id='quicksavebutton'][data-savetype='QuickSave']");
	public static By wholeText = By.cssSelector("main[class*='resultlist']");
	
	public static By lnkresult=By.cssSelector("a[data-action='linksearch']");
	public static By pdflink = By.cssSelector("a[data-action='pdflink']");
	public static By emailFormat = By.cssSelector("label[id='Format'][data-value='Pdf']");
	public static By stuReport = By.cssSelector("table[id='studentReportTable']");
	public static By selectAllItemsUl=By.cssSelector("ul[class='menu overflowTo']");
	public static By selectAllItemsCheckBox=By.cssSelector("input[data-action='selectall']");
	public static By selectAllItemsCount=By.cssSelector("li[class='count']");
	public static By popupDiv=By.cssSelector("div[id='popup']");
	public static By popup=By.cssSelector("aside[class='dialog citationpopup']");
	public static By popupOk=By.cssSelector("input[id='warningCloseButton']");
	public static By documentText=By.cssSelector("span[class='SS_LeftAlign']");
	public static By highlightText=By.cssSelector("span[class='SS_Pag_Show textHighlight']");
	public static By highlightText1=By.cssSelector("span[class='SS_Pag_Show textHilg']");
	
	public static By toolbar=By.cssSelector("div[class='toolbar noreader sticky']");
	public static By snapToolbar=By.cssSelector("div[class='toolbar sticky']");
	public static By citationHeader=By.cssSelector("header[class='divided citref']");
	public static By viewReferences=By.cssSelector("a[data-action='viewcitations']");
	public static By postFilters=By.cssSelector("ul[class='supplemental']");
	public static By getItNow=By.cssSelector("button[data-action='purchase']");
	public static By getItNowSearch=By.cssSelector("input[name='purchase']");
	public static By overview=By.cssSelector("p[class='overview min']");
	public static By docText=By.cssSelector("p[class='min']");
	
	public static By eltCollapsedFilterHeaderOne = By.cssSelector("button[class='icon trigger la-TriangleRight']");
	
	
	public static By headerh1=By.cssSelector("h1[class='contenttypeTitle']");
	
	public static By permaLinkDiv = By.cssSelector("div[class='comp permalinkgatekeeper']");
	public static By tabList=By.cssSelector("ul[class='wftabheaders']");
	public static By tabList1=By.cssSelector("ul[class='tabheaders']");
	public static By leftPanelCont = By.cssSelector("div[class='sotLeftPanel']");
	public static By radio = By.cssSelector("input[type='radio']");
	public static By btnSelectMultipleCB = By.cssSelector("button[data-event='selmulti']");
	
	public static By h2tag=By.tagName("h2");
	public static By timelineCont=By.cssSelector("div[class='supplemental timeline']");
	public static By sliderBase=By.cssSelector("div[class='noUi-base']");
	public static By lower=By.cssSelector("div[class='noUi-handle noUi-handle-lower']");
	public static By upper=By.cssSelector("div[class='noUi-handle noUi-handle-upper']");
	public static By labelsCont=By.cssSelector("div[class='labels']");
	public static By btnClientID=By.cssSelector("button[class='menulabel client-button icon']");
	
	public static By amtAwardDiv = By.cssSelector("div[data-id='amountawarded']");
	public static By okButton = By.cssSelector("input[value='OK']");
	public static By amountLowInput = By.cssSelector("input[id='amountawardedlow']");
	public static By amountHighInput = By.cssSelector("input[id='amountawardedhigh']");
	public static By webLink = By.cssSelector("a[class='icon la-ExternalLinkAfter']");
	
	public static By h1 = By.cssSelector("h1[id='SS_DocumentTitle']");
	
	public static By menu1 = By.cssSelector("ul[class='menu overflowTo']");
	public static By button1 = By.cssSelector("button[class='dropdown-item action']");
	
	public static By connectorMsg = By.cssSelector("dd[class='connectorFunction']");

	public static By CancelSelMultiPopUp = By.cssSelector("input[data-action='cancel']");

	public static By hdrDoc = By.cssSelector("header[class*='banner']");
	public static By startYear=By.cssSelector("input[class='min-val'][type='text']");
	public static By endYear=By.cssSelector("input[class='max-val'][type='text']");
	public static By timeLineOkButton=By.cssSelector("button[class='save btn secondary'][type='button']");
	public static By preferredFilters=By.cssSelector("li[class='preferred']");
	public static By h3=By.tagName("h3");
	public static By edit=By.cssSelector("a[data-action='editpreferred']");
	public static By lnkShepardizeThisDoc = By.cssSelector("a[data-pdlinktype='ShepardsReport']");
	public static By tagSpan = By.tagName("span");
	public static By preferredEdit = By.cssSelector("a[data-action='editpreferred']");
	public static By pgTagHeader = By.tagName("h1");
	public static By tagHeader = By.tagName("h2");
	public static By TitleClassTOC1 = By.cssSelector("h1[class='pagewrapper']");
	public static By backToTop = By.cssSelector("button[class='icon la-JumpToTop']");
	public static By didYouMean = By.cssSelector("header[class='suggest pagewrapper']");	

	public static By activeContentType = By.cssSelector("li[class='active ']");
	public static By contenttyperesult = By.cssSelector("div[class='contentTypeResultData']");
	public static By flagPresent =By.tagName("span");
	public static By startDateCalender= By.cssSelector("button[aria-label='Pick Minimum Date']");
	public static By startDay= By.cssSelector("button[data-monthday='15']");
	public static By sliderStartYear = By.cssSelector("span[class='min-label']");
	public static By sliderEndYear = By.cssSelector("span[class='max-label']");
	public static By h2Document= By.tagName("h2");
	public static By morechildFilterOption=By.cssSelector("button[data-action='moreless']");
	public static By favBtn=By.cssSelector("button[class='icon la-FavoriteEmpty']");
	public static By frmSave = By.cssSelector("input[value='Save']");
	public static By frmCancel = By.cssSelector("input[value='Cancel']");
	public static By docText1 = By.cssSelector("span[class='SS_LeftAlign']");
	public static By embed = By.cssSelector("a[class='SS_EmbeddedLink']");
	public static By mobileToolbar = By.cssSelector("div[class='mobileToolbar']");
	public static By Access = By.cssSelector("span[class='pinfo access']");
	
	// page wrapper
	public static By pageWrapper = By.cssSelector("div[class='wrapper panel pagewrapper']");
	
	//save folder
	public static By saveFolder = By.cssSelector("input[value='Save'][data-id='SaveSearch']");
	public static By highlight = By.cssSelector("span[class='hit']");
	public static By highlight1 = By.cssSelector("span[class*='SS_SH'][class*='SS_prior']");
	public static By resViewFull = By.cssSelector("button[class*='la-ListFull'][data-action='viewfull']");
	public static By resViewtitle = By.cssSelector("button[class*='la-ListTitleView'][data-action='viewmin']");
	

	//result
	public static By resultlist = By.cssSelector("form[class='results-list search']");
	public static By doctitle = By.cssSelector("div[class='wrapper']");

	public static By wholeoopmsginresults = By.cssSelector("span[class='pinfo access']");
	public static By oopmsginresults = By.cssSelector("span[class='pinfo access']");	
	public static By resultList = By.cssSelector("aside[class='search-controls']");
	public static By eltResult = By.cssSelector("form[class='results-list relatedcontentview']");
	public static By latContentType = By.cssSelector("ul[aria-live='polite']");
	public static By moreInfoSegmentSearching = By.cssSelector("a[data-helpkey='searchsegment_hdi-task']");
	public static By TitleDocPurinfo = By.cssSelector("div[class*='row-content']");
	public static By workfolderPopup = By.cssSelector("aside[class*='workfolder_dialog']");

	//Add to folder
	public static By addTofolder = By.cssSelector("li[class='folder']");
	public static By teaserText = By.cssSelector("a[data-action='teaserlink']");
		public static By weburl = By.cssSelector("a[data-action='weburitemplate']");
	public static By article = By.tagName("article");
	//doctitle
	public static By TitleDoc = By.cssSelector("h2[class='doc-title caution']");
	public static By divshep = By.cssSelector("div[class='shepListView']");

	public static By filterheader2 = By.cssSelector("ul[class='supplemental filters ']");
	
	// Check recipient
		public static By notesView= By.cssSelector("span[id='postAsPublicContainer']");
		public static By chkRecipient = By.cssSelector("input[id='PostAsPublic']");
		public static By option=By.cssSelector("li[id='SelecteduserListColumn']");
		public static By selectOption=By.cssSelector("select[id='roleEditorViewer']");
		public static By editor=By.cssSelector("option[value='Editor']");
		public static By viewer=By.cssSelector("option[value='Viewer']");
		public static By parent=By.cssSelector("div[class='tinymce_container']");
		public static By notepopup=By.cssSelector("div[class='dialog-content']");
		public static By imgRed1 = By.cssSelector("img[title*='Negative']");
		


		public static By divadvance = By.cssSelector("div[class='comp container_signpost']");
		public static By h2advance = By.cssSelector("h2[class='pagewrapper']");
		
		public static By segmentimagediv = By.cssSelector("div[class='connectors']");
		public static By segmentchilddiv = By.cssSelector("div[class='segmentsample']");
		public static By segmentimage = By.cssSelector("img[title='Segment Example']");
		
		public static By divcontenttype = By.cssSelector("div[class='documentslist']");
		public static By divcategory = By.cssSelector("div[class='categories']");
		public static By uisttagcontent = By.tagName("ul");
		public static By listtagcontent = By.tagName("li");
		public static By contenttypebutton = By.cssSelector("button[class='icon la-TriangleRight']");
		
		public static By doctitleheader = By.cssSelector("span[class='documenttitle col']");
		public static By eltExpandedFilterHeader1 = By.cssSelector("button[class*='la-TriangleRight expanded']");
		public static By highlightedBoldedTerm = By.cssSelector("span[class='hit']");
		public static By eltShowTerm = By.cssSelector("span[id='h2']");
		public static By reference = By.cssSelector("a[data-action='bnacch']");
		public static By searchBox = By.cssSelector("textarea[id = 'searchTerms']");
		public static By termsteaserLink = By.cssSelector("a[data-action='hitsteaser']");

}
	
	

