@echo off
setlocal

REM Read SONAR_TOKEN from .env file
for /f "tokens=1,* delims==" %%a in (.env) do (
    if "%%a"=="SONAR_TOKEN" set SONAR_TOKEN=%%b
)

REM Check if SONAR_TOKEN was found
if "%SONAR_TOKEN%"=="" (
    echo Error: SONAR_TOKEN not found in .env file
    exit /b 1
)

REM Run Maven with the SONAR_TOKEN environment variable
echo Using SONAR_TOKEN from .env file
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.token=%SONAR_TOKEN%

endlocal