package com.kazurayam.visualtesting
import java.nio.file.Path

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

class Helpers {

	/**
	 * recursively look for parent directory of the name specified.
	 * if not found returns null.
	 * 
	 * @param p Path of "/Users/kazurayam/Reports/VT/clearMaterials/20190524_184507"
	 * @param ancestorName 'Reports'
	 * @return Path of "/Users/kazurayam/Reports"
	 */
	Path lookupAncestorOrSelfPathOfName(Path p, String name) {
		Objects.requireNonNull(p, "p must not be null")
		Objects.requireNonNull(name, "name must not be null")
		if (name.length() == 0) {
			throw new IllegalArgumentException("name is an empty string")
		}
		if (p.getFileName().toString().equals(name)) {
			return p
		} else {
			Path parent = p.getParent()
			if (parent != null) {
				return lookupAncestorOrSelfPathOfName(parent, name)
			} else {
				return null
			}
		}
	}



	/**
	 * Refresh browser
	 */
	@Keyword
	def refreshBrowser() {
		KeywordUtil.logInfo("Refreshing")
		WebDriver webDriver = DriverFactory.getWebDriver()
		webDriver.navigate().refresh()
		KeywordUtil.markPassed("Refresh successfully")
	}

	/**
	 * Click element
	 * @param to Katalon test object
	 */
	@Keyword
	def clickElement(TestObject to) {
		try {
			WebElement element = WebUiBuiltInKeywords.findWebElement(to);
			KeywordUtil.logInfo("Clicking element")
			element.click()
			KeywordUtil.markPassed("Element has been clicked")
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found")
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element")
		}
	}

	/**
	 * Get all rows of HTML table
	 * @param table Katalon test object represent for HTML table
	 * @param outerTagName outer tag name of TR tag, usually is TBODY
	 * @return All rows inside HTML table
	 */
	@Keyword
	def List<WebElement> getHtmlTableRows(TestObject table, String outerTagName) {
		WebElement mailList = WebUiBuiltInKeywords.findWebElement(table)
		List<WebElement> selectedRows = mailList.findElements(By.xpath("./" + outerTagName + "/tr"))
		return selectedRows
	}
}