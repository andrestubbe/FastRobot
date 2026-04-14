package fastrobot;

import fastcore.FastCore;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Benchmark comparing java.awt.Robot vs FastRobot.
 * Run from the project root after compiling.
 *
 * Compile: javac -d build src/fastrobot/*.java
 * Run: java -cp build -Djava.library.path=build fastrobot.Benchmark
 */
public class Benchmark {
    
    private static final int WARMUP_ITERATIONS = 100;
    private static final int BENCHMARK_ITERATIONS = 1000;
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== FastRobot Benchmark ===\n");
        
        // Load native library via FastCore
        FastCore.loadLibrary("fastrobot");
        
        Robot awtRobot = new Robot();
        FastRobot fastRobot = new FastRobot();
        
        // Get screen dimensions for testing
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        
        System.out.println("Screen: " + screenSize.width + "x" + screenSize.height);
        System.out.println("Warmup: " + WARMUP_ITERATIONS + " iterations");
        System.out.println("Benchmark: " + BENCHMARK_ITERATIONS + " iterations\n");
        
        // Warmup
        System.out.print("Warming up... ");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            awtRobot.mouseMove(centerX, centerY);
            fastRobot.mouseMove(centerX, centerY);
        }
        System.out.println("done.\n");
        
        // Run benchmarks
        benchmarkMouseMove(awtRobot, fastRobot, centerX, centerY);
        benchmarkPixelColor(awtRobot, fastRobot, centerX, centerY);
        benchmarkScreenCapture(awtRobot, fastRobot, screenSize);
        benchmarkKeyInput(awtRobot, fastRobot);
        benchmarkMouseClick(awtRobot, fastRobot, centerX, centerY);
        
        System.out.println("\n=== Benchmark Complete ===");
    }
    
    private static void benchmarkMouseMove(Robot awt, FastRobot fast, int x, int y) {
        System.out.println("--- Mouse Move ---");
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            awt.mouseMove(x + (i % 100), y + (i % 100));
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // FastRobot
        long fastStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            fast.mouseMove(x + (i % 100), y + (i % 100));
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        printResults("mouseMove", awtMs, fastMs, BENCHMARK_ITERATIONS);
    }
    
    private static void benchmarkPixelColor(Robot awt, FastRobot fast, int x, int y) {
        System.out.println("\n--- Get Pixel Color ---");
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            awt.getPixelColor(x, y);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // FastRobot
        long fastStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            fast.getPixelColor(x, y);
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        printResults("getPixelColor", awtMs, fastMs, BENCHMARK_ITERATIONS);
    }
    
    private static void benchmarkScreenCapture(Robot awt, FastRobot fast, Dimension screen) {
        System.out.println("\n--- Screen Capture ---");
        
        Rectangle rect = new Rectangle(screen);
        int iterations = 100; // Fewer iterations for screen capture
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awt.createScreenCapture(rect);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // FastRobot
        long fastStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            fast.createScreenCapture(rect);
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        printResults("createScreenCapture", awtMs, fastMs, iterations);
    }
    
    private static void benchmarkKeyInput(Robot awt, FastRobot fast) throws Exception {
        System.out.println("\n--- Key Input (press/release) ---");
        
        // Use a harmless key for testing
        int keyCode = KeyEvent.VK_SHIFT;
        int iterations = 500;
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awt.keyPress(keyCode);
            awt.keyRelease(keyCode);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // FastRobot
        long fastStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            fast.keyPress(keyCode);
            fast.keyRelease(keyCode);
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        printResults("keyPress/Release", awtMs, fastMs, iterations);
    }
    
    private static void benchmarkMouseClick(Robot awt, FastRobot fast, int x, int y) throws Exception {
        System.out.println("\n--- Mouse Click (press/release) ---");
        
        int iterations = 500;
        int button = InputEvent.BUTTON1_DOWN_MASK;
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awt.mousePress(button);
            awt.mouseRelease(button);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // FastRobot
        long fastStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            fast.mousePress(button);
            fast.mouseRelease(button);
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        printResults("mousePress/Release", awtMs, fastMs, iterations);
    }
    
    private static void printResults(String operation, double awtMs, double fastMs, int iterations) {
        double awtPerOp = awtMs / iterations;
        double fastPerOp = fastMs / iterations;
        double speedup = awtMs / fastMs;
        
        System.out.printf("  %-20s %10s %10s%n", "", "AWT Robot", "FastRobot");
        System.out.printf("  %-20s %10.2fms %10.2fms%n", "Total", awtMs, fastMs);
        System.out.printf("  %-20s %10.4fms %10.4fms%n", "Per op", awtPerOp, fastPerOp);
        System.out.printf("  %-20s %10s %10s%n", "Ops/sec", 
            String.format("%.0f", 1000.0 / awtPerOp),
            String.format("%.0f", 1000.0 / fastPerOp));
        System.out.printf("  Speedup: %.2fx faster%n", speedup);
    }
}