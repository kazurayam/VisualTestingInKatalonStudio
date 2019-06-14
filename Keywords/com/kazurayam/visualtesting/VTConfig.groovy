package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Paths

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonSlurper

public class VTConfig {

	// fields
	static Logger logger_ = LoggerFactory.getLogger(VTConfig)

	static final String CONFIG_FILE_NAME = 'vt-config.json'
	/**
	 * Ｙｏｕ　ｃａｎ　ｌｏｃａｌｔｅ　Ｍａｔｅｒｉａｌｓ　ａｎｄ　Ｓｔｏｒａｇｅ　ｄｉｒｅｃｔｏｒｙ　ｏｎ　a nｅｔwork drive by writing as followings in <project dir>/vt-config.json
	 * <PRE>
	 * {
	 *     "AUXILIARY_VT_PROJECT_DIR": "G:/マイドライブ/vtprojects"
	 * }
	 * </PRE>
	 */
	static final String PROPERTY_AUX_DIR = 'AUXILIARY_VT_PROJECTS_DIR'

	private def config_

	VTConfig() {
		this(Paths.get(RunConfiguration.getProjectDir()).resolve(CONFIG_FILE_NAME).toFile())
	}

	VTConfig(File vtConfig) {
		if (vtConfig.exists()) {
			JsonSlurper slurper = new JsonSlurper()
			config_ = slurper.parse(vtConfig)
		} else {
			logger_.info("${vtConfig} does not exist")
			config_ = null
		}
	}

	String getAuxiliaryVTProjectDir() {
		return config_[PROPERTY_AUX_DIR]
	}
}
