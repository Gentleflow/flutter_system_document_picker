package tech.gentleflow.flutter_system_document_picker

import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import androidx.documentfile.provider.DocumentFile
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.io.*


class FlutterSystemDocumentPickerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
    private lateinit var channel: MethodChannel
    private lateinit var binding: ActivityPluginBinding
    private lateinit var selectResult: Result


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_system_document_picker")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "selectDocument" -> {
                this.selectResult = result
                selectDocument()
            }
            "saveFile" -> {
                val formPath = call.argument<String>("formPath")!!
                val toPath = call.argument<String>("toPath")!!
                saveFile(result, formPath, toPath)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun selectDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        binding.activity.startActivityForResult(intent, 1000)
    }

    private fun saveFile(result: Result, formPath: String, toPath: String) {
        if (toPath.startsWith("content://")) {
            try {
                val documentFile = DocumentFile.fromTreeUri(binding.activity, Uri.parse(toPath))
                if (documentFile!!.canWrite()) {
                    val start = formPath.lastIndexOf("/")
                    var fileName = "Backup"
                    if (start != -1) {
                        fileName = formPath.substring(start + 1)
                    }
                    var toFile = documentFile.findFile(fileName)
                    if (toFile == null) {
                        toFile = documentFile.createFile("*/*", fileName)
                    }

                    val formFile = File(formPath)
                    val fileInputStream = FileInputStream(formFile)
                    val outputStream = binding.activity.contentResolver.openOutputStream(toFile!!.uri)
                    val bufferedInputStream = BufferedInputStream(fileInputStream)
                    val bufferedOutputStream = BufferedOutputStream(outputStream)
                    val bytes = ByteArray(1024 * 8)
                    while (true) {
                        val len = bufferedInputStream.read(bytes)
                        if (len == -1) {
                            break
                        } else {
                            bufferedOutputStream.write(bytes, 0, len)
                        }
                    }
                    bufferedInputStream.close()
                    bufferedOutputStream.close()
                    result.success(true)
                } else {
                    result.success(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.success(false)
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.binding = binding
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (data != null && requestCode == 1000) {
            selectResult.success(data.data.toString())
        }
        return true
    }
}
