@echo off
mkdir build 2>nul
echo Compiling with Visual Studio C++...
cl /LD /Fe:build\fastrobot.dll native\fastrobot.cpp user32.lib gdi32.lib /I"C:\Program Files\Java\jdk-25\include" /I"C:\Program Files\Java\jdk-25\include\win32"
if exist build\fastrobot.dll (
    copy build\fastrobot.dll src\main\resources\native\fastrobot.dll
    echo Compilation complete - DLL copied to resources
) else (
    echo Compilation failed
)
