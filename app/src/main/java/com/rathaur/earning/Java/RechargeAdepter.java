package com.rathaur.earning.Java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rathaur.earning.Modal.Recharge;
import com.rathaur.earning.R;

import java.util.ArrayList;
import java.util.List;

public class RechargeAdepter extends RecyclerView.Adapter<RechargeAdepter.ViewHolder> {
    List<Recharge> rechargeList=new ArrayList<>();
    Context context;

    public RechargeAdepter(List<Recharge> rechargeList, Context context) {
        this.rechargeList = rechargeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RechargeAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeAdepter.ViewHolder holder, int position) {
      Recharge recharge=rechargeList.get(position);
        holder.id.setText(recharge.getTime());
        holder.amount.setText(recharge.getAmount());

        // suc=1
        // cen=0
        // rev=00
        // expire=11
        if (recharge.getStatus().equals("1") || recharge.getStatus().equals("11")){
            holder.status.setText("Successful");
        }

        else if (recharge.getStatus().equals("00")){
            holder.status.setText("Review");
        }
        else {
            holder.status.setText("Cencel");
        }
    }

    @Override
    public int getItemCount() {
        return rechargeList.size();
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
