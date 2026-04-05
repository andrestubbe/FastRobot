package fastrobot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UltraFastRobot
 */
public class UltraFastRobotTest {
    
    private UltraFastRobot robot;
    
    @BeforeEach
    void setUp() {
        robot = new UltraFastRobot();
    }
    
    @Test
    void testScreenSize() {
        int width = robot.getScreenWidth();
        int height = robot.getScreenHeight();
        
        assertTrue(width > 0, "Screen width should be positive");
        assertTrue(height > 0, "Screen height should be positive");
        
        System.out.printf("Screen size: %dx%d%n", width, height);
    }
    
    @Test
    void testMouseMove() {
        assertDoesNotThrow(() -> {
            robot.mouseMoveInstant(100, 100);
            robot.mouseMoveInstant(200, 200);
        }, "Mouse move should not throw exception");
    }
    
    @Test
    void testMouseClick() {
        assertDoesNotThrow(() -> {
            robot.mousePressInstant(UltraFastRobot.BUTTON1);
            robot.mouseReleaseInstant(UltraFastRobot.BUTTON1);
        }, "Mouse click should not throw exception");
    }
    
    @Test
    void testKeyboardInput() {
        assertDoesNotThrow(() -> {
            robot.keyPressInstant(65); // 'A'
            robot.keyReleaseInstant(65);
        }, "Keyboard input should not throw exception");
    }
    
    @Test
    void testPixelColor() {
        int color = robot.getPixelColorInstant(100, 100);
        assertTrue(color >= 0, "Pixel color should be non-negative");
        
        // Extract RGB components
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        
        assertTrue(r >= 0 && r <= 255, "Red component should be 0-255");
        assertTrue(g >= 0 && g <= 255, "Green component should be 0-255");
        assertTrue(b >= 0 && b <= 255, "Blue component should be 0-255");
    }
    
    @Test
    void testKeycodeConversion() {
        assertEquals(0x0D, UltraFastRobot.javaKeyToWindows(10)); // Enter
        assertEquals(0x08, UltraFastRobot.javaKeyToWindows(8));  // Backspace
        assertEquals(0x41, UltraFastRobot.javaKeyToWindows('A')); // A
        assertEquals(0x30, UltraFastRobot.javaKeyToWindows('0')); // 0
    }
    
    @Test
    void testNativeLibraryLoader() {
        assertDoesNotThrow(() -> {
            NativeLibraryLoader.load();
        }, "Native library loading should not throw exception");
        
        String platformInfo = NativeLibraryLoader.getPlatformInfo();
        assertNotNull(platformInfo, "Platform info should not be null");
        assertTrue(platformInfo.contains("OS:"), "Platform info should contain OS");
        assertTrue(platformInfo.contains("Arch:"), "Platform info should contain architecture");
        
        System.out.println("Platform: " + platformInfo);
    }
}
