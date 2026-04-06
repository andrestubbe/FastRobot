@echo off
setlocal EnableDelayedExpansion

echo ===========================================
echo FastRobot v2.0 - DirectX DXGI Build Script
echo ===========================================
echo.

:: Check for Visual Studio
set "VS_PATH="

:: Try to find VS 2022
if exist "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat" (
    set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
    echo Found Visual Studio 2022 Community
) else if exist "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvars64.bat" (
    set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvars64.bat"
    echo Found Visual Studio 2022 Professional
) else if exist "C:\Program Files\Microsoft Visual Studio\2022\Enterprise\VC\Auxiliary\Build\vcvars64.bat" (
    set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Enterprise\VC\Auxiliary\Build\vcvars64.bat"
    echo Found Visual Studio 2022 Enterprise
) else if exist "C:\Program Files\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat" (
    set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat"
    echo Found Visual Studio 2019 Community
) else (
    echo ERROR: Visual Studio 2019 or 2022 not found!
    echo.
    echo Please install Visual Studio with C++ support:
    echo 1. Download from https://visualstudio.microsoft.com/downloads/
    echo 2. Select "Desktop development with C++"
    echo 3. Install Windows 10/11 SDK
    echo.
    pause
    exit /b 1
)

:: Setup VS environment
echo Setting up Visual Studio environment...
call "%VS_PATH%"
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
