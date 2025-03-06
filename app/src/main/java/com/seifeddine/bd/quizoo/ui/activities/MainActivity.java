package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textview.MaterialTextView;
import com.seifeddine.bd.quizoo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);
        MaterialTextView appName = findViewById(R.id.app_name_text);

        // Fade-in animation for logo and app name
        logo.setAlpha(0f);
        appName.setAlpha(0f);
        logo.animate().alpha(1f).setDuration(1000).start();
        appName.animate().alpha(1f).setDuration(1000).setStartDelay(500).start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2000);
    }
}