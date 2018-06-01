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
Path imageFile = scRepo.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl())
WebUI.takeScreenshot(imageFile.toString())

// one more shot
Path imageFile1 = scRepo.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, WebUI.getUrl(), '.1')
WebUI.takeScreenshot(imageFile1.toString())

/*
WebUI.setText(findTestObject('Page_CURA Healthcare Service/input_username'), 'John Doe')

WebUI.setText(findTestObject('Page_CURA Healthcare Service/input_password'), 'ThisIsNotAPassword')

WebUI.click(findTestObject('Page_CURA Healthcare Service/footer_CURA Healthcare Service'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/button_Login'))

WebUI.selectOptionByValue(findTestObject('Page_CURA Healthcare Service/select_Tokyo CURA Healthcare C'), 'Hongkong CURA Healthcare Center', 
    true)

WebUI.click(findTestObject('Page_CURA Healthcare Service/input_hospital_readmission'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/input_programs'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/div_input-group-addon'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/td_31'))

WebUI.setText(findTestObject('Page_CURA Healthcare Service/textarea_comment'), 'This is a comment')

WebUI.click(findTestObject('Page_CURA Healthcare Service/button_Book Appointment'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/p_Hongkong CURA Healthcare Cen'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/p_Yes'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/p_Medicaid'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/p_31052018'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/p_This is a comment'))

WebUI.click(findTestObject('Page_CURA Healthcare Service/a_Go to Homepage'))
*/

WebUI.closeBrowser()

