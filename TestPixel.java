import fastrobot.FastRobot;
public class TestPixel {
    public static void main(String[] args) throws Exception {
        FastRobot robot = new FastRobot();
        System.out.println("Screen: " + robot.getScreenWidth() + "x" + robot.getScreenHeight());
        
        // Test pixel reading
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int color = robot.getPixelColor(100, 100);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("1000 pixel reads in " + time + "ms (" + (1000.0/time) + " pixels/ms)");
        
        // Test screen capture
        start = System.currentTimeMillis();
        int[] pixels = robot.getScreenPixels(0, 0, 100, 100);
        time = System.currentTimeMillis() - start;
        System.out.println("100x100 capture in " + time + "ms (" + pixels.length + " pixels)");
        
        System.out.println("Basic tests passed!");
    }
}
