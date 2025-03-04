// com/seifeddine/bd/quizoo/ui/fragments/CategoriesFragment.java
package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.activities.QuizActivity;
import com.seifeddine.bd.quizoo.ui.adapters.CategoryAdapter;

public class CategoriesFragment extends Fragment {
    private QuizRepository repository;
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.category_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter =  new CategoryAdapter(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onClick(Category category) {
                QuizActivity.start(getContext(), category.getId());
            }
        });
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );
        repository.syncData();
        repository.getCategories().observe(getViewLifecycleOwner(), categories -> adapter.setCategories(categories));

        return view;
    }
}