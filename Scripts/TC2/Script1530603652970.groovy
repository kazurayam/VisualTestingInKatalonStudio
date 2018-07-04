import java.nio.file.Path

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.firefox.FirefoxOptions

import com.kazurayam.carmina.material.FileType
import com.kazurayam.carmina.material.MaterialRepository
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * https://forum.katalon.com/discussion/4662/download-files-from-web-ui-not-working
 * https://forum.katalon.com/discussion/6670/how-to-open-firefox-in-private-browsing-mode
 */
//def URL_CSV = 'https://fixturedownload.com/download/csv/fifa-world-cup-2018/japan'

def pageUrl = 'http://spreadsheetpage.com/index.php/file/C35/P10/'

// open a browser
//WebUI.openBrowser('')
CustomKeywords.'com.kazurayam.ksbackyard.MaterialSupport.openReticentBrowser'()

// Navigate to the page
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

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null
Path xlsFile = mr.resolveMaterial(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), 'smilechart.xls', FileType.XLS)
CustomKeywords.'com.kazurayam.ksbackyard.MaterialSupport.importDownloadedFileAsMaterial'(xlsFile)

WebUI.closeBrowser()

