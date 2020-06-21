package com.kazurayam.visualtesting

import java.nio.file.Path

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.TExecutionProfile
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.VisualTestingLogger
import com.kazurayam.visualtesting.ImageCollectionDifferDriver.ChronosOptions
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable

public class ImageDiffer {

	ImageCollectionDifferDriver driver_

	/**
	 * 
	 */
	ImageDiffer() {
		this.driver_ = null
	}

	/**
	 * 
	 * @param testSuiteId
	 * @param options
	 */
	boolean runChronos(String testSuiteId, String executionProfile, ChronosOptions options) {

		Objects.requireNonNull(testSuiteId, "testSuiteId must not be null")
		Objects.requireNonNull(executionProfile, "executionProfile must not be null")
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
		 *     'Materials/47news.chronos_exam/default/yyyyMMdd_hhmmss/ImageDiff_new'
		 * which is in the format of
		 *     'Materials/<current TSuiteName>/<Execution Profile>/<current Timestamp>/<current TCaseName>'
		 */

		driver_ = new ImageCollectionDifferDriver(mr)
		VisualTestingLogger logger = new VisualTestingLoggerImpl()
		driver_.setVisualTestingLogger(logger)

		return driver_.chronos(new TSuiteName(testSuiteId), new TExecutionProfile(executionProfile), ms, options)
	}



	/**
	 * 
	 * @param testSuiteId
	 * @param criteriaPercentage
	 */
	boolean runTwins(String testSuiteId, String executionProfile, double criteriaPercentage) {
		Objects.requireNonNull(testSuiteId, "testSuiteId must not be null")
		Objects.requireNonNull(executionProfile, "executionProfile must not be null")
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
		driver_ = new ImageCollectionDifferDriver(mr)
		VisualTestingLogger logger = new VisualTestingLoggerImpl()
		driver_.setVisualTestingLogger(logger)

		return driver_.twins(new TSuiteName(testSuiteId), new TExecutionProfile(executionProfile), criteriaPercentage)
	}

	/**
	 * 
	 * @return
	 */
	public Path getComparisonResultBundleFile() {
		if (this.driver_ != null) {
			return this.driver_.getComparisonResultBundleFile()
		} else {
			throw new IllegalStateException("this.driver_ is null.")
		}
	}
}
