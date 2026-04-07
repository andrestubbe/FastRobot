
import fastrobot.FastRobot;
import java.awt.Point;
public class TestMouse {
    public static void main(String[] args) throws Exception {
        FastRobot robot = new FastRobot();
        
        // Test get mouse position
        Point pos = robot.getMousePos();
        System.out.println("Current position: " + pos.x + ", " + pos.y);
        
        // Test relative move
        System.out.println("Moving relative +50, +50...");
        robot.mouseMoveRelative(50, 50);
        Thread.sleep(500);
        
        // Test smooth move back
        System.out.println("Smooth moving back...");
        robot.smoothMouseMove(pos.x, pos.y, 500);
        
        // Test click
        System.out.println("Left click...");
        robot.mouseClick(FastRobot.BUTTON1);
        Thread.sleep(200);
        
        // Test double click
        System.out.println("Double click...");
        robot.mouseDoubleClick(FastRobot.BUTTON1);
        
        System.out.println("All mouse tests passed!");
    }
}

