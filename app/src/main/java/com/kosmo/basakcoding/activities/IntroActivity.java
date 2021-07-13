package com.kosmo.basakcoding.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kosmo.basakcoding.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//http://ajaxload.info/ 로딩 gif이미지 만드는 사이트
//gif 이미지를 ImageView에 로드 하려면
//1. gradle파일에 implementation 'com.github.bumptech.glide:glide:4.10.0' 추가
//https://github.com/bumptech/glide
//2. 다음 코드 작성
//ImageView loading=findViewById(R.id.loading);
//Glide.with(this).load(R.drawable.ajax_loader).into(loading);
public class IntroActivity extends AppCompatActivity {

    VideoView mVideoView;
    MediaController mediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);
        VideoView imageView = findViewById(R.id.videoView1);

        mediaController = new MediaController(this);
        mVideoView = findViewById(R.id.videoView1);

        mediaController.setAnchorView(mVideoView);
        Uri localUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.main);
        mVideoView.setVideoURI(localUri);
        mVideoView.setMediaController(mediaController);
        mVideoView.start();

        //몇초 지연후 로그인 화면으로 이동시키기
        /*
        //API LEVEL 30부터  Handler Deprecated
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(intent);
                //전환된 화면(LoginActivity)Destroy시 인트로 화면도 Destroy하기
                finish();
            }
        };
        //3초 지연후 LoginActivity로 전환
        handler.sendEmptyMessageDelayed(0,3000);*/

        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this,SignInActivity.class);
                startActivity(intent);
                //전환된 화면(LoginActivity)Destroy시 인트로 화면도 Destroy하기
                finish();
            }
        };
        worker.schedule(runnable, 3000, TimeUnit.MILLISECONDS);



    }/////////////onCreate
}//////////////class
