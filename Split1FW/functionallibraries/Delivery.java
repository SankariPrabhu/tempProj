package functionallibraries;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import UIMAP.UIMAP_Document;
import UIMAP.UIMAP_SearchResult;
import UIMAP.UIMAP_WorkFolders;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Delivery extends ReusableLibrary {
	private String Mediumwait = properties.getProperty("MediumWait");
	int Mwait = Integer.parseInt(Mediumwait);
	private CommonLibrary commonLibrary = new CommonLibrary(scriptHelper);
	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
	PageCheck pageCheck = new PageCheck(scriptHelper);
	String browsername = cap.getBrowserName();
	String version = cap.getVersion();

	public Delivery(ScriptHelper scriptHelper) {
		super(scriptHelper);
		String url = null;
		int counter = 0;

		do {
			counter = counter + 1;
			url = driver.getCurrentUrl();
			if (!url.contains("delivery"))
				commonLibrary.sleep(5000);

		} while (!url.contains("delivery") && counter < 40);

		if (!driver.getCurrentUrl().contains("delivery")) {
			throw new IllegalStateException(
					"Delivery page is expected, but not displayed!");
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to download and save a document in
	// specified path
	// # Function Name : Download_Save_Document    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards downloadSaveDocumentShepards(String strFilePath,
			String AutoITPath, String PDFText, String TextExistence,
			String fileName) {
		try {
			if (browsername.contains("firefox")) {
				commonLibrary.sleep(4000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_S);
				// CTRL+Z is now pressed (receiving application should see a
				// "key down" event.)
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_ALT);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);

			} else if (browsername.contains("internet")
					&& version.contains("8")) {
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath, "Save As", strFilePath, fileName };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet")) {
				commonLibrary.sleep(3000);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_F6);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_TAB);
				commonLibrary.sleep(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot = null;
			}

			commonLibrary.sleep(30000);

			commonLibrary.fileExists(strFilePath, fileName);

			String downloadedFilename = strFilePath + "\\" + fileName;

			WebElement lnkDownload = commonLibrary.isExist(
					UIMAP_Document.lnkDownloadedDoc, 10);
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null
					&& pgHeader.getText().toLowerCase()
							.contains("delivery complete")
					&& lnkDownload != null)
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.FAIL);

			WebElement btnDownloadClose = commonLibrary.isExist(
					UIMAP_Document.btnDownloadClose, 10);
			if (btnDownloadClose != null)
				driver.close();

			commonLibrary.smallWait();
			commonLibrary.switchToWindow("shepards");

			// Unzip the document present
			String zipFile = strFilePath + "\\" + fileName + ".ZIP";
			fileUnZip(strFilePath, zipFile);

			// Verify content in the downloaded document
			String arrPDFText[] = PDFText.split("#");
			String arrTextExistence[] = TextExistence.split(";");
			String FileDownloaded = downloadedFilename + ".PDF";
			for (int i = 0; i < arrPDFText.length; i++) {
				pdfVerificationPath(FileDownloaded, arrPDFText[i],
						arrTextExistence[i]);
			}

			return new Shepards(scriptHelper);
		} catch (Exception e) {

			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());

		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify contents in a PDF file
	// # Function Name : PDFVerification_Path     
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public void pdfVerificationPath(String FilePath, String Text, String Exist)
			throws IOException {
		PdfReader reader = new PdfReader(FilePath);
		int Pages = reader.getNumberOfPages();
		boolean blnFlag = false;

		for (int i = 1; i <= Pages; i++) {
			String TestText = PdfTextExtractor.getTextFromPage(reader, i);
			if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
				blnFlag = true;
				break;
			}

		}
		switch (Exist) {
		case "Exist": {
			if (blnFlag) {
				report.updateTestLog("'" + Text
						+ "' is present in th ePDF document", "'" + Text
						+ "' is present in the PDF document", Status.PASS);

			} else {
				report.updateTestLog("'" + Text
						+ "' is present in th ePDF document", "'" + Text
						+ "' is not present in the PDF document", Status.FAIL);
			}
			break;
		}
		case "NotExist": {
			if (!blnFlag) {
				report.updateTestLog("'" + Text
						+ "' is present in th ePDF document", "'" + Text
						+ "' is not present in the PDF document", Status.PASS);
			} else {
				report.updateTestLog("'" + Text
						+ "' is present in th ePDF document", "'" + Text
						+ "' is present in the PDF document", Status.FAIL);
			}
			break;
		}
		}
		reader.close();
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to Verify Email Is Sent
	// # Function Name : VerifyEmail_IsSent    
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards verifyEmailIsSent(String strEmailId) {
		// verifying the header in Email sending pop up
		String header = null;
		WebElement txtPopupHeader = commonLibrary.isExist(
				UIMAP_SearchResult.txtPopupHeader, 20);
		try {
			int count = 0;
			// looping while email is sending with the header
			do {
				txtPopupHeader = commonLibrary.isExist(By.tagName("h1"), 20);
				count++;
				if (txtPopupHeader != null) {
					header = txtPopupHeader.getText().toLowerCase();
					if (header.contains("complete"))
						commonLibrary.sleep(10000);
				}
				commonLibrary.sleep(20000);
			} while (header.contains("processing") && count < 250);
		} catch (StaleElementReferenceException e) {
			System.out.println(e);
		}
		commonLibrary.sleep(50000);
		header = txtPopupHeader.getText().toLowerCase();

		// verfying the status of email
		if (header.contains("complete")) {
			report.updateTestLog("Sending Email",
					"Email has sent successfully", Status.PASS);
			report.updateTestLog(
					"Open the Email received, verify the overview section of the document and close the pdf",
					"Manual Verification of email received in ID ' "
							+ strEmailId + "'", Status.WARNING);

			// closing the delivery pop up and navigating to the primary window
			driver.close();
			commonLibrary.switchToWindow("lexis.com/shepards");
		} else {
			report.updateTestLog("Sending Email", "Email has not sent",
					Status.FAIL);
			driver.close();
			commonLibrary.switchToWindow("lexis.com/shepards");
		}
		return new Shepards(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to swich from delivery to shepards
	// window
	// # Function Name : SwitchToShepards    
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************

	public Shepards switchToShepards(String strWindowName) {

		// Boolean blnCheckSwitch = false;
		// for (String winHandle : driver.getWindowHandles()) {
		// Boolean a =
		// driver.switchTo().window(winHandle).getCurrentUrl().contains(strWindowName);
		//
		// if (a == true) {
		// driver.switchTo().window(winHandle);
		//
		// blnCheckSwitch = true;
		// break;
		// }
		// }
		// if (blnCheckSwitch)
		commonLibrary.switchToWindow(strWindowName);
		return new Shepards(scriptHelper);
		// else
		// return null;

	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify email delivery
	// # Function Name : verifyEmailDelivery    
	// # Author : Baswaraj
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public WorkFolders verifyEmailDelivery() {

		try {
			commonLibrary.sleep(20000);
			WebElement pgHeader = commonLibrary.isExist(By.tagName("h1"), 10);
			if (pgHeader != null
					&& (pgHeader.getText().toLowerCase()
							.contains("delivery complete"))
					|| (pgHeader.getText().toLowerCase()
							.contains("processing delivery")))
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.PASS);
			else
				report.updateTestLog(
						"Verify PROCESSING MSG POP UP OPENS AND UPDATED TO A COMPLETE/FAILURE MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						"PROCESSING MSG POP UP is opened AND UPDATED TO A "
								+ pgHeader.getText()
								+ " MESSAGE ONCE THE DELIVERY IS COMPLETE.",
						Status.FAIL);

			WebElement Close = commonLibrary.isExist(
					UIMAP_WorkFolders.closeEmailDelivery, 20);
			try {
				commonLibrary.clickButtonParentWithWait(Close, "Close");
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			commonLibrary.switchToWindow("workfolders");
		}

		catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new WorkFolders(scriptHelper);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to unzip a file
	// # Function Name : fileUnZip    
	// # Author : Shobana
	// # Date Created : Feb'15
	// #*****************************************************************************************************************************
	public void fileUnZip(String outputPath, String zipFile) {
		try {

			File destDir = new File(outputPath);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
					zipFile));
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = outputPath + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(filePath));
					byte[] bytesIn = new byte[4096];
					int read = 0;
					while ((read = zipIn.read(bytesIn)) != -1) {
						bos.write(bytesIn, 0, read);
					}
					bos.close();

				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void sslCertificate() throws NoSuchAlgorithmException,
			KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Delivery pdfVerification(String FilePath, String Text, String Exist) {
		try {
			PdfReader reader = new PdfReader(FilePath);
			int Pages = reader.getNumberOfPages();
			boolean blnFlag = false;

			for (int i = 1; i <= Pages; i++) {
				String TestText = PdfTextExtractor.getTextFromPage(reader, i);
				if (TestText.replace(" ", "").contains(Text.replace(" ", ""))) {
					blnFlag = true;
					break;
				}

			}
			switch (Exist) {
			case "Exist": {
				if (blnFlag) {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is present in the PDF document", Status.PASS);

				} else {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is not present in the PDF document",
							Status.FAIL);
				}
				break;
			}
			case "NotExist": {
				if (!blnFlag) {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is not present in the PDF document",
							Status.PASS);
				} else {
					report.updateTestLog("'" + Text
							+ "' is present in th ePDF document", "'" + Text
							+ "' is present in the PDF document", Status.FAIL);
				}
				break;
			}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
		return new Delivery(scriptHelper);
	}

	public void savePDFBrowser(String AutoITPath, String FilePath,
			String docName) throws IOException {
		try {
			sslCertificate();
			commonLibrary.sleep(4000);
			CookieHandler.setDefault(new CookieManager(null,
					CookiePolicy.ACCEPT_ALL));
			commonLibrary.sleep(3000);
			String path2 = FilePath + docName + ".Pdf";
			String windowTitle = driver.getCurrentUrl()
					+ " - Internet Explorer provided by Reed Elsevier";
			if (browsername.contains("internet") && version.contains("8")) {
				String[] cmd = { AutoITPath, windowTitle, path2,
						"Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if (browsername.contains("internet")
					&& version.contains("9")) {
				String[] cmd = { AutoITPath, windowTitle, path2,
						"Save a Copy..." };
				Runtime.getRuntime().exec(cmd);
			} else if ((browsername.contains("internet") && version
					.contains("11"))
					|| (browsername.contains("internet") && version
							.contains("10"))) {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(10000);
				for (int i = 1; i <= 3; i++) {
					robot.keyPress(KeyEvent.VK_TAB);
					commonLibrary.sleep(1000);
				}
				robot.keyPress(KeyEvent.VK_ENTER);
				commonLibrary.sleep(1000);
				// String[] cmd = {AutoITPath, path2, "Save As"};
				// Runtime.getRuntime().exec(AutoITPath);
			}
			commonLibrary.sleep(7000);

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}
	}

	// #*****************************************************************************************************************************
	// # Function Description : Function to verify text in PDF
	// # Function Name : PDFVerification_Secondary    
	// # Author : Gokul
	// # Date Created : May'15
	// #*****************************************************************************************************************************

	public Delivery pdfVerificationSecondaryByDownload(String pdfContent,
			String strFilePath, String AutoITPath, String AutoITPath1,
			String strFileName, String textExistence) {
		try {
			String filename = null;
			commonLibrary.sleep(600000);
			Thread.sleep(20000);
			commonLibrary.windowFocus();
			pageCheck.ajaxElementCheck(driver,
					properties.getProperty("xSpinner"));

			commonLibrary.fileExistsDelete(strFilePath, strFileName);
			String path = strFilePath + "\\";
			filename = strFilePath + "\\" + strFileName + ".PDF";

			if (browsername.equalsIgnoreCase("internet explorer")) {
				driver.manage().window().maximize();
				savePDFBrowser(AutoITPath, path, strFileName);
			} else if (browsername.equalsIgnoreCase("firefox")) {
				driver.manage().window().maximize();
				Actions action = new Actions(driver);
				action.sendKeys(Keys.chord(Keys.CONTROL, "s")).build()
						.perform();
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);
				// commonLibrary.sleep(1000000);
				// Thread.sleep(10000);
				// PDFVerification(filename,pdfContent,textExistence);
			} else if (browsername.toLowerCase().contains("chrome")) {
				driver.manage().window().maximize();
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_S);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				commonLibrary.sleep(4000);
				String[] cmd = { AutoITPath1, "Save As", filename };
				Runtime.getRuntime().exec(cmd);
			}
			commonLibrary.sleep(1000000);
			Thread.sleep(20000);
			pdfVerification(filename, pdfContent, textExistence);
			// driver.close();
			// commonLibrary.SwitchToWindow("container");
			return new Delivery(scriptHelper);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new FrameworkException("Exception", e.toString());
		}

	}

}