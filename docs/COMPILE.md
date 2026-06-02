# Building FastRobot

Guide for developers building FastRobot from source.

## Prerequisites

- **JDK 17+** — [Download](https://adoptium.net/)
- **Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- **Visual Studio 2019 or 2022** — [Download](https://visualstudio.microsoft.com/downloads/)
  - Required: "Desktop development with C++" workload
  - Required: Windows 10/11 SDK

## Quick Build

```bash
# Clone repository
git clone https://github.com/andrestubbe/fastrobot.git
cd fastrobot

# Build native DLL + Java
compile.bat
mvn clean package
```

## Build Commands

| Command | Purpose |
|---------|---------|
| `compile.bat` | Build native DLL (requires Visual Studio) |
| `mvn clean compile` | Compile Java only |
| `mvn clean package` | Build JAR with DLL |
| `mvn clean package -DskipTests` | Fast build |

## Running Examples

All examples are in `examples/00-basic-usage/`:

```bash
cd examples/00-basic-usage
mvn compile exec:java
```

## Native Code Structure

```
native/
├── fastrobot.cpp           # Main JNI implementation
├── fastrobot.h             # JNI header
├── DXGICapture.cpp         # DirectX capture
└── DXGICapture.h           # DirectX header
```

## Troubleshooting

**"Cannot find cl.exe"** — Run in "Developer Command Prompt for VS 2019/2022"

**"UnsatisfiedLinkError: no fastrobot in java.library.path"** — Run `compile.bat` first

**"DXGI_ERROR_NOT_FOUND"** — Ensure Windows 10/11 with DirectX 11+ GPU
