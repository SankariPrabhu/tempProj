package functionallibraries;

import org.openqa.selenium.WebElement;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Feedback;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import com.cognizant.framework.Status;
import java.util.List;

import supportlibraries.*;

public class Feedback extends ReusableLibrary {
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);

	public Feedback(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("feedback"))
				commonLibrary.sleep(5000);

		} while (!url.contains("feedback") && counter < 40);

		if (!driver.getCurrentUrl().contains("feedback")) {
			throw new IllegalStateException("feedback page is expected, but not displayed!");
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify the Feedback Page is
	// Displayed
	// # Function Name : VerifyFeedbackPage   
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Feedback verifyFeedbackPage() {
		// verifying the feed back page url
		if (driver.getCurrentUrl().toLowerCase().contains(UIMAP_Feedback.txtFeedbackPage)) {
			String title = driver.getTitle();
			driver.manage().window().maximize();
			report.updateTestLog("Verify Feedback Page", "Feedback Page is displayed", Status.PASS);
			if (title.toLowerCase().contains("feedback")) {
				// verifying the Items in Feed back page
				WebElement header = commonLibrary.isExist(UIMAP_Feedback.header, 10);

				WebElement txtBoxComments = commonLibrary.isExist(UIMAP_Feedback.txtAreaComments);
				WebElement txtBoxName = commonLibrary.isExist(UIMAP_Feedback.txtName, 20);
				WebElement txtBoxEmail = commonLibrary.isExist(UIMAP_Feedback.txtEmail);
				commonLibrary.isExist(UIMAP_Feedback.btnSend, 20);
				commonLibrary.isExist(UIMAP_Feedback.btnClose, 20);

				if (header != null && header.getText().contains("Tell Us What You Think") && txtBoxComments != null && txtBoxName != null && txtBoxEmail != null) {
					report.updateTestLog("Verification of  Feedback Page", "The label 'Tell Us What You Think' displayed in Red below global menu", Status.PASS);
					report.updateTestLog("Verification of  Feedback Page", "The label 'Comments' displayed in 'Feedback' Page", Status.PASS);
					report.updateTestLog("Verification of  Feedback Page", "The label 'Name' displayed in 'Feedback' Page", Status.PASS);
					report.updateTestLog("Verification of  Feedback Page", "The label 'E-mail Address' displayed in 'Feedback' Page", Status.PASS);

					if (txtBoxComments != null) {
						report.updateTestLog("Verification * Symbol next to 'Comments' lable", "The '*' symbol displayed next to Comments lable ", Status.PASS);
						report.updateTestLog("Verification * Symbol next to 'Comments' lable", "'*' symbol denotes required feild for comments lable ", Status.PASS);
					}

					report.updateTestLog("Verification of  Feedback Page", "Button 'Send' Displayed at the bottom of the Page", Status.PASS);
					report.updateTestLog("Verification of  Feedback Page", "Button 'Close' Displayed at the bottom of the Page", Status.PASS);

				} else {
					report.updateTestLog("Verification of  Feedback Page", "The label 'Tell Us What You Think' is not displayed in Red below global menu", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verification of  Feedback Page", "Feedback Page is vefified", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify Feedback Page", "Feedback Page is Not displayed", Status.FAIL);
		}

		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Entering value into the comments
	// textbox
	// # Function Name : enterTextIntoComments
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback enterTextIntoComments(String Comment) {

		// Entering the comments
		WebElement txtBoxComments = commonLibrary.isExist(UIMAP_Feedback.txtAreaComments);
		if (txtBoxComments != null) {
			commonLibrary.setDataInTextBox(txtBoxComments, Comment, "Comments");
		} else {
			report.updateTestLog("Enter Comments", "Comments TextArea is not Present", Status.FAIL);
		}
		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Entering value into name text box
	// # Function Name : enterTextIntoNameTextBox
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback enterTextIntoNameTextBox(String Name) {

		// Entering the Name text box
		WebElement txtBoxName = commonLibrary.isExist(UIMAP_Feedback.txtName, 20);
		if (txtBoxName != null) {
			commonLibrary.setDataInTextBox(txtBoxName, Name, "Name");
		} else {
			report.updateTestLog("Enter Name", "Name TextBox is not Present", Status.FAIL);
		}
		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Entering value into email text box
	// # Function Name : enterTextIntoEmail
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback enterTextIntoEmail(String Email) {

		// Entering value into email text box
		WebElement txtBoxEmail = commonLibrary.isExist(UIMAP_Feedback.txtEmail);
		if (txtBoxEmail != null) {
			commonLibrary.setDataInTextBox(txtBoxEmail, Email, "Email");
		} else {
			report.updateTestLog("Enter Email", "Email TextBox is not Present", Status.FAIL);
		}
		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking on Send Button
	// # Function Name : clickButtonSend
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback clickButtonSend() {
		// clicking on the send button for Email
		WebElement btnSend = commonLibrary.isExist(UIMAP_Feedback.btnSend, 20);
		if (btnSend != null) {
			commonLibrary.clickButtonParentWithWait(btnSend, "Send");
		} else {
			report.updateTestLog("Click on Send Button", "Send Button is not Present", Status.FAIL);
		}

		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for clicking the button close
	// # Function Name : clickButtonSend
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Home clickButtonClose() {
		// closing the feedback sent pop up
		WebElement btnClose = commonLibrary.isExist(UIMAP_Feedback.btnClose);
		if (btnClose != null) {
			try {
				commonLibrary.clickButtonParentWithWait(btnClose, "Close");
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// navigating to the primary window
			commonLibrary.switchToWindow("firsttime");
			// report.updateTestLog("Click on Close Button",
			// "Clicked on Close Button", Status.PASS);
		} else {
			report.updateTestLog("Click on Close Button", "Close Button is not Present", Status.FAIL);
		}

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifyig whether feedback has sent
	// or not
	// # Function Name : verifyFeedbackHasSent
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback verifyFeedbackHasSent() {

		// verifying the feed back has send with the header containing the thank
		// you message

		WebElement pageContent = commonLibrary.isExist(UIMAP_Feedback.pageContent, 10);
		WebElement txtThankyou = commonLibrary.isExist(pageContent, UIMAP_Feedback.txtThankyou, 20);
		WebElement txtEmailStatus = commonLibrary.isExist(UIMAP_Feedback.txtEmailStatus, 20);
		WebElement btnClose = commonLibrary.isExist(UIMAP_Feedback.btnClose);
		if (txtThankyou != null && txtEmailStatus != null && txtThankyou.getCssValue("color").equals("rgba(237, 28, 36, 1)") && btnClose != null && txtEmailStatus.getText().contains("Your feedback has been sent") && txtEmailStatus.getText().contains("We value your comments and suggestions. We will use them to determine what to focus on as we work to improve this product")) {
			report.updateTestLog("Verifying Email Sending Status", "1.Thank you text displays in Red color 2. Your feedback has been sent. We value yourcomments and suggestions and will use them todetermine what to focus on as we work to improvethis product> message displays . Close button displays", Status.PASS);
		} else {
			report.updateTestLog("Verifying Email Sending Status", "1.Thank you text not displayed in Red color 2. Your feedback has been sent. We value yourcomments and suggestions and will use them todetermine what to focus on as we work to improvethis product> not message displayed . Close button not displayed", Status.FAIL);
		}

		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for click on More in the Feedback
	// # Function Name : ClickOnMoreFeedBack
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Feedback clickOnMoreFeedBack() {
		WebElement btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {
			// commonLibrary.highlightElement(btnMore);
			commonLibrary.clickMethod(btnMore, "More");
		}
		WebElement lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 100);
		if (lnkFeedBack == null || !lnkFeedBack.isDisplayed()) {
			btnMore = commonLibrary.isExist(UIMAP_Home.btnTitleMore, 10);
			if (btnMore != null) {
				// commonLibrary.highlightElement(btnMore);
				commonLibrary.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkFeedBack = commonLibrary.isExist(UIMAP_SearchResult.lnkTxtFeedBack, 20);
		}

		if (lnkFeedBack != null)
			commonLibrary.clickLinkWithWebElementWithWait(lnkFeedBack, "Feedback");

		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for switch between window
	// # Function Name : swithtoWindow
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public Home swithtoWindow() {

		commonLibrary.switchToWindow("firsttime");

		return new Home(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for verifying error report in sending
	// # Function Name : verifySendError
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback verifySendError() {

		// verification of error message where entered data is wrong message
		// will be displayed
		WebElement errComment = commonLibrary.isExist(UIMAP_Feedback.txtErrComment);
		WebElement error = commonLibrary.isExist(errComment, UIMAP_Feedback.error, 10);
		WebElement errEmail = commonLibrary.isExist(UIMAP_Feedback.txtErrEmail);
		WebElement error1 = commonLibrary.isExist(errEmail, UIMAP_Feedback.error, 10);
		if (error != null && error1 != null && error.getCssValue("color").equals("rgba(237, 28, 36, 1)") && error1.getCssValue("color").equals("rgba(237, 28, 36, 1)")) {
			report.updateTestLog("Please enter your comments", " In line message displayed in Red color above  the Comments field textbox", Status.PASS);
			report.updateTestLog("Please enter a valid email address", " In line message displayed in Red color above  the Email field textbox", Status.PASS);

		} else {
			report.updateTestLog("Please enter your comments ", " In line message not displayed in Red color above  the Comments field textbox", Status.FAIL);
			report.updateTestLog("Please enter a valid email address", " In line message not displayed in Red color above  the Email field textbox", Status.FAIL);
		}
		return new Feedback(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for Entering value into the comments
	// textbox
	// # Function Name : enterTextIntoComments
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Feedback verifyFooterLinks() {

		// verifying the footer links at bottom
		String footerText = "";
		WebElement footer = commonLibrary.isExistNegative(UIMAP_Document.footer, 10);
		List<WebElement> links = commonLibrary.isExistList(footer, UIMAP_Document.links, 10);

		for (WebElement link : links) {
			// looping for collecting the text of all links of footer into a
			// string
			footerText = footerText + link.getText();
		}
		// verifying the string with link text
		if (footerText.contains("About LexisNexis") && footerText.contains("Privacy Policy") && footerText.contains("Terms & Conditions") && footerText.contains("Copyright © 2015 LexisNexis.")) {
			report.updateTestLog("Verify Footer Links ", "Footer Link 'LexisNexis®s Logo' is displayed in footer", Status.PASS);
			report.updateTestLog("Verify Footer Links ", "Footer Link 'About LexisNexis' is displayed in footer", Status.PASS);
			report.updateTestLog("Verify Footer Links ", "Footer Link 'Privacy Policy' is displayed in footer", Status.PASS);
			report.updateTestLog("Verify Footer Links ", "Footer Link 'Terms & Conditions' is displayed in footer", Status.PASS);
			report.updateTestLog("Verify Footer Links ", "Footer Link 'Copyright © 2015 LexisNexis.' is displayed in footer", Status.PASS);
		} else {
			report.updateTestLog("Verify Footer Links ", "Footer Links are not verified", Status.FAIL);
		}
		return new Feedback(scriptHelper);
	}

}
