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
class VTConfigTest {

	@Test
	void test_getAuxiliaryVTProjectDir() {
		// setup:
		Path caseOutputDir = Paths.get(RunConfiguration.getProjectDir()).resolve(Paths.get('build/tmp/testOutput/VTConfigTest/test_getAuxiliaryVTProjectDir'))
		Files.createDirectories(caseOutputDir)
		File jsonFile = caseOutputDir.resolve(VTConfig.CONFIG_FILE_NAME).toFile()
		jsonFile.text = '''
		{
		    "AUXILIARY_VT_PROJECTS_DIR" : "/Users/myname/tmp"
		}
		'''
		// when:
		VTConfig vtConfig = new VTConfig(jsonFile)
		String actual = vtConfig.getAuxiliaryVTProjectDir()
		// then:
		assertThat(actual, is('/Users/myname/tmp'))
	}
}