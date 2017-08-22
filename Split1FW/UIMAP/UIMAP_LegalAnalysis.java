package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_LegalAnalysis {

	public static By questionSection = By.className("currentQuestion");
	public static By listItem = By.tagName("li");
	public static By answerInput = By.tagName("input");
	public static By submitAnswer = By.cssSelector("button[class='btn primary submitdqa']");
	public static By submitAnswerLegalAnalysis = By.cssSelector("button[id='submitAnswer']");
	public static By dashboardName = By.id("topNavigation");
	public static By button = By.tagName("button");
}
