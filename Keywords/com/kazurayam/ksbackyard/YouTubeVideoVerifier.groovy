package com.kazurayam.ksbackyard

import java.awt.image.BufferedImage

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kazurayam.ksbackyard.ScreenshotDriver.ImageDifference
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * This class implements Custom Keywords operational in Katalon Studio.
 * The keywords are useful to verify how a YouTube video is working. 
 *
 * @author kazurayam
 *
 */
class YouTubeVideoVerifier {

	/**
	 * This method accepts a WebDriver instance and a WebElement object as video.
	 * This method checks if the video is autoplayed or not.
	 * 
	 * What it actually does is as follows:
	 * 1. when the video is loaded, push the playButton so that the video will stopped
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

		// how much different these are?
		ImageDifference difference = new ImageDifference(image1, image2)

		difference.setCriteria(criteriaPercent)

		// return true if the movie autoplay in motion, otherwise false
		return difference
	}


	/**
	 * provides the same function as verifyVideoInMotion(WebDriver, WebElement, WebElement, Integer, Double)
	 * @param video
	 * @param playButton
	 * @param gapTimeSecs
	 * @param criteriaPercent
	 * @return
	 */
	@Keyword
	static ImageDifference verifyVideoInMotion(
			TestObject video,
			TestObject playButton,
			Integer gapTimeSecs,
			Double criteriaPercent) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement videoWebElement = WebUI.findWebElement(video)
		WebElement playButtonWebElement = WebUI.findWebElement(playButton)
		return this.verifyVideoInMotion(driver, videoWebElement, playButtonWebElement, gapTimeSecs, criteriaPercent)
	}





	/**
	 * This method accepts a WebDriver instance and a WebElement object as video.
	 * This method checks if the video starts and stay still (no motion)or not.
	 * 
	 * What it actually does is as follows:
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

	@Keyword
	static ImageDifference verifyVideoStartsStill(
			TestObject video,
			Integer gapTimeSecs = 5,
			Double criteriaPercent = 10.0) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement videoElement = WebUI.findWebElement(video, 30)
		return this.verifyVideoStartsStill()
	}

}
