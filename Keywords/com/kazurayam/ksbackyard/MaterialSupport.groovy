package com.kazurayam.ksbackyard

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.util.stream.Collectors

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory


class MaterialSupport {
	
	@Keyword
	def openReticentBrowser() {
		FirefoxProfile profile = new FirefoxProfile()
		profile.setPreference('browser.download.useDownloadDir', true)
		profile.setPreference('browser.download.folderList', 1)         // use user.home/Downlods/
		profile.setPreference('browser.helperApps.neverAsk.saveToDisk',
			'application/pdf,application/vnd.ms-excel')
		profile.setPreference('pdfjs.disabled', true)
		FirefoxOptions options = new FirefoxOptions().setProfile(profile)
		FirefoxDriver driver = new FirefoxDriver(options)
		DriverFactory.changeWebDriver(driver)
	}

	@Keyword
	def importDownloadedFileAsMaterial(Path materialPath) {
		String browser = DriverFactory.getExecutedBrowser()
		if (browser == 'Firefox') {
			Path downloadDir = Paths.get(System.getProperty('user.home') + '/Downloads')
			Path latestModifiedFile = findLatestModifiedFile(downloadDir)
			if (latestModifiedFile != null) {
				Files.copy(latestModifiedFile, materialPath)
			}
		} else {
			KeywordUtil.markErrorAndStop("MaterialSupport#importDownloadedFileAsMaterial: browser=${browser} is not yet supported")
		}
	}

	private Path findLatestModifiedFile(Path dir) {
		List<Path> files = Files.list(dir).collect(Collectors.toList())
		Path pathLatest = null
		FileTime timeLatest = FileTime.fromMillis(0L)
		for (Path file : files) {
			FileTime fileTime = Files.getLastModifiedTime(file)
			if (fileTime > timeLatest) {
				pathLatest = file
				timeLatest = fileTime
			}
		}
		return pathLatest
	}

}