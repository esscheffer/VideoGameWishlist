package com.scheffer.erik.videogamewishlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scheffer.erik.videogamewishlist.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.fullscreen_content)
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                               | View.SYSTEM_UI_FLAG_FULLSCREEN
                                               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                               | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
