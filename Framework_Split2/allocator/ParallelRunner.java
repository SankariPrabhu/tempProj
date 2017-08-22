package allocator;

import java.util.Date;

import supportlibraries.DriverScript;
import com.cognizant.framework.selenium.*;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Util;


/**
 * Class to facilitate parallel execution of test scripts
 * @author Cognizant
 */
public class ParallelRunner implements Runnable
{
	private final SeleniumTestParameters testParameters;
	private final ResultSummaryManager resultSummaryManager;
	private int threadNumber;
	
	/**
	 * Constructor to initialize the details of the test case to be executed
	 * @param testParameters The {@link SeleniumTestParameters} object (passed from the {@link Allocator})
	 * @param resultSummaryManager The {@link ResultSummaryManager} object (passed from the {@link Allocator})
	 */
	public ParallelRunner(SeleniumTestParameters testParameters, ResultSummaryManager resultSummaryManager, int threadNumber)
	{
		super();
		
		this.testParameters = testParameters;
		this.resultSummaryManager = resultSummaryManager;
		this.threadNumber=threadNumber;
	}
	
	@Override
	public void run()
	{
		Date startTime = Util.getCurrentTime();
		String testStatus = invokeTestScript();
		Date endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		resultSummaryManager.updateResultSummary(testParameters.getCurrentScenario(),
									testParameters.getCurrentTestcase(),
									testParameters.getCurrentTestDescription(),
									executionTime, testStatus);
	}
	
	private String invokeTestScript()
	{
		String testStatus;
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if(frameworkParameters.getStopExecution()) {
			testStatus = "Aborted";
		} else {
			DriverScript driverScript = new DriverScript(this.testParameters);
			driverScript.setTestExecutedInUnitTestFramework(false);
			driverScript.driveTestExecution(threadNumber);
			testStatus = driverScript.getTestStatus();
		}
		
		return testStatus;
	}
}