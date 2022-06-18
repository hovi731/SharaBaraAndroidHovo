package com.example.webpageapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.core.view.isVisible
import pl.droidsonroids.gif.GifImageView


class FragmentOne : BaseWebFragment() {

    override var mWebView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        initWebView(view.findViewById(R.id.webViewOne))

        (activity as? WebPageActivity)?.currentFragment = this
        showloader()

        return view
    }

    private fun initWebView(webView: WebView) {
        //mWebView = (WebView) findViewById(R.id.webViewOne);

        mWebView = webView

        webView.apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.allowFileAccess = true
            WebView.setWebContentsDebuggingEnabled(true)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("adsfasdf", "1  " + request?.url.toString())
//                    if (request?.url?.toString()?.endsWith("/user/login") == true) {
//                        navigate(request.url, getHorizontalAnimation())
//                        return true
//                    }
                    // val intent = Intent(context, LoaderAnimation::class.java)
                    //  context?.startActivity(intent)
                    // findViewById<View>(R.id.gifloader).isVisible = true

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
                    // mImageView.setVisibility(View.GONE);
                    //findViewById<View>(R.id.ImageViewOne).setVisibility(View.GONE)
                    hideloader()

                    //findViewById<GifImageView>(R.id.gifloader).setVisibility(GifImageView.GONE);


                    // findViewById<View>(R.id.gifloader).isVisible = false


                    //view.findViewById(R.id.webViewOne).isVisible = false
                    // findViewById<View>(R.id.gifloader).isVisible = false
                    (activity as? WebPageActivity)?.setToolbarTitle(view.title ?: "")

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



                    super.onPageFinished(view, url);


                }
            }
            webChromeClient = mWebChromeClient
            loadUrl(getCurrentUrl())
        }
    }

    private fun navigate(urlRedirect: Uri, navOptions: NavOptions) {
        Log.d("adsfasdf", "navigate 1  " + urlRedirect.toString())
        findNavController().navigate(
            R.id.action_fragmentOne_to_fragmentTwo,
            bundleOf("URL_REDIRECT" to urlRedirect.toString()),
            navOptions
        )
    }
}