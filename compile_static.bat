@echo off
C:\msys64\mingw64\bin\g++.exe -shared -static-libgcc -static-libstdc++ -o build\fastrobot_static.dll -I "C:\Program Files\Java\jdk-25\include" -I "C:\Program Files\Java\jdk-25\include\win32" native\fastrobot_FastRobot.cpp -lgdi32 -luser32
echo Static compilation complete
