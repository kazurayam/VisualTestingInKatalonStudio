import com.kazurayam.visualtesting.ImageCollectionDifferDriver
import com.kazurayam.visualtesting.ImageCollectionDifferDriver.ChronosOptions

/*
 * 47news/ImageDiff_chronos
 */
String TESTSUITE_ID = 'CURA/chronos_capture'
ChronosOptions options = new ChronosOptions.Builder().
							filterDataLessThan(2.0).
							shiftCriteriaPercentageBy(0.0).
							build()

CustomKeywords.'com.kazurayam.visualtesting.ImageDiffer.runChronos'(TESTSUITE_ID, options)
