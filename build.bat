@echo off
echo Building UltraFastRobot with Maven...

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven not found. Installing Maven Wrapper...
    goto :install_wrapper
)

goto :build

:install_wrapper
echo Downloading Maven Wrapper...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile 'maven.zip'"
powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath '.'"
set MAVEN_HOME=%cd%\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

:build
echo Cleaning previous build...
call mvn clean

echo Compiling Java sources...
call mvn compile

echo Creating JAR with native libraries...
call mvn package

echo.
echo === Build Complete ===
echo JAR created in: target\ultrafastrobot-1.0.0.jar
echo Native libraries included in: target\classes\native\
echo.
echo To use: java -cp target\ultrafastrobot-1.0.0.jar your.package.MainClass
echo.
pause
