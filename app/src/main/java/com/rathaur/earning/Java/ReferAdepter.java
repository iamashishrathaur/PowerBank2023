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
import com.rathaur.earning.Modal.Users;
import com.rathaur.earning.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReferAdepter extends RecyclerView.Adapter<ReferAdepter.ViewHolder> {

    List<Users> usersList=new ArrayList<>();
    Context context;
    int allRefer=0;
    String userMobileNumber;

    public ReferAdepter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReferAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferAdepter.ViewHolder holder, int position) {
       Users users=usersList.get(position);
       holder.number.setText(users.getMobile());
        SharedPreferences sharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

       if (!users.isReferAmountReceive()){
           holder.amount.setText("20 rs");
           holder.amount.setVisibility(View.VISIBLE);
           holder.amountSecond.setVisibility(View.GONE);
       }
       else {
           holder.amountSecond.setText("20 rs");
           holder.amountSecond.setVisibility(View.VISIBLE);
           holder.amount.setVisibility(View.GONE);
       }
       holder.amount.setOnClickListener(v -> {

           DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
           HashMap<String,Object> map=new HashMap<>();
           map.put("referAmountReceive",true);
           reference.child(users.getMobile()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("totalAmount");
                   databaseReference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           HashMap<String,Object> map=new HashMap<>();
                           if (snapshot.exists()){
                               String total=snapshot.child("refer").getValue(String.class);
                               if (total!=null){
                                   allRefer=Integer.parseInt(total)+20;
                                   map.put("refer",String.valueOf(allRefer));
                                   databaseReference.child(userMobileNumber).updateChildren(map);
                               }
                               else {
                                   map.put("refer","20");
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
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number,amount,amountSecond;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.referral_sample_number);
            amount=itemView.findViewById(R.id.referral_sample_amount);
            amountSecond=itemView.findViewById(R.id.referral_sample_amount_second);

        }
    }
}
