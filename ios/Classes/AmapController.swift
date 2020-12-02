//
//  AmapController.swift
//  flutter_plugin_amap
//
//  Created by guofei chu on 2020/12/2.
//


import Flutter
import AMapNaviKit


class AmapController:NSObject,FlutterPlatformView{
    
    let frame: CGRect;
    let viewId: Int64;
    var mapView: MAMapView!
    private var methodChannel:FlutterMethodChannel
    
    init(frame: CGRect,viewID: Int64,args :Any?,binaryMessenger register: FlutterPluginRegistrar) {
        
        self.frame = frame
        self.viewId = viewID
        mapView = MAMapView(frame: self.frame)
        methodChannel = FlutterMethodChannel(name: "com.lczp.amap/plugin_\(viewId)", binaryMessenger:register.messenger())
        super.init()
        methodChannel.setMethodCallHandler(onMethodCall)
    }
    
    func view() -> UIView {
        return mapView
    }
    
    
    
    // 管道
   func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
       switch(methodCall.method) {
       case "map#waitForMap":
           result(nil)
       case "amap#showMyLocation":
        mapView.showsUserLocation = true;
        mapView.userTrackingMode = MAUserTrackingMode.follow;
        let r = MAUserLocationRepresentation();
        r.showsHeadingIndicator = true;
        r.showsAccuracyRing = true;
        r.enablePulseAnnimation = true;
        mapView.update(r);
       result(nil)
       default:
           result(FlutterMethodNotImplemented)
       }
   }
    
}
