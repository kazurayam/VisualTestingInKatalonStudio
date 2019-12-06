import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/CURA/restorePreviousScreenshots
 * 
 * You can choose 1 line out the following 3 lines
 */

WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"last"])
//WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"exactlyAtOrBefore", "timestamp":"20191205_110904" ])
//WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"1hourAgo"])
//WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"18pmLastEvening"])