package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Adapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Java.RechargeAdepter;
import com.rathaur.earning.Modal.Recharge;
import com.rathaur.earning.databinding.ActivityRechageDataBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RechageData extends AppCompatActivity {

    ActivityRechageDataBinding binding;
    RechargeAdepter adepter;
    List<Recharge> rechargeList=new ArrayList<>();
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRechageDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");
        binding.rechargeRecycler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Recharge");
        reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rechargeList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Recharge recharge=dataSnapshot.getValue(Recharge.class);
                        rechargeList.add(recharge);
                    }
                    adepter=new RechargeAdepter(rechargeList,getApplicationContext());
                    binding.rechargeRecycler.setAdapter(adepter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}