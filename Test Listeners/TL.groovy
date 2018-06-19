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
	
	static {
		Path materialsDir = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
		// for example, materialsDir = C:/Users/username/temp/ksproject/Materials
		Helpers.ensureDirs(materialsDir)
		
		TestMaterialsRepository tmr = TestMaterialsRepositoryFactory.createInstance(materialsDir)
		GlobalVariable.TEST_MATERIALS_REPOSITORY = tmr
		WebUI.comment("GlobalVariable.TEST_MATERIALS_REPOSITORY has been set with an instance of " +
			"TestMaterialsRepository(${tmr.getBaseDir().toString()})")
	}
	
	
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		
		Path reportDir = Paths.get(RunConfiguration.getReportFolder())
		// for example, reportDir = C:/Users/username/temp/ksproject/Reports/TS1/20180618_165141
		
		// Inform the TestMaterialsRepository object of which Test Suite is current.
		TestMaterialsRepository tmr = (TestMaterialsRepository)GlobalVariable.TEST_MATERIALS_REPOSITORY
		tmr.setCurrentTestSuite(testSuiteContext.getTestSuiteId(), reportDir.getFileName().toString())
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