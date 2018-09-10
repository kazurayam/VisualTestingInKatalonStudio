package com.kazurayam.ksbackyard

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities

import com.kazurayam.materials.FileType
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType


class QuietBrowser {

	@Keyword
	def openBrowser() {
		WebUIDriverType executedBrowser = DriverFactory.getExecutedBrowser()
		switch (executedBrowser) {
			case WebUIDriverType.CHROME_DRIVER:
				System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())
				WebDriver driver = createChromeDriver()
				DriverFactory.changeWebDriver(driver)
				break
			case WebUIDriverType.HEADLESS_DRIVER:
				System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())
				WebDriver driver = createChromeHeadlessDriver()
				DriverFactory.changeWebDriver(driver)
				break
			case WebUIDriverType.FIREFOX_DRIVER:
				System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())
				WebDriver driver = createFirefoxDriver()
				DriverFactory.changeWebDriver(driver)
				break
			default:
				throw new UnsupportedOperationException("Unsuported WebUIDriverType ${executedBrowser}")
		}
	}

	def createChromeDriver() {
		Map<String, Object> chromePreferences = new HashMap<>()
		// Below two preference settings will disable popup dialog when download file
		chromePreferences.put('profile.default_content_settings.popups', 0)
		chromePreferences.put('download.prompt_for_download', false)
		// set directory to save files
		Path downloads = Paths.get(System.getProperty('user.home'), 'Downloads')
		chromePreferences.put('download.default_directory', downloads.toString())
		// disable flash and pdf viewer
		chromePreferences.put('plugins.plugins_disabled',
				[
					'Adobe Flash Player',
					'Chrome PDF Viewer'
				])
		//
		ChromeOptions chromeOptions = new ChromeOptions()
		chromeOptions.setExperimentalOption('prefs', chromePreferences)
		//
		DesiredCapabilities cap = DesiredCapabilities.chrome()
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true)
		cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions)
		return new ChromeDriver(cap)
	}

	def createChromeHeadlessDriver() {
		ChromeOptions o = new ChromeOptions()
		o.addArguments('headless')
		return new ChromeDriver(o)
	}

	def createFirefoxDriver() {
		/**
		 * see https://stackoverflow.com/questions/36309314/set-firefox-profile-to-download-files-automatically-using-selenium-and-java
		 *
		 * see https://developer.mozilla.org/en-US/docs/Archive/Mozilla/Download_Manager_preferences
		 * - browser.download.useDownloadDir : A boolean value that indicates whether or not the user's preference is to automatically save files into the download directory. If this value is false, the user is asked what to do. In Thunderbird and SeaMonkey the default is false. In Other Applications the default is true.
		 * - browser.download.folderList : Indicates the default folder to download a file to. 0 indicates the Desktop; 1 indicates the systems default downloads location; 2 indicates a custom (see: browser.download.dir) folder.
		 * - browser.download.dir : A local folder the user may have selected for downloaded files to be saved. Migration of other browser settings may also set this path. This folder is enabled when browser.download.folderList equals 2.
		 * - browser.download.manager.showWhenStarting : A boolean value that indicates whether or not to show the Downloads window when a download begins. The default value is true.
		 *
		 * - browser.helperApps.neverAsk.saveToDisk :
		 *
		 */
		FirefoxProfile profile = new FirefoxProfile()
		// set location to store files after downloading
		profile.setPreference("browser.download.useDownloadDir", true)
		profile.setPreference("browser.download.folderList", 2)
		Path downloads = Paths.get(System.getProperty('user.home'), 'Downloads')
		profile.setPreference("browser.download.dir", downloads.toString())
		// set preference not to show file download donfirmation dialog
		def mimeTypes = FileType.getAllMimeTypesAsString()
		println "mimeTypes=${mimeTypes}"
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeTypes)
		profile.setPreference("browser.helperApps.neverAsk.openFile", mimeTypes)
		// profile.setPreference("browser.download.manager.showWhenStarting", false) // you can not modify this particular profile any more
		profile.setPreference("pdfjs.disable", true)
		FirefoxOptions options = new FirefoxOptions()
		options.setProfile(profile)
		return new FirefoxDriver(options)
	}






	/**
	 * Refresh browser
	 *
	 @Keyword
	 def refreshBrowser() {
	 KeywordUtil.logInfo("Refreshing")
	 WebDriver webDriver = DriverFactory.getWebDriver()
	 webDriver.navigate().refresh()
	 KeywordUtil.markPassed("Refresh successfully")
	 }
	 */

	/**
	 * Click element
	 * @param to Katalon test object
	 *
	 @Keyword
	 def clickElement(TestObject to) {
	 try {
	 WebElement element = WebUiBuiltInKeywords.findWebElement(to);
	 KeywordUtil.logInfo("Clicking element")
	 element.click()
	 KeywordUtil.markPassed("Element has been clicked")
	 } catch (WebElementNotFoundException e) {
	 KeywordUtil.markFailed("Element not found")
	 } catch (Exception e) {
	 KeywordUtil.markFailed("Fail to click on element")
	 }
	 }
	 */

	/**
	 * Get all rows of HTML table
	 * @param table Katalon test object represent for HTML table
	 * @param outerTagName outer tag name of TR tag, usually is TBODY
	 * @return All rows inside HTML table
	 *
	 @Keyword
	 def List<WebElement> getHtmlTableRows(TestObject table, String outerTagName) {
	 WebElement mailList = WebUiBuiltInKeywords.findWebElement(table)
	 List<WebElement> selectedRows = mailList.findElements(By.xpath("./" + outerTagName + "/tr"))
	 return selectedRows
	 }
	 */
}