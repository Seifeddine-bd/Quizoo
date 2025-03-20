package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.repository.UserRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser == null) {
                // User is not logged in, navigate to login
                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
                return;}



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            try {
                // Log for debugging
                android.widget.Toast.makeText(this, "Logging out...", android.widget.Toast.LENGTH_SHORT).show();

                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut();

                // Clear repository data if needed
                if (UserRepository.getInstance() != null) {
                    UserRepository.getInstance().signOut();
                }

                // Navigate to login with flags to clear stack
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            } catch (Exception e) {
                android.widget.Toast.makeText(this, "Logout error: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}