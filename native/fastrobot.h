#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// Mouse operations
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseMove(JNIEnv *env, jobject obj, jint x, jint y);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mousePress(JNIEnv *env, jobject obj, jint buttons);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseRelease(JNIEnv *env, jobject obj, jint buttons);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_mouseWheel(JNIEnv *env, jobject obj, jint wheelRotation);

// Keyboard operations
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyPress(JNIEnv *env, jobject obj, jint keycode);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_keyRelease(JNIEnv *env, jobject obj, jint keycode);

// Screen capture operations
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getPixelColor(JNIEnv *env, jobject obj, jint x, jint y);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getScreenPixels(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height);

// Screen info
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenWidth(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_fastrobot_FastRobot_getScreenHeight(JNIEnv *env, jobject obj);

// v2.0: High-FPS Async Streaming Capture (60fps-240fps)
JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_startScreenStream(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrame(JNIEnv *env, jobject obj);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrameScaled(JNIEnv *env, jobject obj, jint scaleWidth, jint scaleHeight);
JNIEXPORT jintArray JNICALL Java_fastrobot_FastRobot_getNextFrameScaledAA(JNIEnv *env, jobject obj, jint scaleWidth, jint scaleHeight, jboolean useAA);
JNIEXPORT jboolean JNICALL Java_fastrobot_FastRobot_hasNewFrame(JNIEnv *env, jobject obj);
JNIEXPORT void JNICALL Java_fastrobot_FastRobot_stopScreenStream(JNIEnv *env, jobject obj);
JNIEXPORT jdouble JNICALL Java_fastrobot_FastRobot_getStreamFPS(JNIEnv *env, jobject obj);

#ifdef __cplusplus
}
#endif
