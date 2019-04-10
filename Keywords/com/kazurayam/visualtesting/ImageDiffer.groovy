package com.kazurayam.visualtesting

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.VisualTestingLogger
import com.kazurayam.visualtesting.ImageCollectionDifferDriver.ChronosOptions
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable

public class ImageDiffer {

	@Keyword
	static void runChronos(String testSuiteId, ChronosOptions options) {

		Objects.requireNonNull(testSuiteId, "testSuiteId must not be null")
		Objects.requireNonNull(options, "options must not be null")

		/*
		 * Prepare runtime environment
		 */
		MaterialRepository mr = (MaterialRepository)GlobalVariable[ManagedGlobalVariable.MATERIAL_REPOSITORY.getName()]
		MaterialStorage    ms = (MaterialStorage)GlobalVariable[ManagedGlobalVariable.MATERIAL_STORAGE.getName()]
		assert mr != null
		assert ms != null

		/*
		 * make image diffs, write the result into the directory
		 *     'Materials/47news.chronos_exam/yyyyMMdd_hhmmss/ImageDiff_new' which is
		 *     'Materials/<current TSuiteName>/<current Timestamp>/<cuurent TCaseName>'
		 */

		ImageCollectionDifferDriver driver = new ImageCollectionDifferDriver(mr)
		VisualTestingLogger logger = new VisualTestingLoggerImpl()
		driver.setVisualTestingLogger(logger)

		boolean result = driver.chronos(new TSuiteName(testSuiteId), ms, options)

		if (! result ) {
			KeywordUtil.markFailed("One or more pairs of screenshot are different.")
		}
	}

	@Keyword
	static void runTwins(String testSuiteId, double criteriaPercentage) {
		Objects.requireNonNull(testSuiteId, "testSuiteId must not be null")
		if (criteriaPercentage < 0.0 || criteriaPercentage > 100.0) {
			throw new IllegalArgumentException("criteriaPercentage(${criteriaPercentage} must be a number in range of 0.0 to 100.0)")
		}

		/*
		 * Prepare runtime environment
		 */
		MaterialRepository mr = (MaterialRepository)GlobalVariable[ManagedGlobalVariable.MATERIAL_REPOSITORY.getName()]
		assert mr != null

		/*
		 * make image diffs, write the result into the directory
		 *     'Materials/47news.chronos_exam/yyyyMMdd_hhmmss/ImageDiff_new' which is
		 *     'Materials/<current TSuiteName>/<current Timestamp>/<cuurent TCaseName>'
		 */
		ImageCollectionDifferDriver driver = new ImageCollectionDifferDriver(mr)
		VisualTestingLogger logger = new VisualTestingLoggerImpl()
		driver.setVisualTestingLogger(logger)

		boolean result = driver.twins(new TSuiteName(testSuiteId), criteriaPercentage)

		if (! result ) {
			KeywordUtil.markFailed("One or more pairs of screenshot are different.")
		}
	}
}
