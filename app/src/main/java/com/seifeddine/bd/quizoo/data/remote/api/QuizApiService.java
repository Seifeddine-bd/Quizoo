package com.seifeddine.bd.quizoo.data.remote.api;


import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import com.seifeddine.bd.quizoo.data.remote.dto.AnalyticsRequest;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkCategory;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkQuiz;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface QuizApiService {
    @GET("api/v1/categories")
    Call<List<Category>> getAllCategories();

    @GET("api/v1/quizzes")
    Call<List<Quiz>> getAllQuizzes();

    @GET("api/v1/quizzes/category/{categoryId}")
    Call<List<Quiz>> getQuizzesByCategoryId(@Path("categoryId") Long categoryId);

    @POST("api/v1/quizzes")
    Call<Quiz> createQuiz(@Body Quiz quiz);

    @PUT("api/v1/quizzes/{quizId}")
    Call<Quiz> updateQuiz(@Path("quizId") Long quizId, @Body Quiz quiz);

    @POST("api/v1/analytics")
    Call<Analytics> createAnalytics(@Body Analytics analytics);

    @GET("api/v1/analytics")
    Call<List<Analytics>> getAllAnalytics();
        
    @POST("analytics")
    Call<Void> submitAnalytics(@Body AnalyticsRequest request);

    @GET("categories")
    Call<List<NetworkCategory>> getCategories();

    @GET("quizzes")
    Call<List<NetworkQuiz>> getQuizzes();

}