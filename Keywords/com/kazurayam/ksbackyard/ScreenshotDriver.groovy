package com.kazurayam.ksbackyard

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import javax.imageio.ImageIO

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider
import ru.yandex.qatools.ashot.shooting.ShootingStrategies
import ru.yandex.qatools.ashot.comparison.ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer
// import com.kazurayam.ksbackyard.test.ashot.AShotMock

/**
 * Wraps the AShot API, WebDriver Screenshot utility. 
 * Provides some add-on features used in "Visual Testing in Katalon Studio"
 * 
 * @author kazurayam
 */
class ScreenshotDriver {

	static private Boolean forceSnapshots_ = false

	static void setForceSnapshots(Boolean wanted) {
		forceSnapshots_ = wanted
	}

	static Path tmpDir_ = Paths.get(RunConfiguration.getProjectDir()).resolve('tmp')

	/**
	 * takes screenshot of the specified WebElement in the target WebPage,
	 * returns it as a BufferedImage object.
	 * 
	 * If the specified webElement is not found, then screenshot of whole page
	 * will be returned.
	 * 
	 * @param webDriver
	 * @param webElement
	 * @return BufferedImage
	 */
	static BufferedImage takeElementImage(WebDriver webDriver, WebElement webElement)
	{
		int timeout = 500
		Screenshot screenshot = new AShot().
				coordsProvider(new WebDriverCoordsProvider()).
				shootingStrategy(ShootingStrategies.viewportPasting(timeout)).
				takeScreenshot(webDriver, webElement)
		return screenshot.getImage()
	}


	/**
	 * This method is solely for DEBUGGING purpose.
	 * This method calls com.kazurayam.ksbackyard.test.ashot.AshotMock class which is a copy of real AShot.
	 * We will insert print statements to investigate the behavior of AShot.
	 * 
	 * @param webDriver
	 * @param webElement
	 * @return
	 *
	 @Keyword
	 static BufferedImage takeElementImage_mock(WebDriver webDriver, WebElement webElement)
	 {
	 int timeout = 500
	 Screenshot screenshot = new AShotMock().
	 coordsProvider(new WebDriverCoordsProvider()).
	 shootingStrategy(ShootingStrategies.viewportPasting(timeout)).
	 takeScreenshot(webDriver, webElement)
	 return screenshot.getImage()
	 }
	 */

	/**
	 * provides the same function as takeElementImage(WebDriver, WebElement).
	 * The WebDriver object is resolved by calling DriverFactory.getWebDriver()
	 * 
	 * @param testObject
	 * @return
	 */
	@Keyword
	static BufferedImage takeElementImage(TestObject testObject)
	{
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement webElement = WebUI.findWebElement(testObject, 30)
		return takeElementImage(webDriver, webElement)
	}

	/*
	 static BufferedImage takeElementImage_mock(TestObject testObject)
	 {
	 WebDriver webDriver = DriverFactory.getWebDriver()
	 WebElement webElement = WebUI.findWebElement(testObject, 30)
	 return takeElementImage_mock(webDriver, webElement)
	 }
	 */

	/**
	 * takes screenshot of the specified WebElement in the target WebPage,
	 * and save it into the output file in PNG format.
	 *
	 * @param webDriver
	 * @param webElement
	 * @param file
	 */
	static void saveElementImage(WebDriver webDriver, WebElement webElement, File file)
	{
		BufferedImage image = takeElementImage(webDriver, webElement)
		ImageIO.write(image, "PNG", file)
	}


	/**
	 * provides the same function as saveElementImage(WebDriver, WebElement, File)
	 * The WebDriver object is resolved by calling DriverFactory.getWebDriver()
	 * 
	 * @param testObject
	 * @param file
	 */
	@Keyword
	static void saveElementImage(TestObject testObject, File file)
	{
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement webElement = WebUI.findWebElement(testObject,30)
		saveElementImage(webDriver, webElement, file)
	}



	/**
	 * takes screenshot of the entire page targeted,
	 * returns it as a BufferedImage object
	 *
	 * @param webDriver
	 * @param webElement
	 * @param timeout millisecond, wait for page to be displayed stable after scrolling downward
	 * @return BufferedImage
	 */
	static BufferedImage takeEntirePageImage(WebDriver webDriver, Integer timeout = 300)
	{
		Screenshot screenshot = new AShot().
				shootingStrategy(ShootingStrategies.viewportPasting(timeout)).
				takeScreenshot(webDriver)
		return screenshot.getImage()
	}


	/**
	 * provides the same function as takeEntirePageImage(WebDriver, Integer)
	 * The WebDriver object is resolved by calling DriverFactory.getWebDriver()
	 * 
	 * @timeout millisecond, wait for page to displayed stable after scrolling downward
	 * @return
	 */
	@Keyword
	static BufferedImage takeEntirePageImage(Integer timeout = 300)
	{
		WebDriver webDriver = DriverFactory.getWebDriver()
		return takeEntirePageImage(webDriver, timeout)
	}

	/**
	 * take the screenshot of the entire page targeted,
	 * and save it into the output file in PNG format.
	 *
	 * @param webDriver
	 * @param webElement
	 * @param output
	 */
	static void saveEntirePageImage(WebDriver webDriver, File file, Integer timeout = 300)
	{
		BufferedImage image = takeEntirePageImage(webDriver, timeout)
		ImageIO.write(image, "PNG", file)
	}

	/**
	 * provides the same function as saveEntirePageImage(WebDriver, File, Integer)
	 * The WebDriver object is resolved by calling DriverFactory.getWebDriver()
	 * 
	 * @param file
	 */
	@Keyword
	static void saveEntirePageImage(File file, Integer timeout = 300)
	{
		WebDriver driver = DriverFactory.getWebDriver()
		saveEntirePageImage(driver, file, timeout)
	}



	/**
	 * similar to saveEntirePageImage(WebDriver, File, Integer)
	 * @deprecated use saveEntirePageImage(File, Integer) instead
	 * @param webDriver
	 * @param file
	 */
	static void takeEntirePage(WebDriver webDriver, File file, Integer timeout = 300)
	{
		saveEntirePageImage(webDriver, file, timeout)
	}








	/**
	 * @deprecated use compareImages(BufferedImage, BufferedImage, Double)
	 * @param expectedImage
	 * @param actualImage
	 * @param criteriaPercent
	 * @return
	 */
	static ImageDifference verifyImages(BufferedImage expectedImage,
			BufferedImage actualImage, Double criteriaPercent)
	{
		return compareImages(expectedImage, actualImage, criteriaPercent)
	}

	/**
	 * compare 2 images, calculate the magnitude of difference between the two
	 * 
	 * @param BufferedImage expectedImage
	 * @param BufferedImage actualImage
	 * @param Double criteriaPercentage, e.g. 90.0%
	 * @return ImageDifference object which represents how much different the input 2 images are
	 */
	static ImageDifference compareImages(
			BufferedImage expectedImage,
			BufferedImage actualImage,
			Double criteriaPercent)
	{
		ImageDifference imgDifference =
				new ImageDifference(expectedImage, actualImage)
		imgDifference.setCriteria(criteriaPercent)
		return imgDifference
	}

	/**
	 * @param expectedImage of java.io.File prepared beforehand using saveElementImage(File) method
	 * @param actualImage of TestObject which points HTML element in question
	 * @return ImageDifference object which contains comparison result
	 */
	static ImageDifference compareImages(
			File expected,
			TestObject actual,
			Double criteriaPercent)
	{
		BufferedImage exp = ImageIO.read(expected)
		BufferedImage act = takeElementImage(actual)
		ImageDifference imgDifference = compareImages(exp, act, criteriaPercent)
		return imgDifference
	}

	/**
	 * @param expectedImage of java.io.File prepared beforehand using saveElementImage(File) method
	 * @param actualImage of TestObject which points HTML element in question
	 * @return ImageDifference object which contains comparison result
	 */
	static ImageDifference compareImages(
			TestObject expected,
			TestObject actual,
			Double criteriaPercent)
	{
		BufferedImage exp = takeElementImage(expected)
		BufferedImage act = takeElementImage(actual)
		ImageDifference imgDifference = compareImages(exp, act, criteriaPercent)
		return imgDifference
	}

	/**
	 * Compare 2 images, expected one is read from file, actual one is cropped from web page,
	 * and check if images are SIMILAR enough.
	 * When failed, the actual image is saved into file of which path is shown in the error message.
	 *
	 * @param expectedImage of java.io.File prepared beforehand using saveElementImage(File) method
	 * @param actualImage of TestObject which points HTML element in question
	 * @return true if expectedImage and actualImage are similar enough; difference ratio < criteriaPercent
	 */
	@Keyword
	static Boolean verifyImagesAreSimilar(
			File expected,
			TestObject actual,
			Double criteriaPercent,
			File snapshotsDir = tmpDir_,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE)
	{
		ImageDifference imgDifference = compareImages(expected, actual, criteriaPercent)
		boolean result = imgDifference.imagesAreSimilar()
		ImageDifferenceSerializer serializer =
				new ImageDifferenceSerializer(imgDifference, snapshotsDir.toPath(),
				'verifyImagesAreSimilar(File,TestObject)')
		if (!result || forceSnapshots_) {
			serializer.serialize()
		}
		com.kazurayam.ksbackyard.Assert.assertTrue(
				"images are expected to be similar but are different," +
				" difference=${imgDifference.getRatioAsString()}%," +
				" snapshots were saved in ${snapshotsDir.toString()}",
				result, flowControl)
		return result
	}

	/**
	 * Compare 2 images, expected one is read from file, actual one is cropped from web page,
	 * and check if images are DIFFERENT enough.
	 * When failed, the actual image is saved into file of which path is shown in the error message.
	 * 
	 * @param expectedImage of java.io.File prepared beforehand using saveElementImage(File) method
	 * @param actualImage of TestObject which points HTML element in question
	 * @return true if expecteImage and actualImage are different enough; differenece ratio > criteriaPercent
	 */
	@Keyword
	static Boolean verifyImagesAreDifferent(
			File expected,
			TestObject actual,
			Double criteriaPercent,
			File snapshotsDir = tmpDir_,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE)
	{
		ImageDifference imgDifference = compareImages(expected, actual, criteriaPercent)
		boolean result = imgDifference.imagesAreDifferent()
		ImageDifferenceSerializer serializer =
				new ImageDifferenceSerializer(imgDifference, snapshotsDir.toPath(),
				'verifyImagesAreDifferent(File,TestObject)')
		if (!result || forceSnapshots_) {
			serializer.serialize()
		}
		com.kazurayam.ksbackyard.Assert.assertTrue(
				"images are expected to be different but are similar," +
				" difference=${imgDifference.getRatioAsString()}%," +
				" snapshots were saved in ${snapshotsDir.toString()}",
				result, flowControl)
		return result
	}


	@Keyword
	static Boolean verifyImagesAreSimilar(
			TestObject expected,
			TestObject actual,
			Double criteriaPercent,
			File snapshotsDir = tmpDir_,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE)
	{
		ImageDifference imgDifference = compareImages(expected, actual, criteriaPercent)
		// check if these are similar?
		boolean result = imgDifference.imagesAreSimilar()
		ImageDifferenceSerializer serializer =
				new ImageDifferenceSerializer(imgDifference, snapshotsDir.toPath(),
				'verifyImagesAreSimilar(TestObject,TestObject)')
		if (!result || forceSnapshots_) {
			serializer.serialize()
		}
		com.kazurayam.ksbackyard.Assert.assertTrue(
				"images are expected to be similar but different, " +
				" difference=${imgDifference.getRatioAsString()}%," +
				" snapshots were saved in ${snapshotsDir.toString()}",
				result, flowControl)
		return result
	}

	@Keyword
	static Boolean verifyImagesAreDifferent(
			TestObject expected,
			TestObject actual,
			Double criteriaPercent,
			File snapshotsDir = tmpDir_,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE)
	{
		ImageDifference imgDifference = compareImages(expected, actual, criteriaPercent)
		// check if these are different?
		boolean result = imgDifference.imagesAreDifferent()
		ImageDifferenceSerializer serializer =
				new ImageDifferenceSerializer(imgDifference, snapshotsDir.toPath(),
				'verifyImagesAreDifferent(TestObject,TestObject)')
		if (!result || forceSnapshots_) {
			serializer.serialize()
		}
		com.kazurayam.ksbackyard.Assert.assertTrue(
				"images are expected to be different but similar. " +
				" difference=${imgDifference.getRatioAsString()}%," +
				" snapshots were saved in ${snapshotsDir.toString()}",
				result, flowControl)
		return result
	}




	/**
	 * @return timestamp string of now in the format yyyyMMdd_HHmmss
	 */
	public static getTimestampNow()
	{
		ZonedDateTime now = ZonedDateTime.now()
		return DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(now)
	}


	/**
	 * encloses 3 Path objects; expected, actual and diff
	 * resolves 3 file paths, writes images into files
	 * 
	 * @author kazurayam
	 *
	 */
	static class ImageDifferenceSerializer {

		private ImageDifference imgDifference_
		private Path outputDirectory_
		private Path expected_
		private Path actual_
		private Path diff_

		ImageDifferenceSerializer(ImageDifference imgDifference, Path outputDirectory, String identifier)
		{
			imgDifference_ = imgDifference
			outputDirectory_ = outputDirectory
			expected_ = outputDirectory.resolve(identifier + ".expected.png")
			actual_   = outputDirectory.resolve(identifier + ".actual.png")
			diff_     = outputDirectory.resolve(identifier + ".diff(${imgDifference.getRatioAsString()}).png")
		}

		Path getExpected() {
			return expected_
		}

		Path getActual() {
			return actual_
		}

		Path getDiff() {
			return diff_
		}

		void serialize() {
			Files.createDirectories(outputDirectory_)
			ImageIO.write(imgDifference_.getExpectedImage(), "PNG", expected_.toFile())
			ImageIO.write(imgDifference_.getActualImage(),   "PNG", actual_.toFile())
			ImageIO.write(imgDifference_.getDiffImage(),     "PNG", diff_.toFile())
		}
	}



	/**
	 * accepts 2 BufferedImages as input, compare them, make a difference image,
	 * and calcurate the ratio of difference of the 2 input images.
	 */
	static class ImageDifference {

		private BufferedImage expectedImage_
		private BufferedImage actualImage_
		private BufferedImage diffImage_
		private Double ratio_ = 0.0        // percentage
		private Double criteria_ = 1.0     // percentage

		ImageDifference()
		{
			expectedImage_ = null
			actualImage_ = null
		}

		ImageDifference(BufferedImage expected, BufferedImage actual)
		{
			expectedImage_ = expected
			actualImage_ = actual
			ImageDiff imgDiff = makeImageDiff(expectedImage_, actualImage_)
			ratio_ = calculateRatioPercent(imgDiff)
			diffImage_ = imgDiff.getMarkedImage()
		}

		private ImageDiff makeImageDiff(BufferedImage expected, BufferedImage actual)
		{
			Screenshot expectedScreenshot = new Screenshot(expected)
			Screenshot actualScreenshot = new Screenshot(actual)
			ImageDiff imgDiff = new ImageDiffer().makeDiff(expectedScreenshot, actualScreenshot)
			return imgDiff
		}

		BufferedImage getExpectedImage() {
			expectedImage_
		}

		BufferedImage getActualImage() {
			actualImage_
		}

		BufferedImage getDiffImage() {
			return diffImage_
		}

		void setCriteria(Double criteria) {
			criteria_ = criteria
		}

		Double getCriteria() {
			return criteria_
		}

		/**
		 *
		 * @return e.g. 0.23% or 90.0%
		 */
		Double getRatio() {
			return ratio_
		}

		/**
		 * @return e.g. "0.23" or "90.00"
		 */
		String getRatioAsString(String fmt = '%1$.2f') {
			return String.format(fmt, this.getRatio())
		}

		/**
		 *
		 * Round up 0.0001 to 0.01
		 *
		 * @param diff
		 * @return
		 */
		private Double calculateRatioPercent(ImageDiff diff) {
			boolean hasDiff = diff.hasDiff()
			if (!hasDiff) {
				return 0.0
			}
			int diffSize = diff.getDiffSize()
			int area = diff.getMarkedImage().getWidth() * diff.getMarkedImage().getHeight()
			Double diffRatio = diff.getDiffSize() / area * 100
			BigDecimal bd = new BigDecimal(diffRatio)
			BigDecimal bdUP = bd.setScale(2, BigDecimal.ROUND_UP);  // 0.001 -> 0.01
			return bdUP.doubleValue()
		}


		/**
		 * @return true if the expected image and the actual image pair has
		 *         greater difference than the criteria = these are different enough,
		 *         otherwise false.
		 */
		Boolean imagesAreDifferent() {
			return (ratio_ > criteria_)
		}

		/**
		 * @return true if the expected image and the actual image pair has
		 *         smaller difference than the criteria = these are similar enough,
		 *         otherwise false.
		 */
		Boolean imagesAreSimilar() {
			return (ratio_ <= criteria_)
		}
	}
}
