package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Feedback

{
	public static String txtFeedbackPage = "feedback";
	public static String txtHomePage = "firsttime";

	public static By txtAreaComments = By.cssSelector("textarea[id='Form_Comments']");
	public static By txtName = By.cssSelector("input[id='Form_Name']");
	public static By txtEmail = By.cssSelector("input[id='Form_EmailAddress']");
	public static By txtEmailStatus = By.cssSelector("div[class='success-msg']");
	public static By btnSend = By.cssSelector("input[id='Button_Send']");
	public static By btnClose = By.cssSelector("input[id='Success_Button_Close']");
	public static By frmFeedback = By.cssSelector("form[id='feedbackform']");
	public static By list = By.tagName("li");
	public static By txtThankyou = By.tagName("h2");
	public static By txtErrComment = By.cssSelector("label[for='Form_Comments']");
	public static By txtErrEmail = By.cssSelector("label[for='Form_EmailAddress']");
	public static By pageContent = By.cssSelector("div[class='pagewrapper']");

	public static By error = By.cssSelector("label[class='error']");
	public static By header = By.tagName("h2");
	public static By lableComments =By.cssSelector("lable[for='Form_Comments']");
	public static By lableName =By.cssSelector("lable[for='Form_Name']");
	public static By lableEmail =By.cssSelector("lable[for='Form_EmailAddress']");
	public static By divdilag=By.cssSelector("div[class='dialog-content']");
}
