import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.material.Helpers
import com.kazurayam.material.MaterialRepository
import com.kazurayam.material.MaterialRepositoryFactory

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
	
	static {
		Path materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
		// for example, materialsFolder == C:/Users/username/katalon-workspace/ksproject/Materials
		Helpers.ensureDirs(materialsFolder)
		
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
		GlobalVariable.MATERIAL_REPOSITORY = mr
		WebUI.comment("Instance of MaterialRepository(${mr.getBaseDir().toString()})" + 
			" is set to GlobalVariable.MATERIAL_REPOSITORY")
	}
	
	
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
		MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
		
		// Inform the TestMaterialsRepository object of which Test Suite is current.
		mr.putCurrentTestSuite(testSuiteId, testSuiteTimestamp)
	}
	
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		GlobalVariable.CURRENT_TESTCASE_ID = testCaseContext.getTestCaseId()   //  e.g., 'Test Cases/TC1'
	}
	

	
	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
		Path index = mr.makeIndex()
		WebUI.comment("Material Repository has created ${index.toString()}")
	}

}