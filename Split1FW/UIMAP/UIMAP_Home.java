package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Home {

	// Links
	public static String lnkHrefSignOff = "logoff";
	public static String lnkTextSignOut = "Sign Out";
	public static By lnkTextSettings = By.linkText("Settings");
	// buttons
	public static By btnTitleMore = By.cssSelector("button[title='More']");
	public static By btnhrefForgetMe = By.cssSelector("a[href*='UnsaveRememberMe']");
	public static By btnIdFolder = By.cssSelector("a[id='Folder']");
	public static String btnClassHistory = "history-button icon";
	public static By btnClassClient = By.cssSelector("button[class*='client-button icon']");
	public static By btnIdBrowse = By.cssSelector("button[id='nav_browse_btn']");
	public static By btnIdLexisAdvance = By.cssSelector("button[id='nav_productswitcher_arrowbutton']");
	
	
	public static By btnIdSubSearch = By.cssSelector("input[id='subSearch']");
	public static By btnIdSearch = By.cssSelector("button[id='mainSearch']");
	public static By btnIdHistoryMenuArrow = By.cssSelector("button[id='nav_historymenu_arrowbutton']");
	public static By btnIdRecentDocuments = By.cssSelector("button[class='recentdocbtn']");
	public static By btnClassFilter = By.cssSelector("button[class*='dropdown filters']");
	public static By btnIdjurisdiction = By.cssSelector("button[id*='jurisdiction']");
	public static By btnIdcontenttype = By.cssSelector("button[id*='contenttype']");
	public static By btnIdPracticeAreaTopic = By.cssSelector("button[id*='ssat']");
	public static By btnIdRecentFavorites = By.cssSelector("button[id*='recent-favorites']");
	public static By btnIdAdvanced = By.cssSelector("button[id*='advanced']");
	public static By btnIdFilterMenu = By.cssSelector("button[type='button']");
	public static By btnIdFilterSearch = By.cssSelector("input[id='subSearch']");
	public static By btnClassMoreFilters = By.cssSelector("section[class='morefilterslist']");

	public static By btnFolders = By.cssSelector("a[data-action='workfolders']");

	// Test boxes
	//public static By txtIdSearch = By.cssSelector("input[id='searchTerms']");
	public static By txtIdSearch = By.cssSelector("textarea[id='searchTerms']");

	public static By txtIdprofileSuiteTextbox = By.cssSelector("input[id='searchTerms']");

	public static By txtIdprofileSuiteTextbox1 = By.cssSelector("input[id='searchTerms']");
//	public static By btnIdprofileSuiteSearch = By.cssSelector("button[id='searchTrigger']");
	
//	public static By txtIdprofileSuiteTextbox1 = By.cssSelector("input[id='searchTerms']");
	public static By btnIdprofileSuiteSearch = By.cssSelector("button[id='mainSearch']");

	public static By btnIdprofileSuiteFilter = By.cssSelector("button[class*='showPreFilter']");
	public static By btnIdprofileSuiteSubSearch = By.cssSelector("input[class*='searchFooterBtn']");

	public static By txtIdVerdictTextbox = By.cssSelector("textarea[id='searchTerms']");
	public static By btnIdVerdictSearch = By.cssSelector("button[id='mainSearch']");
	public static By btnIdVerdictFilter = By.cssSelector("button[data-id*='prefilters']");
	public static By btnIdVerdictSubSearch = By.cssSelector("input[data-id='confirm']");

	public static By txtIdMedMalSearch = By.cssSelector("input[id='search-medMal-medical-topic']");
	public static By btnIdMedMalSearch = By.cssSelector("input[id='landingSearch']");
	public static By btnIdMedMalFilter = By.cssSelector("button[data-id*='prefilters']");
	public static By btnIdMedMalSubSearch = By.cssSelector("input[data-id='confirm']");
	public static By btnClassClearFilter = By.cssSelector("button[class='clear-filters']");

	public static By txtclassSearch = By.cssSelector("input[class='search']");

	// Checkboxes
	public static By chkTypeCheckbox = By.cssSelector("input[type='checkbox']");

	// radio
	public static By rdoIdSrchType = By.cssSelector("input[id='r_practicearea'][name='pdsearchtype']");
	public static By rdoIdAllTopic = By.cssSelector("input[id='r_alltopics'][name='pdsearchtype']");

	// Links
	public static By lnkTxtViewAllHistory = By.linkText("View all history");
	public static By lnkTxtResearchMap = By.linkText("Research Map");
	public static By btnActionSetAddClientID = By.cssSelector("button[data-action='setclientid']");
	public static By lnkTxtNone = By.linkText("-None-");
	// Others
	public static String podClassHistory = "historypod";
	public static String podClassFolders = "workfolder_pod";
	public static String podClassFavorites = "favoritespod";
	public static String podClassAlert = "alert_pod";
	public static String podClassnotification = "notification_pod";
	public static String podClassSupport = "nav_supportpod";
	public static String podClassNewspod = "resultslistpod";
	public static String podClassLatestupdates = "highlanderpod";
	public static By lstrecentDocumentsContent = By.cssSelector("ul[class='recentdocuments']");
	public static By mnuFilterToolBar = By.cssSelector("menu[type='toolbar'][class='toolBarMenu']");
	public static By mnuNarrorFilter = By.cssSelector("menu[type='toolbar'][class='filters']");
	public static By divClassDeleteFilter = By.cssSelector("div[class*='deleteFilter']");
	public static By divRecentFilters = By.cssSelector("div[class*='recent-favorites-filters']");
	public static By divClassCTFilters = By.cssSelector("div[class='contenttype-filters']");
	public static By divClassFederalFilters = By.cssSelector("div[class='federal-prefilters']");
	public static By divClassPracticeArea = By.cssSelector("div[class='ssat-filters']");
	public static By divClassLPAPracticeArea = By.cssSelector("div[class='lpassa-filters']");
	public static By divClassStateFilters = By.cssSelector("div[class='states-prefilters']");
	public static By divIdBrowserMenu = By.cssSelector("div[id='exploreMenuWrapper']");
	public static By divIdBrowserSubMenu = By.cssSelector("div[id='exploreMenuMillerMenu']");
	public static By divClassActionsList = By.cssSelector("div[class*='actionsList']");
	public static By tblIdPermUseSteps = By.cssSelector("table[id='permUseSteps']");
	public static By iframe = By.cssSelector("iframe[id='publicRecordsUrl']");

	public static By FilterIdCount = By.cssSelector("strong[id='filterCount']");

	public static By lstTagUList = By.tagName("ul");
	public static By lstTagListItems = By.tagName("li");
	public static By lstTagAside = By.tagName("aside");
	public static By header = By.tagName("header");
	public static By btnTypeButton = By.tagName("button");
	public static By actionlink = By.cssSelector("a[class*='external icon']");
	public static By actionButtonTopics = By.cssSelector("button[class*='external icon']");
	public static By actionbar = By.cssSelector("span[class='actionbar']");

	public static By btnPracticeArea = By.cssSelector("button[class='icon la-TriangleDown']");
	public static By lnkStateAndLocal = By.cssSelector("a[data-id='1001031']");
	public static By btnAlabamaAddToSearch = By.cssSelector("button[data-srcid='pod1group1']");

	public static By btnIndividualStudentReportTab = By.cssSelector("button[id='col1-pod1-tab1']");
	public static By btnExcerciseReportTab = By.cssSelector("button[id='col1-pod1-tab2']");

	public static By frmIDStatusExcercise = By.cssSelector("form[id='status-report-exercises']");
	public static By cboCitationManuals = By.cssSelector("select[id='exercise-report-select-manuals']");
	public static By cboExcercise = By.cssSelector("select[id='exercise-report-select-exercises']");
	public static By btnGetReportForExcercise = By.cssSelector("input[value='Get Status Report for Exercise']");
	public static By btnActionCunselBenchmarking = By.cssSelector("a[data-action='counselbenchmarkinghome']");
	public static By btnActionProfileSuite = By.cssSelector("a[data-action='profilesuitehome']");
	public static By btnActionVSA = By.cssSelector("a[data-action='vsahome']");
	public static By btnActionMedMal = By.cssSelector("a[data-action='medmalhome']");
	public static By btnActionLPA = By.cssSelector("a[data-action='lpahome']");
	public static By btnGuib = By.cssSelector("a[data-action='guibhome']");
	public static By btnPublicRecords = By.cssSelector("a[data-action='publicrecords']");

	public static By btnActionPublicRecords = By.cssSelector("a[data-action='publicrecords']");
	public static By btnLexisAdvanceTax = By.cssSelector("a[data-action='taxhome']");
	public static By btnLexisAdvance = By.cssSelector("span[title='Lexis Advance®']");

	public static By btnLexisAdvanceResearch = By.cssSelector("a[data-action='researchhome']");

	public static By eleStudentPodList = By.cssSelector("section[class*='pod-wrapper pod-index']");
	public static By eleStudentPodHeader = By.cssSelector("span[class='pod-head-label']");
	public static By eleStudentExerciseList = By.cssSelector("a[class='exercisename action']");

	public static By btnSelectPracticeArea = By.cssSelector("button[id='nav_practiceareasandtopics_btn']");
	
	public static By lnkBankingAndFinance = By.cssSelector("a[data-moduleid='1013003']");
	public static By txtPracticeAreaHeading = By.cssSelector("h2[class='pagewrapper']");

	public static By eltTopicPod = By.cssSelector("div[class='panel-content clr']");
	public static By lnkTopicPod = By.tagName("a");

	public static By cboDPPADropdown = By.cssSelector("select[id='MainContent_dppaList']");
	public static By cboGLBADropdown = By.cssSelector("select[id='MainContent_glbaList']");
	public static By btnPublicRecordConfirm = By.cssSelector("input[id='MainContent_btnConfirm1']");
	public static By rdoEmail = By.cssSelector("input[id='email']");
	public static By btnSendOTP = By.cssSelector("input[id='sendOTPBtn']");
	public static By txtPageHeader = By.cssSelector("header[id='sign-post']");
	public static By txtPageContent = By.cssSelector("section[id='wrapper']");

	public static By eltPublicRecordsForm = By.cssSelector("form[id='form1']");
	public static By tblPublicRecordsForm = By.cssSelector("table[id='permUseSteps']");
	public static By tblRow = By.tagName("tr");
	public static By tblCol = By.tagName("td");

	public static By rdoGoogleCaseLaw = By.cssSelector("input[id='as_sdt2']");
	public static By txtGoogleSearchField = By.cssSelector("input[id='gs_hp_tsi']");
	public static By btnGoogleSearch = By.cssSelector("button[id='gs_hp_tsb']");
	public static By eltGoogleHomeDiv = By.cssSelector("div[id='gs_hp_sdt']");

	public static By divHistoryPod = By.id("historypod");
	public static By divFoldersPod = By.cssSelector("div[class*='globalpod workfolderspod']");
	public static By divNewsPod_Parent = By.cssSelector("div[id='nav_newspod']");
	public static By divNewsPod = By.cssSelector("div[class*='globalpod searchResultsPod-wrapper']");
	public static By divFavoritesPod = By.cssSelector("div[class*='globalpod favoritespod']");
	public static By divAlertsPod = By.cssSelector("div[class*='alertspod'][class*='wrapper']");
	public static By divNotificationPod = By.cssSelector("div[class*='globalpod resize-monitor notification-pod']");
	public static By btnPodExpansion = By.cssSelector("button[class='icon la-TriangleDown']");
	public static By btnPodExpansion1 = By.cssSelector("button[class*='icon']");
	public static By divMoreFilter = By.cssSelector("div[class*='morefilteroption']");
	public static By eltLatestUpdatesPod = By.cssSelector("div[class*='comp highlanderpod']");
	public static By latestReleaseLearnMore = By.cssSelector("a[data-action='primarylinkclick']");
	public static By lnkLinks = By.tagName("a");
	public static By eltSupportPod = By.cssSelector("div[class*='nav_supportpod']");
	public static By eltTitleNewWindow = By.cssSelector("div[id='page-title']");
	public static By eltPageContentNewWindow = By.cssSelector("div[id='generated']");
	public static By txtHeaderNewWindow = By.tagName("h1");

	public static By txtHeaderNewWindow1 = By.cssSelector("h1[class='topictitle1']");
	public static By CurrentProduct = By.cssSelector("a[id*='currentproduct']");
	public static By divNews = By.cssSelector("div[class*='comp resultslistpod']");
	public static By spanDate = By.cssSelector("span[class='date']");

	public static By eltLeftSidePods = By.cssSelector("div[class='leftColumn']");
	public static By eltLeftSidePods1 = By.cssSelector("div[class='float-col left']");
	public static By eltRightSidePods = By.cssSelector("div[class='rightColumn']");
	public static By eltRightSidePods1 = By.cssSelector("div[class='float-col']");
	public static By eltPods = By.cssSelector("div[class*='wrapper']");
	public static By eltPodFooter = By.cssSelector("ul[class*='pod-footer']");
	public static By eltHistoryPodContent = By.cssSelector("ul[class='recentsearches ']");
	public static By eltHistoryPod = By.cssSelector("div[id='historypod']");
	public static By btnExpandCollapse = By.cssSelector("button[class*='icon la-TriangleDown']");
	public static By btnExpandCollapse3 = By.cssSelector("button[class*='content']");
	public static By eltPodIcon = By.cssSelector("span[class*='icon']");
	public static By eltFavoritesPodContent = By.tagName("ul");
	public static By eltFavoritelistitem= By.tagName("li");
	public static By eltFavoritesPod = By.cssSelector("div[class*='comp favoritespod']");
	public static By eltFavoritesList=By.cssSelector("div[class='globalpod-content']");
	public static By btnExpandCollapse1 = By.cssSelector("div[class*='globalpod favoritespod']");
	public static By eltPodHeader = By.tagName("h2");
	public static By btnExpandCollapse2 = By.cssSelector("div[class*='workfolderspod']");
	public static By eltFoldersPod = By.cssSelector("div[class*='workfolder_pod']");
	public static By eltFoldersPodContent = By.cssSelector("ul[class='recentFoldersContent']");
	public static By hdrTitle = By.cssSelector("h1[class='entry-title']");
	public static By divArticle = By.cssSelector("div[id='article']");

	// public static By eltNewsPod
	// =By.cssSelector("div[class='nav_newspod-wrapper expanded']");
	public static By eltNewsPod = By.cssSelector("div[class*='comp resultslistpod']");
	public static By eltNewsExternalIcon = By.cssSelector("button[class='icon la-ExternalLink']");
	public static By eltNewsPodContent = By.cssSelector("div[class='pod-content cite-list']");
	public static By btnActionGUIB = By.cssSelector("a[data-action='guibhome']");

	public static By lnkLaw360 = By.cssSelector("a[href='http://www.law360.com']");
	public static By imgLaw360 = By.cssSelector("img[title='Visit Law360']");

	public static By eltAlertsPod = By.cssSelector("div[class*='alertspod-wrapper']");
	public static By eltPodFooter1 = By.tagName("footer");
	public static By eltAlertsPodContent = By.cssSelector("ul[class='alertspodlist']");

	public static By eltSupportPodContent = By.cssSelector("div[class='pod-content cite-list']");
	public static By eltSupportPodContactInfo = By.cssSelector("ul[class='contact-info']");

	public static By recentFavoritesFilter = By.cssSelector("div[class='recent-favorites-filters']");
	public static By deleteRecentFav = By.cssSelector("button[data-popupid='deleteRecentFavoriteRow']");
	//public static By deletePopUp = By.cssSelector("div[class='popup_floater popup_filter_clear']");
	public static By deletePopUp = By.cssSelector("form[id='dialog-content']");
	public static By deletePopUp1 = By.cssSelector("aside[role='dialog']");
	public static By cancelDeletePopUp = By.cssSelector("button[class='cancel_popup']");
	public static By deleteDeletePopUp = By.cssSelector("button[class='save_popup']");
	
	public static By cancelDeletePopUp1 = By.cssSelector("button[class='secondary']");
	public static By deleteDeletePopUp1 = By.cssSelector("submit[class='primary']");
	
	public static By deleteRecentFavPage = By.cssSelector("div[class='popup_overlay'][id='deleteRecentFavoriteRow']");
	public static By recentFilterValue = By.cssSelector("label[data-popupid='recentFavoriteRow']");

	public static By lnkSelectAllUSFederal = By.cssSelector("a[class='selectAllUS']");
	public static By lnkSelectAllStates = By.cssSelector("a[class='selectAllST']");
	public static By btnClosePreFilterPopup = By.cssSelector("button[class='icon la-CloseRemove close']");
	public static By preFiltersDiv = By.cssSelector("div[class='searchfilters shown']");
	public static By generallink = By.cssSelector("button[id='settings_menu_general']");
	public static By resultsList = By.cssSelector("select[id*='resultsdisplay_numberresults']");
	public static By headerinFilter = By.cssSelector("header[class='hiddenMobi']");
	public static By currentFilterSelected = By.cssSelector("menu[label='Current filter selections']");

	public static By chkRelatedFederal = By.cssSelector("input[id='jur:1:78']");

	public static By lawSchoolSignOut = By.cssSelector("a[id='$signout']");

	public static By allSources = By.cssSelector("a[data-id='allsources']");
	public static By frmTopicAlert = By.cssSelector("form[id='dialog-content']");
	public static By frmTopicAlert1 = By.cssSelector("aside[role='dialog']");
	public static By txtFromDate = By.cssSelector("input[id='daterange.from']");
	public static By txtToDate = By.cssSelector("input[id='daterange.to']");
	public static By txtEmail = By.cssSelector("input[id='currentusersettings.emailaddress']");
	public static By btnBrowse = By.cssSelector("button[id='nav_browse_btn']");
	public static By btnCreateAlert = By.cssSelector("input[type='submit'][value='Create Alert']");
	public static By cancelAlert = By.cssSelector("input[type='button'][value='Cancel']");
	public static By Alertbtn = By.cssSelector("a[id='Alerts']");
	public static By frmResult = By.cssSelector("form[class*='results-list']");
	public static By btnAlertDelete = By.cssSelector("button[id='alertdelete']");
	public static By btnDeleteAlert = By.cssSelector("input[value='Delete Alert']");
	public static By topicAlertTabbed = By.cssSelector("aside[class='dialog alerts_dialog shepards_dialog tabbed']");

	public static By wordWheel = By.cssSelector("aside[id='wordwheel']");

	public static By startIn = By.cssSelector("select[id='content-startin']");

	// LPA Topics Page
	public static By lpTopiccontentdisplay = By.cssSelector("div[class='topicnav-content clr']");
	public static By lpaPracticeParentTopics = By.cssSelector("aside[class='module-level']");
	public static By lpaPracticeChildTopics = By.cssSelector("aside[class='topics-level']");
	public static By lpaPracticeSubTopics = By.cssSelector("aside[class='subtopics-level']");
	public static By lpaPracticeTabNav = By.cssSelector("button[type='button'][id='topicnav']");
	public static By btnPracArea = By.cssSelector("button[id='nav_practiceareas_btn']");

	// Client ID
	public static By setClientSubmitButton = By.cssSelector("input[id='submit_clientid']");
	public static By setClientCancelButton = By.cssSelector("input[id='discard_clientid']");
	public static By errorClientMessageText = By.cssSelector("aside[class*='invalid_clientid']");
	public static By clientName = By.cssSelector("button[class*='menulabel client-button icon']");

	public static By browseChildMenu = By.cssSelector("div[id='exploreMenuWrapper']");
	public static By sourceSearch = By.cssSelector("input[id='topicsearchterm'][data-id*='MenuItem']"); // Modified
																										// sourceMenuItem
																										// to
																										// *MenuItem
																										// By
																										// Veeshma
	public static By sourceSearchBtn = By.cssSelector("button[class='la-Search src-submit submit']");
	public static By sourceDiv = By.cssSelector("form[action='sources']");
	public static By sourceSearchWordWheel = By.cssSelector("ul[id='wordwheelresults']");

	// Browse - Topics
	public static By topicsMenu = By.cssSelector("li[class='selected']");
	public static By topicsSearch = By.cssSelector("input[id='topicsearchterm'][data-id='topicsMenuItem']");
	public static By topicsSearchBtn = By.cssSelector("button[class='la-Search src-submit submit btn secondary']");

	public static By Winbrowse = By.cssSelector("div[id='exploreMenuWrapper']");
	public static By btnBrowseMenu = By.cssSelector("button[data-id*='MenuItem']");
	public static By addFavorite = By.cssSelector("button[data-popupid='addRecentFavoriteRow']");
	public static By WintopicList = By.cssSelector("div[id='exploreMenuMillerContainer']");
	public static By aside = By.tagName("aside");
	public static By btnTagListItems = By.tagName("button");
	public static By ul = By.tagName("nav");
	public static By ullist = By.cssSelector("div[id='exploreMenuMillerMenu']");
	public static By downBtn = By.cssSelector("button[class='icon la-TriangleDown active']");
	public static By getDoc = By.cssSelector("a[class*='getDocuments']");
	public static By liclass = By.tagName("li");
	public static By actionButton = By.cssSelector("button[type='button action']");
	public static By actionButton1 = By.tagName("button");
	public static By actionlist = By.cssSelector("div[class='actionsList']");
	public static By healthcare = By.cssSelector("button[class*='la-TriangleRight downlevel'][data-id='tp23']");
	public static By getdocumentlink = By.cssSelector("a[data-action='search']");
	public static By prodswitchermenu = By.cssSelector("div[id='nav_productswitcher_menu']");
	public static By productSwitcherMenu = By.cssSelector("div[id='nav_productswitcher_menu']");
	public static By titleHeader = By.cssSelector("div[class='HeaderBarContainer']");
	public static By deleteFilter = By.cssSelector("div[class='deleteFilter']");
	public static By closebuttonRFF = By.cssSelector("button[class='icon la-CloseRemove close']");
	public static By getPrintHeader = By.cssSelector("div[class='widget-logo']");
	public static By historyPageHeader = By.tagName("h2");
	public static By item = By.tagName("button");
	public static By eltPodFooter2 = By.cssSelector("div[class*='tabresultlist']");
	public static By header3 = By.tagName("h3");
	public static By welcomeDialog = By.cssSelector("form[id='dialog-content']");
	public static By btnIdRecentSearch = By.cssSelector("button[class='recentsearchbtn']");
	public static By podFooterHistory = By.cssSelector("ul[id='pod-footer']");
	public static By viewAllHistory = By.cssSelector("a[data-action='history']");
	public static By viewDocumentList = By.cssSelector("li[class='recentSearchAndDocs']");
	public static By linkname = By.cssSelector("a[data-action='sourceselection']");
	public static By docbtnHistPod = By.cssSelector("button[class='recentdocbtn'][aria-label='Documents']");
	public static By docListHistPod = By.cssSelector("ul[class='recentdocuments']");
	public static By subHeaderHistPod = By.cssSelector("ul[class*='pod-header']");
	public static By selectNone = By.cssSelector("button[data-action='selectclientid'][value='-None-']");
	public static By eltFavButton = By.cssSelector("button[class*='favorite']");
	public static By favContent = By.cssSelector("button[class*='favorite']");
	public static By eltResearchHomeAnchor = By.cssSelector("a[class*='researchhome']");
	public static By btnIdLPSearch = By.cssSelector("button[id='searchTrigger']");
	public static By counsellink = By.cssSelector("ul[class='snapshot-switcher']");
	public static By btnActionGetandprint = By.cssSelector("a[data-value='GetAndPrint']");
	public static By couselbencprint = By.cssSelector("button[data-action='print']");
	public static By printdropdown = By.cssSelector("button[data-action='printnow'][class*='la-Print'][type='button']");
	public static By txtIdtaxlSearch = By.cssSelector("textarea[id='searchTerms']");
	public static By btnIdtaxSearch = By.cssSelector("button[id='mainSearch']");
	public static By btnActionTaxhome = By.cssSelector("a[data-action='taxhome']");
	public static By btnActionICW = By.cssSelector("a[data-action*='icw']");
	public static By eltPopupBoxParent = By.cssSelector("div[id='addFilterFromPanel']");
	public static By eltPopupBox = By.cssSelector("div[class*='popup_floater'][class*='popup_filter_clear']");
	public static By eltPopupButton = By.cssSelector("button[class*='save_popup']");
	public static By eltPopupButton1 = By.cssSelector("input[value='Continue']");
	public static By divClassrecentandfav = By.cssSelector("div[class='recent-favorites-filters']");
	public static By spantag = By.tagName("span");
	public static By divtag = By.tagName("div");
	public static String lnkSettigns = "Settings";
	public static By messageContainer = By.cssSelector("div[class='laacmessage']");
	public static By messageHeader = By.tagName("h2");
	public static By message = By.tagName("p");
	public static By linkInMessage = By.tagName("a");
	public static By btnActionICWFaculty = By.cssSelector("a[data-action='icwfaculty']");
	public static By icwProfSettings = By.cssSelector("button[id='settings_menu_citationexercises_prof']");
	public static By selectAttempts = By.cssSelector("select[id='settings_citationexercises_prof_attempts']");
	public static By folderBtn = By.cssSelector("a[id='Folder']");	
	public static By inputCont = By.cssSelector("div[id='settings_citationexercises_prof']");
	public static By oldICW = By.cssSelector("a[data-value='InteractiveCitationWorkstation']");	
	public static By lnkNotifications = By.cssSelector("a[id='Notifications']");
	public static By lnkHelp = By.cssSelector("a[id='Help']");	
	public static By lexisAdvanceHeader=By.cssSelector("div[class='headerbanner landing']");	
	public static By topicSourceDiv = By.cssSelector("div[class='itemcontainer']");
	public static By topSourcesLi = By.tagName("li");
	public static By expTopSourceBtn = By.cssSelector("button[class*='TriangleRight']");
	public static By btnMainDiv = By.cssSelector("div[class='itemheader']");
	public static By btnDiv = By.cssSelector("div[class='collapsediv']");
	public static By filterSrc = By.cssSelector("ul[class='items padforbutton']");
	public static By addFltrToSrch = By.cssSelector("button[class*='AddToSearch']");
	public static By replaceFiltersAside = By.cssSelector("aside[class='dialog msgbox confirmreplace']");
	public static By ulList = By.cssSelector("ul[class='actions']");	
	public static By btncontinue = By.cssSelector("input[type='submit']");
	public static By frmFilterAlert = By.cssSelector("form[class='dialog-content']");
	public static By inpt = By.tagName("input");	
	public static By lstrecentdocs = By.cssSelector("li[class='recentSearchAndDocs']");	
	public static By filterTray = By.cssSelector("div[id='filtertray']");
	public static By ulFilter = By.cssSelector("ul[id='filtersList']");
	public static By button = By.tagName("button");
	public static By filterCount = By.cssSelector("span[id='filterCount']");
	public static By taxdiv = By.cssSelector("div[class*='taxsearchbox']"); 	
	public static By generalLabel = By.cssSelector("h2[id='selectedMenuTitle']");  
	public static By alphaRadio = By.cssSelector("input[id*='general_resultssortfilteralpha'][type='radio']");
	public static By numberOfResRadio = By.cssSelector("input[id*='general_resultssortfilternumresults'][type='radio']");
	public static By addPreferedJuriLink = By.cssSelector("a[class='generalJurisListFirstLink']");
	public static By jurispopup = By.cssSelector("aside[class*='addjuris']");	
	public static By dlgWelcome=By.cssSelector("form[class='dialog-content']");
	public static By btnGetStarted=By.cssSelector("input[value='Get Started']");	
	public static By srchFindDoc = By.cssSelector("select[id='SearchAssistantDropDownList']");
	public static By connector = By.cssSelector("select[id='strConnector']");
	public static By addSearch = By.cssSelector("button[class='addSearchAssistant']");
	public static By txtSrch = By.cssSelector("input[id='textNearFirst']");
	public static By txtSrch1 = By.cssSelector("input[id='textNearSecond']");
	public static By drpNoOfWords = By.cssSelector("select[id='nearProximityDropDownList']");
	public static By addSearch1 = By.cssSelector("button[class='addConnectorCnt']");
	public static By txtSrch2 = By.cssSelector("input[id='txtSearchAssistant']");
	public static By sectionSrch = By.cssSelector("section[role='search']");
	public static By preTag = By.tagName("pre");	
	public static By sortby = By.cssSelector("input[id*='resultssortfilteralpha']");
	public static String txtHomeTitleMsg = "firsttime";
	public static By addSearchFilter = By.cssSelector("button[class*='AddToSearch']");
	public static By star = By.cssSelector("button[class*='FavoriteEmpty']");	
	public static By archivelink = By.cssSelector("a[title='Archived Codes Search']");
	public static By archiveSearchbox = By.cssSelector("textarea[id='archivedsearchterm']");
	public static By idsearch = By.cssSelector("input[value='Search']");
	public static By catDiv = By.cssSelector("section[class='categoryContent']");
	public static By archivedate = By.cssSelector("select[id='archivesYear']");
	public static By fromYr = By.cssSelector("input[id='fromYear']");
	public static By toYr = By.cssSelector("input[id='toYear']");
	public static By juriSection = By.cssSelector("section[class='jurisdictionContent']");
	public static By expandIcon = By.cssSelector("button[class='icon la-TriangleRight']");
	public static By startdate = By.cssSelector("input[class='min-val']");
	public static By enddate = By.cssSelector("input[class='max-val']");
	public static By chkIncludeNonJurisdiction = By.cssSelector("input[id='jur:1:77']");
	public static By currProd=By.cssSelector("div[class*='currentproduct']");
	public static By contentSwitcher = By.cssSelector("ul[class='content-switcher']");
	public static By btnMoreFilter = By.cssSelector("button[class*='morefilteroption']");	
	public static By ulMoreFilter = By.cssSelector("ul[class*='morefilterslist']");
	public static By podspanheaders = By.cssSelector("span[class='header-label']");
	public static By podContent = By.cssSelector("ul[class*='pod-body']");
	public static By showMoreButton = By.cssSelector("button[class*='la-ShowMore']");
	public static By pods = By.cssSelector("section[class='tax-pod']");
	public static By podsSection1 = By.cssSelector("section[class='pagewrapper']");
	public static By stateSpecificContent = By.cssSelector("ul[id='pod1-content']");
	public static By multiStateContent = By.cssSelector("ul[id='pod2-content']");
	public static By practiceArea = By.cssSelector("button[data-id='PracticePagesPracticeAreaId']");
	public static By browseMenu = By.cssSelector("div[id='exploreMenuMillerMenu']");
	public static By subMenuPA = By.cssSelector("button[data-id='PracticePagesPracticeAreaId']");
	public static By filterCountNumber = By.cssSelector("strong[id='filterCount']");
	public static By alertBtn = By.cssSelector("a[id='Alerts']");
	public static By notifBtn = By.cssSelector("a[id='Notifications']");
	public static By myLexisBtn = By.cssSelector("a[id='MyLexis']");
	public static By eltcancelbutton = By.cssSelector("button[id='Go Back']");
	public static By eltcontibutton = By.cssSelector("button[id='Continue']");
	public static By wordList = By.cssSelector("li[class='links'");
	public static By wordLink = By.cssSelector("a[data-action='toc']");
	public static By saveFavourite = By.cssSelector("button[id='addtofavorites']");
	public static By dismissBtn = By.cssSelector("button[data-action='dismisstoast']");

	// More dropdown
	public static By moredropdown = By.cssSelector("div[class='overflow morefilteroption']");
	public static By clickmore = By.cssSelector("span[class='showMoreFilters icon la-TriangleDown']");

	public static By menu = By.cssSelector("menu[label='Controls for the filter panel']");

	public static By morefilter  = By.cssSelector("div[class='morefilterslist active']");
	public static By removeFilter = By.cssSelector("div[class='deleteFilter overflow']");

	public static By deletePopupPage = By.cssSelector("aside[class='ladialog deletefavorite small']");
	public static By section = By.tagName("section");
	public static By DeleteButton = By.cssSelector("input[type='submit'][value='Delete']");
		
	public static By replaceFilterPopup = By.cssSelector("aside[class='dialog msgbox undefined']");
	public static By homePagePods = By.cssSelector("div[class*='pod comp']");
	public static By homePagePods1 = By.cssSelector("div[class*='pod-wrapper comp']");
	public static By podHeader = By.tagName("h2");
	public static By rmLink = By.cssSelector("a[data-action='researchmap']");
	public static By moreDropdownContainer = By.cssSelector("div[id='nav_applicationmenu_menu']");
	public static By errormsg = By.cssSelector("aside[id='errorMessageArea']");
	public static By spanDate1 = By.cssSelector("span");
	//folders pod
	public static By pod=By.cssSelector("div[class='pods-wrapper']");
	public static By foldersPod=By.cssSelector("div[class='pod comp workfolder_pod']");
	public static By filterTray1 = By.cssSelector("div[class='filter-applied']");
	public static By ulFilter1 = By.cssSelector("ul[class='filters-used']");
	public static By filter2 = By.cssSelector("button[data-id*='-'][class*='CloseRemove']");
	public static By filter1 = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By btnExpandCollapse4 = By.cssSelector("button[class*='icon la-Triangle']");
	public static By viewAllNews = By.cssSelector("a[title='View all news']");
	public static By newsLink = By.cssSelector("a[class='rowdesc']");
	public static By latestRelease = By.cssSelector("ul[class='pod-controls']");
	public static By divclasshead = By.cssSelector("div[class='headerpod-wrapper']");
	public static By divclass = By.cssSelector("div[class='pod-wrapper landing comp searchbox']");
	public static By ulclass = By.cssSelector("ul[class='advancesearch]");
	public static By advancedsearch = By.cssSelector("button[data-action='guidedsearch']");
	//applied prefilter verification
	public static By parentdiv1 = By.cssSelector("div[class='ct-sticky-zone']");
	public static By div1 = By.cssSelector("div[class='pod-wrapper landing comp searchbox']");
	public static By prefilter = By.cssSelector("button[class='dropdown filters']");
    //clicking home link()
	public static By divclass1 = By.cssSelector("div[class='comp container_signpost']");
	public static By ulclass1 = By.cssSelector("nav[class='pagewrapper]");
	public static By homelink = By.cssSelector("a[data-url='/usresearchhome']");	
	//applied prefilter1 verification
	public static By div = By.cssSelector("div[class='pod-wrapper landing comp searchbox']");
	public static By span = By.cssSelector("span[class='options']");
	public static By prefilter1 = By.cssSelector("button[class='dropdown filters icon la-TriangleDownAfter selected']");
	public static By tabheaders = By.cssSelector("ul[class='tabheaders']");
	public static By selecteddocs = By.cssSelector("ul[class='selecteddocs']");
	
	public static By divfaq=By.cssSelector("div[class='faq']");
	public static By spanFaq = By.cssSelector("span[class='icon la-ExternalLink']");
	public static By btndatasearch = By.cssSelector("button[data-action='guidedsearch']");
	public static By btndatahelpkey = By.cssSelector("button[data-helpkey='connectorswlnequivalents_ref-reference']");
	public static By btnclassicon = By.cssSelector("button[class='icon la-TriangleRight expanded']");
	
	public static By closeDialog = By.cssSelector("button[data-action='cancel']");
	public static By ulBreadCrum=By.cssSelector("ul[class='header-breadcrumb']");
	public static By liBreadcrum=By.tagName("li");
	public static By aLink=By.tagName("a");
	public static By pageHeader = By.cssSelector("h1[class='pagewrapper']");
	public static By btnRecentMatter = By.cssSelector("input[id='irecent_clientids']");
	public static By selRecentMatter = By.cssSelector("select[id='srecent_clientids']");
	public static By btnNewMatter = By.cssSelector("input[id='new_clientid']");
	public static By txtClientid = By.cssSelector("input[id='setClientIdText']");
	public static By btnSetMatter = By.cssSelector("input[id='submit_clientid']");
	public static By invalidClientid = By.cssSelector("aside[class*='invalid_clientid']");
	public static By submitCancel = By.cssSelector("input[id='discard_clientid']");
}
