package fastrobot;

import java.awt.Robot;

/**
 * Quick performance test
 */
public class QuickTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Quick FastRobot Test ===");
        
        Robot awtRobot = new Robot();
        FastRobot fastRobot = new FastRobot();
        
        int iterations = 1000;
        int x = 100, y = 100;
        
        // Test mouse move speed
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awtRobot.mouseMove(x + (i % 10), y + (i % 10));
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        long fastStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            fastRobot.mouseMove(x + (i % 10), y + (i % 10));
        }
        long fastEnd = System.nanoTime();
        double fastMs = (fastEnd - fastStart) / 1_000_000.0;
        
        System.out.printf("AWT Robot: %.2fms (%.4fms per op)%n", awtMs, awtMs / iterations);
        System.out.printf("FastRobot: %.2fms (%.4fms per op)%n", fastMs, fastMs / iterations);
        System.out.printf("Speedup: %.2fx faster%n", awtMs / fastMs);
        
        // Test pixel color
        long pixelStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            fastRobot.getPixelColor(x, y);
        }
        long pixelEnd = System.nanoTime();
        double pixelMs = (pixelEnd - pixelStart) / 1_000_000.0;
        System.out.printf("Pixel color: %.4fms per pixel%n", pixelMs / 100);
        
        System.out.println("✅ FastRobot performance test complete!");
    }
}
