package com.kazurayam.ksbackyard

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.comparison.ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider
import ru.yandex.qatools.ashot.shooting.ShootingStrategies

/**
 * 
 * @author kazurayam
 *
 */
class ScreenshotDriver {

	/**
	 * takes screenshot of the specified WebElement in the target WebPage,
	 * returns it as a BufferedImage object
	 * 
	 * @param webDriver
	 * @param webElement
	 * @return BufferedImage
	 */
	@Keyword
	static BufferedImage takeElementImage(WebDriver webDriver, WebElement webElement) {
		Screenshot screenshot = new AShot().
				coordsProvider(new WebDriverCoordsProvider()).
				takeScreenshot(webDriver, webElement)
		return screenshot.getImage()
	}

	/**
	 * provides the same function as takeElementImage(WebDriver, WebElement)
	 * 
	 * @param testObject
	 * @return
	 */
	@Keyword
	static BufferedImage takeElementImage(TestObject testObject) {
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement webElement = WebUI.findWebElement(testObject, 30)
		return takeElementImage(webDriver, webElement)
	}

	@Keyword
	static void saveElementImage(WebDriver webDriver, WebElement webElement, File file) {
		BufferedImage image = takeElementImage(webDriver, webElement)
		ImageIO.write(image, "PNG", file)
	}

	/**
	 * provides the same function as saveElementImage(WebDriver, WebElement, File)
	 * @param testObject
	 * @param file
	 */
	@Keyword
	static void saveElementImage(TestObject testObject, File file) {
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
	@Keyword
	static BufferedImage takeEntirePageImage(WebDriver webDriver, Integer timeout = 300) {
		Screenshot screenshot = new AShot().
				shootingStrategy(ShootingStrategies.viewportPasting(timeout)).
				takeScreenshot(webDriver)
		return screenshot.getImage()
	}


	/**
	 * provides the same function as takeEntirePageImage(WebDriver, Integer)
	 * 
	 * @timeout millisecond, wait for page to displayed stable after scrolling downward
	 * @return
	 */
	@Keyword
	static BufferedImage takeEntirePageImage(Integer timeout = 300) {
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
	@Keyword
	static void saveEntirePageImage(WebDriver webDriver, File file, Integer timeout = 300) {
		BufferedImage image = takeEntirePageImage(webDriver, timeout)
		ImageIO.write(image, "PNG", file)
	}

	/**
	 * provides the same function as saveEntirePageImage(WebDriver, File, Integer)
	 * @param file
	 */
	@Keyword
	static void saveEntirePageImage(File file, Integer timeout = 300) {
		WebDriver driver = DriverFactory.getWebDriver()
		saveEntirePageImage(driver, file, timeout)
	}



	/**
	 * similar to saveEntirePageImage(WebDriver, File, Integer)
	 * @deprecated use saveEntirePageImage(File, Integer) instead
	 * @param webDriver
	 * @param file
	 */
	@Keyword
	static void takeEntirePage(WebDriver webDriver, File file, Integer timeout = 300) {
		saveEntirePageImage(webDriver, file, timeout)
	}


	/**
	 * compare 2 images, calcuralte the magnitude of difference between the two
	 * 
	 * @param BufferedImage expectedImage
	 * @param BufferedImage actualImage
	 * @param Double criteriaPercentage, e.g. 90.0%
	 * @return ImageDifference object which represents how much different the input 2 images are
	 */
	@Keyword
	static ImageDifference verifyImages(BufferedImage expectedImage,
			BufferedImage actualImage, Double criteriaPercent) {
		ImageDifference difference =
				new ImageDifference(expectedImage, actualImage)
		difference.setCriteria(criteriaPercent)
		return difference
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

		ImageDifference(BufferedImage expected, BufferedImage actual) {
			expectedImage_ = expected
			actualImage_ = actual
			ImageDiff imgDiff = makeImageDiff(expectedImage_, actualImage_)
			ratio_ = calculateRatioPercent(imgDiff)
			diffImage_ = imgDiff.getMarkedImage()
		}

		private ImageDiff makeImageDiff(BufferedImage expected, BufferedImage actual) {
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

		private Double calculateRatioPercent(ImageDiff diff) {
			boolean hasDiff = diff.hasDiff()
			if (!hasDiff) {
				return 0.0
			}
			int diffSize = diff.getDiffSize()
			int area = diff.getMarkedImage().getWidth() * diff.getMarkedImage().getHeight()
			Double diffRatio = diff.getDiffSize() / area * 100
			return diffRatio
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
