package com.kazurayam.ksbackyard

import org.openqa.selenium.By
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import groovy.json.JsonOutput

/**
 * Utility methods for Kataon's TestObject
 * - converting TestObject to JSON
 * - converting TestObject to Selenium By object
 *
 * @author kazurayam
 */
class TestObjectSupport {

	/**
	 * convert a TestObject object into a String in JSON format.
	 * will list active properites only.
	 *
	 * @param testObject
	 * @return e.g.
	 * <pre>
	 * {
	 *     "objectId": "Page_CURA Healthcare Service/a_Make Appointment",
	 *     "selectorMethod": "BASIC",
	 *     "selectorCollection": {
	 *         "BASIC": "//a[@id='btn']"
	 *     }
	 * }
	 * </pre>
	 */
	@Keyword
	static String toJson(TestObject testObject) {
		Objects.requireNonNull(testObject, "testObject must not be null")
		String json = JsonOutput.toJson(testObject)
		return json
	}

	static String prettyPrint(TestObject testObject) {
		Objects.requireNonNull(testObject, "testObject must not be null")
		String json = JsonOutput.toJson(testObject)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}

	/**
	 * convert a Katalon's TestObject into a Selenium'S By object
	 *
	 * @param testObject
	 * @return
	 */
	@Keyword
	static By toBy(TestObject testObject) {
		Objects.requireNonNull(testObject, "testObject must not be null")
		switch (testObject.selectorMethod) {
			case 'BASIC' :
				return By.xpath(testObject.getSelectorCollection()[SelectorMethod.BASIC])
				break
			case 'CSS' :
				return By.cssSelector(testObject.getSelectorCollection()[SelectorMethod.CSS])
				break
			case 'XPATH' :
				return By.xpath(testObject.getSelectorCollection()[SelectorMethod.XPATH])
				break
		}
		throw new IllegalArgumentException("unable to convert to By: " + prettyPrint(testObject))
	}

	@Keyword
	static List<By> toBy(List<TestObject> testObjectList) {
		List<By> list = new ArrayList<By>()
		for (TestObject to : testObjectList) {
			list.add(TestObjectSupport.toBy(to))
		}
		return list
	}
}