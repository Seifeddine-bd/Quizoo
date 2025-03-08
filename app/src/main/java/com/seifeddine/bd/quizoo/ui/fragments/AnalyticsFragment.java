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
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;
import com.seifeddine.bd.quizoo.ui.adapters.AnalyticsAdapter;

import java.util.ArrayList;

public class AnalyticsFragment extends Fragment {
    private QuizRepository repository;
    private AnalyticsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.analytics_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AnalyticsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        repository = new QuizRepository(
                AppDatabase.getDatabase(getContext()).quizDao(),
                AppDatabase.getDatabase(getContext()).categoryDao(),
                AppDatabase.getDatabase(getContext()).analyticsDao(),
                RetrofitClient.getApiService()
        );

        repository.getAnalytics().observe(getViewLifecycleOwner(), analytics -> {
            adapter.setAnalytics(analytics);
        });

        return view;
    }
}