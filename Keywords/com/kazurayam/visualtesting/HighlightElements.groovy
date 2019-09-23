package com.kazurayam.visualtesting

import static com.kms.katalon.core.model.FailureHandling.CONTINUE_ON_FAILURE

import java.nio.file.Path

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

import groovy.json.JsonOutput

public class HighlightElements {

	Map<String, DOMRect> highlightedElements

	String script = '''var rect = arguments[0].getBoundingClientRect(); 
return rect.x + "," + rect.y + "," + rect.width + "," + rect.height;
'''

	HighlightElements() {
		highlightedElements = new HashMap<String, DOMRect>()
	}

	void highlightElement(TestObject testObject, FailureHandling flowControl = CONTINUE_ON_FAILURE) {
		try {
			WebDriver driver = DriverFactory.getWebDriver()
			List<WebElement> elements = WebUiCommonHelper.findWebElements(testObject, 10)
			for (int i = 0; i < elements.size(); i++) {
				WebElement element = elements.get(i)
				JavascriptExecutor js = (JavascriptExecutor)driver
				js.executeScript(
						"arguments[0].setAttribute('style','outline: dashed red;');",
						element)
				// log
				String xywh = js.executeScript(script, element)
				println "xywh is ${xywh}"
				String[] values = xywh.split(',')
				Double x = values[0] as Double
				Double y = values[1] as Double
				Double width = values[2] as Double
				Double height = values[3] as Double
				DOMRect domRect = new DOMRect((int)x, (int)y, (int)width, (int)height)
				String id = (i == 0) ? testObject.getObjectId() : testObject.getObjectId() + '.' + i
				highlightedElements.put(id, domRect)
			}
		} catch (Exception e) {
			stepFailed(e.getMessage(), flowControl)
		}
	}

	void writeLog(Path output) {
		StringBuilder sb = new StringBuilder()
		sb.append('[')
		int count = 0
		highlightedElements.each { key, val ->
			if (count > 0) {
				sb.append(',')
			}
			sb.append('{')
			sb.append('\"objectId\":\"')
			sb.append(key)
			sb.append('\",')
			sb.append('\"DOMRect\":')
			sb.append(val)
			sb.append('}')
			count++
		}
		sb.append(']')
		output.toFile().write(JsonOutput.prettyPrint(sb.toString()), "utf-8")
	}

	static createTestObjectWithXPath(String objectId, String xpath) {
		TestObject tobj = new TestObject(objectId)
		tobj.addProperty('xpath', ConditionType.EQUALS, xpath)
		return tobj
	}

	static createTestObjectWithCssSelector(String objectId, String selector) {
		TestObject tobj = new TestObject(objectId)
		tobj.addProperty('css', ConditionType.EQUALS, selector)
		return tobj
	}

	static def stepFailed(String message, FailureHandling flowControl) {
		if (flowControl == FailureHandling.OPTIONAL) {
			//println "#stepFailed('${message}',FailureHandling.OPTIONAL)"
			KeywordUtil.logInfo(message)
		} else if (flowControl == FailureHandling.CONTINUE_ON_FAILURE) {
			//println "#stepFailed('${message}',FailureHandling.CONTINUE_ON_FAILURE)"
			KeywordUtil.markFailed(message)
		} else {
			// in the case where flowControl == FailureHandling.STOP_ON_FAILURE
			//println "#stepFailed('${message}',FailureHandling.STOP_ON_FAILURE)"
			KeywordUtil.markFailedAndStop(message)
		}
	}
}
