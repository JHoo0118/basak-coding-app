package com.kosmo.basakcoding.service;

import com.kosmo.basakcoding.models.CatalogDTO;
import com.kosmo.basakcoding.models.LoginDTO;
import com.kosmo.basakcoding.models.MemberDTO;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CoursesService {

    @POST("courses")
    Call<CatalogDTO> getCoursesList(@Body LoginDTO dto);

    @GET("course")
    public Call<List<HashMap>> getCoursesList(@Query("title") String title);

    @GET("course/detail")
    public Call<HashMap> getCourseDetail(@Query("courseId") String courseId);
}
