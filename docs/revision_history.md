# Revision History

## Changes from 1.6.0 to 1.8.0

New feature of "ignoring web elements in the page" is supported. In order to accomplish it, the following changes were made.

You can find the full difference between the two versions at https://github.com/kazurayam/VisualTestingInKatalonStudio/compare/1.6.0...1.8.0

1. A custom keyword revised to support "ignoring web elements"
  - [`Keywords/com/kazurayam/ksbackyard/ScreenshotDriver`](../Keywords/com/kazurayam/ksbackyard/ScreenshotDriver.groovy)
2. New class was added in the `Keywords` directory. This class is required by the revised `ScreenshotDriver`.
  - [`Keywords/com/kazurayam/ksbackyard/TestObjectSupport`](../Keywords/com/kazurayam/ksbackyard/TestObjectSupport.groovy)
3. Some scripts as test case and test suites are added in oder to give an example of "ignoring web elements".
  - [Test Cases/step10_TC](../ScriptsStepByStep/step10_TC/Script1549952063213.groovy)
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
