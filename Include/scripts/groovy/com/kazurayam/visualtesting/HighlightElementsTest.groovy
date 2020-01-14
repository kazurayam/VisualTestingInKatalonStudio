package com.kazurayam.visualtesting

import static com.kazurayam.ksbackyard.Assert.assertEquals
import static com.kazurayam.ksbackyard.Assert.assertTrue
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


@RunWith(JUnit4.class)
class HighlightElementsTest {

	private URL url = new URL('https://katalon-demo-cura.herokuapp.com/profile.php#login')

	@Test
	void test_createTestObjectWithXPath() {
		// setup:
		String objectId = 'username'
		String xpath = '//input[@id="txt-username"]'
		// when:
		TestObject tobj = HighlightElements.createTestObjectWithXPath(objectId, xpath)
		// then:
		assertEquals("objctId was expected to be \'${objectId}\' but actually was \'${tobj.getObjectId()}\'",
				objectId, tobj.getObjectId())
		assertEquals("xpath was expected to be \'${xpath}\' but actually was \'${tobj.findPropertyValue('xpath')}\'",
				xpath, tobj.findPropertyValue('xpath'))
	}

	@Test
	void test_createTestObjectWithCssSelector() {
		// setup:
		String objectId = 'username'
		String selector = 'input#txt-username'
		// when:
		TestObject tobj = HighlightElements.createTestObjectWithCssSelector(objectId, selector)
		// then:
		assertEquals("objctId was expected to be \'${objectId}\' but actually was \'${tobj.getObjectId()}\'",
				objectId, tobj.getObjectId())
		assertEquals("selector was expected to be \'${selector}\' but actually was \'${tobj.findPropertyValue('css')}\'",
				selector, tobj.findPropertyValue('css'))
	}

	@Test
	void test_highlight() {
		// setup:
		Path dir = Paths.get(RunConfiguration.getProjectDir()).resolve('build/tmp/testOutput/com.kazurayam.visualtesting')
		Files.createDirectories(dir)
		Path log = dir.resolve('login.highlights')
		if (Files.exists(log)) {
			Files.delete(log)
		}
		TestObject tobj = HighlightElements.createTestObjectWithXPath("username", "//input[@id='txt-username']")
		WebUI.openBrowser('')
		WebUI.navigateToUrl(url.toExternalForm())
		WebUI.verifyElementPresent(tobj, 10, FailureHandling.STOP_ON_FAILURE)
		HighlightElements instance = new HighlightElements()
		// when
		instance.highlightElement(tobj)
		WebUI.delay(3)
		WebUI.closeBrowser()
		instance.writeLog(log)
		// then
		assertTrue("log file ${log} is not found",
				Files.exists(log))
		assertTrue("log file should contain a String 'username'",
				((CharSequence)log.toFile().text).contains('username'))
	}
}