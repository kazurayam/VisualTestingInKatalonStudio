<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Revision History](#revision-history)
  - [Changes from 1.8.0 to 1.10.0](#changes-from-180-to-1100)
  - [Changes from 1.6.0 to 1.8.0](#changes-from-160-to-180)
  - [Changes from 1.4.1 to 1.6.0](#changes-from-141-to-160)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Revision History

## Changes from 1.10.0 to 1.10.1

1. upgrade Materials from version 0.68.0 to 0.70.0
2. Keywords are changed to be in sync with Materials-0.70.0.jar
3. Difference of the sets of screenshot images are managed. An orphan image is accompanied with a generated image which shows 'file xxx is not found'.

## Changes from 1.8.0 to 1.10.0

1. Chronological approache of screenshot-comparison testing, namely "Chronos", was supported in addition to the 2 hostname comparison, namely "Twins".
2. `Materials/index.html` now presents 3 screenshot images (Back > diff > Forth) in Carousel format.
3. [Gradle Plugin com.github.kazurayam.visualtestinginks](https://github.com/kazurayam/visualtestinginks-gradle-plugin) was introduced. This Gradle Plugin makes resources of VisualTestingInKatalonStudio portable to other projects.


## Changes from 1.6.0 to 1.8.0

New feature of "ignoring web elements in the page" is supported. In order to accomplish it, the following changes were made.

You can find the full difference between the two versions at https://github.com/kazurayam/VisualTestingInKatalonStudio/compare/1.6.0...1.8.0

1. A custom keyword revised to support "ignoring web elements"
  - [`Keywords/com/kazurayam/ksbackyard/ScreenshotDriver`](../Keywords/com/kazurayam/ksbackyard/ScreenshotDriver.groovy)
2. New class was added in the `Keywords` directory. This class is required by the revised `ScreenshotDriver`.
  - [`Keywords/com/kazurayam/ksbackyard/TestObjectSupport`](../Keywords/com/kazurayam/ksbackyard/TestObjectSupport.groovy)
3. Some scripts as test case and test suites are added in oder to give an example of "ignoring web elements".
  - [Test Cases/step10_TC](../Scripts/StepByStep/step10_TC/Script1549952063213.groovy)
  - [Test Cases/step10_ImageDiff](../Scripts/StepByStep/step10_ImageDiff/Script1549948805708.groovy)
  - `Test Suites/StepByStep/step10_ImageDiff`
  - `Test Suites/StepByStep/step10_TS`
  - `Test Suites/StepByStep/Step10_TSC`
4. Many of existing scripts were renamed: `Test Cases/StepByStep/*` and `Test Suites/Test Suites/*` to make them well ordered.
5. The [Step by step document](./Step_by_step_instruction.md) was revised to cover a new section "step10 : ignoring particular web elements".
6. `Drivers/Materials-x.x.x.jar` is upgraded to 0.26.0. This upgrade is just house keeping; not required for the "ignoring web elements" feature.


## Changes from 1.4.1 to 1.6.0

https://github.com/kazurayam/VisualTestingInKatalonStudio/compare/1.4.1...1.6.0

1. add the jar of the [`ImageDifference`](https://github.com/kazurayam/ImageDifference) project into the `Driver` directory.
2. Changed the source of custom keywords in the `Keywords/com/kazurayam/ksbackward` directory so that they work with the ImageDifference product.
  - [`Assert`](../Keywords/com/kazurayam/ksbackyard/Assert.groovy)
  - [`ImageCollectionDiffer`](../Keywords/com/kazurayam/ksbackyard/ImageCollectionDiffer.groovy)
  - [`ScreenshotDriver`](../Keywords/com/kazurayam/ksbackyard/ScreenshotDriver.groovy)

These changes are backward-compatible. All test cases in the version 1.4.1 works in the version 1.6.0 without modifications.
