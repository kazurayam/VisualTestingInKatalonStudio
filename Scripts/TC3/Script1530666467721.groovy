import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.driver.WebUIDriverType

// Test Listener TS1 sets System.setProperty('webdriver.chrome.driver', <path to chromedriver.exe>)
if (System.getProperty('webdriver.chrome.driver') == null) {
	System.setProperty('webdriver.chrome.driver', 'C:\\Katalon_Studio_Windows_64-5.4.2\\configuration\\resources\\drivers\\chromedriver_win32\\chromedriver.exe')
}

// open Chrome browser and let Katalon Studio to use it
WebDriver driver = new ChromeDriver()
DriverFactory.changeWebDriver(driver)

// execute some steps
WebUI.navigateToUrl('http://demoaut.katalon.com')
WebUI.verifyElementPresent(findTestObject("Page_CURA Healthcare Service/a_Make Appointment"), 20)

// close the browser
WebUI.closeBrowser()