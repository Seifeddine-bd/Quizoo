package com.seifeddine.bd.quizoo.data.remote;

import com.seifeddine.bd.quizoo.data.remote.api.QuizApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Emulator localhost
    private static Retrofit retrofit = null;

    public static QuizApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(QuizApiService.class);
    }
}