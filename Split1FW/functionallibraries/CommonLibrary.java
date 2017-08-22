package functionallibraries;

import org.openqa.selenium.By;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import UIMAP.UIMAP_Home;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_SignIn;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class CommonLibrary extends ReusableLibrary {
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	/**
	 * Constructor to initialize the functional library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link driverScript}
	 */
	public CommonLibrary(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Back
	// # Function Name : ClickBrowserBack     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickBrowserBack() {
		try {

			String PrevURL = driver.getCurrentUrl();
			String currentURL = null;
			if (browsername.contains("chrome")
					|| browsername.contains("firefox")) {
				driver.navigate().back();
				this.sleep(7000);
			} else {
				this.sleep(15000);
				Actions builder = new Actions(driver);
				builder.sendKeys(Keys.BACK_SPACE).perform();
				this.sleep(30000);
			}
			report.updateTestLog("Click on Browser Back",
					"Clicked on Browser Back", Status.PASS);

			int counter = 0;
			do {
				counter = counter + 1;
				currentURL = driver.getCurrentUrl();
				if (currentURL.equals(PrevURL))
					this.sleep(80000);
			} while (currentURL.equals(PrevURL) && counter <= 40);

			if (currentURL.equals(PrevURL)) {
				if (browsername.contains("internet")) {
					driver.navigate().back();
					this.sleep(70000);
				} else {
					this.sleep(3000);
					Actions builder = new Actions(driver);
					builder.sendKeys(Keys.BACK_SPACE).perform();
					this.sleep(150000);
				}

				counter = 0;
				do {
					counter = counter + 1;
					currentURL = driver.getCurrentUrl();
					if (currentURL.equals(PrevURL))
						this.sleep(50000);
				} while (currentURL.equals(PrevURL) && counter <= 40);

			}
		} catch (StaleElementReferenceException e) {
			// return 0;
			System.out.println(e.toString());
		} catch (Exception e) {

			throw new FrameworkException("Click on Browser Back",
					"Not Clicked on Browser Back");

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to highlight Element
	// # Function Name : highlightElement     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void highlightElement(WebElement element) {
		try {
			for (int i = 0; i < 1; i++) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "color: yellow; border: 3px solid black;");
				this.sleep(300);
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check if the element is present
	// # Function Name : isExist(By by, int timeOut)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExist(By by, int timeOut) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement myElement;
			int counter = 0;
			do {
				counter = counter + 1;
				myElement = wait.until(ExpectedConditions
						.elementToBeClickable(by));
				if (myElement == null)
					this.sleep(5000);
			} while (myElement == null && counter <= 50);

			return myElement;
		} catch (Exception e) {
			return null;


			// throw new FrameworkException("Click on Browser Back",
			// "Not Clicked on Browser Back");


			// throw new FrameworkException("object not identified "+by, by
			// +" is not identified,unable to continue with execution");

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check if the element is present
	// # Function Name : isExist(By by, int timeOut)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement getElement(By by, String elementName) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement myElement;
			myElement = wait.until(ExpectedConditions.elementToBeClickable(by));
			return myElement;
		} catch (Exception e) {

			throw new FrameworkException("The Element " + elementName
					+ " should be present", "The Element " + elementName
					+ " is not  present");

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check if the element is present
	// within the passed timeout
	// # Function Name : isExist_Negative(By by, int timeOut)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExistNegative(By by, int timeOut) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			WebElement myElement = wait.until(ExpectedConditions
					.elementToBeClickable(by));

			return myElement;
		} catch (Exception e) {
			// System.out.println(e.toString());
			return null;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for webelement List
	// present in particular parent object
	// # Function Name : isExist_List(WebElement parent, By by, int timeOut)
	// # Author : Uma
	// # Date Created : Jan’15
	// #*****************************************************************************************************************************

	public List<WebElement> isExistList(WebElement parent, By by, int timeOut) {
		try {
			// driver.manage().timeouts().implicitlyWait(timeOut,
			// TimeUnit.MILLISECONDS);
			// WebDriverWait wait = new WebDriverWait(driver,timeOut);
			List<WebElement> elementList = parent.findElements(by);
			// driver.manage().timeouts().implicitlyWait(120,
			// TimeUnit.MILLISECONDS);
			return elementList;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for webelement List
	// present in particular parent object
	// # Function Name : isExist_List(WebElement parent, By by, int timeOut)
	// # Author : Uma
	// # Date Created : Jan’15
	// #*****************************************************************************************************************************

	public WebElement isExistNegative(WebElement parent, By by, int timeOut) {
		try {
			// driver.manage().timeouts().implicitlyWait(1,
			// TimeUnit.MILLISECONDS);
			// WebDriverWait wait = new WebDriverWait(driver,timeOut);
			WebElement elementList = parent.findElement(by);
			// driver.manage().timeouts().implicitlyWait(120,
			// TimeUnit.MILLISECONDS);
			return elementList;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for webelement List
	// present
	// # Function Name : isExist_List(By by, int timeOut)
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public List<WebElement> isExistList(By by, int timeOut) {
		try {

			List<WebElement> elementList = driver.findElements(by);

			return elementList;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for webelement List
	// present
	// # Function Name : isExist_report(By by, int timeOut, String
	// elementName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExistReport(By by, int timeOut, String elementName) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			WebElement myElement = wait.until(ExpectedConditions
					.elementToBeClickable(by));

			if (myElement != null)
				report.updateTestLog("verify " + elementName + " exist ",
						elementName + " exist in the page", Status.PASS);
			else
				report.updateTestLog("verify " + elementName + " exist ",
						elementName + " NOT exist in the page", Status.FAIL);

			return myElement;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify element text from the list of
	// elements
	// # Function Name : VerifyElementText_List(List<WebElement> elementList,
	// String elementText, Boolean exactMatch)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verifyElementTextList(List<WebElement> elementList,
			String elementText, Boolean exactMatch) {
		for (WebElement element : elementList) {

			if (!exactMatch) {
				if (element.getText().contains(elementText))
					return true;
			} else if (element.getText().equals(elementText))
				return true;

		}

		return false;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for the element
	// # Function Name : isExist_obj(WebElement elnt, int timeOut)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExistobj(WebElement elnt, int timeOut) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			WebElement myElement = wait.until(ExpectedConditions
					.elementToBeClickable(elnt));

			return myElement;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to implicitly wait for the element
	// within the parent element
	// # Function Name : isExist(WebElement parent, By by, int timeOut)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExist(WebElement parent, By by, int timeOut) {
		try {

			WebElement element = parent.findElement(by);

			return element;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return null;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait for small time in milliseconds
	// # Function Name : smallWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void smallWait() {
		try {
			this.sleep(Integer.parseInt(properties.getProperty("SmallWait")) * 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait for medium time in milliseconds
	// # Function Name : mediamWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void mediamWait() {
		try {
			this.sleep(Integer.parseInt(properties.getProperty("MediumWait")) * 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait for long time in milliseconds
	// # Function Name : longWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void longWait() {
		try {
			this.sleep(Integer.parseInt(properties.getProperty("LongWait")) * 1000);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to slect the dropdown value using
	// selectbyVisibletest method
	// # Function Name : SelectByVisibleText(WebElement element, String
	// value)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean selectByVisibleText(WebElement element, String value) {
		boolean result = false;
		try {
			Select select = new Select(element);
			select.selectByVisibleText(value);
			result = true;

		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get the selected dropdown value
	// # Function Name : Select_GetSelected(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String selectGetSelected(WebElement element) {
		String result = null;
		try {
			Select select = new Select(element);
			if (select.getAllSelectedOptions().size() > 0)
				result = select.getAllSelectedOptions().get(0).getText();
			report.updateTestLog("Select the value", "The value is selected",
					Status.PASS);
		} catch (Exception e) {
			System.out.println(e.toString());
			report.updateTestLog("Select the value",
					"The value is not selected", Status.FAIL);
			result = null;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List
	// # Function Name : Select_FromList(WebElement element,String strValue)   
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean selectFromList(WebElement element, String strValue) {

		Boolean blnFlag = false;
		try {
			List<WebElement> listItems = isExistList(element, By.tagName("li"),
					20);
			for (WebElement item : listItems) {
				if (item.getText().toUpperCase()
						.contains(strValue.toUpperCase())) {

					WebElement button = isExistNegative(item,
							By.tagName("button"), 2);
					if (button != null) {

						if (browsername.contains("internet"))
							this.clickJS(button, item.getText());// not working
																	// to select
																	// actions
																	// dropdownlist
																	// in IE
						else
							this.clickJS(button, item.getText());
					} else {
						//
						this.clickJS(item, item.getText());
						// button.click();
						// this.click_JS(parent,item.getText());
					}
					this.sleep(5000);
					blnFlag = true;
					break;
				}

			}

		} catch (Exception e) {
			throw new FrameworkException("Select Value " + strValue, strValue
					+ " is not selected");

		}
		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List
	// # Function Name : Select_FromList_Index(WebElement element,String
	// strValue)   
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void selectFromListIndex(WebElement element, int intIndex) {

		try {
			List<WebElement> listItems = isExistList(element, By.tagName("li"),
					20);
			if (listItems.size() > 0) {
				listItems.get(intIndex).click();

			}

		} catch (Exception e) {
			throw new FrameworkException("Select Value with index of "
					+ intIndex, "Value with index of " + intIndex
					+ " is not selected");

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll down the page
	// # Function Name : scrollDown(int height)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void scrollDown(int height) {
		String str = Integer.toString(height);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + str
				+ ");");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll up the page
	// # Function Name : scrollUp(int height)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void scrollUp(int height) {
		if (height > 0) {
			String str = Integer.toString(height);
			((JavascriptExecutor) driver).executeScript("scroll(0,-" + str
					+ ");");
		} else {
			String str = Integer.toString(height * -1);
			((JavascriptExecutor) driver).executeScript(("window.scrollTo(0,"
					+ str + ");"));
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll down to see document content
	// # Function Name : scrollDown     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void scrollDown() {

		((JavascriptExecutor) driver)
				.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll up to see document content
	// # Function Name : highlightElement     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void scrollUp() {
		((JavascriptExecutor) driver)
				.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for scrolling up to view the webelement
	// # Function Name : ScrollToView(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean scrollToView(WebElement element) {
		boolean result = false;
		try {

			this.sleep(2000);
			int y = element.getLocation().y;
			int y1 = 380;
			this.scrollUp(y1 / 2 - y);
			this.sleep(2000);
			result = true;
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the value is selected by
	// default
	// # Function Name : Select_IsSelected(WebElement element, String
	// value)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean selectIsSelected(WebElement element, String value) {
		boolean result = false;
		try {
			List<WebElement> options = element.findElements(By
					.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().trim().equalsIgnoreCase(value)) {
					if (option.isSelected() == true) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the checkbox
	// # Function Name : SetCheckBox(By by)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setCheckBox(By by) {
		boolean result = false;
		try {
			for (int i = 0; i < 4; i++) {
				if (!driver.findElement(by).isSelected())
					driver.findElement(by).click();
				if (driver.findElement(by).isSelected())
					break;
				else
					this.sleep(400);
			}
			result = driver.findElement(by).isSelected();
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the checkbox
	// # Function Name : SetCheckBox(WebElement obj)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setCheckBox(WebElement obj, String FieldName) {
		boolean result = false;
		try {
			for (int i = 0; i < 4; i++) {
				if (!obj.isSelected()) {
					// ScrollToView(obj);
					this.clickJSNoLog(obj);
					this.sleep(1000);
				}
				if (obj.isSelected())
					break;
				else
					this.sleep(400);
			}
			result = obj.isSelected();
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		if (result)
			report.updateTestLog("Select Check box", FieldName
					+ " Checkbox is selected", Status.PASS);
		else
			report.updateTestLog("Select Check box", FieldName
					+ " Checkbox is not selected", Status.FAIL);
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the checkbox by just a simple
	// click
	// # Function Name : SetCheckBox_SimpleClick(WebElement obj)     
	// # Author : Divakar
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setCheckBoxSimpleClick(WebElement obj, String FieldName) {
		boolean result = false;
		try {
			for (int i = 0; i < 4; i++) {
				if (!obj.isSelected()) {
					// ScrollToView(obj);
					this.clickButtonRet(obj, FieldName);
					this.sleep(1000);
				}
				if (obj.isSelected())
					break;
				else
					this.sleep(400);
			}
			result = obj.isSelected();
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		if (result)
			report.updateTestLog("Select Check box", FieldName
					+ " Checkbox is selected", Status.PASS);
		else
			report.updateTestLog("Select Check box", FieldName
					+ " Checkbox is not selected", Status.FAIL);
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check/uncheck the checkbox
	// # Function Name : SetCheckBox(WebElement obj, boolean isCheck)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setCheckBox(WebElement obj, boolean isCheck) {
		boolean result = false;

		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;

			if (obj.isSelected() != isCheck) {
				executor.executeScript("arguments[0].focus();", obj);
				if (browsername.contains("internet")) {

					executor.executeScript("arguments[0].click();", obj);
				} else if (browsername.contains("chrome")) {

					executor.executeScript("arguments[0].click();", obj);
				} else {
					obj.click();
				}
				this.sleep(400);
				result = true;

			}

			else
				result = obj.isSelected() == isCheck;

		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Set RadioButton
	// # Function Name : SetRadioButton(By by)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setRadioButton(By by) {
		return this.setCheckBox(by);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Set RadioButton
	// # Function Name : SetRadioButton(WebElement obj)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setRadioButton(WebElement obj, String strFieldName) {
		return this.setCheckBox(obj, strFieldName);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select/unselect RadioButton
	// # Function Name : SetRadioButton(WebElement obj, boolean isCheck)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean setRadioButton(WebElement obj, boolean isCheck) {
		return this.setCheckBox(obj, isCheck);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Delete Cookies of browser
	// # Function Name : DeleteCookies     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean deleteCookies() {
		boolean result = false;
		try {
			driver.manage().deleteAllCookies();
			result = true;
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to mouse over on the element
	// # Function Name : onMouseOver(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseOver(WebElement element) {
		boolean result = false;
		try {
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, element);
			result = true;
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do left/Right mouse click on the
	// element
	// # Function Name : onMouseclickJS(WebElement element, String
	// clickType)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseclickJS(WebElement element, String clickType) {
		boolean result = false;
		try {
			if (clickType.equalsIgnoreCase("Left")) {
				if (System.getProperty("browser").contains("firefox")) {

					String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent('click', true, true, window,1,1,1,1,1,false,false,false,false,0,arguments[0]); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { var ev = document.createEventObject();ev.button = 1; arguments[0].fireEvent('onclick',ev);}";
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript(mouseOverScript, element);
				} else if (System.getProperty("browser").contains("safari")) {
					String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent('click', true, true, window,1,1,1,1,1,false,false,false,false,0,arguments[0]); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { var ev = document.createEventObject();ev.button = 1; arguments[0].fireEvent('onclick',ev);}";
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript(mouseOverScript, element);
				} else
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].click();", element);
				result = true;
				browserBasedWait();
			} else {
				onMouseRightclick(element);
				this.smallWait();
				result = true;
			}
		} catch (StaleElementReferenceException e) {
			// return 0;
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing left mouse click
	// # Function Name : onMouseclickJS(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseClickJS(WebElement element) {
		return onMouseclickJS(element, "Left");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function for doing right mouse click on element
	// # Function Name : onMouseRightclick(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseRightclick(WebElement element) {
		boolean result = false;
		try {
			if (System.getProperty("browser").contains("safari")) {
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent('click', true, false, window,0,0,0,0,0,false,false,false,false,2,null); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { var ev = document.createEventObject();ev.button = 2; arguments[0].fireEvent('onclick',ev);}";
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(mouseOverScript, element);
			} else {
				org.openqa.selenium.interactions.Actions builderq = new org.openqa.selenium.interactions.Actions(
						driver);
				org.openqa.selenium.interactions.Action rclick = builderq
						.contextClick(element).build();
				rclick.perform();
			}
			result = true;
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		browserBasedWait();
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do left/Right mouse click on the
	// element
	// # Function Name : onMouseclick(WebElement element, String clickType)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseclick(WebElement element, String clickType) {
		boolean result = false;
		try {
			if (clickType.equalsIgnoreCase("Left")) {

				element.click();
				this.smallWait();
				result = true;
			} else {
				onMouseRightclick(element);
				this.smallWait();
				result = true;
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		browserBasedWait();
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to do left mouse click on element
	// # Function Name : onMouseclick(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean onMouseclick(WebElement element) {
		return onMouseclick(element, "Left");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait based on the browser
	// # Function Name : BrowserBasedWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void browserBasedWait() {

		if (System.getProperty("browser").contains("iexplore"))
			this.sleep(5000);
		else if (System.getProperty("browser").contains("safari")) {
			int i = Integer.parseInt(properties.getProperty("SafariWait")) * 1000;
			this.sleep(i);
			if (this.isExist(By.cssSelector("div.error-container"), 2) != null) {
				driver.navigate().refresh();
				this.sleep(i);
				System.out.println("Page Refreshed.");
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on sub link
	// # Function Name : SubLink_click(WebElement parent, String linkText,
	// boolean ignoreSpecialChar, String clickType)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean subLinkClick(WebElement parent, String linkText,
			boolean ignoreSpecialChar, String clickType) {
		boolean result = false;
		String str = null;
		try {
			List<WebElement> lnkName = parent.findElements(By.tagName("a"));
			for (int x = 0; x < lnkName.size(); x = x + 1) {
				str = lnkName.get(x).getText();
				if (str != null) {
					if (ignoreSpecialChar
							&& str.trim()
									.replaceAll("[^a-zA-Z0-9]", "")
									.equalsIgnoreCase(
											linkText.replaceAll("[^a-zA-Z0-9]",
													""))) {
						if (this.onMouseclick(lnkName.get(x), clickType)) {
							result = true;
							break;
						}
					}
					if (!ignoreSpecialChar
							&& str.trim().equalsIgnoreCase(linkText)) {
						if (this.onMouseclick(lnkName.get(x), clickType)) {
							result = true;
							break;
						}
					}
				}
			}
			this.sleep(3000);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on sub link
	// # Function Name : SubLink_click(WebElement parent, String linkText,
	// boolean ignoreSpecialChar)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean subLinkClick(WebElement parent, String linkText,
			boolean ignoreSpecialChar) {
		return subLinkClick(parent, linkText, ignoreSpecialChar, "Left");
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select_FolderTree_Plus_Minus
	// # Function Name : Select_FolderTree_Plus_Minus(WebElement parent, String
	// sign, String FolderName, boolean Isclick, String clickType)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean selectFolderTreePlusMinus(WebElement parent, String sign,
			String FolderName, boolean Isclick, String clickType) {
		boolean result = false;
		try {
			String folders[] = FolderName.toLowerCase().trim().split(",");
			String strLinkName = null;
			String strLink[];
			boolean blnflag = false;
			WebElement parent1 = parent;
			for (int j = 0; j < folders.length; j++) {
				blnflag = false;
				List<WebElement> lis = parent1.findElements(By.tagName("li"));
				for (int i = 0; i < lis.size(); i++) {
					strLinkName = lis.get(i).findElement(By.tagName("a"))
							.getText();
					if (strLinkName.contains("(")) {
						strLink = strLinkName.split("\\(");
						if (strLink[0].trim().equalsIgnoreCase(folders[j])
								&& lis.get(i).findElement(By.tagName("a"))
										.isDisplayed()) {
							blnflag = true;
						}
					} else if (strLinkName.trim().equalsIgnoreCase(folders[j])
							&& lis.get(i).findElement(By.tagName("a"))
									.isDisplayed()) {
						blnflag = true;
					}

					if (blnflag) {
						if (j == folders.length - 1) {
							if (sign.equalsIgnoreCase("-")) {

								if (lis.get(i).findElement(By.tagName("ul"))
										.isDisplayed()) {
									if (Isclick)
										this.onMouseclick(
												lis.get(i).findElement(
														By.tagName("ins")),
												clickType);
									result = true;
									break;
								}
							} else if (sign.equalsIgnoreCase("+")) {
								if (!(lis.get(i).findElement(By.tagName("ul"))
										.isDisplayed())) {
									if (Isclick)
										this.onMouseclick(
												lis.get(i).findElement(
														By.tagName("ins")),
												clickType);
									result = true;
									break;
								}
							}

						}
						parent1 = lis.get(i);
						break;
					}
				}
				if (result)
					break;
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select_FolderTree
	// # Function Name : Select_FolderTree(WebElement parent, String FolderName,
	// boolean Isclick, String clickType)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean selectFolderTree(WebElement parent, String FolderName,
			boolean Isclick, String clickType) {
		boolean result = false;
		boolean blnflag = false;
		try {
			String folders[] = FolderName.split(",");
			String strLinkName;
			String strLink[];
			WebElement parent1 = parent;
			for (int j = 0; j < folders.length; j++) {
				blnflag = false;

				List<WebElement> lis = parent1.findElements(By
						.cssSelector("li"));
				for (int i = 0; i < lis.size(); i++) {
					int n = lis.get(i).findElement(By.tagName("a"))
							.getLocation().x;
					int n1 = lis.get(i).findElement(By.tagName("a"))
							.getLocation().y;
					strLinkName = lis.get(i).findElement(By.cssSelector("a"))
							.getAttribute("textContent");
					if (strLinkName == null)
						strLinkName = lis.get(i).findElement(By.tagName("a"))
								.getText();
					if (strLinkName.contains("(")) {
						strLink = strLinkName.split("\\(");

						if (strLink[0].replaceAll("[^a-zA-Z0-9]", "")
								.equalsIgnoreCase(
										folders[j].replaceAll("[^a-zA-Z0-9]",
												""))
								&& (lis.get(i).findElement(By.tagName("a"))
										.isDisplayed() | (n > 0 && n1 > 0)))

						{
							blnflag = true;
						}
					} else if (strLinkName.replaceAll("[^a-zA-Z0-9]", "")
							.equalsIgnoreCase(
									folders[j].replaceAll("[^a-zA-Z0-9]", ""))
							&& (lis.get(i).findElement(By.tagName("a"))
									.isDisplayed() | (n > 0 && n1 > 0))) {
						blnflag = true;
					}

					if (blnflag) {
						if (j == folders.length - 1) {
							if (Isclick) {
								// this.ScrollToView(lis.get(i).findElement(By.tagName("a")));
								this.onMouseclickJS(
										lis.get(i).findElement(By.tagName("a")),
										clickType);
							}
							result = true;
							break;
						} else if (!lis.get(i).findElement(By.tagName("ul"))
								.isDisplayed())
							this.onMouseclick(
									lis.get(i).findElement(By.tagName("ins")),
									clickType);
						parent1 = lis.get(i);
						break;
					}
				}
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Update Date Format
	// # Function Name : UpdateDateFormat(boolean blnDays, int NoOfDays)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String updateDateFormat(boolean blnDays, int NoOfDays) {
		String Date1 = null;
		try {
			Calendar date = Calendar.getInstance();
			Calendar cldr;
			SimpleDateFormat dateformatter = new SimpleDateFormat(
					"MMM dd, yyyy");
			cldr = (Calendar) date.clone();

			if (blnDays) {
				cldr.add(Calendar.DAY_OF_YEAR, -NoOfDays);
				Date1 = dateformatter.format(cldr.getTime());
			} else {
				Date1 = dateformatter.format(cldr.getTime());
			}

		} catch (Exception e) {
			Date1 = null;
		}

		return Date1;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to send Shift keys from keyboard
	// # Function Name : writeKeyboard(String st)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean writeKeyboard(String st) {
		boolean result = false;
		// int ii=0;
		try {
			Robot bot = new Robot();
			this.sleep(3000);
			int k = 0;
			char[] arr = st.toCharArray();
			for (int i = 0; i < arr.length; i++) {
				this.sleep(500);
				k = arr[i];
				if (arr[i] < 97)
					bot.keyPress(KeyEvent.VK_SHIFT);
				else
					k -= 32;
				bot.keyPress(k);
				this.sleep(500);
				bot.keyRelease(k);
				if (arr[i] < 97)
					bot.keyRelease(KeyEvent.VK_SHIFT);
				result = true;
			}

			bot = null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check if th element present
	// # Function Name : isExist(By by)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public WebElement isExist(By by) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement element = wait.until(ExpectedConditions
					.elementToBeClickable(by));
			return element;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the element font is bold
	// # Function Name : CSS_Font_IsBold(WebElement element)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public boolean cssFontIsBold(WebElement element) {
		boolean result = false;
		try {
			result = (element.getCssValue("font-weight").equalsIgnoreCase(
					"bold") || Integer.parseInt(element
					.getCssValue("font-weight")) > 400);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set data in text box
	// # Function Name : SetDataInTextBox(By by, String strTransId)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean setDataInTextBox(By by, String strVal) {
		try {

			WebElement eltTextBoxId = this.isExistNegative(by, 30);

			if (eltTextBoxId != null) {
				eltTextBoxId.clear();
				eltTextBoxId.sendKeys(strVal);

				report.updateTestLog("Set " + strVal + " in text box", strVal + " is set in text box", Status.DONE);

				this.sleep(2000);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Set Data In TextBox With Log
	// # Function Name : SetDataInTextBox_WithLog(By by, String strVal)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void setDataInTextBoxWithLog(By by, String strVal) {
		try {
			if (setDataInTextBox(by, strVal))
				report.updateTestLog(
						"Set value " + strVal + " in the text box", strVal
								+ " is set", Status.DONE);
			else
				throw new FrameworkException("Set value " + strVal
						+ " in the text box", strVal + " is not set");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to SetDataInTextBox
	// # Function Name : SetDataInTextBox(WebElement Obj, String
	// strTransId)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean setDataInTextBox(WebElement Obj, String strVal,
			String FieldName) {
		try {

			WebElement eltTextBoxId = Obj;
			
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", Obj);

			String strNew = null;
			strNew = strVal.replaceAll("\\(", Keys.chord(Keys.SHIFT, "9"));
			strNew = strNew.replaceAll("\\#", Keys.chord(Keys.SHIFT, "3"));
			strNew = strNew.replaceAll("\\-", Keys.SUBTRACT.toString());
			strNew = strNew.replaceAll("\\}", Keys.chord(Keys.SHIFT, "]"));
			if (eltTextBoxId != null) {
				if (browsername.contains("internet")) {

					String textPresent = "";
					if (Obj.getAttribute("value") != null)
						textPresent = Obj.getAttribute("value");
					else if (Obj.getText() != null)
						textPresent = Obj.getText();
					if (!textPresent
							.contains("Enter terms, sources, a citation, or shep: to Shepardize®")) {
						
						if (!textPresent.equals(strNew)) {
							Obj.clear();
						
							Obj.sendKeys(strNew);
						}
					}

				} else {
					String textPresent = "";
					if (Obj.getAttribute("value") != null)
						textPresent = Obj.getAttribute("value");
					else if (Obj.getText() != null)
						textPresent = Obj.getText();
					if (!textPresent.equals(strVal)) {
						eltTextBoxId.clear();
						this.sleep(2000);
						eltTextBoxId.click();
						eltTextBoxId.sendKeys(strNew);
						eltTextBoxId.sendKeys(Keys.DOWN);
					}
				}

				report.updateTestLog("Set value " + strVal + " in the "
						+ FieldName + " text box", strVal + " is set in the "
						+ FieldName + " text box", Status.DONE);
				return true;
			} else {
				throw new FrameworkException("Set value " + strVal + " in the "
						+ FieldName + " text box", strVal
						+ " is not set in the " + FieldName + "text box");

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to SetDataInTextBox using javascript
	// # Function Name : SetDataInTextBox_javascript     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean setDataInTextBoxJavaScript(String element_Id, String strText) {
		try {

			((JavascriptExecutor) driver)
					.executeScript("document.getElementById('" + element_Id
							+ "').value='" + strText + "'");
			WebElement objNoteText = isExist(By.cssSelector("#" + element_Id),
					30);

			if (objNoteText.getText().contains(strText))
				return true;
			else
				return false;

		} catch (Exception e) {
			throw new FrameworkException("Set value " + strText, strText
					+ " is not set");

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click Button
	// # Function Name : clickButton(By by, String strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButton(By by, String strBtnName) {
		try {

			WebElement elntBtn = this.isExist(by, 40);
			if (elntBtn != null) {

				this.clickJS(elntBtn, strBtnName);

				report.updateTestLog("click on " + strBtnName + " button",
						strBtnName + " button is clicked", Status.DONE);
			} else
				throw new FrameworkException("click on " + strBtnName
						+ " button", strBtnName + " button is NOT clicked");

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("click on " + strBtnName + " button",
					strBtnName + " button is NOT clicked");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on button
	// # Function Name : clickButton(WebElement obj)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButton(WebElement obj) {
		try {

			if (obj != null)
				if (browsername.contains("internet")) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", obj);
				} else {

					this.clickJS(obj);
				}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("click on button",
					" button is NOT clicked");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton WithWait
	// # Function Name : clickButton_WithWait(By by, String strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonWithWait(By by, String strBtnName) {
		try {
			this.sleep(1000);
			WebElement elntBtn = this.isExist(by, 40);
			if (elntBtn != null) {

				this.clickJS(elntBtn, strBtnName);

				this.sleep(7000);

			} else
				throw new FrameworkException("click on " + strBtnName
						+ " button", strBtnName + " button does not exist");
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("click on " + strBtnName + " button",
					strBtnName + " button does not exist");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton with SmallWait
	// # Function Name : clickButton_Log_SmallWait(WebElement obj, String
	// strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonLogSmallWait(WebElement obj, String strBtnName) {
		try {
			if (browsername.contains("internet"))
				clickMethod(obj, strBtnName);
			else
				this.clickJS(obj, strBtnName);

			this.sleep(7000);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("click on " + strBtnName + " button",
					strBtnName + " button does not exist");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton
	// # Function Name : clickButton_Ret(WebElement obj, String strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean clickButtonRet(WebElement obj, String strBtnName) {
		try {
			this.sleep(4000);

			clickJS(obj);
			this.sleep(4000);

			return true;
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
			return false;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton
	// # Function Name : clickButton_Parent(WebElement elnt, String
	// strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonParent(WebElement elnt, String strBtnName) {
		try {

			this.clickJS(elnt, strBtnName);
			this.sleep(5000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("click on " + strBtnName + " button",
					strBtnName + " button does not exist");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton_Parent_WithWait
	// # Function Name : clickButton_Parent_WithWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonParentWithWait(WebElement elnt, String strBtnName) {
		try {

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", elnt);

			this.clickMouseMoveAction(elnt, strBtnName);
			this.sleep(25000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton_Parent_WithoutWait
	// # Function Name : clickButton_WithoutWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void clickButtonWithoutWait(WebElement elnt, String strBtnName) {
		try {

			this.clickJS(elnt, strBtnName);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton_Parent_WithWait
	// # Function Name : clickButton_Parent_WithWait     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonParentWithWaitJS(WebElement elnt, String strBtnName) {
		try {

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", elnt);
			executor.executeScript("arguments[0].click();", elnt);
			this.sleep(28000);

			report.updateTestLog("click on " + strBtnName + " button",
					strBtnName + " button is clicked", Status.DONE);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to get current date time
	// # Function Name : getCurrentDateTime     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public String getCurrentDateTime() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String strFormat = dateFormat.format(date);
			String strFormat1 = strFormat.replaceAll("/", "");
			String strFormat2 = strFormat1.replaceAll(":", "");
			String strFormat3 = strFormat2.replaceAll(" ", "");
			String strRandomString = strFormat3.replaceAll(":", "");
			return strRandomString;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton_submit
	// # Function Name : clickButton_submit(By by, String strBtnName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonSubmit(By by, String strBtnName) {
		try {
			driver.findElement(by).click();
			if (driver.findElement(by) != null)
				driver.findElement(by).submit();
			this.sleep(5000);

			report.updateTestLog("click on " + strBtnName + " button",
					strBtnName + " button is clicked", Status.DONE);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink
	// # Function Name : clickLink(By by, String strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLink(By by, String strLnkName) {
		try {
			this.sleep(3000);
			WebElement elntLnk = this.isExist(by, 40);
			if (elntLnk != null)
				this.clickJS(elntLnk, strLnkName);
			else
				report.updateTestLog("click on " + strLnkName + " link",
						strLnkName + " link does not exist", Status.FAIL);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function clickLink_WithWait
	// # Function Name : clickLink_WithWait(By by, String strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLinkWithWait(By by, String strLnkName) {
		try {

			WebElement elntLnk = this.isExist(by, 40);
			if (elntLnk != null) {

				this.clickJS(elntLnk, strLnkName);
				this.sleep(4000);
			} else
				report.updateTestLog("click on " + strLnkName + " link",
						strLnkName + " link does not exist", Status.FAIL);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// overloading
	public void clickLinkWithWait(WebElement elntLnk, String strLnkName) {
		try {

			if (elntLnk != null) {

				this.clickJS(elntLnk, strLnkName);
				this.sleep(4000);
			} else
				report.updateTestLog("click on " + strLnkName + " link",
						strLnkName + " link does not exist", Status.FAIL);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink
	// # Function Name : clickLink_Obj(WebElement elnt, By by, String
	// strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLinkObj(WebElement elnt, By by, String strLnkName) {
		try {

			WebElement elntLnk = this.isExist(elnt, by, 40);
			if (elntLnk != null) {

				this.clickJS(elntLnk, strLnkName);
				this.sleep(4000);
			} else
				report.updateTestLog("click on " + strLnkName + " link",
						strLnkName + " link does not exist", Status.FAIL);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink
	// # Function Name : clickLink(WebElement obj, String strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLink(WebElement obj, String strLnkName) {
		try {

			this.clickJS(obj, strLnkName);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink_withWebElement
	// # Function Name : clickLink_withWebElement(WebElement Parent, String
	// strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLinkWithWebElement(WebElement Parent, String strLnkName) {
		try {
			this.sleep(5000);// mandatory

			this.clickJS(Parent, strLnkName);
			this.sleep(5000);// mandatory

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());

			// report.updateTestLog("click on " + strLnkName + " link",
			// strLnkName + " link is NOT clicked", Status.FAIL);
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink_withWebElement_WithWait
	// # Function Name : clickLink_withWebElement_WithWait(WebElement Parent,
	// String strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLinkWithWebElementWithWait(WebElement Parent,
			String strLnkName) {
		try {

			this.clickJS(Parent, strLnkName);
			this.sleep(20000);// mandatory

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		}

		catch (Exception e) {
			System.out.println(e.toString());
			// report.updateTestLog("click on " + strLnkName + " link",
			// strLnkName + " link is NOT clicked", Status.FAIL);
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickLink_withWebElement_WithWait
	// # Function Name : clickLink_withWebElement_WithWait(WebElement Parent,
	// String strLnkName)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickLinkWithWebElementWithWaitJS(WebElement Parent,
			String strLnkName) {
		try {

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", Parent);
			executor.executeScript("arguments[0].click();", Parent);

			this.sleep(20000);// mandatory

			report.updateTestLog("click on " + strLnkName + " link", strLnkName
					+ " link is clicked", Status.DONE);
		} catch (StaleElementReferenceException e) {
			// return 0;
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			// report.updateTestLog("click on " + strLnkName + " link",
			// strLnkName + " link is NOT clicked", Status.FAIL);
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to window
	// # Function Name : SwitchToWindow    
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Boolean switchToWindow(String strWindowName) {
		try {

			Boolean blnCheckSwitch = false;
			String[] popUpHandler;
			System.out.println("Window handles"
					+ driver.getWindowHandles().size());
			if (driver.getWindowHandles().size() > 0) {

				this.sleep(2000);
				popUpHandler = driver.getWindowHandles().toArray(new String[0]);

				// Function for switching to the next window based on the window
				// name
				for (int i = 0; i < popUpHandler.length; i++) {

					try {

						try {
							driver.manage().timeouts()
									.pageLoadTimeout(1, TimeUnit.SECONDS);
						} catch (NoSuchWindowException e) {
							System.out.println(e.toString());
						}

						driver.switchTo().window(popUpHandler[i]);

					} catch (TimeoutException e) {

						System.out.println("timeout exception occured");

					}

					driver.manage().timeouts()
							.pageLoadTimeout(220, TimeUnit.SECONDS);
					this.sleep(20000);
					System.out.println("URL" + driver.getCurrentUrl());
					Boolean a = driver.getCurrentUrl().contains(strWindowName);

					if (a == true) {

						blnCheckSwitch = true;
						break;
					}
				}

			} else {
				System.out.println("no Secondary window opened");
			}

			return blnCheckSwitch;

		} catch (StaleElementReferenceException e) {

			System.out.println(e.toString());
			return false;

		} catch (Exception e) {

			System.out.println(e.toString());
			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List
	// # Function Name : Select_FromList_Index_Option(WebElement element,String
	// strValue)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void selectFromListIndexOption(WebElement element, int intIndex) {

		try {
			List<WebElement> listItems = isExistList(element,
					By.tagName("option"), 20);
			if (listItems.size() > 0) {
				listItems.get(intIndex).click();

			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List
	// # Function Name : Select_FromList_Option(WebElement element,String
	// strValue)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void selectFromListOption(WebElement element, String strValue) {
		try {
			List<WebElement> listItems = isExistList(element,
					By.tagName("option"), 20);
			for (WebElement item : listItems) {
				if (item.getText().toLowerCase().equals(strValue.toLowerCase())) {
					item.click();
					report.updateTestLog("Select option " + item.getText(),
							item.getText() + " is selected.", Status.DONE);
					break;
				}

			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : click_JS(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickJS(By by) {

		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();",
					driver.findElement(by));
			this.sleep(6000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : click_JS(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickJS(WebElement ele, String name) {

		try {

			windowFocus();

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", ele);
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on button " + name,
					"Clicked on button " + name, Status.PASS);
			this.sleep(3000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {

			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element without wait
	// # Function Name : click_JS_withoutWait(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void clickJSWithoutWait(WebElement ele, String name) {

		try {
			windowFocus();

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", ele);
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on  " + name, "Clicked on  " + name,
					Status.PASS);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {

			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element without wait
	// # Function Name : click_JS_objType(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void clickJSObjType(WebElement ele, String name) {

		try {
			windowFocus();

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", ele);
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on  " + name, "Clicked on  " + name,
					Status.PASS);
			this.sleep(9000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {

			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : Click_ScrolTo_ByCoordinate(By by) 
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickScrolToByCoordinate(By by) {

		try {
			WebElement elementToClick = driver.findElement(by);

			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"
					+ elementToClick.getLocation().y + ")");

			elementToClick.click();

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : Click_ScrolTo_ByCoordinate(By by) 
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickActionMoveByOffsetByCoordinate(By by) {

		try {
			WebElement elementToClick = driver.findElement(by);

			Actions action = new Actions(driver);
			action.moveByOffset(elementToClick.getLocation().x + 5,
					elementToClick.getLocation().y + 5).click().perform();
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete a file
	// # Function Name : FileExists_Delete
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void fileExistsDelete(String strFilePath, String strFileName) {
		try {

			File folder = new File(strFilePath);
			File[] listOfFiles = folder.listFiles();

			if (listOfFiles.length > 0) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						String[] filename = file.getName().split(
								"\\.(?=[^\\.]+$)"); // split
													// filename
													// from
													// it's
													// extension
						if (filename[0].contains(strFileName)) // matching
																// defined
																// filename
						{
							file.delete();

						}
					}
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : Click_ScrolTo_ByCoordinate(By by) 
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void sendKeysESCAPERobot() {

		try {

			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ESCAPE);
			r.keyRelease(KeyEvent.VK_ESCAPE);
			report.updateTestLog("Press Escape button in keyboard",
					"Pressed Escape button in keyboard", Status.PASS);
			this.sleep(2000);
			r = null;
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to scroll to the element and clicking
	// on it using coordinate
	// # Function Name : scrollHorizontal(By by) 
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void scrollHorizontal(int height) {
		if (height > 0) {
			String str = Integer.toString(height);
			((JavascriptExecutor) driver).executeScript("scroll(-" + str
					+ ",0);");
		} else {
			String str = Integer.toString(height * -1);
			((JavascriptExecutor) driver).executeScript(("window.scrollTo("
					+ str + ",0);"));
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to return parent node of the element
	// # Function Name : GetParentElement(WebElement element)   
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	// ##################################################//
	public WebElement getParentElement(WebElement elem) {
		WebElement parentElem = null;
		if (elem != null) {
			parentElem = elem.findElement(By.xpath(".."));
		}
		return parentElem;
	}

	// ##################################################//

	// #*****************************************************************************************************************************
	// # Function Description : Function to set the checkbox
	// # Function Name : SetCheckBox(WebElement obj)     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void isChecked(WebElement obj, String FieldName) {
		if (obj.isSelected()) {
			report.updateTestLog("verify Check box for " + FieldName
					+ " is checked", "Check box for " + FieldName
					+ " is checked", Status.PASS);
		}

		else
			report.updateTestLog("verify Check box for " + FieldName
					+ " is checked", "Check box for " + FieldName
					+ " is not checked", Status.PASS);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select an option from list
	// # Function Name : Select_FromList_OptionContains     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void selectFromListOptionContains(WebElement element, String strValue) {

		try {
			List<WebElement> listItems = isExistList(element,
					By.tagName("option"), 20);
			for (WebElement item : listItems) {
				if (item.getText().toLowerCase()
						.contains(strValue.toLowerCase())) {
					this.clickJS(item);
					break;
				}

			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select the visible text
	// # Function Name : SelectByVisibleTextByValue     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public boolean selectByVisibleTextByValue(WebElement element, String value,
			String value1) {
		boolean result = false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", element);
			Select select = new Select(element);
			select.selectByValue(value);
			result = true;
			if (select.getFirstSelectedOption().getText().contains(value1)) {
				report.updateTestLog(
						"Check whether selected option is displayed",
						"Selected option " + value1
								+ " is displayed successfully", Status.PASS);
			} else {
				report.updateTestLog(
						"Check whether selected option is displayed",
						"Selected option " + value1
								+ " is not displayed successfully", Status.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify_FromList_button
	// # Function Name : Verify_FromList_button     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verifyFromListButton(WebElement element, String strValue) {
		Boolean blnFlag = false;
		List<WebElement> btn = null;
		List<WebElement> listItems = isExistList(element, By.tagName("li"), 20);
		for (WebElement item : listItems) {
			btn = isExistList(item, By.tagName("button"), 20);
			if (btn.size() > 0) {
				for (WebElement item2 : btn) {
					if (item2.getText().toLowerCase()
							.contains(strValue.toLowerCase())) {
						blnFlag = true;
						break;
					}
				}
			} else {
				btn = isExistList(item, By.tagName("span"), 20);
				if (btn.size() > 0) {
					for (WebElement item2 : btn) {
						if (item2.getText().toLowerCase()
								.contains(strValue.toLowerCase())) {
							blnFlag = true;
							break;
						}
					}
				}
			}
			if (blnFlag)
				break;
		}

		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Select_FromList_Button
	// # Function Name : Select_FromList_Button     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Boolean selectFromListButton(WebElement element, String strValue) {
		Boolean blnFlag = false;

		List<WebElement> listItems = isExistList(element, By.tagName("li"), 20);
		for (WebElement item : listItems) {
			List<WebElement> btn = isExistList(item, By.tagName("button"), 20);
			for (WebElement item2 : btn) {
				if (item2.getText().toLowerCase()
						.contains(strValue.toLowerCase())) {
					if (browsername.contains("internet")) {
						this.clickJS(item2, strValue);
						blnFlag = true;
						break;
					} else {
						this.clickLinkWithWebElement(item2, strValue);
						blnFlag = true;
						break;
					}
				}
			}
			if (blnFlag)
				break;

		}

		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click and hold the left mouse button
	// # Function Name : doubleclickandHold()     
	// # Author : Ram Prasath
	// # Date Created : 2-Mar'15
	// #*****************************************************************************************************************************

	public void doubleClickAndHold() {
		if (browsername.contains("internet")) {

			WebElement textsection = this
					.isExist(UIMAP_SearchResult.textsectiondblclick);
			clickJSMouseMove(textsection, textsection.getText());

			try {
				Robot robot = new Robot();
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				robot = null;
			} catch (AWTException e) {

				e.printStackTrace();
			}

		} else {

			WebElement elem = driver.findElement(By.xpath("/html/body/aside"));
			String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";
			((JavascriptExecutor) driver).executeScript(js, elem);

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to SetDataInTextBox using javascript
	// # Function Name : SetDataInTextBox_javascript     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean setDataInTextBoxJavaScript(WebElement element, String strText) {
		try {
			((JavascriptExecutor) driver).executeScript("argument[0].value='"
					+ strText + "'");

			if (element.getText().contains(strText))
				return true;
			else
				return false;

		} catch (Exception e) {
			throw new FrameworkException("Set value " + strText, strText
					+ " is not set");

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : Click_MouseMove_Action(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickJSMouseMove(WebElement ele, String name) {

		try {
			Actions action = new Actions(driver);
			action.moveToElement(ele).perform();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on " + name, "Clicked on " + name,
					Status.PASS);
			this.sleep(15000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : Click_MouseMove_Action(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickJS(WebElement ele) {

		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on " + ele.getText(), "Clicked on "
					+ ele.getText(), Status.PASS);
			this.sleep(6000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : Click_MouseMove_Action(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickMouseMoveAction(WebElement element, String linkText) {

		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().perform();
			report.updateTestLog("Click on " + linkText, "Clicked on "
					+ linkText, Status.PASS);
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to focus a window
	// # Function Name : windowFocus
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void windowFocus() {
		if (browsername.contains("internet")) {

		} else {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("window.focus()");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set data in a text field
	// # Function Name : SetDataInTextBox_Clear  
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Boolean setDataInTextBoxClear(WebElement Obj, String strVal,
			String FieldName) {
		try {

			WebElement eltTextBoxId = Obj;
			// this.ScrollToView(eltTextBoxId);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", Obj);

			if (eltTextBoxId != null) {
				if (browsername.contains("internet")) {
					int i = 0;
					int len = Obj.getAttribute("value").length();
					for (i = 0; i < len; i++) {
						Obj.sendKeys(Keys.BACK_SPACE);
					}
					Obj.sendKeys(strVal);

				} else {
					eltTextBoxId.clear();
					this.sleep(2000);
					eltTextBoxId.click();
					eltTextBoxId.sendKeys(strVal);
					eltTextBoxId.sendKeys(Keys.DOWN);

				}
				report.updateTestLog("Set value " + strVal + " in the "
						+ FieldName + " text box", strVal + " is set in the "
						+ FieldName + " text box", Status.DONE);
				return true;
			} else {
				throw new FrameworkException("Set value " + strVal + " in the "
						+ FieldName + " text box", strVal
						+ " is not set in the " + FieldName + "text box");

			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
			return false;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Logout from the application
	// # Function Name : Logout     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public SignIn logout() {

		WebElement btnMore = this.isExist(UIMAP_Home.btnTitleMore, 100);
		if (btnMore != null) {

			if ((browsername.contains("internet")))
				this.clickMethod(btnMore, "More");
			else
				this.clickLinkWithWebElementWithWait(btnMore, "More");
		}

		WebElement lnkSignOut = this.isExist(
				By.linkText(UIMAP_Home.lnkTextSignOut), 100);

		if (lnkSignOut == null || !lnkSignOut.isDisplayed()) {
			btnMore = this.isExist(UIMAP_Home.btnTitleMore, 100);
			if (btnMore != null) {

				if ((browsername.contains("internet")))
					this.clickMethod(btnMore, "More");
				else
					this.clickLinkWithWebElementWithWait(btnMore, "More");
			}
			lnkSignOut = this.isExist(By.linkText(UIMAP_Home.lnkTextSignOut),
					100);
		}

		if (lnkSignOut != null)
			if ((browsername.contains("internet")))
				this.clickJS(lnkSignOut, "Sign Out");
			else
				this.clickLinkWithWebElementWithWait(lnkSignOut, "Sign Out");

		WebElement btnIdLogin = this.isExistNegative(
				UIMAP_SignIn.txtSignInHeader, 10);
		if (btnIdLogin != null
				&& driver.getCurrentUrl().toLowerCase()
						.contains(UIMAP_SignIn.txtSigninTitleMsg)) {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is displayed", Status.PASS);
		} else {
			report.updateTestLog("Verify Logout",
					"Sign In to Lexis Advance screen is NOT displayed",
					Status.WARNING);
		}

		return new SignIn(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to set data in a text field
	// # Function Name : SetDataInTextBox_Robot
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void setDataInTextBoxRobot(WebElement txtBox, String txtToEnter) {
		try {

			txtBox.clear();
			this.clickJS(txtBox);

			String word = txtToEnter.toUpperCase();
			char ch1;
			for (int i = 0; i < word.length(); i++) {
				ch1 = word.charAt(i);
				Robot robot = new Robot();
				switch (ch1) {
				case 'A': {
					robot.keyPress(KeyEvent.VK_A);
					break;
				}
				case 'B': {
					robot.keyPress(KeyEvent.VK_B);
					break;
				}
				case 'C': {
					robot.keyPress(KeyEvent.VK_C);
					break;
				}
				case 'D': {
					robot.keyPress(KeyEvent.VK_D);
					break;
				}
				case 'E': {
					robot.keyPress(KeyEvent.VK_E);
					break;
				}
				case 'F': {
					robot.keyPress(KeyEvent.VK_F);
					break;
				}
				case 'G': {
					robot.keyPress(KeyEvent.VK_G);
					break;
				}
				case 'H': {
					robot.keyPress(KeyEvent.VK_H);
					break;
				}
				case 'I': {
					robot.keyPress(KeyEvent.VK_I);
					break;
				}
				case 'J': {
					robot.keyPress(KeyEvent.VK_J);
					break;
				}
				case 'K': {
					robot.keyPress(KeyEvent.VK_K);
					break;
				}
				case 'L': {
					robot.keyPress(KeyEvent.VK_L);
					break;
				}
				case 'M': {
					robot.keyPress(KeyEvent.VK_M);
					break;
				}
				case 'N': {
					robot.keyPress(KeyEvent.VK_N);
					break;
				}
				case 'O': {
					robot.keyPress(KeyEvent.VK_O);
					break;
				}
				case 'P': {
					robot.keyPress(KeyEvent.VK_P);
					break;
				}
				case 'Q': {
					robot.keyPress(KeyEvent.VK_Q);
					break;
				}
				case 'R': {
					robot.keyPress(KeyEvent.VK_R);
					break;
				}
				case 'S': {
					robot.keyPress(KeyEvent.VK_S);
					break;
				}
				case 'T': {
					robot.keyPress(KeyEvent.VK_T);
					break;
				}
				case 'U': {
					robot.keyPress(KeyEvent.VK_U);
					break;
				}
				case 'V': {
					robot.keyPress(KeyEvent.VK_V);
					break;
				}
				case 'W': {
					robot.keyPress(KeyEvent.VK_W);
					break;
				}
				case 'X': {
					robot.keyPress(KeyEvent.VK_X);
					break;
				}
				case 'Y': {
					robot.keyPress(KeyEvent.VK_Y);
					break;
				}
				case 'Z': {
					robot.keyPress(KeyEvent.VK_Z);
					break;
				}
				case '-': {
					robot.keyPress(KeyEvent.VK_MINUS);
				}
				}
				robot = null;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to window
	// # Function Name : SwitchToWindow1
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public Boolean switchToWindow1(String strWindowName) {
		try {
			Boolean blnCheckSwitch = false;

			String winHandleBefore = driver.getWindowHandle();
			if (driver.getWindowHandles().size() > 0) {
				this.sleep(2000);
				String[] popUpHandler = driver.getWindowHandles().toArray(
						new String[0]);

				for (int i = 0; i < popUpHandler.length; i++) {
					try {
						try {
							driver.manage().timeouts()
									.pageLoadTimeout(1, TimeUnit.SECONDS);
						} catch (NoSuchWindowException e) {
							System.out.println(e.toString());
						}
						driver.switchTo().window(popUpHandler[i]);
					} catch (TimeoutException e) {
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println("timeout exception occured");
					}
					driver.manage().timeouts()
							.pageLoadTimeout(220, TimeUnit.SECONDS);
					this.sleep(2000);
					Boolean a = driver.getCurrentUrl().contains(strWindowName);

					if (a == true) {

						blnCheckSwitch = true;
						break;
					}
				}

			} else {
				System.out.println("no Secondary window opened");
			}

			if (!blnCheckSwitch)
				driver.switchTo().window(winHandleBefore);

			return blnCheckSwitch;
		} catch (Exception e) {
			System.out.println(e.toString());

			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the existence of the file
	// # Function Name : FileExists
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void fileExists(String strFilePath, String strFileName) {
		Boolean blnFlag = true;
		try {

			File folder = new File(strFilePath);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
				if (file.isFile()) {
					String[] filename = file.getName().split("\\.(?=[^\\.]+$)"); // split
																					// filename
																					// from
																					// it's
																					// extension
					if (filename[0].contains(strFileName)) // matching defined
															// filename
					{
						blnFlag = true;
						report.updateTestLog("Verify the file " + filename[0]
								+ "." + filename[1]
								+ " is present in the path " + strFilePath,
								"File  " + filename[0] + "." + filename[1]
										+ " is present in the path "
										+ strFilePath, Status.PASS);

						break;
					}

				}
			}
			if (!blnFlag)
				report.updateTestLog("Verify the file " + strFileName
						+ " is present in the path " + strFilePath, "File  "
						+ strFileName + " is not present in the path "
						+ strFilePath, Status.FAIL);

		} catch (Exception e) {
			report.updateTestLog(
					"Verify the file is present in the path " + strFilePath,
					"Downloaded File is not present in the path " + strFilePath,
					Status.FAIL);
			System.out.println(e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to generate a random number
	// # Function Name : Generaterandomnumber
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public int generaterandomnumber() {
		Random t = new Random();
		int result;

		// create random integers in [0, 1000]
		result = t.nextInt(1000);

		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : Click_MouseMove_Action(WebElement element)   
	// # Author : Pratik
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickJSNoLog(WebElement ele) {

		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", ele);

			this.sleep(20000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write a word in a file
	// # Function Name : FileWrite 
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public void fileWrite(String filePath, String value) {

		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(filePath);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write("");
			bufferedWriter.write(value);

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + filePath + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to write a word in a file
	// # Function Name : FileWrite 
	// # Author : Uma
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Boolean fileRead(String filePath, String value) {
		Boolean isComplete = false;
		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(filePath);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains(value))
					isComplete = true;
				System.out.println(line);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filePath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + filePath + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return isComplete;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check whether outlook process is
	// opened
	// # Function Name : checkOutlookProcess
	// # Author : Aravind M
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Boolean checkOutlookProcess() {
		Boolean isExists = false, isExistsChk = false;

		this.sleep(10000);

		try {
			String line;
			Process p = Runtime.getRuntime().exec(
					System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = input.readLine()) != null) {

				if (line.toLowerCase().contains("outlook.exe")) {
					isExists = true;
					break;
				}
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		if (!isExists) {

			this.sleep(10000);

			try {
				String line;
				Process p = Runtime.getRuntime().exec(
						System.getenv("windir") + "\\system32\\"
								+ "tasklist.exe");
				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {

					if (line.toLowerCase().contains("outlook.exe")) {
						isExistsChk = true;
						break;
					}
				}
				input.close();
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

		if (isExists || isExistsChk)
			isExists = true;

		return isExists;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to kill Outlook Process this
	// # Function Name : killOutlookProcessThread
	// # Author : Aravind M
	// # Date Created : May'15
	// #*****************************************************************************************************************************
	public Boolean killOutlookProcessThread() {
		Boolean isComplete = false;
		try {
			String line;
			Process p = Runtime.getRuntime().exec(
					System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = input.readLine()) != null) {

				if (line.toLowerCase().contains("outlook.exe")) {
					try {
						Runtime.getRuntime().exec(
								"taskkill /IM OUTLOOK.exe /T /F");
						isComplete = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return isComplete;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List in workfolders page
	// # Function Name : Select_FromList_ShareFolder(WebElement element,String
	// strValue)   
	// # Author : Divakar
	// # Date Created : May'15
	// # Comments: This function only works for Work folders page - Share folder
	// action
	// #*****************************************************************************************************************************

	public Boolean selectFromListFolder(WebElement element, String strValue) {
		Boolean blnFlag = false;
		try {
			List<WebElement> listItems = isExistList(element, By.tagName("li"),
					20);
			// List<WebElement> listItems =
			// isExist_List(By.cssSelector("li[class*='normal']"), 20);
			for (WebElement item : listItems) {
				if (item.getText().toUpperCase()
						.contains(strValue.toUpperCase())) {

					item.getText().toUpperCase();

					WebElement button = isExistNegative(item,
							By.tagName("button"), 2);
					if (browsername.contains("internet"))
						if (button != null) {
							this.clickJS(button, item.getText());// not working
																	// to select
																	// actions
																	// dropdownlist
																	// in IE
						} else
							this.clickJS(item, item.getText());
					else {
						if (button != null) {
							this.clickJS(button, item.getText());
						} else
							this.clickJS(item, item.getText());
					}
					this.sleep(5000);
					blnFlag = true;
					break;
				}

			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("Select Value " + strValue, strValue
					+ " is not selected");

		}
		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify the existance of the file
	// # Function Name : fileExistsBool(WebElement element,String
	// strValue)   
	// # Author : Divakar
	// # Date Created : May'15
	// # Comments: This function only works for Work folders page - Share folder
	// action
	// #*****************************************************************************************************************************
	public boolean fileExistsBool(String strFilePath, String strFileName) {
		Boolean blnFlag = false;
		try {

			File folder = new File(strFilePath);
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile()) {

						if (file.getName().contains(strFileName)) // matching
																	// defined
																	// filename
						{
							blnFlag = true;

							break;
						}

					}
				}

			}
		} catch (Exception e) {

			System.out.println(e.toString());

		}
		return blnFlag;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click on the element by mouse over
	// # Function Name : click_JS(WebElement element)   
	// # Author : Harish
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickWithSmallWaitJS(WebElement ele, String name) {

		try {

			windowFocus();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", ele);
			executor.executeScript("arguments[0].click();", ele);
			report.updateTestLog("Click on button " + name,
					"Clicked on button " + name, Status.PASS);
			this.sleep(1000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {

			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to click search button
	// # Function Name : clickSearchButton()     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public int clickSearchButton(WebElement elnt, String strBtnName) {

		try {

			windowFocus();
			if (browsername.contains("internet")) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].focus();", elnt);
				executor.executeScript("arguments[0].click();", elnt);
				report.updateTestLog("Click on button " + strBtnName,
						"Clicked on button " + strBtnName, Status.PASS);
			} else
				clickMethod(elnt, strBtnName);

			this.sleep(50000);
			return 1;

		} catch (StaleElementReferenceException ex) {
			driver.navigate().refresh();
			System.out.println(ex.getMessage());
			return 0;
		} catch (Exception e) {

			System.out.println(e.toString());
			return 0;
		}
	}

	// //
	// #*****************************************************************************************************************************
	// // # Function Description : Function to wait
	// // # Function Name : sleep()     
	// // # Author : Uma
	// // # Date Created : Jun'15
	// //
	// #*****************************************************************************************************************************
	//
	// public void sleep(int count) {
	// int innerCount = 0;
	// int outerCount = 0;
	// if (browsername.contains("internet")) {
	//
	// outerCount = 50000;
	// innerCount = 20;
	//
	// } else {
	// outerCount = 150000;
	// innerCount = 10;
	// }
	//
	// count = count * outerCount;
	// for (int i = 0; i <= count; i++) {
	// for (int j = 0; j <= innerCount; j++) {
	// j = j + 1;
	// }
	// i = i + 1;
	// }
	//
	// }

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait
	// # Function Name : sleep()     
	// # Author : Uma
	// # Date Created : Jun'15
	// #*****************************************************************************************************************************

	public void sleep(int count) {

		for (int j = 0; j <= (count / 1000); j++) {
			
			if(browsername.contains("internet"))
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
	
					e.printStackTrace();
				}
			}
			else
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to wait for webelement
	// # Function Name : waitForObjectExists()     
	// # Author : Uma
	// # Date Created : July'15
	// #*****************************************************************************************************************************

	public void waitForObjectExists(By locator) {

		for (int second = 0; second < 10; second++) {
			try {

				List<WebElement> lElements = driver.findElements(locator);
				if (lElements != null && lElements.size() > 0)
					return;
			} catch (Exception e) {
				System.out.println("Exception " + e);
			}
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify_FromList_span
	// # Function Name : Verify_FromList_span     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public Boolean verifyFromListSpan(WebElement element, String strValue) {
		Boolean blnFlag = false;

		List<WebElement> listItems = isExistList(element, By.tagName("li"), 20);
		for (WebElement item : listItems) {
			List<WebElement> btn = isExistList(item, By.tagName("span"), 20);
			for (WebElement item2 : btn) {
				if (item2.getText().toLowerCase()
						.contains(strValue.toLowerCase())) {
					blnFlag = true;
					break;
				}
			}
			if (blnFlag)
				break;
		}

		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to delete all pdf files
	// # Function Name : FileExists_DeletePdfFiles
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void fileExistsDeletePdfFiles(String strFilePath) {
		try {
			File folder = new File(strFilePath);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
				if (file.isFile()) {
					String filename = file.getName();// .split("\\.(?=[^\\.]+$)");
														// // split
														// filename
														// from
														// it's
														// extension
					if (filename.contains("pdf")) // matching defined
													// filename
					{
						file.delete();

					}
				}
			}

		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check/uncheck the checkbox
	// # Function Name : SetCheckBoxEWA    
	// # Author : Aravind
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public boolean setCheckBoxEWA(WebElement obj, boolean isCheck) {
		boolean result = false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			if (obj.isSelected() != isCheck) {
				executor.executeScript("arguments[0].focus();", obj);
				if (browsername.contains("internet")) {

					executor.executeScript("arguments[0].click();", obj);
				} else if (browsername.contains("chrome")) {

					executor.executeScript("arguments[0].click();", obj);
				} else {
					obj.click();
				}
				this.sleep(400);
				result = true;
			} else {
				executor.executeScript("arguments[0].focus();", obj);
				if (browsername.contains("internet")) {

					executor.executeScript("arguments[0].click();", obj);
				} else if (browsername.contains("chrome")) {

					executor.executeScript("arguments[0].click();", obj);
				} else {
					obj.click();
				}
				this.sleep(400);
				result = true;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			result = false;
		}
		return result;
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to check/uncheck the checkbox
	// # Function Name : SetCheckBoxEWA    
	// # Author : Aravind
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************
	public void focusControlJS(WebElement ele) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].focus();", ele);

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to switch to window by title
	// # Function Name : SwitchToWindowByTitle    
	// # Author : Anbarasan
	// # Date Created : Aug'15
	// #*****************************************************************************************************************************
	public Boolean switchToWindowByTitle(String titleName) {
		try {
			Boolean blnCheckSwitch = false;
			// if (!browsername.contains("internet")) {

			if (driver.getWindowHandles().size() > 0) {

				this.sleep(2000);
				String[] popUpHandler = driver.getWindowHandles().toArray(
						new String[0]);

				for (int i = 0; i < popUpHandler.length; i++) {
					try {
						driver.manage().timeouts()
								.pageLoadTimeout(1, TimeUnit.SECONDS);
						driver.switchTo().window(popUpHandler[i]);
					} catch (TimeoutException e) {
						driver.manage().timeouts()
								.pageLoadTimeout(220, TimeUnit.SECONDS);
						System.out.println("timeout exception occured");
					}
					this.sleep(2000);
					Boolean a = driver.getTitle().toLowerCase()
							.contains(titleName.toLowerCase());

					if (a == true) {

						blnCheckSwitch = true;
						break;
					}
				}

			} else {
				System.out.println("no Secondary window opened");
			}

			// } else {
			// driver.close();
			// report.updateTestLog("Secondary browser",
			// "Secondary browser window is not supported in IE", Status.FAIL);
			// }
			return blnCheckSwitch;
		} catch (Exception e) {
			System.out.println(e.toString());

			return false;
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List in workfolders page
	// # Function Name : Select_FromList_ShareFolder(WebElement element,String
	// strValue)   
	// # Author : Divakar
	// # Date Created : May'15
	// # Comments: This function only works for Work folders page - Share folder
	// action
	// #*****************************************************************************************************************************

	public Boolean selectFromListFolderNoParent(WebElement element,
			String strValue) {
		Boolean blnFlag = false;
		try {
			// List<WebElement> listItems = isExist_List(element,
			// By.tagName("li"), 20);
			this.sleep(4000);
			List<WebElement> listItems = isExistList(
					By.cssSelector("li[class*='normal']"), 20);
			for (WebElement item : listItems) {
				if (item.getText().toUpperCase()
						.contains(strValue.toUpperCase())) {
					//

					this.highlightElement(item);
					this.clickJS(item, item.getText());// not working
														// to select
					// actions
					// dropdownlist

					/*
					 * try { driver.manage().timeouts().pageLoadTimeout(1,
					 * TimeUnit.SECONDS);
					 * 
					 * item.click();
					 * 
					 * 
					 * } catch(TimeoutException e) {
					 * 
					 * driver.manage().timeouts().pageLoadTimeout(220,
					 * TimeUnit.SECONDS); }
					 */this.sleep(5000);
					blnFlag = true;
					break;
				}

			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			throw new FrameworkException("Select Value " + strValue, strValue
					+ " is not selected");

		}
		return blnFlag;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to select from List in workfolders page
	// # Function Name : clickMethod
	// strValue)   
	// # Author : Divakar
	// # Date Created : May'15
	// # Comments: This function only works for Work folders page - Share folder
	// action
	// #*****************************************************************************************************************************

	public void clickMethod(WebElement ele, String name) {

		try {
			if (browsername.contains("chrome"))
				this.clickButtonParentWithWait(ele, name);
			else {
				windowFocus();
				try {
					driver.manage().timeouts()
							.pageLoadTimeout(1, TimeUnit.SECONDS);
					ele.click();
				} catch (TimeoutException ex) {

				}
				driver.manage().timeouts()
						.pageLoadTimeout(220, TimeUnit.SECONDS);
				report.updateTestLog("Click on button " + name,
						"Clicked on button " + name, Status.PASS);
				this.sleep(3000);
			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			// report.updateTestLog("Click on button " + name,
			// " Not Clicked on button " + name, Status.FAIL);
			System.out.println(e.toString());

		}

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Click on Browser Forward
	// # Function Name : clickBrowserForward     
	// # Author : Anbu
	// # Date Created : Sep'15
	// #*****************************************************************************************************************************

	public void clickBrowserForward() {
		try {

			String PrevURL = driver.getCurrentUrl();
			String currentURL = null;
			if (browsername.contains("chrome")
					|| browsername.contains("firefox")) {
				driver.navigate().forward();
				this.sleep(7000);
			} else {
				this.sleep(15000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_ALT);
				// Actions builder = new Actions(driver);
				// builder.sendKeys(Keys.BACK_SPACE).perform();
				this.sleep(30000);
			}
			report.updateTestLog("Click on Browser Forward",
					"Clicked on Browser Forward", Status.PASS);

			int counter = 0;
			do {
				counter = counter + 1;
				currentURL = driver.getCurrentUrl();
				if (currentURL.equals(PrevURL))
					this.sleep(5000);
			} while (currentURL.equals(PrevURL) && counter <= 40);

			if (currentURL.equals(PrevURL)) {
				if (browsername.contains("internet")) {
					driver.navigate().forward();
					this.sleep(7000);
				} else {
					this.sleep(3000);
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_RIGHT);
					robot.keyRelease(KeyEvent.VK_RIGHT);
					robot.keyRelease(KeyEvent.VK_ALT);
					// Actions builder = new Actions(driver);
					// builder.sendKeys(Keys.BACK_SPACE).perform();
					this.sleep(15000);
				}

				counter = 0;
				do {
					counter = counter + 1;
					currentURL = driver.getCurrentUrl();
					if (currentURL.equals(PrevURL))
						this.sleep(5000);
				} while (currentURL.equals(PrevURL) && counter <= 40);

			}
		} catch (StaleElementReferenceException e) {
			// return 0;
			System.out.println(e.toString());
		} catch (Exception e) {

			throw new FrameworkException("Click on Browser Forward",
					"Not Clicked on Browser Forward");

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to clickButton_Parent_WithWait
	// # Function Name : clickButtonParentWithWaitWithFocus     
	// # Author : Uma
	// # Date Created : Jan'15
	// #*****************************************************************************************************************************

	public void clickButtonParentWithWaitWithFocus(WebElement elnt,
			String strBtnName) {
		try {

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus();", elnt);

			this.clickMouseMoveAction(elnt, strBtnName);
			this.sleep(5000);

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

}