import 'package:flutter/material.dart';

import 'package:flutter_plugin_amap/flutter_plugin_amap.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
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
                  text: "跳转内部导航",
                  onTap: () {
                    FlutterPluginAmap.amapNav;
                  },
                ),
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
                  text: "驾车路线",
                  onTap: () {
                    _amapController.drivingRoute();
                  },
                ),
              ],
            ),
            Container(
              decoration:
              BoxDecoration(border: Border.all(color: Colors.red)),
              height: 300,
              child: AmapView(
                mapType:MapType.NAVI,
                mapCreatedCallback: (AmapController controller) {
                  _amapController = controller;
                },
              ),
            ),
            Expanded(
              child: ListView(
                children: <Widget>[

                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
