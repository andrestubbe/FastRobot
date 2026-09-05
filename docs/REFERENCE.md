# FastRobot Reference

## 1. CPU Feature Model
*   **AVX2** — detected via CPUID. Enables 32-byte vector ops.
*   **SSE4.2** — detected via CPUID. 16-byte fallback.
*   **Fallback rule**: AVX2 → SSE4.2 → scalar.

## 2. API Specification

### Mouse & Keyboard Input
- `void mouseMove(int x, int y)`: Moves mouse cursor to coordinates using native `SendInput` (bypasses AWT EDT overhead).
- `void mousePress(int buttons)` / `void mouseRelease(int buttons)`: Injects mouse button press/release events.
- `void mouseWheel(int wheelAmt)`: Injects mouse scroll events.
- `void keyPress(int keycode)` / `void keyRelease(int keycode)`: Injects keyboard scancodes.

### Screen Capture
- `BufferedImage createScreenCapture(Rectangle screenRect)`: Captures screen region as standard `BufferedImage`.
- `BufferedImage createScreenCapture(int x, int y, int w, int h)`: Captures screen region with primitive coordinates.
- `int getPixelColor(int x, int y)`: Blazing fast single pixel RGB query via native `GetPixel`.

### FastImage Ecosystem Bridge
- `FastImage captureImage(Rectangle rect)`: Direct native screen capture returning an off-heap `FastImage`.
- `FastImage captureImage(int x, int y, int w, int h)`: Direct coordinate-based capture to `FastImage`.
- `FastImage getFrameImage()`: Zero-copy wrapping of cached internal capture frame into `FastImage`.

## 3. Guarantees & Contracts
*   **Low Latency**: Direct Win32 `SendInput` execution (< 0.1 ms latency).
*   **Zero GC Pressure**: Screen capture buffer reuse avoids JVM heap churn.
*   **Thread-Safety**: Native operations are fully thread-safe.

## 4. Platform Support
| Platform | Status |
|----------|--------|
| Windows 10/11 (x64) | ✅ Fully Supported |

---
**Part of the FastJava Ecosystem** — *Making the JVM faster.*

Made with ⚡ by Andre Stubbe