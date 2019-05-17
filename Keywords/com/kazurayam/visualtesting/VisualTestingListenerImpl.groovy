package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialRepositoryFactory
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.MaterialStorageFactory
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable as GlobalVariable

public class VisualTestingListenerImpl {

	/**
	 * Ｙｏｕ　ｃａｎ　ｌｏｃａｌｔｅ　Ｍａｔｅｒｉａｌｓ　ａｎｄ　Ｓｔｏｒａｇｅ　ｄｉｒｅｃｔｏｒｙ　ｏｎ　a nｅｔwork drive.
	 * 
	 * GlobalVariable.AUXILIARY_VT_PROJECT_DIR = 'G:/マイドライブ/VisualTestingInKatalonStudio'
	 */
	public static final String GVNAME_AUX = 'AUXILIARY_VT_PROJECT_DIR'

	private Path reportDir
	private Path materialsDir
	private Path storageDir

	/**
	 * resolve reportDir, materialDir, storageDir. For example,
	 * 
	 *     reportDir    -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Reports/TS1/20180618_165141
	 *     materialsDir -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Materials
	 *     storageDir   -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Storage
	 *
	 *     If you set GlobalVariable.AUXILIARY_VT_PROJECT_DIR = 'G:/マイドライブ/VisualTestingInKatalonStudio', then you will have
	 *     reportDir    -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Reports/TS1/20180618_165141
	 *     materialsDir -> G:/マイドライブ/VisualTestingInKatalonStudio/Materials
	 *     storageDir   -> G:/マイドライブ/VisualTestingInKatalonStudio/Storage
	 *
	 * By the way, when you open your Katalon project with GUI, then the Reports directory is located at "<project dir>/Reports", and 
	 * you can not change it. But when you run Katalon Studio in Console mode you can specify the Reports directory by command line option
	 * '-reportFolder=<path>'
	 */
	VisualTestingListenerImpl() {
		reportDir    = Paths.get(RunConfiguration.getReportFolder())
		materialsDir = Paths.get(VisualTestingListenerImpl.resolveProjectDir()).resolve('Materials')
		storageDir   = Paths.get(VisualTestingListenerImpl.resolveProjectDir()).resolve('Storage')
	}

	/**
	 * This method resturn a string as the Path of "alternative project directory" where the Materials directory and the Storage directory
	 * are found. The default is equal to the usual project directory.
	 * You can specify "alternative project directory" by defining a GlobalVariable.ALTERNATIVE_PROJECT_DIR in the Execution Profile.
	 * For example, you can specify
	 *     <PRE>GlobalVarialbe.ALTERNATIVE_PROJECT_DIR == "G:\マイドライブ\VisualTestingWorkspace\CorporateVT"</PRE>
	 *     
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is defined, the dir exists and is writable, returns that path.
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is defined but does not exist, log warning message, returns the value of RunConfiguration.getProjectDir() call.
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is not defined, returns the value of RunConfiguration.getProjectDir() call.
	 * 
	 * @return a Path string as the project directory possible on a network drive. Windows Network Drive, Google Drive Stream or UNIX NFS.
	 */
	static String resolveProjectDir() {
		String v = VisualTestingListenerImpl.GVNAME_AUX
		if ( GlobalVariableHelpers.isGlobalVariablePresent(v) ) {
			String s = (String)GlobalVariableHelpers.getGlobalVariableValue(v)
			Path dir = Paths.get(s)
			if (!Files.exists(dir)) {
				KeywordUtil.logInfo("GlobalVariable.${v}=${dir.toString()}: the directory is not found. Alternatively ${RunConfiguration.getProjectDir()} is used.")
				return RunConfiguration.getProjectDir()
			} else {
				return dir.toString()
			}
		} else {
			KeywordUtil.logInfo("GlobalVariable.${v} is not defined. Alternatively ${RunConfiguration.getProjectDir()} is used.")
			return RunConfiguration.getProjectDir()
		}
	}

	/**
	 * 
	 * @param testSuiteContext
	 */
	void beforeTestSuite(TestSuiteContext testSuiteContext) {
		Objects.requireNonNull(testSuiteContext, "testSuiteContext must not be null")
		String testSuiteId        = testSuiteContext.getTestSuiteId()     // e.g. 'Test Suites/TS1'
		String testSuiteTimestamp = reportDir.getFileName().toString()    // e.g. '20180618_165141'
		//
		GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_ID, testSuiteId)
		GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_TIMESTAMP, testSuiteTimestamp)

		// create the MaterialRepository object
		Files.createDirectories(materialsDir)
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsDir)
		mr.putCurrentTestSuite(testSuiteId, testSuiteTimestamp)
		GVH.ensureGlobalVariable(MGV.MATERIAL_REPOSITORY, mr)

		// create the MaterialStorage object
		Files.createDirectories(storageDir)
		MaterialStorage ms = MaterialStorageFactory.createInstance(storageDir)
		GVH.ensureGlobalVariable(MGV.MATERIAL_STORAGE, ms)
	}

	void beforeTestCase(TestCaseContext testCaseContext) {
		Objects.requireNonNull(testCaseContext, "testCaseContext must not be null")

		if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_TESTSUITE_ID) ) {
			GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_ID, TSuiteName.SUITELESS_DIRNAME)
		}
		if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_TESTSUITE_TIMESTAMP) ) {
			GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_TIMESTAMP, TSuiteTimestamp.TIMELESS_DIRNAME)
		}

		GVH.ensureGlobalVariable(ManagedGlobalVariable.CURRENT_TESTCASE_ID, testCaseContext.getTestCaseId())

		def hd = 'VisualTestingListenerImpl#beforeTestCase'
		WebUI.comment("${hd} ${MGV.CURRENT_TESTSUITE_ID} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTSUITE_ID)}\"")
		WebUI.comment("${hd} ${MGV.CURRENT_TESTSUITE_TIMESTAMP} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTSUITE_TIMESTAMP)}\"")
		WebUI.comment("${hd} ${MGV.CURRENT_TESTCASE_ID} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTCASE_ID)}\"")

		//
		if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_REPOSITORY) ) {
			Files.createDirectories(materialsDir)
			MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsDir)
			mr.putCurrentTestSuite(TSuiteName.SUITELESS_DIRNAME, TSuiteTimestamp.TIMELESS_DIRNAME)
			GVH.ensureGlobalVariable(MGV.MATERIAL_REPOSITORY, mr)
		}
		WebUI.comment("${hd} ${MGV.MATERIAL_REPOSITORY} is \"${GVH.getGlobalVariableValue(MGV.MATERIAL_REPOSITORY).toString()}\"")

		if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_STORAGE) ) {
			Files.createDirectories(storageDir)
			MaterialStorage ms = MaterialStorageFactory.createInstance(storageDir)
			GVH.ensureGlobalVariable(MGV.MATERIAL_STORAGE, ms)
		}
		WebUI.comment("${hd} ${MGV.MATERIAL_STORAGE} is \"${GVH.getGlobalVariableValue(MGV.MATERIAL_STORAGE).toString()}\"")

	}

	void afterTestCase(TestCaseContext testCaseContext) {
		// nothing to do
	}

	void afterTestSuite(TestSuiteContext testSuiteContext) {
		// nothing to do
	}
}
