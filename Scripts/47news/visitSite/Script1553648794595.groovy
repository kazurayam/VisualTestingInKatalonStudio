import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver

import com.kazurayam.ksbackyard.ScreenshotDriver.Options
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/main/visit47NEWS
 */

/*
 *  Visi a page, take screenshot, save it into the Materials directory.
 *  
 *  The filename must be unique within a test case.
 */
def visitPage(MaterialRepository mr, URL url) {
	// navigate to the Google form page
	WebUI.navigateToUrl(url.toExternalForm())
	WebUI.verifyElementPresent(findTestObject('47news/div_partners'), 10)
	//WebUI.delay(1)
	
	// move mouse cursor off photos. this is necessary because this site
	// reacts against events mouse over photos and images get a bit dark
	//WebUI.mouseOver(findTestObject('Object Repository/47news/div_global-nav'), FailureHandling.OPTIONAL)
	
	// modify the style of <div class="global-nav fixed"> to have position:static
	// to make the screenshot pretty looking
	//JavascriptExecutor js = (JavascriptExecutor)DriverFactory.getWebDriver()
	//js.executeScript("document.head.appendChild(document.createElement(\"style\"))" +
	//	".innerHTML = \".fixed {position: static !important; }\"")
	
	// MaterialRepository#resolveXXXX() methods returns a path file 
	// relative to the baseDir of MaterialRepository instance : the 'Materials' directory.
	Path pathRelativeToMaterialsDir = 
				mr.resolveScreenshotPathByURLPathComponents(
						GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
						url,
						0,
						'top')
				
	// interpret the path relative to the current directory
	Path writablePath = mr.getBaseDir().resolve(pathRelativeToMaterialsDir)
	WebUI.comment("screenshot will be saved into ${writablePath.toString()}")
	
	Options options = new Options.Builder().timeout(200).
						addIgnoredElement(findTestObject('47news/div_main-post02')).
						addIgnoredElement(findTestObject('47news/div_main-bnr')).
						addIgnoredElement(findTestObject('47news/div_sidebar')).
						addIgnoredElement(findTestObject('47news/div_footer-ad')).
						// width(640).
						build()

	// take screenshot image and save into file
	CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
		writablePath.toFile(),
		options)
	
	WebUI.comment("Screenshot of ${url} was saved into ${writablePath.toString()}")
}

// prepare environement
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]

// open browser
WebUI.openBrowser('')

// set appropriate window size
WebUI.setViewPortSize(1100, 700)

assert GlobalVariable.URL_PREFIX != null
String urlPrefix = GlobalVariable.URL_PREFIX

// iterate over URLs listed in the URLs.csv file
TestData testData = TestDataFactory.findTestData('URL_SUBPATHS')
List<List<Object>> allData = testData.getAllData()
for (int index = 0; index < allData.size(); index++) {
	if (DEBUG_MODE == true && index >= MAX_LINES_DEBUG) {
		break;
	}
	List<Object> line = allData.get(index)
	String url = urlPrefix + (String)line.get(0)    // e.g, 'https://www.47news.jp' + '/news'
    // visit the url and take its screenshot
	visitPage(mr, new URL(url))
}

// close browser
WebUI.closeBrowser()

// let MaterialRepository to scan the Materials directory for the updated Material files
mr.scan()


