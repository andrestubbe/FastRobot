# FastRobot 0.1.0 [ALPHA-2026-06] � High-FPS Screen Capture & Native Automation for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastRobot/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastRobot)

---

**? The high-performance alternative to java.awt.Robot. Achieves 1017x faster screen capture and 515x faster input
events using DirectX and GDI.**

**FastRobot** is built for developers who need raw speed. Whether it's high-FPS screen streaming, low-latency bot input,
or computer vision at 60+ FPS, FastRobot delivers where the standard AWT Robot fails.

---

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Table of Contents

- [Features](#features)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Build from Source](#build-from-source)
- [License](#license)

---

## Features

- **âš¡ Ultra-Fast Capture**: 10-17x faster than java.awt.Robot using DirectX DXGI.
- **ðŸš€? Zero-Latency Input**: Native mouse and keyboard injection via DirectInput.
- **ðŸš€ Desktop Duplication**: 60+ FPS real-time desktop streaming.
- **ðŸš€ Zero GC Stalls**: Native memory buffers keep your Java heap clean.

## Quick Start

```bash
# Clone the repository
git clone https://github.com/andrestubbe/FastRobot.git

# Build the native bridge
cd FastRobot
.\compile.bat

# Launch the DesktopStreamDemo
.\run-demo.bat
```

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
    <artifactId>fastrobot</artifactId>
    <version>0.1.0</version>
</dependency>

<!-- FastCore (Required Native Loader) -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
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
    implementation 'com.github.andrestubbe:fastrobot:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ðŸš€ **[fastrobot-0.1.0.jar](https://github.com/andrestubbe/FastRobot/releases/download/0.1.0/fastrobot-0.1.0.jar)
   ** (The Core Library)
2. ðŸš€ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (
   The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.


---

## Documentation

* **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.

---

## Platform Support

| Platform      | Status            |
|---------------|-------------------|
| Windows 10/11 | ? Fully Supported |
| Linux         | ðŸš€ Planned        |
| macOS         | ðŸš€ Planned        |

---

## License

MIT License  See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastCore](https://github.com/andrestubbe/FastCore)  Native Library Loader for Java
- [FastRobot](https://github.com/andrestubbe/FastRobot)  High-performance RawInput engine
- [FastTheme](https://github.com/andrestubbe/FastTheme)  Advanced UI styling engine

---

**Part of the FastJava Ecosystem**  *Making the JVM faster. Small package. Maximum speed. Zero bloat. ðŸš€ðŸš€*




