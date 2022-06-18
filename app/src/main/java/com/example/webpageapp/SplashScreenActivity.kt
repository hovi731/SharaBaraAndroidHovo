package com.example.webpageapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        lifecycleScope.launchWhenCreated {
            delay(2000)
            startActivity(Intent(this@SplashScreenActivity,WebPageActivity::class.java).also {
                it.putExtra("URL_REDIRECT_LINK", intent.data?.toString())
            })
            finish()
        }
    }
}
