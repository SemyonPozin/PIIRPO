@echo off
setlocal EnableExtensions

echo Installing the built application into the local Maven repository
call mvn -B jar:jar install:install
if errorlevel 1 exit /b 1

echo Reading artifactId and version from pom.xml
call mvn -q -DforceStdout help:evaluate -Dexpression=project.artifactId > "%TEMP%\laba2-artifactId.txt"
if errorlevel 1 exit /b 1
call mvn -q -DforceStdout help:evaluate -Dexpression=project.version > "%TEMP%\laba2-version.txt"
if errorlevel 1 exit /b 1

set /p NAME=<"%TEMP%\laba2-artifactId.txt"
set /p VERSION=<"%TEMP%\laba2-version.txt"

echo Running %NAME%-%VERSION% and then exiting the console menu
REM The application reads a login and a menu choice from stdin.
REM "0" closes the menu so the Jenkins stage does not wait for input.
(echo ci-user& echo 0) | java -cp "target\%NAME%-%VERSION%.jar" Main
exit /b %ERRORLEVEL%
