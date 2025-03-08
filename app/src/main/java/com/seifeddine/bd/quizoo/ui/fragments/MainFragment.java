package com.seifeddine.bd.quizoo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.seifeddine.bd.quizoo.R;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        MaterialButton categoriesButton = view.findViewById(R.id.categories_button);
        MaterialButton resultsButton = view.findViewById(R.id.results_button);

        categoriesButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_categoriesFragment);
        });

        resultsButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_resultsFragment);
        });

        return view;
    }
}