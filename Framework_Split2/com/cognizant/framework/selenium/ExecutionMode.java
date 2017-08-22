package com.cognizant.framework.selenium;

/**
 * Enumeration to represent the mode of execution
 * @author Cognizant
 */
public enum ExecutionMode
{
	/**
	 * Execute on the local machine
	 */
	Local,
	
	/**
	 * Execute on a remote machine 
	 */
	Remote,
	
	/**
	 * Execute on a selenium grid
	 */
	Grid;
	/**
	 * Execute on a Perfecto MobileCloud device
	 */
	//Perfecto;
}