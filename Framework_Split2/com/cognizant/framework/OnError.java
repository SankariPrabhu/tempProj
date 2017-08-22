package com.cognizant.framework;

/**
 * Enumeration to represent the action to be taken in the event of an error during test execution
 * @author Cognizant
 */
public enum OnError
{
	/**
	 * Proceed to the next iteration (if applicable) of the current test case
	 */
	NextIteration,
	/**
	 * Proceed to the next test case (if applicable)
	 */
	NextTestCase,
	/**
	 * Abort execution of all further test cases (applicable only during test batch execution using the Allocator)
	 */
	Stop;
}