package com.kosmo.basakcoding.service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(KEY_BASE_URL + "/api/1.0/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}