package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_Interactivecitationworkstation {

	public static By divlogo = By
			.cssSelector("div[class*='nav_currentproduct ']");
	public static By imglogo = By.cssSelector("a[data-action*='icw']");
	public static By home = By.cssSelector("header[class='banner']");
	public static By prdctSwtcher = By
			.cssSelector("div[class='nav_productswitcher_wrapper']");
	public static By icwPods = By.cssSelector("section[class*='pod-wrapper']");
	public static By lnkLinks = By.tagName("a");
	public static By bluebooktable = By
			.cssSelector("table[id='studentReportTable']");
	public static By bluebookcolheader = By.tagName("th");
	public static By exercisenamecol = By
			.cssSelector("tr[class='exercisename']");
	public static By exercisestatuscol = By
			.cssSelector("tr[class='exercisestatus']");
	public static By gradenumbercol = By
			.cssSelector("tr[class='grade number']");
	public static By totalcountnumbercol = By
			.cssSelector("tr[class='totalcount number']");
	public static By correctcountnumbercol = By
			.cssSelector("tr[class='correctcount number']");
	public static By incorrectcountnumbercol = By
			.cssSelector("tr[class='incorrectcount number']");
	public static By incompletecountnumbercol = By
			.cssSelector("tr[class='incompletecount number']");
	public static By lastupdatedcol = By.cssSelector("tr[class='lastupdated']");
	public static By actionscol = By.cssSelector("tr[class='actions']");
	public static By exercisename = By.cssSelector("td[class='exercisename']");
	public static By exercisestatus = By
			.cssSelector("td[class='exercisestatus']");
	public static By rows = By.tagName("tr");
	public static By headerName = By.cssSelector("span[class='query']");
	public static By problemTab = By
			.cssSelector("li[id*='problem'][id*='tab']");
	public static By problemNo = By.cssSelector("span[class='problem-number']");
	public static By buttons = By.tagName("button");
	public static By textBox = By.id("tinymce");
	public static By submitAnswer = By
			.cssSelector("input[id*='problem'][id*='textarea-submit'][value='Submit Answer']");
	public static By correctAnswer = By.cssSelector("p[class='attempt-text']");
	public static By section = By
			.cssSelector("section[id*='panel'][class='tab-panel selected']");
	public static By completeExercise = By
			.cssSelector("input[class='secondary trigger-dialog'][value='Complete Exercise']");
	public static By comfirmCompleteExer = By
			.cssSelector("input[data-action='confirm'][value='Complete Exercise']");
	public static By selectStudent = By
			.cssSelector("select[id='student-report-select-students']");
	public static By selectCitation = By
			.cssSelector("select[id='student-report-select-manuals']");
	public static By getStatusReportStud = By
			.cssSelector("input[type='submit'][value='Get Status Report for Student']");
	public static By actionButton = By
			.cssSelector("button[type='button'][class*='TriangleDownAfter']");
	public static By resetExercise = By
			.cssSelector("button[type='button'][data-dialog='resetExerciseDialog']");
	public static By comfirmResetExer = By
			.cssSelector("input[data-action='confirm'][value='Reset this Exercise']");
	public static By comfirmResetExer1 = By
			.cssSelector("input[data-action='confirm'][value='Reset All Exercises']");
	public static By btnAddFolder = By
			.cssSelector("button[data-action='folder']");
	public static By lnkDeliveryDownload = By
			.cssSelector("button[data-action='download']");
	public static By btnEmail = By.cssSelector("button[data-action='email']");
	public static By lnkPrint = By.cssSelector("button[data-action='print']");
	public static By divSort = By.cssSelector("div[class='dropdownContainer']");
	public static By divSortList = By
			.cssSelector("aside[class='supplemental sortdropdown']");
	public static By questionSelected = By
			.cssSelector("li[class='tab selected']");
	public static By p = By.tagName("p");
	public static By incorrAttempt = By
			.cssSelector("div[class='incorrect-attempts']");
	public static By incorrAns = By.cssSelector("span[class='error']");
	public static By incorrAns1 = By.cssSelector("span[class='attempt-text']");
	public static By hints = By.cssSelector("p[class='hints']");
	public static By nextButton = By.cssSelector("button[class='next action']");
	public static By correctMsg = By.cssSelector("h4[class='is-correct']");
	public static By attemptTable = By
			.cssSelector("table[class='attempts-indicator']");
	public static By italics = By.cssSelector("span[class='italic']");
	public static By tabs = By.cssSelector("ul[class='globalpod-tabs']");
	public static By italicsButton = By.cssSelector("button[class='italic']");

	public static By selectedProblemTab = By
			.cssSelector("li[class*='selected']");
	public static By completeExerDialogContent = By
			.cssSelector("form[class='dialog-content']");
	public static By completeExerDialog = By
			.cssSelector("aside[class='dialog citationexercise']");
	public static By requestReset = By
			.cssSelector("a[data-dialog='requestResetDialog']");
	public static By requestResetDialog = By
			.cssSelector("aside[role='dialog']");
	public static By input = By.tagName("input");
	public static By submitRequest = By
			.cssSelector("input[value='Submit Request']");
	public static By graphExists = By.cssSelector("img[alt*='Bluebook']");
	public static By deliveryTray = By.cssSelector("div[class='chart-key']");

	public static By requestResetExercise = By
			.cssSelector("button[type='button'][data-dialog='requestResetDialog']");
	public static By header = By.cssSelector("h2[class='pagewrapper']");

	public static By linkStatus = By
			.cssSelector("td[class='exercisestatus nowrap']");
	public static By viewComp = By
			.cssSelector("a[data-action='studentcertificate']");

	public static By selectFromCItationManualDd = By
			.cssSelector("select[id='exercise-report-select-manuals']");
	public static By selectFromExerciseDd = By
			.cssSelector("select[id='exercise-report-select-exercises']");
	public static By getStatusReport = By
			.cssSelector("input[value='Get Status Report for Exercise']");
	public static By deliveryTrayIconParent = By
			.cssSelector("menu[class*='toolbar']");
	public static By studentProgressTable = By
			.cssSelector("table[id='studentProgressTable']");
	public static By verifySortable = By
			.cssSelector("div[class='resultsRightSide desktopNeverOverflow']");
	public static By headerCheck = By.tagName("h3");
	public static By verifySortableDd = By
			.cssSelector("div[class*='dropdownContainer']");
	public static By verifyActionsColumn = By
			.cssSelector("td[class*='actions']");

	public static By editExercise = By
			.cssSelector("a[data-action='studentsetting']");
	public static By clickSortHeaderButton = By
			.cssSelector("button[class*='current trigger']");
	public static By listParent = By.tagName("ul");

	public static By selectFromStudentDd = By
			.cssSelector("select[id='student-report-select-students']");
	public static By selectFromStuCitationDd = By
			.cssSelector("select[id='student-report-select-manuals']");
	public static By getStatusReportStu = By
			.cssSelector("input[value='Get Status Report for Student']");

	public static By assignGrade = By
			.cssSelector("button[type='button'][data-dialog='editGradeDialog']");
	public static By enterGrade = By.cssSelector("input[name='grade']");
	public static By saveGrade = By
			.cssSelector("input[type='submit'][value='Save']");

	public static By viewcomple = By
			.cssSelector("button[type='button'][data-action='studentcertificate']");
	public static By gradeData = By.cssSelector("td[class='grade number']");
	public static By studentName = By.cssSelector("td[class='studentname']");
	public static By cells = By.tagName("td");

	public static By avgAttempts = By
			.cssSelector("td[class='averageattempts number']");
	public static By instructionsTab = By
			.cssSelector("div[class*='instructions-content']");
	public static By gradeParent = By.cssSelector("div[class*='pagewrapper']");
	public static By verifyGradeForFirstExercise = By
			.cssSelector("td[class*='grade number']");

	public static By tableReport = By.cssSelector("table[class='report']");
	public static By totalProbCount = By
			.cssSelector("td[class='totalproblems number']");
	public static By correctProbCount = By
			.cssSelector("td[class='correct number']");
	public static By incorrectProbCount = By
			.cssSelector("td[class='incorrect number']");

	public static By headerAction = By
			.cssSelector("header[class='banner']  button[class*='icon la-TriangleDown']");
	public static By resetForAllStu = By
			.cssSelector("button[data-dialog='resetThisForAllDialog']");
	public static By actionPopup = By
			.cssSelector("aside[class='supplemental overflow']");

	public static By deliveryMenu = By
			.cssSelector("aside[class='supplemental']");
	public static By chooseNewSettingsDownload = By
			.cssSelector("button[data-action='downloadopt']");
	public static By useCurrentSettingsDownload = By
			.cssSelector("button[data-action='downloadnow']");
	public static By chooseNewSettingsPrint = By
			.cssSelector("button[data-action='printopt']");
	public static By useCurrentSettingsPrint = By
			.cssSelector("button[data-action='printnow']");

	public static By coverPage = By.cssSelector("input[id='IncludeCoverPage']");
	public static By download = By.cssSelector("input[data-action='download']");
	public static By deliveryComplete = By.tagName("h1");
	public static By deliveryMessage = By.tagName("p");
	public static By pdfLink = By.tagName("a");
	public static By closeButtton = By
			.cssSelector("input[class='close return primary']");
	public static By emailPopup = By
			.cssSelector("aside[class='dialog delivery']");
	public static By emailFormat = By.cssSelector("label[id='Format']");
	public static By emailIdTextBox = By
			.cssSelector("input[id='EmailAddress']");
	public static By emailButton = By.cssSelector("input[data-action='email']");

	public static By submitReq = By
			.cssSelector("input[data-action='confirm'][value='Submit Request']");
	public static By podContent = By
			.cssSelector("div[class='globalpod-content']");
	public static By certPage = By
			.cssSelector("div[class*='studentcertificate']");
	public static By prevAttemptsContainer = By
			.cssSelector("div[class='attempts-feedback']");

	public static By exerciseContent = By
			.cssSelector("section[class='exercise-content']");
	public static By homeLink = By.cssSelector("a[data-action='studenthome']");

	public static By tableContents(String text) {
		return By.cssSelector("td[class='" + text + "']");
	}

	public static By table = By.tagName("table");
	public static By homeLinkProf = By
			.cssSelector("a[data-action='professorhome']");

	public static By imgVSALogo = By.cssSelector("a[data-action*='vsahome']");
}
