import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin_amap/flutter_plugin_amap.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: impl
 * @ClassName: amap_method_channel
 * @date: 2020/11/20 14:15
 * @Description: methodCallChannel 对地图操作
 */

class MethodCallChannel {
  static final Map<int, MethodChannel> _channels = {};

  MethodChannel getChannel(int mapId) {
    return _channels[mapId];
  }

  static void setChannel(int mapId) {
    if (_channels.containsKey(mapId) == false) {
      _channels[mapId] = MethodChannel('com.lczp.amap/plugin_${mapId}');
    }
  }

  // 实现定位蓝点
  Future setMyLocationEnabled(int id) async {
    this.getChannel(id).invokeMethod("amap#setMyLocationEnabled");
  }
}
