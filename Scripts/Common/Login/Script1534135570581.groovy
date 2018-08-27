import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable


MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

WebUI.click(findTestObject('Page_CuraHomepage/a_Make Appointment'))

// transfered to the Login page
WebUI.verifyElementPresent(findTestObject('Page_Login/button_Login'),
	10, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('Page_Login/input_username'), Username)
WebUI.setText(findTestObject('Page_Login/input_password'), Password)

// takes Screenshot of the Login page
Path png1 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_Login.png")
WebUI.takeScreenshot(png1.toString())

WebUI.click(findTestObject('Page_Login/button_Login'))

// ここで入力ページに遷移
WebUI.verifyElementPresent(findTestObject('Page_CuraAppointment/button_Book Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)