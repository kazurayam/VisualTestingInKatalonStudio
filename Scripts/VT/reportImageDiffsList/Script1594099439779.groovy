import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.visualtesting.ImageDiffer
import com.kazurayam.visualtesting.ImageDiffsLister
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

/**
 * Compile various format of reports about the differences found.
 * The "comparison-result-bundle.json" file is the source of information.
 * This is called by Test Cases/CURA/ImageDiff_chronos and ImageDiff_twins
 */
assert imageDiffer != null


Path comparison = ((ImageDiffer)imageDiffer).getComparisonResultBundleFile()
assert comparison != null
WebUI.comment("${comparison} has been created")

ImageDiffsLister lister = new ImageDiffsLister(comparison)

MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]

Path misc = mr.getBaseDir().resolve('misc')
Files.createDirectories(misc)

/**
 * create a Markdown text
 */
Path md = misc.resolve('imageDiffsList.md')
md.toFile().write(lister.toMarkdown(), "utf-8")
WebUI.comment("${md} has been created")

/**
 * and a CSV text as well
 */
Path csv = misc.resolve('imageDiffsList.csv')
csv.toFile().write(lister.toCsv(), System.getProperty("file.encoding"))
WebUI.comment("${csv} has been created")

