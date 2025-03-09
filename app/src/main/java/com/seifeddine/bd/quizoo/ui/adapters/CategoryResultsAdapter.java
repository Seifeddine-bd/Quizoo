package com.seifeddine.bd.quizoo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryResultsAdapter extends RecyclerView.Adapter<CategoryResultsAdapter.CategoryViewHolder> {

    private Map<Integer, String> categoryNames;
    private Map<Integer, List<Quiz>> quizzesByCategory;
    private Map<Integer, List<Analytics>> analyticsByCategory;
    private Map<String, Quiz> quizMap;

    public void setData(Map<Integer, String> categoryNames, Map<Integer, List<Quiz>> quizzesByCategory,
                        Map<Integer, List<Analytics>> analyticsByCategory, Map<String, Quiz> quizMap) {
        this.categoryNames = categoryNames;
        this.quizzesByCategory = quizzesByCategory;
        this.analyticsByCategory = analyticsByCategory;
        this.quizMap = quizMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_result, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (categoryNames != null && !categoryNames.isEmpty()) {
            Integer categoryId = new ArrayList<>(categoryNames.keySet()).get(position);
            String categoryName = categoryNames.get(categoryId);
            holder.categoryName.setText(categoryName);

            // Set up inner RecyclerView for quizzes
            QuizResultsAdapter quizAdapter = new QuizResultsAdapter(quizMap, analyticsByCategory.get(categoryId), quizzesByCategory, categoryId);
            holder.quizRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.quizRecyclerView.setAdapter(quizAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return categoryNames != null ? categoryNames.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView categoryCard;
        TextView categoryName;
        RecyclerView quizRecyclerView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryCard = itemView.findViewById(R.id.category_card);
            categoryName = itemView.findViewById(R.id.category_name);
            quizRecyclerView = itemView.findViewById(R.id.quiz_recycler_view); // Fixed typo: findViewId -> findViewById
        }
    }
}