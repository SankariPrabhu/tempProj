package functionallibraries;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import UIMAP.UIMAP_MedmalResearch;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class MedmalResearchParties extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	Properties localizeProperties = Settings.getInstance();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);
	public static int check = 0;
	public static int val = 0;

	public MedmalResearchParties(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("medmalresearchparties"))
				commonLibrary.sleep(5000);

		} while (!url.contains("medmalresearchparties") && counter < 40);

		if (!driver.getCurrentUrl().contains("medmalresearchparties")) {
			throw new IllegalStateException("Medmal Research Parties page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to logout
	// the Medmal Research Parties page
	// # Function Name : logout
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		generalFunctions.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Add New Party button
	// # Function Name : clickAddNewParty
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickAddNewParty() {
		WebElement addNewParty = commonLibrary.isExist(UIMAP_MedmalResearch.addNewParty, 10);
		if (addNewParty != null) {
			commonLibrary.clickButtonParentWithWait(addNewParty, "AddNewParty");
			pageCheck.ajaxElementCheck(driver, properties.getProperty("xSpinner"));
		} else {
			report.updateTestLog("Click Add New Party button", "Add New Party button is not available", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add details in Add New Party Popup
	// # Function Name : enterDetailsInAddNewParty
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties enterDetailsInAddNewParty(String firstName, String middleName, String lastName, String dob, String telephone, String streetAddress, String apartmentNumber, String city, String state, String zip) {
		WebElement popup = commonLibrary.isExist(UIMAP_MedmalResearch.popup, 10);
		WebElement popupContent = commonLibrary.isExist(popup, UIMAP_MedmalResearch.popupContent, 10);
		WebElement firstNameBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.firstName, 10);
		WebElement middleNameBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.middleName, 10);
		WebElement lastNameBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.lastName, 10);
		WebElement dobBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.dob, 10);
		WebElement telephoneBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.telephone, 10);
		WebElement streetAddressBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.streetAddress, 10);
		WebElement apartmentNumberBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.apartmentNumber, 10);
		WebElement cityBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.city, 10);
		WebElement stateBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.state, 10);
		WebElement zipBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.zip, 10);

		if (firstName != "") {
			if (firstNameBox != null) {
				commonLibrary.setDataInTextBox(firstNameBox, firstName, "FirstName");
			} else {
				report.updateTestLog("Set " + firstName + " in FirstName text box", "FirstName textbox is not available", Status.FAIL);
			}
		}
		if (middleName != "") {
			if (middleNameBox != null) {
				commonLibrary.setDataInTextBox(middleNameBox, middleName, "MiddleName");
			} else {
				report.updateTestLog("Set " + middleName + " in MiddleName text box", "MiddleName textbox is not available", Status.FAIL);
			}
		}
		if (lastName.length() > 0) {
			if (lastNameBox != null) {
				commonLibrary.setDataInTextBox(lastNameBox, lastName, "LastName");
			} else {
				report.updateTestLog("Set " + lastName + " in LastName text box", "LastName textbox is not available", Status.FAIL);
			}
		}
		if (dob != "") {
			if (dobBox != null) {
				commonLibrary.setDataInTextBox(dobBox, dob, "DateOfBirth");
			} else {
				report.updateTestLog("Set " + dob + " in DateOfBirth text box", "DateOfBirth textbox is not available", Status.FAIL);
			}
		}
		if (telephone.length() > 0) {
			if (telephoneBox != null) {
				commonLibrary.setDataInTextBox(telephoneBox, telephone, "Telephone");
			} else {
				report.updateTestLog("Set " + telephone + " in Telephone text box", "Telephone textbox is not available", Status.FAIL);
			}
		}
		if (streetAddress != "") {
			if (streetAddressBox != null) {
				commonLibrary.setDataInTextBox(streetAddressBox, streetAddress, "StreetAddress");
			} else {
				report.updateTestLog("Set " + streetAddress + " in StreetAddress text box", "StreetAddress textbox is not available", Status.FAIL);
			}
		}
		if (apartmentNumber != "") {
			if (apartmentNumberBox != null) {
				commonLibrary.setDataInTextBox(apartmentNumberBox, apartmentNumber, "ApartmentNumber");
			} else {
				report.updateTestLog("Set " + apartmentNumber + " in ApartmentNumber text box", "ApartmentNumber textbox is not available", Status.FAIL);
			}
		}
		if (city != "") {
			if (cityBox != null) {
				commonLibrary.setDataInTextBox(cityBox, city, "city");
			} else {
				report.updateTestLog("Set " + city + " in City text box", "City textbox is not available", Status.FAIL);
			}
		}
		if (state != "") {
			if (stateBox != null) {
				commonLibrary.selectFromListOption(stateBox, state);
			} else {
				report.updateTestLog("Select " + state + " from State dropdown", "State dropdown is not available", Status.FAIL);
			}
		}
		if (zip != "") {
			if (zipBox != null) {
				commonLibrary.setDataInTextBox(zipBox, zip, "Zip");
			} else {
				report.updateTestLog("Set " + zip + " in Zip text box", "Zip textbox is not available", Status.FAIL);
			}
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Add Party button
	// # Function Name : clickAddParty
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickAddParty() {
		WebElement addParty = commonLibrary.isExist(UIMAP_MedmalResearch.addParty, 10);
		if (addParty != null) {
			commonLibrary.clickButtonParentWithWait(addParty, "AddParty");
			// pageCheck.AjaxElementCheck(driver,
			// properties.getProperty("xSpinner"));
		} else {
			report.updateTestLog("Click Add Party button", "Add Party button is not available", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check error message
	// # Function Name : checkErrorMessage
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties checkErrorMessage(String fieldName) {
		boolean flag = false;
		WebElement popup = commonLibrary.isExist(UIMAP_MedmalResearch.popup, 10);
		WebElement popupContent = commonLibrary.isExist(popup, UIMAP_MedmalResearch.popupContent, 10);
		List<WebElement> popupLabel = commonLibrary.isExistList(popupContent, UIMAP_MedmalResearch.popupLabel, 10);
		if (popupLabel.size() > 0) {
			for (WebElement item : popupLabel) {
				List<WebElement> popupSpan = commonLibrary.isExistList(item, UIMAP_MedmalResearch.popupSpan, 10);
				if (popupSpan.size() > 0) {
					for (WebElement item1 : popupSpan) {
						if (item1.getText().trim().toLowerCase().contains(fieldName.toLowerCase())) {
							WebElement errorMessage = commonLibrary.isExist(item, UIMAP_MedmalResearch.errorMessage, 10);
							if (errorMessage.getText().trim().toLowerCase().contains("this field is required") || errorMessage.getText().trim().toLowerCase().contains("invalid phone number")) {
								// report.updateTestLog("Verify 'This field is required' message displays above "+fieldName+" text box",
								// "'This field is required' message is displayed above "+fieldName+" text box",
								// Status.PASS);
								flag = true;
								break;
							}
						}
					}
				}
			}
		} else
			report.updateTestLog("Verify 'This field is required' message displays above " + fieldName + " text box", "'This field is required' message is not displayed above " + fieldName + " text box", Status.FAIL);
		if (!flag) {
			if (fieldName.contains("Telephone"))
				report.updateTestLog("Verify 'Invalid Phone Number' message displays above " + fieldName + " text box", "'Invalid Phone Number' message is not displayed above " + fieldName + " text box", Status.FAIL);
			else
				report.updateTestLog("Verify 'This field is required' message displays above " + fieldName + " text box", "'This field is required' message is not displayed above " + fieldName + " text box", Status.FAIL);

		} else {
			if (fieldName.contains("Telephone"))
				report.updateTestLog("Verify 'Invalid Phone Number' message displays above " + fieldName + " text box", "'Invalid Phone Number' message is displayed above " + fieldName + " text box", Status.PASS);
			else
				report.updateTestLog("Verify 'This field is required' message displays above " + fieldName + " text box", "'This field is required' message is displayed above " + fieldName + " text box", Status.PASS);

		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click cancel button
	// # Function Name : clickCancel
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickCancel() {

		WebElement aside = commonLibrary.isExist(By.cssSelector("aside[role='dialog']"), 10);
		WebElement cancel = commonLibrary.isExistNegative(aside, UIMAP_MedmalResearch.cancel, 10);
		if (cancel != null) {
			commonLibrary.clickButtonParentWithWait(cancel, "Cancel");
			// pageCheck.AjaxElementCheck(driver,
			// properties.getProperty("xSpinner"));
		} else {
			report.updateTestLog("Click Cancel button", "Cancel button is not available", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Research Party Field
	// # Function Name : verifyResearchPartyField
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties verifyResearchPartyField(String rpLastName1, String rpFirstName1, String rpCity1, String rpState1) {
		WebElement rpLastName = commonLibrary.isExist(UIMAP_MedmalResearch.rpLastName, 10);
		WebElement rpFirstName = commonLibrary.isExist(UIMAP_MedmalResearch.rpFirstName, 10);
		WebElement rpCityName = commonLibrary.isExist(UIMAP_MedmalResearch.rpCityName, 10);
		WebElement rpStateName = commonLibrary.isExist(UIMAP_MedmalResearch.rpStateName, 10);

		if (rpLastName != null && rpLastName.getText().equalsIgnoreCase(rpLastName1) && rpFirstName != null && rpFirstName.getText().equalsIgnoreCase(rpFirstName1)) {

			report.updateTestLog("Verify Research Party Name", "Research Party Name is displayed as :" + "'" + rpLastName1 + "," + " " + rpFirstName1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party Name", "Research Party Name is not displayed", Status.FAIL);
		}

		if (rpCityName != null && rpCityName.getText().equals(rpCity1)) {

			report.updateTestLog("Verify Research Party City Name", "Research Party City Name is displayed as :" + "'" + rpCity1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party City Name", "Research Party City Name is not displayed", Status.FAIL);
		}
		if (rpStateName != null && rpStateName.getText().equals(rpState1)) {

			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is displayed as :" + "'" + rpState1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is not displayed", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Research Party Edit Button
	// # Function Name : clickEditButton
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickEditButton() {
		WebElement rpEdit = commonLibrary.isExist(UIMAP_MedmalResearch.rpEdit, 10);
		if (rpEdit != null) {
			commonLibrary.clickButtonParentWithWait(rpEdit, "Edit");
		} else {
			report.updateTestLog("Click Edit button", "Edit button is not available", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to update Research Party Details
	// # Function Name : updateResearchPartyDetails
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties updateResearchPartyDetails(String rpLastName, String rpFirstName, String rpCity, String rpState) {
		WebElement lastNameBox = commonLibrary.isExist(UIMAP_MedmalResearch.lastName, 10);
		WebElement firstNameBox = commonLibrary.isExist(UIMAP_MedmalResearch.firstName, 10);
		WebElement cityBox = commonLibrary.isExist(UIMAP_MedmalResearch.city, 10);
		WebElement stateBox = commonLibrary.isExist(UIMAP_MedmalResearch.state, 10);
		if (rpFirstName != "") {
			if (firstNameBox != null) {
				commonLibrary.setDataInTextBox(firstNameBox, rpFirstName, "FirstName");
			} else {
				report.updateTestLog("Set " + rpFirstName + " in FirstName text box", "FirstName textbox is not available", Status.FAIL);
			}
		}
		if (rpLastName.length() > 0) {
			if (lastNameBox != null) {
				commonLibrary.setDataInTextBox(lastNameBox, rpLastName, "LastName");
			} else {
				report.updateTestLog("Set " + rpLastName + " in LastName text box", "LastName textbox is not available", Status.FAIL);
			}
		}
		if (rpCity != "") {
			if (cityBox != null) {
				commonLibrary.setDataInTextBox(cityBox, rpCity, "city");
			} else {
				report.updateTestLog("Set " + rpCity + " in City text box", "City textbox is not available", Status.FAIL);
			}
		}
		if (rpState != "") {
			if (stateBox != null) {
				commonLibrary.selectFromListOption(stateBox, rpState);
			} else {
				report.updateTestLog("Select " + rpState + " from State dropdown", "State dropdown is not available", Status.FAIL);
			}
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Research Party Form Save
	// Button
	// # Function Name : clickRpFormSaveButton
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickRpFormSaveButton() {
		WebElement rpFormSave = commonLibrary.isExist(UIMAP_MedmalResearch.rpSaveForm, 10);
		if (rpFormSave != null) {
			commonLibrary.clickButtonParentWithWait(rpFormSave, "SaveChanges");
		} else {
			report.updateTestLog("Click Save Changes button", "Save Changes button is not available", Status.FAIL);
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Research Party Delete Button
	// # Function Name : clickRpDeleteButton
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickRpDeleteIcon() {
		WebElement rpDelete = commonLibrary.isExist(UIMAP_MedmalResearch.rpDelete, 10);

		if (rpDelete != null) {
			commonLibrary.clickButtonParentWithWait(rpDelete, "Delete");
		} else {
			report.updateTestLog("Click Delete Icon", "Delete Icon is not available", Status.FAIL);
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Delete Party Button in Pop-up
	// # Function Name : clickRpDeleteButton
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickDeletePartyButton() {
		// WebElement popup=commonLibrary.isExist(UIMAP_MedmalResearch.popup,
		// 10);
		WebElement deleteParty = commonLibrary.isExist(UIMAP_MedmalResearch.deleteParty, 10);
		if (deleteParty != null) {
			commonLibrary.clickButtonParentWithWait(deleteParty, "DeleteParty");
		} else {
			report.updateTestLog("Click Delete Party button", "Delete Party button is not available", Status.FAIL);
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify deleted Research Party
	// # Function Name : verifyDeletedPartyName
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties verifyDeletedPartyName() {
		WebElement rpLastName = commonLibrary.isExist(UIMAP_MedmalResearch.rpLastName, 10);
		WebElement rpFirstName = commonLibrary.isExist(UIMAP_MedmalResearch.rpFirstName, 10);

		if (rpLastName == null && rpFirstName == null) {

			report.updateTestLog("Verify Deleted Research Party Name", "Research Party Name is not exist", Status.PASS);

		} else {
			report.updateTestLog("Verify Deleted Research Party Name", "Research Party Name is exist", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click Research Party Name Link
	// # Function Name : clickPartyNameLink
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties clickPartyNameLink() {
		WebElement rpName = commonLibrary.isExist(UIMAP_MedmalResearch.rpLastName, 10);
		if (rpName != null) {
			commonLibrary.clickButtonParentWithWait(rpName, "Party Name Link");
		} else {
			report.updateTestLog("Click Party Name Link", "Party Name Link is not available", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click DPPA dropdown in MN
	// Research Party
	// # Function Name : clickDppaDropDownMn     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickDppaDropDownMn(String dppaOption1) {
		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_MedmalResearch.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_MedmalResearch.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 25);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_MedmalResearch.termsofuse, 20);

		WebElement dppaDropDown = commonLibrary.isExist(termsofuse, UIMAP_MedmalResearch.dppaDropDown, 20);

		if (dppaDropDown != null) {

			commonLibrary.clickLink(dppaDropDown, "DPPA");
			commonLibrary.selectFromListOption(dppaDropDown, dppaOption1);

		}

		else {
			report.updateTestLog("Verify DPPA Dropdown ", "DPPA Dropdown is not available", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click GLBA dropdown in MN
	// Research Party
	// # Function Name : clickGlbaDropDownMn     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickGlbaDropDownMn(String glbaoption) {
		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_MedmalResearch.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_MedmalResearch.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_MedmalResearch.termsofuse, 20);
		WebElement glbaDropDown = commonLibrary.isExist(termsofuse, UIMAP_MedmalResearch.glbaDropDown, 20);
		if (glbaDropDown != null) {

			commonLibrary.clickLink(glbaDropDown, "GLBA");
			commonLibrary.selectFromListOption(glbaDropDown, glbaoption);

		}

		else {
			report.updateTestLog("Verify GLBA Dropdown ", "GLBA Dropdown is not available", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to click DMF dropdown in MN
	// Research Party
	// # Function Name : clickDmfDropDownMn     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickDmfDropDownMn(String dmfoption) {
		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_MedmalResearch.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_MedmalResearch.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);
		WebElement termsofuse = commonLibrary.isExist(diverms, UIMAP_MedmalResearch.termsofuse, 20);
		WebElement dmfDropDown = commonLibrary.isExist(termsofuse, UIMAP_MedmalResearch.dmfDropDown, 20);

		if (dmfDropDown != null) {

			commonLibrary.clickLink(dmfDropDown, "DMF");
			commonLibrary.selectFromListOption(dmfDropDown, dmfoption);

		}

		else {
			report.updateTestLog("Verify DMF Dropdown ", "DMF Dropdown is not available", Status.DONE);
		}
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used Click confirm button in MN
	// Research Party
	// # Function Name : clickConfirmMn     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickConfirmMn() {

		driver.switchTo().frame(0);
		WebElement diverms = commonLibrary.isExistNegative(UIMAP_MedmalResearch.divperms1, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_MedmalResearch.divperms1, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 5);

		WebElement confirmButton = commonLibrary.isExist(diverms, UIMAP_MedmalResearch.confirmButton, 20);
		commonLibrary.clickButtonParent(confirmButton, "Confirm");
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to verify PR page in MN
	// Research Party
	// # Function Name : verifyPRPageDisplay     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties verifyPRPageDisplay() {

		driver.switchTo().frame(0);
		WebElement prcontent = commonLibrary.isExistNegative(UIMAP_MedmalResearch.prTabContainer, 20);

		if (prcontent != null) {
			report.updateTestLog("Verify Public Records Page", "Public Records Page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public Records Page", "Public Records Page is not displayed", Status.FAIL);
		}
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function used to verify PR page in MN
	// Research Party
	// # Function Name : verifyPRAuthorization     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public MedmalResearchParties verifyPRAuthorization(String PRAuthMsg) {

		/*
		 * driver.switchTo().frame(0); WebElement prEntitlement =
		 * commonLibrary.isExistNegative( UIMAP_MedmalResearch.prEntitleMsg,
		 * 20);
		 * 
		 * int count = 0; do { count++; prEntitlement = commonLibrary.isExist(
		 * UIMAP_MedmalResearch.prEntitleMsg, 20); commonLibrary.sleep(3000); }
		 * while (prEntitlement == null && count < 5);
		 * 
		 * WebElement prAuth = commonLibrary.isExist(prEntitlement,
		 * UIMAP_MedmalResearch.prAuthorization, 20);
		 */

		WebElement prAuth = commonLibrary.isExist(UIMAP_MedmalResearch.prAuthorization, 20);

		if (prAuth != null && prAuth.getText().equalsIgnoreCase(PRAuthMsg)) {
			report.updateTestLog("Verify PR Feature Message", "Feature Not Authorized message is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify PR Feature Message", "Feature Not Authorized message is not displayed", Status.FAIL);
		}
		/* driver.switchTo().defaultContent(); */
		return new MedmalResearchParties(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to add First Name, Last Name & State in
	// Add New Party Popup
	// # Function Name : enterPartyDetails
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties enterPartyDetails(String firstName, String lastName, String state) {
		WebElement popup = commonLibrary.isExist(UIMAP_MedmalResearch.popup, 10);
		WebElement popupContent = commonLibrary.isExist(popup, UIMAP_MedmalResearch.popupContent, 10);
		WebElement firstNameBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.firstName, 10);
		WebElement lastNameBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.lastName, 10);
		WebElement stateBox = commonLibrary.isExist(popupContent, UIMAP_MedmalResearch.state, 10);

		if (firstName != "") {
			if (firstNameBox != null) {
				commonLibrary.setDataInTextBox(firstNameBox, firstName, "FirstName");
			} else {
				report.updateTestLog("Set " + firstName + " in FirstName text box", "FirstName textbox is not available", Status.FAIL);
			}
		}

		if (lastName.length() > 0) {
			if (lastNameBox != null) {
				commonLibrary.setDataInTextBox(lastNameBox, lastName, "LastName");
			} else {
				report.updateTestLog("Set " + lastName + " in LastName text box", "LastName textbox is not available", Status.FAIL);
			}
		}

		if (state != "") {
			if (stateBox != null) {
				commonLibrary.selectFromListOption(stateBox, state);
			} else {
				report.updateTestLog("Select " + state + " from State dropdown", "State dropdown is not available", Status.FAIL);
			}
		}

		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Research Party Field -Name &
	// State
	// # Function Name : verifyPartyFieldState
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties verifyPartyFieldNameState(String rpLastName1, String rpFirstName1, String rpState1) {
		WebElement rpLastName = commonLibrary.isExist(UIMAP_MedmalResearch.rpLastName, 10);
		WebElement rpFirstName = commonLibrary.isExist(UIMAP_MedmalResearch.rpFirstName, 10);
		WebElement rpStateName = commonLibrary.isExist(UIMAP_MedmalResearch.rpStateName, 10);

		if (rpLastName != null && rpLastName.getText().equalsIgnoreCase(rpLastName1) && rpFirstName != null && rpFirstName.getText().equalsIgnoreCase(rpFirstName1)) {

			report.updateTestLog("Verify Research Party Name", "Research Party Name is displayed as :" + "'" + rpLastName1 + "," + " " + rpFirstName1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party Name", "Research Party Name is not displayed", Status.FAIL);
		}

		if (rpStateName != null && rpStateName.getText().equals(rpState1)) {

			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is displayed as :" + "'" + rpState1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is not displayed", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to update Research Party State
	// # Function Name : updateResearchPartyState
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties updateResearchPartyState(String rpState) {

		WebElement stateBox = commonLibrary.isExist(UIMAP_MedmalResearch.state, 10);
		if (rpState != "") {
			if (stateBox != null) {
				commonLibrary.selectFromListOption(stateBox, rpState);
			} else {
				report.updateTestLog("Select " + rpState + " from State dropdown", "State dropdown is not available", Status.FAIL);
			}
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Research Party Field -given
	// State not displayed
	// # Function Name : verifyResearchPartyStateNegative
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties verifyResearchPartyStateNegative(String rpLastName1, String rpFirstName1, String rpState1) {
		WebElement rpLastName = commonLibrary.isExist(UIMAP_MedmalResearch.rpLastName, 10);
		WebElement rpFirstName = commonLibrary.isExist(UIMAP_MedmalResearch.rpFirstName, 10);

		WebElement rpStateName = commonLibrary.isExist(UIMAP_MedmalResearch.rpStateName, 10);

		if (rpLastName != null && rpLastName.getText().equalsIgnoreCase(rpLastName1) && rpFirstName != null && rpFirstName.getText().equalsIgnoreCase(rpFirstName1)) {

			report.updateTestLog("Verify Research Party Name", "Research Party Name is displayed as :" + "'" + rpLastName1 + "," + " " + rpFirstName1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party Name", "Research Party Name is not displayed", Status.FAIL);
		}

		if (rpStateName != null && !rpStateName.getText().equals(rpState1)) {

			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is not displayed as :" + "'" + rpState1 + "'", Status.PASS);

		} else {
			report.updateTestLog("Verify Research Party State Name", "Research Party State Name is displayed as :" + "'" + rpState1 + "'", Status.FAIL);
		}
		return new MedmalResearchParties(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the State dropdown value
	// in Add New Party Pop-up
	// # Function Name : verifyStateDropdownResearchParty     
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public MedmalResearchParties verifyStateDropdownResearchParty(String option) {

		WebElement StateDropdown = commonLibrary.isExistNegative(UIMAP_MedmalResearch.state, 10);

		if (commonLibrary.selectIsSelected(StateDropdown, option)) {
			report.updateTestLog("Verify " + option + " is selected in State Dropdown.", option + " is selected in State Dropdown.", Status.PASS);
		} else
			report.updateTestLog("Verify " + option + " is selected in State Dropdown.", option + " is not selected in State Dropdown.", Status.FAIL);

		return new MedmalResearchParties(scriptHelper);
	}
	
	// #*****************************************************************************************************************************
	// # Function Description : Function used to click clickPermissibleLink in MN
	// Research Party
	// # Function Name : clickPermissibleLink     
	// # Author : Kalai
	// # Date Created : Feb'16
	// #*****************************************************************************************************************************

	public MedmalResearchParties clickPermissibleLink(String permissibleUse) {
		
		boolean flag=false;
		driver.switchTo().frame(0);
		
		WebElement diverms = commonLibrary.isExist(UIMAP_MedmalResearch.permissibleUse, 20);
		int count = 0;
		do {
			count++;
			diverms = commonLibrary.isExist(UIMAP_MedmalResearch.permissibleUse, 20);
			commonLibrary.sleep(3000);
		} while (diverms == null && count < 25);
		
		List<WebElement> popupLabel = commonLibrary.isExistList(diverms, By.tagName("li"), 10);
		
		if(popupLabel.size()>0){
			for(WebElement popup:popupLabel){
					if(popup.getText().toLowerCase().trim().contains(permissibleUse.toLowerCase().trim())){
					WebElement button = commonLibrary.isExistNegative(popup,By.tagName("a"),10); 
					commonLibrary.clickButtonParentWithWait(button, button.getText());
					flag = true;
					break;
				}
			}
			if(!flag)
				report.updateTestLog("Click "+permissibleUse+ " link", permissibleUse+" link is not clicked", Status.FAIL);
			
		}
		else{
			report.updateTestLog("Verify "+permissibleUse+ " link", permissibleUse+" link is not availablr", Status.FAIL);
		}
		
		driver.switchTo().defaultContent();
		return new MedmalResearchParties(scriptHelper);
	}


}
