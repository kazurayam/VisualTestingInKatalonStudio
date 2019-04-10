import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable


/**
 * Take a screenshot of a Web page, save PNG file to make it a Material
 */
WebUI.openBrowser('')
WebUI.navigateToUrl('http://demoaut.katalon.com/')
WebUI.maximizeWindow()
WebUI.verifyElementPresent(findTestObject('Page_CURA Healthcare Service/a_Make Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

WebUI.comment("Test Case '${GlobalVariable.CURRENT_TESTCASE_ID}' started")

Path pngFile = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl())
WebUI.takeScreenshot(pngFile.toString())
WebUI.comment("saved a screenshot into ${pngFile.toString()}")

Path pngFileOnceMore = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl())
WebUI.takeScreenshot(pngFileOnceMore.toString())
WebUI.comment("saved a screenshot into ${pngFileOnceMore.toString()}")

WebUI.closeBrowser()

