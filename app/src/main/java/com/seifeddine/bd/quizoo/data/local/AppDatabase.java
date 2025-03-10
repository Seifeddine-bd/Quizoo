package com.seifeddine.bd.quizoo.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.seifeddine.bd.quizoo.R;
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
                Category programming = new Category(1, "Programming", R.drawable.programming);
                Category computerScience = new Category(2, "Computer Science",R.drawable.cs);
                Category artificialIntelligence = new Category(3, "AI",R.drawable.ai);
                Category dataStructures = new Category(4, "Data Structures",R.drawable.ds);
                Category algorithms = new Category(5, "Algorithms",R.drawable.algo);

                categoryDao.insertAll(Arrays.asList(programming, computerScience, artificialIntelligence, dataStructures, algorithms));
                Log.d(TAG, "Inserted categories: " + categoryDao.getAllCategoriesSync().size());

// Pre-populate quizzes
                Quiz quiz1 = new Quiz("1", 1, "What does 'OOP' stand for?", Arrays.asList("Object-Oriented Programming", "Optimal Output Processing", "Operational Open Protocol", "Ordered Object Processing"), 0);
                Quiz quiz2 = new Quiz("2", 1, "Which language is primarily used for Android app development?", Arrays.asList("Kotlin", "Swift", "JavaScript", "Python"), 0);
                Quiz quiz3 = new Quiz("3", 2, "What is the time complexity of binary search in the worst case?", Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n log n)"), 2);
                Quiz quiz4 = new Quiz("4", 2, "Who is considered the father of computer science?", Arrays.asList("Alan Turing", "Dennis Ritchie", "Elon Musk", "Bill Gates"), 0);
                Quiz quiz5 = new Quiz("5", 3, "Which algorithm is used in deep learning to adjust weights?", Arrays.asList("Backpropagation", "Gradient Descent", "ReLU", "Softmax"), 1);
                Quiz quiz6 = new Quiz("6", 3, "What does CNN stand for in deep learning?", Arrays.asList("Convolutional Neural Network", "Central Neural Node", "Cascading Neural Network", "Cognitive Network Node"), 0);
                Quiz quiz7 = new Quiz("7", 4, "Which data structure uses LIFO (Last In, First Out)?", Arrays.asList("Queue", "Stack", "Heap", "Linked List"), 1);
                Quiz quiz8 = new Quiz("8", 4, "What is the best case time complexity of quicksort?", Arrays.asList("O(n)", "O(n log n)", "O(log n)", "O(n²)"), 1);
                Quiz quiz9 = new Quiz("9", 5, "Which algorithm is used to find the shortest path in a graph?", Arrays.asList("Dijkstra’s Algorithm", "Prim’s Algorithm", "Merge Sort", "Quick Sort"), 0);
                Quiz quiz10 = new Quiz("10", 5, "What is a divide-and-conquer algorithm?", Arrays.asList("An algorithm that splits problems into smaller subproblems", "A brute force method", "An approach used only in AI", "An algorithm for sorting strings"), 0);

                quizDao.insertAll(Arrays.asList(quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, quiz7, quiz8, quiz9, quiz10));
                Log.d(TAG, "Inserted quizzes: " + quizDao.getAllQuizzesSync().size());

// Pre-populate analytics with correct answers for testing
//                Analytics analytics1 = new Analytics("1", "Object-Oriented Programming", 1957L);
//                Analytics analytics2 = new Analytics("2", "Kotlin", 1881L);
//                Analytics analytics3 = new Analytics("3", "O(log n)", 2646L);
//                Analytics analytics4 = new Analytics("4", "Alan Turing", 2145L);
//                Analytics analytics5 = new Analytics("5", "Gradient Descent", 1672L);
//                Analytics analytics6 = new Analytics("6", "Convolutional Neural Network", 2451L);
//                Analytics analytics7 = new Analytics("7", "Stack", 1832L);
//                Analytics analytics8 = new Analytics("8", "O(n log n)", 2043L);
//                Analytics analytics9 = new Analytics("9", "Dijkstra’s Algorithm", 1950L);
//                Analytics analytics10 = new Analytics("10", "An algorithm that splits problems into smaller subproblems", 1764L);
//
//                analyticsDao.insertAll(Arrays.asList(analytics1, analytics2, analytics3, analytics4, analytics5, analytics6, analytics7, analytics8, analytics9, analytics10));
                Log.d(TAG, "Inserted analytics data.");

                Log.d(TAG, "Inserted analytics: " + analyticsDao.getAllAnalyticsSync().size());
            });
        }
    };
}