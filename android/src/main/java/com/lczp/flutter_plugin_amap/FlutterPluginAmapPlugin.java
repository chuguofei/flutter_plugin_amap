package com.lczp.flutter_plugin_amap;


import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.lifecycle.HiddenLifecycleReference;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterPluginAmapPlugin
 */
public class FlutterPluginAmapPlugin implements FlutterPlugin, ActivityAware, DefaultLifecycleObserver {

    private MethodChannel channel;
    private EventChannel eventChannel;


    private FlutterPluginBinding pluginBinding;
    private Lifecycle lifecycle;


    /**
     * 插件被关联到 FlutterEngine 时调用,可以做一些初始化工作。
     *
     * @param flutterPluginBinding
     */
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        pluginBinding = flutterPluginBinding;
        // 注册methodCallHandle
        channel = new MethodChannel(pluginBinding.getFlutterEngine().getDartExecutor(), "com.lczp.amap_nav/plugin");
        eventChannel = new EventChannel(pluginBinding.getFlutterEngine().getDartExecutor(), "com.lczp.amap_fromAndroid");

        MethodCallHandleImpl methodCallHandle = new MethodCallHandleImpl(pluginBinding.getApplicationContext());
        channel.setMethodCallHandler(methodCallHandle);
        eventChannel.setStreamHandler(methodCallHandle);
    }

    /**
     * 之前flutter版本，现在已经换成onAttachedToEngine方法
     *
     * @param registrar
     */
    public static void registerWith(Registrar registrar) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_plugin_amap");
//        channel.setMethodCallHandler(new FlutterPluginAmapPlugin());
    }


    /**
     * 插件从 FlutterEngine 移除时调用,可以在这里做一些清理工作。
     *
     * @param binding
     */
    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    /**
     * onAttachedToActivity和onDetachedFromActivity是ActivityAware的接口方法,主要是用于获取当前flutter页面所处的Activity.
     *
     * @param binding
     */
    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {




        HiddenLifecycleReference reference = (HiddenLifecycleReference) binding.getLifecycle();
        lifecycle = reference.getLifecycle();
        lifecycle.addObserver(this);
        pluginBinding.getPlatformViewRegistry().registerViewFactory("com.lczp.amap/plugin",
                new AmapFactory(pluginBinding.getBinaryMessenger(), binding.getActivity(), lifecycle));
    }

    /**
     * 在此方法结束时，Activity提供的 onAttachedToActivity (ActivityPluginBinding)不再有效。对关联Activity或的任何引用ActivityPluginBinding均应清除。
     */
    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.onDetachedFromActivity();
    }

    /**
     * 重新创建后，该插件及其插件FlutterEngine已重新附加到，以处理配置更改
     *
     * @param binding
     */
    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        HiddenLifecycleReference reference = (HiddenLifecycleReference) binding.getLifecycle();
        lifecycle = reference.getLifecycle();
        lifecycle.addObserver(this);
    }

    /**
     * 在此方法结束时，Activity提供的 onAttachedToActivity (ActivityPluginBinding)不再有效。对关联Activity或的任何引用ActivityPluginBinding均应清除。
     */
    @Override
    public void onDetachedFromActivity() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

    }
}


