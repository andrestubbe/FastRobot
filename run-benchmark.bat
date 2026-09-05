@echo off
setlocal
chcp 65001 > nul
cd /d "%~dp0"
set "MAVEN_OPTS=--enable-native-access=ALL-UNNAMED -Dorg.slf4j.simpleLogger.defaultLogLevel=warn"

echo ===================================================
echo  Building FastRobot ^& JMH Benchmarks Uber-Jar
echo ===================================================

call mvn -q clean install -DskipTests 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] FastRobot install failed!
    pause
    exit /b %ERRORLEVEL%
)

cd examples\Benchmark
call mvn -q clean package 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Benchmark packaging failed!
    pause
    exit /b %ERRORLEVEL%
)

echo ===================================================
echo  Running JMH Benchmarks (Throughput: ops/ms)
echo ===================================================
java --enable-native-access=ALL-UNNAMED -jar target\benchmarks.jar -f 1 -wi 2 -i 3 -tu ms -bm thrpt
pause
