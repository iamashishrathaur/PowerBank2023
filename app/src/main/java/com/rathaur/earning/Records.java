package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Java.WithdrawAdepter;
import com.rathaur.earning.Modal.Withdraw;
import com.rathaur.earning.databinding.ActivityRecordsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Records extends AppCompatActivity {

    ActivityRecordsBinding binding;
    List<Withdraw> withdraws=new ArrayList<>();
    WithdrawAdepter adepter;
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");
        binding.withdrawRecycler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Withdraw");
        reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    withdraws.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Withdraw withdraw=dataSnapshot.getValue(Withdraw.class);
                        withdraws.add(withdraw);
                    }
                    adepter=new WithdrawAdepter(withdraws,getApplicationContext());
                    adepter.notifyDataSetChanged();
                    binding.withdrawRecycler.setAdapter(adepter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}