package functionallibraries;

//import org.apache.jasper.compiler.ELNode.ELText;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.*;
import UIMAP.*;

public class SignIn extends ReusableLibrary {
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	PageCheck pageCheck = new PageCheck(scriptHelper);

	public SignIn(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("") && !url.contains("outofservice"))
				commonLibrary.sleep(5000);

		} while (!url.contains("") && !url.contains("outofservice") && counter < 40);

		if ((!driver.getCurrentUrl().contains("")) && (!driver.getCurrentUrl().contains("outofservice"))) {
			throw new IllegalStateException("SignIn page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Invoke Application url
	// # Function Name : InvokeApplication     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn invokeApplication() {
		int start = 0;
		String appURL = dataTable.getData("General_Data", "ApplicationUrl");
		System.out.println("application Url" + appURL);

		// driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(appURL);
		commonLibrary.sleep(1000);

		// Logout from the application if it's already logged in then launching
		// the url
		WebElement btnMore = commonLibrary.isExistNegative(UIMAP_Home.btnTitleMore, 5);
		if (btnMore != null) {
			commonLibrary.logout();
			try {
				commonLibrary.sleep(1000);
			} catch (Exception e) {
				System.out.println(e.toString());

			}
			driver.get(appURL);
		} else {
			driver.get(appURL);
			commonLibrary.sleep(1000);
		}
		// Error handling if the expected signin page is not displayed
		int check = pageCheck.positiveCheck(driver, "Sign In", "Sign In");
		if (check != 0) {
			pageCheck.handleFlow(driver, check);
			throw new FrameworkException("Encountered an issue while launching the URL");
		}

		// wait till the sign in page is displayed
		WebElement eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
		int count = 0;
		do {

			if (eltUserName == null) {
				commonLibrary.sleep(10000);

				eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
			}
			count++;

		} while (eltUserName == null && count < 15);

		report.updateTestLog("Invoke Application", "Invoke the application under test @ " + appURL, Status.PASS);

		if (eltUserName == null) {
			// if the signin page is not loaded again launching the URL
			if (!driver.getCurrentUrl().contains("lnaccess")) {

				driver.get(appURL);
				do {

					if (eltUserName == null) {
						commonLibrary.sleep(10000);

						eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
					}
					count++;

				} while (eltUserName == null && count < 15);

			}

			// Error handling if the expected signin page is not displayed
			if (pageCheck.positiveCheck(driver, "Remember Me", "Login page") != 0) {
				start = 1;
				report.updateTestLog("Invoke Application", "Launch App Fail", Status.FAIL);
				System.out.println("Application is Down. So quiting the whole flow");

			}
		}

		if (start == 0)
			// Maximizing the browser window
			driver.manage().window().maximize();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Invoke LA Application url
	// # Function Name : InvokeApplication_Registration     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Register invokeApplicationRegistration() {

		driver.manage().window().maximize();
		// driver.manage().timeouts().pageLoadTimeout(1,TimeUnit.MICROSECONDS);
		String appURL = dataTable.getData("General_Data", "ApplicationUrl");
		driver.get(appURL);

		report.updateTestLog("Invoke Lexis Advance Registration Page", "Invoke the application under test @ " + appURL, Status.PASS);

		commonLibrary.sleep(5000);

		return new Register(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Invoke Application url
	// # Function Name : InvokeApplication     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn invokeApplicationTax() {

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.MICROSECONDS);

		driver.manage().window().maximize();
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);

		driver.get(properties.getProperty("ApplicationTaxUrl"));
		report.updateTestLog("Invoke Lexis Advance Application", "Invoke the application under test @ " + properties.getProperty("ApplicationTaxUrl"), Status.PASS);

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home login() {

		generalFunctions.login();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignInLNAccess logininvalid() {

		generalFunctions.logininvalid();
		return new SignInLNAccess(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home login(String strUserName, String strPassword) {

		WebElement eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
		WebElement eltPassword = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword + "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='" + UIMAP_SignIn.btnIdLogin + "']"));

		if (eltUserName != null) {

			commonLibrary.setDataInTextBox(eltUserName, strUserName, "UserName");
			commonLibrary.setDataInTextBox(eltPassword, strPassword, "Password");
			commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");

			commonLibrary.sleep(10000);
			if (driver.getPageSource().contains("Login Attempt Limit Exceeded"))
				throw new FrameworkException("Error during sign in", "Login Attempt Limit Exceeded for user id " + strUserName);

			WebElement eltSearchbox = null;
			int count = 0;
			do {
				commonLibrary.sleep(20000);
				eltSearchbox = commonLibrary.isExist(UIMAP_Home.txtIdSearch, 20);
				count++;
			} while (eltSearchbox == null && count < 25);

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);

		}
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	/*
	 * public SignIn logout() {
	 * 
	 * //driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.MICROSECONDS);
	 * WebElement btnMore=commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
	 * if(btnMore!=null) { // commonLibrary.highlightElement(btnMore);
	 * if(browsername.contains("internet")) {
	 * commonLibrary.clickLink_withWebElement_WithWait_JS(btnMore, "More"); }
	 * else { commonLibrary.clickLink_withWebElement_WithWait(btnMore, "More");
	 * } } WebElement
	 * lnkSignOut=commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut),
	 * 100); if(lnkSignOut==null || !lnkSignOut.isDisplayed()) {
	 * btnMore=commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
	 * if(btnMore!=null) { //commonLibrary.highlightElement(btnMore);
	 * if((browsername.contains("internet") ))
	 * commonLibrary.click_JS(btnMore,"More"); else
	 * commonLibrary.clickLink_withWebElement_WithWait(btnMore, "More"); }
	 * lnkSignOut=commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut),
	 * 100); } if(lnkSignOut!=null) { if(browsername.contains("internet")) {
	 * commonLibrary.clickLink_withWebElement_WithWait_JS(lnkSignOut,
	 * "Sign Out"); } else {
	 * commonLibrary.clickLink_withWebElement_WithWait(lnkSignOut, "Sign Out");
	 * }
	 * 
	 * }
	 * 
	 * 
	 * //driver.manage().timeouts().implicitlyWait(220,TimeUnit.MICROSECONDS);
	 * 
	 * 
	 * WebElement btnIdLogin =
	 * commonLibrary.isExist_Negative(UIMAP_SignIn.txtSignInHeader, 10); if
	 * (btnIdLogin!=null &&
	 * driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn
	 * .txtSigninTitleMsg)) { report.updateTestLog("Verify Logout",
	 * "Sign In to Lexis Advance screen is displayed", Status.PASS); } else {
	 * report.updateTestLog("Verify Logout",
	 * "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
	 * 
	 * } return new SignIn(scriptHelper); }
	 */

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout_CleanUp     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logoutCleanUp() {

		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null)
			commonLibrary.clickMethod(btnMore, "More");

		WebElement lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				if ((browsername.contains("internet")))
					commonLibrary.clickMethod(btnMore, "More");
				else
					commonLibrary.clickMethod(btnMore, "More");
			}
			lnkSignOut = commonLibrary.isExist(By.linkText(UIMAP_Home.lnkTextSignOut), 100);
		}
		if (lnkSignOut != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		WebElement btnIdLogin = commonLibrary.isExist(By.cssSelector("a[class*='" + UIMAP_SignIn.btnClassSignIn + "']"), 40);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);

		}
		WebElement btnhrefForgetMe = commonLibrary.isExist(UIMAP_Home.btnhrefForgetMe, 100);//
		if (btnhrefForgetMe != null)
			commonLibrary.clickLinkWithWebElementWithWait(btnhrefForgetMe, "Forgot Me signIn Information");

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Invoke Google Scholar url
	// # Function Name : InvokeGoogleScholar     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn invokeGoogleScholar() {
		// driver.manage().timeouts().implicitlyWait(220,TimeUnit.SECONDS);

		driver.manage().window().maximize();
		// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		driver.get(properties.getProperty("GoogleScholarUrl"));
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application with a
	// Global UI Builder ID
	// # Function Name : Login_GUIB  
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public GUIBHome loginGUIB() {

		String strUserName = dataTable.getData("General_Data", "Username");
		String strPassword = dataTable.getData("General_Data", "Password");

		WebElement eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
		WebElement eltPassword = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdPassword + "']"), 20);
		WebElement eltSignIn = driver.findElement(By.cssSelector("input[id*='" + UIMAP_SignIn.btnIdLogin + "']"));
		WebElement eltRememberMe = driver.findElement(By.cssSelector("input[id*='" + UIMAP_SignIn.chkIdLogin + "']"));

		if (eltUserName != null) {

			commonLibrary.setDataInTextBox(eltUserName, strUserName, "UserName");
			commonLibrary.setDataInTextBox(eltPassword, strPassword, "Password");
			commonLibrary.setCheckBox(eltRememberMe, false);
			commonLibrary.clickButtonParentWithWait(eltSignIn, "SignIn");

			// driver.manage().timeouts().pageLoadTimeout(220,TimeUnit.SECONDS);

		}
		return new GUIBHome(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to document page after login
	// # Function Name : loginDoc     
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Document loginDoc() {
		generalFunctions.login();

		return new Document(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to search page after login
	// # Function Name : loginSearch     
	// # Author : Anbu
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Search loginSearch() {
		generalFunctions.login();
		return new Search(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login as another user
	// # Function Name : login_Another     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public Home loginAnother() {
		generalFunctions.login_Another();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to setting url to tyhe browser
	// # Function Name : putUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation setUrl(String Url) {
		// driver.manage().deleteAllCookies();
		driver.get(Url);
		report.updateTestLog("Set Url into Browser", "Url " + Url + " is set in to the browser", Status.PASS);

		return new Interactivecitationworkstation(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function first time login
	// # Function Name : putUrl     
	// # Author : revathi
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public BuildProfile login_newuser() {

		generalFunctions.login();
		return new BuildProfile(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login as another user
	// # Function Name : login_Another     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************
	public Home loginThird() {
		generalFunctions.loginThird();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application using
	// professor id
	// # Function Name : Login     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home loginPro1() {

		generalFunctions.loginPro1();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application using
	// professor id
	// # Function Name : Login     
	// # Author : Seetha
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home loginPro2() {

		generalFunctions.loginPro2();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home loginICW() {

		generalFunctions.loginICW();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login to the application
	// # Function Name : Login     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Home loginThree() {

		generalFunctions.loginThree();
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to Counsel Benchmarking page
	// after login
	// # Function Name : loginCB     
	// # Author : Pratik
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public CounselBenchmarking loginCB() {
		generalFunctions.login();

		return new CounselBenchmarking(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Brand Name in Sign In page
	// # Function Name : verifyBrandName     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************
	public SignIn verifyBrandName(String brandName) {
		WebElement logo = commonLibrary.isExist(UIMAP_SignIn.brandName, 10);
		WebElement logoHeader = commonLibrary.isExist(logo, By.tagName("header"), 10);
		if (logoHeader != null) {
			if (logoHeader.getText().toLowerCase().contains(brandName.toLowerCase()))
				report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is displayed in sign in page", Status.PASS);
			else
				report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is not displayed in sign in page", Status.FAIL);
		} else
			report.updateTestLog("Verify " + brandName + " displays in sign in page", brandName + " is not displayed in sign in page", Status.FAIL);
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to Shepards page after login
	// # Function Name : loginShepards     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public Shepards loginShepards() {
		generalFunctions.login();

		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login as another user
	// # Function Name : login_AnotherVsa     
	// # Author : Anbarasan
	// # Date Created : July'15
	// #*****************************************************************************************************************************
	public VSASearchResults loginAnotherVsa() {
		generalFunctions.login_Another();
		return new VSASearchResults(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to generate report after hitting perma
	// linking url
	// # Function Name : pastePermaLinkUrl     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public SignIn pastePermaLinkUrl(String url) {
		if (url != "") {
			report.updateTestLog("Verify the url '" + url + "' displays in pasted in url", "'" + url + "' is pasted in url and respective page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify the url '" + url + "' displays in pasted in url", "'" + url + "' is not pasted in url and respective page is not displayed", Status.FAIL);
		}
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to generate report after hitting perma
	// linking url
	// # Function Name : pastePermaLinkUrl     
	// # Author : Anbarasan
	// # Date Created : Jul'15
	// #*****************************************************************************************************************************

	public SignIn verifySignOut() {

		// Verification Point : sign in page is displayed
		WebElement btnIdLogin = commonLibrary.isExistNegative(UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null && driver.getCurrentUrl().toLowerCase().contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout", "Sign In to Lexis Advance screen is NOT displayed", Status.WARNING);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Invoke Application url
	// # Function Name : invokeApplicationURLApi     
	// # Author : Jeeva
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public SignIn invokeApplicationURLApi(String appURL) {
		int start = 0;
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		commonLibrary.sleep(12000);

		// Logout from the application if it's already logged in then launching
		// the url
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 5);
		if (btnMore != null) {
			commonLibrary.logout();
			try {
				commonLibrary.sleep(1000);
			} catch (Exception e) {
				System.out.println(e.toString());

			}
			driver.get(appURL);
		} else {
			driver.get(appURL);
			commonLibrary.sleep(10000);
		}
		// Error handling if the expected signin page is not displayed
		int check = pageCheck.positiveCheck(driver, "Sign In", "Sign In");
		if (check != 0) {
			pageCheck.handleFlow(driver, check);
			throw new FrameworkException("Encountered an issue while launching the URL");
		}

		// wait till the sign in page is displayed
		WebElement eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
		int count = 0;
		do {

			if (eltUserName == null) {
				commonLibrary.sleep(10000);

				eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
			}
			count++;

		} while (eltUserName == null && count < 15);

		report.updateTestLog("Invoke Application", "Invoke the application under test @ " + appURL, Status.PASS);

		if (eltUserName == null) {
			// if the signin page is not loaded again launching the URL
			if (!driver.getCurrentUrl().contains("lnaccess")) {

				driver.get(appURL);
				do {

					if (eltUserName == null) {
						commonLibrary.sleep(10000);

						eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
					}
					count++;

				} while (eltUserName == null && count < 15);

			}

			// Error handling if the expected signin page is not displayed
			if (pageCheck.positiveCheck(driver, "Remember Me", "Login page") != 0) {
				start = 1;
				report.updateTestLog("Invoke Application", "Launch App Fail", Status.FAIL);
				System.out.println("Application is Down. So quiting the whole flow");

			}
		}

		if (start == 0)
			// Maximizing the browser window
			driver.manage().window().maximize();

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to launch bookmarked URL
	// # Function Name : launchBMURL     
	// # Author : Jeeva
	// # Date Created : Oct'15
	// #*****************************************************************************************************************************

	public SignIn launchBMURL(String bmURL) {

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		commonLibrary.sleep(12000);
		if (bmURL != null)
			driver.get(bmURL);

		report.updateTestLog("Paste URL in the browser", "URL '" + bmURL + "' is pasted in the browser", Status.DONE);

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to setting url to tyhe browser
	// # Function Name : putUrl     
	// # Author : Baswaraj
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Home setUrl1(String Url) {
		// driver.manage().deleteAllCookies();
		driver.get(Url);
		report.updateTestLog("Set Url into Browser", "Url " + Url + " is set in to the browser", Status.PASS);
		commonLibrary.sleep(500000);
		return new Home(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to History page after login
	// # Function Name : loginHistory     
	// # Author : Jeeva
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public History loginHistory() {
		generalFunctions.login();
		return new History(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Absence of PR in product switcher
	// # Function Name :      
	// # Author : Dinesh Krishnamoorthy
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Publicrecords absenceofPR() {
		WebElement btnIdLexisAdvance = commonLibrary.isExist(UIMAP_Publicrecords.btnIdLexisAdvance, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnIdLexisAdvance, "Lexis Advance Arrow");
		else
			commonLibrary.clickButtonParentWithWait(btnIdLexisAdvance, "Lexis Advance Arrow");

		WebElement btnActionCunselBenchmarking = commonLibrary.isExist(UIMAP_Publicrecords.btnPublicRecords, 20);
		if (browsername.contains("internet"))
			commonLibrary.clickJS(btnActionCunselBenchmarking, "Public Records");
		else
			commonLibrary.clickLinkWithWebElement(btnActionCunselBenchmarking, "Public Records");

		WebElement CurrentProduct = commonLibrary.isExist(UIMAP_Publicrecords.CurrentProduct, 20);
		if (CurrentProduct.getText().toLowerCase().contains("public records")) {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Public Records page is displayed", "Public Records page is not displayed", Status.FAIL);
		}
		WebElement logo = commonLibrary.isExist(UIMAP_Publicrecords.btnPublicRecords, 20);
		if (logo != null) {
			report.updateTestLog("Verify Lexis Advance® Public Records logo displays in the Top left corner of Global menu", "Lexis Advance® Public Records logo is not displayed in the Top left corner of Global menu", Status.FAIL);
		} else {

			report.updateTestLog("Verify Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", "Lexis Advance®  Public Records logo displays in the Top left corner of Global menu", Status.PASS);
		}

		return new Publicrecords(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to move to Alerts page after login
	// # Function Name : loginAlerts     
	// # Author : Nancy
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public Alerts loginAlerts() {
		generalFunctions.login();

		return new Alerts(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set new password
	// # Function Name : setNewPassword
	// # Author : Banu
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************

	public SignInLNAccess setNewPassword(String strId, String strAnswer, String strPassword) {
		generalFunctions.setNewPassword(strId, strAnswer, strPassword);
		return new SignInLNAccess(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to login as PublicRecords
	// # Function Name : login_Another     
	// # Author : Aravind Russell V
	// # Date Created : Dec'15
	// #*****************************************************************************************************************************
	public Publicrecords loginPublicRecords() {
		generalFunctions.login();
		return new Publicrecords(scriptHelper);

	}
	// #*****************************************************************************************************************************
		// # Function Description : Function to Invoke Application url
		// # Function Name : invokeApplicationURLApiLPA    
		// # Author : Deepha H 
		// # Date Created : Sep'15
		// #*****************************************************************************************************************************


		public LPAHome invokeApplicationURLApiLPA(String appURL) {
			int start = 0;
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();

			commonLibrary.sleep(12000);

			// Logout from the application if it's already logged in then launching
			// the url
			WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 5);
			if (btnMore != null) {
				commonLibrary.logout();
				try {
					commonLibrary.sleep(1000);
				} catch (Exception e) {
					System.out.println(e.toString());

				}
				driver.get(appURL);
			} else {
				driver.get(appURL);
				commonLibrary.sleep(10000);
			}
			// Error handling if the expected signin page is not displayed
			int check = pageCheck.positiveCheck(driver, "Sign In", "Sign In");
			if (check != 0) {
				pageCheck.handleFlow(driver, check);
				throw new FrameworkException("Encountered an issue while launching the URL");
			}

			// wait till the sign in page is displayed
			WebElement eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
			int count = 0;
			do {

				if (eltUserName == null) {
					commonLibrary.sleep(10000);

					eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
				}
				count++;

			} while (eltUserName == null && count < 15);

			report.updateTestLog("Invoke Application", "Invoke the application under test @ " + appURL, Status.PASS);

			if (eltUserName == null) {
				// if the signin page is not loaded again launching the URL
				if (!driver.getCurrentUrl().contains("lnaccess")) {

					driver.get(appURL);
					do {

						if (eltUserName == null) {
							commonLibrary.sleep(10000);

							eltUserName = commonLibrary.isExist(By.cssSelector("input[id*='" + UIMAP_SignIn.txtIdUsername + "']"), 20);
						}
						count++;

					} while (eltUserName == null && count < 15);

				}

				// Error handling if the expected signin page is not displayed
				if (pageCheck.positiveCheck(driver, "Remember Me", "Login page") != 0) {
					start = 1;
					report.updateTestLog("Invoke Application", "Launch App Fail", Status.FAIL);
					System.out.println("Application is Down. So quiting the whole flow");

				}
			}

			if (start == 0)
				// Maximizing the browser window
				driver.manage().window().maximize();

			return new LPAHome(scriptHelper);
		}


	// #*****************************************************************************************************************************
	// # Function Description : Function to login as Shepards
	// # Function Name : loginshep     
	// # Author : Vennila
	// # Date Created : may'15
	// #*****************************************************************************************************************************

	public Shepards loginshep() {
		generalFunctions.login();
		return new Shepards(scriptHelper);
	}

}
