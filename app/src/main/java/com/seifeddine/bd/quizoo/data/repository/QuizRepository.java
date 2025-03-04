// com/seifeddine/bd/quizoo/data/repository/QuizRepository.java
package com.seifeddine.bd.quizoo.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public void syncData() {
        apiService.getCategories().enqueue(new Callback<List<NetworkCategory>>() {
            @Override
            public void onResponse(Call<List<NetworkCategory>> call, Response<List<NetworkCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = new ArrayList<>();
                    for (NetworkCategory nc : response.body()) {
                        categories.add(new Category(nc.getId(), nc.getName()));
                    }
                    categoryDao.insertAll(categories); // Inserts Category entities
                }
            }

            @Override
            public void onFailure(Call<List<NetworkCategory>> call, Throwable t) {}
        });

        apiService.getQuizzes().enqueue(new Callback<List<NetworkQuiz>>() {
            @Override
            public void onResponse(Call<List<NetworkQuiz>> call, Response<List<NetworkQuiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quiz> quizzes = new ArrayList<>();
                    for (NetworkQuiz nq : response.body()) {
                        quizzes.add(new Quiz(nq.getId(), nq.getCategoryId(), nq.getQuestion(), nq.getOptions(), nq.getCorrectAnswerIndex()));
                    }
                    quizDao.insertQuizzes(quizzes); // Inserts Quiz entities
                }
            }

            @Override
            public void onFailure(Call<List<NetworkQuiz>> call, Throwable t) {}
        });
    }

    public LiveData<List<Category>> getCategories() {
        return new MutableLiveData<>(categoryDao.getAllCategories().getValue());
    }

    public LiveData<List<Quiz>> getQuizzesByCategory(int categoryId) {
        return new MutableLiveData<>(quizDao.getQuizzesByCategory(categoryId));
    }

    public LiveData<List<Analytics>> getAnalytics() {
        return analyticsDao.getAllAnalytics();
    }

    public void submitAnalytics(AnalyticsRequest analyticsRequest, Context context) {
        apiService.submitAnalytics(analyticsRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Analytics analytics = new Analytics(
                            analyticsRequest.getQuizId(),
                            analyticsRequest.getUserAnswer(),
                            analyticsRequest.getTimeTaken()
                    );
                    new Thread(() -> analyticsDao.insert(analytics)).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}