package com.example.movieapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.api.ApiService;
import com.example.movieapp.api.UserApi;
import com.example.movieapp.databinding.ActivityLoginBinding;
import com.example.movieapp.model.LoginResponse;
import com.example.movieapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        api = UserApi.getClient().create(ApiService.class);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        handleLogin();
        registerActivity();

    }

    private void handleLogin() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String email = binding.editTextEmail.getText().toString();
//                String password = binding.editTextPassword.getText().toString();
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    login(email, password);
//                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });
    }

    private void registerActivity() {
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void login(String email, String password) {
        User user = new User(email, password);
        Call<LoginResponse> call = api.getLogin(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                     // Check if login was successful
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Optional: Close the login activity

                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BAOLE_DEBUG", t.getMessage(), t);
            }
        });
    }

}