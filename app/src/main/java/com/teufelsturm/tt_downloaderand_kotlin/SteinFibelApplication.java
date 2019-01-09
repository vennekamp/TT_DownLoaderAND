//package com.teufelsturm.tt_downloader3;
//
//import android.app.Application;
//import android.content.Context;
//
//import java.lang.ref.WeakReference;
//
//public class SteinFibelApplication extends Application {
//    private static WeakReference<Context> context;
//
//    public void onCreate() {
//        super.onCreate();
//        SteinFibelApplication.context = new WeakReference<>(getApplicationContext());
//    }
//
//    public static Context getAppContext() {
//        return SteinFibelApplication.context.get();
//    }
//}
//
