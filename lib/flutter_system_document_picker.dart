import 'dart:async';

import 'package:flutter/services.dart';

class FlutterSystemDocumentPicker {
  static const MethodChannel _channel =
      const MethodChannel('flutter_system_document_picker');

  static Future<String> selectDocument() {
    return _channel.invokeMethod('selectDocument');
  }

  // [formPath] flutter file path
  // [toPath] selected path
  static Future<bool> saveFile(String formPath, String toPath) {
    return _channel
        .invokeMethod('saveFile', {'formPath': formPath, 'toPath': toPath});
  }
}
