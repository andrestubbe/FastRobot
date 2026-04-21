/**
 * @file fastrobot.h
 * @brief FastRobot JNI Header - Native input automation and screen capture
 *
 * @details Cross-platform (Windows) robot implementation providing:
 * - Mouse control (move, click, wheel)
 * - Keyboard input (press, release)
 * - Screen capture (pixel, region, streaming)
 *
 * @par Features
 * - Native Windows SendInput API for reliability
 * - DXGI Desktop Duplication for 60-240 FPS streaming
 * - Hardware scaling for preview windows
 * - Multi-monitor support
 *
 * @par Architecture
 * - v2.0: Async streaming capture with triple buffering
 * - DXGI 1.2 for GPU-accelerated screen capture
 * - Separate thread for capture loop
 *
 * @par Platform Requirements
 * - Windows 8+ for DXGI features
 * - Standard Win32 API for input
 *
 * @author FastJava Team
 * @version 2.1.0
 * @copyright MIT License
 */

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

/** @defgroup Mouse Mouse Operations
 *  @brief Cursor control and button state
 *  @{ */
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseMove(JNIEnv *env, jobject obj, jint x, jint y);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseMoveRelative(JNIEnv *env, jobject obj, jint dx, jint dy);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mousePress(JNIEnv *env, jobject obj, jint buttons);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseRelease(JNIEnv *env, jobject obj, jint buttons);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseWheel(JNIEnv *env, jobject obj, jint wheelRotation);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getMousePosition(JNIEnv *env, jobject obj);

/** @} */

/** @defgroup Keyboard Keyboard Operations
 *  @brief Key press and release simulation
 *  @{ */
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyPress(JNIEnv *env, jobject obj, jint keycode);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyRelease(JNIEnv *env, jobject obj, jint keycode);

/** @} */

/** @defgroup Capture Screen Capture
 *  @brief Single-frame capture operations
 *  @{ */
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getPixelColor(JNIEnv *env, jobject obj, jint x, jint y);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getScreenPixels(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height);

/** @} */

/** @defgroup Screen Screen Information
 *  @brief Display dimensions
 *  @{ */
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenWidth(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenHeight(JNIEnv *env, jobject obj);

/** @} */

/** @defgroup Streaming Async Streaming Capture
 *  @brief High-FPS continuous capture (60-240 FPS)
 *  @details DXGI Desktop Duplication with hardware scaling
 *  @{ */

/** @name Streaming Control (v2.0+) */
JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_startScreenStream(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrame(JNIEnv *env, jobject obj);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrameScaled(JNIEnv *env, jobject obj, jint scaleWidth, jint scaleHeight);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrameScaledAA(JNIEnv *env, jobject obj, jint scaleWidth, jint scaleHeight, jboolean useAA);
JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_hasNewFrame(JNIEnv *env, jobject obj);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_stopScreenStream(JNIEnv *env, jobject obj);
JNIEXPORT jdouble JNICALL Java_fastrobot_FastRobot_getStreamFPS(JNIEnv *env, jobject obj);

/** @} */

#ifdef __cplusplus
}
#endif
