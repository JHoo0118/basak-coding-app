package com.kosmo.basakcoding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.service.ApiClient;
import com.kosmo.basakcoding.service.AuthService;
import com.kosmo.basakcoding.utilities.Constants;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
    private MaterialButton buttonSignUp;
    private ProgressBar signUpProgressBar;
    private AuthService authService;


    public SignUpActivity() {
        authService = ApiClient.getRetrofit().create(AuthService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.textSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputEmail = findViewById(R.id.inputEmail);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (inputUsername.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "사용자명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (inputPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                    Toast.makeText(SignUpActivity.this, "이메일 형식이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호와 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
            }
        });
    }

    private void signUp(){
        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);
        HashMap map = new HashMap();
        String email= inputEmail.getText().toString().trim();
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        map.put("email",email);
        map.put("username",username);
        map.put("password",password);
        Call<Integer> call = authService.signUp(map);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if(response.body()==-1){

                        signUpProgressBar.setVisibility(View.GONE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Log.i("com.kosmo.basakcoding.activities","이메일중복");
                        Toast.makeText(SignUpActivity.this,"이미 사용 중인 이메일 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else if(response.body()==1){
                        Log.i("com.kosmo.basakcoding.activities","성공");
                        signUpProgressBar.setVisibility(View.GONE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("newEmail",email);
                        startActivity(intent);
                    }
                    else {
                        Log.i("com.kosmo.basakcoding.activities","실패");
                        signUpProgressBar.setVisibility(View.GONE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다. 다시시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                signUpProgressBar.setVisibility(View.GONE);
                buttonSignUp.setVisibility(View.VISIBLE);
                Toast.makeText(SignUpActivity.this,"관리자에게 문의해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    private void signUp() {
        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_USERNAME, inputUsername.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());

        database.collection(Constants.KEY_COLLECTION_MEMBER)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.putExtra("email", inputEmail.getText().toString());
                        intent.putExtra("password", inputPassword.getText().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
     */
}