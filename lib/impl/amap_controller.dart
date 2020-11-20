import 'package:flutter/material.dart';

import 'amap_method_channel.dart';

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

  AmapController._({@required this.mapid});

  // 使用id初始化onMapCreated回掉
  static Future<AmapController> init(int id) async {
    assert(id != null);
    return AmapController._(mapid: id);
  }

  // 实现定位蓝点
  Future setMyLocationEnabled() async {
    new MethodCallChannel().setMyLocationEnabled(mapid);
  }
}
