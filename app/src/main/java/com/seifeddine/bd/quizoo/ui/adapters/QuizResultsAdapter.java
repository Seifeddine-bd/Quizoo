package com.seifeddine.bd.quizoo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizResultsAdapter extends RecyclerView.Adapter<QuizResultsAdapter.QuizViewHolder> {

    private Map<String, Quiz> quizMap;
    private List<Analytics> analyticsList;
    private Map<Integer, List<Quiz>> quizzesByCategory;
    private int categoryId;

    public QuizResultsAdapter(Map<String, Quiz> quizMap, List<Analytics> analyticsList,
                              Map<Integer, List<Quiz>> quizzesByCategory, int categoryId) {
        this.quizMap = quizMap;
        this.analyticsList = analyticsList != null ? analyticsList : new ArrayList<>();
        this.quizzesByCategory = quizzesByCategory;
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_result, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);
        Quiz quiz = quizMap.get(analytics.getQuizId());
        if (quiz != null) {
            List<Quiz> quizzesInCategory = this.quizzesByCategory.get(categoryId);
            int quizNumber = 0;
            int totalQuizzesInCategory = quizzesInCategory != null ? quizzesInCategory.size() : 0;

            if (quizzesInCategory != null) {
                for (int i = 0; i < quizzesInCategory.size(); i++) {
                    if (quizzesInCategory.get(i).getId().equals(quiz.getId())) {
                        quizNumber = i + 1;
                        break;
                    }
                }
            }

            // Check if the answer is correct
            String correctAnswer = quiz.getOptions().get(quiz.getCorrectAnswerIndex());
            boolean isCorrect = analytics.getSelectedAnswer() != null && analytics.getSelectedAnswer().equals(correctAnswer);

            // Update UI
            String displayText = String.format(
                    "Quiz %d of %d\nSelected Answer: %s\nTime Taken: %dms",
                    quizNumber, totalQuizzesInCategory,
                    analytics.getSelectedAnswer(), analytics.getTimeTaken()
            );
            holder.quizResultText.setText(displayText);

            // Show right/wrong icon
            holder.resultIcon.setVisibility(View.VISIBLE);
            holder.resultIcon.setImageResource(isCorrect ? R.drawable.ic_check : R.drawable.ic_cross);
        }
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView quizCard;
        TextView quizResultText;
        ImageView resultIcon;

        QuizViewHolder(View itemView) {
            super(itemView);
            quizCard = itemView.findViewById(R.id.quiz_card);
            quizResultText = itemView.findViewById(R.id.quiz_result_text);
            resultIcon = itemView.findViewById(R.id.result_icon);
        }
    }
}