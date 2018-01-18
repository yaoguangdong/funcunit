package com.yaogd.hook.utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtil {
    private static final String TAG = HookUtil.class.getSimpleName();

    public static void hookInstrumentation() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method sCurrentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            sCurrentActivityThread.setAccessible(true);
            //获取ActivityThread 对象
            Object activityThreadObject = sCurrentActivityThread.invoke(activityThread);

            //获取 Instrumentation 对象
            Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(activityThreadObject);
            CustomInstrumentation customInstrumentation = new CustomInstrumentation(instrumentation);
            //将我们的 customInstrumentation 设置进去
            mInstrumentation.set(activityThreadObject, customInstrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CustomInstrumentation extends Instrumentation{

        private Instrumentation base;

        public CustomInstrumentation(Instrumentation base) {
            this.base = base;
        }

        @Override
        public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
            Log.e(TAG, "invoked  CustomInstrumentation#newActivity, " + "class name =" + className + ", intent = " + intent);
            return super.newActivity(cl, className, intent);
        }
    }

    public static void hookIActivityManager() {

        //一路反射，直到拿到IActivityManager的对象
        try {
            Class<?> ActivityManagerNativeClss = Class.forName("android.app.ActivityManagerNative");
            Field defaultFiled = ActivityManagerNativeClss.getDeclaredField("gDefault");
            defaultFiled.setAccessible(true);
            Object defaultValue = defaultFiled.get(null);

            //反射SingleTon
            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field mInstance = SingletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);

            //拿到ActivityManagerProxy对象 代理ActivityManagerNative对象的子类ActivityManagerService
            Object iActivityManagerObject = mInstance.get(defaultValue);

            //开始动态代理，用代理对象替换掉真实的ActivityManager，瞒天过海
            AmsInvocationHandler handler = new AmsInvocationHandler(iActivityManagerObject);

            Object proxy = Proxy.newProxyInstance(handler.getClass().getClassLoader(), iActivityManagerObject.getClass().getInterfaces(), handler);

            //现在替换掉这个对象
            mInstance.set(defaultValue, proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class AmsInvocationHandler implements InvocationHandler {

        private Object iActivityManagerObject;

        private AmsInvocationHandler(Object iActivityManagerObject) {
            this.iActivityManagerObject = iActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.i(TAG, method.getName());
            //我要在这里搞点事情
            if ("startActivity".contains(method.getName())) {
                Log.e(TAG, "Activity已经开始启动");
            }
            return method.invoke(iActivityManagerObject, args);
        }
    }
}