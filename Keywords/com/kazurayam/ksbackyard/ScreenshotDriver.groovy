package com.kazurayam.ksbackyard

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
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
	 * take the screenshot of the specified WebElement in the target Web page, 
	 * and save it into the output file in PNG format.
	 * 
	 * @param webDriver
	 * @param webElement
	 * @param output
	 */
	@Keyword
	static void saveElementImage(WebDriver webDriver, WebElement webElement, File file) {
		BufferedImage image = takeElementImage(webDriver, webElement)
		ImageIO.write(image, "PNG", file)
	}



	/**
	 * takes screenshot of the entire page targeted,
	 * returns it as a BufferedImage object
	 *
	 * @param webDriver
	 * @param webElement
	 * @param timeout millisecond, wait for page to be displayed after scrolling downward to view next viewport
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
	 * @deprecated use saveEntirePageImage(WebDriver, File, Integer) instead
	 * @param webDriver
	 * @param file
	 */
	@Keyword
	static void takeEntirePage(WebDriver webDriver, File file, Integer timeout = 300) {
		saveEntirePageImage(webDriver, file, timeout)
	}




	/**
	 * 
	 * @param BufferedImage expectedImage
	 * @param BufferedImage actualImage
	 * @param Double criteriaPercentage, e.g. 90.0%
	 * @return
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
	 * return a Map object containing 'evaluated': true when the video is autoplayed,
	 * otherwise false.
	 * 
	 * 1. when the video is loaded, push the playButton so that the vido is stopped
	 * 2. 1st screenshot is taken.
	 * 3. push the playButton so that the video is restarted, wait for some seconds so that the video goes forward
	 * 4. 2nd screenshot is taken.
	 * 5. compare the screenshots. Return true if they are different enough.
	 * 
	 * @param driver WebDriver
	 * @param video  <video> WebElement
	 * @param playButton <button> WebElement to start/stop the video
	 * @param gapTimeSecs 1st screenshot --> gapTimeSecs --> 2nd screenshot  
	 * @return an ImageDifference object
	 */
	@Keyword
	static ImageDifference verifyVideoInMotion(
			WebDriver driver,
			WebElement video,
			WebElement playButton,
			Integer gapTimeSecs,
			Double criteriaPercent) {


		// click the start/stop button
		WebUI.executeJavaScript("arguments[0].click()", Arrays.asList(playButton))

		// take the 1st screen shot
		BufferedImage image1 = ScreenshotDriver.takeElementImage(driver, video)

		// again click the start/stop button and wait
		if (gapTimeSecs > 0) {
			WebUI.executeJavaScript("arguments[0].click()", Arrays.asList(playButton))
			WebUI.delay(gapTimeSecs)
		}

		// take the 2nd screenshot
		BufferedImage image2 = ScreenshotDriver.takeElementImage(driver, video)

		// how differenct these are?
		ImageDifference difference = new ImageDifference(image1, image2)

		difference.setCriteria(criteriaPercent)

		// return true if the movie autoplay in motion, otherwise false
		return difference
	}


	/**
	 * return a Map object containing 'evaluated': true when the video is staying still = is not autoplayed,
	 * otherwise false.
	 * 
	 * 1. when the video is loaded, 1st screenshot is taken.
	 * 2. wait for some seconds
	 * 3. 2nd screenshot is taken.
	 * 4. compare the screenshots. Return true if they have no or just a little difference.
	 * 
	 * @return an ImageDifference object
	 */
	@Keyword
	static ImageDifference verifyVideoStartsStill(
			WebDriver driver,
			WebElement video,
			WebElement playButton = null /* not used */,
			Integer gapTimeSecs = 5,
			Double criteriaPercent = 10.0) {

		// take the 1st screen shot
		BufferedImage image1 = ScreenshotDriver.takeElementImage(driver, video)

		// wait for some seconds
		if (gapTimeSecs > 0) {
			WebUI.delay(gapTimeSecs)
		}

		// take the 2nd screenshot
		BufferedImage image2 = ScreenshotDriver.takeElementImage(driver, video)

		// make KSImageDiff
		ImageDifference difference = new ImageDifference(image1, image2)

		difference.setCriteria(criteriaPercent)

		// return true if the movie start still, otherwise false
		return difference
	}


	/**
	 * wraps aShot's ImageDiff object, plus a few getter methods
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
