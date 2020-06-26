import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.Path

import org.openqa.selenium.WebDriver

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

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

assert GlobalVariable.Hostname != null
//URL url = new URL("http://${GlobalVariable.Hostname}/")
URL url = new URL("http://advance.quote.nomura.co.jp/meigara/nomura2/calendarlist.asp")

WebUI.navigateToUrl(url.toExternalForm())
TestObject tObj = new TestObject()
tObj.addProperty("xpath", ConditionType.EQUALS, "//*[contains(.,'検索結果')]")
WebUI.verifyElementPresent(tObj, 15, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(1)


Path png1 = mr.resolveScreenshotPathByURLPathComponents(
					GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
					url,
					0,
					'home')
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage'(driver, png1.toFile(), 100)
WebUI.comment("saved image into ${png1}")

Random rand = new Random()
for (int i = 0; i < 30; i++) {
	// copy scrennshots to add 290 more times while picking up the source randomly amongst the first 10
	int x = rand.nextInt(10)
	Path target = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "copy${String.format('%03d',i)}.png")
	Files.copy(png1, target, StandardCopyOption.REPLACE_EXISTING)
	WebUI.comment("saved copy into ${target}")
}


WebUI.closeBrowser()