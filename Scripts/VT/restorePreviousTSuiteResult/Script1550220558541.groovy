import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.RestoreResult
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
import groovy.json.JsonOutput

/**
 * Test Cases/VT/restorePreviousTSuiteResult
 * 
 * restore the previous TSuiteResult from the Storage dir into the Materials dir
 */


MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
WebUI.comment("MaterialRepository#getBaseDir()=${mr.getBaseDir()}")
WebUI.comment("currentTSuiteId is ${mr.getCurrentTestSuiteId()}")
WebUI.comment("currentTSuiteTimestamp is ${mr.getCurrentTestSuiteTimestamp()}")
TSuiteName tsn        = new TSuiteName(mr.getCurrentTestSuiteId())
TSuiteTimestamp tst   = new TSuiteTimestamp(mr.getCurrentTestSuiteTimestamp())

MaterialStorage    ms = (MaterialStorage)   GlobalVariable[MGV.MATERIAL_STORAGE.getName()]
WebUI.comment("MaterialStorage#getBaseDir()=${ms.getBaseDir()}")

RestoreResult restoreResult = null

try {
	switch(STRATEGY) {   // is defined in the Variables tab
		case 'last':
			// restore the last shot previous to the current
			restoreResult = ms.retrievingRestoreUnaryExclusive(mr, tsn, RetrievalBy.by(tst))
			WebUI.comment("STRATEGY:last tst=${tst}")
			WebUI.comment("STRATEGY:last restoreResult.getTSuiteResult().getTSuiteName()=${restoreResult.getTSuiteResult().getTSuiteName()}")
			WebUI.comment("STRATEGY:last restoreResult.getTSuiteResult().getTSuiteTimestamp()=${restoreResult.getTSuiteResult().getTSuiteTimestamp()}")
			WebUI.comment("STRATEGY:last restoreResult.getCount()=${restoreResult.getCount()}")
			break
	
		case '10minutesAgo':
			// restore the shot of 1 hour ago or older
			LocalDateTime base = LocalDateTime.now().minusMinutes(10)
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base))
			break
	
		case '30minutesAgo':
			// restore the shot of 1 hour ago or older
			LocalDateTime base = LocalDateTime.now().minusMinutes(10)
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base))
			break
	
		case '1hourAgo':
			// restore the shot of 1 hour ago or older
			LocalDateTime base = LocalDateTime.now().minusHours(1)
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base))
			break
	
		case '2hoursAgo':
			// restore the shot of 2 hours ago or older
			LocalDateTime base = LocalDateTime.now().minusHours(2)
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base))
			break
	
		case '6amToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 6, 0, 0))
			break
	
		case '9amToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 9, 0, 0))
			break
	
		case '12amToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 12, 0, 0))
			break
	
		case '15pmToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 15, 0, 0))
			break
	
		case '18pmToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 18, 0, 0))
			break
	
		case '21pmToday':
			// restore the shot taken before 6AM today
			LocalDateTime base = LocalDateTime.now()
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 21, 0, 0))
			break
	
		case '18pmLastEvening':
			// restore the shot of last evening 18:00 or older
			LocalDateTime base = LocalDateTime.now().minusDays(1)
			restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base, 18, 0, 0))
			break
	
		case 'exactlyAtOrBefore':
		    // requires additonal argument "timestamp" given as a Variable to this test case
			Objects.requireNonNull(timestamp, "timestamp must not be null")
			try {
				TSuiteTimestamp tSuiteTimestamp = new TSuiteTimestamp(timestamp)
				LocalDateTime base = tSuiteTimestamp.getValue()
				restoreResult = ms.retrievingRestoreUnaryInclusive(mr, tsn, RetrievalBy.by(base))
			} catch (Exception e) {
				throw new IllegalArgumentException("timestamp=\"${timestamp}\" must be in the format of YYYYMMdd_hhmmss")
			}
			break
		
		default:
			KeywordUtil.markFailedAndStop("unknown STRATEGY: ${STRATEGY}")
			break
	}
	
	// check if successfully restored or not
	// if not, STOP it
	if (restoreResult != RestoreResult.NULL) {
		def tSuiteName = restoreResult.getTSuiteResult().getTSuiteName().getAbbreviatedId()
		def tSuiteTimestamp = restoreResult.getTSuiteResult().getTSuiteTimestamp().format()
		WebUI.comment("copied ${restoreResult.getCount()} files" +
			" of ${tSuiteName}/${tSuiteTimestamp}" +
			" from ${ms.getBaseDir().toString()}" +
			" into ${mr.getBaseDir().toString()}")
	} else {
		JsonOutput jo = new JsonOutput()
		throw new IllegalArgumentException("failed to retrieve a TSuiteResult" + 
			" of \n${jo.prettyPrint(tsn.toJsonText())}\n" + 
			" before \n${jo.prettyPrint(tst.toJsonText())}\n" + 
			" from \n${jo.prettyPrint(ms.toJsonText())}\n" +
			" into copy into \n${jo.prettyPrint(mr.toJsonText())}")
	}
	
} catch (Exception e) {
    KeywordUtil.markFailedAndStop("${e.getMessage()}")
}




