package com.cognizant.framework.selenium;

import com.cognizant.framework.TestParameters;
import org.openqa.selenium.Platform;


/**
 * Class to encapsulate various input parameters required for each test script
 * @author Cognizant
 */
public class SeleniumTestParameters extends TestParameters
{
	public SeleniumTestParameters(String currentScenario, String currentTestcase)
	{
		super(currentScenario, currentTestcase);
	}
	
	
	private Browser browser;
	/**
	 * Function to get the browser for a specific test
	 * @return The browser
	 */
	public Browser getBrowser()
	{
		return browser;
	}
	/**
	 * Function to set the browser for a specific test
	 * @param browser The browser
	 */
	public void setBrowser(Browser browser)
	{
		this.browser = browser;
	}
	
	private String browserVersion;
	/**
	 * Function to get the browserVersion for a specific test
	 * @return The browserVersion
	 */
	public String getBrowserVersion()
	{
		return browserVersion;
	}
	/**
	 * Function to set the browserVersion for a specific test
	 * @param version The browserVersion
	 */
	public void setBrowserVersion(String version)
	{
		this.browserVersion = version;
	}
	
	private Platform platform;
	/**
	 * Function to get the platform for a specific test
	 * @return The platform
	 */
	public Platform getPlatform()
	{
		return platform;
	}
	/**
	 * Function to set the platform for a specific test
	 * @param platform The platform
	 */
	public void setPlatform(Platform platform)
	{
		this.platform = platform;
	}
	
	//private String perfectoDeviceId;
	/**
	 * Function to get the Perfecto MobileCloud Device ID for a specific test
	 * @return The Perfecto MobileCloud Device ID
	 */
	/*public String getPerfectoDeviceId() {
		return perfectoDeviceId;
	}*/
	/**
	 * Function to set the Perfecto MobileCloud Device ID for a specific test
	 * @param perfectoDeviceId The Perfecto MobileCloud Device ID
	 */
	/*public void setPerfectoDeviceId(String perfectoDeviceId) {
		this.perfectoDeviceId = perfectoDeviceId;
	}*/
}