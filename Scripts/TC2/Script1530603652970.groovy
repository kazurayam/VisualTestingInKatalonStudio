import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * https://forum.katalon.com/discussion/4662/download-files-from-web-ui-not-working
 * https://forum.katalon.com/discussion/6670/how-to-open-firefox-in-private-browsing-mode
 */
//def URL_CSV = 'https://fixturedownload.com/download/csv/fifa-world-cup-2018/japan'

FirefoxProfile createFirefoxProfile() {
	FirefoxProfile profile = new FirefoxProfile();
	profile.setPreference('browser.download.folderList', 2)  // tells firefox not to use default Downloads directory
	profile.setPreference('browser.download.manager.showWhenStarting', false) // turns off showing download progress
	profile.setPreference('browser.download.dir', 'C:\\temp\\')  // sets the directory for downloads
	profile.setPreference('browser.helperApps.neverAsk.saveToDisk', 'text/csv,application/pdf,application/vnd.ms-excel,application/msexcel,application.x-msexcel,application/excel')  // tells Firefox to automatically downlod the files of the selected mime-types
	return profile
}

/**
 * FirefoxOptions object manages firefox specific settings
 * in a way that geckodriver can understand
 */
FirefoxOptions createFirefoxOptions() {
	FirefoxOptions options = new FirefoxOptions()
	options.addPreference('browser.download.folderList', 2)  // tells firefox not to use default Downloads directory
	options.addPreference('browser.download.manager.showWhenStarting', false) // turns off showing download progress
	options.addPreference('browser.download.dir', 'C:\\temp\\')  // sets the directory for downloads
	options.addPreference('browser.helperApps.neverAsk.saveToDisk', 'text/csv,application/pdf,application/vnd.ms-excel,application/msexcel,application.x-msexcel,application/excel')  // tells Firefox to automatically downlod the files of the selected mime-types
	options.addPreference('browser.startup.page', 1) 
	options.addPreference('browser.startup.homepage', 'https://www.katalon.com/')
	return options
}

def pageUrl = 'http://spreadsheetpage.com/index.php/file/C35/P10/'

// open a browser, switch to configured WebDriver 
FirefoxDriver firefoxDriver = new FirefoxDriver(createFirefoxOptions())
DriverFactory.changeWebDriver(firefoxDriver)

// Navigate to URL_CSV
WebUI.navigateToUrl(pageUrl)

// prepare a test object for <a> tag in the page
TestObject testObject = new TestObject('myTestObject')
testObject.addProperty('xpath', ConditionType.EQUALS, '//a[text()="smilechart.xls"]')

// wait for page to load
WebUI.verifyElementPresent(testObject, 10, FailureHandling.STOP_ON_FAILURE)

// click the anchor to download a Excel file
WebUI.click(testObject)

// stay for some seconds to wait for the file to be downloaded 
WebUI.delay(3)

WebUI.closeBrowser()

