import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ImageDiffer
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// Test Cases/CURA/ImageDiff_twins

/**
 * compare 2 sets of images (DevelopmentEnv and ProductionEnv) and compare them
 */
assert GVH.isGlobalVariablePresent(MGV.LAST_EXECUTED_TESTSUITE_ID)
String TESTSUITE_ID      = GVH.getGlobalVariableValue(MGV.LAST_EXECUTED_TESTSUITE_ID)     // e.g, 'CURA/twins_capture'

assert GVH.isGlobalVariablePresent(MGV.LAST_APPLIED_EXECUTION_PROFILE)
String EXECUTION_PROFILE = GVH.getGlobalVariableValue(MGV.LAST_APPLIED_EXECUTION_PROFILE) // e.g, 'CURA_DevelopmentEnv'

double criteriaPercentage = 1.0
ImageDiffer imageDiffer = new ImageDiffer()
boolean result = imageDiffer.runTwins(TESTSUITE_ID, EXECUTION_PROFILE, criteriaPercentage)

// create reports
WebUI.callTestCase(findTestCase("VT/reportImageDiffsList"), ["imageDiffer": imageDiffer])

if (! result ) {
	KeywordUtil.markFailed("One or more pairs of screenshot are different.")
}
