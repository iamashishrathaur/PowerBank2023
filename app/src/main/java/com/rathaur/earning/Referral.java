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
import com.rathaur.earning.Java.ReferAdepter;
import com.rathaur.earning.Modal.Users;
import com.rathaur.earning.databinding.ActivityReferralBinding;

import java.util.ArrayList;
import java.util.List;

public class Referral extends AppCompatActivity {

    ActivityReferralBinding binding;
    ReferAdepter adepter;
    List<Users> usersList=new ArrayList<>();
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReferralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        binding.referRecycler.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myId=snapshot.child("myReferralCode").getValue(String.class);
                    checkMyReferral(myId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void checkMyReferral(String myId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Users users=dataSnapshot.getValue(Users.class);
                        //             Users users=dataSnapshot.getValue(Users.class);
                        assert users != null;
                        if (myId.equals(users.getFriendReferralCode())){
                            usersList.add(users);
                        }
                    }
                    binding.totalReferral.setText("My Referral => "+usersList.size());
                    adepter=new ReferAdepter(usersList,getApplicationContext());
                    adepter.notifyDataSetChanged();
                    binding.referRecycler.setAdapter(adepter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}