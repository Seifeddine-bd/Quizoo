package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.data.remote.dto.AnalyticsRequest;

import java.util.List;

public class QuizFragment extends Fragment {
    private static final String TAG = "QuizFragment";
    private QuizRepository repository;
    private int categoryId;
    private int selectedAnswer = -1;
    private long startTime;

    public QuizFragment(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ProgressBar progressBar = view.findViewById(R.id.loading_progress);
        TextView questionText = view.findViewById(R.id.question_text);
        RadioGroup optionsGroup = view.findViewById(R.id.options_group);
        Button submitButton = view.findViewById(R.id.submit_button);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        progressBar.setVisibility(View.VISIBLE);
        repository.getQuizzesByCategory(categoryId).observe(getViewLifecycleOwner(), quizzes -> {
            progressBar.setVisibility(View.GONE);
            if (quizzes != null && !quizzes.isEmpty()) {
                Quiz quiz = quizzes.get(0); // For simplicity, show the first quiz
                questionText.setText(quiz.getQuestion());
                optionsGroup.removeAllViews();
                for (int i = 0; i < quiz.getOptions().size(); i++) {
                    RadioButton radioButton = new RadioButton(getContext());
                    radioButton.setId(i);
                    radioButton.setText(quiz.getOptions().get(i));
                    optionsGroup.addView(radioButton);
                }
                optionsGroup.setOnCheckedChangeListener((group, checkedId) -> selectedAnswer = checkedId);
                startTime = System.currentTimeMillis();
                submitButton.setOnClickListener(v -> {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    repository.submitAnalytics(new AnalyticsRequest(quiz.getId(), String.valueOf(selectedAnswer), timeTaken), getContext());
                    Toast.makeText(getContext(), "Answer submitted", Toast.LENGTH_SHORT).show();
                });
            } else {
                questionText.setText("No quizzes available for this category");
            }
        });

        return view;
    }
}