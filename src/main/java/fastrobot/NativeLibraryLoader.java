package fastrobot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Smart native library loader that extracts and loads the appropriate library
 * for the current platform from the classpath.
 */
public class NativeLibraryLoader {
    
    private static final String LIBRARY_NAME = "fastrobot";
    private static boolean loaded = false;
    
    /**
     * Loads the native library for the current platform.
     * Extracts from classpath if not found in system path.
     */
    public static synchronized void load() {
        if (loaded) {
            return;
        }
        
        try {
            // Try loading from system path first
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
        } catch (UnsatisfiedLinkError e1) {
            // Extract and load from classpath
            try {
                String libraryPath = extractNativeLibrary();
                System.load(libraryPath);
                loaded = true;
            } catch (Exception e2) {
                throw new UnsatisfiedLinkError("Failed to load native library: " + e2.getMessage());
            }
        }
    }
    
    /**
     * Extracts the native library for the current platform from classpath.
     */
    private static String extractNativeLibrary() throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        
        String libraryFileName;
        String resourcePath;
        
        if (osName.contains("win")) {
            libraryFileName = LIBRARY_NAME + ".dll";
            resourcePath = "/native/" + libraryFileName;
        } else if (osName.contains("mac")) {
            libraryFileName = "lib" + LIBRARY_NAME + ".dylib";
            resourcePath = "/native/" + libraryFileName;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            libraryFileName = "lib" + LIBRARY_NAME + ".so";
            resourcePath = "/native/" + libraryFileName;
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }
        
        // Extract to temp directory
        Path tempDir = Files.createTempDirectory("fastrobot-");
        File libraryFile = tempDir.resolve(libraryFileName).toFile();
        
        try (InputStream in = NativeLibraryLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new RuntimeException("Native library not found: " + resourcePath);
            }
            
            try (FileOutputStream out = new FileOutputStream(libraryFile)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
        }
        
        // Make executable on Unix-like systems
        if (!osName.contains("win")) {
            libraryFile.setExecutable(true);
        }
        
        // Add shutdown hook to clean up temp files
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.deleteIfExists(libraryFile.toPath());
                Files.deleteIfExists(tempDir);
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }));
        
        return libraryFile.getAbsolutePath();
    }
    
    /**
     * Gets the platform information for debugging.
     */
    public static String getPlatformInfo() {
        return String.format("OS: %s, Arch: %s, Java: %s", 
            System.getProperty("os.name"),
            System.getProperty("os.arch"),
            System.getProperty("java.version"));
    }
}
