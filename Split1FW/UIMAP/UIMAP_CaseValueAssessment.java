package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_CaseValueAssessment {
	public static By leftContent = By.cssSelector("div[class='cvaleft']");
	public static By mainQaBox = By.cssSelector("div[id='maindqabox']");
	public static By QaBox = By.cssSelector("div[class='dqabox']");
	public static By currQues = By.cssSelector("div[class='currentQuestion']");
	public static By uList = By.tagName("ul");
	public static By listItem = By.tagName("li");
	public static By input = By.tagName("input");
	public static By submitAnswer = By.cssSelector("button[id='submitAnswer']");
	public static By submitAnswer1 = By.cssSelector("button[id='submitAnswerBtn']");
	public static By submitAnswer2 = By.cssSelector("button[class='btn primary submitdqa']");	
	public static By upcomingQsec = By.cssSelector("div[class='upcomingQSec']");
	public static By changePrevAnswer = By.cssSelector("button[id='btnprevious']");
	public static By popUpPrev = By.cssSelector("div[class='popup_previous']");
	public static By changePrevAnsCont = By.cssSelector("div[id='popUpContainer']");
	public static By changeAnswer = By.cssSelector("a[class='lnkchangeAnswer']");
	public static By ansPrevQues = By.cssSelector("div[class='ansPrevQuestion']");
	public static By submitPrevAnswer = By.cssSelector("button[id='prevSubmit']");
	public static By annotationList = By.cssSelector("ol[class='annotationlist']");
	public static By button = By.tagName("button");
	public static By closeWithoutSave = By.cssSelector("input[data-action='close']");
	public static By dialogCont = By.cssSelector("div[class='dialog-content']");
	public static By caseValueAssessment = By.cssSelector("section[id='mainCVA']");
	public static By cvaHeader = By.tagName("h1");
	public static By cvaQABox = By.cssSelector("div[class='cvaDQABox']");
	public static By qaBOXHeader = By.tagName("h2");
	public static By verdictGraph = By.cssSelector("div[class='cvagraphimg']");
	public static By continueWithoutSaving = By.cssSelector("input[value='Continue without saving']");
	public static By dialog = By.cssSelector("aside[class='dialog savechanges']");
	public static By saveDashboardBtn = By.cssSelector("input[value='Save Dashboard']");
	public static By setAddClient = By.cssSelector("h1[class='pagewrapper']");	
	public static By seljuris = By.cssSelector("li[id='selectedJuris']");		
	public static By cvaGraph = By.cssSelector("div[class='cvagraphimg']");
	public static By viewJuris = By.cssSelector("select[id='newjurisdiction']");
	public static By option = By.tagName("option");
	public static By optionsel = By.cssSelector("option[selected='']");
	
	// Document link	
	public static By DoclinkCVA = By.cssSelector("button[class='cva-annots-title']");	

}

