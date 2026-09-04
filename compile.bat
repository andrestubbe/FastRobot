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
    if exist "C:\Program Files\Microsoft Visual Studio\18\Community\VC\Auxiliary\Build\vcvars64.bat" (
        set "VS_PATH=C:\Program Files\Microsoft Visual Studio\18\Community"
    ) else if exist "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat" (
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
    echo Please install Visual Studio with C++ workload
    exit /b 1
)

echo Found Visual Studio at: %VS_PATH%

:: Try to detect JAVA_HOME if not set
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-21.0.12.1" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1"
    ) else if exist "C:\Program Files\Java\jdk-25" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25"
    ) else if exist "C:\Program Files\Java\latest" (
        set "JAVA_HOME=C:\Program Files\Java\latest"
    ) else if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
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
    echo ??? Build successful: build\fastrobot.dll
    dir build\fastrobot.dll
    if not exist src\main\resources\native mkdir src\main\resources\native
    copy build\fastrobot.dll src\main\resources\native\fastrobot.dll /Y >nul
    echo Copied fastrobot.dll to src\main\resources\native
) else (
    echo.
    echo ❌ Build failed
    exit /b 1
)

echo.
