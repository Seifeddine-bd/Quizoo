package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.adapters.CategoryAdapter;

import java.util.List;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    private QuizRepository repository;
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        TextView statusText = view.findViewById(R.id.status_text);
        RecyclerView recyclerView = view.findViewById(R.id.category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(category -> {
            Bundle args = new Bundle();
            args.putInt("categoryId", category.getId());
            NavHostFragment.findNavController(this).navigate(R.id.nav_quiz, args);
        });
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        statusText.setText("Loading categories...");
        statusText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        repository.getCategories().observe(getViewLifecycleOwner(), categories -> {
            Log.d(TAG, "Categories from LiveData: " + (categories != null ? categories.size() : "null"));
            if (categories != null && !categories.isEmpty()) {
                adapter.setCategories(categories);
                statusText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                statusText.setText("No categories available");
                statusText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        return view;
    }
}