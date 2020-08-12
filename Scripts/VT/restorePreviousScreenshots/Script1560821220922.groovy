import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.util.stream.Collectors

import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.RetrievalBy
import com.kazurayam.materials.RetrievalBy.SearchContext
import com.kazurayam.materials.TExecutionProfile
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteResult
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepErrorException as StepErrorException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/CURA/restorePreviousScreenshots
 * 
 * This script restores a TSuiteResult from the Storage directory to 
 * the Materials directory. At first, this script shows a FileChooser dialog
 * that shows you the list of yyyyMMdd_hhmmdd directories stored in 
 * the Storage/testSuiteName/executionProfile directory.
 * The idalog lets you choose one as the basis of image comparison processing.
 * 
 * There would be a few exceptional cases where
 * 1. there is not Storage/testSuiteName/executionProfile directory
 * 2. the Storage/testSuiteName/executionProfile directory is empty
 * 3. user just pushed CACEL button without selecting any yyyyMMdd_hhmmss dir.
 * 
 * In these excetional cases, this script basically tries to find a directory 
 * with STRATEGY="last" --- it will fail with a warning message
 * 
 * >java.lang.IllegalStateException: Only 1 sub directory found under CURA.chronos_capture,CURA_DevelopmentEnv in /Users/kazuakiurayama/katalon-workspace/VisualTestingInKatalonStudio/Materials. Chronos mode requires 2 sub direstories under CURA.chronos_capture. Don't get surprized. Just execute the chronos test suite again. Possibly Chronos mode will work fine next time.
 * 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())

if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_REPOSITORY) ) {
	throw new StepErrorException("MGV.MATERIAL_REPOSITORY is required")
}
MaterialRepository mr = (MaterialRepository)GVH.getGlobalVariableValue(MGV.MATERIAL_REPOSITORY)
assert Files.exists(mr.getBaseDir())

if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_STORAGE)) {
	throw new StepErrorException("MGV.MATERIAL_STORAGE is required")
}
MaterialStorage ms = (MaterialStorage)GVH.getGlobalVariableValue(MGV.MATERIAL_STORAGE)
assert Files.exists(ms.getBaseDir())

if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_TESTSUITE_ID) ) {
	throw new StepErrorException("MGV.CURRENT_TESTSUITE_ID is required")
}
String currentTestSuiteId = GVH.getGlobalVariableValue(MGV.CURRENT_TESTSUITE_ID)

if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_EXECUTION_PROFILE)) {
	throw new StepErrorException("MGV.CURRENT_EXECUTION_PROFILE is required")
}
String currentExecutionProfile = GVH.getGlobalVariableValue(MGV.CURRENT_EXECUTION_PROFILE)


// we identify the directory where we want to find out the screeshots previously taken
TSuiteResult currentTSuiteResult = mr.getCurrentTSuiteResult()
TSuiteName        tsn = currentTSuiteResult.getTSuiteName()
TExecutionProfile tep = currentTSuiteResult.getTExecutionProfile()
TSuiteTimestamp   tst = currentTSuiteResult.getTSuiteTimestamp()

Path executionProfileDirInStorage = ms.getBaseDir().resolve(tsn.getValue()).resolve(tep.getName())
if ( ! Files.exists(executionProfileDirInStorage) ) {
	//throw new StepErrorException("${executionProfileDirInStorage} is not present")
	Files.createDirectories(executionProfileDirInStorage)
}

//
SearchContext context = new SearchContext(ms, tsn, tep)
RetrievalBy by = RetrievalBy.by(tst)
TSuiteResult latestBackupTSR = by.findTSuiteResultBeforeExclusive(context) 
File latestBackup = null
if (latestBackupTSR != null && latestBackupTSR.getTSuiteTimestampDirectory() != null) {
	latestBackup = latestBackupTSR.getTSuiteTimestampDirectory().toFile()
	WebUI.comment("latestBackup=${latestBackup.getName()}")
}

if (CONSOLE_MODE) {
	WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"last"])
	//WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"1hourAgo"])
	//WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"), ["STRATEGY":"18pmLastEvening"])
}
else {
	// You are executing this script in interactive mode
	
	// check if one or more dirs in the executionProfileDirInStorage
	Set<Path> dirSet = Files.list(executionProfileDirInStorage).filter {f -> Files.isDirectory(f)}.collect(Collectors.toSet())
	if (dirSet.size() > 0) {
	
		// prepare a JFileChooser dialog.
		// It will ask user to specify which previous screenshots to choose as the basis of image comparison
		def chooser = new JFileChooser(
			currentDirectory: executionProfileDirInStorage.toFile(),
			dialogTitle: "${projectDir.relativize(executionProfileDirInStorage)}/",
			fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
			fileFilter: [
				getDescription: { ->
					"yyyyMMdd_hhmmss"
				},
				accept:{ f ->
					//f ==~ /\d{8}_\d{6}/ && f.isDirectory()
					f.isDirectory()
				}
			] as FileFilter
		)
		if (latestBackup != null) {
			chooser.setSelectedFile(latestBackup)
			chooser.ensureFileIsVisible(latestBackup)
		}
		// show the JFileChooser dialog
		int ret = chooser.showOpenDialog(null)
		if (ret == JFileChooser.APPROVE_OPTION) {
			File selectedDir = chooser.getSelectedFile()
			if ( ! selectedDir.exists() ) {
				throw new StepErrorException("${selectedDir} does not exist")
			}
			String timestamp = selectedDir.getName()
			WebUI.comment("timestamp=${timestamp} was selected")
			// now restore the screenshots interactively specified
			WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"),
				["STRATEGY":"exactlyAtOrBefore", "timestamp": timestamp ])
		} else if (ret == JFileChooser.CANCEL_OPTION) {
			// find out the lates screenshots and restore it
			WebUI.callTestCase(findTestCase("Test Cases/VT/restorePreviousTSuiteResult"),
				["STRATEGY":"last"])
		} else {
			throw new StepErrorException("You closed the dialog. Stopping the test.")
		}
	
	} else {
		WebUI.comment("no previous record found in ${executionProfileDirInStorage}")
		// no need to show the dialog,
		// we will just go on.
	}
}