import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebDriver
import org.openqa.selenium.Keys as Keys
import internal.GlobalVariable as GlobalVariable

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 768)
WebDriver driver = DriverFactory.getWebDriver()

if (RunConfiguration.getExecutionProfile() == 'develop') {
	// open the mimic CURA Homepage and take entire page screenshot
	WebUI.navigateToUrl('http://demoaut-mimic.kazurayam.com')
} else {
	// open the production CURA Homepage and take entire page screenshot
	WebUI.navigateToUrl("http://${GlobalVariable.Hostname}/")
}
WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_CuraHomepage/a_Make Appointment'),
		10, FailureHandling.STOP_ON_FAILURE)

Path png1 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_Homepage.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png1.toFile(), 500)


// Make AppointmentボタンをクリックしてLogin画面を呼び出しUsernameとPasswordを入力しログインするまでを
// 別のTest Caseで実行する
WebUI.callTestCase(findTestCase('Main/Login'),
	[
		'Username': GlobalVariable.Username,
		'Password': GlobalVariable.Password
	],
	FailureHandling.STOP_ON_FAILURE)


// 診察の予約を入力する
WebUI.selectOptionByValue(findTestObject('KatalonDemoAut/Page_CuraAppointment/select_Tokyo CURA Healthcare C'), 'Hongkong CURA Healthcare Center',
	true)

WebUI.click(findTestObject('KatalonDemoAut/Page_CuraAppointment/input_hospital_readmission'))

WebUI.click(findTestObject('KatalonDemoAut/Page_CuraAppointment/input_programs'))

// 今日を起点に来週の同じ曜日を指定

def visitDate = LocalDateTime.now().plusWeeks(1)
def visitDateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(visitDate)
WebUI.setText(findTestObject('KatalonDemoAut/Page_CuraAppointment/input_visit_date'), visitDateStr)
// date picker のダイアログを閉じるため Enterを押す
WebUI.sendKeys(findTestObject('KatalonDemoAut/Page_CuraAppointment/input_visit_date'), Keys.chord(Keys.ENTER))

// コメントを入力
WebUI.setText(findTestObject('KatalonDemoAut/Page_CuraAppointment/textarea_comment'), 'This is a comment')


// takes Screenshot of the CURA Appointment page
Path png3 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_Appointment.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png3.toFile(), 500)


// ここで確認ページに遷移

WebUI.click(findTestObject('KatalonDemoAut/Page_CuraAppointment/button_Book Appointment'))

WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/a_Go to Homepage'),
	10, FailureHandling.STOP_ON_FAILURE)

def facility = WebUI.getText(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/p_facility'))
WebUI.verifyMatch(facility,
	'^(Tokyo|Hongkong|Seoul) CURA Healthcare Center$', true)

def readmission = WebUI.getText(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/p_hospital_readmission'))
WebUI.verifyMatch(readmission,
	'(Yes|No)', true)

def program = WebUI.getText(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/p_program'))
WebUI.verifyMatch(program,
	'(Medicare|Medicaid|None)', true)

def visitDateStr2 = WebUI.getText(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/p_visit_date'))
WebUI.verifyMatch(visitDateStr2,
	'[0-9]{2}/[0-9]{2}/[0-9]{4}',
	true, FailureHandling.CONTINUE_ON_FAILURE)

TemporalAccessor parsed = DateTimeFormatter.ofPattern('dd/MM/uuuu').parse(visitDateStr2)

LocalDateTime visitDate2 = LocalDate.from(parsed).atStartOfDay()
// 今日よりも未来の日付であること
boolean isAfterNow = visitDate2.isAfter(LocalDateTime.now())
WebUI.verifyEqual(isAfterNow, true, FailureHandling.CONTINUE_ON_FAILURE)
// 日曜日ではないこと
//def dayOfWeek = DateTimeFormatter.ofPattern('E').withLocale(Locale.US).format(parsed)
//WebUI.verifyNotEqual(dayOfWeek, 'Sun')      //本当はこっち
//WebUI.verifyEqual(dayOfWeek, 'Sun')           //わざと失敗させてみた

def comment = WebUI.getText(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/p_comment'))
if (comment != null) {
	WebUI.verifyLessThan(comment.length(), 400)
}

// takes Screenshot of the Appointment Confirmation page
Path png4 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_AppointmentConfirmation.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png4.toFile(), 500)


WebUI.click(findTestObject('KatalonDemoAut/Page_AppointmentConfirmation/a_Go to Homepage'))

// ここでホーム・ページに遷移

WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_CuraHomepage/a_Make Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)

// takes Screenshot of the Homepage revisited
Path png5 = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "CURA_Homepage_revisited.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png5.toFile(), 500)

WebUI.closeBrowser()