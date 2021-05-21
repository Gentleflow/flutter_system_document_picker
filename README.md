# flutter_system_document_picker

Flutter picker document system  only Android

Use :
```
// select document path
String selectPath = await FlutterSystemDocumentPicker.selectDocument();

// save file to select path
bool saveState = await FlutterSystemDocumentPicker.saveFile(path, selectPath);

```


TODO
- Support iOS
