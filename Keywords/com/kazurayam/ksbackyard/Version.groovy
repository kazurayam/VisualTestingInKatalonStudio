package com.kazurayam.ksbackyard

/**
 * Administrative class to keep track of the version number of
 * the ksbackyard release.
 *
 * @author kazurayam
 */
class Version {

	static String getVersion() {
		return "${getProduct()} ${getMajorVersionNum()}.${getReleaseVersionNum()}.${getDevelopmentVersionNum()}"
	}

	static String getProduct() {
		return 'ksbackyard'
	}

	static int getMajorVersionNum() {
		return 0
	}

	static int getReleaseVersionNum() {
		return 22
	}

	static int getDevelopmentVersionNum() {
		return 0
	}
}