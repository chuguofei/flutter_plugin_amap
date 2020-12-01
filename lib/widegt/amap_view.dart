import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin_amap/export.dart';
import 'package:flutter_plugin_amap/impl/amap_controller.dart';
import 'dart:convert';

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

class AmapView extends StatefulWidget {
  @override
  _AmapViewState createState() => _AmapViewState();

  /// 地图类型
  final MapType mapType;

  /// 当前位置的类型，仅在[myLocationEnabled]为true时有效 (android only)
  final MyLocationStyle myLocationStyle;

  /// 初始化地图位置
//  final CameraPosition cameraPosition;

  /// 设置地图是否显示当前位置，默认为false
  final bool myLocationEnabled;

  /// 设置地图是否可以通过手势滑动，默认为true
  final bool scrollGesturesEnabled;

  /// 设置地图是否可以通过手势进行旋转，默认为true
  final bool rotateGesturesEnabled;

  /// 设置地图是否显示比例尺，默认为false
  final bool scaleControlsEnabled;

  /// 设置地图是否可以通过手势倾斜（3D效果），默认为true (android only)
  final bool tiltGesturesEnabled;

  /// 设置地图是否允许缩放，默认为true
  final bool zoomControlsEnabled;

  /// 设置是否显示跳转当前位置按钮，仅在[myLocationEnabled]为true时有效
  final bool setMyLocationButtonEnabled;

  // 创建完地图到回掉
  MapCreatedCallback mapCreatedCallback;

  AmapView({
    this.mapType,
    this.myLocationStyle,
    this.myLocationEnabled,
    this.scrollGesturesEnabled,
    this.rotateGesturesEnabled,
    this.scaleControlsEnabled,
    this.tiltGesturesEnabled,
    this.zoomControlsEnabled,
    this.setMyLocationButtonEnabled,
    this.mapCreatedCallback,
  });
}

class _AmapViewState extends State<AmapView> {
  _AmapParams _amapParams;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _amapParams = _AmapParams.fromWidget(widget);
  }

  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> creationParams = <String, dynamic>{
      "options": _amapParams.toMap(),
    };

    return defaultTargetPlatform == TargetPlatform.android
        ? AndroidView(
            viewType: viewType,
            creationParams: _amapParams.toMap(),
            onPlatformViewCreated: onPlatformViewCreated,
            creationParamsCodec: const StandardMessageCodec(),
          )
        : UiKitView(
            viewType: viewType,
            creationParams: _amapParams.toMap(),
            onPlatformViewCreated: onPlatformViewCreated,
            creationParamsCodec: const StandardMessageCodec(),
          );
  }

  Future<void> onPlatformViewCreated(int id) async {
    debugPrint("地图id是【$id】");
    final AmapController amapController = await AmapController.init(id);
    if (widget.mapCreatedCallback != null) {
      widget.mapCreatedCallback(amapController);
    }
  }
}

/**
 * 初始化地图默认参数
 */
class _AmapParams {
  _AmapParams(
      {this.mapType,
      this.myLocationStyle,
      this.myLocationEnabled,
      this.setMyLocationButtonEnabled,
      this.scrollGesturesEnabled,
      this.rotateGesturesEnabled,
      this.scaleControlsEnabled,
      this.tiltGesturesEnabled,
      this.zoomControlsEnabled});

  final MapType mapType;

  final MyLocationStyle myLocationStyle;

//  final CameraPosition cameraPosition;

  final bool myLocationEnabled;

  final bool setMyLocationButtonEnabled;

  final bool scrollGesturesEnabled;

  final bool rotateGesturesEnabled;

  final bool scaleControlsEnabled;

  final bool tiltGesturesEnabled;

  final bool zoomControlsEnabled;

  static _AmapParams fromWidget(AmapView map) {
    return _AmapParams(
      mapType: map.mapType,
      myLocationStyle: map.myLocationStyle,
//      cameraPosition: map.cameraPosition,
      myLocationEnabled: map.myLocationEnabled,
      scrollGesturesEnabled: map.scrollGesturesEnabled,
      rotateGesturesEnabled: map.rotateGesturesEnabled,
      scaleControlsEnabled: map.scaleControlsEnabled,
      tiltGesturesEnabled: map.tiltGesturesEnabled,
      zoomControlsEnabled: map.zoomControlsEnabled,
      setMyLocationButtonEnabled: map.setMyLocationButtonEnabled,
    );
  }

  Map<String, dynamic> toMap() {
    final Map<String, dynamic> options = <String, dynamic>{};

    void addIfNonNull(String fieldName, dynamic value) {
      if (value != null) {
        options[fieldName] = value;
      }
    }

    addIfNonNull("mapType", mapType?.index);
    addIfNonNull("myLocationStyle", myLocationStyle?.index);
//    addIfNonNull("camera", cameraPosition?.toMap());
    addIfNonNull("myLocationEnabled", myLocationEnabled);
    addIfNonNull("rotateGesturesEnabled", rotateGesturesEnabled);
    addIfNonNull("scrollGesturesEnabled", scrollGesturesEnabled);
    addIfNonNull("scaleControlsEnabled", scaleControlsEnabled);
    addIfNonNull("tiltGesturesEnabled", tiltGesturesEnabled);
    addIfNonNull("zoomControlsEnabled", zoomControlsEnabled);
    addIfNonNull("setMyLocationButtonEnabled", setMyLocationButtonEnabled);

    return options;
  }

  String toEncodedJson() {
    return jsonEncode(toMap());
  }

  Map<String, dynamic> updatesMap(_AmapParams newOptions) {
    final Map<String, dynamic> prevOptionsMap = toMap();

    return newOptions.toMap()
      ..removeWhere(
          (String key, dynamic value) => prevOptionsMap[key] == value);
  }
}
