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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Quiz.class, Category.class, Analytics.class}, version = 2, exportSchema = false)
@TypeConverters({OptionsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private static final String DATABASE_NAME = "quizoo_db";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract QuizDao quizDao();
    public abstract CategoryDao categoryDao();
    public abstract AnalyticsDao analyticsDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration() // Clear database on version change
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                QuizDao quizDao = INSTANCE.quizDao();
                CategoryDao categoryDao = INSTANCE.categoryDao();
                AnalyticsDao analyticsDao = INSTANCE.analyticsDao();

                // Clear existing data
                quizDao.deleteAll();
                categoryDao.deleteAll();
                analyticsDao.deleteAll();

                // Pre-populate categories
                Category science = new Category(1, "Science");
                Category history = new Category(2, "History");
                categoryDao.insertAll(Arrays.asList(science, history));
                Log.d(TAG, "Inserted categories: " + categoryDao.getAllCategoriesSync().size());

                // Pre-populate quizzes
                Quiz quiz1 = new Quiz("1", 1, "What is the chemical symbol for water?", Arrays.asList("H2O", "CO2", "O2", "N2"), 0);
                Quiz quiz2 = new Quiz("2", 1, "What planet is known as the Red Planet?", Arrays.asList("Jupiter", "Mars", "Venus", "Mercury"), 1);
                Quiz quiz3 = new Quiz("3", 2, "In which year did World War II end?", Arrays.asList("1945", "1918", "1939", "1941"), 0);
                quizDao.insertAll(Arrays.asList(quiz1, quiz2, quiz3));
                Log.d(TAG, "Inserted quizzes: " + quizDao.getAllQuizzesSync().size());

                // Pre-populate analytics with correct answers for testing
                Analytics analytics1 = new Analytics("1", "H2O", 1957L); // Correct answer
                Analytics analytics2 = new Analytics("2", "Mars", 1881L); // Correct answer
                Analytics analytics3 = new Analytics("3", "1939", 2646L); // Incorrect answer
                analyticsDao.insert(analytics1);
                analyticsDao.insert(analytics2);
                analyticsDao.insert(analytics3);
                Log.d(TAG, "Inserted analytics: " + analyticsDao.getAllAnalyticsSync().size());
            });
        }
    };
}