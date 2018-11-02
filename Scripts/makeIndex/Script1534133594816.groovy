import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialRepositoryFactory
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * This test case creates ./Materials/index.html file
 * 
 * This test case makes no interaction with web.
 * It just read files and write a file.
 */
Path materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
Path index = mr.makeIndex()

WebUI.comment(">>> ${index.toString()} is updated")