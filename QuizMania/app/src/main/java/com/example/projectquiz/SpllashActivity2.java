package com.example.projectquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SpllashActivity2 extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spllash2);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SpllashActivity2.this,MainActivity.class);
                SpllashActivity2.this.startActivity(mainIntent);
                SpllashActivity2.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}