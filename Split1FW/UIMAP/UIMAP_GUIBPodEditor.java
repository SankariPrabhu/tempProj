package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_GUIBPodEditor {

	// buttons
	public static By btnInfo = By.cssSelector("button[data-id='0']");
	public static By btnSave = By.cssSelector("input[class='secondary save-btn']");
	public static By btnPreview = By.cssSelector("input[data-action='preview']");
	public static By btnPublish = By.cssSelector("input[data-action='publish']");
	public static By btnGoToPreviewPage = By.cssSelector("a[class='preview-link']");
	public static By btnCancel = By.cssSelector("input[data-action='cancel']");
	public static By btnClearCustomer = By.cssSelector("input[data-action='clearcustomer']");
	// Others
	public static By divPodEditExpIcon = By.cssSelector("div[class*='section-wrapper']");
	public static By eltCustomerList = By.cssSelector("div[class='customer-list']");
	public static By lstTagUList = By.tagName("ul");
	public static By lstTagListItems = By.tagName("li");
	public static By btnProceed = By.cssSelector("input[value='Proceed']");

	public static By txtName = By.cssSelector("input[data-key='name']");
	public static By txtTitle = By.cssSelector("input[data-key='title']");
	public static By txtComments = By.cssSelector("textarea[data-key='comments']");
	public static By txtClientId = By.cssSelector("input[data-key='value']");
	public static By btnAddCustomer = By.cssSelector("input[data-action='addcustomer']");

	public static By divCasePullCust = By.cssSelector("div[id='casepull-config']");
	public static By btnAddPod = By.cssSelector("button[data-action='addpod']");

	public static By btnDateRestriction = By.cssSelector("input[class='check-box']");
	public static By btnTimePeriod = By.cssSelector("input[value='NoDate']");
	public static By btnEditJurisdiction = By.cssSelector("input[id='editjurisdictions']");
	public static By divJurisdictions = By.cssSelector("ul[class='selectjurisdictions']");
	public static By lijurisdiction = By.tagName("li");

	public static By divClientId = By.cssSelector("div[class*='guib_billingeditor']");

	public static By chkShowDateFilter = By.cssSelector("input[id='showdatefilter']");
	public static By divDateFilter = By.cssSelector("div[class='datefilter']");
	public static By txtSourceId = By.cssSelector("input[id='inputsourceid']");
	public static By btnSourceId = By.cssSelector("input[id='addsource']");
	public static By txtSourceName = By.cssSelector("input[id='inputSearchSources']");
	public static By btnSourceName = By.cssSelector("input[id='searchsources']");
	public static By lstSourceName = By.cssSelector("ul[id='searchResults']");
	public static By lstSelectedSource = By.cssSelector("ul[id='selectedSourcesList']");
	public static By divCategoreis = By.cssSelector("div[id='selectcontentypes']");
	public static By divUSfederal = By.cssSelector("div[class='usfederal']");
	public static By chkbox = By.cssSelector("input[type='checkbox']");
	public static By btnOk = By.cssSelector("input[value='OK']");
	public static By imgPod = By.cssSelector("img[src*=.jpg']");
	public static By headerPod = By.tagName("h2");
	public static By btnClrCstmr = By.cssSelector("input[data-action='clearcustomer']");
	public static By divSelctdJrsdns = By.cssSelector("div[id='selectedJurisdictions']");
	public static By divJurisTitle = By.cssSelector("div[class='jurisdictiondisplay']");
	public static By txtCategory = By.cssSelector("span[class='totalcount']");
	public static By lnkEditCategory = By.cssSelector("input[id='editcategory']");
	public static By btnOk1 = By.cssSelector("input[data-action='confirm']");
	public static By divCustDetail = By.cssSelector("div[class='cust-detail']");
	public static By btnClearCust = By.cssSelector("input[data-action='clearcustomer']");
	public static By divJurisdictions1 = By.cssSelector("ul[class='jurisdictiondisplay']");
	public static By eltlist1 = By.cssSelector("div[class='selectedcontent']");
	public static By btnSource = By.cssSelector("input[id='editsources']");

	public static By lnkLinks = By.tagName("a");

	public static By btnEditCustomer = By.cssSelector("input[data-action='editcustomer']");
	public static By eltdiv = By.tagName("div");

	public static By btneditSources = By.cssSelector("input[id='editsources']");
	public static By lnkClearall = By.cssSelector("a[id='sourcesclearall']");
	public static By lstSource = By.cssSelector("ul[class='sources']");
	public static By source = By.tagName("li");
	public static By coloumn1 = By.cssSelector("div[class='column1']");
	public static By coloumn2 = By.cssSelector("div[class='column2']");
	public static By sourceAdd = By.cssSelector("input[class='sourceaddbutton']");
	public static By oKSaveSources = By.cssSelector("input[id='savesources']");
	public static By podGetPrintCont = By.cssSelector("div[class='getprint-cont']");
	public static By input = By.tagName("input");
	public static By label = By.tagName("label");
	public static By tabHeaders=By.cssSelector("ul[class='tabheaders']");
	public static By tabs=By.tagName("li");
	public static By tabButton=By.tagName("button");
	public static By tabUl=By.cssSelector("ul[class='tabs']");
	public static By tabLi=By.cssSelector("li[class='shown']");
	public static By tabFieldset=By.cssSelector("fieldset[class='supplemental lrmargin']");
	public static By fieldsetList=By.tagName("li");
	public static By fieldsetInput=By.tagName("input");
	public static By fieldsetSpan=By.tagName("span");
	public static By fieldsetSubUl=By.cssSelector("ul[class='subgroupmargin']");
	public static By fieldsetSelect=By.tagName("select");
	public static By fieldsetLabel=By.tagName("label");
	public static By fieldsetDiv=By.tagName("div");
	public static By fieldsetOption=By.tagName("option");
	public static By customCont = By.cssSelector("div[class='custom-block']");
	public static By editorLabel = By.cssSelector("div[class='editor-label']");
	public static By table = By.tagName("table");
	public static By row = By.tagName("tr");
	public static By data = By.tagName("td");
	public static By select = By.tagName("select");
	public static By customerDiv=By.cssSelector("div[class='col']");
	public static By customerHeader=By.tagName("h3");
	public static By customerDetail=By.cssSelector("div[class='cust-detail']");
	public static By customerSpan=By.tagName("span");
	
	public static By sectionTab=By.cssSelector("section[class='header-navtabs']");
	public static By dilaog=By.cssSelector("aside[class='dialog tabaction_dialog']");
	public static By tabTitle=By.cssSelector("input[id='Form_TabTitle']");
	public static By btnSave1=By.cssSelector("input[data-action='addtab']");
	public static By sectionCustomization=By.cssSelector("div[class='comp guib_tabbedzoneeditor']");
	public static By headerSection=By.cssSelector("div[data-zoneid*='container-a']");
	public static By leftSection=By.cssSelector("div[data-zoneid*='container-b']");
	public static By rightSection=By.cssSelector("div[data-zoneid*='container-d']");
	public static By centerSection=By.cssSelector("div[data-zoneid*='container-c']");
	
	public static By practiceAreaDrpdwn=By.cssSelector("select[id='PracticeAreaList']");
	public static By setPracAreaBtn=By.cssSelector("input[value='Set Practice Area']");
	public static By selectedPracarea=By.cssSelector("ul[id='selectedAreasList']");
	public static By seelctedList=By.cssSelector("li[class='sortableitem']");
	
	public static By podPracArea = By.cssSelector("div[class*='practiceareabuilder']");

	public static By showdateChk = By.cssSelector("input[id='showdatefilter']");
	public static By radio = By.cssSelector("input[type='radio']");
	public static By datefilterdiv = By.cssSelector("div[class='datefilter']");

	public static By sectionPods=By.cssSelector("div[class='comp guib_zoneeditor']");
	public static By treatisesCont = By.cssSelector("div[class='section sourcecategory']");
	public static By addPublishers=By.cssSelector("input[id='addpublishers']");
	public static By addPubDialog=By.cssSelector("aside[class='ladialog matthewbenderbuilder large']");
	public static By inputButton=By.cssSelector("input[type='button']");
	public static By allPublisherList=By.cssSelector("ul[class='popuppublishers popupall']");
	public static By selectedPublisherList=By.cssSelector("ul[class='popuppublishers popupselected']");
	public static By savePublishers=By.cssSelector("input[id='savepublishers']");
	public static By selectedPubCont=By.cssSelector("div[class='selectedpublishers']");	
	public static By form=By.cssSelector("form[class='paForm']");
	public static By practiceArea=By.cssSelector("span[class='displaywidth']");
	public static By pagedetail=By.cssSelector("div[class*='guib_pagedetails']");
	public static By column=By.cssSelector("div[class='col']");
	public static By hdr = By.tagName("h3");
	public static By span = By.tagName("span");
	public static By narrowPublishers=By.cssSelector("input[name='publisherFilter']");
	public static By findPublishers=By.cssSelector("input[value='Find Publisher']");
	public static By publisherPopup = By.cssSelector("aside[class='dialog publish_dialog']");	
	public static By headerbtn = By.cssSelector("div[class='header-btn']");
}
