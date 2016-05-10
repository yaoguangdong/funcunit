#include "../common/clog.h"
#include "statlib.h"
#include <jni.h>

jstring Java_com_yaogd_nativ_LoadNative_get( JNIEnv* env, jobject thiz)
{
	int v = add(2, 7) ;
	LOGI("call static lib method add(x, y) =:%d", v);
    return (*env)->NewStringUTF(env, "static call !  Compiled with ABI " ABI ".");
}

