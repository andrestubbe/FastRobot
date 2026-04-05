@echo off
echo Setting up Visual Studio environment for UltraFastRobot...
call "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\Common7\Tools\VsDevCmd.bat" -arch=x64

echo Compiling UltraFastRobot with maximum optimizations...
cl.exe /LD /O2 /Oi /Gy /GL /arch:AVX2 ^
/I"C:\Program Files\Java\jdk-25\include" ^
/I"C:\Program Files\Java\jdk-25\include\win32" ^
native\ultrafastrobot_UltraFastRobot.cpp ^
user32.lib gdi32.lib ^
/Fe:build\fastrobotultra.dll

echo UltraFastRobot compilation complete!
pause
