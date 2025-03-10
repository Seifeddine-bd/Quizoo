package com.seifeddine.bd.quizoo.data.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.local.dao.AnalyticsDao;
import com.seifeddine.bd.quizoo.data.local.dao.CategoryDao;
import com.seifeddine.bd.quizoo.data.local.dao.QuizDao;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.remote.api.QuizApiService;
import com.seifeddine.bd.quizoo.data.remote.dto.AnalyticsRequest;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkCategory;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkQuiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRepository {
    private static final String TAG = "QuizRepository";
    private final QuizDao quizDao;
    private final CategoryDao categoryDao;
    private final AnalyticsDao analyticsDao;
    private final QuizApiService apiService;
    private final ExecutorService executorService;

    public QuizRepository(QuizDao quizDao, CategoryDao categoryDao, AnalyticsDao analyticsDao, QuizApiService apiService) {
        this.quizDao = quizDao;
        this.categoryDao = categoryDao;
        this.analyticsDao = analyticsDao;
        this.apiService = apiService;
        this.executorService = Executors.newSingleThreadExecutor();
        fetchCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDao.getAllCategories();
    }

    public LiveData<List<Quiz>> getQuizzesByCategory(int categoryId) {
        return quizDao.getQuizzesByCategory(categoryId);
    }

    public LiveData<Quiz> getQuizById(String quizId) {
        return quizDao.getQuizById(quizId);
    }

    public LiveData<List<Analytics>> getAnalytics() {
        return analyticsDao.getAllAnalytics();
    }

    public LiveData<Analytics> getAnalyticsByQuizId(String quizId) {
        return analyticsDao.getAnalyticsByQuizId(quizId);
    }

    public void submitAnalytics(AnalyticsRequest request, Context context) {
        apiService.submitAnalytics(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Analytics submitted successfully");
                    executorService.execute(() -> {
                        Analytics analytics = new Analytics(request.getQuizId(), request.getSelectedAnswer(), request.getTimeTaken());
                        AppDatabase.getDatabase(context).analyticsDao().insert(analytics);
                    });
                } else {
                    Log.e(TAG, "Failed to submit analytics: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error submitting analytics", t);
                executorService.execute(() -> {
                    Analytics analytics = new Analytics(request.getQuizId(), request.getSelectedAnswer(), request.getTimeTaken());
                    AppDatabase.getDatabase(context).analyticsDao().insert(analytics);
                });
            }
        });
    }

    private void fetchCategories() {
        apiService.getCategories().enqueue(new Callback<List<NetworkCategory>>() {
            @Override
            public void onResponse(Call<List<NetworkCategory>> call, Response<List<NetworkCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = new ArrayList<>();
                    for (NetworkCategory networkCategory : response.body()) {
                        categories.add(new Category(networkCategory.getId(), networkCategory.getName(),networkCategory.getCategoryImage()));
                    }
                    executorService.execute(() -> categoryDao.insertAll(categories));
                    fetchQuizzes();
                } else {
                    Log.e(TAG, "Failed to fetch categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<NetworkCategory>> call, Throwable t) {
                Log.e(TAG, "Error fetching categories", t);
            }
        });
    }

    private void fetchQuizzes() {
        apiService.getQuizzes().enqueue(new Callback<List<NetworkQuiz>>() {
            @Override
            public void onResponse(Call<List<NetworkQuiz>> call, Response<List<NetworkQuiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quiz> quizzes = new ArrayList<>();
                    for (NetworkQuiz networkQuiz : response.body()) {
                        quizzes.add(new Quiz(
                                networkQuiz.getId(),
                                networkQuiz.getCategoryId(),
                                networkQuiz.getQuestion(),
                                networkQuiz.getOptions(),
                                networkQuiz.getCorrectAnswerIndex()
                        ));
                    }
                    executorService.execute(() -> quizDao.insertAll(quizzes));
                } else {
                    Log.e(TAG, "Failed to fetch quizzes: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<NetworkQuiz>> call, Throwable t) {
                Log.e(TAG, "Error fetching quizzes", t);
            }
        });
    }
}