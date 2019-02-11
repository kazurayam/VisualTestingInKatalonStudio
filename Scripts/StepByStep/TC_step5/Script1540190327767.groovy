import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.ksbackyard.ScreenshotDriver
import com.kazurayam.ksbackyard.ScreenshotDriver.Options
import com.kazurayam.ksbackyard.ScreenshotDriver.Options.Builder
import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * StepByStep/TC_step5
 *
 * The screenshot files have fixed name 'search_form.png' and 'search_result.png'
 * rather than URL-dependent file names.
 *
 * Fixed file name is required to do image file comparison. Why?
 *
 * The following 2 PNG files make a MaterialPair:
 * - .\Materials\StepByStep.TS_step5\20181023_141006\StepByStep.TC_step5\search_form.png
 * - .\Materials\StepByStep.TS_step5\20181023_141008\StepByStep.TC_step5\search_form.png
 *
 * But the following 2 PNG files does not make a MaterialPair:
 * -
 * -
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

WebUI.openBrowser('')
WebUI.setViewPortSize(1279, 720)

assert GlobalVariable.URL != null
WebUI.navigateToUrl(GlobalVariable.URL)

WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_search/input_q'), 10)
WebUI.setText(findTestObject('StepByStep/Page_Google_search/input_q'), 'katalon')
WebUI.delay(1)

//DONT Path fileFnamedByURL = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, urlF)
Path fileF = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_form.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileF.toFile())

WebUI.submit(findTestObject('StepByStep/Page_Google_search/input_q'))

WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_result/div_g_1'), 10)
WebUI.delay(3)
Path fileR = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_result.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileR.toFile())

// take screenshot of Google Result page
// and paint <div id="resultStats"> with a grey rectangle 
// to ignore insignificant difference
Path fileS = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID,
									"search_result.ignoreStats.png")
TestObject resultStats = findTestObject('StepByStep/Page_Google_result/div_resultStats')
Builder builder = new ScreenshotDriver.Options.Builder()
Options options = builder.timeout(300).addIgnoredElement(resultStats).build()
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileS.toFile(),
	options)


WebUI.closeBrowser()
