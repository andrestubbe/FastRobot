package fastrobot;

import java.awt.Robot;
import java.awt.AWTException;

/**
 * Simple test to verify Java compilation works without native dependencies
 */
public class JavaOnlyTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Java-only Test ===");
        
        Robot awtRobot = new Robot();
        System.out.println("✅ java.awt.Robot created successfully");
        
        // Test basic AWT Robot functionality
        awtRobot.mouseMove(100, 100);
        System.out.println("✅ Mouse move test completed");
        
        System.out.println("Java compilation and basic AWT Robot working!");
        System.out.println("Note: FastRobot requires native DLL compilation");
    }
}
