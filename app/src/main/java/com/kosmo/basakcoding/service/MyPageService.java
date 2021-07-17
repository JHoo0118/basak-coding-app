package com.kosmo.basakcoding.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyPageService {

    @GET("mypage")
    public Call<HashMap> getMyPageList(@Query("memberId") String memberId);
}
