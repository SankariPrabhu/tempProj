package com.cognizant.framework;

/**
 * Singleton class that encapsulates Framework level global parameters
 * @author Cognizant
 */
public class FrameworkParameters
{
	private String relativePath;
	/**
	 * Function to get the absolute path of the framework (to be used as a relative path)
	 * @return The absolute path of the framework
	 */
	public String getRelativePath()
	{
		return relativePath;
	}
	/**
	 * Function to set the absolute path of the framework (to be used as a relative path)
	 * @param relativePath The absolute path of the framework
	 */
	public void setRelativePath(String relativePath)
	{
		this.relativePath = relativePath;
	}
	
	private String runConfiguration;
	/**
	 * Function to get the run configuration to be executed
	 * @return The run configuration
	 */
	public String getRunConfiguration()
	{
		return runConfiguration;
	}
	/**
	 * Function to set the run configuration to be executed
	 * @param runConfiguration The run configuration
	 */
	public void setRunConfiguration(String runConfiguration)
	{
		this.runConfiguration = runConfiguration;
	}
	
	private boolean stopExecution = false;
	/**
	 * Function to get a boolean value indicating whether to stop the overall test batch execution
	 * @return The stopExecution boolean value
	 */
	public boolean getStopExecution()
	{
		return stopExecution;
	}
	/**
	 * Function to set a boolean value indicating whether to stop the overall test batch execution
	 * @param stopExecution Boolean value indicating whether to stop the overall test batch execution
	 */
	public void setStopExecution(boolean stopExecution)
	{
		this.stopExecution = stopExecution;
	}
	
	
	private static final FrameworkParameters frameworkParameters =
													new FrameworkParameters();
	
	private FrameworkParameters()
	{
		// To prevent external instantiation of this class
	}
	
	/**
	 * Function to return the singleton instance of the {@link FrameworkParameters} object
	 * @return Instance of the {@link FrameworkParameters} object
	 */
	public static FrameworkParameters getInstance()
	{
		return frameworkParameters;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}