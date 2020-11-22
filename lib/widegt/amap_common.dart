import 'package:flutter/material.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: lib.widegt
 * @ClassName: amap_common
 * @date: 2020/11/20 17:42
 * @Description:
 */

enum MapType {
  /// 普通地图
  NORMAL,

  /// 卫星地图
  SATELLITE,

  /// 夜间模式
  NIGHT,

  /// 导航模式
  NAVI,

  /// 公交模式
  BUS,
}

enum MyLocationStyle {
  /// 只定位
  LOCATION_TYPE_SHOW,

  /// 定位、且将视角移动到地图中心点
  LOCATION_TYPE_LOCATE,

  /// 定位、且将视角移动到地图中心点，定位点跟随设备移动
  LOCATION_TYPE_FOLLOW,

  /// 定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动
  LOCATION_TYPE_MAP_ROTATE,

  /// 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
  LOCATION_TYPE_LOCATION_ROTATE,

  /// 定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
  LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER,

  /// 定位、但不会移动到地图中心点，并且会跟随设备移动
  LOCATION_TYPE_FOLLOW_NO_CENTER,

  /// 定位、但不会移动到地图中心点，地图依照设备方向旋转，并且会跟随设备移动
  LOCATION_TYPE_MAP_ROTATE_NO_CENTER,
}
