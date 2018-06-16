import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.carmina.FileType
import com.kazurayam.carmina.TestResultsRepository

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl('http://demoaut.katalon.com/')
WebUI.verifyElementPresent(findTestObject('Page_CURA Healthcare Service/a_Make Appointment'),
	10, FailureHandling.CONTINUE_ON_FAILURE)

TestResultsRepository trr = (TestResultsRepository)GlobalVariable.TEST_RESULTS_REPOSITORY
assert trr != null

Path pngFile = trr.resolveMaterial(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), FileType.PNG)
WebUI.takeScreenshot(pngFile.toString())
WebUI.comment("took a screenshot at ${pngFile.toString()}")

Path pngFile1 = trr.resolveMaterial(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), 'oneMore', FileType.PNG)
WebUI.takeScreenshot(pngFile1.toString())
WebUI.comment("took a screenshot at ${pngFile1.toString()}")

WebUI.closeBrowser()

