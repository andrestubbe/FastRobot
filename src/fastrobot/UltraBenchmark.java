package fastrobot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;

/**
 * Ultra-fast benchmark comparing java.awt.Robot vs UltraFastRobot.
 * Tests maximum performance optimizations.
 */
public class UltraBenchmark {
    
    private static final int WARMUP_ITERATIONS = 100;
    private static final int BENCHMARK_ITERATIONS = 10000; // Increased for ultra-fast tests
    private static final int SCREEN_CAPTURE_ITERATIONS = 1000; // More realistic for 60fps testing
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== UltraFastRobot Benchmark ===\n");
        
        Robot awtRobot = new Robot();
        UltraFastRobot ultraRobot = new UltraFastRobot();
        
        // Get screen dimensions for testing
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        
        System.out.println("Screen: " + screenSize.width + "x" + screenSize.height);
        System.out.println("Warmup: " + WARMUP_ITERATIONS + " iterations");
        System.out.println("Mouse/Keyboard benchmark: " + BENCHMARK_ITERATIONS + " iterations");
        System.out.println("Screen capture benchmark: " + SCREEN_CAPTURE_ITERATIONS + " iterations\n");
        
        // Warmup
        System.out.print("Warming up... ");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            awtRobot.mouseMove(centerX, centerY);
            ultraRobot.mouseMoveInstant(centerX, centerY);
        }
        System.out.println("done.\n");
        
        // Run ultra-fast benchmarks
        benchmarkMouseMove(awtRobot, ultraRobot, centerX, centerY);
        benchmarkPixelColor(awtRobot, ultraRobot, centerX, centerY);
        benchmarkPixelColorBatch(ultraRobot, centerX, centerY);
        benchmarkScreenCapture(awtRobot, ultraRobot, screenSize);
        benchmarkScreenCaptureStreaming(ultraRobot, screenSize);
        benchmarkKeyInput(awtRobot, ultraRobot);
        benchmarkMouseClick(awtRobot, ultraRobot, centerX, centerY);
        benchmarkBatchMouse(ultraRobot, centerX, centerY);
        
        System.out.println("\n=== UltraFastRobot Benchmark Complete ===");
    }
    
    private static void benchmarkMouseMove(Robot awt, UltraFastRobot ultra, int x, int y) {
        System.out.println("--- Ultra-Fast Mouse Move ---");
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            awt.mouseMove(x + (i % 100), y + (i % 100));
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // UltraFastRobot
        long ultraStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            ultra.mouseMoveInstant(x + (i % 100), y + (i % 100));
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        printResults("mouseMove", awtMs, ultraMs, BENCHMARK_ITERATIONS);
    }
    
    private static void benchmarkPixelColor(Robot awt, UltraFastRobot ultra, int x, int y) {
        System.out.println("\n--- Ultra-Fast Pixel Color ---");
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            awt.getPixelColor(x, y);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // UltraFastRobot
        long ultraStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            ultra.getPixelColorInstant(x, y);
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        printResults("getPixelColor", awtMs, ultraMs, BENCHMARK_ITERATIONS);
    }
    
    private static void benchmarkPixelColorBatch(UltraFastRobot ultra, int x, int y) {
        System.out.println("\n--- Ultra-Fast Batch Pixel Color (1000 pixels at once) ---");
        
        // Prepare batch coordinates
        int[] coords = new int[2000]; // 1000 pixels * 2 coords
        for (int i = 0; i < 1000; i++) {
            coords[i * 2] = x + (i % 10);
            coords[i * 2 + 1] = y + (i % 10);
        }
        
        int iterations = 100; // 100 batches of 1000 pixels = 100,000 pixels total
        
        long ultraStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            ultra.getPixelColorsBatch(coords);
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        double ultraPerOp = ultraMs / iterations;
        double pixelsPerSec = 1000.0 / ultraPerOp * 1000; // 1000 pixels per batch
        
        System.out.printf("  %-25s %12.4fms %12.0f px/sec%n", "Batch 1000 pixels", ultraPerOp, pixelsPerSec);
    }
    
    private static void benchmarkScreenCapture(Robot awt, UltraFastRobot ultra, Dimension screen) {
        System.out.println("\n--- Ultra-Fast Screen Capture ---");
        
        Rectangle rect = new Rectangle(100, 100, 800, 600); // Smaller region for faster testing
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < SCREEN_CAPTURE_ITERATIONS; i++) {
            awt.createScreenCapture(rect);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // UltraFastRobot
        long ultraStart = System.nanoTime();
        for (int i = 0; i < SCREEN_CAPTURE_ITERATIONS; i++) {
            ultra.createScreenCaptureUltra(rect);
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        printResults("createScreenCapture", awtMs, ultraMs, SCREEN_CAPTURE_ITERATIONS);
        
        // Check if we achieved 60fps
        double ultraPerOp = ultraMs / SCREEN_CAPTURE_ITERATIONS;
        double fps = 1000.0 / ultraPerOp;
        System.out.printf("  %-25s %12.1f fps%n", "Achieved FPS", fps);
        
        if (fps >= 60) {
            System.out.println("  🎯 60fps+ ACHIEVED!");
        } else {
            System.out.println("  ⚠  60fps not yet achieved");
        }
    }
    
    private static void benchmarkScreenCaptureStreaming(UltraFastRobot ultra, Dimension screen) {
        System.out.println("\n--- Ultra-Fast Screen Streaming (Direct Buffer) ---");
        
        Rectangle region = new Rectangle(100, 100, 800, 600);
        
        long ultraStart = System.nanoTime();
        for (int i = 0; i < SCREEN_CAPTURE_ITERATIONS; i++) {
            ByteBuffer buffer = ultra.streamScreenCapture(region);
            // Simulate processing the buffer
            buffer.rewind();
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        double ultraPerOp = ultraMs / SCREEN_CAPTURE_ITERATIONS;
        double fps = 1000.0 / ultraPerOp;
        
        System.out.printf("  %-25s %12.4fms %12.1f fps%n", "Direct Buffer Streaming", ultraPerOp, fps);
        
        if (fps >= 60) {
            System.out.println("  🚀 60fps+ STREAMING ACHIEVED!");
        }
    }
    
    private static void benchmarkKeyInput(Robot awt, UltraFastRobot ultra) throws Exception {
        System.out.println("\n--- Ultra-Fast Key Input ---");
        
        int keyCode = KeyEvent.VK_SHIFT;
        int iterations = 5000;
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awt.keyPress(keyCode);
            awt.keyRelease(keyCode);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // UltraFastRobot
        long ultraStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            ultra.keyPressInstant(keyCode);
            ultra.keyReleaseInstant(keyCode);
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        printResults("keyPress/Release", awtMs, ultraMs, iterations);
    }
    
    private static void benchmarkMouseClick(Robot awt, UltraFastRobot ultra, int x, int y) throws Exception {
        System.out.println("\n--- Ultra-Fast Mouse Click ---");
        
        int iterations = 5000;
        int button = InputEvent.BUTTON1_DOWN_MASK;
        
        // AWT Robot
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awt.mousePress(button);
            awt.mouseRelease(button);
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        // UltraFastRobot
        long ultraStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            ultra.mousePressInstant(button);
            ultra.mouseReleaseInstant(button);
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        printResults("mousePress/Release", awtMs, ultraMs, iterations);
    }
    
    private static void benchmarkBatchMouse(UltraFastRobot ultra, int x, int y) {
        System.out.println("\n--- Ultra-Fast Batch Mouse Operations ---");
        
        // Prepare batch operations: [type, x, y, buttons]
        int[][] operations = new int[1000][4];
        for (int i = 0; i < 1000; i++) {
            operations[i][0] = 0; // Move
            operations[i][1] = x + (i % 50);
            operations[i][2] = y + (i % 50);
            operations[i][3] = 0;
        }
        
        long ultraStart = System.nanoTime();
        ultra.batchMouseOperations(operations);
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        double ultraPerOp = ultraMs / 1000;
        double opsPerSec = 1000.0 / ultraPerOp * 1000;
        
        System.out.printf("  %-25s %12.4fms %12.0f ops/sec%n", "Batch 1000 operations", ultraPerOp, opsPerSec);
    }
    
    private static void printResults(String operation, double awtMs, double ultraMs, int iterations) {
        double awtPerOp = awtMs / iterations;
        double ultraPerOp = ultraMs / iterations;
        double speedup = awtMs / ultraMs;
        
        System.out.printf("  %-20s %12s %12s%n", "", "AWT Robot", "UltraFast");
        System.out.printf("  %-20s %12.2fms %12.2fms%n", "Total", awtMs, ultraMs);
        System.out.printf("  %-20s %12.4fms %12.4fms%n", "Per op", awtPerOp, ultraPerOp);
        System.out.printf("  %-20s %12s %12s%n", "Ops/sec", 
            String.format("%.0f", 1000.0 / awtPerOp),
            String.format("%.0f", 1000.0 / ultraPerOp));
        System.out.printf("  Speedup: %.2fx faster%n", speedup);
    }
}
