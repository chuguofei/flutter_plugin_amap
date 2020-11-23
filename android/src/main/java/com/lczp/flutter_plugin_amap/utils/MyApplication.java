package com.lczp.flutter_plugin_amap.utils;

import android.app.Application;
import android.content.Context;

/**
 * @author guofei
 * @date 11/22/20 9:26 PM
 */
public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
