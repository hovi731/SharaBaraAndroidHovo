package com.example.webpageapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

@SuppressLint("CustomAnimation")



class LoaderAnimation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader_animation)

        lifecycleScope.launchWhenCreated {
            delay(2500)
            finish()
        }


    }
}


