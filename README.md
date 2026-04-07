# FastRobot — High-Performance Java Automation & DirectX Screen Capture (10-17× Faster than Robot)

**Drop-in replacement for `java.awt.Robot` with 10-17× faster screen capture, 515× faster mouse input, and 60-240fps video streaming for Windows**

[![JitPack](https://jitpack.io/v/andrestubbe/FastRobot.svg)](https://jitpack.io/#andrestubbe/FastRobot)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.andrestubbe/fastrobot.svg)](https://search.maven.org/artifact/io.github.andrestubbe/fastrobot)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![GitHub stars](https://img.shields.io/github/stars/andrestubbe/fastrobot.svg)](https://github.com/andrestubbe/fastrobot/stargazers)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

![FastRobot Desktop Stream Demo](docs/desktop-stream-demo.png)

```java
// Quick Start — Ultra-fast screen capture & input
FastRobot robot = new FastRobot();
robot.mouseMove(500, 300);
Color pixel = robot.getPixelColor(100, 200);
// Start 60fps streaming
robot.startScreenStream(0, 0, 1920, 1080);
```

FastRobot is a **high-performance Java automation library** that replaces `java.awt.Robot` with a **native Windows backend** using DirectInput, GDI BitBlt, and DirectX DXGI. Built for **low-latency automation**, **real-time screen capture**, **gaming bots**, **test automation**, and **computer vision** applications.

**Keywords:** java robot alternative, fast screen capture, directx screen recording, java automation library, windows bot framework, gdi bitblt, directinput, hardware accelerated capture, 60fps screen recording java, jni native performance

If you need **60fps+ screen recording**, **instant mouse/keyboard response**, or **high-frequency automation**, FastRobot delivers native-level performance with Java simplicity.

---

## Table of Contents

- [Why FastRobot?](#why-fastrobot)
- [Key Features](#key-features)
- [Performance Benchmarks](#performance-benchmarks)
- [FastRobot vs java.awt.Robot](#fastrobot-vs-javaawtrobot)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
- [Use Cases](#use-cases)
- [Examples](#examples)
- [Build from Source](#build-from-source)
- [Platform Support](#platform-support)
- [FAQ](#faq)
- [License](#license)

---

## Why FastRobot?

`java.awt.Robot` is convenient but slow. Its screen capture is bottlenecked by Java2D (15-30fps max), and input has OS-level throttling.

FastRobot solves this with:
- **GDI BitBlt** for ultra-fast screen capture (60-240fps capable)
- **DirectInput** for zero-latency keyboard/mouse events  
- **Native memory buffers** to avoid GC overhead
- **Hardware acceleration** via DXGI Desktop Duplication API
- **Drop-in API** — minimal code changes required

---

## Key Features

- **10-17× faster screen capture** than `java.awt.Robot` (60-240fps streaming)
- **515× faster mouse click latency** (DirectInput vs AWT event queue)
- **60fps-240fps streaming** with DXGI hardware acceleration
- **DirectInput mouse/keyboard** — no OS throttling
- **Zero GC pressure** — native buffers, no Java2D overhead
- **Drop-in API** — familiar Robot-style methods
- **Perfect for bots, automation, and real-time vision**
- **MIT licensed** — free for commercial use

---

## Performance Benchmarks

| Operation | java.awt.Robot | FastRobot | Speedup |
|-----------|----------------|-----------|---------|
| Screen Capture (1920×1080) | ~138ms | **~8-16ms** | **10-17×** |
| Mouse Click Latency | ~0.24ms | **~0.0005ms** | **515×** |
| Mouse Move | ~0.29ms | **~0.20ms** | **1.41×** |
| Keyboard Input | ~0.21ms | **~0.18ms** | **1.24×** |
| **Streaming FPS** | ~7fps | **60-240fps** | **10-34×** |

*Measured on Windows 11, Ryzen 7, Java 17, 144Hz monitor*

---

## FastRobot vs java.awt.Robot

| Feature | java.awt.Robot | FastRobot |
|---------|----------------|----------|
| Screen capture backend | Java2D (slow) | GDI BitBlt + DXGI (fast) |
| Max capture FPS | ~7fps | **60-240fps** |
| Input simulation | OS-layered | DirectInput (direct) |
| Latency | High (8-16ms) | Low (1-2ms) |
| GC pressure | High | None (native buffers) |
| Ideal for bots/gaming | ❌ | ✅ |
| Hardware acceleration | ❌ | ✅ (DXGI) |
| Async streaming | ❌ | ✅ |

---

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>io.github.andrestubbe</groupId>
    <artifactId>fastrobot</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'io.github.andrestubbe:fastrobot:2.0.0'
}
```

---

## Quick Start

### Basic Usage

```java
import fastrobot.FastRobot;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

FastRobot robot = new FastRobot();

// Fast screen capture - 10-17× faster than Robot
BufferedImage screen = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));

// Instant mouse control - 515× faster latency
robot.mouseMove(500, 500);
robot.mousePress(FastRobot.BUTTON1);
robot.mouseRelease(FastRobot.BUTTON1);

// Low-latency keyboard
robot.keyPress(java.awt.event.KeyEvent.VK_A);
robot.keyRelease(java.awt.event.KeyEvent.VK_A);
```

### High-FPS Streaming

```java
// Start 60fps-240fps streaming capture
robot.startScreenStream(0, 0, 1920, 1080);

// Non-blocking frame retrieval
while (true) {
    if (robot.hasNewFrame()) {
        int[] pixels = robot.getNextFrame(); // RGBA pixel array
        // Process frame in real-time
        double fps = robot.getStreamFPS();
        System.out.println("Streaming at " + fps + " FPS");
    }
}

// Stop streaming
robot.stopScreenStream();
```

---

## Use Cases

- **Gaming bots** — Real-time screen analysis + instant input response
- **Test automation** — Fast UI interaction without delays
- **Screen recording** — 60fps+ capture for video generation
- **Computer vision** — High-frequency frame processing
- **Automation scripts** — Reliable, fast task automation
- **Live streaming** — Real-time screen capture for broadcasting

---

## Examples

### DesktopStreamDemo.java
Resizable live desktop preview with letterboxing:
- Maintains aspect ratio with black bars
- 60 FPS with `getNextFrameScaledAA()`
- Hardware-accelerated scaling in C++

```bash
mvn compile exec:java -Dexec.mainClass="fastrobot.DesktopStreamDemo"
```

### PNGRecorder.java
Screen recording to numbered PNG frames:
- Lossless frame capture
- 60fps+ recording capability
- Convert to video with FFmpeg

```bash
mvn compile exec:java -Dexec.mainClass="fastrobot.PNGRecorder"
```

After recording, create MP4:
```bash
ffmpeg -i frames/frame_%05d.png -c:v libx264 -r 60 -pix_fmt yuv420p output.mp4
```

### Benchmark.java
Performance comparison vs `java.awt.Robot`:
- Screen capture latency
- Mouse/keyboard input speed
- Streaming FPS measurement

```bash
mvn compile exec:java -Dexec.mainClass="fastrobot.Benchmark"
```

---

## Architecture

FastRobot uses a hybrid Java + native architecture:

```
Java API (FastRobot.java)
    ↓ JNI
Native Layer (C++/Win32)
    ├── DirectInput → Mouse/Keyboard (zero latency)
    ├── GDI BitBlt → Screen capture (fast sync)
    └── DXGI Desktop Duplication → Streaming (60-240fps async)
    ↓
Windows OS (Hardware)
```

**Key technologies:**
- **JNI** — Java-to-native bridge
- **DirectInput** — Direct hardware access for input
- **GDI BitBlt** — Fast synchronous screen capture
- **DXGI Desktop Duplication API** — Hardware-accelerated streaming
- **Native memory buffers** — Zero GC pressure

---

## Build from Source

### Prerequisites

Before building FastRobot, you need to install the following dependencies:

#### 1. Java Development Kit (JDK) 17 or higher
**Download:** https://www.oracle.com/java/technologies/downloads/ or https://adoptium.net/

**Verify installation:**
```bash
java -version
javac -version
```

**Set JAVA_HOME environment variable:**
```powershell
# PowerShell (as Administrator)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
```

#### 2. Apache Maven 3.8+
**Download:** https://maven.apache.org/download.cgi

**Extract to:** `C:\Program Files\Apache\Maven`

**Add to PATH:**
```powershell
# PowerShell (as Administrator)
$path = [System.Environment]::GetEnvironmentVariable("PATH", "Machine")
[System.Environment]::SetEnvironmentVariable("PATH", $path + ";C:\Program Files\Apache\Maven\bin", "Machine")
```

**Verify installation:**
```bash
mvn -version
```

#### 3. Visual Studio 2019 or 2022 (Community Edition is free)
**Download:** https://visualstudio.microsoft.com/downloads/

**Required components:**
- ✅ **Desktop development with C++** workload
- ✅ **Windows 10/11 SDK** (included in workload)
- ✅ **MSVC v142/v143 - VS 2019/2022 C++ x64/x86 build tools**
- ✅ **C++ CMake tools for Windows** (optional but recommended)

**Installation steps:**
1. Run Visual Studio Installer
2. Select "Desktop development with C++"
3. In "Individual components" tab, verify:
   - MSVC v143 - VS 2022 C++ x64/x86 build tools (Latest)
   - Windows 11 SDK (or Windows 10 SDK)
   - C++ Profiling tools (optional)
4. Click "Install" (~5-10 GB download)

**Verify installation:**
```bash
cl.exe
# Should show: Microsoft (R) C/C++ Optimizing Compiler Version
```

#### 4. DirectX Runtime (usually included in Windows)
FastRobot v2.0 uses:
- **DirectX 11** or **DirectX 12**
- **DXGI Desktop Duplication API**

**Check if DirectX is installed:**
```powershell
# Run in PowerShell
dxdiag
# If dxdiag opens, DirectX is installed
```

**If not installed:** Download DirectX End-User Runtime from Microsoft

### Build

```bash
git clone https://github.com/andrestubbe/fastrobot.git
cd fastrobot

# Build Java + compile native DLL
mvn clean compile

# Run tests
mvn test

# Create JAR with native libraries
mvn package
```

---

## API Reference

### Mouse Operations
- `mouseMove(int x, int y)` — Instant cursor positioning
- `mousePress(int buttons)` — Direct hardware button press
- `mouseRelease(int buttons)` — Direct hardware button release
- `mouseWheel(int wheelRotation)` — Precise wheel control

### Keyboard Operations
- `keyPress(int keycode)` — Zero-latency key press
- `keyRelease(int keycode)` — Zero-latency key release

### Screen Capture
- `getPixelColor(int x, int y)` — Single pixel (100× faster)
- `getScreenPixels(int x, int y, int w, int h)` — Region capture (int[])
- `createScreenCapture(Rectangle rect)` — BufferedImage capture

### v2.0 Streaming (High-FPS)
- `startScreenStream(int x, int y, int w, int h)` — Begin 60fps+ capture
- `getNextFrame()` — Get next frame (non-blocking, returns int[])
- `getNextFrameScaled(int w, int h)` — Get scaled frame (C++ nearest-neighbor)
- `getNextFrameScaledAA(int w, int h, boolean useAA)` — Get scaled frame with optional box-filter AA
- `hasNewFrame()` — Check if new frame available
- `getStreamFPS()` — Get current streaming FPS
- `stopScreenStream()` — Stop and cleanup

### Screen Info
- `getScreenWidth()` — Primary monitor width
- `getScreenHeight()` — Primary monitor height

---

## Platform Support

| Platform | Version | Status |
|----------|---------|--------|
| Windows 11 | v2.0 | ✅ Full support (DXGI + DirectInput) |
| Windows 10 | v2.0 | ✅ Full support (DXGI + DirectInput) |
| Linux | — | ❌ Not planned |
| macOS | — | ❌ Not planned |

**Windows-only by design** — we focus on maximum performance on the most common gaming/automation platform.

---

## FAQ

### Is FastRobot faster than Java Robot?
**Yes.** FastRobot is 10-17× faster for screen capture and 515× faster for mouse input using native DirectInput and GDI BitBlt.

### Does FastRobot use DirectX?
**Yes.** FastRobot v2.0 uses the **DXGI Desktop Duplication API** for hardware-accelerated screen streaming at 60-240fps.

### Can I use FastRobot for game bots?
**Yes.** FastRobot is designed for low-latency automation with DirectInput for instant mouse/keyboard control, ideal for gaming bots.

### Is FastRobot cross-platform?
**No.** FastRobot is Windows-only (Windows 10/11) to maximize performance using native Win32 APIs.

### How do I migrate from java.awt.Robot?
Replace `new Robot()` with `new FastRobot()` — the API is mostly compatible. See [API Reference](#api-reference) for details.

### Related Projects
- [Java Robot](https://docs.oracle.com/javase/8/docs/api/java/awt/Robot.html) — Standard Java automation
- [JNA](https://github.com/java-native-access/jna) — Java Native Access
- [LWJGL](https://www.lwjgl.org/) — Lightweight Java Game Library

---

## License

MIT License — free for commercial and private use. See [LICENSE](LICENSE) for details.

---

## Support the Project

If FastRobot helps you build something awesome:
- **Star** the repository ⭐
- **Share** with other developers
- **Report** issues and suggest features
- **Contribute** improvements via pull requests

Your support helps maintain and improve FastRobot!

---

**Small package. Maximum speed. Zero bloat.** 🚀🤖

*Replace slow Java with ultra-fast native performance!*
