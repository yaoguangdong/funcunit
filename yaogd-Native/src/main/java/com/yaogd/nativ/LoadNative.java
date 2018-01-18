package com.yaogd.nativ;

public class LoadNative {
    
    static {
        System.loadLibrary("use_prebuilt_lib") ;
    }
    
    public static native String get() ;
}
