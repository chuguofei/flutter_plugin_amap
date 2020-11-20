import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin_amap/widegt/amap_common.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: impl
 * @ClassName: amap_controller
 * @date: 2020/11/20 13:45
 * @Description:
 */

class AmapController {
  final int mapid;
  static MethodChannel _channel;

  AmapController._({@required this.mapid});

  // 使用id初始化onMapCreated回掉
  static Future<AmapController> init(int id) async {
    assert(id != null);
    _channel = MethodChannel('com.lczp.amap/plugin_$id');
    return AmapController._(mapid: id);
  }

  // 实现定位蓝点
  Future showMyLocation() async {
    _channel.invokeMethod("amap#showMyLocation");
  }

  // 切换地图图层
  Future setMapType(MapType mapType) {
    _channel.invokeMethod("amap#setMapType",{});
  }

  // 切换地图图层
  Future drivingRoute() {
    _channel.invokeMethod("amap#drivingRoute",{});
  }
}
