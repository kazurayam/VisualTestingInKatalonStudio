import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.RetrievalBy
import com.kazurayam.materials.RetrievalBy.SearchContext
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteResultId
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil

import java.time.LocalDateTime

/**
 * restore the previous TSuiteResult from the Storage dir into the Materials dir
 */


MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
MaterialStorage    ms = (MaterialStorage)   GlobalVariable[MGV.MATERIAL_STORAGE.getName()]
TSuiteName tsn        = new TSuiteName(mr.getCurrentTestSuiteId()) 
TSuiteTimestamp tst   = TSuiteTimestamp.newInstance(mr.getCurrentTestSuiteTimestamp())

int count = 0

switch(STRATEGY) {   // is defined in the Variables tab
	case 'last':
		// restore tha last shot previous to the current
		count = ms.restoreUnary(mr, tsn, RetrievalBy.before(tst))
		break
		
	case '1hourAgo':
		// restore the shot of 1 hour ago or older
		LocalDateTime base = LocalDateTime.now().minusHours(1)
		count = ms.restoreUnary(mr, tsn, RetrievalBy.before(base))
		break
		
	case '18pmLastEvening':
	    // restore the shot of last evenng 18:00 or older
	    LocalDateTime base = LocalDateTime.now().minusDays(1)
		count = ms.restoreUnary(mr, tsn, RetrievalBy.before(base, 18, 0, 0))
		break
		
	default:
		KeywordUtil.markFailedAndStop("unknown STRATEGY: ${STRATEGY}")
} 

WebUI.comment("copied ${count} files from ${mr.getBaseDir().toString()} to ${ms.getBaseDir().toString()}")