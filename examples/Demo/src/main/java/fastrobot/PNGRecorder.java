package fastrobot;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * High-FPS Screen Recording to numbered PNG frames
 * Convert to video using FFmpeg: ffmpeg -i frame_%05d.png -c:v libx264 -r 60 output.mp4
 */
public class PNGRecorder {
    
    private FastRobot robot;
    private volatile boolean isRecording = false;
    private File outputDir;
    private int fps;
    private int frameCount = 0;
    private int width, height;
    
    public PNGRecorder() throws Exception {
        robot = new FastRobot();
    }
    
    /**
     * Start recording screen region to numbered PNG frames
     * @param x X coordinate of capture region
     * @param y Y coordinate of capture region
     * @param width Width of capture region
     * @param height Height of capture region
     * @param outputDir Output directory for frames
     * @param targetFPS Target recording FPS
     */
    public void startRecording(int x, int y, int width, int height, 
                               String outputDir, int targetFPS) throws IOException {
        this.width = width;
        this.height = height;
        this.fps = targetFPS;
        
        this.outputDir = new File(outputDir);
        this.outputDir.mkdirs();
        
        // Clear old frames
        File[] oldFiles = this.outputDir.listFiles((d, n) -> n.endsWith(".png"));
        if (oldFiles != null) {
            for (File f : oldFiles) f.delete();
        }
        
        System.out.println("Starting screen recording...");
        System.out.println("Region: " + x + "," + y + " " + width + "x" + height);
        System.out.println("Target FPS: " + targetFPS);
        System.out.println("Output: " + this.outputDir.getAbsolutePath());
        
        boolean success = robot.startScreenStream(x, y, width, height);
        if (!success) {
            throw new RuntimeException("Failed to start screen stream!");
        }
        
        isRecording = true;
        Thread recordingThread = new Thread(() -> recordFrames());
        recordingThread.start();
    }
    
    private void recordFrames() {
        long startTime = System.nanoTime();
        long frameInterval = 1_000_000_000L / fps;
        
        System.out.println("Recording... Press Enter to stop");
        
        while (isRecording) {
            long frameStart = System.nanoTime();
            
            if (robot.hasNewFrame()) {
                int[] pixels = robot.getNextFrame();
                if (pixels != null) {
                    try {
                        writeFrame(pixels);
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
            
            long elapsed = System.nanoTime() - frameStart;
            long sleepTime = (frameInterval - elapsed) / 1_000_000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        
        long totalTime = System.nanoTime() - startTime;
        double actualFPS = frameCount / (totalTime / 1_000_000_000.0);
        System.out.println("\n=== Recording Complete ===");
        System.out.println("Total frames: " + frameCount);
        System.out.printf("Duration: %.1f seconds%n", totalTime / 1_000_000_000.0);
        System.out.printf("Average FPS: %.1f%n", actualFPS);
        System.out.println("\nTo create video:");
        System.out.println("  ffmpeg -i " + outputDir.getAbsolutePath() + "/frame_%05d.png -c:v libx264 -r " + fps + " -pix_fmt yuv420p output.mp4");
    }
    
    private void writeFrame(int[] pixels) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        
        File outputFile = new File(outputDir, String.format("frame_%05d.png", frameCount));
        ImageIO.write(image, "PNG", outputFile);
    }
    
    public void stopRecording() {
        isRecording = false;
        robot.stopScreenStream();
        System.out.println("Recording stopped");
    }
    
    public static void main(String[] args) throws Exception {
        PNGRecorder recorder = new PNGRecorder();
        
        int screenWidth = recorder.robot.getScreenWidth();
        int screenHeight = recorder.robot.getScreenHeight();
        
        int recordWidth = Math.min(1920, screenWidth);
        int recordHeight = Math.min(1080, screenHeight);
        int startX = (screenWidth - recordWidth) / 2;
        int startY = (screenHeight - recordHeight) / 2;
        
        recorder.startRecording(startX, startY, recordWidth, recordHeight, 
                                "frames", 60);
        
        System.out.println("Press Enter to stop recording...");
        System.in.read();
        
        recorder.stopRecording();
    }
}
