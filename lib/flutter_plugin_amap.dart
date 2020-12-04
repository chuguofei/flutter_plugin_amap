import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
export './export.dart';

class FlutterPluginAmap {
  static const MethodChannel amap_nav_channel =
      const MethodChannel('com.lczp.amap_nav/plugin');

  // 跳转内部导航
  static Future<String> get amapNav async {
    final String result = await amap_nav_channel.invokeMethod("amapNav");
    return result;
  }

  /**
   * 开启猎鹰服务
   * terminalName -> 终端注册名称
   * TrackId -> 轨迹id
   */
  static Future amapTrackStart(String terminalName, String trackId) async {
    Map params = {
      "serviceId": 217813,
      "terminalName": terminalName,
      "TrackId": trackId,
    };
    await amap_nav_channel.invokeMethod("amap#track#start", params);
  }

  // 停止猎鹰服务
  static Future get amapTrackStop async {
    await amap_nav_channel.invokeMethod("amap#track#stop");
  }

  // 获取终端行驶里程
  static Future get amapTrackQueryDistance async {
    await amap_nav_channel.invokeMethod("amap#trackQueryDistance");
  }

  // 原生Toast
  static Future Toast(String str) async {
    await amap_nav_channel.invokeMethod("Toast", str);
  }

  // 绑定终端
  static Future<Map> amapTrackAdd(String terminal) async {
    Map params = {
      "key": "b09d2f642a0e006ddc87b16b14fcc85d",
      "sid": 217813,
      "name": terminal
    };
    await amap_nav_channel.invokeMethod("amap#track#add", params);
    Map result = json.decode(await amap_nav_channel.invokeMethod("amap#track#add", params)) as Map<String, dynamic>;
    if(result["errCode"] != 0){
      Toast(result["errdetail"]);
    }
    return result["errCode"] == 0 ? result["data"] : null;
  }

  // 查询终端
  static Future get amapTrackList async {
    Map params = {
      "key": "b09d2f642a0e006ddc87b16b14fcc85d",
      "sid": 217813,
    };
    await amap_nav_channel.invokeMethod("amap#track#list", params);
  }

  /**
   * 创建轨迹
   *
   * 一个终端可创建50万条轨迹
   */
  static Future<String> amapTraceAdd(String tid) async {
    Map params = {
      "key": "b09d2f642a0e006ddc87b16b14fcc85d",
      "sid": 217813,
      "tid": tid,
    };
    return await amap_nav_channel.invokeMethod("amap#trace#add", params);
  }
}
