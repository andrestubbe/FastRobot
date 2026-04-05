package fastrobot;

import java.awt.Robot;

/**
 * Quick performance test
 */
public class QuickTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Quick UltraFastRobot Test ===");
        
        Robot awtRobot = new Robot();
        UltraFastRobot ultraRobot = new UltraFastRobot();
        
        int iterations = 1000;
        int x = 100, y = 100;
        
        // Test mouse move speed
        long awtStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            awtRobot.mouseMove(x + (i % 10), y + (i % 10));
        }
        long awtEnd = System.nanoTime();
        double awtMs = (awtEnd - awtStart) / 1_000_000.0;
        
        long ultraStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            ultraRobot.mouseMoveInstant(x + (i % 10), y + (i % 10));
        }
        long ultraEnd = System.nanoTime();
        double ultraMs = (ultraEnd - ultraStart) / 1_000_000.0;
        
        System.out.printf("AWT Robot: %.2fms (%.4fms per op)%n", awtMs, awtMs / iterations);
        System.out.printf("UltraFastRobot: %.2fms (%.4fms per op)%n", ultraMs, ultraMs / iterations);
        System.out.printf("Speedup: %.2fx faster%n", awtMs / ultraMs);
        
        // Test pixel color
        long pixelStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            ultraRobot.getPixelColorInstant(x, y);
        }
        long pixelEnd = System.nanoTime();
        double pixelMs = (pixelEnd - pixelStart) / 1_000_000.0;
        System.out.printf("Pixel color: %.4fms per pixel%n", pixelMs / 100);
        
        System.out.println("✅ UltraFastRobot performance test complete!");
    }
}
