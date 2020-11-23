import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_plugin_amap/flutter_plugin_amap.dart';
import 'package:flutter_plugin_amap/impl/amap_controller.dart';
import 'package:flutter_plugin_amap/ui/my_button.dart';
import 'package:flutter_plugin_amap/widegt/amap_common.dart';
import 'package:flutter_plugin_amap/widegt/amap_view.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: example.lib
 * @ClassName: MyHome
 * @date: 11/23/20 2:50 PM
 * @Description:
 */

class MyHome extends StatefulWidget {
  @override
  _MyHomeState createState() => _MyHomeState();
}

class _MyHomeState extends State<MyHome> {
  static const myEventPlugin = const EventChannel("com.lczp.amap_fromAndroid");
  int count = 0;
  List<String> fromAndroidList = [];
  TextEditingController terminalController =
      new TextEditingController(); // 注册的终端名称
  TextEditingController terminalIdController;
  TextEditingController trackIdController;
  int trackId = null; // 轨迹id
  int _terminalId = null; // 终端id

  @override
  void initState() {
    super.initState();
    terminalIdController = new TextEditingController();
    trackIdController = new TextEditingController();
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
    return Column(
      children: <Widget>[
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
        Expanded(
          child: Column(
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
              TextField(
                controller: terminalController,
                decoration: InputDecoration(
                  hintText: "请输入终端名称",
                ),
              ),
              Row(
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: TextField(
                      controller: terminalIdController,
                      decoration: InputDecoration(
                        hintText: "终端id",
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 10,
                  ),
                  Expanded(
                    flex: 1,
                    child: TextField(
                      controller: trackIdController,
                      decoration: InputDecoration(
                        hintText: "轨迹id",
                      ),
                    ),
                  )
                ],
              ),
              Wrap(
                children: <Widget>[
                  MyButton(
                    text: "跳转内部导航",
                    onTap: () => FlutterPluginAmap.amapNav,
                  ),
                  MyButton(
                    text: "绑定新终端",
                    onTap: () {
                      showElasticDialog(
                        context: context,
                        builder: (BuildContext _) {
                          return BaseDialog(
                            hiddenTitle: true,
                            child: Text(
                                "将要绑定【${terminalController.text.trim()}】为终端"),
                            autoClose: true,
                            onPressed: () async {
                              if (terminalController.text.trim() == "") {
                                FlutterPluginAmap.Toast("绑定的终端不能为空");
                                return;
                              }
                              // 新增终端
                              String terminalId =
                                  await FlutterPluginAmap.amapTrackAdd(
                                      terminalController.text.trim());
                              _terminalId = int.parse(terminalId);
                              setState(() {});
                            },
                          );
                        },
                      );
                    },
                  ),
                  MyButton(
                    text: "查询终端",
                    onTap: () => FlutterPluginAmap.amapTrackList,
                  ),
                  MyButton(
                    text: "创建轨迹",
                    onTap: () async {
                      if (terminalIdController.text.trim() == "") {
                        FlutterPluginAmap.Toast("终端Id不能为空");
                        return;
                      }
                      String traceId = await FlutterPluginAmap.amapTraceAdd(
                          terminalIdController.text.trim());
                      // 轨迹id
                      trackIdController.text = traceId;
                      setState(() {});
                    },
                  ),
                  MyButton(
                      text: "开启猎鹰服务",
                      onTap: () {
                        if (terminalController.text.trim() == "") {
                          FlutterPluginAmap.Toast("输入终端名称");
                          return;
                        }
                        if (trackIdController.text.trim() == "") {
                          FlutterPluginAmap.Toast("输入轨迹id");
                          return;
                        }
                        FlutterPluginAmap.amapTrackStart(
                            terminalController.text.trim(),
                            trackIdController.text.trim());
                      }),
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
                  physics: const NeverScrollableScrollPhysics(),
                  itemBuilder: (BuildContext context, int index) {
                    return Column(
                      children: <Widget>[
                        Padding(
                          child: Text(fromAndroidList[index].toString()),
                          padding: EdgeInsets.symmetric(vertical: 10),
                        ),
                        Divider(
                          height: 1,
                          color: Colors.red,
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
      ],
    );
  }
}
