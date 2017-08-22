package UIMAP;

import org.openqa.selenium.By;


public class UIMAP_SignIn {

	public static String txtInvokeAppTitleMsg = "localhost";
	public static String txtInvokeAppTitleMsgOtherPg = "lexisnexis";
	public static String txtChromeInvokeAppTitleMsg = "data";
	public static String txtFFInvokeAppTitleMsg = "about:blank";
	public static String txtIdUsername = "userid";
	public static String txtIdPassword = "password";
	public static String txtSigninTitleMsg = "signin";	
	public static String txtLPAHomeTitleMsg = "lpahome";

	// Buttons
	public static String btnIdLogin = "signInSbmtBtn";
	public static String btnClassSignIn = "fake-button sign-in";
	public static String forgotlk = "forgotCredential('/lnaccess/retrieveCredentials?aci=la')";

	// Checkbox
	public static String chkIdLogin = "rmflag";
	public static By pgClassSignInProfile = By.cssSelector("span[class='step-description']");
	public static By txtidUserId = By.cssSelector("input[id='userid']");
	public static By txtidFirstPassword = By.cssSelector("input[id='password']");
	public static By txtidVerifyPassword = By.cssSelector("input[id='verifyPassword']");
	public static By txtidConfirmEmail = By.cssSelector("input[id='confirmEmail']");
	public static By txtidEmail= By.cssSelector("input[id='email']");
	public static By cboPosition = By.cssSelector("select[id*='positionInput']");
	public static By lnkTextFinish = By.linkText("Finish");
	public static By pgNameWelcome = By.tagName("h2");
	public static By btnValGetStarted = By.cssSelector("input[value='Get Started']");
	public static By rememberMe = By.cssSelector("input[id='rmflag1']");

	public static By btnidSubmit = By.cssSelector("input[id='submitBtn']");
	public static By cboidQuestion = By.cssSelector("select[id='ques']");
	public static By txtidAnswer = By.cssSelector("input[id='answer']");

	public static By txtSignInHeader = By.cssSelector("header[class='clearfix']");
	public static By brandName=By.cssSelector("section[id='globalTopBar']");
	public static By forgotlink = By.cssSelector("form[id='signInForm']");
	public static By ul=By.tagName("ul");
	public static By List1=By.tagName("li");
	public static By flink = By.tagName("a");
	public static By errormsg = By.cssSelector("aside[id='errorMessageArea']");
	public static By rdopassword = By.cssSelector("input[id='forgotPwd']");
	public static By txtId = By.cssSelector("input[id='userid']");
	public static By btnSubmit = By.cssSelector("input[class='primary submit']");
	public static By txtAnswer = By.cssSelector("input[id='challengeAns']");
	public static By btnSubmit1 = By.cssSelector("input[id='submitBtn']");
	public static By btnSubmit2 = By.cssSelector("input[id='submitBtn']");
	public static By successful = By.cssSelector("button[class='primary']");
	public static By txtPassword = By.cssSelector("input[id='newPassword']");
	public static By txtCoPassword = By.cssSelector("input[id='confirmPassword']");
	
}
