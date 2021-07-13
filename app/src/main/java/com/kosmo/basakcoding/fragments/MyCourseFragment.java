package com.kosmo.basakcoding.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.adapters.MyCourseAdapter;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.MyCourseService;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kosmo.basakcoding.utilities.Constants.KEY_MEMBER_ID;

public class MyCourseFragment extends Fragment {

    public static final String TAG = "basakcoding";

    private List<HashMap> myCourseList = new ArrayList();
    private MyCourseService myCourseService;
    private PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private MyCourseAdapter myCourseAdapter;
    private ProgressBar myCourseProgressBar;

    public MyCourseFragment(PreferenceManager preferenceManager) {
            myCourseService = ApiClient.getRetrofit().create(MyCourseService.class);
            this.preferenceManager = preferenceManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_course, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.myCourseRV);
        myCourseProgressBar = view.findViewById(R.id.myCoursePB);

        myCourseProgressBar.setVisibility(View.VISIBLE);


        Call<List<HashMap>> call = myCourseService.getMyCouresList(preferenceManager.getString(KEY_MEMBER_ID));
        call.enqueue(new Callback<List<HashMap>>() {
            @Override
            public void onResponse(Call<List<HashMap>> call, Response<List<HashMap>> response) {
                if (response.isSuccessful()) {
                    myCourseList = response.body();
                    Log.i(TAG, myCourseList+"");
                    myCourseAdapter = new MyCourseAdapter(getContext(), myCourseList);

                    recyclerView.setAdapter(myCourseAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<HashMap>> call, Throwable t) {
                Log.i(TAG, "에러:"+t.getMessage());
            }
        });

        myCourseProgressBar.setVisibility(View.INVISIBLE);

//        ProgressBar courseProgress = (ProgressBar) getView().findViewById(R.id.courseProgress);
//        courseProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, getActivity().getTheme())));

    }
}