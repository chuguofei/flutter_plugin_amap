import Flutter
import UIKit
import AMapFoundationKit

public class SwiftFlutterPluginAmapPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_plugin_amap", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterPluginAmapPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
    
    
    if let path = Bundle.main.path(forResource: "Info", ofType: "plist") {
          let resourceFileDictionary = NSDictionary(contentsOfFile: path)
          if let key = resourceFileDictionary?.object(forKey: "amap_key") {
              AMapServices.shared().apiKey = key as? String
          }
   }
      
   AMapServices.shared().enableHTTPS = true
    let factory = AmapFactory(withMessenger: registrar)
    registrar.register(factory, withId: "com.lczp.amap/plugin")
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
