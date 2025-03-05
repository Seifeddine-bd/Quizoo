package com.seifeddine.bd.quizoo.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.seifeddine.bd.quizoo.data.local.dao.AnalyticsDao;
import com.seifeddine.bd.quizoo.data.local.dao.CategoryDao;
import com.seifeddine.bd.quizoo.data.local.dao.QuizDao;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.remote.api.QuizApiService;
import com.seifeddine.bd.quizoo.data.remote.dto.AnalyticsRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRepository {
    private final QuizDao quizDao;
    private final CategoryDao categoryDao;
    private final AnalyticsDao analyticsDao;
    private final QuizApiService apiService;

    public QuizRepository(QuizDao quizDao, CategoryDao categoryDao, AnalyticsDao analyticsDao, QuizApiService apiService) {
        this.quizDao = quizDao;
        this.categoryDao = categoryDao;
        this.analyticsDao = analyticsDao;
        this.apiService = apiService;
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDao.getAllCategories();
    }

    public LiveData<List<Quiz>> getQuizzesByCategory(int categoryId) {
        return quizDao.getQuizzesByCategory(categoryId); // Return LiveData directly from QuizDao
    }

    public LiveData<List<Analytics>> getAnalytics() {
        return analyticsDao.getAllAnalytics();
    }

    public void submitAnalytics(AnalyticsRequest analyticsRequest, Context context) {
        Analytics analytics = new Analytics(
                analyticsRequest.getQuizId(),
                analyticsRequest.getUserAnswer(),
                analyticsRequest.getTimeTaken()
        );
        new Thread(() -> analyticsDao.insert(analytics)).start();
    }
}