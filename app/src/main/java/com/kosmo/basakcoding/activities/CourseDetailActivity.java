package com.kosmo.basakcoding.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.models.CurriculumDTO;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.CoursesService;
import com.kosmo.basakcoding.utilities.Constants;
import com.kosmo.basakcoding.utilities.GlideImageGetter;
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
    private GlideImageGetter glideImageGetter;
    private int isPurchased = 0;
    private String name = null;
    private String title = null;
    private String courseId = null;

    ImageView thumbnailImage, backBtnImage;
    TextView textTitle, textShortDescription, textReview, textReviewCount,
            textTeacherName, textCreatedAt, textPrice, textDescription,
            textVideoLength, textVideoCount;
    RatingBar ratingBar;
    Button buttonPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Intent intent = getIntent();
        courseId = intent.getStringExtra("COURSE_ID");

        coursesService = ApiClient.getRetrofit().create(CoursesService.class);
        this.preferenceManager = new PreferenceManager(getApplicationContext());

        // ????????? ??????
        doInitialize();

        backBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        Call<HashMap> call = coursesService.getCourseDetail(preferenceManager.getString(Constants.KEY_MEMBER_ID), courseId);
        call.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if (response.isSuccessful()) {
                    courseDetail = response.body();
                    List<CurriculumDTO> curriculum = (List<CurriculumDTO>) courseDetail.get("curriculum");


                    isPurchased = Integer.parseInt(courseDetail.get("ALREADY_PAYMENT").toString());
                    if (isPurchased == 1) buttonPurchase.setText("?????? ????????????");

                    // ?????? ????????? ??????
                    Picasso.get().load(KEY_BASE_URL + "/upload/course/" +
                            courseId + "/thumbnail/" +
                            courseDetail.get("THUMBNAIL").toString()).into(thumbnailImage);

                    // ?????? ?????? ??????
                    textTitle.setText(courseDetail.get("TITLE").toString());
                    title = courseDetail.get("TITLE").toString();

                    // ?????? ?????? ??????
                    textShortDescription.setText(courseDetail.get("SHORT_DESCRIPTION").toString());

                    // ?????? ?????? ??????
                    String avgRating = courseDetail.get("AVG_RATING").toString();
                    if (avgRating.equals("NaN")) avgRating = "0.00";
                    textReview.setText(avgRating);
                    ratingBar.setRating(Float.parseFloat(avgRating));
                    textReviewCount.setText("(" + courseDetail.get("REVIEW_COUNT").toString() + "?????? ??????)");

                    // ?????? ??????
                    textTeacherName.setText(courseDetail.get("NAME").toString());
                    name = courseDetail.get("NAME").toString();

                    // ?????? ?????? ??????
                    String createdAt = courseDetail.get("CREATED_AT").toString();
                    textCreatedAt.setText("?????? ??????: " + createdAt.substring(0, 10));

                    // ?????? ??????
                    textPrice.setText("???" + courseDetail.get("PRICE").toString());

                    // ????????? ??????
                    int courseLength = Integer.parseInt(courseDetail.get("COURSE_LENGTH").toString());
                    int hours = courseLength / 3600;
                    int mins = courseLength / 60;
                    int secs = courseLength % 60;
                    textVideoLength.setText(String.format("%s?????? %s??? %s???", hours, mins, secs));

                    // ????????? ??????
                    textVideoCount.setText(courseDetail.get("VIDEO_COUNT").toString() + "?????? ?????????");

                    // ?????? ?????? ??????
                    String description = courseDetail.get("DESCRIPTION").toString();
                    glideImageGetter = new GlideImageGetter(getApplicationContext(), textDescription);
                    Spanned htmlSpan = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, glideImageGetter, null);
                    textDescription.setText(htmlSpan);
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.i(TAG, "??????:" + t.getMessage());
            }
        });

        buttonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPurchased == 0) {

                } else if (isPurchased == 1) {
                    Intent intent = new Intent(v.getContext(), CourseVideoActivity.class);
                    intent.putExtra("ADMIN_NAME", name);
                    intent.putExtra("TITLE", title);
                    intent.putExtra("COURSE_ID", courseId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void doInitialize() {
        // ????????? ??????
        backBtnImage = findViewById(R.id.imageBack);
        thumbnailImage = findViewById(R.id.imageThumnail);
        textTitle = findViewById(R.id.textTitle);
        textShortDescription = findViewById(R.id.textShortDescription);
        textReview = findViewById(R.id.textReview);
        ratingBar = findViewById(R.id.ratingBar);
        textReviewCount = findViewById(R.id.textReviewCount);
        textTeacherName = findViewById(R.id.textTeacherName);
        textCreatedAt = findViewById(R.id.textCreatedAt);
        textPrice = findViewById(R.id.textPrice);
        textDescription = findViewById(R.id.textDescription);
        textVideoLength = findViewById(R.id.textVideoLength);
        textVideoCount = findViewById(R.id.textVideoCount);
        buttonPurchase = findViewById(R.id.buttonPurchase);
    }
}