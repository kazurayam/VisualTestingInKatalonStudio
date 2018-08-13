import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

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