package com.seifeddine.bd.quizoo.data.remote.api;

import com.seifeddine.bd.quizoo.data.remote.dto.AnalyticsRequest;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkCategory;
import com.seifeddine.bd.quizoo.data.remote.dto.NetworkQuiz;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QuizApiService {
    @POST("analytics")
    Call<Void> submitAnalytics(@Body AnalyticsRequest request);

    @GET("categories")
    Call<List<NetworkCategory>> getCategories();

    @GET("quizzes")
    Call<List<NetworkQuiz>> getQuizzes();
}