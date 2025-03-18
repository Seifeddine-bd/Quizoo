package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
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
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
    private int currentQuizIndex = 0;
    private int selectedAnswer = -1;
    private int correctAnswers = 0;
    private long totalTime = 0;
    private long startTime;
    private List<Quiz> quizzes;
    private ProgressBar progressBar;
    private TextView progressText;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ProgressBar loadingProgress = view.findViewById(R.id.loading_progress);
        progressBar = view.findViewById(R.id.progress_bar);
        progressText = view.findViewById(R.id.progress_text);
        TextView questionText = view.findViewById(R.id.question_text);
        RadioGroup optionsGroup = view.findViewById(R.id.options_group);
        Button submitButton = view.findViewById(R.id.submit_button);
        Button nextQuizButton = view.findViewById(R.id.next_quiz_button);
        TextView resultText = view.findViewById(R.id.result_text);
        MaterialButton backToMainButton = view.findViewById(R.id.back_to_main_button);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        loadingProgress.setVisibility(View.VISIBLE);
        repository.getQuizzesByCategory(categoryId).observe(getViewLifecycleOwner(), quizzes -> {
            loadingProgress.setVisibility(View.GONE);
            if (quizzes != null && !quizzes.isEmpty()) {
                this.quizzes = quizzes;
                updateProgress();
                displayQuiz(quizzes.get(currentQuizIndex), questionText, optionsGroup, submitButton, nextQuizButton, resultText);
            } else {
                questionText.setText("No quizzes available for this category");
            }
        });

        submitButton.setOnClickListener(v -> {
            if (selectedAnswer == -1) {
                Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            long timeTaken = System.currentTimeMillis() - startTime;
            Quiz quiz = quizzes.get(currentQuizIndex);
            int correctAnswer = quiz.getCorrectAnswerIndex();
            boolean isCorrect = selectedAnswer == correctAnswer;
            String result = isCorrect ? "Correct!" : "Incorrect. The correct answer is: " + quiz.getOptions().get(correctAnswer);
            resultText.setText(result);
            resultText.setVisibility(View.VISIBLE);
            resultText.setTextColor(isCorrect ? getResources().getColor(R.color.successColor) : getResources().getColor(R.color.errorColor));
            nextQuizButton.setVisibility(View.VISIBLE);
            String selectedAnswerText = quiz.getOptions().get(selectedAnswer);
            repository.submitAnalytics(new AnalyticsRequest(quiz.getId(), selectedAnswerText, timeTaken), getContext());
            Snackbar.make(view, result, Snackbar.LENGTH_SHORT).show();
            submitButton.setVisibility(View.GONE);
            optionsGroup.setEnabled(false);
        });
/*
        nextQuizButton.setOnClickListener(v -> {
            currentQuizIndex++;
            if (currentQuizIndex < quizzes.size()) {
                updateProgress();
                displayQuiz(quizzes.get(currentQuizIndex), questionText, optionsGroup, submitButton, nextQuizButton, resultText);
                resultText.setVisibility(View.GONE);
                nextQuizButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                optionsGroup.setEnabled(true);
                selectedAnswer = -1;
            } else {
                //Toast.makeText(getContext(), "No more quizzes in this category", Toast.LENGTH_SHORT).show();
                nextQuizButton.setVisibility(View.GONE);
                // All quizzes completed, navigate to reward screen


                // Calculate score from repository analytics data
                for (Quiz quiz : quizzes) {
                    // You'll need to retrieve analytics data for each quiz
                    // This is simplified - implement with actual repository call
                    repository.getAnalyticsByQuizId(quiz.getId()).observe(getViewLifecycleOwner(), analytics -> {
                        if (analytics != null) {
                            if (analytics.getSelectedAnswer().equals(
                                    quiz.getOptions().get(quiz.getCorrectAnswerIndex()))) {
                                correctAnswers++;
                            }
                            totalTime += analytics.getTimeTaken();
                        }
                    });
                }

                long averageTime = totalTime / quizzes.size();

                // Navigate to reward fragment
                Bundle args = new Bundle();
                args.putInt("correctAnswers", correctAnswers);
                args.putInt("totalQuestions", quizzes.size());
                args.putLong("averageTime", averageTime);
                args.putString("categoryName", repository.getCategories().getValue().get(categoryId).getName());

                Navigation.findNavController(v).navigate(
                        R.id.action_quizFragment_to_rewardFragment, args);

            }
        });
*/

nextQuizButton.setOnClickListener(v -> {
    // Add null check before accessing the quizzes list
    if (quizzes == null) {
        Toast.makeText(getContext(), "Quiz data not available", Toast.LENGTH_SHORT).show();
        return;
    }

    currentQuizIndex++;
    if (currentQuizIndex < quizzes.size()) {
        updateProgress();
        displayQuiz(quizzes.get(currentQuizIndex), questionText, optionsGroup, submitButton, nextQuizButton, resultText);
        resultText.setVisibility(View.GONE);
        nextQuizButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        optionsGroup.setEnabled(true);
        selectedAnswer = -1;
    } else {
        // All quizzes completed, navigate to reward screen
        Bundle args = new Bundle();
        args.putInt("correctAnswers", correctAnswers);
        args.putInt("totalQuestions", quizzes.size());
        args.putString("categoryName", "this category"); // Replace with actual category name

        // Navigate to reward fragment
        Navigation.findNavController(v).navigate(
                R.id.action_quizFragment_to_rewardFragment, args);
    }
});

        backToMainButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_quizFragment_to_mainFragment);
        });

        return view;
    }

    private void displayQuiz(Quiz quiz, TextView questionText, RadioGroup optionsGroup, Button submitButton, Button nextQuizButton, TextView resultText) {
        questionText.setText(quiz.getQuestion());
        optionsGroup.removeAllViews();
        for (int i = 0; i < quiz.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(quiz.getOptions().get(i));
            radioButton.setTextAppearance(getContext(), com.google.android.material.R.style.TextAppearance_MaterialComponents_Body1);
            radioButton.setTextColor(getResources().getColor(android.R.color.white));
            radioButton.setPadding(8, 8, 8, 8);
            optionsGroup.addView(radioButton);
        }
        optionsGroup.setOnCheckedChangeListener((group, checkedId) -> selectedAnswer = checkedId);
        startTime = System.currentTimeMillis();
        submitButton.setVisibility(View.VISIBLE);
        nextQuizButton.setVisibility(View.GONE);
        resultText.setVisibility(View.GONE);
        optionsGroup.setEnabled(true);
    }

    private void updateProgress() {
        int progress = (int) (((currentQuizIndex + 1) / (float) quizzes.size()) * 100);
        progressBar.setProgress(progress);
        progressText.setText(String.format("Question %d/%d", currentQuizIndex + 1, quizzes.size()));
    }
}