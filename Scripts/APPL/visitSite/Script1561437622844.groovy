
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.webui.driver.DriverFactory

import java.nio.file.Path

/**
 * Test Cases/APPL/visitSite
 * 
 */

WebUI.comment("*** GlobalVariable[${MGV.CURRENT_TESTSUITE_ID.getName()}]=${GlobalVariable[MGV.CURRENT_TESTSUITE_ID.getName()]}")
WebUI.comment("*** GlobalVariable[${MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()}]=${GlobalVariable[MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()]}")

MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
assert mr != null

WebUI.openBrowser('')
WebUI.setViewPortSize(1280, 1024)

assert GlobalVariable.URL != null
URL url = new URL(GlobalVariable.URL)

WebUI.navigateToUrl(url.toExternalForm())
TestObject divQER = findTestObject('APPL/Investor Relations/div_Quarterly Earnings Reports_this year 10-Q')
WebUI.verifyElementPresent(divQER, 5, FailureHandling.STOP_ON_FAILURE)
WebUI.scrollToElement(divQER, 5, FailureHandling.STOP_ON_FAILURE)

// take screenshot of the "Quaterly Earnings Reports" division and save it into a file
Path png = mr.resolveScreenshotPathByURLPathComponents(
	GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
	url,
	0,
	"default")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveElementImage'(divQER, png.toFile())
WebUI.comment("saved image into ${png}")

// download the latest 10-Q PDF file
TestObject anchor10Q = findTestObject('APPL/Investor Relations/a_latest 10-Q')
WebUI.verifyElementPresent(anchor10Q, 5, FailureHandling.STOP_ON_FAILURE)
String href = WebUI.getAttribute(anchor10Q, 'href', FailureHandling.STOP_ON_FAILURE)
WebUI.comment("href=${href}")

WebUI.closeBrowser()


