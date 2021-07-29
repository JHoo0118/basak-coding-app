package com.kosmo.basakcoding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.models.LoginDTO;
import com.kosmo.basakcoding.models.MemberDTO;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.AuthService;
import com.kosmo.basakcoding.utilities.Constants;
import com.kosmo.basakcoding.utilities.PreferenceManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    public static final String TAG = "basakcoding";
    private AuthService authService;

    public SignInActivity() {
        authService = ApiClient.getRetrofit().create(AuthService.class);
    }

    private EditText inputEmail, inputPassword;
    private MaterialButton buttonSignIn;
    private ProgressBar signInProgressBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Intent signUpintent = getIntent();
        if(!TextUtils.isEmpty(signUpintent.getStringExtra("newEmail"))){
            String email = signUpintent.getStringExtra("newEmail");
            inputEmail = findViewById(R.id.inputEmail);
            inputEmail.setText(String.valueOf(email));
        }

        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.textSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        signInProgressBar = findViewById(R.id.signInProgressBar);

        Intent i = getIntent();
        String email = i.getStringExtra("email");
        String password = i.getStringExtra("password");

        if (email != null && password != null) {
            inputEmail.setText(email);
            inputPassword.setText(password);
        }

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                    Toast.makeText(SignInActivity.this, "이메일 형식이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (inputPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    signInWithSpring();
                    // signInWithFirebase();
                }

            }
        });
    }

    private void signInWithSpring() {

        buttonSignIn.setVisibility(View.INVISIBLE);
        signInProgressBar.setVisibility(View.VISIBLE);

        LoginDTO body = new LoginDTO();
        body.setEmail(inputEmail.getText().toString());
        body.setPassword(inputPassword.getText().toString());
        Call<MemberDTO> call = authService.login(body);
        call.enqueue(new Callback<MemberDTO>(){
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful()) {

                    MemberDTO member = response.body();
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_MEMBER_ID, Integer.toString(member.getMemberId()));
                    preferenceManager.putString(Constants.KEY_USERNAME, member.getUsername());
                    preferenceManager.putString(Constants.KEY_EMAIL, member.getEmail());
                    preferenceManager.putString(Constants.KEY_USERNAME, member.getUsername());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, "이메일 또는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                Log.i(TAG, t.getMessage());
                signInProgressBar.setVisibility(View.INVISIBLE);
                buttonSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(SignInActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithFirebase() {
        buttonSignIn.setVisibility(View.INVISIBLE);
        signInProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_MEMBER)
                .whereEqualTo(Constants.KEY_EMAIL, inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                            preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            signInProgressBar.setVisibility(View.INVISIBLE);
                            buttonSignIn.setVisibility(View.VISIBLE);
                            Toast.makeText(SignInActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}