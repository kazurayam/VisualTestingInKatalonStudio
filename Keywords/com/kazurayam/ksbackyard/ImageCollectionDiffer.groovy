package com.kazurayam.ksbackyard

import com.kazurayam.imagedifference.ImageCollectionDiffer as ICD
import com.kazurayam.imagedifference.VisualTestingListener
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.Material
import com.kazurayam.materials.MaterialPair
import com.kazurayam.materials.TCaseName

import com.kms.katalon.core.util.KeywordUtil

/**
 * This class is designed to implement the "Visual Testing in Katalon Studio" feature.
 *
 * This class uses the following 2 external libraries:
 * 1. AShot (https://github.com/yandex-qatools/ashot)
 * 2. Materials (https://github.com/kazurayam/Materials)
 *
 * The makeImageCollectionDifferences() method provides the core value of this class.
 * This method accepts Materials (image files) to compare them, make differences, and
 * store the diff-images into files.
 *
 * @deprecated use com.kazurayam.imagedifference.ImageCollectionDiffer#makeImageCollectionDifferences
 *
 * @author kazurayam
 */
class ImageCollectionDiffer {

	private ICD delegate_

	/**
	 * constructor
	 *
	 * @param mr
	 * @author kazurayam
	 */
	ImageCollectionDiffer(MaterialRepository mr) {
		delegate_ = new ICD(mr)

		// set VisualTestingListener which notifies Katalon Studio of failures
		delegate_.setVTListener(new VisualTestingListenerNotifyingKatalon())
	}

	/**
	 * compare 2 Material files in each MaterialPair object,
	 * create ImageDiff and store the diff image files under the directory
	 * ./Materials/<tSuiteName>/yyyyMMdd_hhmmss/<tCaseName>.
	 * The difference ratio is compared with the criteriaPercent given.
	 * Will be marked FAILED if any of the pairs has greater difference.
	 *
	 * @param materialPairs created by
	 *     com.kazurayam.materials.MaterialRpository#createMaterialPairs() method
	 * @param tCaseName     created by com.kazurayam.materials.TCaseName(String)
	 * @param criteriaPercent e.g. 3.00 percent. If the difference of
	 *     a MaterialPair is greater than this,
	 *     the MaterialPair is evaluated FAILED
	 *
	 *
	 */
	void makeImageCollectionDifferences(
			List<MaterialPair> materialPairs,
			TCaseName tCaseName,
			Double criteriaPercent) {

		delegate_.makeImageCollectionDifferences(materialPairs, tCaseName, criteriaPercent)
	}



	/**
	 * This VisualTestingListener notifies Katalon Studio of failures
	 */
	static class VisualTestingListenerNotifyingKatalon implements VisualTestingListener {

		void info(String message) {
			KeywordUtil.logInfo(message)
		}

		void failed(String message) {
			KeywordUtil.markFailed(message)
		}

		void fatal(String message) {
			KeywordUtil.markFailedAndStop(message)
		}
	}
}
