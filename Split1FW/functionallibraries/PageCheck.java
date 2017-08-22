package functionallibraries;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import supportlibraries.*;

public class PageCheck extends ReusableLibrary {

	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	private String browsername = cap.getBrowserName();
	CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	private String signoutUrl = properties.getProperty("signout_url");

	public PageCheck(ScriptHelper scriptHelper) {

		super(scriptHelper);

	}

	// Return 1 for Unexpected error
	// Return 2 for Unavailable
	// Return 3 for Pricing issue
	public int positiveCheck(WebDriver driver, String TextCheck, String Flow) {
		// for (int i = 0; i < 200; i++) {
		System.out.println("Waiting.." + Flow);
		if (Flow.contains("Citation")) {
			int value = negativeCheck(driver);
			sleepWait(1000);
			if (value == 1) {
				if (Flow.contains("Home")) {
					System.out.println("Few Services throw expected error in Home");
					// if (i > 10)
					return 1;
				} else {
					return 1;
				}
			} else if (value == 2) {
				if (Flow.contains("Home")) {
					System.out.println("Few Services are unavailable in Home");
					// if (i > 10)
					return 2;
				} else {
					return 2;
				}
			} else if (value == 3) {
				System.out.println("Pricing Issue");
				return 3;
			}

			if (driver.getTitle().contains(" results for")) {
				System.out.println("Your Citation resulted in results");

				return 0;
			}
		} else if (Flow.contains("Ajax")) {
			int check = negativeAjaxCheck(driver);
			if (check == 1) {
				System.out.println(Flow + " Ajax Service is Unexpected");
				return 1;
			} else if (check == 2) {
				System.out.println(Flow + " Ajax Service is unavailable");
				return 2;
			} else if (check == 3) {
				System.out.println("Pricing Issue");
				return 3;
			}

		} else {
			int value = negativeCheck(driver);
			sleepWait(1000);
			if (value == 1) {
				if (Flow.contains("Home")) {
					System.out.println("Few Services throw expected error in Home");
					// if (i > 10)
					return 1;
				} else {
					return 1;
				}

			} else if (value == 2) {
				if (Flow.contains("Home")) {
					System.out.println("Few Services are unavailable in Home");
					// if (i > 10)
					return 2;
				} else {
					return 2;
				}
				// break;
			} else if (value == 3) {
				System.out.println("Pricing Issue");
				return 3;
			}

			sleepWait(1000);
			if (value != 1) {
				for (int i = 0; i < 20; i++) {
					if (browsername.contains("firefox")) {
						if (JsFirefox.JsgetPageSource(driver).contains(TextCheck))
							break;
					} else {
						if ((driver.getPageSource().contains(TextCheck)))
							break;
						else
							continue;
					}
				}
			}
		}

		// System.out.println("Total Bytes For the action "+Flow+" is "+driver.getPageSource().toString().length());
		return 0;

	}

	public int negativeCheck(WebDriver driver) {
		String temp = null;
		if (browsername.contains("firefox"))
			temp = JsFirefox.JsgetPageSource(driver);
		else
			temp = driver.getPageSource();
		// if
		// (temp.contains("has encountered an error processing your request"))
		// return 1;
		if (temp.contains("Unexpected Error")) {
			// report.updateTestLog("Error", "Unexpected Error", Status.FAIL);

			return 0;
		} else if (temp.contains("Service Temporarily Unavailable"))
			return 1;
		else if (temp.contains("service is temporarily unavailable"))
			return 2;

		else if (temp.contains("product you are trying to reach is currently unavailable"))
			return 2;
		else if (temp.contains("Service is currently unavailable."))
			return 2;
		else if (temp.contains("pricing information is currently unavailable"))
			return 3;
		else if (temp.contains("The ID or password you entered is incorrect"))
			return 4;
		else if (temp.contains("We are unable to complete your request at this time")) {
			report.updateTestLog("Error", "We are unable to complete your request at this time", Status.FAIL);

			return 1;
		} else if (temp.contains("Service Unavailable"))
			return 2;
		else
			return 0;
	}

	public int negativeAjaxCheck(WebDriver driver) {
		String temp = null;
		if (browsername.contains("firefox"))
			temp = JsFirefox.JsgetPageSource(driver);
		else
			temp = driver.getPageSource();
		if (temp.contains("pricing information is currently unavailable"))
			return 3;
		else if (temp.contains("<h1>This service is temporarily unavailable"))
			return 2;
		else if (temp.contains("<h1>The list service is temporarily unavailable"))
			return 2;
		else if (temp.contains("<h1>Unexpected Error"))
			return 1;
		// else if
		// (temp.contains("has encountered an error processing your request"))
		// return 1;
		else if (temp.contains("Service Unavailable"))
			return 1;

		else
			return 0;
	}

	public int handleFlow(WebDriver driver, int check) {
		switch (check) {
		case 1:
			System.out.println("Unexpected Error");
			System.out.println("Terminating Iteration.");
			// SeleniumAdvance.TakeScreenshot(driver, "Unexpected_Error");
			if (!driver.getPageSource().contains("Sign In")) {
				sleepWait(3000);
				driver.navigate().to(signoutUrl);
				throw new FrameworkException("Unexpected Error");
			}
			return 1;
		case 2:
			System.out.println("Service_Unavailable");
			System.out.println("Terminating Iteration.");
			// report.(driver, "Service_Unavailable");
			if (!driver.getPageSource().contains("Sign In")) {
				sleepWait(3000);
				report.updateTestLog("Error in the page", "Service Unavailable", Status.FAIL);
				driver.navigate().to(signoutUrl);
				throw new FrameworkException("Service Unavailable");
			}
			return 1;
		case 3:
			// SeleniumAdvance.TakeScreenshot(driver, "Pricing_Issue");
			return 2;
		default:
			return 0;
		}
	}

	// Handles Ajax calls Waits till the spinner gets completed and focus the
	// element for the given xpath
	public int ajaxElementCheck(WebDriver driver, String Xpath) {
		// try {
		// WebElement spinner =
		// driver.findElement(By.xpath(properties.getProperty("xSpinner")));
		//
		// int divident = 5000;
		//
		// while (spinner.isDisplayed() && divident <= 25000)
		//
		// {
		//
		// divident += 5000;
		//
		// }
		// System.out.println("Total Time taken for this ajax call");
		//
		// } catch (Exception e) {
		// System.out.println("Spinner Loading Problem");
		// }
		// WebElement element = null;
		// JavascriptExecutor executor = (JavascriptExecutor) driver;
		// for (int i = 0; i < 20; i++) {
		// // try {
		// // int temp = this.handleFlow(driver,
		// this.NegativeAjaxCheck(driver));
		// // if (temp != 0)
		// // return temp;
		// // this.DocumentState(driver);
		// // element = driver.findElement(By.xpath(Xpath));
		// // System.out.println("Focusing On Given Xpath");
		// // executor.executeScript("arguments[0].focus()", element);
		// // i = 20;
		// // } catch (Exception e) {
		// // System.out.println("Ajax Calls Not Yet Complete");
		// // continue;
		// // }
		// }
		return 0;
		// executor.executeScript("arguments[0].blur()", element);
	}

	public static void checkAlert(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			// exception handling
		}
	}

	public void documentState(WebDriver driver) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String state = null;
			boolean temp = false;
			do {
				state = executor.executeScript("return document.readyState;").toString();
				System.out.println("Waiting for Page to load. Current State: " + state);
				if (state.contains("loaded"))
					temp = true;
				else if (state.contains("complete"))
					temp = true;
				else
					temp = false;
				sleepWait(1000);
			} while (!temp);
		} catch (Exception e) {
			System.out.println("JSError Recorded: commonLibrary Time 10 Seconds");

			sleepWait(1000);

			documentState(driver);
		}
		return;

	}

	public int ajaxWait(WebDriver driver) {
		// try {
		// WebElement spinner =
		// commonLibrary.isExist(By.xpath(properties.getProperty("xSpinner")),20);
		//
		// int divident = 5000;
		// while (divident <= 300000 && spinner.isDisplayed()) {
		// commonLibrary.sleep(50000);
		// System.out.println("Spinner - Sensed Ajax call is currently being Processed");
		// divident += 50;
		//
		// }
		// System.out.println("Total Time taken for this ajax call ");
		//
		// } catch (Exception e) {
		// System.out.println("Spinner Loading Problem");
		// }
		return this.handleFlow(driver, this.negativeAjaxCheck(driver));
	}

	public static void sleepWait(long timeToWait) {
		timeToWait = timeToWait * 4;
		for (int i = 0; i <= timeToWait; i++) {
			i = i + 1;
		}
		return;

	}
}
