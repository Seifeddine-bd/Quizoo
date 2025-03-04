package com.seifeddine.bd.quizoo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;

import java.util.ArrayList;
import java.util.List;

// AnalyticsAdapter.java (in ui/adapters/)
public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder> {
    private List<Analytics> analyticsList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analytics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);
        holder.textView.setText(String.format("Quiz %d: Answered %d in %dms",
                analytics.getQuizId(), analytics.getUserAnswer(), analytics.getTimeTaken()));
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    public void setAnalytics(List<Analytics> analytics) {
        this.analyticsList = analytics;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.analytics_text);
        }
    }
}