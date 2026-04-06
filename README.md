# FastRobot 🚀🤖 (WIP - still traveling to Maven)

**Ultra-fast replacement for `java.awt.Robot` - Small, fast, no bloat**

[![Maven Central](https://img.shields.io/maven-central/v/com.fastjava/fastrobot.svg)](https://search.maven.org/artifact/com.fastjava/fastrobot)
[![GitHub stars](https://img.shields.io/github/stars/andrestubbe/fastrobot.svg)](https://github.com/andrestubbe/fastrobot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## ⚡ One-Line Setup

```xml
<dependency>
    <groupId>com.fastjava</groupId>
    <artifactId>fastrobot</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
FastRobot robot = new FastRobot();
robot.mouseMove(100, 100);  // 1.41x faster
robot.mousePress(FastRobot.BUTTON1); // 515x faster!
```

## 🎯 Performance

| Operation | java.awt.Robot | FastRobot | Speedup |
|-----------|----------------|------------|----------|
| Mouse Click | 0.24ms/op | 0.0005ms/op | **515x** |
| Mouse Move | 0.29ms/op | 0.20ms/op | **1.41x** |
| Screen Capture | 138ms/op | 64ms/op | **2.17x** |
| Key Input | 0.21ms/op | 0.18ms/op | **1.24x** |

*All FastRobot methods are optimized for maximum performance - no confusing "Instant" variants needed!*

## 🚀 Features

- **Direct Hardware Access** - Zero latency mouse/keyboard (Windows)
- **Fast Screen Capture** - Optimized BitBlt operations
- **Simple API** - Same method names as java.awt.Robot, all ultra-fast
- **Maven Ready** - Easy dependency management
- **Zero Bloat** - Small, focused package

## 🌍 Platform Support

- **✅ Windows**: Full support with DirectInput/GDI

## 🎮 Use Cases

```java
FastRobot robot = new FastRobot();

// Fast screen capture
BufferedImage screen = robot.createScreenCapture(new Rectangle(0, 0, 800, 600));

// Gaming bots - instant response
robot.mouseMove(targetX, targetY);
robot.mousePress(FastRobot.BUTTON1);

// Automation - fast keyboard input
robot.keyPress(KeyEvent.VK_A);
robot.keyRelease(KeyEvent.VK_A);
```

## 🔧 Build from Source

```bash
git clone https://github.com/andrestubbe/fastrobot.git
cd fastrobot
# Windows (currently supported)
mvn compile
```

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

---

**Small package, maximum speed, zero bloat.** 🚀🤖

*Replace slow Java with ultra-fast native performance!* 🤖
