import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_system_document_picker/flutter_system_document_picker.dart';
import 'package:path_provider/path_provider.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String selectPath = '';

  Future<String> createFile() async {
    Directory directory;
    if (Platform.isIOS) {
      directory = await getApplicationSupportDirectory();
    } else if (Platform.isAndroid) {
      directory = await getExternalStorageDirectory();
    }

    String localPath = directory.path + '/Backup.json';
    File file = File(localPath);
    await file.writeAsString('flutter system document picker');
    return localPath;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              FlatButton(
                child: Text('Select Document'),
                onPressed: () {
                  FlutterSystemDocumentPicker.selectDocument().then((value) {
                    setState(() {
                      selectPath = value;
                    });
                  });
                },
              ),
              FlatButton(
                child: Text('Save Document'),
                onPressed: () {
                  createFile().then((path) async {
                    if (selectPath.isEmpty) {
                       throw('Select Path must be don\'t null');
                    }
                    bool saveState = await FlutterSystemDocumentPicker.saveFile(
                        path, selectPath);
                    print('saveState : $saveState');
                  });
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
