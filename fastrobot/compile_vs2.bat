@echo off
echo Setting up Visual Studio environment...
call "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\Common7\Tools\VsDevCmd.bat" -arch=x64

echo Compiling with MSVC (64-bit)...
cl.exe /LD /I"C:\Program Files\Java\jdk-25\include" /I"C:\Program Files\Java\jdk-25\include\win32" native\fastrobot_FastRobot.cpp user32.lib gdi32.lib /Fe:build\fastrobot_vs64.dll

echo Compilation complete
pause
