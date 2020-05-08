package com.kazurayam.visualtesting

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

@RunWith(JUnit4.class)
class VisualTestingListenerImplTest {

	@Test
	void test_resolveProjectDir_default() {
		VisualTestingListenerImpl instance = new VisualTestingListenerImpl()
		String actual = instance.resolveProjectDir()
		String defaultProjectDir = RunConfiguration.getProjectDir()
		WebUI.comment("[test_resolveProjectDir_default] actual=${actual}, defaultProjectDir=${defaultProjectDir}")
		assertThat(actual, is(defaultProjectDir))
	}

	@Test
	void test_getProjectName() {
		// when:
		String projectName = VisualTestingListenerImpl.getProjectName()
		// then:
		assertThat(projectName, is("VisualTestingInKatalonStudio"))
	}

	@Test
	void test_getRelativeTestSuiteId() {
		TestSuiteContext tsc = new MyTestSuiteContext("Success", "Test Suites\\VT\\cleanMaterials")
		String relativeTestSuiteId = VisualTestingListenerImpl.getRelativeTestSuiteId(tsc)
		assertThat(relativeTestSuiteId, is("VT/cleanMaterials"))
	}

	class MyTestSuiteContext implements TestSuiteContext {
		private String status
		private String testSuiteId
		MyTestSuiteContext(String status, String testSuiteId) {
			this.status = status
			this.testSuiteId = testSuiteId
		}
		@Override
		String getStatus() {
			return this.status
		}
		@Override
		String getTestSuiteId() {
			return this.testSuiteId
		}
	}
}
