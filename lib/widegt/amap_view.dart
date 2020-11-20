import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin_amap/export.dart';
import 'package:flutter_plugin_amap/impl/amap_controller.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: widegt
 * @ClassName: amapView
 * @date: 2020/11/20 11:44
 * @Description:
 */

const String viewType = "com.lczp.amap/plugin";

// 地图创建完后到回掉
typedef void MapCreatedCallback(AmapController amapController);

/**
 * amap widget
 */
class AmapView extends StatelessWidget {
  // 创建完地图到回掉
  MapCreatedCallback mapCreatedCallback;

  AmapView({this.mapCreatedCallback});

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: viewType,
      onPlatformViewCreated: onPlatformViewCreated,
      creationParamsCodec: const StandardMessageCodec(),
    );
  }

  Future<void> onPlatformViewCreated(int id) async {
    debugPrint("地图id是【$id】");
    final AmapController amapController = await AmapController.init(id);
    if (this.mapCreatedCallback != null) {
      this.mapCreatedCallback(amapController);
    }
  }
}
