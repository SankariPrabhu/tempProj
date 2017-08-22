package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Shepards {
	//comment

	public static By title = By.cssSelector("input[id='profilename']");
	public static By createAlert = By.cssSelector("input[value='Create Alert']");
	public static By btnCreateAlert = By.cssSelector("input[type='submit'][value='Create Alert']");
	public static By recentChanges = By.cssSelector("aside[class='recent-enact']");
	public static By resultsHeaderDiv = By.cssSelector("div[class*='pagewrapper panelless']");
	public static By resultsHeader = By.tagName("header");
	public static By shepThisDoc = By.cssSelector("a[id='fallback']");
	public static By lnkCitingDecisions = By.cssSelector("button[data-id='citingref']");
	public static By lnkAppelatehistory = By.cssSelector("button[data-id='history']");
	public static By lnkOtherCitingResources = By.cssSelector("button[data-id='othercitingref']");
	public static By lnkTableOfAuthorities = By.cssSelector("button[data-id='toa']");
	public static By btnIdListView = By.cssSelector("button[id='ListViewLinkButton']");
	public static By btnIdGridMapView = By.cssSelector("button[id='GridMapViewLinkButton']");
	public static By btnIdFullscreen = By.cssSelector("button[id='fullscreen']");
	public static By divIdDisplayOptions = By.cssSelector("div[id='wowShow']");
	public static By chkIdDocumentNumber = By.cssSelector("input[id*='documentNumber']");
	public static By btnValOK = By.cssSelector("input[value='OK']");
	public static By btnMapPointer = By.cssSelector("circle[style*='pointer']");
	public static By lnkIdShowInList = By.cssSelector("a[id='showInList']");
	public static By divIdBubble = By.cssSelector("div[id='bubbleText']");
	public static By lnkIdOpenDocument = By.cssSelector("a[id='openDocument']");
	public static By divIdShepListView = By.cssSelector("div[id='shepListView']");
	public static By HeaderResults = By.cssSelector("header[class*='results-data']");
	public static By hdrResult1 = By.cssSelector("span[class*='query']");
	public static By hdrResult = By.cssSelector("header[class*='banner results']");
	public static By btnIdGridView = By.cssSelector("button[id='GridViewLinkButton']");
	// grid parent
	public static By gridParent = By.cssSelector("aside[id='shep-result-info']");
	public static By divWowheader = By.cssSelector("div[id='wowHeader']");
	public static By divResultPage = By.cssSelector("main[class*='shepardsresults']");
	public static By btnDownload = By.cssSelector("input[class*='primary']");
	public static By btnClear = By.cssSelector("button[data-action='clear']");
	public static By lstSelected = By.cssSelector("li[class='active']");

	public static By imgClose = By.cssSelector("image[id='closeBubble']");
	public static By txtPopUpHeader = By.tagName("h1");
	public static By hdrBanner = By.cssSelector("header[class*='banner document']");
	public static By h1 = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By eltCourtNameLinksContainer = By.cssSelector("tr[class='ln-row-courts']");
	public static By lnkCourtName = By.cssSelector("button[class='btn-link']");
	public static By tagSpan = By.tagName("span");
	public static By SearchResultHeader3 = By.cssSelector("header[class*='document-data']");

	public static By pgClassHeaderOption3 = By.cssSelector("header[class*='header']");
	public static By SearchResultHeader = By.cssSelector("header[class='resultsHeader']");
	public static By SearchResultHeader1 = By.cssSelector("header[class*='results-data']");
	public static By frmClassResult = By.cssSelector("form[class*='results-list']");
	public static By TitleClassDoc = By.cssSelector("h2[class*='doc-title']");
	public static By lnkDocTitle = By.cssSelector("a[data-action='midlinetitle']");
	public static By divClassFiltersUsed = By.cssSelector("span[class='appfiltertitle']");
	public static By treatPhrase = By.cssSelector("span[class='treatphrase']");
	public static By lnkShepardizeThisDoc = By.cssSelector("a[data-pdlinktype='ShepardsReport']");
	public static By lnkSubSeqAppHistory = By.cssSelector("button[class='subseq']");
	public static By chickletImg = By.tagName("img");
	public static By chickletImgText = By.tagName("a");

	public static By btnResetHighlight = By.cssSelector("button[id='resetHighlight']");

	public static By chkDocNo = By.cssSelector("input[id='documentNumber']");
	public static By btnOK = By.cssSelector("input[value='OK']");
	public static By btnResetEnabled = By.cssSelector("button[id='resetHighlight'][class*='enabled']");
	public static By eltResultListWrapper = By.cssSelector("div[class='wrapper pagewrapper panel ']");

	public static By eltResultListHeader = By.tagName("header");
	public static By eltShepListHeader1 = By.tagName("h2");
	public static By divAnalysisByCourt = By.cssSelector("div[id='ln-shep-citation-grid']");
	public static By pgTagHeader = By.tagName("h1");
	public static By lnkTopLineTitle = By.cssSelector("a[data-action='toplinetitle']");
	public static By tagHeader = By.tagName("h2");
	public static By TitleClassTOC = By.cssSelector("h2[class='pagewrapper']");
	public static By btnRecentlyViewed = By.cssSelector("button[class='icon la-RecentlyViewed']");
	public static By lnkFirstRef = By.cssSelector("a[data-action='firstref']");
	public static By lnkSubsecReports = By.cssSelector("a[data-action='subsectionreport']");
	public static By fileNameField = By.cssSelector("input[id='FileName']");
	public static By txtToAddress = By.cssSelector("input[id='EmailAddress']");
	public static By btnSubmitEmail = By.cssSelector("input[class*='primary']");
	public static By divWow = By.cssSelector("div[id='wow']");
	public static By rect = By.tagName("rect");
	public static By link = By.tagName("a");
	public static By lstTagResult = By.cssSelector("li[data-id*='sr']");

	public static By btnActiveList = By.cssSelector("button[class*='shep-active toggle']");
	public static By lnkShowInMap = By.cssSelector("a[data-lnk='showinmap']");
	public static By btnActiveMap = By.cssSelector("button[class='shep-active action']");
	public static By lnkOpenDocument = By.cssSelector("a[id='openDocument']");
	public static By divWowScene = By.cssSelector("div[id='wowScene']");
	public static By btnActiveGrid = By.cssSelector("button[class='shep-active']");

	public static By divWrapperPanel = By.cssSelector("div[class='wrapper pagewrapper panel ']");

	public static By eltYaxisContainer = By.cssSelector("ul[class='ln-table-heading']");

	public static By lnkYaxis = By.cssSelector("button[class='btn-link']");

	public static By path = By.cssSelector("path[style*='opacity']");

	public static By btnMap = By.cssSelector("button[id='GridMapViewLinkButton']");
	public static By btnStarNode = By.tagName("circle");

	public static By lnkShowInList = By.cssSelector("a[id='showInList']");
	public static By eltSelectedItem = By.cssSelector("li[class='selected']");

	public static By eltEmailPopup = By.cssSelector("form[class*='dialog-content']");

	public static By txtEmail = By.cssSelector("input[id='EmailAddress']");
	public static By btnSendEmail = By.cssSelector("input[data-action='email']");

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

	public static By eltResultCellGroup = By.cssSelector("div[class='cellgroup']");
	public static By lnkHeadNotes = By.cssSelector("a[class='headnotes']");
	public static By btnFilterRemove = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By divResultList = By.cssSelector("div[id*='resultlist']");
	public static By eltShepSearchNot = By.cssSelector("div[class='sheperror']");
	
	

	public static By suggestionContent = By.cssSelector("div[class='suggbox']");
	public static By preferredEdit = By.cssSelector("a[data-action='editpreferred']");
	public static By imgRedTri = By.cssSelector("img[src*='recentlyenacted']");
	public static By imgYelTri = By.cssSelector("img[src*='pendingleg']");
	public static By taghdr = By.tagName("h4");
	public static By lnkActsAffecting = By.cssSelector("a[data-action='linkrcm']");
	public static By imgRedCir = By.cssSelector("img[src*='StatuteWarning']");
	public static By dailyRadio = By.cssSelector("input[type='radio'][value='Daily']");
	public static By asUpdatesare = By.cssSelector("input[type='radio'][value='As updates are available']");

	public static By textRadio = By.cssSelector("input[type='radio'][value='Text']");
	public static By htmlRadio = By.cssSelector("input[type='radio'][value='HTML']");
	public static By supplementalFilters = By.cssSelector("ul[class*='supplemental filters']");
	public static By btnNextPage = By.cssSelector("a[data-action='nextpage']");
	public static By docLink = By.cssSelector("a[data-action='title']");

	public static By lstShepListContainer = By.cssSelector("div[class='indent']");

	public static By eltFiltersUsed = By.cssSelector("ul[class='filters-used']");
	public static By alertTitle = By.cssSelector("input[id='profilename']");
	public static By monitorTabContent = By.cssSelector("li[class*='createalert monitor-tab']");
	public static By searchWithinResults = By.cssSelector("div[id='search-within']");
	
	//search within search
	public static By searchWithinSearch = By.cssSelector("input[id='src-txt']");
	
	public static By searchWithinSearchBox = By.cssSelector("textarea[class='search expandingTextarea']");
	public static By searchWithinSearchButton = By.cssSelector("button[class*='Search src-submit']");
	public static By lstTagName = By.tagName("li");
	public static By tagNameAside = By.tagName("aside");
	public static By addAlertShepards = By.cssSelector("button[data-alerttype='Shepards']");
	
	/// informationPhrase
	public static By informationPhrase = By.cssSelector("section[id='shep-infobox']");
		
	public static By menu1 = By.cssSelector("ul[class='menu overflowTo']");
	public static By hdrDoc = By.cssSelector("header[class*='banner']");
	public static By shepDocLink = By.cssSelector("a[data-action='toplinetitle']");
	public static By articleTag = By.tagName("article");
	public static By imgTag = By.tagName("img");
	public static By filterUsed = By.cssSelector("ul[id='refine']");

	public static By subsequentDoc = By.cssSelector("div[class='shep-incidentbox']");

	public static By docnumber = By.cssSelector("span[class='docnumber']");
	public static By starIcon = By.cssSelector("img[src*='IconShepStar']");
	public static By shepardized = By.cssSelector("span[class='shepardized']");
	public static By path1 = By.tagName("path");
	public static By btnStarNode1 = By.className("circle[opacity='1']");
	public static By divIdBubble1 = By.cssSelector("svg[id='bubble']");
	public static By svgsection = By.cssSelector("svg[style*='266']");
	public static By svgsection1 = By.cssSelector("svg[style*='hidden']");
	public static By rect1 = By.tagName("rect");
	public static By motion = By.cssSelector("span[class='treatphrase']");
	public static By motionLink = By.cssSelector("a[class='citedby']");
    public static By shepPopUp = By.cssSelector("div[class='content']");
    public static By clickLinkPopUp = By.cssSelector("a[class='editorialphrase']");
    public static By closeButton = By.cssSelector("a[class='jqmClose']");
    public static By distin = By.cssSelector("a[data-treatmentnumber='74']"); 
	
	
	public static By teaser1 = By.cssSelector("span[id*='teaser']");
	public static By teaser2 = By.cssSelector("span[class='hit']");

	public static By shepReferenceButton = By.cssSelector("div[class='fieldset termNavigateJumpTo']");
    public static By referenceNextterm = By.cssSelector("button[data-action='nextterm']");
    public static By highlightDoc = By.cssSelector("span[class='SS_prior SS_SH textHighlight']");

	public static By shepSignal = By.cssSelector("a[data-action='shepardsignal']");
	public static By docNo = By.cssSelector("span[class='docnumber']");
	//data-id="sr1"

	public static By minTest = By.cssSelector("input[class='min-val']");
	public static By maxTest = By.cssSelector("input[class='max-val']");
	public static By btnTimeline = By.cssSelector("button[class='save']");
	public static By filterHeader = By.cssSelector("button[class*='icon trigger la-TriangleRight']");
}
