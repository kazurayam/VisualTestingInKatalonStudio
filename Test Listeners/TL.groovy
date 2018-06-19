import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.carmina.Helpers
import com.kazurayam.carmina.TestMaterialsRepository
import com.kazurayam.carmina.TestMaterialsRepositoryFactory

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

class TL {
	
	static Path reportFolder
	
	static {
		reportFolder = Paths.get(RunConfiguration.getReportFolder())
		// for example, reportFolder = C:/Users/username/temp/ksproject/Reports/TS1/20180618_165141
		
		Path materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
		// for example, resultsFolder = C:/Users/username/temp/ksproject/Materials
		Helpers.ensureDirs(materialsFolder)
		
		TestMaterialsRepository trr = TestMaterialsRepositoryFactory.createInstance(materialsFolder)
		GlobalVariable.TEST_MATERIALS_REPOSITORY = trr
	}
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		// Inform the TestMaterialsRepository object of which Test Suite is current.
		TestMaterialsRepository tmr = (TestMaterialsRepository)GlobalVariable.TEST_MATERIALS_REPOSITORY
		tmr.setCurrentTestSuite(testSuiteContext.getTestSuiteId(), reportFolder.getFileName().toString())
	}
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		GlobalVariable.CURRENT_TESTCASE_ID = testCaseContext.getTestCaseId()
	}

	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		TestMaterialsRepository tmr = (TestMaterialsRepository)GlobalVariable.TEST_MATERIALS_REPOSITORY
		def testCaseId = testCaseContext.getTestCaseId()
		def testCaseStatus = testCaseContext.getTestCaseStatus()
		tmr.setTestCaseStatus(testCaseId, testCaseStatus)
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		TestMaterialsRepository tmr = (TestMaterialsRepository)GlobalVariable.TEST_MATERIALS_REPOSITORY
		tmr.makeIndex()
	}

}