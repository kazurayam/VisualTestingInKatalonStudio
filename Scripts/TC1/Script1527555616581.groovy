import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.kstestresults.TestResults

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl('http://demoaut.katalon.com/')
WebUI.verifyElementPresent(findTestObject('Page_CURA Healthcare Service/a_Make Appointment'), 10, FailureHandling.CONTINUE_ON_FAILURE)

TestResults testResults = (TestResults)GlobalVariable.TEST_RESULTS
assert testResults != null

Path pngFile = testResults.resolvePngFilePath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl())
WebUI.takeScreenshot(pngFile.toString())
WebUI.comment("took a screenshot at ${pngFile.toString()}")

Path pngFile1 = testResults.resolvePngFilePath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), '1')
WebUI.takeScreenshot(pngFile1.toString())
WebUI.comment("took a screenshot at ${pngFile.toString()}")

WebUI.closeBrowser()

