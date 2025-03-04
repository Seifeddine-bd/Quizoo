// com/seifeddine/bd/quizoo/data/local/AppDatabase.java
package com.seifeddine.bd.quizoo.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.seifeddine.bd.quizoo.data.local.dao.AnalyticsDao;
import com.seifeddine.bd.quizoo.data.local.dao.CategoryDao;
import com.seifeddine.bd.quizoo.data.local.dao.QuizDao;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

@Database(entities = {Category.class, Quiz.class, Analytics.class}, version = 1, exportSchema = false)
@TypeConverters({OptionsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract QuizDao quizDao();
    public abstract AnalyticsDao analyticsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "quiz_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}