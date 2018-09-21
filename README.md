Visual Testing in Katalon Studio
======

# What is this repository?

This is a [Katalon Studio](https://www.katalon.com/) project for demonstration purpose. You can clone this
out to your PC and run it with your Katalon Studio.

This project was developed using Katalon Studio 5.7.0.

This project demonstrates how to perform a
primitive *Visual Testing* in Katalon Studio.

# Problem to solve

What do I mean by the word *Visutal Testing*? Any toolset that provides a solution to my requirements as follows, I would regard it a *Visual Testing* solution.

- Often I have to look at the pages of a Web site. Maybe I am a developer, a designer, a tester or just a fan of the site.
- I want to look at as many pages of the site as possible. Wide coverage matters.
- I want to compare the view of 2 environments; e.g. Production and Development.
- I want to take full-page screen shots as evidence
- After taking screenshots, I want to check them to find out if there are any differences in view between the two.

If the target Web site has 100 pages, it would take me more than 60 minutes to go through them. It's too tiring, too boring. I don't want to do the job everyday! Therefore I would add the final term:

- I want to automate it.

## More specific problems

In terms of coding test scripts, I found 2 problems.

1. I want to make 2 sets of screen shot files on the local disk of my PC and reuse them. I need to define a specification how the  file paths should be. A Script which writes a file and another script which reads the file, both scripts need to be aware of the path format and respect it. I would call a set of files compliant to the path format spec as `MaterialRepository`. I need a set of Java/Groovy library (jar) which implements `MaterialRepository`.
1. [org.openqa.selenium.TakesScreenshot()](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/TakesScreenshot.html) enables to take screen shot. But the image taken is not full-page; show the image of viewport only.

# Solution

As for the file path problem,
I have developed another project titled ['UsingMaterialsInKatalonStudio'](https://github.com/kazurayam/UsingMaterialsInKatalonStudio). This project demonstrates how to take multiple screenshots of web pages and store them into a well-structured file system tree. I named it as **Materials** diretory.

As for the full-page screenshot problem, I found that the library [aShot](https://github.com/yandex-qatools/ashot) solves it like a charm. I wrote a report how I utilized aShot in Katalon Studio:
- [Entire page screenshot by aShot in Katalon Studio](https://github.com/kazurayam/EntirePageScreenshotByAShotInKatalonStudio)

I was impressed that aShot provides [ImageDiff](https://github.com/yandex-qatools/ashot/blob/master/src/main/java/ru/yandex/qatools/ashot/comparison/ImageDiff.java). This utility class enabled me to check very easily how much differences there are amongst 2 images. 

# Setup

# Description the demo

- http://demoaut.katalon.com/  --- called *Production environement*
- http://demoaut-mimic.kazurayam.com/ --- called *Development environement*

## How to run the demo

## Output

| Production | Development |
|:-----------|:------------|
| File path: `<projectDir>/Materials/Main.TS1/20180920_165543/Main.Basic/CURA_Homepage.png`  ![Production](docs/images/Production_CURA_Homepage.png) | File path: ./Materials/Main.TS1/20180920_165544/Main.Basic/CURA_Homepage.png  ![Development](docs/images/Development_CURA_Homepage.png) |

File path: `<projectDir>/Materials/index.html`
![index](docs/images/Materials_index.png)

File path: `<projectDir>/Materials/ImageDiff/yyyyMMdd_hhmmss/ImageDiff/CURA_Homepage.yyyyMMdd_hhmmss_product-yyyyMMdd_hhmmss_develop.(6.30)FAILED.png`
![ImageDiff](docs/images/ImageDiff_CURA_Homepage.png)

# Related resources

1. [UsingMaterialsInKatalonStudio](https://github.com/kazurayam/UsingMaterialsInKatalonStudio)
2. [Materials](https://github.com/kazurayam/Materials)
3. [Materials API doc](https://kazurayam.github.io/Materials/)
