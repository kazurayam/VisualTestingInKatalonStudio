Step by step instruction how to create your own visual testing project
===========

## Overview

In this note, I will describe how you create a new Katalon Studio project and set it up to carry out *Visual Testing* as I did.

I assume that you want your new project:
1. to take screenshots of 2 environments of your AUT (*Application Under Test*) : *Development* and *Production*
2. to compare images to find any significant visual differences
3. to make a HTML view of generated image files
4. to make the HTML view shows the summary of test results (PASS/FAILED)

I will use a term '*the demo project*' for short for the  [VisualTestingInKatalonStudio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project.

## Preparation
### prep1: create a new Katalon Studio project

Do it as usual.

### prep2: resolve external dependencies

You need to include 2 external jar files into your new project.

1. `aShot` : WebDriver Screenshot utility. Its jar is public at the [MavenCentral repository](https://mvnrepository.com/artifact/ru.yandex.qatools.ashot/ashot/1.5.4). Also you can reuse the `ashot-1.5.4.jar` contained in the demo project's [`Drivers`](../Drivers) directory.

2. `Materials` : Utility that manages the `<project dir>/Materials` directory where you can store any files created by test cases in a well structured path format. Another project  [UsingMaterialsInKatalonStudio](https://github.com/kazurayam/UsingMaterialsInKatalonStudio) describes how to use the `Materials` library in detail. The Materials-0.22.0.jar file is public at the [releases](https://github.com/kazurayam/Materials/releases) page. Also you can reuse the jar file contained in the demo project's [`Drivers`](../Drivers) directory.

Please refer to the Katalon Documentation [*How to import external libarary into your automation project*](https://docs.katalon.com/katalon-studio/tutorials/import_java_library.html) for Katalon GUI operation.

### prep3: create GlobalVariables for the Materials library to run

The Materials library requires 2 GlobalVariables defined.

1. `GlobalVariable.MATERIAL_REPOSITORY` : its type should be Null or String
2. `GlobalVariable.CURRENT_TESTCASE_ID` : its type should be String

You need to define them in the Execution Profiles in your project. Following screen shot shows 2 variables defined in the *default* Profile.
![Profile_default](./images/Profile_GlobalVariable.PNG)



### prep4: create a Test Listener

You need to create a Test Listener named `Test Listeners/TL` in your project. All `TL` does is to set appropriate values to `GlobalVariable.MATERIAL_REPOSITORY` and `GlobalVariable.CURRENT_TESTCASE_ID`.

You can copy the code of the [`Test Listners/TL`](../Test%20Listeners/TL.groovy) into you test listener `TL`. You need not to modify it at all.

Or if you want you make your own test listener, you can do so of course. Please merge the codes of [`Test Listners/TL`](../Test%20Listeners/TL.groovy) into yours carefully.

## Creating your Visual Testing test

Now you are ready to create a new Visual Testing project in Katalon Studio. I will tell you how to step by step.


### Test scenario

We need a test scenario anyway. Let me set our scenario as follows:

1. We will compare the following 2 URL.
   - https://www.google.com/ncr
   - https://www.google.co.jp/ncr
2. We will perform search with q="katalon"
3. We will take entire-page screen shots.
4. We well compare the images to see if these 2 URL respond identical view not. If not, we want to know how much of visual differences are there, and reason why.

[NCR(no country redirect)](https://whatis.techtarget.com/definition/NCR-no-country-redirect) is a Google search parameter that tells the search engine to show results for the country specified in the URL rather than redirecting to the country from which the search is being conducted. You can override the redirection by adding `/ncr` (No Country Redirect) to the Google URL for the country in question : https://www.google.com/ncr

### step1:
### prep4: create Execution Profiles which stand for environments of AUT

I made 2 profiles named `develop` and `product` in the demo  project. The profile `develop` stands for the Development Environment for the AUT (*Application Under Test*). The profile `product` stands for the Production Environment for the AUT. Each profiles contains a set of GlobalVariables: `Hostname`, `Username`, `Password` as parameters which are environment-dependent.

Profle > `develop`
![Profile_develop](./images/Profile_develop.PNG)

Profile > `product`
![Profile_product](./images/Profile_product.PNG)

The Materials libary does require
`GlobalVariable.MATERIAL_REPOSITORY` and `GlobalVariable.CURRENT_TESTCASE_ID` to be defined in the `develop` and `product` profiles as well. But the Materials library does NOT require `Hostname`, `Username`, `Password`.

Later, you will want to add 2 profiles in your Visual Testing project. The name of your profiles can be any. Which parameters to define here? it depends on the AUT you are going to work on. Possibly you need `Hostname` or `URL`. But you may not need `Username` and `Password` for your AUT.
