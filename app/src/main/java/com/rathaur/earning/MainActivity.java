package com.rathaur.earning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rathaur.earning.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity { 
    HomeFragment homeFragment=new HomeFragment();
    AccountFragment accountFragment=new AccountFragment();
    SupportFragment supportFragment=new SupportFragment();
    ProductFragment productFragment=new ProductFragment();
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,homeFragment);
        fragmentTransaction.commitNow();
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

           @SuppressLint("NonConstantResourceId")
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if (item.getItemId() == R.id.home) {
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.frameLayout, homeFragment);
                   fragmentTransaction.commit();
                   return true;
               }
               if (item.getItemId()==R.id.product){
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.frameLayout, productFragment);
                   fragmentTransaction.commit();
                   return true;
               }
               if (item.getItemId()==R.id.support){
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.frameLayout, supportFragment);
                   fragmentTransaction.commit();
                   return true;
               }
               if (item.getItemId()==R.id.account){
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.frameLayout, accountFragment);
                   fragmentTransaction.commit();
                   return true;
               }
               return false;
           }
       });
    }
}