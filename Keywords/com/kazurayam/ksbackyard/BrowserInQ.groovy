package com.kazurayam.ksbackyard

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class BrowserInQ {

	@Keyword
	static void openBrowser() {
		WebUIDriverType executedBrowser = DriverFactory.getExecutedBrowser()
		if (executedBrowser == WebUIDriverType.CHROME_DRIVER           &&
		System.getProperty('user.home').startsWith('C:\\Users\\qc')
		) {
			System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())
			WebDriver driver = new ChromeDriver()
			DriverFactory.changeWebDriver(driver)
		} else {
			// open browser as usual
			WebUI.openBrowser('')
		}
	}
}