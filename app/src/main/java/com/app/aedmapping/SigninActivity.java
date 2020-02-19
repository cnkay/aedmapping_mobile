package com.app.aedmapping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.User.CreateLoginRequest;
import com.app.aedmapping.Retrofit.User.CreateLoginResponse;
import com.app.aedmapping.Retrofit.User.CreateUserRequest;
import com.app.aedmapping.Retrofit.User.CreateUserResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    GoogleSignInOptions gso;
    GoogleSignInClient client;

    @BindView(R.id.googleSignIn)
    SignInButton googleSignIn;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.buttonRedirectSignUp)
    Button buttonSignUp;
    private APIManager apiManager;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Fonts fonts;

    @OnClick(R.id.googleSignIn)
    void setGoogleSignIn() {
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.buttonRedirectSignUp)
    void createSignUpIntent() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.buttonLogin)
    void login() {
        EditText[] editTexts = {editEmail, editPassword};
        boolean error = false;
        boolean nullable = false;
        for (EditText text : editTexts) {
            if (text.getText() == null) {
                text.setError("This field can not be blank");
                error = true;
                nullable = true;
            } else if (text.getText().toString().trim().equalsIgnoreCase("")) {
                text.setError("This field can not be blank");
                error = true;
            } else if (editPassword.getText().toString().length() < 8) {
                editPassword.setError("Password must be at least 8 characters");
                error = true;
            }
        }
        if (!error) {
            String mail = editEmail.getText().toString();
            String password = editPassword.getText().toString();

            CreateLoginRequest request = new CreateLoginRequest(mail, password);
            apiManager = new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
            apiManager.getServices().loginUser(request).enqueue(new Callback<CreateLoginResponse>() {
                @Override
                public void onResponse(Call<CreateLoginResponse> call, Response<CreateLoginResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getMsg().equals("Wrong password!") || response.body().getMsg().equals("Email address not found!") || response.body().getMsg().equals("Missing credentials!")) {
                            Toast.makeText(SigninActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else if (response.body().getCode() == 200) {
                            Toast.makeText(SigninActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            editor = pref.edit();
                            editor.putString("normalLogin", "1");
                            editor.commit();
                            createMainActivityIntent();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateLoginResponse> call, Throwable t) {

                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        configureGoogleSignIn();
        configureComponents();

    }

    private void configureComponents() {
        fonts = new Fonts(this);
        editEmail.setTypeface(fonts.getMRegular());
        editPassword.setTypeface(fonts.getMRegular());
        buttonLogin.setTypeface(fonts.getMMedium());
        buttonSignUp.setTypeface(fonts.getMMedium());
    }

    private void createMainActivityIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void configureGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            createMainActivityIntent();
        }
    }

    private void updateUI(GoogleSignInAccount account) {

        SharedPreferences.Editor editor = pref.edit();
        if (account != null) {
            editor.putString("personName", account.getDisplayName());
            editor.putString("personEmail", account.getEmail());
            editor.putString("personIdToken", account.getIdToken());
            editor.commit();
            //Save User
            CreateUserRequest request = new CreateUserRequest(account.getDisplayName(), account.getEmail(), account.getIdToken());
            apiManager = new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
            apiManager.getServices().addUser(request).enqueue(new Callback<CreateUserResponse>() {
                @Override
                public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {

                }

                @Override
                public void onFailure(Call<CreateUserResponse> call, Throwable t) {

                }
            });

            createMainActivityIntent();
        } else {
            Log.d("ACCOUNT", "NULL");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInError", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

}
