package com.seifeddine.bd.quizoo.data.remote;

import com.seifeddine.bd.quizoo.data.remote.api.QuizApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/"; // Use emulator localhost
    private static QuizApiService apiService;

    public static QuizApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(QuizApiService.class);
        }
        return apiService;
    }
}