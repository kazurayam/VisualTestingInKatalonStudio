import java.nio.file.Path

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import com.kazurayam.carmina.material.FileType
import com.kazurayam.carmina.material.MaterialRepository
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
/**
 * https://forum.katalon.com/discussion/4662/download-files-from-web-ui-not-working
 * https://forum.katalon.com/discussion/6670/how-to-open-firefox-in-private-browsing-mode
 */
//def URL_CSV = 'https://fixturedownload.com/download/csv/fifa-world-cup-2018/japan'

def pageUrl = 'http://spreadsheetpage.com/index.php/file/C35/P10/'

// open a browser
def openMyBrowser() {
	WebUIDriverType executedBrowser = DriverFactory.getExecutedBrowser()
	switch(executedBrowser) {
		case WebUIDriverType.CHROME_DRIVER:           // "Chrome"
			// On my machine, Katalon Studio fails to open Chrome browser due to a known reason. 
		    // Therefore I have to open chrome myself.
			System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())
			WebDriver driver = new ChromeDriver()
			DriverFactory.changeWebDriver(driver)
			break
		default:
			WebUI.openBrowser('')
	}
}
openMyBrowser()
WebUI.maximizeWindow()
// Navigate to the page
WebUI.navigateToUrl(pageUrl)

// prepare a test object for <a> tag in the page
TestObject testObject = new TestObject('myTestObject')
testObject.addProperty('xpath', ConditionType.EQUALS, '//a[text()="smilechart.xls"]')

// wait for page to load
WebUI.verifyElementPresent(testObject, 10, FailureHandling.STOP_ON_FAILURE)

// delete files with similar name out of Downloads directory
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null
mr.deleteDownloadedFilesFromDownloadsDir('smilechart.xls')

// click the anchor to download a Excel file
WebUI.click(testObject)

// stay for some seconds to wait for the file to be downloaded 
WebUI.delay(3)

Path xlsFile = mr.importDownloadedFileAsMaterial(GlobalVariable.CURRENT_TESTCASE_ID, 'smilechart.xls')

WebUI.closeBrowser()

