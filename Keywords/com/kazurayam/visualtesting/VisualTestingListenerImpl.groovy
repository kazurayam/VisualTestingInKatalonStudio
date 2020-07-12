package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialRepositoryFactory
import com.kazurayam.materials.MaterialStorage
import com.kazurayam.materials.MaterialStorageFactory
import com.kazurayam.materials.ReportsAccessor
import com.kazurayam.materials.ReportsAccessorFactory
import com.kazurayam.materials.TExecutionProfile
import com.kazurayam.materials.TSuiteName
import com.kazurayam.materials.TSuiteTimestamp
import com.kazurayam.materials.VisualTestingLogger
import com.kazurayam.materials.impl.VisualTestingLoggerDefaultImpl
import com.kazurayam.materials.metadata.MaterialMetadataBundle
import com.kazurayam.visualtesting.GlobalVariableHelpers as GVH
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class VisualTestingListenerImpl {

	private Path materialsDir
	private Path storageDir
	private Path reportsDir
	private Path reportFolder

	public static final Path GLOBAL_VARIABLES_JSON
	static {
		GLOBAL_VARIABLES_JSON = Paths.get(RunConfiguration.getProjectDir()).resolve("build/tmp/GlobalVariables.json")
	}

	/**
	 * resolve reportDir, materialDir, storageDir. For example,
	 * 
	 *     reportDir    -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Reports
	 *     materialsDir -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Materials
	 *     storageDir   -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Storage
	 *
	 *     If you set GlobalVariable.AUXILIARY_VT_PROJECTS_DIR = 'G:/マイドライブ', then you will have
	 *     reportDir    -> C:/Users/username/katalon-workspace/VisualTestingInKatalonStudio/Reports
	 *     materialsDir -> G:/マイドライブ/VisualTestingInKatalonStudio/Materials
	 *     storageDir   -> G:/マイドライブ/VisualTestingInKatalonStudio/Storage
	 *
	 * By the way, when you open your Katalon project with GUI, then the Reports directory is located at "<project dir>/Reports", and 
	 * you can not change it. But when you run Katalon Studio in Console mode you can specify the Reports directory by command line option
	 * '-reportFolder=<path>'
	 */
	VisualTestingListenerImpl() {
		String hd = '#VisualTestingListenerImpl()'

		KeywordUtil.logInfo("${hd} Execution Profile \'${RunConfiguration.getExecutionProfile()}\' is applied")

		// the Materials dir and the Storage dir are located usually under the <projectDir>,
		// but you can change the location by specifying the AUXILIARY_VT_PROJECT_DIR property in the <projectDir>/vt-config.json file
		materialsDir = Paths.get(this.resolveProjectDir()).resolve('Materials')
		storageDir   = Paths.get(this.resolveProjectDir()).resolve('Storage')
		KeywordUtil.logInfo("${hd} materialsDir=${materialsDir}")
		KeywordUtil.logInfo("${hd} storageDir=${storageDir}")

		// the location of the Reports directory is defined by Katalon Studio,
		// usually it is located under the <projectDir>,
		// and when you invoke KS by Console Mode you have an option of changing the location by -reportFolder=<Path> option
		reportFolder = Paths.get(RunConfiguration.getReportFolder())
		KeywordUtil.logInfo("${hd} reportFolder=${reportFolder}")

		// when you run "Test Cases/VT/makeIndex" directly (you do not run "Test Suites/VT/makeIndex"),
		// Katalon Studio's RunConfiguration.getReportFolder() will return a path of temporary directory such as
		// "C:\Users\qcq0264\AppData\Local\Temp\Katalon\Test Cases\VT\makeIndex\20190529_124909"
		// In this case, the following line results null
		reportsDir   = new Helpers().lookupAncestorOrSelfPathOfName(reportFolder, 'Reports')
		if (reportsDir == null) {
			// what else we can do? let's assume <projectDir>/Reports directory is there
			reportsDir = Paths.get(this.resolveProjectDir()).resolve('Reports')
			Files.createDirectories(reportsDir)
		}
		KeywordUtil.logInfo("${hd} reportsDir=${reportsDir}")
	}

	/**
	 * This method return a string as the Path of "alternative project directory" 
	 * where the Materials directory and the Storage directory
	 * are found. The default is equal to the usual project directory.
	 * You can specify "alternative project directory" by defining 
	 * a GlobalVariable.ALTERNATIVE_PROJECT_DIR in the Execution Profile.
	 * For example, you can specify
	 *     <PRE>GlobalVarialbe.ALTERNATIVE_PROJECT_DIR == "G:\マイドライブ\vtprojects\VisualTestingWorkspace\CorporateVT"</PRE>
	 *     
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is defined, the dir exists and is writable, returns that path.
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is defined but does not exist, log warning message, 
	 *     returns the value of RunConfiguration.getProjectDir() call.
	 * If GlobalVariable.ALTERNATIVE_PROJECT_DIR is not defined, returns the value of RunConfiguration.getProjectDir() call.
	 * 
	 * @return a Path string as the project directory possible on a network drive. Windows Network Drive, Google Drive Stream or UNIX NFS.
	 */
	String resolveProjectDir() {
		String hd = this.class.getSimpleName() + '#resolveProjectDir()'
		VTConfig vtConfig = new VTConfig()
		String projectsDir = vtConfig.getAuxiliaryVTProjectDir()
		if ( projectsDir != null ) {
			Path dir = Paths.get(projectsDir, getProjectName())
			if (!Files.exists(dir)) {
				KeywordUtil.logInfo("${hd} {path} does not exist. Materials and Storage dir will be located in ${RunConfiguration.getProjectDir()}")
				return RunConfiguration.getProjectDir()
			} else {
				return dir.toString()
			}
		} else {
			KeywordUtil.logInfo("${hd} ${VTConfig.PROPERTY_AUX_DIR} is not defined in vt-config.json. Materials and Storage dir will be located in ${RunConfiguration.getProjectDir()}")
			return RunConfiguration.getProjectDir()
		}
	}

	/**
	 * @return a String as the name of Project. When the project dir is "C:/Users/me/my/projectX", then returns "projectX"
	 */
	static String getProjectName() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		return projectDir.getFileName().toString()
	}

	/**
	 * 
	 * @param testSuiteContext
	 */
	void beforeTestSuite(TestSuiteContext testSuiteContext) {
		Objects.requireNonNull(testSuiteContext, "testSuiteContext must not be null")

		String hd = 'VisualTestingListenerImpl#beforeTestSuite'

		String testSuiteId          = testSuiteContext.getTestSuiteId()        // e.g. 'Test Suites/TS1'
		String executionProfile     = RunConfiguration.getExecutionProfile()   // e.g, 'CURA_DevelopmentEnv'
		String testSuiteTimestamp   = reportFolder.getFileName().toString()    // e.g. '20200621_061234'

		GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_ID, testSuiteId)
		GVH.ensureGlobalVariable(MGV.CURRENT_EXECUTION_PROFILE, executionProfile)
		GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_TIMESTAMP, testSuiteTimestamp)

		KeywordUtil.logInfo("${hd} testSuiteId=${testSuiteId}")
		KeywordUtil.logInfo("${hd} executionProfile=${executionProfile}")
		KeywordUtil.logInfo("${hd} testSuiteTimestamp=${testSuiteTimestamp}")

		// create the MaterialRepository object
		Files.createDirectories(materialsDir)

		// create the MaterialRepository object, save it as a GlobalVariable
		KeywordUtil.logInfo("${hd} creating MaterialRepository")
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsDir)
		mr.markAsCurrent(testSuiteId, executionProfile, testSuiteTimestamp)
		def tsr = mr.ensureTSuiteResultPresent(testSuiteId, executionProfile, testSuiteTimestamp)
		//if (tsr == null) {
		//	throw new IllegalStateException("mr.ensureTSuiteResultPresent(${testSuiteId},${executionProfile},${testSuiteTimestamp}) returned null")
		//}
		VisualTestingLogger vtLogger4Repos = new VisualTestingLoggerDefaultImpl()
		mr.setVisualTestingLogger(vtLogger4Repos)
		GVH.ensureGlobalVariable(MGV.MATERIAL_REPOSITORY, mr)
		KeywordUtil.logInfo("${hd} created MaterialRepository")


		// create the MaterialStorage object, save it as a GlobalVariable
		KeywordUtil.logInfo("${hd} creating MaterialStorage")
		Files.createDirectories(storageDir)
		MaterialStorage ms = MaterialStorageFactory.createInstance(storageDir)
		VisualTestingLogger vtLogger4Storage = new VisualTestingLoggerDefaultImpl()
		ms.setVisualTestingLogger(vtLogger4Storage)
		GVH.ensureGlobalVariable(MGV.MATERIAL_STORAGE, ms)
		KeywordUtil.logInfo("${hd} created MaterialStorage")


		// create the ReportsAccessor object, save it as a GlobalVariable
		KeywordUtil.logInfo("${hd} creating ReportsAccessor")
		ReportsAccessor ra = ReportsAccessorFactory.createInstance(reportsDir)
		GVH.ensureGlobalVariable(MGV.REPORTS_ACCESSOR, ra)
		KeywordUtil.logInfo("${hd} created ReportsAccessor")


		// read TestSuiteId and ExecutionProfile of the last executed test suite
		// from GlobalVariables.json file
		if (Files.exists(GLOBAL_VARIABLES_JSON)) {
			List<String> names = [
				MGV.LAST_EXECUTED_TESTSUITE_ID.getName(),
				MGV.LAST_APPLIED_EXECUTION_PROFILE.getName()
			]
			Reader reader = new InputStreamReader(new FileInputStream(GLOBAL_VARIABLES_JSON.toFile()), "utf-8")
			Map<String, Object> gvs = GlobalVariableHelpers.read(names, reader)
			for (String name in gvs.keySet()) {
				GlobalVariableHelpers.ensureGlobalVariable(name, gvs.get(name))
				WebUI.comment("GlobalVariable.${name} has value \"${gvs.get(name)}\" loaded from ${GLOBAL_VARIABLES_JSON}")
			}
		} else {
			WebUI.comment("${GLOBAL_VARIABLE_JSON} file is not found")
		}
	}




	/**
	 * 
	 * @param testCaseContext
	 */
	void beforeTestCase(TestCaseContext testCaseContext) {
		Objects.requireNonNull(testCaseContext, "testCaseContext must not be null")

		String hd = 'VisualTestingListenerImpl#beforeTestCase'

		if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_TESTSUITE_ID) ) {
			GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_ID, TSuiteName.SUITELESS_DIRNAME)  // '_' as TSuiteName directory
		}
		if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_EXECUTION_PROFILE) ) {
			GVH.ensureGlobalVariable(MGV.CURRENT_EXECUTION_PROFILE, TExecutionProfile.UNUSED) // '_' as Execution Profile directory
		}
		if ( ! GVH.isGlobalVariablePresent(MGV.CURRENT_TESTSUITE_TIMESTAMP) ) {
			GVH.ensureGlobalVariable(MGV.CURRENT_TESTSUITE_TIMESTAMP, TSuiteTimestamp.TIMELESS_DIRNAME)  // '_' as TSuteTimestamp directory
		}
		GVH.ensureGlobalVariable(ManagedGlobalVariable.CURRENT_TESTCASE_ID, testCaseContext.getTestCaseId())

		WebUI.comment("${hd} GlobalVariable.${MGV.CURRENT_TESTSUITE_ID} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTSUITE_ID)}\"")
		WebUI.comment("${hd} GlobalVariable.${MGV.CURRENT_EXECUTION_PROFILE} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_EXECUTION_PROFILE)}\"")
		WebUI.comment("${hd} GlobalVariable.${MGV.CURRENT_TESTSUITE_TIMESTAMP} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTSUITE_TIMESTAMP)}\"")
		WebUI.comment("${hd} GlobalVariable.${MGV.CURRENT_TESTCASE_ID} is \"${GVH.getGlobalVariableValue(MGV.CURRENT_TESTCASE_ID)}\"")

		// if not exist, create the MaterialRepository object, save it as a GlobalVariable
		if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_REPOSITORY) ) {
			Files.createDirectories(materialsDir)
			MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsDir)
			mr.markAsCurrent(TSuiteName.SUITELESS_DIRNAME, TExecutionProfile.UNUSED_DIRNAME, TSuiteTimestamp.TIMELESS_DIRNAME)
			def tsr = mr.ensureTSuiteResultPresent(TSuiteName.SUITELESS_DIRNAME, TExecutionProfile.UNUSED_DIRNAME, TSuiteTimestamp.TIMELESS_DIRNAME)
			GVH.ensureGlobalVariable(MGV.MATERIAL_REPOSITORY, mr)
		}
		MaterialRepository gvMR = (MaterialRepository)GVH.getGlobalVariableValue(MGV.MATERIAL_REPOSITORY)
		WebUI.comment("${hd} GlobalVariable.${MGV.MATERIAL_REPOSITORY} is located at \"${gvMR.getBaseDir()}\"")

		// if not exist, create the MaterialStorage object, save it as a GlobalVariable
		if ( ! GVH.isGlobalVariablePresent(MGV.MATERIAL_STORAGE) ) {
			Files.createDirectories(storageDir)
			MaterialStorage ms = MaterialStorageFactory.createInstance(storageDir)
			GVH.ensureGlobalVariable(MGV.MATERIAL_STORAGE, ms)
		}
		MaterialStorage gvMS = (MaterialStorage)GVH.getGlobalVariableValue(MGV.MATERIAL_STORAGE)
		WebUI.comment("${hd} GlobalVariable.${MGV.MATERIAL_STORAGE} is located at \"${gvMS.getBaseDir()}\"")

		// if not exist, create the ReportsAccessor object, save it as a GlobalVariable
		if ( ! GVH.isGlobalVariablePresent(MGV.REPORTS_ACCESSOR)) {
			ReportsAccessor ra = ReportsAccessorFactory.createInstance(reportsDir)
			GVH.ensureGlobalVariable(MGV.REPORTS_ACCESSOR, ra)
		}
		ReportsAccessor gvRA = (ReportsAccessor)GVH.getGlobalVariableValue(MGV.REPORTS_ACCESSOR)
		WebUI.comment("${hd} GlobalVariable.${MGV.REPORTS_ACCESSOR} is located at \"${gvRA.getReportsDir()}\"")
	}



	/**
	 * 
	 * @param testCaseContext
	 */
	void afterTestCase(TestCaseContext testCaseContext) {
		// nothing to do
	}

	/**
	 * 
	 * @param testSuiteContext
	 */
	void afterTestSuite(TestSuiteContext testSuiteContext) {
		String testSuiteId        = testSuiteContext.getTestSuiteId()        // e.g. 'Test Suites/TS1'
		String executionProfile   = RunConfiguration.getExecutionProfile()   // e.g. 'CURA_DevelopmentEnv'
		String testSuiteTimestamp = reportFolder.getFileName().toString()    // e.g. '20180618_165141'
		MaterialRepository mr = (MaterialRepository)GVH.getGlobalVariableValue(MGV.MATERIAL_REPOSITORY)
		mr.markAsCurrent(testSuiteId, executionProfile, testSuiteTimestamp)

		// If the TestSuiteResult has a MaterialMetadataBundle property,
		if (mr.hasMaterialMetadataBundleOfCurrentTSuite()) {

			WebUI.comment("printing visited-urls report for ${testSuiteId}/${executionProfile}/${testSuiteTimestamp}")

			// convert it into Markdown format and write Materials/tSuiteName/executionProfile/tSuiteTimestamp/visited-urls.md file
			Path markdown = mr.getCurrentTestSuiteDirectory().resolve(MaterialMetadataBundle.URLS_MARKDOWN_FILE_NAME)
			mr.printVisitedURLsAsMarkdown(new FileWriter(markdown.toFile()))

			// convert it into TAB-Separated-Values format and write Materials/tSuiteName/executionProfile/tSuiteTImestamp/visited-urls.txt
			Path tsv = mr.getCurrentTestSuiteDirectory().resolve(MaterialMetadataBundle.URLS_TSV_FILE_NAME)
			mr.printVisitedURLsAsTSV(new FileWriter(tsv.toFile()))

		} else {
			WebUI.comment("There found no MaterialMetadataBundle for ${testSuiteId}/${executionProfile}/${testSuiteTimestamp}")
		}

		// write the id of Test Suite which was last executed into a GlobalVariable, and write it into json file.
		// write the Execution Profile name as well.
		// It is necessary to pass the info via file to the next TestSuite run.
		// Because a GlobalVariable is TestSuite-scoped; it is not automatically passed across TestSuite boundaries.
		def lastExecutedTestsuiteId     = this.getRelativeTestSuiteId(testSuiteContext)
		def lastAppliedExecutionProfile = RunConfiguration.getExecutionProfile()
		GVH.ensureGlobalVariable(ManagedGlobalVariable.LAST_EXECUTED_TESTSUITE_ID, lastExecutedTestsuiteId)
		GVH.ensureGlobalVariable(ManagedGlobalVariable.LAST_APPLIED_EXECUTION_PROFILE, lastAppliedExecutionProfile)
		WebUI.comment("GlobalVariable.${ManagedGlobalVariable.LAST_EXECUTED_TESTSUITE_ID} is set to ${lastExecutedTestsuiteId}")
		WebUI.comment("GlobalVariable.${ManagedGlobalVariable.LAST_APPLIED_EXECUTION_PROFILE} is set to ${lastAppliedExecutionProfile}")

		//
		Files.createDirectories(GLOBAL_VARIABLES_JSON.getParent())
		List<String> names = [
			MGV.LAST_EXECUTED_TESTSUITE_ID.getName(),
			MGV.LAST_APPLIED_EXECUTION_PROFILE.getName()
		]
		Writer writer = new OutputStreamWriter(new FileOutputStream(GLOBAL_VARIABLES_JSON.toFile()), "utf-8")
		GVH.write(names, writer)
	}

	/**
	 * if testSuiteContext.getTestSuiteId() is "Test Suites\CURA\chronos_capture" 
	 * then returns "CURA/chronos_capture".
	 * 
	 * 1. chomp "Test Suite" prefix off
	 * 2. replace File.separator (\ on Windows) to /
	 * 
	 * @param testSuiteContext
	 * @return
	 */
	static String getRelativeTestSuiteId(TestSuiteContext testSuiteContext) {
		Objects.requireNonNull(testSuiteContext, "testSuiteContext must not be null")
		Path p = Paths.get(testSuiteContext.getTestSuiteId())
		return p.subpath(1, p.getNameCount()).toString().replace(File.separator, '/')
	}
}

