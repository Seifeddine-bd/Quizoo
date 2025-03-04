package com.seifeddine.bd.quizoo.ui.activities;

import com.seifeddine.bd.quizoo.data.local.AppDatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.fragments.QuizFragment;

public class QuizActivity extends AppCompatActivity {
    private QuizRepository repository;
    private int categoryId;

    public static void start(Context context, int categoryId) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra("categoryId", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        categoryId = getIntent().getIntExtra("categoryId", 0);
        repository = new QuizRepository(
                AppDatabase.getDatabase(this).quizDao(),
                AppDatabase.getDatabase(this).categoryDao(),
                AppDatabase.getDatabase(this).analyticsDao(),
                RetrofitClient.getApiService()
        );

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuizFragment(categoryId, repository))
                    .commit();
        }
    }
}