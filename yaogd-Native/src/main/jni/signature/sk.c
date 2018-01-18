#include <string.h>
#include <jni.h>
#include "../common/clog.h"

jstring Java_com_yaogd_nativ_ui_Native01_getSecretKey( JNIEnv* env, jobject thiz)
{
	jclass cls = (*env)->FindClass(env, "android/content/ContextWrapper");
	//this.getPackageManager();
	jmethodID mid = (*env)->GetMethodID(env, cls, "getPackageManager",
			"()Landroid/content/pm/PackageManager;");
	if (mid == NULL) {
		return (*env)->NewStringUTF(env, "-1");
	}

	jobject pm = (*env)->CallObjectMethod(env, thiz, mid);
	if (pm == NULL) {
		return (*env)->NewStringUTF(env, "-2");
	}

	//this.getPackageName();
	mid = (*env)->GetMethodID(env, cls, "getPackageName", "()Ljava/lang/String;");
	if (mid == NULL) {
		return (*env)->NewStringUTF(env, "-3");
	}

	jstring packageName = (jstring)(*env)->CallObjectMethod(env, thiz, mid);

	// packageManager->getPackageInfo(packageName, GET_SIGNATURES);
	cls = (*env)->GetObjectClass(env, pm);
	mid  = (*env)->GetMethodID(env, cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
	jobject packageInfo = (*env)->CallObjectMethod(env, pm, mid, packageName, 0x40); //GET_SIGNATURES = 64;
	cls = (*env)->GetObjectClass(env, packageInfo);
	jfieldID fid = (*env)->GetFieldID(env, cls, "signatures", "[Landroid/content/pm/Signature;");
	jobjectArray signatures = (jobjectArray)(*env)->GetObjectField(env, packageInfo, fid);
	jobject sig = (*env)->GetObjectArrayElement(env, signatures, 0);

	cls = (*env)->GetObjectClass(env, sig);
	mid = (*env)->GetMethodID(env, cls, "hashCode", "()I");
	int sig_value = (int)(*env)->CallIntMethod(env, sig, mid);

	if(sig_value == -1352853962){
		LOGI("legal signature for debug:%d", sig_value);
		return (*env)->NewStringUTF(env, "305CF3900351DDFFF9");
	}else if(sig_value == 1721194776){
		return (*env)->NewStringUTF(env, "305CF3900351DDFFF9");
	}else{
		LOGW("illegal signature");
		return (*env)->NewStringUTF(env, "0");
	}

}
