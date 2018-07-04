import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * This test case retrieve various information about runtime environement
 * 
 */


/**
 * know of the environement
 */
Properties props = System.getProperties()
List<String> keys = new ArrayList<String>(props.keySet())
Collections.sort(keys)
for (String key : keys) {
//	WebUI.comment("${key}:${props.get(key)}")
}
WebUI.comment(">>> user.home : ${props.get('user.home')}")
WebUI.comment(">>> user.dir : ${props.get('user.dir')}")
WebUI.comment(">>> java.home : ${props.get('java.home')}")


/**
 * know of DriverFactory
 */
WebUI.openBrowser('')
WebUI.comment(">>> DriverFactory.getChromeDriverPath()=${DriverFactory.getChromeDriverPath()}") // null
WebUI.comment(">>> DriverFactory.getExecutedBrowser()=${DriverFactory.getExecutedBrowser()}")   // Firefox
WebUI.comment(">>> DriverFactory.getGeckoDriverPath()=${DriverFactory.getGeckoDriverPath()}")   // C:\Katalon_Studio_Windows_64-5.4.2\configuration\resources\drivers\firefox_win64\geckodriver.exe
WebUI.comment(">>> DriverFactory.getRemoteWebDriverServerType()=${DriverFactory.getRemoteWebDriverServerType()}") // null
WebUI.closeBrowser()

/**
 * know of WebDriver
 */
WebDriver driver = DriverFactory.getWebDriver()