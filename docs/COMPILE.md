# Building FastRobot 🛠️

Complete build guide for compiling the native C++ Win32 SendInput and DirectX capture engine and packaging the Java JAR.

---

## Prerequisites

* **Windows 10 or 11 (64-bit)**
* **JDK 17+** ([Eclipse Adoptium](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/))
* **Visual Studio 2022 or 2026** (Community, Professional, or Enterprise) with "Desktop development with C++" workload
* **Windows 10/11 SDK** (installed with Visual Studio)
* **Maven 3.9+**

---

## Automated One-Click Build

FastRobot includes an automated compilation script with Visual Studio and JDK discovery:

```cmd
# In the FastRobot repository root:
compile.bat
```

What `compile.bat` does automatically:
1. Detects Visual Studio 2026 / 2022 Community via `vswhere.exe`.
2. Initializes the 64-bit developer environment (`vcvars64.bat`).
3. Compiles `native/fastrobot.cpp` and `native/DXGICapture.cpp` with `/O2` and links Win32 user/GDI and DirectX libraries.
4. Deploys `fastrobot.dll` directly to:
   - `build/fastrobot.dll`
   - `src/main/resources/native/fastrobot.dll`
   - `%USERPROFILE%\.fastcore\native\fastrobot\fastrobot.dll`

---

## Maven Java Packaging

```bash
mvn clean install -DskipTests
```

---

## JMH Benchmarking

To build and execute the official JMH benchmark suite:

```cmd
run-benchmark.bat
```

---

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
