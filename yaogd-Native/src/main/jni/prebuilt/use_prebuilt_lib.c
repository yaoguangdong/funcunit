#include "../common/clog.h"
#include <jni.h>

jstring Java_com_yaogd_nativ_LoadNative_get( JNIEnv* env, jobject thiz)
{
	int v = change(9, 3) ;
	LOGI("call prebuilt lib method change(x, y) =:%d", v);
    return (*env)->NewStringUTF(env, "static call !  Compiled with ABI " ABI ".");
}

