package com.kazurayam.ksbackyard

import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

public class RunConfigurationWrapper {

	/**
	 * return a Path object which represents the absolute path of the Project directory
	 * 
	 * @return
	 */
	@Keyword
	static Path getProjectDir() {
		return Paths.get(RunConfiguration.getProjectDir())
	}

	/**
	 * given the project directory is "C:\project\dir" and the param pathString is "./tmp/foo", 
	 * then returns a Path object "C:\project\dir\tmp\foo"
	 * 
	 * @param pathString
	 * @return
	 */
	@Keyword
	static Path resolveAgainstProjectDir(String pathString) {
		Path proj = getProjectDir()
		return proj.resolve(pathString).normalize()
	}
}
