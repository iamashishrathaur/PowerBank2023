package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Modal.Users;
import com.rathaur.earning.databinding.ActivityCreateAccountBinding;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class CreateAccount extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.createAccountProgress.setProgress(0);

       binding.createAccountButton.setOnClickListener(v-> {
          String name=binding.userName.getText().toString();
          String email=binding.userEmail.getText().toString();
          String number=binding.userMobile.getText().toString();
          String password=binding.userPassword.getText().toString();
          String confirmPass=binding.userConfirmPassword.getText().toString();
          String withdrawPass=binding.userWithdrawPassword.getText().toString();
          String friendReferral=binding.userReferalCode.getText().toString();
          if (!name.trim().isEmpty() && !email.trim().isEmpty()
                  && !number.trim().isEmpty() && !password.trim().isEmpty()
                  && !confirmPass.trim().isEmpty() && !withdrawPass.trim().isEmpty() && !friendReferral.trim().isEmpty()){
              if (number.length()==10){
                  if (password.length()==8){
                      if (password.equals(confirmPass)){
                          if (withdrawPass.length()==6){
                              binding.createAccountProgress.setProgress(100);
                              binding.createAccountButton.setText("");
                              binding.createAccountProgress.setVisibility(View.VISIBLE);
                              DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                              reference.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      if (!snapshot.exists()){
                                          createAccount(name,email,number,password,confirmPass,withdrawPass,friendReferral);
                                      }
                                      else {
                                          binding.createAccountProgress.setProgress(0);
                                          binding.createAccountButton.setText("Sign Up");
                                          binding.createAccountProgress.setVisibility(View.GONE);
                                          Snackbar.make(v,"user already exist ",Snackbar.LENGTH_SHORT).show();
                                      }
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError error) {
                                      binding.createAccountProgress.setProgress(0);
                                      binding.createAccountButton.setText("Sign Up");
                                      binding.createAccountProgress.setVisibility(View.GONE);
                                  }
                              });
                          }
                          else {
                              Snackbar.make(v,"Withdraw password week ",Snackbar.LENGTH_SHORT).show();
                          }
                      }
                      else {
                          Snackbar.make(v,"password not same",Snackbar.LENGTH_SHORT).show();
                      }
                  }
                  else {
                      Snackbar.make(v,"please enter strong password",Snackbar.LENGTH_SHORT).show();
                  }

              }
              else {
                  Snackbar.make(v,"please enter correct mobile number ",Snackbar.LENGTH_SHORT).show();
              }


          }
          else {
              Snackbar.make(v,"Fill Information ",Snackbar.LENGTH_SHORT).show();
          }
       });
    }

    private void createAccount(String name, String email, String number, String password, String confirmPass, String withdrawPass, String friendReferral) {
        binding.createAccountProgress.setProgress(100);
        binding.createAccountButton.setText("");
        binding.createAccountProgress.setVisibility(View.VISIBLE);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("myReferralCode").equalTo(friendReferral).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.createAccountButton.setText("Sign Up");
                    binding.createAccountProgress.setProgress(0);
                    binding.createAccountProgress.setVisibility(View.GONE);
                    Users users=new Users(name,email,number,password,withdrawPass,friendReferral.trim(),getReferralCode(),false);

                    reference.child(number).setValue(users).addOnSuccessListener(unused -> {
                        binding.createAccountProgress.setProgress(0);
                        binding.createAccountProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(),LoginAccount.class));
                    }).addOnFailureListener(e ->{
                        binding.createAccountProgress.setProgress(0);
                        binding.createAccountProgress.setVisibility(View.GONE);
                        Toast.makeText(CreateAccount.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    });
                }
                else {
                    binding.createAccountButton.setText("Sign Up");
                    Toast.makeText(CreateAccount.this, "Please enter correct referral code", Toast.LENGTH_SHORT).show();
                    binding.createAccountProgress.setProgress(0);
                    binding.createAccountProgress.setVisibility(View.GONE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.createAccountButton.setText("Sign Up");
                binding.createAccountProgress.setProgress(0);
                binding.createAccountProgress.setVisibility(View.GONE);
            }
        });
    }

    public String getReferralCode(){
        char[] chars="ABCDEFGHIJKLMNOPQRSTWXYZ0123456789".toCharArray();
        Random rnd=new Random();
        StringBuilder sb=new StringBuilder((100000+rnd.nextInt(900000)));
        for(int i=0; i<6; i++){
            sb.append(chars[rnd.nextInt(chars.length)]);
        }
        return sb.toString();
    }
}