package fastrobot;

import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * v2.0: High-FPS Streaming Capture Test
 * Demonstrates 60fps-240fps screen capture using DXGI Desktop Duplication API
 */
public class StreamingCaptureTest {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== FastRobot v2.0 - High-FPS Streaming Test ===\n");
        
        FastRobot robot = new FastRobot();
        
        // Get screen dimensions
        int screenWidth = robot.getScreenWidth();
        int screenHeight = robot.getScreenHeight();
        System.out.println("Screen: " + screenWidth + "x" + screenHeight);
        
        // Define capture region (1920x1080 area for performance)
        int captureWidth = Math.min(1920, screenWidth);
        int captureHeight = Math.min(1080, screenHeight);
        int captureX = (screenWidth - captureWidth) / 2;
        int captureY = (screenHeight - captureHeight) / 2;
        
        System.out.println("Capture region: " + captureX + "," + captureY + " " + captureWidth + "x" + captureHeight);
        System.out.println("\nStarting 60fps+ streaming capture...\n");
        
        // Start streaming capture
        boolean success = robot.startScreenStream(captureX, captureY, captureWidth, captureHeight);
        if (!success) {
            System.err.println("❌ Failed to start screen stream!");
            System.err.println("   Make sure you have a DirectX 11 compatible GPU");
            return;
        }
        
        System.out.println("✅ Streaming started successfully!");
        System.out.println("   Capturing at monitor refresh rate (60Hz-240Hz)\n");
        
        // Capture for 5 seconds and measure FPS
        int frameCount = 0;
        long startTime = System.nanoTime();
        long lastFPSUpdate = startTime;
        
        System.out.println("Capturing frames for 5 seconds...");
        System.out.println("Press Ctrl+C to stop early\n");
        
        while (true) {
            // Check if new frame available (non-blocking)
            if (robot.hasNewFrame()) {
                // Get the frame
                int[] pixels = robot.getNextFrame();
                if (pixels != null) {
                    frameCount++;
                    
                    // Display FPS every second
                    long currentTime = System.nanoTime();
                    if ((currentTime - lastFPSUpdate) >= 1_000_000_000L) {
                        double fps = robot.getStreamFPS();
                        System.out.printf("Frames: %d | Streaming FPS: %.1f%n", frameCount, fps);
                        lastFPSUpdate = currentTime;
                    }
                }
            }
            
            // Stop after 5 seconds
            if ((System.nanoTime() - startTime) >= 5_000_000_000L) {
                break;
            }
            
            // Small sleep to prevent CPU overload
            Thread.sleep(1);
        }
        
        // Stop streaming
        robot.stopScreenStream();
        
        // Calculate final stats
        long totalTime = System.nanoTime() - startTime;
        double averageFPS = frameCount / (totalTime / 1_000_000_000.0);
        
        System.out.println("\n=== Results ===");
        System.out.printf("Total frames captured: %d%n", frameCount);
        System.out.printf("Total time: %.2f seconds%n", totalTime / 1_000_000_000.0);
        System.out.printf("Average FPS: %.1f%n", averageFPS);
        System.out.printf("Frame time: %.2f ms%n", 1000.0 / averageFPS);
        
        if (averageFPS >= 60) {
            System.out.println("\n✅ SUCCESS! 60fps+ capture achieved!");
        } else {
            System.out.println("\n⚠️  Below 60fps - check GPU compatibility");
        }
        
        // Compare with v1.0 synchronous capture
        System.out.println("\n=== Comparison with v1.0 (BitBlt) ===");
        long syncStart = System.nanoTime();
        int syncFrames = 0;
        long syncTestDuration = 1_000_000_000L; // 1 second
        
        while ((System.nanoTime() - syncStart) < syncTestDuration) {
            robot.getScreenPixels(captureX, captureY, captureWidth, captureHeight);
            syncFrames++;
        }
        
        double syncFPS = syncFrames / 1.0;
        System.out.printf("v1.0 BitBlt FPS: %.1f%n", syncFPS);
        System.out.printf("v2.0 DXGI FPS:   %.1f%n", averageFPS);
        System.out.printf("Speedup: %.1fx%n", averageFPS / syncFPS);
        
        System.out.println("\n🚀 FastRobot v2.0 delivers ultra-high FPS screen capture!");
    }
}
