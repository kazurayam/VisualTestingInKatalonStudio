import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

new ExecutionProfilesLoader().loadProfile("CURA_DevelopmentEnv")

WebUI.callTestCase(findTestCase("Test Cases/VT/cleanMaterials"), [:])

new ExecutionProfilesLoader().loadProfile("CURA_ProductionEnv")
WebUI.callTestCase(findTestCase("Test Cases/CURA/visitSite"), [:])

new ExecutionProfilesLoader().loadProfile("CURA_DevelopmentEnv")
WebUI.callTestCase(findTestCase("Test Cases/CURA/visitSite"), [:])

WebUI.callTestCase(findTestCase("Test Cases/CURA/imageDiff_twins"), [:])

WebUI.callTestCase(findTestCase("Test Cases/VT/makeIndexAndOpen"), [:])
