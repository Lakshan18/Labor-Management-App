package com.example.labor_management_app.ui.Payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labor_management_app.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ItemViewHolder> {

    private List<Payment> paymentList;

    public PaymentAdapter(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Payment currentItem = paymentList.get(position);
        holder.tvLNo.setText(String.valueOf(currentItem.getLNo()));
        holder.tvName.setText(currentItem.getName());
        holder.tvAmount.setText(currentItem.getAmount());
        holder.tvDayCount.setText(String.valueOf(currentItem.getDayCount()));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvLNo, tvName, tvAmount, tvDayCount;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLNo = itemView.findViewById(R.id.tvLNo);
            tvName = itemView.findViewById(R.id.tvName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDayCount = itemView.findViewById(R.id.tvDayCount);
        }
    }
}
