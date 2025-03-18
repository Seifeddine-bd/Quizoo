package com.seifeddine.bd.quizoo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.seifeddine.bd.quizoo.R;

public class ImageUtils {
    public static int getDrawableResourceId(Context context, String imageUrl) {
        switch (imageUrl) {
            case "programming":
                return R.drawable.programming;
            case "computer_science":
                return R.drawable.cs;
            case "ai":
                return R.drawable.ai;
            case "data_structures":
                return R.drawable.ds;
            case "algorithms":
                return R.drawable.algo;
            default:
                return R.drawable.ic_launcher_background; // Default image
        }
    }
}