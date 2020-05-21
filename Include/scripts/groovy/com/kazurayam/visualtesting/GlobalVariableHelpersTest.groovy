package com.kazurayam.visualtesting

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration

import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import internal.GlobalVariable

@RunWith(JUnit4.class)
public class GlobalVariableHelpersTest {

	private static Path json
	private static String FILENAME = "GlobalVariables.json"

	@BeforeClass
	static void setupClass() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path testOutputDir = projectDir.resolve("build/tmp/testOutput")
		Path pkgDir = testOutputDir.resolve("com.kazurayam.visualtesting")
		Path classDir = pkgDir.resolve(GlobalVariableHelpersTest.class.getSimpleName())
		if (!Files.exists(classDir)) {
			Files.createDirectories(classDir)
		}
		json = classDir.resolve(FILENAME)
	}

	@Test
	void test_isGlobalVariablePresent_negative() {
		assertFalse(GVH.isGlobalVariablePresent("THERE_IS_NO_SUCH_VARIABLE"))
	}
	
	@Test
	void test_addedGlobalVariableShouldImplementSetter() {
		GVH.addGlobalVariable("SETTABLE", "not yet modified")
		GlobalVariable.SETTABLE = "Hello, world"
		assertEquals("Hello, world", GlobalVariable.SETTABLE)
	}

	@Test
	void test_basic_operations() {
		String name = "foo"
		String value = "value"
		GVH.ensureGlobalVariable(name, value)
		assertTrue(GVH.isGlobalVariablePresent(name))
		Object obj = GVH.getGlobalVariableValue(name)
		assertNotNull(obj)
		assertTrue(obj instanceof String)
		assertEquals(value, (String)obj)
	}

	@Test
	void test_basic_operations_of_ManagedGlobalVariable() {
		ManagedGlobalVariable mgv = MGV.LAST_EXECUTED_TESTSUITE_ID
		String value = "CURA/chronos_capture"
		GVH.ensureGlobalVariable(mgv, value)
		assertTrue(GVH.isGlobalVariablePresent(mgv))
		Object obj = GVH.getGlobalVariableValue(mgv)
		assertNotNull(obj)
		assertTrue(obj instanceof String)
		assertEquals(value, (String)obj)
	}

	@Test
	void test_write_read() {
		// setup
		ManagedGlobalVariable mgv = MGV.LAST_EXECUTED_TESTSUITE_ID
		String value = "CURA/chronos_capture"
		GVH.ensureGlobalVariable(mgv, value)
		// when:
		Writer writer = new OutputStreamWriter(new FileOutputStream(json.toFile()),"utf-8")
		GVH.write([
			MGV.LAST_EXECUTED_TESTSUITE_ID.getName()
		], writer)
		// then
		assertTrue(json.toFile().length() > 0)

		// OK, next
		Reader reader = new InputStreamReader(new FileInputStream(json.toFile()),"utf-8")
		Map<String, Object> loaded = GVH.read([
			MGV.LAST_EXECUTED_TESTSUITE_ID.getName()
		], reader)
		assertTrue(loaded.containsKey(MGV.LAST_EXECUTED_TESTSUITE_ID.getName()))
		assertEquals(value, loaded.get(MGV.LAST_EXECUTED_TESTSUITE_ID.getName()))
		println "value read from file: name=${MGV.LAST_EXECUTED_TESTSUITE_ID.getName()}, value=${loaded.get(MGV.LAST_EXECUTED_TESTSUITE_ID.getName())}"
	}
}
