package com.lczp.flutter_plugin_amap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.lczp.flutter_plugin_amap.model.UnifiedMapOptions;
import com.lczp.flutter_plugin_amap.overlay.DrivingRouteOverlay;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

/**
 * @author guofei
 * @date 2020/11/20 14:23
 */
public class AmapController
        implements DefaultLifecycleObserver,
        MethodChannel.MethodCallHandler,
        PlatformView,
        ActivityPluginBinding.OnSaveInstanceStateListener,
        RouteSearch.OnRouteSearchListener {

    private static final String TAG = "AMapController";
    private final BinaryMessenger messenger;
    private final Activity activity;
    private final MapView mapView;
    private final AMap aMap;
    private final MethodChannel methodChannel;
    private boolean disposed = false;

    public AmapController(BinaryMessenger messenger, Activity activity, Context context, int id, UnifiedMapOptions unifiedMapOptions) {
        methodChannel = new MethodChannel(messenger, "com.lczp.amap/plugin_" + id);
        methodChannel.setMethodCallHandler(this);
        this.messenger = messenger;
        this.activity = activity;
        mapView = new MapView(context,unifiedMapOptions.toAMapOptions());
        aMap = mapView.getMap();
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {

        if (mapView == null) {
            result.success("地图没有初始化");
            return;
        }
        switch (call.method) {
            case "amap#showMyLocation":  // 通知Flutter地图是否已建立
                MyLocationStyle myLocationStyle;
                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                result.success(null);
                break;
            case "amap#setMapType":
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
                break;
            case "amap#drivingRoute":
                RouteSearch routeSearch = new RouteSearch(activity);
                routeSearch.setRouteSearchListener(this);
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(39.399798, 116.994106), new LatLonPoint(40.811901, 114.884091));
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
                routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
                break;
            default:
                result.notImplemented();
        }
    }


    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        methodChannel.setMethodCallHandler(null);
    }


    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onCreate");
        if (disposed) {
            return;
        }
        mapView.onCreate(null);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onPause();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        if (disposed) {
            return;
        }
        mapView.onCreate(null);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        if (disposed) {
            return;
        }
        mapView.onCreate(null);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        final DrivePath drivePath = driveRouteResult.getPaths().get(0);
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                activity, aMap, drivePath,
                driveRouteResult.getStartPos(),
                driveRouteResult.getTargetPos(), null);
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
