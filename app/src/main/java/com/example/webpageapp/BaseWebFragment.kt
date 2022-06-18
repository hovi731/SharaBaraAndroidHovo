package com.example.webpageapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseWebFragment: Fragment() {
    abstract var mWebView: WebView?
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }
    private var valueCallback :ValueCallback<Array<Uri?>?>? = null
    private var mCameraPhotoPath: String? = null

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun openFileSelector() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, "image/*")

        val granted_camera = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        val granted_storage = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (granted_camera == PackageManager.PERMISSION_GRANTED && granted_storage == PackageManager.PERMISSION_GRANTED){
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.e("asdfasdf", "Unable to create Image File", ex)
            }

            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
            }
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
        }

        val chooserIntent = Intent.createChooser(pickIntent, "Choose")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(takePhotoIntent))
        activityForResult.launch(chooserIntent)
    }

    private var activityForResult = registerForActivityResult(StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            var results: Array<Uri?>? = null
            if (result.data == null) {
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
            } else {
                val dataString: String = result.data?.data.toString()
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
            valueCallback?.onReceiveValue(results)
        })

    val mWebChromeClient = ChromeClient(::openFileChooser)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                openFileChooser(valueCallback)
                return
            }
            STORAGE_PERMISSION_CODE -> {
                openFileChooser(valueCallback)
                return
            }
        }
    }

    private fun openFileChooser(valueCallback: ValueCallback<Array<Uri?>?>?) {
        this.valueCallback = valueCallback
        val granted_camera = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        val granted_storage = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        when {
            granted_camera == PackageManager.PERMISSION_GRANTED && granted_storage == PackageManager.PERMISSION_GRANTED -> {
                openFileSelector()
            }
            granted_camera != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
            granted_storage != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }
            else -> {
                openFileSelector()
            }
        }
    }

    fun showloader(){
        (activity as? WebPageActivity)?.showloader(true)
    }

    fun hideloader(){
        (activity as? WebPageActivity)?.showloader(false)
    }

    fun runJsOpenMenu(){
        val css = ".u-cabinet-aside.d-none.d-md-block {display: block !important;}"
        val js = "var style = document.createElement('style'); style.innerHTML = '$css'; document.head.appendChild(style);"
        mWebView?.evaluateJavascript(js,null)
    }

    fun runJsCloseMenu(){
        val cssClose = ".u-cabinet-aside.d-none.d-md-block {display: none !important;}"
        val js = "var style = document.createElement('style'); style.innerHTML = '$cssClose'; document.head.appendChild(style);"

        mWebView?.evaluateJavascript(js,null)
    }





    fun shareItem(url: String){
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " ")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun getHorizontalAnimation(): NavOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_from_right)
        .setExitAnim(R.anim.slide_out_to_left)
        .setPopEnterAnim(R.anim.slide_in_from_left)
        .setPopExitAnim(R.anim.slide_out_to_right)
        .build()

    fun getVerticalAnimation(): NavOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_from_bottom)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.slide_out_to_bottom)
        .build()

    fun getCurrentUrl(): String =
        arguments?.getString("URL_REDIRECT") ?: WebPageActivity.baseUrl
}