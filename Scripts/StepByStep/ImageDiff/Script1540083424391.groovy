import java.util.stream.Collectors

import com.kazurayam.ksbackyard.Assert
import com.kazurayam.ksbackyard.ImageCollectionDiffer
import com.kazurayam.materials.ExecutionProfile
import com.kazurayam.materials.FileType
import com.kazurayam.materials.MaterialPair
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.TCaseName
import com.kazurayam.materials.TSuiteName
import com.kms.katalon.core.model.FailureHandling

import internal.GlobalVariable

/**
 * This test case reads 2 sets of PNG files and creates a set of PNG files.
 * 
 * This test case compares 2 img files, calculate how much different these are, and
 * generates 1 ImageDiff file.
 * 
 * This test case makes no interaction with web.
 */
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

// scan the ./Materials directory to make a list of MateriaPair object.
// The latest TSuiteResult 'Test Suites/TS4 - for ImageDiff' with Execution Profile 'google.com' and
// the latest TSuiteResult 'Test Suites/TS4 - for ImageDiff' with Execution Profile 'google.com hl=zh-ON'     are looked up.
// The list will be filtered to include PNG files only.
List<MaterialPair> materialPairs = mr.createMaterialPairs(
		new TSuiteName('Test Suites/StepByStep/TS4 - for ImageDiff'),
		new ExecutionProfile('google.com'),
		new ExecutionProfile('google.com hl=zh-ON')
		).stream().filter { mp ->
			mp.getLeft().getFileType() == FileType.PNG
		}.collect(Collectors.toList())

// make sure the list of MateriaPairs is not empty
Assert.assertTrue(">>> materialPairs.size() is 0. there must be something wrong.",
	materialPairs.size() > 0,
	FailureHandling.STOP_ON_FAILURE)

// make ImageDiff files in the ./Materials/ImageDiff directory
new ImageCollectionDiffer(mr).makeImageCollectionDifferences(
		materialPairs,
		new TCaseName(GlobalVariable.CURRENT_TESTCASE_ID),  // 'Test Cases/ImageDiff'
		3.68)

