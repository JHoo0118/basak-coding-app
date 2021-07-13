package com.kosmo.basakcoding.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.activities.SignInActivity;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import org.jetbrains.annotations.NotNull;

public class MyAccountFragment extends Fragment {

    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        Button logoutBtn = (Button) getView().findViewById(R.id.logoutBtn);
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
                                startActivity(new Intent(getContext(), SignInActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("아니요", null)
                        .show();
            }
        });
    }
}