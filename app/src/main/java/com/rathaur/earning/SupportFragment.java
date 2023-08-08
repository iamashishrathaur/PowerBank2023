package com.rathaur.earning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.databinding.FragmentProductBinding;
import com.rathaur.earning.databinding.FragmentSupportBinding;

public class SupportFragment extends Fragment {

    FragmentSupportBinding binding;
    String userMobileNumber;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");
        binding=FragmentSupportBinding.inflate(inflater, container, false);
        binding.chatServiceRl.setOnClickListener(v -> startActivity(new Intent(getContext(),ChatBot.class)));
        binding.shareReferCodeButton.setOnClickListener(v -> shareMsg());
        binding.serviceRl.setOnClickListener(v -> msgTelegram("https://telegram.me/iamashishrathaur/"));
        binding.groupServiceRl.setOnClickListener(v -> msgTelegram("https://telegram.me/iamarahul/"));

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String refer=snapshot.child("myReferralCode").getValue(String.class);
                    binding.myReferCode.setText(refer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();

    }

    private void msgTelegram(String uid) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uid));
        startActivity(myIntent);
    }

    private void shareMsg(){
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "hjkevkwck/vjrevwvwe v.erkbrkbker mf3.vrbkr");
        intent.setType("text/plain");
        startActivity(intent);
    }
}