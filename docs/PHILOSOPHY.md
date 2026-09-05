# The Philosophy of FastRobot 💡

> [!IMPORTANT]
> **"Keine Kopien. Niemals. Kritischer JNI-Pfad. Native-First Performance."**

FastRobot is built on the conviction that desktop automation, bot control, and UI interaction in Java should never be bottlenecked by legacy AWT event queue delays, high-latency input simulation, or garbage-producing screen captures.

## Core Tenets

1.  **Direct Win32 Hardware Simulation**
    Bypass the Java AWT Event Dispatch Thread (EDT) completely by executing low-latency mouse and keyboard input directly through the native Win32 `SendInput` API (<0.1 ms latency).

2.  **Ultra-Fast Native Screen Capture**
    Provide native DirectX DXGI and GDI DIBSection capture pipelines delivering 10–17× higher throughput than `java.awt.Robot.createScreenCapture()`.

3.  **Zero JVM Garbage Churn**
    Eliminate the multi-megabyte heap allocations of standard `BufferedImage` captures through reusable native frame buffers and direct off-heap memory mapping.

4.  **Ecosystem Synergy with FastImage**
    Bridge captured screen regions directly into `FastImage` instances without copying memory, unlocking immediate AVX2 SIMD computer vision preprocessing.

5.  **FastJava Blueprint Consistency**
    As part of the **FastJava** ecosystem:
    *   **Native Backend**: Direct C++ implementation with Win32 SendInput and DXGI.
    *   **Unified Loading**: Powered by `FastCore` for seamless zero-dependency deployment.
    *   **Production Quality**: High responsiveness, deterministic execution, and thorough JMH verification.

---
**⚡ FastRobot — Powering the next generation of Native Java.**
