package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.util.List;

import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_Settings;
//import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_Interactivecitationworkstation;
//import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
//import com.cognizant.framework.Status;
//import com.itextpdf.text.List;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ICWExcercise extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);

	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private GeneralFunctions generalFunctions = new GeneralFunctions(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public ICWExcercise(ScriptHelper scriptHelper) {

		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("icwcitationexercise"))
				commonLibrary.sleep(5000);

		} while (!url.contains("icwcitationexercise") && counter < 40);

		if (!driver.getCurrentUrl().contains("icwcitationexercise")) {
			throw new IllegalStateException("ICW Excercise page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to view reports
	// # Function Name : viewreport
	// # Author : Uma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWCertificate viewreport() {
		WebElement viewobj = commonLibrary.isExist(By.cssSelector("a[data-action='studentcertificate']"));
		if (viewobj != null) {

			if (browsername.contains("internet")) {
				commonLibrary.clickLinkWithWebElementWithWait(viewobj, viewobj.getText());

			} else {
				commonLibrary.clickLinkWithWebElementWithWait(viewobj, viewobj.getText());

			}
		}

		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the submit answer button in
	// icwcitationexercise page
	// # Function Name : ICWSubmitAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickVerifyICWSubmitAnswer(String problemNo, boolean click, boolean presence) {
		String submitid = "problem-" + problemNo + "-textarea-submit";
		WebElement submit = commonLibrary.isExist(By.id(submitid), 10);
		if (submit != null) {
			if (click) {
				commonLibrary.clickButtonParentWithWait(submit, "Submit Answer");
				try {
					WebElement submitNew = commonLibrary.isExistNegative(By.id(submitid), 3);
					int count = 0;
					do {
						commonLibrary.sleep(30000);
						count++;
						submitNew = commonLibrary.isExistNegative(By.id(submitid), 3);
					} while (submit.equals(submitNew) && count < 40);
				} catch (Exception e) {
					commonLibrary.sleep(500000);
					System.out.println(e.toString());
				}
			} else {
				if (presence)
					report.updateTestLog("verify if Submit Answer button is displayed", "Submit Answer button is displayed", Status.PASS);
			}

		} else if (!presence)
			report.updateTestLog("verify if Submit Answer button is NOT displayed", "Submit Answer button is NOT displayed", Status.PASS);
		else
			report.updateTestLog("verify if Submit Answer button is displayed", "Submit Answer button is NOT displayed", Status.FAIL);

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter the answer in
	// icwcitationexercise page
	// # Function Name : setICWAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise setICWAnswer(String problemNo, String answer) {
		String id = "problem-" + problemNo + "-textarea_ifr";
		driver.switchTo().frame(driver.findElement(By.id(id)));
		WebElement textBox = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.textBox, 10);
		if (textBox != null)
			commonLibrary.setDataInTextBox(textBox, answer, "Answer text box");
		else
			report.updateTestLog("verify if Answer box is displayed", "Answer box is NOT displayed", Status.FAIL);

		driver.switchTo().defaultContent();
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter the answer in
	// icwcitationexercise page
	// # Function Name : setICWAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public String getICWAnswer(String problemNo) {
		String answer = "";
		String id = "problem-" + problemNo + "-textarea_ifr";
		driver.switchTo().frame(driver.findElement(By.id(id)));
		WebElement textBox = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.textBox, 10);
		answer = textBox.getText();
		driver.switchTo().defaultContent();
		return answer;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the given problem no in
	// icwcitationexercise page
	// # Function Name : selectProblem
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise selectProblem(String problemNo) {
		List<WebElement> rows = commonLibrary.isExistList(UIMAP_Interactivecitationworkstation.problemTab, 10);
		for (WebElement li : rows) {
			WebElement probNo = commonLibrary.isExist(li, UIMAP_Interactivecitationworkstation.problemNo, 10);
			if (probNo.getText().toString().equalsIgnoreCase(problemNo)) {
				WebElement button = commonLibrary.isExist(li, UIMAP_Interactivecitationworkstation.buttons, 10);
				commonLibrary.clickJS(button, problemNo);
				break;
			}
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get Answer
	// # Function Name : getAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public String getAnswer(String problemNo) {
		String answer = "";
		String probName = "";
		String correctAns = "";
		String italiText = "";
		try {
			WebElement banner = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.home, 10);
			WebElement header = commonLibrary.isExist(banner, UIMAP_Interactivecitationworkstation.headerName, 10);
			probName = header.getText();

			// select problem
			this.selectProblem(problemNo);

			// set wrong answers
			for (int i = 1; i <= 3; i++) {
				this.setICWAnswer(problemNo, "Answer" + i);
				this.clickVerifyICWSubmitAnswer(problemNo, true, true);
			}

			WebElement attFB = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
			WebElement corrAns = commonLibrary.isExist(attFB, UIMAP_Interactivecitationworkstation.correctAnswer, 10);
			correctAns = corrAns.getText();
			List<WebElement> italics = commonLibrary.isExistList(corrAns, UIMAP_Interactivecitationworkstation.italics, 10);
			for (WebElement ital : italics) {
				italiText = italiText + " " + ital.getText();
			}

			answer = probName + "###" + correctAns + "###" + italiText + " ";
			;

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return answer;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to complete Exercise
	// # Function Name : completeExercise
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWCertificate completeExercise() {
		WebElement compEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.completeExercise, 10);
		if (compEx != null) {

			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWait(compEx, "Complete Excercise");
			else
				commonLibrary.clickLinkWithWebElementWithWait(compEx, "Complete Excercise");

		}

		WebElement conCompEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmCompleteExer, 10);
		if (conCompEx != null) {
			report.updateTestLog("Verify if Complete Exercise pop up displays withInline message, Instructor details,'Complete Exercise' and 'Cancel' buttons.", "Complete Exercise pop up displays", Status.PASS);
			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWait(conCompEx, "Confirm Complete Excercise");
			else
				commonLibrary.clickLinkWithWebElementWithWait(conCompEx, "Confirm Complete Excercise");

		}
		try {
			WebElement certPage = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.certPage, 3);
			int count = 0;
			do {
				commonLibrary.sleep(50000);
				count++;
				certPage = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.certPage, 3);
			} while (certPage == null && count < 20);
		} catch (Exception e) {
			commonLibrary.sleep(500000);
			System.out.println(e.toString());
		}
		new PageCheck(scriptHelper).ajaxWait(driver);
		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the content on entiring the
	// wrong answer
	// # Function Name : verifyICW_WrongAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyICWWrongAnswer(String problemNo, String answer, int attempt, String last) {
		boolean color = false;
		String text = "";
		String message = "You have used the maximum number of attempts allowed.";
		// verify X in answer check box
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);

		if (ans.getAttribute("class").equalsIgnoreCase("attempt failed") || ans.getAttribute("class").equalsIgnoreCase("attempt incorrect"))
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is displayed", Status.PASS);
		else
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is NOT displayed", Status.FAIL);

		// Verify incorrect answer and font color
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement attempts = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.incorrAttempt, 10);
		List<WebElement> error = commonLibrary.isExistList(attempts, UIMAP_Interactivecitationworkstation.incorrAns1, 10);
		List<WebElement> errorIns = commonLibrary.isExistList(attempts, UIMAP_Interactivecitationworkstation.incorrAns, 10);
		if (error.size() == attempt) {
			report.updateTestLog("verify if Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", "Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", Status.PASS);
			for (WebElement er : errorIns) {

				if (er.getCssValue("color").equalsIgnoreCase("rgba(237, 28, 36, 1)") && er.getCssValue("background-color").equalsIgnoreCase("rgba(255, 242, 197, 1)"))
					color = true;
				else {
					color = false;
					break;
				}

			}
			if (color)
				report.updateTestLog("verify if Errors are highlighted in yellow with red font color", "Errors are highlighted in yellow with red font color", Status.PASS);
			else
				report.updateTestLog("verify if Errors are highlighted in yellow with red font color", "Errors are NOT highlighted in yellow with red font color", Status.FAIL);

		} else
			report.updateTestLog("verify if Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", "Incorrect answer doesnot displays with errors higlighted in 'Incorrect Citation' label", Status.FAIL);

		// Verify hints
		WebElement hints = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.hints, 10);
		if (hints != null)
			report.updateTestLog("verify if Hint displays for the incorrect answer", "Hint displays for the incorrect answer", Status.PASS);
		else
			report.updateTestLog("verify if Hint displays for the incorrect answer", "Hint doesnot displays for the incorrect answer", Status.FAIL);

		// verify answer persist in textbox
		if (last.equalsIgnoreCase("false")) {
			text = this.getICWAnswer(problemNo);
			if (text.equalsIgnoreCase(answer))
				report.updateTestLog("verify if Incorrect answer text deos not disappear from the answer box", "Incorrect answer text deos not disappear from the answer box", Status.PASS);
			else
				report.updateTestLog("verify if Incorrect answer text deos not disappear from the answer box", "Incorrect answer text disappear from the answer box", Status.FAIL);
		}

		if (last.equalsIgnoreCase("true")) {
			WebElement section1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
			if (section1.getText().contains(message))
				report.updateTestLog("verify if the message " + message + " displays", "The message " + message + " displays", Status.PASS);
			else
				report.updateTestLog("verify if the message " + message + " displays", "The message " + message + " does not displays", Status.FAIL);

			WebElement corrAns = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.correctAnswer, 10);
			if (corrAns != null)
				report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.PASS);
			else
				report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.FAIL);

			WebElement attempts1 = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.incorrAttempt, 10);
			List<WebElement> error1 = commonLibrary.isExistList(attempts1, UIMAP_Interactivecitationworkstation.p, 10);
			if (error1.size() == 3)
				report.updateTestLog("verify if All three incorrect attempts display in order", "All three incorrect attempts display in order", Status.PASS);
			else
				report.updateTestLog("verify if All three incorrect attempts display in order", "All three incorrect attempts display in order", Status.FAIL);

			this.clickVerifyICWSubmitAnswer(problemNo, false, false);
		}

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the next button to navigate to
	// the next question
	// # Function Name : clickNextQuestion
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickNextQuestion() {
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement nextButton = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.nextButton, 10);
		if (nextButton != null) {
			commonLibrary.clickJS(nextButton, nextButton.getText());
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click the next button to navigate to
	// the next question
	// # Function Name : clickNextQuestion
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyICWCorrectAnswer(int attempt, String problemNo) {
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);
		if (ans.getAttribute("class").equalsIgnoreCase("attempt correct"))
			report.updateTestLog("verify if a 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", "A 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", Status.PASS);
		else
			report.updateTestLog("verify if a 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", "A 'tick' symbol doesnot displays in the third 'answer check box' for the current problem in the list of problems.", Status.FAIL);

		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement msg = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.correctMsg, 10);
		if (msg != null)
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is displayed.", Status.PASS);
		else
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is NOT displayed.", Status.FAIL);

		WebElement section1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement corrAns = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.correctAnswer, 10);
		if (corrAns != null)
			report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.PASS);
		else
			report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.FAIL);

		WebElement attempts1 = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.incorrAttempt, 10);
		List<WebElement> error1 = commonLibrary.isExistList(attempts1, UIMAP_Interactivecitationworkstation.p, 10);
		if (error1.size() >= 1)
			report.updateTestLog("verify if all incorrect attempts display in order", "All incorrect attempts display in order", Status.PASS);
		else
			report.updateTestLog("verify if All incorrect attempts display in order", "All incorrect attempts display in order", Status.FAIL);

		this.clickVerifyICWSubmitAnswer(problemNo, false, false);
		WebElement section2 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		List<WebElement> textBox = commonLibrary.isExistList(section2, By.tagName("iframe"), 10);
		if (textBox.size() == 0)
			report.updateTestLog("verify if 'Answer box' does not display", "'Answer box' does not display", Status.PASS);
		else
			report.updateTestLog("verify if 'Answer box' does not display", "'Answer box' display", Status.FAIL);

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to enter the answer in
	// icwcitationexercise page
	// # Function Name : setICWCorrectAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise setICWCorrectAnswer(String problemNo, String answer, String italic, boolean set, boolean presence) {
		try {

			// int l=10;
			// if
			// (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)==true)
			// Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK,
			// false);
			String id = "problem-" + problemNo + "-textarea_ifr";
			driver.switchTo().frame(driver.findElement(By.id(id)));
			WebElement textBox = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.textBox, 10);
			if (textBox != null) {
				if (set) {
					commonLibrary.setDataInTextBox(textBox, answer, "Answer text box");
					// textBox.sendKeys(Keys.HOME);
					commonLibrary.sleep(1000);

					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_HOME);

					robot.delay(4000);
					// if
					// (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK))
					// Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK,
					// false);
					for (int i = 0; i < italic.length(); i++) {
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.VK_RIGHT);
						robot.delay(1000);
						robot.keyRelease(KeyEvent.VK_RIGHT);
						robot.keyRelease(KeyEvent.VK_SHIFT);

						// commonLibrary.sleep(1000);
						// textBox.sendKeys(Keys.SHIFT);
						// textBox.sendKeys(Keys.ARROW_RIGHT);
					}

					// robot.keyRelease(KeyEvent.VK_SHIFT);

				} else {
					if (presence)
						report.updateTestLog("verify if Answer box is displayed", "Answer box is displayed", Status.PASS);
				}
			} else if (!presence)
				report.updateTestLog("verify if Answer box is NOT displayed", "Answer box is NOT displayed", Status.PASS);
			else
				report.updateTestLog("verify if Answer box is displayed", "Answer box is NOT displayed", Status.FAIL);

			driver.switchTo().defaultContent();
			WebElement section1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
			WebElement corrAns = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.italicsButton, 10);
			commonLibrary.clickJSNoLog(corrAns);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the problem currently
	// displayed
	// # Function Name : verifyProblem
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyProblem(String problemNo) {
		WebElement selectedRows = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.selectedProblemTab, 10);
		WebElement span = commonLibrary.isExistNegative(selectedRows, UIMAP_Home.spantag, 10);
		if (span.getText().toString().equalsIgnoreCase(problemNo)) {
			report.updateTestLog("Verify if Question Number " + problemNo + " is displayed.", "Question Number " + problemNo + " is displayed.", Status.PASS);
		} else
			report.updateTestLog("Verify if Question Number " + problemNo + " is displayed.", "Question Number " + problemNo + " is not displayed.", Status.FAIL);

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to complete Exercise and verify Pop-up
	// # Function Name : completeExerciseVerifyPopup
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWCertificate completeExerciseVerifyPopup(String para1, String para2, String para3) {
		WebElement compEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.completeExercise, 10);
		if (compEx != null) {

			if (browsername.contains("internet"))
				commonLibrary.clickLinkWithWebElementWithWait(compEx, compEx.getText());
			else
				commonLibrary.clickLinkWithWebElementWithWait(compEx, compEx.getText());

		}

		WebElement completeExerDialogContent = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.completeExerDialogContent, 10);
		List<WebElement> links = commonLibrary.isExistList(completeExerDialogContent, UIMAP_Interactivecitationworkstation.lnkLinks, 10);

		if (completeExerDialogContent.getText().toLowerCase().contains(para1.toLowerCase())) {
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para1, "Complete Exercise dialog contains " + para1, Status.PASS);
		} else
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para1, "Complete Exercise dialog does not contains " + para1, Status.FAIL);

		if (completeExerDialogContent.getText().toLowerCase().contains(para2.toLowerCase())) {
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para2, "Complete Exercise dialog contains " + para2, Status.PASS);
		} else
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para2, "Complete Exercise dialog does not contains " + para2, Status.FAIL);

		if (completeExerDialogContent.getText().toLowerCase().contains(para3.toLowerCase()) && links.size() >= 1) {
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para3 + " along with professor details.", "Complete Exercise dialog contains " + para3 + " along with professor details.", Status.PASS);
		} else
			report.updateTestLog("Verify if Complete Exercise dialog contains " + para3 + " along with professor details.", "Complete Exercise dialog does not contains " + para3 + " along with professor details.", Status.FAIL);

		WebElement conCompEx = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.comfirmCompleteExer, 10);
		if (conCompEx != null) {
			report.updateTestLog("Verify if Complete Exercise pop up displays withInline message, Instructor details,'Complete Exercise' and 'Cancel' buttons.", "Complete Exercise pop up displays", Status.PASS);
			if (browsername.contains("internet"))
				commonLibrary.clickJS(conCompEx, conCompEx.getText());
			else
				commonLibrary.clickLinkWithWebElementWithWait(conCompEx, conCompEx.getText());

		}

		return new ICWCertificate(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the wrong answer mark appears
	// once wrong answer is submitted
	// # Function Name : verifyICWWrongAnswerMarkOnly
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyICWWrongAnswerMarkOnly(String problemNo, int attempt) {

		// verify X in answer check box
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);

		if (ans.getAttribute("class").equalsIgnoreCase("attempt failed") || ans.getAttribute("class").equalsIgnoreCase("attempt incorrect"))
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is displayed", Status.PASS);
		else
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is NOT displayed", Status.FAIL);

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Request Reset
	// # Function Name : clickRequestReset
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWExcercise clickRequestReset() {
		WebElement requestReset = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.requestReset, 10);
		commonLibrary.clickLinkWithWebElementWithWait(requestReset, requestReset.getText());
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Submit Request in Dialog Box
	// # Function Name : verifyRequestReset
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************

	public ICWExcercise clickSubmitRequest() {
		WebElement submitRequest = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.submitRequest, 10);
		commonLibrary.clickLinkWithWebElementWithWait(submitRequest, submitRequest.getAttribute("value"));
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to take ScreenShot for manual
	// verification.
	// # Function Name : takeScreenShot
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise takeScreenShot(String strTCName, String strStep) {
		generalFunctions.takeScreenShot(strTCName, strStep);
		return new ICWExcercise(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on product logo
	// # Function Name : clickProductLogo
	// # Author : Pratik
	// # Date Created : June'2015
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation clickProductlogo() {
		generalFunctions.clickProductLogo();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the highlights on entering
	// the wrong answer.
	// # Function Name : verifyICW_WrongAnswer
	// # Author : Veeshma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyICWWrongAnswerHiglight(String problemNo, int attempt) {
		boolean color = false;
		// verify X in answer check box
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);

		if (ans.getAttribute("class").equalsIgnoreCase("attempt failed") || ans.getAttribute("class").equalsIgnoreCase("attempt incorrect"))
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is displayed", Status.PASS);
		else
			report.updateTestLog("verify if a 'x' symbol displays in the first 'answer check box' for the current problem in the List of problems", "'x' symbol is NOT displayed", Status.FAIL);

		// Verify incorrect answer and font color
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement attempts = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.incorrAttempt, 10);
		List<WebElement> error = commonLibrary.isExistList(attempts, UIMAP_Interactivecitationworkstation.incorrAns1, 10);
		List<WebElement> errorIns = commonLibrary.isExistList(attempts, UIMAP_Interactivecitationworkstation.incorrAns, 10);
		if (error.size() == attempt) {
			report.updateTestLog("verify if Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", "Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", Status.PASS);
			for (WebElement er : errorIns) {
				if (er.getCssValue("color").equalsIgnoreCase("rgba(237, 28, 36, 1)") && er.getCssValue("background-color").equalsIgnoreCase("rgba(255, 242, 197, 1)"))
					color = true;
				else {
					color = false;
					break;
				}

			}
			if (color)
				report.updateTestLog("verify if Errors are highlighted in yellow with red font color", "Errors are highlighted in yellow with red font color", Status.PASS);
			else
				report.updateTestLog("verify if Errors are highlighted in yellow with red font color", "Errors are NOT highlighted in yellow with red font color", Status.FAIL);

		} else
			report.updateTestLog("verify if Incorrect answer displays with errors higlighted in 'Incorrect Citation' label", "Incorrect answer doesnot displays with errors higlighted in 'Incorrect Citation' label", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to log out of the application
	// # Function Name : logout
	// # Author : Uma
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public SignIn logout() {
		commonLibrary.logout();
		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Edit Settings
	// # Function Name : clickEditSettings   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public LASettings clickEditSettings() {
		WebElement editExercise = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.editExercise, 10);
		if (editExercise != null)
			commonLibrary.clickLinkWithWebElementWithWait(editExercise, editExercise.getText());

		commonLibrary.sleep(5000);

		return new LASettings(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify faculty
	// # Function Name : verifyfaculty   
	// # Author : Aravind
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyFaculty(String usernameToVerify) {
		WebElement verifyFacultyParent = commonLibrary.isExist(UIMAP_Settings.verifyFacultyParent, 10);
		if (verifyFacultyParent != null) {
			WebElement verifyFaculty = commonLibrary.isExist(verifyFacultyParent, UIMAP_Settings.verifyFaculty, 10);
			if (verifyFaculty.getText().toLowerCase().contains(usernameToVerify.toLowerCase()))
				report.updateTestLog("verify if faculty has been changed.", "Faculty changed.", Status.PASS);
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify number of attempts
	// # Function Name : verifyAttempts   
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyAttempts(String number) {
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		List<WebElement> attempts = commonLibrary.isExistList(table, UIMAP_Interactivecitationworkstation.cells, 10);
		if ((attempts.size() + "").equals(number))
			report.updateTestLog("Verify if Progress Bar with " + number + " check boxes each displays on the left.", "Progress Bar with " + number + " check boxes each displays on the left.", Status.PASS);
		else
			report.updateTestLog("Verify if Progress Bar with " + number + " check boxes each displays on the left.", "Progress Bar with " + number + " check boxes each does not display on the left.", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Correct Answer
	// the next question
	// # Function Name : verifyCorrectAnswer
	// # Author : Aravind
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyCorrectAnswer(int attempt, String problemNo) {
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);
		if (ans.getAttribute("class").equalsIgnoreCase("attempt correct"))
			report.updateTestLog("verify if a symbol displays in the answer check box for the current problem in the list of problems.", "A symbol displayed in the answer check box for the current problem in the list of problems.", Status.PASS);
		else
			report.updateTestLog("verify if a symbol displays in the answer check box for the current problem in the list of problems.", "A symbol is not displayed in the answer check box for the current problem in the list of problems.", Status.FAIL);

		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement msg = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.correctMsg, 10);
		if (msg != null)
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is displayed.", Status.PASS);
		else
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is NOT displayed.", Status.FAIL);

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to the instructions tab
	// # Function Name : verifyAttempts   
	// # Author : Pratik
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyInstructionsTab() {
		WebElement instructionsTab = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.instructionsTab, 10);
		if (instructionsTab != null && !instructionsTab.getText().equals(""))
			report.updateTestLog("Verify if instruction tab with instructions and overview displays.", "instruction tab with instructions and overview displays.", Status.PASS);
		else
			report.updateTestLog("Verify if instruction tab with instructions and overview displays.", "instruction tab with instructions and overview does not display.", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify tick, Correct message and
	// Correct Citation
	// # Function Name : partVerifyICWCorrectAnswer
	// # Author : Pratik
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise partVerifyICWCorrectAnswer(int attempt) {
		WebElement question = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.questionSelected, 10);
		WebElement table = commonLibrary.isExist(question, UIMAP_Interactivecitationworkstation.attemptTable, 10);
		WebElement ans = commonLibrary.isExist(table, By.xpath("td[" + attempt + "]"), 10);
		if (ans.getAttribute("class").equalsIgnoreCase("attempt correct"))
			report.updateTestLog("verify if a 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", "A 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", Status.PASS);
		else
			report.updateTestLog("verify if a 'tick' symbol displays in the third 'answer check box' for the current problem in the list of problems.", "A 'tick' symbol doesnot displays in the third 'answer check box' for the current problem in the list of problems.", Status.FAIL);

		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement msg = commonLibrary.isExist(section, UIMAP_Interactivecitationworkstation.correctMsg, 10);
		if (msg != null)
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is displayed.", Status.PASS);
		else
			report.updateTestLog("verify if Correct! message is displayed.", "Correct! message is NOT displayed.", Status.FAIL);

		WebElement section1 = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement corrAns = commonLibrary.isExist(section1, UIMAP_Interactivecitationworkstation.correctAnswer, 10);
		if (corrAns != null)
			report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.PASS);
		else
			report.updateTestLog("verify if the Correct Citation answer displays", "The Correct Citation answer displays", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Download Icon and Choose the
	// type of settings
	// # Function Name : clickDownloadIconAndChooseSettings
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickDownloadIconAndChooseSettings(String typeOfSettings) {
		WebElement DeliveryIcon = commonLibrary.isExist(UIMAP_Document.lnkDeliveryDownload, 20);
		if (browsername.equalsIgnoreCase("internet explorer"))
			commonLibrary.clickJS(DeliveryIcon, "Delivery Icon");
		else
			commonLibrary.clickLink(DeliveryIcon, "Delivery Icon");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeliveryIcon = commonLibrary.isExist(UIMAP_Document.lnkDeliveryDownload, 20);
		WebElement deliveryMenu = commonLibrary.isExist(commonLibrary.getParentElement(DeliveryIcon), UIMAP_Interactivecitationworkstation.deliveryMenu, 10);
		if (deliveryMenu != null && deliveryMenu.isDisplayed()) {
			WebElement chooseNewSettings = commonLibrary.isExist(deliveryMenu, UIMAP_Interactivecitationworkstation.chooseNewSettingsDownload, 10);
			WebElement useCurrentSettings = commonLibrary.isExist(deliveryMenu, UIMAP_Interactivecitationworkstation.useCurrentSettingsDownload, 10);
			if (chooseNewSettings != null && useCurrentSettings != null) {
				if (typeOfSettings.toLowerCase().equals("choose new settings"))
					commonLibrary.clickButtonParentWithWait(chooseNewSettings, "Choose New Settings");
				if (typeOfSettings.toLowerCase().equals("use current settings"))
					commonLibrary.clickButtonParentWithWait(useCurrentSettings, "Use Current Settings");
			} else {
				report.updateTestLog("Click on type of settings", "Types of settings are not available", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify download menu is present", "Download menu is not present", Status.FAIL);
		}

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Print Icon and Choose the type
	// of settings
	// # Function Name : clickPrintIconAndChooseSettings
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickPrintIconAndChooseSettings(String typeOfSettings) {
		WebElement PrintIcon = commonLibrary.isExist(UIMAP_Document.lnkPrint, 20);
		if (browsername.equalsIgnoreCase("internet explorer"))
			commonLibrary.clickJS(PrintIcon, "Print Icon");
		else
			commonLibrary.clickLink(PrintIcon, "Print Icon");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintIcon = commonLibrary.isExist(UIMAP_Document.lnkPrint, 20);
		WebElement deliveryMenu = commonLibrary.isExist(commonLibrary.getParentElement(PrintIcon), UIMAP_Interactivecitationworkstation.deliveryMenu, 10);
		if (deliveryMenu != null && deliveryMenu.isDisplayed()) {
			WebElement chooseNewSettings = commonLibrary.isExist(deliveryMenu, UIMAP_Interactivecitationworkstation.chooseNewSettingsPrint, 10);
			WebElement useCurrentSettings = commonLibrary.isExist(deliveryMenu, UIMAP_Interactivecitationworkstation.useCurrentSettingsPrint, 10);
			if (chooseNewSettings != null && useCurrentSettings != null) {
				if (typeOfSettings.toLowerCase().equals("choose new settings"))
					commonLibrary.clickButtonParentWithWait(chooseNewSettings, "Choose New Settings");
				if (typeOfSettings.toLowerCase().equals("use current settings"))
					commonLibrary.clickButtonParentWithWait(useCurrentSettings, "Use Current Settings");
			} else {
				report.updateTestLog("Click on type of settings", "Types of settings are not available", Status.FAIL);
			}

		} else {
			report.updateTestLog("Verify Print menu is present", "Print menu is not present", Status.FAIL);
		}

		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify if Option to select the cover
	// page is not displayed
	// # Function Name : verifyCoverPageCheckBoxPresence
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifyCoverPageCheckBoxPresence() {
		WebElement coverPage = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.coverPage, 20);
		if (coverPage == null) {
			report.updateTestLog("Verify if Option to select the cover page is not displayed", "Option to select the cover page is not displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify if Option to select the cover page is not displayed", "Option to select the cover page is displayed", Status.FAIL);
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click download and verify message
	// # Function Name : clickDownloadAndVerifyMessage
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise clickDownloadAndVerifyMessage() {
		WebElement download = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.download, 20);
		if (download != null) {
			if (browsername.equalsIgnoreCase("internet explorer"))
				commonLibrary.clickJS(download);
			else
				commonLibrary.clickButton(download);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			commonLibrary.switchToWindow("deliverysecondarywindow");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WebElement deliveryComplete = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.deliveryComplete, 20);
			if (deliveryComplete != null) {
				report.updateTestLog("Verify secondary is opened", "Secondary window is opened", Status.PASS);
				WebElement deliveryMessage = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.deliveryMessage, 20);
				if (deliveryMessage != null) {
					report.updateTestLog("Verify Processing delivery message is displayed", "Processing delivery message is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify Processing delivery message is displayed", "Processing delivery message is not displayed", Status.FAIL);
				}
				WebElement pdfLink = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.pdfLink, 20);
				if (pdfLink != null) {
					report.updateTestLog("Verify link of PDF of exercise report is displayed", "Link of PDF of exercise report is displayed", Status.PASS);
				} else {
					report.updateTestLog("Verify link of PDF of exercise report is displayed", "Link of PDF of exercise report is not displayed", Status.FAIL);
				}
				WebElement closeButton = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.closeButtton, 20);
				if (closeButton != null) {
					if (browsername.equalsIgnoreCase("internet explorer")) {
						commonLibrary.clickJS(closeButton);
						commonLibrary.switchToWindow("icwexercisestatus");
					} else {
						commonLibrary.clickButton(closeButton);
						commonLibrary.switchToWindow("icwexercisestatus");
					}
				} else {
					report.updateTestLog("Click close button", "Close button is not available", Status.FAIL);
				}
			} else {
				report.updateTestLog("Verify secondary is opened", "Secondary window is not opened", Status.FAIL);
			}
		} else {
			report.updateTestLog("Click Download button", "Download button is not available", Status.FAIL);
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Email button and send Email
	// # Function Name : sendEmail
	// # Author : Anbarasan
	// # Date Created : June'15
	// #*****************************************************************************************************************************
	public ICWExcercise sendEmail(String mailId) {
		// WebElement EmailIcon = commonLibrary.isExist(UIMAP_Document.btnEmail,
		// 20);
		// if(EmailIcon!=null)
		// {
		// if(browsername.equalsIgnoreCase("internet explorer"))
		// commonLibrary.Click_JS(EmailIcon);
		// else
		// commonLibrary.clickButton(EmailIcon);
		generalFunctions.clickDeliverySelectOption("delivery", "email");
		WebElement emailPopup = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.emailPopup, 20);
		if (emailPopup != null) {
			report.updateTestLog("Verify email popup is displayed", "email popup is displayed", Status.PASS);
			WebElement emailFormat = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.emailFormat, 20);
			if (emailFormat != null) {
				if (emailFormat.getText().toLowerCase().contains(".pdf"))
					report.updateTestLog("verify document type is pdf", "The document type is pdf", Status.PASS);
				else
					report.updateTestLog("verify document type is pdf", "The document type is not pdf", Status.FAIL);
			} else {
				report.updateTestLog("verify document type is pdf", "Document Type is not available", Status.FAIL);
			}
			WebElement emailIdTextBox = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.emailIdTextBox, 20);
			if (emailIdTextBox != null) {
				commonLibrary.setDataInTextBoxClear(emailIdTextBox, mailId, "To");
			} else {
				report.updateTestLog("Set email Id in 'To' field", "'To' field is not available", Status.FAIL);
			}
			WebElement coverPage = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.coverPage, 20);
			if (coverPage == null) {
				report.updateTestLog("Verify if Option to select the cover page is not displayed", "Option to select the cover page is not displayed", Status.PASS);
			} else {
				report.updateTestLog("Verify if Option to select the cover page is not displayed", "Option to select the cover page is displayed", Status.FAIL);
			}
			WebElement emailButton = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.emailButton, 20);
			if (emailButton != null) {
				if (browsername.equalsIgnoreCase("internet explorer"))
					commonLibrary.clickJS(emailButton);
				else
					commonLibrary.clickButton(emailButton);
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				commonLibrary.switchToWindow("deliverysecondarywindow");
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				WebElement deliveryComplete = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.deliveryComplete, 20);
				if (deliveryComplete != null) {
					report.updateTestLog("Verify secondary is opened", "Secondary window is opened", Status.PASS);
					WebElement deliveryMessage = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.deliveryMessage, 20);
					if (deliveryMessage != null) {
						report.updateTestLog("Verify Processing delivery message is displayed", "Processing delivery message is displayed", Status.PASS);
					} else {
						report.updateTestLog("Verify Processing delivery message is displayed", "Processing delivery message is not displayed", Status.FAIL);
					}
					WebElement closeButton = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.closeButtton, 20);
					if (closeButton != null) {
						if (browsername.equalsIgnoreCase("internet explorer")) {
							commonLibrary.clickJS(closeButton);
							commonLibrary.switchToWindow("icwexercisestatus");
						} else {
							commonLibrary.clickButton(closeButton);
							commonLibrary.switchToWindow("icwexercisestatus");
						}
					} else {
						report.updateTestLog("Click close button", "Close button is not available", Status.FAIL);
					}
				} else {
					report.updateTestLog("Verify secondary is opened", "Secondary window is not opened", Status.FAIL);
				}
			} else {
				report.updateTestLog("Click Email button", "Email button is not available", Status.FAIL);
			}
		} else {
			report.updateTestLog("Verify email popup is displayed", "email popup is not displayed", Status.FAIL);
		}
		// }
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify previous answer is present in
	// Exercise Page.
	// # Function Name : verifyAnswerInExcercisePage
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyAnswerInExcercisePage(String answer) {
		WebElement prevAttemptsContainer = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.prevAttemptsContainer, 10);
		if (prevAttemptsContainer.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").contains(answer.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))) {
			report.updateTestLog("Verify " + answer + " is present in Exercise page.", answer + " is present in Exercise page.", Status.PASS);
		} else {
			report.updateTestLog("Verify " + answer + " is present in Exercise page.", answer + " is not present in Exercise page.", Status.FAIL);
		}
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify if Exercise Page is
	// displayed.
	// # Function Name : verifyExercisePage
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyExercisePage() {
		WebElement exerciseContent = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.exerciseContent, 15);
		if (exerciseContent != null)
			report.updateTestLog("Verify Exercise page is displayed.", "Exercise page is displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Exercise page is displayed.", "Exercise page is not displayed.", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Submit button is disabled for
	// the given problem.
	// # Function Name : verifySubmitDisabled
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************
	public ICWExcercise verifySubmitDisabled(String problemNo) {
		String submitid = "problem-" + problemNo + "-textarea-submit";
		WebElement exerciseContent = commonLibrary.isExistNegative(UIMAP_Interactivecitationworkstation.exerciseContent, 15);
		WebElement submit = commonLibrary.isExist(exerciseContent, By.cssSelector("input[id='" + submitid + "']"), 10);
		if (submit != null) {
			if (submit.isEnabled())
				report.updateTestLog("Verify Submit button for problem " + problemNo + " is disabled and unclickable.", "Submit button for problem " + problemNo + " is enabled and clickable.", Status.FAIL);
			else
				report.updateTestLog("Verify Submit button for problem " + problemNo + " is disabled and unclickable.", "Submit button for problem " + problemNo + " is disnabled and unclickable.", Status.PASS);
		} else
			report.updateTestLog("Verify Submit button for problem " + problemNo + " is disabled and unclickable.", "Submit button for problem " + problemNo + " is not present.", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click the Home Link
	// # Function Name : clickHome
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public Interactivecitationworkstation clickHome() {
		generalFunctions.clickHome();
		return new Interactivecitationworkstation(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify answer box is absent for
	// selected problem
	// # Function Name : verifyAnswerBoxAbsent
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyAnswerBoxAbsent() {
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement textBox = commonLibrary.isExistNegative(section, UIMAP_Interactivecitationworkstation.textBox, 5);
		if (textBox == null)
			report.updateTestLog("Verify Answer box is absent.", "Answer box is absent.", Status.PASS);
		else
			report.updateTestLog("Verify Answer box is absent.", "Answer box is present.", Status.FAIL);
		return new ICWExcercise(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify incorrect citations are
	// absent for selected problem
	// # Function Name : verifyWrongAnswerCitationsAbsent
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyWrongAnswerCitationsAbsent() {
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement incorrAttempt = commonLibrary.isExistNegative(section, UIMAP_Interactivecitationworkstation.incorrAttempt, 8);
		if (incorrAttempt == null)
			report.updateTestLog("Verify Incorrect citations are not displayed.", "Incorrect citations are not displayed.", Status.PASS);
		else
			report.updateTestLog("Verify Incorrect citations are not displayed", "Incorrect citations are displayed.", Status.FAIL);
		return new ICWExcercise(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify hints are absent for selected
	// problem
	// # Function Name : verifyHintsAbsent
	// # Author : Pratik
	// # Date Created : Nov'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyHintsAbsent() {
		WebElement section = commonLibrary.isExist(UIMAP_Interactivecitationworkstation.section, 10);
		WebElement hints = commonLibrary.isExistNegative(section, UIMAP_Interactivecitationworkstation.hints, 7);
		if (hints == null)
			report.updateTestLog("Verify hints are not displayed.", "Hints are not displayed.", Status.PASS);
		else
			report.updateTestLog("Verify hints are not displayed", "Hints are displayed.", Status.FAIL);
		return new ICWExcercise(scriptHelper);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify Banner message and click on
	// Dismiss button.
	// # Function Name : verifyBannerMessageClickDismiss
	// # Author : Pratik
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public ICWExcercise verifyBannerMessageClickDismiss(String text) {
		generalFunctions.verifyBannerMessageClickDismiss(text);
		return new ICWExcercise(scriptHelper);
	}

}
