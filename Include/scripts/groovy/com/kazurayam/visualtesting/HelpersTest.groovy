package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

@RunWith(JUnit4.class)
class HelpersTest {

	@Test
	void test_lookupAncestorOrSelfPathOfName() {
		// setup:
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path file = projectDir.resolve(Paths.get('Include/scripts/groovy/com/kazurayam/visualtesting/HelpersTest.groovy'))
		println "file is ${file.toString()}"
		String name = 'Include'
		Helpers h = new Helpers()
		// when:
		Path actual = h.lookupAncestorOrSelfPathOfName(file, name)
		println "actual is ${actual.toString()}"
		Path expected = projectDir.resolve('Include')
		// then:
		assertThat(actual, is(notNullValue()))
		assertThat(actual, is(expected))
	}
}