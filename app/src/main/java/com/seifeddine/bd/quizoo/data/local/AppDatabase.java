package com.seifeddine.bd.quizoo.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.seifeddine.bd.quizoo.data.local.dao.AnalyticsDao;
import com.seifeddine.bd.quizoo.data.local.dao.CategoryDao;
import com.seifeddine.bd.quizoo.data.local.dao.QuizDao;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Quiz.class, Category.class, Analytics.class}, version = 2, exportSchema = false)
@TypeConverters({OptionsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    public abstract QuizDao quizDao();
    public abstract CategoryDao categoryDao();
    public abstract AnalyticsDao analyticsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "quiz_db")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d(TAG, "Database created. Seeding fake data...");
                                    // Use a background thread for seeding
                                    new Thread(() -> seedFakeData(getDatabase(context))).start();
                                }
                            })
                            .fallbackToDestructiveMigration() // Recreate the database if schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedFakeData(AppDatabase database) {
        Log.d(TAG, "Starting to seed fake data...");
        CategoryDao categoryDao = database.categoryDao();
        QuizDao quizDao = database.quizDao();
        AnalyticsDao analyticsDao = database.analyticsDao();

        // Clear existing data to ensure fresh seeding
        categoryDao.insertAll(new ArrayList<>());
        quizDao.insertQuizzes(new ArrayList<>());
        analyticsDao.insert(new Analytics(0, "0", 0L)); // Clear or initialize with String

        // Fake Categories
        Category science = new Category(1, "Science");
        Category history = new Category(2, "History");
        categoryDao.insertAll(Arrays.asList(science, history));
        Log.d(TAG, "Inserted categories: " + categoryDao.getAllCategoriesSync().toString());

        // Fake Quizzes
        Quiz quiz1 = new Quiz(1, 1, "What is the chemical symbol for water?",
                Arrays.asList("H2O", "CO2", "O2", "H2SO4"), 0);
        Quiz quiz2 = new Quiz(2, 1, "Which planet is known as the Red Planet?",
                Arrays.asList("Venus", "Mars", "Jupiter", "Saturn"), 1);
        Quiz quiz3 = new Quiz(3, 2, "In which year did World War II end?",
                Arrays.asList("1945", "1939", "1941", "1943"), 0);
        quizDao.insertQuizzes(Arrays.asList(quiz1, quiz2, quiz3));
        Log.d(TAG, "Inserted quizzes for Science: " + quizDao.getQuizzesByCategory(1));

        // Fake Analytics
        Analytics analytics1 = new Analytics(1, "A", 5000L); // Use String for userAnswer
        Analytics analytics2 = new Analytics(2, "B", 3000L);
        analyticsDao.insert(analytics1);
        analyticsDao.insert(analytics2);
        Log.d(TAG, "Inserted analytics: " + analyticsDao.getAllAnalyticsSync().size());
    }
}