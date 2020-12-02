package com.lczp.flutter_plugin_amap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;

import com.google.gson.Gson;
import com.lczp.flutter_plugin_amap.model.UnifiedMapOptions;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * @author guofei
 * @date 2020/11/20 10:34
 */
class AmapFactory extends PlatformViewFactory {


    private final BinaryMessenger messenger;
    private final Activity activity;
    private final Lifecycle lifecycle;

    public AmapFactory(BinaryMessenger messenger, Activity activity, Lifecycle lifecycle) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.activity = activity;
        this.lifecycle = lifecycle;

    }

    @Override
    public PlatformView create(final Context context, int viewId, Object args) {
        Gson g = new Gson();
        UnifiedMapOptions unifiedMapOptions = g.fromJson(args.toString(), UnifiedMapOptions.class);
        final AmapController amapController = new AmapController(messenger, activity, context, viewId, unifiedMapOptions);
        if (lifecycle != null) {
            lifecycle.addObserver(amapController);
        }
        return amapController;
    }
}
