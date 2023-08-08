package com.rathaur.earning.Java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Modal.Product;
import com.rathaur.earning.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DialyAdepter extends RecyclerView.Adapter<DialyAdepter.ViewHolder> {
    List<Product> productList=new ArrayList<>();
    Context context;
    int total=0;
    String userMobileNumber;

    public DialyAdepter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public DialyAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dialy_check_in_sample,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DialyAdepter.ViewHolder holder, int position) {
      Product product=productList.get(position);
      String uid=product.getProductId();
      holder.about.setText(product.getName());
      holder.checkIn.setText(product.getDailyProfit());
      holder.id.setText("("+product.getProductId()+")");
        SharedPreferences sharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        holder.checkIn.setOnClickListener(v -> {

            Calendar c = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String reg_date = df.format(c.getTime());
            HashMap<String,Object> map=new HashMap<>();
            map.put("isCheck",reg_date);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Product");
            reference.child(userMobileNumber).child(uid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("totalAmount");
                    databaseReference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String oldDaily=snapshot.child("daily").getValue(String.class);
                                HashMap<String,Object> map=new HashMap<>();
                                if (oldDaily != null) {
                                    total=Integer.parseInt(oldDaily)+Integer.parseInt(product.getDailyProfit());
                                    map.put("daily",String.valueOf(total));
                                    databaseReference.child(userMobileNumber).updateChildren(map);
                                }
                                else {
                                    map.put("daily",product.getDailyProfit());
                                    databaseReference.child(userMobileNumber).updateChildren(map);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView checkIn,about,id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkIn=itemView.findViewById(R.id.check_in_click);
            about=itemView.findViewById(R.id.check_in_about);
            id=itemView.findViewById(R.id.check_in_id);
        }
    }
}
