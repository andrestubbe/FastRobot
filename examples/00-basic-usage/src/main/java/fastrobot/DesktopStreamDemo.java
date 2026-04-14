package fastrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class DesktopStreamDemo {
    private static FastRobot robot;
    private static JFrame frame;
    private static JLabel imageLabel;
    private static volatile boolean running = true;
    private static volatile boolean uiReady = false;
    private static long frameCount = 0;
    private static long lastFpsTime = System.currentTimeMillis();
    private static BufferedImage scaledImage;
    private static ImageIcon imageIcon;
    private static volatile int windowWidth;
    private static volatile int windowHeight;
    private static final Object dimensionLock = new Object();
    private static double screenAspectRatio;
    
    // Panel with black background that centers the image
    static class LetterboxPanel extends JPanel {
        public LetterboxPanel() {
            setBackground(Color.BLACK);
            setLayout(new GridBagLayout()); // Centers component
        }
    }
    
    public static void main(String[] args) {
        robot = new FastRobot();
        
        int screenWidth = robot.getScreenWidth();
        int screenHeight = robot.getScreenHeight();
        screenAspectRatio = (double) screenWidth / screenHeight;
        
        int initialWidth = screenWidth / 4;
        int initialHeight = screenHeight / 4;
        
        synchronized (dimensionLock) {
            windowWidth = initialWidth;
            windowHeight = initialHeight;
        }
        
        scaledImage = new BufferedImage(initialWidth, initialHeight, BufferedImage.TYPE_INT_RGB);
        imageIcon = new ImageIcon(scaledImage);
        
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Desktop Stream - " + initialWidth + "x" + initialHeight + " - Resizable");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.setResizable(true);
            frame.setSize(initialWidth, initialHeight);
            frame.setLocation(0, 0);
            
            LetterboxPanel panel = new LetterboxPanel();
            imageLabel = new JLabel(imageIcon);
            panel.add(imageLabel);
            frame.add(panel);
            
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    running = false;
                    robot.stopScreenStream();
                }
            });
            
            frame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int w = frame.getWidth();
                    int h = frame.getHeight();
                    synchronized (dimensionLock) {
                        windowWidth = w;
                        windowHeight = h;
                    }
                    frame.setTitle("Desktop Stream - " + w + "x" + h + " - Resizable");
                }
            });
            
            frame.setVisible(true);
            uiReady = true;
        });
        
        robot.startScreenStream(0, 0, screenWidth, screenHeight);
        
        new Thread(() -> {
            while (!uiReady) {
                Thread.yield();
            }
            while (running) {
                if (robot.hasNewFrame()) {
                    int winW, winH;
                    synchronized (dimensionLock) {
                        winW = windowWidth;
                        winH = windowHeight;
                    }
                    
                    // Calculate scaled size that fits while maintaining aspect ratio
                    int[] scaledSize = calculateScaledSize(winW, winH);
                    int captureW = scaledSize[0];
                    int captureH = scaledSize[1];
                    
                    int[] pixels = robot.getNextFrameScaledAA(captureW, captureH, true);
                    if (pixels != null) {
                        frameCount++;
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastFpsTime >= 1000) {
                            long fps = frameCount;
                            SwingUtilities.invokeLater(() -> {
                                frame.setTitle("Desktop Stream - " + winW + "x" + winH + " - " + fps + " FPS");
                            });
                            frameCount = 0;
                            lastFpsTime = currentTime;
                        }
                        
                        // Update the image with the scaled capture
                        if (scaledImage.getWidth() != captureW || scaledImage.getHeight() != captureH) {
                            scaledImage = new BufferedImage(captureW, captureH, BufferedImage.TYPE_INT_RGB);
                            imageIcon.setImage(scaledImage);
                        }
                        scaledImage.setRGB(0, 0, captureW, captureH, pixels, 0, captureW);
                        imageLabel.repaint();
                    }
                }
            }
        }).start();
    }
    
    // Calculate scaled dimensions that fit within window while maintaining aspect ratio
    private static int[] calculateScaledSize(int winW, int winH) {
        double windowAspect = (double) winW / winH;
        
        int scaledW, scaledH;
        if (windowAspect > screenAspectRatio) {
            // Window is wider than screen - fit to height
            scaledH = winH;
            scaledW = (int) (winH * screenAspectRatio);
        } else {
            // Window is taller or same - fit to width
            scaledW = winW;
            scaledH = (int) (winW / screenAspectRatio);
        }
        
        // Ensure minimum size
        if (scaledW < 1) scaledW = 1;
        if (scaledH < 1) scaledH = 1;
        
        return new int[]{scaledW, scaledH};
    }
}
