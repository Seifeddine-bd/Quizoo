package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.adapters.AnalyticsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResultsFragment extends Fragment {
    private static final String TAG = "ResultsFragment";
    private QuizRepository repository;
    private AnalyticsAdapter adapter;
    private MaterialTextView totalQuizzesText;
    private MaterialTextView correctAnswersText;
    private MaterialTextView averageTimeText;
    private Map<String, Quiz> quizMap = new HashMap<>();
    private Map<Integer, List<Quiz>> quizzesByCategory = new HashMap<>();
    private Map<Integer, String> categoryNames = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        totalQuizzesText = view.findViewById(R.id.total_quizzes_text);
        correctAnswersText = view.findViewById(R.id.correct_answers_text);
        averageTimeText = view.findViewById(R.id.average_time_text);
        RecyclerView recyclerView = view.findViewById(R.id.analytics_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AnalyticsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        // Fetch categories and quizzes
        repository.getCategories().observe(getViewLifecycleOwner(), categories -> {
            for (Category category : categories) {
                categoryNames.put(category.getId(), category.getName());
                Log.d(TAG, "Category ID: " + category.getId() + ", Name: " + category.getName());
                repository.getQuizzesByCategory(category.getId()).observe(getViewLifecycleOwner(), quizzes -> {
                    quizzesByCategory.put(category.getId(), quizzes);
                    Log.d(TAG, "Quizzes for Category " + category.getId() + ": " + quizzes);
                    for (Quiz quiz : quizzes) {
                        quizMap.put(quiz.getId(), quiz);
                    }
                    Log.d(TAG, "Quiz Map Updated: " + quizMap);
                    // Update results when all data is loaded
                    repository.getAnalytics().observe(getViewLifecycleOwner(), this::updateResults);
                });
            }
        });

        return view;
    }

    private void updateResults(List<Analytics> analyticsList) {
        Log.d(TAG, "Raw Analytics List: " + analyticsList);
        // Filter analytics to keep only the latest attempt per quizId
        Map<String, Analytics> latestAnalyticsMap = new TreeMap<>();
        for (Analytics analytics : analyticsList) {
            latestAnalyticsMap.put(analytics.getQuizId(), analytics); // Newest entries overwrite older ones
        }
        List<Analytics> filteredAnalytics = new ArrayList<>(latestAnalyticsMap.values());
        Log.d(TAG, "Filtered Analytics List: " + filteredAnalytics);

        // Update adapter with filtered list
        adapter.setAnalytics(filteredAnalytics, quizMap, quizzesByCategory, categoryNames);

        // Calculate total quizzes
        int totalQuizzes = filteredAnalytics.size();
        totalQuizzesText.setText("Total Quizzes: " + totalQuizzes);

        // Calculate correct answers and average time
        int correctAnswers = 0;
        long totalTime = 0;
        for (Analytics analytics : filteredAnalytics) {
            totalTime += analytics.getTimeTaken();
            Quiz quiz = quizMap.get(analytics.getQuizId());
            if (quiz != null) {
                String correctAnswer = quiz.getOptions().get(quiz.getCorrectAnswerIndex());
                Log.d(TAG, "Quiz ID: " + analytics.getQuizId() + ", Selected: " + analytics.getSelectedAnswer() + ", Correct: " + correctAnswer);
                if (analytics.getSelectedAnswer().equals(correctAnswer)) {
                    correctAnswers++;
                }
            } else {
                Log.w(TAG, "Quiz not found for ID: " + analytics.getQuizId());
            }
        }

        correctAnswersText.setText("Correct Answers: " + correctAnswers);
        double averageTime = totalQuizzes > 0 ? (double) totalTime / totalQuizzes / 1000 : 0;
        averageTimeText.setText(String.format("Average Time: %.2fs", averageTime));
    }
}