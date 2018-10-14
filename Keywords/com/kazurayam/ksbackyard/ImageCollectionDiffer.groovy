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
 * 
 * This class depends on 2 external libraries:
 * 1. AShot (https://github.com/yandex-qatools/ashot)
 * 2. Materials (https://github.com/kazurayam/Materials)
 * 
 * @author kazurayam
 */
class ImageCollectionDiffer {

	private MaterialRepository mr_
	private VisualTestingListener listener_ = new DefaultVisualTestingListener()


	ImageCollectionDiffer(MaterialRepository mr) {
		mr_ = mr
	}

	/*
	 * Non-argument constructor is required to pass "Test Cases/Test/Prologue" 
	 * which calls `CustomKeywords."${className}.getClass"().getName()`
	 */
	ImageCollectionDiffer() {}

	void setVTListener(VisualTestingListener listener) {
		listener_ = listener
	}

	/**
	 *
	 * @param profileExpected e.g. 'product'
	 * @param profileAcutual  e.g. 'develop'
	 * @param tSuiteName        e.g. new TSuiteName('Test Suites/Main/TS1')
	 * @param tCaseName         e.g. new TCaseName('Test Cases/main/ImageDiff')
	 * @param criteriaPercent e.g.  3.83
	 * @return
	 */
	def makeDiffs(ExecutionProfile profileExpected,
			ExecutionProfile profileActual,
			TSuiteName tSuiteName,
			TCaseName tCaseName,
			Double criteriaPercent = 3.0) {

		if (mr_ == null) {
			throw new IllegalStateException('mr_ is null')
		}

		if (profileExpected == null) {
			throw new IllegalArgumentException('profileExcpected is required')
		}
		if (profileActual == null) {
			throw new IllegalArgumentException('profileActual is required')
		}
		if (tSuiteName == null) {
			throw new IllegalArgumentException('tSuiteName is required')
		}
		if (tCaseName == null) {
			throw new IllegalArgumentException('tCaseName is required')
		}

		List<MaterialPair> materialPairs =
				mr_.getRecentMaterialPairs(profileExpected, profileActual, tSuiteName).
				stream().filter { mp ->
					mp.getLeft().getFileType() == FileType.PNG
				}.collect(Collectors.toList())
		if (materialPairs.size() == 0) {
			listener_.fatal(">>> materialPairs.size() is 0. there must be something wrong.")
		}

		Statistics stats = new Statistics()

		for (MaterialPair pair : materialPairs) {
			Material expMate = pair.getExpected()
			Material actMate = pair.getActual()
			ImageDifference diff = new ImageDifference(
					ImageIO.read(expMate.getPath().toFile()),
					ImageIO.read(actMate.getPath().toFile()))

			stats.add(diff)

			String fileName = resolveImageDiffFilename(profileExpected,
					profileActual,
					expMate,
					actMate,
					diff,
					criteriaPercent)

			Path pngFile = mr_.resolveMaterialPath(tCaseName,
					expMate.getDirpathRelativeToTSuiteResult(),fileName)

			ImageIO.write(diff.getDiffImage(), "PNG", pngFile.toFile())

			// verify the diffRatio, fail the test if the ratio is greater than criteria
			if (diff.getRatio() > criteriaPercent && listener_ != null) {
				listener_.failed(">>> diffRatio = ${diff.getRatio()} is exceeding criteria = ${criteriaPercent}")
			}

		}

		// show statistics
		listener_.info(">>> #makeDiffs ${stats.toString()}")
		listener_.info(">>> #makeDiffs average of diffRatios is ${String.format('%.2f', stats.diffRatioAverage())}")
		listener_.info(">>> #makeDiffs standard deviation of diffRatio is ${String.format('%.2f', stats.evalStandardDeviation())}")
		listener_.info(">>> #makeDiffs recommended criteria is ${String.format('%.2f', stats.evalRecommendedCriteria(1.6))}")
	}



	/**
	 *
	 * @return
	 */
	String resolveImageDiffFilename(
			ExecutionProfile profileExpected,
			ExecutionProfile profileActual,
			Material expMate,
			Material actMate,
			ImageDifference diff,
			Double criteriaPercent) {
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