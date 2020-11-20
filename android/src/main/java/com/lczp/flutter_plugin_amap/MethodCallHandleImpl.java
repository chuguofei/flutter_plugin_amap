package com.lczp.flutter_plugin_amap;

import android.content.Context;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * @author guofei
 * @date 2020/11/20 10:48
 * @deprecated 实现methdCallHandler方法
 */
public class MethodCallHandleImpl implements MethodChannel.MethodCallHandler {

    private final Context context;

    public MethodCallHandleImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("amapNav")) { // 内部导航
            //终点
            Poi end = new Poi("经纬城市绿洲二期", new LatLng(39.399798, 116.994106), null);
            // 组件参数配置
            AmapNaviParams params = new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
            // 启动组件
            AmapNaviPage.getInstance().showRouteActivity(context, params, null);
        } else if (call.method.equals("amap#setMyLocationEnabled")) { // 显示当前位置

        } else {
            result.notImplemented();
        }
    }
}
