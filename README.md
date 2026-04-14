# FastRobot — High-Performance Java Automation & DirectX Screen Capture

**⚡ Ultra-fast Java screen capture & automation library — 10-17× faster than java.awt.Robot**

[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/andrestubbe/FastRobot.svg)](https://jitpack.io/#andrestubbe/FastRobot)

FastRobot is a **high-performance Java automation library** that replaces `java.awt.Robot` with a **native Windows backend** using DirectInput, GDI BitBlt, and DirectX DXGI. Built for **low-latency automation**, **real-time screen capture**, **gaming bots**, **test automation**, and **computer vision** applications.

---

## Quick Start

### Installation

**Maven:**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastrobot</artifactId>
    <version>v2.1.0</version>
</dependency>
```

**Gradle:**
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastrobot:v2.1.0'
}
```

**Direct Download:**
- [fastrobot-2.1.0.jar](https://github.com/andrestubbe/FastRobot/releases/download/v2.1.0/fastrobot-2.1.0.jar)

### Basic Usage

```java
import fastrobot.FastRobot;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

FastRobot robot = new FastRobot();

// Fast screen capture - 10-17× faster than Robot
BufferedImage screen = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));

// Instant mouse control
robot.mouseMove(500, 500);
robot.mousePress(FastRobot.BUTTON1);
robot.mouseRelease(FastRobot.BUTTON1);
```

### High-FPS Streaming

```java
// Start 60fps-240fps streaming capture
robot.startScreenStream(0, 0, 1920, 1080);

while (true) {
    if (robot.hasNewFrame()) {
        int[] pixels = robot.getNextFrame(); // RGBA pixel array
        double fps = robot.getStreamFPS();
        System.out.println("Streaming at " + fps + " FPS");
    }
}
```

---

## Key Features

- **10-17× faster screen capture** than `java.awt.Robot` (60fps+ streaming)
- **515× faster mouse click latency** (DirectInput vs AWT event queue)
- **60fps+ streaming** with DXGI hardware acceleration
- **DirectInput mouse/keyboard** — no OS throttling
- **Zero GC pressure** — native buffers, no Java2D overhead
- **Powered by FastCore** — unified JNI loader for all FastJava modules
- **MIT licensed** — free for commercial use

---

## Performance Benchmarks

| Operation | java.awt.Robot | FastRobot | Speedup |
|-----------|----------------|-----------|---------|
| Screen Capture (1920×1080) | ~138ms | **~8-16ms** | **10-17×** |
| Mouse Click Latency | ~0.24ms | **~0.0005ms** | **515×** |
| **Streaming FPS** | ~7fps | **60fps+** | **8-10×** |

*Measured on Windows 11, Intel Core i7, Java 17, 120Hz display*

---

## Examples

All examples are in the `examples/` folder:

```bash
# Desktop Stream Demo - Live preview with 60 FPS
cd examples/00-basic-usage
mvn compile exec:java
```

---

## Project Structure

```
fastrobot/
├── src/main/java/fastrobot/    # Main API
│   ├── FastRobot.java          # Core automation class
│   └── NativeLibraryLoader.java # JNI loader
├── examples/                   # Runnable examples
│   └── 00-basic-usage/         # DesktopStreamDemo, PNGRecorder, Benchmark
├── native/                     # C++ JNI source
│   ├── fastrobot.cpp           # Native implementation
│   └── DXGICapture.cpp         # DirectX capture
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

---

## Building from Source

### Prerequisites
- JDK 17+
- Maven 3.9+
- Visual Studio 2019+ (for native DLL)

### Build
```bash
git clone https://github.com/andrestubbe/fastrobot.git
cd fastrobot

# Build Java + native DLL
mvn clean compile

# Create JAR with native libraries
mvn package
```

---

## API Reference

### Mouse Operations
- `mouseMove(int x, int y)` — Instant cursor positioning
- `mousePress(int buttons)` — Direct hardware button press
- `mouseRelease(int buttons)` — Direct hardware button release
- `mouseClick(int buttons)` — Press + release
- `smoothMouseMove(int x, int y, int durationMs)` — Human-like movement

### Keyboard Operations
- `keyPress(int keycode)` — Zero-latency key press
- `keyRelease(int keycode)` — Zero-latency key release

### Screen Capture
- `createScreenCapture(Rectangle rect)` — BufferedImage capture
- `getPixelColor(int x, int y)` — Single pixel (100× faster)

### v2.0+ Streaming (High-FPS)
- `startScreenStream(int x, int y, int w, int h)` — Begin 60fps+ capture
- `getNextFrame()` — Get next frame (non-blocking)
- `stopScreenStream()` — Stop and cleanup

---

## Architecture

```
Java API (FastRobot.java)
    ↓ JNI (via FastCore)
Native Layer (C++/Win32)
    ├── DirectInput → Mouse/Keyboard
    ├── GDI BitBlt → Screen capture
    └── DXGI Desktop Duplication → Streaming
    ↓
Windows OS (Hardware)
```

**Powered by [FastCore](https://github.com/andrestubbe/FastCore)** — Unified JNI loader for the FastJava ecosystem.

---

## Platform Support

| Platform | Status |
|----------|--------|
| Windows 11 | ✅ Full support |
| Windows 10 | ✅ Full support |
| Linux | ❌ Not planned |
| macOS | ❌ Not planned |

---

## Version History

### v2.1.0 — Current
- **FastCore integration** — Unified JNI loader
- **Cleaner structure** — Blueprint-based project layout
- **Examples folder** — Separated demos from core library

### v2.0.0
- **DXGI Desktop Duplication API** — hardware-accelerated streaming
- **60fps+ capture** — matches monitor refresh rate
- **C++ scaling** — Hardware-accelerated frame scaling

### v1.0.0
- GDI BitBlt screen capture
- DirectInput mouse/keyboard
- Basic JNI wrapper

---

## License

MIT License — free for commercial and private use. See [LICENSE](LICENSE) for details.

---

## Related Projects

- [FastCore](https://github.com/andrestubbe/FastCore) — Unified JNI loader
- [FastHotkey](https://github.com/andrestubbe/FastHotkey) — Global hotkey library
- [FastClipboard](https://github.com/andrestubbe/FastClipboard) — Fast clipboard access

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.*
