import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

import com.kazurayam.ksbackyard.DevicePixelRatioResolver as DPRResolver
import com.kazurayam.ksbackyard.ScreenshotDriver as ScreenshotDriver
import com.kazurayam.ksbackyard.ScreenshotDriver.Options as Options
import com.kazurayam.materials.FileType
import com.kazurayam.materials.MaterialDescription
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebDriver
import org.openqa.selenium.Keys as Keys
import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/CURA/visitSite
 */
WebUI.comment("*** GlobalVariable[${MGV.CURRENT_TESTSUITE_ID.getName()}]=${GlobalVariable[MGV.CURRENT_TESTSUITE_ID.getName()]}")
WebUI.comment("*** GlobalVariable[${MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()}]=${GlobalVariable[MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()]}")

MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
assert mr != null

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 768)
WebDriver driver = DriverFactory.getWebDriver()

// query browser for the Device-Pixel-Ratio of the current platform; e.g, 2.0f for MackBook Air
float dpr = DPRResolver.resolveDPR(driver)

// prepare the options for calling ScreenshotDriver.takeEntirePage(WebDriver, File, ScreenshotDriver.Option)
Options options = new Options.Builder().timeout(500).devicePixelRatio(dpr).build()

assert GlobalVariable.Hostname != null
URL url = new URL("http://${GlobalVariable.Hostname}/")

WebUI.navigateToUrl(url.toExternalForm())
WebUI.verifyElementPresent(findTestObject('CURA/Page_Homepage/a_Make Appointment'),
		10, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(1)


Path png1 = mr.resolveScreenshotPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'screenshots',
					url,
					0,
					'home',
					new MaterialDescription("1", "Home page"))
ScreenshotDriver.saveEntirePageImage(driver, png1.toFile(), options)
WebUI.comment("saved image into ${png1}")

// create one more screenshot file with name in Japanese
Path png2 = mr.resolveMaterialPath(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'screenshots',
					"ホーム.png",
					new MaterialDescription("1", "ホームページ\\"))

ScreenshotDriver.saveEntirePageImage(driver, png2.toFile(), options)
WebUI.comment("saved image into ${png1}")

// if required, save page source as HTML
if (GlobalVariable.SAVE_PAGE_SOURCE != null && GlobalVariable.SAVE_PAGE_SOURCE == true) {
	//Path html = png1.getParent().resolve(png1.getFileName().toString().replace('.png', '.html'))
    Path html = mr.resolveMaterialPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'pagesources',
					url,
					0,
					'home',
					FileType.HTML,
					new MaterialDescription("1", "Home page"))
	String content = driver.getPageSource()
	html.toFile().write(content, 'utf-8')
}



WebUI.callTestCase(findTestCase('CURA/Login'),
	[
		'Username': GlobalVariable.Username,
		'Password': GlobalVariable.Password
	],
	FailureHandling.STOP_ON_FAILURE)


// input hostpital admission
WebUI.selectOptionByValue(findTestObject('CURA/Page_Appointment/select_Tokyo CURA Healthcare C'), 'Hongkong CURA Healthcare Center',
	true)

WebUI.click(findTestObject('CURA/Page_Appointment/input_hospital_readmission'))

WebUI.click(findTestObject('CURA/Page_Appointment/input_programs'))

// choose a same day in the next week
def visitDate = LocalDateTime.now().plusWeeks(1)
def visitDateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(visitDate)
WebUI.setText(findTestObject('CURA/Page_Appointment/input_visit_date'), visitDateStr)
// send ENTER to locase the dialog of Date Picker
WebUI.sendKeys(findTestObject('CURA/Page_Appointment/input_visit_date'), Keys.chord(Keys.ENTER))

// input a comment
WebUI.setText(findTestObject('CURA/Page_Appointment/textarea_comment'), 'This is a comment')


// takes Screenshot of the CURA Appointment page
//Path png3 = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "CURA_Appointment.png")
Path png3 = mr.resolveScreenshotPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'screenshots',
					new URL(WebUI.getUrl()),
					0,
					"default",
					new MaterialDescription("1", "Appointment Input Form"))

ScreenshotDriver.saveEntirePageImage(driver, png3.toFile(), options)
WebUI.comment("saved image into ${png3}")


// transfer to the next page
WebUI.click(findTestObject('CURA/Page_Appointment/button_Book Appointment'))

WebUI.verifyElementPresent(findTestObject('CURA/Page_AppointmentConfirmation/a_Go to Homepage'),
	10, FailureHandling.STOP_ON_FAILURE)

def facility = WebUI.getText(findTestObject('CURA/Page_AppointmentConfirmation/p_facility'))
WebUI.verifyMatch(facility,
	'^(Tokyo|Hongkong|Seoul) CURA Healthcare Center$', true)

def readmission = WebUI.getText(findTestObject('CURA/Page_AppointmentConfirmation/p_hospital_readmission'))
WebUI.verifyMatch(readmission,
	'(Yes|No)', true)

def program = WebUI.getText(findTestObject('CURA/Page_AppointmentConfirmation/p_program'))
WebUI.verifyMatch(program,
	'(Medicare|Medicaid|None)', true)

def visitDateStr2 = WebUI.getText(findTestObject('CURA/Page_AppointmentConfirmation/p_visit_date'))
WebUI.verifyMatch(visitDateStr2,
	'[0-9]{2}/[0-9]{2}/[0-9]{4}',
	true, FailureHandling.CONTINUE_ON_FAILURE)

TemporalAccessor parsed = DateTimeFormatter.ofPattern('dd/MM/uuuu').parse(visitDateStr2)

LocalDateTime visitDate2 = LocalDate.from(parsed).atStartOfDay()
// the date should be in future
boolean isAfterNow = visitDate2.isAfter(LocalDateTime.now())
WebUI.verifyEqual(isAfterNow, true, FailureHandling.CONTINUE_ON_FAILURE)
// the date should not be Sunday
//def dayOfWeek = DateTimeFormatter.ofPattern('E').withLocale(Locale.US).format(parsed)
//WebUI.verifyNotEqual(dayOfWeek, 'Sun')
//WebUI.verifyEqual(dayOfWeek, 'Sun')         // make it fail intentionally

def comment = WebUI.getText(findTestObject('CURA/Page_AppointmentConfirmation/p_comment'))
if (comment != null) {
	WebUI.verifyLessThan(comment.length(), 400)
}

// takes Screenshot of the Appointment Confirmation page
//Path png4 = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "CURA_AppointmentConfirmation.png")
Path png4 = mr.resolveScreenshotPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'screenshots',
					new URL(WebUI.getUrl()),
					0,
					"default",
					new MaterialDescription("1", "Appointment Confirmation page"))

ScreenshotDriver.saveEntirePageImage(driver, png4.toFile(), options)
WebUI.comment("saved image into ${png4}")


WebUI.click(findTestObject('CURA/Page_AppointmentConfirmation/a_Go to Homepage'))

// transfer to the Home page

WebUI.verifyElementPresent(findTestObject('CURA/Page_Homepage/a_Make Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)

// takes Screenshot of the Homepage revisited
//Path png5 = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "CURA_Homepage_revisited.png")
Path png5 = mr.resolveScreenshotPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					'screenshots',
					new URL(WebUI.getUrl()),
					0,
					'revisited',
					new MaterialDescription("1", "Home page revisited"))

ScreenshotDriver.saveEntirePageImage(driver, png5.toFile(), options)
WebUI.comment("saved image into ${png5}")

WebUI.closeBrowser()