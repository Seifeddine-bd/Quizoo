package com.seifeddine.bd.quizoo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private List<Analytics> analyticsList;

    public ResultsAdapter(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList != null ? analyticsList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);
        holder.quizIdText.setText("Quiz ID: " + analytics.getQuizId());
        holder.selectedAnswerText.setText("Selected Answer: " + (analytics.getSelectedAnswer() != null ? analytics.getSelectedAnswer() : "N/A"));
        holder.timeTakenText.setText("Time Taken: " + (analytics.getTimeTaken() / 1000) + "s");
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView quizIdText, selectedAnswerText, timeTakenText;

        ViewHolder(View itemView) {
            super(itemView);
            quizIdText = itemView.findViewById(R.id.result_quiz_id);
            selectedAnswerText = itemView.findViewById(R.id.result_selected_answer);
            timeTakenText = itemView.findViewById(R.id.result_time_taken);
        }
    }
}