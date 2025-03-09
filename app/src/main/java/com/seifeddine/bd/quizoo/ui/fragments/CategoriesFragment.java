package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.adapters.CategoryAdapter;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private QuizRepository repository;
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.categories_recycler_view);
        int spanCount = 2; // 2 columns for grid
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        adapter = new CategoryAdapter(category -> {
            // Use NavController for navigation
            Bundle args = new Bundle();
            args.putInt("categoryId", category.getId());
            Navigation.findNavController(view).navigate(R.id.action_categoriesFragment_to_quizFragment, args);
        });
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        // Observe categories and update adapter
        repository.getCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.setCategories(categories);
        });

        return view;
    }
}