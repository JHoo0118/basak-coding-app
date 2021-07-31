package com.kosmo.basakcoding.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.activities.SignInActivity;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.MyPageService;
import com.kosmo.basakcoding.utilities.Constants;
import com.kosmo.basakcoding.utilities.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kosmo.basakcoding.utilities.Constants.KEY_EMAIL;
import static com.kosmo.basakcoding.utilities.Constants.KEY_GOOGLE_LOGIN;
import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;
import static com.kosmo.basakcoding.utilities.Constants.KEY_MEMBER_ID;
import static com.kosmo.basakcoding.utilities.Constants.KEY_USERNAME;

public class MyPageFragment extends Fragment {

    public static final String TAG = "basakcoding";

    private HashMap MyPage = new HashMap();
    private HashMap UpDate = new HashMap();

    private MyPageService myPageService;
    private PreferenceManager preferenceManager;
    RoundedImageView profile;
    TextView textUserName;
    TextView textEmail;
    View viewBackground;


    public MyPageFragment(PreferenceManager preferenceManager) {
        myPageService = ApiClient.getRetrofit().create(MyPageService.class);
        this.preferenceManager = preferenceManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewBackground = view.findViewById(R.id.viewBackground);
        profile = view.findViewById(R.id.profile);
        textUserName = view.findViewById(R.id.textUserName);
        textEmail = view.findViewById(R.id.textEmail);

        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        Button logoutBtn = (Button) getView().findViewById(R.id.logoutBtn);
        Button updateBtn = (Button) getView().findViewById(R.id.updateBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("로그아웃")
                        .setMessage("정말 로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferenceManager.clearPreferences();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), SignInActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("아니요", null)
                        .show();
            }
        });

        Call<HashMap> call = myPageService.getMyPageList(preferenceManager.getString(KEY_MEMBER_ID));
        call.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if (response.isSuccessful()) {
                    MyPage = response.body();
                    Log.i(TAG, MyPage.toString());

                    textUserName.setText(MyPage.get("USERNAME").toString());
                    textEmail.setText(MyPage.get("EMAIL").toString());

                    Picasso.get().load(KEY_BASE_URL + "/upload/member/" +
                            MyPage.get("MEMBER_ID").toString() + "/" +
                            MyPage.get("AVATAR").toString())
                            .placeholder(R.drawable.image4)
                            .into(profile);
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.i(TAG, "에러:" + t.getMessage());
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("회원정보 수정")
                        .setMessage("수정 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                TextView username = view.findViewById(R.id.textUserName);
                                TextView password = view.findViewById(R.id.password);
                                TextView pwck = view.findViewById(R.id.pwck);
                                Log.i(TAG, "username, password, pwck" + password.getText().toString().trim().equals(pwck.getText()));

                                if (password.getText().toString().trim().equals(pwck.getText().toString().trim())) {
                                    Map map = new HashMap();
                                    map.put("memberId", preferenceManager.getString(KEY_MEMBER_ID));
                                    map.put("username", username.getText().toString());
                                    map.put("password", password.getText().toString());

                                    Call<HashMap> update = myPageService.updateMember(map);
                                    update.enqueue(new Callback<HashMap>() {
                                        @Override
                                        public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                                            if (response.isSuccessful()) {
                                                UpDate = response.body();
                                                Log.i(TAG, UpDate + "");

                                                textUserName.setText(UpDate.get("username").toString());
                                                textEmail.setText(MyPage.get("EMAIL").toString());

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<HashMap> call, Throwable t) {
                                            Log.i(TAG, "에러:" + t.getMessage());

                                        }
                                    });
                                } else {
                                    Log.i(TAG, "회원정보수정실패");
                                }
                            }
                        })
                        .setNegativeButton("아니요", null)
                        .show();
            }
        });
//        ProgressBar courseProgress = (ProgressBar) getView().findViewById(R.id.courseProgress);
//        courseProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, getActivity().getTheme())));

    }
}
