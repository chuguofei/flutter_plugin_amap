import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:flutter_plugin_amap/flutter_plugin_amap.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const myEventPlugin = const EventChannel("com.lczp.amap_fromAndroid");
  int count = 0;
  List<String> fromAndroidList = [];

  @override
  void initState() {
    super.initState();
    myEventPlugin.receiveBroadcastStream().listen(_onEvent, onError: _onError);
  }

  void _onEvent(Object event) {
    print("[success]: $event");
    setState(() {
      count++;
      fromAndroidList.insert(0, event);
    });
  }

  void _onError(Object error) {
    print("error: $error");
    setState(() {
      count--;
    });
  }

  AmapController _amapController;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Wrap(
              alignment: WrapAlignment.start,
              children: <Widget>[
                MyButton(
                  text: "定位当前位置",
                  onTap: () {
                    _amapController.showMyLocation();
                  },
                ),
                MyButton(
                  text: "切换地图模式",
                  onTap: () {
                    _amapController.setMapType(MapType.BUS);
                  },
                ),
                MyButton(
                  text: "驾车路线绘制",
                  onTap: () {
                    _amapController.drivingRoute();
                  },
                ),
              ],
            ),
            Container(
              decoration: BoxDecoration(border: Border.all(color: Colors.red)),
              height: 300,
              child: AmapView(
                mapType: MapType.NORMAL,
                myLocationStyle: MyLocationStyle.LOCATION_TYPE_LOCATE,
                scaleControlsEnabled: true,
                mapCreatedCallback: (AmapController controller) {
                  _amapController = controller;
                },
              ),
            ),
            Wrap(
              children: <Widget>[
                MyButton(
                  text: "跳转内部导航",
                  onTap: () => FlutterPluginAmap.amapNav,
                ),
                MyButton(
                  text: "开启猎鹰服务",
                  onTap: () => FlutterPluginAmap.amapTrackStart,
                ),
                MyButton(
                  text: "停止猎鹰服务",
                  onTap: () => FlutterPluginAmap.amapTrackStop,
                ),
                MyButton(
                  text: "查询终端行驶里程",
                  onTap: () => FlutterPluginAmap.amapTrackQueryDistance,
                ),
              ],
            ),
            Text("from android count ${count}"),
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                itemBuilder: (BuildContext context, int index) {
                  return Column(
                    children: <Widget>[
                      Padding(
                        child: Text(fromAndroidList[index].toString()),
                        padding: EdgeInsets.symmetric(vertical: 5),
                      ),
                      Divider(
                        height: 1,
                      )
                    ],
                  );
                },
                itemCount: fromAndroidList.length,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
