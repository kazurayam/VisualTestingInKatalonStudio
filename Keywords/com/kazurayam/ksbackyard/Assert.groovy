package com.kazurayam.ksbackyard

import static com.kms.katalon.core.model.FailureHandling.CONTINUE_ON_FAILURE

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * This class provides a few JUnit-like assertion methods which accepts a String as message
 * which is emitted when the condition value is found unexpected.
 * 
 * see https://forum.katalon.com/discussion/2269/keywordutil-markfailed-method for failure handling
 */
class Assert {

	/**
	 *
	 */
	@Keyword
	static def assertTrue(String message, Boolean condition,
			FailureHandling flowControl = CONTINUE_ON_FAILURE) {
		if (!condition) {
			stepFailed(message, flowControl)
		}
	}

	@Keyword
	static def assertFalse(String message, Boolean condition,
			FailureHandling flowControl = CONTINUE_ON_FAILURE) {
		if (condition) {
			stepFailed(message, flowControl)
		}
	}


	/**
	 *
	 */
	@Keyword
	static def assertEquals(String message, String expected, String actual,
			FailureHandling flowControl = CONTINUE_ON_FAILURE) {
		if (! expected.equals(actual)) {
			stepFailed(message, flowControl)
		}
	}
	/**
	 *
	 */
	@Keyword
	static def assertEquals(String message, Number expected, Number actual,
			FailureHandling flowControl = CONTINUE_ON_FAILURE) {
		if (expected != actual) {
			stepFailed(message, flowControl)
		}
	}


	static def stepFailed(String message, FailureHandling flowControl) {
		if (flowControl == FailureHandling.OPTIONAL) {
			//println "#stepFailed('${message}',FailureHandling.OPTIONAL)"
			KeywordUtil.logInfo(message)
		} else if (flowControl == FailureHandling.CONTINUE_ON_FAILURE) {
			//println "#stepFailed('${message}',FailureHandling.CONTINUE_ON_FAILURE)"
			KeywordUtil.markFailed(message)
		} else {
			// in the case where flowControl == FailureHandling.STOP_ON_FAILURE
			//println "#stepFailed('${message}',FailureHandling.STOP_ON_FAILURE)"
			KeywordUtil.markFailedAndStop(message)
		}
	}
}
