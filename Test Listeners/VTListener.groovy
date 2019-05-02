import com.kazurayam.visualtesting.VisualTestingListenerImpl

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class VTListener {
	
	static VisualTestingListenerImpl listener = new VisualTestingListenerImpl()
		
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		WebUI.comment("VTListener#beforeTestSuite() was invoked")
		listener.beforeTestSuite(testSuiteContext)
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		WebUI.comment("VTListener#beforeTestCase() was invoked")
		listener.beforeTestCase(testCaseContext)
	}

}