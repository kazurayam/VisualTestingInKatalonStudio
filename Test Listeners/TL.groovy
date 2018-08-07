import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.material.Helpers
import com.kazurayam.material.MaterialRepository
import com.kazurayam.material.MaterialRepositoryFactory

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable


class TL {
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		def testSuiteId = testSuiteContext.getTestSuiteId()
		
		// for example, reportDir = C:/Users/username/temp/ksproject/Reports/TS1/20180618_165141
		Path reportDir = Paths.get(RunConfiguration.getReportFolder())
		def testSuiteTimestamp = reportDir.getFileName().toString()    // e.g., '20180618_165141'
		
		Path materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
		// for example, materialsFolder == C:/Users/username/katalon-workspace/ksproject/Materials
		Helpers.ensureDirs(materialsFolder)
		
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
		WebUI.comment(">>> testSuiteId is '${testSuiteId}', testSuiteTimestamp is '${testSuiteTimestamp}'")
		mr.putCurrentTestSuite(testSuiteId, testSuiteTimestamp)
		
		GlobalVariable.MATERIAL_REPOSITORY = mr
		WebUI.comment(">>> Instance of MaterialRepository(${mr.getBaseDir().toString()})" +
			" is set to GlobalVariable.MATERIAL_REPOSITORY")
	}
	
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		GlobalVariable.CURRENT_TESTCASE_ID = testCaseContext.getTestCaseId()   //  e.g., 'Test Cases/TC1'
	}

}