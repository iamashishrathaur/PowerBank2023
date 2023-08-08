package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.databinding.ActivityPasswordBinding;

import java.util.HashMap;
import java.util.Objects;

public class Password extends AppCompatActivity {

    ActivityPasswordBinding binding;
    String userMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass=binding.userOldPassword.getText().toString();
                String newPass=binding.userNewPassword.getText().toString();
                String confirmPass=binding.userNewPasswordConfirm.getText().toString();
               if (!oldPass.trim().isEmpty() && !newPass.trim().isEmpty() && !confirmPass.trim().isEmpty() ){
                   if (newPass.equals(confirmPass)){
                       changePass(oldPass,newPass,confirmPass);
                   }
                   else {
                       Toast.makeText(Password.this, "new password not same", Toast.LENGTH_SHORT).show();
                   }
               }
            }
        });

    }

    private void changePass(String oldPass, String newPass, String confirmPass) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String ,Object> map=new HashMap<>();
        map.put("password",newPass);
        reference.child("7905321205").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String  oldPassword=snapshot.child("password").getValue(String.class);
                    if (oldPassword.equals(oldPass)){
                        reference.child("7905321205").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(Password.this, "Successfully update", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(Password.this, "wrong old password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}