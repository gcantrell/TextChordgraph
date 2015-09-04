/*
 * Copyright (c) 2015. Greg Cantrell, All rights reserved.
 */

package com.examples.gregcantrell.textchordgraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

/**
 * Created by gcantrell on 8/21/2015.
 *
 * This is a simple Splash Screen. It is the entry point of the app. It displays the logo and fades
 * to the MainActivity.
 */
public class SplashScreen extends Activity {

    // How long to show the splash screen before switching
    private static int SPLASH_TIMEOUT = 1000;

    /**
     * Sets the view content and switches to the MainActivity after a time
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This will execute after the timeout period
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                //close this activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
