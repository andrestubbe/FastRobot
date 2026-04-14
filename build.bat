@echo off
echo Building FastTheme JNI Bridge...

REM Create directories
if not exist target\classes mkdir target\classes
if not exist target\native mkdir target\native

REM Compile Java classes
echo Compiling Java classes...
javac -d target\classes src\main\java\*.java

REM Generate JNI header (already done, but including for completeness)
echo Generating JNI header...
javac -h src\main\cpp -d target\classes src\main\java\FastTheme.java

REM Compile C++ to DLL
echo Compiling native library...
cl /LD /EHsc /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" ^
   /Fe:target\native\fasttheme.dll ^
   src\main\cpp\FastTheme.cpp ^
   user32.lib gdi32.lib /link /DLL

REM Copy DLL to resources
if not exist target\classes\native mkdir target\classes\native
copy target\native\fasttheme.dll target\classes\native\

REM Create manifest file for fat jar
echo Manifest-Version: 1.0 > target\manifest.txt
echo Main-Class: Demo >> target\manifest.txt

REM Create fat jar with DLL included
echo Creating fat jar...
cd target\classes
jar cfm ..\FastTheme-Demo.jar ..\manifest.txt *.class native\*
cd ..\..

echo Build complete!
echo Run with: java -jar target\FastTheme-Demo.jar
