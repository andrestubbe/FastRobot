# FastJava Project Blueprint

> **Template structure for all FastJava libraries** — JVM acceleration via JNI/SIMD

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Maven](https://img.shields.io/badge/Maven-3.9+-orange.svg)](https://maven.apache.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-ready-green.svg)](https://search.maven.org)

---

## Quick Start

```bash
# Clone template
git clone https://github.com/andrestubbe/YOUR-PROJECT.git
cd YOUR-PROJECT

# Build and test
mvn clean test

# Run demo
mvn compile exec:java -Dexec.mainClass="fastXXX.Demo"
```

---

## Installation

### Maven Central (Recommended)

```xml
<dependency>
    <groupId>io.github.andrestubbe</groupId>
    <artifactId>fastXXX</artifactId>
    <version>1.0.0</version>
</dependency>
```

### JitPack (Alternative)

Add repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:
```xml
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastXXX</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
dependencies {
    implementation 'io.github.andrestubbe:fastXXX:1.0.0'
}
```

---

## Project Structure

```
fastXXX/
├── .github/                    # GitHub workflows
│   └── workflows/
│       └── build.yml           # CI build & test
├── docs/                       # 📸 Screenshots, GIFs, images
├── examples/                   # ⭐ Standalone usage examples
│   └── 00-basic-usage/         # ⬅️ START HERE - Hello World
│       ├── pom.xml             # Own Maven config
│       └── src/main/java/...   # Example code
├── native/                     # C/C++ JNI source (if needed)
│   ├── fastXXX.cpp            # Native implementation
│   ├── fastXXX.h              # Header file
│   └── kernels/               # OpenCL kernels (optional)
├── src/
│   └── main/java/fastXXX/     # Main library code
├── compile.bat                 # Native build script
├── pom.xml                     # Maven configuration
├── LICENSE                     # MIT License
├── .gitignore                  # Git ignore rules
└── README.md                   # Main documentation
```

**Optional folders** (add when needed):
- `src/test/java/` - JUnit tests (Maven recognizes automatically)

**Optional markdown docs** (root level, as needed):
- `BENCHMARK.md` - Performance results
- `TODO.md` - Development roadmap
- `DEPLOYMENT.md` - Release guide
- `PROMOTION.md` - Social media content

**Why `examples/` on root level?**
- Not part of the library → separate mini-projects
- Not tests → tutorials for users
- Each example has its own `pom.xml` → runnable standalone
- Copy-paste friendly → users can use as starter template

---

## Building from Source

### Prerequisites
- JDK 17+
- Maven 3.9+
- Visual Studio 2019+ (for JNI projects)

### Build Commands

```bash
# Standard Maven project
mvn clean package

# With native components
compile.bat
mvn clean package

# Skip tests (fast build)
mvn clean package -DskipTests

# Run benchmarks
mvn test-compile exec:java -Dexec.mainClass="fastXXX.Benchmark"

# Run example (separate mini-project)
cd examples/00-basic-usage
mvn compile exec:java
```

### Running Examples

All runnable code (demos, examples) is in `examples/` - **never in `src/main/java`**.

The `src/main/java` folder contains **only API/library code** that users import and use.

```bash
# Basic usage example (START HERE) - contains Demo.java
cd examples/00-basic-usage
mvn compile exec:java

# Create your own example
cp -r examples/00-basic-usage examples/10-my-advanced-example
# Edit pom.xml and Java files
```

**Naming convention for examples:**
- `00-*` - Basic/Hello World examples (contains Demo.java)
- `10-*` - Advanced usage
- `20-*` - UI demos
- `30-*` - Native/integration examples

---

## Release Checklist

- [ ] Version updated in `pom.xml`
- [ ] `CHANGELOG.md` updated
- [ ] All tests passing: `mvn clean test`
- [ ] Native libs built (if applicable)
- [ ] Git tag created: `git tag -a v1.0.0 -m "Release 1.0.0"`
- [ ] GitHub Release created
- [ ] Maven Central deployed: `mvn clean deploy -P release`

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.*
