import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteResultId
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/VT/backupCurrentTSuiteResult
 * 
 * copy the Material files under the ./Materials/<Test Suite Name> directory.
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
MaterialStorage    ms = (MaterialStorage)   GlobalVariable[MGV.MATERIAL_STORAGE.getName()]

WebUI.comment("mr.getCurrentTestSuiteId(): ${mr.getCurrentTestSuiteId()}")

TSuiteResultId currentTSRI = TSuiteResultId.newInstance(
						new TSuiteName(mr.getCurrentTestSuiteId()),
						TSuiteTimestamp.newInstance(mr.getCurrentTestSuiteTimestamp()))

int count = ms.backup(mr, currentTSRI)

WebUI.comment("copied ${count} files of ${currentTSRI} from ${mr.getBaseDir().toString()} to ${ms.getBaseDir().toString()}")