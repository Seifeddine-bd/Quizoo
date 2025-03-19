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
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.Random;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.seifeddine.bd.quizoo.R;
public class RewardFragment extends Fragment {
    private int correctAnswers;
    private int totalQuestions;
    private long timeTaken;
    private String categoryName;
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        // Get arguments from bundle
        if (getArguments() != null) {
            correctAnswers = getArguments().getInt("correctAnswers", 0);
            totalQuestions = getArguments().getInt("totalQuestions", 1);
            categoryName = getArguments().getString("categoryName", "this category");
            timeTaken = getArguments().getLong("timeTaken", 1);
            // Calculate score percentage (avoid division by zero)
            int scorePercentage = totalQuestions > 0 ?
                    (int) (((float) correctAnswers / totalQuestions) * 100) : 0;

            // Setup UI elements
            CircularProgressIndicator scoreProgress = view.findViewById(R.id.score_progress);
            TextView scoreText = view.findViewById(R.id.score_text);
            TextView scoreDetails = view.findViewById(R.id.score_details);
            TextView completionText = view.findViewById(R.id.completion_text);
            TextView timedetails = view.findViewById(R.id.time_details);
            MaterialButton continueButton = view.findViewById(R.id.continue_button);

            // Update UI with score information
            scoreProgress.setProgress(scorePercentage);
            scoreText.setText(scorePercentage + "%");
            scoreDetails.setText("You got " + correctAnswers + " out of " + totalQuestions + " questions correct!");
            completionText.setText("You've completed all quizzes in " + categoryName);
            // Convert milliseconds to seconds and format
            double seconds = timeTaken / 1000.0;
            timedetails.setText(String.format("Time taken: %.2f seconds", seconds));
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
*/
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reward, container, false);

    // Get arguments from bundle
    if (getArguments() != null) {
        correctAnswers = getArguments().getInt("correctAnswers", 0);
        totalQuestions = getArguments().getInt("totalQuestions", 1);
        categoryName = getArguments().getString("categoryName", "this category");
        timeTaken = getArguments().getLong("timeTaken", 1);

        // Calculate score percentage
        int scorePercentage = totalQuestions > 0 ?
                (int) (((float) correctAnswers / totalQuestions) * 100) : 0;

        // Setup UI elements
        CircularProgressIndicator scoreProgress = view.findViewById(R.id.score_progress);
        TextView scoreText = view.findViewById(R.id.score_text);
        TextView scoreDetails = view.findViewById(R.id.score_details);
        TextView completionText = view.findViewById(R.id.completion_text);
        TextView timedetails = view.findViewById(R.id.time_details);
        TextView congratsText = view.findViewById(R.id.congrats_text);
        MaterialButton continueButton = view.findViewById(R.id.continue_button);

        // Apply bounce animation to trophy
        View trophyImage = view.findViewById(R.id.trophy_image);
        Animation bounceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        trophyImage.startAnimation(bounceAnimation);

        // Initially hide elements for sequential animation
        congratsText.setAlpha(0f);
        completionText.setAlpha(0f);
        scoreProgress.setProgress(0);
        scoreText.setAlpha(0f);
        scoreDetails.setAlpha(0f);
        timedetails.setAlpha(0f);
        continueButton.setAlpha(0f);

        // Start sequential animations after a delay
        new Handler().postDelayed(() -> {
            // 1. Animate congratulation text
            congratsText.animate().alpha(1f).setDuration(500).start();

            // 2. Animate completion text after delay
            new Handler().postDelayed(() -> {
                completionText.setText("You've completed all quizzes in " + categoryName);
                completionText.animate().alpha(1f).setDuration(500).start();

                // 3. Animate progress indicator with animated counter
                new Handler().postDelayed(() -> {
                    scoreText.setAlpha(1f);
                    ValueAnimator animator = ValueAnimator.ofInt(0, scorePercentage);
                    animator.setDuration(1500);
                    animator.addUpdateListener(animation -> {
                        int animatedValue = (int) animation.getAnimatedValue();
                        scoreProgress.setProgress(animatedValue);
                        scoreText.setText(animatedValue + "%");
                    });
                    animator.start();

                    // Apply color based on score
                    int textColor;
                    if (scorePercentage >= 80) {
                        textColor = getResources().getColor(R.color.successColor);
                        if (scorePercentage == 100) {
                            showConfetti(view); // Show confetti for perfect score
                        }
                    } else if (scorePercentage >= 50) {
                        textColor = getResources().getColor(R.color.gold);
                    } else {
                        textColor = getResources().getColor(R.color.errorColor);
                    }
                    scoreText.setTextColor(textColor);

                    // 4. Animate details text
                    new Handler().postDelayed(() -> {
                        scoreDetails.setText("You got " + correctAnswers + " out of " + totalQuestions + " questions correct!");
                        scoreDetails.animate().alpha(1f).setDuration(500).start();

                        // 5. Animate time details
                        new Handler().postDelayed(() -> {
                            double seconds = timeTaken / 1000.0;
                            timedetails.setText(String.format("Time taken: %.2f seconds", seconds));
                            timedetails.animate().alpha(1f).setDuration(500).start();

                            // 6. Animate continue button with bounce effect
                            new Handler().postDelayed(() -> {
                                continueButton.animate().alpha(1f).setDuration(500).start();
                                continueButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce));
                            }, 300);
                        }, 300);
                    }, 300);
                }, 300);
            }, 300);
        }, 300);

        // Navigate back to categories screen
        continueButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack(R.id.categoriesFragment, false);
        });
    }

    return view;
}

private void showConfetti(View rootView) {
    FrameLayout container = rootView.findViewById(R.id.confetti_container);
    if (container == null) return;

    // Clear any previous confetti
    container.removeAllViews();

    for (int i = 0; i < 50; i++) {
        ImageView confetti = new ImageView(getContext());
        confetti.setImageResource(R.drawable.ic_trophy);
        confetti.setColorFilter(getRandomConfettiColor());

        int size = new Random().nextInt(30) + 10; // Random size between 10-40dp
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.leftMargin = new Random().nextInt(container.getWidth() > 0 ? container.getWidth() : 1000);
        params.topMargin = -size;
        confetti.setLayoutParams(params);

        container.addView(confetti);
        animateConfetti(confetti);
    }
}


    // Simple confetti effect for perfect scores
//    private void showConfetti(View rootView) {
//        ViewGroup container = (ViewGroup) rootView.getParent();
//        for (int i = 0; i < 50; i++) {
//            ImageView confetti = new ImageView(getContext());
//            confetti.setImageResource(R.drawable.ic_trophy); // Use a small icon
//            confetti.setColorFilter(getRandomConfettiColor());
//
//            int size = new Random().nextInt(30) + 10; // Random size between 10-40dp
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
//            params.leftMargin = new Random().nextInt(rootView.getWidth());
//            params.topMargin = -size;
//            confetti.setLayoutParams(params);
//
//            if (container instanceof FrameLayout) {
//                ((FrameLayout) container).addView(confetti);
//                animateConfetti(confetti);
//            }
//        }
//    }

    private int getRandomConfettiColor() {
        int[] colors = {
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA,
                Color.CYAN, getResources().getColor(R.color.gold)
        };
        return colors[new Random().nextInt(colors.length)];
    }

//    private void animateConfetti(ImageView confetti) {
//        ObjectAnimator fallDown = ObjectAnimator.ofFloat(confetti, "translationY",
//                -confetti.getHeight(), ((View)confetti.getParent()).getHeight());
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(confetti, "rotation",
//                0f, 360f * (new Random().nextFloat() * 4 + 1));
//        ObjectAnimator sway = ObjectAnimator.ofFloat(confetti, "translationX",
//                confetti.getTranslationX(), confetti.getTranslationX() + new Random().nextInt(200) - 100);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(fallDown, rotate, sway);
//        animatorSet.setDuration(3000 + new Random().nextInt(5000));
//        animatorSet.setInterpolator(new AccelerateInterpolator(0.1f));
//
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                ((ViewGroup)confetti.getParent()).removeView(confetti);
//            }
//        });
//
//        animatorSet.start();
//    }

    private void animateConfetti(ImageView confetti) {
        if (confetti.getParent() == null) return;

        int parentHeight = ((View)confetti.getParent()).getHeight();
        if (parentHeight <= 0) parentHeight = 2000; // Fallback height

        ObjectAnimator fallDown = ObjectAnimator.ofFloat(confetti, "translationY",
                -confetti.getHeight(), parentHeight);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(confetti, "rotation",
                0f, 360f * (new Random().nextFloat() * 4 + 1));
        ObjectAnimator sway = ObjectAnimator.ofFloat(confetti, "translationX",
                confetti.getTranslationX(), confetti.getTranslationX() + new Random().nextInt(200) - 100);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fallDown, rotate, sway);
        animatorSet.setDuration(3000 + new Random().nextInt(5000));
        animatorSet.setInterpolator(new AccelerateInterpolator(0.1f));

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (confetti.getParent() != null) {
                    ((ViewGroup)confetti.getParent()).removeView(confetti);
                }
            }
        });

        animatorSet.start();
    }
}