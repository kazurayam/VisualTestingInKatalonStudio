@echo off

echo Environment variable KATALONSTUDIO_HOME="%KATALONSTUDIO_HOME%"

set DIR=%~dp0
set PROJECT_DIR=%DIR:~0,-1%
echo PROJECT_DIR=%PROJECT_DIR%

cd "%KATALONSTUDIO_HOME%"

.\katalon.exe -noSplash -runMode=console -summaryReport -projectPath="%PROJECT_DIR%" -testSuiteCollectionPath="Test Suites\Main\Execute_headless" --config -proxyOption=MANUAL_CONFIG -proxy.server.type=HTTP -proxy.server.address="172.24.2.10" -proxy.server.port="8080"
set exitCode=%ERRORLEVEL%

cd /d "%PROJECT_DIR%"

echo %exitCode%
exit /B %exitCode%
