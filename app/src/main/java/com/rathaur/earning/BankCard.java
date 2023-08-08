package com.rathaur.earning;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rathaur.earning.Modal.Bank;
import com.rathaur.earning.databinding.ActivityBankCardBinding;

import java.util.Objects;

public class BankCard extends AppCompatActivity {

    ActivityBankCardBinding binding;
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBankCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
         SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
         userMobileNumber=sharedPreferences.getString("mobile","");
         binding.addBankCard.setOnClickListener(v -> {
           String bankName =binding.bankName.getText().toString();
           String accountNumber=binding.accountNumber.getText().toString();
           String name=binding.bankName.getText().toString();
           String ifsc=binding.ifscCode.getText().toString();

           if (bankName.trim().isEmpty()&& accountNumber.trim().isEmpty()&& name.trim().isEmpty()&& ifsc.trim().isEmpty()){
               Snackbar.make(v,"Fill Full Information",Snackbar.LENGTH_SHORT).show();
           }
           else {
               addCard(bankName,accountNumber,name,ifsc);
           }
        });


    }

    private void addCard(String bankName, String accountNumber, String name, String ifsc) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Bank");
        Bank bank=new Bank(name,bankName,accountNumber,ifsc);
        reference.child(userMobileNumber).setValue(bank).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onBackPressed();
            }
        });
    }
}