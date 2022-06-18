package com.example.webpageapp;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        //Beginning the loading animation as we attempt to verify registration with SIP


        ImageView ivLoader = (ImageView) findViewById(R.id.IVloadinganimation);

        //ivLoader.setBackgroundResource(R.drawable.animationloader);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivLoader.getBackground();
        ivLoader.setVisibility(VISIBLE);
        animationDrawable.start();
        ivLoader.setVisibility(View.GONE);
        animationDrawable.stop();
        //setContentView(R.layout.fragment_one);


        //ivLoader.setVisibility(View.GONE);

        //frameAnimation.stop();




    }
}
