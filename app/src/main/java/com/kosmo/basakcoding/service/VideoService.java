package com.kosmo.basakcoding.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {

    @GET("course/video")
    public Call<HashMap> getVideo(@Query("memberId") String memberId,
                                  @Query("courseId") String courseId,
                                  @Query("videoId") String videoId);
}
