import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import com.kms.katalon.core.configuration.RunConfiguration
import com.kazurayam.ksbackyard.screenshotsupport.ScreenshotRepository
import java.nio.file.Path
import java.nio.file.Paths

class TL {
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		Path screenshotsDir = Paths.get(RunConfiguration.getProjectDir()).resolve('Screenshots')
		ScreenshotRepository scRepos = new ScreenshotRepository(screenshotsDir, testSuiteContext.getTestSuiteId())
		WebUI.comment(">>> got ScreenshotRepository instance: ${scRepos.toString()}")
		// save the ScreenshotRepository instance into a GlobalVariable
		GlobalVariable.SCREENSHOT_REPOSITORY = scRepos
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		// need explicitly cast the instance of java.lang.Object to its native class
		def scRepos = (ScreenshotRepository)GlobalVariable.SCREENSHOT_REPOSITORY
		WebUI.comment(">>> testSuiteId: ${scRepos.getCurrentTestSuiteId()}")
	}
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		def scRepos = (ScreenshotRepository)GlobalVariable.SCREENSHOT_REPOSITORY
		scRepos.setCurrentTestCaseId(testCaseContext.getTestCaseId())
	}

	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		def scRepos = (ScreenshotRepository)GlobalVariable.SCREENSHOT_REPOSITORY
		scRepos.setCurrentTestCaseStatus(testCaseContext.getTestCaseStatus())
		WebUI.comment(">>> testCaseId: ${scRepos.getCurrentTestCaseId()}")
		WebUI.comment(">>> testCaseStatus: ${scRepos.getCurrentTestCaseStatus()}")
	}

}