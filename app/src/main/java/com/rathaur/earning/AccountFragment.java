package com.rathaur.earning;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rathaur.earning.Java.DialyAdepter;
import com.rathaur.earning.Modal.Product;
import com.rathaur.earning.Modal.Recharge;
import com.rathaur.earning.Modal.Withdraw;
import com.rathaur.earning.databinding.FragmentAccountBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    int totalBalance;
    DialyAdepter adepter;
    int totalWithdraw=0;
    int totalWithdrawSumDaily;
    String userMobileNumber;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAccountBinding.inflate(inflater,container,false);

        SharedPreferences sharedPreferences= requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userMobileNumber=sharedPreferences.getString("mobile","");

        binding.userRecharge.setText("₹ "+"0");
        binding.userBalance.setText("₹ "+"0");

        Dialog dialog=new Dialog(requireContext());
        dialog.setContentView(R.layout.recharge_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        // withdraw
        Dialog dialog1=new Dialog(requireContext());
        dialog1.setContentView(R.layout.wirhdraw_dailog);
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        Window window1 = dialog1.getWindow();
        Objects.requireNonNull(window1).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        DatabaseReference db=FirebaseDatabase.getInstance().getReference("totalAmount");
        db.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int referInt=0;
                    int dailyInt=0;
                    int withdrawInt=0;
                    String refer=snapshot.child("refer").getValue(String.class);
                    String daily=snapshot.child("daily").getValue(String.class);
                    String withdraw=snapshot.child("withdraw").getValue(String.class);
                    if (refer!=null){
                     referInt=Integer.parseInt(refer);
                    }
                    if(daily!=null) {
                        dailyInt=Integer.parseInt(daily);
                    }
                    if (withdraw!=null){
                       withdrawInt=Integer.parseInt(withdraw) ;
                    }
                    totalBalance=referInt+dailyInt-withdrawInt;
                    binding.userBalance.setText("₹ "+totalBalance);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rechargeButton.setOnClickListener(v -> {
            dialog.show();
            TextView rt1=dialog.findViewById(R.id.recharge_amount_1);
            TextView rt2=dialog.findViewById(R.id.recharge_amount_2);
            TextView rt3=dialog.findViewById(R.id.recharge_amount_3);
            TextView rt4=dialog.findViewById(R.id.recharge_amount_4);
            TextView rt5=dialog.findViewById(R.id.recharge_amount_5);
            TextView rt6=dialog.findViewById(R.id.recharge_amount_6);
            EditText amount=dialog.findViewById(R.id.recharge_amount_edit_text);
            Button cancel=dialog.findViewById(R.id.recharge_cancel_button);
            Button recharge=dialog.findViewById(R.id.recharge_confirm_button);
            rt1.setOnClickListener(v1 -> amount.setText("220"));
            rt2.setOnClickListener(v1 -> amount.setText("470"));
            rt3.setOnClickListener(v1 -> amount.setText("1700"));
            rt4.setOnClickListener(v1 -> amount.setText("3700"));
            rt5.setOnClickListener(v1 -> amount.setText("20000"));
            rt6.setOnClickListener(v1 -> amount.setText("50000"));


            cancel.setOnClickListener(v12 -> dialog.dismiss());
            recharge.setOnClickListener(v13 -> {
                if (!amount.getText().toString().trim().isEmpty()) {
                    int amou = Integer.parseInt(amount.getText().toString());
                    if (amou >= 220) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recharge");
                        databaseReference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String orderId = "TT" + System.currentTimeMillis();
                                Recharge rech = new Recharge(userMobileNumber, amount.getText().toString(), orderId, "0");
                                databaseReference.child(userMobileNumber).child(orderId).setValue(rech);
                                Intent intent = new Intent(getContext(), RechargeQrCode.class);
                                intent.putExtra("amount", String.valueOf(amou));
                                intent.putExtra("orderId", orderId);
                                startActivity(intent);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Snackbar.make(v13, "Amount is very small ", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(v13, "Enter valid amount ", Snackbar.LENGTH_SHORT).show();
                   // Toast.makeText(getContext(), "Enter valid amount", Toast.LENGTH_SHORT).show();
                }
            });

        });

       binding.withdrawButton.setOnClickListener(v -> {
           dialog1.show();
           TextView inform=dialog1.findViewById(R.id.account_information);
           EditText amount=dialog1.findViewById(R.id.withdraw_amount);
           EditText password=dialog1.findViewById(R.id.withdraw_password);
           Button cancel=dialog1.findViewById(R.id.withdraw_cancel_button);
           Button withdraw=dialog1.findViewById(R.id.withdraw_confirm_button);
           TextView all_amount=dialog1.findViewById(R.id.withdrawal_all_amount);


           DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Bank");
           reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
               @SuppressLint("SetTextI18n")
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       String name=snapshot.child("name").getValue(String.class);
                       String account=snapshot.child("accountNumber").getValue(String.class);
                       inform.setText(name+"("+account+")");
                       all_amount.setText("₹ "+totalBalance);
                    }
                   else {
                       startActivity(new Intent(getContext(), BankCard.class));
                       totalBalance=totalBalance-totalWithdraw;
                       all_amount.setText("₹ "+totalBalance);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
           cancel.setOnClickListener(v14 -> dialog1.dismiss());
           withdraw.setOnClickListener(v15 -> {
               DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
               reference1.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){

                           String pass=password.getText().toString();
                           String amoun=amount.getText().toString();
                           String withdrawPass=snapshot.child("withdrawPassword").getValue(String.class);
                           assert withdrawPass != null;
                          int amountInt=Integer.parseInt(amoun);
                           if (withdrawPass.equals(pass)){
                               if (totalBalance >= amountInt && amountInt>=230){
                                   withdraw(amoun,dialog1,v);
                               }
                               else {
                                   Snackbar.make(v15,"Withdraw amount not is exist",Snackbar.LENGTH_SHORT).show();
                               }
                           }
                           else {
                               Snackbar.make(v15,"Withdraw password wrong",Snackbar.LENGTH_SHORT).show();
                           }
                       }


                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           });

       });


       binding.userAddBackAccount.setOnClickListener(v -> startActivity(new Intent(getContext(),BankCard.class)));
       binding.userGift.setOnClickListener(v -> {
          msgTelegram();
         //  SendMessagesHelper.getInstance().sendMessage("Hi there", to_user_id, null, null, false, null, null, null);
       });
       binding.records.setOnClickListener(v -> startActivity(new Intent(getContext(), Records.class)));
       binding.myTeam.setOnClickListener(v -> startActivity(new Intent(getContext(), Referral.class)));
       binding.changePassword.setOnClickListener(v -> startActivity(new Intent(getContext(),Password.class)));
       binding.referral.setOnClickListener(v -> {
               SupportFragment supportFragment=new SupportFragment();
               FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.frameLayout, supportFragment);
               fragmentTransaction.commit();
       });
       binding.purchased.setOnClickListener(v -> startActivity(new Intent(getContext(), Purchased.class)));

       binding.rechargeDetails.setOnClickListener(v -> startActivity(new Intent(getContext(),RechageData.class)));
       binding.dailyCheckInRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
       binding.logout.setOnClickListener(view -> {
           SharedPreferences sharedPreferences1= requireContext().getSharedPreferences("user",Context.MODE_PRIVATE);
           SharedPreferences.Editor editor=sharedPreferences1.edit();
           editor.clear();
           editor.apply();
           startActivity(new Intent(getContext(), LoginAccount.class));
           requireActivity().finish();
       });

        getUserData();
        getRechageData();
        getCheckIn();
        setWithdrawData();
        setRechargeData();




        return binding.getRoot();
    }

    public void setWithdrawData(){
         DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Withdraw");
         reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                    int totalWithdraw=0;
                     for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                         Withdraw withdraw=dataSnapshot.getValue(Withdraw.class);
                         assert withdraw != null;
                         if (withdraw.getStatus().equals("00") || withdraw.getStatus().equals("1")){
                             int amount= Integer.parseInt(withdraw.getAmount());
                             totalWithdraw=totalWithdraw+amount;
                             DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("totalAmount");
                             HashMap<String ,Object> map=new HashMap<>();
                             map.put("withdraw",String.valueOf(totalWithdraw));
                             databaseReference.child(userMobileNumber).updateChildren(map);
                         }
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }

    private void setRechargeData() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("totalAmount");
        databaseReference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int availableRecharge;
                if (snapshot.exists()){
                   String  totalRecharge=snapshot.child("recharge").getValue(String.class);
                   String  totalProduct=snapshot.child("productBuy").getValue(String.class);
                   int tp=0;
                   int tr=0;
                    if (totalRecharge!=null){
                       tr=Integer.parseInt(totalRecharge);
                    }
                    if (totalProduct!=null){
                      tp=Integer.parseInt(totalProduct);
                    }
                    availableRecharge=tr-tp;
                    binding.userRecharge.setText("₹ "+availableRecharge);
                }
                else {
                    binding.userRecharge.setText("₹ "+0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCheckIn() {


        List<Product > productList=new ArrayList<>();
       DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Product");
        reference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                if (snapshot.exists()) {
                    Calendar c = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String reg_date = df.format(c.getTime());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Product product = dataSnapshot.getValue(Product.class);
                        assert product != null;
                        String date = product.getIsCheck();

                        if (reg_date.compareTo(date) !=0 && !product.isExpire()){
                            productList.add(product);
                        }


                    }
                    adepter = new DialyAdepter(productList, getContext());
                    adepter.notifyDataSetChanged();
                    binding.dailyCheckInRecycler.setAdapter(adepter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void withdraw(String amount,Dialog dialog,View v) {
        String uuid="WW"+System.currentTimeMillis();
      DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Withdraw");
      reference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              Withdraw withdraw=new Withdraw(uuid,amount,userMobileNumber,"00");
              reference.child(userMobileNumber).child(uuid).setValue(withdraw).addOnSuccessListener(unused -> {
                dialog.dismiss();
                  Snackbar.make(v,"Withdraw Successfully Submit",Snackbar.LENGTH_SHORT).show();
              });
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }

    private void getRechageData() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Recharge");
        databaseReference.child(userMobileNumber).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int totalRecharge=0;
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                       Recharge recharge=dataSnapshot.getValue(Recharge.class);
                        assert recharge != null;
                        if (recharge.getStatus().equals("1")){
                            int totalAmount= Integer.parseInt(recharge.getAmount());
                            totalRecharge=totalRecharge+totalAmount;
                        }

                    }
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("totalAmount");
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("recharge",String.valueOf(totalRecharge));
                    reference.child(userMobileNumber).updateChildren(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.userId.setText(snapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void msgTelegram() {
        final String id = "https://telegram.me/iamashishrathaur/";
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        startActivity(myIntent);

        }
}