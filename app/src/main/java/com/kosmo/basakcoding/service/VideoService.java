package com.kosmo.basakcoding.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VideoService {

    @GET("course/video")
    public Call<HashMap> getVideo(@Query("memberId") String memberId,
                                  @Query("courseId") String courseId);

    @FormUrlEncoded
    @POST("seen")
    public Call<Integer> updateSeen(@Field("memberId") String memberId, @Field("videoId") String videoId);

}
