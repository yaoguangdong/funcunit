# the set of ndk samples reorganize by yaoguangdong

LOCAL_PATH := $(call my-dir)

############################### secret key build #######################
include $(CLEAR_VARS)
LOCAL_MODULE    := sk
LOCAL_SRC_FILES := signature/sk.c
LOCAL_LDLIBS    := -llog
include $(BUILD_SHARED_LIBRARY)

############################### static lib build #######################

include $(CLEAR_VARS)
LOCAL_MODULE    := statlib
LOCAL_SRC_FILES := native-test/statlib.c
include $(BUILD_STATIC_LIBRARY)

############################### static caller lib build ##################

include $(CLEAR_VARS)
LOCAL_MODULE    := stat
LOCAL_SRC_FILES := native-test/stat.c
LOCAL_LDLIBS    := -llog
LOCAL_STATIC_LIBRARIES := statlib
include $(BUILD_SHARED_LIBRARY)

############################### build prebuilt library #####################

#预编译库：提前编译好的.so库或者.a库，当提供给调用者后，调用方式是以预编译模块的方式被调用者依赖。
#测试步骤：1.指定编译模块prebuilt_supplier，生成一个预编译库libprebuilt_supplier.so，然后拷贝到调用者的源码目录下。
#		2.重新指定编译模块use_prebuilt_lib，生成调用者库
#       3.java中System.loadLibrary("prebuilt_supplier") ; 和System.loadLibrary("use_prebuilt_lib") ;都需要加载。
# 说明：调用者源码中不用引用提供者的头文件，就能直接编译通过，因为prebuilt_module的方法直接被合并到了use_prebuilt_lib库中
#      这样的预编译动态库，需要重复加载，而静态库不需要重复加载，所以预编译库用静态的发布给别人最好。

#include $(CLEAR_VARS)
#LOCAL_MODULE    := prebuilt_supplier
#LOCAL_SRC_FILES := prebuilt-supplier/prebuiltlib.c
#LOCAL_LDLIBS    := -llog
#include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := prebuilt_module
LOCAL_SRC_FILES := prebuilt/libprebuilt_supplier.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := use_prebuilt_lib
LOCAL_SRC_FILES := prebuilt/use_prebuilt_lib.c
LOCAL_SHARED_LIBRARIES := prebuilt_module
LOCAL_LDLIBS    := -llog
include $(BUILD_SHARED_LIBRARY)
