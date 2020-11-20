import 'dart:async';

import 'package:flutter/services.dart';
export './export.dart';

class FlutterPluginAmap {

  static const MethodChannel amap_nav_channel =  const MethodChannel('com.lczp.amap_nav/plugin');

  // 跳转内部导航
  static Future<String> get amapNav async {
    final String result = await amap_nav_channel.invokeMethod("amapNav");
    return result;
  }
}
