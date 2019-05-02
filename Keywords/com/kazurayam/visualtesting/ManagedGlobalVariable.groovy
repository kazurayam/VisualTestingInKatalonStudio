package com.kazurayam.visualtesting

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public enum ManagedGlobalVariable {

	CURRENT_TESTSUITE_ID        ('VT_CURRENT_TESTSUITE_ID'),
	CURRENT_TESTSUITE_TIMESTAMP ('VT_CURRENT_TESTSUITE_TIMESTAMP'),
	CURRENT_TESTCASE_ID         ('VT_CURRENT_TESTCASE_ID'),
	MATERIAL_REPOSITORY         ('VT_MATERIAL_REPOSITORY'),
	MATERIAL_STORAGE            ('VT_MATERIAL_STORAGE')
	;
	private String name_
	public ManagedGlobalVariable(String name) {
		this.name_ = name
	}
	public String getName() {
		return name_
	}
}
