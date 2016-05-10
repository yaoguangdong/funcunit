#armeabi mips x86 all
APP_ABI := armeabi
#
APP_BUILD_SCRIPT := jni/boss.mk
# optional build modules split by space
APP_MODULES := sk use_prebuilt_lib
#  A 'release' mode is the default, and will generate highly optimized binaries. The 'debug' mode will generate un-optimized binaries which are much easier to debug.
APP_OPTIM=debug
