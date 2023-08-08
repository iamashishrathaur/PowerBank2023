package com.rathaur.earning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rathaur.earning.Java.Adepter;
import com.rathaur.earning.Modal.Product;
import com.rathaur.earning.databinding.FragmentProductBinding;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    FragmentProductBinding binding;
    Adepter adepter;
    ArrayList<Product> arrayList=new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= FragmentProductBinding.inflate(inflater, container, false);

        //adepter=new Adepter()
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList.clear();
        arrayList.add(new Product("PI100","3","20","220",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI200","20","40","420",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI500","40","100","880",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI900","45","100","1500",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI1000","50","150","2000",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI1500","10","300","5000",R.drawable.ic_launcher_background));
        arrayList.add(new Product("PI100","2","1000","Pre Sale",R.drawable.ic_launcher_background));

        adepter=new Adepter(getContext(),arrayList);

        binding.recyclerView.setAdapter(adepter);


        return binding.getRoot();
    }
}