@echo off
echo Setting up Visual Studio environment...

REM Try to find and run vcvars64.bat
for %%f in (
    "C:\Program Files\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
    "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
    "C:\Program Files\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
    "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
) do (
    if exist %%f (
        echo Found: %%f
        call %%f
        goto :found
    )
)

echo Visual Studio environment not found. Trying Windows SDK...
call "C:\Program Files (x86)\Windows Kits\10\bin\10.0.22000.0\x64\vcvars64.bat" 2>nul

:found
echo Compiling with MSVC...
cl.exe /LD /I"C:\Program Files\Java\jdk-25\include" /I"C:\Program Files\Java\jdk-25\include\win32" native\fastrobot_FastRobot.cpp user32.lib gdi32.lib /Fe:build\fastrobot_vs.dll

echo Compilation complete
pause
