package com.rathaur.earning.Java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Modal.Product;
import com.rathaur.earning.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Adepter extends RecyclerView.Adapter<Adepter.ViewHolder> {
     List<Product> products;
     Context context;
     private int availableRecharge=0;
     String userMobileNumber;

    public Adepter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }


    @NonNull
    @Override
    public Adepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adepter.ViewHolder holder, int position) {
       Product product=products.get(position);
       holder.name.setText(product.getName());
       holder.time.setText(product.getDays());
       holder.daily.setText(product.getDailyProfit());
       holder.amount.setText(product.getValue());
       holder.button.setText(product.getValue());

        SharedPreferences sharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.bottom_drawer, null);

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(mBottomSheetDialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

       holder.button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mBottomSheetDialog.show();
               AppCompatButton buy=mBottomSheetDialog.findViewById(R.id.button_buy);
               AppCompatButton close=mBottomSheetDialog.findViewById(R.id.button_close);
               TextView amount=mBottomSheetDialog.findViewById(R.id.dialog_total_amount);
               TextView daily=mBottomSheetDialog.findViewById(R.id.dialog_daily_amount);
               TextView topAmount=mBottomSheetDialog.findViewById(R.id.dialog_top_total);
               TextView name=mBottomSheetDialog.findViewById(R.id.dialog_product_name);
               assert amount != null;
               amount.setText(product.getValue());
               assert daily != null;
               daily.setText(product.getDailyProfit());
               assert topAmount != null;
               topAmount.setText(product.getValue());
               assert name != null;
               name.setText(product.getName());
               Objects.requireNonNull(close).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mBottomSheetDialog.cancel();
                   }
               });

               // get recharge amount
               DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("totalAmount");
               databaseReference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
                   @SuppressLint("SetTextI18n")
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                          String totalRecharge= snapshot.child("recharge").getValue(String.class);
                           String totalProduct=snapshot.child("productBuy").getValue(String.class);
                           int tp=0;
                           int tr=0;
                           if (totalRecharge!=null){
                               tr=Integer.parseInt(totalRecharge);
                           }
                           if (totalProduct!=null){
                               tp=Integer.parseInt(totalProduct);
                           }
                           availableRecharge=tr-tp;
                           //availableRecharge=Integer.parseInt(totalRecharge)-Integer.parseInt(totalProduct);
                          // Toast.makeText(context, ""+availableRecharge, Toast.LENGTH_SHORT).show();
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


               Objects.requireNonNull(buy).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
             //   database
                       Calendar c = Calendar.getInstance();
                       @SuppressLint("SimpleDateFormat")

                       SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");// HH:mm:ss");
                       String reg_date = df.format(c.getTime());


                       c.add(Calendar.DATE, Integer.parseInt(product.getDays()));  // number of days to add
                       String end_date = df.format(c.getTime());
                       String uuid="PI"+System.currentTimeMillis();

                       if (!product.getValue().equals("Pre Sale")){
                           int amount= Integer.parseInt(product.getValue());
                       if (amount<= availableRecharge){
                           DatabaseReference references= FirebaseDatabase.getInstance().getReference("Product");
                           Product product1=new Product(product.getName(),product.getDays(),product.getDailyProfit(),product.getValue(),0,reg_date,end_date,reg_date,uuid,false);
                           references.child(userMobileNumber).child(uuid).setValue(product1).addOnSuccessListener(unused -> {
                               DatabaseReference reference=FirebaseDatabase.getInstance().getReference("totalAmount");
                               reference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if (snapshot.exists()){
                                           String totalProduct=snapshot.child("productBuy").getValue(String.class);
                                           int tp=0;
                                           if (totalProduct!=null){
                                               tp=Integer.parseInt(totalProduct);
                                           }
                                           int buy= tp+Integer.parseInt(product.getValue());
                                           HashMap<String,Object> map=new HashMap<>();
                                           map.put("productBuy",String.valueOf(buy));
                                           reference.child(userMobileNumber).updateChildren(map);
                                           mBottomSheetDialog.cancel();
                                           Toast.makeText(context, "Product Successfully Buy", Toast.LENGTH_SHORT).show();
                                       }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });

                           });
                       }
                       else {
                           Snackbar.make(v,"Please Recharge",Snackbar.LENGTH_SHORT).show();
                       }
                       }
                       else {
                           Snackbar.make(v,"This Product Currently Not available",Snackbar.LENGTH_SHORT).show();
                       }

                       //


                   }
               });
           }
       });
    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton button;
        TextView amount,name,time,daily;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.buy_product_button);
            amount=itemView.findViewById(R.id.product_amount);
            name=itemView.findViewById(R.id.product_name);
            time=itemView.findViewById(R.id.product_days);
            daily=itemView.findViewById(R.id.product_value);
            image=itemView.findViewById(R.id.product_image);
        }
    }
}
