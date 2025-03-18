package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.seifeddine.bd.quizoo.R;
public class RewardFragment extends Fragment {
    private int correctAnswers;
    private int totalQuestions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        // Get arguments from bundle
        if (getArguments() != null) {
            correctAnswers = getArguments().getInt("correctAnswers", 0);
            totalQuestions = getArguments().getInt("totalQuestions", 1);
            String categoryName = getArguments().getString("categoryName", "this category");

            // Calculate score percentage (avoid division by zero)
            int scorePercentage = totalQuestions > 0 ?
                    (int) (((float) correctAnswers / totalQuestions) * 100) : 0;

            // Setup UI elements
            CircularProgressIndicator scoreProgress = view.findViewById(R.id.score_progress);
            TextView scoreText = view.findViewById(R.id.score_text);
            TextView scoreDetails = view.findViewById(R.id.score_details);
            TextView completionText = view.findViewById(R.id.completion_text);
            MaterialButton continueButton = view.findViewById(R.id.continue_button);

            // Update UI with score information
            scoreProgress.setProgress(scorePercentage);
            scoreText.setText(scorePercentage + "%");
            scoreDetails.setText("You got " + correctAnswers + " out of " + totalQuestions + " questions correct!");
            completionText.setText("You've completed all quizzes in " + categoryName);

            // Add animation to trophy image
            View trophyImage = view.findViewById(R.id.trophy_image);
            if (trophyImage != null) {
                trophyImage.setAlpha(0f);
                trophyImage.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .start();
            }

            // Navigate back to categories screen
            continueButton.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(requireView());
                navController.popBackStack(R.id.categoriesFragment, false);
            });
        }

        return view;
    }
}