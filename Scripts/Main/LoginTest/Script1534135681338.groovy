import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')

WebUI.navigateToUrl("http://${GlobalVariable.Hostname}/")

// ホーム・ページが開く
WebUI.verifyElementPresent(findTestObject('KatalonDemoAut/Page_CuraHomepage/a_Make Appointment'),
	10, FailureHandling.STOP_ON_FAILURE)


// Make AppointmentボタンをクリックしてLogin画面を呼び出しUsernameとPasswordを入力しログインするまでを
// 別のTest Caseで実行する
WebUI.callTestCase(findTestCase('Main/Login'),
	[
		'Username': GlobalVariable.Username,
		'Password': GlobalVariable.Password
	],
	FailureHandling.STOP_ON_FAILURE)

WebUI.closeBrowser()