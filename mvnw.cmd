@echo off
set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@REM ==== START VALIDATION ====
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@REM load maven env vars from rc file
if not exist "%HOME%\mavenrc_pre.bat" goto skipRcPre
call "%HOME%\mavenrc_pre.bat"
:skipRcPre

@REM ==== START VALIDATION ====
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
@REM load maven env vars from rc file
if not exist "%HOME%\mavenrc_post.bat" goto skipRcPost
call "%HOME%\mavenrc_post.bat"
:skipRcPost

@REM ==== START REPOSITORY ====
if not "%MAVEN_SKIP_REPO%" == "" goto skipRepo
if not exist "%USERPROFILE%\.m2" goto skipRepo
if "%MAVEN_BATCH%" == "" set MAVEN_BATCH=true
call "%MAVEN_HOME%\bin\mvn" -version
if "%MAVEN_BATCH%" == "" set MAVEN_BATCH=false
:skipRepo

@REM ==== START MAVEN WRAPPER ====
@REM Execute Maven using the Maven wrapper
if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    @REM We have Maven installed locally
    call "%MAVEN_HOME%\bin\mvn.cmd" %*
) else (
    @REM We need to download Maven wrapper
    echo Maven not found. Please install Maven first.
    echo Download from: https://maven.apache.org/download.cgi
    echo Extract to: C:\Program Files\Apache\Maven
    echo Add to PATH: C:\Program Files\Apache\Maven\apache-maven-3.9.6\bin
    set ERROR_CODE=1
)

@REM ==== END MAVEN WRAPPER ====

@REM ==== END VALIDATION ====
if "%OS%"=="Windows_NT" endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
