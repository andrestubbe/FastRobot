# FastRobot Roadmap 🗺️

**Vision:** To provide the fastest possible native primitives for robot automation by aggressively bypassing bottlenecks in standard Java.

## 🟢 v0.1.0: Initial Release (Completed)
- [x] **Core Native Engine**: Basic JNI implementation for Win32 SendInput and screen capture.
- [x] **Blueprint Standards**: README, Reference, and Philosophy integration.

## 🟢 v0.1.1: FastImage Interop & Modern Toolchain (Current)
- [x] **FastImage Ecosystem Integration**: Direct `captureImage(...)` and zero-copy `getFrameImage()` bridge.
- [x] **Visual Studio 2026/2022 Auto-Detection**: Universal build automation via `vswhere.exe`.
- [x] **JMH Benchmark Suite**: Official comparative benchmark suite against `java.awt.Robot`.

## 🟡 v0.2.0: Optimization Phase
- [ ] **SIMD Acceleration**: Implement AVX2/SSE4.2 paths for core loops.
- [ ] **Software Prefetching**: Optimize memory access patterns.
- [ ] **Alignment Enforcement**: Ensure zero-penalty memory boundaries.

## 🟠 v0.5.0: Platform & Logic Expansion
- [ ] **ARM NEON Port**: Parity for Apple Silicon/Mobile.
- [ ] **Advanced Features**: Multi-threaded paths and complex batch operations.

## 🔴 v1.0.0: Production Hardening
- [ ] **Full Stability Audit**: Long-run stress testing.
- [ ] **Enterprise Support**: NUMA-awareness and Large Pages support.

---
**Focus:** Performance is our USP. We optimize where Java stops.