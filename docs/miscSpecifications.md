Miscellaneous descriptions
=========

## Sequence diagram

The following picture shows how `Test Suites/CURA/Execute_twins` goes.

![sequence-diagram](docs/images/sequence-diagram.png)

## ImageDiff file naming rule

The file name of ImageDiff will be in the format as follows

1. An ImageDiff file has a name for example: `CURA_Homepage.20180920_165543_product-20180920_165544_develop.(6.30)FAILED.png`
1. prefix part `CURA_Homepage` is equal to the file name prefix of the source image file `CURA_Homepage.png`
2. middle part `yyyyMMdd_hhmmss_PPPPPPP` is equal to the timestamp when test suite `Main/TS` was executed, and `PPPPPPP` is the Katalon Execution Profile applied to the test suite execution.
3. `(6.30)` is called *diff%* = number of read pixcels(differences) / (width * hight of page) * 100
4. `FAILED` is marked by the test suite `ImageDiff` in case that the 2 source images are *too different*.
5. What does "too different" means? The Test Case `ImageDiff` has the following code:
```
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.makeDiffs'(
    'product', 'develop', 'Main/TS1', 3.68)
```
The 4th argument is called *criteria%*. If the difference is larger than the criteria% specified, then the generated ImageDiff file will be marked `FAILED`. This mark enables CSS in ./Materials/index.html to show the line highlighted.
