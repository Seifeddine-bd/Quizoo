package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.adapters.ResultsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultsFragment extends Fragment {
    private QuizRepository repository;
    private ResultsAdapter adapter;
    private TextView totalQuizzesText, correctAnswersText, averageTimeText;
    private List<Analytics> analyticsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        totalQuizzesText = view.findViewById(R.id.total_quizzes);
        correctAnswersText = view.findViewById(R.id.correct_answers);
        averageTimeText = view.findViewById(R.id.average_time);
        RecyclerView recyclerView = view.findViewById(R.id.results_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResultsAdapter(analyticsList);
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        repository.getAnalytics().observe(getViewLifecycleOwner(), analytics -> {
            if (analytics != null && !analytics.isEmpty()) {
                analyticsList.clear();
                analyticsList.addAll(analytics);
                adapter.notifyDataSetChanged();

                // Calculate summary statistics
                int totalQuizzes = analytics.size();
                AtomicInteger correctAnswers = new AtomicInteger();
                long totalTime = 0;

                for (Analytics analytic : analytics) {
                    // Fetch the corresponding quiz to determine the correct answer
                    repository.getQuizById(analytic.getQuizId()).observe(getViewLifecycleOwner(), quiz -> {
                        if (quiz != null) {
                            int selectedAnswerIndex = -1;
                            try {
                                selectedAnswerIndex = Integer.parseInt(analytic.getSelectedAnswer());
                            } catch (NumberFormatException e) {
                                // Handle invalid selectedAnswer
                            }
                            if (selectedAnswerIndex == quiz.getCorrectAnswerIndex()) {
                                correctAnswers.getAndIncrement();
                                // Update UI after calculating correct answers
                                correctAnswersText.setText("Correct Answers: " + correctAnswers);
                            }
                        }
                    });
                    totalTime += analytic.getTimeTaken();
                }

                double averageTime = totalQuizzes > 0 ? (double) totalTime / totalQuizzes / 1000 : 0; // Convert to seconds

                totalQuizzesText.setText("Total Quizzes: " + totalQuizzes);
                averageTimeText.setText(String.format("Average Time: %.2fs", averageTime));
            } else {
                totalQuizzesText.setText("Total Quizzes: 0");
                correctAnswersText.setText("Correct Answers: 0");
                averageTimeText.setText("Average Time: 0s");
            }
        });

        return view;
    }
}