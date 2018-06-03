import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.ksbackyard.screenshotsupport.ScreenshotRepository

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')

WebUI.navigateToUrl('http://demoaut.katalon.com/')

WebUI.verifyElementPresent(findTestObject('Page_CURA Healthcare Service/a_Make Appointment'), 10, FailureHandling.OPTIONAL)

ScreenshotRepository scRepo = (ScreenshotRepository)GlobalVariable.SCREENSHOT_REPOSITORY
assert scRepo != null

Path imageFile = scRepo.resolveScreenshotFilePath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl())
WebUI.takeScreenshot(imageFile.toString())

Path imageFile1 = scRepo.resolveScreenshotFilePath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), '.1')
WebUI.takeScreenshot(imageFile1.toString())

Path imageFile2 = scRepo.resolveScreenshotFilePath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), '.2')
WebUI.takeScreenshot(imageFile2.toString())

WebUI.closeBrowser()

