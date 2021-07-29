package com.kosmo.basakcoding.service;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoursesService {

    @GET("courses")
    public Call<List<HashMap>> getCoursesList();

    @GET("course")
    public Call<List<HashMap>> getCoursesList(@Query("title") String title);

    @GET("course/detail")
    public Call<HashMap> getCourseDetail(@Query("memberId") String memberId, @Query("courseId") String courseId);
}
