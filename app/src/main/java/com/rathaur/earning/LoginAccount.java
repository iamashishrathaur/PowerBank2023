package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.databinding.ActivityLoginAccountBinding;

import java.util.Objects;

public class LoginAccount extends AppCompatActivity {

    ActivityLoginAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.createAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateAccount.class)));
        binding.loginButton.setOnClickListener(v -> {

            String mobile=binding.loginMobile.getText().toString();
            String pass=binding.loginPassword.getText().toString();
            if (!mobile.trim().isEmpty() && mobile.trim().length()==10){
                if (!pass.trim().isEmpty()){
                    login(mobile,pass,v);
                    binding.loginProgress.setProgress(100);
                    binding.loginProgress.setVisibility(View.VISIBLE);
                    binding.loginButton.setText(" ");
                }
                else {
                    Snackbar.make(v,"Enter Password",Snackbar.LENGTH_SHORT).show();
                }
            }
            else {
                Snackbar.make(v,"Enter Valid Mobile Number",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String mobile, String password,View v) {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String  pass=snapshot.child("password").getValue(String.class);
                    String  mob=snapshot.child("mobile").getValue(String.class);
                    if (pass != null && pass.equals(password)){
                        binding.loginProgress.setProgress(0);
                        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("mobile",mob);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else {
                        binding.loginProgress.setProgress(0);
                        binding.loginProgress.setVisibility(View.GONE);
                        binding.loginButton.setText("Login");
                        Snackbar.make(v,"Password is wrong",Snackbar.LENGTH_SHORT).show();
                    }

                }
                else {
                    binding.loginProgress.setProgress(0);
                    binding.loginProgress.setVisibility(View.GONE);
                    binding.loginButton.setText("Login");
                    Snackbar.make(v,"User not exist",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.loginButton.setText("Login");
                binding.loginProgress.setProgress(0);
                binding.loginProgress.setVisibility(View.GONE);
            }
        });
    }
}