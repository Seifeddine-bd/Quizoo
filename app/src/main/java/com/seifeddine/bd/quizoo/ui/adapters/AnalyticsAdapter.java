package com.seifeddine.bd.quizoo.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder> {
    private List<Analytics> analyticsList;
    private Map<String, Quiz> quizMap;
    private Map<Integer, List<Quiz>> quizzesByCategory;
    private Map<Integer, String> categoryNames;

    public AnalyticsAdapter(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList != null ? analyticsList : new ArrayList<>();
    }

    public void setAnalytics(List<Analytics> newAnalyticsList) {
        this.analyticsList = newAnalyticsList != null ? new ArrayList<>(newAnalyticsList) : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setAnalytics(List<Analytics> newAnalyticsList, Map<String, Quiz> quizMap,
                             Map<Integer, List<Quiz>> quizzesByCategory, Map<Integer, String> categoryNames) {
        this.analyticsList = newAnalyticsList != null ? new ArrayList<>(newAnalyticsList) : new ArrayList<>();
        this.quizMap = quizMap;
        this.quizzesByCategory = quizzesByCategory;
        this.categoryNames = categoryNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analytics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);
        if (quizMap != null && quizzesByCategory != null && categoryNames != null) {
            Quiz quiz = quizMap.get(analytics.getQuizId());
            if (quiz != null) {
                int categoryId = quiz.getCategoryId();
                List<Quiz> quizzesInCategory = quizzesByCategory.get(categoryId);
                String categoryName = categoryNames.get(categoryId);
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
                Log.d("AnalyticsAdapter", "Quiz: " + quiz + ", Category: " + categoryName);

                String displayText = String.format(
                        "Quiz %d of %d in %s\nQuiz ID: %s\nSelected Answer: %s\nTime Taken: %dms",
                        quizNumber, totalQuizzesInCategory, categoryName != null ? categoryName : "Unknown",
                        analytics.getQuizId(), analytics.getSelectedAnswer(), analytics.getTimeTaken()
                );
                holder.analyticsText.setText(displayText);
                return;
            }
        }
        holder.analyticsText.setText(String.format(
                "Quiz ID: %s\nSelected Answer: %s\nTime Taken: %dms",
                analytics.getQuizId(), analytics.getSelectedAnswer(), analytics.getTimeTaken()
        ));
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView analyticsText;

        ViewHolder(View itemView) {
            super(itemView);
            analyticsText = itemView.findViewById(R.id.analytics_text);
        }
    }
}