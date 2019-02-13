import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path

import com.kazurayam.ksbackyard.ScreenshotDriver
import com.kazurayam.ksbackyard.ScreenshotDriver.Options
import com.kazurayam.ksbackyard.ScreenshotDriver.Options.Builder
import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * StepByStep/TC_step10
 *
 * The screenshot files have fixed name 'search_form.png' and 'search_result.ignoreStats.png'
 * rather than URL-dependent file names.
 *
 * The Search Result page has <div id="resultStats"> which displays number of search 
 * hits and processing time. This portion displays different numbers everytime.
 * This portion is always detected "different" when compared.
 * You can specify to "ignore" web elements when you compare 2 images.
 * The idea is that saveEntirePageImage() keyword paints the specified web element with
 * a grey rectangle. The painted portion will be  the same always and no differece will be
 * detected.
 * 
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

WebUI.openBrowser('')
WebUI.setViewPortSize(1279, 720)

assert GlobalVariable.URL != null
WebUI.navigateToUrl(GlobalVariable.URL)

WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_search/input_q'), 10)
WebUI.setText(findTestObject('StepByStep/Page_Google_search/input_q'), 'katalon')

//DONT Path fileFnamedByURL = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, urlF)
Path fileF = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_form.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileF.toFile(),
	1000)   // scrolling timeout: 1.0 second

WebUI.submit(findTestObject('StepByStep/Page_Google_search/input_q'))
WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_result/div_g_1'), 10)

WebUI.delay(3)   // wait for the Search Result page to settle

Path fileS = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID,
									"search_result.png")
// web element to be ignored
TestObject resultStats = findTestObject('StepByStep/Page_Google_result/div_resultStats')

Builder builder = new ScreenshotDriver.Options.Builder()
Options options = builder.
					timeout(2000).    // scrolling timeout 2.0 seconds
					addIgnoredElement(resultStats).    
					// you can give multiple addIgnoreElement() here
					build()          // don't forget to call build()

// take screenshot of Google Result page,
// and paint <div id="resultStats"> with a grey rectangle to ignore insignificant difference
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileS.toFile(),
	options)

WebUI.closeBrowser()
