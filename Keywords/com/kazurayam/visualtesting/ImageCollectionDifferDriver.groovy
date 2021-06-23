package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.Path
import java.util.stream.Collectors

import com.kazurayam.materials.FileType
import com.kazurayam.materials.MaterialPair
import com.kazurayam.materials.MaterialPairs
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.TExecutionProfile
import com.kazurayam.materials.TCaseName
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteResultId
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.materials.VisualTestingLogger
import com.kazurayam.materials.imagedifference.ComparisonResultBundle
import com.kazurayam.materials.imagedifference.ImageCollectionDiffer
import com.kazurayam.materials.stats.ImageDeltaStats
import com.kazurayam.materials.stats.StorageScanner
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class ImageCollectionDifferDriver {

	private MaterialRepository mr_
	private TSuiteName capturingTSuiteName_
	private TExecutionProfile capturingTExecutionProfile_
	private VisualTestingLogger logger_
	private Path imageDeltaStatsJson_
	private Path comparisonResultBundleFile_

	ImageCollectionDifferDriver(MaterialRepository mr) {
		Objects.requireNonNull(mr, "mr must not be null")
		this.mr_                  = mr
		// call MaterialRepository#markAsCurrent() to specify where to save the image diff files
		String tSuiteResultId   = GlobalVariable[MGV.CURRENT_TESTSUITE_ID.getName()]
		String executionProfile = GlobalVariable[MGV.CURRENT_EXECUTION_PROFILE.getName()]
		String tSuiteTimestamp  = GlobalVariable[MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()]
		mr_.markAsCurrent(tSuiteResultId, executionProfile, tSuiteTimestamp)
		def tsr = mr_.ensureTSuiteResultPresent(tSuiteResultId, executionProfile, tSuiteTimestamp)
	}

	void setVisualTestingLogger(VisualTestingLogger logger) {
		this.logger_ = logger
	}

	/**
	 * 
	 * @param capturingTSuiteName
	 * @param ms
	 * @param options
	 */
	public boolean chronos(TSuiteName capturingTSuiteName,
			TExecutionProfile capturingTExecutionProfile,
			MaterialStorage ms,
			ChronosOptions options) {
		Objects.requireNonNull(capturingTSuiteName, "capturingTSuiteName must not be null")
		Objects.requireNonNull(capturingTExecutionProfile, "capturingTExecutionProfile must not be null")
		Objects.requireNonNull(ms, "ms must not be null")
		Objects.requireNonNull(options, "options must not be null")

		// scan the 'Storage' directory to get the statistics of previous runs
		ImageDeltaStats stats = this.createImageDeltaStats(ms,
				capturingTSuiteName,
				capturingTExecutionProfile,
				options)

		// copy image-delta-stats.json file from Storage dir to the Materials dir
		// to bring it visible in the Materials/index.html
		Path toPath1 = mr_.resolveMaterialPath(
				GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
				ImageDeltaStats.IMAGE_DELTA_STATS_FILE_NAME)
		if (this.imageDeltaStatsJson_ != null) {
			Files.copy(this.imageDeltaStatsJson_, toPath1, StandardCopyOption.REPLACE_EXISTING)
		}

		// make image diffs, write the result into the directory named
		// 'Materials/<current TSuiteName>/<current ExecutionProfile>/<current Timestamp>/<current TCaseName>'
		ImageCollectionDiffer imageCollectionDiffer = new ImageCollectionDiffer(this.mr_)
		if (logger_ != null) {
			imageCollectionDiffer.setVisualTestingLogger(logger_)
		}
		MaterialPairs materialPairs =
				this.createMaterialPairsForChronosMode(this.mr_,
				capturingTSuiteName,
				capturingTExecutionProfile)

		boolean result =
				imageCollectionDiffer.makeImageCollectionDifferences(
				materialPairs,
				new TCaseName( GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()] ),
				stats)
		WebUI.comment("${ComparisonResultBundle.SERIALIZED_FILE_NAME} files will be saved into ${imageCollectionDiffer.getOutput()}")

		// save the comparison-result-bundle.json file into the Materials dir
		// to bring it visible in the Materials/index.html
		comparisonResultBundleFile_ =
				mr_.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
				ComparisonResultBundle.SERIALIZED_FILE_NAME)
		Files.copy(imageCollectionDiffer.getOutput(), comparisonResultBundleFile_)
		WebUI.comment("copied into ${comparisonResultBundleFile_}")

		return result
	}

	public Path getComparisonResultBundleFile() {
		return this.comparisonResultBundleFile_
	}

	/**
	 * 
	 * @param tSuiteName
	 * @param mr
	 * @param criteriaPercentage
	 */
	public boolean twins(TSuiteName capturingTSuiteName,
			TExecutionProfile capturingTExecutionProfile,
			double criteriaPercentage)
	{
		Objects.requireNonNull(capturingTSuiteName, "capturingTSuiteName must not be null")
		Objects.requireNonNull(capturingTExecutionProfile, "capturingTExecutionProfile must not be null")

		WebUI.comment(">>> diff image files will be saved into ${mr_.getCurrentTestSuiteDirectory().toString()}")

		ImageCollectionDiffer imageCollectionDiffer = new ImageCollectionDiffer(this.mr_)
		if (logger_ != null) {
			imageCollectionDiffer.setVisualTestingLogger(logger_)
		}

		MaterialPairs materialPairs =
				this.createMaterialPairsForTwinsMode(this.mr_, capturingTSuiteName)

		boolean result =
				imageCollectionDiffer.makeImageCollectionDifferences(
				materialPairs,
				new TCaseName( GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()] ),
				criteriaPercentage
				)

		// save the comparison-result-bundle.json file into the Materials dir
		// to bring it visible in the Materials/index.html
		comparisonResultBundleFile_ =
				mr_.resolveMaterialPath(GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()],
				ComparisonResultBundle.SERIALIZED_FILE_NAME)
		Files.copy(imageCollectionDiffer.getOutput(), comparisonResultBundleFile_)
		WebUI.comment("copied into ${comparisonResultBundleFile_}")

		return result
	}

	/**
	 *
	 * @return
	 */
	private ImageDeltaStats createImageDeltaStats(MaterialStorage     ms,
			TSuiteName        capturingTSuiteName,
			TExecutionProfile capturingTExecutionProfile,
			ChronosOptions    options )
	{
		TSuiteName        examiningTSuiteName        = new TSuiteName(       GlobalVariable[MGV.CURRENT_TESTSUITE_ID.getName()]        )
		TExecutionProfile examiningTExecutionProfile = new TExecutionProfile(GlobalVariable[MGV.CURRENT_EXECUTION_PROFILE.getName()]   )
		TSuiteTimestamp   examiningTSuiteTimestamp   = new TSuiteTimestamp(  GlobalVariable[MGV.CURRENT_TESTSUITE_TIMESTAMP.getName()] )
		TCaseName         examiningTCaseName         = new TCaseName(        GlobalVariable[MGV.CURRENT_TESTCASE_ID.getName()]         )

		Path previousImageDeltaStats =
				StorageScanner.findLatestImageDeltaStats(ms,
				examiningTSuiteName,
				examiningTExecutionProfile,
				examiningTCaseName)
		//
		StorageScanner storageScanner =
				new StorageScanner(
				ms,
				new StorageScanner.Options.Builder()
				.previousImageDeltaStats( previousImageDeltaStats )
				.filterDataLessThan( options.getFilterDataLessThan() )
				.shiftCriteriaPercentageBy( options.getShiftCriteriaPercentageBy() )
				.build()
				)

		if (logger_ != null) {
			storageScanner.setVisualTestingLogger(logger_)
		}

		// calculate the criteriaPercentages for each screenshot images based on the diffs of previous images
		ImageDeltaStats imageDeltaStats =
				storageScanner.scan(capturingTSuiteName,
				capturingTExecutionProfile)

		// persist the image-delta-stats.json into disk.
		// It will be reused as previousImageDeltaStats when this script run next time
		this.imageDeltaStatsJson_ =
				storageScanner.persist(imageDeltaStats,
				examiningTSuiteName,
				examiningTExecutionProfile,
				examiningTSuiteTimestamp,
				examiningTCaseName)

		//
		return imageDeltaStats
	}

	/**
	 * 
	 * @param tSuiteName
	 * @param mr
	 * @return
	 */
	private MaterialPairs createMaterialPairsForChronosMode(MaterialRepository mr,
			TSuiteName capturingTSuiteName,
			TExecutionProfile capturingTExecutionProfile)
	{
		Objects.requireNonNull(mr, "mr must not be null")
		Objects.requireNonNull(capturingTSuiteName, "capturingTSuiteName must not be null")
		Objects.requireNonNull(capturingTExecutionProfile, "capturingTExecutionProfile must not be null")

		MaterialPairs materialPairs =
				mr.createMaterialPairsForChronosMode(capturingTSuiteName, capturingTExecutionProfile)

		if (materialPairs.size() == 0) {
			KeywordUtil.markFailedAndStop(
					"MaterialPairs in the Test Suite \"${capturingTSuiteName.getId()}/${capturingTExecutionProfile.getName()}\" is empty. " +
					"Please make sure your test suite for capturing screenshots successfully ran. " +
					"You may have executed the test suite for the 1st time, " +
					"or you had erased all previous records in the Storage directory. OK. " +
					"Don\'t mind it. " +
					"The last execution has restored a set of screenshots. " +
					"Just try again. " +
					"It sould run OK next time.")
		}

		return materialPairs
	}

	/**
	 *
	 * @param tSuiteName
	 * @param mr
	 * @return
	 */
	private MaterialPairs createMaterialPairsForTwinsMode(MaterialRepository mr,
			TSuiteName capturingTSuiteName)
	{
		Objects.requireNonNull(mr, "mr must not be null")
		Objects.requireNonNull(capturingTSuiteName, "capturingTSuiteName must not be null")

		MaterialPairs materialPairs =
				mr.createMaterialPairsForTwinsMode(capturingTSuiteName)

		if (materialPairs.size() == 0) {
			KeywordUtil.markFailedAndStop(
					"MaterialPairs in the Test Suite \"${capturingTSuiteName.getId()}\" is empty. " +
					"Please make sure your test suite for capturing screenshots successfully ran. " +
					"You may have executed the test suite for the 1st time, " +
					"or you had erased all previous records in the Storage directory. OK. " +
					"Don\'t mind it. " +
					"The last execution has restored a set of screenshots. " +
					"Just try again. " +
					"It sould run OK next time.")
		}

		return materialPairs
	}


	/**
	 *
	 */
	static class ChronosOptions {
		private double filterDataLessThan
		private double shiftCriteriaPercentageBy

		static class Builder {
			private double filterDataLessThan
			private double shiftCriteriaPercentageBy
			Builder() {
				this.filterDataLessThan = 0.0
				this.shiftCriteriaPercentageBy = 0.0
			}
			Builder filterDataLessThan(double value) {
				if (value < 0.0) {
					throw new IllegalArgumentException("filterDataLessThan must not be negative")
				}
				if (value > 100.0) {
					throw new IllegalArgumentException("filterDataLessThan must not be  > 100.0")
				}
				this.filterDataLessThan = value
				return this
			}
			Builder shiftCriteriaPercentageBy(double value) {
				if (value < 0.0) {
					throw new IllegalArgumentException("shiftCriteriaPercentageBy must not be negative")
				}
				if (value > 100.0) {
					throw new IllegalArgumentException("shiftCriteriaPercentageBy must not be > 100.0")
				}
				this.shiftCriteriaPercentageBy = value
				return this
			}
			ChronosOptions build() {
				return new ChronosOptions(this)
			}
		}

		private ChronosOptions(Builder builder) {
			this.filterDataLessThan = builder.filterDataLessThan
			this.shiftCriteriaPercentageBy = builder.filterDataLessThan
		}

		double getFilterDataLessThan() {
			return this.filterDataLessThan
		}

		double getShiftCriteriaPercentageBy() {
			return this.shiftCriteriaPercentageBy
		}

		@Override
		String toString() {
			this.toJsonText()
		}

		String toJsonText() {
			StringBuilder sb = new StringBuilder()
			sb.append("{")
			//
			sb.append("\"shiftCriteriaPercentageBy\":")
			sb.append(this.getShiftCriteriaPercentageBy())
			sb.append(",")
			//
			sb.append("\"filterDataLessThan\":")
			sb.append(this.getFilterDataLessThan())
			sb.append("}")
			return sb.toString()
		}
	}
}
