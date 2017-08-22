package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Document {
	public static By copyCitation = By.cssSelector("button[id='copycitaton']");
	public static By popUpCitation = By.cssSelector("aside[class='dialog citationpopup']");
	public static By popUpTitle = By.cssSelector("h1[class='citationpopuptitle']");
	public static By cboCitationFormat = By.cssSelector("select[id='StylesList']");
	public static By CitationSelect = By.cssSelector("input[id='citationReporterUpdate']");
	public static By btnCitationClose = By.cssSelector("input[id='citationCloseButton']");
	public static By btnActivatepassages = By.cssSelector("button[id='activatepassages']");
	public static By txtClipboardText = By.cssSelector("div[id='ClipboardText']");
	public static By lnkRFPassage = By.cssSelector("span[data-func='LN.Advance.ContentView.getCitationMap']");
	public static By btnDownload = By.cssSelector("button[data-action='deliverymenu']");
	public static By btnDownloadinDialog = By.cssSelector("input[data-action='download']");
	public static By btnDownloadNow = By.cssSelector("button[data-action='downloadnow']");
	public static By DownloadChooseSet = By.cssSelector("button[data-action='downloadopt']");
	public static By lnkDownloadedDoc = By.cssSelector("a[href*='download'][href*='delivery']");
	public static By btnDownloadClose = By.cssSelector("input[value='Close']");
	public static By btnAddFolder = By.cssSelector("button[data-action='folder']");
	public static By btnChooseFolder = By.cssSelector("button[id='showaddtofolder']");
	public static By btnCreateFolder = By.cssSelector("input[id='createfolder']");
	public static By txtFolderName = By.cssSelector("input[id='newfolderinput']");
	public static By btnCreateNewFolder = By.cssSelector("input[id='addnewfolder']");
	public static By tabSelectedDocuments = By.cssSelector("button[data-value='Selected Documents']");
	public static By tabShareWithOthers = By.cssSelector("button[data-value='Share with Others']");
	public static By tabSaveOptions = By.cssSelector("button[data-value='Save Options']");
	public static By txtShareContacts = By.cssSelector("input[id='sharedname']");
	public static By btnAddToContact = By.cssSelector("input[id='addtocontact']");
	public static By btnDocumentSave = By.cssSelector("input[value='Save']");
	public static By cboSharingList = By.cssSelector("ul[id='SharingDropDownDiv']");
	public static By lnkViewReport = By.cssSelector("a[data-pdlinktext*='View reports']");
	public static By pgTitleTopicSummary = By.cssSelector("span[class*='query']");
	public static By lnkAssistanceOFCounsel = By.linkText("Assistance of Counsel");
	public static By txtDocumentHeading = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By elt2of4 = By.linkText("Part 2 of 4");
	public static By eltAboutThisDoc = By.cssSelector("section[id='AboutThisDocument']");
	public static By txtStudentDocumentHeading = By.cssSelector("h2[class='pagewrapper']");
	//public static By txtStudentDocumentHeading = By.cssSelector("div[class='pagewrapper']");
	public static By DialogChooseSett = By.cssSelector("aside[class='dialog delivery']");
	public static By DialogChooseSettings = By.cssSelector("aside[class*='dialog delivery']");
	public static By tabDLDialogBasicOpt = By.cssSelector("button[class='tabButton BasicOptions']");
	public static By divDLDialogRows = By.cssSelector("div[class='row']");
	public static By chkZIPFile = By.cssSelector("input[id='ZipFile']");
	public static By DocTitleContent = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By ShepardsSec = By.cssSelector("section[id='Shepards']");
	public static By lnkShepardizeDoc = By.cssSelector("a[data-action='sheppreview']");
	public static By lnkShepardizeHeadNote16 = By.cssSelector("a[data-headnote='HN16']");
	public static By divComments = By.cssSelector("div[data-id='HideShow_comments']");
	public static By btnHideComments = By.cssSelector("button[id='HideShow_comments']");
	public static By eltDocContent = By.cssSelector("section[class='doc-content SS_contentdocument']");
	public static By tableOfContentsTab = By.cssSelector("button[class*='minitoc-tab']");
	public static By inlineResultsTab = By.cssSelector("button[data-id='results']");
	public static By expandinlineResults = By.cssSelector("button[class='results-tab selected']");
	public static By collapseinlineResults = By.cssSelector("button[class='results-tab']");
	public static By miniToc = By.cssSelector("aside[class*='minitoc leftpane']");
//	public static By miniTabOfCon = By.cssSelector("button[data-id='minitoc']");
	public static By tocLink = By.cssSelector("a[data-action='linktoc']");
	public static By tocLink1 = By.cssSelector("a[data-action='linktoc']");
	public static By noResults = By.cssSelector("div[class='noresults']");
	public static By scrollbar = By.cssSelector("div[class='scrollbar']");
	public static By copyrightContainer = By.cssSelector("div[class='SS_Copyright']");
	public static By copyrightImage = By.tagName("img");
	// Full Document SST
	public static By selectedtextmenu = By.cssSelector("aside[class='doccontextmenu selectedtextmenu']");
	public static By addtosearch = By.cssSelector("button[class='icon la-AddToSearch']");
	public static By selectedtextarea = By.cssSelector("textarea[class='selectedtext'][id='txtSeletedText']");
	public static By submitsearch = By.cssSelector("input[data-action='confirm'][value='Search']");
	public static By cancelsearch = By.cssSelector("input[data-action='cancel'][value='Cancel']");
	public static By activecategory = By.cssSelector("li[class='active ']");
	public static By usedfilter = By.cssSelector("ul[class='filters-used']");
	public static By searchfilter = By.cssSelector("ul[class='filters']");
	public static By copycitation = By.cssSelector("button[class='icon la-Copy']");
	public static By rightContainer = By.cssSelector("div[class*='documentRightSide']");
	public static By fieldset1 = By.cssSelector("div[class*='documentRightSide']");
	public static By jumpto = By.cssSelector("button[data-action='jumptotrigger']");
	public static By sectionContainer = By.cssSelector("div[class='dropdownContainer sectionJumpTo']");
	public static By sections = By.cssSelector("button[class*='icon la-TriangleDownAfter']");
	public static By sectionsDropdown = By.cssSelector("aside[class='supplemental scrollable']");
	public static By lstTagUList = By.tagName("ul");
	public static By childCustody = By.cssSelector("a[class='SS_SummaryLinks'][data-displayname='Child Custody Procedures']");
	public static By hn1 = By.cssSelector("a[data-id='LNHNRef1']");
	public static By headnotes = By.cssSelector("div[id='LexisNexisHeadnotes']");
	public static By navigateContainer = By.cssSelector("div[class='dropdownContainer navigateJumpTo']");
	public static By nextTerm = By.cssSelector("button[data-action*='nextterm']");
	public static By prevTerm = By.cssSelector("button[data-action*='previousterm']");
	public static By fundRights = By.cssSelector("a[class='SS_SummaryLinks'][data-displayname='Fundamental Rights']");
	public static By docHeader = By.cssSelector("header[class='SS_DocumentHeader']");
	public static By jumpToContainer = By.cssSelector("aside[class='supplemental scrollable']");
	public static By closeButton = By.cssSelector("input[id='entitylinkcancel']");
	public static By editorialContent = By.cssSelector("span[class='SS_EditorialContent']");
	public static By showhideLink = By.cssSelector("a[class='SS_HideShowLink']");
	public static By charlesDSusano = By.cssSelector("a[class='SS_EntityLink'][data-searchtext='CHARLES D. SUSANO, JR.']");
	public static By inlinePopup = By.cssSelector("div[id='popup'][class='inline-popup']");
	public static By mobileToolbar = By.cssSelector("div[class='toolbar noreader']");
	public static By mobileToolbar1 = By.cssSelector("div[class='toolbar']");
	public static By tagButton = By.tagName("button");
	public static By linkAboveImage = By.cssSelector("a[id='para_123']");
	// public static By docContent =
	// By.cssSelector("span[class='SS_LeftAlign']");
	public static By powdertermhighlighttext = By.cssSelector("span[name='SH_514672486']");
	public static By percenttermhighlighttext = By.cssSelector("span[class*='SH_551501916 Pnote SS_SH']");
	public static By referencetermhighlighttext = By.cssSelector("span[name='SH_3653863455'][class='Pnote SS_SH textHilg']");
	public static By header = By.tagName("h1");
	public static By tips = By.cssSelector("a[id='tipslink']");
	public static By aside = By.tagName("aside");
	public static By text = By.tagName("p");
	public static By viewusing = By.cssSelector("input[title*='View using']");
	public static By docExtra = By.cssSelector("aside[class='doc-extras noreader']");
	public static By docExtraSections = By.tagName("section");
	public static By sectionHeader = By.tagName("h3");
	public static By links = By.tagName("a");
	public static By actionsSection = By.cssSelector("section[id='Actions']");
	public static By topicIndex = By.cssSelector("a[data-action='linktopics']");
	public static By allTopicDocuments = By.cssSelector("a[data-action='linksearch']");
	public static By selectedItem = By.cssSelector("li[class='selected']");
	public static By firstButton = By.cssSelector("button[class*='first icon la-FirstColumn']");
	public static By prevButton = By.cssSelector("button[class*='previous icon la-TriangleLeft']");
	public static By nextButton = By.cssSelector("button[class*='next icon la-TriangleRight']");
	public static By lastButton = By.cssSelector("button[class*='last icon la-LastColumn']");
	public static By divIdBrowserNav = By.cssSelector("div[id='exploreMenuMillerNavigation']");
	public static By docSliderLeft = By.cssSelector("button[class='icon la-TriangleLeft'][data-action='linkdocslider']");
	public static By docSliderRight = By.cssSelector("button[class='icon la-TriangleRightAfter'][data-action='linkdocslider']");
	public static By pagenumber = By.cssSelector("span[id='PAGE_2847_8326']");
	// VSA Document Page
	public static By jumptobutton = By.cssSelector("div[class*='documentRightSide']");
	public static By navigateto = By.cssSelector("div[class='dropdownContainer navigateJumpTo']");
	public static By aboutthisdoclink = By.cssSelector("a[data-action='linkrcm']");
	public static By fieldset = By.cssSelector("div[class*='fieldset']");
	public static By citationlink = By.cssSelector("a[class='SS_Rptr']");
	public static By shepardizeheadnote = By.cssSelector("a[class*='NarrowByHN']");
	public static By footnotelink = By.cssSelector("a[class*='EmbeddedLink']");
	public static By footnote2 = By.cssSelector("a[data-id='fntext_fn_fnote2']");
	public static By tipsinatd = By.cssSelector("a[class*='legalissuetrail-tips-link']");
	public static By createdFoldersContainer = By.cssSelector("li[id='MyFoldersId']");
	public static By listItems = By.tagName("li");
	public static By extralinks = By.cssSelector("aside[class='doc-extras noreader']");
	public static By sectionNames = By.cssSelector("section[id='Actions']");
	public static By briefcaseIcon = By.cssSelector("a[data-docfullpath='/shared/document/analytical-materials/urn:contentItem:836D-H6N1-6439-B0X7-00000-00']");
	public static By photoIdentificationLink = By.cssSelector("a[class='SS_SummaryLinks'][data-docfullpath='/shared/document/analytical-materials/urn:contentItem:836D-H6N1-6439-B0X7-00000-00']");
	public static By viewTopicSummaryLink = By.cssSelector("a[class='icon la-LegalTopicSummary']");
	public static By fileName = By.cssSelector("input[id='FileName']");
	public static By formatDropdown = By.cssSelector("select[id='StylesList']");
	public static By selectButton = By.cssSelector("input[id='citationReporterUpdate']");
	public static By copyCitationPopup = By.cssSelector("aside[class='dialog citationpopup']");
	public static By oopmsgindoc = By.tagName("div");
	public static By wholebox = By.cssSelector("section[class='pagewrapper']");
	public static By citationInclude = By.cssSelector("div[id='citationInclude']");
	public static By citationOptions = By.cssSelector("div[id='citationOptions']");
	public static By checkBox = By.cssSelector("input[type='checkbox']");
	public static By clipText = By.cssSelector("div[id='ClipboardText']");
	public static By italics = By.tagName("i");
	public static By closeCitationPopup = By.cssSelector("input[id='citationCloseButton']");
	public static By clipTextUnderlined = By.cssSelector("u");
	public static By headNotesContent = By.cssSelector("div[id='LexisNexisHeadnotes']");
	public static By expandButton = By.cssSelector("a[class='SS_HideShowLink']");
	public static By noneOption = By.cssSelector("option[value='None']");
	public static By docSlider = By.cssSelector("span[class='docslider']");
	public static By embeddedLink = By.cssSelector("a[class='SS_EntityLink']");
	public static By embeddedSearchLink = By.cssSelector("a[id='entityLinkingSearchLinkClick']");
	public static By span = By.tagName("span");
	public static By footnote = By.cssSelector("a[class='SS_FootnoteReference']");
	public static By FilterButton = By.cssSelector("button[class*='Search src-submit']");
	public static By docParts = By.cssSelector("a[data-action='linkdoc']");
	public static By toc = By.cssSelector("a[data-action='toc']");
	public static By traiangleicon = By.cssSelector("button[class*='la-TriangleRight trigger'][data-nodeid='AAB']");
	public static By deliveryStatus = By.cssSelector("div[class='comp delivery_status']");
	public static By lnkDeliveryDownload = By.cssSelector("a[href*='/r/delivery/content'][href*='TOC']");
	public static By lnkDeliveryDownloadContent = By.cssSelector("button[data-action*='download']");
	public static By txtDownloadStatus = By.tagName("h1");
	public static By highlightedSearchTerm = By.cssSelector("span[class*='Pnote']");
	public static By docContent = By.cssSelector("section[class*='doc-content SS_contentdocument']");// Modified
																										// by
																										// Pratik
	public static By collpasedList = By.cssSelector("li[class='collapsed']");
	public static By tocSelectedNode = By.cssSelector("span[class='selectednode']");
	public static By notesIframe = By.cssSelector("iframe[id='notes_ifr']");
	public static By notesArea = By.cssSelector("body[id='tinymce']");
	public static By toolbar = By.cssSelector("div[role='toolbar']");
	public static By overflowtoolbar = By.cssSelector("nav[class='docnav desktopNeverOverflow']");
	public static By btncombine = By.cssSelector("div[class='buttoncombine']");
	public static By spanclass = By.cssSelector("span[class='docslider']");
//	public static By resultlink = By.cssSelector("a[data-action='linkpssearch']");
	public static By resultlink = By.cssSelector("a[data-action='linksearch']");
	public static By asideclass = By.cssSelector("aside[class='search-controls']");
	public static By narrowbyfilter = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By formview = By.cssSelector("form[novalidate='novalidate']");
	public static By searchlink = By.cssSelector("data-action='linksearch'");
	public static By viewPDFAttachment = By.cssSelector("a[class='SS_Attachment']");
	public static By btnEmail = By.cssSelector("button[data-action='email']");
	public static By lnkPrint = By.cssSelector("button[data-action*='print']");
	public static By docslider = By.cssSelector("span[class='docslider']");
	public static By resultlist = By.cssSelector("a[data-action='linksearch']");
	public static By resultlistlatnews = By.cssSelector("a[data-action='linktaxnews']");
	public static By lnksdrresultslist = By.cssSelector("a[data-action='linksdrsearch']");
	public static By nextdoc = By.cssSelector("button[data-action='linkdocslider']");
	public static By previous = By.cssSelector("button[class='icon la-TriangleLeft']");
	public static By next = By.cssSelector("button[class='icon la-TriangleRightAfter']");
	public static By jumpToParts = By.cssSelector("h2[id*='JUMPTO']");
	public static By jumpToParts1 = By.cssSelector("span[id*='JUMPTO']");
	public static By ATD = By.cssSelector("section[id='AboutThisDocument']");
	public static By eleClickNTAnchor = By.cssSelector("a[data-action='shepards']");
	public static By highlightedRFCSection = By.cssSelector("span[class='SS_RFCPassage icon la-LegalIssueTrail']");
	// public static By footer = By.cssSelector("footer[id='pageFooter']");
	public static By footer = By.cssSelector("ul[class='footer-nav']");
	public static By embed = By.cssSelector("a[class='SS_EmbeddedLink']");
	public static By topicTrailContent = By.cssSelector("span[class='SS_TopicTrail']");
	public static By fedIncomeTax = By.cssSelector("a[data-displayname='Federal Income Tax Computation']");
	public static By docContMenu = By.cssSelector("aside[class='doccontextmenu']");
	public static By footNotesContainer = By.cssSelector("footer[class='SS_Footnote']");
	public static By pdfData = By.id("viewer");
	public static By eltEmbeddedLinks = By.cssSelector("a[class*='SS_EmbeddedLink']");
	public static By eltEmbeddedLinks1 = By.cssSelector("a[class*='SS_EntityLink']");
	public static By tocNav = By.cssSelector("div[class='tocnavlinks']");
	public static By containerPopUp = By.cssSelector("div[id='popup']");
	public static By actionLinkUnderPopUp = By.cssSelector("a[id='entityLinkingSearchLinkClick']");
	public static By docText = By.cssSelector("span[class='SS_LeftAlign']");
	public static By pdfLink = By.cssSelector("a[data-action='attachment']");
	public static By txtReport = By.cssSelector("span[id='JUMPTO_Reporter']");
	public static By otherjurislinks = By.cssSelector("li[class='morefew']");
	public static By jurissection = By.cssSelector("section[id='OtherJurisdictions']");
	public static By jurisViewMore = By.cssSelector("a[class*='viewmore']");
	public static By headerCheck = By.cssSelector("span[class='SS_bf']");
	public static By atdSection = By.cssSelector("section[id='AboutThisDocument']");
	public static By activeAtd = By.cssSelector("h3[class='icon la-TriangleDownAfter active']");
	public static By atdDropDown = By.cssSelector("h3[class='icon la-TriangleDownAfter']");
	public static By ulIdSelecteduserListTableAddtoFolder = By.cssSelector("ul[id='SelecteduserListTableAddtoFolder']");
	public static By tabShareWithOthers1 = By.cssSelector("button[data-value='Share With Others']");
	public static By lnkgetSketch = By.cssSelector("a[class='SS_Attachment']");
	public static By jumpToPreviousArrow = By.cssSelector("button[class*='icon la-ShowPane dontClose']");
	public static By jumpToNextArrow = By.cssSelector("button[class*='icon la-HidePane dontClose']");
	public static By jumpToExpanded = By.cssSelector("button[class='trigger icon la-TriangleDownAfter jqSelJumpto expanded']");

	public static By subHeader = By.cssSelector("h2[class='SS_Banner']");

	public static By docContextMenu = By.cssSelector("aside[class='doccontextmenu selectedtextmenu']");
	public static By addToFolderInMenu = By.cssSelector("button[data-id='addtofolder']");
	public static By docTitInFoldPopup = By.cssSelector("input[id='searchtitle']");
	public static By addToFolderDialog = By.cssSelector("aside[class*='workfolderdialog']");
	public static By folderIcon = By.cssSelector("button[data-action='folderinfo']");
	public static By collapsedMiniToc = By.cssSelector("button[class='minitoc-tab']");
	public static By expandedMiniToc = By.cssSelector("button[class='minitoc-tab selected']");
	public static By docNavClass = By.cssSelector("div[class='tocnavlinks']");
	public static By docNav = By.cssSelector("a[class*='icon la-Triangle']");
	public static By printerFriendlyView = By.cssSelector("button[data-action='printablepage']");
	public static By downloadIcon = By.cssSelector("button[data-action='download']");

	public static By spanc = By.cssSelector("span[class='SS_it']");
	public static By ExtLink = By.cssSelector("a[data-action='ExternalLink']");
	public static By entityLinks = By.cssSelector("a[class*='SS_EntityLink']");
	public static By pdfload1 = By.id("div[class='canvasWrapper']");
	public static By pdfload = By.id("div[class='textLayer']");

	public static By docSection = By.cssSelector("section[id='document']");
	public static By docSpan = By.cssSelector("span[class='SS_LeftAlign']");
	public static By docLinks = By.cssSelector("a[class*='SS_Editable']");
	public static By replaceFiltersAside = By.cssSelector("aside[class='dialog editthisfield-dialog']");
	public static By txtIdField = By.cssSelector("input[id='editfield']");
	public static By mainDiv = By.cssSelector("div[class='content']");
	public static By subDiv = By.cssSelector("div[class='editfieldblock']");

	public static By lnLogo = By.cssSelector("img[alt='LexisNexis Logo']");
	public static By alert = By.cssSelector("button[data-alerttype='Topic']");
	public static By relatedreport = By.cssSelector("section[id='RelatedReports']");
	public static By editdoc = By.cssSelector("button[id='editdocument']");

	public static By pagewrapperpanel = By.cssSelector("div[class='wrapper pagewrapper panel']");
	public static By pagewrapperpanel1 = By.cssSelector("div[class*='pagewrapper']");
	public static By h1 = By.cssSelector("h1[id='SS_DocumentTitle']");
	public static By selectedStyle1 = By.cssSelector("div[class='currentFormatStyle']");
	public static By pagewrapperpanelh2 = By.cssSelector("h2[class*='pagewrapper']");
	public static By footerNew = By.cssSelector("ul[class='footer-nav']");

	public static By btnIdDeliverTab = By.cssSelector("button[data-switch='2']");
	public static By rdoIdOnlineOnly = By.cssSelector("input[id='deliverytype_Online']");

	public static By frmClassResult = By.cssSelector("form[class*='results-list']");
	public static By frmClassResult1 = By.cssSelector("div[class*='results-list']");

	public static By lnkDocTitle = By.cssSelector("a[data-action='midlinetitle']");
	public static By TitleClassDoc = By.cssSelector("h2[class*='doc-title']");
	public static By divDocCondent = By.cssSelector("div[class='row-content']");
	public static By ulCondentSwitcher = By.cssSelector("ul[class*='content-switcher']");
	public static By lnkTableOfCondents = By.cssSelector("a[class*='TableOfContents']");
	public static By TitleClassTOC = By.cssSelector("h2[class='pagewrapper']");
	public static By SearchResultHeader3 = By.cssSelector("header[class*='document-data']");

	public static By pgClassHeaderOption3 = By.cssSelector("header[class*='header']");
	public static By SearchResultHeader = By.cssSelector("header[class='resultsHeader']");
	public static By SearchResultHeader1 = By.cssSelector("header[class*='results-data']");
	public static By pgClassHeaderOption = By.cssSelector("header[class='signpost']");
	public static By pgClassHeaderOption1 = By.cssSelector("div[class*='headerbanner']");
	public static By pgClassHeaderOption2 = By.cssSelector("section[class*='signpost']");
	public static By secIdMainTopics = By.cssSelector("section[id='results']");
	public static By lstTagName = By.tagName("li");
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

	public static By txtAppTitleBlack = By.cssSelector("span[title='Lexis Advance�']");
	public static By legalNews = By.cssSelector("span[class='query']");
	public static By txtAccessChargeRed = By.cssSelector("header[role='banner']");
	public static By btnAgreeAndContinue = By.cssSelector("input[id='submit_laac']");
	public static By eltCollapsedFilterHeader = By.cssSelector("button[class*='TriangleRight collapsed']");
	public static By filterHeader = By.cssSelector("button[class*='icon trigger la-TriangleRight']");
	public static By eltFilterList = By.tagName("label");

	public static By eltResultList = By.cssSelector("li[data-id*='sr']");
	public static By btnNextPage = By.cssSelector("a[data-action='nextpage']");
	public static By docLink = By.cssSelector("a[data-action='title']");

	public static By btnGetItNow = By.cssSelector("button[data-action='purchase']");

	public static By eltSnapshotList = By.cssSelector("a[data-action='snapshottitle']");
	public static By eltStatutes = By.cssSelector("button[data-id='urn:ict:1140']");

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
	public static By SelMultiPopUp = By.cssSelector("aside[class='dialog selmulti']");

	// others
	public static By hdrResult = By.cssSelector("header[class*='banner results']");
	public static By FilterTitle = By.cssSelector("span[class='filtertitle']");
	public static By chkDocNo = By.cssSelector("input[id='documentNumber']");

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
	public static By txtToAddress = By.cssSelector("input[id='EmailAddress']");
	public static By btnSubmitEmail = By.cssSelector("input[class*='primary']");
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
	public static By imgRed = By.cssSelector("img[src*='Warning']");
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
	// public static By txtIdSearch = By.cssSelector("input[id='searchTerms']");
	public static By txtIdSearch = By.cssSelector("textarea[id='searchTerms']");
	public static By lnkArticle = By.cssSelector("a[href*='articles_search']");

	public static By lnkTxtFeedBack = By.cssSelector("a[id='Feedback']");

	public static By eltExpandedFilterHeader = By.cssSelector("button[class='icon trigger la-TriangleRight expanded']");
	public static By ulFiltersUsed = By.cssSelector("ul[class='filters-used']");
	public static By btnRemoveFilter = By.cssSelector("button[class='icon la-CloseRemove']");
	public static By lstTagListItems = By.tagName("li");
	public static By filtersCourt = By.cssSelector("ul[class='supplemental filters court']");
	public static By filtersJurisdiction = By.cssSelector("ul[data-id='jurisdiction']");
	public static By filtersFederal = By.cssSelector("ul[data-id='pf0']");
	public static By tagBtn = By.tagName("button");
	public static By productLogo = By.cssSelector("a[class='linklabel'][data-action='researchhome']");
	public static By lpaProductLogo = By.cssSelector("a[class='lpahome'][data-action='lpahome']");

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
	public static By resultwrapper = By.cssSelector("div[class='wrapper']");

	public static By pagination = By.cssSelector("nav[class='pagination']");
	public static By pageNumbers = By.cssSelector("a[class='action']");
	public static By printFriendlyView = By.cssSelector("button[data-action='printablepage']");
	public static By searchWithinResults = By.cssSelector("div[id='search-within']");
	public static By searchWithinSearchBox = By.cssSelector("textarea[class='search expandingTextarea']");
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
	public static By nextPage = By.cssSelector("a[data-selectedvalue='nextpage']");
	public static By sortByContainer = By.cssSelector("div[class='dropdownContainer']");
	public static By sortByButton = By.cssSelector("button[class*='current trigger']");
	public static By sortByMenu = By.cssSelector("aside[class='supplemental sortdropdown']");
	public static By docContent1 = By.cssSelector("div[class='result-data']");

	public static By addalertreg = By.cssSelector("button[data-alerttype='Regulatory']");
	public static By chkbox = By.cssSelector("input[type='checkbox']");
	public static By alertPending = By.cssSelector("button[data-alerttype='PendingLegislative']");
	public static By addalertleg = By.cssSelector("button[data-alerttype='Legislative']");
	public static By addalertTopic = By.cssSelector("button[data-alerttype='Topic']");

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

	public static By folderName = By.cssSelector("a[class*='la-Folder']");
	public static By titleWf = By.cssSelector("a[data-action*='link']");
	public static By update = By.cssSelector("span[id='updatesignaldate']");
	public static By divWow = By.cssSelector("div[id='wow']");

	public static By errormessage = By.cssSelector("div[class='errorHeading']");
	public static By textsectiondblclick = By.cssSelector("span[id='PAGE_2848_8326']");

	public static By showMoreButton = By.cssSelector("button[data-action='moretypes']");
	public static By casesButton = By.cssSelector("button[data-id='urn:hlct:5']");
	public static By searchControl = By.cssSelector("aside[class='search-controls']");
	public static By casesHeader = By.tagName("li");

	public static By divWrapperPanel = By.cssSelector("div[class='wrapper pagewrapper panel ']");

	public static By fileNameField = By.cssSelector("input[id='FileName']");

	public static By actualLinkSection = By.cssSelector("section[class='doc-content SS_contentdocument small']");
	public static By actualLink = By.tagName("a");

	public static By searchTermLink = By.cssSelector("a[data-pdsearchterms='32CFR']");

	public static By searchTypeFilter = By.cssSelector("ul[data-id='type']");

	public static By supplementalFiltersList = By.cssSelector("ul[class*='default']");
	public static By TitleClassDocFolder = By.cssSelector("h2[class*='doc-title']");

	public static By lnkUseDefaultSettPrint = By.cssSelector("button[data-action='printnow']");

	public static By documentcount = By.cssSelector("span[class='count']");

	public static By activetab = By.cssSelector("li[class='Active']");

	public static By resultListDiv = By.cssSelector("div[id='researchMapGraph']");
	public static By displayItem = By.cssSelector(".displayItem");

	public static By lnkDownload = By.cssSelector("button[data-action='download']");
	public static By lnkUseDefaultSett = By.cssSelector("button[data-action='downloadnow']");

	public static By historyListContainer = By.cssSelector("div[class='wrapper']");
	public static By historyPopup = By.cssSelector("form[class='dialog-content']");
	public static By historyPopupClose = By.cssSelector("input[value='Close']");

	public static By ulclass = By.cssSelector("ul[class='SS_NoStyleList']");
	public static By sectiong = By.cssSelector("a[id='_j_10_g'][class='textHilg']");

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

	public static By lnkresult = By.cssSelector("a[data-action='linksearch']");
	public static By pdflink = By.cssSelector("a[data-action='pdflink']");
	public static By emailFormat = By.cssSelector("label[id='Format'][data-value='Pdf']");
	public static By stuReport = By.cssSelector("table[id='studentReportTable']");
	public static By selectAllItemsUl = By.cssSelector("ul[class='menu overflowTo']");
	public static By selectAllItemsCheckBox = By.cssSelector("input[data-action='selectall']");
	public static By selectAllItemsCount = By.cssSelector("li[class='count']");
	public static By popupDiv = By.cssSelector("div[id='popup']");
	public static By popup = By.cssSelector("aside[class='dialog citationpopup']");
	public static By popupOk = By.cssSelector("input[id='warningCloseButton']");
	public static By documentText = By.cssSelector("span[class='SS_LeftAlign']");
	public static By highlightText = By.cssSelector("span[class='SS_Pag_Show textHilg']");
	public static By citationHeader = By.cssSelector("header[class='divided citref']");
	public static By viewReferences = By.cssSelector("a[data-action='viewcitations']");
	public static By postFilters = By.cssSelector("ul[class='supplemental']");
	public static By getItNow = By.cssSelector("button[data-action='purchase']");
	public static By overview = By.cssSelector("p[class='overview min']");
	public static By eltCollapsedFilterHeaderOne = By.cssSelector("button[class='icon trigger la-TriangleRight']");

	public static By headerh1 = By.cssSelector("h1[class='contenttypeTitle']");

	public static By permaLinkDiv = By.cssSelector("div[class='comp permalinkgatekeeper']");
	public static By tabList = By.cssSelector("ul[class='wftabheaders']");
	public static By leftPanelCont = By.cssSelector("div[class='sotLeftPanel']");
	public static By radio = By.cssSelector("input[type='radio']");
	public static By btnSelectMultipleCB = By.cssSelector("button[data-event='selmulti']");

	public static By h2tag = By.tagName("h2");
	public static By timelineCont = By.cssSelector("div[class='supplemental timeline']");
	public static By sliderBase = By.cssSelector("div[class='noUi-base']");
	public static By lower = By.cssSelector("div[class='noUi-handle noUi-handle-lower']");
	public static By upper = By.cssSelector("div[class='noUi-handle noUi-handle-upper']");
	public static By labelsCont = By.cssSelector("div[class='labels']");
	public static By btnClientID = By.cssSelector("button[class='menulabel client-button icon']");

	public static By amtAwardDiv = By.cssSelector("div[data-id='amountawarded']");
	public static By okButton = By.cssSelector("input[value='OK']");
	public static By amountLowInput = By.cssSelector("input[id='amountawardedlow']");
	public static By amountHighInput = By.cssSelector("input[id='amountawardedhigh']");
	public static By webLink = By.cssSelector("a[class='icon la-ExternalLinkAfter']");


	public static By menu1 = By.cssSelector("ul[class='menu overflowTo']");
	public static By button1 = By.cssSelector("button[class='dropdown-item action']");

	public static By connectorMsg = By.cssSelector("dd[class='connectorFunction']");

	public static By CancelSelMultiPopUp = By.cssSelector("input[data-action='cancel']");

	public static By hdrDoc = By.cssSelector("header[class*='banner']");
	public static By startYear = By.cssSelector("input[class='min-val'][type='text']");
	public static By endYear = By.cssSelector("input[class='max-val'][type='text']");
	public static By timeLineOkButton = By.cssSelector("button[class='save btn secondary'][type='button']");
	public static By preferredFilters = By.cssSelector("li[class='preferred']");
	public static By h3 = By.tagName("h3");
	public static By edit = By.cssSelector("a[data-action='editpreferred']");
	public static By lnkShepardizeThisDoc = By.cssSelector("a[data-pdlinktype='ShepardsReport']");
	public static By tagSpan = By.tagName("span");
	public static By notificationError = By.cssSelector("div[class='sheperror']");
	public static By jumptoCollapse = By.cssSelector("button[class*='la-TriangleDownAfter']");
	
	public static By highlightText1=By.cssSelector("span[class='SS_RFCPassage_Deactivated']");
//	public static By highlightText2=By.cssSelector("span[class='SS_FootnoteBody']");
	public static By docInfo=By.cssSelector("p[class='SS_DocumentInfo']");
	public static By floatAreaSticky=By.cssSelector("header[class*='signpost sticky']");
	public static By courtDateInfo=By.cssSelector("span[class='documentinfo']");
	public static By highlightText3=By.cssSelector("footer[class='SS_Footnote']");
	public static By floatArea=By.cssSelector("header[class='banner document-data signpost']");
	public static By sketch = By.cssSelector("a[data-attachmenttype='IMAGE']");
	public static By currentStyleFormat = By.cssSelector("span[id='CurrentStyleFormat']");
	public static By previewTextStyle = By.cssSelector("a[class='previewtextStyle']");
	public static By ssList = By.cssSelector("ul[class='SS_ListNoIndent']");
	public static By showMore = By.cssSelector("a[class='SS_ShowMore']");
	public static By showLess = By.cssSelector("a[data-texttohide='View fewer']");
	public static By ssExpandedList = By.cssSelector("ul[class='SS_ListNoIndent dummyClass']");
	
	public static By divClause = By.cssSelector("div[class='SS_Clause SS_OriginalClause']");
	public static By p = By.tagName("p");
	public static By draftingNote = By.cssSelector("img[alt='Drafting notes']");
	public static By draftingNotePopup = By.cssSelector("aside[class*='draftingnotes-dialog']");
	public static By greenSentence=By.cssSelector("span[class='SS_ChangeAdd']");
	public static By redSentence=By.cssSelector("span[class='SS_ChangeDelete']");
	
	public static By subnodeSection=By.cssSelector("ul[class='subnodes']");
	
	//Medmal Document
	public static By addtoFolderDocument = By.cssSelector("li[class='folder']");
	public static By addtoFolderDocument1 = By.cssSelector("button[class='trigger action collapsed']");
	public static By button3 = By.cssSelector("button[class*='trigger collapsed icon la-TriangleDownAfter']");
	public static By popupAside = By.cssSelector("aside[class*='ladialog searchselectedtext medium']");
	public static By selectedFilters = By.cssSelector("div[class*='selectedfilters']");
	public static By subsectionlink = By.cssSelector("li[class='expanded']");
	public static By listheader = By.cssSelector("div[class='SS_ListHeading']");
	public static By pageJumpDiv = By.cssSelector("div[class='fieldset pageJumpto']");
	public static By pageJumpTextBox = By.cssSelector("input[id='pagenumberelement']");
	public static By pageNumber = By.cssSelector("span[class*='SS_Pag_Show']");
	public static By highlightOptions = By.cssSelector("li[class='highlightoptions']");
	public static By selectedColors = By.cssSelector("span[class='selectedcolor']");
	public static By expandColorSelector = By.cssSelector("button[data-id='togglecolorselector']");
	public static By expandedColorSelector = By.cssSelector("button[class='icon la-TriangleUp']");
	public static By highlightColors = By.cssSelector("li[class='highlightcolors']");
	public static By highlightedText = By.cssSelector("span[class*='highlighted']");
	public static By inlineresultlistul =  By.cssSelector("ul[class='taxfulldoc-sidebar-resultslist']");
	public static By inlineresultlistli =  By.cssSelector("li[class='tax-fulldoc-listitem']");
	public static By inlineresultbutton =  By.cssSelector("button[class='btn-link miniresult']");
	public static By resultlink1 = By.cssSelector("a[data-action='linktaxsearch']");
	public static By spanclass1 = By.cssSelector("span[class='SS_ListItemContent']");
	
	public static By sourceResultHeader = By.cssSelector("header[class='banner results-data']");
	public static By HeadersourceResult2 = By.cssSelector("h2[class='pagewrapper']");
	public static By headerSource = By.tagName("span");
	public static By tagSpan2 = By.tagName("span[class='query']");

	public static By jumpTo = By.cssSelector("div[class='resultsRightSide desktopNeverOverflow']");
	public static By jumptoAside = By.cssSelector("aside[class='supplemental jumptodropdown']");

	public static By resultsList = By.cssSelector("form[class='results-list search']");	
	public static By docTextRef = By.cssSelector("footer[class='SS_Footnote']");
	
	

	public static By italicText = By.tagName("i");	

	public static By docTextUnder = By.cssSelector("span[class='SS_RptrLine']");
	public static By docTextUnderSpan = By.cssSelector("span[class='SS_NonPaginatedRptr']");
	public static By generalOverview = By.cssSelector("a[class='SS_SummaryLinks'][data-topicid ='urn:topic:8E1979D1FCBF43AB8B79C757C1926A88']");
    public static By menuContainer = By.cssSelector("ul[id='topicSummaryMenuContainer']");
    public static By getDocument =By.cssSelector("a[class='icon la-Document']");
    public static By docSliderLeft1 = By.cssSelector("button[class='icon la-TriangleLeft'][data-action='linkdocslider']");
	public static By docSliderRight1 = By.cssSelector("button[class='icon la-TriangleRightAfter'][data-action='linkdocslider']");
	
	public static By contents = By.cssSelector("aside[class='minitoc leftpane noreader']");
	public static By tag1 = By.tagName("ul");
	public static By tag2 = By.cssSelector("li[class='collapsed']");
	
	
}