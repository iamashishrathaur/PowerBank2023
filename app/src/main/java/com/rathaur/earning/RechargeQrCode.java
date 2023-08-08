package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Modal.Recharge;
import com.rathaur.earning.databinding.ActivityRechargeQrCodeBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RechargeQrCode extends AppCompatActivity {
 ActivityRechargeQrCodeBinding binding;
 String userMobileNumber;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRechargeQrCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");
        Intent intent=getIntent();
        String orderId=intent.getStringExtra("orderId");
        String amount=intent.getStringExtra("amount");
        assert orderId != null;
        binding.qrOrderId.setText(orderId);
        binding.qrAmount.setText("₹ "+amount);

        binding.qrSubmitButton.setOnClickListener(v -> {
            String utr=binding.qrUtrId.getText().toString();
            if (!utr.isEmpty()){
               DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Recharge");
               HashMap<String ,Object> map=new HashMap<>();
               map.put("utr",utr);
               map.put("status","00");
               reference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           reference.child(userMobileNumber).child(orderId).updateChildren(map).addOnSuccessListener(unused -> onBackPressed());
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });

    }


}