package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_StandardOfCare {
	public static By questionSection = By.className("currentQuestion");
	public static By listItem = By.tagName("li");
	public static By answerInput = By.tagName("input");
	public static By submitAnswer = By.cssSelector("button[id='submitAnswer']");
	public static By socContent = By.id("socContent");
	public static By analysisContent = By.id("analysisContent");
	public static By issueIndicator = By.cssSelector("span[class*='answer-issue']");
	public static By showOnlyIssue = By.id("showOnlyIssues");
	public static By nonIssueType = By.cssSelector("span[class*='hidden']");
	public static By nonIssueIndicator = By.cssSelector("span[class*='Updates negative']");
	public static By sectionName = By.tagName("section");
	public static By showDetails = By.cssSelector("span[class*='showDetails']");
	public static By buttonInput = By.cssSelector("button[class*='link-button showDetails']");
	public static By standardOfCare = By.cssSelector("section[id='mainSOC']");
	public static By socHeader = By.tagName("h2");
	public static By socShowDetails = By.cssSelector("div[class*=answer-info]");
	public static By socAnswer = By.cssSelector("span[class='SS_bf']");

}
