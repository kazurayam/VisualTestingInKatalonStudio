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

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

// scan the ./Materials directory to make a list of MateriaPair object.
// The latest TSuiteResult 'Test Suites/TS1' with ExecutionProfile 'product' and
// the latest TSuiteResult 'Test Suites/TS1' with ExecutionProfile 'develop' are looked up.
// The list will be filtered to include PNG files only.
List<MaterialPair> materialPairs =
// we use Java 8 Stream API to filter entries
mr.createMaterialPairs(
	new TSuiteName('Test Suites/Main/TS1'),
	new ExecutionProfile('product'),
	new ExecutionProfile('develop')
	).stream().filter { mp ->
		mp.getLeft().getFileType() == FileType.PNG
		}.collect(Collectors.toList())

// make sure the list of MateriaPairs is not empty
Assert.assertTrue(">>> materialPairs.size() is 0. there must be something wrong.",
	materialPairs.size() > 0,
	FailureHandling.STOP_ON_FAILURE)

// make ImageDiff files in the ./Materials/ImageDiff directory
ImageCollectionDiffer icd = new ImageCollectionDiffer(mr)
icd.makeImageCollectionDifferences(
		materialPairs,
		new TCaseName(GlobalVariable.CURRENT_TESTCASE_ID),  // 'Test Cases/ImageDiff'
		3.68)

