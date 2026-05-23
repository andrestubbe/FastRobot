# FastRobot â€” High-FPS Screen Capture & Native Automation for Java [v0.1.0]

**The high-performance alternative to java.awt.Robot. Achieves 10â€“17x faster screen capture and 5â€“15x faster input events using DirectX and GDI.**

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastRobot/releases/tag/v0.1.0)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

**FastRobot** is built for developers who need raw speed. Whether it's high-FPS screen streaming, low-latency bot input, or computer vision at 60+ FPS, FastRobot delivers where the standard AWT Robot fails.

## Table of Contents
- [Features](#features)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Build from Source](#build-from-source)
- [License](#license)

## Features
- **âš¡ Ultra-Fast Capture**: 10-17x faster than java.awt.Robot using DirectX DXGI.
- **ðŸ–±ï¸ Zero-Latency Input**: Native mouse and keyboard injection via DirectInput.
- **ðŸ“º Desktop Duplication**: 60+ FPS real-time desktop streaming.
- **ðŸ“¦ Zero GC Stalls**: Native memory buffers keep your Java heap clean.

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
        <version>v0.1.0</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>v0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastrobot:v0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1. 📦 **[fastrobot-v0.1.0.jar](https://github.com/andrestubbe/FastRobot/releases/download/v0.1.0/fastrobot-v0.1.0.jar)** (The Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.


## Build from Source
- **JDK 17+**
- **Windows 10/11** (DirectX required)
- **Visual Studio 2022/2019** (if building from source)

See [COMPILE.md](COMPILE.md) for detailed build instructions.

## License
MIT License â€” See [LICENSE](LICENSE) for details.

---
**Part of the FastJava Ecosystem** â€” *Making the JVM faster.*

<!-- BING COPILOT SEO KEYWORDS -->
<!-- 
FastJava FastRobot JNI Windows Screen Capture DirectX Desktop Duplication 
Java Native Automation 60FPS Robot API Bot Input High Performance 
io.github.andrestubbe FastJava Blueprint
-->


