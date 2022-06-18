package com.example.webpageapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

class FragmentTwo : BaseWebFragment() {

    override var mWebView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_two, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? WebPageActivity)?.currentFragment = this
        initWebView(view.findViewById(R.id.webViewTwo))
        showloader()
    }

    private fun initWebView(webView: WebView) {
        mWebView = webView
        webView.apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.allowFileAccess = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("adsfasdf", "2  " + request?.url.toString())
//                    if (request?.url?.toString()?.contains("/cabinet/") == true) {
//                        navigate(request.url, getHorizontalAnimation())
//                        return true
//                    }
                    //val intent = Intent(context, LoaderAnimation::class.java)
                    // context?.startActivity(intent)


                    showloader()

                    if (request?.url?.toString()?.endsWith("https://sharabara.kz/") == true) {
                        (activity as? WebPageActivity)?.changeMenuType(MenuType.MENU_TYPE_UNDEFINED)
                    }
                    if (request?.url?.toString()?.startsWith("tel:") == true) {
                        request.url?.toString()?.substringAfter("tel:")?.trim()?.let {
                            (activity as? WebPageActivity)?.callPhoneNumber(it)
                        }
                        return true
                    }
                    if (request?.url?.toString()?.contains("/item/add") == true) {
                        val css = ".b-breadcrumbs {display: none !important;}"
                        val js =
                            "var style = document.createElement('style'); style.innerHTML = '$css'; document.head.appendChild(style);"
                        mWebView?.evaluateJavascript(js, null)
                        navigate(request.url, getVerticalAnimation())

                        return true
                    }
                    if (request?.url?.toString()?.endsWith("/cabinet/") == true) {
                        navigate(request.url, getHorizontalAnimation())
                        return true
                    }
                    if (request?.url?.toString()?.contains("/item/") == true) {
                        navigate(request.url, getHorizontalAnimation())
                        return true
                    }
                    if (request?.url?.toString()?.contains("/cabinet/") != true) {
                        (activity as? WebPageActivity)?.changeMenuType(MenuType.NO_MENU)
                    } else {
                        if (request?.url?.toString()?.contains("/cabinet/favs") == true) {
                            (activity as? WebPageActivity)?.changeMenuType(MenuType.NO_MENU)
                        } else {
                            (activity as? WebPageActivity)?.changeMenuType(MenuType.MENU_MORE)
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView, url: String) {

                    hideloader()
                    (activity as? WebPageActivity)?.setToolbarTitle(view.title ?: "")

                    //findViewById<View>(R.id.gifloader).isVisible = false
                    if (url?.toString()?.contains("/item/add") == true) {
                        val css =
                            ".b-breadcrumbs {display: none !important;} .h-header {display: none !important;}"
                        val js =
                            "var style = document.createElement('style'); style.innerHTML = '$css'; document.head.appendChild(style);"
                        mWebView?.evaluateJavascript(js, null)
                        (activity as? WebPageActivity)?.setToolbarTitle("")

                    }
                    if (url?.toString()?.contains("/item/") == true) {
                        val css =
                            ".f-footer { display : none !important } .b-breadcrumbs{ display : none !important } .l-page-head{ display : none !important} .fl-search-container{display : none !important} .h-header{display : none !important}"
                        val js =
                            "var style = document.createElement('style'); style.innerHTML = '$css'; document.head.appendChild(style);"
                        mWebView?.evaluateJavascript(js, null)


                    }


                }
            }
            webChromeClient = mWebChromeClient
//            webChromeClient = object : WebChromeClient.FileChooserParams() {
//                override fun getMode(): Int {
//                    TODO("Not yet implemented")
//                }
//
//                override fun getAcceptTypes(): Array<String> {
//                    TODO("Not yet implemented")
//                }
//
//                override fun isCaptureEnabled(): Boolean {
//                    TODO("Not yet implemented")
//                }
//
//                override fun getTitle(): CharSequence? {
//                    TODO("Not yet implemented")
//                }
//
//                override fun getFilenameHint(): String? {
//                    TODO("Not yet implemented")
//                }
//
//                override fun createIntent(): Intent {
//                    TODO("Not yet implemented")
//                }
//
//            }
            loadUrl(getCurrentUrl())
        }
    }

    private fun navigate(urlRedirect: Uri, navOptions: NavOptions) {
        Log.d("adsfasdf", "navigate 2  " + urlRedirect.toString())
        findNavController().navigate(
            R.id.action_fragmentTwo_to_fragmentOne,
            bundleOf("URL_REDIRECT" to urlRedirect.toString()),
            navOptions
        )
    }
}