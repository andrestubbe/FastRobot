package fastrobot;

/**
 * Simple test for UltraFastRobot
 */
public class UltraTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== UltraFastRobot Test ===");
        
        try {
            UltraFastRobot ultra = new UltraFastRobot();
            System.out.println("✅ UltraFastRobot created successfully!");
            
            int width = ultra.getScreenWidth();
            int height = ultra.getScreenHeight();
            System.out.println("✅ Screen size: " + width + "x" + height);
            
            // Test instant mouse move
            ultra.mouseMoveInstant(100, 100);
            System.out.println("✅ Instant mouse move successful!");
            
            System.out.println("🚀 UltraFastRobot is working!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
