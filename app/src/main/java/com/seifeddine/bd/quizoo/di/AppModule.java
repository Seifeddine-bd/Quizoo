package com.seifeddine.bd.quizoo.di;

import android.content.Context;
import androidx.room.Room;

import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;


public class AppModule {
    private static AppDatabase database;
    private static QuizRepository repository;

    public static AppDatabase provideDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "quiz_db")
                    .build();
        }
        return database;
    }

    public static QuizRepository provideRepository(Context context) {
        if (repository == null) {
            AppDatabase db = provideDatabase(context);
            repository = new QuizRepository(db.quizDao(), db.categoryDao(), db.analyticsDao(), RetrofitClient.getApiService());
        }
        return repository;
    }
}