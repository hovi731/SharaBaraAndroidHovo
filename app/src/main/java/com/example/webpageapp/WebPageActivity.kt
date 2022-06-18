package com.example.webpageapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment


class WebPageActivity : AppCompatActivity() {

    @MenuType
    var menuType: Int = MenuType.MENU_TYPE_UNDEFINED

    var currentFragment: BaseWebFragment? = null
    private val viewModel: WebViewModel = WebViewModel()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)





       // val i = Intent(this@WebPageActivity, SplashScreen::class.java)
        //startActivity(i)

        //val i = Intent(this@WebPageActivity, LoaderAnimation::class.java)
        //startActivity(i)
        viewModel.menuTypeLiveData.observe(this, Observer(::onMenuTypeChanged))
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        val navController = navHostFragment.navController

        if (intent.extras?.getString("URL_REDIRECT_LINK") != null) {
            navController.navigate(
                R.id.fragmentOne,
                bundleOf("URL_REDIRECT" to intent.extras?.getString("URL_REDIRECT_LINK"))
            )




        }


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("adsfasdf", "destination changed url is ${arguments?.getString("URL_REDIRECT")}")


           // val i = Intent(this@WebPageActivity, LoaderAnimation::class.java)
            //startActivity(i)
            //

            //val i = Intent(this@WebPageActivity, LoaderAnimation::class.java)
            //startActivity(i)




            //Beginning the loading animation as we attempt to verify registration with SIP
            //SplashScreen()



            findViewById<View>(
                R.id.btnBack
            ).isVisible = controller.backQueue.size > 2

            findViewById<View>(
                R.id.txtTitle
            ).isVisible = controller.backQueue.size > 2

            findViewById<View>(
                R.id.sharabara_icon_container
            ).isVisible = controller.backQueue.size <= 2

            findViewById<AppCompatImageView>(
                R.id.btnMenu
            ).apply {
                isVisible = controller.backQueue.size > 2
            }
            changeMenuType(arguments?.getString("URL_REDIRECT") ?: "")

        }
        initOnToolbarItemsClickListeners()

    }
    class myWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {

        }
    }






    private fun showToolbarWithoutMenu(){
        findViewById<View>(
            R.id.btnBack
        ).isVisible = true

        findViewById<View>(
            R.id.txtTitle
        ).isVisible = true

        findViewById<View>(
            R.id.sharabara_icon_container
        ).isVisible = false

        findViewById<AppCompatImageView>(
            R.id.btnMenu
        ).apply {
            isVisible = false
        }
    }

    private fun showHomeToolbar() {
        findViewById<View>(
            R.id.btnBack
        ).isVisible = false

        findViewById<View>(
            R.id.txtTitle
        ).isVisible = false

        findViewById<View>(
            R.id.sharabara_icon_container
        ).isVisible = true

        findViewById<AppCompatImageView>(
            R.id.btnMenu
        ).apply {
            isVisible = false
        }
    }

    private fun changeMenuType(url: String){
        when{
            url.endsWith("https://sharabara.kz/")->{
                viewModel.menuTypeLiveData.value  = MenuType.MENU_TYPE_UNDEFINED
            }
            url.startsWith("https://sharabara.kz/?offline=")->{
                viewModel.menuTypeLiveData.value  =  MenuType.MENU_TYPE_UNDEFINED
            }
            url.contains("/item/add") -> {
                viewModel.menuTypeLiveData.value = MenuType.NO_MENU
                return
            }
            url.contains("/item/") -> {
                viewModel.menuTypeLiveData.value = MenuType.MENU_SHARE
            }
            url.contains("/cabinet/") -> {
                if (viewModel.menuTypeLiveData.value != MenuType.MENU_MORE || viewModel.menuTypeLiveData.value != MenuType.MENU_CLOSE){
                    viewModel.menuTypeLiveData.value = MenuType.MENU_MORE
                }
            }
            else -> MenuType.MENU_TYPE_UNDEFINED
        }
    }

    private fun onMenuTypeChanged(type: Int){
        when(type){
            MenuType.MENU_MORE -> {
                findViewById<ImageView>(R.id.btnMenu).apply {
                    isVisible = true
                    setImageResource(R.drawable.ic_bars_solid)
                    setOnClickListener {
                        currentFragment?.runJsOpenMenu()
                        viewModel.menuTypeLiveData.value = MenuType.MENU_CLOSE
                    }
                }
            }
            MenuType.MENU_CLOSE -> {
                findViewById<ImageView>(R.id.btnMenu).apply {
                    isVisible = true
                    setImageResource(R.drawable.ic_baseline_close_24)
                    setOnClickListener {
                        currentFragment?.runJsCloseMenu()
                        viewModel.menuTypeLiveData.value = MenuType.MENU_MORE
                    }
                }
            }
            MenuType.MENU_SHARE -> {
                findViewById<ImageView>(R.id.btnMenu).apply {
                    isVisible = true
                    setImageResource(R.drawable.ic_baseline_share_24)
                    setOnClickListener {
                        currentFragment?.shareItem(currentFragment?.mWebView?.url.toString())
                    }
                }
            }
            MenuType.NO_MENU -> {
                showToolbarWithoutMenu()
            }
            MenuType.MENU_TYPE_UNDEFINED -> {
                showHomeToolbar()
            }
        }
    }

    private fun initOnToolbarItemsClickListeners() {
        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
    }

    fun callPhoneNumber(phone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phone")
        startActivity(callIntent)
    }

    fun setToolbarTitle(title: String) {
        findViewById<TextView>(R.id.txtTitle).text = title.substringBefore("-")
    }

    override fun onBackPressed() {
        if (currentFragment?.mWebView?.canGoBack() == true) {
            currentFragment?.mWebView?.copyBackForwardList()?.apply {
                Log.d("adsfasdf", "asdfadsadsfadsf ${getItemAtIndex(this.size - 2).url} ")
                changeMenuType(getItemAtIndex(this.size - 2).url)
            }
            currentFragment?.mWebView?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    fun changeMenuType(@MenuType menuType: Int){
        viewModel.menuTypeLiveData.value = menuType
    }

    fun showloader(shutshow:Boolean){

            findViewById<View>(R.id.gifloader).isVisible=shutshow

    }

    companion object {
        const val baseUrl =
            "https://sharabara.kz/"
    }
}