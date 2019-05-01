Test Suites/CURA/Execute_twins
=========


The Test Suite `Main/TS1` visits the target URL and traverse pages while taking screen shots. Directory named  `./Materials/Main.TS1/yyyyMMdd_hhmmss/Main.Basic` will be created where 5 PNG files are stored.

`Executes` calls Test Suite `ImageDiff`. The Test Suite `ImageDiff` scans 2 directories previously created by `Main/TS1` and compares pairs of PNG files with same file name: e.g. `CURA_Homepage.png`.

The test suite `ImageDiff` generates an PNG file in directory named `./Materials/ImageDiff/yyyyMMdd_hhmmss/ImageDiff`. If any visual difference found, the generated PNG file will show the difference in red color like this:
![ImageDiff](docs/images/CURA_Homepage.diff.png)

The following picture shows how our Visual Testing in Katalon Studio goes.

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
