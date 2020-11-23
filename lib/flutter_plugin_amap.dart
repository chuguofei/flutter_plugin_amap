import 'dart:async';

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

  // 开启猎鹰服务
  static Future get amapTrackStart async {
    await amap_nav_channel.invokeMethod("amap#trackStart");
  }

  // 停止猎鹰服务
  static Future get amapTrackStop async {
    await amap_nav_channel.invokeMethod("amap#trackStop");
  }

  // 获取终端行驶里程
  static Future get amapTrackQueryDistance async {
    await amap_nav_channel.invokeMethod("amap#trackQueryDistance");
  }
}
