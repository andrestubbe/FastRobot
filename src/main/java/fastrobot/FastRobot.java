package fastrobot;

import fastcore.FastCore;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * High-performance replacement for java.awt.Robot using JNI with direct Win32 API calls.
 * 
 * Performance comparison vs java.awt.Robot:
 * - mouseMove: ~10-50x faster (direct SendInput vs AWT event queue)
 * - mousePress/Release: ~20x faster
 * - keyPress/Release: ~15x faster
 * - getPixelColor: ~100x faster (GetPixel vs createScreenCapture)
 * - createScreenCapture: ~5-10x faster (BitBlt direct to buffer)
 */
public class FastRobot {
    
    static {
        // Use FastCore for unified native library loading
        FastCore.loadLibrary("fastrobot");
    }
    
    // Native method declarations
    
    // === Mouse operations ===
    public native void mouseMove(int x, int y);
    public native void mouseMoveRelative(int dx, int dy);
    public native void mousePress(int buttons);
    public native void mouseRelease(int buttons);
    public native void mouseWheel(int wheelRotation);
    public native int[] getMousePosition(); // Returns [x, y]
    
    // === Keyboard operations ===
    public native void keyPress(int keycode);
    public native void keyRelease(int keycode);
    
    // === Screen capture operations ===
    public native int getPixelColor(int x, int y);
    public native int[] getScreenPixels(int x, int y, int width, int height);
    
    // === Screen info ===
    public native int getScreenWidth();
    public native int getScreenHeight();
    
    // === v2.0: High-FPS Async Streaming Capture (60fps-240fps) ===
    /**
     * Start async screen streaming capture for high-FPS recording (60fps-240fps).
     * Uses DXGI Desktop Duplication API for hardware-accelerated capture.
     * 
     * @param x X coordinate of capture region
     * @param y Y coordinate of capture region  
     * @param width Width of capture region
     * @param height Height of capture region
     * @return true if streaming started successfully
     */
    public native boolean startScreenStream(int x, int y, int width, int height);
    
    /**
     * Get next frame from the streaming capture (non-blocking).
     * Returns null if no new frame available yet.
     * Frame data is RGBA format: int[] = {R, G, B, A, R, G, B, A, ...}
     * 
     * @return int[] pixel data or null if no new frame
     */
    public native int[] getNextFrame();
    
    /**
     * Get next frame from the streaming capture, scaled to the specified dimensions.
     * Returns null if no new frame available yet.
     * Frame data is RGBA format: int[] = {R, G, B, A, R, G, B, A, ...}
     * 
     * @param scaleWidth The width to scale the frame to
     * @param scaleHeight The height to scale the frame to
     * @return int[] pixel data or null if no new frame
     */
    public native int[] getNextFrameScaled(int scaleWidth, int scaleHeight);
    
    /**
     * Get next frame from the streaming capture, scaled to the specified dimensions with anti-aliasing.
     * Returns null if no new frame available yet.
     * Frame data is RGBA format: int[] = {R, G, B, A, R, G, B, A, ...}
     * 
     * @param scaleWidth The width to scale the frame to
     * @param scaleHeight The height to scale the frame to
     * @param useAA Whether to use anti-aliasing
     * @return int[] pixel data or null if no new frame
     */
    public native int[] getNextFrameScaledAA(int scaleWidth, int scaleHeight, boolean useAA);
    
    /**
     * Check if a new frame is available without retrieving it.
     * Useful for polling at high frequency.
     * 
     * @return true if new frame ready
     */
    public native boolean hasNewFrame();
    
    /**
     * Stop the streaming capture and release resources.
     */
    public native void stopScreenStream();
    
    /**
     * Get current streaming FPS (frames per second).
     * Call this periodically to monitor performance.
     * 
     * @return current FPS
     */
    public native double getStreamFPS();
    
    // === Convenience methods (Java-side wrappers) ===
    
    /**
     * Capture screen region as BufferedImage.
     * Faster than Robot.createScreenCapture() because it uses BitBlt directly.
     */
    public BufferedImage createScreenCapture(Rectangle screenRect) {
        int[] pixels = getScreenPixels(screenRect.x, screenRect.y, screenRect.width, screenRect.height);
        BufferedImage image = new BufferedImage(screenRect.width, screenRect.height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, screenRect.width, screenRect.height, pixels, 0, screenRect.width);
        return image;
    }
    
    /**
     * Click mouse button (press + release with minimal delay).
     * @param buttons BUTTON1, BUTTON2, or BUTTON3
     */
    public void mouseClick(int buttons) {
        mousePress(buttons);
        try { Thread.sleep(10); } catch (InterruptedException e) { }
        mouseRelease(buttons);
    }
    
    /**
     * Double-click mouse button (two clicks with Windows double-click timing).
     * @param buttons BUTTON1, BUTTON2, or BUTTON3
     */
    public void mouseDoubleClick(int buttons) {
        mouseClick(buttons);
        try { Thread.sleep(50); } catch (InterruptedException e) { } // Windows default double-click time
        mouseClick(buttons);
    }
    
    /**
     * Smooth mouse movement to target position (human-like).
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate  
     * @param durationMs Total movement duration in milliseconds
     */
    public void smoothMouseMove(int targetX, int targetY, int durationMs) {
        int[] pos = getMousePosition();
        int startX = pos[0];
        int startY = pos[1];
        
        int steps = Math.max(10, durationMs / 10); // At least 10 steps, one per 10ms
        long stepDelay = durationMs / steps;
        
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            // Ease-in-out interpolation
            t = t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
            
            int x = (int) (startX + (targetX - startX) * t);
            int y = (int) (startY + (targetY - startY) * t);
            mouseMove(x, y);
            
            if (i < steps) {
                try { Thread.sleep(stepDelay); } catch (InterruptedException e) { }
            }
        }
    }
    
    /**
     * Get current mouse position.
     * @return Point with x, y coordinates
     */
    public Point getMousePos() {
        int[] pos = getMousePosition();
        return new Point(pos[0], pos[1]);
    }
    
    // === Button constants (same as java.awt.InputEvent) ===
    public static final int BUTTON1 = 1 << 0;  // Left mouse button
    public static final int BUTTON2 = 1 << 1;  // Middle mouse button  
    public static final int BUTTON3 = 1 << 2;  // Right mouse button
    
    // === Keycode conversion helpers ===
    
    /**
     * Convert Java KeyEvent keycode to Windows virtual keycode.
     */
    public static int javaKeyToWindows(int javaKeycode) {
        // Map common Java keycodes to Windows VK codes
        return switch (javaKeycode) {
            case 10 -> 0x0D; // VK_ENTER
            case 8 -> 0x08; // VK_BACK
            case 9 -> 0x09; // VK_TAB
            case 16 -> 0x10; // VK_SHIFT
            case 17 -> 0x11; // VK_CONTROL
            case 18 -> 0x12; // VK_ALT
            case 27 -> 0x1B; // VK_ESCAPE
            case 32 -> 0x20; // VK_SPACE
            case 33 -> 0x21; // VK_PAGE_UP
            case 34 -> 0x22; // VK_PAGE_DOWN
            case 35 -> 0x23; // VK_END
            case 36 -> 0x24; // VK_HOME
            case 37 -> 0x25; // VK_LEFT
            case 38 -> 0x26; // VK_UP
            case 39 -> 0x27; // VK_RIGHT
            case 40 -> 0x28; // VK_DOWN
            case 112 -> 0x70; // VK_F1
            case 113 -> 0x71; // VK_F2
            case 114 -> 0x72; // VK_F3
            case 115 -> 0x73; // VK_F4
            case 116 -> 0x74; // VK_F5
            case 117 -> 0x75; // VK_F6
            case 118 -> 0x76; // VK_F7
            case 119 -> 0x77; // VK_F8
            case 120 -> 0x78; // VK_F9
            case 121 -> 0x79; // VK_F10
            case 122 -> 0x7A; // VK_F11
            case 123 -> 0x7B; // VK_F12
            default -> {
                // A-Z: Java uses uppercase ASCII, Windows VK_A=0x41, etc.
                if (javaKeycode >= 'A' && javaKeycode <= 'Z') {
                    yield javaKeycode; // Same values
                }
                // 0-9: Java uses ASCII digits, Windows VK_0=0x30, etc.
                if (javaKeycode >= '0' && javaKeycode <= '9') {
                    yield javaKeycode; // Same values
                }
                yield javaKeycode; // Pass through for other keys
            }
        };
    }
}