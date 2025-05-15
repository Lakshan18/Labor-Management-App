package com.example.labor_management_app.ui.Payments;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labor_management_app.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ItemViewHolder> {

    private List<Payment> paymentList;
    private Context context;

    public PaymentAdapter(List<Payment> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
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

        // <<< ......Long Press Listener......>>>>
        holder.itemView.setOnLongClickListener(v -> {
            showLaborDetailsDialog(currentItem);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    private void showLaborDetailsDialog(Payment payment) {

        if (payment == null || payment.getWorkDays() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Labor Salary Details");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_labor_details, null);
        builder.setView(dialogView);

        // Initialize views
        TextView tvName = dialogView.findViewById(R.id.tvLaborName);
        TextView tvDays = dialogView.findViewById(R.id.tvAttendedDays);
        TextView tvPayoutPeriod = dialogView.findViewById(R.id.tvPayoutPeriod);
        TextView tvNextPayout = dialogView.findViewById(R.id.tvNextPayoutDate);
        ListView lvDaily = dialogView.findViewById(R.id.lvDailyBreakdown);
        TextView tvTotal = dialogView.findViewById(R.id.tvTotalEarnings);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmSalary);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        // Set basic info
        tvName.setText(payment.getName());
        tvDays.setText("Attended Days: " + payment.getDayCount());
        tvTotal.setText("Total: " + payment.getAmount());

        // Calculate payout info
        String payoutInfo = calculatePayoutPeriod(payment.getWorkDays());
        tvPayoutPeriod.setText("Payout Period: " + payoutInfo);
        tvNextPayout.setText("Next Payout: " + getNextPayoutDate(payment.getWorkDays()));

        // Daily breakdown adapter
        ArrayAdapter<Payment.WorkDay> adapter = new ArrayAdapter<Payment.WorkDay>(
                context, R.layout.item_daily_salary, payment.getWorkDays()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_daily_salary, parent, false);
                }

                Payment.WorkDay day = getItem(position);
                TextView tvDate = convertView.findViewById(R.id.tvDate);
                TextView tvHours = convertView.findViewById(R.id.tvHours);
                TextView tvSalary = convertView.findViewById(R.id.tvSalary);

                tvDate.setText(day.date);
                tvHours.setText(day.workingHours);
                tvSalary.setText(String.format("₹%.2f", day.dailySalary));

                return convertView;
            }
        };
        lvDaily.setAdapter(adapter);
        AlertDialog dialog = builder.create();

        btnConfirm.setVisibility(payment.isSalaryConfirmed() ? View.GONE : View.VISIBLE);
        btnConfirm.setOnClickListener(v -> {
            payment.setSalaryConfirmed(true);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private String calculateSalaryBreakdown(String workingHours) {
        return "7-8AM: ₹200 | 8AM-5PM: ₹1200";
    }

    private String calculatePayoutPeriod(List<Payment.WorkDay> workDays) {
        String firstDay = workDays.get(0).date;
        String lastDay = workDays.get(workDays.size()-1).date;
        return firstDay + "-" + lastDay;
    }

    private String getNextPayoutDate(List<Payment.WorkDay> workDays) {
        String lastDay = workDays.get(workDays.size()-1).date;
        switch(lastDay) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
                return "Thursday";
            case "Thursday":
            case "Friday":
            case "Saturday":
                return "Next Tuesday";
            default:
                return "Unknown";
        }
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
