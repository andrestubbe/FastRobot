#include <windows.h>
#include <winuser.h>
#include <jni.h>
#include "fastrobot.h"

// Global variables for screen capture
HDC screenDC = NULL;
HDC memDC = NULL;
HBITMAP hBitmap = NULL;
HBITMAP hOldBitmap = NULL;

// JNI method implementations
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseMove(JNIEnv *env, jobject obj, jint x, jint y) {
    SetCursorPos(x, y);
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mousePress(JNIEnv *env, jobject obj, jint buttons) {
    INPUT input = {};
    input.type = INPUT_MOUSE;
    
    if (buttons & 1) { // BUTTON1 (left)
        input.mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
    } else if (buttons & 2) { // BUTTON2 (middle)
        input.mi.dwFlags = MOUSEEVENTF_MIDDLEDOWN;
    } else if (buttons & 4) { // BUTTON3 (right)
        input.mi.dwFlags = MOUSEEVENTF_RIGHTDOWN;
    }
    
    SendInput(1, &input, sizeof(INPUT));
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseRelease(JNIEnv *env, jobject obj, jint buttons) {
    INPUT input = {};
    input.type = INPUT_MOUSE;
    
    if (buttons & 1) { // BUTTON1 (left)
        input.mi.dwFlags = MOUSEEVENTF_LEFTUP;
    } else if (buttons & 2) { // BUTTON2 (middle)
        input.mi.dwFlags = MOUSEEVENTF_MIDDLEUP;
    } else if (buttons & 4) { // BUTTON3 (right)
        input.mi.dwFlags = MOUSEEVENTF_RIGHTUP;
    }
    
    SendInput(1, &input, sizeof(INPUT));
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseWheel(JNIEnv *env, jobject obj, jint wheelRotation) {
    INPUT input = {};
    input.type = INPUT_MOUSE;
    input.mi.dwFlags = MOUSEEVENTF_WHEEL;
    input.mi.mouseData = wheelRotation * WHEEL_DELTA;
    SendInput(1, &input, sizeof(INPUT));
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyPress(JNIEnv *env, jobject obj, jint keycode) {
    INPUT input = {};
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = keycode;
    input.ki.dwFlags = 0;
    SendInput(1, &input, sizeof(INPUT));
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyRelease(JNIEnv *env, jobject obj, jint keycode) {
    INPUT input = {};
    input.type = INPUT_KEYBOARD;
    input.ki.wVk = keycode;
    input.ki.dwFlags = KEYEVENTF_KEYUP;
    SendInput(1, &input, sizeof(INPUT));
}

JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getPixelColor(JNIEnv *env, jobject obj, jint x, jint y) {
    HDC hdc = GetDC(NULL);
    COLORREF color = GetPixel(hdc, x, y);
    ReleaseDC(NULL, hdc);
    
    // Convert COLORREF (BGR) to RGB int
    return GetRValue(color) << 16 | GetGValue(color) << 8 | GetBValue(color);
}

JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getScreenPixels(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height) {
    // Create Java int array for pixel data
    jintArray result = env->NewIntArray(width * height);
    if (result == NULL) {
        return NULL; // OutOfMemoryError already thrown
    }
    
    // Get screen DC
    HDC hdc = GetDC(NULL);
    
    // Create compatible DC and bitmap
    HDC memDC = CreateCompatibleDC(hdc);
    HBITMAP hBitmap = CreateCompatibleBitmap(hdc, width, height);
    HBITMAP hOldBitmap = (HBITMAP)SelectObject(memDC, hBitmap);
    
    // Copy screen region
    BitBlt(memDC, 0, 0, width, height, hdc, x, y, SRCCOPY);
    
    // Get pixel data
    jint* pixels = new jint[width * height];
    BITMAPINFO bmi = {};
    bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
    bmi.bmiHeader.biWidth = width;
    bmi.bmiHeader.biHeight = -height; // Top-down
    bmi.bmiHeader.biPlanes = 1;
    bmi.bmiHeader.biBitCount = 32;
    bmi.bmiHeader.biCompression = BI_RGB;
    
    GetDIBits(memDC, hBitmap, 0, height, pixels, &bmi, DIB_RGB_COLORS);
    
    // Convert BGR to RGB
    for (int i = 0; i < width * height; i++) {
        jint pixel = pixels[i];
        pixels[i] = ((pixel & 0xFF) << 16) | (pixel & 0xFF00) | ((pixel & 0xFF0000) >> 16);
    }
    
    // Copy to Java array
    env->SetIntArrayRegion(result, 0, width * height, pixels);
    
    // Cleanup
    delete[] pixels;
    SelectObject(memDC, hOldBitmap);
    DeleteObject(hBitmap);
    DeleteDC(memDC);
    ReleaseDC(NULL, hdc);
    
    return result;
}

JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenWidth(JNIEnv *env, jobject obj) {
    return GetSystemMetrics(SM_CXSCREEN);
}

JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenHeight(JNIEnv *env, jobject obj) {
    return GetSystemMetrics(SM_CYSCREEN);
}

// === v2.0: High-FPS Async Streaming Capture ===
#include "DXGICapture.h"
#include <thread>
#include <atomic>

static fastrobot::DXGICapture* g_capture = nullptr;
static std::thread g_captureThread;
static std::atomic<bool> g_isRunning(false);

static void CaptureLoop() {
    while (g_isRunning && g_capture) {
        g_capture->CaptureFrame();
        std::this_thread::sleep_for(std::chrono::milliseconds(1)); // Small delay to prevent CPU overload
    }
}

JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_startScreenStream(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height) {
    // Stop existing capture if any
    if (g_capture) {
        Java_fastrobot_FastRobot_stopScreenStream(env, obj);
    }
    
    // Create new capture instance
    g_capture = new fastrobot::DXGICapture();
    
    if (!g_capture->Initialize(x, y, width, height)) {
        delete g_capture;
        g_capture = nullptr;
        return JNI_FALSE;
    }
    
    if (!g_capture->StartCapture()) {
        delete g_capture;
        g_capture = nullptr;
        return JNI_FALSE;
    }
    
    // Start capture thread
    g_isRunning = true;
    g_captureThread = std::thread(CaptureLoop);
    
    return JNI_TRUE;
}

JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrame(JNIEnv *env, jobject obj) {
    if (!g_capture) {
        return nullptr;
    }
    
    fastrobot::FrameData frame = g_capture->GetNextFrame();
    if (frame.data.empty()) {
        return nullptr;
    }
    
    // Convert RGBA to RGB int array
    int pixelCount = frame.width * frame.height;
    jintArray result = env->NewIntArray(pixelCount);
    if (result == nullptr) {
        return nullptr;
    }
    
    jint* pixels = new jint[pixelCount];
    for (int i = 0; i < pixelCount; i++) {
        // Convert BGRA to RGB (ignore alpha)
        pixels[i] = (frame.data[i * 4 + 2] << 16) |  // R
                    (frame.data[i * 4 + 1] << 8)  |  // G
                    (frame.data[i * 4 + 0]);         // B
    }
    
    env->SetIntArrayRegion(result, 0, pixelCount, pixels);
    delete[] pixels;
    
    return result;
}

JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_hasNewFrame(JNIEnv *env, jobject obj) {
    if (!g_capture) {
        return JNI_FALSE;
    }
    return g_capture->HasNewFrame() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_fastrobot_FastRobot_stopScreenStream(JNIEnv *env, jobject obj) {
    if (g_capture) {
        g_isRunning = false;
        if (g_captureThread.joinable()) {
            g_captureThread.join();
        }
        delete g_capture;
        g_capture = nullptr;
    }
}

JNIEXPORT jdouble JNICALL Java_fastrobot_FastRobot_getStreamFPS(JNIEnv *env, jobject obj) {
    if (!g_capture) {
        return 0.0;
    }
    return g_capture->GetCurrentFPS();
}

// DLL entry point
BOOL APIENTRY DllMain(HMODULE hModule, DWORD ul_reason_for_call, LPVOID lpReserved) {
    switch (ul_reason_for_call) {
    case DLL_PROCESS_ATTACH:
        // Initialize screen capture resources
        screenDC = GetDC(NULL);
        memDC = CreateCompatibleDC(screenDC);
        break;
    case DLL_PROCESS_DETACH:
        // Cleanup screen capture resources
        if (hOldBitmap) SelectObject(memDC, hOldBitmap);
        if (hBitmap) DeleteObject(hBitmap);
        if (memDC) DeleteDC(memDC);
        if (screenDC) ReleaseDC(NULL, screenDC);
        break;
    }
    return TRUE;
}
