import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.carmina.FileType
import com.kazurayam.carmina.TestMaterialsRepository

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl('http://demoaut.katalon.com/')
WebUI.verifyElementPresent(findTestObject('Page_CURA Healthcare Service/a_Make Appointment'),
	10, FailureHandling.CONTINUE_ON_FAILURE)

TestMaterialsRepository tmr = (TestMaterialsRepository)GlobalVariable.TEST_MATERIALS_REPOSITORY
assert tmr != null

Path pngFile = tmr.resolveMaterial(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), FileType.PNG)
WebUI.takeScreenshot(pngFile.toString())
WebUI.comment("saved a screenshot into ${pngFile.toString()}")

Path pngFile1 = tmr.resolveMaterial(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), 'oneMore', FileType.PNG)
WebUI.takeScreenshot(pngFile1.toString())
WebUI.comment("saved a screenshot into ${pngFile1.toString()}")

WebUI.closeBrowser()

