@echo off
mkdir build 2>nul
echo Compiling FastRobot v2.0 with DirectX DXGI support...
echo.
echo Requirements: Visual Studio 2019/2022 with Windows SDK
echo.
cl /LD /Fe:build\fastrobot.dll ^
    native\fastrobot.cpp ^
    native\DXGICapture.cpp ^
    user32.lib gdi32.lib d3d11.lib dxgi.lib ^
    /I"C:\Program Files\Java\jdk-25\include" ^
    /I"C:\Program Files\Java\jdk-25\include\win32" ^
    /EHsc /std:c++17
if exist build\fastrobot.dll (
    copy build\fastrobot.dll src\main\resources\native\fastrobot.dll
    echo.
    echo Compilation successful - fastrobot.dll v2.0 created with 60fps+ capture support!
) else (
    echo.
    echo Compilation failed - check Visual Studio installation
)
