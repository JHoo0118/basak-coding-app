package com.kosmo.basakcoding.service;

import com.kosmo.basakcoding.models.LoginDTO;
import com.kosmo.basakcoding.models.MemberDTO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {
    @POST("login")
    Call<MemberDTO> login(@Body LoginDTO dto);
}
