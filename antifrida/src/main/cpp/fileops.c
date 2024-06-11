#include <jni.h>
#include <unistd.h>

JNIEXPORT jboolean JNICALL
Java_com_tg_antifrida_BaseScanner_nativeFileExists(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = (*env)->GetStringUTFChars(env, path_, 0);

    int result = access(path, F_OK);

    (*env)->ReleaseStringUTFChars(env, path_, path);
    return result == 0;
}
