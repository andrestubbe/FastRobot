@echo off
:: Native compilation script for JNI projects
:: Modify paths and library name as needed

echo ========================================
echo FastXXX Native Library Builder
echo ========================================

:: Configuration
set LIB_NAME=fastXXX
set MSVC_PATH=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build

:: Check for MSVC
if not exist "%MSVC_PATH%\vcvars64.bat" (
    echo ERROR: Visual Studio 2022 not found at expected path
    echo Please modify MSVC_PATH in this script
    exit /b 1
)

:: Setup environment
call "%MSVC_PATH%\vcvars64.bat"

:: Create build directory
if not exist build mkdir build

:: Compile C++ source (uncomment when you have native code)
:: cl.exe /O2 /W3 /MD /EHsc ^
::    /I "%JAVA_HOME%\include" ^
::    /I "%JAVA_HOME%\include\win32" ^
::    /I native\include ^
::    /Fo:build\ ^
::    /Fe:build\%LIB_NAME%.dll ^
::    native\src\*.cpp ^
::    /link /DLL /MACHINE:X64

:: Check result
if %ERRORLEVEL% == 0 (
    echo.
    echo ✅ Build successful: build\%LIB_NAME%.dll
) else (
    echo.
    echo ❌ Build failed
    exit /b 1
)

echo.
pause
