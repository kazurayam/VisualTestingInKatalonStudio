package com.kazurayam.visualtesting

import com.kazurayam.materials.VisualTestingLogger
import com.kms.katalon.core.util.KeywordUtil

public class VisualTestingLoggerImpl implements VisualTestingLogger {

	VisualTestingLoggerImpl() {}

	@Override
	void info(String message) {
		KeywordUtil.logInfo(message)
	}

	@Override
	void fatal(String message) {
		KeywordUtil.logInfo(message)
	}

	@Override
	void failed(String message) {
		KeywordUtil.logInfo(message)
	}
}
