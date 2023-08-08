package com.rathaur.earning.Java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.rathaur.earning.Modal.Withdraw;
import com.rathaur.earning.R;

import java.util.ArrayList;
import java.util.List;

public class WithdrawAdepter extends RecyclerView.Adapter<WithdrawAdepter.ViewHolder> {

    List<Withdraw> withdraws=new ArrayList<>();
    Context context;

    public WithdrawAdepter(List<Withdraw> withdraws, Context context) {
        this.withdraws = withdraws;
        this.context = context;
    }

    @NonNull
    @Override
    public WithdrawAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_sample,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WithdrawAdepter.ViewHolder holder, int position) {
       Withdraw withdraw=withdraws.get(position);
       holder.id.setText(withdraw.getId());
       holder.amount.setText(withdraw.getAmount());
     //  String type=withdraw.getStatus();
       if (withdraw.getStatus().equals("1")){
           holder.status.setText("Successful");
       }
       else if (withdraw.getStatus().equals("00")) {
          holder.status.setText("Pending");
       }
       else {
           holder.status.setText("Reject");
       }
    }

    @Override
    public int getItemCount() {
        return withdraws.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView id,amount,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.sample_withdraw_id);
            amount=itemView.findViewById(R.id.sample_withdraw_amount);
            status=itemView.findViewById(R.id.sample_withdraw_status);
        }
    }
}
