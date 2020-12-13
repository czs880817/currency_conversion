#include <jni.h>
#include <android/log.h>

#define TAG "ACCESS_KEY_JNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_cz_currency_1conversion_server_AccessKey_getAccessKey(JNIEnv *env, jobject clazz) {
    LOGD("#getAccessKey in JNI is called");
    return (*env)->NewStringUTF(env, "99e6c98107fa76894460e33e6db8f325");
}