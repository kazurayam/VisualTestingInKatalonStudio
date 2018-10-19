Step by step instruction how to create your own visual testing project
===========

In this note, I will describe how you create a new Katalon Studio project and set it up to carry out *Visual Testing* as I did.

I will use a term '*the demo project*' for short for the  [VisualTestingInKatalonStudio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project.

#### (1) create a new Katalon Studio project

Do it as usual.

#### (2) resolving the external dependencies

You need to include 2 external jar files into your new project.

1. `aShot` : WebDriver Screenshot utility. Its jar is public at the [MavenCentral repository](https://mvnrepository.com/artifact/ru.yandex.qatools.ashot/ashot/1.5.4). Also you can reuse the `ashot-1.5.4.jar` contained in the demo projct's `./Drivers` folder.

2. `Materials` : Utility that manages the `<project dir>/Materials` folder where you can store any files created by test cases in a well structured path format. Another project  [UsingMaterialsInKatalonStudio](https://github.com/kazurayam/UsingMaterialsInKatalonStudio) describes how to use the `Materials` library in detail. The Materials-0.22.0.jar file is public at the [releases](https://github.com/kazurayam/Materials/releases) page. Also you can reuse the jar file contained in the demo project's `./Drivers` folder.

Please refer to the Katalon Documentation [*How to import external libarary into your automation project*](https://docs.katalon.com/katalon-studio/tutorials/import_java_library.html)





----

create a new Katalon Studio for you
import the jar file of aShot into the project. see the Katalon document How to import external library into your automation project . The jar of aShot is here. Or you can reuse the ashot-x.x.x.jar bundled in the demo project's ./Drivers folder.
import the jar file of Materials. The jar file is donwloadable at Releases page. Or you can reuse the Materials-x.x.x.jar bundled in the demo project's ./Drivers folder.
create 2 custom keywords: com.kazurayam.ksbackyard.Assert and com.kazurayam.ksbackyard.ScreenshotDriver. Copy the Groovy source from the demo project into your replication. You do not have to modify the source of 2 keywords. Keywords
create Test Listeners/TL. Copy the Groovy source from the demo project into your replication. You do not have to modify the source.
modify the default Profile and add 2 GlobalVariables: MATERIAL_REPOSITORY of type Null, CURRENT_TESTCASE_ID of type String. No inital value required. The Test Listener TL will resolve appropriate values runtime.GlobalVariables_default
Finally you need to develop test cases and test suites. Create a mimic of Test Cases/verify-slideshow-example and modify it as you would like. I have written as much comments in the Groovy source. Read them and find how to customize to meet your requirements.
