package fastrobot;

import java.awt.Robot;
import java.awt.AWTException;

/**
 * Simple test to try loading the DLL with absolute path
 */
public class FastRobotTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== FastRobot DLL Test ===");
        
        try {
            // Try loading with absolute path
            System.load("c:\\Users\\andre\\.openclaw\\workspace\\fastrobot\\build\\fastrobot.dll");
            System.out.println("✅ DLL loaded successfully!");
            
            // Test if we can create FastRobot
            FastRobot fastRobot = new FastRobot();
            System.out.println("✅ FastRobot created successfully!");
            
            // Test a simple operation
            int width = fastRobot.getScreenWidth();
            int height = fastRobot.getScreenHeight();
            System.out.println("✅ Screen size: " + width + "x" + height);
            
            System.out.println("🎉 FastRobot is working!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
