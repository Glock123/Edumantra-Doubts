package com.example.edumantradoubts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.edumantradoubts.loginsignup.LoginActivity;

public class MainActivity extends AppCompatActivity {
    
    private static int SPLASH_SCREEN_TIMEOUT = 1000; // Time in ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Splash screen can cover the entire screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }
}