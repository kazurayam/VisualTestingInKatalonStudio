import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/VT/utils/backupAll
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
MaterialStorage    ms = (MaterialStorage)   GlobalVariable[MGV.MATERIAL_STORAGE.getName()]

int count = ms.backup(mr)

WebUI.comment("${count} files were copied from ${mr.getBaseDir().toString()} into ${ms.getBaseDir().toString()}")