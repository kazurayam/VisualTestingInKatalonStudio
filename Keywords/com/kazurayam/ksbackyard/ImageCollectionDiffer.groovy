package com.kazurayam.ksbackyard

import java.nio.file.Path
import java.util.stream.Collectors

import javax.imageio.ImageIO

import com.kazurayam.ksbackyard.ScreenshotDriver.ImageDifference

import com.kazurayam.materials.ExecutionProfile
import com.kazurayam.materials.FileType
import com.kazurayam.materials.Material
import com.kazurayam.materials.MaterialPair
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.TCaseName
import com.kazurayam.materials.TSuiteName
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.KeywordUtil


/**
 * This class is designed to implement the "Visual Testing in Katalon Studio" feature.
 * 
 * This class uses the following 2 external libraries:
 * 1. AShot (https://github.com/yandex-qatools/ashot)
 * 2. Materials (https://github.com/kazurayam/Materials)
 * 
 * The makeImageCollectionDifferences() method provides the core value of this class.
 * This method accepts Materials (image files) to compare them, make differences, and
 * store the diff-images into files.
 * 
 * @author kazurayam
 */
class ImageCollectionDiffer {

	private MaterialRepository mr_
	private ImageDifferenceFilenameResolver idfResolver_
	private VisualTestingListener listener_ = new DefaultVisualTestingListener()


	/**
	 * constructor
	 * 
	 * @param mr
	 * @author kazurayam
	 */
	ImageCollectionDiffer(MaterialRepository mr) {
		mr_ = mr
		idfResolver_ = new DefaultImageDifferenceFilenameResolver()
	}

	/*
	 * Non-argument constructor is required to pass "Test Cases/Test/Prologue" 
	 * which calls `CustomKeywords."${className}.getClass"().getName()`
	 */
	ImageCollectionDiffer() {}

	void setImageDifferenceFilenameResolver(ImageDifferenceFilenameResolver idfResolver) {
		idfResolver_ = idfResolver
	}

	void setVTListener(VisualTestingListener listener) {
		listener_ = listener
	}


	/**
	 * compare 2 Material files in each MaterialPair object, create ImageDiff and
	 * store the diff image files under the directory ./Materials/<tSuiteName>/yyyyMMdd_hhmmss/<tCaseName>.
	 * The difference ratio is compared with the criteriaPercent given. marked FAILED if greater.
	 * 
	 * @param materialPairs created by com.kazurayam.materials.MaterialRpository#createMaterialPairs() method
	 * @param tCaseName     created by com.kazurayam.materials.TCaseName(String)
	 * @param criteriaPercent e.g. 3.00 percent. If the difference of a MaterialPair is greater than this, 
	 *                        the MaterialPair is evaluated FAILED   
	 */
	void makeImageCollectionDifferences(
			List<MaterialPair> materialPairs,
			TCaseName tCaseName,
			Double criteriaPercent) {

		Statistics stats = new Statistics()

		// iterate over the list of Materials
		for (MaterialPair pair : materialPairs) {

			Material expMate = pair.getExpected()
			Material actMate = pair.getActual()

			// create ImageDifference of the 2 given images
			ImageDifference diff = new ImageDifference(
					ImageIO.read(expMate.getPath().toFile()),
					ImageIO.read(actMate.getPath().toFile()))

			// record this pair
			stats.add(diff)

			// resolve the name of output file to save the ImageDiff
			String fileName = idfResolver_.resolveImageDifferenceFilename(
					expMate,
					actMate,
					diff,
					criteriaPercent)

			// resolve the path of output file to save the ImageDiff
			Path pngFile = mr_.resolveMaterialPath(
					tCaseName,
					expMate.getDirpathRelativeToTSuiteResult(),
					fileName)

			// write the ImageDiff into the output file
			ImageIO.write(diff.getDiffImage(), "PNG", pngFile.toFile())

			// verify the diffRatio, fail the test if the ratio is greater than criteria
			if (diff.getRatio() > criteriaPercent && listener_ != null) {
				listener_.failed(">>> diffRatio = ${diff.getRatio()} is exceeding criteria = ${criteriaPercent}")
			}

		}

		// show statistics for making debugging easier
		listener_.info(">>> #makeDiffs ${stats.toString()}")
		listener_.info(">>> #makeDiffs average of diffRatios is ${String.format('%.2f', stats.diffRatioAverage())}")
		listener_.info(">>> #makeDiffs standard deviation of diffRatio is ${String.format('%.2f', stats.evalStandardDeviation())}")
		listener_.info(">>> #makeDiffs recommended criteria is ${String.format('%.2f', stats.evalRecommendedCriteria(1.6))}")
	}



	/**
	 * 
	 * @author kazurayam
	 *
	 */
	static interface ImageDifferenceFilenameResolver {
		String resolveImageDifferenceFilename(
				Material expectedMaterial,
				Material actualMaterial,
				ImageDifference imageDifference,
		Double criteriaPercent)
	}

	/**
	 * 
	 * @author kazurayam
	 *
	 */
	static class DefaultImageDifferenceFilenameResolver implements ImageDifferenceFilenameResolver {
		/**
		 * Given with the following arguments:
		 *     Material expMate:                 'Materials/Main/TS1/20181014_131314/CURA_Homepage' created by TSuiteResult with 'product' profile
		 *     Material actMate:                 'Materials/Main/TS1/20181014_131315/CURA_Homepage' created by TSuiteResult with 'develop' profile
		 *     ImageDifference:                  6.71
		 *     Double criteriaPercent:           3.0
		 *      
		 * @return 'CURA_Homepage.20181014_131314_product-20181014_131315_develop.(6.71)FAILED.png'
		 */
		String resolveImageDifferenceFilename(
				Material expMate,
				Material actMate,
				ImageDifference diff,
				Double criteriaPercent) {

			ExecutionProfile profileExpected = expMate.getParent().getParent().getExecutionPropertiesWrapper().getExecutionProfile()
			ExecutionProfile profileActual   = actMate.getParent().getParent().getExecutionPropertiesWrapper().getExecutionProfile()
			//
			String fileName = expMate.getPath().getFileName().toString()
			String fileId = fileName.substring(0, fileName.lastIndexOf('.'))
			String expTimestamp = expMate.getParent().getParent().getTSuiteTimestamp().format()
			String actTimestamp = actMate.getParent().getParent().getTSuiteTimestamp().format()
			//
			StringBuilder sb = new StringBuilder()
			sb.append("${fileId}.")
			sb.append("${expTimestamp}_${profileExpected}")
			sb.append("-")
			sb.append("${actTimestamp}_${profileActual}")
			sb.append(".")
			sb.append("(${diff.getRatioAsString()})")
			sb.append("${(diff.imagesAreSimilar()) ? '' : 'FAILED'}")
			sb.append(".png")
			return sb.toString()
		}
	}


	/**
	 *
	 */
	class Statistics {

		private List<ImageDifference> list_

		Statistics() {
			list_ = new ArrayList<ImageDifference>()
		}

		void add(ImageDifference diff) {
			list_.add(diff)
		}

		String toString() {
			StringBuilder sb = new StringBuilder()
			sb.append(">>> # diffRatio: ")
			sb.append("[")
			def count = 0
			for (ImageDifference diff : list_) {
				if (count > 0) {
					sb.append(", ")
				}
				sb.append(diff.getRatioAsString())
				count += 1
			}
			sb.append("] percent")
			return sb.toString()
		}

		Double diffRatioAverage() {
			Double sum = 0.0
			for (ImageDifference diff : list_) {
				sum += diff.getRatio()
			}
			return sum / list_.size()
		}

		Double evalStandardDeviation() {
			Double average = this.diffRatioAverage()
			Double s = 0.0
			for (ImageDifference diff : list_) {
				s += (average - diff.getRatio()) * (average - diff.getRatio())
			}
			return Math.sqrt(s / list_.size)
		}

		Double evalRecommendedCriteria(Double factor = 1.5) {
			Double average = this.diffRatioAverage()
			Double stddevi = this.evalStandardDeviation()
			return average + stddevi * factor
		}
	}


	/**
	 *
	 * @author kazurayam
	 *
	 */
	static interface VisualTestingListener {

		void info(String message)

		void failed(String message)

		void fatal(String message)
	}



	/**
	 *
	 * @author kazurayam
	 */
	static class DefaultVisualTestingListener implements VisualTestingListener {

		private KeywordLogger logger = new KeywordLogger()

		void info(String message) {
			logger.logWarning(message)
		}

		void failed(String message) {
			logger.logFailed(message)
			KeywordUtil.markFailed(message)
		}

		void fatal(String message) {
			logger.logFailed(message)
			KeywordUtil.markFailedAndStop(message)
		}
	}

}