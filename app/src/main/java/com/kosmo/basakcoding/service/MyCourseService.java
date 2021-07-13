package com.kosmo.basakcoding.service;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyCourseService {

    @GET("my-course")
    public Call<List<HashMap>> getMyCouresList(@Query("memberId") String memberId);
}
