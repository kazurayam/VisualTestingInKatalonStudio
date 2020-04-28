import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.configuration.RunConfiguration
import java.io.File
import java.net.URL
import java.nio.file.Paths

/**
 * open the ./Materials/index.html file in browser
 * 
 */
String projectPath = RunConfiguration.getProjectDir()
File index = Paths.get(projectPath).resolve("Materials/index.html").toFile()
URL indexURL = index.toURI().toURL()

WebUI.openBrowser(indexURL.toExternalForm())

//WebUI.delay(20)