package com.kosmo.basakcoding.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.adapters.CurriculumListAdapter;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.VideoService;
import com.kosmo.basakcoding.utilities.Constants;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;

public class CourseVideoActivity extends AppCompatActivity {
    public static final String TAG = "basakcoding";
    private HashMap video = new HashMap();

    public static int lastTappedPosition = -1;
    public static String currPlayingVideoId = null;
    public static ViewParent tappedParentView = null;

    PlayerView playerView;
    SimpleExoPlayer player;
    ProgressBar progressBar;
    ImageView btnFullScreen;
    TextView textTitle, textTeacherName;

    private RecyclerView recyclerView;
    private CurriculumListAdapter curriculumListAdapter;

    boolean playWhenReady = true;
    boolean isLandscape = false;
    int currentWindow = 0;
    int originHeight = 0;
    long playbackPosition = 0;
    String videoUri;

    private PreferenceManager preferenceManager;
    private VideoService videoService;

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24)
            initPlayer();
    }

    @Override
    protected void onStop() {
        if (Util.SDK_INT >= 24)
            releasePlayer();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            initPlayer();
            hideSystemUI();
        }
    }

    @Override
    protected void onPause() {
        if (Util.SDK_INT < 24) releasePlayer();
        super.onPause();
    }

    private void hideSystemUI() {
        getWindow().setDecorFitsSystemWindows(false);
        if (getWindow().getInsetsController() != null) {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_video);

        // 초기화
        doInitialize();

        Intent intent = getIntent();
        String courseId = intent.getStringExtra("COURSE_ID");
        String title = intent.getStringExtra("TITLE");
        String adminName = intent.getStringExtra("ADMIN_NAME");


        Call<HashMap> call = videoService.getVideo(preferenceManager.getString(Constants.KEY_MEMBER_ID), courseId);
        call.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if (response.isSuccessful()) {
                    video = response.body();

                    currPlayingVideoId = video.get("LAST_VIDEO_ID").toString();
                    // 리사이클러 뷰
                    List<HashMap> curriculumList = (List<HashMap>) video.get("curriculum");
                    int startIndex = 1;
                    for (int i = 0; i < curriculumList.size(); i++) {
                        curriculumList.get(i).put("cIndex", i + 1);
                        List<HashMap> videoList = (List<HashMap>) curriculumList.get(i).get("videos");
                        for (int j = 0; j < videoList.size(); j++) {
                            videoList.get(j).put("index", startIndex++);
                            videoList.get(j).put("courseId", courseId);
                        }
                    }


                    HashMap currVideo = (HashMap) video.get("currVideo");
                    videoUri = currVideo.get("videoPath").toString();
                    textTitle.setText(title);
                    textTeacherName.setText(adminName);

                    initPlayer();

                    curriculumListAdapter = new CurriculumListAdapter(getApplicationContext(), curriculumList, player, currPlayingVideoId);

                    recyclerView.setAdapter(curriculumListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.i(TAG, "에러:" + t.getMessage());
            }
        });


        // 전체 화면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    private void doInitialize() {
        lastTappedPosition = -1;
        tappedParentView = null;

        preferenceManager = new PreferenceManager(getApplicationContext());
        videoService = ApiClient.getRetrofit().create(VideoService.class);
        recyclerView = findViewById(R.id.currRV);

        playerView = findViewById(R.id.playerView);
        progressBar = findViewById(R.id.progress_bar);
        btnFullScreen = playerView.findViewById(R.id.btn_fullscreen);
        originHeight = playerView.getLayoutParams().height;
        textTitle = findViewById(R.id.textTitle);
        textTeacherName = findViewById(R.id.textTeacherName);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initPlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);

        MediaItem mediaItem = MediaItem.fromUri(KEY_BASE_URL + videoUri);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
        player.prepare();
        player.play();
        progressBar.setVisibility(View.GONE);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_BUFFERING)
                    progressBar.setVisibility(View.VISIBLE);
                else if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                } else if (state == Player.STATE_ENDED) {
                    Call<Integer> result = videoService.updateSeen(preferenceManager.getString(Constants.KEY_MEMBER_ID), currPlayingVideoId);
                    result.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                Log.i(TAG, currPlayingVideoId);
                                recreate();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.i(TAG, "에러:" + t.getMessage());
                        }
                    });
                }
            }
        });

        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLandscape) {
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen, null));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    playerView.getLayoutParams().height = originHeight;
                    isLandscape = false;
                } else {
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    isLandscape = true;
                }
            }
        });
    }
}