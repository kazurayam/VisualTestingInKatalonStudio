import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.material.MaterialRepository
import com.kazurayam.material.MaterialRepositoryFactory
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
Path index = mr.makeIndex()
WebUI.comment(">>> ${index.toString()} is updated")