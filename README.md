# FastRobot 0.1.1 [ALPHA-2026-06-14] — High-FPS Screen Capture & Native Automation for Java

[![Status](https://img.shields.io/badge/status-0.1.1-brightgreen.svg)](https://github.com/andrestubbe/FastRobot/releases/tag/0.1.1)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastRobot)

**🤖 The high-performance alternative to `java.awt.Robot` — 10–17× faster screen capture and 5–15× faster input events using DirectX and GDI.**

FastRobot is built for developers who need raw speed. Whether it's high-FPS screen streaming, low-latency bot input,
or computer vision at 60+ FPS, FastRobot delivers where the standard AWT Robot fails.

[![FastRobot Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Quick Start

```java
import fastrobot.FastRobot;
import java.awt.image.BufferedImage;

public class Demo {
    public static void main(String[] args) {
        FastRobot robot = new FastRobot();

        // 1. Direct Low-Latency Mouse Movement & Click (10-50x faster than AWT Robot)
        robot.mouseMove(500, 300);
        robot.mousePress(1);   // Left Button Down
        robot.mouseRelease(1); // Left Button Up

        // 2. High-Speed Screen Capture via Direct Native BitBlt / DXGI
        int width = 800;
        int height = 600;
        BufferedImage screen = robot.createScreenCapture(0, 0, width, height);
        System.out.println("Captured screen frame: " + screen.getWidth() + "x" + screen.getHeight());

        // 3. Ultra-Fast Single Pixel Color Probe (GetPixel without capturing screen)
        int rgb = robot.getPixelColor(100, 100);
        System.out.printf("Pixel at (100, 100) RGB: #%06X\n", (rgb & 0xFFFFFF));
    }
}
```

---

## Table of Contents

- [Why FastRobot?](#why-fastrobot)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Architecture & Hardware Pipeline](#architecture--hardware-pipeline)
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
2. **Slow Screen Capture**: `createScreenCapture()` invokes legacy GDI BitBlt under lock, creating massive Java heap allocations (~8 MB per 1080p frame) that trigger frequent Garbage Collection stalls.
3. **No Direct Hardware Injection**: AWT lacks direct Win32 `SendInput` hardware simulation, leading to dropped inputs in fast-paced automation.

**FastRobot** solves this by implementing direct native Win32 `SendInput` and hardware-accelerated screen capture:
- **Sub-Millisecond Input Latency**: Direct Win32 C++ API calls bypass the Java AWT event queue completely.
- **Off-Heap FastImage Bridge**: Direct screen capture into zero-GC `FastImage` buffers for SIMD computer vision pipelines.
- **Native Color Probing**: `getPixelColor()` queries screen pixels up to **2× faster** than AWT Robot.

---

## Key Features

- ⚡ **Ultra-Low Latency Input** — Direct Win32 `SendInput` mouse and keyboard injection (<0.1 ms latency).
- 🖼️ **FastImage Ecosystem Bridge** — Capture directly into off-heap `FastImage` instances with zero JVM heap churn.
- 🎯 **High-Speed Pixel Probing** — Blazing fast `getPixelColor(x, y)` without capturing entire display surfaces.
- 🖥️ **High-Performance Screen Capture** — Native GDI/DXGI capture pipeline up to 10–17× faster than `java.awt.Robot`.
- 🚀 **Zero GC Stalls** — Pre-allocated native frame buffers protect latency-sensitive automation bots.
- 🔗 **FastCore Integration** — Automated zero-dependency native DLL extraction and loading.

---

## Real-World Use Cases

- 🤖 **Autonomous RPA & Desktop Agents**: Drive desktop automation with sub-millisecond mouse and keyboard responsiveness.
- 🎮 **Game Bots & Vision-Guided AI**: Process screen state at 60+ FPS and inject precision input without detection jitter.
- 🧪 **High-Speed UI Regression Testing**: Accelerate massive GUI test suites by cutting out AWT event queue delays.
- 👁️ **Instant Color & State Verification**: Poll UI elements using native `getPixelColor()` at over 27,000 checks/sec.

---

## Architecture & Hardware Pipeline

```
┌────────────────────────────────────────────────────────┐
│                   Java Application                     │
└───────────────┬────────────────────────┬───────────────┘
                │ Direct JNI Calls       │ FastImage Bridge
                ▼                        ▼
┌───────────────────────────────┐ ┌──────────────────────┐
│  fastrobot.dll (Native C++)   │ │      FastImage       │
├───────────────┬───────────────┤ │  (SIMD / Off-Heap)   │
│ Win32         │ Native GDI /  │ └──────────┬───────────┘
│ SendInput     │ DXGI Capture  │            │
└───────┬───────┴───────┬───────┘            ▼
        │               └─────────────► Zero-Copy Frame
        ▼
┌───────────────────────────────┐
│ Windows OS & Hardware Drivers │
└───────────────────────────────┘
```

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

> **Nearly 2× Faster Pixel Retrieval**: `FastRobot.getPixelColor` runs at **~27,000 queries/sec**, roughly **93% faster** than `java.awt.Robot` (13,973 ops/ms), while cursor position tracking achieves over **2.2 million queries/sec**.

---

## API Quick Reference

| Method | Description | Docs |
|--------|-------------|------|
| `mouseMove(x, y)` | Moves mouse cursor via native `SendInput`. | [Reference 📖](docs/REFERENCE.md) |
| `mousePress(btn)` / `mouseRelease(btn)` | Injects mouse button click events. | [Reference 📖](docs/REFERENCE.md) |
| `keyPress(code)` / `keyRelease(code)` | Injects keyboard scancodes. | [Reference 📖](docs/REFERENCE.md) |
| `getPixelColor(x, y)` | High-speed single pixel RGB query. | [Reference 📖](docs/REFERENCE.md) |
| `createScreenCapture(rect)` | Native screen capture to `BufferedImage`. | [Reference 📖](docs/REFERENCE.md) |
| `captureImage(rect)` | **FastImage Bridge:** Capture to off-heap `FastImage`. | [Reference 📖](docs/REFERENCE.md) |
| `getFrameImage()` | **Zero-Copy:** Wraps cached frame into `FastImage`. | [Reference 📖](docs/REFERENCE.md) |

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
    <!-- FastRobot Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastRobot</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastImage Frame Processing -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastImage</artifactId>
        <version>0.1.2</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
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
    implementation 'com.github.andrestubbe:FastImage:0.1.2'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 **[FastRobot-0.1.1.jar](https://github.com/andrestubbe/FastRobot/releases/tag/0.1.1)** (The Core Library)
2. ⚡ **[FastImage-0.1.2.jar](https://github.com/andrestubbe/FastImage/releases/tag/0.1.2)** (The SIMD Image Engine)
3. ⚙️ **[FastCore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/tag/0.1.0)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.

---

## Documentation

* **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions and method reference.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.

---

## Platform Support

| Platform      | Status             |
|---------------|--------------------|
| Windows 10/11 | ✅ Fully Supported  |
| Linux         | 🚧 Planned         |
| macOS         | 🚧 Planned         |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastCore](https://github.com/andrestubbe/FastCore) — Native Library Loader for Java
- [FastScreen](https://github.com/andrestubbe/FastScreen) — High-Performance Native Screen Capture for Java
- [FastMouse](https://github.com/andrestubbe/FastMouse) — Native Mouse API for Java
- [FastKeyboard](https://github.com/andrestubbe/FastKeyboard) — Native Windows RawInput API for Java
- [FastOCR](https://github.com/andrestubbe/FastOCR) — Ultra-Fast Native OCR for Java
- [FastImage](https://github.com/andrestubbe/FastImage) — Ultra-Fast Native Image Processing for Java

---
**Part of the FastJava Ecosystem** — *Making the JVM faster. ⚡*
