import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.kazurayam.materials.MaterialRepository

//Path projectDir = Paths.get(RunConfiguration.getProjectDir())
//Path basedir = projectDir.resolve('tmp')
//Path outdir = basedir.resolve('a')
//Files.createDirectories(outdir)
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

WebUI.openBrowser('')
WebUI.setViewPortSize(1279, 720)

assert GlobalVariable.URL != null
WebUI.navigateToUrl(GlobalVariable.URL)

WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_search/input_q'), 10)
WebUI.setText(findTestObject('StepByStep/Page_Google_search/input_q'), 'katalon')

Path fileFnamedByURL = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_form.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileFnamedByURL.toFile())

WebUI.submit(findTestObject('StepByStep/Page_Google_search/input_q'))
WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_result/div_g_1'), 10)

Path fileRnamedByURL = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_result.png")
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileRnamedByURL.toFile())

WebUI.closeBrowser()