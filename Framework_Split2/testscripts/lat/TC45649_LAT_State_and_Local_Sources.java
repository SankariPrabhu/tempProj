//#*****************************************************************************************************************************
//# Application Automated : Lexis Advance
//# Test Case ID :     45649
//# Test Case Name :  	TC LAT State and  Local Sources
//# Test Script Name : TC45649_LAT_State_and_Local_Sources
//# Author : Anbarasan
//# Date Created : 19-Dec'15
//#*****************************************************************************************************************************

package testscripts.lat;

import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import supportlibraries.DriverScript;
import supportlibraries.TestCase;
import functionallibraries.*;

public class TC45649_LAT_State_and_Local_Sources extends TestCase {
	private SignIn signInSignOut;
	private Home home;
	private LexisAdvanceTax lat;
	private LexisAdvanceTaxResults latResults;

	@Test()
	public void runTC45649_LAT_State_and_Local_Sources() {
		testParameters.setCurrentTestDescription("Test for verifying Filters in LAT");
		testParameters.setIterationMode(IterationOptions.RunAllIterations);
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution(1);
	}

	@Override
	public void setUp() {
		signInSignOut = new SignIn(scriptHelper);
	}

	@Override
	public void executeTest() {

		String searchTerm = dataTable.getData("General_Data", "SearchTerm");
		String[] searchTermList = searchTerm.split(";");
		String practiceArea = dataTable.getData("General_Data", "PracticeArea");
		String[] practiceAreaList = practiceArea.split(";");
		String header = dataTable.getData("General_Data", "DocHeading1");
		String podName = dataTable.getData("General_Data", "DocLink");
		String[] podNameList = podName.split(";");
		String sourceTitle1 = dataTable.getData("General_Data", "Title1");
		String[] sourceTitleList1 = sourceTitle1.split(";");
		String sourceTitle2 = dataTable.getData("General_Data", "Title2");
		String[] sourceTitleList2 = sourceTitle2.split(";");
		String sourceTitle3 = dataTable.getData("General_Data", "Title3");
		String[] sourceTitleList3 = sourceTitle3.split(";");
		String verifySource = dataTable.getData("General_Data", "DocHeading2");

		// Logging in to the application

		home = signInSignOut.invokeApplication().login();

		// Switching to practice area

		lat = home.productTaxSwitcher(practiceAreaList[0]);

		// Verifying practice area

		lat = lat.verifyPracticeArea(practiceAreaList[0]);

		//
		for (int i = 0; i < 3; i++) {
			lat = lat.expandSourceAndAddToSearch(podNameList[0], sourceTitleList1[i], sourceTitleList2[i], sourceTitleList3[i]);

			lat = lat.verifyNarrowBySection(sourceTitleList3[i]);
		}

		lat = lat.expandSourceAndAddToSearch1(podNameList[0], sourceTitleList1[3], sourceTitleList3[3]);

		lat = lat.expandSourceAndAddToSearch(podNameList[0], sourceTitleList1[4], sourceTitleList2[4], sourceTitleList3[4]);

		latResults = lat.simpleSearch(searchTermList[0], false);

		latResults = latResults.verifyResultHeader1(header);

		lat = latResults.productTaxSwitcher(practiceAreaList[1]);

		lat = lat.verifyPracticeArea(practiceAreaList[1]);

		lat = lat.expandSourceAndAddToSearch1(podNameList[1], sourceTitleList1[5], sourceTitleList3[5]);

		lat = lat.verifyLinksDisplayesUnderLink(sourceTitleList1[5], verifySource);

		lat = lat.verifyNarrowBySection(sourceTitleList3[5]);

		latResults = lat.simpleSearch(searchTermList[1], false);

		latResults = latResults.verifyFilter(sourceTitleList3[5]);

		signInSignOut = latResults.logout();

		signInSignOut = null;

	}

	@Override
	public void tearDown() {
		// Nothing to do
	}
}
