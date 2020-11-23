package com.lczp.flutter_plugin_amap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceRequest;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lczp.flutter_plugin_amap.model.AmapModel;
import com.lczp.flutter_plugin_amap.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * @author guofei
 * @date 2020/11/20 10:48
 * @deprecated 实现methdCallHandler方法
 */
public class MethodCallHandleImpl implements MethodChannel.MethodCallHandler, EventChannel.StreamHandler {
    private Activity activity;
    private final Context context;
    AMapTrackClient aMapTrackClient; // 轨迹对象
    int serviceId = 217813;  // 服务id
    long terminalId = 319254202;  // 终端id
    String terminalName = "19931341381";   // 唯一标识某个用户或某台设备的名称
    private int TrackId = 20; // 轨迹id

    private EventChannel.EventSink mEventSink = null;
    private MethodChannel.Result locResult = null;

    public MethodCallHandleImpl(Context context) {
        this.context = context;
        aMapTrackClient = new AMapTrackClient(this.context); // 初始化猎鹰
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Map<String, Object> params = null;
        if (call.arguments != null && !(call.arguments instanceof String)) {
            params = (HashMap) call.arguments;
        }
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "Toast":
                Toast.makeText(context, call.arguments.toString(), Toast.LENGTH_SHORT).show();
                result.success(null);
                break;
            case "amapNav":  // 内部导航
                //终点
                Poi end = new Poi("经纬城市绿洲二期", new LatLng(39.399798, 116.994106), null);
                // 组件参数配置
                AmapNaviParams params_nav = new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                // 启动组件
                AmapNaviPage.getInstance().showRouteActivity(context, params_nav, null);
                break;
            case "amap#setMyLocationEnabled": // 显示当前位置

                break;
            case "amap#track#start": // 开始猎鹰服务
                if (params.containsKey("serviceId")) {
                    serviceId = (int) params.get("serviceId");
                }
                if (params.containsKey("terminalName")) {
                    terminalName = (String) params.get("terminalName");
                }
                if (params.containsKey("TrackId")) {
                    TrackId = Integer.parseInt(params.get("TrackId").toString());
                }
                aMapTrackClient.queryTerminal(new QueryTerminalRequest(serviceId, terminalName), new OnTrackListener() {

                    @Override
                    public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                        if (queryTerminalResponse.isSuccess()) {
                            if (queryTerminalResponse.getTid() <= 0) {
                                // terminal还不存在，先创建
                                aMapTrackClient.addTerminal(new AddTerminalRequest(terminalName, serviceId), new OnTrackListener() {
                                    @Override
                                    public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

                                    }

                                    @Override
                                    public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                                        if (addTerminalResponse.isSuccess()) {
                                            // 创建完成，开启猎鹰服务
                                            terminalId = addTerminalResponse.getTid();
                                            TrackParam trackParam = new TrackParam(serviceId, terminalId);
                                            trackParam.setTrackId(TrackId); // 轨迹id
                                            mEventSink.success(String.format("【terminal不存在，创建完成，开启猎鹰服务】服务id->%d 终端id->%s 轨迹id->%s", serviceId, terminalId, TrackId));
                                            aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                                        } else {
                                            // 请求失败
                                            Toast.makeText(context, "请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onDistanceCallback(DistanceResponse distanceResponse) {

                                    }

                                    @Override
                                    public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

                                    }

                                    @Override
                                    public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

                                    }

                                    @Override
                                    public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

                                    }

                                    @Override
                                    public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

                                    }

                                    @Override
                                    public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

                                    }
                                });
                            } else {
                                // terminal已经存在，直接开启猎鹰服务
                                terminalId = queryTerminalResponse.getTid();
                                TrackParam trackParam = new TrackParam(serviceId, terminalId);
                                trackParam.setTrackId(TrackId);
                                mEventSink.success(String.format("【terminal已经存在，直接开启猎鹰服务】服务id->%d 终端id->%s 轨迹id->%s", serviceId, terminalId, TrackId));
                                aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                            }
                        } else {
                            // 请求失败
                            Toast.makeText(context, "请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

                    }

                    @Override
                    public void onDistanceCallback(DistanceResponse distanceResponse) {

                    }

                    @Override
                    public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

                    }

                    @Override
                    public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

                    }

                    @Override
                    public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

                    }

                    @Override
                    public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

                    }

                    @Override
                    public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

                    }
                });
                break;
            case "amap#track#stop": // 停止服务
                // 1、先停止采集
                // 2、再停止服务
                aMapTrackClient.stopGather(onTrackLifecycleListener); // 停止采集
                TrackParam trackParam = new TrackParam(serviceId, terminalId);
                trackParam.setTrackId(TrackId); // 轨迹id
                aMapTrackClient.stopTrack(trackParam, onTrackLifecycleListener);  // 停止服务
                mEventSink.success("轨迹服务停止");
                break;
            case "amap#trackQueryDistance":  // 获取猎鹰行驶里程
                long curr = System.currentTimeMillis();
                DistanceRequest distanceRequest = new DistanceRequest(
                        serviceId,
                        terminalId,
                        curr - 12 * 60 * 60 * 1000, // 开始时间
                        curr,   // 结束时间
                        -1  // 轨迹id，传-1 表示包含散点在内的所有轨迹点
                );
                aMapTrackClient.queryDistance(distanceRequest, new OnTrackListener() {
                    @Override
                    public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

                    }

                    @Override
                    public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

                    }

                    @Override
                    public void onDistanceCallback(DistanceResponse distanceResponse) {
                        if (distanceResponse.isSuccess()) {
                            double meters = distanceResponse.getDistance();
                            mEventSink.success(String.format("终端%s 共行驶了 %s", terminalName, meters));
                        } else {
                            // 行驶里程查询失败
                            mEventSink.success("驶里程查询失败");
                        }
                    }

                    @Override
                    public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

                    }

                    @Override
                    public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

                    }

                    @Override
                    public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

                    }

                    @Override
                    public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

                    }

                    @Override
                    public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

                    }
                });
                break;
            case "amap#track#add": // 绑定终端
                HttpUtils.obtain().post("https://tsapi.amap.com/v1/track/terminal/add", params, new HttpUtils.ICallBack() {
                    @Override
                    public void onFailure(String throwable) {
                        Log.d("Tag", throwable);
                        mEventSink.success(throwable);
                    }

                    @Override
                    public void onSuccess(String response) {
                        AmapModel amapModel = new Gson().fromJson(response, AmapModel.class);
                        Log.d("Tag", response);
                        mEventSink.success("绑定终端"+response);
                        if(amapModel.getErrCode() == 10000){
                            // 返回终端id
                            result.success(amapModel.getData().get("sid"));
                        }
                    }
                });
                break;
            case "amap#track#list": //  查询终端
                HttpUtils.obtain().post("https://tsapi.amap.com/v1/track/terminal/list", params, new HttpUtils.ICallBack() {
                    @Override
                    public void onFailure(String throwable) {
                        mEventSink.success(throwable);
                    }

                    @Override
                    public void onSuccess(String response) {
                        mEventSink.success("查询终端"+response);
                    }
                });
                break;
            case "amap#trace#add":  // 创建轨迹
                HttpUtils.obtain().post("https://tsapi.amap.com/v1/track/trace/add", params, new HttpUtils.ICallBack() {
                    @Override
                    public void onFailure(String throwable) {
                        mEventSink.success(throwable);
                    }

                    @Override
                    public void onSuccess(String response) {
                        mEventSink.success("轨迹创建" + response);
                        AmapModel amapModel = new Gson().fromJson(response, AmapModel.class);
                        if (amapModel.getErrCode() == 10000) {
                            result.success(amapModel.getData().get("trid"));
                        }
                    }
                });
                break;
            default:
                result.notImplemented();
                break;
        }
    }


    final OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
                    status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                mEventSink.success("定位采集开启成功！");
                Toast.makeText(context, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
            } else {
                mEventSink.success("定位采集启动异常！");
                Toast.makeText(context, "定位采集启动异常，" + msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                    status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                    status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 服务启动成功，继续开启收集上报
                // 1、 先开启服务
                // 2、 定位采集
                aMapTrackClient.startGather(this);
                mEventSink.success("服务启动成功，继续开启收集上报");
            } else {
                Toast.makeText(context, "轨迹上报服务服务启动异常，" + msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            mEventSink.success("猎鹰停止收集");
        }

        @Override
        public void onStopTrackCallback(int i, String s) {
            mEventSink.success("猎鹰停止服务");
        }
    };

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        mEventSink = events;
        //设置定时器给Flutter传值

        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("log", "❤️  来自于android的问候 ->Timer ❤️");
                        mEventSink.success("❤来自于android的问候 ->Timer");
                    }
                });
            }
        }, 2000, 2);*/


       /* ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("log", "❤️  来自于android的问候 ->ScheduledThreadPoolExecutor  ❤️");
                        mEventSink.success("❤来自于android ->ScheduledThreadPoolExecutor的问候");
                    }
                });

            }
        }, 10, 1000, TimeUnit.MILLISECONDS);*/


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mEventSink.success("❤️  来自于android的问候  ❤️");
                    }
                }, 2 * 1000);
            }
        });*/
    }

    @Override
    public void onCancel(Object arguments) {

    }
}
