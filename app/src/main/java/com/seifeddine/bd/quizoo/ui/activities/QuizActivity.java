package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.fragments.CategoriesFragment;
import com.seifeddine.bd.quizoo.ui.fragments.QuizFragment;

public class QuizActivity extends AppCompatActivity {
    private QuizRepository repository;

    public static void start(Context context, int categoryId) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra("categoryId", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new QuizRepository(
                AppDatabase.getDatabase(this).quizDao(),
                AppDatabase.getDatabase(this).categoryDao(),
                AppDatabase.getDatabase(this).analyticsDao(),
                RetrofitClient.getApiService()
        );

        if (savedInstanceState == null) {
            int categoryId = getIntent().getIntExtra("categoryId", -1);
            if (categoryId != -1) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new QuizFragment(categoryId))
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CategoriesFragment())
                        .commit();
            }
        }
    }
}