//
//  AmapFactory.swift
//  flutter_plugin_amap
//
//  Created by guofei chu on 2020/12/2.
//
import Flutter

class AmapFactory:NSObject, FlutterPlatformViewFactory{
   
    var flutterRegister:FlutterPluginRegistrar
    
    func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return AmapController(frame: frame, viewID: viewId, args: args,binaryMessenger: flutterRegister);
    }
    
    init(withMessenger registart: FlutterPluginRegistrar) {
       self.flutterRegister = registart
       super.init()
    }
       
    
}
