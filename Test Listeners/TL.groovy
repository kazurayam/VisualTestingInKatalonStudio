import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.carmina.Helpers
import com.kazurayam.carmina.TestResultsRepository
import com.kazurayam.carmina.TestResultsRepositoryFactory

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
		TestResultsRepository trr = TestResultsRepositoryFactory.createInstance(resultsDir)
		trr.setCurrentTestSuite(testSuiteContext.getTestSuiteId())
		GlobalVariable.TEST_RESULTS_REPOSITORY = trr
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		TestResultsRepository trr = (TestResultsRepository)GlobalVariable.TEST_RESULTS_REPOSITORY
		assert trr != null
		trr.report()
	}
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (GlobalVariable.TEST_RESULTS_REPOSITORY == null) {
			GlobalVariable.TEST_RESULTS_REPOSITORY = TestResultsRepositoryFactory.createInstance(resultsDir)
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
		TestResultsRepository trr = (TestResultsRepository)GlobalVariable.TEST_RESULTS_REPOSITORY
		assert trr != null
		def testCaseId = testCaseContext.getTestCaseId()
		def testCaseStatus = testCaseContext.getTestCaseStatus()
		trr.setTestCaseStatus(testCaseId, testCaseStatus)
	}

}