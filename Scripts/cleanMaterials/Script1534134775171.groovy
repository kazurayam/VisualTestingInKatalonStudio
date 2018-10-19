import java.nio.file.Paths
import com.kms.katalon.core.configuration.RunConfiguration
import com.kazurayam.materials.Helpers
import com.kazurayam.materials.MaterialRepository

import internal.GlobalVariable

/**
 * delete the contents of the ./Materials directory
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

Helpers.deleteDirectoryContents(mr.getBaseDir())