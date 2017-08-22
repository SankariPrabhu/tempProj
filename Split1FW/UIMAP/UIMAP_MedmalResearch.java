package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_MedmalResearch {
	public static By addNewParty = By.cssSelector("button[id='addNewParty']");
	public static By popup = By.cssSelector("aside[role='dialog']");
	public static By popupContent = By
			.cssSelector("section[class='dialog-content']");
	public static By firstName = By.cssSelector("input[id='rpFirstName']");
	public static By middleName = By.cssSelector("input[id='rpMiddleName']");
	public static By lastName = By.cssSelector("input[id='rpLastName']");
	public static By dob = By.cssSelector("input[id='rpDOB']");
	public static By telephone = By.cssSelector("input[id='rpTelephone']");
	public static By streetAddress = By.cssSelector("input[id='rpAddress1']");
	public static By apartmentNumber = By.cssSelector("input[id='rpAddress2']");
	public static By city = By.cssSelector("input[id='rpCity']");
	public static By state = By.cssSelector("select[id='rpSelectState']");
	public static By zip = By.cssSelector("input[id='rpZipcode']");
	
//	public static By addParty = By.cssSelector("input[id='rpFormSave1']");
	public static By addParty = By.cssSelector("input[data-action='addFormResearchParties']");
	public static By popupLabel = By.tagName("label");
	public static By popupSpan = By.tagName("span");
	public static By errorMessage = By.cssSelector("p[class='error']");
	public static By aside = By.cssSelector("aside[class*='addpartydialogwrapper large']");
	public static By cancel = By.cssSelector("input[value='Cancel']");
	public static By rpList = By.cssSelector("section[class='rpList']");
	public static By rpLastName = By.cssSelector("span[class='lblRPLastName']");
	public static By rpFirstName = By
			.cssSelector("span[class='lblRPFirstName']");
	public static By rpCityName = By.cssSelector("span[class='lblrpCity']");
	public static By rpStateName = By.cssSelector("span[class='lblrpState']");
	public static By rpEdit = By.cssSelector("button[class='icon la-Edit']");
	public static By rpSaveForm = By.cssSelector("input[id='rpFormSave']");
	public static By rpDelete = By
			.cssSelector("button[class='icon la-CloseRemove']");
	public static By deleteParty = By
			.cssSelector("input[id='rpconfirmdelete']");
	public static By termsofuse = By.cssSelector("table[id='permUseSteps']");
	public static By divperms1 = By.cssSelector("div[id='selectPermUse']");
	public static By dppaDropDown = By
			.cssSelector("select[id='MainContent_dppaList']");
	public static By glbaDropDown = By
			.cssSelector("select[id='MainContent_glbaList']");
	public static By dmfDropDown = By.cssSelector("select[id='MainContent_dmfList']");
	public static By confirmButton = By
			.cssSelector("input[id='MainContent_btnConfirm1']");
	public static By prTabContainer = By.cssSelector("div[id='tabContainer']");
	public static By prEntitleMsg = By.cssSelector("aside[id='entitlemessage']");
	public static By prAuthorization = By.cssSelector("ul[id='wam-message-ul']");
	public static By permissibleUse = By.cssSelector("div[id='permissibleUse']");
	

}
