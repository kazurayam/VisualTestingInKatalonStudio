Step by step instruction - how to create a visual testing project from scratch
===========

- Author: kazurayam
- #2 Date: 14 Feb 2019
- #1 Date: 22 November 2018

## Required Katalon Studio version

Katalon Studio 5.7.1 and lower, or 5.8.4 and upper is required.

Unfortunately Katalon Studio 5.8.0 .. 5.8.3 has a restriction: https://forum.katalon.com/discussion/10315/5-8-0-globalvariable-of-type-null-is-not-allowed-no-longer-store-instances-of-user-defined-class

## Overview

In this note, I will describe how you create a new Katalon Studio project and set it up to carry out a *Visual Testing* as I did.

I will create a Katalon Studio project where I do the following:

1. take screenshots of 2 environments of your AUT (*Application Under Test*) = a pair of different URLs. We will use the [Google Search](https://www.google.com/) as a test bed.
2. compare each pairs of screenshots to find out any visual differences
3. make a HTML file as the viewer of generated images.

Here is a example visual difference output:

![output](./images/StepByStep/step9_search_result.20181025_161316_google.com-20181025_161317_google.co.jp.%280.01%29.png)

The 2 URLs were almost identical for 99.99% except 0.01% of difference. In the above image, you can find a very small area (just a few characters) is painted <span style="color: red; ">red</span>. This section was the milli-seconds taken for table search. *Without comparison by the tool, this difference would never be noticed.* :-)

## Preparation

I will use a term '*the demo project*' for short for the  [VisualTestingInKatalonStudio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project.


### prep1: create a new Katalon Studio project

Do it as usual.

### prep2: resolve external dependencies

You need to include 2 external jar files into the new project.

1. `aShot` : WebDriver Screenshot utility. Its jar is public at the [MavenCentral repository](https://mvnrepository.com/artifact/ru.yandex.qatools.ashot/ashot/1.5.4). Also you can reuse the `ashot-1.5.4.jar` contained in the demo project's [`Drivers`](../Drivers) directory.

2. `Materials` : Utility that manages the `<project dir>/Materials` directory where you can store any files created by test cases in a well structured path format. The Materials-0.26.0.jar file is public at the [releases](https://github.com/kazurayam/Materials/releases) page. Also you can reuse the jar file contained in the demo project's [`Drivers`](../Drivers) directory.

>Another project  [UsingMaterialsInKatalonStudio](https://github.com/kazurayam/UsingMaterialsInKatalonStudio) describes how to use the `Materials` library in detail.

3. `ImageDifference` : Utility that compares two images or two sets of images files. Its jar is public at the [releases](https://github.com/kazurayam/ImageDifference/releases) page.

Please refer to the Katalon Documentation [*How to import external libarary into your automation project*](https://docs.katalon.com/katalon-studio/tutorials/import_java_library.html) for Katalon GUI operation.

### prep3: create GlobalVariables for the Materials library

The Materials library requires 2 GlobalVariables defined in the Execution Profile.

1. `GlobalVariable.MATERIAL_REPOSITORY` : its type should be Null
2. `GlobalVariable.CURRENT_TESTCASE_ID` : its type should be String

You need to define them in the Execution Profiles in your project. Following screen shot shows 2 variables defined in the *default* Profile.
![Profile_default](./images/Profile_GlobalVariable.PNG)



### prep4: create a Test Listener

You need to create a Test Listener named `Test Listeners/TL` in your project. All `TL` does is to set appropriate values to `GlobalVariable.MATERIAL_REPOSITORY` and `GlobalVariable.CURRENT_TESTCASE_ID`.

You can copy the code of the [`Test Listners/TL`](../Test%20Listeners/TL.groovy) into you test listener `TL`. You need not to modify it at all.

Or if you want you make your own test listener, you can do so of course. Please merge the codes of [`Test Listners/TL`](../Test%20Listeners/TL.groovy) into your one  carefully.


### prep5: create Custom Keywords

Following Custom Keywords are required to carry out "Visual Testing". Please make the keyword files and copy the source from the demo project. You do no need to change the source at all.

1. [Keywords/com/kazurayam/ksbackyard/Assert](../Keywords/com/kazurayam/ksbackyard/Assert.groovy)
2. [Keywords/com/kazurayam/ksbackyard/ImageCollectionDiffer](../Keywords/com/kazurayam/ksbackyard/ImageCollectionDiffer.groovy)
3. [Keywords/com/kazurayam/ksbackyard/ScreenshotDriver](../Keywords/com/kazurayam/ksbackyard/ScreenshotDriver.groovy)
4. [Keywords/com/kazurayam/ksbackyard/TestObjectSupport](../Keywords/com/kazurayam/ksbackyard/TestObjectSupport.groovy)

`ScreenshotDriver` class enables you to take page screenshot using [AShot API](https://github.com/yandex-qatools/ashot). `ImageCollectionDiffer` keyword processes a List of pairs of screenshots and generates a set of difference image files. `Assert` and `TestObjectSupport` are helper classes used by the `ScreenshotDriver`.

## Creating your Visual Testing test

Now you are ready to create a new Visual Testing project in Katalon Studio for yourself. I will tell you how to step by step.

### Test scenario

We need a test scenario anyway. Let me set our scenario as follows:

1. We will visit the following 2 URL.
   - https://www.google.com/ncr
   - https://www.google.co.jp/ncr
2. We will perform Google search with query of `q="katalon"`
3. We will take entire-page screen shots of the search form page and the search result page.
4. We will compare the images of a URL. If not identical, we want to know how much of visual differences there are.

>*[NCR(no country redirect)](https://whatis.techtarget.com/definition/NCR-no-country-redirect) is a Google search parameter that tells the search engine to show results for the country specified in the URL rather than redirecting to the country from which the search is being conducted. You can override the redirection by adding `/ncr` (No Country Redirect) to the Google URL for the country in question : https://www.google.com/ncr*

### step01 : A simple test case as starting point

At first you want a simple test case where you use ordinary Katalon-built-in features only. Here is the source:
[`Test Cases/StepByStep/step01_TC - start up`](../Scripts/StepByStep/step01_TC%20-%20start%20up/Script1549948893162.groovy)

```
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// resolve output dir
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outdir = projectDir.resolve('tmp')
Files.createDirectories(outdir)

// open browser
WebUI.openBrowser('')
WebUI.setViewPortSize(1279, 720)

// navigate to the Google form page
WebUI.navigateToUrl('https://www.google.com/')

WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_search/input_q'), 10)
WebUI.setText(findTestObject('StepByStep/Page_Google_search/input_q'), 'katalon')

// take screen shot and save it into search_form.png file
Path fileF = outdir.resolve("search_form.png")
WebUI.takeScreenshot(fileF.toString())

// submit query, page is transfered to the Google result page
WebUI.submit(findTestObject('StepByStep/Page_Google_search/input_q'))
WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_result/div_g_1'), 10)

// take scree shot and save it into search_result.png file
Path fileR = outdir.resolve("search_result.png")
WebUI.takeScreenshot(fileR.toString())

// close browser
WebUI.closeBrowser()
```
This is a basic Katalon Studio test case script. No tricks at all.

You can run this test case just as usual. Once ran it, you will find output files created in the *&lt;projectDir&gt;*`/tmp` folder.
![outputs](images/StepByStep/step1_outputs_in_tmp.png)

### step02 : Resolve output file path using Materials library

We want the output files are located at path managed by the Materials library. We are to change the test case slightly.

Here is the source: [`Test Cases/StepByStep/step02_TC - Materials applied`](../Scripts/StepByStep/step02_TC%20-%20Materials%20applied/Script1549948900396.groovy)
```
import com.kazurayam.materials.MaterialRepository
import internal.GlobalVariable as GlobalVariable

...

MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

...

URL urlF = new URL(WebUI.getUrl())
Path fileFnamedByURL = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, urlF)
WebUI.takeScreenshot(fileFnamedByURL.toString())

...
WebUI.submit(findTestObject('StepByStep/Page_Google_search/input_q'))
WebUI.verifyElementPresent(findTestObject('StepByStep/Page_Google_result/div_g_1'), 10)

...

URL urlR = new URL(WebUI.getUrl())
Path fileRnamedByURL = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, urlR)
WebUI.takeScreenshot(fileRnamedByURL.toString())
```

`GlobalVariable.MATERIAL_REPOSITORY` and `GlobalVariable.CURRENT_TESTCASE_ID` are set with values by the Test Listener [`Test Listners/TL`](../Test%20Listeners/TL.groovy).


The step02 is calling  `com.kazurayam.materials.MaterialRepository#resolveScreenshotPath(String testCaseId, URL url)` method. This method generates URL-based file name for screenshots. For example: `https%3A%2F%2Fwww.google.com%2F.png`. This file name is generated by converting the URL string `https://www.google.com/`. `:` is converted to `%3A`. `/` is converted into `%2F`.

You can run the test case step2 just as usual. The output will be written in the folder *&lt;projectDir&gt;*`/Materials/_/_/StepByStep.step02_TC - Materials applied`

![step2 output](images/StepByStep/step2_Materials.png)

Are you curious about the strange path *&lt;projectDir&gt;*`/Materials/_/_` ? This will be explained later.


### step03 : Take entire page screen shots

The step2 script uses `WebUI.takeScreenshot()` method to take screen shot of web pages. This is a Katalon Studio's built-in keyword. Unfortunately this method can not take entire page screenshot. For example, the screen shot of the search result page will show just top 3 or 4 links.
![step2_search_result](images/StepByStep/step2_search_result.png)

But I want entire page screen shots. Therefore I will use a custom keyword `com.kazurayam.ksbackyard.ScreenshotDriver` backed by [AShot](https://github.com/yandex-qatools/ashot).

Here is the scritp source: [`Test Cases/StepByStep/step03_TC`](../Scripts/StepByStep/step03_TC%20-%20Entire%20page%20screenshot/Script1549948907494.groovy)

```
...
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileFnamedByURL.toFile())

...

CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileRnamedByURL.toFile())
```

The `saveEntirePageImage()` method generates a entire-page screen shot like this:

![step03 search result](images/StepByStep/step3_search_result.png)

### step04 : Parameterize URL in question by Execution Profiles

I am going to visit 2 URL using a single set of test case scripts. Therefore I need to parameterize the URL out of the test scripts. The best way is to create 2 Execution Profiles for each URLs.

I made 2 profiles named `google.com` and `google.co.jp` in the demo  project. Principally the name of Profile can be any. Each profiles contains a set of GlobalVariables: `URL`. as parameters which are environment-dependent.

Profle > `google.com`
![Profile_google.com](./images/Profile_google.com.PNG)

Profile > `google.co.jp`
![Profile_google.co.jp](./images/Profile_google.co.jp.PNG)

The Materials libary does require 2 GlobalVariables:
`MATERIAL_REPOSITORY` and `CURRENT_TESTCASE_ID` to be defined in the Profile `google.com` and `google.co.jp` as well.

The test case [step04](../Scripts/StepByStep/step04_TC%20-%20parameterized%20URL/Script1549948907494.groovy) uses [`MaterialRepository#resolveScreenshotPath(String testCaseId, URL url)`](https://kazurayam.github.io/Materials/com/kazurayam/materials/MaterialRepository.html). The generated file names as follows:

| Profile      | URL                        | File name |
| ------------ | -------------------------- | ------------------------------------- |
| google.co.jp | https://wwww.google.co.jp/ | https%3A%2F%2Fwww.google.co.jp%2F.png |
| google.com   | https://www.google.com/    | https%3A%2F%2Fwww.google.com%2F.png   |

### step05 : Identical file names

Now we want to compare the two files. The test case [step05_TC](../Scripts/StepByStep/step05_TC/Script1549948936458.groovy) gives simple file names: `search_form.png` and `search_result.png`.

| Profile      | URL                        | File names |
| ------------ | -------------------------- | --------------- |
| google.co.jp | https://wwww.google.co.jp/ | search_form.png,  serach_result.png |
| google.com   | https://www.google.com/    | search_form.png, search_result.png |

The test case `step05_TC` uses `MaterialRepository#resolveMaterialPath(String testCaseId, String fileName)`:

```
// Path fileFnamedByURL = mr.resolveScreenshotPath(GlobalVariable.CURRENT_TESTCASE_ID, urlF)
Path fileF = mr.resolveMaterialPath(GlobalVariable.CURRENT_TESTCASE_ID, "search_form.png")
```

### step06 : Introducing Test Suite Collection to activate a Test Suite multiple times using different profiles

I want to take screenshots of https://www.google.com/ and https://www.google.co.jp/. I can do that by executing the test case [step5](../Scripts/StepByStep/TC_step5/Script1540190327767.groovy) while specifying a URL via Execution Profile. Therefore you need to execute the test case step5 twice while specifying tw Execution Profiles: `google.com` and `google.co.jp`.

You can implement this by creating
- a Test Suite `Test Suites/StepByStep/step05_TS`
- a Test Suite Collection `Test Suites/StepByStep/step06_TSC - execute step05_TS twice with different Profile`.

The `Test Suites/step06_TSC` looks like this:
![step6](./images/StepByStep/step6_execute_step5_twice_with_different_profile.png)

Please note that the Test Suite Collection `step06_TSC` executes the Test Suite `step05_TS` twice while applying Profile `google.com` and `google.co.jp` each.

The Test Suite `Test Suites/step05_TS` looks like this:
![step05_TS](images/StepByStep/step5_just_calls_TC_Step5.png)
All it does is to call `Test Cases/StepByStep/step05_TC`.

When you execute the Test Suite Collection  `Test Suites/step06_TSC`, you will find PNG files are saved under the *&lt;projectDir&gt;*`/Materials` directory.
![step6_Materials](images/StepByStep/step6_Materials.png)

### step07 : Make Materials/index.html

You can generate *&lt;projectDir&gt;*`/Materials/index.html` file by calling `makeIndex()` method of `com.kazurayam.materials.MaterialRepository` object. The index page generated will looks like this:
![step7_Materials_index](images/StepByStep/step7_Materials_index.png)

By clicking one of the row in the index.html You can open a screenshot image in a modal window.
![step7_Materials_index_modal](images/StepByStep/step7_Materials_index_modal.png)

The index pages shows what you can see by Windows Explorer or Mac Finder. No more than that. But the index page is more convenient to view contents of *&lt;projectDir&gt;*`/Materials/index.html` than those generic tools. Why? In the index page,

1. Test Suite results are sorted by reverse order of the timestamp of Test Suites' execution. You always find the newest result at the page top.
2. Meta data of Test Suite execution are shown: which browser was used, which Profile was applied, how many test cases were run and how many test cases failed.
3. In the modal window, various types of files are rendered nicely: JSON is pretty-printed, XML is indented, images are auto-resized to fit the modal window's width, etc.
4. Windows Explorer does *LOCK* the files and directories while you are viewing them in the Explorer windows. Sometimes this LOCK causes Katalon Studio to fail getting access to the files in the Materials directory. Once race condition occurs, you have to restart Windows OS. This is very annoying. Therefore you should avoid using Windows Explorer as much as possible. On the other hand, Web Browsers does not *LOCK* the files in the Materials directory at all. You should use `Materials/index.html` in browser as it does not cause any race condition.

You can create *&lt;projectDir&gt;*`/Materials/index.html` file by running [Test Cases/makeIndex](../Scripts/makeIndex/Script1534133594816.groovy)

The `Test Suites/StepByStep/step07_TSC - plus makeIndex` shows how to make index.html in practice. It will look like this.
![step07_TSC](images/StepByStep/step7_TSC_step7.png)

In the Test Suite Collection `step07_TSC` you run the Test Suite `step05_TS` twice applying the Profiles `google.com` and `google.co.jp` for each. After that you run the Test Suite `Test Suites/makeIndex`. The source of `Test Cases/makeIndex` is here:
- [Test Cases/makeIndex](../Scripts/makeIndex/Script1534133594816.groovy)

This test case is just calling `makeIndex()` method of `com.kazurayam.materials.MaterialRepository` object without any parameters. The method scans the  *&lt;projectDir&gt;*`/Materials` directory for all contained files and generates *&lt;projectDir&gt;*`/Materials/index.html` file.

### step08 : running ImageDiff to compare screenshots of two URLs

We will go one step forward. We will first take screenshots of 2 URLS and store the images into the `Materials` directory, and then we will compare the images to find visual differences. The `Test Suites/StepByStep/step08_TSC - plus ImageDiff` executes this processing.
![TSC_step8_imageDiff](images/StepByStep/step8_TSC_step8_imageDiff.png)

The difference between the step7 and step8 is only one point: Step08 calls `Test Suites/StepByStep/step05_ImageDiff` immediately after calling `Test Suites/StepByStep/step05_TS` twice.

The Test Suite `Test Suites/StepByStep/step05_ImageDiff` which calls another Test Case `Test Cases/StepByStep/ImageDiff` with 2 parameters:
- TESTSUITE_ID
- CRITERIA_PERCENTAGE

```
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.callTestCase(findTestCase('StepByStep/ImageDiff'),
	[
		'TESTSUITE_ID': 'StepByStep/step05_TS',
		'CRITERIA_PERCENTAGE': '0.5'
	])
```

Click the following link to see the source of the test case:
- [ImageDiff](../Scripts/StepByStep/ImageDiff/Script1540083424391.groovy)

The core part of the test case `ImageDiff` is as follows:

```
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

List<MaterialPair> materialPairs = mr.createMaterialPairs(
		new TSuiteName( TESTSUITE_ID )
		).stream().filter { mp ->
			mp.getLeft().getFileType() == FileType.PNG
		}.collect(Collectors.toList())

new ImageCollectionDiffer(mr).makeImageCollectionDifferences(
		materialPairs,
		new TCaseName(GlobalVariable.CURRENT_TESTCASE_ID),  // 'Test Cases/StepByStep/ImageDiff'
		CRITERIA_PERCENTAGE
        )
```

This code snippet is short but in fact a lot of details are encapsulated in the external Java classes.

`com.kazurayam.materials.MaterialRepository#createMaterialPairs(TSuiteName)` returns a List<MaterialPairs>. See the javadoc comment of the method at the line 340 of [MaterialRepositoryImpl](https://github.com/kazurayam/Materials/blob/develop/src/main/groovy/com/kazurayam/materials/MaterialRepositoryImpl.groovy)


>Scans the Materials directory to look up pairs of Material objects to compare.
>
>This method perform the following search under the &lt;projectDir&gt;/Materials directory in order to identify which Material object to be included.
>
> 1. selects all &lt;projectDir&gt;/Materials/&lt;Test Suite Name&gt;/&lt;yyyyMMdd_hhmmss&gt; directories with the name equals to the Test Suite Name specified as argument tSuiteName
>
> 2. among them, select the directory with the 1st latest timestamp. This one is regarded as "Actual one".
> 3. among them, select the directory with the 2nd latest timestamp. This one is regarded as "Expected one".
> 4. please note that we do not check the profile name which was applied to each Test Suite run. also we do not check the browser type used to each Test Suite run.
> 5. Scan the 2 directories selected and create a List of Material objects. 2 files which have the same path under the &lt;yyyyMMdd_hhmmss&gt; directory will be packaged as a pair to form a MaterialPair object.
> 6. A List&lt;MaterialPair&gt; is created, fulfilled and returned as the result


The `com.kazurayam.ksbackyard.ImageCollectionDiffer` class is a custom keyword , it is located [here](../Keywords/com/kazurayam/ksbackyard/ImageCollectionDiffer.groovy) in the Keywords directory.

```
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
 */
void makeImageCollectionDifferences(
        List<MaterialPair> materialPairs,
        TCaseName tCaseName,
        Double criteriaPercent) {

    ...

}

```

Executing `Test Suites/StepByStep/TSC_step8 - plus ImageDiff` will creates `<projectDir>/Materials/StepByStep.ImageDiff` directory as well as `<projectDir>/Materials/StepByStep.TS_step5`. In the `<projectDir>/Materials/StepByStep.ImageDiff` directory you will find image files which shows the difference of each pairs of screenshots.

```
C:\USERS\QCQ0264\KATALON-WORKSPACE\VISUALTESTINGINKATALONSTUDIO\MATERIALS
│
│  index.html
│
├─StepByStep.ImageDiff
│  └─20181122_143352
│      └─StepByStep.ImageDiff
│          └─StepByStep.step05_TC
│                  search_form.20181122_143351_google.com-20181122_143352_google.co.jp.(0.00).png
│                  search_result.20181122_143351_google.com-20181122_143352_google.co.jp.(0.01).png
│
└─StepByStep.step05_TS
    ├─20181122_143351
    │  └─StepByStep.step05_TC
    │          search_form.png
    │          search_result.png
    │
    └─20181122_143352
        └─StepByStep.step05_TC
                search_form.png
                search_result.png
```

An ImageDiff file has following format of file name:
`search_result.20181122_143351_google.com-20181122_143352_google.co.jp.(0.01).png`

| file name element | meaning |
|:------------------|:--------|
| `search_result`   | Material file name without extension (`.png`)  |
| `.`               |   |
| `20181122_143351` | Timestamp of Expected image  |
| `_`               |   |
| `google.com`      | Execution Profile applied to the Test Suite run as Expected one |
| `_`               |   |
| `20181122_143352` | Timestamp of Actual image  |
| `-`               |   |
| `google.co.jp`    | Execution Profile applied to the Test Suite run as Actual one  |
| `.`               |   |
| `(0.01)`          | Difference of Expected & Actual images in percentage  |
| `.`               |   |
| `png`             | image file type: PNG  |

The &lt;projectDir&gt;`Materials/index.html` page now includes the contents of `StepByStep.ImageDiff` directory as follows:

![step8_Materials_index](images/StepByStep/step8_Materials_index.png)



### step09 : clearing ./Materials directory first

If you execute the `Test Suites/StepByStep/step08_TSC - plus ImageDiff` and other test suites
multiple times, many sub-directories will be created in the Materials repository .
![Materials_index_with_old_records](images/StepByStep/step9_Materials_index_with_old_records.png)


Old records are useless in most cases, but will NOT be removed automatically. These sub-directories will be retained until you intentionally remove them.

The Test Suite Collection `Test Suites/StepByStep/step09_TSC - with cleanMaterials` shows how to clear the Materials directory.

![step09_TSC_cleanMaterials](images/StepByStep/step9_TSC_step9_cleanMaterials.png)

The `step09_TSC` calls `Test Suites/cleanMaterials` which calls [`Test Cases/cleanMaterials`](../Scripts/cleanMaterials/Script1534134775171.groovy)

```
MaterialRepository mr = (MaterialRepository)GlobalVariable.MATERIAL_REPOSITORY

Helpers.deleteDirectoryContents(mr.getBaseDir())
```

This code removes all the contents under the `Materials` directory recursively but retains the `Materials` directory itself.

The `Test Suites/StepByStep/step09_TSC - with cleanMaterials` will generate `Materials/index.html` cleared out as follows:
![cleaned](images/StepByStep/step9_Materials_index_cleared.png)


### step10 : ignoring particular web elements

`Test Suites/StepByStep/step09_TSC - with cleanMaterials` will generate a diff image like this:

![output](./images/StepByStep/step9_search_result.20181025_161316_google.com-20181025_161317_google.co.jp.%280.01%29.png)

You will find a small portion painted <span style="color: red; ">red</span>. This postion displays a statistics data. For example:

```
About 357,000 results (0.36 seconds)
```

The turn-around time (0.36) is variable. www.google.com and www.google.co.jp tends show different value here. I am not interested in the turn-around time very much. The the visual difference caused by this varying statistics data is a noise for me. Therefore I want ignore this specific web element when I compare screen shots.

You can modify the PNG image file: paint specific web elements with <span style="color:white; background-color:grey;">grey rectangles</span>.

You can see an example at a Test Suite Collection `Test Suites/StepByStep/step10_TSC - ignoring specifc web elements`. Try execuiting it. You will find the screenshots images with grey rectangles as follows:

![step10_stats_painted_grey](./images/StepByStep/step10_stats_painted_grey.png)

2 images are similarly painted. The statistics data is no longer visible in both images. The difference image will become less noisy.

But how you should write code to specify which web elements to ignore? You can find an example of test case script at [Test Cases/Scripts/StepByStep/step10_TC](../Scripts/StepByStep/step10_TC/Script1549952063213.groovy).

The following fragment shows how to specify web elements to ignore:

```
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kazurayam.ksbackyard.ScreenshotDriver
import com.kazurayam.ksbackyard.ScreenshotDriver.Options
import com.kazurayam.ksbackyard.ScreenshotDriver.Options.Builder
import com.kms.katalon.core.testobject.TestObject as TestObject
...

// web element to be ignored
TestObject resultStats = findTestObject('StepByStep/Page_Google_result/div_resultStats')

Builder builder = new ScreenshotDriver.Options.Builder()
Options options = builder.
					timeout(2000).    // scrolling timeout 2.0 seconds
					addIgnoredElement(resultStats).    
					// you can give multiple addIgnoreElement() here
					build()          // don't forget to call build()

// take screenshot of Google Result page,
// and paint <div id="resultStats"> with a grey rectangle to ignore insignificant difference
CustomKeywords.'com.kazurayam.ksbackyard.ScreenshotDriver.saveEntirePageImage'(
	fileS.toFile(),
	options)
```

## Conclusion

I believe that automated visual testing is an effective method to detect unplanned/accidental changes made somehow in your Web app. In this document I have tried to describe in detail how to get started with "Visual Testing in Katalon Studio" --- comparing 2 sites (production & developement) visually by taking screenshots and making diffs. Please try.
