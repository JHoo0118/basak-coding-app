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
import com.kosmo.basakcoding.adapters.CoursesAdapter;
import com.kosmo.basakcoding.adapters.MyCourseAdapter;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.CoursesService;
import com.kosmo.basakcoding.service.MyCourseService;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kosmo.basakcoding.utilities.Constants.KEY_MEMBER_ID;

public class CatalogFragment extends Fragment {

    public static final String TAG = "basakcoding";

    private List<HashMap> CoursesList = new ArrayList();
    private CoursesService CoursesService;
    private PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private CoursesAdapter CoursesAdapter;

    public CatalogFragment(PreferenceManager preferenceManager) {
        CoursesService = ApiClient.getRetrofit().create(CoursesService.class);
        this.preferenceManager = preferenceManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.Course);


        Call<List<HashMap>> call = CoursesService.getCoursesList();
        call.enqueue(new Callback<List<HashMap>>() {
            @Override
            public void onResponse(Call<List<HashMap>> call, Response<List<HashMap>> response) {
                if (response.isSuccessful()) {
                    CoursesList = response.body();
                    CoursesAdapter = new CoursesAdapter(getContext(), CoursesList);

                    recyclerView.setAdapter(CoursesAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<HashMap>> call, Throwable t) {
                Log.i(TAG, "에러:" + t.getMessage());
            }
        });


//        ProgressBar courseProgress = (ProgressBar) getView().findViewById(R.id.courseProgress);
//        courseProgress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, getActivity().getTheme())));

    }
}