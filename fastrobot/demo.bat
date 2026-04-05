@echo off
echo === UltraFastRobot Demo ===
echo.
echo This demo shows how easy UltraFastRobot is to use with Maven!
echo.

REM Set up classpath with current JAR (simulating Maven)
set CLASSPATH=build

echo 1. Testing UltraFastRobot creation...
java -cp "%CLASSPATH%" -Djava.library.path=build fastrobot.UltraTest

echo.
echo 2. Performance comparison...
java -cp "%CLASSPATH%" -Djava.library.path=build fastrobot.QuickTest

echo.
echo 3. Full benchmark (may take a moment)...
java -cp "%CLASSPATH%" -Djava.library.path=build fastrobot.UltraBenchmark

echo.
echo === Demo Complete ===
echo.
echo To use UltraFastRobot in your project:
echo 1. Add Maven dependency to pom.xml
echo 2. Import: import fastrobot.UltraFastRobot;
echo 3. Use: UltraFastRobot robot = new UltraFastRobot();
echo.
echo That's it! Ultra-fast performance with zero configuration! 🚀
echo.
pause
