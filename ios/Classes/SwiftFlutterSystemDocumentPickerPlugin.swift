import Flutter
import UIKit

public class SwiftFlutterSystemDocumentPickerPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_system_document_picker", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterSystemDocumentPickerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
