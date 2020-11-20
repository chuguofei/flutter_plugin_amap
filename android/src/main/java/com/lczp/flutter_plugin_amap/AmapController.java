package com.lczp.flutter_plugin_amap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

/**
 * @author guofei
 * @date 2020/11/20 14:23
 */
public class AmapController implements DefaultLifecycleObserver, MethodChannel.MethodCallHandler, PlatformView {

    private static final String TAG = "AMapController";
    private final BinaryMessenger messenger;
    private final Activity activity;
    private final Context context;
    private final int id;
    private final MapView mapView;
    private final AMap aMap;
    private final MethodChannel methodChannel;

    public AmapController(BinaryMessenger messenger, Activity activity, Context context, int id) {
        methodChannel = new MethodChannel(messenger, "com.lczp.amap/plugin_" + id);
        methodChannel.setMethodCallHandler(this);
        this.messenger = messenger;
        this.activity = activity;
        this.context = context;
        this.id = id;
        mapView = new MapView(context);
        aMap = mapView.getMap();
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "amap#setMyLocationEnabled": { // 通知Flutter地图是否已建立
                if (mapView == null) {
                    result.success(null);
                    return;
                }
                MyLocationStyle myLocationStyle;
                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                result.success(null);
                break;
            }
            default:
                result.notImplemented();
        }
    }


    @Override
    public void onFlutterViewAttached(@NonNull View flutterView) {

    }

    @Override
    public void onFlutterViewDetached() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void onInputConnectionLocked() {

    }

    @Override
    public void onInputConnectionUnlocked() {

    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onCreate");
        mapView.onCreate(null);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        mapView.onResume();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        mapView.onPause();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

    }
}
