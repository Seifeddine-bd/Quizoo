package com.seifeddine.bd.quizoo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener listener;
    private Map<Integer, Integer> categoryIconMap; // Map category ID to drawable resource

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
        // Initialize icon map (example)
         categoryIconMap = Map.of(
                1, R.drawable.programming, // Replace with actual drawable
                2, R.drawable.cs  // Replace with actual drawable
        );
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? new ArrayList<>(categories) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryLogo.setImageResource(categoryIconMap.getOrDefault(category.getId(), category.getCategoryImage()));
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        Glide.with(holder.categoryLogo.getContext())
                .load(categoryIconMap.getOrDefault(category.getId(), category.getCategoryImage()))
                .into(holder.categoryLogo);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView categoryLogo;
        TextView categoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryLogo = itemView.findViewById(R.id.category_logo);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}