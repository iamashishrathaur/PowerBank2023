package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Java.PurchasedAdepter;
import com.rathaur.earning.Modal.Product;
import com.rathaur.earning.R;
import com.rathaur.earning.databinding.ActivityPurchassedBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Purchased extends AppCompatActivity {

    ActivityPurchassedBinding binding;
    PurchasedAdepter adepter;
    List<Product> productList=new ArrayList<>();
    String userMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPurchassedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        binding.purchasedRecyclers.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Product");
        reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Product product=dataSnapshot.getValue(Product.class);
                        productList.add(product);
                    }
                    adepter=new PurchasedAdepter(productList,getApplicationContext());
                    adepter.notifyDataSetChanged();
                    binding.purchasedRecyclers.setAdapter(adepter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}