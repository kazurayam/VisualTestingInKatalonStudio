package com.kazurayam.ksbackyard

import static com.kazurayam.ksbackyard.Assert.assertTrue

import java.awt.image.BufferedImage
import java.nio.file.Path
import java.util.stream.Collectors

import javax.imageio.ImageIO

import org.openqa.selenium.WebDriver

import com.kazurayam.materials.FileType
import com.kazurayam.materials.Material
import com.kazurayam.materials.MaterialPair
import com.kazurayam.materials.MaterialRepository
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.comparison.ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer
import ru.yandex.qatools.ashot.shooting.ShootingStrategies

public class ScreenshotDriver {

	static MaterialRepository mr_ = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

	static {
		assert mr_ != null
	}

	@Keyword
	static void takeEntirePage(WebDriver webDriver, File file, Integer timeout = 300) {
		Screenshot screenshot = new AShot().
				shootingStrategy(ShootingStrategies.viewportPasting(timeout)).
				takeScreenshot(webDriver)
		ImageIO.write(screenshot.getImage(), "PNG", file)
	}


	@Keyword
	static Double diffRatioPercent(ImageDiff diff) {
		boolean hasDiff = diff.hasDiff()
		if (!hasDiff) {
			return 0.0
		}
		int diffSize = diff.getDiffSize()
		int area = diff.getMarkedImage().getWidth() * diff.getMarkedImage().getHeight()
		Double diffRatio = diff.getDiffSize() / area * 100
		return diffRatio
	}

	@Keyword
	static boolean hasSignificantDiff(ImageDiff diff, Double criteriaPercent) {
		Double diffRatio = ScreenshotDriver.diffRatioPercent(diff)
		if (diffRatio > criteriaPercent) {
			KeywordUtil.markFailed("diffRatio = ${diffRatio} is exceeding criteria = ${criteriaPercent}")
		}
	}



	/**
	 *
	 * @param profileExpected e.g., 'product'
	 * @param profileAcutual  e.g., 'develop'
	 * @param tSuiteName      e.g., 'TS1'
	 * @param criteriaPercent e.g.,  3.83
	 * @return
	 */
	@Keyword
	static def makeDiffs(String profileExpected = 'product', String profileActual = 'develop', String tSuiteName,
			Double criteriaPercent = 3.0) {

		if (tSuiteName == null) {
			throw new IllegalArgumentException('#doDiff argument tSuiteName is required')
		}

		List<MaterialPair> materialPairs = ScreenshotDriver.getScreenshotPairs(profileExpected, profileActual, tSuiteName)
		assertTrue(">>> materialPairs.size() is 0", materialPairs.size() > 0)

		MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY
		assertTrue(">>> GlobalVariable.MATERIAL_REPOSITORY is null", mr != null)

		List<Double> ratios = new ArrayList<Double>()

		for (MaterialPair pair : materialPairs) {
			Material expMate = pair.getExpected()
			Material actMate = pair.getActual()
			BufferedImage expectedImage = ImageIO.read(expMate.getPath().toFile())
			BufferedImage actualImage   = ImageIO.read(actMate.getPath().toFile())
			Screenshot expectedScreenshot = new Screenshot(expectedImage)
			Screenshot actualScreenshot   = new Screenshot(actualImage)
			// get diff of the pair of images
			ImageDiff diff = new ImageDiffer().makeDiff(expectedScreenshot, actualScreenshot)

			// get diffRatioPercent
			Float diffRatioPercent = ScreenshotDriver.diffRatioPercent(diff)
			ratios.add(diffRatioPercent)

			// save the diff image into file
			BufferedImage markedImage = diff.getMarkedImage()
			String fileName = expMate.getPath().getFileName().toString()
			String fileId = fileName.substring(0, fileName.lastIndexOf('.'))
			String expTimestamp = expMate.getParent().getParent().getTSuiteTimestamp().format()
			String actTimestamp = actMate.getParent().getParent().getTSuiteTimestamp().format()
			Boolean failed = (diffRatioPercent > criteriaPercent)
			// verify the diff-ratio, fail the test if the ratio is greater than criteria
			if (failed) {
				KeywordUtil.markFailed("diffRatio = ${diffRatioPercent} is exceeding criteria = ${criteriaPercent}")
			}
			Path pngFile = mr.resolveMaterialPath(
					GlobalVariable.CURRENT_TESTCASE_ID,
					"${fileId}.${expTimestamp}_${profileExpected}-${actTimestamp}_${profileActual}" +
					".(${String.format('%.2f', diffRatioPercent)})${(failed) ? 'FAILED' : ''}.png")
			ImageIO.write(markedImage, "PNG", pngFile.toFile())


		}
		// show statistics
		printRatios(ratios)
		Double averageValue = average(ratios)
		println ">>> #makeDiffs averate of diffRatios is ${String.format('%.2f', averageValue)}"
		Double stddevValue = evalStandardDeviation(ratios)
		println ">>> #makeDiffs standard deviation of diffRatio is ${String.format('%.2f', stddevValue)}"
		Double recommendedCriteria = evalRecommendedCriteria(ratios, 1.6)
		println ">>> #makeDiffs recommended criteria is ${String.format('%.2f', recommendedCriteria)}"
	}

	static void printRatios(List<Double> data) {
		StringBuilder sb = new StringBuilder()
		sb.append(">>> #makeIndex ratios is ")
		sb.append("[")
		def count = 0
		for (Double d : data) {
			if (count > 0) {
				sb.append(", ")
			}
			sb.append(String.format('%.2f', d))
			count += 1
		}
		sb.append("] percent")
		println sb.toString()
	}

	static Double average(List<Double> data) {
		Double sum = 0.0
		for (Double d : data) {
			sum += d
		}
		return sum / data.size()
	}

	static Double evalStandardDeviation(List<Double> data) {
		Double average = average(data)
		Double s = 0.0
		for (Double d : data) {
			s += (average - d) * (average - d)
		}
		return Math.sqrt(s / data.size)
	}

	static Double evalRecommendedCriteria(List<Double> data, Double factor = 1.5) {
		Double average = average(data)
		Double stddevi = evalStandardDeviation(data)
		return average + stddevi * factor
	}

	/**
	 *
	 * @param expectedProfile
	 * @param actualProfile
	 * @param testSuiteId
	 * @return
	 */
	static List<MaterialPair> getScreenshotPairs(
			String expectedProfile /* 'product' */,
			String actualProfile   /* 'develop' */,
			String testSuiteId     /* 'Test Suites/TS1' */) {

		List<MaterialPair> list = mr_.getRecentMaterialPairs(expectedProfile, actualProfile, testSuiteId)
		KeywordUtil.logInfo(">>> list.size() is ${list.size()}")
		List<MaterialPair> result = list.stream().filter { mp ->
			mp.getLeft().getFileType() == FileType.PNG
		}.collect(Collectors.toList())
		KeywordUtil.markPassed("returning MaterialPairs successfully")
		return result
	}
}
