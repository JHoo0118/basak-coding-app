package com.kosmo.basakcoding.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.models.CurriculumDTO;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.CoursesService;
import com.kosmo.basakcoding.service.MyCourseService;
import com.kosmo.basakcoding.utilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;

public class CourseDetailActivity extends AppCompatActivity {
    public static final String TAG = "basakcoding";
    private HashMap courseDetail = new HashMap();
    private CoursesService coursesService;
    private PreferenceManager preferenceManager;

    ImageView thumbnailImage, backBtnImage;
    TextView textTitle, textShortDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("COURSE_ID");

        coursesService = ApiClient.getRetrofit().create(CoursesService.class);
        this.preferenceManager = new PreferenceManager(getApplicationContext());

        // 아이디 얻기
        doIntialize();

        backBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Call<HashMap> call = coursesService.getCourseDetail(courseId);
        call.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if (response.isSuccessful()) {
                    courseDetail = response.body();
                    List<CurriculumDTO> curriculum = (List<CurriculumDTO>) courseDetail.get("curriculum");

                    // 강의 썸네일 설정
                    Picasso.get().load(KEY_BASE_URL + "/upload/course/" +
                            courseId +"/thumbnail/" +
                            courseDetail.get("THUMBNAIL").toString()).into(thumbnailImage);

                    // 강의 제목 설정
                    textTitle.setText(courseDetail.get("TITLE").toString());

                    // 강의 요약 설정
                    textShortDescription.setText(courseDetail.get("SHORT_DESCRIPTION").toString());

                    Log.i(TAG, curriculum.toString());
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.i(TAG, "에러:"+t.getMessage());
            }
        });
    }

    private void doIntialize() {
        // 아이디 얻기
        backBtnImage = findViewById(R.id.imageBack);
        thumbnailImage = findViewById(R.id.imageThumnail);
        textTitle = findViewById(R.id.textTitle);
        textShortDescription = findViewById(R.id.textShortDescription);
    }
}