# Revision History

## Changes from 1.4.1 to 1.6.0

1. add the jar of the [`ImageDifference`](https://github.com/kazurayam/ImageDifference) project into the `Driver` directory.
2. Changed the source of custom keywords in the `Keywords/com/kazurayam/ksbackward` directory so that they work with the ImageDifference product.
  - [`Assert`](../Keywords/com/kazurayam/ksbackyard/Assert.groovy)
  - [`ImageCollectionDiffer`](../Keywords/com/kazurayam/ksbackyard/ImageCollectionDiffer.groovy)
  - [`ScreenshotDriver`](../Keywords/com/kazurayam/ksbackyard/ScreenshotDriver.groovy)

These changes are backward-compatible. All test cases in the version 1.4.1 works in the version 1.6.0 without modifications.
