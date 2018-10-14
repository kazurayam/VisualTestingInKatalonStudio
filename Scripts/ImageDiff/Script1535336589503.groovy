import com.kazurayam.ksbackyard.ImageCollectionDiffer
import com.kazurayam.materials.ExecutionProfile
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.TCaseName
import com.kazurayam.materials.TSuiteName

import internal.GlobalVariable

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
assert mr != null

ImageCollectionDiffer icd = new ImageCollectionDiffer(mr)
icd.makeDiffs(
	new ExecutionProfile('product'), 
	new ExecutionProfile('develop'),
	new TSuiteName('Main/TS1'),
	new TCaseName(GlobalVariable.CURRENT_TESTCASE_ID),
	3.68)