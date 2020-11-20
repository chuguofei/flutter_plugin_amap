import 'package:flutter/material.dart';
import 'dart:async';

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
  @override
  void initState() {
    super.initState();
  }

  AmapController _amapController;
  AmapController _amapController1;

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
                    _amapController1.setMyLocationEnabled();
                  },
                ),
              ],
            ),
            Expanded(
              child: ListView(
                children: <Widget>[
                  Container(
                    decoration:
                        BoxDecoration(border: Border.all(color: Colors.red)),
                    height: 300,
                    child: AmapView(
                      mapCreatedCallback: (AmapController controller) {
                        _amapController = controller;
                        _amapController.setMyLocationEnabled();
                      },
                    ),
                  ),
                  Container(
                    decoration:
                        BoxDecoration(border: Border.all(color: Colors.red)),
                    height: 100,
                    child: AmapView(
                      mapCreatedCallback: (AmapController controller) {
                        _amapController1 = controller;
                      },
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
