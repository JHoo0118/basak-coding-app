package com.kosmo.basakcoding.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    public SignInActivity() {
        authService = ApiClient.getRetrofit().create(AuthService.class);
    }

    private EditText inputEmail, inputPassword;
    private MaterialButton buttonSignIn, buttonGoogleSignIn;
    private ProgressBar signInProgressBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonGoogleSignIn = findViewById(R.id.buttonGoogleSignIn);
        signInProgressBar = findViewById(R.id.signInProgressBar);

        // 구글 계정으로 로그인 시작
        mAuth = FirebaseAuth.getInstance();
        requestGoogleSignIn();

        buttonGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        // 구글 계정으로 로그인 끝

        // 회원가입 시 이메일 받기 시작
        Intent signUpintent = getIntent();
        if(!TextUtils.isEmpty(signUpintent.getStringExtra("newEmail"))){
            String email = signUpintent.getStringExtra("newEmail");
            inputEmail = findViewById(R.id.inputEmail);
            inputEmail.setText(String.valueOf(email));
        }

        // 회원가입 시 이메일 받기 끝

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
        call.enqueue(new Callback<MemberDTO>() {
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful()) {

                    MemberDTO member = response.body();
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_MEMBER_ID, Integer.toString(member.getMemberId()));
                    preferenceManager.putString(Constants.KEY_USERNAME, member.getUsername());
                    preferenceManager.putString(Constants.KEY_EMAIL, member.getEmail());
                    preferenceManager.putString(Constants.KEY_GOOGLE_LOGIN, "N");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, "이메일 또는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                Log.i(TAG, "에러:" + t.getMessage());
                signInProgressBar.setVisibility(View.INVISIBLE);
                buttonSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(SignInActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // 구글 로그인 런쳐
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            
                            // retrofit2
                            HashMap params = new HashMap();
                            params.put("email", account.getEmail());
                            params.put("username", account.getDisplayName());
                            Call<Integer> check = authService.googleSignUp(params);
                            check.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.isSuccessful()) {
                                        Log.i(TAG, response.body().toString());
                                        firebaseAuthWithGoogle(account.getIdToken());
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_MEMBER_ID, response.body().toString());
                                        preferenceManager.putString(Constants.KEY_USERNAME, account.getDisplayName());
                                        preferenceManager.putString(Constants.KEY_EMAIL, account.getEmail());
                                        preferenceManager.putString(Constants.KEY_GOOGLE_LOGIN, "Y");
                                    }
                                }
                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.i(TAG, "에러:" + t.getMessage());
                                }
                            });
                            

                        } catch (ApiException e) {
                            Log.i(TAG, "구글 로그인 실패:" + e);
                        }
                    }
                }
            });

    private void googleSignIn() {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        launcher.launch(googleSignInIntent);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignInActivity.this, "구글 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
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