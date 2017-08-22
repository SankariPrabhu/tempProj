package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Register {

	// TextBox

	public static By firstName = By.cssSelector("input[id*='fnameInput::content']");
	public static By lastName = By.cssSelector("input[id*='lnameInput::content']");
	public static By eMail = By.cssSelector("input[id*='emailInput::content']");
	public static By registrationCode = By.cssSelector("input[id*='rcodeInput::content']");
	public static By error = By.cssSelector("div[id*='r1:1:pgl1'][class*='xei']");

	// Buttons

	public static By submitNext = By.cssSelector("div[id*='nextBtn']");

}
