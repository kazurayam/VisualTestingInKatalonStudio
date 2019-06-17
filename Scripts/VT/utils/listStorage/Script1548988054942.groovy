import com.kazurayam.materials.Material
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.TSuiteResult
import com.kazurayam.visualtesting.ManagedGlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/VT/utils/listStorage
 */
MaterialStorage ms = (MaterialStorage)GlobalVariable[ManagedGlobalVariable.MATERIAL_STORAGE.getName()]

List<TSuiteResult> tsrList = ms.getTSuiteResultList()

def count = 0
for (TSuiteResult tsr : tsrList) {
	List<Material> mateList = tsr.getMaterialList()
	for (Material mate : mateList) {
		WebUI.comment("${mate.getPath().toString()}")
		count += 1
	}
}
WebUI.comment("number of Material files: ${count}")