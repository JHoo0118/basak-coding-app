package com.kosmo.basakcoding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.fragments.CatalogFragment;
import com.kosmo.basakcoding.fragments.MyAccountFragment;
import com.kosmo.basakcoding.fragments.MyCourseFragment;
import com.kosmo.basakcoding.fragments.MyPageFragment;
import com.kosmo.basakcoding.utilities.Constants;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "basakcoding";

    private PreferenceManager preferenceManager;

    private ChipNavigationBar chipNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());
        chipNavigationBar = findViewById(R.id.chipNavigationBar);

        chipNavigationBar.setItemSelected(R.id.catalog, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CatalogFragment(preferenceManager)).commit();

        bottomMenu();

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<InstanceIdResult> task) {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    sendFCMTokenToDatabase(task.getResult().getToken());
//                }
//            }
//        });
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Fragment fragment = null;
                switch (index) {
                    case R.id.catalog:
                        fragment = new CatalogFragment(preferenceManager);
                        break;

                    case R.id.myCourse:
                        fragment = new MyCourseFragment(preferenceManager);
                        break;

                    case R.id.myAccount:
                        fragment = new MyPageFragment(preferenceManager);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });
    }

//    private void sendFCMTokenToDatabase(String token) {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =
//                database.collection(Constants.KEY_COLLECTION_MEMBER).document(
//                        preferenceManager.getString(Constants.KEY_MEMBER_ID)
//                );
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(MainActivity.this, "토큰 업데이트 성공", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(MainActivity.this, "토큰을 보낼 수 없습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}