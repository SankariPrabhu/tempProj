package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_MedmalNavigator {

	public static By expandCollapse = By.cssSelector("ul[class='expand-collapse-list']");
	public static By viewAllTopics = By.cssSelector("button[id='viewAllTopics']");
	public static By medicalResearch = By.cssSelector("div[id='medical-research-topics']");
	public static By changeTopic = By.cssSelector("a[class='mrchangetopic']");
	public static By medicalResearchResults = By.cssSelector("section[id='medicalResearchResults']");
	public static By backToTopicList = By.cssSelector("button[id='backToTopicList']");
	public static By topicHeaderCont = By.cssSelector("ul[id='topicsSearchResults']");
	public static By listitem = By.tagName("li");
	public static By expandTopic = By.cssSelector("button[class*='expand-topic']");
	public static By links = By.tagName("a");
	public static By strong = By.tagName("strong");
	public static By topicName = By.cssSelector("span[class='topic-name']");
	public static By subListCont = By.cssSelector("ul[class='sub-topics']");
	public static By noResultsCont = By.cssSelector("h3[class='no-results-alert']");
	public static By clientIdDropdown = By.cssSelector("ul[class='clientidmenu menu']");
	public static By testClientB = By.cssSelector("button[class='Test Client B']");
	public static By testClientA = By.cssSelector("button[class='Test Client A']");	
	public static By podheader = By.cssSelector("h2[class='pod-header']");
	
}
