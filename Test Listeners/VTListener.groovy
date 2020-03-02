/**
 * Test Listeners/VTListener
 */
import com.kazurayam.visualtesting.VisualTestingListenerImpl

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class VTListener {
	
	static VisualTestingListenerImpl listener = new VisualTestingListenerImpl()
		
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		//WebUI.comment("VTListener#beforeTestSuite() was invoked")
		listener.beforeTestSuite(testSuiteContext)
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		//WebUI.comment("VTListener#beforeTestCase() was invoked")
		listener.beforeTestCase(testCaseContext)
	}
	
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		//WebUI.comment("VTListener#afterTestCase() was invoked")
		listener.afterTestCase(testCaseContext)
	}
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		//WebUI.comment("VTListener#beforeTestSuite() was invoked")
		listener.afterTestSuite(testSuiteContext)
	}
}