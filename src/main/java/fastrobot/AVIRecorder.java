package fastrobot;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * v2.0: High-FPS Screen Recording to AVI/MJPEG
 * Demonstrates 60fps+ screen capture and video encoding
 */
public class AVIRecorder {
    
    private FastRobot robot;
    private boolean isRecording = false;
    private String outputPath;
    private int fps;
    
    public AVIRecorder() throws Exception {
        robot = new FastRobot();
    }
    
    /**
     * Start recording screen region to AVI file
     * @param x X coordinate of capture region
     * @param y Y coordinate of capture region
     * @param width Width of capture region
     * @param height Height of capture region
     * @param outputFile Output AVI file path
     * @param targetFPS Target recording FPS (30, 60, 120, etc.)
     */
    public void startRecording(int x, int y, int width, int height, 
                               String outputFile, int targetFPS) {
        this.outputPath = outputFile;
        this.fps = targetFPS;
        
        System.out.println("Starting screen recording...");
        System.out.println("Region: " + x + "," + y + " " + width + "x" + height);
        System.out.println("Target FPS: " + targetFPS);
        System.out.println("Output: " + outputFile);
        
        // Start high-FPS streaming capture
        boolean success = robot.startScreenStream(x, y, width, height);
        if (!success) {
            throw new RuntimeException("Failed to start screen stream!");
        }
        
        isRecording = true;
        
        // Start recording thread
        Thread recordingThread = new Thread(() -> recordFrames(width, height));
        recordingThread.start();
    }
    
    private void recordFrames(int width, int height) {
        int frameCount = 0;
        long startTime = System.nanoTime();
        long frameInterval = 1_000_000_000L / fps; // nanoseconds per frame
        
        // Create output directory
        File outputDir = new File("frames");
        outputDir.mkdirs();
        
        System.out.println("Recording... Press Enter to stop");
        
        while (isRecording) {
            long frameStart = System.nanoTime();
            
            // Get frame if available
            if (robot.hasNewFrame()) {
                int[] pixels = robot.getNextFrame();
                if (pixels != null) {
                    // Convert to BufferedImage
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    image.setRGB(0, 0, width, height, pixels, 0, width);
                    
                    // Save frame as PNG (temporary)
                    // In production, use MJPEG encoder directly
                    try {
                        File outputFile = new File(outputDir, String.format("frame_%05d.png", frameCount));
                        ImageIO.write(image, "PNG", outputFile);
                        frameCount++;
                        
                        if (frameCount % 30 == 0) {
                            double currentFPS = robot.getStreamFPS();
                            System.out.printf("Captured %d frames (%.1f fps actual)%n", 
                                              frameCount, currentFPS);
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to write frame: " + e.getMessage());
                    }
                }
            }
            
            // Maintain target FPS
            long elapsed = System.nanoTime() - frameStart;
            long sleepTime = (frameInterval - elapsed) / 1_000_000; // convert to ms
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        
        // Summary
        long totalTime = System.nanoTime() - startTime;
        double actualFPS = frameCount / (totalTime / 1_000_000_000.0);
        System.out.println("\n=== Recording Complete ===");
        System.out.println("Total frames: " + frameCount);
        System.out.printf("Duration: %.1f seconds%n", totalTime / 1_000_000_000.0);
        System.out.printf("Average FPS: %.1f%n", actualFPS);
        System.out.println("Frames saved to: " + outputDir.getAbsolutePath());
        
        // Note about AVI encoding
        System.out.println("\nTo create AVI file:");
        System.out.println("1. Use FFmpeg: ffmpeg -i frames/frame_%05d.png -c:v mjpeg -q:v 2 output.avi");
        System.out.println("2. Or use a Java library like Xuggler or JavaCV");
    }
    
    public void stopRecording() {
        isRecording = false;
        robot.stopScreenStream();
        System.out.println("Recording stopped");
    }
    
    public static void main(String[] args) throws Exception {
        AVIRecorder recorder = new AVIRecorder();
        
        // Record 1920x1080 region at 60fps
        int screenWidth = recorder.robot.getScreenWidth();
        int screenHeight = recorder.robot.getScreenHeight();
        
        int recordWidth = Math.min(1920, screenWidth);
        int recordHeight = Math.min(1080, screenHeight);
        int startX = (screenWidth - recordWidth) / 2;
        int startY = (screenHeight - recordHeight) / 2;
        
        recorder.startRecording(startX, startY, recordWidth, recordHeight, 
                                "output.avi", 60);
        
        // Wait for user to press Enter
        System.out.println("Press Enter to stop recording...");
        System.in.read();
        
        recorder.stopRecording();
    }
}
