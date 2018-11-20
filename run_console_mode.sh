#!/bin/bash

echo Environment variable KATALONSTUDIO_HOME=$KATALONSTUDIO_HOME

PROJECT_DIR=$(pwd)
echo PROJECT_DIR="$PROJECT_DIR"

cd "$KATALONSTUDIO_HOME"
./katalon -noSplash -runMode=console -summaryReport -projectPath="$PROJECT_DIR" -testSuiteCollectionPath="Test Suites/Main/Execute_headless" -reportFolder="Reports" -reportFileName=console_mode
exitCode=$?
cd "$PROJECT_DIR"

echo $exitCode
exit $exitCode
