package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

public class JsFirefox {

	public static void JsXpathClick(WebDriver driver, String Xpath) {
		for (int i = 1; i < 120; i++) {
			try {
				sleepWait(1000);
				while (!(driver.findElement(By.xpath(Xpath)).isDisplayed()))
					System.out.println("Finding Xpath Var " + Xpath + " To Click");
				WebElement element = driver.findElement(By.xpath(Xpath));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				System.out.println("Clicked Xpath: " + Xpath);
				JsDocumentState(driver);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);
			}
		}
	}

	public static void JsXpathEnter(WebDriver driver, String Xpath, String Text) {
		for (int i = 1; i < 120; i++) {
			try {
				while (!driver.findElement(By.xpath(Xpath)).isDisplayed())
					System.out.println("Finding Xpath Var " + Xpath + " To Enter " + Text);
				WebElement element = driver.findElement(By.xpath(Xpath));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].value=arguments[1];", element, Text);
				System.out.println("Entered " + Text);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);
			}
		}
	}

	public static void JsXpathClear(WebDriver driver, String Xpath) {
		for (int i = 1; i < 120; i++) {
			try {
				while (!driver.findElement(By.xpath(Xpath)).isDisplayed())
					System.out.println("Finding Xpath Var " + Xpath + " To Clear");
				WebElement element = driver.findElement(By.xpath(Xpath));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].value='';", element);
				System.out.println("Cleared " + Xpath);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);
			}
		}

	}

	public static void JsCSSClick(WebDriver driver, String CSS) {
		for (int i = 1; i < 120; i++) {
			try {
				sleepWait(1000);
				while (!(driver.findElement(By.cssSelector(CSS)).isDisplayed()))
					System.out.println("Finding CSS Var " + CSS);
				WebElement element = driver.findElement(By.cssSelector(CSS));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				System.out.println("Clicked CSS: " + CSS);
				JsDocumentState(driver);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);
			}
		}
	}

	public static void JsCSSEnter(WebDriver driver, String CSS, String Text) {
		for (int i = 1; i < 120; i++) {
			try {
				while (!driver.findElement(By.cssSelector(CSS)).isDisplayed())
					System.out.println("Finding CSS Var " + CSS + " To Enter " + Text);
				WebElement element = driver.findElement(By.cssSelector(CSS));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].value=arguments[1];", element, Text);
				System.out.println("Entered " + Text);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);
			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);
			}
		}
	}

	public static void JsCSSClear(WebDriver driver, String CSS) {
		for (int i = 1; i < 120; i++) {
			try {
				while (!driver.findElement(By.cssSelector(CSS)).isDisplayed())
					System.out.println("Finding CSS Var " + CSS + " To Clear");
				WebElement element = driver.findElement(By.cssSelector(CSS));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].value='';", element);
				System.out.println("Cleared " + CSS);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + CSS);
			}
		}
	}

	public static void JsIdClick(WebDriver driver, String Id) {
		for (int i = 1; i < 120; i++) {
			try {
				sleepWait(1000);
				while (!(driver.findElement(By.id(Id)).isDisplayed()))
					System.out.println("Finding Id Var " + Id);
				WebElement element = driver.findElement(By.id(Id));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				System.out.println("Clicked Id: " + Id);
				JsDocumentState(driver);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Id);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Id);
			}
		}
	}

	public static void JsPartialLinkClick(WebDriver driver, String Link) {
		for (int i = 1; i < 120; i++) {
			try {
				sleepWait(1000);
				while (!driver.findElement(By.partialLinkText(Link)).isDisplayed())
					System.out.println("Finding Link Text " + Link);
				WebElement element = driver.findElement(By.partialLinkText(Link));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				System.out.println("Clicked Link: " + Link);
				JsDocumentState(driver);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Link);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Link);
			}
		}
	}

	public static void JsDriverLog(WebDriver driver) {
		Logs logs = driver.manage().logs();
		LogEntries logEntries = logs.get(LogType.DRIVER);

		for (LogEntry logEntry : logEntries) {
			System.out.println(logEntry.getMessage());
		}
	}

	public static void JsDocumentState(WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		String state = null;
		boolean temp = false;
		do {
			state = executor.executeScript("return document.readyState;").toString();
			System.out.println("Waiting for Page to load. Current State: " + state);
			if (state.contains("loaded"))
				temp = true;
			else if (state.contains("interactive"))
				temp = true;
			else if (state.contains("complete"))
				temp = true;
			else
				temp = false;
			sleepWait(1000);
		} while (!temp);
		return;
	}

	public static String JsgetPageSource(WebDriver driver) {
		JsDocumentState(driver);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		return executor.executeScript("return document.body.innerHTML;").toString();
	}

	public static String JsgetPageURl(WebDriver driver) {
		JsDocumentState(driver);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		return executor.executeScript("return window.location.href;").toString();
	}

	public static void JsXpathSelect(WebDriver driver, String Xpath, String Text) {
		for (int i = 1; i < 120; i++) {
			try {
				while (!driver.findElement(By.xpath(Xpath)).isDisplayed())
					System.out.println("Finding Xpath Var " + Xpath + " To Select " + Text);
				WebElement element = driver.findElement(By.xpath(Xpath));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("var val = arguments[1]; var sel = arguments[0]; var opts = sel.options;for(var opt, j = 0; opt = opts[j]; j++) {        if(opt.value == val) {sel.selectedIndex = j;break;}}", element, Text);
				System.out.println("Selected " + Text);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Checking Again " + Xpath);
			}
		}
	}

	public static void JsNavigate(WebDriver driver, String url) {
		for (int i = 1; i < 120; i++) {
			try {
				sleepWait(1000);
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("window.location.href=arguments[0];", url);
				System.out.println("Navigated to  " + url);
				JsDocumentState(driver);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Naigating Again");

			} catch (Exception e) {
				System.out.println("Exception Occured RePerforming " + e.getMessage());
				System.out.println("Naigating Again");
			}
		}
	}

	public static void sleepWait(long timeToWait) {
		timeToWait = timeToWait * 40000;
		for (int i = 0; i <= timeToWait; i++) {
			i = i + 1;
		}
		return;

	}
}
