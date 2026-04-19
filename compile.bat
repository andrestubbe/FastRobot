@echo off
:: FastRobot Native Library Builder
:: Automatically finds Visual Studio

echo ========================================
echo FastRobot Native Library Builder
echo ========================================

:: Try to find VS using vswhere.exe (most reliable)
set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
if exist "%VSWHERE%" (
    for /f "usebackq tokens=*" %%i in (`"%VSWHERE%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
        set "VS_PATH=%%i"
    )
)

:: Fallback: Check standard paths if vswhere didn't work
if not defined VS_PATH (
    if exist "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Community"
    ) else if exist "C:\Program Files\Microsoft Visual Studio\2022\Enterprise\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Enterprise"
    ) else if exist "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files\Microsoft Visual Studio\2022\Professional"
    ) else if exist "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools"
    ) else if exist "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files (x86)\Microsoft Visual Studio\2019\Community"
    ) else if exist "C:\Program Files (x86)\Microsoft Visual Studio\2019\Enterprise\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files (x86)\Microsoft Visual Studio\2019\Enterprise"
    )
)

:: Check if VS was found
if not defined VS_PATH (
    echo ERROR: Visual Studio not found!
    echo.
    echo Please install Visual Studio 2019 or 2022 with "Desktop development with C++" workload
    echo Or run this script from "Developer Command Prompt for VS"
    exit /b 1
)

echo Found Visual Studio at: %VS_PATH%

:: Try to detect JAVA_HOME if not set
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-25" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-17-hotspot" (
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17-hotspot"
    ) else if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    ) else if exist "C:\Program Files\Microsoft\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17"
    )
)

if not defined JAVA_HOME (
    echo ERROR: JAVA_HOME not set!
    echo Please set JAVA_HOME to your JDK installation path
    echo Example: set JAVA_HOME=C:\Program Files\Java\jdk-17
    exit /b 1
)

echo Using JAVA_HOME: %JAVA_HOME%

:: Setup environment
call "%VS_PATH%\VC\Auxiliary\Build\vcvars64.bat"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to setup VS environment
    exit /b 1
)

:: Create build directory
if not exist build mkdir build

:: Compile C++ source
echo.
echo Compiling native DLL...
cl.exe /LD /O2 /W3 /MD /EHsc ^
    /I "%JAVA_HOME%\include" ^
    /I "%JAVA_HOME%\include\win32" ^
    /Fo:build\ ^
    /Fe:build\fastrobot.dll ^
    native\fastrobot.cpp native\DXGICapture.cpp ^
    /link /DLL /MACHINE:X64 user32.lib gdi32.lib dxgi.lib d3d11.lib

:: Check result
if %ERRORLEVEL% == 0 (
    echo.
    echo ✅ Build successful: build\fastrobot.dll
    dir build\fastrobot.dll
) else (
    echo.
    echo ❌ Build failed
    exit /b 1
)

echo.
pause
