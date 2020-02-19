package com.app.aedmapping;

import android.content.Intent;
import android.os.Bundle;

import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.User.CreateUserRequest;
import com.app.aedmapping.Retrofit.User.CreateUserResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    Fonts fonts;
    @BindView(R.id.buttonSignUp)
    Button submit;
    @BindView(R.id.headerText)
    TextView headerText;
    private APIManager apiManager;
    EditText name,email,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        configureComponents();
    }

    private void configureComponents() {
        name=findViewById(R.id.textNameSurname);
        email=findViewById(R.id.textEmail);
        password=findViewById(R.id.textPassword);
        fonts = new Fonts(this);
        email.setTypeface(fonts.getMRegular());
        password.setTypeface(fonts.getMRegular());
        name.setTypeface(fonts.getMRegular());
        headerText.setTypeface(fonts.getMMedium());
        submit.setTypeface(fonts.getMMedium());
    }


    @OnClick(R.id.buttonSignUp)
    void signup() {
        EditText[] texts = {name,email,password};
        boolean error=false;
        boolean nullable=false;
        for (EditText text:texts) {
            if(text.getText()==null) {
                text.setError("This field can not be blank");
                error=true;
                nullable=true;
            }
            else if (text.getText().toString().trim().equalsIgnoreCase("")) {
                text.setError("This field can not be blank");
                error=true;
            }
            else if(password.getText().toString().length()<8) {
                password.setError("Password must be at least 8 characters");
                error=true;
            }
        }

        if(!error) {
            String personName = name.getText().toString();
            String personEmail = email.getText().toString();
            String personPassword = password.getText().toString();
            CreateUserRequest request = new CreateUserRequest(personName,personEmail,personPassword);
            apiManager=new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
            apiManager.getServices().addUser(request).enqueue(new Callback<CreateUserResponse>() {
                @Override
                public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                    if(response.body()!=null) {
                        if(response.body().getMsg().equals("Saved")) {
                            Toast.makeText(SignupActivity.this, "Now you can log in", Toast.LENGTH_LONG).show();
                            createSignInActivityIntent();
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateUserResponse> call, Throwable t) {

                }
            });

            Toast.makeText(this, "Now you can log in", Toast.LENGTH_LONG).show();
        }

    }
    @OnClick(R.id.backPressButton)
    void backPressed () {
       createSignInActivityIntent();
    }
    @OnTextChanged({R.id.textNameSurname,R.id.textEmail,R.id.textPassword})
    void textChanged() {
        EditText[] texts = {name,email,password};
        for (EditText text:texts)
            text.setError(null);
    }
    private void createSignInActivityIntent(){
        Intent intent = new Intent(this,SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
