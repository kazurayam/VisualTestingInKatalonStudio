import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.ImageCapabilities

import org.openqa.selenium.firefox.FirefoxBinary

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.configuration.RunConfiguration


/**
 * This test case retrieve various information about runtime environement
 * 
 */


/**
 * know of the System environement
 */
Properties props = System.getProperties()
List<String> keys = new ArrayList<String>(props.keySet())
Collections.sort(keys)
for (String key : keys) {
//	WebUI.comment("${key}:${props.get(key)}")
}
WebUI.comment(">>> user.home : ${props.get('user.home')}")
WebUI.comment(">>> user.dir : ${props.get('user.dir')}")
WebUI.comment(">>> java.home : ${props.get('java.home')}")


/**
 * know of RunConfiguration
 */
WebUI.comment(">>> RunConfiguration.getOS()=${RunConfiguration.getOS()}")  // 'Windows 7 64bit', 'Mac OSX xxx' etc
WebUI.comment(">>> RunConfiguration.getProjectDir()=${RunConfiguration.getProjectDir()}")  // 'Windows 7 64bit', 'Mac OSX xxx' etc
WebUI.comment(">>> RunConfiguration.getReportFolder()=${RunConfiguration.getReportFolder()}")  // 'Windows 7 64bit', 'Mac OSX xxx' etc
Map<String, Object> driverPreferencesProperties = RunConfiguration.getDriverPreferencesProperties("WebUI")
for (String key : driverPreferencesProperties.keySet()) {
	WebUI.comment(">>> DriverPreferencesProperties['${key}']=${driverPreferencesProperties.get(key)}")
}
Map<String, Object> executionGeneralProperties = RunConfiguration.getExecutionGeneralProperties()
for (String key : executionGeneralProperties.keySet()) {
	WebUI.comment(">>> ExecutionGeneralProperties['${key}']=${executionGeneralProperties.get(key)}")
}

/**
 * Proxy
 */
//>>> ExecutionGeneralProperties[proxy]={"proxyOption":"MANUAL_CONFIG","proxyServerType":"HTTP","username":"","password":"","proxyServerAddress":"172.24.2.10","proxyServerPort":8080}

/**
 * know of DriverFactory
 */
WebUI.comment(">>> DriverFactory.getChromeDriverPath()=${DriverFactory.getChromeDriverPath()}") // null
WebUI.comment(">>> DriverFactory.getExecutedBrowser()=${DriverFactory.getExecutedBrowser()}")   // Firefox
WebUI.comment(">>> DriverFactory.getGeckoDriverPath()=${DriverFactory.getGeckoDriverPath()}")   // C:\Katalon_Studio_Windows_64-5.4.2\configuration\resources\drivers\firefox_win64\geckodriver.exe
WebUI.comment(">>> DriverFactory.getRemoteWebDriverServerType()=${DriverFactory.getRemoteWebDriverServerType()}") // null

/**
 * know of ExecuitedBrowser
 * 
 * see DriverFactory
 */
WebUI.comment(">>> DriverFactory.getExecutedBrowser()=${DriverFactory.getExecutedBrowser()}")   // Firefox
WebUI.comment(">>> RunConfiguration.getDriverSystemProperty('WebUI','browserType')=${RunConfiguration.getDriverSystemProperty('WebUI','browserType')}")
WebUI.comment(">>> RunConfiguration.getDriverSystemProperty('Mobile','devicePlatform')=${RunConfiguration.getDriverSystemProperty('Mobile','devicePlatform')}")
		
import com.kms.katalon.core.webui.driver.WebUIDriverType
WebUIDriverType executedBrowser = DriverFactory.getExecutedBrowser()
switch(executedBrowser) {
	case WebUIDriverType.FIREFOX_DRIVER:          // "Firefox"
	case WebUIDriverType.IE_DRIVER:               // "IE"
	case WebUIDriverType.CHROME_DRIVER:           // "Chrome"
	case WebUIDriverType.SAFARI_DRIVER:           // "Safari"
	case WebUIDriverType.REMOTE_WEB_DRIVER:       // "Remote"
	case WebUIDriverType.ANDROID_DRIVER:          // "Android"
	case WebUIDriverType.IOS_DRIVER:              // "iOS"
	case WebUIDriverType.EDGE_DRIVER:             // "Edge"
	case WebUIDriverType.REMOTE_FIREFOX_DRIVER:   // "Remote Firefox"
	case WebUIDriverType.REMOTE_CHROME_DRIVER:    // "Remote Chrome"
	case WebUIDriverType.KOBITON_WEB_DRIVER:      // "Kobiton Device"
	case WebUIDriverType.HEADLESS_DRIVER:         // "Chrome (headless)"
	case WebUIDriverType.FIREFOX_HEADLESS_DRIVER: // "Firefox (headless)"
		WebUI.comment("DriverFactory.getExecutedBrowser()=${DriverFactory.getExecutedBrowser().toString()}")
		break
	default:
		WebUI.comment("unexpected value of DriverFactory.getExecutedBrowser()=${DriverFactory.getExecutedBrowser().toString()}")
}