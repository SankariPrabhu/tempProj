package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_MedmalDashboard {

	public static By dashboardName = By.id("topNavigation");
	public static By podsContent = By.cssSelector("ul[id='pods']");
	public static By listItem = By.tagName("li");
	public static By button = By.tagName("button");
	public static By selectedTab = By.cssSelector("li[class*='selected_tab']");
	public static By navigationBar = By.cssSelector("nav[id='topNavigation']");
	public static By dashBoard = By.cssSelector("button[data-action='medmaldashboard']");
	public static By expertWitness = By.cssSelector("button[id='expertWitness']");
	
	public static By medRefHeader = By.cssSelector("aside[class='mrheader_left']");
	public static By medSOCHeader = By.tagName("header");
	public static By MedRefSection = By.cssSelector("section[id='mainSOC']");
	
//Dashboard topic
	public static By dashboardTopic = By.cssSelector("section[id='signpost']");
}
