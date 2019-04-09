package com.kazurayam.visualtesting

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class GlobalVariableHelpers {

	private GlobalVariableHelpers() {}

	/**
	 * insert a public static property of type java.lang.Object
	 * into the internal.GlobalVarialbe runtime.
	 *
	 * e.g, addGlobalVariable('my_new_variable','foo') makes
	 * internal.GlobalVariable.getMy_new_variale() to return 'foo'
	 *
	 * @param name
	 * @param value
	 */
	static void addGlobalVariable(String name, Object value) {
		GroovyShell sh = new GroovyShell()
		MetaClass mc = sh.evaluate("internal.GlobalVariable").metaClass
		String getterName = 'get' + ((CharSequence)name).capitalize()
		mc.'static'."${getterName}" = {-> return value }
		mc.'static'."${name}"       = value
	}

	static void addGlobalVariable(ManagedGlobalVariable mgv, Object value) {
		addGlobalVariable(mgv.getName(), value)
	}

	/**
	 * @return true if GlobalVarialbe.name is defined, otherwise false
	 */
	static boolean isGlobalVariablePresent(String name) {
		boolean result = internal.GlobalVariable.metaClass.hasProperty( internal.GlobalVariable, name ) &&
				internal.GlobalVariable[name]
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") internal.GlobalVariable.metaClass.hassProperty(internal.GlobalVariable, name) is ${internal.GlobalVariable.metaClass.hasProperty( internal.GlobalVariable, name )}")
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") internal.GlobalVariable[name] is ${internal.GlobalVariable[name]}")
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") returns ${result}")
		return result
	}

	static boolean isGlobalVariablePresent(ManagedGlobalVariable mgv) {
		return isGlobalVariablePresent(mgv.getName())
	}

	static Object getGlobalVariableValue(String name) {
		if (isGlobalVariablePresent(name)) {
			return GlobalVariable[name]
		} else {
			return null
		}
	}

	static Object getGlobalVariableValue(ManagedGlobalVariable mgv) {
		if (isGlobalVariablePresent(mgv.getName())) {
			return GlobalVariable[mgv.getName()]
		} else {
			return null
		}
	}

	/**
	 * If GlobaleVariable.name is present, set the value into it.
	 * Otherwise create GlobalVariable.name dynamically and set the value into it.
	 * 
	 * @param name
	 * @param value
	 */
	static void ensureGlobalVariable(String name, Object value) {
		if (isGlobalVariablePresent(name)) {
			//GlobalVariable[name] = value
			addGlobalVariable(name, value)
		} else {
			addGlobalVariable(name, value)
		}
	}

	static void ensureGlobalVariable(ManagedGlobalVariable mgv, Object value) {
		ensureGlobalVariable(mgv.getName(), value)
	}
}
