@echo off
setlocal EnableDelayedExpansion

echo ===========================================
echo FastRobot v2.0 - DirectX DXGI Build Script
echo ===========================================
echo.

:: Check for Java
if not defined JAVA_HOME (
    set "JAVA_HOME=C:\Program Files\Java\jdk-25"
)

if not exist "%JAVA_HOME%\include\jni.h" (
    echo ERROR: Cannot find jni.h in %JAVA_HOME%\include
    echo Please check your Java installation
    pause
    exit /b 1
)

:: Use vswhere to find Visual Studio
set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"

if not exist "%VSWHERE%" (
    echo ERROR: vswhere.exe not found!
    echo Visual Studio Installer might be missing.
    echo.
    pause
    exit /b 1
)

:: Find VS installation path
for /f "usebackq tokens=*" %%i in (`"%VSWHERE%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
    set "VS_INSTALL=%%i"
)

if not defined VS_INSTALL (
    echo ERROR: Visual Studio with C++ tools not found!
    echo.
    pause
    exit /b 1
)

echo Found Visual Studio at: %VS_INSTALL%

:: Setup VS environment
set "VCVARS=%VS_INSTALL%\VC\Auxiliary\Build\vcvars64.bat"

echo Setting up Visual Studio environment...
call "%VCVARS%"
if errorlevel 1 (
    echo ERROR: Failed to setup VS environment
    pause
    exit /b 1
)

:: Create build directory
if not exist build mkdir build

:: Compile
echo.
echo Compiling FastRobot v2.0 with DirectX DXGI support...
echo =====================================================
cl /LD /Fe:build\fastrobot.dll ^
    native\fastrobot.cpp ^
    native\DXGICapture.cpp ^
    user32.lib gdi32.lib d3d11.lib dxgi.lib ^
    /I"%JAVA_HOME%\include" ^
    /I"%JAVA_HOME%\include\win32" ^
    /EHsc /std:c++17 /O2 /W3

:: Check result
if %errorlevel% neq 0 (
    echo.
    echo =====================================================
    echo COMPILATION FAILED
    echo =====================================================
    echo Check errors above
    pause
    exit /b 1
)

:: Copy to resources
echo.
echo Copying DLL to resources...
copy build\fastrobot.dll src\main\resources\native\fastrobot.dll

:: Success
echo.
echo =====================================================
echo COMPILATION SUCCESSFUL!
echo =====================================================
echo.
echo FastRobot v2.0 DLL created with:
echo - DirectX 11 DXGI Desktop Duplication
echo - 60-240fps screen capture support
echo - Hardware acceleration enabled
echo.
echo You can now build with Maven:
echo   mvn clean package
echo.
pause
