package com.kazurayam.visualtesting

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

@RunWith(JUnit4.class)
class VisualTestingListenerImplTest {

	@Test
	void test_resolveProjectDir_default() {
		String actual = VisualTestingListenerImpl.resolveProjectDir()
		String defaultProjectDir = RunConfiguration.getProjectDir()
		WebUI.comment("[test_resolveProjectDir_default] actual=${actual}, defaultProjectDir=${defaultProjectDir}")
		assertThat(actual, is(defaultProjectDir))
	}

	@Test
	void test_resolveProjectDir_ALTERNATIVE() {
		String alternativeProjectDir = 'G:\\マイドライブ\\VisualTestingWorkspace\\VisualTestingInKatalonStudio'
		GlobalVariableHelpers.addGlobalVariable(VisualTestingListenerImpl.GVNAME_AUX, alternativeProjectDir)
		String actual = VisualTestingListenerImpl.resolveProjectDir()
		WebUI.comment("[test_resolveProjectDir_default] actual=${actual}, defaultProjectDir=${alternativeProjectDir}")
		assertThat(actual, is(alternativeProjectDir))
		GlobalVariableHelpers.addGlobalVariable(VisualTestingListenerImpl.GVNAME_AUX, null)
	}
}
