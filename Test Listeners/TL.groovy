import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.kstestresults.Helpers
import com.kazurayam.kstestresults.TestResults
import com.kazurayam.kstestresults.TestResultsFactory

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable as GlobalVariable

class TL {
	
	static Path resultsDir = Paths.get(RunConfiguration.getProjectDir()).resolve('Results')
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		//
		Helpers.ensureDirs(resultsDir)
		GlobalVariable.RESULTS_DIR = resultsDir
		//
		TestResults testResults = 
			TestResultsFactory.createInstance(resultsDir, testSuiteContext.getTestSuiteId())
		GlobalVariable.TEST_RESULTS = testResults
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		TestResults testResults = (TestResults)GlobalVariable.TEST_RESULTS
		testResults.report()
	}
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (GlobalVariable.TEST_RESULTS == null) {
			GlobalVariable.TEST_RESULTS = TestResultsFactory.createInstance(resultsDir)
		}
		//
		GlobalVariable.CURRENT_TESTCASE_ID = testCaseContext.getTestCaseId()
	}

	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		TestResults testResults = (TestResults)GlobalVariable.TEST_RESULTS
		def testCaseId = testCaseContext.getTestCaseId()
		def testCaseStatus = testCaseContext.getTestCaseStatus()
		testResults.setTcStatus(testCaseId, testCaseStatus)
	}

}