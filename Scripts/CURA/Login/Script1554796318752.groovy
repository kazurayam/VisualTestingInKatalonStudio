import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import org.openqa.selenium.WebDriver

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/CURA/Login
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
assert mr != null
WebDriver driver = DriverFactory.getWebDriver()

WebUI.click(findTestObject('CURA/Page_Homepage/a_Make Appointment'))

// transfered to the Login page
WebUI.verifyElementPresent(findTestObject('CURA/Page_Login/button_Login'),
	10, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('CURA/Page_Login/input_username'), Username)
WebUI.setText(findTestObject('CURA/Page_Login/input_password'), Password)

// takes Screenshot of the Login page
//Path png2 = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "CURA_Login.png")
Path png2 = mr.resolveScreenshotPathByURLPathComponents(
	GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
	new URL(WebUI.getUrl()),
	0,
	'top.png')
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png2.toFile(), 500)

WebUI.click(findTestObject('CURA/Page_Login/button_Login'))

// ここで入力ページに遷移
WebUI.verifyElementPresent(findTestObject('CURA/Page_Appointment/button_Book Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)