package com.kazurayam.ksbackyard

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords


class Assert {
	
	/**
	 * Refresh browser
	 */
	@Keyword
	static def assertTrue(String message, Boolean condition) {
		if (!condition) {
			KeywordUtil.markFailed(message)
		}
	}

}