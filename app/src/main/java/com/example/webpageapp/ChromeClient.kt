package com.example.webpageapp

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.File
import java.io.IOException

const val INPUT_FILE_REQUEST_CODE = 2890

class ChromeClient(private val openFileChooser:(ValueCallback<Array<Uri?>?>?) -> Unit): WebChromeClient() {
    override fun onShowFileChooser(
        view: WebView?,
        filePath: ValueCallback<Array<Uri?>?>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        openFileChooser.invoke(filePath)
       return true
    }
}