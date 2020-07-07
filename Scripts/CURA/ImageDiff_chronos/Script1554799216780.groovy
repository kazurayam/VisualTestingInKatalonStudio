import java.nio.file.Files
import java.nio.file.Path


import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ImageDiffer
import com.kazurayam.visualtesting.ImageDiffsLister
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kazurayam.visualtesting.ImageCollectionDifferDriver.ChronosOptions
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

// Test Cases/CURA/ImageDiff_chronos

/**
 * compare 2 sets of images and produce diff-images
 * and a summary file: comparison-result-bundle.json
 */
assert GVH.isGlobalVariablePresent(MGV.LAST_EXECUTED_TESTSUITE_ID)
String TESTSUITE_ID      = GVH.getGlobalVariableValue(MGV.LAST_EXECUTED_TESTSUITE_ID)     // e.g, 'CURA/chronos_capture'
String EXECUTION_PROFILE = GVH.getGlobalVariableValue(MGV.LAST_APPLIED_EXECUTION_PROFILE) // e.g, 'CURA_DevelopmentEnv' 

ChronosOptions options = new ChronosOptions.Builder().
							filterDataLessThan(2.0).
							shiftCriteriaPercentageBy(0.0).
							build()
ImageDiffer imageDiffer = new ImageDiffer()
boolean result = imageDiffer.runChronos(TESTSUITE_ID, EXECUTION_PROFILE, options)

/**
 * convert comparison-result-bundle.json file into a Markdown text
 */
Path comparison = imageDiffer.getComparisonResultBundleFile()
assert comparison != null
WebUI.comment("${comparison} has been created")
ImageDiffsLister lister = new ImageDiffsLister(comparison)
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
Path misc = mr.getBaseDir().resolve('misc')
Files.createDirectories(misc)
//
Path md = misc.resolve('imageDiffsList.md')
md.toFile().write(lister.toMarkdown(), "utf-8")
WebUI.comment("${md} has been created")

/**
 * and a CSV text as well
 */
Path csv = misc.resolve('imageDiffsList.csv')
csv.toFile().write(lister.toCsv(), "utf-8")
WebUI.comment("${csv} has been created")

/**
 * and a PS1 text (PowerShell script) as well
 */
Path ps1 = misc.resolve('imageDiffsList.ps1')
ps1.toFile().write(lister.toPs1(), "utf-8")
WebUI.comment("${ps1} has been created")


/**
 * 
 */
if (! result ) {
	KeywordUtil.markFailed("One or more pairs of screenshot are different.")
}
