# FastRobot 0.1.1 [ALPHA-2026-09-04] — Low-Latency Native Automation & Bot Substrate for Java

[![Status](https://img.shields.io/badge/status-0.1.1-brightgreen.svg)](https://github.com/andrestubbe/FastRobot/releases/tag/0.1.1)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastRobot)

---

**🤖 The high-performance alternative to `java.awt.Robot` — sub-millisecond native input injection, microsecond pixel probing, and zero-allocation FastImage & FastScreen bridge.**

**FastRobot** is the native automation, input injection, and bot-control substrate of the **FastJava** ecosystem. Where standard Java `Robot` introduces severe AWT Event Dispatch Thread (EDT) jitter, slow synchronous GDI locks, and garbage collection stalls, FastRobot communicates directly with the Windows OS input subsystem via native Win32 `SendInput`.

For vision-guided automation, FastRobot seamlessly bridges with **[FastImage](https://github.com/andrestubbe/FastImage)** for zero-allocation SIMD image processing and partners with **[FastScreen](https://github.com/andrestubbe/FastScreen)**—the dedicated 240–2000 FPS DirectX DXGI desktop duplication engine—to provide a complete, ultra-fast robotics and automation stack.

[**Watch Showcase Demo (YouTube)**](https://youtu.be/DWSC35M_mdI)

[![FastRobot Showcase](docs/screenshot.png)](https://youtu.be/DWSC35M_mdI)

---

## Quick Start

```java
import fastrobot.FastRobot;
import fastimage.FastImage;
import java.awt.Rectangle;

public class Demo {
    public static void main(String[] args) {
        FastRobot robot = new FastRobot();

        // 1. Direct Low-Latency Mouse Movement & Click (bypasses AWT EDT queue)
        robot.mouseMove(500, 300);
        robot.mousePress(1);   // Left Button Down
        robot.mouseRelease(1); // Left Button Up

        // 2. Ultra-Fast Single Pixel Color Probe (direct Win32 GetPixel without screen dumps)
        int rgb = robot.getPixelColor(100, 100);
        System.out.printf("Pixel at (100, 100) RGB: #%06X%n", (rgb & 0xFFFFFF));

        // 3. FastImage Bridge: Capture directly to off-heap SIMD image (zero heap GC)
        FastImage targetArea = robot.captureImage(0, 0, 400, 300);
        if (targetArea != null) {
            System.out.println("Captured FastImage bounds: " + targetArea.getWidth() + "x" + targetArea.getHeight());
            targetArea.dispose();
        }

        // NOTE: For dedicated 240-2000 FPS desktop streaming with DXGI hardware acceleration,
        // use FastRobot's companion module: FastScreen!
    }
}
```

---

## Table of Contents

- [Why FastRobot?](#why-fastrobot)
- [Ecosystem Architecture (FastRobot + FastScreen + FastImage)](#ecosystem-architecture)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastRobot?

Standard Java `java.awt.Robot` was designed in the late 1990s and has severe limitations for modern robotics, high-FPS automation, and vision applications:

1. **AWT Event Queue Bottlenecks**: Input events (`mouseMove`, `keyPress`) are dispatched through the AWT Event Dispatch Thread (EDT) and OS message queues with noticeable latency (5–15 ms jitter).
2. **Massive GC Stalls on Screen Dumps**: `createScreenCapture()` copies GDI bitmaps onto the Java heap (~8 MB per 1080p frame), triggering frequent Garbage Collection pauses that freeze automation loops.
3. **No Direct Hardware Injection**: AWT lacks direct Win32 `SendInput` hardware simulation, leading to dropped or desynchronized inputs in fast-paced scenarios.

**FastRobot** redefines Java automation by focusing on what matters:

- **Sub-Millisecond Input Latency**: Direct native Win32 `SendInput` calls bypass the JVM AWT event queue entirely (<0.1 ms execution).
- **FastImage Ecosystem Bridge**: Native screen captures can be delivered directly into off-heap `FastImage` buffers (`captureImage()`, `getFrameImage()`) with zero heap churn.
- **Microsecond Color Probing**: `getPixelColor()` queries screen pixels up to **2× faster** than AWT Robot without locking or copying full screen surfaces.
- **Clean Decoupling with FastScreen**: While FastRobot provides built-in GDI capture convenience, high-throughput DirectX 11 / DXGI Desktop Duplication (240–2000 FPS) is delegated to **[FastScreen](https://github.com/andrestubbe/FastScreen)**, giving you the fastest possible screen-reading pipeline in the JVM.

---

## Ecosystem Architecture

FastRobot works harmoniously with the FastJava perception and manipulation modules:

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Autonomous Bot / Vision Agent                        │
└────────────────┬───────────────────────────────────────┬────────────────┘
                 │ 1. Decision & Input Actions           │ 2. Visual Feedback
                 ▼                                       ▼
 ┌───────────────────────────────┐       ┌───────────────────────────────┐
 │           FastRobot           │       │           FastScreen          │
 │  • Win32 SendInput (<0.1ms)   │       │  • DXGI 1.2 Desktop Dup       │
 │  • Instant getPixelColor()    │       │  • 240–2000 FPS Zero-Copy     │
 │  • Mouse / Keyboard Injection │       │  • Window Mirror Exclusion    │
 └───────────────┬───────────────┘       └───────────────┬───────────────┘
                 │                                       │ Native Frame Addr
                 ▼                                       ▼
 ┌───────────────────────────────┐       ┌───────────────────────────────┐
 │        Windows OS Subsystem   │       │           FastImage           │
 │  (Hardware Input / Message)   │       │  • SIMD Resizing & Filtering  │
 └───────────────────────────────┘       │  • Zero-GC Off-Heap Container │
                                         └───────────────────────────────┘
```

---

## Key Features

- ⚡ **Ultra-Low Latency Input** — Direct Win32 `SendInput` mouse and keyboard injection (<0.1 ms latency).
- 🖼️ **FastImage Bridge** — Capture directly into off-heap `FastImage` instances (`captureImage()`, `getFrameImage()`) with zero JVM heap churn.
- 🎯 **High-Speed Pixel Probing** — Blazing fast `getPixelColor(x, y)` running at >27,000 queries/second.
- 🖥️ **Integrated Screen Convenience** — Built-in fast region capture for lightweight checks; seamlessly links with `FastScreen` for continuous 240+ FPS streams.
- 🚀 **Zero GC Stalls** — Avoids Java heap allocations in hot automation paths.
- 🔗 **FastCore Integration** — Automated zero-dependency native DLL extraction and loading.

---

## Real-World Use Cases

- 🤖 **Autonomous RPA & Desktop Agents**: Drive desktop automation with sub-millisecond mouse and keyboard responsiveness.
- 🎮 **Game Bots & Vision-Guided AI**: Process screen state via `FastScreen` / `FastImage` and inject precision inputs via `FastRobot` without detection jitter.
- 🧪 **High-Speed UI Regression Testing**: Accelerate massive GUI test suites by cutting out AWT event queue delays.
- 👁️ **Instant Color & State Verification**: Poll UI elements and trigger buttons using native `getPixelColor()` at over 27,000 checks/sec.

---

## Performance Benchmarks

Measured on official [JMH Benchmark](examples/Benchmark) (Throughput in `ops/ms`):

```text
Benchmark                                      Mode  Cnt      Score   Error   Units
Benchmark.benchmarkFastRobotGetPixelColor     thrpt    3     27.021          ops/ms
Benchmark.benchmarkAwtRobotGetPixelColor      thrpt    3     13.973          ops/ms
Benchmark.benchmarkFastRobotGetMousePosition  thrpt    3   2218.287          ops/ms
Benchmark.benchmarkFastRobotScreenDimensions  thrpt    3  18872.366          ops/ms
```

> [!NOTE]
> **Environment & Setup**: Measured on Windows 11 (x64), JDK 21. `FastRobot.getPixelColor` runs at **~27,000 queries/sec**, roughly **93% faster** than `java.awt.Robot` (13,973 ops/ms), while cursor position tracking achieves over **2.2 million queries/sec**.

---

## API Quick Reference

| Method | Return Type | Description | Docs |
|:---|:---|:---|:---|
| `mouseMove(x, y)` | `void` | Moves mouse cursor via native `SendInput`. | [Reference](docs/REFERENCE.md#mouse--keyboard-input) |
| `mousePress(btn)` / `mouseRelease(btn)` | `void` | Injects mouse button click events. | [Reference](docs/REFERENCE.md#mouse--keyboard-input) |
| `keyPress(code)` / `keyRelease(code)` | `void` | Injects keyboard scancodes. | [Reference](docs/REFERENCE.md#mouse--keyboard-input) |
| `getPixelColor(x, y)` | `int` | High-speed single pixel RGB query without full screen capture. | [Reference](docs/REFERENCE.md#screen-capture) |
| `captureImage(rect)` | `FastImage` | **FastImage Bridge:** Capture region directly to off-heap `FastImage`. | [Reference](docs/REFERENCE.md#fastimage-ecosystem-bridge) |
| `captureImage(x, y, w, h)` | `FastImage` | **FastImage Bridge:** Capture with primitive coordinates to `FastImage`. | [Reference](docs/REFERENCE.md#fastimage-ecosystem-bridge) |
| `getFrameImage()` | `FastImage` | **FastImage Bridge:** Wraps streaming frame into `FastImage`. | [Reference](docs/REFERENCE.md#fastimage-ecosystem-bridge) |
| `createScreenCapture(rect)` | `BufferedImage` | Native screen capture to standard `BufferedImage`. | [Reference](docs/REFERENCE.md#screen-capture) |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastRobot - Low-Latency Native Input & Bot Automation -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastRobot</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastScreen - Dedicated High-FPS DXGI Screen Capture Engine (Optional / Companion) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastScreen</artifactId>
        <version>0.1.4</version>
    </dependency>

    <!-- FastImage - Zero-Copy Frame Container & SIMD Processing -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastImage</artifactId>
        <version>0.1.2</version>
    </dependency>

    <!-- FastCore - Required Native Loader -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastRobot:0.1.1'
    implementation 'com.github.andrestubbe:FastScreen:0.1.4'
    implementation 'com.github.andrestubbe:FastImage:0.1.2'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 **[FastRobot-0.1.1.jar](https://github.com/andrestubbe/FastRobot/releases/tag/0.1.1)** (The Core Automation Library)
2. 🖥️ **[FastScreen-0.1.4.jar](https://github.com/andrestubbe/FastScreen/releases/tag/0.1.4)** (Dedicated 240–2000 FPS Screen Capture)
3. ⚡ **[FastImage-0.1.2.jar](https://github.com/andrestubbe/FastImage/releases/tag/0.1.2)** (The SIMD Image Engine)
4. ⚙️ **[FastCore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/tag/0.1.0)** (The Mandatory Native Loader)

---

## Documentation

- **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
- **[REFERENCE.md](docs/REFERENCE.md)**: Comprehensive API specification, robotics control, and pixel capture methods.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation automation performance.
- **[ROADMAP.md](docs/ROADMAP.md)**: Planned milestone features and performance extensions.
- **[CHANGELOG.md](docs/CHANGELOG.md)**: Complete version history and release notes.

---

## Platform Support

| Platform | Architecture | Status | Driver / Subsystem |
|:---|:---:|:---:|:---|
| **Windows 10 / 11** | x64 | ✅ Fully Supported | Native Win32 `SendInput` & DXGI Desktop Duplication |
| **Linux** | x64 / AArch64 | 🚧 Planned | `uinput` / `XTest` & PipeWire / Wayland Portal |
| **macOS** | Apple Silicon / x64 | 🚧 Planned | `CGEventCreate` & ScreenCaptureKit / CoreGraphics |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- **[`FastCore`](https://github.com/andrestubbe/FastCore)** — Native Library Loader & JNI Utilities for Java
- **[`FastScreen`](https://github.com/andrestubbe/FastScreen)** — High-Performance Native DXGI Screen Capture for Java (240–2000 FPS)
- **[`FastImage`](https://github.com/andrestubbe/FastImage)** — Ultra-Fast Native SIMD Image Processing for Java
- **[`FastMouse`](https://github.com/andrestubbe/FastMouse)** — Ultra-Low Latency Native RawInput Mouse Engine
- **[`FastKeyboard`](https://github.com/andrestubbe/FastKeyboard)** — Ultra-Fast Native RawInput Keyboard Engine
- **[`FastHotkey`](https://github.com/andrestubbe/FastHotkey)** — Low-Latency Global Hotkey API for Java
- **[`FastOCR`](https://github.com/andrestubbe/FastOCR)** — Ultra-Fast Native OCR for Java

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.* 🚀
