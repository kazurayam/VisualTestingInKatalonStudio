package com.kazurayam.visualtesting

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

import org.openqa.selenium.WebDriver

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class MemoryUsageResearch {
	
	static void injectFixtures(MaterialRepository mr, WebDriver driver, URL url) {
		
		// take 10 times more of screenshots
		for (int i = 0; i < 10; i++) {
			WebUI.navigateToUrl(url.toExternalForm())
			WebUI.verifyElementPresent(findTestObject('CURA/Page_Homepage/a_Make Appointment'),
					10, FailureHandling.STOP_ON_FAILURE)
			Path png2 = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "トップ${String.format('%03d',i)}.png")
			com.kazurayam.ksbackyard.ScreenshotDriver.takeEntirePage(driver, png2.toFile(), 100)
			WebUI.comment("saved image into ${png2}")
			WebUI.delay(1)
		}
		Random rand = new Random()
		for (int i = 10; i < 300; i++) {
			// copy scrennshots to add 290 more times while picking up the source randomly amongst the first 10
			int x = rand.nextInt(10)
			Path origin = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "トップ${String.format('%03d',x)}.png")
			Path target = mr.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()], "トップ${String.format('%03d',i)}.png")
			Files.copy(origin, target, StandardCopyOption.REPLACE_EXISTING)
			WebUI.comment("saved copy into ${target}")
		}
		
	}
}
