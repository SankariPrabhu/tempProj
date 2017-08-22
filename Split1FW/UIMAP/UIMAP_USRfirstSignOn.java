package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_USRfirstSignOn {

	// Drop down
	public static By locale = By.cssSelector("select[id*='Locale']"); // fsoRegion:0:formatLocaleSoc
	public static By timeZone = By.cssSelector("select[id*='timeZone']");// soctimezone::content
	public static By position = By
			.cssSelector("select[id*='position']");
	public static By program = By.cssSelector("select[id*='program']");// fsoRegion:0:programSoc
																		// programInput::content
	public static By gradTerm = By.cssSelector("select[id*='gradTerm']");// fsoRegion:0:gradTermSoc
																			// gradTermInput::content

	// TextBox

	public static By gradYear = By.cssSelector("input[id*='gradYear']");
	// Buttons

	public static By submitNext = By.cssSelector("div[id*='submit']");// fsoRegion:0:submitBtn

	// Others

	public static By trainStopHeader = By.cssSelector("div[id*='trainStops']");
	public static By trainStopHeader1 = By.cssSelector("ol[id*='Steps']");
	// RadioButton

	public static By student = By.cssSelector("input[id*='sbrSTUbtn']");

	// CheckBox

	public static By participateLA = By
			.cssSelector("input[id*='ProgramCheckbox']");// sbc1

	// Links

	public static By learnRewards = By
			.cssSelector("a[id*='rewardsDetailsLink']");
	public static By rewardRules = By.cssSelector("a[id*='rewardsRulelsLink']");

}
