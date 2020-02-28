package com.kazurayam.visualtesting

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration
import groovy.json.JsonOutput

@RunWith(JUnit4.class)
class CapturedURLsTest {

	private CapturedURLs instance_
	
	@Before
	void setup() {
		instance_ = new CapturedURLs()
		instance_.add("Section A", "entry A1", new URL("http://demo-aut.katalon.com"), Paths.get("./A1.png"))
		instance_.add("Section B", "entry B1", new URL("http://demo-aut.katalon.com"), Paths.get("./B1.png"))
		instance_.add("Section A", "entry A2", new URL("http://demo-aut.katalon.com"), Paths.get("./A2.png"))
		instance_.add("Section B", "entry B2", new URL("http://demo-aut.katalon.com"), Paths.get("./B2.png"))
	}

	@Test
	void test_toJsonText() {
		// setup:
		// when:
		String json = JsonOutput.prettyPrint(instance_.toJsonText())
		println json
		// then:
		assertTrue(json.contains("Section A"))
	}
	
	@Test
	void test_serializeMarkdown() {
		// when:
		StringWriter sw = new StringWriter()
		instance_.serializeMarkdown(sw)
		String md = sw.toString()
		println md
		//
		assertTrue(md.contains("### Section A"))
	}
	
	@Test
	void test_serializeText() {
		// when:
		StringWriter sw = new StringWriter()
		instance_.serializeText(sw)
		String md = sw.toString()
		println md
		//
		assertTrue(md.contains("Section A"))
	}
}
