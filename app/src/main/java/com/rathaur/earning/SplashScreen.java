package com.rathaur.earning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");
            new Handler().postDelayed(() -> {
                if (!userMobileNumber.isEmpty()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), LoginAccount.class));
                    finish();
                }
            }, 1000);

    }
}