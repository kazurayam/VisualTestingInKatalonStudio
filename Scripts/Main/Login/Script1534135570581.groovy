import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import org.openqa.selenium.WebDriver

import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null
WebDriver driver = DriverFactory.getWebDriver()

WebUI.click(findTestObject('KatalonDemoAut/Page_CuraHomepage/a_Make Appointment'))

// transfered to the Login page
WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_Login/button_Login'),
	10, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('KatalonDemoAut/Page_Login/input_username'), Username)
WebUI.setText(findTestObject('KatalonDemoAut/Page_Login/input_password'), Password)

// takes Screenshot of the Login page
Path png2 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_Login.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png2.toFile(), 500)

WebUI.click(findTestObject('KatalonDemoAut/Page_Login/button_Login'))

// ここで入力ページに遷移
WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_CuraAppointment/button_Book Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)